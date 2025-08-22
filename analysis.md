# Analyse the BAD query

```postgresql
    SELECT
        t.employee_id,
        (SELECT name FROM employee e WHERE e.id = t.employee_id) AS employee_name,
        (SELECT name FROM project p WHERE p.id = t.project_id) AS project_name,
        SUM(EXTRACT(EPOCH FROM (t.time_to - t.time_from)) / 3600) AS total_hours
    FROM time_record t
    WHERE t.time_from >= NOW() - INTERVAL '1000 month'
    GROUP BY
        t.employee_id,
        (SELECT name FROM employee e WHERE e.id = t.employee_id),
        (SELECT name FROM project p WHERE p.id = t.project_id)
    ORDER BY
            (SELECT name FROM employee e WHERE e.id = t.employee_id),
            (SELECT name FROM project p WHERE p.id = t.project_id);
```

## Problems
To better showing how bad this query is, I'm populating an amount of data to these tables:

- 100 projects
- 500 employees
- 1 million time records which is random combination of those projects and employees

Result of the ANALYZE query:
```text
GroupAggregate  (cost=7880245.26..8421960.86 rows=50000 width=592) (actual time=8895.782..9243.127 rows=50000 loops=1)
"  Group Key: ((SubPlan 1)), ((SubPlan 2)), t.employee_id"
  ->  Sort  (cost=7880245.26..7882006.59 rows=704530 width=576) (actual time=8895.756..9011.176 rows=703909 loops=1)
"        Sort Key: ((SubPlan 1)), ((SubPlan 2)), t.employee_id"
        Sort Method: external merge  Disk: 39968kB
        ->  Seq Scan on time_record t  (cost=0.00..7450599.20 rows=704530 width=576) (actual time=0.060..6262.316 rows=703909 loops=1)
              Filter: (time_from >= (now() - '83 years 4 mons'::interval))
              Rows Removed by Filter: 296091
              SubPlan 1
                ->  Index Scan using employee_pkey on employee e  (cost=0.27..8.29 rows=1 width=12) (actual time=0.001..0.001 rows=1 loops=703909)
                      Index Cond: (id = t.employee_id)
              SubPlan 2
                ->  Seq Scan on project p  (cost=0.00..2.25 rows=1 width=10) (actual time=0.003..0.005 rows=1 loops=703909)
                      Filter: (id = t.project_id)
                      Rows Removed by Filter: 99
Planning Time: 0.349 ms
Execution Time: 9252.760 ms
```

Pay attention to the cost of the root GroupAggregate. It currently predicted as 7880245.26..8421960.86. It has no meaning right now until we actually fine tune this query and do comparisons. 

But anyway, the query takes about `9` seconds to run on my machine

### First problem: Using sub-queries on SELECT
The leaf nodes SubPlan 1 and SubPlan 2 indicate that there are 2 scans are performed for each result returned by the WHERE. Imagine that the WHERE clause returns a thousand records, then a thousand index scan on `employee` table and a thousand sequential scan on `project` table will be performed.

Luckily for us, we are selecting `id` and it's primary key so postgres is smart enough to use Index Scan for `employee` table.

### Second problem: Using sub-queries on ORDER BY
Basically the same thing. Look at the cost for the SORT, it's taking basically the most.

### SOLUTION: Using JOIN
```postgresql
SELECT tr.employee_id, 
       e.name, 
       p.name, 
       SUM(EXTRACT(EPOCH FROM (tr.time_to - tr.time_from)) / 3600) AS total_hours
FROM xphr.employee e
    JOIN xphr.time_record tr on e.id = tr.employee_id
    JOIN xphr.project p on tr.project_id = p.id
WHERE tr.time_from >= NOW() - INTERVAL '1000 month'
GROUP BY tr.employee_id, e.name, p.name
ORDER BY e.name, p.name;
```

ANALYZE result:

```text
GroupAggregate  (cost=118771.89..141669.12 rows=704530 width=58) (actual time=2880.631..3221.355 rows=50000 loops=1)
"  Group Key: e.name, p.name, tr.employee_id"
  ->  Sort  (cost=118771.89..120533.22 rows=704530 width=42) (actual time=2880.602..2995.530 rows=703908 loops=1)
"        Sort Key: e.name, p.name, tr.employee_id"
        Sort Method: external merge  Disk: 39968kB
        ->  Hash Join  (cost=18.50..28664.33 rows=704530 width=42) (actual time=0.253..361.027 rows=703908 loops=1)
              Hash Cond: (tr.project_id = p.id)
              ->  Hash Join  (cost=15.25..26733.32 rows=704530 width=36) (actual time=0.139..277.856 rows=703908 loops=1)
                    Hash Cond: (tr.employee_id = e.id)
                    ->  Seq Scan on time_record tr  (cost=0.00..24853.00 rows=704530 width=24) (actual time=0.017..181.117 rows=703908 loops=1)
                          Filter: (time_from >= (now() - '83 years 4 mons'::interval))
                          Rows Removed by Filter: 296092
                    ->  Hash  (cost=9.00..9.00 rows=500 width=20) (actual time=0.108..0.110 rows=500 loops=1)
                          Buckets: 1024  Batches: 1  Memory Usage: 36kB
                          ->  Seq Scan on employee e  (cost=0.00..9.00 rows=500 width=20) (actual time=0.010..0.055 rows=500 loops=1)
              ->  Hash  (cost=2.00..2.00 rows=100 width=18) (actual time=0.090..0.091 rows=100 loops=1)
                    Buckets: 1024  Batches: 1  Memory Usage: 14kB
                    ->  Seq Scan on project p  (cost=0.00..2.00 rows=100 width=18) (actual time=0.057..0.066 rows=100 loops=1)
Planning Time: 0.428 ms
Execution Time: 3229.967 ms
```

First, notice at the root cost, it's now 118771.89..141669.12 which is a lot better than the original one (7880245.26..8421960.86).

And YAY, it only takes about `3` seconds.
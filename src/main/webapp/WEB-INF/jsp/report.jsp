<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<meta charset="UTF-8">
<title>Page Title</title>
<meta name="viewport" content="width=device-width,initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js" integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q" crossorigin="anonymous"></script>
<body>
<header class="d-flex flex-wrap justify-content-center py-3 mb-4 border-bottom container">
    <a href="/xphr-demo" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-dark text-decoration-none">
        <span class="fs-4">xpHR</span>
    </a>

    <ul class="nav">
        <li class="nav-item">Hello, ${user} &nbsp;&nbsp;</li>
        <li class="nav-item"><a href="/xphr-demo/logout">Logout</a></li>
    </ul>
</header>
<div class="container">
    <div class="row">
        <div class="col-12">
            <h1>REPORT</h1>
        </div>
    </div>
    <form:form action="/xphr-demo/report" method="GET" modelAttribute="request" class="row g-3 align-items-center">
        <div class="col-auto">
            <label for="from" class="col-form-label">From</label>
        </div>
        <div class="col-auto">
            <form:input path="from" value="${from}" type="datetime-local" class="form-control"/>
        </div>
        <div class="col-auto">
            <label for="to" class="col-form-label">From</label>
        </div>
        <div class="col-auto">
            <form:input path="to" value="${to}" type="datetime-local" class="form-control"/>
        </div>
        <div class="col-auto">
            <button type="submit" class="btn btn-primary" id="search">Search</button>
        </div>
        <c:if test="${error != null}">
            <div class="col-auto">
                <span class="text-danger">${error}</span>
            </div>
        </c:if>
    </form:form>
    <div class="row">
        <div class="col-12">
            <div class="table-responsive">
                <table class="table table-striped table-sm">
                    <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">User</th>
                        <th scope="col">Project</th>
                        <th scope="col">Total Hours</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var = "i" begin = "1" end = "${report.size()}">
                        <tr>
                            <td>${i}</td>
                            <td>${report.get(i - 1).getUser()}</td>
                            <td>${report.get(i - 1).getProject()}</td>
                            <td>${report.get(i - 1).getHours()}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="pagination float-right">
                <nav>
                    <ul class="pagination">
                        <c:if test="${page > 2}">
                            <li class="page-item"><a class="page-link" href="/xphr-demo/report?page=1&from=${request.from}&to=${request.to}">1</a></li>
                            <li class="page-item"><a class="page-link" href="#">...</a></li>
                        </c:if>
                        <c:if test="${page > 1}">
                            <li class="page-item"><a class="page-link" href="/xphr-demo/report?page=${page - 1}&from=${request.from}&to=${request.to}">${page - 1}</a></li>
                        </c:if>
                        <li class="page-item active"><a class="page-link" href="#">${page}</a></li>
                        <c:if test="${page < maxPage}">
                            <li class="page-item"><a class="page-link" href="/xphr-demo/report?page=${page + 1}&from=${request.from}&to=${request.to}">${page + 1}</a></li>
                        </c:if>
                        <c:if test="${page < (maxPage - 1)}">
                            <li class="page-item"><a class="page-link" href="#">...</a></li>
                            <li class="page-item"><a class="page-link" href="/xphr-demo/report?page=${maxPage}&from=${request.from}&to=${request.to}">${maxPage}</a></li>
                        </c:if>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</div>
</body>
</html>
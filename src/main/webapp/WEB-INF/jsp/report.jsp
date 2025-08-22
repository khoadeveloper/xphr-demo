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
    <a href="/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-dark text-decoration-none">
        <span class="fs-4">xpHR</span>
    </a>

    <ul class="nav">
        <li class="nav-item">Hello, ${user}</li>
    </ul>
</header>
<div class="container">
    <div class="row">
        <div class="col-12">
            <h1>REPORT</h1>
        </div>
    </div>
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
        </div>
    </div>
</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Đăng nhập</title></head>
<body>
<h2>Đăng nhập</h2>
<c:if test="${not empty error}"><p style="color:red">${error}</p></c:if>
<form method="post" action="${pageContext.request.contextPath}/login">
    <p>Username: <input name="username" required></p>
    <p>Password: <input name="password" type="password" required></p>
    <button type="submit">Đăng nhập</button>
</form>
<p>Tài khoản mẫu: <b>admin / 123456</b></p>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Lab 7 - CRUD MVC</title></head>
<body>
<h2>Lab 7 - CRUD bằng Servlet + JSP, dùng MVC đơn giản</h2>
<ul>
    <li><a href="${pageContext.request.contextPath}/admin/sinh-vien">Quản lý sinh viên</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/sach">Quản lý sách</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/san-pham">Quản lý sản phẩm</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/lop-hoc">Quản lý lớp học</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/diem">Quản lý điểm sinh viên</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/gio-hang">Giỏ hàng</a></li>
    <li><a href="${pageContext.request.contextPath}/login">Đăng nhập</a></li>
</ul>
</body>
</html>

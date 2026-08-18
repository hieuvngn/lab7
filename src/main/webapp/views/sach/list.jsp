<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Danh sách sách</title></head>
<body>
<%@ include file="/views/menu.jsp" %>
<h2>Danh sách sách</h2>
<form method="get" action="${pageContext.request.contextPath}/admin/sach">
    <input name="keyword" placeholder="Tìm theo tên hoặc tác giả" value="${param.keyword}">
    <button type="submit">Tìm</button>
</form>
<p><a href="${pageContext.request.contextPath}/admin/sach?action=new">Thêm sách</a></p>
<table border="1" cellpadding="6">
    <tr><th>ID</th><th>Mã sách</th><th>Tên sách</th><th>Tác giả</th><th>NXB</th><th>Năm XB</th><th>Thao tác</th></tr>
    <c:forEach var="s" items="${dsSach}">
        <tr>
            <td>${s.id}</td><td>${s.maSach}</td><td>${s.tenSach}</td><td>${s.tacGia}</td>
            <td>${s.nhaXuatBan}</td><td>${s.namXuatBan}</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/sach?action=edit&id=${s.id}">Sửa</a> |
                <a href="${pageContext.request.contextPath}/admin/sach?action=delete&id=${s.id}" onclick="return confirm('Xóa?')">Xóa</a>
            </td>
        </tr>
    </c:forEach>
</table>
<c:if test="${empty dsSach}"><p>Không tìm thấy sách.</p></c:if>
</body>
</html>

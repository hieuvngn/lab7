<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Danh sách sản phẩm</title></head>
<body>
<%@ include file="/views/menu.jsp" %>
<h2>Danh sách sản phẩm</h2>
<form method="get" action="${pageContext.request.contextPath}/admin/san-pham">
    <input name="keyword" placeholder="Tìm theo mã hoặc tên" value="${param.keyword}">
    <button type="submit">Tìm</button>
</form>
<p><a href="${pageContext.request.contextPath}/admin/san-pham?action=new">Thêm sản phẩm</a></p>
<table border="1" cellpadding="6">
    <tr><th>ID</th><th>Mã SP</th><th>Tên</th><th>Mô tả</th><th>Giá</th><th>Số lượng</th><th>Thao tác</th></tr>
    <c:forEach var="sp" items="${dsSanPham}">
        <tr>
            <td>${sp.id}</td><td>${sp.maSanPham}</td><td>${sp.tenSanPham}</td><td>${sp.moTa}</td>
            <td>${sp.gia}</td><td>${sp.soLuong}</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/gio-hang?action=add&id=${sp.id}">Thêm vào giỏ</a> |
                <a href="${pageContext.request.contextPath}/admin/san-pham?action=edit&id=${sp.id}">Sửa</a> |
                <a href="${pageContext.request.contextPath}/admin/san-pham?action=delete&id=${sp.id}" onclick="return confirm('Xóa?')">Xóa</a>
            </td>
        </tr>
    </c:forEach>
</table>
<c:if test="${empty dsSanPham}"><p>Không tìm thấy sản phẩm.</p></c:if>
</body>
</html>

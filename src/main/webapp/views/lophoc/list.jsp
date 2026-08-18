<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Danh sách lớp học</title></head>
<body>
<%@ include file="/views/menu.jsp" %>
<h2>Danh sách lớp học</h2>
<form method="get" action="${pageContext.request.contextPath}/admin/lop-hoc">
    <input name="keyword" placeholder="Tìm theo mã hoặc tên lớp" value="${param.keyword}">
    <button type="submit">Tìm</button>
</form>
<p><a href="${pageContext.request.contextPath}/admin/lop-hoc?action=new">Thêm lớp học</a></p>
<table border="1" cellpadding="6">
    <tr><th>ID</th><th>Mã lớp</th><th>Tên lớp</th><th>Cố vấn học tập</th><th>Số lượng SV</th><th>Thao tác</th></tr>
    <c:forEach var="l" items="${dsLop}">
        <tr>
            <td>${l.id}</td><td>${l.maLop}</td><td>${l.tenLop}</td><td>${l.coVanHocTap}</td><td>${l.soLuongSinhVien}</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/lop-hoc?action=edit&id=${l.id}">Sửa</a> |
                <a href="${pageContext.request.contextPath}/admin/lop-hoc?action=delete&id=${l.id}" onclick="return confirm('Xóa?')">Xóa</a>
            </td>
        </tr>
    </c:forEach>
</table>
<c:if test="${empty dsLop}"><p>Không tìm thấy lớp học.</p></c:if>
</body>
</html>

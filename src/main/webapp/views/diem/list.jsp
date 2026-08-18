<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Điểm sinh viên</title></head>
<body>
<%@ include file="/views/menu.jsp" %>
<h2>Điểm sinh viên</h2>
<form method="get" action="${pageContext.request.contextPath}/admin/diem">
    <input name="keyword" placeholder="Tìm theo mã SV" value="${param.keyword}">
    <button type="submit">Tìm</button>
</form>
<p><a href="${pageContext.request.contextPath}/admin/diem?action=new">Nhập điểm</a></p>
<table border="1" cellpadding="6">
    <tr><th>ID</th><th>Mã SV</th><th>Chuyên cần</th><th>Giữa kỳ</th><th>Cuối kỳ</th><th>Tổng kết</th><th>Xếp loại</th><th>Thao tác</th></tr>
    <c:forEach var="d" items="${dsDiem}">
        <tr>
            <td>${d.id}</td><td>${d.maSinhVien}</td><td>${d.chuyenCan}</td><td>${d.giuaKy}</td><td>${d.cuoiKy}</td>
            <td>${d.tongKet}</td><td>${d.xepLoai}</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/diem?action=edit&id=${d.id}">Sửa</a> |
                <a href="${pageContext.request.contextPath}/admin/diem?action=delete&id=${d.id}" onclick="return confirm('Xóa?')">Xóa</a>
            </td>
        </tr>
    </c:forEach>
</table>
<c:if test="${empty dsDiem}"><p>Chưa có dữ liệu điểm.</p></c:if>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Form lớp học</title></head>
<body>
<%@ include file="/views/menu.jsp" %>
<h2>Form lớp học</h2>
<form method="post" action="${pageContext.request.contextPath}/admin/lop-hoc">
    <input type="hidden" name="id" value="${lop.id}">
    <p>Mã lớp: <input name="maLop" value="${lop.maLop}" required></p>
    <p>Tên lớp: <input name="tenLop" value="${lop.tenLop}" required></p>
    <p>Cố vấn học tập: <input name="coVanHocTap" value="${lop.coVanHocTap}"></p>
    <p>Số lượng SV: <input name="soLuongSinhVien" value="${lop.soLuongSinhVien}" type="number" min="0" required></p>
    <button type="submit">Lưu</button>
</form>
</body>
</html>

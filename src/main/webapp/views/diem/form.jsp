<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Form điểm sinh viên</title></head>
<body>
<%@ include file="/views/menu.jsp" %>
<h2>Nhập điểm sinh viên</h2>
<c:if test="${not empty error}"><p style="color:red">${error}</p></c:if>
<form method="post" action="${pageContext.request.contextPath}/admin/diem">
    <input type="hidden" name="id" value="${diem.id}">
    <p>Mã SV: <input name="maSinhVien" value="${diem.maSinhVien}" required></p>
    <p>Chuyên cần: <input name="chuyenCan" value="${diem.chuyenCan}" type="number" step="0.1" min="0" max="10" required></p>
    <p>Giữa kỳ: <input name="giuaKy" value="${diem.giuaKy}" type="number" step="0.1" min="0" max="10" required></p>
    <p>Cuối kỳ: <input name="cuoiKy" value="${diem.cuoiKy}" type="number" step="0.1" min="0" max="10" required></p>
    <button type="submit">Lưu</button>
</form>
</body>
</html>

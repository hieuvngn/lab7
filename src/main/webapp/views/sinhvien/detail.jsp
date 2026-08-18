<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Chi tiết sinh viên</title></head>
<body>
<%@ include file="/views/menu.jsp" %>
<h2>Chi tiết sinh viên</h2>
<p><b>ID:</b> ${sv.id}</p>
<p><b>Mã SV:</b> ${sv.maSinhVien}</p>
<p><b>Họ tên:</b> ${sv.hoTen}</p>
<p><b>Email:</b> ${sv.email}</p>
<p><b>Lớp:</b> ${sv.lop}</p>
<p><a href="${pageContext.request.contextPath}/admin/sinh-vien">Quay lại danh sách</a></p>
</body>
</html>

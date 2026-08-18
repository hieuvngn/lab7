<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Form sách</title></head>
<body>
<%@ include file="/views/menu.jsp" %>
<h2>Form sách</h2>
<form method="post" action="${pageContext.request.contextPath}/admin/sach">
    <input type="hidden" name="id" value="${sach.id}">
    <p>Mã sách: <input name="maSach" value="${sach.maSach}" required></p>
    <p>Tên sách: <input name="tenSach" value="${sach.tenSach}" required></p>
    <p>Tác giả: <input name="tacGia" value="${sach.tacGia}" required></p>
    <p>Nhà xuất bản: <input name="nhaXuatBan" value="${sach.nhaXuatBan}"></p>
    <p>Năm xuất bản: <input name="namXuatBan" value="${sach.namXuatBan}" type="number" required></p>
    <button type="submit">Lưu</button>
</form>
</body>
</html>

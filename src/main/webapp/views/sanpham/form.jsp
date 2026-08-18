<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Form sản phẩm</title></head>
<body>
<%@ include file="/views/menu.jsp" %>
<h2>Form sản phẩm</h2>
<c:if test="${not empty error}"><p style="color:red">${error}</p></c:if>
<form method="post" action="${pageContext.request.contextPath}/admin/san-pham">
    <input type="hidden" name="id" value="${sp.id}">
    <p>Mã SP: <input name="maSanPham" value="${sp.maSanPham}" required></p>
    <p>Tên: <input name="tenSanPham" value="${sp.tenSanPham}" required></p>
    <p>Mô tả: <textarea name="moTa" rows="3" cols="30">${sp.moTa}</textarea></p>
    <p>Giá: <input name="gia" value="${sp.gia}" type="number" step="0.01" min="0.01" required></p>
    <p>Số lượng: <input name="soLuong" value="${sp.soLuong}" type="number" min="0" required></p>
    <button type="submit">Lưu</button>
</form>
</body>
</html>

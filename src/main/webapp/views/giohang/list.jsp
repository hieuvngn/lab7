<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Giỏ hàng</title></head>
<body>
<%@ include file="/views/menu.jsp" %>
<h2>Giỏ hàng</h2>
<c:choose>
    <c:when test="${empty cartItems}">
        <p>Giỏ hàng trống. <a href="${pageContext.request.contextPath}/admin/san-pham">Tiếp tục mua sắm</a></p>
    </c:when>
    <c:otherwise>
        <table border="1" cellpadding="6">
            <tr><th>Mã SP</th><th>Tên</th><th>Đơn giá</th><th>Số lượng</th><th>Thành tiền</th><th>Thao tác</th></tr>
            <c:forEach var="item" items="${cartItems}">
                <tr>
                    <td>${item.sanPham.maSanPham}</td>
                    <td>${item.sanPham.tenSanPham}</td>
                    <td>${item.sanPham.gia}</td>
                    <td>
                        <form method="get" action="${pageContext.request.contextPath}/admin/gio-hang" style="margin:0">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="id" value="${item.sanPham.id}">
                            <input name="soLuong" value="${item.soLuong}" type="number" min="1" size="3">
                            <button type="submit">Cập nhật</button>
                        </form>
                    </td>
                    <td>${item.thanhTien}</td>
                    <td><a href="${pageContext.request.contextPath}/admin/gio-hang?action=remove&id=${item.sanPham.id}" onclick="return confirm('Xóa khỏi giỏ?')">Xóa</a></td>
                </tr>
            </c:forEach>
        </table>
        <p><b>Tổng tiền: ${total}</b></p>
        <p><a href="${pageContext.request.contextPath}/admin/san-pham">Tiếp tục mua sắm</a></p>
    </c:otherwise>
</c:choose>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav>
    <a href="${pageContext.request.contextPath}/">Trang chủ</a> |
    <a href="${pageContext.request.contextPath}/admin/sinh-vien">Sinh viên</a> |
    <a href="${pageContext.request.contextPath}/admin/sach">Sách</a> |
    <a href="${pageContext.request.contextPath}/admin/san-pham">Sản phẩm</a> |
    <a href="${pageContext.request.contextPath}/admin/lop-hoc">Lớp học</a> |
    <a href="${pageContext.request.contextPath}/admin/diem">Điểm SV</a> |
    <a href="${pageContext.request.contextPath}/admin/gio-hang">Giỏ hàng</a>
    <c:choose>
        <c:when test="${not empty sessionScope.username}">
            | Xin chào <b>${sessionScope.username}</b> |
            <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
        </c:when>
        <c:otherwise>
            | <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
        </c:otherwise>
    </c:choose>
</nav>
<hr>

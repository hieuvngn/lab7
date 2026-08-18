Tên bài lab Lab 7 - Xây dựng CRUD bằng Servlet + JSP, dùng MVC đơn giản

3. Cấu trúc project yêu cầu
Cấu trúc thư mục gợi ý
lab07-crud-mvc/
├── pom.xml
└── src/main/
├── java/vn/edu/eaut/lab7/
│ ├── controller/
│ ├── filter/
│ ├── model/
│ └── repository/
└── webapp/
├── index.jsp
├── login.jsp
├── views/sinhvien/
│ ├── list.jsp
│ ├── form.jsp
│ └── detail.jsp
└── WEB-INF/web.xml
4. File cấu hình Maven
pom.xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
<modelVersion>4.0.0</modelVersion>
<groupId>vn.edu.eaut</groupId>
<artifactId>lab07-crud-mvc</artifactId>
<version>1.0-SNAPSHOT</version>
<packaging>war</packaging>
<properties>
<maven.compiler.source>17</maven.compiler.source>
<maven.compiler.target>17</maven.compiler.target>
<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
<dependencies>
<dependency>
<groupId>jakarta.servlet</groupId>
<artifactId>jakarta.servlet-api</artifactId>
<version>6.0.0</version>
<scope>provided</scope>
</dependency>
<dependency>
<groupId>jakarta.servlet.jsp.jstl</groupId>
<artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
<version>3.0.0</version>
</dependency>
<dependency>
<groupId>org.glassfish.web</groupId>
<artifactId>jakarta.servlet.jsp.jstl</artifactId>
<version>3.0.1</version>
</dependency>
</dependencies>
<build><finalName>lab07-crud-mvc</finalName></build>
</project>
5. Bài tập có code gợi ý
Bài 1. Tạo trang chủ và menu điều hướng
Yêu cầu: tạo trang index.jsp có menu dẫn đến module sinh viên, sách, sản phẩm và đăng nhập.
index.jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Lab 7 - CRUD MVC</title></head>
<body>
<h2>Lab 7 - CRUD bằng Servlet + JSP, dùng MVC đơn giản</h2>
<ul>

Công nghệ Java | Lab 7 - Chương 3

Công nghệ Java - IT3242

<li><a href="${pageContext.request.contextPath}/sinh-vien">Quản lý sinh viên</a></li>
<li><a href="${pageContext.request.contextPath}/sach">Quản lý sách</a></li>
<li><a href="${pageContext.request.contextPath}/san-pham">Quản lý sản phẩm</a></li>
<li><a href="${pageContext.request.contextPath}/login.jsp">Đăng nhập</a></li>
</ul>
</body>
</html>
Bài 2. Tạo Model và Repository cho sinh viên
Yêu cầu: tạo lớp SinhVien và SinhVienRepository lưu dữ liệu bằng List trong bộ nhớ.
SinhVien.java
package vn.edu.eaut.lab7.model;
public class SinhVien {
private int id;
private String maSinhVien;
private String hoTen;
private String email;
private String lop;
public SinhVien() {}
public SinhVien(int id, String maSinhVien, String hoTen, String email, String lop) {
this.id = id; this.maSinhVien = maSinhVien; this.hoTen = hoTen;
this.email = email; this.lop = lop;
}
public int getId() { return id; }
public void setId(int id) { this.id = id; }
public String getMaSinhVien() { return maSinhVien; }
public void setMaSinhVien(String maSinhVien) { this.maSinhVien = maSinhVien; }
public String getHoTen() { return hoTen; }
public void setHoTen(String hoTen) { this.hoTen = hoTen; }
public String getEmail() { return email; }
public void setEmail(String email) { this.email = email; }
public String getLop() { return lop; }
public void setLop(String lop) { this.lop = lop; }
}
SinhVienRepository.java
package vn.edu.eaut.lab7.repository;
import vn.edu.eaut.lab7.model.SinhVien;
import java.util.*;
import java.util.stream.Collectors;
public class SinhVienRepository {
private static final List<SinhVien> data = new ArrayList<>();
private static int autoId = 3;
static {
data.add(new SinhVien(1, "20240001", "Nguyễn Văn An", "an@gmail.com", "DCCNTT15.10.1"));
data.add(new SinhVien(2, "20240002", "Trần Thị Bình", "binh@gmail.com", "DCCNTT15.10.2"));
}
public List<SinhVien> findAll() { return data; }
public SinhVien findById(int id) { return data.stream().filter(x -> x.getId() == id).findFirst().orElse(null); }
public void add(SinhVien sv) { sv.setId(autoId++); data.add(sv); }
public void update(SinhVien sv) {
SinhVien old = findById(sv.getId());
if (old != null) { old.setMaSinhVien(sv.getMaSinhVien()); old.setHoTen(sv.getHoTen()); old.setEmail(sv.getEmail());
old.setLop(sv.getLop()); }
}
public void delete(int id) { data.removeIf(x -> x.getId() == id); }
public List<SinhVien> search(String key) {
if (key == null || key.trim().isEmpty()) return data;
String k = key.toLowerCase();
return data.stream().filter(x -> x.getHoTen().toLowerCase().contains(k) ||
x.getLop().toLowerCase().contains(k)).collect(Collectors.toList());
}
}
Bài 3. Tạo Controller xử lý CRUD sinh viên
Yêu cầu: Servlet xử lý action list, new, edit, detail, delete; POST dùng cho lưu dữ liệu.
SinhVienController.java
package vn.edu.eaut.lab7.controller;

Công nghệ Java | Lab 7 - Chương 3

Công nghệ Java - IT3242

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.eaut.lab7.model.SinhVien;
import vn.edu.eaut.lab7.repository.SinhVienRepository;
import java.io.IOException;
@WebServlet("/sinh-vien")
public class SinhVienController extends HttpServlet {
private final SinhVienRepository repo = new SinhVienRepository();
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
req.setCharacterEncoding("UTF-8");
String action = req.getParameter("action");
if ("new".equals(action)) { req.getRequestDispatcher("/views/sinhvien/form.jsp").forward(req, resp); return; }
if ("edit".equals(action)) { req.setAttribute("sv", repo.findById(Integer.parseInt(req.getParameter("id"))));
req.getRequestDispatcher("/views/sinhvien/form.jsp").forward(req, resp); return; }
if ("detail".equals(action)) { req.setAttribute("sv", repo.findById(Integer.parseInt(req.getParameter("id"))));
req.getRequestDispatcher("/views/sinhvien/detail.jsp").forward(req, resp); return; }
if ("delete".equals(action)) { repo.delete(Integer.parseInt(req.getParameter("id")));
resp.sendRedirect(req.getContextPath() + "/sinh-vien"); return; }
req.setAttribute("dsSinhVien", repo.search(req.getParameter("keyword")));
req.getRequestDispatcher("/views/sinhvien/list.jsp").forward(req, resp);
}
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
req.setCharacterEncoding("UTF-8");
String id = req.getParameter("id");
SinhVien sv = new SinhVien(id == null || id.isBlank() ? 0 : Integer.parseInt(id), req.getParameter("maSinhVien"),
req.getParameter("hoTen"), req.getParameter("email"), req.getParameter("lop"));
if (sv.getId() == 0) repo.add(sv); else repo.update(sv);
resp.sendRedirect(req.getContextPath() + "/sinh-vien");
}
}
Bài 4. Tạo View JSP hiển thị danh sách và form
Yêu cầu: dùng JSTL để duyệt danh sách; form dùng chung cho thêm và sửa.
views/sinhvien/list.jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<h2>Danh sách sinh viên</h2>
<form method="get" action="${pageContext.request.contextPath}/sinh-vien">
<input name="keyword" placeholder="Tìm theo tên hoặc lớp">
<button type="submit">Tìm</button>
</form>
<p><a href="${pageContext.request.contextPath}/sinh-vien?action=new">Thêm sinh viên</a></p>
<table border="1" cellpadding="6">
<tr><th>ID</th><th>Mã SV</th><th>Họ tên</th><th>Email</th><th>Lớp</th><th>Thao tác</th></tr>
<c:forEach var="sv" items="${dsSinhVien}">
<tr>
<td>${sv.id}</td><td>${sv.maSinhVien}</td>
<td><a href="${pageContext.request.contextPath}/sinh-vien?action=detail&id=${sv.id}">${sv.hoTen}</a></td>
<td>${sv.email}</td><td>${sv.lop}</td>
<td>
<a href="${pageContext.request.contextPath}/sinh-vien?action=edit&id=${sv.id}">Sửa</a> |
<a href="${pageContext.request.contextPath}/sinh-vien?action=delete&id=${sv.id}" onclick="return
confirm('Xóa?')">Xóa</a>
</td>
</tr>
</c:forEach>
</table>
views/sinhvien/form.jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h2>Form sinh viên</h2>
<form method="post" action="${pageContext.request.contextPath}/sinh-vien">
<input type="hidden" name="id" value="${sv.id}">
<p>Mã SV: <input name="maSinhVien" value="${sv.maSinhVien}" required></p>
<p>Họ tên: <input name="hoTen" value="${sv.hoTen}" required></p>
<p>Email: <input name="email" value="${sv.email}" type="email"></p>
<p>Lớp: <input name="lop" value="${sv.lop}"></p>
<button type="submit">Lưu</button>
</form>

Công nghệ Java | Lab 7 - Chương 3

Công nghệ Java - IT3242
Bài 5. Tạo đăng nhập và Filter bảo vệ URL quản trị
Yêu cầu: đăng nhập đơn giản bằng tài khoản mẫu, lưu username vào session; Filter chặn URL /admin/* nếu
chưa đăng nhập.
LoginFilter.java
package vn.edu.eaut.lab7.filter;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
@WebFilter("/admin/*")
public class LoginFilter implements Filter {
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
ServletException {
HttpServletRequest req = (HttpServletRequest) request;
HttpServletResponse resp = (HttpServletResponse) response;
HttpSession session = req.getSession(false);
if (session == null || session.getAttribute("username") == null) {
resp.sendRedirect(req.getContextPath() + "/login.jsp");
return;
}
chain.doFilter(request, response);
}
}
6. Bài tập không có code gợi ý
Bài 6. Quản lý sách
Tạo CRUD sách gồm mã sách, tên sách, tác giả, nhà xuất bản, năm xuất bản; có tìm kiếm theo tên hoặc tác
giả.
Bài 7. Quản lý sản phẩm
Tạo CRUD sản phẩm gồm mã, tên, mô tả, giá, số lượng; validate giá > 0 và số lượng >= 0.
Bài 8. Quản lý lớp học
Tạo CRUD lớp học gồm mã lớp, tên lớp, cố vấn học tập, số lượng sinh viên; tìm kiếm theo mã hoặc tên lớp.
Bài 9. Quản lý điểm sinh viên
Tạo chức năng nhập điểm chuyên cần, giữa kỳ, cuối kỳ; tính tổng kết và xếp loại A/B/C/D/F.
Bài 10. Quản lý giỏ hàng bằng Session
Tạo giỏ hàng đơn giản, thêm sản phẩm, cập nhật số lượng, xóa khỏi giỏ và tính tổng tiền.
Bài 11. Phân trang danh sách
Bổ sung phân trang cho danh sách sinh viên hoặc sản phẩm, mỗi trang 5 dòng.
Bài 12. Listener ghi log ứng dụng
Tạo listener ghi log khi ứng dụng khởi động/dừng và khi session được tạo/hủy.
Bài 13. Bài tổng hợp MVC
Hoàn thiện ít nhất 3 module CRUD, có menu, session đăng nhập, filter và báo cáo luồng MVC.
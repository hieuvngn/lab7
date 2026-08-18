# Lab 7 - CRUD MVC Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Xây dựng ứng dụng Web CRUD (Servlet + JSP + JSTL, MVC) hoàn chỉnh 13 bài của Lab 7 trong thư mục `lab7`.

**Architecture:** Maven WAR `lab07-crud-mvc`, package `vn.edu.eaut.lab7` với các tầng `model` / `repository` (dữ liệu `List` trong bộ nhớ) / `controller` / `filter` / `listener`; views JSP trong `webapp/views`. Mọi URL quản trị nằm dưới `/admin/*` và được `LoginFilter` bảo vệ.

**Tech Stack:** Java 17 (máy có JDK 26, biên dịch target 17), Jakarta Servlet 6.0 (Tomcat 10.1+), JSTL 3.0, Maven.

## Global Constraints

- Thư mục `lab7` KHÔNG phải git repo → **bỏ qua bước commit** trong các task.
- Dữ liệu lưu bằng `List` trong bộ nhớ, KHÔNG dùng database.
- Mã nguồn UTF-8; mọi JSP bắt đầu bằng `<%@ page contentType="text/html;charset=UTF-8" language="java" %>`.
- Không unit test; bước xác minh là `mvn -q package` (chạy tại gốc `lab7`), WAR đầu ra `target/lab07-crud-mvc.war`.
- `pom.xml`: jakarta.servlet-api 6.0.0 (scope provided), jakarta.servlet.jsp.jstl-api 3.0.0, org.glassfish.web jakarta.servlet.jsp.jstl 3.0.1, finalName `lab07-crud-mvc`.
- URL gốc trong JSP luôn dùng `${pageContext.request.contextPath}`.
- Tài khoản mẫu: `admin / 123456` (chỉ lưu `username` vào session).
- Điểm: tổng kết = 10% chuyên cần + 30% giữa kỳ + 60% cuối kỳ; A ≥ 8.5, B ≥ 7, C ≥ 5.5, D ≥ 4, F < 4.
- Phân trang: 5 dòng/trang, tham số `page`, giữ `keyword` khi chuyển trang.
- Mọi controller gọi `req.setCharacterEncoding("UTF-8")` trước khi đọc tham số; lưu thành công → redirect-after-post.

---

### Task 1: Dựng khung dự án (pom.xml, web.xml, index.jsp — Bài 1)

**Files:**
- Create: `pom.xml`, `src/main/webapp/WEB-INF/web.xml`, `src/main/webapp/index.jsp`

**Interfaces:**
- Produces: cấu trúc thư mục `src/main/java/vn/edu/eaut/lab7/{controller,filter,listener,model,repository}` và `src/main/webapp/views/{sinhvien,sach,sanpham,lophoc,diem,giohang}` (tạo rỗng, giữ git placeholder không cần vì chưa có git); trang chủ `index.jsp` với menu trỏ các URL `/admin/*` và `/login`.

- [ ] **Step 1: Tạo `pom.xml`** (đúng docs.md)

```xml
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
    <build>
        <finalName>lab07-crud-mvc</finalName>
    </build>
</project>
```

- [ ] **Step 2: Tạo `src/main/webapp/WEB-INF/web.xml`**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee
                             https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
         version="6.0">
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>
</web-app>
```

- [ ] **Step 3: Tạo `src/main/webapp/index.jsp`** (menu trỏ `/admin/*` vì controller nằm dưới prefix này — điều chỉnh từ code gợi ý)

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Lab 7 - CRUD MVC</title></head>
<body>
<h2>Lab 7 - CRUD bằng Servlet + JSP, dùng MVC đơn giản</h2>
<ul>
    <li><a href="${pageContext.request.contextPath}/admin/sinh-vien">Quản lý sinh viên</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/sach">Quản lý sách</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/san-pham">Quản lý sản phẩm</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/lop-hoc">Quản lý lớp học</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/diem">Quản lý điểm sinh viên</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/gio-hang">Giỏ hàng</a></li>
    <li><a href="${pageContext.request.contextPath}/login">Đăng nhập</a></li>
</ul>
</body>
</html>
```

- [ ] **Step 4: Tạo thư mục trống cho các package**

```bash
mkdir -p src/main/java/vn/edu/eaut/lab7/{controller,filter,listener,model,repository} \
         src/main/webapp/views/{sinhvien,sach,sanpham,lophoc,diem,giohang}
```

- [ ] **Step 5: Xác minh build**

Run: `mvn -q package` (tại gốc `lab7`)
Expected: BUILD SUCCESS, xuất hiện `target/lab07-crud-mvc.war`.

---

### Task 2: Model SinhVien + SinhVienRepository (Bài 2)

**Files:**
- Create: `src/main/java/vn/edu/eaut/lab7/model/SinhVien.java`, `src/main/java/vn/edu/eaut/lab7/model/HasId.java`, `src/main/java/vn/edu/eaut/lab7/repository/SinhVienRepository.java`

**Interfaces:**
- Produces: `SinhVien` (id, maSinhVien, hoTen, email, lop — full constructor + default + getters/setters); `HasId { int getId(); void setId(int id); }` (SinhVien implements); `SinhVienRepository` với `List<SinhVien> findAll()`, `SinhVien findById(int)`, `void add(SinhVien)`, `void update(SinhVien)`, `void delete(int)`, `List<SinhVien> search(String)`.
- Consumes: Task 1 (cấu trúc dự án).

- [ ] **Step 1: Tạo `model/HasId.java`** (interface chung cho repository generic ở Task 5)

```java
package vn.edu.eaut.lab7.model;

public interface HasId {
    int getId();
    void setId(int id);
}
```

- [ ] **Step 2: Tạo `model/SinhVien.java`** (đúng code gợi ý)

```java
package vn.edu.eaut.lab7.model;

public class SinhVien implements HasId {
    private int id;
    private String maSinhVien;
    private String hoTen;
    private String email;
    private String lop;

    public SinhVien() {}

    public SinhVien(int id, String maSinhVien, String hoTen, String email, String lop) {
        this.id = id;
        this.maSinhVien = maSinhVien;
        this.hoTen = hoTen;
        this.email = email;
        this.lop = lop;
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
```

- [ ] **Step 3: Tạo `repository/SinhVienRepository.java`** (đúng code gợi ý, dữ liệu static)

```java
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

    public SinhVien findById(int id) {
        return data.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    public void add(SinhVien sv) {
        sv.setId(autoId++);
        data.add(sv);
    }

    public void update(SinhVien sv) {
        SinhVien old = findById(sv.getId());
        if (old != null) {
            old.setMaSinhVien(sv.getMaSinhVien());
            old.setHoTen(sv.getHoTen());
            old.setEmail(sv.getEmail());
            old.setLop(sv.getLop());
        }
    }

    public void delete(int id) { data.removeIf(x -> x.getId() == id); }

    public List<SinhVien> search(String key) {
        if (key == null || key.trim().isEmpty()) return data;
        String k = key.toLowerCase();
        return data.stream().filter(x -> x.getHoTen().toLowerCase().contains(k) ||
                x.getLop().toLowerCase().contains(k)).collect(Collectors.toList());
    }
}
```

- [ ] **Step 4: Xác minh build**

Run: `mvn -q package`
Expected: BUILD SUCCESS.

---

### Task 3: SinhVienController + views sinh viên (Bài 3, 4)

**Files:**
- Create: `src/main/java/vn/edu/eaut/lab7/controller/SinhVienController.java`, `src/main/webapp/views/menu.jsp`, `src/main/webapp/views/sinhvien/list.jsp`, `src/main/webapp/views/sinhvien/form.jsp`, `src/main/webapp/views/sinhvien/detail.jsp`

**Interfaces:**
- Consumes: `SinhVienRepository` (Task 2) — `findById`, `add`, `update`, `delete`, `search`.
- Produces: `SinhVienController` `@WebServlet("/admin/sinh-vien")` — GET actions `new|edit|detail|delete` + mặc định list/search; POST lưu thêm/sửa; attributes `dsSinhVien`, `sv`; redirect về `/admin/sinh-vien`. `views/menu.jsp` — thanh menu chung (dùng `<%@ include file="/views/menu.jsp" %>`), đọc `sessionScope.username`.

- [ ] **Step 1: Tạo `views/menu.jsp`** (menu chung, dùng được trong mọi trang admin)

```jsp
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
```

- [ ] **Step 2: Tạo `controller/SinhVienController.java`** (mapping `/admin/sinh-vien`, code theo gợi ý, chỉ đổi path)

```java
package vn.edu.eaut.lab7.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.eaut.lab7.model.SinhVien;
import vn.edu.eaut.lab7.repository.SinhVienRepository;
import java.io.IOException;

@WebServlet("/admin/sinh-vien")
public class SinhVienController extends HttpServlet {
    private final SinhVienRepository repo = new SinhVienRepository();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if ("new".equals(action)) {
            req.getRequestDispatcher("/views/sinhvien/form.jsp").forward(req, resp);
            return;
        }
        if ("edit".equals(action)) {
            int id = parseId(req);
            if (id <= 0) {
                resp.sendRedirect(req.getContextPath() + "/admin/sinh-vien");
                return;
            }
            req.setAttribute("sv", repo.findById(id));
            req.getRequestDispatcher("/views/sinhvien/form.jsp").forward(req, resp);
            return;
        }
        if ("detail".equals(action)) {
            int id = parseId(req);
            if (id <= 0) {
                resp.sendRedirect(req.getContextPath() + "/admin/sinh-vien");
                return;
            }
            req.setAttribute("sv", repo.findById(id));
            req.getRequestDispatcher("/views/sinhvien/detail.jsp").forward(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            int id = parseId(req);
            if (id > 0) repo.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/sinh-vien");
            return;
        }
        req.setAttribute("dsSinhVien", repo.search(req.getParameter("keyword")));
        req.getRequestDispatcher("/views/sinhvien/list.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        SinhVien sv = new SinhVien(id == null || id.isBlank() ? 0 : Integer.parseInt(id),
                req.getParameter("maSinhVien"), req.getParameter("hoTen"),
                req.getParameter("email"), req.getParameter("lop"));
        if (sv.getId() == 0) repo.add(sv);
        else repo.update(sv);
        resp.sendRedirect(req.getContextPath() + "/admin/sinh-vien");
    }

    private int parseId(HttpServletRequest req) {
        try {
            return Integer.parseInt(req.getParameter("id"));
        } catch (Exception e) {
            return -1;
        }
    }
}
```

- [ ] **Step 3: Tạo `views/sinhvien/list.jsp`**

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Danh sách sinh viên</title></head>
<body>
<%@ include file="/views/menu.jsp" %>
<h2>Danh sách sinh viên</h2>
<form method="get" action="${pageContext.request.contextPath}/admin/sinh-vien">
    <input name="keyword" placeholder="Tìm theo tên hoặc lớp" value="${param.keyword}">
    <button type="submit">Tìm</button>
</form>
<p><a href="${pageContext.request.contextPath}/admin/sinh-vien?action=new">Thêm sinh viên</a></p>
<table border="1" cellpadding="6">
    <tr><th>ID</th><th>Mã SV</th><th>Họ tên</th><th>Email</th><th>Lớp</th><th>Thao tác</th></tr>
    <c:forEach var="sv" items="${dsSinhVien}">
        <tr>
            <td>${sv.id}</td><td>${sv.maSinhVien}</td>
            <td><a href="${pageContext.request.contextPath}/admin/sinh-vien?action=detail&id=${sv.id}">${sv.hoTen}</a></td>
            <td>${sv.email}</td><td>${sv.lop}</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/sinh-vien?action=edit&id=${sv.id}">Sửa</a> |
                <a href="${pageContext.request.contextPath}/admin/sinh-vien?action=delete&id=${sv.id}" onclick="return confirm('Xóa?')">Xóa</a>
            </td>
        </tr>
    </c:forEach>
</table>
<c:if test="${empty dsSinhVien}"><p>Không tìm thấy sinh viên.</p></c:if>
</body>
</html>
```

- [ ] **Step 4: Tạo `views/sinhvien/form.jsp`** (dùng chung thêm/sửa)

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Form sinh viên</title></head>
<body>
<%@ include file="/views/menu.jsp" %>
<h2>Form sinh viên</h2>
<form method="post" action="${pageContext.request.contextPath}/admin/sinh-vien">
    <input type="hidden" name="id" value="${sv.id}">
    <p>Mã SV: <input name="maSinhVien" value="${sv.maSinhVien}" required></p>
    <p>Họ tên: <input name="hoTen" value="${sv.hoTen}" required></p>
    <p>Email: <input name="email" value="${sv.email}" type="email"></p>
    <p>Lớp: <input name="lop" value="${sv.lop}"></p>
    <button type="submit">Lưu</button>
</form>
</body>
</html>
```

- [ ] **Step 5: Tạo `views/sinhvien/detail.jsp`**

```jsp
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
```

- [ ] **Step 6: Xác minh build**

Run: `mvn -q package`
Expected: BUILD SUCCESS.

---

### Task 4: Đăng nhập + LoginFilter (Bài 5)

**Files:**
- Create: `src/main/java/vn/edu/eaut/lab7/controller/AuthController.java`, `src/main/java/vn/edu/eaut/lab7/filter/LoginFilter.java`, `src/main/webapp/login.jsp`

**Interfaces:**
- Consumes: menu.jsp (Task 3) đọc `sessionScope.username`.
- Produces: `AuthController` `@WebServlet({"/login", "/logout"})` — GET `/login` forward `login.jsp`, GET `/logout` invalidate + redirect `/`, POST `/login` xác thực `admin/123456` → set session `username` → redirect `/admin/sinh-vien`, sai → forward `login.jsp` với attribute `error`. `LoginFilter` `@WebFilter("/admin/*")` — chưa đăng nhập → redirect `/login.jsp`... (dùng contextPath).

- [ ] **Step 1: Tạo `controller/AuthController.java`**

```java
package vn.edu.eaut.lab7.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet({"/login", "/logout"})
public class AuthController extends HttpServlet {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123456";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getServletPath().equals("/logout")) {
            HttpSession session = req.getSession(false);
            if (session != null) session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            req.getSession().setAttribute("username", username);
            resp.sendRedirect(req.getContextPath() + "/admin/sinh-vien");
        } else {
            req.setAttribute("error", "Sai tài khoản hoặc mật khẩu");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
```

- [ ] **Step 2: Tạo `filter/LoginFilter.java`** (đúng code gợi ý)

```java
package vn.edu.eaut.lab7.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/admin/*")
public class LoginFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
```

- [ ] **Step 3: Tạo `src/main/webapp/login.jsp`**

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Đăng nhập</title></head>
<body>
<h2>Đăng nhập</h2>
<c:if test="${not empty error}"><p style="color:red">${error}</p></c:if>
<form method="post" action="${pageContext.request.contextPath}/login">
    <p>Username: <input name="username" required></p>
    <p>Password: <input name="password" type="password" required></p>
    <button type="submit">Đăng nhập</button>
</form>
<p>Tài khoản mẫu: <b>admin / 123456</b></p>
</body>
</html>
```

- [ ] **Step 4: Xác minh build**

Run: `mvn -q package`
Expected: BUILD SUCCESS.

---

### Task 5: Module Sách (Bài 6)

**Files:**
- Create: `src/main/java/vn/edu/eaut/lab7/model/Sach.java`, `src/main/java/vn/edu/eaut/lab7/repository/AbstractRepository.java`, `src/main/java/vn/edu/eaut/lab7/repository/SachRepository.java`, `src/main/java/vn/edu/eaut/lab7/controller/SachController.java`, `src/main/webapp/views/sach/list.jsp`, `src/main/webapp/views/sach/form.jsp`

**Interfaces:**
- Consumes: `HasId` (Task 2).
- Produces: `AbstractRepository<T extends HasId>` — phương thức `findAll()`, `findById(int)`, `add(T)`, `update(T)`, `delete(int)`, abstract `List<T> data()` (subclass cung cấp static list); id tự tăng = max hiện tại + 1. `Sach` (id, maSach, tenSach, tacGia, nhaXuatBan, namXuatBan). `SachRepository` — kế thừa base + `List<Sach> search(String)` theo tenSach/tacGia (case-insensitive). `SachController` `@WebServlet("/admin/sach")` — mẫu giống Task 3: actions `new|edit|delete` + mặc định list/search; POST lưu. Các Task 6, 7, 8 dùng cùng mẫu.

- [ ] **Step 1: Tạo `model/Sach.java`**

```java
package vn.edu.eaut.lab7.model;

public class Sach implements HasId {
    private int id;
    private String maSach;
    private String tenSach;
    private String tacGia;
    private String nhaXuatBan;
    private int namXuatBan;

    public Sach() {}

    public Sach(int id, String maSach, String tenSach, String tacGia, String nhaXuatBan, int namXuatBan) {
        this.id = id;
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.nhaXuatBan = nhaXuatBan;
        this.namXuatBan = namXuatBan;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaSach() { return maSach; }
    public void setMaSach(String maSach) { this.maSach = maSach; }
    public String getTenSach() { return tenSach; }
    public void setTenSach(String tenSach) { this.tenSach = tenSach; }
    public String getTacGia() { return tacGia; }
    public void setTacGia(String tacGia) { this.tacGia = tacGia; }
    public String getNhaXuatBan() { return nhaXuatBan; }
    public void setNhaXuatBan(String nhaXuatBan) { this.nhaXuatBan = nhaXuatBan; }
    public int getNamXuatBan() { return namXuatBan; }
    public void setNamXuatBan(int namXuatBan) { this.namXuatBan = namXuatBan; }
}
```

- [ ] **Step 2: Tạo `repository/AbstractRepository.java`**

```java
package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.HasId;
import java.util.List;

public abstract class AbstractRepository<T extends HasId> {

    protected abstract List<T> data();

    public List<T> findAll() { return data(); }

    public T findById(int id) {
        return data().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    public void add(T item) {
        int nextId = data().stream().mapToInt(HasId::getId).max().orElse(0) + 1;
        item.setId(nextId);
        data().add(item);
    }

    public void update(T item) {
        T old = findById(item.getId());
        if (old != null) {
            data().set(data().indexOf(old), item);
        }
    }

    public void delete(int id) {
        data().removeIf(x -> x.getId() == id);
    }
}
```

- [ ] **Step 3: Tạo `repository/SachRepository.java`** (data static để mọi instance chia sẻ)

```java
package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.Sach;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SachRepository extends AbstractRepository<Sach> {
    private static final List<Sach> DATA = new ArrayList<>();

    @Override
    protected List<Sach> data() { return DATA; }

    public List<Sach> search(String key) {
        if (key == null || key.trim().isEmpty()) return DATA;
        String k = key.trim().toLowerCase();
        return DATA.stream()
                .filter(x -> x.getTenSach().toLowerCase().contains(k)
                        || x.getTacGia().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }
}
```

- [ ] **Step 4: Tạo `controller/SachController.java`**

```java
package vn.edu.eaut.lab7.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.eaut.lab7.model.Sach;
import vn.edu.eaut.lab7.repository.SachRepository;
import java.io.IOException;

@WebServlet("/admin/sach")
public class SachController extends HttpServlet {
    private final SachRepository repo = new SachRepository();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if ("new".equals(action)) {
            req.getRequestDispatcher("/views/sach/form.jsp").forward(req, resp);
            return;
        }
        if ("edit".equals(action)) {
            int id = parseId(req);
            if (id <= 0) {
                resp.sendRedirect(req.getContextPath() + "/admin/sach");
                return;
            }
            req.setAttribute("sach", repo.findById(id));
            req.getRequestDispatcher("/views/sach/form.jsp").forward(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            int id = parseId(req);
            if (id > 0) repo.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/sach");
            return;
        }
        req.setAttribute("dsSach", repo.search(req.getParameter("keyword")));
        req.getRequestDispatcher("/views/sach/list.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        Sach sach = new Sach(id == null || id.isBlank() ? 0 : Integer.parseInt(id),
                req.getParameter("maSach"), req.getParameter("tenSach"),
                req.getParameter("tacGia"), req.getParameter("nhaXuatBan"),
                Integer.parseInt(req.getParameter("namXuatBan")));
        if (sach.getId() == 0) repo.add(sach);
        else repo.update(sach);
        resp.sendRedirect(req.getContextPath() + "/admin/sach");
    }

    private int parseId(HttpServletRequest req) {
        try {
            return Integer.parseInt(req.getParameter("id"));
        } catch (Exception e) {
            return -1;
        }
    }
}
```

- [ ] **Step 5: Tạo `views/sach/list.jsp`**

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Danh sách sách</title></head>
<body>
<%@ include file="/views/menu.jsp" %>
<h2>Danh sách sách</h2>
<form method="get" action="${pageContext.request.contextPath}/admin/sach">
    <input name="keyword" placeholder="Tìm theo tên hoặc tác giả" value="${param.keyword}">
    <button type="submit">Tìm</button>
</form>
<p><a href="${pageContext.request.contextPath}/admin/sach?action=new">Thêm sách</a></p>
<table border="1" cellpadding="6">
    <tr><th>ID</th><th>Mã sách</th><th>Tên sách</th><th>Tác giả</th><th>NXB</th><th>Năm XB</th><th>Thao tác</th></tr>
    <c:forEach var="s" items="${dsSach}">
        <tr>
            <td>${s.id}</td><td>${s.maSach}</td><td>${s.tenSach}</td><td>${s.tacGia}</td>
            <td>${s.nhaXuatBan}</td><td>${s.namXuatBan}</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/sach?action=edit&id=${s.id}">Sửa</a> |
                <a href="${pageContext.request.contextPath}/admin/sach?action=delete&id=${s.id}" onclick="return confirm('Xóa?')">Xóa</a>
            </td>
        </tr>
    </c:forEach>
</table>
<c:if test="${empty dsSach}"><p>Không tìm thấy sách.</p></c:if>
</body>
</html>
```

- [ ] **Step 6: Tạo `views/sach/form.jsp`**

```jsp
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
```

- [ ] **Step 7: Xác minh build**

Run: `mvn -q package`
Expected: BUILD SUCCESS.

---

### Task 6: Module Sản phẩm + validate (Bài 7)

**Files:**
- Create: `src/main/java/vn/edu/eaut/lab7/model/SanPham.java`, `src/main/java/vn/edu/eaut/lab7/repository/SanPhamRepository.java`, `src/main/java/vn/edu/eaut/lab7/controller/SanPhamController.java`, `src/main/webapp/views/sanpham/list.jsp`, `src/main/webapp/views/sanpham/form.jsp`

**Interfaces:**
- Consumes: `AbstractRepository` (Task 5), `HasId` (Task 2).
- Produces: `SanPham` (id, maSanPham, tenSanPham, moTa, `double gia`, `int soLuong`); `SanPhamRepository` extends base + `search(String)` theo tenSanPham/maSanPham; `SanPhamController` `@WebServlet("/admin/san-pham")` — **validate: gia > 0, soLuong ≥ 0, cả lỗi parse số**; lỗi → attribute `error` + `sp` (giữ giá trị nhập) → forward `form.jsp`; `list.jsp` có cột **"Thêm vào giỏ"** trỏ `/admin/gio-hang?action=add&id=...` (GioHangController ở Task 9).

- [ ] **Step 1: Tạo `model/SanPham.java`**

```java
package vn.edu.eaut.lab7.model;

public class SanPham implements HasId {
    private int id;
    private String maSanPham;
    private String tenSanPham;
    private String moTa;
    private double gia;
    private int soLuong;

    public SanPham() {}

    public SanPham(int id, String maSanPham, String tenSanPham, String moTa, double gia, int soLuong) {
        this.id = id;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaSanPham() { return maSanPham; }
    public void setMaSanPham(String maSanPham) { this.maSanPham = maSanPham; }
    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}
```

- [ ] **Step 2: Tạo `repository/SanPhamRepository.java`**

```java
package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.SanPham;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SanPhamRepository extends AbstractRepository<SanPham> {
    private static final List<SanPham> DATA = new ArrayList<>();

    @Override
    protected List<SanPham> data() { return DATA; }

    public List<SanPham> search(String key) {
        if (key == null || key.trim().isEmpty()) return DATA;
        String k = key.trim().toLowerCase();
        return DATA.stream()
                .filter(x -> x.getTenSanPham().toLowerCase().contains(k)
                        || x.getMaSanPham().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }
}
```

- [ ] **Step 3: Tạo `controller/SanPhamController.java`** (có validate)

```java
package vn.edu.eaut.lab7.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.eaut.lab7.model.SanPham;
import vn.edu.eaut.lab7.repository.SanPhamRepository;
import java.io.IOException;

@WebServlet("/admin/san-pham")
public class SanPhamController extends HttpServlet {
    private final SanPhamRepository repo = new SanPhamRepository();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if ("new".equals(action)) {
            req.getRequestDispatcher("/views/sanpham/form.jsp").forward(req, resp);
            return;
        }
        if ("edit".equals(action)) {
            int id = parseId(req);
            if (id <= 0) {
                resp.sendRedirect(req.getContextPath() + "/admin/san-pham");
                return;
            }
            req.setAttribute("sp", repo.findById(id));
            req.getRequestDispatcher("/views/sanpham/form.jsp").forward(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            int id = parseId(req);
            if (id > 0) repo.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/san-pham");
            return;
        }
        req.setAttribute("dsSanPham", repo.search(req.getParameter("keyword")));
        req.getRequestDispatcher("/views/sanpham/list.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        SanPham sp = new SanPham(id == null || id.isBlank() ? 0 : Integer.parseInt(id),
                req.getParameter("maSanPham"), req.getParameter("tenSanPham"),
                req.getParameter("moTa"), 0, 0);
        String error = null;
        try {
            double gia = Double.parseDouble(req.getParameter("gia"));
            int soLuong = Integer.parseInt(req.getParameter("soLuong"));
            if (gia <= 0) error = "Giá phải lớn hơn 0";
            else if (soLuong < 0) error = "Số lượng phải >= 0";
            else {
                sp.setGia(gia);
                sp.setSoLuong(soLuong);
            }
        } catch (NumberFormatException e) {
            error = "Giá và số lượng phải là số hợp lệ";
        }
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("sp", sp);
            req.getRequestDispatcher("/views/sanpham/form.jsp").forward(req, resp);
            return;
        }
        if (sp.getId() == 0) repo.add(sp);
        else repo.update(sp);
        resp.sendRedirect(req.getContextPath() + "/admin/san-pham");
    }

    private int parseId(HttpServletRequest req) {
        try {
            return Integer.parseInt(req.getParameter("id"));
        } catch (Exception e) {
            return -1;
        }
    }
}
```

- [ ] **Step 4: Tạo `views/sanpham/list.jsp`** (có cột "Thêm vào giỏ")

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Danh sách sản phẩm</title></head>
<body>
<%@ include file="/views/menu.jsp" %>
<h2>Danh sách sản phẩm</h2>
<form method="get" action="${pageContext.request.contextPath}/admin/san-pham">
    <input name="keyword" placeholder="Tìm theo mã hoặc tên" value="${param.keyword}">
    <button type="submit">Tìm</button>
</form>
<p><a href="${pageContext.request.contextPath}/admin/san-pham?action=new">Thêm sản phẩm</a></p>
<table border="1" cellpadding="6">
    <tr><th>ID</th><th>Mã SP</th><th>Tên</th><th>Mô tả</th><th>Giá</th><th>Số lượng</th><th>Thao tác</th></tr>
    <c:forEach var="sp" items="${dsSanPham}">
        <tr>
            <td>${sp.id}</td><td>${sp.maSanPham}</td><td>${sp.tenSanPham}</td><td>${sp.moTa}</td>
            <td>${sp.gia}</td><td>${sp.soLuong}</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/gio-hang?action=add&id=${sp.id}">Thêm vào giỏ</a> |
                <a href="${pageContext.request.contextPath}/admin/san-pham?action=edit&id=${sp.id}">Sửa</a> |
                <a href="${pageContext.request.contextPath}/admin/san-pham?action=delete&id=${sp.id}" onclick="return confirm('Xóa?')">Xóa</a>
            </td>
        </tr>
    </c:forEach>
</table>
<c:if test="${empty dsSanPham}"><p>Không tìm thấy sản phẩm.</p></c:if>
</body>
</html>
```

- [ ] **Step 5: Tạo `views/sanpham/form.jsp`** (hiển thị `${error}` và giữ giá trị nhập khi lỗi)

```jsp
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
```

- [ ] **Step 6: Xác minh build**

Run: `mvn -q package`
Expected: BUILD SUCCESS.

---

### Task 7: Module Lớp học (Bài 8)

**Files:**
- Create: `src/main/java/vn/edu/eaut/lab7/model/LopHoc.java`, `src/main/java/vn/edu/eaut/lab7/repository/LopHocRepository.java`, `src/main/java/vn/edu/eaut/lab7/controller/LopHocController.java`, `src/main/webapp/views/lophoc/list.jsp`, `src/main/webapp/views/lophoc/form.jsp`

**Interfaces:**
- Consumes: `AbstractRepository` (Task 5), `HasId` (Task 2).
- Produces: `LopHoc` (id, maLop, tenLop, coVanHocTap, `int soLuongSinhVien`); `LopHocRepository` extends base + `search(String)` theo maLop/tenLop (case-insensitive); `LopHocController` `@WebServlet("/admin/lop-hoc")` — mẫu y hệt Task 5 (actions `new|edit|delete`, POST lưu).

- [ ] **Step 1: Tạo `model/LopHoc.java`**

```java
package vn.edu.eaut.lab7.model;

public class LopHoc implements HasId {
    private int id;
    private String maLop;
    private String tenLop;
    private String coVanHocTap;
    private int soLuongSinhVien;

    public LopHoc() {}

    public LopHoc(int id, String maLop, String tenLop, String coVanHocTap, int soLuongSinhVien) {
        this.id = id;
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.coVanHocTap = coVanHocTap;
        this.soLuongSinhVien = soLuongSinhVien;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }
    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }
    public String getCoVanHocTap() { return coVanHocTap; }
    public void setCoVanHocTap(String coVanHocTap) { this.coVanHocTap = coVanHocTap; }
    public int getSoLuongSinhVien() { return soLuongSinhVien; }
    public void setSoLuongSinhVien(int soLuongSinhVien) { this.soLuongSinhVien = soLuongSinhVien; }
}
```

- [ ] **Step 2: Tạo `repository/LopHocRepository.java`**

```java
package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.LopHoc;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LopHocRepository extends AbstractRepository<LopHoc> {
    private static final List<LopHoc> DATA = new ArrayList<>();

    @Override
    protected List<LopHoc> data() { return DATA; }

    public List<LopHoc> search(String key) {
        if (key == null || key.trim().isEmpty()) return DATA;
        String k = key.trim().toLowerCase();
        return DATA.stream()
                .filter(x -> x.getMaLop().toLowerCase().contains(k)
                        || x.getTenLop().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }
}
```

- [ ] **Step 3: Tạo `controller/LopHocController.java`**

```java
package vn.edu.eaut.lab7.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.eaut.lab7.model.LopHoc;
import vn.edu.eaut.lab7.repository.LopHocRepository;
import java.io.IOException;

@WebServlet("/admin/lop-hoc")
public class LopHocController extends HttpServlet {
    private final LopHocRepository repo = new LopHocRepository();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if ("new".equals(action)) {
            req.getRequestDispatcher("/views/lophoc/form.jsp").forward(req, resp);
            return;
        }
        if ("edit".equals(action)) {
            int id = parseId(req);
            if (id <= 0) {
                resp.sendRedirect(req.getContextPath() + "/admin/lop-hoc");
                return;
            }
            req.setAttribute("lop", repo.findById(id));
            req.getRequestDispatcher("/views/lophoc/form.jsp").forward(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            int id = parseId(req);
            if (id > 0) repo.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/lop-hoc");
            return;
        }
        req.setAttribute("dsLop", repo.search(req.getParameter("keyword")));
        req.getRequestDispatcher("/views/lophoc/list.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        LopHoc lop = new LopHoc(id == null || id.isBlank() ? 0 : Integer.parseInt(id),
                req.getParameter("maLop"), req.getParameter("tenLop"),
                req.getParameter("coVanHocTap"),
                Integer.parseInt(req.getParameter("soLuongSinhVien")));
        if (lop.getId() == 0) repo.add(lop);
        else repo.update(lop);
        resp.sendRedirect(req.getContextPath() + "/admin/lop-hoc");
    }

    private int parseId(HttpServletRequest req) {
        try {
            return Integer.parseInt(req.getParameter("id"));
        } catch (Exception e) {
            return -1;
        }
    }
}
```

- [ ] **Step 4: Tạo `views/lophoc/list.jsp`**

```jsp
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
```

- [ ] **Step 5: Tạo `views/lophoc/form.jsp`**

```jsp
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
```

- [ ] **Step 6: Xác minh build**

Run: `mvn -q package`
Expected: BUILD SUCCESS.

---

### Task 8: Module Điểm sinh viên (Bài 9)

**Files:**
- Create: `src/main/java/vn/edu/eaut/lab7/model/DiemSinhVien.java`, `src/main/java/vn/edu/eaut/lab7/repository/DiemRepository.java`, `src/main/java/vn/edu/eaut/lab7/controller/DiemController.java`, `src/main/webapp/views/diem/list.jsp`, `src/main/webapp/views/diem/form.jsp`

**Interfaces:**
- Consumes: `AbstractRepository` (Task 5), `HasId` (Task 2).
- Produces: `DiemSinhVien` (id, maSinhVien, chuyenCan, giuaKy, cuoiKy — `double`) với `double getTongKet()` = round1(0.1·CC + 0.3·GK + 0.6·CK) và `String getXepLoai()` (A ≥ 8.5, B ≥ 7, C ≥ 5.5, D ≥ 4, F < 4); `DiemRepository` extends base + `search(String)` theo maSinhVien; `DiemController` `@WebServlet("/admin/diem")` — mẫu như Task 5/7 nhưng **validate điểm trong 0-10** (lỗi → forward `form.jsp` với `${error}` và giữ giá trị); `list.jsp` có cột "Tổng kết" và "Xếp loại".

- [ ] **Step 1: Tạo `model/DiemSinhVien.java`**

```java
package vn.edu.eaut.lab7.model;

public class DiemSinhVien implements HasId {
    private int id;
    private String maSinhVien;
    private double chuyenCan;
    private double giuaKy;
    private double cuoiKy;

    public DiemSinhVien() {}

    public DiemSinhVien(int id, String maSinhVien, double chuyenCan, double giuaKy, double cuoiKy) {
        this.id = id;
        this.maSinhVien = maSinhVien;
        this.chuyenCan = chuyenCan;
        this.giuaKy = giuaKy;
        this.cuoiKy = cuoiKy;
    }

    public double getTongKet() {
        return Math.round((0.1 * chuyenCan + 0.3 * giuaKy + 0.6 * cuoiKy) * 10) / 10.0;
    }

    public String getXepLoai() {
        double t = getTongKet();
        if (t >= 8.5) return "A";
        if (t >= 7.0) return "B";
        if (t >= 5.5) return "C";
        if (t >= 4.0) return "D";
        return "F";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaSinhVien() { return maSinhVien; }
    public void setMaSinhVien(String maSinhVien) { this.maSinhVien = maSinhVien; }
    public double getChuyenCan() { return chuyenCan; }
    public void setChuyenCan(double chuyenCan) { this.chuyenCan = chuyenCan; }
    public double getGiuaKy() { return giuaKy; }
    public void setGiuaKy(double giuaKy) { this.giuaKy = giuaKy; }
    public double getCuoiKy() { return cuoiKy; }
    public void setCuoiKy(double cuoiKy) { this.cuoiKy = cuoiKy; }
}
```

- [ ] **Step 2: Tạo `repository/DiemRepository.java`**

```java
package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.DiemSinhVien;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiemRepository extends AbstractRepository<DiemSinhVien> {
    private static final List<DiemSinhVien> DATA = new ArrayList<>();

    @Override
    protected List<DiemSinhVien> data() { return DATA; }

    public List<DiemSinhVien> search(String key) {
        if (key == null || key.trim().isEmpty()) return DATA;
        String k = key.trim().toLowerCase();
        return DATA.stream()
                .filter(x -> x.getMaSinhVien().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }
}
```

- [ ] **Step 3: Tạo `controller/DiemController.java`** (validate 0-10)

```java
package vn.edu.eaut.lab7.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.eaut.lab7.model.DiemSinhVien;
import vn.edu.eaut.lab7.repository.DiemRepository;
import java.io.IOException;

@WebServlet("/admin/diem")
public class DiemController extends HttpServlet {
    private final DiemRepository repo = new DiemRepository();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if ("new".equals(action)) {
            req.getRequestDispatcher("/views/diem/form.jsp").forward(req, resp);
            return;
        }
        if ("edit".equals(action)) {
            int id = parseId(req);
            if (id <= 0) {
                resp.sendRedirect(req.getContextPath() + "/admin/diem");
                return;
            }
            req.setAttribute("diem", repo.findById(id));
            req.getRequestDispatcher("/views/diem/form.jsp").forward(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            int id = parseId(req);
            if (id > 0) repo.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/diem");
            return;
        }
        req.setAttribute("dsDiem", repo.search(req.getParameter("keyword")));
        req.getRequestDispatcher("/views/diem/list.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        DiemSinhVien diem = new DiemSinhVien(id == null || id.isBlank() ? 0 : Integer.parseInt(id),
                req.getParameter("maSinhVien"), 0, 0, 0);
        String error = null;
        try {
            double cc = Double.parseDouble(req.getParameter("chuyenCan"));
            double gk = Double.parseDouble(req.getParameter("giuaKy"));
            double ck = Double.parseDouble(req.getParameter("cuoiKy"));
            if (cc < 0 || cc > 10 || gk < 0 || gk > 10 || ck < 0 || ck > 10) {
                error = "Điểm phải nằm trong khoảng 0-10";
            } else {
                diem.setChuyenCan(cc);
                diem.setGiuaKy(gk);
                diem.setCuoiKy(ck);
            }
        } catch (NumberFormatException e) {
            error = "Điểm phải là số hợp lệ";
        }
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("diem", diem);
            req.getRequestDispatcher("/views/diem/form.jsp").forward(req, resp);
            return;
        }
        if (diem.getId() == 0) repo.add(diem);
        else repo.update(diem);
        resp.sendRedirect(req.getContextPath() + "/admin/diem");
    }

    private int parseId(HttpServletRequest req) {
        try {
            return Integer.parseInt(req.getParameter("id"));
        } catch (Exception e) {
            return -1;
        }
    }
}
```

- [ ] **Step 4: Tạo `views/diem/list.jsp`** (có cột Tổng kết, Xếp loại)

```jsp
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
```

- [ ] **Step 5: Tạo `views/diem/form.jsp`**

```jsp
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
```

- [ ] **Step 6: Xác minh build**

Run: `mvn -q package`
Expected: BUILD SUCCESS.

---

### Task 9: Giỏ hàng bằng Session (Bài 10)

**Files:**
- Create: `src/main/java/vn/edu/eaut/lab7/model/CartItem.java`, `src/main/java/vn/edu/eaut/lab7/controller/GioHangController.java`, `src/main/webapp/views/giohang/list.jsp`
- Modify: `src/main/webapp/views/sanpham/list.jsp` (đã có cột "Thêm vào giỏ" từ Task 6 — kiểm tra, không cần sửa)

**Interfaces:**
- Consumes: `SanPhamRepository` (Task 6), `SanPham` (Task 6).
- Produces: `CartItem` (sanPham, soLuong) với `double getThanhTien()`; `GioHangController` `@WebServlet("/admin/gio-hang")` — giỏ là `Map<Integer, CartItem>` trong session (key = id sản phẩm, `LinkedHashMap` giữ thứ tự): action `add` (tăng số lượng nếu đã có), `update` (tham số `soLuong`; ≤ 0 → xóa), `remove`, mặc định hiển thị; attributes `cartItems` (collection) và `total` (double).

- [ ] **Step 1: Tạo `model/CartItem.java`**

```java
package vn.edu.eaut.lab7.model;

public class CartItem {
    private SanPham sanPham;
    private int soLuong;

    public CartItem(SanPham sanPham, int soLuong) {
        this.sanPham = sanPham;
        this.soLuong = soLuong;
    }

    public double getThanhTien() {
        return sanPham.getGia() * soLuong;
    }

    public SanPham getSanPham() { return sanPham; }
    public void setSanPham(SanPham sanPham) { this.sanPham = sanPham; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}
```

- [ ] **Step 2: Tạo `controller/GioHangController.java`**

```java
package vn.edu.eaut.lab7.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.eaut.lab7.model.CartItem;
import vn.edu.eaut.lab7.model.SanPham;
import vn.edu.eaut.lab7.repository.SanPhamRepository;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/admin/gio-hang")
public class GioHangController extends HttpServlet {
    private final SanPhamRepository sanPhamRepo = new SanPhamRepository();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute("cart", cart);
        }
        if ("add".equals(action)) {
            int id = parseInt(req.getParameter("id"));
            SanPham sp = sanPhamRepo.findById(id);
            if (sp != null) {
                CartItem item = cart.get(id);
                if (item == null) cart.put(id, new CartItem(sp, 1));
                else item.setSoLuong(item.getSoLuong() + 1);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/gio-hang");
            return;
        }
        if ("update".equals(action)) {
            int id = parseInt(req.getParameter("id"));
            int soLuong = parseInt(req.getParameter("soLuong"));
            CartItem item = cart.get(id);
            if (item != null) {
                if (soLuong <= 0) cart.remove(id);
                else item.setSoLuong(soLuong);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/gio-hang");
            return;
        }
        if ("remove".equals(action)) {
            cart.remove(parseInt(req.getParameter("id")));
            resp.sendRedirect(req.getContextPath() + "/admin/gio-hang");
            return;
        }
        double total = cart.values().stream().mapToDouble(CartItem::getThanhTien).sum();
        req.setAttribute("cartItems", cart.values());
        req.setAttribute("total", total);
        req.getRequestDispatcher("/views/giohang/list.jsp").forward(req, resp);
    }

    private int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }
}
```

- [ ] **Step 3: Tạo `views/giohang/list.jsp`**

```jsp
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
```

- [ ] **Step 4: Xác minh build**

Run: `mvn -q package`
Expected: BUILD SUCCESS.

---

### Task 10: Phân trang danh sách sinh viên (Bài 11)

**Files:**
- Modify: `src/main/java/vn/edu/eaut/lab7/controller/SinhVienController.java`, `src/main/webapp/views/sinhvien/list.jsp`

**Interfaces:**
- Consumes: `SinhVienRepository.search(String)` (Task 2), controller hiện tại (Task 3).
- Produces: controller bổ sung slicing: attributes `dsSinhVien` (trang hiện tại), `page`, `totalPages`, `keyword`; `list.jsp` hiển thị điều hướng Trước/Sau + số trang, giữ `keyword`.

- [ ] **Step 1: Sửa `SinhVienController.java`** — thay khối mặc định trong `doGet` (giữ nguyên các nhánh action khác)

```java
        List<SinhVien> all = repo.search(req.getParameter("keyword"));
        int page = 1;
        try {
            page = Integer.parseInt(req.getParameter("page"));
        } catch (Exception ignored) {}
        int pageSize = 5;
        int totalPages = Math.max(1, (int) Math.ceil(all.size() / (double) pageSize));
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;
        int from = (page - 1) * pageSize;
        int to = Math.min(from + pageSize, all.size());
        req.setAttribute("dsSinhVien", all.subList(from, to));
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("keyword", req.getParameter("keyword"));
        req.getRequestDispatcher("/views/sinhvien/list.jsp").forward(req, resp);
```

Cần thêm import `java.util.List` vào đầu file (dòng `import java.io.IOException;` → thêm `import java.util.List;`).

- [ ] **Step 2: Sửa `views/sinhvien/list.jsp`** — thêm khối phân trang ngay sau `</table>` (trước `<c:if test="${empty dsSinhVien}">`)

```jsp
<c:if test="${totalPages > 1}">
    <p>
        <c:if test="${page > 1}">
            <a href="${pageContext.request.contextPath}/admin/sinh-vien?page=${page - 1}&keyword=${keyword}">Trước</a>
        </c:if>
        <c:forEach var="i" begin="1" end="${totalPages}">
            <c:choose>
                <c:when test="${i == page}"><b>${i}</b></c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/admin/sinh-vien?page=${i}&keyword=${keyword}">${i}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if test="${page < totalPages}">
            <a href="${pageContext.request.contextPath}/admin/sinh-vien?page=${page + 1}&keyword=${keyword}">Sau</a>
        </c:if>
    </p>
</c:if>
```

- [ ] **Step 3: Xác minh build**

Run: `mvn -q package`
Expected: BUILD SUCCESS.

---

### Task 11: Listener ghi log (Bài 12)

**Files:**
- Create: `src/main/java/vn/edu/eaut/lab7/listener/AppContextListener.java`, `src/main/java/vn/edu/eaut/lab7/listener/SessionLogListener.java`

**Interfaces:**
- Consumes: `SachRepository`, `SanPhamRepository`, `LopHocRepository`, `DiemRepository` (Task 5-8) — `add(T)`, `findAll()`.
- Produces: `AppContextListener` `@WebListener` — `contextInitialized`: seed dữ liệu mẫu cho Sách/Sản phẩm/Lớp/Điểm (SinhVien đã seed sẵn trong `SinhVienRepository`), log số bản ghi; `contextDestroyed`: log tổng số bản ghi mỗi module. `SessionLogListener` `@WebListener` — log khi session tạo/hủy. Cả hai dùng `ServletContext.log` và in ra console bằng `System.out`.

- [ ] **Step 1: Tạo `listener/AppContextListener.java`**

```java
package vn.edu.eaut.lab7.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import vn.edu.eaut.lab7.model.*;
import vn.edu.eaut.lab7.repository.*;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        SachRepository sachRepo = new SachRepository();
        sachRepo.add(new Sach(0, "S001", "Lập trình Java", "Nguyễn Văn An", "NXB Giáo dục", 2021));
        sachRepo.add(new Sach(0, "S002", "Cấu trúc dữ liệu và giải thuật", "Trần Thị Bình", "NXB Khoa học", 2022));
        sachRepo.add(new Sach(0, "S003", "Cơ sở dữ liệu", "Lê Văn Cường", "NXB Thống kê", 2020));

        SanPhamRepository spRepo = new SanPhamRepository();
        spRepo.add(new SanPham(0, "SP001", "Bàn phím cơ", "Bàn phím cơ RGB", 890000, 20));
        spRepo.add(new SanPham(0, "SP002", "Chuột không dây", "Chuột không dây 2.4GHz", 350000, 50));
        spRepo.add(new SanPham(0, "SP003", "Màn hình 24 inch", "Màn hình Full HD IPS", 2500000, 10));

        LopHocRepository lopRepo = new LopHocRepository();
        lopRepo.add(new LopHoc(0, "DCCNTT15.10.1", "Đại học Công nghệ thông tin 15.10.1", "ThS. Nguyễn Văn An", 45));
        lopRepo.add(new LopHoc(0, "DCCNTT15.10.2", "Đại học Công nghệ thông tin 15.10.2", "ThS. Trần Thị Bình", 48));

        DiemRepository diemRepo = new DiemRepository();
        diemRepo.add(new DiemSinhVien(0, "20240001", 9, 8, 8.5));
        diemRepo.add(new DiemSinhVien(0, "20240002", 8, 7, 6.5));

        String msg = String.format(
                "[App] Khởi động: sách=%d, sản phẩm=%d, lớp=%d, điểm=%d, sinh viên=%d",
                sachRepo.findAll().size(), spRepo.findAll().size(), lopRepo.findAll().size(),
                diemRepo.findAll().size(), new SinhVienRepository().findAll().size());
        System.out.println(msg);
        sce.getServletContext().log(msg);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        String msg = String.format(
                "[App] Dừng: sách=%d, sản phẩm=%d, lớp=%d, điểm=%d, sinh viên=%d",
                new SachRepository().findAll().size(), new SanPhamRepository().findAll().size(),
                new LopHocRepository().findAll().size(), new DiemRepository().findAll().size(),
                new SinhVienRepository().findAll().size());
        System.out.println(msg);
        sce.getServletContext().log(msg);
    }
}
```

- [ ] **Step 2: Tạo `listener/SessionLogListener.java`**

```java
package vn.edu.eaut.lab7.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionLogListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        String msg = "[Session] Tạo session mới: " + se.getSession().getId();
        System.out.println(msg);
        se.getSession().getServletContext().log(msg);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String msg = "[Session] Hủy session: " + se.getSession().getId();
        System.out.println(msg);
        se.getSession().getServletContext().log(msg);
    }
}
```

- [ ] **Step 3: Xác minh build**

Run: `mvn -q package`
Expected: BUILD SUCCESS.

---

### Task 12: Báo cáo luồng MVC (Bài 13)

**Files:**
- Create: `docs/bao-cao-lab7.md`

**Interfaces:**
- Consumes: toàn bộ module đã xây dựng (Task 1-11).
- Produces: báo cáo Markdown mô tả kiến trúc MVC, luồng yêu cầu qua từng module, liệt kê menu/session login/filter/báo cáo luồng MVC.

- [ ] **Step 1: Tạo `docs/bao-cao-lab7.md`**

```markdown
# Báo cáo Lab 7 - CRUD bằng Servlet + JSP, MVC đơn giản

## 1. Kiến trúc tổng quan (MVC)

- **Model**: `vn.edu.eaut.lab7.model` - SinhVien, Sach, SanPham, LopHoc, DiemSinhVien, CartItem.
- **Repository**: `vn.edu.eaut.lab7.repository` - tầng lưu trữ dữ liệu bằng `List` trong bộ nhớ
  (SinhVienRepository theo code gợi ý; các module khác kế thừa `AbstractRepository<T>`).
- **View**: JSP + JSTL trong `src/main/webapp` (index.jsp, login.jsp, views/...).
- **Controller**: `vn.edu.eaut.lab7.controller` - các Servlet xử lý yêu cầu,
  chuyển dữ liệu model cho View, điều hướng sau khi lưu (redirect-after-post).
- **Filter**: `LoginFilter` bảo vệ `/admin/*`.
- **Listener**: `AppContextListener`, `SessionLogListener` ghi log vòng đời ứng dụng/session.

## 2. Luồng xử lý một yêu cầu

```
Trình duyệt
   | GET /admin/sinh-vien?action=edit&id=1
   v
LoginFilter (kiểm tra session username, chưa đăng nhập -> redirect /login)
   v
SinhVienController (Controller)
   | repo.findById(1)  ->  SinhVienRepository (Model/Repository)
   | setAttribute("sv", sinhVien)
   v
forward -> views/sinhvien/form.jsp (View)
   | người dùng sửa và submit POST
   v
SinhVienController.doPost
   | repo.update(sv)
   v
redirect /admin/sinh-vien (redirect-after-post, tránh submit lại)
   v
list.jsp hiển thị danh sách
```

## 3. Các module đã hoàn thành

| Bài | Module | URL | Chức năng |
|-----|--------|-----|-----------|
| 1 | Trang chủ | `/` | index.jsp + menu điều hướng |
| 2-4 | Sinh viên | `/admin/sinh-vien` | CRUD, tìm kiếm theo tên/lớp, detail |
| 5 | Đăng nhập | `/login`, `/logout` | session username, LoginFilter chặn `/admin/*` |
| 6 | Sách | `/admin/sach` | CRUD, tìm kiếm theo tên/tác giả |
| 7 | Sản phẩm | `/admin/san-pham` | CRUD, validate giá > 0, số lượng >= 0 |
| 8 | Lớp học | `/admin/lop-hoc` | CRUD, tìm kiếm theo mã/tên lớp |
| 9 | Điểm SV | `/admin/diem` | Nhập CC/GK/CK, tính tổng kết, xếp loại A-F |
| 10 | Giỏ hàng | `/admin/gio-hang` | Session cart, cập nhật số lượng, tổng tiền |
| 11 | Phân trang | `/admin/sinh-vien?page=N` | 5 dòng/trang, giữ keyword |
| 12 | Listener | - | Log khởi động/dừng, tạo/hủy session |

## 4. Tài khoản

- `admin / 123456` - tài khoản mẫu, lưu `username` vào session.

## 5. Cách chạy

1. `mvn package` -> `target/lab07-crud-mvc.war`
2. Deploy lên Tomcat 10.1+, truy cập `http://localhost:8080/lab07-crud-mvc/`
```

- [ ] **Step 2: Xác minh** — không cần build (chỉ tài liệu), chạy `mvn -q package` để chắc chắn toàn bộ dự án vẫn build được.

---

### Task 13: Kiểm tra tổng thể

**Files:**
- Kiểm tra toàn bộ cấu trúc thư mục `lab7`.

**Interfaces:**
- Consumes: tất cả task trên.

- [ ] **Step 1: Kiểm tra cấu trúc dự án**

Run: `find . -path ./target -prune -o -type f -print | sort`
Expected: đủ `pom.xml`, `web.xml`, `index.jsp`, `login.jsp`, 5 package Java, views đủ 6 thư mục con.

- [ ] **Step 2: Build sạch toàn bộ**

Run: `mvn -q clean package`
Expected: BUILD SUCCESS, `target/lab07-crud-mvc.war` tồn tại.

- [ ] **Step 3: Kiểm tra nội dung WAR**

Run: `jar tf target/lab07-crud-mvc.war | sort`
Expected: có `index.jsp`, `login.jsp`, `WEB-INF/classes/vn/edu/eaut/lab7/controller/...` (đủ 7 controller), `filter/LoginFilter`, `listener/AppContextListener`, `listener/SessionLogListener`, các views.
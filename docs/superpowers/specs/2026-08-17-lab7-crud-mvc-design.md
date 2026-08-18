# Lab 7 - CRUD bằng Servlet + JSP, MVC đơn giản

Ngày: 2026-08-17

## Mục tiêu

Xây dựng ứng dụng Web CRUD bằng Servlet, JSP, JSTL theo mô hình MVC đơn giản,
project Maven `lab07-crud-mvc`, package gốc `vn.edu.eaut.lab7`, Jakarta EE 10 (Tomcat 10.1+).
Dữ liệu lưu bằng `List` trong bộ nhớ (không dùng database).

Hoàn thành **cả 13 bài** trong docs.md: Bài 1-5 có code gợi ý, Bài 6-13 tự làm.

## Kiến trúc

- **Model** (package `model`):
  - `SinhVien` (id, maSinhVien, hoTen, email, lop) — đúng code gợi ý.
  - `Sach` (id, maSach, tenSach, tacGia, nhaXuatBan, namXuatBan) — Bài 6.
  - `SanPham` (id, maSanPham, tenSanPham, moTa, gia, soLuong) — Bài 7.
  - `LopHoc` (id, maLop, tenLop, coVanHocTap, soLuongSinhVien) — Bài 8.
  - `DiemSinhVien` (id, maSinhVien, chuyenCan, giuaKy, cuoiKy; phương thức
    `tongKet()` = 10% CC + 30% GK + 60% CK, `xepLoai()`: A ≥ 8.5, B ≥ 7, C ≥ 5.5, D ≥ 4, F < 4) — Bài 9.
  - `CartItem` (sanPham, soLuong; getter `thanhTien()` = gia × soLuong) — Bài 10.
- **Repository** (package `repository`):
  - `SinhVienRepository` — đúng code gợi ý (static List, autoId, findAll/findById/add/update/delete/search).
  - `AbstractRepository<T>` — base: static List, autoId, findAll/findById/add/update/delete (update theo predicate), search theo predicate.
  - `SachRepository`, `SanPhamRepository`, `LopHocRepository`, `DiemRepository` — kế thừa base,
    thêm search: sách theo tên hoặc tác giả; lớp học theo mã hoặc tên lớp; điểm theo mã SV.
- **Controllers** (package `controller`, tất cả mapping dưới `/admin/*` để `LoginFilter` hoạt động):
  - `SinhVienController` (`/admin/sinh-vien`) — action `list`, `new`, `edit`, `detail`, `delete`;
    GET hiển thị, POST lưu; có tìm kiếm theo keyword (tên/lớp) và **phân trang 5 dòng/trang** (Bài 11).
  - `SachController` (`/admin/sach`) — CRUD + tìm kiếm theo tên hoặc tác giả (Bài 6).
  - `SanPhamController` (`/admin/san-pham`) — CRUD + **validate giá > 0 và số lượng ≥ 0** (Bài 7).
  - `LopHocController` (`/admin/lop-hoc`) — CRUD + tìm kiếm theo mã hoặc tên lớp (Bài 8).
  - `DiemController` (`/admin/diem`) — CRUD điểm, hiển thị tổng kết và xếp loại (Bài 9).
  - `GioHangController` (`/admin/gio-hang`) — thêm sản phẩm vào giỏ (liên kết "Thêm vào giỏ"
    ở `views/sanpham/list.jsp`), cập nhật số lượng, xóa khỏi giỏ, tính tổng tiền;
    giỏ là `Map<Integer, CartItem>` trong session (Bài 10).
  - `AuthController` (`/login`, `/logout`) — POST `/login` kiểm tra tài khoản mẫu,
    lưu `username` vào session, redirect `/admin/sinh-vien`; `/logout` invalidate session, redirect `login.jsp`.
- **Filter** (package `filter`):
  - `LoginFilter` `@WebFilter("/admin/*")` — đúng code gợi ý: chưa đăng nhập → redirect `/login.jsp`.
- **Listeners** (package `listener`) — Bài 12:
  - `AppContextListener` — khi khởi động: seed dữ liệu mẫu (SV, sách, SP, lớp, điểm) + log;
    khi dừng: log tổng số bản ghi.
  - `SessionLogListener` — log khi session được tạo/hủy.
- **Views** (JSP + JSTL, charset UTF-8):
  - `index.jsp` — trang chủ + menu điều hướng (Bài 1); menu trỏ các module CRUD
    (`/admin/sinh-vien`, `/admin/sach`, `/admin/san-pham`, `/admin/lop-hoc`, `/admin/diem`, `/admin/gio-hang`)
    và `/login.jsp` (điều chỉnh từ code gợi ý vì controller nằm dưới `/admin/*`).
  - `login.jsp` — form đăng nhập + `${error}`.
  - `views/menu.jsp` — thanh menu dùng chung (`<%@ include %>`), có nút đăng nhập/đăng xuất.
  - `views/sinhvien/list.jsp` (tìm kiếm + bảng + phân trang), `form.jsp` (thêm/sửa dùng chung), `detail.jsp`.
  - `views/sach/list.jsp`, `form.jsp`.
  - `views/sanpham/list.jsp`, `form.jsp` (hiển thị lỗi validate `${error}`).
  - `views/lophoc/list.jsp`, `form.jsp`.
  - `views/diem/list.jsp` (bảng điểm + cột tổng kết/xếp loại), `form.jsp`.
  - `views/giohang/list.jsp` (giỏ hàng + tổng tiền).
- **Cấu hình**: `web.xml` tối thiểu (welcome-file `index.jsp`); `pom.xml` theo docs.md
  (jakarta.servlet-api 6.0.0 provided, JSTL api 3.0.0 + impl 3.0.1).

## Luồng dữ liệu

- Danh sách: GET `/admin/sinh-vien?keyword=...&page=...` → repo tìm kiếm + cắt trang →
  set `dsSinhVien`, `page`, `totalPages` → forward `views/sinhvien/list.jsp`.
- Thêm/Sửa: GET `?action=new|edit&id=...` → set `sv` → forward `form.jsp`;
  POST → repo.add/update → redirect `/admin/sinh-vien` (redirect-after-post).
- Xem: GET `?action=detail&id=...` → set `sv` → forward `detail.jsp`.
- Xóa: GET `?action=delete&id=...` → repo.delete → redirect.
- Giỏ hàng: GET `/admin/gio-hang?action=add|update|remove&id=...` → thao tác trên
  `Map` trong session → redirect lại danh sách giỏ.
- Các module Sách/Sản phẩm/Lớp/Điểm theo cùng mẫu.

## Tài khoản

- `admin / 123456` — tài khoản mẫu duy nhất, lưu `username` vào session.

## Xử lý lỗi

- Sai tài khoản → `login.jsp` với `${error}`.
- Chưa đăng nhập truy cập `/admin/*` → redirect `login.jsp`.
- `id` không hợp lệ (parse lỗi hoặc không tồn tại) → redirect về list.
- Validate sản phẩm sai (giá ≤ 0, số lượng < 0) → forward lại `form.jsp` với `${error}`,
  giữ giá trị đã nhập.
- Validate điểm (chuyên cần/giữa kỳ/cuối kỳ phải trong 0-10) → forward lại `form.jsp` với `${error}`.
- Tìm kiếm không có kết quả → thông báo trong list.

## Báo cáo (Bài 13)

- `docs/bao-cao-lab7.md` — mô tả kiến trúc MVC, luồng yêu cầu qua từng module,
  liệt kê đủ: menu, session đăng nhập, filter `/admin/*`, và báo cáo luồng MVC.

## Kiểm thử

- Build: `mvn package` là bước xác minh chính (lab thuần web, không unit test, như lab6).
- Chạy: deploy `lab07-crud-mvc.war` lên Tomcat 10.1+.

## Phạm vi

Một project Maven duy nhất trong thư mục `lab7`. Không dùng database, không dùng
framework ngoài Servlet/JSP/JSTL.

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

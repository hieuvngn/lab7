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

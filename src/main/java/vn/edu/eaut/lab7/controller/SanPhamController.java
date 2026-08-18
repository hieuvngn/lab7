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

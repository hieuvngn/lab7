package vn.edu.eaut.lab7.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.eaut.lab7.model.SinhVien;
import vn.edu.eaut.lab7.repository.SinhVienRepository;
import java.io.IOException;
import java.util.List;

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

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

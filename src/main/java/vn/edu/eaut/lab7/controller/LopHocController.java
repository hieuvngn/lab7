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

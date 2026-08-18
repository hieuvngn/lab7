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

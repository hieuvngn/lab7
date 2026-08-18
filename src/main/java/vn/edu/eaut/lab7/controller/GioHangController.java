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

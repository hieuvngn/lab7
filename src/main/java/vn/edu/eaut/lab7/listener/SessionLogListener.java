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

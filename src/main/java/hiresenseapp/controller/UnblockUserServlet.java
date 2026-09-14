package hiresenseapp.controller;

import java.io.IOException;

import hiresenseapp.dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/UnblockUserServlet")
public class UnblockUserServlet extends HttpServlet {
    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int userId = Integer.parseInt(req.getParameter("userId"));
        try {
            UserDao.updateStatus(userId, "active");
            res.sendRedirect("adminPanel");
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("error.jsp");
        }
    }
}
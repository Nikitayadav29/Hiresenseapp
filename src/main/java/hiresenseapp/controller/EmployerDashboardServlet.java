package hiresenseapp.controller;

import java.io.IOException;
import java.util.List;

import hiresenseapp.dao.JobDao;
import hiresenseapp.pojo.JobPojo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/employerDashboard")
public class EmployerDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            try {
                int employerId = (Integer) session.getAttribute("userId");
                String search = request.getParameter("search");
                String sort = request.getParameter("sort");
                String status = request.getParameter("status");

                List<JobPojo> jobs = JobDao.getJobsByEmployer(employerId, search, status, sort);
                request.setAttribute("jobList", jobs);
                request.setAttribute("search", search);
                request.setAttribute("status", status);
                request.setAttribute("sort", sort);

                request.getRequestDispatcher("employeDashboard.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                StringBuilder sb = new StringBuilder();
                sb.append(e.toString()).append("\n");
                for (StackTraceElement el : e.getStackTrace()) {
                    sb.append(el.toString()).append("\n");
                }
                session.setAttribute("debugError", sb.toString());
                response.sendRedirect("error.jsp");
            }
        }
    }
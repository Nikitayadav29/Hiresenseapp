package hiresenseapp.controller;

import java.io.IOException;
import java.util.List;

import hiresenseapp.dao.ApplicationDao;
import hiresenseapp.dao.JobDao;
import hiresenseapp.dao.ResumeAnalysisLogDAO;
import hiresenseapp.dao.UserDao;
import hiresenseapp.pojo.Applicationpojo;
import hiresenseapp.pojo.JobPojo;
import hiresenseapp.pojo.ResumeAnalysisLogPojo;
import hiresenseapp.pojo.UserPojo;
import hiresenseapp.utils.MailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ApplyJobServlet")
public class ApplyJobServlet extends HttpServlet {
    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null || !"user".equals(session.getAttribute("userRole"))) {
            res.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        int jobId = Integer.parseInt(req.getParameter("jobId"));
        double score = Double.parseDouble(req.getParameter("score"));

        try {

            // ✅ Get resume path from latest resume analysis log

            String resumePath = "N/A";
            List<ResumeAnalysisLogPojo> logs = ResumeAnalysisLogDAO.getLogsByUser(userId);
            if (!logs.isEmpty()) {
                String resultJson = logs.get(0).getJsonResult();
                org.json.JSONObject obj = new org.json.JSONObject(resultJson);
                org.json.JSONObject data = obj.has("data") ? obj.getJSONObject("data") : new org.json.JSONObject();
                resumePath = data.optString("resumePath", "N/A");
            }

            Applicationpojo app = new Applicationpojo(0, userId, jobId, resumePath, score, "applied", null);
            ApplicationDao.apply(app);
            UserPojo user = UserDao.getUserById(userId);
            JobPojo job = JobDao.getJobById(jobId);
            MailUtil.sendApplicationConfirmation(user.getName(), user.getEmail(), job.getTitle(), job.getCompany());
            UserPojo user2 = UserDao.getUserById(job.getEmployerId());
            MailUtil.sendNewApplicationNotificationToEmployer(user2.getName(), user2.getEmail(),user.getName(),job.getTitle());

            res.sendRedirect("userDashboard?success=applied");
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("userDashboard?error=apply_failed");
        }
    }
}

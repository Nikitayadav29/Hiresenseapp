package hiresenseapp.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import hiresenseapp.dao.ApplicationDao;
import hiresenseapp.dao.JobDao;
import hiresenseapp.dao.ResumeAnalysisLogDAO;
import hiresenseapp.pojo.Applicationpojo;
import hiresenseapp.pojo.JobPojo;
import hiresenseapp.pojo.ResumeAnalysisLogPojo;
import hiresenseapp.utils.AffindaAPI;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/userDashboard")
public class UserDashboardServlet extends HttpServlet {
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String search = request.getParameter("search");
        String sort = request.getParameter("sort");
        String location = request.getParameter("location");
        String experience = request.getParameter("experience");
        String packageLpa = request.getParameter("packageLpa");

        try {
            // ✅ Check if resume uploaded
            List<ResumeAnalysisLogPojo> logs = ResumeAnalysisLogDAO.getLogsByUser(userId);
            boolean resumeUploaded = !logs.isEmpty();

            String resumeText = null;
            if (resumeUploaded) {
                JSONObject obj = new JSONObject(logs.get(0).getJsonResult());
                JSONObject data = obj.has("data") ? obj.getJSONObject("data") : new JSONObject();
                String resumePath = data.optString("resumePath", null);

                if (resumePath != null) {
                    java.io.File resumeFile = new java.io.File(resumePath);

                    if (!resumeFile.isAbsolute()) {
                        resumeFile = new java.io.File(getServletContext().getRealPath("/"), resumePath);
                    }

                    if (resumeFile.exists()) {
                        try {
                            resumeText = AffindaAPI.extractTextFromPdf(resumeFile);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            resumeText = null;
                        }
                    }
                }
            }

            List<JobPojo> jobs = JobDao.getAllJobsForUserDashboard(search, sort, location, experience, packageLpa);
            if (resumeUploaded && resumeText != null) {
                for (JobPojo job : jobs) {
                    int score = AffindaAPI.calculateMatchScoreFromText(resumeText, job.getSkills());
                    job.setScore(score);
                }
            }

            // ✅ Mark applied jobs
            List<Applicationpojo> appliedList = ApplicationDao.getApplicationsByUser(userId);
            Set<Integer> appliedJobIds = new HashSet<>();
            for (Applicationpojo app : appliedList) {
                appliedJobIds.add(app.getJobId());
            }

            // ✅ Set attributes
            request.setAttribute("jobs", jobs);
            request.setAttribute("appliedJobIds", appliedJobIds);
            request.setAttribute("search", search);
            request.setAttribute("sort", sort);
            request.setAttribute("location", location);
            request.setAttribute("experience", experience);
            request.setAttribute("packageLpa", packageLpa);
            request.setAttribute("resumeUploaded", resumeUploaded);

            request.getRequestDispatcher("userDashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
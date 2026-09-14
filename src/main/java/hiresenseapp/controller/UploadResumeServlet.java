package hiresenseapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONObject;

import hiresenseapp.dao.JobDao;
import hiresenseapp.dao.ResumeAnalysisLogDAO;
import hiresenseapp.pojo.JobPojo;
import hiresenseapp.pojo.ResumeAnalysisLogPojo;
import hiresenseapp.utils.AffindaAPI;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/UploadResumeServlet")
@MultipartConfig
public class UploadResumeServlet extends HttpServlet {
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String jobIdParam = request.getParameter("jobId");
        Integer jobId = (jobIdParam != null && !jobIdParam.isEmpty()) ? Integer.parseInt(jobIdParam) : null;

        Part filePart = request.getPart("resume");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        String uploadDir = getServletContext().getRealPath("/resumes");
        File dir = new File(uploadDir);
        if (!dir.exists()) {
			dir.mkdirs();
		}

        File resumeFile = new File(dir, fileName);

        try {
            List<ResumeAnalysisLogPojo> logs = ResumeAnalysisLogDAO.getLogsByUser(userId);
            if (!logs.isEmpty()) {
                String prevJson = logs.get(0).getJsonResult();
                JSONObject obj = new JSONObject(prevJson);
                String prevPath = (obj.has("data") ? obj.getJSONObject("data").optString("resumePath", null) : null);
                if (prevPath != null) {
                    File oldFile = new File(prevPath);
                    if (oldFile.exists()) {
						oldFile.delete();
					}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (InputStream input = filePart.getInputStream(); FileOutputStream out = new FileOutputStream(resumeFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        String resumeText = null;
        try {
            resumeText = AffindaAPI.extractTextFromPdf(resumeFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            JSONObject result = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("resumePath", resumeFile.getAbsolutePath());
            result.put("data", data);

            ResumeAnalysisLogDAO.saveLog(userId, result.toString());

            if (jobId != null) {
                try {
                	String resumePath = resumeFile.getAbsolutePath();

                	JobPojo job = JobDao.getJobById(jobId);
                	int matchScore = 0;
                	if (job != null && job.getSkills() != null && resumeText != null) {
                	    matchScore = AffindaAPI.calculateMatchScoreFromText(resumeText, job.getSkills());
                	}

                    hiresenseapp.pojo.Applicationpojo app = new hiresenseapp.pojo.Applicationpojo(0, userId, jobId, resumePath, matchScore, "applied", null);
                    hiresenseapp.dao.ApplicationDao.apply(app);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("userDashboard?success=applied");
    }
}
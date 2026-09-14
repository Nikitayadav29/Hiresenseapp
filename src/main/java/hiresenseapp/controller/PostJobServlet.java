package hiresenseapp.controller;

import java.io.IOException;

import hiresenseapp.dao.JobDao;
import hiresenseapp.pojo.JobPojo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/PostJobServlet")
public class PostJobServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(false);

		if (session == null || session.getAttribute("userId") == null
				|| !"employer".equals(session.getAttribute("userRole"))) {
			res.sendRedirect("login.jsp");
			return;
		}

		try {
			int employerId = (int) session.getAttribute("userId");

			// Get form parameters
			String title = req.getParameter("title");
			String description = req.getParameter("description");
			String skills = req.getParameter("skills");
			String company = req.getParameter("company");
			String location = req.getParameter("location");
			String experience = req.getParameter("experience");
			String packageLpa = req.getParameter("packageLpa");
			int vacancies = Integer.parseInt(req.getParameter("vacancies"));

			// Prepare Job object
			JobPojo job = new JobPojo();
			job.setTitle(title);
			job.setDescription(description);
			job.setSkills(skills);
			job.setCompany(company);
			job.setLocation(location);
			job.setExperience(experience);
			job.setPackageLpa(packageLpa);
			job.setVacancies(vacancies);
			job.setEmployerId(employerId);

			boolean ans = JobDao.postJob(job);
			if (ans) {
				res.sendRedirect("employerDashboard?success=1");
			} else {
				res.sendRedirect("employerDashboard?error=1");
			}

		} catch (Exception e) {
			e.printStackTrace();
			res.sendRedirect("employerDashboard?error=1");
		}
	}
}

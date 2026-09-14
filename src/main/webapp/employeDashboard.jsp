<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>employeDashboard | <%=application.getAttribute("appName")%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
</head>
<body>

	<%@include file="includes/header.jsp"%>
	<%
	if ((session.getAttribute("userId") == null)
			|| !"employer".equalsIgnoreCase((String) session.getAttribute("userRole"))) {
		response.sendRedirect("login.jsp");
		return;
	}
	%>
	<main class="container py-5">
		<h2 class="mb-4">
			Welcome,
			<%=session.getAttribute("userName")%>
			👔
		</h2>

		<!-- Post a Job Form -->
		<div class="card bg-glass p-4 mb-5">
			<h5>📢 Post a New Job</h5>
			<form action="PostJobServlet" method="post">
				<div class="mb-3">
					<input type="text" name="title" class="form-control text-black"
						placeholder="Job Title" required />
				</div>
				<div class="mb-3">
					<textarea name="description" class="form-control text-black"
						placeholder="Job Description" rows="3" required></textarea>
				</div>
				<div class="mb-3">
					<input type="text" name="skills" class="form-control text-black"
						placeholder="Required Skills (comma-separated)" required />
				</div>
				<div class="mb-3">
					<input type="text" name="company" class="form-control text-black"
						placeholder="Company Name" required />
				</div>
				<div class="mb-3">
					<select name="location" class="form-select" required>
						<option value="">Select Location</option>
						<option>Bangalore</option>
						<option>Mumbai</option>
						<option>Delhi</option>
						<option>Chennai</option>
						<option>Pune</option>
						<option>Hyderabad</option>
						<option>Bhopal</option>
						<option>Kolkata</option>
					</select>
				</div>
				<div class="mb-3">
					<select name="experience" class="form-select" required>
						<option value="">Select Experience</option>
						<option>Fresher</option>
						<option>0 - 1 year</option>
						<option>1 - 2 years</option>
						<option>2 - 3 years</option>
						<option>3 - 5 years</option>
						<option>5+ years</option>
					</select>
				</div>
				<div class="mb-3">
					<select name="packageLpa" class="form-select" required>
						<option value="">Select Package</option>
						<option>1-2 Lacs P.A.</option>
						<option>2-3 Lacs P.A.</option>
						<option>3-4 Lacs P.A.</option>
						<option>4-5 Lacs P.A.</option>
						<option>5-10 Lacs P.A.</option>
						<option>10-20 Lacs P.A.</option>
						<option>Not disclosed</option>
					</select>
				</div>
				<div class="mb-3">
					<input type="number" name="vacancies" min="1"
						class="form-control text-black" placeholder="Number of Vacancies"
						required />
				</div>
				<button type="submit" class="btn btn-login">Post Job</button>
			</form>
		</div>

		<!-- Search & Filter -->
		<form method="get" action="employerDashboard">
			<div class="row g-2 align-items-center">
				<div class="col-md-4">
					<input type="text" name="search" class="form-control text-black"
						placeholder="Search by job title..." value="${param.search}" />
				</div>
				<div class="col-md-3">
					<select name="status" class="form-select text-black">
						<option value="">All Status</option>
						<option value="active"
							${param.status == 'active' ? 'selected' : ''}>Active</option>
						<option value="inactive"
							${param.status == 'inactive' ? 'selected' : ''}>Inactive</option>
					</select>
				</div>
				<div class="col-md-3">
					<select name="sort" class="form-select text-black">
						<option value="">Sort by Applicants</option>
						<option value="asc" ${param.sort == 'asc' ? 'selected' : ''}>Least
							to Most</option>
						<option value="desc" ${param.sort == 'desc' ? 'selected' : ''}>Most
							to Least</option>
					</select>
				</div>
				<div class="col-md-2">
					<button type="submit" class="btn btn-login">Search</button>
				</div>
			</div>
		</form>

		<!-- Posted Jobs -->
		<%
		java.util.List<hiresenseapp.pojo.JobPojo> jobList = (java.util.List<hiresenseapp.pojo.JobPojo>) request
				.getAttribute("jobList");
		if (jobList != null && !jobList.isEmpty()) {
		%>
		<div class="card bg-glass p-4">
			<h5>
				📄
				<%=jobList.get(0).getCompany()%>'s Posted Jobs
			</h5>
			<table class="table text-white mt-3">
				<thead>
					<tr>
						<th>Job Title</th>
						<th>Applicants</th>
						<th>Status</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody>
					<%
					//if (jobList != null && !jobList.isEmpty()) {
					for (hiresenseapp.pojo.JobPojo job : jobList) {
					%>
					<tr>
						<td><%=job.getTitle()%></td>
						<td><%=job.getApplicantCount()%></td>
						<td><%=job.getStatus().toUpperCase()%></td>
						<td><a href="ViewApplicantsServlet?jobId=<%=job.getId()%>"
							class="btn btn-sm btn-info">View</a> <a
							href="ToggleJobStatusServlet?jobId=<%=job.getId()%>"
							class="btn btn-sm <%="active".equals(job.getStatus()) ? "btn-warning" : "btn-success"%>">
								<%="active".equals(job.getStatus()) ? "Deactivate" : "Activate"%>
						</a></td>
					</tr>
					<%
					}
					%>


				</tbody>
			</table>
		</div>
		<%
		} else {
		%>
		<tr>
			<td colspan="5" class="text-center">No jobs posted yet.</td>
		</tr>
		<%
		}
		%>
	</main>

	<%
	String success = request.getParameter("success");
	if ("1".equals(success)) {
	%>
	<script>
		Swal.fire({
			icon : 'success',
			title : 'Job Posted!',
			text : 'Your job has been posted successfully.',
			timer : 2000,
			showConfirmButton : false
		});
	</script>
	<%
	}
	%>

	<%
	String error = request.getParameter("error");
	if ("1".equals(error)) {
	%>
	<script>
		Swal.fire({
			icon : 'error',
			title : 'Failed!',
			text : 'Something went wrong. Please try again.',
			confirmButtonText : 'Okay'
		});
	</script>
	<%
	}
	%>

	<%@include file="includes/footer.jsp"%>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
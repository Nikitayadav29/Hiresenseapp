# HireSense – AI-Powered Smart Job Portal

HireSense is a job portal web application that connects job seekers and recruiters. It allows candidates to upload resumes, matches them against job listings based on skills, and helps recruiters manage postings and applications.

## Features
- User authentication (Job Seeker / Employer / Admin roles)
- Resume upload with automatic text extraction (PDFBox)
- Skill-based resume-to-job match scoring
- Job listing with search, sort, and filters
- Application tracking dashboard

## Tech Stack
- Java (Servlets, JSP)
- Apache Tomcat
- MySQL
- Apache PDFBox (resume text extraction)
- HTML, CSS, JavaScript

## Setup Instructions

1. Clone the repository
2. Import the project into Eclipse as a Dynamic Web Project
3. Create a MySQL database named `hiresense_db`
4. Rename `src/main/webapp/WEB-INF/web.xml.example` to `web.xml`, and update it with your own local MySQL credentials
5. Run the project on Apache Tomcat (v10+) from Eclipse
6. Access the app at `http://localhost:8081/hiresenseapp`

## Notes
- Resume text extraction works best with text-based PDFs. Scanned/image-based PDF resumes are not supported (no OCR yet).
- Uploaded resumes are stored locally and excluded from version control.

## Author
Nikita Yadav
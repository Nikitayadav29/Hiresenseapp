package hiresenseapp.utils;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailUtil {

    public static void sendTextEmail(String toEmail, String subject, String body) throws MessagingException {
        Session session = MailConfig.getSession();

        Message msg = new MimeMessage(session);
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject(subject);
        msg.setText(body);

        Transport.send(msg);
    }

    public static void sendApplicationConfirmation(String name,String toEmail, String jobTitle, String company) throws MessagingException {
        String subject = "✅ Application Submitted - " + jobTitle;
        String body = "Dear "+name+",\n\n"
                    + "You have successfully applied for the position of " + jobTitle + " at " + company + ".\n"
                    + "We wish you the best of luck!\n\n"
                    + "Regards,\nHireSense Team";
        sendTextEmail(toEmail, subject, body);
    }

    public static void sendApplicationStatusUpdate(String name,String toEmail, String jobTitle, String company, String status) throws MessagingException {
        String subject = "📢 Application Status Update - " + jobTitle;
        String body;

        if ("shortlisted".equalsIgnoreCase(status)) {
            body = "Dear "+name+",\n\n"
                 + "Congratulations! You have been shortlisted for the role of " + jobTitle + " at " + company + ".\n"
                 + "The employer may contact you for the next steps.\n\n"
                 + "Best wishes,\nHireSense Team";
        } else if ("rejected".equalsIgnoreCase(status)) {
            body = "Dear candidate,\n\n"
                 + "We regret to inform you that you were not shortlisted for the position of " + jobTitle + " at " + company + ".\n"
                 + "We encourage you to apply for other jobs on HireSense.\n\n"
                 + "Regards,\nHireSense Team";
        } else {
            body = "Dear candidate,\n\n"
                 + "Your application status has been updated to: " + status + " for the job " + jobTitle + " at " + company + ".\n\n"
                 + "Regards,\nHireSense Team";
        }

        sendTextEmail(toEmail, subject, body);
    }

    public static void sendAccountActionNotice(String toEmail, String userName, String action) throws MessagingException {
        String subject = "⚠️ Account Update - HireSense";
        String body;

        switch (action.toLowerCase()) {
            case "blocked":
                body = "Hello " + userName + ",\n\n"
                     + "Your account on HireSense has been *blocked* due to policy violations or suspicious activity.\n"
                     + "Please contact support for more details.\n\n"
                     + "Regards,\nAdmin Team";
                break;

            case "unblocked":
                body = "Hello " + userName + ",\n\n"
                     + "Your account has been *unblocked* and you may now access all services again.\n"
                     + "Thank you for your patience.\n\n"
                     + "Regards,\nAdmin Team";
                break;

            case "removed":
                body = "Hello " + userName + ",\n\n"
                     + "Your account has been *permanently removed* from HireSense by the administrator.\n"
                     + "If you believe this is a mistake, please contact support.\n\n"
                     + "Regards,\nAdmin Team";
                break;

            default:
                body = "Hello " + userName + ",\n\n"
                     + "There has been an update to your HireSense account: *" + action + "*.\n\n"
                     + "Regards,\nAdmin Team";
                break;
        }

        sendTextEmail(toEmail, subject, body);
    }

    public static void sendNewApplicationNotificationToEmployer(String employerName, String toEmail, String applicantName, String jobTitle) throws MessagingException  {

                String subject = "New Application Received";
                String message = "Dear " + employerName + ",\n\n"
                        + "A new candidate (" + applicantName + ") has applied for your job posting: \"" + jobTitle + "\".\n"
                        + "You can review the applicant’s details in your dashboard.\n\n"
                        + "Regards,\nHireSense Team";

                MailUtil.sendTextEmail(toEmail, subject, message);
    }
}

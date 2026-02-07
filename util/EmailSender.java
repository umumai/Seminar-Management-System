package util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Utility class for sending emails to students.
 *
 * IMPORTANT:
 *  - Configure SMTP settings below (host, port, authentication, etc.)
 *  - For Gmail, use smtp.gmail.com with TLS on port 587
 *  - For other SMTP servers, adjust the properties accordingly
 *  - If email sending fails, the email content will be logged to console as fallback
 */

public class EmailSender {

    // SMTP Configuration - Update these values for your email server
    private static final String SMTP_HOST = "smtp.gmail.com"; // Change to your SMTP server
    private static final String SMTP_PORT = "587"; // TLS port (use "465" for SSL, "25" for no auth)
    private static final String FROM_EMAIL = "ummumai10@gmail.com"; // Change to your sender email
    private static final String FROM_NAME = "Seminar Management System";
    private static final boolean USE_AUTH = false; // Set to true if SMTP requires authentication
    private static final boolean USE_TLS = true; // Set to true for TLS/STARTTLS
    
    // If USE_AUTH is true, provide credentials here (or use environment variables for security)
    private static final String SMTP_USERNAME = ""; // Your email username
    private static final String SMTP_PASSWORD = ""; // Your email password

    /**
     * Sends a welcome email to a newly registered student with their account info.
     * 
     * @param studentId The student's ID (e.g., "STU001")
     * @param studentName The student's name
     * @param studentEmail The student's email address (if available)
     * @param password The student's password (for initial login)
     */
    public static void sendStudentWelcomeEmail(String studentId, String studentName, String studentEmail, String password) {
        // Validate email address
        if (studentEmail == null || studentEmail.trim().isEmpty()) {
            System.out.println("Warning: No email address provided for student " + studentId);
            logEmailToConsole(studentId, studentName, studentEmail, password);
            return;
        }

        try {
            // Setup SMTP properties
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", String.valueOf(USE_AUTH));
            
            if (USE_TLS) {
                props.put("mail.smtp.starttls.enable", "true");
            }

            // Create session
            Session session;
            if (USE_AUTH) {
                javax.mail.Authenticator auth = new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                    }
                };
                session = Session.getInstance(props, auth);
            } else {
                session = Session.getInstance(props, null);
            }

            // Create message
            MimeMessage msg = new MimeMessage(session);
            
            // Set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            // Set from address
            msg.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));

            // Set reply-to
            msg.setReplyTo(InternetAddress.parse(FROM_EMAIL, false));

            // Set subject
            String subject = "Welcome to Seminar Management System";
            msg.setSubject(subject, "UTF-8");

            // Build email body
            String body = buildEmailBody(studentId, studentName, password);
            msg.setText(body, "UTF-8");

            // Set sent date
            msg.setSentDate(new Date());

            // Set recipient
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(studentEmail, false));

            System.out.println("Sending email to " + studentEmail + "...");
            
            // Send message
            Transport.send(msg);

            System.out.println("Email sent successfully to " + studentEmail + "!");
            
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
            System.out.println("\nFalling back to console output:");
            logEmailToConsole(studentId, studentName, studentEmail, password);
        }
    }

    /**
     * Builds the email body content.
     */
    private static String buildEmailBody(String studentId, String studentName, String password) {
        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(studentName).append(",\n\n");
        body.append("Welcome to the Seminar Management System!\n\n");
        body.append("Your account has been created successfully.\n");
        body.append("Your login credentials are:\n");
        body.append("  Student ID: ").append(studentId).append("\n");
        body.append("  Password: ").append(password).append("\n\n");
        body.append("Please keep this information secure and change your password after your first login.\n\n");
        body.append("Best regards,\n");
        body.append("Seminar Management System");
        return body.toString();
    }

    /**
     * Logs email content to console as fallback when email sending fails or email is not configured.
     */
    private static void logEmailToConsole(String studentId, String studentName, String studentEmail, String password) {
        System.out.println("========================================");
        System.out.println("EMAIL (Console Output)");
        System.out.println("========================================");
        System.out.println("To: " + (studentEmail != null && !studentEmail.isEmpty() ? studentEmail : "student@example.com"));
        System.out.println("Subject: Welcome to Seminar Management System");
        System.out.println("----------------------------------------");
        System.out.println(buildEmailBody(studentId, studentName, password));
        System.out.println("========================================");
    }
}


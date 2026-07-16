package software.project;

import java.util.Properties;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailService {

    private final String username;
    private final String password;

    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void sendEmail(String to, String subject, String body) {
    	
        Properties props = new Properties();
        
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // e.g., Gmail SMTP
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully to " + to);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(" Failed to send email", e);
        }
    }
    
    
    static void  run() {
    	

    	Dotenv dotenv = Dotenv.load();  
    	
    	String username = dotenv.get("EMAIL_USERNAME");
    	String password = dotenv.get("EMAIL_PASSWORD");
		
    	
    	EmailService emailService=new EmailService(username,password );
    	
    	String subject = "Appointment";
        String body = "Dear user, Your Appointment is comming soon. Best regards";
       
        
    	emailService.sendEmail("s12323849@stu.najah.edu", subject, body);
    
    	
    }
    
    
    public static void main(String []s) {
    	run();
    }
    
    
   
}

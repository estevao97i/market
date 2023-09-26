package estevao.market.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

// classe de envio de email
@Service
public class SendServiceEmail {

    private String userName = "@gmail.com";
    private String senha = "**********";

    @Async
    public void enviarEmailHtml(String assunto, String mensagem, String emailDestino) throws MessagingException, UnsupportedEncodingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls", "false");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.port", "4654");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, senha);
            }
        });

        session.setDebug(true);

        Address[] toUser = InternetAddress.parse(emailDestino);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(userName, "estevao email projeto", "UTF-8"));
        message.setRecipients(Message.RecipientType.TO, toUser);
        message.setSubject(assunto);
        message.setContent(message, "text/html; charset=utf-8");

        Transport.send(message);
    }
}

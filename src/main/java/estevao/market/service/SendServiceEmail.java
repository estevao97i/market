package estevao.market.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

// classe de envio de email
@Service
public class SendServiceEmail {

    private String userName = "***********@gmail.com";
    private String senha = "senhaAqui";

    @Async
    public void enviarEmailHtml(String assunto, String mensagem, String emailDestino) {

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
    }
}

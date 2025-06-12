package ing.gpps.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;
@Service //lo registro como un bean de Spring
public class EmailService {

    private final String remitente;
    private final String claveApp;

    public EmailService(
            @Value ("${email.remitente}") String remitente,
            @Value("${email.claveApp}") String claveApp) {
        this.remitente = remitente;
        this.claveApp = claveApp;
    }

    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        // Configuración de SMTP para Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Autenticación
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, claveApp);
            }
        });

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(remitente));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);

            Transport.send(mensaje);
            System.out.println("Correo enviado con éxito a " + destinatario);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

//ejemplos de uso
class Main {
    public static void main(String[] args) {
        String remitente = "universidadderionegrodireccion@gmail.com";//gmail
        String claveApp = "lzoj hgbp ffwv fbrq"; // clave generada en Google

        EmailService servicio = new EmailService(remitente, claveApp);

        servicio.enviarCorreo(
                "salvoschaferlautaro@gmail.com",// destinatario
                "Que onda pa ",//Asunto
                "ponete a laburar"//mensaje
                );
}
}
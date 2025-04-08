package com.security.EduNext.Services;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendCertificateEmail(String to, String certificateTitle, File certificatePdf) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("🎉 Félicitations ! Votre certificat est prêt 🎓");
            helper.setText("Bonjour,\n\n"
                    + "Félicitations ! Vous avez réussi l'examen '" + certificateTitle + "'. 🎉\n"
                    + "Votre certificat est disponible en pièce jointe.\n\n"
                    + "Cordialement,\n"
                    + "L'équipe EduNext");

            // Ajouter le fichier PDF en pièce jointe
            helper.addAttachment("Certificat_" + certificateTitle + ".pdf", certificatePdf);

            mailSender.send(message);
            System.out.println("Email envoyé à " + to);
        } catch (MessagingException e) {
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }


}

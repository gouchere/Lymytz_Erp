package yvs.users.messagerie;

import java.io.IOException;
import java.io.StringReader;
import java.security.Security;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

public class MailSender {

    final private static String CHARSET = "charset=ISO-8859-1";

    final private static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    final private static int DEFAULT_SMTP_PORT = 25;

    final private Session _session;

    // Constructeur n°1: Connexion au serveur mail
    public MailSender(final String host, final int port, final String userName, final String password, final boolean ssl, final int portSSL) {
        final Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.setProperty("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.auth", "false");
        if (ssl) {
            // Connection SSL
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.socketFactory.port", portSSL);
        }
        if (null == userName || null == password) {
            _session = Session.getDefaultInstance(props, null);
        } else {
            // Connexion avec authentification
            _session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            });
        }
    }

    // Autres constructeurs
    public MailSender(final String host, final int port, final String userName,
            final String password) {
        this(host, port, userName, password, false, 0);
    }

    public MailSender(final String host, final int port) {
        this(host, port, null, null, false, 0);
    }

    public MailSender(final String host, final String userName,
            final String password, final boolean ssl, final int portSsl) {
        this(host, DEFAULT_SMTP_PORT, userName, password, ssl, portSsl);
    }

    public MailSender(final String host, final String userName,
            final String password) {
        this(host, DEFAULT_SMTP_PORT, userName, password, false, 0);
    }

    public MailSender(final String host) {
        this(host, DEFAULT_SMTP_PORT, null, null, false, 0);
    }

    // Convertit un texte au format html en texte brut
    private static final String HtmlToText(final String s) {
        final HTMLEditorKit kit = new HTMLEditorKit();
        final Document doc = kit.createDefaultDocument();
        try {
            kit.read(new StringReader(s), doc, 0);
            return doc.getText(0, doc.getLength()).trim();
        } catch (final IOException | BadLocationException ioe) {
            return s;
        }
    }

    // Défini les fichiers à joindre
    private void setAttachmentPart(final String[] attachmentPaths, final MimeMultipart related, final MimeMultipart attachment, final String body, final boolean htmlText) throws MessagingException {
        for (int i = 0; i < attachmentPaths.length; ++i) {
            // Création du fichier à inclure
            final MimeBodyPart messageFilePart = new MimeBodyPart();
            final DataSource source = new FileDataSource(attachmentPaths[i]);
            final String fileName = source.getName();
            messageFilePart.setDataHandler(new DataHandler(source));
            messageFilePart.setFileName(fileName);
            // Image à inclure dans un texte au format HTML ou pièce jointe
            if (htmlText && null != body && body.matches(
                    ".*<img[^>]*src=[\"|']?cid:([\"|']?" + fileName + "[\"|']?)[^>]*>.*")) {
                // " <-- pour éviter une coloration syntaxique désastreuse...
                messageFilePart.setDisposition("inline");
                messageFilePart.setHeader("Content-ID", '<' + fileName + '>');
                related.addBodyPart(messageFilePart);
            } else {
                messageFilePart.setDisposition("attachment");
                attachment.addBodyPart(messageFilePart);
            }
        }
    }

    // Texte alternatif = texte + texte html
    private void setHtmlText(final MimeMultipart related, final MimeMultipart alternative, final String body) throws MessagingException {
        // Plain text
        final BodyPart plainText = new MimeBodyPart();
        plainText.setContent(HtmlToText(body), "text/plain; " + CHARSET);
        alternative.addBodyPart(plainText);
        // Html text ou Html text + images incluses
        final BodyPart htmlText = new MimeBodyPart();
        htmlText.setContent(body, "text/html; " + CHARSET);
        if (0 != related.getCount()) {
            related.addBodyPart(htmlText, 0);
            final MimeBodyPart tmp = new MimeBodyPart();
            tmp.setContent(related);
            alternative.addBodyPart(tmp);
        } else {
            alternative.addBodyPart(htmlText);
        }
    }

    // Définition du corps de l'e-mail
    private void setContent(final Message message, final MimeMultipart alternative, final MimeMultipart attachment, final String body) throws MessagingException {
        if (0 != attachment.getCount()) {
            // Contenu mixte: Pièces jointes +  texte
            if (0 != alternative.getCount() || null != body) {
                // Texte alternatif = texte + texte html
                final MimeBodyPart tmp = new MimeBodyPart();
                tmp.setContent(alternative);
                attachment.addBodyPart(tmp, 0);
            } else {
                // Juste du texte
                final BodyPart plainText = new MimeBodyPart();
                plainText.setContent(body, "text/plain; " + CHARSET);
                attachment.addBodyPart(plainText, 0);
            }
            message.setContent(attachment);
        } else {
            // Juste un message texte
            if (0 != alternative.getCount()) {
                // Texte alternatif = texte + texte html
                message.setContent(alternative);
            } else {
                // Texte
                message.setText(body);
            }
        }
    }

    // Prototype n°1: Envoi de message avec pièce jointe
    public void sendMessage(final MailMessage mailMsg) throws MessagingException {
        final Message message = new MimeMessage(_session);
        // Subect
        message.setSubject(mailMsg.getSubject());
        // Expéditeur
        message.setFrom(mailMsg.getFrom());
        if ((mailMsg.getPiecesJointes() != null) ? mailMsg.getPiecesJointes().getCount() > 0 : false) {
            message.setContent(mailMsg.getPiecesJointes());
        } else {
            message.setText(mailMsg.getText());
        }
        // Destinataires
        message.setRecipients(Message.RecipientType.TO, mailMsg.getTo());
        message.setRecipients(Message.RecipientType.CC, mailMsg.getCc());
        message.setRecipients(Message.RecipientType.BCC, mailMsg.getBcc());
        // Date d'envoi
        message.setSentDate(mailMsg.getSendDate());
        Transport.send(message);
        // Réinitialise le message
        mailMsg.reset();
        System.out.println("Message Envoye !");
    }
}

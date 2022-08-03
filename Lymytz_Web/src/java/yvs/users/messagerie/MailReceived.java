/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users.messagerie;

import java.io.IOException;
import java.security.Security;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

/**
 *
 * @author LYMYTZ
 */
public class MailReceived {

    final private static String CHARSET = "charset=ISO-8859-1";

    final private static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    final private static int DEFAULT_SMTP_PORT = 25;

    private Store _store = null;

    private Folder _defaultFolder = null;

    private Folder _inbox = null;

    private Session _session = null;

    // Constructeur n°1: Connexion au serveur mail
    public MailReceived(final String host, final int port, final String userName, final String password, final boolean ssl, final int portSSL) {
        final String strPort = String.valueOf(port);
        final Properties props = new Properties();
        props.setProperty("mail.store.protocol", "pop3");
        props.put("mail.imap.host", host);
        props.put("mail.imap.port", strPort);
        props.setProperty("mail.imap.starttls.enable", "true");
        props.put("mail.imap.auth", "false");
        if (ssl) {
            // Connection SSL
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            props.put("mail.imap.socketFactory.class", SSL_FACTORY);
            props.put("mail.imap.socketFactory.fallback", "false");
            props.put("mail.imap.auth", "true");
            props.put("mail.imap.socketFactory.port", portSSL);
        }
        if (null == userName || null == password) {
            _session = Session.getDefaultInstance(props, null);
        } else {
            // Connexion avec authentification
            _session = Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            });
        }
        try {
            _store = _session.getStore(new URLName("imap://" + host));
            if (null != userName && null != password) {
                // Connexion avec authentification
                try {
                    _store.connect(userName, password);
                } catch (MessagingException ex) {
                    Logger.getLogger(MailReceived.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(MailReceived.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Autres constructeurs
    public MailReceived(final String host, final int port, final String userName,
            final String password) {
        this(host, port, userName, password, false, 0);
    }

    public MailReceived(final String host, final int port) {
        this(host, port, null, null, false, 0);
    }

    public MailReceived(final String host, final String userName,
            final String password, final boolean ssl, final int portSsl) {
        this(host, DEFAULT_SMTP_PORT, userName, password, ssl, portSsl);
    }

    public MailReceived(final String host, final String userName,
            final String password) {
        this(host, DEFAULT_SMTP_PORT, userName, password, false, 0);
    }

    public MailReceived(final String host) {
        this(host, DEFAULT_SMTP_PORT, null, null, false, 0);
    }

    public void receiveMsg(int messageToReceive) throws IOException {
        try {
            _defaultFolder = _store.getDefaultFolder();
            System.out.println("defaultFolder : " + _defaultFolder.getName());
            for (Folder folder : _defaultFolder.list()) {
                System.out.println(folder.getName());
            }

            _inbox = _defaultFolder.getFolder("INBOX");
            printMessages(_inbox, messageToReceive);
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            close(_inbox);
            close(_defaultFolder);
            try {
                if (_store != null && _store.isConnected()) {
                    _store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

    }

    private void close(Folder folder) {
        if (folder != null && folder.isOpen()) {
            try {
                folder.close(false); // false -> On n'efface pas les messages marqués DELETED
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private void printMessages(Folder folder, int messageToReceive) throws IOException {
        try {
            folder.open(Folder.READ_ONLY);
            int count = folder.getMessageCount();
            int unread = folder.getUnreadMessageCount();
            System.out.println("Il y a " + count + " messages, dont " + unread + " non lus.");
            if (messageToReceive > count) {
                messageToReceive = count;
            }
            for (int i = 0; i < messageToReceive; i++) {
                int messageNumber = count - i;
                Message message = folder.getMessage(messageNumber);
                System.out.println("Message n° " + messageNumber);
                System.out.println("Sujet : " + message.getSubject());

                System.out.println("Expéditeur : ");
                Address[] addresses = message.getFrom();
                for (Address address : addresses) {
                    System.out.println("\t" + address);
                }

                System.out.println("Destinataires : ");
                addresses = message.getRecipients(RecipientType.TO);
                for (Address address : addresses) {
                    System.out.println("\tTo : " + address);
                }
                addresses = message.getRecipients(RecipientType.CC);
                for (Address address : addresses) {
                    System.out.println("\tCopie : " + address);
                }

                System.out.println("Content : ");
//                for (String line : inputStreamToStrings(message.getInputStream())) {
//                    System.out.println(line);
//                }

                System.out.println("-------------");
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

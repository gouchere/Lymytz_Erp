/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users.messagerie;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;

/**
 *
 * @author GOUCHERE YVES
 */
public class Notifications implements Serializable {

    private long id, idNotif, idUser;
    private boolean vue;
    private String destinataire;
    private String title;
    private String messages;
    private Date date;
    private Date heure;

    public Notifications() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public Date getHeure() {
        return heure;
    }

    public void setHeure(Date heure) {
        this.heure = heure;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVue() {
        return vue;
    }

    public void setVue(boolean vue) {
        this.vue = vue;
    }

    public long getIdNotif() {
        return idNotif;
    }

    public void setIdNotif(long idNotif) {
        this.idNotif = idNotif;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Notifications other = (Notifications) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}

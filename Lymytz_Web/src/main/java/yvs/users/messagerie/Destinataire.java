/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users.messagerie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import yvs.users.Users;

/**
 *
 * @author lymytz
 */
public class Destinataire {

    private int id;
    private boolean accuse;
    private Date dateReception;
    private Date heureReception;
    private boolean supp;
    private boolean copie;
    private String sendTo, listSendTo;
    private boolean externe;
    private boolean send_accuse;
    private boolean delete;
    private boolean selectActif;
    private String adresseExterne;
    private Users destinataire = new Users();
    private String adresseExterne_cc;
    private List<Users> destinataires_cc;
    private Conversation message = new Conversation();
    private GroupeMessage groupe = new GroupeMessage();

    public Destinataire() {
        destinataires_cc = new ArrayList<>();
    }

    public Destinataire(int id) {
        this.id = id;
        destinataires_cc = new ArrayList<>();
    }

    public GroupeMessage getGroupe() {
        return groupe;
    }

    public void setGroupe(GroupeMessage groupe) {
        this.groupe = groupe;
    }

    public boolean isSendAccuse() {
        return send_accuse;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public void setSendAccuse(boolean send_accuse) {
        this.send_accuse = send_accuse;
    }

    public int getId() {
        return id;
    }

    public String getListSendTo() {
        return listSendTo;
    }

    public void setListSendTo(String listsendTo) {
        this.listSendTo = listsendTo;
    }

    public String getAdresseExterne_cc() {
        return adresseExterne_cc;
    }

    public void setAdresseExterne_cc(String adresseExterne_cc) {
        this.adresseExterne_cc = adresseExterne_cc;
    }

    public List<Users> getDestinataires_cc() {
        return destinataires_cc;
    }

    public void setDestinataires_cc(List<Users> destinataires_cc) {
        this.destinataires_cc = destinataires_cc;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAccuse() {
        return accuse;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public void setAccuse(boolean accuse) {
        this.accuse = accuse;
    }

    public Date getDateReception() {
        return dateReception;
    }

    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    public Date getHeureReception() {
        return heureReception;
    }

    public void setHeureReception(Date heureReception) {
        this.heureReception = heureReception;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
    }

    public boolean isCopie() {
        return copie;
    }

    public void setCopie(boolean copie) {
        this.copie = copie;
    }

    public boolean isExterne() {
        return externe;
    }

    public void setExterne(boolean externe) {
        this.externe = externe;
    }

    public String getAdresseExterne() {
        return adresseExterne;
    }

    public void setAdresseExterne(String adresseExterne) {
        this.adresseExterne = adresseExterne;
    }

    public Users getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(Users destinataire) {
        this.destinataire = destinataire;
    }

    public Conversation getMessage() {
        return message;
    }

    public void setMessage(Conversation message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Destinataire other = (Destinataire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

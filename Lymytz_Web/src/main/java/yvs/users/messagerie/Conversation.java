/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users.messagerie;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.users.Users;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Conversation implements Serializable {

    private int id;
    private String reference;
    private String objet;
    private String contenu;
    private Date dateEnvoi;
    private Date heureEnvoi;
    private boolean delete;
    private boolean supp;
    private boolean envoyer;
    private boolean accuse;
    private boolean externe;
    private String priorite = "N";
    private Users emetteur = new Users();
    private List<Conversation> conversationList;
    private List<Destinataire> listDestinataire;
    private String pieceJointe;
    
    private boolean conversation = true;
    private int idDestinataire;
    private String nomDestinataire;
    
    private String adresseExterne;
    private String adressesExternes;
    private String adresseExterne_cc;
    
    private String destinataires; 
    private String destinataires_cc;
    private String destinataires_;
    
    private boolean sendCopie;
    private boolean sendMultiple;
    private boolean selectActif;

    public Conversation() {
        conversationList = new ArrayList<>();
        listDestinataire = new ArrayList<>();
    }

    public Conversation(int id) {
        this.id = id;
        conversationList = new ArrayList<>();
        listDestinataire = new ArrayList<>();        
    }

    public boolean isSendMultiple() {
        return sendMultiple;
    }

    public void setSendMultiple(boolean sendMultiple) {
        this.sendMultiple = sendMultiple;
    }

    public String getDestinataires_() {
        return destinataires_;
    }

    public void setDestinataires_(String destinataires_) {
        this.destinataires_ = destinataires_;
    }

    public String getAdressesExternes() {
        return adressesExternes;
    }

    public void setAdressesExternes(String adressesExternes) {
        this.adressesExternes = adressesExternes;
    }

    public boolean isSendCopie() {
        return sendCopie;
    }

    public void setSendCopie(boolean sendCopie) {
        this.sendCopie = sendCopie;
    }

    public String getDestinataires_cc() {
        return destinataires_cc;
    }

    public void setDestinataires_cc(String destinataires_cc) {
        this.destinataires_cc = destinataires_cc;
    }

    public String getNomDestinataire() {
        return nomDestinataire;
    }

    public void setNomDestinataire(String nomDestinataire) {
        this.nomDestinataire = nomDestinataire;
    }

    public String getAdresseExterne() {
        return adresseExterne;
    }

    public boolean isAccuse() {
        return accuse;
    }

    public void setAccuse(boolean accuse) {
        this.accuse = accuse;
    }

    public void setAdresseExterne(String adresse_externe) {
        this.adresseExterne = adresse_externe;
    }

    public boolean isExterne() {
        return externe;
    }

    public void setExterne(boolean externe) {
        this.externe = externe;
    }

    public String getAdresseExterne_cc() {
        return adresseExterne_cc;
    }

    public void setAdresseExterne_cc(String adresseExterne_cc) {
        this.adresseExterne_cc = adresseExterne_cc;
    }

    public boolean isConversation() {
        return conversation;
    }

    public void setConversation(boolean conversation) {
        this.conversation = conversation;
    }

    public int getIdDestinataire() {
        return idDestinataire;
    }

    public void setIdDestinataire(int idDestinataire) {
        this.idDestinataire = idDestinataire;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public List<Destinataire> getListDestinataire() {
        return listDestinataire;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public void setListDestinataire(List<Destinataire> listDestinataire) {
        this.listDestinataire = listDestinataire;
    }

    public String getDestinataires() {
        return destinataires;
    }

    public void setDestinataires(String destinataires) {
        this.destinataires = destinataires;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
    }

    public String getPieceJointe() {
        return pieceJointe;
    }

    public void setPieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isEnvoyer() {
        return envoyer;
    }

    public void setEnvoyer(boolean envoyer) {
        this.envoyer = envoyer;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public Date getHeureEnvoi() {
        return heureEnvoi;
    }

    public void setHeureEnvoi(Date heureEnvoi) {
        this.heureEnvoi = heureEnvoi;
    }

    public Users getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(Users emetteur) {
        this.emetteur = emetteur;
    }

    public List<Conversation> getConversationList() {
        return conversationList;
    }

    public void setConversationList(List<Conversation> conversationList) {
        this.conversationList = conversationList;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
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
        final Conversation other = (Conversation) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}

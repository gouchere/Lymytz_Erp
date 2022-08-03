/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users.messagerie;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class GroupeMessage implements Serializable {

    private int id;
    private String libelle;    
    private String reference;
    private List<Destinataire> listDestinataire;
    private List<GroupeMessage> listDossier;
    private boolean selectActif;
    private String cheminParent;

    public GroupeMessage() {
        listDestinataire = new ArrayList<>();
        listDossier = new ArrayList<>();
    }

    public GroupeMessage(int id) {
        this.id = id;
        listDestinataire = new ArrayList<>();
        listDossier = new ArrayList<>();
    }

    public GroupeMessage(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
        listDestinataire = new ArrayList<>();
        listDossier = new ArrayList<>();
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCheminParent() {
        return cheminParent;
    }

    public void setCheminParent(String cheminParent) {
        this.cheminParent = cheminParent;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<Destinataire> getListDestinataire() {
        return listDestinataire;
    }

    public void setListDestinataire(List<Destinataire> listDestinataire) {
        this.listDestinataire = listDestinataire;
    }

    public List<GroupeMessage> getListDossier() {
        return listDossier;
    }

    public void setListDossier(List<GroupeMessage> listDossier) {
        this.listDossier = listDossier;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
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
        final GroupeMessage other = (GroupeMessage) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}

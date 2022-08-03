/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.print;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.param.YvsSocietes;
import yvs.entity.print.YvsPrintTicketVente;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class PrintTicketVente implements Serializable {

    private long id;
    private String nom;
    private String model;
    private Date dateSave = new Date();
    private boolean defaut = false;
    private boolean viewNameSociete = true;
    private boolean viewRccSociete = true;
    private boolean viewNuiSociete = true;
    private boolean viewPhoneSociete = true;
    private boolean viewAdresseSociete = true;
    private boolean viewDateFacture = true;
    private boolean viewNumeroFacture = true;
    private boolean viewNameVendeur = true;
    private boolean viewNameClient = true;
    private boolean viewPhoneClient = true;
    private boolean viewStatutReglerFacture = true;
    private boolean viewImageQrFacture = true;

    public PrintTicketVente() {
    }

    public PrintTicketVente(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isDefaut() {
        return defaut;
    }

    public void setDefaut(boolean defaut) {
        this.defaut = defaut;
    }

    public boolean isViewNameSociete() {
        return viewNameSociete;
    }

    public void setViewNameSociete(boolean viewNameSociete) {
        this.viewNameSociete = viewNameSociete;
    }

    public boolean isViewRccSociete() {
        return viewRccSociete;
    }

    public void setViewRccSociete(boolean viewRccSociete) {
        this.viewRccSociete = viewRccSociete;
    }

    public boolean isViewNuiSociete() {
        return viewNuiSociete;
    }

    public void setViewNuiSociete(boolean viewNuiSociete) {
        this.viewNuiSociete = viewNuiSociete;
    }

    public boolean isViewPhoneSociete() {
        return viewPhoneSociete;
    }

    public void setViewPhoneSociete(boolean viewPhoneSociete) {
        this.viewPhoneSociete = viewPhoneSociete;
    }

    public boolean isViewAdresseSociete() {
        return viewAdresseSociete;
    }

    public void setViewAdresseSociete(boolean viewAdresseSociete) {
        this.viewAdresseSociete = viewAdresseSociete;
    }

    public boolean isViewStatutReglerFacture() {
        return viewStatutReglerFacture;
    }

    public void setViewStatutReglerFacture(boolean viewStatutReglerFacture) {
        this.viewStatutReglerFacture = viewStatutReglerFacture;
    }

    public boolean isViewImageQrFacture() {
        return viewImageQrFacture;
    }

    public void setViewImageQrFacture(boolean viewImageQrFacture) {
        this.viewImageQrFacture = viewImageQrFacture;
    }

    public boolean isViewDateFacture() {
        return viewDateFacture;
    }

    public void setViewDateFacture(boolean viewDateFacture) {
        this.viewDateFacture = viewDateFacture;
    }

    public boolean isViewNumeroFacture() {
        return viewNumeroFacture;
    }

    public void setViewNumeroFacture(boolean viewNumeroFacture) {
        this.viewNumeroFacture = viewNumeroFacture;
    }

    public boolean isViewNameVendeur() {
        return viewNameVendeur;
    }

    public void setViewNameVendeur(boolean viewNameVendeur) {
        this.viewNameVendeur = viewNameVendeur;
    }

    public boolean isViewNameClient() {
        return viewNameClient;
    }

    public void setViewNameClient(boolean viewNameClient) {
        this.viewNameClient = viewNameClient;
    }

    public boolean isViewPhoneClient() {
        return viewPhoneClient;
    }

    public void setViewPhoneClient(boolean viewPhoneClient) {
        this.viewPhoneClient = viewPhoneClient;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PrintTicketVente other = (PrintTicketVente) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public static PrintTicketVente buildBean(YvsPrintTicketVente e) {
        PrintTicketVente b = new PrintTicketVente();
        if (e != null) {
            b.setId(e.getId());
            b.setModel(e.getModel());
            b.setNom(e.getNom());
            b.setDateSave(e.getDateSave());
            b.setDefaut(e.getDefaut());
            b.setViewNameSociete(e.getViewNameSociete());
            b.setViewDateFacture(e.getViewDateFacture());
            b.setViewNameClient(e.getViewNameClient());
            b.setViewNameVendeur(e.getViewNameVendeur());
            b.setViewNumeroFacture(e.getViewNumeroFacture());
            b.setViewPhoneClient(e.getViewPhoneClient());
            b.setViewRccSociete(e.getViewRccSociete());
            b.setViewNuiSociete(e.getViewNuiSociete());
            b.setViewPhoneSociete(e.getViewPhoneSociete());
            b.setViewAdresseSociete(e.getViewAdresseSociete());
            b.setViewStatutReglerFacture(e.getViewStatutReglerFacture());
            b.setViewImageQrFacture(e.getViewImageQrFacture());
        }
        return b;
    }

    public static YvsPrintTicketVente buildEntity(PrintTicketVente b, YvsSocietes s, YvsUsersAgence a) {
        YvsPrintTicketVente e = new YvsPrintTicketVente();
        if (b != null) {
            e.setId(b.getId());
            e.setModel(b.getModel());
            e.setNom(b.getNom());
            e.setDateSave(b.getDateSave());
            e.setDefaut(b.isDefaut());
            e.setViewNameSociete(b.isViewNameSociete());
            e.setViewDateFacture(b.isViewDateFacture());
            e.setViewNameClient(b.isViewNameClient());
            e.setViewNameVendeur(b.isViewNameVendeur());
            e.setViewNumeroFacture(b.isViewNumeroFacture());
            e.setViewPhoneClient(b.isViewPhoneClient());
            e.setViewRccSociete(b.isViewRccSociete());
            e.setViewNuiSociete(b.isViewNuiSociete());
            e.setViewPhoneSociete(b.isViewPhoneSociete());
            e.setViewAdresseSociete(b.isViewAdresseSociete());
            e.setViewStatutReglerFacture(b.isViewStatutReglerFacture());
            e.setViewImageQrFacture(b.isViewImageQrFacture());
            e.setDateUpdate(new Date());
            e.setAuthor(a);
            e.setSociete(s);
        }
        return e;
    }

}

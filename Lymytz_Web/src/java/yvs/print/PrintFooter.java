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
import yvs.entity.print.YvsPrintFooter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class PrintFooter implements Serializable {

    protected long id;
    protected String nom;
    protected String model;
    protected Date dateSave = new Date();
    protected boolean viewAdresseSociete = true;
    protected boolean viewSiegeSociete = true;
    protected boolean viewBpSociete = true;
    protected boolean viewFaxSociete = true;
    protected boolean viewEmailSociete = true;
    protected boolean viewPhoneSociete = true;
    protected boolean viewCapitalSociete = true;
    protected boolean viewSiteSociete = true;
    protected boolean viewContribSociete = true;
    protected boolean viewRegistrSociete = true;
    protected boolean viewAgreementSociete = true;

    public PrintFooter() {
    }

    public PrintFooter(long id) {
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

    public boolean isViewAdresseSociete() {
        return viewAdresseSociete;
    }

    public void setViewAdresseSociete(boolean viewAdresseSociete) {
        this.viewAdresseSociete = viewAdresseSociete;
    }

    public boolean isViewSiegeSociete() {
        return viewSiegeSociete;
    }

    public void setViewSiegeSociete(boolean viewSiegeSociete) {
        this.viewSiegeSociete = viewSiegeSociete;
    }

    public boolean isViewBpSociete() {
        return viewBpSociete;
    }

    public void setViewBpSociete(boolean viewBpSociete) {
        this.viewBpSociete = viewBpSociete;
    }

    public boolean isViewFaxSociete() {
        return viewFaxSociete;
    }

    public void setViewFaxSociete(boolean viewFaxSociete) {
        this.viewFaxSociete = viewFaxSociete;
    }

    public boolean isViewEmailSociete() {
        return viewEmailSociete;
    }

    public void setViewEmailSociete(boolean viewEmailSociete) {
        this.viewEmailSociete = viewEmailSociete;
    }

    public boolean isViewPhoneSociete() {
        return viewPhoneSociete;
    }

    public void setViewPhoneSociete(boolean viewPhoneSociete) {
        this.viewPhoneSociete = viewPhoneSociete;
    }

    public boolean isViewSiteSociete() {
        return viewSiteSociete;
    }

    public void setViewSiteSociete(boolean viewSiteSociete) {
        this.viewSiteSociete = viewSiteSociete;
    }

    public boolean isViewContribSociete() {
        return viewContribSociete;
    }

    public void setViewContribSociete(boolean viewContribSociete) {
        this.viewContribSociete = viewContribSociete;
    }

    public boolean isViewRegistrSociete() {
        return viewRegistrSociete;
    }

    public void setViewRegistrSociete(boolean viewRegistrSociete) {
        this.viewRegistrSociete = viewRegistrSociete;
    }

    public boolean isViewCapitalSociete() {
        return viewCapitalSociete;
    }

    public void setViewCapitalSociete(boolean viewCapitalSociete) {
        this.viewCapitalSociete = viewCapitalSociete;
    }

    public boolean isViewAgreementSociete() {
        return viewAgreementSociete;
    }

    public void setViewAgreementSociete(boolean viewAgreementSociete) {
        this.viewAgreementSociete = viewAgreementSociete;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PrintFooter other = (PrintFooter) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public static PrintFooter buildBean(YvsPrintFooter e) {
        PrintFooter b = new PrintFooter();
        if (e != null) {
            b.setId(e.getId());
            b.setDateSave(e.getDateSave());
            b.setNom(e.getNom());
            b.setModel(e.getModel());
            b.setViewAdresseSociete(e.getViewAdresseSociete());
            b.setViewBpSociete(e.getViewBpSociete());
            b.setViewContribSociete(e.getViewContribSociete());
            b.setViewEmailSociete(e.getViewEmailSociete());
            b.setViewFaxSociete(e.getViewFaxSociete());
            b.setViewPhoneSociete(e.getViewPhoneSociete());
            b.setViewRegistrSociete(e.getViewRegistrSociete());
            b.setViewSiegeSociete(e.getViewSiegeSociete());
            b.setViewSiteSociete(e.getViewSiteSociete());
            b.setViewCapitalSociete(e.getViewCapitalSociete());
            b.setViewAgreementSociete(e.getViewAgreementSociete());
        }
        return b;
    }

    public static YvsPrintFooter buildEntity(PrintFooter b, YvsSocietes s, YvsUsersAgence a) {
        YvsPrintFooter e = new YvsPrintFooter();
        if (b != null) {
            e.setId(b.getId());
            e.setDateSave(b.getDateSave());
            e.setNom(b.getNom());
            e.setModel(b.getModel());
            e.setViewAdresseSociete(b.isViewAdresseSociete());
            e.setViewBpSociete(b.isViewBpSociete());
            e.setViewContribSociete(b.isViewContribSociete());
            e.setViewEmailSociete(b.isViewEmailSociete());
            e.setViewFaxSociete(b.isViewFaxSociete());
            e.setViewPhoneSociete(b.isViewPhoneSociete());
            e.setViewRegistrSociete(b.isViewRegistrSociete());
            e.setViewSiegeSociete(b.isViewSiegeSociete());
            e.setViewSiteSociete(b.isViewSiteSociete());
            e.setViewCapitalSociete(b.isViewCapitalSociete());
            e.setViewAgreementSociete(b.isViewAgreementSociete());
            e.setDateUpdate(new Date());
            e.setAuthor(a);
            e.setSociete(s);
        }
        return e;
    }

}

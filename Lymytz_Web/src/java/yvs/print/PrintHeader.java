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
import yvs.entity.print.YvsPrintHeader;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class PrintHeader extends PrintFooter implements Serializable {

    private boolean viewNameAgence = true;
    private boolean viewNameSociete = true;
    private boolean viewCodeSociete = true;
    private boolean viewDescriptionSociete = true;
    private boolean viewLogoSociete = true;

    public PrintHeader() {
    }

    public PrintHeader(long id) {
        this.id = id;
    }

    public boolean isViewNameAgence() {
        return viewNameAgence;
    }

    public void setViewNameAgence(boolean viewNameAgence) {
        this.viewNameAgence = viewNameAgence;
    }

    public boolean isViewNameSociete() {
        return viewNameSociete;
    }

    public void setViewNameSociete(boolean viewNameSociete) {
        this.viewNameSociete = viewNameSociete;
    }

    public boolean isViewCodeSociete() {
        return viewCodeSociete;
    }

    public void setViewCodeSociete(boolean viewCodeSociete) {
        this.viewCodeSociete = viewCodeSociete;
    }

    public boolean isViewDescriptionSociete() {
        return viewDescriptionSociete;
    }

    public void setViewDescriptionSociete(boolean viewDescriptionSociete) {
        this.viewDescriptionSociete = viewDescriptionSociete;
    }

    public boolean isViewLogoSociete() {
        return viewLogoSociete;
    }

    public void setViewLogoSociete(boolean viewLogoSociete) {
        this.viewLogoSociete = viewLogoSociete;
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
        final PrintHeader other = (PrintHeader) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public static PrintHeader buildBean(YvsPrintHeader e) {
        PrintHeader b = new PrintHeader();
        if (e != null) {
            b.setId(e.getId());
            b.setDateSave(e.getDateSave());
            b.setNom(e.getNom());
            b.setModel(e.getModel());
            b.setViewCodeSociete(e.getViewCodeSociete());
            b.setViewDescriptionSociete(e.getViewDescriptionSociete());
            b.setViewAdresseSociete(e.getViewAdresseSociete());
            b.setViewBpSociete(e.getViewBpSociete());
            b.setViewContribSociete(e.getViewContribSociete());
            b.setViewEmailSociete(e.getViewEmailSociete());
            b.setViewFaxSociete(e.getViewFaxSociete());
            b.setViewLogoSociete(e.getViewLogoSociete());
            b.setViewNameAgence(e.getViewNameAgence());
            b.setViewNameSociete(e.getViewNameSociete());
            b.setViewPhoneSociete(e.getViewPhoneSociete());
            b.setViewRegistrSociete(e.getViewRegistrSociete());
            b.setViewSiegeSociete(e.getViewSiegeSociete());
            b.setViewSiteSociete(e.getViewSiteSociete());
            b.setViewCapitalSociete(e.getViewCapitalSociete());
        }
        return b;
    }

    public static YvsPrintHeader buildEntity(PrintHeader b, YvsSocietes s, YvsUsersAgence a) {
        YvsPrintHeader e = new YvsPrintHeader();
        if (b != null) {
            e.setId(b.getId());
            e.setDateSave(b.getDateSave());
            e.setNom(b.getNom());
            e.setModel(b.getModel());
            e.setViewAdresseSociete(b.isViewAdresseSociete());
            e.setViewCodeSociete(b.isViewCodeSociete());
            e.setViewDescriptionSociete(b.isViewDescriptionSociete());
            e.setViewBpSociete(b.isViewBpSociete());
            e.setViewContribSociete(b.isViewContribSociete());
            e.setViewEmailSociete(b.isViewEmailSociete());
            e.setViewFaxSociete(b.isViewFaxSociete());
            e.setViewLogoSociete(b.isViewLogoSociete());
            e.setViewNameAgence(b.isViewNameAgence());
            e.setViewNameSociete(b.isViewNameSociete());
            e.setViewPhoneSociete(b.isViewPhoneSociete());
            e.setViewRegistrSociete(b.isViewRegistrSociete());
            e.setViewSiegeSociete(b.isViewSiegeSociete());
            e.setViewSiteSociete(b.isViewSiteSociete());
            e.setViewCapitalSociete(b.isViewCapitalSociete());
            e.setDateUpdate(new Date());
            e.setAuthor(a);
            e.setSociete(s);
        }
        return e;
    }

}

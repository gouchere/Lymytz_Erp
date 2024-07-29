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
import yvs.entity.print.YvsPrintFactureVente;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class PrintFactureVente implements Serializable {

    private long id;
    private String nom;
    private String model;
    private Date dateSave = new Date();
    private boolean defaut = false;
    private boolean viewDateFacture = true;
    private boolean viewNumeroFacture = true;
    private boolean viewTaxeFacture = true;
    private boolean viewCoutFacture = true;
    private boolean viewServiceFacture = true;
    private boolean viewReglementFacture = true;
    private boolean viewStatutFacture = true;
    private boolean viewNameVendeur = true;
    private boolean viewPointVente = true;
    private boolean viewNameClient = true;
    private boolean viewPhoneClient = true;
    private boolean viewAdresseClient = true;
    private boolean viewNuiClient = true;
    private boolean viewRccClient = true;
    private boolean viewSignatureVendeur = true;
    private boolean viewSignatureCaissier = true;
    private boolean viewSignatureClient = true;
    private boolean viewImagePayer = true;
    private boolean viewImageLivrer = true;
    private boolean viewImpayeVente = true;
    private boolean viewPenaliteFacture = true;
    private PrintHeader header = new PrintHeader();
    private PrintFooter footer = new PrintFooter();

    public PrintFactureVente() {
    }

    public PrintFactureVente(long id) {
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

    public boolean isViewTaxeFacture() {
        return viewTaxeFacture;
    }

    public void setViewTaxeFacture(boolean viewTaxeFacture) {
        this.viewTaxeFacture = viewTaxeFacture;
    }

    public boolean isViewCoutFacture() {
        return viewCoutFacture;
    }

    public void setViewCoutFacture(boolean viewCoutFacture) {
        this.viewCoutFacture = viewCoutFacture;
    }

    public boolean isViewServiceFacture() {
        return viewServiceFacture;
    }

    public void setViewServiceFacture(boolean viewServiceFacture) {
        this.viewServiceFacture = viewServiceFacture;
    }

    public boolean isViewReglementFacture() {
        return viewReglementFacture;
    }

    public void setViewReglementFacture(boolean viewReglementFacture) {
        this.viewReglementFacture = viewReglementFacture;
    }

    public boolean isViewStatutFacture() {
        return viewStatutFacture;
    }

    public void setViewStatutFacture(boolean viewStatutFacture) {
        this.viewStatutFacture = viewStatutFacture;
    }

    public boolean isViewNameVendeur() {
        return viewNameVendeur;
    }

    public void setViewNameVendeur(boolean viewNameVendeur) {
        this.viewNameVendeur = viewNameVendeur;
    }

    public boolean isViewPointVente() {
        return viewPointVente;
    }

    public void setViewPointVente(boolean viewPointVente) {
        this.viewPointVente = viewPointVente;
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

    public boolean isViewAdresseClient() {
        return viewAdresseClient;
    }

    public void setViewAdresseClient(boolean viewAdresseClient) {
        this.viewAdresseClient = viewAdresseClient;
    }

    public boolean isViewNuiClient() {
        return viewNuiClient;
    }

    public void setViewNuiClient(boolean viewNuiClient) {
        this.viewNuiClient = viewNuiClient;
    }

    public boolean isViewRccClient() {
        return viewRccClient;
    }

    public void setViewRccClient(boolean viewRccClient) {
        this.viewRccClient = viewRccClient;
    }

    public boolean isViewSignatureVendeur() {
        return viewSignatureVendeur;
    }

    public boolean isViewImpayeVente() {
        return viewImpayeVente;
    }

    public void setViewImpayeVente(boolean viewImpayeVente) {
        this.viewImpayeVente = viewImpayeVente;
    }

    public boolean isViewPenaliteFacture() {
        return viewPenaliteFacture;
    }

    public void setViewPenaliteFacture(boolean viewPenaliteFacture) {
        this.viewPenaliteFacture = viewPenaliteFacture;
    }

    public void setViewSignatureVendeur(boolean viewSignatureVendeur) {
        this.viewSignatureVendeur = viewSignatureVendeur;
    }

    public boolean isViewSignatureCaissier() {
        return viewSignatureCaissier;
    }

    public void setViewSignatureCaissier(boolean viewSignatureCaissier) {
        this.viewSignatureCaissier = viewSignatureCaissier;
    }

    public boolean isViewSignatureClient() {
        return viewSignatureClient;
    }

    public void setViewSignatureClient(boolean viewSignatureClient) {
        this.viewSignatureClient = viewSignatureClient;
    }

    public boolean isViewImagePayer() {
        return viewImagePayer;
    }

    public void setViewImagePayer(boolean viewImagePayer) {
        this.viewImagePayer = viewImagePayer;
    }

    public boolean isViewImageLivrer() {
        return viewImageLivrer;
    }

    public void setViewImageLivrer(boolean viewImageLivrer) {
        this.viewImageLivrer = viewImageLivrer;
    }

    public PrintFooter getFooter() {
        return footer;
    }

    public void setFooter(PrintFooter footer) {
        this.footer = footer;
    }

    public PrintHeader getHeader() {
        return header;
    }

    public void setHeader(PrintHeader header) {
        this.header = header;
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
        final PrintFactureVente other = (PrintFactureVente) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public static PrintFactureVente buildBean(YvsPrintFactureVente e) {
        PrintFactureVente b = new PrintFactureVente();
        if (e != null) {
            b.setHeader(PrintHeader.buildBean(e.getHeader()));
            b.setFooter(PrintFooter.buildBean(e.getFooter()));
            b.setId(e.getId());
            b.setModel(e.getModel());
            b.setNom(e.getNom());
            b.setDateSave(e.getDateSave());
            b.setDefaut(e.getDefaut());
            b.setViewCoutFacture(e.getViewCoutFacture());
            b.setViewDateFacture(e.getViewDateFacture());
            b.setViewNameClient(e.getViewNameClient());
            b.setViewNameVendeur(e.getViewNameVendeur());
            b.setViewNumeroFacture(e.getViewNumeroFacture());
            b.setViewPhoneClient(e.getViewPhoneClient());
            b.setViewAdresseClient(e.getViewAdresseClient());
            b.setViewNuiClient(e.getViewNuiClient());
            b.setViewRccClient(e.getViewRccClient());
            b.setViewPointVente(e.getViewPointVente());
            b.setViewReglementFacture(e.getViewReglementFacture());
            b.setViewServiceFacture(e.getViewServiceFacture());
            b.setViewSignatureCaissier(e.getViewSignatureCaissier());
            b.setViewSignatureClient(e.getViewSignatureClient());
            b.setViewSignatureVendeur(e.getViewSignatureVendeur());
            b.setViewStatutFacture(e.getViewStatutFacture());
            b.setViewTaxeFacture(e.getViewTaxeFacture());
            b.setViewImageLivrer(e.getViewImageLivrer());
            b.setViewImagePayer(e.getViewImagePayer());
            b.setViewPenaliteFacture(e.getViewPenaliteFacture());
            b.setViewImpayeVente(e.getViewImpayeVente());
        }
        return b;
    }

    public static YvsPrintFactureVente buildEntity(PrintFactureVente b, YvsSocietes s, YvsUsersAgence a) {
        YvsPrintFactureVente e = new YvsPrintFactureVente();
        if (b != null) {
            e.setId(b.getId());
            e.setModel(b.getModel());
            e.setNom(b.getNom());
            e.setDateSave(b.getDateSave());
            e.setDefaut(b.isDefaut());
            e.setViewCoutFacture(b.isViewCoutFacture());
            e.setViewDateFacture(b.isViewDateFacture());
            e.setViewNameClient(b.isViewNameClient());
            e.setViewNameVendeur(b.isViewNameVendeur());
            e.setViewNumeroFacture(b.isViewNumeroFacture());
            e.setViewPhoneClient(b.isViewPhoneClient());
            e.setViewAdresseClient(b.isViewAdresseClient());
            e.setViewNuiClient(b.isViewNuiClient());
            e.setViewRccClient(b.isViewRccClient());
            e.setViewPointVente(b.isViewPointVente());
            e.setViewReglementFacture(b.isViewReglementFacture());
            e.setViewServiceFacture(b.isViewServiceFacture());
            e.setViewSignatureCaissier(b.isViewSignatureCaissier());
            e.setViewSignatureClient(b.isViewSignatureClient());
            e.setViewSignatureVendeur(b.isViewSignatureVendeur());
            e.setViewStatutFacture(b.isViewStatutFacture());
            e.setViewTaxeFacture(b.isViewTaxeFacture());
            e.setViewImageLivrer(b.isViewImageLivrer());
            e.setViewImagePayer(b.isViewImagePayer());
            e.setViewPenaliteFacture(b.isViewPenaliteFacture());
            e.setViewImpayeVente(b.isViewImpayeVente());
            if (b.getHeader() != null ? b.getHeader().getId() > 0 : false) {
                e.setHeader(PrintHeader.buildEntity(b.getHeader(), s, a));
            }
            if (b.getFooter() != null ? b.getFooter().getId() > 0 : false) {
                e.setFooter(PrintFooter.buildEntity(b.getFooter(), s, a));
            }
            e.setDateUpdate(new Date());
            e.setAuthor(a);
            e.setSociete(s);
        }
        return e;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.entrepot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.achat.LotReception;
import yvs.commercial.creneau.Creneau;
import yvs.commercial.depot.Emplacement;
import yvs.commercial.depot.PointVenteDepot;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseDepotOperation;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.grh.bean.Employe;
import yvs.parametrage.agence.Agence;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Depots implements Serializable, Comparable {

    private long id;
    private boolean supp;
    private boolean actif = true;
    private boolean opAchat;
    private boolean opVente;
    private boolean opProduction;
    private boolean opTransit;
    private boolean opRetour;
    private boolean opReserv;
    private boolean opTechnique;
    private boolean crenau = true;
    private boolean controlStock;
    private boolean requiereLot = false ;
    private String abbreviation;
    private String adresse;
    private String code;
    private String description;
    private String designation;
    private Agence agence = new Agence();
    private Employe responsable = new Employe();
    private String codeAcces;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private boolean selectActif, new_;
    private double stock;

    private boolean typeMp, typePsf, typePf, typeNegoce;
    private boolean verifyAppro, verifyAllValidInventaire = true;
    private List<YvsBaseArticleDepot> articles;
    private List<LotReception> lots;
    private List<Emplacement> emplacements;
    private List<LiaisonDepot> liaisons;
    private List<Creneau> creneaux;
    private List<YvsComCreneauDepot> tranches;
    private List<PointVenteDepot> points;
    private List<YvsBaseDepotOperation> operations;

    private boolean error;

    public Depots() {
        lots = new ArrayList<>();
        operations = new ArrayList<>();
        creneaux = new ArrayList<>();
        liaisons = new ArrayList<>();
        articles = new ArrayList<>();
        emplacements = new ArrayList<>();
        points = new ArrayList<>();
        tranches = new ArrayList<>();
    }

    public Depots(long id) {
        this();
        this.id = id;
    }

    public Depots(long id, String designation) {
        this(id);
        this.designation = designation;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public boolean isOpTechnique() {
        return opTechnique;
    }

    public void setOpTechnique(boolean opTechnique) {
        this.opTechnique = opTechnique;
    }

    public boolean isOpRetour() {
        return opRetour;
    }

    public void setOpRetour(boolean opRetour) {
        this.opRetour = opRetour;
    }

    public boolean isOpReserv() {
        return opReserv;
    }

    public void setOpReserv(boolean opReserv) {
        this.opReserv = opReserv;
    }

    public boolean isVerifyAllValidInventaire() {
        return verifyAllValidInventaire;
    }

    public void setVerifyAllValidInventaire(boolean verifyAllValidInventaire) {
        this.verifyAllValidInventaire = verifyAllValidInventaire;
    }

    public List<PointVenteDepot> getPoints() {
        return points;
    }

    public void setPoints(List<PointVenteDepot> points) {
        this.points = points;
    }

    public List<Creneau> getCreneaux() {
        return creneaux;
    }

    public void setCreneaux(List<Creneau> creneaux) {
        this.creneaux = creneaux;
    }

    public List<Emplacement> getEmplacements() {
        return emplacements;
    }

    public void setEmplacements(List<Emplacement> emplacements) {
        this.emplacements = emplacements;
    }

    public List<LiaisonDepot> getLiaisons() {
        return liaisons;
    }

    public void setLiaisons(List<LiaisonDepot> liaisons) {
        this.liaisons = liaisons; 
    }

    public List<LotReception> getLots() {
        return lots;
    }

    public void setLots(List<LotReception> lots) {
        this.lots = lots;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<YvsBaseArticleDepot> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticleDepot> articles) {
        this.articles = articles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isOpAchat() {
        return opAchat;
    }

    public void setOpAchat(boolean opAchat) {
        this.opAchat = opAchat;
    }

    public boolean isOpVente() {
        return opVente;
    }

    public void setOpVente(boolean opVente) {
        this.opVente = opVente;
    }

    public boolean isOpProduction() {
        return opProduction;
    }

    public void setOpProduction(boolean opProduction) {
        this.opProduction = opProduction;
    }

    public boolean isOpTransit() {
        return opTransit;
    }

    public void setOpTransit(boolean opTransit) {
        this.opTransit = opTransit;
    }

    public boolean isCrenau() {
        return crenau;
    }

    public void setCrenau(boolean crenau) {
        this.crenau = crenau;
    }

    public boolean isRequiereLot() {
        return requiereLot;
    }

    public void setRequiereLot(boolean requiereLot) {
        this.requiereLot = requiereLot;
    }

    public boolean isControlStock() {
        return controlStock;
    }

    public void setControlStock(boolean controlStock) {
        this.controlStock = controlStock;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public Employe getResponsable() {
        return responsable;
    }

    public void setResponsable(Employe responsable) {
        this.responsable = responsable;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public List<YvsBaseDepotOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<YvsBaseDepotOperation> operations) {
        this.operations = operations;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isTypeMp() {
        return typeMp;
    }

    public void setTypeMp(boolean typeMp) {
        this.typeMp = typeMp;
    }

    public boolean isTypePsf() {
        return typePsf;
    }

    public void setTypePsf(boolean typePsf) {
        this.typePsf = typePsf;
    }

    public boolean isTypePf() {
        return typePf;
    }

    public void setTypePf(boolean typePf) {
        this.typePf = typePf;
    }

    public boolean isTypeNegoce() {
        return typeNegoce;
    }

    public void setTypeNegoce(boolean typeNegoce) {
        this.typeNegoce = typeNegoce;
    }

    public boolean isVerifyAppro() {
        return verifyAppro;
    }

    public void setVerifyAppro(boolean verifyAppro) {
        this.verifyAppro = verifyAppro;
    }

    public List<YvsComCreneauDepot> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsComCreneauDepot> tranches) {
        this.tranches = tranches;
    }

    public String getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(String codeAcces) {
        this.codeAcces = codeAcces;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Depots other = (Depots) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        Depots d = (Depots) o;
        if (designation.equals(d.designation)) {
            return Long.valueOf(id).compareTo(d.id);
        }
        return designation.compareTo(d.designation);
    }

}

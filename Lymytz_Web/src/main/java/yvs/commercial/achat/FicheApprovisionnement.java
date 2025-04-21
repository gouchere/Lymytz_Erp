/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.achat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.creneau.Creneau;
import yvs.commercial.stock.DocStock;
import yvs.entity.commercial.achat.YvsComArticleApprovisionnement;
import yvs.entity.param.workflow.YvsWorkflowValidApprovissionnement;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class FicheApprovisionnement implements Serializable, Comparable {

    private long id;
    private Date dateApprovisionnement = new Date();
    private Date heureApprovisionnement = new Date();
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private List<YvsComArticleApprovisionnement> Articles;
    private String etat = Constantes.ETAT_EDITABLE, nameEtat = Constantes.ETAT_EDITABLE;
    private String statutTerminer = Constantes.ETAT_ATTENTE;
    private String reference;
    private Depots depot = new Depots();
    private Creneau creneau = new Creneau();
    private List<DocAchat> documents;
    private List<DocStock> documents_;
    private boolean selectActif, new_, int_, update, cloturer, auto;
    private int etapeValide;
    private int etapeTotal;
    private List<YvsWorkflowValidApprovissionnement> etapesValidations;
    private String firstEtape = "VALIDER";

    public FicheApprovisionnement() {
        Articles = new ArrayList<>();
        documents = new ArrayList<>();
        documents_ = new ArrayList<>();
        etapesValidations = new ArrayList<>();
    }

    public FicheApprovisionnement(long id) {
        this();
        this.id = id;
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

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public boolean isCloturer() {
        return cloturer;
    }

    public void setCloturer(boolean cloturer) {
        this.cloturer = cloturer;
    }

    public List<YvsComArticleApprovisionnement> getArticles() {
        return Articles;
    }

    public void setArticles(List<YvsComArticleApprovisionnement> Articles) {
        this.Articles = Articles;
    }

    public boolean isUpdate() {
        return id > 0;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public Creneau getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

    public String getFirstEtape() {
        return firstEtape;
    }

    public void setFirstEtape(String firstEtape) {
        this.firstEtape = firstEtape;
    }

    public List<DocStock> getDocuments_() {
        return documents_;
    }

    public void setDocuments_(List<DocStock> documents_) {
        this.documents_ = documents_;
    }

    public String getNameEtat() {
        return nameEtat;
    }

    public void setNameEtat(String nameEtat) {
        this.nameEtat = nameEtat;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<DocAchat> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocAchat> documents) {
        this.documents = documents;
    }

    public boolean isInt_() {
        return int_;
    }

    public void setInt_(boolean int_) {
        this.int_ = int_;
    }

    public String getStatutTerminer() {
        return statutTerminer != null ? statutTerminer.trim().length() > 0 ? statutTerminer : Constantes.ETAT_ATTENTE : Constantes.ETAT_ATTENTE;
    }

    public void setStatutTerminer(String statutTerminer) {
        this.statutTerminer = statutTerminer;
    }

    public int getEtapeValide() {
        return etapeValide;
    }

    public void setEtapeValide(int etapeValide) {
        this.etapeValide = etapeValide;
    }

    public int getEtapeTotal() {
        return etapeTotal;
    }

    public void setEtapeTotal(int etapeTotal) {
        this.etapeTotal = etapeTotal;
    }

    public String getEtat() {
        return etat != null ? etat.trim().length() > 0 ? etat : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateApprovisionnement() {
        return dateApprovisionnement != null ? dateApprovisionnement : new Date();
    }

    public void setDateApprovisionnement(Date dateApprovisionnement) {
        this.dateApprovisionnement = dateApprovisionnement;
    }

    public Date getHeureApprovisionnement() {
        return heureApprovisionnement;
    }

    public void setHeureApprovisionnement(Date heureApprovisionnement) {
        this.heureApprovisionnement = heureApprovisionnement;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
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

    public List<YvsWorkflowValidApprovissionnement> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidApprovissionnement> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final FicheApprovisionnement other = (FicheApprovisionnement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        FicheApprovisionnement m = (FicheApprovisionnement) o;
        if (dateApprovisionnement.equals(m.dateApprovisionnement)) {
            if (heureApprovisionnement.equals(m.heureApprovisionnement)) {
                return Long.valueOf(id).compareTo(m.id);
            }
            return heureApprovisionnement.compareTo(m.heureApprovisionnement);
        }
        return dateApprovisionnement.compareTo(m.dateApprovisionnement);
    }

}

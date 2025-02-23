/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.stock;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.commercial.Qualite;
import yvs.commercial.achat.DocAchat;
import yvs.commercial.achat.LotReception;
import yvs.commercial.rations.DocRations;
import yvs.commercial.vente.DocVente;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class MouvementStock implements Serializable, Comparable {

    private long id;
    private double quantite, reste, quantiteEntree;
    private Date dateDoc = new Date(), dateDebut = new Date(), dateFin = new Date();
    private String mouvement = Constantes.MOUV_CAISS_SORTIE;
    private boolean supp = false;
    private boolean actif = true;
    private boolean calculPr;
    private long idExterne;
    private String tableExterne;
    private String description;
    private Depots depot = new Depots();
    private Depots destination = new Depots();
    private LotReception lot = new LotReception();
    private double cout, coutStock, coutEntree;
    private int periode;
    private Articles article = new Articles();
    private TrancheHoraire tranche = new TrancheHoraire();
    private MouvementStock parent;
    private boolean select, update, new_;
    private DocVente vente = new DocVente();
    private DocAchat achat = new DocAchat();
    private Qualite qualite = new Qualite();
    private DocRations ration = new DocRations();
    private DocStock stock = new DocStock();
    private Conditionnement conditionnement = new Conditionnement(), conditionnementEntree = new Conditionnement();
    private Date dateSave = new Date();
    private Date lastDatePr = null;

    public MouvementStock() {
    }

    public MouvementStock(long id) {
        this.id = id;
    }

    public boolean isCalculPr() {
        return calculPr;
    }

    public void setCalculPr(boolean calculPr) {
        this.calculPr = calculPr;
    }

    public double getQuantiteEntree() {
        return quantiteEntree;
    }

    public void setQuantiteEntree(double quantiteEntree) {
        this.quantiteEntree = quantiteEntree;
    }

    public Conditionnement getConditionnementEntree() {
        return conditionnementEntree;
    }

    public void setConditionnementEntree(Conditionnement conditionnementEntree) {
        this.conditionnementEntree = conditionnementEntree;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public LotReception getLot() {
        return lot;
    }

    public void setLot(LotReception lot) {
        this.lot = lot;
    }

    public Qualite getQualite() {
        return qualite;
    }

    public void setQualite(Qualite qualite) {
        this.qualite = qualite;
    }

    public double getCoutEntree() {
        return coutEntree;
    }

    public void setCoutEntree(double coutEntree) {
        this.coutEntree = coutEntree;
    }

    public TrancheHoraire getTranche() {
        return tranche;
    }

    public void setTranche(TrancheHoraire tranche) {
        this.tranche = tranche;
    }

    public double getCoutStock() {
        return coutStock;
    }

    public void setCoutStock(double coutStock) {
        this.coutStock = coutStock;
    }

    public int getPeriode() {
        return periode;
    }

    public void setPeriode(int periode) {
        this.periode = periode;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public double getReste() {
        return reste;
    }

    public void setReste(double reste) {
        this.reste = reste;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public MouvementStock getParent() {
        return parent;
    }

    public void setParent(MouvementStock parent) {
        this.parent = parent;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Date getDateDoc() {
        return dateDoc;
    }

    public void setDateDoc(Date dateDoc) {
        this.dateDoc = dateDoc;
    }

    public String getMouvement() {
        return mouvement;
    }

    public void setMouvement(String mouvement) {
        this.mouvement = mouvement;
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

    public long getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(long idExterne) {
        this.idExterne = idExterne;
    }

    public String getTableExterne() {
        return tableExterne;
    }

    public void setTableExterne(String tableExterne) {
        this.tableExterne = tableExterne;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public Depots getDestination() {
        return destination;
    }

    public void setDestination(Depots destination) {
        this.destination = destination;
    }

    public DocVente getVente() {
        return vente;
    }

    public void setVente(DocVente vente) {
        this.vente = vente;
    }

    public DocAchat getAchat() {
        return achat;
    }

    public void setAchat(DocAchat achat) {
        this.achat = achat;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public DocRations getRation() {
        return ration;
    }

    public void setRation(DocRations ration) {
        this.ration = ration;
    }

    public DocStock getStock() {
        return stock;
    }

    public void setStock(DocStock stock) {
        this.stock = stock;
    }

    public Date getLastDatePr() {
        return lastDatePr;
    }

    public void setLastDatePr(Date lastDatePr) {
        this.lastDatePr = lastDatePr;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final MouvementStock other = (MouvementStock) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        MouvementStock m = (MouvementStock) o;
        if (dateDoc.equals(m.dateDoc)) {
            if (article.equals(m.article)) {
                if (mouvement.equals(m.mouvement)) {
                    if (tableExterne.equals(m.tableExterne)) {
                        if (Long.valueOf(idExterne).equals(m.idExterne)) {
                            return Long.valueOf(id).compareTo(m.id);
                        }
                        return Long.valueOf(idExterne).compareTo(m.idExterne);
                    }
                    return tableExterne.compareTo(m.tableExterne);
                }
                return mouvement.compareTo(m.mouvement);
            }
//            return article.compareTo(m.article);
        }
        return dateDoc.compareTo(m.dateDoc);
    }

}

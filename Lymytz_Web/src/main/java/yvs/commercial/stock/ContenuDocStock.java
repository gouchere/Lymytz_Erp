/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.commercial.Qualite;
import yvs.commercial.achat.LotReception;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.users.Users;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ContenuDocStock implements Serializable {

    private long id;
    private double quantite, qteAttente, resultante;
    private double prix, prixEntree;
    private boolean supp;
    private boolean actif;
    private Date dateContenu = new Date();
    private double prixTotal = 0;
    private Users author = new Users();
    private Date dateSave = new Date();
    private Date dateReception = new Date();
    private boolean calculPr = true;
    private DocStock docStock = new DocStock();
    private Qualite qualite = new Qualite();
    private Qualite qualiteEntree = new Qualite();
    private Articles article = new Articles();
    private boolean selectActif, new_, update;
    private boolean impactValeurInventaire = true;
    private String commentaire, numSerie, statut;
    private ContenuDocStock parent;
    private LotReception lotSortie = new LotReception();
    private LotReception lotEntree = new LotReception();
    private Conditionnement conditionnement = new Conditionnement();
    private Conditionnement uniteDestination = new Conditionnement();
    private List<YvsComLotReception> lots;

    public ContenuDocStock() {
        lots = new ArrayList<>();
    }

    public ContenuDocStock(long id) {
        this();
        this.id = id;
    }

    public double getPrixEntree() {
        return prixEntree;
    }

    public void setPrixEntree(double prixEntree) {
        this.prixEntree = prixEntree;
    }

    public Qualite getQualiteEntree() {
        return qualiteEntree;
    }

    public void setQualiteEntree(Qualite qualiteEntree) {
        this.qualiteEntree = qualiteEntree;
    }

    public boolean isCalculPr() {
        return calculPr;
    }

    public void setCalculPr(boolean calculPr) {
        this.calculPr = calculPr;
    }

    public boolean isImpactValeurInventaire() {
        return impactValeurInventaire;
    }

    public void setImpactValeurInventaire(boolean impactValeurInventaire) {
        this.impactValeurInventaire = impactValeurInventaire;
    }

    public LotReception getLotSortie() {
        return lotSortie;
    }

    public void setLotSortie(LotReception lotSortie) {
        this.lotSortie = lotSortie;
    }

    public LotReception getLotEntree() {
        return lotEntree;
    }

    public void setLotEntree(LotReception lotEntree) {
        this.lotEntree = lotEntree;
    }

    public double getResultante() {
        return resultante;
    }

    public void setResultante(double resultante) {
        this.resultante = resultante;
    }

    public Conditionnement getUniteDestination() {
        return uniteDestination;
    }

    public void setUniteDestination(Conditionnement uniteDestination) {
        this.uniteDestination = uniteDestination;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public Qualite getQualite() {
        return qualite;
    }

    public void setQualite(Qualite qualite) {
        this.qualite = qualite;
    }

    public String getStatut() {
        return statut != null ? (statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE) : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getNumSerie() {
        return numSerie != null ? numSerie : "";
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public String getCommentaire() {
        return commentaire != null ? commentaire : "";
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public double getQteAttente() {
        return qteAttente;
    }

    public void setQteAttente(double qteAttente) {
        this.qteAttente = qteAttente;
    }

    public Date getDateReception() {
        return dateReception != null ? dateReception : new Date();
    }

    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    public ContenuDocStock getParent() {
        return parent;
    }

    public void setParent(ContenuDocStock parent) {
        this.parent = parent;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateContenu() {
        return dateContenu != null ? dateContenu : new Date();
    }

    public void setDateContenu(Date dateContenu) {
        this.dateContenu = dateContenu;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getQuantite() {
        return quantite <= 0 ? 1 : quantite;
    }

    public double getQuantite_() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
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

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public DocStock getDocStock() {
        return docStock;
    }

    public void setDocStock(DocStock docStock) {
        this.docStock = docStock;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
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

    public boolean isUpdate() {
        return id > 0;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public List<YvsComLotReception> getLots() {
        return lots;
    }

    public void setLots(List<YvsComLotReception> lots) {
        this.lots = lots;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
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
        final ContenuDocStock other = (ContenuDocStock) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ContenuDocStock{" + "id=" + id + '}';
    }

}

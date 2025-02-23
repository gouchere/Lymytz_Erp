/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.commission;

import java.io.Serializable;
import java.util.ArrayList; 
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.commission.YvsComCibleFacteurTaux;
import yvs.entity.commercial.commission.YvsComPeriodeValiditeCommision;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.produits.YvsBaseArticles;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class FacteurTaux implements Serializable {

    private long id;
    private boolean general;
    private boolean nouveauClient;
    private double taux;
    private char base = Constantes.BASE_COMMISSION_CA_TTC;
    private char champApplication = Constantes.APPICATION_COMMISSION_FACTURE_VALIDE;
    private boolean actif = true;
    private boolean permanent = true;
    private PlanCommission plan = new PlanCommission();
    private TypeGrille typeGrille = new TypeGrille();
    private boolean byGrille;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private List<YvsBaseArticles> articles, articlesSave;
    private List<YvsDictionnaire> zones;
    private List<YvsComClient> clients;
    private List<YvsBaseCategorieClient> categories;
    private List<YvsComCibleFacteurTaux> cibles;
    private List<YvsComPeriodeValiditeCommision> periodicites;

    public FacteurTaux() {
        cibles = new ArrayList<>();
        articles = new ArrayList<>();
        articlesSave = new ArrayList<>();
        zones = new ArrayList<>();
        clients = new ArrayList<>();
        categories = new ArrayList<>();
        periodicites = new ArrayList<>();
    }

    public FacteurTaux(long id) {
        this();
        this.id = id;
    }

    public List<YvsBaseArticles> getArticlesSave() {
        return articlesSave;
    }

    public void setArticlesSave(List<YvsBaseArticles> articlesSave) {
        this.articlesSave = articlesSave;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public boolean isGeneral() {
        return general;
    }

    public void setGeneral(boolean general) {
        this.general = general;
    }

    public boolean isNouveauClient() {
        return nouveauClient;
    }

    public void setNouveauClient(boolean nouveauClient) {
        this.nouveauClient = nouveauClient;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public char getBase() {
        return Character.valueOf(base) != null ? String.valueOf(base).trim().length() > 0 ? base : Constantes.BASE_COMMISSION_CA_TTC : Constantes.BASE_COMMISSION_CA_TTC;
    }

    public void setBase(char base) {
        this.base = base;
    }

    public char getChampApplication() {
        return Character.valueOf(champApplication) != null ? String.valueOf(champApplication).trim().length() > 0 ? champApplication : Constantes.APPICATION_COMMISSION_FACTURE_VALIDE : Constantes.APPICATION_COMMISSION_FACTURE_VALIDE;
    }

    public void setChampApplication(char champApplication) {
        this.champApplication = champApplication;
    }

    public PlanCommission getPlan() {
        return plan;
    }

    public void setPlan(PlanCommission plan) {
        this.plan = plan;
    }

    public boolean isByGrille() {
        return byGrille;
    }

    public void setByGrille(boolean byGrille) {
        this.byGrille = byGrille;
    }

    public TypeGrille getTypeGrille() {
        return typeGrille;
    }

    public void setTypeGrille(TypeGrille typeGrille) {
        this.typeGrille = typeGrille;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public List<YvsComCibleFacteurTaux> getCibles() {
        return cibles;
    }

    public void setCibles(List<YvsComCibleFacteurTaux> cibles) {
        this.cibles = cibles;
    }

    public List<YvsBaseArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticles> articles) {
        this.articles = articles;
    }

    public List<YvsDictionnaire> getZones() {
        return zones;
    }

    public void setZones(List<YvsDictionnaire> zones) {
        this.zones = zones;
    }

    public List<YvsComClient> getClients() {
        return clients;
    }

    public void setClients(List<YvsComClient> clients) {
        this.clients = clients;
    }

    public List<YvsBaseCategorieClient> getCategories() {
        return categories;
    }

    public void setCategories(List<YvsBaseCategorieClient> categories) {
        this.categories = categories;
    }

    public List<YvsComPeriodeValiditeCommision> getPeriodicites() {
        return periodicites;
    }

    public void setPeriodicites(List<YvsComPeriodeValiditeCommision> periodicites) {
        this.periodicites = periodicites;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final FacteurTaux other = (FacteurTaux) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

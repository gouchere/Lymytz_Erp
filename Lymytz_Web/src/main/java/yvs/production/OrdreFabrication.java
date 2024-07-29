/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.entity.production.pilotage.YvsProdComposantOF;
import yvs.entity.production.pilotage.YvsProdDeclarationProduction;
import yvs.entity.production.pilotage.YvsProdOperationsOF;
import yvs.parametrage.entrepot.Depots;
import yvs.production.technique.GammeArticle;
import yvs.production.technique.Nomenclature;
import yvs.production.base.EquipeProduction;
import yvs.production.base.SiteProduction;
import yvs.util.Constantes;

/**
 *
 * @author hp Elite 8300
 */
@SessionScoped
@ManagedBean
public class OrdreFabrication implements Serializable {

    private long id;
    private String reference;
    private String numIdentification;
    private double quantite;    //Quantité entrée
//    private double quantiteReel;//Quantité validé
    private double quantitePrevu;// Quantité attendu à la fin de la production
    private double quantiteDeclare;
    private double quantiteRebute;

    private boolean suiviOperations=true;
    private boolean generateOfSousProduit = true;
    private boolean suiviStockByOperation;
    private int priorite = 3;
    private String statusOrdre = Constantes.ETAT_ATTENTE; //Lancé, En cours, En attente, Suspendu, Terminé, Clôturé
    private String statutDeclaration;   // W=En cours, E=En Attente, T=Terminé
    private String typeGeneration;  //Automatique ou manuel
    private String typeOf;
    private String numCommande;
    private String generateur;  //OF suggéré, Commandes, Flux interne, PDP
    private Date dateDebutLancement = new Date();
    private Date heureDeLancement = new Date();
    private Date dateFinFabrication = new Date();
    private Date heureFinFabrication = new Date();
    private Date dateSave = new Date();

    private Articles articles = new Articles();
    private Nomenclature nomenclature = new Nomenclature();
    private GammeArticle gamme = new GammeArticle();
    private EquipeProduction equipe = new EquipeProduction();
    private SiteProduction site = new SiteProduction();

    private Depots depotMp = new Depots();
    private Depots depotPf = new Depots();

    private List<YvsProdDeclarationProduction> declarations;
    private List<YvsProdOperationsOF> listOperationsOf;
    private List<YvsProdComposantOF> listComposantOf;

    private double coutDeProduction;
    private double rebut;
    private boolean retardDeLancement;  //utile sur la vue pour détecter les ordres en retard de lancement (un OF est en retard si son statut est en attente et la date prévu de lancement est passé)
    private boolean suspendu = false;

    public OrdreFabrication() {
        listComposantOf = new ArrayList<>();
        listOperationsOf = new ArrayList<>();
        declarations = new ArrayList<>();
    }

    public OrdreFabrication(long id) {
        this();
        this.id = id;
    }

    public OrdreFabrication(long id, double quantitePrevu) {
        this(id);
        this.quantitePrevu = quantitePrevu;
    }

    public boolean isGenerateOfSousProduit() {
        return generateOfSousProduit;
    }

    public void setGenerateOfSousProduit(boolean generateOfSousProduit) {
        this.generateOfSousProduit = generateOfSousProduit;
    }

    public boolean isSuspendu() {
        return suspendu;
    }

    public void setSuspendu(boolean suspendu) {
        this.suspendu = suspendu;
    }

    public List<YvsProdOperationsOF> getListOperationsOf() {
        if (listOperationsOf != null) {
            Collections.sort(listOperationsOf, new YvsProdOperationsOF());
        }
        return listOperationsOf;
    }

    public void setListOperationsOf(List<YvsProdOperationsOF> listOperationsOf) {
        this.listOperationsOf = listOperationsOf;
    }

    public List<YvsProdComposantOF> getListComposantOf() {
        return listComposantOf;
    }

    public void setListComposantOf(List<YvsProdComposantOF> listComposantOf) {
        this.listComposantOf = listComposantOf;
    }

    public Articles getArticles() {
        return articles;
    }

    public void setArticles(Articles articles) {
        this.articles = articles;
    }

    public GammeArticle getGamme() {
        return gamme;
    }

    public void setGamme(GammeArticle gamme) {
        this.gamme = gamme;
    }

    public Nomenclature getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(Nomenclature nomenclature) {
        this.nomenclature = nomenclature;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNumIdentification() {
        return numIdentification;
    }

    public void setNumIdentification(String numIdentification) {
        this.numIdentification = numIdentification;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getQuantitePrevu() {
        return quantitePrevu;
    }

    public void setQuantitePrevu(double quantitePrevu) {
        this.quantitePrevu = quantitePrevu;
    }

    public int getPriorite() {
        return priorite;
    }

    public void setPriorite(int priorite) {
        this.priorite = priorite;
    }

    public boolean isSuiviStockByOperation() {
        return suiviStockByOperation;
    }

    public void setSuiviStockByOperation(boolean suiviStockByOperation) {
        this.suiviStockByOperation = suiviStockByOperation;
    }

    public String getStatusOrdre() {
        return statusOrdre != null ? statusOrdre.trim().length() > 0 ? statusOrdre : Constantes.ETAT_ATTENTE : Constantes.ETAT_ATTENTE;
    }

    public void setStatusOrdre(String statusOrdre) {
        this.statusOrdre = statusOrdre;
    }

    public String getStatutDeclaration() {
        return statutDeclaration != null ? statutDeclaration : Constantes.ETAT_ATTENTE;
    }

    public void setStatutDeclaration(String statutDeclaration) {
        this.statutDeclaration = statutDeclaration;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getGenerateur() {
        return generateur;
    }

    public void setGenerateur(String generateur) {
        this.generateur = generateur;
    }

    public String getTypeGeneration() {
        return typeGeneration;
    }

    public void setTypeGeneration(String typeGeneration) {
        this.typeGeneration = typeGeneration;
    }

    public EquipeProduction getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeProduction equipe) {
        this.equipe = equipe;
    }

    public SiteProduction getSite() {
        return site;
    }

    public void setSite(SiteProduction site) {
        this.site = site;
    }

    public double getCoutDeProduction() {
        return coutDeProduction;
    }

    public void setCoutDeProduction(double coutDeProduction) {
        this.coutDeProduction = coutDeProduction;
    }

    public double getRebut() {
        return rebut;
    }

    public void setRebut(double rebut) {
        this.rebut = rebut;
    }

    public boolean isRetardDeLancement() {
        return retardDeLancement;
    }

    public void setRetardDeLancement(boolean retardDeLancement) {
        this.retardDeLancement = retardDeLancement;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateDebutLancement() {
        return dateDebutLancement;
    }

    public void setDateDebutLancement(Date dateDebutLancement) {
        this.dateDebutLancement = dateDebutLancement;
    }

    public Date getHeureDeLancement() {
        return heureDeLancement;
    }

    public void setHeureDeLancement(Date heureDeLancement) {
        this.heureDeLancement = heureDeLancement;
    }

    public Date getDateFinFabrication() {
        return dateFinFabrication;
    }

    public void setDateFinFabrication(Date dateFinFabrication) {
        this.dateFinFabrication = dateFinFabrication;
    }

    public Date getHeureFinFabrication() {
        return heureFinFabrication;
    }

    public void setHeureFinFabrication(Date heureFinFabrication) {
        this.heureFinFabrication = heureFinFabrication;
    }

    public Depots getDepotMp() {
        return depotMp;
    }

    public void setDepotMp(Depots depotMp) {
        this.depotMp = depotMp;
    }

    public Depots getDepotPf() {
        return depotPf;
    }

    public void setDepotPf(Depots depotPf) {
        this.depotPf = depotPf;
    }

    public List<YvsProdDeclarationProduction> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<YvsProdDeclarationProduction> declarations) {
        this.declarations = declarations;
    }

    public String getTypeOf() {
        return typeOf;
    }

    public void setTypeOf(String typeOf) {
        this.typeOf = typeOf;
    }

    public String getNumCommande() {
        return numCommande;
    }

    public void setNumCommande(String numCommande) {
        this.numCommande = numCommande;
    }

    public boolean isSuiviOperations() {
        return suiviOperations;
    }

    public void setSuiviOperations(boolean suiviOperations) {
        this.suiviOperations = suiviOperations;
    }

    public double getQuantiteDeclare() {
        if (declarations != null) {
            quantiteDeclare = 0;
            for (YvsProdDeclarationProduction de : declarations) {
                if (de.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                    quantiteDeclare += de.getQuantite();
                }
            }
        }
        return quantiteDeclare;
    }

    public void setQuantiteDeclare(double quantiteDeclare) {
        this.quantiteDeclare = quantiteDeclare;
    }

    public double getQuantiteRebute() {
        return quantiteRebute;
    }

    public void setQuantiteRebute(double quantiteRebute) {
        this.quantiteRebute = quantiteRebute;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final OrdreFabrication other = (OrdreFabrication) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.production.OrdreFabrication[ id=" + id + " ]";
    }

    public boolean canChangeStatutOperation() {
        return !getStatusOrdre().equals(Constantes.ETAT_ATTENTE) && !getStatusOrdre().equals(Constantes.ETAT_SUSPENDU);
    }

    public boolean isEndAll() {
        for (YvsProdOperationsOF op : listOperationsOf) {
            if (!op.getStatutOp().equals(Constantes.ETAT_TERMINE)) {
                return false;
            }
        }
        return true;
    }

    public boolean canDelete() {
        return getStatusOrdre().equals(Constantes.ETAT_TERMINE);
    }

    public boolean canEditable(boolean autorise) {
        // l'OF doit être en cours ou bien (Non cloturé avec un droit)
        return getStatusOrdre().equals(Constantes.ETAT_ATTENTE) || (getStatusOrdre().equals(Constantes.ETAT_ENCOURS) && autorise);
    }

}

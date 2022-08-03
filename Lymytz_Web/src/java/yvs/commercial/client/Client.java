/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.Comptes;
import yvs.base.compta.ModelReglement;
import yvs.base.tiers.Tiers;
import yvs.commercial.depot.PointLivraison;
import yvs.commercial.rrr.PlanRistourne;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.commercial.client.YvsComContratsClient;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaCreditClient;
import yvs.users.Users;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Client implements Serializable {

    private long id;
    private Tiers tiers = new Tiers();
    private boolean defaut, suiviComptable = true, confirmer, venteOnline, exclusForHome;
    private String codeClient, nom, prenom, nom_prenom;
    private String categorie;
    private CategorieComptable categorieComptable = new CategorieComptable();
    private Comptes compteCollectif = new Comptes();
    private ModelReglement model = new ModelReglement();
    private PlanRistourne ristourne = new PlanRistourne();
    private PointLivraison ligne = new PointLivraison();
    private Users representant = new Users();
    private Users createBy = new Users();
    private Date dateCreation = new Date();
    private Date dateUpdate = new Date();
    private List<YvsComContenuDocVente> transactions;
    private List<YvsComDocVentes> factures;
    private List<YvsComptaCaissePieceVente> reglements;
    private List<YvsComptaAcompteClient> acomptes;
    private List<YvsComptaCreditClient> creances;
    private List<YvsComContratsClient> contrats;
    private YvsComDocVentes lastFacture = new YvsComDocVentes();
    private List<YvsBaseExercice> soldes;
    private long nbrFactureImpayee;
    private double credit, debit, solde, seuilSolde, acompte, creance;
    private boolean selectActif, new_, actif = true, error, listClient, afficheSolde;

    public Client() {
        factures = new ArrayList<>();
        soldes = new ArrayList<>();
        transactions = new ArrayList<>();
        reglements = new ArrayList<>();
        acomptes = new ArrayList<>();
        creances = new ArrayList<>();
        contrats = new ArrayList<>();
    }

    public Client(long id) {
        this();
        this.id = id;
    }

    public Client(long id, Tiers tiers) {
        this(id);
        this.tiers = tiers;
        this.codeClient = tiers.getCodeTiers();
        this.nom = tiers.getNom();
        this.prenom = tiers.getPrenom();
        this.selectActif = tiers.isSelectActif();
        this.new_ = tiers.isNew_();
        this.error = tiers.isError();
    }

    public Client(Long id, String nom, String prenom) {
        this(id);
        this.nom = nom;
        this.prenom = prenom;
    }

    public Client(Long id, String codeClient, String nom, String prenom) {
        this(id, nom, prenom);
        this.codeClient = codeClient;
    }

    public long getNbrFactureImpayee() {
        return nbrFactureImpayee;
    }

    public void setNbrFactureImpayee(long nbrFactureImpayee) {
        this.nbrFactureImpayee = nbrFactureImpayee;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getNom_prenom() {
        nom_prenom = "";
        if (!(getNom() == null || getNom().trim().equals(""))) {
            nom_prenom = getNom();
        }
        if (!(getPrenom() == null || getPrenom().trim().equals(""))) {
            if (nom_prenom == null || nom_prenom.trim().equals("")) {
                nom_prenom = getPrenom();
            } else {
                nom_prenom += " " + getPrenom();
            }
        }
        if (getTiers() != null) {
            if (getTiers().isSociete()) {
                if (!(getTiers().getResponsable() == null || getTiers().getResponsable().trim().equals(""))) {
                    if (nom_prenom == null || nom_prenom.trim().equals("")) {
                        nom_prenom = getTiers().getResponsable();
                    } else {
                        nom_prenom += " / " + getTiers().getResponsable();
                    }
                }
            }
        }
        return nom_prenom;
    }

    public void setNom_prenom(String nom_prenom) {
        this.nom_prenom = nom_prenom;
    }

    public List<YvsComptaCaissePieceVente> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceVente> reglements) {
        this.reglements = reglements;
    }

    public List<YvsComptaAcompteClient> getAcomptes() {
        return acomptes;
    }

    public void setAcomptes(List<YvsComptaAcompteClient> acomptes) {
        this.acomptes = acomptes;
    }

    public List<YvsComptaCreditClient> getCreances() {
        return creances;
    }

    public void setCreances(List<YvsComptaCreditClient> creances) {
        this.creances = creances;
    }

    public List<YvsComContenuDocVente> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<YvsComContenuDocVente> transactions) {
        this.transactions = transactions;
    }

    public YvsComDocVentes getLastFacture() {
        return lastFacture;
    }

    public void setLastFacture(YvsComDocVentes lastFacture) {
        this.lastFacture = lastFacture;
    }

    public Users getRepresentant() {
        return representant;
    }

    public void setRepresentant(Users representant) {
        this.representant = representant;
    }

    public double getAcompte() {
        return acompte;
    }

    public void setAcompte(double acompte) {
        this.acompte = acompte;
    }

    public double getCreance() {
        return creance;
    }

    public void setCreance(double creance) {
        this.creance = creance;
    }

    public String getCategorie() {
        return categorie;
    }

    public boolean isExclusForHome() {
        return exclusForHome;
    }

    public void setExclusForHome(boolean exclusForHome) {
        this.exclusForHome = exclusForHome;
    }

    public boolean isConfirmer() {
        return confirmer;
    }

    public void setConfirmer(boolean confirmer) {
        this.confirmer = confirmer;
    }

    public boolean isVenteOnline() {
        return venteOnline;
    }

    public void setVenteOnline(boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public double getSeuilSolde() {
        return seuilSolde;
    }

    public void setSeuilSolde(double seuilSolde) {
        this.seuilSolde = seuilSolde;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public boolean isAfficheSolde() {
        return afficheSolde;
    }

    public void setAfficheSolde(boolean afficheSolde) {
        this.afficheSolde = afficheSolde;
    }

    public List<YvsBaseExercice> getSoldes() {
        return soldes;
    }

    public void setSoldes(List<YvsBaseExercice> soldes) {
        this.soldes = soldes;
    }

    public List<YvsComDocVentes> getFactures() {
        return factures;
    }

    public void setFactures(List<YvsComDocVentes> factures) {
        this.factures = factures;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public boolean isSuiviComptable() {
        return suiviComptable;
    }

    public void setSuiviComptable(boolean suiviComptable) {
        this.suiviComptable = suiviComptable;
    }

    public boolean isListClient() {
        return listClient;
    }

    public void setListClient(boolean listClient) {
        this.listClient = listClient;
    }

    public PlanRistourne getRistourne() {
        return ristourne;
    }

    public void setRistourne(PlanRistourne ristourne) {
        this.ristourne = ristourne;
    }

    public String getNom() {
        return nom != null ? nom : "";
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom != null ? prenom : "";
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public ModelReglement getModel() {
        return model;
    }

    public void setModel(ModelReglement model) {
        this.model = model;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Comptes getCompteCollectif() {
        return compteCollectif;
    }

    public void setCompteCollectif(Comptes compteCollectif) {
        this.compteCollectif = compteCollectif;
    }

    public String getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(String codeClient) {
        this.codeClient = codeClient;
    }

    public boolean isDefaut() {
        return defaut;
    }

    public void setDefaut(boolean defaut) {
        this.defaut = defaut;
    }

    public CategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(CategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
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

    public Users getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Users createBy) {
        this.createBy = createBy;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public List<YvsComContratsClient> getContrats() {
        return contrats;
    }

    public void setContrats(List<YvsComContratsClient> contrats) {
        this.contrats = contrats;
    }

    public PointLivraison getLigne() {
        return ligne;
    }

    public void setLigne(PointLivraison ligne) {
        this.ligne = ligne;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Client other = (Client) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

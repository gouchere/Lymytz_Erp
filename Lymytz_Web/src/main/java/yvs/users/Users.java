/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users;

import java.io.Serializable;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import java.util.*;
import javax.faces.bean.SessionScoped;
import yvs.base.tiers.Tiers;
import yvs.commercial.CategoriePerso;
import yvs.commercial.commission.PlanCommission;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.societe.Societe;
import yvs.users.messagerie.GroupeMessage;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class Users implements Serializable {

    private long id;
    private String codeUsers;
    private String passwordUser;
    private String photo;
    private String civilite = "M";
    private String employe;
    private boolean supp;
    private boolean actif = true;
    private boolean initPassword;
    private boolean connectOnlinePlanning;
    private boolean accesMultiSociete, accesMultiAgence;
    private boolean selectActif, viewActif;
    private String aleaMdp;
    private String nomUsers;
    private String abbreviation;
    private NiveauAcces niveauAcces = new NiveauAcces();
    private Agence agence = new Agence();
    private Societe societe = new Societe();
    private boolean connecte;
    private boolean superAdmin;
    private boolean venteOnline;
    private Tiers tiers = new Tiers();
    private AdresseProfessionnel adresse = new AdresseProfessionnel();
    private List<AdresseProfessionnel> listAdresse;
    private List<GroupeMessage> dossiers;
    private List<CarnetAdresse> CarnetAdresse;
    private CategoriePerso categorie = new CategoriePerso();
    private PlanCommission planCommission = new PlanCommission();
    private double credit, debit, solde;
    private boolean update, new_, error, viewList, afficheSolde, temporaire;
    private Date dateExpiration = new Date();
    private List<YvsBaseExercice> soldes;
    private List<YvsComDocVentes> factures;
    private YvsComComerciale commerciaux = new YvsComComerciale();

    public Users() {
        listAdresse = new ArrayList<>();
        dossiers = new ArrayList<>();
        CarnetAdresse = new ArrayList<>();
        soldes = new ArrayList<>();
        factures = new ArrayList<>();
    }

    public Users(long id) {
        this();
        this.id = id;
    }

    public Users(long id, String nomUsers) {
        this(id);
        this.nomUsers = nomUsers;
    }

    public Users(long id, String codeUsers, String nomUsers) {
        this(id, nomUsers);
        this.codeUsers = codeUsers;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public String getEmploye() {
        return employe;
    }

    public void setEmploye(String employe) {
        this.employe = employe;
    }

    public boolean isVenteOnline() {
        return venteOnline;
    }

    public void setVenteOnline(boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public List<YvsComDocVentes> getFactures() {
        return factures;
    }

    public void setFactures(List<YvsComDocVentes> factures) {
        this.factures = factures;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public boolean isAfficheSolde() {
        return afficheSolde;
    }

    public void setAfficheSolde(boolean afficheSolde) {
        this.afficheSolde = afficheSolde;
    }

    public boolean isConnectOnlinePlanning() {
        return connectOnlinePlanning;
    }

    public void setConnectOnlinePlanning(boolean connectOnlinePlanning) {
        this.connectOnlinePlanning = connectOnlinePlanning;
    }

    public List<YvsBaseExercice> getSoldes() {
        return soldes;
    }

    public void setSoldes(List<YvsBaseExercice> soldes) {
        this.soldes = soldes;
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

    public boolean isViewList() {
        return viewList;
    }

    public void setViewList(boolean viewList) {
        this.viewList = viewList;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public CategoriePerso getCategorie() {
        return categorie;
    }

    public void setCategorie(CategoriePerso categorie) {
        this.categorie = categorie;
    }

    public PlanCommission getPlanCommission() {
        return planCommission;
    }

    public void setPlanCommission(PlanCommission planCommission) {
        this.planCommission = planCommission;
    }

    public boolean isAccesMultiSociete() {
        return accesMultiSociete;
    }

    public void setAccesMultiSociete(boolean accesMultiSociete) {
        this.accesMultiSociete = accesMultiSociete;
    }

    public List<CarnetAdresse> getCarnetAdresse() {
        return CarnetAdresse;
    }

    public void setCarnetAdresse(List<CarnetAdresse> CarnetAdresse) {
        this.CarnetAdresse = CarnetAdresse;
    }

    public List<GroupeMessage> getDossiers() {
        return dossiers;
    }

    public void setDossiers(List<GroupeMessage> dossiers) {
        this.dossiers = dossiers;
    }

    public AdresseProfessionnel getAdresse() {
        return adresse;
    }

    public void setAdresse(AdresseProfessionnel adresse) {
        this.adresse = adresse;
    }

    public List<AdresseProfessionnel> getListAdresse() {
        return listAdresse;
    }

    public void setListAdresse(List<AdresseProfessionnel> listAdresse) {
        this.listAdresse = listAdresse;
    }

    public boolean isViewActif() {
        return viewActif;
    }

    public void setViewActif(boolean viewActif) {
        this.viewActif = viewActif;
    }

    public boolean isConnecte() {
        return connecte;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public void setConnecte(boolean connecte) {
        this.connecte = connecte;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public long getId() {
        return id;
    }

    public void setNomUsers(String nomUsers) {
        this.nomUsers = nomUsers;
    }

    public String getNomUsers() {
        return nomUsers;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodeUsers() {
        return codeUsers;
    }

    public void setCodeUsers(String codeUsers) {
        this.codeUsers = codeUsers;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
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

    public boolean isInitPassword() {
        return initPassword;
    }

    public void setInitPassword(boolean initPassword) {
        this.initPassword = initPassword;
    }

    public boolean isAccesMultiAgence() {
        return accesMultiAgence;
    }

    public void setAccesMultiAgence(boolean accesMultiAgence) {
        this.accesMultiAgence = accesMultiAgence;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public String getAleaMdp() {
        return aleaMdp;
    }

    public void setAleaMdp(String aleaMdp) {
        this.aleaMdp = aleaMdp;
    }

    public NiveauAcces getNiveauAcces() {
        return niveauAcces;
    }

    public void setNiveauAcces(NiveauAcces niveauAcces) {
        this.niveauAcces = niveauAcces;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public String getCivilite() {
        return civilite != null ? civilite.trim().length() > 0 ? civilite : "M." : "M.";
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public boolean isTemporaire() {
        return temporaire;
    }

    public void setTemporaire(boolean temporaire) {
        this.temporaire = temporaire;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }

    public YvsComComerciale getCommerciaux() {
        return commerciaux;
    }

    public void setCommerciaux(YvsComComerciale commerciaux) {
        this.commerciaux = commerciaux;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final Users other = (Users) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.users.Users[ id=" + id + " ]";
    }

}

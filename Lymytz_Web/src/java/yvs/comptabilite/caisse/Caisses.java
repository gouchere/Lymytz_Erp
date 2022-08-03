/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.caisse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.Comptes;
import yvs.base.compta.Journaux;
import yvs.base.compta.ModeDeReglement;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBaseComptesCaisse;
import yvs.entity.compta.YvsBaseLiaisonCaisse;
import yvs.grh.bean.Employe;
import yvs.users.Users;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class Caisses implements Serializable {

    private long id;
    private long idLiaison;
    private String intitule;
    private String code;
    private String type = "CAISSE";   //CAISSE ou BANQUE
    private Caisses parent;
    private long caisseLie;
    private Comptes compte = new Comptes();
    private Employe responsable = new Employe();
    private Users caissier = new Users();
    private boolean actif = true;
    private boolean defaultCaisse = false;
    private boolean giveBilletage = false;
    private boolean principal;
    private boolean venteOnline;
    private Journaux journal = new Journaux();
    private String codeAcces;
    private String adresse;
    private List<YvsBaseLiaisonCaisse> subCaisses;
    private double solde, recette, depense;
    private double soldeMission, soldeAchat, soldeVente, soldeDivers, soldeVirement, soldeNote, soldeOther;
    private boolean canBeNegative;
    private ModeDeReglement modeRegDefaut = new ModeDeReglement();
    private List<YvsBaseComptesCaisse> othersComptes;

    private Date dateSave = new Date();

    public Caisses() {
        subCaisses = new ArrayList<>();
        othersComptes = new ArrayList<>();
    }

    public Caisses(long id, String intitule) {
        this();
        this.id = id;
        this.intitule = intitule;
    }

    public Caisses(long id, String code, String intitule) {
        this(id, intitule);
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCaisseLie() {
        return caisseLie;
    }

    public void setCaisseLie(long caisseLie) {
        this.caisseLie = caisseLie;
    }

    public String getCode() {
        return code != null ? code : "";
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getSoldeNote() {
        return soldeNote;
    }

    public void setSoldeNote(double soldeNote) {
        this.soldeNote = soldeNote;
    }

    public double getSoldeOther() {
        return soldeOther;
    }

    public void setSoldeOther(double soldeOther) {
        this.soldeOther = soldeOther;
    }

    public double getSoldeMission() {
        return soldeMission;
    }

    public void setSoldeMission(double soldeMission) {
        this.soldeMission = soldeMission;
    }

    public double getSoldeAchat() {
        return soldeAchat;
    }

    public void setSoldeAchat(double soldeAchat) {
        this.soldeAchat = soldeAchat;
    }

    public double getSoldeVente() {
        return soldeVente;
    }

    public void setSoldeVente(double soldeVente) {
        this.soldeVente = soldeVente;
    }

    public double getSoldeDivers() {
        return soldeDivers;
    }

    public void setSoldeDivers(double soldeDivers) {
        this.soldeDivers = soldeDivers;
    }

    public double getSoldeVirement() {
        return soldeVirement;
    }

    public void setSoldeVirement(double soldeVirement) {
        this.soldeVirement = soldeVirement;
    }

    public List<YvsBaseLiaisonCaisse> getSubCaisses() {
        return subCaisses;
    }

    public void setSubCaisses(List<YvsBaseLiaisonCaisse> subCaisses) {
        this.subCaisses = subCaisses;
    }

    public double getRecette() {
        return recette;
    }

    public void setRecette(double recette) {
        this.recette = recette;
    }

    public double getDepense() {
        return depense;
    }

    public void setDepense(double depense) {
        this.depense = depense;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public boolean isVenteOnline() {
        return venteOnline;
    }

    public void setVenteOnline(boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public String getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(String codeAcces) {
        this.codeAcces = codeAcces;
    }

    public Caisses getParent() {
        return parent;
    }

    public void setParent(Caisses parent) {
        this.parent = parent;
    }

    public Comptes getCompte() {
        return compte;
    }

    public void setCompte(Comptes compte) {
        this.compte = compte;
    }

    public Employe getResponsable() {
        return responsable;
    }

    public void setResponsable(Employe responsable) {
        this.responsable = responsable;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Journaux getJournal() {
        return journal;
    }

    public void setJournal(Journaux journal) {
        this.journal = journal;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public boolean isCanBeNegative() {
        return canBeNegative;
    }

    public void setCanBeNegative(boolean canBeNegative) {
        this.canBeNegative = canBeNegative;
    }

    public ModeDeReglement getModeRegDefaut() {
        return modeRegDefaut;
    }

    public void setModeRegDefaut(ModeDeReglement modeRegDefaut) {
        this.modeRegDefaut = modeRegDefaut;
    }

    public String getType() {
        return type != null ? type.trim().length() > 0 ? type : "CAISSE" : "CAISSE";
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<YvsBaseComptesCaisse> getOthersComptes() {
        return othersComptes;
    }

    public void setOthersComptes(List<YvsBaseComptesCaisse> othersComptes) {
        this.othersComptes = othersComptes;
    }

    public boolean isDefaultCaisse() {
        return defaultCaisse;
    }

    public void setDefaultCaisse(boolean defaultCaisse) {
        this.defaultCaisse = defaultCaisse;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Users getCaissier() {
        return caissier;
    }

    public void setCaissier(Users caissier) {
        this.caissier = caissier;
    }

    public boolean isGiveBilletage() {
        return giveBilletage;
    }

    public void setGiveBilletage(boolean giveBilletage) {
        this.giveBilletage = giveBilletage;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public long getIdLiaison() {
        return idLiaison;
    }

    public void setIdLiaison(long idLiaison) {
        this.idLiaison = idLiaison;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Caisses other = (Caisses) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

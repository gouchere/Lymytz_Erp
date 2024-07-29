/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.echellonage;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultScheduleEvent;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.mutuelle.echellonage.YvsMutMensualite;
import yvs.entity.mutuelle.echellonage.YvsMutReglementMensualite;
import yvs.mutuelle.credit.Credit;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Echellonage extends DefaultScheduleEvent implements Serializable {

    private long idEch;
    private Date dateEchellonage = new Date();
    private Date dateSave = new Date();
    private double montant, montantVerse;
    private double montantReste, montantInteret;
    private String etat;
    private double taux;
    private double dureeEcheance;
    private String description;
    private int ecartMensualite = 1;
    private List<YvsMutMensualite> mensualites;
    private List<YvsMutReglementMensualite> reglements;
    private Credit credit = new Credit();
    private boolean selectActif, new_, echellone = true, update = true, actif;
    private boolean creditRetainsInteret;

    public Echellonage() {
        mensualites = new ArrayList<>();
        reglements = new ArrayList<>();
    }

    public Echellonage(long id) {
        this.idEch = id;
        mensualites = new ArrayList<>();
        reglements = new ArrayList<>();
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public double getMontantInteret() {
        return montantInteret;
    }

    public void setMontantInteret(double montantInteret) {
        this.montantInteret = montantInteret;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public List<YvsMutReglementMensualite> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsMutReglementMensualite> reglements) {
        this.reglements = reglements;
    }

    public double getMontantReste() {
        montantReste = getMontant() - getMontantVerse();
        return montantReste;
//        return montantReste - (creditRetainsInteret ? montantInteret : 0);
    }

    public void setMontantReste(double montantReste) {
        this.montantReste = montantReste;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isEchellone() {
        return echellone;
    }

    public void setEchellone(boolean echellone) {
        this.echellone = echellone;
    }

    public double getMontantVerse() {
        montantVerse = 0;
        for (YvsMutMensualite m : mensualites) {
            montantVerse += m.getMontantVerse();
        }
        return montantVerse + (creditRetainsInteret ? getMontantInteret() : 0);
    }

    public void setMontantVerse(double montantVerse) {
        this.montantVerse = montantVerse;
    }

    public int getEcartMensualite() {
        return ecartMensualite;
    }

    public void setEcartMensualite(int ecartMensualite) {
        this.ecartMensualite = ecartMensualite;
    }

    public long getIdEch() {
        return idEch;
    }

    public void setIdEch(long idEch) {
        this.idEch = idEch;
    }

    public Date getDateEchellonage() {
        return dateEchellonage != null ? dateEchellonage : new Date();
    }

    public void setDateEchellonage(Date dateEchellonage) {
        this.dateEchellonage = dateEchellonage;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getEtat() {
        return etat != null ? etat.trim().length() > 0 ? etat : Constantes.ETAT_ATTENTE : Constantes.ETAT_ATTENTE;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public double getDureeEcheance() {
        return dureeEcheance;
    }

    public void setDureeEcheance(double dureeEcheance) {
        this.dureeEcheance = dureeEcheance;
    }

    public List<YvsMutMensualite> getMensualites() {
        return mensualites;
    }

    public void setMensualites(List<YvsMutMensualite> mensualites) {
        this.mensualites = mensualites;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isCreditRetainsInteret() {
        return creditRetainsInteret;
    }

    public void setCreditRetainsInteret(boolean creditRetainsInteret) {
        this.creditRetainsInteret = creditRetainsInteret;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.idEch ^ (this.idEch >>> 32));
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
        final Echellonage other = (Echellonage) obj;
        if (this.idEch != other.idEch) {
            return false;
        }
        return true;
    }

}

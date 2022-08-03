/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.presence;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.grh.presence.YvsPointeuse;
import yvs.grh.bean.Employe;
import yvs.users.Users;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class PointageEmploye implements Serializable, Comparator<PointageEmploye> {

    private long id;
    private Date heure = new Date();
    private Date dateDebut = new Date(), dateFin = new Date();
    private Date entree = new Date();
    private Date sortie = new Date(); //utile pour la présentation des pointage
    private String motif, descriptionMotif;
    private char statut; //E pour Entrée S pour Sortie
    private long duree, dureeTService, dureeTAbsence;
    private boolean valider;
    private String dureeService;
    private String generateur;  //permet de spécifier la partie qui a généré la ligne pointé (USER ou SYSTEM)                  
    private String dureeAbsence;
    private boolean horaireNormale; //permet de spécifier les pointages qui ont eu lieu en dehors des heures de travail requise
    private long idFiche;
//    private Employe superieur = new Employe();
    private boolean lineTransient;
    private Date heurePonitageIn, heurePointageOut;
    private Users operateurIn = new Users(), operateurOut = new Users();
    private boolean pointageAuto;
    private boolean heureCompensation;
    private boolean heureSupp;
    private PresenceEmploye presence = new PresenceEmploye();
    private Date dateSave = new Date();
    private YvsPointeuse pointeuse;

    public PointageEmploye() {
    }

    public PointageEmploye(long id) {
        this.id = id;
    }

    public PresenceEmploye getPresence() {
        return presence;
    }

    public void setPresence(PresenceEmploye presence) {
        this.presence = presence;
    }

    public boolean isValider() {
        return valider;
    }

    public void setValider(boolean valider) {
        this.valider = valider;
    }

    public String getDureeService() {
        return dureeService;
    }

    public void setDureeService(String dureeService) {
        this.dureeService = dureeService;
    }

    public Date getHeure() {
        return heure;
    }

    public void setHeure(Date heure) {
        this.heure = heure;
    }

    public String getMotif() {
        return motif;
    }

    public String getDescriptionMotif() {
        return descriptionMotif;
    }

    public void setDescriptionMotif(String descriptionMotif) {
        this.descriptionMotif = descriptionMotif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public long getDuree() {
        return duree;
    }

    public void setDuree(long duree) {
        this.duree = duree;
    }

    public char getStatut() {
        return statut;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    public Date getEntree() {
        return entree;
    }

    public void setEntree(Date entree) {
        this.entree = entree;
    }

    public Date getSortie() {
        return sortie;
    }

    public void setSortie(Date sortie) {
        this.sortie = sortie;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGenerateur() {
        return generateur;
    }

    public void setGenerateur(String generateur) {
        this.generateur = generateur;
    }

    public String getDureeAbsence() {
        return dureeAbsence;
    }

    public void setDureeAbsence(String dureeAbsence) {
        this.dureeAbsence = dureeAbsence;
    }

    public boolean isHoraireNormale() {
        return horaireNormale;
    }

    public void setHoraireNormale(boolean horaireNormale) {
        this.horaireNormale = horaireNormale;
    }

    public long getDureeTAbsence() {
        return dureeTAbsence;
    }

    public void setDureeTAbsence(long dureeTAbsence) {
        this.dureeTAbsence = dureeTAbsence;
    }

    public long getDureeTService() {
        return dureeTService;
    }

    public void setDureeTService(long dureeTService) {
        this.dureeTService = dureeTService;
    }

    public long getIdFiche() {
        return idFiche;
    }

    public void setIdFiche(long idFiche) {
        this.idFiche = idFiche;
    }

//    public Employe getSuperieur() {
//        return superieur;
//    }
//
//    public void setSuperieur(Employe superieur) {
//        this.superieur = superieur;
//    }
    public boolean isLineTransient() {
        return lineTransient;
    }

    public void setLineTransient(boolean lineTransient) {
        this.lineTransient = lineTransient;
    }

    public Date getHeurePonitageIn() {
        return heurePonitageIn;
    }

    public void setHeurePonitageIn(Date heurePonitageIn) {
        this.heurePonitageIn = heurePonitageIn;
    }

    public Date getHeurePointageOut() {
        return heurePointageOut;
    }

    public void setHeurePointageOut(Date heurePointageOut) {
        this.heurePointageOut = heurePointageOut;
    }

    public Users getOperateurIn() {
        return operateurIn;
    }

    public void setOperateurIn(Users operateurIn) {
        this.operateurIn = operateurIn;
    }

    public Users getOperateurOut() {
        return operateurOut;
    }

    public void setOperateurOut(Users operateurOut) {
        this.operateurOut = operateurOut;
    }

    public boolean isPointageAuto() {
        return pointageAuto;
    }

    public void setPointageAuto(boolean pointageAuto) {
        this.pointageAuto = pointageAuto;
    }

    public boolean isHeureCompensation() {
        return heureCompensation;
    }

    public void setHeureCompensation(boolean heureCompensation) {
        this.heureCompensation = heureCompensation;
    }

    public boolean isHeureSupp() {
        return heureSupp;
    }

    public void setHeureSupp(boolean heureSupp) {
        this.heureSupp = heureSupp;
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

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsPointeuse getPointeuse() {
        return pointeuse;
    }

    public void setPointeuse(YvsPointeuse pointeuse) {
        this.pointeuse = pointeuse;
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
        final PointageEmploye other = (PointageEmploye) obj;
        return this.id == other.id;
    }

    @Override
    public int compare(PointageEmploye o1, PointageEmploye o2) {
        if (o1 != null && o2 != null) {
            if (o1.getHeure() != null && o2.getHeure() != null) {
                if (!o1.getHeure().equals(o2.getHeure())) {
                    return (o1.getHeure().after(o2.getHeure())) ? 1 : -1;
                } else {
                    return 0;
                }

            } else {
                if (o1.getHeure() != null) {
                    return 1;
                } else {
                    return -1;
                }
            }
        } else {
            if (o1 != null) {
                return 1;
            } else {
                return -1;
            }
        }
    }

}

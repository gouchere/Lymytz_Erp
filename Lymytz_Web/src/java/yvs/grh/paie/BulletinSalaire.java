/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import yvs.entity.grh.salaire.YvsGrhDetailBulletin;
import yvs.entity.grh.salaire.YvsGrhRubriqueBulletin;
import yvs.grh.bean.ContratEmploye;

/**
 *
 * @author LYMYTZ-PC
 */
public class BulletinSalaire {

    private long id;
    private ContratEmploye contrat = new ContratEmploye();
    private Date dateDebut;
    private Date dateFin;
    private Date dateCalcul;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private char statut = 'B';
    private String refBulletin, numMonth;
    private List<YvsGrhDetailBulletin> listDetails;
    private List<YvsGrhRubriqueBulletin> listElementApercu;
    private short numero;
    private boolean comptabilise;
    private double netApayer;
    private String anciennete;
    private OrdreCalculSalaire entete = new OrdreCalculSalaire();

    public BulletinSalaire() {
        listDetails = new ArrayList<>();
        listElementApercu = new ArrayList<>();
    }

    public boolean isComptabilise() {
        return comptabilise;
    }

    public void setComptabilise(boolean comptabilise) {
        this.comptabilise = comptabilise;
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

    public List<YvsGrhRubriqueBulletin> getListElementApercu() {
        return listElementApercu;
    }

    public void setListElementApercu(List<YvsGrhRubriqueBulletin> listElementApercu) {
        this.listElementApercu = listElementApercu;
    }

    public List<YvsGrhDetailBulletin> getListDetails() {
        return listDetails;
    }

    public void setListDetails(List<YvsGrhDetailBulletin> listDetails) {
        this.listDetails = listDetails;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ContratEmploye getContrat() {
        return contrat;
    }

    public void setContrat(ContratEmploye contrat) {
        this.contrat = contrat;
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

    public char getStatut() {
        return statut;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    public String getRefBulletin() {
        return refBulletin;
    }

    public void setRefBulletin(String refBulletin) {
        this.refBulletin = refBulletin;
    }

    public short getNumero() {
        return numero;
    }

    public void setNumero(short numero) {
        this.numero = numero;
    }

    public String getNumMonth() {
        return numMonth;
    }

    public void setNumMonth(String numMonth) {
        this.numMonth = numMonth;
    }

    public Date getDateCalcul() {
        return dateCalcul;
    }

    public void setDateCalcul(Date dateCalcul) {
        this.dateCalcul = dateCalcul;
    }

    public OrdreCalculSalaire getEntete() {
        return entete;
    }

    public void setEntete(OrdreCalculSalaire entete) {
        this.entete = entete;
    }

    public String getAnciennete() {
        return anciennete;
    }

    public void setAnciennete(String anciennete) {
        this.anciennete = anciennete;
    }

    public double getNetApayer() {
        return netApayer;
    }

    public void setNetApayer(double netApayer) {
        this.netApayer = netApayer;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final BulletinSalaire other = (BulletinSalaire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

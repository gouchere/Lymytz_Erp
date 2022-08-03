/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.credit;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.mutuelle.CaisseMutuelle;
import yvs.mutuelle.Compte;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ReglementCreditMut implements Serializable {

    private long id;
    private Date dateReglement = new Date();
    private double montant;
    private CaisseMutuelle caisse = new CaisseMutuelle();
    private boolean selectActif, new_;
    private char statutPiece = Constantes.STATUT_DOC_PAYER;
    private String reglerPar;
    private String modePaiement = Constantes.MUT_MODE_PAIEMENT_ESPECE;
    private Compte compte = new Compte();
    private Date dateSave = new Date();
    private Credit credit = new Credit();

    public ReglementCreditMut() {

    }

    public ReglementCreditMut(long id) {
        this.id = id;
    }

    public String getReglerPar() {
        return reglerPar;
    }

    public void setReglerPar(String reglerPar) {
        this.reglerPar = reglerPar;
    }

    public CaisseMutuelle getCaisse() {
        return caisse;
    }

    public void setCaisse(CaisseMutuelle caisse) {
        this.caisse = caisse;
    }

    public char getStatutPiece() {
        return statutPiece;
    }

    public void setStatutPiece(char statutPiece) {
        this.statutPiece = statutPiece;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateReglement() {
        return dateReglement;
    }

    public void setDateReglement(Date dateReglement) {
        this.dateReglement = dateReglement;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
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

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ReglementCreditMut other = (ReglementCreditMut) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

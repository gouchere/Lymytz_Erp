/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.salaire;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.dao.salaire.service.Constantes;
import yvs.mutuelle.Mutualiste;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class AvanceSalaire implements Serializable {

    private long id;
    private Date dateAvance = new Date();
    private String reference, etat;
    private char statutPaiement;
    private double montant, montantTotal, montantReste;
    private String motifRefus;
    private TypeAvance type = new TypeAvance();
    private Mutualiste mutualiste = new Mutualiste();
    private List<ReglementAvance> reglements;
    private boolean selectActif, new_, update = true;
 
    public AvanceSalaire() {
        reglements = new ArrayList<>();
     }

    public AvanceSalaire(long id) {
        this();
        this.id = id;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getMotifRefus() {
        return motifRefus;
    }

    public void setMotifRefus(String motifRefus) {
        this.motifRefus = motifRefus;
    }

    public String getEtat() {
        return etat != null ? etat.trim().length() > 0 ? etat : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public double getMontantReste() {
        return montantReste;
    }

    public void setMontantReste(double montantReste) {
        this.montantReste = montantReste;
    }

    public List<ReglementAvance> getReglements() {
        return reglements;
    }

    public void setReglements(List<ReglementAvance> reglements) {
        this.reglements = reglements;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateAvance() {
        return dateAvance != null ? dateAvance : new Date();
    }

    public void setDateAvance(Date dateAvance) {
        this.dateAvance = dateAvance;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public TypeAvance getType() {
        return type;
    }

    public void setType(TypeAvance type) {
        this.type = type;
    }

    public Mutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(Mutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    public char getStatutPaiement() {
        return statutPaiement != ' ' ? String.valueOf(statutPaiement).trim().length() > 0 ? statutPaiement : yvs.util.Constantes.STATUT_DOC_ATTENTE : yvs.util.Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatutPaiement(char statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

//    public List<YvsMutConditionAvance> getConditions() {
//        return conditions;
//    }
//
//    public void setConditions(List<YvsMutConditionAvance> conditions) {
//        this.conditions = conditions;
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final AvanceSalaire other = (AvanceSalaire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.evenement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.mutuelle.evenement.YvsMutFinancementActivite;
import yvs.entity.mutuelle.evenement.YvsMutParticipantEvenement;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class Activite implements Serializable {

    private long id;
    private double montantRequis, montantRecu;
    private Evenement evenement = new Evenement();
    private TypeContribution typeActivite = new TypeContribution();
    private List<YvsMutParticipantEvenement> participants;
    private List<YvsMutFinancementActivite> financements;

    public Activite() {
        participants = new ArrayList();
        financements = new ArrayList();
    }

    public Activite(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMontantRecu() {
        return montantRecu;
    }

    public void setMontantRecu(double montantRecu) {
        this.montantRecu = montantRecu;
    }

    public double getMontantRequis() {
        return montantRequis;
    }

    public void setMontantRequis(double montantRequis) {
        this.montantRequis = montantRequis;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public TypeContribution getTypeActivite() {
        return typeActivite;
    }

    public void setTypeActivite(TypeContribution typeActivite) {
        this.typeActivite = typeActivite;
    }

    public List<YvsMutParticipantEvenement> getParticipants() {
        return participants;
    }

    public void setParticipants(List<YvsMutParticipantEvenement> participants) {
        this.participants = participants;
    }

    public List<YvsMutFinancementActivite> getFinancements() {
        return financements;
    }

    public void setFinancements(List<YvsMutFinancementActivite> financements) {
        this.financements = financements;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Activite other = (Activite) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

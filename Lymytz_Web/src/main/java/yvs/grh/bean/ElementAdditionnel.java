/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.parametrage.PlanPrelevement;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class ElementAdditionnel implements Serializable {

    private long id;
    private String description;
    private double montant;
    private Date date = new Date();   //date du positionnement de l'élément de retenu
    private boolean planifie = false;    //précise si un élément de retenue a été planifié dans le temps
    private TypeElementAdd typeElt = new TypeElementAdd();
    private ContratEmploye contrat = new ContratEmploye();
    private List<YvsGrhDetailPrelevementEmps> listPrelevement;
    private PlanPrelevement plan = new PlanPrelevement();
//    private PrelevementEmps prelevement;    //cette attribut est ajouté contre les règle initiale pour conserver l'id du prélèvement qu'on perd facilent sur la vue
    private boolean permanent = true, suspendu, comptabilise;
    private Date debut = new Date(), fin = new Date(), debutSuspension = new Date();
    private char statut = 'E';   //statut de la retenu E=En cours, S=suspendu, R=réglé
    private int dureeSuspension;
    private double montantPlanifie;
    private Date dateSave = new Date();

    public ElementAdditionnel() {
        listPrelevement = new ArrayList<>();
    }

    public ElementAdditionnel(long id) {
        this.id = id;
        listPrelevement = new ArrayList<>();
    }

    public Date getDebutSuspension() {
        return debutSuspension;
    }

    public void setDebutSuspension(Date debutSuspension) {
        this.debutSuspension = debutSuspension;
    }

    public boolean isSuspendu() {
        return suspendu;
    }

    public void setSuspendu(boolean suspendu) {
        this.suspendu = suspendu;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public List<YvsGrhDetailPrelevementEmps> getListPrelevement() {
        return listPrelevement;
    }

    public void setListPrelevement(List<YvsGrhDetailPrelevementEmps> listPrelevement) {
        this.listPrelevement = listPrelevement;
    }

    public ContratEmploye getContrat() {
        return contrat;
    }

    public void setContrat(ContratEmploye contrat) {
        this.contrat = contrat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public TypeElementAdd getTypeElt() {
        return typeElt;
    }

    public void setTypeElt(TypeElementAdd typeElt) {
        this.typeElt = typeElt;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isPlanifie() {
        return planifie;
    }

    public void setPlanifie(boolean planifie) {
        this.planifie = planifie;
    }

    public boolean isComptabilise() {
        return comptabilise;
    }

    public void setComptabilise(boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public PlanPrelevement getPlan() {
        return plan;
    }

    public void setPlan(PlanPrelevement plan) {
        this.plan = plan;
    }

    public char getStatut() {
        return statut;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    public int getDureeSuspension() {
        return dureeSuspension;
    }

    public void setDureeSuspension(int dureeSuspension) {
        this.dureeSuspension = dureeSuspension;
    }

    public double getMontantPlanifie() {
        return montantPlanifie;
    }

    public void setMontantPlanifie(double montantPlanifie) {
        this.montantPlanifie = montantPlanifie;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ElementAdditionnel other = (ElementAdditionnel) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}

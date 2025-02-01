/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.poste;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.bean.TypeElementAdd;
import yvs.grh.contrat.ModelContrat;
import yvs.grh.paie.PrelevementEmps;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ElementAdditionnelPoste implements Serializable {

    private long id;
    private String description;
    private double montant;
    private Date date = new Date();   //date du positionnement de l'élément de retenu
    private Date dateSave = new Date();   //date du positionnement de l'élément de retenu
    private boolean planifie;    //précise si un élément de retenue a été planifié dans le temps
    private TypeElementAdd typeElt = new TypeElementAdd();
    private PosteDeTravail poste = new PosteDeTravail();
    private boolean selectActif;
    private List<PrelevementEmps> listPrelevement;
    private ModelContrat model = new ModelContrat();
    private PrelevementEmps prelevement;    //cette attribut est ajouté contre les règle initiale pour conserver l'id du prélèvement qu'on perd facilent sur la vue
    private boolean permanent = true, suspendu;
    private Date debut = new Date(), fin = new Date();

    public ElementAdditionnelPoste() {
        listPrelevement = new ArrayList<>();
    }

    public ModelContrat getModel() {
        return model;
    }

    public void setModel(ModelContrat model) {
        this.model = model;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
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

    public TypeElementAdd getTypeElt() {
        return typeElt;
    }

    public void setTypeElt(TypeElementAdd typeElt) {
        this.typeElt = typeElt;
    }

    public void setPoste(PosteDeTravail poste) {
        this.poste = poste;
    }

    public PosteDeTravail getPoste() { 
        return poste;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public List<PrelevementEmps> getListPrelevement() {
        return listPrelevement;
    }

    public void setListPrelevement(List<PrelevementEmps> listPrelevement) {
        this.listPrelevement = listPrelevement;
    }

    public PrelevementEmps getPrelevement() {
        return prelevement;
    }

    public void setPrelevement(PrelevementEmps prelevement) {
        this.prelevement = prelevement;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public boolean isSuspendu() {
        return suspendu;
    }

    public void setSuspendu(boolean suspendu) {
        this.suspendu = suspendu;
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
        final ElementAdditionnelPoste other = (ElementAdditionnelPoste) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

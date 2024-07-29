/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.production.base.YvsProdDocumentTechnique;
import yvs.entity.production.base.YvsProdIndicateurOp;
import yvs.entity.production.base.YvsProdPosteOperation;
import yvs.entity.production.pilotage.YvsProdComposantOp;
import yvs.production.planification.DetailPlanPDP;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class OperationsGamme implements Serializable, Comparator<OperationsGamme> {

    private int id;
    private int numero = 10;
    private String reference;
    private double tauxEfficience;
    private double tauxDePerte;
    private double dureeReglage;
    private double dureeOperation;
    private String typeDeTemps; //Proportionnel, Fixe, Cadence  (relatif à la durée opératoire)  
    /*pour le type proportionnel, il faut définir une quantité de base; pour le type cadence, on définit la qté cadence*/
    private double cadence;
    private double quantiteBase;
    private double quantiteMinimale;
    private String description;
    private boolean actif = true;
    private boolean selectActif;
    private Date dateSave = new Date();
    private Character typeCout = Constantes.PROD_TYPE_COUT_TAUX;

    private DetailPlanPDP pdp = new DetailPlanPDP();

    private List<YvsProdPosteOperation> postes;
    private List<YvsProdDocumentTechnique> documents;
    private List<YvsProdIndicateurOp> indicateurs;
    private List<YvsProdComposantOp> composants;
    private YvsProdComposantOp selectComposantOp;

    public OperationsGamme() {
        documents = new ArrayList<>();
        postes = new ArrayList<>();
        indicateurs = new ArrayList<>();
        composants = new ArrayList<>();
        selectComposantOp = new YvsProdComposantOp(-1);
    }

    public YvsProdComposantOp getSelectComposantOp() {
        return selectComposantOp;
    }

    public void setSelectComposantOp(YvsProdComposantOp selectComposantOp) {
        this.selectComposantOp = selectComposantOp;
    }

    public OperationsGamme(int id) {
        this();
        this.id = id;
    }

    public OperationsGamme(int id, int numero) {
        this(id);
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getTauxEfficience() {
        return tauxEfficience;
    }

    public void setTauxEfficience(double tauxEfficience) {
        this.tauxEfficience = tauxEfficience;
    }

    public double getTauxDePerte() {
        return tauxDePerte;
    }

    public void setTauxDePerte(double tauxDePerte) {
        this.tauxDePerte = tauxDePerte;
    }

    public double getDureeReglage() {
        return dureeReglage;
    }

    public void setDureeReglage(double dureeReglage) {
        this.dureeReglage = dureeReglage;
    }

    public double getDureeOperation() {
        return dureeOperation;
    }

    public void setDureeOperation(double dureeOperation) {
        this.dureeOperation = dureeOperation;
    }

    public String getTypeDeTemps() {
        return typeDeTemps != null ? typeDeTemps.trim().length() > 0 ? typeDeTemps : Constantes.PROD_OP_TYPE_TEMPS_PROPORTIONNEL : Constantes.PROD_OP_TYPE_TEMPS_PROPORTIONNEL;
    }

    public void setTypeDeTemps(String typeDeTemps) {
        this.typeDeTemps = typeDeTemps;
    }

    public double getCadence() {
        return cadence;
    }

    public void setCadence(double cadence) {
        this.cadence = cadence;
    }

    public double getQuantiteBase() {
        return quantiteBase;
    }

    public void setQuantiteBase(double quantiteBase) {
        this.quantiteBase = quantiteBase;
    }

    public double getQuantiteMinimale() {
        return quantiteMinimale;
    }

    public void setQuantiteMinimale(double quantiteMinimale) {
        this.quantiteMinimale = quantiteMinimale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public DetailPlanPDP getPdp() {
        return pdp;
    }

    public void setPdp(DetailPlanPDP pdp) {
        this.pdp = pdp;
    }

    public List<YvsProdPosteOperation> getPostes() {
        return postes;
    }

    public void setPostes(List<YvsProdPosteOperation> postes) {
        this.postes = postes;
    }

    public List<YvsProdDocumentTechnique> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsProdDocumentTechnique> documents) {
        this.documents = documents;
    }

    public List<YvsProdIndicateurOp> getIndicateurs() {
        return indicateurs;
    }

    public void setIndicateurs(List<YvsProdIndicateurOp> indicateurs) {
        this.indicateurs = indicateurs;
    }

    public List<YvsProdComposantOp> getComposants() {
        return composants;
    }

    public void setComposants(List<YvsProdComposantOp> composants) {
        this.composants = composants;
    }

    public Character getTypeCout() {
        return typeCout != null ? typeCout : Constantes.PROD_TYPE_COUT_TAUX;
    }

    public void setTypeCout(Character typeCout) {
        this.typeCout = typeCout;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.id;
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
        final OperationsGamme other = (OperationsGamme) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compare(OperationsGamme o1, OperationsGamme o2) {
        if (o1.getNumero() - (o2.getNumero()) > 0) {
            return 1;
        } else if (o1.getNumero() - (o2.getNumero()) < 0) {
            return -1;
        }
        return 0;

    }
}

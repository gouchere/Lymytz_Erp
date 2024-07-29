/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;

/**
 *
 * @author hp Elite 8300
 */
@SessionScoped
@ManagedBean
public class ModelDocuments implements Serializable {

    private int id;
    private String titreDocument;
    private String tableDoc;
    private boolean definedLivraison, definedUpdate, definedReglement;
    private List<YvsWorkflowEtapeValidation> etapesValidation;

    public ModelDocuments() {
        etapesValidation = new ArrayList<>();
    }

    public ModelDocuments(int id, String titreDocument, String tableDoc) {
        this.id = id;
        this.titreDocument = titreDocument;
        this.tableDoc = tableDoc;
    }

    public boolean isDefinedLivraison() {
        return definedLivraison;
    }

    public void setDefinedLivraison(boolean definedLivraison) {
        this.definedLivraison = definedLivraison;
    }

    public List<YvsWorkflowEtapeValidation> getEtapesValidation() {
        return etapesValidation;
    }

    public void setEtapesValidation(List<YvsWorkflowEtapeValidation> etapesValidation) {
        this.etapesValidation = etapesValidation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitreDocument() {
        return titreDocument != null ? titreDocument : "";
    }

    public void setTitreDocument(String titreDocument) {
        this.titreDocument = titreDocument;
    }

    public String getTableDoc() {
        return tableDoc;
    }

    public void setTableDoc(String tableDoc) {
        this.tableDoc = tableDoc;
    }

    public boolean isDefinedUpdate() {
        return definedUpdate;
    }

    public void setDefinedUpdate(boolean definedUpdate) {
        this.definedUpdate = definedUpdate;
    }

    public boolean isDefinedReglement() {
        return definedReglement;
    }

    public void setDefinedReglement(boolean definedReglement) {
        this.definedReglement = definedReglement;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
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
        final ModelDocuments other = (ModelDocuments) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

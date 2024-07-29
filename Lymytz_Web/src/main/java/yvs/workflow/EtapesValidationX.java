/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.workflow;

import java.io.Serializable;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 * @param <T>
 */
public class EtapesValidationX<T extends Serializable> {

    private long id;
    private Boolean etapeValid;
    private YvsUsersAgence author;
    private YvsWorkflowEtapeValidation etape;
    private T document;

    public EtapesValidationX() {
    }

    public EtapesValidationX(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean isEtapeValid() {
        return etapeValid;
    }

    public void setEtapeValid(Boolean etapeValid) {
        this.etapeValid = etapeValid;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsWorkflowEtapeValidation getEtape() {
        return etape;
    }

    public void setEtape(YvsWorkflowEtapeValidation etape) {
        this.etape = etape;
    }

    public T getDocument() {
        return document;
    }

    public void setDocument(T document) {
        this.document = document;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final EtapesValidationX<?> other = (EtapesValidationX<?>) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

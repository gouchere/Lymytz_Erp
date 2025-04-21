/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.operation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import yvs.entity.mutuelle.operation.YvsMutOperationCompte;

/**
 *
 * @author hp Elite 8300
 * cette classe permet de recapituler les opérations par date
 */
public class RecapOperation {

    private Date date; //(Mois)
    private String refperiode; //(Mois)
    private List<YvsMutOperationCompte> listOperation;

    public RecapOperation() {
        listOperation = new ArrayList<>();
    }

    public RecapOperation(Date date) {
        this.date = date;
        listOperation = new ArrayList<>();
    }
    public RecapOperation(Date date, List<YvsMutOperationCompte> listOperation) {
        this.date = date;
        this.listOperation = listOperation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<YvsMutOperationCompte> getListOperation() {
        return listOperation;
    }

    public void setListOperation(List<YvsMutOperationCompte> listOperation) {
        this.listOperation = listOperation;
    }

    public String getRefperiode() {
        return refperiode;
    }

    public void setRefperiode(String refperiode) {
        this.refperiode = refperiode;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.refperiode);
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
        final RecapOperation other = (RecapOperation) obj;
        if (!Objects.equals(this.refperiode, other.refperiode)) {
            return false;
        }
        return true;
    }
        

    

}

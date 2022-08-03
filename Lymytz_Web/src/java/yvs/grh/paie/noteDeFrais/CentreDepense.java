/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie.noteDeFrais;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class CentreDepense<T> implements Serializable{

    private long id;
    private long idSource;
    private T source;
    private String nameSource;

    public CentreDepense() {
    }

    public CentreDepense(long id) {
        this.id = id;
    }

    public CentreDepense(long id,String nameSource) {
        this.id = id;        
        this.nameSource = nameSource;
    }

    
    public String getNameSource() {
        return nameSource;
    }

    public void setNameSource(String nameSource) {
        this.nameSource = nameSource;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdSource() {
        return idSource;
    }

    public void setIdSource(long idSource) {
        this.idSource = idSource;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final CentreDepense<?> other = (CentreDepense<?>) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
}

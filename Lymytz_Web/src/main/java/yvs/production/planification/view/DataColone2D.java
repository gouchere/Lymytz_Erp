/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification.view;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class DataColone2D implements Serializable {

    private long id;
    private ObjectData data;

    public DataColone2D() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DataColone2D(ObjectData data) {
        this.data = data;
    }

    public DataColone2D(long id, ObjectData data) {
        this.id = id;
        this.data = data;
    }

    public ObjectData getData() {
        return data;
    }

    public void setData(ObjectData data) {
        this.data = data;
    }

}

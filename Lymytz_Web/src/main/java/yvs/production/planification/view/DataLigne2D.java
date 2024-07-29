/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification.view;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class DataLigne2D implements Serializable {

    private long id;
    private String labelDetail;
    private List<DataColone2D> listData;

    public DataLigne2D() {
        listData = new ArrayList<>();
    }

    public DataLigne2D(String labelDetail) {
        this.labelDetail = labelDetail;
        listData = new ArrayList<>();
    }

    public List<DataColone2D> getListData() {
        return listData;
    }

    public void setListData(List<DataColone2D> listData) {
        this.listData = listData;
    }

    public String getLabelDetail() {
        return labelDetail;
    }

    public void setLabelDetail(String labelDetail) {
        this.labelDetail = labelDetail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final DataLigne2D other = (DataLigne2D) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

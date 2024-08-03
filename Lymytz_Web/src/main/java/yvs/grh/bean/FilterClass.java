/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import yvs.grh.presence.PointageEmploye;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@RequestScoped
public class FilterClass implements Serializable {

    private List<PointageEmploye> listFilter;

    public FilterClass() {
    }

    public List<PointageEmploye> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<PointageEmploye> listFilter) {
        this.listFilter = listFilter;
    }

}

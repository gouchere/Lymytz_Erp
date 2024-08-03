/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.grh.sanction;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class Sanction implements Serializable{
    private List<SanctionEmps> listSanctionEmps;
    private int id;
    private boolean actif = true;
    private boolean supp;    
    private String description;
    private String code;
    private double point;
    private FauteSanction faute = new FauteSanction();
    private DecisionSanction decision = new DecisionSanction();
    private boolean selectActif;

    public Sanction() {
    }

    public Sanction(Integer id) {
        this.id = id;
    }

    public List<SanctionEmps> getListSanctionEmps() {
        return listSanctionEmps;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public FauteSanction getFaute() {
        return faute;
    }

    public void setFaute(FauteSanction faute) {
        this.faute = faute;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public void setListSanctionEmps(List<SanctionEmps> listSanctionEmps) {
        this.listSanctionEmps = listSanctionEmps;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean isActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean isSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public DecisionSanction getDecision() {
        return decision;
    }

    public void setDecision(DecisionSanction decision) {
        this.decision = decision;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
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
        final Sanction other = (Sanction) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.majoration;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.grh.taches.YvsGrhIntervalMajoration;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class MajorationConge implements Serializable {

    private Long id;
    private String nature;
    private Boolean actif;
    private List<YvsGrhIntervalMajoration> listIntervalle;
    private String uniteIntervalle;

    public MajorationConge() {
        listIntervalle = new ArrayList<>();
    }

    public MajorationConge(Long id) {
        this.id = id;
        listIntervalle = new ArrayList<>();
    }

    public MajorationConge(Long id, String nature, Boolean actif, String unite) {
        this.id = id;
        this.nature = nature;
        this.actif = actif;
        this.uniteIntervalle=unite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public Boolean isActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public void setListIntervalle(List<YvsGrhIntervalMajoration> listIntervalle) {
        this.listIntervalle = listIntervalle;
    }

    public List<YvsGrhIntervalMajoration> getListIntervalle() {
        return listIntervalle;
    }

    public String getUniteIntervalle() {
        return uniteIntervalle;
    }

    public void setUniteIntervalle(String uniteIntervalle) {
        this.uniteIntervalle = uniteIntervalle;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final MajorationConge other = (MajorationConge) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}

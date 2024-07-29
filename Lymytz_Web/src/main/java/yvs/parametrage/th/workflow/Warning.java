/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.th.workflow;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz-pc
 */
@ManagedBean
@SessionScoped
public class Warning implements Serializable {

    private Integer id;
    private String nature;
    private String titre;
    private String description;
    private String type = "A";
    private long count;

    public Warning() {
    }

    public Warning(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getTitre() {
        return titre != null ? titre : "";
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getType() {
        return type != null ? type.trim().length() > 0 ? type : "A" : "A";
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Warning build(Object[] in) {
        if (in != null ? in.length > 0 : false) {
            setDescription((String) in[0]);
            long count = 0;
            try{
                count = in.length > 1 ? (Long) in[1]:0;
            }catch(Exception ex){
                count = in.length > 1 ? (Integer) in[1]:0;
            }
            setCount(count);
            setTitre(in.length > 2 ? (String) in[2] : "");
            setNature(in.length > 3 ? (String) in[3] : "");
            setId(in.length > 4 ? (Integer) in[4] : 0);
            setType(in.length > 5 ? (String) in[5] : "A");
        }
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Warning other = (Warning) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Warning{" + "id=" + id + ", nature=" + nature + ", titre=" + titre + ", description=" + description + ", ocunt=" + count + '}';
    }

}

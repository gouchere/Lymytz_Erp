/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.catTarif;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.BeanDeBase;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "catT")
@SessionScoped
public class CategorieTarifaire extends BeanDeBase implements Serializable {

    private long id;
    private String designation;
    private String codeAppel;

    public CategorieTarifaire() {
        list=new ArrayList<>();
    }

    public CategorieTarifaire(String designation) {
        this.designation = designation;
    }
     public CategorieTarifaire(long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodeAppel() {
        return codeAppel;
    }

    public void setCodeAppel(String codeAppel) {
        this.codeAppel = codeAppel;
    }
    
    private List<CategorieTarifaire> list;

    public List<CategorieTarifaire> loadList(long societe){
        String[] ch = {"societe"};
        Object[] v = {societe};
        List<Object[]> l = dao.loadNameQueries("YvsCatTarif.findByActif", ch, v);
        list.clear();
        for (Object[] tuple : l) {
            //récupère la liste des relations GroupeProduit-CategorieTarif contenu dans g
            CategorieTarifaire ca = new CategorieTarifaire((long) tuple[0]);
            ca.setDesignation((String) tuple[1]);
            ca.setCodeAppel((String)tuple[2]);
            list.add(0, ca);
        }
        return list;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CategorieTarifaire other = (CategorieTarifaire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}

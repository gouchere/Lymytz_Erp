/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import yvs.commercial.rrr.PlanTarifGroup;
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.BeanDeBase;
import yvs.base.produits.GroupeCatC;

/**
 *
 * @author GOUCHERE YVES
 */
@SessionScoped
@ManagedBean(name = "groupeProd")
public class GroupeDeProduit extends BeanDeBase implements Serializable {

    private long id;
    private String refGroupe;
    private String description;
    private String codeAppel;
//    private String agence;
    private List<PlanTarifGroup> listTarif;
    private List<GroupeDepot> listGroupeDepot;
    private List<GroupeCatC> listArtCatC;

    /**
     * Creates a new instance of GroupeDeProduit
     */
    public GroupeDeProduit() {
        listTarif = new ArrayList<>();
        listGroupeDepot = new ArrayList<>();
        listArtCatC = new ArrayList<>();
        list = new ArrayList<>();
    }

    public GroupeDeProduit(long id) {
        this.id = id;
        listTarif = new ArrayList<>();
        listGroupeDepot = new ArrayList<>();
        listArtCatC = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRefGroupe() {
        return refGroupe;
    }

    public void setRefGroupe(String refGroupe) {
        this.refGroupe = refGroupe;
    }

    public String getCodeAppel() {
        return codeAppel;
    }

    public void setCodeAppel(String codeAppel) {
        this.codeAppel = codeAppel;
    }

    public List<PlanTarifGroup> getListTarif() {
        return listTarif;
    }

    public void setListTarif(List<PlanTarifGroup> listTarif) {
        this.listTarif = listTarif;
    }

    public List<GroupeDepot> getListGroupeDepot() {
        return listGroupeDepot;
    }

    public void setListGroupeDepot(List<GroupeDepot> listGroupeDepot) {
        this.listGroupeDepot = listGroupeDepot;
    }

    public List<GroupeCatC> getListArtCatC() {
        return listArtCatC;
    }

    public void setListArtCatC(List<GroupeCatC> listArtCatC) {
        this.listArtCatC = listArtCatC;
    }
    private static List<GroupeDeProduit> list;

    public List<GroupeDeProduit> loadList() {
        list.clear();
        String[] ch = {};
        Object[] val = {};
        List<Object[]> lg = dao.loadNameQueries("YvsGroupesProduits.findByActif", ch, val);
        for (Object[] tuple : lg) {
            //récupère la liste des relations GroupeProduit-CategorieTarif contenu dans g
            GroupeDeProduit gro = new GroupeDeProduit((long) tuple[0]);
            gro.setRefGroupe((String) tuple[1]);
            list.add(0, gro);
        }
        return list;

    }

    private GroupeDeProduit findGroupe(String code, int deb, int fin) {
        int current;
        fin = list.size();
        current = (deb + fin) / 2;
        GroupeDeProduit g = list.get(current);
        if (g.getCodeAppel() == null ? code != null : !g.getCodeAppel().equals(code)) {
            return g;
        } else {
            if (g.getCodeAppel().compareTo(code) > 0) {
                fin = current - 1;
                findGroupe(code, deb, fin);
            } else {
                deb = current + 1;
                findGroupe(code, deb, fin);
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GroupeDeProduit other = (GroupeDeProduit) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}

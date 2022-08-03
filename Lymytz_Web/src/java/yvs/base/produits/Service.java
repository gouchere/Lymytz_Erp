/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.BeanDeBase;
import yvs.commercial.client.PlanTarifaireClient;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "service")
@SessionScoped
public class Service extends BeanDeBase implements Serializable, Comparator<Service> {

    private long id;
    private String refService;
    private String designation;
    private String description;
    private double pua;
    private double puv;
    private double puvMin;
    private double remise;
    private String categorie;
    private String codeBarre;
    private boolean suiviEnStock = true;
//    private String classe;
    private boolean changePrix; //signifie que le prix de vente est négociable;
    private String refGroupe;
    private long idGroupe;
    private String classeStat;
    private List<PlanTarifaireClient> listTarifArt;

    public Service() {
        listTarifArt = new ArrayList<>();
    }

    public Service(long id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public boolean isChangePrix() {
        return changePrix;
    }

    public void setChangePrix(boolean changePrix) {
        this.changePrix = changePrix;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public long getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(long idGroupe) {
        this.idGroupe = idGroupe;
    }

    public List<PlanTarifaireClient> getListTarifArt() {
        return listTarifArt;
    }

    public void setListTarifArt(List<PlanTarifaireClient> listTarifArt) {
        this.listTarifArt = listTarifArt;
    }

    public double getPua() {
        return pua;
    }

    public void setPua(double pua) {
        this.pua = pua;
    }

    public double getPuv() {
        return puv;
    }

    public void setPuv(double puv) {
        this.puv = puv;
    }

    public double getPuvMin() {
        return puvMin;
    }

    public void setPuvMin(double puvMin) {
        this.puvMin = puvMin;
    }

    public String getRefService() {
        return refService;
    }

    public void setRefService(String refService) {
        this.refService = refService;
    }

    public String getRefGroupe() {
        return refGroupe;
    }

    public void setRefGroupe(String refGroupe) {
        this.refGroupe = refGroupe;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public boolean isSuiviEnStock() {
        return suiviEnStock;
    }

    public void setSuiviEnStock(boolean suiviEnStock) {
        this.suiviEnStock = suiviEnStock;
    }

    public String getClasseStat() {
        return classeStat;
    }

    public void setClasseStat(String classeStat) {
        this.classeStat = classeStat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Service other = (Service) obj;
        if (!Objects.equals(this.refService, other.refService)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.refService);
        return hash;
    }

    @Override
    public int compare(Service o1, Service o2) {
        if (o1.getRefGroupe().compareTo(o2.getRefGroupe()) > 0) {
            return 1;
        } else if (o1.getRefGroupe().compareTo(o2.getRefGroupe()) < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}

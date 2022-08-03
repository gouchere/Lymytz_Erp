/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import yvs.parametrage.poste.Departements;
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
//import yvs.entity.grh.param.YvsDepartements;
//import yvs.entity.grh.param.YvsServices;
import yvs.util.Managed;

/**
 *
 * @author GOUCHERE YVES
 */

@ManagedBean
@SessionScoped
public class Services extends Managed<Services, YvsBaseCaisse> implements Serializable {

    private int id;
    private String intitule;
    private String description;
    private List<Services> listValue;
    private boolean update;
    private Departements departement = new Departements();

    public Services() {
        listValue = new ArrayList<>();
    }

    public Services(int id, String intitule) {
        this.id = id;
        this.intitule = intitule;
    }

    public Services(int id, String intitule, String description) {
        this.id = id;
        this.intitule = intitule;
        this.description = description;
        listValue = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Services> getListValue() {
        return listValue;
    }

    public void setListValue(List<Services> listValue) {
        this.listValue = listValue;
    }

    public Departements getDepartement() {
        return departement;
    }

    public void setDepartement(Departements departement) {
        this.departement = departement;
    }

    @Override
    public boolean controleFiche(Services bean) {
        //contrôle qu'il y ai pas deux service de même intitulé
        if (departement.getId() == 0) {
            getMessage("Veuillez choisir le département à rattacher", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (this.getIntitule() != null) {
            String[] champ = new String[]{"departement", "intitule"};
            Object[] vam = new Object[]{departement.getId(), intitule};
            Long dep = (Long) dao.loadObjectByNameQueries("YvsServices.countS", champ, vam);
            if (dep != null) {
                if (dep != 0) {
                    getMessage("Un service de même nom a déjà été trouvé", FacesMessage.SEVERITY_ERROR);
                }
                return dep == 0;//si le résultat est égale à zéro, c'est qu'il y a pas de encore de département avec le même intitulé
            }
        } else {
            getMessage("Vous devez saisir une désignation pour ce département", FacesMessage.SEVERITY_ERROR);
        }
        return true;
    }

    @Override
    public void updateBean() {
        if (intitule != null && departement.getId() != 0) {
//            YvsServices dep = new YvsServices(this.getId());
//            dep.setIntitule(intitule);
//            dep.setDescription(description);
//            dep.setYvsDepartements(new YvsDepartements(departement.getId()));
//            dao.update(dep);
            update = false;
            Services d = new Services(id, intitule); 
            d.setDescription(description);
            listValue.set(listValue.indexOf(d), d);
        } else {
            getMessage("Imposible d'effectuer la modification, il y a des informations manquantes", FacesMessage.SEVERITY_ERROR);
        }
    }

    @Override
    public Services recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Services bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    int row = 0;

    @Override
    public void loadOnView(SelectEvent ev) {
        Services s = (Services) ev.getObject();
        DataTable tab = (DataTable) ev.getSource();
        row = tab.getRowIndex();
        this.id = s.getId();
        this.setIntitule(s.getIntitule());
        setDescription(s.getDescription());
        departement.setId(s.getDepartement().getId());
        update = true;
    }

    @Override
    public void loadAll() {
        String[] champ = new String[]{"agence"};
        Object[] vam = new Object[]{currentAgence};
//        List<YvsServices> l = dao.loadNameQueries("YvsServices.findAll", champ, vam);
//        if (l != null) {
//            listValue.clear();
//            for (YvsServices d : l) {
//                Services s = new Services(d.getId(), d.getIntitule());
//                s.setDescription(d.getDescription());
//                s.setDepartement(new Departements(d.getYvsDepartements().getId(), d.getYvsDepartements().getIntitule()));
//                listValue.add(s);
//            }
//        }
    }

    @Override
    public boolean saveNew() {
        if (!update) {
            if (controleFiche(this)) {
//                YvsServices dep = new YvsServices();
//                dep.setIntitule(intitule);
//                dep.setDescription(description);
//                dep.setYvsDepartements(new YvsDepartements(departement.getId()));
//                dep = (YvsServices) dao.save1(dep);
//                listValue.add(new Services(dep.getId(), this.intitule, this.description));
            }
        } else {
            updateBean();
        }
        resetFiche(this);
        update = false;
        row = 0;
        succes();
        return true;
    }

    public void remove() {
        try {
//            boolean b = dao.delete(new YvsServices(this.id));
        } catch (Exception ex) {
            getMessage("erreur de  suppression du service ! cette instance est probablement liée à d'autres données", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void resetFiche(Services s) {
        this.setIntitule(null);
        setDescription(null);
        update = false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + this.id;
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
        final Services other = (Services) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

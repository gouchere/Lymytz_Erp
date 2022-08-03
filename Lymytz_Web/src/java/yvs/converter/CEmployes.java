/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.converter;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import yvs.util.Managed;

/**
 *
 * @author LYMYTZ-PC
 */
@FacesConverter("CEMPS")
public class CEmployes extends Managed<Serializable, Serializable> implements Converter {

    private List<Employe> employes;

    public CEmployes() {
        
    }

    @PostConstruct
    public void init(){
    String[] champ = new String[]{"agence"};
        Object[] val = new Object[]{currentAgence};
        employes = UtilGrh.buildListEmployeBean(dao.loadNameQueries("YvsGrhEmployes.findAll", champ, val));
    }
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        //recherche un employé dans la liste
        for(Employe e:employes){
            if(e.getMatricule().equals(value)){
                return e;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return "";
    }

    @Override
    public boolean controleFiche(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Serializable recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

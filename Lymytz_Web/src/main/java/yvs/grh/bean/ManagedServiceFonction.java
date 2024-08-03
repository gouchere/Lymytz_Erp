/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.grh.bean;

import yvs.parametrage.poste.Departements;
import yvs.parametrage.poste.PosteDeTravail;
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.util.Managed;

/**
 *
 * @author GOUCHERE YVES
 */
@SessionScoped
@ManagedBean(name = "MservFonc")
public class ManagedServiceFonction extends Managed<Departements, YvsBaseCaisse> implements Serializable{
    
    @ManagedProperty(value = "#{services}")
    private Departements service;
    @ManagedProperty(value = "#{fonctions}")
    private PosteDeTravail fonction;
    
    private List<Departements> listService;
    private List<PosteDeTravail> listFonction;

    public ManagedServiceFonction() {
        listFonction=new ArrayList<>();
        listService=new ArrayList<>();
    }

    public Departements getService() {
        return service;
    }

    public void setService(Departements service) {
        this.service = service;
    }

    public PosteDeTravail getFonction() {
        return fonction;
    }

    public void setFonction(PosteDeTravail fonction) {
        this.fonction = fonction;
    }

    public List<Departements> getListService() {
        return listService;
    }

    public void setListService(List<Departements> listService) {
        this.listService = listService;
    }

    public List<PosteDeTravail> getListFonction() {
        return listFonction;
    }

    public void setListFonction(List<PosteDeTravail> listFonction) {
        this.listFonction = listFonction;
    }

    @Override
    public boolean controleFiche(Departements bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Departements recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Departements bean) {
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

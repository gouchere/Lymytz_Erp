///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.parametrage.taxes;
//
//import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.SessionScoped;
//import org.primefaces.event.SelectEvent;
//import org.primefaces.event.UnselectEvent;
//import yvs.entity.param.YvsTaxes;
//import yvs.util.Managed;
//
///**
// *
// * @author GOUCHERE YVES
// */
//@ManagedBean(name = "Mtaxes")
//@SessionScoped
//public class ManagedTaxes extends Managed<Taxes, YvsBaseCaisse> implements Serializable {
//
//    List<Taxes> listTaxe;
//    List<Taxes> listFiltre;
//    @ManagedProperty(value = "#{taxes}")
//    private Taxes taxe;
//    private Taxes selectTaxe;
//
//    public ManagedTaxes() {
//        listTaxe = new ArrayList<>();
//    }
//
//    public List<Taxes> getListFiltre() {
//        return listFiltre;
//    }
//
//    public void setListFiltre(List<Taxes> listFiltre) {
//        this.listFiltre = listFiltre;
//    }
//
//    public List<Taxes> getListTaxe() {
//        return listTaxe;
//    }
//
//    public void setListTaxe(List<Taxes> listTaxe) {
//        this.listTaxe = listTaxe;
//    }
//
//    public Taxes getTaxe() {
//        return taxe;
//    }
//
//    public void setTaxe(Taxes taxe) {
//        this.taxe = taxe;
//    }
//
//    public Taxes getSelectTaxe() {
//        return selectTaxe;
//    }
//
//    public void setSelectTaxe(Taxes selectTaxe) {
//        this.selectTaxe = selectTaxe;
//    }
//
//    @Override
//    public boolean controleFiche(Taxes bean) {
//        if (bean.getCodeTaxe() == null) {
//            getMessage(message.getMessage("codeTaxe"), FacesMessage.SEVERITY_INFO);
//            return false;
//        }
//        if (bean.getTaux() == 0) {
//            getMessage(message.getMessage("tauxTaxe"), FacesMessage.SEVERITY_INFO);
//            return false;
//        }
//        return true;
//    }
//    long key;
//
//    @Override
//    public boolean saveNew() {
//        Taxes tax = recopieView();
//        if (controleFiche(tax)) {
//            YvsTaxes ta = buildEntityFromBean(tax);
//            ta.setSociete(currentScte);
//            ta.setId(null);
//            dao.save(ta);
//            listTaxe.add(0, tax);
//            update("tax-table");
//            succes();
//            setDisableSave(true);
//        }
//        return true;
//    }
//
//    @Override
//    public void updateBean() {
//        Taxes tax = recopieView();
//        if (controleFiche(tax)) {
//            YvsTaxes ta = buildEntityFromBean(tax);
//            ta.setLastAuthor(getUserOnLine());
//            ta.setLastDateSave(new Date());
//            tax.setLastAuteur(getUserOnLine());
//            tax.setLastDateUpdate(df.format(new Date()));
//            dao.update(ta);
//            listTaxe.set(listTaxe.indexOf(tax), tax);
//            update("tax-table");
//            getMessage(message.getMessage("effectue"), FacesMessage.SEVERITY_INFO);
//            setDisableSave(true);
//        }
//    }
//
//    @Override
//    public Taxes recopieView() {
//        Taxes tax = new Taxes();        
//        tax.setCodeTaxe(taxe.getCodeTaxe());
//        tax.setTaux(taxe.getTaux());
//        tax.setActif(taxe.isActif());
//        tax.setId(taxe.getId());
//        tax.setCodeAppel(taxe.getCodeAppel());
//        if (tax.getDateSave() != null) {
//            tax.setDateSave(df.format(taxe.getDateSave()));
//        } else {
//            tax.setDateSave(df.format(new Date()));
//        }
//        if (tax.getLastDateUpdate() != null) {
//            tax.setLastDateUpdate(df.format(taxe.getLastDateUpdate()));
//        } else {
//            tax.setLastDateUpdate(df.format(new Date()));
//        }
//        if (tax.getAuteur() != null) {
//            tax.setAuteur(taxe.getAuteur());
//        } else {
//            tax.setAuteur(getUserOnLine());
//        }
//        if (tax.getLastAuteur() != null) {
//            tax.setLastAuteur(taxe.getLastAuteur());
//        } else {
//            tax.setLastAuteur(getUserOnLine());
//        }
//        return tax;
//    }
//
//    @Override
//    public void populateView(Taxes bean) {
//        taxe.setCodeTaxe(bean.getCodeTaxe());
//        taxe.setTaux(bean.getTaux());
//        taxe.setActif(bean.isActif());
//        taxe.setAuteur(bean.getAuteur());
//        taxe.setLastAuteur(bean.getLastAuteur());
//        taxe.setDateSave(bean.getDateSave());
//        taxe.setLastDateUpdate(bean.getLastDateUpdate()); 
//        taxe.setId(bean.getId());
//        taxe.setCodeAppel(bean.getCodeAppel());
//    }
// 
//    @Override
//    public void loadOnView(SelectEvent ev) {
//        Taxes tax = (Taxes) ev.getObject();
//        if (tax != null) {
//            populateView(tax);
//            setDisableSave(true);
//        }
//    }
//
//    @Override
//    public void loadAll() {
//        String[] ch = {"societe"};
//        Object[] v = {currentScte};
//        List<YvsTaxes> l = dao.loadNameQueries("YvsTaxes.findAll", ch, v);
//        listTaxe.clear();
//        for(YvsTaxes t:l){
//            listTaxe.add(buildBeanFromEntity(t));
//        }
//    }
//
//    @Override
//    public void resetFiche() {
//        resetFiche(taxe);
//        setDisableSave(false);
//    }
//
//    private YvsTaxes buildEntityFromBean(Taxes tax) {
//        YvsTaxes result = new YvsTaxes();
//        result.setCodeTaxe(tax.getCodeTaxe());
//        result.setActif(tax.isActif());
//        result.setTaux(tax.getTaux());
//        result.setId(tax.getId());
//        result.setCodeAppel(tax.getCodeAppel());
//        try {
//            if (tax.getDateSave() != null) {
//                result.setDateSave(df.parse(tax.getDateSave()));
//            } else {
//                result.setDateSave(new Date());
//            }
//            if (tax.getLastDateUpdate() != null) {
//                result.setLastDateSave(df.parse(tax.getLastDateUpdate()));
//            } else {
//                result.setLastDateSave(new Date());
//            }
//        } catch (ParseException ex) {
//            Logger.getLogger(ManagedTaxes.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (tax.getAuteur() != null) {
//            result.setAuthor(tax.getAuteur());
//        } else {
//            result.setAuthor(getUserOnLine());
//        }
//        if (tax.getLastAuteur() != null) {
//            result.setLastAuthor(tax.getLastAuteur());
//        } else {
//            result.setLastAuthor(getUserOnLine());
//        }
//        return result;
//    }
//
//    private Taxes buildBeanFromEntity(YvsTaxes tax) {
//        Taxes result = new Taxes();
//        result.setCodeTaxe(tax.getCodeTaxe());
//        result.setId(tax.getId());
//        result.setActif(tax.isActif());
//        result.setTaux(tax.getTaux());
//        result.setCodeAppel(tax.getCodeAppel());
//        if (tax.getDateSave() != null) {
//            result.setDateSave(df.format(tax.getDateSave()));
//        } else {
//            result.setDateSave(df.format(new Date()));
//        }
//        if (tax.getLastDateSave() != null) {
//            result.setLastDateUpdate(df.format(tax.getLastDateSave()));
//        } else {
//            result.setLastDateUpdate(df.format(new Date()));
//        }
//        if (tax.getAuthor() != null) {
//            result.setAuteur(tax.getAuthor());
//        } else {
//            result.setAuteur(getUserOnLine());
//        }
//        if (tax.getLastAuthor() != null) {
//            result.setLastAuteur(tax.getLastAuthor());
//        } else {
//            result.setLastAuteur(getUserOnLine());
//        }
//        return result;
//    }
//
//    public void disable() {
//        YvsTaxes tax = buildEntityFromBean(selectTaxe);
//        tax.setLastAuthor(getUserOnLine());
//        tax.setLastDateSave(new Date());
//        tax.setActif(false);
//        selectTaxe.setLastAuteur(getUserOnLine());
//        selectTaxe.setLastDateUpdate(df.format(new Date()));
//        selectTaxe.setActif(false);
//        dao.update(tax);
//        listTaxe.set(listTaxe.indexOf(selectTaxe), selectTaxe);
//        update("tax-table");
//        succes();
//    }
//
//    public void delete() {
//        YvsTaxes tax = buildEntityFromBean(selectTaxe);
//        tax.setLastAuthor(getUserOnLine());
//        tax.setLastDateSave(new Date());
//        tax.setActif(false);
//        tax.setSupp(true);
//        dao.update(tax);
//        listTaxe.remove(selectTaxe);
//        update("tax-table");
//        succes();
//    }
//
//    @Override
//    public void deleteBean() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void unLoadOnView(UnselectEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//}

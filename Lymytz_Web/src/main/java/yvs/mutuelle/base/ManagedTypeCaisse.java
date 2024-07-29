///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.mutuelle.base;
//
//import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
//import java.util.ArrayList;
//import java.util.List;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.SessionScoped;
//import org.primefaces.event.SelectEvent;
//import org.primefaces.event.UnselectEvent;
//import yvs.entity.mutuelle.base.YvsMutTypeCompte;
//import yvs.mutuelle.UtilMut;
//import yvs.util.Managed;
//
///**
// *
// * @author lymytz
// */
//@ManagedBean
//@SessionScoped
//public class ManagedTypeCaisse extends Managed<TypeCaisse, YvsBaseCaisse> implements Serializable {
//
//    @ManagedProperty(value = "#{typeCaisse}")
////    private TypeCaisse typeCaisse;
//    private List<YvsMutTypeCompte> types;
//
//    private String tabIds, input_reset;
//    private boolean updateType;
//
//    public ManagedTypeCaisse() {
//        types = new ArrayList<>();
//    }
//
////    public TypeCaisse getTypeCaisse() {
////        return typeCaisse;
////    }
////
////    public void setTypeCaisse(TypeCaisse typeCaisse) {
////        this.typeCaisse = typeCaisse;
////    }
//
//    public List<YvsMutTypeCompte> getTypes() {
//        return types;
//    }
//
//    public void setTypes(List<YvsMutTypeCompte> types) {
//        this.types = types;
//    }
//
//    public String getTabIds() {
//        return tabIds;
//    }
//
//    public void setTabIds(String tabIds) {
//        this.tabIds = tabIds;
//    }
//
//    public String getInput_reset() {
//        return input_reset;
//    }
//
//    public void setInput_reset(String input_reset) {
//        this.input_reset = input_reset;
//    }
//
//    public boolean isUpdateType() {
//        return updateType;
//    }
//
//    public void setUpdateType(boolean updateType) {
//        this.updateType = updateType;
//    }
//
//    @Override
//    public void loadAll() {
//        
//        loadAllType();
//        tabIds = "";
//    }
//
//    public void loadAllType() {
//        types = dao.loadNameQueries("YvsMutTypeCompte.findByMutuelle", new String[]{"mutuelle", "typeCaisse"}, new Object[]{currentMutuel, true});
//    }
//
//    public YvsMutTypeCompte buildTypeCaisse(TypeCaisse y) {
//        YvsMutTypeCompte t = new YvsMutTypeCompte();
//        if (y != null) {
//            t.setId(y.getId());
//            t.setLibelle(y.getLibelle());
//            t.setNature(y.getNature());
//            t.setTypeCaisse(true);
//            t.setMutuelle(currentMutuel);
//            t.setAuthor(currentUser);
//        }
//        return t;
//    }
//
//    @Override
//    public TypeCaisse recopieView() {
//        TypeCaisse t = new TypeCaisse();
//        t.setId(typeCaisse.getId());
//        t.setLibelle(typeCaisse.getLibelle());
//        t.setNature(typeCaisse.getNature());
//        return t;
//    }
//
//    @Override
//    public boolean controleFiche(TypeCaisse bean) {
//        if (bean.getLibelle() == null || bean.getLibelle().equals("")) {
//            getErrorMessage("Vous devez entrer le libelle");
//            return false;
//        }
//        if (currentMutuel != null ? currentMutuel.getId() < 1 : true) {
//            getErrorMessage("Vous devez etre connecté dans une mutuelle");
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void populateView(TypeCaisse bean) {
//        typeCaisse.setId(bean.getId());
//        typeCaisse.setLibelle(bean.getLibelle());
//        typeCaisse.setNature(bean.getNature());
//        setUpdateType(true);
//    }
//
//    @Override
//    public void resetFiche() {
//        typeCaisse.setId(0);
//        typeCaisse.setLibelle("");
//        typeCaisse.setNature("Salaire");
//        tabIds = "";
//        input_reset = "";
//        setUpdateType(false);
//    }
//
//    @Override
//    public boolean saveNew() {
//        String action = isUpdateType() ? "Modification" : "Insertion";
//        try {
//            TypeCaisse bean = recopieView();
//            bean.setNew_(true);
//            if (controleFiche(bean)) {
//                YvsMutTypeCompte entyPoste = buildTypeCaisse(bean);
//                if (!isUpdateType()) {
//                    entyPoste = (YvsMutTypeCompte) dao.save1(entyPoste);
//                    bean.setId(entyPoste.getId());
//                    typeCaisse.setId(entyPoste.getId());
//                    types.add(0, entyPoste);
//                } else {
//                    dao.update(entyPoste);
//                    types.set(types.indexOf(entyPoste), entyPoste);
//                }
//                succes();
//                resetFiche();
//            }
//        } catch (Exception ex) {
//            getException("Error " + action + " : " + ex.getMessage(), ex);
//            getErrorMessage(action + " Impossible !");
//        }
//        return true;
//    }
//
//    @Override
//    public void deleteBean() {
//        try {
//            if ((tabIds != null) ? tabIds.length() > 0 : false) {
//                String[] ids = tabIds.split("-");
//                if ((ids != null) ? ids.length > 0 : false) {
//                    for (String s : ids) {
//                        long id = Integer.valueOf(s);
//                        YvsMutTypeCompte bean = types.get(types.indexOf(new YvsMutTypeCompte(id)));
//                        dao.delete(new YvsMutTypeCompte(bean.getId()));
//                        types.remove(bean);
//                    }
//                    resetFiche();
//                    succes();
//                    update("data_type_caisse_");
//                    update("blog_form_type_caisse_");
//                }
//            }
//        } catch (NumberFormatException ex) {
//            getErrorMessage("Suppression Impossible !");
//            System.err.println("error suppression gamme article : " + ex.getMessage());
//        }
//    }
//
//    public void deleteBean(YvsMutTypeCompte y) {
//        try {
//            if (y != null) {
//                dao.delete(y);
//                types.remove(y);
//
//                resetFiche();
//                succes();
//                update("data_type_caisse_");
//                update("blog_form_type_caisse_");
//            }
//        } catch (NumberFormatException ex) {
//            getErrorMessage("Suppression Impossible !");
//            System.err.println("error suppression gamme article : " + ex.getMessage());
//        }
//    }
//
//    @Override
//    public void updateBean() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void loadOnView(SelectEvent ev) {
//        if ((ev != null) ? ev.getObject() != null : false) {
//            YvsMutTypeCompte bean = (YvsMutTypeCompte) ev.getObject();
//            populateView(UtilMut.buildBeanTypeCaisse(bean));
//            update("blog_form_type_caisse_");
//        }
//    }
//
//    @Override
//    public void unLoadOnView(UnselectEvent ev) {
//        resetFiche();
//        update("blog_form_type_caisse_");
//    }
//
//}

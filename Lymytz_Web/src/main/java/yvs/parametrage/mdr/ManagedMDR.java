///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.parametrage.mdr;
//
//import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.SessionScoped;
//import org.primefaces.event.SelectEvent;
//import org.primefaces.event.UnselectEvent;
//import yvs.entity.param.YvsModelDeReglement;
//import yvs.entity.param.YvsTraiteMdr;
//import yvs.util.Constantes;
//import yvs.util.Managed;
//
///**
// *
// * @author GOUCHERE YVES
// */
//@ManagedBean(name = "Mmdr")
//@SessionScoped
//public class ManagedMDR extends Managed<ModelDeReglement, YvsBaseCaisse> implements Serializable {
//
//    @ManagedProperty(value = "#{mdr}")
//    private ModelDeReglement mdr;
//    @ManagedProperty(value = "#{traiteMdr}")
//    private TraiteMdr traiteMdr;
//    private List<ModelDeReglement> listMdr;
//    private List<ModelDeReglement> listFiltre;
//    private List<TraiteMdr> listTraite;
//    /**
//     * permet de maintenir en mémoire la liste des traites d'un model de
//     * règlement cette liste sera lors d'une modification comparé à la liste des
//     * traite présente sur la vue afin de déterminer les traites qui ont été
//     * supprimé!
//     */
//    private List<TraiteMdr> listOldTraite;
//    private boolean disBSave = true;
//    private TraiteMdr selectTraite;
//    private ModelDeReglement selectMdr;
//
//    public ManagedMDR() {
//        listMdr = new ArrayList<>();
//        listTraite = new ArrayList<>();
//        listOldTraite = new ArrayList<>();
//    }
//
//    public ModelDeReglement getMdr() {
//        return mdr;
//    }
//
//    public void setMdr(ModelDeReglement mdr) {
//        this.mdr = mdr;
//    }
//
//    public TraiteMdr getTraiteMdr() {
//        return traiteMdr;
//    }
//
//    public List<ModelDeReglement> getListMdr() {
//        return listMdr;
//    }
//
//    public void setListMdr(List<ModelDeReglement> listMdr) {
//        this.listMdr = listMdr;
//    }
//
//    public void setTraiteMdr(TraiteMdr traiteMdr) {
//        this.traiteMdr = traiteMdr;
//    }
//
//    public List<ModelDeReglement> getListFiltre() {
//        return listFiltre;
//    }
//
//    public void setListFiltre(List<ModelDeReglement> listFiltre) {
//        this.listFiltre = listFiltre;
//    }
//
//    public boolean isDisBSave() {
//        return disBSave;
//    }
//
//    public void setDisBSave(boolean disBSave) {
//        this.disBSave = disBSave;
//    }
//
//    public List<TraiteMdr> getListTraite() {
//        return listTraite;
//    }
//
//    public void setListTraite(List<TraiteMdr> listTraite) {
//        this.listTraite = listTraite;
//    }
//
//    public TraiteMdr getSelectTraite() {
//        return selectTraite;
//    }
//
//    public void setSelectTraite(TraiteMdr selectTraite) {
//        this.selectTraite = selectTraite;
//    }
//
//    public ModelDeReglement getSelectMdr() {
//        return selectMdr;
//    }
//
//    public void setSelectMdr(ModelDeReglement selectMdr) {
//        this.selectMdr = selectMdr;
//    }
//
//    @Override
//    public boolean controleFiche(ModelDeReglement bean) {
//        if (bean.getCodeMdr() == null) {
//            getMessage(message.getMessage("codeMdr"), FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
//        return true;
//    }
//    int key = 0;
//
//    @Override
//    public boolean saveNew() {
//        ModelDeReglement md = recopieView();
//        md = saveNewMDR(md);        
//        listMdr.add(0, md);
//        disBSave = true;
//        setDisableSave(false);
//        update("mdr-table");
//        succes();
//        return true;
//    }
//
//    public ModelDeReglement saveNewMDR(ModelDeReglement md) {
//        if (controleFiche(md)) {
//            YvsModelDeReglement model = buildEntityFromBean(md);
//            model.setSociete(currentScte);
//            List<YvsTraiteMdr> ltmdr = new ArrayList<>();
//            model = (YvsModelDeReglement) dao.save1(model);
//            listOldTraite.clear();
//            //persiste les tranches
//            for (TraiteMdr t : md.getListTraite()) {
//                YvsTraiteMdr tmdr = buildTraite(t);
//                tmdr.setMdr(model);
//                ltmdr.add(tmdr);
//                YvsTraiteMdr id = (YvsTraiteMdr) dao.save1(tmdr);
//                //là je change l'id de la ligne sur la vue pour mieux gérer la supression
//                //je remplace l'id généré sur la vue par celui renvoyé par la bd
//                int current = md.getListTraite().indexOf(t);
//                t.setId(id.getId());
//                md.getListTraite().set(current, t);
//                listOldTraite.add(t);
//            }
//            md.setId(model.getId());
////            md.setListTraite(listTraite);
//        }
//        return md;
//    }
//
//    public void addTraite() {
//        TraiteMdr t = new TraiteMdr();
//        if (traiteMdr.getNatureTraite().equals(Constantes.EQUILIBRE)) {
//            traiteMdr.setPourcentage(0);
//        }
//        t.setId(key++);
//        t.setDuree(traiteMdr.getDuree());
//        t.setNatureTraite(traiteMdr.getNatureTraite());
//        t.setPourcentage(traiteMdr.getPourcentage());
//        t.setModeDeReglement(traiteMdr.getModeDeReglement());
//        listTraite.add(0, t);
//        if (isDisableSave()) {
//            disBSave = !controleTraite(listTraite);
//        }
//    }
//
//    @Override
//    public void updateBean() {
//        int id = 0;
//        if (controleTraite(listTraite)) {
//            ModelDeReglement md = recopieView();
//            YvsModelDeReglement model = buildEntityFromBean(md);
//            model.setLastAuthor(getUserOnLine());
//            model.setLastDatSave(new Date());
//            md.setLastAuteur(getUserOnLine());
//            md.setLastDateUpdate(df.format(new Date()));
//            List<YvsTraiteMdr> lt = new ArrayList<>();
////            model = (YvsModelDeReglement) dao.update(model);
//            //modifie également les traites            
//            for (TraiteMdr t : listTraite) {
//                YvsTraiteMdr tr = buildTraite(t);
//                //lorsque je passe directement la référence de model à tr, l'entité disparait du contexte de persistence
//                //par la suite; J'IGNORE POURQUOI?
//                tr.setMdr(model);
//                tr = (YvsTraiteMdr) dao.update(tr);
//                lt.add(tr);
//                t.setId(tr.getId());
//                //raffraichit la liste des traite sur la vue
//                listTraite.set(id, t);
//                //si la traite t se trouve également dans la liste listOldTraite, je la rétire; pour ne laisser dans cette liste 
//                //que les élément qui doivent être supprimé de la base de donnée.
//                if (listOldTraite.contains(t)) {
//                    listOldTraite.remove(t);
//                }
//                id++;
//            }
//            model.setYvsTraiteMdrList(lt);
//            dao.update(model);
//            //là je met à jour les traite du model dans la bd
//            if (!listOldTraite.isEmpty()) {
//                for (TraiteMdr t : listOldTraite) {
//                    YvsTraiteMdr del = buildTraite(t);
//                    del.setMdr(model);
//                    dao.delete(del);
//                }
//            }
//            listMdr.set(listMdr.indexOf(md), md);
//            update("mdr-table");
//            succes();
//        } else {
//            getMessage(message.getMessage("traiteInvalid"), FacesMessage.SEVERITY_ERROR);
//        }
//    }
//
//    @Override
//    public ModelDeReglement recopieView() {
//        ModelDeReglement md = new ModelDeReglement(mdr.getCodeMdr(), mdr.getDesignation());
//        md.setDescription(mdr.getDescription());
//        md.setActif(mdr.isActif());
//        if (mdr.getAuteur() != null) {
//            md.setAuteur(mdr.getAuteur());
//        } else {
//            md.setAuteur(getUserOnLine());
//        }
//        if (mdr.getDateSave() != null) {
//            md.setDateSave(mdr.getDateSave());
//        } else {
//            md.setDateSave(df.format(new Date()));
//        }
//        md.setDescription(mdr.getDescription());
//        md.setId(mdr.getId());
//        if (mdr.getLastAuteur() != null) {
//            md.setLastAuteur(mdr.getLastAuteur());
//        } else {
//            md.setLastAuteur(getUserOnLine());
//        }
//        if (mdr.getLastDateUpdate() != null) {
//            md.setLastDateUpdate(mdr.getLastDateUpdate());
//        } else {
//            md.setLastDateUpdate(df.format(new Date()));
//        }
//        List<TraiteMdr> l = new ArrayList<>();
//        l.addAll(listTraite);
//        md.setListTraite(l);
//        return md;
//    }
//
//    @Override
//    public void populateView(ModelDeReglement bean) {
//        mdr.setCodeMdr(bean.getCodeMdr());
//        mdr.setActif(bean.isActif());
//        mdr.setDescription(bean.getDescription());
//        mdr.setDesignation(bean.getDesignation());
//        mdr.setListTraite(bean.getListTraite());
//        mdr.setId(bean.getId());
//        mdr.setAuteur(bean.getAuteur());
//        mdr.setLastAuteur(bean.getLastAuteur());
//        mdr.setDateSave(bean.getDateSave());
//        mdr.setLastDateUpdate(bean.getLastDateUpdate());
//        listTraite.clear();
//        listOldTraite.clear();
//        listTraite.addAll(bean.getListTraite());
//        listOldTraite.addAll(bean.getListTraite());
//        // les deux liste possède les mêmes élément mais ont des référence differentes.
//    }
//
//    @Override
//    public void loadOnView(SelectEvent ev) {
//        ModelDeReglement md = (ModelDeReglement) ev.getObject();
//        populateView(md);
//        setDisableSave(false);
//    }
//
//    @Override
//    public void loadAll() {
//        String[] ch = {"societe"};
//        Object[] v = {currentScte};
//        listMdr.clear();
//        List<YvsModelDeReglement> l = dao.loadNameQueries("YvsModelDeReglement.findAll", ch, v);
//        for (YvsModelDeReglement md : l) {
//            listMdr.add(buildBeanFromEntity(md));
//        }
//    }
//
//    public void loadActif() {
//        String[] ch = {};
//        Object[] v = {};
//        listMdr.clear();
//        List<YvsModelDeReglement> l = dao.loadNameQueries("YvsModelDeReglement.findByActif", ch, v);
//        for (YvsModelDeReglement md : l) {
//            listMdr.add(buildBeanFromEntity(md));
//        }
//    }
//
//    @Override
//    public void resetFiche() {
//        resetFiche(mdr);
//        resetFiche(traiteMdr);
//        listTraite.clear();
//        disBSave = true;
//        setDisableSave(true);
//        eq = false;
//        percentOk = false;
//    }
//
//    public boolean controleTraite(List<TraiteMdr> l) {
//        int n = countEquilibre(l);
//        if (n > 1) {
//            return false;
//        } else if (eq) {
//            return false;
//        } else if (!percentOk && n != 1) {
//            return false;
//        }
//        return true;
//    }
//    private boolean eq = false; //précise si la liste des traites pourcentage est supérieure à 100%
//    private boolean percentOk = false;
//
//    private int countEquilibre(List<TraiteMdr> l) {
//        int n = 0;
//        double percent = 0;
//        for (TraiteMdr t : l) {
//            if (t.getNatureTraite().equals(Constantes.EQUILIBRE)) {
//                n++;
//                if (n > 1) {
//                    return n;
//                }
//            } else {
//                percent += t.getPourcentage();
//                if (percent > 100) {
//                    eq = true;
//                } else {
//                    eq = false;
//                }
//            }
//        }
//        if (percent == 100) {
//            percentOk = true;
//        }
//        return n;
//    }
//
//    public void removeTraite() {
//        if (selectTraite != null) {
//            listTraite.remove(selectTraite);
//            if (isDisableSave()) {
//                disBSave = !controleTraite(listTraite);
//            }
//        }
//    }
//
//    private ModelDeReglement buildBeanFromEntity(YvsModelDeReglement model) {
//        ModelDeReglement result = new ModelDeReglement();
//        result.setActif(model.getActif());
//        result.setAuteur(model.getAuthor());
//        result.setDesignation(model.getDesignation());
//        result.setDescription(model.getDescription());
//        if (model.getDateSave() != null) {
//            result.setDateSave(df.format(model.getDateSave()));
//        }
//        if (model.getLastDatSave() != null) {
//            result.setLastDateUpdate(df.format(model.getLastDatSave()));
//        }
//        result.setId(model.getId());
//        result.setLastAuteur(model.getLastAuthor());
//        result.getListTraite().clear();
//        for (YvsTraiteMdr t : model.getYvsTraiteMdrList()) {
//            System.out.println(t.getMdr().getDesignation() + "- Traite -" + t.getId());
//            result.getListTraite().add(buildTraite(t));
//        }
//        return result;
//    }
//
//    private TraiteMdr buildTraite(YvsTraiteMdr tranche) {
//        TraiteMdr traite = new TraiteMdr();
//        traite.setDuree(tranche.getDuree());
//        traite.setId(tranche.getId());
//        traite.setModeDeReglement(tranche.getModeDeReglement());
//        traite.setNatureTraite(tranche.getNature());
//        traite.setPourcentage(tranche.getPourcentage());
//        return traite;
//    }
//
//    private YvsModelDeReglement buildEntityFromBean(ModelDeReglement mdr) {
//        YvsModelDeReglement result = new YvsModelDeReglement();
//        result.setActif(mdr.isActif());
//        result.setDesignation(mdr.getDesignation());
//        result.setDescription(mdr.getDescription());
//        if (mdr.getAuteur() != null) {
//            result.setAuthor(mdr.getAuteur());
//        } else {
//            result.setAuthor(getUserOnLine());
//        }
//        if (mdr.getLastAuteur() != null) {
//            result.setLastAuthor(mdr.getLastAuteur());
//        } else {
//            result.setLastAuthor(getUserOnLine());
//        }
//        try {
//            if (mdr.getDateSave() != null) {
//                result.setDateSave(df.parse(mdr.getDateSave()));
//            } else {
//                result.setDateSave(new Date());
//            }
//            if (mdr.getLastDateUpdate() != null) {
//                result.setLastDatSave(df.parse(mdr.getLastDateUpdate()));
//            } else {
//                result.setLastDatSave(new Date());
//            }
//        } catch (ParseException ex) {
//            ex.printStackTrace();
//        }        
//        result.setId(mdr.getId());
//        return result;
//    }
//
//    private YvsTraiteMdr buildTraite(TraiteMdr tranche) {
//        YvsTraiteMdr traite = new YvsTraiteMdr();
//        traite.setId(tranche.getId());
//        traite.setDuree(tranche.getDuree());
//        traite.setModeDeReglement(tranche.getModeDeReglement());
//        traite.setNature(tranche.getNatureTraite());
//        traite.setPourcentage(tranche.getPourcentage());
//        return traite;
//    }
//
//    public void disable() {
//        YvsModelDeReglement md = buildEntityFromBean(selectMdr);
//        md.setLastAuthor(getUserOnLine());
//        md.setLastDatSave(new Date());
//        md.setActif(false);
//        selectMdr.setLastAuteur(getUserOnLine());
//        selectMdr.setLastDateUpdate(df.format(new Date()));
//        selectMdr.setActif(false);
//        dao.update(md);
//        listMdr.set(listMdr.indexOf(selectMdr), selectMdr);
//        update("mdr-table");
//        succes();
//    }
//
//    public void delete() {
//        YvsModelDeReglement md = buildEntityFromBean(selectMdr);
//        md.setLastAuthor(getUserOnLine());
//        md.setLastDatSave(new Date());
//        md.setActif(false);
//        md.setSupp(true);
//        dao.update(md);
//        listMdr.remove(selectMdr);
//        update("mdr-table");
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

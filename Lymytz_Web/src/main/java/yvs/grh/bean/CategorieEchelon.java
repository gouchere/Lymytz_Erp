/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.param.YvsGrhCategorieProfessionelle;
import yvs.entity.grh.param.YvsGrhConventionCollective;
import yvs.entity.grh.param.YvsGrhEchelons;
import yvs.entity.grh.param.YvsGrhCategoriePreavis;
import yvs.entity.grh.param.YvsGrhIntervalleAnciennete;
import yvs.entity.param.YvsGrhSecteurs;
import yvs.entity.param.YvsSocietes;
import yvs.parametrage.agence.SecteurActivite;
import yvs.parametrage.societe.Societe;
import yvs.util.Managed;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class CategorieEchelon extends Managed<CategorieEchelon, YvsBaseCaisse> implements Serializable {

    private int categories;
    private int echelon;
    private double salaireHoraire;
    private double salaireMensuel;

    private List<Object[]> listValue;
    Object[] tab = new Object[13];

    private List<String> columns1;
    private List<String> columns2;
    private List<String> columns;
    private List<YvsGrhSecteurs> listSecteurs;
    private List<YvsGrhCategorieProfessionelle> listCategorie;
    private List<Categories> listCategorie1;
    private List<YvsGrhEchelons> listEchelon, selectionEchelons;
    private YvsGrhSecteurs selectedSect;
    private boolean selectConvention, updateConvention;

    private Categories newCategorie = new Categories();
    private Echelons newEchelons = new Echelons();

    private Societe societe = new Societe();
    private List<YvsSocietes> societes;
    private SecteurActivite secteur = new SecteurActivite();

    private String chainePreavis;
    private String chaineSelectCategorie;
    private int indexIntervalle;

    public CategorieEchelon(int categories, int echelon) {
        this.categories = categories;
        this.echelon = echelon;
        societes = new ArrayList<>();
        listeDureePreavis = new ArrayList<>();
    }

    public CategorieEchelon() {
        listValue = new ArrayList<>();
        columns1 = new ArrayList<>();
        columns2 = new ArrayList<>();
        columns = new ArrayList<>();
        listEchelon = new ArrayList<>();
        listSecteurs = new ArrayList<>();
        listCategorie = new ArrayList<>();
        listCategorie1 = new ArrayList<>();
        societes = new ArrayList<>();
        listeDureePreavis = new ArrayList<>();
    }

    public SecteurActivite getSecteur() {
        return secteur;
    }

    public void setSecteur(SecteurActivite secteur) {
        this.secteur = secteur;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public List<YvsSocietes> getSocietes() {
        return societes;
    }

    public void setSocietes(List<YvsSocietes> societes) {
        this.societes = societes;
    }

    public void setSelectConvention(boolean selectConvention) {
        this.selectConvention = selectConvention;
    }

    public void setUpdateConvention(boolean updateConvention) {
        this.updateConvention = updateConvention;
    }

    public boolean isSelectConvention() {
        return selectConvention;
    }

    public boolean isUpdateConvention() {
        return updateConvention;
    }

    public double getSalaireHoraire() {
        return salaireHoraire;
    }

    public void setSalaireHoraire(double salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    public double getSalaireMensuel() {
        return salaireMensuel;
    }

    public void setSalaireMensuel(double salaireMensuel) {
        this.salaireMensuel = salaireMensuel;
    }

    public List<Object[]> getListValue() {
        return listValue;
    }

    public void setListValue(List<Object[]> listValue) {
        this.listValue = listValue;
    }

    public List<String> getColumns1() {
        return columns1;
    }

    public void setColumns1(List<String> columns1) {
        this.columns1 = columns1;
    }

    public List<String> getColumns2() {
        return columns2;
    }

    public void setColumns2(List<String> columns2) {
        this.columns2 = columns2;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<YvsGrhSecteurs> getListSecteurs() {
        return listSecteurs;
    }

    public void setListSecteurs(List<YvsGrhSecteurs> listSecteurs) {
        this.listSecteurs = listSecteurs;
    }

    public List<YvsGrhCategorieProfessionelle> getListCategorie() {
        return listCategorie;
    }

    public void setListCategorie(List<YvsGrhCategorieProfessionelle> listCategorie) {
        this.listCategorie = listCategorie;
    }

    public int getCategories() {
        return categories;
    }

    public void setCategories(int categories) {
        this.categories = categories;
    }

    public int getEchelon() {
        return echelon;
    }

    public void setEchelon(int echelon) {
        this.echelon = echelon;
    }

    public List<YvsGrhEchelons> getListEchelon() {
        return listEchelon;
    }

    public void setListEchelon(List<YvsGrhEchelons> listEchelon) {
        this.listEchelon = listEchelon;
    }

    public YvsGrhSecteurs getSelectedSect() {
        return selectedSect;
    }

    public void setSelectedSect(YvsGrhSecteurs selectedSect) {
        this.selectedSect = selectedSect;
    }

    public Categories getNewCategorie() {
        return newCategorie;
    }

    public void setNewCategorie(Categories newCategorie) {
        this.newCategorie = newCategorie;
    }

    public Echelons getNewEchelons() {
        return newEchelons;
    }

    public void setNewEchelons(Echelons newEchelons) {
        this.newEchelons = newEchelons;
    }

    public String getChaineSelectCategorie() {
        return chaineSelectCategorie;
    }

    public void setChaineSelectCategorie(String chaineSelectCategorie) {
        this.chaineSelectCategorie = chaineSelectCategorie;
    }

    public String getChainePreavis() {
        return chainePreavis;
    }

    public void setChainePreavis(String chainePreavis) {
        this.chainePreavis = chainePreavis;
    }

    public int getIndexIntervalle() {
        return indexIntervalle;
    }

    public void setIndexIntervalle(int indexIntervalle) {
        this.indexIntervalle = indexIntervalle;
    }

    public List<YvsGrhEchelons> getSelectionEchelons() {
        return selectionEchelons;
    }

    public void setSelectionEchelons(List<YvsGrhEchelons> selectionEchelons) {
        this.selectionEchelons = selectionEchelons;
    }

    public void selectSecteur(SelectEvent ev) {
        selectedSect = (YvsGrhSecteurs) ev.getObject();
        YvsGrhConventionCollective cc;
        if (selectedSect != null) {
//            champ = new String[]{"societe"};
//            val = new Object[]{currentAgence.getSociete()};
//            listCategorie = dao.loadNameQueries("YvsCategorieProfessionelle.findAllActif", champ, val);
            for (YvsGrhCategorieProfessionelle cat : listCategorie) {
                cat.setListEchelons(new ArrayList<YvsGrhConventionCollective>());
                for (YvsGrhEchelons ec : listEchelon) {
                    //cherche la convention qui correspond: s'il y en a pas mette 0
                    champ = new String[]{"secteur", "categorie", "echelon"};
                    val = new Object[]{selectedSect, cat, ec};
                    cc = (YvsGrhConventionCollective) dao.loadOneByNameQueries("YvsConventionCollective.findByCE", champ, val);
                    if (cc != null) {
                        cat.getListEchelons().add(cc);
                    } else {
                        cc = new YvsGrhConventionCollective(-1L, 0d, 0d);
                        cat.getListEchelons().add(cc);
                    }
                }
            }
        }
    }

    public void addConvention() {
        if (selectedSect != null) {
            if (categories > 0 && echelon > 0) {
                YvsGrhCategorieProfessionelle cat = listCategorie.get(listCategorie.indexOf(new YvsGrhCategorieProfessionelle(categories)));
                if (entityConvention != null) {
                    entityConvention.setSalaireHoraireMin(salaireHoraire);
                    entityConvention.setSalaireMin(salaireMensuel);
                    entityConvention.setAuthor(currentUser);
                    entityConvention.setDateUpdate(new Date());
                    dao.update(entityConvention);
                    cat.getListEchelons().set(cat.getListEchelons().indexOf(entityConvention), entityConvention);
                } else {
                    YvsGrhConventionCollective convention = new YvsGrhConventionCollective();
                    convention.setEchelon(listEchelon.get(listEchelon.indexOf(new YvsGrhEchelons(echelon))));
                    convention.setCategorie(listCategorie.get(listCategorie.indexOf(new YvsGrhCategorieProfessionelle(categories))));
                    convention.setYvsSecteurs(selectedSect);
                    convention.setSalaireMin(salaireMensuel);
                    convention.setSalaireHoraireMin(salaireHoraire);
                    convention.setActif(true);
                    convention.setAuthor(currentUser);
                    if (!cat.getListEchelons().contains(ech)) {
                        convention.setDateSave(new Date());
                        convention.setDateUpdate(new Date());
                        convention = (YvsGrhConventionCollective) dao.save1(convention);
                        cat.getListEchelons().add(convention);
                        listCategorie.set(listCategorie.indexOf(cat), cat);
                        succes();
                    }
                }
            }
        } else {
            getMessage("Vous devez d'abord selectionner un secteur d'activité", FacesMessage.SEVERITY_ERROR);
        }
    }

    private boolean containsEchelon(List<YvsGrhConventionCollective> l, int ech) {
        for (YvsGrhConventionCollective c : l) {
            if (c.getEchelon().getId() == ech) {
                return true;
            }
        }
        return false;
    }

    YvsGrhCategorieProfessionelle c;
    YvsGrhConventionCollective ech = new YvsGrhConventionCollective();
    YvsGrhConventionCollective conv;

    //si on enregistre une convention déjà existante avec des montant de salaire différent
    public void confirmUpdateConvention(YvsGrhCategorieProfessionelle cat, YvsGrhEchelons eche, YvsGrhConventionCollective cc) {
        if (cc.getSalaireHoraireMin() != salaireHoraire || cc.getSalaireMin() != salaireMensuel) {
            c = cat;
            //ouvre la boîte de dailogue modif
            openDialog("dlgUpdateConv");
        }
    }

    public void majConvention() {
        entityConvention.setSalaireHoraireMin(salaireHoraire);
        entityConvention.setSalaireMin(salaireMensuel);
//        c.getListEchelons().set(c.getListEchelons().indexOf(conv), conv);
        dao.update(entityConvention);
        listCategorie.get(listCategorie.indexOf(entityConvention.getCategorie())).getListEchelons().set(listCategorie.get(listCategorie.indexOf(entityConvention.getCategorie())).getListEchelons().indexOf(entityConvention), entityConvention);

        closeDialog("dlgUpdateConv");
        update("tabConvention:tabCEch");
    }

    @Override
    public boolean controleFiche(CategorieEchelon bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CategorieEchelon recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadAll() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listSecteurs = dao.loadNameQueries("YvsSecteurs.findAll", champ, val);
        listCategorie = dao.loadNameQueries("YvsCategorieProfessionelle.findAllActif", champ, val);
//        for (YvsGrhCategorieProfessionelle d : listCategorie) {
//            //construit la chaine des préavis rattaché à la catégorie
//            chainePreavis = "";
//            for (YvsGrhCategoriePreavis cp : d.getListDureePreavis()) {
//                chainePreavis += "[" + cp.getAnciennete().getAncienneteMin() + " - " + cp.getAnciennete().getAncienneteMax() + " ans] "
//                        + " (" + cp.getPreavis() + " " + cp.getUnitePreavis() + ") ; ";
//            }
//            d.setChainePreavis(chainePreavis);
//        }
        listEchelon = dao.loadNameQueries("YvsEchelons.findAll", champ, val);
        //charge la liste des intervalles d'ancienneté
        listeDureePreavis = dao.loadNameQueries("YvsGrhIntervalleAnciennete.findAll", champ, val);
        echelon = categories = 0;
    }

    public void loadAllSociete() {
        societes = dao.loadNameQueries("YvsSocietes.findAll", new String[]{}, new Object[]{});
        update("data_societe");
    }

//    private Secteurs buildSecteur(YvsGrhSecteurs s) {
//        Secteurs sect = new Secteurs();
//        sect.setIdSecteur(s.getId());
//        sect.setSecteur(s.getNom());
//        sect.setDescription(s.getDescription());
//        return sect;
//    }
//    private List<Secteurs> buildBeanListSecteur(List<YvsGrhSecteurs> l) {
//        List<Secteurs> r = new ArrayList<>();
//        if (l != null) {
//            for (YvsGrhSecteurs s : l) {
//                r.add(buildSecteur(s));
//            }
//        }
//        return r;
//    }
//
//    private Echelons buildEchelon(YvsGrhEchelons e) {
//        Echelons ech_ = new Echelons();
//        ech_.setId(e.getId());
//        ech_.setTitre(e.getEchelon());
//        ech_.setEchelon(e.getEchelon());
//        return ech_;
//    }
//    private Echelons buildEchelon(YvsGrhConventionCollective cvc) {
//        Echelons eche = buildEchelon(cvc.getEchelon());
//        eche.setSalaireH(cvc.getSalaireHoraireMin());
//        eche.setSalaireM(cvc.getSalaireMin());
//        return eche;
//    }
//
//    private Categories buildCategorie(YvsGrhCategorieProfessionelle c) {
//        Categories cat = new Categories();
//        cat.setId(c.getId());
//        cat.setCategorie(c.getCategorie());
//        return cat;
//    }
    YvsGrhConventionCollective entityConvention;

    public void choixCategorie(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            categories = (int) ev.getNewValue();
            if (echelon > 0 && selectedSect != null && categories > 0) {
                //charge les salaire de base min et max
                champ = new String[]{"secteur", "categorie", "echelon"};
                val = new Object[]{selectedSect, new YvsGrhCategorieProfessionelle(categories), new YvsGrhEchelons(echelon)};
                entityConvention = (YvsGrhConventionCollective) dao.loadOneByNameQueries("YvsConventionCollective.findByCE", champ, val);
                if (entityConvention != null) {
                    salaireHoraire = entityConvention.getSalaireHoraireMin();
                    salaireMensuel = entityConvention.getSalaireMin();
                } else {
                    salaireHoraire = salaireMensuel = 0;
                }
                update("gridSalaire");
            } else if (categories == -1) {
                if (!listCategorie.isEmpty()) {
                    newCategorie.setDegre(listCategorie.get(listCategorie.size() - 1).getDegre() + 1);
                }
                openDialog("dlgAddCat");
                update("chp-deg-cat");
            }
        }
    }

    public void choixEchelon(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            echelon = (int) ev.getNewValue();
            if (categories > 0 && selectedSect != null && echelon > 0) {
                //charge les salaire de base min et max
                champ = new String[]{"secteur", "categorie", "echelon"};
                val = new Object[]{selectedSect, new YvsGrhCategorieProfessionelle(categories), new YvsGrhEchelons(echelon)};
                entityConvention = (YvsGrhConventionCollective) dao.loadOneByNameQueries("YvsConventionCollective.findByCE", champ, val);
                if (entityConvention != null) {
                    salaireHoraire = entityConvention.getSalaireHoraireMin();
                    salaireMensuel = entityConvention.getSalaireMin();
                } else {
                    salaireHoraire = salaireMensuel = 0;
                }
                update("gridSalaire");
            } else if (echelon == -1) {
                if (!listEchelon.isEmpty()) {
                    newEchelons.setDegre(listEchelon.get(listEchelon.size() - 1).getDegre() + 1);
                }
                openDialog("dlgAddEche");
                update("chp-deg-ech");
            }
        }
    }

    public void saveNewCategorie() {
        if (newCategorie.getCategorie() != null && newCategorie.getDegre() != 0) {
            YvsGrhCategorieProfessionelle c_ = new YvsGrhCategorieProfessionelle();
            c_.setActif(true);
            c_.setCategorie(newCategorie.getCategorie());
            c_.setDegre(newCategorie.getDegre());
            c_.setSociete(currentAgence.getSociete());
            c_.setSupp(false);
            c_ = (YvsGrhCategorieProfessionelle) dao.save1(c_);
            newCategorie.setId(c_.getId());
            listCategorie.add(c_);
            listCategorie1.add(newCategorie);
            categories = c_.getId();
            newCategorie = new Categories();
            update("chp-select-cat");
            closeDialog("dlgAddCat");
            update("tabConvention");
        }
    }

    public void saveNewEchelons() {
        if (newEchelons.getEchelon() != null && newEchelons.getDegre() != 0) {
            YvsGrhEchelons e = new YvsGrhEchelons();
            e.setActif(true);
            e.setEchelon(newEchelons.getEchelon());
            e.setDegre(newEchelons.getDegre());
            e.setSociete(currentAgence.getSociete());
            e.setAuthor(currentUser);
            e.setSupp(false);
            e = (YvsGrhEchelons) dao.save1(e);
            listEchelon.add(e);
            echelon = e.getId();
            update("chp-select-ech");
            closeDialog("dlgAddEche");
        }
    }

    @Override
    public void deleteBean() {
        if (cnv != null) {
            try {
                dao.delete(cnv);
                succes();
            } catch (Exception e) {
                getErrorMessage("Impossible de supprimer cette convention! ");
            }
        }
    }
    YvsGrhConventionCollective cnv;

    @Override
    public void populateView(CategorieEchelon bean) {
//        champ = new String[]{"categorie", "echelon", "secteur"};
//        val = new Object[]{bean.getCategories(), bean.getEchelon(), selectedSect.getIdSecteur()};
//        cnv = (YvsGrhConventionCollective) dao.loadOneByNameQueries("YvsConventionCollective.findByCE", champ, val);
//        if (cnv != null) {
//            this.salaireHoraire = cnv.getSalaireHoraireMin();
//            this.salaireMensuel = cnv.getSalaireMin();
//        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
//        if (ev != null) {
//            Echelons ec = (Echelons) ev.getObject();
//            echelon = ec.getId();
//            populateView(new CategorieEchelon(categories, echelon));
//            setSelectConvention(true);
//            setUpdateConvention(true);
////            update("panel_convention_detail_00");
//        }
    }

    public void loadOnViewCategorie(SelectEvent ev) {
        if (ev != null) {
            Categories bean = (Categories) ev.getObject();
            categories = bean.getId();
        }
    }

    public void chooseCategorie(Categories bean) {
        if (bean != null) {
            categories = bean.getId();
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.categories;
        hash = 89 * hash + this.echelon;
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
        final CategorieEchelon other = (CategorieEchelon) obj;
        if (this.categories != other.categories) {
            return false;
        }
        if (this.echelon != other.echelon) {
            return false;
        }
        return true;
    }

    public YvsGrhSecteurs buildSecteur(Secteurs s) {
        YvsGrhSecteurs r = new YvsGrhSecteurs();
        if (s != null) {
            r.setId(s.getIdSecteur());
            r.setDescription(s.getDescription());
            r.setNom(s.getSecteur());
            r.setSociete(currentAgence.getSociete());
        }
        return r;
    }

    public void loadOnViewSecteur(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsGrhSecteurs bean = (YvsGrhSecteurs) ev.getObject();
            if (getImporteConvention(bean)) {
                update("tabSecteur");
                update("tabConvention");
                succes();
            }
        }
    }

    public boolean getImporteConvention(YvsGrhSecteurs entity) {
        try {
            if ((currentAgence.getSecteurActivite() != null) ? currentAgence.getSecteurActivite().getId() < 1 : true) {
                entity.setId(null);
                entity = (YvsGrhSecteurs) dao.save1(entity);
                currentAgence.setSecteurActivite(entity);
                dao.update(currentAgence);
                listSecteurs.add(entity);
            } else {
                entity = currentAgence.getSecteurActivite();
            }
            List<YvsGrhConventionCollective> l = dao.loadNameQueries("YvsConventionCollective.findBySecteur", new String[]{"secteur"}, new Object[]{entity});
            if ((l != null) ? !l.isEmpty() : false) {
                listCategorie1.clear();
                listCategorie.clear();
                listEchelon.clear();

                for (YvsGrhConventionCollective con : l) {
                    YvsGrhCategorieProfessionelle c_ = con.getCategorie();
                    String[] ch = new String[]{"categorie", "societe"};
                    Object[] v = new Object[]{c_.getCategorie(), currentAgence.getSociete()};
                    YvsGrhCategorieProfessionelle cat = (YvsGrhCategorieProfessionelle) dao.loadOneByNameQueries("YvsCategorieProfessionelle.findByCategorieSociete", ch, v);
                    if ((cat != null) ? cat.getId() < 1 : true) {
                        c_.setId(null);
                        c_.setSociete(currentAgence.getSociete());
                        c_.getListEchelons().clear();
                        cat = (YvsGrhCategorieProfessionelle) dao.save1(c_);
                    }

//                    Categories cco = buildCategorie(cat);
//                    if (!listCategorie1.contains(cat)) {
//                        listCategorie1.add(cco);
//                    }
                    YvsGrhEchelons e_ = con.getEchelon();
                    ch = new String[]{"echelon", "societe"};
                    v = new Object[]{e_.getEchelon(), currentAgence.getSociete()};
                    YvsGrhEchelons ech_ = (YvsGrhEchelons) dao.loadOneByNameQueries("YvsEchelons.findByEchelonSociete", ch, v);
                    if ((ech_ != null) ? ech_.getId() < 1 : true) {
                        e_.setId(null);
                        e_.setSociete(currentAgence.getSociete());
                        e_ = (YvsGrhEchelons) dao.save1(e_);
                    } else {
                        e_ = ech_;
                    }
                    listEchelon.add(e_);

                    con.setId(null);
                    con.setCategorie(cat);
                    con.setEchelon(e_);
                    con.setYvsSecteurs(entity);
                    con = (YvsGrhConventionCollective) dao.save1(con);
                    if (!listCategorie.contains(cat)) {
                        cat.getListEchelons().add(con);
                        listCategorie.add(cat);
                    } else {
                        YvsGrhCategorieProfessionelle cc1 = listCategorie.get(listCategorie.indexOf(cat));
                        cc1.getListEchelons().add(con);
                        listCategorie.set(listCategorie.indexOf(cat), cc1);
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    /**
     * *GERER LES PREAVIS**********
     */
    private IntervalleAnciennete durePreavis = new IntervalleAnciennete();
    private boolean updateIntervallePreavis;
    private List<YvsGrhIntervalleAnciennete> listeDureePreavis;

    public IntervalleAnciennete getDurePreavis() {
        return durePreavis;
    }

    public void setDurePreavis(IntervalleAnciennete durePreavis) {
        this.durePreavis = durePreavis;
    }

    public boolean isUpdateIntervallePreavis() {
        return updateIntervallePreavis;
    }

    public void setUpdateIntervallePreavis(boolean updateIntervallePreavis) {
        this.updateIntervallePreavis = updateIntervallePreavis;
    }

    public List<YvsGrhIntervalleAnciennete> getListeDureePreavis() {
        return listeDureePreavis;
    }

    public void setListeDureePreavis(List<YvsGrhIntervalleAnciennete> listeDureePreavis) {
        this.listeDureePreavis = listeDureePreavis;
    }

    public void createIntervalleAnciennete() {
        if (durePreavis.getAncienneteMax() > 0 && durePreavis.getAncienneteMin() >= 0) {
//            YvsGrhIntervalleAnciennete an = (durePreavis);
//            an.setSociete(currentAgence.getSociete());
//            if (!updateIntervallePreavis) {
//                an.setAuthor(currentUser);
//                an = (YvsGrhIntervalleAnciennete) dao.save1(an);
//                durePreavis.setId(an.getId());
//                listeDureePreavis.add(durePreavis);
//            } else {
//                an.setAuthor(currentUser);
//                an.setId(durePreavis.getId());
//                dao.update(an);
//                listeDureePreavis.set(listeDureePreavis.indexOf(durePreavis), durePreavis);
//            }
//            updateIntervallePreavis = false;
//            durePreavis = new YvsGrhIntervalleAnciennete();
        } else {
            getErrorMessage("Erreur lors de l'enregistrement! votre formulaire est incorrecte");
        }
    }

    //modifie l'intervale d'anciènneté
    public void openIntervalleToUpdate(YvsGrhIntervalleAnciennete i) {
        if (i != null) {
            updateIntervallePreavis = true;
//            durePreavis = i;
        }
    }

    public void deleteIntervallePreavis(IntervalleAnciennete i) {
        if (i != null) {
            try {
                dao.delete(new YvsGrhIntervalleAnciennete(i.getId()));
                listeDureePreavis.remove(i);
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cet élément !");
                Logger.getLogger(Categories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private YvsGrhIntervalleAnciennete buildEntityIntervalle(IntervalleAnciennete i) {
        YvsGrhIntervalleAnciennete r = new YvsGrhIntervalleAnciennete();
        r.setAncienneteMax(i.getAncienneteMax());
        r.setAncienneteMin(i.getAncienneteMin());
        return r;
    }

    public void createPreavis() {
        List<Integer> categories_ = giveCategorieSelectione();
        if (!categories_.isEmpty() && indexIntervalle >= 0) {
            for (Integer cat : categories_) {
                //Créer l'intervalle
                if (!listCategorie.get(cat).getListDureePreavis().contains(listeDureePreavis.get(indexIntervalle))) {
                    YvsGrhCategoriePreavis catP = new YvsGrhCategoriePreavis();
                    catP.setAnciennete(new YvsGrhIntervalleAnciennete(listeDureePreavis.get(indexIntervalle).getId()));
                    catP.setCategorie(new YvsGrhCategorieProfessionelle(listCategorie.get(cat).getId()));
//                    catP.setPreavis(durePreavis.ge);
//                    catP.setUnitePreavis(durePreavis.getUniteJour());
//                    dao.save(catP);
//                    chainePreavis = listCategorie.get(cat).getChainePreavis();
//                    chainePreavis = (chainePreavis == null) ? "" : chainePreavis;
//                    chainePreavis += "[" + listeDureePreavis.get(indexIntervalle).getAncienneteMin() + " - " + listeDureePreavis.get(indexIntervalle).getAncienneteMax() + " ans] "
//                            + " (" + durePreavis.getDureePreavie() + " " + durePreavis.getUniteJour() + ")  ;";
//                    listCategorie.get(cat).setChainePreavis(chainePreavis);
//                    listCategorie.get(cat).getListDureePreavis().add(listeDureePreavis.get(indexIntervalle));
                }
            }
        } else {
            getErrorMessage("Erreur lors de l'enregistrement! votre formulaire est incomplet");
        }
    }

    private List<Integer> giveCategorieSelectione() {
        List<Integer> l = new ArrayList<>();
        if (chaineSelectCategorie != null) {
            String numroLine[] = chaineSelectCategorie.split("-");
            try {
                int index;
                for (String numroLine1 : numroLine) {
                    index = Integer.parseInt(numroLine1);
                    l.add(index);
                }
                chaineSelectCategorie = "";
                succes();
            } catch (NumberFormatException ex) {
                chaineSelectCategorie = "";
                for (String numroLine1 : numroLine) {
                    if (!l.contains(numroLine1)) {
                        chaineSelectCategorie += numroLine1 + "-";
                    }
                }
                getErrorMessage("Impossible de terminer cette opération !");
            }
        }
        return l;
    }

    public void deleteBean(Echelons e) {
        try {
            dao.delete(buildEntityByBean(e));
            listValue.remove(e);
            update("tab-echelon");
        } catch (Exception ex) {
            getMessage("Impossible de terminer cette action! La catégorie est certainement utilisé par d'autres ressource", FacesMessage.SEVERITY_ERROR);
        }
    }

    private YvsGrhEchelons buildEntityByBean(Echelons c) {
        YvsGrhEchelons cat = new YvsGrhEchelons();
        cat.setEchelon(c.getTitre());
        cat.setId(c.getId());
        cat.setDegre(c.getDegre());
        cat.setActif(true);
        cat.setSupp(false);
        cat.setSociete(currentAgence.getSociete());
        cat.setAuthor(currentUser);
        return cat;
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

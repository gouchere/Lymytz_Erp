/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.salaire.YvsGrhElementSalaire;
import yvs.entity.grh.salaire.YvsGrhElementStructure;
import yvs.entity.grh.salaire.YvsGrhStructureSalaire;
import yvs.grh.UtilGrh;
import yvs.util.Managed;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class ManagedStructureSalaire extends Managed<StructureElementSalaire, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{structureElementSalaire}")
    private StructureElementSalaire structure;
    private List<YvsGrhStructureSalaire> listeStructure, selectionStructure;
    private YvsGrhStructureSalaire selectedStructure;
    private List<YvsGrhElementSalaire> regles, selectionRegles, filterElement, filterElement1;
//    private List<ElementSalaire> selectedElement;
    private boolean displayGrille = true, displayButonOption;
    private boolean update; //contrôle la modification de l'élément.
    private boolean filter; //permet de filtrer la tableau sur les règles attribuées ou non
    private String labelButon = "Nouveau", labelBtnAttribuer = "Ajouter";
    private String textFind;
    private String filtre;
    private String chaineSelectStructure;
    private String textSearchRegleAttribuer, searchRegle;

    public ManagedStructureSalaire() {
        regles = new ArrayList<>();
        listeStructure = new ArrayList<>();
        selectionStructure = new ArrayList<>();
    }

    public List<YvsGrhElementSalaire> getFilterElement() {
        return filterElement;
    }

    public void setFilterElement(List<YvsGrhElementSalaire> filterElement) {
        this.filterElement = filterElement;
    }

    public List<YvsGrhElementSalaire> getFilterElement1() {
        return filterElement1;
    }

    public void setFilterElement1(List<YvsGrhElementSalaire> filterElement1) {
        this.filterElement1 = filterElement1;
    }

    public List<YvsGrhElementSalaire> getSelectionRegles() {
        return selectionRegles;
    }

    public void setSelectionRegles(List<YvsGrhElementSalaire> selectionRegles) {
        this.selectionRegles = selectionRegles;
    }

    public String getFiltre() {
        return filtre;
    }

    public void setFiltre(String filtre) {
        this.filtre = filtre;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public String getTextFind() {
        return textFind;
    }

    public void setTextFind(String textFind) {
        this.textFind = textFind;
    }

    public String getLabelButon() {
        return labelButon;
    }

    public void setLabelButon(String labelButon) {
        this.labelButon = labelButon;
    }

    public List<YvsGrhElementSalaire> getRegles() {
        return regles;
    }

    public void setRegles(List<YvsGrhElementSalaire> regles) {
        this.regles = regles;
    }

    public List<YvsGrhStructureSalaire> getListeStructure() {
        return listeStructure;
    }

    public void setListeStructure(List<YvsGrhStructureSalaire> listeStructure) {
        this.listeStructure = listeStructure;
    }

    public StructureElementSalaire getStructure() {
        return structure;
    }

    public void setStructure(StructureElementSalaire structure) {
        this.structure = structure;
    }

    public boolean isDisplayGrille() {
        return displayGrille;
    }

    public void setDisplayGrille(boolean displayGrille) {
        this.displayGrille = displayGrille;
    }

    public boolean isDisplayButonOption() {
        return displayButonOption;
    }

    public void setDisplayButonOption(boolean displayButonOption) {
        this.displayButonOption = displayButonOption;
    }

    public List<YvsGrhStructureSalaire> getSelectionStructure() {
        return selectionStructure;
    }

    public void setSelectionStructure(List<YvsGrhStructureSalaire> selectionStructure) {
        this.selectionStructure = selectionStructure;
    }

    public String getLabelBtnAttribuer() {
        return labelBtnAttribuer;
    }

    public void setLabelBtnAttribuer(String labelBtnAttribuer) {
        this.labelBtnAttribuer = labelBtnAttribuer;
    }

    public String getChaineSelectStructure() {
        return chaineSelectStructure;
    }

    public void setChaineSelectStructure(String chaineSelectStructure) {
        this.chaineSelectStructure = chaineSelectStructure;
    }

    public String getTextSearchRegleAttribuer() {
        return textSearchRegleAttribuer;
    }

    public void setTextSearchRegleAttribuer(String textSearchRegleAttribuer) {
        this.textSearchRegleAttribuer = textSearchRegleAttribuer;
    }

    public String getSearchRegle() {
        return searchRegle;
    }

    public void setSearchRegle(String searchRegle) {
        this.searchRegle = searchRegle;
    }

    @Override
    public boolean controleFiche(StructureElementSalaire bean) {

        if ((bean.getCode() == null) ? true : bean.getCode().trim().equals("")) {
            getMessage("Vous devez entrer un code", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if ((bean.getNom() == null) ? true : bean.getNom().trim().equals("")) {
            getMessage("Vous devez entrer un nom", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StructureElementSalaire recopieView() {
        StructureElementSalaire st = new StructureElementSalaire(structure.getId(), structure.getNom(), structure.getCode());
        st.setDescription(structure.getDescription());
        return st;
    }

    @Override
    public void populateView(StructureElementSalaire bean) {
        structure.setCode(bean.getCode());
        structure.setDescription(bean.getDescription());
        structure.setId(bean.getId());
        structure.setNom(bean.getNom());
        structure.setListElement(bean.getListElement());
        update = true;
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        execute("collapseForm('structureS')");
        currentStrucre = (YvsGrhStructureSalaire) ev.getObject();
        populateView(UtilGrh.buildBeanStructure(currentStrucre));
        chaineSelectStructure = listeStructure.indexOf(currentStrucre) + "";
     

    }

//    public void selectedLine() {
//        setDisplayButonOption(selectedStructure.length != 0);
//        update("str-rs-panel");
//        populateView(structure);
//    }
    @Override
    public void loadAll() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listeStructure = dao.loadNameQueries("YvsStructureSalaire.findAll", champ, val);
    }
    YvsGrhStructureSalaire currentStrucre;

    @Override
    public boolean saveNew() {
        if (controleFiche(structure)) {
            currentStrucre = UtilGrh.buildBeanStructure(structure);
            currentStrucre.setSociete(currentUser.getAgence().getSociete());
            currentStrucre.setAuthor(currentUser);
            if (!update) {
                currentStrucre.setId(null);
                currentStrucre = (YvsGrhStructureSalaire) dao.save1(currentStrucre);
            } else //update
            {
                dao.update(currentStrucre);
            }
            listeStructure.add(0, currentStrucre);
            succes();
        }
        return true;
    }

    @Override
    public void resetFiche() {
        resetFiche(structure);
        structure.setListElement(new ArrayList<YvsGrhElementStructure>());
    }

    public void openDlgConfirmClone(YvsGrhStructureSalaire st) {
        selectedStructure = st;
        openDialog("dlgclone");
    }

    public void confirmCloneStructure() {
        if (selectedStructure != null) {
            YvsGrhStructureSalaire st = new YvsGrhStructureSalaire(currentStrucre);
            st.setId(null);
            st = (YvsGrhStructureSalaire) dao.save1(st);
            YvsGrhElementStructure es;
            st.setElementSalaire(new ArrayList<YvsGrhElementStructure>());
            for (YvsGrhElementStructure el : selectedStructure.getElementSalaire()) {
                es = new YvsGrhElementStructure(el);
                es.setId(null);
                es.setStructure(st);
                es = (YvsGrhElementStructure) dao.save1(es);
                st.getElementSalaire().add(es);
            }
            listeStructure.add(st);
            succes();
        }
    }

    public void loadElementSalaire() {
        if (currentStrucre != null) {
            champ = new String[]{"societe"};
            val = new Object[]{currentUser.getAgence().getSociete()};
            regles = dao.loadNameQueries("YvsGrhElementSalaire.findAll", champ, val);
            //règle déjà attribuer à la structure
            champ = new String[]{"structure"};
            val = new Object[]{currentStrucre};
            List<YvsGrhElementSalaire> l = dao.loadNameQueries("YvsElementStructure.findElementStructure", champ, val);
            regles.removeAll(l);
        } else {
            getErrorMessage("Aucune structure n'a été selectionné !");
        }
    }

    public void deleteStructure() {
        System.err.println("chaineSelectStructure = " +chaineSelectStructure);
        if ((chaineSelectStructure != null) ? !chaineSelectStructure.equals("") : false) {
            List<Long> l = decomposeIdSelection(chaineSelectStructure);
            List<YvsGrhStructureSalaire> list = new ArrayList<>();
            YvsGrhStructureSalaire bean;
            try {
                for (Long ids : l) {
                    bean = listeStructure.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
          
                }
                listeStructure.removeAll(list);
                chaineSelectStructure = "";
                succes();
                resetFiche();
            } catch (Exception ex) {
                getErrorMessage("Impossible de terminer cette opération !");
            }
//        if (selectedStructure != null) {
//            for (StructureElementSalaire s : selectedStructure) {
//                YvsGrhStructureSalaire ss = new YvsGrhStructureSalaire(s.getId());
//                ss.setSociete(currentAgence.getSociete());
//                dao.delete(ss);
//                listeStructure.remove(s);
//            }

//            closeDialog("dlgDelete");
//            update("tab-struct-regS");
        }
    }

    public void voirElements() {
//        if (selectedStructure != null) {
////            populateView(selectedStructure[0]);
//        }
    }

    public void addRegleInStructure() {
        if (currentStrucre != null) {
            for (YvsGrhElementSalaire el : selectionRegles) {
                YvsGrhElementStructure es = new YvsGrhElementStructure();
                es.setActif(true);
                es.setElement(el);
                es.setStructure(currentStrucre);
                es.setAuthor(currentUser);
                es = (YvsGrhElementStructure) dao.save1(es);
                el.setIdStructure(es.getId());
                el.setAttribuer(true);
                structure.getListElement().add(0, es);
            }
            regles.removeAll(selectionRegles);
            succes();
        }
        update("tab-add-regle");
    }

    public void removeElementInstructure(YvsGrhElementStructure els) {
        try {
            if (els != null) {
                els.setAuthor(currentUser);
                dao.delete(els);
                structure.getListElement().remove(els);
            } else {
                getMessage("Votre selection est ambigue", FacesMessage.SEVERITY_ERROR);
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette ligne !");
            log.log(Level.SEVERE, null, ex);
        }
    }
    /*recherche une structure de tache*/

    public void findStructure(String regle) {
        champ = new String[]{"nom", "societe"};
        val = new Object[]{"%" + regle + "%", currentAgence.getSociete()};
        listeStructure = dao.loadNameQueries("YvsStructureSalaire.findByNom", champ, val, 0, 100);
    }

    public void findRegleAttribuer(String regle) {
        champ = new String[]{"code", "structure"};
        val = new Object[]{"%" + regle + "%", currentStrucre};
        List<YvsGrhElementStructure> l = dao.loadNameQueries("YvsGrhElementStructure.findElementStructureLike", champ, val);
        structure.setListElement(l);
    }

    public void findRegle(String regle) {
        champ = new String[]{"code", "societe"};
        val = new Object[]{"%" + regle + "%", currentUser.getAgence().getSociete()};
        regles = dao.loadNameQueries("YvsGrhElementSalaire.findByCode1", champ, val);
    }

    public void filterList() {
        getMessage(filtre, FacesMessage.SEVERITY_INFO);
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

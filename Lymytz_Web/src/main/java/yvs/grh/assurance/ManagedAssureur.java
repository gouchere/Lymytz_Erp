/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.assurance;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.personnel.YvsGrhAssureur;
import yvs.entity.grh.personnel.YvsGrhTypeAssurance;
import yvs.grh.UtilGrh;
import yvs.util.Managed;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class ManagedAssureur extends Managed<TypeAssurance, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{typeAssurance}")
    TypeAssurance typeAssurance;
    private Assureur assureur = new Assureur();
    private boolean vueListe = true, vueListeAssureur = true, updateAssureur, optionUpdate, optionUpdateAssureur,
            vuePersCharge, selectTypeAssurance, selectAssureur, updateTypeAssurance;
    private String nameBtnVue = "Nouveau", nameBtnVueAssureur = "Nouveau";

    private List<TypeAssurance> listTypeAssurance, listSelectTypeAssurance;
    private List<Assureur> listAssureur, listSelectAssureur;

    YvsGrhTypeAssurance entityTypeAssurance;
    YvsGrhAssureur entityAssureur;

    public ManagedAssureur() {
        listTypeAssurance = new ArrayList<>();
        listAssureur = new ArrayList<>();
        listSelectTypeAssurance = new ArrayList<>();
        listSelectAssureur = new ArrayList<>();
    }

    public boolean isOptionUpdateAssureur() {
        return optionUpdateAssureur;
    }

    public void setOptionUpdateAssureur(boolean optionUpdateAssureur) {
        this.optionUpdateAssureur = optionUpdateAssureur;
    }

    public List<Assureur> getListSelectAssureur() {
        return listSelectAssureur;
    }

    public void setListSelectAssureur(List<Assureur> listSelectAssureur) {
        this.listSelectAssureur = listSelectAssureur;
    }

    public boolean isSelectAssureur() {
        return selectAssureur;
    }

    public void setSelectAssureur(boolean selectAssureur) {
        this.selectAssureur = selectAssureur;
    }

    public String getNameBtnVueAssureur() {
        return nameBtnVueAssureur;
    }

    public void setNameBtnVueAssureur(String nameBtnVueAssureur) {
        this.nameBtnVueAssureur = nameBtnVueAssureur;
    }

    public boolean isVueListeAssureur() {
        return vueListeAssureur;
    }

    public void setVueListeAssureur(boolean vueListeAssureur) {
        this.vueListeAssureur = vueListeAssureur;
    }

    public boolean isUpdateTypeAssurance() {
        return updateTypeAssurance;
    }

    public void setUpdateTypeAssurance(boolean updateTypeAssurance) {
        this.updateTypeAssurance = updateTypeAssurance;
    }

    public List<Assureur> getListAssureur() {
        return listAssureur;
    }

    public void setListAssureur(List<Assureur> listAssureur) {
        this.listAssureur = listAssureur;
    }

    public YvsGrhAssureur getEntityAssureur() {
        return entityAssureur;
    }

    public void setEntityAssureur(YvsGrhAssureur entityAssureur) {
        this.entityAssureur = entityAssureur;
    }

    public List<TypeAssurance> getListSelectTypeAssurance() {
        return listSelectTypeAssurance;
    }

    public void setListSelectTypeAssurance(List<TypeAssurance> listSelectTypeAssurance) {
        this.listSelectTypeAssurance = listSelectTypeAssurance;
    }

    public boolean isSelectTypeAssurance() {
        return selectTypeAssurance;
    }

    public void setSelectTypeAssurance(boolean selectTypeAssurance) {
        this.selectTypeAssurance = selectTypeAssurance;
    }

    public List<TypeAssurance> getListTypeAssurance() {
        return listTypeAssurance;
    }

    public void setListTypeAssurance(List<TypeAssurance> listTypeAssurance) {
        this.listTypeAssurance = listTypeAssurance;
    }

    public boolean isVuePersCharge() {
        return vuePersCharge;
    }

    public void setVuePersCharge(boolean vuePersCharge) {
        this.vuePersCharge = vuePersCharge;
    }

    public boolean isOptionUpdate() {
        return optionUpdate;
    }

    public void setOptionUpdate(boolean optionUpdate) {
        this.optionUpdate = optionUpdate;
    }

    public boolean isUpdateAssureur() {
        return updateAssureur;
    }

    public void setUpdateAssureur(boolean updateAssureur) {
        this.updateAssureur = updateAssureur;
    }

    public TypeAssurance getTypeAssurance() {
        return typeAssurance;
    }

    public void setTypeAssurance(TypeAssurance typeAssurance) {
        this.typeAssurance = typeAssurance;
    }

    public YvsGrhTypeAssurance getEntityTypeAssurance() {
        return entityTypeAssurance;
    }

    public void setEntityTypeAssurance(YvsGrhTypeAssurance entityTypeAssurance) {
        this.entityTypeAssurance = entityTypeAssurance;
    }

    public boolean isVueListe() {
        return vueListe;
    }

    public void setVueListe(boolean vueListe) {
        this.vueListe = vueListe;
    }

    public String getNameBtnVue() {
        return nameBtnVue;
    }

    public void setNameBtnVue(String nameBtnVue) {
        this.nameBtnVue = nameBtnVue;
    }

    public Assureur getAssureur() {
        return assureur;
    }

    public void setAssureur(Assureur assureur) {
        this.assureur = assureur;
    }

    public YvsGrhAssureur buildAssureur(Assureur e) {
        YvsGrhAssureur b = new YvsGrhAssureur();
        if (e != null) {
            b.setId(e.getId());
            b.setNom(e.getNom());
            b.setTelephone(e.getTelephone());
            b.setAdresse(e.getAdresse());
            b.setSociete(currentAgence.getSociete());
        }
        return b;
    }

    public YvsGrhTypeAssurance buildTypeAssurance(TypeAssurance e) {
        YvsGrhTypeAssurance b = new YvsGrhTypeAssurance();
        if (e != null) {
            b.setId(e.getId());
            b.setDescription(e.getDescription());
            b.setLibelle(e.getLibelle());
            b.setAssureur(new YvsGrhAssureur(e.getAssureur().getId()));
        }
        return b;
    }

    public void changeVue() {
        setVueListe(!isVueListe());
        resetFiche();
        resetPage();
        setNameBtnVue((isVueListe()) ? "Nouveau" : "Liste");
        update("entete_assureur_00");
        update("form_assureur_00");
    }

    public void changeVueAssureur() {
        setVueListeAssureur(!isVueListeAssureur());
        resetFicheAssureur();
        resetPageAssureur();
        setNameBtnVueAssureur((isVueListeAssureur()) ? "Nouveau" : "Liste");
        update("dlg_entete_assureur_00");
        update("dlg_form_assureur_00");
    }

    public void selectTypeAssurance(TypeAssurance bean) {
        bean.setSelectActif(!bean.isSelectActif());
        listTypeAssurance.get(listTypeAssurance.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (listSelectTypeAssurance.contains(bean)) {
            listSelectTypeAssurance.remove(bean);
        } else {
            listSelectTypeAssurance.add(bean);
        }
        if (listSelectTypeAssurance.isEmpty()) {

        } else {
            populateView(listSelectTypeAssurance.get(listSelectTypeAssurance.size() - 1));
        }
        setUpdateTypeAssurance(!listSelectTypeAssurance.isEmpty());
        setUpdateAssureur(!listSelectTypeAssurance.isEmpty());
        setSelectTypeAssurance(!listSelectTypeAssurance.isEmpty());
        setOptionUpdate(listSelectTypeAssurance.size() == 1);
        update("entete_assureur_00");
        update("blog_assureur_00");
    }

    public void selectAssureur(Assureur bean) {
        bean.setSelectActif(!bean.isSelectActif());
        listAssureur.get(listAssureur.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (listSelectAssureur.contains(bean)) {
            listSelectAssureur.remove(bean);
        } else {
            listSelectAssureur.add(bean);
        }
        if (listSelectAssureur.isEmpty()) {

        } else {
            populateView(listSelectAssureur.get(listSelectAssureur.size() - 1));
        }
        setUpdateAssureur(!listSelectAssureur.isEmpty());
        setSelectAssureur(!listSelectAssureur.isEmpty());
        setOptionUpdateAssureur(listSelectAssureur.size() == 1);
        update("dlg_entete_assureur_00");
        update("dlg_form_assureur_00");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        TypeAssurance bean = (TypeAssurance) ev.getObject();
        selectTypeAssurance(bean);
    }

    public void unLoadOnView(UnselectEvent ev) {
        TypeAssurance bean = (TypeAssurance) ev.getObject();
        selectTypeAssurance(bean);
    }

    public void loadOnViewAssureur(SelectEvent ev) {
        Assureur bean = (Assureur) ev.getObject();
        selectAssureur(bean);
    }

    public void unLoadOnViewAssureur(UnselectEvent ev) {
        Assureur bean = (Assureur) ev.getObject();
        selectAssureur(bean);
    }

    public void loadAllAssureur() {
        listAssureur.clear();
        listAssureur = UtilGrh.buildBeanListAssureur(dao.loadNameQueries("YvsGrhAssureur.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()}));
    }

    @Override
    public void loadAll() {
        listTypeAssurance.clear();
        listTypeAssurance = UtilGrh.buildBeanListTypeAssurance(dao.loadNameQueries("YvsGrhTypeAssurance.findAll", new String[]{}, new Object[]{}));
    }

    public boolean controleFiche(Assureur bean) {
        if (bean.getNom() == null || "".equals(bean.getNom())) {
            getMessage("Vous devez entrer le nom", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getAdresse() == null || "".equals(bean.getAdresse())) {
            getMessage("Vous devez entrer une adresse", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getTelephone() == null || "".equals(bean.getTelephone())) {
            getMessage("Vous devez entrer un numero de telephone", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public boolean controleFiche(TypeAssurance bean) {
        if (bean.getDescription() == null || "".equals(bean.getDescription())) {
            getMessage("Vous devez entrer la description", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getLibelle() == null || "".equals(bean.getLibelle())) {
            getMessage("Vous devez entrer le libelle", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getAssureur().getId() == 0) {
            getMessage("Vous devez specifier l'assureur", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(typeAssurance)) {
            if (!updateTypeAssurance) {
                entityTypeAssurance = buildTypeAssurance(typeAssurance);
                entityTypeAssurance.setId(null);
                entityTypeAssurance = (YvsGrhTypeAssurance) dao.save1(entityTypeAssurance);
                typeAssurance.setId(entityTypeAssurance.getId());
                TypeAssurance t = new TypeAssurance();
                cloneObject(t, typeAssurance);
                listTypeAssurance.add(t);
            } else {
                entityTypeAssurance = buildTypeAssurance(typeAssurance);
                entityTypeAssurance = (YvsGrhTypeAssurance) dao.update(entityTypeAssurance);
                TypeAssurance t = new TypeAssurance();
                cloneObject(t, typeAssurance);
                listTypeAssurance.set(listTypeAssurance.indexOf(typeAssurance), t);
            }
            setUpdateTypeAssurance(true);
            update("form_assureur_00");
            succes();
        }
        return true;
    }

    public boolean saveNewAssureur() {
        if (controleFiche(assureur)) {
            entityAssureur = buildAssureur(assureur);
            Assureur a = new Assureur();
            if (!updateAssureur) {
                entityAssureur.setId(null);
                 entityAssureur = (YvsGrhAssureur) dao.save1(entityAssureur);
                assureur.setId(entityAssureur.getId());
                cloneObject(a, assureur);
                cloneObject(typeAssurance.getAssureur(), a);
                listAssureur.add(a);
            } else {
                System.err.println("UPDATE ASSUREUR");
                entityAssureur = (YvsGrhAssureur) dao.update(entityAssureur);
                cloneObject(a, assureur);
                TypeAssurance t = new TypeAssurance();
                cloneObject(t, typeAssurance);
                t.setAssureur(a);
                listAssureur.set(listAssureur.indexOf(assureur), a);
//                listTypeAssurance.set(listTypeAssurance.indexOf(typeAssurance), t);
            }
            setUpdateAssureur(true);
            succes();
            update("form_assureur_00");
            update("dlg_form_assureur_00");
        }
        return true;
    }

    @Override
    public void updateBean() {
        setVueListe(false);
        setNameBtnVue((isVueListe()) ? "Nouveau" : "Liste");
        setSelectTypeAssurance(false);
        update("entete_assureur_00");
        update("form_assureur_00");
    }

    public void updateBeanAssureur(TypeAssurance ev) {
        selectTypeAssurance(ev);
        populateView(ev.getAssureur());
        setVueListeAssureur(false);
        setUpdateAssureur(true);
        setSelectAssureur(false);
        openDialog("dlgAssureur");
        setNameBtnVueAssureur((isVueListeAssureur()) ? "Nouveau" : "Liste");
        update("dlg_entete_assureur_00");
        update("dlg_form_assureur_00");
    }

    public void updateBeanAssureur() {
        setVueListeAssureur(false);
        setSelectAssureur(false);
        setNameBtnVueAssureur((isVueListeAssureur()) ? "Nouveau" : "Liste");
        update("dlg_entete_assureur_00");
        update("dlg_form_assureur_00");
    }

    public void deleteBean() {
        if (listSelectTypeAssurance != null) {
            if (!listSelectTypeAssurance.isEmpty()) {
                for (TypeAssurance f : listSelectTypeAssurance) {
                    dao.delete(new YvsGrhTypeAssurance(f.getId()));
                    listTypeAssurance.remove(f);
                }
                succes();
                resetFiche();
                resetPage();
                update("entete_assureur_00");
                update("blog_assureur_00");
            }
        }
    }

    public boolean deleteBeanAssureur() {
        try {
            if (listSelectAssureur != null) {
                if (!listSelectAssureur.isEmpty()) {
                    for (Assureur f : listSelectAssureur) {
                        dao.delete(new YvsGrhAssureur(f.getId()));
                        listAssureur.remove(f);
                    }
                    succes();
                    resetFicheAssureur();
                    resetPageAssureur();
                    update("dlg_entete_assureur_00");
                    update("dlg_form_assureur_00");
                }
            }
            return true;
        } catch (Exception ex) {
            getMessage("Cet assureur est associe a des types d'assurance", FacesMessage.SEVERITY_ERROR);
            System.err.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(typeAssurance);
        typeAssurance.setAssureur(new Assureur());
        typeAssurance.setAssuranceList(new ArrayList<Assurance>());
        setUpdateTypeAssurance(false);
        setSelectTypeAssurance(false);
        update("blog_assureur_01");
    }

    public void resetFicheAssureur() {
        resetFiche(assureur);
        assureur.setTypeAssuranceList(new ArrayList<TypeAssurance>());
        setUpdateAssureur(false);
        setSelectTypeAssurance(false);
        update("dlg_form_assureur_001");
    }

    public void resetPage() {
        for (TypeAssurance f : listTypeAssurance) {
            listTypeAssurance.get(listTypeAssurance.indexOf(f)).setSelectActif(false);
        }
        listSelectTypeAssurance.clear();
    }

    public void resetPageAssureur() {
        for (Assureur f : listAssureur) {
            listAssureur.get(listAssureur.indexOf(f)).setSelectActif(false);
        }
        listSelectAssureur.clear();
    }

    public Assureur recopieViewAssureur(Assureur beans) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TypeAssurance recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(TypeAssurance bean) {
        cloneObject(typeAssurance, bean);
        populateView(typeAssurance.getAssureur());
        entityTypeAssurance = buildTypeAssurance(typeAssurance);
    }

    public void populateView(Assureur bean) {
        cloneObject(assureur, bean);
        entityAssureur = buildAssureur(assureur);
    }

    public void choixTypeAssureur(ValueChangeEvent ev) {
        int type = ((int) ev.getNewValue());
        if (type == -1) {
            setVueListeAssureur(false);
            setSelectAssureur(false);
            openDialog("dlgAssureur");
            setNameBtnVueAssureur((isVueListeAssureur()) ? "Nouveau" : "Liste");
            update("dlg_entete_assureur_00");
            update("dlg_form_assureur_00");
        } else if (type > 0) {
            typeAssurance.getAssureur().setId(type);
            typeAssurance.getAssureur().setNom(listAssureur.get(listAssureur.indexOf(new Assureur(type))).getNom());
        }
    }
    public void updateForm(){
        
    }
}

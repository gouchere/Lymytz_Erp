/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.assurance;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsGrhAssurance;
import yvs.entity.grh.personnel.YvsGrhBilanAssurance;
import yvs.entity.grh.personnel.YvsGrhBilanCouverture;
import yvs.entity.grh.personnel.YvsGrhCouverturePersonneCharge;
import yvs.entity.grh.personnel.YvsGrhPersonneEnCharge;
import yvs.entity.grh.personnel.YvsGrhTypeAssurance;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import yvs.grh.bean.PersonneEnCharge;
import yvs.util.Managed;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class ManagedAssurance extends Managed<Assurance, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{assurance}")
    private Assurance assurance;

    private boolean error, vueListe = true, updateAssurance, updateAssuranceEmploye, optionUpdate, optionSvtBilan, vuePersCharge, vueDetailAssurance,
            selectAssurance, viewTabPersCharge, displayTxtEmploye, selectPersCharge, updateCouverture,
            vueListeBilan, selectBilan, updateBilan, optionUpdateBilan;
    private String nameBtnVue = "Nouveau", nameBtnDlgVueBilan = "Nouveau", nameBtnAddAssurance = "Ajouter";
    private int idTemp = 0;

    private Assurance couverture = new Assurance(), assuranceSelect = new Assurance();
    private BilanAssurance bilan = new BilanAssurance();
    private TypeAssurance typeAssurance = new TypeAssurance();
    private Employe employe = new Employe();
    private List<PersonneEnCharge> listPersCharge, listPersChargeAssurance, listSelectPersCharge;
    private List<Assurance> listAssurance, listAssuranceEmploye, listSelectAssurance, listSelectAssuranceEmploye,
            listSelectCouverture, listCouverture;
    private List<TypeAssurance> listTypeAssurance;
    private List<BilanAssurance> listBilan, listSelectBilan;

    YvsGrhAssurance entityAssurance;

    public ManagedAssurance() {
        listPersCharge = new ArrayList<>();
        listSelectPersCharge = new ArrayList<>();
        listSelectAssurance = new ArrayList<>();
        listAssurance = new ArrayList<>();
        listAssuranceEmploye = new ArrayList<>();
        listTypeAssurance = new ArrayList<>();
        listSelectCouverture = new ArrayList<>();
        listCouverture = new ArrayList<>();
        listSelectAssuranceEmploye = new ArrayList<>();
        listPersChargeAssurance = new ArrayList<>();
        listBilan = new ArrayList<>();
        listSelectBilan = new ArrayList<>();
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isSelectBilan() {
        return selectBilan;
    }

    public void setSelectBilan(boolean selectBilan) {
        this.selectBilan = selectBilan;
    }

    public boolean isUpdateBilan() {
        return updateBilan;
    }

    public void setUpdateBilan(boolean updateBilan) {
        this.updateBilan = updateBilan;
    }

    public boolean isOptionUpdateBilan() {
        return optionUpdateBilan;
    }

    public void setOptionUpdateBilan(boolean optionUpdateBilan) {
        this.optionUpdateBilan = optionUpdateBilan;
    }

    public String getNameBtnDlgVueBilan() {
        return nameBtnDlgVueBilan;
    }

    public void setNameBtnDlgVueBilan(String nameBtnDlgVueBilan) {
        this.nameBtnDlgVueBilan = nameBtnDlgVueBilan;
    }

    public BilanAssurance getBilan() {
        return bilan;
    }

    public void setBilan(BilanAssurance bilan) {
        this.bilan = bilan;
    }

    public List<BilanAssurance> getListBilan() {
        return listBilan;
    }

    public void setListBilan(List<BilanAssurance> listBilan) {
        this.listBilan = listBilan;
    }

    public List<BilanAssurance> getListSelectBilan() {
        return listSelectBilan;
    }

    public void setListSelectBilan(List<BilanAssurance> listSelectBilan) {
        this.listSelectBilan = listSelectBilan;
    }

    public boolean isVueListeBilan() {
        return vueListeBilan;
    }

    public void setVueListeBilan(boolean vueListeBilan) {
        this.vueListeBilan = vueListeBilan;
    }

    public boolean isOptionSvtBilan() {
        return optionSvtBilan;
    }

    public void setOptionSvtBilan(boolean optionSvtBilan) {
        this.optionSvtBilan = optionSvtBilan;
    }

    public boolean isUpdateCouverture() {
        return updateCouverture;
    }

    public void setUpdateCouverture(boolean updateCouverture) {
        this.updateCouverture = updateCouverture;
    }

    public boolean isSelectPersCharge() {
        return selectPersCharge;
    }

    public void setSelectPersCharge(boolean selectPersCharge) {
        this.selectPersCharge = selectPersCharge;
    }

    public List<PersonneEnCharge> getListPersChargeAssurance() {
        return listPersChargeAssurance;
    }

    public void setListPersChargeAssurance(List<PersonneEnCharge> listPersChargeAssurance) {
        this.listPersChargeAssurance = listPersChargeAssurance;
    }

    public boolean isViewTabPersCharge() {
        return viewTabPersCharge;
    }

    public void setViewTabPersCharge(boolean viewTabPersCharge) {
        this.viewTabPersCharge = viewTabPersCharge;
    }

    public List<Assurance> getListSelectAssuranceEmploye() {
        return listSelectAssuranceEmploye;
    }

    public void setListSelectAssuranceEmploye(List<Assurance> listSelectAssuranceEmploye) {
        this.listSelectAssuranceEmploye = listSelectAssuranceEmploye;
    }

    public boolean isUpdateAssuranceEmploye() {
        return updateAssuranceEmploye;
    }

    public void setUpdateAssuranceEmploye(boolean updateAssuranceEmploye) {
        this.updateAssuranceEmploye = updateAssuranceEmploye;
    }

    public String getNameBtnAddAssurance() {
        return nameBtnAddAssurance;
    }

    public void setNameBtnAddAssurance(String nameBtnAddAssurance) {
        this.nameBtnAddAssurance = nameBtnAddAssurance;
    }

    public int getIdTemp() {
        return idTemp;
    }

    public void setIdTemp(int idTemp) {
        this.idTemp = idTemp;
    }

    public boolean isDisplayTxtEmploye() {
        return displayTxtEmploye;
    }

    public void setDisplayTxtEmploye(boolean displayTxtEmploye) {
        this.displayTxtEmploye = displayTxtEmploye;
    }

    public boolean isVueDetailAssurance() {
        return vueDetailAssurance;
    }

    public void setVueDetailAssurance(boolean vueDetailAssurance) {
        this.vueDetailAssurance = vueDetailAssurance;
    }

    public List<PersonneEnCharge> getListSelectPersCharge() {
        return listSelectPersCharge;
    }

    public void setListSelectPersCharge(List<PersonneEnCharge> listSelectPersCharge) {
        this.listSelectPersCharge = listSelectPersCharge;
    }

    public List<Assurance> getListAssuranceEmploye() {
        return listAssuranceEmploye;
    }

    public void setListAssuranceEmploye(List<Assurance> listAssuranceEmploye) {
        this.listAssuranceEmploye = listAssuranceEmploye;
    }

    public Assurance getCouverture() {
        return couverture;
    }

    public void setCouverture(Assurance couverture) {
        this.couverture = couverture;
    }

    public List<Assurance> getListSelectCouverture() {
        return listSelectCouverture;
    }

    public void setListSelectCouverture(List<Assurance> listSelectCouverture) {
        this.listSelectCouverture = listSelectCouverture;
    }

    public Assurance getAssuranceSelect() {
        return assuranceSelect;
    }

    public void setAssuranceSelect(Assurance AssuranceSelect) {
        this.assuranceSelect = AssuranceSelect;
    }

    public List<TypeAssurance> getListTypeAssurance() {
        return listTypeAssurance;
    }

    public void setListTypeAssurance(List<TypeAssurance> listTypeAssurance) {
        this.listTypeAssurance = listTypeAssurance;
    }

    public List<PersonneEnCharge> getListPersCharge() {
        return listPersCharge;
    }

    public void setListPersCharge(List<PersonneEnCharge> listPersCharge) {
        this.listPersCharge = listPersCharge;
    }

    public List<Assurance> getListSelectAssurance() {
        return listSelectAssurance;
    }

    public void setListSelectAssurance(List<Assurance> listSelectAssurance) {
        this.listSelectAssurance = listSelectAssurance;
    }

    public List<Assurance> getListCouverture() {
        return listCouverture;
    }

    public void setListCouverture(List<Assurance> listCouverture) {
        this.listCouverture = listCouverture;
    }

    public boolean isSelectAssurance() {
        return selectAssurance;
    }

    public void setSelectAssurance(boolean selectAssurance) {
        this.selectAssurance = selectAssurance;
    }

    public List<Assurance> getListAssurance() {
        return listAssurance;
    }

    public void setListAssurance(List<Assurance> listAssurance) {
        this.listAssurance = listAssurance;
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

    public boolean isUpdateAssurance() {
        return updateAssurance;
    }

    public void setUpdateAssurance(boolean updateAssurance) {
        this.updateAssurance = updateAssurance;
    }

    public TypeAssurance getTypeAssurance() {
        return typeAssurance;
    }

    public void setTypeAssurance(TypeAssurance typeAssurance) {
        this.typeAssurance = typeAssurance;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public YvsGrhAssurance getEntityAssurance() {
        return entityAssurance;
    }

    public void setEntityAssurance(YvsGrhAssurance entityAssurance) {
        this.entityAssurance = entityAssurance;
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

    public Assurance getAssurance() {
        return assurance;
    }

    public void setAssurance(Assurance assurance) {
        this.assurance = assurance;
    }

    public YvsGrhCouverturePersonneCharge buildCouverture(Assurance e) {
        YvsGrhCouverturePersonneCharge b = new YvsGrhCouverturePersonneCharge();
        if (e != null) {
            b.setId(e.getId());
            b.setTauxCouverture((e.getTauxCouverture() != null) ? e.getTauxCouverture() : 0);
            b.setPersonne((e.getPersonneCharge() != null) ? new YvsGrhPersonneEnCharge(e.getPersonneCharge().getId())
                    : new YvsGrhPersonneEnCharge());
            b.setAssurance(entityAssurance);
        }
        return b;
    }

    public YvsGrhAssurance buildAssurance(Assurance e) {
        YvsGrhAssurance b = new YvsGrhAssurance();
        if (e != null) {
            b.setId(e.getId());
            b.setTauxCouverture((e.getTauxCouverture() != null) ? e.getTauxCouverture() : 0);
            b.setReference(e.getReference());
            b.setTauxCotisation((e.getTauxCotisation() != null) ? e.getTauxCotisation() : 0);
            b.setTypeAssurance((e.getTypeAssurance() != null) ? new YvsGrhTypeAssurance(e.getTypeAssurance().getId()) : new YvsGrhTypeAssurance());
            b.setEmploye((e.getEmploye() != null) ? new YvsGrhEmployes(e.getEmploye().getId()) : new YvsGrhEmployes());
        }
        return b;
    }

    public YvsGrhBilanAssurance buildBilanAssurance(BilanAssurance e) {
        YvsGrhBilanAssurance b = new YvsGrhBilanAssurance();
        if (e != null) {
            b.setId(e.getId());
            b.setFichier(e.getFichier());
            b.setDateAssurance((e.getDateAssurance() != null) ? e.getDateAssurance() : new Date());
            b.setMontant((e.getMontant() != null) ? e.getMontant() : 0);
            b.setAssurance((e.getAssurance() != null) ? new YvsGrhAssurance(e.getAssurance().getId()) : new YvsGrhAssurance());
        }
        return b;
    }

    public YvsGrhBilanCouverture buildBilanCouverture(BilanAssurance e) {
        YvsGrhBilanCouverture b = new YvsGrhBilanCouverture();
        if (e != null) {
            b.setId(e.getId());
            b.setFichier(e.getFichier());
            b.setDateCouverture((e.getDateAssurance() != null) ? e.getDateAssurance() : new Date());
            b.setMontant((e.getMontant() != null) ? e.getMontant() : 0);
            b.setCouverturePersonne((e.getAssurance() != null) ? new YvsGrhCouverturePersonneCharge(e.getAssurance().getId()) : new YvsGrhCouverturePersonneCharge());
        }
        return b;
    }

    public void changeVue() {
        setVueListe(!isVueListe());
        setNameBtnVue((isVueListe()) ? "Nouveau" : "Liste");
        resetFiche();
        update("entete_assurance_00");
        update("form_assurance_00");
    }

    @Override
    public Assurance recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Employe recopieViewEmploye(Employe bean) {
        Employe emp = new Employe(bean.getId(), bean.getNom());
        emp.setAgence(bean.getAgence());
        emp.setMatricule(bean.getMatricule());
        emp.setContrat(bean.getContrat());
        return emp;
    }

    @Override
    public void populateView(Assurance bean) {
        if (bean != null) {
            cloneObject(assurance, bean);
//            listPersCharge.addAll(assurance.getEmploye().getPersonneEnCharge());
            loadAllAssuranceOneEmploye();
            for (Assurance a : listAssuranceEmploye) {
                cloneObject(assurance, a);
                loadAllCourvertureOneAssurance();
            }
            setViewTabPersCharge(!listCouverture.isEmpty());
        }
    }

    public void populateViewBilan(BilanAssurance bean) {
        if (bean != null) {
            cloneObject(bilan, bean);
        }
    }

    public void populateViewAssuranceEmploye(Assurance bean) {
        if (bean != null) {
            cloneObject(assurance, bean);
            cloneObject(assuranceSelect, assurance);
            listCouverture.clear();
            listCouverture.addAll((assurance.getCouvertureList() != null) ? assurance.getCouvertureList() : new ArrayList<Assurance>());
            for (Assurance a : listCouverture) {
                listCouverture.get(listCouverture.indexOf(a)).setTypeAssurance(assurance.getTypeAssurance());
            }
            entityAssurance = buildAssurance(assurance);
            listBilan.clear();
            listBilan.addAll(assurance.getBilanAssuranceList());
            for (BilanAssurance b : listBilan) {
                listBilan.get(listBilan.indexOf(b)).setAssurance(assurance);
            }
        }
    }

    public void populateViewCouverture(Assurance bean) {
        cloneObject(couverture, bean);
        cloneObject(assuranceSelect, couverture);
        listBilan.addAll(couverture.getBilanAssuranceList());
        for (BilanAssurance b : listBilan) {
            listBilan.get(listBilan.indexOf(b)).setAssurance(couverture);
        }
    }

    public void populateViewEmploye(Employe bean) {
        employe.setId(bean.getId());
        employe.setNom(bean.getNom());
        employe.setPrenom(bean.getPrenom());
        employe.setCodeEmploye(bean.getCodeEmploye());
        employe.setMatricule(bean.getMatricule());
        employe.getPersonneEnCharge().addAll(bean.getPersonneEnCharge());
        setDisplayTxtEmploye(true);
        loadAllAssuranceOneEmploye();
        setUpdateAssurance(listAssuranceEmploye.size() > 0);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            BilanAssurance bean = (BilanAssurance) ev.getObject();
            selectBilan(bean);
            update("dlg_entete_bilan_00");
            update("dlg_form_bilan_00");
        }
    }

    public void unLoadOnView(UnselectEvent ev) {
        if (ev != null) {
            BilanAssurance bean = (BilanAssurance) ev.getObject();
            selectBilan(bean);
            update("dlg_entete_bilan_00");
            update("dlg_form_bilan_00");
        }
    }

    public void loadOnViewAssurance(Assurance ev) {
        if (ev != null) {
            selectAssurance(ev);
            update("entete_assurance_00");
            update("blog_assurance_001");
        }
    }

    public void loadOnViewAssuranceEmploye(SelectEvent ev) {
        if (ev != null) {
            Assurance bean = (Assurance) ev.getObject();
            selectAssuranceEmploye(bean);
            if (isVueListe()) {
                update("entete_assurance_00");
                update("blog_assurance_00");
            } else {
                update("blog_assurance_01");
            }
        }
    }

    public void unLoadOnViewAssuranceEmploye(UnselectEvent ev) {
        if (ev != null) {
            Assurance bean = (Assurance) ev.getObject();
            selectAssuranceEmploye(bean);
            if (isVueListe()) {
                update("entete_assurance_00");
                update("blog_assurance_00");
            } else {
                update("blog_assurance_01");
            }
        }
    }

    public void loadOnViewCourveturePersCharger(SelectEvent ev) {
        if (ev != null) {
            Assurance bean = (Assurance) ev.getObject();
            selectCouverture(bean);
            if (isVueListe()) {
                update("entete_assurance_00");
                update("blog_assurance_00");
            } else {
                update("tab_assurance_00:tab_assurance_02");
            }
        }
    }

    public void unLoadOnViewCourveturePersCharger(UnselectEvent ev) {
        if (ev != null) {
            Assurance bean = (Assurance) ev.getObject();
            selectCouverture(bean);
            if (isVueListe()) {
                update("entete_assurance_00");
                update("blog_assurance_00");
            } else {
                update("tab_assurance_00:tab_assurance_02");
                update("tab_assurance_00:footer_couverture_pers");
            }
        }
    }

    public void loadViewEmploye(SelectEvent ev) {
        Employe e = (Employe) ev.getObject();
        if (e != null) {
            assurance.setEmploye(employe);
            populateViewEmploye(e);
        }
        closeDialog("dlgEmploye");
        update("form_assurance_00");
    }

    public void unLoadViewEmploye(SelectEvent ev) {
        resetFiche(employe);
        update("form_assurance_00");
    }

    public void loadViewPersCharge(SelectEvent ev) {
        if (ev != null) {
            PersonneEnCharge e = (PersonneEnCharge) ev.getObject();
            selectPersCharge(e);
        }
        update("dlg_assurance_pers_00");
        update("dlg_assurance_pers_01");
    }

    public void unLoadViewPersCharge(SelectEvent ev) {
        if (ev != null) {
            PersonneEnCharge e = (PersonneEnCharge) ev.getObject();
            selectPersCharge(e);
        }
        update("dlg_assurance_pers_00");
        update("dlg_assurance_pers_01");
    }

    public void selectPersCharge(PersonneEnCharge bean) {
        if (bean != null) {
            if (listSelectPersCharge.contains(bean)) {
                listSelectPersCharge.remove(bean);
            } else {
                listSelectPersCharge.add(bean);
            }
            setSelectPersCharge(!listSelectPersCharge.isEmpty());
        }
    }

    public void selectBilan(BilanAssurance bean) {
        if (bean != null) {
            bean.setSelectActif(!bean.isSelectActif());
            listBilan.get(listBilan.indexOf(bean)).setSelectActif(bean.isSelectActif());
            if (listSelectBilan.contains(bean)) {
                listSelectBilan.remove(bean);
            } else {
                listSelectBilan.add(bean);
            }
            if (listSelectBilan.isEmpty()) {
                resetFicheBilan();
            } else {
                populateViewBilan(listSelectBilan.get(listSelectBilan.size() - 1));
            }
            setSelectBilan(!listSelectBilan.isEmpty());
            setOptionUpdateBilan(listSelectBilan.size() == 1);
            setUpdateBilan(!listSelectBilan.isEmpty());
        }
    }

    public void selectAssurance(Assurance bean) {
        if (bean != null) {
            bean.setSelectActif(!bean.isSelectActif());
            listAssurance.get(listAssurance.indexOf(bean)).setSelectActif(bean.isSelectActif());
            if (listSelectAssurance.contains(bean)) {
                listSelectAssurance.remove(bean);
            } else {
                listSelectAssurance.add(bean);
            }
            if (listSelectAssurance.isEmpty()) {
                resetFicheAssurance();
            } else {
                populateView(listSelectAssurance.get(listSelectAssurance.size() - 1));
            }
            setUpdateAssurance(!listSelectAssurance.isEmpty());
            setOptionUpdate(listSelectAssurance.size() == 1);
            setSelectAssurance(!listSelectAssurance.isEmpty());
        }
    }

    public void selectAssuranceEmploye(Assurance bean) {
        if (bean != null) {
            bean.setSelectActif(!bean.isSelectActif());
            listAssuranceEmploye.get(listAssuranceEmploye.indexOf(bean)).setSelectActif(bean.isSelectActif());
            if (listSelectAssuranceEmploye.contains(bean)) {
                listSelectAssuranceEmploye.remove(bean);
            } else {
                listSelectAssuranceEmploye.add(bean);
            }
            if (listSelectAssuranceEmploye.isEmpty()) {
                resetFicheAssuranceEmploye();
                listCouverture.clear();
            } else {
                populateViewAssuranceEmploye(listSelectAssuranceEmploye.get(listSelectAssuranceEmploye.size() - 1));
            }
            setUpdateAssuranceEmploye(!listSelectAssuranceEmploye.isEmpty());
            setViewTabPersCharge(!listCouverture.isEmpty());
            setNameBtnAddAssurance((isUpdateAssuranceEmploye()) ? "Modifier" : "Ajouter");
            if (isVueListe()) {
                setOptionUpdate(false);
                setSelectAssurance(!listSelectAssuranceEmploye.isEmpty());
                setOptionSvtBilan(listSelectAssuranceEmploye.size() == 1);
            }
        }
    }

    public void selectCouverture(Assurance bean) {
        if (bean != null) {
            bean.setSelectActif(!bean.isSelectActif());
            listCouverture.get(listCouverture.indexOf(bean)).setSelectActif(bean.isSelectActif());
            if (listSelectCouverture.contains(bean)) {
                listSelectCouverture.remove(bean);
            } else {
                listSelectCouverture.add(bean);
            }
            if (listSelectCouverture.isEmpty()) {
                resetFiche(couverture);
            } else {
                populateViewCouverture(listSelectCouverture.get(listSelectCouverture.size() - 1));
            }
            setUpdateCouverture(!listSelectCouverture.isEmpty());
            if (isVueListe()) {
                setOptionUpdate(false);
                setSelectAssurance(!listSelectCouverture.isEmpty());
                setOptionSvtBilan(listSelectCouverture.size() == 1);
            }
        }
    }

    @Override
    public void loadAll() {
        List<YvsGrhAssurance> list = dao.loadNameQueries("YvsGrhAssurance.findAll", new String[]{}, new Object[]{});
        for (YvsGrhAssurance l : list) {
            boolean trouv = false;
            for (Assurance s : listAssurance) {
                if (s.getEmploye().equals(new Employe(l.getEmploye().getId()))) {
                    trouv = true;
                    break;
                }
            }
            if (!trouv) {
                Assurance a = UtilGrh.buildBeanAssurance(l);
                Employe e = UtilGrh.buildBeanPartialEmploye(l.getEmploye());
                a.setEmploye(e);
                listAssurance.add(a);
            }
        }
    }

    public void loadAllTypeAssurance() {
        listTypeAssurance.clear();
        listTypeAssurance = UtilGrh.buildBeanListTypeAssurance(dao.loadNameQueries("YvsGrhTypeAssurance.findAll", new String[]{}, new Object[]{}));
    }

    public void loadAllAssuranceOneEmploye() {
        listAssuranceEmploye.clear();
        listAssuranceEmploye = UtilGrh.buildBeanListAssurance(dao.loadNameQueries("YvsGrhAssurance.findByEmploye",
                new String[]{"employe"}, new Object[]{new YvsGrhEmployes(assurance.getEmploye().getId())}));
        for (Assurance a : listAssuranceEmploye) {
            listAssuranceEmploye.get(listAssuranceEmploye.indexOf(a)).setEmploye(assurance.getEmploye());
        }
    }

    public void loadAllCourvertureOneAssurance() {
        listCouverture = UtilGrh.buildBeanListCouverture(dao.loadNameQueries("YvsGrhCouverturePersonneCharge.findByAssurance",
                new String[]{"assurance"}, new Object[]{new YvsGrhAssurance(assurance.getId())}));
        for (Assurance a : listCouverture) {
            listCouverture.get(listCouverture.indexOf(a)).setTypeAssurance(assurance.getTypeAssurance());
        }
    }

    @Override
    public boolean controleFiche(Assurance bean) {
        if (bean.getEmploye().getId() == 0 || bean.getEmploye() == null) {
            getMessage("Vous devez impliquer un employe", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getTypeAssurance().getId() == 0 || bean.getTypeAssurance() == null) {
            getMessage("Vous devez specifier le type d'assurance", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getTauxCotisation() == 0) {
            getMessage("Vous devez entre le taux de cotisation", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getTauxCouverture() == 0) {
            getMessage("Vous devez entre le taux de couverture", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    public boolean controleFicheCouverture(Assurance bean) {
        if (bean.getPersonneCharge().getId() == 0 || bean.getPersonneCharge() == null) {
            getMessage("Vous devez impliquer une personne", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getTauxCouverture() == 0) {
            getMessage("Vous devez entre le taux de couverture", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    public boolean controleFicheBilan(BilanAssurance bean) {
        if (bean.getAssurance().getId() == 0 || bean.getAssurance() == null) {
            getMessage("Vous devez indiquer la couverture", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getMontant() == 0) {
            getMessage("Vous devez entre le montanr", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getDateAssurance() == null) {
            getMessage("Vous devez entre la date couverture", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public boolean saveNew() {
        if (listAssuranceEmploye != null) {
            if (!listAssuranceEmploye.isEmpty()) {
                if (!isUpdateAssurance()) {
                    for (Assurance a : listAssuranceEmploye) {
                        System.err.println("INSERT ASSURANCE");
                        entityAssurance = buildAssurance(a);
                        entityAssurance.setId(null);
                        entityAssurance = (YvsGrhAssurance) dao.save1(entityAssurance);
                        a.setId(entityAssurance.getId());
                        listAssuranceEmploye.set(listAssuranceEmploye.indexOf(a), a);
                    }
                    boolean trouv = false;
                    for (Assurance t : listAssurance) {
                        if (assurance.getEmploye().equals(t.getEmploye())) {
                            trouv = true;
                            break;
                        }
                    }
                    if (!trouv) {
                        listAssurance.add(assurance);
                    }
                } else {
                    List<Assurance> list = UtilGrh.buildBeanListAssurance(dao.loadNameQueries("YvsGrhAssurance.findByEmploye",
                            new String[]{"employe"}, new Object[]{new YvsGrhEmployes(assurance.getEmploye().getId())}));
                    for (Assurance a : list) {
                        if (!listAssuranceEmploye.contains(a)) {
                            dao.delete(new YvsGrhAssurance(a.getId()));
                        }
                    }
                    for (Assurance a : listAssuranceEmploye) {
                        entityAssurance = buildAssurance(a);
                        if (listAssuranceEmploye.contains(a)) {
                            entityAssurance = (YvsGrhAssurance) dao.update(entityAssurance);
                        } else {
                            entityAssurance.setId(null);
                            entityAssurance = (YvsGrhAssurance) dao.save1(entityAssurance);
                            assurance.setId(entityAssurance.getId());
                        }
                        listAssuranceEmploye.set(listAssuranceEmploye.indexOf(a), a);
                    }
                }
                succes();
                setUpdateAssurance(true);
                update("entete_assurance_00");
                update("blog_assurance_01");
            }
        }
        return true;
    }

    @Override
    public void updateBean() {
        setSelectAssurance(false);
        setVueListe(false);
        setOptionUpdate(false);
        setDisplayTxtEmploye(true);
        resetFicheAssuranceEmploye();
        setNameBtnVue((isVueListe()) ? "Nouveau" : "Liste");
        update("form_assurance_00");
        update("entete_assurance_00");
    }

    public void checkVuePersCharge() {
        setVuePersCharge(true);
        setVueDetailAssurance(true);
        setOptionUpdate(false);
        setSelectAssurance(false);
        resetPage();
        update("blog_assurance_00");
        update("entete_assurance_00");
    }

    public void checkVueAssuranceEmploye() {
        setVuePersCharge(false);
        setVueDetailAssurance(true);
        setSelectAssurance(false);
        setOptionUpdate(false);
        resetPage();
        update("blog_assurance_00");
        update("entete_assurance_00");
    }

    public void checkVueEmploye() {
        setVuePersCharge(false);
        setVueDetailAssurance(false);
        setSelectAssurance(false);
        setOptionSvtBilan(false);
        setOptionUpdate(false);
        resetPage();
        update("blog_assurance_00");
        update("entete_assurance_00");
    }

    public void deleteBean() {
        if (isVueDetailAssurance()) {
            if (isVuePersCharge()) {
                if (!listSelectCouverture.isEmpty()) {
                    for (Assurance a : listSelectCouverture) {
                        dao.delete(new YvsGrhCouverturePersonneCharge(a.getId()));
                        listCouverture.remove(a);
                    }
                    succes();
                }

            } else {
                if (!listSelectAssuranceEmploye.isEmpty()) {
                    for (Assurance a : listSelectAssuranceEmploye) {
                        dao.delete(new YvsGrhAssurance(a.getId()));
                        listAssuranceEmploye.remove(a);
                    }
                    succes();
                }
            }
        } else {
            if (!listSelectAssurance.isEmpty()) {
                for (Assurance a : listSelectAssurance) {
                    cloneObject(assurance, a);
                    loadAllAssuranceOneEmploye();
                    listSelectAssuranceEmploye.addAll(listAssuranceEmploye);
                    for (Assurance a1 : listSelectAssuranceEmploye) {
                        cloneObject(assurance, a1);
                        loadAllCourvertureOneAssurance();
                        listSelectCouverture.addAll(listCouverture);
                        if (listSelectCouverture.isEmpty()) {
                            dao.delete(new YvsGrhAssurance(a1.getId()));
                            listAssuranceEmploye.remove(a1);
                        }
                    }
                    if (listAssuranceEmploye.isEmpty()) {
                        listAssurance.remove(a);
                    }
                    listSelectAssuranceEmploye.clear();
                }
                succes();
            }
        }
        setSelectAssurance(false);
        setOptionUpdate(false);
        resetPage();
        update("form_assurance_00");
        update("entete_assurance_00");
    }

    @Override
    public void resetFiche() {
        resetFiche(employe);
        employe.setAssurances(new ArrayList<Assurance>());
        setDisplayTxtEmploye(false);
        setUpdateAssurance(false);
        setOptionSvtBilan(false);
        setOptionUpdate(false);
        setSelectAssurance(false);
        resetFicheAssurance();
        listAssuranceEmploye.clear();
        listCouverture.clear();
        listPersCharge.clear();
        update("form_assurance_00");
    }

    public void resetFicheAssurance() {
        resetFiche(assurance);
        assurance.setCouvertureList(new ArrayList<Assurance>());
        assurance.setBilanAssuranceList(new ArrayList<BilanAssurance>());
        assurance.setEmploye(new Employe());
        assurance.setPersonneCharge(new PersonneEnCharge());
        assurance.setTypeAssurance(new TypeAssurance());
        resetPage();
    }

    public void resetFicheAssuranceEmploye() {
        assurance.setTypeAssurance(new TypeAssurance());
        assurance.setReference(null);
        assurance.setTauxCotisation(0.0);
        assurance.setTauxCouverture(0.0);
        resetPageAssuranceEmploye();
    }

    public void resetFicheBilan() {
        resetFiche(bilan);
        bilan.setDateAssurance(new Date());
        bilan.setMontant(0.0);
        resetPageBilan();
        update("dlg_form_bilan_00");
    }

    public void resetPage() {
        if (listAssurance != null) {
            for (Assurance e : listAssurance) {
                listAssurance.get(listAssurance.indexOf(e)).setSelectActif(false);
            }
        }
        setDisplayTxtEmploye(false);
        listSelectAssurance.clear();
        listSelectCouverture.clear();
        listSelectPersCharge.clear();
        resetPageAssuranceEmploye();
    }

    public void resetPageAssuranceEmploye() {
        if (listAssuranceEmploye != null) {
            for (Assurance e : listAssuranceEmploye) {
                listAssuranceEmploye.get(listAssuranceEmploye.indexOf(e)).setSelectActif(false);
            }
        }
        setViewTabPersCharge(false);
        setUpdateAssuranceEmploye(false);
        setNameBtnAddAssurance((isUpdateAssuranceEmploye()) ? "Modifier" : "Ajouter");
        listSelectAssuranceEmploye.clear();
    }

    public void resetPageBilan() {
        if (listBilan != null) {
            for (BilanAssurance e : listBilan) {
                listBilan.get(listBilan.indexOf(e)).setSelectActif(false);
            }
        }
        setOptionUpdateBilan(false);
        setSelectBilan(false);
        listSelectBilan.clear();
    }

    public void choixEmploye1(Employe ev) {
        if (ev != null) {
            assurance.setEmploye(employe);
            populateViewEmploye(ev);
            update("form_assurance_00");
        }
    }

    public void choixTypeAssurance(ValueChangeEvent ev) {
        int type = ((int) ev.getNewValue());
        if (type == -1) {

        } else if (type > 0) {
            boolean trouv = false;
            TypeAssurance t = new TypeAssurance(type, listTypeAssurance.get(listTypeAssurance.indexOf(new TypeAssurance(type))).getLibelle());
            for (Assurance a : listAssuranceEmploye) {
                if (a.getTypeAssurance().equals(t)) {
                    trouv = true;
                    break;
                }
            }
            if (!trouv) {
                assurance.setTypeAssurance(t);
            } else {
                assurance.setTypeAssurance(new TypeAssurance());
                getMessage("Vous avez deja attribué cette assurance", FacesMessage.SEVERITY_ERROR);
                setError(true);
                update("blog_assurance_01");
            }
        }
    }

    public void addAssuranceEmploye() {
        if (controleFiche(assurance)) {
            Assurance a = new Assurance();
            if (!isUpdateAssuranceEmploye()) {
                assurance.setId(idTemp);
                while (listAssuranceEmploye.contains(assurance)) {
                    idTemp += 1;
                    assurance.setId(idTemp);
                }
                cloneObject(a, assurance);
                a.setSelectActif(false);
                listAssuranceEmploye.add(a);
            } else {
                cloneObject(a, assurance);
                listAssuranceEmploye.set(listAssuranceEmploye.indexOf(assurance), a);
            }
            resetFicheAssuranceEmploye();
            setUpdateAssuranceEmploye(false);
            setNameBtnAddAssurance((isUpdateAssuranceEmploye()) ? "Modifier" : "Ajouter");
            update("blog_assurance_01");
        }
    }

    public void deleteAssuranceEmploye() {
        if (listSelectAssuranceEmploye != null) {
            if (!listSelectAssuranceEmploye.isEmpty()) {
                for (Assurance a : listSelectAssuranceEmploye) {
                    listAssuranceEmploye.remove(a);
                }
                resetFicheAssuranceEmploye();
                setUpdateAssuranceEmploye(false);
                setNameBtnAddAssurance((isUpdateAssuranceEmploye()) ? "Modifier" : "Ajouter");
                update("blog_assurance_01");
            }
        }
    }

    public void openDlgPersCharge() {
        listPersChargeAssurance.addAll(listPersCharge);
        if (!listPersChargeAssurance.isEmpty()) {
            listPersCharge.clear();
            for (PersonneEnCharge p : listPersChargeAssurance) {
                boolean trouv = false;
                if (!listCouverture.isEmpty()) {
                    for (Assurance a : listCouverture) {
                        if (a.getPersonneCharge().equals(p)) {
                            trouv = true;
                            break;
                        }
                    }
                }
                if (!trouv) {
                    listPersCharge.add(p);
                }
            }
            if (!listPersCharge.isEmpty()) {
                openDialog("dlgPersCharge");
            } else {
                getMessage("Toutes les personnes en charge sont deja affiliees pour cet assurance", FacesMessage.SEVERITY_ERROR);
            }
        } else {
            getMessage("L'employe n'a pas de personne en charge", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void checkUpdateCouverture() {
        setUpdateCouverture(true);
        openDialog("dlgTauxCouverturePers");
    }

    public void saveNewPersoCharge() {
        if (listSelectPersCharge != null) {
            if (!isUpdateCouverture()) {
                System.err.println("INSERT COUVERTURE PERSONNE EN CHARGE");
                for (PersonneEnCharge p : listSelectPersCharge) {
                    assurance.setPersonneCharge(p);
                    assurance.setTauxCouverture(couverture.getTauxCouverture());
                    YvsGrhCouverturePersonneCharge c = buildCouverture(assurance);
                    c.setId(null);
                    c = (YvsGrhCouverturePersonneCharge) dao.save1(c);
                    assurance.setId(c.getId());
                    Assurance a = new Assurance();
                    cloneObject(a, assurance);
                    listCouverture.add(a);
                }
            } else {
                System.err.println("UPDATE COUVERTURE PERSONNE EN CHARGE");
                for (Assurance p : listSelectCouverture) {
                    p.setTauxCouverture(couverture.getTauxCouverture());
                    YvsGrhCouverturePersonneCharge c = buildCouverture(p);
                    dao.update(c);
                    listCouverture.set(listCouverture.indexOf(p), p);
                }
            }
            setViewTabPersCharge(!listCouverture.isEmpty());
            closeDialog("dlgPersCharge");
            update("blog_assurance_01");
            succes();
        }
    }

    public void openDlgEditBilan() {
        setVueListeBilan(false);
        setNameBtnDlgVueBilan((isVueListeBilan()) ? "Nouveau" : "Liste");
        resetFicheBilan();
        openDialog("dlgBilanCouverture");
        update("dlg_entete_bilan_00");
        update("dlg_form_bilan_00");
    }

    public void openDlgListeBilan() {
        setVueListeBilan(true);
        setNameBtnDlgVueBilan((isVueListeBilan()) ? "Nouveau" : "Liste");
        resetFicheBilan();
        openDialog("dlgBilanCouverture");
        update("dlg_entete_bilan_00");
        update("dlg_form_bilan_00");
    }

    public void changeVueDlgBilan() {
        setVueListeBilan(!isVueListeBilan());
        setNameBtnDlgVueBilan((isVueListeBilan()) ? "Nouveau" : "Liste");
        resetFicheBilan();
        update("dlg_entete_bilan_00");
        update("dlg_form_bilan_00");
    }

    public void checkVueDlgBilan() {
        setVueListeBilan(false);
        setNameBtnDlgVueBilan((isVueListeBilan()) ? "Nouveau" : "Liste");
        setSelectBilan(false);
        update("dlg_entete_bilan_00");
        update("dlg_form_bilan_00");
    }

    public void saveNewBilan() {
        bilan.setAssurance(assuranceSelect);
        if (controleFicheBilan(bilan)) {
            if (!isUpdateBilan()) {
                if (isVuePersCharge()) {
                    YvsGrhBilanCouverture b = buildBilanCouverture(bilan);
                    b.setId(null);
                    b = (YvsGrhBilanCouverture) dao.save1(b);
                    bilan.setId(b.getId());
                    BilanAssurance a = new BilanAssurance();
                    cloneObject(a, bilan);
                    listBilan.add(a);
                } else {
                    YvsGrhBilanAssurance b = buildBilanAssurance(bilan);
                    b.setId(null);
                    b = (YvsGrhBilanAssurance) dao.save1(b);
                    bilan.setId(b.getId());
                    BilanAssurance a = new BilanAssurance();
                    cloneObject(a, bilan);
                    listBilan.add(a);
                }
            } else {
                if (isVuePersCharge()) {
                    YvsGrhBilanCouverture b = buildBilanCouverture(bilan);
                    b.setId(null);
                    dao.save1(b);
                    BilanAssurance a = new BilanAssurance();
                    cloneObject(a, bilan);
                    listBilan.set(listBilan.indexOf(bilan), a);
                } else {
                    YvsGrhBilanAssurance b = buildBilanAssurance(bilan);
                    b.setId(null);
                    dao.save1(b);
                    BilanAssurance a = new BilanAssurance();
                    cloneObject(a, bilan);
                    listBilan.set(listBilan.indexOf(bilan), a);
                }
            }
            setUpdateBilan(true);
            succes();
            update("dlg_form_bilan_00");
        }
    }

    public void deleteBeanBilan() {
        if (listSelectBilan != null) {
            if (!listSelectBilan.isEmpty()) {
                if (isVuePersCharge()) {
                    for (BilanAssurance b : listSelectBilan) {
                        dao.delete(new YvsGrhBilanCouverture(b.getId()));
                        listBilan.remove(b);
                    }
                } else {
                    for (BilanAssurance b : listSelectBilan) {
                        dao.delete(new YvsGrhBilanAssurance(b.getId()));
                        listBilan.remove(b);
                    }
                }
                succes();
                resetFicheBilan();
                update("dlg_entete_bilan_00");
                update("dlg_form_bilan_00");
            }
        }
    }

    public void chechError() {
        if (isError()) {
            update("blog_assurance_01");
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie.noteDeFrais;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
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
import yvs.dao.Options;
import yvs.entity.grh.activite.YvsGrhFormation;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.param.poste.YvsGrhDepartement;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.salaire.YvsGrhCentreDepense;
import yvs.entity.grh.salaire.YvsGrhDepenseNote;
import yvs.entity.grh.salaire.YvsGrhDepenseNotePK;
import yvs.entity.grh.salaire.YvsGrhNotesFrais;
import yvs.entity.grh.salaire.YvsGrhTypeDepense;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import yvs.util.Managed;

/**
 *
 * @author LYMYTZ-PC
 * @param <T>
 */
@SessionScoped
@ManagedBean
public class ManagedNoteDeFrais<T> extends Managed<NoteDeFrais, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{noteDeFrais}")
    private NoteDeFrais note;
    private List<NoteDeFrais> listNote, listSelectNote;
    private TypeDepense typeDepense = new TypeDepense();
    private List<TypeDepense> listTypeDepense, listSelectTypeDepense;
    private Depenses depense = new Depenses();
    private List<Depenses> listDepense, listSelectDepense;
    private CentreDepense centre = new CentreDepense();
    private List<CentreDepense> listCentreDepense;
    private List<ElementAnalytique> listEltAnalytique;
    @ManagedProperty(value = "#{MEmps}")
    private ManagedEmployes managedEmps;
    private Employe employe = new Employe();

    private String nameVueDialogType = "Nouveau", nameVuePage = "Nouveau", nameTableauNote = "Liste des notes", nameBtnAddDepense = "Ajouter",
            nameFaireSuivre = "Faire Suivre";

    private Double totalMontant, totalMontantApprouve;

    private boolean viewPageNote, viewSelect, viewSelectType, vueListe = true, vueListeType = true, optionUpdate, updateTypeDepense,
            updateDepense, updateNote, updateCentre, deleteDepenseNote, faireSuivre,
            soumis = false, approuver = false, refuser = false, payer = false, relancer = false, annuler = false;

    private YvsGrhTypeDepense entityTypedepense;
    private YvsGrhNotesFrais entityNoteFrais;
    private YvsGrhCentreDepense entityCentre;
    private YvsGrhDepenseNote entityDepense;

    public ManagedNoteDeFrais() {
        listNote = new ArrayList<>();
        listSelectNote = new ArrayList<>();
        listTypeDepense = new ArrayList<>();
        listSelectTypeDepense = new ArrayList<>();
        listCentreDepense = new ArrayList<>();
        listDepense = new ArrayList<>();
        listEltAnalytique = new ArrayList<>();
        listSelectDepense = new ArrayList<>();
    }

    public void setAnnuler(boolean annuler) {
        this.annuler = annuler;
    }

    public boolean isAnnuler() {
        return annuler;
    }

    public void setRelancer(boolean relancer) {
        this.relancer = relancer;
    }

    public boolean isRelancer() {
        return relancer;
    }

    public void setPayer(boolean payer) {
        this.payer = payer;
    }

    public boolean isPayer() {
        return payer;
    }

    public boolean isApprouver() {
        return approuver;
    }

    public void setApprouver(boolean approuver) {
        this.approuver = approuver;
    }

    public boolean isRefuser() {
        return refuser;
    }

    public void setRefuser(boolean refuser) {
        this.refuser = refuser;
    }

    public boolean isSoumis() {
        return soumis;
    }

    public void setSoumis(boolean soumis) {
        this.soumis = soumis;
    }

    public Double getTotalMontant() {
        return totalMontant;
    }

    public void setTotalMontant(Double totalMontant) {
        this.totalMontant = totalMontant;
    }

    public Double getTotalMontantApprouve() {
        return totalMontantApprouve;
    }

    public void setTotalMontantApprouve(Double totalMontantApprouve) {
        this.totalMontantApprouve = totalMontantApprouve;
    }

    public String getNameFaireSuivre() {
        return nameFaireSuivre;
    }

    public void setNameFaireSuivre(String nameFaireSuivre) {
        this.nameFaireSuivre = nameFaireSuivre;
    }

    public boolean isFaireSuivre() {
        return faireSuivre;
    }

    public void setFaireSuivre(boolean faireSuivre) {
        this.faireSuivre = faireSuivre;
    }

    public boolean isDeleteDepenseNote() {
        return deleteDepenseNote;
    }

    public void setDeleteDepenseNote(boolean deleteDepenseNote) {
        this.deleteDepenseNote = deleteDepenseNote;
    }

    public String getNameVueDialogType() {
        return nameVueDialogType;
    }

    public void setNameVueDialogType(String nameVueDialogType) {
        this.nameVueDialogType = nameVueDialogType;
    }

    public boolean isViewSelectType() {
        return viewSelectType;
    }

    public void setViewSelectType(boolean viewSelectType) {
        this.viewSelectType = viewSelectType;
    }

    public boolean isVueListeType() {
        return vueListeType;
    }

    public void setVueListeType(boolean vueListeType) {
        this.vueListeType = vueListeType;
    }

    public boolean isUpdateCentre() {
        return updateCentre;
    }

    public List<NoteDeFrais> getListSelectNote() {
        return listSelectNote;
    }

    public void setListSelectNote(List<NoteDeFrais> listSelectNote) {
        this.listSelectNote = listSelectNote;
    }

    public void setUpdateCentre(boolean updateCentre) {
        this.updateCentre = updateCentre;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public boolean isUpdateNote() {
        return updateNote;
    }

    public void setUpdateNote(boolean updateNote) {
        this.updateNote = updateNote;
    }

    public String getNameBtnAddDepense() {
        return nameBtnAddDepense;
    }

    public void setNameBtnAddDepense(String nameBtnAddDepense) {
        this.nameBtnAddDepense = nameBtnAddDepense;
    }

    public boolean isUpdateDepense() {
        return updateDepense;
    }

    public void setUpdateDepense(boolean updateDepense) {
        this.updateDepense = updateDepense;
    }

    public List<Depenses> getListSelectDepense() {
        return listSelectDepense;
    }

    public void setListSelectDepense(List<Depenses> listSelectDepense) {
        this.listSelectDepense = listSelectDepense;
    }

    public List<ElementAnalytique> getListEltAnalytique() {
        return listEltAnalytique;
    }

    public void setListEltAnalytique(List<ElementAnalytique> listEltAnalytique) {
        this.listEltAnalytique = listEltAnalytique;
    }

    public String getNameTableauNote() {
        return nameTableauNote;
    }

    public void setNameTableauNote(String nameTableauNote) {
        this.nameTableauNote = nameTableauNote;
    }

    public boolean isUpdateTypeDepense() {
        return updateTypeDepense;
    }

    public void setUpdateTypeDepense(boolean updateTypeDepense) {
        this.updateTypeDepense = updateTypeDepense;
    }

    public boolean isOptionUpdate() {
        return optionUpdate;
    }

    public void setOptionUpdate(boolean optionUpdate) {
        this.optionUpdate = optionUpdate;
    }

    public List<TypeDepense> getListSelectTypeDepense() {
        return listSelectTypeDepense;
    }

    public void setListSelectTypeDepense(List<TypeDepense> listSelectTypeDepense) {
        this.listSelectTypeDepense = listSelectTypeDepense;
    }

    public boolean isViewPageNote() {
        return viewPageNote;
    }

    public void setViewPageNote(boolean viewPageNote) {
        this.viewPageNote = viewPageNote;
    }

    public String getNameVuePage() {
        return nameVuePage;
    }

    public void setNameVuePage(String nameVuePage) {
        this.nameVuePage = nameVuePage;
    }

    public boolean isVueListe() {
        return vueListe;
    }

    public void setVueListe(boolean vueListe) {
        this.vueListe = vueListe;
    }

    public boolean isViewSelect() {
        return viewSelect;
    }

    public void setViewSelect(boolean viewSelect) {
        this.viewSelect = viewSelect;
    }

    public List<TypeDepense> getListTypeDepense() {
        return listTypeDepense;
    }

    public void setListTypeDepense(List<TypeDepense> listTypeDepense) {
        this.listTypeDepense = listTypeDepense;
    }

    public List<Depenses> getListDepense() {
        return listDepense;
    }

    public void setListDepense(List<Depenses> listDepense) {
        this.listDepense = listDepense;
    }

    public List<CentreDepense> getListCentreDepense() {
        return listCentreDepense;
    }

    public void setListCentreDepense(List<CentreDepense> listCentreDepense) {
        this.listCentreDepense = listCentreDepense;
    }

    public YvsGrhTypeDepense getEntityTypedepense() {
        return entityTypedepense;
    }

    public void setEntityTypedepense(YvsGrhTypeDepense entityTypedepense) {
        this.entityTypedepense = entityTypedepense;
    }

    public YvsGrhNotesFrais getEntityNoteFrais() {
        return entityNoteFrais;
    }

    public void setEntityNoteFrais(YvsGrhNotesFrais entityNoteFrais) {
        this.entityNoteFrais = entityNoteFrais;
    }

    public YvsGrhCentreDepense getEntityCentre() {
        return entityCentre;
    }

    public void setEntityCentre(YvsGrhCentreDepense entityCentre) {
        this.entityCentre = entityCentre;
    }

    public YvsGrhDepenseNote getEntityDepense() {
        return entityDepense;
    }

    public void setEntityDepense(YvsGrhDepenseNote entityDepense) {
        this.entityDepense = entityDepense;
    }

    public Depenses getDepense() {
        return depense;
    }

    public void setDepense(Depenses depense) {
        this.depense = depense;
    }

    public CentreDepense getCentre() {
        return centre;
    }

    public void setCentre(CentreDepense centre) {
        this.centre = centre;
    }

    public ManagedEmployes getManagedEmps() {
        return managedEmps;
    }

    public void setManagedEmps(ManagedEmployes managedEmps) {
        this.managedEmps = managedEmps;
    }

    public NoteDeFrais getNote() {
        return note;
    }

    public void setNote(NoteDeFrais note) {
        this.note = note;
    }

    public List<NoteDeFrais> getListNote() {
        return listNote;
    }

    public void setListNote(List<NoteDeFrais> listNote) {
        this.listNote = listNote;
    }

    public TypeDepense getTypeDepense() {
        return typeDepense;
    }

    public void setTypeDepense(TypeDepense typeDepense) {
        this.typeDepense = typeDepense;
    }

    @Override
    public boolean controleFiche(NoteDeFrais bean) {
        if (bean.getDate() == null) {
            getMessage("Vous devez entrer la date", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getDescription() == null) {
            getMessage("Vous devez entrer la description", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getEmploye().getId() == 0) {
            getMessage("Vous devez identifier l'employe", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    public boolean controleFicheCentre(CentreDepense bean) {
        if (bean.getIdSource() == 0) {
            getMessage("Vous devez selectionner un element de la source", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getSource() == null) {
            getMessage("Vous devez indiquer lq source", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    public boolean controleFicheTypeDepense(TypeDepense bean) {
        if (bean.getTypeDepense() == null) {
            getMessage("Vous devez entrer le type de depense", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getMarge() == 0) {
            getMessage("Vous devez entrer le montant possible", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getMontant() == 0) {
            getMessage("Vous devez entrer la marge acceptable", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    public boolean controleFicheDepense(Depenses bean) {
        if (bean.getTypeDeDepense().getId() == 0) {
            getMessage("Vous devez selectionner une depense", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getMontantApprouve() == 0) {
            getMessage("Vous devez entrer le montant approuve", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getMontant() == 0) {
            getMessage("Vous devez entrer le montant de la note", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NoteDeFrais recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public CentreDepense recopieViewCentre(CentreDepense bean) {
        CentreDepense c = new CentreDepense();

        return c;
    }

    @Override
    public void populateView(NoteDeFrais bean) {
        if (bean != null) {
            listDepense.clear();
            cloneObject(note, bean);
            entityNoteFrais = buildNoteDeFrais(bean);
            listDepense.addAll(note.getDepenses());
            if (note.getCentreAnal() != null && note.getCentreAnal().getId() != 0) {
                switch (note.getCentreAnal().getNameSource()) {
                    case "Départements":
                        loadAllForDepartement();
                        centre.setId(1);
                        centre.setSource(new YvsGrhDepartement());
                        centre.setNameSource("Départements");
                        break;
                    case "Missions":
                        loadAllForMissions();
                        centre.setId(2);
                        centre.setSource(new YvsGrhMissions());
                        centre.setNameSource("Missions");
                        break;
                    case "Formations":
                        loadAllForFormation();
                        centre.setId(3);
                        centre.setSource(new YvsGrhFormation());
                        centre.setNameSource("Formations");
                        break;
                    default:
                        listEltAnalytique.clear();
                        centre.setSource(new Object());
                        centre.setNameSource(null);
                        break;
                }
                centre.setIdSource(note.getCentreAnal().getIdSource());
                centre.setSource(note.getCentreAnal().getSource());
            }
            activeStatut(note);
        }
    }

    public void activeStatut(NoteDeFrais bean) {
        switch (bean.getStatut()) {
            case "Créee":
                resetAllStatut();
                setSoumis(true);
                break;
            case "Soumise":
                resetAllStatut();
                setApprouver(true);
                setRefuser(true);
                break;
            case "Approuvée":
                resetAllStatut();
                setAnnuler(true);
                setPayer(true);
                break;
            case "Refusée":
                resetAllStatut();
                setRelancer(true);
                break;
            case "Payée":
                resetAllStatut();
                break;
        }
    }

    public void resetAllStatut() {
        setSoumis(false);
        setApprouver(false);
        setRefuser(false);
        setPayer(false);
        setRelancer(false);
        setAnnuler(false);
    }

    public void populateViewTypeDepense(TypeDepense bean) {
        cloneObject(typeDepense, bean);
    }

    public void populateViewDepense(Depenses bean) {
        cloneObject(depense, bean);
    }

    public void populateViewEmploye(Employe bean) {
        employe.setId(bean.getId());
        employe.setNom(bean.getNom());
        employe.setPrenom(bean.getPrenom());
        employe.setCodeEmploye(bean.getCodeEmploye());
        employe.setMatricule(bean.getMatricule());
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        NoteDeFrais bean = (NoteDeFrais) ev.getObject();
        selectNoteFrais(bean);
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        NoteDeFrais bean = (NoteDeFrais) ev.getObject();
        selectNoteFrais(bean);
    }

    public void loadOnViewTypeDepense(SelectEvent ev) {
        TypeDepense bean = (TypeDepense) ev.getObject();
        selectTypeDepense(bean);
    }

    public void loadOnViewTypeDepense2(SelectEvent ev) {
        TypeDepense bean = (TypeDepense) ev.getObject();
        cloneObject(depense.getTypeDeDepense(), bean);
//        setViewSelectType(true);
        closeDialog("dlgTypeDepense");
        update("panel-depense-note");
    }

    public void unLoadOnViewTypeDepense(UnselectEvent ev) {
        resetFiche(typeDepense);
    }

    public void unLoadOnViewTypeDepense2(UnselectEvent ev) {
        TypeDepense bean = (TypeDepense) ev.getObject();
        selectTypeDepense(bean);
    }

    public void loadOnViewDepense(SelectEvent ev) {
        Depenses bean = (Depenses) ev.getObject();
        selectDepense(bean);
    }

    public void unLoadOnViewDepense(UnselectEvent ev) {
        Depenses bean = (Depenses) ev.getObject();
        selectDepense(bean);
    }

    public void loadViewEmploye(SelectEvent ev) {
        YvsGrhEmployes e = (YvsGrhEmployes) ev.getObject();
        if (e != null) {
            populateViewEmploye(UtilGrh.buildBeanSimplePartialEmploye(e));
            note.setEmploye(UtilGrh.buildBeanSimplePartialEmploye(e));
        }
        closeDialog("dlgEmploye");
        update("form-note-01");
    }

    @Override
    public void loadAll() {
        listNote.clear();
        champ = new String[]{"agence"};
        val = new Object[]{currentAgence};
        listNote = UtilGrh.buildBeanListTypeNoteDeFrais(dao.loadNameQueries("YvsGrhNotesFrais.findAll", champ, val));
    }

    public void loadAllTypeDepense() {
        listTypeDepense.clear();
        String[] champ = new String[]{"societe"};
        Object[] val = new Object[]{currentAgence.getSociete()};
        listTypeDepense = UtilGrh.buildBeanListTypeDepense(dao.loadNameQueries("YvsGrhTypeDepense.findBySociete", champ, val));
    }

    public void loadAllForMissions() {
        listEltAnalytique.clear();
        String[] champ = new String[]{"societe"};
        Object[] val = new Object[]{currentAgence.getSociete()};
        List<YvsGrhMissions> list = (List<YvsGrhMissions>) dao.loadNameQueries("YvsMissions.findBySociete", champ, val);
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhMissions l : list) {
                    ElementAnalytique elt = new ElementAnalytique(l.getId());
                    elt.setLibelle(l.getOrdre());
                    listEltAnalytique.add(elt);
                }
            }
        }
    }

    public void loadAllForFormation() {
        listEltAnalytique.clear();
        String[] champ = new String[]{"societe"};
        Object[] val = new Object[]{currentAgence.getSociete()};
        List<YvsGrhFormation> list = (List<YvsGrhFormation>) dao.loadNameQueries("YvsFormation.findAll", champ, val);
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhFormation l : list) {
                    ElementAnalytique elt = new ElementAnalytique(l.getId());
                    elt.setLibelle(l.getLibelle());
                    listEltAnalytique.add(elt);
                }
            }
        }
    }

    public void loadAllForDepartement() {
        listEltAnalytique.clear();
        String[] champ = new String[]{"agence"};
        Object[] val = new Object[]{currentAgence};
        List<YvsGrhDepartement> list = (List<YvsGrhDepartement>) dao.loadNameQueries("YvsGrhDepartement.findByAgence", champ, val);
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhDepartement l : list) {
                    ElementAnalytique elt = new ElementAnalytique(l.getId());
                    elt.setLibelle(l.getIntitule());
                    listEltAnalytique.add(elt);
                }
            }
        }
    }

    @Override
    public boolean saveNew() {
        if (isViewPageNote()) {
            saveNewNoteFrais();
        } else {
            saveNewTypeDepense();
        }
        return true;
    }

    public boolean saveNewTypeDepense() {
        if (controleFicheTypeDepense(typeDepense)) {
            if (!isUpdateTypeDepense()) {
                entityTypedepense = buildTypeDepense(typeDepense);
                entityTypedepense.setId(null);
                entityTypedepense = (YvsGrhTypeDepense) dao.save1(entityTypedepense);
                typeDepense.setId(entityTypedepense.getId());
                TypeDepense dep = new TypeDepense();
                cloneObject(dep, typeDepense);
                listTypeDepense.add(dep);
            } else {
                entityTypedepense = buildTypeDepense(typeDepense);
                entityTypedepense = (YvsGrhTypeDepense) dao.update(entityTypedepense);
                TypeDepense dep = new TypeDepense();
                cloneObject(dep, typeDepense);
                listTypeDepense.set(listTypeDepense.indexOf(typeDepense), dep);
            }
            if (isViewPageNote()) {
                cloneObject(depense.getTypeDeDepense(), typeDepense);
                closeDialog("dlgTypeDepense");
                update("panel-depense-note");
            }
            updateTypeDepense = true;
            succes();
        }
        return true;
    }

    public boolean saveNewNoteFrais() {
        if (!isUpdateCentre()) {
            entityCentre = buildCentreDepense(centre);
            entityCentre.setId(null);
            entityCentre = (YvsGrhCentreDepense) dao.save1(entityCentre);
            centre.setId(entityCentre.getId());
        } else {
            entityCentre = buildCentreDepense(centre);
            entityCentre = (YvsGrhCentreDepense) dao.update(entityCentre);
        }
        note.setCentreAnal(centre);
        if (controleFiche(note)) {
            if (!isUpdateNote()) {
                 note.setStatut("Créee");
                entityNoteFrais = buildNoteDeFrais(note);
                entityNoteFrais.setId(null);
                entityNoteFrais = (YvsGrhNotesFrais) dao.save1(entityNoteFrais);
                for (Depenses d : listDepense) {
                    entityDepense = buildDepenseNote(d);
                    entityDepense = (YvsGrhDepenseNote) dao.save1(entityDepense);
                    note.getDepenses().add(d);
                }
                note.setId(entityNoteFrais.getId());
                note.getCentreAnal().setNameSource(centre.getNameSource());
                NoteDeFrais not = new NoteDeFrais();
                cloneObject(not, note);
                CentreDepense c = new CentreDepense();
                cloneObject(c, note.getCentreAnal());
                not.setCentreAnal(c);
                listNote.add(not);
            } else {
                System.err.println("UPDATE NOTE");
                entityNoteFrais = buildNoteDeFrais(note);
                entityNoteFrais = (YvsGrhNotesFrais) dao.update(entityNoteFrais);
                List<Depenses> list = new ArrayList<>();
                list.addAll(note.getDepenses());
                for (Depenses d : list) {
                    if (!listDepense.contains(d)) {
                        entityDepense = buildDepenseNote(d);
                        dao.delete(entityDepense);
                        note.getDepenses().remove(d);
                    }
                }
                for (Depenses d : listDepense) {
                    entityDepense = buildDepenseNote(d);
                    entityDepense = (YvsGrhDepenseNote) dao.update(entityDepense);
                    if (note.getDepenses().contains(d)) {
                        System.err.println("-- UPDATE DEPENSE");
                        note.getDepenses().set(note.getDepenses().indexOf(d), d);
                    } else {
                        System.err.println("-- INSERT DEPENSE");
                        note.getDepenses().add(d);
                    }
                }
                note.getCentreAnal().setNameSource(centre.getNameSource());
                NoteDeFrais not = new NoteDeFrais();
                cloneObject(not, note);
                CentreDepense c = new CentreDepense();
                cloneObject(c, note.getCentreAnal());
                not.setCentreAnal(c);
                listNote.set(listNote.indexOf(note), not);
            }
            setUpdateTypeDepense(true);
            succes();
        }
        return true;
    }

    public void onPageNote(boolean etat) {
        setViewPageNote(etat);
        resetPage();
    }

    public void changeVuePage() {
        setViewSelect(false);
        setVueListe(!vueListe);
        setFaireSuivre(false);
        setUpdateTypeDepense(false);
        setUpdateNote(false);
        setUpdateDepense(false);
        setNameVuePage((isVueListe()) ? "Nouveau" : "Liste");
        setNameFaireSuivre((isFaireSuivre()) ? "Annuler" : "Faire Suivre");
        resetFiche();
    }

    public void changeVueDialogType() {
//        setViewSelect(false);
        setVueListeType(!vueListeType);
        setNameVueDialogType((isVueListeType()) ? "Nouveau" : "Liste");
        resetFicheTypeDepense();
        update("dialog-parma-not-00");
        update("dialog-parma-not-01");
    }

    public void selectTypeDepense(TypeDepense bean) {
        bean.setSelectActif(!bean.isSelectActif());
        listTypeDepense.get(listTypeDepense.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (!listSelectTypeDepense.contains(bean)) {
            listSelectTypeDepense.add(bean);
        } else {
            listSelectTypeDepense.remove(bean);
        }
        if (listSelectTypeDepense.isEmpty()) {
            resetFiche(typeDepense);
        } else {
            populateViewTypeDepense(listSelectTypeDepense.get(listSelectTypeDepense.size() - 1));
        }
        setUpdateTypeDepense(!listSelectTypeDepense.isEmpty());
        setViewSelect(!listSelectTypeDepense.isEmpty());
        setOptionUpdate(listSelectTypeDepense.size() == 1);
        update("entete-parma-note-00");
        update("blog-parma-note-00");
    }

    public void selectNoteFrais(NoteDeFrais bean) {
        listDepense.clear();
        bean.setSelectActif(!bean.isSelectActif());
        listNote.get(listNote.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (!listSelectNote.contains(bean)) {
            listSelectNote.add(bean);
        } else {
            listSelectNote.remove(bean);
        }
        if (listSelectNote.isEmpty()) {
            resetFiche(note);
            note.setCentreAnal(new CentreDepense());
            note.setDepenses(new ArrayList<Depenses>());
            note.setEmploye(new Employe());
        } else {
            populateView(listSelectNote.get(listSelectNote.size() - 1));
        }
        setUpdateNote(!listSelectNote.isEmpty());
        setUpdateCentre(isUpdateNote());
        setViewSelect(!listSelectNote.isEmpty());
        setOptionUpdate(listSelectNote.size() == 1);
        update("entete-note-00");
        update("blog-note-00");
    }

    public void selectDepense(Depenses bean) {
        if (listSelectDepense.isEmpty()) {
            resetFiche(depense);
            depense.setTypeDeDepense(new TypeDepense());
        } else {
            populateViewDepense(listSelectDepense.get(listSelectDepense.size() - 1));
        }
        setUpdateDepense(!listSelectDepense.isEmpty());
        setNameBtnAddDepense((isUpdateDepense()) ? "Modifier" : "Ajouer");
        update("blog-depense-note");
    }

    public YvsGrhTypeDepense buildTypeDepense(TypeDepense bean) {
        YvsGrhTypeDepense d = new YvsGrhTypeDepense();
        if (bean != null) {
            d.setId((int) bean.getId());
            d.setLibelle(bean.getTypeDepense());
            d.setMargeMontant(bean.getMarge());
            d.setSeuilMontant(bean.getMontant());
            d.setSociete(currentAgence.getSociete());
        }
        return d;
    }

    public YvsGrhNotesFrais buildNoteDeFrais(NoteDeFrais f) {
        YvsGrhNotesFrais n = new YvsGrhNotesFrais();
        if (f != null) {
            n.setId(f.getId());
            n.setCentreDepense(new YvsGrhCentreDepense(f.getCentreAnal().getId()));
            n.setDateNote(f.getDate());
            n.setDescription(f.getDescription());
            n.setStatut(f.getStatut());
            n.setEmploye(new YvsGrhEmployes(f.getEmploye().getId()));
        }
        return n;
    }

    public YvsGrhCentreDepense buildCentreDepense(CentreDepense d) {
        YvsGrhCentreDepense c = new YvsGrhCentreDepense();
        if (d != null) {
            c.setId(d.getId());
            c.setIdSource(d.getIdSource());
            c.setSource(d.getSource().getClass().getName());
        }
        return c;
    }

    public YvsGrhDepenseNote buildDepenseNote(Depenses d) {
        YvsGrhDepenseNote n = new YvsGrhDepenseNote();
        if (d != null) {
            n.setMontant(d.getMontant());
            n.setMontantApprouve(d.getMontantApprouve());
            n.setYvsGrhDepenseNotePK(new YvsGrhDepenseNotePK(entityNoteFrais.getId(), (int) d.getTypeDeDepense().getId()));
            n.setYvsGrhNotesFrais(entityNoteFrais);
            n.setYvsGrhTypeDepense(new YvsGrhTypeDepense((int) d.getTypeDeDepense().getId()));
        }
        return n;
    }

    @Override
    public void resetFiche() {
        resetPage();
        if (isViewPageNote()) {
            resetFicheNoteFrais();
            update("entete-note-00");
            update("blog-note-00");
        } else {
            resetFicheTypeDepense();
            update("entete-parma-note-00");
            update("blog-parma-note-00");
        }
    }

    public void resetFicheTypeDepense() {
        resetFiche(typeDepense);
        updateTypeDepense = false;
    }

    public void resetFicheDepense() {
        resetFiche(depense);
        depense.setTypeDeDepense(new TypeDepense());
        setUpdateDepense(false);
        setNameBtnAddDepense((isUpdateDepense()) ? "Modifier" : "Ajouer");
        update("blog-depense-note");
    }

    public void resetFicheCentre() {
        resetFiche(centre);
        centre.setSource(new Object());
        updateCentre = false;
    }

    public void resetFicheNoteFrais() {
        resetFiche(note);
        note.setCentreAnal(new CentreDepense());
        note.setDepenses(new ArrayList<Depenses>());
        note.setEmploye(new Employe());
        note.setDate(new Date());
        listDepense.clear();
        resetFicheCentre();
        resetFicheDepense();
        updateNote = false;
    }

    public void resetPage() {
        if (isViewPageNote()) {
            resetPageNoteFrais();
        } else {
            resetPageTypeDepense();
        }
        setViewSelect(false);
    }

    public void resetPageNoteFrais() {
        for (NoteDeFrais n : listNote) {
            listNote.get(listNote.indexOf(n)).setSelectActif(false);
        }
        listSelectNote.clear();
        listEltAnalytique.clear();
    }

    public void resetPageTypeDepense() {
        for (TypeDepense t : listTypeDepense) {
            listTypeDepense.get(listTypeDepense.indexOf(t)).setSelectActif(false);
        }
        listSelectTypeDepense.clear();
    }

    public void checkFaireSuivre() {
        setFaireSuivre(!isFaireSuivre());
        setTotalMontant(note.getTotalMontant());
        setTotalMontantApprouve(note.getTotalMontantApprouve());
        setNameFaireSuivre((isFaireSuivre()) ? "Annuler" : "Faire Suivre");
        update("entete-note-00");
        update("blog-note-00");
    }

    public void chechUpdateAll() {
        setViewSelect(false);
        setVueListe(false);
        setFaireSuivre(false);
        setNameVuePage((isVueListe()) ? "Nouveau" : "Liste");
        if (isViewPageNote()) {
            chechUpdateNote();
        } else {
            chechUpdateTypeDepense();
        }
    }

    public void chechUpdateTypeDepense() {
        update("entete-parma-note-00");
        update("blog-parma-note-00");
    }

    public void chechUpdateNote() {
        update("entete-note-00");
        update("blog-note-00");
    }

    @Override
    public void deleteBean() {
        if (isViewPageNote()) {
            deleteBeanNote();
        } else {
            deleteBeanTypeDepense();
        }
    }

    public boolean deleteBeanTypeDepense() {
        if (listSelectTypeDepense != null) {
            if (!listSelectTypeDepense.isEmpty()) {
                for (TypeDepense t : listSelectTypeDepense) {
                    entityTypedepense = buildTypeDepense(t);
                    dao.delete(entityTypedepense);
                    listTypeDepense.remove(t);
                }
                listSelectTypeDepense.clear();
                succes();
                setViewSelect(false);
                setVueListe(true);
                resetFicheTypeDepense();
                update("entete-parma-note-00");
                update("blog-parma-note-00");
            }
        }
        return true;
    }

    public boolean deleteBeanNote() {
        boolean etat = false;
        for (NoteDeFrais n : listSelectNote) {
            if (!n.getDepenses().isEmpty()) {
                etat = true;
                break;
            }
        }
        if (etat) {
            openDialog("dlgConfirmDeleteD");
        } else {
            openDialog("dlgConfirmDeleteN");
        }
        return etat;
    }

    public boolean deleteBeanNoteFrais() {
        if (listSelectNote != null) {
            if (!listSelectNote.isEmpty()) {
                for (NoteDeFrais t : listSelectNote) {
                    entityNoteFrais = buildNoteDeFrais(t);
                    dao.delete(entityNoteFrais);
                    listNote.remove(t);
                }
                listSelectNote.clear();
                succes();
                setViewSelect(false);
                setVueListe(true);
                resetFicheNoteFrais();
                update("entete-note-00");
                update("blog-note-00");
            }
        }
        return true;
    }

    public void deleteAllDepense() {
        for (NoteDeFrais bean : listSelectNote) {
            if (bean != null) {
                if (bean.getDepenses() != null) {
                    if (!bean.getDepenses().isEmpty()) {
                        for (Depenses d : bean.getDepenses()) {
                            entityDepense = buildDepenseNote(d);
                            dao.delete(entityDepense);
                        }
                    }
                }
                entityNoteFrais = buildNoteDeFrais(bean);
                dao.delete(entityNoteFrais);
                listNote.remove(bean);
            }
        }
        listSelectNote.clear();
        succes();
        setViewSelect(false);
        setVueListe(true);
        resetFicheNoteFrais();
        update("entete-note-00");
        update("blog-note-00");
    }

    public void choixCentreAnalytique(ValueChangeEvent ev) {
        long id = (long) ev.getNewValue();
        if (id == 0) {
            listEltAnalytique.clear();
            centre.setSource(new Object());
            centre.setNameSource(null);
        } else if (id == 1) {
            loadAllForDepartement();
            centre.setSource(new YvsGrhDepartement());
            centre.setNameSource("Départements");
        } else if (id == 2) {
            loadAllForMissions();
            centre.setSource(new YvsGrhMissions());
            centre.setNameSource("Missions");
        } else if (id == 3) {
            loadAllForFormation();
            centre.setSource(new YvsGrhFormation());
            centre.setNameSource("Formations");
        }
        update("select-analytique-note");
    }

    public void choixObjetAnalytique(ValueChangeEvent ev) {
        long id = (long) ev.getNewValue();
        centre.setIdSource(id);
    }

    public void choixEmploye1(YvsGrhEmployes ev) {
        if (ev != null) {
            note.setEmploye(UtilGrh.buildBeanSimplePartialEmploye(ev));
            update("form-note-01");
        }
    }

    public void choixTypeDepense(ValueChangeEvent ev) {
        if (ev != null && ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id == -1) {
                openDialog("dlgTypeDepense");
            } else {
                Depenses d = new Depenses();
                d.setTypeDeDepense(new TypeDepense(id));
                if (listDepense.contains(d)) {
                    getMessage("Il existe deja une depense a ce type", FacesMessage.SEVERITY_ERROR);
                    resetFiche(depense);
                    depense.setTypeDeDepense(new TypeDepense());
                }
                depense.setMontantApprouve(listTypeDepense.get(listTypeDepense.indexOf(d.getTypeDeDepense())).getMontant());
                update("panel-depense-note");
            }
        }
    }

    public void addDepenseNote() {
        if (controleFicheDepense(depense)) {
            depense.getTypeDeDepense().setTypeDepense(listTypeDepense.get(listTypeDepense.indexOf(depense.getTypeDeDepense())).getTypeDepense());
            if (!isUpdateDepense()) {
                if (!listDepense.contains(depense)) {
                    note.setTotalMontant(note.getTotalMontant() + depense.getMontant());
                    note.setTotalMontantApprouve(note.getTotalMontantApprouve() + depense.getMontantApprouve());
                    listDepense.add(depense);
                    depense = new Depenses();
                } else {

                }
            } else {
                note.setTotalMontant(note.getTotalMontant() + depense.getMontant() - listDepense.get(listDepense.indexOf(depense)).getMontant());
                note.setTotalMontantApprouve(note.getTotalMontantApprouve() + depense.getMontantApprouve() - listDepense.get(listDepense.indexOf(depense)).getMontantApprouve());
                listDepense.set(listDepense.indexOf(depense), depense);
                depense = new Depenses();
            }
            update("form-note-01");
        }
    }

    public void removeDepenseNote() {
        if (listSelectDepense != null) {
            if (!listSelectDepense.isEmpty()) {
                for (Depenses d : listSelectDepense) {
                    note.setTotalMontant(note.getTotalMontant() - d.getMontant());
                    note.setTotalMontantApprouve(note.getTotalMontantApprouve() - d.getMontantApprouve());
                    listDepense.remove(d);
                    depense = new Depenses();
                }
                setUpdateDepense(false);
                listSelectDepense.clear();
                setNameBtnAddDepense((isUpdateDepense()) ? "Modifier" : "Ajouer");
                update("form-note-01");
            }
        }
    }

    public void soumettreNote() {
        updateEtatNoteFrais("Soumise");
        activeStatut(note);
        update("form-note-02");
        update("entete-note-00");
    }

    public void approuverNote() {
        updateEtatNoteFrais("Approuvée");
        activeStatut(note);
        update("form-note-02");
        update("entete-note-00");
    }

    public void refuserNote() {
        updateEtatNoteFrais("Refusée");
        activeStatut(note);
        update("form-note-02");
        update("entete-note-00");
    }

    public void annulerNote() {
        updateEtatNoteFrais("Créee");
        activeStatut(note);
        update("form-note-02");
        update("entete-note-00");
    }

    public void relancerNote() {
        updateEtatNoteFrais("Créee");
        activeStatut(note);
        update("form-note-02");
        update("entete-note-00");
    }

    public void payerNote() {
        updateEtatNoteFrais("Payée");
        activeStatut(note);
        update("form-note-02");
        update("entete-note-00");
    }

    public void updateEtatNoteFrais(String statut) {
        String rq = "UPDATE yvs_grh_notes_frais SET statut='" + statut + "' WHERE id=?";
        Options[] param = new Options[]{new Options(note.getId(), 1)};
        dao.requeteLibre(rq, param);
        note.setStatut(statut);
        NoteDeFrais not = new NoteDeFrais();
        cloneObject(not, note);
        listNote.set(listNote.indexOf(note), not);
    }
}

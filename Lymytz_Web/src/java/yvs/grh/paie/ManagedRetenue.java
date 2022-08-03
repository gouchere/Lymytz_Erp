/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.UtilCompta;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.grh.contrat.YvsGrhComposantsRetenue;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.contrat.YvsGrhTypeElementAdditionel;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.salaire.YvsGrhCategorieElement;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.grh.salaire.YvsGrhElementSalaire;
import yvs.entity.grh.salaire.YvsGrhElementStructure;
import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
import yvs.entity.grh.salaire.YvsGrhStructureSalaire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsGrhPlanPrelevement;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.ContratEmploye;
import yvs.grh.bean.ElementAdditionnel;
import yvs.grh.bean.Employe;
import yvs.grh.bean.mission.ManagedMission;
import yvs.grh.bean.TypeElementAdd;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.parametrage.PlanPrelevement;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author Lymytz
 */
@SessionScoped
@ManagedBean
public class ManagedRetenue extends Managed implements Serializable {

    private List<YvsGrhPlanPrelevement> plansRetenues;
    private PlanPrelevement newPlanRetenu = new PlanPrelevement();
    private List<YvsGrhElementAdditionel> listRetenues, selectRetenues;
    private List<YvsGrhTypeElementAdditionel> listTypesElts;
    private YvsGrhElementAdditionel selectedRetenue;
    private List<YvsGrhComposantsRetenue> listComposantRetenue;
    private List<YvsGrhDetailPrelevementEmps> listPrelevement;
    private ElementAdditionnel elementAdd = new ElementAdditionnel();
    private YvsGrhElementAdditionel selectElement = new YvsGrhElementAdditionel();
    private TypeElementAdd typeElement = new TypeElementAdd();
    private boolean generer;
//    private List<PrelevementEmps> listDetail;
    private double dette = 0;
    YvsParametreGrh currentParam;

    private List<YvsBaseExercice> exercices;

    private boolean updateRetenu,
            retenuPlanifier, selectDetail, updateDetail,
            activebaseTaux = false, activebaseMontant = false, activebaseNombreMois = false,
            displayContrat = false, displayPlacement = false, updatePlanPrelevement;
    private List<Employe> listEmploye;
    private String chaineSelectPlan;
    private String chaineSelectPrelev;
    private Date debutPlanif = new Date();
    private List<YvsGrhDetailPrelevementEmps> listeRetenues;
    private PaginatorResult<YvsGrhDetailPrelevementEmps> p_prevelement = new PaginatorResult<>();

    private String employe, employeDetail;
    private Date debutF = new Date(), finF = new Date(), debutDetail, finDetail;
    private Character statut, statutDetail;
    private int planifier, planifierDetail;
    private double montant1, montant2, montant1Detail, montant2Detail;
    private Long typeRetenu, typeRetenuDetail, nbrComptaSearch, agenceSearch;
    private boolean paramDate, paramDateDetail;
    private Boolean comptaSearch;

    public ManagedRetenue() {
        listEmploye = new ArrayList<>();
        listTypesElts = new ArrayList<>();
        listRetenues = new ArrayList<>();
        plansRetenues = new ArrayList<>();
        listComposantRetenue = new ArrayList<>();
        listeRetenues = new ArrayList<>();
        listPrelevement = new ArrayList<>();
        exercices = new ArrayList<>();
    }

    public String getChaineSelectPrelev() {
        return chaineSelectPrelev;
    }

    public void setChaineSelectPrelev(String chaineSelectPrelev) {
        this.chaineSelectPrelev = chaineSelectPrelev;
    }

    public Long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(Long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public Long getNbrComptaSearch() {
        return nbrComptaSearch;
    }

    public void setNbrComptaSearch(Long nbrComptaSearch) {
        this.nbrComptaSearch = nbrComptaSearch;
    }

    public Boolean getComptaSearch() {
        return comptaSearch;
    }

    public void setComptaSearch(Boolean comptaSearch) {
        this.comptaSearch = comptaSearch;
    }

    public List<YvsBaseExercice> getExercices() {
        return exercices;
    }

    public void setExercices(List<YvsBaseExercice> exercices) {
        this.exercices = exercices;
    }

    public boolean isUpdatePlan() {
        return updatePlan;
    }

    public void setUpdatePlan(boolean updatePlan) {
        this.updatePlan = updatePlan;
    }

    public int getIndexEmp() {
        return indexEmp;
    }

    public void setIndexEmp(int indexEmp) {
        this.indexEmp = indexEmp;
    }

    public boolean isUpdateTypeRetenue() {
        return updateTypeRetenue;
    }

    public void setUpdateTypeRetenue(boolean updateTypeRetenue) {
        this.updateTypeRetenue = updateTypeRetenue;
    }

    public boolean isUpdateComposant() {
        return updateComposant;
    }

    public void setUpdateComposant(boolean updateComposant) {
        this.updateComposant = updateComposant;
    }

    public Date getDe() {
        return de;
    }

    public void setDe(Date de) {
        this.de = de;
    }

    public Date getFi() {
        return fi;
    }

    public void setFi(Date fi) {
        this.fi = fi;
    }

    public String getEmployeDetail() {
        return employeDetail;
    }

    public void setEmployeDetail(String employeDetail) {
        this.employeDetail = employeDetail;
    }

    public Date getDebutDetail() {
        return debutDetail;
    }

    public void setDebutDetail(Date debutDetail) {
        this.debutDetail = debutDetail;
    }

    public Date getFinDetail() {
        return finDetail;
    }

    public void setFinDetail(Date finDetail) {
        this.finDetail = finDetail;
    }

    public Character getStatutDetail() {
        return statutDetail;
    }

    public void setStatutDetail(Character statutDetail) {
        this.statutDetail = statutDetail;
    }

    public int getPlanifierDetail() {
        return planifierDetail;
    }

    public void setPlanifierDetail(int planifierDetail) {
        this.planifierDetail = planifierDetail;
    }

    public double getMontant1Detail() {
        return montant1Detail;
    }

    public void setMontant1Detail(double montant1Detail) {
        this.montant1Detail = montant1Detail;
    }

    public double getMontant2Detail() {
        return montant2Detail;
    }

    public void setMontant2Detail(double montant2Detail) {
        this.montant2Detail = montant2Detail;
    }

    public Long getTypeRetenuDetail() {
        return typeRetenuDetail;
    }

    public void setTypeRetenuDetail(Long typeRetenuDetail) {
        this.typeRetenuDetail = typeRetenuDetail;
    }

    public boolean isParamDateDetail() {
        return paramDateDetail;
    }

    public void setParamDateDetail(boolean paramDateDetail) {
        this.paramDateDetail = paramDateDetail;
    }

    public List<ParametreRequete> getParams() {
        return params;
    }

    public void setParams(List<ParametreRequete> params) {
        this.params = params;
    }

    public boolean isParamDate() {
        return paramDate;
    }

    public void setParamDate(boolean paramDate) {
        this.paramDate = paramDate;
    }

    public String getEmploye() {
        return employe;
    }

    public void setEmploye(String employe) {
        this.employe = employe;
    }

    public Date getDebutF() {
        return debutF;
    }

    public void setDebutF(Date debutF) {
        this.debutF = debutF;
    }

    public Date getFinF() {
        return finF;
    }

    public void setFinF(Date finF) {
        this.finF = finF;
    }

    public Character getStatut() {
        return statut;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public int getPlanifier() {
        return planifier;
    }

    public void setPlanifier(int planifier) {
        this.planifier = planifier;
    }

    public double getMontant1() {
        return montant1;
    }

    public void setMontant1(double montant1) {
        this.montant1 = montant1;
    }

    public double getMontant2() {
        return montant2;
    }

    public void setMontant2(double montant2) {
        this.montant2 = montant2;
    }

    public Long getTypeRetenu() {
        return typeRetenu;
    }

    public void setTypeRetenu(Long typeRetenu) {
        this.typeRetenu = typeRetenu;
    }

    public PaginatorResult<YvsGrhDetailPrelevementEmps> getP_prevelement() {
        return p_prevelement;
    }

    public void setP_prevelement(PaginatorResult<YvsGrhDetailPrelevementEmps> p_prevelement) {
        this.p_prevelement = p_prevelement;
    }

    public List<YvsGrhDetailPrelevementEmps> getListPrelevement() {
        return listPrelevement;
    }

    public void setListPrelevement(List<YvsGrhDetailPrelevementEmps> listPrelevement) {
        this.listPrelevement = listPrelevement;
    }

    public YvsGrhElementAdditionel getSelectElement() {
        return selectElement;
    }

    public void setSelectElement(YvsGrhElementAdditionel selectElement) {
        this.selectElement = selectElement;
    }

    public List<YvsGrhDetailPrelevementEmps> getListeRetenues() {
        return listeRetenues;
    }

    public void setListeRetenues(List<YvsGrhDetailPrelevementEmps> listeRetenues) {
        this.listeRetenues = listeRetenues;
    }

    public void setDebutPlanif(Date debutPlanif) {
        this.debutPlanif = debutPlanif;
    }

    public Date getDebutPlanif() {
        return debutPlanif;
    }

    public List<YvsGrhComposantsRetenue> getListComposantRetenue() {
        return listComposantRetenue;
    }

    public void setListComposantRetenue(List<YvsGrhComposantsRetenue> listComposantRetenue) {
        this.listComposantRetenue = listComposantRetenue;
    }

    public YvsGrhElementAdditionel getSelectedRetenue() {
        return selectedRetenue;
    }

    public void setSelectedRetenue(YvsGrhElementAdditionel selectedRetenue) {
        this.selectedRetenue = selectedRetenue;
    }

    public PlanPrelevement getNewPlanRetenu() {
        return newPlanRetenu;
    }

    public void setNewPlanRetenu(PlanPrelevement newPlanRetenu) {
        this.newPlanRetenu = newPlanRetenu;
    }

    public List<YvsGrhPlanPrelevement> getPlansRetenues() {
        return plansRetenues;
    }

    public void setPlansRetenues(List<YvsGrhPlanPrelevement> plansRetenues) {
        this.plansRetenues = plansRetenues;
    }

    public String getChaineSelectPlan() {
        return chaineSelectPlan;
    }

    public void setChaineSelectPlan(String chaineSelectPlan) {
        this.chaineSelectPlan = chaineSelectPlan;
    }

    public List<Employe> getListEmploye() {
        return listEmploye;
    }

    public void setListEmploye(List<Employe> listEmploye) {
        this.listEmploye = listEmploye;
    }

    public void setSelectDetail(boolean selectDetail) {
        this.selectDetail = selectDetail;
    }

    public void setUpdateDetail(boolean updateDetail) {
        this.updateDetail = updateDetail;
    }

    public boolean isSelectDetail() {
        return selectDetail;
    }

    public boolean isUpdateDetail() {
        return updateDetail;
    }

    public boolean isRetenuPlanifier() {
        return retenuPlanifier;
    }

    public void setRetenuPlanifier(boolean retenuPlanifier) {
        this.retenuPlanifier = retenuPlanifier;
    }

    public void setUpdatePlanPrelevement(boolean updatePlanPrelevement) {
        this.updatePlanPrelevement = updatePlanPrelevement;
    }

    public boolean isUpdatePlanPrelevement() {
        return updatePlanPrelevement;
    }
// public void setDefautlPlan(PlanPrelevement defautlPlan) {
//        this.defautlPlan = defautlPlan;
//    }
//    public PlanPrelevement getDefautlPlan() {
//        return defautlPlan;
//    }

    public void setDisplayContrat(boolean displayContrat) {
        this.displayContrat = displayContrat;
    }

    public void setDisplayPlacement(boolean displayPlacement) {
        this.displayPlacement = displayPlacement;
    }

    public boolean isDisplayPlacement() {
        return displayPlacement;
    }

    public boolean isDisplayContrat() {
        return displayContrat;
    }

    public void setDette(double dette) {
        this.dette = dette;
    }

    public double getDette() {
        return dette;
    }

    public boolean isActivebaseTaux() {
        return activebaseTaux;
    }

//    public void setEmploye(Employe employe) {
//        this.employe = employe;
//    }
//
//    public Employe getEmploye() {
//        return employe;
//    }
    public void setActivebaseTaux(boolean activebaseTaux) {
        this.activebaseTaux = activebaseTaux;
    }

    public boolean isActivebaseMontant() {
        return activebaseMontant;
    }

    public void setActivebaseMontant(boolean activebaseMontant) {
        this.activebaseMontant = activebaseMontant;
    }

    public boolean isActivebaseNombreMois() {
        return activebaseNombreMois;
    }

    public void setActivebaseNombreMois(boolean activebaseNombreMois) {
        this.activebaseNombreMois = activebaseNombreMois;
    }

    public boolean isUpdateRetenu() {
        return updateRetenu;
    }

    public void setUpdateRetenu(boolean updateRetenu) {
        this.updateRetenu = updateRetenu;
    }

    public List<YvsGrhElementAdditionel> getListRetenues() {
        return listRetenues;
    }

    public List<YvsGrhElementAdditionel> getSelectRetenues() {
        return selectRetenues;
    }

    public void setSelectRetenues(List<YvsGrhElementAdditionel> selectRetenues) {
        this.selectRetenues = selectRetenues;
    }

    public void setListRetenues(List<YvsGrhElementAdditionel> listRetenues) {
        this.listRetenues = listRetenues;
    }

    public List<YvsGrhTypeElementAdditionel> getListTypesElts() {
        return listTypesElts;
    }

    public void setListTypesElts(List<YvsGrhTypeElementAdditionel> listTypesElts) {
        this.listTypesElts = listTypesElts;
    }

    public TypeElementAdd getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(TypeElementAdd typeElement) {
        this.typeElement = typeElement;
    }

    public ElementAdditionnel getElementAdd() {
        return elementAdd;
    }

    public void setElementAdd(ElementAdditionnel elementAdd) {
        this.elementAdd = elementAdd;
    }

    private boolean updatePlan;

    public void findOneEmploye() {
        String num = elementAdd.getContrat().getEmploye().getMatricule();
        elementAdd.getContrat().getEmploye().setId(0);
        elementAdd.getContrat().getEmploye().setNom("");
        elementAdd.getContrat().getEmploye().setPrenom("");
        if (Util.asString(num)) {
            ManagedEmployes m = (ManagedEmployes) giveManagedBean("MEmps");
            if (m != null) {
                m.findEmployeInSociete(num);
                if (!m.getListEmployes().isEmpty()) {
                    if (m.getListEmployes().size() == 1) {
                        elementAdd.getContrat().getEmploye().setError(false);
                        choixEmploye1(m.getListEmployes().get(0));
                    } else {
                        elementAdd.getContrat().getEmploye().setError(false);
                        openDialog("dlgEmploye");
                        update("tabEmployes_prelev");
                    }
                } else {
                    elementAdd.getContrat().getEmploye().setError(true);
                }
            }
        }
    }

    int indexEmp = 0;

    public void navigueInListEmploye() {
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (elementAdd.getContrat().getEmploye().getId() > 0) {
            if (service != null) {
                indexEmp = service.getListEmployes().indexOf(new YvsGrhEmployes(elementAdd.getContrat().getEmploye().getId()));
                if (indexEmp >= 0) {
                    indexEmp++;
                    choixEmploye1(service.getListEmployes().get(indexEmp++));
                }
            }
        } else {
            if (service != null) {
                if (indexEmp >= 0) {
                    choixEmploye1(service.getListEmployes().get(indexEmp++));
                }
            }
        }
        elementAdd.setId(0);
        elementAdd.setPlan(new PlanPrelevement());
        elementAdd.setDescription(null);
        elementAdd.setStatut(Constantes.STATUT_DOC_EDITABLE);
        elementAdd.setPlanifie(false);
        elementAdd.setMontant(0);
        elementAdd.setListPrelevement(new ArrayList<YvsGrhDetailPrelevementEmps>());
        updateRetenu = false;
    }

    public void choisirPlan(ValueChangeEvent ev) {
        System.err.println("Plan = " + ev);
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                elementAdd.setPlan(UtilGrh.buildBeanPlanPrelevement(plansRetenues.get(plansRetenues.indexOf(new YvsGrhPlanPrelevement(id)))));
            } else if (id == -1) {
                openDialog("dlgPlanRetenue");
            }
        }
    }

    public void loadAllTypeElementAdd() {
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        plansRetenues = dao.loadNameQueries("YvsGrhPlanPrelevement.findAll", champ, val);
        champ = new String[]{"societe", "retenue"};
        val = new Object[]{currentUser.getAgence().getSociete(), true};
        listTypesElts = dao.loadNameQueries("YvsGrhTypeElementAdditionel.findAll", champ, val);
    }

    public void loadAllTypeElementAddActif() {
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        plansRetenues = dao.loadNameQueries("YvsGrhPlanPrelevement.findAllCom", champ, val);
        champ = new String[]{"societe", "retenue"};
        val = new Object[]{currentUser.getAgence().getSociete(), true};
        listTypesElts = dao.loadNameQueries("YvsGrhTypeElementAdditionel.findAllCom", champ, val);
    }

    public void saveNewPlanRetenue() {
        if (newPlanRetenu.getReference() != null && newPlanRetenu.getBasePlan() != ' ' && newPlanRetenu.getValeur() > 0) {
            YvsGrhPlanPrelevement ep = new YvsGrhPlanPrelevement();
            ep.setActif(newPlanRetenu.isActif());
            ep.setAuthor(currentUser);
            ep.setBasePlan(newPlanRetenu.getBasePlan());
            ep.setBaseRetenue(newPlanRetenu.getBase());
            ep.setDefaut(newPlanRetenu.isDefaut());
            ep.setReferencePlan(newPlanRetenu.getReference());
            ep.setValeur(newPlanRetenu.getValeur());
            ep.setVisibleEnGescom(newPlanRetenu.isVisibleEnGescom());
            ep.setSociete(currentAgence.getSociete());
            if (!updatePlan) {
                ep = (YvsGrhPlanPrelevement) dao.save1(ep);
                newPlanRetenu.setId(ep.getId());
                plansRetenues.add(0, ep);
            } else {
                ep.setId(newPlanRetenu.getId());
                dao.update(ep);
                plansRetenues.set(plansRetenues.indexOf(ep), ep);
            }
            elementAdd.setPlan(newPlanRetenu);
            newPlanRetenu = new PlanPrelevement();
            updatePlan = false;
        } else {
            getErrorMessage("Formuaire incorrecte !");
        }
    }

    public void chooseOnePlan(SelectEvent ev) {
        YvsGrhPlanPrelevement p = (YvsGrhPlanPrelevement) ev.getObject();
        elementAdd.setPlan(UtilGrh.buildBeanPlanPrelevement(p));
    }

    public void loadOnViewPlan(SelectEvent ev) {
        YvsGrhPlanPrelevement p = (YvsGrhPlanPrelevement) ev.getObject();
        chooseOnePlanToUpdate(p);
    }

    public void chooseOnePlanToUpdate(YvsGrhPlanPrelevement p) {
        newPlanRetenu = (UtilGrh.buildBeanPlanPrelevement(p));
        updatePlan = true;
    }

    public void deletePlanRetenue(YvsGrhPlanPrelevement p) {
        try {
            p.setAuthor(currentUser);
            dao.delete(p);
            plansRetenues.remove(p);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer ce plan !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void loadExercicesActif() {
        exercices = giveListExerciceActif();
    }

    /**
     * **GERER LES TYPES DE RETENUE*******
     */
    private boolean updateTypeRetenue;

    public void saveRegleSalaire(YvsGrhTypeElementAdditionel retenue) {
        try {
            List<YvsGrhStructureSalaire> list = dao.loadNameQueries("YvsStructureSalaire.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            YvsGrhCategorieElement cat = (YvsGrhCategorieElement) dao.loadOneByNameQueries("YvsGrhCategorieElement.findByRetenue", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            if (cat != null ? cat.getId() > 0 : false) {
                YvsGrhElementSalaire element = new YvsGrhElementSalaire();
                element.setActif(true);
                element.setCode(retenue.getCodeElement());
                element.setNom(retenue.getLibelle());
                element.setCategorie(cat);
                element.setTypeMontant(3);
                element.setExpressionRegle("retenue." + retenue.getCodeElement());
                element.setAuthor(currentUser);
                element.setDateSave(new Date());
                element.setRetenue(true);
                element.setVisibleBulletin(true);
                element.setDateUpdate(new Date());
                element = (YvsGrhElementSalaire) dao.save1(element);
                if (!list.isEmpty()) {
                    for (YvsGrhStructureSalaire s : list) {
                        YvsGrhElementStructure el = new YvsGrhElementStructure();
                        el.setActif(true);
                        el.setAuthor(currentUser);
                        el.setDateSave(new Date());
                        el.setDateUpdate(new Date());
                        el.setStructure(s);
                        el.setElement(element);
                        dao.save(el);
                    }
                }
            }

        } catch (Exception e) {
        }
    }

    // créer un nouveau type d'élément ou modifier
    public void addTypeElt() {
        if (typeElement.getCode() != null ? !typeElement.getCode().trim().isEmpty() : false) {
            //Contrôle l'unicité du code
            YvsGrhTypeElementAdditionel old = (YvsGrhTypeElementAdditionel) dao.loadOneByNameQueries("YvsGrhTypeElementAdditionel.findByCodeElementRetenu", new String[]{"codeElement", "societe"}, new Object[]{typeElement.getCode(), currentAgence.getSociete()});
            typeElement.setRetenue(true);
            typeElement.setActif(true);
            YvsGrhTypeElementAdditionel ta = UtilGrh.buildTypeElt(typeElement);
            ta.setSociete(currentAgence.getSociete());
            ta.setAuthor(currentUser);
            if (typeElement.getId() <= 0) {
                if (old != null) {
                    getErrorMessage("Une règle de retenue existe déjà avec ce code !");
                    return;
                }
                ta.setId(null);
                ta = (YvsGrhTypeElementAdditionel) dao.save1(ta);
                typeElement.setId(ta.getId());
                listTypesElts.add(0, ta);
                if (generer) {
                    saveRegleSalaire(ta);
                }

            } else {
                if (old != null ? old.getId() != typeElement.getId() : false) {
                    getErrorMessage("Une autre règle de retenue existe déjà avec ce code !");
                    return;
                }
                dao.update(ta);
                listTypesElts.set(listTypesElts.indexOf(ta), ta);
            }
            elementAdd.setTypeElt(typeElement);
            update("txt_prevelement_type_elt");
            resetFormAddTypeRetenu();
        } else {
            getErrorMessage("Veuillez entrer le code de la retenue.");
        }
    }

    public void loadOnViewCompte(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBasePlanComptable bean = (YvsBasePlanComptable) ev.getObject();
            typeElement.setCompte(UtilCompta.buildBeanCompte(bean));
            update("txt_compte_type_retenu");
        }
    }

    public void searchCompte() {
        searchCompte("dlgListComptes");
    }

    public void searchCompte(String dialogueComtpe) {
        String num = typeElement.getCompte().getNumCompte();
        typeElement.getCompte().setId(0);
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (service != null) {
                service.findCompteByNum(num);
                typeElement.getCompte().setError(service.getListComptes().isEmpty());
                if (service.getListComptes() != null ? !service.getListComptes().isEmpty() : false) {
                    if (service.getListComptes().size() > 1) {
                        openDialog(dialogueComtpe);
                        update("data_comptes_type_retenu");
                    } else {
                        YvsBasePlanComptable c = service.getListComptes().get(0);
                        typeElement.setCompte(UtilCompta.buildBeanCompte(c));
                    }
                    typeElement.getCompte().setError(false);
                } else {
                    typeElement.getCompte().setError(true);
                }
            }
        }
    }

    //selectioner un type
    public void selectOneTyepeR(SelectEvent ev) {
        if (ev != null) {
            typeElement = UtilGrh.buildTypeElt(((YvsGrhTypeElementAdditionel) ev.getObject()));
            updateTypeRetenue = true;
        }
    }

    public void resetFormAddTypeRetenu() {
        typeElement = new TypeElementAdd();
        updateTypeRetenue = false;
        generer = false;
    }

    //enregistrer un nouvel élément additionnel
    @Override
    public boolean saveNew() {
        return saveNew(false, false);
    }

    public boolean saveNew(boolean continu, boolean reinit) {
        //comparer le salaire courant du mois avec le positionnnement de la dette courante
        if (controleFiche(elementAdd)) {
            YvsGrhElementAdditionel elt = buildElementAdditionel(elementAdd);
            elt.setAuthor(currentUser);
            elt.setDateUpdate(new Date());
            if (elt != null) {
                if (!updateRetenu) {
                    elt.setId(null);
                    elt.setMontantElement(0.0);
                    elt = (YvsGrhElementAdditionel) dao.save1(elt);
                    elt.setMontantElement(elementAdd.getMontant());
                    //insert un composant correspondant au montant
                    saveComposant(elt);
                    elementAdd.setId(elt.getId());
                    listRetenues.add(0, elt);
                } else {
                    if (elt.getPlanifier() && !continu) {
                        getWarningMessage("Cette retenu avait déjà été planifié !", "pensé à vérifier la cohérence du planing !");
                        openDialog("dlgConfirmReplanif");
                        return false;
                    }
                    if (elt.getPlanifier() && continu) {
                        if (reinit) {
                            elt.setPlanifier(false);
                            //suprimer les mensualités non encore réglé
                            List<YvsGrhDetailPrelevementEmps> l = elementAdd.getListPrelevement();
                            try {
                                for (YvsGrhDetailPrelevementEmps de_ : l) {
                                    if (de_.getStatutReglement() != Constantes.STATUT_DOC_PAYER) {
                                        de_.setAuthor(currentUser);
                                        de_.setDateUpdate(new Date());
                                        dao.delete(de_);
                                        elementAdd.getListPrelevement().remove(de_);
                                    }
                                }
                            } catch (Exception ex) {
                                getErrorMessage("Impossible de terminer l'opération !");
                            }
                        }
                    }
                    dao.update(elt);
                    listRetenues.set(listRetenues.indexOf(elt), elt);
                }
                succes();
            }
            updateRetenu = true;
        }
        return true;
    }

    private void saveComposant(YvsGrhElementAdditionel ed) {
        YvsGrhComposantsRetenue cr = new YvsGrhComposantsRetenue();
        cr.setActif(true);
        cr.setAuthor(currentUser);
        cr.setDateAjout(ed.getDateElement());
        cr.setMontant(ed.getMontantElement());
        cr.setMotif(ed.getDescription());
        cr.setRetenue(new YvsGrhElementAdditionel(ed.getId()));
        cr.setId(null);
        cr = (YvsGrhComposantsRetenue) dao.save1(cr);
        listComposantRetenue.add(0, cr);
    }

    public YvsGrhElementAdditionel buildElementAdditionel(ElementAdditionnel bean) {
        if (bean != null) {
            YvsGrhElementAdditionel r = new YvsGrhElementAdditionel(bean.getId());
            r.setContrat(UtilGrh.buildBeanContratEmploye(bean.getContrat()));
            r.setDateElement(bean.getDate());
            r.setDescription(bean.getDescription());
            r.setMontantElement(bean.getMontant());
            r.setPlanifier(bean.isPlanifie());
            int idx = listTypesElts.indexOf(new YvsGrhTypeElementAdditionel(bean.getTypeElt().getId()));
            if (idx >= 0) {
                r.setTypeElement(listTypesElts.get(idx));
            }
            r.setAuthor(currentUser);
            r.setPlanPrelevement(new YvsGrhPlanPrelevement(bean.getPlan().getId()));
            r.setStatut(bean.getStatut());
            if (r.getStatut() == null) {
                r.setStatut(Constantes.STATUT_DOC_EDITABLE);
            }
            r.setPermanent(false);
            r.setDateSave(bean.getDateSave());
            return r;
        }
        return null;
    }

    public void selectElementAdd(YvsGrhElementAdditionel bean) {
        if (bean != null) {
            selectedRetenue = bean;
            elementAdd = UtilGrh.buildBeanElt(bean, true);
            champ = new String[]{"retenue"};
            val = new Object[]{bean};
            elementAdd.setListPrelevement(dao.loadNameQueries("YvsGrhDetailPrelevementEmps.findByRetenue", champ, val));
            //charge l'enmployé et le contrat           
            updateRetenu = true;
            update("form_prelevement_00");
            update("head_prelevement_00");
            update("form_P_retenue");
        }
    }

    public void findDefaultPlan(ValueChangeEvent ev) {
        if (ev != null) {
            boolean b = (boolean) ev.getNewValue();
            findDefaultPlan(b);
        }
    }

    public void findDefaultPlan(boolean defaut) {
        if (defaut) {
            champ = new String[]{"societe"};
            val = new Object[]{currentUser.getAgence().getSociete()};
            YvsGrhPlanPrelevement plan = (YvsGrhPlanPrelevement) dao.loadOneByNameQueries("YvsGrhPlanPrelevement.findDefault", champ, val);
            if (plan != null) {
                elementAdd.setPlan(UtilGrh.buildBeanPlanPrelevement(plan));
            } else {
                getErrorMessage("Aucun plan par défaut n'a été trouvé. Veuillez en créer un !");
            }
        }
    }

    /**
     * GERER LES ELEMENt ADditionnel aux retenues
     *
     **************
     * @param ed
     */
    private ComposantRetenue newComposant = new ComposantRetenue();
    private boolean updateComposant;

    public ComposantRetenue getNewComposant() {
        return newComposant;
    }

    public void setNewComposant(ComposantRetenue newComposant) {
        this.newComposant = newComposant;
    }

    public void openViewAddDetailRetenu(YvsGrhElementAdditionel ed) {
        selectedRetenue = ed;
//        if (selectedRetenue.getListDetails().isEmpty()) {
        listComposantRetenue = selectedRetenue.getListDetails();
//        }
        update("table_detail_retenu");
        openDialog("dlgDetailRet");

    }

    //
    public void deplanifierRetenue(YvsGrhElementAdditionel ed) {
        //on  déplanifie la planification d'une retenue seulement si elle n'est pas complètement réglé
        selectedRetenue = ed;
        if (ed.getStatut() != Constantes.STATUT_DOC_PAYER) {
            if (!ed.getPlanifier()) {
                ed.setAuthor(currentUser);
                ed.setPlanifier(false);
                dao.update(ed);
            } else {
                //open dlg confirm déplanification
                openDialog("dlgConfirmDeplanif");
            }
        } else {
            getErrorMessage("Cette retenu a entèrement été réglé !");
        }

    }

    public void choixLineRetenue(YvsGrhElementAdditionel elt) {
        this.selectedRetenue = elt;
    }

    public void confirmDeplanif() {
        if (selectedRetenue != null) {
            try {
                for (YvsGrhDetailPrelevementEmps de_ : selectedRetenue.getRetenues()) {
                    if (de_.getStatutReglement() != Constantes.STATUT_DOC_PAYER) {
                        de_.setAuthor(currentUser);
                        dao.delete(de_);
                    }
                }
                selectedRetenue.getRetenues().clear();
                selectedRetenue.setAuthor(currentUser);
                selectedRetenue.setPlanifier(false);
                selectedRetenue.setStatut(Constantes.STATUT_DOC_EDITABLE);
                dao.update(selectedRetenue);
                succes();
            } catch (Exception ex) {
                log.log(Level.SEVERE, null, ex);
                getErrorMessage("Impossible de terminer !");
            }
        }
    }

    public void toogleActive(YvsGrhComposantsRetenue ed) {
        if (selectedRetenue != null) {
            ed.setActif(!ed.getActif());
            ed.setAuthor(currentUser);
            dao.update(ed);
        } else {
            getErrorMessage("Aucune retenu selectionné !");
        }
    }

    public void removeComposantRetenu(YvsGrhComposantsRetenue cr) {
        if (selectedRetenue != null) {
            try {
                cr.setAuthor(currentUser);
                dao.delete(cr);
                selectedRetenue.getListDetails().remove(cr);
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cet élément !");
                log.log(Level.SEVERE, null, ex);
            }
        } else {
            getErrorMessage("Aucune retenu selectionné !");
        }
    }

    public void saveNewComposantRetenu() {
        if (selectedRetenue != null) {
            if (newComposant.getMontant() > 0 && newComposant.getReference() != null) {
                YvsGrhComposantsRetenue cr = new YvsGrhComposantsRetenue();
                cr.setActif(newComposant.isActif());
                cr.setAuthor(currentUser);
                cr.setDateAjout(newComposant.getDateAjout());
                cr.setMontant(newComposant.getMontant());
                cr.setMotif(newComposant.getReference());
                cr.setRetenue(selectedRetenue);
                if (!updateComposant) {
                    cr = (YvsGrhComposantsRetenue) dao.save1(cr);
                    listComposantRetenue.add(0, cr);
                } else {
                    cr.setId(newComposant.getId());
                    dao.update(cr);
                    int idx = listComposantRetenue.indexOf(cr);
                    if (idx >= 0) {
                        listComposantRetenue.set(listComposantRetenue.indexOf(cr), cr);
                    }
                }
                newComposant = new ComposantRetenue();
                updateComposant = false;
                //ajoute le montant de la dette
                champ = new String[]{"id"};
                val = new Object[]{selectedRetenue.getId()};
                selectedRetenue.setMontantElement(selectedRetenue.getMontantElement() + cr.getMontant());
                elementAdd.setMontant(selectedRetenue.getMontantElement());
                selectedRetenue = (YvsGrhElementAdditionel) dao.loadOneByNameQueries("YvsGrhElementAdditionel.findById", champ, val);
            } else if (newComposant.getMontant() <= 0) {
                getErrorMessage("Vore formulaire est incorrecte !", "montant invalide");
            } else {
                getErrorMessage("Vore formulaire est incorrecte !", "le motif de la retenu doit être indiqué");
            }
        } else {
            getErrorMessage("Aucune retenu selectionné !");
        }
    }

    public void chooseOneComposantRetenu(SelectEvent ev) {
        if (ev != null) {
            YvsGrhComposantsRetenue cr = (YvsGrhComposantsRetenue) ev.getObject();
            newComposant.setActif(cr.getActif());
            newComposant.setDateAjout(cr.getDateAjout());
            newComposant.setMontant(cr.getMontant());
            newComposant.setReference(cr.getMotif());
            updateComposant = true;
            update("form_add_composantR");
        }
    }

    /**
     * *********************LES RETENUE
     *
     ************************
     * @param de
     */
    private YvsGrhDetailPrelevementEmps selectedDetailPrelevement;

    public YvsGrhDetailPrelevementEmps getSelectedDetailPrelevement() {
        return selectedDetailPrelevement;
    }

    public void setSelectedDetailPrelevement(YvsGrhDetailPrelevementEmps selectedDetailPrelevement) {
        this.selectedDetailPrelevement = selectedDetailPrelevement;
    }

    private List<YvsGrhDetailPrelevementEmps> listSelectPrelement = new ArrayList<>();

    public List<YvsGrhDetailPrelevementEmps> getListSelectPrelement() {
        return listSelectPrelement;
    }

    public void setListSelectPrelement(List<YvsGrhDetailPrelevementEmps> listSelectPrelement) {
        this.listSelectPrelement = listSelectPrelement;
    }

    public void supendreRetenuMany() {
        List<Integer> keys = decomposeSelection(chaineSelectPrelev);
        if (keys != null ? !keys.isEmpty() : false) {
            YvsGrhDetailPrelevementEmps bean;
            if (autoriser("ret_suspendre_prelevement")) {
                for (Integer idx : keys) {
                    bean = listPrelevement.get(idx);
                    supendreRetenuImp(bean, true, Constantes.STATUT_DOC_SUSPENDU);
                }
                succes();
            } else {
                openNotAcces();
            }
        } else {
            getErrorMessage("Aucune selection n'a été trouvé !");
        }
    }

    public void supendreRetenu(YvsGrhDetailPrelevementEmps de) {
        supendreRetenuImp(de, false, Constantes.STATUT_DOC_SUSPENDU);
    }

    public void supendreRetenuImp(YvsGrhDetailPrelevementEmps de, boolean many, Character statut) {
        if (!many ? !autoriser("ret_suspendre_prelevement") : false) {
            openNotAcces();
            return;
        }
        if (de.getStatutReglement() != Constantes.STATUT_DOC_PAYER) {
            de.setAuthor(currentUser);
            de.setStatutReglement(statut);
            if (de.getId() > 0) {
                dao.update(de);
                selectedDetailPrelevement = de;
            }
        } else {
            getErrorMessage("La retenue a déjà été réglé !");
        }
    }

    public void changeStatutRetenueRegleMany() {
        List<Integer> keys = decomposeSelection(chaineSelectPrelev);
        if (keys != null ? !keys.isEmpty() : false) {
            YvsGrhDetailPrelevementEmps bean;
            if (autoriser("ret_marque_regle_prelevement")) {
                for (Integer idx : keys) {
                    bean = listPrelevement.get(idx);
                    changeStatutRetenueRegleImp(bean, true);
                }
                succes();
            } else {
                openNotAcces();
            }
        } else {
            getErrorMessage("Aucune selection n'a été trouvé !");
        }
    }

    public void changeStatutRetenueRegle(YvsGrhDetailPrelevementEmps de) {
        changeStatutRetenueRegleImp(de, false);
    }

    public void changeStatutRetenueRegleImp(YvsGrhDetailPrelevementEmps de, boolean many) {
        if (!many) {
            if (!autoriser("ret_marque_regle_prelevement")) {
                openNotAcces();
                return;
            }
        }
        if (de.getStatutReglement() != Constantes.STATUT_DOC_PAYER) {
            de.setAuthor(currentUser);
            de.setStatutReglement(Constantes.STATUT_DOC_PAYER);
            de.setDatePreleve(new Date());
            de.setDateUpdate(new Date());
            dao.update(de);
            selectedDetailPrelevement = de;
            int idx = listPrelevement.indexOf(de);
            if (idx > -1) {
                listPrelevement.set(idx, de);
            }
        } else {
            getErrorMessage("La retenue a déjà été réglé !");
        }
    }

    public void fixeAndInitRetenueMany() {
        List<Integer> keys = decomposeSelection(chaineSelectPrelev);
        if (keys != null ? !keys.isEmpty() : false) {
            YvsGrhDetailPrelevementEmps bean;
            for (Integer idx : keys) {
                bean = listPrelevement.get(idx);
                fixeAndInitRetenueImp(bean, true);
            }
            succes();
        } else {
            getErrorMessage("Aucune selection n'a été trouvé");
        }
    }

    public void fixeAndInitRetenue(YvsGrhDetailPrelevementEmps de) {
        fixeAndInitRetenueImp(de, false);
    }

    public void fixeAndInitRetenueImp(YvsGrhDetailPrelevementEmps de, boolean many) {
        de.setAuthor(currentUser);
        de.setRetenuFixe(!de.getRetenuFixe());
        dao.update(de);
        selectedDetailPrelevement = de;
        int idx = listPrelevement.indexOf(de);
        if (idx > -1) {
            listPrelevement.set(idx, de);
        }
        succes();
    }

    public void deleteMensualiteMany() {
        List<Integer> keys = decomposeSelection(chaineSelectPrelev);
        if (keys != null ? !keys.isEmpty() : false) {
            YvsGrhDetailPrelevementEmps bean;
            List<YvsGrhDetailPrelevementEmps> l = new ArrayList<>();
            if (autoriser("ret_delete_prelevement")) {
                for (Integer idx : keys) {
                    bean = listPrelevement.get(idx);
                    deleteMensualiteImp(bean, true);
                    l.add(bean);
                }
                listPrelevement.removeAll(l);
                succes();
            } else {
                openNotAcces();
            }
        } else {
            getErrorMessage("Aucune selection n'a été trouvé !");
        }
    }

    public void deleteMensualite(YvsGrhDetailPrelevementEmps de) {
        deleteMensualiteImp(de, false);
    }

    public void deleteMensualiteImp(YvsGrhDetailPrelevementEmps de, boolean many) {
        if (!many ? !autoriser("ret_delete_prelevement") : false) {
            openNotAcces();
            return;
        }
        if (de.getStatutReglement() != Constantes.STATUT_DOC_PAYER) {
            de.setAuthor(currentUser);
            try {
                dao.delete(de);
                if (!many) {
                    elementAdd.getListPrelevement().remove(de);
                    listPrelevement.remove(de);
                }
                if (elementAdd.getListPrelevement().isEmpty()) {
                    selectedRetenue.setDateUpdate(new Date());
                    selectedRetenue.setAuthor(currentUser);
                    selectedRetenue.setPlanifier(false);
                    dao.update(selectedRetenue);
                    int index = listRetenues.indexOf(selectRetenues);
                    if (index > -1) {
                        listRetenues.set(index, selectElement);
                        update("table_p_retenue");
                    }
                }
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cet élémnet !");
                getException("Lymytz Error >>>>", ex);
            }
            selectedDetailPrelevement = de;
        } else {
            getErrorMessage("La retenue selectionné est déjà réglé !");
        }
    }

    public void changeStatutEditableRetenueMany() {
        if (autoriser("ret_annuler_when_payer")) {
            List<Integer> keys = decomposeSelection(chaineSelectPrelev);
            if (keys != null ? !keys.isEmpty() : false) {
                YvsGrhDetailPrelevementEmps bean;
                for (Integer idx : keys) {
                    bean = listPrelevement.get(idx);
                    changeStatutEditableRetenue(bean, true);
                }
                succes();
            } else {
                getErrorMessage("Aucune selection n'a été trouvé ");
            }
        } else {
            openNotAcces();
        }
    }

    public void changeStatutEditableRetenue(YvsGrhDetailPrelevementEmps de) {
        changeStatutEditableRetenue(de, false);
    }

    public void changeStatutEditableRetenue(YvsGrhDetailPrelevementEmps de, boolean many) {
        if (!many) {
            if (!autoriser("ret_annuler_when_payer")) {
                openNotAcces();
                return;
            }
        }
        if (de.getStatutReglement() != Constantes.STATUT_DOC_PAYER) {
            de.setAuthor(currentUser);
            de.setStatutReglement(Constantes.STATUT_DOC_EDITABLE);
            dao.update(de);
            selectedDetailPrelevement = de;
            int idx = listPrelevement.indexOf(de);
            if (idx > -1) {
                listPrelevement.set(idx, de);
            }
        } else {
            getErrorMessage("La retenue a déjà été réglé !");
        }
    }

    public void avancerLaretenueMany(boolean avancer) {
        if (!autoriser("ret_delete_prelevement")) {
            openNotAcces();
            return;
        }
        List<Integer> keys = decomposeSelection(chaineSelectPrelev);
        YvsGrhDetailPrelevementEmps de;
        for (Integer i : keys) {
            if (i >= 0 && i < listPrelevement.size()) {
                de = listPrelevement.get(i);
                avancerLaretenueImp(de, avancer, true);
            }
        }
        succes();
    }

    public void avancerLaretenueImp(YvsGrhDetailPrelevementEmps de, boolean avancer, boolean all) {
        if (de.getStatutReglement() != Constantes.STATUT_DOC_PAYER) {
            //récupère les element de prélèvement non réglé aux date supérieuere ou égalent à la date du détail encouurs
            if (!all) {
                if (!autoriser("ret_delete_prelevement")) {
                    openNotAcces();
                    return;
                }
            }
            if (avancer) {
                Calendar last = Calendar.getInstance();
                last.setTime(de.getDateBegin());
                last.add(Calendar.MONTH, (currentParam != null ? currentParam.getNombreMoisAvanceMaxRetenue() : 5));
                Calendar date = Calendar.getInstance();
                date.setTime(de.getDatePrelevement());
                date.add(Calendar.MONTH, 1);
                if (date.after(last)) {
                    if (!all) {
                        getErrorMessage("Vous ne pouvez plus avancer ce prelevement");
                    }
                    return;

                }
            }
            champ = new String[]{"date", "retenue", "etat"};
            val = new Object[]{de.getDatePrelevement(), de.getRetenue(), 'R'};
            List<YvsGrhDetailPrelevementEmps> l = dao.loadNameQueries("YvsGrhDetailPrelevementEmps.findSuivant", champ, val);
            Calendar cal = Calendar.getInstance();
            for (YvsGrhDetailPrelevementEmps c : l) {
                cal.setTime(c.getDatePrelevement());
                cal.add(Calendar.MONTH, (avancer) ? 1 : -1);
                c.setDatePrelevement(cal.getTime());
                c.setAuthor(currentUser);
                dao.update(c);
                int idx = elementAdd.getListPrelevement().indexOf(c);
                if (idx > -1) {
                    elementAdd.getListPrelevement().set(idx, c);
                }
                if (c.getId().equals(de.getId())) {
                    de.setDatePrelevement(c.getDatePrelevement());
                }
                idx = listPrelevement.indexOf(de);
                if (idx > -1) {
                    listPrelevement.set(idx, de);
                }
            }

        } else {
            if (!all) {
                getErrorMessage("La retenue a déjà été réglé !");
            }
        }
    }

    public void avancerLaretenueImp(YvsGrhDetailPrelevementEmps de, boolean avancer) {
        avancerLaretenueImp(de, avancer, false);
    }

    public void onSelectObjectByVente(YvsComptaCaissePieceVente y) {
        if (y != null ? y.getId() > 0 : false) {
            champ = new String[]{"piece"};
            val = new Object[]{y};
            YvsGrhElementAdditionel l = (YvsGrhElementAdditionel) dao.loadOneByNameQueries("YvsGrhElementAdditionel.findByPieceVente", champ, val);
            if (l != null ? l.getId() > 0 : false) {
                selectElementAdd(l);
            }
        }
    }

    public void onSelectObject(YvsGrhElementAdditionel y) {
        selectElementAdd(y);
        update("form_P_retenue");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            YvsGrhElementAdditionel bean = (YvsGrhElementAdditionel) ev.getObject();
            selectElementAdd(bean);
            selectRetenues.clear();
            selectRetenues.add(bean);
        }
    }

    public void loadOnViewDetail(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsGrhDetailPrelevementEmps y = (YvsGrhDetailPrelevementEmps) ev.getObject();
            selectElementAdd(y.getRetenue());
        }
    }

    public YvsGrhDetailPrelevementEmps buildDetailPrelevementEmploye(PrelevementEmps p) {
        YvsGrhDetailPrelevementEmps pp = new YvsGrhDetailPrelevementEmps();
        if (p != null) {
            pp.setId(p.getId());
        }
        return pp;
    }

    public void loadViewEmploye(SelectEvent ev) {
        YvsGrhEmployes e = (YvsGrhEmployes) ev.getObject();
        if (e != null) {
            choixEmploye1(e);
        }
    }

    public void choixEmploye1(YvsGrhEmployes ev) {
        if (ev != null) {
            if ((ev.getContrat() != null) ? ev.getContrat().getActif() : false) {
                elementAdd.setContrat(UtilGrh.buildBeanContratEmploye(ev.getContrat()));
            } else {
                elementAdd = new ElementAdditionnel();
                getErrorMessage("Aucun contrat Actif trouvé pour cet employé !");
            }
            update("chp_champ_employe_retenu");
            update("txt_salaire_mensuel_contrat_prelevement");
            update("chp_contrat_retenue_employe");
            update("id_zone_empPrelev");
        }
    }

    public boolean controleFiche(ElementAdditionnel bean) {
        if (bean.getDate() == null) {
            getErrorMessage("Vous devez entrer la date");
            return false;
        }
        if (bean.getMontant() <= 0) {
            getErrorMessage("Vous devez entrer le montant");
            return false;
        }
        if ((bean.getTypeElt() == null) ? true : bean.getTypeElt().getId() <= 0) {
            getErrorMessage("Vous devez choisir une retenue");
            return false;
        }
        if ((bean.getContrat() == null) ? true : bean.getContrat().getId() <= 0) {
            getErrorMessage("Vous devez choisir une retenue");
            return false;
        }
        if ((bean.getPlan() == null) ? true : bean.getPlan().getId() <= 0) {
            getErrorMessage("Vous devez choisir un plan de retenue");
            return false;
        }
        return true;
    }

    private boolean controleEtalage(ElementAdditionnel e) {
        if (e.getId() <= 0) {
            getErrorMessage("Aucune retenue n'a été selectionné !", " Veuillez d'abord Enregistrer ou selectionner une retenue");
            return false;
        }
        if (e.getPlan().getId() <= 0) {
            getErrorMessage("Aucun plan de retenu n'a été trouvé pour cette retenu!");
            return false;
        }
        if (e.getPlan().getValeur() <= 0) {
            getErrorMessage("Le plan de retenu a été mal configuré");
            return false;
        }
        if (e.getMontant() <= 0) {
            getErrorMessage("Le montant de la retenu est nul !");
            return false;
        }
        if (e.getContrat().getSalaireMensuel() <= 0) {
            getErrorMessage("Le contrat de l'employé ne porte aucune indication de son revenu mensuel !");
            return false;
        }
        return true;
    }

    private double sumPrelevementRegle(YvsGrhElementAdditionel elt) {
        double re = 0;
        for (Object p : dao.loadNameQueries("YvsGrhDetailPrelevementEmps.findRetenueRegle", new String[]{"retenue", "statut"}, new Object[]{elt, 'R'})) {
            YvsGrhDetailPrelevementEmps d = (YvsGrhDetailPrelevementEmps) (p);
            re += d.getValeur();
        }
        return re;
    }

    public void placerRetenu(double montant) {
        if (elementAdd.getPlan().getId() > 0 && elementAdd.getTypeElt().getId() > 0) {
            elementAdd.setPlan(UtilGrh.buildBeanPlanPrelevement(plansRetenues.get(plansRetenues.indexOf(new YvsGrhPlanPrelevement(elementAdd.getPlan().getId())))));
            elementAdd.setMontant(montant);
            double baseRetenu = montant;
            double avance = 0;
            double reste;
            int mens = 1;
            double valeur;
            Calendar cal = Calendar.getInstance();
            cal.setTime((debutPlanif != null) ? debutPlanif : elementAdd.getDebut());
            elementAdd.getListPrelevement().clear();
            //on ne planifie que les prélèvemnet non encore réglé
            baseRetenu = baseRetenu - sumPrelevementRegle(new YvsGrhElementAdditionel(elementAdd.getId()));
            switch (elementAdd.getPlan().getBasePlan()) {
                case 'F': //fixe
                    //tant que le montant est inférieure à la dette
                    reste = montant;
                    while (avance < montant) {
                        if (elementAdd.getPlan().getValeur() > reste) {
                            valeur = reste;
                        } else {
                            valeur = elementAdd.getPlan().getValeur();
                        }
                        YvsGrhDetailPrelevementEmps d = buildMensualite(elementAdd, mens, valeur, cal.getTime());
                        avance = avance + elementAdd.getPlan().getValeur();
                        cal.add(Calendar.MONTH, 1);
                        elementAdd.getListPrelevement().add(d);
                        mens++;
                        reste = (montant - avance);
                    }
                    break;
                case 'D'://dette
                    avance = (baseRetenu / elementAdd.getPlan().getValeur());
                    reste = montant;
                    while (mens <= elementAdd.getPlan().getValeur()) {
                        reste = (reste - avance);
                        if (mens == elementAdd.getPlan().getValeur() && reste != 0) {
                            avance += reste;
                        }
                        YvsGrhDetailPrelevementEmps d = buildMensualite(elementAdd, mens, avance, cal.getTime());
                        cal.add(Calendar.MONTH, 1);
                        elementAdd.getListPrelevement().add(d);
                        mens++;
                    }
                    break;
                case 'T':
                    double montantFixe = baseRetenu * elementAdd.getPlan().getValeur() / 100;
                    while (avance < montant) {
                        montantFixe = ((montant - avance) >= montantFixe) ? montantFixe : (montant - avance);
                        YvsGrhDetailPrelevementEmps d = buildMensualite(elementAdd, mens, montantFixe, cal.getTime());
                        avance = avance + montantFixe;
                        cal.add(Calendar.MONTH, 1);
                        elementAdd.getListPrelevement().add(d);
                        mens++;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //echelonner le montant d'une retenue sur les mois d'une période données
    public void etalerRetenue() {
        if (elementAdd.getId() <= 0) {
            saveNew();
        }
        if (controleEtalage(elementAdd)) {
            double baseRetenu = (elementAdd.getPlan().getBase() == 'S') ? elementAdd.getContrat().getSalaireMensuel() : elementAdd.getMontant();
            double avance = 0;
            double reste;
            int mens = 1;
            double valeur;
            Calendar cal = Calendar.getInstance();
            cal.setTime((debutPlanif != null) ? debutPlanif : elementAdd.getDebut());
            elementAdd.getListPrelevement().clear();
            //on ne planifie que les prélèvemnet non encore réglé
            baseRetenu = baseRetenu - sumPrelevementRegle(new YvsGrhElementAdditionel(elementAdd.getId()));
            switch (elementAdd.getPlan().getBasePlan()) {
                case 'F':
                    //tant que le montant est inférieure à la dette
                    reste = elementAdd.getMontant();
                    while (avance < elementAdd.getMontant()) {
                        if (elementAdd.getPlan().getValeur() > reste) {
                            valeur = reste;
                        } else {
                            valeur = elementAdd.getPlan().getValeur();
                        }
                        YvsGrhDetailPrelevementEmps d = buildMensualite(elementAdd, mens, valeur, cal.getTime());
                        avance = avance + elementAdd.getPlan().getValeur();
                        cal.add(Calendar.MONTH, 1);
                        elementAdd.getListPrelevement().add(d);
                        mens++;
                        reste = (elementAdd.getMontant() - avance);
                    }
                    break;
                case 'D':
                    avance = (baseRetenu / elementAdd.getPlan().getValeur());
                    reste = elementAdd.getMontant();
                    while (mens <= elementAdd.getPlan().getValeur()) {
                        reste = (reste - avance);
                        if (mens == elementAdd.getPlan().getValeur() && reste != 0) {
                            avance += reste;
                        }
                        YvsGrhDetailPrelevementEmps d = buildMensualite(elementAdd, mens, avance, cal.getTime());
                        cal.add(Calendar.MONTH, 1);
                        elementAdd.getListPrelevement().add(d);
                        mens++;
                    }
                    break;
                case 'T':
                    double montantFixe = baseRetenu * elementAdd.getPlan().getValeur() / 100;
                    while (avance < elementAdd.getMontant()) {
                        montantFixe = ((elementAdd.getMontant() - avance) >= montantFixe) ? montantFixe : (elementAdd.getMontant() - avance);
                        YvsGrhDetailPrelevementEmps d = buildMensualite(elementAdd, mens, montantFixe, cal.getTime());
                        avance = avance + montantFixe;
                        cal.add(Calendar.MONTH, 1);
                        elementAdd.getListPrelevement().add(d);
                        mens++;
                    }
                    break;
                default:
                    break;
            }
        }
        update("tab_prelevement_detail");
    }

    private YvsGrhDetailPrelevementEmps buildMensualite(ElementAdditionnel elt, int numero, double val, Date date) {
        YvsGrhDetailPrelevementEmps d = new YvsGrhDetailPrelevementEmps();
        d.setDateBegin(date);
        d.setDatePrelevement(date);
        d.setPlanPrelevement(new YvsGrhPlanPrelevement(elt.getPlan().getId()));
        if (elt.getTypeElt().getId() > 0) {
            d.setReference(listTypesElts.get(listTypesElts.indexOf(new YvsGrhTypeElementAdditionel(elt.getTypeElt().getId()))).getLibelle() + " ECH /" + numero);
        }
        d.setRetenue(new YvsGrhElementAdditionel(elt.getId()));
        d.setStatutReglement('E');
        d.setId(-(long) numero);
        d.setValeur(val);
        return d;
    }
    private Date de, fi;

    private boolean totalRetenuInMothOk(YvsGrhDetailPrelevementEmps detail, Date date) {
        if (paramGrh == null) {
            paramGrh = (YvsParametreGrh) dao.loadOneByNameQueries("YvsParametreGrh.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        //récupère toutes les retenu planifié au cours du mois
        //calcul l'intervalle de date
        Calendar actual = Calendar.getInstance();
        actual.setTime(date);
        int day = actual.get(Calendar.DAY_OF_MONTH);
        Calendar dayS = Calendar.getInstance();
        dayS.setTime(paramGrh.getDateDebutTraitementSalaire());
        int daySalaire = actual.get(Calendar.DAY_OF_MONTH);
        if (day >= daySalaire) {
            //la date de début est dans le mois en cours            
            actual.set(Calendar.DAY_OF_MONTH, dayS.get(Calendar.DAY_OF_MONTH));
            actual.add(Calendar.DAY_OF_MONTH, 1);
            de = actual.getTime();
            actual.add(Calendar.MONTH, 1);
            actual.add(Calendar.DAY_OF_MONTH, -1);
            fi = actual.getTime();
        } else {
            //la date de début est dans le mois précédent            
            actual.set(Calendar.DAY_OF_MONTH, dayS.get(Calendar.DAY_OF_MONTH));
            actual.add(Calendar.MONTH, -1);
            actual.add(Calendar.DAY_OF_MONTH, 1);
            de = actual.getTime();
            actual.add(Calendar.MONTH, 1);
            actual.add(Calendar.DAY_OF_MONTH, -1);
            fi = actual.getTime();
        }
        //Somme des retenu de ce mois
        Double re = (Double) dao.loadObjectByNameQueries("YvsGrhDetailPrelevementEmps.sumRetenuInMonth", new String[]{"contrat", "date1", "date2", "statut"}, new Object[]{new YvsGrhContratEmps(elementAdd.getContrat().getId()), de, fi, 'R'});
        if (re != null && paramGrh.getQuotiteCessible() != null) {
            //re doit être inférieure à la quotité cessible
            double q = (paramGrh.getQuotiteCessible() / 100) * elementAdd.getContrat().getSalaireMensuel();
            return re <= q;
        } else {
            if (paramGrh.getQuotiteCessible() == null) {
                getWarningMessage("Valeur de paramétrage incorrecte !", "Aucune valeur n'a été trouvé pour la quotité cessible");
            }
            //return   false;
        }
        return true;
    }

    public boolean saveListeDetail() {
        if (!elementAdd.isPlanifie()) {
            boolean continu = true;
            //controle que le salaire de lemployé couvre la totalité des mensualité de ce mois

            for (YvsGrhDetailPrelevementEmps d : elementAdd.getListPrelevement()) {
                //controle que le salaire de lemployé couvre la totalité des mensualité de ce mois
                if (!totalRetenuInMothOk(d, d.getDatePrelevement())) {
                    continu = false;
                    getErrorMessage("Le total des retenues planifié du mois de " + dfML.format(d.getDatePrelevement()) + " est supérieure à la quotité céssible !", "Vous pouvez définir un aute plan de retenue");
                    break;
                }
            }
            if (continu) {
                for (YvsGrhDetailPrelevementEmps d : elementAdd.getListPrelevement()) {
                    d.setId(null);
                    d.setAuthor(currentUser);
                    d.setRetenue(new YvsGrhElementAdditionel(elementAdd.getId()));
                    d.setRetenuFixe(false);
                    d.setDateSave(new Date());
                    d.setDateUpdate(new Date());
                    d.setAuthor(currentUser);
                    d = (YvsGrhDetailPrelevementEmps) dao.save1(d);

                }
                //préciser que la retenue a été planifié
                YvsGrhElementAdditionel ee = listRetenues.get(listRetenues.indexOf(new YvsGrhElementAdditionel(elementAdd.getId())));
                ee.setPlanifier(true);
                ee.setDateUpdate(new Date());
                ee.setAuthor(currentUser);
                dao.update(ee);
                elementAdd.setPlanifie(true);
            }

        } else {
            //modifie la planification
            getErrorMessage("Cette retenue a déjà été planifié !");
        }
        succes();
        return true;
    }

    //Enregistre un nouveau plan de prelèvement
//    public boolean saveNewPrelevement() {
//        if (!isUpdatePlanPrelevement()) {
//            entityPrevelement = buildPrelevementEmps(prelevement);
//            entityPrevelement.setAuthor(currentUser);
////            entityPrevelement = (YvsGrhPrelevementEmps) dao.save1(entityPrevelement);
//            prelevement.setId(entityPrevelement.getId());
//            elementAdd.getListPrelevement().add(prelevement);
//            saveListeDetail();
//        } else {
//            entityPrevelement = buildPrelevementEmps(prelevement);
////            entityPrevelement = (YvsGrhPrelevementEmps) dao.update(entityPrevelement);
//            prelevement.setId(entityPrevelement.getId());
//            elementAdd.getListPrelevement().set(elementAdd.getListPrelevement().indexOf(prelevement), prelevement);
//            deleteBeanDetail();
//            saveListeDetail();
//        }
//        setUpdatePlanPrelevement(true);
//        succes();
//        return true;
//    }
    public void saveNewDetail() {
//        if (detailPrelevement != null) {
//            entityDetail = buildDetailPrelevementEmploye(detailPrelevement);
//            if (!isUpdateDetail()) {
//                entityDetail = (YvsGrhDetailPrelevementEmps) dao.save1(entityDetail);
//                detailPrelevement.setId(entityDetail.getId());
//                listDetail.add(detailPrelevement);
//            } else {
//                dao.update(entityDetail);
//                listDetail.remove(detailPrelevement);
//                listDetail.add(listDetail.size(), detailPrelevement);
//            }
//            update("tab_prelevement_detail");
//            succes();
//        }
    }

    public void deleteBeanDetail() {
        String rq = "DELETE FROM yvs_grh_detail_prelevement_emps WHERE prelevement_emps=?";
//        Options[] param = new Options[]{new Options(prelevement.getId(), 1)};
//        dao.requeteLibre(rq, param);
    }

    @Override
    public boolean controleFiche(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteBean() {
        try {
            List<Integer> l = decomposeSelection(chaineSelectPlan);
            List<YvsGrhElementAdditionel> lr = new ArrayList<>();
            if (l != null) {
                if (!l.isEmpty()) {
                    for (Integer id : l) {
                        YvsGrhElementAdditionel el = listRetenues.get(id);
                        el.setAuthor(currentUser);
                        el.setDateUpdate(new Date());
                        dao.delete(el);
                        lr.add(el);
                    }
                    listRetenues.removeAll(lr);
                    update("table_p_retenue");
                    resetFiche();
                    succes();
                }
            }
        } catch (Exception ex) {
            getException("Action impossible !", ex);
        }
    }

    public void openDeleteOneRetenue(YvsGrhElementAdditionel e) {
        if (e != null) {
            if (e.getPiceReglement() == null) {
                selectedRetenue = e;
                openDialog("confirmDeleteRet");
            } else {
                if (!autoriser("compta_del_reg_retenue")) {
                    getErrorMessage("Vous ne pouvez supprimer cette retenue car elle fait référence au règlement d'une facture !");
                } else {
                    selectedRetenue = e;
                    openDialog("confirmDeleteRet");
                }
            }
        }
    }

    public boolean canDeleteRetenue(YvsGrhElementAdditionel re) {
        if (re.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
            getErrorMessage("Cette retenue a déjà été payé !");
            return false;
        }
        Long nb = (Long) dao.loadObjectByNameQueries("YvsGrhDetailPrelevementEmps.countByStatutReglement", new String[]{"retenue", "statut"}, new Object[]{re, Constantes.STATUT_DOC_PAYER});
//        if (nb != null ? nb <= 0 : false) {
        if (nb != null ? nb > 0 : false) {
            getErrorMessage("Certaines mensualité de cette retenue ont déjà été payé", "Impossible de la supprimer");
            return false;
        }
        return true;
    }

    public boolean deleteOneRetenue_(YvsGrhElementAdditionel re) {
        if (canDeleteRetenue(re)) {
            try {
                re.setDateUpdate(new Date());
                re.setAuthor(currentUser);
                dao.delete(re);
                return true;
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cette élément !");
                return false;
            }
        }
        return false;
    }

    public void deleteOneRetenue() {
        try {
            if (selectedRetenue != null) {
                selectedRetenue.setAuthor(currentUser);
                dao.delete(selectedRetenue);
                listRetenues.remove(selectedRetenue);
                succes();
                update("tab_prelevement_detail");
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de continuer !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deletePrelevenement() {
        try {
            System.err.println("selectRetenues = " + selectRetenues);
            if (selectRetenues != null ? !selectRetenues.isEmpty() : false) {
                for (YvsGrhElementAdditionel p : selectRetenues) {
                    p.setAuthor(currentUser);
                    p.setDateUpdate(new Date());
                    dao.delete(p);

                }
                succes();
                update("tab_prelevement_detail");
                resetFiche();
                listRetenues.removeAll(listSelectPrelement);
            }
        } catch (Exception e) {
            getErrorMessage("Impossible de continuer !");
            log.log(Level.SEVERE, null, e);
        }
    }

    public void deleteOneRetenueForFacture(YvsComptaCaissePieceVente piece) {
        try {
            if (piece != null) {
                String query = "DELETE FROM yvs_grh_element_additionel WHERE pice_reglement=? ";
                dao.requeteLibre(query, new Options[]{new Options(piece.getId(), 1)});
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de continuer !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    private List<ElementAdditionnel> loadListSelectLine() {
        List<ElementAdditionnel> re = new ArrayList<>();
        if (chaineSelectPlan != null) {
            String numroLine[] = chaineSelectPlan.split("-");
            List<String> l = new ArrayList<>();
            try {
                int index;
                for (String numroLine1 : numroLine) {
                    index = Integer.parseInt(numroLine1);
//                    re.add(listRetenues.get(index));
                    l.add(numroLine1);
                }
                chaineSelectPlan = "";
                succes();
            } catch (NumberFormatException ex) {
                chaineSelectPlan = "";
                for (String numroLine1 : numroLine) {
                    if (!l.contains(numroLine1)) {
                        chaineSelectPlan += numroLine1 + "-";
                    }
                }
                getErrorMessage("Impossible de terminer cette opération ! des élément de votre sélection doivent être encore en liaison");
                return null;
            }
        }
        return re;
    }

    public void deleteBeanPrelevement() {
//        if (selectedDetailPrelevement != null) {
//            if (prelevement.getId() > 0) {
//                List<ElementAdditionnel> l = loadListSelectLine();
//                if (l != null) {
//                    String rq = "UPDATE yvs_grh_element_additionel SET planifier=false WHERE id=?";
//                    for (ElementAdditionnel ed : l) {
//                        if (ed.getPrelevement() != null) {
////                            dao.delete(new YvsGrhPrelevementEmps(ed.getPrelevement().getId()));
//                        }
//                        Options[] param = new Options[]{new Options(ed.getId(), 1)};
//                        dao.requeteLibre(rq, param);
////                        listRetenues.get(listRetenues.indexOf(ed)).setPlanifie(false);
//                    }
//                    resetPrelevement(new PlanPrelevement());
//                    resetFiche();
//                    succes();
//                }
//            }
//        }
    }

//    @Override
//    public void updateBean() {
//        setVueListe(false);
//        setSelectRetenu(false);
//        champ = new String[]{"id"};
//        val = new Object[]{elementAdd.getContrat().getId()};
//        ContratEmploye co = UtilGrh.buildBeanContratEmploye((YvsGrhContratEmps) dao.loadOneByNameQueries("YvsGrhContratEmps.findById", champ, val), true);
//        choixEmploye1(co.getEmploye());
//        setDette((prelevement.getBase() == 1) ? elementAdd.getContrat().getSalaireMensuel() : elementAdd.getMontant());
//        update("body_prelevement_00");
//        update("head_prelevement_00");
//    }
    @Override
    public Serializable recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Serializable bean) {
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
//        if (ev != null) {
//            ElementAdditionnel bean = (ElementAdditionnel) ev.getObject();
//            selectElementAdd(bean);
//        }
    }

    /**
     * Charge le éléments de retenue en fonction de certains détails
     */
    @Override
    public void loadAll() {
        loadAllRetenue(true, true);
        if (elementAdd != null ? (elementAdd.getPlan() != null ? elementAdd.getPlan().isDefaut() : false) : false) {
            if (elementAdd.getPlan().getId() < 1) {
                findDefaultPlan(true);
            }
        }
        if (currentParam == null) {
            currentParam = (YvsParametreGrh) dao.loadOneByNameQueries("YvsParametreGrh.findAll", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
        }
    }

    public void loadAllRetenue(boolean avancer, boolean init) {
        ParametreRequete pr = new ParametreRequete("y.typeElement.societe", "societe", currentUser.getAgence().getSociete());
        pr.setOperation("=");
        pr.setPredicat("AND");
        paginator.addParam(pr);
        pr = new ParametreRequete("y.typeElement.retenue", "retenue", true);
        pr.setOperation("=");
        pr.setPredicat("AND");
        paginator.addParam(pr);
        listRetenues = paginator.executeDynamicQuery("YvsGrhElementAdditionel", "y.dateElement DESC", avancer, init, (int) imax, dao);
    }

    public void loadAllPrelevement(boolean avancer, boolean init) {
        p_prevelement.addParam(new ParametreRequete("y.retenue.typeElement.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND"));
        p_prevelement.addParam(new ParametreRequete("y.retenue.typeElement.retenue", "retenue", true, "=", "AND"));
        listPrelevement = p_prevelement.executeDynamicQuery("YvsGrhDetailPrelevementEmps", "y.datePrelevement DESC, y.retenue.contrat.employe.nom", avancer, init, dao);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsGrhElementAdditionel> re = paginator.parcoursDynamicData("YvsGrhElementAdditionel", "y", "y.dateDebut DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void p_parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (p_prevelement.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsGrhDetailPrelevementEmps> re = p_prevelement.parcoursDynamicData("YvsGrhDetailPrelevementEmps", "y", "y.datePrelevement DESC, y.retenue.contrat.employe.nom", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0).getRetenue());
        }
    }

    public void pagineResult(boolean avancer) {
        loadAllRetenue(avancer, false);
        optionSearch = 1;
    }

    public void paginePrelevement(boolean avancer) {
        loadAllPrelevement(avancer, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllRetenue(true, true);
    }

    public void gotoPagePaginatorPrelevement() {
        loadAllPrelevement(true, true);
    }

    public void changeMaxResult(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            imax = (long) ev.getNewValue();
            loadAllRetenue(true, true);
        }
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
    }

    public void changeMaxPrelevement(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long v;
            try {
                v = (long) ev.getNewValue();
            } catch (Exception ex) {
                v = (int) ev.getNewValue();
            }
            p_prevelement.setRows((int) v);
            loadAllPrelevement(true, true);
        }
    }

    public void loadAllRetenueByContrat() {
        champ = new String[]{"contrat"};
        val = new Object[]{new YvsGrhContratEmps(elementAdd.getContrat().getId())};
        listRetenues = dao.loadListTableByNameQueries("YvsGrhElementAdditionel.findByContrat_", champ, val);
    }

    public void loadAllRetenueBynature(boolean planifie) {
        champ = new String[]{"planifier", "societe"};
        val = new Object[]{planifie, currentUser.getAgence().getSociete()};
        listRetenues = dao.loadListTableByNameQueries("YvsGrhElementAdditionel.findByPlanifier", champ, val);
    }

    @Override
    public void resetFiche() {
        resetFiche(true);
    }

    public void resetFiche(boolean all) {
        TypeElementAdd type = new TypeElementAdd();
        cloneObject(type, elementAdd.getTypeElt());
        ContratEmploye c = elementAdd.getContrat();
        elementAdd = new ElementAdditionnel();
        elementAdd.setContrat(c);
        if (!all) {
            elementAdd.setTypeElt(type);
        }
        if (elementAdd != null ? (elementAdd.getPlan() != null ? elementAdd.getPlan().isDefaut() : false) : false) {
            findDefaultPlan(true);
        }
        updateRetenu = false;
    }

    @Override
    public void resetPage() {

    }

    public void onSelectDistant(YvsGrhElementAdditionel da) {
        if (da != null ? da.getId() > 0 : false) {
            onSelectObject(da);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Prélèvement", "modRh", "smenPrelevement", true);
            }
        }
    }

    public void resetPageRetenu() {
        if (listRetenues != null) {
//            for (ElementAdditionnel e : listRetenues) {
//                listRetenues.get(listRetenues.indexOf(e)).setSelectActif(false);
//            }
        }
        setRetenuPlanifier(false);
    }

    public void resetPrelevement(PlanPrelevement plan) {
        dette = 0;
        setActivebaseMontant(false);
        setActivebaseNombreMois(false);
        setActivebaseTaux(false);
    }

    public void changeToBaseTaux() {
        if (isActivebaseTaux()) {
            setActivebaseMontant(false);
            setActivebaseNombreMois(false);
        }
        update("panel_prelevement_choix");
    }

    public void changeToBaseMontant() {
        if (isActivebaseMontant()) {
            setActivebaseTaux(false);
            setActivebaseNombreMois(false);
        }
        update("panel_prelevement_choix");
    }

    public void changeToBaseNombreMois() {
        if (isActivebaseNombreMois()) {
            setActivebaseMontant(false);
            setActivebaseTaux(false);
        }
        update("panel_prelevement_choix");
    }

    //calculer les autres variable de la retenue en fonction du taux de l'echéance (Montant de l'echéance et la nombre d'échéances).
    public void otherElementByTaux() {
        try {
//            dette = ((prelevement.getBase() == 1) ? elementAdd.getContrat().getSalaireMensuel() : elementAdd.getMontant());
//            double taux, montant = 0;
//            Integer duree = 0;
//            if (dette != 0) {
//                taux = prelevement.getTaux() / 100;     //taux en double
//                montant = dette * taux;
//                if (montant >= elementAdd.getMontant()) {
//                    duree = 1;
//                } else {
//                    duree = (((dette / montant) - (new Double(dette / montant).intValue()) == 0) ? new Double(dette / montant).intValue()
//                            : new Double(dette / montant).intValue() + 1);
//                }
//            }
//            if (prelevement.getBase() == 1) {   //si la base est le salaire            
//                duree = (int) (elementAdd.getMontant() / montant);
//                if (elementAdd.getMontant() % montant != 0) {
//                    duree = duree + 1;
//                }
//            }
//            if (duree < 0) {
//                duree = 0;
//            }
//            if (montant < 0) {
//                montant = 0;
//            }
//            prelevement.setMontantPrelevement(montant);
//            prelevement.setNombreMois(duree);
//            placeRetenue();
            update("panel_prelevement_choix");
        } catch (Exception ex) {
            Logger.getLogger(ManagedRetenue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void otherElementByMontant() {
        double taux = 0, montant = 0;
//        setDette((prelevement.getBase() == 1) ? elementAdd.getContrat().getSalaireMensuel() : elementAdd.getMontant());
        Integer duree = 0;
        if (dette != 0) {
//            montant = prelevement.getMontantPrelevement();
            taux = montant / dette;
            if (montant > elementAdd.getMontant()) {
                duree = 1;
            } else {
                duree = (((dette / montant) - (new Double(dette / montant).intValue()) == 0) ? new Double(dette / montant).intValue()
                        : new Double(dette / montant).intValue() + 1);
            }
        }
        if (duree < 0) {
            duree = 0;
        }
//        placeRetenue();
        update("panel_prelevement_choix");
    }

    public void otherElementByDuree() {
        double taux = 0, montant = 0, duree = 0;
//        setDette((prelevement.getBase() == 1) ? elementAdd.getContrat().getSalaireMensuel() : elementAdd.getMontant());
        if (dette != 0) {
//            duree = prelevement.getNombreMois();
            montant = dette / duree;
            taux = montant / dette;
            if (montant > elementAdd.getMontant()) {
                duree = 1;
            }
        }
        if (montant < 0) {
            montant = 0;
        }
//        placeRetenue();
        update("panel_prelevement_choix");
    }

//    public void chooseRetenu(ValueChangeEvent ev) {
//        if (ev != null) {
//            Long id = (Long) ev.getNewValue();
//            if (id == -1) {
//                openDialog("dlgRetenu");
//            }
//            update("form_prelevement_01");
//        }
//    }
    public void chooseBase(ValueChangeEvent ev) {
        if (ev != null) {
            short base = (short) ev.getNewValue();
            if (base > 0) {
//                prelevement.setBase(base);
//                changePlanDefault(new ValueChangeEvent(new SelectBooleanButton(), false, prelevement.isPlanDefaut()));
            }
            update("tab_prelevement_detail");
        }
    }

    public void changedPlanifier(ValueChangeEvent ev) {
        if (ev != null) {
            boolean value = (boolean) ev.getNewValue();
            displayPlacement = value;
            update("panel_prelevement_choix");
        }
    }

    //lorsqu'on change le plan de retenue  (du plan par défaut à un plan qu'on veut créer)
//    public void changePlanDefault(ValueChangeEvent ev) {
//        if (ev != null) {
//            prelevement.setPlanDefaut((boolean) ev.getNewValue()); //nvelle valeur du plan
////            prelevement.setBase((prelevement.getBase() != null) ? prelevement.getBase() : 1);
//            if (prelevement.isPlanDefaut()) {   // si le plan choisi est le plan par défaut (i.e true)
//                loadAllPlanPrelementByBase(prelevement.getBase()); //charge le détail
////                defautlPlan.setBase(prelevement.getBase());
//            } else {
////                resetFiche(defautlPlan);
////                defautlPlan.setListDetail(new ArrayList<PlanPrelevement>());
//            }
//            resetPrelevement(defautlPlan);
//            setDette((prelevement.getBase() == 1) ? elementAdd.getContrat().getSalaireMensuel() : elementAdd.getMontant());
//            otherElementByTaux();
//            etalerRetenue();
//        }
//    }
//    public void placeRetenue() {
//        if (elementAdd.getMontant() > 0) {
//            if (isUpdateRetenuAdd()) { //si on est en train de modifier un plan de prélèvement existant
////                changePlanDefault(new ValueChangeEvent(new SelectBooleanButton(), false, prelevement.isPlanDefaut()));
//            } else {
//                openDialog("dlgConfirmSave");
//            }
//            update("tab_prelevement_detail");
//        }
//    }
    public void changeSuspension(PrelevementEmps bean) {
//        bean.setSuspendu(!bean.isSuspendu());
//        listDetail.get(listDetail.indexOf(bean)).setSuspendu(bean.isSuspendu());
//        String rq = "UPDATE yvs_grh_detail_prelevement_emps SET suspendu=" + bean.isSuspendu() + " WHERE id=?";
//        Options[] param = new Options[]{new Options(bean.getId(), 1)};
//        dao.requeteLibre(rq, param);
//        update("tab_prelevement_detail");
    }

//    PrelevementEmps detailEgale = new PrelevementEmps();
//    public PrelevementEmps getDetailEgale() {
//        return detailEgale;
//    }
//
//    public void setDetailEgale(PrelevementEmps detailEgale) {
//        this.detailEgale = detailEgale;
//    }
    public void deplacementDetail() {
//        if (detailPrelevement.getDatePrelevement().after(new Date())) {
//            boolean trouv = false;
//            String txt_date_detail, txt_date_suiv;
//            Calendar datePre = Calendar.getInstance();
//            Calendar dateParam = Calendar.getInstance();
//            Calendar dateSuiv = Calendar.getInstance();
//            champ = new String[]{"societe"};
//            val = new Object[]{currentAgence.getSociete()};
//            ParametrageGRH param = UtilGrh.buildBeanParametrageGRH((YvsParametreGrh) dao.loadOneByNameQueries("YvsParametreGrh.findAll", champ, val));
//            dateParam.setTime((param.getDatePaiementSalaire() != null) ? param.getDatePaiementSalaire() : new Date());
//            datePre.setTime(detailPrelevement.getDatePrelevement());
//            txt_date_detail = dateParam.get(Calendar.DATE) + "/" + datePre.get(Calendar.MONTH) + "/" + datePre.get(Calendar.YEAR);
//            for (PrelevementEmps d : listDetail) {
//                dateSuiv.setTime(d.getDatePrelevement());
//                txt_date_suiv = dateParam.get(Calendar.DATE) + "/" + dateSuiv.get(Calendar.MONTH) + "/" + dateSuiv.get(Calendar.YEAR);
//                if (txt_date_detail.trim().equals(txt_date_suiv.trim())) {
//                    trouv = true;
////                    cloneObject(detailEgale, d);
//                    break;
//                }
//            }
//            if (!trouv) {
//                openDialog("dlgConfirmDeplacement");
//            } else {
//                openDialog("dlgConfirmFusion");
//            }
//        } else {
//            getErrorMessage("Impossible de deplacer a cette date");
//        }
    }

    public void fusionnerDetail() {
//        if (detailEgale != null) {
//            detailPrelevement.setValeur(detailPrelevement.getValeur() + detailEgale.getValeur());
//            dao.delete(new YvsGrhDetailPrelevementEmps(detailEgale.getId()));
//            listDetail.remove(detailEgale);
//            saveNewDetail();
//        }
    }

    @Override
    public void updateBean() {
    }
    /*Recherche les retenues par une requête dynamique*/

    List<ParametreRequete> params = new ArrayList<>();

    public void loadRetenueByEmploye(String matricule) {
        ParametreRequete p = new ParametreRequete(null, "employe", "XX", "LIKE", "AND");
        if ((matricule != null) ? !matricule.isEmpty() : false) {
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.contrat.employe.nom)", "emps", "%" + matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.contrat.employe.prenom)", "emps", "%" + matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.contrat.employe.matricule)", "emps", matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        loadAllRetenue(true, true);
    }

    public void loadPrelevementByEmploye(String matricule) {
        ParametreRequete p = new ParametreRequete(null, "employe", "XX", "LIKE", "AND");
        if ((matricule != null) ? !matricule.isEmpty() : false) {
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.retenue.contrat.employe.nom)", "emps", "%" + matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.retenue.contrat.employe.prenom)", "emps", "%" + matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.retenue.contrat.employe.matricule)", "emps", matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
        } else {
            p.setObjet(null);
        }
        p_prevelement.addParam(p);
        loadAllPrelevement(true, true);
    }

    public void addParamEmploye(ValueChangeEvent ev) {
        if (ev != null) {
            String str = (String) ev.getNewValue();
            if (str != null) {
                ParametreRequete p = new ParametreRequete("employe");
                if (!str.isEmpty()) {
                    p = new ParametreRequete("contrat.employe.agence.societe", "employe", "%" + str + "%");
                    p.setOperation("LIKE");
                    p.setPredicat("AND");
                    if (!params.contains(p)) {
                        params.add(p);
                    } else {
                        params.set(params.indexOf(p), p);
                    }
                } else {
                    params.remove(p);
                }

            } else {
                params.remove(new ParametreRequete("employe"));
            }
        }
    }

    public void addParamDate(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.dateElement", "dateElt", null, "BETWEEN", "AND");
        if (ev != null ? ev.getNewValue() != null : false) {
            boolean b = (boolean) ev.getNewValue();
            if (b && debutF != null && finF != null) {
                if (debutF.before(finF) || debutF.equals(finF)) {
                    p.setObjet(debutF);
                    p.setOtherObjet(finF);
                }
            }
        }
        paginator.addParam(p);
        loadAllRetenue(true, true);
    }

    public void addParamDatePrelevement(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.datePrelevement", "dateElt", null, "BETWEEN", "AND");
        if (ev != null ? ev.getNewValue() != null : false) {
            boolean b = (boolean) ev.getNewValue();
            if (b && debutDetail != null && finDetail != null) {
                if (debutDetail.before(finDetail) || debutDetail.equals(finDetail)) {
                    p.setObjet(debutDetail);
                    p.setOtherObjet(finDetail);
                }
            }
        }
        p_prevelement.addParam(p);
        loadAllPrelevement(true, true);
    }

    public void addParamAgence() {
        ParametreRequete p = new ParametreRequete("y.contrat.employe.agence", "agence", null, "=", "AND");
        if (agenceSearch != null ? agenceSearch > 0 : false) {
            p = new ParametreRequete("y.contrat.employe.agence", "agence", new YvsAgences(agenceSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllRetenue(true, true);
    }

    public void addParamDate1(SelectEvent ev) {
        ParametreRequete p = new ParametreRequete("y.dateElement", "dateElt", null, "BETWEEN", "AND");
        if (ev != null ? ev.getObject() != null : false) {
            Date debut = (Date) ev.getObject();
            if (finF != null) {
                if (finF.after(debut) || finF.equals(debut)) {
                    p.setObjet(debut);
                    p.setOtherObjet(finF);
                }
            }
        }
        paginator.addParam(p);
        loadAllRetenue(true, true);
    }

    public void addParamDate1Prelevement(SelectEvent ev) {
        ParametreRequete p = new ParametreRequete("y.datePrelevement", "dateElt", null, "BETWEEN", "AND");
        if (ev != null ? ev.getObject() != null : false) {
            Date debut = (Date) ev.getObject();
            if (finDetail != null) {
                if (finDetail.after(debut) || finDetail.equals(debut)) {
                    p.setObjet(debut);
                    p.setOtherObjet(finDetail);
                }
            }
        }
        p_prevelement.addParam(p);
        loadAllPrelevement(true, true);
    }

    public void addParamDate2(SelectEvent ev) {
        ParametreRequete p = new ParametreRequete("y.dateElement", "dateElt", null, "BETWEEN", "AND");
        if (ev != null ? ev.getObject() != null : false) {
            Date fin = (Date) ev.getObject();
            if (debutF != null) {
                if (debutF.before(fin) || debutF.equals(fin)) {
                    p.setObjet(debutF);
                    p.setOtherObjet(fin);
                }
            }
        }
        paginator.addParam(p);
        loadAllRetenue(true, true);
    }

    public void addParamDate2Prelevement(SelectEvent ev) {
        ParametreRequete p = new ParametreRequete("y.datePrelevement", "dateElt", null, "BETWEEN", "AND");
        if (ev != null ? ev.getObject() != null : false) {
            Date fin = (Date) ev.getObject();
            if (debutDetail != null) {
                if (debutDetail.before(fin) || debutDetail.equals(fin)) {
                    p.setObjet(debutDetail);
                    p.setOtherObjet(fin);
                }
            }
        }
        p_prevelement.addParam(p);
        loadAllPrelevement(true, true);
    }

    public void addParamStatut(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.statut", "statut", null, "=", "AND");
        if (ev != null ? ev.getNewValue() != null : false) {
            char str = (char) ev.getNewValue();
            if (str != ' ') {
                p.setObjet(str);
            }
        }
        paginator.addParam(p);
        loadAllRetenue(true, true);
    }

    public void addParamComptabilised() {
        ParametreRequete p = new ParametreRequete("coalesce(y.comptabilise, false)", "comptabilise", comptaSearch, "=", "AND");
        if (comptaSearch != null) {
            String query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_retenue_salaire c RIGHT JOIN yvs_grh_element_additionel y ON c.retenue = y.id "
                    + "INNER JOIN yvs_grh_contrat_emps o On y.contrat = o.id INNER JOIN yvs_grh_employes e ON o.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id "
                    + "WHERE a.societe = ? AND c.id " + (comptaSearch ? "IS NOT NULL" : "IS NULL");
            Options[] params = new Options[]{new Options(currentAgence.getSociete().getId(), 1)};
            if (paramDate) {
                query += " AND y.date_element BETWEEN ? AND ?";
                params = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(debutF, 2), new Options(finF, 3)};
            }
            Long count = (Long) dao.loadObjectBySqlQuery(query, params);
            nbrComptaSearch = count != null ? count : 0;
        }
        paginator.addParam(p);
        loadAllRetenue(true, true);
    }

    public void addParamStatutPrelevement(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.retenue.statut", "statut", null, "=", "AND");
        if (ev != null ? ev.getNewValue() != null : false) {
            char str = (char) ev.getNewValue();
            if (str != ' ') {
                p.setObjet(str);
            }
        }
        p_prevelement.addParam(p);
        loadAllPrelevement(true, true);
    }

    public void addParamTypeElement(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.typeElement", "typeElement", null, "=", "AND");
        if (ev != null ? ev.getNewValue() != null : false) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                YvsGrhTypeElementAdditionel type = new YvsGrhTypeElementAdditionel((long) ev.getNewValue());
                p.setObjet(type);

            }
        }
        paginator.addParam(p);
        loadAllRetenue(true, true);
    }

    public void addParamTypeElementPrelevement(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.retenue.typeElement", "typeElement", null, "=", "AND");
        if (ev != null ? ev.getNewValue() != null : false) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                YvsGrhTypeElementAdditionel type = new YvsGrhTypeElementAdditionel((long) ev.getNewValue());
                p.setObjet(type);

            }
        }
        p_prevelement.addParam(p);
        loadAllPrelevement(true, true);
    }

    public void addParamMontant() {
        ParametreRequete p = new ParametreRequete("y.montantElement", "montantElement", null, " BETWEEN ", "AND");
        if (montant1 > 0 && montant2 > 0) {
            if (montant1 <= montant2) {
                p.setObjet(montant1);
                p.setOtherObjet(montant2);
            }
        }
        paginator.addParam(p);
        loadAllRetenue(true, true);
    }

    public void addParamMontantPrelevement() {
        ParametreRequete p = new ParametreRequete("y.valeur", "montant", null, " BETWEEN ", "AND");
        if (montant1Detail > 0 && montant2Detail > 0) {
            if (montant1Detail <= montant2Detail) {
                p.setObjet(montant1Detail);
                p.setOtherObjet(montant2Detail);
            }
        }
        p_prevelement.addParam(p);
        loadAllPrelevement(true, true);
    }

    public void addParamPlanifier(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.planifier", "planifier", null, "=", "AND");
        if (ev != null ? ev.getNewValue() != null : false) {
            int planif = (int) ev.getNewValue();
            if (planif > 0) {
                p.setObjet(planif == 1);
            }
        }
        paginator.addParam(p);
        loadAllRetenue(true, true);
    }

    /*Traiter l'affichage des retenues      : CETTE PARTIE GERE LA VUE GESTION DES RETENUES*/
    private long idExo, idAgence;
    private PaginatorResult<YvsGrhDetailPrelevementEmps> po = new PaginatorResult<>();
    private int maxElt = 25;
    private long periodeSalaire;
    private String empsSearch;

    public PaginatorResult<YvsGrhDetailPrelevementEmps> getPo() {
        return po;
    }

    public void setPo(PaginatorResult<YvsGrhDetailPrelevementEmps> po) {
        this.po = po;
    }

    public long getPeriodeSalaire() {
        return periodeSalaire;
    }

    public void setPeriodeSalaire(long periodeSalaire) {
        this.periodeSalaire = periodeSalaire;
    }

    public int getMaxElt() {
        return maxElt;
    }

    public void setMaxElt(int maxElt) {
        this.maxElt = maxElt;
    }

    public long getIdExo() {
        return idExo;
    }

    public void setIdExo(long idExo) {
        this.idExo = idExo;
    }

    public long getIdAgence() {
        return idAgence;
    }

    public void setIdAgence(long idAgence) {
        this.idAgence = idAgence;
    }

    public String getEmpsSearch() {
        return empsSearch;
    }

    public void setEmpsSearch(String empsSearch) {
        this.empsSearch = empsSearch;
    }

    public void loadRetenuEmploye(String matricule) {
        ParametreRequete p = new ParametreRequete(null, "employe", "XX", "LIKE", "AND");
        if ((matricule != null) ? !matricule.isEmpty() : false) {
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.retenue.contrat.employe.nom)", "employe", "%" + matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.retenue.contrat.employe.prenom)", "employe", "%" + matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.retenue.contrat.employe.matricule)", "employe", matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
        } else {
            p.setObjet(null);
        }
        po.addParam(p);
        loadAllDataRetenue(true, true);
    }

    public void loadAllDataRetenue(boolean avancer, boolean init) {
        po.addParam(new ParametreRequete("y.retenue.contrat.employe.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        po.addParam(new ParametreRequete("y.retenue.typeElement.retenue", "retenue", true, "=", "AND"));
        listeRetenues = po.executeDynamicQuery("YvsGrhDetailPrelevementEmps", "y.datePrelevement ASC, y.retenue.typeElement.id", avancer, init, maxElt, dao);
    }

    public void pagineDateRetenue(boolean next) {
        loadAllDataRetenue(next, false);
    }

    public void changeMaxData(ValueChangeEvent ev) {
        maxElt = (int) ev.getNewValue();
        loadAllDataRetenue(true, true);
    }

    public void addParamAgence(ValueChangeEvent ev) {
        //extrait les date de l'exercice
        ParametreRequete p = new ParametreRequete("y.retenue.contrat.employe.agence", "agence", null, " = ", "AND");
        if (ev.getNewValue() != null) {
            idAgence = (long) ev.getNewValue();
            if (idAgence > 0) {
                p.setObjet(new YvsAgences(idAgence));
            }
        }
        po.addParam(p);
        loadAllDataRetenue(true, true);
    }

    public void addParamExercice() {
        addParamExercice(false);
    }

    public void addParamExercice(boolean load) {
        //extrait les date de l'exercice
        ParametreRequete p = new ParametreRequete("y.datePrelevement", "datePrelevement", null, " BETWEEN ", "AND");
        if (idExo > 0) {
            YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findById", new String[]{"id"}, new Object[]{idExo});
            if (exo != null) {
                p.setObjet(exo.getDateDebut());
                p.setOtherObjet(exo.getDateFin());
                if (load) {
                    ManagedSalaire ws = (ManagedSalaire) giveManagedBean(ManagedSalaire.class);
                    if (ws != null) {
                        ws.setDateDebutSearch(exo.getDateDebut());
                        ws.setDateFinSearch(exo.getDateFin());
                        ws.setDateSearch(true);
                        ws.addParamDates();
                    }
                }
            }
        }
        po.addParam(p);
        loadAllDataRetenue(true, true);
    }

    public void addParamStatut_(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.statutReglement", "statutReglement", null, "=", "AND");
        if (ev.getNewValue() != null) {
            p.setObjet((char) ev.getNewValue());

        }
        po.addParam(p);
        loadAllDataRetenue(true, true);
    }

    public void addParamMoisYear(ValueChangeEvent ev) {
    }

    public void addParamMoisSalaire(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.datePrelevement", "datePrelevement", null, " BETWEEN ", "AND");
        if (ev.getNewValue() != null) {
            YvsGrhOrdreCalculSalaire head = (YvsGrhOrdreCalculSalaire) dao.loadOneByNameQueries("YvsGrhOrdreCalculSalaire.findById", new String[]{"id"}, new Object[]{((long) ev.getNewValue())});
            if (head != null) {
                p.setObjet(head.getDateDebutTraitement());
                p.setOtherObjet(head.getDateFinTraitement());
            }
        }
        po.addParam(p);
        loadAllDataRetenue(true, true);
    }

    public void addParamTypeElement_(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.retenue.typeElement", "typeElement", null, "=", "AND");
        if (ev.getNewValue() != null) {
            p.setObjet(new YvsGrhTypeElementAdditionel((Long) ev.getNewValue()));
        }
        po.addParam(p);
        loadAllDataRetenue(true, true);
    }

    /**
     * ***********************: CETTE PARTIE GERE LA VUE GESTION DES RETENUES
     */
    public void printRecapRetenue() {
        //choisir le rapport à imprimer       
        try {
            Map<String, Object> param_ = new HashMap<>();
            param_.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param_.put("AGENCE", currentUser.getAgence().getId());
            param_.put("TITRE", "LISTE DES RETENUES PERIODIQUE");
            param_.put("IMG_LOGO", returnLogo());
            param_.put("IMG_SIEGE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "siege.png"));
            param_.put("IMG_PHONE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "phone.png"));
            param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report" + FILE_SEPARATOR + ((true) ? "full" : "semi")) + FILE_SEPARATOR);
            executeReport("recap_retenue", param_, currentAgence.getId() + "");

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedMission.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printRecapitulatifRetenue(boolean actif) {
        //choisir le rapport à imprimer       
        try {
            Map<String, Object> param_ = new HashMap<>();
            ManagedSalaire service = (ManagedSalaire) giveManagedBean(ManagedSalaire.class);
            if (service != null) {
                param_.put("PERIODE", service.getListPlanification().get(service.getListPlanification().indexOf(new YvsGrhOrdreCalculSalaire(periodeSalaire))).getReference());
                param_.put("DATE_DEBUT", service.getListPlanification().get(service.getListPlanification().indexOf(new YvsGrhOrdreCalculSalaire(periodeSalaire))).getDateDebutTraitement());
                param_.put("DATE_FIN", service.getListPlanification().get(service.getListPlanification().indexOf(new YvsGrhOrdreCalculSalaire(periodeSalaire))).getDateFinTraitement());
            }
            param_.put("ID_AGENCE", (int) idAgence);
            param_.put("NAME_AGENCE", currentAgence.getSociete().getAgences().indexOf(new YvsAgences(idAgence).getDesignation()));
            param_.put("NAME_SOCIETE", currentAgence.getSociete().getName());
            executeReport((!actif) ? "etat_retenues" : "etat_retenues_emp_actif", param_, currentAgence.getId() + "");

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedMission.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printRecapRetenue(YvsGrhElementAdditionel el) {
        //choisir le rapport à imprimer       
        try {
            Map<String, Object> param_ = new HashMap<>();
            param_.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param_.put("AGENCE", currentUser.getAgence().getId());
            param_.put("TITRE", "RETENUES SPECIFIQUES");
            param_.put("ID_CONTRAT", el.getContrat().getId());
            param_.put("IMG_LOGO", returnLogo());
            param_.put("IMG_SIEGE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "siege.png"));
            param_.put("IMG_PHONE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "phone.png"));
            param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report" + FILE_SEPARATOR + ((true) ? "full" : "semi")) + FILE_SEPARATOR);
            executeReport("retenue_employe", param_, "retenue_employe" + el.getContrat().getEmploye().getMatricule());

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedMission.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isGenerer() {
        return generer;
    }

    public void setGenerer(boolean generer) {
        this.generer = generer;
    }

    public boolean isComptabiliseBean(ElementAdditionnel y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_RETENUE));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabilise(YvsGrhElementAdditionel y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_RETENUE));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public void comptabiliserRetenue() {
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (Util.asString(chaineSelectPlan) && w != null) {
            List<Integer> ids = decomposeSelection(chaineSelectPlan);
            if (ids != null ? !ids.isEmpty() : false) {
                for (Integer index : ids) {
                    w.comptabiliserRetenue(listRetenues.get(index), true);
                }
            }
        }
    }

}

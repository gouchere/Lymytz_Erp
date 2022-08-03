/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.TreeNode;
import yvs.dao.Options;
import yvs.entity.grh.param.poste.YvsGrhPosteDeTravail;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.param.poste.YvsGrhPosteEmployes;
import yvs.entity.param.YvsAgences;
import yvs.grh.bean.Employe;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.poste.PosteDeTravail;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class ManagedAffectations extends Managed<Affectation, YvsGrhPosteEmployes> implements Serializable {

    @ManagedProperty(value = "#{affectation}")
    private Affectation affectation;
    private YvsGrhEmployes employeSelect = new YvsGrhEmployes();
    private List<PosteDeTravail> postes;
    private List<Affectation> historiqueAffectation;
    private List<YvsGrhPosteEmployes> listeAffectations, selectedAffectations;
    private YvsGrhPosteEmployes selectAffectation = new YvsGrhPosteEmployes();
    private boolean updateAffectation;
    private String tabIds;

    private boolean displayPrevPoste; // affiche la zone de présentation de la fonction actuelle de l'employé
    /*Poste précédents*/

    /*--end poste précédent--*/
    private TreeNode selectedNodePoste;
    YvsGrhEmployes entityEmploye;

    private Date dateFinInterim = new Date(), dateConfirm = new Date();
    private boolean indemnisable;

    boolean initForm = true;

    public ManagedAffectations() {
        postes = new ArrayList<>();
        listeAffectations = new ArrayList<>();
        historiqueAffectation = new ArrayList<>();
        selectedAffectations = new ArrayList<>();
    }

    public YvsGrhPosteEmployes getSelectAffectation() {
        return selectAffectation;
    }

    public void setSelectAffectation(YvsGrhPosteEmployes selectAffectation) {
        this.selectAffectation = selectAffectation;
    }

    public boolean isUpdateAffectation() {
        return updateAffectation;
    }

    public void setUpdateAffectation(boolean updateAffectation) {
        this.updateAffectation = updateAffectation;
    }

    public YvsGrhEmployes getEntityEmploye() {
        return entityEmploye;
    }

    public void setEntityEmploye(YvsGrhEmployes entityEmploye) {
        this.entityEmploye = entityEmploye;
    }

    public boolean isInitForm() {
        return initForm;
    }

    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }

    public int getOffsetEmps() {
        return offsetEmps;
    }

    public void setOffsetEmps(int offsetEmps) {
        this.offsetEmps = offsetEmps;
    }

    public List<YvsGrhPosteEmployes> getListeAffectations() {
        return listeAffectations;
    }

    public void setListeAffectations(List<YvsGrhPosteEmployes> listeAffectations) {
        this.listeAffectations = listeAffectations;
    }

    public void setEmployeSelect(YvsGrhEmployes employeSelect) {
        this.employeSelect = employeSelect;
    }

    public YvsGrhEmployes getEmployeSelect() {
        return employeSelect;
    }

    public List<YvsGrhPosteEmployes> getSelectedAffectations() {
        return selectedAffectations;
    }

    public void setSelectedAffectations(List<YvsGrhPosteEmployes> selectedAffectations) {
        this.selectedAffectations = selectedAffectations;
    }

    public List<Affectation> getHistoriqueAffectation() {
        return historiqueAffectation;
    }

    public void setHistoriqueAffectation(List<Affectation> historiqueAffectation) {
        this.historiqueAffectation = historiqueAffectation;
    }

    public Affectation getAffectation() {
        return affectation;
    }

    public void setAffectation(Affectation affectation) {
        this.affectation = affectation;
    }

    public List<PosteDeTravail> getPostes() {
        return postes;
    }

    public void setPostes(List<PosteDeTravail> postes) {
        this.postes = postes;
    }

    public boolean isDisplayPrevPoste() {
        return displayPrevPoste;
    }

    public void setDisplayPrevPoste(boolean displayPrevPoste) {
        this.displayPrevPoste = displayPrevPoste;
    }

    public TreeNode getSelectedNodePoste() {
        return selectedNodePoste;
    }

    public void setSelectedNodePoste(TreeNode selectedNodePoste) {
        this.selectedNodePoste = selectedNodePoste;
    }

    public Date getDateFinInterim() {
        return dateFinInterim;
    }

    public void setDateFinInterim(Date dateFinInterim) {
        this.dateFinInterim = dateFinInterim;
    }

    public Date getDateConfirm() {
        return dateConfirm;
    }

    public void setDateConfirm(Date dateConfirm) {
        this.dateConfirm = dateConfirm;
    }

    public boolean isIndemnisable() {
        return indemnisable;
    }

    public void setIndemnisable(boolean indemnisable) {
        this.indemnisable = indemnisable;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public boolean controleFiche(Affectation bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Affectation recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteBean() {
        try {
            System.err.println("selectedAffectations = " + selectedAffectations);
            for (YvsGrhPosteEmployes p : selectedAffectations) {
                p.setAuthor(currentUser);
                affectation.getHistoriques().remove(p);
                dao.delete(p);
            }
            resetFiche();
            listeAffectations.removeAll(selectedAffectations);
            update("table_all_affectation");
            succes();
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer ces lignes !");
        }

    }

    public void opentTodelete(YvsGrhPosteEmployes pe) {
        selectedAffectations.clear();
        selectedAffectations.add(pe);
        openDialog("dlgDelPE");
    }

    public void activeLineAffectation(YvsGrhPosteEmployes pe) {
        if (pe.getStatut() == 'C') {
            //modifie le poste actif de l'employé 
            String rq = "UPDATE yvs_grh_poste_employes SET actif=false WHERE employe=?";
            Options[] param = new Options[]{new Options(pe.getEmploye().getId(), 1)};
            dao.requeteLibre(rq, param);
            pe.setActif(true);
            dao.update(pe);
            update("table_all_affectation");
        } else {
            getErrorMessage("Le statut de la ligne doit être confirmé !");
        }
    }

    @Override
    public void onSelectObject(YvsGrhPosteEmployes y) {
        selectAffectation = y;
        YvsGrhPosteEmployes pe = y;
        entityEmploye = pe.getEmploye();
        Affectation aff = UtilGrh.buildBeanAffectation(pe);
        cloneObject(affectation, aff);
        updateAffectation = true;
        update("panel_employe_affectation");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        execute("collapseForm('affectationPoste')");
        YvsGrhPosteEmployes pe = (YvsGrhPosteEmployes) ev.getObject();
        onSelectObject(pe);
        selectedAffectations.clear();
        selectedAffectations.add(pe);
       

    }

    @Override
    public void loadAll() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listeAffectations = dao.loadNameQueries("YvsPosteEmployes.findAll", champ, val, 0, 200);
    }

    public void loadAllAffectation(boolean next) {
        ParametreRequete p = new ParametreRequete("y.employe.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
        paginator.addParam(p);
        listeAffectations = paginator.executeDynamicQuery("YvsGrhPosteEmployes", "y.dateDebut DESC", next, initForm, (int) imax, dao);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsGrhPosteEmployes> re = paginator.parcoursDynamicData("YvsGrhPosteEmployes", "y", "y.dateDebut DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        initForm = true;
        loadAllAffectation(true);
    }

    public void changePage(ValueChangeEvent ev) {
        imax = (long) ev.getNewValue();
        initForm = true;
        loadAllAffectation(true);
    }

    public void pagineResult_(boolean avancer) {
        initForm = false;
        loadAllAffectation(avancer);
    }

    public void loadHistorique() {
        historiqueAffectation.clear();
        champ = new String[]{"employe"};
        YvsGrhEmployes e = null;
    }
    /**
     * Employé charge les employé en gérant la pagination
     */
    int offsetEmps;
    private boolean disPrevEmps = true, disNextEmps;

    public boolean isDisNextEmps() {
        return disNextEmps;
    }

    public boolean isDisPrevEmps() {
        return disPrevEmps;
    }

    public void setDisPrevEmps(boolean disPrevEmps) {
        this.disPrevEmps = disPrevEmps;
    }

    public void setDisNextEmps(boolean disNextEmps) {
        this.disNextEmps = disNextEmps;
    }

    private boolean controlePoste() {
        if (affectation.getEmploye().getId() <= 0) {
            getMessage("Vous devez choisir un employé !", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (affectation.getPoste().getId() <= 0) {
            getMessage("Vous devez choisir Le poste de travail !", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (affectation.isChangeAgence() && affectation.getAgence().getId() <= 0) {
            getErrorMessage("Vous avez indiquez un changement d'agence", "Veuillez indiquer l'agence !");
            return false;
        }
        return true;
    }

    public void savePoste() {
        if (controlePoste()) {
            selectAffectation = buildAffectation(affectation);
            //modifie le poste actif de l'employé 
            if (affectation.isPosteActif()) {
                String rq = "UPDATE yvs_grh_poste_employes SET actif=false WHERE employe=?";
                Options[] param = new Options[]{new Options(affectation.getEmploye().getId(), 1)};
                dao.requeteLibre(rq, param);
            }
            if (affectation.getId() <= 0) {
                selectAffectation.setId(null);
                selectAffectation = (YvsGrhPosteEmployes) dao.save1(selectAffectation);
                affectation.setId(selectAffectation.getId());
            } else {
                dao.update(selectAffectation);
            }
            int idx = affectation.getHistoriques().indexOf(selectAffectation);
            if (idx >= 0) {
                affectation.getHistoriques().set(idx, selectAffectation);
            } else {
                affectation.getHistoriques().add(0, selectAffectation);
            }
            idx = listeAffectations.indexOf(selectAffectation);
            if (idx >= 0) {
                listeAffectations.set(idx, selectAffectation);
            } else {
                listeAffectations.add(0, selectAffectation);
            }
            //si l'agence à changer, modifier l'agence de la fiche employé !
            if (affectation.getAgence().getId() > 0) {
                entityEmploye.setAgence(new YvsAgences(affectation.getAgence().getId()));
                entityEmploye.setAuthor(currentUser);
                dao.update(entityEmploye);
            }
            succes();
        }
    }

    private YvsGrhPosteEmployes buildAffectation(Affectation aff) {
        YvsGrhPosteEmployes post = new YvsGrhPosteEmployes();
        post.setId(aff.getId());
        post.setValider(aff.isValider());
        post.setIndemnisable(aff.isIndemnisable());
        post.setDateDebut(aff.getDateDebut());
        post.setDateFinInterim(aff.getDateFinInterim());
        post.setDateAcquisition(aff.getDate());
        aff.getEmploye().setPosteDeTravail(aff.getPoste());
        post.setEmploye(UtilGrh.buildEmployeEntity(aff.getEmploye()));
        post.setActif(aff.isPosteActif());
        post.setStatut((aff.getStatut() == ' ') ? Constantes.STATUT_DOC_EDITABLE : aff.getStatut());
        post.setPoste(UtilGrh.buildBeanPoste(aff.getPoste()));
        post.setPostePrecedent((aff.getPostePrecedent().getId() > 0) ? UtilGrh.buildBeanPoste(aff.getPostePrecedent()) : null);
        post.setMotifAffectation(aff.getMotifAffectation());
        post.setDateUpdate(new Date());
        post.setDateSave(aff.getDateSave());
        post.setAuthor(currentUser);
        post.setAgence((aff.getAgence().getId() > 0) ? new YvsAgences(aff.getAgence().getId()) : currentAgence);
        return post;
    }

    @Override
    public void populateView(Affectation bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void populateViewEmploye(Employe bean) {
//        affectation.setEmploye(bean);
        cloneObject(affectation.getEmploye(), bean);
        affectation.setPostePrecedent(bean.getPosteDeTravail());
        //charge l'historique des affectations
        champ = new String[]{"employe"};
        val = new Object[]{new YvsGrhEmployes(bean.getId())};
        affectation.getEmploye().setAffectations(UtilGrh.buildBeanListePoste(dao.loadNameQueries("YvsPosteEmployes.findByEmploye", champ, val)));
        affectation.setAgence(new Agence(bean.getAgence().getId()));
    }

    public void loadViewEmploye(SelectEvent ev) {
        entityEmploye = (YvsGrhEmployes) ev.getObject();
        choixEmploye1(entityEmploye);
    }

    public void saisirEmploye(String matricule) {
        if (matricule != null) {
            if (!matricule.trim().isEmpty()) {
                ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
                if (service != null) {
                    service.findEmploye(matricule);
                    affectation.getEmploye().setError(true);
                    if (service.getListEmployes().size() == 1) {
                        affectation.getEmploye().setError(false);
                        choixEmploye1(service.getListEmployes().get(0));
                    } else {
                        if (!service.getListEmployes().isEmpty()) {
                            affectation.getEmploye().setError(false);
                            openDialog("dlgEmploye");
                        }
                    }
                }
            }
        }
    }

    public void choixEmploye1(YvsGrhEmployes ev) {
        entityEmploye = ev;
        populateViewEmploye(UtilGrh.buildBeanSimplePartialEmploye(ev));
        List<YvsGrhPosteEmployes> l = dao.loadNameQueries("YvsPosteEmployes.findByEmploye", new String[]{"employe"}, new Object[]{ev});
        affectation.setHistoriques(l);
        affectation.setPostePrecedent(UtilGrh.builSimpledBeanPoste(ev.getPosteActif()));
        update("tabEmployes_Affectation");
    }

    public void unLoadViewEmploye(SelectEvent ev) {
        resetFiche();
    }

    @Override
    public void resetFiche() {
        resetFiche(affectation);
        affectation.setEmploye(new Employe());
        affectation.getEmploye().setPosteDeTravail(new PosteDeTravail());
        affectation.setPoste(new PosteDeTravail());
        affectation.setPostePrecedent(new PosteDeTravail());
        affectation.setAgence(new Agence());
        affectation.setChangeAgence(false);
        affectation.getHistoriques().clear();
        updateAffectation = false;
        selectAffectation = new YvsGrhPosteEmployes();
    }

    @Override
    public void resetPage() {
        selectedAffectations.clear();
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
//
    }

//    public void saisirPoste(String titre) {
//        if (titre != null) {
//            if (!titre.trim().isEmpty()) {
//                ManagedPosteDeTravail service = (ManagedPosteDeTravail) giveManagedBean(ManagedPosteDeTravail.class);
//                if (service != null) {
//                    service.sear(titre);
//                    affectation.getEmploye().setError(true);
//                    if (service.getListEmployes().size() == 1) {
//                        affectation.getEmploye().setError(false);
//                        choixEmploye1(service.getListEmployes().get(0));
//                    } else {
//                        if (!service.getListEmployes().isEmpty()) {
//                            affectation.getEmploye().setError(false);
//                            openDialog("dlgEmploye");
//                            update("tabEmployes_Affectation");
//                        }
//                    }
//                }
//            }
//        }
//    }
    public void choixNodePoste(NodeSelectEvent ev) {
        YvsGrhPosteDeTravail post = (YvsGrhPosteDeTravail) ev.getTreeNode().getData();
        if (post != null) {
            cloneObject(affectation.getPostePrecedent(), affectation.getPoste());
            affectation.setPoste(UtilGrh.builSimpledBeanPoste(post));
            update("panel_employe_affectation");
        }
    }

    public void chageStateAffectation(char statu) {
        affectation.setStatut(statu);
        selectAffectation.setStatut(statu);
        selectAffectation.setAuthor(currentUser);
        if (statu == 'C') {
            if (dateConfirm != null) {
                selectAffectation.setDateConfirmation(dateConfirm);
                selectAffectation.setActif(true);
                affectation.setDateConfirmation(dateConfirm);
            } else {
                getErrorMessage("Vous devez indiquer la date de confirmation de l'intérim");
            }
        }
        if (statu == 'I') {
            if (dateFinInterim != null) {
                selectAffectation.setDateConfirmation(dateFinInterim);
                selectAffectation.setIndemnisable(indemnisable);
                affectation.setIndemnisable(indemnisable);
                affectation.setDateFinInterim(dateFinInterim);
            } else {
                getErrorMessage("Vous devez indiquer la date de fin de l'intérim !");
            }
        }
        if (selectAffectation.getActif()) {
            String rq = "UPDATE yvs_grh_poste_employes SET actif=false WHERE employe=?";
            Options[] param = new Options[]{new Options(selectAffectation.getEmploye().getId(), 1)};
            dao.requeteLibre(rq, param);
            for (YvsGrhPosteEmployes pe : affectation.getHistoriques()) {
                if (!pe.equals(selectAffectation)) {
                    pe.setActif(false);
                }
            }
        }
        dao.update(selectAffectation);
        //change le poste actif  de l'employé
        String rq = "UPDATE yvs_grh_employes SET poste_actif=? WHERE id=?";
        Options[] param = new Options[]{new Options(selectAffectation.getPoste().getId(), 1), new Options(selectAffectation.getEmploye().getId(), 2)};
        dao.requeteLibre(rq, param);
        int idx = listeAffectations.indexOf(selectAffectation);
        if (idx >= 0) {
            listeAffectations.set(idx, selectAffectation);
        } else {
            listeAffectations.add(0, selectAffectation);
        }
        if (affectation != null) {
            idx = affectation.getHistoriques().indexOf(selectAffectation);
            if (idx >= 0) {
                affectation.getHistoriques().set(idx, selectAffectation);
            } else {
                affectation.getHistoriques().add(0, selectAffectation);
            }
            update("tab-Af-01");
        }
        succes();
        update("table_all_affectation");
    }
    /**
     * Traitement des recherche et options de recherche*
     */
    private int optionSearchAff = 1;
    private String employeF;
    private String posteF;
    private Character selectionStatu;
    private Date dateDF = new Date();
    private Date dateFF = new Date();
    private Boolean paramDate;
    private Boolean actifF;

    public String getEmployeF() {
        return employeF;
    }

    public void setEmployeF(String employeF) {
        this.employeF = employeF;
    }

    public Character getSelectionStatu() {
        return selectionStatu;
    }

    public void setSelectionStatu(Character selectionStatu) {
        this.selectionStatu = selectionStatu;
    }

    public Date getDateDF() {
        return dateDF;
    }

    public void setDateDF(Date dateDF) {
        this.dateDF = dateDF;
    }

    public Date getDateFF() {
        return dateFF;
    }

    public void setDateFF(Date dateFF) {
        this.dateFF = dateFF;
    }

    public int getOptionSearchAff() {
        return optionSearchAff;
    }

    public void setOptionSearchAff(int optionSearchAff) {
        this.optionSearchAff = optionSearchAff;
    }

    public Boolean getActifF() {
        return actifF;
    }

    public void setActifF(Boolean actifF) {
        this.actifF = actifF;
    }

    public String getPosteF() {
        return posteF;
    }

    public void setPosteF(String posteF) {
        this.posteF = posteF;
    }

    public Boolean getParamDate() {
        return paramDate;
    }

    public void setParamDate(Boolean paramDate) {
        this.paramDate = paramDate;
    }

    public void addParamDate(boolean b) {
        ParametreRequete p = new ParametreRequete("y.dateDebut", "dateDebut", null, " BETWEEN ", "AND");
        if (b) {
            if (dateDF != null && dateFF != null) {
                if (dateDF.before(dateFF) || dateDF.equals(dateFF)) {
                    p.setObjet(dateDF);
                    p.setOtherObjet(dateFF);
                }
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllAffectation(true);
    }

    public void addParamDate1(SelectEvent ev) {
        addParamDate(paramDate);
    }

    public void findByEmploye(String matriculeF) {
        ParametreRequete p0 = new ParametreRequete(null, "emps", null, " LIKE ", "AND");
        if ((matriculeF != null) ? !matriculeF.isEmpty() : false) {
            p0 = new ParametreRequete(null, "employe", "%" + matriculeF.toUpperCase() + "%", " LIKE ", "AND");
            ParametreRequete p01 = new ParametreRequete("UPPER(y.employe.nom)", "employe", "%" + matriculeF.toUpperCase() + "%", " LIKE ", "OR");
            ParametreRequete p02 = new ParametreRequete("UPPER(y.employe.prenom)", "employe", "%" + matriculeF.toUpperCase() + "%", " LIKE ", "OR");
            ParametreRequete p03 = new ParametreRequete("UPPER(y.employe.matricule)", "employe", "%" + matriculeF.toUpperCase() + "%", " LIKE ", "OR");
            ParametreRequete p04 = new ParametreRequete("UPPER(concat(y.employe.nom,' ',y.employe.prenom))", "employe", "%" + matriculeF.toUpperCase() + "%", " LIKE ", "OR");
            ParametreRequete p05 = new ParametreRequete("UPPER(concat(y.employe.prenom,' ',y.employe.nom))", "employe", "%" + matriculeF.toUpperCase() + "%", " LIKE ", "OR");
            p0.getOtherExpression().add(p01);
            p0.getOtherExpression().add(p02);
            p0.getOtherExpression().add(p03);
            p0.getOtherExpression().add(p04);
            p0.getOtherExpression().add(p05);
        }
        paginator.addParam(p0);
        initForm = true;
        loadAllAffectation(true);
    }

    public void findByPoste(String poste) {
        ParametreRequete p = new ParametreRequete("UPPER(y.poste.intitule)", "poste", null, " LIKE ", "AND");
        if ((poste != null) ? !poste.isEmpty() : false) {
            p.setObjet(poste.toUpperCase() + "%");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllAffectation(true);
    }

    public void findByStatut(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.statut", "statut", null, "=", "AND");
        if (ev.getNewValue() != null) {
            Character statu = (Character) ev.getNewValue();
            p.setObjet(statu);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllAffectation(true);
    }

    public void findByStatutActif(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", null, "=", "AND");
        if (ev.getNewValue() != null) {
            Boolean statu = (Boolean) ev.getNewValue();
            p.setObjet(statu);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllAffectation(true);
    }

    public String giveStatut(char c) {
        switch (c) {
            case 'A':
                return "Abandonné";
            case 'I':
                return "Intérim";
            case 'E':
                return "En cours";
            case 'C':
                return "Confirmé";
        }
        return "";
    }

}

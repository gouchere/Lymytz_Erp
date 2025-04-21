/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.poste;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import yvs.base.tresoreri.ModePaiement;
import yvs.dao.Options;
import yvs.entity.grh.activite.YvsGrhDetailGrilleFraiMission;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.grh.activite.YvsGrhGrilleMission;
import yvs.entity.grh.contrat.YvsGrhModelElementAdditionel;
import yvs.entity.grh.contrat.YvsGrhTypeElementAdditionel;
import yvs.entity.grh.param.YvsGrhDomainesQualifications;
import yvs.entity.grh.param.YvsGrhExperiencePoste;
import yvs.entity.grh.param.YvsGrhActivitesPoste;
import yvs.entity.grh.param.poste.YvsGrhDepartement;
import yvs.entity.grh.param.YvsGrhMissionPoste;
import yvs.entity.grh.param.poste.YvsGrhPosteDeTravail;
import yvs.entity.grh.param.poste.YvsGrhQualificationPoste;
import yvs.entity.grh.param.YvsGrhQualifications;
import yvs.entity.grh.param.poste.YvsGrhModelContrat;
import yvs.entity.grh.param.poste.YvsGrhModelPrimePoste;
import yvs.entity.grh.param.poste.YvsGrhPreavis;
import yvs.entity.grh.personnel.YvsDiplomes;
import yvs.entity.grh.personnel.YvsSpecialiteDiplomes;
import yvs.entity.grh.salaire.YvsGrhStructureSalaire;
import yvs.entity.param.YvsModePaiement;
import yvs.entity.param.YvsSocietes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.mission.DetailFraisMission;
import yvs.grh.bean.Diplomes;
import yvs.grh.bean.DomainesQualifications;
import yvs.grh.bean.ExperienceRequise;
import yvs.grh.bean.mission.GrilleFraisMission;
import yvs.grh.bean.Qualification;
import yvs.grh.bean.RegleDeTache;
import yvs.grh.bean.TypeCout;
import yvs.grh.bean.TypeElementAdd;
import yvs.grh.contrat.ModelContrat;
import yvs.grh.paie.StructureElementSalaire;
import yvs.parametrage.societe.Societe;
import yvs.parametrage.societe.UtilSte;
import yvs.util.Managed;
import yvs.util.Nodes;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class ManagedPosteDeTravail extends Managed<PosteDeTravail, YvsGrhPosteDeTravail> implements Serializable {
    
    @ManagedProperty(value = "#{posteDeTravail}")
    private PosteDeTravail poste;
//    private List<YvsGrhDepartement> departements;
    private List<YvsGrhPosteDeTravail> listPostes, listSelection, postesSuperieurs;
    private YvsGrhPosteDeTravail selectPoste = new YvsGrhPosteDeTravail();
    private List<YvsDiplomes> diplomes;
    private List<YvsSpecialiteDiplomes> specialites;
//    private List<YvsGrhQualifications> qualifications;
    private List<YvsGrhDomainesQualifications> domaines;
    private boolean displayDelQualif, displayExperience;
    private String codeSearch;
    private String chaineSelectPoste;
//    private PosteDeTravail newPoste = new PosteDeTravail();
    private DomainesQualifications domaine = new DomainesQualifications();
//    private List<ModePaiement> listModePaiement;
//    private List<RegleDeTache> reglesDeTaches;

    private boolean viewBtnModel = false;
    private String tabIds;
    private List<YvsGrhModelContrat> models;
    private ModelContrat model = new ModelContrat();
    private ElementAdditionnelPoste modelPrime = new ElementAdditionnelPoste();
    
    public ManagedPosteDeTravail() {
        models = new ArrayList<>();
        listPostes = new ArrayList<>();
        postesSuperieurs = new ArrayList<>();
//        qualifications = new ArrayList<>();
        diplomes = new ArrayList<>();
        listeGrilleFrais = new ArrayList<>();
        experiences = new ArrayList<>();
        domaines = new ArrayList<>();
//        reglesDeTaches = new ArrayList<>();
        specialites = new ArrayList<>();
        modelePrimes = new ArrayList<>();
        listSociete = new ArrayList<>();
        
        listSelection = new ArrayList<>();
    }
    
    public YvsGrhPosteDeTravail getSelectPoste() {
        return selectPoste;
    }
    
    public void setSelectPoste(YvsGrhPosteDeTravail selectPoste) {
        this.selectPoste = selectPoste;
    }
    
    public boolean isViewBtnModel() {
        return viewBtnModel;
    }
    
    public void setViewBtnModel(boolean viewBtnModel) {
        this.viewBtnModel = viewBtnModel;
    }
    
    public ElementAdditionnelPoste getModelPrime() {
        return modelPrime;
    }
    
    public void setModelPrime(ElementAdditionnelPoste modelPrime) {
        this.modelPrime = modelPrime;
    }
    
    public ModelContrat getModel() {
        return model;
    }
    
    public void setModel(ModelContrat model) {
        this.model = model;
    }
    
    public List<YvsGrhModelContrat> getModels() {
        return models;
    }
    
    public void setModels(List<YvsGrhModelContrat> models) {
        this.models = models;
    }
    
    public boolean isUpdate() {
        return update;
    }
    
    public void setUpdate(boolean update) {
        this.update = update;
    }
    
    public int getFirstMission() {
        return firstMission;
    }
    
    public void setFirstMission(int firstMission) {
        this.firstMission = firstMission;
    }
    
    public int getFirstPoste() {
        return firstPoste;
    }
    
    public void setFirstPoste(int firstPoste) {
        this.firstPoste = firstPoste;
    }
    
    public boolean isUpdateFraisM() {
        return updateFraisM;
    }
    
    public void setUpdateFraisM(boolean updateFraisM) {
        this.updateFraisM = updateFraisM;
    }
    
    public boolean isUpdateMiss() {
        return updateMiss;
    }
    
    public void setUpdateMiss(boolean updateMiss) {
        this.updateMiss = updateMiss;
    }
    
    public boolean isUpdateActivite() {
        return updateActivite;
    }
    
    public void setUpdateActivite(boolean updateActivite) {
        this.updateActivite = updateActivite;
    }
    
    public boolean isUpdatePrime() {
        return updatePrime;
    }
    
    public void setUpdatePrime(boolean updatePrime) {
        this.updatePrime = updatePrime;
    }
    
    public boolean isUpdateTypePrime() {
        return updateTypePrime;
    }
    
    public void setUpdateTypePrime(boolean updateTypePrime) {
        this.updateTypePrime = updateTypePrime;
    }
    
    public void setSpecialites(List<YvsSpecialiteDiplomes> specialites) {
        this.specialites = specialites;
    }
    
    public List<YvsSpecialiteDiplomes> getSpecialites() {
        return specialites;
    }

//    public List<RegleDeTache> getReglesDeTaches() {
//        return reglesDeTaches;
//    }
//
//    public void setReglesDeTaches(List<RegleDeTache> reglesDeTaches) {
//        this.reglesDeTaches = reglesDeTaches;
//    }
    public DomainesQualifications getDomaine() {
        return domaine;
    }
    
    public void setDomaine(DomainesQualifications domaine) {
        this.domaine = domaine;
    }
    
    public List<YvsGrhDomainesQualifications> getDomaines() {
        return domaines;
    }
    
    public void setDomaines(List<YvsGrhDomainesQualifications> domaines) {
        this.domaines = domaines;
    }
    
    public String getChaineSelectPoste() {
        return chaineSelectPoste;
    }
    
    public void setChaineSelectPoste(String chaineSelectPoste) {
        this.chaineSelectPoste = chaineSelectPoste;
    }
    
    public List<YvsGrhGrilleMission> getListeGrilleFrais() {
        return listeGrilleFrais;
    }
    
    public void setListeGrilleFrais(List<YvsGrhGrilleMission> listeGrilleFrais) {
        this.listeGrilleFrais = listeGrilleFrais;
    }
    
    public String getCodeSearch() {
        return codeSearch;
    }
    
    public void setCodeSearch(String codeSearch) {
        this.codeSearch = codeSearch;
    }
    
    public List<YvsGrhPosteDeTravail> getPostesSuperieurs() {
        return postesSuperieurs;
    }
    
    public void setPostesSuperieurs(List<YvsGrhPosteDeTravail> postesSuperieurs) {
        this.postesSuperieurs = postesSuperieurs;
    }
    
    public boolean isDisplayDelQualif() {
        return displayDelQualif;
    }
    
    public void setDisplayDelQualif(boolean displayDelQualif) {
        this.displayDelQualif = displayDelQualif;
    }
    
    public boolean isDisplayExperience() {
        return displayExperience;
    }
    
    public void setDisplayExperience(boolean displayExperience) {
        this.displayExperience = displayExperience;
    }
    
    public List<YvsDiplomes> getDiplomes() {
        return diplomes;
    }
    
    public void setDiplomes(List<YvsDiplomes> diplomes) {
        this.diplomes = diplomes;
    }

//    public List<YvsGrhQualifications> getQualifications() {
//        return qualifications;
//    }
//
//    public void setQualifications(List<YvsGrhQualifications> qualifications) {
//        this.qualifications = qualifications;
//    }
//    public void setDepartements(List<YvsGrhDepartement> departements) {
//        this.departements = departements;
//    }
//
//    public List<YvsGrhDepartement> getDepartements() {
//        return departements;
//    }
    public PosteDeTravail getPoste() {
        return poste;
    }
    
    public void setPoste(PosteDeTravail poste) {
        this.poste = poste;
    }
    
    public List<YvsGrhPosteDeTravail> getListPostes() {
        return listPostes;
    }
    
    public void setListPostes(List<YvsGrhPosteDeTravail> listPostes) {
        this.listPostes = listPostes;
    }
    
    public List<YvsGrhPosteDeTravail> getListSelection() {
        return listSelection;
    }
    
    public void setListSelection(List<YvsGrhPosteDeTravail> listSelection) {
        this.listSelection = listSelection;
    }
    
    public void selectDepartement(SelectEvent ev) {
        Departements dep = (Departements) ev.getObject();
        poste.setDepartement(dep);
        update("depart-poste");
        closeDialog("dlgDepartement");
    }
    
    @Override
    public void onSelectObject(YvsGrhPosteDeTravail y) {
        choixPoste(y);
        update("global_form_poste");
    }
    
    public void selectedLine(SelectEvent ev) {
//        execute("collapseForm('posteTravail')");
        YvsGrhPosteDeTravail p = (YvsGrhPosteDeTravail) ev.getObject();
        choixPoste(p);
//        execute("oncollapsesForm('posteTravail')");
    }
    
    private void choixPoste(YvsGrhPosteDeTravail p) {
        selectPoste = p;
        populateView(UtilGrh.buildBeanPoste(p));
        loadPosteSuperieur(p);
        poste.setQualifications(dao.loadNameQueries("YvsQualificationPoste.findByPoste", new String[]{"poste"}, new Object[]{p}));
        update = true;
    }
    
    @Override
    public boolean controleFiche(PosteDeTravail bean) {
        if ((poste.getIntitule() != null) ? poste.getIntitule().trim().equals("") : false) {
            getMessage("veuillez préciser la désignation du poste", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }
    
    @Override
    public PosteDeTravail recopieView() {
        return new PosteDeTravail();
    }
    
    @Override
    public void populateView(PosteDeTravail bean) {
        cloneObject(poste, bean);
        poste.setDepartement(bean.getDepartement());
        poste.setNiveauRequis(bean.getNiveauRequis());
        if (!listeGrilleFrais.contains(new YvsGrhGrilleMission(bean.getFraisMission().getId())) && bean.getFraisMission().getId() > 0) {
            listeGrilleFrais.add(new YvsGrhGrilleMission(bean.getFraisMission().getId()));
        }
        poste.setActif(bean.isActif());
        poste.setIdPosteSup(bean.getIdPosteSup());
        cloneObject(model, bean.getModelContrat());
        update = true;
        // charge la liste des expériences requises   
        champ = new String[]{"poste"};
        val = new Object[]{poste.getId()};
        List<YvsGrhExperiencePoste> lp = dao.loadNameQueries("YvsExperiencePoste.findByPoste", champ, val);
        experiences.clear();
        for (YvsGrhExperiencePoste ex : lp) {
            experiences.add(new ExperienceRequise(ex.getId(), new PosteDeTravail(0, ex.getPoste()), ex.getDuree()));
        }
    }
    
    @Override
    public void loadOnView(SelectEvent ev) {
//        PosteDeTravail p = (PosteDeTravail) ev.getObject();
//        if (p != null) {
//            poste.setDegre(p.getDegre());
//            poste.setDepartement(p.getDepartement());
//            poste.setDescription(p.getDescription());
//            poste.setId(p.getId());
//            poste.setIntitule(p.getIntitule());
//            poste.setNiveauRequis(p.getNiveauRequis());
//            //charge la liste des qualifications requises
//            String champ[] = new String[]{"poste"};
//            Object val[] = new Object[]{poste.getId()};
//            List<YvsQualificationPoste> lq = dao.loadNameQueries("YvsGrhQualificationPoste.findByPosteDepartement", champ, val);
//            listQualification.clear();
//            for (YvsGrhQualificationPoste qp : lq) {
//                listQualification.add(new Qualification(qp.getYvsQualifications().getId(), qp.getYvsQualifications().getDesignation()));
//            }
//            //charge la liste des expériences requises   
////            champ[0] = "poste";
////            val[0] = poste.getId();
////            List<YvsExperiencePoste> lp = dao.loadNameQueries("YvsGrhExperiencePoste.findByPosteService", champ, val);
////            experiences.clear();
////            for (YvsGrhExperiencePoste ex : lp) {
////                experiences.add(new ExperienceRequise(ex.getId(), new PosteDeTravail(0, ex.getPoste()), ex.getDuree()));
////            }
//            displayOnglet = true;
//            update("tabview-poste");
//        }
    }
    
    @Override
    public void updateBean() {
        if (controleFiche(poste)) {
            YvsGrhPosteDeTravail post = buildPoste(poste);
            post.setId(poste.getId());
            //le poste supérieure ne doit pas se trouver parmi les poste inférieur du poste de travail
            if (listPostes.get(listPostes.indexOf(post)).getListPostesInferieur().contains(post.getPosteSuperieur())) {
                getErrorMessage("Erreur de liaison cyclique sur votre formulaire !");
                return;
            }
            dao.update(post);
            update = true;
            succes();
            if (poste.getFraisMission().getId() > 0) {
                int idx = listeGrilleFrais.indexOf(new YvsGrhGrilleMission(poste.getFraisMission().getId()));
                if (idx >= 0) {
                    poste.setFraisMission(UtilGrh.buildSimpleGrilleMission(listeGrilleFrais.get(idx)));
                }
                
            }
            if (poste.getNiveauRequis().getId() > 0) {
                poste.setNiveauRequis(UtilGrh.buildDiplome(post.getNiveau()));
            }
            listPostes.set(listPostes.indexOf(post), post);
        }
    }
    
    private boolean update;
    int firstMission = 0;   //contrôle la pagination des grilles de frais de mission
    int firstPoste = 0;  //contrôle la pagination des postes de travaille

    /**
     *
     * @return
     */
    @Override
    public boolean saveNew() {
        if (controleFiche(poste)) {
            if (model != null ? model.getId() > 0 : false) {
                poste.setModelContrat(model);
            }
            if (!update) {
                YvsGrhPosteDeTravail post = buildPoste(poste);
                post.setId(null);
                post = (YvsGrhPosteDeTravail) dao.save1(post);
                poste.setId(post.getId());
                succes();
                if (poste.getFraisMission().getId() > 0) {
                    int idx = listeGrilleFrais.indexOf(new YvsGrhGrilleMission(poste.getFraisMission().getId()));
                    if (idx >= 0) {
                        poste.setFraisMission(UtilGrh.buildSimpleGrilleMission(listeGrilleFrais.get(idx)));
                    }
                }
//                if (poste.getNiveauRequis().getId() > 0) {
//                    poste.setNiveauRequis(diplomes.get(diplomes.indexOf(poste.getNiveauRequis())));
//                } 
//                cloneObject(p, poste);
//                p.setDepartement(departements.get(departements.indexOf(p.getDepartement())));
                listPostes.add(0, post);
            } else {
                updateBean();
            }
            actionOpenOrResetAfter(this);
        }
        return update = true;
    }
    
    private YvsGrhPosteDeTravail buildPoste(PosteDeTravail poste) {
        YvsGrhPosteDeTravail post = new YvsGrhPosteDeTravail();
        post.setIntitule(poste.getIntitule());
        post.setActif(poste.isActif());
        post.setAuthor(currentUser);
        post.setNombrePlace(poste.getNombrePlace());
        post.setCongeAcquis(poste.getCongeAcquis());
        post.setDureePreavis(poste.getDureePreavie());
        post.setModePaiement((poste.getModePaiement().getId() > 0) ? new YvsModePaiement(poste.getModePaiement().getId()) : null);
        post.setSalaireHoraire(poste.getSalaireHoraire());
        post.setSalaireMensuel(poste.getSalaireMensuel());
        post.setStructureSalaire((poste.getStructSalaire().getId() > 0) ? new YvsGrhStructureSalaire(poste.getStructSalaire().getId()) : null);
        post.setUniteDureePreavis(poste.getUnitePreavis());
        post.setGrade(poste.getGrade());
        post.setDateUpdate(new Date());
        post.setDateSave(poste.getDateSave());
        if (poste.getIdPosteSup() > 0) {
            post.setPosteSuperieur(postesSuperieurs.get(postesSuperieurs.indexOf(new YvsGrhPosteDeTravail(poste.getIdPosteSup()))));
        }
        if (poste.getNiveauRequis().getId() > 0) {
            post.setNiveau(diplomes.get(diplomes.indexOf(new YvsDiplomes(poste.getNiveauRequis().getId()))));
        }
        if (poste.getFraisMission().getId() > 0) {
            post.setFraisMission(new YvsGrhGrilleMission(poste.getFraisMission().getId()));
        } else {
            post.setFraisMission(null);
        }
        if (poste.getDepartement().getId() > 0) {
            post.setDepartement(UtilGrh.buildBeanDepartement(poste.getDepartement()));
        } else {
            post.setDepartement(null);
        }
        if (poste.getModelContrat().getId() > 0) {
            post.setModelContrat(new YvsGrhModelContrat(poste.getModelContrat().getId()));
        }
        return post;
    }
    
    @Override
    public void loadAll() {
        loadPosteSuperieur(null);
        loadPosteDetravail(true, true);
        diplomes = dao.loadNameQueries("YvsDiplomes.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
//        loadDomaines();
//        //charge toutes les qualifications
//        loadQualification();
//        //charge les grille e frais de mission
//        loadListGrille(100);
//        //charge les règles de tâches
//        loadRegleTache();
//        //charge les spécialités
//        loadSpecialite();
        //charge les sociétés
//        loadAllSociete();
//        //charge les primes
//        loadTypePrime();
//        //charge les modèles de primes 
//        loadModelePrime();
//        //charge les modèles de contrat
//        loadModeleContrat();
    }
    
    public void loadModeleContrat() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        models = dao.loadNameQueries("YvsGrhModelContrat.findAll", champ, val);
    }
    
    public void loadDomaines() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        domaines = dao.loadNameQueries("YvsGrhDomainesQualifications.findAll", champ, val);
    }
    
    public void loadListGrille(int limit) {
        PaginatorResult<YvsGrhGrilleMission> pagegin = new PaginatorResult<>();
        String[] param = new String[]{"societe"};
        Object[] value = new Object[]{currentUser.getAgence().getSociete()};
        String nameQuery = "YvsGrilleMission.findAll";
        pagegin = pagegin.loadResult("YvsGrilleMission.countAll", nameQuery, param, value, 0, limit, dao);
        listeGrilleFrais = pagegin.getResult();
    }
    
    @Override
    public void resetFiche() {
        resetFiche(poste);
        poste.setDepartement(new Departements());
        poste.setFraisMission(new GrilleFraisMission());
        poste.setNiveauRequis(new Diplomes());
        poste.setModePaiement(new ModePaiement());
        poste.setRegle(new RegleDeTache());
        poste.setStructSalaire(new StructureElementSalaire());
        poste.setModelContrat(new ModelContrat());
        experiences.clear();
        poste.getListEmployes().clear();
        poste.getMissions().clear();
        poste.getPrimes().clear();
        poste.getQualifications().clear();
        update = false;
        loadPosteSuperieur(null);
        update("tabview-poste");
    }
    
    public void deletePoste() {
        System.err.println("listSelection " + listSelection.size());
        try {
            for (YvsGrhPosteDeTravail p : listSelection) {
                p.setAuthor(currentUser);
                dao.delete(p);
                listPostes.remove(p);
                update("tabview-poste");
            }
            succes();
            update("poste-poste");
            update("form-poste-000");
            resetFiche();
            
        } catch (Exception ex) {
            getMessage("Impossible d'effectuer cette opération", FacesMessage.SEVERITY_ERROR);
        }
    }
    
    public void loadPosteSuperieur(YvsGrhPosteDeTravail y) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            champ = new String[]{"societe", "id"};
            val = new Object[]{currentAgence.getSociete(), y.getId()};
            nameQueri = "YvsPosteDeTravail.findByNotId";
        } else {
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            nameQueri = "YvsPosteDeTravail.findAll";
        }
        postesSuperieurs = dao.loadNameQueries(nameQueri, champ, val);
    }
    
    public void loadPosteDetravail(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.departement.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        String query = "YvsGrhPosteDeTravail y JOIN FETCH y.departement LEFT JOIN FETCH y.niveau LEFT JOIN FETCH y.posteSuperieur LEFT JOIN FETCH y.modelContrat LEFT JOIN FETCH y.fraisMission";
        listPostes = paginator.executeDynamicQuery("y", "y", query, "y.intitule", avance, init, (int) imax, dao);
        update("poste-poste");
    }
    
    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        String query = "YvsGrhPosteDeTravail y JOIN FETCH y.departement LEFT JOIN FETCH y.niveau LEFT JOIN FETCH y.posteSuperieur LEFT JOIN FETCH y.modelContrat LEFT JOIN FETCH y.fraisMission";
        List<YvsGrhPosteDeTravail> re = paginator.parcoursDynamicData(query, "y", "", "y.intitule", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }
    
    public void paginer(boolean next) {
        loadPosteDetravail(next, false);
    }
    
    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadPosteDetravail(true, true);
    }
    
    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadPosteDetravail(true, true);
    }
    
    public void loadSpecialite() {
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        specialites = dao.loadNameQueries("YvsSpecialiteDiplomes.findAll", champ, val);
    }

    /**
     * gerer les qualifications au poste*
     */
    private Qualification newQualif = new Qualification();
    
    public Qualification getNewQualif() {
        return newQualif;
    }
    
    public void setNewQualif(Qualification newQualif) {
        this.newQualif = newQualif;
    }
    
    private long idQualificationRequise;
    
    public long getIdQualificationRequise() {
        return idQualificationRequise;
    }
    
    public void setIdQualificationRequise(long idQualificationRequise) {
        this.idQualificationRequise = idQualificationRequise;
    }
    
    public void choixQualification(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            int id = (int) ev.getNewValue();
            if (id == -1) {
                openDialog("dlgQualification");
            }
        }
    }
    
    public void createQualification() {
        if (newQualif != null) {
            YvsGrhQualifications q = new YvsGrhQualifications();
            q.setDesignation(newQualif.getIntitule());
            q.setCodeInterne(newQualif.getCodeInterne());
            q.setDomaine(new YvsGrhDomainesQualifications(newQualif.getDomaine().getId()));
            q.setSupp(false);
            q.setDateSave(new Date());
            q.setDateUpdate(new Date());
            q.setId(null);
            q = (YvsGrhQualifications) dao.save1(q);
            newQualif.setId(q.getId());
            int idx = domaines.indexOf(new YvsGrhDomainesQualifications(newQualif.getDomaine().getId()));
            if (idx >= 0) {
                domaines.get(idx).getQualifications().add(q);
                update("tab-qualif-poste");
            }
//            if (qualifications.contains(new DomainesQualificationPoste(newQualif.getDomaine().getId()))) {
//                qualifications.get(qualifications.indexOf(new DomainesQualificationPoste(newQualif.getDomaine().getId()))).getQualifications().add(0, new QualificationPoste(0, newQualif));
//            } else {
//                YvsGrhDomainesQualifications s = domaines.get(domaines.indexOf(new YvsGrhDomainesQualifications(newQualif.getDomaine().getId())));
//                DomainesQualificationPoste d = new DomainesQualificationPoste(s.getId(), s.getTitreDomaine());
//                d.getQualifications().add(new QualificationPoste(0, newQualif));
//                qualifications.add(q);
//            }
            idQualificationRequise = q.getId();
            update("qualif-poste-t");
            closeDialog("dlgAddQualification");
        }
    }
    
    public void addQualificationRequise(YvsGrhDomainesQualifications dom, YvsGrhQualifications qual) {
        if (poste.getId() > 0) {
            if (qual.getId() > 0) {
                if (!containsQualification(poste.getQualifications(), qual)) {
                    YvsGrhQualificationPoste qp = new YvsGrhQualificationPoste();
                    qp.setPoste(new YvsGrhPosteDeTravail(poste.getId()));
                    qp.setQualification(qual);
                    qp.setAuthor(currentUser);
                    qp.setSupp(false);
                    qp.setDateSave(new Date());
                    qp.setDateUpdate(new Date());
                    try {
                        qp.setId(null);
                        qp = (YvsGrhQualificationPoste) dao.save1(qp);
                        poste.getQualifications().add(qp);
                    } catch (Exception ex) {
                        getMessage("Non efféctué !", FacesMessage.SEVERITY_ERROR);
                    }
                } else {
                    getErrorMessage("Cette qualification est déjà présente dans la liste !");
                    return;
                }
            }
        } else {
            getErrorMessage("Aucun poste de travail n'a été selectionné !");
        }
    }
    
    private boolean containsQualification(List<YvsGrhQualificationPoste> l, YvsGrhQualifications q) {
        for (YvsGrhQualificationPoste qp : l) {
            if (qp.getQualification().equals(q)) {
                return true;
            }
        }
        return false;
    }
    
    public void removeQualificationPoste(YvsGrhQualificationPoste qual) {
        try {
            qual.setAuthor(currentUser);
            dao.delete(qual);
            poste.getQualifications().remove(qual);
        } catch (Exception ex) {
            Logger.getLogger(ManagedPosteDeTravail.class.getName()).log(Level.SEVERE, null, ex);
            getMessage("Non efféctué !", FacesMessage.SEVERITY_ERROR);
        }
    }
    private List<Qualification> selectedQualif;
    
    public List<Qualification> getSelectedQualif() {
        return selectedQualif;
    }
    
    public void setSelectedQualif(List<Qualification> selectedQualif) {
        this.selectedQualif = selectedQualif;
    }
    
    public void choixQualifPoste() {
        displayDelQualif = !selectedQualif.isEmpty();
        update("tabview-poste0:tab-qp");
    }
    
    public void deleteQualifPoste() {
        try {
            for (Qualification q : selectedQualif) {
//                dao.delete(new YvsGrhQualificationPoste(new YvsQualificationPostePK((int) poste.getId(), (int) q.getId())));
            }
            selectedQualif.clear();
            displayDelQualif = false;
        } catch (Exception ex) {
            getMessage("Non effectué !", FacesMessage.SEVERITY_ERROR);
        }
        
    }

    /**
     * gerer les expériences au poste au poste*
     */
    private String posteDetravail;
    private int duree;
    private List<ExperienceRequise> experiences; //ici on charge uniquement les poste d'un service

    public String getPosteDetravail() {
        return posteDetravail;
    }
    
    public void setPosteDetravail(String posteDetravail) {
        this.posteDetravail = posteDetravail;
    }
    
    public List<ExperienceRequise> getExperiences() {
        return experiences;
    }
    
    public void setExperiences(List<ExperienceRequise> experiences) {
        this.experiences = experiences;
    }
    
    public int getDuree() {
        return duree;
    }
    
    public void setDuree(int duree) {
        this.duree = duree;
    }
//ajouter l'expérience requise ) un poste

    public void addExperiencePoste() {
//        String champ[] = new String[]{"agence"};
//        Object val[] = new Object[]{currentAgence};
        if (poste.getId() != 0 && posteDetravail != null) {
            YvsGrhExperiencePoste ex = new YvsGrhExperiencePoste();
            ex.setDuree(duree);
            ex.setSupp(false);
            ex.setPoste(posteDetravail);
            ex.setPosteTravail(new YvsGrhPosteDeTravail(poste.getId()));
            ex.setId(null);
            ex = (YvsGrhExperiencePoste) dao.save1(ex);
            experiences.add(new ExperienceRequise(ex.getId(), new PosteDeTravail(0, posteDetravail), ex.getDuree()));
        } else {
            getMessage("Vous devez saisir ou choisir un poste", FacesMessage.SEVERITY_ERROR);
        }
    }
    private List<ExperienceRequise> selectedExperience;
    
    public List<ExperienceRequise> getSelectedExperience() {
        return selectedExperience;
    }
    
    public void setSelectedExperience(List<ExperienceRequise> selectedExperience) {
        this.selectedExperience = selectedExperience;
    }
    
    public void choixExpfPoste() {
        displayExperience = !selectedExperience.isEmpty();
        update("tabview-poste0:tab-profil-poste");
    }
    
    public void deleteExpPoste() {
        try {
            for (ExperienceRequise ex : selectedExperience) {
                dao.delete(new YvsGrhExperiencePoste(ex.getId()));
                experiences.remove(ex);
            }
            selectedExperience.clear();
            displayExperience = false;
        } catch (Exception e) {
            getMessage("Non effectué !", FacesMessage.SEVERITY_ERROR);
        }
        
    }

//    public void loadQualification() {
//        champ = new String[]{"societe"};
//        val = new Object[]{currentAgence.getSociete()};
//        qualifications = dao.loadNameQueries("YvsQualifications.findAll", champ, val);
////        buildDataQualification(lq);
//    }
//    private void buildDataQualification(List<YvsGrhQualifications> lq) {
//        qualifications.clear();
//        DomainesQualificationPoste dom = new DomainesQualificationPoste();
//        int i = -1;
//        for (YvsGrhQualifications q : lq) {
//            QualificationPoste qa = new QualificationPoste(i, UtilGrh.buildBeanQualification(q));
//            qa.setRattache(false);
////            for (QualificationPoste qp : poste.getQualifications()) {
////                if (qp.getQualification().equals(qa.getQualification())) {
////                    qa.setRattache(true);
////                    break;
////                }
////            }
//            if (dom.getId() != q.getDomaine().getId()) {
//                dom = new DomainesQualificationPoste();
//                dom.setId(q.getDomaine().getId());
//                dom.setTitreDomaine(q.getDomaine().getTitreDomaine());
//                dom.setQualifications(new ArrayList<QualificationPoste>());
//                dom.getQualifications().add(qa);
//                qualifications.add(dom);
//            } else {
//                qualifications.get(qualifications.indexOf(dom)).getQualifications().add(qa);
//            }
//            i--;
//        }
//    }
    /**
     * Gestion des diplome(Ajout d'un diplome)
     */
    private Diplomes diplome = new Diplomes();
    private SpecialiteDiplomes specialite = new SpecialiteDiplomes();
    private int niveauOrdre = 1;
    
    public Diplomes getDiplome() {
        return diplome;
    }
    
    public void setDiplome(Diplomes diplome) {
        this.diplome = diplome;
    }
    
    public int getNiveauOrdre() {
        return niveauOrdre;
    }
    
    public void setNiveauOrdre(int niveauOrdre) {
        this.niveauOrdre = niveauOrdre;
    }
    
    public SpecialiteDiplomes getSpecialite() {
        return specialite;
    }
    
    public void setSpecialite(SpecialiteDiplomes specialite) {
        this.specialite = specialite;
    }
    
    public void createNewDiplome() {
        if (diplome.getDesignation() != null && diplome.getSpecialite().getId() > 0) {
            YvsDiplomes d = UtilGrh.buildDiplome(diplome);
            d.setAuthor(currentUser);
            d.setActif(true);
            d.setSupp(false);
            d.setSociete(currentAgence.getSociete());
            d.setId(null);
            d = (YvsDiplomes) dao.save1(d);
            if (d.getSpecialite() != null) {
                specialites.get(specialites.indexOf(new YvsSpecialiteDiplomes(d.getSpecialite().getId()))).getDiplomes().add(0, d);
            }
//            newPoste.setNiveauRequis(diplome);
            diplome = new Diplomes();
            succes();
        } else {
            if (diplome.getSpecialite().getId() <= 0) {
                getErrorMessage("Veuillez choisir la spécialité !");
            } else {
                getErrorMessage("Vous devez indiquer l'intitulé du diplôme");
            }
        }
    }
//

    public void choixDiplomes(YvsDiplomes d) {
        poste.setNiveauRequis(UtilGrh.buildDiplome(d));
    }
    
    public void createNewSpecialite() {
        if (specialite.getCodeInterne() != null && specialite.getDesignation() != null) {
            YvsSpecialiteDiplomes sp = new YvsSpecialiteDiplomes();
            sp.setAuthor(currentUser);
            sp.setCodeInterne(specialite.getCodeInterne());
            sp.setTitreSpecialite(specialite.getDesignation());
            sp.setId(null);
            sp = (YvsSpecialiteDiplomes) dao.save1(sp);
            specialite.setId(sp.getId());
            specialites.add(0, sp);
            diplome.setSpecialite(specialite);
            specialite = new SpecialiteDiplomes();
        }
    }
    
    public void removeDiplome(YvsDiplomes d) {
        try {
            d.setAuthor(currentUser);
            dao.delete(d);
            d.getSpecialite().getDiplomes().remove(d);
            diplome = new Diplomes();
            update("form_NewposteDip");
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer !");
            log.log(Level.SEVERE, null, ex);
        }
    }
    
    public void searchPoste() {
        ParametreRequete p = new ParametreRequete("y.intitule", "intitule", null, "LIKE", "AND");
        if (codeSearch != null ? codeSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.intitule)", "intitule", codeSearch.toUpperCase() + "%", "LIKE", "AND");
        }
        paginator.addParam(p);
        loadPosteDetravail(true, true);
    }
    
    private int depSearch;
    
    public int getDepSearch() {
        return depSearch;
    }
    
    public void setDepSearch(int depSearch) {
        this.depSearch = depSearch;
    }
    
    public void addParamDepartement() {
        ParametreRequete p = new ParametreRequete("y.departement", "departement", null, "=", "AND");
        if (depSearch > 0) {
            p = new ParametreRequete("y.departement", "departement", new YvsGrhDepartement(depSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadPosteDetravail(true, true);
    }

    /**
     * *Ajouter un nouveau département
     */
    private Departements newDep = new Departements();
    
    public Departements getNewDep() {
        return newDep;
    }
    
    public void setNewDep(Departements newDep) {
        this.newDep = newDep;
    }
    
    public void createNewDep() {
        if (newDep.getCodeDepartement() != null) {
            YvsGrhDepartement d = new YvsGrhDepartement();
            d.setCodeDepartement(newDep.getCodeDepartement());
            d.setIntitule(newDep.getIntitule());
            if (newDep.getIdParent() != 0) {
                d.setDepartementParent(new YvsGrhDepartement(newDep.getIdParent()));
            }
            d.setSociete(currentAgence.getSociete());
            d.setActif(true);
            d.setId(null);
            d = (YvsGrhDepartement) dao.save1(d);
            poste.getDepartement().setId(d.getId());
            update("depart-poste");
            closeDialog("dlgAddDepartement");
            closeDialog("dlgDepartement");
            newDep = new Departements();
        } else {
            getErrorMessage("Le champ code département est vide");
        }
    }

    /**/
    //Grille des frais de mission par poste
    /**/
    private GrilleFraisMission grilleFrais = new GrilleFraisMission();
    private DetailFraisMission detailFrais = new DetailFraisMission();
    private List<DetailFraisMission> selectTionDetailFraisMission = new ArrayList<>();
    private String newLibelleCout;
    private List<YvsGrhTypeCout> listLibelle = new ArrayList<>();
    private List<YvsGrhGrilleMission> listeGrilleFrais;
    private boolean displayDelGrille, displayDelDetailG;
    private boolean displayListFrais, updateFraisM;
    
    public List<DetailFraisMission> getSelectTionDetailFraisMission() {
        return selectTionDetailFraisMission;
    }
    
    public void setSelectTionDetailFraisMission(List<DetailFraisMission> selectTionDetailFraisMission) {
        this.selectTionDetailFraisMission = selectTionDetailFraisMission;
    }
    
    public DetailFraisMission getDetailFrais() {
        return detailFrais;
    }
    
    public boolean isDisplayListFrais() {
        return displayListFrais;
    }
    
    public void setDisplayListFrais(boolean displayListFrais) {
        this.displayListFrais = displayListFrais;
    }
    
    public boolean isDisplayDelDetailG() {
        return displayDelDetailG;
    }
    
    public void setDisplayDelDetailG(boolean displayDelDetailG) {
        this.displayDelDetailG = displayDelDetailG;
    }
    
    public void setDetailFrais(DetailFraisMission detailFrais) {
        this.detailFrais = detailFrais;
    }
    
    public GrilleFraisMission getGrilleFrais() {
        return grilleFrais;
    }
    
    public void setGrilleFrais(GrilleFraisMission grilleFrais) {
        this.grilleFrais = grilleFrais;
    }
    
    public String getNewLibelleCout() {
        return newLibelleCout;
    }
    
    public void setNewLibelleCout(String newLibelleCout) {
        this.newLibelleCout = newLibelleCout;
    }
    
    public List<YvsGrhTypeCout> getListLibelle() {
        return listLibelle;
    }
    
    public void setListLibelle(List<YvsGrhTypeCout> listLibelle) {
        this.listLibelle = listLibelle;
    }
    
    public boolean isDisplayDelGrille() {
        return displayDelGrille;
    }
    
    public void setDisplayDelGrille(boolean displayDelGrille) {
        this.displayDelGrille = displayDelGrille;
    }

//    public void toogleDlgFraisMission() {
//        displayListFrais = !displayListFrais;
//        grilleFrais = new GrilleFraisMission();
//        updateFraisM = false;
//        update("view-poste-frais-mission");
//    }
//    public void choixFraisMission() {
//        {
//            if (poste.getFraisMission().getId() > 0) {
//                long id = (long) ev.getNewValue();
//                if (id == -1) {
//                    openDialog("dlgAddGrille");
//
//                } else if (id != 0) {
//                    poste.setFraisMission(listeGrilleFrais.get(listeGrilleFrais.indexOf(new GrilleFraisMission(id))));
//                }
//            }
//        }
//
//    }
//    public void choixLibelle(ValueChangeEvent ev) {
//        if (ev != null && ev.getNewValue() != null) {
//            long id = (long) ev.getNewValue();
//            if (id == -1) {
//                //ouvre la boite de dialogue d'ajout d'un nouveau libellé
//                openDialog("dlgNewlibelle");
//            } else if (id > 0) {
//                detailFrais.setTypeCout(listLibelle.get(listLibelle.indexOf(new TypeCout(id))));
//            }
//        }
//    }
    public void loadLibelleCout() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listLibelle = dao.loadNameQueries("YvsGrhTypeCout.findAll", champ, val);
    }
    
    public void saveNewLibelle() {
        if (newLibelleCout != null) {
            YvsGrhTypeCout tc = new YvsGrhTypeCout();
            tc.setLibelle(newLibelleCout);
            tc.setSociete(currentAgence.getSociete());
            tc.setAuthor(currentUser);
            tc.setId(null);
            tc = (YvsGrhTypeCout) dao.save1(tc);
            TypeCout t = new TypeCout(tc.getId(), tc.getLibelle());
            listLibelle.add(0, tc);
            detailFrais.setTypeCout(t);
            update("post-add-grille-grid");
            closeDialog("dlgNewlibelle");
        }
    }
    
    public void deleteLibelleFrais(YvsGrhTypeCout tc) {
        try {
            tc.setAuthor(currentUser);
            dao.delete(tc);
            listLibelle.remove(tc);
            update("table_libC");
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer !");
            log.log(Level.SEVERE, null, ex);
        }
    }
    
    public void selectGrille(SelectEvent ev) {
        grilleFrais = (GrilleFraisMission) ev.getObject();
        updateFraisM = true;
        update("view-poste-frais-mission");
    }
    
    public void selectDetailGrille(SelectEvent ev) {
        detailFrais = (DetailFraisMission) ev.getObject();
        displayDelDetailG = !selectTionDetailFraisMission.isEmpty();
        update("post-add-grille-grid");
    }
    
    public void unSelectDetailGrille(UnselectEvent ev) {
        detailFrais = new DetailFraisMission();
        displayDelDetailG = !selectTionDetailFraisMission.isEmpty();
        update("post-add-grille-grid");
    }
    
    public void resetFormFraisM() {
        grilleFrais = new GrilleFraisMission();
        detailFrais = new DetailFraisMission();
        updateFraisM = false;
        updateMiss = false;
    }

    //sauvegarde une grille pour plusieur poste
    public void saveGrille() {
        if (grilleFrais.getCategorie() != null && !grilleFrais.getDetailsFrais().isEmpty()) {
            if (grilleFrais.getCategorie().trim().length() != 0) {
                //sauvegarde la grille
                YvsGrhGrilleMission g = new YvsGrhGrilleMission();
                g.setCategorie(grilleFrais.getCategorie());
                g.setActif(true);
                g.setSociete(currentUser.getAgence().getSociete());
                g.setAuthor(currentUser);
                g.setDateUpdate(new Date());
                if (!updateFraisM) {
                    g.setDateSave(new Date());
                    g.setId(null);
                    g = (YvsGrhGrilleMission) dao.save1(g);
                    listeGrilleFrais.add(g);
                } else {
                    g.setId(grilleFrais.getId());
                    dao.update(g);
                    listeGrilleFrais.set(listeGrilleFrais.indexOf(g), g);
                }
                grilleFrais.setId(g.getId());
                poste.setFraisMission(grilleFrais);
                for (YvsGrhDetailGrilleFraiMission d : grilleFrais.getDetailsFrais()) {
                    YvsGrhDetailGrilleFraiMission yd = new YvsGrhDetailGrilleFraiMission();
                    yd.setTypeCout(new YvsGrhTypeCout(d.getTypeCout().getId()));
                    yd.setGrilleMission(new YvsGrhGrilleMission(grilleFrais.getId()));
                    yd.setMontantPrevu(d.getMontantPrevu());
                    yd.setAuthor(currentUser);
                    dao.update(yd);
                }
                
                succes();
                updateFraisM = true;
                update("tab-grille-poste");
                update("post-add-grille-grid");
                update("select-grille-mission-poste");
            } else {
                getErrorMessage("Formulaire incomplet");
            }
        } else {
            getErrorMessage("Formulaire incomplet");
        }
    }
    
    public void openFormGrille() {
        displayListFrais = true;
        updateFraisM = true;
    }
    
    public void saveGrilleAndClose() {
        saveGrille();
    }
    
    public void addDetailGrille() {
        if (!containsTypeFrais(grilleFrais.getDetailsFrais(), detailFrais.getTypeCout())) {
            if (detailFrais.getMontant() > 0) {
                YvsGrhTypeCout tc = listLibelle.get(listLibelle.indexOf(new YvsGrhTypeCout(detailFrais.getTypeCout().getId())));
                detailFrais.setTypeCout(new TypeCout(tc.getId(), tc.getLibelle()));
                YvsGrhDetailGrilleFraiMission dgrf = UtilGrh.buildEntityDetailFrais(detailFrais);
                dgrf.setTypeCout(tc);
                dgrf.setId(-tc.getId());
                grilleFrais.getDetailsFrais().add(0, dgrf);
                detailFrais = new DetailFraisMission();
            } else {
                getErrorMessage("formulaire incorrecte");
            }
        } else {
            YvsGrhTypeCout tc = listLibelle.get(listLibelle.indexOf(new YvsGrhTypeCout(detailFrais.getTypeCout().getId())));
            YvsGrhDetailGrilleFraiMission dgrf = UtilGrh.buildEntityDetailFrais(detailFrais);
            dgrf.setTypeCout(tc);
            grilleFrais.getDetailsFrais().set(grilleFrais.getDetailsFrais().indexOf(dgrf), dgrf);
            detailFrais = new DetailFraisMission();
        }
        update("tab-grille-poste");
        update("post-add-grille-grid");
    }
    
    private boolean containsTypeFrais(List<YvsGrhDetailGrilleFraiMission> l, TypeCout t) {
        for (YvsGrhDetailGrilleFraiMission d : l) {
            if (d.getTypeCout().equals(new YvsGrhTypeCout(t.getId()))) {
                return true;
            }
        }
        return false;
    }
    
    public void deleteDetailGrille() {
        for (DetailFraisMission d : selectTionDetailFraisMission) {
            if (d.isSave()) {
//                dao.delete(new YvsGrhDetailBulletin(new YvsGrhMontanSalairePK(d.getTypeCout().getId(), grilleFrais.getId())));
//                grilleFrais.getDetailsFrais().remove(d);

            } else {
                grilleFrais.getDetailsFrais().remove(d);
            }
        }
        update("tab-grille-poste");
    }
    
    public void deleteGrille() {
        if (grilleFrais.getId() != 0) {
            try {
                dao.delete(new YvsGrhGrilleMission(grilleFrais.getId()));
                listeGrilleFrais.remove(grilleFrais);
                succes();
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer ces éléments");
            }
            grilleFrais = new GrilleFraisMission();
            update("view-poste-frais-mission");
        }
    }
    
    public void resetViewGrille() {
        grilleFrais = new GrilleFraisMission();
        updateFraisM = false;
        update("view-poste-frais-mission");
    }

    /**
     * *************Lier les missions et les activités ************
     */
    private MissionPoste mission = new MissionPoste();
    private MissionPoste activite = new MissionPoste();
    private List<MissionPoste> selectionMissions;
    private List<MissionPoste> selectionActivites;
    private boolean displayBtnDelMission, displayBtnDelActivite, updateMiss, updateActivite;
    
    public MissionPoste getMission() {
        return mission;
    }
    
    public void setMission(MissionPoste mission) {
        this.mission = mission;
    }
    
    public MissionPoste getActivite() {
        return activite;
    }
    
    public void setActivite(MissionPoste activite) {
        this.activite = activite;
    }
    
    public List<MissionPoste> getSelectionActivites() {
        return selectionActivites;
    }
    
    public void setSelectionActivites(List<MissionPoste> selectionActivites) {
        this.selectionActivites = selectionActivites;
    }
    
    public List<MissionPoste> getSelectionMissions() {
        return selectionMissions;
    }
    
    public void setSelectionMissions(List<MissionPoste> selectionMissions) {
        this.selectionMissions = selectionMissions;
    }
    
    public boolean isDisplayBtnDelMission() {
        return displayBtnDelMission;
    }
    
    public void setDisplayBtnDelMission(boolean displayBtnDelMission) {
        this.displayBtnDelMission = displayBtnDelMission;
    }
    
    public boolean isDisplayBtnDelActivite() {
        return displayBtnDelActivite;
    }
    
    public void setDisplayBtnDelActivite(boolean displayBtnDelActivite) {
        this.displayBtnDelActivite = displayBtnDelActivite;
    }
    
    public void addMission() {
        if (poste.getId() != 0) {
            if ((mission.getLibelle() == null) ? true : !mission.getLibelle().trim().equals("")) {
                YvsGrhMissionPoste m = new YvsGrhMissionPoste();
                m.setLibelle(mission.getLibelle());
                m.setPoste(new YvsGrhPosteDeTravail(poste.getId()));
                if (updateMiss) {
                    m.setId(mission.getId());
                    dao.update(m);
                    poste.getMissions().set(poste.getMissions().indexOf(mission), mission);
                } else {
                    m.setId(null);
                    m = (YvsGrhMissionPoste) dao.save1(m);
                    mission.setId(m.getId());
                    poste.getMissions().add(mission);
                }
                selectionMissions = null;
                displayBtnDelMission = false;
                mission = new MissionPoste();
                updateMiss = false;
            } else {
                getErrorMessage("Le champ mission est vide");
            }
        } else {
            getErrorMessage("Formilaire incomplet, vous devez spécifier le poste de travail");
        }
    }
    
    public void addActivites() {
        if (poste.getId() != 0) {
            if ((activite.getLibelle() == null) ? true : !activite.getLibelle().trim().equals("")) {
                YvsGrhActivitesPoste a = new YvsGrhActivitesPoste(activite.getId());
                a.setLibelle(activite.getLibelle());
                a.setPoste(new YvsGrhPosteDeTravail(poste.getId()));
                a.setAuthor(currentUser);
                if (updateActivite) {
                    a.setId(activite.getId());
                    dao.update(a);
                    poste.getActivites().set(poste.getActivites().indexOf(activite), activite);
                } else {
                    a.setId(null);
                    a = (YvsGrhActivitesPoste) dao.save1(a);
                    activite.setId(a.getId());
                    poste.getActivites().add(activite);
                }
                updateActivite = false;
                activite = new MissionPoste();
                selectionActivites = null;
                displayBtnDelActivite = false;
            } else {
                getErrorMessage("Le champ activite est vide");
            }
        } else {
            getErrorMessage("Formilaire incomplet, vous devez spécifier le poste de travail");
        }
    }
    
    public void deleteMission() {
        try {
            for (MissionPoste m : selectionMissions) {
                if (m.getId() != 0) {
                    dao.delete(new YvsGrhMissionPoste(m.getId()));
                    poste.getMissions().remove(m);
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer");
        }
        update("tabview-poste0:S-tabview-poste0:tab-poste-mission");
    }
    
    public void deleteActivite(MissionPoste m) {
        try {
            
            dao.delete(new YvsGrhActivitesPoste(m.getId(), currentUser));
            poste.getActivites().remove(m);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer");
        }
        
        update(
                "tabview-poste0:S-tabview-poste0:tab-poste-activite");
    }
    
    public void selectMission(SelectEvent ev) {
        MissionPoste m = (MissionPoste) ev.getObject();
        choixMission(m, true);
        updateMiss = true;
    }
    
    public void unSelectMission(UnselectEvent ev) {
        MissionPoste m = (MissionPoste) ev.getObject();
        choixMission(m, true);
    }

//    public void selectActivite(SelectEvent ev) {
//        MissionPoste m = (MissionPoste) ev.getObject();
//        choixMission(m, false);
//        updateActivite = true;
//    }
//
//    public void unSelectActivite(UnselectEvent ev) {
//        MissionPoste m = (MissionPoste) ev.getObject();
//        choixMission(m, false);
//    }
    public void choixMission(MissionPoste m, boolean isMiss) {
        if (isMiss) {
            displayBtnDelMission = !selectionMissions.isEmpty();
            mission = (selectionMissions.isEmpty()) ? new MissionPoste() : selectionMissions.get(selectionMissions.size() - 1);
            update("tabview-poste0:S-tabview-poste0:grid-post-mission");
        } else {
            displayBtnDelActivite = !selectionMissions.isEmpty();
            activite = (selectionActivites.isEmpty()) ? new MissionPoste() : selectionActivites.get(selectionActivites.size() - 1);
            update("tabview-poste0:S-tabview-poste0:grid-post-activite");
        }
    }
    
    private boolean displayFichePoste;
    
    public boolean isDisplayFichePoste() {
        return displayFichePoste;
    }
    
    public void setDisplayFichePoste(boolean displayFichePoste) {
        this.displayFichePoste = displayFichePoste;
    }
    
    public void openFichePoste() {
        displayFichePoste = !displayFichePoste;
        if (listSelection != null) {
            if (!listSelection.isEmpty()) {
                populateView(UtilGrh.buildBeanPoste(listSelection.get(listSelection.size() - 1)));
            }
        }
        update("top-poste-de-travail");
    }
    
    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void choosePosteEquivalent(SelectEvent ev) {
        if (ev != null) {
            try {
                PosteDeTravail bean = (PosteDeTravail) ev.getObject();
                String rq = "UPDATE yvs_poste_de_travail SET poste_equivalent='" + bean.getId() + "' WHERE id=?";
                Options[] param = new Options[]{new Options(listSelection.get(0).getId(), 1)};
                dao.requeteLibre(rq, param);
                
            } catch (Exception ex) {
                Logger.getLogger(ManagedPosteDeTravail.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//    public void saveOtherPoste() {
//        if (controleFiche(newPoste)) {
//            YvsGrhPosteDeTravail post = buildPoste(newPoste);
//            post = (YvsGrhPosteDeTravail) dao.save1(post);
//            newPoste.setId(post.getId());
//            succes();
////            newPoste.setDepartement(UtilGrh.buildBeanDepartement(departements.get(departements.indexOf(newPoste.getDepartement()))));
//            if (newPoste.getFraisMission().getId() > 0) {
//                newPoste.setFraisMission(listeGrilleFrais.get(listeGrilleFrais.indexOf(newPoste.getFraisMission())));
//            }
//            if (newPoste.getNiveauRequis().getId() > 0) {
//                newPoste.setNiveauRequis(UtilGrh.buildDiplome(diplomes.get(diplomes.indexOf(new YvsDiplomes(newPoste.getNiveauRequis().getId())))));
//            }
//            listPostes.add(post);
//            posteDetravail = newPoste.getIntitule();
//            newPoste = new PosteDeTravail();
////            loadPosteDetravail();
//            update("txt_poste_experience");
//            update("poste-poste");
////            update("form-poste-000");
//        }
//    }
    public void addNewDomaine() {
        if ((domaine.getTitreDomaine() != null) ? !domaine.getTitreDomaine().trim().equals("") : false) {
            YvsGrhDomainesQualifications q = new YvsGrhDomainesQualifications();
            q.setTitreDomaine(domaine.getTitreDomaine());
            q.setAuthor(currentUser);
            q.setSociete(currentAgence.getSociete());
            q.setId(null);
            q = (YvsGrhDomainesQualifications) dao.save1(q);
            domaine.setId(q.getId());
            domaines.add(0, q);
//            qualifications.add(new DomainesQualificationPoste(q.getId(), q.getTitreDomaine()));
            domaine = new DomainesQualifications();
        }
    }
    
    public void RemoveOneDomaine() {
        if (newQualif.getDomaine().getId() > 0) {
            try {
                YvsGrhDomainesQualifications q = new YvsGrhDomainesQualifications(newQualif.getDomaine().getId());
                q.setAuthor(currentUser);
                dao.delete(q);
                domaines.remove(q);
                update("txt_domaine_qualifC");
                
            } catch (Exception ex) {
                Logger.getLogger(ManagedPosteDeTravail.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*Gerer les éléments de rémunération*/
    private ModePaiement modePaie = new ModePaiement();
    
    public ModePaiement getModePaie() {
        return modePaie;
    }
    
    public void setModePaie(ModePaiement modePaie) {
        this.modePaie = modePaie;
    }

//    public void loadRegleTache() {
//        champ = new String[]{"societe"};
//        val = new Object[]{currentUser.getAgence().getSociete()};
//        List<YvsGrhRegleTache> l = dao.loadNameQueries("YvsRegleTache.findAll", champ, val);
//        reglesDeTaches = UtilGrh.buildSimpleBeanListRegleDeTache(l);
//    }
    public void loadOnViewModePaiement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            ModePaiement mode = (ModePaiement) ev.getObject();
            poste.setModePaiement(mode);
        }
    }

    /*Gérer les primes par poste de travail*/
    private List<TypeElementAdd> listTypePrime = new ArrayList<>(), listTypePrimeToImport = new ArrayList<>();
    private List<ElementAdditionnelPoste> modelePrimes;
    private List<Societe> listSociete;
    private ElementAdditionnelPoste prime = new ElementAdditionnelPoste();
    boolean updatePrime, updateTypePrime;
    private TypeElementAdd newType = new TypeElementAdd();
    private List<TypeElementAdd> selectionTypePrime;
    
    public List<TypeElementAdd> getSelectionTypePrime() {
        return selectionTypePrime;
    }
    
    public void setSelectionTypePrime(List<TypeElementAdd> selectionTypePrime) {
        this.selectionTypePrime = selectionTypePrime;
    }
    
    public TypeElementAdd getNewType() {
        return newType;
    }
    
    public void setNewType(TypeElementAdd newType) {
        this.newType = newType;
    }
    
    public ElementAdditionnelPoste getPrime() {
        return prime;
    }
    
    public void setPrime(ElementAdditionnelPoste prime) {
        this.prime = prime;
    }
    
    public List<TypeElementAdd> getListTypePrime() {
        return listTypePrime;
    }
    
    public void setListTypePrime(List<TypeElementAdd> listTypePrime) {
        this.listTypePrime = listTypePrime;
    }
    
    public List<TypeElementAdd> getListTypePrimeToImport() {
        return listTypePrimeToImport;
    }
    
    public void setListTypePrimeToImport(List<TypeElementAdd> listTypePrimeToImport) {
        this.listTypePrimeToImport = listTypePrimeToImport;
    }
    
    public List<ElementAdditionnelPoste> getModelePrimes() {
        return modelePrimes;
    }
    
    public void setModelePrimes(List<ElementAdditionnelPoste> modelePrimes) {
        this.modelePrimes = modelePrimes;
    }
    
    public List<Societe> getListSociete() {
        return listSociete;
    }
    
    public void setListSociete(List<Societe> listSociete) {
        this.listSociete = listSociete;
    }
    
    private void loadAllSociete() {
        //charge les sociétés
        champ = new String[]{};
        val = new Object[]{};
        listSociete = UtilSte.buildBeanListeSociete(dao.loadNameQueries("YvsSocietes.findAll", champ, val));
    }
    
    private void loadModelePrime() {
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        modelePrimes = UtilGrh.buildEltAddPoste(dao.loadNameQueries("YvsGrhModelElementAdditionel.findAll", champ, val));
    }
    
    public void loadTypePrime() {
        champ = new String[]{"societe", "retenue"};
        val = new Object[]{currentUser.getAgence().getSociete(), false};
        listTypePrime = UtilGrh.buildBeanListTypePrime(dao.loadNameQueries("YvsGrhTypeElementAdditionel.findAll", champ, val));
    }
    
    public void addPrime() {
        if (poste.getId() > 0 && prime.getTypeElt() != null) {
            if (prime.isPermanent()) {
                prime.setDebut(null);
                prime.setFin(null);
            }
            if (!prime.isPermanent() && (prime.getDebut() == null || prime.getFin() == null)) {
                getErrorMessage("Formulaire incorrecte ! les dates d'application des retenues sont erronnées");
                return;
            } else if ((!prime.isPermanent() && prime.getDebut() != null && prime.getFin() != null) ? (prime.getDebut().after(prime.getFin())) : false) {
//                getErrorMessage("Formulaire incorrecte ! ", "les dates d'application des retenues sont erronnées");
                System.err.println(" " + prime.getDebut() + " ");
                getErrorMessage("erreur ", "détails de l'erreur");
                return;
            }
            //s'assurer que la prime n'a pas déjà été attribuer
            if (!updatePrime) {
                for (ElementAdditionnelPoste ea : poste.getPrimes()) {
                    if (Objects.equals(ea.getTypeElt().getId(), prime.getTypeElt().getId())) {
                        getErrorMessage("Cette prime a déjà été attribuer");
                        return;
                    }
                }
            }
            YvsGrhModelPrimePoste el = new YvsGrhModelPrimePoste();
            el.setTypeElement(new YvsGrhTypeElementAdditionel(prime.getTypeElt().getId()));
            el.setMontantElement(prime.getMontant());
            el.setPermanent(prime.isPermanent());
            el.setPoste(new YvsGrhPosteDeTravail(poste.getId()));
            el.setAuthor(currentUser);
            el.setDateSave(prime.getDateSave());
            el.setDateUpdate(new Date());
            if (!prime.isPermanent()) {
                el.setDateDebut(prime.getDebut());
                el.setDateFin(prime.getFin());
            } else {
                prime.setDebut(null);
                prime.setFin(null);
            }
            if (!updatePrime) {
                el.setId(null);
                el = (YvsGrhModelPrimePoste) dao.save1(el);
                prime.setId(el.getId());
            } else {
                el.setId(prime.getId());
                dao.update(el);
            }
            prime.setTypeElt(listTypePrime.get(listTypePrime.indexOf(prime.getTypeElt())));
            ElementAdditionnelPoste e = new ElementAdditionnelPoste();
            if (!poste.getPrimes().contains(prime)) {
                cloneObject(e, prime);
                poste.getPrimes().add(e);
            } else {
                cloneObject(e, prime);
                poste.getPrimes().set(poste.getPrimes().indexOf(prime), e);
            }
            newType = new TypeElementAdd();
            prime = new ElementAdditionnelPoste();
            
        } else {
            if (poste.getId() <= 0) {
                getErrorMessage("Aucun poste de travail n'a été selectionné !");
            } else {
                getErrorMessage("Vous devez selectionner une prime !");
            }
        }
    }
    
    public void importPrimeFromModele() {
        if (poste.getId() > 0) {
            ElementAdditionnelPoste ee;
            for (ElementAdditionnelPoste e : modelePrimes) {
                YvsGrhModelPrimePoste el = new YvsGrhModelPrimePoste();
                el.setTypeElement(new YvsGrhTypeElementAdditionel(e.getTypeElt().getId()));
                el.setMontantElement(e.getMontant());
                el.setPermanent(e.isPermanent());
                el.setPoste(new YvsGrhPosteDeTravail(poste.getId()));
                el.setDateDebut(e.getDebut());
                el.setDateFin(e.getFin());
                el.setAuthor(currentUser);
                el.setId(null);
                el = (YvsGrhModelPrimePoste) dao.save1(el);
                ee = new ElementAdditionnelPoste();
                cloneObject(ee, e);
                ee.setId(el.getId());
                poste.getPrimes().add(e);
                
            }
        } else {
            getErrorMessage("Formulaire incorrecte ", "Aucun contrat n'est sélectionné !");
        }
    }
    
    private boolean existInModel(TypeElementAdd t) {
        for (ElementAdditionnelPoste e : modelePrimes) {
            if (e.getTypeElt().equals(t)) {
                return true;
            }
        }
        return false;
    }

    //Ajoute une prime au modèle des primes
    public void createModelePrime() {
        if (!existInModel(prime.getTypeElt())) {
            YvsGrhModelElementAdditionel el = new YvsGrhModelElementAdditionel(prime.getId());
            el.setTypeElement(new YvsGrhTypeElementAdditionel(prime.getTypeElt().getId()));
            el.setDescription(prime.getDescription());
            el.setMontantElement(prime.getMontant());
            el.setPermanent(prime.isPermanent());
            el.setDateElement(new Date());
            el.setAuthor(currentUser);
            if (!prime.isPermanent()) {
                el.setDateDebut(prime.getDebut());
                el.setDateFin(prime.getFin());
            } else {
                prime.setDebut(null);
                prime.setFin(null);
            }
            el.setId(null);
            el = (YvsGrhModelElementAdditionel) dao.save1(el);
            prime.setId(el.getId());
            prime.setTypeElt(listTypePrime.get(listTypePrime.indexOf(prime.getTypeElt())));
            ElementAdditionnelPoste e = new ElementAdditionnelPoste();
            cloneObject(e, prime);
            modelePrimes.add(0, e);
            prime.setId(0);
        } else {
            getErrorMessage("Cette prime existe déjà dans le modèle !");
        }
        addPrime();
    }
    
    public void selectionSocieteToImport(SelectEvent ev) {
        if (ev != null) {
            //charge les prime de la société selectionnée
            champ = new String[]{"societe", "retenue"};
            val = new Object[]{new YvsSocietes(((Societe) ev.getObject()).getId()), false};
            listTypePrimeToImport = UtilGrh.buildBeanListTypePrime(dao.loadNameQueries("YvsGrhTypeElementAdditionel.findAll", champ, val));
            openDialog("dlgEltToImport");
        }
    }
    
    public void addTypePrime() {
        if (newType.getCode() != null) {
            YvsGrhTypeElementAdditionel ty = new YvsGrhTypeElementAdditionel();
            ty.setCodeElement(newType.getCode());
            ty.setLibelle(newType.getLibelle());
            ty.setSociete(currentAgence.getSociete());
            ty.setActif(newType.isActif());
            ty.setRetenue(false);
            ty.setAuthor(currentUser);
            ty.setDateSave(newType.getDateSave());
            ty.setDateUpdate(new Date());
            if (!updateTypePrime) {
                ty.setId(null);
                ty = (YvsGrhTypeElementAdditionel) dao.save1(ty);
                newType.setId(ty.getId());
                listTypePrime.add(0, newType);
            } else {
                ty.setId(newType.getId());
                dao.update(ty);
                listTypePrime.set(listTypePrime.indexOf(newType), cloneTypeP(newType));
            }
            if (newType.isActif()) {
                prime.setTypeElt(newType);
                modelPrime.setTypeElt(newType);
            }
            update("input_selectPoste_type_prime");
            update("input_select_type_prime_model");
        }
        newType = new TypeElementAdd();
    }
    
    private TypeElementAdd cloneTypeP(TypeElementAdd t) {
        TypeElementAdd r = new TypeElementAdd();
        r.setActif(t.isActif());
        r.setCode(t.getCode());
        r.setLibelle(t.getLibelle());
        r.setRetenue(t.isRetenue());
        r.setId(t.getId());
        return r;
    }
    
    public void selectTypePrime(SelectEvent ev) {
//        updateTypePrime = displayBtnUpPrime = selectionTypePrime.size() == 1;        
        prime.setTypeElt((TypeElementAdd) ev.getObject());
    }
    
    public void deleteTypePrime() {
        try {
            for (TypeElementAdd t : selectionTypePrime) {
                dao.delete(new YvsGrhTypeElementAdditionel(t.getId()));
            }
            listTypePrime.removeAll(selectionTypePrime);
            selectionTypePrime.clear();
            succes();
        } catch (Exception ex) {
            
            getErrorMessage("Impossible de supprimer cette prime");
        }
    }
    
    public void desactiverTypePrime() {
        try {
            YvsGrhTypeElementAdditionel t;
            for (TypeElementAdd tc : selectionTypePrime) {
                t = new YvsGrhTypeElementAdditionel(tc.getId());
                t.setCodeElement(tc.getCode());
                t.setLibelle(tc.getLibelle());
                t.setSociete(currentAgence.getSociete());
                t.setAuthor(currentUser);
                t.setRetenue(false);
                t.setActif(false);
                dao.update(t);
            }
            listTypePrime.removeAll(selectionTypePrime);
            selectionTypePrime.clear();
            succes();
        } catch (Exception ex) {
            
            getErrorMessage("Impossible de supprimer cette prime");
        }
    }
    private String searchCompetence;
    
    public String getSearchCompetence() {
        return searchCompetence;
    }
    
    public void setSearchCompetence(String searchCompetence) {
        this.searchCompetence = searchCompetence;
    }

//    public void findCompetence(String str) {
//        champ = new String[]{"societe", "code"};
//        val = new Object[]{currentAgence.getSociete(), "%" + str + "%"};
//        qualifications = dao.loadNameQueries("YvsQualifications.findByCode", champ, val);
////        buildDataQualification(lq);
//    }
    private int currentPoste = 0;
//    private int nombrePoste = 0;

    public void setCurrentPoste(int currentPoste) {
        this.currentPoste = currentPoste;
    }
    
    public int getCurrentPoste() {
        return currentPoste;
    }
    
    public void navigateInView(int pas) {
        if (poste.getId() > 0) {
            currentPoste = listPostes.indexOf(new YvsGrhPosteDeTravail(poste.getId()));
            if (pas > 0) {  //avance         
                currentPoste += pas;
                if (listPostes.size() > currentPoste && currentPoste >= 0) {
                    choixPoste(listPostes.get(currentPoste));
                } else {
                    poste.setId(0);
                }
            } else {//recule
                currentPoste -= 1;
                if (currentPoste >= 0) {
                    choixPoste(listPostes.get(currentPoste));
                } else {
                    poste.setId(0);
                }
            }
        } else {
            currentPoste = 1;
            if (listPostes.size() > currentPoste) {
                choixPoste(listPostes.get(0));
            }
        }
    }
    private TreeNode root;
    
    public TreeNode getRoot() {
        return root;
    }
    
    public void setRoot(TreeNode root) {
        this.root = root;
    }

    /**
     * Afichage arboressant dans postes de travail*
     */
    public void createTree() {
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        List<YvsGrhPosteDeTravail> l = dao.loadNameQueries("YvsPosteDeTravail.findAll", champ, val);
        createTree(buildNode(l));
    }
    
    private void createTree(List<Nodes> l) {
        root = new DefaultTreeNode("--", null);
        for (Nodes n : l) {
            if (n.getIdParent() == 0) {
                root.getChildren().add(n);
            } else {
                //cherche le noeud parent de n
                if (l.contains(new Nodes(n.getIdParent(), n.getIdParent()))) {
                    l.get(l.indexOf(new Nodes(n.getIdParent(), null))).getChildren().add(n);
                }
            }
        }
    }
    
    private List<Nodes> buildNode(List<YvsGrhPosteDeTravail> l) {
        List<Nodes> r = new ArrayList<>();
        for (YvsGrhPosteDeTravail d : l) {
            Nodes n = new Nodes(d.getId(), d);
            if (d.getPosteSuperieur() != null) {
                n.setIdParent(d.getPosteSuperieur().getId());
            } else {
                n.setIdParent(0);
            }
            r.add(n);
        }
        return r;
    }
    
    public void chooseModelContrat() {
        long id = model.getId();
        resetModelContrat();
        if (id > 0) {
            int idx = models.indexOf(new YvsGrhModelContrat(id));
            if (idx > -1) {
                YvsGrhModelContrat y = models.get(idx);
                cloneObject(model, UtilGrh.buildBeanModelContrat(y));
            }
            viewBtnModel = true;
        } else if (id == -1) {
            model.setId(id);
            viewBtnModel = true;
        }
    }
    
    public boolean controleFiche(ModelContrat y) {
        if (y == null) {
            getErrorMessage("Création impossible");
            return false;
        }
        if (y.getIntitule() != null ? y.getIntitule().trim().length() < 1 : true) {
            getErrorMessage("Vous devez preciser le libelle");
            return false;
        }
        if (y.getSalaireBaseHoraire() <= 0 && y.getSalaireBaseMensuel() <= 0) {
            getErrorMessage("Vous devez preciser le salaire de base");
            return false;
        }
        return true;
    }
    
    public void resetModelContrat() {
        model = new ModelContrat();
        viewBtnModel = false;
        resetModelPrime();
        update("tbView_elt_sal_poste:tab_general_poste_00");
    }
    
    public void saveModelContrat() {
        if (controleFiche(model)) {
            YvsGrhPreavis p = UtilGrh.buildPreavis(model.getPreavis(), currentUser);
            if (p.getDuree() > 0) {
                if (p.getUnite() != null ? p.getUnite().getId() < 1 : true) {
                    getErrorMessage("Vous devez selectionner l'unite");
                    return;
                }
                if (p != null ? p.getId() < 1 : true) {
                    p.setId(null);
                    p = (YvsGrhPreavis) dao.save1(p);
                    model.getPreavis().setId(p.getId());
                } else {
                    dao.update(p);
                }
            }
            YvsGrhModelContrat y = UtilGrh.buildModelContrat(model, currentUser, currentAgence.getSociete());
            if (y != null ? y.getId() < 1 : true) {
                y.setId(null);
                y = (YvsGrhModelContrat) dao.save1(y);
                model.setId(y.getId());
            } else {
                dao.update(y);
            }
            int idx = models.indexOf(y);
            if (idx > -1) {
                models.set(idx, y);
            } else {
                models.add(0, y);
            }
            succes();
        }
    }
    
    public void deleteModelContrat() {
        if (model != null ? model.getId() > 0 : false) {
            YvsGrhModelContrat y = new YvsGrhModelContrat(model.getId());
            dao.delete(y);
            models.remove(y);
            resetModelContrat();
            succes();
        }
    }
    
    public void resetModelPrime() {
        modelPrime = new ElementAdditionnelPoste();
        update("form_model_prime_poste");
    }
    
    public boolean controleFiche(ElementAdditionnelPoste y) {
        if (y == null) {
            getErrorMessage("Création impossible");
            return false;
        }
        if (y.getModel() != null ? y.getModel().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner le model");
            return false;
        }
        if (y.getTypeElt() != null ? y.getTypeElt().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner la prime");
            return false;
        }
        int idx = listTypePrime.indexOf(y.getTypeElt());
        if (idx > -1) {
            TypeElementAdd t = listTypePrime.get(idx);
            y.setTypeElt(t);
        }
        return true;
    }
    
    public void getSelectPoste(YvsGrhPosteDeTravail p) {
        selectPoste = p;
    }
    
    public void saveModelPrime() {
        modelPrime.setModel(model);
        if (controleFiche(modelPrime)) {
            YvsGrhModelPrimePoste y = UtilGrh.buildModelPrimePoste(modelPrime, currentUser);
            if (y != null ? y.getId() < 1 : true) {
                y.setId(null);
                y = (YvsGrhModelPrimePoste) dao.save1(y);
            } else {
                dao.update(y);
            }
            int idx = model.getPrimes().indexOf(y);
            if (idx > -1) {
                model.getPrimes().set(idx, y);
            } else {
                model.getPrimes().add(0, y);
            }
            resetModelPrime();
            succes();
        }
    }
    
    public void deleteModelPrime(YvsGrhModelPrimePoste y) {
        if (y != null ? y.getId() > 0 : false) {
            dao.delete(y);
            model.getPrimes().remove(y);
            if (modelPrime.getId() == y.getId()) {
                resetFiche();
            }
            succes();
        }
    }
    
    public void loadOnViewPrimeModel(SelectEvent ev) {
        YvsGrhModelPrimePoste y = (YvsGrhModelPrimePoste) ev.getObject();
        cloneObject(modelPrime, UtilGrh.buildBeanModelPrimePoste(y));
        update("form_model_prime_poste");
    }
    
    public void unLoadOnViewPrimeModel(UnselectEvent ev) {
        resetModelPrime();
    }
    
    public void deleteOnePoste() {
        try {
            if (selectPoste != null ? selectPoste.getId() > 0 : false) {
                List<YvsGrhPosteDeTravail> sup = dao.loadNameQueries("YvsPosteDeTravail.findBySuperieur", new String[]{"superieur"}, new Object[]{selectPoste});
                if (!sup.isEmpty()) {
                    for (YvsGrhPosteDeTravail p : sup) {
                        p.setPosteSuperieur(null);
                        dao.update(p);
                    }
                }
                dao.delete(selectPoste);
                listPostes.remove(selectPoste);
                succes();
                update("poste-poste");
            }
        } catch (Exception e) {
            getErrorMessage("Impossible d'effectuer cette opération");
            Logger.getLogger(ManagedPosteDeTravail.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void uipdatePoste(YvsGrhPosteDeTravail poste) {
        try {
            if (poste != null ? poste.getId() > 0 : false) {
                poste.setActif(!poste.getActif());
                int index = listPostes.indexOf(poste);
                if (index > -1) {
                    dao.update(poste);
                    listPostes.set(index, poste);
                }
                succes();
                update("poste-poste");
            }
        } catch (Exception e) {
            getErrorMessage("Impossible d'effectuer cette opération");
            Logger.getLogger(ManagedPosteDeTravail.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public String getTabIds() {
        return tabIds;
    }
    
    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }
    
}

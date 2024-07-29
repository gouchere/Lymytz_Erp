/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;
import yvs.base.compta.Comptes;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.UtilCompta;
import yvs.entity.grh.contrat.YvsGrhTypeElementAdditionel;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.salaire.YvsGrhCategorieElement;
import yvs.dao.salaire.service.UtilFormules;
import yvs.dao.salaire.service.Lexemes;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.salaire.YvsGrhElementSalaire;
import yvs.entity.grh.salaire.YvsGrhElementStructure;
import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
import yvs.entity.grh.salaire.YvsGrhRubriqueBulletin;
import yvs.entity.grh.salaire.YvsGrhStructureSalaire;
import yvs.entity.param.YvsSocietes;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.OptionEltSalaire;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.Options;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class ManagedRegleSalaire extends Managed<ElementSalaire, YvsGrhElementSalaire> implements Serializable {

    private List<YvsGrhCategorieElement> listCategorie, listRegleCagorie;
    private List<YvsGrhElementSalaire> listeRegle, listRegleImport;
    private YvsGrhElementSalaire selectElement = new YvsGrhElementSalaire();
    @ManagedProperty(value = "#{elementSalaire}")
    private ElementSalaire elements;
    private boolean displayExpR, displayQuantite, displayPourcentage, displayMontant = true;
    private boolean displayOther;

    private String labelMontant = "Montant/Valeur";
    private String labelPourcentage = "Pourcentage basé sur";

    private String labelList = "Liste";
    private String labelButon = "Nouveau";
    /**
     */
    private CategorieRegleSalaire newCategorie = new CategorieRegleSalaire();
    private YvsGrhElementSalaire selectedElement;
    private boolean displayBtnDelCat, updateCategorie;
//    private String labelBtnCat = "Créer";
    private YvsGrhCategorieElement selectedCategorie;

//    private boolean update;
    private boolean displayGrille = true, displayForm;
    private boolean displayButonOption;

    private String textFind, formuleF;

    private TagCloudModel model, modelSigne, modelSigne1, modelSigne2;
    private int sourceRegle;
    private String choixElt;
    private List<Options> listElt, listEltPrime, listConstante, listRetenue;
    private boolean enSaisie;
    private String chaineSelectRegle, tabIds;
    private List<YvsSocietes> listSociete;
    private List<YvsGrhRubriqueBulletin> listRubrique;
    private RubriqueBulletin rubrique = new RubriqueBulletin();
    /*Evaluation d'une règle de salaire*/
    private YvsGrhEmployes employe = new YvsGrhEmployes();
    private Date debut = new Date(), fin = new Date();
    private String resultEvaluation;
    private boolean activeRename;
    private boolean compteTiers;

    private String fusionneTo, fusionneCategorieTo;
    private List<String> fusionnesBy, fusionnesCategorieBy;

    public ManagedRegleSalaire() {
        listRegleCagorie = new ArrayList<>();
        listCategorie = new ArrayList<>();
        listeRegle = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
        fusionnesCategorieBy = new ArrayList<>();
        model = new DefaultTagCloudModel();
        modelSigne = new DefaultTagCloudModel();
        modelSigne1 = new DefaultTagCloudModel();
        modelSigne2 = new DefaultTagCloudModel();
        modelSigne.addTag(new DefaultTagCloudItem("+", 10));
        modelSigne.addTag(new DefaultTagCloudItem("-", 10));
        modelSigne.addTag(new DefaultTagCloudItem("*", 10));
        modelSigne.addTag(new DefaultTagCloudItem("/", 10));
        modelSigne1.addTag(new DefaultTagCloudItem("(", 1));
        modelSigne1.addTag(new DefaultTagCloudItem(")", 1));
        modelSigne1.addTag(new DefaultTagCloudItem("{", 1));
        modelSigne1.addTag(new DefaultTagCloudItem("}", 1));
        modelSigne1.addTag(new DefaultTagCloudItem("&", 1));
        modelSigne1.addTag(new DefaultTagCloudItem("|", 1));
        modelSigne2.addTag(new DefaultTagCloudItem("SI", 1));
        modelSigne2.addTag(new DefaultTagCloudItem("SINON", 1));
        modelSigne2.addTag(new DefaultTagCloudItem("SINONSI", 1));
        listElt = new ArrayList<>();
        listConstante = new ArrayList<>();
        listEltPrime = new ArrayList<>();
        listRetenue = new ArrayList<>();
        listSociete = new ArrayList<>();
        listRubrique = new ArrayList<>();
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getFusionneCategorieTo() {
        return fusionneCategorieTo;
    }

    public void setFusionneCategorieTo(String fusionneCategorieTo) {
        this.fusionneCategorieTo = fusionneCategorieTo;
    }

    public List<String> getFusionnesCategorieBy() {
        return fusionnesCategorieBy;
    }

    public void setFusionnesCategorieBy(List<String> fusionnesCategorieBy) {
        this.fusionnesCategorieBy = fusionnesCategorieBy;
    }

    public String getFusionneTo() {
        return fusionneTo;
    }

    public void setFusionneTo(String fusionneTo) {
        this.fusionneTo = fusionneTo;
    }

    public List<String> getFusionnesBy() {
        return fusionnesBy;
    }

    public void setFusionnesBy(List<String> fusionnesBy) {
        this.fusionnesBy = fusionnesBy;
    }

    public YvsGrhElementSalaire getSelectElement() {
        return selectElement;
    }

    public void setSelectElement(YvsGrhElementSalaire selectElement) {
        this.selectElement = selectElement;
    }

    public boolean isCompteTiers() {
        return compteTiers;
    }

    public void setCompteTiers(boolean compteTiers) {
        this.compteTiers = compteTiers;
    }

    public boolean isActiveRename() {
        return activeRename;
    }

    public void setActiveRename(boolean activeRename) {
        this.activeRename = activeRename;
    }

    public List<YvsGrhRubriqueBulletin> getListRubrique() {
        return listRubrique;
    }

    public void setListRubrique(List<YvsGrhRubriqueBulletin> listRubrique) {
        this.listRubrique = listRubrique;
    }

    public RubriqueBulletin getRubrique() {
        return rubrique;
    }

    public void setRubrique(RubriqueBulletin rubrique) {
        this.rubrique = rubrique;
    }

    public List<YvsSocietes> getListSociete() {
        return listSociete;
    }

    public void setListSociete(List<YvsSocietes> listSociete) {
        this.listSociete = listSociete;
    }

    public void setChaineSelectRegle(String chaineSelectRegle) {
        this.chaineSelectRegle = chaineSelectRegle;
    }

    public String getChaineSelectRegle() {
        return chaineSelectRegle;
    }

    public List<YvsGrhCategorieElement> getListRegleCagorie() {
        return listRegleCagorie;
    }

    public void setListRegleCagorie(List<YvsGrhCategorieElement> listRegleCagorie) {
        this.listRegleCagorie = listRegleCagorie;
    }

    public List<YvsGrhElementSalaire> getListRegleImport() {
        return listRegleImport;
    }

    public void setListRegleImport(List<YvsGrhElementSalaire> listRegleImport) {
        this.listRegleImport = listRegleImport;
    }

    public boolean isEnSaisie() {
        return enSaisie;
    }

    public void setEnSaisie(boolean enSaisie) {
        this.enSaisie = enSaisie;
    }

    public List<Options> getListElt() {
        return listElt;
    }

    public void setListElt(List<Options> listElt) {
        this.listElt = listElt;
    }

    public List<Options> getListRetenue() {
        return listRetenue;
    }

    public void setListRetenue(List<Options> listRetenue) {
        this.listRetenue = listRetenue;
    }

    public String getChoixElt() {
        return choixElt;
    }

    public void setChoixElt(String choixElt) {
        this.choixElt = choixElt;
    }

    public YvsGrhCategorieElement getSelectedCategorie() {
        return selectedCategorie;
    }

    public void setSelectedCategorie(YvsGrhCategorieElement selectedCategorie) {
        this.selectedCategorie = selectedCategorie;
    }

    public TagCloudModel getModelSigne() {
        return modelSigne;
    }

    public void setModelSigne(TagCloudModel modelSigne) {
        this.modelSigne = modelSigne;
    }

    public TagCloudModel getModel() {
        return model;
    }

    public void setModel(TagCloudModel model) {
        this.model = model;
    }

    public int getSourceRegle() {
        return sourceRegle;
    }

    public TagCloudModel getModelSigne1() {
        return modelSigne1;
    }

    public void setModelSigne1(TagCloudModel modelSigne1) {
        this.modelSigne1 = modelSigne1;
    }

    public List<Options> getListEltPrime() {
        return listEltPrime;
    }

    public void setListEltPrime(List<Options> listEltPrime) {
        this.listEltPrime = listEltPrime;
    }

    public List<Options> getListConstante() {
        return listConstante;
    }

    public void setListConstante(List<Options> listConstante) {
        this.listConstante = listConstante;
    }

    public TagCloudModel getModelSigne2() {
        return modelSigne2;
    }

    public void setModelSigne2(TagCloudModel modelSigne2) {
        this.modelSigne2 = modelSigne2;
    }

    public void setSourceRegle(int sourceRegle) {
        this.sourceRegle = sourceRegle;
    }

    public String getLabelMontant() {
        return labelMontant;
    }

    public String getLabelButon() {
        return labelButon;
    }

    public void setLabelButon(String labelButon) {
        this.labelButon = labelButon;
    }

    public String getLabelList() {
        return labelList;
    }

    public void setLabelList(String labelList) {
        this.labelList = labelList;
    }

    public void setLabelMontant(String labelMontant) {
        this.labelMontant = labelMontant;
    }

    public String getLabelPourcentage() {
        return labelPourcentage;
    }

    public void setLabelPourcentage(String labelPourcentage) {
        this.labelPourcentage = labelPourcentage;
    }

    public boolean isDisplayForm() {
        return displayForm;
    }

    public void setDisplayForm(boolean displayForm) {
        this.displayForm = displayForm;
    }

    public boolean isDisplayOther() {
        return displayOther;
    }

    public void setDisplayOther(boolean displayOther) {
        this.displayOther = displayOther;
    }

    public ElementSalaire getElements() {
        return elements;
    }

    public YvsGrhElementSalaire getSelectedElement() {
        return selectedElement;
    }

    public void setSelectedElement(YvsGrhElementSalaire selectedElement) {
        this.selectedElement = selectedElement;
    }

    public void setElements(ElementSalaire elements) {
        this.elements = elements;
    }

    public List<YvsGrhCategorieElement> getListCategorie() {
        return listCategorie;
    }

    public void setListCategorie(List<YvsGrhCategorieElement> listCategorie) {
        this.listCategorie = listCategorie;
    }

    public List<YvsGrhElementSalaire> getListeRegle() {
        return listeRegle;
    }

    public void setListeRegle(List<YvsGrhElementSalaire> listeRegle) {
        this.listeRegle = listeRegle;
    }

    public boolean isDisplayExpR() {
        return displayExpR;
    }

    public void setDisplayExpR(boolean displayExpR) {
        this.displayExpR = displayExpR;
    }

    public boolean isDisplayQuantite() {
        return displayQuantite;
    }

    public void setDisplayQuantite(boolean displayQuantite) {
        this.displayQuantite = displayQuantite;
    }

    public boolean isDisplayPourcentage() {
        return displayPourcentage;
    }

    public void setDisplayPourcentage(boolean displayPourcentage) {
        this.displayPourcentage = displayPourcentage;
    }

    public boolean isDisplayMontant() {
        return displayMontant;
    }

    public void setDisplayMontant(boolean displayMontant) {
        this.displayMontant = displayMontant;
    }

    public CategorieRegleSalaire getNewCategorie() {
        return newCategorie;
    }

    public void setNewCategorie(CategorieRegleSalaire newCategorie) {
        this.newCategorie = newCategorie;
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

    public String getTextFind() {
        return textFind;
    }

    public void setTextFind(String textFind) {
        this.textFind = textFind;
    }

    public String getFormuleF() {
        return formuleF;
    }

    public void setFormuleF(String formuleF) {
        this.formuleF = formuleF;
    }

    public boolean isDisplayBtnDelCat() {
        return displayBtnDelCat;
    }

    public void setDisplayBtnDelCat(boolean displayBtnDelCat) {
        this.displayBtnDelCat = displayBtnDelCat;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public void setResultEvaluation(String resultEvaluation) {
        this.resultEvaluation = resultEvaluation;
    }

    public String getResultEvaluation() {
        return resultEvaluation;
    }

    public void choixRetenue(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            boolean b = (boolean) ev.getNewValue();
            if (elements.getTypeMontant() == 2) {
                displayOther = b;
            }
        }
    }

    public void saisiePourcentage(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            double d = (double) ev.getNewValue();
            if (elements.getTypeMontant() == 2 && elements.isRetenue()) {
                elements.setTauxSalarial(d);
            }
        }
    }

    public void choixTypeMontant(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            int v = (int) ev.getNewValue();
            afficherLesChamps(v);
        }
    }

    public void afficherLesChamps(int v) {
        switch (v) {
            case 1: //montant fixe
                displayOther = displayQuantite = displayExpR = displayPourcentage = false;
                displayMontant = true;
                labelMontant = "Montant";
                break;
            case 2://pourcentage
                displayQuantite = displayExpR = false;
                displayPourcentage = true;
                displayMontant = true;
                displayOther = elements.isRetenue();
                labelMontant = "Pourcentage";
                labelPourcentage = "Pourcentage basé sur ";
                break;
            case 3://expression algébrique
                displayOther = displayQuantite = displayMontant = displayPourcentage = false;
                displayExpR = true;
                break;
            case 4://quantité
                displayOther = displayMontant = false;
                displayQuantite = displayPourcentage = true;
                displayExpR = false;
                labelPourcentage = "Quantité basé sur ";
                break;
        }
    }

    @Override
    public boolean controleFiche(ElementSalaire bean) {
        if (bean.getNom() == null) {
            getMessage("Vous devez spécifier le nom", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getCategorie().getId() <= 0) {
            getErrorMessage("Vous devez choisir une catégorie !");
        }
        if (bean.getCode() == null) {
            getMessage("Vous devez spécifier le code de la règle", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        //Le code doit être unique et sans espace
        YvsGrhElementSalaire old = (YvsGrhElementSalaire) dao.loadOneByNameQueries("YvsGrhElementSalaire.findByCode0", new String[]{"code", "societe"}, new Object[]{bean.getCode(), currentAgence.getSociete()});
        if (old != null) {
            if (bean.getId() <= 0 || (bean.getId() > 0 && bean.getId() != old.getId())) {
                getErrorMessage("Une règle de salaire a déjà été définit avec ce code");
                return false;
            }
        }
        if (bean.getCode().contains(" ")) {
            getErrorMessage("La règle de salaire doit avoir un code sans espace !");
            return false;
        }
        if (bean.getTypeMontant() == 2) {
            if (bean.getBasePourcentage().getId() == 0) {
                getMessage("vous devez choisir la base du pourcentage", FacesMessage.SEVERITY_ERROR);
                return false;
            }
            if (bean.isRetenue()) {
                if (bean.getTauxPatronale() <= 0 && bean.getTauxSalarial() <= 0) {
                    getMessage("vous devez indiquer le pourcentage", FacesMessage.SEVERITY_ERROR);
                    return false;
                }
            } else {
                if (bean.getMontant() <= 0) {
                    getMessage("vous devez indiquer le pourcentage", FacesMessage.SEVERITY_ERROR);
                    return false;
                }
            }
        }
        if (bean.getTypeMontant() == 4) {
            if (bean.getBasePourcentage().getId() == 0) {
                getMessage("vous devez choisir la base de calcul de la quantité", FacesMessage.SEVERITY_ERROR);
                return false;
            }
            if (bean.getExpQuantite().getId() == 0) {
                getMessage("vous devez choisir l'expression de la quantité", FacesMessage.SEVERITY_ERROR);
                return false;
            }
        }
        if (bean.isRegleArondi()) {
            getErrorMessage("Vous ne pouvez modifier cette règle de salaire !");
            return false;
        }
        //contrôle le nombre de règle allocation congé
        if (bean.isRegleConge()) {
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            Long n = (Long) dao.loadObjectByNameQueries("YvsGrhElementSalaire.countRegleAllocConge", champ, val);
            if (n != null) {
                if (n > 0) {
                    openDialog("dlgConfirmSave");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void updateBean() {

    }

    @Override
    public ElementSalaire recopieView() {
        ElementSalaire bean = new ElementSalaire();
        cloneObject(bean, elements);
        return bean;
    }

    /**
     *
     * @param bean
     */
    @Override
    public void populateView(ElementSalaire bean) {
        cloneObject(elements, bean);
        afficherLesChamps(elements.getTypeMontant());
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        ElementSalaire elt = (ElementSalaire) ev.getObject();
        populateView(elt);
    }

    @Override
    public void loadAll() {
        //charge les catégories de règles salariale
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        listCategorie = dao.loadNameQueries("YvsGrhCategorieElement.findAll", champ, val, 0, 100);

        //charge les éléments de salaire
        loadAllRegle(true, true);
        //charge la liste indicatrice des éléments de salaire        
        buildListElt();
        //ordonancement par categorie
        loadAllRegleByCategorie();
        //charge les sociétés
        champ = new String[]{};
        val = new Object[]{};
        listSociete = dao.loadNameQueries("YvsSocietes.findBySameGroupe", new String[]{"groupe"}, new Object[]{currentAgence.getSociete().getGroupe()});
        loadAllRubrique();
        //créer la règle arrondi
        createRegleArrondi();
        //CHARGE LES structure
        loadListStructureSalaire();
    }

    public void loadAllRegle(boolean avancer, boolean init) {
        ParametreRequete p = new ParametreRequete("y.categorie.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
        paginator.addParam(p);
        listeRegle = paginator.executeDynamicQuery("YvsGrhElementSalaire", "y.categorie.codeCategorie, y.code", avancer, init, (int) imax, dao);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsGrhElementSalaire> re = paginator.parcoursDynamicData("YvsGrhElementSalaire", "y", "y.categorie.codeCategorie, y.code", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void pagineResult(boolean next) {
        loadAllRegle(next, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllRegle(true, true);
    }

    public void changeMaxResult(ValueChangeEvent ev) {
        imax = (long) ev.getNewValue();
        loadAllRegle(true, true);
    }

    public void loadAllRegleByCategorie() {
        listRegleCagorie.clear();
        for (YvsGrhElementSalaire e : listeRegle) {
            boolean trouv = false;
//            List<YvsGrhCategorieElement> list = new ArrayList<>();
//            list.addAll(listRegleCagorie);
            for (YvsGrhCategorieElement c : listRegleCagorie) {
                if (c.equals(e.getCategorie())) {
                    listRegleCagorie.get(listRegleCagorie.indexOf(c)).getElementsSalaires().add(e);
                    trouv = true;
                    break;
                }
            }
            if (!trouv) {
                listRegleCagorie.add(e.getCategorie());
            }
        }
    }

    private void buildListElt() {
        listElt.clear();
        listEltPrime.clear();
        listConstante.clear();
        listRetenue.clear();
        for (YvsGrhElementSalaire e : listeRegle) {
            listElt.add(new Options(e.getCode(), e.getNom()));
        }
        //charge les type de prime
        champ = new String[]{"societe", "retenue"};
        val = new Object[]{currentAgence.getSociete(), false};
        List<YvsGrhTypeElementAdditionel> l = dao.loadNameQueries("YvsGrhTypeElementAdditionel.findAll", champ, val);
        for (YvsGrhTypeElementAdditionel te : l) {
            listEltPrime.add(new Options("contrat." + te.getCodeElement(), te.getLibelle()));
        }
        val = new Object[]{currentAgence.getSociete(), true};
        List<YvsGrhTypeElementAdditionel> lr = dao.loadNameQueries("YvsGrhTypeElementAdditionel.findAll", champ, val);
        for (YvsGrhTypeElementAdditionel te : lr) {
            listRetenue.add(new Options("retenue." + te.getCodeElement(), te.getLibelle()));
        }
        //charge les constante fixe  
        listConstante.add(new Options(Constantes.S_SALAIRE_CONVENTIONNELLE, "Salaire conventionnel"));
        listConstante.add(new Options(Constantes.S_SALAIRE_CONTRACTUELLE, "Salaire contractuel"));
        listConstante.add(new Options(Constantes.S_SALAIRE_SUR_OBJETIF, "Rémunération selon l'objectif"));
        listConstante.add(new Options(Constantes.S_SALAIRE_HORAIRE, "Salaire horaire"));
        listConstante.add(new Options(Constantes.S_ENFANT, "Nombre d'enfant mineur"));
        listConstante.add(new Options(Constantes.S_JOUR_DE_CONGE, "Congé restant"));
        listConstante.add(new Options(Constantes.S_HEURE_SUPPLEMENTAIRE, "Total d'heure supplémentaire"));
//      listConstante.add(new Options(Constantes.S_JOUR_DE_TRAVAIL, "Nombre de jour de travail"));
        listConstante.add(new Options(Constantes.S_HEURE_JOUR_FERIE, "Heure des jours fériés"));
        listConstante.add(new Options(Constantes.S_HEURE_DIMANCHE, "Heure des dimanches"));
        listConstante.add(new Options(Constantes.S_ANCIENETE, "Ancienneté"));
        listConstante.add(new Options(Constantes.S_NB_HEURE_REQUISE, "Nombre d'heure de travail requis"));
        listConstante.add(new Options(Constantes.S_NB_JOUR_REQUIS, "Nombre de jour de travail requis"));
        listConstante.add(new Options(Constantes.S_NB_HEURE_EFFECTIF, "Nombre d'heure de travail effectif"));
        listConstante.add(new Options(Constantes.S_NB_JOUR_EFFECTIF, "Nombre de jour de travail effectif"));
        listConstante.add(new Options(Constantes.S_NOTE_DE_FRAIS, "Note de frais"));
        listConstante.add(new Options(Constantes.S_DUREE_PREAVIS, "Durée du préavis"));
        listConstante.add(new Options(Constantes.S_NB_JOUR_PASSE, "Nombre de jour passé"));
        listConstante.add(new Options(Constantes.S_NB_JOUR_ACCEPTE_IN_EXO, "Nombre de jour validé pour les congés"));
        listConstante.add(new Options(Constantes.S_NB_MOIS_ACCEPTE_IN_EXO, "Nombre de mois validé pour les congés"));
        listConstante.add(new Options(Constantes.S_BASE_DUREE_CONGE, "Base de calcul de la durée des congé"));

    }

    public void delete() {
        System.err.println("chaineSelectRegle = " + chaineSelectRegle);
        if ((chaineSelectRegle != null) ? !chaineSelectRegle.equals("") : false) {
            List<Long> l = decomposeIdSelection(chaineSelectRegle);
            List<YvsGrhElementSalaire> list = new ArrayList<>();
            YvsGrhElementSalaire bean;
            try {
                for (Long ids : l) {
                    bean = listeRegle.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
                }
                listeRegle.removeAll(list);
                chaineSelectRegle = "";
                succes();
                resetFiche();
            } catch (Exception ex) {
                getMessage("Impossible de supprimer cette règle", FacesMessage.SEVERITY_ERROR);
                chaineSelectRegle = "";
            }
        }
    }

    YvsGrhElementSalaire currentElement;

    public void findOneElement(String code) {
        if (Util.asString(code)) {
            YvsGrhElementSalaire el = (YvsGrhElementSalaire) dao.loadOneByNameQueries("YvsGrhElementSalaire.findByCodeAll", new String[]{"code", "societe"}, new Object[]{code.trim(), currentAgence.getSociete()});
            if (el != null) {
                populateView(UtilGrh.buildElementSalaire(el));
                update("grid_form_res");
            }
        }
    }

    @Override
    public boolean saveNew() {
        return true;
    }

    public boolean saveNew(boolean controle) {
        if (controle ? controleFiche(elements) : true) {
            YvsGrhElementSalaire entity = UtilGrh.buildElementSalaire(elements);
            entity.setCategorie(listCategorie.get(listCategorie.indexOf(entity.getCategorie())));
            if (entity.getRubrique() != null) {
                entity.setRubrique(listRubrique.get(listRubrique.indexOf(entity.getRubrique())));
            }
            entity.setAuthor(currentUser);
            if (elements.getId() <= 0) {
                entity.setId(null);
                currentElement = (YvsGrhElementSalaire) dao.save1(entity);
                elements.setId(entity.getId());
                listeRegle.add(0, currentElement);
            } else {
                entity.setId(elements.getId());
                dao.update(entity);
                int idx = listeRegle.indexOf(entity);
                if (idx >= 0) {
                    listeRegle.set(idx, entity);
                } else {
                    listeRegle.add(0, entity);
                }
            }
            succes();
            enSaisie = false;
        }
        return true;
    }

    public void saveNewRegle(boolean continueSave) {
        if (continueSave) {
            String requete = "UPDATE yvs_element_salaire set allocation_conge=false WHERE societe=?";
            yvs.dao.Options[] lp = new yvs.dao.Options[]{new yvs.dao.Options(currentAgence.getSociete().getId(), 1)};
            dao.requeteLibre(requete, lp);
            saveNew(false);
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(elements);
        elements.setExpQuantite(new OptionEltSalaire());
        elements.setBasePourcentage(new OptionEltSalaire());
        elements.setCategorie(new CategorieRegleSalaire());
        elements.setRubrique(new RubriqueBulletin());
        elements.setCompteCotisation(new Comptes());
        elements.setCompteCharge(new Comptes());
        elements.setRetenue(false);
        elements.setSaisiCompteTiers(false);
        elements.setActif(true);
        elements.setRegleArondi(false);
        elements.setRegleConge(false);
        enSaisie = true;
        elements.setId(-1);
    }

    public void beginSaisie() {
        enSaisie = true;
        update("re-gridButon");
    }

    int champCompte = 1;  //distingue la champ de texte compte qui solicite la recherche:   1=Compte général de charge, 2 compte de cotisation

    public void ecouteSaisieCG(String numCmpte) {
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (service != null && (numCmpte != null) ? !numCmpte.isEmpty() : false) {
            service.findCompteByNum(numCmpte);
            elements.getCompteCharge().setError(service.getListComptes().isEmpty());
            if (service.getListComptes() != null) {
                if (!service.getListComptes().isEmpty()) {
                    if (service.getListComptes().size() == 1) {
                        elements.getCompteCharge().setError(false);
                        cloneObject(elements.getCompteCharge(), UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                        if (elements.getCompteCharge().isSaisieCompteTiers()) {
                            elements.setSaisiCompteTiers(true);
                        }
                    } else {
                        elements.getCompteCharge().setError(false);
                        elements.getCompteCharge().setId(-1);
                        openDialog("dlgCmpteG");
                        update("table_cptG_reg_s");
                    }
                } else {
                    elements.getCompteCharge().setError(true);
                    elements.getCompteCharge().setId(-1);
                }
            } else {
                elements.getCompteCharge().setError(true);
                elements.getCompteCharge().setId(-1);

            }
        } else {
            elements.getCompteCharge().setId(-1);
        }

    }

    public void choisirCompte(SelectEvent ev) {
        if (ev != null) {
            cloneObject(elements.getCompteCharge(), UtilCompta.buildSimpleBeanCompte(((YvsBasePlanComptable) ev.getObject())));
            if (elements.getCompteCharge().isSaisieCompteTiers()) {
                elements.setSaisiCompteTiers(true);
                update("tab-regle-salaire:element_saisi_compte_tiers");
            }
        }
    }

    public void ecouteSaisieCG_(String numCmpte) {
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (service != null && (numCmpte != null) ? !numCmpte.isEmpty() : false) {
            service.findCompteByNum(numCmpte);
            elements.getCompteCotisation().setError(service.getListComptes().isEmpty());
            if (service.getListComptes() != null) {
                if (!service.getListComptes().isEmpty()) {
                    if (service.getListComptes().size() == 1) {
                        elements.getCompteCotisation().setError(false);
                        cloneObject(elements.getCompteCotisation(), UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                        if (elements.getCompteCotisation().isSaisieCompteTiers()) {
                            elements.setSaisiCompteTiers(true);
                        }
                    } else {
                        elements.getCompteCotisation().setError(false);
                        elements.getCompteCotisation().setId(-1);
                        openDialog("dlgCmpteG_");
                        update("table_cptG_reg_s_");
                    }
                } else {
                    elements.getCompteCotisation().setError(true);
                    elements.getCompteCotisation().setId(-1);
                }
            } else {
                elements.getCompteCotisation().setError(true);
                elements.getCompteCotisation().setId(-1);
            }
        } else {
            elements.getCompteCotisation().setId(-1);
        }

    }

    public void choisirCompte_(SelectEvent ev) {
        if (ev != null) {
            cloneObject(elements.getCompteCotisation(), UtilCompta.buildSimpleBeanCompte(((YvsBasePlanComptable) ev.getObject())));
            if (elements.getCompteCotisation().isSaisieCompteTiers()) {
                elements.setSaisiCompteTiers(true);
                update("tab-regle-salaire:element_saisi_compte_tiers");
            }
        }
    }

    public void selectCategorie(SelectEvent ev) {
        if (ev != null) {
            YvsGrhCategorieElement cat = (YvsGrhCategorieElement) ev.getObject();
            elements.setCategorie(UtilGrh.buildCategorieRegle(cat));
            cloneObject(newCategorie, elements.getCategorie());
            update("cat-regle-salaire");
            update("table-cat-elt-sal");
            update("grille-add-cat-reg");
        }
    }

    public void selectCategorieToUpdate(YvsGrhCategorieElement cat) {
        if (cat != null) {
            elements.setCategorie(UtilGrh.buildCategorieRegle(cat));
            cloneObject(newCategorie, elements.getCategorie());
            updateCategorie = displayBtnDelCat = true;
            update("cat-regle-salaire");
            updateCategorie = displayBtnDelCat = true;
        }
    }

//    public void unSelectCategorie(UnselectEvent ev) {
//        if (ev != null) {
//            elements.setCategorie(new CategorieRegleSalaire());
//            updateCategorie = displayBtnDelCat = false;
//            labelBtnCat = "Créer";
//            update("cat-regle-salaire");
//            update("table-cat-elt-sal");
//            update("grille-add-cat-reg");
//        }
//    }
//    private CategorieRegleSalaire buildBeanCategorie(YvsGrhCategorieElement c) {
//        CategorieRegleSalaire r = new CategorieRegleSalaire(c.getId(), c.getCodeCategorie(), c.getDesignation());
//        r.setDescription(c.getDescription());
//        return r;
//    }
    public void createNewCategorie() {
        if (newCategorie.getCode() != null) {
            //vérifie le code de la catégorie
            if (!updateCategorie) {
                champ = new String[]{"societe", "code"};
                val = new Object[]{currentAgence.getSociete(), newCategorie.getCode()};
                long nb = (long) dao.loadObjectByNameQueries("YvsGrhCategorieElement.findByCodeCategorieC", champ, val);
                if (nb > 0) {
                    getErrorMessage("Une catégorie de code identique a été trouvée !");
                    return;
                }
            }
            if (newCategorie.isDefaultPrime()) {
                List<YvsGrhCategorieElement> list = dao.loadNameQueries("YvsGrhCategorieElement.findByPrime", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                for (YvsGrhCategorieElement e : list) {
                    if (e.getDefaultPrime()) {
                        e.setDefaultPrime(false);
                        dao.update(e);
                    }
                }

            }
            if (newCategorie.isDefaultretenue()) {
                List<YvsGrhCategorieElement> list = dao.loadNameQueries("YvsGrhCategorieElement.findByRetenue", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                for (YvsGrhCategorieElement e : list) {
                    if (e.getDefaultRetenue()) {
                        e.setDefaultRetenue(false);
                        dao.update(e);
                    }
                }

            }

            YvsGrhCategorieElement catElt = new YvsGrhCategorieElement();
            catElt.setCodeCategorie(newCategorie.getCode());
            catElt.setDescription(newCategorie.getDescription());
            catElt.setDesignation(newCategorie.getDesignation());
            catElt.setSociete(currentAgence.getSociete());
            catElt.setActif(newCategorie.isActif());
            catElt.setAuthor(currentUser);
            catElt.setDefaultPrime(newCategorie.isDefaultPrime());
            catElt.setDefaultRetenue(newCategorie.isDefaultretenue());
            if (!updateCategorie) {
                catElt = (YvsGrhCategorieElement) dao.save1(catElt);
                newCategorie.setId(catElt.getId());
                if (newCategorie != null) {
                    elements.setCategorie(newCategorie);
                }
                listCategorie.add(0, catElt);
                updateCategorie = false;
            } else {
                catElt.setId(newCategorie.getId());
                dao.update(catElt);
                listCategorie.set(listCategorie.indexOf(catElt), catElt);
                updateCategorie = false;
            }
            newCategorie = new CategorieRegleSalaire();
            updateCategorie = false;
            selectedCategorie = null;
            update("cat-regle-salaire");
        }
    }

    public void deleteCategorie(YvsGrhCategorieElement c) {
        if (newCategorie.getId() != 0) {
            try {
                c.setAuthor(currentUser);
                dao.delete(c);
                listCategorie.remove(c);
                newCategorie = new CategorieRegleSalaire();
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cette catégorie");
            }
        }
    }

    /**
     * *****GERER LES RUBRIQUES*****
     */
    public void createNewRubrique() {
        if (rubrique.getCode() != null) {
            YvsGrhRubriqueBulletin er = new YvsGrhRubriqueBulletin(rubrique.getId());
            er.setCode(rubrique.getCode());
            er.setDesignation(rubrique.getDesignation());
            er.setSociete(currentAgence.getSociete());
            er.setAuthor(currentUser);
            if (er.getId() < 1) {
                er = (YvsGrhRubriqueBulletin) dao.save1(er);
                rubrique.setId(er.getId());
                listRubrique.add(0, er);
            } else {
                dao.update(er);
                int idx = listRubrique.indexOf(er);
                if (idx > -1) {
                    listRubrique.set(idx, er);
                }
            }
            newRubrique();
            succes();
            update("input_select_rubrique");
        }
    }

    public void newRubrique() {
        rubrique = new RubriqueBulletin();
    }

    public void loadOnRubrique(SelectEvent ev) {
        if (ev != null) {
            YvsGrhRubriqueBulletin er = (YvsGrhRubriqueBulletin) ev.getObject();
            rubrique = new RubriqueBulletin(er.getId(), er.getCode(), er.getDesignation());
        }
    }

    public void unLoadOnRubrique(UnselectEvent ev) {
        if (ev != null) {
            newRubrique();
        }
    }

    public void selectRubrique(YvsGrhRubriqueBulletin er) {
        if (er != null) {
            elements.setRubrique(new RubriqueBulletin(er.getId(), er.getCode(), er.getDesignation()));
            rubrique = new RubriqueBulletin();
        }
    }

    public void deleteRubrique(YvsGrhRubriqueBulletin r) {
        try {
            r.setAuthor(currentUser);
            dao.delete(r);
            listRubrique.remove(r);
            if (rubrique.getId() == r.getId()) {
                newRubrique();
                update("regS_form_rubrique");
            }
            update("input_select_rubrique");
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette rubrique. des éléments de salaire y sont rataché !");
            Logger.getLogger(ManagedRegleSalaire.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadAllRubrique() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listRubrique = dao.loadNameQueries("YvsGrhRubriqueBulletin.findAll", champ, val);
        rubrique = new RubriqueBulletin();
    }

    /**
     * *****FIN RUBRIQUES
     *
     *****
     * @param ev
     */
    public void selectRegle(SelectEvent ev) {
        execute("collapseForm('regleSalaire')");
        YvsGrhElementSalaire e = (YvsGrhElementSalaire) ev.getObject();
        onSelectObject(e);
        chaineSelectRegle = listeRegle.indexOf(e) + "";
    }

    @Override
    public void onSelectObject(YvsGrhElementSalaire y) {
        selectedElement = y;
        if (selectedElement.getBasePourcentage() != null && !listeRegle.contains(selectedElement.getBasePourcentage())) {
            listeRegle.add(selectedElement.getBasePourcentage());
        }
        if (selectedElement.getQuantite() != null && !listeRegle.contains(selectedElement.getQuantite())) {
            listeRegle.add(selectedElement.getQuantite());
        }
        populateView(UtilGrh.buildElementSalaire(selectedElement));
        update("grid_form_res");
    }

    public void selectedChooseRegle(YvsGrhElementSalaire e) {
        populateView(UtilGrh.buildElementSalaire(e));
    }

    public void selectBasePlage(ValueChangeEvent ev) {
        listeRegle.clear();
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id == -2) {//charge les liste des règle dans la boite de dialogue
                listeRegle = dao.loadNameQueries("YvsGrhElementSalaire.findAll", champ, val, 0, 100);
                update("form-rs-01");
                openDialog("dlgListRegle");
            }
        }
    }

    public void choixRegleQ(SelectEvent ev) {
        ElementSalaire e = (ElementSalaire) ev.getObject();
        if (e != null) {
            elements.setExpQuantite(e.getExpQuantite());
        }
//        update("tab-regle-salaire");
//        closeDialog("dlgListRegle");
    }

    public void choixRegle1(SelectEvent ev) {
        ElementSalaire e = (ElementSalaire) ev.getObject();
        if (elements.getTypeMontant() != 3) {
            elements.setBasePourcentage(new OptionEltSalaire(e.getId(), e.getNom()));
            update("tab-regle-salaire");
        } else {
            elements.setExpressionRegle(elements.getExpressionRegle() + " " + e.getCode());
            update("tab-regle-salaire:grid-regle-s0");
        }
//        closeDialog("dlgListRegle1");
    }

    private List<YvsGrhStructureSalaire> listeStructure = new ArrayList<>();
    private YvsGrhStructureSalaire selectedStructure[];

    public YvsGrhStructureSalaire[] getSelectedStructure() {
        return selectedStructure;
    }

    public void setSelectedStructure(YvsGrhStructureSalaire[] selectedStructure) {
        this.selectedStructure = selectedStructure;
    }

    public List<YvsGrhStructureSalaire> getListeStructure() {
        return listeStructure;
    }

    public void setListeStructure(List<YvsGrhStructureSalaire> listeStructure) {
        this.listeStructure = listeStructure;
    }

    public void openViewToAddInStructure(YvsGrhElementSalaire e) {
        selectedElement = e;
        openDialog("dlgLierStruct");
        update("table_regleSalaire");
    }

    public void toogleVisibleOnBulletin(YvsGrhElementSalaire e) {
        e.setVisibleBulletin(!e.getVisibleBulletin());
        e.setAuthor(currentUser);
        dao.update(e);
        update("table_regleSalaire");
    }

    public void toogleActive(YvsGrhElementSalaire e) {
        e.setActif(!e.getActif());
        e.setDateUpdate(new Date());
        e.setAuthor(currentUser);
        dao.update(e);
        update("table_regleSalaire");
    }

    public void toogleActives(YvsGrhCategorieElement e) {
        e.setActif(!e.getActif());
        e.setDateUpdate(new Date());
        e.setAuthor(currentUser);
        dao.update(e);
        getMessage("Succès !", FacesMessage.SEVERITY_INFO);
        update("table-cat-elt-sal");
    }

    //ouvrir la liste des structures de règle de salaire
    public void loadListStructureSalaire() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listeStructure = dao.loadListTableByNameQueries("YvsStructureSalaire.findAll", champ, val);
//        listeStructure.clear();
//        for (YvsGrhStructureSalaire ss : l) {
//            listeStructure.add(UtilGrh.buildBeanStructure(ss));
//            listeStructure.add(new StructureElementSalaire(ss.getId(), ss.getNom(), ss.getNom()));
//        }
//        openDialog("dlgLierStruct");
//        update("tab-rs-lierS");
    }

    //lier des règles de salaire à des structures de salaire
    public void liaisonRegle() {
        if (selectedElement != null) {
            for (YvsGrhStructureSalaire st : selectedStructure) {
                //le lien ne doit pas déjà exister
                YvsGrhElementStructure e = (YvsGrhElementStructure) dao.loadOneByNameQueries("YvsElementStructure.findElementStructure_", new String[]{"element", "structure"}, new Object[]{selectedElement, st});
                if (e != null) {
                    e.setActif(true);
                    e.setAuthor(currentUser);
                    dao.update(e);
                } else {
                    e = new YvsGrhElementStructure();
                    e.setActif(true);
                    e.setAuthor(currentUser);
                    e.setElement(selectedElement);
                    e.setId(null);
                    e.setStructure(st);
                    dao.save(e);
                }
            }
        }
        succes();
        closeDialog("dlgLierStruct");
    }

    public void liaisonManyRegle() {
        for (YvsGrhStructureSalaire st : selectedStructure) {
            List<Integer> keys = decomposeSelection(chaineSelectRegle);
            if (!keys.isEmpty()) {
                //le lien ne doit pas déjà exister
                YvsGrhElementSalaire elt;
                YvsGrhElementStructure e;
                for (Integer idx : keys) {
                    elt = listeRegle.get(idx);
                    e = (YvsGrhElementStructure) dao.loadOneByNameQueries("YvsElementStructure.findElementStructure_", new String[]{"element", "structure"}, new Object[]{elt, st});
                    if (e != null) {
                        e.setActif(true);
                        e.setAuthor(currentUser);
                        dao.update(e);
                    } else {
                        e = new YvsGrhElementStructure();
                        e.setActif(true);
                        e.setAuthor(currentUser);
                        e.setElement(elt);
                        e.setId(null);
                        e.setStructure(st);
                        dao.save(e);
                    }
                }
                succes();
                closeDialog("dlgLierStruct");
            } else {
                getWarningMessage("Aucune selection n'a été trouvé.");
            }
        }
    }

    private boolean help; //permet de spécifier si la liste des règles est ouverte comme aide mémoire ou non.

//    }
    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * *Récupérer les règles de salaire d'une société et les transférer dans la
     * société courante
     *
     ***
     * @param ev
     */
//    private List<YvsGrhElementSalaire> listEntityToImport;
    public void transfertRegle(SelectEvent ev) {
        YvsSocietes s = (YvsSocietes) ev.getObject();
        champ = new String[]{"societe"};
        val = new Object[]{s};
//        listEntityToImport = dao.loadNameQueries("YvsGrhElementSalaire.findAll_", champ, val);
        listRegleImport = dao.loadNameQueries("YvsGrhElementSalaire.findAll_", champ, val);
        openDialog("dlgRegToImport");
        update("table_regle_to_import");
    }
    boolean regleExist;

    public YvsGrhElementSalaire importRegleInsociete(YvsGrhElementSalaire reg) {
        YvsGrhCategorieElement idCat;
        YvsGrhRubriqueBulletin idRubrique;
        //vérifier qu'il y ait pas déjà une règle de salaire dans cette société avec le code de la règle courante.
        champ = new String[]{"code", "societe"};
        val = new Object[]{reg.getCode(), currentUser.getAgence().getSociete()};
        YvsGrhElementSalaire enE = (YvsGrhElementSalaire) dao.loadObjectByNameQueries("YvsGrhElementSalaire.findByCode", champ, val);
        if (enE == null) {
            reg.setId(null);
//                //si la règle a une catégorie
            if (reg.getCategorie() != null) {
                //si une catégorie n'existe pas avec le même code dans la société
                val = new Object[]{reg.getCategorie().getCodeCategorie(), currentUser.getAgence().getSociete()};
                YvsGrhCategorieElement c_ = (YvsGrhCategorieElement) dao.loadObjectByNameQueries("YvsGrhCategorieElement.findByCodeCategorie", champ, val);
                if (c_ == null) {//si la catégorie n'existe pas encore dans la société
                    reg.getCategorie().setId(null);
                    reg.getCategorie().setSociete(currentAgence.getSociete());
                    reg.getCategorie().setAuthor(currentUser);
                    idCat = (YvsGrhCategorieElement) dao.save1(reg.getCategorie());
                    listCategorie.add(0, idCat);
                } else {
                    idCat = (new YvsGrhCategorieElement(c_.getId()));
                }
            } else {
                idCat = null;
            }
//                //si la règle a une rubrique
            if (reg.getRubrique() != null) {
                YvsGrhRubriqueBulletin r_ = containsRubrique(listRubrique, reg.getRubrique().getCode());
                if (r_ == null) {
                    reg.getRubrique().setId(null);
                    reg.getRubrique().setSociete(currentAgence.getSociete());
                    reg.getRubrique().setAuthor(currentUser);
                    idRubrique = (YvsGrhRubriqueBulletin) dao.save1(reg.getRubrique());
                    listRubrique.add(new YvsGrhRubriqueBulletin(idRubrique.getId(), idRubrique.getCode(), idRubrique.getDesignation()));
                } else {
                    idRubrique = (new YvsGrhRubriqueBulletin(r_.getId()));
                }
            } else {
                idRubrique = null;
            }
            //si la règle a un pourcentage
            if (reg.getBasePourcentage() != null) {
                //recherche aussi dans la liste des élément déjà importé
                champ = new String[]{"code", "societe"};
                val = new Object[]{reg.getBasePourcentage().getCode(), currentUser.getAgence().getSociete()};
                YvsGrhElementSalaire ne = (YvsGrhElementSalaire) dao.loadObjectByNameQueries("YvsGrhElementSalaire.findByCode", champ, val);
                if (ne != null) {
                    reg.setBasePourcentage(new YvsGrhElementSalaire(ne.getId()));
                } else {
                    //enregistrer l'élément dans la société
                    reg.getBasePourcentage().setId(null);
                    reg.getBasePourcentage().setAuthor(currentUser);
                    YvsGrhElementSalaire n_ = (YvsGrhElementSalaire) dao.save1(reg.getBasePourcentage());
//                    ne = UtilGrh.buildElementSalaire(n_);
                    reg.setBasePourcentage(n_);
                    listeRegle.add(0, n_);
                }
            }
//                //si la règle a une quantité
            if (reg.getQuantite() != null) {
                //recherche aussi dans la liste des éléments déjà importé
                champ = new String[]{"code", "societe"};
                val = new Object[]{reg.getQuantite().getCode(), currentUser.getAgence().getSociete()};
                YvsGrhElementSalaire ne = (YvsGrhElementSalaire) dao.loadObjectByNameQueries("YvsGrhElementSalaire.findByCode", champ, val);
                if (ne != null) {
                    reg.setQuantite(new YvsGrhElementSalaire(ne.getId()));
                } else {
                    //enregistrer l'élément dans la société
                    reg.getQuantite().setId(null);
                    reg.getQuantite().setAuthor(currentUser);
                    YvsGrhElementSalaire n_ = (YvsGrhElementSalaire) dao.save1(reg.getQuantite());
                    reg.setQuantite(n_);
                    listeRegle.add(0, n_);
                }
            }
            reg.setCategorie(idCat);
            reg.setRubrique(idRubrique);
            reg.setAuthor(currentUser);
            reg = (YvsGrhElementSalaire) dao.save1(reg);
            if (idCat != null) {
                if (idCat.getElementsSalaires() == null) {
                    idCat.setElementsSalaires(new ArrayList<YvsGrhElementSalaire>());
                }
                idCat.getElementsSalaires().add(0, reg);
                listCategorie.set(listCategorie.indexOf(idCat), idCat);
            }
            listeRegle.add(0, reg);
            regleExist = false;
        } else {
            return reg = enE;
        }
        return reg;
    }
    YvsGrhElementSalaire re;

    public void importOne(YvsGrhElementSalaire e, boolean continu) {
        if (continu) {
            if (re != null) {
                dao.update(re);
                succes();
                re = null;
            }
        } else {
            re = importRegleInsociete(e);
            if (regleExist) {
                openDialog("dlgConfirmImport");
            } else {
                succes();
            }
        }
    }

    public void importAll() {
        for (YvsGrhElementSalaire e : listRegleImport) {
            importRegleInsociete(e);
        }
        succes();
    }

    public void removeRToListImport(YvsGrhElementSalaire ee) {
        listRegleImport.remove(ee);
//        listRegleImport.remove(new YvsGrhElementSalaire(ee.getId()));
    }

    private YvsGrhCategorieElement containsCategorie(List<YvsGrhCategorieElement> l, String categorie) {

        for (YvsGrhCategorieElement c : l) {
            if (c.getCodeCategorie().equals(categorie)) {
                return c;
            }
        }
        return null;
    }

    private YvsGrhRubriqueBulletin containsRubrique(List<YvsGrhRubriqueBulletin> l, String rubrique) {
        for (YvsGrhRubriqueBulletin r : l) {
            if (r.getCode().equals(rubrique)) {
                return r;
            }
        }
        return null;
    }

    private YvsGrhElementSalaire findOneElement(List<YvsGrhElementSalaire> l, YvsGrhElementSalaire ne) {
//        Collections.sort(l, new ElementSalaire());
//        for(ElementSalaire e:l){
//            System.err.println("----elt > "+e.getCode());
//        }
//        System.err.println(":::::::::::::::::::::::::::::::::::::::::::::::::::");
//        UtilitaireGenerique ut = new UtilitaireGenerique();
//        int r = ut.triDichontomique(l, "code", ne.getCode().trim());
//        if (r > -1) {
//            return l.get(r);
//        } else {           
//            return null;
//        }        
        for (YvsGrhElementSalaire e : l) {
            if (e.getCode().trim().equals(ne.getCode().trim())) {
                return e;
            }
        }
        return null;
    }

    //vérifie si l'expression est bien formé ou non
    public void verifierExpression(List<YvsGrhElementSalaire> l, YvsGrhElementSalaire ne) {

    }

    public void exludeEltInBaseConge(YvsGrhElementSalaire e, boolean exclu, int line) {
        String rq = "UPDATE yvs_element_salaire SET exclude_in_conge=" + exclu + " WHERE id=?";
        yvs.dao.Options[] param = new yvs.dao.Options[]{new yvs.dao.Options(e.getId(), 1)};
        dao.requeteLibre(rq, param);
        e.setExcludeInConge(exclu);
        listeRegle.set(line, e);
        update("table_elt_exludeConge");
    }

    public void definirRegleConge() {
        if (selectedElement != null) {
            definirRegleConge(currentElement);
            getMessage("La régle congé a été définie avec succès ", FacesMessage.SEVERITY_INFO);
        }
    }

    public void definirRegleConge(YvsGrhElementSalaire e) {
        String rq = "UPDATE yvs_element_salaire SET allocation_conge=" + false + " WHERE societe=?";
        yvs.dao.Options[] param = new yvs.dao.Options[]{new yvs.dao.Options(currentAgence.getSociete().getId(), 1)};
        dao.requeteLibre(rq, param);
        rq = "UPDATE yvs_element_salaire SET allocation_conge=" + true + " WHERE id=?";
        param = new yvs.dao.Options[]{new yvs.dao.Options(e.getId(), 1)};
        dao.requeteLibre(rq, param);
        e.setRegleConge(true);
        listeRegle.set(listeRegle.indexOf(e), e);
        update("table_regleSalaire");
    }

    private void createRegleArrondi() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        Long re = (Long) dao.loadObjectByNameQueries("YvsGrhElementSalaire.findRegleArrondi", champ, val);
        re = (re != null) ? re : 0;
        if (re == 0) {
            //créer la règle arrondi
            YvsGrhElementSalaire el = new YvsGrhElementSalaire();
            el.setCode("Arrondi");
            el.setNom("Arrondi");
            el.setActif(true);
            el.setAllocationConge(false);
            el.setAuthor(currentUser);
            el.setExcludeInConge(true);
            el.setMontant(0.0);
            el.setNumSequence(1000000);
            el.setPoucentagePatronale(0.0);
            el.setPoucentageSalarial(0.0);
            el.setRegleArrondi(true);
            el.setSupp(false);
            el.setVisibleBulletin(true);
            el.setTypeMontant(0);
            if (!listCategorie.isEmpty()) {
                el.setCategorie(listCategorie.get(0));
                dao.save(el);
            }
        }
    }

    public void toogleEltGain(YvsGrhElementSalaire el) {
        el.setRetenue(!el.getRetenue());
        el.setAuthor(currentUser);
        dao.update(el);
        selectedElement = el;
    }

    public void appyRenameVar() {
        if (elements.getCode() != null && elements.getNewCode() != null) {
            if (!elements.getCode().isEmpty() && !elements.getNewCode().isEmpty() && !elements.getCode().equals(elements.getNewCode())) {
                List<YvsGrhElementSalaire> l = dao.loadNameQueries("YvsGrhElementSalaire.findAllFormule", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
                for (YvsGrhElementSalaire el : l) {
                    if (el.getExpressionRegle() != null) {
                        String str = el.getExpressionRegle().replaceAll(elements.getCode(), elements.getNewCode());
                        el.setExpressionRegle(str);
                        dao.update(el);
                    }
                }
                elements.setCode(elements.getNewCode());
                saveNew(true);
            }
        }
    }

    /**
     * Evaluer une règle de salaire
     */
    private YvsGrhOrdreCalculSalaire header;

    public YvsGrhOrdreCalculSalaire getHeader() {
        return header;
    }

    public void setHeader(YvsGrhOrdreCalculSalaire header) {
        this.header = header;
    }

    public void findEmploye() {
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (service != null && employe.getMatricule() != null) {
            employe = service.findOneEmploye(employe.getMatricule());
            if (employe == null) {
                employe = new YvsGrhEmployes();
            }
        } else {
            employe = new YvsGrhEmployes();
        }
    }

    public void evaluateRegle() {
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (selectedElement != null) {
            if (service != null && employe.getMatricule() != null) {
                employe = service.findOneEmploye(employe.getMatricule());
                if ((employe != null) ? employe.getContrat() != null : false) {
                    UtilFormules serviceReg = null;
                    //cherche la période 
                    header = (YvsGrhOrdreCalculSalaire) dao.loadOneByNameQueries("YvsGrhOrdreCalculSalaire.findByContainDate", new String[]{"date", "societe"}, new Object[]{debut, currentAgence.getSociete()});
                    if (header != null) {
                        serviceReg = new UtilFormules(paramGrh, debut, fin, dao, currentAgence.getSociete(), employe.getContrat(), employe, header);
                        resultEvaluation = serviceReg.findVar(new Lexemes(selectedElement.getCode(), false));
                    } else {
                        getErrorMessage("Aucune période de référence n'a été trouvé !");
                    }
                } else {
                    if (employe == null) {
                        getErrorMessage("Aucun employé trouvé !");
                    } else {
                        getErrorMessage("L'employé dtest dois disposé d'un contrat actif !");
                    }
                }
            } else {
                getErrorMessage("Vous devez choisir un employé !");
            }
        } else {
            getErrorMessage("Aucune règle de salaire n'a été selectionné !");

        }
    }

//    public void saveRègleEvalue() {
//        if (header != null && employe != null && selectedElement != null) {
//            YvsGrhDetailBulletin db = (YvsGrhDetailBulletin) dao.loadOneByNameQueries("YvsGrhMontanSalaire.findOneElement", new String[]{"periode", "element"}, new Object[]{header, selectedElement});
//            YvsGrhBulletins bulletin;
//            System.err.println("---- " + db);
//            if (db == null) {
//                //récupère un bulletin de la période
//                bulletin = (YvsGrhBulletins) dao.loadOneByNameQueries("YvsGrhBulletins.findByPeriode_", new String[]{"head", "contrat"}, new Object[]{header, employe});
//                if (bulletin == null) {
//                    getErrorMessage("Cette employé ne dispose pas de bulletin pour la période de référence !");
//                    return;
//                } else {
//                    db=new YvsGrhDetailBulletin();
//                    db.setAuthor(currentUser);
//                    db.set
//                }
//            } else {
//                bulletin = db.getYvsGrhBulletins();
//            }
//
//        }
//    }

    /*recherche une règle de salaire*/
    private long idCategorieF, idRubriqueF;
    private Boolean actifF, visibleF;

    public long getIdCategorieF() {
        return idCategorieF;
    }

    public void setIdCategorieF(long idCategorieF) {
        this.idCategorieF = idCategorieF;
    }

    public long getIdRubriqueF() {
        return idRubriqueF;
    }

    public void setIdRubriqueF(long idRubriqueF) {
        this.idRubriqueF = idRubriqueF;
    }

    public Boolean getActifF() {
        return actifF;
    }

    public void setActifF(Boolean actifF) {
        this.actifF = actifF;
    }

    public Boolean getVisibleF() {
        return visibleF != null ? visibleF : false;
    }

    public void setVisibleF(Boolean visibleF) {
        this.visibleF = visibleF;
    }

    public void findRegleSalaire(String regle) {
        ParametreRequete p = new ParametreRequete(null, "code", "null", "=", "AND");
        if (regle != null) {
            if (!regle.trim().isEmpty()) {
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.code)", "codeE", "%" + regle.trim().toUpperCase() + "%", " LIKE ", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.nom)", "nomE", "%" + regle.trim().toUpperCase() + "%", " LIKE ", "OR"));
            } else {
                p.setObjet(null);
            }
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        loadAllRegle(true, true);
    }

    public void findByExpressionRegle(String expression) {
        ParametreRequete p = new ParametreRequete("UPPER(y.expressionRegle)", "expressionRegle", null, " LIKE ", "AND");
        if ((expression != null) ? !expression.isEmpty() : false) {
            p.setObjet("%" + expression.toUpperCase() + "%");
        }
        paginator.addParam(p);
        loadAllRegle(true, true);
    }

    public void findByCategorie(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.categorie", "categorie", null, "=", "AND");
        if (ev.getNewValue() != null) {
            if ((long) ev.getNewValue() > 0) {
                p.setObjet(new YvsGrhCategorieElement((long) ev.getNewValue()));
            }
        }
        paginator.addParam(p);
        loadAllRegle(true, true);
    }

    public void findByRubrique(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.rubrique", "rubrique", null, "=", "AND");
        if (ev.getNewValue() != null) {
            if ((long) ev.getNewValue() > 0) {
                p.setObjet(new YvsGrhRubriqueBulletin((long) ev.getNewValue()));
            }
        }
        paginator.addParam(p);
        loadAllRegle(true, true);
    }

    public void findByActif(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", null, "=", "AND");
        if (ev.getNewValue() != null) {
            p.setObjet((boolean) ev.getNewValue());
        }
        paginator.addParam(p);
        loadAllRegle(true, true);
    }

    public void findByVisibleB(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.visibleBulletin", "visibleB", null, "=", "AND");
        if (ev.getNewValue() != null) {
            p.setObjet((boolean) ev.getNewValue());
        }
        paginator.addParam(p);
        loadAllRegle(true, true);
    }

    public void fusionner(boolean fusionne) {
        try {
            if (!autoriser("grh_regl_sal_fusion")) {
                openNotAcces();
                return;
            }
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(chaineSelectRegle);
            if (!ids.isEmpty() ? ids.size() > 1 : false) {
                long newValue = listeRegle.get(ids.get(0)).getId();
                if (!fusionne) {
                    String oldValue = "0";
                    for (int i : ids) {
                        if (listeRegle.get(i).getId() != newValue) {
                            oldValue += "," + listeRegle.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_grh_element_salaire", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                listeRegle.remove(new YvsGrhElementSalaire(id));
                            }
                        }
                    }
                    chaineSelectRegle = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = listeRegle.get(idx).getNom();
                    } else {
                        YvsGrhElementSalaire c = (YvsGrhElementSalaire) dao.loadOneByNameQueries("YvsGrhElementSalaire.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneTo = c.getNom();
                        }
                    }
                    YvsGrhElementSalaire c;
                    for (int i : ids) {
                        long oldValue = listeRegle.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(listeRegle.get(i).getNom());
                            }
                        } else {
                            c = (YvsGrhElementSalaire) dao.loadOneByNameQueries("YvsGrhElementSalaire.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesBy.add(c.getNom());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 éléments");
            }
        } catch (NumberFormatException ex) {
            getException(getClass().getSimpleName() + " (fusionner) ", ex);
        }
    }

    public void fusionnerCategorie(boolean fusionne) {
        try {
            if (!autoriser("grh_regl_sal_fusion")) {
                openNotAcces();
                return;
            }
            fusionneCategorieTo = "";
            fusionnesCategorieBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty() ? ids.size() > 1 : false) {
                long newValue = listCategorie.get(ids.get(0)).getId();
                if (!fusionne) {
                    String oldValue = "0";
                    for (int i : ids) {
                        if (listCategorie.get(i).getId() != newValue) {
                            oldValue += "," + listCategorie.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_grh_categorie_element", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                listCategorie.remove(new YvsGrhCategorieElement(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneCategorieTo = listCategorie.get(idx).getDesignation();
                    } else {
                        YvsGrhCategorieElement c = (YvsGrhCategorieElement) dao.loadOneByNameQueries("YvsGrhCategorieElement.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneCategorieTo = c.getDesignation();
                        }
                    }
                    YvsGrhCategorieElement c;
                    for (int i : ids) {
                        long oldValue = listCategorie.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesCategorieBy.add(listCategorie.get(i).getDesignation());
                            }
                        } else {
                            c = (YvsGrhCategorieElement) dao.loadOneByNameQueries("YvsGrhCategorieElement.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesCategorieBy.add(c.getDesignation());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 catégories");
            }
        } catch (NumberFormatException ex) {
            getException(getClass().getSimpleName() + " (fusionnerCategorie) ", ex);
        }
    }
}

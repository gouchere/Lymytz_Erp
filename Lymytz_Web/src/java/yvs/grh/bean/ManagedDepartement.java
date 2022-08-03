/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import yvs.parametrage.poste.Departements;
import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;
import yvs.base.compta.ManagedCentreAnalytique;
import yvs.dao.Options;
import yvs.entity.compta.YvsComptaAffecAnalDepartement;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.grh.param.poste.YvsGrhDepartement;
import yvs.entity.grh.param.poste.YvsGrhPosteDeTravail;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.salaire.YvsGrhHeaderBulletin;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.util.DynamicExport;
import yvs.util.Managed;
import yvs.util.Nodes;
import yvs.util.Util;
import yvs.util.Utilitaire;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class ManagedDepartement extends Managed<Departements, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{departements}")
    private Departements departement;
    private List<Departements> listValue, listAllParent, selectionList;
    private boolean update;
    private String codeSearch;
//    private Departements selectedDepartement;

    private boolean displayButonOption, displayGrille = true, displayButonSave, displayTree;
    private boolean updateParent;
    private String chaineSelectDepartement;

    private List<Employe> employes;

    private String labelBtn = "Création";

    public boolean isDisplayTree() {
        return displayTree;
    }

    public void setDisplayTree(boolean displayTree) {
        this.displayTree = displayTree;
    }

    public ManagedDepartement() {
        listValue = new ArrayList<>();
        listAllParent = new ArrayList<>();
        employes = new ArrayList<>();
        allFields = new ArrayList<>();
        fieldsToImport = new ArrayList<>();
        fields = new DualListModel<>(allFields, fieldsToImport);
    }

    public List<Departements> getSelectionList() {
        return selectionList;
    }

    public void setSelectionList(List<Departements> selectionList) {
        this.selectionList = selectionList;
    }

    public String getLabelBtn() {
        return labelBtn;
    }

    public void setLabelBtn(String labelBtn) {
        this.labelBtn = labelBtn;
    }

    public List<Employe> getEmployes() {
        return employes;
    }

    public void setEmployes(List<Employe> employes) {
        this.employes = employes;
    }

    public String getCodeSearch() {
        return codeSearch;
    }

    public void setCodeSearch(String codeSearch) {
        this.codeSearch = codeSearch;
    }

    public boolean isDisplayButonSave() {
        return displayButonSave;
    }

    public void setDisplayButonSave(boolean displayButonSave) {
        this.displayButonSave = displayButonSave;
    }

    public List<Departements> getListAllParent() {
        return listAllParent;
    }

    public void setListAllParent(List<Departements> listAllParent) {
        this.listAllParent = listAllParent;
    }

    public List<Departements> getListValue() {
        return listValue;
    }

    public void setListValue(List<Departements> listValue) {
        this.listValue = listValue;
    }

    public Departements getDepartement() {
        return departement;
    }

    public void setDepartement(Departements departement) {
        this.departement = departement;
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

    public String getChaineSelectDepartement() {
        return chaineSelectDepartement;
    }

    public void setChaineSelectDepartement(String chaineSelectDepartement) {
        this.chaineSelectDepartement = chaineSelectDepartement;
    }

    public void addParent() {
        if (chaineSelectDepartement != null) {
            String numroLine[] = chaineSelectDepartement.split("-");
            if (numroLine.length == 1) {
                try {
                    int idx = Integer.parseInt(numroLine[0]);
                    trieListe(listValue.get(idx));
                    updateParent = true;
                    update("tab-depart-parent");
                    openDialog("dlgDepartement");
                } catch (NumberFormatException ex) {
                    getErrorMessage("Selection ambigue !!");
                }
            } else {
                getMessage("Votre selection est ambigue !", FacesMessage.SEVERITY_ERROR);
            }
        }
    }

    public void choixParent(SelectEvent ev) {
        Departements dep = (Departements) ev.getObject();
        if (dep != null) {
            //si la selection vise à modifier directement le département parent d'un département sélectionné. pour celà; il faut qu'on soi
            if (updateParent) {
                if (chaineSelectDepartement != null) {
                    String numroLine[] = chaineSelectDepartement.split("-");
                    if (numroLine.length == 1) {
                        try {
                            int idx = Integer.parseInt(numroLine[0]);
                            YvsGrhDepartement d = UtilGrh.buildBeanDepartement(listValue.get(idx));
                            d.setSociete(currentUser.getAgence().getSociete());
                            d.setDepartementParent(new YvsGrhDepartement(dep.getId()));
                            dao.update(d);
                            succes();
                        } catch (NumberFormatException ex) {
                            Logger.getLogger(ManagedDepartement.class.getName()).log(Level.SEVERE, null, ex);
                            getMessage("Votre selection est ambigue !", FacesMessage.SEVERITY_ERROR);
                        }
                    }
                }
            } else {
                //choisir un département parent
                departement.setIdParent(dep.getId());
                update("depart-parent");
            }
            loadAll();
            update("tadDepartement");
            displayButonOption = false;
//            update("form-dep00");    
        }
    }

//    public void toogleView() {
//        displayGrille = !displayGrille;
//        if (displayGrille) {
//            displayButonSave = false;
//            displayButonOption = (listSelection != null) ? !listSelection.isEmpty() : false;
//            labelBtn = "Création";
//        } else {
//            displayButonOption = false;
//            displayButonSave = true;
//            labelBtn = "Liste";
//        }
//        resetFiche();
//        updateParent = false;
//        update("tabServices");
//        update("main-departement");
//        update("departement-gridButon");
//        codeSearch = null;
//    }
    public void selectedLine(SelectEvent dep) {
        populateView((Departements) dep.getObject());
        update = true;
        execute("collapseForm('departement')");
        selectionList.clear();
        selectionList.add((Departements) dep.getObject());

    }

//    public void choixDepartement(ValueChangeEvent ev) {
//        int id = (int) ev.getNewValue();
//        if (id == -1) {
//            //ouvrir une boite de dialogue de choix d'un département
//            openDialog("dlgDepartement");
//        }
//    }
    @Override
    public boolean controleFiche(Departements bean) {
        //contrôle qu'il y ai pas deux départements de même intitulé
        if (departement.getCodeDepartement() != null) {
            if (!update) {
                champ = new String[]{"societe", "code"};
                val = new Object[]{currentUser.getAgence().getSociete(), departement.getCodeDepartement()};
                Long dep = (Long) dao.loadObjectByNameQueries("YvsGrhDepartement.countD", champ, val);
                if (dep != null) {
                    if (dep != 0) {
                        getMessage("Un département de même nom a déjà été trouvé", FacesMessage.SEVERITY_ERROR);
                    }
                    return dep == 0;//si le résultat est égale à zéro, c'est qu'il y a pas de encore de département avec le même intitulé
                }
            }
        } else {
            getMessage("Vous devez saisir une désignation pour ce département", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (departement.getResponsable().getId() == -1) {
            getErrorMessage("Valeur érroné du code responsable !");
            return false;
        }
        return true;
    }

    //retrouve et élimine tous les fils de dep
    private void trieListe(Departements dep) {
        listAllParent.remove(dep);
        for (Departements d : listValue) {
            if (d.getIdParent() == dep.getId()) {
                listAllParent.remove(d);
                trieListe(d);
            }
        }
    }

    @Override
    public void updateBean() {
        YvsGrhDepartement dep = UtilGrh.buildBeanDepartement(departement);
        dep.setSociete(currentAgence.getSociete());
        dep.setAuthor(currentUser);
        dao.update(dep);
        update = true;
    }

    @Override
    public Departements recopieView() {
        Departements dep = new Departements();
        cloneObject(dep, departement);
        return dep;
    }

    @Override
    public void populateView(Departements bean) {
        cloneObject(departement, bean);
        departement.setSectionsAnalytiques(
                dao.loadNameQueries("YvsComptaAffecAnalDepartement.findByDepartement", new String[]{"departement"}, new Object[]{new YvsGrhDepartement(departement.getId())}));
        update("tableSectionAnal");
    }
    int row = 0;

    @Override
    public void loadOnView(SelectEvent ev) {
        Departements s = (Departements) ev.getObject();
        populateView(s);
        update = true;
    }

    @Override
    public void loadAll() {
        List<YvsGrhDepartement> l = dao.loadNameQueries("YvsGrhDepartement.findAll", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
        if (l != null) {
            listAllParent.clear();
            listValue = UtilGrh.buildBeanDepartement(l);
            createTree(buildNode(l));
            listAllParent.addAll(listValue);
        }
    }

    public void loadAllActif() {
        List<YvsGrhDepartement> l = dao.loadNameQueries("YvsGrhDepartement.findBySociete", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
        if (l != null) {
            listValue = UtilGrh.buildBeanDepartement(l);
        }
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(departement)) {
            if (!update) {
                YvsGrhDepartement dep = UtilGrh.buildBeanDepartement(departement);
                dep.setId(null);
                dep.setSociete(currentAgence.getSociete());
                dep.setAuthor(currentUser);
                dao.save(dep);
            } else {
                updateBean();
            }
            loadAll();
            update = true;
            succes();
            actionOpenOrResetAfter(this);
            update("tabServices");
        }
        return true;
    }

    public void remove() {
        System.err.println("list deleted = " + selectionList.size());
        if (!selectionList.isEmpty()) {
            try {
                for (Departements dep : selectionList) {
                    dao.delete(new YvsGrhDepartement(dep.getId(), currentUser));
                    listValue.remove(dep);
                    listAllParent.remove(dep);
                }
                succes();
                resetFiche();
                update("tadDepartement");
            } catch (Exception ex) {
                getMessage("erreur de  suppression", FacesMessage.SEVERITY_ERROR);
                Logger.getLogger(ManagedDepartement.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            getErrorMessage("Aucune ligne n'a été selectionné !");
        }

    }

    @Override
    public void resetFiche() {
        listAllParent.clear();
        listAllParent.addAll(listValue);
        resetFiche(departement);
        departement.setResponsable(new EmployePartial());
        update = false;
        updateParent = false;
//        update("main-departement");
//        update("tab-depart-parent");
        update("head");
        execute("collapseForm('departement')");
    }

    public void searchDepartement() {
        listValue.clear();
        if (codeSearch != null) {
            champ = new String[]{"societe", "codeDepartement"};
            val = new Object[]{currentUser.getAgence().getSociete(), "%" + codeSearch + "%"};
            List<YvsGrhDepartement> l = dao.loadNameQueries("YvsGrhDepartement.findByCode", champ, val);
            if (l != null) {
                listValue.clear();
                listAllParent.clear();
                listValue = UtilGrh.buildBeanDepartement(l);
                listAllParent.addAll(listValue);
            }
        } else {
            loadAll();
        }
        update("tadDepartement");
//        if (!displayGrille) {
//            displayGrille = true;
//            displayButonSave = false;
//            displayButonOption = (listSelection != null) ? !listSelection.isEmpty() : false;
//            update("main-departement");
//        }
    }

    /**
     * ***AJOUTER UN RESPONSABLE*****
     */
    public void loadEMployes() {
        champ = new String[]{"agence"};
        val = new Object[]{currentAgence};
        employes.clear();
        employes = UtilGrh.buildListEmployeBean(dao.loadNameQueries("YvsGrhEmployes.findAll", champ, val, 0, 20));
    }
//
//    public void choiEmploye(ValueChangeEvent ev) {
//        if (ev.getNewValue() != null) {
//            long id = (long) ev.getNewValue();
//            if (id == -1) {
//                openDialog("dlgEmploye");
//            }
//        }
//    }

    public void selectEmploye(SelectEvent ev) {
        if (ev != null) {
            YvsGrhEmployes e = (YvsGrhEmployes) ev.getObject();
            selectOneEmploye(e);
        }
    }

    public void selectOneEmploye(YvsGrhEmployes ev) {
        departement.setResponsable(UtilGrh.buildBeanEmployeToEmployePartial(ev));
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (service != null) {
            service.getListEmployes().remove(ev);
            service.getListEmployes().add(0, ev);
        }
        update("depart-emp");
        closeDialog("dlgEmploye");
    }

    /**
     * Afficher l'arboresence
     */
    private TreeNode root;

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

//    public void createTree() {
//        champ = new String[]{"societe"};
//        val = new Object[]{currentUser.getAgence().getSociete()};
//        List<YvsGrhDepartement> l = dao.loadNameQueries("YvsGrhDepartement.findAll", champ, val);
//        createTree(buildNode(l));s
//    }
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

    private List<Nodes> buildNode(List<YvsGrhDepartement> l) {
        List<Nodes> r = new ArrayList<>();
        for (YvsGrhDepartement d : l) {
            Nodes n = new Nodes(d.getId(), new Departements(d.getId(), d.getIntitule()));
            if (d.getDepartementParent() != null) {
                n.setIdParent(d.getDepartementParent().getId());
            } else {
                n.setIdParent(0);
            }
            r.add(n);
        }
        return r;
    }

    public void toogleVisibleOnLP(Nodes node) {
        if (node != null) {
            int idx = listValue.indexOf(new Departements((int) node.getId(), ""));
            if (idx >= 0) {
                YvsGrhDepartement dep = (YvsGrhDepartement) dao.loadOneByNameQueries("YvsGrhDepartement.findById", new String[]{"id"}, new Object[]{(int) node.getId()});
                dep.setVisibleOnLivrePaie(!dep.getVisibleOnLivrePaie());
                dao.update(dep);
                listValue.get(idx).setVisibleOnLp(dep.getVisibleOnLivrePaie());
            }
        }
    }

    public void toogleVisibleOnLP_(Departements node) {
        if (node != null) {
            int idx = listValue.indexOf(node);
            if (idx >= 0) {
                YvsGrhDepartement dep = (YvsGrhDepartement) dao.loadOneByNameQueries("YvsGrhDepartement.findById", new String[]{"id"}, new Object[]{(int) node.getId()});
                dep.setVisibleOnLivrePaie(!dep.getVisibleOnLivrePaie());
                dao.update(dep);
                listValue.get(idx).setVisibleOnLp(dep.getVisibleOnLivrePaie());
            }
        }
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * **********************************************************************************
     */
    /**
     * *************LES FORMATS DE DONNEES
     * IMPORT/EXPORT**********************************
     */
    private List<String> allFields, fieldsToImport;
    private DualListModel<String> fields;
    private FileUpload file;

    public DualListModel<String> getFields() {
        return fields;
    }

    public void setFields(DualListModel<String> fields) {
        this.fields = fields;
    }

    public List<String> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<String> allFields) {
        this.allFields = allFields;
    }

    public void setFieldsToImport(List<String> fieldsToImport) {
        this.fieldsToImport = fieldsToImport;
    }

    public List<String> getFieldsToImport() {
        return fieldsToImport;
    }

    public FileUpload getFile() {
        return file;
    }

    public void setFile(FileUpload file) {
        this.file = file;
    }

    DynamicExport<YvsGrhDepartement> toExport = new DynamicExport<>(YvsGrhDepartement.class);

    public void loadFieldsToExport() {
        allFields = toExport.giveFieldClass();
        fields = new DualListModel<>(allFields, fieldsToImport);
        update("pickList_departements");
        System.err.println(buildRequete_(allFields));
    }

    private String buildRequete_(List<String> l) {
        String rq;
        rq = "SELECT " + decompose(l)
                + " FROM " + toExport.getClasse().getSimpleName() + " y";
        return rq;
    }

    private String decompose(List<String> l) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String r : l) {
            if (i == l.size() - 1) {
                sb.append("y.").append(r);
            } else {
                sb.append("y.").append(r).append(", ");
            }
            i++;
        }
        return sb.toString();
    }

    public void executeRequeteExport() {
        try {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            File file = new File(ext.getRealPath("/WEB-INF/report/temp.txt"));
            FileWriter fw = new FileWriter(file);
            String rq = buildRequete_(fields.getTarget());
            List<Object[]> result = dao.loadEntity(rq);
            if (result != null) {
                for (Object[] line : result) {
                    fw.write(buildLineFile(line));
                }
                fw.flush();
                fw.close();
            }
            byte[] bytes = Utilitaire.readFile(file);
            FacesContext faces = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
            response.setContentType("application/csv");
            response.addHeader("Content-disposition",
                    "attachment;filename=departement.csv");
            response.setContentLength(bytes.length);
            try {
                response.getOutputStream().write(bytes);
                response.getOutputStream().flush();
            } catch (IOException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            getErrorMessage("Paramètre d'exportation incorrecte !");
            Logger.getLogger(ManagedDepartement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String buildLineFile(Object[] tab) {
        StringBuilder sb = new StringBuilder();
        for (Object o : tab) {
            sb.append(o).append(";");
        }
        sb.append("\n");
        return sb.toString();
    }
    List<String> contentFile;

    public void handleFileUpload(FileUploadEvent ev) {
        if (ev != null) {
            try {
                contentFile = Util.inputStreamToStrings(ev.getFile().getInputstream());
                fields.setSource(loadFieldsToImport());
                getInfoMessage("Importation réussi !", contentFile.size() + " Lignes trouvées ");
            } catch (IOException ex) {
                Logger.getLogger(ManagedDepartement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<String> loadFieldsToImport() {
        List<String> re = new ArrayList<>();
        Field[] champs = toExport.getClasse().getDeclaredFields();
        for (Field f : champs) {
            for (Annotation annotation : f.getAnnotations()) {
                if (annotation instanceof Column) {
                    Column monAnnotation = (Column) annotation;
                    re.add(monAnnotation.name());
                }
            }
        }
        return re;
    }
    String chaineParam = "";

    private Options[] buildDataParam(String str) {
        Options[] options = null;
        if (str != null) {
            int i = 0;
            String[] l = str.split(";");
            StringBuilder sb = new StringBuilder();
            options = new Options[l.length + 1];
            Options op;
            for (String r : l) {
                if ("".equals(r)) {
                    r = null;
                }
                op = new Options(str, i + 1);
                if (i == l.length - 1) {
                    sb.append("?");
                } else {
                    sb.append("?").append(", ");
                }
                options[i] = op;
                i++;
            }
            options[l.length] = new Options(currentUser.getAgence().getSociete().getId(), l.length + 1);
            sb.append(", ?");
            chaineParam = sb.toString();
        }
        return options;
    }

    public void executeImport() {
        try {
            String rq;
            for (String str : contentFile) {
                Options[] op = buildDataParam(str);
                rq = "INSERT INTO yvs_grh_departement ( " + decomposeHeader(fields.getTarget()) + ", societe ) VALUES ( " + chaineParam + " )";
                dao.requeteLibre(rq, op);
            }
            getInfoMessage("Importation tterminée avec succès !");
        } catch (Exception ex) {
            getErrorMessage("Impossible d'importer les données erreur dans le format de données !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    private String decomposeHeader(List<String> l) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String r : l) {
            if (i == l.size() - 1) {
                sb.append(r);
            } else {
                sb.append(r).append(", ");
            }
            i++;
        }
        return sb.toString();
    }
    //charge uniquement les sous départements du département d'un utilisateur connecté

    public void loadAllDepartementByDroit() {
        if (autoriser("point_validFicheAllScte") || autoriser("point_validFicheAgence")) {
            //Charge tous les départements
            List<YvsGrhDepartement> l = dao.loadNameQueries("YvsGrhDepartement.findAll", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
            if (l != null) {
                listValue = UtilGrh.buildBeanDepartement(l);
            }
        } else {
            //charge uniquement les sous départements de mon département
            listIdSubDepartement.clear();
            if (currentUser.getUsers().getEmploye() != null) {
                if (currentUser.getUsers().getEmploye().getPosteActif() != null) {
                    giveAllSupDepartement(currentUser.getUsers().getEmploye().getPosteActif().getDepartement());
                    List<YvsGrhDepartement> l = dao.loadNameQueries("YvsGrhDepartement.findByIdIn", new String[]{"ids"}, new Object[]{listIdSubDepartement});
                    if (l != null) {
                        listValue = UtilGrh.buildBeanDepartement(l);
                    }
                }
            }
        }

    }

    public void openListPlanAnal() {
        if (departement.getId() > 0) {
            ManagedCentreAnalytique service = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
            if (service != null) {
                service.loadAllByNamedQuery();
                int idx;
                for (YvsComptaAffecAnalDepartement ca : departement.getSectionsAnalytiques()) {
                    idx = service.getCentres().indexOf(ca.getCentre());
                    if (idx >= 0) {
                        service.getCentres().get(idx).setIdAffectation(ca.getId());
                        service.getCentres().get(idx).setCoeficient(ca.getCoeficient());
                        service.getCentres().get(idx).setDateSave_(ca.getDateSave());
                    }
                }
                update("data_centre_analytique_dep");
                openDialog("dlgAffec");
            }
        } else {
            getErrorMessage("Veuillez selectionner un département !");
        }
    }

    public void saveNewSectionEmploye(YvsComptaCentreAnalytique ca, boolean all) {
        if (departement.getId() > 0) {
            YvsComptaAffecAnalDepartement ae;
            ae = new YvsComptaAffecAnalDepartement();
            ae.setAuthor(currentUser);
            ae.setCentre(ca);
            ae.setCoeficient(ca.getCoeficient());
            ae.setDateSave(ca.getDateSave_());
            ae.setDateUpdate(new Date());
            ae.setDepartement(new YvsGrhDepartement(departement.getId()));
            if (ca.getIdAffectation() <= 0) {
                ae = (YvsComptaAffecAnalDepartement) dao.save1(ae);
                ca.setIdAffectation(ae.getId());
                departement.getSectionsAnalytiques().add(0, ae);
            } else {
                ae.setDateUpdate(new Date());
                ae.setId(ca.getIdAffectation());
                dao.update(ae);
            }
            if (!all) {
                succes();
                update("data_centre_analytique_dep");
                update("tableSectionAnal");
            }
        } else {
            if (!all) {
                getErrorMessage("Aucun département n'a été selectionné !");
            }
        }
    }

    public void saveNewSectionsEmployes() {
        ManagedCentreAnalytique service = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
        if (service != null && departement.getId() > 0) {
            for (YvsComptaCentreAnalytique ca : service.getCentres()) {
                if (ca.getIdAffectation() > 0 || ca.getCoeficient() > 0) {
                    saveNewSectionEmploye(ca, true);
                }
            }
            succes();
            update("data_centre_analytique_dep");
            update("tableSectionAnal");
        } else {
            if (departement.getId() <= 0) {
                getErrorMessage("Aucun employé n'a été selectionné.", "veuiller en selectionner un !");
            }
        }
    }

    public void removeLineAffecAnal(YvsComptaAffecAnalDepartement ef) {
        try {
            ef.setAuthor(currentUser);
            ef.setDateUpdate(new Date());
            dao.delete(ef);
            departement.getSectionsAnalytiques().remove(ef);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette ligne !");
            getException("Lymytz Error...", ex);
        }
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < listValue.size() && getOffset() >= 0) {
            populateView(listValue.get(getOffset()));
        } else {
            setOffset(0);
        }
        update("head");
    }

    public void getSelectDepart(Departements d) {
        departement = d;
    }

    public void deleteDepartement() {
        try {
            selectEmploye(null);
            if (departement != null ? departement.getId() > 0 : false) {
                YvsGrhDepartement dep = UtilGrh.buildBeanDepartement(departement);
                List<YvsGrhDepartement> departs = dao.loadNameQueries("YvsGrhDepartement.findByParent", new String[]{"parent"}, new Object[]{dep});
                if (departs != null ? !departs.isEmpty() : false) {
                    for (YvsGrhDepartement d : departs) {
                        d.setDepartementParent(null);
                        dao.update(d);
                    }
                }

                List<YvsGrhPosteDeTravail> postes = dao.loadNameQueries("YvsPosteDeTravail.findByDepartement", new String[]{"departement"}, new Object[]{dep});
                if (postes != null ? !postes.isEmpty() : false) {
                    for (YvsGrhPosteDeTravail p : postes) {
                        List<YvsGrhHeaderBulletin> header = dao.loadNameQueries("YvsGrhHeaderBulletin.findByPostes", new String[]{"poste"}, new Object[]{p});
                        if (header != null ? !header.isEmpty() : false) {
                            for (YvsGrhHeaderBulletin h : header) {

                                dao.delete(h);
                            }
                        }

                        List<YvsGrhPosteDeTravail> list_sup = dao.loadNameQueries("YvsPosteDeTravail.findBySuperieur", new String[]{"superieur"}, new Object[]{p});
                        if (list_sup != null ? !list_sup.isEmpty() : false) {
                            for (YvsGrhPosteDeTravail pp : list_sup) {
                                pp.setPosteEquivalent(null);
                                pp.setPosteSuperieur(null);
                                dao.update(pp);
                            }
                        }
                        dao.delete(p);
                    }
                }

                dao.delete(dep);
                listValue.remove(departement);
                succes();
                update("tadDepartement");
            }
        } catch (Exception e) {
            getErrorMessage("Impossible d'effectuer cette opération");
            Logger.getLogger(ManagedDepartement.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void updateDepartement(Departements depart) {
        try {
            if (depart != null ? depart.getId() > 0 : false) {
                YvsGrhDepartement dep = UtilGrh.buildBeanDepartement(depart);
                dep.setActif(!dep.getActif());
                dao.update(dep);
                depart = UtilGrh.buildBeanDepartement(dep);
                int index = listValue.indexOf(depart);
                if (index > -1) {
                    listValue.set(index, depart);
                }

                succes();
                update("tadDepartement");
            }
        } catch (Exception e) {
            getErrorMessage("Impossible d'effectuer cette opération");
            Logger.getLogger(ManagedDepartement.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}

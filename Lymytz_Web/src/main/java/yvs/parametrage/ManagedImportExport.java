/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.NoResultException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import yvs.commercial.stock.ManagedInventaire;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.entity.produits.YvsBaseArticles;
import yvs.init.Initialisation;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseParametre;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.compta.YvsBaseNatureCompte;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.grh.param.YvsGrhCategorieProfessionelle;
import yvs.entity.grh.param.YvsGrhConventionCollective;
import yvs.entity.grh.param.YvsGrhEchelons;
import yvs.entity.grh.param.poste.YvsGrhDepartement;
import yvs.entity.grh.param.poste.YvsGrhPosteDeTravail;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsTypeContrat;
import yvs.entity.grh.salaire.YvsGrhElementSalaire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsGrhSecteurs;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.etats.Dashboards;
import yvs.etats.commercial.InventairePreparatoire;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedImportExport extends Managed<Serializable, Serializable> implements Serializable {

    private String objetExport, objetImport = "article";
    private File file;
    //Objet Contenu doc stock
    private long depotPrint;
    private Date datePrint = new Date(), dateInventaire = new Date();
    private Dashboards inventaire = new Dashboards();
    private List<YvsComContenuDocStock> listContenus, listImportContenus;
    //Objet Comptes
    private List<YvsBasePlanComptable> listComptes, listImportComptes;
    //Objet Article
    private List<YvsBaseConditionnement> listArticles, listImportArticles;
    private boolean codeBarre = false, reference = true, designation = true, description = false, puv = true, pua = true, puvMin = false, remise = false,
            masseNet = false, unitMasse = false, conditionnement = false, modeConso = false, methoVal = false, famille = false;
    //Objet Regle de salaire
    private List<YvsGrhElementSalaire> listElementSalaire;
    private boolean nom = true, code = true, categorie = true, type_montant = true, montant = true, expression_regle = true, visible_bulletin = true,
            base_pourcentage = true, description_element = true, retenue = true, poucentage_patronale = true, poucentage_salarial = true, quantite = true;
    //Employes 
    private long agencePrint;
    private List<YvsGrhEmployes> listEmployes, listImportEmployes;
    private List<YvsGrhDepartement> listDepartements, listImportDepartements;
    private List<YvsGrhPosteDeTravail> listPostes, listImportPostes;
    private List<YvsGrhContratEmps> listContrats, listImportContrats;
    private List<YvsGrhConventionCollective> listConventions, listImportConventions;
    private List<YvsComDocVentes> listDocVentes;
    YvsBaseParametre currentParam = null;

    private List<String> colonnes;
    private int numEtat;
    private String etat;

    public ManagedImportExport() {
        listArticles = new ArrayList<>();
        listComptes = new ArrayList<>();
        listContenus = new ArrayList<>();
        listImportArticles = new ArrayList<>();
        listImportComptes = new ArrayList<>();
        listImportContenus = new ArrayList<>();
        listElementSalaire = new ArrayList<>();
        listEmployes = new ArrayList<>();
        listDepartements = new ArrayList<>();
        listPostes = new ArrayList<>();
        listContrats = new ArrayList<>();
        listConventions = new ArrayList<>();
        listImportEmployes = new ArrayList<>();
        listImportDepartements = new ArrayList<>();
        listImportPostes = new ArrayList<>();
        listImportContrats = new ArrayList<>();
        listImportConventions = new ArrayList<>();
        colonnes = new ArrayList<>();
    }

    public List<String> getColonnes() {
        return colonnes;
    }

    public void setColonnes(List<String> colonnes) {
        this.colonnes = colonnes;
    }

    public int getNumEtat() {
        return numEtat;
    }

    public void setNumEtat(int numEtat) {
        this.numEtat = numEtat;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public List<YvsGrhConventionCollective> getListConventions() {
        return listConventions;
    }

    public void setListConventions(List<YvsGrhConventionCollective> listConventions) {
        this.listConventions = listConventions;
    }

    public List<YvsGrhConventionCollective> getListImportConventions() {
        return listImportConventions;
    }

    public void setListImportConventions(List<YvsGrhConventionCollective> listImportConventions) {
        this.listImportConventions = listImportConventions;
    }

    public Date getDateInventaire() {
        return dateInventaire;
    }

    public void setDateInventaire(Date dateInventaire) {
        this.dateInventaire = dateInventaire;
    }

    public List<YvsGrhEmployes> getListImportEmployes() {
        return listImportEmployes;
    }

    public void setListImportEmployes(List<YvsGrhEmployes> listImportEmployes) {
        this.listImportEmployes = listImportEmployes;
    }

    public List<YvsGrhDepartement> getListImportDepartements() {
        return listImportDepartements;
    }

    public void setListImportDepartements(List<YvsGrhDepartement> listImportDepartements) {
        this.listImportDepartements = listImportDepartements;
    }

    public List<YvsGrhPosteDeTravail> getListImportPostes() {
        return listImportPostes;
    }

    public void setListImportPostes(List<YvsGrhPosteDeTravail> listImportPostes) {
        this.listImportPostes = listImportPostes;
    }

    public List<YvsGrhContratEmps> getListImportContrats() {
        return listImportContrats;
    }

    public void setListImportContrats(List<YvsGrhContratEmps> listImportContrats) {
        this.listImportContrats = listImportContrats;
    }

    public YvsBaseParametre getCurrentParam() {
        return currentParam;
    }

    public void setCurrentParam(YvsBaseParametre currentParam) {
        this.currentParam = currentParam;
    }

    public long getAgencePrint() {
        return agencePrint;
    }

    public void setAgencePrint(long agencePrint) {
        this.agencePrint = agencePrint;
    }

    public List<YvsGrhDepartement> getListDepartements() {
        return listDepartements;
    }

    public void setListDepartements(List<YvsGrhDepartement> listDepartements) {
        this.listDepartements = listDepartements;
    }

    public List<YvsGrhPosteDeTravail> getListPostes() {
        return listPostes;
    }

    public void setListPostes(List<YvsGrhPosteDeTravail> listPostes) {
        this.listPostes = listPostes;
    }

    public List<YvsGrhContratEmps> getListContrats() {
        return listContrats;
    }

    public void setListContrats(List<YvsGrhContratEmps> listContrats) {
        this.listContrats = listContrats;
    }

    public List<YvsGrhEmployes> getListEmployes() {
        return listEmployes;
    }

    public void setListEmployes(List<YvsGrhEmployes> listEmployes) {
        this.listEmployes = listEmployes;
    }

    public boolean isFamille() {
        return famille;
    }

    public void setFamille(boolean famille) {
        this.famille = famille;
    }

    public Date getDatePrint() {
        return datePrint;
    }

    public void setDatePrint(Date datePrint) {
        this.datePrint = datePrint;
    }

    public Dashboards getInventaire() {
        return inventaire;
    }

    public void setInventaire(Dashboards inventaire) {
        this.inventaire = inventaire;
    }

    public long getDepotPrint() {
        return depotPrint;
    }

    public void setDepotPrint(long depotPrint) {
        this.depotPrint = depotPrint;
    }

    public List<YvsComContenuDocStock> getListContenus() {
        return listContenus;
    }

    public void setListContenus(List<YvsComContenuDocStock> listContenus) {
        this.listContenus = listContenus;
    }

    public List<YvsComContenuDocStock> getListImportContenus() {
        return listImportContenus;
    }

    public void setListImportContenus(List<YvsComContenuDocStock> listImportContenus) {
        this.listImportContenus = listImportContenus;
    }

    public List<YvsBasePlanComptable> getListComptes() {
        return listComptes;
    }

    public void setListComptes(List<YvsBasePlanComptable> listComptes) {
        this.listComptes = listComptes;
    }

    public List<YvsBasePlanComptable> getListImportComptes() {
        return listImportComptes;
    }

    public void setListImportComptes(List<YvsBasePlanComptable> listImportComptes) {
        this.listImportComptes = listImportComptes;
    }

    public boolean isNom() {
        return nom;
    }

    public void setNom(boolean nom) {
        this.nom = nom;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isCode() {
        return code;
    }

    public void setCode(boolean code) {
        this.code = code;
    }

    public boolean isCategorie() {
        return categorie;
    }

    public void setCategorie(boolean categorie) {
        this.categorie = categorie;
    }

    public boolean isType_montant() {
        return type_montant;
    }

    public void setType_montant(boolean type_montant) {
        this.type_montant = type_montant;
    }

    public boolean isMontant() {
        return montant;
    }

    public void setMontant(boolean montant) {
        this.montant = montant;
    }

    public boolean isExpression_regle() {
        return expression_regle;
    }

    public void setExpression_regle(boolean expression_regle) {
        this.expression_regle = expression_regle;
    }

    public boolean isVisible_bulletin() {
        return visible_bulletin;
    }

    public void setVisible_bulletin(boolean visible_bulletin) {
        this.visible_bulletin = visible_bulletin;
    }

    public boolean isBase_pourcentage() {
        return base_pourcentage;
    }

    public void setBase_pourcentage(boolean base_pourcentage) {
        this.base_pourcentage = base_pourcentage;
    }

    public boolean isDescription_element() {
        return description_element;
    }

    public void setDescription_element(boolean description_element) {
        this.description_element = description_element;
    }

    public boolean isRetenue() {
        return retenue;
    }

    public void setRetenue(boolean retenue) {
        this.retenue = retenue;
    }

    public boolean isPoucentage_patronale() {
        return poucentage_patronale;
    }

    public void setPoucentage_patronale(boolean poucentage_patronale) {
        this.poucentage_patronale = poucentage_patronale;
    }

    public boolean isPoucentage_salarial() {
        return poucentage_salarial;
    }

    public void setPoucentage_salarial(boolean poucentage_salarial) {
        this.poucentage_salarial = poucentage_salarial;
    }

    public boolean isQuantite() {
        return quantite;
    }

    public void setQuantite(boolean quantite) {
        this.quantite = quantite;
    }

    public String getObjetImport() {
        return objetImport;
    }

    public void setObjetImport(String objetImport) {
        this.objetImport = objetImport;
    }

    public List<YvsBaseConditionnement> getListImportArticles() {
        return listImportArticles;
    }

    public void setListImportArticles(List<YvsBaseConditionnement> listImportArticles) {
        this.listImportArticles = listImportArticles;
    }

    public boolean isCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(boolean codeBarre) {
        this.codeBarre = codeBarre;
    }

    public boolean isReference() {
        return reference;
    }

    public void setReference(boolean reference) {
        this.reference = reference;
    }

    public boolean isDesignation() {
        return designation;
    }

    public void setDesignation(boolean designation) {
        this.designation = designation;
    }

    public boolean isDescription() {
        return description;
    }

    public void setDescription(boolean description) {
        this.description = description;
    }

    public boolean isPuv() {
        return puv;
    }

    public void setPuv(boolean puv) {
        this.puv = puv;
    }

    public boolean isPua() {
        return pua;
    }

    public void setPua(boolean pua) {
        this.pua = pua;
    }

    public boolean isPuvMin() {
        return puvMin;
    }

    public void setPuvMin(boolean puvMin) {
        this.puvMin = puvMin;
    }

    public boolean isRemise() {
        return remise;
    }

    public void setRemise(boolean remise) {
        this.remise = remise;
    }

    public boolean isMasseNet() {
        return masseNet;
    }

    public void setMasseNet(boolean masseNet) {
        this.masseNet = masseNet;
    }

    public boolean isUnitMasse() {
        return unitMasse;
    }

    public void setUnitMasse(boolean unitMasse) {
        this.unitMasse = unitMasse;
    }

    public boolean isConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(boolean conditionnement) {
        this.conditionnement = conditionnement;
    }

    public boolean isModeConso() {
        return modeConso;
    }

    public void setModeConso(boolean modeConso) {
        this.modeConso = modeConso;
    }

    public boolean isMethoVal() {
        return methoVal;
    }

    public void setMethoVal(boolean methoVal) {
        this.methoVal = methoVal;
    }

    public DaoInterfaceLocal getDao() {
        return dao;
    }

    public void setDao(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public String getObjetExport() {
        return objetExport;
    }

    public void setObjetExport(String objetExport) {
        this.objetExport = objetExport;
    }

    public void setListElementSalaire(List<YvsGrhElementSalaire> listElementSalaire) {
        this.listElementSalaire = listElementSalaire;
    }

    public List<YvsGrhElementSalaire> getListElementSalaire() {
        return listElementSalaire;
    }

    public List<YvsBaseConditionnement> getListArticles() {
        return listArticles;
    }

    public void setListArticles(List<YvsBaseConditionnement> listArticles) {
        this.listArticles = listArticles;
    }

    public void loadAllArticle() {
        listArticles = dao.loadNameQueries("YvsBaseConditionnement.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllComptes() {
        listComptes = dao.loadNameQueries("YvsBasePlanComptable.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllElementSalaire() {
        listElementSalaire = dao.loadNameQueries("YvsGrhElementSalaire.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllEmployeByAgence() {
        listEmployes = dao.loadNameQueries("YvsGrhEmployes.findByAgence", new String[]{"agence"}, new Object[]{new YvsAgences(agencePrint)});
    }

    public void loadAllEmploye() {
        listEmployes = dao.loadNameQueries("YvsGrhEmployes.findActifs", new String[]{"societe", "actif"}, new Object[]{currentAgence.getSociete(), true});
    }

    public void loadAllContrat() {
        listContrats = dao.loadNameQueries("YvsGrhContratEmps.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllContratByAgence() {
        listContrats = dao.loadNameQueries("YvsGrhContratEmps.findToExportByAgence", new String[]{"agence"}, new Object[]{new YvsAgences(agencePrint)});
    }

    public void loadAllDepartement() {
        listDepartements = dao.loadNameQueries("YvsGrhDepartement.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllPoste() {
        listPostes = dao.loadNameQueries("YvsPosteDeTravail.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllConvention() {
        listConventions = dao.loadNameQueries("YvsConventionCollective.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllFactureVente() {
        listDocVentes = dao.loadNameQueries("YvsComDocVentes.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllInventaire(boolean calculer) {
        if (depotPrint > 0) {
            if (calculer) {
                inventaire.returnInventaire(currentAgence.getSociete().getId(), 0, depotPrint, 0, "", 0, datePrint, true, "", "V", "", 0, 0, false, dao);
            } else {
                Options[] param = new Options[]{new Options(depotPrint, 1)};
                String query = "SELECT DISTINCT ad.depot, a.id, a.ref_art, a.designation, f.designation, c.id, u.reference, COALESCE(c.prix, 0), COALESCE(c.prix_achat, 0) FROM yvs_base_conditionnement c "
                        + "INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id "
                        + "INNER JOIN yvs_base_article_depot ad ON ad.article = a.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id "
                        + "WHERE ad.depot = ? ORDER BY ad.depot, f.designation, a.ref_art";
                List<Object> data = dao.loadListBySqlQuery(query, param);
                try {
                    inventaire.getInventaires().clear();
                    Object[] o;
                    for (Object y : data) {
                        o = (Object[]) y;
                        if (o != null ? o.length > 0 : false) {
                            Long _depot_ = (Long) o[0];
                            Long _article_ = (Long) o[1];
                            String _code_ = (String) o[2];
                            String _designation_ = (String) o[3];
                            String _famille_ = (String) o[4];
                            Long _unite_ = (Long) o[5];
                            String _reference_ = (String) o[6];
                            Double _puv_ = (Double) o[7];
                            Double _pua_ = (Double) o[8];

                            inventaire.getInventaires().add(new InventairePreparatoire(_depot_, _article_, _code_, _designation_, _famille_, _unite_, _reference_, _puv_, _pua_));
                        }
                    }
                } catch (NoResultException ex) {
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        update("tab_export");
    }

    public void loadAllByAgence() {
        if (objetExport.equals("employe")) {
            if (agencePrint > 0) {
                loadAllEmployeByAgence();
            } else {
                loadAllEmploye();
            }
        } else if (objetExport.equals("contrat")) {
            if (agencePrint > 0) {
                loadAllContratByAgence();
            } else {
                loadAllContrat();
            }
        }
        update("tab_export");
    }

    public void chooseObject(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            objetExport = (String) ev.getNewValue().toString();
            if (objetExport.equals("article")) {
                loadAllArticle();
            } else if (objetExport.equals("element_salaire")) {
                loadAllElementSalaire();
            } else if (objetExport.equals("plan_comptable")) {
                loadAllComptes();
            } else if (objetExport.equals("departement")) {
                loadAllDepartement();
            } else if (objetExport.equals("poste_travail")) {
                loadAllPoste();
            } else if (objetExport.equals("convention")) {
                loadAllConvention();
            } else if (objetExport.equals("doc_vente")) {
                loadAllFactureVente();
            }
            update("tab_export");
        }
    }

    public void handleFileUploadXLS(FileUploadEvent ev) {
        if (ev != null) {
            try {
//                getDataFromFileXLSX(ev.getFile().getInputstream());
                getDataFromFileExcel(ev.getFile().getInputstream());
                update("tabView_import_export:tab_import");
            } catch (IOException ex) {
                Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void handleFileUploadXML(FileUploadEvent ev) {
        if (ev != null) {
            String destination = Initialisation.getCheminResource() + "" + Initialisation.FILE_SEPARATOR + "temp.xml";
            try {
                file = Util.convertInputStreamToFile(destination, ev.getFile().getInputstream());
            } catch (IOException ex) {
                Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
            }
            getDataFromFileXML(file);
            update("tabView_import_export:tab_import");
        }
    }

    public void handleFileUploadTXT(FileUploadEvent ev) {
        if (ev != null) {
            try {
                getDataFromFileTXT(ev.getFile().getInputstream());
                update("tabView_import_export:tab_import");
            } catch (IOException ex) {
                Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void handleFileUploadCSV(FileUploadEvent ev) throws IOException {
        if (ev != null) {
            try {
                String destination = Initialisation.getCheminResource() + "" + Initialisation.FILE_SEPARATOR + "temp.csv";
                try {
                    file = Util.convertInputStreamToFile(destination, ev.getFile().getInputstream());
                } catch (IOException ex) {
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                getDataFromFileCSV(file);
                update("tabView_import_export:tab_import");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void print() {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("AGENCE", currentAgence.getId().intValue());
            param.put("NAME_AUTEUR", currentUser.getUsers().getNomUsers());
//            param.put("FAMILLE", (int) famillePrint);
//            param.put("GROUPE", (int) groupePrint);
//            param.put("CATEGORIE", categoriePrint);
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport("articles", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void postProcessTXT() {
        File files;
        if (objetExport.equals("article")) {
            files = new File("articles");
            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(files)));
                for (YvsBaseConditionnement d : listArticles) {
                    for (Field var : d.getClass().getDeclaredFields()) {

                    }
                    pw.println(d);
                }
                pw.close();
            } catch (IOException exception) {
                System.out.println("Erreur lors de la lecture : " + exception.getMessage());
            }
            System.err.println("impression article");
        } else if (objetExport.equals("element_salaire")) {
            files = new File("elements_salaire");
        }
    }

    private void genererReference(YvsBaseArticles bean, String famille) {
        System.err.println("famille :" + famille);
        if (!Util.asString(bean.getRefArt())) {
            if (currentParam == null) {
                currentParam = (YvsBaseParametre) dao.loadOneByNameQueries("YvsBaseParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            }
            if (currentParam != null ? currentParam.getGenererReferenceArticle() : false) {
                int tailleNumero = currentParam.getTailleNumeroReferenceArticle();
                int tailleLettre = currentParam.getTailleLettreReferenceArticle();
                String code = famille.trim().length() > tailleLettre ? famille.substring(0, tailleLettre) : famille;
                String result = (String) dao.loadObjectByNameQueries("YvsBaseArticles.findLastCodeLikeCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), code + "%"});
                if (Util.asString(result)) {
                    Integer numero = Integer.valueOf(result.replace(code, "").trim()) + 1;
                    if (Integer.toString(numero).length() > tailleNumero) {
                        return;
                    }
                    for (int i = 0; i < (tailleNumero - Integer.toString(numero).length()); i++) {
                        code += "0";
                    }
                    code += numero;
                } else {
                    for (int i = 0; i < (tailleNumero - 1); i++) {
                        code += "0";
                    }
                    code += "1";
                }
                bean.setRefArt(code);
            }
        }

    }

    public YvsBaseConditionnement getArticleFormData(Row maRow) {
        YvsBaseConditionnement bean = new YvsBaseConditionnement();
        try {
            if (maRow != null) {
                bean.setArticle(new YvsBaseArticles(null, maRow.getCell(0).getStringCellValue().trim(), maRow.getCell(0).getStringCellValue().trim()));
                bean.getArticle().setDesignation(maRow.getCell(1).getStringCellValue().trim());
                bean.setUnite(new YvsBaseUniteMesure(null, maRow.getCell(2).getStringCellValue().trim(), maRow.getCell(2).getStringCellValue().trim()));
                bean.setPrixAchat(getCellDouble(maRow.getCell(3)));
                bean.setPrix(getCellDouble(maRow.getCell(4)));
                bean.getArticle().setFamille(new YvsBaseFamilleArticle(maRow.getCell(5).getStringCellValue().trim()));
                bean.setActif(true);
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsBaseConditionnement getArticleFormData(HSSFRow maRow) {
        YvsBaseConditionnement bean = new YvsBaseConditionnement();
        try {
            if (maRow != null) {
                bean.setArticle(new YvsBaseArticles(null, maRow.getCell(0).getStringCellValue().trim(), maRow.getCell(0).getStringCellValue().trim()));
                bean.getArticle().setDesignation(maRow.getCell(1).getStringCellValue().trim());
                bean.setUnite(new YvsBaseUniteMesure(null, maRow.getCell(2).getStringCellValue().trim(), maRow.getCell(2).getStringCellValue().trim()));
                bean.setPrixAchat(getCellDouble(maRow.getCell(3)));
                bean.setPrix(getCellDouble(maRow.getCell(4)));
                bean.getArticle().setFamille(new YvsBaseFamilleArticle(maRow.getCell(5).getStringCellValue().trim()));
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsBaseConditionnement getArticleFormData(XSSFRow maRow) {
        YvsBaseConditionnement bean = new YvsBaseConditionnement();
        try {
            if (maRow != null) {
                bean.setArticle(new YvsBaseArticles(null, maRow.getCell(0).getStringCellValue().trim(), maRow.getCell(0).getStringCellValue().trim()));
                bean.getArticle().setDesignation(maRow.getCell(1).getStringCellValue().trim());
                bean.setUnite(new YvsBaseUniteMesure(null, maRow.getCell(2).getStringCellValue().trim(), maRow.getCell(2).getStringCellValue().trim()));
                bean.setPrixAchat(getCellDouble(maRow.getCell(3)));
                bean.setPrix(getCellDouble(maRow.getCell(4)));
                bean.getArticle().setFamille(new YvsBaseFamilleArticle(maRow.getCell(5).getStringCellValue().trim()));
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsBasePlanComptable getCompteFormData(Row maRow) {
        YvsBasePlanComptable bean = new YvsBasePlanComptable();
        try {
            if (maRow != null) {
                bean.setNumCompte(maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "");
                bean.setIntitule(maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "");
                bean.setIntitule(bean.getIntitule().toUpperCase());
                String nature = maRow.getCell(2) != null ? maRow.getCell(2).getStringCellValue().trim() : "";
                bean.setNatureCompte(new YvsBaseNatureCompte(null, nature));
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsBasePlanComptable getCompteFormData(HSSFRow maRow) {
        YvsBasePlanComptable bean = new YvsBasePlanComptable();
        try {
            if (maRow != null) {
                bean.setNumCompte(maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "");
                bean.setIntitule(maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "");
                bean.setIntitule(bean.getIntitule().toUpperCase());
                String nature = maRow.getCell(2) != null ? maRow.getCell(2).getStringCellValue().trim() : "";
                bean.setNatureCompte(new YvsBaseNatureCompte(null, nature));
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsBasePlanComptable getCompteFormData(XSSFRow maRow) {
        YvsBasePlanComptable bean = new YvsBasePlanComptable();
        try {
            if (maRow != null) {
                bean.setNumCompte(maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "");
                bean.setIntitule(maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "");
                bean.setIntitule(bean.getIntitule().toUpperCase());
                String nature = maRow.getCell(2) != null ? maRow.getCell(2).getStringCellValue().trim() : "";
                bean.setNatureCompte(new YvsBaseNatureCompte(null, nature));
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsComContenuDocStock getContenuFormData(Row maRow) {
        YvsComContenuDocStock bean = new YvsComContenuDocStock();
        try {
            if (maRow != null) {
                String article = maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "";
                bean.setArticle(new YvsBaseArticles(null, article, article));
                String unite = maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "";
                bean.setConditionnement(new YvsBaseConditionnement(null, new YvsBaseUniteMesure(null, unite, unite)));
                try {
                    bean.setQuantite(maRow.getCell(2) != null ? maRow.getCell(2).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setQuantite(maRow.getCell(2) != null ? Double.valueOf(maRow.getCell(2).getStringCellValue().trim()) : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                String depot = maRow.getCell(3) != null ? maRow.getCell(3).getStringCellValue().trim() : "";
                if (bean.getDocStock() == null) {
                    bean.setDocStock(new YvsComDocStocks());
                }
                bean.getDocStock().setSource(new YvsBaseDepots(null, depot));
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsComContenuDocStock getContenuFormData(HSSFRow maRow) {
        YvsComContenuDocStock bean = new YvsComContenuDocStock();
        try {
            if (maRow != null) {
                String article = maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "";
                bean.setArticle(new YvsBaseArticles(null, article, article));
                String unite = maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "";
                bean.setConditionnement(new YvsBaseConditionnement(null, new YvsBaseUniteMesure(null, unite, unite)));
                try {
                    bean.setQuantite(maRow.getCell(2) != null ? maRow.getCell(2).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setQuantite(maRow.getCell(2) != null ? Double.valueOf(maRow.getCell(2).getStringCellValue().trim()) : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                String depot = maRow.getCell(3) != null ? maRow.getCell(3).getStringCellValue().trim() : "";
                if (bean.getDocStock() == null) {
                    bean.setDocStock(new YvsComDocStocks());
                }
                bean.getDocStock().setSource(new YvsBaseDepots(null, depot));
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsComContenuDocStock getContenuFormData(XSSFRow maRow) {
        YvsComContenuDocStock bean = new YvsComContenuDocStock();
        try {
            if (maRow != null) {
                String article = maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "";
                bean.setArticle(new YvsBaseArticles(null, article, article));
                String unite = maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "";
                bean.setConditionnement(new YvsBaseConditionnement(null, new YvsBaseUniteMesure(null, unite, unite)));
                try {
                    bean.setQuantite(maRow.getCell(2) != null ? maRow.getCell(2).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setQuantite(maRow.getCell(2) != null ? Double.valueOf(maRow.getCell(2).getStringCellValue().trim()) : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                String depot = maRow.getCell(3) != null ? maRow.getCell(3).getStringCellValue().trim() : "";
                if (bean.getDocStock() == null) {
                    bean.setDocStock(new YvsComDocStocks());
                }
                bean.getDocStock().setSource(new YvsBaseDepots(null, depot));
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhDepartement getDepartementFormData(Row maRow) {
        YvsGrhDepartement bean = new YvsGrhDepartement();
        try {
            if (maRow != null) {
                bean.setCodeDepartement(maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "");
                bean.setAbreviation(maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "");
                bean.setIntitule(maRow.getCell(2) != null ? maRow.getCell(2).getStringCellValue().trim() : "");
                String parent = maRow.getCell(3) != null ? maRow.getCell(3).getStringCellValue().trim() : "";
                bean.setDepartementParent(new YvsGrhDepartement(null, parent, null));
                bean.setVisibleOnLivrePaie(maRow.getCell(4) != null ? Boolean.valueOf(maRow.getCell(4).getStringCellValue().trim()) : false);
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhDepartement getDepartementFormData(HSSFRow maRow) {
        YvsGrhDepartement bean = new YvsGrhDepartement();
        try {
            if (maRow != null) {
                bean.setCodeDepartement(maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "");
                bean.setAbreviation(maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "");
                bean.setIntitule(maRow.getCell(2) != null ? maRow.getCell(2).getStringCellValue().trim() : "");
                String parent = maRow.getCell(3) != null ? maRow.getCell(3).getStringCellValue().trim() : "";
                bean.setDepartementParent(new YvsGrhDepartement(null, parent, null));
                bean.setVisibleOnLivrePaie(maRow.getCell(4) != null ? Boolean.valueOf(maRow.getCell(4).getStringCellValue().trim()) : false);
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhDepartement getDepartementFormData(XSSFRow maRow) {
        YvsGrhDepartement bean = new YvsGrhDepartement();
        try {
            if (maRow != null) {
                bean.setCodeDepartement(maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "");
                bean.setAbreviation(maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "");
                bean.setIntitule(maRow.getCell(2) != null ? maRow.getCell(2).getStringCellValue().trim() : "");
                String parent = maRow.getCell(3) != null ? maRow.getCell(3).getStringCellValue().trim() : "";
                bean.setDepartementParent(new YvsGrhDepartement(null, parent, null));
                bean.setVisibleOnLivrePaie(maRow.getCell(4) != null ? Boolean.valueOf(maRow.getCell(4).getStringCellValue().trim()) : false);
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhPosteDeTravail getPosteFormData(Row maRow) {
        YvsGrhPosteDeTravail bean = new YvsGrhPosteDeTravail();
        try {
            if (maRow != null) {
                bean.setIntitule(maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "");
                String departement = maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "";
                bean.setDepartement(new YvsGrhDepartement(null, departement, null));
                bean.setGrade(maRow.getCell(2) != null ? maRow.getCell(2).getStringCellValue().trim() : "");
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhPosteDeTravail getPosteFormData(HSSFRow maRow) {
        YvsGrhPosteDeTravail bean = new YvsGrhPosteDeTravail();
        try {
            if (maRow != null) {
                bean.setIntitule(maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "");
                String departement = maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "";
                bean.setDepartement(new YvsGrhDepartement(null, departement, null));
                bean.setGrade(maRow.getCell(2) != null ? maRow.getCell(2).getStringCellValue().trim() : "");
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhPosteDeTravail getPosteFormData(XSSFRow maRow) {
        YvsGrhPosteDeTravail bean = new YvsGrhPosteDeTravail();
        try {
            if (maRow != null) {
                bean.setIntitule(maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "");
                String departement = maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "";
                bean.setDepartement(new YvsGrhDepartement(null, departement, null));
                bean.setGrade(maRow.getCell(2) != null ? maRow.getCell(2).getStringCellValue().trim() : "");
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhEmployes getEmployeFormData(Row maRow) {
        YvsGrhEmployes bean = new YvsGrhEmployes();
        try {
            if (maRow != null) {
                bean.setMatricule(maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "");
                bean.setCivilite(maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "");
                bean.setNom(maRow.getCell(2) != null ? maRow.getCell(2).getStringCellValue().trim() : "");
                bean.setPrenom(maRow.getCell(3) != null ? maRow.getCell(3).getStringCellValue().trim() : "");
                bean.setMatriculeCnps(maRow.getCell(4) != null ? maRow.getCell(4).getStringCellValue().trim() : "");
                bean.setAdresse1(maRow.getCell(5) != null ? maRow.getCell(5).getStringCellValue().trim() : "");
                bean.setDateNaissance(maRow.getCell(6) != null ? formatDate.parse(maRow.getCell(6).getStringCellValue().trim()) : new Date());
                bean.setDateEmbauche(maRow.getCell(7) != null ? formatDate.parse(maRow.getCell(7).getStringCellValue().trim()) : new Date());
                String poste = maRow.getCell(8) != null ? maRow.getCell(8).getStringCellValue().trim() : "";
                bean.setPosteActif(new YvsGrhPosteDeTravail(null, poste));
                bean.setPhotos(maRow.getCell(9) != null ? maRow.getCell(9).getStringCellValue().trim() : "");
                String agence = maRow.getCell(10) != null ? maRow.getCell(10).getStringCellValue().trim() : "";
                bean.setAgence(new YvsAgences(null, agence, null));
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhEmployes getEmployeFormData(HSSFRow maRow) {
        YvsGrhEmployes bean = new YvsGrhEmployes();
        try {
            if (maRow != null) {
                bean.setMatricule(maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "");
                bean.setCivilite(maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "");
                bean.setNom(maRow.getCell(2) != null ? maRow.getCell(2).getStringCellValue().trim() : "");
                bean.setPrenom(maRow.getCell(3) != null ? maRow.getCell(3).getStringCellValue().trim() : "");
                bean.setMatriculeCnps(maRow.getCell(4) != null ? maRow.getCell(4).getStringCellValue().trim() : "");
                bean.setAdresse1(maRow.getCell(5) != null ? maRow.getCell(5).getStringCellValue().trim() : "");
                bean.setDateNaissance(maRow.getCell(6) != null ? formatDate.parse(maRow.getCell(6).getStringCellValue().trim()) : new Date());
                bean.setDateEmbauche(maRow.getCell(7) != null ? formatDate.parse(maRow.getCell(7).getStringCellValue().trim()) : new Date());
                String poste = maRow.getCell(8) != null ? maRow.getCell(8).getStringCellValue().trim() : "";
                bean.setPosteActif(new YvsGrhPosteDeTravail(null, poste));
                String agence = maRow.getCell(9) != null ? maRow.getCell(9).getStringCellValue().trim() : "";
                bean.setAgence(new YvsAgences(null, agence, null));
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhEmployes getEmployeFormData(XSSFRow maRow) {
        YvsGrhEmployes bean = new YvsGrhEmployes();
        try {
            if (maRow != null) {
                bean.setMatricule(maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "");
                bean.setCivilite(maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "");
                bean.setNom(maRow.getCell(2) != null ? maRow.getCell(2).getStringCellValue().trim() : "");
                bean.setPrenom(maRow.getCell(3) != null ? maRow.getCell(3).getStringCellValue().trim() : "");
                bean.setMatriculeCnps(maRow.getCell(4) != null ? maRow.getCell(4).getStringCellValue().trim() : "");
                bean.setAdresse1(maRow.getCell(5) != null ? maRow.getCell(5).getStringCellValue().trim() : "");
                bean.setDateNaissance(maRow.getCell(6) != null ? formatDate.parse(maRow.getCell(6).getStringCellValue().trim()) : new Date());
                bean.setDateEmbauche(maRow.getCell(7) != null ? formatDate.parse(maRow.getCell(7).getStringCellValue().trim()) : new Date());
                String poste = maRow.getCell(8) != null ? maRow.getCell(8).getStringCellValue().trim() : "";
                bean.setPosteActif(new YvsGrhPosteDeTravail(null, poste));
                String agence = maRow.getCell(9) != null ? maRow.getCell(9).getStringCellValue().trim() : "";
                bean.setAgence(new YvsAgences(null, agence, null));
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhContratEmps getContratFormData(Row maRow) {
        YvsGrhContratEmps bean = new YvsGrhContratEmps();
        try {
            if (maRow != null) {
                String matricule = maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "";
                bean.setEmploye(new YvsGrhEmployes(null, matricule));
                bean.setReferenceContrat(maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "");
                bean.setDateDebut(maRow.getCell(2) != null ? formatDate.parse(maRow.getCell(2).getStringCellValue().trim()) : new Date());
                bean.setDateFin(maRow.getCell(3) != null ? formatDate.parse(maRow.getCell(3).getStringCellValue().trim()) : new Date());
                try {
                    bean.setSalaireHoraire(maRow.getCell(4) != null ? maRow.getCell(4).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setSalaireHoraire(Util.asValeur(maRow.getCell(4)) ? Lnf.parse(maRow.getCell(4).getStringCellValue().trim()).doubleValue() : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    bean.setSalaireMensuel(maRow.getCell(5) != null ? maRow.getCell(5).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setSalaireMensuel(Util.asValeur(maRow.getCell(5)) ? Lnf.parse(maRow.getCell(5).getStringCellValue().trim()).doubleValue() : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                String modePaiement = maRow.getCell(6) != null ? maRow.getCell(6).getStringCellValue().trim() : "";
                bean.setModePaiement(new YvsBaseModeReglement(null, modePaiement));
                String typeContrat = maRow.getCell(7) != null ? maRow.getCell(7).getStringCellValue().trim() : "";
                bean.setTypeContrat(new YvsTypeContrat(null, typeContrat));
                try {
                    bean.setHoraireHebdo(maRow.getCell(8) != null ? maRow.getCell(8).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setHoraireHebdo(Util.asValeur(maRow.getCell(8)) ? Lnf.parse(maRow.getCell(8).getStringCellValue().trim()).doubleValue() : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    bean.setCongeAcquis(maRow.getCell(9) != null ? maRow.getCell(9).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setCongeAcquis(Util.asValeur(maRow.getCell(9)) ? Lnf.parse(maRow.getCell(9).getStringCellValue().trim()).doubleValue() : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                bean.setTypeTranche(maRow.getCell(10) != null ? maRow.getCell(10).getStringCellValue().trim() : "");
                bean.setSourceFirstConge(maRow.getCell(11) != null ? maRow.getCell(11).getStringCellValue().trim() : "");
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhContratEmps getContratFormData(HSSFRow maRow) {
        YvsGrhContratEmps bean = new YvsGrhContratEmps();
        try {
            if (maRow != null) {
                String matricule = maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "";
                bean.setEmploye(new YvsGrhEmployes(null, matricule));
                bean.setReferenceContrat(maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "");
                bean.setDateDebut(maRow.getCell(2) != null ? formatDate.parse(maRow.getCell(2).getStringCellValue().trim()) : new Date());
                bean.setDateFin(maRow.getCell(3) != null ? formatDate.parse(maRow.getCell(3).getStringCellValue().trim()) : new Date());
                try {
                    bean.setSalaireHoraire(maRow.getCell(4) != null ? maRow.getCell(4).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setSalaireHoraire(Util.asValeur(maRow.getCell(4)) ? Lnf.parse(maRow.getCell(4).getStringCellValue().trim()).doubleValue() : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    bean.setSalaireMensuel(maRow.getCell(5) != null ? maRow.getCell(5).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setSalaireMensuel(Util.asValeur(maRow.getCell(5)) ? Lnf.parse(maRow.getCell(5).getStringCellValue().trim()).doubleValue() : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                String modePaiement = maRow.getCell(6) != null ? maRow.getCell(6).getStringCellValue().trim() : "";
                bean.setModePaiement(new YvsBaseModeReglement(null, modePaiement));
                String typeContrat = maRow.getCell(7) != null ? maRow.getCell(7).getStringCellValue().trim() : "";
                bean.setTypeContrat(new YvsTypeContrat(null, typeContrat));
                try {
                    bean.setHoraireHebdo(maRow.getCell(8) != null ? maRow.getCell(8).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setHoraireHebdo(Util.asValeur(maRow.getCell(8)) ? Lnf.parse(maRow.getCell(8).getStringCellValue().trim()).doubleValue() : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    bean.setCongeAcquis(maRow.getCell(9) != null ? maRow.getCell(9).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setCongeAcquis(Util.asValeur(maRow.getCell(9)) ? Lnf.parse(maRow.getCell(9).getStringCellValue().trim()).doubleValue() : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                bean.setTypeTranche(maRow.getCell(10) != null ? maRow.getCell(10).getStringCellValue().trim() : "");
                bean.setSourceFirstConge(maRow.getCell(11) != null ? maRow.getCell(11).getStringCellValue().trim() : "");
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhContratEmps getContratFormData(XSSFRow maRow) {
        YvsGrhContratEmps bean = new YvsGrhContratEmps();
        try {
            if (maRow != null) {
                String matricule = maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "";
                bean.setEmploye(new YvsGrhEmployes(null, matricule));
                bean.setReferenceContrat(maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "");
                bean.setDateDebut(maRow.getCell(2) != null ? formatDate.parse(maRow.getCell(2).getStringCellValue().trim()) : new Date());
                bean.setDateFin(maRow.getCell(3) != null ? formatDate.parse(maRow.getCell(3).getStringCellValue().trim()) : new Date());
                try {
                    bean.setSalaireHoraire(maRow.getCell(4) != null ? maRow.getCell(4).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setSalaireHoraire(Util.asValeur(maRow.getCell(4)) ? Lnf.parse(maRow.getCell(4).getStringCellValue().trim()).doubleValue() : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    bean.setSalaireMensuel(maRow.getCell(5) != null ? maRow.getCell(5).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setSalaireMensuel(Util.asValeur(maRow.getCell(5)) ? Lnf.parse(maRow.getCell(5).getStringCellValue().trim()).doubleValue() : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                String modePaiement = maRow.getCell(6) != null ? maRow.getCell(6).getStringCellValue().trim() : "";
                bean.setModePaiement(new YvsBaseModeReglement(null, modePaiement));
                String typeContrat = maRow.getCell(7) != null ? maRow.getCell(7).getStringCellValue().trim() : "";
                bean.setTypeContrat(new YvsTypeContrat(null, typeContrat));
                try {
                    bean.setHoraireHebdo(maRow.getCell(8) != null ? maRow.getCell(8).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setHoraireHebdo(Util.asValeur(maRow.getCell(8)) ? Lnf.parse(maRow.getCell(8).getStringCellValue().trim()).doubleValue() : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    bean.setCongeAcquis(maRow.getCell(9) != null ? maRow.getCell(9).getNumericCellValue() : 0);
                } catch (Exception ex) {
                    bean.setCongeAcquis(Util.asValeur(maRow.getCell(9)) ? Lnf.parse(maRow.getCell(9).getStringCellValue().trim()).doubleValue() : 0);
                    Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
                }
                bean.setTypeTranche(maRow.getCell(10) != null ? maRow.getCell(10).getStringCellValue().trim() : "");
                bean.setSourceFirstConge(maRow.getCell(11) != null ? maRow.getCell(11).getStringCellValue().trim() : "");
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhConventionCollective getConventionCollectiveFormData(Row maRow) {
        YvsGrhConventionCollective bean = new YvsGrhConventionCollective();
        try {
            if (maRow != null) {
                String categorie = maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "";
                bean.setCategorie(new YvsGrhCategorieProfessionelle(null, categorie));
                String echellon = maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "";
                bean.setEchelon(new YvsGrhEchelons(null, echellon));
                bean.setSalaireMin(getCellDouble(maRow.getCell(2)));
                bean.setSalaireHoraireMin(getCellDouble(maRow.getCell(3)));
                String secteur = maRow.getCell(4) != null ? maRow.getCell(4).getStringCellValue().trim() : "";
                bean.setYvsSecteurs(new YvsGrhSecteurs(null, secteur));
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhConventionCollective getConventionCollectiveFormData(HSSFRow maRow) {
        YvsGrhConventionCollective bean = new YvsGrhConventionCollective();
        try {
            if (maRow != null) {
                String categorie = maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "";
                bean.setCategorie(new YvsGrhCategorieProfessionelle(null, categorie));
                String echellon = maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "";
                bean.setEchelon(new YvsGrhEchelons(null, echellon));
                bean.setSalaireMin(getCellDouble(maRow.getCell(2)));
                bean.setSalaireHoraireMin(getCellDouble(maRow.getCell(3)));
                String secteur = maRow.getCell(4) != null ? maRow.getCell(4).getStringCellValue().trim() : "";
                bean.setYvsSecteurs(new YvsGrhSecteurs(null, secteur));
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public YvsGrhConventionCollective getConventionCollectiveFormData(XSSFRow maRow) {
        YvsGrhConventionCollective bean = new YvsGrhConventionCollective();
        try {
            if (maRow != null) {
                String categorie = maRow.getCell(0) != null ? maRow.getCell(0).getStringCellValue().trim() : "";
                bean.setCategorie(new YvsGrhCategorieProfessionelle(null, categorie));
                String echellon = maRow.getCell(1) != null ? maRow.getCell(1).getStringCellValue().trim() : "";
                bean.setEchelon(new YvsGrhEchelons(null, echellon));
                bean.setSalaireMin(getCellDouble(maRow.getCell(2)));
                bean.setSalaireHoraireMin(getCellDouble(maRow.getCell(3)));
                String secteur = maRow.getCell(4) != null ? maRow.getCell(4).getStringCellValue().trim() : "";
                bean.setYvsSecteurs(new YvsGrhSecteurs(null, secteur));
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public void getDataFromFileXLS(InputStream monFileInputStream) throws IOException {
        if (monFileInputStream != null) {
            try {
                POIFSFileSystem monFileSystem = new POIFSFileSystem(monFileInputStream);
                if (monFileSystem != null) {
                    //création du worbook
                    HSSFWorkbook monWorkbook = new HSSFWorkbook(monFileSystem);
                    //HSSFWorkbook monWorkbook = new HSSFWorkbook(fileInputStream);
                    HSSFSheet maSheet = monWorkbook.getSheetAt(0);
                    int idexRowMax = maSheet.getLastRowNum();
                    objetImport = getElementObject(maSheet.getRow(0));
                    switch (objetImport) {
                        case "article":
                            listImportArticles.clear();
                            for (int i = 1; i <= idexRowMax; i++) {
                                HSSFRow maRow = maSheet.getRow(i);
                                YvsBaseConditionnement bean = getArticleFormData(maRow);
                                listImportArticles.add(bean);
                            }
                            break;
                        case "plan_comptable":
                            listImportComptes.clear();
                            for (int i = 1; i <= idexRowMax; i++) {
                                HSSFRow maRow = maSheet.getRow(i);
                                YvsBasePlanComptable bean = getCompteFormData(maRow);
                                listImportComptes.add(bean);
                            }
                            break;
                        case "inventaire":
                            listImportContenus.clear();
                            for (int i = 1; i <= idexRowMax; i++) {
                                HSSFRow maRow = maSheet.getRow(i);
                                YvsComContenuDocStock bean = getContenuFormData(maRow);
                                listImportContenus.add(bean);
                            }
                            break;
                        case "regle_salaire":

                            break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getDataFromFileXLSX(InputStream monFileInputStream) throws IOException {
        if (monFileInputStream != null) {
            try {
                //création du worbook
                XSSFWorkbook monWorkbook = new XSSFWorkbook(monFileInputStream);
                //HSSFWorkbook monWorkbook = new HSSFWorkbook(fileInputStream);
                XSSFSheet maSheet = monWorkbook.getSheetAt(0);
                int idexRowMax = maSheet.getLastRowNum();
                objetImport = getElementObject(maSheet.getRow(0));
                switch (objetImport) {
                    case "article":
                        listImportArticles.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            XSSFRow maRow = maSheet.getRow(i);
                            YvsBaseConditionnement bean = getArticleFormData(maRow);
                            listImportArticles.add(bean);
                        }
                        break;
                    case "plan_comptable":
                        listImportComptes.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            XSSFRow maRow = maSheet.getRow(i);
                            YvsBasePlanComptable bean = getCompteFormData(maRow);
                            listImportComptes.add(bean);
                        }
                        break;
                    case "inventaire":
                        listImportContenus.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            XSSFRow maRow = maSheet.getRow(i);
                            YvsComContenuDocStock bean = getContenuFormData(maRow);
                            listImportContenus.add(bean);
                        }
                        break;
                    case "regle_salaire":

                        break;
                    case "employe":
                        listImportEmployes.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            XSSFRow maRow = maSheet.getRow(i);
                            YvsGrhEmployes bean = getEmployeFormData(maRow);
                            listImportEmployes.add(bean);
                        }
                        break;
                    case "departement":
                        listImportDepartements.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            XSSFRow maRow = maSheet.getRow(i);
                            YvsGrhDepartement bean = getDepartementFormData(maRow);
                            listImportDepartements.add(bean);
                        }
                        break;
                    case "poste_travail":
                        listImportPostes.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            XSSFRow maRow = maSheet.getRow(i);
                            YvsGrhPosteDeTravail bean = getPosteFormData(maRow);
                            listImportPostes.add(bean);
                        }
                        break;
                    case "contrat":
                        listImportContrats.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            XSSFRow maRow = maSheet.getRow(i);
                            YvsGrhContratEmps bean = getContratFormData(maRow);
                            listImportContrats.add(bean);
                        }
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getDataFromFileExcel(InputStream monFileInputStream) throws IOException {
        if (monFileInputStream != null) {
            try {
                //création du worbook
                Workbook monWorkbook = WorkbookFactory.create(monFileInputStream);
                //HSSFWorkbook monWorkbook = new HSSFWorkbook(fileInputStream);
                Sheet maSheet = monWorkbook.getSheetAt(0);
                int idexRowMax = maSheet.getLastRowNum();
                objetImport = getElementObject(maSheet.getRow(0));
                switch (objetImport) {
                    case "article":
                        listImportArticles.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            Row maRow = maSheet.getRow(i);
                            YvsBaseConditionnement bean = getArticleFormData(maRow);
                            listImportArticles.add(bean);
                        }
                        break;
                    case "plan_comptable":
                        listImportComptes.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            Row maRow = maSheet.getRow(i);
                            YvsBasePlanComptable bean = getCompteFormData(maRow);
                            listImportComptes.add(bean);
                        }
                        break;
                    case "inventaire":
                        listImportContenus.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            Row maRow = maSheet.getRow(i);
                            YvsComContenuDocStock bean = getContenuFormData(maRow);
                            listImportContenus.add(bean);
                        }
                        break;
                    case "regle_salaire":

                        break;
                    case "employe":
                        listImportEmployes.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            Row maRow = maSheet.getRow(i);
                            YvsGrhEmployes bean = getEmployeFormData(maRow);
                            listImportEmployes.add(bean);
                        }
                        break;
                    case "departement":
                        listImportDepartements.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            Row maRow = maSheet.getRow(i);
                            YvsGrhDepartement bean = getDepartementFormData(maRow);
                            listImportDepartements.add(bean);
                        }
                        break;
                    case "poste_travail":
                        listImportPostes.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            Row maRow = maSheet.getRow(i);
                            YvsGrhPosteDeTravail bean = getPosteFormData(maRow);
                            listImportPostes.add(bean);
                        }
                        break;
                    case "contrat":
                        listImportContrats.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            Row maRow = maSheet.getRow(i);
                            YvsGrhContratEmps bean = getContratFormData(maRow);
                            listImportContrats.add(bean);
                        }
                        break;
                    case "convention":
                        listImportConventions.clear();
                        for (int i = 1; i <= idexRowMax; i++) {
                            Row maRow = maSheet.getRow(i);
                            YvsGrhConventionCollective bean = getConventionCollectiveFormData(maRow);
                            listImportConventions.add(bean);
                        }
                        break;
                }
            } catch (IOException | InvalidFormatException ex) {
                Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getDataFromFileTXT(InputStream monFileInputStream) {

    }

    public YvsBaseConditionnement getArticleFormData(Element maRow) {
        YvsBaseConditionnement bean = new YvsBaseConditionnement();
        Element elt;
        if (maRow != null) {
//            elt = (Element) maRow.getElementsByTagName("id").item(0);
//            bean.setId(Long.valueOf(elt.getTextContent()));
//            elt = (Element) maRow.getElementsByTagName("reference").item(0);
//            bean.setRefArt(elt.getTextContent());
//            elt = (Element) maRow.getElementsByTagName("designation").item(0);
//            bean.setDesignation(elt.getTextContent());
//            elt = (Element) maRow.getElementsByTagName("code_barre").item(0);
//            bean.setCodeBarre(elt.getTextContent());
//            elt = (Element) maRow.getElementsByTagName("description").item(0);
//            bean.setDescription(elt.getTextContent());
//            elt = (Element) maRow.getElementsByTagName("masse_net").item(0);
//            bean.setMasseNet((!"".equals(elt.getTextContent())) ? Double.valueOf(elt.getTextContent()) : 0);
//            elt = (Element) maRow.getElementsByTagName("unite_stockage").item(0);
////            bean.getUniteStockage().setId((!"".equals(elt.getTextContent())) ? Long.valueOf(elt.getTextContent()) : 0);
//            elt = (Element) maRow.getElementsByTagName("mode_consommation").item(0);
//            bean.setModeConso(elt.getTextContent());
//            elt = (Element) maRow.getElementsByTagName("prix_minimal").item(0);
//            bean.setPrixMin((!"".equals(elt.getTextContent())) ? Double.valueOf(elt.getTextContent()) : 0);
//            elt = (Element) maRow.getElementsByTagName("prix_unitaire_vente").item(0);
//            bean.setPuv((!"".equals(elt.getTextContent())) ? Double.valueOf(elt.getTextContent()) : 0);
//            elt = (Element) maRow.getElementsByTagName("prix_unitaire_achat").item(0);
//            bean.setPua((!"".equals(elt.getTextContent())) ? Double.valueOf(elt.getTextContent()) : 0);
//            elt = (Element) maRow.getElementsByTagName("remise").item(0);
//            bean.setRemise((!"".equals(elt.getTextContent())) ? Double.valueOf(elt.getTextContent()) : 0);
//            elt = (Element) maRow.getElementsByTagName("unite_masse").item(0);
//            bean.getUnite().setId((!"".equals(elt.getTextContent())) ? Long.valueOf(elt.getTextContent()) : 0);
//            elt = (Element) maRow.getElementsByTagName("methode_valorisation").item(0);
//            bean.setMethodeVal(elt.getTextContent());
        }
        return bean;
    }

    public void getDataFromFileXML(File monFileInputStream) {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.parse(new FileInputStream(monFileInputStream));
            final Element racine = document.getDocumentElement();
            objetImport = getElementObject(racine);
            final NodeList racineNoeuds = racine.getChildNodes();
            final int nbRacineNoeuds = racineNoeuds.getLength();
            switch (objetImport) {
                case "article":
                    listImportArticles.clear();
                    for (int i = 0; i < nbRacineNoeuds; i++) {
                        if (racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
                            final Element element = (Element) racineNoeuds.item(i);
                            YvsBaseConditionnement bean = getArticleFormData(element);
                            listImportArticles.add(bean);
                        }
                    }
                    break;
                case "plan_comptable":
                    listImportComptes.clear();

                    break;
                case "inventaire":
                    listImportContenus.clear();

                    break;
                case "regle_salaire":

                    break;
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public YvsBaseConditionnement getArticleFormData(String[] maRow) {
        YvsBaseConditionnement bean = new YvsBaseConditionnement();
        if (maRow != null) {
//            bean.setId(Long.valueOf(maRow[0].trim().substring(1, maRow[0].trim().length() - 1)));
//            bean.setRefArt(maRow[1].trim().substring(1, maRow[1].trim().length() - 1));
//            bean.setDesignation(maRow[2].trim().substring(1, maRow[2].trim().length() - 1));
//            bean.setCodeBarre(maRow[3].trim().substring(1, maRow[3].trim().length() - 1));
//            bean.setDescription(maRow[4].trim().substring(1, maRow[4].trim().length() - 1));
//            bean.setMasseNet((!"".equals(maRow[5].trim().substring(1, maRow[5].trim().length() - 1))) ? Double.valueOf(maRow[5].trim().substring(1, maRow[5].trim().length() - 1)) : 0);
//            int id = Integer.valueOf(maRow[6].trim().substring(1, maRow[6].trim().length() - 1));
////            bean.setUniteStockage(new YvsBaseUniteMesure((long)id));
//            bean.setModeConso(maRow[7].trim().substring(1, maRow[7].trim().length() - 1));
//            bean.setPrixMin((!"".equals(maRow[8].trim().substring(1, maRow[8].trim().length() - 1))) ? Double.valueOf(maRow[8].trim().substring(1, maRow[8].trim().length() - 1)) : 0);
//            bean.setPuv((!"".equals(maRow[9].trim().substring(1, maRow[9].trim().length() - 1))) ? Double.valueOf(maRow[9].trim().substring(1, maRow[9].trim().length() - 1)) : 0);
//            bean.setPua((!"".equals(maRow[10].trim().substring(1, maRow[10].trim().length() - 1))) ? Double.valueOf(maRow[10].trim().substring(1, maRow[10].trim().length() - 1)) : 0);
//            bean.setRemise((!"".equals(maRow[11].trim().substring(1, maRow[11].trim().length() - 1))) ? Double.valueOf(maRow[11].trim().substring(1, maRow[11].trim().length() - 1)) : 0);
//            id = Integer.valueOf(maRow[12].trim().substring(1, maRow[12].trim().length() - 1));
//            bean.setUnite(new YvsBaseConditionnement((long) id));
//            bean.setMethodeVal(maRow[13].trim().substring(1, maRow[13].trim().length() - 1));
        }
        return bean;
    }

    public void getDataFromFileCSV(File monFileInputStream) throws FileNotFoundException, IOException {
        if (monFileInputStream != null) {
            try {
                try (BufferedReader br = new BufferedReader(new FileReader(monFileInputStream))) {
                    String ligne = null;
                    int cpt = 0;
                    listImportArticles.clear();
                    listImportComptes.clear();
                    listImportContenus.clear();
                    while ((ligne = br.readLine()) != null) {
                        String[] data = ligne.split(",");
                        if (cpt == 0) {
                            objetImport = getElementObject(data);
                        } else {
                            switch (objetImport) {
                                case "article":
                                    YvsBaseConditionnement bean = getArticleFormData(data);
                                    listImportArticles.add(bean);
                                    break;
                                case "plan_comptable":

                                    break;
                                case "inventaire":

                                    break;
                                case "regle_salaire":

                                    break;
                            }
                        }
                        cpt += 1;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private double getCellDouble(Cell cell) {
        if (cell == null) {
            return 0;
        }
        try {
            return cell.getNumericCellValue();
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
            try {
                return Lnf.parse(cell.getStringCellValue().trim()).doubleValue();
            } catch (ParseException ex1) {
                Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return 0;
    }

    private double getCellDouble(HSSFCell cell) {
        if (cell == null) {
            return 0;
        }
        try {
            return cell.getNumericCellValue();
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
            try {
                return Lnf.parse(cell.getStringCellValue().trim()).doubleValue();
            } catch (ParseException ex1) {
                Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return 0;
    }

    private double getCellDouble(XSSFCell cell) {
        if (cell == null) {
            return 0;
        }
        try {
            return cell.getNumericCellValue();
        } catch (Exception ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
            try {
                return Lnf.parse(cell.getStringCellValue().trim()).doubleValue();
            } catch (ParseException ex1) {
                Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return 0;
    }

    private String getElementObject(String title) {
        if (!Util.asString(title)) {
            return "";
        }
        switch (title.toLowerCase()) {
            case "reference":
                return "article";
            case "numero":
                return "plan_comptable";
            case "code":
                return "regle_salaire";
            case "article":
                return "inventaire";
            case "matricule":
                return "employe";
            case "departement":
                return "departement";
            case "poste":
                return "poste_travail";
            case "employe":
                return "contrat";
            case "categorie":
                return "convention";
        }
        return "";
    }

    public String getElementObject(Row maRow) {
        if (maRow != null) {
            String obj = maRow.getCell(0).getStringCellValue().trim();
            return getElementObject(obj);
        }
        return "";
    }

    public String getElementObject(HSSFRow maRow) {
        if (maRow != null) {
            String obj = maRow.getCell(0).getStringCellValue().trim();
            return getElementObject(obj);
        }
        return "";
    }

    public String getElementObject(XSSFRow maRow) {
        if (maRow != null) {
            String obj = maRow.getCell(0).getStringCellValue().trim();
            return getElementObject(obj);
        }
        return "";
    }

    public String getElementObject(Element racine) {
        if (racine != null) {
            String obj = racine.getNodeName();
            switch (obj) {
                case "tabl_articles":
                    return "article";
                case "tabl_regle_salaire":
                    return "regle_salaire";
                case "tabl_inventaire":
                    return "inventaire";
            }
        }
        return "";
    }

    public String getElementObject(String[] maRow) {
        if (maRow != null) {
            String obj = maRow[0];
            return getElementObject(obj);
        }
        return "";
    }

    public void importData() {
        switch (objetImport) {
            case "article":
                importDataArticle();
                break;
            case "plan_comptable":
                importDataCompte();
                break;
            case "inventaire":
                importDataInventaire();
                break;
            case "convention":
                importDataConvention();
                break;
            case "regle_salaire":

                break;
            case "employe":
                importDataEmploye();
                break;
            case "departement":
                importDataDepartement();
                break;
            case "poste_travail":
                importDataPoste();
                break;
            case "contrat":
                importDataContrat();
                break;
        }
    }

    public void importDataContrat() {
        try {
            YvsGrhEmployes employe;
            YvsBaseModeReglement model;
            YvsTypeContrat type;
            for (YvsGrhContratEmps d : listImportContrats) {
                employe = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByReference", new String[]{"matricule", "societe"}, new Object[]{d.getEmploye().getMatricule().trim(), currentAgence.getSociete()});
                if (employe != null ? employe.getId() < 1 : true) {
                    continue;
                }
                if (d.getModePaiement() != null ? Util.asString(d.getModePaiement().getDesignation()) : false) {
                    model = (YvsBaseModeReglement) dao.loadOneByNameQueries("YvsBaseModeReglement.findByDesignation", new String[]{"designation", "societe"}, new Object[]{d.getModePaiement().getDesignation().trim(), currentAgence.getSociete()});
                    if (model != null ? model.getId() < 1 : true) {
                        model = new YvsBaseModeReglement(null, d.getModePaiement().getDesignation());
                        model.setAuthor(currentUser);
                        model.setActif(true);
                        model = (YvsBaseModeReglement) dao.save1(model);
                    }
                    d.setModePaiement(model);
                } else {
                    d.setModePaiement(null);
                }
                if (d.getTypeContrat() != null ? Util.asString(d.getTypeContrat().getLibelle()) : false) {
                    type = (YvsTypeContrat) dao.loadOneByNameQueries("YvsTypeContrat.findByLibelle", new String[]{"libelle", "societe"}, new Object[]{d.getTypeContrat().getLibelle().trim(), currentAgence.getSociete()});
                    if (type != null ? type.getId() < 1 : true) {
                        type = new YvsTypeContrat(null, d.getModePaiement().getDesignation());
                        type.setAuthor(currentUser);
                        type.setActif(true);
                        type = (YvsTypeContrat) dao.save1(type);
                    }
                    d.setTypeContrat(type);
                } else {
                    d.setTypeContrat(null);
                }
                d.setId(null);
                d.setActif(true);
                d.setEmploye(employe);
                d.setAuthor(currentUser);
                dao.save1(d);
            }
            succes();
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importDataEmploye() {
        try {
            YvsGrhDepartement department;
            YvsGrhPosteDeTravail poste;
            YvsAgences agence;
            for (YvsGrhEmployes d : listImportEmployes) {
                if (d.getPosteActif() != null ? Util.asString(d.getPosteActif().getIntitule()) : false) {
                    poste = (YvsGrhPosteDeTravail) dao.loadOneByNameQueries("YvsPosteDeTravail.findByIntitule", new String[]{"intitule", "societe"}, new Object[]{d.getPosteActif().getIntitule(), currentAgence.getSociete()});
                    if (poste != null ? poste.getId() < 1 : true) {
                        department = (YvsGrhDepartement) dao.loadOneByNameQueries("YvsGrhDepartement.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                        if (department != null ? department.getId() < 1 : true) {
                            continue;
                        }
                        poste = new YvsGrhPosteDeTravail(null);
                        poste.setId(null);
                        poste.setIntitule(d.getPosteActif().getIntitule());
                        poste.setDepartement(department);
                        poste.setAuthor(currentUser);
                        poste.setActif(true);
                        poste = (YvsGrhPosteDeTravail) dao.save1(poste);
                    }
                    d.setPosteActif(poste);
                } else {
                    d.setPosteActif(null);
                }
                agence = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findByCodeagence", new String[]{"codeagence", "societe"}, new Object[]{d.getAgence().getCodeagence(), currentAgence.getSociete()});
                if (agence != null ? agence.getId() < 1 : true) {
//                    agence = new YvsAgences();
//                    agence.setId(null);
//                    agence.setCodeagence(d.getAgence().getCodeagence());
//                    agence.setDesignation(d.getAgence().getCodeagence());
//                    agence.setAbbreviation(d.getAgence().getCodeagence());
//                    agence.setSociete(currentAgence.getSociete());
//                    agence.setAuthor(currentUser);
//                    agence.setActif(true);
//                    agence = (YvsAgences) dao.save1(agence);
                    d.setAgence(currentAgence);
//                    continue;
                }
                d.setId(null);
                d.setActif(true);
                d.setAuthor(currentUser);
                d.setAgence(agence);
                dao.save1(d);
            }
            succes();
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importDataPoste() {
        try {
            YvsGrhDepartement department;
            for (YvsGrhPosteDeTravail d : listImportPostes) {
                department = (YvsGrhDepartement) dao.loadOneByNameQueries("YvsGrhDepartement.findByReference", new String[]{"code", "societe"}, new Object[]{d.getDepartement().getCodeDepartement(), currentAgence.getSociete()});
                if (department != null ? department.getId() < 1 : true) {
                    department = new YvsGrhDepartement();
                    department.setId(null);
                    department.setCodeDepartement(d.getDepartement().getCodeDepartement());
                    department.setIntitule(d.getDepartement().getCodeDepartement());
                    department.setAbreviation(d.getDepartement().getCodeDepartement());
                    department.setSociete(currentAgence.getSociete());
                    department.setAuthor(currentUser);
                    department.setActif(true);
                    department = (YvsGrhDepartement) dao.save1(department);
                }
                d.setId(null);
                d.setActif(true);
                d.setAuthor(currentUser);
                d.setDepartement(department);
                dao.save1(d);
            }
            succes();
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importDataDepartement() {
        try {
            YvsGrhDepartement department;
            for (YvsGrhDepartement d : listImportDepartements) {
                if (d.getDepartementParent() != null ? Util.asString(d.getDepartementParent().getCodeDepartement()) : false) {
                    department = (YvsGrhDepartement) dao.loadOneByNameQueries("YvsGrhDepartement.findByReference", new String[]{"code", "societe"}, new Object[]{d.getDepartementParent().getCodeDepartement(), currentAgence.getSociete()});
                    if (department != null ? department.getId() < 1 : true) {
                        department = new YvsGrhDepartement();
                        department.setId(null);
                        department.setCodeDepartement(d.getDepartementParent().getCodeDepartement());
                        department.setIntitule(d.getDepartementParent().getCodeDepartement());
                        department.setAbreviation(d.getDepartementParent().getCodeDepartement());
                        department.setSociete(currentAgence.getSociete());
                        department.setAuthor(currentUser);
                        department.setActif(true);
                        department = (YvsGrhDepartement) dao.save1(department);
                    }
                    d.setDepartementParent(department);
                } else {
                    d.setDepartementParent(null);
                }
                d.setId(null);
                d.setActif(true);
                d.setAuthor(currentUser);
                d.setSociete(currentAgence.getSociete());
                dao.save1(d);
            }
            succes();
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importDataConvention() {
        try {
            YvsGrhConventionCollective convention;
            YvsGrhCategorieProfessionelle categorie = null;
            YvsGrhEchelons echelon = null;
            YvsGrhSecteurs secteur = null;
            for (YvsGrhConventionCollective c : listImportConventions) {
                if (categorie != null ? !categorie.getCategorie().equals(c.getCategorie().getCategorie()) : true) {
                    categorie = (YvsGrhCategorieProfessionelle) dao.loadOneByNameQueries("YvsCategorieProfessionelle.findByCategorieSociete", new String[]{"categorie", "societe"}, new Object[]{c.getCategorie().getCategorie(), currentAgence.getSociete()});
                    if (categorie != null ? categorie.getId() < 1 : true) {
                        categorie = new YvsGrhCategorieProfessionelle();
                        categorie.setAuthor(currentUser);
                        categorie.setCategorie(c.getCategorie().getCategorie());
                        categorie.setSociete(currentAgence.getSociete());
                        categorie = (YvsGrhCategorieProfessionelle) dao.save1(categorie);
                    }
                }
                c.setCategorie(categorie);

                if (echelon != null ? !echelon.getEchelon().equals(c.getEchelon().getEchelon()) : true) {
                    echelon = (YvsGrhEchelons) dao.loadOneByNameQueries("YvsEchelons.findByEchelonSociete", new String[]{"echelon", "societe"}, new Object[]{c.getEchelon().getEchelon(), currentAgence.getSociete()});
                    if (echelon != null ? echelon.getId() < 1 : true) {
                        echelon = new YvsGrhEchelons();
                        echelon.setAuthor(currentUser);
                        echelon.setEchelon(c.getEchelon().getEchelon());
                        echelon.setSociete(currentAgence.getSociete());
                        echelon = (YvsGrhEchelons) dao.save1(echelon);
                    }
                }
                c.setEchelon(echelon);

                if (secteur != null ? !secteur.getNom().equals(c.getYvsSecteurs().getNom()) : true) {
                    secteur = (YvsGrhSecteurs) dao.loadOneByNameQueries("YvsSecteurs.findByNomSociete", new String[]{"nom", "societe"}, new Object[]{c.getYvsSecteurs().getNom(), currentAgence.getSociete()});
                    if (secteur != null ? secteur.getId() < 1 : true) {
                        secteur = new YvsGrhSecteurs();
                        secteur.setAuthor(currentUser);
                        secteur.setNom(c.getYvsSecteurs().getNom());
                        secteur.setSociete(currentAgence.getSociete());
                        secteur = (YvsGrhSecteurs) dao.save1(secteur);
                    }
                }
                c.setYvsSecteurs(secteur);

                convention = (YvsGrhConventionCollective) dao.loadOneByNameQueries("YvsConventionCollective.findByCE", new String[]{"categorie", "echelon", "secteur"}, new Object[]{categorie, echelon, secteur});
                if (convention != null ? convention.getId() < 1 : true) {
                    c.setAuthor(currentUser);
                    dao.save1(c);
                    c.setNew_(c != null ? c.getId() > 0 : false);
                }
            }
            succes();
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importDataInventaire() {
        try {
            YvsBaseArticles article;
            YvsBaseConditionnement unite;
            YvsBaseDepots depot;

            ManagedInventaire w = (ManagedInventaire) giveManagedBean(ManagedInventaire.class);
            if (w != null) {
                YvsComDocStocks docStockEntree = null, docStockSortie = null;
                YvsComDocStocks docStock = new YvsComDocStocks(0L);
                docStock.setActif(true);
                docStock.setAuthor(currentUser);
                docStock.setDateDoc(dateInventaire);
                docStock.setEditeur(currentUser.getUsers());
                docStock.setTypeDoc(Constantes.TYPE_IN);
                docStock.setNature(Constantes.INVENTAIRE);
                docStock.setSociete(currentAgence.getSociete());
                docStock.setStatut(Constantes.ETAT_EDITABLE);

                for (YvsComContenuDocStock c : listImportContenus) {
                    article = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findByCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), c.getArticle().getRefArt()});
                    if (article != null ? article.getId() > 0 : false) {
                        depot = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findByCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), c.getDocStock().getSource().getDesignation()});
                        if (depot != null ? depot.getId() > 0 : false) {
                            unite = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findByArticleReferenceUnite", new String[]{"article", "unite"}, new Object[]{article, c.getConditionnement().getUnite().getReference()});
                            if (unite != null ? unite.getId() > 0 : false) {
                                if (docStock.getId() < 1) {
                                    String numero = genererReference(Constantes.TYPE_IN_NAME, dateInventaire, depot.getId());
                                    if (numero == null || numero.trim().equals("")) {
                                        return;
                                    }
                                    docStock.setNumDoc(numero);
                                    docStock.setDestination(depot);
                                    docStock.setSource(depot);
                                    docStock.setHeureDoc(new Date());
                                    docStock.setCloturer(false);
                                    docStock.setAutomatique(false);
                                    docStock.setEtapeTotal(0);
                                    docStock.setEtapeValide(0);
                                    docStock.setId(null);
                                    docStock = (YvsComDocStocks) dao.save1(docStock);
                                }
                                if (docStock != null ? docStock.getId() > 0 : false) {
                                    double stock = dao.stocks(article.getId(), 0, depot.getId(), 0, 0, dateInventaire, unite.getId(), 0);
                                    double pr = dao.getPr(article.getId(), depot.getId(), 0, dateInventaire, unite.getId());
                                    double quantiteAttendu = c.getQuantite();
                                    double quantite = Math.abs(quantiteAttendu - stock);

                                    String type = null;
                                    if (c.getQuantite() > stock) {
                                        type = Constantes.TYPE_ES;
                                        if (docStockEntree != null ? docStockEntree.getId() < 1 : true) {
                                            Long id = docStock.getId();
                                            docStockEntree = w.saveDocInvenatire(docStock, new YvsComDocStocks(id), type);
                                            if (docStockEntree != null ? docStockEntree.getId() < 1 : true) {
                                                continue;
                                            }
                                        }
                                    } else if (c.getQuantite() < stock) {
                                        type = Constantes.TYPE_SS;
                                        if (docStockSortie != null ? docStockSortie.getId() < 1 : true) {
                                            Long id = docStock.getId();
                                            docStockSortie = w.saveDocInvenatire(docStock, new YvsComDocStocks(id), type);
                                            if (docStockSortie != null ? docStockSortie.getId() < 1 : true) {
                                                continue;
                                            }
                                        }
                                    }
                                    if (Util.asString(type)) {
                                        c.setId(null);
                                        c.setActif(true);
                                        c.setArticle(article);
                                        c.setAuthor(currentUser);
                                        c.setCalculPr(true);
                                        c.setConditionnement(unite);
                                        c.setConditionnementEntree(unite);
                                        c.setDateContenu(new Date());
                                        c.setDateReception(new Date());
                                        c.setPrix(pr);
                                        c.setPrixEntree(pr);
                                        c.setPrixTotal(quantite * pr);
                                        c.setQteAttente(quantite);
                                        c.setQteRestant(quantite);
                                        c.setQuantite(quantite);
                                        c.setQuantiteEntree(quantite);
                                        c.setQuantiteRecu(quantite);
                                        c.setStatut(Constantes.ETAT_EDITABLE);

                                        if (type.equals(Constantes.TYPE_ES)) {
                                            c.setDocStock(docStockEntree);
                                        } else if (type.equals(Constantes.TYPE_SS)) {
                                            c.setDocStock(docStockSortie);
                                        }
                                        c = (YvsComContenuDocStock) dao.save1(c);
                                        c.setQuantite(quantiteAttendu);
                                        c.setUpdate(type.equals(Constantes.TYPE_ES));
                                        c.setNew_(c != null ? c.getId() > 0 : false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            succes();
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importDataCompte() {
        try {
            YvsBaseNatureCompte autre = (YvsBaseNatureCompte) dao.loadOneByNameQueries("YvsBaseNatureCompte.findDesignation", new String[]{"designation", "societe"}, new Object[]{"AUTRE", currentAgence.getSociete()});
            if (autre != null ? autre.getId() < 1 : true) {
                autre = new YvsBaseNatureCompte();
                autre.setActif(true);
                autre.setDesignation("AUTRE");
                autre.setLettrable(false);
                autre.setNature("AUTRE");
                autre.setSensCompte("A");
                autre.setTypeReport("AUCUN");
                autre.setAuthor(currentUser);
                autre.setSociete(currentAgence.getSociete());
                autre = (YvsBaseNatureCompte) dao.save1(autre);
            }
            if (autre != null ? autre.getId() > 0 : false) {
                YvsBaseNatureCompte nature = null;
                for (YvsBasePlanComptable c : listImportComptes) {
                    String numero = c.getNumCompte();
                    String abbreviation = c.getNumCompte();
                    for (int i = c.getNumCompte().length(); i < 8; i++) {
                        numero += "0";
                    }
                    YvsBasePlanComptable y = (YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findByNumCompte", new String[]{"numCompte", "societe"}, new Object[]{numero, currentAgence.getSociete()});
                    if (y != null ? y.getId() < 1 : true) {
                        nature = null;
                        if (c.getNatureCompte() != null ? Util.asString(c.getNatureCompte().getDesignation()) : false) {
                            nature = (YvsBaseNatureCompte) dao.loadOneByNameQueries("YvsBaseNatureCompte.findDesignation", new String[]{"designation", "societe"}, new Object[]{c.getNatureCompte().getDesignation(), currentAgence.getSociete()});
                            if (nature != null ? nature.getId() < 1 : true) {
                                nature = new YvsBaseNatureCompte();
                                nature.setActif(true);
                                nature.setDesignation(c.getNatureCompte().getDesignation());
                                nature.setLettrable(false);
                                nature.setNature("AUTRE");
                                nature.setSensCompte("A");
                                nature.setTypeReport("AUCUN");
                                nature.setAuthor(currentUser);
                                nature.setSociete(currentAgence.getSociete());
                                nature = (YvsBaseNatureCompte) dao.save1(nature);
                            }
                        }
                        if (nature != null ? nature.getId() > 0 : false) {
                            c.setNatureCompte(nature);
                        } else {
                            c.setNatureCompte(autre);
                        }
                        c.setNumCompte(numero);
                        c.setAbbreviation(abbreviation);
                        c.setActif(true);
                        c.setSensCompte("A");
                        c.setTypeCompte("CO");
                        c.setTypeReport("AU");
                        c.setAuthor(currentUser);
                        c.setActif(true);
                        dao.save1(c);
                    } else {
                        System.out.println("le compte " + y + " existe deja");
                    }
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importDataArticle() {
        try {
            YvsBaseUniteMesure u;
            YvsBaseFamilleArticle f;
            YvsBaseArticles y;
            YvsBaseConditionnement c;
            for (YvsBaseConditionnement a : listImportArticles) {
                y = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findByCode", new String[]{"code", "societe"}, new Object[]{a.getArticle().getRefArt(), currentAgence.getSociete()});
                if (y != null ? y.getId() < 1 : true) {
                    f = (YvsBaseFamilleArticle) dao.loadOneByNameQueries("YvsBaseFamilleArticle.findByReference", new String[]{"code", "societe"}, new Object[]{a.getArticle().getFamille().getReferenceFamille(), currentAgence.getSociete()});
                    if (f != null ? f.getId() < 1 : true) {
                        f = new YvsBaseFamilleArticle();
                        f.setReferenceFamille(a.getArticle().getFamille().getReferenceFamille());
                        f.setDesignation(a.getArticle().getFamille().getReferenceFamille());
                        f.setActif(true);
                        f.setSociete(currentAgence.getSociete());
                        f.setAuthor(currentUser);
                        f = (YvsBaseFamilleArticle) dao.save1(f);
                    }
                    if (f != null ? f.getId() < 1 : true) {
                        continue;
                    }
                    if (!Util.asString(a.getArticle().getRefArt())) {
                        genererReference(a.getArticle(), a.getArticle().getFamille().getReferenceFamille());
                    }
                    y = new YvsBaseArticles();
                    y.setRefArt(a.getArticle().getRefArt());
                    y.setDesignation(a.getArticle().getDesignation());
                    y.setFamille(f);
                    y.setChangePrix(true);
                    y.setPuaTtc(true);
                    y.setPuvTtc(true);
                    y.setSuiviEnStock(true);
                    y.setMethodeVal(Constantes.CMP1);
                    y.setAuthor(currentUser);
                    y.setActif(true);
                    y.setPua(a.getPrixAchat());
                    y.setPuv(a.getPrix());
                    y.setPrixMin(a.getPrixMin());
                    y.setCategorie(Constantes.CAT_MARCHANDISE);
                    y = (YvsBaseArticles) dao.save1(y);
                } else {
                    y.setPua(a.getPrixAchat());
                    y.setPrixMin(a.getPrixMin());
                    y.setPuv(a.getPrix());
                    dao.update(y);
                }
                a.setArticle(y);
                if (y != null ? y.getId() < 1 : true) {
                    continue;
                }

                u = (YvsBaseUniteMesure) dao.loadOneByNameQueries("YvsBaseUniteMesure.findByCode", new String[]{"societe", "reference"}, new Object[]{currentAgence.getSociete(), a.getUnite().getReference()});
                if (u != null ? u.getId() < 1 : true) {
                    u = new YvsBaseUniteMesure();
                    u.setReference(a.getUnite().getReference());
                    u.setLibelle(a.getUnite().getReference());
                    u.setType(Constantes.UNITE_QUANTITE);
                    u.setSociete(currentAgence.getSociete());
                    u.setAuthor(currentUser);
                    u = (YvsBaseUniteMesure) dao.save1(u);
                }
                if (u != null ? u.getId() < 1 : true) {
                    continue;
                }

                c = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findByArticleUnite", new String[]{"article", "unite"}, new Object[]{y, u});
                if (c != null ? c.getId() < 1 : true) {
                    c = new YvsBaseConditionnement(null, y, u);
                    c.setAuthor(currentUser);
                    c.setPrix(a.getPrix());
                    c.setPrixAchat(a.getPrixAchat());
                    c.setPrixMin(a.getPrixMin());
                    c.setNaturePrixMin(Constantes.NATURE_MTANT);
                    c.setByVente(true);
                    c.setDefaut(true);
                    dao.save(c);
                } else {
                    c.setPrix(a.getPrix());
                    c.setPrixAchat(a.getPrixAchat());
                    c.setPrixMin(a.getPrixMin());
                    c.setNaturePrixMin(Constantes.NATURE_MTANT);
                    c.setByVente(true);
                    c.setDefaut(true);
                    dao.update(c);
                }
            }
            succes();
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void exportDataFromCSV() throws IOException {
        List<String[]> data = new ArrayList<>();
        String[] title = {"username", "password"};
        boolean add = data.add(title);
        if (add) {
            String[] entree = {"myusername", "mypassword"};
            data.add(entree);
        }
        if (data != null) {
            Util.createFileCSV("F:\\test.csv", data);
        }
    }

    @Override
    public boolean controleFiche(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void chooseState(ValueChangeEvent ev) {
        if (ev != null) {
            colonnes.clear();
            switch ((Integer) ev.getNewValue()) {
                case 1: //article
                    colonnes.add("reference");
                    colonnes.add("designation");
                    colonnes.add("unite");
                    colonnes.add("pua");
                    colonnes.add("puv");
                    colonnes.add("famille");
                    break;
                case 2: //plan comptable
                    colonnes.add("numero");
                    colonnes.add("intitule");
                    colonnes.add("nature");
                    break;
                case 3: //inventaire
                    colonnes.add("article");
                    colonnes.add("unite");
                    colonnes.add("quantite");
                    colonnes.add("depot");
                    break;
                case 4: //employe
                    colonnes.add("matricule");
                    colonnes.add("civilite");
                    colonnes.add("nom");
                    colonnes.add("prenom");
                    colonnes.add("matricule social");
                    colonnes.add("adresse");
                    colonnes.add("date de naissance");
                    colonnes.add("date d'embauche");
                    colonnes.add("poste");
                    colonnes.add("photo");
                    colonnes.add("agence");
                    break;
                case 5: //departements
                    colonnes.add("departement");
                    colonnes.add("abbreviation");
                    colonnes.add("intitule");
                    colonnes.add("parent");
                    colonnes.add("visible");
                    break;
                case 6: //poste de travail
                    colonnes.add("poste");
                    colonnes.add("departement");
                    colonnes.add("grade");
                    break;
                case 7: //contrats
                    break;
                case 8: //convention
                    break;
            }
        }
    }
}

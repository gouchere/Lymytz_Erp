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
import java.util.Stack;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.compta.YvsComptaPhaseReglement;
import yvs.entity.compta.salaire.YvsComptaCaissePieceSalaire;
import yvs.entity.compta.salaire.YvsComptaNotifReglementBulletin;
import yvs.entity.compta.salaire.YvsComptaPhasePieceSalaire;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.salaire.YvsGrhBulletins;
import yvs.entity.grh.salaire.YvsGrhDetailBulletin;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.grh.salaire.YvsGrhElementFormuleNonInterprete;
import yvs.entity.grh.salaire.YvsGrhElementSalaire;
import yvs.entity.grh.salaire.YvsGrhHeaderBulletin;
import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
import yvs.entity.grh.salaire.YvsGrhParametresBulletin;
import yvs.entity.grh.salaire.YvsGrhPlanifSalaireContrat;
import yvs.entity.grh.salaire.YvsGrhRubriqueBulletin;
import yvs.entity.param.YvsAgences;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.ContratEmploye;
import yvs.grh.bean.Employe;
import yvs.grh.paie.evalExpression.UtilFormules;
import yvs.grh.paie.evalExpression.UtilsTest;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.mutuelle.ManagedExercice;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Utilitaire;

/**
 *
 * @author LYMYTZ classe qui permet d'analyser et d'evaluer une expression
 * mathématique
 */
@ManagedBean
@SessionScoped
public class ManagedSalaire extends Managed<DetailsBulletin, YvsGrhBulletins> implements Serializable {

    private List<YvsGrhBulletins> listBulletins;
    private YvsGrhBulletins selectBull = new YvsGrhBulletins();
    private List<YvsGrhContratEmps> listContratEmployes;
    private List<YvsGrhElementFormuleNonInterprete> reglesabsentes;
    private boolean initForm = true;

    private final String PLUS = "+", MOINS = "-", FOIS = "*", DIV = "/";
    private final String INF = "<", SUP = ">", EGAL = "=", ET = "&", OU = "|";
    private final String OUVRANT = "(", FERMANT = ")", POURCENT = "%";
    private Stack<String> pile;
    private final String patternVariable = "^-?[a-bA-Z0-9]+$";//toutes les variables doivent respecter ce pattern       
    private boolean displayTable, displayTabBulletin, selectBulletin;
    private BulletinSalaire currentBulletin = new BulletinSalaire();

    private List<YvsBaseExercice> exercices;

    private PaginatorResult<YvsGrhOrdreCalculSalaire> p_entete = new PaginatorResult<>();
    private Boolean cloturerSearch, comptaSearch;
    private long nbrComptaSearch;
    private boolean dateSearch;
    private Date dateDebutSearch = new Date(), dateFinSearch = new Date();
    private String numSearch;
    private Long idExercice;

    private long idParamSalaire;
    private String codeParamSal;
    private String descParamSal;

    /**
     * Attribut pour le calcul du salaire
     */
    private Date debut = new Date(), fin = new Date();

//    private List<Employe> selectionEmployes;
    private List<yvs.util.Options> erreurs;

    private List<DetailsBulletin> bulletins, detailsBulletin; //liste des éléments du bulletin courant

    private String chaineSelectBulletin;
    private String refAction = "A";

    private OrdreCalculSalaire ordreCalcul = new OrdreCalculSalaire();
    private String chaineSelectOc;
    private List<YvsGrhOrdreCalculSalaire> listPlanification;
    private String chaineSelectEmploye, tabIds;
    private List<ElementSalaire> reglesExcluConge;
    private YvsGrhElementSalaire regleArrondi;

    public UtilFormules formule;

    private List<Long> listIdContratsRestreint;

    private YvsGrhBulletins selectedBulletin;

    private boolean deleteAllDoublon;

    public ManagedSalaire() {
        pile = new Stack<>();
        listBulletins = new ArrayList<>();
        reglesabsentes = new ArrayList<>();
        employesNonTraite = new ArrayList<>();
        exercices = new ArrayList<>();
        //detecter les bornes de mois
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMinimum(Calendar.DAY_OF_MONTH));
        debut = ca.getTime();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        fin = ca.getTime();
//        selectionEmployes = new ArrayList<>();
        erreurs = new ArrayList<>();
        bulletins = new ArrayList<>();
        detailsBulletin = new ArrayList<>();
        listPlanification = new ArrayList<>();
        listContratEmployes = new ArrayList<>();
        ordreCalcul.setDebutMois(debut);
        ordreCalcul.setFinMois(fin);
        reglesExcluConge = new ArrayList<>();
        listeParametre = new ArrayList<>();
    }

    public List<YvsGrhElementFormuleNonInterprete> getReglesabsentes() {
        return reglesabsentes;
    }

    public void setReglesabsentes(List<YvsGrhElementFormuleNonInterprete> reglesabsentes) {
        this.reglesabsentes = reglesabsentes;
    }

    public List<YvsBaseExercice> getExercices() {
        return exercices;
    }

    public void setExercices(List<YvsBaseExercice> exercices) {
        this.exercices = exercices;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public long getNbrComptaSearch() {
        return nbrComptaSearch;
    }

    public void setNbrComptaSearch(long nbrComptaSearch) {
        this.nbrComptaSearch = nbrComptaSearch;
    }

    public Boolean getComptaSearch() {
        return comptaSearch;
    }

    public void setComptaSearch(Boolean comptaSearch) {
        this.comptaSearch = comptaSearch;
    }

    public YvsGrhBulletins getSelectBull() {
        return selectBull;
    }

    public void setSelectBull(YvsGrhBulletins selectBull) {
        this.selectBull = selectBull;
    }

    public boolean isDeleteAllDoublon() {
        return deleteAllDoublon;
    }

    public void setDeleteAllDoublon(boolean deleteAllDoublon) {
        this.deleteAllDoublon = deleteAllDoublon;
    }

    public boolean isInitForm() {
        return initForm;
    }

    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }

    public Stack<String> getPile() {
        return pile;
    }

    public void setPile(Stack<String> pile) {
        this.pile = pile;
    }

    public PaginatorResult<YvsGrhOrdreCalculSalaire> getP_entete() {
        return p_entete;
    }

    public void setP_entete(PaginatorResult<YvsGrhOrdreCalculSalaire> p_entete) {
        this.p_entete = p_entete;
    }

    public Boolean getCloturerSearch() {
        return cloturerSearch;
    }

    public void setCloturerSearch(Boolean cloturerSearch) {
        this.cloturerSearch = cloturerSearch;
    }

    public boolean isDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(boolean dateSearch) {
        this.dateSearch = dateSearch;
    }

    public Date getDateDebutSearch() {
        return dateDebutSearch;
    }

    public void setDateDebutSearch(Date dateDebutSearch) {
        this.dateDebutSearch = dateDebutSearch;
    }

    public Date getDateFinSearch() {
        return dateFinSearch;
    }

    public void setDateFinSearch(Date dateFinSearch) {
        this.dateFinSearch = dateFinSearch;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public List<yvs.util.Options> getErreurs() {
        return erreurs;
    }

    public void setErreurs(List<yvs.util.Options> erreurs) {
        this.erreurs = erreurs;
    }

    public List<ElementSalaire> getReglesExcluConge() {
        return reglesExcluConge;
    }

    public void setReglesExcluConge(List<ElementSalaire> reglesExcluConge) {
        this.reglesExcluConge = reglesExcluConge;
    }

    public YvsGrhElementSalaire getRegleArrondi() {
        return regleArrondi;
    }

    public void setRegleArrondi(YvsGrhElementSalaire regleArrondi) {
        this.regleArrondi = regleArrondi;
    }

    public UtilFormules getFormule() {
        return formule;
    }

    public void setFormule(UtilFormules formule) {
        this.formule = formule;
    }

    public List<Long> getListIdContratsRestreint() {
        return listIdContratsRestreint;
    }

    public void setListIdContratsRestreint(List<Long> listIdContratsRestreint) {
        this.listIdContratsRestreint = listIdContratsRestreint;
    }

    public YvsParametreGrh getParam() {
        return param;
    }

    public void setParam(YvsParametreGrh param) {
        this.param = param;
    }

    public UtilsTest getUtil() {
        return util;
    }

    public void setUtil(UtilsTest util) {
        this.util = util;
    }

    public Employe getCurrentEmps() {
        return currentEmps;
    }

    public void setCurrentEmps(Employe currentEmps) {
        this.currentEmps = currentEmps;
    }

    public int getOffsetEmps() {
        return offsetEmps;
    }

    public void setOffsetEmps(int offsetEmps) {
        this.offsetEmps = offsetEmps;
    }

    public YvsGrhContratEmps getContrat() {
        return contrat;
    }

    public void setContrat(YvsGrhContratEmps contrat) {
        this.contrat = contrat;
    }

    public boolean isContinueSave() {
        return continueSave;
    }

    public void setContinueSave(boolean continueSave) {
        this.continueSave = continueSave;
    }

    public double getCumulArrondi() {
        return cumulArrondi;
    }

    public void setCumulArrondi(double cumulArrondi) {
        this.cumulArrondi = cumulArrondi;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public YvsGrhDetailBulletin getLineRecap() {
        return lineRecap;
    }

    public void setLineRecap(YvsGrhDetailBulletin lineRecap) {
        this.lineRecap = lineRecap;
    }

    public boolean isUpdateOrdre() {
        return updateOrdre;
    }

    public void setUpdateOrdre(boolean updateOrdre) {
        this.updateOrdre = updateOrdre;
    }

    public long getI() {
        return i;
    }

    public void setI(long i) {
        this.i = i;
    }

    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }

    public void setSelectedBulletin(YvsGrhBulletins selectedBulletin) {
        this.selectedBulletin = selectedBulletin;
    }

    public YvsGrhBulletins getSelectedBulletin() {
        return selectedBulletin;
    }

    public String getRefAction() {
        return refAction;
    }

    public void setRefAction(String refAction) {
        this.refAction = refAction;
    }

    public List<YvsGrhContratEmps> getListContratEmployes() {
        return listContratEmployes;
    }

    public void setListContratEmployes(List<YvsGrhContratEmps> listContratEmployes) {
        this.listContratEmployes = listContratEmployes;
    }

    public List<YvsGrhOrdreCalculSalaire> getListPlanification() {
        return listPlanification;
    }

    public void setListPlanification(List<YvsGrhOrdreCalculSalaire> listPlanification) {
        this.listPlanification = listPlanification;
    }

    public String getChaineSelectOc() {
        return chaineSelectOc;
    }

    public void setChaineSelectOc(String chaineSelectOc) {
        this.chaineSelectOc = chaineSelectOc;
    }

    public OrdreCalculSalaire getOrdreCalcul() {
        return ordreCalcul;
    }

    public void setOrdreCalcul(OrdreCalculSalaire ordreCalcul) {
        this.ordreCalcul = ordreCalcul;
    }

    public String getChaineSelectBulletin() {
        return chaineSelectBulletin;
    }

    public void setChaineSelectBulletin(String chaineSelectBulletin) {
        this.chaineSelectBulletin = chaineSelectBulletin;
    }

    public boolean isSelectBulletin() {
        return selectBulletin;
    }

    public void setSelectBulletin(boolean selectBulletin) {
        this.selectBulletin = selectBulletin;
    }

    public List<DetailsBulletin> getBulletins() {
        return bulletins;
    }

    public void setBulletins(List<DetailsBulletin> bulletins) {
        this.bulletins = bulletins;
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

    public boolean isDisplayTable() {
        return displayTable;
    }

    public void setDisplayTable(boolean displayTable) {
        this.displayTable = displayTable;
    }

    public BulletinSalaire getCurrentBulletin() {
        return currentBulletin;
    }

    public void setCurrentBulletin(BulletinSalaire currentBulletin) {
        this.currentBulletin = currentBulletin;
    }

    public List<YvsGrhBulletins> getListBulletins() {
        return listBulletins;
    }

    public void setListBulletins(List<YvsGrhBulletins> listBulletins) {
        this.listBulletins = listBulletins;
    }

    public List<DetailsBulletin> getDetailsBulletin() {
        return detailsBulletin;
    }

    public void setDetailsBulletin(List<DetailsBulletin> detailsBulletin) {
        this.detailsBulletin = detailsBulletin;
    }

    public String getChaineSelectEmploye() {
        return chaineSelectEmploye;
    }

    public void setChaineSelectEmploye(String chaineSelectEmploye) {
        this.chaineSelectEmploye = chaineSelectEmploye;
    }

    //renvoi true si la chaine passé en paramètre est un opérateur
    private boolean operateur(String str) {
        return str.equals(PLUS) || str.equals(MOINS) || str.equals(FOIS) || str.equals(DIV)
                || str.equals(SUP) || str.equals(INF) || str.equals(EGAL) || str.equals(ET) || str.equals(OU);
    }

    //renvoi true si la chaine passé en paramètre est une variable
    private boolean variable(String str) {
        return Pattern.matches(patternVariable, str);
    }

    //renvoi true si la chaine passé en paramètre est un opérateur
    private boolean ouvrante(String str) {
        return str.equals(OUVRANT);
    }

    private boolean fermante(String str) {
        return str.equals(FERMANT);
    }

    private boolean pourcentage(String str) {
        return Pattern.matches(POURCENT, str);
    }

    public boolean isDisplayTabBulletin() {
        return displayTabBulletin;
    }

    public void setDisplayTabBulletin(boolean displayTabBulletin) {
        this.displayTabBulletin = displayTabBulletin;
    }

    public Long getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Long idExercice) {
        this.idExercice = idExercice;
    }

    public long getIdParamSalaire() {
        return idParamSalaire;
    }

    public void setIdParamSalaire(long idParamSalaire) {
        this.idParamSalaire = idParamSalaire;
    }

    public String getCodeParamSal() {
        return codeParamSal;
    }

    public void setCodeParamSal(String codeParamSal) {
        this.codeParamSal = codeParamSal;
    }

    public String getDescParamSal() {
        return descParamSal;
    }

    public void setDescParamSal(String descParamSal) {
        this.descParamSal = descParamSal;
    }

    YvsParametreGrh param;

    public void loadExercicesActif() {
        exercices = giveListExerciceActif();
    }

    public void positionneDate() {
        if (currentBulletin.getEntete().getId() <= 0) {
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            param = (YvsParametreGrh) dao.loadOneByNameQueries("YvsParametreGrh.findAll", champ, val);
            if (param != null) {
                Calendar deb = Calendar.getInstance();
                Calendar now = Calendar.getInstance();
                if (param.getDatePaiementSalaire() != null) {
                    deb.setTime(param.getDatePaiementSalaire());
                    deb.set(Calendar.MONTH, now.get(Calendar.MONTH));
                    deb.set(Calendar.YEAR, now.get(Calendar.YEAR));
                    debut = deb.getTime();
                    deb.add(Calendar.MONTH, 1);
                    deb.add(Calendar.DAY_OF_MONTH, -1);
                    fin = deb.getTime();
                    ordreCalcul.setDebutMois(debut);
                    ordreCalcul.setFinMois(fin);
                }
            }
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(currentBulletin);
        currentBulletin.setContrat(new ContratEmploye());
        currentBulletin.setListDetails(new ArrayList<YvsGrhDetailBulletin>());
        currentBulletin.setListElementApercu(new ArrayList<YvsGrhRubriqueBulletin>());
        currentBulletin.setDateDebut(new Date());
        currentBulletin.setDateFin(new Date());
        currentBulletin.setEntete(new OrdreCalculSalaire());
        chaineSelectEmploye = null;
        selectBull = new YvsGrhBulletins();
    }

    public String convertECPToEPOST(String chaine) {
        String[] formules = chaine.split(" ");
        Stack<String> operator = new Stack<>(); //pile d'opérateurs
        Stack<String> out = new Stack<>();//pile en sortie
        boolean prio = false;
        for (String token : formules) {
            /*Si le token courant est un opérateur et que l'opérateur au sommet de la pile n'est pas prioritaire sur le token. 
             Il faut dépiler l'opérateur du sommet, le mettre sur la sortie, et empiler le token.*/
            if (!operator.isEmpty()) {
                if (("+".equals(token) || "-".equals(token)) && ("*".equals(operator.peek()) || "/".equals(operator.peek()))) {
                    out.push(operator.pop());
                    operator.push(token);
                    prio = true;
                }
            }
            if (prio == false) {
                try {
                    double nombre = Double.parseDouble(token);
                    out.push(nombre + "");
                } catch (NumberFormatException nfe) {
                    if (")".equals(token)) {
                        //Si le token courant est une parenthèse fermante, on dépile sur la sortie tant qu'on ne trouve pas de parenthèse ouvrante
                        while (!"(".equals(operator.peek())) {
                            out.push(operator.pop());
                        }
                        operator.pop();//enleve la parenthese
                    } else {
                        operator.push(token);
                    }
                }
            }

            prio = false;
        }

        //Si il n'y a plus de token à lire, on dépile sur la sortie tous les opérateurs restés dans la pile (sauf les parenthèses)
        while (operator.isEmpty() == false) {
            if (")".equals(operator.peek()) || "(".equals(operator.peek())) {
                operator.pop();//enleve la parenthese
            } else {
                out.push(operator.pop());
            }
        }
        String result = "";
        for (String st : out) {
            result += " " + st;
        }
        return result + " #";
    }

    //valide une expression complètement paranthésé
    /**
     * une ECP est valide
     *
     * @param expression
     * @return
     */
    public boolean valideExpression(String expression) {
        String[] ch = expression.split(" ");
        String cas;
        int i = 1, prec = 0;
        String chP = null;
        boolean correct = true;
        //contrôle la première expression
        if (fermante(ch[0]) || pourcentage(ch[0])) {
            return false;
        }
        while (!ch[i].equals("#")) {
            cas = ch[i];
            chP = ch[prec];
            switch (cas) {
                case DIV:
                case FOIS:
                case MOINS:
                case PLUS:
                    if (!fermante(chP) && !variable(chP) && !pourcentage(chP)) {
                        return false;
                    }
                    break;
                case OUVRANT:
                    //le caractère qui précède une parenthèse ouvrante peut être quelconque
                    if (!pourcentage(chP) && !variable(chP) && !fermante(chP) && !operateur(chP) && !ouvrante(chP)) {
                        return false;
                    }
                    break;
                case FERMANT:
                    if (!pourcentage(chP) && !variable(chP) && !fermante(chP)) {
                        return false;
                    }
                    break;
                case POURCENT:
                    if (!variable(chP) && !fermante(chP)) {
                        return false;
                    }
                    break;
                default:
                    //cas d'une variable
                    if (Pattern.matches(patternVariable, chP)) {
                        if (!operateur(chP) || !fermante(chP) || ouvrante(chP)) {
                            return false;
                        }
                    }
                    break;
            }
            prec++;
            i++;
        }
        return correct;
    }

    //on suppose que cette chaine nous fournie dirrectement les variable à évaluer
    /**
     * le principe: on empile les opérandes, et on dépile pour effectuer une
     * opération
     *
     * @param chaine
     * @return
     *
     */
    public String evalueExpression(String chaine) {
        String result = null;
        pile.clear();
        String ch[] = chaine.split(" ");
        int i = 0;
        while (!ch[i].equals("#")) {
            if (!operateur(ch[i])) {
                pile.push(ch[i]);
            } else {
                String str1 = pile.pop();
                String str2 = pile.pop();
                if (isANumber(str1) && isANumber(str2)) {
                    double op1 = Double.valueOf(str1);
                    double op2 = Double.valueOf(str2);
                    double r = calcul(ch[i], op1, op2);
                    pile.push("" + r);
                } else {
                    erreurs.add(new yvs.util.Options(str2 + ", " + str1, "Les valeurs ne sont pas des nombres"));
                    return null;
                }
            }
            i++;
        }
        if (!pile.isEmpty()) {
            result = (pile.pop());
        }
        return result;
    }

    public boolean evalueExpressionLogique(String chaine) {
        String result = null;
        pile.clear();
        String ch[] = chaine.split(" ");
        int i = 0;
        while (!ch[i].equals("#")) {
            if (!operateur(ch[i])) {
                pile.push(ch[i]);
            } else {
                String str1 = pile.pop();
                String str2 = pile.pop();
                if (isANumber(str1) && isANumber(str2)) {
                    double op1 = Double.valueOf(str1);
                    double op2 = Double.valueOf(str2);
                    double r = calculLogique(ch[i], op1, op2);
                    pile.push("" + r);
                } else {
                    return false;
                }
            }
            i++;
        }
        if (!pile.isEmpty()) {
            result = (pile.pop());
        }
        result = ((result != null) ? (result.isEmpty() ? "" : result) : "");
        int r = (int) Double.parseDouble((result.isEmpty()) ? "0" : result);
        return r == 1;
    }

    public static boolean isANumber(String s) {
        if (s == null) {
            return false;
        }
        try {
            new java.math.BigDecimal(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private double calculLogique(String signe, double d1, double d2) {
        switch (signe) {
            case INF:
                if (d2 < d1) {
                    return 1;
                } else {
                    return 0;
                }
            case SUP:
                if (d2 > d1) {
                    return 1;
                } else {
                    return 0;
                }
            case EGAL:
                if (d2 == d1) {
                    return 1;
                } else {
                    return 0;
                }
            case OU:
                return (int) (d1 + d2);
            case ET:
                return (int) (d1 * d2);
            default:
                return calcul(signe, d1, d2);
        }
    }

    private double calcul(String signe, double d1, double d2) {
        switch (signe) {
            case DIV:
                return d2 / d1;
            case PLUS:
                return d2 + d1;
            case FOIS:
                return d2 * d1;
            case MOINS:
                return d2 - d1;
            default:
                return 0;
        }
    }

    /**
     * une méthode qui substitue dans une expression un identifiant par sa
     * formule exemple: si X=a+b et Y=c+d et Z=X-Y la méthode doit écrire
     * l'expression Z=(a+b)-(c+d)
     *
     * @param lexeme
     * @param l
     * @return
     */
    UtilsTest util = new UtilsTest();

    Employe currentEmps;

    @Override
    public boolean controleFiche(DetailsBulletin bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {

    }

    @Override
    public DetailsBulletin recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(DetailsBulletin bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onSelectDistantOrdre(YvsGrhOrdreCalculSalaire y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObjectOrdre(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Bulletins de paye", "modRh", "smenBulletinS", true);
            }
        }
    }

    @Override
    public void onSelectDistant(YvsGrhBulletins y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Bulletins de paye", "modRh", "smenBulletinS", true);
            }
        }
    }

    public void onSelectObjectOrdre(YvsGrhOrdreCalculSalaire y) {
        idSearchBulletin = y.getId();
        initForm = true;
        paginator.addParam(new ParametreRequete("y.bulletin.entete", "header", y, "=", "AND"));
        loadAllBulletin(true);
        update("input_employe_bulletin");
        update("table_buletin_calcule");
    }

    @Override
    public void onSelectObject(YvsGrhBulletins y) {
        choixBulletin(y);
        update("input_employe_bulletin");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            YvsGrhBulletins b = (YvsGrhBulletins) ev.getObject();
            choixBulletin(b);
            chaineSelectBulletin = listBulletins.indexOf(b) + "";
        }

    }

    @Override
    public void loadAll() {
        positionneDate();
        //charge les règles exclus de la base de calcul du salaire
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        reglesExcluConge = UtilGrh.buildElementSalaire(dao.loadNameQueries("YvsGrhElementSalaire.findRegleExclus", champ, val));
        //Charge la règle arrodi
        regleArrondi = (YvsGrhElementSalaire) dao.loadOneByNameQueries("YvsGrhElementSalaire.findRegleArrondi_", champ, val);
        //charge les règles non interpreté
        reglesabsentes = dao.loadNameQueries("YvsGrhElementFormuleNonInterprete.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }
    private String matricule;

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    //recherche un employé en fonction de son nom, matricule, code utilisateur
    public void findEmploye(String matricule) {
        if (matricule != null) {
            champ = new String[]{"codeUsers", "agence"};
            val = new Object[]{"%" + matricule + "%", currentAgence};
            listContratEmployes = dao.loadNameQueries("YvsGrhContratEmps.findSearch", champ, val);
        }
    }

    /**
     * *
     * CHOIX D'UN EMPLOYE
     */
    private YvsGrhEmployes seleEmployeEmploye;
    private List<YvsGrhEmployes> employesNonTraite;

    public List<YvsGrhEmployes> getEmployesNonTraite() {
        return employesNonTraite;
    }

    public void setEmployesNonTraite(List<YvsGrhEmployes> employesNonTraite) {
        this.employesNonTraite = employesNonTraite;
    }

    public YvsGrhEmployes getSeleEmployeEmploye() {
        return seleEmployeEmploye;
    }

    public void setSeleEmployeEmploye(YvsGrhEmployes seleEmployeEmploye) {
        this.seleEmployeEmploye = seleEmployeEmploye;
    }

    public void choixEmploye(SelectEvent ev) {
        if (ev != null) {
            YvsGrhEmployes bean = (YvsGrhEmployes) ev.getObject();
            choixEmploye1(bean);
        }
    }

    public void choixEmploye1(YvsGrhEmployes em) {
        displayTable = false;
        contrat = em.getContrat();
        if (contrat != null) {
//            if (contrat.getStructureSalaire() != null) {
//                currentBulletin.setContrat(UtilGrh.buildBeanContratEmploye(contrat));
//                currentBulletin.setRefBulletin(giveReference());
//                currentBulletin.setAnciennete(Constantes.calculNbYear(contrat.getEmploye().getDateEmbauche(), fin) + "");
//            }
        }
    }

    public void directCalculSalaire(YvsGrhContratEmps contra) {
        if (contra != null) {
            if (contra.getStructureSalaire() != null) {
                currentBulletin.getContrat().getStructSalaire().setNom(contra.getStructureSalaire().getNom());
                currentBulletin.getContrat().setReference(contra.getReferenceContrat());
                currentBulletin.setRefBulletin(giveReference());
            } else {
                getErrorMessage("Cet employé ne possède aucune structure de salaire", "Veuillez en définir une !");
            }
        } else {
            getErrorMessage("L'employe n'a pas de contrat");
        }
    }

    int offsetEmps = 0;
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

//    private String codeExp;
    YvsGrhContratEmps contrat;

//    //récupère uniquement les élément qui peuvent être visible sur le bulletin
//    private List<YvsGrhElementStructure> filterElement(List<YvsGrhElementStructure> l) {
//        List<YvsGrhElementStructure> result = new ArrayList<>();
//        for (YvsGrhElementStructure e : l) {
//            if (e.getYvsElementSalaire().getVisibleBulletin()) {
//                result.add(e);
//            }
//        }
//        return result;
//    }
    private boolean continueSave;

    public void createBulletin(boolean continueSave) {
        compileBulletin();

    }
    double cumulArrondi = 0;

    public YvsGrhHeaderBulletin buildHeaderBulletin(YvsGrhBulletins b) {
        YvsGrhHeaderBulletin head = new YvsGrhHeaderBulletin();
        head.setAuthor(currentUser);
        head.setBulletin(b);
        head.setCategoriePro(contrat.getEmploye().getConvention().getCategorie().getCategorie());
        head.setEchelonPro(contrat.getEmploye().getConvention().getEchelon().getEchelon());
        head.setCni(contrat.getEmploye().getCni());
        head.setDateEmbauche(contrat.getEmploye().getDateEmbauche());
        head.setDepartement(contrat.getEmploye().getPosteActif().getDepartement().getIntitule());
        head.setEmail(((contrat.getEmploye().getMail1() != null) ? contrat.getEmploye().getMail1() : "") + "  " + ((contrat.getEmploye().getMail2() != null) ? contrat.getEmploye().getMail2() : ""));
        head.setMatricule(contrat.getEmploye().getMatricule());
        head.setMatriculeCnps((contrat.getEmploye().getMatriculeCnps() == null) ? "" : contrat.getEmploye().getMatriculeCnps());
        head.setPoste(contrat.getEmploye().getPosteActif().getIntitule());
        head.setTelephone(((contrat.getEmploye().getTelephone1() != null) ? contrat.getEmploye().getTelephone1() : "") + "  " + ((contrat.getEmploye().getTelephone2() != null) ? contrat.getEmploye().getTelephone2() : ""));
        head.setAdresseAgence(contrat.getEmploye().getAgence().getAdresse());
        head.setAdresseSociete(contrat.getEmploye().getAgence().getSociete().getAdressSiege());
        head.setCodeAgence(contrat.getEmploye().getAgence().getCodeagence());
        head.setCodeSociete(contrat.getEmploye().getAgence().getSociete().getCodeAbreviation());
        head.setDesignationAgence(contrat.getEmploye().getAgence().getDesignation());
        head.setNomEmploye(contrat.getEmploye().getNom() + " " + contrat.getEmploye().getPrenom());
        head.setCivilite(contrat.getEmploye().getCivilite());
        head.setAnciennete(Utilitaire.calculNbyear(b.getContrat().getEmploye().getDateEmbauche(), b.getDateFin()));
        head.setAdresseEmploye(contrat.getEmploye().getAdresse1());
        head.setModePaiement((contrat.getModePaiement() != null) ? ((contrat.getModePaiement().getDesignation() != null) ? contrat.getModePaiement().getDesignation() : "") : "");
        return head;
    }
    String month;

    private String giveReference() {
        if (debut != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(debut);
            month = ((c.get(Calendar.MONTH) + 1) > 9) ? "" + (c.get(Calendar.MONTH) + 1) : "0" + ((c.get(Calendar.MONTH) + 1));
            String year = c.get(Calendar.YEAR) + "";
            month += "-" + year;
            //cherche le dernier bulletin du  mois pour cet employé
            champ = new String[]{"mois", "contrat"};
            val = new Object[]{month, contrat};
            YvsGrhBulletins last = (YvsGrhBulletins) dao.loadOneByNameQueries("YvsGrhBulletins.findLastBp", champ, val);
            if (last != null) {
                numero = last.getNumero() + 1;
            } else {
                numero = 0;
            }
            return "BP/" + contrat.getEmploye().getNom() + "/" + month + "/0" + numero;
        } else {
            getErrorMessage("Veuillez choisir une période valide !");
            return null;
        }
    }

//    //fabrique le bulletin de salaire d'un employé à partir des règle de sa structure de salaire
    /**
     * **************************
     *
     */
    //ajouter un élément à la liste des élément du bulletin de paie
    public void addElmtBulletin() {
//        DetailsBulletin eb = new DetailsBulletin();
//        numero = numero + 1;
//        cloneObject(eb, eltBulletin);
//        eb.setId(numero);
//        bulletins.add(eb);
    }

    public void deleteEltBulletin(DetailsBulletin e) {
        bulletins.remove(e);
        update("teb-elt-bull");
    }

    /**
     * *****************
     */
    YvsGrhDetailBulletin lineRecap;

    public void loadAllBulletin() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        //charge les id des contrats limité
        listIdContratsRestreint = dao.loadNameQueries("YvsGrhContratEmps.findIdRestreint", champ, val);
        if (listIdContratsRestreint.isEmpty()) {
        } else {
            ParametreRequete p = new ParametreRequete("y.bulletin.contrat.id", "ids", listIdContratsRestreint, " NOT IN ", "AND");
            paginator.addParam(p);
        }
        loadAllBulletin(true);
    }

    public void loadAllBulletin(boolean avancer) {
        ParametreRequete p = new ParametreRequete("y.bulletin.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
        paginator.addParam(p);
        listBulletins = paginator.executeDynamicQuery("DISTINCT(y.bulletin)", "DISTINCT(y.bulletin)", "YvsGrhHeaderBulletin y", "y.bulletin.dateFin DESC, y.agence.id, y.bulletin.contrat.employe.nom", avancer, initForm, (int) imax, dao);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsGrhBulletins> re = paginator.parcoursDynamicData("YvsGrhHeaderBulletin ", "DISTINCT(y.bulletin)", "y", "y.bulletin.dateFin ,y.bulletin.contrat.employe.matricule", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void loadDuplicateBulletin() {
        if (idSearchBulletin > 0) {
            String rq = "SELECT DISTINCT e.id from yvs_grh_employes e where e.id in "
                    + "(SELECT c.employe  from yvs_grh_bulletins b INNER JOIN yvs_grh_contrat_emps c on b.contrat = c.id "
                    + "WHERE b.entete = ? GROUP BY c.employe HAVING COUNT(b.id)>1 )";
            List<Long> ids = dao.loadDataByNativeQuery(rq, new Object[]{idSearchBulletin});
            listBulletins = dao.loadNameQueries("YvsGrhBulletins.findByIds", new String[]{"ids"}, new Object[]{ids});
            update("table_buletin_calcule");
        } else {
            getErrorMessage("Vous devez-indiquer une période valide !");
        }
    }

    public void loadEmployesNotCompiled() {
        if (idSearchBulletin > 0) {
            String rq = "SELECT e.id from yvs_grh_employes e INNER JOIN yvs_agences a ON a.id=e.agence where e.id not in "
                    + "(SELECT c.employe FROM yvs_grh_bulletins b INNER JOIN yvs_grh_contrat_emps c on c.id=b.contrat "
                    + "INNER JOIN yvs_grh_employes e ON e.id=c.employe "
                    + "WHERE b.entete=?)  AND e.matricule NOT LIKE 'T%' AND a.societe=? AND e.actif IS true";
            List<Long> ids = dao.loadDataByNativeQuery(rq, new Object[]{idSearchBulletin, currentAgence.getSociete().getId()});
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                service.setListEmployes(dao.loadNameQueries("YvsGrhEmployes.findByIds", new String[]{"ids"}, new Object[]{ids}));
                update("salaire-tabEmployes");
                openDialog("dlgEmploye");
            }
        } else {
            getErrorMessage("Vous devez-indiquer une période valide !");
        }
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        initForm = false;
        loadAllBulletin(true);
    }

    public void changeMaxResult(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            imax = (long) ev.getNewValue();
            initForm = false;
            loadAllBulletin(true);
        }
    }

    public void pagineResult(boolean avancer) {
        initForm = false;
        loadAllBulletin(avancer);
    }

    public void choixBulletin(YvsGrhBulletins b) {
        selectBull = b;
        if (b != null) {
            if (!listPlanification.contains(b.getEntete())) {
                listPlanification.add(0, b.getEntete());
            }
            cloneObject(currentBulletin, UtilGrh.buildBeanBulletin(b, false));
            champ = new String[]{"bulletin"};
            val = new Object[]{b};
            currentBulletin.setListElementApercu(buildApercu(dao.loadNameQueries("YvsGrhMontanSalaire.findElementEvaluer", champ, val)));
            currentBulletin.setAnciennete(Utilitaire.calculNbyear(b.getContrat().getDateDebut(), b.getDateFin()));
            setSelectBulletin(true);
        } else {
            setSelectBulletin(false);
        }
        update("tabV-salaire:bulletins");
        update("entete_bulletin_00");
    }

    public List<YvsGrhRubriqueBulletin> buildApercu(List<YvsGrhDetailBulletin> l) {
        List<YvsGrhRubriqueBulletin> re = new ArrayList<>();
        YvsGrhRubriqueBulletin current = new YvsGrhRubriqueBulletin();
        int numRubrique = 0;
        for (YvsGrhDetailBulletin e : l) {
            if (!current.equals(e.getElement().getRubrique())) {
                //Ajoute une ligne de total de la rubrique
                if (!re.isEmpty()) {
                    lineRecap.setLineRecap(true);
                    lineRecap.setElement(new YvsGrhElementSalaire("Total " + re.get(numRubrique - 1).getDesignation()));
                    re.get(numRubrique - 1).getElementsBulletin().add(lineRecap);
                }
                lineRecap = new YvsGrhDetailBulletin();
                current = e.getElement().getRubrique();
                current.setElementsBulletin(new ArrayList<YvsGrhDetailBulletin>());
                current.getElementsBulletin().add(e);
                cumulRecap(e);
                re.add(current);
                numRubrique++;
                currentBulletin.setNetApayer(currentBulletin.getNetApayer() + e.getMontantPayer() - e.getRetenuSalariale());
            } else {
                re.get(re.indexOf(current)).getElementsBulletin().add(e);
                cumulRecap(e);
                currentBulletin.setNetApayer(currentBulletin.getNetApayer() + e.getMontantPayer() - e.getRetenuSalariale());
            }
        }
        return re;
    }

    private void cumulRecap(YvsGrhDetailBulletin d) {
        if (lineRecap != null) {
            lineRecap.setMontantEmployeur(lineRecap.getMontantEmployeur() + d.getMontantEmployeur());
            lineRecap.setMontantPayer(lineRecap.getMontantPayer() + d.getMontantPayer());
            lineRecap.setRetenuSalariale(lineRecap.getRetenuSalariale() + d.getRetenuSalariale());
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((chaineSelectBulletin != null) ? !chaineSelectBulletin.equals("") : false) {
                List<Integer> l = decomposeSelection(chaineSelectBulletin);
                List<YvsGrhBulletins> list = new ArrayList<>();
                YvsGrhBulletins bean;
                for (Integer ids : l) {
                    bean = listBulletins.get(ids);
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    deleteOneBulletin(bean, true);
                }
                listBulletins.removeAll(list);
                chaineSelectBulletin = "";
                succes();

            }
        } catch (Exception e) {
            getErrorMessage("Suppression Impossible ! Cet élement est déjà utiliser ");
            getException("Error Suppression ", e);
        }
    }

    public void deleteAllSelection() {
        if (!autoriser("grh_delete_bulletin")) {
            openNotAcces();
            return;
        }
        for (YvsGrhBulletins b : listBulletins) {
            deleteOneBulletin(b, true);
        }
        listBulletins.clear();
        succes();
    }

    public void deleteOneBulletin(YvsGrhBulletins b, boolean all) {
        try {
            if (b.getStatut() == Constantes.STATUT_DOC_EDITABLE || b.getStatut() == Constantes.STATUT_DOC_SUSPENDU) {
                if (!all) {
                    if (!autoriser("grh_delete_bulletin")) {
                        openNotAcces();
                        return;
                    }
                }
                if (b != null) {
                    try {
                        b.setAuthor(currentUser);
                        dao.delete(b);
                        if (!all) {
                            succes();
                            listBulletins.remove(b);
                        }
                    } catch (Exception ex) {
                        log.log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                getErrorMessage("Vous ne pouvez supprimer ce bulletin de paye !");
            }
        } catch (Exception e) {
            getErrorMessage("Suppression Impossible ! Cet élement est déjà utiliser ");
            getException("Error Suppression ", e);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void exporteBulletin() {
        YvsGrhBulletins b = giveBulletin();
        if (b != null) {
            Map<String, Object> param_ = new HashMap<>();
            param_.put("ID_BULLETIN", b.getId());
            param_.put("NOM_USER", currentUser.getUsers().getNomUsers());
            param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
            param_.put("PATH", returnLogo());
            executeReport("Bulletin_paie", param_, b.getContrat().getEmploye().getMatricule());
        } else {
            getErrorMessage("Impossible d'imprimer, votre selection est ambigue !");
        }
    }

    public void exporteOneBulletin(YvsGrhBulletins b) {
        if (b != null) {
            Map<String, Object> param_ = new HashMap<>();
            param_.put("ID_BULLETIN", b.getId());
            param_.put("NOM_USER", currentUser.getUsers().getNomUsers());
            param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
            param_.put("PATH", returnLogo());
            executeReport("Bulletin_paie", param_, b.getContrat().getEmploye().getMatricule());
        } else {
            getErrorMessage("Impossible d'imprimer, votre selection est ambigue !");
        }
    }

    public String exporteListBulletin() {
        if (idSearchBulletin < 1) {
            getErrorMessage("Vous devez selectionner une periode");
            return null;
        }
        if (!listBulletins.isEmpty()) {
            String ids = "0";
            for (YvsGrhBulletins a : listBulletins) {
                ids += "," + a.getId();
            }
            Map<String, Object> param_ = new HashMap<>();
            param_.put("ID_HEADER", idSearchBulletin);
            param_.put("ID_BULLETINS", ids);
            param_.put("NOM_USER", currentUser.getUsers().getNomUsers());
            param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
            param_.put("PATH", returnLogo());
            executeReport("all_bulletin", param_, "Paie_de_" + listPlanification.get(listPlanification.indexOf(new YvsGrhOrdreCalculSalaire(idSearchBulletin))).getReference());
        } else {
            getErrorMessage("Impossible d'imprimer, votre selection est ambigue !");
        }
        return null;
    }

    public String exporteALLBulletin() {
        if (idSearchBulletin < 1) {
            getErrorMessage("Vous devez selectionner une periode");
            return null;
        }
        if (!chaineSelectBulletin.isEmpty()) {
            List<Integer> le = decomposeSelection(chaineSelectBulletin);
            String ids = "0";
            for (Integer a : le) {
                ids += "," + listBulletins.get(a).getId();
            }
            Map<String, Object> param_ = new HashMap<>();
            param_.put("ID_HEADER", idSearchBulletin);
            param_.put("ID_BULLETINS", ids);
            param_.put("NOM_USER", currentUser.getUsers().getNomUsers());
            param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
            param_.put("PATH", returnLogo());
            executeReport("all_bulletin", param_, "Paie_de_" + listPlanification.get(listPlanification.indexOf(new YvsGrhOrdreCalculSalaire(idSearchBulletin))).getReference());
        } else {
            getErrorMessage("Impossible d'imprimer, votre selection est ambigue !");
        }
        return null;
    }

    private YvsGrhBulletins giveBulletin() {
        if (chaineSelectBulletin != null) {
            String numroLine[] = chaineSelectBulletin.split("-");
            try {
                int index;
                for (String numroLine1 : numroLine) {
                    index = Integer.parseInt(numroLine1);
                    return listBulletins.get(index);
                }
                chaineSelectBulletin = "";
                succes();
            } catch (NumberFormatException ex) {
                chaineSelectBulletin = "";
                getErrorMessage("Impossible de terminer cette opération !");
            }
        }
        return null;
    }

    public List<YvsGrhContratEmps> valideChoixEmploye() {
        List<YvsGrhContratEmps> re = new ArrayList<>();
        if (chaineSelectEmploye != null) {
            List<Integer> list = decomposeSelection(chaineSelectEmploye);
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                YvsGrhEmployes em;
                for (int i : list) {
                    if (i < list.size()) {
                        em = service.getListEmployes().get(i);
                        if (em.getContrat() != null && em.getPosteActif() != null && em.getConvention() != null) {
                            re.add(em.getContrat());
                        } else {
                            employesNonTraite.add(em);
                        }
                    }
                }
            }
        }
        return re;
    }

    private boolean controleFicheOrdre(OrdreCalculSalaire o) {
        if (o.getListContrat().isEmpty() && refAction.equals("P")) {
            getErrorMessage("Vous devez selectionné les contrats concernés !");
            return false;
        }
        if (o.getDebutMois() == null || o.getFinMois() == null) {
            getErrorMessage("la période de calcul n'est pas indiqué !");
            return false;
        } else {
            if ((o.getDebutMois() != null && o.getFinMois() != null) ? o.getFinMois().before(o.getDebutMois()) : false) {
                getErrorMessage("La période de calcul est incohérente !");
                return false;
            }
        }
        if (o.getDebutTraitement() == null || o.getFinTraitement() == null) {
            getErrorMessage("la période de traitement du salaire n'est pas indiqué !");
            return false;
        } else {
            if ((o.getDebutTraitement() != null && o.getFinTraitement() != null) ? o.getFinTraitement().before(o.getDebutTraitement()) : false) {
                getErrorMessage("La période de traitement est incohérente !");
                return false;
            }
        }
        if (o.getDateExecution() == null || o.getHeureExecution() == null) {
            getErrorMessage("Préciser la date d'exécution de l'opération !");
            return false;
        }
        if (o.isCloture()) {
            getErrorMessage("Impossible de continuer. cette période a déjà été clôturé !");
            return true;
        }
        return true;
    }
    private boolean updateOrdre;

    public void savecPlanCalculSalaire() {
        if (controleFicheOrdre(ordreCalcul)) {
            String ref = formatReference();
            String abbr = formatAbbreviation();
            //verifie s'il existe un ordre portant déjà cette référence
            if (!updateOrdre) {
                if (findOrderP(ref) != null) {
                    getErrorMessage("Les ordres de calcul de la période " + ref + " Ont déjà été planifiés");
                    return;
                }
            }
            YvsGrhOrdreCalculSalaire o = new YvsGrhOrdreCalculSalaire();
            o.setAuthor(currentUser);
            o.setDateExecution(ordreCalcul.getDateExecution());
            o.setDateJour(new Date());
            o.setDebutMois(ordreCalcul.getDebutMois());
            o.setFinMois(ordreCalcul.getFinMois());
            o.setHeureExecution(ordreCalcul.getHeureExecution());
            o.setReference(ref);
            o.setAbbreviation(abbr);
            o.setSociete(currentAgence.getSociete());
            o.setCloture(ordreCalcul.isCloture());
            o.setDateDebutTraitement(ordreCalcul.getDebutTraitement());
            o.setDateFinTraitement(ordreCalcul.getFinTraitement());
            ordreCalcul.setReference(o.getReference());
            o.setDateUpdate(new Date());
            if (refAction.equals("P")) {
                o.setRealise(false);
            } else {
                o.setRealise(true);
            }
            if (!updateOrdre) {
                o.setDateSave(new Date());
                o = (YvsGrhOrdreCalculSalaire) dao.save1(o);
                ordreCalcul.setId(o.getId());
                listPlanification.add(o);
            } else {
                o.setId(ordreCalcul.getId());
                dao.update(o);
                listPlanification.set(listPlanification.indexOf(o), o);
            }
            currentBulletin.getEntete().setId(o.getId());

            if (refAction.equals("P")) {
                //enregistre les contrats impliqué
                YvsGrhPlanifSalaireContrat pc;
                for (YvsGrhContratEmps ce : ordreCalcul.getListContrat()) {
                    pc = new YvsGrhPlanifSalaireContrat(null);
                    pc.setContrat(new YvsGrhContratEmps(ce.getId()));
                    pc.setPlanification(o);
                    pc = (YvsGrhPlanifSalaireContrat) dao.save1(pc);
                    ce.setId(pc.getId());
                }
            }
            ordreCalcul = new OrdreCalculSalaire();
            updateOrdre = false;
            succes();
        }
    }

    public void resetOrdrePlanif() {
        ordreCalcul = new OrdreCalculSalaire();
        updateOrdre = false;
    }

    private YvsGrhOrdreCalculSalaire findOrderP(String ref) {
        champ = new String[]{"reference", "societe"};
        val = new Object[]{ref, currentAgence.getSociete()};
        return (YvsGrhOrdreCalculSalaire) dao.loadOneByNameQueries("YvsGrhOrdreCalculSalaire.findByReference", champ, val);
    }

//    private List<OrdreCalculSalaire> buildBeanOrdre(List<YvsGrhOrdreCalculSalaire> lo) {
//        List<OrdreCalculSalaire> result = new ArrayList<>();
//        for (YvsGrhOrdreCalculSalaire ob : lo) {
//            result.add(UtilGrh.buildBeanOrdrePlanif(ob));
//        }
//        return result;
//    }
    //charge les ordres
    public void loadPlanifOrdreCalcul() {
//        champ = new String[]{"societe"};
//        val = new Object[]{currentAgence.getSociete()};
//        listPlanification = dao.loadListTableByNameQueries("YvsGrhOrdreCalculSalaire.findAll", champ, val);        
        YvsBaseExercice exo = giveExerciceActif(new Date());
        if (exo != null) {
            dateDebutSearch = exo.getDateDebut();
            dateFinSearch = exo.getDateFin();
            dateSearch = true;
        } else {
            dateSearch = false;
        }
        addParamDates();
    }

    public void loadOnviewOrdreP(SelectEvent ev) {
        if (ev != null) {
            YvsGrhOrdreCalculSalaire o = (YvsGrhOrdreCalculSalaire) ev.getObject();
            debut = o.getDebutMois();
            fin = o.getFinMois();
            champ = new String[]{"header"};
            val = new Object[]{new YvsGrhOrdreCalculSalaire(o.getId())};
            nombreBulletin = (Long) dao.loadObjectByNameQueries("YvsGrhBulletins.findCountBullPeriode", champ, val);
            currentBulletin.setEntete(UtilGrh.buildBeanOrdre(o));
        }
    }

    public void loadOnviewOrdrePToUpdate(YvsGrhOrdreCalculSalaire ev) {
        if (ev != null) {
            ordreCalcul = UtilGrh.buildBeanOrdrePlanif(ev);
            updateOrdre = true;
        }
    }

    public void deleteOrdrePlanif() {
        if (chaineSelectOc != null) {
            boolean delete;
            String numroLine[] = chaineSelectOc.split("-");
            List<String> l = new ArrayList<>();
            try {
                int index;
                for (String numroLine1 : numroLine) {
                    index = Integer.parseInt(numroLine1);
                    dao.delete(new YvsGrhOrdreCalculSalaire(listPlanification.get(index).getId()));
                    listPlanification.remove(index);
                    l.add(numroLine1);
                }
                chaineSelectOc = "";
                succes();
                delete = true;

            } catch (NumberFormatException ex) {
                chaineSelectOc = "";
                for (String numroLine1 : numroLine) {
                    if (!l.contains(numroLine1)) {
                        chaineSelectOc += numroLine1 + "-";
                    }
                }
                delete = false;
            }
            if (!delete) {
                getErrorMessage("Impossible de supprimer cet élément");
            }
        }
    }

    public void cloturePeriodeCalcul() {
        if (chaineSelectOc != null) {
            String numroLine[] = chaineSelectOc.split("-");
            List<String> l = new ArrayList<>();
            try {
                int index;
                for (String numroLine1 : numroLine) {
                    index = Integer.parseInt(numroLine1);
                    YvsGrhOrdreCalculSalaire od = listPlanification.get(index);
                    od.setAuthor(currentUser);
                    od.setSociete(currentAgence.getSociete());
                    od.setCloture(true);
                    od.setDateCloture(new Date());
                    dao.update(od);
                    ordreCalcul.setCloture(true);
                    ordreCalcul.setReference(od.getReference());
                    listPlanification.set(index, od);
                    l.add(numroLine1);
                }
                chaineSelectOc = "";
                succes();

            } catch (NumberFormatException ex) {
                chaineSelectOc = "";
                for (String numroLine1 : numroLine) {
                    if (!l.contains(numroLine1)) {
                        chaineSelectOc += numroLine1 + "-";
                    }
                }
            }
        }
    }

    private boolean canValide(YvsGrhBulletins b) {
        if (b != null) {
            if (b.getListDetails() != null) {
                double q = (paramGrh.getQuotiteCessible() != null) ? paramGrh.getQuotiteCessible() : 1;
                double salaire = 0;
                for (YvsGrhDetailBulletin d : b.getListDetails()) {
                    salaire += (d.getMontantPayer() - d.getRetenuSalariale());
                }
                if (salaire <= 0) {
                    return (autoriser("bulletin_save_bul_negatif"));
                }
            }
        }
        return true;
    }

    //confirmer (valider un bulletin de paie)
    public void confirmBulletin(String statut, boolean all) {
        if (chaineSelectBulletin != null) {
            List<Integer> l = decomposeSelection(chaineSelectBulletin);
            YvsGrhBulletins b;
            for (int index : l) {
                b = listBulletins.get(index);
                if ("P".equals(statut)) {
                    if (b.getStatut() != 'C') {
                        return;
                    }
                }
                if ("C".equals(statut) && !canValide(b)) {
                    getErrorMessage("Vous ne pouvez confirmer ce bulletin", "le salaire est négatif");
                    //afficher ses retenue non prélevé du mois
                    b.setListeRetenue(dao.loadNameQueries("YvsGrhDetailPrelevementEmps.findByDatePrelevement", new String[]{"contrat", "fin"}, new Object[]{contrat, fin}));
                    return;
                }
                b.setStatut(statut.charAt(0));
                b.setDateValidation(new Date());
                b.setDateUpdate(new Date());
                b.setAuthor(currentUser);
                b.setSociete(currentAgence.getSociete());
                b.setAuteurValidation(currentUser.getUsers());
                listBulletins.get(index).setStatut(statut.charAt(0));
                dao.update(b);
            }
            chaineSelectBulletin = "";
            succes();
        }
    }

    //vérifie si on a déjà un bulletin de validé à la période en cours
    private YvsGrhBulletins findBulletinValide(YvsGrhContratEmps cont, YvsGrhOrdreCalculSalaire head) {
        return (YvsGrhBulletins) dao.loadOneByNameQueries("YvsGrhBulletins.findBulEmploye", new String[]{"contrat", "header"}, new Object[]{cont, head});
    }
    private List<YvsGrhDetailPrelevementEmps> listeRetenues;

    public List<YvsGrhDetailPrelevementEmps> getListeRetenues() {
        return listeRetenues;
    }

    public void setListeRetenues(List<YvsGrhDetailPrelevementEmps> listeRetenues) {
        this.listeRetenues = listeRetenues;
    }

    public void openListeRetenuesBulletin(YvsGrhBulletins b) {
        listeRetenues = dao.loadNameQueries("YvsGrhDetailPrelevementEmps.findByDatePrelevement", new String[]{"contrat", "fin"}, new Object[]{b.getContrat(), b.getEntete().getFinMois()});
        if (!listeRetenues.isEmpty()) {
            openDialog("dlgretenuesMois");
        } else {
            getWarningMessage("Aucune retenues n'a été trouvé ce mois pour l'employé selectionné !");
        }
    }

    public void confirmValidOneBulletin(YvsGrhBulletins b) {
        selectedBulletin = b;
        update("table_buletin_calcule");
        choixBulletin(b);
        if (b.getStatut() == Constantes.STATUT_DOC_EDITABLE) {
            openDialog("dlgConfirmB");
        } else {
            openDialog("dlgConfirmAB");
        }
    }

    public void confirmPayeOneBulletin(YvsGrhBulletins b) {
        selectedBulletin = b;
        update("table_buletin_calcule");
        choixBulletin(b);
        openDialog("dlgConfirmPB");
    }

    private String formatReference() {
        String ref;
        Calendar date = Calendar.getInstance();
        date.setTime(ordreCalcul.getFinMois());
        int m = date.get(Calendar.MONTH);
        int y = date.get(Calendar.YEAR);
        ref = (Constantes.tabMois[m] + " " + y);
        return ref;
    }

    private String formatAbbreviation() {
        return periodeAbbreviation(ordreCalcul.getFinMois());
    }

    //recherche
    private long idSearchBulletin;
    private long idAgence;

    public long getIdSearchBulletin() {
        return idSearchBulletin;
    }

    public void setIdSearchBulletin(long idSearchBulletin) {
        this.idSearchBulletin = idSearchBulletin;
    }

    public long getIdAgence() {
        return idAgence;
    }

    public void setIdAgence(long idAgence) {
        this.idAgence = idAgence;
    }

    public void choiHeader(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                YvsGrhOrdreCalculSalaire o = listPlanification.get(listPlanification.indexOf(new YvsGrhOrdreCalculSalaire(id)));
                if (!o.getCloture()) {
                    debut = o.getDebutMois();
                    fin = o.getFinMois();
                    champ = new String[]{"header"};
                    val = new Object[]{new YvsGrhOrdreCalculSalaire(o.getId())};
                    nombreBulletin = (Long) dao.loadObjectByNameQueries("YvsGrhBulletins.findCountBullPeriode", champ, val);
                    currentBulletin.setEntete(UtilGrh.buildBeanOrdre(o));
                } else {
                    debut = fin = null;
                    getMessage("Cette période a déjà été clôturé", FacesMessage.SEVERITY_ERROR);
                }
            } else if (id == -1) {
                openDialog("dlgOrdreCalcul");
                refAction = "N";
                debut = fin = null;
            } else {
                debut = fin = null;
            }
        }
    }

    //Imprimer le livre de paye
    public void exporteLivrePaye() {
        YvsGrhBulletins b = giveBulletin();
        if (b != null) {
            Map<String, Object> param_ = new HashMap<>();
            String anciennete = Utilitaire.calculNbyear(b.getContrat().getEmploye().getDateEmbauche(), b.getDateFin());
            param_.put("ID_BULLETIN", b.getId());
            param_.put("NOM_USER", currentUser.getUsers().getNomUsers());
            param_.put("ANCIENNETE", anciennete);
            param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
            param_.put("PATH", returnLogo());
            executeReport("Bulletin_paie", param_, "");
        } else {
            getErrorMessage("Impossible d'imprimer, votre selection est ambigue !");
        }
    }

    private int position;
    private long nombreBulletin;

    public long getNombreBulletin() {
        return nombreBulletin;
    }

    public void setNombreBulletin(long nombreBulletin) {
        this.nombreBulletin = nombreBulletin;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void navigueInAppercu(int pas) {
        if (currentBulletin.getEntete().getId() > 0) {
            champ = new String[]{"header"};
            val = new Object[]{new YvsGrhOrdreCalculSalaire(currentBulletin.getEntete().getId())};
            if (position >= 0 && nombreBulletin >= position) {
                List<YvsGrhBulletins> l = dao.loadNameQueries("YvsGrhBulletins.findNextBulletin", champ, val, position, 1);
                if (!l.isEmpty()) {
                    choixBulletin(l.get(0));
                    if (pas > 0) {  //avance         
                        position += pas;
                    } else {//recule
                        position -= 1;
                    }
                } else {
                    position = 0;
                }
            } else {
                position = 0;
            }
        }

    }

    public void compileBulletin() {
        if (currentBulletin.getEntete().getId() > 0) {
            champ = new String[]{"id"};
            val = new Object[]{currentBulletin.getEntete().getId()};
            YvsGrhOrdreCalculSalaire head = (YvsGrhOrdreCalculSalaire) dao.loadOneByNameQueries("YvsGrhOrdreCalculSalaire.findById", champ, val);
            if (!autoriser("grh_compile_bulletin")) {
                openNotAcces();
                return;
            }
            List<YvsGrhBulletins> re = dao.calculSalaire(head, valideChoixEmploye(), deleteAllDoublon);
            listBulletins.clear();
            if (re != null) {
                listBulletins.addAll(0, re);
                if (!re.isEmpty()) {
                    currentBulletin = UtilGrh.buildBeanBulletin(re.get(0), true);
                    succes();
                } else {
                    getWarningMessage("Aucun bulletin n'a été compilé !");
                }
            }
            if (!employesNonTraite.isEmpty()) {
                openDialog("dlgNonTraite");
            }
        }
    }

    public void continuCompile(boolean delete) {
        compileOneBulletin(seleEmployeEmploye, true, delete);
    }
    List<YvsGrhBulletins> old;

    public void compileOneBulletin(YvsGrhEmployes employe, boolean continu, boolean delete) {
        if (currentBulletin.getEntete().getId() > 0 && employe != null) {
            if (!autoriser("grh_compile_bulletin")) {
                openNotAcces();
                return;
            }
            seleEmployeEmploye = employe;
            champ = new String[]{"id"};
            val = new Object[]{currentBulletin.getEntete().getId()};
            YvsGrhOrdreCalculSalaire head = (YvsGrhOrdreCalculSalaire) dao.loadOneByNameQueries("YvsGrhOrdreCalculSalaire.findById", champ, val);
            if (employe.getContrat() != null) {
                List<YvsGrhContratEmps> l = new ArrayList<>();
                if (employe.getContrat().getActif()) {
                    if (employe.getContrat().getStatut().equals("C")) {
                        if (deleteAllDoublon) {
                            old = dao.loadNameQueries("YvsGrhBulletins.findOneByHead_", new String[]{"contrat", "header"}, new Object[]{employe.getContrat(), head});
                        }
                        l.add(employe.getContrat());
                        List<YvsGrhBulletins> re = dao.calculSalaire(head, l, deleteAllDoublon);
                        if (re != null) {
                            listBulletins.addAll(0, re);
                            if (old != null && delete) {
                                listBulletins.removeAll(old);
                            }
                            if (!re.isEmpty()) {
                                currentBulletin = UtilGrh.buildBeanBulletin(re.get(0), true);
                                succes();
                            } else {
                                getWarningMessage("Aucun bulletin n'a été compilé !");
                            }
                        } else {
                            getWarningMessage("Aucun bulletin n'a été compilé !");
                        }
                    } else {
                        getErrorMessage("Le contrat de l'employé n'est pas encore confirmé");
                    }
                } else {
                    getErrorMessage("L'employé selectionné n'a pas de contrat actif !");
                }
            } else {
                getErrorMessage("L'employé selectionné n'a pas de contrat actif !");
            }
            if (!employesNonTraite.isEmpty()) {
                openDialog("dlgNonTraite");
                update("list_Bull_nonTraite");
            }
        }
    }

    public void loadBulletinOneEmploye(YvsGrhEmployes emp) {
        if (emp != null) {
            if (emp.getContrat() != null) {
                listBulletins = dao.loadNameQueries("YvsGrhBulletins.findByContratEmploye", new String[]{"contrat"}, new Object[]{emp.getContrat()});
                closeDialog("dlgEmploye");
            } else {
                getErrorMessage("Cet employé ne possède aucun contrat !");
            }
        }
    }

    private List<YvsGrhParametresBulletin> listeParametre;
    private YvsGrhParametresBulletin currentParam;
    private YvsGrhParametresBulletin newParam = new YvsGrhParametresBulletin();
    private boolean updateParam;

    public List<YvsGrhParametresBulletin> getListeParametre() {
        return listeParametre;
    }

    public void setListeParametre(List<YvsGrhParametresBulletin> listeParametre) {
        this.listeParametre = listeParametre;
    }

    public YvsGrhParametresBulletin getCurrentParam() {
        return currentParam;
    }

    public void setCurrentParam(YvsGrhParametresBulletin currentParam) {
        this.currentParam = currentParam;
    }

    public YvsGrhParametresBulletin getNewParam() {
        return newParam;
    }

    public void setNewParam(YvsGrhParametresBulletin newParam) {
        this.newParam = newParam;
    }

    public boolean isUpdateParam() {
        return updateParam;
    }

    public void setUpdateParam(boolean updateParam) {
        this.updateParam = updateParam;
    }

    long i = -1000;

    public void openDialogToAddOthersElement() {
        List<Integer> indexs = decomposeSelection(tabIds);
        if (indexs != null ? indexs.isEmpty() : true) {
            getErrorMessage("Vous devez selectionner plusieurs employés");
            return;
        }
        if (currentBulletin.getEntete().getId() < 1) {
            getErrorMessage("Aucune période de calcul n'a été spécifié !");
            return;
        }
        int idx = listPlanification.indexOf(new YvsGrhOrdreCalculSalaire(currentBulletin.getEntete().getId()));
        if (idx < 0) {
            getErrorMessage("Choisissez une période de calcul !");
            return;
        }
        currentParam = new YvsGrhParametresBulletin();
        currentParam.setHeaderBulletin(listPlanification.get(idx));
        List<Object[]> l = dao.loadNameQueries("YvsGrhParametresBulletin.findDistinctCode", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
        YvsGrhParametresBulletin par;
        listeParametre.clear();
        for (Object st[] : l) {
            par = new YvsGrhParametresBulletin(i);
            par.setCodeElement((String) st[0]);
            par.setDescription((String) st[1]);
            par.setActif(true);
            par.setHeaderBulletin(currentParam.getHeaderBulletin());

            listeParametre.add(par);
            i++;
        }
        openDialog("dlgAddParamBulletin");
        update("header_Form_AddPAram");
        update("form_Form_AddPAram");
    }

    public void openDialogToAddOthersElement(YvsGrhEmployes emps) {
        chaineSelectEmploye = "";
        if (currentBulletin.getEntete().getId() > 0) {
            if (emps != null) {
                if (emps.getContrat() != null) {
                    currentParam = new YvsGrhParametresBulletin();
                    currentParam.setContrat(emps.getContrat());
                    int idx = listPlanification.indexOf(new YvsGrhOrdreCalculSalaire(currentBulletin.getEntete().getId()));
                    if (idx >= 0) {
                        currentParam.setHeaderBulletin(listPlanification.get(idx));
                        List<Object[]> l = dao.loadNameQueries("YvsGrhParametresBulletin.findDistinctCode", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
                        YvsGrhParametresBulletin par;
                        listeParametre.clear();
                        for (Object st[] : l) {
                            //vérifie si on a déjà enregistré cette valeur pour l'utilisateur
                            par = (YvsGrhParametresBulletin) dao.loadOneByNameQueries("YvsGrhParametresBulletin.findOneParam", new String[]{"code", "contrat", "header"}, new Object[]{st[0], emps.getContrat(), new YvsGrhOrdreCalculSalaire(currentBulletin.getEntete().getId())});
                            if (par == null) {
                                par = new YvsGrhParametresBulletin(i);
                                par.setCodeElement((String) st[0]);
                                par.setDescription((String) st[1]);
                                par.setActif(true);
                                par.setContrat(emps.getContrat());
                                par.setHeaderBulletin(currentParam.getHeaderBulletin());
                            }
                            listeParametre.add(par);
                            i++;
                        }
                        openDialog("dlgAddParamBulletin");
                        update("header_Form_AddPAram");
                        update("form_Form_AddPAram");
                    } else {
                        getErrorMessage("Choisissez une période de calcul !");
                    }
                } else {
                    getErrorMessage("Aucun contrat n'a été trouvé pour cet employé !");
                }
            } else {
                getErrorMessage("Votre selection est ambigue !");
            }
        } else {
            getErrorMessage("Aucune période de calcul n'a été spécifié !");
        }
    }

    public void loadListeParametre() {
        List<Object[]> l = dao.loadNameQueries("YvsGrhParametresBulletin.findDistinctCode", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
        YvsGrhParametresBulletin par;
        listeParametre.clear();
        int i = -1000;
        for (Object st[] : l) {
            //vérifie si on a déjà enregistré cette valeur pour l'utilisateur
            par = new YvsGrhParametresBulletin((long) i);
            par.setCodeElement((String) st[0]);
            par.setDescription((String) st[1]);
            par.setActif(true);
            listeParametre.add(par);
            i++;
        }
    }

    public void saveParametreBulletin() {
        if (listeParametre != null ? !listeParametre.isEmpty() : false) {
            List<Integer> indexs = decomposeSelection(tabIds);
            if (indexs != null ? !indexs.isEmpty() : false) {
                ManagedEmployes ws = (ManagedEmployes) giveManagedBean("MEmps");
                if (ws != null) {
                    YvsGrhEmployes employe;
                    YvsGrhParametresBulletin y;
                    for (Integer index : indexs) {
                        employe = ws.getListEmployes().get(index);
                        for (YvsGrhParametresBulletin pb : listeParametre) {
                            if ((pb.getValeur() != null) ? pb.getValeur() != 0 : false) {
                                y = new YvsGrhParametresBulletin(null, pb);
                                y.setContrat(employe.getContrat());
                                y.setAuthor(currentUser);
                                y.setDateSave(new Date());
                                dao.save1(y);
                            }
                        }
                    }
                }
            } else {
                for (YvsGrhParametresBulletin pb : listeParametre) {
                    pb.setAuthor(currentUser);
                    pb.setDateSave(new Date());
                    if (pb.getId() > 0) {
                        dao.update(pb);
                    } else {
                        if ((pb.getValeur() != null) ? pb.getValeur() != 0 : false) {
                            pb.setId(null);
                            pb = (YvsGrhParametresBulletin) dao.save1(pb);
                        }
                    }
                }
            }
            succes();
        }
    }

    public void openToUpdateLineParam(SelectEvent ev) {
        if (ev != null) {
            newParam = (YvsGrhParametresBulletin) ev.getObject();
            currentParam = newParam;
            openToaddLineParam(true);
        }
    }

    public void openToaddLineParam(boolean b) {
        if (!autoriser("grh_bull_add_regle")) {
            openNotAcces();
            return;
        }
        updateParam = b;
        if (!b) {   //si on ouvre pour une nouvelle insertion
            newParam.setId(i++);
        }
        openDialog("dlgUpdateParamBulletin");
        update("form_addParam");
    }

    public void onRowLineParamEdit(RowEditEvent ev) {
        try {
            if (ev != null) {
                YvsGrhParametresBulletin y = (YvsGrhParametresBulletin) ev.getObject();
                if (y != null) {
                    if (!autoriser("grh_bull_add_regle")) {
                        int index = listeParametre.indexOf(y);
                        if (index > -1) {
                            listeParametre.get(index).setValeur(null);
                        }
                        update("form_Form_AddPAram");
                        openNotAcces();
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("ERROR (onRowWarningEdit)", ex);
        }
    }

    public void addLineParam() {
        if (currentParam != null) {
            if (newParam.getCodeElement() != null && newParam.getDescription() != null) {
                if ((newParam.getValeur() != null ? newParam.getValeur() != 0 : false) && !autoriser("grh_bull_add_regle")) {
                    openNotAcces();
                    return;
                }
                newParam.setContrat(currentParam.getContrat());
                newParam.setHeaderBulletin(currentParam.getHeaderBulletin());
                newParam.setAuthor(currentUser);
                newParam.setDateSave(new Date());
                if (!updateParam) {
                    listeParametre.add(newParam);
                } else {
                    listeParametre.set(listeParametre.indexOf(newParam), newParam);
                }
                newParam = new YvsGrhParametresBulletin(i++);
            }
        }
    }

    public void hideElementFromList(YvsGrhParametresBulletin p) {
        listeParametre.remove(p);
    }

    public void disableElement(YvsGrhParametresBulletin p) {
        p.setActif(!p.getActif());
    }

    public String oldCode;

    public void openDlgConfirmRename(YvsGrhParametresBulletin p) {
        if ((p.getValeur() != null ? p.getValeur() != 0 : false) && !autoriser("grh_bull_add_regle")) {
            openNotAcces();
            return;
        }
        oldCode = p.getCodeElement();
        newParam = p;
        openDialog("dlgUpdateCodeParamBulletin");
        update("form_addParam_");
    }

    public void applyNewCode() {
        if (newParam.getCodeElement() != null && newParam.getDescription() != null && oldCode != null) {
            String rq = "UPDATE yvs_grh_parametres_bulletin SET code_element=?, description=?, actif=? WHERE code_element=?";
            dao.requeteLibre(rq, new Options[]{new Options(newParam.getCodeElement(), 1), new Options(newParam.getDescription(), 2), new Options(newParam.getActif(), 3), new Options(oldCode, 4)});
            for (YvsGrhParametresBulletin p : listeParametre) {
                p.setCodeElement(newParam.getCodeElement());
                p.setDescription(newParam.getDescription());
                p.setActif(newParam.getActif());
            }
            succes();
        } else {
            getErrorMessage("Formulaire incorrect !");
        }
    }

    /**
     * Zone de recherhe
     *
     *
     * @param ev
     */
    private String matriculeF;
    private long idExo;

    public long getIdExo() {
        return idExo;
    }

    public void setIdExo(long idExo) {
        this.idExo = idExo;
    }

    public String getMatriculeF() {
        return matriculeF;
    }

    public void setMatriculeF(String matriculeF) {
        this.matriculeF = matriculeF;
    }

    public void chooseExercice() {
        //extrait les date de l'exercice
        if (idExo > 0) {
            int index = exercices.indexOf(new YvsBaseExercice(idExo));
            if (index > -1) {
                YvsBaseExercice exo = exercices.get(index);
                setDateDebutSearch(exo.getDateDebut());
                setDateFinSearch(exo.getDateFin());
                setDateSearch(true);
                addParamDates();
            }
        }
    }

    public void filterBulletinByExercice() {
        chooseExercice();
        ParametreRequete p = new ParametreRequete("y.bulletin.entete.debutMois", "debutMois", null, "=", "AND");
        if (idExo > 0) {
            int index = exercices.indexOf(new YvsBaseExercice(idExo));
            if (index > -1) {
                YvsBaseExercice exo = exercices.get(index);
                p = new ParametreRequete("y.bulletin.entete.debutMois", "debutMois", exo.getDateDebut(), exo.getDateFin(), "BETWEEN", "AND");
            }
        }
        initForm = true;
        paginator.addParam(p);
        loadAllBulletin(true);
    }

    public void filterBulletinByPeriode(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.bulletin.entete", "header", null, "=", "AND");
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                p.setObjet(new YvsGrhOrdreCalculSalaire(id));
            }
        }
        initForm = true;
        paginator.addParam(p);
        loadAllBulletin(true);
    }

    public void filterBulletinByAgence(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.agence", "agence", null, "=", "AND");
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                p.setObjet(new YvsAgences(id));
            }
        }
        initForm = true;
        paginator.addParam(p);
        loadAllBulletin(true);
    }

    public void filterBulletinByStatut(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.bulletin.statut", "statut", null, "=", "AND");
        if (ev.getNewValue() != null) {
            Character st = (Character) ev.getNewValue();
            if (st != null) {
                p.setObjet((Character) ev.getNewValue());
            }
        }
        initForm = true;
        paginator.addParam(p);
        loadAllBulletin(true);
    }

    public void filterBulletinByEmploye(String matricule) {
        ParametreRequete p = new ParametreRequete(null, "employe", "XX", "LIKE", "AND");
        if ((matricule != null) ? !matricule.isEmpty() : false) {
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.bulletin.contrat.employe.nom)", "emps", "%" + matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.bulletin.contrat.employe.prenom)", "emps", "%" + matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.bulletin.contrat.employe.matricule)", "emps", matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllBulletin(true);
    }

    private boolean controleValideBulletin(YvsGrhBulletins b, boolean all) {
        if (b == null) {
            if (!all) {
                getErrorMessage("Aucun bulletin de paye n'a été selectionné !");
            }
            return false;
        }
        if (b.getEntete() == null) {
            if (!all) {
                getErrorMessage("Le bulletin est incomplet !");
            }
            return false;
        }
        //Il ne doit exister aucun buletin de l'employé déjà validé ce mois
        YvsGrhBulletins cb = (YvsGrhBulletins) dao.loadOneByNameQueries("YvsGrhBulletins.findOneByHead", new String[]{"header", "contrat", "statut", "statut1"}, new Object[]{b.getEntete(), b.getContrat(), Constantes.STATUT_DOC_VALIDE, Constantes.STATUT_DOC_PAYER});
        if (cb != null) {
            if (!all) {
                getErrorMessage("Un bulletin du même contrat a déjà été validé pour cette période");
            }
            return false;
        }
        if (b.getStatut() != Constantes.STATUT_DOC_EDITABLE) {
            if (!all) {
                getErrorMessage("Le bulletin doit être éditable");
            }
            return false;
        }
        //le net à payer ne doit pas être inférieur à 0
        Double net = (Double) dao.loadObjectByNameQueries("YvsGrhMontanSalaire.findNetAPayer", new String[]{"bulletin"}, new Object[]{b});
        if (net != null) {
            if (net <= 0) {
                if (!all) {
                    getErrorMessage("Vous ne pouvez valider ce document; son solde est nul ou négatif !");
                }
                return false;
            }
        }
        return true;
    }

    public void confirmValide() {
        validBulletin(selectedBulletin, false);
//        update("input_employe_bulletin");
    }

    public void confirmCancel() {
        cancelBulletin(selectedBulletin);
//        update("input_employe_bulletin");
    }

    public void payeBulletin() {
        payeBulletin(selectedBulletin, false);
//        update("input_employe_bulletin");
    }

    public void validBulletin(YvsGrhBulletins b, boolean all) {
        if (controleValideBulletin(b, all)) {
            if (!all) {
                if (!autoriser("grh_valid_bulletin")) {
                    openNotAcces();
                    return;
                }
            }
            b.setAuthor(currentUser);
            b.setDateUpdate(new Date());
            b.setAuteurValidation(currentUser.getUsers());
            b.setDateValidation(new Date());
            b.setStatut(Constantes.STATUT_DOC_VALIDE);
            //modifie le statut de toutes les retenues de la période
            changeStatutRetenue(b);
            //marque les retards de la période
            cancelRetardPeriode(b, true);
            List< YvsGrhBulletins> list = new ArrayList<>();
            list.add(b);
            generePieceSalaire(list);
            dao.update(b);
            if (!all) {
                currentBulletin.setStatut(Constantes.STATUT_DOC_VALIDE);
                succes();
            }
        }
    }

    public void payeBulletin(YvsGrhBulletins b, boolean all) {
        if (b.getStatut() == Constantes.STATUT_DOC_VALIDE) {
            b.setAuthor(currentUser);
            b.setDateUpdate(new Date());
            b.setStatut(Constantes.STATUT_DOC_PAYER);
            //modifie le statut de toutes les retenues de la période
            dao.update(b);
            if (!all) {
                currentBulletin.setStatut(Constantes.STATUT_DOC_PAYER);
                succes();
            }
        } else {
            if (!all) {
                getErrorMessage("Le bulletin n'a pas encore été validé !");
            }
        }
    }

    public void cancelBulletin(YvsGrhBulletins b) {
        if (b.getStatut() == Constantes.STATUT_DOC_VALIDE || b.getStatut() == Constantes.STATUT_DOC_SUSPENDU) {
            if (b.getStatut() == Constantes.STATUT_DOC_VALIDE) {
                if (!autoriser("grh_cancel_valid_bulletin")) {
                    openNotAcces();
                    return;
                }
            }
            b.setAuthor(currentUser);
            b.setDateUpdate(new Date());
            b.setStatut(Constantes.STATUT_DOC_EDITABLE);
            b.setDateValidation(null);
            b.setAuteurValidation(null);
            dao.update(b);
            //remodifie le statut de toutes les retenues de la période
            restaureRetenues(b);
            //efface les retards valorisé du bulletin
            cancelRetardPeriode(b, false);
            currentBulletin.setStatut(Constantes.STATUT_DOC_EDITABLE);
            List<YvsGrhBulletins> list = new ArrayList<>();
            list.add(b);
            deletePieceSalaire(list);
            succes();
        } else {
            getErrorMessage("Le bulletin n'a pas encore été validé !");
        }
    }

    private void changeStatutRetenue(YvsGrhBulletins b) {
        listeRetenues = dao.loadNameQueries("YvsGrhDetailPrelevementEmps.findByDatePrelevement", new String[]{"contrat", "fin"}, new Object[]{b.getContrat(), b.getEntete().getDateFinTraitement()});
        String query = "INSERT INTO yvs_grh_valorisation_retenues(retenue, bulletin) "
                + "    VALUES (?, ?)";
        for (YvsGrhDetailPrelevementEmps dp : listeRetenues) {
            if (!dp.getRetenuFixe()) {
                dp.setDatePreleve(b.getDateSave());
                dp.setStatutReglement(Constantes.STATUT_DOC_PAYER);
                dao.update(dp);
                dao.requeteLibre(query, new Options[]{new Options(dp.getId(), 1), new Options(b.getId(), 2)});
            }
        }
    }

    private void restaureRetenues(YvsGrhBulletins b) {
        //récupère dans la table yvs_grh_valorisation_retenues les lignes en rapport avec ce bulletin
        String query = "SELECT retenue FROM yvs_grh_valorisation_retenues WHERE bulletin=? ";
        List<Long> l = dao.loadListBySqlQuery(query, new Options[]{new Options(b.getId(), 1)});
        query = "UPDATE yvs_grh_detail_prelevement_emps SET date_preleve=null, statut_reglement=? WHERE id=? ";
        for (Long id : l) {
            dao.requeteLibre(query, new Options[]{new Options(Constantes.STATUT_DOC_EDITABLE, 1),
                new Options(id, 2)});

        }
    }

    private void cancelRetardPeriode(YvsGrhBulletins b, boolean insert) {
        if (insert) {
//récupère les fiche de présence de où total_retard est positif        
            List<Object[]> l = dao.loadNameQueries("YvsGrhPresence.findWithRetard", new String[]{"employe", "date"}, new Object[]{b.getContrat().getEmploye(), b.getEntete().getDateFinTraitement()});
            String query = "INSERT INTO yvs_grh_valorisation_retard(fiche, bulletin, retard_valorise) "
                    + "    VALUES (?, ?, ?) ";
            for (Object[] idP : l) {
                dao.requeteLibre(query, new Options[]{new Options(idP[0], 1), new Options(b.getId(), 2), new Options(idP[1], 3)});
            }
        } else {
            String query = "DELETE FROM yvs_grh_valorisation_retard WHERE bulletin=? ";
            dao.requeteLibre(query, new Options[]{new Options(b.getId(), 1)});
        }
    }

    public void confirmValidAll() {
        if (!autoriser("grh_valid_bulletin")) {
            openNotAcces();
            return;
        }
        for (YvsGrhBulletins b : listBulletins) {
            validBulletin(b, true);
        }
        generePieceSalaire(listBulletins);
    }

    public void confirmCancelValidAll() {
        if (!autoriser("grh_cancel_valid_bulletin")) {
            openNotAcces();
            return;
        }
        for (YvsGrhBulletins b : listBulletins) {
            cancelBulletin(b);
        }
        generePieceSalaire(listBulletins);
    }

    public void confirmPayeAll() {
        for (YvsGrhBulletins b : listBulletins) {
            payeBulletin(b, true);
        }
    }

    public void updateDetailBulletin(YvsGrhDetailBulletin de, boolean nowVisible) {
        de.setNowVisible(nowVisible);
        dao.update(de);
    }

    private void deletePieceSalaire(List<YvsGrhBulletins> list) {
        if (list != null ? !list.isEmpty() : false) {
            YvsComptaNotifReglementBulletin pb;
            for (YvsGrhBulletins b : list) {
                pb = (YvsComptaNotifReglementBulletin) dao.loadOneByNameQueries("YvsComptaNotifReglementBulletin.findByBulletin", new String[]{"bulletin"}, new Object[]{b});
                if (pb != null ? pb.getId() > 0 : false) {
                    dao.delete(pb);
                    Long count = (Long) dao.loadObjectByNameQueries("YvsComptaNotifReglementBulletin.countByPiece", new String[]{"piece"}, new Object[]{pb.getPiece()});
                    if (count != null ? count < 1 : true) {
                        dao.delete(pb.getPiece());
                    }
                }
            }
        }
    }

    private void generePieceSalaire(List<YvsGrhBulletins> list) {
        if (list != null ? !list.isEmpty() : false) {
            YvsComptaCaissePieceSalaire ps;
            YvsComptaNotifReglementBulletin pb;
            String ref = genererReference(Constantes.TYPE_PC_SALAIRE_NAME, new Date());
            if (ref != null ? ref.trim().length() < 1 : true) {
                return;
            }
            for (YvsGrhBulletins b : list) {
                if (b.getContrat().getCaisse() == null) {
                    continue;
                }
                if (b.getContrat().getModePaiement() == null) {
                    continue;
                }
                pb = (YvsComptaNotifReglementBulletin) dao.loadOneByNameQueries("YvsComptaNotifReglementBulletin.findByBulletin", new String[]{"bulletin"}, new Object[]{b});
                if (pb != null ? pb.getId() > 0 : false) {
                    continue;
                }
                boolean update = false;
                Date date = b.getEntete().getDebutMois();
                if (b.getContrat().getModePaiement().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE)) {
                    ps = new YvsComptaCaissePieceSalaire();
                    ps.setAuthor(currentUser);
                    ps.setCaisse(b.getContrat().getCaisse());
                    ps.setDatePaiement(date);
                    ps.setDatePaimentPrevu(date);
                    ps.setDatePiece(date);
                    ps.setModel(b.getContrat().getModePaiement());
                    ps.setMontant(b.getNetApayer());
                    ps.setNumeroPiece(genererReference(Constantes.TYPE_PC_SALAIRE_NAME, b.getEntete().getDebutMois()));
                    ps.setReferenceExterne(b.getRefBulletin());
                    ps.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                    ps = (YvsComptaCaissePieceSalaire) dao.save1(ps);
                    update = true;
                } else {
                    ps = (YvsComptaCaissePieceSalaire) dao.loadOneByNameQueries("YvsComptaCaissePieceSalaire.findByDateCaisseMode", new String[]{"datePaimentPrevu", "caisse", "model"}, new Object[]{date, b.getContrat().getCaisse(), b.getContrat().getModePaiement()});
                    if (ps != null ? ps.getId() > 0 : false) {
                        ps.setMontant(ps.getMontant() + b.getNetApayer());
                        dao.update(ps);
                    } else {
                        ps = new YvsComptaCaissePieceSalaire();
                        ps.setAuthor(currentUser);
                        ps.setCaisse(b.getContrat().getCaisse());
                        ps.setDatePaiement(date);
                        ps.setDatePaimentPrevu(date);
                        ps.setDatePiece(date);
                        ps.setModel(b.getContrat().getModePaiement());
                        ps.setMontant(b.getNetApayer());
                        ps.setNumeroPiece(genererReference(Constantes.TYPE_PC_SALAIRE_NAME, b.getEntete().getDebutMois()));
                        ps.setReferenceExterne(b.getRefBulletin());
                        ps.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                        ps = (YvsComptaCaissePieceSalaire) dao.save1(ps);
                        if (b.getContrat().getModePaiement().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                            List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{b.getContrat().getModePaiement(), true});
                            //lié les phases à la pièce de règlements
                            YvsComptaPhasePieceSalaire pp;
                            for (YvsComptaPhaseReglement ph : phases) {
                                pp = new YvsComptaPhasePieceSalaire(null);
                                pp.setAuthor(currentUser);
                                pp.setPhaseOk(false);
                                pp.setPhaseReg(ph);
                                pp.setPiece(ps);
                                pp.setCaisse(ps.getCaisse());
                                dao.save(pp);
                            }
                        }
                    }
                }
                pb = new YvsComptaNotifReglementBulletin(b, ps);
                pb.setAuthor(currentUser);
                dao.save(pb);
                if (update) {
                    dao.update(ps);
                }
            }
        }
    }

    public void loadHeadBulletin(boolean avance, boolean init) {
        p_entete.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        listPlanification = p_entete.executeDynamicQuery("YvsGrhOrdreCalculSalaire", "y.debutMois ASC", avance, init, ifin, dao);
    }

    public void choosePaginatorHead(ValueChangeEvent ev) {
        if (ev != null ? ev.getNewValue() != null : false) {
            ifin = (int) ev.getNewValue();
            loadHeadBulletin(true, true);
        }
    }

    public void loadHeadBulletin(Boolean cloture) {
        p_entete.getParams().clear();
        cloturerSearch = cloture;
        addParamCloture();
    }

    public void addParamComptabilised() {
        ParametreRequete p = new ParametreRequete("coalesce(y.comptabilise, false)", "comptabilise", comptaSearch, "=", "AND");
        if (comptaSearch != null) {
            String query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_entete_bulletin c RIGHT JOIN yvs_grh_ordre_calcul_salaire y ON c.entete = y.id "
                    + "WHERE y.societe = ? AND c.id " + (comptaSearch ? "IS NOT NULL" : "IS NULL");
            Options[] params = new Options[]{new Options(currentAgence.getSociete().getId(), 1)};
            if (dateSearch) {
                query += " AND y.date_jour BETWEEN ? AND ?";
                params = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateDebutSearch, 2), new Options(dateFinSearch, 3)};
            }
            Long count = (Long) dao.loadObjectBySqlQuery(query, params);
            nbrComptaSearch = count != null ? count : 0;
        }
        p_entete.addParam(p);
        loadHeadBulletin(true, true);
    }

    public void addParamCloture() {
        p_entete.addParam(new ParametreRequete("y.cloture", "cloture", cloturerSearch, "=", "AND"));
        loadHeadBulletin(true, true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.reference", "reference", null, "=", "AND");
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.reference)", "reference", numSearch.toUpperCase() + "%", "LIKE", "AND");
        }
        p_entete.addParam(p);
        loadHeadBulletin(true, true);
    }

    public void choixExercice(ValueChangeEvent ev) {
        dateSearch = false;
        if (ev.getNewValue() != null) {
            Long id = (Long) ev.getNewValue();
            if (id >= 0) {
                ManagedExercice service = (ManagedExercice) giveManagedBean(ManagedExercice.class);
                if (service != null) {
                    int idx = service.getExercices().indexOf(new YvsBaseExercice(id));
                    if (idx >= 0) {
                        YvsBaseExercice exo = service.getExercices().get(idx);
                        dateDebutSearch = exo.getDateDebut();
                        dateFinSearch = exo.getDateFin();
                        dateSearch = true;
                    }
                }
            }
        }
        addParamDates();
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.debutMois", "debutMois", null, "=", "AND");
        if (dateSearch) {
            p = new ParametreRequete("y.debutMois", "debutMois", dateDebutSearch, dateFinSearch, "BETWEEN", "AND");
        }
        p_entete.addParam(p);
        loadHeadBulletin(true, true);
    }

    public void verifyComptabilised(Boolean comptabilised) {
        initForm = true;
        loadAllBulletin(true);
        if (comptabilised != null) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                List<YvsGrhBulletins> list = new ArrayList<>();
                list.addAll(listBulletins);
                for (YvsGrhBulletins y : list) {
                    y.setComptabilised(w.isComptabilise(y.getId(), Constantes.SCR_BULLETIN));
                    if (comptabilised ? !y.isComptabilised() : y.isComptabilised()) {
                        listBulletins.remove(y);
                    }
                }
            }
        }
        update("data_facture_vente");
    }

    public void chooseParamSalaire(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (Long) ev.getNewValue();
            int idx = listeParametre.indexOf(new YvsGrhParametresBulletin(id));
            if (idx >= 0) {
                codeParamSal = listeParametre.get(idx).getCodeElement();
                descParamSal = listeParametre.get(idx).getDescription();
                //charge la règle pour toute la liste affiché
            }
        }
    }

    public double giveValueElt(YvsGrhEmployes emp, String code) {
        Double re = 0d;
        if (emp != null ? emp.getContrat() != null : false) {
            if (currentBulletin.getEntete() != null ? currentBulletin.getEntete().getId() > 0 : false) {
                re = (Double) dao.loadObjectByNameQueries("YvsGrhParametresBulletin.findVOneParam", new String[]{"contrat", "header", "code"}, new Object[]{emp.getContrat(), new YvsGrhOrdreCalculSalaire(currentBulletin.getEntete().getId()), code});
            }
        }
        return re != null ? re : 0;
    }

    public void saveOrUpdateNewElt(CellEditEvent ev) {
        Double val = (Double) ev.getNewValue();
        Double Oldval = (Double) ev.getOldValue();
        int row = ev.getRowIndex();
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (currentBulletin.getEntete().getId() > 0) {
            if (row >= 0 && service != null) {
                YvsGrhEmployes emp = service.getListEmployes().get(row);
                if (emp.getContrat() != null) {
                    YvsGrhParametresBulletin p = (YvsGrhParametresBulletin) dao.loadOneByNameQueries("YvsGrhParametresBulletin.findOneParam", new String[]{"header", "contrat", "code"}, new Object[]{new YvsGrhOrdreCalculSalaire(currentBulletin.getEntete().getId()), emp.getContrat(), codeParamSal});
                    if (val != null ? val > 0 : false) {
                        if (p != null) {
                            p.setValeur(val);
                            p.setDateUpdate(new Date());
                            p.setAuthor(currentUser);
                            p.setActif(true);
                            dao.update(p);
                        } else {
                            p = new YvsGrhParametresBulletin(null);
                            p.setActif(true);
                            p.setAuthor(currentUser);
                            p.setCodeElement(codeParamSal);
                            p.setContrat(emp.getContrat());
                            p.setDateSave(new Date());
                            p.setDateUpdate(new Date());
                            p.setHeaderBulletin(new YvsGrhOrdreCalculSalaire(currentBulletin.getEntete().getId()));
                            p.setValeur(val);
                            p.setDescription(descParamSal);
                            p = (YvsGrhParametresBulletin) dao.save1(p);
                        }
                    } else {
                        System.err.println(" delete param...");
                        if (p != null) {
                            dao.delete(p);
                        }
                    }
                } else {

                }
            }
        } else {
            getErrorMessage("Vous devez choisir une période !");
        }
    }

    public void deleteOneElt(YvsGrhElementFormuleNonInterprete e) {
        try {
            dao.delete(e);
            reglesExcluConge.remove(e);
            update("table_regle_abs");
        } catch (Exception ex) {
            getException("Erreur lors de la Suppression ", ex);
        }
    }
}

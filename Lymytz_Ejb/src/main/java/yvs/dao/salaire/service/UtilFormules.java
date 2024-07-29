/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.salaire.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import javax.faces.application.FacesMessage;
import static org.primefaces.extensions.util.MessageUtils.getMessage;
import yvs.dao.DaoInterfaceLocal;
import static yvs.dao.salaire.service.UniteLexicale.calcul;
import static yvs.dao.salaire.service.UniteLexicale.calculLogique;
import static yvs.dao.salaire.service.UniteLexicale.isANumber;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.param.YvsGrhCategoriePreavis;
import yvs.entity.grh.param.YvsGrhConventionCollective;
import yvs.entity.grh.param.YvsGrhParamsTauxChomageTech;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.personnel.YvsCategorieProEmploye;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.personnel.YvsGrhConventionEmploye;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsGrhPersonneEnCharge;
import yvs.entity.grh.presence.YvsGrhTypeValidation;
import yvs.entity.grh.salaire.YvsGrhBulletins;
import yvs.entity.grh.salaire.YvsGrhDetailBulletin;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.grh.salaire.YvsGrhElementSalaire;
import yvs.entity.grh.salaire.YvsGrhElementStructure;
import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
import yvs.entity.param.YvsSocietes;

/**
 *
 * @author hp Elite 8300
 */
public class UtilFormules implements Serializable {

    private List<Options> erreurs;
    private List<Options> tempon;
    private Stack<String> pile = new Stack<>();
    YvsGrhEmployes currentEmps;
    YvsGrhContratEmps contrat;
    private Date debut, fin;
    private Date debutTraitement, finTraitement;
    private DaoInterfaceLocal dao;
    private YvsParametreGrh paramGrh;
    private YvsSocietes currentScte;
    private List<YvsGrhElementSalaire> listEltSalaire;
    private String[] champ;
    private Object[] val;
    YvsBaseExercice exo;
    YvsGrhOrdreCalculSalaire header;
    private DataPresence presence;

    public UtilFormules() {
        erreurs = new ArrayList<>();
        tempon = new ArrayList<>();
        listEltSalaire = new ArrayList<>();
    }

    public UtilFormules(YvsParametreGrh param, Date debut, Date fin, DaoInterfaceLocal dao, YvsSocietes scte, YvsGrhContratEmps contrat, YvsGrhEmployes employe, YvsGrhOrdreCalculSalaire header) {
        erreurs = new ArrayList<>();
        tempon = new ArrayList<>();
        listEltSalaire = new ArrayList<>();
        this.debut = debut;
        this.fin = fin;
        this.paramGrh = param;
        this.dao = dao;
        this.currentScte = scte;
        this.currentEmps = employe;
        this.contrat = contrat;
        this.header = header;
        this.debutTraitement = header.getDateDebutTraitement();
        this.finTraitement = header.getDateFinTraitement();
        presence = findPresence(contrat.getEmploye().getId(), debutTraitement, finTraitement);
    }

    public YvsGrhBulletins createBulletin(YvsGrhContratEmps contrat, Date debut, Date fin) {
        //récupère les données de présence               
        this.contrat = contrat;
        this.debut = debut;
        this.fin = fin;
        YvsGrhBulletins bs = null;
        if (contrat != null) {
            exo = giveExerciceActif(new Date());
            //création du bulletin
            if (contrat.getStructureSalaire() != null) {
                listEltSalaire = dao.loadNameQueries("YvsElementStructure.findElementStructure", new String[]{"structure"}, new Object[]{contrat.getStructureSalaire()});
                bs = buildBulletin(contrat.getStructureSalaire().getElementSalaire());
                bs.setListeRetenue(listRetenues);
            } else {
                getMessage("Erreur! Le contrat de l'employé n'a pas de structure de salaire", FacesMessage.SEVERITY_ERROR);
            }
        } else {
            getMessage("Erreur, L'employé ne dispose d'aucun contrat", FacesMessage.SEVERITY_ERROR);
        }
        return bs;
    }

    private DataPresence findPresence(long emplye, Date debut, Date fin) {
        List<Object[]> re = dao.findDataPresence(emplye, debut, fin);
        DataPresence dp = new DataPresence();
        if (re != null) {
            String type;
            for (Object[] line_ : re) {
                type = (String) line_[3];
                switch (type) {
                    case Constantes.JOUR_EFFECTIF:
                        dp.setDureePresenceEffectif((double) line_[2]);
                        break;
                    case Constantes.JOUR_COMPENSATOIRE:
                        dp.setJourCompensation((double) line_[2]);
                        break;
                    case Constantes.JOUR_SUPPLEMENTAIRE:
                        dp.setJourSup((double) line_[2]);
                        break;
                    case Constantes.JOUR_SUPPLEMENTAIRE_FERIE:
                        dp.setJourSupFerie((double) line_[2]);
                        break;
                    case Constantes.JOUR_REQUIS:
                        dp.setDureePresenceRequis((double) line_[2]);
                        break;
                    case Constantes.JOUR_NORMAL:
                        dp.setDureePresenceNormal((double) line_[2]);
                        break;
                    case Constantes.ABSENCE:
                        dp.setDureeAbsence((double) line_[2]);
                        break;
                    case Constantes.REPOS_EFFECTIF:
                        dp.setNbRepos((double) line_[2]);
                        break;
                    case Constantes.REPOS_REQUIS:
                        dp.setNbReposRequis((double) line_[2]);
                        break;
                    case Constantes.CONGE_MALADIE:
                        dp.setNbCongeMaladie((double) line_[2]);
                        break;
                    case Constantes.CONGE_ANNUEL:
                        dp.setNbCongeAnnuel((double) line_[2]);
                        break;
                    case Constantes.CONGE_TECHNIQUE:
                        dp.setNbCongeTechnique((double) line_[2]);
                        break;
                    case Constantes.PERMISSION_COURT_DUREE:
                        dp.setNbPermissionCD((double) line_[2]);
                        break;
                    case Constantes.PERMISSION_LONG_DUREE:
                        dp.setNbPermissionLD((double) line_[2]);
                        break;
                    case Constantes.FERIE:
                        dp.setNbFerie((double) line_[2]);
                        break;
                    case Constantes.MISSION:
                        dp.setNbMission((double) line_[2]);
                        break;
                    case Constantes.FORMATION:
                        dp.setNbFormation((double) line_[2]);
                        break;
                }
            }
        }
        return dp;
    }

    //fabrique le bulletin de salaire d'un employé à partir des règle de sa structure de salaire
    private YvsGrhBulletins buildBulletin(List<YvsGrhElementStructure> list) {
        YvsGrhBulletins bs = new YvsGrhBulletins();
        for (YvsGrhElementStructure e : list) {
            YvsGrhDetailBulletin b = buildElementBulletin(e.getElement());
            if (bs.getListDetails() == null) {
                bs.setListDetails(new ArrayList<YvsGrhDetailBulletin>());
            }
            bs.getListDetails().add(calculDtailB(b));
        }

        return bs;
    }

    private YvsGrhDetailBulletin calculDtailB(YvsGrhDetailBulletin db) {
        switch (db.getElement().getTypeMontant()) {
            case 1://montant fixe
                if (db.getElement().getRetenue()) {
                    db.setRetenuSalariale(db.getBase());
                } else {
                    db.setMontantPayer(db.getBase());
                }
                break;
            case 2://pourcentage
                if (db.getElement().getRetenue()) {
                    db.setRetenuSalariale(db.getBase() * db.getElement().getPoucentageSalarial() / 100);
                    db.setMontantEmployeur(db.getBase() * db.getElement().getPoucentagePatronale() / 100);
                    db.setTauxPatronal(db.getElement().getPoucentagePatronale());
                    db.setTauxSalarial(db.getElement().getPoucentageSalarial());
                } else {
                    db.setMontantPayer(db.getBase() * db.getElement().getMontant() / 100);
                    db.setTauxSalarial(db.getElement().getMontant());
                }
                break;
            case 3:
            case 4:
                if (db.getElement().getRetenue()) {
                    db.setRetenuSalariale(db.getBase() * db.getQuantite());
                } else {
                    db.setMontantPayer(db.getBase() * db.getQuantite());
                }
                break;
            default:
                break;

        }
        return db;
    }

    /**
     * ********************************DEBUT DE TRAITEMENTS DES ELEMENT
     *
     * @param expression
     * @param codeElt
     * @return **********************************
     */
    public List<Lexemes> buildLexemeToLexeme(String expression, String codeElt) {
        List<Lexemes> tab = new ArrayList<>();
        if (expression != null) {
            if (!expression.isEmpty()) {
                FactoryLexmes bl = new FactoryLexmes(expression);
                Lexemes l;
                do {
                    l = bl.suivant(codeElt);
                    if (l != null) {
                        tab.add(l);
                    }
                } while (l != null);
            } else {
                erreurs.add(new Options("Erreur Lexème incorrecte", "L'expression correspondant à " + codeElt + " est vide "));
            }
        } else {
            erreurs.add(new Options("Erreur Lexème incorrecte", "L'expression correspondant à " + codeElt + " est null "));
        }
        return tab;
    }

    //cette fonction retourne la chaîne numérique d'une expression de salaire. ie une chaîne où les variables ont été remplacé par leurs valeurs numérique
    public String rewriteExpression(List<Lexemes> l) {
        String newExpression = "";
        //verifie que le lexème se trouve dans la liste des identifiants enregistrée.
        for (Lexemes lx : l) {
            if (lx.nature == UniteLexicale.ID_VAR) {
                //si le lexème est une variable, recherche sa valeur.
                //Si  la variable commence par ABS (invocation de la fonction valeur absolu)
                if (!lx.nom.startsWith("ABS") && !lx.nom.startsWith("TRUNC")) {
                    String str = findVar(lx);
                    if (str == null) {
                        erreurs.add(new Options(lx.code, "Impossible de localiser la variable "));
                        return null;
                    }
                    newExpression += " " + str;
                } else {
                    if(lx.nom.startsWith("ABS")){
                        //extraire l'expression à évaluer
                        newExpression += " " + evalueFonctionABS(lx.nom.substring(3, lx.nom.length()), "");
                    }else if(lx.nom.startsWith("TRUNC")){
                        newExpression += " " + evalueAndTruncate(lx.nom.substring(3, lx.nom.length()), "");
                    }
                }
            } else if (lx.nature == UniteLexicale.MOT_CLE) {
                newExpression += " ";
            } else {
                newExpression += " " + lx.nom;
            }
        }
        return newExpression;
    }

    public String convertECPToEPOST(String chaine) {
//        System.err.println(" --- Expression par ==  " + chaine);
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
                    if (!operator.isEmpty()) {
                        if (")".equals(token)) {
                            //Si le token courant est une parenthèse fermante, on dépile sur la sortie tant qu'on ne trouve pas de parenthèse ouvrante
                            while (!"(".equals(operator.peek())) {
                                out.push(operator.pop());
                            }
                            operator.pop();//enleve la parenthese
                        } else {
                            operator.push(token);
                        }
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
        return result;
    }

    //renvoi true si la chaine passé en paramètre est un opérateur
    private boolean operateur(String str) {
        return str.equals(UniteLexicale.PLUS) || str.equals(UniteLexicale.MOINS) || str.equals(UniteLexicale.FOIS) || str.equals(UniteLexicale.DIV)
                || str.equals(UniteLexicale.SUP) || str.equals(UniteLexicale.INF) || str.equals(UniteLexicale.EGAL) || str.equals(UniteLexicale.ET) || str.equals(UniteLexicale.OU);
    }

    public String evalueExpression(String chaine) {
        String result = null;
        pile.clear();
        String ch[] = chaine.split(" ");
        int i = 0;
        while (!ch[i].equals("#")) {
            if (!ch[i].trim().isEmpty()) {
                if (!operateur(ch[i])) {
                    pile.push(ch[i]);
                } else {
                    try {
                        String str1 = "";
                        String str2 = "";
                        try {
                            str1 = pile.pop();
                        } catch (EmptyStackException e) {
                        }
                        try {
                            str2 = pile.pop();
                        } catch (EmptyStackException e) {
                        }
                        if (isANumber(str1) && isANumber(str2)) {
                            double op1 = Double.valueOf(str1);
                            double op2 = Double.valueOf(str2);
                            double r = calcul(ch[i], op1, op2);
                            pile.push("" + r);
                        } else {
                            return null;
                        }
                    } catch (EmptyStackException ex) {

                    }
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
        String ch[] = chaine.split(" ");
        pile.clear();
        int i = 0;
        while (!ch[i].equals("#")) {
            if (!ch[i].trim().isEmpty()) {
                if (!operateur(ch[i])) {
                    pile.push(ch[i]); //empile
                } else {
                    try {
                        String str1 = "";
                        String str2 = "";
                        try {
                            str1 = pile.pop();
                        } catch (EmptyStackException e) {
                        }
                        try {
                            str2 = pile.pop();
                        } catch (EmptyStackException e) {
                        }
                        if (isANumber(str1) && isANumber(str2)) {
                            double op1 = Double.valueOf(str1);
                            double op2 = Double.valueOf(str2);
                            double r = calculLogique(ch[i], op1, op2);
                            pile.push("" + r);
                        } else {
                            return false;
                        }
                    } catch (EmptyStackException ex) {

                    }
                }
            }
            i++;
        }
        if (!pile.isEmpty()) {
            result = (pile.pop());
        }
        result = ((result != null) ? (result.isEmpty() ? "" : result) : "");
        int r = (int) Double.parseDouble((result.isEmpty()) ? "0" : result);
        return r >= 1;
    }

    private double evalueFonctionABS(String exp, String codeElt) {
        List<Lexemes> tab = buildLexemeToLexeme(exp, codeElt);
        if (tab != null) {
            String formule = rewriteExpression(tab);
            if (formule != null) {
                if (!formule.trim().equals("")) {
                    formule += " #";
                    String re = evalueExpression(convertECPToEPOST(formule.trim()));
                    if (!re.trim().isEmpty()) {
                        return Math.abs(Double.valueOf(re));
                    }
                }
            } else {
                return 0;
            }
        } else {
            //une erreur dans l'expression algébrique a empêcher la générattion de la table des lexèmes
            return 0;
        }
        return 0;
    }

    private double evalueAndTruncate(String exp, String codeElt) {
        List<Lexemes> tab = buildLexemeToLexeme(exp, codeElt);
        if (tab != null) {
            String formule = rewriteExpression(tab);
            if (formule != null) {
                if (!formule.trim().equals("")) {
                    formule += " #";
                    String re = evalueExpression(convertECPToEPOST(formule.trim()));
                    if (!re.trim().isEmpty()) {
                        return Math.floor((Double.valueOf(re)));
                    }
                }
            } else {
                return 0;
            }
        } else {
            //une erreur dans l'expression algébrique a empêcher la générattion de la table des lexèmes
            return 0;
        }
        return 0;
    }

    //cette méthode évalue les éléments d'une catégorie et revoie leur soe
    private double buildCategorie(List<YvsGrhElementSalaire> list) {
        double result = 0;
        for (YvsGrhElementSalaire lx : list) {
            String re = findVar(new Lexemes(lx.getCode(), false));
            if (re != null) {
                if (!re.isEmpty()) {
                    result = result + Double.valueOf(re);
                }
            }
//                result += calculQuantite(lx.getCode(), "");
        }
        return result;
    }

//évalue une règle de salaire pour fabriquer un élément du bulletin
    public YvsGrhDetailBulletin buildElementBulletin(YvsGrhElementSalaire e) {
        YvsGrhDetailBulletin db = new YvsGrhDetailBulletin();
        db.setElement(e);
        switch (e.getTypeMontant()) {
            case 1://montant fixe
                db.setBase(e.getMontant());
                db.setQuantite(1.0);
                break;
            case 2://pourcetage
                String re = findVar(new Lexemes(e.getBasePourcentage().getCode(), false));
                if (re != null) {
//                    db.setBase(calculQuantite(e.getBasePourcentage().getCode(), e.getCode()));
                    if (!re.isEmpty()) {
                        db.setBase(Double.valueOf(re));
                        db.setQuantite(e.getMontant());
                    }
                }

                break;
            case 3://expression 
                String expression = e.getExpressionRegle();
                if (expression.startsWith("SI")) {
                    ExpressionConditionel ef = new ExpressionConditionel("", (e.getExpressionRegle() != null) ? e.getExpressionRegle() : "");
                    ef = extractExpressionConditionnel(ef);  //l'utilisation de cette méthode vise à traiter des cas particulier d'expression conditionnelle
                    if (ef != null) {
                        db.setTrancheMin(ef.getTrancheMin());
                        db.setTrancheMax(ef.getTrancheMax());
                        expression = ef.getExpression();
                    } else {
                        expression = "";
                    }
                }
                List<Lexemes> tab = buildLexemeToLexeme(expression, e.getCode());
                if (tab != null) {
                    String formule = rewriteExpression(tab);
                    if (formule != null) {
                        formule = formule.concat(" #");
                        String epf = convertECPToEPOST(formule.trim());
                        formule = evalueExpression(epf);
                        if ((formule != null) ? !formule.trim().equals("") : false) {
                            db.setBase(Double.valueOf(formule));
                            db.setQuantite(1.0);
                        }
                    }
                } else {
                    //il y a une erreur dans l'expression algébrique; et la table des lexèmes n'a pas  pu être générée
                }
                break;
            case 4://quantité
                String re_ = findVar(new Lexemes(e.getBasePourcentage().getCode(), false));
                if (re_ != null) {
//                    db.setBase(calculQuantite(e.getBasePourcentage().getCode(), e.getCode()));
                    if (!re_.isEmpty()) {
                        db.setQuantite(Double.valueOf(re_));
                    }

                }
                String re__ = findVar(new Lexemes(e.getQuantite().getCode(), false));
                if (re__ != null) {
//                    db.setBase(calculQuantite(e.getBasePourcentage().getCode(), e.getCode()));
                    if (!re__.isEmpty()) {
                        db.setBase(Double.valueOf(re__));
                    }

                }
//                db.setQuantite(calculQuantite(e.getBasePourcentage().getCode(), e.getCode()));
//                db.setBase(calculQuantite(e.getQuantite().getCode(), e.getCode()));
                break;
            default://constante système (0)
                db.setBase(e.getMontant());
                db.setQuantite(1.0);
                break;
        }
        return db;
    }

    /**
     * *
     * @param expression
     * @return
     */
    /*cette méthode, permet d'extraire l'expression algébrique d'une règle de salaire.
     *elle trouve son sens dans les cas où l'expression algébrique à évaluer est donnée sous forme conditionnelle.
     *car si l'expression est une condition, on a besoin de n'en extraire que l'expression qui répond au bon critère de la condition
     * Ainsi, la sortie de cette méthode est encore une expression algébrique à évaluer ou rien si l'expression conditionnelle n'est pas correct
     */
    public ExpressionConditionel extractExpressionConditionnel(ExpressionConditionel expression) {
//        String resultat = "";
        ExpressionConditionel expesult = expression;
        UtilExpression utilE;
        if (expression != null) {
            if (!expression.getExpression().trim().startsWith("SI")) {
                expesult = (expression);
            } else {
                utilE = new UtilExpression(expression.getExpression());
                utilE.decompose();
                //ce tableau contient les conditions et les expressions correspondantes
                expesult = extractExpressionLogique(utilE.tab); //cette variable  contient la branche conditionnelle à évaluer
                if (expesult != null) {
                    expesult = extractExpressionConditionnel(expesult);
                }
            }
        }
        return expesult;
    }

    private ExpressionConditionel extractExpressionLogique(List<ExpressionConditionel> tab) {
        //parcours les conditions
        for (ExpressionConditionel tab1 : tab) {
            String str = tab1.getCondition();
            //évalue str; cette évaluation consiste en l'évaluation d'une expression logique; si elle retourne true alors l'expression correcte sera celle à la colonne1
            if ((str != null) ? !str.trim().equals("") : false) {
                //evalue la condition
                ExpressionConditionel e = conditionCorrect(str);
                if (e.isConditionOk()) {
                    tab1.setTrancheMin(e.getTrancheMin());
                    tab1.setTrancheMax(e.getTrancheMax());
                    return tab1;
                }
            } else {
                //on est dans le sinon
                return tab1;
            }
        }
        return null;
    }

    //cette méthode evalue une expression conditionnelle (i.e une comparaison en générale)
    private ExpressionConditionel conditionCorrect(String expression) {
        List<Lexemes> tab = new ArrayList<>();
        boolean result;
        ExpressionConditionel formIntervale = new ExpressionConditionel();
        if ((expression != null) ? !expression.trim().equals("") : false) {
            FactoryLexmes bl = new FactoryLexmes(expression);
            //décomposition de l'expréssion en lexèmes élémentaire
            Lexemes l;
            int i = 0;
            while ((l = bl.suivant(expression)) != null) {
                tab.add(l);
            }
            //tab contient les lexèmes de la condition
            //1. examinons les lexèmes de cette condition            
            String formule = rewriteExpression(tab);
            if ((formule != null) ? !formule.trim().equals("") : false) {
                result = evalueExpressionLogique(convertECPToEPOST(formule.trim().concat(" #")));
                formIntervale.setConditionOk(result);
            } else {
                result = true;
                formIntervale.setConditionOk(true);
            }
        } else {
            result = true;
            formIntervale.setConditionOk(true);
        }
        if (result) {
            formIntervale = examineFormeIntervalle(tab);
            formIntervale.setConditionOk(true);
        }
        return formIntervale;
    }

     /////////////
    /*Examine les forme conditionnelle intervalle*/
    /////
    private ExpressionConditionel examineFormeIntervalle(List<Lexemes> lx) {
        ExpressionConditionel result = new ExpressionConditionel();
        List<Lexemes> re = removeParenthese(lx);
        //compte les signes < et/ou > de la liste
        int cpt = 0;
        int nbComparateurInf = 0;
        int nbComparateurSup = 0;
        for (Lexemes l : re) {
            if (l.nature == '<') {
                //contrôle le lexeme de gauche
                if (cpt > 0) {
                    if (re.get(cpt - 1).nature == UniteLexicale.NOMBRE) { //Si à gauche du symbole < on a un nombre, c'est le minimun
                        result.setTrancheMin(re.get(cpt - 1).valeur);
                    } else { //sinon, on à la tranche max à droite
                        if (re.size() > cpt + 1) {
                            if (re.get(cpt + 1).nature == UniteLexicale.NOMBRE) {
                                result.setTrancheMax(re.get(cpt + 1).valeur);
                            }

                        }
                    }
                }
                nbComparateurInf++;
            } else if (l.nature == '>') {
                if (cpt > 0) {
                    if (re.get(cpt - 1).nature == UniteLexicale.NOMBRE) { //Si à gauche du symbole < on a un nombre, c'est le minimun
                        result.setTrancheMax(re.get(cpt - 1).valeur);
                    } else { //sinon, on à la tranche max à droite
                        if (re.size() > cpt + 1) {
                            if (re.get(cpt + 1).nature == UniteLexicale.NOMBRE) {
                                result.setTrancheMin(re.get(cpt + 1).valeur);
                            }

                        }
                    }
                }

                nbComparateurSup++;
            }
            cpt++;
        }
        if (nbComparateurInf > 1 || nbComparateurSup > 1) {
            return new ExpressionConditionel();
        }
        return result;
    }

    private List<Lexemes> removeParenthese(List<Lexemes> lx) {
        List<Lexemes> re = new ArrayList<>();
        if (lx != null) {
            for (Lexemes l : lx) {
                if (l.nature != ')' && l.nature != '(') {
                    re.add(l);
                }
            }
        }
        return re;
    }

    private String findVarInListTempon(String code) {
        if (code != null) {
            if (tempon.contains(new Options(code))) {
                return tempon.get(tempon.indexOf(new Options(code))).getValeur();
            } else {
                return null;
            }
        }
        return null;
    }

    public String findVar(Lexemes lxVar) {
        String re;
        //recherche dans la liste tempon
        if ((re = findVarInListTempon(lxVar.code)) != null) {
            return re;
        } else { // si la règle recherché n'a pas encore été calculé
            champ = new String[]{"code", "structure"};
            val = new Object[]{lxVar.code, contrat.getStructureSalaire()};
            YvsGrhElementSalaire elt;
            //si l'élément est une catégorie, on calcul la soe des éléments de la catégorie
            if (lxVar.code.startsWith("categorie")) {
                List<YvsGrhElementSalaire> lcat = dao.loadNameQueries("YvsElementStructure.findElementStructureByCat", new String[]{"categorie", "structure"}, new Object[]{UtilExpression.extract(lxVar.code.trim(), "categorie."), contrat.getStructureSalaire()});
                double r = buildCategorie(lcat);
                return " " + r;

            } else if (lxVar.code.startsWith("employe")) {
                //évalue les éléments de salaire disponible sur la fiche employé (Elément fixé sur la période)                
                re = " " + calculEltSalaire(lxVar.code, currentEmps, contrat);
                //Récupère un élément calculé d'une variable de salaire calculé sur le bulletin courant.(par exemple cbulletin.PV.base= donée base base de la pension viellesse)
            } else if (lxVar.code.startsWith("cbulletin")) {
                String[] part = split(lxVar.code);
                re = "" + findSpecificEltFromCurrentBulletin(part[1], part[2]);
            } else if (lxVar.code.startsWith("bulletin")) {
                String[] part = split(lxVar.code);
                if (part.length == 3) {
                    //recherche l'élément de salaire sur le bulletin du mois précisé par le troisième élément du tableau
                    re = " " + findLastEltSalaire(part[1], part[2]);
                } else if (part.length == 2) {
                    //recherche l'élément de salaire sur le bulletin en cours (formulaire en édition)
                    re = " " + findElementSalaireInParamaBulletin(part[1]);
                }
                //récupère le salaire d'un mois relativement au mois de traitement en cours
            } else if (lxVar.code.startsWith("salaire.S_")) {
                re = " " + findLastSalaire(UtilExpression.extract(lxVar.code, "salaire.S_"), fin);
                //récupère le nombre de jour effectif de travail d'un mois relativement au mois en cours
            } else if (lxVar.code.startsWith("duree.M_")) {
                re = " " + findNbreDayInMonth(UtilExpression.extract(lxVar.code, "duree.M_")); //récupère le nombre de jour de trvail dans un mois donnée
            } else if (lxVar.code.startsWith("contrat")) {
                //rechercher l'élément dans les contrat
                re = " " + givePrime(UtilExpression.extract(lxVar.code, "contrat."));
            } else if (lxVar.code.startsWith("retenue")) {
                //récupère une retenue spécifique
                re = " " + giveMontantRetenue(UtilExpression.extract(lxVar.code, "retenue."));
            } else if (lxVar.code.startsWith("conge")) {
                //récupère les élément de durée de congé (Durée effective, chomage technique, etc.)
                re = " " + (findDurreConge(lxVar.code));
                //récupère un élémet sur la fiche de pointage
            } else if (lxVar.code.startsWith("pointage.")) {
                //compte le nombre de jour de travail dont le code de validation est donné
                re = " " + calculeNbJourValideByCode(UtilExpression.extract(lxVar.code, "pointage."));
            } else if (lxVar.code.startsWith("presence.")) {
                re = " " + calculEltPresence(lxVar.code, contrat);
            } else if (lxVar.code.startsWith("mission.")) {
                re = " " + calculEltMission(lxVar.code, contrat);
            } else if (lxVar.code.startsWith("tauxP")) {
                //cherche le taux de validation pour une fiche de pointage
                re = " " + findTauxValidationPointage(UtilExpression.extract(lxVar.code, "tauxP."));
            } else {
                //si la règle est une règle de salaire défini par l'utilisateur
                elt = (YvsGrhElementSalaire) dao.loadOneByNameQueries("YvsGrhElementStructure.findElementInStructure", champ, val);
                if (elt != null) {
                    YvsGrhDetailBulletin eb = buildElementBulletin(elt);
                    switch (eb.getElement().getTypeMontant()) {
                        case 1:
                            re = elt.getMontant() + "";
                        case 3:
                            re = " " + eb.getBase();
                            break;
                        case 2:
                            if (lxVar.retenu) {
                                re = " " + (eb.getBase() * (eb.getElement().getPoucentagePatronale() + eb.getElement().getPoucentageSalarial()) / 100);
                            } else {
                                re = " " + (eb.getBase() * (eb.getElement().getMontant()) / 100);
                            }
                            break;
                        case 4:
                            re = " " + (eb.getBase() * eb.getQuantite());
                            break;
                        default:
                            re = " 0";
                            break;
                    }
                } else {
                    //si la règle n'est pas localiser parmi les règles défini par l'utilisateur
//                    getMessage("Impossible de terminer. Votre expression est incorrecte", FacesMessage.SEVERITY_ERROR);
                    re = " 0";
                }
            }
        }
        yvs.dao.salaire.service.Options o = new yvs.dao.salaire.service.Options("" + re, lxVar.code);
        tempon.add(o);
        return re;
    }

    private double calculEltSalaire(String code, YvsGrhEmployes currentEmps, YvsGrhContratEmps contrat) {
        double result = 0;
        switch (code) {
            case Constantes.S_JOUR_DE_CONGE:
                result = getDureeCongeRestant(currentEmps.getId()); //renvoie le nombre de congé restatnt de l'emplyé
                break;
            case Constantes.S_SALAIRE_CONTRACTUELLE:
                //si le contrat spécifie un salaire à la tâche
                if (contrat != null) {
                    result = contrat.getSalaireMensuel();
                }
                break;
            case Constantes.S_SALAIRE_HORAIRE:
                if (contrat != null) {
                    if (contrat.getSalaireHoraire() != null) {
                        if (contrat.getSalaireHoraire() != 0) {
                            result = contrat.getSalaireHoraire();
                        } else {
                            result = contrat.getSalaireMensuel() / contrat.getHoraireMensuel();
                        }
                    } else {
                        result = contrat.getSalaireMensuel() / contrat.getHoraireMensuel();
                    }
                }
                break;
            case Constantes.S_SALAIRE_CONVENTIONNELLE:
                if (contrat != null) {
                    //récupère la convention active d'un employé
                    champ = new String[]{"employe"};
                    val = new Object[]{contrat.getEmploye()};
                    YvsGrhConventionEmploye cc = (YvsGrhConventionEmploye) dao.loadOneByNameQueries("YvsConventionEmploye.findByEmploye", champ, val);
                    if (cc != null) {
                        //recherche l'echelon A 
                        YvsGrhConventionCollective c = (YvsGrhConventionCollective) dao.loadOneByNameQueries("YvsConventionCollective.findByCatEchMin", new String[]{"categorie", "societe"}, new Object[]{cc.getConvention().getCategorie(), currentScte});
                        if (c != null) {
                            result = c.getSalaireMin();
                        }
                    }
                }
                break;
            case Constantes.S_SALAIRE_SUR_OBJETIF:
                result = getSalaireTache(contrat.getEmploye().getId());
                break;
            case Constantes.S_HEURE_SUPPLEMENTAIRE:
                result = getHeureSup(currentEmps.getId());
                break;
            case Constantes.S_HEURE_DIMANCHE:
                result = getHeureSupDimanche(currentEmps.getId());
                break;
            case Constantes.S_HEURE_JOUR_FERIE:
                result = getHeureSupJFerie(currentEmps.getId());
                break;
            case Constantes.S_ENFANT:
                result = giveNbEnfant(contrat);
                break;
            case Constantes.S_ANCIENETE:
                if (contrat != null) {
                    if (contrat.getDateDebut() != null) {
                        result = Constantes.calculNbYear(contrat.getDateDebut(), fin);
                    }
                }
                break;
            case Constantes.S_DUREE_EMBAUCHE:
                if (contrat != null) {
                    if (contrat.getEmploye().getDateEmbauche() != null) {
                        result = Constantes.calculNbYear(contrat.getEmploye().getDateEmbauche(), fin);
                    }
                }
                break;
            case Constantes.S_NB_HEURE_EFFECTIF:
                //récupère le nombre d'heure de tavail effectif
                result = giveHourOfWorkReal(contrat.getEmploye().getId());
                break;
            case Constantes.S_NB_HEURE_REQUISE:
                result = giveHourOfWorkPrevu(contrat.getEmploye().getId());
                break;
            case Constantes.S_NB_JOUR_EFFECTIF:
                if (presence != null) {
                    result = presence.getDureePresenceEffectif();
                }
                break;
            case Constantes.S_NB_JOUR_REQUIS:
                result = presence.getDureePresenceRequis();
                break;
            case Constantes.S_NOTE_DE_FRAIS:
                result = giveNoteDeFrais();
                break;
            case Constantes.S_DUREE_PREAVIS:
                result = givePreavis();
                break;
            case Constantes.S_NB_JOUR_PASSE:
                result = countDayMonth(fin);
                break;
            case Constantes.S_NB_MOIS_ACCEPTE_IN_EXO:
                result = countMonthAcceptInYear();
                break;
            case Constantes.S_NB_JOUR_ACCEPTE_IN_EXO:
                result = countDaysAcceptInYear();
                break;
            case Constantes.S_BASE_DUREE_CONGE:
                result = giveBaseCalculConge(paramGrh);
                break;
            default:
                result = 0;
                break;
        }
        return result;
    }

    private double calculEltPresence(String code, YvsGrhContratEmps contrat) {
        double result = 0;
        switch (code) {
            case Constantes.POINTAGE_NB_FICHE:
                result = findNbPointage(contrat.getEmploye());
                break;
            case Constantes.GRH_DUREE_POINTAGE_REPOS:
                result = findDureeReposPointe(contrat.getEmploye());    //nombre de jour de repos pointé
                break;
            case Constantes.GRH_DUREE_ABSENCE:
                result = presence.getDureeAbsence();
                break;
            case Constantes.GRH_DUREE_RETARD:
                result = findDureeRetard(currentEmps, finTraitement);
                break;
            default:
                break;
        }
        return result;
    }

    private double calculEltMission(String code, YvsGrhContratEmps contrat) {
        double result = 0;
        switch (code) {
            case Constantes.GRH_DUREE_MISSION:
                result = presence.getNbMission();
                break;
            default:
                break;
        }
        return result;
    }

    private long findNbPointage(YvsGrhEmployes e) {
        Long l = (Long) dao.loadOneByNameQueries("YvsGrhPresence.findFichePointe", new String[]{"employe", "debut", "fin"}, new Object[]{e, debutTraitement, finTraitement});
        return (l != null) ? l : 0;
    }

    private double findDureeRetard(YvsGrhEmployes e, Date date) {
        String requete = "SELECT SUM(COALESCE(p.total_retard, 0) - COALESCE(r.retard_valorise, 0)) FROM yvs_grh_presence p "
                + "LEFT JOIN yvs_grh_valorisation_retard r ON r.fiche=p.id "
                + "WHERE p.employe=? AND p.valider IS TRUE AND p.date_fin< ?";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(e.getId(), 1), new yvs.dao.Options(date, 2)};
        Double v = (Double) dao.loadObjectBySqlQuery(requete, param_);
        return v != null ? v : 0;
    }

    private double findDureeReposPointe(YvsGrhEmployes e) {
        String requete = "select public.jour_repos_pointe(?,?,?)";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(e.getId(), 1), new yvs.dao.Options(debutTraitement, 2), new yvs.dao.Options(finTraitement, 3)};
        return (Double) dao.callFonction(requete, param_);
    }

    private double findDureeAbsence(YvsGrhEmployes e) {
        String requete = "select public.grh_presence_duree_month(?,?,?,?)";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(e.getId(), 3), new yvs.dao.Options(debutTraitement, 1), new yvs.dao.Options(finTraitement, 2), new yvs.dao.Options(2, 4)};
        return (Double) dao.callFonction(requete, param_);
    }
    /*Détermine le nombre de jour de congé restant dans le mois*/

    private double getDureeCongeRestant(long idEmp) {
        YvsBaseExercice exo = giveExerciceActif(new Date());
        int d = (int) dao.getNbreCongeSuppDu(idEmp, fin, (exo != null) ? exo.getId() : 0) + (int) dao.getNbreCongePrincipalDu(idEmp, fin, (exo != null) ? exo.getId() : 0);
        int p = (int) dao.getNbreJourCongePris(idEmp, fin, (exo != null) ? exo.getId() : 0)
                + (int) dao.getNbreJourPermissionPris(idEmp, fin, (exo != null) ? exo.getId() : 0, Constantes.GRH_PERMISSION_SUR_CONGE);
        return d - p;
//        return (Double) dao.getNbreJourConge(idEmp, fin);
    }

    private double findDureeMission(YvsGrhEmployes e) {
        String requete = "select public.grh_presence_duree_month(?,?,?,?)";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(e.getId(), 3), new yvs.dao.Options(debutTraitement, 1), new yvs.dao.Options(finTraitement, 2), new yvs.dao.Options(3, 4)};
        return (Double) dao.callFonction(requete, param_);
    }

    private double getSalaireTache(long idEmp) {
        String requete = "select public.tache_montant_total(?,?,?)";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(idEmp, 1), new yvs.dao.Options(debutTraitement, 2), new yvs.dao.Options(finTraitement, 3)};
        return (Double) dao.callFonction(requete, param_);
    }

    private double getHeureSup(long idEmp) {
        String requete = "SELECT SUM(y.total_Heure_Sup) FROM Yvs_Grh_Presence y WHERE y.valider=true AND y.employe=? AND "
                + "(y.date_Debut BETWEEN ? AND ?)";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(idEmp, 1), new yvs.dao.Options(debutTraitement, 2), new yvs.dao.Options(finTraitement, 3)};
        Double d = (Double) dao.callFonction(requete, param_);
        return (d > 0) ? (d) : 0;
    }

    private double getHeureSupDimanche(long idEmp) {
        String requete = "SELECT SUM(y.total_Heure_Sup) FROM Yvs_Grh_Presence y WHERE y.valider=true AND y.employe=? AND "
                + "(y.date_debut BETWEEN ? AND ?) AND extract (DOW FROM y.date_debut)=0";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(idEmp, 1), new yvs.dao.Options(debutTraitement, 2), new yvs.dao.Options(finTraitement, 3)};
        Double d = (Double) dao.callFonction(requete, param_);
        return (d > 0) ? (d) : 0;
    }

    private double getHeureSupJFerie(long idEmp) {
        String requete = "SELECT SUM(y.total_Heure_Sup) FROM Yvs_Grh_Presence y WHERE y.employe=? AND y.valider=true AND y.date_debut IN "
                + "(SELECT distinct alter_date(js.jour::timestamp, 'year', y.date_debut) FROM yvs_jours_feries js "
                + "WHERE (((alter_date(js.jour, 'year', y.date_debut) BETWEEN ? AND ?) AND js.all_year is true) "
                + "OR ((js.jour BETWEEN ? AND ?) AND js.all_year is false)))AND y.date_debut BETWEEN ? AND ?";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(idEmp, 1), new yvs.dao.Options(debutTraitement, 2), new yvs.dao.Options(finTraitement, 3), new yvs.dao.Options(debutTraitement, 4), new yvs.dao.Options(finTraitement, 5),
            new yvs.dao.Options(debutTraitement, 6), new yvs.dao.Options(finTraitement, 7)};
        Double d = (Double) dao.callFonction(requete, param_);
        return (d > 0) ? (d) : 0;
    }

    //récupère le nombre d'heure  de travail effectif
    private double giveHourOfWorkReal(long idEmp) {
        String requete = "SELECT SUM(y.total_presence) FROM Yvs_Grh_Presence y WHERE y.valider=true AND y.employe=? AND (y.date_debut BETWEEN ? AND ?) AND extract (DOW FROM y.date_debut)!=0";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(idEmp, 1), new yvs.dao.Options(debutTraitement, 2), new yvs.dao.Options(finTraitement, 3), new yvs.dao.Options(true, 4)};
        Double d = (Double) dao.callFonction(requete, param_);
        return (d > 0) ? (d) : 0;
    }

    //récupère le nombre d'heure de travail requis
    private double giveHourOfWorkPrevu(long idEmp) {
        String requete = "select public.heure_travail_requis(?,?,?)";

        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(idEmp, 1), new yvs.dao.Options(debutTraitement, 2), new yvs.dao.Options(finTraitement, 3)};
        return (Double) dao.callFonction(requete, param_);
    }

    //récupère le nombre de jour de travail prévu
    private double giveDayOfWorkPrevu(long idEmp) {
        String requete = "select public.jour_travail_requis(?,?,?)";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(idEmp, 1), new yvs.dao.Options(debutTraitement, 2), new yvs.dao.Options(finTraitement, 3)};
        return (Double) dao.callFonction(requete, param_);
    }

    //recherche les notes de frais validé etnon payé
    private double giveNoteDeFrais() {
        champ = new String[]{"employe", "debut", "fin"};
        val = new Object[]{contrat.getEmploye(), debut, fin};
        Double d = (Double) dao.loadObjectByNameQueries("YvsGrhDepenseNote.findNoteNonPaye", champ, val);
        if (d != null) {
            return d;
        } else {
            return 0;
        }
    }
    private List<YvsGrhDetailPrelevementEmps> listRetenues;

    //recherche dans la table des prélèvement, les retenues lié à l'employé
    private double giveMontantRetenue(String codeRetenue) {
        champ = new String[]{"contrat", "fin"};
        val = new Object[]{contrat, finTraitement};
        double r = 0;
        listRetenues = dao.loadNameQueries("YvsGrhDetailPrelevementEmps.findByDatePrelevement", champ, val);
        for (YvsGrhDetailPrelevementEmps d : listRetenues) {
            if (d.getRetenue().getTypeElement().getCodeElement().equals(codeRetenue)) {
                r = r + d.getValeur();
            }
        }
        return r;
    }

    //recherche la durée de préavis d'un employé
    private double givePreavis() {
        double duree = 0;
        if ((contrat.getDureePreavie() != null) ? contrat.getDureePreavie() != 0 : false) {
            if (contrat.getUnitePreavis().trim().toLowerCase().equals("jour")) {
                return contrat.getDureePreavie() / 30;
            } else {
                return contrat.getDureePreavie();
            }
        } else {
            //récupère la catégorie e l'employé
            champ = new String[]{"employe"};
            val = new Object[]{contrat.getEmploye()};
            YvsCategorieProEmploye ce = (YvsCategorieProEmploye) dao.loadOneByNameQueries("YvsCategorieProEmploye.findByEmploye", champ, val);
            //récupère son ancienneté   
            double anc = giveAnciennete(contrat);
            if (ce != null) {
                //récupère le préavis correspondant
                champ = new String[]{"categorie", "ancienete"};
                val = new Object[]{contrat.getEmploye(), anc};
                YvsGrhCategoriePreavis cp = (YvsGrhCategoriePreavis) dao.loadOneByNameQueries("YvsGrhCategoriePreavis.findPreavis", champ, val);
                if (cp != null) {
                    if (cp.getUnitePreavis().trim().toLowerCase().equals("jour")) {
                        duree = cp.getPreavis() / 31;
                    } else {
                        duree = cp.getPreavis();
                    }
                }
            }
        }
        return duree;
    }

    private double giveAnciennete(YvsGrhContratEmps c) {
        double result = 0;
        if (c != null) {
            if (c.getEmploye().getDateEmbauche() != null) {
                result = Constantes.calculNbYear(c.getEmploye().getDateEmbauche(), fin);
            }
        }
        return result;
    }

    private int countDayMonth(Date dateJ) {
        if (paramGrh != null) {
            if (paramGrh.getDateDebutTraitementSalaire() != null) {
                Calendar c = Calendar.getInstance();
                Calendar c1 = Calendar.getInstance();
                c1.setTime(dateJ);
                c.setTime(paramGrh.getDateDebutTraitementSalaire());
                c.set(Calendar.MONTH, c1.get(Calendar.MONTH) - 1);
                c.set(Calendar.YEAR, c1.get(Calendar.YEAR));
                c.set(Calendar.HOUR_OF_DAY, c1.get(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c1.get(Calendar.MINUTE));
                Calendar re = Calendar.getInstance();
                re.setTime(new Date((dateJ.getTime() - c.getTimeInMillis())));
                int result = (re.get(Calendar.DAY_OF_MONTH));
                return result;
            }
        }
        return 0;
    }

    private int countMonthAcceptInYear() {
        int nbreMois = 0;
        if (exo != null) {
            if (paramGrh.getBaseConge() != 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(exo.getDateDebut());
                Date debut;
                DataPresence dp;
                while (c.getTime().before(exo.getDateFin())) {
                    debut = c.getTime();
                    c.add(Calendar.MONTH, 1);
                    dp = findPresence(contrat.getEmploye().getId(), debut, c.getTime());
                    if (dp.getDureePresenceEffectif() >= paramGrh.getBaseConge()) {
                        nbreMois++;
                    }
//                    int r = (int) giveDayOfWorkReel(contrat.getEmploye().getId(), debut, c.getTime());                    
                }
            }
        }
        return nbreMois;
    }

    public YvsBaseExercice giveExerciceActif(Date date) {
        return (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByActif", new String[]{"actif", "societe", "date"}, new Object[]{true, currentScte, date});

    }

    private int countDaysAcceptInYear() {
        int nbreDay = 0;
        if (paramGrh != null && exo != null) {
            if (paramGrh.getBaseConge() != 0 && exo.getActif()) {
                Calendar c = Calendar.getInstance();
                c.setTime(exo.getDateDebut());
                Date debut_;
                DataPresence dp;
                while (c.getTime().before(exo.getDateFin())) {
                    debut_ = c.getTime();
                    c.add(Calendar.MONTH, 1);
                    dp = findPresence(contrat.getEmploye().getId(), debut_, c.getTime());
//                    int r = (int) giveDayOfWorkReel(contrat.getEmploye().getId(), debut_, c.getTime());
                    if (dp.getDureePresenceEffectif() >= paramGrh.getBaseConge()) {
                        nbreDay += dp.getDureePresenceEffectif();
                    }
                }
            }
        }
        return nbreDay;
    }

    private int giveNbEnfant(YvsGrhContratEmps ce) {
        {
            //nombre d'enfant mineur
            int r = 0;
            for (YvsGrhPersonneEnCharge p : ce.getEmploye().getPersonnes()) {
                if (Constantes.calculNbYear(p.getDateNaissance(), fin) < Constantes.ENV_MAJORITE) {
                    r = r + 1;
                }
            }
            return r;
        }

    }

    private double findLastEltSalaire(String codeElt, String numeroMois) {
        Double d = null;
        try {
            int n = Integer.parseInt(numeroMois);
            //calcule la date du mois correspondant
            Calendar c = Calendar.getInstance();
            c.setTime(fin);
            c.add(Calendar.MONTH, -n);
            c.roll(Calendar.DAY_OF_MONTH, false);
            //recherche l'élément de salaire du mois correspondant à cette date
            champ = new String[]{"contrat", "date", "regle"};
            val = new Object[]{contrat, c.getTime(), codeElt};
            YvsGrhDetailBulletin re = (YvsGrhDetailBulletin) dao.loadOneByNameQueries("YvsGrhDetailBulletin.findElementSalaire", champ, val);
            if (re != null) {
                d = Math.abs(re.getMontantPayer() - re.getRetenuSalariale());
            }
        } catch (NumberFormatException ex) {
            //
            return 0;
        }
        return (d == null) ? 0 : d;
    }

    //il faut s'assurer que les élément de type de variable soient évalué en dernier.
    private double findSpecificEltFromCurrentBulletin(String codeElt, String variable) {
        YvsGrhElementSalaire e = (YvsGrhElementSalaire) dao.loadOneByNameQueries("YvsGrhElementSalaire.findByCode", new String[]{"code", "societe"}, new Object[]{codeElt, currentScte});
        if (e != null) {
            YvsGrhDetailBulletin b = buildElementBulletin(e);
            b = calculDtailB(b);
            switch (variable) {
                case "BASE": //base
                    return b.getBase();
                case "Q"://quantité
                    return b.getQuantite();
                case "RS"://retenue salariale
                    return b.getRetenuSalariale();
                case "RP"://retenu patronale
                    return b.getMontantEmployeur();
                case "TS"://taux salariale
                    return b.getTauxSalarial();
                case "TP"://taux patronale
                    return b.getTauxPatronal();
                case "VAL"://montant gain
                    return b.getMontantPayer();
            }
        }
        return 0;
    }

    private String[] split(String code) {
        String[] re = new String[3];
        char[] cars = code.toCharArray();
        String word = "";
        int i = 0;
        int nbCar = 1;
        for (char c : cars) {
            if (c != '.') {
                word += c;
                if (nbCar == cars.length) {
                    re[i++] = word;
                }
            } else {
                re[i] = word;
                word = "";
                i++;
            }
            nbCar++;
        }
        if ((re[0] != null && re[1] != null && re[2] != null)) {
            return re;
        } else if (re[0] != null && re[1] != null) {
            return new String[]{re[0], re[1]};
        } else {
            return new String[1];
        }
    }

    private double givePrime(String code) {
        //récupère les primes du contrat qui ne sont pas annulé
        double result = 0;
        for (YvsGrhElementAdditionel e : contrat.getPrimes()) {
            if (e.getTypeElement().getCodeElement().equals(code)) {
                //si la prime fait l'objets de suspension, on ne la compte pas !
                if (!e.getRetenues().isEmpty()) {
                    //vérifions que la date de suspension est incluse dans la période de calcul (cas d'une suspension temporaire)
                    for (YvsGrhDetailPrelevementEmps p : e.getRetenues()) {
                        if (((p.getDatePrelevement().after(debutTraitement) && p.getDatePrelevement().before(finTraitement)) || p.getDatePrelevement().equals(debutTraitement) || p.getDatePrelevement().equals(finTraitement)) && p.getStatutReglement() != 'S') {
                            return 0;
                        }
                    }
                    result = e.getMontantElement();
                } else {
                    if (!e.getPermanent()) {
//                        if (e.getDateDebut().before(debut) && e.getDateFin().after(fin)) {
                        if ((finTraitement.before(e.getDateFin()) && finTraitement.after(e.getDateDebut())) || (finTraitement.equals(e.getDateFin()) || finTraitement.equals(e.getDateDebut()))) {
                            result = e.getMontantElement();
                        }
                    } else {
                        result = e.getMontantElement();
                    }
                }
            }
        }
        return result;
    }

    private double findDurreConge(String code) {
        switch (code) {
            case Constantes.GRH_DUREE_CONGE_PRINCIPAL:  //déterminer le nombre de jour de congé du à un employé 
                return getDureeCongeRestant(contrat.getEmploye().getId());
            case Constantes.GRH_DUREE_CONGE_SUPPLEMENTAIRE:   //déterminer le nombre de jour de congé supplémentaire du à un employé
                return findDureeCongeSupplementaire(contrat.getEmploye(), fin);
            case Constantes.GRH_DUREE_CONGE_MOIS_REFERENCE:
                return findPeriodeDeRefence(contrat.getEmploye());
            case Constantes.GRH_DUREE_CONGE_NB_JOUR_IN_PERIODE_REF:
                return findNbDayInPeriodeDeRefence(contrat.getEmploye());
            case Constantes.GRH_DUREE_CONGE_NB_JOUR_REF:
                return paramGrh.getNbreJourMoisRef();
            case Constantes.GRH_DUREE_CHOMAGE_TECHNIQUE:
            //évalue la durée du congé technique
//                return findCongeTechnique(true);
            case Constantes.GRH_DUREE_CONGE_AUTORISE:
                //évalue la durée du congé autorisé entre deux date                
                return findNbDayCongeAutorise(contrat.getEmploye());
            case Constantes.GRH_DUREE_CONGE_MALADIE:
                //évalue la durée du congé autorisé entre deux date                
                return findNbDayCongeMaladie(contrat.getEmploye());
            case Constantes.GRH_DUREE_CONGE_N_AUTORISE:
                //évalue la durée du congé autorisé entre deux date                
                return findNbDayCongeNonAutorise(contrat.getEmploye());
            case Constantes.GRH_DUREE_CONGE_IN_MONTH:
                //évalue la durée du congé autorisé entre deux date                
                return findNbDayCongeInMonth(contrat.getEmploye());
            default:
                //cas des congé technique
                String chaine = UtilExpression.extract(code, "conge.");
                if (chaine.startsWith("tauxCT")) {
                    //récupère le taux
                    return findCongeTechnique(false, UtilExpression.extract(chaine, "tauxCT."));
                } else if (chaine.startsWith("dureeCT")) {
                    //récupère la durée
                    return findCongeTechnique(true, UtilExpression.extract(chaine, "dureeCT."));
                }
        }
        return 0;
    }

    private double findCongeTechnique(boolean duree, String code) {
        //cherche les lignes de congé technique correspondant à un employé et contenu dans la période e calul de salaire
        double re = 0;
        YvsGrhParamsTauxChomageTech p = (YvsGrhParamsTauxChomageTech) dao.loadOneByNameQueries("YvsGrhParamsTauxChomageTech.findByCode", new String[]{"codeTaux", "societe"}, new Object[]{code, currentScte});
        if (p != null) {
            if (duree) {
                String requete = "select public.grh_conge_nbday_technique(?,?,?,?)";
                yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(debutTraitement, 1), new yvs.dao.Options(finTraitement, 2), new yvs.dao.Options(p.getNumMois(), 3), new yvs.dao.Options(contrat.getEmploye().getId(), 4)};
                re = (Integer) dao.callFonctionInt(requete, param_);
            } else {
                re = p.getTaux();
            }
        }
        return re;
    }

    private int findPeriodeDeRefence(YvsGrhEmployes e) {
        String requete = "select public.calcul_periode_reference(?)";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(e.getId(), 1)};
        Double d = (Double) dao.callFonction(requete, param_);
        if (d != null) {
            return d.intValue();
        } else {
            return 0;
        }
    }

    private double findTauxValidationPointage(String codeP) {
        champ = new String[]{"code"};
        val = new Object[]{codeP};
        YvsGrhTypeValidation t = (YvsGrhTypeValidation) dao.loadOneByNameQueries("YvsGrhTypeValidation.findByCode", champ, val);
        return (t != null) ? t.getTauxJournee() : 0;
    }

    private long calculeNbJourValideByCode(String codeP) {
        champ = new String[]{"code", "employe", "date1", "date2"};
        val = new Object[]{codeP, contrat.getEmploye(), debutTraitement, finTraitement};
        Long t = (Long) dao.loadObjectByNameQueries("YvsGrhPresence.findByCodeValidation", champ, val);
        return (t != null) ? t : 0;
    }

    private int findDureeCongeSupplementaire(YvsGrhEmployes e, Date jour_) {
        String requete = "select public.conge_jour_supplementaire(?,?)";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(e.getId(), 1), new yvs.dao.Options(jour_, 2)};
        Double d = (Double) dao.callFonction(requete, param_);
        if (d != null) {
            return d.intValue();
        } else {
            return 0;
        }
    }

    private int findNbDayInPeriodeDeRefence(YvsGrhEmployes e) {
        String requete = "select give_nbDay_in_periode_reference(?,?)";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(e.getId(), 1), new yvs.dao.Options(e.getAgence().getSociete().getId(), 2)};
        Double d = (Double) dao.callFonction(requete, param_);
        if (d != null) {
            return d.intValue();
        } else {
            return 0;
        }
    }

    private int findNbDayCongeAutorise(YvsGrhEmployes e) {
//        return (int) presence.getNbCongeMaladie();
        String requete = "select jour_conge_autorise(?,?,?,?)";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(e.getId(), 1), new yvs.dao.Options(debutTraitement, 2), new yvs.dao.Options(finTraitement, 3), new yvs.dao.Options(true, 4)};
        Double d = (Double) dao.callFonction(requete, param_);
        if (d != null) {
            return d.intValue();
        } else {
            return 0;
        }
    }

    private int findNbDayCongeMaladie(YvsGrhEmployes e) {
        return (int) presence.getNbCongeMaladie();
    }

    private int findNbDayCongeNonAutorise(YvsGrhEmployes e) {
        String requete = "select jour_conge_autorise(?,?,?,?)";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(e.getId(), 1), new yvs.dao.Options(debutTraitement, 2), new yvs.dao.Options(finTraitement, 3), new yvs.dao.Options(false, 4)};
        Double d = (Double) dao.callFonction(requete, param_);
        if (d != null) {
            return d.intValue();
        } else {
            return 0;
        }
    }

    private int findNbDayCongeInMonth(YvsGrhEmployes e) {
        String requete = "select grh_get_duree_conge(?,?,?)";
        yvs.dao.Options param_[] = new yvs.dao.Options[]{new yvs.dao.Options(debutTraitement, 1), new yvs.dao.Options(finTraitement, 2), new yvs.dao.Options(e.getId(), 3)};
        Double d = (Double) dao.callFonction(requete, param_);
        if (d != null) {
            return d.intValue();
        } else {
            return 0;
        }
    }
    /* date c'est la date de fin du mois en cours (celui pour lequel on calcule le salaire)
     */

    public double findLastSalaire(String numero, Date date) {
        Double d = null;
        try {
            int n = Integer.parseInt(numero);
            //calcule la date du mois correspondant
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, -n);
            c.roll(Calendar.DAY_OF_MONTH, false);
            //recherche le salaire du mois correspondant à cette date
            d = (Double) dao.loadObjectByNameQueries("YvsGrhDetailBulletin.findSalaire", new String[]{"contrat", "date"}, new Object[]{contrat, c.getTime()});
        } catch (NumberFormatException ex) {
            //
            return 0;
        }
        return (d == null) ? 0 : d;
    }

    public int findNbreDayInMonth(String numero) {
        int d = 0;
        try {
            int n = Integer.parseInt(numero);
            if (n > 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(exo.getDateDebut());
                c.add(Calendar.MONTH, n - 1);
                Date debut_ = c.getTime();
                c.add(Calendar.MONTH, 1);
                Date fin_ = c.getTime();
                //appelle la fonction qui renvoie le nombre de jour travaillé entre deux dates
                DataPresence dp = findPresence(contrat.getEmploye().getId(), debut_, fin_);
                return (int) dp.getDureePresenceEffectif();
            }
        } catch (NumberFormatException ex) {
            //
            return 0;
        }
        return 0;
    }

    private int giveBaseCalculConge(YvsParametreGrh param) {
        if (param != null) {
            return param.getBaseConge();
        } else {
            return 0;
        }
    }

    private double findElementSalaireInParamaBulletin(String code) {
        if (code != null && header != null) {
            Double re = (Double) dao.loadObjectByNameQueries("YvsGrhParametresBulletin.findVOneParam", new String[]{"contrat", "header", "code"}, new Object[]{contrat, header, code});
            return (re != null) ? re : 0;
        }
        return 0;
    }

}

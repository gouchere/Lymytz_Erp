/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.salaire.service;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import yvs.MyClassLoader;

/**
 *
 * @author Yves
 */
//@ManagedBean
public class Constantes {

    public static final MyClassLoader CLASSLOADER = new MyClassLoader();
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    
    public static final String SUPPL_SOCIETE_AGREEMENT = "AGREEMENT";
    public static final String SUPPL_SOCIETE_AGREEMENT_NAME = "Agréement";

    public static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final DateFormat dfN = new SimpleDateFormat("dd-MM-yyyy");
    public static final DateFormat dfN1 = new SimpleDateFormat("dd MMMM yyyy");
    public static final DateFormat df = new SimpleDateFormat("dd-MM-yy");
    public static final DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat dfh = new SimpleDateFormat("dd-MM-yy HH:mm");
    public static final DateFormat dfh1 = new SimpleDateFormat("ddMMyy");
    public static final DateFormat HMS = new SimpleDateFormat("HH:mm:ss");
    public static final DateFormat dfH = new SimpleDateFormat("HH:mm");
    public static final DateFormat SDFL = new SimpleDateFormat("EEE, dd MMM yyy 'à' HH:mm:ss");
    public static final DateFormat dfM = new SimpleDateFormat("MM-yyyy");
    public static final DateFormat dfD = new SimpleDateFormat("dd");
    public static final DecimalFormat nbF = new DecimalFormat("#,##0");
    public static final DecimalFormat nbF00 = new DecimalFormat("#,##0.00");

    public static Calendar dateToCalendar(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(date);
            return cal;
        }
        return Calendar.getInstance();
    }

    public static final String CAT_MARCHANDISE = "MARCHANDISE", CAT_SERVICE = "SERVICE";
    public static final String CAT_PSF = "PSF";
    public static final String CAT_MP = "MP";
    public static final String CAT_PF = "PF";

    /**
     * Libellé des frais supplémentaire
     *
     */
    public static DateFormat dfML = new SimpleDateFormat("MMMM yyyy");
    public static final String TRANSPORT = "TRANSPORT", ASSURANCE = "ASSURANCE", COMMISSION = "COMMISSION", MANUTENTION = "MANUTENTION";
    public static final String AUTRES = "AUTRES";

    public static final String NATURE_TAUX = "TAUX";
    public static final String NATURE_MTANT = "MONTANT";

    public static final char SERVICE_RESTAURANT = 'R';
    public static final char SERVICE_LOGEMENT = 'L';
    public static final char SERVICE_COMMERCE_GENERAL = 'C';

    public static final String BASE_QTE = "QTE";
    public static final String BASE_CA = "CA";

    public static final String DEPENSE = "DEPENSE";
    public static final String RECETTE = "RECETTE";
    public static final String COMPTA_DEPENSE = "D";
    public static final String COMPTA_RECETTE = "R";

    public static final char STATUT_EDITABLE = 'E';
    public static final char STATUT_ATTENTE = 'W';
    public static final char STATUT_VALIDE = 'V';
    public static final char STATUT_ENCOURS = 'R';
    public static final char STATUT_ANNULE = 'A';
    public static final char STATUT_SUSPENDU = 'S';
    public static final char STATUT_PROD_LANCE = 'L';
    public static final char STATUT_DOC_JUSTIFIER = 'J';
    //Etat et Statut
    public static final String ETAT_ATTENTE = "W";
    public static final String ETAT_EDITABLE = "E";
    public static final String ETAT_SOUMIS = "U";
    public static final String ETAT_REGLE = "P";
    public static final String ETAT_VALIDE = "V";
    public static final String ETAT_ANNULE = "A";
    public static final String ETAT_RELANCE = "C";
    public static final String ETAT_LIVRE = "L";
    public static final String ETAT_PROD_LANCE = "L";
    public static final String ETAT_ENCOURS = "R";
    public static final String ETAT_RENVOYE = "B";
    public static final String ETAT_JUSTIFIE = "J";
    public static final String ETAT_INJUSTIFIE = "I";
    public static final String ETAT_SUSPENDU = "S";
    public static final String ETAT_TERMINE = "T";
    public static final String ETAT_CLOTURE = "F";
    public static final String ETAT_INCOMPLET = "M";

    public static final String PAYS = "Pays";
    public static final String VILLES = "Villes";
    public static final String SECTEURS = "Secteurs";

    //Nature nature compte
    public static final String NAT_AUTRE = "AUTRE";
    public static final String NAT_ACOMPTE_CLIENT = "ACOMPTE_CLIENT";
    public static final String NAT_ACOMPTE_FOURNISSEUR = "ACOMPTE_FOURNISSEUR";
    public static final String NAT_CREDIT_CLIENT = "CREDIT_CLIENT";
    public static final String NAT_CREDIT_FOURNISSEUR = "CREDIT_FOURNISSEUR";
    public static final String NAT_ARRONDI_GAIN = "ARRONDI_GAIN";
    public static final String NAT_ARRONDI_PERTE = "ARRONDI_PERTE";
    public static final String NAT_COMPENSATION = "COMPENSATION";
    public static final String NAT_ABONNEMENT = "ABONNEMENT";

    /*
     MODULES
     */
    public static final String MOD_GRH = "GRH";
    public static final String MOD_GRH_NAME = "RESSOURCE HUMAINE";
    public static final String MOD_MUT = "MUT";
    public static final String MOD_MUT_NAME = "MUTUELLE";
    public static final String MOD_COM = "COM";
    public static final String MOD_COM_NAME = "GESTION COMMERCIAL";
    public static final String MOD_MSG = "MSG";
    public static final String MOD_MSG_NAME = "MESSAGERIE";
    public static final String MOD_REL = "REL";
    public static final String MOD_REL_NAME = "RELATION CLIENT";
    public static final String MOD_COFI = "COFI";
    public static final String MOD_COFI_NAME = "COMPTABILITE & FINANCE";
    public static final String MOD_PROD = "PROD";
    public static final String MOD_PROD_NAME = "PRODUCTION";

    /*
     * mode de réapprovisionnement
     */
    public static final String REAPPRO_SEUIL = "SEUIL";
    public static final String REAPPRO_PERIODE = "PERIODIQUE";
    public static final String REAPPRO_RUPTURE = "RUPTURE";

    public static final String UNITE_TIERCE_ = "T";
    public static final String UNITE_TIERCE = "Tierce(s)";
    public static final String UNITE_TIERCE_m = "Tce(s)";
    public static final String UNITE_SECONDE_ = "C";
    public static final String UNITE_SECONDE = "Seconde(s)";
    public static final String UNITE_SECONDE_m = "Sec(s)";
    public static final String UNITE_MINUTE_ = "U";
    public static final String UNITE_MINUTE = "Minute(s)";
    public static final String UNITE_MINUTE_m = "Min(s)";
    public static final String UNITE_HEURE_ = "H";
    public static final String UNITE_HEURE = "Heure(s)";
    public static final String UNITE_HEURE_m = "Hr(s)";
    public static final String UNITE_JOUR_ = "J";
    public static final String UNITE_JOUR = "Jour(s)";
    public static final String UNITE_JOUR_m = "Jr(s)";
    public static final String UNITE_SEMAINE_ = "S";
    public static final String UNITE_SEMAINE = "Semaine(s)";
    public static final String UNITE_SEMAINE_m = "Sem(s)";
    public static final String UNITE_MOIS_ = "M";
    public static final String UNITE_MOIS = "Mois";
    public static final String UNITE_ANNEE_ = "A";
    public static final String UNITE_ANNEE = "Année(s)";
    public static final String UNITE_ANNEE_m = "An(s)";

    public static final String APPRO_ACHTON = "ACHTON";   //achat only
    public static final String APPRO_ENON = "ENHON";   //Entrée only
    public static final String APPRO_PRODON = "PRODON";   //production only
    public static final String APPRO_ACHT_PROD = "ACHT_PROD";   //achat et production
    public static final String APPRO_ACHT_EN = "ACHT_EN";   //achat et entré
    public static final String APPRO_PROD_EN = "PROD_EN";    //production et entrée
    public static final String APPRO_ACHT_PROD_EN = "ACHT_PROD_EN";   //achat production et entrée
    /**
     * GRH
     */
    public static final int ENV_MAJORITE = 21;
    public static final String GRH_EN_COMMISSION = "EN COMMISSION";
    public static final String GRH_EN_PAUSE = "EN PAUSE";
    public static final String GRH_EN_SERVICE = "EN SERVICE";
    public static final String GRH_ABSENT = "ABSENT";

    public static final String GRH_PERMISSION_SUR_CONGE = "CONGE ANNUEL";
    public static final String GRH_PERMISSION_SUR_SALAIRE = "SALAIRE";
    public static final String GRH_PERMISSION_AUTORISE = "AUTORISE";

    public static final String S_SALAIRE_CONVENTIONNELLE = "employe.s_conv";
    public static final String S_SALAIRE_CONTRACTUELLE = "employe.s_contrat";
    public static final String S_SALAIRE_SUR_OBJETIF = "employe.s_objectif";
    public static final String S_SALAIRE_HORAIRE = "employe.s_horaire";
    public static final String S_ENFANT = "employe.Nenfant";
    public static final String S_HEURE_DE_TRAVAIL = "employe.Htravail";
    public static final String S_DURRE_REQUISE_DE_TRAVAIL = "employe.dureeMois"; //durée de travail requise pour un mois
    public static final String S_JOUR_DE_CONGE = "employe.conge";
    public static final String S_HEURE_SUPPLEMENTAIRE = "employe.heureSup";
    public static final String S_JOUR_DE_TRAVAIL = "employe.Njour";
    public static final String S_HEURE_JOUR_FERIE = "employe.supFerie";
    public static final String S_HEURE_DIMANCHE = "employe.supDim";
    public static final String S_ANCIENETE = "employe.ancienete";
    public static final String S_DUREE_EMBAUCHE = "employe.dembauche";
    //nombre d'heure de travail requis
    public static final String S_NB_HEURE_REQUISE = "employe.NHrequis";
    //Nombre de jour de travail requis
    public static final String S_NB_JOUR_REQUISE = "employe.NJrequis";
    //nombre d'heure de travail effectif
    public static final String S_NB_HEURE_EFFECTIF = "employe.NHreffec";
    //nombre de jour de travail effectif
    public static final String S_NB_JOUR_EFFECTIF = "employe.NJreffec";
    //Nombre de jour de travail requis
    public static final String S_NB_JOUR_REQUIS = "employe.NJrequis";
    //total des notes de frais payé
    public static final String S_NOTE_DE_FRAIS = "employe.NotDeFrai";
    //Nombre de jour dans la période de référence
    public static final String GRH_DUREE_CONGE_NB_JOUR_REF = "conge.nbJourRef"; //fait  référence au nombre de jour de travail acceptable dans un mois pour bénéficier du congé mensuel
    public static final String GRH_DUREE_CHOMAGE_TECHNIQUE = "conge.dureeCT";
    public static final String GRH_DUREE_CONGE_AUTORISE = "conge.accept";//Durée des congés spéciales ou autorisé!
    public static final String GRH_DUREE_CONGE_MALADIE = "conge.maladie";//Durée des congés spéciales ou autorisé!
    public static final String GRH_DUREE_CONGE_N_AUTORISE = "conge.NotAccept";//Durée des congés Annuel ou absence!
    public static final String GRH_DUREE_CONGE_IN_MONTH = "conge.duree";//Durée des congés dans un mois
//    public static final String GRH_DUREE_POINTAGE_REPOS = "presence.repos";//Durée des congés dans un mois
    public static final String POINTAGE_NB_FICHE = "presence.countFiche";   //nombre de fiche pointé et validé
    public static final String GRH_DUREE_POINTAGE_REPOS = "presence.reposPointe";//Nombre de pointage les jours de repos
    public static final String GRH_DUREE_ABSENCE = "presence.dureeAbs";//Nombre de pointage les jours de repos
    public static final String GRH_DUREE_RETARD = "presence.dureeRetard";//Temps de retard cumulé en heure
    public static final String GRH_DUREE_MISSION = "mission.duree";//Nombre de mission
    public static final String GRH_TAUX_CHOMAGE_TECHNIQUE = "conge.tauxCT";
    public static final String UNITE_DUREE_PREAVIS_MOIS = "Mois";
    public static final String UNITE_DUREE_PREAVIS_JOUR = "Jour";
    //Nombre de jour travaillé entre le début du mois commercial et la date du jour 
    //(Necessite que la date de début du mois commercial soit indiqué dans les paramètres généraux)
    public static final String S_NB_JOUR_PASSE = "employe.NJpasse";/*?*/

    //Nombre de jours de congé technique
    public static final String S_NB_JOUR_CONGE_TECHNIQUE = "employe.NJCT";
    //durée préavis
    public static final String S_DUREE_PREAVIS = "employe.Preavis";
    //Nombre de mois accepté dans l'exercice
    public static final String S_NB_MOIS_ACCEPTE_IN_EXO = "employe.NBmoisAccept";
    //Nombre de jour accepté dans l'exercice
    public static final String S_NB_JOUR_ACCEPTE_IN_EXO = "employe.NBjourAccept";
    //base du calcul de la durée des congés
    public static final String S_BASE_DUREE_CONGE = "employe.baseDureConge";

    //durée du congé principal
    public static final String GRH_DUREE_CONGE_PRINCIPAL = "conge.dureePrincipal";
    //durée du congé supplémentaire
    public static final String GRH_DUREE_CONGE_SUPPLEMENTAIRE = "conge.dureeSup";
    //durée de la période de référence
    public static final String GRH_DUREE_CONGE_MOIS_REFERENCE = "conge.moisRef";
    //Nombre de jour dans la période de référence
    public static final String GRH_DUREE_CONGE_NB_JOUR_IN_PERIODE_REF = "conge.nbJourInPeriodeRef";

    public static double arrondi(double nbre, int precision) {
        return Math.round(nbre * Math.pow(10, precision)) / Math.pow(10, precision);
    }
    //gestion Piece Tresorerie
    public static final String SCR_ARTICLES = "BASE_ARTICLE";//
    public static final String SCR_CONDITIONNEMENT = "BASE_CONDITIONNEMENT";//
    public static final String SCR_CLIENTS = "BASE_CLIENT";//
    public static final String SCR_DICTIONNAIRE = "DOC_ZONE";//
    public static final String SCR_CATEGORIE = "BASE_CATEGORIE";//
    public static final String SCR_BON_PROVISOIRE = "BON_PROVISOIRE";// 
    public static final String SCR_BON_PROVISOIRE_NAME = "BON PROVISOIRE";
    public static final String SCR_ACHAT = "DOC_ACHAT";//
    public static final String SCR_ACHAT_NAME = "FACTURE ACHAT";
    public static final String SCR_VENTE = "DOC_VENTE";//
    public static final String SCR_VENTE_NAME = "FACTURE VENTE";
    public static final String SCR_HEAD_VENTE = "JOURNAL_VENTE";//
    public static final String SCR_HEAD_VENTE_NAME = "JOURNAL VENTE";
    public static final String SCR_SALAIRE = "ORDRE_SALAIRE";//
    public static final String SCR_SALAIRE_NAME = "ORDRE SALAIRE";
    public static final String SCR_BULLETIN = "BULLETIN";//
    public static final String SCR_BULLETIN_NAME = "BULLETIN";
    public static final String SCR_RETENUE = "RETENUE";//
    public static final String SCR_RETENU_NAME = "RETENUE";
    public static final String SCR_DIVERS = "DOC_DIVERS";
    public static final String SCR_DIVERS_NAME = "OPERATION DIVERS";
    public static final String SCR_MISSIONS = "MISSION";
    public static final String SCR_VIREMENT = "DOC_VIREMENT";
    public static final String SCR_CREDIT_VENTE = "CREDIT_VENTE";
    public static final String SCR_CREDIT_VENTE_NAME = "CREDITS VENTE";
    public static final String SCR_CREDIT_ACHAT = "CREDIT_ACHAT";
    public static final String SCR_CREDIT_ACHAT_NAME = "CREDITS ACHAT";
    public static final String SCR_ACOMPTE_ACHAT = "ACOMPTE_ACHAT";
    public static final String SCR_ACOMPTE_ACHAT_NAME = "ACOMPTES ACHAT";
    public static final String SCR_ACOMPTE_VENTE = "ACOMPTE_VENTE";
    public static final String SCR_ACOMPTE_VENTE_NAME = "ACOMPTES VENTE";
    public static final String SCR_VIREMENT_NAME = "VIREMENT";
    public static final String SCR_NOTE_FRAIS = "NOTE_DE_FRAIS";
    public static final String SCR_NOTE_FRAIS_NAME = "NOTE DE FRAIS";
    public static final String SCR_CAISSE_ACHAT = "CAISSE_ACHAT";
    public static final String SCR_CAISSE_ACHAT_NAME = "REGLEMENT FACTURE ACHAT";
    public static final String SCR_CAISSE_DIVERS = "CAISSE_DIVERS";
    public static final String SCR_CAISSE_DIVERS_NAME = "REGLEMENT OPERATION DIVERS";
    public static final String SCR_CAISSE_VENTE = "CAISSE_VENTE";
    public static final String SCR_CAISSE_VENTE_NAME = "REGLEMENT FACTURE VENTE";
    public static final String SCR_CAISSE_AVOIR_VENTE = "CAISSE_AVOIR_VENTE";
    public static final String SCR_CAISSE_CREDIT_ACHAT = "CAISSE_CREDIT_ACHAT";
    public static final String SCR_CAISSE_CREDIT_ACHAT_NAME = "REGLEMENT CREDITS ACHAT";
    public static final String SCR_CAISSE_CREDIT_VENTE = "CAISSE_CREDIT_VENTE";
    public static final String SCR_CAISSE_CREDIT_VENTE_NAME = "REGLEMENT CREDITS CLIENTS";
    public static final String SCR_PHASE_CAISSE_VENTE = "PHASE_CAISSE_VENTE";
    public static final String SCR_PHASE_CAISSE_VENTE_NAME = "PHASE CHEQUE FACTURE VENTE";
    public static final String SCR_PHASE_CAISSE_ACHAT = "PHASE_CAISSE_ACHAT";
    public static final String SCR_PHASE_CAISSE_ACHAT_NAME = "PHASE CHEQUE FACTURE ACHAT";
    public static final String SCR_PHASE_CAISSE_DIVERS = "PHASE_CAISSE_DIVERS";
    public static final String SCR_PHASE_CAISSE_DIVERS_NAME = "PHASE CHEQUE OD";
    public static final String SCR_PHASE_ACOMPTE_ACHAT = "PHASE_ACOMPTE_ACHAT";
    public static final String SCR_PHASE_ACOMPTE_ACHAT_NAME = "PHASE CHEQUE ACOMPTE FOURNISSEUR";
    public static final String SCR_PHASE_CREDIT_ACHAT = "PHASE_CREDIT_ACHAT";
    public static final String SCR_PHASE_CREDIT_ACHAT_NAME = "PHASE CHEQUE REGLEMENT CREDIT FOURNISSEUR";
    public static final String SCR_PHASE_ACOMPTE_VENTE = "PHASE_ACOMPTE_VENTE";
    public static final String SCR_PHASE_ACOMPTE_VENTE_NAME = "PHASE CHEQUE ACOMPTE CLIENT";
    public static final String SCR_PHASE_CREDIT_VENTE = "PHASE_CREDIT_VENTE";
    public static final String SCR_PHASE_CREDIT_VENTE_NAME = "PHASE CHEQUE REGLEMENT CREDIT CLIENT";
    public static final String SCR_PHASE_VIREMENT = "PHASE_VIREMENT";
    public static final String SCR_PHASE_VIREMENT_NAME = "PHASE CHEQUE VIREMENT";
    public static final String SCR_AUTRES = "AUTRES";
    public static final String SCR_FRAIS_MISSIONS = "FRAIS_MISSION";
    public static final String SCR_FRAIS_MISSIONS_NAME = "FRAIS MISSION";
    public static final String SCR_TRESORERIE = "TRESORERIE";

    public static final String SCR_ABONNEMENT_DIVERS = "ABONNEMENT_DIVERS";
    public static final String SCR_ABONNEMENT_DIVERS_NAME = "ABONNEMENT OPERATION DIVERS";
    public static final String SCR_AVOIR_VENTE = "AVOIR_VENTE";//
    public static final String SCR_AVOIR_VENTE_NAME = "AVOIR VENTE";
    public static final String SCR_AVOIR_ACHAT = "AVOIR_ACHAT";//
    public static final String SCR_AVOIR_ACHAT_NAME = "AVOIR ACHAT";

    public static final String JOUR_REQUIS = "Tr";
    public static final String JOUR_EFFECTIF = "Je";
    public static final String JOUR_NORMAL = "Jn";
    public static final String JOUR_SUPPLEMENTAIRE = "Js";
    public static final String JOUR_SUPPLEMENTAIRE_FERIE = "Jsf";
    public static final String JOUR_COMPENSATOIRE = "Jc";
    public static final String ABSENCE = "Ab";
    public static final String REPOS_EFFECTIF = "Re";
    public static final String REPOS_REQUIS = "Rr";
    public static final String CONGE_MALADIE = "Cm";
    public static final String CONGE_TECHNIQUE = "Ct";
    public static final String CONGE_ANNUEL = "Ca";
    public static final String PERMISSION_COURT_DUREE = "Pc";
    public static final String PERMISSION_LONG_DUREE = "Pl";
    public static final String FERIE = "Fe";
    public static final String MISSION = "Mi";
    public static final String FORMATION = "Fo";

    public static final String AUTRE = "AUTRE";
    public static final String ACHAT = "ACHAT";
    public static final String SALAIRE = "SALAIRE";
    public static final String TRESORERIE = "TRESORERIE";
    public static final String VIREMENT = "VIREMENT";
    public static final String GENERAL = "GENERAL";
    public static final String VENTE = "VENTE";
    public static final String LOCATION = "LOCATION";
    public static final String STOCK = "STOCK";
    public static final String VSTOCK = "V-STOCK";
    public static final String DEFAUT = "DEFAUT";

    public static final char STATUT_DOC_ATTENTE = 'W', STATUT_DOC_ENCOUR = 'R', STATUT_DOC_VALIDE = 'V', STATUT_DOC_SUSPENDU = 'S', STATUT_DOC_ANNULE = 'A', STATUT_DOC_SOUMIS = 'U',
            STATUT_DOC_TERMINE = 'T', STATUT_DOC_CLOTURE = 'C', STATUT_DOC_PAYER = 'P', STATUT_DOC_LIVRER = 'L', STATUT_DOC_EDITABLE = 'E', STATUT_DOC_RENVOYE = 'B';

    public static final String MOUV_ENTREE = "E";
    public static final String MOUV_SORTIE = "S";
    public static final String MOUV_DEBIT = "D";
    public static final String MOUV_CREDIT = "C";

    public static final String MOUV_CAISS_ENTREE = "R";
    public static final String MOUV_CAISS_SORTIE = "D";

    public static final String MUT_TYPE_COMPTE_EPARGNE = "EPARGNE";
    public static final String MUT_TYPE_COMPTE_COURANT = "COURANT";
    public static final String MUT_TYPE_COMPTE_A_TERME = "A_TERME";
    public static final String MUT_TYPE_COMPTE_ASSURANCE = "ASSURANCE";

    public static final String MUT_SENS_OP_DEPOT = "D";
    public static final String MUT_SENS_OP_RETRAIT = "R";

    public static final String PROD_OP_TYPE_CHARGE_MO = "MO";
    public static final String PROD_OP_TYPE_CHARGE_MACHINE = "Machine";
    public static final String SOCIETE = "SOCIETE";
    public static final String AGENCE = "AGENCE";
    public static final String DEPOT = "DEPOT";
    public static final String JOURNAL = "JOURNAL";
    public static final String POINTVENTE = "POINTVENTE";
    public static final String CAISSE = "CAISSE";
    public static final String TYPECREDIT = "TYPECREDIT";

    //Type de documents achat
    public static final String TYPE_FiA = "FiA"; //Fiche Approvisionnement
    public static final String TYPE_FiA_NAME = "Fiche Approvisionnement"; //
    public static final String TYPE_FA = "FA"; //Facture Achat
    public static final String TYPE_FA_NAME = "Facture Achat"; //
    public static final String TYPE_FRA = "FRA"; //Facture Retour Achat
    public static final String TYPE_FRA_NAME = "Facture Retour Achat"; //
    public static final String TYPE_FAA = "FAA"; //Facture Avoir Achat
    public static final String TYPE_FAA_NAME = "Facture Avoir Achat"; //
    public static final String TYPE_BCA = "BCA"; //Bon Commande Achat
    public static final String TYPE_BCA_NAME = "Bon Commande Achat"; //
    public static final String TYPE_BLA = "BLA"; //Bon Livraison Achat
    public static final String TYPE_BLA_NAME = "Bon Livraison Achat"; //
    public static final String TYPE_BRA = "BRA"; //Bon Retour Achat
    public static final String TYPE_BRA_NAME = "Bon Retour Achat"; //
    public static final String TYPE_BAA = "BAA"; //Bon Avoir Achat
    public static final String TYPE_BAA_NAME = "Bon Avoir Achat"; //

    //Type de documents stock
    public static final String TYPE_FT = "FT"; //Fiche Transfert
    public static final String TYPE_FT_NAME = "Fiche Transfert"; //
    public static final String TYPE_OT = "OT"; //Ordre Transfert
    public static final String TYPE_OT_NAME = "Ordre Transfert"; //
    public static final String TYPE_SS = "SS"; //Sortie Stock
    public static final String TYPE_SS_NAME = "Sortie Stock"; //
    public static final String TYPE_ES = "ES"; //Entree Stock
    public static final String TYPE_ES_NAME = "Entree Stock"; //
    public static final String TYPE_IN = "IN"; //Inventaire
    public static final String TYPE_IN_NAME = "Inventaire"; //
    public static final String TYPE_RS = "RS"; //Reservation Stock
    public static final String TYPE_RS_NAME = "Reservation Stock"; //
    public static final String TYPE_RE = "TR"; //Reconditionnement
    public static final String TYPE_RE_NAME = "Reconditionnement"; //
    public static final String TYPE_RA = "RA"; //Ration
    public static final String TYPE_RA_NAME = "Ration";

    //Type de documents vente
    public static final String TYPE_FV = "FV"; //Facture Vente
    public static final String TYPE_FV_NAME = "Facture Vente"; //
    public static final String TYPE_FRV = "FRV"; //Facture Retour Vente
    public static final String TYPE_FRV_NAME = "Facture Retour Vente"; //
    public static final String TYPE_FAV = "FAV"; //Facture Avoir Vente
    public static final String TYPE_FAV_NAME = "Facture Avoir Vente"; //
    public static final String TYPE_BCV = "BCV"; //Bon Commande Vente
    public static final String TYPE_BCV_NAME = "Bon Commande Vente"; //
    public static final String TYPE_BLV = "BLV"; //Bon Livraison Vente
    public static final String TYPE_BLV_NAME = "Bon Livraison Vente"; //
    public static final String TYPE_BRV = "BRV"; //Bon Retour Vente
    public static final String TYPE_BRV_NAME = "Bon Retour Vente"; //
    public static final String TYPE_BAV = "BAV"; //Bon Avoir Vente
    public static final String TYPE_BAV_NAME = "Bon Avoir Vente"; //
    public static final String TYPE_PFV = "PFV"; //Bon Avoir Vente
    public static final String TYPE_PFV_NAME = "Proforma Vente"; //

    //Type de documents autres
    public static final String TYPE_EMPLOYE = "Employe"; //Employe
    public static final String TYPE_OD = "OD"; //Opération divers
    public static final String TYPE_OD_NAME = "Operation Divers"; //
    public static final String TYPE_OD_RECETTE_NAME = "Recette OD"; //
    public static final String TYPE_OD_DEPENSE_NAME = "Depense OD"; //
    public static final String TYPE_BP = "BP"; //Bon Provisoire
    public static final String TYPE_BP_NAME = "Bon Provisoire"; //
    public static final String TYPE_PIECE_COMPTABLE_NAME = "PIECE COMPTABLE"; //
    public static final String TYPE_PT = "PT"; //Piece Tresorerie
    public static final String TYPE_PT_NAME = "Piece Tresorerie"; //
    public static final String TYPE_PC_NAME = "Piece Caisse"; //
    public static final String TYPE_PC_ACHAT_NAME = "Piece Caisse Achat"; //
    public static final String TYPE_PC_VENTE_NAME = "Piece Caisse Vente"; //
    public static final String TYPE_PC_DIVERS_NAME = "Piece Caisse Divers"; //
    public static final String TYPE_PC_MISSION_NAME = "Piece Caisse Mission"; //
    public static final String TYPE_PC_SALAIRE_NAME = "Piece Caisse Salaire"; //
    public static final String TYPE_PT_CREDIT_VENTE = "Piece Credit Vente";
    public static final String TYPE_PT_CREDIT_ACHAT = "Piece Credit Achat";
    public static final String TYPE_PT_AVANCE_VENTE = "Piece Avance Vente";
    public static final String TYPE_PT_AVANCE_ACHAT = "Piece Avance Achat";
    public static final String TYPE_DOC_CAISSE_CHARGE_TIERS = "CTD";
    public static final String TYPE_DOC_CAISSE_CHARGE = "CD";
    public static final String TYPE_DOC_CAISSE_RECETTE = "RD";
    public static final String TYPE_DOC_CAISSE_RECETTE_TIERS = "RTD";
    public static final String TYPE_DOC_MISSION_NAME = "MISSIONS";
    public static final String TYPE_CONTRAT_CLIENT_NAME = "Contrat Client";

    public static final String TYPE_COM = "CM"; //Commission
    public static final String TYPE_COM_NAME = "Commission"; //
    public static final String PROD_TYPE_PROD = "OF";
    public static final String PROD_TYPE_PROD_NAME = "Ordre Fabrication";
    public static final String PROD_TYPE_PAGE_SUIVI = "SUIVI";
    public static final String PROD_TYPE_PAGE_CLASSIQUE = "C";

    public static final String PROD_OP_TYPE_TEMPS_FIXE = "Fixe";
    public static final String PROD_OP_TYPE_TEMPS_PROPORTIONNEL = "Proportionnel";
    public static final String PROD_OP_TYPE_TEMPS_CADENCE = "Cadence";

    public static final String PROD_OP_TYPE_COMPOSANT_NORMAL = "N";
    public static final String PROD_OP_TYPE_COMPOSANT_SOUS_PRODUIT = "SP";
    public static final String PROD_TYPE_NOMENCLATURE_TRANSFORMATION = "T";
    public static final String PROD_TYPE_NOMENCLATURE_PRODUCTION = "P";
    public static final String PROD_TYPE_NOMENCLATURE_CONDTIONNEMENT = "C";

    public static final String PROD_OP_TYPE_COUT_TOTAL = "T";
    public static final String PROD_OP_TYPE_COUT_PROPORTIONNEL = "P";

    public static final String ENTREE = "ENTREE";
    public static final String SORTIE = "SORTIE";
    public static final String TRANSFERT = "TRANSFERT";
    public static final String DECLARATION = "DECLARATION";
    public static final String PRODUCTION = "PRODUCTION";
    public static final String TECHNIQUE = "TECHNIQUE";
    public static final String RETOUR = "RETOUR";
    public static final String TRANSIT = "TRANSIT";
    public static final String RESERVATION = "RESERVATION";

    /*COMPTA*/
    public static final String MODE_PAIEMENT_ESPECE = "ESPECE";
    public static final String MODE_PAIEMENT_SALAIRE = "SALAIRE";
    public static final String MODE_PAIEMENT_BANQUE = "BANQUE";
    public static final String MODE_PAIEMENT_COMPENSATION = "COMPENSATION";
    public static final String CMP1 = "CMPI";
    public static final String CMP2 = "CMPII";
    public static final String CMPU1 = "Cout Moyen Pondere 1";
    public static final String CMPU2 = "Cout Moyen Pondere 2";

    public static final String FIFO_NAME = "First Int First Out";
    public static final String LIFO_NAME = "Last Int First Out";
    public static final String CMP1_NAME = "Cout Moyen Pondere I";
    public static final String CMP2_NAME = "Cout Moyen Pondere II";

    public static final String MUT_TRANSACTIONS_MUT = "Transactions Mutuelle";
    public static final String MUT_ACTIVITE_CREDIT = "Credit";

    public static final String CONGE_PRINCIPAL = "CPD";
    public static final String CONGE_SUPPLEMENTAIRE = "CSD";
    public static final String CONGE_ANNUEL_PRIS = "CNP";
    public static final String PERMISSION_DU = "PLD";
    public static final String PERMISSION_LONG_SPECIAL = "PLS";
    public static final String PERMISSION_LONG_AUTORISE = "PLA";
    public static final String PERMISSION_COURT_ANNUEL = "PCN";
    public static final String PERMISSION_COURT_AUTORISE = "PCA";
    public static final String PERMISSION_COURT_SPECIAL = "PCS";
    public static final String PERMISSION_COURT_SALAIRE = "PCSL";

    public static final String BASE_TIERS_FOURNISSEUR = "FOURNISSEUR";
    public static final String BASE_TIERS_CLIENT = "CLIENT";
    public static final String BASE_TIERS_EMPLOYE = "EMPLOYE";
    public static final String BASE_TIERS_COMMERCIAL = "COMMERCIAL";
    public static final String BASE_TIERS_TIERS = "TIERS";

    /**
     * ********************Model de documents**********************
     */
    public static final String DOCUMENT_MISSION = "MISSIONS";
    public static final String DOCUMENT_FORMATION = "FORMATIONS";
    public static final String DOCUMENT_CONGES = "CONGES";
    public static final String DOCUMENT_PERMISSION_COURTE_DUREE = "PERMISSION_CD";
    public static final String DOCUMENT_PC_MISSION = "PC_MISSIONS";
    public static final String DOCUMENT_DOC_DIVERS_CAISSE = "OPERATION_DIVERS";
    public static final String DOCUMENT_BON_DIVERS_CAISSE = "BON_OPERATION_DIVERS";
    public static final String DOCUMENT_FACTURE_ACHAT = "FACTURE_ACHAT";
    public static final String DOCUMENT_RETOUR_ACHAT = "RETOUR_ACHAT";
    public static final String DOCUMENT_AVOIR_ACHAT = "AVOIR_ACHAT";
    public static final String DOCUMENT_FACTURE_VENTE = "FACTURE_VENTE";
    public static final String DOCUMENT_RETOUR_VENTE = "RETOUR_VENTE";
    public static final String DOCUMENT_AVOIR_VENTE = "AVOIR_VENTE";
    public static final String DOCUMENT_TRANSFERT = "TRANSFERT_STOCK";
    public static final String DOCUMENT_SORTIE = "SORTIE_STOCK";
    public static final String DOCUMENT_APPROVISIONNEMENT = "APPROVISIONNEMENT";
    public static final String DOCUMENT_PIECE_CAISSE = "PIECE_CAISSE";
    public static final String DOCUMENT_PHASE_CAISSE_SALAIRE = "PHASE_CAISSE_SALAIRE";
    public static final String DOCUMENT_PHASE_CAISSE_DIVERS = "PHASE_CAISSE_DIVERS";
    public static final String DOCUMENT_PHASE_CAISSE_VENTE = "PHASE_CAISSE_VENTE";
    public static final String DOCUMENT_PHASE_CAISSE_ACHAT = "PHASE_CAISSE_ACHAT";
    public static final String DOCUMENT_PHASE_ACOMPTE_VENTE = "PHASE_ACOMPTE_VENTE";
    public static final String DOCUMENT_PHASE_ACOMPTE_ACHAT = "PHASE_ACOMPTE_ACHAT";
    public static final String DOCUMENT_PHASE_CREDIT_VENTE = "PHASE_CREDIT_VENTE";
    public static final String DOCUMENT_PHASE_CREDIT_ACHAT = "PHASE_CREDIT_ACHAT";

    public static double arrondiA0Chiffre(double d) {
        return (double) Math.round(d);
    }

    public static double arrondiA2Chiffre(double d) {
        double result = 0;
        result = (double) Math.round(d * 100) / 100;
        return result;
    }

    public static double arrondiA3Chiffre(double d) {
        double result = 0;
        result = (double) Math.round(d * 1000) / 1000;
        return result;
    }

    public static double calculNbYear(Date d1, Date d2) {
        if (d1 != null && d2 != null) {
            if (d2.after(d1)) {
                double A1, A2, M1, M2;
                Calendar c1 = Calendar.getInstance();
                c1.setTime(d1);
                A1 = c1.get(Calendar.YEAR);
                M1 = c1.get(Calendar.MONTH);
                c1.setTime(d2);
                A2 = c1.get(Calendar.YEAR);
                M2 = c1.get(Calendar.MONTH);
                return ((((A2 * 12) + M2) - ((A1 * 12) + M1)) / 12);
            }
        }
        return 0;
    }

    public static double calculNbDay(Date d1, Date d2) {
        final long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
        if (d1 != null && d2 != null) {
            if (d2.after(d1)) {
                Calendar aCal = Calendar.getInstance();
                aCal.setTime(d1);
                long aFromOffset = aCal.get(Calendar.DST_OFFSET);
                aCal.setTime(d2);
                long aToOffset = aCal.get(Calendar.DST_OFFSET);
                long aDayDiffInMili = (d2.getTime() + aToOffset) - (d1.getTime() + aFromOffset);
                return ((double) aDayDiffInMili / MILLISECONDS_PER_DAY);
            }
        }
        return 0;
    }

    public static String calculNbyear(Date d1, Date d2) {
        if (d2 != null && d1 != null) {
            if (d2.after(d1)) {
                int A1 = 0, A2 = 0;
                int M1 = 0, M2 = 0;
                Calendar c1 = Calendar.getInstance();
                c1.setTime(d1);
                A1 = c1.get(Calendar.YEAR);
                M1 = c1.get(Calendar.MONTH);
                c1.setTime(d2);
                A2 = c1.get(Calendar.YEAR);
                M2 = c1.get(Calendar.MONTH);
                int year = ((((A2 * 12) + M2) - ((A1 * 12) + M1)) / 12);
                int month = ((((A2 * 12) + M2) - ((A1 * 12) + M1)) - (year * 12));
                String ry = ((year != 0) ? (year + " Ans") : (""));
                String rm = ((month != 0) ? (" (" + month + " Mois)") : (""));
                return ry + "" + rm;
            }
        }
        return "0";
    }

    public static double giveDecimalPart(double nbre) {
        int entier = (int) nbre;
        return nbre - entier;
    }

    public static Date giveOnlyDate(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date givePrevOrNextDate(Date d, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    public static Date givePrevOrNextDate(Date d, int type, int nombre) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(type, nombre);
        return cal.getTime();
    }

    public static final String PASSWORD = "YVS1910/%";

    public static String PASSWORD() {
        Calendar c = Calendar.getInstance();
        String r = PASSWORD;
        int v = c.get(Calendar.DAY_OF_MONTH);
        r += v > 9 ? v : "0" + v;
        v = c.get(Calendar.MONTH) + 1;
        r += v > 9 ? v : "0" + v;
        v = c.get(Calendar.YEAR);
        r += v > 9 ? v : "0" + v;
        return r;
    }
}

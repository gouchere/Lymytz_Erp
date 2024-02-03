/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.io.IOUtils;
import yvs.init.Initialisation;
import static yvs.util.Utilitaire.getIniTializeDate;

/**
 *
 * @author Yves
 */
@ManagedBean
@ApplicationScoped
public class Constantes implements Serializable {

    public static Object managedBean;
    public static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static double MAX_DOUBLE = Double.MAX_VALUE;
    public static String ADRESSE = "Lymytz_Web";
    public static boolean USE_PRETTY = true;

    public static final int MAX_VALUE_PARAM = 1000;
    public static final char DEFAULT_SEPARATOR = ';';
    public static final char DEFAULT_QUOTE = '"';

    public static final String NEW_LINE = ("\\r\\n").replace("\\r", "");
    public static final String NEW_LINE_END = ("\\r\\n\"").replace("\\r", "");

    public static String[] tabMois = new String[]{"Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre"};
    public static int[] tabJour = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
    public static final String FIFO = "First Int First Out";
    public static final String LIFO = "Last Int First Out";
    public static final String CMP = "Cout Moyen Pondere";
    public static final String NOTHING = "NOTHING";

    public static String DEFAULT_PHOTO = "";
    public static String DEFAULT_PHOTO_EMPLOYE_MAN = "";
    public static String DEFAULT_PHOTO_EMPLOYE_WOMAN = "";

    public static final String SYMBOLE_SUP_EGALE = ">=";
    public static final String SYMBOLE_INF_EGALE = "<=";
    public static final String SYMBOLE_BETWEEN = "<>";
    public static final String SYMBOLE_DIFFERENT = "!=";
    public static final String SYMBOLE_EGALE = "=";

    public static final char SERVICE_RESTAURANT = 'R';
    public static final char SERVICE_LOGEMENT = 'L';
    public static final char SERVICE_COMMERCE_GENERAL = 'C';

    public static final String CIVILITE_M = "M";
    public static final String CIVILITE_MME = "Mme";
    public static final String CIVILITE_MLLE = "Mlle";

    public static final String BETWEEN = "BETWEEN";

    public static final String PRINCIPAL = "P";
    public static final String PRINCIPAL_NAME = "PRINCIPAL";
    public static final String AUXILLIAIRE = "A";
    public static final String AUXILIAIRE_NAME = "AUXILLIAIRE";

    public static final String CMP1 = "CMPI";
    public static final String CMP2 = "CMPII";
    public static final String CMPU1 = "Cout Moyen Pondere 1";
    public static final String CMPU2 = "Cout Moyen Pondere 2";

    public static final String FIFO_NAME = "First Int First Out";
    public static final String LIFO_NAME = "Last Int First Out";
    public static final String CMP1_NAME = "Cout Moyen Pondere I";
    public static final String CMP2_NAME = "Cout Moyen Pondere II";

    public static final String T_PAYS = "Pays";
    public static final String PAYS = "Pays";
    public static final String T_VILLES = "Villes";
    public static final String VILLES = "Villes";
    public static final String T_SECTEURS = "Secteurs";
    public static final String SECTEURS = "Secteurs";
    public static final int ENV_MAJORITE = 21;
    public static final long HOUR = 3600000;
    public static final String LUNDI = "Lundi";
    public static final String MARDI = "Mardi";
    public static final String MERCREDI = "Mercredi";
    public static final String JEUDI = "Jeudi";
    public static final String VENDREDI = "Vendredi";
    public static final String SAMEDI = "Samedi";
    public static final String DIMANCHE = "Dimanche";

    public static final String EQUILIBRE = "EQUILIBRE";
    public static final String POURCENTAGE = "POURCENTAGE";
    public static final String FR = "fr";
    public static final String EN = "en";
    public static final String CAT_MARCHANDISE = "MARCHANDISE";
    public static final String CAT_MARCHANDISE_NAME = "MARCHANDISES (NEGOCE)";
    public static final String CAT_SERVICE = "SERVICE";
    public static final String CAT_SERVICE_NAME = "SERVICES";
    public static final String CAT_PSF = "PSF";
    public static final String CAT_PSF_NAME = "PRODUITS SEMI FINI";
    public static final String CAT_MP = "MP";
    public static final String CAT_MP_NAME = "MATIERES PREMIERES";
    public static final String CAT_PF = "PF";
    public static final String CAT_PF_NAME = "PRODUITS FINI";
    public static final String CAT_FOURNITURE = "FOURNITURE";
    public static final String CAT_FOURNITURE_NAME = "FOURNITURE";
    public static final String PIECE_DE_RECHANGE = "PIECE";
    public static final String PIECE_DE_RECHANGE_NAME = "PIECE DE RECHANGE";
    public static final String CAT_EMBALLAGE = "EMBALLAGE";
    public static final String CAT_EMBALLAGE_NAME = "EMBALLAGES";
    public static final String KG = "kg";
    public static final String GR = "g";
    public static final String MG = "mg";
    public static final String MODE_CONSO_UNITAIRE = "U";
    public static final String MODE_CONSO_MASSIQUE = "M";
    public static final boolean NORME_FIXE = true;
    public static final boolean NORME_VARIABLE = false;
    public static final String TIERS_TIERS = "TIERS";
    public static final String TIERS_CLIENT = "CLIENTS";
    public static final String TIERS_FOURNISSEUR = "FOURNISSEURS";
    public static final String TIERS_EMPLOYE = "EMPLOYE";
    public static final String TIERS_REPRESENTANT = "REPRESENTANT";
    public static final String DUREE_PENDANT = "Pendant";
    public static final String DUREE_JUSQUA = "Jusqu'au";
    public static final String DUREE_LE = "Le";
    public static final String OBJECTIF_QUANTITE = "Quantite";
    public static final String OBJECTIF_VALEUR = "Valeur";
    public static final String DUREE_A_PARTIR = "A Partir du";
    public static final String DUREE_PERMANENCE = "Permanence";
    public static final String NAT_COMISSION = "Comission";
    public static final String NAT_RISTOURNE = "Ristourne";
    /*
     * mode d'approvisionnement
     */
    public static final String APPRO_ACHTON = "ACHTON";   //achat only
    public static final String APPRO_ENON = "ENHON";   //Entrée only
    public static final String APPRO_PRODON = "PRODON";   //production only
    public static final String APPRO_ACHT_PROD = "ACHT_PROD";   //achat et production
    public static final String APPRO_ACHT_EN = "ACHT_EN";   //achat et entré
    public static final String APPRO_PROD_EN = "PROD_EN";    //production et entrée
    public static final String APPRO_ACHT_PROD_EN = "ACHT_PROD_EN";   //achat production et entrée
    /*
     * mode de réapprovisionnement
     */
    public static final String REAPPRO_AUCUN = "AUCUN";
    public static final String REAPPRO_SEUIL = "SEUIL";
    public static final String REAPPRO_PERIODE = "PERIODIQUE";
    public static final String REAPPRO_RUPTURE = "RUPTURE";

    /**
     * Libellé des frais supplémentaire²
     *
     */
    public static final String TRANSPORT = "TRANSPORT", ASSURANCE = "ASSURANCE", COMMISSION = "COMMISSION", MANUTENTION = "MANUTENTION";
    public static final String AUTRES = "AUTRES";

    /**
     * GRH
     */
    public static final char GRH_STATUT_TEMPORAIRE = 'T';
    public static final char GRH_STATUT_PERMANANT = 'P';
    public static final char GRH_STATUT_STAGIAIRE = 'S';
    public static final char GRH_STATUT_TACHERON = 'U';
    public static final String GRH_STATUT_LIB_TEMPORAIRE = "Temporaires";
    public static final String GRH_STATUT_LIB_PERMANANT = "Permanants";
    public static final String GRH_STATUT_LIB_STAGIAIRE = "Stagiaires";
    public static final String GRH_STATUT_LIB_TACHERON = "Tacherons";
    public static final String GRH_MERE = "Dame mere";
    public static final String GRH_ANCIENNETE = "Anciennete";
    public static final String GRH_EN_COMMISSION = "EN COMMISSION";
    public static final String GRH_EN_PAUSE = "EN PAUSE";
    public static final String GRH_EN_SERVICE = "EN SERVICE";
    public static final String GRH_ABSENT = "ABSENT";

    public static final String GRH_PERMISSION_SUR_CONGE = "CONGE ANNUEL";
    public static final String GRH_PERMISSION_SUR_SALAIRE = "SALAIRE";
    public static final String GRH_PERMISSION_AUTORISE = "AUTORISE";
    public static final String GRH_PERMISSION_SPECIALE = "SPECIALE";
    public static final String GRH_TYPE_CONGE_ANNUELLE = "Annuel";
    public static final String GRH_TYPE_CONGE_CT = "CT";
    public static final String GRH_TYPE_CONGE_MALADI = "Maladie";
    public static final String GRH_TYPE_CONGE_AUTRE = "Autres";
    public static final String[] GRH_TYPES_CONGE = new String[]{GRH_TYPE_CONGE_AUTRE, GRH_TYPE_CONGE_ANNUELLE, GRH_TYPE_CONGE_CT, GRH_TYPE_CONGE_MALADI};

    public static final String S_SALAIRE_CONVENTIONNELLE = "employe.s_conv";
    public static final String S_SALAIRE_CONTRACTUELLE = "employe.s_contrat";
    public static final String S_SALAIRE_SUR_OBJETIF = "employe.s_objectif";
    public static final String S_SALAIRE_HORAIRE = "employe.s_horaire";
    public static final String S_ENFANT = "employe.Nenfant";
    //public static final String S_DURRE_REQUISE_DE_TRAVAIL = "employe.dureeMois"; //durée de travail requise pour un mois
    public static final String S_JOUR_DE_CONGE = "employe.conge";   //Fait référence au nombre de jour de congés restant dans le mois
    public static final String S_HEURE_SUPPLEMENTAIRE = "employe.heureSup";
    //public static final String S_JOUR_DE_TRAVAIL = "employe.Njour";
    public static final String POINTAGE_NB_FICHE = "presence.countFiche";   //nombre de fiche pointé et validé
    public static final String S_HEURE_JOUR_FERIE = "employe.supFerie";
    public static final String S_HEURE_DIMANCHE = "employe.supDim";
    public static final String S_ANCIENETE = "employe.ancienete";
    //nombre d'heure de travail requis
    public static final String S_NB_HEURE_REQUISE = "employe.NHrequis";
    //Nombre de jour de travail requis
    public static final String S_NB_JOUR_REQUIS = "employe.NJrequis";
    //nombre d'heure de travail effectif
    public static final String S_NB_HEURE_EFFECTIF = "employe.NHreffec";
    //nombre de jour de travail effectif
    public static final String S_NB_JOUR_EFFECTIF = "employe.NJreffec";
    //Nombre de jour travaillé entre le début du mois commercial et la date du jour 
    //(Necessite que la date de début du mois commercial soit indiqué dans les paramètres généraux)
    public static final String S_NB_JOUR_PASSE = "employe.NJpasse";/*?*/

    //Nombre de jours de congé technique
    public static final String S_NB_JOUR_CONGE_TECHNIQUE = "employe.NJCT";
    //total des notes de frais payé
    public static final String S_NOTE_DE_FRAIS = "employe.NotDeFrai";
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
    public static final String GRH_DUREE_CONGE_MOIS_REFERENCE = "conge.dureeRef";
    //Nombre de jour dans la période de référence
    public static final String GRH_DUREE_CONGE_NB_JOUR_IN_PERIODE_REF = "conge.nbJourInPeriodeRef";
    //Nombre de jour dans la période de référence
    public static final String GRH_DUREE_CONGE_NB_JOUR_REF = "conge.nbJourRef"; //fait  référence au nombre de jour de travail acceptable dans un mois pour bénéficier du congé mensuel
    public static final String UNITE_DUREE_PREAVIS_MOIS = "Mois";
    public static final String UNITE_DUREE_PREAVIS_JOUR = "Jour";

    //Rubrique des indemnité
    public static final String RUBRIQUE_INDEMNITE_PREAVIS = "PREAVIS";
    public static final String RUBRIQUE_INDEMNITE_LICENCIEMENT = "LICENCIEMENT";
    public static final String RUBRIQUE_INDEMNITE_CONGE = "CONGE";
    public static final String RUBRIQUE_RELIQUAT_SALAIRE = "RELIQUAT_SALAIRE";
    public static final String RUBRIQUE_AUTRES = "AUTRES";

    /**
     * ********************Model de documents**********************
     */
    public static final String DOCUMENT_MISSION = "MISSIONS";
    public static final String DOCUMENT_FORMATION = "FORMATIONS";
    public static final String DOCUMENT_CONGES = "CONGES";
    public static final String DOCUMENT_PERMISSION_COURTE_DUREE = "PERMISSION_CD";
    public static final String DOCUMENT_PC_MISSION = "PC_MISSIONS";
    public static final String DOCUMENT_DOC_DIVERS_CAISSE = "OPERATION_DIVERS";
    public static final String DOCUMENT_OD_NON_PLANNIFIE = "OD_NON_PLANNIFIE";
    public static final String DOCUMENT_OD_NON_COMPTABILISE = "OD_NON_COMPTABILISE";
    public static final String DOCUMENT_BON_DIVERS_CAISSE = "BON_OPERATION_DIVERS";
    public static final String DOCUMENT_FACTURE_ACHAT = "FACTURE_ACHAT";
    public static final String DOCUMENT_COMMANDE_ACHAT = "COMMANDE_ACHAT";
    public static final String DOCUMENT_ACHAT_NON_COMPTABILISE = "ACHAT_NON_COMPTABILISE";
    public static final String DOCUMENT_RETOUR_ACHAT = "RETOUR_ACHAT";
    public static final String DOCUMENT_AVOIR_ACHAT = "AVOIR_ACHAT";
    public static final String DOCUMENT_FACTURE_VENTE = "FACTURE_VENTE";
    public static final String DOCUMENT_VENTE_NON_COMPTABILISE = "VENTE_NON_COMPTABILISE";
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
    public static final String DOCUMENT_STOCK_ARTICLE = "STOCK_ARTICLE";
    public static final String DOCUMENT_ARTICLE_NON_MOUVEMENTE = "ARTICLE_NON_MOUVEMENTE";
    public static final String DOCUMENT_HIGH_PR_ARTICLE = "HIGH_PR_ARTICLE";
    public static final String DOCUMENT_ENTREE = "ENTREE_STOCK";
    public static final String DOCUMENT_BON_LIVRAISON_ACHAT = "BON_LIVRAISON_ACHAT";
    public static final String DOCUMENT_BON_LIVRAISON_VENTE = "BON_LIVRAISON_VENTE";
    public static final String DOCUMENT_FACTURE_ACHAT_LIVRE = "FACTURE_ACHAT_LIVRE";
    public static final String DOCUMENT_FACTURE_ACHAT_REGLE = "FACTURE_ACHAT_REGLE";
    public static final String DOCUMENT_FACTURE_VENTE_LIVRE = "FACTURE_VENTE_LIVRE";
    public static final String DOCUMENT_FACTURE_VENTE_REGLE = "FACTURE_VENTE_REGLE";
    public static final String DOCUMENT_INVENTAIRE_STOCK = "INVENTAIRE_STOCK";
    public static final String DOCUMENT_RECONDITIONNEMENT_STOCK = "RECONDITIONNEMENT_STOCK";
    public static final String DOCUMENT_DOC_DIVERS_DEPENSE = "DOC_DIVERS_DEPENSE";
    public static final String DOCUMENT_DOC_DIVERS_RECETTE = "DOC_DIVERS_RECETTE";
    public static final String DOCUMENT_LOWER_MARGIN = "LOWER_MARGIN";
    public static final String DOCUMENT_CP_UPPER_PR = "CP_UPPER_PR";
    public static final String DOCUMENT_ORDRE_FABRICATION_DECLARE = "ORDRE_FABRICATION_DECLARE";
    public static final String DOCUMENT_ORDRE_FABRICATION_TERMINE = "ORDRE_FABRICATION_TERMINE";
    public static final String DOCUMENT_ARTICLE = "ARTICLE";
    public static final String DOCUMENT_CLIENT = "CLIENT";
    public static final String DOCUMENT_FOURNISSEUR = "FOURNISSEUR";
    public static final String DOCUMENT_USERS = "USERS";
    public static final String DOCUMENT_EMPLOYE = "EMPLOYE";
    public static final String DOCUMENT_CAISSE = "CAISSE";
    public static final String DOCUMENT_DEPOT = "DEPOT";
    public static final String DOCUMENT_POINTVENTE = "POINTVENTE";

    public static final String PROD_OP_TYPE_TEMPS_FIXE = "Fixe";
    public static final String PROD_OP_TYPE_TEMPS_PROPORTIONNEL = "Proportionnel";
    public static final String PROD_OP_TYPE_TEMPS_CADENCE = "Cadence";

    public static final String PROD_OP_TYPE_CHARGE_MO = "MO";
    public static final String PROD_OP_TYPE_CHARGE_MACHINE = "Machine";

    public static final String PROD_OP_TYPE_COMPOSANT_NORMAL = "N";
    public static final String PROD_OP_TYPE_COMPOSANT_SOUS_PRODUIT = "SP";

    public static final String PROD_TYPE_PAGE_SUIVI = "SUIVI";
    public static final String PROD_TYPE_PAGE_CLASSIQUE = "CLASSIQUE";

    public static final Character PROD_OP_TYPE_COUT_TOTAL = 'T';
    public static final Character PROD_OP_TYPE_COUT_PROPORTIONNEL = 'P';

    public static final String PROD_TYPE_NOMENCLATURE_TRANSFORMATION = "T";
    public static final String PROD_TYPE_NOMENCLATURE_PRODUCTION = "P";
    public static final String PROD_TYPE_NOMENCLATURE_CONDTIONNEMENT = "C";

    public static final SelectItem[] DOCUMENTS = {
        new SelectItem(DOCUMENT_MISSION, DOCUMENT_MISSION),
        new SelectItem(DOCUMENT_FORMATION, DOCUMENT_FORMATION),
        new SelectItem(DOCUMENT_PERMISSION_COURTE_DUREE, DOCUMENT_PERMISSION_COURTE_DUREE),
        new SelectItem(DOCUMENT_PC_MISSION, DOCUMENT_PC_MISSION),
        new SelectItem(DOCUMENT_DOC_DIVERS_CAISSE, DOCUMENT_DOC_DIVERS_CAISSE),
        new SelectItem(DOCUMENT_FACTURE_ACHAT, DOCUMENT_FACTURE_ACHAT),
        new SelectItem(DOCUMENT_FACTURE_VENTE, DOCUMENT_FACTURE_VENTE),
        new SelectItem(DOCUMENT_TRANSFERT, DOCUMENT_TRANSFERT),
        new SelectItem(DOCUMENT_SORTIE, DOCUMENT_SORTIE),
        new SelectItem(DOCUMENT_CONGES, DOCUMENT_CONGES),};

    public static final String CONVERTER_STOCK = "S";
    public static final String CONVERTER_MONNAIE = "M";
    public static final String CONVERTER_COUT_STOCK = "CS";
    public static final String CONVERTER_PRODUCTION = "P";
    public static final String CONVERTER_PRODUCTIONPF = "PF";
    public static final String CONVERTER_DN = "DN";
    public static final String CONVERTER_DN_NAME = "DECIMAL SANS VIRGULE";
    public static final String CONVERTER_DNA = "DNA";
    public static final String CONVERTER_DNA_NAME = "DECIMAL AVEC VIRGULE (SANS LIMITE)";
    public static final String CONVERTER_DNS = "DNS";
    public static final String CONVERTER_DNS_NAME = "DECIMAL AVEC VIRGULE (2 CHIFFRES)";

    public static final SelectItem[] CONVERTERS = {
        new SelectItem(CONVERTER_DN, CONVERTER_DN_NAME),
        new SelectItem(CONVERTER_DNA, CONVERTER_DNA_NAME),
        new SelectItem(CONVERTER_DNS, CONVERTER_DNS_NAME),};

    public static String CRITERE_MAJORATION_CONGE_ANC = "Anciennete";
    public static String CRITERE_MAJORATION_CONGE_DAME = "Dame mere";

    /**
     * COM
     */
    //Base Remise / Ristourne
    public static final String BASE_CA = "CA";
    public static final String BASE_CAHT = "CAHT";
    public static final String BASE_CATTC = "CATTC";
    public static final String BASE_QTE = "QTE";
    public static final String BASE_MARGE = "MARGE";

    public static final char BASE_COMMISSION_CA_HT = 'H';
    public static final char BASE_COMMISSION_CA_TTC = 'T';
    public static final char BASE_COMMISSION_MARGE = 'M';
    public static final String BASE_COMMISSION_CA_HT_NAME = "C.A HT";
    public static final String BASE_COMMISSION_CA_TTC_NAME = "C.A TTC";
    public static final String BASE_COMMISSION_MARGE_NAME = "MARGE";

    public static final SelectItem[] BASES_COMMISSION = {
        new SelectItem(BASE_COMMISSION_CA_HT, BASE_COMMISSION_CA_HT_NAME),
        new SelectItem(BASE_COMMISSION_CA_TTC, BASE_COMMISSION_CA_TTC_NAME),
        new SelectItem(BASE_COMMISSION_MARGE, BASE_COMMISSION_MARGE_NAME),};

    public static final char CIBLE_TRANCHE_CA = 'C';
    public static final char CIBLE_TRANCHE_REMISE = 'R';
    public static final char CIBLE_TRANCHE_MARGE = 'M';
    public static final String CIBLE_TRANCHE_CA_NAME = "C.A";
    public static final String CIBLE_TRANCHE_REMISE_NAME = "REMISE";
    public static final String CIBLE_TRANCHE_MARGE_NAME = "MARGE";

    public static final SelectItem[] CIBLES_TRANCHE = {
        new SelectItem(CIBLE_TRANCHE_CA, CIBLE_TRANCHE_CA_NAME),
        new SelectItem(CIBLE_TRANCHE_REMISE, CIBLE_TRANCHE_REMISE_NAME),
        new SelectItem(CIBLE_TRANCHE_MARGE, CIBLE_TRANCHE_MARGE_NAME),};

    public static final char DIRECT = 'D';
    public static final char CALCULE = 'C';
    public static final String DIRECT_NAME = "DIRECT";
    public static final String CALCULE_NAME = "CALCULE";

    public static final char APPICATION_COMMISSION_FACTURE_VALIDE = 'V';
    public static final char APPICATION_COMMISSION_FACTURE_REGLE = 'R';
    public static final char APPICATION_COMMISSION_FACTURE_ENCOURS = 'E';
    public static final String APPICATION_COMMISSION_FACTURE_VALIDE_NAME = "FACTURES VALIDEES";
    public static final String APPICATION_COMMISSION_FACTURE_REGLE_NAME = "FACTURES REGLEES";
    public static final String APPICATION_COMMISSION_FACTURE_ENCOURS_NAME = "FACTURES ENCOURS";

    public static final SelectItem[] APPICATIONS_COMMISSION = {
        new SelectItem(APPICATION_COMMISSION_FACTURE_VALIDE, APPICATION_COMMISSION_FACTURE_VALIDE_NAME),
        new SelectItem(APPICATION_COMMISSION_FACTURE_ENCOURS, APPICATION_COMMISSION_FACTURE_ENCOURS_NAME),
        new SelectItem(APPICATION_COMMISSION_FACTURE_REGLE, APPICATION_COMMISSION_FACTURE_REGLE_NAME),};

    //Nature Montant
    public static final String NATURE_TAUX = "TAUX";
    public static final String NATURE_MTANT = "MONTANT";
    public static final char NATURE_POURCENTAGE = 'T';
    public static final char NATURE_MONTANT = 'M';

    public static final SelectItem[] NATURES = {
        new SelectItem(NATURE_POURCENTAGE, NATURE_TAUX),
        new SelectItem(NATURE_MONTANT, NATURE_MTANT),};

    public static final char PERIODICITE_JUSQUA = 'J';
    public static final char PERIODICITE_A_PARTIR = 'A';
    public static final char PERIODICITE_PERMANENT = 'P';
    public static final char PERIODICITE_DETERMINER = 'D';
    public static final String PERIODICITE_JUSQUA_NAME = "JUSQU'AU";
    public static final String PERIODICITE_A_PARTIR_NAME = "A PARTIR DU";
    public static final String PERIODICITE_PERMANENT_NAME = "PERMANENT";
    public static final String PERIODICITE_DETERMINER_NAME = "DETERMINER";

    public static final SelectItem[] PERIODICITES = {
        new SelectItem(PERIODICITE_JUSQUA, PERIODICITE_JUSQUA_NAME),
        new SelectItem(PERIODICITE_A_PARTIR, PERIODICITE_A_PARTIR_NAME),
        new SelectItem(PERIODICITE_DETERMINER, PERIODICITE_DETERMINER_NAME),
        new SelectItem(PERIODICITE_PERMANENT, PERIODICITE_PERMANENT_NAME),};
    public static final Character STOCK_SENS_SORTIE = 'S';
    public static final Character STOCK_SENS_ENTREE = 'E';
    public static final Character STOCK_SENS_NEUTRE = 'N';
    public static final String AUTRE = "AUTRE";
    public static final String ACHAT = "ACHAT";
    public static final String SALAIRE = "SALAIRE";
    public static final String TRESORERIE = "TRESORERIE";
    public static final String VIREMENT = "VIREMENT";
    public static final String GENERAL = "GENERAL";
    public static final String VENTE = "VENTE";
    public static final String SERVICE = "SERVICE";
    public static final String LOCATION = "LOCATION";
    public static final String LOCATION_MATERIEL = "LOCATION_MATERIEL";
    public static final String LOCATION_IMMOBILIER = "LOCATION_IMMOBILIER";
    public static final String STOCK = "STOCK";
    public static final String VSTOCK = "V-STOCK";
    public static final String DEFAUT = "DEFAUT";

    public static final String SOCIETE = "SOCIETE";
    public static final String AGENCE = "AGENCE";
    public static final String DEPOT = "DEPOT";
    public static final String JOURNAL = "JOURNAL";
    public static final String POINTVENTE = "POINTVENTE";
    public static final String CAISSE = "CAISSE";
    public static final String TYPECREDIT = "TYPECREDIT";

    public static final String MONNAIE_FCFA = "Fcfa";
    public static final String MONNAIE_EURO = "Euro";
    public static final String MONNAIE_DOLLAR = "Dollard";

    public static final String MUT_LIBELLE_SALAIRE_CONTRAT = "SALAIRE CONTRAT";
    public static final String MUT_LIBELLE_SALAIRE_MOYEN = "NET MOYEN";

    public static final String MUT_LIBELLE_CONDITION_DUREE_EVALUATION = "Durée d'Evaluation";
    public static final String MUT_LIBELLE_CAPACITE_ENDETTEMENT = "Capacité Maximale d'endettement";
    public static final String MUT_LIBELLE_CONDITION_MONTANT_MAX = "Montant Maximal permis";
    public static final String MUT_LIBELLE_CONDITION_DUREE_CREDIT = "Durée du crédit";
    public static final String MUT_LIBELLE_CONDITION_MONTANT_COUVERTURE = "Montant de couverture";
    public static final String MUT_LIBELLE_CONDITION_CAPACITE_REMBOURSEMENT = "Capacité de remboursement";
    public static final String MUT_LIBELLE_CONDITION_TAUX_MENSUEL = "Taux mensuel maximale";
    public static final String MUT_LIBELE_CONDITION_TYPE_AVANCE = "Acompte du même type";
    public static final String MUT_LIBELE_CONDITION_NBRE_AVALISE = "Nombre d'avalise";

    public static final String MUT_CODE_CONDITION_DUREE_EVALUATION = "A";
    public static final String MUT_CODE_CONDITION_CAPACITE_ENDETTEMENT = "B";
    public static final String MUT_CODE_CONDITION_MONTANT_MAX = "C";
    public static final String MUT_CODE_CONDITION_DUREE_CREDIT = "D";
    public static final String MUT_CODE_CONDITION_MONTANT_COUVERTURE = "E";
    public static final String MUT_CODE_CONDITION_CAPACITE_REMBOURSEMENT = "F";
    public static final String MUT_CODE_CONDITION_TAUX_MENSUEL = "G";
    public static final String MUT_CODE_CONDITION_TYPE_AVANCE = "H";
    public static final String MUT_CODE_CONDITION_NBRE_AVALISE = "N";

    public static final String MUT_BASE_FORCAGE_AMORTISSEMENT = "AMOR";
    public static final String MUT_BASE_FORCAGE_INTERET = "INTERET";
    public static final String MUT_ETAT_CREDIT_INCOMPLET = "Incomplete";
    public static final String MUT_ETAT_CREDIT_TRANSFERE = "Transferé";

    public static final String MUT_TYPE_COMPTE_EPARGNE = "EPARGNE";
    public static final String MUT_TYPE_COMPTE_COURANT = "COURANT";
    public static final String MUT_TYPE_COMPTE_A_TERME = "A_TERME";
    public static final String MUT_TYPE_COMPTE_ASSURANCE = "ASSURANCE";

    public static final String MUT_TRANSACTIONS_MUT = "Transactions Mutuelle";
    public static final String MUT_ACTIVITE_CREDIT = "Credit";

    public static final String MUT_SENS_OP_DEPOT = "D";
    public static final String MUT_SENS_OP_RETRAIT = "R";

    public static final String MUT_NATURE_OP_EPARGNE = "EPARGNE";
    public static final String MUT_NATURE_OP_ASSURANCE = "ASSURANCE";
    public static final String MUT_NATURE_OP_ASSISTANCE = "ASSISTANCE";
    public static final String MUT_NATURE_OP_PRIMES = "PRIMES";
    public static final String MUT_NATURE_OP_INTERET = "INTERET";
    public static final String MUT_NATURE_OP_SALAIRE = "SALAIRE";
    public static final String MUT_NATURE_OP_DEPOT = "DEPOT";
    public static final String MUT_NATURE_OP_RETRAIT = "RETRAIT";
    public static final String MUT_NATURE_OP_RETENUE_FIXE = "RETENUE FIXE";

    public static final String MUT_TYPE_MONTANT_FIXE = "Fixe";
    public static final String MUT_TYPE_MONTANT_POURCENTAGE = "Pourcentage";
    public static final String MUT_TYPE_REPORT_PARTIEL = "P";
    public static final String MUT_TYPE_REPORT_TOTAL = "T";

    public static final String MUT_PERIODE_MOIS = "Mois";
    public static final String MUT_PERIODE_TRIMESTRES = "Trimestre";
    public static final String MUT_PERIODE_SEMESTRES = "Semestres";
    public static final String MUT_PERIODE_ANNEE = "Annee";

    public static final String MUT_MODE_PAIEMENT_ESPECE = "ESPECE";
    public static final String MUT_MODE_PAIEMENT_COMPTE = "COMPTE"; //paiement par dépôt ou retrait sur un compte

    public static final String MUT_TYPE_MONAIE_PHYSIQUE = "PHYSIQUE";
    public static final String MUT_TYPE_MONAIE_SCRIPTURALE = "SCRIPTURALE"; //paiement par dépôt ou retrait sur un compte

    public static final String MUT_TABLE_SRC_REGLEMENT_MENSUALITE = "REGLEMENT_MENSUALITE";
    public static final String MUT_TABLE_SRC_REGLEMENT_CREDIT = "REGLEMENT_CREDIT";
    public static final String MUT_TABLE_SRC_OPERATION_COMPTE = "OPERATION_COMPTE";
    public static final String MUT_TABLE_SRC_OPERATION_CAISSE = "OPERATION_CAISSE";

    /*COMPTA*/
    public static final String CODE_PAIEMENT_ORANGE_MONEY = "OM";
    public static final String CODE_PAIEMENT_MOBILE_MONEY = "MM";
    public static final String CODE_PAIEMENT_VISA = "VISA";
    public static final String CODE_PAIEMENT_AUTRE = "AU";

    public static final String MODE_PAIEMENT_ESPECE = "ESPECE";
    public static final String MODE_PAIEMENT_SALAIRE = "SALAIRE";
    public static final String MODE_PAIEMENT_BANQUE = "BANQUE";
    public static final String MODE_PAIEMENT_COMPENSATION = "COMPENSATION";
    public static final String MODE_PAIEMENT_ACOMPTE = "ACOMPTE";

    //Nature Type Compte
    //Action sur le déficit de crédit
    public static final String MUT_ACTION_TRANSFERE = "Transfert";
    public static final String MUT_ACTION_REPORT = "Report";
    public static final String MUT_ACTION_RIEN = "Rien";
    public static final String MUT_ACTION_CHOIX = "Choix";

    //Possibilte
    public static final String POSS_YES = "Possible";
    public static final String POSS_YES_NO = "Insuffissant";
    public static final String POSS_NO = "Impossible";
    //Indicateurs Quantitatifs
    public static final String INDIC_CA = "CA";
    public static final String INDIC_MARGE = "MARGE";
    public static final String INDIC_QUANTITE = "QUANTITE";
    public static final String INDIC_NB_CLIENT = "NB_CLIENT";
    public static final String INDIC_CREANCE = "CREANCES";

    //Etat et Statut
    public static final String ETAT_ATTENTE = "W";
    public static final String ETAT_EDITABLE = "E";
    public static final String ETAT_SOUMIS = "U";
    public static final String ETAT_REGLE = "P";
    public static final String ETAT_VALIDE = "V";
    public static final String ETAT_ANNULE = "A";
    public static final String ETAT_RELANCE = "F";
    public static final String ETAT_LIVRE = "L";
    public static final String ETAT_PROD_LANCE = "L";
    public static final String ETAT_ENCOURS = "R";
    public static final String ETAT_RENVOYE = "B";
    public static final String ETAT_JUSTIFIE = "J";
    public static final String ETAT_INJUSTIFIE = "I";
    public static final String ETAT_SUSPENDU = "S";
    public static final String ETAT_TERMINE = "T";
    public static final String ETAT_CLOTURE = "C";
    public static final String ETAT_INCOMPLET = "M";

    /*PRODUCTION*/
    public static final char STATUT_DOC_ATTENTE = 'W',
            STATUT_DOC_ENCOUR = 'R',
            STATUT_DOC_VALIDE = 'V',
            STATUT_DOC_SUSPENDU = 'S',
            STATUT_DOC_ANNULE = 'A',
            STATUT_DOC_SOUMIS = 'U',
            STATUT_DOC_TERMINE = 'T',
            STATUT_DOC_CLOTURE = 'C',
            STATUT_DOC_PAYER = 'P',
            STATUT_DOC_LIVRER = 'L',
            STATUT_DOC_EDITABLE = 'E',
            STATUT_PROD_LANCE = 'L',
            STATUT_DOC_RENVOYE = 'B',
            STATUT_DOC_INJUSTIFIE = 'I',
            STATUT_DOC_JUSTIFIER = 'J';
    public static final String ETAT_COMPOSANT_OF_0 = "W", ETAT_COMPOSANT_OF_1 = "C";
    public static final String OF_GENERATEUR_SUGGERE = "ORDRE SUGGERE", OF_GENERATEUR_COMMANDE = "COMMANDE", OF_GENERATEUR_BESOIN_INTERNE = "FLUX INTERNE";
    public static final String PDP_TYPE_BB = "BB", PDP_TYPE_SD = "SD", PDP_TYPE_RA = "RA", PDP_TYPE_BN = "BN", PDP_TYPE_OPF = "OPF", PDP_TYPE_OPD = "OPD",
            PDP_TYPE_TM = "TM";
    public static final String OF_TYPE_GENERATION_AUTO = "AUTOMATIQUE", OF_TYPE_GENERATION_MANUEL = "MANUEL";
    public static final String PROD_VENTE = "Ventes", PROD_STOCK = "Stocks", PROD_PRODUCTION = "Productions";

    public static final String TYPE_OF_PRODUCTION = "PRODUCTION", //Production d'une quantité fixe
            TYPE_OF_REPETITIF = "REPETITIF", //Production en série, production en continu
            TYPE_OF_ARA = "ARA", //Ordre autorisant le retour du produit à l'atelier
            TYPE_OF_REPRISE = "REPRISE",//Utilisé pour reprendre un article après une première fabrication
            TYPE_OF_CONTROLE = "CONTROLE",//Utilisé pour le contrôle d'articles acheté
            TYPE_OF_OUTILLAGE = "OUTILLAGE" //pour la fabrication d'outils ou de pièces de rechange
            ;

    public static Character PROD_TYPE_COUT_TAUX = 'T';
    public static Character PROD_TYPE_COUT_VALEUR = 'V';

    public static final SelectItem[] ETATS = {
        new SelectItem(ETAT_EDITABLE, ETAT_EDITABLE),
        new SelectItem(ETAT_ANNULE, ETAT_ANNULE),
        new SelectItem(ETAT_VALIDE, ETAT_VALIDE),
        new SelectItem(ETAT_ENCOURS, ETAT_ENCOURS),
        new SelectItem(ETAT_REGLE, ETAT_REGLE),
        new SelectItem(ETAT_SOUMIS, ETAT_SOUMIS),
        new SelectItem(ETAT_LIVRE, ETAT_LIVRE)
    };

    public static final SelectItem[] ETATS_ = {
        new SelectItem(ETAT_EDITABLE, ETAT_EDITABLE),
        new SelectItem(ETAT_ANNULE, ETAT_ANNULE),
        new SelectItem(ETAT_VALIDE, ETAT_VALIDE),
        new SelectItem(ETAT_ENCOURS, ETAT_ENCOURS),
        new SelectItem(ETAT_REGLE, ETAT_REGLE)
    };

    public static final SelectItem[] ETATS_V = {
        new SelectItem(ETAT_EDITABLE, ETAT_EDITABLE),
        new SelectItem(ETAT_ANNULE, ETAT_ANNULE),
        new SelectItem(ETAT_VALIDE, ETAT_VALIDE),
        new SelectItem(ETAT_ENCOURS, ETAT_ENCOURS),
        new SelectItem(ETAT_REGLE, ETAT_REGLE),
        new SelectItem(ETAT_LIVRE, ETAT_LIVRE)
    };

    public static final SelectItem[] ETATS_T = {
        new SelectItem(ETAT_EDITABLE, ETAT_EDITABLE),
        new SelectItem(ETAT_ANNULE, ETAT_ANNULE),
        new SelectItem(ETAT_VALIDE, ETAT_VALIDE),
        new SelectItem(ETAT_SOUMIS, ETAT_SOUMIS)
    };

    public static final SelectItem[] ETATS_ES = {
        new SelectItem(ETAT_EDITABLE, ETAT_EDITABLE),
        new SelectItem(ETAT_ANNULE, ETAT_ANNULE),
        new SelectItem(ETAT_VALIDE, ETAT_VALIDE)
    };
    public static final SelectItem[] INDICATEURS_QUANTITATIF = {
        new SelectItem(INDIC_CA, INDIC_CA),
        new SelectItem(INDIC_MARGE, INDIC_MARGE),
        new SelectItem(INDIC_QUANTITE, INDIC_QUANTITE),
        new SelectItem(INDIC_NB_CLIENT, INDIC_NB_CLIENT),
        new SelectItem(INDIC_CREANCE, INDIC_CREANCE)
    };

    //Unite Temporel
    public static final List<String[]> UNITES_TIMES_ALL = new ArrayList<String[]>() {
        {
            add(new String[]{UNITE_TIERCE_, UNITE_TIERCE});
            add(new String[]{UNITE_SECONDE_, UNITE_SECONDE});
            add(new String[]{UNITE_MINUTE_, UNITE_MINUTE});
            add(new String[]{UNITE_HEURE_, UNITE_HEURE});
            add(new String[]{UNITE_JOUR_, UNITE_JOUR});
            add(new String[]{UNITE_SEMAINE_, UNITE_SEMAINE});
            add(new String[]{UNITE_MOIS_, UNITE_MOIS});
            add(new String[]{UNITE_ANNEE_, UNITE_ANNEE});
        }
    ;
    };
    public static final List<String[]> UNITES_TIMES = new ArrayList<String[]>() {
        {
            add(new String[]{UNITE_HEURE_, UNITE_HEURE});
            add(new String[]{UNITE_JOUR_, UNITE_JOUR});
            add(new String[]{UNITE_SEMAINE_, UNITE_SEMAINE});
            add(new String[]{UNITE_MOIS_, UNITE_MOIS});
            add(new String[]{UNITE_ANNEE_, UNITE_ANNEE});
        }
    };
    public static final List<String[]> UNITES_TIMES_m = new ArrayList<String[]>() {
        {
            add(new String[]{UNITE_HEURE_, UNITE_HEURE_m});
            add(new String[]{UNITE_JOUR_, UNITE_JOUR_m});
            add(new String[]{UNITE_SEMAINE_, UNITE_SEMAINE_m});
            add(new String[]{UNITE_MOIS_, UNITE_MOIS});
            add(new String[]{UNITE_ANNEE_, UNITE_ANNEE_m});
        }
    };
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
    public static final String UNITE_SEMESTRE_ = "Se";
    public static final String UNITE_SEMESTRE = "Semestre(s)";
    public static final String UNITE_SEMESTRE_m = "Seme(s)";
    public static final String UNITE_TRIMESTRE_ = "Tr";
    public static final String UNITE_TRIMESTRE = "Trimestre(s)";
    public static final String UNITE_TRIMESTRE_m = "Trim(s)";
    public static final String UNITE_MOIS_ = "M";
    public static final String UNITE_MOIS = "Mois";
    public static final String UNITE_ANNEE_ = "A";
    public static final String UNITE_ANNEE = "Année(s)";
    public static final String UNITE_ANNEE_m = "An(s)";

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
    public static final String TYPE_BRL = "BRL"; //Bon Retour Location
    public static final String TYPE_BRL_NAME = "Bon Retour Location"; //

    //Type de documents autres
    public static final String TYPE_COM = "CM"; //Commission
    public static final String TYPE_COM_NAME = "Commission"; //
    public static final String TYPE_OD = "OD"; //Operation Divers
    public static final String TYPE_OD_NAME = "Operation Divers"; //
    public static final String TYPE_OD_RECETTE_NAME = "Recette OD"; //
    public static final String TYPE_OD_DEPENSE_NAME = "Depense OD"; //
    public static final String TYPE_BP = "BP"; //Bon Provisoire
    public static final String TYPE_BP_NAME = "Bon Provisoire"; //
    public static final String TYPE_PIECE_COMPTABLE_NAME = "PIECE COMPTABLE"; //
    public static final String TYPE_RC = "RC"; //Conditionnement
    public static final String TYPE_RC_NAME = "Conditionnement"; //
    public static final String TYPE_PT = "PT"; //Piece Tresorerie
    public static final String TYPE_PT_NAME = "Piece Tresorerie"; //
    public static final String TYPE_PC_NAME = "Piece Caisse"; //
    public static final String TYPE_PC_ACHAT_NAME = "Piece Caisse Achat"; //
    public static final String TYPE_PC_VENTE_NAME = "Piece Caisse Vente"; //
    public static final String TYPE_PC_DIVERS_NAME = "Piece Caisse Divers"; //
    public static final String TYPE_PC_MISSION_NAME = "Piece Caisse Mission"; //
    public static final String TYPE_PC_COMMISSION_NAME = "Piece Caisse Commission"; //
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
    public static final String TYPE_CGE = "CGE"; //Conge
    public static final String TYPE_CGE_NAME = "Conge"; //

    public static final String PROD_TYPE_INDICATEUR_QUALITATIF = "QUALITATIF";
    public static final String PROD_TYPE_INDICATEUR_QUANTITATIF = "QUANTITATIF";

    public static final String PROD_TYPE_PROD = "OF";
    public static final String PROD_TYPE_PROD_NAME = "Ordre Fabrication";

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
    public static final String NAT_RISTOURNE_VENTE = "RISTOURNE_VENTE";
    public static final String NAT_RISTOURNE_ACHAT = "RISTOURNE_ACHAT";
    public static final String NAT_RETENUE = "RETENUE";

    public static final SelectItem[] NATURES_COMPTE = {
        new SelectItem(NAT_AUTRE, NAT_AUTRE),
        new SelectItem(NAT_ABONNEMENT, "ABONNEMENT"),
        new SelectItem(NAT_ACOMPTE_CLIENT, "ACOMPTE CLIENT"),
        new SelectItem(NAT_ACOMPTE_FOURNISSEUR, "ACOMPTE FOURNISSEUR"),
        new SelectItem(NAT_ARRONDI_GAIN, "ARRONDI GAIN"),
        new SelectItem(NAT_ARRONDI_PERTE, "ARRONDI PERTE"),
        new SelectItem(NAT_COMPENSATION, "COMPENSATION"),
        new SelectItem(NAT_CREDIT_CLIENT, "CREDIT CLIENT"),
        new SelectItem(NAT_CREDIT_FOURNISSEUR, "CREDIT FOURNISSEUR"),
        new SelectItem(NAT_RISTOURNE_ACHAT, "RISTOURNE ACHAT"),
        new SelectItem(NAT_RISTOURNE_VENTE, "RISTOURNE VENTE"),
        new SelectItem(NAT_RETENUE, "RETENUE"),};

    //gestion Piece Tresorerie
    public static final String SCR_ARTICLES = "BASE_ARTICLE";//
    public static final String SCR_CONDITIONNEMENT = "BASE_CONDITIONNEMENT";//
    public static final String SCR_CLIENTS = "BASE_CLIENT";//
    public static final String SCR_DICTIONNAIRE = "DOC_ZONE";//
    public static final String SCR_CATEGORIE = "BASE_CATEGORIE";//
    public static final String SCR_BON_PROVISOIRE = "BON_PROVISOIRE";// 
    public static final String SCR_BON_PROVISOIRE_NAME = "BON PROVISOIRE";
    public static final String SCR_NOTIF_ACHAT = "NOTIF_ACHAT";//
    public static final String SCR_NOTIF_VENTE = "NOTIF_VENTE";//
    public static final String SCR_ACHAT = "DOC_ACHAT";//
    public static final String SCR_ACHAT_NAME = "FACTURE ACHAT";
    public static final String SCR_AVOIR_ACHAT = "AVOIR_ACHAT";//
    public static final String SCR_AVOIR_ACHAT_NAME = "AVOIR ACHAT";
    public static final String SCR_VENTE = "DOC_VENTE";//
    public static final String SCR_VENTE_NAME = "FACTURE VENTE";
    public static final String SCR_AVOIR_VENTE = "AVOIR_VENTE";//
    public static final String SCR_AVOIR_VENTE_NAME = "AVOIR VENTE";
    public static final String SCR_HEAD_VENTE = "JOURNAL_VENTE";//
    public static final String SCR_HEAD_VENTE_NAME = "JOURNAL VENTE";
    public static final String SCR_SALAIRE = "ORDRE_SALAIRE";//
    public static final String SCR_SALAIRE_NAME = "ORDRE SALAIRE";
    public static final String SCR_BULLETIN = "BULLETIN";
    public static final String SCR_BULLETIN_NAME = "BULLETIN";
    public static final String SCR_RETENUE = "RETENUE";//
    public static final String SCR_RETENUE_NAME = "RETENUE";
    public static final String SCR_DIVERS = "DOC_DIVERS";
    public static final String SCR_DIVERS_NAME = "OPERATION DIVERS";
    public static final String SCR_MISSIONS = "MISSION";
    public static final String SCR_CREDIT_VENTE = "CREDIT_VENTE";
    public static final String SCR_CREDIT_VENTE_NAME = "CREDITS CLIENTS";
    public static final String SCR_CREDIT_ACHAT = "CREDIT_ACHAT";
    public static final String SCR_CREDIT_ACHAT_NAME = "CREDITS FOURNISSEURS";
    public static final String SCR_ACOMPTE_ACHAT = "ACOMPTE_ACHAT";
    public static final String SCR_ACOMPTE_ACHAT_NAME = "ACOMPTES FOURNISSEURS";
    public static final String SCR_ACOMPTE_VENTE = "ACOMPTE_VENTE";
    public static final String SCR_ACOMPTE_VENTE_NAME = "AVANCES CLIENTS";
    public static final String SCR_CAISSE_CREDIT_ACHAT = "CAISSE_CREDIT_ACHAT";
    public static final String SCR_CAISSE_CREDIT_ACHAT_NAME = "REGLEMENT CREDITS ACHAT";
    public static final String SCR_CAISSE_CREDIT_VENTE = "CAISSE_CREDIT_VENTE";
    public static final String SCR_CAISSE_CREDIT_VENTE_NAME = "REGLEMENT CREDITS CLIENTS";
    public static final String SCR_VIREMENT = "DOC_VIREMENT";
    public static final String SCR_VIREMENT_NAME = "VIREMENT";
    public static final String SCR_ECART_VENTE = "ECART_VENTE";
    public static final String SCR_ECART_VENTE_NAME = "ECART VENTE";
    public static final String SCR_NOTE_FRAIS = "NOTE_DE_FRAIS";
    public static final String SCR_NOTE_FRAIS_NAME = "NOTE DE FRAIS";
    public static final String SCR_COMMISSION = "COMMISSION";
    public static final String SCR_COMMISSION_NAME = "COMMISSIONS";
    public static final String SCR_CAISSE_ACHAT = "CAISSE_ACHAT";
    public static final String SCR_CAISSE_ACHAT_NAME = "REGLEMENT FACTURE ACHAT";
    public static final String SCR_CAISSE_AVOIR_ACHAT = "CAISSE_AVOIR_ACHAT";
    public static final String SCR_CAISSE_AVOIR_ACHAT_NAME = "REGLEMENT AVOIR ACHAT";
    public static final String SCR_CAISSE_DIVERS = "CAISSE_DIVERS";
    public static final String SCR_CAISSE_DIVERS_NAME = "REGLEMENT OPERATION DIVERS";
    public static final String SCR_ABONNEMENT_DIVERS = "ABONNEMENT_DIVERS";
    public static final String SCR_ABONNEMENT_DIVERS_NAME = "ABONNEMENT OPERATION DIVERS";
    public static final String SCR_CAISSE_VENTE = "CAISSE_VENTE";
    public static final String SCR_CAISSE_VENTE_NAME = "REGLEMENT FACTURE VENTE";
    public static final String SCR_CAISSE_AVOIR_VENTE = "CAISSE_AVOIR_VENTE";
    public static final String SCR_CAISSE_AVOIR_VENTE_NAME = "REGLEMENT AVOIR VENTE";
    public static final String SCR_CAISSE_COMMISSION = "CAISSE_COMMISSION";
    public static final String SCR_CAISSE_COMMISSION_NAME = "REGLEMENT COMMISSIONS";
    public static final String SCR_PHASE_CAISSE_VENTE = "PHASE_CAISSE_VENTE";
    public static final String SCR_PHASE_CAISSE_VENTE_NAME = "PHASE CHEQUE FACTURE VENTE";
    public static final String SCR_PHASE_ACOMPTE_VENTE = "PHASE_ACOMPTE_VENTE";
    public static final String SCR_PHASE_ACOMPTE_VENTE_NAME = "PHASE CHEQUE ACOMPTE CLIENT";
    public static final String SCR_PHASE_CREDIT_VENTE = "PHASE_CREDIT_VENTE";
    public static final String SCR_PHASE_CREDIT_VENTE_NAME = "PHASE CHEQUE REGLEMENT CREDIT CLIENT";
    public static final String SCR_PHASE_CAISSE_ACHAT = "PHASE_CAISSE_ACHAT";
    public static final String SCR_PHASE_CAISSE_ACHAT_NAME = "PHASE CHEQUE FACTURE ACHAT";
    public static final String SCR_PHASE_CAISSE_DIVERS = "PHASE_CAISSE_DIVERS";
    public static final String SCR_PHASE_CAISSE_DIVERS_NAME = "PHASE CHEQUE OD";
    public static final String SCR_PHASE_VIREMENT = "PHASE_VIREMENT";
    public static final String SCR_PHASE_VIREMENT_NAME = "PHASE CHEQUE VIREMENT";
    public static final String SCR_PHASE_ACOMPTE_ACHAT = "PHASE_ACOMPTE_ACHAT";
    public static final String SCR_PHASE_ACOMPTE_ACHAT_NAME = "PHASE CHEQUE ACOMPTE FOURNISSEUR";
    public static final String SCR_PHASE_CREDIT_ACHAT = "PHASE_CREDIT_ACHAT";
    public static final String SCR_PHASE_CREDIT_ACHAT_NAME = "PHASE CHEQUE REGLEMENT CREDIT FOURNISSEUR";
    public static final String SCR_AUTRES = "AUTRES";
    public static final String SCR_FRAIS_MISSIONS = "FRAIS_MISSION";
    public static final String SCR_FRAIS_MISSIONS_NAME = "FRAIS MISSION";
    public static final String SCR_TRESORERIE = "TRESORERIE";

    public static final String DOC_MOUV_BL = "BL";
    public static final String DOC_MOUV_F = "F";
    public static final String DOC_MOUV_BL_NAME = "Bons Livraison";
    public static final String DOC_MOUV_F_NAME = "Facture";

    public static final String DOC_MANAGE_ECART_RE = "RE";
    public static final String DOC_MANAGE_ECART_CR = "CR";
    public static final String DOC_MANAGE_ECART_RE_NAME = "RETENUE";
    public static final String DOC_MANAGE_ECART_CR_NAME = "CREDIT";

    public static final String MODE_INV_PERM = "P";
    public static final String MODE_INV_INTE = "I";
    public static final String MODE_INV_PERM_NAME = "Permanent";
    public static final String MODE_INV_INTE_NAME = "Intermitent";

    public static final String ENTREE = "ENTREE";
    public static final String SORTIE = "SORTIE";
    public static final String TRANSFERT = "TRANSFERT";
    public static final String INVENTAIRE = "INVENTAIRE";
    public static final String DECLARATION = "DECLARATION";
    public static final String PRODUCTION = "PRODUCTION";
    public static final String TECHNIQUE = "TECHNIQUE";
    public static final String RETOUR = "RETOUR";
    public static final String TRANSIT = "TRANSIT";
    public static final String RESERVATION = "RESERVATION";

    public static final String OP_DONS = "DONS";
    public static final String OP_RATIONS = "RATIONS";
    public static final String OP_DEPRECIATION = "DEPRECIATION";
    public static final String OP_INITIALISATION = "INITIALISATION";
    public static final String OP_AJUSTEMENT_STOCK = "AJUSTEMENT STOCK";
    public static final String OP_RECONDITIONNEMENT = "RECONDITIONNEMENT";

    public static final String DEPENSE = "DEPENSE";
    public static final String RECETTE = "RECETTE";
    public static final String COMPTA_DEPENSE = "D";
    public static final String COMPTA_RECETTE = "R";
    /*
     TABLES
    
     */
    public static final String yvs_com_mensualite_facture_vente = "yvs_com_mensualite_facture_vente";
    public static final String yvs_com_mensualite_facture_achat = "yvs_com_mensualite_facture_achat";
    public static final String yvs_base_mensualite_doc_divers = "yvs_base_mensualite_doc_divers";
    public static final String yvs_com_contenu_doc_vente = "yvs_com_contenu_doc_vente";
    public static final String yvs_com_contenu_doc_achat = "yvs_com_contenu_doc_achat";
    public static final String yvs_com_contenu_doc_stock = "yvs_com_contenu_doc_stock";
    public static final String yvs_com_contenu_doc_stock_reception = "yvs_com_contenu_doc_stock_reception";
    public static final String yvs_com_ration = "yvs_com_ration";
    public static final String yvs_prod_of_suivi_flux = "yvs_prod_of_suivi_flux";
    public static final String yvs_prod_declaration_production = "yvs_prod_declaration_production";
    public static final String yvs_prod_contenu_conditionnement = "yvs_prod_contenu_conditionnement";
    public static final String yvs_prod_fiche_conditionnement = "yvs_prod_fiche_conditionnement";
    public static final String yvs_prod_composant_ordre_fabrication = "yvs_prod_composant_ordre_fabrication";

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

    public static final SelectItem[] MODULES = {
        new SelectItem(MOD_GRH, MOD_GRH_NAME),
        new SelectItem(MOD_MUT, MOD_MUT_NAME),
        new SelectItem(MOD_COM, MOD_COM_NAME),
        new SelectItem(MOD_MSG, MOD_MSG_NAME),
        new SelectItem(MOD_REL, MOD_REL_NAME),
        new SelectItem(MOD_COFI, MOD_COFI_NAME),
        new SelectItem(MOD_PROD, MOD_PROD_NAME),};

    /*
     MODULES
     */
    public static final String COUT_MIXTE = "L";
    public static final String COUT_MIXTE_NAME = "MIXTE";
    public static final String COUT_VENTE = "V";
    public static final String COUT_VENTE_NAME = "VENTE";
    public static final String COUT_ACHAT = "A";
    public static final String COUT_ACHAT_NAME = "ACHAT";
    public static final String COUT_MISSION = "M";
    public static final String COUT_MISSION_NAME = "MISSION";
    public static final String COUT_FORMATION = "F";
    public static final String COUT_FORMATION_NAME = "FORMATION";
    public static final String COUT_TRANSFERT = "T";
    public static final String COUT_TRANSFERT_NAME = "TRANSFERT";
    public static final String COUT_CREDIT = "C";
    public static final String COUT_CREDIT_NAME = "CREDIT";
    public static final String COUT_VIREMENT = "I";
    public static final String COUT_VIREMENT_NAME = "VIREMENT";
    public static final String COUT_OD = "O";
    public static final String COUT_OD_NAME = "OPERATION DIVERS";

    public static final SelectItem[] COUTS = {
        new SelectItem(COUT_MIXTE, COUT_MIXTE_NAME),
        new SelectItem(COUT_VENTE, COUT_VENTE_NAME),
        new SelectItem(COUT_ACHAT, COUT_ACHAT_NAME),
        new SelectItem(COUT_CREDIT, COUT_CREDIT_NAME),
        new SelectItem(COUT_MISSION, COUT_MISSION_NAME),
        new SelectItem(COUT_FORMATION, COUT_FORMATION_NAME),
        new SelectItem(COUT_TRANSFERT, COUT_TRANSFERT_NAME),
        new SelectItem(COUT_OD, COUT_OD_NAME),
        new SelectItem(COUT_VIREMENT, COUT_VIREMENT_NAME),};

    public static List<SelectItem> COUTS(String module) {
        List<SelectItem> list = new ArrayList<>();
        list.add(new SelectItem(COUT_MIXTE, COUT_MIXTE_NAME));
        switch (module) {
            case MOD_GRH:
                list.add(new SelectItem(COUT_MISSION, COUT_MISSION_NAME));
                list.add(new SelectItem(COUT_FORMATION, COUT_FORMATION_NAME));
                break;
            case MOD_MUT:
                break;
            case MOD_COM:
                list.add(new SelectItem(COUT_VENTE, COUT_VENTE_NAME));
                list.add(new SelectItem(COUT_ACHAT, COUT_ACHAT_NAME));
                list.add(new SelectItem(COUT_TRANSFERT, COUT_TRANSFERT_NAME));
                break;
            case MOD_MSG:
                break;
            case MOD_REL:
                break;
            case MOD_COFI:
                list.add(new SelectItem(COUT_CREDIT, COUT_CREDIT_NAME));
                list.add(new SelectItem(COUT_VIREMENT, COUT_VIREMENT_NAME));
                list.add(new SelectItem(COUT_OD, COUT_OD_NAME));
                break;
            case MOD_PROD:
                break;
        }
        return list;
    }

    public static final String UNITE_TEMPS = "H";
    public static final String UNITE_TEMPS_NAME = "TEMPS";
    public static final String UNITE_QUANTITE = "Q";
    public static final String UNITE_QUANTITE_NAME = "QUANTITE";
    public static final String UNITE_OEUVRE = "O";
    public static final String UNITE_OEUVRE_NAME = "OEUVRE";
    public static final String UNITE_AUTRE = "A";
    public static final String UNITE_AUTRE_NAME = "AUTRE";

    public static final SelectItem[] UNITES_MESURE = {
        new SelectItem(UNITE_QUANTITE, UNITE_QUANTITE_NAME),
        new SelectItem(UNITE_TEMPS, UNITE_TEMPS_NAME),
        new SelectItem(UNITE_OEUVRE, UNITE_OEUVRE_NAME),};

    public static final String UNITE_VENTE = "V";
    public static final String UNITE_VENTE_NAME = "VENTE";
    public static final String UNITE_ACHAT = "A";
    public static final String UNITE_ACHAT_NAME = "ACHAT";
    public static final String UNITE_STOCKAGE = "S";
    public static final String UNITE_STOCKAGE_NAME = "STOCKAGE";
    public static final String UNITE_TRANSPORT = "T";
    public static final String UNITE_TRANSPORT_NAME = "TRANSPORT";
    public static final String UNITE_TRANSFERT = "F";
    public static final String UNITE_TRANSFERT_NAME = "TRANSFERT";
    public static final String UNITE_ENTREE = "E";
    public static final String UNITE_ENTREE_NAME = "ENTREE";
    public static final String UNITE_SORTIE = "D";
    public static final String UNITE_SORTIE_NAME = "SORTIE";
    public static final SelectItem[] UNITES_CONDITIONNEMENT = {
        new SelectItem(UNITE_VENTE, UNITE_VENTE_NAME),
        new SelectItem(UNITE_ACHAT, UNITE_ACHAT_NAME),
        new SelectItem(UNITE_STOCKAGE, UNITE_STOCKAGE_NAME),
        new SelectItem(UNITE_TRANSPORT, UNITE_TRANSPORT_NAME),
        new SelectItem(UNITE_TRANSFERT, UNITE_TRANSFERT_NAME),
        new SelectItem(UNITE_ENTREE, UNITE_ENTREE_NAME),
        new SelectItem(UNITE_SORTIE, UNITE_SORTIE_NAME),};

    public static final String MOUV_CAISS_ENTREE = "R";
    public static final String MOUV_CAISS_SORTIE = "D";

    public static final String MOUV_ENTREE = "E";
    public static final String MOUV_SORTIE = "S";
    public static final String MOUV_DEBIT = "D";
    public static final String MOUV_CREDIT = "C";

    public static final Options[] CATEGORIES = new Options[]{
        new Options(Constantes.CAT_MARCHANDISE, Constantes.CAT_MARCHANDISE_NAME),
        new Options(Constantes.CAT_PSF, Constantes.CAT_PSF_NAME),
        new Options(Constantes.CAT_PF, Constantes.CAT_PF_NAME),
        new Options(Constantes.CAT_MP, Constantes.CAT_MP_NAME),
        new Options(Constantes.CAT_FOURNITURE, Constantes.CAT_FOURNITURE_NAME),
        new Options(Constantes.CAT_SERVICE, Constantes.CAT_SERVICE_NAME),
        new Options(Constantes.PIECE_DE_RECHANGE, Constantes.PIECE_DE_RECHANGE_NAME),
        new Options(Constantes.CAT_EMBALLAGE, Constantes.CAT_EMBALLAGE_NAME)
    };
    public static final Options[] UNITE_MASSES = new Options[]{
        new Options(Constantes.KG, "Kilogramme"),
        new Options(Constantes.GR, "Gramme"),
        new Options(Constantes.MG, "Miligramme")
    };
    public static final Options[] MODE_CONSO = new Options[]{
        new Options(Constantes.MODE_CONSO_MASSIQUE, "Massique"),
        new Options(Constantes.MODE_CONSO_UNITAIRE, "Unitaire")
    };
    public static final Options[] MODES_APPRO = new Options[]{
        new Options(Constantes.APPRO_ACHTON, "ACHAT UNIQUEMENT"),
        new Options(Constantes.APPRO_ENON, "ENTREE UNIQUEMENT"),
        new Options(Constantes.APPRO_PRODON, "PRODUCTION UNIQUEMENT"),
        new Options(Constantes.APPRO_ACHT_EN, "ACHAT + ENTREE"),
        new Options(Constantes.APPRO_ACHT_PROD, "ACHAT + PRODUCTION"),
        new Options(Constantes.APPRO_PROD_EN, "PRODUCTION + ENTREE"),
        new Options(Constantes.APPRO_ACHT_PROD_EN, "ACHAT + PRODUCTION + ENTREE")
    };
    public static final Options[] MODES_REAPPRO = new Options[]{
        new Options(Constantes.REAPPRO_SEUIL, Constantes.REAPPRO_SEUIL),
        new Options(Constantes.REAPPRO_RUPTURE, Constantes.REAPPRO_RUPTURE),
        new Options(Constantes.REAPPRO_PERIODE, Constantes.REAPPRO_PERIODE)
    };
    public static final Options[] METHODES_VAL = new Options[]{
        new Options("FIFO", Constantes.FIFO),
        new Options("CMPI", Constantes.CMPU1),
        new Options("CMPII", Constantes.CMPU2)
    };

    public static final String EXPORT_CONTENU_JOURNAL = "EXP_CONT_JNL";
    public static final String EXPORT_CONTENU_JOURNAL_NAME = "Contenu Journal";
    public static final String EXPORT_ARTICLE = "EXP_ART";
    public static final String EXPORT_ARTICLE_NAME = "Articles";
    public static final String EXPORT_CLIENT = "EXP_CLT";
    public static final String EXPORT_CLIENT_NAME = "Clients";
    public static final String EXPORT_PLAN_COMPTABLE = "EXP_PLAN_CPT";
    public static final String EXPORT_PLAN_COMPTABLE_NAME = "Plan Comptable";
    public static final String EXPORT_SALAIRE = "EXP_SALAIRE";
    public static final String EXPORT_SALAIRE_NAME = "Salaire";
    public static final String EXPORT_EMPLOYE = "EXP_EMPLOYE";
    public static final String EXPORT_EMPLOYE_NAME = "employe";
    public static final String EXPORT_DEPARTEMENT = "EXP_DEPARTEMENT";
    public static final String EXPORT_DEPARTEMENT_NAME = "departement";
    public static final String EXPORT_POSTE_TRAVAIL = "EXP_POSTE_TRAVAIL";
    public static final String EXPORT_POSTE_TRAVAIL_NAME = "poste_travail";
    public static final String EXPORT_CONTRATS = "EXP_CONTRATS";
    public static final String EXPORT_CONTRATS_NAME = "contrats";

    public static final SelectItem[] TYPE_ETAT_EXPORT = {
        new SelectItem(EXPORT_CONTENU_JOURNAL, EXPORT_CONTENU_JOURNAL_NAME),
        new SelectItem(EXPORT_ARTICLE, EXPORT_ARTICLE_NAME),
        new SelectItem(EXPORT_CLIENT, EXPORT_CLIENT_NAME),
        new SelectItem(EXPORT_SALAIRE, EXPORT_SALAIRE_NAME),
        new SelectItem(EXPORT_PLAN_COMPTABLE, EXPORT_PLAN_COMPTABLE_NAME),
        new SelectItem(EXPORT_EMPLOYE, EXPORT_EMPLOYE_NAME),
        new SelectItem(EXPORT_DEPARTEMENT, EXPORT_DEPARTEMENT_NAME),
        new SelectItem(EXPORT_POSTE_TRAVAIL, EXPORT_POSTE_TRAVAIL_NAME),
        new SelectItem(EXPORT_CONTRATS, EXPORT_CONTRATS_NAME),};

    public static final String FILE_TEXT = "TXT";
    public static final String FILE_CSV = "CSV";
    public static final String FILE_PDF = "PDF";
    public static final String FILE_XLS = "XLS";
    public static final String FILE_XLSX = "XLSX";
    public static final String FILE_XML = "XML";

    public static final SelectItem[] FILES_EXTENSION = {
        new SelectItem(FILE_TEXT, FILE_TEXT),
        new SelectItem(FILE_CSV, FILE_CSV),
        new SelectItem(FILE_XLS, FILE_XLS),
        new SelectItem(FILE_XLSX, FILE_XLSX),
        new SelectItem(FILE_XML, FILE_XML),
        new SelectItem(FILE_PDF, FILE_PDF),};

    public static final char TYPE_MENSUALITE_ANNUITE_FIXE = 'N';
    public static final String TYPE_MENSUALITE_ANNUITE_FIXE_NAME = "ANNUITE FIXE";
    public static final char TYPE_MENSUALITE_AMORTISSEMENT_FIXE = 'M';
    public static final String TYPE_MENSUALITE_AMORTISSEMENT_FIXE_NAME = "AMORTISSEMENT FIXE";
    public static final char TYPE_MENSUALITE_INFINE = 'I';
    public static final String TYPE_MENSUALITE_INFINE_NAME = "IN-FINE";

    public static final SelectItem[] MUT_TYPE_MENSUALITE = {
        new SelectItem(TYPE_MENSUALITE_ANNUITE_FIXE, TYPE_MENSUALITE_ANNUITE_FIXE_NAME),
        new SelectItem(TYPE_MENSUALITE_AMORTISSEMENT_FIXE, TYPE_MENSUALITE_AMORTISSEMENT_FIXE_NAME),
        new SelectItem(TYPE_MENSUALITE_INFINE, TYPE_MENSUALITE_INFINE_NAME),};

    public static final char FORMULE_INTERET_SIMPLE = 'S';
    public static final String FORMULE_INTERET_SIMPLE_NAME = "SIMPLE";
    public static final char FORMULE_INTERET_COMPOSE = 'C';
    public static final String FORMULE_INTERET_COMPOSE_NAME = "COMPOSE";

    public static final SelectItem[] MUT_FORMULE_INTERET = {
        new SelectItem(FORMULE_INTERET_SIMPLE, FORMULE_INTERET_SIMPLE_NAME),
        new SelectItem(FORMULE_INTERET_COMPOSE, FORMULE_INTERET_COMPOSE_NAME),};

    public static final char MODEL_REMBOURSEMENT_STRICT = 'S';
    public static final String MODEL_REMBOURSEMENT_STRICT_NAME = "STRICT";
    public static final char MODEL_REMBOURSEMENT_MODULABLE = 'M';
    public static final String MODEL_REMBOURSEMENT_MODULABLE_NAME = "MODULABLE";

    public static final SelectItem[] MUT_MODEL_REMBOURSEMENT = {
        new SelectItem(MODEL_REMBOURSEMENT_STRICT, MODEL_REMBOURSEMENT_STRICT_NAME),
        new SelectItem(MODEL_REMBOURSEMENT_MODULABLE, MODEL_REMBOURSEMENT_MODULABLE_NAME),};

    public static final char PENALITE_BASE_MENSUALITE = 'M';
    public static final String PENALITE_BASE_MENSUALITE_NAME = "MENSUALITES ANCITIPEES";
    public static final char PENALITE_BASE_INTERET = 'I';
    public static final String PENALITE_BASE_INTERET_NAME = "INTERETS SUR MENSUALITES ANTICIPEES";

    public static final SelectItem[] MUT_BASE_PENALITE = {
        new SelectItem(PENALITE_BASE_MENSUALITE, PENALITE_BASE_MENSUALITE_NAME),
        new SelectItem(PENALITE_BASE_INTERET, PENALITE_BASE_INTERET_NAME),};

    public static final String BASE_TIERS_FOURNISSEUR = "FOURNISSEUR";
    public static final String BASE_TIERS_CLIENT = "CLIENT";
    public static final String BASE_TIERS_EMPLOYE = "EMPLOYE";
    public static final String BASE_TIERS_COMMERCIAL = "COMMERCIAL";
    public static final String BASE_TIERS_TIERS = "TIERS";

    public static final String FILE_TIERS_RCC = "RCC";
    public static final String FILE_TIERS_RCC_NAME = "Registre de commerce";
    public static final String FILE_TIERS_NUI = "NUI";
    public static final String FILE_TIERS_NUI_NAME = "Numero de contribuable";
    public static final String FILE_TIERS_RIB = "RIB";
    public static final String FILE_TIERS_RIB_NAME = "RIB (Releve Identite Bancaire)";

    public static final String SUPPL_SOCIETE_AGREEMENT = "AGREEMENT";
    public static final String SUPPL_SOCIETE_AGREEMENT_NAME = "Agréement";

    public String getNOTHING() {
        return NOTHING;
    }

    public String getSUPPL_SOCIETE_AGREEMENT() {
        return SUPPL_SOCIETE_AGREEMENT;
    }

    public String getSUPPL_SOCIETE_AGREEMENT_NAME() {
        return SUPPL_SOCIETE_AGREEMENT_NAME;
    }

    public String getFILE_TIERS_NUI() {
        return FILE_TIERS_NUI;
    }

    public String getFILE_TIERS_RCC() {
        return FILE_TIERS_RCC;
    }

    public String getFILE_TIERS_RCC_NAME() {
        return FILE_TIERS_RCC_NAME;
    }

    public String getFILE_TIERS_NUI_NAME() {
        return FILE_TIERS_NUI_NAME;
    }

    public String getFILE_TIERS_RIB() {
        return FILE_TIERS_RIB;
    }

    public String getFILE_TIERS_RIB_NAME() {
        return FILE_TIERS_RIB_NAME;
    }

    public String getDOC_MANAGE_ECART_RE() {
        return DOC_MANAGE_ECART_RE;
    }

    public String getDOC_MANAGE_ECART_CR() {
        return DOC_MANAGE_ECART_CR;
    }

    public String getDOC_MANAGE_ECART_RE_NAME() {
        return DOC_MANAGE_ECART_RE_NAME;
    }

    public String getDOC_MANAGE_ECART_CR_NAME() {
        return DOC_MANAGE_ECART_CR_NAME;
    }

    public String getSCR_ECART_VENTE_NAME() {
        return SCR_ECART_VENTE_NAME;
    }

    public String getSCR_ECART_VENTE() {
        return SCR_ECART_VENTE;
    }

    public String getSCR_ABONNEMENT_DIVERS_NAME() {
        return SCR_ABONNEMENT_DIVERS_NAME;
    }

    public String getSCR_ABONNEMENT_DIVERS() {
        return SCR_ABONNEMENT_DIVERS;
    }

    public String getSCR_AVOIR_VENTE_NAME() {
        return SCR_AVOIR_VENTE_NAME;
    }

    public String getSCR_AVOIR_VENTE() {
        return SCR_AVOIR_VENTE;
    }

    public String getSCR_AVOIR_ACHAT_NAME() {
        return SCR_AVOIR_ACHAT_NAME;
    }

    public String getSCR_AVOIR_ACHAT() {
        return SCR_AVOIR_ACHAT;
    }

    public String getBASE_TIERS_TIERS() {
        return BASE_TIERS_TIERS;
    }

    public String getBASE_TIERS_FOURNISSEUR() {
        return BASE_TIERS_FOURNISSEUR;
    }

    public String getBASE_TIERS_EMPLOYE() {
        return BASE_TIERS_EMPLOYE;
    }

    public String getBASE_TIERS_COMMERCIAL() {
        return BASE_TIERS_COMMERCIAL;
    }

    public String getBASE_TIERS_CLIENT() {
        return BASE_TIERS_CLIENT;
    }

    public String getCOUT_OD_NAME() {
        return COUT_OD_NAME;
    }

    public String getCOUT_OD() {
        return COUT_OD;
    }

    public String getCOUT_VIREMENT_NAME() {
        return COUT_VIREMENT_NAME;
    }

    public String getCOUT_VIREMENT() {
        return COUT_VIREMENT;
    }

    public char getSERVICE_RESTAURANT() {
        return SERVICE_RESTAURANT;
    }

    public char getSERVICE_LOGEMENT() {
        return SERVICE_LOGEMENT;
    }

    public char getSERVICE_COMMERCE_GENERAL() {
        return SERVICE_COMMERCE_GENERAL;
    }

    public String getSCR_BULLETIN() {
        return SCR_BULLETIN;
    }

    public String getSCR_CAISSE_CREDIT_VENTE_NAME() {
        return SCR_CAISSE_CREDIT_VENTE_NAME;
    }

    public String getSCR_CAISSE_CREDIT_VENTE() {
        return SCR_CAISSE_CREDIT_VENTE;
    }

    public String getSCR_CAISSE_CREDIT_ACHAT_NAME() {
        return SCR_CAISSE_CREDIT_ACHAT_NAME;
    }

    public String getSCR_CAISSE_CREDIT_ACHAT() {
        return SCR_CAISSE_CREDIT_ACHAT;
    }

    public String getSCR_PHASE_VIREMENT() {
        return SCR_PHASE_VIREMENT;
    }

    public String getSCR_PHASE_CAISSE_DIVERS() {
        return SCR_PHASE_CAISSE_DIVERS;
    }

    public String getSCR_PHASE_CAISSE_ACHAT() {
        return SCR_PHASE_CAISSE_ACHAT;
    }

    public String getSCR_PHASE_ACOMPTE_ACHAT() {
        return SCR_PHASE_ACOMPTE_ACHAT;
    }

    public String getSCR_PHASE_ACOMPTE_VENTE() {
        return SCR_PHASE_ACOMPTE_VENTE;
    }

    public String getSCR_PHASE_CREDIT_ACHAT() {
        return SCR_PHASE_CREDIT_ACHAT;
    }

    public String getSCR_PHASE_CREDIT_VENTE() {
        return SCR_PHASE_CREDIT_VENTE;
    }

    public boolean isUSE_PRETTY() {
        return USE_PRETTY;
    }

    public void setUSE_PRETTY(boolean USE_PRETTY) {
        Constantes.USE_PRETTY = USE_PRETTY;
    }

    public String getNAT_AUTRE() {
        return NAT_AUTRE;
    }

    public SelectItem[] getNATURES_COMPTE() {
        return NATURES_COMPTE;
    }

    public String getNAT_ABONNEMENT() {
        return NAT_ABONNEMENT;
    }

    public String getNAT_COMPENSATION() {
        return NAT_COMPENSATION;
    }

    public String getNAT_ARRONDI_PERTE() {
        return NAT_ARRONDI_PERTE;
    }

    public String getNAT_ARRONDI_GAIN() {
        return NAT_ARRONDI_GAIN;
    }

    public String getNAT_CREDIT_FOURNISSEUR() {
        return NAT_CREDIT_FOURNISSEUR;
    }

    public String getNAT_CREDIT_CLIENT() {
        return NAT_CREDIT_CLIENT;
    }

    public String getNAT_ACOMPTE_FOURNISSEUR() {
        return NAT_ACOMPTE_FOURNISSEUR;
    }

    public String getNAT_ACOMPTE_CLIENT() {
        return NAT_ACOMPTE_CLIENT;
    }

    public String getSCR_CREDIT_VENTE() {
        return SCR_CREDIT_VENTE;
    }

    public String getSCR_CREDIT_ACHAT() {
        return SCR_CREDIT_ACHAT;
    }

    public String getSCR_ACOMPTE_ACHAT() {
        return SCR_ACOMPTE_ACHAT;
    }

    public String getSCR_ACOMPTE_VENTE() {
        return SCR_ACOMPTE_VENTE;
    }

    public char getDIRECT() {
        return DIRECT;
    }

    public char getCALCULE() {
        return CALCULE;
    }

    public String getDIRECT_NAME() {
        return DIRECT_NAME;
    }

    public String getCALCULE_NAME() {
        return CALCULE_NAME;
    }

    public String getUNITE_OEUVRE() {
        return UNITE_OEUVRE;
    }

    public String getUNITE_OEUVRE_NAME() {
        return UNITE_OEUVRE_NAME;
    }

    public String getPRINCIPAL_NAME() {
        return PRINCIPAL_NAME;
    }

    public String getAUXILIAIRE_NAME() {
        return AUXILIAIRE_NAME;
    }

    public String getPRINCIPAL() {
        return PRINCIPAL;
    }

    public String getAUXILLIAIRE() {
        return AUXILLIAIRE;
    }

    public String getTYPE_PT_CREDIT_ACHAT() {
        return TYPE_PT_CREDIT_ACHAT;
    }

    public String getMODE_PAIEMENT_COMPENSATION() {
        return MODE_PAIEMENT_COMPENSATION;
    }

    public String getMODE_PAIEMENT_ACOMPTE() {
        return MODE_PAIEMENT_ACOMPTE;
    }

    public String getCOUT_MIXTE() {
        return COUT_MIXTE;
    }

    public String getCOUT_MIXTE_NAME() {
        return COUT_MIXTE_NAME;
    }

    public String getCOUT_VENTE() {
        return COUT_VENTE;
    }

    public String getCOUT_VENTE_NAME() {
        return COUT_VENTE_NAME;
    }

    public String getCOUT_ACHAT() {
        return COUT_ACHAT;
    }

    public String getCOUT_ACHAT_NAME() {
        return COUT_ACHAT_NAME;
    }

    public String getCOUT_MISSION() {
        return COUT_MISSION;
    }

    public String getCOUT_MISSION_NAME() {
        return COUT_MISSION_NAME;
    }

    public String getCOUT_FORMATION() {
        return COUT_FORMATION;
    }

    public String getCOUT_FORMATION_NAME() {
        return COUT_FORMATION_NAME;
    }

    public String getCOUT_TRANSFERT() {
        return COUT_TRANSFERT;
    }

    public String getCOUT_CREDIT_NAME() {
        return COUT_CREDIT_NAME;
    }

    public String getCOUT_CREDIT() {
        return COUT_CREDIT;
    }

    public String getCOUT_TRANSFERT_NAME() {
        return COUT_TRANSFERT_NAME;
    }

    public SelectItem[] getCOUTS() {
        return COUTS;
    }

    public String getCONVERTER_PRODUCTION() {
        return CONVERTER_PRODUCTION;
    }

    public String getCONVERTER_STOCK() {
        return CONVERTER_STOCK;
    }

    public String getCONVERTER_MONNAIE() {
        return CONVERTER_MONNAIE;
    }

    public String getCONVERTER_DN() {
        return CONVERTER_DN;
    }

    public String getCONVERTER_DN_NAME() {
        return CONVERTER_DN_NAME;
    }

    public String getCONVERTER_DNA() {
        return CONVERTER_DNA;
    }

    public String getCONVERTER_DNA_NAME() {
        return CONVERTER_DNA_NAME;
    }

    public String getCONVERTER_DNS() {
        return CONVERTER_DNS;
    }

    public String getCONVERTER_DNS_NAME() {
        return CONVERTER_DNS_NAME;
    }

    public SelectItem[] getCONVERTERS() {
        return CONVERTERS;
    }

    public String getTYPE_RA() {
        return TYPE_RA;
    }

    public String getTYPE_RA_NAME() {
        return TYPE_RA_NAME;
    }

    public String getEXPORT_SALAIRE() {
        return EXPORT_SALAIRE;
    }

    public String getEXPORT_SALAIRE_NAME() {
        return EXPORT_SALAIRE_NAME;
    }

    public char getBASE_COMMISSION_CA_HT() {
        return BASE_COMMISSION_CA_HT;
    }

    public char getBASE_COMMISSION_CA_TTC() {
        return BASE_COMMISSION_CA_TTC;
    }

    public char getBASE_COMMISSION_MARGE() {
        return BASE_COMMISSION_MARGE;
    }

    public String getBASE_COMMISSION_CA_HT_NAME() {
        return BASE_COMMISSION_CA_HT_NAME;
    }

    public String getBASE_COMMISSION_CA_TTC_NAME() {
        return BASE_COMMISSION_CA_TTC_NAME;
    }

    public String getBASE_COMMISSION_MARGE_NAME() {
        return BASE_COMMISSION_MARGE_NAME;
    }

    public SelectItem[] getBASES_COMMISSION() {
        return BASES_COMMISSION;
    }

    public char getCIBLE_TRANCHE_CA() {
        return CIBLE_TRANCHE_CA;
    }

    public char getCIBLE_TRANCHE_REMISE() {
        return CIBLE_TRANCHE_REMISE;
    }

    public char getCIBLE_TRANCHE_MARGE() {
        return CIBLE_TRANCHE_MARGE;
    }

    public String getCIBLE_TRANCHE_CA_NAME() {
        return CIBLE_TRANCHE_CA_NAME;
    }

    public String getCIBLE_TRANCHE_REMISE_NAME() {
        return CIBLE_TRANCHE_REMISE_NAME;
    }

    public String getCIBLE_TRANCHE_MARGE_NAME() {
        return CIBLE_TRANCHE_MARGE_NAME;
    }

    public SelectItem[] getCIBLES_TRANCHE() {
        return CIBLES_TRANCHE;
    }

    public char getAPPICATION_COMMISSION_FACTURE_ENCOURS() {
        return APPICATION_COMMISSION_FACTURE_ENCOURS;
    }

    public char getAPPICATION_COMMISSION_FACTURE_VALIDE() {
        return APPICATION_COMMISSION_FACTURE_VALIDE;
    }

    public char getAPPICATION_COMMISSION_FACTURE_REGLE() {
        return APPICATION_COMMISSION_FACTURE_REGLE;
    }

    public String getAPPICATION_COMMISSION_FACTURE_VALIDE_NAME() {
        return APPICATION_COMMISSION_FACTURE_VALIDE_NAME;
    }

    public String getAPPICATION_COMMISSION_FACTURE_ENCOURS_NAME() {
        return APPICATION_COMMISSION_FACTURE_ENCOURS_NAME;
    }

    public String getAPPICATION_COMMISSION_FACTURE_REGLE_NAME() {
        return APPICATION_COMMISSION_FACTURE_REGLE_NAME;
    }

    public SelectItem[] getAPPICATIONS_COMMISSION() {
        return APPICATIONS_COMMISSION;
    }

    public String getPERIODICITE_DETERMINER_NAME() {
        return PERIODICITE_DETERMINER_NAME;
    }

    public char getPERIODICITE_DETERMINER() {
        return PERIODICITE_DETERMINER;
    }

    public char getGRH_STATUT_TEMPORAIRE() {
        return GRH_STATUT_TEMPORAIRE;
    }

    public char getGRH_STATUT_PERMANANT() {
        return GRH_STATUT_PERMANANT;
    }

    public char getGRH_STATUT_STAGIAIRE() {
        return GRH_STATUT_STAGIAIRE;
    }

    public char getGRH_STATUT_TACHERON() {
        return GRH_STATUT_TACHERON;
    }

    public String getGRH_STATUT_LIB_TEMPORAIRE() {
        return GRH_STATUT_LIB_TEMPORAIRE;
    }

    public String getGRH_STATUT_LIB_PERMANANT() {
        return GRH_STATUT_LIB_PERMANANT;
    }

    public String getGRH_STATUT_LIB_STAGIAIRE() {
        return GRH_STATUT_LIB_STAGIAIRE;
    }

    public String getGRH_STATUT_LIB_TACHERON() {
        return GRH_STATUT_LIB_TACHERON;
    }

    public char getNATURE_POURCENTAGE() {
        return NATURE_POURCENTAGE;
    }

    public char getNATURE_MONTANT() {
        return NATURE_MONTANT;
    }

    public SelectItem[] getNATURES() {
        return NATURES;
    }

    public char getPERIODICITE_JUSQUA() {
        return PERIODICITE_JUSQUA;
    }

    public char getPERIODICITE_A_PARTIR() {
        return PERIODICITE_A_PARTIR;
    }

    public char getPERIODICITE_PERMANENT() {
        return PERIODICITE_PERMANENT;
    }

    public String getPERIODICITE_JUSQUA_NAME() {
        return PERIODICITE_JUSQUA_NAME;
    }

    public String getPERIODICITE_A_PARTIR_NAME() {
        return PERIODICITE_A_PARTIR_NAME;
    }

    public String getPERIODICITE_PERMANENT_NAME() {
        return PERIODICITE_PERMANENT_NAME;
    }

    public SelectItem[] getPERIODICITES() {
        return PERIODICITES;
    }

    public String getMUT_TRANSACTIONS_MUT() {
        return MUT_TRANSACTIONS_MUT;
    }

    public SelectItem[] getMODULES() {
        return MODULES;
    }

    public String getTYPE_MENSUALITE_ANNUITE_FIXE_NAME() {
        return TYPE_MENSUALITE_ANNUITE_FIXE_NAME;
    }

    public String getTYPE_MENSUALITE_AMORTISSEMENT_FIXE_NAME() {
        return TYPE_MENSUALITE_AMORTISSEMENT_FIXE_NAME;
    }

    public String getTYPE_MENSUALITE_INFINE_NAME() {
        return TYPE_MENSUALITE_INFINE_NAME;
    }

    public SelectItem[] getMUT_TYPE_MENSUALITE() {
        return MUT_TYPE_MENSUALITE;
    }

    public String getFORMULE_INTERET_SIMPLE_NAME() {
        return FORMULE_INTERET_SIMPLE_NAME;
    }

    public String getFORMULE_INTERET_COMPOSE_NAME() {
        return FORMULE_INTERET_COMPOSE_NAME;
    }

    public SelectItem[] getMUT_FORMULE_INTERET() {
        return MUT_FORMULE_INTERET;
    }

    public String getMODEL_REMBOURSEMENT_STRICT_NAME() {
        return MODEL_REMBOURSEMENT_STRICT_NAME;
    }

    public String getMODEL_REMBOURSEMENT_MODULABLE_NAME() {
        return MODEL_REMBOURSEMENT_MODULABLE_NAME;
    }

    public SelectItem[] getMUT_MODEL_REMBOURSEMENT() {
        return MUT_MODEL_REMBOURSEMENT;
    }

    public String getPENALITE_BASE_MENSUALITE_NAME() {
        return PENALITE_BASE_MENSUALITE_NAME;
    }

    public String getPENALITE_BASE_INTERET_NAME() {
        return PENALITE_BASE_INTERET_NAME;
    }

    public SelectItem[] getMUT_BASE_PENALITE() {
        return MUT_BASE_PENALITE;
    }

    public String getEXPORT_ARTICLE() {
        return EXPORT_ARTICLE;
    }

    public String getEXPORT_ARTICLE_NAME() {
        return EXPORT_ARTICLE_NAME;
    }

    public String getEXPORT_CLIENT() {
        return EXPORT_CLIENT;
    }

    public String getEXPORT_CLIENT_NAME() {
        return EXPORT_CLIENT_NAME;
    }

    public String getEXPORT_PLAN_COMPTABLE() {
        return EXPORT_PLAN_COMPTABLE;
    }

    public String getEXPORT_PLAN_COMPTABLE_NAME() {
        return EXPORT_PLAN_COMPTABLE_NAME;
    }

    public char getTYPE_MENSUALITE_ANNUITE_FIXE() {
        return TYPE_MENSUALITE_ANNUITE_FIXE;
    }

    public char getTYPE_MENSUALITE_AMORTISSEMENT_FIXE() {
        return TYPE_MENSUALITE_AMORTISSEMENT_FIXE;
    }

    public char getTYPE_MENSUALITE_INFINE() {
        return TYPE_MENSUALITE_INFINE;
    }

    public char getFORMULE_INTERET_SIMPLE() {
        return FORMULE_INTERET_SIMPLE;
    }

    public char getFORMULE_INTERET_COMPOSE() {
        return FORMULE_INTERET_COMPOSE;
    }

    public char getMODEL_REMBOURSEMENT_STRICT() {
        return MODEL_REMBOURSEMENT_STRICT;
    }

    public char getMODEL_REMBOURSEMENT_MODULABLE() {
        return MODEL_REMBOURSEMENT_MODULABLE;
    }

    public char getPENALITE_BASE_MENSUALITE() {
        return PENALITE_BASE_MENSUALITE;
    }

    public char getPENALITE_BASE_INTERET() {
        return PENALITE_BASE_INTERET;
    }

    public SelectItem[] getFILES_EXTENSION() {
        return FILES_EXTENSION;
    }

    public String getFILE_TEXT() {
        return FILE_TEXT;
    }

    public String getFILE_CSV() {
        return FILE_CSV;
    }

    public String getMUT_LIBELE_CONDITION_NBRE_AVALISE() {
        return MUT_LIBELE_CONDITION_NBRE_AVALISE;
    }

    public String getMUT_CODE_CONDITION_NBRE_AVALISE() {
        return MUT_CODE_CONDITION_NBRE_AVALISE;
    }

    public String getMUT_TYPE_COMPTE_COURANT() {
        return MUT_TYPE_COMPTE_COURANT;
    }

    public String getMUT_TYPE_COMPTE_A_TERME() {
        return MUT_TYPE_COMPTE_A_TERME;
    }

    public String getMUT_TYPE_COMPTE_ASSURANCE() {
        return MUT_TYPE_COMPTE_ASSURANCE;
    }

    public String getMUT_ACTIVITE_CREDIT() {
        return MUT_ACTIVITE_CREDIT;
    }

    public String getMUT_NATURE_OP_EPARGNE() {
        return MUT_NATURE_OP_EPARGNE;
    }

    public String getMUT_NATURE_OP_SALAIRE() {
        return MUT_NATURE_OP_SALAIRE;
    }

    public String getMUT_NATURE_OP_ASSISTANCE() {
        return MUT_NATURE_OP_ASSISTANCE;
    }

    public String getMUT_NATURE_OP_ASSURANCE() {
        return MUT_NATURE_OP_ASSURANCE;
    }

    public String getOP_RATIONS() {
        return OP_RATIONS;
    }

    public String getEXPORT_CONTENU_JOURNAL() {
        return EXPORT_CONTENU_JOURNAL;
    }

    public String getEXPORT_CONTENU_JOURNAL_NAME() {
        return EXPORT_CONTENU_JOURNAL_NAME;
    }

    public SelectItem[] getTYPE_ETAT_EXPORT() {
        return TYPE_ETAT_EXPORT;
    }

    public int[] getTabJour() {
        return tabJour;
    }

    public String getPAYS() {
        return PAYS;
    }

    public String getVILLES() {
        return VILLES;
    }

    public String getSECTEURS() {
        return SECTEURS;
    }

    public String getUNITE_AUTRE() {
        return UNITE_AUTRE;
    }

    public String getUNITE_AUTRE_NAME() {
        return UNITE_AUTRE_NAME;
    }

    public String getUNITE_VENTE() {
        return UNITE_VENTE;
    }

    public String getUNITE_VENTE_NAME() {
        return UNITE_VENTE_NAME;
    }

    public String getUNITE_ACHAT() {
        return UNITE_ACHAT;
    }

    public String getUNITE_ACHAT_NAME() {
        return UNITE_ACHAT_NAME;
    }

    public String getUNITE_STOCKAGE() {
        return UNITE_STOCKAGE;
    }

    public String getUNITE_STOCKAGE_NAME() {
        return UNITE_STOCKAGE_NAME;
    }

    public String getUNITE_TRANSPORT() {
        return UNITE_TRANSPORT;
    }

    public String getUNITE_TRANSPORT_NAME() {
        return UNITE_TRANSPORT_NAME;
    }

    public String getUNITE_TRANSFERT() {
        return UNITE_TRANSFERT;
    }

    public String getUNITE_TRANSFERT_NAME() {
        return UNITE_TRANSFERT_NAME;
    }

    public String getUNITE_ENTREE() {
        return UNITE_ENTREE;
    }

    public String getUNITE_ENTREE_NAME() {
        return UNITE_ENTREE_NAME;
    }

    public String getUNITE_SORTIE() {
        return UNITE_SORTIE;
    }

    public String getUNITE_SORTIE_NAME() {
        return UNITE_SORTIE_NAME;
    }

    public SelectItem[] getUNITES_CONDITIONNEMENT() {
        return UNITES_CONDITIONNEMENT;
    }

    public SelectItem[] getUNITES_MESURE() {
        return UNITES_MESURE;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public String getDOCUMENT_DOC_DIVERS_CAISSE() {
        return DOCUMENT_DOC_DIVERS_CAISSE;
    }

    public String getDOCUMENT_FACTURE_ACHAT() {
        return DOCUMENT_FACTURE_ACHAT;
    }

    public String getDOCUMENT_TRANSFERT() {
        return DOCUMENT_TRANSFERT;
    }

    public String getDOCUMENT_SORTIE() {
        return DOCUMENT_SORTIE;
    }

    public SelectItem[] getDOCUMENTS() {
        return DOCUMENTS;
    }

    public String getTYPE_RS() {
        return TYPE_RS;
    }

    public String getTYPE_RS_NAME() {
        return TYPE_RS_NAME;
    }

    public String getLUNDI() {
        return LUNDI;
    }

    public String getMARDI() {
        return MARDI;
    }

    public String getMERCREDI() {
        return MERCREDI;
    }

    public String getJEUDI() {
        return JEUDI;
    }

    public String getVENDREDI() {
        return VENDREDI;
    }

    public String getSAMEDI() {
        return SAMEDI;
    }

    public String getDIMANCHE() {
        return DIMANCHE;
    }

    public String getGRH_TYPE_CONGE_CT() {
        return GRH_TYPE_CONGE_CT;
    }

    public String getGRH_TYPE_CONGE_MALADI() {
        return GRH_TYPE_CONGE_MALADI;
    }

    public String getGRH_TYPE_CONGE_AUTRE() {
        return GRH_TYPE_CONGE_AUTRE;
    }

    public String getPOINTAGE_NB_FICHE() {
        return POINTAGE_NB_FICHE;
    }

    public String getDOCUMENT_FACTURE_VENTE() {
        return DOCUMENT_FACTURE_VENTE;
    }

    public String getMONNAIE_FCFA() {
        return MONNAIE_FCFA;
    }

    public String getMONNAIE_EURO() {
        return MONNAIE_EURO;
    }

    public String getMONNAIE_DOLLAR() {
        return MONNAIE_DOLLAR;
    }

    public String getMUT_LIBELLE_CONDITION_DUREE_EVALUATION() {
        return MUT_LIBELLE_CONDITION_DUREE_EVALUATION;
    }

    public String getMUT_LIBELLE_CAPACITE_ENDETTEMENT() {
        return MUT_LIBELLE_CAPACITE_ENDETTEMENT;
    }

    public String getMUT_LIBELLE_CONDITION_MONTANT_MAX() {
        return MUT_LIBELLE_CONDITION_MONTANT_MAX;
    }

    public String getMUT_LIBELLE_CONDITION_DUREE_CREDIT() {
        return MUT_LIBELLE_CONDITION_DUREE_CREDIT;
    }

    public String getMUT_LIBELLE_CONDITION_MONTANT_COUVERTURE() {
        return MUT_LIBELLE_CONDITION_MONTANT_COUVERTURE;
    }

    public String getMUT_LIBELLE_CONDITION_TAUX_MENSUEL() {
        return MUT_LIBELLE_CONDITION_TAUX_MENSUEL;
    }

    public String getMUT_LIBELE_CONDITION_TYPE_AVANCE() {
        return MUT_LIBELE_CONDITION_TYPE_AVANCE;
    }

    public String getMUT_CODE_CONDITION_DUREE_EVALUATION() {
        return MUT_CODE_CONDITION_DUREE_EVALUATION;
    }

    public String getMUT_CODE_CONDITION_CAPACITE_ENDETTEMENT() {
        return MUT_CODE_CONDITION_CAPACITE_ENDETTEMENT;
    }

    public String getMUT_CODE_CONDITION_MONTANT_MAX() {
        return MUT_CODE_CONDITION_MONTANT_MAX;
    }

    public String getMUT_CODE_CONDITION_DUREE_CREDIT() {
        return MUT_CODE_CONDITION_DUREE_CREDIT;
    }

    public String getMUT_CODE_CONDITION_MONTANT_COUVERTURE() {
        return MUT_CODE_CONDITION_MONTANT_COUVERTURE;
    }

    public String getMUT_LIBELLE_CONDITION_CAPACITE_REMBOURSEMENT() {
        return MUT_LIBELLE_CONDITION_CAPACITE_REMBOURSEMENT;
    }

    public String getMUT_CODE_CONDITION_CAPACITE_REMBOURSEMENT() {
        return MUT_CODE_CONDITION_CAPACITE_REMBOURSEMENT;
    }

    public String getMUT_CODE_CONDITION_TAUX_MENSUEL() {
        return MUT_CODE_CONDITION_TAUX_MENSUEL;
    }

    public String getMUT_CODE_CONDITION_TYPE_AVANCE() {
        return MUT_CODE_CONDITION_TYPE_AVANCE;
    }

    public String getMUT_BASE_FORCAGE_AMORTISSEMENT() {
        return MUT_BASE_FORCAGE_AMORTISSEMENT;
    }

    public String getMUT_BASE_FORCAGE_INTERET() {
        return MUT_BASE_FORCAGE_INTERET;
    }

    public String getMUT_ETAT_CREDIT_INCOMPLET() {
        return MUT_ETAT_CREDIT_INCOMPLET;
    }

    public String getMUT_ETAT_CREDIT_TRANSFERE() {
        return MUT_ETAT_CREDIT_TRANSFERE;
    }

    public String getMUT_TYPE_COMPTE_EPARGNE() {
        return MUT_TYPE_COMPTE_EPARGNE;
    }

    public String getMUT_SENS_OP_DEPOT() {
        return MUT_SENS_OP_DEPOT;
    }

    public String getMUT_SENS_OP_RETRAIT() {
        return MUT_SENS_OP_RETRAIT;
    }

    public String getMUT_TYPE_MONTANT_FIXE() {
        return MUT_TYPE_MONTANT_FIXE;
    }

    public String getMUT_TYPE_MONTANT_POURCENTAGE() {
        return MUT_TYPE_MONTANT_POURCENTAGE;
    }

    public String getMUT_TYPE_REPORT_PARTIEL() {
        return MUT_TYPE_REPORT_PARTIEL;
    }

    public String getMUT_TYPE_REPORT_TOTAL() {
        return MUT_TYPE_REPORT_TOTAL;
    }

    public String getMUT_PERIODE_MOIS() {
        return MUT_PERIODE_MOIS;
    }

    public String getMUT_PERIODE_TRIMESTRES() {
        return MUT_PERIODE_TRIMESTRES;
    }

    public String getMUT_PERIODE_SEMESTRES() {
        return MUT_PERIODE_SEMESTRES;
    }

    public String getMUT_PERIODE_ANNEE() {
        return MUT_PERIODE_ANNEE;
    }

    public String getMUT_TABLE_SRC_OPERATION_COMPTE() {
        return MUT_TABLE_SRC_OPERATION_COMPTE;
    }

    public String getMUT_TABLE_SRC_OPERATION_CAISSE() {
        return MUT_TABLE_SRC_OPERATION_CAISSE;
    }

    public String getMUT_TABLE_SRC_REGLEMENT_MENSUALITE() {
        return MUT_TABLE_SRC_REGLEMENT_MENSUALITE;
    }

    public String getMODE_PAIEMENT_ESPECE() {
        return MODE_PAIEMENT_ESPECE;
    }

    public String getMODE_PAIEMENT_SALAIRE() {
        return MODE_PAIEMENT_SALAIRE;
    }

    public String getMODE_PAIEMENT_BANQUE() {
        return MODE_PAIEMENT_BANQUE;
    }

    public String getCODE_PAIEMENT_ORANGE_MONEY() {
        return CODE_PAIEMENT_ORANGE_MONEY;
    }

    public String getCODE_PAIEMENT_MOBILE_MONEY() {
        return CODE_PAIEMENT_MOBILE_MONEY;
    }

    public String getCODE_PAIEMENT_VISA() {
        return CODE_PAIEMENT_VISA;
    }

    public String getCODE_PAIEMENT_AUTRE() {
        return CODE_PAIEMENT_AUTRE;
    }

    public String getMUT_ACTION_TRANSFERE() {
        return MUT_ACTION_TRANSFERE;
    }

    public String getMUT_ACTION_REPORT() {
        return MUT_ACTION_REPORT;
    }

    public String getMUT_ACTION_RIEN() {
        return MUT_ACTION_RIEN;
    }

    public String getMUT_ACTION_CHOIX() {
        return MUT_ACTION_CHOIX;
    }

    public String getETAT_COMPOSANT_OF_0() {
        return ETAT_COMPOSANT_OF_0;
    }

    public String getETAT_COMPOSANT_OF_1() {
        return ETAT_COMPOSANT_OF_1;
    }

    public String getOF_GENERATEUR_SUGGERE() {
        return OF_GENERATEUR_SUGGERE;
    }

    public String getOF_GENERATEUR_COMMANDE() {
        return OF_GENERATEUR_COMMANDE;
    }

    public String getOF_GENERATEUR_BESOIN_INTERNE() {
        return OF_GENERATEUR_BESOIN_INTERNE;
    }

    public String getPDP_TYPE_BB() {
        return PDP_TYPE_BB;
    }

    public String getPDP_TYPE_SD() {
        return PDP_TYPE_SD;
    }

    public String getPDP_TYPE_RA() {
        return PDP_TYPE_RA;
    }

    public String getPDP_TYPE_BN() {
        return PDP_TYPE_BN;
    }

    public String getPDP_TYPE_OPF() {
        return PDP_TYPE_OPF;
    }

    public String getPDP_TYPE_OPD() {
        return PDP_TYPE_OPD;
    }

    public String getPDP_TYPE_TM() {
        return PDP_TYPE_TM;
    }

    public String getOF_TYPE_GENERATION_AUTO() {
        return OF_TYPE_GENERATION_AUTO;
    }

    public String getOF_TYPE_GENERATION_MANUEL() {
        return OF_TYPE_GENERATION_MANUEL;
    }

    public String getPROD_VENTE() {
        return PROD_VENTE;
    }

    public String getPROD_STOCK() {
        return PROD_STOCK;
    }

    public String getPROD_PRODUCTION() {
        return PROD_PRODUCTION;
    }

    public String getUNITE_SEMESTRE_() {
        return UNITE_SEMESTRE_;
    }

    public String getUNITE_SEMESTRE() {
        return UNITE_SEMESTRE;
    }

    public String getUNITE_SEMESTRE_m() {
        return UNITE_SEMESTRE_m;
    }

    public String getUNITE_TRIMESTRE_() {
        return UNITE_TRIMESTRE_;
    }

    public String getUNITE_TRIMESTRE() {
        return UNITE_TRIMESTRE;
    }

    public String getUNITE_TRIMESTRE_m() {
        return UNITE_TRIMESTRE_m;
    }

    public String getTYPE_PIECE_COMPTABLE_NAME() {
        return TYPE_PIECE_COMPTABLE_NAME;
    }

    public String getTYPE_PT_AVANCE_VENTE() {
        return TYPE_PT_AVANCE_VENTE;
    }

    public String getTYPE_PT_AVANCE_ACHAT() {
        return TYPE_PT_AVANCE_ACHAT;
    }

    public String getTYPE_DOC_CAISSE_CHARGE_TIERS() {
        return TYPE_DOC_CAISSE_CHARGE_TIERS;
    }

    public String getTYPE_DOC_CAISSE_CHARGE() {
        return TYPE_DOC_CAISSE_CHARGE;
    }

    public String getTYPE_DOC_CAISSE_RECETTE() {
        return TYPE_DOC_CAISSE_RECETTE;
    }

    public String getTYPE_DOC_CAISSE_RECETTE_TIERS() {
        return TYPE_DOC_CAISSE_RECETTE_TIERS;
    }

    public String getTYPE_DOC_MISSION_NAME() {
        return TYPE_DOC_MISSION_NAME;
    }

    public String getSCR_HEAD_VENTE() {
        return SCR_HEAD_VENTE;
    }

    public String getSCR_HEAD_VENTE_NAME() {
        return SCR_HEAD_VENTE_NAME;
    }

    public String getSCR_SALAIRE() {
        return SCR_SALAIRE;
    }

    public String getSCR_SALAIRE_NAME() {
        return SCR_SALAIRE_NAME;
    }

    public String getSCR_MISSIONS() {
        return SCR_MISSIONS;
    }

    public String getSCR_VIREMENT() {
        return SCR_VIREMENT;
    }

    public String getSCR_VIREMENT_NAME() {
        return SCR_VIREMENT_NAME;
    }

    public String getSCR_NOTE_FRAIS() {
        return SCR_NOTE_FRAIS;
    }

    public String getSCR_NOTE_FRAIS_NAME() {
        return SCR_NOTE_FRAIS_NAME;
    }

    public String getSCR_CAISSE_ACHAT() {
        return SCR_CAISSE_ACHAT;
    }

    public String getSCR_CAISSE_ACHAT_NAME() {
        return SCR_CAISSE_ACHAT_NAME;
    }

    public String getSCR_CAISSE_DIVERS() {
        return SCR_CAISSE_DIVERS;
    }

    public String getSCR_CAISSE_DIVERS_NAME() {
        return SCR_CAISSE_DIVERS_NAME;
    }

    public String getSCR_CAISSE_VENTE() {
        return SCR_CAISSE_VENTE;
    }

    public String getSCR_CAISSE_VENTE_NAME() {
        return SCR_CAISSE_VENTE_NAME;
    }

    public String getSCR_AUTRES() {
        return SCR_AUTRES;
    }

    public String getSCR_FRAIS_MISSIONS() {
        return SCR_FRAIS_MISSIONS;
    }

    public String getSCR_FRAIS_MISSIONS_NAME() {
        return SCR_FRAIS_MISSIONS_NAME;
    }

    public String getSCR_TRESORERIE() {
        return SCR_TRESORERIE;
    }

    public static String message = "";

    public String getUNITE_QUANTITE() {
        return UNITE_QUANTITE;
    }

    public String getUNITE_QUANTITE_NAME() {
        return UNITE_QUANTITE_NAME;
    }

    public String getUNITE_TEMPS() {
        return UNITE_TEMPS;
    }

    public String getUNITE_TEMPS_NAME() {
        return UNITE_TEMPS_NAME;
    }

    public String getDOCUMENT_PC_MISSION() {
        return DOCUMENT_PC_MISSION;
    }

//    public void setDOCUMENT_PC_MISSION(String DOCUMENT_PC_MISSION) {
//        Constantes.DOCUMENT_PC_MISSION = DOCUMENT_PC_MISSION;
//    }
//
//    public String getDOCUMENT_DOC_DIVERS_CAISSE() {
//        return DOCUMENT_DOC_DIVERS_CAISSE;
//    }
//
//    public void setDOCUMENT_DOC_DIVERS_CAISSE(String DOCUMENT_DOC_DIVERS_CAISSE) {
//        Constantes.DOCUMENT_DOC_DIVERS_CAISSE = DOCUMENT_DOC_DIVERS_CAISSE;
//    }
//
//    public String getDOCUMENT_FACTURE_ACHAT() {
//        return DOCUMENT_FACTURE_ACHAT;
//    }
//
//    public void setDOCUMENT_FACTURE_ACHAT(String DOCUMENT_FACTURE_ACHAT) {
//        Constantes.DOCUMENT_FACTURE_ACHAT = DOCUMENT_FACTURE_ACHAT;
//    }
    public char getSTATUT_DOC_SOUMIS() {
        return STATUT_DOC_SOUMIS;
    }

    public char getSTATUT_DOC_RENVOYE() {
        return STATUT_DOC_RENVOYE;
    }

    public char getSTATUT_DOC_ATTENTE() {
        return STATUT_DOC_ATTENTE;
    }

    public char getSTATUT_PROD_LANCE() {
        return STATUT_PROD_LANCE;
    }

    public char getSTATUT_DOC_ENCOUR() {
        return STATUT_DOC_ENCOUR;
    }

    public char getSTATUT_DOC_VALIDE() {
        return STATUT_DOC_VALIDE;
    }

    public char getSTATUT_DOC_JUSTIFIER() {
        return STATUT_DOC_JUSTIFIER;
    }

    public char getSTATUT_DOC_INJUSTIFIE() {
        return STATUT_DOC_INJUSTIFIE;
    }

    public String getSCR_BON_PROVISOIRE_NAME() {
        return SCR_BON_PROVISOIRE_NAME;
    }

    public String getSCR_BON_PROVISOIRE() {
        return SCR_BON_PROVISOIRE;
    }

    public char getSTATUT_DOC_SUSPENDU() {
        return STATUT_DOC_SUSPENDU;
    }

    public char getSTATUT_DOC_ANNULE() {
        return STATUT_DOC_ANNULE;
    }

    public char getSTATUT_DOC_TERMINE() {
        return STATUT_DOC_TERMINE;
    }

    public char getSTATUT_DOC_CLOTURE() {
        return STATUT_DOC_CLOTURE;
    }

    public char getSTATUT_DOC_PAYER() {
        return STATUT_DOC_PAYER;
    }

    public char getSTATUT_DOC_LIVRER() {
        return STATUT_DOC_LIVRER;
    }

    public char getSTATUT_DOC_EDITABLE() {
        return STATUT_DOC_EDITABLE;
    }

    public String getMOUV_ENTREE() {
        return MOUV_ENTREE;
    }

    public String getMOUV_CREDIT() {
        return MOUV_CREDIT;
    }

    public String getMOUV_DEBIT() {
        return MOUV_DEBIT;
    }

    public String getMOUV_SORTIE() {
        return MOUV_SORTIE;
    }

    public String getSTOCK() {
        return STOCK;
    }

    public String getVSTOCK() {
        return VSTOCK;
    }

    public List<String[]> getUNITES_TIMES_m() {
        return UNITES_TIMES_m;
    }

    public String getUNITE_TIERCE_m() {
        return UNITE_TIERCE_m;
    }

    public String getUNITE_SECONDE_m() {
        return UNITE_SECONDE_m;
    }

    public String getUNITE_MINUTE_m() {
        return UNITE_MINUTE_m;
    }

    public String getUNITE_HEURE_m() {
        return UNITE_HEURE_m;
    }

    public String getUNITE_JOUR_m() {
        return UNITE_JOUR_m;
    }

    public String getUNITE_SEMAINE_m() {
        return UNITE_SEMAINE_m;
    }

    public String getUNITE_ANNEE_m() {
        return UNITE_ANNEE_m;
    }

    public Options[] getUNITE_MASSES() {
        return UNITE_MASSES;
    }

    public String getDEFAUT() {
        return DEFAUT;
    }

    public String getETAT_CLOTURE() {
        return ETAT_CLOTURE;
    }

    public String getETAT_TERMINE() {
        return ETAT_TERMINE;
    }

    public String getETAT_SUSPENDU() {
        return ETAT_SUSPENDU;
    }

    public String getETAT_RENVOYE() {
        return ETAT_RENVOYE;
    }

    public String getETAT_INCOMPLET() {
        return ETAT_INCOMPLET;
    }

    public String getETAT_ATTENTE() {
        return ETAT_ATTENTE;
    }

    public double getMAX_DOUBLE() {
        return MAX_DOUBLE;
    }

    public void setMAX_DOUBLE(double MAX_DOUBLE) {
        Constantes.MAX_DOUBLE = MAX_DOUBLE;
    }

    public String getDOCUMENT_PERMISSION_COURTE_DUREE() {
        return DOCUMENT_PERMISSION_COURTE_DUREE;
    }

//    public void setDOCUMENT_PERMISSION_COURTE_DUREE(String DOCUMENT_PERMISSION_COURTE_DUREE) {
//        Constantes.DOCUMENT_PERMISSION_COURTE_DUREE = DOCUMENT_PERMISSION_COURTE_DUREE;
//    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        Constantes.message = message;
    }

    public String getGR() {
        return GR;
    }

    public String getKG() {
        return KG;
    }

    public String getMG() {
        return MG;
    }

    public String getEN() {
        return EN;
    }

    public String getFR() {
        return FR;
    }

    public String getMODE_CONSO_MASSIQUE() {
        return MODE_CONSO_MASSIQUE;
    }

    public String getMODE_CONSO_UNITAIRE() {
        return MODE_CONSO_UNITAIRE;
    }

    public boolean isNORME_FIXE() {
        return NORME_FIXE;
    }

    public boolean isNORME_VARIABLE() {
        return NORME_VARIABLE;
    }

    public String getTIERS_CLIENT() {
        return TIERS_CLIENT;
    }

    public String getTIERS_FOURNISSEUR() {
        return TIERS_FOURNISSEUR;
    }

    public String getTIERS_REPRESENTANT() {
        return TIERS_REPRESENTANT;
    }

    public String getTIERS_EMPLOYE() {
        return TIERS_EMPLOYE;
    }

    public String getGRH_TYPE_CONGE_ANNUELLE() {
        return GRH_TYPE_CONGE_ANNUELLE;
    }

    public String getDOCUMENT_CONGES() {
        return DOCUMENT_CONGES;
    }

    public String getDOCUMENT_FORMATION() {
        return DOCUMENT_FORMATION;
    }

    public String getDOCUMENT_MISSION() {
        return DOCUMENT_MISSION;
    }

    public String getCRITERE_MAJORATION_CONGE_ANC() {
        return CRITERE_MAJORATION_CONGE_ANC;
    }

    public void setCRITERE_MAJORATION_CONGE_ANC(String CRITERE_MAJORATION_CONGE_ANC) {
        Constantes.CRITERE_MAJORATION_CONGE_ANC = CRITERE_MAJORATION_CONGE_ANC;
    }

    public String getCRITERE_MAJORATION_CONGE_DAME() {
        return CRITERE_MAJORATION_CONGE_DAME;
    }

    public void setCRITERE_MAJORATION_CONGE_DAME(String CRITERE_MAJORATION_CONGE_DAME) {
        Constantes.CRITERE_MAJORATION_CONGE_DAME = CRITERE_MAJORATION_CONGE_DAME;
    }

    public String[] getTabMois() {
        return tabMois;
    }

    public String getFIFO() {
        return FIFO;
    }

    public String getLIFO() {
        return LIFO;
    }

    public String getCMP() {
        return CMP;
    }

    public String getCMP1() {
        return CMP1;
    }

    public String getCMP2() {
        return CMP2;
    }

    public String getCMPU1() {
        return CMPU1;
    }

    public String getTRESORERIE() {
        return TRESORERIE;
    }

    public String getVIREMENT() {
        return VIREMENT;
    }

    public String getGENERAL() {
        return GENERAL;
    }

    public String getCMPU2() {
        return CMPU2;
    }

    public String getFIFO_NAME() {
        return FIFO_NAME;
    }

    public String getLIFO_NAME() {
        return LIFO_NAME;
    }

    public String getCMP1_NAME() {
        return CMP1_NAME;
    }

    public String getCMP2_NAME() {
        return CMP2_NAME;
    }

    public String getT_PAYS() {
        return T_PAYS;
    }

    public String getT_VILLES() {
        return T_VILLES;
    }

    public String getT_SECTEURS() {
        return T_SECTEURS;
    }

    public int getENV_MAJORITE() {
        return ENV_MAJORITE;
    }

    public long getHOUR() {
        return HOUR;
    }

    public String getEQUILIBRE() {
        return EQUILIBRE;
    }

    public String getPOURCENTAGE() {
        return POURCENTAGE;
    }

    public String getCAT_MARCHANDISE() {
        return CAT_MARCHANDISE;
    }

    public String getCAT_SERVICE() {
        return CAT_SERVICE;
    }

    public String getCAT_PSF() {
        return CAT_PSF;
    }

    public String getCAT_MP() {
        return CAT_MP;
    }

    public String getCAT_PF() {
        return CAT_PF;
    }

    public String getCAT_PF_NAME() {
        return CAT_PF_NAME;
    }

    public String getCAT_FOURNITURE() {
        return CAT_FOURNITURE;
    }

    public String getPIECE_DE_RECHANGE() {
        return PIECE_DE_RECHANGE;
    }

    public String getCAT_EMBALLAGE() {
        return CAT_EMBALLAGE;
    }

    public String getDUREE_PENDANT() {
        return DUREE_PENDANT;
    }

    public String getDUREE_JUSQUA() {
        return DUREE_JUSQUA;
    }

    public String getDUREE_LE() {
        return DUREE_LE;
    }

    public String getOBJECTIF_QUANTITE() {
        return OBJECTIF_QUANTITE;
    }

    public String getOBJECTIF_VALEUR() {
        return OBJECTIF_VALEUR;
    }

    public String getBASE_CATTC() {
        return BASE_CATTC;
    }

    public String getBASE_CAHT() {
        return BASE_CAHT;
    }

    public String getBASE_MARGE() {
        return BASE_MARGE;
    }

    public String getDUREE_A_PARTIR() {
        return DUREE_A_PARTIR;
    }

    public String getDUREE_PERMANENCE() {
        return DUREE_PERMANENCE;
    }

    public String getNAT_COMISSION() {
        return NAT_COMISSION;
    }

    public String getNAT_RISTOURNE() {
        return NAT_RISTOURNE;
    }

    public String getAPPRO_ACHTON() {
        return APPRO_ACHTON;
    }

    public String getAPPRO_ENON() {
        return APPRO_ENON;
    }

    public String getAPPRO_PRODON() {
        return APPRO_PRODON;
    }

    public String getAPPRO_ACHT_PROD() {
        return APPRO_ACHT_PROD;
    }

    public String getAPPRO_ACHT_EN() {
        return APPRO_ACHT_EN;
    }

    public String getAPPRO_PROD_EN() {
        return APPRO_PROD_EN;
    }

    public String getAPPRO_ACHT_PROD_EN() {
        return APPRO_ACHT_PROD_EN;
    }

    public String getREAPPRO_SEUIL() {
        return REAPPRO_SEUIL;
    }

    public String getREAPPRO_AUCUN() {
        return REAPPRO_AUCUN;
    }

    public String getREAPPRO_PERIODE() {
        return REAPPRO_PERIODE;
    }

    public String getREAPPRO_RUPTURE() {
        return REAPPRO_RUPTURE;
    }

    public String getTRANSPORT() {
        return TRANSPORT;
    }

    public String getASSURANCE() {
        return ASSURANCE;
    }

    public String getCOMMISSION() {
        return COMMISSION;
    }

    public String getMANUTENTION() {
        return MANUTENTION;
    }

    public String getAUTRES() {
        return AUTRES;
    }

    public String getGRH_MERE() {
        return GRH_MERE;
    }

    public String getGRH_ANCIENNETE() {
        return GRH_ANCIENNETE;
    }

    public String getGRH_EN_COMMISSION() {
        return GRH_EN_COMMISSION;
    }

    public String getGRH_EN_PAUSE() {
        return GRH_EN_PAUSE;
    }

    public String getGRH_EN_SERVICE() {
        return GRH_EN_SERVICE;
    }

    public String getGRH_ABSENT() {
        return GRH_ABSENT;
    }

    public String getGRH_PERMISSION_SUR_CONGE() {
        return GRH_PERMISSION_SUR_CONGE;
    }

    public String getGRH_PERMISSION_SUR_SALAIRE() {
        return GRH_PERMISSION_SUR_SALAIRE;
    }

    public String getGRH_PERMISSION_AUTORISE() {
        return GRH_PERMISSION_AUTORISE;
    }

    public String getGRH_PERMISSION_SPECIALE() {
        return GRH_PERMISSION_SPECIALE;
    }

    public String getS_SALAIRE_CONVENTIONNELLE() {
        return S_SALAIRE_CONVENTIONNELLE;
    }

    public String getS_SALAIRE_CONTRACTUELLE() {
        return S_SALAIRE_CONTRACTUELLE;
    }

    public String getS_SALAIRE_SUR_OBJETIF() {
        return S_SALAIRE_SUR_OBJETIF;
    }

    public String getS_SALAIRE_HORAIRE() {
        return S_SALAIRE_HORAIRE;
    }

    public String getS_ENFANT() {
        return S_ENFANT;
    }

    public String getS_JOUR_DE_CONGE() {
        return S_JOUR_DE_CONGE;
    }

    public String getS_HEURE_SUPPLEMENTAIRE() {
        return S_HEURE_SUPPLEMENTAIRE;
    }

    public String getS_HEURE_JOUR_FERIE() {
        return S_HEURE_JOUR_FERIE;
    }

    public String getS_HEURE_DIMANCHE() {
        return S_HEURE_DIMANCHE;
    }

    public String getS_ANCIENETE() {
        return S_ANCIENETE;
    }

    public String getS_NB_HEURE_REQUISE() {
        return S_NB_HEURE_REQUISE;
    }

    public String getS_NB_JOUR_REQUIS() {
        return S_NB_JOUR_REQUIS;
    }

    public String getS_NB_HEURE_EFFECTIF() {
        return S_NB_HEURE_EFFECTIF;
    }

    public String getS_NB_JOUR_EFFECTIF() {
        return S_NB_JOUR_EFFECTIF;
    }

    public String getS_NB_JOUR_PASSE() {
        return S_NB_JOUR_PASSE;
    }

    public String getS_NB_JOUR_CONGE_TECHNIQUE() {
        return S_NB_JOUR_CONGE_TECHNIQUE;
    }

    public String getS_NOTE_DE_FRAIS() {
        return S_NOTE_DE_FRAIS;
    }

    public String getS_DUREE_PREAVIS() {
        return S_DUREE_PREAVIS;
    }

    public String getS_NB_MOIS_ACCEPTE_IN_EXO() {
        return S_NB_MOIS_ACCEPTE_IN_EXO;
    }

    public String getS_NB_JOUR_ACCEPTE_IN_EXO() {
        return S_NB_JOUR_ACCEPTE_IN_EXO;
    }

    public String getS_BASE_DUREE_CONGE() {
        return S_BASE_DUREE_CONGE;
    }

    public String getGRH_DUREE_CONGE_PRINCIPAL() {
        return GRH_DUREE_CONGE_PRINCIPAL;
    }

    public String getGRH_DUREE_CONGE_SUPPLEMENTAIRE() {
        return GRH_DUREE_CONGE_SUPPLEMENTAIRE;
    }

    public String getGRH_DUREE_CONGE_MOIS_REFERENCE() {
        return GRH_DUREE_CONGE_MOIS_REFERENCE;
    }

    public String getGRH_DUREE_CONGE_NB_JOUR_IN_PERIODE_REF() {
        return GRH_DUREE_CONGE_NB_JOUR_IN_PERIODE_REF;
    }

    public String getGRH_DUREE_CONGE_NB_JOUR_REF() {
        return GRH_DUREE_CONGE_NB_JOUR_REF;
    }

    public String getUNITE_DUREE_PREAVIS_MOIS() {
        return UNITE_DUREE_PREAVIS_MOIS;
    }

    public String getUNITE_DUREE_PREAVIS_JOUR() {
        return UNITE_DUREE_PREAVIS_JOUR;
    }

    public String getRUBRIQUE_INDEMNITE_PREAVIS() {
        return RUBRIQUE_INDEMNITE_PREAVIS;
    }

    public String getRUBRIQUE_INDEMNITE_LICENCIEMENT() {
        return RUBRIQUE_INDEMNITE_LICENCIEMENT;
    }

    public String getRUBRIQUE_INDEMNITE_CONGE() {
        return RUBRIQUE_INDEMNITE_CONGE;
    }

    public String getRUBRIQUE_RELIQUAT_SALAIRE() {
        return RUBRIQUE_RELIQUAT_SALAIRE;
    }

    public String getRUBRIQUE_AUTRES() {
        return RUBRIQUE_AUTRES;
    }

    public String getBASE_CA() {
        return BASE_CA;
    }

    public String getBASE_QTE() {
        return BASE_QTE;
    }

    public String getNATURE_TAUX() {
        return NATURE_TAUX;
    }

    public String getNATURE_MTANT() {
        return NATURE_MTANT;
    }

    public String getAUTRE() {
        return AUTRE;
    }

    public String getSALAIRE() {
        return SALAIRE;
    }

    public String getACHAT() {
        return ACHAT;
    }

    public String getSERVICE() {
        return SERVICE;
    }

    public String getVENTE() {
        return VENTE;
    }

    public String getPOSS_YES() {
        return POSS_YES;
    }

    public String getPOSS_YES_NO() {
        return POSS_YES_NO;
    }

    public String getPOSS_NO() {
        return POSS_NO;
    }

    public String getETAT_EDITABLE() {
        return ETAT_EDITABLE;
    }

    public String getETAT_SOUMIS() {
        return ETAT_SOUMIS;
    }

    public String getETAT_REGLE() {
        return ETAT_REGLE;
    }

    public String getETAT_VALIDE() {
        return ETAT_VALIDE;
    }

    public String getETAT_ANNULE() {
        return ETAT_ANNULE;
    }

    public String getETAT_RELANCE() {
        return ETAT_RELANCE;
    }

    public String getETAT_LIVRE() {
        return ETAT_LIVRE;
    }

    public String getETAT_ENCOURS() {
        return ETAT_ENCOURS;
    }

    public String getETAT_JUSTIFIE() {
        return ETAT_JUSTIFIE;
    }

    public String getETAT_INJUSTIFIE() {
        return ETAT_INJUSTIFIE;
    }

    public SelectItem[] getETATS_V() {
        return ETATS_V;
    }

    public SelectItem[] getETATS() {
        return ETATS;
    }

    public SelectItem[] getETATS_() {
        return ETATS_;
    }

    public SelectItem[] getETATS_T() {
        return ETATS_T;
    }

    public SelectItem[] getETATS_ES() {
        return ETATS_ES;
    }

    public List<String[]> getUNITES_TIMES_ALL() {
        return UNITES_TIMES_ALL;
    }

    public List<String[]> getUNITES_TIMES() {
        return UNITES_TIMES;
    }

    public SelectItem[] getINDICATEURS_QUANTITATIF() {
        return INDICATEURS_QUANTITATIF;
    }

    public static String getINDIC_CA() {
        return INDIC_CA;
    }

    public static String getINDIC_MARGE() {
        return INDIC_MARGE;
    }

    public static String getINDIC_QUANTITE() {
        return INDIC_QUANTITE;
    }

    public static String getINDIC_NB_CLIENT() {
        return INDIC_NB_CLIENT;
    }

    public static String getINDIC_CREANCE() {
        return INDIC_CREANCE;
    }

    public String getUNITE_TIERCE_() {
        return UNITE_TIERCE_;
    }

    public String getUNITE_TIERCE() {
        return UNITE_TIERCE;
    }

    public String getUNITE_SECONDE_() {
        return UNITE_SECONDE_;
    }

    public String getUNITE_SECONDE() {
        return UNITE_SECONDE;
    }

    public String getUNITE_MINUTE_() {
        return UNITE_MINUTE_;
    }

    public String getUNITE_MINUTE() {
        return UNITE_MINUTE;
    }

    public String getUNITE_HEURE_() {
        return UNITE_HEURE_;
    }

    public String getUNITE_HEURE() {
        return UNITE_HEURE;
    }

    public String getUNITE_JOUR_() {
        return UNITE_JOUR_;
    }

    public String getUNITE_JOUR() {
        return UNITE_JOUR;
    }

    public String getUNITE_SEMAINE_() {
        return UNITE_SEMAINE_;
    }

    public String getUNITE_SEMAINE() {
        return UNITE_SEMAINE;
    }

    public String getUNITE_MOIS_() {
        return UNITE_MOIS_;
    }

    public String getUNITE_MOIS() {
        return UNITE_MOIS;
    }

    public String getUNITE_ANNEE_() {
        return UNITE_ANNEE_;
    }

    public String getUNITE_ANNEE() {
        return UNITE_ANNEE;
    }

    public String getTYPE_FiA() {
        return TYPE_FiA;
    }

    public String getTYPE_FiA_NAME() {
        return TYPE_FiA_NAME;
    }

    public String getTYPE_FA() {
        return TYPE_FA;
    }

    public String getTYPE_FA_NAME() {
        return TYPE_FA_NAME;
    }

    public String getTYPE_FRA() {
        return TYPE_FRA;
    }

    public String getTYPE_FRA_NAME() {
        return TYPE_FRA_NAME;
    }

    public String getTYPE_FAA() {
        return TYPE_FAA;
    }

    public String getTYPE_FAA_NAME() {
        return TYPE_FAA_NAME;
    }

    public String getTYPE_BCA() {
        return TYPE_BCA;
    }

    public String getTYPE_BCA_NAME() {
        return TYPE_BCA_NAME;
    }

    public String getTYPE_BLA() {
        return TYPE_BLA;
    }

    public String getTYPE_BLA_NAME() {
        return TYPE_BLA_NAME;
    }

    public String getTYPE_BRA() {
        return TYPE_BRA;
    }

    public String getTYPE_BRA_NAME() {
        return TYPE_BRA_NAME;
    }

    public String getTYPE_BAA() {
        return TYPE_BAA;
    }

    public String getTYPE_BAA_NAME() {
        return TYPE_BAA_NAME;
    }

    public String getTYPE_FT() {
        return TYPE_FT;
    }

    public String getTYPE_FT_NAME() {
        return TYPE_FT_NAME;
    }

    public String getTYPE_OT() {
        return TYPE_OT;
    }

    public String getTYPE_OT_NAME() {
        return TYPE_OT_NAME;
    }

    public String getTYPE_SS() {
        return TYPE_SS;
    }

    public String getTYPE_SS_NAME() {
        return TYPE_SS_NAME;
    }

    public String getTYPE_ES() {
        return TYPE_ES;
    }

    public String getTYPE_ES_NAME() {
        return TYPE_ES_NAME;
    }

    public String getTYPE_IN() {
        return TYPE_IN;
    }

    public String getTYPE_IN_NAME() {
        return TYPE_IN_NAME;
    }

    public String getTYPE_FV() {
        return TYPE_FV;
    }

    public String getTYPE_FV_NAME() {
        return TYPE_FV_NAME;
    }

    public String getTYPE_FRV() {
        return TYPE_FRV;
    }

    public String getTYPE_FRV_NAME() {
        return TYPE_FRV_NAME;
    }

    public String getTYPE_FAV() {
        return TYPE_FAV;
    }

    public String getTYPE_FAV_NAME() {
        return TYPE_FAV_NAME;
    }

    public String getTYPE_BCV() {
        return TYPE_BCV;
    }

    public String getTYPE_BCV_NAME() {
        return TYPE_BCV_NAME;
    }

    public String getTYPE_BLV() {
        return TYPE_BLV;
    }

    public String getTYPE_BLV_NAME() {
        return TYPE_BLV_NAME;
    }

    public String getTYPE_BRV() {
        return TYPE_BRV;
    }

    public String getTYPE_BRV_NAME() {
        return TYPE_BRV_NAME;
    }

    public String getTYPE_BAV() {
        return TYPE_BAV;
    }

    public String getTYPE_BAV_NAME() {
        return TYPE_BAV_NAME;
    }

    public String getTYPE_OD() {
        return TYPE_OD;
    }

    public String getTYPE_OD_NAME() {
        return TYPE_OD_NAME;
    }

    public String getTYPE_OD_RECETTE_NAME() {
        return TYPE_OD_RECETTE_NAME;
    }

    public String getTYPE_OD_DEPENSE_NAME() {
        return TYPE_OD_DEPENSE_NAME;
    }

    public String getTYPE_PT() {
        return TYPE_PT;
    }

    public String getTYPE_PT_NAME() {
        return TYPE_PT_NAME;
    }

    public String getSCR_ARTICLES() {
        return SCR_ARTICLES;
    }

    public String getTYPE_RE() {
        return TYPE_RE;
    }

    public String getTYPE_RE_NAME() {
        return TYPE_RE_NAME;
    }

    public String getTYPE_PT_CREDIT_VENTE() {
        return TYPE_PT_CREDIT_VENTE;
    }

    public String getSCR_CATEGORIE() {
        return SCR_CATEGORIE;
    }

    public String getSCR_CLIENTS() {
        return SCR_CLIENTS;
    }

    public String getSCR_DICTIONNAIRE() {
        return SCR_DICTIONNAIRE;
    }

    public String getSCR_ACHAT() {
        return SCR_ACHAT;
    }

    public String getSCR_ACHAT_NAME() {
        return SCR_ACHAT_NAME;
    }

    public String getSCR_VENTE() {
        return SCR_VENTE;
    }

    public String getSCR_VENTE_NAME() {
        return SCR_VENTE_NAME;
    }

    public String getSCR_DIVERS() {
        return SCR_DIVERS;
    }

    public String getSCR_PHASE_CAISSE_VENTE() {
        return SCR_PHASE_CAISSE_VENTE;
    }

    public String getSCR_DIVERS_NAME() {
        return SCR_DIVERS_NAME;
    }

    public String getSCR_RETENUE() {
        return SCR_RETENUE;
    }

    public String getSCR_RETENUE_NAME() {
        return SCR_RETENUE_NAME;
    }

    public String getDOC_MOUV_BL() {
        return DOC_MOUV_BL;
    }

    public String getDOC_MOUV_F() {
        return DOC_MOUV_F;
    }

    public String getDOC_MOUV_BL_NAME() {
        return DOC_MOUV_BL_NAME;
    }

    public String getDOC_MOUV_F_NAME() {
        return DOC_MOUV_F_NAME;
    }

    public String getMODE_INV_PERM() {
        return MODE_INV_PERM;
    }

    public String getMODE_INV_INTE() {
        return MODE_INV_INTE;
    }

    public String getMODE_INV_PERM_NAME() {
        return MODE_INV_PERM_NAME;
    }

    public String getMODE_INV_INTE_NAME() {
        return MODE_INV_INTE_NAME;
    }

    public String getENTREE() {
        return ENTREE;
    }

    public String getSORTIE() {
        return SORTIE;
    }

    public String getDECLARATION() {
        return DECLARATION;
    }

    public String getTRANSFERT() {
        return TRANSFERT;
    }

    public String getINVENTAIRE() {
        return INVENTAIRE;
    }

    public String getOP_DONS() {
        return OP_DONS;
    }

    public String getPRODUCTION() {
        return PRODUCTION;
    }

    public String getTECHNIQUE() {
        return TECHNIQUE;
    }

    public String getRETOUR() {
        return RETOUR;
    }

    public String getTRANSIT() {
        return TRANSIT;
    }

    public String getRESERVATION() {
        return RESERVATION;
    }

    public String getOP_DEPRECIATION() {
        return OP_DEPRECIATION;
    }

    public String getOP_INITIALISATION() {
        return OP_INITIALISATION;
    }

    public String getOP_AJUSTEMENT_STOCK() {
        return OP_AJUSTEMENT_STOCK;
    }

    public String getYvs_com_mensualite_facture_vente() {
        return yvs_com_mensualite_facture_vente;
    }

    public String getYvs_com_mensualite_facture_achat() {
        return yvs_com_mensualite_facture_achat;
    }

    public String getYvs_base_mensualite_doc_divers() {
        return yvs_base_mensualite_doc_divers;
    }

    public String getYvs_com_contenu_doc_vente() {
        return yvs_com_contenu_doc_vente;
    }

    public String getYvs_com_contenu_doc_achat() {
        return yvs_com_contenu_doc_achat;
    }

    public String getYvs_com_contenu_doc_stock() {
        return yvs_com_contenu_doc_stock;
    }

    public String getYvs_prod_composant_ordre_fabrication() {
        return yvs_prod_composant_ordre_fabrication;
    }

    public String getPROD_OP_TYPE_TEMPS_FIXE() {
        return PROD_OP_TYPE_TEMPS_FIXE;
    }

    public String getPROD_OP_TYPE_TEMPS_PROPORTIONNEL() {
        return PROD_OP_TYPE_TEMPS_PROPORTIONNEL;
    }

    public String getPROD_OP_TYPE_TEMPS_CADENCE() {
        return PROD_OP_TYPE_TEMPS_CADENCE;
    }

    public String getPROD_OP_TYPE_CHARGE_MO() {
        return PROD_OP_TYPE_CHARGE_MO;
    }

    public String getPROD_OP_TYPE_CHARGE_MACHINE() {
        return PROD_OP_TYPE_CHARGE_MACHINE;
    }

    public String getMOD_GRH() {
        return MOD_GRH;
    }

    public String getMOD_MUT() {
        return MOD_MUT;
    }

    public String getMOD_COM() {
        return MOD_COM;
    }

    public String getMOD_MSG() {
        return MOD_MSG;
    }

    public String getMOD_REL() {
        return MOD_REL;
    }

    public String getMOD_COFI() {
        return MOD_COFI;
    }

    public String getMOD_PROD() {
        return MOD_PROD;
    }

    public String getMOD_GRH_NAME() {
        return MOD_GRH_NAME;
    }

    public String getMOD_MUT_NAME() {
        return MOD_MUT_NAME;
    }

    public String getMOD_COM_NAME() {
        return MOD_COM_NAME;
    }

    public String getMOD_MSG_NAME() {
        return MOD_MSG_NAME;
    }

    public String getMOD_REL_NAME() {
        return MOD_REL_NAME;
    }

    public String getMOD_COFI_NAME() {
        return MOD_COFI_NAME;
    }

    public String getMOD_PROD_NAME() {
        return MOD_PROD_NAME;
    }

    public String getMOUV_CAISS_ENTREE() {
        return MOUV_CAISS_ENTREE;
    }

    public String getMOUV_CAISS_SORTIE() {
        return MOUV_CAISS_SORTIE;
    }

    public Options[] getCATEGORIES() {
        return CATEGORIES;
    }

    public Options[] getMODE_CONSO() {
        return MODE_CONSO;
    }

    public Options[] getMODES_APPRO() {
        return MODES_APPRO;
    }

    public Options[] getMODES_REAPPRO() {
        return MODES_REAPPRO;
    }

    public Options[] getMETHODES_VAL() {
        return METHODES_VAL;
    }

    public String[] getGRH_TYPES_CONGE() {
        return GRH_TYPES_CONGE;
    }

    public String getPROD_OP_TYPE_COMPOSANT_NORMAL() {
        return PROD_OP_TYPE_COMPOSANT_NORMAL;
    }

    public String getPROD_OP_TYPE_COMPOSANT_SOUS_PRODUIT() {
        return PROD_OP_TYPE_COMPOSANT_SOUS_PRODUIT;
    }

    public String getSYMBOLE_SUP_EGALE() {
        return SYMBOLE_SUP_EGALE;
    }

    public String getSYMBOLE_INF_EGALE() {
        return SYMBOLE_INF_EGALE;
    }

    public String getSYMBOLE_BETWEEN() {
        return SYMBOLE_BETWEEN;
    }

    public String getSYMBOLE_DIFFERENT() {
        return SYMBOLE_DIFFERENT;
    }

    public String getSYMBOLE_EGALE() {
        return SYMBOLE_EGALE;
    }

    public String getBETWEEN() {
        return BETWEEN;
    }

    public String getMUT_MODE_PAIEMENT_COMPTE() {
        return MUT_MODE_PAIEMENT_COMPTE;
    }

    public String getMUT_MODE_PAIEMENT_ESPECE() {
        return MUT_MODE_PAIEMENT_ESPECE;
    }

    public String getMUT_TYPE_MONAIE_PHYSIQUE() {
        return MUT_TYPE_MONAIE_PHYSIQUE;
    }

    public String getMUT_TYPE_MONAIE_SCRIPTURALE() {
        return MUT_TYPE_MONAIE_SCRIPTURALE;
    }

    public String getETAT_PROD_LANCE() {
        return ETAT_PROD_LANCE;
    }

    public String getTYPE_OF_REPRISE() {
        return TYPE_OF_REPRISE;
    }

    public String getTYPE_OF_CONTROLE() {
        return TYPE_OF_CONTROLE;
    }

    public String getTYPE_OF_OUTILLAGE() {
        return TYPE_OF_OUTILLAGE;
    }

    public String getTYPE_OF_PRODUCTION() {
        return TYPE_OF_PRODUCTION;
    }

    public String getTYPE_OF_REPETITIF() {
        return TYPE_OF_REPETITIF;
    }

    public String getTYPE_OF_ARA() {
        return TYPE_OF_ARA;
    }

    public String getPROD_TYPE_INDICATEUR_QUALITATIF() {
        return PROD_TYPE_INDICATEUR_QUALITATIF;
    }

    public String getPROD_TYPE_INDICATEUR_QUANTITATIF() {
        return PROD_TYPE_INDICATEUR_QUANTITATIF;
    }

    public Character getSTOCK_SENS_ENTREE() {
        return STOCK_SENS_ENTREE;
    }

    public Character getSTOCK_SENS_SORTIE() {
        return STOCK_SENS_SORTIE;
    }

    public Character getSTOCK_SENS_NEUTRE() {
        return STOCK_SENS_NEUTRE;
    }

    public String getCOMPTA_DEPENSE() {
        return COMPTA_DEPENSE;
    }

    public String getCOMPTA_RECETTE() {
        return COMPTA_RECETTE;
    }

    public Character getPROD_TYPE_COUT_TAUX() {
        return PROD_TYPE_COUT_TAUX;
    }

    public Character getPROD_TYPE_COUT_VALEUR() {
        return PROD_TYPE_COUT_VALEUR;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public String getLOCATION_MATERIEL() {
        return LOCATION_MATERIEL;
    }

    public String getLOCATION_IMMOBILIER() {
        return LOCATION_IMMOBILIER;
    }

    public Character getPROD_OP_TYPE_COUT_PROPORTIONNEL() {
        return PROD_OP_TYPE_COUT_PROPORTIONNEL;
    }

    public Character getPROD_OP_TYPE_COUT_TOTAL() {
        return PROD_OP_TYPE_COUT_TOTAL;
    }

    public String getPROD_TYPE_NOMENCLATURE_CONDTIONNEMENT() {
        return PROD_TYPE_NOMENCLATURE_CONDTIONNEMENT;
    }

    public String getPROD_TYPE_NOMENCLATURE_PRODUCTION() {
        return PROD_TYPE_NOMENCLATURE_PRODUCTION;
    }

    public String getPROD_TYPE_NOMENCLATURE_TRANSFORMATION() {
        return PROD_TYPE_NOMENCLATURE_TRANSFORMATION;
    }

    public String getDOCUMENT_OD_NON_PLANNIFIE() {
        return DOCUMENT_OD_NON_PLANNIFIE;
    }

    public String getDOCUMENT_BON_DIVERS_CAISSE() {
        return DOCUMENT_BON_DIVERS_CAISSE;
    }

    public String getDOCUMENT_RETOUR_ACHAT() {
        return DOCUMENT_RETOUR_ACHAT;
    }

    public String getDOCUMENT_AVOIR_ACHAT() {
        return DOCUMENT_AVOIR_ACHAT;
    }

    public String getDOCUMENT_RETOUR_VENTE() {
        return DOCUMENT_RETOUR_VENTE;
    }

    public String getDOCUMENT_AVOIR_VENTE() {
        return DOCUMENT_AVOIR_VENTE;
    }

    public String getDOCUMENT_APPROVISIONNEMENT() {
        return DOCUMENT_APPROVISIONNEMENT;
    }

    public String getDOCUMENT_PIECE_CAISSE() {
        return DOCUMENT_PIECE_CAISSE;
    }

    public String getDOCUMENT_PHASE_CAISSE_SALAIRE() {
        return DOCUMENT_PHASE_CAISSE_SALAIRE;
    }

    public String getDOCUMENT_PHASE_CAISSE_DIVERS() {
        return DOCUMENT_PHASE_CAISSE_DIVERS;
    }

    public String getDOCUMENT_PHASE_CAISSE_VENTE() {
        return DOCUMENT_PHASE_CAISSE_VENTE;
    }

    public String getDOCUMENT_PHASE_CAISSE_ACHAT() {
        return DOCUMENT_PHASE_CAISSE_ACHAT;
    }

    public String getDOCUMENT_CP_UPPER_PR() {
        return DOCUMENT_CP_UPPER_PR;
    }

    public String getDOCUMENT_PHASE_ACOMPTE_VENTE() {
        return DOCUMENT_PHASE_ACOMPTE_VENTE;
    }

    public String getDOCUMENT_PHASE_ACOMPTE_ACHAT() {
        return DOCUMENT_PHASE_ACOMPTE_ACHAT;
    }

    public String getDOCUMENT_PHASE_CREDIT_VENTE() {
        return DOCUMENT_PHASE_CREDIT_VENTE;
    }

    public String getDOCUMENT_PHASE_CREDIT_ACHAT() {
        return DOCUMENT_PHASE_CREDIT_ACHAT;
    }

    public String getDOCUMENT_STOCK_ARTICLE() {
        return DOCUMENT_STOCK_ARTICLE;
    }

    public String getDOCUMENT_ARTICLE_NON_MOUVEMENTE() {
        return DOCUMENT_ARTICLE_NON_MOUVEMENTE;
    }

    public String getDOCUMENT_HIGH_PR_ARTICLE() {
        return DOCUMENT_HIGH_PR_ARTICLE;
    }

    public String getDOCUMENT_ENTREE() {
        return DOCUMENT_ENTREE;
    }

    public String getDOCUMENT_BON_LIVRAISON_ACHAT() {
        return DOCUMENT_BON_LIVRAISON_ACHAT;
    }

    public String getDOCUMENT_BON_LIVRAISON_VENTE() {
        return DOCUMENT_BON_LIVRAISON_VENTE;
    }

    public String getDOCUMENT_FACTURE_ACHAT_LIVRE() {
        return DOCUMENT_FACTURE_ACHAT_LIVRE;
    }

    public String getDOCUMENT_FACTURE_ACHAT_REGLE() {
        return DOCUMENT_FACTURE_ACHAT_REGLE;
    }

    public String getDOCUMENT_FACTURE_VENTE_LIVRE() {
        return DOCUMENT_FACTURE_VENTE_LIVRE;
    }

    public String getDOCUMENT_FACTURE_VENTE_REGLE() {
        return DOCUMENT_FACTURE_VENTE_REGLE;
    }

    public String getDOCUMENT_INVENTAIRE_STOCK() {
        return DOCUMENT_INVENTAIRE_STOCK;
    }

    public String getDOCUMENT_RECONDITIONNEMENT_STOCK() {
        return DOCUMENT_RECONDITIONNEMENT_STOCK;
    }

    public String getDOCUMENT_DOC_DIVERS_DEPENSE() {
        return DOCUMENT_DOC_DIVERS_DEPENSE;
    }

    public String getDOCUMENT_DOC_DIVERS_RECETTE() {
        return DOCUMENT_DOC_DIVERS_RECETTE;
    }

    public String getDOCUMENT_LOWER_MARGIN() {
        return DOCUMENT_LOWER_MARGIN;
    }

    public static double arrondi(double nbre, int precision) {
        return Math.round(nbre * Math.pow(10, precision)) / Math.pow(10, precision);
    }

    public static double arrondiA0Chiffre(double d) {
        return (double) Math.round(d);
    }

    public static double arrondiA2Chiffre(double d) {
        double result = (double) Math.round(d * 100) / 100;
        return result;
    }

    public static double arrondiA3Chiffre(double d) {
        double result = (double) Math.round(d * 1000) / 1000;
        return result;
    }

    public static double giveDecimalPart(double nbre) {
        int entier = (int) nbre;
        return nbre - entier;
    }

    public String getPROD_TYPE_PAGE_CLASSIQUE() {
        return PROD_TYPE_PAGE_CLASSIQUE;
    }

    public String getPROD_TYPE_PAGE_SUIVI() {
        return PROD_TYPE_PAGE_SUIVI;
    }

    public static int calculNbYear(Date d1, Date d2) {
        if (d1 != null && d2 != null) {
            if (d2.after(d1)) {
                int A1, A2, M1, M2;
                Calendar c1 = Calendar.getInstance();
                c1.setTime(d1);
                A1 = c1.get(Calendar.YEAR);
                M1 = c1.get(Calendar.MONTH);
                c1.setTime(d2);
                A2 = c1.get(Calendar.YEAR);
                M2 = c1.get(Calendar.MONTH);
                return (int) ((((A2 * 12) + M2) - ((A1 * 12) + M1)) / 12);
            }
        }
        return 0;
    }

    public Date doubleToTime(double time) {
        int hour = new Double(time).intValue(); //recuperer la partie entiere
        int min = (int) ((time - hour) * 60);
        Date re = new Date();
        Calendar c = getIniTializeDate(re);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        re = c.getTime();
        return re;
    }

    public int countDayBetweenDate(Date begin, Date end) {
        if (begin != null && end != null) {
            Calendar d1 = Calendar.getInstance();
            d1.setTime(begin);
            d1.set(Calendar.HOUR, 0);
            d1.set(Calendar.MINUTE, 0);
            d1.set(Calendar.SECOND, 0);
            d1.set(Calendar.MILLISECOND, 0);
            Calendar d2 = Calendar.getInstance();
            d2.setTime(end);
            d2.set(Calendar.HOUR, 0);
            d2.set(Calendar.MINUTE, 0);
            d2.set(Calendar.SECOND, 0);
            d2.set(Calendar.MILLISECOND, 0);
            if (d2.getTime().equals(d1.getTime())) {
                return 1;
            } else {
                return (int) TimeUnit.MILLISECONDS.toDays(d2.getTimeInMillis() - d1.getTimeInMillis());
            }
        }
        return 0;
    }

    public Date buildDateTime(Date date, Date time) {
        Calendar Cdat = Calendar.getInstance();
        Calendar Ctim = Calendar.getInstance();
        if (date != null && time != null) {
            Cdat.setTime(date);
            Ctim.setTime(time);
            Cdat.set(Calendar.HOUR_OF_DAY, Ctim.get(Calendar.HOUR_OF_DAY));
            Cdat.set(Calendar.MINUTE, Ctim.get(Calendar.MINUTE));
            Cdat.set(Calendar.SECOND, Ctim.get(Calendar.SECOND));
            return Cdat.getTime();
        } else if (date != null && time == null) {
            return date;
        } else if (date == null && time != null) {
            return time;
        } else {
            return null;
        }
    }

    public static Date getPreviewDate(Date date) {
        Calendar Cdat = Calendar.getInstance();
        if (date != null) {
            Cdat.setTime(date);
            Cdat.add(Calendar.DAY_OF_MONTH, -1);
            return Cdat.getTime();
        }
        return new Date();
    }

    public static double arrondiPlus(double nbre) {
        return Math.ceil(nbre);
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

    public static String PERIODE_NAME(String value) {
        switch (value) {
            case UNITE_ANNEE_:
                return UNITE_ANNEE;
            case UNITE_MOIS_:
                return UNITE_MOIS;
            case UNITE_SEMAINE_:
                return UNITE_SEMAINE;
            default:
                return UNITE_JOUR;
        }
    }

    public static String DEFAULT_PHOTO() {
        if (DEFAULT_PHOTO != null ? DEFAULT_PHOTO.trim().length() < 1 : true) {
            FileInputStream fis = null;
            try {
                String fileName = Initialisation.getCheminResource() + Initialisation.FILE_SEPARATOR + "default_photo.txt";
                File file = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath(fileName));
                fis = new FileInputStream(file);
                DEFAULT_PHOTO = IOUtils.toString(fis, "UTF-8");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Constantes.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Constantes.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Constantes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return DEFAULT_PHOTO;
    }

    public static String DEFAULT_PHOTO_EMPLOYE_MAN() {
        if (DEFAULT_PHOTO_EMPLOYE_MAN != null ? DEFAULT_PHOTO_EMPLOYE_MAN.trim().length() < 1 : true) {
            FileInputStream fis = null;
            try {
                String fileName = Initialisation.getCheminResource() + Initialisation.FILE_SEPARATOR + "default_photo_employe_man.txt";
                File file = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath(fileName));
                fis = new FileInputStream(file);
                DEFAULT_PHOTO_EMPLOYE_MAN = IOUtils.toString(fis, "UTF-8");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Constantes.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Constantes.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Constantes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return DEFAULT_PHOTO_EMPLOYE_MAN;
    }

    public static String DEFAULT_PHOTO_EMPLOYE_WOMAN() {
        if (DEFAULT_PHOTO_EMPLOYE_WOMAN != null ? DEFAULT_PHOTO_EMPLOYE_WOMAN.trim().length() < 1 : true) {
            FileInputStream fis = null;
            try {
                String fileName = Initialisation.getCheminResource() + Initialisation.FILE_SEPARATOR + "default_photo_employe_woman.txt";
                File file = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath(fileName));
                fis = new FileInputStream(file);
                DEFAULT_PHOTO_EMPLOYE_WOMAN = IOUtils.toString(fis, "UTF-8");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Constantes.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Constantes.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Constantes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return DEFAULT_PHOTO_EMPLOYE_WOMAN;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.dao.Util;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseDepotOperation;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.compta.saisie.YvsComptaContentAnalytique;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.personnel.YvsGrhPlanningEmploye;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
public class AbstractEntity {

    public static DateFormat formatJour = new SimpleDateFormat("EEEE");
    public static DateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
    public static DateFormat formatDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    protected DaoInterfaceLocal dao;
    protected YvsNiveauAcces niveau;
    protected YvsAgences currentAgence;
    protected YvsParametreGrh currentParamGrh;

    protected String NULL_DATETIME = "01-01-0001 00:00:00";

    protected List<YvsComptaPiecesComptable> listePiece, piecesSelect;

    protected String nameQueri;
    protected String[] champ;
    protected Object[] val;

    protected IEntitySax IEntitiSax = new IEntitySax();

    public void setDao(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public void setAgence(YvsAgences agence) {
        this.currentAgence = agence;
    }

    public void setNiveauAcces(YvsNiveauAcces niveau) {
        this.niveau = niveau;
    }

    protected boolean asString(String value) {
        return value != null ? value.trim().length() > 0 : false;
    }

    public boolean autoriser(String ressource, YvsNiveauAcces niveau) {
        this.niveau = niveau;
        return autoriser(ressource);
    }

    public Long getValueForServeur(String serveur) {
        try {
            if (asString(serveur)) {
                return (Long) dao.loadObjectByNameQueries("YvsSynchroDataSynchro.countByServeur", new String[]{"serveur"}, new Object[]{serveur});
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0L;
    }

    public Long getLocalCurrent(Long distant, String tableName, String serveur) {
        try {
            if (distant != null ? distant > 0 : false) {
                return (Long) dao.loadObjectByNameQueries("YvsSynchroDataSynchro.findSource", new String[]{"tableName", "distant", "serveur"}, new Object[]{tableName, distant, serveur});
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean autoriser(String ressource) {
        if (niveau != null ? niveau.getId() > 0 : false) {
            String query = "SELECT a.acces FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON r.id=a.ressource_page "
                    + "WHERE a.niveau_acces=? AND r.reference_ressource=? ";
//        Boolean b = (Boolean) dao.loadObjectByNameQueries("YvsAutorisationRessourcesPage.findAccesRessource", new String[]{"reference", "niveau"}, new Object[]{ressource, niveau});
            Boolean b = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(niveau.getId(), 1), new Options(ressource, 2)});
            return b != null ? b : false;
        }
        return false;
    }

    public boolean verifyOperation(YvsBaseDepots d, String type, String operation, boolean error) {
        if (d != null ? d.getId() > 0 : false) {
            if (!checkOperationDepot(d.getId(), type)) {
                if (error) {
//                    getErrorMessage("Le depot '" + d.getDesignation() + "' n'est pas paramètré pour les opérations '" + type + "'");
                } else {
//                    getWarningMessage("Le depot '" + d.getDesignation() + "' n'est pas paramètré pour les opérations '" + type + "'");
                }
                return false;
            }
            if (!checkOperationDepot(d.getId(), type, operation)) {
                if (error) {
//                    getErrorMessage("Le depot '" + d.getDesignation() + "' n'est pas paramètré pour les opérations '" + operation + "' en '" + type + "'");
                } else {
//                    getWarningMessage("Le depot '" + d.getDesignation() + "' n'est pas paramètré pour les opérations '" + operation + "' en '" + type + "'");
                }
                return false;
            }
        }
        return true;
    }

    public void loadParametreGrh(YvsSocietes societe) {
        currentParamGrh = (YvsParametreGrh) dao.loadOneByNameQueries("YvsParametreGrh.findAll", new String[]{"societe"}, new Object[]{societe});
    }

    public boolean checkOperationDepot(long depot, String type) {
        return checkOperationDepot(depot, type, dao);
    }

    public boolean checkOperationDepot(long depot, String type, DaoInterfaceLocal dao) {
        String[] champ = new String[]{"depot", "type"};
        Object[] val = new Object[]{new YvsBaseDepots(depot), type};
        String nameQueri = "YvsBaseDepotOperation.findByDepotType";
        List<YvsBaseDepotOperation> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        return l != null ? !l.isEmpty() : false;
    }

    public boolean checkOperationDepot(long depot, String type, String operation) {
        if (!checkOperationDepot(depot, type)) {
            return false;
        }
        String[] champ = new String[]{"depot", "type", "operation"};
        Object[] val = new Object[]{new YvsBaseDepots(depot), type, operation};
        String nameQueri = "YvsBaseDepotOperation.findByDepotTypeOperation";
        List<YvsBaseDepotOperation> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        return l != null ? !l.isEmpty() : false;
    }

    public ResultatAction<YvsComEnteteDocVente> verifyDate(Date date, int ecart) {
        date = date != null ? date : new Date();
        YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findActifByDate", new String[]{"dateJour", "societe"}, new Object[]{date, currentAgence.getSociete()});
        if (exo != null ? exo.getId() < 1 : true) {
            return new ResultatAction<>(false, null, 0L, "Le document doit etre enregistré dans un exercice actif");
        }
        if (exo.getCloturer()) {
            return new ResultatAction<>(false, null, 0L, "Le document ne peut pas etre enregistré dans un exercice cloturé");
        }
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        now.add(Calendar.DATE, 1);

        Calendar d = Calendar.getInstance();
        d.setTime(date);
        d.set(Calendar.HOUR_OF_DAY, 0);
        d.set(Calendar.MINUTE, 0);
        d.set(Calendar.SECOND, 0);
        d.set(Calendar.MILLISECOND, 0);
        if (d.after(now)) {
            return new ResultatAction<>(false, null, 0L, "La date ne doit pas superieur à la date du jour");
        }
        if (autoriser("com_save_hors_limit")) {
            return new ResultatAction<>(true, null, 0L, "");
        }
        if (ecart > 0) {
            now.add(Calendar.DATE, -ecart);
            if (d.before(now)) {
                return new ResultatAction<>(false, null, 0L, "La date ne doit pas exceder le nombre de jour de retrait prévu");
            }
        }
        return new ResultatAction<>(true, null, 0L, "");
    }

    public ResultatAction toString(String value) {
        return new ResultatAction(true, 1, value);
    }

    public List<YvsComptaContentJournal> buildContentJournal(long id, String table, boolean msg, YvsUsersAgence currentUser) {
        return buildContentJournal(id, table, false, msg, currentUser);
    }

    public List<YvsComptaContentJournal> buildContentJournal(long id, String table, boolean action, boolean msg, YvsUsersAgence currentUser) {
        return buildContentJournal(id, table, null, action, msg, currentUser.getAgence().getSociete(), currentUser);
    }

    public List<YvsComptaContentJournal> buildContentJournal(long id, String table, boolean msg, YvsSocietes currentSte, YvsUsersAgence currentUser) {
        return buildContentJournal(id, table, null, false, msg, currentUser.getAgence().getSociete(), currentUser);
    }

    public List<YvsComptaContentJournal> buildContentJournal(long id, String table, List<YvsAgences> agences, boolean msg, YvsSocietes currentSte, YvsUsersAgence currentUser) {
        return buildContentJournal(id, table, agences, false, msg, currentUser.getAgence().getSociete(), currentUser);
    }

    public List<YvsComptaContentJournal> buildContentJournal(long id, String table, List<YvsAgences> agences, boolean action, boolean msg, YvsSocietes currentSte, YvsUsersAgence currentUser) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        dao.getEntityManager().clear();
        String query = "select y.* from public.yvs_compta_content_journal(?,?,?,?,?) y";
        String ids = "";
        if (agences != null ? !agences.isEmpty() : false) {
            ids = "0";
            if (!action) {
                List<YvsAgences> listAgence = dao.loadNameQueries("YvsAgences.findBySociete", new String[]{"societe"}, new Object[]{currentSte});
                for (YvsAgences a : listAgence) {
                    if (!agences.contains(a)) {
                        ids += "," + a.getId();
                    }
                }
            } else {
                for (YvsAgences a : agences) {
                    ids += "," + a.getId();
                }
            }
        }
        Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(ids, 2), new Options(id, 3), new Options(table, 4), new Options(true, 5)};
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        YvsComptaContentJournal line;
        for (Object o : qr.getResultList()) {
            line = convertTabToContenu((Object[]) o, id, msg, currentUser);
            if (!controleContenu(line, msg)) {
                return null;
            }
            int idx = list.indexOf(line);
            if (idx > -1) {
                list.get(idx).getAnalytiques().addAll(line.getAnalytiques());
            } else {
                list.add(line);
            }
        }
        return list;
    }

    public boolean controleContenu(YvsComptaPiecesComptable y) {
        Long count = (Long) dao.loadObjectByNameQueries("YvsComptaContentJournal.countByPiece", new String[]{"piece"}, new Object[]{y});
        if (count != null ? count > 0 : false) {
            return true;
        }
        return false;
    }

    public boolean controleContenu(YvsComptaContentJournal bean, boolean msg) {
        if (bean == null) {
            return false;
        }
        if (bean.getCompteGeneral() != null ? (bean.getCompteGeneral().getId() != null ? bean.getCompteGeneral().getId() < 1 : true) : true) {
            if (msg) {
//                getErrorMessage("Vous devez preciser le compte général");
            }
            return false;
        }
        YvsBasePlanComptable c = bean.getCompteGeneral();
        if (c.getSaisieAnalytique() && false) {
//            getErrorMessage("Vous devez preciser le plan analytique");
//            return false;
        }
        if (c.getSaisieCompteTiers() && bean.getCompteTiers() == null) {
            if (msg) {
//                getErrorMessage("Vous devez preciser le compte tiers");
            }
            return false;
        }
        if (c.getSaisieEcheance() && bean.getEcheance() == null) {
            if (msg) {
//                getErrorMessage("Vous devez preciser l'échéancier");
            }
            return false;
        }
        return true;
    }

    public YvsComptaContentJournal convertTabToContenu(Object[] line, Long id, boolean msg, YvsUsersAgence currentUser) {
        YvsComptaContentJournal re = null;
        if (line != null ? line.length > 0 : false) {
            Long _id = line[0] != null ? Long.valueOf(line[0].toString()) : null;
            Integer _jour = line[1] != null ? Integer.valueOf(line[1].toString()) : 0;
            String _num_piece = line[2] != null ? line[2].toString() : "";
            String _num_ref = line[3] != null ? line[3].toString() : "";
            Long _compte_general = line[4] != null ? Long.valueOf(line[4].toString()) : 0;
            Long _compte_tiers = line[5] != null ? Long.valueOf(line[5].toString()) : 0;
            String _libelle = line[6] != null ? line[6].toString() : "";
            Double _debit = line[7] != null ? Double.valueOf(line[7].toString()) : 0;
            Double _credit = line[8] != null ? Double.valueOf(line[8].toString()) : 0;
            Date _echeance = line[9] != null ? (Date) line[9] : new Date();
            Long _ref_externe = line[10] != null ? Long.valueOf(line[10].toString()) : id;
            String _table_externe = line[11] != null ? line[11].toString() : Constantes.SCR_VENTE;
            String _statut = line[12] != null ? line[12].toString() : Constantes.ETAT_VALIDE;
            String _error = line[13] != null ? line[13].toString() : "";
            Long _contenu = line[14] != null ? Long.valueOf(line[14].toString()) : 0;
            Long _centre = line[15] != null ? Long.valueOf(line[15].toString()) : 0;
            Double _coefficient = line[16] != null ? Double.valueOf(line[16].toString()) : 0;
            Integer _numero = line[17] != null ? Integer.valueOf(line[17].toString()) : 0;
            Long _agence = line[18] != null ? Long.valueOf(line[18].toString()) : 0;
            String _warning = line[19] != null ? line[19].toString() : "";
            String _table_tiers = line[20] != null ? line[20].toString() : "";

            if (_error != null ? _error.trim().length() > 0 : false) {
                if (msg) {
//                    getErrorMessage("Comptabilisation impossible.. car " + _error);
                }
                return null;
            }
            if (_warning != null ? _warning.trim().length() > 0 : false) {
                if (msg) {
//                    getWarningMessage(_warning);
                }
            }
            re = new YvsComptaContentJournal(_id, _jour, _num_piece, _num_ref, _libelle, _debit, _credit, _echeance, _compte_general, _compte_tiers, _table_tiers, _ref_externe, _table_externe, _statut.charAt(0), _numero);
            if (re.getCompteGeneral() != null ? re.getCompteGeneral().getId() > 0 : false) {
                re.setCompteGeneral((YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findById", new String[]{"id"}, new Object[]{re.getCompteGeneral().getId()}));
            }
            if (_contenu != null ? _contenu != 0 : false) {
                YvsComptaContentAnalytique ca = new YvsComptaContentAnalytique(_id);
                if (_centre != null ? _centre > 0 : false) {
                    ca.setCentre((YvsComptaCentreAnalytique) dao.loadOneByNameQueries("YvsComptaCentreAnalytique.findById", new String[]{"id"}, new Object[]{_centre}));
                }
                ca.setCoefficient(_coefficient);
                ca.setDebit(_debit);
                ca.setCredit(_credit);
                ca.setAuthor(currentUser);
                ca.setDateSaisie(new Date());
                ca.setDateSave(new Date());
                ca.setDateUpdate(new Date());

                re.getAnalytiques().add(ca);
            }
        }
        return re;
    }

    public List<Integer> decomposeSelection(String selection) {
        List<Integer> re = new ArrayList<>();
        List<String> l = new ArrayList<>();
        if (selection != null) {
            String numroLine[] = selection.split("-");
            try {
                int index;
                for (String numroLine1 : numroLine) {
                    index = Integer.parseInt(numroLine1);
                    re.add(index);
                    l.add(numroLine1);
                }
            } catch (NumberFormatException ex) {
                selection = "";
                for (String numroLine1 : numroLine) {
                    if (!l.contains(numroLine1)) {
                        selection += numroLine1 + "-";      //mémorise la selection
                    }
                }

            }
        }
        return re;
    }

    public double giveSoePieces(List<YvsComptaContentJournal> contenus) {
        double re, cr = 0, db = 0;
        for (YvsComptaContentJournal cc : contenus) {
            db += cc.getDebit();
            cr += cc.getCredit();
        }
        re = db - cr;
        return arrondi(re, 3);
    }

    public static double arrondi(double d, int l) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(l, BigDecimal.ROUND_DOWN);
        double r = bd.doubleValue();
        return r;
    }

    public static String nextValue(String oldValue) {
        // TODO code application logic here
        String newValue = "A";
        if (oldValue != null ? oldValue.trim().length() > 0 : false) {
            newValue = oldValue.toUpperCase();
            String temp = newValue;
            boolean continu = true;
            boolean increment = false;
            int length = newValue.length() - 1;//Recuperer la taille de la chaine entrée
            for (int i = length; i > -1; i--) {//Boucle inverse sur la taille
                char ch = newValue.charAt(i);//Recuperer le caracter a la position i
                int index = Constantes.ALPHABET.indexOf(ch);//Recupere l'index dans l'alphabet
                char rs = 'A';
                if (index == Constantes.ALPHABET.length() - 1) {//Verifie si on est a la dernier lettre de l'alphabet
                    increment = true;//On precise On va revenir a la lettre A
                } else {
                    rs = Constantes.ALPHABET.charAt(index + 1);//Recupere la lettre suivante
                    System.err.println("index " + index);
                    System.err.println("rs " + rs);
                    if (!continu) {//Verifie si on est passé a la lettre A
                        break;
                    }
                    if (increment) {//Definie si on est passé a la lettre A
                        continu = false;
                    }
                    index = Constantes.ALPHABET.indexOf(rs);//Recupere l'index dans l'alphabet
                    if (index != Constantes.ALPHABET.length() - 1) {//Verifie si on n'est pas a la dernier lettre de l'alphabet
                        continu = false;
                    }
                }
                newValue = newValue.substring(0, i) + String.valueOf(rs);//Change la lettre courante en sa nouvelle valeur
                if (i < length) {//Verifie si on n'est plus a la 1ere lettre a modifier
                    newValue += temp.substring(i + 1, temp.length());//Ajoute la suite des lettres a la nouvelle chaine
                }
                temp = newValue;//Sauvegarde la nouvelle valeur
            }
            increment = true;//Cas ou on a est passé a la lettre A pour toutes les lettres
            for (int i = 0; i < newValue.length(); i++) {
                if (newValue.charAt(i) != 'A') {
                    increment = false;
                    break;
                }
            }
            if (increment) {//On ajoute une nouvelle lettre A
                newValue += "A";
            }
        }
        return newValue;
    }

    public String nextLettre() {
        String value = "A";
        try {
            Integer length = (Integer) dao.loadObjectByNameQueries("YvsComptaContentJournal.findMaxLength", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            if (length != null ? length > 0 : false) {
                String lettre = (String) dao.loadObjectByNameQueries("YvsComptaContentJournal.findMaxLetter", new String[]{"societe", "length"}, new Object[]{currentAgence.getSociete(), length});
                value = nextValue(lettre);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public String lettrageCompte(List<YvsComptaContentJournal> contenus, YvsUsersAgence currentUser) {
        String lettrage = "";
        try {
            if (contenus != null ? !contenus.isEmpty() : false) {
                lettrage = nextLettre();
                for (YvsComptaContentJournal c : contenus) {
                    c.setLettrage(lettrage);
                    c.setAuthor(currentUser);
                    c.setDateUpdate(new Date());
                    dao.update(c);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lettrage;
    }

    public YvsComptaPiecesComptable saveNewPieceComptable(Date dateDoc, YvsComptaJournaux jrn, List<YvsComptaContentJournal> contenus, boolean msg, YvsUsersAgence currentUser, YvsAgences currentAgence) {
        try {
            if (contenus != null ? !contenus.isEmpty() : false) {
                if (jrn != null ? jrn.getId() < 1 : true) {

                    return null;
                }
                dateDoc = dateDoc != null ? dateDoc : new Date();
                YvsBaseExercice exo = controleExercice(dateDoc, msg, currentAgence);
                if (exo != null ? exo.getId() < 1 : true) {
                    return null;
                }
                String num = dao.genererReference(Constantes.TYPE_PIECE_COMPTABLE_NAME, dateDoc, jrn.getId(), currentAgence.getSociete(), currentAgence);
                if (num == null || num.trim().length() < 1) {
                    return null;
                }
                YvsComptaPiecesComptable p = new YvsComptaPiecesComptable();
                p.setAuthor(currentUser);
                p.setDatePiece(dateDoc);
                p.setDateSaisie(new Date());
                p.setExercice(exo);
                p.setJournal(jrn);
                p.setNumPiece(num);
                p.setStatutPiece(Constantes.STATUT_DOC_EDITABLE);
                p.setExtourne(false);
                p = (YvsComptaPiecesComptable) dao.save1(p);
                for (YvsComptaContentJournal c : contenus) {
                    List<YvsComptaContentAnalytique> lista = new ArrayList<>();
                    lista.addAll(c.getAnalytiques());
                    c.getAnalytiques().clear();

                    c.setId(null);
                    c.setAuthor(currentUser);
                    c.setPiece(p);
                    c = (YvsComptaContentJournal) dao.save1(c);

                    for (YvsComptaContentAnalytique a : lista) {
                        if (a.getCentre() != null ? a.getCentre().getId() > 0 : false) {
                            a.setId(null);
                            a.setContenu(c);
                            a = (YvsComptaContentAnalytique) dao.save1(a);
                            c.getAnalytiques().add(a);
                        }
                    }
                    p.getContentsPiece().add(c);
                }
                return p;
            }
        } catch (Exception ex) {

        }
        return null;
    }

    public YvsBaseExercice controleExercice(Date date, boolean msg, YvsAgences currentAgence) {
        YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByDate", new String[]{"societe", "date"}, new Object[]{currentAgence.getSociete(), date});
        if (exo != null ? exo.getId() < 1 : true) {

            return null;
        }
        if (!exo.getActif()) {

            return null;
        }
        if (exo.getCloturer()) {

            return null;
        }
        return exo;
    }

    public boolean isComptabilise(long id, String table) {
        return dao.isComptabilise(id, table);
    }

    public List<YvsGrhTrancheHoraire> loadTranche(YvsBaseDepots y, Date date) {
        return loadTranche(null, y, date);
    }

    public boolean checkOperationArticle(long article, long depot, String operation) {
        if (depot > 0) {
            champ = new String[]{"depot", "article"};
            val = new Object[]{new YvsBaseDepots(depot), new YvsBaseArticles(article)};
            nameQueri = "YvsBaseArticleDepot.findByArticleDepot";
            List<YvsBaseArticleDepot> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
            if (l != null ? !l.isEmpty() : false) {
                YvsBaseArticleDepot a = l.get(0);
                if (a.getModeAppro() != null) {
                    switch (operation) {
                        case Constantes.ACHAT: {
                            switch (a.getModeAppro()) {
                                case Constantes.APPRO_ACHTON:
                                case Constantes.APPRO_ACHT_EN:
                                case Constantes.APPRO_ACHT_PROD:
                                case Constantes.APPRO_ACHT_PROD_EN:
                                    return true;
                                default:
                                    return false;
                            }
                        }
                        case Constantes.ENTREE: {
                            switch (a.getModeAppro()) {
                                case Constantes.APPRO_ENON:
                                case Constantes.APPRO_ACHT_EN:
                                case Constantes.APPRO_PROD_EN:
                                case Constantes.APPRO_ACHT_PROD_EN:
                                    return true;
                                default:
                                    return false;
                            }
                        }
                        case Constantes.PRODUCTION: {
                            switch (a.getModeAppro()) {
                                case Constantes.APPRO_PRODON:
                                case Constantes.APPRO_ACHT_PROD:
                                case Constantes.APPRO_PROD_EN:
                                case Constantes.APPRO_ACHT_PROD_EN:
                                    return true;
                                default:
                                    return false;
                            }
                        }
                        default:
                            return false;
                    }
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    public List<YvsGrhTrancheHoraire> loadTranche(YvsUsers u, YvsBaseDepots y, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date debut = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 2);
        Date fin = cal.getTime();

        String nameQueri = "YvsComCreneauHoraireUsers.findTrancheByDepotDates";
        String[] champ = new String[]{"depot", "debut", "fin"};
        Object[] val = new Object[]{y, debut, fin};
        if (u != null ? u.getId() > 0 : false) {
            nameQueri = "YvsComCreneauHoraireUsers.findTrancheByUsersDepotDates";
            champ = new String[]{"users", "depot", "debut", "fin"};
            val = new Object[]{u, y, debut, fin};
        }
        List<YvsGrhTrancheHoraire> l = dao.loadNameQueries(nameQueri, champ, val);
        return l != null ? l : new ArrayList<YvsGrhTrancheHoraire>();
    }

    public List<YvsGrhTrancheHoraire> loadTranches(YvsBaseDepots y, Date date) {
        return loadTranche(null, y, date);
    }

    public List<YvsGrhTrancheHoraire> loadTranches(YvsUsers u, YvsBaseDepots y, Date date) {
        String jour = getDay(date);
        String nameQueri = "YvsComCreneauDepot.findTrancheCurrentByDepot";
        String[] champ = new String[]{"depot", "jour"};
        Object[] val = new Object[]{y, jour};
        if (u != null ? u.getId() > 0 : false) {
            nameQueri = "YvsComCreneauHoraireUsers.findTrancheDepotByUsersDateDepot";
            champ = new String[]{"users", "depot", "date"};
            val = new Object[]{u, y, date};
        }
        List<YvsGrhTrancheHoraire> l = dao.loadNameQueries(nameQueri, champ, val);
        return l != null ? l : new ArrayList<YvsGrhTrancheHoraire>();
    }

    public String getDay(Date jour) {
        return getDay(dateToCalendar(jour));
    }

    public Calendar dateToCalendar(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(date);
            return cal;
        }
        return Calendar.getInstance();
    }

    public Date addDay(Date date, int day) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(date);
            cal.add(Calendar.DATE, day);
            return cal.getTime();
        }
        return new Date();
    }

    public Date dateTimeWithOutSecond(Date date) {
        try {
            Calendar c_date = dateToCalendar(date);
            c_date.set(Calendar.SECOND, 0);
            c_date.set(Calendar.MILLISECOND, 0);
            return c_date.getTime();
        } catch (Exception ex) {
            Logger.getLogger(AbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public Date timestampToDate(Date date) {
        try {
            Calendar c_date = dateToCalendar(date);
            c_date.set(Calendar.HOUR_OF_DAY, 0);
            c_date.set(Calendar.MINUTE, 0);
            c_date.set(Calendar.SECOND, 0);
            c_date.set(Calendar.MILLISECOND, 0);
            return c_date.getTime();
        } catch (Exception ex) {
            Logger.getLogger(AbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public Date datesToTimestamp(Date date, Date heure) {
        try {
            Calendar c_date = dateToCalendar(date);
            if (heure != null) {
                Calendar c_heure = dateToCalendar(heure);
                c_date.set(Calendar.HOUR_OF_DAY, c_heure.get(Calendar.HOUR_OF_DAY));
                c_date.set(Calendar.MINUTE, c_heure.get(Calendar.MINUTE));
                c_date.set(Calendar.SECOND, c_heure.get(Calendar.SECOND));
            }
            return c_date.getTime();
        } catch (Exception ex) {
            Logger.getLogger(AbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public Date addTimeToTimestamp(Date date, Date heure) {
        try {
            Calendar c_date = dateToCalendar(date);
            if (heure != null) {
                Calendar c_heure = dateToCalendar(heure);
                c_date.add(Calendar.HOUR_OF_DAY, c_heure.get(Calendar.HOUR_OF_DAY));
                c_date.add(Calendar.MINUTE, c_heure.get(Calendar.MINUTE));
                c_date.add(Calendar.SECOND, c_heure.get(Calendar.SECOND));
            }
            return c_date.getTime();
        } catch (Exception ex) {
            Logger.getLogger(AbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public Date removeTimeToTimestamp(Date date, Date heure) {
        try {
            Calendar c_date = dateToCalendar(date);
            if (heure != null) {
                Calendar c_heure = dateToCalendar(heure);
                c_date.add(Calendar.HOUR_OF_DAY, -c_heure.get(Calendar.HOUR_OF_DAY));
                c_date.add(Calendar.MINUTE, -c_heure.get(Calendar.MINUTE));
                c_date.add(Calendar.SECOND, -c_heure.get(Calendar.SECOND));
            }
            return c_date.getTime();
        } catch (Exception ex) {
            Logger.getLogger(AbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public Date definedTimestamp(Date date, Date heure) {
        try {
            Calendar debut = dateToCalendar(date);
            Calendar fin = dateToCalendar(datesToTimestamp(date, heure));
            if (!fin.after(debut)) {
                fin.add(Calendar.DATE, 1);
            }
            return fin.getTime();
        } catch (Exception ex) {
            Logger.getLogger(AbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public boolean verifyDateHeure(YvsGrhPlanningEmploye planning, Date heure) {
        try {
            if (planning != null) {
                Date timeMargeAvance = (currentParamGrh != null ? (currentParamGrh.getTimeMargeAvance() != null ? currentParamGrh.getTimeMargeAvance() : Util.getHeure(1, 0, 0)) : Util.getHeure(1, 0, 0));
                Date heure_debut = dateTimeWithOutSecond(datesToTimestamp(timestampToDate(planning.getDateDebut()), planning.getHeureDebut()));
                Date heure_fin = dateTimeWithOutSecond(datesToTimestamp(timestampToDate(planning.getDateFin()), planning.getHeureFin()));

                heure_debut = removeTimeToTimestamp(heure_debut, timeMargeAvance);
                heure_fin = addTimeToTimestamp(heure_fin, timeMargeAvance);
                if (!heure_debut.after(heure) && !heure.after(heure_fin)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String getDay(Calendar jour) {
        int d = jour.get(Calendar.DAY_OF_WEEK);
        String str = "";
        if (d == Calendar.MONDAY) {
            str = "Lundi";
        } else if (d == Calendar.TUESDAY) {
            str = "Mardi";
        } else if (d == Calendar.WEDNESDAY) {
            str = "Mercredi";
        } else if (d == Calendar.THURSDAY) {
            str = "Jeudi";
        } else if (d == Calendar.FRIDAY) {
            str = "Vendredi";
        } else if (d == Calendar.SATURDAY) {
            str = "Samedi";
        } else if (d == Calendar.SUNDAY) {
            str = "Dimanche";
        }
        return str;
    }

    public static String getDay(int day) {
        switch (day) {
            case 1:
                return "Dimanche";
            case 2:
                return "Lundi";
            case 3:
                return "Mardi";
            case 4:
                return "Mercredi";
            case 5:
                return "Jeudi";
            case 6:
                return "Vendredi";
            case 7:
                return "Samedi";
            default:
                return "";
        }
    }

    public boolean verifyInventaire(YvsBaseDepots depot, YvsGrhTrancheHoraire tranche, Date date) {
        return controleInventaire(depot.getId(), date, tranche.getId());
    }

    public boolean controleInventaire(long depot, Date date, long tranche) {
        if (!dao.controleInventaire(depot, date, tranche)) {
//            getErrorMessage("Vous ne pouvez créer une fiche de stock à cette date car un ou plusieurs inventaires ont déjà été réalisés après dans ce dépot");
            return false;
        }
        return true;
    }

    public boolean verifyTranche(YvsGrhTrancheHoraire tranche, YvsBaseDepots depot, Date date) {
        if (date == null) {
//            getErrorMessage("Vous devez preciser le date de traitement");
            return false;
        }
        if (depot != null ? depot.getId() < 1 : true) {
//            getErrorMessage("Vous devez preciser le dépôt");
            return false;
        }
        if (tranche != null ? tranche.getId() > 0 : false) {
            Long y = (Long) dao.loadOneByNameQueries("YvsBaseMouvementStock.countMvtOtherTypeJ", new String[]{"depot", "dateDoc", "typeJ"},
                    new Object[]{depot, date, tranche.getTypeJournee()});
            if (y != null ? y > 0 : false) {
//                getErrorMessage("Vous ne pouvez pas enregistrer ce document à cette tranche", "car le dernier document est dans un autre type de tranche");
                return false;
            }
        }
        return true;
    }

}

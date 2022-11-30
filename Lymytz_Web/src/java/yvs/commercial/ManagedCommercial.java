/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.commercial.achat.DocAchat;
import yvs.commercial.client.Client;
import yvs.commercial.depot.ManagedDepot;
import yvs.commercial.depot.ManagedPointVente;
import yvs.commercial.vente.DocVente;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseArticleCategorieComptableTaxe;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepotOperation;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.YvsComParametreAchat;
import yvs.entity.commercial.YvsComParametreStock;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComCoutSupDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComTaxeContenuAchat;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsComCategorieTarifaire;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.commercial.rrr.YvsComGrilleRabais;
import yvs.entity.commercial.rrr.YvsComRabais;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComCoutSupDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.commercial.vente.YvsComTaxeContenuVente;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsers;
import yvs.parametrage.entrepot.Depots;
import yvs.users.Acces;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 * @param <T>
 * @param <S>
 */
public abstract class ManagedCommercial<T extends Serializable, S extends Serializable> extends Managed<T, S> {

    PaginatorResult<YvsComEnteteDocVente> p_entete = new PaginatorResult<>();

    public List<YvsBasePlanComptable> comptes = new ArrayList<>();
    public List<YvsBasePlanComptable> comptesResult = new ArrayList<>();

    public List<YvsUsers> users = new ArrayList<>();
    public List<YvsUsers> usersResult = new ArrayList<>();

    public long societe_, exercice_, point_, creneau_, tranche_, users_, entete_, facture_, depot_, zone;
    public boolean date_, date_up;
    public Boolean cloturer_, actif_, livrer_, entree_;
    public boolean solder_;
    public Date dateDebut_ = new Date(), dateFin_ = new Date();
    public String numSearch_, indiceNumsearch_, codeVendeur_, codeLivreur_, codeFsseur_, codeClient_, statutRegle_, statutLivre_, otherSearch_;

    public static YvsComParametre currentParam;
    public static YvsComParametreVente currentParamVente;
    public static YvsComParametreAchat currentParamAchat;
    public static YvsComParametreStock currentParamStock;

    @Override
    public abstract boolean controleFiche(T bean);

    @Override
    public abstract void deleteBean();

    @Override
    public void updateBean() {

    }

    @Override
    public T recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(T bean) {
    }

    @Override
    public abstract void loadOnView(SelectEvent ev);

    @Override
    public abstract void unLoadOnView(UnselectEvent ev);

    @Override
    public abstract void loadAll();

    public YvsComParametre getCurrentParam() {
        return currentParam;
    }

    public void setCurrentParam(YvsComParametre currentParam) {
        this.currentParam = currentParam;
    }

    public String getCodeFsseur_() {
        return codeFsseur_;
    }

    public void setCodeFsseur_(String codeFsseur_) {
        this.codeFsseur_ = codeFsseur_;
    }

    public String getOtherSearch_() {
        return otherSearch_;
    }

    public void setOtherSearch_(String otherSearch_) {
        this.otherSearch_ = otherSearch_;
    }

    public String getCodeLivreur_() {
        return codeLivreur_;
    }

    public void setCodeLivreur_(String codeLivreur_) {
        this.codeLivreur_ = codeLivreur_;
    }

    public String getCodeClient_() {
        return codeClient_;
    }

    public String getStatutRegle_() {
        return statutRegle_;
    }

    public void setStatutRegle_(String statutRegle_) {
        this.statutRegle_ = statutRegle_;
    }

    public String getStatutLivre_() {
        return statutLivre_;
    }

    public void setStatutLivre_(String statutLivre_) {
        this.statutLivre_ = statutLivre_;
    }

    public void setCodeClient_(String codeClient_) {
        this.codeClient_ = codeClient_;
    }

    public String getCodeVendeur_() {
        return codeVendeur_;
    }

    public void setCodeVendeur_(String codeVendeur_) {
        this.codeVendeur_ = codeVendeur_;
    }

    public List<YvsUsers> getUsersResult() {
        return usersResult;
    }

    public void setUsersResult(List<YvsUsers> usersResult) {
        this.usersResult = usersResult;
    }

    public long getFacture_() {
        return facture_;
    }

    public void setFacture_(long facture_) {
        this.facture_ = facture_;
    }

    public List<YvsUsers> getUsers() {
        return users;
    }

    public void setUsers(List<YvsUsers> users) {
        this.users = users;
    }

    public List<YvsBasePlanComptable> getComptes() {
        return comptes;
    }

    public void setComptes(List<YvsBasePlanComptable> comptes) {
        this.comptes = comptes;
    }

    public List<YvsBasePlanComptable> getComptesResult() {
        return comptesResult;
    }

    public void setComptesResult(List<YvsBasePlanComptable> comptesResult) {
        this.comptesResult = comptesResult;
    }

    public long getSociete_() {
        return societe_;
    }

    public void setSociete_(long societe_) {
        this.societe_ = societe_;
    }

    public long getExercice_() {
        return exercice_;
    }

    public void setExercice_(long exercice_) {
        this.exercice_ = exercice_;
    }

    public long getPoint_() {
        return point_;
    }

    public void setPoint_(long point_) {
        this.point_ = point_;
    }

    public long getDepot_() {
        return depot_;
    }

    public void setDepot_(long depot_) {
        this.depot_ = depot_;
    }

    public long getCreneau_() {
        return creneau_;
    }

    public void setCreneau_(long creneau_) {
        this.creneau_ = creneau_;
    }

    public long getTranche_() {
        return tranche_;
    }

    public void setTranche_(long tranche_) {
        this.tranche_ = tranche_;
    }

    public long getUsers_() {
        return users_;
    }

    public void setUsers_(long users_) {
        this.users_ = users_;
    }

    public long getEntete_() {
        return entete_;
    }

    public void setEntete_(long entete_) {
        this.entete_ = entete_;
    }

    public boolean isDate_() {
        return date_;
    }

    public void setDate_(boolean date_) {
        this.date_ = date_;
    }

    public boolean isDate_up() {
        return date_up;
    }

    public void setDate_up(boolean date_up) {
        this.date_up = date_up;
    }

    public Boolean getCloturer_() {
        return cloturer_;
    }

    public void setCloturer_(Boolean cloturer_) {
        this.cloturer_ = cloturer_;
    }

    public Boolean getActif_() {
        return actif_;
    }

    public void setActif_(Boolean actif_) {
        this.actif_ = actif_;
    }

    public Boolean getLivrer_() {
        return livrer_;
    }

    public void setLivrer_(Boolean livrer_) {
        this.livrer_ = livrer_;
    }

    public Boolean getEntree_() {
        return entree_;
    }

    public void setEntree_(Boolean entree_) {
        this.entree_ = entree_;
    }

    public boolean isSolder_() {
        return solder_;
    }

    public void setSolder_(boolean solder_) {
        this.solder_ = solder_;
    }

    public Date getDateDebut_() {
        return dateDebut_;
    }

    public void setDateDebut_(Date dateDebut_) {
        this.dateDebut_ = dateDebut_;
    }

    public Date getDateFin_() {
        return dateFin_;
    }

    public void setDateFin_(Date dateFin_) {
        this.dateFin_ = dateFin_;
    }

    public String getNumSearch_() {
        return numSearch_;
    }

    public void setNumSearch_(String numSearch_) {
        this.numSearch_ = numSearch_;
    }

    public String getIndiceNumsearch_() {
        return indiceNumsearch_;
    }

    public void setIndiceNumsearch_(String indiceNumsearch_) {
        this.indiceNumsearch_ = indiceNumsearch_;
    }

    public long getZone() {
        return zone;
    }

    public void setZone(long zone) {
        this.zone = zone;
    }

    private List<YvsComptaPiecesComptable> contentCompta = new ArrayList<>();

    @Override
    public List<YvsComptaPiecesComptable> getContentCompta() {
        return contentCompta;
    }

    @Override
    public void setContentCompta(List<YvsComptaPiecesComptable> contentCompta) {
        this.contentCompta = contentCompta;
    }

    public void doNothing(String load) {

    }

    public int buildDocByDroit(String type) {
        type = type != null ? type.trim().length() > 0 ? type : "FV" : "FV";
        switch (type) {
            case Constantes.TYPE_FAA:
            case Constantes.TYPE_FA: {
                if (autoriser("fa_view_all_doc")) {
                    return 1;
                } else if (autoriser("fa_view_only_doc_agence")) {
                    return 2;
                } else if (autoriser("fa_view_only_doc_depot")) {
                    return 3;
                } else {
                    return 4;
                }
            }
            case Constantes.TYPE_BRA:
            case Constantes.TYPE_BLA: {
                if (autoriser("bla_view_all_doc")) {
                    return 1;
                } else if (autoriser("bla_view_only_doc_agence")) {
                    return 2;
                } else if (autoriser("bla_view_only_doc_depot")) {
                    return 3;
                } else {
                    return 4;
                }
            }
            case Constantes.TYPE_BCV: {
                if (autoriser("bcv_view_all_doc")) {
                    return 1;
                } else if (autoriser("bcv_view_only_doc_agence")) {
                    return 2;
                } else if (autoriser("bcv_view_only_doc_pv")) {
                    return 3;
                } else if (autoriser("bcv_view_only_doc_depot")) {
                    return 4;
                } else {
                    return 5;
                }
            }
            case Constantes.TYPE_FV: {
                if (autoriser("fv_view_all_doc")) {
                    return 1;
                } else if (autoriser("fv_view_only_doc_agence")) {
                    return 2;
                } else if (autoriser("fv_view_only_doc_pv")) {
                    return 3;
                } else if (autoriser("fv_view_only_doc_depot")) {
                    return 4;
                } else {
                    return 5;
                }
            }
            case Constantes.TYPE_BLV: {
                if (autoriser("blv_view_all_doc")) {
                    return 1;
                } else if (autoriser("blv_view_only_doc_agence")) {
                    return 2;
                } else if (autoriser("blv_view_only_doc_depot")) {
                    return 3;
                } else {
                    return 4;
                }
            }
            case Constantes.TYPE_SS:
            case Constantes.TYPE_IN:
            case Constantes.TYPE_RA:
            case Constantes.TYPE_ES: {
                if (autoriser("d_stock_view_all_agence")) {
                    return 1;
                } else if (autoriser("d_stock_view_all_depot")) {
                    return 2;
                } else if (autoriser("d_stock_view_all_date")) {
                    return 3;
                } else {
                    return 4;
                }
            }
            case Constantes.TYPE_FT: {
                if (autoriser("tr_view_all")) {
                    return 1;
                } else if (autoriser("tr_view_all_user")) {
                    return 2;
                } else if (autoriser("tr_view_historique")) {
                    return 3;
                } else {
                    return 4;
                }
            }
            default:
                return -1;
        }
    }

    public int buildDocByDroit(PaginatorResult paginator, String type, boolean contenu, boolean todo) {
        controlListAgence();
//        currentDepot = (currentDepot != null) ? currentDepot : ((YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findByResponsable", new String[]{"responsable"}, new Object[]{(currentUser != null) ? currentUser.getUsers().getEmploye() : null}));
        if (!autoriser("tr_view_all")) {
            if (!autoriser("tr_view_historique")) {
                //retriction aux seules transfert du jour
                dateDebut_ = new Date();
                dateFin_ = new Date();
                if (currentCreneauDepot != null ? currentCreneauDepot.getId() > 0 : false) {
                    Date debut = Util.addTimeInDate(dateDebut_, currentCreneauDepot.getTranche().getHeureDebut());
                    Date fin = Util.addTimeInDate(dateFin_, currentCreneauDepot.getTranche().getHeureFin());
                    if (debut.after(fin)) {
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DATE, -1);
                        dateDebut_ = c.getTime();
                    }
                }
            } else {
                // ajoute le paramètre de restriction temporelle
                YvsComParametreStock ps = (YvsComParametreStock) dao.loadOneByNameQueries("YvsComParametreStock.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
                int limit = (ps != null) ? ((ps.getJourAnterieur() != null) ? ps.getJourAnterieur() : 0) : 0;
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, -limit);
                dateDebut_ = (dateDebut_ != null) ? ((dateDebut_.after(c.getTime()) || dateDebut_.equals(c.getTime())) ? dateDebut_ : c.getTime()) : c.getTime();
                dateFin_ = (dateFin_ != null) ? ((dateFin_.before(new Date()) || dateFin_.equals(new Date())) ? dateFin_ : c.getTime()) : new Date();
            }
            ParametreRequete p = new ParametreRequete(null, "date", dateDebut_, dateFin_, "BETWEEN", "AND");
            p.getOtherExpression().add(new ParametreRequete((!contenu ? "y.dateDoc" : "y.docStock.dateDoc"), "dateX", dateDebut_, dateFin_, "BETWEEN", "OR"));
            p.getOtherExpression().add(new ParametreRequete((!contenu ? "y.statut" : "y.docStock.statut"), "statutX", Constantes.ETAT_VALIDE, "!=", "OR"));
            paginator.addParam(p);
        } else if (type.equals(Constantes.TYPE_FT)) {
            ParametreRequete p = new ParametreRequete(null, "agences", listIdAgences, "IN", "AND");
            p.getOtherExpression().add(new ParametreRequete((!contenu ? "y.destination.agence.id" : "y.docStock.destination.agence.id"), "agences", listIdAgences, "IN", "OR"));
            p.getOtherExpression().add(new ParametreRequete((!contenu ? "y.source.agence.id" : "y.docStock.source.agence.id"), "agences", listIdAgences, "IN", "OR"));
            paginator.addParam(p);
        }
        if (!autoriser("d_stock_view_all_agence")) {
            // ajoute le paramètre de restriction par agence
            switch (type) {
                case Constantes.TYPE_ES:
                    paginator.addParam(new ParametreRequete((!contenu ? "y.destination.agence.id" : "y.docStock.destination.agence.id"), "agences", listIdAgences, "IN", "AND"));
                    break;
                case Constantes.TYPE_SS:
                    paginator.addParam(new ParametreRequete((!contenu ? "y.source.agence.id" : "y.docStock.source.agence.id"), "agences", listIdAgences, "IN", "AND"));
                    break;
            }
        }
        List<Long> ids;
//        if (!autoriser("tr_view_all_user")) {
//            // ajoute le paramètre de restriction utilisateur  et dépôts    
//            ParametreRequete p = new ParametreRequete(null, "depots", "X", "=", "AND");
//            if (!todo && !type.equals(Constantes.TYPE_ES)) {
//                ParametreRequete p0 = new ParametreRequete(null, "source", "X", "=", "OR");
//                ParametreRequete p00 = new ParametreRequete(null, "source", "X", "=", "OR");
//                p00.getOtherExpression().add(new ParametreRequete((!contenu ? "y.source" : "y.docStock.source"), "source", "X", "IS NOT NULL", "AND"));
//                p00.getOtherExpression().add(new ParametreRequete((!contenu ? "y.source" : "y.docStock.source"), "source", currentDepot, "=", "AND"));
//                p0.getOtherExpression().add(p00);
//                if (currentUser.getUsers().getEmploye() != null) {
//                    p0.getOtherExpression().add(new ParametreRequete((!contenu ? "y.source.responsable" : "y.docStock.source.responsable"), "responsable", currentUser.getUsers().getEmploye(), "=", "AND"));
//                }
//                p.getOtherExpression().add(p0);
//            }
//            if (!type.equals(Constantes.TYPE_SS)) {
//                ParametreRequete p1 = new ParametreRequete(null, "destination", "X", "=", "OR");
//                ParametreRequete p11 = new ParametreRequete(null, "destination", "X", "=", "OR");
//                p11.getOtherExpression().add(new ParametreRequete((!contenu ? "y.destination" : "y.docStock.destination"), "destination", "X", "IS NOT NULL", "AND"));
//                p11.getOtherExpression().add(new ParametreRequete((!contenu ? "y.destination" : "y.docStock.destination"), "destination", currentDepot, "=", "AND"));
//                p1.getOtherExpression().add(p11);
//                if (currentUser.getUsers().getEmploye() != null) {
//                    p1.getOtherExpression().add(new ParametreRequete((!contenu ? "y.destination.responsable" : "y.docStock.destination.responsable"), "responsable1", currentUser.getUsers().getEmploye(), "=", "AND"));
//                }
//                p.getOtherExpression().add(p1);
//            }
//            if (!p.getOtherExpression().isEmpty()) {
//                paginator.addParam(p);
//            }
//        }
        if (!autoriser("d_stock_view_all_depot")) {
            ids = getIdDepots();
            // ajoute le paramètre de restriction aux dépôts où l'utilisateur est planifié ou bien là où il est responsable                                  
            ParametreRequete p = new ParametreRequete(null, "source", "X", "=", "AND");
            ParametreRequete p0 = new ParametreRequete(null, "source", "X", "=", "OR");
            p0.getOtherExpression().add(new ParametreRequete((!contenu ? "y.source" : "y.docStock.source"), "source", "X", "IS NOT NULL", "AND"));
            p0.getOtherExpression().add(new ParametreRequete((!contenu ? "y.source.id" : "y.docStock.source.id"), "ids", ids, " IN ", "AND"));
            ParametreRequete p1 = new ParametreRequete(null, "destination", "X", "=", "OR");
            p1.getOtherExpression().add(new ParametreRequete((!contenu ? "y.destination" : "y.docStock.destination"), "destination", "x", "IS NOT NULL", "AND"));
            p1.getOtherExpression().add(new ParametreRequete((!contenu ? "y.destination.id" : "y.docStock.destination.id"), "idsd", ids, " IN ", "AND"));
            p1.getOtherExpression().add(new ParametreRequete((!contenu ? "y.statut" : "y.docStock.statut"), "_statut_", Constantes.ETAT_EDITABLE, "!=", "AND"));
            p.getOtherExpression().add(p0);
            p.getOtherExpression().add(p1);
            paginator.addParam(p);
        }
        return 0;
    }

    private List<Long> getIdDepots() {
        List<Long> re = new ArrayList<>();
        ManagedDepot service = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (service != null) {
            if (service.getDepots() != null) {
                for (YvsBaseDepots b : service.getDepots()) {
                    re.add(b.getId());
                }
            }
        }
        if (re.isEmpty()) {//pour éviter une erreur dans la requête
            re.add(-1L);
        }
        return re;
    }
    /*
     DEBUT OTHERS MANAGE
     */

    public void loadComptes() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsBasePlanComptable.findTrue";
        comptes = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void searchComptess(String num) {
        champ = new String[]{"societe", "numCompte"};
        val = new Object[]{currentAgence.getSociete(), num + "%"};
        nameQueri = "YvsBasePlanComptable.findLikeNumActif";
        comptesResult = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadUsers() {
        champ = new String[]{"societe", "actif"};
        val = new Object[]{currentAgence.getSociete(), true};
        nameQueri = "YvsUsers.findByActif";
        users = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void searchUsers(String num) {
        champ = new String[]{"societe", "codeUsers"};
        val = new Object[]{currentAgence.getSociete(), num + "%"};
        nameQueri = "YvsUsers.findLikeCodeActif";
        usersResult = dao.loadNameQueries(nameQueri, champ, val);
    }

    public boolean verifyOperation(Depots d, String type, String operation, boolean error) {
        if (d != null ? d.getId() > 0 : false) {
            if (!checkOperationDepot(d.getId(), type, operation)) {
                if (error) {
                    getErrorMessage("Le depot '" + d.getDesignation() + "' n'est pas paramètré pour les opérations '" + operation + "' en '" + type + "'");
                } else {
                    getWarningMessage("Le depot '" + d.getDesignation() + "' n'est pas paramètré pour les opérations '" + operation + "' en '" + type + "'");
                }
                return false;
            }
        }
        return true;
    }

    public boolean checkOperationDepot(long depot, String type) {
        return checkOperationDepot(depot, type, dao);
    }

    public boolean checkOperationDepot(long depot, String type, DaoInterfaceLocal dao) {
        champ = new String[]{"depot", "type"};
        val = new Object[]{new YvsBaseDepots(depot), type};
        nameQueri = "YvsBaseDepotOperation.findByDepotType";
        List<YvsBaseDepotOperation> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        return l != null ? !l.isEmpty() : false;
    }

    public boolean checkOperationDepot(long depot, String type, String operation) {
        champ = new String[]{"depot", "type", "operation"};
        val = new Object[]{new YvsBaseDepots(depot), type, operation};
        nameQueri = "YvsBaseDepotOperation.cByDepotTypeOperation";
        Long N = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
//        List<YvsBaseDepotOperation> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
//        return l != null ? !l.isEmpty() : false;
        return (N != null) ? N > 0 : false;
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

    public List<YvsBaseDepots> loadDestination(YvsBaseDepots y) {
        nameQueri = "YvsComLiaisonDepot.findDepotLierByDepot";
        champ = new String[]{"depot"};
        val = new Object[]{y};
        return dao.loadNameQueries(nameQueri, champ, val);
    }

    public List<YvsComCreneauDepot> loadCreneaux(YvsBaseDepots y, Date date) {
        String jour = Util.getDay(date);
        nameQueri = "YvsComCreneauDepot.findByJourDepot_";
        champ = new String[]{"depot", "jour"};
        val = new Object[]{y, jour};
        List<YvsComCreneauDepot> l = dao.loadNameQueries(nameQueri, champ, val);

        nameQueri = "YvsComCreneauDepot.findByDepotPermanent";
        champ = new String[]{"depot", "permanent"};
        val = new Object[]{y, true};
        List<YvsComCreneauDepot> l_ = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsComCreneauDepot t : l_) {
            if (!l.contains(t)) {
                l.add(t);
            }
        }
        return l != null ? l : new ArrayList<YvsComCreneauDepot>();
    }

    public List<YvsGrhTrancheHoraire> loadTranche(YvsBasePointVente y, Date date) {
        String jour = Util.getDay(date);
        nameQueri = "YvsComCreneauPoint.findTrancheByJourDepot_";
        champ = new String[]{"point", "jour"};
        val = new Object[]{y, jour};
        List<YvsGrhTrancheHoraire> l = dao.loadNameQueries(nameQueri, champ, val);

        nameQueri = "YvsComCreneauPoint.findTrancheByDepotPermanent";
        champ = new String[]{"point", "permanent"};
        val = new Object[]{y, true};
        List<YvsGrhTrancheHoraire> l_ = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsGrhTrancheHoraire t : l_) {
            if (!l.contains(t)) {
                l.add(t);
            }
        }
        return l != null ? l : new ArrayList<YvsGrhTrancheHoraire>();
    }

    public List<YvsComCreneauPoint> loadCreneaux(YvsBasePointVente y, Date date) {
        String jour = Util.getDay(date);
        nameQueri = "YvsComCreneauPoint.findByJourDepot_";
        champ = new String[]{"point", "jour"};
        val = new Object[]{y, jour};
        List<YvsComCreneauPoint> l = dao.loadNameQueries(nameQueri, champ, val);

        nameQueri = "YvsComCreneauPoint.findByDepotPermanent";
        champ = new String[]{"point", "permanent"};
        val = new Object[]{y, true};
        List<YvsComCreneauPoint> l_ = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsComCreneauPoint t : l_) {
            if (!l.contains(t)) {
                l.add(t);
            }
        }
        return l != null ? l : new ArrayList<YvsComCreneauPoint>();
    }

    public List<YvsComContenuDocAchat> loadContenus(YvsComDocAchats y) {
        nameQueri = "YvsComContenuDocAchat.findByDocAchat";
        champ = new String[]{"docAchat"};
        val = new Object[]{y};
        return dao.loadNameQueries(nameQueri, champ, val);
    }

    public List<YvsComCoutSupDocAchat> loadCouts(YvsComDocAchats y) {
        nameQueri = "YvsComCoutSupDocAchat.findByDocAchat";
        champ = new String[]{"docAchat"};
        val = new Object[]{y};
        return dao.loadNameQueries(nameQueri, champ, val);
    }

    public List<YvsComContenuDocVente> loadContenus(YvsComDocVentes y) {
        nameQueri = "YvsComContenuDocVente.findByDocVente";
        champ = new String[]{"docVente"};
        val = new Object[]{y};
        return dao.loadNameQueries(nameQueri, champ, val);
    }

    public List<YvsComCoutSupDocVente> loadCouts(YvsComDocVentes y) {
        nameQueri = "YvsComCoutSupDocVente.findByDocVente";
        champ = new String[]{"docVente"};
        val = new Object[]{y};
        return dao.loadNameQueries(nameQueri, champ, val);
    }

    public boolean verifyTransfert() {
        return verifyTransfert(currentDepot, currentCreneauDepot);
    }

    public boolean verifyTransfert(YvsBaseDepots depot) {
        return verifyTransfert(depot, currentCreneauDepot);
    }

    public boolean verifyTransfert(YvsBaseDepots depot, YvsComCreneauDepot tranche) {
        if (depot != null) {
            champ = new String[]{"source", "statut", "creneauSource", "typeDoc"};
            val = new Object[]{depot, Constantes.ETAT_SOUMIS, tranche, Constantes.TYPE_FT};
            List<YvsComDocStocks> l = dao.loadNameQueries("YvsComDocStocks.findBySourceCreneauSourceStatut", champ, val);
            return l != null ? l.isEmpty() : true;
        }
        return true;
    }

    /*
     FIN OTHERS MANAGE
     */

    /*
     DEBUT REQUETE DYNAMIQUE VENTE
     */
    public YvsComClient currentClientDefault() {
        champ = new String[]{"societe", "ville"};
        val = new Object[]{currentAgence.getSociete(), currentAgence.getVille()};
        nameQueri = "YvsComClient.findDefautVille";
        YvsComClient client = (YvsComClient) dao.loadOneByNameQueries(nameQueri, champ, val);
        if (client != null ? client.getId() > 0 : false) {
            return client;
        }
        nameQueri = "YvsComClient.findByDefautOrVille";
        client = (YvsComClient) dao.loadOneByNameQueries(nameQueri, champ, val);
        if (client != null ? client.getId() > 0 : false) {
            return client;
        }
        getWarningMessage("Pensez à définir un client par défaut pour cette agence");
        return null;
    }

    public YvsBaseFournisseur currentFournisseurDefault() {
        if (currentAgence.getVille() != null) {
            if (currentAgence.getVille().getParent() != null) {
                champ = new String[]{"societe", "pays"};
                val = new Object[]{currentAgence.getSociete(), currentAgence.getVille().getParent()};
                nameQueri = "YvsBaseFournisseur.findDefautPays";
            } else {
                champ = new String[]{"societe", "ville"};
                val = new Object[]{currentAgence.getSociete(), currentAgence.getVille()};
                nameQueri = "YvsBaseFournisseur.findDefautVille";
            }
            YvsBaseFournisseur lc = (YvsBaseFournisseur) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (lc != null) {
                return lc;
            }
        }
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsBaseFournisseur.findDefaut";
        YvsBaseFournisseur lc = (YvsBaseFournisseur) dao.loadOneByNameQueries(nameQueri, champ, val);
        if (lc != null) {
            getWarningMessage("Pensez à définir un fournisseur par défaut pour cette agence");
            return lc;
        }
        return null;
    }

    /*initialise les éléments de la zone de recherche des factures*/
    public void _load() {
        Acces acces = (Acces) giveManagedBean(Acces.class);
        societe_ = currentAgence.getSociete().getId();
        if (acces.isMultiAgence()) {
            _loadAgence();
        } else {
            agence_ = currentAgence.getId();
        }
//        _loadDepot();
        _loadPoint();
        _loadUsers();
        _loadTrancheDepot();

    }

    public void _loadSociete() {
//        champ = new String[]{};
//        val = new Object[]{};
//        societes_ = dao.loadNameQueries("YvsSocietes.findAll", champ, val);
    }

    public void _loadAgence() {
        champ = new String[]{"societe", "users"};
        val = new Object[]{new YvsSocietes(societe_), currentUser.getUsers()};
        agences_ = dao.loadNameQueries("YvsUsersAgence.findAgenceByUsersSociete", champ, val);
        if (!agences_.contains(currentAgence)) {
            agences_.add(currentAgence);
        }
    }

    public void _loadUsers() {
        champ = new String[]{"agence", "actif"};
        val = new Object[]{new YvsAgences(agence_), true};
        userss_ = dao.loadNameQueries("YvsUsers.findByAgence", champ, val);
    }

    public void _loadDepot() {
        ManagedDepot w = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (w != null) {
            w.onSelectAgenceForFind(agence_);
        }
    }

    public void _loadPoint() {
        ManagedPointVente w = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
        if (w != null) {
            w.onSelectAgenceForFind(agence_);
        }
    }

    public void _loadTrancheDepot() {
        tranches_point_ = loadTranche(new YvsBaseDepots(depot_), dateDebut_);
    }

    public void _loadTranchePoint() {
        tranches_point_ = loadTranche(new YvsBasePointVente(point_), dateDebut_);
    }

    public void _chooseSociete() {
        agence_ = 0;
        _loadAgence();

        users_ = 0;
        userss_.clear();

        depot_ = 0;
        depots_.clear();

        point_ = 0;
        points_.clear();

        tranche_ = 0;
        tranches_point_.clear();
    }

    public void _chooseAgence() {
        users_ = 0;
        _loadUsers();

        point_ = 0;
        _loadPoint();

        depot_ = 0;
        _loadDepot();

        tranche_ = 0;
        tranches_point_.clear();

        entete_ = 0;
    }

    public void _chooseDepot() {
        tranche_ = 0;
        _loadTrancheDepot();
    }

    public void _choosePoint() {
        tranche_ = 0;
        if (point_ > 0) {
            _loadTranchePoint();
        } else {
            boolean pagine = false;
            boolean next = false;
            if (point_ == -1) {
                pagine = true;
                next = false;
            } else if (point_ == -2) {
                pagine = true;
                next = true;
            }
            if (pagine) {
                ManagedPointVente m = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                if (m != null) {
                    m.loadPointVenteByDroit(next, false);
                }
            }
            point_ = 0;
        }
    }

    public YvsComEnteteDocVente returnEntete(YvsUsers u, YvsBaseDepots d, YvsBasePointVente p, YvsGrhTrancheHoraire t, Date date) {
        try {
            if (p != null ? p.getId() > 0 : false) {
                nameQueri = "YvsComEnteteDocVente.findByDepotPointTrancheUsersDate";
                champ = new String[]{"depot", "point", "tranche", "users", "date"};
                val = new Object[]{d, p, t, u, date};
            } else {
                nameQueri = "YvsComEnteteDocVente.findByDepotTrancheUsersDate";
                champ = new String[]{"depot", "tranche", "users", "date"};
                val = new Object[]{d, t, u, date};
            }
            List<YvsComEnteteDocVente> le = dao.loadNameQueries(nameQueri, champ, val);
            if (le != null ? !le.isEmpty() : false) {
                return le.get(0);
            }
            return null;
        } catch (Exception ex) {
            getErrorMessage("Création Journal de vente impossible");
            System.err.println("Error " + ex.getMessage());
            return null;
        }
    }

    public List<S> loadVentes(String type, boolean avance, boolean init) {
        ParametreRequete p;
        if (currentUser.getUsers().getAccesMultiAgence()) {
            p = new ParametreRequete("enteteDoc.creneau.creneauDepot.depot.agence.societe", "societe", currentAgence.getSociete());
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            if (currentUser.getUsers().getSuperAdmin()) {
                p = new ParametreRequete("enteteDoc.creneau.creneauDepot.depot.agence", "agence", currentAgence);
                p.setOperation("=");
                p.setPredicat("AND");
            } else {
                p = new ParametreRequete("enteteDoc.creneau.users", "users", currentUser.getUsers());
                p.setOperation("=");
                p.setPredicat("AND");
            }
        }
        paginator.addParam(p);

        p = new ParametreRequete("typeDoc", "typeDoc", type);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);

        return paginator.executeDynamicQuery("YvsComDocVentes", "y.enteteDoc.dateEntete DESC", avance, init, dao);
    }

    public YvsBaseCategorieClient currentCategorieClient(Client y) {
        YvsComClient c = new YvsComClient(y.getId());
        champ = new String[]{"client", "actif"};
        val = new Object[]{c, true};
        List<YvsComCategorieTarifaire> lt = dao.loadNameQueries("YvsComCategorieTarifaire.findByClientActif", champ, val, 0, 1);
        if (lt != null ? !lt.isEmpty() : false) {
            return lt.get(0).getCategorie();
        }
        champ = new String[]{"client"};
        val = new Object[]{c};
        lt = dao.loadNameQueries("YvsComCategorieTarifaire.findByClient", champ, val, 0, 1);
        if (lt != null ? !lt.isEmpty() : false) {
            return lt.get(0).getCategorie();
        }
        champ = new String[]{"defaut", "actif", "societe"};
        val = new Object[]{true, true, currentAgence.getSociete()};
        List<YvsBaseCategorieClient> lc = dao.loadNameQueries("YvsBaseCategorieClient.findDefaultActif", champ, val, 0, 1);
        if (lc != null ? !lc.isEmpty() : false) {
            return lc.get(0);
        }
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        lc = dao.loadNameQueries("YvsBaseCategorieClient.findDefault", champ, val, 0, 1);
        if (lc != null ? !lc.isEmpty() : false) {
            return lc.get(0);
        }
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        lc = dao.loadNameQueries("YvsBaseCategorieClient.findAllActif", champ, val, 0, 1);
        if (lc != null ? !lc.isEmpty() : false) {
            return lc.get(0);
        }
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        lc = dao.loadNameQueries("YvsBaseCategorieClient.findAll", champ, val, 0, 1);
        if (lc != null ? !lc.isEmpty() : false) {
            return lc.get(0);
        }
        return null;
    }

    public void arrondiVal(YvsComContenuDocVente c) {
        c.setPrix(arrondi(c.getPrix()));
        c.setRabais(arrondi(c.getRabais()));
        c.setRemise(arrondi(c.getRemise()));
        c.setTaxe(arrondi(c.getTaxe()));
        c.setRistourne(arrondi(c.getRistourne()));
        c.setComission(arrondi(c.getComission()));
        c.setPr(arrondi(c.getPr()));
        c.setPrixTotal(Constantes.arrondi(c.getPrixTotal(), 2));
    }

    public double prixTotal(YvsComContenuDocVente c) {
        double prix = c.getPrix() - c.getRabais();
        double total = c.getQuantite() * prix;
        total -= c.getRemise();
        total += (c.getArticle().getPuvTtc() ? 0 : c.getTaxe());
        return total;
    }

    public double prixTotal(YvsComContenuDocAchat c) {
        double total = c.getQuantiteRecu() * c.getPrixAchat();
        total -= c.getRemise();
        total += (c.getArticle().getPuaTtc() ? 0 : c.getTaxe());
        return total;
    }
    /*
     FIN REQUETE DYNAMIQUE VENTE
     */
    /*
     DEBUT REQUETE DYNAMIQUE ACHAT
     */

    public void arrondiVal(YvsComContenuDocAchat c) {
        c.setPrixAchat(arrondi(c.getPrixAchat()));
        c.setRemise(arrondi(c.getRemise()));
        c.setTaxe(arrondi(c.getTaxe()));
    }
    /*
     FIN REQUETE DYNAMIQUE ACHAT
     */

    public String buildDynamicQuery(List<ParametreRequete> params, String basicQuery, String[] fieldOrderBy, boolean desc) {
        StringBuilder sb;
        if (!desc) {
            sb = new StringBuilder(buildDynamicQuery(params, basicQuery));
            if (fieldOrderBy != null) {
                for (int i = 0; i < fieldOrderBy.length; i++) {
                    String s = fieldOrderBy[i];
                    if (i == 0) {
                        sb.append(" ORDER BY y.").append(s).append(" DESC");
                        continue;
                    }
                    sb.append("y.").append(s).append(" DESC");
                }
            }
        } else {
            sb = new StringBuilder(buildDynamicQuery(params, basicQuery, fieldOrderBy));
        }
        return sb.toString();
    }

    public String buildDynamicQuery(List<ParametreRequete> params, String basicQuery, String[] fieldOrderBy) {
        StringBuilder sb = new StringBuilder(buildDynamicQuery(params, basicQuery));
        if (fieldOrderBy != null) {
            for (int i = 0; i < fieldOrderBy.length; i++) {
                String s = fieldOrderBy[i];
                if (i == 0) {
                    sb.append(" ORDER BY y.").append(s);
                    continue;
                }
                sb.append(", y.").append(s);
            }
        }
        return sb.toString();
    }

    @Override
    public String buildDynamicQuery(List<ParametreRequete> params, String basicQuery) {
        StringBuilder sb = new StringBuilder(basicQuery);
        int i = 0;
        for (ParametreRequete p : params) {
            if (i != (params.size() - 1)) {     //cas du dernier paramètre
                if (!p.getOperation().equals("BETWEEN")) {
                    if (!p.getPredicat().equals("OR")) {
                        sb.append(" y.").append(p.getAttribut()).append(p.getOperation()).append(" :").append(p.getParamNome()).append(" ").append(p.getPredicat()).append(" ");
                    } else {
                        if (p.getTypeClauseOr() == 1) {
                            sb.append(" (y.").append(p.getAttribut()).append(p.getOperation()).append(" :").append(p.getParamNome()).append(" ").append(p.getPredicat()).append(" y.").append(p.getAttribut()).append(p.getOperation()).append(" :").append(p.getParamNome()).append("1) AND");
                        } else if (p.getTypeClauseOr() == 2) {
                            sb.append(" (y.").append(p.getAttribut()).append(p.getOperation()).append(" :").append(p.getParamNome()).append(" ").append(p.getPredicat()).append(" y.").append(p.getOtherObjet()).append(p.getOperation()).append(" :").append(p.getParamNome()).append(") AND");
                        } else {

                        }
                    }
                } else {
                    sb.append(" (y.").append(p.getAttribut()).append(" BETWEEN :").append(p.getParamNome()).append(" AND ").append(":").append(p.getParamNome()).append("1) ").append(p.getPredicat()).append(" ");
                }
            } else {
                if (!p.getOperation().equals("BETWEEN")) {
                    if (!p.getPredicat().equals("OR")) {
                        sb.append(" y.").append(p.getAttribut()).append(p.getOperation()).append(" :").append(p.getParamNome());
                    } else {
                        if (p.getTypeClauseOr() == 1) {
                            sb.append(" (y.").append(p.getAttribut()).append(p.getOperation()).append(" :").append(p.getParamNome()).append(" ").append(p.getPredicat()).append(" y.").append(p.getAttribut()).append(p.getOperation()).append(" :").append(p.getParamNome()).append("1) ");
                        } else if (p.getTypeClauseOr() == 2) {
                            sb.append(" (y.").append(p.getAttribut()).append(p.getOperation()).append(" :").append(p.getParamNome()).append(" ").append(p.getPredicat()).append(" y.").append(p.getOtherObjet()).append(p.getOperation()).append(" :").append(p.getParamNome()).append(") ");
                        } else {

                        }
                    }
                } else {
                    sb.append(" (y.").append(p.getAttribut()).append(" BETWEEN :").append(p.getParamNome()).append(" AND ").append(":").append(p.getParamNome()).append("1) ");
                }
            }
            i++;
        }
        buildDinamycParam(params);
        return sb.toString();
    }

    public double saveAllTaxe(YvsComContenuDocAchat y, DocAchat bean, YvsComDocAchats entity, long categorie, boolean save) {
        double montant = 0;
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            double prix = y.getPrixAchat();
            double qte = y.getQuantiteCommande();
            double remise = y.getRemise();
            double taxe = 0;
            double valeur = 0;
            YvsBaseArticles article = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{y.getArticle().getId()});
            nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticle";
            champ = new String[]{"categorie", "article"};
            val = new Object[]{new YvsBaseCategorieComptable(categorie), article};
            YvsBaseArticleCategorieComptable acc = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (acc != null ? (acc.getId() != null ? acc.getId() > 0 : false) : false) {
                if (article.getPuvTtc()) {
                    for (YvsBaseArticleCategorieComptableTaxe t : acc.getTaxes()) {
                        taxe += t.getTaxe().getTaux();
                    }
                    prix = prix / (1 + (taxe / 100));
                }
                valeur = qte * prix;
                for (YvsBaseArticleCategorieComptableTaxe t : acc.getTaxes()) {
                    taxe = 0;
                    if (t.getAppRemise()) {
                        taxe = (((valeur - remise) * t.getTaxe().getTaux()) / 100);
                    } else {
                        taxe = ((valeur * t.getTaxe().getTaux()) / 100);
                    }
                    taxe = dao.arrondi(currentAgence.getSociete().getId(), taxe);
                    if (save) {
                        YvsComTaxeContenuAchat ct = (YvsComTaxeContenuAchat) dao.loadOneByNameQueries("YvsComTaxeContenuAchat.findOne", new String[]{"contenu", "taxe"}, new Object[]{y, t.getTaxe()});
                        if (ct != null ? (ct.getId() != null ? ct.getId() > 0 : false) : false) {
                            ct.setMontant(taxe);
                            ct.setAuthor(currentUser);
                            ct.setDateSave(new Date());
                            ct.setDateUpdate(new Date());
                            dao.update(ct);
                        } else {
                            ct = new YvsComTaxeContenuAchat();
                            ct.setContenu(y);
                            ct.setMontant(taxe);
                            ct.setTaxe(t.getTaxe());
                            ct.setAuthor(currentUser);
                            ct.setDateUpdate(new Date());
                            dao.save1(ct);
                        }
                        int idx = y.getTaxes().indexOf(ct);
                        if (idx > -1) {
                            y.getTaxes().set(idx, ct);
                        } else {
                            y.getTaxes().add(0, ct);
                        }
                    }
                }
                if (save) {
                    int idx = bean.getContenus().indexOf(y);
                    if (idx > -1) {
                        bean.getContenus().set(idx, y);
                    }
                }
            }
        }
        return montant;
    }

    public double saveAllTaxe(YvsComContenuDocVente y, DocVente bean, YvsComDocVentes entity, long categorie, boolean save) {
        double montant = 0;
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            double prix = y.getPrix() - y.getRabais();
            double qte = y.getQuantite();
            double remise = y.getRemise();
            double taxe = 0;
            double valeur = 0;
            YvsBaseArticles article = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{y.getArticle().getId()});
            nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticle";
            champ = new String[]{"categorie", "article"};
            val = new Object[]{new YvsBaseCategorieComptable(categorie), article};
            YvsBaseArticleCategorieComptable acc = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (acc != null ? (acc.getId() != null ? acc.getId() > 0 : false) : false) {
                if (article.getPuvTtc()) {
                    for (YvsBaseArticleCategorieComptableTaxe t : acc.getTaxes()) {
                        taxe += t.getTaxe().getTaux();
                    }
                    prix = prix / (1 + (taxe / 100));
                }
                valeur = qte * prix;
                for (YvsBaseArticleCategorieComptableTaxe t : acc.getTaxes()) {
                    taxe = 0;
                    if (t.getAppRemise()) {
                        taxe = (((valeur - remise) * t.getTaxe().getTaux()) / 100);
                    } else {
                        taxe = ((valeur * t.getTaxe().getTaux()) / 100);
                    }
                    taxe = dao.arrondi(currentAgence.getSociete().getId(), taxe);
                    if (save) {
                        YvsComTaxeContenuVente ct = (YvsComTaxeContenuVente) dao.loadOneByNameQueries("YvsComTaxeContenuVente.findOne", new String[]{"contenu", "taxe"}, new Object[]{y, t.getTaxe()});
                        if (ct != null ? (ct.getId() != null ? ct.getId() > 0 : false) : false) {
                            ct.setMontant(taxe);
                            ct.setAuthor(currentUser);
                            ct.setDateSave(new Date());
                            ct.setDateUpdate(new Date());
                            dao.update(ct);
                        } else {
                            ct = new YvsComTaxeContenuVente();
                            ct.setContenu(y);
                            ct.setMontant(taxe);
                            ct.setTaxe(t.getTaxe());
                            ct.setAuthor(currentUser);
                            ct.setDateUpdate(new Date());
                            dao.save1(ct);
                        }
                        int idx = y.getTaxes().indexOf(ct);
                        if (idx > -1) {
                            y.getTaxes().set(idx, ct);
                        } else {
                            y.getTaxes().add(0, ct);
                        }
                    }
                }
                if (save) {
                    int idx = bean.getContenus().indexOf(y);
                    if (idx > -1) {
                        bean.getContenus().set(idx, y);
                    }
                }
            }
        }
        return montant;
    }

    public double returnRabais(long c, YvsBasePointVente p, Date date, double qte, double prix) {
        YvsBaseConditionnement y = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findById", new String[]{"id"}, new Object[]{c});
        return returnRabais(y, p, date, qte, prix);
    }

    public double returnRabais(YvsBaseConditionnement c, YvsBasePointVente p, Date date, double qte, double prix) {
        double rabais = 0;
        if ((c != null ? c.getId() > 0 : false) && (p != null ? p.getId() > 0 : false)) {
            YvsComRabais r = (YvsComRabais) dao.loadOneByNameQueries("YvsComRabais.findOneActifDates", new String[]{"article", "point", "conditionnement", "date"}, new Object[]{c.getArticle(), p, c, date});
            if (r != null ? r.getId() > 0 : false) {
                rabais = r.getMontant();
                if (r.getTranches() != null ? !r.getTranches().isEmpty() : false) {
                    double ca = qte * prix;
                    for (YvsComGrilleRabais g : r.getTranches()) {
                        if (g.getBase().equals(Constantes.BASE_QTE)) {
                            if (qte >= g.getMontantMinimal() && qte <= g.getMontantMaximal()) {
                                rabais = g.getMontantRabais();
                                break;
                            }
                        } else {
                            if (ca >= g.getMontantMinimal() && ca <= g.getMontantMaximal()) {
                                rabais = g.getMontantRabais();
                                break;
                            }
                        }
                    }
                }
            }
        }
        return rabais;
    }

}

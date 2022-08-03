/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.salaire.service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ValueChangeEvent;
import javax.sql.DataSource;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.JDBC;

/**
 *
 * @author LYMYTZ-PC
 * @param <T>
 */
public class PaginatorResult<T extends Serializable> {

    public boolean IS_JDBC = false;
    JDBC<T> jdbc;
    T entity;

    private List<T> result;
    private boolean disNext = true, disPrev = true;
    private int nbPage, currentPage = 1;
    private int gotoPage = 1, rows = 15, page;
    private long nbResult;
    private int idebut;
    private List<ParametreRequete> params;
    private String nameQueriCount, nameQueri;
    private String[] champ;
    private Object[] val;
    DaoInterfaceLocal dao;
    DataSource ds;

    public PaginatorResult() {
        result = new ArrayList<>();
        params = new ArrayList<>();
    }

    public PaginatorResult(int rows) {
        this();
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getGotoPage() {
        return gotoPage;
    }

    public void setGotoPage(int gotoPage) {
        this.gotoPage = gotoPage;
    }

    public String getNameQueriCount() {
        return nameQueriCount;
    }

    public void setNameQueriCount(String nameQueriCount) {
        this.nameQueriCount = nameQueriCount;
    }

    public String getNameQueri() {
        return nameQueri;
    }

    public void setNameQueri(String nameQueri) {
        this.nameQueri = nameQueri;
    }

    public String[] getChamp() {
        return champ;
    }

    public void setChamp(String[] champ) {
        this.champ = champ;
    }

    public Object[] getVal() {
        return val;
    }

    public void setVal(Object[] val) {
        this.val = val;
    }

    public List<ParametreRequete> getParams() {
        return params;
    }

    public void setParams(List<ParametreRequete> params) {
        this.params = params;
    }

    public int getIdebut() {
        return idebut;
    }

    public void setIdebut(int idebut) {
        this.idebut = idebut;
    }

    public List<T> getResult() {
        return result;
    }

    public void setDisNext(boolean disNext) {
        this.disNext = disNext;
    }

    public boolean isDisNext() {
        return disNext;
    }

    public boolean isDisPrev() {
        return disPrev;
    }

    public int getNbPage() {
        return nbPage;
    }

    public void setNbPage(int nbPage) {
        this.nbPage = nbPage;
    }

    public void setDisPrev(boolean disPrev) {
        this.disPrev = disPrev;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public long getNbResult() {
        return nbResult;
    }

    public void setNbResult(long nbResult) {
        this.nbResult = nbResult;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void choosePaginator(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            long row;
            try {
                row = (long) ev.getNewValue();
            } catch (Exception ex) {
                row = (int) ev.getNewValue();
            }
            rows = (int) row;
        }
    }

    public void gotoPage(int imax) {
        setGotoPage(getGotoPage() < 1 ? 1 : (getGotoPage() > getNbPage() ? getNbPage() : getGotoPage()));
        setIdebut(imax * (getGotoPage() - 1));
    }

    //Méthode de récupération des résultats
//ici la requete est basé sur les Name querie
    public PaginatorResult loadResult(String nameQueriCOunt, String nameQuery, String[] param, Object[] valParam, int first, int nbLimit, DaoInterfaceLocal dao) {
        nbResult = loadStat(nameQueriCOunt, param, valParam, dao);
        if (first > nbResult || first < 0) {
            first = 0;
        }
        idebut = first;
        int i = new Double((double) nbResult / (double) nbLimit).intValue();
        double dec = (((double) nbResult / (double) nbLimit) - i);//partie décimle
        nbPage = (dec > 0) ? (i + 1) : i;//si la partie décimale est >0, on arrondi le nombre de page à l'unité supérieure
        int ii = (nbResult != 0) ? new Double((double) nbPage * (double) first / (double) nbResult).intValue() : 1;
        double decc = (nbResult != 0) ? (((double) nbPage * (double) first / (double) nbResult) - ii) : 0;//partie décimle
        currentPage = (decc > 0) ? (ii + 1) : ii;
        currentPage = (currentPage == 0) ? 1 : currentPage;
        result = dao.loadNameQueries(nameQuery, param, valParam, first, nbLimit);
//        if (nbPage > 1) {
//            disNext = false;
//        } else {
//            disNext = true;
//        }
//        if (currentPage > 1) {
//            disPrev = false;
//        } else {
//            disPrev = true;
//        }

        return this;
    }

    public PaginatorResult loadResult(String nameQueriCOunt, String nameQuery, String[] param, Object[] valParam, DaoInterfaceLocal dao) {
        nbResult = loadStat(nameQueriCOunt, param, valParam, dao);
        result = dao.loadNameQueries(nameQuery, param, valParam);
        return this;
    }

    //ici la requete est basé sur JPQL
    private PaginatorResult loadResultJPQL(String nameQueriCOunt, String nameQuery, String[] param, Object[] valParam, int first, int nbLimit) {
        nbResult = (Long) (IS_JDBC ? jdbc.object(nameQueriCOunt, param, valParam) : dao.loadObjectByEntity(nameQueriCOunt, param, valParam));
        if (first > nbResult || first < 0) {
            first = 0;
        }
        int i = new Double((double) nbResult / (double) nbLimit).intValue();
        double dec = (((double) nbResult / (double) nbLimit) - i);//partie décimle
        nbPage = (dec > 0) ? (i + 1) : i;//si la partie décimale est >0, on arrondi le nombre de page à l'unité supérieure
        int ii = (nbResult != 0) ? new Double((double) nbPage * (double) first / (double) nbResult).intValue() : 1;
        double decc = (nbResult != 0) ? (((double) nbPage * (double) first / (double) nbResult) - ii) : 0;//partie décimle
        currentPage = (decc > 0) ? (ii + 1) : ii;
        currentPage = (currentPage == 0) ? 1 : currentPage;
        result = (IS_JDBC ? jdbc.list(nameQuery, param, valParam, first, nbLimit) : dao.loadEntity(nameQuery, param, valParam, first, nbLimit));
//        if (nbPage > 1) {
//            disNext = false;
//        } else {
//            disNext = true;
//        }
//        if (currentPage > 1) {
//            disPrev = false;
//        } else {
//            disPrev = true;
//        }

        return this;
    }

    private PaginatorResult loadResultJPQL(String nameQueriCOunt, String nameQuery, String[] param, Object[] valParam) {
        nbResult = (Long) (IS_JDBC ? jdbc.object(nameQueriCOunt, param, valParam) : dao.loadObjectByEntity(nameQueriCOunt, param, valParam));
        result = (IS_JDBC ? jdbc.list(nameQuery, param, valParam) : dao.loadEntity(nameQuery, param, valParam));
        return this;
    }

    public long loadStat(String nameQuery, String[] param, Object[] valParam, DaoInterfaceLocal dao) {
        return (Long) dao.loadObjectByNameQueries(nameQuery, param, valParam);
    }

    public long loadStatDyn(String nameQuery, String[] param, Object[] valParam, DaoInterfaceLocal dao) {
        return (Long) dao.loadObjectByEntity(nameQuery, param, valParam);
    }

    private List<T> pagineResult(boolean avancer, boolean init, int imax) {
        if (avancer) {
            setIdebut(getIdebut() + ((init) ? 0 : imax));
        } else {
            setIdebut(getIdebut() - ((init) ? 0 : imax));
        }
//        if (avancer) {
//            setIdebut((init) ? 0:(getIdebut() + imax));
//        } else {
//            setIdebut((init) ? 0:(getIdebut() - imax));
//        }
        if (getIdebut() < 0) {
            setIdebut(0);
        }
        if (!nameQueri.trim().toUpperCase().startsWith("SELECT")) {
            if (imax > 0) {
                this.loadResult(nameQueriCount, nameQueri, champ, val, getIdebut(), imax, dao);
            } else {
                this.loadResult(nameQueriCount, nameQueri, champ, val, dao);
            }
        } else {
            if (imax > 0) {
                this.loadResultJPQL(nameQueriCount, nameQueri, champ, val, getIdebut(), imax);
            } else {
                this.loadResultJPQL(nameQueriCount, nameQueri, champ, val);
            }
        }
        nbPage = nbPage > 0 ? nbPage : 1;
        if ((nbPage < 2) || (getIdebut() + imax) >= this.getNbResult()) {
            disNext = true;
        } else {
            disNext = false;
        }
        if (this.getCurrentPage() > 1) {
            disPrev = false;
        } else {
            disPrev = true;
        }
        if (getIdebut() < 0) {
            setIdebut(0);
        }
        return this.getResult();
    }

    /*Fin des méthodes de récupération des résultats paginé*/
    /**
     * ***********************************************************xxxxxxxxxxxxxxxxxxxxxxx
     *
     ********************
     * @param prq
     */
    //Ajouter un paramètre à la liste des paramètres de la requête
    public void addParam(ParametreRequete prq) {
        if (IS_JDBC) {
            if (prq.getOtherExpression() != null ? !prq.getOtherExpression().isEmpty() : false) {
                for (int i = 0; i < prq.getOtherExpression().size(); i++) {
                    ParametreRequete p = prq.getOtherExpression().get(i);
                    for (int j = (i + 1); j < prq.getOtherExpression().size(); j++) {
                        ParametreRequete y = prq.getOtherExpression().get(j);
                        if (y.getParamNome().equals(p.getParamNome())) {
                            prq.getOtherExpression().get(j).setParamNome(prq.getOtherExpression().get(j) + "" + j);
                        }
                    }
                }
            }
        }
        if (!params.contains(prq)) {
            params.add(prq);
        } else {
            params.set(params.indexOf(prq), prq);
        }
        if (prq.getObjet() == null) {
            if (params.contains(prq)) {
                params.remove(prq);
            }
        }
    }

    public void addParamFind(ParametreRequete prq) {
        idebut = 0;
        addParam(prq);
    }

    public void clear() {
        params.clear();
    }

    /*debut
     Exécute une requête dynamique: entity=Object Entity dont on veux la liste; paramsOrder=Chaine de caractère pour ordonner le résultat de la requête
     */
    public List<T> parcoursDynamicData(String entity, String field, String paramsOrder, int debut, DaoInterfaceLocal dao) {
        return parcoursDynamicData(entity, field, field, paramsOrder, debut, dao);
    }

    public List<T> parcoursDynamicData(String entity, String field, String alias, String paramsOrder, int debut, DaoInterfaceLocal dao) {
        IS_JDBC = false;
        this.dao = dao;
        nameQueri = (!params.isEmpty()) ? buildDynamicQuery(params, "SELECT " + field + " FROM " + entity + " " + alias + " WHERE") : buildDynamicQuery(params, "SELECT " + field + " FROM " + entity + " " + alias);
        //Ajouter les critère de tri
        if (paramsOrder != null ? paramsOrder.trim().length() > 0 : false) {
            nameQueri = nameQueri + " ORDER BY " + paramsOrder;
        }
        if (!nameQueri.trim().startsWith("SELECT")) {
            if (debut >= 0) {
                return dao.loadNameQueries(nameQueri, champ, val, debut, 1);
            }
        } else {
            if (debut >= 0) {
                return dao.loadEntity(nameQueri, champ, val, debut, 1);
            }
        }
        return this.getResult();
    }

    public List<T> executeDynamicQuery(String entity, String paramsOrder, boolean avance, boolean init, DaoInterfaceLocal dao) {
        return executeDynamicQuery(entity, paramsOrder, avance, init, rows, dao);
    }

    public List<T> executeDynamicQuery(String entity, String paramsOrder, boolean avance, boolean init, int imax, DaoInterfaceLocal dao) {
        this.dao = dao;
        return executeDynamicQuery("y", "y", entity + " y", paramsOrder, avance, init, imax, dao);
    }

    public String  getQueryToExecute(String field, String fieldCount, String entity, String paramsOrder) {
        IS_JDBC = false;
        this.dao = dao;
        nameQueriCount = (!params.isEmpty()) ? buildDynamicQuery(params, "SELECT COUNT(" + fieldCount + ") FROM " + entity + " WHERE") : buildDynamicQuery(params, "SELECT COUNT(" + fieldCount + ") FROM " + entity);
        nameQueri = (!params.isEmpty()) ? buildDynamicQuery(params, "SELECT " + field + " FROM " + entity + " WHERE") : buildDynamicQuery(params, "SELECT " + field + " FROM " + entity);
        //Ajouter les critère de tri
        if (paramsOrder != null ? paramsOrder.trim().length() > 0 : false) {
            nameQueri = nameQueri + " ORDER BY " + paramsOrder;
        }
        System.err.println("QUERY : " + nameQueri);
        return nameQueri;
    }
    public List<T> executeDynamicQuery(String field, String fieldCount, String entity, String paramsOrder, boolean avance, boolean init, int imax, DaoInterfaceLocal dao) {
        IS_JDBC = false;
        this.dao = dao;
        nameQueriCount = (!params.isEmpty()) ? buildDynamicQuery(params, "SELECT COUNT(" + fieldCount + ") FROM " + entity + " WHERE") : buildDynamicQuery(params, "SELECT COUNT(" + fieldCount + ") FROM " + entity);
        nameQueri = (!params.isEmpty()) ? buildDynamicQuery(params, "SELECT " + field + " FROM " + entity + " WHERE") : buildDynamicQuery(params, "SELECT " + field + " FROM " + entity);
        //Ajouter les critère de tri
        if (paramsOrder != null ? paramsOrder.trim().length() > 0 : false) {
            nameQueri = nameQueri + " ORDER BY " + paramsOrder;
        }
        System.err.println("QUERY : " + nameQueri);
        return pagineResult(avance, init, imax);
    }

    public List<T> executeDynamicQuery(String query, T entity, String paramsOrder, boolean avance, boolean init, int imax, DataSource ds) {
        IS_JDBC = true;
        this.ds = ds;
        this.entity = entity;
        try {
            this.jdbc = new JDBC<>(entity, ds);
        } catch (SQLException ex) {
            Logger.getLogger(PaginatorResult.class.getName()).log(Level.SEVERE, null, ex);
        }
        nameQueriCount = (!params.isEmpty()) ? buildDynamicQuery(params, JDBC.count(entity.getClass(), query).concat(" WHERE")) : buildDynamicQuery(params, JDBC.count(entity.getClass(), query));
        nameQueri = (!params.isEmpty()) ? buildDynamicQuery(params, query.concat(" WHERE")) : buildDynamicQuery(params, query);
        //Ajouter les critère de tri
        if (paramsOrder != null ? paramsOrder.trim().length() > 0 : false) {
            nameQueri = nameQueri + " ORDER BY " + jdbc.order(entity, paramsOrder);
        }
        return pagineResult(avance, init, imax);
    }

    //reconstruit la  requête en y appliquant la chaine de paramètres dynamiques  préalablement construite au niveau des formulaires
    public String buildDynamicQuery(List<ParametreRequete> params, String basicQuery) {
        buildDinamycParam(params);
        return basicQuery + " " + buildRequete(params);
    }

    //rempli les paramètres constrits sous forme de liste d'objets dans les tableaux champs / valeur accepté par notre exécuteur de requête
    public void buildDinamycParam(List<ParametreRequete> params) {
        List<ParametreRequete> lp = new ArrayList<>();
        for (ParametreRequete p : params) {
            ajouteParam(p, lp);
        }
        if (IS_JDBC) {
            List<ParametreRequete> list = new ArrayList<>(lp);
            lp.clear();
            for (ParametreRequete p : list) {
                if (!lp.contains(p)) {
                    lp.add(p);
                }
            }
        }
        int i = 0;
        champ = new String[lp.size()];
        val = new Object[lp.size()];
        for (ParametreRequete p : lp) {
            champ[i] = p.getParamNome();
            val[i] = p.getObjet();
            i++;
        }
    }

    public List<ParametreRequete> ajouteParam(ParametreRequete p, List<ParametreRequete> lp) {
        if (p.getAttribut() != null && !(p.getOperation().trim().equals("IS NOT NULL") || p.getOperation().trim().equals("IS NULL"))) {
            if (!p.getOperation().trim().equals("BETWEEN") && !p.getOperation().trim().equals("NOT BETWEEN")) {
                lp.add(p);
            } else {
                lp.add(p);
                p = new ParametreRequete(p.getAttribut(), p.getParamNome() + "1", p.getOtherObjet());
                lp.add(p);
            }
        }
        if (!p.getOtherExpression().isEmpty()) {
            for (ParametreRequete pp : p.getOtherExpression()) {
                lp.addAll(ajouteParam(pp, lp));
            }
        }
        return lp;
    }

    /*Suite de méhode qui permettent de construire la chaine de paramètre sous forme de String*/
    private String buildRequete(List<ParametreRequete> lp) {
        String re = "";
        for (ParametreRequete p : lp) {
            int i = lp.indexOf(p);
            if (i != (lp.size() - 1)) {
                p.setLastParam(false);
            } else {
                p.setLastParam(true);
            }
            re += decomposeRequete(p) + "" + ((p.isLastParam()) ? "" : " " + p.getPredicat()) + " ";
        }
        return re;
    }

    private String decomposeRequete(ParametreRequete p) {
        String re = "";
        if (p.getAttribut() != null) {
            re += concateneParam(p);
        }
        if (!p.getOtherExpression().isEmpty()) {
            p.getOtherExpression().get(p.getOtherExpression().size() - 1).setLastParam(true);
            if (p.getAttribut() != null) {
                re += " " + p.getPredicat() + " (";
            } else {
                re += "(";
            }
            for (ParametreRequete p1 : p.getOtherExpression()) {
                re += decomposeRequete(p1) + "" + ((p1.isLastParam()) ? "" : " " + p1.getPredicat()) + " ";
            }
            re += ")";
        }
        return re;
    }

    private String concateneParam(ParametreRequete p) {
        String re = "";
        if (p.getOperation().trim().equals("BETWEEN") || p.getOperation().trim().equals("NOT BETWEEN")) {
            if (IS_JDBC) {
                re += "(" + jdbc.rewrite(entity, p.getAttribut()) + " " + p.getOperation() + " ? AND ?)";
            } else {
                re += "(" + p.getAttribut() + " " + p.getOperation() + " :" + p.getParamNome() + " AND :" + p.getParamNome() + "" + 1 + ")";
            }
        } else {
            if (IS_JDBC) {
                re += "" + jdbc.rewrite(entity, p.getAttribut()) + " " + p.getOperation() + ((p.getOperation().trim().equals("IS NOT NULL") || p.getOperation().trim().equals("IS NULL")) ? "" : " ?");
            } else {
                re += "" + p.getAttribut() + " " + p.getOperation() + ((p.getOperation().trim().equals("IS NOT NULL") || p.getOperation().trim().equals("IS NULL")) ? "" : " :" + p.getParamNome());
            }
        }
        return re;
    }
}

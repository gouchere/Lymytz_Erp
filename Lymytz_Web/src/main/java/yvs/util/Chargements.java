/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import yvs.entity.base.YvsBaseArticleDepot;
import java.io.Serializable;import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsCrenauxHoraire;
import yvs.entity.param.YvsSocietes;
import yvs.entity.produits.*;
import yvs.entity.tiers.YvsBaseTiers; 
import yvs.base.produits.Articles;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Chargements implements Serializable {

    @EJB
    public DaoInterfaceLocal dao;

    public Chargements() {
    }

    public YvsAgences loadAgence(long id) {
        dao.setEntityClass(YvsAgences.class);
        return (YvsAgences) dao.getOne(id);
    }

    public YvsAgences loadAgence(String codeAgence) {
        dao.setEntityClass(YvsAgences.class);
        String[] ch = {"codeagence"};
        Object[] val = {codeAgence};
        return (YvsAgences) dao.getOne(ch, val);
    }

    public YvsBaseDepots loadDepot(String code, long agence) {
        String[] ch = {"code", "agence"};
        Object[] val = {code, agence};
        YvsBaseDepots dep = (YvsBaseDepots) dao.loadOneByNameQueries("YvsDepots.findByCode", ch, val);
        return dep;
    }

    public YvsBaseTiers loadFournisseur(String code, long agence) {
        String[] ch = {"codeFournisseur", "agence"};
        Object[] val = {code};
        return (YvsBaseTiers) dao.loadOneByNameQueries("YvsTiers.findByCodeFournisseur", ch, val);
    }

    public YvsBaseTiers loadClient(String code, long agence) {
        String[] ch = {"codeClt", "agence"};
        Object[] val = {code, agence};
        return (YvsBaseTiers) dao.loadOneByNameQueries("YvsTiers.findByCodeClient", ch, val);
    }

    public YvsBaseCategorieComptable loadCatC(YvsSocietes societe, String code) {
        String[] ch = {"societe", "codecat"};
        Object[] val = {societe.getId(), code};
        return (YvsBaseCategorieComptable) dao.loadOneByNameQueries("YvsCatcompta.findOne", ch, val);
    }

//    public YvsCatTarif loadCatT(YvsSocietes societe, String code) {
//        String[] ch = {"societe", "codecat"};
//        Object[] val = {societe.getId(), code};
//        return (YvsCatTarif) dao.loadOneByNameQueries("YvsCatTarif.findOne", ch, val);
//    }

    public List<YvsBaseDepots> loadAllDepot(long agence) {
        String[] ch = {"agence"};
        Object[] val = {agence};
        List<Object[]> l = dao.loadListTableByNameQueries("YvsDepots.findByFilter", ch, val);
        YvsBaseDepots dep = new YvsBaseDepots();
        List<YvsBaseDepots> ldep = new ArrayList<>();
        for (Object[] tab : l) {
            dep.setId((long) tab[0]);
            dep.setCode((String) tab[1]);
            dep.setDesignation((String) tab[2]);
            ldep.add(dep);
        }
        return ldep;
    }

    public YvsBaseArticles loadOneArtNegoceInDepot(long depot, String refArt, List<String> lnature) {
        String[] ch = {"depot", "refArt", "nature"};
        Object[] val = {depot, refArt, lnature};
        Object[] tab = (Object[]) dao.loadOneByNameQueries("YvsArticlesDepot.findOneArtInDepot", ch, val);
        YvsBaseArticles art = null;
        if (tab != null) {
            art = new YvsBaseArticles();
            art.setId((long) tab[0]);
            art.setRefArt((String) tab[1]);
            art.setDesignation((String) tab[2]);
            art.setPua((double) tab[3]);
            art.setRemise((double) tab[4]);
            art.setPuv((double) tab[5]);
        }
        return art;
    }

    public YvsBaseArticles loadOneArtNegoceInAgence(long agence, String refArt, List<String> lnature) {
        String[] ch = {"agence", "refArt", "nature"};
        Object[] val = {agence, refArt, lnature};
        Object[] tab = (Object[]) dao.loadOneByNameQueries("YvsArticlesDepot.findOneArtInAgence", ch, val);
        YvsBaseArticles art = null;
        if (tab != null) {
            art = new YvsBaseArticles();
            art.setId((long) tab[0]);
            art.setRefArt((String) tab[1]);
            art.setDesignation((String) tab[2]);
            art.setPua((double) tab[3]);
            art.setRemise((double) tab[4]);
            art.setPuv((double) tab[5]);
        }
        return art;
    }

    public List<Articles> loadListArtNegoceInDepot(long depot, List<String> lnature) {
        String[] ch = {"depot", "nature"};
        Object[] val = {depot, lnature};
        List<Object[]> l = dao.loadListTableByNameQueries("YvsArticlesDepot.findAllArtInDepot", ch, val);
        Articles art = new Articles();
        List<Articles> lf = new ArrayList<>();
        for (Object[] tab : l) {
            art.setId((long) tab[0]);
            art.setRefArt((String) tab[1]);
            art.setDesignation((String) tab[2]);
            art.setPua((double) tab[3]);
            art.setRemise((double) tab[4]);
            art.setPuv((double) tab[5]);
            lf.add(art);
            art = new Articles();
        }
        return lf;
    }

    public List<Articles> loadListArtNegoceInAgence(long agence, List<String> lnature) {
        String[] ch = {"agence", "nature"};
        Object[] val = {agence,lnature};
        List<Object[]> l = dao.loadListTableByNameQueries("YvsArticlesDepot.findAllArtInAgence", ch, val);
        Articles art = new Articles();
        List<Articles> lf = new ArrayList<>();
        for (Object[] tab : l) {
            art.setId((long) tab[0]);
            art.setRefArt((String) tab[1]);
            art.setDesignation((String) tab[2]);
            art.setPua((double) tab[3]);
            art.setRemise((double) tab[4]);
            art.setPuv((double) tab[5]);
            lf.add(art);
            art = new Articles();
        }
        return lf;
    }
//
//    public List<BaseTaxe> loadTaxes(YvsSocietes sct) {
//        List<BaseTaxe> lr = new ArrayList<>();
//        String[] ch = {"societe"};
//        Object[] v = {sct.getId()};
//        List<Object[]> l = dao.loadNameQueries("YvsTaxes.findPartial", ch, v);
//        if (l != null) {
//            BaseTaxe b = new BaseTaxe();
//            for (Object[] tab : l) {
//                b.setIdTaxe((long) tab[0]);
//                b.setTaxe((String) tab[1]);
//                b.setTaux((double) tab[2]);
//                lr.add(b);
//                b = new BaseTaxe();
//            }
//        }
//        return lr;
//    }
//
//    public List<BaseTaxe> repartitionTaxe(YvsBaseArticles art, YvsBaseCategorieComptable cat, List<BaseTaxe> lr, double montant) {
//        String[] ch = {"article", "categorie"};
//        Object[] val = {art, cat};
//        List<Object[]> l = dao.loadListTableByNameQueries("YvsArtCatTaxes.findTaxe", ch, val);
//        if (l != null) {
//            for (Object[] tab : l) {
//                BaseTaxe bt = new BaseTaxe((long) tab[0], (String) tab[1]);
//                if (lr.contains(bt)) {
//                    int i = lr.indexOf(bt);
////                    BaseTaxe b = lr.get(i);
////                    b.setBase(b.getBase() + montant);
////                    b.setMontant((b.getBase() * b.getTaux() / 100) + b.getMontant());
////                    lr.set(i, b);
//                }
//            }
//        }
//        return lr;
//    }

//    public BorneTranche findRemise(YvsBaseArticles art, YvsCatTarif cat, double qte, double prix) {
//        BorneTranche b = null;
//        YvsBasePlanTarifaireArticle pt = getPlanTarif(art.getId(), cat.getId());
//        if (pt != null) {
//            b = new BorneTranche();
//            if (pt.getTranche() != null) {
//                YvsBorneTranches bt;
//                if (pt.getTranche().getModelTranche().equals("Valeurs")) {
//                    bt = getBorne(pt.getTranche().getYvsBorneTranchesList(), (qte * prix));
//                } else {
//                    bt = getBorne(pt.getTranche().getYvsBorneTranchesList(), qte);
//                }
//                if (bt != null) {
//                    b.setBorne(bt.getBorne());
//                    b.setPrix(bt.getPrix());
//                    b.setRemise(bt.getRemiseVente());
//                }
//            } else {
//                b.setPrix(pt.getPrix());
//                b.setRemise(pt.getRemiseVente());
//            }
//        }
//        return b;
//    }

//    public BorneTranche findRistourne(YvsBaseArticles art, YvsPlanDeRecompense rist, double qte, double prix, Date today) {
//        BorneTranche b = null;
//        System.err.println(art + "  " + rist);
//        YvsArticlesRecompense artR = getRistOrCom(art.getId(), (rist.getId() != null) ? rist.getId() : 0);
//        if (artR != null) {
//            if (findRec(rist, today)) {
//                b = new BorneTranche();
//                if (artR.getTranche() != null) {
//                    YvsBorneTranches bt;
//                    if (artR.getIdRecompense().getObjectif().equals("Valeur")) {
//                        bt = getBorne(artR.getTranche().getYvsBorneTranchesList(), (qte * prix));
//                    } else {
//                        bt = getBorne(artR.getTranche().getYvsBorneTranchesList(), qte);
//                    }
//                    if (bt != null) {
//                        b.setBorne(bt.getBorne());
//                        b.setPrix(bt.getPrix());
//                        b.setRemise(bt.getRemiseVente());
//                    }
//                } else {
//                    b.setPrix(artR.getValeur());
//                    b.setRemise(artR.getTaux());
//                }
//            }
//        }
//        return b;
//    }
//
//    private boolean findRec(YvsPlanDeRecompense plan, Date today) {
//        switch (plan.getDuree()) {
//            case "Permanence":
//                return true;
//            case "A Partir du":
//                if (today.after(plan.getDebut())) {
//                    return true;
//                } else {
//                    return false;
//                }
//            case "Jusqu'au":
//                if (today.before(plan.getDebut())) {
//                    return true;
//                } else {
//                    return false;
//                }
//            case "Entre ":
//                if (today.after(plan.getDebut()) && today.before(plan.getFin())) {
//                    return true;
//                } else {
//                    return false;
//                }
//        }
//        return false;
//    }

//    private YvsComPlanRemise getPlanTarif(long art, long cat) {
//        String[] ch = {"article", "categorie"};
//        Object[] val = {art, cat};
//        return (YvsComPlanRemise) dao.loadOneByNameQueries("YvsPlanTarif.findOne", ch, val);
//    }
//
//    private YvsBorneTranches getBorne(List<YvsBorneTranches> l, double val) {
//        YvsBorneTranches b = null;
//        Collections.sort(l, new YvsBorneTranches());
//        int i = 0;
//        for (YvsBorneTranches bt : l) {
//            if (bt.getBorne() > val && i > 0) {
//                return l.get(i - 1);
//            }
//            i++;
//        }
//        return b;
//    }

//    private YvsArticlesRecompense getRistOrCom(long art, long rec) {
//        String[] ch = {"article", "idRecompense"};
//        Object[] val = {art, rec};
//        return (YvsArticlesRecompense) dao.loadOneByNameQueries("YvsArticlesRecompense.findOne", ch, val);
//    }

    public YvsCrenauxHoraire getCreno(long agence, String code) {
        String[] ch = {"agence", "code"};
        Object[] val = {agence, code};
        return (YvsCrenauxHoraire) dao.loadOneByNameQueries("YvsCrenauxHoraire.findByCode", ch, val);
    }

    public double getLastPr(long art, long dep) {
        String[] ch = {"idArticle", "idDepot"};
        Object[] val = {art, dep};
        YvsBaseArticleDepot ad = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsArticlesDepot.findOne", ch, val);
//        return (ad != null) ? (ad.getPr()!=0)?ad.getPr():ad.getArticle().getPua() : 0;
        return 0;
    }
    
    public double getStocks(long art, long dep, Date d) {
        return dao.stocks(art, dep, d);
    }
}

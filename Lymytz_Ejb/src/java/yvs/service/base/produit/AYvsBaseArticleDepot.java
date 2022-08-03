/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.produits.YvsBaseArticles;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseArticleDepot extends AbstractEntity {

    public AYvsBaseArticleDepot() {
    }

    public AYvsBaseArticleDepot(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseArticleDepot> controle(YvsBaseArticleDepot entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }

            if (entity.getArticle() != null ? entity.getArticle().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner l'article");
            }
            if (entity.getDepot() != null ? entity.getDepot().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner le depot");
            }
            if (entity.getStockMax() < entity.getStockMin()) {
                return new ResultatAction<>(false, entity, 0L, "Le stock maximal ne peut pas etre inferieur au stock minimal");
            }

            String[] champ = new String[]{"article", "depot"};
            Object[] val = new Object[]{new YvsBaseArticles(entity.getArticle().getId()), new YvsBaseDepots(entity.getDepot().getId())};
            String nameQueri = "YvsBaseArticleDepot.findByArticleDepot";
            YvsBaseArticleDepot a = (YvsBaseArticleDepot) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (a != null ? (a.getId() > 0 ? !a.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(true, a, a.getId(), "Succès", false);
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleDepot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseArticleDepot> save(YvsBaseArticleDepot entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_article_depot", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseArticleDepot) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleDepot.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseArticleDepot> update(YvsBaseArticleDepot entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseArticleDepot) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleDepot.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseArticleDepot> delete(YvsBaseArticleDepot entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleDepot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Méthode 2*
     */
    public double calculStockSecurite(YvsBaseArticleDepot ad) {
        if (ad != null ? (ad.getArticle() != null && ad.getDepot() != null) : false) {

        }
        return 0;
    }

    private Double getPeriodeApproMoyen(YvsBaseArticles article, YvsBaseDepots depot, Date debut, Date fin) {
//        /*Période moyenne + période max*/
//        long periodeMAx = 0, periodeMoy = 0;
//        long total = 0;
//        //Parcours toutes les mouvement de stock du produit et relevé 
//        String query = "SELECT DISTINCT m.date_doc FROM yvs_base_mouvement_stock m WHERE m.article=? AND m.depot=? AND m.date_doc BETWEEN ? AND ? "
//                + "AND m.mouvement='E' AND m.table_externe='yvs_com_contenu_doc_achat' ORDER BY m.date_doc";
//        List<Date> re = dao.loadListBySqlQuery(query, new Options[]{new Options(article.getId(), 1), new Options(depot.getId(), 2), new Options(debut, 3), new Options(fin, 4)});
//        int i = 0;
//        LocalDate first, next;
//        long p;
//        if (!re.isEmpty()) {
//            while (i + 1 < re.size()) {
//                first = re.get(i).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                next = re.get(i + 1).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                p = ChronoUnit.DAYS.between(first, next);
//                if (p > periodeMAx) {
//                    periodeMAx = p;
//                }
//                total += p;
//                i++;
//            }
//        }
//        periodeMoy = total / re.size();

        /*COnsommation moy et Consommation max /Jour*/
        return 0d;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseArticleCategorieComptableTaxe;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.commercial.client.YvsBasePlanTarifaireTranche;
import yvs.entity.param.YvsAgences;
import yvs.entity.prod.YvsBaseArticlesTemplate;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsNiveauAcces;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz Dowes
 */
public abstract class AYvsBaseArticles extends AbstractEntity {

    public AYvsBaseArticles() {
    }

    public AYvsBaseArticles(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public void setAgence(YvsAgences agence) {
        this.currentAgence = agence;
    }

    public void setNiveauAcces(YvsNiveauAcces niveau) {
        this.niveau = niveau;
    }

    public ResultatAction<YvsBaseArticles> controle(YvsBaseArticles entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getRefArt())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner la référence");
            }
            if (!asString(entity.getDesignation())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner la désignation");
            }
            if (!asString(entity.getCategorie())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner la catégorie");
            }
            if (entity.getFamille() != null ? entity.getFamille().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner la famille");
            }
            if (entity.getFamille().getSociete() != null ? entity.getFamille().getSociete().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner la société de la famille");
            }
            YvsBaseArticles y = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findByCode", new String[]{"societe", "code"}, new Object[]{entity.getFamille().getSociete(), entity.getRefArt()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(entity.getId()) : false : false) {
                if (y.getDesignation().equals(entity.getDesignation())) {
                    return new ResultatAction<>(true, y, y.getId(), "Succès", false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Il existe deja un article avec cette reference");
                }

            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseArticles> save(YvsBaseArticles entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                if (!entity.isSynchroniser()) {
                    if (!autoriser("base_article_save")) {
                        return new ResultatAction<>(false, entity, 0L, "Vous ne disposez pas de privillège pour réaliser cette action");
                    }
                }
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_articles", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseArticles) dao.save1(entity);
                }
                //Si on n'est pas en train de synchroniser depuis un autre appareils
                if (!entity.isSynchroniser()) {
                    //Save les relations Sup.
                    saveAll(entity, entity.getTemplate(), false);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès", entity);
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticles.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseArticles> update(YvsBaseArticles entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                if (!entity.isSynchroniser()) {
                    if (!autoriser("base_article_update")) {
                        return new ResultatAction<>(false, entity, 0L, "Vous ne disposez pas de privillège pour réaliser cette action");
                    }
                }
                entity = (YvsBaseArticles) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès", entity);
                }
                //Si on n'est pas en train de synchroniser depuis un autre appareils
                if (!entity.isSynchroniser()) {
                    //Save les relations Sup.
                    saveAllDepot(entity);
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticles.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseArticles> delete(YvsBaseArticles entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public YvsBaseArticles saveAll(YvsBaseArticles current, YvsBaseArticlesTemplate template, boolean add) {
        try {
            if (current != null ? (current.getId() > 0 ? (template != null ? template.getId() > 0 : false) : false) : false) {
                List<YvsBaseArticleCategorieComptableTaxe> lt = new ArrayList<>();
                YvsBaseArticleCategorieComptable yc;
                String[] champ = new String[]{"categorie", "article"};
                Object[] val;
                String nameQueri;
                for (YvsBaseArticleCategorieComptable y : template.getComptes()) {
                    val = new Object[]{y.getCategorie(), current};
                    nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticle";
                    yc = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries(nameQueri, champ, val);
                    if (yc != null ? (yc.getId() != null ? yc.getId() < 1 : true) : true) {
                        lt.clear();
                        lt.addAll(y.getTaxes());
                        y.getTaxes().clear();

                        y.setArticle(current);
                        y.setTemplate(null);
                        y.setId(null);
                        y = (YvsBaseArticleCategorieComptable) dao.save1(y);
                        for (YvsBaseArticleCategorieComptableTaxe s : lt) {
                            s.setArticleCategorie(y);
                            s.setId(null);
                            s = (YvsBaseArticleCategorieComptableTaxe) dao.save1(s);
                            y.getTaxes().add(s);
                        }
                        int idx = current.getComptes().indexOf(y);
                        if (idx > -1) {
                            current.getComptes().set(idx, y);
                        } else {
                            current.getComptes().add(0, y);
                        }
                        if (add) {
                            idx = current.getComptes().indexOf(y);
                            if (idx > -1) {
                                current.getComptes().set(idx, y);
                            } else {
                                current.getComptes().add(0, y);
                            }
                        }
                    }
                }
                YvsBasePlanTarifaire yp;
                List<YvsBasePlanTarifaireTranche> lp = new ArrayList<>();
                champ = new String[]{"categorie", "article"};
                for (YvsBasePlanTarifaire y : template.getPlans_tarifaires()) {
                    val = new Object[]{y.getCategorie(), current};
                    nameQueri = "YvsBasePlanTarifaire.findByCategorieArticle";
                    yp = (YvsBasePlanTarifaire) dao.loadOneByNameQueries(nameQueri, champ, val);
                    if (yp != null ? (yp.getId() != null ? yp.getId() < 1 : true) : true) {
                        lp.clear();
                        lp.addAll(y.getGrilles());
                        y.getGrilles().clear();

                        y.setArticle(current);
                        y.setTemplate(null);
                        y.setId(null);
                        y = (YvsBasePlanTarifaire) dao.save1(y);
                        for (YvsBasePlanTarifaireTranche s : lp) {
                            s.setPlan(y);
                            s.setId(null);
                            s = (YvsBasePlanTarifaireTranche) dao.save1(s);
                            y.getGrilles().add(s);
                        }
                        int idx = current.getPlans_tarifaires().indexOf(y);
                        if (idx > -1) {
                            current.getPlans_tarifaires().set(idx, y);
                        } else {
                            current.getPlans_tarifaires().add(0, y);
                        }
                        if (add) {
                            idx = current.getPlans_tarifaires().indexOf(y);
                            if (idx > -1) {
                                current.getPlans_tarifaires().set(idx, y);
                            } else {
                                current.getPlans_tarifaires().add(0, y);
                            }
                        }
                    }
                }
            }
            return saveAllDepot(current);
        } catch (Exception ex) {
            return null;
        }
    }

    public YvsBaseArticles saveAllDepot(YvsBaseArticles current) {
        try {
            if (current != null ? current.getId() > 0 : false) {
                //Liaison dépôts
                if (current.getCategorie() != null) {
                    List<YvsBaseDepots> depots = null;
                    switch (current.getCategorie()) {
                        case Constantes.CAT_MARCHANDISE:
                            depots = dao.loadNameQueries("YvsBaseDepots.findDepotByTypeOpNegoce", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                            break;
                        case Constantes.CAT_PF:
                            depots = dao.loadNameQueries("YvsBaseDepots.findDepotByTypeOpPF", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                            break;
                        case Constantes.CAT_PSF:
                            depots = dao.loadNameQueries("YvsBaseDepots.findDepotByTypeOpPSF", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                            break;
                        case Constantes.CAT_MP:
                            depots = dao.loadNameQueries("YvsBaseDepots.findDepotByTypeOpMP", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                            break;
                    }
                    if (depots != null ? !depots.isEmpty() : false) {
                        YvsBaseArticleDepot ad;
                        current.getYvsBaseArticleDepotList().clear();
                        for (YvsBaseDepots d : depots) {
                            ad = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findOneByArticleDepot", new String[]{"article", "depot"}, new Object[]{current, d});
                            if (ad == null) {
                                ad = new YvsBaseArticleDepot(null, current);
                                ad.setAuthor(current.getAuthor());
                                ad.setDateSave(new Date());
                                ad.setDateUpdate(new Date());
                                ad.setDepot(d);
                                ad.setDepotPr(d);
                                ad.setSellWithoutStock(true);
                                ad.setSuiviStock(true);
                                ad.setRequiereLot(d.getRequiereLot() && current.getRequiereLot());
                                ad.setActif(true);
                                ad = (YvsBaseArticleDepot) dao.save1(ad);
                            } else {
                                ad.setActif(true);
                                dao.update(ad);
                            }
                            current.getYvsBaseArticleDepotList().add(ad);
                        }
                    }
                }
            }
            return current;
        } catch (Exception ex) {
            return null;
        }
    }
}

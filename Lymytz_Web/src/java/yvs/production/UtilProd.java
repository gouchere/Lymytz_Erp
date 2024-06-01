/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import yvs.base.compta.ArticleTaxe;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.Taxes;
import yvs.base.compta.UtilCompta;
import yvs.base.produits.ArticleAnalytique;
import yvs.base.produits.ArticleDescription;
import yvs.base.produits.ArticleFournisseur;
import yvs.base.produits.ArticlePack;
import yvs.base.produits.Articles;
import yvs.base.produits.ArticlesCatComptable;
import yvs.base.produits.ClassesStat;
import yvs.base.produits.CodeBarre;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.ContenuPack;
import yvs.base.produits.EmplacementDepot;
import yvs.base.produits.FamilleArticle;
import yvs.base.produits.GroupeArticle;
import yvs.base.produits.TableConversion;
import yvs.base.produits.TemplateArticles;
import yvs.base.produits.UniteMesure;
import yvs.base.tiers.Tiers;
import yvs.commercial.UtilCom;
import yvs.commercial.creneau.Creneau;
import yvs.commercial.depot.ArticleDepot;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.entity.base.YvsBaseArticleAnalytique;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseArticleCategorieComptableTaxe;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseClassesStat;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseEmplacementDepot;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseTableConversion;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.creneau.YvsComTypeCreneauHoraire;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.grh.param.YvsCalendrier;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsBaseCentreValorisation;
import yvs.entity.param.YvsBaseTypeEtat;
import yvs.entity.param.YvsSocietes;
import yvs.entity.prod.YvsBaseArticlesTemplate;
import yvs.entity.production.YvsProdParametre;
import yvs.entity.production.base.YvsProdCapacitePosteCharge;
import yvs.entity.production.base.YvsProdCentreCharge;
import yvs.entity.production.base.YvsProdCentrePosteCharge;
import yvs.entity.production.base.YvsProdComposantNomenclature;
import yvs.entity.production.base.YvsProdCreneauEquipeProduction;
import yvs.entity.production.base.YvsProdDocumentTechnique;
import yvs.entity.production.base.YvsProdEtatRessource;
import yvs.entity.production.base.YvsProdGammeArticle;
import yvs.entity.production.base.YvsProdNomenclature;
import yvs.entity.production.base.YvsProdOperationsGamme;
import yvs.entity.production.base.YvsProdPosteCharge;
import yvs.entity.production.base.YvsProdPosteOperation;
import yvs.entity.production.base.YvsProdSiteProduction;
import yvs.entity.production.equipe.YvsProdMembresEquipe;
import yvs.entity.production.equipe.YvsProdEquipeProduction;
import yvs.entity.production.pilotage.YvsProdComposantOF;
import yvs.entity.production.pilotage.YvsProdContenuConditionnement;
import yvs.entity.production.pilotage.YvsProdDeclarationProduction;
import yvs.entity.production.pilotage.YvsProdFicheConditionnement;
import yvs.entity.production.pilotage.YvsProdOperationsOF;
import yvs.entity.production.pilotage.YvsProdOrdreFabrication;
import yvs.entity.production.pilotage.YvsProdSessionOf;
import yvs.entity.production.pilotage.YvsProdSessionProd;
import yvs.entity.production.planification.YvsProdDetailPdc;
import yvs.entity.production.planification.YvsProdDetailPdp;
import yvs.entity.production.planification.YvsProdDetailPic;
import yvs.entity.production.planification.YvsProdPeriodePlan;
import yvs.entity.production.planification.YvsProdPlanification;
import yvs.entity.produits.YvsBaseArticleCodeBarre;
import yvs.entity.produits.YvsBaseArticleContenuPack;
import yvs.entity.produits.YvsBaseArticleDescription;
import yvs.entity.produits.YvsBaseArticlePack;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.entity.produits.group.YvsBorneTranches;
import yvs.entity.produits.group.YvsTranches;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.JoursOuvres;
import yvs.grh.UtilGrh;
import static yvs.grh.UtilGrh.buildBeanJoursOuvree;
import yvs.grh.bean.Employe;
import yvs.parametrage.TypeEtat;
import yvs.parametrage.UtilParam;
import yvs.parametrage.entrepot.Depots;
import yvs.parametrage.societe.UtilSte;
import yvs.production.base.CentreCharge;
import yvs.production.base.CrenauxHoraireEquipe;
import yvs.production.base.EmployeEquipe;
import yvs.production.base.EquipeProduction;
import yvs.production.base.EtatRessource;
import yvs.production.base.OperationsGamme;
import yvs.production.base.SiteProduction;
import yvs.production.base.TypeCrenaux;
import yvs.production.planification.DetailPlanPDC;
import yvs.production.planification.DetailPlanPDP;
import yvs.production.planification.PeriodePlanification;
import yvs.production.planification.PeriodePlanificationPDP;
import yvs.production.planification.Planification;
import yvs.production.planification.PlanificationPDP;
import yvs.production.planification.view.ObjectData;
import yvs.production.technique.CapacitePosteCharge;
import yvs.production.technique.CentrePosteCharge;
import yvs.production.technique.ComposantNomenclature;
import yvs.production.technique.ContenuConditionnement;
import yvs.production.technique.FicheConditionnement;
import yvs.production.technique.GammeArticle;
import yvs.production.technique.Nomenclature;
import yvs.production.technique.PosteCharge;
import yvs.production.technique.PosteOperation;
import yvs.users.UtilUsers;
import yvs.util.BorneTranche;
import yvs.util.Constantes;
import yvs.util.TrancheVal;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
public class UtilProd {

    public static ParametreProd buildBeanParametre(YvsProdParametre y) {
        ParametreProd p = new ParametreProd();
        if (y != null) {
            p.setId(y.getId());
            p.setConverter(y.getConverter());
            p.setSuiviOprequis(y.getSuiviOpRequis());
            p.setNumCmdeRequis(y.getNumCmdeRequis());
            p.setDateSave(y.getDateSave());
            p.setLimiteCreateOf(y.getLimiteCreateOf());
            p.setLimiteVuOf(y.getLimiteVuOf());
            p.setCloseDeclAuto(y.getCloseDeclAuto());
            p.setDeclarationProportionnel(y.getDeclarationProportionnel());
            p.setDeclareWhenFinishOf(y.getDeclareWhenFinishOf());
            p.setValoriserBy(y.getValoriserBy());
            p.setValoriseFromOf(y.getValoriseFromOf());
        }
        return p;
    }

    public static YvsProdParametre buildParametre(ParametreProd y, YvsSocietes ste, YvsUsersAgence ua) {
        YvsProdParametre p = new YvsProdParametre();
        if (y != null) {
            p.setId(y.getId());
            p.setConverter(y.getConverter());
            p.setSuiviOpRequis(y.isSuiviOprequis());
            p.setNumCmdeRequis(y.isNumCmdeRequis());
            p.setSociete(ste);
            p.setAuthor(ua);
            p.setDateSave(y.getDateSave());
            p.setLimiteCreateOf(y.getLimiteCreateOf());
            p.setLimiteVuOf(y.getLimiteVuOf());
            p.setCloseDeclAuto(y.isCloseDeclAuto());
            p.setDeclarationProportionnel(y.isDeclarationProportionnel());
            p.setValoriserBy(y.getValoriserBy());
            p.setDeclareWhenFinishOf(y.isDeclareWhenFinishOf());
            p.setValoriseFromOf(y.isValoriseFromOf());

        }
        return p;
    }
    /*
     DEBUT GESTION DE POSTE DE CHARGE

     */

    public static EtatRessource buildBeanEtatRessource(YvsProdEtatRessource y) {
        EtatRessource e = new EtatRessource();
        if (y != null) {
            e.setActif((y.getActif() != null) ? y.getActif() : false);
            e.setCapaciteH((y.getCapaciteH() != null) ? y.getCapaciteH() : 0);
            e.setCapaciteQ((y.getCapaciteQ() != null) ? y.getCapaciteQ() : 0);
            e.setChargeH((y.getChargeH() != null) ? y.getChargeH() : 0);
            e.setChargeQ((y.getChargeQ() != null) ? y.getChargeQ() : 0);
            e.setId(y.getId());
            e.setDateEtat((y.getDateEtat() != null) ? y.getDateEtat() : new Date());
            e.setTauxObsolescence((y.getTauxObsolescence() != null) ? y.getTauxObsolescence() : 0);
            e.setTypeEtat((y.getTypeEtat() != null) ? UtilParam.buildBeanTypeEtat(y.getTypeEtat()) : new TypeEtat());
            e.setRessource(buildBeanPosteCharge(y.getRessourceProduction()));
        }
        return e;
    }

    public static YvsProdEtatRessource buildEtatRessource(EtatRessource y, YvsUsersAgence ua) {
        YvsProdEtatRessource e = new YvsProdEtatRessource();
        if (y != null) {
            e.setId(y.getId());
            e.setActif(y.isActif());
            e.setCapaciteH(y.getCapaciteH());
            e.setCapaciteQ(y.getCapaciteQ());
            e.setChargeH(y.getChargeH());
            e.setChargeQ(y.getChargeQ());
            e.setDateEtat((y.getDateEtat() != null) ? y.getDateEtat() : new Date());
            e.setTauxObsolescence(y.getTauxObsolescence());
            if (y.getTypeEtat() != null ? y.getTypeEtat().getId() > 0 : false) {
                e.setTypeEtat(new YvsBaseTypeEtat(y.getTypeEtat().getId(), y.getTypeEtat().getLibelle()));
            }
            if (y.getRessource() != null ? y.getRessource().getId() > 0 : false) {
                e.setRessourceProduction(new YvsProdPosteCharge(y.getRessource().getId()));
            }
            e.setAuthor(ua);
            e.setNew_(true);
        }
        return e;
    }

    public static List<EtatRessource> buildBeanListEtatRessource(List<YvsProdEtatRessource> y) {
        List<EtatRessource> l = new ArrayList<>();
        if (y != null) {
            for (YvsProdEtatRessource c : y) {
                l.add(buildBeanEtatRessource(c));
            }
        }
        return l;
    }

    public static CapacitePosteCharge buildBeanCapacitePosteCharge(YvsProdCapacitePosteCharge y) {
        CapacitePosteCharge c = new CapacitePosteCharge();
        if (y != null) {
            c.setId(y.getId());
            c.setCapaciteQ(y.getCapaciteQ());
            c.setArticle(buildBeanArticles(y.getArticles()));
            c.setPoste(buildBeanPosteCharge(y.getPosteCharge()));
        }
        return c;
    }

    public static YvsProdCapacitePosteCharge buildCapacitePosteCharge(CapacitePosteCharge y, YvsUsersAgence ua) {
        YvsProdCapacitePosteCharge c = new YvsProdCapacitePosteCharge();
        if (y != null) {
            c.setId(y.getId());
            c.setCapaciteQ(y.getCapaciteQ());
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                c.setArticles(new YvsBaseArticles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()));
            }
            if (y.getPoste() != null ? y.getPoste().getId() > 0 : false) {
                c.setPosteCharge(new YvsProdPosteCharge(y.getPoste().getId()));
            }
            c.setAuthor(ua);
            c.setNew_(true);
        }
        return c;
    }

    public static List<CapacitePosteCharge> buildBeanListCapacitePosteCharge(List<YvsProdCapacitePosteCharge> y) {
        List<CapacitePosteCharge> l = new ArrayList<>();
        if (y != null) {
            for (YvsProdCapacitePosteCharge c : y) {
                l.add(buildBeanCapacitePosteCharge(c));
            }
        }
        return l;
    }

    public static CentrePosteCharge buildBeanCentrePosteCharge(YvsProdCentrePosteCharge y) {
        CentrePosteCharge c = new CentrePosteCharge();
        if (y != null) {
            c.setId(y.getId());
            c.setValeur((y.getValeur() != null) ? y.getValeur() : 0);
            c.setDirect((y.getDirect() != null) ? y.getDirect() : false);
            c.setPeriode(buildBeanUniteMesure(y.getPeriode()));
            c.setCentre(UtilParam.buildBeanCentreValorisation(y.getCentreValorisation()));
            c.setPoste(buildBeanPosteCharge(y.getPosteCharge()));
        }
        return c;
    }

    public static YvsProdCentrePosteCharge buildCentrePosteCharge(CentrePosteCharge y, YvsUsersAgence ua) {
        YvsProdCentrePosteCharge c = new YvsProdCentrePosteCharge();
        if (y != null) {
            c.setId(y.getId());
            c.setValeur(y.getValeur());
            c.setDirect(y.isDirect());
            if (y.getPeriode() != null ? y.getPeriode().getId() > 0 : false) {
                c.setPeriode(new YvsBaseUniteMesure(y.getPeriode().getId(), y.getPeriode().getReference(), y.getPeriode().getLibelle()));
            }
            if (y.getCentre() != null ? y.getCentre().getId() > 0 : false) {
                c.setCentreValorisation(new YvsBaseCentreValorisation(y.getCentre().getId(), y.getCentre().getReference(), y.getCentre().getDesignation()));
            }
            if (y.getPoste() != null ? y.getPoste().getId() > 0 : false) {
                c.setPosteCharge(new YvsProdPosteCharge(y.getPoste().getId()));
            }
            c.setAuthor(ua);
            c.setNew_(true);
        }
        return c;
    }

    public static List<CentrePosteCharge> buildBeanListCentrePosteCharge(List<YvsProdCentrePosteCharge> y) {
        List<CentrePosteCharge> l = new ArrayList<>();
        if (y != null) {
            for (YvsProdCentrePosteCharge c : y) {
                l.add(buildBeanCentrePosteCharge(c));
            }
        }
        return l;
    }

    public static CentreCharge buildBeanCentreCharge(YvsProdCentreCharge y) {
        CentreCharge c = new CentreCharge();
        if (y != null) {
            c.setId(y.getId());
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setDescription(y.getDescription());
            c.setDesignation(y.getDesignation());
            c.setReference(y.getReference());
            c.setSiteProduction((y.getSiteProduction() != null) ? buildBeanSiteProduction(y.getSiteProduction()) : new SiteProduction());
        }
        return c;
    }

    public static YvsProdCentreCharge buildCentreCharge(CentreCharge y, YvsUsersAgence ua) {
        YvsProdCentreCharge c = new YvsProdCentreCharge();
        if (y != null) {
            c.setId(y.getId());
            c.setActif(y.isActif());
            c.setDescription(y.getDescription());
            c.setDesignation(y.getDesignation());
            c.setReference(y.getReference());
            if (y.getSiteProduction() != null ? y.getSiteProduction().getId() > 0 : false) {
                c.setSiteProduction(new YvsProdSiteProduction(y.getSiteProduction().getId(), y.getSiteProduction().getReference(), y.getSiteProduction().getDesignation()));
            }
            c.setAuthor(ua);
            c.setNew_(true);
        }
        return c;
    }

    public static CentreCharge buildBeanCentreCharge(YvsProdCentreCharge y, PeriodePlanificationPDP periode, Articles article) {
        CentreCharge c = new CentreCharge();
        if (y != null) {
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setDescription(y.getDescription());
            c.setDesignation(y.getDesignation());
            c.setId(y.getId());
            c.setReference(y.getReference());
            c.setSiteProduction((y.getSiteProduction() != null) ? buildBeanSiteProduction(y.getSiteProduction(), periode, article) : new SiteProduction());
//            if ((y.getYvsProdPosteChargeList() != null) ? !y.getYvsProdPosteChargeList().isEmpty() : false) {
//                c.setPosteChargeList(buildBeanListPosteCharge(y.getYvsProdPosteChargeList(), periode, article));
//            }
        }
        return c;
    }

    public static List<CentreCharge> buildBeanListCentreCharge(List<YvsProdCentreCharge> y) {
        List<CentreCharge> l = new ArrayList<>();
        if (y != null) {
            for (YvsProdCentreCharge c : y) {
                l.add(buildBeanCentreCharge(c));
            }
        }
        return l;
    }

    public static List<CentreCharge> buildBeanListCentreCharge(List<YvsProdCentreCharge> y, PeriodePlanificationPDP periode, Articles article) {
        List<CentreCharge> l = new ArrayList<>();
        if (y != null) {
            for (YvsProdCentreCharge c : y) {
                l.add(buildBeanCentreCharge(c, periode, article));
            }
        }
        return l;
    }

    public static SiteProduction buildBeanSiteProduction(YvsProdSiteProduction y) {
        SiteProduction s = new SiteProduction();
        if (y != null) {
            s.setId(y.getId());
            s.setAdresse(y.getAdresse());
            s.setDescription(y.getDescription());
            s.setDesignation(y.getDesignation());
            s.setReference(y.getReference());
        }
        return s;
    }

    public static YvsProdSiteProduction buildSiteProduction(SiteProduction y, YvsAgences ste, YvsUsersAgence ua) {
        YvsProdSiteProduction s = null;
        if (y != null) {
            s = new YvsProdSiteProduction();
            s.setId(y.getId());
            s.setAdresse(y.getAdresse());
            s.setDescription(y.getDescription());
            s.setDesignation(y.getDesignation());
            s.setReference(y.getReference());
            s.setAgence(ste);
            s.setNew_(true);
            s.setAuthor(ua);
        }
        return s;
    }

    public static SiteProduction buildBeanSiteProduction(YvsProdSiteProduction y, PeriodePlanificationPDP periode, Articles article) {
        SiteProduction s = new SiteProduction();
        if (y != null) {
            s.setId(y.getId());
            s.setAdresse(y.getAdresse());
            s.setDescription(y.getDescription());
            s.setDesignation(y.getDesignation());
            s.setReference(y.getReference());
//            if ((y.getYvsProdPosteChargeList() != null) ? !y.getYvsProdCentreChargeList().isEmpty() : false) {
////                s.setPosteChargeList(buildBeanListPosteCharge(y.getYvsProdPosteChargeList()));
//            }
//            if ((y.getYvsProdCentreChargeList() != null) ? !y.getYvsProdCentreChargeList().isEmpty() : false) {
//                s.setCentreChargeList(buildBeanListCentreCharge(y.getYvsProdCentreChargeList(), periode, article));
//            }
//            s.setAgence((y.getSociete() != null) ? buildBeanAgence(y.getSociete()) : new Agence());
        }
        return s;
    }

    public static List<SiteProduction> buildBeanListSiteProduction(List<YvsProdSiteProduction> y, PeriodePlanificationPDP periode, Articles article) {
        List<SiteProduction> l = new ArrayList<>();
        for (YvsProdSiteProduction s : y) {
            l.add(buildBeanSiteProduction(s, periode, article));
        }
        return l;
    }

    static Calendar cals;

    public static PosteCharge buildBeanPosteCharge(YvsProdPosteCharge y) {
        PosteCharge p = new PosteCharge();
        if (y != null) {
            p.setId(y.getId());
            p.setReference(y.getReference());
            p.setDesignation(y.getDesignation());
            p.setDescription(y.getDescription());
            p.setType(y.getType());
            p.setNameType(y.getTypeName());
            p.setTypeValeur(y.getTypeValeur());
            p.setPosteEquivalent(y.getPosteEquivalent() != null ? y.getPosteEquivalent().getId() : 0);
            p.setTempsAttente((y.getTempsAttente() != null) ? y.getTempsAttente() : 0);
            p.setTempsExecution(y.getTempsExecution());
            p.setTempsTransfert((y.getTempsTransfert() != null) ? y.getTempsTransfert() : 0);
            p.setTempsReglage((y.getTempsReglage() != null) ? y.getTempsReglage() : 0);
            p.setTempsRebus((y.getTempsRebus() != null) ? y.getTempsRebus() : 0);
            p.setTauxRendement((y.getTauxRendement() != null) ? y.getTauxRendement() : 0);
            p.setCapaciteQ(y.getCapaciteQ());
            p.setCapaciteH(y.getCapaciteH());
            p.setCalendrier(UtilGrh.buildBeanCalendrier(y.getCalendrier()));
            p.setSite(buildBeanSiteProduction(y.getSiteProduction()));
            p.setCentre(buildBeanCentreCharge(y.getCentreCharge()));
            p.setTiers(y.getTiers() != null ? buildBeanTiers(y.getTiers().getTiers()) : new Tiers());
            p.setEmploye(y.getEmploye() != null ? UtilGrh.buildBeanSimpleEmploye(y.getEmploye().getEmploye()) : new Employe());
            p.setMateriel(y.getMateriel() != null ? buildBeanArticles(y.getMateriel().getMateriel()) : new Articles());
            p.setEtats(y.getEtats());
            p.setCentres(y.getCentres());
            p.setCapacites(y.getCapacites());
            p.setCentreVal(UtilParam.buildBeanCentreValorisation(y.getCentreValorisation()));
        }
        return p;
    }

    public static YvsProdPosteCharge buildPosteCharge(PosteCharge y, YvsUsersAgence u) {
        YvsProdPosteCharge p = new YvsProdPosteCharge();
        if (y != null) {
            p.setId(y.getId());
            p.setReference(y.getReference());
            p.setDesignation(y.getDesignation());
            p.setDescription(y.getDescription());
            p.setType(y.getType());
            p.setTempsAttente(y.getTempsAttente());
            p.setTempsExecution(y.getTempsExecution());
            p.setTempsTransfert(y.getTempsTransfert());
            p.setTempsReglage(y.getTempsReglage());
            p.setTypeValeur(y.getTypeValeur());
            p.setTempsRebus(y.getTempsRebus());
            p.setTauxRendement(y.getTauxRendement());
            p.setCapaciteQ(y.getCapaciteQ());
            p.setCapaciteH(y.getCapaciteH());
            if (y.getPosteEquivalent() > 0) {
                p.setPosteEquivalent(new YvsProdPosteCharge(y.getPosteEquivalent()));
            }
            if (y.getCentreVal() != null ? y.getCentreVal().getId() > 0 : false) {
                p.setCentreValorisation(new YvsBaseCentreValorisation(y.getCentreVal().getId(), y.getCentreVal().getReference(), y.getCentreVal().getDesignation()));
            }
            if (y.getCalendrier() != null ? y.getCalendrier().getId() > 0 : false) {
                p.setCalendrier(new YvsCalendrier(y.getCalendrier().getId(), y.getCalendrier().getReference()));
            }
            if (y.getSite() != null ? y.getSite().getId() > 0 : false) {
                p.setSiteProduction(new YvsProdSiteProduction(y.getSite().getId(), y.getSite().getReference(), y.getSite().getDesignation()));
            }
            if (y.getCentre() != null ? y.getCentre().getId() > 0 : false) {
                p.setCentreCharge(new YvsProdCentreCharge(y.getCentre().getId(), y.getCentre().getReference(), y.getCentre().getDesignation()));
            }
            p.setAuthor(u);
            p.setNew_(true);
        }
        return p;
    }

    public static PosteCharge buildBeanPosteCharge(YvsProdPosteCharge y, PeriodePlanificationPDP periode, Articles article) {
        PosteCharge p = new PosteCharge();
        if (y != null) {
            p.setDesignation(y.getDesignation());
            p.setReference(y.getReference());
            p.setDescription(y.getDescription());
            p.setTauxRendement((y.getTauxRendement() != null) ? y.getTauxRendement() : 0);
            p.setId(y.getId());
            p.setTypeValeur(y.getTypeValeur());
            p.setTauxChargeMax(p.getChargeH() / p.getCapaciteH());
            p.setTempsAttente((y.getTempsAttente() != null) ? y.getTempsAttente() : 0);
            p.setTempsRebus((y.getTempsRebus() != null) ? y.getTempsRebus() : 0);
            p.setTempsReglage((y.getTempsReglage() != null) ? y.getTempsReglage() : 0);
            p.setTempsTransfert((y.getTempsTransfert() != null) ? y.getTempsTransfert() : 0);
            p.setTiers(y.getTiers() != null ? buildBeanTiers(y.getTiers().getTiers()) : new Tiers());
            p.setEmploye(y.getEmploye() != null ? UtilGrh.buildBeanSimpleEmploye(y.getEmploye().getEmploye()) : new Employe());
            p.setMateriel(y.getMateriel() != null ? buildBeanArticles(y.getMateriel().getMateriel()) : new Articles());
//            p.setCalendrier((y.getCalendrier() != null) ? UtilParametre.buildBeanCalendrier(y.getCalendrier()) : new Calendrier());
//            p.setCentre((y.getSiteProduction() != null) ? buildBeanSiteProduction(y.getSiteProduction(), periode, article) : new SiteProduction());
            p.setCentre((y.getCentreCharge() != null) ? buildBeanCentreCharge(y.getCentreCharge()) : new CentreCharge());
            p.setEtats(y.getEtats());
            p.setCentres(y.getCentres());
            double capacite = 0;
            int mods = 0;
            if ((p.getCalendrier() != null) ? p.getCalendrier().getId() == 0 : true) {
                Date dateD = new Date();
                Calendar cal = Util.dateToCalendar(dateD);
                cal.add(Calendar.MONTH, 1);
                cal.add(Calendar.DATE, -1);
                Date dateF = cal.getTime();
                int jour = Util.ecartOnDate(dateD, dateF, "jour");
                capacite = capacite * jour;
            } else {
                if ((p.getCalendrier().getListJoursOuvres() != null) ? !p.getCalendrier().getListJoursOuvres().isEmpty() : false) {
                    capacite = 0;
                    if ((periode != null) ? periode.getId() != 0 : false) {
                        //periode sur l'année 
                        if (periode.getIndicatif() == 1) {
                            for (YvsJoursOuvres j : p.getCalendrier().getListJoursOuvres()) {
                                int jour = Util.getOccurenceDay(j.getJour(), periode.getDateDebut(), periode.getDateFin());
                                capacite += j.getDureeTravail() * jour;
                            }
                        } //periode sur le mois 
                        else if (periode.getIndicatif() == 2) {
                            for (YvsJoursOuvres j : p.getCalendrier().getListJoursOuvres()) {
                                int jour = Util.getOccurenceDay(j.getJour(), periode.getDateDebut(), periode.getDateFin());
                                capacite += j.getDureeTravail() * jour;
                            }
                        }//periode sur la semaine 
                        else if (periode.getIndicatif() == 3) {
                            for (YvsJoursOuvres j : p.getCalendrier().getListJoursOuvres()) {
                                int jour = Util.getOccurenceDay(j.getJour(), periode.getDateDebut(), periode.getDateFin());
                                capacite += j.getDureeTravail() * jour;
                            }
                        }//periode sur le journée 
                        else if (periode.getIndicatif() == 4) {
                            capacite = p.getCalendrier().getListJoursOuvres().get(0).getDureeTravail();
                        } else {
                            Date dateD = new Date();
                            Calendar cal = Util.dateToCalendar(dateD);
                            cal.add(Calendar.MONTH, 1);
                            cal.add(Calendar.DATE, -1);
                            Date dateF = cal.getTime();
                            for (YvsJoursOuvres j : p.getCalendrier().getListJoursOuvres()) {
                                int jour = Util.getOccurenceDay(j.getJour(), dateD, dateF);
                                capacite += j.getDureeTravail() * jour;
                            }
                        }
                    } else {
                        Date dateD = new Date();
                        Calendar cal = Util.dateToCalendar(dateD);
                        cal.add(Calendar.MONTH, 1);
                        cal.add(Calendar.DATE, -1);
                        Date dateF = cal.getTime();
                        for (YvsJoursOuvres j : p.getCalendrier().getListJoursOuvres()) {
                            int jour = Util.getOccurenceDay(j.getJour(), dateD, dateF);
                            capacite += j.getDureeTravail() * jour;
                        }
                    }
                } else {
                    capacite = 0;
                }
            }
            if (capacite == Long.MAX_VALUE) {
                capacite = 0;
            }
            p.setCapaciteH(capacite);
            p.setModsQ(mods);
            p.setType(y.getType());
            switch (p.getType()) {
                case "H":
                    p.setNameType("Main Oeuvre");
                    break;
                case "S":
                    p.setNameType("Sous-Traitant");
                    p.setModsQ(1);
                    break;
                case "MH":
                    p.setNameType("Machine(s) + Main(s) Oeuvre(s)");
                    break;
                default:
                    p.setNameType("Machine");
                    p.setType("M");
                    p.setModsQ(1);
                    break;
            }
            p.setCapacites(y.getCapacites());
            if ((article != null) ? article.getId() != 0 : false) {
                if (y.getCapacites() != null) {
                    p.setCapaciteQ((y.getCapaciteQ() != null) ? y.getCapaciteQ() : 0);
                    for (YvsProdCapacitePosteCharge c : y.getCapacites()) {
                        Articles a = buildBeanArticles(c.getArticles());
                        if (a.getId() == article.getId()) {
                            p.setCapaciteQ(c.getCapaciteQ());
                            break;
                        }
                    }
                } else {
                    p.setCapaciteQ((y.getCapaciteQ() != null) ? y.getCapaciteQ() : 0);
                }
            } else {
                p.setCapaciteQ((y.getCapaciteQ() != null) ? y.getCapaciteQ() : 0);
            }
        }
        return p;
    }

    public static List<PosteCharge> buildBeanListPosteCharge(List<YvsProdPosteCharge> y, PeriodePlanificationPDP periode, Articles article) {
        List<PosteCharge> l = new ArrayList<>();
        if (y != null) {
            for (YvsProdPosteCharge p : y) {
                l.add(buildBeanPosteCharge(p, periode, article));
            }
        }
        return l;
    }
//
//    /*
//     FIN GESTION DE POSTE DE CHARGE
//     */
//    /*
//     DEBUT GESTION DES GAMMES
//
//     */
//
//    public static IndicateurReussite buildBeanIndicateurReussite(YvsBaseIndicateurReussite y) {
//        IndicateurReussite i = new IndicateurReussite();
//        if (y != null) {
//            i.setId(y.getId());
//            i.setDescription(y.getDescription());
//            i.setDesignation(y.getDesignation());
//            i.setType(y.getType());
//            i.setValeur((y.getValeur() != null) ? y.getValeur() : 0);
//        }
//        return i;
//    }
//
//    public static List<IndicateurReussite> buildBeanListIndicateurReussite(List<YvsBaseIndicateurReussite> y) {
//        List<IndicateurReussite> l = new ArrayList<>();
//        if (y != null) {
//            for (YvsBaseIndicateurReussite p : y) {
//                l.add(buildBeanIndicateurReussite(p));
//            }
//        }
//        return l;
//    }
//
////    public static ValeurIndicateur buildBeanIndicateurPhase(YvsProdIndicateurOp y) {
////        ValeurIndicateur i = new ValeurIndicateur();
////        if (y != null) {
////            i.setId(y.getId());
////            i.setDescription(y.getCommentaire());
////            i.setReference(y.getTypeIndicateur());
////            i.setValeur((y.getValeur() != null) ? y.getValeur() : 0);
////            i.setIndicateurReussite((y.getIndicateurReussite() != null) ? buildBeanIndicateurReussite(y.getIndicateurReussite()) : new IndicateurReussite());
////        }
////        return i;
////    }
//
//    public static List<IndicateurPhase> buildBeanListIndicateurPhase(List<YvsProdIndicateurOp> y) {
//        List<IndicateurPhase> l = new ArrayList<>();
//        if (y != null) {
//            for (YvsProdIndicateurOp p : y) {
//                l.add(buildBeanIndicateurPhase(p));
//            }
//        }
//        return l;
//    }

    public static DocumentTechnique buildBeanDocumentTechnique(YvsProdDocumentTechnique y) {
        DocumentTechnique d = new DocumentTechnique();
        if (y != null) {
            d.setId(y.getId());
            d.setDescription(y.getDescription());
            d.setDesignation(y.getDesignation());
            d.setFichier(y.getFichier());
            d.setReference(y.getReference());
        }
        return d;
    }

    public static List<DocumentTechnique> buildBeanListDocumentTechnique(List<YvsProdDocumentTechnique> y) {
        List<DocumentTechnique> l = new ArrayList<>();
        if (y != null) {
            for (YvsProdDocumentTechnique p : y) {
                l.add(buildBeanDocumentTechnique(p));
            }
        }
        return l;
    }

    public static YvsProdOperationsGamme buildOperationGammes(OperationsGamme y) {
        YvsProdOperationsGamme p = new YvsProdOperationsGamme();
        if (y != null) {
            p.setId(y.getId());
            p.setDescription(y.getDescription());
            p.setCodeRef(y.getReference());
            p.setNumero(y.getNumero());
            p.setCadence(y.getCadence());
            p.setDateSave(y.getDateSave());
            p.setQuantiteBase(y.getQuantiteBase());
            p.setQuantiteMin(y.getQuantiteMinimale());
            p.setTauxEfficience(y.getTauxEfficience());
            p.setTauxPerte(y.getTauxDePerte());
            p.setTempsOperation(y.getDureeOperation());
            p.setTempsReglage(y.getDureeReglage());
            p.setTypeTemps(y.getTypeDeTemps());
            p.setDateUpdate(new Date());
            p.setTypeCout(y.getTypeCout());
            p.setNew_(true);
            p.setActif(y.isActif());
        }
        return p;
    }

    public static OperationsGamme buildSimpleBeanPhaseGamme(YvsProdOperationsGamme y) {
        OperationsGamme p = new OperationsGamme();
        if (y != null) {
            p.setId(y.getId());
            p.setDescription(y.getDescription());
            p.setReference(y.getCodeRef());
            p.setNumero(y.getNumero());
            p.setCadence(y.getCadence());
            p.setDateSave(y.getDateSave());
            p.setQuantiteBase(y.getQuantiteBase());
            p.setQuantiteMinimale(y.getQuantiteMin());
            p.setTauxEfficience(y.getTauxEfficience());
            p.setTauxDePerte(y.getTauxPerte());
            p.setDureeOperation(y.getTempsOperation());
            p.setDureeReglage(y.getTempsReglage());
            p.setTypeDeTemps(y.getTypeTemps());
            p.setDocuments(y.getDocuments());
            p.setPostes(y.getPostes());
            p.setComposants(y.getComposants());
            p.setTypeCout(y.getTypeCout());
            p.setActif(y.getActif());
        }
        return p;
    }

    public static PosteOperation buildBeanPostePhase(YvsProdPosteOperation y) {
        PosteOperation p = new PosteOperation();
        if (y != null) {
            p.setId(y.getId());
            p.setNombre((y.getNombre() != null) ? ((y.getNombre() > 1) ? y.getNombre() : 1) : 1);
            p.setPoste((y.getPosteCharge() != null) ? buildBeanPosteCharge(y.getPosteCharge()) : new PosteCharge());
            p.setTypeCharge(y.getTypeCharge());
            p.setDateSave(y.getDateSave());
        }
        return p;
    }

//    PosteOperation static PostePhase buildBeanPostePhase(YvsProdPosteOperation y, PeriodePlanificationPDP periode, ArticlesPosteOperation {
//        PosteOperationPhase p = new PostePhase();
//        if (y != null) {
//            p.setId(y.getId());
//            p.setNombre((y.getNombre() != null) ? ((y.getNombre() > 1) ? y.getNombre() : 1) : 1);
//            p.setPoste((y.getPosteCharge() != null) ? buildBeanPosteCharge(y.getPosteCharge(), periode, article) : new PosteCharge());
//            p.setCapacite_h(p.getPoste().getCapaciteH());
//            p.setCapacite_q(p.getPoste().getCapaciteQ());
//            p.setMods_h(p.getPoste().getModsH());
//            p.setMods_q(p.getPoste().getModsQ());
//            p.setTemps_rebus(p.getPoste().getTempsRebus());
//        }
//        return p;
//        PosteOperationublic
//
//    static List<PostePhase> buildBeanListPostePhase(List<YvsProdPoPosteOperationon> y) {
//        List<PostePhase> l = new ArrayList<>();
//        if (y != null) {
//            for (YvsProdPosteOperation p : y) {
//                l.add(buildBeanPostePhase(p));
//            }
//        }
//        retuPosteOperation
//    }
//
//    public static List<PostePhase> buildBeanListPostePhase(List<YvsProdPosteOperation> y, PeriodePlanificationPDP pePosteOperationicles 
//        article) {
//        List<PostePhase> l = new ArrayList<>();
//        if (y != null) {
//            for (YvsProdPosteOperation p : y) {
//                l.add(buildBeanPostePhase(p, periode, article));
//            }
//        }
//        return l;
//    }
    public static OperationsGamme buildBeanPhaseGamme(YvsProdOperationsGamme y, PeriodePlanificationPDP periode, Articles article) {
        OperationsGamme p = new OperationsGamme();
        if (y != null) {
            p.setId(y.getId());
            p.setDescription(y.getDescription());
            p.setReference(y.getCodeRef());
            p.setNumero(y.getNumero());
            List<List<YvsProdPosteOperation>> list = new ArrayList<>();
            List<YvsProdPosteOperation> l;
            boolean trouv = false, add = false;
            for (YvsProdPosteOperation pp : p.getPostes()) {
                if (list.isEmpty()) {
                    l = new ArrayList<>();
                    l.add(pp);
                    list.add(l);
                } else {
//                    for (List<PostePhase>PosteOperation {
//                        for (PostePhase i : e) {
//                            if (pp.getNiveau() == i.getNiveau()) {
//                                trouv = true;
//                                break;
//                            }
//                        }
//                        add = !trouv;
//                        if (!trouv) {
//                            e.add(pp);
//                            break;
//                        } else {
//                            trouv = false;
//                        }
//                    }
                    if (!add) {
                        l = new ArrayList<>();
                        l.add(pp);
                        list.add(l);
                    }
                }
            }
            double capacite_h = 0, capacite_q = Long.MAX_VALUE;
            int mod_q = 0;
//            if (!list.isEmpty()) {
//                if PosteOperation() == 1) {
//                    for (PostePhase pp : list.get(0)) {
//                        capacite_h += (pp.getCapacite_h() * pp.getNombre()) - pp.getTemps_rebus();
//                        if (capacite_q > pp.getCapacite_q()) {
//                            capacite_q = pp.getCapacite_q();
//                            mod_q = pp.getMods_q();
//                        }
//                    }
//                } else {
//                    double c;
//                    for (List<PostePhase> e : list) {
//                        c = 0;
//                        if PosteOperationy()) {
//                            for (PostePhase pp : e) {
//                                c += (pp.getCapacite_h() * pp.getNombre()) - pp.getTemps_rebus();
//                            }
//                            if (c > capacite_h) {
//                                capacite_h = c;
//                                capacite_q =PosteOperationVALUE;
//                                for (PostePhase pp : e) {
//                                    if (capacite_q > pp.getCapacite_q()) {
//                                        capacite_q = pp.getCapacite_q();
//                                        mod_q = pp.getMods_q();
//                                    }
//                                }
//                            }
//                            if (c < 1) {
//                                capacite_q = e.get(e.size() - 1).getCapacite_q();
//                            }
//                        } else {
//                            capacite_q = Long.MAX_VALUE;
//                        }
//                    }
//                }
//            } else {
//                capacite_q = 0;
//            }
            if (capacite_h < 0) {
                capacite_h = 0;
            }
            if (capacite_q < 0 || capacite_q == Long.MAX_VALUE) {
                capacite_q = 0;
            }
//            p.setCapacite_h(capacite_h);
//            p.setCapacite_q(capacite_q);
//            p.setMods_q(mod_q);
//            System.err.println("phase : " + p.getDesignation() + " - list : " + list.size() + " - p.setCapacite_h : "
//                    + p.getCapacite_h_() + " - p.setCapacite_q : " + p.getCapacite_q_() + " - p.setMods_q : " + p.getMods_q());
        }
        return p;
    }

    public static List<OperationsGamme> buildBeanListPhaseGamme(List<YvsProdOperationsGamme> y, PeriodePlanificationPDP periode, Articles article) {
        List<OperationsGamme> l = new ArrayList<>();
        if (y != null) {
            for (YvsProdOperationsGamme p : y) {

            }
            Collections.sort(l, new OperationsGamme());
        }
        return l;
    }

    public static GammeArticle buildSimpleBeanGammeArticle(YvsProdGammeArticle y) {
        GammeArticle g = buildSimpleBeanGammeArticleWA(y);
        if (y != null) {
            g.setArticle((y.getArticle() != null) ? buildBeanArticles(y.getArticle()) : new Articles());
        }
        return g;
    }

    public static GammeArticle buildSimpleBeanGammeArticleWA(YvsProdGammeArticle y) {
        GammeArticle g = new GammeArticle();
        if (y != null) {
            g.setId(y.getId());
            g.setDescription(y.getDescription());
            g.setDesignation(y.getDesignation());
            g.setReference(y.getCodeRef());
            g.setActif((y.getActif() != null) ? y.getActif() : true);
            g.setPrincipal((y.getPrincipal() != null) ? y.getPrincipal() : false);
            g.setDateSave(y.getDateSave());
            g.setUniteTemps(buildBeanUniteMesure(y.getUniteTemps()));
            g.setPermanant(y.getPermanant());
            g.setDebutValidite(y.getDebutValidite());
            g.setFinValidite(y.getFinValidite());
            g.setOperations(y.getOperations());
            g.setSites(y.getSites());
            g.setMasquer(y.getMasquer());
        }
        return g;
    }

    public static YvsProdGammeArticle buildGammeArticle(GammeArticle y, YvsUsersAgence ua) {
        YvsProdGammeArticle g = new YvsProdGammeArticle();
        if (y != null) {
            g.setId(y.getId());
            if ((y.getArticle() != null) ? y.getArticle().getId() > 0 : false) {
                g.setArticle(UtilProd.buildEntityArticle(y.getArticle()));
            }
            g.setDescription(y.getDescription());
            g.setDesignation(y.getDesignation());
            g.setCodeRef(y.getReference());
            g.setPrincipal(y.isPrincipal());
            g.setActif(y.isActif());
            g.setDateSave(y.getDateSave());
            g.setPermanant(y.isPermanant());
            g.setDebutValidite(y.getDebutValidite());
            g.setFinValidite(y.getFinValidite());
            g.setMasquer(y.isMasquer());
            if ((y.getUniteTemps() != null) ? y.getUniteTemps().getId() > 0 : false) {
                g.setUniteTemps(buildUniteMasse(y.getUniteTemps(), ua, null));
            }
            g.setAuthor(ua);
        }
        return g;
    }

    public static List<OperationsGamme> buildSimpleBeanListPhaseGamme(List<YvsProdOperationsGamme> y) {
        List<OperationsGamme> l = new ArrayList<>();
        if (y != null) {
            for (YvsProdOperationsGamme p : y) {
                l.add(buildSimpleBeanPhaseGamme(p));
            }
        }
        return l;
    }

    public static GammeArticle buildBeanGammeArticle(YvsProdGammeArticle y, PeriodePlanificationPDP periode, Articles article) {
        GammeArticle g = new GammeArticle();
        if (y != null) {
            g.setId(y.getId());
            g.setArticle((y.getArticle() != null) ? buildBeanArticles(y.getArticle()) : new Articles());
            g.setDescription(y.getDescription());
            g.setDesignation(y.getDesignation());
            g.setReference(y.getCodeRef());
            g.setActif((y.getActif() != null) ? y.getActif() : true);
            g.setPrincipal((y.getPrincipal() != null) ? y.getPrincipal() : false);
            if ((y.getOperations() != null) ? !y.getOperations().isEmpty() : false) {
//                g.setPhases(buildBeanListPhaseGamme(y.getPhases(), periode, article));
            }
        }
        return g;
    }

    public static List<GammeArticle> buildBeanListGammeArticle(List<YvsProdGammeArticle> y, PeriodePlanificationPDP periode, Articles article) {
        List<GammeArticle> l = new ArrayList<>();
        if (y != null) {
            for (YvsProdGammeArticle p : y) {
                l.add(buildBeanGammeArticle(p, periode, article));
            }
        }
        return l;
    }
//
//
//    /*
//     FIN GESTION DES GAMMES
    //    */

    public static Conditionnement buildBeanConditionnement(YvsBaseConditionnement c) {
        Conditionnement r = new Conditionnement();
        if (c != null) {
            r.setId(c.getId());
            r.setArticle(buildBeanArticleWithoutList(c.getArticle()));
            r.setUnite(buildSimpleBeanUniteMesure(c.getUnite()));
            r.setNaturePrixMin(c.getNaturePrixMin());
            r.setPrix(c.getPrix());
            r.setPrixAchat(c.getPrixAchat());
            r.setPrixProd(c.getPrixProd());
            r.setPrixMin(c.getPrixMin());
            r.setRemise(c.getRemise());
            r.setByVente(c.getByVente());
            r.setByAchat(c.getByAchat());
            r.setByProd(c.getByProd());
            r.setDefaut(c.getDefaut());
            r.setPhoto(c.getPhoto());
            r.setCodeBarre(c.getCodeBarre());
            r.setDateSave(c.getDateSave());
            r.setMargeMin(c.getMargeMin());
            r.setProportionPua(c.getProportionPua());
            r.setTauxPua(c.getTauxPua());
            r.setPacks(c.getPacks());
        }
        return r;
    }

    public static Conditionnement buildBeanUnite(YvsBaseConditionnement c) {
        Conditionnement r = new Conditionnement();
        if (c != null) {
            r.setId(c.getId());
//            r.setArticle(buildBeanArticleWithoutList(c.getArticle()));
            r.setUnite(buildSimpleBeanUniteMesure(c.getUnite()));
            r.setNaturePrixMin(c.getNaturePrixMin());
            r.setPrix(c.getPrix());
            r.setPrixAchat(c.getPrixAchat());
            r.setPrixProd(c.getPrixProd());
            r.setPrixMin(c.getPrixMin());
            r.setRemise(c.getRemise());
            r.setByVente(c.getByVente());
            r.setByAchat(c.getByAchat());
            r.setByProd(c.getByProd());
            r.setDefaut(c.getDefaut());
            r.setPhoto(c.getPhoto());
            r.setCodeBarre(c.getCodeBarre());
            r.setDateSave(c.getDateSave());
            r.setMargeMin(c.getMargeMin());
            r.setProportionPua(c.getProportionPua());
            r.setTauxPua(c.getTauxPua());
        }
        return r;
    }

    public static Conditionnement buildSimpleBeanConditionnement(YvsBaseConditionnement c) {
        Conditionnement r = new Conditionnement();
        if (c != null) {
            r.setId(c.getId());
            r.setUnite(buildSimpleBeanUniteMesure(c.getUnite()));
            r.setNaturePrixMin(c.getNaturePrixMin());
            r.setPrix(c.getPrix());
            r.setPrixAchat(c.getPrixAchat());
            r.setPrixProd(c.getPrixProd());
            r.setPrixMin(c.getPrixMin());
            r.setRemise(c.getRemise());
            r.setByVente(c.getByVente());
            r.setByAchat(c.getByAchat());
            r.setByProd(c.getByProd());
            r.setDefaut(c.getDefaut());
            r.setPhoto(c.getPhoto());
            r.setCodeBarre(c.getCodeBarre());
            r.setDateSave(c.getDateSave());
            r.setMargeMin(c.getMargeMin());
            r.setActif(c.getActif());
            r.setProportionPua(c.getProportionPua());
            r.setTauxPua(c.getTauxPua());
        }
        return r;
    }

    public static YvsBaseConditionnement buildConditionnement(Conditionnement c, YvsUsersAgence ua) {
        YvsBaseConditionnement r = new YvsBaseConditionnement();
        if (c != null) {
            r.setId(c.getId());
            r.setNaturePrixMin(c.getNaturePrixMin());
            r.setPrix(c.getPrix());
            r.setPrixAchat(c.getPrixAchat());
            r.setPrixProd(c.getPrixProd());
            r.setPrixMin(c.getPrixMin());
            r.setRemise(c.getRemise());
            r.setByVente(c.isByVente());
            r.setByAchat(c.isByAchat());
            r.setByProd(c.isByProd());
            r.setDefaut(c.isDefaut());
            r.setPhoto(c.getPhoto());
            r.setMargeMin(c.getMargeMin());
            r.setCodeBarre(c.getCodeBarre());
            r.setProportionPua(c.isProportionPua());
            r.setTauxPua(c.getTauxPua());
            r.setActif(c.getActif());
            if (c.getArticle() != null ? c.getArticle().getId() > 0 : false) {
                r.setArticle(new YvsBaseArticles(c.getArticle().getId(), c.getArticle().getRefArt(), c.getArticle().getDesignation()));
            }
            if ((c.getUnite() != null) ? c.getUnite().getId() != 0 : false) {
                r.setUnite(new YvsBaseUniteMesure(c.getUnite().getId(), c.getUnite().getReference(), c.getUnite().getLibelle()));
            }
            r.setAuthor(ua);
            r.setDateUpdate(new Date());
            r.setDateSave(c.getDateSave());
            r.setNew_(true);
        }
        return r;
    }

    public static List<Conditionnement> buildBeanListConditionnement(List<YvsBaseConditionnement> list) {
        List<Conditionnement> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseConditionnement c : list) {
                r.add(buildBeanConditionnement(c));
            }
        }
        return r;
    }

    public static YvsBaseFamilleArticle buildFamilleArticle(FamilleArticle f, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        YvsBaseFamilleArticle r = new YvsBaseFamilleArticle();
        if (f != null) {
            r.setDescription(f.getDescription());
            r.setDesignation(f.getDesignation());
            r.setId(f.getId());
            r.setReferenceFamille(f.getReference());
            r.setActif(f.isActif());
            r.setDateSave(f.getDateSave());
            r.setDateUpdate(new Date());
            r.setPrefixe(f.getPrefixe());
            if (f.getParentFamille() != null ? f.getParentFamille().getId() > 0 : false) {
                r.setFamilleParent(new YvsBaseFamilleArticle(f.getParentFamille().getId(), f.getParentFamille().getReference(), f.getParentFamille().getDesignation()));
            }
            r.setSociete(currentScte);
            r.setAuthor(currentUser);
            r.setNew_(true);
        }
        return r;
    }

    public static YvsBaseFamilleArticle buildFamilleArticle(FamilleArticle f, YvsSocietes s, YvsUsersAgence ua) {
        return buildFamilleArticle(f, ua, s);
    }

    public static FamilleArticle buildSimpleBeanFamilleArticle(YvsBaseFamilleArticle f) {
        FamilleArticle r = new FamilleArticle();
        if (f != null) {
            r.setDescription(f.getDescription());
            r.setDesignation(f.getDesignation());
            r.setId(f.getId());
            r.setActif(f.getActif());
            r.setDateSave(f.getDateSave());
            r.setDateUpdate(f.getDateUpdate());
        }
        return r;
    }

    public static FamilleArticle buildBeanFamilleArticle(YvsBaseFamilleArticle f) {
        FamilleArticle r = buildSimpleBeanFamilleArticle(f);
        if (f != null) {
            r.setPrefixe(f.getPrefixe());
            r.setReference(f.getReferenceFamille());
            if (f.getFamilleParent() != null ? f.getFamilleParent().getId() > 0 : false) {
                r.setParentFamille(new FamilleArticle(f.getFamilleParent().getId(), f.getFamilleParent().getReferenceFamille(), f.getFamilleParent().getDesignation(), f.getFamilleParent().getDescription()));
            } else {
                r.setParentFamille(new FamilleArticle());
            }
            r.setUpdate(true);
        }
        return r;
    }

    public static List<FamilleArticle> buildBeanListFamilleArticle(List<YvsBaseFamilleArticle> list) {
        List<FamilleArticle> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseFamilleArticle c : list) {
                r.add(buildBeanFamilleArticle(c));
            }
        }
        return r;
    }

    public static GroupeArticle buildSimpleBeanGroupeArticle(YvsBaseGroupesArticle g) {
        GroupeArticle r = new GroupeArticle();
        if (g != null) {
            r.setId(g.getId());
            r.setDescription(g.getDescription());
            r.setDesignation(g.getDesignation());
            r.setReference(g.getRefgroupe());
            r.setDateSave(g.getDateSave());
            r.setDateUpdate(g.getDateUpdate());
            r.setSous(g.getSous());
            r.setActif(g.getActif());
        }
        return r;
    }

    public static GroupeArticle buildBeanGroupeArticle(YvsBaseGroupesArticle g) {
        GroupeArticle r = buildSimpleBeanGroupeArticle(g);
        if (g != null) {
            r.setPhoto(g.getPhoto());
            if (g.getGroupeParent() != null ? (g.getGroupeParent().getId() != null ? g.getGroupeParent().getId() > 0 : false) : false) {
                r.setParent(new GroupeArticle(g.getGroupeParent().getId(), g.getGroupeParent().getRefgroupe(), g.getGroupeParent().getDesignation()));
            } else {
                r.setParent(new GroupeArticle());
            }
        }
        return r;
    }

    public static YvsBaseGroupesArticle buildGroupeArticle(GroupeArticle g, YvsSocietes s, YvsUsersAgence ua) {
        YvsBaseGroupesArticle r = new YvsBaseGroupesArticle();
        if (g != null) {
            r.setId(g.getId());
            r.setDescription(g.getDescription());
            r.setDesignation(g.getDesignation());
            r.setRefgroupe(g.getReference());
            r.setDateSave(g.getDateSave());
            r.setDateUpdate(new Date());
            r.setActif(g.isActif());
            r.setPhoto(g.getPhoto());
            if (g.getParent() != null ? g.getParent().getId() > 0 : false) {
                r.setGroupeParent(new YvsBaseGroupesArticle(g.getParent().getId(), g.getParent().getReference(), g.getParent().getDesignation()));
            }
            r.setSociete(s);
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static YvsBaseFamilleArticle buildBeanFamilleArticle(FamilleArticle g) {
        YvsBaseFamilleArticle r = new YvsBaseFamilleArticle();
        if (g != null) {
            r.setDescription(g.getDescription());
            r.setDesignation(g.getDesignation());
            r.setId(g.getId());
            r.setReferenceFamille(g.getReference());
        }
        return r;
    }

    public static List<GroupeArticle> buildBeanListGroupeArticle(List<YvsBaseGroupesArticle> list) {
        List<GroupeArticle> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseGroupesArticle c : list) {
                r.add(buildBeanGroupeArticle(c));
            }
        }
        return r;
    }

    public static ArticleFournisseur buildBeanArticleFournisseur(YvsBaseArticleFournisseur f) {
        ArticleFournisseur r = new ArticleFournisseur();
        if (f != null) {
            r.setId(f.getId());
            r.setDelaiLivraison(f.getDelaiLivraison());
            r.setDureeGarantie(f.getDureeGarantie());
            r.setDureeVie(f.getDureeVie());
            r.setArticle(buildSimpleBeanArticles(f.getArticle()));
//            r.setFournisseur((f.getFournisseur() != null) ? buildBeanTiers(f.getFournisseur()) : new Fournisseur());
        }
        return r;
    }

    public static List<ArticleFournisseur> buildBeanListArticleFournisseur(List<YvsBaseArticleFournisseur> list) {
        List<ArticleFournisseur> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseArticleFournisseur c : list) {
                r.add(buildBeanArticleFournisseur(c));
            }
        }
        return r;
    }

    public static ArticlePack buildBeanArticlePack(YvsBaseArticlePack y) {
        ArticlePack r = new ArticlePack();
        if (y != null) {
            r.setId(y.getId());
            r.setArticle(buildBeanConditionnement(y.getArticle()));
            r.setDesignation(y.getDesignation());
            r.setPhoto(y.getPhoto());
            r.setMontant(y.getMontant());
            r.setDateSave(y.getDateSave());
            r.setContenus(y.getContenus());
        }
        return r;
    }

    public static YvsBaseArticlePack buildArticlePack(ArticlePack y, YvsUsersAgence ua) {
        YvsBaseArticlePack r = new YvsBaseArticlePack();
        if (y != null) {
            r.setId(y.getId());
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                r.setArticle(buildConditionnement(y.getArticle(), ua));
            }
            r.setDesignation(y.getDesignation());
            r.setPhoto(y.getPhoto());
            r.setMontant(y.getMontant());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static ContenuPack buildBeanContenuPack(YvsBaseArticleContenuPack y) {
        ContenuPack r = new ContenuPack();
        if (y != null) {
            r.setId(y.getId());
            r.setUnite(buildBeanConditionnement(y.getArticle()));
            r.setArticle(r.getUnite().getArticle());
            r.setPack(new ArticlePack(y.getPack() != null ? y.getPack().getId() : 0));
            r.setMontant(y.getMontant());
            r.setQuantite(y.getQuantite());
            r.setQuantiteMax(y.getQuantiteMax());
            r.setDateSave(y.getDateSave());
        }
        return r;
    }

    public static YvsBaseArticleContenuPack buildContenuPack(ContenuPack y, YvsUsersAgence ua) {
        YvsBaseArticleContenuPack r = new YvsBaseArticleContenuPack();
        if (y != null) {
            r.setId(y.getId());
            if (y.getUnite() != null ? y.getUnite().getId() > 0 : false) {
                r.setArticle(buildConditionnement(y.getUnite(), ua));
            }
            if (y.getPack() != null ? y.getPack().getId() > 0 : false) {
                r.setPack(new YvsBaseArticlePack(y.getPack().getId()));
            }
            r.setMontant(y.getMontant());
            r.setQuantite(y.getQuantite());
            r.setQuantiteMax(y.getQuantiteMax());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static CodeBarre buildBeanCodeBarre(YvsBaseArticleCodeBarre y) {
        CodeBarre r = new CodeBarre();
        if (y != null) {
            r.setId(y.getId());
            r.setCodeBarre(y.getCodeBarre());
            r.setDescription(y.getDescription());
            r.setArticle(new Articles(y.getConditionnement().getId()));
            r.setDateSave(y.getDateSave());
        }
        return r;
    }

    public static YvsBaseArticleCodeBarre buildCodeBarre(CodeBarre y, YvsUsersAgence ua) {
        YvsBaseArticleCodeBarre r = new YvsBaseArticleCodeBarre();
        if (y != null) {
            r.setId(y.getId());
            r.setCodeBarre(y.getCodeBarre());
            r.setDescription(y.getDescription());
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                r.setConditionnement(new YvsBaseConditionnement(y.getArticle().getId()));
            }
            r.setAuthor(ua);
            r.setDateUpdate(new Date());
            r.setDateSave(y.getDateSave());
            r.setNew_(true);
        }
        return r;
    }

    public static Articles buildSimpleBeanArticles(YvsBaseArticles a) {
        Articles r = buildBeanArticleWithoutList(a);
        if (a != null) {
            r.setTags(a.getTags());
            r.setConditionnements(a.getConditionnements());
        }
        return r;
    }

    public static Articles buildBeanArticleWithoutList(YvsBaseArticles a) {
        Articles r = new Articles();
        if (a != null) {
            r.setCategorie(a.getCategorie());
            r.setDescription(a.getDescription());
            r.setDesignation(a.getDesignation());
            r.setDelaiLivraison((a.getDelaiLivraison() != null) ? a.getDelaiLivraison() : 0);
            r.setId(a.getId());
            r.setMasseNet((a.getMasseNet() != null) ? a.getMasseNet() : 0);
            r.setMethodeVal(a.getMethodeVal());
            r.setActif((a.getActif() != null) ? a.getActif() : false);
            r.setChangePrix(a.getChangePrix());
            r.setPuvTtc(a.getPuvTtc());
            r.setPuaTtc(a.getPuaTtc());
            r.setSuiviEnStock((a.getSuiviEnStock() != null) ? a.getSuiviEnStock() : false);
            r.setModeConso(a.getModeConso());
            r.setPhoto1(a.getPhoto1());
            r.setPhoto1Extension(a.getPhoto1Extension());
            r.setPhoto2(a.getPhoto2());
            r.setPhoto2Extension(a.getPhoto2Extension());
            r.setPhoto3(a.getPhoto3());
            r.setPhoto3Extension(a.getPhoto3Extension());
            r.setPua(a.getPua());
            r.setPuv(a.getPuv());
            r.setTypeService(a.getTypeService());
            r.setDateSave(a.getDateSave());
            r.setDateUpdate(a.getDateUpdate());
            r.setPuvMin((a.getPrixMin() != null) ? a.getPrixMin() : 0);
            r.setNaturePrixMin(a.getNaturePrixMin());
            r.setRefArt(a.getRefArt());
            r.setControleFournisseur(a.getControleFournisseur());
        }
        return r;
    }

    public static Articles buildBeanArticleForForm(YvsBaseArticles a) {
        Articles r = buildBeanArticleWithoutList(a);
        if (a != null) {
            r.setConditionnements(a.getConditionnements());
        }
        return r;
    }

    public static Articles buildBeanArticles(YvsBaseArticles a) {
        Articles r = buildSimpleBeanArticles(a);
        if (a != null) {
            r.setLibelle(a.getLibelle());
            r.setTemplate((a.getTemplate() != null) ? buildSimpleBeanTemplateArticles(a.getTemplate()) : new TemplateArticles());
            r.setGroupe((a.getGroupe() != null) ? buildSimpleBeanGroupeArticle(a.getGroupe()) : new GroupeArticle());
            r.setFamille((a.getFamille() != null) ? buildSimpleBeanFamilleArticle(a.getFamille()) : new FamilleArticle());
            r.setClasse1(a.getClasse1() != null ? buildSimpleBeanClasseStat(a.getClasse1()) : new ClassesStat());
            r.setClasse2(a.getClasse2() != null ? buildSimpleBeanClasseStat(a.getClasse2()) : new ClassesStat());
            r.setArticlesEquivalents(a.getArticlesEquivalents());
            r.setFournisseurs(a.getFournisseurs());
            r.setArticlesSubstitutions(a.getArticlesSubstitutions());
            r.setListArtDepots(a.getYvsBaseArticleDepotList());
            r.setListArticleCatComptable(a.getComptes());
            r.setPlans_tarifaires(a.getPlans_tarifaires());
            r.setGammes(a.getGammes());
            r.setNomenclatures(a.getNomenclatures());
            r.setDescriptions(a.getDescriptions());
            r.setAnalytiques(a.getAnalytiques());
            r.setTauxEcartPr(a.getTauxEcartPr());
            r.setRequiereLot(a.getRequiereLot());
            if (a.getUnite() != null ? a.getUnite().getId() > 0 : false) {
                r.setUniteVenteByDefault(new Conditionnement(a.getUnite().getId(), buildBeanUniteMesure(a.getUnite().getUnite())));
            }
            r.setPacks(new ArrayList<YvsBaseArticlePack>());
            for (YvsBaseConditionnement c : a.getConditionnements()) {
                r.getPacks().addAll(c.getPacks());
            }
            r.setAvis(a.getAvis());
            r.setTarifs(a.getTarifs());
        }
        return r;
    }

    public static YvsBaseArticles buildEntityArticle(Articles a) {
        YvsBaseArticles art = new YvsBaseArticles(a.getId());
        art.setActif(a.isActif());
        art.setDescription(a.getDescription());
        art.setCategorie(a.getCategorie());
        art.setChangePrix(a.isChangePrix());
        art.setPuvTtc(a.isPuvTtc());
        art.setPuaTtc(a.isPuaTtc());
        art.setClassStat(a.getClasseStat());
        art.setCoefficient(a.getCoefficient());
        art.setDateSave(a.getDateSave());
        art.setDateUpdate(new Date());
        art.setTypeService(a.getTypeService());
        art.setTags(a.getTags());
        art.setControleFournisseur(a.isControleFournisseur());
        art.setRequiereLot(a.isRequiereLot());

        if (a.getFamille().getId() > 0) {
            art.setFamille(new YvsBaseFamilleArticle(a.getFamille().getId(), a.getFamille().getReference(), a.getFamille().getDesignation()));
        }
        if ((a.getGroupe() != null) ? a.getGroupe().getId() != 0 : false) {
            art.setGroupe(new YvsBaseGroupesArticle(a.getGroupe().getId()));
        }
        if ((a.getFabricant() != null) ? a.getFabricant().getId() != 0 : false) {
            art.setFabriquant(new YvsBaseTiers(a.getFabricant().getId()));
        }
        if ((a.getTemplate() != null) ? a.getTemplate().getId() != 0 : false) {
            art.setTemplate(new YvsBaseArticlesTemplate(a.getTemplate().getId()));
        }
        if ((a.getUnite() != null) ? a.getUnite().getId() != 0 : false) {
            art.setUniteDeMasse(new YvsBaseUniteMesure(a.getUnite().getId(), a.getUnite().getReference(), a.getUnite().getLibelle()));
        }
        if ((a.getUniteVolume() != null) ? a.getUniteVolume().getId() != 0 : false) {
            art.setUniteDeVolume(new YvsBaseUniteMesure(a.getUniteVolume().getId(), a.getUniteVolume().getReference(), a.getUniteVolume().getLibelle()));
        }
        if ((a.getUniteStockage() != null) ? a.getUniteStockage().getId() != 0 : false) {
            art.setUniteStockage(new YvsBaseUniteMesure(a.getUniteStockage().getId(), a.getUniteStockage().getReference(), a.getUniteStockage().getLibelle()));
        }
        if ((a.getUniteVente() != null) ? a.getUniteVente().getId() != 0 : false) {
            art.setUniteVente(new YvsBaseUniteMesure(a.getUniteVente().getId(), a.getUniteVente().getReference(), a.getUniteVente().getLibelle()));
        }
        if ((a.getClasse1() != null) ? a.getClasse1().getId() != 0 : false) {
            art.setClasse1(new YvsBaseClassesStat(a.getClasse1().getId(), a.getClasse1().getCodeRef(), a.getClasse1().getDesignation()));
        }
        if ((a.getClasse2() != null) ? a.getClasse2().getId() != 0 : false) {
            art.setClasse2(new YvsBaseClassesStat(a.getClasse2().getId(), a.getClasse2().getCodeRef(), a.getClasse2().getDesignation()));
        }
        art.setDefNorme(a.isDefNorme());
        art.setDesignation(a.getDesignation());
        art.setMasseNet(a.getMasseNet());
        art.setModeConso(a.getModeConso());
        art.setMethodeVal(a.getMethodeVal());
        art.setFichier(a.getFichier());
        art.setDureeGarantie(a.getDureeGarantie());
        art.setDelaiLivraison(a.getDelaiLivraison());
        art.setNorme(a.isNormeFixe());
        art.setPhoto1(a.getPhoto1());
        art.setPhoto1Extension(a.getPhoto1Extension());
        art.setPhoto2(a.getPhoto2());
        art.setPhoto2Extension(a.getPhoto2Extension());
        art.setPhoto3(a.getPhoto3());
        art.setPhoto3Extension(a.getPhoto3Extension());
        art.setPua(a.getPua());
        art.setPuv(a.getPuv());
        art.setPrixMin(a.getPuvMin());
        art.setNaturePrixMin(a.getNaturePrixMin());
        art.setSuiviEnStock(a.isSuiviEnStock());
        art.setRemise(a.getRemise());
        art.setRefArt(a.getRefArt());
        art.setConditionnements(a.getConditionnements());
        art.setSynchroniser(false);
        art.setTauxEcartPr(a.getTauxEcartPr());
        return art;
    }

    public static List<Articles> buildBeanListArticles(List<YvsBaseArticles> list) {
        List<Articles> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseArticles a : list) {
                r.add(buildBeanArticles(a));
            }
        }
        return r;
    }

    public static Articles buildArticleByTemplateArticles(TemplateArticles a, boolean force) {
        Articles r = new Articles();
        if (a != null) {
            r.setId(-(a.getId()));
            r.setLibelle(a.getLibelle());
            r.setCategorie(a.getCategorie());
            r.setDescription(a.getDescription());
            r.setDesignation(a.getDesignation());
            r.setDureeGarantie(a.getDureeGarantie());
            r.setDelaiLivraison(a.getDureeVie());
            r.setFamille(a.getFamille());
            r.setFichier(a.getFichier());
            r.setGroupe(a.getGroupe());
            r.setClasse1(a.getClasse());
            r.setMasseNet(a.getMasseNet());
            r.setMethodeVal(a.getMethodeVal());
            r.setModeConso(a.getModeConso());
            r.setPhoto1(a.getPhoto1());
            r.setPhoto2(a.getPhoto2());
            r.setPhoto3(a.getPhoto3());
            r.setPua(a.getPua());
            r.setPuv(a.getPuv());
            r.setPuvMin(a.getPuvMin());
            r.setRefArt(a.getRefArt());
            r.setRemise(a.getRemise());
        }
        return r;
    }

    public static TemplateArticles buildTemplateArticlesByArticle(Articles a) {
        TemplateArticles r = new TemplateArticles();
        if (a != null) {
            r.setId(-(a.getId()));
            r.setCategorie(a.getCategorie());
            r.setLibelle(a.getLibelle());
            r.setDescription(a.getDescription());
            r.setDesignation(a.getDesignation());
            r.setDureeGarantie(a.getDureeGarantie());
            r.setDureeVie(a.getDelaiLivraison());
            r.setFamille(a.getFamille());
            r.setFichier(a.getFichier());
            r.setGroupe(a.getGroupe());
            r.setClasse(a.getClasse1());
            r.setMasseNet(a.getMasseNet());
            r.setMethodeVal(a.getMethodeVal());
            r.setModeConso(a.getModeConso());
            r.setPhoto1(a.getPhoto1());
            r.setPhoto2(a.getPhoto2());
            r.setPhoto3(a.getPhoto3());
            r.setPua(a.getPua());
            r.setPuv(a.getPuv());
            r.setPuvMin(a.getPuvMin());
            r.setRefArt(a.getRefArt());
            r.setRemise(a.getRemise());
        }
        return r;
    }

    public static TemplateArticles buildSimpleBeanTemplateArticles(YvsBaseArticlesTemplate a) {
        TemplateArticles r = new TemplateArticles();
        if (a != null) {
            r.setId(a.getId());
            r.setChangePrix(a.getChangePrix());
            r.setDescription(a.getDescription());
            r.setLibelle(a.getLibelle());
            r.setDefNorme(a.getDefNorme());
            r.setDesignation(a.getDesignation());
            r.setModeConso(a.getModeConso());
            r.setNormeFixe(a.getNorme());
            r.setSuiviEnStock(a.getSuiviEnStock());
            r.setVisibleEnSynthese(a.getVisibleEnSynthese());
            r.setCoefficient(a.getCoefficient());
            r.setService(a.getService());
            r.setMethodeVal(a.getMethodeVal());
            r.setActif(a.getActif());
            r.setCategorie(a.getCategorie());
            r.setDureeVie(a.getDureeVie());
            r.setDureeGarantie(a.getDureeGarantie());
            r.setFichier(a.getFichier());
            r.setRefArt(a.getRefArt());
            r.setDateSave(a.getDateSave());
            r.setDateUpdate(a.getDateUpdate());
        }
        return r;

    }

    public static TemplateArticles buildBeanTemplateArticles(YvsBaseArticlesTemplate a) {
        TemplateArticles r = buildSimpleBeanTemplateArticles(a);
        if (a != null) {
            r.setGroupe((a.getGroupe() != null) ? buildBeanGroupeArticle(a.getGroupe()) : new GroupeArticle());
            r.setClasse((a.getClasse() != null) ? buildBeanClasseStat(a.getClasse()) : new ClassesStat());
            r.setFamille((a.getFamille() != null) ? buildBeanFamilleArticle(a.getFamille()) : new FamilleArticle());
            r.setTarifaires(a.getPlans_tarifaires());
            r.setComptes(a.getComptes());
            r.setArticles(a.getArticles());
        }
        return r;
    }

    public static YvsBaseArticlesTemplate buildTemplateArticles(TemplateArticles a, YvsUsersAgence ua, YvsSocietes s) {
        YvsBaseArticlesTemplate r = new YvsBaseArticlesTemplate();
        if (a != null) {
            r.setId(a.getId());
            r.setChangePrix(a.isChangePrix());
            r.setLibelle(a.getLibelle());
            r.setDescription(a.getDescription());
            r.setDefNorme(a.isDefNorme());
            r.setDesignation(a.getDesignation());
            r.setModeConso(a.getModeConso());
            r.setNorme(a.isNormeFixe());
            r.setSuiviEnStock(a.isSuiviEnStock());
            r.setVisibleEnSynthese(a.isVisibleEnSynthese());
            r.setCoefficient(a.getCoefficient());
            r.setService(a.isService());
            r.setMethodeVal(a.getMethodeVal());
            r.setActif(a.isActif());
            r.setCategorie(a.getCategorie());
            r.setDureeVie(a.getDureeVie());
            r.setDureeGarantie(a.getDureeGarantie());
            r.setFichier(a.getFichier());
            r.setRefArt(a.getRefArt());
            r.setDateSave(a.getDateSave());
            r.setDateUpdate(new Date());
            if (a.getGroupe() != null ? a.getGroupe().getId() > 0 : false) {
                r.setGroupe(new YvsBaseGroupesArticle(a.getGroupe().getId()));
            }
            if (a.getClasse() != null ? a.getClasse().getId() > 0 : false) {
                r.setClasse(new YvsBaseClassesStat(a.getClasse().getId(), a.getClasse().getCodeRef(), a.getClasse().getDesignation()));
            }
            if (a.getFamille() != null ? a.getFamille().getId() > 0 : false) {
                r.setFamille(new YvsBaseFamilleArticle(a.getFamille().getId(), a.getFamille().getReference(), a.getFamille().getDesignation()));
            }

            r.setArticles(a.getArticles());
            r.setPlans_tarifaires(a.getTarifaires());
            r.setComptes(a.getComptes());

            r.setAuthor(ua);
            r.setSociete(s);
            r.setNew_(true);
        }
        return r;
    }

    public static List<TemplateArticles> buildBeanListTemplateArticles(List<YvsBaseArticlesTemplate> list) {
        List<TemplateArticles> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseArticlesTemplate a : list) {
                r.add(buildBeanTemplateArticles(a));
            }
        }
        return r;
    }

    public static ArticleDescription buildBeanArticleDescription(YvsBaseArticleDescription y) {
        ArticleDescription r = new ArticleDescription();
        if (y != null) {
            r.setArticle(new Articles(y.getArticle().getId()));
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
            r.setDescription(y.getDescription());
            r.setTitre(y.getTitre());
        }
        return r;
    }

    public static YvsBaseArticleDescription buildArticleDescription(ArticleDescription y, YvsUsersAgence ua) {
        YvsBaseArticleDescription r = new YvsBaseArticleDescription();
        if (y != null) {
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                r.setArticle(new YvsBaseArticles(y.getArticle().getId()));
            }
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setDescription(y.getDescription());
            r.setTitre(y.getTitre());
            if (ua != null ? ua.getId() > 0 : false) {
                r.setAuthor(ua);
            }
            r.setNew_(true);
        }
        return r;
    }

    public static ArticleAnalytique buildBeanArticleAnalytique(YvsBaseArticleAnalytique y) {
        ArticleAnalytique r = new ArticleAnalytique();
        if (y != null) {
            r.setId(y.getId());
            r.setTaux(y.getCoefficient());
            if (y.getArticle() != null) {
                r.setArticle(new Articles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()));
            }
            r.setCentre(UtilCompta.buildBeanCentreAnalytique(y.getCentre()));
            r.setDateSave(y.getDateSave());
        }
        return r;
    }

    public static YvsBaseArticleAnalytique buildArticleAnalytique(ArticleAnalytique y, YvsUsersAgence ua) {
        YvsBaseArticleAnalytique r = new YvsBaseArticleAnalytique();
        if (y != null) {
            r.setId(y.getId());
            r.setCoefficient(y.getTaux());
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                r.setArticle(new YvsBaseArticles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()));
            }
            if (y.getCentre() != null ? y.getCentre().getId() > 0 : false) {
                r.setCentre(new YvsComptaCentreAnalytique(y.getCentre().getId(), y.getCentre().getCodeRef(), y.getCentre().getIntitule()));
            }
            r.setAuthor(ua);
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setNew_(true);
        }
        return r;
    }

//    public static List<ArticleSubstitution> buildBeanListArticleEquivalent(List<YvsBaseArticleEquivalent> list, boolean e) {
//        List<ArticleSubstitution> r = new ArrayList<>();
//        ArticleSubstitution art;
//        if (list != null) {
//            if (e) {
//                for (YvsBaseArticleEquivalent a : list) {
//                    art = new ArticleSubstitution(a.getId(), buildSimpleBeanArticles(a.getArticle()), buildSimpleBeanArticles(a.getArticleEquivalent()));
//                    r.add(art);
//                }
//            } else {
//                for (YvsBaseArticleEquivalent a : list) {
//                    art = new ArticleSubstitution(a.getId(), buildSimpleBeanArticles(a.getArticle()), buildSimpleBeanArticles(a.getArticleEquivalent()));
//                    r.add(art);
//                }
//            }
//        }
//        return r;
//    }
//
//    public static List<ArticleSubstitution> buildBeanListArticleSubstitution(List<YvsBaseArticleSubstitution> list, boolean e) {
//        List<ArticleSubstitution> r = new ArrayList<>();
//        ArticleSubstitution art;
//        if (list != null) {
//            if (e) {
//                for (YvsBaseArticleSubstitution a : list) {
//                    art = new ArticleSubstitution(a.getId(), buildSimpleBeanArticles(a.getArticle()), buildSimpleBeanArticles(a.getArticleSubstitution()));
////                    art = new Articles(a.getArticleSubstitution().getId(), a.getArticleSubstitution().getDesignation());
////                    art.setRefArt(a.getArticleSubstitution().getRefArt());
////                    art.setIdArtSubstitution(a.getId());
//                    r.add(art);
//
////                    r.add(buildBeanArticles(a.getArticle()));
//                }
//            } else {
//                for (YvsBaseArticleSubstitution a : list) {
//                    art = new ArticleSubstitution(a.getId(), buildSimpleBeanArticles(a.getArticle()), buildSimpleBeanArticles(a.getArticleSubstitution()));
////                    art.setRefArt(a.getArticleSubstitution().getRefArt());
////                    art.setIdArtSubstitution(a.getId());
//                    r.add(art);
//                }
//            }
//        }
//        return r;
//    }
    public static Tiers buildBeanTiers(YvsBaseFournisseur t) {
        Tiers r = new Tiers();
        if (t != null) {
            r.setId(t.getId());
            r.setCodeTiers(t.getTiers().getCodeTiers());
            r.setNom(t.getTiers().getNom());
            r.setAdresse(t.getTiers().getAdresse());
            r.setTelephone(t.getTiers().getTel());
            r.setClient(t.getTiers().getClient());
            r.setFournisseur(t.getTiers().getFournisseur());
            r.setRepresentant(t.getTiers().getRepresentant());
            r.setSociete(t.getTiers().getStSociete());
        }
        return r;
    }

    public static Tiers buildBeanTiers(YvsBaseTiers t) {
        Tiers r = new Tiers();
        if (t != null) {
            r.setId(t.getId());
            r.setCodeTiers(t.getCodeTiers());
            r.setNom(t.getNom());
            r.setAdresse(t.getAdresse());
            r.setTelephone(t.getTel());
            r.setClient(t.getClient());
            r.setFournisseur(t.getFournisseur());
            r.setRepresentant(t.getRepresentant());
            r.setSociete(t.getStSociete());
            r.setActif(t.getActif());
            r.setBp(t.getBp());
            r.setCodeRepresentant(t.getCodePostal());
            r.setEmail(t.getEmail());
            r.setPays(UtilSte.buildBeanDictionnaire(t.getPays()));
            r.setVille(UtilSte.buildBeanDictionnaire(t.getVille()));
        }
        return r;
    }

    public static Fournisseur buildBeanTiersFournisseurs(YvsBaseTiers t) {
        Fournisseur r = new Fournisseur();
        if (t != null) {
            r.setId(t.getId());
            r.setCodeFsseur(t.getCodeTiers());
        }
        return r;
    }

    public static List<Fournisseur> buildBeanListTiersFournisseurs(List<YvsBaseFournisseur> list) {
        List<Fournisseur> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseFournisseur a : list) {
                r.add(buildBeanTiersFournisseurs(a.getTiers()));
            }
        }
        return r;
    }

    public static List<Tiers> buildBeanListTiers(List<YvsBaseTiers> list) {
        List<Tiers> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseTiers a : list) {
                r.add(buildBeanTiers(a));
            }
        }
        return r;
    }

    public static UniteMesure buildSimpleBeanUniteMesure(YvsBaseUniteMesure u) {
        UniteMesure r = new UniteMesure();
        if (u != null) {
            r.setId(u.getId());
            r.setReference(u.getReference());
            r.setLibelle(u.getLibelle());
            r.setType(u.getType());
            r.setDefaut(u.getDefaut());
            r.setDateSave(u.getDateSave());
        }
        return r;
    }

    public static UniteMesure buildBeanUniteMesure(YvsBaseUniteMesure u) {
        UniteMesure r = buildSimpleBeanUniteMesure(u);
        if (u != null && r != null ? r.getId() > 0 : false) {
            r.setEquivalences(u.getEquivalences());
        }
        return r;
    }

    public static YvsBaseUniteMesure buildUniteMasse(UniteMesure u, YvsUsersAgence ua, YvsSocietes s) {
        YvsBaseUniteMesure r = new YvsBaseUniteMesure();
        if (u != null) {
            r.setId(u.getId());
            r.setReference(u.getReference());
            r.setLibelle(u.getLibelle());
            r.setType(u.getType());
            r.setDefaut(u.isDefaut());
            r.setSociete(s);
            r.setAuthor(ua);
            r.setDateSave(u.getDateSave());
            r.setDateUpdate(new Date());
            r.setNew_(true);
        }
        return r;
    }

    public static List<UniteMesure> buildBeanListUniteMasse(List<YvsBaseUniteMesure> list) {
        List<UniteMesure> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseUniteMesure a : list) {
                r.add(buildBeanUniteMesure(a));
            }
        }
        return r;
    }

    public static TableConversion buildBeanTableConversion(YvsBaseTableConversion u) {
        TableConversion r = new TableConversion();
        if (u != null) {
            r.setId(u.getId());
            r.setTauxChange(u.getTauxChange());
            r.setUnite(buildBeanUniteMesure(u.getUnite()));
            r.setUniteEquivalent(buildBeanUniteMesure(u.getUniteEquivalent()));
        }
        return r;
    }

    public static YvsBaseTableConversion buildTableConversion(TableConversion u, YvsUsersAgence ua) {
        YvsBaseTableConversion r = new YvsBaseTableConversion();
        if (u != null) {
            r.setId(u.getId());
            r.setTauxChange(u.getTauxChange());
            if (u.getUnite() != null ? u.getUnite().getId() > 0 : false) {
                r.setUnite(new YvsBaseUniteMesure(u.getUnite().getId(), u.getUnite().getReference(), u.getUnite().getLibelle(), u.getUnite().getType()));
            }
            if (u.getUniteEquivalent() != null ? u.getUniteEquivalent().getId() > 0 : false) {
                r.setUniteEquivalent(new YvsBaseUniteMesure(u.getUniteEquivalent().getId(), u.getUniteEquivalent().getReference(), u.getUniteEquivalent().getLibelle(), u.getUniteEquivalent().getType()));
            }
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static List<TableConversion> buildBeanListTableConversion(List<YvsBaseTableConversion> list) {
        List<TableConversion> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseTableConversion a : list) {
                r.add(buildBeanTableConversion(a));
            }
        }
        return r;
    }

    public static YvsProdNomenclature buildBeanNomenclature(Nomenclature nm, YvsUsersAgence u) {
        YvsProdNomenclature n = new YvsProdNomenclature(nm.getId());
        if (nm != null) {
            n.setActif(nm.isActif());
            if (nm.getCompose() != null ? nm.getCompose().getId() > 0 : false) {
                n.setArticle(new YvsBaseArticles(nm.getCompose().getId(), nm.getCompose().getRefArt(), nm.getCompose().getDesignation()));
            }
            n.setDebutValidite(nm.getDebut());
            n.setFinValidite(nm.getFin());
            n.setNiveau(nm.getNiveau());
            n.setQuantiteLieAuxComposants(nm.isQteLieAuxComposant());
            n.setQuantite(nm.getQuantite());
            n.setReference(nm.getReference());
            n.setAlwayValide(nm.isAlwayValid());
            n.setTypeNomenclature(nm.getTypeNomenclature());
            if (nm.getUnite() != null ? nm.getUnite().getId() > 0 : false) {
                n.setUniteMesure(buildConditionnement(nm.getUnite(), u));
            }
            n.setForConditionnement(nm.isForConditionnement());
            n.setPrincipal(nm.isPrincipal());
            n.setAuthor(u);
            n.setDateSave(nm.getDateSave());
            n.setDateUpdate(new Date());
            n.setMasquer(nm.isMasquer());
        }
        return n;
    }

    public static Nomenclature buildSimpleBeanNomenclature(YvsProdNomenclature nm) {
        Nomenclature n = new Nomenclature();
        if (nm != null) {
            n.setId((nm.getId() == null) ? 0 : nm.getId());
            n.setActif(nm.getActif());
            n.setDebut(nm.getDebutValidite());
            n.setFin(nm.getFinValidite());
            n.setNiveau(nm.getNiveau());
            n.setQuantite(nm.getQuantite());
            n.setReference(nm.getReference());
            n.setQteLieAuxComposant((nm.getQuantiteLieAuxComposants() != null) ? nm.getQuantiteLieAuxComposants() : false);
            n.setAlwayValid((nm.getAlwayValide() != null) ? nm.getAlwayValide() : false);
            n.setTypeNomenclature(nm.getTypeNomenclature());
            n.setPrincipal(nm.getPrincipal());
            n.setComposants(nm.getComposants());
            n.setDateSave(nm.getDateSave());
            n.setForConditionnement(nm.getForConditionnement());
            n.setVal_total(nm.getValeur_total());
            n.setMasquer(nm.getMasquer());
        }
        return n;
    }

    public static Nomenclature buildSimpleBeanNomenclatureAndUnite(YvsProdNomenclature nm) {
        Nomenclature n = buildSimpleBeanNomenclature(nm);
        if (nm != null) {
            n.setUnite(buildBeanUnite(nm.getUniteMesure()));
        }
        return n;
    }

    public static Nomenclature buildBeanNomenclature(YvsProdNomenclature nm) {
        Nomenclature n = buildSimpleBeanNomenclature(nm);
        if (nm != null) {
            n.setCompose(buildSimpleBeanArticles(nm.getArticle()));
            n.setUnite(buildBeanUnite(nm.getUniteMesure()));
            n.setSites(nm.getSites());
        }
        return n;
    }

    public static List<ComposantNomenclature> buildComposantNomenclature(List<YvsProdComposantNomenclature> lc) {
        List<ComposantNomenclature> lr = new ArrayList<>();
        for (YvsProdComposantNomenclature c : lc) {
            lr.add(buildBeanComposantNomenclature(c));
        }
        return lr;
    }

    public static ComposantNomenclature buildBeanComposantNomenclature(YvsProdComposantNomenclature cn) {
        ComposantNomenclature c = new ComposantNomenclature();
        if (cn != null) {
            c.setId(cn.getId());
            c.setArticle(buildSimpleBeanArticles(cn.getArticle()));
            c.setModeArrondi(cn.getModeArrondi());
            c.setQuantite(cn.getQuantite());
            c.setTypeComposant(cn.getType());
            c.setCoefficient(cn.getCoefficient());
            c.setNomenclature(buildBeanNomenclature(cn.getNomenclature()));
            c.setUnite(buildBeanConditionnement(cn.getUnite()));
            c.setActif(cn.getActif());
            c.setStockable(cn.getStockable());
            c.setDateSave(cn.getDateSave());
            c.setOrdre(cn.getOrdre());
            c.setInsideCout(cn.getInsideCout());
            c.setAlternatif(cn.getAlternatif());
            c.setFreeUse(cn.getFreeUse());
        }
        return c;
    }

    public static List<Nomenclature> buildBeanListNomenclature(List<YvsProdNomenclature> l) {
        List<Nomenclature> result = new ArrayList<>();
        for (YvsProdNomenclature n : l) {
            result.add(buildBeanNomenclature(n));
        }
        return result;
    }

    public static YvsProdComposantNomenclature buildBeanNomenclature(ComposantNomenclature c, YvsUsersAgence u) {
        YvsProdComposantNomenclature cn = new YvsProdComposantNomenclature();
        cn.setId(c.getId());
        if (c.getArticle() != null ? c.getArticle().getId() > 0 : false) {
            cn.setArticle(buildEntityArticle(c.getArticle()));
        }
        if (c.getUnite() != null ? c.getUnite().getId() > 0 : false) {
            cn.setUnite(buildConditionnement(c.getUnite(), u));
        }
        if (c.getNomenclature() != null ? c.getNomenclature().getId() > 0 : false) {
            cn.setNomenclature(new YvsProdNomenclature(c.getNomenclature().getId()));
        }
        cn.setQuantite(c.getQuantite());
        cn.setType(c.getTypeComposant());
        cn.setModeArrondi(c.getModeArrondi());
        cn.setCoefficient(c.getCoefficient());
        cn.setActif(c.isActif());
        cn.setStockable(c.isStockable());
        cn.setAuthor(u);
        cn.setDateSave(c.getDateSave());
        cn.setOrdre(c.getOrdre());
        cn.setInsideCout(c.isInsideCout());
        cn.setAlternatif(c.isAlternatif());
        cn.setFreeUse(c.isFreeUse());
        cn.setNew_(true);
        return cn;
    }

    public static YvsProdPeriodePlan buildPeriodePlan(PeriodePlanification p) {
        YvsProdPeriodePlan pe = new YvsProdPeriodePlan();
        pe.setDebutPeriode(p.getDateDebut());
        pe.setFinPeriode(p.getDateFin());
        pe.setIndicatif(p.getIndicatif());
        pe.setReference(p.getReference());
        if (p.getPlan().getId() != 0) {
            pe.setPlan(new YvsProdPlanification(p.getPlan().getId()));
        }
        return pe;
    }

    public static PeriodePlanification buildPeriodePlan(YvsProdPeriodePlan p) {
        PeriodePlanification pe = new PeriodePlanification();
        pe.setDateDebut(p.getDebutPeriode());
        pe.setDateFin(p.getFinPeriode());
        pe.setIndicatif(p.getIndicatif());
        pe.setReference(p.getReference());
        pe.setId(p.getId());
        if (p.getPlan() != null) {
            pe.setPlan(new Planification(p.getPlan().getId()));
        }
        return pe;
    }

    public static List<PeriodePlanification> buildBeanListPeriodePlanification(List<YvsProdPeriodePlan> l) {
        List<PeriodePlanification> result = new ArrayList<>();
        for (YvsProdPeriodePlan p : l) {
            result.add(buildPeriodePlan(p));
        }
        return result;
    }

    public static YvsProdPlanification buildPlanification(Planification p) {
        YvsProdPlanification pl = new YvsProdPlanification();
        pl.setAmplitude(p.getAmplitude());
        pl.setDateDebut(p.getDateDebut());
        pl.setDateFin(p.getDateFin());
        pl.setDatePlanification(p.getDateReference());
        pl.setHorizon(p.getHorizon());
        pl.setId(p.getId());
        pl.setPeriode(p.getPeriode());
        pl.setTypePlan(p.getType());
        pl.setReference(p.getReference());
        return pl;
    }

    public static Planification buildBeanPlanification(YvsProdPlanification y) {
        Planification p = new Planification();
        if (y != null) {
            p.setId(y.getId());
            p.setAmplitude(y.getAmplitude().shortValue());
            p.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            p.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            p.setHorizon(y.getHorizon());
            p.setPeriodicite((y.getPeriodicite() != null) ? y.getPeriodicite() : 0);
            p.setPeriode(y.getPeriode());
            p.setReference(y.getReference());
            p.setType(y.getTypePlan());
        }
        return p;
    }

    public static List<Planification> buildBeanListPlanification(List<YvsProdPlanification> l) {
        List<Planification> result = new ArrayList<>();
        for (YvsProdPlanification p : l) {
            result.add(buildBeanPlanification(p));
        }
        return result;
    }

    public static YvsProdDetailPic buildDataPic(ObjectData value, YvsProdPeriodePlan period, YvsBaseFamilleArticle fa) {
        YvsProdDetailPic pl = new YvsProdDetailPic();
        pl.setFamille(fa);
        pl.setPeriode(period);
        pl.setValeur(value.getValue());
        return pl;
    }
//
//    /*
//     DEBUT GESTION DES DPD
//
//     */

    public static DetailPlanPDP buildBeanDetailPlanPDP(YvsProdDetailPdp y) {
        DetailPlanPDP d = new DetailPlanPDP();
        if (y != null) {
            d.setId(y.getId());
            d.setValeur((y.getValeur() != null) ? y.getValeur() : 0);
            d.setTypeVal((y.getTypeVal() != null) ? y.getTypeVal() : "BB");
            //d.setPeriode((y.getPeriode() != null) ? buildBeanPeriodePlanification(y.getPeriode()) : new PeriodePlanification());
            d.setArticle((y.getArticle() != null) ? buildBeanArticles(y.getArticle()) : new Articles());
//            d.setFournisseur((y.getFournisseur() != null) ? buildBeanArticleFournisseur(y.getFournisseur()) : new ArticleFournisseur());
        }
        return d;
    }

    public static List<DetailPlanPDP> buildBeanListDetailPlanPDP(List<YvsProdDetailPdp> y) {
        List<DetailPlanPDP> l = new ArrayList<>();
        if (y != null) {
            for (YvsProdDetailPdp p : y) {
                l.add(buildBeanDetailPlanPDP(p));
            }
        }
        return l;
    }

    public static PeriodePlanificationPDP buildBeanPeriodePlanificationPDP(YvsProdPeriodePlan y) {
        PeriodePlanificationPDP p = new PeriodePlanificationPDP();
        if (y != null) {
            p.setDateDebut((y.getDebutPeriode() != null) ? y.getDebutPeriode() : new Date());
            p.setDateFin((y.getFinPeriode() != null) ? y.getFinPeriode() : new Date());
            p.setId(y.getId());
            p.setIndicatif(y.getIndicatif());
            p.setReference(y.getReference());
            p.setDetails((y.getYvsProdDetailPdpList() != null) ? buildBeanListDetailPlanPDP(y.getYvsProdDetailPdpList()) : new ArrayList<DetailPlanPDP>());
        }
        return p;
    }

    public static List<PeriodePlanificationPDP> buildBeanListPeriodePlanificationPDP(List<YvsProdPeriodePlan> y) {
        List<PeriodePlanificationPDP> l = new ArrayList<>();
        if (y != null) {
            for (YvsProdPeriodePlan p : y) {
                l.add(buildBeanPeriodePlanificationPDP(p));
            }
        }
        return l;
    }

    public static PlanificationPDP buildBeanPlanificationPDP(YvsProdPlanification y) {
        PlanificationPDP p = new PlanificationPDP();
        if (y != null) {
            p.setId(y.getId());
            p.setAmplitude(y.getAmplitude().shortValue());
            p.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            p.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            p.setHorizon(y.getHorizon());
            switch (p.getHorizon()) {
                case 1:
                    p.setNameHorizon("Année(s)");
                    break;
                case 2:
                    p.setNameHorizon("Mois");
                    break;
                case 3:
                    p.setNameHorizon("Semaine(s)");
                    break;
                default:
                    break;
            }
            p.setPeriodicite(y.getPeriodicite());
            p.setPeriode(y.getPeriode());
            switch (p.getPeriode()) {
                case "M":
                case "Mois":
                    p.setNamePeriode("Mois");
                    break;
                case "S":
                case "Semaine":
                    p.setNamePeriode("Semaine(s)");
                    break;
                case "J":
                case "Jour":
                    p.setNamePeriode("jour(s)");
                    break;
                default:
                    break;
            }
            p.setReference(y.getReference());
            p.setType(y.getTypePlan());
//            p.setPeriodes((y.getYvsProdPeriodePlanList() != null)
//                    ? buildBeanListPeriodePlanificationPDP(y.getYvsProdPeriodePlanList())
//                    : new ArrayList<PeriodePlanificationPDP>());
        }
        return p;
    }

    public static List<PlanificationPDP> buildBeanListPlanificationPDP(List<YvsProdPlanification> l) {
        List<PlanificationPDP> result = new ArrayList<>();
        for (YvsProdPlanification p : l) {
            result.add(buildBeanPlanificationPDP(p));
        }
        return result;
    }

//    /*
//     FIN GESTION DES PDP
//
//     */
//    /*
//     DEBUT GESTION DES DPC
//
//     */
    public static DetailPlanPDC buildBeanDetailPlanPDC(YvsProdDetailPdc y) {
        DetailPlanPDC d = new DetailPlanPDC();
        if (y != null) {
            d.setId_(y.getId());
//            d.setCapacite_h((y.getCapaciteH() != null) ? y.getCapaciteH() : 0);
//            d.setCapacite_q((y.getCapaciteQ() != null) ? y.getCapaciteQ() : 0);
//            d.setCharge_h((y.getChargeH() != null) ? y.getChargeH() : 0);
//            d.setCharge_q((y.getChargeQ() != null) ? y.getChargeQ() : 0);
//            d.setMods_h((y.getModsH() != null) ? y.getModsH() : 0);
//            d.setMods_q((y.getModsQ() != null) ? y.getModsQ() : 0);
//            if (d.getCapacite_q() > 0 && d.getCharge_q() > 0) {
//                d.setTaux_charge(d.getCharge_q() / d.getCapacite_q());
//            }
            d.setPhase((y.getPhase() != null) ? buildBeanPhaseGamme(y.getPhase(), null, null) : new OperationsGamme());
            if (y.getPhase() != null) {
                d.setGammeArticle((y.getPhase().getGammeArticle() != null) ? buildBeanGammeArticle(y.getPhase().getGammeArticle(), null, null)
                        : new GammeArticle());
                d.getGammeArticle().getOperations().clear();
            }
            d.setPdp((y.getPdp() != null) ? buildBeanDetailPlanPDP(y.getPdp()) : new DetailPlanPDP());
        }
        return d;
    }

    public static List<DetailPlanPDC> buildBeanListDetailPlanPDC(List<YvsProdDetailPdc> l) {
        List<DetailPlanPDC> result = new ArrayList<>();
        for (YvsProdDetailPdc p : l) {
            result.add(buildBeanDetailPlanPDC(p));
        }
        return result;
    }
//
//
//    /*
//     FIN GESTION DES PDC
//
//     */
//    /**
//     * DEBUT Gestion des OF
//     *
//     **
//     * @param co
//     * @return
//     */

    public static ComposantsOf buildBeanComposantOf(ComposantNomenclature co) {
        ComposantsOf of = new ComposantsOf();
        of.setComposant(co.getArticle());
        of.setModeArrondi(co.getModeArrondi());
        of.setQuantitePrevu(co.getQuantite());
        of.setQuantiteValide(co.getQuantite());
        of.setCoefficient(co.getCoefficient());
        of.setNiveau(co.getOrdre());
        of.setFreeUse(co.isFreeUse());
        return of;
    }

    public static YvsProdComposantOF buildComposantOf(YvsProdComposantNomenclature co) {
        YvsProdComposantOF of = new YvsProdComposantOF(-co.getId());
        of.setArticle(co.getArticle());
        of.setModeArrondi(co.getModeArrondi());
        of.setQuantitePrevu(co.getQuantite());
        of.setQuantiteValide(co.getQuantite());
        of.setUnite(co.getUnite());
        of.setCoefficient(co.getCoefficient());
        of.setOrdre(co.getOrdre());
        of.setFreeUse(co.getFreeUse());
        return of;
    }

    public static OperationsOF buildPhaseOf(OperationsGamme co) {
        OperationsOF of = new OperationsOF();
        of.setPhase(co);
        of.setTermine(false);
        return of;
    }

    public static OrdreFabrication buildBeanOf(YvsProdOrdreFabrication of) {
        OrdreFabrication o = new OrdreFabrication();
        if (of != null) {
            o.setId(of.getId());
            o.setArticles(buildBeanArticleForForm(of.getArticle()));
            if (o.getArticles().getNomenclatures() == null) {
                o.getArticles().setNomenclatures(new ArrayList());
            }
            if (o.getArticles().getGammes() == null) {
                o.getArticles().setGammes(new ArrayList());
            }
            if (!o.getArticles().getNomenclatures().contains(of.getNomenclature())) {
                o.getArticles().getNomenclatures().add(of.getNomenclature());
            }
            if (!o.getArticles().getGammes().contains(of.getGamme())) {
                o.getArticles().getGammes().add(of.getGamme());
            }
            o.setNumIdentification(of.getNumeroIdentification());
            o.setPriorite(of.getPriorite());
            o.setQuantitePrevu(of.getQuantite());
            o.setQuantite(of.getQuantite());
            o.setReference(of.getCodeRef());
            o.setStatusOrdre(of.getStatutOrdre());
            o.setNomenclature(buildSimpleBeanNomenclatureAndUnite(of.getNomenclature()));
            o.setGamme(buildSimpleBeanGammeArticle(of.getGamme()));
            o.setRetardDeLancement(o.getStatusOrdre().equals(Constantes.STATUT_DOC_ATTENTE) && of.getDateDebut().before(new Date()));
            o.setDateSave(of.getDateSave());
            o.setDateDebutLancement(of.getDateDebut());
            o.setHeureDeLancement(of.getHeureLancement());
            o.setDateFinFabrication(of.getFinValidite());
            o.setSuiviStockByOperation(of.getSuiviStockByOperation());
            o.setSuiviOperations(of.getSuiviOperation());
            o.setStatutDeclaration(of.getStatutDeclaration());
            o.setTypeOf(of.getTypeOf());
            o.setSite(buildBeanSiteProduction(of.getSiteProduction()));
            o.setCoutDeProduction(of.getCoutOf());
        }
        return o;
    }

    public static OrdreFabrication buildSimpleBeanOf(YvsProdOrdreFabrication of) {
        OrdreFabrication o = new OrdreFabrication();
        if (of != null) {
            o.setId(of.getId());
            o.setArticles(buildBeanArticles(of.getArticle()));
            o.getArticles().setNomenclatures(of.getArticle().getNomenclatures());
            o.getArticles().setGammes(of.getArticle().getGammes());
            o.setNumIdentification(of.getNumeroIdentification());
            o.setPriorite(of.getPriorite());
            o.setQuantitePrevu(of.getQuantite());
            o.setReference(of.getCodeRef());
            o.setStatusOrdre(of.getStatutOrdre());
            o.setDateSave(of.getDateSave());
            o.setDateDebutLancement(of.getDateDebut());
            o.setHeureDeLancement(of.getHeureLancement());
            o.setDateFinFabrication(of.getFinValidite());
            o.setSuiviStockByOperation(of.getSuiviStockByOperation());
            o.setSuiviOperations(of.getSuiviOperation());
            o.setStatutDeclaration(of.getStatutDeclaration());
            o.setTypeOf(of.getTypeOf());
            o.setSite(buildBeanSiteProduction(of.getSiteProduction()));
            o.setCoutDeProduction(of.getCoutOf());
            o.setSuspendu(of.getSuspendu());
        }
        return o;
    }

    public static YvsProdOrdreFabrication buildEntityOf(OrdreFabrication of) {
        YvsProdOrdreFabrication o = new YvsProdOrdreFabrication();
        o.setId(of.getId());
        if (of.getArticles() != null ? of.getArticles().getId() > 0 : false) {
            o.setArticle(buildEntityArticle(of.getArticles()));
        }
        o.setNumeroIdentification(of.getNumIdentification());
        o.setPriorite(of.getPriorite());
        o.setQuantite(of.getQuantitePrevu());
        o.setCodeRef(of.getReference());
        o.setStatutOrdre((of.getStatusOrdre() == null) ? Constantes.STATUT_DOC_ATTENTE + "" : of.getStatusOrdre());
        o.setStatutDeclaration((of.getStatutDeclaration() == null) ? Constantes.STATUT_DOC_ATTENTE + "" : of.getStatutDeclaration());
        if (of.getNomenclature() != null ? of.getNomenclature().getId() > 0 : false) {
            o.setNomenclature(buildBeanNomenclature(of.getNomenclature(), null));
        }
        if (of.getGamme() != null ? of.getGamme().getId() > 0 : false) {
            o.setGamme(buildGammeArticle(of.getGamme(), null));
        }
        o.setRebut(of.getRebut());
        o.setDateDebut(of.getDateDebutLancement());
        o.setDebutValidite(of.getDateDebutLancement());
        o.setFinValidite(of.getDateFinFabrication());
        o.setDateSave(of.getDateSave());
        o.setHeureLancement(of.getHeureDeLancement());
        o.setTypeOf(of.getTypeOf());
        o.setSuspendu(of.isSuspendu());
        o.setSuiviStockByOperation(of.isSuiviStockByOperation());
        o.setSuiviOperation(of.isSuiviOperations());
        o.setSiteProduction(buildSiteProduction(of.getSite(), null, null));
        return o;
    }

    public static YvsProdComposantOF buildEntityComposantOf(ComposantsOf co, YvsUsersAgence ua) {
        YvsProdComposantOF c = new YvsProdComposantOF();
        c.setId(co.getId());
        c.setCommentaire(co.getCommentaire());
        c.setArticle(buildEntityArticle(co.getComposant()));
        c.setModeArrondi(co.getModeArrondi());
        c.setQuantitePrevu(co.getQuantitePrevu());
        c.setQuantiteValide(co.getQuantiteValide());
        c.setCoutComposant(co.getCout());
        c.setEtatComposant(co.getEtat());
        c.setDateSave(co.getDateSave());
        c.setDateUpdate(new Date());
        c.setOrdre(co.getNiveau());
        c.setAuthor(ua);
        c.setType(co.getNature());
        c.setCoefficient(co.getCoefficient());
        c.setInsideCout(co.isInsideCout());
        c.setFreeUse(co.isFreeUse());
        if (co.getComposant() != null ? co.getComposant().getId() > 0 : false) {
            c.setArticle(buildEntityArticle(co.getComposant()));
        }
        if (co.getDepotConso() != null ? co.getDepotConso().getId() > 0 : false) {
            c.setDepotConso(buildBeanDepot(co.getDepotConso()));
        } else {
            c.setDepotConso(null);
        }
        if (co.getLotSortie() != null ? co.getLotSortie().getId() > 0 : false) {
            c.setLotSortie(UtilCom.buildLotReception(co.getLotSortie(), ua.getAgence(), ua));
        }
        if (co.getOrdre() != null ? co.getOrdre().getId() > 0 : false) {
            c.setOrdreFabrication(new YvsProdOrdreFabrication(co.getOrdre() != null ? co.getOrdre().getId() : 0));
        }
        if (co.getUnite() != null ? co.getUnite().getId() > 0 : false) {
            c.setUnite(buildConditionnement(co.getUnite(), ua));
        }
        return c;
    }

    public static ComposantsOf buildComposantOf(YvsProdComposantOF co) {
        ComposantsOf c = new ComposantsOf();
        c.setId(co.getId());
        c.setCommentaire(co.getCommentaire());
        c.setComposant(buildSimpleBeanArticles(co.getArticle()));
        c.setModeArrondi(co.getModeArrondi());
        c.setQuantitePrevu(co.getQuantitePrevu());
        c.setQuantiteValide(co.getQuantiteValide());
        c.setCout(co.getCoutComposant());
        c.setEtat(co.getEtatComposant());
        c.setDateSave(co.getDateSave());
        c.setDepotConso(UtilCom.buildSimpleBeanDepot(co.getDepotConso()));
        c.setLotSortie(UtilCom.buildBeanLotReception(co.getLotSortie()));
        c.setOrdre(new OrdreFabrication(co.getOrdreFabrication() != null ? co.getOrdreFabrication().getId() : 0));
        c.setUnite(buildBeanConditionnement(co.getUnite()));
        c.setComposantsUsed(co.getComposantsUsed());
        c.setNiveau(co.getOrdre());
        c.setInsideCout(co.getInsideCout());
        c.setFreeUse(co.getFreeUse());
        return c;
    }

    public static List<ComposantsOf> buildComposantOf(List<YvsProdComposantOF> lco) {
        List<ComposantsOf> result = new ArrayList<>();
        for (YvsProdComposantOF co : lco) {
            result.add(buildComposantOf(co));
        }
        return result;
    }

    public static List<YvsProdComposantOF> buildEntityComposantOf(List<ComposantsOf> lco) {
        List<YvsProdComposantOF> result = new ArrayList<>();
        for (ComposantsOf co : lco) {
            result.add(UtilProd.buildEntityComposantOf(co, null));
        }
        return result;
    }

    public static OperationsOF buildBeanPhaseOf(YvsProdOperationsOF op) {
        OperationsOF bean = new OperationsOF();
        if (op != null) {
            bean.setCommentaire(op.getCommentaire());
            bean.setId(op.getId());
            bean.setReference(op.getCodeRef());
            bean.setDateDebut(op.getDateDebut());
            bean.setDateFin(op.getDateFin());
            bean.setDateSave(op.getDateSave());
            bean.setHeureDebut(op.getHeureDebut());
            bean.setHeureFin(op.getHeureFin());
            bean.setNbMachine(op.getNbMachine());
            bean.setNbMainOeuvre(op.getNbMainOeuvre());
            bean.setNumero(op.getNumero());
            bean.setStatutOp(op.getStatutOp());
            bean.setTempsOperation(op.getTempsOperation());
            bean.setTempsReglage(op.getTempsReglage());
            bean.setMachine(buildBeanPosteCharge(op.getMachine()));
            bean.setMainOeuvre(buildBeanPosteCharge(op.getMainOeuvre()));
            bean.setOrdreFabrication(new OrdreFabrication(op.getOrdreFabrication() != null ? op.getOrdreFabrication().getId() : 0));
//            bean.setComposants(op.getComposants());
        }
        return bean;
    }

    public static YvsProdOperationsOF buildEntityPhaseOf(OperationsOF ph, YvsUsersAgence ua) {
        YvsProdOperationsOF p = new YvsProdOperationsOF();
        p.setCommentaire(ph.getCommentaire());
        p.setId(ph.getId());
        p.setAuthor(ua);
        p.setCodeRef(ph.getReference());
        p.setDateDebut(ph.getDateDebut());
        p.setDateFin(ph.getDateFin());
        p.setDateSave(ph.getDateSave());
        p.setDateUpdate(new Date());
        p.setHeureDebut(ph.getHeureDebut());
        p.setHeureFin(ph.getHeureFin());
        p.setNbMachine(ph.getNbMachine());
        p.setNbMainOeuvre(ph.getNbMainOeuvre());
        p.setNumero(ph.getNumero());
        p.setStatutOp(ph.getStatutOp());
        p.setTempsOperation(ph.getTempsOperation());
        p.setTempsReglage(ph.getTempsReglage());
        if (ph.getMachine() != null ? ph.getMachine().getId() > 0 : false) {
            p.setMachine(buildPosteCharge(ph.getMachine(), ua));
        }
        if (ph.getMainOeuvre() != null ? ph.getMainOeuvre().getId() > 0 : false) {
            p.setMainOeuvre(buildPosteCharge(ph.getMainOeuvre(), ua));
        }
        if (ph.getOrdreFabrication() != null ? ph.getOrdreFabrication().getId() > 0 : false) {
            p.setOrdreFabrication(new YvsProdOrdreFabrication(ph.getOrdreFabrication().getId()));
        }
        return p;
    }

    public static DeclarationProduction buildBeanDeclarationProduction(YvsProdDeclarationProduction y) {
        DeclarationProduction r = new DeclarationProduction();
        if (y != null) {
            r.setConditionnement(buildBeanConditionnement(y.getConditionnement()));
            r.setDateSave(y.getDateSave());
            r.setId(y.getId());
            r.setOrdre(new OrdreFabrication(y.getOrdre() != null ? y.getOrdre().getId() : 0));
            r.setQuantite(y.getQuantite());
            r.setStatut(y.getStatut());
            r.setCout(y.getCoutProduction());
        }
        return r;
    }

    public static YvsProdDeclarationProduction buildDeclarationProduction(DeclarationProduction y, YvsUsersAgence ua, YvsProdSessionOf session) {
        YvsProdDeclarationProduction r = new YvsProdDeclarationProduction();
        if (y != null && session != null) {
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setQuantite(y.getQuantite());
            r.setStatut(y.getStatut());
            r.setCoutProduction(y.getCout());
            r.setSessionOf(session);
            if (y.getOrdre() != null ? y.getOrdre().getId() > 0 : false) {
                r.setOrdre(new YvsProdOrdreFabrication(y.getOrdre().getId()));
            }
            if (y.getConditionnement() != null ? y.getConditionnement().getId() > 0 : false) {
                r.setConditionnement(buildConditionnement(y.getConditionnement(), ua));
            }
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }
//
//    /**
//     * FIN Gestion des OF**
//     */
//    /**
//     * FIN Gestion des OF
//     *
//     **
//     * @param y
//     * @return
//     */

    public static EquipeProduction buildBeanEquipeProduction(YvsProdEquipeProduction y) {
        EquipeProduction e = new EquipeProduction();
        if (y != null) {
            e.setId(y.getId());
            e.setNom(y.getNom());
            e.setReference(y.getReference());
            e.setPrincipal((y.getPrincipal() != null) ? y.getPrincipal() : false);
            e.setActif((y.getActif() != null) ? y.getActif() : false);
            e.setChefEquipe((y.getChefEquipe() != null) ? UtilGrh.buildBeanSimpleEmploye(y.getChefEquipe()) : new Employe());
            e.setDepot((y.getDepot() != null) ? UtilCom.buildBeanDepot(y.getDepot()) : new Depots());
            e.setSite((y.getSite() != null) ? buildBeanSiteProduction(y.getSite(), null, null) : new SiteProduction());
            e.setDateSave(y.getDateSave());
            e.setEmployeEquipeList(y.getEmployeEquipeList());
        }
        return e;
    }

    public static CrenauxHoraireEquipe buildBeanCrenauxEquipe(YvsProdCreneauEquipeProduction cr) {
        CrenauxHoraireEquipe ce = new CrenauxHoraireEquipe();
        if (cr != null) {
            ce.setId(cr.getId());
            ce.setEquipe(buildBeanEquipeProduction(cr.getEquipe()));
            ce.setTranche(UtilGrh.buildTrancheHoraire(cr.getTranche()));
            ce.setUsers(UtilUsers.buildSimpleBeanUsers(cr.getUsers()));
            ce.setSite(buildBeanSiteProduction(cr.getSite()));
            ce.setDateSave(cr.getDateSave());
            ce.setDateTravail(cr.getDateTravail());
            ce.setActif(cr.getActif());
            ce.setPermanent(cr.getPermanent());
        }
        return ce;
    }

    public static YvsProdCreneauEquipeProduction buildCrenauxEquipe(CrenauxHoraireEquipe cr, YvsUsersAgence ua) {
        YvsProdCreneauEquipeProduction ce = new YvsProdCreneauEquipeProduction();
        if (cr != null) {
            ce.setId(cr.getId());
            if (cr.getEquipe() != null ? cr.getEquipe().getId() > 0 : false) {
                ce.setEquipe(new YvsProdEquipeProduction(cr.getEquipe().getId(), cr.getEquipe().getNom()));
            }
            if (cr.getTranche() != null ? cr.getTranche().getId() > 0 : false) {
                ce.setTranche(new YvsGrhTrancheHoraire(cr.getTranche().getId(), cr.getTranche().getTitre()));
            }
            if (cr.getUsers() != null ? cr.getUsers().getId() > 0 : false) {
                ce.setUsers(new YvsUsers(cr.getUsers().getId(), cr.getUsers().getNomUsers()));
            }
            if (cr.getSite() != null ? cr.getSite().getId() > 0 : false) {
                ce.setSite(new YvsProdSiteProduction(cr.getSite().getId(), cr.getSite().getReference(), cr.getSite().getDesignation()));
            }
            ce.setActif(cr.isActif());
            ce.setPermanent(cr.isPermanent());
            ce.setDateTravail(cr.getDateTravail());
            ce.setDateSave(cr.getDateSave());
            ce.setDateUpdate(new Date());
            ce.setNew_(true);
            ce.setAuthor(ua);
        }
        return ce;
    }

    public static Creneau buildBeanListCreneau(YvsComCreneauDepot c) {
        Creneau re = new Creneau();
        if (c != null) {
            re.setActif(c.getActif());
//            re.setCritere(c.getTypeCreno().getCritere());
            re.setJour(buildBeanJoursOuvree(c.getJour()));
            re.setReference(c.getJour().getJour());
//            re.setType(buildTypeCrenaux(c.getTypeCreno()));
        }
        return re;
    }

    public static JoursOuvres buildJourOuvrees(YvsJoursOuvres jo) {
        JoursOuvres j = new JoursOuvres(jo.getId().intValue());
        j.setJour(jo.getJour());
        j.setActif(jo.getActif());
        j.setHeureDebutTravail(jo.getHeureDebutTravail());
        j.setHeureFinTravail(jo.getHeureFinTravail());
        return j;
    }

    public static TypeCrenaux buildTypeCrenaux(YvsComTypeCreneauHoraire jo) {
        TypeCrenaux j = new TypeCrenaux();
        j.setId(jo.getId());
        j.setHeureDebut(jo.getHeureDebut());
        j.setHeureFin(jo.getHeureFin());
        j.setReference(jo.getReference());
        j.setCritere(jo.getCritere());
        j.setActif(jo.getActif());
        return j;
    }

    public static List<TypeCrenaux> buildBeanListTypeCrenau(List<YvsComTypeCreneauHoraire> ljo) {
        List<TypeCrenaux> re = new ArrayList<>();
        for (YvsComTypeCreneauHoraire tc : ljo) {
            re.add(buildTypeCrenaux(tc));
        }
        return re;
    }

    public static List<EquipeProduction> buildBeanListEquipeProduction(List<YvsProdEquipeProduction> l) {
        List<EquipeProduction> r = new ArrayList<>();
        if (l != null) {
            for (YvsProdEquipeProduction y : l) {
                r.add(buildBeanEquipeProduction(y));
            }
        }
        return r;
    }

    public static EmployeEquipe buildBeanEmployeEquipe(YvsProdMembresEquipe y) {
        EmployeEquipe e = new EmployeEquipe();
        if (y != null) {
            e.setId(y.getId());
            e.setActif((y.getActif() != null) ? y.getActif() : true);
//            e.setEmploye((y.getEmploye() != null) ? UtilGrh.buildBeanSimpleEmploye(y.getEmploye()) : new Employe());
        }
        return e;
    }

    public static List<EmployeEquipe> buildBeanListEmployeEquipe(List<YvsProdMembresEquipe> l) {
        List<EmployeEquipe> r = new ArrayList<>();
        if (l != null) {
            for (YvsProdMembresEquipe y : l) {
                r.add(buildBeanEmployeEquipe(y));
            }
        }
        return r;
    }

    public static List<ArticlesCatComptable> buildArticleCatC(List<YvsBaseArticleCategorieComptable> l) {
        List<ArticlesCatComptable> result = new ArrayList<>();
        for (YvsBaseArticleCategorieComptable ac : l) {
            result.add(buildBeanArticleCatC(ac));
        }
        return result;
    }

    public static ArticlesCatComptable buildBeanArticleCatC(YvsBaseArticleCategorieComptable cat) {
        return UtilCom.buildBeanArticleCategorie(cat);
    }

    public static YvsBaseArticleCategorieComptable buildArticleCatC(ArticlesCatComptable cat, YvsUsersAgence ua) {
        return UtilCom.buildArticleCategorie(cat, ua);
    }

    public static ArticlesCatComptable buildArticleCatC_(YvsBaseArticleCategorieComptable cat) {
        ArticlesCatComptable re = new ArticlesCatComptable();
        re.setCategorie(buildCategorieC(cat.getCategorie()));
        re.setCompte(UtilCompta.buildBeanCompte(cat.getCompte()));
        re.setId(re.getId());
        re.setNew_(false);

        return re;
    }

    public static CategorieComptable buildCategorieC(YvsBaseCategorieComptable c) {
        CategorieComptable cc = new CategorieComptable();
        cc.setActif(c.getActif());
        cc.setCodeCategorie(c.getCode());
        cc.setCodeAppel(c.getCodeAppel());
        cc.setId(c.getId());
        cc.setNature(c.getNature());
        return cc;
    }

    public static List<ArticleTaxe> buildCatTaxe(List<YvsBaseArticleCategorieComptableTaxe> lt) {
        List<ArticleTaxe> lre = new ArrayList<>();
        for (YvsBaseArticleCategorieComptableTaxe at : lt) {
            lre.add(buildCatTaxe(at));
        }
        return lre;
    }

    public static ArticleTaxe buildCatTaxe(YvsBaseArticleCategorieComptableTaxe at) {
        ArticleTaxe a = new ArticleTaxe();
        a.setArticle(buildArticleCatC_(at.getArticleCategorie()));
        a.setId(at.getId());
        a.setSelectActif(at.getActif());
        a.setTaxe(buildCatTaxe(at.getTaxe()));
        a.setUpdate(false);
        return a;
    }

    public static Taxes buildCatTaxe(YvsBaseTaxes t) {
        Taxes ta = new Taxes();
        ta.setId(t.getId());
        ta.setCodeAppel(t.getCodeAppel());
        ta.setCodeTaxe(t.getCodeTaxe());
        ta.setTaux(t.getTaux());
        return ta;
    }

    public static EmplacementDepot buildBeanEmplacement(YvsBaseEmplacementDepot e) {
        EmplacementDepot em = new EmplacementDepot();
        em.setId(e.getId());
        em.setCode(e.getCode());
        em.setDesignation(e.getDesignation());
        em.setDepot(buildBeanDepot(e.getDepot()));
        return em;
    }

    public static Depots buildBeanDepot(YvsBaseDepots dep) {
        Depots d = new Depots();
        if (dep != null) {
            d.setId(dep.getId());
            d.setAbbreviation(dep.getAbbreviation());
            d.setCode(dep.getCode());
            d.setDesignation(dep.getDesignation());
        }
        return d;
    }

    public static YvsBaseDepots buildBeanDepot(Depots y) {
        YvsBaseDepots d = null;
        if ((y != null) ? y.getId() > 0 : false) {
            d = new YvsBaseDepots();
            d.setAbbreviation(y.getAbbreviation());
            d.setActif(y.isActif());
            d.setAdresse(y.getAdresse());
            d.setCode(y.getCode());
            d.setControlStock(y.isControlStock());
            d.setCrenau(y.isCrenau());
            d.setDesignation(y.getDesignation());
            d.setId(y.getId());
            d.setDescription(y.getDescription());
            d.setOpProduction(y.isOpProduction());
            d.setOpAchat(y.isOpAchat());
            d.setOpTransit(y.isOpTransit());
            d.setOpVente(y.isOpVente());
            d.setOpRetour(y.isOpRetour());
            d.setOpReserv(y.isOpReserv());
            d.setOpTechnique(y.isOpTechnique());
            d.setArticles(y.getArticles());
            d.setSupp(false);
            if ((y.getResponsable() != null) ? y.getResponsable().getId() > 0 : false) {
                d.setResponsable(new YvsGrhEmployes(y.getResponsable().getId(), y.getResponsable().getNom(), y.getResponsable().getPrenom()));
            }
        }
        return d;
    }

    public static List<Depots> buildBeanListDepot(List<YvsBaseDepots> ldep) {
        List<Depots> re = new ArrayList<>();
        for (YvsBaseDepots bd : ldep) {
            re.add(buildBeanDepot(bd));
        }
        return re;
    }

    public static TrancheVal buildGammeRemise(YvsTranches tr) {
        TrancheVal re = new TrancheVal();
        if (tr != null) {
            re.setId(tr.getId());
            re.setModelTranche(tr.getModelTranche());
            re.setReference(tr.getReferenceTranche());
            re.setListBorne(tr.getBornes());
            re.setActif(tr.getActif());
        }
        return re;
    }

    public static YvsTranches buildGammeRemise(TrancheVal tr) {
        YvsTranches re = new YvsTranches();
        if (tr != null) {
            re.setId(tr.getId());
            re.setModelTranche(tr.getModelTranche());
            re.setActif(tr.isActif());
            re.setReferenceTranche(tr.getReference());
        }
        return re;
    }

    public static BorneTranche buildBorneGamme(YvsBorneTranches bo) {
        BorneTranche borne_ = new BorneTranche();
        if (bo != null) {
            borne_.setBorne(bo.getBorne());
            borne_.setPrix(bo.getPrix());
            borne_.setRemise(bo.getRemise());
            borne_.setDateSave(bo.getDateSave());
            borne_.setTranche((bo.getTranche() != null) ? new TrancheVal(bo.getTranche().getId(), bo.getTranche().getReferenceTranche(), bo.getTranche().getModelTranche()) : new TrancheVal());
        }
        return borne_;
    }

    public static YvsBorneTranches buildBorne(BorneTranche b) {
        YvsBorneTranches bb = new YvsBorneTranches(b.getId());
        bb.setBorne(b.getBorne());
        bb.setPrix(b.getPrix());
        bb.setRemise(b.getRemise());
        bb.setDateSave(b.getDateSave());
        if (b.getTranche().getId() > 0) {
            bb.setTranche(new YvsTranches(b.getTranche().getId()));
        }
        return bb;
    }

    public static YvsBaseArticleDepot buildArticleDepot(ArticleDepot gr) {
        YvsBaseArticleDepot g = new YvsBaseArticleDepot();
        if (gr != null) {
            g.setModeAppro(gr.getModeAppro());
            if (gr.getArticle().getId() > 0) {
                g.setArticle(new YvsBaseArticles(gr.getArticle().getId()));
            }
            if (gr.getDepot().getId() > 0) {
                g.setDepot(new YvsBaseDepots(gr.getDepot().getId()));
            }
            g.setStockMax(gr.getStockMax());
            g.setStockMin(gr.getStockMin());
            g.setId(gr.getId());
        }
        return g;
    }

    public static ArticleDepot buildArticleDepot(YvsBaseArticleDepot gr) {
        ArticleDepot g = new ArticleDepot();
        if (gr != null) {
            if (gr.getArticle() != null) {
                g.setArticle(new Articles(gr.getArticle().getId()));
            }
            if (gr.getDepot().getId() > 0) {
                g.setDepot(new Depots(gr.getDepot().getId()));
            }
            g.setStockMax(gr.getStockMax());
            g.setStockMin(gr.getStockMin());
            g.setModeAppro(gr.getModeAppro());
            g.setModeReappro(gr.getModeReappro());
            g.setLotLivraison(gr.getLotLivraison());
            g.setIntervalApprov(gr.getIntervalApprov());
            g.setUniteInterval(gr.getUniteInterval());
            g.setStockSecurite(gr.getStockAlert() - gr.getStockMin());
            g.setId(gr.getId());
        }
        return g;
    }

    public static ArticleFournisseur buildArticleFournisseur(YvsBaseArticleFournisseur af) {
        ArticleFournisseur re = new ArticleFournisseur();
        if (af != null) {
            re.setArticle((af.getArticle() != null) ? new Articles(af.getArticle().getId()) : new Articles());
            re.setDateLivraison(af.getDateLivraison());
            re.setDelaiLivraison(af.getDelaiLivraison());
            re.setDureeGarantie(af.getDureeGarantie());
            re.setDureeVie(af.getDureeVie());
            re.setFournisseur((af.getFournisseur() != null) ? UtilCom.buildBeanFournisseur(af.getFournisseur()) : new Fournisseur());
            re.setId(af.getId());
            re.setPrincipal(af.getPrincipal());
            re.setPrix(af.getPuv());
            re.setRemise(af.getRemise());
            re.setUniteDelaisLiv(af.getUniteDelai());
            re.setUniteDureeGaranti(af.getUniteGarantie());
            re.setUniteDureeVie(af.getUniteVie());
            re.setRefArtExterne(af.getRefArtExterne());
            re.setDesArtExterne(af.getDesArtExterne());
            re.setPuaTtc(af.getPuaTtc());
        }
        return re;
    }

    public static YvsBaseArticleFournisseur buildArticleFournisseur(ArticleFournisseur af) {
        YvsBaseArticleFournisseur re = new YvsBaseArticleFournisseur();
        if (af != null) {
            re.setArticle((af.getArticle().getId() > 0) ? new YvsBaseArticles(af.getArticle().getId()) : null);
            re.setDateLivraison(af.getDateLivraison());
            re.setDelaiLivraison(af.getDelaiLivraison());
            re.setDureeGarantie(af.getDureeGarantie());
            re.setDureeVie(af.getDureeVie());
            re.setFournisseur((af.getFournisseur().getId() > 0) ? new YvsBaseFournisseur(af.getFournisseur().getId()) : null);
            re.setId(af.getId());
            re.setPrincipal(af.isPrincipal());
            re.setPuv(af.getPrix());
            re.setRemise(af.getRemise());
            re.setUniteDelai(af.getUniteDelaisLiv());
            re.setUniteGarantie(af.getUniteDureeGaranti());
            re.setUniteVie(af.getUniteDureeVie());
            re.setPuaTtc(af.isPuaTtc());
        }
        return re;
    }

    public static FicheConditionnement buildBeanFicheConditionnement(YvsProdFicheConditionnement y) {
        FicheConditionnement r = new FicheConditionnement();
        if (y != null) {
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
            r.setQuantite(y.getQuantite());
            r.setStatut(y.getStatut());
            r.setNumero(y.getNumero());
            r.setDateConditionnement(y.getDateConditionnement());
            r.setNomenclature(buildBeanNomenclature(y.getNomenclature()));
            r.setArticle(r.getNomenclature().getCompose());
            r.setConditionnement(r.getNomenclature().getUnite());
            if (y.getDepot() != null ? y.getDepot().getId() > 0 : false) {
                r.setDepot(new Depots(y.getDepot().getId(), y.getDepot().getDesignation()));
            }
            r.setContenus(y.getContenus());
        }
        return r;
    }

    public static YvsProdFicheConditionnement buildFicheConditionnement(FicheConditionnement y, YvsUsersAgence ua) {
        YvsProdFicheConditionnement r = new YvsProdFicheConditionnement();
        if (y != null) {
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
            r.setQuantite(y.getQuantite());
            r.setStatut(y.getStatut());
            r.setNumero(y.getNumero());
            r.setDateConditionnement(y.getDateConditionnement());
            if (y.getNomenclature() != null ? y.getNomenclature().getId() > 0 : false) {
                r.setNomenclature(new YvsProdNomenclature(y.getNomenclature().getId(), y.getNomenclature().getReference(), new YvsBaseArticles(y.getNomenclature().getCompose().getId(), y.getNomenclature().getCompose().getRefArt(), y.getNomenclature().getCompose().getDesignation()), buildConditionnement(y.getNomenclature().getUnite(), ua)));
            }
            if (y.getDepot() != null ? y.getDepot().getId() > 0 : false) {
                r.setDepot(new YvsBaseDepots(y.getDepot().getId(), y.getDepot().getDesignation()));
            }
            r.setAuthor(ua);
            r.setDateUpdate(new Date());
            r.setNew_(true);
        }
        return r;
    }

    public static ContenuConditionnement buildBeanContenuConditionnement(YvsProdContenuConditionnement y) {
        ContenuConditionnement r = new ContenuConditionnement();
        if (y != null) {
            r.setId(y.getId());
            r.setQuantite(y.getQuantite());
            r.setConsommable(y.getConsommable());
            r.setArticle(buildBeanArticles(y.getArticle()));
            r.setCondition(buildBeanConditionnement(y.getConditionnement()));
            r.setFiche(new FicheConditionnement(y.getFiche().getId()));
            r.setDateSave(y.getDateSave());
        }
        return r;
    }

    public static YvsProdContenuConditionnement buildContenuConditionnement(ContenuConditionnement y, YvsUsersAgence ua) {
        YvsProdContenuConditionnement r = new YvsProdContenuConditionnement();
        if (y != null) {
            r.setId(y.getId());
            r.setQuantite(y.getQuantite());
            r.setConsommable(y.isConsommable());
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                r.setArticle(new YvsBaseArticles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()));
            }
            if (y.getCondition() != null ? y.getCondition().getId() > 0 : false) {
                r.setConditionnement(buildConditionnement(y.getCondition(), ua));
            }
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                r.setFiche(new YvsProdFicheConditionnement(y.getFiche().getId()));
            }
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setNew_(true);
        }
        return r;
    }

    public static ClassesStat buildSimpleBeanClasseStat(YvsBaseClassesStat bean) {
        ClassesStat re = new ClassesStat();
        if (bean != null) {
            re.setActif(bean.getActif());
            re.setCodeRef(bean.getCodeRef());
            re.setDateSave(bean.getDateSave());
            re.setDesignation(bean.getDesignation());
            re.setId(bean.getId());
            re.setVisibleJournal(bean.getVisibleJournal());
            re.setVisibleSynthese(bean.getVisibleSynthese());
        }
        return re;
    }

    public static ClassesStat buildBeanClasseStat(YvsBaseClassesStat bean) {
        ClassesStat re = buildSimpleBeanClasseStat(bean);
        if (bean != null) {
            re.setParent(bean.getParent() != null ? bean.getParent().getId() : 0);
        }
        return re;
    }

    public static YvsBaseClassesStat buildClasseStat(ClassesStat bean) {
        YvsBaseClassesStat re = new YvsBaseClassesStat();
        if (bean != null) {
            re.setActif(bean.isActif());
            re.setCodeRef(bean.getCodeRef());
            re.setDateSave(bean.getDateSave());
            re.setDesignation(bean.getDesignation());
            re.setId(bean.getId());
            re.setVisibleJournal(bean.isVisibleJournal());
            re.setVisibleSynthese(bean.isVisibleSynthese());
            if (bean.getParent() > 0) {
                re.setParent(new YvsBaseClassesStat(bean.getParent()));
            }
            re.setDateUpdate(new Date());
            re.setNew_(true);
        }
        return re;
    }

    public static YvsProdMembresEquipe buildEmployeEquipe(EmployeEquipe y, YvsProdEquipeProduction entity, YvsUsersAgence ua) {
        YvsProdMembresEquipe e = new YvsProdMembresEquipe();
        if (y != null) {
            e.setActif(y.isActif());
            e.setId(y.getId());
            if ((y.getProducteur() != null) ? y.getProducteur().getId() != 0 : false) {
//                e.setEmploye(new YvsGrhEmployes(y.getEmploye().getId(), y.getEmploye().getNom(), y.getEmploye().getPrenom()));
            }
            e.setEquipeProduction(entity);
        }
        return e;
    }

    public static YvsProdEquipeProduction buildEquipeProduction(EquipeProduction y, YvsUsersAgence ua) {
        YvsProdEquipeProduction e = new YvsProdEquipeProduction();
        if (y != null) {
            e.setId(y.getId());
            e.setNom(y.getNom());
            e.setReference(y.getReference());
            e.setPrincipal(y.isPrincipal());
            e.setActif(true);
            e.setDateUpdate(new Date());
            e.setDateSave(y.getDateSave());
            e.setAuthor(ua);
            if ((y.getChefEquipe() != null) ? y.getChefEquipe().getId() != 0 : false) {
                e.setChefEquipe(new YvsGrhEmployes(y.getChefEquipe().getId(), y.getChefEquipe().getNom(), y.getChefEquipe().getPrenom()));
            }
            if ((y.getDepot() != null) ? y.getDepot().getId() != 0 : false) {
                e.setDepot(new YvsBaseDepots(y.getDepot().getId(), y.getDepot().getDesignation()));
            }
            if ((y.getSite() != null) ? y.getSite().getId() != 0 : false) {
                e.setSite(new YvsProdSiteProduction(y.getSite().getId(), y.getSite().getReference(), y.getSite().getDesignation()));
            }
            e.setDateSave(y.getDateSave());
        }
        return e;
    }

    public static SessionProd copyBeanSessionProdOf(YvsProdSessionOf s) {
        SessionProd sp = new SessionProd();
        if (s != null ? s.getSessionProd() != null : false) {
            sp.setAuthor(s.getAuthor());
            sp.setDateSave(s.getDateSave());
            sp.setDateSession(s.getSessionProd().getDateSession());
            sp.setDateUpdate(s.getDateUpdate());
            sp.setDepot(UtilCom.buildBeanDepot(s.getSessionProd().getDepot()));
            sp.setEquipe(UtilProd.buildBeanEquipeProduction(s.getSessionProd().getEquipe()));
            sp.setId(s.getId());
            sp.setProducteur(UtilUsers.buildBeanUsers(s.getSessionProd().getProducteur()));
            sp.setTranche(UtilCom.buildBeanTrancheHoraire(s.getSessionProd().getTranche()));
            sp.setOrdre(buildSimpleBeanOf(s.getOrdre()));
        }
        return sp;
    }

    public static SessionProd copyBeanSessionProd(YvsProdSessionProd s) {
        SessionProd sp = new SessionProd();
        if (s != null) {
            sp.setAuthor(s.getAuthor());
            sp.setDateSave(s.getDateSave());
            sp.setDateSession(s.getDateSession());
            sp.setDateUpdate(s.getDateUpdate());
            sp.setDepot(UtilCom.buildBeanDepot(s.getDepot()));
            sp.setEquipe(UtilProd.buildBeanEquipeProduction(s.getEquipe()));
            sp.setId(s.getId());
            sp.setProducteur(UtilUsers.buildBeanUsers(s.getProducteur()));
            sp.setTranche(UtilCom.buildBeanTrancheHoraire(s.getTranche()));
            sp.setActif(s.getActif());
        }
        return sp;
    }

    public static YvsProdSessionOf copyBeanSessionProdOf(SessionProd s) {
        YvsProdSessionOf sp = new YvsProdSessionOf();
        if (s != null ? s.getId() > 0 : false) {
            if (s.getOrdre() != null ? s.getOrdre().getId() > 0 : false) {
                sp.setAuthor(s.getAuthor());
                sp.setDateSave(s.getDateSave());
                sp.setSessionProd(new YvsProdSessionProd());
                sp.getSessionProd().setDateSession(s.getDateSession());
                sp.getSessionProd().setDepot(UtilCom.buildDepot(s.getDepot(), null, null));
                sp.getSessionProd().setTranche(UtilCom.buildTrancheHoraire(s.getTranche(), null));
                sp.getSessionProd().setEquipe(buildEquipeProduction(s.getEquipe(), null));
                sp.getSessionProd().setProducteur(UtilUsers.buildSimpleBeanUsers(s.getProducteur()));
                sp.setDateUpdate(s.getDateUpdate());
                sp.setId(s.getId());

            }
        }
        return sp;
    }
//    public static YvsProdSessionProd copyBeanSessionProd(SessionProd s) {
//        YvsProdSessionProd sp = new YvsProdSessionProd();
//        if (s != null ? s.getId() > 0 : false) {
//            sp.setAuthor(s.getAuthor());
//            sp.setDateSave(s.getDateSave());
//            sp.setSessionProd();
//            sp.setDateSession(s.getSessionProd().getDateSession());
//            sp.setDateUpdate(s.getDateUpdate());
//            sp.setDepot(UtilCom.buildBeanDepot(s.getSessionProd().getDepot()));
//            sp.setEquipe(UtilProd.buildBeanEquipeProduction(s.getSessionProd().getEquipe()));
//            sp.setId(s.getId());
//            sp.setProducteur(UtilUsers.buildBeanUsers(s.getSessionProd().getProducteur()));
//            sp.setTranche(UtilCom.buildBeanTrancheHoraire(s.getSessionProd().getTranche()));
//        }
//        return sp;
//    }
}

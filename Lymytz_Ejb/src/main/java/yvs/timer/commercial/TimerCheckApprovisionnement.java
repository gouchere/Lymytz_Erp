/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.timer.commercial;

import java.util.Calendar;
import java.util.Date;
import yvs.dao.*;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import yvs.timer.InterfaceTimerLocal;

/**
 *
 * @author LYMYTZ-PC
 */
@Singleton
public class TimerCheckApprovisionnement implements InterfaceTimerLocal {

    @EJB
    public DaoInterfaceLocal dao;

//    @Schedule(dayOfMonth = "*", month = "*", year = "*", hour = "*", dayOfWeek = "*", minute = "*/5", persistent = false)
    @Override
    public void myTimer() {
//        System.err.println("---- exécution du timer (APPROVISSION)");
//        List<YvsBaseDepots> depots = dao.loadNameQueries("YvsBaseDepots.findByActif", new String[]{"actif"}, new Object[]{true});
//        List<YvsComArticleApprovisionnement> articles = new ArrayList<>();
//        YvsComArticleApprovisionnement af;
//        for (YvsBaseDepots d : depots) {
//            for (YvsBaseArticleDepot a : d.getArticles()) {
//                boolean correct_ = false;
//                switch (a.getModeReappro()) {
//                    case Constantes.REAPPRO_PERIODE:
//                        Calendar cal = dateToCalendar(a.getDateAppro());
//                        switch (a.getUniteInterval()) {
//                            case Constantes.UNITE_SECONDE:
//                                cal.add(Calendar.SECOND, a.getIntervalApprov());
//                                break;
//                            case Constantes.UNITE_MINUTE:
//                                cal.add(Calendar.MINUTE, a.getIntervalApprov());
//                                break;
//                            case Constantes.UNITE_HEURE:
//                                cal.add(Calendar.HOUR_OF_DAY, a.getIntervalApprov());
//                                break;
//                            case Constantes.UNITE_JOUR:
//                                cal.add(Calendar.DATE, a.getIntervalApprov());
//                                break;
//                            case Constantes.UNITE_SEMAINE:
//                                cal.add(Calendar.WEEK_OF_YEAR, a.getIntervalApprov());
//                                break;
//                            case Constantes.UNITE_MOIS:
//                                cal.add(Calendar.MONTH, a.getIntervalApprov());
//                                break;
//                            case Constantes.UNITE_ANNEE:
//                                cal.add(Calendar.YEAR, a.getIntervalApprov());
//                                break;
//                            default:
//                                break;
//                        }
//                        if (!dateToCalendar(new Date()).before(cal)) {
//                            correct_ = true;
//                        }
//                        break;
//                    case Constantes.REAPPRO_RUPTURE:
//                        if (a.getQuantiteStock() <= a.getStockMin()) {
//                            correct_ = true;
//                        }
//                        break;
//                    case Constantes.REAPPRO_SEUIL:
//                        if (a.getQuantiteStock() <= a.getStockAlert()) {
//                            correct_ = true;
//                        }
//                        break;
//                    default:
//                        break;
//                }
//                if (correct_) {
//                    double qte = a.getLotLivraison();
//                    if (qte == 0) {
//                        qte = a.getStockAlert();
//                    }
//                    if (qte > 0) {
//                        af = new YvsComArticleApprovisionnement((long) articles.size() + 1);
//                        af.setArticle(a);
//                        af.setDateLivraison(new Date());
//                        af.setQuantiteRest(qte);
//                        af.setQuantite(qte);
//                        af.setFiche(new YvsComFicheApprovisionnement(d));
//                        if (a.getGenererDocument()) {
//                            switch (a.getTypeDocumentGenerer()) {
//                                case "FA": {
//                                    YvsBaseFournisseur y = (YvsBaseFournisseur) dao.loadOneByNameQueries("YvsComContenuDocAchat.findFsseurByArticle", new String[]{"article"}, new Object[]{a.getArticle()});
//                                    if (y != null ? (y.getId() != null ? y.getId() < 1 : true) : true) {
//                                        y = (YvsBaseFournisseur) dao.loadOneByNameQueries("YvsBaseArticleFournisseur.findFsseurByArticle", new String[]{"article"}, new Object[]{a.getArticle()});
//                                    }
//                                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
//                                        af.setFournisseur(y);
//                                    }
//                                }
//                                case "FT": {
//                                    YvsBaseDepots y = (YvsBaseDepots) dao.loadOneByNameQueries("YvsComContenuDocStock.findDepotByArticleDepot", new String[]{"article", "depot"}, new Object[]{a.getArticle(), d});
//                                    if (y != null ? (y.getId() != null ? y.getId() < 1 : true) : true) {
//                                        List<YvsBaseDepots> list = dao.loadNameQueries("YvsComLiaisonDepot.findDepotLierByDepot", new String[]{"depot"}, new Object[]{d});
//                                        for (YvsBaseDepots w : list) {
//                                            y = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseArticleDepot.findDepotByArticleDepot", new String[]{"article", "depot"}, new Object[]{a.getArticle(), d});
//                                            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
//                                                break;
//                                            }
//                                        }
//                                    }
//                                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
//                                        af.setDepot(y);
//                                    }
//                                }
//                            }
//                        }
//                        articles.add(af);
//                    }
//                }
//            }
//        }
//        if (!articles.isEmpty()) {
//            YvsComFicheApprovisionnement fiche;
//            YvsComArticleApprovisionnement f_contenu;
//            YvsComDocAchats factures;
//            YvsComContenuDocAchat a_contenu;
//            YvsComDocStocks transfert;
//            YvsComContenuDocStock t_contenu;
//
//            for (YvsComArticleApprovisionnement a : articles) {
//                if (a.getArticle() != null ? (a.getArticle().getId() != null ? a.getArticle().getId() > 0 : false) : false) {
//                    YvsBaseDepots d = a.getFiche().getDepot();
//                    boolean by_fiche = true;
//                    if (a.getArticle().getGenererDocument()) {
//                        switch (a.getArticle().getTypeDocumentGenerer()) {
//                            case "FA": {
//                                if (a.getFournisseur() != null ? (a.getFournisseur().getId() != null ? a.getFournisseur().getId() > 0 : false) : false) {
//                                    factures = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findByDepotsStatutAuto", new String[]{"fournisseur", "source", "statut", "auto"}, new Object[]{a.getFournisseur(), d, Constantes.ETAT_EDITABLE, true});
//                                    if (factures != null ? (factures.getId() != null ? factures.getId() > 0 : false) : false) {
//                                        a_contenu = (YvsComContenuDocAchat) dao.loadOneByNameQueries("YvsComContenuDocAchat.findByArticleDocAchat", new String[]{"docAchat", "article"}, new Object[]{factures, a.getArticle().getArticle()});
//                                        if (a_contenu != null ? (a_contenu.getId() != null ? a_contenu.getId() < 1 : true) : true) {
//                                            a_contenu = new YvsComContenuDocAchat();
//                                            a_contenu.setArticle(a.getArticle().getArticle());
//                                            a_contenu.setDocAchat(factures);
//                                        }
//                                        a_contenu.setQuantiteRecu(a.getQuantite());
//                                        if (a_contenu.getQuantiteRecu() > 0) {
//                                            if (a_contenu.getId() > 0) {
//                                                dao.update(a_contenu);
//                                            } else {
//                                                dao.save1(a_contenu);
//                                            }
//                                        }
//                                    } else {
//                                        if (a.getQuantite() > 0) {
//                                            factures = new YvsComDocAchats();
//                                            factures.setFournisseur(a.getFournisseur());
//                                            factures.setDepotReception(d);
//                                            factures.setDateDoc(new Date());
//                                            factures.setStatut(Constantes.ETAT_EDITABLE);
//                                            factures.setAutomatique(true);
//
//                                            factures = (YvsComDocAchats) dao.save1(factures);
//
//                                            a_contenu = new YvsComContenuDocAchat();
//                                            a_contenu.setArticle(a.getArticle().getArticle());
//                                            a_contenu.setDocAchat(factures);
//                                            a_contenu.setQuantiteCommande(a.getQuantite());
//
//                                            dao.save1(a_contenu);
//                                        }
//                                    }
//                                    by_fiche = false;
//                                }
//                            }
//                            case "FT": {
//                                if (a.getDepot() != null ? (a.getDepot().getId() != null ? a.getDepot().getId() > 0 : false) : false) {
//                                    transfert = (YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findByDepotsStatutAuto", new String[]{"source", "destination", "statut", "auto"}, new Object[]{a.getDepot(), d, Constantes.ETAT_EDITABLE, true});
//                                    if (transfert != null ? (transfert.getId() != null ? transfert.getId() > 0 : false) : false) {
//                                        t_contenu = (YvsComContenuDocStock) dao.loadOneByNameQueries("YvsComContenuDocStock.findByDocArticle", new String[]{"docStock", "article"}, new Object[]{transfert, a.getArticle().getArticle()});
//                                        if (t_contenu != null ? (t_contenu.getId() != null ? t_contenu.getId() < 1 : true) : true) {
//                                            t_contenu = new YvsComContenuDocStock();
//                                            t_contenu.setArticle(a.getArticle().getArticle());
//                                            t_contenu.setDocStock(transfert);
//                                        }
//                                        t_contenu.setQuantite(a.getQuantite());
//                                        if (t_contenu.getQuantite() > 0) {
//                                            if (t_contenu.getId() > 0) {
//                                                dao.update(t_contenu);
//                                            } else {
//                                                dao.save1(t_contenu);
//                                            }
//                                        }
//                                    } else {
//                                        if (a.getQuantite() > 0) {
//                                            transfert = new YvsComDocStocks();
//                                            transfert.setSource(a.getDepot());
//                                            transfert.setDestination(d);
//                                            transfert.setDateDoc(new Date());
//                                            transfert.setStatut(Constantes.ETAT_EDITABLE);
//                                            transfert.setAutomatique(true);
//
//                                            transfert = (YvsComDocStocks) dao.save1(transfert);
//
//                                            t_contenu = new YvsComContenuDocStock();
//                                            t_contenu.setArticle(a.getArticle().getArticle());
//                                            t_contenu.setDocStock(transfert);
//                                            t_contenu.setQuantite(a.getQuantite());
//
//                                            dao.save1(t_contenu);
//                                        }
//                                    }
//                                    by_fiche = false;
//                                }
//                            }
//                        }
//                    }
//                    if (by_fiche) {
//                        fiche = (YvsComFicheApprovisionnement) dao.loadOneByNameQueries("YvsComFicheApprovisionnement.findByDepotStatutAuto", new String[]{"depot", "statut", "auto"}, new Object[]{d, Constantes.ETAT_EDITABLE, true});
//                        if (fiche != null ? (fiche.getId() != null ? fiche.getId() > 0 : false) : false) {
//                            f_contenu = (YvsComArticleApprovisionnement) dao.loadOneByNameQueries("YvsComArticleApprovisionnement.findByArticleFiche", new String[]{"fiche", "article"}, new Object[]{fiche, a.getArticle()});
//                            if (f_contenu != null ? (f_contenu.getId() != null ? f_contenu.getId() > 0 : false) : false) {
//                                a.setId(f_contenu.getId());
//                                a.setFiche(new YvsComFicheApprovisionnement(fiche.getId()));
//                                if (a.getQuantite() > 0) {
//                                    dao.update(a);
//                                }
//                            } else {
//                                a.setFiche(new YvsComFicheApprovisionnement(fiche.getId()));
//                                if (a.getQuantite() > 0) {
//                                    dao.save1(a);
//                                }
//                            }
//                        } else {
//                            if (a.getQuantite() > 0) {
//                                fiche = new YvsComFicheApprovisionnement();
//                                fiche.setDepot(d);
//                                fiche.setDateApprovisionnement(new Date());
//                                fiche.setHeureApprovisionnement(new Date());
//                                fiche.setEtat(Constantes.ETAT_EDITABLE);
//                                fiche.setAuto(true);
//
//                                fiche = (YvsComFicheApprovisionnement) dao.save1(fiche);
//                                a.setFiche(fiche);
//
//                                dao.save1(a);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        System.err.println("---- end timer (APPROVISSION)");
    }

    @Timeout
    public void timeout() {
        System.out.println("Fin du traitement... (APPROVISSION)");
    }

    @Override
    public void avancement() {
        //-
    }

    public static Calendar dateToCalendar(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(date);
            return cal;
        }
        return Calendar.getInstance();
    }
}

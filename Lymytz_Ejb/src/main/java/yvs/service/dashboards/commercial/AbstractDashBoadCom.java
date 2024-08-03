/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.dashboards.commercial;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.param.YvsSocietes;
import yvs.entity.produits.group.YvsBaseFamilleArticle;

/**
 *
 * @author Lymytz-pc
 */
public abstract class AbstractDashBoadCom {

    DaoInterfaceLocal dao;
    protected DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    public AbstractDashBoadCom(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public List<Object[]> getTableauBordArticles(String societe, String agence, String point, String vendeur, String commercial, String client, String famille, String groupe, String article, String categorie, String type, String date_debut, String date_fin, String periode, String offset, String limit) {
        try {
            Date debut = df.parse(date_debut);
            Date fin = df.parse(date_fin);
            String rq = "select y.* from public.com_et_total_articles(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) y order by rang, total desc";
            return dao.loadDataByNativeQuery(rq, new Object[]{Long.valueOf(societe), Long.valueOf(agence), Long.valueOf(point), Long.valueOf(vendeur), Long.valueOf(commercial), Long.valueOf(client), Long.valueOf(famille), Long.valueOf(groupe), debut, fin, article, categorie, periode, type, Long.valueOf(offset), Long.valueOf(limit)});

        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(AbstractDashBoadCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> getTableauBordCA(String societe, String agence, String date_debut, String date_fin) {
        try {
            Date debut = df.parse(date_debut);
            Date fin = df.parse(date_fin);
            String rq = "select y.* from public.com_et_dashboard(?,?,?,?,?) y";
            Long a = Long.valueOf(agence);
            return dao.loadDataByNativeQuery(rq, new Object[]{Long.valueOf(societe), a != null ? a : 0, 0, debut, fin});
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(AbstractDashBoadCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> getTableauBordCAGeneral(String societe, String agence, String date_debut, String date_fin) {
        try {
            Date dateDebut = df.parse(date_debut);
            Date dateFin = df.parse(date_fin);

            Calendar debut = Calendar.getInstance();
            debut.setTime(dateDebut);
            debut.add(Calendar.YEAR, -1);
            Date dateDebutPrec = debut.getTime();

            Calendar fin = Calendar.getInstance();
            fin.setTime(dateFin);
            fin.add(Calendar.YEAR, -1);
            Date dateFinPrec = fin.getTime();

            String rq = "select y.* from public.com_et_dashboard_generale(?,?,?,?,?,?,?) y";
            return dao.loadDataByNativeQuery(rq, new Object[]{Long.valueOf(societe), Long.valueOf(agence), dateDebut, dateFin, dateDebutPrec, dateFinPrec, dateFinPrec});

        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(AbstractDashBoadCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> getTableauBordPoint(String agence_, String point_, String date_debut_, String date_fin_, String periode_) {
        try {
            Date debut = df.parse(date_debut_);
            Date fin = df.parse(date_fin_);
            String rq = "SELECT * FROM et_total_one_pt_vente(?,?,?,?,?)";
            Long a = Long.valueOf(agence_);
            return dao.loadDataByNativeQuery(rq, new Object[]{a != null ? a : 0, point_, debut, fin, periode_.charAt(0),});

        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(AbstractDashBoadCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> getTableauBordPoints(String societe, String agence, String date_debut_, String date_fin_, String reference, String periode_) {
        try {
            Date debut = df.parse(date_debut_);
            Date fin = df.parse(date_fin_);
            String rq = "SELECT * FROM et_total_pt_vente(?,?,?,?,?,?) order by rang,total desc";
            Long a = Long.valueOf(agence);
            return dao.loadDataByNativeQuery(rq, new Object[]{Long.valueOf(societe), a != null ? a : 0, debut, fin, reference, periode_});

        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(AbstractDashBoadCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> getTableauBordArticle(String agence_, String point_, String articles_, String date_debut_, String date_fin_, String periode_) {
        try {
            Date debut = df.parse(date_debut_);
            Date fin = df.parse(date_fin_);
            String rq = "SELECT * FROM et_total_article_pt_vente(?,?,?,?,?,?)";
            return dao.loadDataByNativeQuery(rq, new Object[]{Long.valueOf(agence_), Long.valueOf(point_), articles_, debut, fin, periode_});
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(AbstractDashBoadCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> getTableauBordArticle_(String agence_, String articles_, String date_debut_, String date_fin_, String periode_) {
        try {
            Date debut = df.parse(date_debut_);
            Date fin = df.parse(date_fin_);
            String rq = "SELECT * FROM et_total_article_pt_vente(?,?,?,?,?,?)";
            return dao.loadDataByNativeQuery(rq, new Object[]{Long.valueOf(agence_), 0, articles_, debut, fin, periode_});
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(AbstractDashBoadCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> getTableauBordStock(String date_, String categorie_, String depots_, String groupe_by_, String societe_) {
        try {
            Date debut = df.parse(date_);
            String rq = "SELECT * FROM com_et_valorise_stock(?,?,?,?,?)";
            return dao.loadDataByNativeQuery(rq, new Object[]{debut, "", depots_, groupe_by_, Long.valueOf(societe_)});
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(AbstractDashBoadCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> getTableauBordStock(String date_, String categorie_, String depots_, String groupe_by_, String societe_, String famille_) {
        try {
            Date debut = df.parse(date_);
            String rq = "SELECT * FROM com_et_valorise_stock(?,?,?,?,?)";
            return dao.loadDataByNativeQuery(rq, new Object[]{debut, "", depots_, groupe_by_, Long.valueOf(societe_)});
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(AbstractDashBoadCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> getTableauBordVendeur(String societe, String agence, String debut, String fin, String reference, String periode) {
        try {
            Date debuts = df.parse(debut);
            Date fins = df.parse(fin);
            String rq = "SELECT * FROM et_total_vendeurs(?,?,?,?,?,?) order by rang, total desc";
            Long a = Long.valueOf(agence);
            return dao.loadDataByNativeQuery(rq, new Object[]{Long.valueOf(societe), a != null ? a : 0, debuts, fins, reference, periode});
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(AbstractDashBoadCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> getTableauBordClient(String date_debut, String date_fin, boolean all, String societe) {
        try {
            Date debuts = df.parse(date_debut);
            Date fins = df.parse(date_fin);
            String rq = "SELECT * FROM et_total_clients(?,?,?,?) order by ca desc";
            return dao.loadDataByNativeQuery(rq, new Object[]{debuts, fins, all, Long.valueOf(societe)});
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(AbstractDashBoadCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> getJournalVente(String societe, String agence, String date_debut, String date_fin, String famille) {
        try {
            Date debuts = df.parse(date_debut);
            Date fins = df.parse(date_fin);
            String rq = "SELECT * FROM com_et_journal_vente(?,?,?,?,?) order by is_vendeur,is_classe, montant desc";
            Long agences = asString(agence) ? Long.valueOf(agence) : 0;
            Long societes = asString(societe) ? Long.valueOf(societe) : 0;
            boolean family = false;
            if (asString(famille)) {
                if (famille.equals("true")) {
                    family = true;
                } else {
                    family = false;
                }
            }
            return dao.loadDataByNativeQuery(rq, new Object[]{societes, agences, debuts, fins, family});
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(AbstractDashBoadCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> getInventaire(String societe, String agence_, String depot_, String famille_, String categorie_, String groupe_, String date_, String print_all_, String option_print_, String valoriser_, String articles_, String offset, String limit) {
        try {
            String rq = "SELECT * FROM com_inventaire(?,?,?,?,?,?,?,?::date,?,?,?,?,?,?,?,?)";
            Long agence = asString(agence_) ? Long.valueOf(agence_) : 0;
            Long depot = asString(depot_) ? Long.valueOf(depot_) : 0;
            Long famille = asString(famille_) ? Long.valueOf(famille_) : 0;
            Long groupe = asString(groupe_) ? Long.valueOf(groupe_) : 0;
            Long societes = asString(societe) ? Long.valueOf(societe) : 0;
            Long of = asString(offset) ? Long.valueOf(offset) : 0;
            Long l = asString(limit) ? Long.valueOf(limit) : 0;
            return dao.loadDataByNativeQuery(rq, new Object[]{societes, agence, depot,0, famille, categorie_, groupe, date_, Boolean.valueOf(print_all_), option_print_, valoriser_, articles_, of, l,true,false});
        } catch (Exception ex) {
            Logger.getLogger(AbstractDashBoadCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean asString(String valeur) {
        if (valeur != null ? valeur.trim().length() > 0 : false) {
            return true;
        }
        return false;
    }

    public List<YvsBaseFamilleArticle> getFamille(String societe) {
        try {
            if (asString(societe)) {
                return dao.loadNameQueries("YvsBaseFamilleArticle.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }
}

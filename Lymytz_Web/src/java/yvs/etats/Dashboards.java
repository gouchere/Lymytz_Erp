/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.etats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.param.YvsSocietes;
import yvs.etats.commercial.InventairePreparatoire;
import yvs.etats.commercial.JournalVendeur;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class Dashboards extends Gestionnaire implements Serializable, Cloneable {

    public Dashboards() {
        if (dateDebut.equals(dateFin) && dateDebut.equals(new Date())) {
            Calendar c = Calendar.getInstance();
            c.set(c.get(Calendar.YEAR), 0, 1);
            dateDebut = c.getTime();
        }
        inventaires = new ArrayList<>();
        filterInventaires = new ArrayList<>();
    }

    public Dashboards(String nature) {
        this();
        this.nature = nature;
    }

    public void returnDashBoardVendeur(DaoInterfaceLocal dao) {
        returnDashBoardVendeur(societe, agence, client, vendeur, dateDebut, dateFin, cumule, dao);
    }

    public void returnDashBoardVendeur(long societe, long agence, long client, long vendeur, Date dateDebut, Date dateFin, boolean grouper, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(client, 3), new Options(vendeur, 4), new Options(dateDebut, 5), new Options(dateFin, 6), new Options(grouper, 7)};
        String query = "select y.client, y.nom_client, y.numero, y.date, y.montant, y.avance, y.reste, y.vendeur, y.nom_vendeur from public.com_et_dashboard_vendeur(?,?,?,?,?,?,?) y";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long _client_ = (Long) o[0];
                    String _nom_client_ = (String) o[1];
                    String _numero_ = (String) o[2];
                    Date _date_ = (Date) o[3];
                    Double _montant_ = (Double) o[4];
                    Double _avance_ = (Double) o[5];
                    Double _reste_ = (Double) o[6];
                    Long _vendeur_ = (Long) o[7];
                    String _nom_vendeur_ = (String) o[8];

                    JournalVendeur v = new JournalVendeur(_vendeur_, _nom_vendeur_);
                    int idx = valeurs.indexOf(v);
                    if (idx > -1) {
                        v = valeurs.get(idx);
                    }
                    JournalVendeur c = new JournalVendeur(0, _nom_client_);
                    int idz = v.getSous().indexOf(c);
                    if (idz > -1) {
                        c = v.getSous().get(idz);
                    }
                    c.getSous().add(new JournalVendeur(_client_, _numero_, _date_, _montant_, _avance_, _reste_));
                    if (idz > -1) {
                        v.getSous().set(idz, c);
                    } else {
                        v.getSous().add(c);
                    }
                    if (idx > -1) {
                        valeurs.set(idx, v);
                    } else {
                        valeurs.add(v);
                    }
                }
            }
            totaux = 0;
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnStockArticle(DaoInterfaceLocal dao) {
        returnStockArticle(dateDebut, categorie, depots, nature, societe, dao);
    }

    public void returnStockArticle(Date date, String categorie, String depots, String nature, long societe, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(date, 1), new Options(categorie, 2), new Options(depots, 3), new Options(nature, 4), new Options(societe, 5)};
        String query = "select y.id, y.reference, y.designation, y.unite, y.depot, y.quantite, y.prix_revient, y.prix_vente, y.prix_achat from public.com_et_valorise_stock(?,?,?,?,?) y order by y.reference";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long id = (Long) o[0];
                    String reference = (String) o[1];
                    String designation = (String) o[2];
                    String unite = (String) o[3];
                    String depot = (String) o[4];
                    Double quantite = (Double) o[5];
                    Double prix_revient = (Double) o[6];
                    Double prix_vente = (Double) o[7];
                    Double prix_achat = (Double) o[8];

                    JournalVendeur e = new JournalVendeur(0, depot);
                    int idx = valeurs.indexOf(e);
                    if (idx > -1) {
                        e = valeurs.get(idx);
                    }
                    e.getSous().add(new JournalVendeur(id, reference, designation, unite, quantite, prix_revient, prix_vente, prix_achat));
                    if (idx > -1) {
                        valeurs.set(idx, e);
                    } else {
                        valeurs.add(e);
                    }
                }
            }
            totaux = 0;
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnSoldeFournisseur(DaoInterfaceLocal dao) {
        returnSoldeCaisses(societe, agence, dateDebut, dateFin, periode, compteDebut, 0, dao);
    }

    public void returnSoldeFournisseur(int limit, DaoInterfaceLocal dao) {
        returnSoldeFournisseur(societe, agence, dateDebut, dateFin, periode, compteDebut, limit, dao);
    }

    public void returnSoldeFournisseur(long societe, long agence, Date dateDebut, Date dateFin, String periode, String reference, int limit, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(reference, 3), new Options(dateDebut, 4), new Options(dateFin, 5), new Options(periode, 6)};
        String query = "select y.id, y.entete, y.ttc, y.avance, y.solde, y.rang, y.code, y.nom, y.solde_initial, y.coup_sup, y.acompte, y.credit from public.com_et_solde_fournisseur(?, ?, ?, ?, ?, ?) y";
        if (limit > 0) {
            query += " INNER JOIN yvs_base_fournisseur f ON y.id = f.id WHERE f.exclus_for_home IS FALSE order by y.solde limit " + limit;
        } else {
            query += " order by y.code, y.nom, y.rang";
        }
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long id = (Long) o[0];
                    String entete = (String) o[1];
                    Double ttc = (Double) o[2];
                    Double avance = (Double) o[3];
                    Double solde = (Double) o[4];
                    Integer rang = (Integer) o[5];
                    String code = (String) o[6];
                    String nom = (String) o[7];
                    if (!elements.contains(id)) {
                        elements.add(id);
                        lignes.add(new Rows(id, code, nom, ""));
                    }
                    if (!periodes.contains(entete)) {
                        periodes.add(entete);
                        colonnes.add(new Columns(entete, rang));
                    }
                    valeurs.add(new JournalVendeur(id, entete, ttc, avance, solde, rang));
                }
            }
            Collections.sort(colonnes);
            saveRows();
            addTotaux(JournalVendeur.TYPE_TTC + ";" + JournalVendeur.TYPE_QUANTITE + ";" + JournalVendeur.TYPE_TAUX);
            totaux = 0;
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnSoldeCaisses(DaoInterfaceLocal dao) {
        returnSoldeCaisses(societe, agence, dateDebut, dateFin, periode, compteDebut, 0, dao);
    }

    public void returnSoldeCaisses(int limit, DaoInterfaceLocal dao) {
        returnSoldeCaisses(societe, agence, dateDebut, dateFin, periode, compteDebut, limit, dao);
    }

    public void returnSoldeCaisses(long societe, long agence, Date dateDebut, Date dateFin, String periode, String reference, int limit, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(reference, 3), new Options(dateDebut, 4), new Options(dateFin, 5), new Options(periode, 6)};
        String query = "select y.caisse, y.periode, y.recette, y.depense, y.solde, y.rang, y.code, y.intitule from public.compta_total_caisses(?, ?, ?, 0, '', '', '', 'P', ?, ?, ?) y";
        if (limit > 0) {
            query += " order by y.solde DESC limit " + limit;
        } else {
            query += " order by y.code, y.intitule, y.rang";
        }
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long caisse = (Long) o[0];
                    String entete = (String) o[1];
                    Double recette = (Double) o[2];
                    Double depense = (Double) o[3];
                    Double solde = (Double) o[4];
                    Integer rang = (Integer) o[5];
                    String code = (String) o[6];
                    String nom = (String) o[7];
                    if (!elements.contains(caisse)) {
                        elements.add(caisse);
                        lignes.add(new Rows(caisse, code, nom, ""));
                    }
                    if (!periodes.contains(entete)) {
                        periodes.add(entete);
                        colonnes.add(new Columns(entete, rang));
                    }
                    valeurs.add(new JournalVendeur(caisse, entete, recette, depense, solde, rang));
                }
            }
            Collections.sort(colonnes);
            saveRows();
            addTotaux(JournalVendeur.TYPE_TTC + ";" + JournalVendeur.TYPE_QUANTITE + ";" + JournalVendeur.TYPE_TAUX);
            totaux = 0;
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnTotalVendeurs(DaoInterfaceLocal dao) {
        returnTotalVendeurs(societe, agence, dateDebut, dateFin, periode, compteDebut, 0, dao);
    }

    public void returnTotalVendeurs(int limit, DaoInterfaceLocal dao) {
        returnTotalVendeurs(societe, agence, dateDebut, dateFin, periode, compteDebut, limit, dao);
    }

    public void returnTotalVendeurs(long societe, long agence, Date dateDebut, Date dateFin, String periode, String reference, int limit, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(dateDebut, 3), new Options(dateFin, 4), new Options(reference, 5), new Options(periode, 6)};
        String query = "select y.vendeur, y.jour, y.total, y.quantite, y.taux, y.rang, y.code, y.nom from public.et_total_vendeurs(?,?,?,?,?,?) y";
        if (limit > 0) {
            query += " order by y.total DESC limit " + limit;
        } else {
            query += " order by y.code, y.nom, y.rang";
        }
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long vendeur = (Long) o[0];
                    String entete = (String) o[1];
                    Double total = (Double) o[2];
                    Double quantite = (Double) o[3];
                    Double taux = (Double) o[4];
                    Integer rang = (Integer) o[5];
                    String code = (String) o[6];
                    String nom = (String) o[7];
                    if (!elements.contains(vendeur)) {
                        elements.add(vendeur);
                        lignes.add(new Rows(vendeur, code, nom, ""));
                    }
                    if (!periodes.contains(entete)) {
                        periodes.add(entete);
                        colonnes.add(new Columns(entete, rang));
                    }
                    valeurs.add(new JournalVendeur(vendeur, entete, total, quantite, taux, rang));
                }
            }
            Collections.sort(colonnes);
            saveRows();
            addTotaux(JournalVendeur.TYPE_TTC + ";" + JournalVendeur.TYPE_QUANTITE + ";" + JournalVendeur.TYPE_TAUX);
            totaux = 0;
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnTotalClients(DaoInterfaceLocal dao) {
        returnTotalClients(societe, dateDebut, dateFin, periode, compteDebut, 0, dao);
    }

    public void returnTotalClients(int limit, DaoInterfaceLocal dao) {
        returnTotalClients(societe, dateDebut, dateFin, periode, compteDebut, limit, dao);
    }

    public void returnTotalClients(long societe, Date dateDebut, Date dateFin, String periode, String reference, int limit, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(dateDebut, 2), new Options(dateFin, 3), new Options(reference, 4), new Options(periode, 5)};
        String query = "select y.client, y.jour, y.total, y.quantite, y.taux, y.rang, y.code, y.nom from public.et_total_clients(?,?,?,?,?) y ";
        if (limit > 0) {
            query += " INNER JOIN yvs_com_client c ON y.client = c.id WHERE c.defaut IS FALSE AND c.exclus_for_home IS FALSE order by y.total DESC limit " + limit;
        } else {
            query += " order by y.code, y.nom, y.rang";
        }
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long client = (Long) o[0];
                    String entete = (String) o[1];
                    Double total = (Double) o[2];
                    total = total != null ? total : 0;
                    Double quantite = (Double) o[3];
                    quantite = (quantite != null) ? quantite : 0;
                    Double taux = (Double) o[4];
                    taux = (taux != null) ? taux : 0;
                    Integer rang = (Integer) o[5];
                    rang = (rang != null) ? rang : 0;
                    String code = (String) o[6];
                    String nom = (String) o[7];
                    if (!elements.contains(client)) {
                        elements.add(client);
                        lignes.add(new Rows(client != null ? client : 0L, code, nom, ""));
                    }
                    if (!periodes.contains(entete)) {
                        periodes.add(entete);
                        colonnes.add(new Columns(entete, rang));
                    }
                    valeurs.add(new JournalVendeur(client, entete, total, quantite, taux, rang));
                }
            }
            Collections.sort(colonnes);
            saveRows();
            addTotaux(JournalVendeur.TYPE_TTC + ";" + JournalVendeur.TYPE_QUANTITE + ";" + JournalVendeur.TYPE_TAUX);
            totaux = 0;
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnDashboardClients(DaoInterfaceLocal dao) {
        returnDashboardClients(societe, dateDebut, dateFin, compteDebut, periode, dao);
    }

    public void returnDashboardClients(long societe, Date dateDebut, Date dateFin, String reference, String periode, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(dateDebut, 2), new Options(dateFin, 3), new Options(reference, 4), new Options(periode, 5)};
        String query = "select y.client, y.code, y.nom, y.jour, y.total, y.versement, y.solde, y.ecart, y.rang from public.com_et_dashboard_client(?,?,?,?,?) y order by y.code, y.rang";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                int i = 0;
                if (o != null ? o.length > 0 : false) {
                    Long client = (Long) o[i++];
                    String code = (String) o[i++];
                    String nom = (String) o[i++];
                    String entete = (String) o[i++];
                    Double total = (Double) o[i++];
                    total = total != null ? total : 0;
                    Double versement = (Double) o[i++];
                    versement = (versement != null) ? versement : 0;
                    Double solde = (Double) o[i++];
                    solde = (solde != null) ? solde : 0;
                    Double ecart = (Double) o[i++];
                    ecart = (ecart != null) ? ecart : 0;
                    Integer rang = (Integer) o[i++];
                    rang = (rang != null) ? rang : 0;
                    if (!elements.contains(client)) {
                        elements.add(client);
                        lignes.add(new Rows((client != null ? client : 0L), code, nom));
                    }
                    if (!periodes.contains(entete)) {
                        periodes.add(entete);
                        colonnes.add(new Columns(entete, rang));
                    }
                    valeurs.add(new JournalVendeur(client, entete, total, versement, solde, ecart, rang));
                }
            }
            lignesFind = new ArrayList<>(lignes);
            elementsFinds = new ArrayList<>(elements);
            totaux = 0;
            Collections.sort(colonnes);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnArticleClient(DaoInterfaceLocal dao) {
        returnArticles(societe, agence, point, vendeur, commercial, client, famille, classe, dateDebut, dateFin, periode, comptes, categorie, nature, offset, limit, dao);
    }

    public void returnArticleVendeurs(DaoInterfaceLocal dao) {
        returnArticles(societe, agence, point, vendeur, commercial, client, famille, classe, dateDebut, dateFin, periode, comptes, categorie, nature, offset, limit, dao);
    }

    public void returnArticleCommercial(DaoInterfaceLocal dao) {
        returnArticles(societe, agence, point, vendeur, commercial, client, famille, classe, dateDebut, dateFin, periode, comptes, categorie, nature, offset, limit, dao);
    }

    public void returnArticleAgence(DaoInterfaceLocal dao) {
        returnArticles(societe, agence, point, vendeur, commercial, client, famille, classe, dateDebut, dateFin, periode, comptes, categorie, nature, offset, limit, dao);
    }

    public void returnArticlePoints(DaoInterfaceLocal dao) {
        returnArticles(societe, agence, point, vendeur, commercial, client, famille, classe, dateDebut, dateFin, periode, comptes, categorie, nature, offset, limit, dao);
    }

    public void returnArticles(int limit, DaoInterfaceLocal dao) {
        returnArticles(societe, agence, point, vendeur, commercial, client, famille, classe, dateDebut, dateFin, periode, comptes, categorie, nature, offset, limit, true, dao);
    }

    public void returnArticles(DaoInterfaceLocal dao) {
        returnArticles(societe, agence, point, vendeur, commercial, client, famille, classe, dateDebut, dateFin, periode, comptes, categorie, nature, offset, limit, dao);
    }

    public void returnArticles(long societe, long agence, long point, long vendeur, long commercial, long client, long famille, long classe, Date dateDebut, Date dateFin, String periode, String articles, String categorie, String type, long offset, long limit, DaoInterfaceLocal dao) {
        returnArticles(societe, agence, point, vendeur, commercial, client, famille, classe, dateDebut, dateFin, periode, articles, categorie, type, offset, limit, false, dao);
    }

    public void returnArticles(long societe, long agence, long point, long vendeur, long commercial, long client, long famille, long classe, Date dateDebut, Date dateFin, String periode, String articles, String categorie, String type, long offset, long limit, boolean cut, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        if (parametreCom == null) {
            parametreCom = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(societe)});
        }
        String query = "select * from public.com_et_total_articles(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) y ";
        if (cut) {
            query += " order by (y.total - y.totalpr) DESC limit " + limit;
            limit = 0;
        } else {
            query += " order by y.code, y.nom, y.rang";
        }
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(point, 3), new Options(vendeur, 4), new Options(commercial, 5), new Options(client, 6), new Options(famille, 7), new Options(classe, 8), new Options(dateDebut, 9), new Options(dateFin, 10), new Options(articles, 11), new Options(categorie, 12), new Options(periode, 13), new Options(type, 14), new Options(offset, 15), new Options(limit, 16)};
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            periodes.clear();
            colonnes.clear();
            valeurs.clear();

            Object[] line;
            Rows rows;
            String code;
            String nom;
            Long article;
            Long unite;
            String reference;
            String entete;
            Double total;
            Double quantite;
            Double pr;
            Double totalpr;
            Double taux;
            Double marge_min;
            Integer rang;
            Double attente;

            for (Object y : qr.getResultList()) {
                line = (Object[]) y;
                if (line != null ? line.length > 0 : false) {
                    int i = 0;
                    code = (String) line[i++];
                    nom = (String) line[i++];
                    article = (Long) line[i++];
                    unite = (Long) line[i++];
                    reference = (String) line[i++];
                    entete = (String) line[i++];
                    total = (Double) line[i++];
                    quantite = (Double) line[i++];
                    pr = (Double) line[i++];
                    totalpr = (Double) line[i++];
                    taux = (Double) line[i++];
                    marge_min = (Double) line[i++];
                    rang = (Integer) line[i++];
                    attente = 0D;
                    if (unite != null ? unite < 1 : true) {
                        unite = article;
                    }
                    if ((total - totalpr) != 0) {
                        if (parametreCom != null ? parametreCom.getTauxMargeSur().equals("A") : false) {
                            attente = totalpr != 0 ? ((total - totalpr) / totalpr) * 100 : 0;
                        } else {
                            attente = total != 0 ? ((total - totalpr) / total) * 100 : 0;
                        }
                    }

                    if (!elements.contains(unite)) {
                        elements.add(unite);
                        lignes.add(new Rows(unite, article, code, nom, reference));
                    }
                    if (!periodes.contains(entete)) {
                        periodes.add(entete);
                        colonnes.add(new Columns(entete, rang));
                    }
                    valeurs.add(new JournalVendeur(unite, entete, "", (total != null ? total : 0), (quantite != null ? quantite : 0), (totalpr != null ? totalpr : 0), (attente != null ? attente : 0), (taux != null ? taux : 0), (marge_min != null ? marge_min : 0), (rang != null ? rang : 0)));
                }
            }
            Collections.sort(colonnes);
            saveRows();
            addTotaux(JournalVendeur.TYPE_TTC + ";" + JournalVendeur.TYPE_QUANTITE + ";" + JournalVendeur.TYPE_PRIX_REVIENT + ";" + JournalVendeur.TYPE_MARGE + ";" + JournalVendeur.TYPE_ATTENTE + ";" + JournalVendeur.TYPE_TAUX + ";" + JournalVendeur.TYPE_MARGE_MIN, JournalVendeur.TYPE_ATTENTE);
            totaux = 0;
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnArticlesAchat(DaoInterfaceLocal dao) {
        returnArticlesAchat(societe, agence, depot, fournisseur, famille, classe, dateDebut, dateFin, periode, comptes, categorie, nature, offset, limit, dao);
    }

    public void returnArticlesAchat(long societe, long agence, long depot, long fournisseur, long famille, long classe, Date dateDebut, Date dateFin, String periode, String articles, String categorie, String type, long offset, long limit, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(depot, 3), new Options(fournisseur, 4), new Options(famille, 5), new Options(classe, 6), new Options(dateDebut, 7), new Options(dateFin, 8), new Options(articles, 9), new Options(categorie, 10), new Options(periode, 11), new Options(type, 12), new Options(offset, 13), new Options(limit, 14)};
        String query = "select * from public.com_et_total_articles_achat(?,?,?,?,?,?,?,?,?,?,?,?,?,?) y order by y.code, y.nom, y.rang";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            periodes.clear();
            colonnes.clear();
            valeurs.clear();

            Object[] line;
            Rows rows;
            String code;
            String nom;
            Long article;
            Long unite;
            String reference;
            String entete;
            Double total;
            Double quantite;
            Double taux;
            Integer rang;

            for (Object y : qr.getResultList()) {
                line = (Object[]) y;
                if (line != null ? line.length > 0 : false) {
                    int i = 0;
                    code = (String) line[i++];
                    nom = (String) line[i++];
                    article = (Long) line[i++];
                    unite = (Long) line[i++];
                    reference = (String) line[i++];
                    entete = (String) line[i++];
                    total = (Double) line[i++];
                    quantite = (Double) line[i++];
                    taux = (Double) line[i++];
                    rang = (Integer) line[i++];

                    if (!elements.contains(unite)) {
                        elements.add(unite);
                        lignes.add(new Rows(unite, article, code, nom, reference));
                    }
                    if (!periodes.contains(entete)) {
                        periodes.add(entete);
                        colonnes.add(new Columns(entete, rang));
                    }
                    valeurs.add(new JournalVendeur(unite, entete, "", (total != null ? total : 0), (quantite != null ? quantite : 0), 0, 0, (taux != null ? taux : 0), 0, (rang != null ? rang : 0)));
                }
            }
            Collections.sort(colonnes);
            saveRows();
            addTotaux(JournalVendeur.TYPE_TTC + ";" + JournalVendeur.TYPE_QUANTITE + ";" + JournalVendeur.TYPE_PRIX_REVIENT + ";" + JournalVendeur.TYPE_MARGE + ";" + JournalVendeur.TYPE_ATTENTE + ";" + JournalVendeur.TYPE_TAUX + ";" + JournalVendeur.TYPE_MARGE_MIN, JournalVendeur.TYPE_ATTENTE);
            totaux = 0;
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnTotalAgence(DaoInterfaceLocal dao) {
        returnTotalAgence(societe, dateDebut, dateFin, periode, compteDebut, ordres, descOrdre, limit, dao);
    }

    public void returnTotalAgence(long societe, Date dateDebut, Date dateFin, String periode, String reference, String ordre, String descOrdre, int limit, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        if (!Util.asString(ordre)) {
            ordre = "y.code, y.nom";
        }
        String ordreBy = (ordre + " " + descOrdre).trim();
        Options[] param = new Options[]{new Options(societe, 1), new Options(dateDebut, 2), new Options(dateFin, 3), new Options(reference, 4), new Options(periode, 5)};
        String query = "select y.agence, y.jour, y.total, y.quantite, y.taux, y.rang, y.is_total, y.is_footer, y.code, y.nom from public.et_total_agence(?,?,?,?,?) y order by y.is_total, y.is_footer, " + ordreBy + ", y.rang";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            periodes.clear();
            colonnes.clear();
            valeurs.clear();
            valeursTotauxRow.clear();
            boolean addTotaux = true;
            Object[] o;

            Long agence;
            String entete;
            Double total;
            Double quantite;
            Double taux;
            Integer rang;
            Boolean is_total;
            Boolean is_footer;
            String code;
            String nom;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    agence = (Long) o[0];
                    entete = (String) o[1];
                    total = (Double) o[2];
                    quantite = (Double) o[3];
                    taux = (Double) o[4];
                    rang = (Integer) o[5];
                    is_total = (Boolean) o[6];
                    is_footer = (Boolean) o[7];
                    code = (String) o[8];
                    nom = (String) o[9];
                    if (!elements.contains(agence)) {
                        elements.add(agence);
                        lignes.add(new Rows(agence, code, nom, "", is_footer));
                    }
                    if (!periodes.contains(entete)) {
                        periodes.add(entete);
                        colonnes.add(new Columns(entete, rang));
                    }
                    valeurs.add(new JournalVendeur(agence, entete, total, quantite, taux, rang, is_total, is_footer));
                    if (entete.equals("TOTAUX") || code.equals("TOTAUX")) {
                        addTotaux = false;
                    }
                }
            }
            if (limit > 0) {
                limitValue(limit);
            }
            totaux = 0;
            Collections.sort(colonnes);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnTotalPoints(DaoInterfaceLocal dao) {
        returnTotalPoints(societe, agence, dateDebut, dateFin, periode, compteDebut, 0, dao);
    }

    public void returnTotalPoints(int limit, DaoInterfaceLocal dao) {
        returnTotalPoints(societe, agence, dateDebut, dateFin, periode, compteDebut, limit, dao);
    }

    public void returnTotalPoints(long societe, long agence, Date dateDebut, Date dateFin, String periode, String reference, int limit, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(dateDebut, 3), new Options(dateFin, 4), new Options(reference, 5), new Options(periode, 6)};
        String query = "select y.point, y.jour, y.total, y.quantite, y.taux, y.rang, y.code, y.nom from public.et_total_pt_vente(?,?,?,?,?,?) y";
        if (limit > 0) {
            query += " order by y.total DESC limit " + limit;
        } else {
            query += " order by y.code, y.nom, y.rang";
        }
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            periodes.clear();
            colonnes.clear();
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long point = (Long) o[0];
                    String entete = (String) o[1];
                    Double total = (Double) o[2];
                    Double quantite = (Double) o[3];
                    Double taux = (Double) o[4];
                    Integer rang = (Integer) o[5];
                    String code = (String) o[6];
                    String nom = (String) o[7];
                    if (!elements.contains(point)) {
                        elements.add(point);
                        lignes.add(new Rows(point, code, nom, ""));
                    }
                    if (!periodes.contains(entete)) {
                        periodes.add(entete);
                        colonnes.add(new Columns(entete, rang));
                    }
                    valeurs.add(new JournalVendeur(point, entete, total, quantite, taux, rang));
                }
            }
            Collections.sort(colonnes);
            saveRows();
            addTotaux(JournalVendeur.TYPE_TTC + ";" + JournalVendeur.TYPE_QUANTITE + ";" + JournalVendeur.TYPE_TAUX + ";");

            totaux = 0;
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnInventaire(long societe, long agence, long depot, long famille, String categorie, long groupe, Date date, boolean print_all, String option_print, String type, String article, long offset, long limit, boolean preparatoire, DaoInterfaceLocal dao) {
        returnInventaire(societe, agence, depot, famille, categorie, groupe, date, print_all, option_print, type, article, offset, limit, preparatoire, false, dao);
    }

    public void returnInventaire(long societe, long agence, long depot, long famille, String categorie, long groupe, Date date, boolean print_all, String option_print, String type, String article, long offset, long limit, boolean preparatoire, boolean multi, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(depot, 3), new Options(famille, 4), new Options(0, 5), new Options(categorie, 6), new Options(groupe, 7), new Options(date, 8), new Options(print_all, 9), new Options(option_print, 10), new Options(type, 11), new Options(article, 12), new Options(offset, 13), new Options(limit, 14), new Options(preparatoire, 15)};
        String query = "select y.depot, y.article, y.code, y.designation, y.numero, y.famille, y.unite, y.reference, y.prix, y.puv, y.pua, y.pr, y.stock, y.reservation, y.reste_a_livre, a.categorie, d.designation as libelle, (y.prix * y.stock) AS valeur, ad.requiere_lot "
                + "from public.com_inventaire(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) y INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_depots d ON y.depot = d.id LEFT JOIN yvs_base_article_depot ad ON ad.article = y.article AND ad.depot = y.depot ";
        if (multi) {
            query += "order by y.designation, d.designation";
        } else {
            query += "order by y.depot, y.famille, y.code";
        }
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            totaux = 0;
            inventaires.clear();
            lignes.clear();
            elements.clear();
            periodes.clear();
            colonnes.clear();
            valeurs.clear();
            Object[] o;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    int i = 0;
                    Long _depot_ = (Long) o[i++];
                    Long _article_ = (Long) o[i++];
                    String _code_ = (String) o[i++];
                    String _designation_ = (String) o[i++];
                    String _numero_ = (String) o[i++];
                    String _famille_ = (String) o[i++];
                    Long _unite_ = (Long) o[i++];
                    String _reference_ = (String) o[i++];
                    Double _prix_ = (Double) o[i++];
                    Double _puv_ = (Double) o[i++];
                    Double _pua_ = (Double) o[i++];
                    Double _pr_ = (Double) o[i++];
                    Double _stock_ = (Double) o[i++];
                    Double _reservation_ = (Double) o[i++];
                    Double _reste_a_livre_ = (Double) o[i++];
                    String _categorie_ = (String) o[i++];
                    String _libelle_ = (String) o[i++];
                    Double _valeur_ = (Double) o[i++];
                    Boolean _requiere_lot_ = (Boolean) o[i++];

                    inventaires.add(new InventairePreparatoire(_depot_, _article_, _code_, _designation_, _categorie_, _numero_, _famille_, _unite_, _reference_, _libelle_, _prix_, _puv_, _pua_, _pr_, _stock_, _reservation_, _reste_a_livre_, _valeur_, (_requiere_lot_ != null ? _requiere_lot_ : false)));
                    totaux += (_valeur_ != null ? _valeur_ : 0);

                    if (!elements.contains(_unite_)) {
                        elements.add(_unite_);
                        lignes.add(new Rows(_unite_, _code_, _designation_, _reference_));
                    }
                    if (!periodes.contains(_libelle_)) {
                        periodes.add(_libelle_);
                        colonnes.add(new Columns(_libelle_, 0));
                    }
                    valeurs.add(new JournalVendeur(_unite_, _libelle_, _stock_, _valeur_));
                }
                Collections.sort(colonnes);
                saveRows();
                addTotaux(JournalVendeur.TYPE_VALEUR);
            }
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnYearClient(DaoInterfaceLocal dao) {
        returnYearClient(agence, client, dateDebut, dateFin, periode, dao);
    }

    public void returnYearClient(long agence, long client, Date dateDebut, Date dateFin, String periode, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(agence, 1), new Options(client, 2), new Options(dateDebut, 3), new Options(dateFin, 4), new Options(periode, 5)};
        String query = "select y.annee, y.jour, y.total, y.quantite, y.taux, y.rang, y.is_total, y.is_footer, y.code from public.et_progression_client(?,?,?,?,?) y order by y.is_total, y.is_footer, y.annee, y.rang";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            periodes.clear();
            colonnes.clear();
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    if (!elements.contains((Long) o[0])) {
                        elements.add((Long) o[0]);
                        lignes.add(new Rows((String) o[8], (String) o[8]));
                    }
                    if (!periodes.contains((String) o[1])) {
                        periodes.add((String) o[1]);
                        colonnes.add(new Columns(o[1], (int) o[5]));
                    }
                    valeurs.add(new JournalVendeur((Long) o[0], (String) o[1], (Double) o[2], (Double) o[3], (Double) o[4], (Integer) o[5], (Boolean) o[6], (Boolean) o[7]));
                }
            }
            totaux = 0;
            Collections.sort(colonnes);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnBAgeClient(DaoInterfaceLocal dao) {
        returnBAgeClient(societe, ecart, colonne, dateDebut, dateFin, dao);
    }

    public void returnBAgeClient(long societe, int ecart, int colonne, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(ecart, 2), new Options(colonne, 3), new Options(dateDebut, 4), new Options(dateFin, 5)};
        String query = "select y.client, y.jour, y.solde, y.rang, y.is_total, y.code, y.nom from public.et_balance_age_client(?,?,?,?,?) y order by y.is_total, y.nom, y.code, y.rang";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            periodes.clear();
            colonnes.clear();
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    if (!elements.contains((Long) o[0])) {
                        elements.add((Long) o[0]);
                        lignes.add(new Rows((String) o[5], (String) o[6]));
                    }
                    if (!periodes.contains((String) o[1])) {
                        periodes.add((String) o[1]);
                        colonnes.add(new Columns(o[1], (int) o[3]));
                    }
                    valeurs.add(new JournalVendeur((Long) o[0], (String) o[1], (Double) o[2], 0, 0, (Integer) o[3], (Boolean) o[4], false));
                }
            }
            totaux = 0;
            Collections.sort(colonnes);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnMasseSalarial(DaoInterfaceLocal dao) {
        returnMasseSalarial(societe, agences, ordres, dao);
    }

    public void returnMasseSalarial(long societe, String agences, String ordres, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(agences, 2), new Options(ordres, 3)};
        String query = "select coalesce(y.element,0), coalesce(y.code,''), coalesce(y.periode,0), coalesce(y.entete,''), coalesce(y.valeur,0), coalesce(y.rang,0) from public.grh_et_masse_salariale(?,?,?) y order by y.code, y.rang";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            periodes.clear();
            colonnes.clear();
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    if (!elements.contains((Long) o[0])) {
                        elements.add((Long) o[0]);
                        lignes.add(new Rows((String) o[1], (String) o[1]));
                    }
                    if (!periodes.contains((String) o[3])) {
                        periodes.add((String) o[3]);
                        colonnes.add(new Columns(o[3], (int) o[5]));
                    }
                    valeurs.add(new JournalVendeur((Long) o[0], (String) o[3], (Double) o[4], (Integer) o[5]));
                }
            }
            totaux = 0;
            Collections.sort(colonnes);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnObjectifsCommerciaux(DaoInterfaceLocal dao) {
        returnObjectifsCommerciaux(societe, periode, nature, dao);
    }

    public void returnObjectifsCommerciaux(long societe, String periode, String type, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        String query = "select y.indicateur, y.objectif, y.element, y.code, y.nom, y.periode, y.entete, y.attente, y.valeur, y.rang from public.com_et_objectif(?,?,?) y order by y.indicateur, y.code, y.objectif, y.rang";
        Options[] param = new Options[]{new Options(societe, 1), new Options(periode, 2), new Options(type, 3)};
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            sous.clear();
            Object[] line;

            Dashboards u;
            for (Object y : qr.getResultList()) {
                line = (Object[]) y;
                if (line != null ? line.length > 0 : false) {
                    String indicateur = (String) line[0];
                    int index = sous.indexOf(new Dashboards(indicateur));
                    if (index > -1) {
                        u = sous.get(index);
                    } else {
                        u = new Dashboards(indicateur);
                    }
                    if (!u.principaux.contains(indicateur)) {
                        u.principaux.add(indicateur);
                    }
                    Rows e = new Rows((String) line[3], (String) line[4]);// COmmerciale
                    String titre = (String) line[1];
                    if (!u.elements.contains((Long) line[2])) { // dans elements, on met l'Id du commerciale
                        u.elements.add((Long) line[2]);
                        if (titre != null ? titre.trim().length() > 0 : false) {
                            e.getTitres().add(titre);
                        }
                        u.lignes.add(e);    //ligne contient le code et nom du commerciale
                    } else {
                        if (titre != null ? titre.trim().length() > 0 : false) {
                            int idx = u.lignes.indexOf(e);
                            if (idx > -1) {
                                e = (Rows) u.lignes.get(idx);
                                if (!e.getTitres().contains(titre)) {
                                    e.getTitres().add(titre);
                                    u.lignes.set(idx, e);
                                }
                            }
                        }
                    }
                    if (!u.periodes.contains((String) line[6])) {
                        u.periodes.add((String) line[6]);
                        u.colonnes.add(new Columns(line[6], (Integer) line[9])); //colone contient: code période et la propriété rang
                    }
                    u.valeurs.add(new JournalVendeur((Long) line[2], (String) line[6], titre, indicateur, (Double) line[7], (Double) line[8], (Integer) line[9]));

                    if (index > -1) {
                        sous.set(index, u);
                    } else {
                        sous.add(u);
                    }
                }
            }
            for (Dashboards s : sous) {
                s.containsValue = !s.valeurs.isEmpty();
                s.totaux = 0;
                Collections.sort(s.colonnes);
            }
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnProgressionEmploye(DaoInterfaceLocal dao) {
        returnProgressionEmploye(societe, agence, service, employe, dateDebut, dateFin, periode, dao);
    }

    public void returnProgressionEmploye(long societe, long agence, long service, long employe, Date dateDebut, Date dateFin, String periode, DaoInterfaceLocal dao) {
        String query = "select y.employe, y.code, y.nom, y.periode, y.rang, y.salaire, y.presence, y.conge, y.permission, y.is_total, y.is_footer from public.grh_et_progression_all(?,?,?,?,?,?,?) y order by y.is_total, y.agence, y.nom, y.code, y.rang";
        returnProgressionEmploye(query, societe, agence, service, employe, dateDebut, dateFin, periode, dao);
    }

    public void returnProgressionEmploye(String query, DaoInterfaceLocal dao) {
        returnProgressionEmploye(query, societe, agence, service, employe, dateDebut, dateFin, periode, dao);
    }

    public void returnProgressionEmploye(String query, long societe, long agence, long service, long employe, Date dateDebut, Date dateFin, String periode, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(service, 3), new Options(employe, 4), new Options(dateDebut, 5), new Options(dateFin, 6), new Options(periode, 7)};
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            periodes.clear();
            colonnes.clear();
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    if (!elements.contains((Long) o[0])) {
                        elements.add((Long) o[0]);
                        lignes.add(new Rows((String) o[1], (String) o[2]));
                    }
                    if (!periodes.contains((String) o[3])) {
                        periodes.add((String) o[3]);
                        colonnes.add(new Columns(o[3], (int) o[4]));
                    }
                    valeurs.add(new JournalVendeur((Long) o[0], (String) o[3], (Double) o[5], (Double) o[6], (Double) o[7], (Double) o[8], (Integer) o[4], (Boolean) o[9], (Boolean) o[10]));
                }
            }
            totaux = 0;
            Collections.sort(colonnes);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnFraisMission(DaoInterfaceLocal dao) {
        returnFraisMission(societe, agence, dateDebut, dateFin, type, periode, dao);
    }

    public void returnFraisMission(long societe, long agence, Date dateDebut, Date dateFin, String type, String periode, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        String query = "select y.id, y.element, y.entete, y.valeur, y.rang from public.grh_et_frais_mission(?,?,?,?,?,?) y order by y.element, y.rang";
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(dateDebut, 3), new Options(dateFin, 4), new Options(type, 5), new Options(periode, 6)};
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            periodes.clear();
            colonnes.clear();
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    if (!elements.contains((Long) o[0])) {
                        elements.add((Long) o[0]);
                        lignes.add(new Rows((String) o[1], (String) o[1]));
                    }
                    if (!periodes.contains((String) o[2])) {
                        periodes.add((String) o[2]);
                        colonnes.add(new Columns(o[2], (int) o[4]));
                    }
                    valeurs.add(new JournalVendeur((Long) o[0], (String) o[2], (Double) o[3], (Integer) o[4]));
                }
            }
            containsValue = !valeurs.isEmpty();
            totaux = 0;
            Collections.sort(colonnes);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnBalance(DaoInterfaceLocal dao) {
        returnBalance(societe, agence, comptes, dateDebut, dateFin, journal, nature, ordres, dao);
    }

    public void returnBalance(long societe, long agence, String comptes, Date dateDebut, Date dateFin, long journal, String type, String nature, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        String query = "SELECT numero as id, code as numero, comptes.intitule as libelle, general.num_compte as general, general.intitule as libelle_general, debit_initial, credit_initial, "
                + "debit_periode, credit_periode, debit_solde_periode, credit_solde_periode, debit_solde_cumul, credit_solde_cumul, is_general FROM public.compta_et_balance(?, ?, ?, ?, ?, ?, ?, ?) y "
                + "INNER JOIN public.yvs_base_plan_comptable comptes on comptes.id = y.numero LEFT JOIN public.yvs_base_plan_comptable general on general.id = comptes.compte_general WHERE comptes.id IS NOT NULL ORDER BY y.general, comptes.num_compte";
        if (type.equals("T")) {
            query = "SELECT numero as id, code as numero, name_tiers(numero, table_tiers, 'N') as libelle, general.num_compte as general, general.intitule as libelle_general, debit_initial, credit_initial, "
                    + "debit_periode, credit_periode, debit_solde_periode, credit_solde_periode, debit_solde_cumul, credit_solde_cumul, is_general FROM public.compta_et_balance(?, ?, ?, ?, ?, ?, ?, ?) "
                    + "LEFT JOIN public.yvs_base_tiers comptes on comptes.id = numero LEFT JOIN public.yvs_base_plan_comptable general on general.id = comptes.compte_collectif WHERE numero IS NOT NULL";
            query += "  ORDER BY code";
        } else if (type.equals("A")) {
            query = "SELECT numero as id, code as numero, comptes.designation as libelle, null as general, null as libelle_general, debit_initial, credit_initial, "
                    + "debit_periode, credit_periode, debit_solde_periode, credit_solde_periode, debit_solde_cumul, credit_solde_cumul, is_general FROM public.compta_et_balance(?, ?, ?, ?, ?, ?, ?, ?) y "
                    + "INNER JOIN public.yvs_compta_centre_analytique comptes on comptes.id = y.numero WHERE comptes.id IS NOT NULL ORDER BY comptes.code_ref";
        }
        Options[] param = new Options[]{new Options(agence, 1), new Options(societe, 2), new Options(comptes, 3), new Options(dateDebut, 4), new Options(dateFin, 5), new Options(journal, 6), new Options(type, 7), new Options(nature, 8)};
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    String numero = (String) o[1];
                    String libelle = (String) o[2];
                    String numeroGeneral = (String) o[3] != null ? (String) o[3] : numero;
                    String libelleGeneral = (String) o[4] != null ? (String) o[4] : libelle;
                    if (!type.equals("C")) {
                        numeroGeneral = "";
                        libelleGeneral = "";
                    }
                    Double di = o[5] != null ? (Double) o[5] : 0;
                    Double cs = o[6] != null ? (Double) o[6] : 0;
                    Double dp = o[7] != null ? (Double) o[7] : 0;
                    Double cp = o[8] != null ? (Double) o[8] : 0;
                    Double dsp = o[9] != null ? (Double) o[9] : 0;
                    Double csp = o[10] != null ? (Double) o[10] : 0;
                    Double dsc = o[11] != null ? (Double) o[11] : 0;
                    Double csc = o[12] != null ? (Double) o[12] : 0;
                    Boolean general = (Boolean) o[13];
                    valeurs.add(new JournalVendeur(numero, libelle, numeroGeneral, libelleGeneral, cs, di, cp, dp, csp, dsp, csc, dsc, general));
                }
            }
            containsValue = !valeurs.isEmpty();
            totaux = 0;
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnDashboard(DaoInterfaceLocal dao) {
        returnDashboard(societe, agence, compteDebut, compteFin, dateDebut, dateFin, nature, periode, dao);
    }

    public void returnDashboard(long societe, long agence, String compteDebut, String compteFin, Date dateDebut, Date dateFin, String type, String periode, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
//        dao.requeteLibre("VACUUM FULL FREEZE VERBOSE ANALYSE yvs_compta_content_journal", new Options[]{});
        String query = "SELECT numero, entete, valeur as intitule, montant, num_classe, is_head, is_produit, is_charge, is_total FROM public.compta_et_dashboard(?, ?, ?, ?, ?, ?, ?, ?)  ORDER BY num_classe, numero";
        Options[] param = new Options[]{new Options(agence, 1), new Options(societe, 2), new Options(compteDebut, 3), new Options(compteFin, 4), new Options(dateDebut, 5), new Options(dateFin, 6), new Options(type, 7), new Options(periode, 8)};
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            periodes.clear();
            colonnes.clear();
            valeurs.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    String numero = (String) o[0];
                    String entete = (String) o[1];
                    String intitule = (String) o[2];
                    Double montant = o[3] != null ? (Double) o[3] : 0;
                    String classe = (String) o[4];
                    Boolean is_head = o[5] != null ? (Boolean) o[5] : false;
                    Boolean is_produit = o[6] != null ? (Boolean) o[6] : false;
                    Boolean is_charge = o[7] != null ? (Boolean) o[7] : false;
                    Boolean is_total = o[8] != null ? (Boolean) o[8] : false;

                    long id = numero.hashCode() + intitule.hashCode();
                    if (!elements.contains(id)) {
                        elements.add(id);
                        lignes.add(new Rows(numero, intitule));
                    }
                    if (!periodes.contains(entete)) {
                        periodes.add(entete);
                        colonnes.add(new Columns(entete, 0));
                    }
                    valeurs.add(new JournalVendeur(id, numero, entete, intitule, montant, ""/*/classe/*/, is_head, is_produit, is_charge, is_total));
                }
            }
            containsValue = !valeurs.isEmpty();
            totaux = 0;
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadJournauxVentes(DaoInterfaceLocal dao) {
        loadJournauxVentes(societe, agence, dateDebut, dateFin, byValue, dao);
    }

    private void loadJournauxVentes(long societe, long agence, Date dateDebut, Date dateFin, boolean byFamille, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(dateDebut, 3), new Options(dateFin, 4), new Options(byFamille, 5)};
        String query = "select y.users, y.code, y.nom, y.classe, y.montant, y.rang, y.is_vendeur, y.is_classe, y.reference, y.designation from public.com_et_journal_vente(?,?,?,?,?) y";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();
            Object[] o;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long users = (Long) o[0];
                    String code = (String) o[1];
                    String nom = (String) o[2];
                    Long classe = (Long) o[3];
                    Double montant = (Double) o[4];
                    Integer rang = (Integer) o[5];
                    Boolean is_vendeur = (Boolean) o[6];
                    Boolean is_classe = (Boolean) o[7];
                    String reference = (String) o[8];
                    String designation = (String) o[9];
                    if (!elements.contains(users)) { //fabrique les lignes
                        elements.add(users);
                        lignes.add(new Rows(code, nom));
                    }
                    if (!periodes.contains(reference)) {
                        periodes.add(reference);
                        colonnes.add(new Columns(reference, designation, rang));
                    }
                    valeurs.add(new JournalVendeur((users != null ? users : 0), reference, designation, (montant != null ? montant : 0), (rang != null ? rang : 0)));
                }
            }
            Collections.sort(colonnes);
            saveRows();
            addTotaux(JournalVendeur.TYPE_VALEUR);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadJournauxAchats(DaoInterfaceLocal dao) {
        loadJournauxAchats(societe, agence, dateDebut, dateFin, periode, nature, ordres, dao);
    }

    private void loadJournauxAchats(long societe, long agence, Date dateDebut, Date dateFin, String periode, String type, String groupe, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(dateDebut, 3), new Options(dateFin, 4), new Options(type, 5), new Options(periode, 6), new Options(groupe, 7)};
        String query = "select y.id, y.code, y.nom, y.classe, y.montant, y.rang, y.is_vendeur, y.is_classe, y.reference, y.designation from public.com_et_journal_achat(?,?,?,?,?,?,?) y ORDER BY y.is_classe DESC, y.is_vendeur DESC, y.code , y.reference";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();
            Object[] o;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long users = (Long) o[0];
                    String code = (String) o[1];
                    String nom = (String) o[2];
                    Long classe = (Long) o[3];
                    Double montant = (Double) o[4];
                    Integer rang = (Integer) o[5];
                    Boolean is_vendeur = (Boolean) o[6];
                    Boolean is_classe = (Boolean) o[7];
                    String reference = (String) o[8];
                    String designation = (String) o[9];
                    if (!elements.contains(users)) { //fabrique les lignes
                        elements.add(users);
                        lignes.add(new Rows(code, nom));
                    }
                    if (!periodes.contains(reference)) {
                        periodes.add(reference);
                        colonnes.add(new Columns(reference, designation, rang));
                    }
                    valeurs.add(new JournalVendeur((users != null ? users : 0), reference, designation, (montant != null ? montant : 0), (rang != null ? rang : 0)));
                }
            }
            Collections.sort(colonnes);
            saveRows();
            addTotaux(JournalVendeur.TYPE_VALEUR);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadCaPeriodes(DaoInterfaceLocal dao) {
        loadCaPeriodes(societe, agence, comptes, dateDebut, dateFin, periode, nature, dao);
    }

    private void loadCaPeriodes(long societe, long agence, String comptes, Date dateDebut, Date dateFin, String periode, String nature, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(comptes, 3), new Options(dateDebut, 4), new Options(dateFin, 5), new Options(periode, 6), new Options(nature, 7)};
        String query = "select y.id, y.code, y.intitule, y.periode, y.montant, y.rang, y.date_debut, y.date_fin from public.com_et_ca_by_periode(?,?,?,?,?,?,?) y";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();

            Object[] line;
            Long id;
            String code;
            String intitule;
            String period;
            Double montant;
            Integer rang;
            Date date_debut;
            Date date_fin;
            for (Object y : qr.getResultList()) {
                line = (Object[]) y;
                if (line != null ? line.length > 0 : false) {
                    id = (Long) line[0];
                    code = (String) line[1];
                    intitule = (String) line[2];
                    period = (String) line[3];
                    montant = (Double) line[4];
                    rang = (Integer) line[5];
                    date_debut = (Date) line[6];
                    date_fin = (Date) line[7];
                    if (!elements.contains(id)) { //fabrique les lignes
                        elements.add(id);
                        lignes.add(new Rows(id, code, intitule));
                    }
                    if (!periodes.contains(period)) {
                        periodes.add(period);
                        colonnes.add(new Columns(period, rang));
                    }
                    valeurs.add(new JournalVendeur((id != null ? id : 0), period, (montant != null ? montant : 0), (rang != null ? rang : 0)));
                }
            }
            Collections.sort(colonnes);
            saveRows();
            addTotaux(JournalVendeur.TYPE_VALEUR);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadCaAchatPeriodes(DaoInterfaceLocal dao) {
        loadCaAchatPeriodes(societe, agence, comptes, dateDebut, dateFin, periode, nature, dao);
    }

    private void loadCaAchatPeriodes(long societe, long agence, String comptes, Date dateDebut, Date dateFin, String periode, String nature, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(comptes, 3), new Options(dateDebut, 4), new Options(dateFin, 5), new Options(periode, 6), new Options(nature, 7)};
        String query = "select y.id, y.code, y.intitule, y.periode, y.montant, y.rang, y.date_debut, y.date_fin from public.com_et_ca_achat_by_periode(?,?,?,?,?,?,?) y";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();

            Object[] line;
            Long id;
            String code;
            String intitule;
            String period;
            Double montant;
            Integer rang;
            Date date_debut;
            Date date_fin;
            for (Object y : qr.getResultList()) {
                line = (Object[]) y;
                if (line != null ? line.length > 0 : false) {
                    id = (Long) line[0];
                    code = (String) line[1];
                    intitule = (String) line[2];
                    period = (String) line[3];
                    montant = (Double) line[4];
                    rang = (Integer) line[5];
                    date_debut = (Date) line[6];
                    date_fin = (Date) line[7];
                    if (!elements.contains(id)) { //fabrique les lignes
                        elements.add(id);
                        lignes.add(new Rows(id, code, intitule));
                    }
                    if (!periodes.contains(period)) {
                        periodes.add(period);
                        colonnes.add(new Columns(period, rang));
                    }
                    valeurs.add(new JournalVendeur((id != null ? id : 0), period, (montant != null ? montant : 0), (rang != null ? rang : 0)));
                }
            }
            Collections.sort(colonnes);
            saveRows();
            addTotaux(JournalVendeur.TYPE_VALEUR);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadEcartsVentes(DaoInterfaceLocal dao) {
        loadEcartsVentes(societe, agence, (byValue ? (point > 0 ? point : "") : (vendeur > 0 ? vendeur : "")) + "", dateDebut, dateFin, periode, byValue, dao);
    }

    private void loadEcartsVentes(long societe, long agence, String element, Date dateDebut, Date dateFin, String periode, boolean byPoint, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(element, 3), new Options(dateDebut, 4), new Options(dateFin, 5), new Options(periode, 6), new Options(byPoint, 7)};
        String query = "select y.element, y.code, y.nom, y.entete, y.versement_attendu, y.versement_reel, y.manquant, y.excedent, y.solde, y.rang, y.solde_initial, y.manquant_planifier, y.manquant_traite, y.manquant_hors_limit from public.com_et_ecart_vente(?,?,?,?,?,?,?) y";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            valeurs.clear();
            Object[] o;
            JournalVendeur row = null;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                int i = 0;
                if (o != null ? o.length > 0 : false) {
                    Long id = (Long) o[i++];
                    String code = (String) o[i++];
                    String nom = (String) o[i++];
                    String entete = (String) o[i++];
                    Double versement_attendu = (Double) o[i++];
                    Double versement_reel = (Double) o[i++];
                    Double manquant = (Double) o[i++];
                    Double excedent = (Double) o[i++];
                    Double solde = (Double) o[i++];
                    Integer rang = (Integer) o[i++];
                    Boolean solde_initial = (Boolean) o[i++];
                    Double manquant_planifier = (Double) o[i++];
                    Double manquant_traite = (Double) o[i++];
                    Boolean manquant_hors_limit = (Boolean) o[i++];

                    row = new JournalVendeur(id, code, nom, manquant_hors_limit);
                    int idx = valeurs.indexOf(row);
                    if (idx > -1) {
                        row = valeurs.get(idx);
                    }
                    if (manquant_planifier == -1) {
                        row.getFooters().add(new JournalVendeur((id != null ? id : 0), entete, versement_attendu, versement_reel, manquant, excedent, solde, (solde_initial != null ? solde_initial : false), manquant_planifier, manquant_traite, manquant_hors_limit));
                        if (idx > -1) {
                            valeurs.set(idx, row);
                        } else {
                            valeurs.add(row);
                        }
                    } else {
                        row.getSous().add(new JournalVendeur((id != null ? id : 0), entete, versement_attendu, versement_reel, manquant, excedent, solde, (solde_initial != null ? solde_initial : false), manquant_planifier, manquant_traite, manquant_hors_limit));
                        if (idx > -1) {
                            valeurs.set(idx, row);
                        } else {
                            valeurs.add(row);
                        }
                    }
                }
            }
            Collections.sort(colonnes);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadRistourneVentes(DaoInterfaceLocal dao) {
        loadRistourneVentes(societe, agence, zone, vendeur, client, dateDebut, dateFin, periode, cumulBy, dao);
    }

    private void loadRistourneVentes(long societe, long agence, long zone, long vendeur, long client, Date dateDebut, Date dateFin, String periode, int cumule, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(zone, 3), new Options(vendeur, 4), new Options(client, 5), new Options(dateDebut, 6), new Options(dateFin, 7), new Options(periode, 8), new Options(cumule, 9)};
        String query;
        if (cumule != 2) {
            query = "select y.client, y.code, y.nom, code_users, nom_users, y.entete, y.quantite, y.prix_total, y.ristourne, y.rang, a.ref_art, a.designation, u.reference, u.libelle, y.id_facture, y.numero_facture "
                    + "from public.com_et_ristourne_vente(?,?,?,?,?,?,?,?,?) y INNER JOIN yvs_base_conditionnement c ON y.unite = c.id INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id";
        } else {
            query = "select y.client, y.code, y.nom, code_users, nom_users, y.entete, y.quantite, y.prix_total, y.ristourne, y.rang "
                    + "from public.com_et_ristourne_vente(?,?,?,?,?,?,?,?,?) y ";
        }
        if (cumule == 4) {
            query += " ORDER BY y.numero_facture, y.nom, y.rang";
        }
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            valeurs.clear();
            Object[] o;
            String reference;
            String designation = null;
            String unite = null;
            String libelle;
            String code;
            Long id_facture = null;
            String numero_facture = null;
            JournalVendeur row;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long id = (Long) o[0];      //id client
                    code = (String) o[1]; //code client
                    String nom = (String) o[2]; // nom client
                    String code_users = (String) o[3];
                    String nom_users = (String) o[4];
                    String entete = (String) o[5];
                    Double quantite = (Double) o[6];
                    Double prix_total = (Double) o[7];
                    Double ristourne = (Double) o[8];
                    Integer rang = (Integer) o[9];
                    if (cumule != 2) {
                        reference = (String) o[10];
                        designation = (String) o[11];
                        unite = (String) o[12];
                        libelle = (String) o[13];
                        id_facture = (Long) o[14];
                        numero_facture = (String) o[15];
                    }
                    id_facture = id_facture != null ? id_facture : id;
                    numero_facture = numero_facture != null ? numero_facture : code;
                    if (cumule == 4) {
                        row = new JournalVendeur(id_facture, numero_facture, nom);
                    } else {
                        row = new JournalVendeur(id, code, nom);
                    }
                    int idx = valeurs.indexOf(row);
                    if (idx > -1) {
                        row = valeurs.get(idx);
                    }
                    row.getSous().add(new JournalVendeur((id != null ? id : 0), entete, designation, unite, code_users, quantite, prix_total, ristourne, numero_facture));
                    if (idx > -1) {
                        valeurs.set(idx, row);
                    } else {
                        valeurs.add(row);
                    }
                }
            }
            Collections.sort(colonnes);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadBonusVentes(DaoInterfaceLocal dao) {
        loadBonusVentes(societe, agence, zone, vendeur, client, dateDebut, dateFin, periode, cumulBy, dao);
    }

    private void loadBonusVentes(long societe, long agence, long zone, long vendeur, long client, Date dateDebut, Date dateFin, String periode, int cumule, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(zone, 3), new Options(vendeur, 4), new Options(client, 5), new Options(dateDebut, 6), new Options(dateFin, 7), new Options(periode, 8), new Options(cumule, 9)};
        String query;
        if (cumule != 2) {
            query = "select y.client, y.code, y.nom, code_users, nom_users, y.entete, y.quantite, y.prix_total, y.bonus, y.rang, a.ref_art, a.designation, u.reference, u.libelle, y.id_facture, y.numero_facture "
                    + "from public.com_et_bonus_vente(?,?,?,?,?,?,?,?,?) y INNER JOIN yvs_base_conditionnement c ON y.unite = c.id INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id";
        } else {
            query = "select y.client, y.code, y.nom, code_users, nom_users, y.entete, y.quantite, y.prix_total, y.bonus, y.rang "
                    + "from public.com_et_bonus_vente(?,?,?,?,?,?,?,?,?) y ";
        }
        if (cumule == 4) {
            query += " ORDER BY y.numero_facture, y.nom, y.rang";
        }
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            valeurs.clear();
            Object[] o;
            String reference;
            String designation = null;
            String unite = null;
            String libelle;
            String code;
            Long id_facture = null;
            String numero_facture = null;
            JournalVendeur row;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long id = (Long) o[0];      //id client
                    code = (String) o[1]; //code client
                    String nom = (String) o[2]; // nom client
                    String code_users = (String) o[3];
                    String nom_users = (String) o[4];
                    String entete = (String) o[5];
                    Double quantite = (Double) o[6];
                    Double prix_total = (Double) o[7];
                    Double bonus = (Double) o[8];
                    Integer rang = (Integer) o[9];
                    if (cumule != 2) {
                        reference = (String) o[10];
                        designation = (String) o[11];
                        unite = (String) o[12];
                        libelle = (String) o[13];
                        id_facture = (Long) o[14];
                        numero_facture = (String) o[15];
                    }
                    id_facture = id_facture != null ? id_facture : id;
                    numero_facture = numero_facture != null ? numero_facture : code;
                    if (cumule == 4) {
                        row = new JournalVendeur(id_facture, numero_facture, nom);
                    } else {
                        row = new JournalVendeur(id, code, nom);
                    }
                    int idx = valeurs.indexOf(row);
                    if (idx > -1) {
                        row = valeurs.get(idx);
                    }
                    row.getSous().add(new JournalVendeur((id != null ? id : 0), entete, designation, unite, code_users, quantite, prix_total, bonus, numero_facture));
                    if (idx > -1) {
                        valeurs.set(idx, row);
                    } else {
                        valeurs.add(row);
                    }
                }
            }
            Collections.sort(colonnes);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadListingVenteByClient(DaoInterfaceLocal dao) {
        loadListingVenteByClient(societe, agence, zone, vendeur, client, dateDebut, dateFin, periode, cumulBy, vueType, dao);
    }

    private void loadListingVenteByClient(long societe, long agence, long zone, long vendeur, long client, Date dateDebut, Date dateFin, String periode, int cumule, int vueType, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(zone, 3), new Options(vendeur, 4), new Options(client, 5), new Options(dateDebut, 6), new Options(dateFin, 7), new Options(periode, 8), new Options(cumule, 9)};
        String query;
        if (cumule != 2) {
            query = "select y.client, y.code, y.nom, code_users, nom_users, y.entete, y.quantite, y.prix_total, y.ristourne, y.rang, y.unite, a.ref_art, a.designation, u.reference, u.libelle "
                    + "from public.com_et_listing_vente_by_client(?,?,?,?,?,?,?,?,?) y INNER JOIN yvs_base_conditionnement c ON y.unite = c.id INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id ORDER BY code, rang";
        } else {
            query = "select y.client, y.code, y.nom, code_users, nom_users, y.entete, y.quantite, y.prix_total, y.ristourne, y.rang, y.unite "
                    + "from public.com_et_listing_vente_by_client(?,?,?,?,?,?,?,?,?) y ";
        }
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();

            Object[] o;
            String reference = null;
            String designation = null;
            String unite = null;
            String libelle = null;
            JournalVendeur row;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long id = (Long) o[0];      //id client
                    String code = (String) o[1]; //code client
                    String nom = (String) o[2]; // nom client
                    String code_users = (String) o[3];
                    String nom_users = (String) o[4];
                    String entete = (String) o[5];
                    Double quantite = (Double) o[6];
                    Double prix_total = (Double) o[7];
                    Double ristourne = (Double) o[8];
                    Integer rang = (Integer) o[9];
                    Long mesure = (Long) o[10];
                    if (cumule != 2) {
                        reference = (String) o[11];
                        designation = (String) o[12];
                        unite = (String) o[13];
                        libelle = (String) o[14];
                    }
                    if (cumule != 1) {
                        vueType = 0;
                    }
                    if (vueType == 2) {
                        String period = reference + " (" + unite + ")";
                        if (!elements.contains(id)) { //fabrique les lignes
                            elements.add(id);
                            lignes.add(new Rows(code, nom));
                        }
                        if (!periodes.contains(period)) {
                            periodes.add(period);
                            colonnes.add(new Columns(period, designation, rang));
                        }
                        valeurs.add(new JournalVendeur((id != null ? id : 0), period, (prix_total != null ? prix_total : 0), (quantite != null ? quantite : 0), (rang != null ? rang : 0)));
                    } else if (vueType == 1) {
                        row = new JournalVendeur(id, code, nom);
                        int idx = valeurs.indexOf(row);
                        if (idx > -1) {
                            row = valeurs.get(idx);
                        }

                        String refart = reference + " (" + unite + ")";
                        if (!row.getElements().contains(mesure)) { //fabrique les lignes
                            row.getElements().add(mesure);
                            row.getLignes().add(new Rows(mesure, refart, designation));
                        }
                        if (!periodes.contains(entete)) {
                            periodes.add(entete);
                            colonnes.add(new Columns(entete, rang));
                        }
                        row.getSous().add(new JournalVendeur((mesure != null ? mesure : 0), entete, +(prix_total != null ? prix_total : 0), (quantite != null ? quantite : 0), (rang != null ? rang : 0)));
                        if (idx > -1) {
                            valeurs.set(idx, row);
                        } else {
                            valeurs.add(row);
                        }
                    } else {
                        row = new JournalVendeur(id, code, nom);
                        int idx = valeurs.indexOf(row);
                        if (idx > -1) {
                            row = valeurs.get(idx);
                        }
                        row.getSous().add(new JournalVendeur((id != null ? id : 0), entete, designation, unite, code_users, quantite, prix_total, ristourne));
                        if (idx > -1) {
                            valeurs.set(idx, row);
                        } else {
                            valeurs.add(row);
                        }
                    }
                }
            }
            Collections.sort(colonnes);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadListingAchatByFournisseur(DaoInterfaceLocal dao) {
        loadListingAchatByFournisseur(societe, agence, fournisseur, dateDebut, dateFin, periode, cumulBy, vueType, dao);
    }

    private void loadListingAchatByFournisseur(long societe, long agence, long fournisseur, Date dateDebut, Date dateFin, String periode, int cumule, int vueType, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(fournisseur, 3), new Options(dateDebut, 4), new Options(dateFin, 5), new Options(periode, 6), new Options(cumule, 7)};
        String query;
        if (cumule != 2) {
            query = "select y.fournisseur, y.code, y.nom, y.abbreviation, y.designation, y.entete, y.quantite, y.prix_total, y.rang, y.unite, a.ref_art, a.designation, u.reference, u.libelle "
                    + "from public.com_et_listing_achat_by_fournisseur(?,?,?,?,?,?,?) y INNER JOIN yvs_base_conditionnement c ON y.unite = c.id INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id ORDER BY code, rang";
        } else {
            query = "select y.fournisseur, y.code, y.nom, y.abbreviation, y.designation, y.entete, y.quantite, y.prix_total, y.rang, y.unite "
                    + "from public.com_et_listing_achat_by_fournisseur(?,?,?,?,?,?,?) y ";
        }
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();

            Object[] o;
            String reference = null;
            String designation = null;
            String unite = null;
            String libelle = null;
            JournalVendeur row;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long id = (Long) o[0];      //id client
                    String code = (String) o[1]; //code client
                    String nom = (String) o[2]; // nom client
                    String abbreviation = (String) o[3];
                    String label = (String) o[4];
                    String entete = (String) o[5];
                    Double quantite = (Double) o[6];
                    Double prix_total = (Double) o[7];
                    Double ristourne = 0D;
                    Integer rang = (Integer) o[8];
                    Long mesure = (Long) o[9];
                    if (cumule != 2) {
                        reference = (String) o[10];
                        designation = (String) o[11];
                        unite = (String) o[12];
                        libelle = (String) o[13];
                    }
                    if (cumule != 1) {
                        vueType = 0;
                    }
                    if (vueType == 2) {
                        String period = reference + " (" + unite + ")";
                        if (!elements.contains(id)) { //fabrique les lignes
                            elements.add(id);
                            lignes.add(new Rows(code, nom));
                        }
                        if (!periodes.contains(period)) {
                            periodes.add(period);
                            colonnes.add(new Columns(period, designation, rang));
                        }
                        valeurs.add(new JournalVendeur((id != null ? id : 0), period, (prix_total != null ? prix_total : 0), (quantite != null ? quantite : 0), (rang != null ? rang : 0)));
                    } else if (vueType == 1) {
                        row = new JournalVendeur(id, code, nom);
                        int idx = valeurs.indexOf(row);
                        if (idx > -1) {
                            row = valeurs.get(idx);
                        }

                        String refart = reference + " (" + unite + ")";
                        if (!row.getElements().contains(mesure)) { //fabrique les lignes
                            row.getElements().add(mesure);
                            row.getLignes().add(new Rows(mesure, refart, designation));
                        }
                        if (!periodes.contains(entete)) {
                            periodes.add(entete);
                            colonnes.add(new Columns(entete, rang));
                        }
                        row.getSous().add(new JournalVendeur((mesure != null ? mesure : 0), entete, +(prix_total != null ? prix_total : 0), (quantite != null ? quantite : 0), (rang != null ? rang : 0)));
                        if (idx > -1) {
                            valeurs.set(idx, row);
                        } else {
                            valeurs.add(row);
                        }
                    } else {
                        row = new JournalVendeur(id, code, nom);
                        int idx = valeurs.indexOf(row);
                        if (idx > -1) {
                            row = valeurs.get(idx);
                        }
                        row.getSous().add(new JournalVendeur((id != null ? id : 0), entete, designation, unite, abbreviation, quantite, prix_total, ristourne));
                        if (idx > -1) {
                            valeurs.set(idx, row);
                        } else {
                            valeurs.add(row);
                        }
                    }
                }
            }
            Collections.sort(colonnes);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadGrandLivre(DaoInterfaceLocal dao) {
        loadGrandLivre(societe, agence, comptes, dateDebut, dateFin, journal, nature, ordres, lettrer, cumule, dao);
    }

    private void loadGrandLivre(long societe, long agence, String comptes, Date dateDebut, Date dateFin, long journal, String type, String nature, Boolean lettrer, boolean cumule, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(comptes, 3), new Options(dateDebut, 4), new Options(dateFin, 5), new Options(journal, 6), new Options(type, 7), new Options(lettrer, 8), new Options(cumule, 9), new Options(nature, 10)};
        String query = "select y.id, y.code, y.intitule, y.compte, y.designation, y.numero, y.date_piece, y.jour, y.libelle, y.lettrage, y.journal, y.debit, y.credit, y.compte_tiers, y.table_tiers, y.solde from public.compta_et_grand_livre(?,?,?,?,?,?,?,?::boolean,?::boolean,?) y WHERE COALESCE(y.id, 0) > 0";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            comptables.clear();
            Object[] o;
            double soldeProg = 0;
            double debitFinal = 0, creditFinal = 0;
            double debitPeriodFinal = 0, creditPeriodFinal = 0;
            double debitFinalCompte = 0, creditFinalCompte = 0;
            double debitPeriodFinalCompte = 0, creditPeriodFinalCompte = 0;
            ValeurComptable row, last = null;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long id = (Long) o[0];
                    String code = (String) o[1];
                    String intitule = (String) o[2];
                    String compte = (String) o[3];
                    String designation = (String) o[4];
                    String numero = (String) o[5];
                    Date datePiece = (Date) o[6];
                    Integer jour = (Integer) o[7];
                    String libelle = (String) o[8];
                    String lettrage = (String) o[9];
                    String codeJournal = (String) o[10];
                    Double debit = (Double) o[11];
                    Double credit = (Double) o[12];
                    Long compteTiers = (Long) o[13];
                    String tableTiers = (String) o[14];
                    Boolean solde = (Boolean) o[15];

                    row = new ValeurComptable(id, code, intitule);
                    if (last != null ? !last.equals(row) : false) {
                        last.getSous().add(new ValeurComptable(-2, last.getCode(), last.getIntitule(), "", "TOTAL PERIODIQUE", "TOTAL PERIODIQUE", null, 0, "TOTAL PERIODE GENERAL : " + fmdMy.format(dateDebut) + "-" + fmdMy.format(dateFin), "", "", debitPeriodFinalCompte, creditPeriodFinalCompte, 0, "", false, (debitPeriodFinalCompte - creditPeriodFinalCompte)));
                        last.getSous().add(new ValeurComptable(-3, last.getCode(), last.getIntitule(), "", "TOTAL", "TOTAL", null, 0, "TOTAL AU " + fmdMy.format(dateFin), "", "", debitFinalCompte, creditFinalCompte, 0, "", false, (debitFinalCompte - creditFinalCompte)));
                        double s = debitFinalCompte - creditFinalCompte;
                        last.getSous().add(new ValeurComptable(-3, last.getCode(), last.getIntitule(), "", "SOLDE FINAL", "SOLDE FINAL", null, 0, "SOLDE FINAL DU COMPTE " + code, "", "", (s > 0 ? s : 0), (s < 0 ? -(s) : 0), 0, "", false));
                        int idx = comptables.indexOf(last);
                        if (idx > -1) {
                            comptables.set(idx, last);
                        }
                        soldeProg = 0;
                        debitPeriodFinalCompte = 0;
                        creditPeriodFinalCompte = 0;
                        debitFinalCompte = 0;
                        creditFinalCompte = 0;
                    }
                    if (solde != null ? !solde : true) {
                        debitPeriodFinal += (debit != null ? debit : 0);
                        creditPeriodFinal += (credit != null ? credit : 0);
                        debitPeriodFinalCompte += (debit != null ? debit : 0);
                        creditPeriodFinalCompte += (credit != null ? credit : 0);
                    }
                    debitFinal += (debit != null ? debit : 0);
                    creditFinal += (credit != null ? credit : 0);
                    debitFinalCompte += (debit != null ? debit : 0);
                    creditFinalCompte += (credit != null ? credit : 0);
                    soldeProg += ((debit != null ? debit : 0) - (credit != null ? credit : 0));

                    int idx = comptables.indexOf(row);
                    if (idx > -1) {
                        row = comptables.get(idx);
                    }
                    row.getSous().add(new ValeurComptable((id != null ? id : -1L), code, intitule, compte, designation, numero, datePiece, jour != null ? jour : 0, libelle, lettrage, codeJournal, (debit != null ? debit : 0), (credit != null ? credit : 0), (compteTiers != null ? compteTiers : 0), tableTiers, (solde != null ? solde : false), soldeProg));
                    if (idx > -1) {
                        comptables.set(idx, row);
                    } else {
                        comptables.add(row);
                    }
                    last = row;
                }
            }
            last.getSous().add(new ValeurComptable(-2, last.getCode(), last.getIntitule(), "", "TOTAL PERIODIQUE", "TOTAL PERIODIQUE", null, 0, "TOTAL PERIODE GENERAL : " + fmdMy.format(dateDebut) + "-" + fmdMy.format(dateFin), "", "", debitPeriodFinalCompte, creditPeriodFinalCompte, 0, "", false, (debitPeriodFinalCompte - creditPeriodFinalCompte)));
            last.getSous().add(new ValeurComptable(-3, last.getCode(), last.getIntitule(), "", "TOTAL", "TOTAL", null, 0, "TOTAL AU " + fmdMy.format(dateFin), "", "", debitFinalCompte, creditFinalCompte, 0, "", false, (debitFinalCompte - creditFinalCompte)));
            double s = debitFinalCompte - creditFinalCompte;
            last.getSous().add(new ValeurComptable(-3, last.getCode(), last.getIntitule(), "", "SOLDE FINAL", "SOLDE FINAL", null, 0, "SOLDE FINAL DU COMPTE " + last.getCode(), "", "", (s > 0 ? s : 0), (s < 0 ? -(s) : 0), 0, "", false));
            int idx = comptables.indexOf(last);
            if (idx > -1) {
                comptables.set(idx, last);
            }

            totalPeriodique = new ValeurComptable(0, "", "", "", "TOTAL PERIODIQUE GENERAL", "TOTAL PERIODIQUE GENERAL", dateFin, 0, "TOTAL PERIODE GENERAL : " + fmdMy.format(dateDebut) + "-" + fmdMy.format(dateFin), "", "", debitPeriodFinal, creditPeriodFinal, 0, "", true);
            totalGeneral = new ValeurComptable(0, "", "", "", "TOTAL GENERAL", "TOTAL GENERAL", dateFin, 0, "TOTAL GENERAL AU " + fmdMy.format(dateFin), "", "", debitFinal, creditFinal, 0, "", true);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadJournal(DaoInterfaceLocal dao) {
        loadJournal(societe, agence, comptes, dateDebut, dateFin, nature, ordres, dao);
    }

    private void loadJournal(long societe, long agence, String comptes, Date dateDebut, Date dateFin, String type, String nature, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(comptes, 3), new Options(dateDebut, 4), new Options(dateFin, 5), new Options(type, 6)};
        String query = "select y.id, y.code, y.intitule, y.piece, y.reference, y.date_piece, y.compte, y.numero, y.designation, y.compte_tiers, y.nom_prenom, y.plan, y.code_plan, y.libelle, "
                + "y.jour, UPPER(y.description), y.lettrage, y.debit, y.credit, y.table_tiers, y.reference_externe from public.compta_et_journal(?,?,?,?,?,?) y";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            comptables.clear();
            Object[] o;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long id = (Long) o[0];
                    String code = (String) o[1];
                    String intitule = (String) o[2];
                    Long piece = (Long) o[3];
                    String reference = (String) o[4];
                    Date date_piece = (Date) o[5];
                    Long compte_general = (Long) o[6];
                    String numero = (String) o[7];
                    String designation = (String) o[8];
                    Long compte_tiers = (Long) o[9];
                    String nom_prenom = (String) o[10];
                    Long plan = (Long) o[11];
                    String code_plan = (String) o[12];
                    String libelle = (String) o[13];
                    Integer jour = (Integer) o[14];
                    String description = (String) o[15];
                    String lettrage = (String) o[16];
                    Double debit = (Double) o[17];
                    Double credit = (Double) o[18];
                    String tableTiers = (String) o[19];
                    String reference_externe = (String) o[20];

                    id = id != null ? id : 0;
                    piece = piece != null ? piece : 0;
                    compte_general = compte_general != null ? compte_general : 0;
                    compte_tiers = compte_tiers != null ? compte_tiers : 0;
                    plan = plan != null ? plan : 0;
                    jour = jour != null ? jour : 0;
                    debit = debit != null ? debit : 0;
                    credit = credit != null ? credit : 0;

                    ValeurComptable row = new ValeurComptable(piece, Constantes.dfN.format(date_piece), reference);
                    int idx = comptables.indexOf(row);
                    if (idx > -1) {
                        row = comptables.get(idx);
                    }

                    row.getSous().add(new ValeurComptable(id, code, intitule, piece, reference, date_piece, compte_general, numero, designation, compte_tiers, nom_prenom, plan, code_plan, libelle, jour, description, lettrage, debit, credit, tableTiers, reference_externe));
                    if (idx > -1) {
                        comptables.set(idx, row);
                    } else {
                        comptables.add(row);
                    }
                }
            }
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadEcartInventaire(DaoInterfaceLocal dao) {
        loadEcartInventaire(societe, agence, vendeur, nature, categorie, coefficient, dateDebut, dateFin, periode, cumule, dao);
    }

    private void loadEcartInventaire(long societe, long agence, long user, String nature, String groupe, double coefficient, Date dateDebut, Date dateFin, String periode, boolean cumule, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(user, 3), new Options(nature, 4), new Options(groupe, 5), new Options(coefficient, 6), new Options(dateDebut, 7), new Options(dateFin, 8), new Options(periode, 9), new Options(cumule, 10)};
        String query = "select y.users, y.code_users, y.nom_users, y.article, y.refart, y.designation, y.unite, y.reference, y.jour, y.quantite, y.pr, y.puv, y.total, y.rang from public.com_et_ecart_inventaire(?,?,?,?,?,?,?,?,?,?) y";
        if (cumule) {
            query += " order by y.rang, y.code_users, y.refart";
        } else {
            query += " order by y.code_users, y.refart, y.rang";
        }
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            titres.clear();
            groupes.clear();
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();

            Object[] o;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long users = (Long) o[0];
                    String code_users = (String) o[1];
                    String nom_users = (String) o[2];
                    Long article = (Long) o[3];
                    String refart = (String) o[4];
                    String designation = (String) o[5];
                    Long unite = (Long) o[6];
                    String reference = (String) o[7];
                    String jour = (String) o[8];
                    Double quantite = (Double) o[9];
                    Double pr = (Double) o[10];
                    Double puv = (Double) o[11];
                    Double total = (Double) o[12];
                    Integer rang = (Integer) o[13];

                    JournalVendeur row;
                    if (cumule) {
                        row = new JournalVendeur(0, jour);
                    } else {
                        row = new JournalVendeur(0, code_users, nom_users);
                    }
                    int idx = valeurs.indexOf(row);
                    if (idx > -1) {
                        row = valeurs.get(idx);
                    }
                    row.getSous().add(new JournalVendeur(code_users, nom_users, refart, designation, reference, quantite, pr, puv, total));
                    if (idx > -1) {
                        valeurs.set(idx, row);
                    } else {
                        valeurs.add(row);
                    }
                }
            }
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadValeurInventaire(String whatValeurDisplay, DaoInterfaceLocal dao) {
        loadValeurInventaire(societe, depot, editeurs, valoriseMs, valorisePf, valorisePsf, valoriseMp, coefficient, dateDebut, dateFin, valoriseExcedent, whatValeurDisplay, dao);
    }

    private void loadValeurInventaire(long societe, long depot, String editeurs, String valoriseMs, String valorisePf, String valorisePsf, String valoriseMp, double coefficient, Date dateDebut, Date dateFin, boolean valoriseExcedent, String whatValeurDisplay, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(depot, 2), new Options(editeurs, 3), new Options(valoriseMs, 4), new Options(valorisePf, 5), new Options(valorisePsf, 6), new Options(valoriseMp, 7), new Options(coefficient, 8), new Options(dateDebut, 9), new Options(dateFin, 10), new Options(valoriseExcedent, 11)};
        String query = "select y.users, y.code, y.nom, y.article, y.refart, y.designation, y.categorie, y.reffam, y.famille, y.unite, y.reference, y.quantite, y.prix, y.total, y.excedent, y.manquant, y.total_excedent, y.total_manquant from public.com_et_valorise_inventaire(?,?,?,?,?,?,?,?,?,?,?) y order by y.nom, y.designation";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            titres.clear();
            groupes.clear();
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();

            boolean displayManquant = whatValeurDisplay.equals("MANQUANT");
            boolean displayExcedent = whatValeurDisplay.equals("EXCEDENT");
            boolean displayManquantExcedent = whatValeurDisplay.equals("MANQUANT+EXCEDENT");

            Object[] o;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long _users = (Long) o[0];
                    String _code = (String) o[1];
                    String _nom = (String) o[2];
                    Long _article = (Long) o[3];
                    String _refart = (String) o[4];
                    String _designation = (String) o[5];
                    String _categorie = (String) o[6];
                    String _reffam = (String) o[7];
                    String _famille = (String) o[8];
                    Long _unite = (Long) o[9];
                    String _reference = (String) o[10];
                    Double _quantite = (Double) o[11];
                    Double _prix = (Double) o[12];
                    Double _total = (Double) o[13];
                    Double _excedent = (Double) o[14];
                    Double _manquant = (Double) o[15];
                    Double _total_excedent = (Double) o[16];
                    Double _total_manquant = (Double) o[17];

                    if (displayExcedent && _excedent <= 0) {
                        continue;
                    }
                    if (displayManquant && _manquant <= 0) {
                        continue;
                    }
                    if (displayManquantExcedent && _quantite <= 0) {
                        continue;
                    }

                    JournalVendeur row = new JournalVendeur(_users, _code, _nom);
                    int idx = valeurs.indexOf(row);
                    if (idx > -1) {
                        row = valeurs.get(idx);
                    }
                    //element, periode, secondaire, unite, principal, quantite, prixrevient, prixvente
                    row.getSous().add(new JournalVendeur(_users, _famille, _refart, _designation, _reference, _quantite, _prix, _total, _categorie, _excedent, _manquant, _total_excedent, _total_manquant));
                    if (idx > -1) {
                        valeurs.set(idx, row);
                    } else {
                        valeurs.add(row);
                    }
                }
            }
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadJournalProduction(DaoInterfaceLocal dao) {
        loadJournalProduction(societe, agence, depot, comptes, dateDebut, dateFin, categorie, cumulBy, valorise_by, nature, dao);
    }

    private void loadJournalProduction(long societe, long agence, long depot, String articles, Date dateDebut, Date dateFin, String categorie, int cumule, String valoriser, String type, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(depot, 3), new Options(articles, 4), new Options(dateDebut, 5), new Options(dateFin, 6), new Options(categorie, 7), new Options(cumule, 8), new Options(valoriser, 9), new Options(type, 10)};
        String query = "select y.* from public.prod_et_journal_production(?,?,?,?,?,?,?,?,?,?) y";
        if (cumule == 0) {
            query += " order by y.designation_mp, y.designation, y.reference";
        } else {
            query += " order by y.designation_mp, y.code_equipe, y.designation, y.reference";
        }
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();

            Object[] ligne;
            Long article;
            String code;
            String designation;
            Long unite;
            String reference;
            Double production;
            Long mp;
            String code_mp;
            String designation_mp;
            Long unite_mp;
            String reference_mp;
            Long equipe;
            String code_equipe;
            String nom_equipe;
            Double prix_vente;
            Double prix_achat;
            Double prix_revient;
            Double prix_prod;
            Double quantite;
            Double valeur;
            for (Object y : qr.getResultList()) {
                ligne = (Object[]) y;
                if (ligne != null ? ligne.length > 0 : false) {
                    int i = 0;
                    article = (Long) ligne[i++];
                    code = (String) ligne[i++];
                    designation = (String) ligne[i++];
                    unite = (Long) ligne[i++];
                    reference = (String) ligne[i++];
                    production = (Double) ligne[i++];
                    mp = (Long) ligne[i++];
                    code_mp = (String) ligne[i++];
                    designation_mp = (String) ligne[i++];
                    unite_mp = (Long) ligne[i++];
                    reference_mp = (String) ligne[i++];
                    equipe = (Long) ligne[i++];
                    code_equipe = (String) ligne[i++];
                    nom_equipe = (String) ligne[i++];
                    prix_vente = (Double) ligne[i++];
                    prix_achat = (Double) ligne[i++];
                    prix_revient = (Double) ligne[i++];
                    prix_prod = (Double) ligne[i++];
                    quantite = (Double) ligne[i++];
                    valeur = (Double) ligne[i++];
                    double prix = 0;
                    switch (valoriser) {
                        case "P":
                            prix = prix_prod != null ? prix_prod : 0;
                            break;
                        case "A":
                            prix = prix_achat != null ? prix_achat : 0;
                            break;
                        case "V":
                            prix = prix_vente != null ? prix_vente : 0;
                            break;
                        default:
                            prix = prix_revient != null ? prix_revient : 0;
                            break;
                    }

                    if (!periodes.contains(unite.toString())) {
                        periodes.add(unite.toString());
                        colonnes.add(new Columns(unite, code, designation, reference, production));
                    }
                    JournalVendeur value = new JournalVendeur(unite_mp, unite + "", (production != null ? production : 0), (prix_vente != null ? prix_vente : 0), (prix_achat != null ? prix_achat : 0), (prix_revient != null ? prix_revient : 0), (prix_prod != null ? prix_prod : 0), (quantite != null ? quantite : 0), (valeur != null ? valeur : 0));
                    if (!elements.contains(unite_mp)) {
                        elements.add(unite_mp);
                        lignes.add(new Rows(unite_mp, code_mp + " (" + reference_mp + ")", designation_mp + " (" + reference_mp + ")", 0, prix));
                    }
                    if (cumule == 0) {
                        valeurs.add(value);
                    } else {
                        JournalVendeur row = new JournalVendeur(equipe, code_equipe, nom_equipe);
                        int idx = valeurs.indexOf(row);
                        if (idx > -1) {
                            row = valeurs.get(idx);
                        }
                        if (!row.getElements().contains(unite_mp)) {
                            row.getElements().add(unite_mp);
                            row.getLignes().add(new Rows(unite_mp, code_mp + " (" + reference_mp + ")", designation_mp + " (" + reference_mp + ")", 0, prix));
                            row.getLignesFind().add(new Rows(unite_mp, code_mp + " (" + reference_mp + ")", designation_mp + " (" + reference_mp + ")", 0, prix));
                        }
                        row.getSous().add(value);
                        if (idx > -1) {
                            valeurs.set(idx, row);
                        } else {
                            valeurs.add(row);
                        }
                    }
                }
            }
            lignesFind = new ArrayList<>(lignes);
            elementsFinds = new ArrayList<>(elements);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadRecapitulatifOF(DaoInterfaceLocal dao) {
        loadRecapitulatifOF(societe, agence, site, article, dateDebut, dateFin, categorie, cumulBy, valorise_by, dao);
    }

    private void loadRecapitulatifOF(long societe, long agence, long site, long articles, Date dateDebut, Date dateFin, String categorie, int cumule, String valoriser, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(site, 3), new Options(articles, 4), new Options(dateDebut, 5), new Options(dateFin, 6), new Options(categorie, 7), new Options(cumule, 8), new Options(valoriser, 9)};
        String query = "select y.* from public.prod_et_recapitulatif_of(?,?,?,?,?,?,?,?,?) y";
        if (cumule == 0) {
            query += " order by y.numero, y.designation_mp, y.designation_pf, y.reference_pf";
        } else {
            query += " order by y.numero, y.designation_mp, y.code_equipe, y.designation_pf, y.reference_pf";
        }
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();

            Object[] ligne;
            Long ordre;
            String numero;
            Long article;
            String code;
            String designation;
            Long unite;
            String reference;
            Double production;
            Long mp;
            String code_mp;
            String designation_mp;
            Long unite_mp;
            String reference_mp;
            Long equipe;
            String code_equipe;
            String nom_equipe;
            Double prix_vente;
            Double prix_achat;
            Double prix_revient;
            Double prix_prod;
            Double quantite;
            Double valeur;
            for (Object y : qr.getResultList()) {
                ligne = (Object[]) y;
                if (ligne != null ? ligne.length > 0 : false) {
                    int i = 0;
                    ordre = (Long) ligne[i++];
                    numero = (String) ligne[i++];
                    article = (Long) ligne[i++];
                    code = (String) ligne[i++];
                    designation = (String) ligne[i++];
                    unite = (Long) ligne[i++];
                    reference = (String) ligne[i++];
                    production = (Double) ligne[i++];
                    mp = (Long) ligne[i++];
                    code_mp = (String) ligne[i++];
                    designation_mp = (String) ligne[i++];
                    unite_mp = (Long) ligne[i++];
                    reference_mp = (String) ligne[i++];
                    equipe = (Long) ligne[i++];
                    code_equipe = (String) ligne[i++];
                    nom_equipe = (String) ligne[i++];
                    prix_vente = (Double) ligne[i++];
                    prix_achat = (Double) ligne[i++];
                    prix_revient = (Double) ligne[i++];
                    prix_prod = (Double) ligne[i++];
                    quantite = (Double) ligne[i++];
                    valeur = (Double) ligne[i++];
                    double prix = 0;
                    switch (valoriser) {
                        case "P":
                            prix = prix_prod != null ? prix_prod : 0;
                            break;
                        case "A":
                            prix = prix_achat != null ? prix_achat : 0;
                            break;
                        case "V":
                            prix = prix_vente != null ? prix_vente : 0;
                            break;
                        default:
                            prix = prix_revient != null ? prix_revient : 0;
                            break;
                    }

                    if (!periodes.contains(numero)) {
                        periodes.add(numero);
                        colonnes.add(new Columns(numero, numero, designation + "(" + reference + ")", code + "(" + reference + ")", production));
                    }
                    JournalVendeur value = new JournalVendeur(unite_mp, numero, (production != null ? production : 0), (prix_vente != null ? prix_vente : 0), (prix_achat != null ? prix_achat : 0), (prix_revient != null ? prix_revient : 0), (prix_prod != null ? prix_prod : 0), (quantite != null ? quantite : 0), (valeur != null ? valeur : 0));
                    if (!elements.contains(unite_mp)) {
                        elements.add(unite_mp);
                        lignes.add(new Rows(unite_mp, code_mp + " (" + reference_mp + ")", designation_mp + " (" + reference_mp + ")", 0, prix));
                    }
                    if (cumule == 0) {
                        valeurs.add(value);
                    } else {
                        JournalVendeur row = new JournalVendeur(equipe, code_equipe, nom_equipe);
                        int idx = valeurs.indexOf(row);
                        if (idx > -1) {
                            row = valeurs.get(idx);
                        }
                        if (!row.getElements().contains(unite_mp)) {
                            row.getElements().add(unite_mp);
                            row.getLignes().add(new Rows(unite_mp, code_mp + " (" + reference_mp + ")", designation_mp + " (" + reference_mp + ")", 0, prix));
                            row.getLignesFind().add(new Rows(unite_mp, code_mp + " (" + reference_mp + ")", designation_mp + " (" + reference_mp + ")", 0, prix));
                        }
                        row.getSous().add(value);
                        if (idx > -1) {
                            valeurs.set(idx, row);
                        } else {
                            valeurs.add(row);
                        }
                    }
                }
            }
            lignesFind = new ArrayList<>(lignes);
            elementsFinds = new ArrayList<>(elements);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadProductionConsommation(DaoInterfaceLocal dao) {
        loadProductionConsommation(article, dateDebut, dateFin, periode, valorise_by, dao);
    }

    private void loadProductionConsommation(long articles, Date dateDebut, Date dateFin, String periode, String valoriser, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(articles, 1), new Options(dateDebut, 2), new Options(dateFin, 3), new Options(periode, 4), new Options(valoriser, 5)};
        String query = "select y.* from public.prod_et_production_consommation(?,?,?,?,?) y ORDER BY rang, code, designation";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();

            Object[] ligne;
            JournalVendeur value;
            Long article;
            String code;
            String designation;
            Long unite;
            String reference;
            String entete;
            Double production;
            String intutile;
            Double prix_vente;
            Double prix_achat;
            Double prix_revient;
            Double prix_prod;
            Double quantite;
            Double valeur;
            Integer rang;
            for (Object y : qr.getResultList()) {
                ligne = (Object[]) y;
                if (ligne != null ? ligne.length > 0 : false) {
                    int i = 0;
                    article = (Long) ligne[i++];
                    code = (String) ligne[i++];
                    designation = (String) ligne[i++];
                    unite = (Long) ligne[i++];
                    reference = (String) ligne[i++];
                    entete = (String) ligne[i++];
                    production = (Double) ligne[i++];
                    intutile = (String) ligne[i++];
                    prix_vente = (Double) ligne[i++];
                    prix_achat = (Double) ligne[i++];
                    prix_revient = (Double) ligne[i++];
                    prix_prod = (Double) ligne[i++];
                    quantite = (Double) ligne[i++];
                    valeur = (Double) ligne[i++];
                    rang = (Integer) ligne[i++];
                    double prix = 0;
                    switch (valoriser) {
                        case "P":
                            prix = prix_prod != null ? prix_prod : 0;
                            break;
                        case "A":
                            prix = prix_achat != null ? prix_achat : 0;
                            break;
                        case "V":
                            prix = prix_vente != null ? prix_vente : 0;
                            break;
                        default:
                            prix = prix_revient != null ? prix_revient : 0;
                            break;
                    }

                    if (!periodes.contains(entete)) {
                        periodes.add(entete);
                        colonnes.add(new Columns(entete, intutile, rang, production));
                    }
                    if (!elements.contains(unite)) {
                        elements.add(unite);
                        lignes.add(new Rows(unite, code + " (" + reference + ")", designation + " (" + reference + ")", 0, prix));
                    }
                    value = new JournalVendeur(unite, entete, (production != null ? production : 0), (prix_vente != null ? prix_vente : 0), (prix_achat != null ? prix_achat : 0), (prix_revient != null ? prix_revient : 0), (prix_prod != null ? prix_prod : 0), (quantite != null ? quantite : 0), (valeur != null ? valeur : 0));
                    valeurs.add(value);
                }
            }
            lignesFind = new ArrayList<>(lignes);
            elementsFinds = new ArrayList<>(elements);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadProductionConsommationByEquipe(DaoInterfaceLocal dao) {
        loadProductionConsommationByEquipe(societe, agence, site, depot, dateDebut, dateFin, valorise_by, dao);
    }

    private void loadProductionConsommationByEquipe(long societe, long agence, long site, long depot, Date dateDebut, Date dateFin, String valoriser, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(site, 3), new Options(depot, 4), new Options(dateDebut, 5), new Options(dateFin, 6), new Options(valoriser, 7)};
        String query = "select y.* from public.prod_et_production_consommation_by_equipe(?,?,?,?,?,?,?) y ORDER BY rang, designation, reference, numero, code, entree desc";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();

            Object[] ligne;
            JournalVendeur value;

            Long equipe;
            String code;
            String nom;
            Long article;
            String reference;
            String designation;
            Long unite;
            String numero;
            String categorie;
            Double quantite;
            Double prix;
            Double valeur;
            Boolean entree;
            String groupe;
            Integer rang;

            for (Object y : qr.getResultList()) {
                ligne = (Object[]) y;
                if (ligne != null ? ligne.length > 0 : false) {
                    int i = 0;
                    equipe = (Long) ligne[i++];
                    code = (String) ligne[i++];
                    nom = (String) ligne[i++];
                    article = (Long) ligne[i++];
                    reference = (String) ligne[i++];
                    designation = (String) ligne[i++];
                    unite = (Long) ligne[i++];
                    numero = (String) ligne[i++];
                    categorie = (String) ligne[i++];
                    quantite = (Double) ligne[i++];
                    prix = (Double) ligne[i++];
                    valeur = (Double) ligne[i++];
                    entree = (Boolean) ligne[i++];
                    groupe = (String) ligne[i++];
                    rang = (Integer) ligne[i++];

                    if (!periodes.contains(code)) {
                        periodes.add(code);
                        colonnes.add(new Columns(code, nom));
                    }
                    unite = entree != null ? entree ? unite : -unite : -unite;
                    value = new JournalVendeur(groupe, rang);
                    int index = valeurs.indexOf(value);
                    if (index > -1) {
                        value = valeurs.get(index);
                    }
                    if (!value.getElements().contains(unite)) {
                        value.getElements().add(unite);
                        value.getLignes().add(new Rows(unite, reference + " (" + numero + ")", designation + " (" + numero + ")", categorie, 0, prix, entree));
                    }
                    value.getSous().add(new JournalVendeur(unite, code, quantite, valeur, entree));
                    if (index > -1) {
                        valeurs.set(index, value);
                    } else {
                        valeurs.add(value);
                    }
                }
            }
            lignesFind = new ArrayList<>(lignes);
            elementsFinds = new ArrayList<>(elements);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnSyntheseDistribution(DaoInterfaceLocal dao) {
        returnSyntheseDistribution(societe, agence, depot, dateDebut, dateFin, byValue, nature, dao);
    }

    public void returnSyntheseDistribution(long societe, long agence, long depot, Date dateDebut, Date dateFin, boolean ForProd, String typeVal, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(depot, 3), new Options(dateDebut, 4), new Options(dateFin, 5), new Options(ForProd, 6), new Options(typeVal, 7)};
        String query = "select y.id, y.groupe, y.libelle, y.type, y.classe, y.code, y.intitule, y.valeur, y.prix, y.quantite, y.rang "
                + "FROM public.com_et_synthese_approvision_distribution(?,?,?,?,?,?,?) y";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            titres.clear();
            groupes.clear();
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();

            valeurs.clear();
            Object[] o;
            JournalVendeur j;
            Long id;
            String groupe;
            String libelle;
            Long type;
            Long classe;
            String code;
            String intitule;
            Double valeur;
            Double prix;
            Double quantite;
            Integer rang;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    id = (Long) o[0];
                    groupe = (String) o[1];
                    libelle = (String) o[2];
                    type = (Long) o[3];
                    classe = (Long) o[4];
                    code = (String) o[5];
                    intitule = (String) o[6];
                    valeur = (Double) o[7];
                    prix = (Double) o[8];
                    quantite = (Double) o[9];
                    rang = (Integer) o[10];

                    if (!titres.contains(groupe)) { //fabrique les groupes de colonnes
                        titres.add(groupe);
                        groupes.add(new Columns(null, groupe, 1));
                    } else {
                        if (!periodes.contains(libelle)) {
                            for (Columns grp : groupes) {
                                if (Objects.equals(grp.getLibelle(), groupe)) {
                                    grp.setPosition(grp.getPosition() + 1);
                                }
                            }
                        }
                    }
                    if (!periodes.contains(libelle)) { //fabrique les colonnes
                        periodes.add(libelle);
                        colonnes.add(new Columns(libelle, groupe, rang));
                    }
                    if (!elements.contains(classe)) { //fabrique les lignes
                        elements.add(classe);
                        lignes.add(new Rows(classe, code, intitule));
                    }
                    j = new JournalVendeur((classe != null ? classe : 0), libelle, groupe);
                    j.setValeur((valeur != null ? valeur : 0));
                    j.setQuantite(quantite);
                    j.setPrixvente(prix);
                    valeurs.add(j);
                }
            }
            totaux = 0;
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnSyntheseRation(DaoInterfaceLocal dao) {
        returnSyntheseRation(societe, agence, depot, comptes, dateDebut, dateFin, periode, valorise_by, dao);
    }

    public void returnSyntheseRation(long societe, long agence, long depot, String comptes, Date dateDebut, Date dateFin, String periode, String valoriserBy, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(depot, 3), new Options(comptes, 4), new Options(dateDebut, 5), new Options(dateFin, 6), new Options(periode, 7), new Options(valoriserBy, 8)};
        String query = "select * from public.com_et_ration(?,?,?,?,?,?,?,?) y order by reference, nom, code, rang";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();

            valeurs.clear();
            Object[] ligne;
            JournalVendeur value;

            Long tiers;
            String code;
            String nom;
            Long article;
            String reference;
            String designation;
            Long unite;
            String libelle;
            String intitule;
            Double prix;
            Double ration;
            Double prevu;
            Integer rang;
            Double valeur_ration;
            Double valeur_prevu;
            for (Object y : qr.getResultList()) {
                ligne = (Object[]) y;
                if (ligne != null ? ligne.length > 0 : false) {
                    int i = 0;
                    tiers = (Long) ligne[i++];
                    code = (String) ligne[i++];
                    nom = (String) ligne[i++];
                    article = (Long) ligne[i++];
                    reference = (String) ligne[i++];
                    designation = (String) ligne[i++];
                    unite = (Long) ligne[i++];
                    libelle = (String) ligne[i++];
                    intitule = (String) ligne[i++];
                    prix = (Double) ligne[i++];
                    ration = (Double) ligne[i++];
                    prevu = (Double) ligne[i++];
                    rang = (Integer) ligne[i++];

                    prix = (prix != null ? prix : 0);
                    ration = (ration != null ? ration : 0);
                    prevu = (prevu != null ? prevu : 0);

                    valeur_ration = prix * ration;
                    valeur_prevu = prix * prevu;

                    if (!periodes.contains(intitule)) { //fabrique les colonnes
                        periodes.add(intitule);
                        colonnes.add(new Columns(intitule, intitule, rang));
                    }

                    value = new JournalVendeur((article != null ? article : 0), reference, designation, libelle, prix);
                    int index = valeurs.indexOf(value);
                    if (index > -1) {
                        value = valeurs.get(index);
                    }
                    if (!value.getElements().contains(tiers)) { //fabrique les lignes
                        value.getElements().add(tiers);
                        value.getLignes().add(new Rows(tiers, code, nom));
                    }
                    value.getSous().add(new JournalVendeur(tiers, intitule, prix, ration, prevu, valeur_ration, valeur_prevu));
                    if (index > -1) {
                        valeurs.set(index, value);
                    } else {
                        valeurs.add(value);
                    }
                }
            }
            Collections.sort(colonnes);
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnConsommationMP(DaoInterfaceLocal dao) {
        returnConsommationMP(societe, agence, depot, comptes, dateDebut, dateFin, periode, nature, dao);
    }

    public void returnConsommationMP(long societe, long agence, long depot, String comptes, Date dateDebut, Date dateFin, String periode, String type, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(depot, 3), new Options(comptes, 4), new Options(dateDebut, 5), new Options(dateFin, 6), new Options(periode, 7), new Options(type, 8)};
        String query = "select y.article, y.reference, y.designation, y.unite, y.numero, y.classe, y.code, y.intitule, y.valeur, "
                + "y.quantite, y.rang, y.prix from public.prod_et_synthese_consommation_mp(?,?,?,?,?,?,?,?) y";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();

            valeurs.clear();
            Object[] o;
            JournalVendeur j;
            Rows l;
            Long article;
            String reference;
            String designation;
            Long unite;
            String numero;
            Long classe;
            String code;
            String intitule;
            Double valeur;
            Double quantite;
            Integer rang;
            Double prix;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    article = (Long) o[0];
                    reference = (String) o[1];
                    designation = (String) o[2];
                    unite = ((Long) o[3] != null) ? (Long) o[3] : 0;
                    numero = (String) o[4];
                    classe = ((Long) o[5] != null) ? (Long) o[5] : 0L;
                    code = (String) o[6];
                    intitule = (String) o[7];
                    valeur = ((Double) o[8] != null) ? (Double) o[8] : 0;
                    quantite = ((Double) o[9] != null) ? (Double) o[9] : 0;
                    rang = ((Integer) o[10] != null) ? (Integer) o[10] : 0;
                    prix = ((Double) o[11] != null) ? (Double) o[11] : 0;

                    if (!periodes.contains(intitule)) { //fabrique les colonnes
                        periodes.add(intitule);
                        colonnes.add(new Columns(intitule, code, rang));
                    }
                    l = new Rows(reference, designation, numero);
                    l.setQuantite(prix != null ? prix : 0);
                    if (!elements.contains(article)) { //fabrique les lignes
                        elements.add(article);
                        l.getTitres().add(numero);
                        l.getUnites().add(new Rows(prix, numero));
                        lignes.add(l);
                    } else {
                        int idx = lignes.indexOf(l);
                        if (idx > -1) {
                            l = (Rows) lignes.get(idx);
                            if (!l.getAutres().equals(numero) ? !l.getTitres().contains(numero) : false) {
                                l.getTitres().add(numero);
                                l.getUnites().add(new Rows(prix, numero));
                                lignes.set(idx, l);
                            }
                        }
                    }
                    j = new JournalVendeur((article != null ? article : 0), intitule, numero);
                    j.setValeur((valeur != null ? valeur : 0));
                    j.setQuantite((quantite != null ? quantite : 0));
                    valeurs.add(j);
                }
            }
            totaux = 0;
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnProductionVente(DaoInterfaceLocal dao) {
        returnProductionVente(societe, agence, comptes, dateDebut, dateFin, periode, nature, valorise_by, dao);
    }

    public void returnProductionVente(long societe, long agence, String articles, Date dateDebut, Date dateFin, String periode, String type, String valorise_by, DaoInterfaceLocal dao) {
        dao.getEntityManager().clear();
        int i = 1;
        Options[] param = new Options[]{new Options(societe, i++), new Options(agence, i++), new Options(articles, i++), new Options(dateDebut, i++), new Options(dateFin, i++), new Options(periode, i++), new Options(type, i++), new Options(valorise_by, i++)};
        String query = "select y.id, y.reference, y.designation, y.unite, y.intitule, y.entete, y.date_debut, y.date_fin, y.production, y.vente, y.ecart, y.production_val, y.vente_val, y.ecart_val, y.prix from public.prod_et_production_vente(?,?,?,?,?,?,?,?) y";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();

            valeurs.clear();
            Object[] o;
            JournalVendeur j;
            Rows l;
            Long article;
            String reference;
            String designation;
            Long unite;
            String intitule;
            String entete;
            Date date_debut;
            Date date_fin;
            Double production;
            Double vente;
            Double ecart;
            Double production_val;
            Double vente_val;
            Double ecart_val;
            Double prix;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    article = (Long) o[0];
                    reference = (String) o[1];
                    designation = (String) o[2];
                    unite = (Long) o[3];
                    intitule = (String) o[4];
                    entete = (String) o[5];
                    date_debut = (Date) o[6];
                    date_fin = (Date) o[7];
                    production = (Double) o[8];
                    vente = (Double) o[9];
                    ecart = (Double) o[10];
                    production_val = (Double) o[11];
                    vente_val = (Double) o[12];
                    ecart_val = (Double) o[13];
                    prix = (Double) o[14];

                    if (!periodes.contains(entete)) { //fabrique les colonnes
                        periodes.add(entete);
                        colonnes.add(new Columns(entete, entete, 0));
                    }
                    l = new Rows(reference, designation, intitule);
                    if (!elements.contains(article)) { //fabrique les lignes
                        elements.add(article);
                        l.getTitres().add(intitule);
                        lignes.add(l);
                    } else {
                        int idx = lignes.indexOf(l);
                        if (idx > -1) {
                            l = (Rows) lignes.get(idx);
                            if (!l.getTitre().equals(intitule) ? !l.getTitres().contains(intitule) : false) {
                                l.getTitres().add(intitule);
                                lignes.set(idx, l);
                            }
                        }
                    }
                    j = new JournalVendeur((article != null ? article : 0), entete, intitule);
                    j.setQuantite((production != null ? production : 0));
                    j.setValeur((vente != null ? vente : 0));
                    j.setAttente((production_val != null ? production_val : 0));
                    j.setTtc((vente_val != null ? vente_val : 0));
                    j.setPrixachat((ecart != null ? ecart : 0));
                    j.setPrixrevient((ecart_val != null ? ecart_val : 0));
                    j.setPrixvente((prix != null ? prix : 0));
                    valeurs.add(j);
                }
            }
            totaux = 0;
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadProgressSalaire(DaoInterfaceLocal dao) {
        loadProgressSalaire(societe, agence, employe, dateDebut, dateFin, dao);
    }

    private void loadProgressSalaire(long societe, long agence, long employe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        Options[] param = new Options[]{new Options(societe, 1), new Options(agence, 2), new Options(employe, 3), new Options(dateDebut, 4), new Options(dateFin, 5)};
        String query = "select y.element, y.designation, y.header, y.reference, y.date_debut, y.date_fin, y.montant, y.retenue, y.is_total from public.grh_et_progression_salariale(?,?,?,?,?) y";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            lignes.clear();
            elements.clear();
            colonnes.clear();
            periodes.clear();
            valeurs.clear();
            Object[] o;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long element = (Long) o[0];
                    String designation = (String) o[1];
                    Long header = (Long) o[2];
                    String reference = (String) o[3];
                    Date date_debut = (Date) o[4];
                    Date date_fin = (Date) o[5];
                    Double montant = (Double) o[6];
                    Boolean retenue = (Boolean) o[7];
                    Boolean is_total = (Boolean) o[8];
                    if (!elements.contains(element)) { //fabrique les lignes
                        elements.add(element);
                        lignes.add(new Rows(designation, designation));
                    }
                    if (!periodes.contains(reference)) {
                        periodes.add(reference);
                        colonnes.add(new Columns(reference, reference, 0));
                    }
                    valeurs.add(new JournalVendeur((element != null ? element : 0), reference, (montant != null ? montant : 0), (retenue != null ? retenue : false), (is_total != null ? is_total : false)));
                }
            }
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.nature);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Dashboards other = (Dashboards) obj;
        if (!Objects.equals(this.nature, other.nature)) {
            return false;
        }
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    public Dashboards deepClone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Dashboards) ois.readObject();
    }
}

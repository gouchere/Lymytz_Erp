/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.etats.commercial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.produits.YvsBaseArticles;
import yvs.etats.Rows;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class JournalVendeur implements Serializable {

    private long element;
    private String reference;
    private String periode;
    private String principal;
    private String secondaire;
    private double salaire;
    private double presence;
    private double conge;
    private double permission;
    private double ttc;
    private double quantite;
    private double prixrevient;
    private double prixvente;
    private double prixachat;
    private double taux;
    private double valeur;
    private double attente;
    private double marge;
    private double marge_min;
    private int position;
    private boolean onTotal;
    private boolean onFooter;

    private boolean onHead;
    private boolean onCharge;
    private boolean onProduit;

    private String numero;
    private double ci, di, cp, dp, csp, dsp, csc, dsc;
    private boolean general;

    private Date date;

    private YvsComClient client = new YvsComClient();
    private YvsBaseArticles article = new YvsBaseArticles();
    private List<Long> elements = new ArrayList<>();    //Modélise les lignes du tableau à n dimension
    private List<Long> elementsFinds = new ArrayList<>();
    private List<Rows> lignes = new ArrayList<>();
    private List<Rows> lignesFind = new ArrayList<>();

    public static final String TYPE_TTC = "ca";
    public static final String TYPE_QUANTITE = "qte";
    public static final String TYPE_TAUX = "taux";
    public static final String TYPE_SALAIRE = "sal";
    public static final String TYPE_PRESENCE = "pre";
    public static final String TYPE_CONGE = "con";
    public static final String TYPE_PERMISSION = "per";
    public static final String TYPE_VALEUR = "valeur";
    public static final String TYPE_ATTENTE = "attente";
    public static final String TYPE_MARGE = "marge";
    public static final String TYPE_MARGE_MIN = "marge_min";
    public static final String TYPE_PRIX_REVIENT = "pr";
    public static final String TYPE_PRIX_VENTE = "puv";
    public static final String TYPE_PRIX_ACHAT = "pua";
    public static final String TYPE_CREDIT_INITIAL = "ci";
    public static final String TYPE_DEBIT_INITIAL = "di";
    public static final String TYPE_CREDIT_PERIODIQUE = "cp";
    public static final String TYPE_DEBIT_PERIODIQUE = "dp";
    public static final String TYPE_CREDIT_SOLDE_PERIODIQUE = "csp";
    public static final String TYPE_DEBIT_SOLDE_PERIODIQUE = "dsp";
    public static final String TYPE_CREDIT_SOLDE_CUMULE = "csc";
    public static final String TYPE_DEBIT_SOLDE_CUMULE = "dsc";

    private List<JournalVendeur> footers;
    private List<JournalVendeur> sous;

    public JournalVendeur() {
        sous = new ArrayList<>();
        footers = new ArrayList<>();
    }

    public JournalVendeur(String periode) {
        this();
        this.periode = periode;
    }

    public JournalVendeur(String periode, int position) {
        this(periode);
        this.position = position;
    }

    public JournalVendeur(long element, String periode) {
        this(periode);
        this.element = element;
    }

    public JournalVendeur(long element, String periode, String secondaire) {
        this(element, periode);
        this.secondaire = secondaire;
    }

    public JournalVendeur(long element, String periode, String secondaire, boolean onProduit) {
        this(element, periode, secondaire);
        this.onProduit = onProduit;
    }

    public JournalVendeur(long element, String periode, double quantite, double valeur) {
        this(element, periode);
        this.quantite = quantite;
        this.valeur = valeur;
    }

    public JournalVendeur(long element, String periode, String principal, String secondaire) {
        this(element, periode, secondaire);
        this.principal = principal;
    }

    public JournalVendeur(long element, String periode, String principal, String secondaire, double valeur) {
        this(element, periode, secondaire, principal);
        this.valeur = valeur;
    }

    public JournalVendeur(long element, String periode, double valeur) {
        this(element, periode);
        this.valeur = valeur;
    }

    public JournalVendeur(long element, String periode, double quantite, double valeur, boolean onProduit) {
        this(element, periode, valeur);
        this.quantite = quantite;
        this.onProduit = onProduit;
    }

    public JournalVendeur(long element, String periode, double valeur, boolean onCharge, boolean onTotal) {
        this(element, periode, valeur);
        this.onCharge = onCharge;
        this.onTotal = onTotal;
    }

    public JournalVendeur(long element, String periode, double attente, double prixvente, double prixachat, double prixrevient, double prixprod, double quantite, double total) {
        this(element, periode, total);
        this.attente = attente;
        this.prixvente = prixvente;
        this.prixachat = prixachat;
        this.prixrevient = prixrevient;
        this.taux = prixprod;
        this.quantite = quantite;
    }

    public JournalVendeur(long element, String periode, double prixvente, double quantite, double attente, double valeur, double marge) {
        this(element, periode);
        this.attente = attente;
        this.prixvente = prixvente;
        this.valeur = valeur;
        this.marge = marge;
        this.quantite = quantite;
    }

    public JournalVendeur(long element, String periode, double valeur, int position) {
        this(element, periode, valeur);
        this.position = position;
    }

    public JournalVendeur(long element, String periode, String principal, double valeur, int position) {
        this(element, periode, valeur, position);
        this.principal = principal;
    }

    public JournalVendeur(long element, String periode, String principal, double valeur, double quantite, int position) {
        this(element, periode, principal, valeur, position);
        this.quantite = quantite;
    }

    public JournalVendeur(long element, String periode, double valeur, double attente, int position) {
        this(element, periode, valeur, position);
        this.attente = attente;
    }

    public JournalVendeur(long element, String periode, double quantite, double prixrevient, double prixvente) {
        this(element, periode);
        this.quantite = quantite;
        this.prixrevient = prixrevient;
        this.prixvente = prixvente;
    }

    public JournalVendeur(long element, String periode, double quantite, double prixrevient, double prixvente, double prixachat) {
        this(element, periode, quantite, prixrevient, prixvente);
        this.prixachat = prixachat;
    }

    public JournalVendeur(long element, String periode, double quantite, double prixrevient, double prixvente, double prixachat, boolean onTotal) {
        this(element, periode, quantite, prixrevient, prixvente, prixachat);
        this.onTotal = onTotal;
    }

    public JournalVendeur(long element, String periode, double quantite, double prixrevient, double prixvente, double prixachat, boolean onTotal, double attente, double taux, boolean onFooter) {
        this(element, periode, quantite, prixrevient, prixvente, prixachat, onTotal);
        this.attente = attente;
        this.taux = taux;
        this.onFooter = onFooter;
    }

    public JournalVendeur(long element, String periode, double quantite, double marge, double prixrevient, double prixvente, double prixachat, boolean onTotal, double attente, double taux, boolean onFooter) {
        this(element, periode, quantite, prixrevient, prixvente, prixachat, onTotal, attente, taux, onFooter);
        this.marge = marge;
    }

    public JournalVendeur(long element, String periode, Date date, double quantite, double prixrevient, double prixvente) {
        this(element, periode, quantite, prixrevient, prixvente);
        this.date = date;
    }

    public JournalVendeur(long element, String periode, String secondaire, String unite, double quantite, double prixrevient, double prixvente) {
        this(element, periode, quantite, prixrevient, prixvente);
        this.principal = unite;
        this.secondaire = secondaire;
    }

    public JournalVendeur(long element, String periode, String secondaire, String unite, String principal, double quantite, double prixrevient, double prixvente) {
        this(element, periode, secondaire, principal, quantite, prixrevient, prixvente);
        this.numero = unite;
    }

    public JournalVendeur(long element, String periode, String secondaire, String unite, String principal, double quantite, double prixrevient, double prixvente, String reference) {
        this(element, periode, secondaire, unite, principal, quantite, prixrevient, prixvente);
        this.reference = reference;
    }

    public JournalVendeur(long element, String periode, String secondaire, String unite, String principal, double quantite, double prixrevient, double prixvente, String reference, double marge, double margemin, double valeur, double attente) {
        this(element, periode, secondaire, unite, principal, quantite, prixrevient, prixvente, reference);
        this.marge = marge;
        this.marge_min = margemin;
        this.valeur = valeur;
        this.attente = attente;
    }

    public JournalVendeur(long element, String periode, String secondaire, String unite, double quantite, double prixrevient, double prixvente, double prixachat) {
        this(element, periode, secondaire, unite, quantite, prixrevient, prixvente);
        this.prixachat = prixachat;
    }

    public JournalVendeur(long element, String periode, String secondaire, String principal, double valeur, double attente, int position) {
        this(element, periode, valeur, attente, position);
        this.secondaire = secondaire;
        this.principal = principal;
    }

    public JournalVendeur(long element, String periode, String unite, double ttc, double quantite, double taux, int position) {
        this(element, periode, ttc, quantite, taux, position);
        this.secondaire = unite;
    }

    public JournalVendeur(long element, String periode, String unite, double ttc, double quantite, double taux, int position, boolean onTotal, boolean onFooter) {
        this(element, periode, ttc, quantite, taux, position, onTotal, onFooter);
        this.secondaire = unite;
    }

    public JournalVendeur(long element, String periode, String unite, double ttc, double quantite, double prixrevient, double taux, int position, boolean onTotal, boolean onFooter) {
        this(element, periode, unite, ttc, quantite, taux, position, onTotal, onFooter);
        this.prixrevient = prixrevient;
        this.prixvente = ttc / quantite;
    }

    public JournalVendeur(long element, String periode, String unite, double ttc, double quantite, double prixrevient, double attente, double taux, int position) {
        this(element, periode, unite, ttc, quantite, taux, position);
        this.attente = attente;
        this.prixrevient = prixrevient;
        this.prixvente = ttc / quantite;
    }

    public JournalVendeur(long element, String periode, String unite, double ttc, double quantite, double prixrevient, double attente, double taux, int position, boolean onTotal, boolean onFooter) {
        this(element, periode, unite, ttc, quantite, taux, position, onTotal, onFooter);
        this.attente = attente;
        this.prixrevient = prixrevient;
        this.prixvente = ttc / quantite;
    }

    public JournalVendeur(long element, String periode, String unite, double ttc, double quantite, double prixrevient, double attente, double taux, double marge_min, int position) {
        this(element, periode, unite, ttc, quantite, prixrevient, attente, taux, position);
        this.marge_min = marge_min;
        this.marge = ttc - prixrevient;
    }

    public JournalVendeur(long element, String periode, String unite, double ttc, double quantite, double prixrevient, double attente, double taux, double marge_min, int position, boolean onTotal, boolean onFooter) {
        this(element, periode, unite, ttc, quantite, prixrevient, attente, taux, position, onTotal, onFooter);
        this.marge_min = marge_min;
        this.marge = ttc - prixrevient;
    }

    public JournalVendeur(long element, String periode, double ttc, double quantite, double taux, int position) {
        this(element, periode);
        this.ttc = ttc;
        this.quantite = quantite;
        this.taux = taux;
        this.position = position;
    }

    public JournalVendeur(long element, String periode, double ttc, double quantite, double taux, double attente, int position) {
        this(element, periode, ttc, quantite, taux, position);
        this.attente = attente;
    }

    public JournalVendeur(long element, String periode, double ttc, double quantite, double taux, int position, boolean onTotal, boolean onFooter) {
        this(element, periode, ttc, quantite, taux, position);
        this.onTotal = onTotal;
        this.onFooter = onFooter;
    }

    public JournalVendeur(long element, String periode, double salaire, double presence, double conge, double permission, int position, boolean onTotal, boolean onFooter) {
        this(element, periode);
        this.salaire = salaire;
        this.presence = presence;
        this.conge = conge;
        this.permission = permission;
        this.position = position;
        this.onTotal = onTotal;
        this.onFooter = onFooter;
    }

    public JournalVendeur(String numero, String intitule, String numeroGeneral, String intituleGeneral, double ci, double di, double cp, double dp, double csp, double dsp, double csc, double dsc, boolean general) {
        this(numero != null ? numero.hashCode() : 0, intitule);
        this.numero = numero;
        this.principal = numeroGeneral;
        this.secondaire = intituleGeneral;
        this.ci = ci;
        this.di = di;
        this.cp = cp;
        this.dp = dp;
        this.csp = csp;
        this.dsp = dsp;
        this.csc = csc;
        this.dsc = dsc;
        this.general = general;
    }

    public JournalVendeur(long element, String numero, String periode, String principal, double valeur, String secondaire, boolean onHead, boolean onProduit, boolean onCharge, boolean onTotal) {
        this(element, periode);
        this.principal = principal;
        this.secondaire = secondaire;
        this.valeur = valeur;
        this.onTotal = onTotal;
        this.onHead = onHead;
        this.onCharge = onCharge;
        this.onProduit = onProduit;
        this.numero = numero;
    }

    public JournalVendeur(String numero, String periode, String principal, String secondaire, String reference, double quantite, double pr, double puv, double total) {
        this(periode);
        this.principal = principal;
        this.secondaire = secondaire;
        this.reference = reference;
        this.numero = numero;
        this.quantite = quantite;
        this.prixrevient = pr;
        this.prixvente = puv;
        this.valeur = total;
    }

    public JournalVendeur(String numero, String periode, String principal, String secondaire, String reference, double quantite1, double quantite2, double pr, double puv, double total1, double total2) {
        this(periode);
        this.principal = principal;
        this.secondaire = secondaire;
        this.reference = reference;
        this.numero = numero;
        this.quantite = quantite1;
        this.attente = quantite2;
        this.prixrevient = pr;
        this.prixvente = puv;
        this.valeur = total1;
        this.ttc = total2;
    }

    public String getTYPE_TTC() {
        return TYPE_TTC;
    }

    public String getTYPE_QUANTITE() {
        return TYPE_QUANTITE;
    }

    public String getTYPE_TAUX() {
        return TYPE_TAUX;
    }

    public String getTYPE_SALAIRE() {
        return TYPE_SALAIRE;
    }

    public String getTYPE_PRESENCE() {
        return TYPE_PRESENCE;
    }

    public String getTYPE_CONGE() {
        return TYPE_CONGE;
    }

    public String getTYPE_PERMISSION() {
        return TYPE_PERMISSION;
    }

    public String getTYPE_VALEUR() {
        return TYPE_VALEUR;
    }

    public String getTYPE_ATTENTE() {
        return TYPE_ATTENTE;
    }

    public String getTYPE_MARGE() {
        return TYPE_MARGE;
    }

    public String getTYPE_MARGE_MIN() {
        return TYPE_MARGE_MIN;
    }

    public String getTYPE_PRIX_REVIENT() {
        return TYPE_PRIX_REVIENT;
    }

    public String getTYPE_PRIX_VENTE() {
        return TYPE_PRIX_VENTE;
    }

    public String getTYPE_PRIX_ACHAT() {
        return TYPE_PRIX_ACHAT;
    }

    public String getTYPE_CREDIT_INITIAL() {
        return TYPE_CREDIT_INITIAL;
    }

    public String getTYPE_DEBIT_INITIAL() {
        return TYPE_DEBIT_INITIAL;
    }

    public String getTYPE_CREDIT_PERIODIQUE() {
        return TYPE_CREDIT_PERIODIQUE;
    }

    public String getTYPE_DEBIT_PERIODIQUE() {
        return TYPE_DEBIT_PERIODIQUE;
    }

    public String getTYPE_CREDIT_SOLDE_PERIODIQUE() {
        return TYPE_CREDIT_SOLDE_PERIODIQUE;
    }

    public String getTYPE_DEBIT_SOLDE_PERIODIQUE() {
        return TYPE_DEBIT_SOLDE_PERIODIQUE;
    }

    public String getTYPE_CREDIT_SOLDE_CUMULE() {
        return TYPE_CREDIT_SOLDE_CUMULE;
    }

    public String getTYPE_DEBIT_SOLDE_CUMULE() {
        return TYPE_DEBIT_SOLDE_CUMULE;
    }

    public List<Long> getElements() {
        return elements;
    }

    public void setElements(List<Long> elements) {
        this.elements = elements;
    }

    public List<Long> getElementsFinds() {
        return elementsFinds;
    }

    public void setElementsFinds(List<Long> elementsFinds) {
        this.elementsFinds = elementsFinds;
    }

    public List<Rows> getLignes() {
        return lignes;
    }

    public void setLignes(List<Rows> lignes) {
        this.lignes = lignes;
    }

    public List<Rows> getLignesFind() {
        return lignesFind;
    }

    public void setLignesFind(List<Rows> lignesFind) {
        this.lignesFind = lignesFind;
    }

    public List<JournalVendeur> getFooters() {
        return footers;
    }

    public void setFooters(List<JournalVendeur> footers) {
        this.footers = footers;
    }

    public List<JournalVendeur> getSous() {
        return sous;
    }

    public void setSous(List<JournalVendeur> sous) {
        this.sous = sous;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isOnHead() {
        return onHead;
    }

    public void setOnHead(boolean onHead) {
        this.onHead = onHead;
    }

    public boolean isOnCharge() {
        return onCharge;
    }

    public void setOnCharge(boolean onCharge) {
        this.onCharge = onCharge;
    }

    public boolean isOnProduit() {
        return onProduit;
    }

    public void setOnProduit(boolean onProduit) {
        this.onProduit = onProduit;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getCi() {
        return ci;
    }

    public void setCi(double ci) {
        this.ci = ci;
    }

    public double getDi() {
        return di;
    }

    public void setDi(double di) {
        this.di = di;
    }

    public double getCp() {
        return cp;
    }

    public void setCp(double cp) {
        this.cp = cp;
    }

    public double getDp() {
        return dp;
    }

    public void setDp(double dp) {
        this.dp = dp;
    }

    public double getCsp() {
        return csp;
    }

    public void setCsp(double csp) {
        this.csp = csp;
    }

    public double getDsp() {
        return dsp;
    }

    public void setDsp(double dsp) {
        this.dsp = dsp;
    }

    public double getCsc() {
        return csc;
    }

    public void setCsc(double csc) {
        this.csc = csc;
    }

    public double getDsc() {
        return dsc;
    }

    public void setDsc(double dsc) {
        this.dsc = dsc;
    }

    public boolean isGeneral() {
        return general;
    }

    public double getMarge() {
        return marge;
    }

    public void setMarge(double marge) {
        this.marge = marge;
    }

    public double getMarge_min() {
        return marge_min;
    }

    public void setMarge_min(double marge_min) {
        this.marge_min = marge_min;
    }

    public void setGeneral(boolean general) {
        this.general = general;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getSecondaire() {
        return secondaire;
    }

    public void setSecondaire(String secondaire) {
        this.secondaire = secondaire;
    }

    public double getPrixrevient() {
        return prixrevient;
    }

    public void setPrixrevient(double prixrevient) {
        this.prixrevient = prixrevient;
    }

    public double getPrixvente() {
        return prixvente;
    }

    public void setPrixvente(double prixvente) {
        this.prixvente = prixvente;
    }

    public double getPrixachat() {
        return prixachat;
    }

    public void setPrixachat(double prixachat) {
        this.prixachat = prixachat;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public double getAttente() {
        return attente;
    }

    public void setAttente(double attente) {
        this.attente = attente;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public double getPresence() {
        return presence;
    }

    public void setPresence(double presence) {
        this.presence = presence;
    }

    public double getConge() {
        return conge;
    }

    public void setConge(double conge) {
        this.conge = conge;
    }

    public double getPermission() {
        return permission;
    }

    public void setPermission(double permission) {
        this.permission = permission;
    }

    public boolean isOnTotal() {
        return onTotal;
    }

    public void setOnTotal(boolean onTotal) {
        this.onTotal = onTotal;
    }

    public boolean isOnFooter() {
        return onFooter;
    }

    public void setOnFooter(boolean onFooter) {
        this.onFooter = onFooter;
    }

    public YvsComClient getClient() {
        return client;
    }

    public void setClient(YvsComClient client) {
        this.client = client;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public long getElement() {
        return element;
    }

    public void setElement(long element) {
        this.element = element;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public double getTtc() {
        return ttc;
    }

    public void setTtc(double ttc) {
        this.ttc = ttc;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (int) (this.element ^ (this.element >>> 32));
        hash = 53 * hash + Objects.hashCode(this.periode);
        if (this.secondaire != null) {
            hash = 53 * hash + Objects.hashCode(this.secondaire);
        }
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
        final JournalVendeur other = (JournalVendeur) obj;
        if (this.element != other.element) {
            return false;
        }
        if (!Objects.equals(this.periode, other.periode)) {
            return false;
        }
        if (this.secondaire != null && other.secondaire != null) {
            if (!Objects.equals(this.secondaire, other.secondaire)) {
                return false;
            }
        }
        return true;
    }

    public void set(String type, Object value) {
        if (type != null) {
            switch (type) {
                case TYPE_TTC:
                    ttc = Double.valueOf(value.toString());
                    break;
                case TYPE_QUANTITE:
                    quantite = Double.valueOf(value.toString());
                    break;
                case TYPE_TAUX:
                    taux = Double.valueOf(value.toString());
                    break;
                case TYPE_SALAIRE:
                    salaire = Double.valueOf(value.toString());
                    break;
                case TYPE_PRESENCE:
                    presence = Double.valueOf(value.toString());
                    break;
                case TYPE_CONGE:
                    conge = Double.valueOf(value.toString());
                    break;
                case TYPE_PERMISSION:
                    permission = Double.valueOf(value.toString());
                    break;
                case TYPE_VALEUR:
                    valeur = Double.valueOf(value.toString());
                    break;
                case TYPE_ATTENTE:
                    attente = Double.valueOf(value.toString());
                    break;
                case TYPE_MARGE:
                    marge = Double.valueOf(value.toString());
                    break;
                case TYPE_MARGE_MIN:
                    marge_min = Double.valueOf(value.toString());
                    break;
                case TYPE_PRIX_REVIENT:
                    prixrevient = Double.valueOf(value.toString());
                    break;
                case TYPE_PRIX_VENTE:
                    prixvente = Double.valueOf(value.toString());
                    break;
                case TYPE_PRIX_ACHAT:
                    prixachat = Double.valueOf(value.toString());
                    break;
                case TYPE_CREDIT_INITIAL:
                    ci = Double.valueOf(value.toString());
                    break;
                case TYPE_DEBIT_INITIAL:
                    di = Double.valueOf(value.toString());
                    break;
                case TYPE_CREDIT_PERIODIQUE:
                    cp = Double.valueOf(value.toString());
                    break;
                case TYPE_DEBIT_PERIODIQUE:
                    dp = Double.valueOf(value.toString());
                    break;
                case TYPE_CREDIT_SOLDE_PERIODIQUE:
                    csp = Double.valueOf(value.toString());
                    break;
                case TYPE_DEBIT_SOLDE_PERIODIQUE:
                    dsp = Double.valueOf(value.toString());
                    break;
                case TYPE_CREDIT_SOLDE_CUMULE:
                    csc = Double.valueOf(value.toString());
                    break;
                case TYPE_DEBIT_SOLDE_CUMULE:
                    dsc = Double.valueOf(value.toString());
                    break;
            }
        }
    }

    public Object get(String type) {
        if (type != null) {
            switch (type) {
                case TYPE_TTC:
                    return ttc;
                case TYPE_QUANTITE:
                    return quantite;
                case TYPE_TAUX:
                    return taux;
                case TYPE_SALAIRE:
                    return salaire;
                case TYPE_PRESENCE:
                    return presence;
                case TYPE_CONGE:
                    return conge;
                case TYPE_PERMISSION:
                    return permission;
                case TYPE_VALEUR:
                    return valeur;
                case TYPE_ATTENTE:
                    return attente;
                case TYPE_MARGE:
                    return marge;
                case TYPE_MARGE_MIN:
                    return marge_min;
                case TYPE_PRIX_REVIENT:
                    return prixrevient;
                case TYPE_PRIX_VENTE:
                    return prixvente;
                case TYPE_PRIX_ACHAT:
                    return prixachat;
                case TYPE_CREDIT_INITIAL:
                    return ci;
                case TYPE_DEBIT_INITIAL:
                    return di;
                case TYPE_CREDIT_PERIODIQUE:
                    return cp;
                case TYPE_DEBIT_PERIODIQUE:
                    return dp;
                case TYPE_CREDIT_SOLDE_PERIODIQUE:
                    return csp;
                case TYPE_DEBIT_SOLDE_PERIODIQUE:
                    return dsp;
                case TYPE_CREDIT_SOLDE_CUMULE:
                    return csc;
                case TYPE_DEBIT_SOLDE_CUMULE:
                    return dsc;
                default:
                    return 0;
            }
        }
        return 0;
    }
}

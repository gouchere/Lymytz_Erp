/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.etats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.connexion.Loggin;
import yvs.entity.compta.YvsComptaContentJournal;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ValeurComptable extends Valeurs implements Serializable {

    private long id;
    private long piece;
    private long compteGeneral;
    private long plan;
    private String code;
    private String codePlan;
    private String reference;
    private String description;
    private String intitule;
    private String compte;
    private String designation;
    private String numero;
    private String nomPrenom;
    private Date date = new Date();
    private int jour;
    private String libelle;
    private String lettrage;
    private String journal;
    private String tableTiers;
    private long compteTiers;
    private double debit;
    private double credit;
    private double soldeProg = 0;
    private double soldeDebit;
    private double soldeCredit;
    private boolean solde;
    private double debitPeriodComptaResume, creditPeriodComptaResume;

    private List<ValeurComptable> sous;
    private List<YvsComptaContentJournal> contents;

    public ValeurComptable() {
        sous = new ArrayList<>();
        contents = new ArrayList<>();
    }

    public ValeurComptable(long id) {
        this();
        this.id = id;
    }

    public ValeurComptable(long id, String code, String intitule) {
        this(id);
        this.code = code;
        this.intitule = intitule;
    }

    public ValeurComptable(long id, String code, String intitule, String designation, String compte, String numero, Date date, int jour, String libelle, String lettrage, double debit, double credit, long compteTiers, String tableTiers) {
        this(id, code, intitule);
        this.designation = designation;
        this.compte = compte;
        this.numero = numero;
        this.date = date;
        this.jour = jour;
        this.libelle = libelle;
        this.lettrage = lettrage;
        this.debit = debit;
        this.credit = credit;
        this.compteTiers = compteTiers;
        this.tableTiers = tableTiers;
    }

    public ValeurComptable(long id, String code, String intitule, String compte, String designation, String numero, Date date, int jour, String libelle, String lettrage, String journal, double debit, double credit, long compteTiers, String tableTiers, boolean solde) {
        this(id, code, intitule, designation, compte, numero, date, jour, libelle, lettrage, debit, credit, compteTiers, tableTiers);
        this.journal = journal;
        this.solde = solde;
    }

    public ValeurComptable(long id, String code, String intitule, String compte, String designation, String numero, Date date, int jour, String libelle, String lettrage, String journal, double debit, double credit, long compteTiers, String tableTiers, boolean solde, double soldeProg) {
        this(id, code, intitule, compte, designation, numero, date, jour, libelle, lettrage, journal, debit, credit, compteTiers, tableTiers, solde);
        this.soldeProg = soldeProg;
    }

    public ValeurComptable(long id, String code, String intitule, long piece, String reference, Date datePiece, long compteGeneral, String compte, String designation, long compteTiers, String nomPrenom, long plan, String codePlan, String libelle, int jour, String description, String lettrage, double debit, double credit, String tableTiers, String numero) {
        this(id, code, intitule, designation, compte, numero, datePiece, jour, libelle, lettrage, debit, credit, compteTiers, tableTiers);
        this.piece = piece;
        this.compteGeneral = compteGeneral;
        this.plan = plan;
        this.codePlan = codePlan;
        this.reference = reference;
        this.description = description;
        this.intitule = intitule;
        this.nomPrenom = nomPrenom;
    }

    public long getPiece() {
        return piece;
    }

    public void setPiece(long piece) {
        this.piece = piece;
    }

    public long getCompteGeneral() {
        return compteGeneral;
    }

    public void setCompteGeneral(long compteGeneral) {
        this.compteGeneral = compteGeneral;
    }

    public long getPlan() {
        return plan;
    }

    public void setPlan(long plan) {
        this.plan = plan;
    }

    public String getCodePlan() {
        return codePlan;
    }

    public void setCodePlan(String codePlan) {
        this.codePlan = codePlan;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getCompte() {
        return compte != null ? compte : "";
    }

    public void setCompte(String compte) {
        this.compte = compte;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getJour() {
        return jour;
    }

    public void setJour(int jour) {
        this.jour = jour;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getLettrage() {
        return lettrage;
    }

    public void setLettrage(String lettrage) {
        this.lettrage = lettrage;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public double getSoldeProg() {
        return soldeProg;
    }

    public void setSoldeProg(double soldeProg) {
        this.soldeProg = soldeProg;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public String getTableTiers() {
        return tableTiers;
    }

    public void setTableTiers(String tableTiers) {
        this.tableTiers = tableTiers;
    }

    public long getCompteTiers() {
        return compteTiers;
    }

    public void setCompteTiers(long compteTiers) {
        this.compteTiers = compteTiers;
    }

    public boolean isSolde() {
        return solde;
    }

    public void setSolde(boolean solde) {
        this.solde = solde;
    }

    public List<ValeurComptable> getSous() {
        return sous;
    }

    public void setSous(List<ValeurComptable> sous) {
        this.sous = sous;
    }

    public double getSoldeDebit() {
        return soldeDebit;
    }

    public void setSoldeDebit(double soldeDebit) {
        this.soldeDebit = soldeDebit;
    }

    public double getSoldeCredit() {
        return soldeCredit;
    }

    public void setSoldeCredit(double soldeCredit) {
        this.soldeCredit = soldeCredit;
    }

    public double getDebitPeriodComptaResume() {
        return debitPeriodComptaResume;
    }

    public void setDebitPeriodComptaResume(double debitPeriodComptaResume) {
        this.debitPeriodComptaResume = debitPeriodComptaResume;
    }

    public double getCreditPeriodComptaResume() {
        return creditPeriodComptaResume;
    }

    public void setCreditPeriodComptaResume(double creditPeriodComptaResume) {
        this.creditPeriodComptaResume = creditPeriodComptaResume;
    }

    public List<YvsComptaContentJournal> getContents() {
        return contents;
    }

    public void setContents(List<YvsComptaContentJournal> contents) {
        this.contents = contents;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 89 * hash + Objects.hashCode(this.code);
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
        final ValeurComptable other = (ValeurComptable) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return true;
    }

    public Object valeur(String type) {
        if (type != null) {
            switch (type) {
                case "credit":
                    return credit;
                case "debit":
                    return debit;
                default:
                    return 0;
            }
        }
        return 0;
    }

    public double valueSoldeProg(ValeurComptable item) {
        try {
            double debit = item.getDebit();
            double credit = item.getCredit();
            int index = getSous().indexOf(item);
            if (index == 0) {
                soldeProg = 0;
            }
            soldeProg += (debit - credit);
            return soldeProg;
        } catch (Exception ex) {
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}

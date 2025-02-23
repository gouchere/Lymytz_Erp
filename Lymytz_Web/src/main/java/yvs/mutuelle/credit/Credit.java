/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.credit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.mutuelle.credit.YvsMutAvaliseCredit;
import yvs.entity.mutuelle.credit.YvsMutConditionCredit;
import yvs.entity.mutuelle.credit.YvsMutReglementCredit;
import yvs.entity.mutuelle.credit.YvsMutVoteValidationCredit;
import yvs.entity.mutuelle.echellonage.YvsMutEchellonage;
import yvs.entity.mutuelle.echellonage.YvsMutMensualite;
import yvs.mutuelle.CaisseMutuelle;
import yvs.mutuelle.Compte;
import yvs.mutuelle.echellonage.Echellonage;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Credit implements Serializable {

    private long id;
    private String reference;
    private String motifRefus;
    private Date dateCredit = new Date();
    private Date heureCredit = new Date();
    private Date dateEffet = new Date();
    private Date dateSoumission = new Date();
    private Date dateSave = new Date();
    private double montant, montantVerse, montantTotal, montantReste, montantEncaisse, montantRestant;
    private String etatValidation=Constantes.ETAT_EDITABLE;
    private char statutRemboursement=Constantes.STATUT_DOC_ATTENTE, statutPaiement=Constantes.STATUT_DOC_ATTENTE;
    private boolean selectActif, new_, update = true, visibleVote, automatique;
    private long nombreVoteRequis = 0;
    private String nameApprouve;
    private String nameDeapprouve;
    private double mensualite;
    private int duree;
    private double fraisAdditionnel = 0;
    private double montantDispo;       //montant de l'epargne
    private Echellonage echeancier;

    private TypeCredit type = new TypeCredit();
    private Compte compte = new Compte();
    private CaisseMutuelle caisse = new CaisseMutuelle();
    private List<YvsMutAvaliseCredit> avalises;
    private List<YvsMutEchellonage> echeanciers;
    private List<YvsMutConditionCredit> conditions;
    private List<YvsMutVoteValidationCredit> votes;
    private List<YvsMutVoteValidationCredit> votesApprouve;
    private List<YvsMutVoteValidationCredit> votesDeapprouve;
    private List<YvsMutReglementCredit> reglements;
    private List<YvsMutMensualite> mensualites;

    public Credit() {
        avalises = new ArrayList<>();
        reglements = new ArrayList<>();
        conditions = new ArrayList<>();
        echeanciers = new ArrayList<>();
        votes = new ArrayList<>();
        votesApprouve = new ArrayList<>();
        votesDeapprouve = new ArrayList<>();
        mensualites = new ArrayList<>();
        Calendar first = Calendar.getInstance();
        first.setTime(new Date());
        first.set(Calendar.DAY_OF_MONTH, 1);
        first.add(Calendar.MONTH, 1);
        dateEffet = first.getTime();
    }

    public Credit(long id) {
        this();
        this.id = id;
    }

    public double getMontantEncaisse() {
        return montantEncaisse;
    }

    public void setMontantEncaisse(double montantEncaisse) {
        this.montantEncaisse = montantEncaisse;
    }

    public double getMontantRestant() {
        return montantRestant;
    }

    public void setMontantRestant(double montantRestant) {
        this.montantRestant = montantRestant;
    }

    public List<YvsMutMensualite> getMensualites() {
        return mensualites != null ? mensualites : new ArrayList<YvsMutMensualite>();
    }

    public void setMensualites(List<YvsMutMensualite> mensualites) {
        this.mensualites = mensualites;
    }

    public double getMontantReste() {
//        montantReste=0;
//        if (reglements != null) {
//            for (YvsMutReglementCredit re : reglements) {
//                if (re.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
//                    montantReste += re.getMontant();
//                }
//            }
//        }
        return montant - getMontantPaye() - ((echeancier!=null)?(echeancier.isCreditRetainsInteret()?echeancier.getMontantInteret():0):0);
    }
    /* Crédit déjà versé au mutualiste*/
    public double getMontantPaye() {
        double val=0;
        if (reglements != null) {
            for (YvsMutReglementCredit re : reglements) {
                if (re.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    val += re.getMontant();
                }
            }
        }
        return val;
    }

    public void setMontantReste(double montantReste) {
        this.montantReste = montantReste;
    }

    public List<YvsMutReglementCredit> getReglements() {
        return reglements != null ? reglements : new ArrayList<YvsMutReglementCredit>();
    }

    public void setReglements(List<YvsMutReglementCredit> reglements) {
        this.reglements = reglements;
    }

    public double getFraisAdditionnel() {
        return fraisAdditionnel;
    }

    public void setFraisAdditionnel(double fraisAdditionnel) {
        this.fraisAdditionnel = fraisAdditionnel;
    }

    public long getNombreVoteRequis() {
        return nombreVoteRequis;
    }

    public void setNombreVoteRequis(long nombreVoteRequis) {
        this.nombreVoteRequis = nombreVoteRequis;
    }

    public char getStatutPaiement() {
        return statutPaiement != ' ' ? String.valueOf(statutPaiement).trim().length() > 0 ? statutPaiement : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatutPaiement(char statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public char getStatutRemboursement() {
        return statutRemboursement != ' ' ? String.valueOf(statutRemboursement).trim().length() > 0 ? statutRemboursement : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatutRemboursement(char statutRemboursement) {
        this.statutRemboursement = statutRemboursement;
    }

    public Date getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(Date dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public String getNameApprouve() {
        return nameApprouve;
    }

    public void setNameApprouve(String nameApprouve) {
        this.nameApprouve = nameApprouve;
    }

    public String getNameDeapprouve() {
        return nameDeapprouve;
    }

    public void setNameDeapprouve(String nameDeapprouve) {
        this.nameDeapprouve = nameDeapprouve;
    }

    public List<YvsMutVoteValidationCredit> getVotes() {
        return votes != null ? votes : new ArrayList<YvsMutVoteValidationCredit>();
    }

    public void setVotes(List<YvsMutVoteValidationCredit> votes) {
        this.votes = votes;
    }

    public List<YvsMutVoteValidationCredit> getVotesApprouve() {
        return votesApprouve != null ? votesApprouve : new ArrayList<YvsMutVoteValidationCredit>();
    }

    public void setVotesApprouve(List<YvsMutVoteValidationCredit> votesApprouve) {
        this.votesApprouve = votesApprouve;
    }

    public List<YvsMutVoteValidationCredit> getVotesDeapprouve() {
        return votesDeapprouve != null ? votesDeapprouve : new ArrayList<YvsMutVoteValidationCredit>();
    }

    public void setVotesDeapprouve(List<YvsMutVoteValidationCredit> votesDeapprouve) {
        this.votesDeapprouve = votesDeapprouve;
    }

    public boolean isVisibleVote() {
        return visibleVote;
    }

    public void setVisibleVote(boolean visibleVote) {
        this.visibleVote = visibleVote;
    }

    public double getMensualite() {
        return mensualite;
    }

    public void setMensualite(double mensualite) {
        this.mensualite = mensualite;
    }

    public List<YvsMutConditionCredit> getConditions() {
        return conditions != null ? conditions : new ArrayList<YvsMutConditionCredit>();
    }

    public void setConditions(List<YvsMutConditionCredit> conditions) {
        this.conditions = conditions;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public Date getDateEffet() {
        return dateEffet != null ? dateEffet : new Date();
    }

    public void setDateEffet(Date dateEffet) {
        this.dateEffet = dateEffet;
    }

    public double getMontantDispo() {
        return montantDispo;
    }

    public void setMontantDispo(double montantDispo) {
        this.montantDispo = montantDispo;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public List<YvsMutEchellonage> getEcheanciers() {
        return echeanciers != null ? echeanciers : new ArrayList<YvsMutEchellonage>();
    }

    public void setEcheanciers(List<YvsMutEchellonage> echeanciers) {
        this.echeanciers = echeanciers;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public double getMontantVerse() {
        return montantVerse;
    }

    public void setMontantVerse(double montantVerse) {
        this.montantVerse = montantVerse;
    }

    public String getMotifRefus() {
        return motifRefus;
    }

    public void setMotifRefus(String motifRefus) {
        this.motifRefus = motifRefus;
    }

    public boolean isNew_() {
        return new_;
    }

    public Date getHeureCredit() {
        return heureCredit;
    }

    public void setHeureCredit(Date heureCredit) {
        this.heureCredit = heureCredit;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDateCredit() {
        return dateCredit;
    }

    public void setDateCredit(Date dateCredit) {
        this.dateCredit = dateCredit;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getEtatValidation() {
        return etatValidation != null ? etatValidation.trim().length() > 0 ? etatValidation : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setEtatValidation(String etatValidation) {
        this.etatValidation = etatValidation;
    }

    public TypeCredit getType() {
        return type;
    }

    public void setType(TypeCredit type) {
        this.type = type;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public List<YvsMutAvaliseCredit> getAvalises() {
        return avalises != null ? avalises : new ArrayList<YvsMutAvaliseCredit>();
    }

    public void setAvalises(List<YvsMutAvaliseCredit> avalises) {
        this.avalises = avalises;
    }

    public CaisseMutuelle getCaisse() {
        return caisse;
    }

    public void setCaisse(CaisseMutuelle caisse) {
        this.caisse = caisse;
    }

    public boolean isAutomatique() {
        return automatique;
    }

    public void setAutomatique(boolean automatique) {
        this.automatique = automatique;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Echellonage getEcheancier() {
        return echeancier;
    }

    public void setEcheancier(Echellonage echeancier) {
        this.echeancier = echeancier;
    }
    
    public long getNombreVoteApprouve(){
        long nb=0;
        for(YvsMutVoteValidationCredit v:votesApprouve){
            if(v.getAccepte()){nb++;}
        }
        return nb;
    }

    public double getSoeAvalise() {
        double re = 0;
        if (avalises != null) {
            for (YvsMutAvaliseCredit a : avalises) {
                re += a.getMontant();
            }
        }
        return re;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Credit other = (Credit) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public void loadVotes() {
        votesApprouve.clear();
        votesDeapprouve.clear();

        nameApprouve = "";
        nameDeapprouve = "";

        if (getVotes() != null) {
            for (YvsMutVoteValidationCredit v : getVotes()) {
                if (v.getAccepte()) { 
                    if (v.getMutualiste() != null ? v.getMutualiste().getEmploye() != null : false) {
                        if (votesApprouve.isEmpty()) {
                            nameApprouve = v.getMutualiste().getEmploye().getNom_prenom();
                        } else {
                            nameApprouve += " - " + v.getMutualiste().getEmploye().getNom_prenom();
                        }
                    }
                    votesApprouve.add(v);
                } else {
                    if (v.getMutualiste() != null ? v.getMutualiste().getEmploye() != null : false) {
                        if (votesDeapprouve.isEmpty()) {
                            nameDeapprouve = v.getMutualiste().getEmploye().getNom_prenom();
                        } else {
                            nameDeapprouve += " - " + v.getMutualiste().getEmploye().getNom_prenom();
                        }
                    }
                    votesDeapprouve.add(v);
                }
            }
        }
    }
}

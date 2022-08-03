/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.ModeDeReglement;
import yvs.base.tresoreri.ModePaiement;
import yvs.comptabilite.caisse.Caisses;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.salaire.YvsGrhBulletins;
import yvs.grh.Calendrier;
import yvs.grh.contrat.FinDeContrats;
import yvs.grh.paie.StructureElementSalaire;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ContratEmploye implements Serializable {

    private long id;
    private TypeContrat typeContrat = new TypeContrat();
    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private Date finEssai = new Date();
    private ModeDeReglement modePaiement = new ModeDeReglement();
    private Caisses caisse = new Caisses();
    private double salaireHoraire;
    private double salaireMensuel, congeAcquis;
    private double horaireHebdo, horaireMensuel; //inutilisé actuellement, on lui préfère un calendrier hebdomadaire de travail
    private String fichier = "\\Path\\", strHoraireHebdo, statut = "W";
    private boolean actif;
    private Employe employe = new Employe();
    private StructureElementSalaire structSalaire = new StructureElementSalaire();
    private List<YvsGrhElementAdditionel> primes, primesCumule;
    private List<YvsGrhElementAdditionel> retenus, retenusCumule;
    private List<YvsGrhElementAdditionel> gains, gainsCumule;
    private List<YvsGrhBulletins> bulletins;
    private Calendrier calendrier = new Calendrier();
    private RegleDeTache regleTache = new RegleDeTache();
    private String reference;
    private int delaisALert;
    private int dureePreavie;
    private String unitePreavis, unitePreavisView = "Jour";    //jour ou mois
    private double dureeMajoration;
    private FinDeContrats contratS;
    private String typeTranche;
    private boolean accesRestreint;
    private String sourceFirstConge = "DEX";
    private Date dateFirstConge = new Date();
    private double salaireCumule;
    private double montantRetenu;
    private double montantRetenuRegle;
    private double montantRetenuEncours;
    private double montantRetenuReste;

    public ContratEmploye() {
        primes = new ArrayList<>();
        primesCumule = new ArrayList<>();
        gains = new ArrayList<>();
        gainsCumule = new ArrayList<>();
        retenus = new ArrayList<>();
        retenusCumule = new ArrayList<>();
        bulletins = new ArrayList<>();
    }

    public String getStatut() {
        return statut != null ? statut.trim().length() > 0 ? statut : "W" : "W";
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Caisses getCaisse() {
        return caisse;
    }

    public void setCaisse(Caisses caisse) {
        this.caisse = caisse;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getUnitePreavisView() {
        return unitePreavisView != null ? unitePreavisView.trim().length() > 0 ? unitePreavisView : "Jour" : "Jour";
    }

    public void setUnitePreavisView(String unitePreavisView) {
        this.unitePreavisView = unitePreavisView;
    }

    public List<YvsGrhElementAdditionel> getPrimesCumule() {
        return primesCumule;
    }

    public void setPrimesCumule(List<YvsGrhElementAdditionel> primesCumule) {
        this.primesCumule = primesCumule;
    }

    public List<YvsGrhElementAdditionel> getRetenusCumule() {
        return retenusCumule;
    }

    public void setRetenusCumule(List<YvsGrhElementAdditionel> retenusCumule) {
        this.retenusCumule = retenusCumule;
    }

    public List<YvsGrhElementAdditionel> getGainsCumule() {
        return gainsCumule;
    }

    public void setGainsCumule(List<YvsGrhElementAdditionel> gainsCumule) {
        this.gainsCumule = gainsCumule;
    }

    public double getMontantRetenuEncours() {
        return montantRetenuEncours;
    }

    public void setMontantRetenuEncours(double montantRetenuEncours) {
        this.montantRetenuEncours = montantRetenuEncours;
    }

    public double getMontantRetenu() {
        return montantRetenu;
    }

    public void setMontantRetenu(double montantRetenu) {
        this.montantRetenu = montantRetenu;
    }

    public double getMontantRetenuRegle() {
        return montantRetenuRegle;
    }

    public void setMontantRetenuRegle(double montantRetenuRegle) {
        this.montantRetenuRegle = montantRetenuRegle;
    }

    public double getMontantRetenuReste() {
        return montantRetenuReste;
    }

    public void setMontantRetenuReste(double montantRetenuReste) {
        this.montantRetenuReste = montantRetenuReste;
    }

    public List<YvsGrhElementAdditionel> getGains() {
        return gains;
    }

    public void setGains(List<YvsGrhElementAdditionel> gains) {
        this.gains = gains;
    }

    public List<YvsGrhElementAdditionel> getRetenus() {
        return retenus;
    }

    public void setRetenus(List<YvsGrhElementAdditionel> retenus) {
        this.retenus = retenus;
    }

    public double getSalaireCumule() {
        return salaireCumule;
    }

    public void setSalaireCumule(double salaireCumule) {
        this.salaireCumule = salaireCumule;
    }

    public List<YvsGrhBulletins> getBulletins() {
        return bulletins;
    }

    public void setBulletins(List<YvsGrhBulletins> bulletins) {
        this.bulletins = bulletins;
    }

    public String getSourceFirstConge() {
        return sourceFirstConge;
    }

    public void setSourceFirstConge(String sourceFirstConge) {
        this.sourceFirstConge = sourceFirstConge;
    }

    public Date getDateFirstConge() {
        return dateFirstConge;
    }

    public void setDateFirstConge(Date dateFirstConge) {
        this.dateFirstConge = dateFirstConge;
    }

    public ContratEmploye(long id) {
        this.id = id;
        primes = new ArrayList<>();
    }

    public int getDelaisALert() {
        return delaisALert;
    }

    public void setDelaisALert(int delaisALert) {
        this.delaisALert = delaisALert;
    }

    public ContratEmploye(long id, String reference) {
        this.id = id;
        this.reference = reference;
        primes = new ArrayList<>();
    }

    public RegleDeTache getRegleTache() {
        return regleTache;
    }

    public void setRegleTache(RegleDeTache regleTache) {
        this.regleTache = regleTache;
    }

    public double getSalaireHoraire() {
        return salaireHoraire;
    }

    public void setSalaireHoraire(double salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    public double getSalaireMensuel() {
        return salaireMensuel;
    }

    public void setSalaireMensuel(double salaireMensuel) {
        this.salaireMensuel = salaireMensuel;
    }

    public double getHoraireHebdo() {
        return horaireHebdo;
    }

    public void setHoraireHebdo(double horaireHebdo) {
        this.horaireHebdo = horaireHebdo;
    }

    public double getHoraireMensuel() {
        return horaireMensuel;
    }

    public void setHoraireMensuel(double horaireMensuel) {
        this.horaireMensuel = horaireMensuel;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<YvsGrhElementAdditionel> getPrimes() {
        return primes;
    }

    public Calendrier getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(Calendrier calendrier) {
        this.calendrier = calendrier;
    }

    public void setPrimes(List<YvsGrhElementAdditionel> primes) {
        this.primes = primes;
    }

    public StructureElementSalaire getStructSalaire() {
        return structSalaire;
    }

    public void setStructSalaire(StructureElementSalaire structSalaire) {
        this.structSalaire = structSalaire;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TypeContrat getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(TypeContrat typeContrat) {
        this.typeContrat = typeContrat;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getFinEssai() {
        return finEssai;
    }

    public void setFinEssai(Date finEssai) {
        this.finEssai = finEssai;
    }

    public ModeDeReglement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModeDeReglement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public double getCongeAcquis() {
        return congeAcquis;
    }

    public void setCongeAcquis(double congeAcquis) {
        this.congeAcquis = congeAcquis;
    }

    public String getStrHoraireHebdo() {
        return strHoraireHebdo;
    }

    public void setStrHoraireHebdo(String strHoraireHebdo) {
        this.strHoraireHebdo = strHoraireHebdo;
    }

    public int getDureePreavie() {
        return dureePreavie;
    }

    public void setDureePreavie(int dureePreavie) {
        this.dureePreavie = dureePreavie;
    }

    public String getUnitePreavis() {
        return unitePreavis;
    }

    public void setUnitePreavis(String unitePreavis) {
        this.unitePreavis = unitePreavis;
    }

    public double getDureeMajoration() {
        return dureeMajoration;
    }

    public void setDureeMajoration(double dureeMajoration) {
        this.dureeMajoration = dureeMajoration;
    }

    public FinDeContrats getContratS() {
        return contratS;
    }

    public void setContratS(FinDeContrats contratS) {
        this.contratS = contratS;
    }

    public String getTypeTranche() {
        return typeTranche;
    }

    public void setTypeTranche(String typeTranche) {
        this.typeTranche = typeTranche;
    }

    public boolean isAccesRestreint() {
        return accesRestreint;
    }

    public void setAccesRestreint(boolean accesRestreint) {
        this.accesRestreint = accesRestreint;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.id);
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
        final ContratEmploye other = (ContratEmploye) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}

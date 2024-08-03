/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.poste;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.BeanDeBase;
import yvs.base.tresoreri.ModePaiement;
import yvs.entity.grh.param.poste.YvsGrhQualificationPoste;
import yvs.grh.bean.Diplomes;
import yvs.grh.bean.Employe;
import yvs.grh.bean.mission.GrilleFraisMission; 
import yvs.grh.bean.RegleDeTache;
import yvs.grh.contrat.ModelContrat;
import yvs.grh.paie.StructureElementSalaire;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class PosteDeTravail extends BeanDeBase implements Serializable {

    private long id, posteActif;
    private String intitule;
    private String description;
    private int degre;
    private Diplomes niveauRequis = new Diplomes();
    private Departements departement = new Departements();
    private GrilleFraisMission FraisMission = new GrilleFraisMission();
    private List<Employe> listEmployes;
    private double etatEndettement = 0;
    private double masseSalariale = 0;
    private Integer totalEmploye = 0;
    private List<MissionPoste> missions = new ArrayList<>();
    private List<MissionPoste> activites = new ArrayList<>();
    private List<YvsGrhQualificationPoste> qualifications;
    private short nombrePlace;
    private ModePaiement modePaiement = new ModePaiement();
    private double salaireHoraire;
    private double salaireMensuel, congeAcquis;
    private List<ElementAdditionnelPoste> primes;
    private double dureePreavie;
    private String unitePreavis;    //jour ou mois
    private double dureeMajoration;
    private StructureElementSalaire structSalaire = new StructureElementSalaire();
    private RegleDeTache regle = new RegleDeTache();
    private boolean salaireTache;
    private String grade;
    private PosteDeTravail posteSuperieur;
    private ModelContrat modelContrat = new ModelContrat();
    private long idPosteSup;
    private Date dateSave = new Date();

    public PosteDeTravail() {
        listEmployes = new ArrayList<>();
        qualifications = new ArrayList<>();
        primes = new ArrayList<>();
        activites = new ArrayList<>();
        missions = new ArrayList<>();
    }

    public PosteDeTravail(long id) {
        this();
        this.id = id;
    }

    public PosteDeTravail(long id, String intitule) {
        this(id);
        this.intitule = intitule;
    }

    public PosteDeTravail(long id, String intitule, String description) {
        this(id, intitule);
        this.description = description;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public ModelContrat getModelContrat() {
        return modelContrat;
    }

    public void setModelContrat(ModelContrat modelContrat) {
        this.modelContrat = modelContrat;
    }

    public long getIdPosteSup() {
        return idPosteSup;
    }

    public void setIdPosteSup(long idPosteSup) {
        this.idPosteSup = idPosteSup;
    }

    public PosteDeTravail getPosteSuperieur() {
        return posteSuperieur;
    }

    public void setPosteSuperieur(PosteDeTravail posteSuperieur) {
        this.posteSuperieur = posteSuperieur;
    }

    public boolean isSalaireTache() {
        return salaireTache;
    }

    public void setSalaireTache(boolean salaireTache) {
        this.salaireTache = salaireTache;
    }

    public RegleDeTache getRegle() {
        return regle;
    }

    public void setRegle(RegleDeTache regle) {
        this.regle = regle;
    }

    public GrilleFraisMission getFraisMission() {
        return FraisMission;
    }

    public void setFraisMission(GrilleFraisMission FraisMission) {
        this.FraisMission = FraisMission;
    }

    public Integer getTotalEmploye() {
        return totalEmploye;
    }

    public void setTotalEmploye(Integer totalEmploye) {
        this.totalEmploye = totalEmploye;
    }

    public List<Employe> getListEmployes() {
        return listEmployes;
    }

    public void setListEmployes(List<Employe> listEmployes) {
        this.listEmployes = listEmployes;
    }

    public double getEtatEndettement() {
        return etatEndettement;
    }

    public void setEtatEndettement(double etatEndettement) {
        this.etatEndettement = etatEndettement;
    }

    public double getMasseSalariale() {
        return masseSalariale;
    }

    public void setMasseSalariale(double masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    public int getDegre() {
        return degre;
    }

    public void setDegre(int degre) {
        this.degre = degre;
    }

    public Diplomes getNiveauRequis() {
        return niveauRequis;
    }

    public void setNiveauRequis(Diplomes niveauRequis) {
        this.niveauRequis = niveauRequis;
    }

    public Departements getDepartement() {
        return departement;
    }

    public void setDepartement(Departements departement) {
        this.departement = departement;
    }

    public long getPosteActif() {
        return posteActif;
    }

    public void setPosteActif(long posteActif) {
        this.posteActif = posteActif;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MissionPoste> getMissions() {
        return missions;
    }

    public void setMissions(List<MissionPoste> missions) {
        this.missions = missions;
    }

    public List<MissionPoste> getActivites() {
        return activites;
    }

    public void setActivites(List<MissionPoste> activites) {
        this.activites = activites;
    }

    public short getNombrePlace() {
        return nombrePlace;
    }

    public void setNombrePlace(short nombrePlace) {
        this.nombrePlace = nombrePlace;
    }

    public List<YvsGrhQualificationPoste> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<YvsGrhQualificationPoste> qualifications) {
        this.qualifications = qualifications;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
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

    public double getCongeAcquis() {
        return congeAcquis;
    }

    public void setCongeAcquis(double congeAcquis) {
        this.congeAcquis = congeAcquis;
    }

    public List<ElementAdditionnelPoste> getPrimes() {
        return primes;
    }

    public void setPrimes(List<ElementAdditionnelPoste> primes) {
        this.primes = primes;
    }

    public double getDureePreavie() {
        return dureePreavie;
    }

    public void setDureePreavie(double dureePreavie) {
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

    public StructureElementSalaire getStructSalaire() {
        return structSalaire;
    }

    public void setStructSalaire(StructureElementSalaire structSalaire) {
        this.structSalaire = structSalaire;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PosteDeTravail other = (PosteDeTravail) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

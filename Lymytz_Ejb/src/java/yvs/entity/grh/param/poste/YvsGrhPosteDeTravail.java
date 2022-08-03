/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param.poste;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.grh.activite.YvsGrhGrilleMission;
import yvs.entity.grh.param.YvsGrhActivitesPoste;
import yvs.entity.grh.param.YvsGrhMissionPoste;
import yvs.entity.grh.taches.YvsGrhRegleTache;
import yvs.entity.grh.personnel.YvsDiplomes;
import yvs.entity.grh.salaire.YvsGrhStructureSalaire;
import yvs.entity.param.YvsModePaiement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_poste_de_travail")
@NamedQueries({
    @NamedQuery(name = "YvsPosteDeTravail.findAll", query = "SELECT y FROM YvsGrhPosteDeTravail y JOIN FETCH y.departement WHERE y.departement.societe=:societe ORDER BY y.intitule ASC"),
    @NamedQuery(name = "YvsPosteDeTravail.findAllActif", query = "SELECT y FROM YvsGrhPosteDeTravail y JOIN FETCH y.departement WHERE y.departement.societe=:societe AND y.actif=true AND y.departement.actif=true ORDER BY y.intitule ASC"),
    @NamedQuery(name = "YvsPosteDeTravail.findById", query = "SELECT y FROM YvsGrhPosteDeTravail y WHERE y.id = :id"),
    @NamedQuery(name = "YvsPosteDeTravail.findByNotId", query = "SELECT y FROM YvsGrhPosteDeTravail y WHERE y.id != :id AND y.departement.societe=:societe ORDER BY y.intitule ASC"),
    @NamedQuery(name = "YvsPosteDeTravail.findByIntitule", query = "SELECT y FROM YvsGrhPosteDeTravail y WHERE y.intitule = :intitule AND y.departement.societe=:societe ORDER BY y.intitule"),
    @NamedQuery(name = "YvsPosteDeTravail.findByIntituleLike", query = "SELECT y FROM YvsGrhPosteDeTravail y WHERE (y.intitule LIKE :codePoste OR y.departement.intitule LIKE :codePoste) AND y.actif=true "
            + "AND y.departement.societe=:societe ORDER BY y.intitule ASC"),
    @NamedQuery(name = "YvsPosteDeTravail.findByDepartement", query = "SELECT y FROM YvsGrhPosteDeTravail y WHERE y.departement = :departement AND y.actif=true ORDER BY y.intitule ASC"),
    @NamedQuery(name = "YvsPosteDeTravail.findBySupp", query = "SELECT y FROM YvsGrhPosteDeTravail y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsPosteDeTravail.findBySuperieur", query = "SELECT y FROM YvsGrhPosteDeTravail y WHERE y.posteSuperieur = :superieur"),
    @NamedQuery(name = "YvsPosteDeTravail.findByCode", query = "SELECT y FROM YvsGrhPosteDeTravail y WHERE y.intitule LIKE :intitule AND y.departement.societe=:societe"),
    @NamedQuery(name = "YvsPosteDeTravail.findByDescriptionPoste", query = "SELECT y FROM YvsGrhPosteDeTravail y WHERE y.descriptionPoste = :descriptionPoste")})
public class YvsGrhPosteDeTravail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_poste_de_travail_id_seq", name = "yvs_poste_de_travail_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_poste_de_travail_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "remuneration_sur_objectif")
    private Boolean remunerationSurObjectif;
    @Column(name = "duree_preavis")
    private Double dureePreavis;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 2147483647)
    @Column(name = "intitule")
    private String intitule;
    @Column(name = "nombre_place")
    private Short nombrePlace;
    @Column(name = "supp")
    private Boolean supp;
    @Size(max = 2147483647)
    @Column(name = "description_poste")
    private String descriptionPoste;
    @Column(name = "grade")
    private String grade;
    @Column(name = "salaire_horaire")
    private Double salaireHoraire;
    @Column(name = "salaire_mensuel")
    private Double salaireMensuel;
    @Column(name = "conge_acquis")
    private Double congeAcquis;
    @Column(name = "unite_duree_preavis")
    private String uniteDureePreavis;    //jour ou mois
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "departement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhDepartement departement;
    @JoinColumn(name = "niveau", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDiplomes niveau;
    @JoinColumn(name = "mode_paiement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsModePaiement modePaiement;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "regle_tache", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhRegleTache regleTache;
    @JoinColumn(name = "poste_superieur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPosteDeTravail posteSuperieur;
    @JoinColumn(name = "model_contrat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhModelContrat modelContrat;
    @JoinColumn(name = "structure_salaire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhStructureSalaire structureSalaire;
    @JoinColumn(name = "poste_equivalent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPosteDeTravail posteEquivalent;
    @JoinColumn(name = "frais_mission", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhGrilleMission fraisMission;

    @OneToMany(mappedBy = "poste", fetch = FetchType.LAZY)
    private List<YvsGrhQualificationPoste> qualifications;
    @OneToMany(mappedBy = "poste", fetch = FetchType.LAZY)
    private List<YvsGrhPosteEmployes> postesEmployes;
    @OneToMany(mappedBy = "poste")
    private List<YvsGrhMissionPoste> missions;
    @OneToMany(mappedBy = "poste")
    private List<YvsGrhActivitesPoste> activites;
    @OneToMany(mappedBy = "posteSuperieur")
    private List<YvsGrhPosteDeTravail> listPostesInferieur;

    public YvsGrhPosteDeTravail() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsGrhPosteDeTravail(Long id) {
        this();
        this.id = id;
    }

    public YvsGrhPosteDeTravail(Long id, String intitule) {
        this(id);
        this.intitule = intitule;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsGrhModelContrat getModelContrat() {
        return modelContrat;
    }

    public void setModelContrat(YvsGrhModelContrat modelContrat) {
        this.modelContrat = modelContrat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public void setNombrePlace(Short nombrePlace) {
        this.nombrePlace = nombrePlace;
    }

    public Short getNombrePlace() {
        return nombrePlace != null ? nombrePlace : 0;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Boolean getSupp() {
        return supp != null ? supp : false;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public String getDescriptionPoste() {
        return descriptionPoste;
    }

    public void setDescriptionPoste(String descriptionPoste) {
        this.descriptionPoste = descriptionPoste;
    }

    public YvsGrhDepartement getDepartement() {
        return departement;
    }

    public void setDepartement(YvsGrhDepartement departement) {
        this.departement = departement;
    }

    public YvsDiplomes getNiveau() {
        return niveau;
    }

    public void setNiveau(YvsDiplomes niveau) {
        this.niveau = niveau;
    }

    public YvsGrhGrilleMission getFraisMission() {
        return fraisMission;
    }

    public void setFraisMission(YvsGrhGrilleMission fraisMission) {
        this.fraisMission = fraisMission;
    }

    public YvsModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(YvsModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public Double getSalaireHoraire() {
        return salaireHoraire != null ? salaireHoraire : 0;
    }

    public void setSalaireHoraire(Double salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    public Double getSalaireMensuel() {
        return salaireMensuel != null ? salaireMensuel : 0;
    }

    public void setSalaireMensuel(Double salaireMensuel) {
        this.salaireMensuel = salaireMensuel;
    }

    public Double getCongeAcquis() {
        return congeAcquis != null ? congeAcquis : 0;
    }

    public void setCongeAcquis(Double congeAcquis) {
        this.congeAcquis = congeAcquis;
    }

    public YvsGrhStructureSalaire getStructureSalaire() {
        return structureSalaire;
    }

    public void setStructureSalaire(YvsGrhStructureSalaire structureSalaire) {
        this.structureSalaire = structureSalaire;
    }

    public Double getDureePreavis() {
        return dureePreavis != null ? dureePreavis : 0;
    }

    public void setDureePreavis(Double dureePreavis) {
        this.dureePreavis = dureePreavis;
    }

    public String getUniteDureePreavis() {
        return uniteDureePreavis;
    }

    public void setUniteDureePreavis(String uniteDureePreavis) {
        this.uniteDureePreavis = uniteDureePreavis;
    }

    public YvsGrhRegleTache getRegleTache() {
        return regleTache;
    }

    public void setRegleTache(YvsGrhRegleTache regleTache) {
        this.regleTache = regleTache;
    }

    public YvsGrhPosteDeTravail getPosteSuperieur() {
        return posteSuperieur;
    }

    public void setPosteSuperieur(YvsGrhPosteDeTravail posteSuperieur) {
        this.posteSuperieur = posteSuperieur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsGrhPosteDeTravail)) {
            return false;
        }
        YvsGrhPosteDeTravail other = (YvsGrhPosteDeTravail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsPosteDeTravail[ id=" + id + " ]";
    }

    public List<YvsGrhQualificationPoste> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<YvsGrhQualificationPoste> qualifications) {
        this.qualifications = qualifications;
    }

    public List<YvsGrhPosteEmployes> getPostesEmployes() {
        return postesEmployes;
    }

    public void setPostesEmployes(List<YvsGrhPosteEmployes> postesEmployes) {
        this.postesEmployes = postesEmployes;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsGrhPosteDeTravail getPosteEquivalent() {
        return posteEquivalent;
    }

    public void setPosteEquivalent(YvsGrhPosteDeTravail posteEquivalent) {
        this.posteEquivalent = posteEquivalent;
    }

    public List<YvsGrhMissionPoste> getMissions() {
        return missions;
    }

    public void setMissions(List<YvsGrhMissionPoste> missions) {
        this.missions = missions;
    }

    public List<YvsGrhActivitesPoste> getActivites() {
        return activites;
    }

    public void setActivites(List<YvsGrhActivitesPoste> activites) {
        this.activites = activites;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getRemunerationSurObjectif() {
        return remunerationSurObjectif;
    }

    public void setRemunerationSurObjectif(Boolean remunerationSurObjectif) {
        this.remunerationSurObjectif = remunerationSurObjectif;
    }

    public List<YvsGrhPosteDeTravail> getListPostesInferieur() {
        return listPostesInferieur;
    }

    public void setListPostesInferieur(List<YvsGrhPosteDeTravail> listPostesInferieur) {
        this.listPostesInferieur = listPostesInferieur;
    }

    }

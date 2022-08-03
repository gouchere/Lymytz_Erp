/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import static yvs.dao.salaire.service.Constantes.dfs;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.workflow.YvsWorkflowValidConge;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_conge_emps")
@NamedQueries({
    @NamedQuery(name = "YvsGrhCongeEmps.findAll", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.employe.agence=:agence ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByIds", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.id in :ids"),
    @NamedQuery(name = "YvsGrhCongeEmps.findAlls", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.employe.agence.societe=:agence ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsGrhCongeEmps.CountAll", query = "SELECT COUNT(y) FROM YvsGrhCongeEmps y WHERE y.employe.agence=:agence"),
    @NamedQuery(name = "YvsGrhCongeEmps.CountAlls", query = "SELECT COUNT(y) FROM YvsGrhCongeEmps y WHERE y.employe.agence.societe=:agence"),
    @NamedQuery(name = "YvsGrhCongeEmps.findAllSte", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.employe.agence.societe = :societe AND y.dateDebut <= :date AND y.dateFin>=:date AND y.employe.actif=true"),
    @NamedQuery(name = "YvsGrhCongeEmps.findById", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhCongeEmps.CountByService", query = "SELECT COUNT(y) FROM YvsGrhCongeEmps y WHERE y.employe.posteActif.departement.id IN :departements"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByService", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.employe.posteActif.departement.id IN :departements"),

    //retourne le nombre d'employés en congé
    @NamedQuery(name = "YvsGrhCongeEmps.FindEnCongeC", query = "SELECT COUNT(y) FROM YvsGrhCongeEmps y WHERE y.statut=:statut AND (:date BETWEEN y.dateDebut AND y.dateFin) AND y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhCongeEmps.FindACloture", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.statut=:statut AND y.dateFin>:date AND y.actif=true AND y.employe.agence.societe=:societe ORDER BY y.dateFin DESC"),
    @NamedQuery(name = "YvsGrhCongeEmps.FindAClotureC", query = "SELECT COUNT(y) FROM YvsGrhCongeEmps y WHERE y.statut=:statut AND y.dateFin>:date AND y.actif=true AND y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhCongeEmps.findLastCongeAPaye", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.employe =:employe AND y.statut=:statut ORDER BY y.dateFin DESC"),
    @NamedQuery(name = "YvsGrhCongeEmps.findCongeAPaye", query = "SELECT COUNT(y) FROM YvsGrhCongeEmps y WHERE y.employe = :employe AND y.datePaiementAlloc BETWEEN :debut AND :fin"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByEmpsAndDate", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.dateDebut <= :date AND y.dateFin>=:date AND y.statut=:statut AND y.employe=:employe"),
    @NamedQuery(name = "YvsGrhCongeEmps.findIdEmpInPeriode", query = "SELECT y.employe.id FROM YvsGrhCongeEmps y WHERE ((y.dateDebut BETWEEN :debut AND :fin) OR (y.dateFin BETWEEN :debut AND :fin)) AND y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByDate", query = "SELECT y FROM YvsGrhCongeEmps y WHERE (:date BETWEEN y.dateDebut AND y.dateFin ) AND y.employe.agence=:agence"),
    @NamedQuery(name = "YvsGrhCongeEmps.CountByDate", query = "SELECT COUNT(y) FROM YvsGrhCongeEmps y WHERE (:date BETWEEN y.dateDebut AND y.dateFin ) AND y.employe.agence=:agence"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByDates", query = "SELECT y FROM YvsGrhCongeEmps y WHERE (:date BETWEEN y.dateDebut AND y.dateFin) AND y.employe.agence.societe=:agence"),
    @NamedQuery(name = "YvsGrhCongeEmps.CountByDates", query = "SELECT COUNT(y) FROM YvsGrhCongeEmps y WHERE (:date BETWEEN y.dateDebut AND y.dateFin) AND y.employe.agence.societe=:agence"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByDateDebut", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByDateFin", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByMotif", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.motif = :motif"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByReference", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.referenceConge = :referenceConge"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByActif", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByEffet", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.effet = :effet"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByStatutC", query = "SELECT COUNT(y) FROM YvsGrhCongeEmps y WHERE y.statut = :statut AND y.employe.agence=:agence"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByStatut", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.statut = :statut AND y.employe.agence=:agence ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByStatutCs", query = "SELECT COUNT(y) FROM YvsGrhCongeEmps y WHERE y.statut = :statut AND y.employe.agence.societe=:agence"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByStatuts", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.statut = :statut AND y.employe.agence.societe=:agence ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByLibelle", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.libelle = :libelle"),
    //s'assurer qu'un employé n'est pas déjà en congé dans un intervalle de date spécifié
    @NamedQuery(name = "YvsGrhCongeEmps.findTrue", query = "SELECT y FROM YvsGrhCongeEmps y WHERE (y.employe.id = :employe) AND ((y.dateDebut<=:debut AND y.dateFin>=:debut) OR (y.dateDebut<=:fin AND y.dateFin >=:fin) OR (y.dateDebut>=:debut AND y.dateDebut<=:fin))"),
    //charge les congés d'un employé pour l'année en cours 
    @NamedQuery(name = "YvsGrhCongeEmps.findByEmploye", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.employe.id = :employe"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByEmployeDate", query = "SELECT y.employe FROM YvsGrhCongeEmps y WHERE (:date BETWEEN y.dateDebut AND y.dateFin) AND y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByOneCongeDate", query = "SELECT y FROM YvsGrhCongeEmps y WHERE (:date BETWEEN y.dateDebut AND y.dateFin) AND (y.statut='V' OR y.statut='C') AND y.employe=:employe"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByOneEmployeDate", query = "SELECT y.employe FROM YvsGrhCongeEmps y WHERE (:date BETWEEN y.dateDebut AND y.dateFin) AND y.employe=:employe"),
    @NamedQuery(name = "YvsGrhCongeEmps.CountByEmployeOver", query = "SELECT COUNT(y) FROM YvsGrhCongeEmps y WHERE y.employe.id IN :employe AND y.dateFin >= :dateFin"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByEmployeOver", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.employe.id IN :employe AND y.dateFin >= :dateFin"),
    @NamedQuery(name = "YvsGrhCongeEmps.findLastCongeAnnuelle", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.employe=:employe AND y.typeConge='Annuelle' ORDER BY y.dateFin DESC"),
//charge un congé d'un employé
    @NamedQuery(name = "YvsGrhCongeEmps.findOneByEmploye", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.employe.id = :employe AND y.dateDebut =:debut AND y.dateFin=:fin"),

    @NamedQuery(name = "YvsGrhCongeEmps.findLast", query = "SELECT y FROM YvsGrhCongeEmps y WHERE y.employe.agence.societe = :societe ORDER BY y.id DESC"),
    @NamedQuery(name = "YvsGrhCongeEmps.findByNotNumero", query = "SELECT y FROM YvsGrhCongeEmps y WHERE TRIM(COALESCE(y.referenceConge, '')) = '' AND y.dateDebut BETWEEN :debut AND :fin AND y.employe.agence.societe = :societe"),
    @NamedQuery(name = "YvsGrhCongeEmps.findReferenceByReference", query = "SELECT y.referenceConge FROM YvsGrhCongeEmps y WHERE y.employe.agence.societe = :societe AND y.referenceConge >:numDoc AND y.referenceConge <:numDoc1 ORDER by y.referenceConge DESC"),})
public class YvsGrhCongeEmps implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", columnDefinition = "BIGSERIAL")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_conge_emp_id_seq")
    @SequenceGenerator(sequenceName = "yvs_conge_emp_id_seq", allocationSize = 1, name = "yvs_conge_emp_id_seq")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "duree_sup")
    private Double dureeSup;
    @Column(name = "duree_permission_prise")
    private Double dureePermissionPrise;
    @Column(name = "nature")
    private Character nature; //P=permission ou C=congé
    @Column(name = "duree_permission")
    private Character dureePermission;   //C=permission corte durée L=permission longue durée
    @Column(name = "heure_debut")
    @Temporal(TemporalType.TIME)
    private Date heureDebut;
    @Column(name = "heure_fin")
    @Temporal(TemporalType.TIME)
    private Date heureFin;
    @Column(name = "duree")
    private Integer duree;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_conge")
    @Temporal(TemporalType.DATE)
    private Date dateConge;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Size(max = 2147483647)
    @Column(name = "motif")
    private String motif;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 2147483647)
    @Column(name = "effet")
    private String effet;
    @Column(name = "type_conge")
    private String typeConge;// Maternité, Anuelle, Maladie, Autre
    @Column(name = "statut")
    private Character statut;//B=brouillon, T=Transmi; V=Validé ; O=ok (Jouit)
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "date_retour")
    private Date dateRetour;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "date_fin_prevu")
    private Date dateFinPrevu;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "date_suspension")
    private Date dateSuspension;
    @Column(name = "motif_suspension")
    private String motifSuspension;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "date_paiement_alloc")
    private Date datePaiementAlloc;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "date_save")
    private Date dateSave;
    @Temporal(javax.persistence.TemporalType.TIME)
    @Column(name = "heure_retour_effectif")
    private Date heureRetourEffectif;
    @Column(name = "reference_conge")
    private String referenceConge;
    @Column(name = "compter_jour_repos")
    private Boolean compterJourRepos;
    @Column(name = "etape_total")
    private Integer etapeTotal;
    @Column(name = "etape_valide")
    private Integer etapeValide;

    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "conge")
    private List<YvsWorkflowValidConge> etapesValidations;

//    @Transient
//    private int etapeValide;
    @Transient
    private int congePrincipalDu;
    @Transient
    private int congePrincipalPris;
    @Transient
    private int dureePermPrisSpeciale;      //durée des permissions longue durée à effet spéciale
    @Transient
    private double congeSupp;
    @Transient
    private double dureePermissionAutorisePris;    //durée de permission prise à faire valoir sur le quotas de permissions autorisé
    @Transient
    private double dureePermissionAutorise;
    @Transient
    private double dureePermCD;
    @Transient
    private double dureePermCD_AN;
    @Transient
    private double dureePermCD_SP;
    @Transient
    private double dureePermCD_AU;
    @Transient
    private double dureePermCD_SAL;
    @Transient
    private boolean compteJourRepos = true;
    @Transient
    private String madateDebut;
    @Transient
    private String maDateFin;
    @Transient
    private String maDateRetour;
    @Transient
    private String maDateSave;

    public YvsGrhCongeEmps() {
    }

    public YvsGrhCongeEmps(Long id) {
        this.id = id;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Character getStatut() {
        return (statut != null) ? statut : 'W';
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateConge() {
        return dateConge;
    }

    public void setDateConge(Date dateConge) {
        this.dateConge = dateConge;
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

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getEffet() {
        return effet;
    }

    public void setEffet(String effet) {
        this.effet = effet;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public Character getDureePermission() {
        return dureePermission != null ? dureePermission : ' ';
    }

    public void setDureePermission(Character dureePermission) {
        this.dureePermission = dureePermission;
    }

    public String getTypeConge() {
        return typeConge;
    }

    public void setTypeConge(String typeConge) {
        this.typeConge = typeConge;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Integer getDuree() {
        return duree != null ? duree : 0;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsWorkflowValidConge> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidConge> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public Integer getEtapeValide() {
        return etapeValide != null ? etapeValide : 0;
    }

    public void setEtapeValide(Integer etapeValide) {
        this.etapeValide = etapeValide;
    }

    public Integer getEtapeTotal() {
        return etapeTotal != null ? etapeTotal : 0;
    }

    public void setEtapeTotal(Integer etapeTotal) {
        this.etapeTotal = etapeTotal;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }

    public String getReferenceConge() {
        return referenceConge;
    }

    public void setReferenceConge(String referenceConge) {
        this.referenceConge = referenceConge;
    }

    public Boolean getCompterJourRepos() {
        return compterJourRepos != null ? compterJourRepos : false;
    }

    public void setCompterJourRepos(Boolean compterJourRepos) {
        this.compterJourRepos = compterJourRepos;
    }

    public Character getNature() {
        return nature != null ? nature : ' ';
    }

    public void setNature(Character nature) {
        this.nature = nature;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public Date getDatePaiementAlloc() {
        return datePaiementAlloc;
    }

    public void setDatePaiementAlloc(Date datePaiementAlloc) {
        this.datePaiementAlloc = datePaiementAlloc;
    }

    public Date getDateFinPrevu() {
        return dateFinPrevu;
    }

    public void setDateFinPrevu(Date dateFinPrevu) {
        this.dateFinPrevu = dateFinPrevu;
    }

    public Date getDateSuspension() {
        return dateSuspension;
    }

    public void setDateSuspension(Date dateSuspension) {
        this.dateSuspension = dateSuspension;
    }

    public String getMotifSuspension() {
        return motifSuspension;
    }

    public void setMotifSuspension(String motifSuspension) {
        this.motifSuspension = motifSuspension;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getHeureRetourEffectif() {
        return heureRetourEffectif;
    }

    public void setHeureRetourEffectif(Date heureRetourEffectif) {
        this.heureRetourEffectif = heureRetourEffectif;
    }

    public Double getDureePermissionPrise() {
        return dureePermissionPrise != null ? dureePermissionPrise : 0;
    }

    public Double getDureeSup() {
        return dureeSup != null ? dureeSup : 0;
    }

    public void setDureeSup(Double dureeSup) {
        this.dureeSup = dureeSup;
    }

    public void setDureePermissionPrise(Double dureePermissionPrise) {
        this.dureePermissionPrise = dureePermissionPrise;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.id);
        return hash;
    }

    public int getCongePrincipalDu() {
        return congePrincipalDu;
    }

    public void setCongePrincipalDu(int congePrincipalDu) {
        this.congePrincipalDu = congePrincipalDu;
    }

    public int getCongePrincipalPris() {
        return congePrincipalPris;
    }

    public void setCongePrincipalPris(int congePrincipalPris) {
        this.congePrincipalPris = congePrincipalPris;
    }

    public int getDureePermPrisSpeciale() {
        return dureePermPrisSpeciale;
    }

    public void setDureePermPrisSpeciale(int dureePermPrisSpeciale) {
        this.dureePermPrisSpeciale = dureePermPrisSpeciale;
    }

    public double getCongeSupp() {
        return congeSupp;
    }

    public void setCongeSupp(double congeSupp) {
        this.congeSupp = congeSupp;
    }

    public double getDureePermissionAutorisePris() {
        return dureePermissionAutorisePris;
    }

    public void setDureePermissionAutorisePris(double dureePermissionAutorisePris) {
        this.dureePermissionAutorisePris = dureePermissionAutorisePris;
    }

    public double getDureePermissionAutorise() {
        return dureePermissionAutorise;
    }

    public void setDureePermissionAutorise(double dureePermissionAutorise) {
        this.dureePermissionAutorise = dureePermissionAutorise;
    }

    public double getDureePermCD() {
        return dureePermCD;
    }

    public void setDureePermCD(double dureePermCD) {
        this.dureePermCD = dureePermCD;
    }

    public double getDureePermCD_AN() {
        return dureePermCD_AN;
    }

    public void setDureePermCD_AN(double dureePermCD_AN) {
        this.dureePermCD_AN = dureePermCD_AN;
    }

    public double getDureePermCD_SP() {
        return dureePermCD_SP;
    }

    public void setDureePermCD_SP(double dureePermCD_SP) {
        this.dureePermCD_SP = dureePermCD_SP;
    }

    public double getDureePermCD_AU() {
        return dureePermCD_AU;
    }

    public void setDureePermCD_AU(double dureePermCD_AU) {
        this.dureePermCD_AU = dureePermCD_AU;
    }

    public double getDureePermCD_SAL() {
        return dureePermCD_SAL;
    }

    public void setDureePermCD_SAL(double dureePermCD_SAL) {
        this.dureePermCD_SAL = dureePermCD_SAL;
    }

    public boolean isCompteJourRepos() {
        return compteJourRepos;
    }

    public void setCompteJourRepos(boolean compteJourRepos) {
        this.compteJourRepos = compteJourRepos;
    }

    public String getMadateDebut() {
        return dfs.format(getDateDebut());
    }

    public void setMadateDebut(String madateDebut) {
        this.madateDebut = madateDebut;
    }

    public String getMaDateFin() {
        return dfs.format(getDateFin());
    }

    public void setMaDateFin(String maDateFin) {
        this.maDateFin = maDateFin;
    }

    public String getMaDateRetour() {
        return dfs.format(getDateRetour());
    }

    public void setMaDateRetour(String maDateRetour) {
        this.maDateRetour = maDateRetour;
    }

    public String getMaDateSave() {
        return getDateSave() != null ? dfs.format(getDateSave()) : "";
    }

    public void setMaDateSave(String maDateSave) {
        this.maDateSave = maDateSave;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsGrhCongeEmps other = (YvsGrhCongeEmps) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsCongeEmps[ id=" + id + " ]";
    }

}

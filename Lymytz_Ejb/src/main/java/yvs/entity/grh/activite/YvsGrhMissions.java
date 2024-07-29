/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import com.fasterxml.jackson.annotation.JsonIgnore;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.compta.YvsComptaCaissePieceMission;
import yvs.entity.compta.YvsComptaJustifBonMission;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.workflow.YvsWorkflowValidMission;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import static yvs.dao.salaire.service.Constantes.dfs;
import yvs.entity.compta.analytique.YvsComptaCentreMission;
import yvs.entity.proj.projet.YvsProjProjetMissions;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_grh_missions")
@NamedQueries({
    @NamedQuery(name = "YvsMissions.findAll", query = "SELECT y FROM YvsGrhMissions y"),
    @NamedQuery(name = "YvsMissions.findByIds", query = "SELECT y FROM YvsGrhMissions y WHERE y.id in :ids"),
    @NamedQuery(name = "YvsMissions.findBySociete", query = "SELECT y FROM YvsGrhMissions y WHERE y.employe.agence.societe=:societe  ORDER BY y.dateDebut DESC, y.numeroMission"),
    @NamedQuery(name = "YvsMissions.findBySocieteC", query = "SELECT COUNT(y) FROM YvsGrhMissions y WHERE y.employe.agence.societe=:societe "),
    @NamedQuery(name = "YvsMissions.findByAgence", query = "SELECT y FROM YvsGrhMissions y WHERE y.employe.agence=:agence ORDER BY y.dateFin DESC, y.numeroMission"),
    @NamedQuery(name = "YvsMissions.findByAgenceC", query = "SELECT COUNT(y) FROM YvsGrhMissions y WHERE y.employe.agence=:agence"),
    @NamedQuery(name = "YvsMissions.findInDepartement", query = "SELECT y FROM YvsGrhMissions y WHERE y.employe.posteActif.departement.id IN :departements ORDER BY y.dateFin DESC, y.numeroMission"),
    @NamedQuery(name = "YvsMissions.findInDepartementC", query = "SELECT COUNT(y) FROM YvsGrhMissions y WHERE y.employe.posteActif.departement.id IN :departements"),
    @NamedQuery(name = "YvsMissions.findAll2", query = "SELECT y FROM YvsGrhMissions y WHERE y.lieu = :lieu and y.ordre = :ordre"),
    @NamedQuery(name = "YvsMissions.findEnMissionC", query = "SELECT COUNT(y) FROM YvsGrhMissions y WHERE (:date BETWEEN y.dateDebut AND y.dateFin) AND y.statutMission=:statut AND y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsMissions.findAll3", query = "SELECT y FROM YvsGrhMissions y WHERE y.lieu = :lieu AND y.employe.agence.societe = :societe"),
    @NamedQuery(name = "YvsMissions.findAll4", query = "SELECT y FROM YvsGrhMissions y WHERE y.ordre LIKE :ordre AND y.employe.agence.societe = :societe"),
    @NamedQuery(name = "YvsMissions.findAll5", query = "SELECT y FROM YvsGrhMissions y WHERE y.dateDebut = :date AND y.employe.agence.societe = :societe"),
    @NamedQuery(name = "YvsGrhMissions.findAll6", query = "SELECT y FROM YvsGrhMissions y WHERE y.dateFin >= :date AND y.employe.agence.societe = :societe"),
    @NamedQuery(name = "YvsMissions.findById", query = "SELECT y FROM YvsGrhMissions y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMissions.findByEmployesDate", query = "SELECT y.employe FROM YvsGrhMissions y WHERE (:date BETWEEN y.dateDebut AND y.dateFin) AND y.actif=true AND y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsMissions.findByDateMission", query = "SELECT y FROM YvsGrhMissions y WHERE y.dateMission = :dateMission"),
    @NamedQuery(name = "YvsMissions.findByDateDebut", query = "SELECT y FROM YvsGrhMissions y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsGrhMissions.findIdEmpInPeriode", query = "SELECT y.employe.id FROM YvsGrhMissions y WHERE ((y.dateDebut BETWEEN :debut AND :fin) OR (y.dateFin BETWEEN :debut AND :fin)) AND y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsMissions.findByDateFin", query = "SELECT y FROM YvsGrhMissions y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsMissions.findByLieu", query = "SELECT y FROM YvsGrhMissions y WHERE y.lieu = :lieu"),
    @NamedQuery(name = "YvsMissions.findByServiceLieu", query = "SELECT y FROM YvsGrhMissions y WHERE y.employe.agence.societe=:societe AND y.lieu=:lieu AND y.employe.posteActif.departement=:departement ORDER BY y.dateDebut DESC, y.numeroMission"),
    @NamedQuery(name = "YvsMissions.findByServiceLieuC", query = "SELECT COUNT(y) FROM YvsGrhMissions y WHERE y.employe.agence.societe=:societe AND y.lieu=:lieu AND y.employe.posteActif.departement=:departement"),
    @NamedQuery(name = "YvsGrhMissions.findByOneEmployeDate", query = "SELECT y.employe FROM YvsGrhMissions y WHERE (:date BETWEEN y.dateDebut AND y.dateFin) AND ( y.statutMission='V' OR y.statutMission='C' )AND y.employe=:employe ORDER BY y.dateDebut, y.numeroMission"),
    @NamedQuery(name = "YvsGrhMissions.findCountInExercice", query = "SELECT COUNT(y) FROM YvsGrhMissions y WHERE (y.dateDebut BETWEEN :debut AND :fin) AND y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhMissions.findByNumeroM", query = "SELECT y FROM YvsGrhMissions y WHERE y.numeroMission LIKE :numero AND y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhMissions.findByNumPiece", query = "SELECT y FROM YvsGrhMissions y WHERE y.numeroMission LIKE :numeroPiece AND y.employe.agence.societe=:societe ORDER BY y.numeroMission DESC"),
    @NamedQuery(name = "YvsGrhMissions.findByNumeroMC", query = "SELECT COUNT(y) FROM YvsGrhMissions y WHERE y.numeroMission LIKE :numero AND y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsMissions.findByOrdre", query = "SELECT y FROM YvsGrhMissions y WHERE y.ordre = :ordre")})
public class YvsGrhMissions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_missions_id_seq", name = "yvs_missions_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_missions_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_mission")
    @Temporal(TemporalType.DATE)
    private Date dateMission;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Size(max = 2147483647)
    @Column(name = "ordre")
    private String ordre;
    @Column(name = "statut_mission")
    private Character statutMission;    //E= encours de validation, V=Valide, C=cloture, A=Annulé, S=suspendu
    @Column(name = "statut_paiement")
    private Character statutPaiement;    //R= encours de paiement, W=en attente de paiement, P=payer
    @Column(name = "cloturer")
    private Boolean cloturer;
    @Column(name = "role_mission")
    private String roleMission;
    @Column(name = "transport")
    private String transport;   //moyen de transport
    @Column(name = "reference_mission")
    private String referenceMission;
    @Column(name = "date_retour")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateRetour;
    @Temporal(javax.persistence.TemporalType.TIME)
    @Column(name = "heure_depart")
    private Date heureDepart;
    @Temporal(javax.persistence.TemporalType.TIME)
    @Column(name = "heure_arrive")
    private Date heureArrive;
    @Column(name = "date_save")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_fin_prevu")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateFinPrevu;
    @Column(name = "motif_report")
    private String motifReport;
    @Column(name = "lieu_escale")
    private String lieuEscale;
    @Column(name = "duree_mission")
    private Integer dureeMission;
    @Column(name = "numero_mission")
    private String numeroMission;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "etape_total")
    private Integer etapeTotal;
    @Column(name = "etape_valide")
    private Integer etapeValide;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "date_valider")
    private Date dateValider;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "frais_mission", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhGrilleMission fraisMission;
    @JoinColumn(name = "lieu", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire lieu;
    @JoinColumn(name = "valider_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers validerBy;
    @JoinColumn(name = "objet_mission", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhObjetsMission objetMission;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;

    @OneToMany(mappedBy = "mission")
    private List<YvsComptaCaissePieceMission> reglements;
    @OneToMany(mappedBy = "mission")
    private List<YvsComptaCentreMission> analytiques;
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<YvsGrhFraisMission> fraisMissions;
    @OneToMany(mappedBy = "mission", fetch = FetchType.LAZY)
    private List<YvsWorkflowValidMission> etapesValidations;
    @OneToMany(mappedBy = "mission", fetch = FetchType.LAZY)
    private List<YvsGrhMissionRessource> ressources;

    @Transient
    private List<YvsProjProjetMissions> projets;
    @Transient
    private List<YvsComptaJustifBonMission> bonsProvisoire;

    @Transient
    private boolean docClasse;
    @Transient
    private boolean comptabilised;
    @Transient
    private String madateDebut;
    @Transient
    private String madatefin;
    @Transient
    private String madateRetour;
    @Transient
    private String maDateSave;
    @Transient
    private boolean error;
    @Transient
    private double totalFraisMission;
    @Transient
    private double totalPiece;
    @Transient
    private double totalRegle;
    @Transient
    private double totalReste;
    @Transient
    private double totalResteAPlanifier;
    @Transient
    private double totalBon;
    @Transient
    private double totalBonPaye;
    @Transient
    private double totalPiecePaye;

    public YvsGrhMissions() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        bonsProvisoire = new ArrayList<>();
        reglements = new ArrayList<>();
        ressources = new ArrayList<>();
        fraisMissions = new ArrayList<>();
        etapesValidations = new ArrayList<>();
        analytiques = new ArrayList<>();
    }

    public YvsGrhMissions(Long id) {
        this();
        this.id = id;
    }

    public YvsGrhMissions(boolean error) {
        this();
        this.error = error;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Long getId() {
        return this.id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotalFraisMission() {
        return totalFraisMission;
    }

    public void setTotalFraisMission(double totalFraisMission) {
        this.totalFraisMission = totalFraisMission;
    }

    public Date getDateMission() {
        return dateMission;
    }

    public void setDateMission(Date dateMission) {
        this.dateMission = dateMission;
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

    public String getOrdre() {
        return ordre;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsGrhMissionRessource> getRessources() {
        return ressources;
    }

    public void setRessources(List<YvsGrhMissionRessource> ressources) {
        this.ressources = ressources;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhGrilleMission getFraisMission() {
        return fraisMission;
    }

    public void setFraisMission(YvsGrhGrilleMission fraisMission) {
        this.fraisMission = fraisMission;
    }

    public void setStatutMission(Character statutMission) {
        this.statutMission = statutMission;
    }

    public Character getStatutMission() {
        return statutMission != null ? statutMission : 'E';
    }

    public Boolean getCloturer() {
        return cloturer != null ? cloturer : false;
    }

    public void setCloturer(Boolean cloturer) {
        this.cloturer = cloturer;
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

    public boolean isDocClasse() {
        return docClasse;
    }

    public void setDocClasse(boolean docClasse) {
        this.docClasse = docClasse;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsDictionnaire getLieu() {
        return lieu;
    }

    public void setLieu(YvsDictionnaire lieu) {
        this.lieu = lieu;
    }

    public boolean isComptabilised() {
        return comptabilised;
    }

    public void setComptabilised(boolean comptabilised) {
        this.comptabilised = comptabilised;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsWorkflowValidMission> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidMission> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsGrhFraisMission> getFraisMissions() {
        return fraisMissions;
    }

    public void setFraisMissions(List<YvsGrhFraisMission> fraisMissions) {
        this.fraisMissions = fraisMissions;
    }

    public String getRoleMission() {
        return roleMission;
    }

    public void setRoleMission(String roleMission) {
        this.roleMission = roleMission;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getReferenceMission() {
        return referenceMission;
    }

    public void setReferenceMission(String referenceMission) {
        this.referenceMission = referenceMission;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }

    public Date getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(Date heureDepart) {
        this.heureDepart = heureDepart;
    }

    public Date getHeureArrive() {
        return heureArrive;
    }

    public void setHeureArrive(Date heureArrive) {
        this.heureArrive = heureArrive;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateFinPrevu() {
        return dateFinPrevu;
    }

    public void setDateFinPrevu(Date dateFinPrevu) {
        this.dateFinPrevu = dateFinPrevu;
    }

    public String getMotifReport() {
        return motifReport;
    }

    public void setMotifReport(String motifReport) {
        this.motifReport = motifReport;
    }

    public String getLieuEscale() {
        return lieuEscale;
    }

    public void setLieuEscale(String lieuEscale) {
        this.lieuEscale = lieuEscale;
    }

    public Integer getDureeMission() {
        return dureeMission != null ? dureeMission : 0;
    }

    public void setDureeMission(Integer dureeMission) {
        this.dureeMission = dureeMission;
    }

    public String getNumeroMission() {
        return numeroMission != null ? numeroMission : "";
    }

    public void setNumeroMission(String numeroMission) {
        this.numeroMission = numeroMission;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceMission> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceMission> reglements) {
        this.reglements = reglements;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsers getValiderBy() {
        return validerBy;
    }

    public void setValiderBy(YvsUsers validerBy) {
        this.validerBy = validerBy;
    }

    public Date getDateValider() {
        return dateValider;
    }

    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

    public YvsGrhObjetsMission getObjetMission() {
        return objetMission;
    }

    public void setObjetMission(YvsGrhObjetsMission objetMission) {
        this.objetMission = objetMission;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaJustifBonMission> getBonsProvisoire() {
        return bonsProvisoire;
    }

    public void setBonsProvisoire(List<YvsComptaJustifBonMission> bonsProvisoire) {
        this.bonsProvisoire = bonsProvisoire;
    }

    public double getTotalBon() {
        return totalBon;
    }

    public void setTotalBon(double totalBon) {
        this.totalBon = totalBon;
    }

    public double getTotalPiece() {
        return totalPiece;
    }

    public void setTotalPiece(double totalPiece) {
        this.totalPiece = totalPiece;
    }

    public double getTotalRegle() {
        return totalRegle;
    }

    public void setTotalRegle(double totalRegle) {
        this.totalRegle = totalRegle;
    }

    public double getTotalReste() {
        return totalReste;
    }

    public void setTotalReste(double totalReste) {
        this.totalReste = totalReste;
    }

    public double getTotalResteAPlanifier() {
        return totalResteAPlanifier;
    }

    public void setTotalResteAPlanifier(double totalResteAPlanifier) {
        this.totalResteAPlanifier = totalResteAPlanifier;
    }

    public double getTotalBonPaye() {
        return totalBonPaye;
    }

    public void setTotalBonPaye(double totalBonPaye) {
        this.totalBonPaye = totalBonPaye;
    }

    public String getMadateDebut() {
        return dfs.format(getDateDebut());
    }

    public void setMadateDebut(String madateDebut) {
        this.madateDebut = madateDebut;
    }

    public String getMadatefin() {
        return dfs.format(getDateFin());
    }

    public void setMadatefin(String madatefin) {
        this.madatefin = madatefin;
    }

    public String getMadateRetour() {
        return dfs.format(getDateRetour());
    }

    public void setMadateRetour(String maDateRetour) {
        this.madateRetour = maDateRetour;
    }

    public String getMaDateSave() {
        return getDateSave() != null ? dfs.format(getDateSave()) : "";
    }

    public void setMaDateSave(String maDateSave) {
        this.maDateSave = maDateSave;
    }

    public Character getStatutPaiement() {
        return statutPaiement != null ? statutPaiement != ' ' ? statutPaiement : 'W' : 'W';
    }

    public void setStatutPaiement(Character statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsProjProjetMissions> getProjets() {
        return projets;
    }

    public void setProjets(List<YvsProjProjetMissions> projets) {
        this.projets = projets;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCentreMission> getAnalytiques() {
        return analytiques;
    }

    public void setAnalytiques(List<YvsComptaCentreMission> analytiques) {
        this.analytiques = analytiques;
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
        if (!(object instanceof YvsGrhMissions)) {
            return false;
        }
        YvsGrhMissions other = (YvsGrhMissions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsMissions[ id=" + id + " ]";
    }

    public boolean canEditable() {
        return statutMission == Constantes.STATUT_ATTENTE || statutMission == Constantes.STATUT_EDITABLE;
    }

    public boolean canDelete() {
        return statutMission == Constantes.STATUT_ATTENTE || statutMission == Constantes.STATUT_EDITABLE || statutMission == Constantes.STATUT_SUSPENDU || statutMission == Constantes.STATUT_ANNULE;
    }

    public double getTotalPiecePaye() {
        return totalPiecePaye;
    }

    public void setTotalPiecePaye(double totalPiecePaye) {
        this.totalPiecePaye = totalPiecePaye;
    }
    
    

}

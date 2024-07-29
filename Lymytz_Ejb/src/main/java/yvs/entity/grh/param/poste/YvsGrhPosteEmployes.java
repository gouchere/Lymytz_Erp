/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param.poste;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_poste_employes")
@NamedQueries({
    @NamedQuery(name = "YvsPosteEmployes.findAll", query = "SELECT y FROM YvsGrhPosteEmployes y WHERE y.poste.departement.societe=:societe ORDER BY y.dateAcquisition DESC"),
    @NamedQuery(name = "YvsPosteEmployes.findById", query = "SELECT y FROM YvsGrhPosteEmployes y WHERE y.id = :id"),
    @NamedQuery(name = "YvsPosteEmployes.findByIds", query = "SELECT y FROM YvsGrhPosteEmployes y WHERE y.employe = :employe AND y.poste=:poste AND y.actif=true"),
    @NamedQuery(name = "YvsPosteEmployes.findByEmploye", query = "SELECT y FROM YvsGrhPosteEmployes y WHERE y.employe = :employe ORDER BY y.dateAcquisition"),
    @NamedQuery(name = "YvsPosteEmployes.findByEmploye_", query = "SELECT y FROM YvsGrhPosteEmployes y WHERE (y.employe.matricule LIKE :employe OR y.employe.nom LIKE :employe OR y.employe.prenom LIKE :employe) AND y.poste.departement.societe=:societe ORDER BY y.dateAcquisition"),
    @NamedQuery(name = "YvsPosteEmployes.findByDateAcquisition", query = "SELECT y FROM YvsGrhPosteEmployes y WHERE y.dateAcquisition = :date AND y.poste.departement.societe=:societe"),
    @NamedQuery(name = "YvsPosteEmployes.findByDateConfirm", query = "SELECT y FROM YvsGrhPosteEmployes y WHERE y.dateConfirmation = :date AND y.poste.departement.societe=:societe"),
    @NamedQuery(name = "YvsPosteEmployes.findByPoste", query = "SELECT y.employe FROM YvsGrhPosteEmployes y WHERE y.poste.intitule LIKE :poste ORDER BY y.dateAcquisition DESC"),
    @NamedQuery(name = "YvsPosteEmployes.findByDepartement", query = "SELECT y.employe FROM YvsGrhPosteEmployes y WHERE y.employe.actif = true AND y.poste.departement=:dep AND y.actif=true"),
    @NamedQuery(name = "YvsPosteEmployes.findByStatut", query = "SELECT y FROM YvsGrhPosteEmployes y WHERE y.statut=:statut AND y.poste.departement.societe=:societe"),
    @NamedQuery(name = "YvsPosteEmployes.findByActif", query = "SELECT y FROM YvsGrhPosteEmployes y WHERE y.employe.actif = true AND y.actif= :actif AND y.employe = :employe"),
    @NamedQuery(name = "YvsPosteEmployes.findByEmployeDates", query = "SELECT y FROM YvsGrhPosteEmployes y WHERE y.employe = :employe AND ((y.dateAcquisition BETWEEN :dateDebut AND :dateFin) OR (y.dateConfirmation BETWEEN :dateDebut AND :dateFin))"),
    @NamedQuery(name = "YvsPosteEmployes.findByValider", query = "SELECT y FROM YvsGrhPosteEmployes y WHERE y.valider = :valider")})
public class YvsGrhPosteEmployes implements Serializable {

    @JoinColumn(name = "poste", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPosteDeTravail poste;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_poste_employes_id_seq", name = "yvs_poste_employes_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_poste_employes_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_acquisition")
    @Temporal(TemporalType.DATE)
    private Date dateAcquisition;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "valider")
    private Boolean valider;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @Column(name = "statut")
    private Character statut;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "date_debut")
    private Date dateDebut;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "date_fin_interim")
    private Date dateFinInterim;
    @JoinColumn(name = "poste_precedent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPosteDeTravail postePrecedent;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "date_confirmation")
    private Date dateConfirmation;
    @Column(name = "indemnisable")
    private Boolean indemnisable;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Column(name = "motif_affectation")
    private String motifAffectation;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence = new YvsAgences();

    public YvsGrhPosteEmployes() {
    }

    public YvsGrhPosteEmployes(Long id) {
        this.id = id;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(Date dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getValider() {
        return valider != null ? valider : false;
    }

    public void setValider(Boolean valider) {
        this.valider = valider;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public Character getStatut() {
        return statut != null ? statut : 'E';
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFinInterim() {
        return dateFinInterim;
    }

    public void setDateFinInterim(Date dateFinInterim) {
        this.dateFinInterim = dateFinInterim;
    }

    public YvsGrhPosteDeTravail getPostePrecedent() {
        return postePrecedent;
    }

    public void setPostePrecedent(YvsGrhPosteDeTravail postePrecedent) {
        this.postePrecedent = postePrecedent;
    }

    public Date getDateConfirmation() {
        return dateConfirmation;
    }

    public void setDateConfirmation(Date dateConfirmation) {
        this.dateConfirmation = dateConfirmation;
    }

    public Boolean isIndemnisable() {
        return indemnisable != null ? indemnisable : false;
    }

    public void setIndemnisable(Boolean indemnisable) {
        this.indemnisable = indemnisable;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getMotifAffectation() {
        return motifAffectation;
    }

    public void setMotifAffectation(String motifAffectation) {
        this.motifAffectation = motifAffectation;
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
        if (!(object instanceof YvsGrhPosteEmployes)) {
            return false;
        }
        YvsGrhPosteEmployes other = (YvsGrhPosteEmployes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsPosteEmployes[ id=" + id + " ]";
    }

    public YvsGrhPosteDeTravail getPoste() {
        return poste;
    }

    public void setPoste(YvsGrhPosteDeTravail poste) {
        this.poste = poste;
    }

}

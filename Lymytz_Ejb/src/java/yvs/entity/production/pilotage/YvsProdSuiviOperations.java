/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.pilotage;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_suivi_operations")
@NamedQueries({
    @NamedQuery(name = "YvsProdSuiviOperations.findAll", query = "SELECT y FROM YvsProdSuiviOperations y"),
    @NamedQuery(name = "YvsProdSuiviOperations.findById", query = "SELECT y FROM YvsProdSuiviOperations y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdSuiviOperations.findIdByOrdre", query = "SELECT y.id FROM YvsProdSuiviOperations y WHERE y.sessionOf.ordre = :ordre"),
    @NamedQuery(name = "YvsProdSuiviOperations.findByOF", query = "SELECT y FROM YvsProdSuiviOperations y WHERE y.operationOf.ordreFabrication=:ordre ORDER BY y.statut, y.referenceOp DESC"),
    @NamedQuery(name = "YvsProdSuiviOperations.findOne", query = "SELECT y FROM YvsProdSuiviOperations y WHERE y.operationOf=:operation AND y.sessionOf=:session ORDER BY y.statut, y.referenceOp DESC"),
    @NamedQuery(name = "YvsProdSuiviOperations.findByOperation", query = "SELECT y FROM YvsProdSuiviOperations y WHERE y.operationOf=:operation  ORDER BY y.referenceOp, y.dateDebut, y.heureDebut"),
    @NamedQuery(name = "YvsProdSuiviOperations.findByOperationProducteur", query = "SELECT y FROM YvsProdSuiviOperations y WHERE y.operationOf=:operation AND y.sessionOf.sessionProd.producteur=:producteur ORDER BY y.dateDebut DESC, y.heureDebut DESC"),
    @NamedQuery(name = "YvsProdSuiviOperations.findByOperationSessionProducteur", query = "SELECT y FROM YvsProdSuiviOperations y WHERE y.operationOf=:operation AND y.sessionOf=:session ORDER BY y.dateDebut DESC, y.heureDebut DESC"),
    @NamedQuery(name = "YvsProdSuiviOperations.findLastRefOp", query = "SELECT y.referenceOp FROM YvsProdSuiviOperations y WHERE y.operationOf=:operation ORDER BY y.referenceOp DESC"),
    @NamedQuery(name = "YvsProdSuiviOperations.findByDateDebut", query = "SELECT y FROM YvsProdSuiviOperations y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsProdSuiviOperations.findByDateFin", query = "SELECT y FROM YvsProdSuiviOperations y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsProdSuiviOperations.findByHeureDebut", query = "SELECT y FROM YvsProdSuiviOperations y WHERE y.heureDebut = :heureDebut"),
    @NamedQuery(name = "YvsProdSuiviOperations.findByHeureFin", query = "SELECT y FROM YvsProdSuiviOperations y WHERE y.heureFin = :heureFin"),
    @NamedQuery(name = "YvsProdSuiviOperations.findByCout", query = "SELECT y FROM YvsProdSuiviOperations y WHERE y.cout = :cout"),
    @NamedQuery(name = "YvsProdSuiviOperations.findByDateSave", query = "SELECT y FROM YvsProdSuiviOperations y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsProdSuiviOperations.findByDateUpadate", query = "SELECT y FROM YvsProdSuiviOperations y WHERE y.dateUpadate = :dateUpadate")})
public class YvsProdSuiviOperations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_suivi_operations_id_seq", name = "yvs_prod_suivi_operations_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_suivi_operations_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "heure_debut")
    @Temporal(TemporalType.TIME)
    private Date heureDebut;
    @Column(name = "heure_fin")
    @Temporal(TemporalType.TIME)
    private Date heureFin;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cout")
    private Double cout;
    @Column(name = "ref_operation")
    private String referenceOp;
    @Column(name = "statut_op")
    private String statut;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_upadate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpadate;
    @JoinColumn(name = "session_of", referencedColumnName = "id")
    @ManyToOne
    private YvsProdSessionOf sessionOf;
    @JoinColumn(name = "operation_of", referencedColumnName = "id")
    @ManyToOne
    private YvsProdOperationsOF operationOf;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsProdSuiviOperations() {
    }

    public YvsProdSuiviOperations(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getCout() {
        return cout != null ? cout : 0;
    }

    public void setCout(Double cout) {
        this.cout = cout;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpadate() {
        return dateUpadate;
    }

    public void setDateUpadate(Date dateUpadate) {
        this.dateUpadate = dateUpadate;
    }

    public YvsProdSessionOf getSessionOf() {
        return sessionOf;
    }

    public void setSessionOf(YvsProdSessionOf sessionOf) {
        this.sessionOf = sessionOf;
    }

    public YvsProdOperationsOF getOperationOf() {
        return operationOf;
    }

    public void setOperationOf(YvsProdOperationsOF operationOf) {
        this.operationOf = operationOf;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getReferenceOp() {
        return referenceOp;
    }

    public void setReferenceOp(String referenceOp) {
        this.referenceOp = referenceOp;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
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
        if (!(object instanceof YvsProdSuiviOperations)) {
            return false;
        }
        YvsProdSuiviOperations other = (YvsProdSuiviOperations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdSuiviOperations[ id=" + id + " ]";
    }

}

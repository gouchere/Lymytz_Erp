/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.contrat;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_model_element_additionel")
@NamedQueries({
    @NamedQuery(name = "YvsGrhModelElementAdditionel.findAll", query = "SELECT y FROM YvsGrhModelElementAdditionel y WHERE y.typeElement.societe=:societe"),
    @NamedQuery(name = "YvsGrhModelElementAdditionel.findByDescription", query = "SELECT y FROM YvsGrhModelElementAdditionel y WHERE y.description = :description"),
    @NamedQuery(name = "YvsGrhModelElementAdditionel.findByMontantElement", query = "SELECT y FROM YvsGrhModelElementAdditionel y WHERE y.montantElement = :montantElement"),
    @NamedQuery(name = "YvsGrhModelElementAdditionel.findById", query = "SELECT y FROM YvsGrhModelElementAdditionel y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhModelElementAdditionel.findByDateElement", query = "SELECT y FROM YvsGrhModelElementAdditionel y WHERE y.dateElement = :dateElement"),
    @NamedQuery(name = "YvsGrhModelElementAdditionel.findByDateDebut", query = "SELECT y FROM YvsGrhModelElementAdditionel y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsGrhModelElementAdditionel.findByDateFin", query = "SELECT y FROM YvsGrhModelElementAdditionel y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsGrhModelElementAdditionel.findByPermanent", query = "SELECT y FROM YvsGrhModelElementAdditionel y WHERE y.permanent = :permanent")})
public class YvsGrhModelElementAdditionel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_element")
    private Double montantElement;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_model_element_additionel_id_seq", name = "yvs_grh_model_element_additionel_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_model_element_additionel_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_element")
    @Temporal(TemporalType.DATE)
    private Date dateElement;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "permanent")
    private Boolean permanent;
    @JoinColumn(name = "type_element", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeElementAdditionel typeElement;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private boolean suspendu;

    public YvsGrhModelElementAdditionel() {
    }

    public YvsGrhModelElementAdditionel(Long id) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getMontantElement() {
        return montantElement;
    }

    public void setMontantElement(Double montantElement) {
        this.montantElement = montantElement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateElement() {
        return dateElement;
    }

    public void setDateElement(Date dateElement) {
        this.dateElement = dateElement;
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

    public Boolean getPermanent() {
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public YvsGrhTypeElementAdditionel getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(YvsGrhTypeElementAdditionel typeElement) {
        this.typeElement = typeElement;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isSuspendu() {
        return suspendu;
    }

    public void setSuspendu(boolean suspendu) {
        this.suspendu = suspendu;
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
        if (!(object instanceof YvsGrhModelElementAdditionel)) {
            return false;
        }
        YvsGrhModelElementAdditionel other = (YvsGrhModelElementAdditionel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.contrat.YvsGrhModelElementAdditionel[ id=" + id + " ]";
    }

}

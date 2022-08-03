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
import javax.persistence.Transient;
import yvs.entity.grh.contrat.YvsGrhTypeElementAdditionel;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_model_primes_poste")
@NamedQueries({
    @NamedQuery(name = "YvsGrhElementAdditionelPoste.findAll", query = "SELECT y FROM YvsGrhModelPrimePoste y"),
    @NamedQuery(name = "YvsGrhElementAdditionelPoste.findByMontantElement", query = "SELECT y FROM YvsGrhModelPrimePoste y WHERE y.montantElement = :montantElement"),
    @NamedQuery(name = "YvsGrhElementAdditionelPoste.findById", query = "SELECT y FROM YvsGrhModelPrimePoste y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhElementAdditionelPoste.findByDateDebut", query = "SELECT y FROM YvsGrhModelPrimePoste y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsGrhElementAdditionelPoste.findByDateFin", query = "SELECT y FROM YvsGrhModelPrimePoste y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsGrhElementAdditionelPoste.findByPermanent", query = "SELECT y FROM YvsGrhModelPrimePoste y WHERE y.permanent = :permanent")})
public class YvsGrhModelPrimePoste implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_element_additionel_poste_id_seq", name = "yvs_grh_element_additionel_poste_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_element_additionel_poste_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "permanent")
    private Boolean permanent;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_element")
    private Double montantElement;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhModelContrat model;
    @JoinColumn(name = "type_element", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeElementAdditionel typeElement;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private YvsGrhPosteDeTravail poste;
    @Transient
    private boolean select;

    public YvsGrhModelPrimePoste() {
    }

    public YvsGrhModelPrimePoste(Long id) {
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

    public YvsGrhPosteDeTravail getPoste() {
        return poste;
    }

    public void setPoste(YvsGrhPosteDeTravail poste) {
        this.poste = poste;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Double getMontantElement() {
        return montantElement != null ? montantElement : 0;
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

    public Date getDateDebut() {
        return dateDebut != null ? dateDebut : new Date();
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin != null ? dateFin : new Date();
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getPermanent() {
        return permanent != null ? permanent : false;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsGrhModelPrimePoste)) {
            return false;
        }
        YvsGrhModelPrimePoste other = (YvsGrhModelPrimePoste) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.poste.YvsGrhElementAdditionelPoste[ id=" + id + " ]";
    }

    public YvsGrhModelContrat getModel() {
        return model;
    }

    public void setModel(YvsGrhModelContrat model) {
        this.model = model;
    }

}

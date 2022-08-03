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
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_composants_retenue")
@NamedQueries({
    @NamedQuery(name = "YvsGrhComposantsRetenue.findAll", query = "SELECT y FROM YvsGrhComposantsRetenue y"),
    @NamedQuery(name = "YvsGrhComposantsRetenue.findById", query = "SELECT y FROM YvsGrhComposantsRetenue y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhComposantsRetenue.findByMotif", query = "SELECT y FROM YvsGrhComposantsRetenue y WHERE y.motif = :motif"),
    @NamedQuery(name = "YvsGrhComposantsRetenue.findByMontant", query = "SELECT y FROM YvsGrhComposantsRetenue y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsGrhComposantsRetenue.findByActif", query = "SELECT y FROM YvsGrhComposantsRetenue y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsGrhComposantsRetenue.findByDateAjout", query = "SELECT y FROM YvsGrhComposantsRetenue y WHERE y.dateAjout = :dateAjout")})
public class YvsGrhComposantsRetenue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_composants_retenue_id_seq", name = "yvs_grh_composants_retenue_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_composants_retenue_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "motif")
    private String motif;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_ajout")
    @Temporal(TemporalType.DATE)
    private Date dateAjout;
    @JoinColumn(name = "retenue", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhElementAdditionel retenue;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhComposantsRetenue() {
    }

    public YvsGrhComposantsRetenue(Long id) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Date getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }

    public YvsGrhElementAdditionel getRetenue() {
        return retenue;
    }

    public void setRetenue(YvsGrhElementAdditionel retenue) {
        this.retenue = retenue;
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
        if (!(object instanceof YvsGrhComposantsRetenue)) {
            return false;
        }
        YvsGrhComposantsRetenue other = (YvsGrhComposantsRetenue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.contrat.YvsGrhDetailsRetenue[ id=" + id + " ]";
    }

}

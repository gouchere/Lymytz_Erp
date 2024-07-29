/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.evenement;

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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_taux_contribution")
@NamedQueries({
    @NamedQuery(name = "YvsMutTauxContribution.findAll", query = "SELECT y FROM YvsMutTauxContribution y WHERE y.typeContibution.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutTauxContribution.findById", query = "SELECT y FROM YvsMutTauxContribution y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutTauxContribution.findByType", query = "SELECT y FROM YvsMutTauxContribution y WHERE y.typeEvenement = :type"),
    @NamedQuery(name = "YvsMutTauxContribution.findByMontant", query = "SELECT y FROM YvsMutTauxContribution y WHERE y.montantMin = :montant")})
public class YvsMutTauxContribution implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_taux_contribution_id_seq", name = "yvs_mut_taux_contribution_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_taux_contribution_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_min")
    private Double montantMin;
    @JoinColumn(name = "type_evenement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutTypeEvenement typeEvenement;
    @JoinColumn(name = "activite_type", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutActiviteType typeContibution;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;

    public YvsMutTauxContribution() {
    }

    public YvsMutTauxContribution(Long id) {
        this.id = id;
    }

    public YvsMutTauxContribution(Long id, YvsMutActiviteType typeContibution) {
        this.id = id;
        this.typeContibution = typeContibution;
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

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontantMin() {
        return montantMin != null ? montantMin : 0;
    }

    public void setMontantMin(Double montantMin) {
        this.montantMin = montantMin;
    }

    public YvsMutTypeEvenement getTypeEvenement() {
        return typeEvenement;
    }

    public void setTypeEvenement(YvsMutTypeEvenement typeEvenement) {
        this.typeEvenement = typeEvenement;
    }

    public YvsMutActiviteType getTypeContibution() {
        return typeContibution;
    }

    public void setTypeContibution(YvsMutActiviteType typeContibution) {
        this.typeContibution = typeContibution;
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
        if (!(object instanceof YvsMutTauxContribution)) {
            return false;
        }
        YvsMutTauxContribution other = (YvsMutTauxContribution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.evenement.YvsMutContribution[ id=" + id + " ]";
    }

}

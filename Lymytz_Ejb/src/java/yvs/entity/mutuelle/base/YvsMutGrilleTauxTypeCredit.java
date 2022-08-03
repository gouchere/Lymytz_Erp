/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.base;

import yvs.entity.mutuelle.credit.YvsMutTypeCredit;
import java.io.Serializable;
import java.util.Date;
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
@Table(name = "yvs_mut_grille_taux_type_credit")
@NamedQueries({
    @NamedQuery(name = "YvsMutGrilleTauxTypeCredit.findAll", query = "SELECT y FROM YvsMutGrilleTauxTypeCredit y"),
    @NamedQuery(name = "YvsMutGrilleTauxTypeCredit.findById", query = "SELECT y FROM YvsMutGrilleTauxTypeCredit y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutGrilleTauxTypeCredit.findByMontantMinimal", query = "SELECT y FROM YvsMutGrilleTauxTypeCredit y WHERE y.montantMinimal = :montantMinimal"),
    @NamedQuery(name = "YvsMutGrilleTauxTypeCredit.findByTaux", query = "SELECT y FROM YvsMutGrilleTauxTypeCredit y WHERE y.taux = :taux"),
    @NamedQuery(name = "YvsMutGrilleTauxTypeCredit.findByPeriodeMaximal", query = "SELECT y FROM YvsMutGrilleTauxTypeCredit y WHERE y.periodeMaximal = :periodeMaximal")})
public class YvsMutGrilleTauxTypeCredit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_grille_taux_type_credit_id_seq", name = "yvs_mut_grille_taux_type_credit_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_grille_taux_type_credit_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "montant_minimal")
    private Double montantMinimal;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_maximal")
    private Double montantMaximal;
    @Column(name = "taux")
    private Double taux;
    @Column(name = "periode_maximal")
    private Double periodeMaximal; //durée maximale de remboursement
    @JoinColumn(name = "type_credit", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutTypeCredit typeCredit;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;

    public YvsMutGrilleTauxTypeCredit() {
    }

    public YvsMutGrilleTauxTypeCredit(Long id) {
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontantMinimal() {
        return montantMinimal != null ? montantMinimal : 0;
    }

    public void setMontantMinimal(Double montantMinimal) {
        this.montantMinimal = montantMinimal;
    }

    public Double getTaux() {
        return taux != null ? taux : 0;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public Double getPeriodeMaximal() {
        return periodeMaximal != null ? periodeMaximal : 0;
    }

    public void setPeriodeMaximal(Double periodeMaximal) {
        this.periodeMaximal = periodeMaximal;
    }

    public YvsMutTypeCredit getTypeCredit() {
        return typeCredit;
    }

    public void setTypeCredit(YvsMutTypeCredit typeCredit) {
        this.typeCredit = typeCredit;
    }

    public Double getMontantMaximal() {
        return montantMaximal != null ? montantMaximal : 0;
    }

    public void setMontantMaximal(Double montantMaximal) {
        this.montantMaximal = montantMaximal;
    }

    public static YvsMutGrilleTauxTypeCredit getDefault(double montant) {
        YvsMutGrilleTauxTypeCredit g = new YvsMutGrilleTauxTypeCredit();
        g.setId(Long.valueOf(0));
        g.setMontantMaximal(montant);
        g.setMontantMinimal(montant);
        g.setPeriodeMaximal(10.0);
        g.setTaux(10.0);
        return g;
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
        if (!(object instanceof YvsMutGrilleTauxTypeCredit)) {
            return false;
        }
        YvsMutGrilleTauxTypeCredit other = (YvsMutGrilleTauxTypeCredit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.credit.YvsMutGrilleTauxTypeCredit[ id=" + id + " ]";
    }

}

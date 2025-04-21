/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial;

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
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_operation_compte_fsseur")
@NamedQueries({
    @NamedQuery(name = "YvsBaseOperationCompteFsseur.findAll", query = "SELECT y FROM YvsBaseOperationCompteFsseur y WHERE y.fournisseur.tiers.societe = :societe"),
    @NamedQuery(name = "YvsBaseOperationCompteFsseur.findByFournisseur", query = "SELECT y FROM YvsBaseOperationCompteFsseur y WHERE y.fournisseur = :fournisseur"),
    @NamedQuery(name = "YvsBaseOperationCompteFsseur.findById", query = "SELECT y FROM YvsBaseOperationCompteFsseur y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseOperationCompteFsseur.findByDateOperation", query = "SELECT y FROM YvsBaseOperationCompteFsseur y WHERE y.dateOperation = :dateOperation"),
    @NamedQuery(name = "YvsBaseOperationCompteFsseur.findByHeureOperation", query = "SELECT y FROM YvsBaseOperationCompteFsseur y WHERE y.heureOperation = :heureOperation"),
    @NamedQuery(name = "YvsBaseOperationCompteFsseur.findByMontant", query = "SELECT y FROM YvsBaseOperationCompteFsseur y WHERE y.montant = :montant")})
public class YvsBaseOperationCompteFsseur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_operation_compte_fsseur_id_seq", name = "yvs_com_operation_compte_fsseur_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_operation_compte_fsseur_id_seq_name", strategy = GenerationType.SEQUENCE) 
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_operation")
    @Temporal(TemporalType.DATE)
    private Date dateOperation;
    @Column(name = "heure_operation")
    @Temporal(TemporalType.TIME)
    private Date heureOperation;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @JoinColumn(name = "fournisseur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseFournisseur fournisseur;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsBaseOperationCompteFsseur() {
    }

    public YvsBaseOperationCompteFsseur(Long id) {
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(Date dateOperation) {
        this.dateOperation = dateOperation;
    }

    public Date getHeureOperation() {
        return heureOperation;
    }

    public void setHeureOperation(Date heureOperation) {
        this.heureOperation = heureOperation;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public YvsBaseFournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(YvsBaseFournisseur fournisseur) {
        this.fournisseur = fournisseur;
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
        if (!(object instanceof YvsBaseOperationCompteFsseur)) {
            return false;
        }
        YvsBaseOperationCompteFsseur other = (YvsBaseOperationCompteFsseur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.YvsBaseOperationCompteFsseur[ id=" + id + " ]";
    }

}

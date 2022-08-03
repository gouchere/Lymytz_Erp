/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.salaire;

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
import yvs.entity.param.YvsSocietes;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_type_depense")
@NamedQueries({
    @NamedQuery(name = "YvsGrhTypeDepense.findAll", query = "SELECT y FROM YvsGrhTypeDepense y"),
    @NamedQuery(name = "YvsGrhTypeDepense.findBySociete", query = "SELECT y FROM YvsGrhTypeDepense y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsGrhTypeDepense.findById", query = "SELECT y FROM YvsGrhTypeDepense y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhTypeDepense.findByLibelle", query = "SELECT y FROM YvsGrhTypeDepense y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsGrhTypeDepense.findBySeuilMontant", query = "SELECT y FROM YvsGrhTypeDepense y WHERE y.seuilMontant = :seuilMontant"),
    @NamedQuery(name = "YvsGrhTypeDepense.findByMargeMontant", query = "SELECT y FROM YvsGrhTypeDepense y WHERE y.margeMontant = :margeMontant")})
public class YvsGrhTypeDepense implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_type_depense_id_seq", name = "yvs_grh_type_depense_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_type_depense_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "seuil_montant")
    private double seuilMontant;
    @Column(name = "marge_montant")
    private double margeMontant;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    public YvsGrhTypeDepense() {
    }

    public YvsGrhTypeDepense(Integer id) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getSeuilMontant() {
        return seuilMontant;
    }

    public void setSeuilMontant(double seuilMontant) {
        this.seuilMontant = seuilMontant;
    }

    public double getMargeMontant() {
        return margeMontant;
    }

    public void setMargeMontant(double margeMontant) {
        this.margeMontant = margeMontant;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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
        if (!(object instanceof YvsGrhTypeDepense)) {
            return false;
        }
        YvsGrhTypeDepense other = (YvsGrhTypeDepense) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhTypeDepense[ id=" + id + " ]";
    }

}

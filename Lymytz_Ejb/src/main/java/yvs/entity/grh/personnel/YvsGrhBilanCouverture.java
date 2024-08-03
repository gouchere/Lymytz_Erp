/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

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

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_bilan_couverture")
@NamedQueries({
    @NamedQuery(name = "YvsGrhBilanCouverture.findAll", query = "SELECT y FROM YvsGrhBilanCouverture y"),
    @NamedQuery(name = "YvsGrhBilanCouverture.findById", query = "SELECT y FROM YvsGrhBilanCouverture y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhBilanCouverture.findByDateCouverture", query = "SELECT y FROM YvsGrhBilanCouverture y WHERE y.dateCouverture = :dateCouverture"),
    @NamedQuery(name = "YvsGrhBilanCouverture.findByMontant", query = "SELECT y FROM YvsGrhBilanCouverture y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsGrhBilanCouverture.findByFichier", query = "SELECT y FROM YvsGrhBilanCouverture y WHERE y.fichier = :fichier")})
public class YvsGrhBilanCouverture implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_bilan_couverture_id_seq", name = "yvs_grh_bilan_couverture_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_bilan_couverture_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_couverture")
    @Temporal(TemporalType.DATE)
    private Date dateCouverture;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Size(max = 2147483647)
    @Column(name = "fichier")
    private String fichier;
    @JoinColumn(name = "couverture_personne", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhCouverturePersonneCharge couverturePersonne;

    public YvsGrhBilanCouverture() {
    }

    public YvsGrhBilanCouverture(Integer id) {
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

    public Date getDateCouverture() {
        return dateCouverture;
    }

    public void setDateCouverture(Date dateCouverture) {
        this.dateCouverture = dateCouverture;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public YvsGrhCouverturePersonneCharge getCouverturePersonne() {
        return couverturePersonne;
    }

    public void setCouverturePersonne(YvsGrhCouverturePersonneCharge couverturePersonne) {
        this.couverturePersonne = couverturePersonne;
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
        if (!(object instanceof YvsGrhBilanCouverture)) {
            return false;
        }
        YvsGrhBilanCouverture other = (YvsGrhBilanCouverture) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsGrhBilanCouverture[ id=" + id + " ]";
    }

}

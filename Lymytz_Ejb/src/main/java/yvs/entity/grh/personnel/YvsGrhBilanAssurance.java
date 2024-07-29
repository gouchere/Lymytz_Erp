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
@Table(name = "yvs_grh_bilan_assurance")
@NamedQueries({
    @NamedQuery(name = "YvsGrhBilanAssurance.findAll", query = "SELECT y FROM YvsGrhBilanAssurance y"),
    @NamedQuery(name = "YvsGrhBilanAssurance.findById", query = "SELECT y FROM YvsGrhBilanAssurance y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhBilanAssurance.findByDateAssurance", query = "SELECT y FROM YvsGrhBilanAssurance y WHERE y.dateAssurance = :dateAssurance"),
    @NamedQuery(name = "YvsGrhBilanAssurance.findByMontant", query = "SELECT y FROM YvsGrhBilanAssurance y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsGrhBilanAssurance.findByFichier", query = "SELECT y FROM YvsGrhBilanAssurance y WHERE y.fichier = :fichier")})
public class YvsGrhBilanAssurance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_bilan_assurance_id_seq", name = "yvs_grh_bilan_assurance_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_bilan_assurance_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_assurance")
    @Temporal(TemporalType.DATE)
    private Date dateAssurance;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Size(max = 2147483647)
    @Column(name = "fichier")
    private String fichier;
    @JoinColumn(name = "assurance", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhAssurance assurance;

    public YvsGrhBilanAssurance() {
    }

    public YvsGrhBilanAssurance(Integer id) {
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

    public Date getDateAssurance() {
        return dateAssurance;
    }

    public void setDateAssurance(Date dateAssurance) {
        this.dateAssurance = dateAssurance;
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

    public YvsGrhAssurance getAssurance() {
        return assurance;
    }

    public void setAssurance(YvsGrhAssurance assurance) {
        this.assurance = assurance;
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
        if (!(object instanceof YvsGrhBilanAssurance)) {
            return false;
        }
        YvsGrhBilanAssurance other = (YvsGrhBilanAssurance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsGrhBilanAssurance[ id=" + id + " ]";
    }

}

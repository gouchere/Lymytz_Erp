/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.statistique;

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
import javax.validation.constraints.Size;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_stat_grh_taux_contribution")
@NamedQueries({
    @NamedQuery(name = "YvsStatTauxContribution.findAll", query = "SELECT y FROM YvsStatGrhTauxContribution y"),
    @NamedQuery(name = "YvsStatTauxContribution.findById", query = "SELECT y FROM YvsStatGrhTauxContribution y WHERE y.id = :id"),
    @NamedQuery(name = "YvsStatTauxContribution.findByLibelle", query = "SELECT y FROM YvsStatGrhTauxContribution y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsStatTauxContribution.findByTaux", query = "SELECT y FROM YvsStatGrhTauxContribution y WHERE y.taux = :taux")})
public class YvsStatGrhTauxContribution implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_stat_taux_contribution_id_seq", name = "yvs_stat_taux_contribution_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_stat_taux_contribution_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "type_taux")
    private String typeTaux = "P";
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @JoinColumn(name = "etat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsStatGrhEtat etat;

    public YvsStatGrhTauxContribution() {
    }

    public YvsStatGrhTauxContribution(Long id) {
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public YvsStatGrhEtat getEtat() {
        return etat;
    }

    public void setEtat(YvsStatGrhEtat etat) {
        this.etat = etat;
    }

    public String getTypeTaux() {
        return typeTaux;
    }

    public void setTypeTaux(String typeTaux) {
        this.typeTaux = typeTaux;
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
        if (!(object instanceof YvsStatGrhTauxContribution)) {
            return false;
        }
        YvsStatGrhTauxContribution other = (YvsStatGrhTauxContribution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.statistique.YvsStatTauxContribution[ id=" + id + " ]";
    }

}

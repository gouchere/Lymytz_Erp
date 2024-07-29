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

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_libelle_droit_fin_contrat")
@NamedQueries({
    @NamedQuery(name = "YvsGrhLibelleDroitFinContrat.findAll", query = "SELECT y FROM YvsGrhLibelleDroitFinContrat y WHERE y.paramContrat=:contrat ORDER BY Y.libelle ASC"),
    @NamedQuery(name = "YvsGrhLibelleDroitFinContrat.findById", query = "SELECT y FROM YvsGrhLibelleDroitFinContrat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhLibelleDroitFinContrat.findByLibelle", query = "SELECT y FROM YvsGrhLibelleDroitFinContrat y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsGrhLibelleDroitFinContrat.findByActif", query = "SELECT y FROM YvsGrhLibelleDroitFinContrat y WHERE y.actif = :actif")})
public class YvsGrhLibelleDroitFinContrat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @SequenceGenerator(sequenceName = "yvs_grh_libelle_droit_fin_contrat_id_seq", name = "yvs_grh_libelle_droit_fin_contrat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_libelle_droit_fin_contrat_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "param_contrat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhParamContrat paramContrat;

    public YvsGrhLibelleDroitFinContrat() {
    }

    public YvsGrhLibelleDroitFinContrat(Long id) {
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

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsGrhParamContrat getParamContrat() {
        return paramContrat;
    }

    public void setParamContrat(YvsGrhParamContrat paramContrat) {
        this.paramContrat = paramContrat;
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
        if (!(object instanceof YvsGrhLibelleDroitFinContrat)) {
            return false;
        }
        YvsGrhLibelleDroitFinContrat other = (YvsGrhLibelleDroitFinContrat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.contrat.YvsGrhLibelleDroitFinContrat[ id=" + id + " ]";
    }

}

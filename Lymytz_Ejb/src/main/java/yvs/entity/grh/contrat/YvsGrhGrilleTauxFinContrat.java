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

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_grille_taux_fin_contrat")
@NamedQueries({
    @NamedQuery(name = "YvsGrhGrilleTauxFinContrat.findAll", query = "SELECT y FROM YvsGrhGrilleTauxFinContrat y"),
    @NamedQuery(name = "YvsGrhGrilleTauxFinContrat.findById", query = "SELECT y FROM YvsGrhGrilleTauxFinContrat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhGrilleTauxFinContrat.findByContrat", query = "SELECT y FROM YvsGrhGrilleTauxFinContrat y WHERE y.paramContrat = :contrat ORDER BY y.ancMin ASC"),
    @NamedQuery(name = "YvsGrhGrilleTauxFinContrat.findByAncMin", query = "SELECT y FROM YvsGrhGrilleTauxFinContrat y WHERE y.ancMin = :ancMin"),
    @NamedQuery(name = "YvsGrhGrilleTauxFinContrat.findByAncMax", query = "SELECT y FROM YvsGrhGrilleTauxFinContrat y WHERE y.ancMax = :ancMax"),
    @NamedQuery(name = "YvsGrhGrilleTauxFinContrat.findByTaux", query = "SELECT y FROM YvsGrhGrilleTauxFinContrat y WHERE y.taux = :taux")})
public class YvsGrhGrilleTauxFinContrat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_grille_taux_fin_contrat_id_seq", name = "yvs_grh_grille_taux_fin_contrat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_grille_taux_fin_contrat_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "anc_min")
    private Integer ancMin;
    @Column(name = "anc_max")
    private Integer ancMax;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @JoinColumn(name = "param_contrat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhParamContrat paramContrat;

    public YvsGrhGrilleTauxFinContrat() {
    }

    public YvsGrhGrilleTauxFinContrat(Long id) {
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

    public Integer getAncMin() {
        return ancMin;
    }

    public void setAncMin(Integer ancMin) {
        this.ancMin = ancMin;
    }

    public Integer getAncMax() {
        return ancMax;
    }

    public void setAncMax(Integer ancMax) {
        this.ancMax = ancMax;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
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
        if (!(object instanceof YvsGrhGrilleTauxFinContrat)) {
            return false;
        }
        YvsGrhGrilleTauxFinContrat other = (YvsGrhGrilleTauxFinContrat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.contrat.YvsGrhGrilleTauxFinContrat[ id=" + id + " ]";
    }

}

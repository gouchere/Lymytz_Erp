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
import yvs.entity.param.YvsGrhSecteurs;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_param_contrat")
@NamedQueries({
    @NamedQuery(name = "YvsGrhParamContrat.findAll", query = "SELECT y FROM YvsGrhParamContrat y"),
    @NamedQuery(name = "YvsGrhParamContrat.findBySecteur", query = "SELECT y FROM YvsGrhParamContrat y WHERE y.secteur = :secteur"),
    @NamedQuery(name = "YvsGrhParamContrat.findByPeriodeReference", query = "SELECT y FROM YvsGrhParamContrat y WHERE y.periodeReference = :periodeReference"),
    @NamedQuery(name = "YvsGrhParamContrat.findByFormuleSalaire", query = "SELECT y FROM YvsGrhParamContrat y WHERE y.formuleSalaire = :formuleSalaire"),
    @NamedQuery(name = "YvsGrhParamContrat.findByFormulePreavis", query = "SELECT y FROM YvsGrhParamContrat y WHERE y.formulePreavis = :formulePreavis")})
public class YvsGrhParamContrat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_param_contrat_id_seq", name = "yvs_grh_param_contrat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_param_contrat_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "periode_reference")
    private Integer periodeReference;
    @Size(max = 2147483647)
    @Column(name = "formule_salaire")
    private String formuleSalaire;
    @Size(max = 2147483647)
    @Column(name = "formule_preavis")
    private String formulePreavis;
    @JoinColumn(name = "secteur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhSecteurs secteur;
    @Column(name = "anciennete_requise")
    private Double ancienneteRequise;

    public YvsGrhParamContrat() {
    }

    public YvsGrhParamContrat(Long id) {
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

    public Integer getPeriodeReference() {
        return periodeReference;
    }

    public void setPeriodeReference(Integer periodeReference) {
        this.periodeReference = periodeReference;
    }

    public String getFormuleSalaire() {
        return formuleSalaire;
    }

    public void setFormuleSalaire(String formuleSalaire) {
        this.formuleSalaire = formuleSalaire;
    }

    public String getFormulePreavis() {
        return formulePreavis;
    }

    public void setFormulePreavis(String formulePreavis) {
        this.formulePreavis = formulePreavis;
    }

    public YvsGrhSecteurs getSecteur() {
        return secteur;
    }

    public void setSecteur(YvsGrhSecteurs secteur) {
        this.secteur = secteur;
    }

    public Double getAncienneteRequise() {
        return ancienneteRequise;
    }

    public void setAncienneteRequise(Double ancienneteRequise) {
        this.ancienneteRequise = ancienneteRequise;
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
        if (!(object instanceof YvsGrhParamContrat)) {
            return false;
        }
        YvsGrhParamContrat other = (YvsGrhParamContrat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.contrat.YvsGrhParamContrat[ id=" + id + " ]";
    }

}

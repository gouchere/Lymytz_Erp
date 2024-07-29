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
import yvs.entity.grh.personnel.YvsGrhContratEmps;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_planif_salaire_contrat")
@NamedQueries({
    @NamedQuery(name = "YvsGrhPlanifSalaireContrat.findAll", query = "SELECT y FROM YvsGrhPlanifSalaireContrat y"),
    @NamedQuery(name = "YvsGrhPlanifSalaireContrat.findById", query = "SELECT y FROM YvsGrhPlanifSalaireContrat y WHERE y.id = :id")})
public class YvsGrhPlanifSalaireContrat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_planif_salaire_employe_id_seq", name = "yvs_grh_planif_salaire_employe_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_planif_salaire_employe_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "planification", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhOrdreCalculSalaire planification;
    @JoinColumn(name = "contrat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhContratEmps contrat;

    public YvsGrhPlanifSalaireContrat() {
    }

    public YvsGrhPlanifSalaireContrat(Long id) {
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

    public YvsGrhOrdreCalculSalaire getPlanification() {
        return planification;
    }

    public void setPlanification(YvsGrhOrdreCalculSalaire planification) {
        this.planification = planification;
    }

    public YvsGrhContratEmps getContrat() {
        return contrat;
    }

    public void setContrat(YvsGrhContratEmps contrat) {
        this.contrat = contrat;
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
        if (!(object instanceof YvsGrhPlanifSalaireContrat)) {
            return false;
        }
        YvsGrhPlanifSalaireContrat other = (YvsGrhPlanifSalaireContrat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhPlanifSalaireContrat[ id=" + id + " ]";
    }

}

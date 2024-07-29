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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_historique_pr")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsHistoriquePr.findById", query = "SELECT y FROM YvsHistoriquePr y WHERE y.id = :id")
})
public class YvsHistoriquePr implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_historique_pr_id_seq", name = "yvs_historique_pr_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_historique_pr_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_evaluation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEvaluation;
    @Column(name = "pr")
    private Double pr;
    @Column(name = "conditionnement")
    private Long conditionnement;
    @Column(name = "depot")
    private Long depot;
    @Transient
    private boolean select;

    public YvsHistoriquePr() {
    }

    public YvsHistoriquePr(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateEvaluation() {
        return dateEvaluation;
    }

    public void setDateEvaluation(Date dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }

    public Double getPr() {
        return pr != null ? pr : 0;
    }

    public void setPr(Double pr) {
        this.pr = pr;
    }

    public Long getConditionnement() {
        return conditionnement != null ? conditionnement : 0;
    }

    public void setConditionnement(Long conditionnement) {
        this.conditionnement = conditionnement;
    }

    public Long getDepot() {
        return depot != null ? depot : 0;
    }

    public void setDepot(Long depot) {
        this.depot = depot;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
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
        if (!(object instanceof YvsHistoriquePr)) {
            return false;
        }
        YvsHistoriquePr other = (YvsHistoriquePr) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.YvsHistoriquePr[ id=" + id + " ]";
    }

}

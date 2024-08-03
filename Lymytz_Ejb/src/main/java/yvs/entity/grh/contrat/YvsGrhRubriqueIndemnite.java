/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.contrat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.param.YvsSocietes;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_rubrique_indemnite")
@NamedQueries({
    @NamedQuery(name = "YvsGrhRubriqueIndemnite.findAll", query = "SELECT y FROM YvsGrhRubriqueIndemnite y WHERE y.societe=:societe ORDER BY y.ordre ASC"),
    @NamedQuery(name = "YvsGrhRubriqueIndemnite.findById", query = "SELECT y FROM YvsGrhRubriqueIndemnite y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhRubriqueIndemnite.findByDesignation", query = "SELECT y FROM YvsGrhRubriqueIndemnite y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsGrhRubriqueIndemnite.findByOrdre", query = "SELECT y FROM YvsGrhRubriqueIndemnite y WHERE y.ordre = :ordre")})
public class YvsGrhRubriqueIndemnite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_rubrique_indemnite_id_seq", name = "yvs_grh_rubrique_indemnite_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_rubrique_indemnite_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "designation")
    private String designation;
    @Column(name = "code")
    private String code;
    @Column(name = "ordre")
    private Integer ordre;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "societe", referencedColumnName = "id")
    private YvsSocietes societe;
    
    @OneToMany(mappedBy = "rubrique")
    private List<YvsGrhElementsIndemnite> indemnites;

    public YvsGrhRubriqueIndemnite() {
    }

    public YvsGrhRubriqueIndemnite(Long id) {
        this.id = id;
    }

    public YvsGrhRubriqueIndemnite(Long id, String designation, int ordre) {
        this.id = id;
        this.designation = designation;
        this.ordre = ordre;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<YvsGrhElementsIndemnite> getIndemnites() {
        return indemnites;
    }

    public void setIndemnites(List<YvsGrhElementsIndemnite> indemnites) {
        this.indemnites = indemnites;
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
        if (!(object instanceof YvsGrhRubriqueIndemnite)) {
            return false;
        }
        YvsGrhRubriqueIndemnite other = (YvsGrhRubriqueIndemnite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.contrat.YvsGrhRubriqueIndemnite[ id=" + id + " ]";
    }

}

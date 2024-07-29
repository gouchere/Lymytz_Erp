/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.statistique;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "yvs_grh_doc_statistiques")
@NamedQueries({
    @NamedQuery(name = "YvsGrhDocStatistiques.findAll", query = "SELECT y FROM YvsGrhDocStatistiques y"),
    @NamedQuery(name = "YvsGrhDocStatistiques.findById", query = "SELECT y FROM YvsGrhDocStatistiques y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhDocStatistiques.findByDesignationEtat", query = "SELECT y FROM YvsGrhDocStatistiques y WHERE y.designationEtat = :designationEtat"),
    @NamedQuery(name = "YvsGrhDocStatistiques.findByCode", query = "SELECT y FROM YvsGrhDocStatistiques y WHERE y.code = :code")})
public class YvsGrhDocStatistiques implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_doc_statistiques_id_seq", name = "yvs_grh_doc_statistiques_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_doc_statistiques_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "designation_etat")
    private String designationEtat;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    @OneToMany(mappedBy = "etat")
    private List<YvsGrhElementStatistique> yvsGrhElementStatistiqueList;

    public YvsGrhDocStatistiques() {
    }

    public YvsGrhDocStatistiques(Integer id) {
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

    public String getDesignationEtat() {
        return designationEtat;
    }

    public void setDesignationEtat(String designationEtat) {
        this.designationEtat = designationEtat;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<YvsGrhElementStatistique> getYvsGrhElementStatistiqueList() {
        return yvsGrhElementStatistiqueList;
    }

    public void setYvsGrhElementStatistiqueList(List<YvsGrhElementStatistique> yvsGrhElementStatistiqueList) {
        this.yvsGrhElementStatistiqueList = yvsGrhElementStatistiqueList;
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
        if (!(object instanceof YvsGrhDocStatistiques)) {
            return false;
        }
        YvsGrhDocStatistiques other = (YvsGrhDocStatistiques) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.statistique.YvsGrhDocStatistiques[ id=" + id + " ]";
    }

}

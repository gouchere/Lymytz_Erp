/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

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
import yvs.entity.grh.activite.YvsGrhSanctionEmps;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_sanction")
@NamedQueries({
    @NamedQuery(name = "YvsGrhSanction.findAll", query = "SELECT y FROM YvsGrhSanction y"),
    @NamedQuery(name = "YvsGrhSanction.findById", query = "SELECT y FROM YvsGrhSanction y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhSanction.findByActif", query = "SELECT y FROM YvsGrhSanction y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsGrhSanction.findByCode", query = "SELECT y FROM YvsGrhSanction y WHERE y.code = :code"),
    @NamedQuery(name = "YvsGrhSanction.findByFaute", query = "SELECT y FROM YvsGrhSanction y WHERE y.faute = :faute"),
    @NamedQuery(name = "YvsGrhSanction.findByDecision", query = "SELECT y FROM YvsGrhSanction y WHERE y.decision = :decision"),
    @NamedQuery(name = "YvsGrhSanction.findBySupp", query = "SELECT y FROM YvsGrhSanction y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsGrhSanction.findByPoint", query = "SELECT y FROM YvsGrhSanction y WHERE y.point = :point")})
public class YvsGrhSanction implements Serializable {

    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_sanction_id_seq", name = "yvs_grh_sanction_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_sanction_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "supp")
    private Boolean supp;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "point")
    private Double point;
    @JoinColumn(name = "faute", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhFauteSanction faute;
    @JoinColumn(name = "decision", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhDecisionSanction decision;

    public YvsGrhSanction() {
    }

    public YvsGrhSanction(Integer id) {
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

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public YvsGrhFauteSanction getFaute() {
        return faute;
    }

    public void setFaute(YvsGrhFauteSanction faute) {
        this.faute = faute;
    }

    public YvsGrhDecisionSanction getDecision() {
        return decision;
    }

    public void setDecision(YvsGrhDecisionSanction decision) {
        this.decision = decision;
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
        if (!(object instanceof YvsGrhSanction)) {
            return false;
        }
        YvsGrhSanction other = (YvsGrhSanction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsGrhSanction[ id=" + id + " ]";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}

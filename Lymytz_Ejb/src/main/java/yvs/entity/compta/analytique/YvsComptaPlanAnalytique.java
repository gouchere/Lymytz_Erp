/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.analytique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.dao.YvsEntity;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_plan_analytique")
@NamedQueries({
    @NamedQuery(name = "YvsComptaPlanAnalytique.findAll", query = "SELECT y FROM YvsComptaPlanAnalytique y"),
    @NamedQuery(name = "YvsComptaPlanAnalytique.findAlls", query = "SELECT y FROM YvsComptaPlanAnalytique y WHERE y.societe = :societe ORDER BY y.codePlan"),
    @NamedQuery(name = "YvsComptaPlanAnalytique.findById", query = "SELECT y FROM YvsComptaPlanAnalytique y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaPlanAnalytique.findByIntitule", query = "SELECT y FROM YvsComptaPlanAnalytique y WHERE y.intitule = :intitule"),
    @NamedQuery(name = "YvsComptaPlanAnalytique.findByCodePlan", query = "SELECT y FROM YvsComptaPlanAnalytique y WHERE y.codePlan = :codePlan AND y.societe = :societe"),
    @NamedQuery(name = "YvsComptaPlanAnalytique.findByDescription", query = "SELECT y FROM YvsComptaPlanAnalytique y WHERE y.description = :description"),
    @NamedQuery(name = "YvsComptaPlanAnalytique.findByActif", query = "SELECT y FROM YvsComptaPlanAnalytique y WHERE y.actif = :actif AND y.societe = :societe"),
    @NamedQuery(name = "YvsComptaPlanAnalytique.findByDateUpdate", query = "SELECT y FROM YvsComptaPlanAnalytique y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaPlanAnalytique.findByDateSave", query = "SELECT y FROM YvsComptaPlanAnalytique y WHERE y.dateSave = :dateSave")})
public class YvsComptaPlanAnalytique extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_plan_analytique_id_seq", name = "yvs_compta_plan_analytique_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_plan_analytique_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "intitule")
    private String intitule;
    @Size(max = 2147483647)
    @Column(name = "code_plan")
    private String codePlan;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    @OneToMany(mappedBy = "plan")
    private List<YvsComptaCentreAnalytique> centres;

    @Transient
    private boolean new_;

    public YvsComptaPlanAnalytique() {
        centres = new ArrayList<>();
    }

    public YvsComptaPlanAnalytique(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaPlanAnalytique(Long id, String codePlan, String intitule) {
        this(id);
        this.intitule = intitule;
        this.codePlan = codePlan;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getCodePlan() {
        return codePlan;
    }

    public void setCodePlan(String codePlan) {
        this.codePlan = codePlan;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public List<YvsComptaCentreAnalytique> getCentres() {
        return centres;
    }

    public void setCentres(List<YvsComptaCentreAnalytique> centres) {
        this.centres = centres;
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
        if (!(object instanceof YvsComptaPlanAnalytique)) {
            return false;
        }
        YvsComptaPlanAnalytique other = (YvsComptaPlanAnalytique) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.analytique.YvsComptaPlanAnalytique[ id=" + id + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

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
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_params_taux_chomage_tech")
@NamedQueries({
    @NamedQuery(name = "YvsGrhParamsTauxChomageTech.findAll", query = "SELECT y FROM YvsGrhParamsTauxChomageTech y WHERE y.societe=:societe ORDER BY y.numMois ASC"),
    @NamedQuery(name = "YvsGrhParamsTauxChomageTech.findById", query = "SELECT y FROM YvsGrhParamsTauxChomageTech y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhParamsTauxChomageTech.findByCode", query = "SELECT y FROM YvsGrhParamsTauxChomageTech y WHERE y.codeTaux = :codeTaux AND y.societe=:societe"),
    @NamedQuery(name = "YvsGrhParamsTauxChomageTech.findByNumMoisSup", query = "SELECT y FROM YvsGrhParamsTauxChomageTech y WHERE y.numMois >= :numMois AND y.societe=:societe"),
    @NamedQuery(name = "YvsGrhParamsTauxChomageTech.findByNumMoisInf", query = "SELECT y FROM YvsGrhParamsTauxChomageTech y WHERE y.numMois <= :numMois AND y.societe=:societe"),
    @NamedQuery(name = "YvsGrhParamsTauxChomageTech.findByNumMois", query = "SELECT y FROM YvsGrhParamsTauxChomageTech y WHERE y.numMois = :numMois AND y.societe=:societe"),
    @NamedQuery(name = "YvsGrhParamsTauxChomageTech.findByTaux", query = "SELECT y FROM YvsGrhParamsTauxChomageTech y WHERE y.taux = :taux")})
public class YvsGrhParamsTauxChomageTech implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_params_taux_chomage_tech_id_seq", name = "yvs_grh_params_taux_chomage_tech_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_params_taux_chomage_tech_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "num_mois")
    private Integer numMois;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Column(name = "code_taux")
    private String codeTaux;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private YvsUsersAgence author;
    @Column(name = "actif")
    private Boolean actif;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "societe", referencedColumnName = "id")
    private YvsSocietes societe;

    public YvsGrhParamsTauxChomageTech() {
    }

    public YvsGrhParamsTauxChomageTech(Integer id) {
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

    public Integer getNumMois() {
        return numMois;
    }

    public void setNumMois(Integer numMois) {
        this.numMois = numMois;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public String getCodeTaux() {
        return codeTaux;
    }

    public void setCodeTaux(String codeTaux) {
        this.codeTaux = codeTaux;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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
        if (!(object instanceof YvsGrhParamsTauxChomageTech)) {
            return false;
        }
        YvsGrhParamsTauxChomageTech other = (YvsGrhParamsTauxChomageTech) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsGrhParamsTauxChomageTech[ id=" + id + " ]";
    }

}

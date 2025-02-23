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
import yvs.entity.param.YvsSocietes;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_decision_sanction")
@NamedQueries({
    @NamedQuery(name = "YvsGrhDecisionSanction.findAll", query = "SELECT y FROM YvsGrhDecisionSanction y WHERE y.societe = :societe AND y.actif = true"),
    @NamedQuery(name = "YvsGrhDecisionSanction.findById", query = "SELECT y FROM YvsGrhDecisionSanction y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhDecisionSanction.findByLibelle", query = "SELECT y FROM YvsGrhDecisionSanction y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsGrhDecisionSanction.findByDescriptionMotif", query = "SELECT y FROM YvsGrhDecisionSanction y WHERE y.descriptionMotif = :descriptionMotif"),
    @NamedQuery(name = "YvsGrhDecisionSanction.findByDuree", query = "SELECT y FROM YvsGrhDecisionSanction y WHERE y.duree = :duree"),
    @NamedQuery(name = "YvsGrhDecisionSanction.findByActif", query = "SELECT y FROM YvsGrhDecisionSanction y WHERE y.actif = :actif")})
public class YvsGrhDecisionSanction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_effet_sanction_id_seq", name = "yvs_grh_effet_sanction_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_effet_sanction_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "description_motif")
    private String descriptionMotif;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "duree")
    private Double duree;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    public YvsGrhDecisionSanction() {
    }

    public YvsGrhDecisionSanction(Integer id) {
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescriptionMotif() {
        return descriptionMotif;
    }

    public void setDescriptionMotif(String descriptionMotif) {
        this.descriptionMotif = descriptionMotif;
    }

    public Double getDuree() {
        return duree;
    }

    public void setDuree(Double duree) {
        this.duree = duree;
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
        if (!(object instanceof YvsGrhDecisionSanction)) {
            return false;
        }
        YvsGrhDecisionSanction other = (YvsGrhDecisionSanction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsGrhDecisionSanction[ id=" + id + " ]";
    }

}

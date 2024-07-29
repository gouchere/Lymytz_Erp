/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_plan_prelevement")
@NamedQueries({
    @NamedQuery(name = "YvsGrhPlanPrelevement.findAll", query = "SELECT y FROM YvsGrhPlanPrelevement y WHERE y.societe= :societe"),
    @NamedQuery(name = "YvsGrhPlanPrelevement.findAllCom", query = "SELECT y FROM YvsGrhPlanPrelevement y WHERE y.societe= :societe AND y.actif=true AND y.visibleEnGescom=true"),
    @NamedQuery(name = "YvsGrhPlanPrelevement.findById", query = "SELECT y FROM YvsGrhPlanPrelevement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhPlanPrelevement.findByReference", query = "SELECT y FROM YvsGrhPlanPrelevement y WHERE y.referencePlan = :referencePlan"),
    @NamedQuery(name = "YvsGrhPlanPrelevement.findDefault", query = "SELECT y FROM YvsGrhPlanPrelevement y WHERE y.societe = :societe AND y.defaut=true AND y.actif=true "),
    @NamedQuery(name = "YvsGrhPlanPrelevement.findBySociete", query = "SELECT y FROM YvsGrhPlanPrelevement y WHERE y.societe =:societe")
})
public class YvsGrhPlanPrelevement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_plan_prelevement_id_seq", name = "yvs_plan_prelevement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_plan_prelevement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "base_retenue")
    private Character baseRetenue;
    @Column(name = "valeur")
    private Double valeur;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "visible_en_gescom")
    private Boolean visibleEnGescom;
    @Column(name = "defaut")
    private Boolean defaut;
    @Column(name = "base_plan")
    private Character basePlan;
    @Column(name = "reference_plan")
    private String referencePlan;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;   
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    public YvsGrhPlanPrelevement() {
    }

    public YvsGrhPlanPrelevement(Long id) {
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

    public String getReferencePlan() {
        return referencePlan;
    }

    public void setReferencePlan(String referencePlan) {
        this.referencePlan = referencePlan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Character getBasePlan() {
        return basePlan != null ? basePlan : 'T';
    }

    public void setBasePlan(Character basePlan) {
        this.basePlan = basePlan;
    }

    public Boolean getVisibleEnGescom() {
        return visibleEnGescom != null ? visibleEnGescom : false;
    }

    public void setVisibleEnGescom(Boolean visibleEnGescom) {
        this.visibleEnGescom = visibleEnGescom;
    }

    public Character getBaseRetenue() {
        return baseRetenue != null ? baseRetenue : 'S';
    }

    public void setBaseRetenue(Character baseRetenue) {
        this.baseRetenue = baseRetenue;
    }

    public Double getValeur() {
        return valeur != null ? valeur : 0.0;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getDefaut() {
        return defaut != null ? defaut : false;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
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
        if (!(object instanceof YvsGrhPlanPrelevement)) {
            return false;
        }
        YvsGrhPlanPrelevement other = (YvsGrhPlanPrelevement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.YvsPlanPrelevement[ id=" + id + " ]";
    }

}

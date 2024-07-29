/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.base;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.production.equipe.YvsProdEquipeProduction;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_creneau_equipe_production")
@NamedQueries({
    @NamedQuery(name = "YvsProdCreneauEquipeProduction.findAll", query = "SELECT y FROM YvsProdCreneauEquipeProduction y ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsProdCreneauEquipeProduction.findById", query = "SELECT y FROM YvsProdCreneauEquipeProduction y WHERE y.id = :id ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsProdCreneauEquipeProduction.findByUsersPermanent", query = "SELECT y FROM YvsProdCreneauEquipeProduction y WHERE y.users = :users AND y.permanent = :permanent ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsProdCreneauEquipeProduction.findByUsers", query = "SELECT y FROM YvsProdCreneauEquipeProduction y WHERE y.users = :users ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsProdCreneauEquipeProduction.findByUsersActif", query = "SELECT y FROM YvsProdCreneauEquipeProduction y WHERE y.users = :users AND y.actif=true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsProdCreneauEquipeProduction.findByUsersDates", query = "SELECT y FROM YvsProdCreneauEquipeProduction y WHERE y.users = :users AND y.dateTravail BETWEEN :dateDebut AND :dateFin ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsProdCreneauEquipeProduction.findByUsersPermActif", query = "SELECT y FROM YvsProdCreneauEquipeProduction y WHERE y.users = :users AND y.permanent = true AND y.actif = true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsProdCreneauEquipeProduction.findByUserPlanif", query = "SELECT y FROM YvsProdCreneauEquipeProduction y WHERE y.users = :users AND y.actif=true AND y.dateTravail BETWEEN :debut AND :fin ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsProdCreneauEquipeProduction.findByUsersDate", query = "SELECT y FROM YvsProdCreneauEquipeProduction y WHERE y.users = :users AND y.dateTravail = :dateTravail ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsProdCreneauEquipeProduction.findByActif", query = "SELECT y FROM YvsProdCreneauEquipeProduction y WHERE y.actif = :actif ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsProdCreneauEquipeProduction.findSitePlanif", query = "SELECT DISTINCT y.site FROM YvsProdCreneauEquipeProduction y WHERE y.users=:user AND y.dateTravail BETWEEN :date1 AND :date2 AND y.actif = true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsProdCreneauEquipeProduction.findByTypeDepotPermanent", query = "SELECT y FROM YvsProdCreneauEquipeProduction y WHERE y.tranche = :type AND y.equipe = :equipe AND y.permanent = :permanent ORDER BY y.dateTravail DESC")})
public class YvsProdCreneauEquipeProduction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_creneau_equipe_production_id_seq", name = "yvs_prod_creneau_equipe_production_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_creneau_equipe_production_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_travail")
    @Temporal(TemporalType.DATE)
    private Date dateTravail;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "equipe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdEquipeProduction equipe;
    @JoinColumn(name = "tranche", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTrancheHoraire tranche;
    @JoinColumn(name = "site", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdSiteProduction site;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers users;

    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsProdCreneauEquipeProduction() {
    }

    public YvsProdCreneauEquipeProduction(Long id) {
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
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getPermanent() {
        return permanent != null ? permanent : false;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public Date getDateTravail() {
        return dateTravail;
    }

    public void setDateTravail(Date dateTravail) {
        this.dateTravail = dateTravail;
    }

    public YvsProdEquipeProduction getEquipe() {
        return equipe;
    }

    public void setEquipe(YvsProdEquipeProduction equipe) {
        this.equipe = equipe;
    }

    public YvsGrhTrancheHoraire getTranche() {
        return tranche;
    }

    public void setTranche(YvsGrhTrancheHoraire tranche) {
        this.tranche = tranche;
    }

    public YvsProdSiteProduction getSite() {
        return site;
    }

    public void setSite(YvsProdSiteProduction site) {
        this.site = site;
    }

    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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
        if (!(object instanceof YvsProdCreneauEquipeProduction)) {
            return false;
        }
        YvsProdCreneauEquipeProduction other = (YvsProdCreneauEquipeProduction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdCreneauEquipeProduction[ id=" + id + " ]";
    }

}

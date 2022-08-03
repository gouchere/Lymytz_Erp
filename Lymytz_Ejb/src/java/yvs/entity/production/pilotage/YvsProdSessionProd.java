/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.pilotage;

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
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.production.equipe.YvsProdEquipeProduction;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_session_prod")
@NamedQueries({
    @NamedQuery(name = "YvsProdSessionProd.findAll", query = "SELECT y FROM YvsProdSessionProd y"),
    @NamedQuery(name = "YvsProdSessionProd.findCurrent", query = "SELECT y FROM YvsProdSessionProd y WHERE y.producteur=:producteur AND y.dateSession=:date AND y.actif=true"),
    @NamedQuery(name = "YvsProdSessionProd.findOne", query = "SELECT y FROM YvsProdSessionProd y WHERE y.producteur=:producteur AND y.depot=:depot AND y.tranche=:tranche AND y.dateSession=:date"),
    @NamedQuery(name = "YvsProdSessionProd.findById", query = "SELECT y FROM YvsProdSessionProd y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdSessionProd.findByDateSession", query = "SELECT y FROM YvsProdSessionProd y WHERE y.dateSession = :dateSession"),
    @NamedQuery(name = "YvsProdSessionProd.findByDateSave", query = "SELECT y FROM YvsProdSessionProd y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsProdSessionProd.findByDateUpdate", query = "SELECT y FROM YvsProdSessionProd y WHERE y.dateUpdate = :dateUpdate")})
public class YvsProdSessionProd implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_session_prod_id_seq", name = "yvs_prod_session_prod_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_session_prod_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_session")
    @Temporal(TemporalType.DATE)
    private Date dateSession;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "actif")
    private Boolean actif;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe", referencedColumnName = "id")
    private YvsProdEquipeProduction equipe;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tranche", referencedColumnName = "id")
    private YvsGrhTrancheHoraire tranche;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producteur", referencedColumnName = "id")
    private YvsUsers producteur;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depot", referencedColumnName = "id")
    private YvsBaseDepots depot;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private YvsUsersAgence author;

    @Transient
    private YvsProdSessionOf ordreF;

    public YvsProdSessionProd() {
    }

    public YvsProdSessionProd(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0L;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateSession() {
        return dateSession;
    }

    public void setDateSession(Date dateSession) {
        this.dateSession = dateSession;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
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

    public YvsUsers getProducteur() {
        return producteur;
    }

    public void setProducteur(YvsUsers producteur) {
        this.producteur = producteur;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
    }

    public YvsProdSessionOf getOrdreF() {
        return ordreF;
    }

    public void setOrdreF(YvsProdSessionOf ordreF) {
        this.ordreF = ordreF;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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
        if (!(object instanceof YvsProdSessionProd)) {
            return false;
        }
        YvsProdSessionProd other = (YvsProdSessionProd) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdSessionProd[ id=" + id + " ]";
    }

}

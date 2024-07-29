/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.client;

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
import javax.persistence.Transient;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_tranche_reglement_client")
@NamedQueries({
    @NamedQuery(name = "YvsComTrancheReglementClient.findAll", query = "SELECT y FROM YvsComTrancheReglementClient y"),
    @NamedQuery(name = "YvsComTrancheReglementClient.findById", query = "SELECT y FROM YvsComTrancheReglementClient y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComTrancheReglementClient.findByPlanCategorie", query = "SELECT y FROM YvsComTrancheReglementClient y WHERE y.planCategorie = :plan"),
    @NamedQuery(name = "YvsComTrancheReglementClient.findByPlanClient", query = "SELECT y FROM YvsComTrancheReglementClient y WHERE y.planClient = :plan"),
    @NamedQuery(name = "YvsComTrancheReglementClient.findByNumero", query = "SELECT y FROM YvsComTrancheReglementClient y WHERE y.numero = :numero"),
    @NamedQuery(name = "YvsComTrancheReglementClient.findByTaux", query = "SELECT y FROM YvsComTrancheReglementClient y WHERE y.taux = :taux"),
    @NamedQuery(name = "YvsComTrancheReglementClient.findByIntervalJour", query = "SELECT y FROM YvsComTrancheReglementClient y WHERE y.intervalJour = :intervalJour")})
public class YvsComTrancheReglementClient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_tranche_reglement_client_id_seq", name = "yvs_com_tranche_reglement_client_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_tranche_reglement_client_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "numero")
    private Integer numero;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Column(name = "interval_jour")
    private Integer intervalJour;
    @JoinColumn(name = "plan_categorie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComPlanReglementCategorie planCategorie;
    @JoinColumn(name = "plan_client", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComPlanReglementClient planClient;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;

    public YvsComTrancheReglementClient() {
    }

    public YvsComTrancheReglementClient(Long id) {
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

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero != null ? numero : 0;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Double getTaux() {
        return taux != null ? taux : 0;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public Integer getIntervalJour() {
        return intervalJour != null ? intervalJour : 0;
    }

    public void setIntervalJour(Integer intervalJour) {
        this.intervalJour = intervalJour;
    }

    public YvsComPlanReglementCategorie getPlanCategorie() {
        return planCategorie;
    }

    public void setPlanCategorie(YvsComPlanReglementCategorie planCategorie) {
        this.planCategorie = planCategorie;
    }

    public YvsComPlanReglementClient getPlanClient() {
        return planClient;
    }

    public void setPlanClient(YvsComPlanReglementClient planClient) {
        this.planClient = planClient;
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
        if (!(object instanceof YvsComTrancheReglementClient)) {
            return false;
        }
        YvsComTrancheReglementClient other = (YvsComTrancheReglementClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.client.YvsComTrancheReglementClient[ id=" + id + " ]";
    }

}

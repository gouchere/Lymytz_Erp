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
import javax.persistence.Entity;
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
import yvs.dao.YvsEntity;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_element_add_contrats_client")
@NamedQueries({
    @NamedQuery(name = "YvsComElementAddContratsClient.findAll", query = "SELECT y FROM YvsComElementAddContratsClient y"),
    @NamedQuery(name = "YvsComElementAddContratsClient.findById", query = "SELECT y FROM YvsComElementAddContratsClient y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComElementAddContratsClient.findByMontant", query = "SELECT y FROM YvsComElementAddContratsClient y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComElementAddContratsClient.findByApplication", query = "SELECT y FROM YvsComElementAddContratsClient y WHERE y.application = :application"),
    @NamedQuery(name = "YvsComElementAddContratsClient.findByPeriodiciteApplication", query = "SELECT y FROM YvsComElementAddContratsClient y WHERE y.periodiciteApplication = :periodiciteApplication"),
    @NamedQuery(name = "YvsComElementAddContratsClient.findByDateSave", query = "SELECT y FROM YvsComElementAddContratsClient y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComElementAddContratsClient.findByDateUpdate", query = "SELECT y FROM YvsComElementAddContratsClient y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComElementAddContratsClient.findByContrat", query = "SELECT DISTINCT y FROM YvsComElementAddContratsClient y JOIN FETCH y.typeCout WHERE y.contrat = :contrat ORDER BY y.id ASC")})
public class YvsComElementAddContratsClient extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_element_add_contrats_client_id_seq", name = "yvs_com_element_add_contrats_client_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_element_add_contrats_client_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "application")
    private Character application;
    @Column(name = "periodicite_application")
    private Integer periodiciteApplication;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "type_cout", referencedColumnName = "id")
    @ManyToOne
    private YvsGrhTypeCout typeCout;
    @JoinColumn(name = "contrat", referencedColumnName = "id")
    @ManyToOne
    private YvsComContratsClient contrat;
    @Transient
    private boolean new_;

    public YvsComElementAddContratsClient() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComElementAddContratsClient(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Character getApplication() {
        return application != null ? String.valueOf(application).length() > 0 ? application : 'I' : 'I';
    }

    public void setApplication(Character application) {
        this.application = application;
    }

    public Integer getPeriodiciteApplication() {
        return periodiciteApplication != null ? periodiciteApplication : 0;
    }

    public void setPeriodiciteApplication(Integer periodiciteApplication) {
        this.periodiciteApplication = periodiciteApplication;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhTypeCout getTypeCout() {
        return typeCout;
    }

    public void setTypeCout(YvsGrhTypeCout typeCout) {
        this.typeCout = typeCout;
    }

    public YvsComContratsClient getContrat() {
        return contrat;
    }

    public void setContrat(YvsComContratsClient contrat) {
        this.contrat = contrat;
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
        if (!(object instanceof YvsComElementAddContratsClient)) {
            return false;
        }
        YvsComElementAddContratsClient other = (YvsComElementAddContratsClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.client.YvsComElementAddContratsClient[ id=" + id + " ]";
    }

}

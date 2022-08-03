/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.client;

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
import javax.xml.bind.annotation.XmlTransient;
import yvs.dao.YvsEntity;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_contrats_client")
@NamedQueries({
    @NamedQuery(name = "YvsComContratsClient.findAll", query = "SELECT y FROM YvsComContratsClient y"),
    @NamedQuery(name = "YvsComContratsClient.findById", query = "SELECT y FROM YvsComContratsClient y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComContratsClient.findByReference", query = "SELECT y FROM YvsComContratsClient y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsComContratsClient.findLikeReference", query = "SELECT y FROM YvsComContratsClient y WHERE y.client.tiers.societe = :societe AND y.reference LIKE :reference ORDER by y.reference DESC"),
    @NamedQuery(name = "YvsComContratsClient.findByReferenceExterne", query = "SELECT y FROM YvsComContratsClient y WHERE y.referenceExterne = :referenceExterne"),
    @NamedQuery(name = "YvsComContratsClient.findByType", query = "SELECT y FROM YvsComContratsClient y WHERE y.type = :type"),
    @NamedQuery(name = "YvsComContratsClient.findByDateExpiration", query = "SELECT y FROM YvsComContratsClient y WHERE y.dateExpiration = :dateExpiration"),
    @NamedQuery(name = "YvsComContratsClient.findByDateDebutFacturation", query = "SELECT y FROM YvsComContratsClient y WHERE y.dateDebutFacturation = :dateDebutFacturation"),
    @NamedQuery(name = "YvsComContratsClient.findByIntervalFacturation", query = "SELECT y FROM YvsComContratsClient y WHERE y.intervalFacturation = :intervalFacturation"),
    @NamedQuery(name = "YvsComContratsClient.findByPeriodeFacturation", query = "SELECT y FROM YvsComContratsClient y WHERE y.periodeFacturation = :periodeFacturation"),
    @NamedQuery(name = "YvsComContratsClient.findByActif", query = "SELECT y FROM YvsComContratsClient y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComContratsClient.findByDateSave", query = "SELECT y FROM YvsComContratsClient y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComContratsClient.findByDateUpdate", query = "SELECT y FROM YvsComContratsClient y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComContratsClient.findByClient", query = "SELECT DISTINCT y FROM YvsComContratsClient y LEFT JOIN FETCH y.elements e LEFT JOIN FETCH y.additionnels a "
            + "JOIN FETCH e.article JOIN FETCH e.article.article JOIN FETCH e.article.unite JOIN FETCH a.typeCout WHERE y.client = :client ORDER BY y.id ASC")})
public class YvsComContratsClient extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_contrats_client_id_seq", name = "yvs_com_contrats_client_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_contrats_client_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "reference")
    private String reference;
    @Size(max = 2147483647)
    @Column(name = "reference_externe")
    private String referenceExterne;
    @Column(name = "type")
    private Character type;
    @Column(name = "date_expiration")
    @Temporal(TemporalType.DATE)
    private Date dateExpiration;
    @Column(name = "date_debut_facturation")
    @Temporal(TemporalType.DATE)
    private Date dateDebutFacturation;
    @Column(name = "interval_facturation")
    private Integer intervalFacturation;
    @Column(name = "periode_facturation")
    private Character periodeFacturation;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne
    private YvsComClient client;
    @OneToMany(mappedBy = "contrat", fetch = FetchType.LAZY)
    private List<YvsComElementContratsClient> elements;
    @OneToMany(mappedBy = "contrat", fetch = FetchType.LAZY)
    private List<YvsComElementAddContratsClient> additionnels;
    @Transient
    private boolean new_;

    public YvsComContratsClient() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        elements = new ArrayList<>();
        additionnels = new ArrayList<>();
    }

    public YvsComContratsClient(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public Character getType() {
        return type != null ? String.valueOf(type).length() > 0 ? type : 'I' : 'I';
    }

    public void setType(Character type) {
        this.type = type;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Date getDateDebutFacturation() {
        return dateDebutFacturation != null ? dateDebutFacturation : new Date();
    }

    public void setDateDebutFacturation(Date dateDebutFacturation) {
        this.dateDebutFacturation = dateDebutFacturation;
    }

    public Integer getIntervalFacturation() {
        return intervalFacturation != null ? intervalFacturation : 0;
    }

    public void setIntervalFacturation(Integer intervalFacturation) {
        this.intervalFacturation = intervalFacturation;
    }

    public Character getPeriodeFacturation() {
        return periodeFacturation != null ? String.valueOf(periodeFacturation).length() > 0 ? periodeFacturation : 'M' : 'M';
    }

    public void setPeriodeFacturation(Character periodeFacturation) {
        this.periodeFacturation = periodeFacturation;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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

    public YvsComClient getClient() {
        return client;
    }

    public void setClient(YvsComClient client) {
        this.client = client;
    }

    @XmlTransient
    public List<YvsComElementContratsClient> getElements() {
        return elements;
    }

    public void setElements(List<YvsComElementContratsClient> elements) {
        this.elements = elements;
    }

    @XmlTransient
    public List<YvsComElementAddContratsClient> getAdditionnels() {
        return additionnels;
    }

    public void setAdditionnels(List<YvsComElementAddContratsClient> additionnels) {
        this.additionnels = additionnels;
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
        if (!(object instanceof YvsComContratsClient)) {
            return false;
        }
        YvsComContratsClient other = (YvsComContratsClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.client.YvsComContratsClient[ id=" + id + " ]";
    }

}

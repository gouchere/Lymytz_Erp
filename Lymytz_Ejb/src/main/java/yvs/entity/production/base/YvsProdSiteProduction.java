/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.base;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_site_production")
@NamedQueries({
    @NamedQuery(name = "YvsProdSiteProduction.findAll", query = "SELECT y FROM YvsProdSiteProduction y WHERE y.agence.societe = :societe"),
    @NamedQuery(name = "YvsProdSiteProduction.findById", query = "SELECT y FROM YvsProdSiteProduction y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdSiteProduction.findByReference", query = "SELECT y FROM YvsProdSiteProduction y WHERE y.reference = :reference AND y.agence.societe = :societe"),
    @NamedQuery(name = "YvsProdSiteProduction.findByDesignation", query = "SELECT y FROM YvsProdSiteProduction y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsProdSiteProduction.findByDescription", query = "SELECT y FROM YvsProdSiteProduction y WHERE y.description = :description"),
    @NamedQuery(name = "YvsProdSiteProduction.findByAdresse", query = "SELECT y FROM YvsProdSiteProduction y WHERE y.adresse = :adresse")})
public class YvsProdSiteProduction implements Serializable {
    @OneToMany(mappedBy = "site")
    private List<YvsProdDefaultDepotComposants> yvsProdDefaultDepotComposantsList;

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_site_production_id_seq", name = "yvs_prod_site_production_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_site_production_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "reference")
    private String reference;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "adresse")
    private String adresse;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "site")
    private List<YvsBaseArticleSite> articles;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsProdSiteProduction() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        articles = new ArrayList<>();
    }

    public YvsProdSiteProduction(Integer id) {
        this();
        this.id = id;
    }

    public YvsProdSiteProduction(Integer id, String reference, String designation) {
        this(id);
        this.reference = reference;
        this.designation = designation;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
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

    public Integer getId() {
        return id != null ? id : 0;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDesignation() {
        return designation != null ? designation : "";
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient
    public List<YvsBaseArticleSite> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticleSite> articles) {
        this.articles = articles;
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
        if (!(object instanceof YvsProdSiteProduction)) {
            return false;
        }
        YvsProdSiteProduction other = (YvsProdSiteProduction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdSiteProduction[ id=" + id + " ]";
    }

    public List<YvsProdDefaultDepotComposants> getYvsProdDefaultDepotComposantsList() {
        return yvsProdDefaultDepotComposantsList;
    }

    public void setYvsProdDefaultDepotComposantsList(List<YvsProdDefaultDepotComposants> yvsProdDefaultDepotComposantsList) {
        this.yvsProdDefaultDepotComposantsList = yvsProdDefaultDepotComposantsList;
    }

}

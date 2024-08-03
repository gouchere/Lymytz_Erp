/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
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
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_emplacement_depot")
@NamedQueries({
    @NamedQuery(name = "YvsBaseEmplacementDepot.findAll", query = "SELECT y FROM YvsBaseEmplacementDepot y WHERE y.depot.agence.societe = :societe"),
    @NamedQuery(name = "YvsBaseEmplacementDepot.findByDepot", query = "SELECT y FROM YvsBaseEmplacementDepot y WHERE y.depot = :depot"),
    @NamedQuery(name = "YvsBaseEmplacementDepot.findByDepotDefaut", query = "SELECT y FROM YvsBaseEmplacementDepot y WHERE y.depot = :depot AND y.defaut = :true"),
    @NamedQuery(name = "YvsBaseEmplacementDepot.findById", query = "SELECT y FROM YvsBaseEmplacementDepot y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseEmplacementDepot.findByCode", query = "SELECT y FROM YvsBaseEmplacementDepot y WHERE y.code = :code"),
    @NamedQuery(name = "YvsBaseEmplacementDepot.findByCurrentCode", query = "SELECT y FROM YvsBaseEmplacementDepot y WHERE y.code = :code AND y.depot.agence.societe = :societe"),
    @NamedQuery(name = "YvsBaseEmplacementDepot.findByCodeDepot", query = "SELECT y FROM YvsBaseEmplacementDepot y WHERE y.code = :code AND y.depot = :depot"),
    @NamedQuery(name = "YvsBaseEmplacementDepot.findByDesignation", query = "SELECT y FROM YvsBaseEmplacementDepot y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseEmplacementDepot.findByDescription", query = "SELECT y FROM YvsBaseEmplacementDepot y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseEmplacementDepot.findByActif", query = "SELECT y FROM YvsBaseEmplacementDepot y WHERE y.actif = :actif")})
public class YvsBaseEmplacementDepot extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_emplacement_depot_id_seq", name = "yvs_base_emplacement_depot_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_emplacement_depot_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "defaut")
    private Boolean defaut;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depot;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseEmplacementDepot parent;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "emplacement", fetch = FetchType.LAZY)
    private List<YvsBaseArticleEmplacement> articles;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;
    @Transient
    private long idExterne;

    public YvsBaseEmplacementDepot() {
    }

    public YvsBaseEmplacementDepot(Long id) {
        this.id = id;
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

    public long getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(long idExterne) {
        this.idExterne = idExterne;
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

    public Long getId() {
        return id != null ? id : 0;
    }

    public Boolean getDefaut() {
        return defaut;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
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

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
    }

    public YvsBaseEmplacementDepot getParent() {
        return parent;
    }

    public void setParent(YvsBaseEmplacementDepot parent) {
        this.parent = parent;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient
    public List<YvsBaseArticleEmplacement> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticleEmplacement> articles) {
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
        if (!(object instanceof YvsBaseEmplacementDepot)) {
            return false;
        }
        YvsBaseEmplacementDepot other = (YvsBaseEmplacementDepot) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseEmplacementDepot[ id=" + id + " ]";
    }

}

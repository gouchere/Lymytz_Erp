/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_categorie_comptable")
@NamedQueries({
    @NamedQuery(name = "YvsBaseCategorieComptable.findAll", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.societe = :societe ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findById", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByCodeAppel", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.codeAppel = :codeAppel"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByCode", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.code = :code AND y.societe=:societe"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByNature", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.nature = :nature AND y.societe=:societe ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findBySupp", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByActif", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByOnline", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.societe = :societe AND y.venteOnline = :venteOnline"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByDesignation", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.designation = :designation"),

    @NamedQuery(name = "YvsBaseCategorieComptable.findAllNotIds", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.societe = :societe AND y.id NOT IN :ids ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findLikeCodeNature", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE (UPPER(y.designation) LIKE UPPER(:code) OR UPPER(y.codeAppel) LIKE UPPER(:code) OR UPPER(y.code) LIKE UPPER(:code)) AND y.societe = :societe AND y.nature = :nature")})
public class YvsBaseCategorieComptable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_catcompta_id_seq", name = "yvs_catcompta_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_catcompta_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 255)
    @Column(name = "code_appel")
    private String codeAppel;
    @Size(max = 255)
    @Column(name = "code")
    private String code;
    @Size(max = 255)
    @Column(name = "nature")
    private String nature;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "vente_online")
    private Boolean venteOnline;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private boolean new_;

    public YvsBaseCategorieComptable() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseCategorieComptable(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseCategorieComptable(Long id, String code) {
        this(id);
        this.code = code;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAppel() {
        return codeAppel;
    }

    public void setCodeAppel(String codeAppel) {
        this.codeAppel = codeAppel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public Boolean getSupp() {
        return supp != null ? supp : false;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getDesignation() {
        return designation != null ? designation : "";
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public Boolean getVenteOnline() {
        return venteOnline != null ? venteOnline : false;
    }

    public void setVenteOnline(Boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsBaseCategorieComptable)) {
            return false;
        }
        YvsBaseCategorieComptable other = (YvsBaseCategorieComptable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseCategorieComptable[ id=" + id + " ]";
    }

    }

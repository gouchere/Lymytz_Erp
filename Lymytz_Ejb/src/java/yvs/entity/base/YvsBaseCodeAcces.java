/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

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
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.commercial.rrr.YvsComRemise;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_base_code_acces")
@NamedQueries({
    @NamedQuery(name = "YvsBaseCodeAcces.findAll", query = "SELECT y FROM YvsBaseCodeAcces y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsBaseCodeAcces.findById", query = "SELECT y FROM YvsBaseCodeAcces y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseCodeAcces.findByCode", query = "SELECT y FROM YvsBaseCodeAcces y WHERE y.code = :code AND y.societe = :societe"),
    @NamedQuery(name = "YvsBaseCodeAcces.findByDescription", query = "SELECT y FROM YvsBaseCodeAcces y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseCodeAcces.findByDateSave", query = "SELECT y FROM YvsBaseCodeAcces y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseCodeAcces.findByDateUpdate", query = "SELECT y FROM YvsBaseCodeAcces y WHERE y.dateUpdate = :dateUpdate")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseCodeAcces implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_code_acces_id_seq", name = "yvs_base_code_acces_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_code_acces_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @OneToMany(mappedBy = "codeAcces")
    private List<YvsComRemise> remises;
    @Transient
    private List<String> elements;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsBaseCodeAcces() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        remises = new ArrayList<>();
        elements = new ArrayList<>();
    }

    public YvsBaseCodeAcces(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    @XmlTransient
    @JsonIgnore
    public List<String> getElements() {
        elements.clear();
        for (YvsComRemise r : remises) {
            elements.add("Remise : " + r.getRefRemise());
        }
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComRemise> getRemises() {
        return remises;
    }

    public void setRemises(List<YvsComRemise> remises) {
        this.remises = remises;
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
        if (!(object instanceof YvsBaseCodeAcces)) {
            return false;
        }
        YvsBaseCodeAcces other = (YvsBaseCodeAcces) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseCodeAcces[ id=" + id + " ]";
    }
}

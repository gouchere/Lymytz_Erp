/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.FetchType;
import javax.xml.bind.annotation.XmlTransient;
import yvs.dao.YvsEntity;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_base_classes_stat")
@NamedQueries({
    @NamedQuery(name = "YvsBaseClassesStat.findAll", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.societe=:societe"),
    @NamedQuery(name = "YvsBaseClassesStat.findNotIds", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.societe=:societe AND y.id NOT IN :ids"),
    @NamedQuery(name = "YvsBaseClassesStat.findById", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseClassesStat.findByCodeRef", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.codeRef = :codeRef AND y.societe=:societe"),
    @NamedQuery(name = "YvsBaseClassesStat.findByDesignation", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseClassesStat.findByActif", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseClassesStat.findByVisibleSynthese", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.visibleSynthese = :visibleSynthese"),
    @NamedQuery(name = "YvsBaseClassesStat.findByVisibleJournal", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.visibleJournal = :visibleJournal"),
    @NamedQuery(name = "YvsBaseClassesStat.findByAuthor", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.author = :author"),
    @NamedQuery(name = "YvsBaseClassesStat.findByDateSave", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseClassesStat.findByDateUpdate", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.dateUpdate = :dateUpdate")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseClassesStat extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_classes_stat_id_seq", name = "yvs_base_classes_stat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_classes_stat_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code_ref")
    private String codeRef;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "visible_synthese")
    private Boolean visibleSynthese;
    @Column(name = "visible_journal")
    private Boolean visibleJournal;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private YvsBaseClassesStat parent;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;

    public YvsBaseClassesStat() {
    }

    public YvsBaseClassesStat(Long id) {
        this.id = id;
    }

    public YvsBaseClassesStat(Long id, String codeRef) {
        this(id);
        this.codeRef = codeRef;
    }

    public YvsBaseClassesStat(Long id, String codeRef, String designation) {
        this(id, codeRef);
        this.designation = designation;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeRef() {
        return codeRef;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getVisibleSynthese() {
        return visibleSynthese != null ? visibleSynthese : false;
    }

    public void setVisibleSynthese(Boolean visibleSynthese) {
        this.visibleSynthese = visibleSynthese;
    }

    public Boolean getVisibleJournal() {
        return visibleJournal != null ? visibleJournal : false;
    }

    public void setVisibleJournal(Boolean visibleJournal) {
        this.visibleJournal = visibleJournal;
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
    public YvsBaseClassesStat getParent() {
        return parent;
    }

    public void setParent(YvsBaseClassesStat parent) {
        this.parent = parent;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsBaseClassesStat other = (YvsBaseClassesStat) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseClassesStat[ id=" + id + " ]";
    }

}

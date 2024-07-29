/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.users;

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
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_users_grade")
@NamedQueries({
    @NamedQuery(name = "YvsUsersGrade.findAll", query = "SELECT y FROM YvsUsersGrade y ORDER BY y.reference"),
    @NamedQuery(name = "YvsUsersGrade.findNotIds", query = "SELECT y FROM YvsUsersGrade y WHERE y.id NOT IN :ids ORDER BY y.reference"),
    @NamedQuery(name = "YvsUsersGrade.findByLibelle", query = "SELECT y FROM YvsUsersGrade y WHERE y.libelle=:libelle ORDER BY y.reference"),
    @NamedQuery(name = "YvsUsersGrade.findByReference", query = "SELECT y FROM YvsUsersGrade y WHERE y.reference=:reference ORDER BY y.reference"),
    @NamedQuery(name = "YvsUsersGrade.findById", query = "SELECT y FROM YvsUsersGrade y WHERE y.id = :id ORDER BY y.reference")})
public class YvsUsersGrade implements Serializable {   
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @SequenceGenerator(sequenceName = "yvs_users_grade_id_seq", name = "yvs_users_grade_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_users_grade_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "reference")
    private String reference;
    @Column(name = "libelle")
    private String libelle;
    
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersGrade author;

    public YvsUsersGrade() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsUsersGrade(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return (id == null) ? 0 : id;
    }

    public void setId(Long id) {
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @XmlTransient  @JsonIgnore
    public YvsUsersGrade getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersGrade author) {
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
        if (!(object instanceof YvsUsersGrade)) {
            return false;
        }
        YvsUsersGrade other = (YvsUsersGrade) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsUsersGrade[ id=" + id + " ]";
    }

    }

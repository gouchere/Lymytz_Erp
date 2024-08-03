/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_base_type_doc_categorie")
@NamedQueries({
    @NamedQuery(name = "YvsBaseTypeDocCategorie.findAll", query = "SELECT y FROM YvsBaseTypeDocCategorie y WHERE y.typeDoc.societe = :societe"),
    @NamedQuery(name = "YvsBaseTypeDocCategorie.findByType", query = "SELECT y FROM YvsBaseTypeDocCategorie y WHERE y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsBaseTypeDocCategorie.findOne", query = "SELECT y FROM YvsBaseTypeDocCategorie y WHERE y.typeDoc = :typeDoc AND y.categorie = :categorie"),
    @NamedQuery(name = "YvsBaseTypeDocCategorie.findById", query = "SELECT y FROM YvsBaseTypeDocCategorie y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseTypeDocCategorie.findByDateSave", query = "SELECT y FROM YvsBaseTypeDocCategorie y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseTypeDocCategorie.findByDateUpdate", query = "SELECT y FROM YvsBaseTypeDocCategorie y WHERE y.dateUpdate = :dateUpdate")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseTypeDocCategorie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_type_doc_categorie_id_seq", name = "yvs_base_type_doc_categorie_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_type_doc_categorie_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "categorie")
    private String categorie;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "type_doc", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTypeDocDivers typeDoc;

    public YvsBaseTypeDocCategorie() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseTypeDocCategorie(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseTypeDocCategorie(Long id, String libelle) {
        this(id);
        this.categorie = libelle;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseTypeDocDivers getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(YvsBaseTypeDocDivers typeDoc) {
        this.typeDoc = typeDoc;
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
        if (!(object instanceof YvsBaseTypeDocCategorie)) {
            return false;
        }
        YvsBaseTypeDocCategorie other = (YvsBaseTypeDocCategorie) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsBaseTypeDocDivers[ id=" + id + " ]";
    }

}

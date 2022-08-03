/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_categorie_fournisseur")
@NamedQueries({
    @NamedQuery(name = "YvsBaseCategorieFournisseur.findAll", query = "SELECT y FROM YvsBaseCategorieFournisseur y WHERE y.societe = :societe ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseCategorieFournisseur.findById", query = "SELECT y FROM YvsBaseCategorieFournisseur y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseCategorieFournisseur.findByNotId", query = "SELECT y FROM YvsBaseCategorieFournisseur y WHERE y.societe = :societe AND y.id != :id ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseCategorieFournisseur.findByCode", query = "SELECT y FROM YvsBaseCategorieFournisseur y WHERE y.code = :code"),
    @NamedQuery(name = "YvsBaseCategorieFournisseur.findByCurrentCode", query = "SELECT y FROM YvsBaseCategorieFournisseur y WHERE y.code = :code AND y.societe = :societe"),
    @NamedQuery(name = "YvsBaseCategorieFournisseur.findByLibelle", query = "SELECT y FROM YvsBaseCategorieFournisseur y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsBaseCategorieFournisseur.findByDescription", query = "SELECT y FROM YvsBaseCategorieFournisseur y WHERE y.description = :description")})
public class YvsBaseCategorieFournisseur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_categorie_fournisseur_id_seq", name = "yvs_base_categorie_fournisseur_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_categorie_fournisseur_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCategorieFournisseur parent;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;

    public YvsBaseCategorieFournisseur() {
    }

    public YvsBaseCategorieFournisseur(Long id) {
        this.id = id;
    }

    public YvsBaseCategorieFournisseur(Long id, String libelle) {
        this.id = id;
        this.libelle = libelle;
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsBaseCategorieFournisseur getParent() {
        return parent;
    }

    public void setParent(YvsBaseCategorieFournisseur parent) {
        this.parent = parent;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsBaseCategorieFournisseur)) {
            return false;
        }
        YvsBaseCategorieFournisseur other = (YvsBaseCategorieFournisseur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseCategorieFournisseur[ id=" + id + " ]";
    }

}

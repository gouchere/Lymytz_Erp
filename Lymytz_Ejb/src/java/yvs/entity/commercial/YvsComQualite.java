/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial;

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
import javax.xml.bind.annotation.XmlRootElement;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_qualite")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsComQualite.findAll", query = "SELECT y FROM YvsComQualite y WHERE y.societe = :societe ORDER BY y.code"),
    @NamedQuery(name = "YvsComQualite.findById", query = "SELECT y FROM YvsComQualite y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComQualite.findByCode", query = "SELECT y FROM YvsComQualite y WHERE y.code = :code AND y.societe = :societe ORDER BY y.code"),
    @NamedQuery(name = "YvsComQualite.findByLibelle", query = "SELECT y FROM YvsComQualite y WHERE y.libelle = :libelle AND y.societe = :societe ORDER BY y.code"),
    @NamedQuery(name = "YvsComQualite.findBySupp", query = "SELECT y FROM YvsComQualite y WHERE y.supp = :supp ORDER BY y.code"),
    @NamedQuery(name = "YvsComQualite.findByActif", query = "SELECT y FROM YvsComQualite y WHERE y.actif = :actif ORDER BY y.code")})
public class YvsComQualite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_qualite_id_seq", name = "yvs_com_qualite_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_qualite_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsComQualite() {
    }

    public YvsComQualite(Long id) {
        this.id = id;
    }

    public YvsComQualite(Long id, String code, String libelle) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code != null ? code : "";
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle != null ? libelle : "";
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsComQualite)) {
            return false;
        }
        YvsComQualite other = (YvsComQualite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.YvsComQualite[ id=" + id + " ]";
    }

}

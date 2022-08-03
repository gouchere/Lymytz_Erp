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
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_point_livraison")
@NamedQueries({
    @NamedQuery(name = "YvsBasePointLivraison.findAll", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.societe = :societe ORDER BY y.ville, y.libelle"),
    @NamedQuery(name = "YvsBasePointLivraison.findById", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.id = :id ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBasePointLivraison.findByLibelle", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.libelle = :libelle ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBasePointLivraison.findByVille", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.ville = :ville ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBasePointLivraison.findBySocieteVille", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.societe = :societe AND y.ville = :ville ORDER BY y.libelle")})
public class YvsBasePointLivraison extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_point_livraison_id_seq", name = "yvs_base_point_livraison_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_point_livraison_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "telephone")
    private String telephone;
    @Size(max = 2147483647)
    @Column(name = "lieu_dit")
    private String lieuDit;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;

    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComClient client;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "ville", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire ville;

    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsBasePointLivraison() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBasePointLivraison(Long id) {
        this();
        this.id = id;
    }

    public YvsBasePointLivraison(Long id, String libelle) {
        this(id);
        this.libelle = libelle;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsDictionnaire getVille() {
        return ville;
    }

    public void setVille(YvsDictionnaire ville) {
        this.ville = ville;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLieuDit() {
        return lieuDit;
    }

    public void setLieuDit(String lieuDit) {
        this.lieuDit = lieuDit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YvsComClient getClient() {
        return client;
    }

    public void setClient(YvsComClient client) {
        this.client = client;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsBasePointLivraison)) {
            return false;
        }
        YvsBasePointLivraison other = (YvsBasePointLivraison) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBasePointLivraison[ id=" + id + " ]";
    }

}

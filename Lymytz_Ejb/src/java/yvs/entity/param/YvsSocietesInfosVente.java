/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_societes_infos_vente")
@NamedQueries({
    @NamedQuery(name = "YvsSocieteInfosVente.findAll", query = "SELECT y FROM YvsSocietesInfosVente y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsSocieteInfosVente.findById", query = "SELECT y FROM YvsSocietesInfosVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsSocieteInfosVente.findByTypeLivraison", query = "SELECT y FROM YvsSocietesInfosVente y WHERE y.typeLivraison = :typeLivraison")})
public class YvsSocietesInfosVente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_societe_infos_vente_id_seq", name = "yvs_societe_infos_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_societe_infos_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "display_catalogue_on_list")
    private Boolean displayCatalogueOnList;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "type_livraison", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeCout typeLivraison;

    public YvsSocietesInfosVente() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsSocietesInfosVente(Long id) {
        this();
        this.id = id;
    }

    public YvsSocietesInfosVente(Long id, YvsGrhTypeCout typeLivraison) {
        this(id);
        this.typeLivraison = typeLivraison;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDisplayCatalogueOnList() {
        return displayCatalogueOnList != null ? displayCatalogueOnList : false;
    }

    public void setDisplayCatalogueOnList(Boolean displayCatalogueOnList) {
        this.displayCatalogueOnList = displayCatalogueOnList;
    }

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

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsGrhTypeCout getTypeLivraison() {
        return typeLivraison;
    }

    public void setTypeLivraison(YvsGrhTypeCout typeLivraison) {
        this.typeLivraison = typeLivraison;
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
        if (!(object instanceof YvsSocietesInfosVente)) {
            return false;
        }
        YvsSocietesInfosVente other = (YvsSocietesInfosVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.YvsSocieteInfosVente[ id=" + id + " ]";
    }

}

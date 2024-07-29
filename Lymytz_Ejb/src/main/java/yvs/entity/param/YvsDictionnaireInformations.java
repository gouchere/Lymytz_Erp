/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_dictionnaire_informations")
@NamedQueries({
    @NamedQuery(name = "YvsDictionnaireInformations.findAll", query = "SELECT y FROM YvsDictionnaireInformations y"),
    @NamedQuery(name = "YvsDictionnaireInformations.findById", query = "SELECT y FROM YvsDictionnaireInformations y WHERE y.id = :id"),
    @NamedQuery(name = "YvsDictionnaireInformations.findByBarent", query = "SELECT y.lieux FROM YvsDictionnaireInformations y WHERE y.lieux.parent = :parent AND y.activeLivraison=true AND y.societe=:societe ORDER BY y.lieux.libele"),
    @NamedQuery(name = "YvsDictionnaireInformations.findByActiveLivraison", query = "SELECT y FROM YvsDictionnaireInformations y WHERE y.activeLivraison = :activeLivraison"),
    @NamedQuery(name = "YvsDictionnaireInformations.findByDateUpdate", query = "SELECT y FROM YvsDictionnaireInformations y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsDictionnaireInformations.findByDateSave", query = "SELECT y FROM YvsDictionnaireInformations y WHERE y.dateSave = :dateSave"),

    @NamedQuery(name = "YvsDictionnaireInformations.findOne", query = "SELECT y FROM YvsDictionnaireInformations y WHERE y.lieux = :lieux AND y.societe = :societe")})
public class YvsDictionnaireInformations extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_dictionnaire_informations_id_seq")
    @SequenceGenerator(sequenceName = "yvs_dictionnaire_informations_id_seq", allocationSize = 1, name = "yvs_dictionnaire_informations_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "active_livraison")
    private Boolean activeLivraison;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP) 
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "lieux", referencedColumnName = "id")
    @ManyToOne
    private YvsDictionnaire lieux;

    public YvsDictionnaireInformations() {
    }

    public YvsDictionnaireInformations(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActiveLivraison() {
        return activeLivraison != null ? activeLivraison : false;
    }

    public void setActiveLivraison(Boolean activeLivraison) {
        this.activeLivraison = activeLivraison;
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

    public YvsDictionnaire getLieux() {
        return lieux;
    }

    public void setLieux(YvsDictionnaire lieux) {
        this.lieux = lieux;
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
        if (!(object instanceof YvsDictionnaireInformations)) {
            return false;
        }
        YvsDictionnaireInformations other = (YvsDictionnaireInformations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.YvsDictionnaireInformations[ id=" + id + " ]";
    }

}

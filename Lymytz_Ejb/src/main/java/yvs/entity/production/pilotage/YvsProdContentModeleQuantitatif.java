/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.production.pilotage;

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
import yvs.entity.production.base.YvsProdValeursQualitative;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_content_modele_quantitatif")
@NamedQueries({
    @NamedQuery(name = "YvsProdContentModeleQuantitatif.findAll", query = "SELECT y FROM YvsProdContentModeleQuantitatif y"),
    @NamedQuery(name = "YvsProdContentModeleQuantitatif.findById", query = "SELECT y FROM YvsProdContentModeleQuantitatif y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdContentModeleQuantitatif.findOne", query = "SELECT y FROM YvsProdContentModeleQuantitatif y WHERE y.model=:model AND y.valeur=:valeur"),
    @NamedQuery(name = "YvsProdContentModeleQuantitatif.findValByModel", query = "SELECT y.valeur FROM YvsProdContentModeleQuantitatif y WHERE y.model=:model "),
    @NamedQuery(name = "YvsProdContentModeleQuantitatif.findByDateSave", query = "SELECT y FROM YvsProdContentModeleQuantitatif y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsProdContentModeleQuantitatif.findByDateUpdate", query = "SELECT y FROM YvsProdContentModeleQuantitatif y WHERE y.dateUpdate = :dateUpdate")})
public class YvsProdContentModeleQuantitatif implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_content_modele_quantitatif_id_seq", name = "yvs_prod_content_modele_quantitatif_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_content_modele_quantitatif_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "valeur", referencedColumnName = "id")
    @ManyToOne
    private YvsProdValeursQualitative valeur;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne
    private YvsProdModeleQuantitatif model;

    public YvsProdContentModeleQuantitatif() {
    }

    public YvsProdContentModeleQuantitatif(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsProdValeursQualitative getValeur() {
        return valeur;
    }

    public void setValeur(YvsProdValeursQualitative valeur) {
        this.valeur = valeur;
    }

    public YvsProdModeleQuantitatif getModel() {
        return model;
    }

    public void setModel(YvsProdModeleQuantitatif model) {
        this.model = model;
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
        if (!(object instanceof YvsProdContentModeleQuantitatif)) {
            return false;
        }
        YvsProdContentModeleQuantitatif other = (YvsProdContentModeleQuantitatif) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdContentModeleQuantitatif[ id=" + id + " ]";
    }
    
}

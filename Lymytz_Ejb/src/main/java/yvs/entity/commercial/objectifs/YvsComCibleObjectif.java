/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.objectifs;

import java.io.Serializable;
import java.util.Comparator;
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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_com_cible_objectif")
@NamedQueries({
    @NamedQuery(name = "YvsComCibleObjectif.findAll", query = "SELECT y FROM YvsComCibleObjectif y"),
    @NamedQuery(name = "YvsComCibleObjectif.findById", query = "SELECT y FROM YvsComCibleObjectif y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCibleObjectif.findByTableExterne", query = "SELECT y FROM YvsComCibleObjectif y WHERE y.tableExterne = :tableExterne"),
    @NamedQuery(name = "YvsComCibleObjectif.findByLibelle", query = "SELECT y FROM YvsComCibleObjectif y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsComCibleObjectif.findByDateSave", query = "SELECT y FROM YvsComCibleObjectif y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComCibleObjectif.findByDateUpdate", query = "SELECT y FROM YvsComCibleObjectif y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComCibleObjectif.findByOneElt", query = "SELECT y FROM YvsComCibleObjectif y WHERE y.tableExterne=:table AND y.idExterne=:idExterne AND y.objectif=:model"),
    @NamedQuery(name = "YvsComCibleObjectif.findByModelTable", query = "SELECT y FROM YvsComCibleObjectif y WHERE y.tableExterne=:table AND y.objectif=:model"),
    @NamedQuery(name = "YvsComCibleObjectif.findByIdExterne", query = "SELECT y FROM YvsComCibleObjectif y WHERE y.idExterne = :idExterne")})
public class YvsComCibleObjectif implements Serializable, Comparator<YvsComCibleObjectif> {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_cible_objectif_id_seq", name = "yvs_com_cible_objectif_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_cible_objectif_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "table_externe")
    private String tableExterne;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "id_externe")
    private Long idExterne;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "objectif", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComModeleObjectif objectif;
    @Transient
    private int ordre;
    @Transient
    private boolean new_;

    public YvsComCibleObjectif() {
    }

    public YvsComCibleObjectif(Long id) {
        this.id = id;
    }

    public YvsComCibleObjectif(Long id, YvsComCibleObjectif y) {
        this.id = id;
        this.tableExterne = y.tableExterne;
        this.libelle = y.libelle;
        this.dateSave = y.dateSave;
        this.dateUpdate = y.dateUpdate;
        this.idExterne = y.idExterne;
        this.author = y.author;
        this.objectif = y.objectif;
        this.ordre = y.ordre;
        this.new_ = y.new_;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableExterne() {
        return tableExterne;
    }

    public void setTableExterne(String tableExterne) {
        this.tableExterne = tableExterne;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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

    public Long getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(Long idExterne) {
        this.idExterne = idExterne;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComModeleObjectif getObjectif() {
        return objectif;
    }

    public void setObjectif(YvsComModeleObjectif objectif) {
        this.objectif = objectif;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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
        if (!(object instanceof YvsComCibleObjectif)) {
            return false;
        }
        YvsComCibleObjectif other = (YvsComCibleObjectif) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.objectifs.YvsComCibleObjectif[ id=" + id + " ]";
    }

    @Override
    public int compare(YvsComCibleObjectif o, YvsComCibleObjectif o1) {
        if (o1 != null && o != null) {
            return (o.getOrdre() - o1.getOrdre());
        } else {
            return 1;
        }
    }

}

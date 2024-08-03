/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.produits.group;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import javax.persistence.*;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_borne_tranches")
@NamedQueries({
    @NamedQuery(name = "YvsBorneTranches.findAll", query = "SELECT y FROM YvsBorneTranches y"),
    @NamedQuery(name = "YvsBorneTranches.findById", query = "SELECT y FROM YvsBorneTranches y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBorneTranches.findByBorne", query = "SELECT y FROM YvsBorneTranches y WHERE y.borne = :borne"),
    @NamedQuery(name = "YvsBorneTranches.findByRemise", query = "SELECT y FROM YvsBorneTranches y WHERE y.remise = :remise"),
    @NamedQuery(name = "YvsBorneTranches.findByPrix", query = "SELECT y FROM YvsBorneTranches y WHERE y.prix = :prix"),
    @NamedQuery(name = "YvsBorneTranches.findByTranche", query = "SELECT y.id,y.borne,y.remise,y.prix,y.tranche.id FROM YvsBorneTranches y left join fetch y.tranche WHERE y.tranche = :tranche")})
public class YvsBorneTranches implements Serializable, Comparator<YvsBorneTranches> {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", columnDefinition = "BIGSERIAL")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_borne_tranches_id_seq")
    @SequenceGenerator(sequenceName = "yvs_borne_tranches_id_seq", allocationSize = 1, name = "yvs_borne_tranches_id_seq")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Basic(optional = false)
    @Column(name = "borne")
    private Double borne;
    @Basic(optional = false)
    @Column(name = "remise")
    private Double remise;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "prix")
    private Double prix;
    @JoinColumn(name = "tranche", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsTranches tranche;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
//    @EJB
//    @Transient
//    DaoInterfaceLocal dao;

    public YvsBorneTranches() {
    }

    public YvsBorneTranches(Long id) {
        this.id = id;
    }

    public YvsBorneTranches(Long id, Double borne, Double remise) {
        this.id = id;
        this.borne = borne;
        this.remise = remise;
    }

    public YvsBorneTranches(YvsBorneTranches b) {
        this.id = b.id;
        this.borne = b.borne;
        this.remise = b.remise;
        this.prix = b.prix;
        this.tranche = b.tranche;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBorne() {
        return borne;
    }

    public void setBorne(Double borne) {
        this.borne = borne;
    }

    public Double getRemise() {
        return remise;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public YvsTranches getTranche() {
        return tranche;
    }

    public void setTranche(YvsTranches tranche) {
        this.tranche = tranche;
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
        if (!(object instanceof YvsBorneTranches)) {
            return false;
        }
        YvsBorneTranches other = (YvsBorneTranches) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.produits.group.YvsBorneTranches[ id=" + id + " ]";
    }

    @Override
    public int compare(YvsBorneTranches o1, YvsBorneTranches o2) {
        return (int) (o1.getBorne().compareTo(o2.getBorne()));
    }
}

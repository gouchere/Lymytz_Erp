/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.produits.group;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_tranches")
@NamedQueries({
    @NamedQuery(name = "YvsTranches.findAll", query = "SELECT y FROM YvsTranches y"),
    @NamedQuery(name = "YvsTranches.findAllActif", query = "SELECT y FROM YvsTranches y WHERE y.actif=true AND y.author.agence.societe=:societe"),
    @NamedQuery(name = "YvsTranches.findById", query = "SELECT y FROM YvsTranches y WHERE y.id = :id"),
    @NamedQuery(name = "YvsTranches.findByModelTranche", query = "SELECT y FROM YvsTranches y WHERE y.modelTranche = :modelTranche"),
    @NamedQuery(name = "YvsTranches.findByAuthor", query = "SELECT y FROM YvsTranches y WHERE y.author = :author"),
    @NamedQuery(name = "YvsTranches.findByDateSave", query = "SELECT y FROM YvsTranches y WHERE y.dateSave = :dateSave")})
public class YvsTranches implements Serializable {

    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_tranches_id_seq")
    @SequenceGenerator(sequenceName = "yvs_tranches_id_seq", allocationSize = 1, name = "yvs_tranches_id_seq")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "model_tranche")
    private String modelTranche;
    @Column(name = "reference_tranche")
    private String referenceTranche;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "tranche")
    private List<YvsBorneTranches> bornes;

    public YvsTranches() {
    }

    public String getReferenceTranche() {
        return referenceTranche;
    }

    public void setReferenceTranche(String referenceTranche) {
        this.referenceTranche = referenceTranche;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsTranches(Integer id) {
        this.id = id;
    }

    public YvsTranches(YvsTranches tr) {
        this.id = tr.id;
        this.modelTranche = tr.modelTranche;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModelTranche() {
        return modelTranche;
    }

    public void setModelTranche(String modelTranche) {
        this.modelTranche = modelTranche;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public List<YvsBorneTranches> getBornes() {
        return bornes;
    }

    public void setBornes(List<YvsBorneTranches> bornes) {
        this.bornes = bornes;
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
        if (!(object instanceof YvsTranches)) {
            return false;
        }
        YvsTranches other = (YvsTranches) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.produits.group.YvsTranches[ id=" + id + " ]";
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }
}

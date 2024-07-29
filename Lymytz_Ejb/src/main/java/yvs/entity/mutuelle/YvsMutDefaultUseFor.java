/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle;

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
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_mut_default_use_for")
@NamedQueries({
    @NamedQuery(name = "YvsMutDefaultUseFor.findAll", query = "SELECT y FROM YvsMutDefaultUseFor y"),
    @NamedQuery(name = "YvsMutDefaultUseFor.findById", query = "SELECT y FROM YvsMutDefaultUseFor y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutDefaultUseFor.findByActivite", query = "SELECT y FROM YvsMutDefaultUseFor y WHERE y.activite = :activite AND y.caisse=:caisse AND y.caisse.actif=true"),
    @NamedQuery(name = "YvsMutDefaultUseFor.findByActiviteM", query = "SELECT y FROM YvsMutDefaultUseFor y WHERE y.activite = :activite AND y.caisse.mutuelle=:mutuelle"),

    @NamedQuery(name = "YvsMutDefaultUseFor.findCaisseByType", query = "SELECT DISTINCT(y.caisse) FROM YvsMutDefaultUseFor y WHERE y.activite = :type AND y.caisse.mutuelle=:mutuelle")})
public class YvsMutDefaultUseFor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_default_use_for_id_seq", name = "yvs_mut_default_use_for_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_default_use_for_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "activite")
    private String activite;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCaisse caisse;

    public YvsMutDefaultUseFor() {
    }

    public YvsMutDefaultUseFor(Integer id) {
        this.id = id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsMutCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsMutCaisse caisse) {
        this.caisse = caisse;
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
        if (!(object instanceof YvsMutDefaultUseFor)) {
            return false;
        }
        YvsMutDefaultUseFor other = (YvsMutDefaultUseFor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.operation.YvsMutDefaultUseFor[ id=" + id + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.salaire;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_centre_depense")
@NamedQueries({
    @NamedQuery(name = "YvsGrhCentreDepense.findAll", query = "SELECT y FROM YvsGrhCentreDepense y"),
    @NamedQuery(name = "YvsGrhCentreDepense.findById", query = "SELECT y FROM YvsGrhCentreDepense y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhCentreDepense.findByIdSource", query = "SELECT y FROM YvsGrhCentreDepense y WHERE y.idSource = :idSource"),
    @NamedQuery(name = "YvsGrhCentreDepense.findBySource", query = "SELECT y FROM YvsGrhCentreDepense y WHERE y.source = :source")})
public class YvsGrhCentreDepense implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_centre_depense_id_seq", name = "yvs_grh_centre_depense_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_centre_depense_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "id_source")
    private long idSource;
    @Size(max = 2147483647)
    @Column(name = "source")
    private String source;

    public YvsGrhCentreDepense() {
    }

    public YvsGrhCentreDepense(Long id) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getIdSource() {
        return idSource;
    }

    public void setIdSource(long idSource) {
        this.idSource = idSource;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
        if (!(object instanceof YvsGrhCentreDepense)) {
            return false;
        }
        YvsGrhCentreDepense other = (YvsGrhCentreDepense) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhCentreDepense[ id=" + id + " ]";
    }

}

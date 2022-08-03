/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.contrat;

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
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LENOVO
 */
@Entity
@Table(name = "yvs_grh_contrat_restreint")
@NamedQueries({
    @NamedQuery(name = "YvsGrhContratRestreint.findAll", query = "SELECT y FROM YvsGrhContratRestreint y"),
    @NamedQuery(name = "YvsGrhContratRestreint.findOne", query = "SELECT y FROM YvsGrhContratRestreint y WHERE y.contrat=:contrat AND y.niveaux=:niveau"),
    @NamedQuery(name = "YvsGrhContratRestreint.findByNiveau", query = "SELECT y.contrat FROM YvsGrhContratRestreint y WHERE y.niveaux=:niveau"),
    @NamedQuery(name = "YvsGrhContratRestreint.findById", query = "SELECT y FROM YvsGrhContratRestreint y WHERE y.id = :id")})
public class YvsGrhContratRestreint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_contrat_restreint_id_seq", name = "yvs_grh_contrat_restreint_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_contrat_restreint_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "niveaux", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsNiveauAcces niveaux;
    @JoinColumn(name = "contrat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhContratEmps contrat;

    public YvsGrhContratRestreint() {
    }

    public YvsGrhContratRestreint(Long id) {
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsNiveauAcces getNiveaux() {
        return niveaux;
    }

    public void setNiveaux(YvsNiveauAcces niveaux) {
        this.niveaux = niveaux;
    }

    public YvsGrhContratEmps getContrat() {
        return contrat;
    }

    public void setContrat(YvsGrhContratEmps contrat) {
        this.contrat = contrat;
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
        if (!(object instanceof YvsGrhContratRestreint)) {
            return false;
        }
        YvsGrhContratRestreint other = (YvsGrhContratRestreint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.contrat.YvsGrhContratRestreint[ id=" + id + " ]";
    }

}

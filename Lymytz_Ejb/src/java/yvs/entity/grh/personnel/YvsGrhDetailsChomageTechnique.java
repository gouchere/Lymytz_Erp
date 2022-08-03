/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

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
import yvs.entity.grh.activite.YvsGrhCongeEmps;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_grh_details_chomage_technique")
@NamedQueries({
    @NamedQuery(name = "YvsGrhDetailsChomageTechnique.findById", query = "SELECT y FROM YvsGrhDetailsChomageTechnique y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhDetailsChomageTechnique.findByActif", query = "SELECT y FROM YvsGrhDetailsChomageTechnique y WHERE y.actif = :actif")})
public class YvsGrhDetailsChomageTechnique implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Column(name = "debut_periode")
    @Temporal(TemporalType.DATE)
    private Date debutPeriode;
    @Column(name = "fin_periode")
    @Temporal(TemporalType.DATE)
    private Date finPeriode;
    @JoinColumn(name = "conge", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhCongeEmps conge;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_details_chomage_technique_id_seq", name = "yvs_grh_details_chomage_technique_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_details_chomage_technique_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "duree")
    private double duree;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhDetailsChomageTechnique() {
    }

    public YvsGrhDetailsChomageTechnique(Long id) {
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

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public double getDuree() {
        return duree;
    }

    public void setDuree(double duree) {
        this.duree = duree;
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
        if (!(object instanceof YvsGrhDetailsChomageTechnique)) {
            return false;
        }
        YvsGrhDetailsChomageTechnique other = (YvsGrhDetailsChomageTechnique) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsGrhChomageEmps[ id=" + id + " ]";
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public Date getDebutPeriode() {
        return debutPeriode;
    }

    public void setDebutPeriode(Date debutPeriode) {
        this.debutPeriode = debutPeriode;
    }

    public Date getFinPeriode() {
        return finPeriode;
    }

    public void setFinPeriode(Date finPeriode) {
        this.finPeriode = finPeriode;
    }

    public YvsGrhCongeEmps getConge() {
        return conge;
    }

    public void setConge(YvsGrhCongeEmps conge) {
        this.conge = conge;
    }

}

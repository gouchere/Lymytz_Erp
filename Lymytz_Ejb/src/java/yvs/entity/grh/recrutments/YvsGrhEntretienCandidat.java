/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.recrutments;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_entretien_candidat")
@NamedQueries({
    @NamedQuery(name = "YvsGrhEntretienCandidat.findAll", query = "SELECT y FROM YvsGrhEntretienCandidat y WHERE y.candidat.posteTravail.departement.societe=:societe"),
    @NamedQuery(name = "YvsGrhEntretienCandidat.findById", query = "SELECT y FROM YvsGrhEntretienCandidat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhEntretienCandidat.findByDateEntretien", query = "SELECT y FROM YvsGrhEntretienCandidat y WHERE y.dateEntretien = :dateEntretien"),
    @NamedQuery(name = "YvsGrhEntretienCandidat.findByHeure", query = "SELECT y FROM YvsGrhEntretienCandidat y WHERE y.heure = :heure"),
    @NamedQuery(name = "YvsGrhEntretienCandidat.findByLieu", query = "SELECT y FROM YvsGrhEntretienCandidat y WHERE y.lieu = :lieu")})
public class YvsGrhEntretienCandidat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_etretien_candidat_id_seq", name = "yvs_grh_etretien_candidat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_etretien_candidat_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_entretien")
    @Temporal(TemporalType.DATE)
    private Date dateEntretien;
    @Column(name = "heure")
    @Temporal(TemporalType.TIME)
    private Date heure;
    @Size(max = 2147483647)
    @Column(name = "lieu")
    private String lieu;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "candidat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhCandidats candidat;
    @JoinColumn(name = "examinateur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes examinateur;

    public YvsGrhEntretienCandidat() {
    }

    public YvsGrhEntretienCandidat(Long id) {
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

    public Date getDateEntretien() {
        return dateEntretien;
    }

    public void setDateEntretien(Date dateEntretien) {
        this.dateEntretien = dateEntretien;
    }

    public Date getHeure() {
        return heure;
    }

    public void setHeure(Date heure) {
        this.heure = heure;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhCandidats getCandidat() {
        return candidat;
    }

    public void setCandidat(YvsGrhCandidats candidat) {
        this.candidat = candidat;
    }

    public YvsGrhEmployes getExaminateur() {
        return examinateur;
    }

    public void setExaminateur(YvsGrhEmployes examinateur) {
        this.examinateur = examinateur;
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
        if (!(object instanceof YvsGrhEntretienCandidat)) {
            return false;
        }
        YvsGrhEntretienCandidat other = (YvsGrhEntretienCandidat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.recrutments.YvsGrhEtretienCandidat[ id=" + id + " ]";
    }

}

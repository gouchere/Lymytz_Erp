/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.recrutments;

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
import yvs.entity.grh.param.YvsGrhQualifications;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_qualification_candidat")
@NamedQueries({
    @NamedQuery(name = "YvsGrhQualificationCandidat.findAll", query = "SELECT y FROM YvsGrhQualificationCandidat y"),
    @NamedQuery(name = "YvsGrhQualificationCandidat.findById", query = "SELECT y FROM YvsGrhQualificationCandidat y WHERE y.id = :id")})
public class YvsGrhQualificationCandidat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_qualification_candidat_id_seq", name = "yvs_grh_qualification_candidat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_qualification_candidat_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "niveau")
    private Integer niveau;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "qualification", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhQualifications qualification;
    @JoinColumn(name = "candidat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhCandidats candidat;

    public YvsGrhQualificationCandidat() {
    }

    public YvsGrhQualificationCandidat(Long id) {
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

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }

    public Integer getNiveau() {
        return niveau;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhQualifications getQualification() {
        return qualification;
    }

    public void setQualification(YvsGrhQualifications qualification) {
        this.qualification = qualification;
    }

    public YvsGrhCandidats getCandidat() {
        return candidat;
    }

    public void setCandidat(YvsGrhCandidats candidat) {
        this.candidat = candidat;
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
        if (!(object instanceof YvsGrhQualificationCandidat)) {
            return false;
        }
        YvsGrhQualificationCandidat other = (YvsGrhQualificationCandidat) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.recrutments.YvsGrhQualificationCandidat[ id=" + id + " ]";
    }

}

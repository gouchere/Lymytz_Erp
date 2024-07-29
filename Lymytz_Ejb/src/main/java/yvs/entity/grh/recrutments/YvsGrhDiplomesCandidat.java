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
import javax.validation.constraints.Size;
import yvs.entity.grh.personnel.YvsDiplomes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_diplomes_candidat")
@NamedQueries({
    @NamedQuery(name = "YvsGrhDiplomesCandidat.findAll", query = "SELECT y FROM YvsGrhDiplomesCandidat y"),
    @NamedQuery(name = "YvsGrhDiplomesCandidat.findById", query = "SELECT y FROM YvsGrhDiplomesCandidat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhDiplomesCandidat.findByMention", query = "SELECT y FROM YvsGrhDiplomesCandidat y WHERE y.mention = :mention"),
    @NamedQuery(name = "YvsGrhDiplomesCandidat.findByEtablissement", query = "SELECT y FROM YvsGrhDiplomesCandidat y WHERE y.etablissement = :etablissement")})
public class YvsGrhDiplomesCandidat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_diplomes_candidat_id_seq", name = "yvs_grh_diplomes_candidat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_diplomes_candidat_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "mention")
    private String mention;
    @Size(max = 2147483647)
    @Column(name = "etablissement")
    private String etablissement;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "candidat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhCandidats candidat;
    @JoinColumn(name = "diplome", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDiplomes diplome;
    @Column(name = "date_obtention")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateObtention;

    public YvsGrhDiplomesCandidat() {
    }

    public YvsGrhDiplomesCandidat(Long id) {
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

    public String getMention() {
        return mention;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
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

    public YvsDiplomes getDiplome() {
        return diplome;
    }

    public void setDiplome(YvsDiplomes diplome) {
        this.diplome = diplome;
    }

    public Date getDateObtention() {
        return dateObtention;
    }

    public void setDateObtention(Date dateObtention) {
        this.dateObtention = dateObtention;
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
        if (!(object instanceof YvsGrhDiplomesCandidat)) {
            return false;
        }
        YvsGrhDiplomesCandidat other = (YvsGrhDiplomesCandidat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.recrutments.YvsGrhDiplomesCandidat[ id=" + id + " ]";
    }

}

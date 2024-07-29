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
@Table(name = "yvs_grh_formation_candidats")
@NamedQueries({
    @NamedQuery(name = "YvsGrhFormationCandidats.findAll", query = "SELECT y FROM YvsGrhFormationCandidats y"),
    @NamedQuery(name = "YvsGrhFormationCandidats.findById", query = "SELECT y FROM YvsGrhFormationCandidats y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhFormationCandidats.findByMention", query = "SELECT y FROM YvsGrhFormationCandidats y WHERE y.mention = :mention"),
    @NamedQuery(name = "YvsGrhFormationCandidats.findByEtablissement", query = "SELECT y FROM YvsGrhFormationCandidats y WHERE y.etablissement = :etablissement"),
    @NamedQuery(name = "YvsGrhFormationCandidats.findByAnneeObtention", query = "SELECT y FROM YvsGrhFormationCandidats y WHERE y.anneeObtention = :anneeObtention")})
public class YvsGrhFormationCandidats implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_formation_candidats_id_seq", name = "yvs_grh_formation_candidats_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_formation_candidats_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "annee_obtention")
    @Temporal(TemporalType.DATE)
    private Date anneeObtention;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "diplome", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDiplomes diplome;

    public YvsGrhFormationCandidats() {
    }

    public YvsGrhFormationCandidats(Long id) {
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

    public Date getAnneeObtention() {
        return anneeObtention;
    }

    public void setAnneeObtention(Date anneeObtention) {
        this.anneeObtention = anneeObtention;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsDiplomes getDiplome() {
        return diplome;
    }

    public void setDiplome(YvsDiplomes diplome) {
        this.diplome = diplome;
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
        if (!(object instanceof YvsGrhFormationCandidats)) {
            return false;
        }
        YvsGrhFormationCandidats other = (YvsGrhFormationCandidats) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.recrutments.YvsGrhFormationCandidats[ id=" + id + " ]";
    }

}

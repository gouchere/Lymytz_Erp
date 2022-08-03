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
import yvs.entity.grh.personnel.YvsLangues;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_langues_candidats")
@NamedQueries({
    @NamedQuery(name = "YvsGrhLanguesCandidats.findAll", query = "SELECT y FROM YvsGrhLanguesCandidats y"),
    @NamedQuery(name = "YvsGrhLanguesCandidats.findById", query = "SELECT y FROM YvsGrhLanguesCandidats y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhLanguesCandidats.findByLu", query = "SELECT y FROM YvsGrhLanguesCandidats y WHERE y.lu = :lu"),
    @NamedQuery(name = "YvsGrhLanguesCandidats.findByEcrit", query = "SELECT y FROM YvsGrhLanguesCandidats y WHERE y.ecrit = :ecrit"),
    @NamedQuery(name = "YvsGrhLanguesCandidats.findByParler", query = "SELECT y FROM YvsGrhLanguesCandidats y WHERE y.parler = :parler")})
public class YvsGrhLanguesCandidats implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_langues_candidats_id_seq", name = "yvs_grh_langues_candidats_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_langues_candidats_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "lu")
    private Boolean lu;
    @Column(name = "ecrit")
    private Boolean ecrit;
    @Column(name = "parler")
    private Boolean parler;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "candidat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhCandidats candidat;
    @JoinColumn(name = "langue", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsLangues langue;

    public YvsGrhLanguesCandidats() {
    }

    public YvsGrhLanguesCandidats(Long id) {
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

    public Boolean getLu() {
        return (lu != null) ? lu : false;
    }

    public void setLu(Boolean lu) {
        this.lu = lu;
    }

    public Boolean getEcrit() {
        return (ecrit != null) ? ecrit : false;
    }

    public void setEcrit(Boolean ecrit) {
        this.ecrit = ecrit;
    }

    public Boolean getParler() {
        return (parler != null) ? parler : false;
    }

    public void setParler(Boolean parler) {
        this.parler = parler;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsLangues getLangue() {
        return langue;
    }

    public void setLangue(YvsLangues langue) {
        this.langue = langue;
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
        if (!(object instanceof YvsGrhLanguesCandidats)) {
            return false;
        }
        YvsGrhLanguesCandidats other = (YvsGrhLanguesCandidats) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.recrutments.YvsGrhLanguesCandidats[ id=" + id + " ]";
    }

    public YvsGrhCandidats getCandidat() {
        return candidat;
    }

    public void setCandidat(YvsGrhCandidats candidat) {
        this.candidat = candidat;
    }

}

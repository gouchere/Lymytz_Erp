/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

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
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_jours_feries")
@NamedQueries({
    @NamedQuery(name = "YvsJoursFeries.findAll", query = "SELECT y FROM YvsJoursFeries y WHERE y.societe=:societe ORDER BY y.jour"),
    @NamedQuery(name = "YvsJoursFeries.findById", query = "SELECT y FROM YvsJoursFeries y WHERE y.id = :id"),
    @NamedQuery(name = "YvsJoursFeries.findByTitre", query = "SELECT y FROM YvsJoursFeries y WHERE y.titre = :titre"),
    @NamedQuery(name = "YvsJoursFeries.findByJour", query = "SELECT y FROM YvsJoursFeries y WHERE y.jour = :jour AND y.societe=:societe"),
    @NamedQuery(name = "YvsJoursFeries.findByJourFBetweenDates", query = "SELECT y FROM YvsJoursFeries y WHERE ((y.jour BETWEEN :debut AND :fin) OR y.allYear=true) AND y.societe=:societe"),
    @NamedQuery(name = "YvsJoursFeries.countByTitre", query = "SELECT count(y) FROM YvsJoursFeries y WHERE y.titre=:titre AND y.jour!=:jour AND y.societe=:societe"),
    @NamedQuery(name = "YvsJoursFeries.countByDate", query = "SELECT count(y) FROM YvsJoursFeries y WHERE y.jour=:jour AND y.titre!=:titre AND y.societe=:societe"),
    @NamedQuery(name = "YvsJoursFeries.countByDateAndTitre", query = "SELECT count(y) FROM YvsJoursFeries y WHERE y.jour=:jour AND y.titre=:titre AND y.societe=:societe"),
    @NamedQuery(name = "YvsJoursFeries.findByCommentaire", query = "SELECT y FROM YvsJoursFeries y WHERE y.commentaire = :commentaire")})
public class YvsJoursFeries implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_jours_feries_id_seq")
    @SequenceGenerator(sequenceName = "yvs_jours_feries_id_seq", allocationSize = 1, name = "yvs_jours_feries_id_seq")
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
    @Column(name = "titre")
    private String titre;
    @Column(name = "jour")
    @Temporal(TemporalType.DATE)
    private Date jour;
    @Size(max = 2147483647)
    @Column(name = "commentaire")
    private String commentaire;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @Column(name = "all_year")
    private Boolean allYear;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsJoursFeries() {
    }

    public YvsJoursFeries(Long id) {
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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Date getJour() {
        return jour;
    }

    public void setJour(Date jour) {
        this.jour = jour;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public void setAllYear(Boolean allYear) {
        this.allYear = allYear;
    }

    public Boolean isAllYear() {
        return allYear;
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
        if (!(object instanceof YvsJoursFeries)) {
            return false;
        }
        YvsJoursFeries other = (YvsJoursFeries) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsJoursFeries[ id=" + id + " ]";
    }

}

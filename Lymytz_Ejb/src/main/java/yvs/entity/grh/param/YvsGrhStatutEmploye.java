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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_statut_employe")
@NamedQueries({
    @NamedQuery(name = "YvsGrhStatutEmploye.findAll", query = "SELECT y FROM YvsGrhStatutEmploye y"),
    @NamedQuery(name = "YvsGrhStatutEmploye.findById", query = "SELECT y FROM YvsGrhStatutEmploye y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhStatutEmploye.findByStatut", query = "SELECT y FROM YvsGrhStatutEmploye y WHERE y.statut = :statut"),
    @NamedQuery(name = "YvsGrhStatutEmploye.findByLibelleStatut", query = "SELECT y FROM YvsGrhStatutEmploye y WHERE y.libelleStatut = :libelleStatut")})
public class YvsGrhStatutEmploye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_statut_employe_id_seq", name = "yvs_grh_statut_employe_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_statut_employe_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "statut")
    private Character statut;
    @Size(max = 2147483647)
    @Column(name = "libelle_statut")
    private String libelleStatut;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhStatutEmploye() {
    }

    public YvsGrhStatutEmploye(Long id, Character statut, String libelleStatut) {
        this.id = id;
        this.statut = statut;
        this.libelleStatut = libelleStatut;
    }

    public YvsGrhStatutEmploye(Long id) {
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

    public Character getStatut() {
        return statut;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public String getLibelleStatut() {
        return libelleStatut;
    }

    public void setLibelleStatut(String libelleStatut) {
        this.libelleStatut = libelleStatut;
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
        if (!(object instanceof YvsGrhStatutEmploye)) {
            return false;
        }
        YvsGrhStatutEmploye other = (YvsGrhStatutEmploye) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsGrhStatutEmploye[ id=" + id + " ]";
    }

    }

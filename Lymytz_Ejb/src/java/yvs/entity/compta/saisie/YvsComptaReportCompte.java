/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.saisie;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_report_compte")
@NamedQueries({
    @NamedQuery(name = "YvsComptaReportCompte.findAll", query = "SELECT y FROM YvsComptaReportCompte y"),
    @NamedQuery(name = "YvsComptaReportCompte.findById", query = "SELECT y FROM YvsComptaReportCompte y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaReportCompte.findOne", query = "SELECT y FROM YvsComptaReportCompte y WHERE y.agence = :agence AND y.compte = :compte AND y.exercice = :exercice"),
    @NamedQuery(name = "YvsComptaReportCompte.findOneBySociete", query = "SELECT y FROM YvsComptaReportCompte y WHERE y.agence.societe = :societe AND y.compte = :compte AND y.exercice = :exercice"),
    @NamedQuery(name = "YvsComptaReportCompte.findByDateUpdate", query = "SELECT y FROM YvsComptaReportCompte y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaReportCompte.findByDateSave", query = "SELECT y FROM YvsComptaReportCompte y WHERE y.dateSave = :dateSave")})
public class YvsComptaReportCompte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_report_compte_id_seq", name = "yvs_compta_report_compte_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_report_compte_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compte;
    @JoinColumn(name = "exercice", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseExercice exercice;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;

    public YvsComptaReportCompte() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaReportCompte(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaReportCompte(YvsAgences agence, YvsBaseExercice exercice, YvsBasePlanComptable compte) {
        this();
        this.agence = agence;
        this.exercice = exercice;
        this.compte = compte;
    }

    public YvsComptaReportCompte(YvsAgences agence, YvsBaseExercice exercice, YvsBasePlanComptable compte, YvsUsersAgence author) {
        this(agence, exercice, compte);
        this.author = author;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBasePlanComptable getCompte() {
        return compte;
    }

    public void setCompte(YvsBasePlanComptable compte) {
        this.compte = compte;
    }

    public YvsBaseExercice getExercice() {
        return exercice;
    }

    public void setExercice(YvsBaseExercice exercice) {
        this.exercice = exercice;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
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
        if (!(object instanceof YvsComptaReportCompte)) {
            return false;
        }
        YvsComptaReportCompte other = (YvsComptaReportCompte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.saisie.YvsComptaReportCompte[ id=" + id + " ]";
    }

}

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
import yvs.dao.salaire.service.Constantes;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_content_analytique")
@NamedQueries({
    @NamedQuery(name = "YvsComptaContentAnalytique.findAll", query = "SELECT y FROM YvsComptaContentAnalytique y"),
    @NamedQuery(name = "YvsComptaContentAnalytique.findById", query = "SELECT y FROM YvsComptaContentAnalytique y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaContentAnalytique.findByDebit", query = "SELECT y FROM YvsComptaContentAnalytique y WHERE y.debit = :debit"),
    @NamedQuery(name = "YvsComptaContentAnalytique.findByCredit", query = "SELECT y FROM YvsComptaContentAnalytique y WHERE y.credit = :credit"),
    @NamedQuery(name = "YvsComptaContentAnalytique.findByDateSasie", query = "SELECT y FROM YvsComptaContentAnalytique y WHERE y.dateSaisie = :dateSasie"),
    @NamedQuery(name = "YvsComptaContentAnalytique.findByDateUpdate", query = "SELECT y FROM YvsComptaContentAnalytique y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaContentAnalytique.findByDateSave", query = "SELECT y FROM YvsComptaContentAnalytique y WHERE y.dateSave = :dateSave"),
    
    @NamedQuery(name = "YvsComptaContentAnalytique.findSumByContentNotId", query = "SELECT SUM(y.coefficient) FROM YvsComptaContentAnalytique y WHERE y.contenu = :contenu AND y.id != :id")})
public class YvsComptaContentAnalytique implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_content_analytique_id_seq", name = "yvs_compta_content_analytique_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_content_analytique_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debit")
    private Double debit;
    @Column(name = "credit")
    private Double credit;
    @Column(name = "coefficient")
    private Double coefficient;
    @Column(name = "date_sasie")
    @Temporal(TemporalType.DATE)
    private Date dateSaisie;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "statut")
    private Character statut;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "contenu", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaContentJournal contenu;
    @JoinColumn(name = "centre", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCentreAnalytique centre;

    public YvsComptaContentAnalytique() {
    }

    public YvsComptaContentAnalytique(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaContentAnalytique(Long id, Double debit, Double credit, Double coefficient, YvsComptaContentJournal contenu, YvsComptaCentreAnalytique centre) {
        this(id);
        this.debit = debit;
        this.credit = credit;
        this.coefficient = coefficient;
        this.contenu = contenu;
        this.centre = centre;
    }

    public YvsComptaContentAnalytique(Long id, Double debit, Double credit, Double coefficient, YvsComptaContentJournal contenu, YvsComptaCentreAnalytique centre, Date dateSaisie) {
        this(id, debit, credit, coefficient, contenu, centre);
        this.dateSaisie = dateSaisie;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDebit() {
        return debit != null ? debit : 0;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit != null ? credit : 0;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getCoefficient() {
        return coefficient != null ? coefficient : 0;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Date getDateSaisie() {
        return dateSaisie != null ? dateSaisie : new Date();
    }

    public void setDateSaisie(Date dateSaisie) {
        this.dateSaisie = dateSaisie;
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

    public YvsComptaContentJournal getContenu() {
        return contenu;
    }

    public void setContenu(YvsComptaContentJournal contenu) {
        this.contenu = contenu;
    }

    public YvsComptaCentreAnalytique getCentre() {
        return centre;
    }

    public void setCentre(YvsComptaCentreAnalytique centre) {
        this.centre = centre;
    }

    public Character getStatut() {
        return statut != null ? String.valueOf(statut).trim().length() > 0 ? statut : Constantes.STATUT_DOC_VALIDE : Constantes.STATUT_DOC_VALIDE;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
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
        if (!(object instanceof YvsComptaContentAnalytique)) {
            return false;
        }
        YvsComptaContentAnalytique other = (YvsComptaContentAnalytique) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.saisie.YvsComptaContentAnalytique[ id=" + id + " ]";
    }

}

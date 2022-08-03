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
import javax.validation.constraints.Size;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_diplomes")
@NamedQueries({
    @NamedQuery(name = "YvsDiplomes.findAll", query = "SELECT y FROM YvsDiplomes y LEFT JOIN FETCH y.specialite WHERE y.societe=:societe ORDER BY y.specialite.titreSpecialite, y.nom "),
    @NamedQuery(name = "YvsDiplomes.findById", query = "SELECT y FROM YvsDiplomes y WHERE y.id = :id"),
    @NamedQuery(name = "YvsDiplomes.count", query = "SELECT COUNT(y) FROM YvsDiplomes y WHERE y.nom = :nom AND y.societe=:societe"),
    @NamedQuery(name = "YvsDiplomes.findByNom", query = "SELECT y FROM YvsDiplomes y WHERE y.nom = :nom AND y.societe=:societe")})
public class YvsDiplomes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_diplomes_id_seq")
    @SequenceGenerator(sequenceName = "yvs_diplomes_id_seq", allocationSize = 1, name = "yvs_diplomes_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "nom")
    private String nom;
    @Column(name = "niveau")
    private Integer niveau;
    @Column(name = "code_interne")
    private String codeInterne;
    @Column(name = "serie")
    private String serie;
    @Column(name = "cycle_diplome")
    private String cycleDipome;

    @JoinColumn(name = "specialite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSpecialiteDiplomes specialite;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    public YvsDiplomes() {
    }

    public YvsDiplomes(Long id) {
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getNiveau() {
        return (niveau != null) ? niveau : 0;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public String getCodeInterne() {
        return codeInterne;
    }

    public void setCodeInterne(String codeInterne) {
        this.codeInterne = codeInterne;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getCycleDipome() {
        return cycleDipome;
    }

    public void setCycleDipome(String cycleDipome) {
        this.cycleDipome = cycleDipome;
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
        if (!(object instanceof YvsDiplomes)) {
            return false;
        }
        YvsDiplomes other = (YvsDiplomes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsDiplomes[ id=" + id + " ]";
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return (actif != null) ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsSpecialiteDiplomes getSpecialite() {
        return specialite;
    }

    public void setSpecialite(YvsSpecialiteDiplomes specialite) {
        this.specialite = specialite;
    }

    }

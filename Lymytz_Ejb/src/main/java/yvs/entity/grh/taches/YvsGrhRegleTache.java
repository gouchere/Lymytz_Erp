/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.taches;

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
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_grh_regle_tache")
@NamedQueries({
    @NamedQuery(name = "YvsRegleTache.findAll", query = "SELECT y FROM YvsGrhRegleTache y WHERE y.agence.societe = :societe AND y.actif=true"),
    @NamedQuery(name = "YvsRegleTache.findById", query = "SELECT y FROM YvsGrhRegleTache y WHERE y.id = :id"),
    @NamedQuery(name = "YvsRegleTache.findByCode", query = "SELECT y FROM YvsGrhRegleTache y WHERE y.code = :code"),
    @NamedQuery(name = "YvsRegleTache.findByNom", query = "SELECT y FROM YvsGrhRegleTache y WHERE y.nom = :nom"),
    @NamedQuery(name = "YvsRegleTache.findByCodeNom", query = "SELECT y FROM YvsGrhRegleTache y WHERE y.code like :code OR y.nom like :code AND y.agence = :agence"),
    @NamedQuery(name = "YvsRegleTache.findBySupp", query = "SELECT y FROM YvsGrhRegleTache y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsRegleTache.findByActif", query = "SELECT y FROM YvsGrhRegleTache y WHERE y.actif = :actif")})
public class YvsGrhRegleTache implements Serializable {

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "regleTache")
    private List<YvsGrhMontantTache> listeTaches;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_regle_tache_id_seq", name = "yvs_regle_tache_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_regle_tache_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 255)
    @Column(name = "code")
    private String code;
    @Size(max = 255)
    @Column(name = "nom")
    private String nom;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;

    public YvsGrhRegleTache() {
    }

    public YvsGrhRegleTache(Long id) {
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

    public Boolean isSupp() {
        return supp;
    }

    public Boolean isActif() {
        return actif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Boolean getSupp() {
        return (supp == null) ? false : supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return (actif == null) ? false : actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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
        if (!(object instanceof YvsGrhRegleTache)) {
            return false;
        }
        YvsGrhRegleTache other = (YvsGrhRegleTache) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsRegleTache[ id=" + id + " ]";
    }

    public List<YvsGrhMontantTache> getListeTaches() {
        return listeTaches;
    }

    public void setListeTaches(List<YvsGrhMontantTache> listeTaches) {
        this.listeTaches = listeTaches;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    }

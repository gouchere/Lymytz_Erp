/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

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
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_grh_type_chomage")
@NamedQueries({
    @NamedQuery(name = "YvsGrhTypeChomage.findAll", query = "SELECT y FROM YvsGrhTypeChomage y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsGrhTypeChomage.findById", query = "SELECT y FROM YvsGrhTypeChomage y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhTypeChomage.findByLibelle", query = "SELECT y FROM YvsGrhTypeChomage y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsGrhTypeChomage.findByCode", query = "SELECT y FROM YvsGrhTypeChomage y WHERE y.code = :code"),
    @NamedQuery(name = "YvsGrhTypeChomage.findByActif", query = "SELECT y FROM YvsGrhTypeChomage y WHERE y.actif = :actif")})
public class YvsGrhTypeChomage implements Serializable {

    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "etat")
    private Character etat; //on considère deux etats: En cours (R) et Clôturé (C)
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_type_chomage_id_seq", name = "yvs_grh_type_chomage_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_type_chomage_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhTypeChomage() {
    }

    public YvsGrhTypeChomage(Long id) {
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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
        if (!(object instanceof YvsGrhTypeChomage)) {
            return false;
        }
        YvsGrhTypeChomage other = (YvsGrhTypeChomage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsGrhTypeChomage[ id=" + id + " ]";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Character getEtat() {
        return etat;
    }

    public void setEtat(Character etat) {
        this.etat = etat;
    }
}

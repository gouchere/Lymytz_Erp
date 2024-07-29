/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import yvs.entity.grh.param.YvsGrhQualifications;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_qualification_formation")
@NamedQueries({
    @NamedQuery(name = "YvsQualificationFormation.findAll", query = "SELECT y FROM YvsGrhQualificationFormation y"),
    @NamedQuery(name = "YvsQualificationFormation.findByNiveau", query = "SELECT y FROM YvsGrhQualificationFormation y WHERE y.niveau = :niveau"),
    @NamedQuery(name = "YvsQualificationFormation.findBySupp", query = "SELECT y FROM YvsGrhQualificationFormation y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsQualificationFormation.findByActif", query = "SELECT y FROM YvsGrhQualificationFormation y WHERE y.actif = :actif")})
public class YvsGrhQualificationFormation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @SequenceGenerator(sequenceName = "yvs_qualification_formation_id_seq", name = "yvs_qualification_formation_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_qualification_formation_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "niveau")
    private String niveau;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "qualification")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhQualifications qualification;
    @JoinColumn(name = "formation", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhFormation formation;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhQualificationFormation() {
    }

    public void setId(Long id) {
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

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsGrhQualifications getQualification() {
        return qualification;
    }

    public void setQualification(YvsGrhQualifications qualification) {
        this.qualification = qualification;
    }

    public YvsGrhFormation getFormation() {
        return formation;
    }

    public void setFormation(YvsGrhFormation formation) {
        this.formation = formation;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsGrhQualificationFormation other = (YvsGrhQualificationFormation) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}

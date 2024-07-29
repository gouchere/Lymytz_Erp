/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.presence;

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
 * @author Lymytz
 */
@Entity
@Table(name = "yvs_grh_type_validation")
@NamedQueries({
    @NamedQuery(name = "YvsGrhTypeValidation.findAll", query = "SELECT y FROM YvsGrhTypeValidation y WHERE y.societe=:societe AND y.actif=true"),
    @NamedQuery(name = "YvsGrhTypeValidation.findById", query = "SELECT y FROM YvsGrhTypeValidation y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhTypeValidation.findByCode", query = "SELECT y FROM YvsGrhTypeValidation y WHERE y.code = :code"),
    @NamedQuery(name = "YvsGrhTypeValidation.findByLibelle", query = "SELECT y FROM YvsGrhTypeValidation y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsGrhTypeValidation.findByActif", query = "SELECT y FROM YvsGrhTypeValidation y WHERE y.actif = :actif")})
public class YvsGrhTypeValidation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_type_validation_id_seq", name = "yvs_grh_type_validation_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_type_validation_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "code")
    private String code;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "taux_journee")
    private Double tauxJournee;
    @Column(name = "temps_minimal")
    private Double tempsMinimal;
    
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhTypeValidation() {
    }

    public YvsGrhTypeValidation(Long id) {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public Double getTauxJournee() {
        return tauxJournee == null ? 0 : tauxJournee;
    }

    public void setTauxJournee(Double tauxJournee) {
        this.tauxJournee = tauxJournee;
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

    public Double getTempsMinimal() {
        return tempsMinimal != null ? tempsMinimal : 0;
    }

    public void setTempsMinimal(Double tempsMinimal) {
        this.tempsMinimal = tempsMinimal;
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
        if (!(object instanceof YvsGrhTypeValidation)) {
            return false;
        }
        YvsGrhTypeValidation other = (YvsGrhTypeValidation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.presence.YvsGrhTypeValidation[ id=" + id + " ]";
    }

}

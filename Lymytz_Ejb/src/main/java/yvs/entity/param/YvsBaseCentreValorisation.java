/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.production.base.YvsProdCentrePosteCharge;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_base_centre_valorisation")
@NamedQueries({
    @NamedQuery(name = "YvsBaseCentreValorisation.findAll", query = "SELECT y FROM YvsBaseCentreValorisation y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsBaseCentreValorisation.findById", query = "SELECT y FROM YvsBaseCentreValorisation y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseCentreValorisation.findByReference", query = "SELECT y FROM YvsBaseCentreValorisation y WHERE y.reference = :reference AND y.societe = :societe"),
    @NamedQuery(name = "YvsBaseCentreValorisation.findByDesignation", query = "SELECT y FROM YvsBaseCentreValorisation y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseCentreValorisation.findByDescription", query = "SELECT y FROM YvsBaseCentreValorisation y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseCentreValorisation.findByActif", query = "SELECT y FROM YvsBaseCentreValorisation y WHERE y.actif = :actif")})
public class YvsBaseCentreValorisation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_centre_valorisation_id_seq", name = "yvs_base_centre_valorisation_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_centre_valorisation_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "reference")
    private String reference;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "type_valeur")
    private Character typeValeur = 'D';
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "centre_analyse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCentreAnalytique centreAnalyse;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsBaseCentreValorisation() {
    }

    public YvsBaseCentreValorisation(Integer id) {
        this.id = id;
    }

    public YvsBaseCentreValorisation(Integer id, String reference, String designation) {
        this.id = id;
        this.reference = reference;
        this.designation = designation;
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

    public YvsComptaCentreAnalytique getCentreAnalyse() {
        return centreAnalyse;
    }

    public void setCentreAnalyse(YvsComptaCentreAnalytique centreAnalyse) {
        this.centreAnalyse = centreAnalyse;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Integer getId() {
        return id != null ? id : 0;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
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

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public Character getTypeValeur() {
        return typeValeur != null ? String.valueOf(typeValeur).trim().length() > 0 ? typeValeur : 'D' : 'D';
    }

    public void setTypeValeur(Character typeValeur) {
        this.typeValeur = typeValeur;
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
        if (!(object instanceof YvsBaseCentreValorisation)) {
            return false;
        }
        YvsBaseCentreValorisation other = (YvsBaseCentreValorisation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.YvsBaseCentreValorisation[ id=" + id + " ]";
    }

}

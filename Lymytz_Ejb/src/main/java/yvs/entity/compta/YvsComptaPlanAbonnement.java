/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore; 
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LENOVO
 */
@Entity
@Table(name = "yvs_compta_plan_abonnement")
@NamedQueries({
    @NamedQuery(name = "YvsComptaPlanAbonnement.findAll", query = "SELECT y FROM YvsComptaPlanAbonnement y LEFT JOIN FETCH y.compte WHERE y.societe=:societe"),
    @NamedQuery(name = "YvsComptaPlanAbonnement.findById", query = "SELECT y FROM YvsComptaPlanAbonnement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaPlanAbonnement.findByReferencePlan", query = "SELECT y FROM YvsComptaPlanAbonnement y WHERE y.referencePlan = :referencePlan"),
    @NamedQuery(name = "YvsComptaPlanAbonnement.findByTypeValeur", query = "SELECT y FROM YvsComptaPlanAbonnement y WHERE y.typeValeur = :typeValeur"),
    @NamedQuery(name = "YvsComptaPlanAbonnement.findByValeur", query = "SELECT y FROM YvsComptaPlanAbonnement y WHERE y.valeur = :valeur"),
    @NamedQuery(name = "YvsComptaPlanAbonnement.findByDureePlan", query = "SELECT y FROM YvsComptaPlanAbonnement y WHERE y.periodicite = :periodicite")})
public class YvsComptaPlanAbonnement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_plan_abonnement_id_seq", name = "yvs_compta_plan_abonnement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_plan_abonnement_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "reference_plan")
    private String referencePlan;
    @Column(name = "type_valeur")
    private Character typeValeur;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valeur")
    private Double valeur;
    @Column(name = "periodicite")
    private Character periodicite;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compte;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    
    @Transient
    private boolean new_;

    public YvsComptaPlanAbonnement() {
    }

    public YvsComptaPlanAbonnement(Long id) {
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

    public String getReferencePlan() {
        return referencePlan;
    }

    public void setReferencePlan(String referencePlan) {
        this.referencePlan = referencePlan;
    }

    public Character getTypeValeur() {
        return typeValeur;
    }

    public void setTypeValeur(Character typeValeur) {
        this.typeValeur = typeValeur;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public void setPeriodicite(Character periodicite) {
        this.periodicite = periodicite;
    }

    public Character getPeriodicite() {
        return periodicite;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    @XmlTransient  @JsonIgnore
    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    @XmlTransient  @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient  @JsonIgnore
    public YvsBasePlanComptable getCompte() {
        return compte;
    }

    public void setCompte(YvsBasePlanComptable compte) {
        this.compte = compte;
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
        if (!(object instanceof YvsComptaPlanAbonnement)) {
            return false;
        }
        YvsComptaPlanAbonnement other = (YvsComptaPlanAbonnement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaPlanAbonnement[ id=" + id + " ]";
    }

}

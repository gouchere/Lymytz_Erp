///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.entity.production.base;
//
//import java.io.Serializable;
//import java.util.Date;
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.validation.constraints.Size;
//
///**
// *
// * @author hp Elite 8300
// */
//@Entity
//@Table(name = "yvs_base_indicateur_reussite")
//@NamedQueries({
//    @NamedQuery(name = "YvsBaseIndicateurReussite.findAll", query = "SELECT y FROM YvsBaseIndicateurReussite y"),
//    @NamedQuery(name = "YvsBaseIndicateurReussite.findById", query = "SELECT y FROM YvsBaseIndicateurReussite y WHERE y.id = :id"),
//    @NamedQuery(name = "YvsBaseIndicateurReussite.findByDesignation", query = "SELECT y FROM YvsBaseIndicateurReussite y WHERE y.designation = :designation"),
//    @NamedQuery(name = "YvsBaseIndicateurReussite.findByType", query = "SELECT y FROM YvsBaseIndicateurReussite y WHERE y.type = :type"),
//    @NamedQuery(name = "YvsBaseIndicateurReussite.findByValeur", query = "SELECT y FROM YvsBaseIndicateurReussite y WHERE y.valeur = :valeur"),
//    @NamedQuery(name = "YvsBaseIndicateurReussite.findByDescription", query = "SELECT y FROM YvsBaseIndicateurReussite y WHERE y.description = :description")})
//public class YvsBaseIndicateurReussite implements Serializable {
//
//    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
//    @Column(name = "valeur")
//    private Double valeur;
//    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
//    @Column(name = "id")
//    private Integer id;
//    @Column(name = "date_update")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date dateUpdate;
//    @Column(name = "date_save")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date dateSave;
//    @Size(max = 2147483647)
//    @Column(name = "designation")
//    private String designation;
//    @Size(max = 2147483647)
//    @Column(name = "type")
//    private String type;
//    @Size(max = 2147483647)
//    @Column(name = "description")
//    private String description;
//
//    public YvsBaseIndicateurReussite() {
//    }
//
//    public YvsBaseIndicateurReussite(Integer id) {
//        this.id = id;
//    }
//
//    public Date getDateUpdate() {
//        return dateUpdate;
//    }
//
//    public void setDateUpdate(Date dateUpdate) {
//        this.dateUpdate = dateUpdate;
//    }
//
//    public Date getDateSave() {
//        return dateSave;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getDesignation() {
//        return designation;
//    }
//
//    public void setDesignation(String designation) {
//        this.designation = designation;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof YvsBaseIndicateurReussite)) {
//            return false;
//        }
//        YvsBaseIndicateurReussite other = (YvsBaseIndicateurReussite) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "yvs.entity.production.base.YvsBaseIndicateurReussite[ id=" + id + " ]";
//    }
//
//    public Double getValeur() {
//        return valeur;
//    }
//
//    public void setValeur(Double valeur) {
//        this.valeur = valeur;
//    }
//
//}

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.entity.mutuelle.salaire;
//
//import java.io.Serializable;
//import java.util.Date;
//import java.util.List;
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Entity; import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.OneToMany;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
//import yvs.entity.mutuelle.YvsMutMutuelle;
//import yvs.entity.users.YvsUsersAgence;
//
///**
// *
// * @author hp Elite 8300
// */
//@Entity
//@Table(name = "yvs_mut_periode_salaire")
//@NamedQueries({
//    @NamedQuery(name = "YvsMutPeriodeSalaire.findAll", query = "SELECT y FROM YvsMutPeriodeSalaire y WHERE y.mutuelle=:mutuelle ORDER BY y.periodeRh.debutMois"),
//    @NamedQuery(name = "YvsMutPeriodeSalaire.findById", query = "SELECT y FROM YvsMutPeriodeSalaire y WHERE y.id = :id"),
//    @NamedQuery(name = "YvsMutPeriodeSalaire.findByActif", query = "SELECT y FROM YvsMutPeriodeSalaire y WHERE y.actif = :actif"),
//    @NamedQuery(name = "YvsMutPeriodeSalaire.findByCloture", query = "SELECT y FROM YvsMutPeriodeSalaire y WHERE y.cloture = :cloture")})
//public class YvsMutPeriodeSalaire implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
//    @Column(name = "id")
//    private Long id;
//    @Column(name = "date_update")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date dateUpdate;
//    @Column(name = "actif")
//    private Boolean actif;
//    @Column(name = "cloture")
//    private Boolean cloture;
//    @OneToOne
//    @JoinColumn(name = "periode_rh", referencedColumnName = "id")
//    private YvsGrhOrdreCalculSalaire periodeRh;
//    @JoinColumn(name = "mutuelle", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsMutMutuelle mutuelle;
//    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
//    @Column(name = "date_save")
//    private Date dateSave;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "author", referencedColumnName = "id")
//    private YvsUsersAgence author;
//    @OneToMany(mappedBy = "periode")
//    private List<YvsMutPaiementSalaire> salaires;
//
//    public YvsMutPeriodeSalaire() {
//    }
//
//    public YvsMutPeriodeSalaire(Long id) {
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
//    public List<YvsMutPaiementSalaire> getSalaires() {
//        return salaires;
//    }
//
//    public void setSalaires(List<YvsMutPaiementSalaire> salaires) {
//        this.salaires = salaires;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Boolean getActif() {
//        return actif!=null?actif:false;
//    }
//
//    public void setActif(Boolean actif) {
//        this.actif = actif;
//    }
//
//    public Boolean getCloture() {
//        return cloture!=null?cloture:false;
//    }
//
//    public void setCloture(Boolean cloture) {
//        this.cloture = cloture;
//    }
//
//    public YvsGrhOrdreCalculSalaire getPeriodeRh() {
//        return periodeRh;
//    }
//
//    public void setPeriodeRh(YvsGrhOrdreCalculSalaire periodeRh) {
//        this.periodeRh = periodeRh;
//    }
//
//    public YvsMutMutuelle getMutuelle() {
//        return mutuelle;
//    }
//
//    public void setMutuelle(YvsMutMutuelle mutuelle) {
//        this.mutuelle = mutuelle;
//    }
//
//    public Date getDateSave() {
//        return dateSave;
//    }
//
//    public void setDateSave(Date dateSave) {
//        this.dateSave = dateSave;
//    }
//
//    public YvsUsersAgence getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(YvsUsersAgence author) {
//        this.author = author;
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
//        if (!(object instanceof YvsMutPeriodeSalaire)) {
//            return false;
//        }
//        YvsMutPeriodeSalaire other = (YvsMutPeriodeSalaire) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "yvs.entity.mutuelle.salaire.YvsMutPeriodeSalaire[ id=" + id + " ]";
//    }
//
//}

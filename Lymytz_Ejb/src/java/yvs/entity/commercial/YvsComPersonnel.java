///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.entity.commercial;
//
//import java.io.Serializable;
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
//import javax.persistence.Table;
//import javax.persistence.Transient;
//import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
//import yvs.entity.users.YvsUsers;
//import yvs.entity.users.YvsUsersAgence;
//
///**
// *
// * @author lymytz
// */
//@Entity
//@Table(name = "yvs_com_personnel")
//@NamedQueries({
//    @NamedQuery(name = "YvsComPersonnel.findAll", query = "SELECT y FROM YvsComPersonnel y WHERE y.users.agence.societe = :societe"),
//    @NamedQuery(name = "YvsComPersonnel.findByAgence", query = "SELECT y FROM YvsComPersonnel y WHERE y.users.agence = :agence"),
//    @NamedQuery(name = "YvsComPersonnel.findById", query = "SELECT y FROM YvsComPersonnel y WHERE y.id = :id")})
//public class YvsComPersonnel implements Serializable {
//
//    @OneToMany(mappedBy = "users")
//    private List<YvsComCreneauHoraireUsers> yvsComCreneauHoraireUsersList;
//    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
//    @Column(name = "id")
//       private Long id;      @Column(name = "date_update")     @Temporal(TemporalType.TIMESTAMP)     private Date dateUpdate; @Column(name = "date_save") @Temporal(TemporalType.DATE) private Date dateSave; 
//    @JoinColumn(name = "users", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsUsers users;
//    @JoinColumn(name = "categorie", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsComCategoriePersonnel categorie;
//    @JoinColumn(name = "plan_commission", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsComPlanCommission planCommission;
//    @JoinColumn(name = "author", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsUsersAgence author;
//    @Transient
//    private boolean new_;
//    @Transient
//    private boolean selectActif;
//
//    public YvsComPersonnel() {
//    }
//
//    public YvsComPersonnel(Long id) {
//        this.id = id;
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
//    public boolean isNew_() {
//        return new_;
//    }
//
//    public void setNew_(boolean new_) {
//        this.new_ = new_;
//    }
//
//    public boolean isSelectActif() {
//        return selectActif;
//    }
//
//    public void setSelectActif(boolean selectActif) {
//        this.selectActif = selectActif;
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
//    public YvsUsers getUsers() {
//        return users;
//    }
//
//    public void setUsers(YvsUsers users) {
//        this.users = users;
//    }
//
//    public YvsComCategoriePersonnel getCategorie() {
//        return categorie;
//    }
//
//    public void setCategorie(YvsComCategoriePersonnel categorie) {
//        this.categorie = categorie;
//    }
//
//    public YvsComPlanCommission getPlanCommission() {
//        return planCommission;
//    }
//
//    public void setPlanCommission(YvsComPlanCommission planCommission) {
//        this.planCommission = planCommission;
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
//        if (!(object instanceof YvsComPersonnel)) {
//            return false;
//        }
//        YvsComPersonnel other = (YvsComPersonnel) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "yvs.entity.commercial.YvsComPersonnel[ id=" + id + " ]";
//    }
//
//    public List<YvsComCreneauHoraireUsers> getYvsComCreneauHoraireUsersList() {
//        return yvsComCreneauHoraireUsersList;
//    }
//
//    public void setYvsComCreneauHoraireUsersList(List<YvsComCreneauHoraireUsers> yvsComCreneauHoraireUsersList) {
//        this.yvsComCreneauHoraireUsersList = yvsComCreneauHoraireUsersList;
//    }
//
//}

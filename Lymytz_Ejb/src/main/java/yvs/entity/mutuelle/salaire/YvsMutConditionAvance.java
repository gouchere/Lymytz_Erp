///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.entity.mutuelle.salaire;
//
//import java.io.Serializable;
//import java.util.Date;
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
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.persistence.Transient;
//import javax.validation.constraints.Size;
//
///**
// *
// * @author hp Elite 8300
// */
//@Entity
//@Table(name = "yvs_mut_condition_avance")
//@NamedQueries({
//    @NamedQuery(name = "YvsMutConditionAvance.findAll", query = "SELECT y FROM YvsMutConditionAvance y"),
//    @NamedQuery(name = "YvsMutConditionAvance.findById", query = "SELECT y FROM YvsMutConditionAvance y WHERE y.id = :id"),
//    @NamedQuery(name = "YvsMutConditionAvance.findByLibelle", query = "SELECT y FROM YvsMutConditionAvance y WHERE y.libelle = :libelle"),
//    @NamedQuery(name = "YvsMutConditionAvance.findByValeurRequise", query = "SELECT y FROM YvsMutConditionAvance y WHERE y.valeurRequise = :valeurRequise"),
//    @NamedQuery(name = "YvsMutConditionAvance.findByValeurEntree", query = "SELECT y FROM YvsMutConditionAvance y WHERE y.valeurEntree = :valeurEntree"),
//    @NamedQuery(name = "YvsMutConditionAvance.findByCorrect", query = "SELECT y FROM YvsMutConditionAvance y WHERE y.correct = :correct"),
//    @NamedQuery(name = "YvsMutConditionAvance.findByComentaire", query = "SELECT y FROM YvsMutConditionAvance y WHERE y.comentaire = :comentaire"),
//    @NamedQuery(name = "YvsMutConditionAvance.findByDateModification", query = "SELECT y FROM YvsMutConditionAvance y WHERE y.dateModification = :dateModification")})
//public class YvsMutConditionAvance implements Serializable {
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
//    @Column(name = "date_save")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date dateSave;
//    @Size(max = 2147483647)
//    @Column(name = "libelle")
//    private String libelle;
//    @Column(name = "code")
//    private String code;
//    @Column(name = "valeur_requise")
//    private Double valeurRequise;
//    @Column(name = "valeur_entree")
//    private Double valeurEntree;
//    @Column(name = "unite")
//    private String unite;
//    @Column(name = "date_modification")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date dateModification;
//    @Column(name = "correct")
//    private Boolean correct;
//    @Size(max = 2147483647)
//    @Column(name = "comentaire")
//    private String comentaire;
//    @JoinColumn(name = "avance", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsMutAvanceSalaire avance;
//    @Transient
//    private boolean selectActif;
//    @Transient
//    private boolean new_;
//    @Transient
//    private boolean update;
//    @Transient
//    private double valeurRequise2;
//
//    public YvsMutConditionAvance() {
//    }
//
//    public YvsMutConditionAvance(Long id) {
//        this.id = id;
//    }
//
//    public YvsMutConditionAvance(long id, String libelle) {
//        this.id = id;
//        this.libelle = libelle;
//    }
//
//    public YvsMutConditionAvance(long id, String libelle, boolean correct) {
//        this.id = id;
//        this.libelle = libelle;
//        this.correct = correct;
//    }
//
//    public YvsMutConditionAvance(long id, String libelle, double valeur, double entree, String unite, boolean correct) {
//        this.id = id;
//        this.libelle = libelle;
//        this.valeurRequise = valeur;
//        this.valeurEntree = entree;
//        this.correct = correct;
//        this.unite = unite;
//    }
//
//    public YvsMutConditionAvance(long id, String code, String libelle, double valeur, double entree, String unite, boolean correct) {
//        this.id = id;
//        this.code = code;
//        this.libelle = libelle;
//        this.valeurRequise = valeur;
//        this.valeurEntree = entree;
//        this.correct = correct;
//        this.unite = unite;
//    }
//
//    public YvsMutConditionAvance(long id, String code, String libelle, double valeur, double valeur2, double entree, String unite, boolean correct) {
//        this.id = id;
//        this.code = code;
//        this.libelle = libelle;
//        this.valeurRequise = valeur;
//        this.valeurEntree = entree;
//        this.correct = correct;
//        this.unite = unite;
//        this.valeurRequise2 = valeur2;
//    }
//
//    public YvsMutConditionAvance(YvsMutConditionAvance c) {
//        this.id = c.id;
//        this.code = c.code;
//        this.libelle = c.libelle;
//        this.valeurRequise = c.valeurRequise;
//        this.valeurEntree = c.valeurEntree;
//        this.correct = c.correct;
//        this.unite = c.unite;
//        this.dateModification = c.dateModification;
//        this.valeurRequise2 = c.getValeurRequise2();
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
//    public void setDateSave(Date dateSave) {
//        this.dateSave = dateSave;
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
//    public String getLibelle() {
//        return libelle;
//    }
//
//    public void setLibelle(String libelle) {
//        this.libelle = libelle;
//    }
//
//    public String getUnite() {
//        return unite;
//    }
//
//    public void setUnite(String unite) {
//        this.unite = unite;
//    }
//
//    public Double getValeurRequise() {
//        return valeurRequise != null ? valeurRequise : 0;
//    }
//
//    public void setValeurRequise(Double valeurRequise) {
//        this.valeurRequise = valeurRequise;
//    }
//
//    public Double getValeurEntree() {
//        return valeurEntree != null ? valeurEntree : 0;
//    }
//
//    public void setValeurEntree(Double valeurEntree) {
//        this.valeurEntree = valeurEntree;
//    }
//
//    public Boolean getCorrect() {
//        return correct != null ? correct : false;
//    }
//
//    public void setCorrect(Boolean correct) {
//        this.correct = correct;
//    }
//
//    public String getComentaire() {
//        return comentaire;
//    }
//
//    public void setComentaire(String comentaire) {
//        this.comentaire = comentaire;
//    }
//
//    public Date getDateModification() {
//        return dateModification;
//    }
//
//    public void setDateModification(Date dateModification) {
//        this.dateModification = dateModification;
//    }
//
//    public YvsMutAvanceSalaire getAvance() {
//        return avance;
//    }
//
//    public void setAvance(YvsMutAvanceSalaire avance) {
//        this.avance = avance;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
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
//    public boolean isNew_() {
//        return new_;
//    }
//
//    public void setNew_(boolean new_) {
//        this.new_ = new_;
//    }
//
//    public boolean isUpdate() {
//        return update;
//    }
//
//    public void setUpdate(boolean update) {
//        this.update = update;
//    }
//
//    public double getValeurRequise2() {
//        return valeurRequise2;
//    }
//
//    public void setValeurRequise2(double valeurRequise2) {
//        this.valeurRequise2 = valeurRequise2;
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
//        if (!(object instanceof YvsMutConditionAvance)) {
//            return false;
//        }
//        YvsMutConditionAvance other = (YvsMutConditionAvance) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "yvs.entity.mutuelle.credit.YvsMutConditionAvance[ id=" + id + " ]";
//    }
//
//}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.credit;

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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_mut_condition_credit")
@NamedQueries({
    @NamedQuery(name = "YvsMutConditionCredit.findAll", query = "SELECT y FROM YvsMutConditionCredit y"),
    @NamedQuery(name = "YvsMutConditionCredit.findById", query = "SELECT y FROM YvsMutConditionCredit y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutConditionCredit.findByLibelle", query = "SELECT y FROM YvsMutConditionCredit y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsMutConditionCredit.findByValeurRequise", query = "SELECT y FROM YvsMutConditionCredit y WHERE y.valeurRequise = :valeurRequise"),
    @NamedQuery(name = "YvsMutConditionCredit.findByValeurEntree", query = "SELECT y FROM YvsMutConditionCredit y WHERE y.valeurEntree = :valeurEntree"),
    @NamedQuery(name = "YvsMutConditionCredit.findByCorrect", query = "SELECT y FROM YvsMutConditionCredit y WHERE y.correct = :correct"),
    @NamedQuery(name = "YvsMutConditionCredit.findByComentaire", query = "SELECT y FROM YvsMutConditionCredit y WHERE y.commentaire = :comentaire"),
    @NamedQuery(name = "YvsMutConditionCredit.findByDateModification", query = "SELECT y FROM YvsMutConditionCredit y WHERE y.dateModification = :dateModification")})
public class YvsMutConditionCredit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_condition_credit_id_seq", name = "yvs_mut_condition_credit_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_condition_credit_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "code")
    private String code;
    @Column(name = "valeur_requise")
    private Double valeurRequise;
    @Column(name = "valeur_entree")
    private Double valeurEntree;
    @Column(name = "unite")
    private String unite;
    @Column(name = "correct")
    private Boolean correct;
    @Size(max = 2147483647)
    @Column(name = "comentaire")
    private String commentaire;
    @Column(name = "date_modification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;
    
    @JoinColumn(name = "credit", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCredit credit;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private boolean update;
    @Transient
    private double valeurRequise2;

    public YvsMutConditionCredit() {
    }

    public YvsMutConditionCredit(Long id) {
        this.id = id;
    }

    public YvsMutConditionCredit(long id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public YvsMutConditionCredit(long id, String libelle, boolean correct) {
        this.id = id;
        this.libelle = libelle;
        this.correct = correct;
    }

    public YvsMutConditionCredit(long id, String libelle, double valeur, double entree, String unite, boolean correct) {
        this.id = id;
        this.libelle = libelle;
        this.valeurRequise = valeur;
        this.valeurEntree = entree;
        this.correct = correct;
        this.unite = unite;
    }

    public YvsMutConditionCredit(long id, String code, String libelle, double valeur, double entree, String unite, boolean correct) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.valeurRequise = valeur;
        this.valeurEntree = entree;
        this.correct = correct;
        this.unite = unite;
    }

    public YvsMutConditionCredit(long id, String code, String libelle, double valeur, double valeur2, double entree, String unite, boolean correct) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.valeurRequise = valeur;
        this.valeurEntree = entree;
        this.correct = correct;
        this.unite = unite;
        this.valeurRequise2 = valeur2;
    }

    public YvsMutConditionCredit(YvsMutConditionCredit c) {
        this.id = c.id;
        this.code = c.code;
        this.libelle = c.libelle;
        this.valeurRequise = c.valeurRequise;
        this.valeurEntree = c.valeurEntree;
        this.correct = c.correct;
        this.unite = c.unite;
        this.dateModification = c.dateModification;
        this.valeurRequise2 = c.getValeurRequise2();
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

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public double getValeurRequise2() {
        return valeurRequise2;
    }

    public void setValeurRequise2(double valeurRequise2) {
        this.valeurRequise2 = valeurRequise2;
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

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public Double getValeurRequise() {
        return valeurRequise;
    }

    public void setValeurRequise(Double valeurRequise) {
        this.valeurRequise = valeurRequise;
    }

    public Double getValeurEntree() {
        return valeurEntree;
    }

    public void setValeurEntree(Double valeurEntree) {
        this.valeurEntree = valeurEntree;
    }

    public Boolean getCorrect() {
        return (correct == null) ? false : correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    public YvsMutCredit getCredit() {
        return credit;
    }

    public void setCredit(YvsMutCredit credit) {
        this.credit = credit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getTextCondition() {
        switch (code) {
            case "A":   //
                return "Période requise";
            case "B":
                return "Valeur maximale ";
            case "N":
                return "Nombre requis";
            case "C":
                return "Valeur Maximale ";
            case "D":
                return "Durée Maximale ";
            case "E":
                return "Couverture requise";
            case "G":
                return "Période requise";
            case "F":
                return "Remboursement attendu";
            default:
                return "";
        }
    }

    public String getTextCondition2() {
        switch (code) {
            case "A":   //
                return "Ancienneté ";
            case "B":
                return "Dettes cumulée ";
            case "N":
                return "Nombre indiqué";
            case "C":
                return "Valeur entrée ";
            case "D":
                return "Durée demandé ";
            case "E":
                return "Couverture réelle";
            case "G":
                return "";
            case "F":
                return "Remboursement planifié";
            default:
                return "";
        }
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
        if (!(object instanceof YvsMutConditionCredit)) {
            return false;
        }
        YvsMutConditionCredit other = (YvsMutConditionCredit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.credit.YvsMutConditionCredit[ id=" + id + " ]";
    }

}

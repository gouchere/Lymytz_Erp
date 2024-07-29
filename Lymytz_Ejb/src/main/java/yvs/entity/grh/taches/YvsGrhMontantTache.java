/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.taches;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import yvs.entity.grh.param.YvsGrhPrimeTache;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_grh_montant_tache")
@NamedQueries({
    @NamedQuery(name = "YvsMontantTache.findAll", query = "SELECT y FROM YvsGrhMontantTache y"),
    @NamedQuery(name = "YvsMontantTache.findByRegleTache", query = "SELECT y FROM YvsGrhMontantTache y WHERE y.regleTache = :regleTache"),
    @NamedQuery(name = "YvsMontantTache.findByTache", query = "SELECT y FROM YvsGrhMontantTache y WHERE y.taches = :tache"),
    @NamedQuery(name = "YvsMontantTache.findByMontant", query = "SELECT y FROM YvsGrhMontantTache y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsMontantTache.findBySupp", query = "SELECT y FROM YvsGrhMontantTache y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsMontantTache.findByActif", query = "SELECT y FROM YvsGrhMontantTache y WHERE y.actif = :actif")})
public class YvsGrhMontantTache implements Serializable {

    @JoinColumn(name = "prime_tache", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPrimeTache primeTache;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_montant_tache_id_seq", name = "yvs_grh_montant_tache_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_montant_tache_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "tache", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTaches taches;
    @JoinColumn(name = "regle_tache", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhRegleTache regleTache;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "tache")
    private List<YvsGrhTacheEmps> employes;

    public YvsGrhMontantTache() {
    }

    public YvsGrhMontantTache(Long id) {
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

    public Boolean isSupp() {
        return supp;
    }

    public Boolean isActif() {
        return actif == null ? false : actif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
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

    public YvsGrhTaches getTaches() {
        return taches;
    }

    public void setTaches(YvsGrhTaches taches) {
        this.taches = taches;
    }

    public YvsGrhRegleTache getRegleTache() {
        return regleTache;
    }

    public void setRegleTache(YvsGrhRegleTache regleTache) {
        this.regleTache = regleTache;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public List<YvsGrhTacheEmps> getEmployes() {
        return employes;
    }

    public void setEmployes(List<YvsGrhTacheEmps> employes) {
        this.employes = employes;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final YvsGrhMontantTache other = (YvsGrhMontantTache) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "YvsGrhMontantTache{" + "id=" + id + '}';
    }

    public YvsGrhPrimeTache getPrimeTache() {
        return primeTache;
    }

    public void setPrimeTache(YvsGrhPrimeTache primeTache) {
        this.primeTache = primeTache;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.salaire;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.validation.constraints.Size;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_categorie_element")
@NamedQueries({
    @NamedQuery(name = "YvsGrhCategorieElement.findAll", query = "SELECT y FROM YvsGrhCategorieElement y WHERE y.societe=:societe"),
    @NamedQuery(name = "YvsGrhCategorieElement.findById", query = "SELECT y FROM YvsGrhCategorieElement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhCategorieElement.findByPrime", query = "SELECT y FROM YvsGrhCategorieElement y WHERE y.defaultPrime = true AND y.societe = :societe"),
    @NamedQuery(name = "YvsGrhCategorieElement.findByRetenue", query = "SELECT y FROM YvsGrhCategorieElement y WHERE y.defaultRetenue = true AND y.societe = :societe"),
    @NamedQuery(name = "YvsGrhCategorieElement.findByCodeCategorieC", query = "SELECT COUNT(y) FROM YvsGrhCategorieElement y WHERE y.codeCategorie = :code AND y.societe=:societe"),
    @NamedQuery(name = "YvsGrhCategorieElement.findByCodeCategorie", query = "SELECT y FROM YvsGrhCategorieElement y WHERE upper(y.codeCategorie) = upper(:code) AND y.societe=:societe")})
public class YvsGrhCategorieElement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_categorie_element_id_seq", name = "yvs_categorie_element_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_categorie_element_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "code_categorie")
    private String codeCategorie;
    @Column(name = "designation")
    private String designation;
    @Column(name = "description")
    private String description;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "default_prime")
    private Boolean defaultPrime;
    @Column(name = "default_retenue")
    private Boolean defaultRetenue;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "categorie")
    private List<YvsGrhElementSalaire> elementsSalaires;

    public YvsGrhCategorieElement() {
    }

    public YvsGrhCategorieElement(Long id) {
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

    public String getCodeCategorie() {
        return codeCategorie;
    }

    public void setCodeCategorie(String codeCategorie) {
        this.codeCategorie = codeCategorie;
    }

    public List<YvsGrhElementSalaire> getElementsSalaires() {
        return elementsSalaires;
    }

    public void setElementsSalaires(List<YvsGrhElementSalaire> elementsSalaires) {
        this.elementsSalaires = elementsSalaires;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public String getDescription() {
        return description;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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
        if (!(object instanceof YvsGrhCategorieElement)) {
            return false;
        }
        YvsGrhCategorieElement other = (YvsGrhCategorieElement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsCategorieElement[ id=" + id + " ]";
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getDefaultPrime() {
        return defaultPrime!=null?defaultPrime:false;
    }

    public void setDefaultPrime(Boolean defaultPrime) {
        this.defaultPrime = defaultPrime;
    }

    public Boolean getDefaultRetenue() {
        return defaultRetenue!=null?defaultRetenue:false;
    }

    public void setDefaultRetenue(Boolean defaultRetenue) {
        this.defaultRetenue = defaultRetenue;
    }

}

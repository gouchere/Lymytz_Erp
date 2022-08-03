/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.tiers;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_contacts_tiers")
@NamedQueries({
    @NamedQuery(name = "YvsContactsTiers.findAll", query = "SELECT y FROM YvsContactsTiers y"),
    @NamedQuery(name = "YvsContactsTiers.findById", query = "SELECT y FROM YvsContactsTiers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsContactsTiers.findByNom", query = "SELECT y FROM YvsContactsTiers y WHERE y.nom = :nom"),
    @NamedQuery(name = "YvsContactsTiers.findByTelephone", query = "SELECT y FROM YvsContactsTiers y WHERE y.telephone = :telephone"),
    @NamedQuery(name = "YvsContactsTiers.findByAdress", query = "SELECT y FROM YvsContactsTiers y WHERE y.adress = :adress"),
    @NamedQuery(name = "YvsContactsTiers.findBySupp", query = "SELECT y FROM YvsContactsTiers y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsContactsTiers.findByActif", query = "SELECT y FROM YvsContactsTiers y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsContactsTiers.findByCivilite", query = "SELECT y FROM YvsContactsTiers y WHERE y.civilite = :civilite"),
    @NamedQuery(name = "YvsContactsTiers.findByEmail", query = "SELECT y FROM YvsContactsTiers y WHERE y.email = :email")})
public class YvsContactsTiers implements Serializable {

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_contacts_tiers_id_seq", name = "yvs_contacts_tiers_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_contacts_tiers_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nom")
    private String nom;
    @Size(max = 2147483647)
    @Column(name = "telephone")
    private String telephone;
    @Size(max = 2147483647)
    @Column(name = "adress")
    private String adress;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 2147483647)
    @Column(name = "civilite")
    private String civilite;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 2147483647)
    @Column(name = "email")
    private String email;
    @JoinColumn(name = "tiers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTiers tiers;

    public YvsContactsTiers() {
    }

    public YvsContactsTiers(Integer id) {
        this.id = id;
    }

    public YvsContactsTiers(Integer id, String nom) {
        this.id = id;
        this.nom = nom;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
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

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public YvsBaseTiers getTiers() {
        return tiers;
    }

    public void setTiers(YvsBaseTiers tiers) {
        this.tiers = tiers;
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
        if (!(object instanceof YvsContactsTiers)) {
            return false;
        }
        YvsContactsTiers other = (YvsContactsTiers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.tiers.YvsContactsTiers[ id=" + id + " ]";
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

}

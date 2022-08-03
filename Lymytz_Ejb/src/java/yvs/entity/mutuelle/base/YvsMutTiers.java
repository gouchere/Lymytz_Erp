/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.base;

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
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_tiers")
@NamedQueries({
    @NamedQuery(name = "YvsMutTiers.findAll", query = "SELECT y FROM YvsMutTiers y WHERE y.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutTiers.findByMutuelle", query = "SELECT y FROM YvsMutTiers y WHERE y.mutuelle = :mutuelle"),
    @NamedQuery(name = "YvsMutTiers.findByMutuelleType", query = "SELECT y FROM YvsMutTiers y WHERE y.mutuelle = :mutuelle AND y.typeTiers = :typeTiers"),
    @NamedQuery(name = "YvsMutTiers.findById", query = "SELECT y FROM YvsMutTiers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutTiers.findByNom", query = "SELECT y FROM YvsMutTiers y WHERE y.nom = :nom"),
    @NamedQuery(name = "YvsMutTiers.findByPrenom", query = "SELECT y FROM YvsMutTiers y WHERE y.prenom = :prenom"),
    @NamedQuery(name = "YvsMutTiers.findByRaisonSociale", query = "SELECT y FROM YvsMutTiers y WHERE y.raisonSociale = :raisonSociale"),
    @NamedQuery(name = "YvsMutTiers.findByAdresse", query = "SELECT y FROM YvsMutTiers y WHERE y.adresse = :adresse"),
    @NamedQuery(name = "YvsMutTiers.findByTelephone", query = "SELECT y FROM YvsMutTiers y WHERE y.telephone = :telephone"),
    @NamedQuery(name = "YvsMutTiers.findByTypeTiers", query = "SELECT y FROM YvsMutTiers y WHERE y.typeTiers = :typeTiers"),
    @NamedQuery(name = "YvsMutTiers.findByIdExterne", query = "SELECT y FROM YvsMutTiers y WHERE y.idExterne = :idExterne")})
public class YvsMutTiers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_tiers_id_seq1", name = "yvs_mut_tiers_id_seq1_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_tiers_id_seq1_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "id_externe")
    private Long idExterne;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "nom")
    private String nom;
    @Size(max = 2147483647)
    @Column(name = "prenom")
    private String prenom;
    @Size(max = 2147483647)
    @Column(name = "raison_sociale")
    private String raisonSociale;
    @Size(max = 2147483647)
    @Column(name = "adresse")
    private String adresse;
    @Size(max = 2147483647)
    @Column(name = "telephone")
    private String telephone;
    @Size(max = 2147483647)
    @Column(name = "type_tiers")
    private String typeTiers;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "mutuelle", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutuelle mutuelle;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private YvsGrhEmployes employe = new YvsGrhEmployes();
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;

    public YvsMutTiers() {
    }

    public YvsMutTiers(Long id) {
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

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTypeTiers() {
        return typeTiers;
    }

    public void setTypeTiers(String typeTiers) {
        this.typeTiers = typeTiers;
    }

    public Long getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(Long idExterne) {
        this.idExterne = idExterne;
    }

    public YvsMutMutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(YvsMutMutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getActif() {
        return (actif != null) ? actif : false;
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
        if (!(object instanceof YvsMutTiers)) {
            return false;
        }
        YvsMutTiers other = (YvsMutTiers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.operation.YvsMutTiers[ id=" + id + " ]";
    }

}

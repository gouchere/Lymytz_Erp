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
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_visiteur")
@NamedQueries({
    @NamedQuery(name = "YvsBaseVisiteur.findAll", query = "SELECT y FROM YvsBaseVisiteur y"),
    @NamedQuery(name = "YvsBaseVisiteur.findNoms", query = "SELECT CONCAT(y.nom, ' ', y.prenom) FROM YvsBaseVisiteur y"),
    @NamedQuery(name = "YvsBaseVisiteur.findById", query = "SELECT y FROM YvsBaseVisiteur y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseVisiteur.findByNoms", query = "SELECT y FROM YvsBaseVisiteur y WHERE CONCAT(y.nom, ' ', y.prenom) = :noms"),
    @NamedQuery(name = "YvsBaseVisiteur.findByTelephone", query = "SELECT y FROM YvsBaseVisiteur y WHERE y.telephone = :telephone"),
    @NamedQuery(name = "YvsBaseVisiteur.findByNomsPhone", query = "SELECT y FROM YvsBaseVisiteur y WHERE y.telephone = :telephone AND CONCAT(y.nom, ' ', y.prenom) = :noms")})
public class YvsBaseVisiteur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_visiteur_id_seq", name = "yvs_base_visiteur_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_visiteur_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 255)
    @Column(name = "adresse")
    private String adresse;
    @Size(max = 255)
    @Column(name = "telephone")
    private String telephone;
    @Size(max = 255)
    @Column(name = "civilite")
    private String civilite;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "photo")
    private String photo;
    @Size(max = 255)
    @Column(name = "nom")
    private String nom;
    @Size(max = 255)
    @Column(name = "prenom")
    private String prenom;
    @Size(max = 255)
    @Column(name = "cni")
    private String cni;

    @JoinColumn(name = "pays", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire pays;
    @JoinColumn(name = "ville", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire ville;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private boolean error;
    @Transient
    private String nom_prenom;

    public YvsBaseVisiteur() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseVisiteur(String telephone) {
        this();
        this.telephone = telephone;
    }

    public YvsBaseVisiteur(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseVisiteur(Long id, String telephone) {
        this(id);
        this.telephone = telephone;
    }

    public YvsBaseVisiteur(Long id, String nom, String prenom) {
        this(id);
        this.nom = nom;
        this.prenom = prenom;
    }

    public YvsBaseVisiteur(Long id, String telephone, String nom, String prenom) {
        this.id = id;
        this.telephone = telephone;
        this.nom = nom;
        this.prenom = prenom;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom != null ? nom : "";
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCni() {
        return cni;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    public String getNom_prenom() {
        nom_prenom = "";
        if (!(getNom() == null || getNom().trim().equals(""))) {
            nom_prenom = getNom();
        }
        if (!(getPrenom() == null || getPrenom().trim().equals(""))) {
            if (nom_prenom == null || nom_prenom.trim().equals("")) {
                nom_prenom = getPrenom();
            } else {
                nom_prenom += " " + getPrenom();
            }
        }
        return nom_prenom;
    }

    public void setNom_prenom(String nom_prenom) {
        this.nom_prenom = nom_prenom;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public YvsDictionnaire getPays() {
        return pays;
    }

    public void setPays(YvsDictionnaire pays) {
        this.pays = pays;
    }

    public YvsDictionnaire getVille() {
        return ville;
    }

    public void setVille(YvsDictionnaire ville) {
        this.ville = ville;
    }

    @XmlTransient  @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsBaseVisiteur)) {
            return false;
        }
        YvsBaseVisiteur other = (YvsBaseVisiteur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.tiers.YvsBaseVisiteur[ id=" + id + " ]";
    }
}

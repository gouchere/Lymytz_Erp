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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_base_visiteur_point_livraison")
@NamedQueries({
    @NamedQuery(name = "YvsBaseVisiteurPointLivraison.findAll", query = "SELECT y FROM YvsBaseVisiteurPointLivraison y ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseVisiteurPointLivraison.findById", query = "SELECT y FROM YvsBaseVisiteurPointLivraison y WHERE y.id = :id ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseVisiteurPointLivraison.findByLibelle", query = "SELECT y FROM YvsBaseVisiteurPointLivraison y WHERE y.libelle = :libelle ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseVisiteurPointLivraison.findByVisiteur", query = "SELECT y FROM YvsBaseVisiteurPointLivraison y WHERE y.visiteur = :visiteur ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseVisiteurPointLivraison.findByVisiteurVille", query = "SELECT y FROM YvsBaseVisiteurPointLivraison y WHERE y.visiteur = :visiteur AND y.secteur.parent = :ville ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseVisiteurPointLivraison.findByVisiteurSecteur", query = "SELECT y FROM YvsBaseVisiteurPointLivraison y WHERE y.visiteur = :visiteur AND y.secteur = :secteur ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseVisiteurPointLivraison.findByDateSave", query = "SELECT y FROM YvsBaseVisiteurPointLivraison y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseVisiteurPointLivraison.findByDateUpdate", query = "SELECT y FROM YvsBaseVisiteurPointLivraison y WHERE y.dateUpdate = :dateUpdate")})
public class YvsBaseVisiteurPointLivraison implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_visiteur_point_livraison_id_seq", name = "yvs_base_visiteur_point_livraison_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_visiteur_point_livraison_id_seq_name", strategy = GenerationType.SEQUENCE) 
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "secteur", referencedColumnName = "id")
    @ManyToOne
    private YvsDictionnaire secteur;
    @JoinColumn(name = "visiteur", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseVisiteur visiteur;

    public YvsBaseVisiteurPointLivraison() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseVisiteurPointLivraison(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlTransient  @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsDictionnaire getSecteur() {
        return secteur;
    }

    public void setSecteur(YvsDictionnaire secteur) {
        this.secteur = secteur;
    }

    @XmlTransient  @JsonIgnore
    public YvsBaseVisiteur getVisiteur() {
        return visiteur;
    }

    public void setVisiteur(YvsBaseVisiteur visiteur) {
        this.visiteur = visiteur;
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
        if (!(object instanceof YvsBaseVisiteurPointLivraison)) {
            return false;
        }
        YvsBaseVisiteurPointLivraison other = (YvsBaseVisiteurPointLivraison) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.tiers.YvsBaseVisiteurPointLivraison[ id=" + id + " ]";
    }
    
}

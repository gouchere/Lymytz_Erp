/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.vente;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_contenu_doc_vente_etat")
@NamedQueries({
    @NamedQuery(name = "YvsComContenuDocVenteEtat.findAll", query = "SELECT y FROM YvsComContenuDocVenteEtat y"),
    @NamedQuery(name = "YvsComContenuDocVenteEtat.findById", query = "SELECT y FROM YvsComContenuDocVenteEtat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComContenuDocVenteEtat.findByLibelle", query = "SELECT y FROM YvsComContenuDocVenteEtat y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsComContenuDocVenteEtat.findByValeur", query = "SELECT y FROM YvsComContenuDocVenteEtat y WHERE y.valeur = :valeur"),
    @NamedQuery(name = "YvsComContenuDocVenteEtat.findByDateSave", query = "SELECT y FROM YvsComContenuDocVenteEtat y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComContenuDocVenteEtat.findByDateUpdate", query = "SELECT y FROM YvsComContenuDocVenteEtat y WHERE y.dateUpdate = :dateUpdate")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComContenuDocVenteEtat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "valeur")
    private String valeur;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "contenu", referencedColumnName = "id")
    @ManyToOne
    private YvsComContenuDocVente contenu;
    @Transient
    private boolean new_;

    public YvsComContenuDocVenteEtat() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComContenuDocVenteEtat(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id!=null?id:0;
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

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComContenuDocVente getContenu() {
        return contenu;
    }

    public void setContenu(YvsComContenuDocVente contenu) {
        this.contenu = contenu;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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
        if (!(object instanceof YvsComContenuDocVenteEtat)) {
            return false;
        }
        YvsComContenuDocVenteEtat other = (YvsComContenuDocVenteEtat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.vente.YvsComContenuDocVenteEtat[ id=" + id + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_societes_avis")
@NamedQueries({
    @NamedQuery(name = "YvsSocietesAvis.findAll", query = "SELECT y FROM YvsSocietesAvis y"),
    @NamedQuery(name = "YvsSocietesAvis.findById", query = "SELECT y FROM YvsSocietesAvis y WHERE y.id = :id"),
    @NamedQuery(name = "YvsSocietesAvis.findByNote", query = "SELECT y FROM YvsSocietesAvis y WHERE y.note = :note"),
    @NamedQuery(name = "YvsSocietesAvis.findByCommentaire", query = "SELECT y FROM YvsSocietesAvis y WHERE y.commentaire = :commentaire"),
    @NamedQuery(name = "YvsSocietesAvis.findByDateSave", query = "SELECT y FROM YvsSocietesAvis y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsSocietesAvis.findByDateUpdate", query = "SELECT y FROM YvsSocietesAvis y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsSocietesAvis.findByAuteur", query = "SELECT y FROM YvsSocietesAvis y WHERE y.auteur = :auteur"),
    @NamedQuery(name = "YvsSocietesAvis.findByTelephone", query = "SELECT y FROM YvsSocietesAvis y WHERE y.telephone = :telephone"),
    @NamedQuery(name = "YvsSocietesAvis.findByEmail", query = "SELECT y FROM YvsSocietesAvis y WHERE y.email = :email")})
public class YvsSocietesAvis extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "note")
    private Integer note;
    @Size(max = 2147483647)
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Size(max = 2147483647)
    @Column(name = "auteur")
    private String auteur;
    @Size(max = 2147483647)
    @Column(name = "telephone")
    private String telephone;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 2147483647)
    @Column(name = "email")
    private String email;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;

    public YvsSocietesAvis() {
    }

    public YvsSocietesAvis(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNote() {
        return note != null ? note : 0;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire != null ? commentaire : "";
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
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

    public String getAuteur() {
        return auteur != null ? auteur : "";
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getTelephone() {
        return telephone != null ? telephone : "";
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email != null ? email : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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
        if (!(object instanceof YvsSocietesAvis)) {
            return false;
        }
        YvsSocietesAvis other = (YvsSocietesAvis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.YvsSocietesAvis[ id=" + id + " ]";
    }

    @Override
    public YvsUsersAgence getAuthor() {
        return null;
    }

}

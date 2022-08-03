/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.formulaire;

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
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_param_user_formulaire")
@NamedQueries({
    @NamedQuery(name = "YvsParamUserFormulaire.findAll", query = "SELECT y FROM YvsParamUserFormulaire y"),
    @NamedQuery(name = "YvsParamUserFormulaire.findById", query = "SELECT y FROM YvsParamUserFormulaire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsParamUserFormulaire.findByOne", query = "SELECT y.formulaire FROM YvsParamUserFormulaire y WHERE y.utilisateur = :utilisateur AND y.formulaire.formulaire.intitule = :intitule")})
public class YvsParamUserFormulaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_param_user_formulaire_id_seq", name = "yvs_param_user_formulaire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_param_user_formulaire_id_seq_name", strategy = GenerationType.SEQUENCE) 
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "utilisateur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers utilisateur;
    @JoinColumn(name = "formulaire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsParamModelFormulaire formulaire;

    public YvsParamUserFormulaire() {
    }

    public YvsParamUserFormulaire(Long id) {
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsers getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(YvsUsers utilisateur) {
        this.utilisateur = utilisateur;
    }

    public YvsParamModelFormulaire getFormulaire() {
        return formulaire;
    }

    public void setFormulaire(YvsParamModelFormulaire formulaire) {
        this.formulaire = formulaire;
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
        if (!(object instanceof YvsParamUserFormulaire)) {
            return false;
        }
        YvsParamUserFormulaire other = (YvsParamUserFormulaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.formulaire.YvsParamUserFormulaire[ id=" + id + " ]";
    }

}

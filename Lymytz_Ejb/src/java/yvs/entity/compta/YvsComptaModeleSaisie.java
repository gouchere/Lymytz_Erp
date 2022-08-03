/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_modele_saisie")
@NamedQueries({
    @NamedQuery(name = "YvsComptaModeleSaisie.findAll", query = "SELECT y FROM YvsComptaModeleSaisie y"),
    @NamedQuery(name = "YvsComptaModeleSaisie.findById", query = "SELECT y FROM YvsComptaModeleSaisie y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaModeleSaisie.findByIntitule", query = "SELECT y FROM YvsComptaModeleSaisie y WHERE y.intitule = :intitule"),
    @NamedQuery(name = "YvsComptaModeleSaisie.findByTypeModele", query = "SELECT y FROM YvsComptaModeleSaisie y WHERE y.typeModele = :typeModele")})
public class YvsComptaModeleSaisie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_modele_saisie_id_seq", name = "yvs_compta_modele_saisie_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_modele_saisie_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "intitule")
    private String intitule;
    @Size(max = 2147483647)
    @Column(name = "type_modele")
    private String typeModele;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @OneToMany(mappedBy = "modeleSaisie")
    private List<YvsComptaContentModeleSaisi> contentsModel;
    @Column(name = "actif")
    private Boolean actif;

    public YvsComptaModeleSaisie() {
        contentsModel = new ArrayList<>();
    }

    public YvsComptaModeleSaisie(Long id) {
        this.id = id;
        contentsModel = new ArrayList<>();
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

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getTypeModele() {
        return typeModele;
    }

    public void setTypeModele(String typeModele) {
        this.typeModele = typeModele;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }


    @XmlTransient
    @JsonIgnore
    public List<YvsComptaContentModeleSaisi> getContentsModel() {
        Collections.sort(contentsModel, Collections.reverseOrder());
        return contentsModel;
    }

    public void setContentsModel(List<YvsComptaContentModeleSaisi> contentsModel) {
        this.contentsModel = contentsModel;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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
        if (!(object instanceof YvsComptaModeleSaisie)) {
            return false;
        }
        YvsComptaModeleSaisie other = (YvsComptaModeleSaisie) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaModeleSaisie[ id=" + id + " ]";
    }

}

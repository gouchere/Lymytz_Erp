/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.formulaire;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_param_model_formulaire")
@NamedQueries({
    @NamedQuery(name = "YvsParamModelFormulaire.findAll", query = "SELECT y FROM YvsParamModelFormulaire y"),
    @NamedQuery(name = "YvsParamModelFormulaire.findById", query = "SELECT y FROM YvsParamModelFormulaire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsParamModelFormulaire.findByOne", query = "SELECT y FROM YvsParamModelFormulaire y WHERE y.intitule = :intitule AND y.formulaire = :formulaire"),
    @NamedQuery(name = "YvsParamModelFormulaire.findByForm", query = "SELECT y FROM YvsParamModelFormulaire y WHERE y.formulaire = :formulaire"),
    @NamedQuery(name = "YvsParamModelFormulaire.findByIntitule", query = "SELECT y FROM YvsParamModelFormulaire y WHERE y.intitule = :intitule"),
    @NamedQuery(name = "YvsParamModelFormulaire.findByAuthor", query = "SELECT y FROM YvsParamModelFormulaire y WHERE y.author = :author")})
public class YvsParamModelFormulaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_param_model_formulaire_id_seq", name = "yvs_param_model_formulaire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_param_model_formulaire_id_seq_name", strategy = GenerationType.SEQUENCE) 
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
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "formulaire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsParamFormulaire formulaire;
    @OneToMany(mappedBy = "modele")
    private List<YvsParamProprieteChamp> composants;
    @Transient
    private boolean select;

    public YvsParamModelFormulaire() {
        composants = new ArrayList<>();
    }

    public YvsParamModelFormulaire(Long id) {
        this();
        this.id = id;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsParamFormulaire getFormulaire() {
        return formulaire;
    }

    public void setFormulaire(YvsParamFormulaire formulaire) {
        this.formulaire = formulaire;
    }

    public List<YvsParamProprieteChamp> getComposants() {
        return composants;
    }

    public void setComposants(List<YvsParamProprieteChamp> composants) {
        this.composants = composants;
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
        if (!(object instanceof YvsParamModelFormulaire)) {
            return false;
        }
        YvsParamModelFormulaire other = (YvsParamModelFormulaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.formulaire.YvsParamModelFormulaire[ id=" + id + " ]";
    }

}

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
import javax.persistence.Transient;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_param_propriete_champ")
@NamedQueries({
    @NamedQuery(name = "YvsParamProprieteChamp.findAll", query = "SELECT y FROM YvsParamProprieteChamp y"),
    @NamedQuery(name = "YvsParamProprieteChamp.findById", query = "SELECT y FROM YvsParamProprieteChamp y WHERE y.id = :id"),
    @NamedQuery(name = "YvsParamProprieteChamp.findByOne", query = "SELECT y FROM YvsParamProprieteChamp y WHERE y.champ = :champ AND y.modele = :modele"),
    @NamedQuery(name = "YvsParamProprieteChamp.findByChamp", query = "SELECT y FROM YvsParamProprieteChamp y WHERE y.champ = :champ"),
    @NamedQuery(name = "YvsParamProprieteChamp.findByModele", query = "SELECT y FROM YvsParamProprieteChamp y WHERE y.modele = :modele"),
    @NamedQuery(name = "YvsParamProprieteChamp.findByVisible", query = "SELECT y FROM YvsParamProprieteChamp y WHERE y.visible = :visible"),
    @NamedQuery(name = "YvsParamProprieteChamp.findByObligatoire", query = "SELECT y FROM YvsParamProprieteChamp y WHERE y.obligatoire = :obligatoire"),
    @NamedQuery(name = "YvsParamProprieteChamp.findByEditable", query = "SELECT y FROM YvsParamProprieteChamp y WHERE y.editable = :editable")})
public class YvsParamProprieteChamp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_param_propriete_champ_id_seq", name = "yvs_param_propriete_champ_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_param_propriete_champ_id_seq_name", strategy = GenerationType.SEQUENCE) 
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "visible")
    private Boolean visible;
    @Column(name = "obligatoire")
    private Boolean obligatoire;
    @Column(name = "editable")
    private Boolean editable;
    @JoinColumn(name = "champ", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsParamChampFormulaire champ;
    @JoinColumn(name = "modele", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsParamModelFormulaire modele;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean select;

    public YvsParamProprieteChamp() {
    }

    public YvsParamProprieteChamp(Long id) {
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

    public Boolean getVisible() {
        return visible != null ? visible : false;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getObligatoire() {
        return obligatoire != null ? obligatoire : false;
    }

    public void setObligatoire(Boolean obligatoire) {
        this.obligatoire = obligatoire;
    }

    public Boolean getEditable() {
        return editable != null ? editable : false;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public YvsParamChampFormulaire getChamp() {
        return champ;
    }

    public void setChamp(YvsParamChampFormulaire champ) {
        this.champ = champ;
    }

    public YvsParamModelFormulaire getModele() {
        return modele;
    }

    public void setModele(YvsParamModelFormulaire modele) {
        this.modele = modele;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

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
        if (!(object instanceof YvsParamProprieteChamp)) {
            return false;
        }
        YvsParamProprieteChamp other = (YvsParamProprieteChamp) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "yvs.entity.param.formulaire.YvsParamProprieteChamp[ id=" + id + " ]";
    }

}

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
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_param_champ_formulaire")
@NamedQueries({
    @NamedQuery(name = "YvsParamChampFormulaire.findAll", query = "SELECT y FROM YvsParamChampFormulaire y"),
    @NamedQuery(name = "YvsParamChampFormulaire.findById", query = "SELECT y FROM YvsParamChampFormulaire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsParamChampFormulaire.findByChamp", query = "SELECT y FROM YvsParamChampFormulaire y WHERE y.champ = :champ"),
    @NamedQuery(name = "YvsParamChampFormulaire.findByCode", query = "SELECT y FROM YvsParamChampFormulaire y WHERE y.code = :code"),
    @NamedQuery(name = "YvsParamChampFormulaire.findByForm", query = "SELECT y FROM YvsParamChampFormulaire y WHERE y.formulaire=:formulaire")})
public class YvsParamChampFormulaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_param_champ_formulaire_id_seq", name = "yvs_param_champ_formulaire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_param_champ_formulaire_id_seq_name", strategy = GenerationType.SEQUENCE)    
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
    @Column(name = "code")
    private String code;
    @Size(max = 2147483647)
    @Column(name = "champ")
    private String champ;
    @JoinColumn(name = "formulaire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsParamFormulaire formulaire;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean select;

    public YvsParamChampFormulaire() {
    }

    public YvsParamChampFormulaire(Long id) {
        this.id = id;
    }

    public YvsParamChampFormulaire(Long id, String code, String champ) {
        this.id = id;
        this.code = code;
        this.champ = champ;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public String getChamp() {
        return champ;
    }

    public void setChamp(String champ) {
        this.champ = champ;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public YvsParamFormulaire getFormulaire() {
        return formulaire;
    }

    public void setFormulaire(YvsParamFormulaire formulaire) {
        this.formulaire = formulaire;
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
        if (!(object instanceof YvsParamChampFormulaire)) {
            return false;
        }
        YvsParamChampFormulaire other = (YvsParamChampFormulaire) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "yvs.entity.param.formulaire.YvsParamChampFormulaire[ id=" + id + " ]";
    }

}

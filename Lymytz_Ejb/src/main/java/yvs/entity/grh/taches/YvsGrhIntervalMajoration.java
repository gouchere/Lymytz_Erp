/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.taches;

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
import yvs.entity.grh.param.YvsGrhMajorationConge;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_interval_majoration")
@NamedQueries({
    @NamedQuery(name = "YvsIntervalMajoration.findAll", query = "SELECT y FROM YvsGrhIntervalMajoration y"),
    @NamedQuery(name = "YvsIntervalMajoration.findById", query = "SELECT y FROM YvsGrhIntervalMajoration y WHERE y.id = :id"),
    @NamedQuery(name = "YvsIntervalMajoration.findByValeurMinimal", query = "SELECT y FROM YvsGrhIntervalMajoration y WHERE y.valeurMinimal = :valeurMinimal"),
    @NamedQuery(name = "YvsIntervalMajoration.findByValeurMaximal", query = "SELECT y FROM YvsGrhIntervalMajoration y WHERE y.valeurMaximal = :valeurMaximal"),
    @NamedQuery(name = "YvsIntervalMajoration.findByNbJour", query = "SELECT y FROM YvsGrhIntervalMajoration y WHERE y.nbJour = :nbJour"),
    @NamedQuery(name = "YvsIntervalMajoration.findByActif", query = "SELECT y FROM YvsGrhIntervalMajoration y WHERE y.actif = :actif")})
public class YvsGrhIntervalMajoration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_interval_majoration_id_seq", name = "yvs_interval_majoration_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_interval_majoration_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "valeur_minimal")
    private Integer valeurMinimal;
    @Column(name = "valeur_maximal")
    private Integer valeurMaximal;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "nb_jour")
    private Double nbJour;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "majoration_conge", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhMajorationConge majorationConge;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhIntervalMajoration() {
    }

    public YvsGrhIntervalMajoration(Long id) {
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValeurMinimal() {
        return valeurMinimal;
    }

    public void setValeurMinimal(Integer valeurMinimal) {
        this.valeurMinimal = valeurMinimal;
    }

    public Integer getValeurMaximal() {
        return valeurMaximal;
    }

    public void setValeurMaximal(Integer valeurMaximal) {
        this.valeurMaximal = valeurMaximal;
    }

    public Double getNbJour() {
        return nbJour != null ? nbJour : 0;
    }

    public void setNbJour(Double nbJour) {
        this.nbJour = nbJour;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsGrhMajorationConge getMajorationConge() {
        return majorationConge;
    }

    public void setMajorationConge(YvsGrhMajorationConge majorationConge) {
        this.majorationConge = majorationConge;
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
        if (!(object instanceof YvsGrhIntervalMajoration)) {
            return false;
        }
        YvsGrhIntervalMajoration other = (YvsGrhIntervalMajoration) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsIntervalMajoration[ id=" + id + " ]";
    }

}

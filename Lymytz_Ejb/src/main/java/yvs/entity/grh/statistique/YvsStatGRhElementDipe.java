/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.statistique;

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
import javax.validation.constraints.Size;
import yvs.entity.grh.salaire.YvsGrhElementSalaire;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_stat_grh_element_dipe")
@NamedQueries({
    @NamedQuery(name = "YvsStatRhElementDipe.findAll", query = "SELECT y FROM YvsStatGRhElementDipe y ORDER BY y.etat.code ASC"),
    @NamedQuery(name = "YvsStatRhElementDipe.findById", query = "SELECT y FROM YvsStatGRhElementDipe y WHERE y.id = :id"),
    @NamedQuery(name = "YvsStatRhElementDipe.findByLibelle", query = "SELECT y FROM YvsStatGRhElementDipe y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsStatRhElementDipe.findByDipe", query = "SELECT y FROM YvsStatGRhElementDipe y WHERE y.etat = :etat")})
public class YvsStatGRhElementDipe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_stat_rh_element_rv_tresor_id_seq", name = "yvs_stat_rh_element_rv_tresor_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_stat_rh_element_rv_tresor_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "by_formulaire")
    private Boolean byFormulaire;
    @JoinColumn(name = "element_salaire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhElementSalaire elementSalaire;
    @JoinColumn(name = "etat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsStatGrhEtat etat;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "groupe_element", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsStatGrhGroupeElement groupeElement;

    @Column(name = "ordre")
    private Integer ordre;

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsStatGRhElementDipe() {
    }

    public YvsStatGRhElementDipe(Long id) {
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

    public Boolean getByFormulaire() {
        return byFormulaire != null ? byFormulaire : false;
    }

    public void setByFormulaire(Boolean byFormulaire) {
        this.byFormulaire = byFormulaire;
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

    public YvsGrhElementSalaire getElementSalaire() {
        return elementSalaire;
    }

    public void setElementSalaire(YvsGrhElementSalaire elementSalaire) {
        this.elementSalaire = elementSalaire;
    }

    public YvsStatGrhEtat getEtat() {
        return etat;
    }

    public void setEtat(YvsStatGrhEtat etat) {
        this.etat = etat;
    }

    public YvsStatGrhGroupeElement getGroupeElement() {
        return groupeElement;
    }

    public void setGroupeElement(YvsStatGrhGroupeElement groupeElement) {
        this.groupeElement = groupeElement;
    }

    public Integer getOrdre() {
        return ordre != null ? ordre : 0;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
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
        if (!(object instanceof YvsStatGRhElementDipe)) {
            return false;
        }
        YvsStatGRhElementDipe other = (YvsStatGRhElementDipe) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsStatRhElementDipe[ id=" + id + " ]";
    }

}

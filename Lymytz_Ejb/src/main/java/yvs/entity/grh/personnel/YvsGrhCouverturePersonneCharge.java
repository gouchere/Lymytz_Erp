/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

import java.io.Serializable;
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

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_couverture_personne_charge")
@NamedQueries({
    @NamedQuery(name = "YvsGrhCouverturePersonneCharge.findAll", query = "SELECT y FROM YvsGrhCouverturePersonneCharge y"),
    @NamedQuery(name = "YvsGrhCouverturePersonneCharge.findById", query = "SELECT y FROM YvsGrhCouverturePersonneCharge y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhCouverturePersonneCharge.findByAssurance", query = "SELECT y FROM YvsGrhCouverturePersonneCharge y WHERE y.assurance = :assurance"),
    @NamedQuery(name = "YvsGrhCouverturePersonneCharge.findByTauxCouverture", query = "SELECT y FROM YvsGrhCouverturePersonneCharge y WHERE y.tauxCouverture = :tauxCouverture")})
public class YvsGrhCouverturePersonneCharge implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_couverture_personne_charge_id_seq", name = "yvs_grh_couverture_personne_charge_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_couverture_personne_charge_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux_couverture")
    private Double tauxCouverture;
    @JoinColumn(name = "personne", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPersonneEnCharge personne;
    @JoinColumn(name = "assurance", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhAssurance assurance;
    @OneToMany(mappedBy = "couverturePersonne")
    private List<YvsGrhBilanCouverture> yvsGrhBilanCouvertureList;

    public YvsGrhCouverturePersonneCharge() {
    }

    public YvsGrhCouverturePersonneCharge(Integer id) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTauxCouverture() {
        return tauxCouverture;
    }

    public void setTauxCouverture(Double tauxCouverture) {
        this.tauxCouverture = tauxCouverture;
    }

    public YvsGrhPersonneEnCharge getPersonne() {
        return personne;
    }

    public void setPersonne(YvsGrhPersonneEnCharge personne) {
        this.personne = personne;
    }

    public YvsGrhAssurance getAssurance() {
        return assurance;
    }

    public void setAssurance(YvsGrhAssurance assurance) {
        this.assurance = assurance;
    }

    public List<YvsGrhBilanCouverture> getYvsGrhBilanCouvertureList() {
        return yvsGrhBilanCouvertureList;
    }

    public void setYvsGrhBilanCouvertureList(List<YvsGrhBilanCouverture> yvsGrhBilanCouvertureList) {
        this.yvsGrhBilanCouvertureList = yvsGrhBilanCouvertureList;
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
        if (!(object instanceof YvsGrhCouverturePersonneCharge)) {
            return false;
        }
        YvsGrhCouverturePersonneCharge other = (YvsGrhCouverturePersonneCharge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsGrhCouverturePersonneCharge[ id=" + id + " ]";
    }

}

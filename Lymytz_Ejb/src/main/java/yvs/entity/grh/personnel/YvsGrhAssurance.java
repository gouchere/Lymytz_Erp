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
import javax.validation.constraints.Size;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_assurance")
@NamedQueries({
    @NamedQuery(name = "YvsGrhAssurance.findAll", query = "SELECT y FROM YvsGrhAssurance y"),
    @NamedQuery(name = "YvsGrhAssurance.findById", query = "SELECT y FROM YvsGrhAssurance y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhAssurance.findByEmploye", query = "SELECT y FROM YvsGrhAssurance y WHERE y.employe = :employe"),
    @NamedQuery(name = "YvsGrhAssurance.findByReference", query = "SELECT y FROM YvsGrhAssurance y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsGrhAssurance.findByTauxCotisation", query = "SELECT y FROM YvsGrhAssurance y WHERE y.tauxCotisation = :tauxCotisation"),
    @NamedQuery(name = "YvsGrhAssurance.findByTauxCouverture", query = "SELECT y FROM YvsGrhAssurance y WHERE y.tauxCouverture = :tauxCouverture")})
public class YvsGrhAssurance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_assurance_id_seq", name = "yvs_grh_assurance_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_assurance_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "reference")
    private String reference;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux_cotisation")
    private Double tauxCotisation;
    @Column(name = "taux_couverture")
    private Double tauxCouverture;
    @OneToMany(mappedBy = "assurance")
    private List<YvsGrhBilanAssurance> yvsGrhBilanAssuranceList;
    @JoinColumn(name = "type_assurance", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeAssurance typeAssurance;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @OneToMany(mappedBy = "assurance")
    private List<YvsGrhCouverturePersonneCharge> yvsGrhCouverturePersonneChargeList;

    public YvsGrhAssurance() {
    }

    public YvsGrhAssurance(Integer id) {
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Double getTauxCotisation() {
        return tauxCotisation;
    }

    public void setTauxCotisation(Double tauxCotisation) {
        this.tauxCotisation = tauxCotisation;
    }

    public Double getTauxCouverture() {
        return tauxCouverture;
    }

    public void setTauxCouverture(Double tauxCouverture) {
        this.tauxCouverture = tauxCouverture;
    }

    public List<YvsGrhBilanAssurance> getYvsGrhBilanAssuranceList() {
        return yvsGrhBilanAssuranceList;
    }

    public void setYvsGrhBilanAssuranceList(List<YvsGrhBilanAssurance> yvsGrhBilanAssuranceList) {
        this.yvsGrhBilanAssuranceList = yvsGrhBilanAssuranceList;
    }

    public YvsGrhTypeAssurance getTypeAssurance() {
        return typeAssurance;
    }

    public void setTypeAssurance(YvsGrhTypeAssurance typeAssurance) {
        this.typeAssurance = typeAssurance;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public List<YvsGrhCouverturePersonneCharge> getYvsGrhCouverturePersonneChargeList() {
        return yvsGrhCouverturePersonneChargeList;
    }

    public void setYvsGrhCouverturePersonneChargeList(List<YvsGrhCouverturePersonneCharge> yvsGrhCouverturePersonneChargeList) {
        this.yvsGrhCouverturePersonneChargeList = yvsGrhCouverturePersonneChargeList;
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
        if (!(object instanceof YvsGrhAssurance)) {
            return false;
        }
        YvsGrhAssurance other = (YvsGrhAssurance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsGrhAssurance[ id=" + id + " ]";
    }

}

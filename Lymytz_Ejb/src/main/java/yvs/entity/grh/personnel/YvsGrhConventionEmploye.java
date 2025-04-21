/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

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
import yvs.entity.grh.param.YvsGrhConventionCollective;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_convention_employe")
@NamedQueries({
    @NamedQuery(name = "YvsConventionEmploye.findAll", query = "SELECT y FROM YvsGrhConventionEmploye y"),
    @NamedQuery(name = "YvsConventionEmploye.findById", query = "SELECT y FROM YvsGrhConventionEmploye y WHERE y.id = :id"),
    @NamedQuery(name = "YvsConventionEmploye.findOneCByEmploye", query = "SELECT y.convention FROM YvsGrhConventionEmploye y JOIN y.convention C JOIN FETCH C.categorie JOIN FETCH C.echelon  WHERE y.employe = :employe AND y.actif=true ORDER BY y.dateChange DESC"),
    @NamedQuery(name = "YvsConventionEmploye.findByEmps", query = "SELECT y FROM YvsGrhConventionEmploye y WHERE y.employe = :employe ORDER BY y.dateChange DESC"),
    @NamedQuery(name = "YvsConventionEmploye.findByEmploye", query = "SELECT y FROM YvsGrhConventionEmploye y WHERE y.employe = :employe AND y.actif=true ORDER BY y.dateChange DESC"),
    @NamedQuery(name = "YvsConventionEmploye.findByEmployeDates", query = "SELECT y FROM YvsGrhConventionEmploye y WHERE y.employe = :employe AND y.dateChange BETWEEN :dateDebut AND :dateFin ORDER BY y.dateChange DESC"),
    @NamedQuery(name = "YvsConventionEmploye.findByDateChange", query = "SELECT y FROM YvsGrhConventionEmploye y WHERE y.dateChange = :dateChange"),
    @NamedQuery(name = "YvsConventionEmploye.findBySupp", query = "SELECT y FROM YvsGrhConventionEmploye y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsConventionEmploye.findCatConvActif", query = "SELECT y.convention.categorie FROM YvsGrhConventionEmploye y WHERE y.employe = :employe AND y.actif=true"),
    @NamedQuery(name = "YvsConventionEmploye.findByActif", query = "SELECT y FROM YvsGrhConventionEmploye y WHERE y.actif = :actif AND y.employe=:employe"),
    @NamedQuery(name = "YvsConventionEmploye.findCurrentByEmploye", query = "SELECT y FROM YvsGrhConventionEmploye y WHERE y.employe = :employe AND y.actif = true")})
public class YvsGrhConventionEmploye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_convention_employe_id_seq", name = "yvs_convention_employe_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_convention_employe_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_change")
    @Temporal(TemporalType.DATE)
    private Date dateChange;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    
    @JoinColumn(name = "convention", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhConventionCollective convention;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsGrhConventionEmploye() {
    }

    public YvsGrhConventionEmploye(Long id) {
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

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
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

    public Date getDateChange() {
        return dateChange;
    }

    public void setDateChange(Date dateChange) {
        this.dateChange = dateChange;
    }

    public Boolean getSupp() {
        return supp != null ? supp : false;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsGrhConventionCollective getConvention() {
        return convention;
    }

    public void setConvention(YvsGrhConventionCollective convention) {
        this.convention = convention;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
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
        if (!(object instanceof YvsGrhConventionEmploye)) {
            return false;
        }
        YvsGrhConventionEmploye other = (YvsGrhConventionEmploye) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsConventionEmploye[ id=" + id + " ]";
    }

}

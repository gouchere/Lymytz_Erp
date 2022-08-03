/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.analytique;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import yvs.entity.compta.YvsComptaCoutSupPieceVirement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_centre_cout_virement")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCentreCoutVirement.findAll", query = "SELECT y FROM YvsComptaCentreCoutVirement y"),
    @NamedQuery(name = "YvsComptaCentreCoutVirement.findById", query = "SELECT y FROM YvsComptaCentreCoutVirement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCentreCoutVirement.findByCoefficient", query = "SELECT y FROM YvsComptaCentreCoutVirement y WHERE y.coefficient = :coefficient"),
    @NamedQuery(name = "YvsComptaCentreCoutVirement.findByDateUpdate", query = "SELECT y FROM YvsComptaCentreCoutVirement y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaCentreCoutVirement.findByDateSave", query = "SELECT y FROM YvsComptaCentreCoutVirement y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaCentreCoutVirement.findByCout", query = "SELECT y FROM YvsComptaCentreCoutVirement y WHERE y.cout = :cout"),
    @NamedQuery(name = "YvsComptaCentreCoutVirement.findByCoutCentre", query = "SELECT y FROM YvsComptaCentreCoutVirement y WHERE y.cout = :cout AND y.centre = :centre")})
public class YvsComptaCentreCoutVirement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_centre_cout_virement_id_seq", name = "yvs_compta_centre_cout_virement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_centre_cout_virement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "coefficient")
    private Double coefficient;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "cout", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCoutSupPieceVirement cout;
    @JoinColumn(name = "centre", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCentreAnalytique centre;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsComptaCentreCoutVirement() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaCentreCoutVirement(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaCentreCoutVirement(YvsComptaCoutSupPieceVirement cout, YvsComptaCentreAnalytique centre) {
        this();
        this.cout = cout;
        this.centre = centre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCoefficient() {
        return coefficient != null ? coefficient : 0;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
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

    public YvsComptaCoutSupPieceVirement getCout() {
        return cout;
    }

    public void setCout(YvsComptaCoutSupPieceVirement cout) {
        this.cout = cout;
    }

    public YvsComptaCentreAnalytique getCentre() {
        return centre;
    }

    public void setCentre(YvsComptaCentreAnalytique centre) {
        this.centre = centre;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsComptaCentreCoutVirement)) {
            return false;
        }
        YvsComptaCentreCoutVirement other = (YvsComptaCentreCoutVirement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.analytique.YvsComptaCentreCoutVirement[ id=" + id + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.commission;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.commercial.objectifs.YvsComPeriodeObjectif;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_commission_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComCommissionVente.findAll", query = "SELECT y FROM YvsComCommissionVente y"),
    @NamedQuery(name = "YvsComCommissionVente.findById", query = "SELECT y FROM YvsComCommissionVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCommissionVente.findByMontant", query = "SELECT y FROM YvsComCommissionVente y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComCommissionVente.findByDateUpdate", query = "SELECT y FROM YvsComCommissionVente y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComCommissionVente.findByDateSave", query = "SELECT y FROM YvsComCommissionVente y WHERE y.dateSave = :dateSave"),

    @NamedQuery(name = "YvsComCommissionVente.findByPeriode", query = "SELECT y FROM YvsComCommissionVente y WHERE y.periode = :periode"),
    @NamedQuery(name = "YvsComCommissionVente.findSumOne", query = "SELECT SUM(y.montant) FROM YvsComCommissionVente y WHERE y.facture = :facture AND y.periode = :periode"),
    @NamedQuery(name = "YvsComCommissionVente.findOne", query = "SELECT y FROM YvsComCommissionVente y WHERE y.facture = :facture AND y.periode = :periode"),
    
    @NamedQuery(name = "YvsComCommissionVente.findIdFactureByPeriode", query = "SELECT DISTINCT y.facture.id FROM YvsComCommissionVente y WHERE y.periode = :periode")})
public class YvsComCommissionVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_commission_vente_id_seq", name = "yvs_com_commission_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_commission_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "periode", referencedColumnName = "id")
    @ManyToOne
    private YvsComPeriodeObjectif periode;
    @JsonManagedReference
    @JoinColumn(name = "facture", referencedColumnName = "id")
    @ManyToOne
    private YvsComDocVentes facture;

    public YvsComCommissionVente() {
    }

    public YvsComCommissionVente(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComPeriodeObjectif getPeriode() {
        return periode;
    }

    public void setPeriode(YvsComPeriodeObjectif periode) {
        this.periode = periode;
    }

    public YvsComDocVentes getFacture() {
        return facture;
    }

    public void setFacture(YvsComDocVentes facture) {
        this.facture = facture;
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
        if (!(object instanceof YvsComCommissionVente)) {
            return false;
        }
        YvsComCommissionVente other = (YvsComCommissionVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.commission.YvsComCommissionVente[ id=" + id + " ]";
    }

}

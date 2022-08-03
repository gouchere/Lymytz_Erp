/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.contrat.retenue;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.commercial.stock.YvsComReglementEcartStock;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_retenue_ecart_stock")
@NamedQueries({
    @NamedQuery(name = "YvsGrhRetenueEcartStock.findAll", query = "SELECT y FROM YvsGrhRetenueEcartStock y WHERE y.retenue=:retenue"),
    @NamedQuery(name = "YvsGrhRetenueEcartStock.findById", query = "SELECT y FROM YvsGrhRetenueEcartStock y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhRetenueEcartStock.findByEcart", query = "SELECT y FROM YvsGrhRetenueEcartStock y WHERE y.ecart = :ecart")
})
public class YvsGrhRetenueEcartStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_retenue_ecart_stock_id_seq", name = "yvs_grh_retenue_ecart_stock_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_retenue_ecart_stock_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "ecart", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComReglementEcartStock ecart;
    @JoinColumn(name = "retenue", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhElementAdditionel retenue;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhRetenueEcartStock() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsGrhRetenueEcartStock(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public YvsGrhElementAdditionel getRetenue() {
        return retenue;
    }

    public void setRetenue(YvsGrhElementAdditionel retenue) {
        this.retenue = retenue;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComReglementEcartStock getEcart() {
        return ecart;
    }

    public void setEcart(YvsComReglementEcartStock ecart) {
        this.ecart = ecart;
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
        if (!(object instanceof YvsGrhRetenueEcartStock)) {
            return false;
        }
        YvsGrhRetenueEcartStock other = (YvsGrhRetenueEcartStock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsGrhRetenueEcartStock[ id=" + id + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.stock;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
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
import yvs.dao.Util;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_com_doc_stocks_valeur")
@NamedQueries({
    @NamedQuery(name = "YvsComDocStocksValeur.findAll", query = "SELECT y FROM YvsComDocStocksValeur y WHERE y.docStock=:docStock"),
    @NamedQuery(name = "YvsComDocStocksValeur.findById", query = "SELECT y FROM YvsComDocStocksValeur y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComDocStocksValeur.findByDateSave", query = "SELECT y FROM YvsComDocStocksValeur y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComDocStocksValeur.findByDateUpdate", query = "SELECT y FROM YvsComDocStocksValeur y WHERE y.dateUpdate = :dateUpdate")
})
public class YvsComDocStocksValeur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_doc_stocks_valeur_id_seq", name = "yvs_com_doc_stocks_valeur_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_doc_stocks_valeur_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "montant")
    private Double montant;
    @Column(name = "coefficient")
    private Double coefficient = 1D;
    @Column(name = "valorise_mp_by")
    private String valoriseMpBy = "A";
    @Column(name = "valorise_pf_by")
    private String valorisePfBy = "V";
    @Column(name = "valorise_pfs_by")
    private String valorisePfsBy = "R";
    @Column(name = "valorise_ms_by")
    private String valoriseMsBy = "V";
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "doc_stock", referencedColumnName = "id")
    @ManyToOne
    private YvsComDocStocks docStock;

    public static long ids = -1;

    public YvsComDocStocksValeur() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        this.valoriseMpBy = "A";
        this.valorisePfBy = "V";
        this.valorisePfsBy = "R";
        this.valoriseMsBy = "V";
        this.coefficient = 1D;
    }

    public YvsComDocStocksValeur(Long id) {
        this();
        this.id = id;
    }

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
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
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

    public Double getCoefficient() {
        return coefficient != null ? coefficient : 1;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public String getValoriseMpBy() {
        return Util.asString(valoriseMpBy) ? valoriseMpBy : "A";
    }

    public void setValoriseMpBy(String valoriseMpBy) {
        this.valoriseMpBy = valoriseMpBy;
    }

    public String getValorisePfBy() {
        return Util.asString(valorisePfBy) ? valorisePfBy : "V";
    }

    public void setValorisePfBy(String valorisePfBy) {
        this.valorisePfBy = valorisePfBy;
    }

    public String getValorisePfsBy() {
        return Util.asString(valorisePfsBy) ? valorisePfsBy : "R";
    }

    public void setValorisePfsBy(String valorisePfsBy) {
        this.valorisePfsBy = valorisePfsBy;
    }

    public String getValoriseMsBy() {
        return Util.asString(valoriseMsBy) ? valoriseMsBy : "V";
    }

    public void setValoriseMsBy(String valoriseMsBy) {
        this.valoriseMsBy = valoriseMsBy;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComDocStocks getDocStock() {
        return docStock;
    }

    public void setDocStock(YvsComDocStocks docStock) {
        this.docStock = docStock;
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
        if (!(object instanceof YvsComDocStocksValeur)) {
            return false;
        }
        YvsComDocStocksValeur other = (YvsComDocStocksValeur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.stock.YvsComDocStocksValeur[ id=" + id + " ]";
    }

}

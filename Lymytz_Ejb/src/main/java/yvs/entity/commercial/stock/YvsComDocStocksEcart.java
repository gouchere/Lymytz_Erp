/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.stock;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_com_doc_stocks_ecart")
@NamedQueries({
    @NamedQuery(name = "YvsComDocStocksEcart.findAll", query = "SELECT y FROM YvsComDocStocksEcart y WHERE y.docStock=:docStock"),
    @NamedQuery(name = "YvsComDocStocksEcart.findById", query = "SELECT y FROM YvsComDocStocksEcart y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComDocStocksEcart.findByIds", query = "SELECT y FROM YvsComDocStocksEcart y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsComDocStocksEcart.findByDateSave", query = "SELECT y FROM YvsComDocStocksEcart y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComDocStocksEcart.findByDateUpdate", query = "SELECT y FROM YvsComDocStocksEcart y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComDocStocksEcart.sumByStockNotId", query = "SELECT SUM(y.taux) FROM YvsComDocStocksEcart y WHERE y.docStock = :docStock AND y.id != :id"),
    @NamedQuery(name = "YvsComDocStocksEcart.sumByStock", query = "SELECT SUM(y.taux) FROM YvsComDocStocksEcart y WHERE y.docStock = :docStock"),
    @NamedQuery(name = "YvsComDocStocksEcart.findByNoNumero", query = "SELECT y FROM YvsComDocStocksEcart y WHERE y.tiers.societe = :societe AND COALESCE(y.numero, '') = ''"),
    @NamedQuery(name = "YvsComDocStocksEcart.findLikeNumero", query = "SELECT y FROM YvsComDocStocksEcart y WHERE y.numero like :numero AND y.tiers.societe = :societe ORDER BY y.numero DESC")
})
public class YvsComDocStocksEcart implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_doc_stocks_ecart_id_seq", name = "yvs_com_doc_stocks_ecart_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_doc_stocks_ecart_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "numero")
    private String numero;
    @Column(name = "statut")
    private Character statut = 'E';
    @Column(name = "statut_regle")
    private Character statutRegle = 'W';
    @Column(name = "taux")
    private Double taux;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "doc_stock", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocStocks docStock;
    @JoinColumn(name = "tiers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTiers tiers;

    @OneToMany(mappedBy = "piece",fetch = FetchType.LAZY)
    private List<YvsComReglementEcartStock> reglements;

    @Transient
    private boolean new_;
    @Transient
    public static long ids = -1;

    public YvsComDocStocksEcart() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        this.reglements = new ArrayList<>();
    }

    public YvsComDocStocksEcart(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Character getStatut() {
        return statut != null ? statut : 'E';
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public Character getStatutRegle() {
        return statutRegle != null ? statutRegle : 'W';
    }

    public void setStatutRegle(Character statutRegle) {
        this.statutRegle = statutRegle;
    }

    public Double getTaux() {
        return taux != null ? taux : 0;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
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

    public YvsBaseTiers getTiers() {
        return tiers;
    }

    public void setTiers(YvsBaseTiers tiers) {
        this.tiers = tiers;
    }

    public List<YvsComReglementEcartStock> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComReglementEcartStock> reglements) {
        this.reglements = reglements;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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
        if (!(object instanceof YvsComDocStocksEcart)) {
            return false;
        }
        YvsComDocStocksEcart other = (YvsComDocStocksEcart) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.stock.YvsComDocStocksEcart[ id=" + id + " ]";
    }

    public double haveRetenue() {
        try {
            double retenue = 0;
            for (YvsComReglementEcartStock r : getReglements()) {
                if (r.getRetenue() != null ? r.getRetenue().getId() > 0 : false) {
                    retenue += r.getMontant();
                }
            }
            return retenue;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public double canBuildRetenue(double total) {
        try {
            double montant = (getTaux() * total) / 100;
            double retenue = 0;
            for (YvsComReglementEcartStock r : getReglements()) {
                if (r.getRetenue() != null ? r.getRetenue().getId() > 0 : false) {
                    retenue += r.getMontant();
                }
            }
            return montant - retenue;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public double canBuildReglement(double total) {
        try {
            double montant = (getTaux() * total) / 100;
            double retenue = 0;
            for (YvsComReglementEcartStock r : getReglements()) {
                retenue += r.getMontant();
            }
            return montant - retenue;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}

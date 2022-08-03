/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.achat;

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
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_lot_reception")
@NamedQueries({
    @NamedQuery(name = "YvsComLotReception.findAll", query = "SELECT y FROM YvsComLotReception y WHERE y.agence.societe = :societe"),
    @NamedQuery(name = "YvsComLotReception.findByAgence", query = "SELECT y FROM YvsComLotReception y WHERE y.agence = :agence"),
    @NamedQuery(name = "YvsComLotReception.findById", query = "SELECT y FROM YvsComLotReception y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComLotReception.findByNumero", query = "SELECT y FROM YvsComLotReception y WHERE y.agence.societe = :societe AND y.numero = :numero"),
    @NamedQuery(name = "YvsComLotReception.findByNumeroArticle", query = "SELECT y FROM YvsComLotReception y WHERE y.article = :article AND y.numero = :numero"),
    @NamedQuery(name = "YvsComLotReception.findByNotId", query = "SELECT y FROM YvsComLotReception y WHERE y.agence.societe = :societe AND y.id != :id"),
    @NamedQuery(name = "YvsComLotReception.findByDateFabrication", query = "SELECT y FROM YvsComLotReception y WHERE y.dateFabrication = :dateFabrication"),
    @NamedQuery(name = "YvsComLotReception.findByDateExpiration", query = "SELECT y FROM YvsComLotReception y WHERE y.dateExpiration = :dateExpiration"),
    @NamedQuery(name = "YvsComLotReception.findByStatut", query = "SELECT y FROM YvsComLotReception y WHERE y.statut = :statut"),
    @NamedQuery(name = "YvsComLotReception.findByActif", query = "SELECT y FROM YvsComLotReception y WHERE y.actif = :actif")})
public class YvsComLotReception extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_lot_reception_id_seq", name = "yvs_com_lot_reception_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_lot_reception_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numero")
    private String numero;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_fabrication")
    @Temporal(TemporalType.DATE)
    private Date dateFabrication;
    @Column(name = "date_expiration")
    @Temporal(TemporalType.DATE)
    private Date dateExpiration;
    @Column(name = "statut")
    private String statut;
    @Column(name = "actif")
    private Boolean actif;
    
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private boolean new_;
    @Transient
    private double stock;
    @Transient
    private double quantitee;
    @Transient
    private boolean selectActif;

    public YvsComLotReception() {
    }

    public YvsComLotReception(Long id) {
        this();
        this.id = id;
    }

    public YvsComLotReception(Long id, String numero) {
        this(id);
        this.numero = numero;
    }

    public YvsComLotReception(Long id, String numero, Date dateFabrication, Date dateExpiration, Double stock) {
        this(id, numero);
        this.dateFabrication = dateFabrication;
        this.dateExpiration = dateExpiration;
        this.stock = stock != null ? stock : 0;
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

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateFabrication() {
        return dateFabrication;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateFabrication(Date dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    public String getStatut() {
        return statut != null ? statut.trim().length() > 0 ? statut : "V" : "V";
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateExpiration() {
        return dateExpiration;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getQuantitee() {
        return quantitee;
    }

    public void setQuantitee(double quantitee) {
        this.quantitee = quantitee;
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
        if (!(object instanceof YvsComLotReception)) {
            return false;
        }
        YvsComLotReception other = (YvsComLotReception) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.achat.YvsComLotReception[ id=" + id + " ]";
    }

}

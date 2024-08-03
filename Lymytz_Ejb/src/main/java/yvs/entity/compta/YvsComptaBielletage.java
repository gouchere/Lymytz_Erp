/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_bielletage")
@NamedQueries({
    @NamedQuery(name = "YvsComptaBielletage.findAll", query = "SELECT y FROM YvsComptaBielletage y"),
    @NamedQuery(name = "YvsComptaBielletage.findById", query = "SELECT y FROM YvsComptaBielletage y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaBielletage.findByFormatMonai", query = "SELECT y FROM YvsComptaBielletage y WHERE y.formatMonai = :formatMonai"),
    @NamedQuery(name = "YvsComptaBielletage.findByValeur", query = "SELECT y FROM YvsComptaBielletage y WHERE y.valeur = :valeur"),
    @NamedQuery(name = "YvsComptaBielletage.findByQuantite", query = "SELECT y FROM YvsComptaBielletage y WHERE y.quantite = :quantite")})
public class YvsComptaBielletage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_bielletage_id_seq", name = "yvs_compta_bielletage_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_bielletage_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "format_monai")
    private String formatMonai;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valeur")
    private Double valeur;
    @Column(name = "quantite")
    private Integer quantite;
    @Column(name = "billet")
    private Boolean billet;
    @JoinColumn(name = "piece_virement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceVirement pieceVirement;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsComptaBielletage() {
    }

    public YvsComptaBielletage(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getBillet() {
        return billet != null ? billet : false;
    }

    public void setBillet(Boolean billet) {
        this.billet = billet;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getFormatMonai() {
        return formatMonai;
    }

    public void setFormatMonai(String formatMonai) {
        this.formatMonai = formatMonai;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public YvsComptaCaissePieceVirement getPieceVirement() {
        return pieceVirement;
    }

    public void setPieceVirement(YvsComptaCaissePieceVirement pieceVirement) {
        this.pieceVirement = pieceVirement;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsComptaBielletage)) {
            return false;
        }
        YvsComptaBielletage other = (YvsComptaBielletage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaBielletage[ id=" + id + " ]";
    }

}

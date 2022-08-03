/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.vente;

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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Transient;
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_taxe_contenu_vente", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsComTaxeContenuVente.findAll", query = "SELECT y FROM YvsComTaxeContenuVente y"),
    @NamedQuery(name = "YvsComTaxeContenuVente.findById", query = "SELECT y FROM YvsComTaxeContenuVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComTaxeContenuVente.findByMontant", query = "SELECT y FROM YvsComTaxeContenuVente y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComTaxeContenuVente.findByContenu", query = "SELECT y FROM YvsComTaxeContenuVente y WHERE y.contenu = :contenu ORDER BY y.taxe.taux"),

    @NamedQuery(name = "YvsComContenuDocVente.findTaxeByArticle", query = "SELECT SUM(y.montant) FROM YvsComTaxeContenuVente y WHERE y.contenu.docVente = :docVente AND y.contenu.article = :article AND y.contenu.conditionnement = :unite"),
    @NamedQuery(name = "YvsComTaxeContenuVente.findOne", query = "SELECT y FROM YvsComTaxeContenuVente y WHERE y.contenu = :contenu AND y.taxe = :taxe")})
public class YvsComTaxeContenuVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_taxe_contenu_vente_id_seq", name = "yvs_com_taxe_contenu_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_taxe_contenu_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "contenu", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComContenuDocVente contenu;
    @JoinColumn(name = "taxe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTaxes taxe;

    @Transient
    private long idDistant;

    public long getIdDistant() {
        return idDistant;
    }

    public void setIdDistant(long idDistant) {
        this.idDistant = idDistant;
    }

    public YvsComTaxeContenuVente() {
    }

    public YvsComTaxeContenuVente(Long id) {
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

    public Long getId() {
        return id;
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

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComContenuDocVente getContenu() {
        return contenu;
    }

    public void setContenu(YvsComContenuDocVente contenu) {
        this.contenu = contenu;
    }

    public YvsBaseTaxes getTaxe() {
        return taxe;
    }

    public void setTaxe(YvsBaseTaxes taxe) {
        this.taxe = taxe;
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
        if (!(object instanceof YvsComTaxeContenuVente)) {
            return false;
        }
        YvsComTaxeContenuVente other = (YvsComTaxeContenuVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.vente.YvsComTaxeContenuVente[ id=" + id + " ]";
    }

}

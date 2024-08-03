/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.stock;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_reservation_stock")
@NamedQueries({
    @NamedQuery(name = "YvsComReservationStock.findAll", query = "SELECT y FROM YvsComReservationStock y WHERE y.depot.agence.societe = :societe ORDER BY y.dateEcheance DESC"),
    @NamedQuery(name = "YvsComReservationStock.countAll", query = "SELECT COUNT(y) FROM YvsComReservationStock y WHERE y.depot.agence.societe = :societe"),
    @NamedQuery(name = "YvsComReservationStock.findByAgence", query = "SELECT y FROM YvsComReservationStock y WHERE y.depot.agence = :agence ORDER BY y.dateEcheance DESC"),
    @NamedQuery(name = "YvsComReservationStock.findQuantiteReserve", query = "SELECT SUM(y.quantite) FROM YvsComReservationStock y WHERE y.depot = :depot AND y.article=:article AND y.dateEcheance<=:date AND y.statut=:statut"),
    @NamedQuery(name = "YvsComReservationStock.findQuantite", query = "SELECT SUM(y.quantite) FROM YvsComReservationStock y WHERE y.depot = :depot AND y.article=:article AND y.dateEcheance<=:date AND y.statut=:statut AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsComReservationStock.findById", query = "SELECT y FROM YvsComReservationStock y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComReservationStock.findByReference", query = "SELECT y FROM YvsComReservationStock y WHERE y.numReference LIKE :numero AND y.depot.agence.societe=:societe "),
    @NamedQuery(name = "YvsComReservationStock.findByDateReservation", query = "SELECT y FROM YvsComReservationStock y WHERE y.dateReservation = :dateReservation ORDER BY y.dateEcheance DESC"),
    @NamedQuery(name = "YvsComReservationStock.findByDateEcheance", query = "SELECT y FROM YvsComReservationStock y WHERE y.dateEcheance = :dateEcheance ORDER BY y.dateEcheance DESC"),
    @NamedQuery(name = "YvsComReservationStock.findByNumReference", query = "SELECT y FROM YvsComReservationStock y WHERE y.numReference = :reference ORDER BY y.dateEcheance DESC"),
    @NamedQuery(name = "YvsComReservationStock.findByNumExterne", query = "SELECT y FROM YvsComReservationStock y WHERE y.numExterne = :reference ORDER BY y.dateEcheance DESC"),
    @NamedQuery(name = "YvsComReservationStock.findByStatut", query = "SELECT y FROM YvsComReservationStock y WHERE y.statut = :statut ORDER BY y.dateEcheance DESC"),
    @NamedQuery(name = "YvsComReservationStock.findByActif", query = "SELECT y FROM YvsComReservationStock y WHERE y.actif = :actif ORDER BY y.dateEcheance DESC"),
    @NamedQuery(name = "YvsComReservationStock.findByAuthor", query = "SELECT y FROM YvsComReservationStock y WHERE y.author = :author ORDER BY y.dateEcheance DESC")})
public class YvsComReservationStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_reservation_stock_id_seq", name = "yvs_com_reservation_stock_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_reservation_stock_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_reservation")
    @Temporal(TemporalType.DATE)
    private Date dateReservation;
    @Column(name = "date_echeance")
    @Temporal(TemporalType.DATE)
    private Date dateEcheance;
    @Size(max = 2147483647)
    @Column(name = "num_reference")
    private String numReference;
    @Size(max = 2147483647)
    @Column(name = "num_externe")
    private String numExterne;
    @Size(max = 2147483647)
    @Column(name = "statut")
    private String statut;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "quantite")
    private double quantite;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @OneToOne(optional = false)
    private YvsUsersAgence author;
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depot;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    @Transient
    private boolean select;
    @Transient
    private boolean new_;

    public YvsComReservationStock() {
    }

    public YvsComReservationStock(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumExterne() {
        return numExterne != null ? numExterne : "";
    }

    public void setNumExterne(String numExterne) {
        this.numExterne = numExterne;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Date getDateReservation() {
        return dateReservation != null ? dateReservation : new Date();
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Date getDateEcheance() {
        return dateEcheance != null ? dateEcheance : new Date();
    }

    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public String getNumReference() {
        return numReference != null ? numReference : "";
    }

    public void setNumReference(String numReference) {
        this.numReference = numReference;
    }

    public String getStatut() {
        return statut != null ? (statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE) : Constantes.ETAT_EDITABLE;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
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
        if (!(object instanceof YvsComReservationStock)) {
            return false;
        }
        YvsComReservationStock other = (YvsComReservationStock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.stock.YvsComReservationStock[ id=" + id + " ]";
    }

    public boolean canEditable() {
        return statut.equals(Constantes.ETAT_ATTENTE) || statut.equals(Constantes.ETAT_EDITABLE);
    }

    public boolean canDelete() {
        return statut.equals(Constantes.ETAT_ATTENTE) || statut.equals(Constantes.ETAT_EDITABLE) || statut.equals(Constantes.ETAT_SUSPENDU) || statut.equals(Constantes.ETAT_ANNULE);
    }

}

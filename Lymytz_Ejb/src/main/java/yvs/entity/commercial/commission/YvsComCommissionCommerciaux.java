/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.commission;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.objectifs.YvsComPeriodeObjectif;
import yvs.entity.commercial.vente.YvsComCommercialVente;
import yvs.entity.compta.YvsComptaCaissePieceCommission;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_commission_commerciaux")
@NamedQueries({
    @NamedQuery(name = "YvsComCommissionCommerciaux.findAll", query = "SELECT y FROM YvsComCommissionCommerciaux y WHERE y.periode.societe = :societe "),
    @NamedQuery(name = "YvsComCommissionCommerciaux.findById", query = "SELECT y FROM YvsComCommissionCommerciaux y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCommissionCommerciaux.findByNumero", query = "SELECT y FROM YvsComCommissionCommerciaux y WHERE y.periode.societe = :societe AND y.numero LIKE :reference ORDER by y.numero DESC"),
    @NamedQuery(name = "YvsComCommissionCommerciaux.findByMontant", query = "SELECT y FROM YvsComCommissionCommerciaux y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComCommissionCommerciaux.findByDateUpdate", query = "SELECT y FROM YvsComCommissionCommerciaux y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComCommissionCommerciaux.findByDateSave", query = "SELECT y FROM YvsComCommissionCommerciaux y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComCommissionCommerciaux.findByPeriode", query = "SELECT y FROM YvsComCommissionCommerciaux y WHERE y.periode = :periode"),
    @NamedQuery(name = "YvsComCommissionCommerciaux.findSumOne", query = "SELECT SUM(y.montant) FROM YvsComCommissionCommerciaux y WHERE y.commerciaux = :commerciaux AND y.periode = :periode"),
    @NamedQuery(name = "YvsComCommissionCommerciaux.findOne", query = "SELECT y FROM YvsComCommissionCommerciaux y WHERE y.commerciaux = :commerciaux AND y.periode = :periode")})
public class YvsComCommissionCommerciaux extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_commission_commerciaux_id_seq", name = "yvs_com_commission_commerciaux_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_commission_commerciaux_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "numero")
    private String numero;
    @Column(name = "statut")
    private String statut;
    @Column(name = "statut_regle")
    private String statutRegle;
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
    @JoinColumn(name = "commerciaux", referencedColumnName = "id")
    @ManyToOne
    private YvsComComerciale commerciaux;

    @OneToMany(mappedBy = "commission", fetch = FetchType.LAZY)
    private List<YvsComptaCaissePieceCommission> reglements;
    @Transient
    private double chiffreAffaire = 0;
    @Transient
    private List<YvsComCommercialVente> factures, facturesSave;

    @Transient
    private boolean new_;

    public YvsComCommissionCommerciaux() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        this.reglements = new ArrayList<>();
        this.factures = new ArrayList<>();
        this.facturesSave = new ArrayList<>();
    }

    public YvsComCommissionCommerciaux(Long id) {
        this();
        this.id = id;
    }

    public YvsComCommissionCommerciaux(Long id, String numero) {
        this(id);
        this.numero = numero;
    }

    public YvsComCommissionCommerciaux(Long id, String numero, String statut) {
        this(id, numero);
        this.statut = statut;
    }

    @Override
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

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getStatut() {
        return statut != null ? !statut.trim().isEmpty() ? statut : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getStatutRegle() {
        return statutRegle != null ? !statutRegle.trim().isEmpty() ? statutRegle : Constantes.ETAT_ATTENTE : Constantes.ETAT_ATTENTE;
    }

    public void setStatutRegle(String statut) {
        this.statutRegle = statut;
    }

    public double getChiffreAffaire() {
        return chiffreAffaire;
    }

    public void setChiffreAffaire(double chiffreAffaire) {
        this.chiffreAffaire = chiffreAffaire;
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

    public YvsComComerciale getCommerciaux() {
        return commerciaux;
    }

    public void setCommerciaux(YvsComComerciale commerciaux) {
        this.commerciaux = commerciaux;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComCommercialVente> getFactures() {
        return factures;
    }

    public void setFactures(List<YvsComCommercialVente> factures) {
        this.factures = factures;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComCommercialVente> getFacturesSave() {
        return facturesSave;
    }

    public void setFacturesSave(List<YvsComCommercialVente> facturesSave) {
        this.facturesSave = facturesSave;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceCommission> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceCommission> reglements) {
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
        if (!(object instanceof YvsComCommissionCommerciaux)) {
            return false;
        }
        YvsComCommissionCommerciaux other = (YvsComCommissionCommerciaux) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.commission.YvsComCommissionCommerciaux[ id=" + id + " ]";
    }

}

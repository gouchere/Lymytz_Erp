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
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import yvs.dao.YvsEntity;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_commercial_vente", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsComCommercialVente.findAll", query = "SELECT y FROM YvsComCommercialVente y"),
    @NamedQuery(name = "YvsComCommercialVente.findById", query = "SELECT y FROM YvsComCommercialVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCommercialVente.findByTaux", query = "SELECT y FROM YvsComCommercialVente y WHERE y.taux = :taux"),
    @NamedQuery(name = "YvsComCommercialVente.findByDateSave", query = "SELECT y FROM YvsComCommercialVente y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComCommercialVente.findByDateUpdate", query = "SELECT y FROM YvsComCommercialVente y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComCommercialVente.findByAuthor", query = "SELECT y FROM YvsComCommercialVente y WHERE y.author = :author"),
    @NamedQuery(name = "YvsComCommercialVente.findByFacture", query = "SELECT y FROM YvsComCommercialVente y WHERE y.facture = :facture"),
    @NamedQuery(name = "YvsComCommercialVente.findByFactureCommercial", query = "SELECT y FROM YvsComCommercialVente y WHERE y.facture = :facture AND y.commercial = :commercial"),
    @NamedQuery(name = "YvsComCommercialVente.countByFacture", query = "SELECT COUNT(y) FROM YvsComCommercialVente y WHERE y.facture = :facture"),
    @NamedQuery(name = "YvsComCommercialVente.findByResponsable", query = "SELECT y FROM YvsComCommercialVente y WHERE y.responsable = :responsable"),
    
    @NamedQuery(name = "YvsComCommercialVente.findIdResponsable", query = "SELECT y.id FROM YvsComCommercialVente y WHERE y.responsable = true AND y.facture = :facture"),

    @NamedQuery(name = "YvsComCommercialVente.findIdFactureByDatesLivre", query = "SELECT DISTINCT y.facture.id FROM YvsComCommercialVente y WHERE y.commercial.utilisateur = :vendeur AND y.facture.typeDoc = :type AND y.facture.statut = :statut AND y.facture.dateLivraison BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComCommercialVente.findIdFactureByDatesStatut", query = "SELECT DISTINCT y.facture.id FROM YvsComCommercialVente y WHERE y.commercial.utilisateur = :vendeur AND y.facture.typeDoc = :type AND y.facture.statut = :statut AND y.facture.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComCommercialVente.findIdFactureByDates", query = "SELECT DISTINCT y.facture.id FROM YvsComCommercialVente y WHERE y.commercial.utilisateur = :vendeur AND y.facture.typeDoc = :type AND y.facture.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),

    @NamedQuery(name = "YvsComCommercialVente.countByDates", query = "SELECT COUNT(y.facture) FROM YvsComCommercialVente y WHERE y.commercial.utilisateur = :vendeur AND y.facture.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.facture.typeDoc = :type"),
    @NamedQuery(name = "YvsComCommercialVente.countBy2StatutPDatesLivre", query = "SELECT COUNT(y.facture) FROM YvsComCommercialVente y WHERE y.commercial.utilisateur = :vendeur AND y.facture.statut = :statut AND y.facture.dateLivraison BETWEEN :dateDebut AND :dateFin AND (y.facture.typeDoc = :type1 OR y.facture.typeDoc = :type2)"),
    @NamedQuery(name = "YvsComCommercialVente.countBy2StatutPDates", query = "SELECT COUNT(y.facture) FROM YvsComCommercialVente y WHERE y.commercial.utilisateur = :vendeur AND y.facture.statut = :statut AND y.facture.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND (y.facture.typeDoc = :type1 OR y.facture.typeDoc = :type2)"),
    @NamedQuery(name = "YvsComCommercialVente.countByStatutPDates", query = "SELECT COUNT(y.facture) FROM YvsComCommercialVente y WHERE y.commercial.utilisateur = :vendeur AND y.facture.statut = :statut AND y.facture.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.facture.typeDoc = :type"),
    @NamedQuery(name = "YvsComCommercialVente.countByStatutLDates", query = "SELECT COUNT(y.facture) FROM YvsComCommercialVente y WHERE y.commercial.utilisateur = :vendeur AND y.facture.statutLivre = :statut AND y.facture.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.facture.typeDoc = :type"),
    @NamedQuery(name = "YvsComCommercialVente.countByRetardLDates", query = "SELECT COUNT(y.facture) FROM YvsComCommercialVente y WHERE y.commercial.utilisateur = :vendeur AND y.facture.statutLivre != 'L' AND y.facture.dateLivraisonPrevu < :dateFin AND y.facture.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.facture.typeDoc = :type"),
    @NamedQuery(name = "YvsComCommercialVente.countByStatutRDates", query = "SELECT COUNT(y.facture) FROM YvsComCommercialVente y WHERE y.commercial.utilisateur = :vendeur AND y.facture.statutRegle = :statut AND y.facture.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.facture.typeDoc = :type"),
    
    @NamedQuery(name = "YvsComCommercialVente.sumTauxByFacture", query = "SELECT SUM(y.taux) FROM YvsComCommercialVente y WHERE y.facture = :facture"),
    @NamedQuery(name = "YvsComCommercialVente.sumTauxByFactureNotId", query = "SELECT SUM(y.taux) FROM YvsComCommercialVente y WHERE y.facture = :facture AND y.id != :id"),

    @NamedQuery(name = "YvsComCommercialVente.findFactureByIdsCommercial", query = "SELECT DISTINCT y FROM YvsComCommercialVente y WHERE y.commercial = :commercial AND y.facture.id IN :ids ORDER BY y.facture.enteteDoc.dateEntete, y.facture.heureDoc DESC"),
    @NamedQuery(name = "YvsComCommercialVente.findFactureImpayeByVendeur", query = "SELECT DISTINCT y.facture FROM YvsComCommercialVente y WHERE y.commercial.utilisateur = :vendeur AND y.facture.typeDoc = 'FV' AND y.facture.statutRegle != 'P' AND y.facture.statut = 'V' ORDER BY y.facture.enteteDoc.dateEntete, y.facture.heureDoc DESC"),
    @NamedQuery(name = "YvsComCommercialVente.findFactureByVendeurDates", query = "SELECT DISTINCT y.facture FROM YvsComCommercialVente y WHERE y.commercial.utilisateur = :vendeur AND y.facture.typeDoc = 'FV' AND y.facture.statut = 'V' AND y.facture.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin ORDER BY y.facture.enteteDoc.dateEntete, y.facture.heureDoc DESC"),
    @NamedQuery(name = "YvsComCommercialVente.findFactureByVendeur", query = "SELECT DISTINCT y.facture FROM YvsComCommercialVente y WHERE y.commercial.utilisateur = :vendeur AND y.facture.typeDoc = 'FV' AND y.facture.statut = 'V' ORDER BY y.facture.enteteDoc.dateEntete, y.facture.heureDoc DESC")})
public class YvsComCommercialVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_commercial_vente_id_seq", name = "yvs_com_commercial_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_commercial_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "responsable")
    private Boolean responsable;
    @Column(name = "diminue_ca")
    private Boolean diminueCa;
    @JsonManagedReference
    @JoinColumn(name = "facture", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocVentes facture;
    @JoinColumn(name = "commercial", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComComerciale commercial;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    @Transient
    private long idDistant;

    public long getIdDistant() {
        return idDistant;
    }

    public void setIdDistant(long idDistant) {
        this.idDistant = idDistant;
    }
    public YvsComCommercialVente() {
    }

    public YvsComCommercialVente(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTaux() {
        return taux != null ? taux : 0;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getResponsable() {
        return responsable != null ? responsable : false;
    }

    public void setResponsable(Boolean responsable) {
        this.responsable = responsable;
    }

    public YvsComDocVentes getFacture() {
        return facture;
    }

    public void setFacture(YvsComDocVentes facture) {
        this.facture = facture;
    }

    public YvsComComerciale getCommercial() {
        return commercial;
    }

    public void setCommercial(YvsComComerciale commercial) {
        this.commercial = commercial;
    }

    public Boolean getDiminueCa() {
        return diminueCa != null ? diminueCa : false;
    }

    public void setDiminueCa(Boolean diminueCa) {
        this.diminueCa = diminueCa;
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
        if (!(object instanceof YvsComCommercialVente)) {
            return false;
        }
        YvsComCommercialVente other = (YvsComCommercialVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.vente.YvsComCommercialVente[ id=" + id + " ]";
    }

}

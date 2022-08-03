/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.commission.YvsComCommissionCommerciaux;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_caisse_piece_commission")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCaissePieceCommission.findAll", query = "SELECT y FROM YvsComptaCaissePieceCommission y"),
    @NamedQuery(name = "YvsComptaCaissePieceCommission.countAll", query = "SELECT COUNT(y) FROM YvsComptaCaissePieceCommission y WHERE y.commission.periode.societe = :societe "),
    @NamedQuery(name = "YvsComptaCaissePieceCommission.findById", query = "SELECT y FROM YvsComptaCaissePieceCommission y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCaissePieceCommission.findByMission", query = "SELECT y FROM YvsComptaCaissePieceCommission y LEFT JOIN FETCH y.caisse LEFT JOIN FETCH y.model  WHERE y.commission = :commission"),
    @NamedQuery(name = "YvsComptaCaissePieceCommission.findSumByMission", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceCommission y WHERE y.commission = :commission"),
    @NamedQuery(name = "YvsComptaCaissePieceCommission.findTotalPieceByMission", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceCommission y WHERE y.commission = :commission AND y.statutPiece!=:statut"),
    @NamedQuery(name = "YvsComptaCaissePieceCommission.findTotalPiecePayeByMission", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceCommission y WHERE y.commission = :commission AND y.statutPiece=:statut"),
    @NamedQuery(name = "YvsComptaCaissePieceCommission.findByMissC", query = "SELECT COUNT(y) FROM YvsComptaCaissePieceCommission y WHERE y.commission = :commission"),
    @NamedQuery(name = "YvsComptaCaissePieceCommission.findByMissPaye", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceCommission y WHERE y.commission = :commission AND y.statutPiece=:statut"),
    @NamedQuery(name = "YvsComptaCaissePieceCommission.findByNumeroPiece", query = "SELECT y FROM YvsComptaCaissePieceCommission y WHERE y.numeroPiece = :numeroPiece"),
    @NamedQuery(name = "YvsComptaCaissePieceCommission.findByReferencePiece", query = "SELECT y FROM YvsComptaCaissePieceCommission y WHERE y.numeroPiece = :numeroPiece AND y.commission = :commission"),
    @NamedQuery(name = "YvsComptaCaissePieceCommission.findByMontant", query = "SELECT y FROM YvsComptaCaissePieceCommission y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComptaCaissePieceCommission.findSumMontantByCaisse", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceCommission y WHERE y.caisse = :caisse"),
    @NamedQuery(name = "YvsComptaCaissePieceCommission.findByStatutPiece", query = "SELECT y FROM YvsComptaCaissePieceCommission y WHERE y.statutPiece = :statutPiece"),

    @NamedQuery(name = "YvsComptaCaissePieceCommission.findByNumPiece", query = "SELECT y FROM YvsComptaCaissePieceCommission y WHERE y.commission.periode.societe = :societe AND y.numeroPiece LIKE :numero ORDER BY y.numeroPiece DESC"),
    @NamedQuery(name = "YvsComptaCaissePieceCommission.findComptabiliseById", query = "SELECT y.comptabilise FROM YvsComptaCaissePieceCommission y WHERE y.id = :id")})
public class YvsComptaCaissePieceCommission implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_caisse_piece_commission_id_seq", name = "yvs_compta_caisse_piece_commission_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_caisse_piece_commission_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "reference_externe")
    private String referenceExterne;
    @Size(max = 2147483647)
    @Column(name = "numero_piece")
    private String numeroPiece;
    @Column(name = "statut_piece")
    private Character statutPiece;
    @Column(name = "date_paiment_prevu")
    @Temporal(TemporalType.DATE)
    private Date datePaimentPrevu;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private double montant;
    @Size(max = 2147483647)
    @Column(name = "note")
    private String note;
    @Column(name = "date_piece")
    @Temporal(TemporalType.DATE)
    private Date datePiece;
    @Column(name = "date_paiement")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePaiement;
    @Column(name = "comptabilise")
    private Boolean comptabilise;

    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement model;
    @JoinColumn(name = "caissier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers caissier;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "commission", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComCommissionCommerciaux commission;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;

    @Transient
    private boolean selectActif;
    @Transient
    private boolean update;
    @Transient
    private boolean comptabilised;
    @Transient
    private boolean errorComptabilise;
    @Transient
    private boolean new_;
    @Transient
    private double montantRest;
    @Transient
    private boolean outDelai;
    @Transient
    private String nameMens;
    @Transient
    private YvsComptaBonProvisoire bonProvisoire = new YvsComptaBonProvisoire();

    public YvsComptaCaissePieceCommission() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaCaissePieceCommission(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaCaissePieceCommission(YvsComptaCaissePieceCommission y) {
        this();
        this.referenceExterne = y.referenceExterne;
        this.numeroPiece = y.numeroPiece;
        this.statutPiece = y.statutPiece;
        this.datePaimentPrevu = y.datePaimentPrevu;
        this.montant = y.montant;
        this.note = y.note;
        this.datePiece = y.datePiece;
        this.datePaiement = y.datePaiement;
        this.comptabilised = y.comptabilised;
        this.model = y.model;
        this.caissier = y.caissier;
        this.author = y.author;
        this.commission = y.commission;
        this.caisse = y.caisse;
        this.selectActif = y.selectActif;
        this.update = y.update;
        this.new_ = y.new_;
        this.montantRest = y.montantRest;
        this.outDelai = y.outDelai;
        this.nameMens = y.nameMens;
    }

    public boolean isErrorComptabilise() {
        return errorComptabilise;
    }

    public void setErrorComptabilise(boolean errorComptabilise) {
        this.errorComptabilise = errorComptabilise;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public boolean isComptabilised() {
        return comptabilised;
    }

    public void setComptabilised(boolean comptabilised) {
        this.comptabilised = comptabilised;
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
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public double getMontantRest() {
        return montantRest;
    }

    public void setMontantRest(double montantRest) {
        this.montantRest = montantRest;
    }

    public boolean isOutDelai() {
        return outDelai;
    }

    public void setOutDelai(boolean outDelai) {
        this.outDelai = outDelai;
    }

    public String getNameMens() {
        return nameMens;
    }

    public void setNameMens(String nameMens) {
        this.nameMens = nameMens;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseModeReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModeReglement model) {
        this.model = model;
    }

    public String getNumeroPiece() {
        return numeroPiece;
    }

    public void setNumeroPiece(String numeroPiece) {
        this.numeroPiece = numeroPiece;
    }

    public Character getStatutPiece() {
        return statutPiece != null ? statutPiece : 'W';
    }

    public void setStatutPiece(Character statutPiece) {
        this.statutPiece = statutPiece;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComCommissionCommerciaux getCommission() {
        return commission;
    }

    public void setCommission(YvsComCommissionCommerciaux commission) {
        this.commission = commission;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    public Date getDatePaimentPrevu() {
        return datePaimentPrevu != null ? datePaimentPrevu : new Date();
    }

    public void setDatePaimentPrevu(Date datePaimentPrevu) {
        this.datePaimentPrevu = datePaimentPrevu;
    }

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public Date getDatePiece() {
        return datePiece;
    }

    public void setDatePiece(Date datePiece) {
        this.datePiece = datePiece;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    public YvsUsers getCaissier() {
        return caissier;
    }

    public void setCaissier(YvsUsers caissier) {
        this.caissier = caissier;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaBonProvisoire getBonProvisoire() {
        return bonProvisoire;
    }

    public void setBonProvisoire(YvsComptaBonProvisoire bonProvisoire) {
        this.bonProvisoire = bonProvisoire;
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
        if (!(object instanceof YvsComptaCaissePieceCommission)) {
            return false;
        }
        YvsComptaCaissePieceCommission other = (YvsComptaCaissePieceCommission) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaCaissePieceCommission[ id=" + id + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.salaire;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.validation.constraints.Size;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_caisse_piece_salaire")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findAll", query = "SELECT y FROM YvsComptaCaissePieceSalaire y"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findById", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findByNumeroPiece", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.numeroPiece = :numeroPiece"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findByMontant", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findByStatutPiece", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.statutPiece = :statutPiece"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findByDatePiece", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.datePiece = :datePiece"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findByDatePaiement", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.datePaiement = :datePaiement"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findByNote", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.note = :note"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findByDatePaimentPrevu", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.datePaimentPrevu = :datePaimentPrevu"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findByReferenceExterne", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.referenceExterne = :referenceExterne"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findByDateValide", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.dateValide = :dateValide"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findByDateUpdate", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findByDateSave", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findByMontantRecu", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.montantRecu = :montantRecu"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findByDateCaisseMode", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.datePaimentPrevu = :datePaimentPrevu AND y.caisse = :caisse AND y.model = :model"),
    @NamedQuery(name = "YvsComptaCaissePieceSalaire.findByNumPiece", query = "SELECT y FROM YvsComptaCaissePieceSalaire y WHERE y.caisse.journal.agence.societe = :societe AND y.numeroPiece LIKE :numero ORDER BY y.numeroPiece DESC")})
public class YvsComptaCaissePieceSalaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_caisse_piece_salaire_id_seq", name = "yvs_compta_caisse_piece_salaire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_caisse_piece_salaire_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "numero_piece")
    private String numeroPiece;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "statut_piece")
    private Character statutPiece;
    @Column(name = "date_piece")
    @Temporal(TemporalType.DATE)
    private Date datePiece;
    @Column(name = "date_paiement")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePaiement;
    @Size(max = 2147483647)
    @Column(name = "note")
    private String note;
    @Column(name = "date_paiment_prevu")
    @Temporal(TemporalType.DATE)
    private Date datePaimentPrevu;
    @Size(max = 2147483647)
    @Column(name = "reference_externe")
    private String referenceExterne;
    @Column(name = "date_valide")
    @Temporal(TemporalType.DATE)
    private Date dateValide;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "montant_recu")
    private Double montantRecu;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "valide_by", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers valideBy;
    @JoinColumn(name = "caissier", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers caissier;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseModeReglement model;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse caisse;
    @OneToMany(mappedBy = "piece")
    private List<YvsComptaNotifReglementBulletin> yvsComptaCaissePieceBulletinList;

    public YvsComptaCaissePieceSalaire() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaCaissePieceSalaire(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPiece() {
        return numeroPiece;
    }

    public void setNumeroPiece(String numeroPiece) {
        this.numeroPiece = numeroPiece;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Character getStatutPiece() {
        return statutPiece;
    }

    public void setStatutPiece(Character statutPiece) {
        this.statutPiece = statutPiece;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDatePaimentPrevu() {
        return datePaimentPrevu;
    }

    public void setDatePaimentPrevu(Date datePaimentPrevu) {
        this.datePaimentPrevu = datePaimentPrevu;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public Date getDateValide() {
        return dateValide;
    }

    public void setDateValide(Date dateValide) {
        this.dateValide = dateValide;
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

    public Double getMontantRecu() {
        return montantRecu;
    }

    public void setMontantRecu(Double montantRecu) {
        this.montantRecu = montantRecu;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsers getValideBy() {
        return valideBy;
    }

    public void setValideBy(YvsUsers valideBy) {
        this.valideBy = valideBy;
    }

    public YvsUsers getCaissier() {
        return caissier;
    }

    public void setCaissier(YvsUsers caissier) {
        this.caissier = caissier;
    }

    public YvsBaseModeReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModeReglement model) {
        this.model = model;
    }

    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    public List<YvsComptaNotifReglementBulletin> getYvsComptaCaissePieceBulletinList() {
        return yvsComptaCaissePieceBulletinList;
    }

    public void setYvsComptaCaissePieceBulletinList(List<YvsComptaNotifReglementBulletin> yvsComptaCaissePieceBulletinList) {
        this.yvsComptaCaissePieceBulletinList = yvsComptaCaissePieceBulletinList;
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
        if (!(object instanceof YvsComptaCaissePieceSalaire)) {
            return false;
        }
        YvsComptaCaissePieceSalaire other = (YvsComptaCaissePieceSalaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.salaire.YvsComptaCaissePieceSalaire[ id=" + id + " ]";
    }

}

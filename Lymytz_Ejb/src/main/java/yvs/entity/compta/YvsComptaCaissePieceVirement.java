/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceVirement;
import yvs.entity.compta.vente.YvsComptaNotifVersementVente;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_caisse_piece_virement")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findAll", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.source.journal.agence.societe=:societe"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.countAll", query = "SELECT COUNT(y) FROM YvsComptaCaissePieceVirement y WHERE y.source.journal.agence.societe=:societe"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findById", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByNumeroPiece", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.numeroPiece LIKE :numeroPiece AND y.source.journal.agence.societe=:societe ORDER BY y.numeroPiece DESC"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByMontant", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findSumBySourceDates", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVirement y WHERE y.source = :caisse AND y.statutPiece = :statut AND y.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findSumMontantBySource", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVirement y WHERE y.source = :caisse"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findSumMontantByCible", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVirement y WHERE y.cible = :caisse"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByStatutPiece", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.statutPiece = :statutPiece"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByDatePiece", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.datePiece = :datePiece"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByDatePaiement", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.datePaiement = :datePaiement"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findBySourcesDate", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.source.id IN :sources AND y.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByNote", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.note = :note"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByDatePaimentPrevu", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.datePaimentPrevu = :datePaimentPrevu"),

    @NamedQuery(name = "YvsComptaCaissePieceVirement.findComptabiliseById", query = "SELECT y.comptabilise FROM YvsComptaCaissePieceVirement y WHERE y.id = :id")})
public class YvsComptaCaissePieceVirement extends YvsEntity implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_caisse_piece_virement_id_seq", name = "yvs_compta_caisse_piece_virement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_caisse_piece_virement_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "numero_piece")
    private String numeroPiece;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
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
    @Column(name = "montant")
    private Double montant;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "caissier_cible", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers caissierCible;
    @JoinColumn(name = "caissier_source", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers caissierSource;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement model;
    @JoinColumn(name = "cible", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse cible;
    @JoinColumn(name = "source", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse source;

    @OneToOne(mappedBy = "piece")
    private YvsComptaNotifVersementVente versement;
    @OneToOne(mappedBy = "reglement")
    private YvsComptaContentJournalPieceVirement pieceContenu;

    @OneToMany(mappedBy = "pieceVirement")
    private List<YvsComptaBielletage> bielletages;
    @OneToMany(mappedBy = "virement")
    private List<YvsComptaPhasePieceVirement> phasesReglement;
    @OneToMany(mappedBy = "virement")
    private List<YvsComptaCoutSupPieceVirement> couts;
    @Transient
    private YvsComptaJournaux journalCible, journalSource;
    @Transient
    private YvsComEnteteDocVente header;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private boolean errorComptabilise;
    @Transient
    private double montantCout;
    @Transient
    private boolean comptabilised;

    ;

    public YvsComptaCaissePieceVirement() {
        bielletages = new ArrayList<>();
        phasesReglement = new ArrayList<>();
        couts = new ArrayList<>();
    }

    public YvsComptaCaissePieceVirement(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaCaissePieceVirement(Long id, Character statutPiece) {
        this(id);
        this.statutPiece = statutPiece;
    }

    public YvsComptaCaissePieceVirement(Long id, Character statutPiece, Date datePaiement, Date datePaiementPrevu, YvsBaseCaisse cible, YvsBaseCaisse source) {
        this(id, statutPiece);
        this.datePaiement = datePaiement;
        this.datePaimentPrevu = datePaiementPrevu;
        this.cible = cible;
        this.source = source;
    }

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
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
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPiece() {
        return numeroPiece != null ? numeroPiece : "";
    }

    public void setNumeroPiece(String numeroPiece) {
        this.numeroPiece = numeroPiece;
    }

    public Character getStatutPiece() {
        return statutPiece;
    }

    public void setStatutPiece(Character statutPiece) {
        this.statutPiece = statutPiece;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDatePiece() {
        return datePiece;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDatePiece(Date datePiece) {
        this.datePiece = datePiece;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDatePaiement() {
        return datePaiement;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDatePaimentPrevu() {
        return datePaimentPrevu != null ? datePaimentPrevu : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDatePaimentPrevu(Date datePaimentPrevu) {
        this.datePaimentPrevu = datePaimentPrevu;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsers getCaissierCible() {
        return caissierCible;
    }

    public void setCaissierCible(YvsUsers caissierCible) {
        this.caissierCible = caissierCible;
    }

    public YvsUsers getCaissierSource() {
        return caissierSource;
    }

    public void setCaissierSource(YvsUsers caissierSource) {
        this.caissierSource = caissierSource;
    }

    public YvsBaseModeReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModeReglement model) {
        this.model = model;
    }

    public YvsBaseCaisse getCible() {
        return cible;
    }

    public void setCible(YvsBaseCaisse cible) {
        this.cible = cible;
    }

    public YvsBaseCaisse getSource() {
        return source;
    }

    public void setSource(YvsBaseCaisse source) {
        this.source = source;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isErrorComptabilise() {
        return errorComptabilise;
    }

    public void setErrorComptabilise(boolean errorComptabilise) {
        this.errorComptabilise = errorComptabilise;
    }

    public boolean isComptabilised() {
        return comptabilised;
    }

    public void setComptabilised(Boolean comptabilise) {
        this.comptabilised = comptabilise;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    @XmlTransient
    public List<YvsComptaBielletage> getBielletages() {
        return bielletages;
    }

    public void setBielletages(List<YvsComptaBielletage> bielletages) {
        this.bielletages = bielletages;
    }

    public List<YvsComptaPhasePieceVirement> getPhasesReglement() {
        return phasesReglement;
    }

    public void setPhasesReglement(List<YvsComptaPhasePieceVirement> phasesReglement) {
        this.phasesReglement = phasesReglement;
    }

    public List<YvsComptaCoutSupPieceVirement> getCouts() {
        return couts;
    }

    public void setCouts(List<YvsComptaCoutSupPieceVirement> couts) {
        this.couts = couts;
    }

    public double getMontantCout() {
        montantCout = 0;
        for (YvsComptaCoutSupPieceVirement c : couts) {
            montantCout += c.getMontant();
        }
        return montantCout;
    }

    public void setMontantCout(double montantCout) {
        this.montantCout = montantCout;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaNotifVersementVente getVersement() {
        return versement;
    }

    public void setVersement(YvsComptaNotifVersementVente versement) {
        this.versement = versement;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaContentJournalPieceVirement getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalPieceVirement pieceContenu) {
        this.pieceContenu = pieceContenu;
    }

    public YvsComptaJournaux getJournalCible() {
        return journalCible;
    }

    public void setJournalCible(YvsComptaJournaux journalCible) {
        this.journalCible = journalCible;
    }

    public YvsComptaJournaux getJournalSource() {
        return journalSource;
    }

    public void setJournalSource(YvsComptaJournaux journalSource) {
        this.journalSource = journalSource;
    }

    public YvsComEnteteDocVente getHeader() {
        return header;
    }

    public void setHeader(YvsComEnteteDocVente header) {
        this.header = header;
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
        if (!(object instanceof YvsComptaCaissePieceVirement)) {
            return false;
        }
        YvsComptaCaissePieceVirement other = (YvsComptaCaissePieceVirement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaCaissePieceVirement[ id=" + id + " ]";
    }

    public int getPhaseValide() {
        int nb = 0;
        for (YvsComptaPhasePieceVirement vm : getPhasesReglement()) {
            if (vm.getPhaseOk()) {
                nb++;
            }
        }
        return nb++;
    }

    public String getLibphases() {
        return "Etp. " + getPhaseValide() + " / " + getPhasesReglement().size();
    }

}

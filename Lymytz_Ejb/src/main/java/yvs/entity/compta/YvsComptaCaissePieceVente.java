/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceVente;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_caisse_piece_vente", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCaissePieceVente.findAll", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.vente.enteteDoc.creneau.creneauPoint.point.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.countAll", query = "SELECT COUNT(y) FROM YvsComptaCaissePieceVente y WHERE y.vente.enteteDoc.creneau.creneauPoint.point.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findAllStatut", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.vente.enteteDoc.creneau.creneauPoint.point.agence.societe = :societe AND y.statutPiece = :statut"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByIdAll", query = "SELECT y FROM YvsComptaCaissePieceVente y JOIN FETCH y.model JOIN FETCH y.vente JOIN FETCH y.caisse JOIN FETCH y.vente.client JOIN FETCH y.author.users "
            + "LEFT JOIN FETCH y.caissier LEFT JOIN FETCH y.valideBy "
            + "WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findById", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByIds", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByFactureStatut", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.vente = :facture AND y.statutPiece = :statut ORDER BY y.datePaiement"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findIdByFactureStatut", query = "SELECT y.id FROM YvsComptaCaissePieceVente y WHERE y.vente = :facture AND y.statutPiece = :statut"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findDepenseByFactureStatutS", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente = :facture AND y.statutPiece = :statut AND COALESCE(y.mouvement, 'R') = 'D'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByFactureStatutS", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente = :facture AND y.statutPiece = :statut AND (y.mouvement IS NULL OR y.mouvement = 'R')"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByFactureStatutSDiff", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente = :facture AND y.statutPiece != :statut AND (y.mouvement IS NULL OR y.mouvement = 'R')"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByMensualite", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.vente = :vente"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.countByMensualite", query = "SELECT COUNT(y) FROM YvsComptaCaissePieceVente y WHERE y.vente = :vente"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByMensualiteStatut", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.vente = :vente AND y.statutPiece = :statu"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByMensualiteStatutS", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente = :vente AND y.statutPiece = :statut AND COALESCE(y.mouvement, 'R') = 'R' AND y.datePaiement <= :date"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findDepenseByMensualiteStatutS", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente = :facture AND y.statutPiece = :statut AND COALESCE(y.mouvement, 'R') = 'D' AND y.datePaiement <= :date"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByNumeroPiece", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.numeroPiece = :numeroPiece AND y.vente=:docVente"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByMontant", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumMontantByCaisse", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.caisse = :caisse AND COALESCE(y.mouvement, 'R') = 'R'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByStatutPiece", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.statutPiece = :statutPiece"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findMontantRecuByFacture", query = "SELECT y.montantRecu FROM YvsComptaCaissePieceVente y WHERE y.vente = :vente"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByNotNumero", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE TRIM(COALESCE(y.numeroPiece, '')) = '' AND y.datePiece BETWEEN :debut AND :fin AND y.vente.enteteDoc.creneau.creneauPoint.point.agence.societe = :societe"),
    
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByVentes", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE (y.vente = :vente1 OR y.vente.documentLie = :vente2) ORDER BY y.datePaiement"),

    @NamedQuery(name = "YvsComptaCaissePieceVente.findComptabiliseById", query = "SELECT y.comptabilise FROM YvsComptaCaissePieceVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByNumPiece", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.vente.enteteDoc.agence.societe = :societe AND y.numeroPiece LIKE :numero ORDER BY y.numeroPiece DESC"),

    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumByVendeur", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente.enteteDoc.creneau.users = :vendeur AND COALESCE(y.mouvement, 'R') = 'R' AND y.statutPiece = 'P'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumByVendeurDates", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente.enteteDoc.creneau.users = :vendeur AND y.datePaiement BETWEEN :dateDebut AND :dateFin AND COALESCE(y.mouvement, 'R') = 'R' AND y.statutPiece = 'P'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumByVendeurDatesVente", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente.enteteDoc.creneau.users = :vendeur AND y.vente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND COALESCE(y.mouvement, 'R') = 'R' AND y.statutPiece = 'P'"),

    @NamedQuery(name = "YvsComptaCaissePieceVente.findByFacture", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.vente = :facture"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findRegFactByFacture", query = "SELECT y FROM YvsComptaCaissePieceVente y LEFT JOIN FETCH y.caisse JOIN FETCH y.model LEFT JOIN FETCH y.caissier WHERE y.vente = :facture AND y.parent IS NULL"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumByFacture", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente = :facture AND COALESCE(y.mouvement, 'R') = 'R'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumByVentesStatut", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente.id IN :ventes AND y.statutPiece = :statut AND COALESCE(y.mouvement, 'R') = 'R'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumByCaissierDates", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.caissier = :caissier AND (y.datePaiement BETWEEN :dateDebut AND :dateFin) AND COALESCE(y.mouvement, 'R') = 'R'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumBCVRecuByCaissierDates", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.caissier = :caissier AND (y.datePaiement BETWEEN :dateDebut AND :dateFin) AND y.vente.typeDoc = 'BCV' AND y.statutPiece = 'P' AND COALESCE(y.mouvement, 'R') = 'R'"),

    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumByClientDate", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente.client = :client AND y.statutPiece = 'P' AND y.datePaiement <= :dateFin AND COALESCE(y.mouvement, 'R') = :mouvement"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumByClient", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente.client = :client AND y.vente.typeDoc = 'FV' AND y.statutPiece = 'P' AND COALESCE(y.mouvement, 'R') = 'R'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumByClientDates", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente.client = :client AND y.vente.typeDoc = 'FV' AND y.statutPiece = 'P' AND y.datePaiement BETWEEN :dateDebut AND :dateFin AND COALESCE(y.mouvement, 'R') = :mouvement"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumByClientDatesAgence", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente.enteteDoc.agence = :agence AND y.vente.client = :client AND y.vente.typeDoc = 'FV' AND y.statutPiece = 'P' AND y.datePaiement BETWEEN :dateDebut AND :dateFin AND COALESCE(y.mouvement, 'R') = :mouvement"),

    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumAvoirByClient", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente.client = :client AND y.vente.typeDoc = 'FAV' AND y.statutPiece = 'P' AND COALESCE(y.mouvement, 'R') = 'D'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumAvoirByClientDates", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente.client = :client AND y.vente.typeDoc = 'FAV' AND y.statutPiece = 'P' AND y.datePaiement BETWEEN :dateDebut AND :dateFin AND COALESCE(y.mouvement, 'R') = :mouvement"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumAvoirByClientDatesAgence", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente.enteteDoc.agence = :agence AND y.vente.client = :client AND y.vente.typeDoc = 'FAV' AND y.statutPiece = 'P' AND y.datePaiement BETWEEN :dateDebut AND :dateFin AND COALESCE(y.mouvement, 'R') = :mouvement"),

    @NamedQuery(name = "YvsComptaCaissePieceVente.findCaisseByCaissier", query = "SELECT DISTINCT y.caisse FROM YvsComptaCaissePieceVente y WHERE y.caissier = :caissier AND COALESCE(y.mouvement, 'R') = 'R'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findCaisseByEntete", query = "SELECT DISTINCT y.caisse FROM YvsComptaCaissePieceVente y WHERE y.vente.enteteDoc = :enteteDoc AND COALESCE(y.mouvement, 'R') = 'R'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findIdCaisseByEntete", query = "SELECT DISTINCT y.caisse.id FROM YvsComptaCaissePieceVente y WHERE y.vente.enteteDoc = :enteteDoc AND COALESCE(y.mouvement, 'R') = 'R'"),

    @NamedQuery(name = "YvsComptaCaissePieceVente.findAvanceByModeFacturesDates", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.model = :model AND y.statutPiece = 'P' AND (y.datePaiement BETWEEN :dateDebut AND :dateFin) AND y.vente.id IN :facture AND COALESCE(y.mouvement, 'R') = 'R'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findAvanceByModeDates", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.model = :model AND y.statutPiece = 'P' AND y.datePaiement BETWEEN :dateDebut AND :dateFin AND COALESCE(y.mouvement, 'R') = 'R'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findSumByCaissier", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.caissier = :caissier AND y.statutPiece = 'P' AND COALESCE(y.mouvement, 'R') = 'R' AND y.datePaiement=:date AND y.vente.typeDoc='BCV'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByFacturesRegle", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.statutPiece=:statutPiece AND (y.vente.client.tiers=:tiers AND y.vente.typeDoc=:typeDoc AND y.vente.statut IN :statut AND y.vente.statutRegle IN :statutRegle)"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.countByParent", query = "SELECT COUNT(y) FROM YvsComptaCaissePieceVente y WHERE y.parent=:piece"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByFactureTypeStatut", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.vente = :vente AND (y.model IS NULL OR y.model.typeReglement = :type) AND y.statutPiece = :statut")})

public class YvsComptaCaissePieceVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_caisse_piece_vente_id_seq", name = "yvs_compta_caisse_piece_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_caisse_piece_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
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
    @Size(max = 2147483647)
    @Column(name = "reference_externe")
    private String referenceExterne;
    @Column(name = "statut_piece")
    private Character statutPiece;
    @Column(name = "mouvement")
    private Character mouvement;
    @Size(max = 2147483647)
    @Column(name = "note")
    private String note;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "verouille")
    private Boolean verouille;
    @Column(name = "date_piece")
    @Temporal(TemporalType.DATE)
    private Date datePiece;
    @Column(name = "date_paiement")
    @Temporal(TemporalType.DATE)
    private Date datePaiement;
    @Column(name = "date_paiment_prevu")
    @Temporal(TemporalType.DATE)
    private Date datePaimentPrevu;
    @Column(name = "date_valide")
    @Temporal(TemporalType.DATE)
    private Date dateValide;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "montant_recu")
    private Double montantRecu; //montant reç du client

    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement model = new YvsBaseModeReglement();
    @JoinColumn(name = "vente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    private YvsComDocVentes vente;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse = new YvsBaseCaisse();
    @JoinColumn(name = "caissier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers caissier;
    @JoinColumn(name = "valide_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers valideBy;
    @JsonManagedReference
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceVente parent;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToOne(mappedBy = "reglement", fetch = FetchType.LAZY)
    private YvsComptaContentJournalPieceVente pieceContenu;
    @OneToOne(mappedBy = "piceReglement", fetch = FetchType.LAZY)
    private YvsGrhElementAdditionel retenue;

    @JsonBackReference
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<YvsComptaCaissePieceVente> sousVentes;
    @JsonBackReference
    @OneToMany(mappedBy = "vente", fetch = FetchType.LAZY)
    private List<YvsComptaCaissePieceCompensation> compensations;
    @JsonBackReference
    @OneToOne(mappedBy = "pieceVente", fetch = FetchType.LAZY)
    private YvsComptaNotifReglementVente notifs;
    @JsonBackReference
    @OneToMany(mappedBy = "pieceVente", fetch = FetchType.LAZY)
    private List<YvsComptaPhasePiece> phasesReglement;

//    @OneToMany(mappedBy = "piceReglement",fetch = FetchType.LAZY)
//    private List<YvsGrhElementAdditionel> retenues;
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<YvsComptaCaissePieceVente> othersReglements;

    @Transient
    private YvsComptaJournaux journal;
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
    private boolean view_id;
    @Transient
    private double montantRest;
    @Transient
    private boolean outDelai;
    @Transient
    private String nameMens;
    @Transient
    private double sommeAchat;

    public YvsComptaCaissePieceVente() {
        compensations = new ArrayList<>();
//        retenues = new ArrayList<>();
        phasesReglement = new ArrayList<>();
        sousVentes = new ArrayList<>();
        othersReglements = new ArrayList<>();
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaCaissePieceVente(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaCaissePieceVente(YvsComptaCaissePieceVente y) {
        this(y.getId(), y);
    }

    public YvsComptaCaissePieceVente(Long id, YvsComptaCaissePieceVente y) {
        this(id);
        this.numeroPiece = y.numeroPiece;
        this.statutPiece = y.statutPiece;
        this.montant = y.montant;
        this.montantRecu = y.montantRecu;
        this.note = y.note;
        this.datePiece = y.datePiece;
        this.comptabilise = y.comptabilise;
        this.datePaiement = y.datePaiement;
        this.model = y.model;
        this.author = y.author;
        this.vente = y.vente;
        this.caisse = y.caisse;
        this.caissier = y.caissier;
        this.datePaimentPrevu = y.datePaimentPrevu;
        this.selectActif = y.selectActif;
        this.update = y.update;
        this.new_ = y.new_;
        this.montantRest = y.montantRest;
        this.outDelai = y.outDelai;
        this.nameMens = y.nameMens;
        this.referenceExterne = y.referenceExterne;
        this.dateValide = y.dateValide;
        this.valideBy = y.valideBy;
        this.compensations = y.compensations;
        this.notifs = y.notifs;
//        this.retenues = y.retenues;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public Boolean getVerouille() {
        return verouille != null ? verouille : false;
    }

    public void setVerouille(Boolean verouille) {
        this.verouille = verouille;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhElementAdditionel getRetenue() {
        return retenue;
    }

    public void setRetenue(YvsGrhElementAdditionel retenue) {
        this.retenue = retenue;
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
        outDelai = dateToCalendar(getDatePaimentPrevu()).before(dateToCalendar(new Date()));
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

    public String getNumeroPiece() {
        return numeroPiece != null ? numeroPiece : "";
    }

    public void setNumeroPiece(String numeroPiece) {
        this.numeroPiece = numeroPiece;
    }

    public Character getStatutPiece() {
        return statutPiece != null ? statutPiece : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatutPiece(Character statutPiece) {
        this.statutPiece = statutPiece;
    }

    public Character getMouvement() {
        return mouvement != null ? String.valueOf(mouvement).trim().length() > 0 ? mouvement : 'R' : 'R';
    }

    public void setMouvement(Character mouvement) {
        this.mouvement = mouvement;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDatePaimentPrevu() {
        return datePaimentPrevu != null ? datePaimentPrevu : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDatePaimentPrevu(Date datePaimentPrevu) {
        this.datePaimentPrevu = datePaimentPrevu;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDatePiece() {
        return datePiece;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDatePiece(Date datePiece) {
        this.datePiece = datePiece;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDatePaiement() {
        return datePaiement;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateValide() {
        return dateValide;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateValide(Date dateValide) {
        this.dateValide = dateValide;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public boolean isView_id() {
        return view_id;
    }

    public void setView_id(boolean view_id) {
        this.view_id = view_id;
    }

    public Double getMontantRecu() {
        return montantRecu;
    }

    public void setMontantRecu(Double montantRecu) {
        this.montantRecu = montantRecu;
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

    public void setComptabilised(boolean comptabilised) {
        this.comptabilised = comptabilised;
    }

    public YvsUsers getCaissier() {
        return caissier;
    }

    public void setCaissier(YvsUsers caissier) {
        this.caissier = caissier;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComDocVentes getVente() {
        return vente;
    }

    public void setVente(YvsComDocVentes vente) {
        this.vente = vente;
    }

    public YvsBaseCaisse getCaisse() {
        return caisse != null ? caisse : new YvsBaseCaisse();
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    public YvsBaseModeReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModeReglement model) {
        this.model = model;
    }

    public YvsUsers getValideBy() {
        return valideBy;
    }

    public void setValideBy(YvsUsers valideBy) {
        this.valideBy = valideBy;
    }

//    @XmlTransient
//    public List<YvsGrhElementAdditionel> getRetenues() {
//        if (retenues == null) {
//            retenues = new ArrayList<>();
//        }
//        return retenues;
//    }
//
//    public void setRetenues(List<YvsGrhElementAdditionel> retenues) {
//        this.retenues = retenues;
//    }
    public String getAction() {
        if (statutPiece != null) {
            if (statutPiece == Constantes.STATUT_ATTENTE) {
                return "Valider";
            } else if (statutPiece == Constantes.STATUT_DOC_ATTENTE || statutPiece == Constantes.STATUT_DOC_VALIDE) {
                return "Encaisser";
            } else {
                return "Annuler";
            }
        } else {
            return "Annuler";
        }
    }

    @XmlTransient
    @JsonIgnore
    public int getPhaseValide() {
        int nb = 0;
        for (YvsComptaPhasePiece vm : getPhasesReglement()) {
            if (vm.getPhaseOk()) {
                nb++;
            }
        }
        return nb++;
    }

    @XmlTransient
    @JsonIgnore
    public String getLibphases() {
        return "Etp. " + getPhaseValide() + " / " + getPhasesReglement().size();
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaPhasePiece> getPhasesReglement() {
        return phasesReglement;
    }

    public void setPhasesReglement(List<YvsComptaPhasePiece> phasesReglement) {
        this.phasesReglement = phasesReglement;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaNotifReglementVente getNotifs() {
        return notifs;
    }

    public void setNotifs(YvsComptaNotifReglementVente notifs) {
        this.notifs = notifs;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceCompensation> getCompensations() {
        return compensations;
    }

    public void setCompensations(List<YvsComptaCaissePieceCompensation> compensations) {
        this.compensations = compensations;
    }

    @XmlTransient
    @JsonIgnore
    public double getSommeAchat() {
        sommeAchat = 0;
        for (YvsComptaCaissePieceCompensation y : compensations) {
            if (y.getId() > 0) {
                sommeAchat += y.getAchat().getMontant();
            }
        }
        return sommeAchat;
    }

    public void setSommeAchat(double sommeAchat) {
        this.sommeAchat = sommeAchat;
    }

    @XmlTransient
    @JsonIgnore
    public double getMontantNonCompense() {
        double re = getMontant();
        if (compensations != null ? compensations.isEmpty() : false) {
            for (YvsComptaCaissePieceCompensation p : compensations) {
                re -= p.getAchat().getMontant();
            }
        }
        return re;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaContentJournalPieceVente getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalPieceVente pieceContenu) {
        this.pieceContenu = pieceContenu;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaCaissePieceVente getParent() {
        return parent;
    }

    public void setParent(YvsComptaCaissePieceVente parent) {
        this.parent = parent;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceVente> getSousVentes() {
        return sousVentes;
    }

    public void setSousVentes(List<YvsComptaCaissePieceVente> sousVentes) {
        this.sousVentes = sousVentes;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceVente> getOthersReglements() {
        return othersReglements;
    }

    public void setOthersReglements(List<YvsComptaCaissePieceVente> othersReglements) {
        this.othersReglements = othersReglements;
    }

    public YvsComptaJournaux getJournal() {
        return journal;
    }

    public void setJournal(YvsComptaJournaux journal) {
        this.journal = journal;
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
        if (!(object instanceof YvsComptaCaissePieceVente)) {
            return false;
        }
        YvsComptaCaissePieceVente other = (YvsComptaCaissePieceVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaCaissePieceVente[ id=" + id + " ]";
    }

    Calendar dateToCalendar(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(date);
            return cal;
        }
        return Calendar.getInstance();
    }

    @XmlTransient
    @JsonIgnore
    public String canValide() {
        String ERROR = "";
        double montant = 0;
        if (getModel() != null ? getModel().getId() > 0 ? !getModel().getTypeReglement().equals("COMPENSATION") : false : false) {
            montant = getMontant();
        }
        for (YvsComptaCaissePieceCompensation y : compensations) {
            if (y.getAchat().getModel() != null ? y.getAchat().getModel().getId() < 1 : true) {
                ERROR = "Un reglement n'a pas de mode de paiement";
                break;
            }
            if (getModel() != null ? getModel().getId() > 0 ? !getModel().getTypeReglement().equals("COMPENSATION") : false : false) {
                if (y.getAchat().getCaisse() != null ? y.getAchat().getCaisse().getId() < 1 : true) {
                    ERROR = "Un reglement n'a pas de caisse";
                    break;
                }
            }
            montant += y.getAchat().getMontant();
        }
        if (ERROR != null ? ERROR.trim().length() < 1 : true) {
            if (getMontant() != montant) {
                ERROR = "Cette compensation n'est pas équilibrée";
            }
        }
        return ERROR;
    }

    public boolean error() {
        if (getModel() != null ? getModel().getId() > 0 ? getModel().getTypeReglement().equals("SALAIRE") : false : false) {
            if (getStatutPiece().equals('P')) {
//                return getRetenues().isEmpty();
                return getRetenue() == null;
            }
        }
        return false;
    }

}

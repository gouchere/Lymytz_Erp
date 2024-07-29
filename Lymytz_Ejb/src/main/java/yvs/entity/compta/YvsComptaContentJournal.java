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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import yvs.dao.YvsEntity;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.compta.saisie.YvsComptaContentAnalytique;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_content_journal")
@NamedQueries({
    @NamedQuery(name = "YvsComptaContentJournal.findAll", query = "SELECT y FROM YvsComptaContentJournal y"),
    @NamedQuery(name = "YvsComptaContentJournal.findById", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaContentJournal.findSoldeJournal", query = "SELECT SUM(y.debit - y.credit) FROM YvsComptaContentJournal y WHERE y.piece.journal=:journal"),
    @NamedQuery(name = "YvsComptaContentJournal.findSoldePiece", query = "SELECT SUM(y.debit - y.credit) FROM YvsComptaContentJournal y WHERE y.piece=:piece"),
    @NamedQuery(name = "YvsComptaContentJournal.findCreditByPiece", query = "SELECT SUM(y.credit) FROM YvsComptaContentJournal y WHERE y.piece=:piece"),
    @NamedQuery(name = "YvsComptaContentJournal.findDebitByPiece", query = "SELECT SUM(y.debit) FROM YvsComptaContentJournal y WHERE y.piece=:piece"),
    @NamedQuery(name = "YvsComptaContentJournal.findCreditByJournalDates", query = "SELECT SUM(y.credit) FROM YvsComptaContentJournal y WHERE y.piece.journal=:journal AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findDebitByJournalDates", query = "SELECT SUM(y.debit) FROM YvsComptaContentJournal y WHERE y.piece.journal=:journal AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findByJour", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.jour = :jour"),
    @NamedQuery(name = "YvsComptaContentJournal.findByNumPiece", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.numPiece = :numPiece"),
    @NamedQuery(name = "YvsComptaContentJournal.findByNumRef", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.numRef = :numRef AND y.piece.journal.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaContentJournal.findByLibelle", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsComptaContentJournal.findByDebit", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.debit = :debit"),
    @NamedQuery(name = "YvsComptaContentJournal.findByCredit", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.credit = :credit"),
    @NamedQuery(name = "YvsComptaContentJournal.findByEcheance", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.echeance = :echeance"),
    @NamedQuery(name = "YvsComptaContentJournal.findByRefExterne", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.refExterne = :refExterne"),

    @NamedQuery(name = "YvsComptaContentJournal.countByPiece", query = "SELECT COUNT(y) FROM YvsComptaContentJournal y WHERE y.piece = :piece"),

    @NamedQuery(name = "YvsComptaContentJournal.findExterneByTable", query = "SELECT DISTINCT y.refExterne FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.tableExterne = :table ORDER BY y.refExterne"),
    @NamedQuery(name = "YvsComptaContentJournal.findCompteByComptes", query = "SELECT DISTINCT y.compteGeneral.id, y.compteGeneral.numCompte, y.compteGeneral.intitule FROM YvsComptaContentJournal y WHERE y.piece.journal.agence = :agence AND y.compteGeneral.numCompte BETWEEN :numeroDebut AND :numeroFin AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin ORDER BY y.compteGeneral.numCompte"),
    @NamedQuery(name = "YvsComptaContentJournal.findTiersByComptes", query = "SELECT DISTINCT y.compteTiers FROM YvsComptaContentJournal y WHERE y.piece.journal.agence = :agence AND y.compteTiers BETWEEN :numeroDebut AND :numeroFin AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin ORDER BY y.compteTiers"),
    @NamedQuery(name = "YvsComptaContentJournal.findAllCompteByComptes", query = "SELECT DISTINCT y.compteGeneral.id, y.compteGeneral.numCompte, y.compteGeneral.intitule FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.compteGeneral.numCompte BETWEEN :numeroDebut AND :numeroFin AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin ORDER BY y.compteGeneral.numCompte"),
    @NamedQuery(name = "YvsComptaContentJournal.findAllTiersByComptes", query = "SELECT DISTINCT y.compteTiers FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.compteTiers BETWEEN :numeroDebut AND :numeroFin AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin ORDER BY y.compteTiers"),
    @NamedQuery(name = "YvsComptaContentJournal.findByCompteGeneral", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.piece.journal.agence = :agence AND y.compteGeneral.id = :compte AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin ORDER BY y.piece.datePiece"),
    @NamedQuery(name = "YvsComptaContentJournal.findByCompteTiers", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.piece.journal.agence = :agence AND y.compteTiers = :compte AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin ORDER BY y.piece.datePiece"),
    @NamedQuery(name = "YvsComptaContentJournal.findAllByCompteGeneral", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.compteGeneral.id = :compte AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin ORDER BY y.piece.datePiece"),
    @NamedQuery(name = "YvsComptaContentJournal.findAllByCompteTiers", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.compteTiers = :compte AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin ORDER BY y.piece.datePiece"),
    @NamedQuery(name = "YvsComptaContentJournal.findNoLetterByCompteGeneral", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.piece.exercice = :exercice AND y.compteGeneral = :compte AND LENGTH(COALESCE(y.lettrage, '')) < 1 AND (COALESCE(y.debit, 0) > 0 OR COALESCE(y.credit, 0) > 0) ORDER BY y.piece.datePiece"),
    @NamedQuery(name = "YvsComptaContentJournal.findNoLetterByCompteGeneralAgence", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.piece.exercice = :exercice AND y.piece.journal.agence = :agence AND y.compteGeneral = :compte AND LENGTH(COALESCE(y.lettrage, '')) < 1 AND (COALESCE(y.debit, 0) > 0 OR COALESCE(y.credit, 0) > 0) ORDER BY y.piece.datePiece"),

    @NamedQuery(name = "YvsComptaContentJournal.findSumCreditByLettrage", query = "SELECT SUM(y.credit) FROM YvsComptaContentJournal y WHERE y.piece.exercice = :exercice AND y.lettrage = :lettrage"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumDebitByLettrage", query = "SELECT SUM(y.debit) FROM YvsComptaContentJournal y WHERE y.piece.exercice = :exercice AND y.lettrage = :lettrage"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumCreditByCompteGeneral", query = "SELECT SUM(y.credit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence = :agence AND y.compteGeneral.id = :compte AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumDebitByCompteGeneral", query = "SELECT SUM(y.debit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence = :agence AND y.compteGeneral.id = :compte AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumAllCreditByCompteGeneral", query = "SELECT SUM(y.credit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.compteGeneral.id = :compte AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumAllDebitByCompteGeneral", query = "SELECT SUM(y.debit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.compteGeneral.id = :compte AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumCreditByCompteTiers", query = "SELECT SUM(y.credit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence = :agence AND y.compteTiers = :compte AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumDebitByCompteTiers", query = "SELECT SUM(y.debit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence = :agence AND y.compteTiers = :compte AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumAllCreditByCompteTiers", query = "SELECT SUM(y.credit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.compteTiers = :compte AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumAllDebitByCompteTiers", query = "SELECT SUM(y.debit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.compteTiers = :compte AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumCreditByDatesCompteNotNull", query = "SELECT SUM(y.credit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence = :agence AND y.compteGeneral IS NOT NULL AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumDebitByDatesCompteNotNull", query = "SELECT SUM(y.debit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence = :agence AND y.compteGeneral IS NOT NULL AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumAllCreditByDatesCompteNotNull", query = "SELECT SUM(y.credit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.compteGeneral IS NOT NULL AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumAllDebitByDatesCompteNotNull", query = "SELECT SUM(y.debit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.compteGeneral IS NOT NULL AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumCreditByDatesTiersNotNull", query = "SELECT SUM(y.credit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence = :agence AND y.compteTiers IS NOT NULL AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumDebitByDatesTiersNotNull", query = "SELECT SUM(y.debit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence = :agence AND y.compteTiers IS NOT NULL AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumAllCreditByDatesTiersNotNull", query = "SELECT SUM(y.credit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.compteTiers IS NOT NULL AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findSumAllDebitByDatesTiersNotNull", query = "SELECT SUM(y.debit) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.compteTiers IS NOT NULL AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),

    @NamedQuery(name = "YvsComptaContentJournal.findByExterne", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.refExterne = :id AND y.tableExterne = :table"),
    @NamedQuery(name = "YvsComptaContentJournal.findByExternes", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.refExterne IN :ids AND y.tableExterne = :table"),
    @NamedQuery(name = "YvsComptaContentJournal.findByCreditPiece", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.credit > 0 AND y.piece = :piece"),
    @NamedQuery(name = "YvsComptaContentJournal.findByDebitPiece", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.debit > 0 AND y.piece = :piece"),
    @NamedQuery(name = "YvsComptaContentJournal.findByCreditExterne", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.credit > 0 AND y.refExterne = :id AND y.tableExterne = :table"),
    @NamedQuery(name = "YvsComptaContentJournal.findByDebitExterne", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.debit > 0 AND y.refExterne = :id AND y.tableExterne = :table"),
    @NamedQuery(name = "YvsComptaContentJournal.findByDebitExternes", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.debit > 0 AND y.refExterne IN :ids AND y.tableExterne = :table"),
    @NamedQuery(name = "YvsComptaContentJournal.findByCreditExternes", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.credit > 0 AND y.refExterne IN :ids AND y.tableExterne = :table"),
    
    @NamedQuery(name = "YvsComptaContentJournal.findByCreditPieceDates", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.credit > 0 AND y.piece = :piece AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findByDebitPieceDates", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.debit > 0 AND y.piece = :piece AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findByCreditExterneDates", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.credit > 0 AND y.refExterne = :id AND y.tableExterne = :table AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findByDebitExterneDates", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.debit > 0 AND y.refExterne = :id AND y.tableExterne = :table AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findByDebitExternesDates", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.debit > 0 AND y.refExterne IN :ids AND y.tableExterne = :table AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaContentJournal.findByCreditExternesDates", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.credit > 0 AND y.refExterne IN :ids AND y.tableExterne = :table AND y.piece.datePiece BETWEEN :dateDebut AND :dateFin"),
    
    @NamedQuery(name = "YvsComptaContentJournal.findCompteWithReport", query = "SELECT DISTINCT(y.compteGeneral) FROM YvsComptaContentJournal y WHERE y.compteGeneral.natureCompte.societe=:societe AND y.piece.datePiece <= :dateFin AND y.compteGeneral.typeReport != 'AU' ORDER BY y.compteGeneral.numCompte"),

    @NamedQuery(name = "YvsComptaContentJournal.countPieceByExternes", query = "SELECT COUNT(DISTINCT(y.piece)) FROM YvsComptaContentJournal y WHERE y.refExterne IN :ids AND y.tableExterne = :table"),

    @NamedQuery(name = "YvsComptaContentJournal.findIdPiece", query = "SELECT DISTINCT(y.piece.id) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaContentJournal.findIdPieceByExterne", query = "SELECT DISTINCT(y.piece.id) FROM YvsComptaContentJournal y WHERE y.refExterne = :id AND y.tableExterne = :table"),
    @NamedQuery(name = "YvsComptaContentJournal.findPieceByExterne", query = "SELECT DISTINCT(y.piece) FROM YvsComptaContentJournal y WHERE y.refExterne = :id AND y.tableExterne = :table"),
    @NamedQuery(name = "YvsComptaContentJournal.findPieceByExterneOrder", query = "SELECT DISTINCT(y.piece) FROM YvsComptaContentJournal y WHERE y.refExterne = :id AND y.tableExterne = :table AND y.piece.extourne=false ORDER BY y.piece.datePiece DESC"),
    @NamedQuery(name = "YvsComptaContentJournal.findRevertPieceByExterneOrder", query = "SELECT DISTINCT(y.piece) FROM YvsComptaContentJournal y WHERE y.refExterne = :id AND y.tableExterne = :table AND y.piece.extourne=true ORDER BY y.piece.datePiece DESC"),
    @NamedQuery(name = "YvsComptaContentJournal.findPieceByExternes", query = "SELECT DISTINCT(y.piece) FROM YvsComptaContentJournal y WHERE y.refExterne IN :ids AND y.tableExterne = :table"),

    @NamedQuery(name = "YvsComptaContentJournal.findByLettrage", query = "SELECT y FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.lettrage = :lettrage"),
    @NamedQuery(name = "YvsComptaContentJournal.findMaxLength", query = "SELECT MAX(LENGTH(y.lettrage)) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND y.lettrage IS NOT NULL AND y.piece.exercice = :exercice"),
    @NamedQuery(name = "YvsComptaContentJournal.findMaxLetter", query = "SELECT MAX(y.lettrage) FROM YvsComptaContentJournal y WHERE y.piece.journal.agence.societe = :societe AND LENGTH(y.lettrage) = :length AND y.lettrage IS NOT NULL AND y.piece.exercice = :exercice")})
public class YvsComptaContentJournal extends YvsEntity implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_content_journal_id_seq", name = "yvs_compta_content_journal_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_content_journal_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "jour")
    private Integer jour;
    @Size(max = 2147483647)
    @Column(name = "num_piece")
    private String numPiece;
    @Size(max = 2147483647)
    @Column(name = "num_ref")
    private String numRef;
    @Size(max = 2147483647)
    @Column(name = "lettrage")
    private String lettrage;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "echeance")
    @Temporal(TemporalType.DATE)
    private Date echeance;
    @Size(max = 2147483647)
    @Column(name = "table_externe")
    private String tableExterne;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debit")
    private Double debit;
    @Column(name = "credit")
    private Double credit;
    @Column(name = "ref_externe")
    private Long refExterne;
    @Column(name = "numero")
    private Integer numero;
    @Column(name = "statut")
    private Character statut;
    @Column(name = "table_tiers")
    private String tableTiers;
    @Column(name = "compte_tiers")
    private Long compteTiers;
    @Column(name = "report")
    private Boolean report;

    @JoinColumn(name = "compte_general", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compteGeneral;
    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaPiecesComptable piece;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "contenu")
    private List<YvsComptaContentAnalytique> analytiques;

    @Transient
    private double solde;
    @Transient
    private boolean select;
    @Transient
    private long idX = 100;
    @Transient
    private String error_;
    @Transient
    private String warning_;

    public YvsComptaContentJournal() {
        analytiques = new ArrayList<>();
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaContentJournal(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaContentJournal(long id, int jour, String numPiece, String reference, String libelle, double debit, double credit, Date echeance, YvsBasePlanComptable compteGeneral, Long compteTiers, String tableTiers, long idExterne, String tableExterne, Integer numero) {
        this(id);
        this.jour = jour;
        this.numPiece = numPiece;
        this.numRef = reference;
        this.libelle = libelle;
        this.debit = debit;
        this.credit = credit;
        this.echeance = echeance;
        this.numero = numero;
        this.refExterne = idExterne;
        this.tableExterne = tableExterne;
        if (compteGeneral != null ? (compteGeneral.getId() != null ? compteGeneral.getId() > 0 : false) : false) {
            this.compteGeneral = compteGeneral;
        }
        this.compteTiers = compteTiers;
        this.tableTiers = tableTiers;
    }

    public YvsComptaContentJournal(long id, int jour, String numPiece, String reference, String libelle, double debit, double credit, Date echeance, YvsBasePlanComptable compteGeneral, Long compteTiers, String tableTiers, long idExterne, String tableExterne, Character statut, Integer numero) {
        this(id, jour, numPiece, reference, libelle, debit, credit, echeance, compteGeneral, compteTiers, tableTiers, idExterne, tableExterne, numero);
        this.statut = statut;
    }

    public YvsComptaContentJournal(long id, int jour, String numPiece, String reference, String libelle, double debit, double credit, Date echeance, long compteGeneral, Long compteTiers, String tableTiers, long idExterne, String tableExterne) {
        this(id, jour, numPiece, reference, libelle, debit, credit, echeance, new YvsBasePlanComptable(compteGeneral), compteTiers, tableTiers, idExterne, tableExterne, 0);
    }

    public YvsComptaContentJournal(long id, int jour, String numPiece, String reference, String libelle, double debit, double credit, Date echeance, long compteGeneral, long compteTiers, String tableTiers, long idExterne, String tableExterne, Character statut, Integer numero) {
        this(id, jour, numPiece, reference, libelle, debit, credit, echeance, new YvsBasePlanComptable(compteGeneral), compteTiers, tableTiers, idExterne, tableExterne, statut, numero);
    }

    public YvsComptaContentJournal(YvsComptaContentJournal c) {
        this.id = c.id;
        this.dateUpdate = c.dateUpdate;
        this.dateSave = c.dateSave;
        this.jour = c.jour;
        this.numPiece = c.numPiece;
        this.numRef = c.numRef;
        this.lettrage = c.lettrage;
        this.libelle = c.libelle;
        this.echeance = c.echeance;
        this.tableExterne = c.tableExterne;
        this.debit = c.debit;
        this.credit = c.credit;
        this.refExterne = c.refExterne;
        this.numero = c.numero;
        this.statut = c.statut;
        this.tableTiers = c.tableTiers;
        this.compteTiers = c.compteTiers;
        this.compteGeneral = c.compteGeneral;

        this.piece = c.piece;
        this.author = c.author;
        this.analytiques = c.analytiques;
        this.solde = c.solde;
        this.select = c.select;
        this.error_ = c.error_;
        this.warning_ = c.warning_;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getLettrage() {
        return lettrage;
    }

    public void setLettrage(String lettrage) {
        this.lettrage = lettrage;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public double getSolde() {
        solde = getDebit() - getCredit();
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public String getTableExterne() {
        return tableExterne != null ? tableExterne : "";
    }

    public void setTableExterne(String tableExterne) {
        this.tableExterne = tableExterne;
    }

    public Integer getJour() {
        return jour != null ? jour : 0;
    }

    public void setJour(Integer jour) {
        this.jour = jour;
    }

    public String getNumPiece() {
        return numPiece != null ? numPiece : "";
    }

    public void setNumPiece(String numPiece) {
        this.numPiece = numPiece;
    }

    public String getNumRef() {
        return numRef != null ? numRef : "";
    }

    public void setNumRef(String numRef) {
        this.numRef = numRef;
    }

    public String getLibelle() {
        return libelle != null ? libelle : "";
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Date getEcheance() {
        return echeance != null ? echeance : new Date();
    }

    public void setEcheance(Date echeance) {
        this.echeance = echeance;
    }

    public Long getRefExterne() {
        return refExterne != null ? refExterne : 0;
    }

    public void setRefExterne(Long refExterne) {
        this.refExterne = refExterne;
    }

    public YvsBasePlanComptable getCompteGeneral() {
        return compteGeneral;
    }

    public void setCompteGeneral(YvsBasePlanComptable compteGeneral) {
        this.compteGeneral = compteGeneral;
    }

    public Long getCompteTiers() {
        return compteTiers;
    }

    public void setCompteTiers(Long compteTiers) {
        this.compteTiers = compteTiers;
    }

    public String getTableTiers() {
        return tableTiers;
    }

    public void setTableTiers(String tableTiers) {
        this.tableTiers = tableTiers;
    }

    public YvsComptaPiecesComptable getPiece() {
        return piece;
    }

    public void setPiece(YvsComptaPiecesComptable piece) {
        this.piece = piece;
    }

    public Double getDebit() {
        return debit != null ? debit : 0;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit != null ? credit : 0;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Integer getNumero() {
        return numero != null ? numero : 0;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Boolean getReport() {
        return report != null ? report : false;
    }

    public void setReport(Boolean report) {
        this.report = report;
    }

    public List<YvsComptaContentAnalytique> getAnalytiques() {
        return analytiques;
    }

    public void setAnalytiques(List<YvsComptaContentAnalytique> analytiques) {
        this.analytiques = analytiques;
    }

    public long getIdX() {
        return idX;
    }

    public void setIdX(long idX) {
        this.idX = idX;
    }

    public Character getStatut() {
        return statut != null ? String.valueOf(statut).trim().length() > 0 ? statut : Constantes.STATUT_DOC_VALIDE : Constantes.STATUT_DOC_VALIDE;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public double getTotalRepartitionCredit() {
        double re = 0;
        for (YvsComptaContentAnalytique ca : analytiques) {
            re += ca.getCredit();
        }
        return re;
    }

    public double getTotalRepartitionDebit() {
        double re = 0;
        for (YvsComptaContentAnalytique ca : analytiques) {
            re += ca.getDebit();
        }
        return re;
    }

    @XmlTransient
    @JsonIgnore
    public String getError_() {
        return error_;
    }

    public void setError_(String error_) {
        this.error_ = error_;
    }

    @XmlTransient
    @JsonIgnore
    public String getWarning_() {
        return warning_;
    }

    public void setWarning_(String warning_) {
        this.warning_ = warning_;
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
        if (!(object instanceof YvsComptaContentJournal)) {
            return false;
        }
        YvsComptaContentJournal other = (YvsComptaContentJournal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaContentJournal[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
        if (o != null) {
            YvsComptaContentJournal y = (YvsComptaContentJournal) o;
            if (y.getNumero().equals(getNumero())) {
                if (y.getDebit().equals(getDebit())) {
                    if (y.getCredit().equals(getCredit())) {
                        return y.getId().compareTo(getId());
                    }
                    return y.getCredit().compareTo(getCredit());
                }
                return y.getDebit().compareTo(getDebit());
            }
            return getNumero().compareTo(y.getNumero());
        }
        return 0;
    }

    public String orderBy(String orderBy) {
        if (orderBy != null) {
            if (orderBy.equals("T")) {
                return compteTiers != null ? compteTiers + "" : "";
            } else if (orderBy.equals("C")) {
                return compteGeneral != null ? compteGeneral.getNumCompte() : "";
            }
        }
        return numPiece;
    }

}

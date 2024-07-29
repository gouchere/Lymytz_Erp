/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.vente;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.salaire.service.Constantes;
import static yvs.dao.salaire.service.Constantes.dfs;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.vente.YvsComptaNotifVersementVente;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_entete_doc_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComEnteteDocVente.findByAll", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.agence.societe = :societe ORDER BY y.dateEntete DESC"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByAllC", query = "SELECT COUNT(y) FROM YvsComEnteteDocVente y WHERE y.agence.societe = :societe"),
    @NamedQuery(name = "YvsComEnteteDocVente.findById", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByHeader", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.creneau = :creno AND y.dateEntete=:date AND y.etat!=:etat"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByDateEntete", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.dateEntete = :dateEntete"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByCrenauDate", query = "SELECT y FROM YvsComEnteteDocVente y JOIN FETCH y.agence JOIN FETCH y.creneau JOIN FETCH y.agence.societe "
            + "LEFT JOIN FETCH y.creneau.creneauPoint LEFT JOIN FETCH y.creneau.creneauDepot WHERE y.dateEntete = :date AND y.creneau.id = :creanau"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByEtat", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.etat = :etat"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByUsers_", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.creneau.users = :users ORDER BY y.dateEntete"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByUsersC_", query = "SELECT COUNT(y) FROM YvsComEnteteDocVente y WHERE y.creneau.users = :users"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByUsersStatut_", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.creneau.users = :users AND y.etat = :statut ORDER BY y.dateEntete"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByUsersStatutC_", query = "SELECT COUNT(y) FROM YvsComEnteteDocVente y WHERE y.creneau.users = :users AND y.etat = :statut"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByUsersDates_", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.creneau.users = :users AND y.dateEntete BETWEEN :dateDebut AND :dateFin ORDER BY y.dateEntete"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByUsersDatesC_", query = "SELECT COUNT(y) FROM YvsComEnteteDocVente y WHERE y.creneau.users = :users AND y.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByUsersStatutDates_", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.creneau.users = :users AND y.etat = :statut AND y.dateEntete BETWEEN :dateDebut AND :dateFin ORDER BY y.dateEntete"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByUsersStatutDatesC_", query = "SELECT COUNT(y) FROM YvsComEnteteDocVente y WHERE y.creneau.users = :users AND y.etat = :statut AND y.dateEntete BETWEEN :dateDebut AND :dateFin"),

    @NamedQuery(name = "YvsComEnteteDocVente.findEncourByUsers_", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.creneau.users = :users AND y.etat NOT IN :etats AND y.dateEntete>=:date AND y.cloturer=false ORDER BY y.dateEntete DESC"),
    @NamedQuery(name = "YvsComEnteteDocVente.findVendeur", query = "SELECT DISTINCT y.creneau.users FROM YvsComEnteteDocVente y WHERE y.agence.societe = :societe ORDER BY y.creneau.users.nomUsers"),

    @NamedQuery(name = "YvsComEnteteDocVente.findOneFiche", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.dateEntete = :date AND y.creneau=:creno"),
    @NamedQuery(name = "YvsComEnteteDocVente.countByCreneau", query = "SELECT COUNT(y) FROM YvsComEnteteDocVente y WHERE y.creneau = :creneau"),
    @NamedQuery(name = "YvsComEnteteDocVente.countFicheOpenByUsers", query = "SELECT COUNT(y) FROM YvsComEnteteDocVente y WHERE y.creneau.users = :users AND y.cloturer=FALSE"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByDepotTrancheUsersDate", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.creneau.creneauDepot.depot = :depot AND y.creneau.creneauDepot.tranche = :tranche AND y.creneau.users = :users AND y.dateEntete = :date"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByDepotPointTrancheUsersDate", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.creneau.creneauDepot.depot = :depot AND y.creneau.creneauPoint.point = :point AND y.creneau.creneauDepot.tranche = :tranche AND y.creneau.users = :users AND y.dateEntete = :date")})
public class YvsComEnteteDocVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_entete_doc_vente_id_seq", name = "yvs_com_entete_doc_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_entete_doc_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_entete")
    @Temporal(TemporalType.DATE)
    private Date dateEntete;
    @Column(name = "date_cloturer")
    @Temporal(TemporalType.DATE)
    private Date dateCloturer;
    @Column(name = "date_valider")
    @Temporal(TemporalType.DATE)
    private Date dateValider;
    @Size(max = 2147483647)
    @Column(name = "etat")
    private String etat;
    @Size(max = 2147483647)
    @Column(name = "statut_livre")
    private String statutLivre;
    @Size(max = 2147483647)
    @Column(name = "statut_regle")
    private String statutRegle;
    @Column(name = "cloturer")
    private Boolean cloturer;
    @Column(name = "comptabilise")
    private Boolean comptabilise;

    @JoinColumn(name = "creneau", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComCreneauHoraireUsers creneau;
    @JoinColumn(name = "cloturer_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers cloturerBy;
    @JoinColumn(name = "valider_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers validerBy;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;

    @OneToMany(mappedBy = "enteteDoc", fetch = FetchType.LAZY)
    private List<YvsComDocVentes> documents;
    @OneToMany(mappedBy = "enteteDoc", fetch = FetchType.LAZY)
    private List<YvsComptaNotifVersementVente> versements;
    
    @Transient
    private YvsComptaJournaux journal;
    @Transient
    private List<YvsComEcartEnteteVente> ecarts;
    @Transient
    private String reference;
    @Transient
    private String message;
    @Transient
    private String libelleStatut;
    @Transient
    private String maDateDoc;
    @Transient
    private List<YvsComDocVentes> factures;
    @Transient
    private List<YvsComptaCaissePieceVirement> virements;
    @Transient
    private List<YvsBaseCaisse> caisses;
    @Transient
    private String numero;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private boolean force;
    @Transient
    private boolean errorComptabilise;
    @Transient
    private boolean comptabilised;
    @Transient
    private boolean synchroniser;
    @Transient
    private long caisse;
    @Transient
    private long totalWait;
    @Transient
    private long totalCours;
    @Transient
    private long totalRegle;
    @Transient
    private long totalLivre;
    @Transient
    private double montantTotal;
    @Transient
    private double versementAttendu;
    @Transient
    private double versementReel;
    @Transient
    private double versementWait;
    @Transient
    private double ecart;
    @Transient
    private double ecartPlanifier;
    @Transient
    private double avanceCmde;
    @Transient
    private double avanceFact;
    @Transient
    private double totalCmde;
    @Transient
    private double totalFact;

    public YvsComEnteteDocVente() {
        ecarts = new ArrayList<>();
        caisses = new ArrayList<>();
        factures = new ArrayList<>();
        documents = new ArrayList<>();
        versements = new ArrayList<>();
        virements = new ArrayList<>();
    }

    public YvsComEnteteDocVente(Long id) {
        this();
        this.id = id;
    }

    public YvsComEnteteDocVente(Long id, Date dateEntete) {
        this(id);
        this.dateEntete = dateEntete;
    }

    public YvsComEnteteDocVente(Long id, String reference) {
        this(id);
        this.reference = reference;
    }

    public YvsComEnteteDocVente(YvsComEnteteDocVente y) {
        this.id = y.id;
        this.dateUpdate = y.dateUpdate;
        this.dateSave = y.dateSave;
        this.dateEntete = y.dateEntete;
        this.dateCloturer = y.dateCloturer;
        this.dateValider = y.dateValider;
        this.etat = y.etat;
        this.statutLivre = y.statutLivre;
        this.statutRegle = y.statutRegle;
        this.cloturer = y.cloturer;
        this.creneau = y.creneau;
        this.cloturerBy = y.cloturerBy;
        this.validerBy = y.validerBy;
        this.author = y.author;
        this.agence = y.agence;
        this.documents = y.documents;
        this.versements = y.versements;
        this.reference = y.reference;
        this.factures = y.factures;
        this.numero = y.numero;
        this.comptabilise = y.comptabilise;
        this.new_ = y.new_;
        this.select = y.select;
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

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public String getNumero() {
        numero = "";
        if (creneau != null ? creneau.getId() > 0 : false) {
            YvsUsers u = creneau.getUsers();
            if (u != null ? u.getId() > 0 : false) {
                numero = u.getCodeUsers() + "/";
                if (creneau.getCreneauPoint() != null ? creneau.getCreneauPoint().getId() > 0 : false) {
                    YvsBasePointVente p = creneau.getCreneauPoint().getPoint();
                    if (p != null ? p.getId() > 0 : false) {
                        numero += p.getCode() + "/";
                    }
                } else {
                    if (creneau.getCreneauDepot() != null ? creneau.getCreneauDepot().getId() > 0 : false) {
                        YvsBaseDepots p = creneau.getCreneauDepot().getDepot();
                        if (p != null ? p.getId() > 0 : false) {
                            numero += p.getCode() + "/";
                        }
                    }
                }
            }
        }
        if (dateEntete != null) {
            numero += new SimpleDateFormat("ddMMyy").format(dateEntete);
        }
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getReference() {
        reference = "";
        if (creneau != null ? creneau.getId() > 0 : false) {
            YvsUsers u = creneau.getUsers();
            if (u != null ? u.getId() > 0 : false) {
                reference = u.getNomUsers() + " / ";
                if (creneau.getCreneauPoint() != null ? creneau.getCreneauPoint().getId() > 0 : false) {
                    YvsBasePointVente p = creneau.getCreneauPoint().getPoint();
                    if (p != null ? p.getId() > 0 : false) {
                        reference += p.getLibelle() + " / ";
                    }
                } else {
                    if (creneau.getCreneauDepot() != null ? creneau.getCreneauDepot().getId() > 0 : false) {
                        YvsBaseDepots p = creneau.getCreneauDepot().getDepot();
                        if (p != null ? p.getId() > 0 : false) {
                            reference += p.getDesignation() + " / ";
                        }
                    }
                }
            }
        }
        if (dateEntete != null) {
            reference += new SimpleDateFormat("dd MMM yyyy").format(dateEntete);
        }
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateValider() {
        return dateValider;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

    public String getStatutLivre() {
        return statutLivre;
    }

    public void setStatutLivre(String statutLivre) {
        this.statutLivre = statutLivre;
    }

    public String getStatutRegle() {
        return statutRegle;
    }

    public void setStatutRegle(String statutRegle) {
        this.statutRegle = statutRegle;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateCloturer() {
        return dateCloturer;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateCloturer(Date dateCloturer) {
        this.dateCloturer = dateCloturer;
    }

    public Boolean getCloturer() {
        return cloturer != null ? cloturer : false;
    }

    public void setCloturer(Boolean cloturer) {
        this.cloturer = cloturer;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateEntete() {
        return dateEntete != null ? dateEntete : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public void setDateEntete(Date dateEntete) {
        this.dateEntete = dateEntete;
    }

    public String getEtat() {
        return etat != null ? etat.trim().length() > 0 ? etat : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setEtat(String etat) {
        this.etat = etat;
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

    public boolean isSynchroniser() {
        return synchroniser;
    }

    public void setSynchroniser(boolean synchroniser) {
        this.synchroniser = synchroniser;
    }

    public String getMaDateDoc() {
        return getDateEntete() != null ? dfs.format(getDateEntete()) : "";
    }

    public void setMaDateDoc(String maDateDoc) {
        this.maDateDoc = maDateDoc;
    }

    public long getTotalWait() {
        return totalWait;
    }

    public void setTotalWait(long totalWait) {
        this.totalWait = totalWait;
    }

    public long getTotalCours() {
        return totalCours;
    }

    public void setTotalCours(long totalCours) {
        this.totalCours = totalCours;
    }

    public long getTotalRegle() {
        return totalRegle;
    }

    public void setTotalRegle(long totalRegle) {
        this.totalRegle = totalRegle;
    }

    public long getTotalLivre() {
        return totalLivre;
    }

    public void setTotalLivre(long totalLivre) {
        this.totalLivre = totalLivre;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getLibelleStatut() {
        return libelleStatut;
    }

    public void setLibelleStatut(String libelleStatut) {
        this.libelleStatut = libelleStatut;
    }

    @XmlTransient
    @JsonIgnore
    public double getAvanceCmde() {
        return avanceCmde;
    }

    public void setAvanceCmde(double avanceCmde) {
        this.avanceCmde = avanceCmde;
    }

    @XmlTransient
    @JsonIgnore
    public double getAvanceFact() {
        return avanceFact;
    }

    public void setAvanceFact(double avanceFact) {
        this.avanceFact = avanceFact;
    }

    @XmlTransient
    @JsonIgnore
    public double getTotalCmde() {
        return totalCmde;
    }

    public void setTotalCmde(double totalCmde) {
        this.totalCmde = totalCmde;
    }

    @XmlTransient
    @JsonIgnore
    public double getTotalFact() {
        return totalFact;
    }

    @XmlTransient
    @JsonIgnore
    public void setTotalFact(double totalFact) {
        this.totalFact = totalFact;
    }

    @XmlTransient
    @JsonIgnore
    public double getVersementWait() {
        return versementWait;
    }

    public void setVersementWait(double versementWait) {
        this.versementWait = versementWait;
    }

    @XmlTransient
    @JsonIgnore
    public double getVersementAttendu() {
        return versementAttendu;
    }

    public void setVersementAttendu(double versementAttendu) {
        this.versementAttendu = versementAttendu;
    }

    @XmlTransient
    @JsonIgnore
    public double getVersementReel() {
        return versementReel;
    }

    public void setVersementReel(double versementReel) {
        this.versementReel = versementReel;
    }

    @XmlTransient
    @JsonIgnore
    public double getEcart() {
        return ecart;
    }

    public void setEcart(double ecart) {
        this.ecart = ecart;
    }

    @XmlTransient
    @JsonIgnore
    public double getEcartPlanifier() {
        return ecartPlanifier;
    }

    public void setEcartPlanifier(double ecartPlanifier) {
        this.ecartPlanifier = ecartPlanifier;
    }

    @XmlTransient
    @JsonIgnore
    public long getCaisse() {
        return caisse;
    }

    public void setCaisse(long caisse) {
        this.caisse = caisse;
    }

    public YvsUsers getValiderBy() {
        return validerBy;
    }

    public void setValiderBy(YvsUsers validerBy) {
        this.validerBy = validerBy;
    }

    public YvsComCreneauHoraireUsers getCreneau() {
        return creneau;
    }

    public void setCreneau(YvsComCreneauHoraireUsers creneau) {
        this.creneau = creneau;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsers getCloturerBy() {
        return cloturerBy;
    }

    public void setCloturerBy(YvsUsers cloturerBy) {
        this.cloturerBy = cloturerBy;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComDocVentes> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocVentes> documents) {
        this.documents = documents;
    }

    @XmlTransient
    @JsonIgnore()
    public List<YvsComEcartEnteteVente> getEcarts() {
        return ecarts;
    }

    public void setEcarts(List<YvsComEcartEnteteVente> ecarts) {
        this.ecarts = ecarts;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComDocVentes> getFactures() {
        return factures;
    }

    public void setFactures(List<YvsComDocVentes> factures) {
        this.factures = factures;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseCaisse> getCaisses() {
        return caisses;
    }

    public void setCaisses(List<YvsBaseCaisse> caisses) {
        this.caisses = caisses;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaNotifVersementVente> getVersements() {
        return versements;
    }

    public void setVersements(List<YvsComptaNotifVersementVente> versements) {
        this.versements = versements;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceVirement> getVirements() {
        return virements;
    }

    public void setVirements(List<YvsComptaCaissePieceVirement> virements) {
        this.virements = virements;
    }

    public YvsComptaJournaux getJournal() {
        return journal;
    }

    public void setJournal(YvsComptaJournaux journal) {
        this.journal = journal;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
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
        if (!(object instanceof YvsComEnteteDocVente)) {
            return false;
        }
        YvsComEnteteDocVente other = (YvsComEnteteDocVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "YvsComEnteteDocVente{" + "id=" + id + '}';
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComDocVentes> loadFactures() {
        List<YvsComDocVentes> list = new ArrayList<>();
        if (documents != null ? !documents.isEmpty() : false) {
            for (YvsComDocVentes v : documents) {
                if (v.getTypeDoc().equals("FV")) {
                    list.add(v);
                }
            }
        }
        return list;
    }

    public boolean canEditable() {
        return etat.equals(Constantes.ETAT_ATTENTE) || etat.equals(Constantes.ETAT_EDITABLE);
    }

    public boolean canDelete() {
        return etat.equals(Constantes.ETAT_ATTENTE) || etat.equals(Constantes.ETAT_EDITABLE) || etat.equals(Constantes.ETAT_SUSPENDU) || etat.equals(Constantes.ETAT_ANNULE);
    }

    public boolean canComptabilise() {
        if (getFactures() != null ? !getFactures().isEmpty() : false) {
            for (YvsComDocVentes v : getFactures()) {
                if (Constantes.ETAT_VALIDE.equals(v.getStatut())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

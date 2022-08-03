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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseCaisseUser;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_base_caisse")
@NamedQueries({
    @NamedQuery(name = "YvsBaseCaisse.findAll", query = "SELECT y FROM YvsBaseCaisse y WHERE y.journal.agence.societe=:societe ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findByAgence", query = "SELECT y FROM YvsBaseCaisse y WHERE y.journal.agence=:agence ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findAllActif", query = "SELECT y FROM YvsBaseCaisse y WHERE y.journal.agence.societe=:societe AND y.actif = true ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findAllPrincipalActif", query = "SELECT y FROM YvsBaseCaisse y WHERE y.journal.agence.societe=:societe AND y.actif = true AND y.principal=true ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findAllC", query = "SELECT COUNT(y) FROM YvsBaseCaisse y WHERE y.journal.agence.societe=:societe"),
    @NamedQuery(name = "YvsBaseCaisse.findById", query = "SELECT y FROM YvsBaseCaisse y JOIN FETCH y.journal LEFT JOIN FETCH y.caissier LEFT JOIN FETCH y.codeAcces WHERE y.id = :id ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findCanNegative", query = "SELECT y.canNegative FROM YvsBaseCaisse y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseCaisse.findByCode", query = "SELECT y FROM YvsBaseCaisse y WHERE y.code = :code"),
    @NamedQuery(name = "YvsBaseCaisse.findDefault", query = "SELECT y FROM YvsBaseCaisse y WHERE y.defaultCaisse=true AND y.journal.agence=:agence ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findDefaultBySociete", query = "SELECT y FROM YvsBaseCaisse y WHERE y.defaultCaisse=true AND y.journal.agence.societe=:societe ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findByIntitule", query = "SELECT y FROM YvsBaseCaisse y WHERE y.intitule = :intitule ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findByAdresse", query = "SELECT y FROM YvsBaseCaisse y WHERE y.adresse = :adresse ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findByJournal", query = "SELECT y FROM YvsBaseCaisse y WHERE y.journal = :journal ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findByResponsable", query = "SELECT y FROM YvsBaseCaisse y WHERE y.responsable = :responsable ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findByCaissier", query = "SELECT y FROM YvsBaseCaisse y JOIN FETCH y.compte WHERE (y.caissier = :caissier OR y.responsable=:responsable) AND y.journal.agence.societe=:societe ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findByUsers", query = "SELECT y FROM YvsBaseCaisse y WHERE y.caissier = :caissier ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findByActif", query = "SELECT y FROM YvsBaseCaisse y WHERE  y.actif = :actif ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findByVenteOnline", query = "SELECT y FROM YvsBaseCaisse y WHERE y.journal.agence.societe = :societe AND y.venteOnline = :venteOnline ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findByAccesAgence", query = "SELECT y FROM YvsBaseCaisse y WHERE  y.codeAcces.id IN :acces AND y.journal.agence=:agence ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findByAccesAgenceCaissier", query = "SELECT y FROM YvsBaseCaisse y WHERE (y.codeAcces.id IN :acces OR y.caissier = :caissier) AND y.journal.agence=:agence ORDER BY y.intitule"),

    @NamedQuery(name = "YvsBaseCaisse.findAllForUsers", query = "SELECT DISTINCT y FROM YvsBaseCaisse y LEFT JOIN y.codeAcces c LEFT JOIN y.responsable e WHERE (c.id IS NOT NULL AND c.id IN :codes) OR y.id IN :ids OR y.caissier = :caissier OR (e.id IS NOT NULL AND COALESCE(:responsable, 0) > 0 AND e.id = :responsable) ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findIdAllForUsers", query = "SELECT DISTINCT y.id FROM YvsBaseCaisse y LEFT JOIN y.codeAcces c LEFT JOIN y.responsable e WHERE (c.id IS NOT NULL AND c.id IN :codes) OR y.id IN :ids OR y.caissier = :caissier OR (e.id IS NOT NULL AND COALESCE(:responsable, 0) > 0 AND e.id = :responsable) ORDER BY y.intitule")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseCaisse extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_caisse_id_seq", name = "yvs_base_caisse_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_caisse_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "intitule")
    private String intitule;
    @Column(name = "code")
    private String code;
    @Column(name = "type_caisse")
    private String typeCaisse;
    @Size(max = 2147483647)
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "principal")
    private Boolean principal;
    @Column(name = "can_negative")
    private Boolean canNegative;
    @Column(name = "default_caisse")
    private Boolean defaultCaisse;
    @Column(name = "give_billetage")
    private Boolean giveBilletage;
    @Column(name = "vente_online")
    private Boolean venteOnline;

    @JoinColumn(name = "journal", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaJournaux journal;
    @JoinColumn(name = "code_acces", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCodeAcces codeAcces;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes responsable;
    @JoinColumn(name = "caissier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers caissier;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse parent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mode_reg_defaut", referencedColumnName = "id")
    private YvsBaseModeReglement modeRegDefaut; //indique le mode de règlement par défaut dans cette caisse
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compte;

    @OneToMany(mappedBy = "caisseSource", fetch = FetchType.LAZY)
    private List<YvsBaseLiaisonCaisse> caisseLiees;
    @OneToMany(mappedBy = "caisse", fetch = FetchType.LAZY)
    private List<YvsBaseComptesCaisse> othersCompte;
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<YvsBaseCaisse> subCaisses;
    @OneToMany(mappedBy = "caisse", fetch = FetchType.LAZY)
    private List<YvsComptaCaissePieceMission> piecesMission;
    @OneToMany(mappedBy = "caisse", fetch = FetchType.LAZY)
    private List<YvsComptaCaissePieceAchat> piecesAchat;
    @OneToMany(mappedBy = "caisse",fetch = FetchType.LAZY)
    private List<YvsComptaCaissePieceVente> piecesVente;
    @OneToMany(mappedBy = "caisse", fetch = FetchType.LAZY)
    private List<YvsComptaCaissePieceDivers> piecesDivers;
    @OneToMany(mappedBy = "source", fetch = FetchType.LAZY)
    private List<YvsComptaCaissePieceVirement> piecesVirement;
    @OneToMany(mappedBy = "caisse", fetch = FetchType.LAZY)
    private List<YvsComptaMouvementCaisse> mouvements;
    @OneToMany(mappedBy = "cible", fetch = FetchType.LAZY)
    private List<YvsComptaCaissePieceVirement> cibles;
    @OneToMany(mappedBy = "idCaisse", fetch = FetchType.LAZY)
    private List<YvsBaseCaisseUser> yvsBaseCaisseUserList;

    @Transient
    private boolean select;
    @Transient
    private double solde;
    @Transient
    private double versement;
    @Transient
    private double virement;
    @Transient
    private double recette;
    @Transient
    private double depense;
    @Transient
    private double ecart;
    @Transient
    private List<YvsBaseModeReglement> modes;

    public YvsBaseCaisse() {
        caisseLiees = new ArrayList<>();
        modes = new ArrayList<>();
    }

    public YvsBaseCaisse(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseCaisse(Long id, YvsComptaJournaux journal) {
        this(id);
        this.journal = journal;
    }

    public YvsBaseCaisse(Long id, String intitule) {
        this(id);
        this.intitule = intitule;
    }

    public YvsBaseCaisse(Long id, String intitule, boolean canNegative) {
        this(id, intitule);
        this.canNegative = canNegative;
    }

    public YvsBaseCaisse(YvsBaseCaisse y) {
        this(y.id);
        this.dateUpdate = y.dateUpdate;
        this.dateSave = y.dateSave;
        this.intitule = y.intitule;
        this.code = y.code;
        this.typeCaisse = y.typeCaisse;
        this.adresse = y.adresse;
        this.journal = y.journal;
        this.actif = y.actif;
        this.principal = y.principal;
        this.canNegative = y.canNegative;
        this.defaultCaisse = y.defaultCaisse;
        this.giveBilletage = y.giveBilletage;
        this.codeAcces = y.codeAcces;
        this.author = y.author;
        this.responsable = y.responsable;
        this.caissier = y.caissier;
        this.parent = y.parent;
        this.modeRegDefaut = y.modeRegDefaut;
        this.compte = y.compte;
        this.select = y.select;
    }

    public String getCode() {
        return code != null ? code : "";
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public YvsComptaJournaux getJournal() {
        return journal;
    }

    public void setJournal(YvsComptaJournaux journal) {
        this.journal = journal;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getVenteOnline() {
        return venteOnline != null ? venteOnline : false;
    }

    public void setVenteOnline(Boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhEmployes getResponsable() {
        return responsable;
    }

    public void setResponsable(YvsGrhEmployes responsable) {
        this.responsable = responsable;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseCaisse> getSubCaisses() {
        return subCaisses;
    }

    public YvsBaseCodeAcces getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(YvsBaseCodeAcces codeAcces) {
        this.codeAcces = codeAcces;
    }

    public void setSubCaisses(List<YvsBaseCaisse> subCaisses) {
        this.subCaisses = subCaisses;
    }

    public YvsBaseCaisse getParent() {
        return parent;
    }

    public void setParent(YvsBaseCaisse parent) {
        this.parent = parent;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceMission> getPiecesMission() {
        return piecesMission;
    }

    public void setPiecesMission(List<YvsComptaCaissePieceMission> piecesMission) {
        this.piecesMission = piecesMission;
    }

    public YvsBasePlanComptable getCompte() {
        return compte;
    }

    public void setCompte(YvsBasePlanComptable compte) {
        this.compte = compte;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceVirement> getCibles() {
        return cibles;
    }

    public void setCibles(List<YvsComptaCaissePieceVirement> cibles) {
        this.cibles = cibles;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceVirement> getPiecesVirement() {
        return piecesVirement;
    }

    public void setPiecesVirement(List<YvsComptaCaissePieceVirement> piecesVirement) {
        this.piecesVirement = piecesVirement;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaMouvementCaisse> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<YvsComptaMouvementCaisse> mouvements) {
        this.mouvements = mouvements;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceAchat> getPiecesAchat() {
        return piecesAchat;
    }

    public void setPiecesAchat(List<YvsComptaCaissePieceAchat> piecesAchat) {
        this.piecesAchat = piecesAchat;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceVente> getPiecesVente() {
        return piecesVente;
    }

    public void setPiecesVente(List<YvsComptaCaissePieceVente> piecesVente) {
        this.piecesVente = piecesVente;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceDivers> getPiecesDivers() {
        return piecesDivers;
    }

    public void setPiecesDivers(List<YvsComptaCaissePieceDivers> piecesDivers) {
        this.piecesDivers = piecesDivers;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseModeReglement getModeRegDefaut() {
        return modeRegDefaut;
    }

    public void setModeRegDefaut(YvsBaseModeReglement modeRegDefaut) {
        this.modeRegDefaut = modeRegDefaut;
    }

    public String getTypeCaisse() {
        return typeCaisse != null ? typeCaisse : "CAISSE";
    }

    public void setTypeCaisse(String typeCaisse) {
        this.typeCaisse = typeCaisse;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseComptesCaisse> getOthersCompte() {
        return othersCompte;
    }

    public void setOthersCompte(List<YvsBaseComptesCaisse> othersCompte) {
        this.othersCompte = othersCompte;
    }

    public Boolean getCanNegative() {
        return canNegative != null ? canNegative : false;
    }

    public void setCanNegative(Boolean canNegative) {
        this.canNegative = canNegative;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseLiaisonCaisse> getCaisseLiees() {
        return caisseLiees;
    }

    public void setCaisseLiees(List<YvsBaseLiaisonCaisse> caisseLiees) {
        this.caisseLiees = caisseLiees;
    }

    public Boolean getDefaultCaisse() {
        return defaultCaisse != null ? defaultCaisse : false;
    }

    public void setDefaultCaisse(Boolean defaultCaisse) {
        this.defaultCaisse = defaultCaisse;
    }

    public Boolean getGiveBilletage() {
        return giveBilletage != null ? giveBilletage : false;
    }

    public void setGiveBilletage(Boolean giveBilletage) {
        this.giveBilletage = giveBilletage;
    }

    public YvsUsers getCaissier() {
        return caissier;
    }

    public void setCaissier(YvsUsers caissier) {
        this.caissier = caissier;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Boolean getPrincipal() {
        return principal != null ? principal : false;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseModeReglement> getModes() {
        return modes;
    }

    public void setModes(List<YvsBaseModeReglement> modes) {
        this.modes = modes;
    }

    public double getVirement() {
        return virement;
    }

    public void setVirement(double virement) {
        this.virement = virement;
    }

    public double getVersement() {
        return versement;
    }

    public void setVersement(double versement) {
        this.versement = versement;
    }

    public double getRecette() {
        return recette;
    }

    public void setRecette(double recette) {
        this.recette = recette;
    }

    public double getDepense() {
        return depense;
    }

    public void setDepense(double depense) {
        this.depense = depense;
    }

    public double getEcart() {
        return ecart;
    }

    public void setEcart(double ecart) {
        this.ecart = ecart;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseCaisseUser> getYvsBaseCaisseUserList() {
        return yvsBaseCaisseUserList;
    }

    public void setYvsBaseCaisseUserList(List<YvsBaseCaisseUser> yvsBaseCaisseUserList) {
        this.yvsBaseCaisseUserList = yvsBaseCaisseUserList;
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
        if (!(object instanceof YvsBaseCaisse)) {
            return false;
        }
        YvsBaseCaisse other = (YvsBaseCaisse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return intitule;
    }

    public double getSoldeCaisse(DaoInterfaceLocal dao) {
        if (id >= 0) {
            return dao.getRecetteCaisse(id, null, Constantes.STATUT_DOC_PAYER) - dao.getDepenseCaisse(id, null, Constantes.STATUT_DOC_PAYER);
        } else {
            return 0d;
        }
    }
}

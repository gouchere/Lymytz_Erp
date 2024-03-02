/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

import java.io.Serializable;
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
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Calendar;
import yvs.entity.grh.activite.YvsGrhCongeEmps;
import yvs.entity.grh.activite.YvsContactsEmps;
import yvs.entity.grh.taches.YvsGrhRegleTache;
import yvs.entity.param.YvsAgences;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.ext.YvsExtEmploye;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.param.YvsGrhConventionCollective;
import yvs.entity.grh.param.YvsGrhStatutEmploye;
import yvs.entity.grh.param.poste.YvsGrhPosteDeTravail;
import yvs.entity.grh.param.poste.YvsGrhPosteEmployes;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.grh.presence.YvsGrhEquipeEmploye;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_grh_employes")
@NamedQueries({
    @NamedQuery(name = "YvsGrhEmployes.findAll", query = "SELECT y FROM YvsGrhEmployes y WHERE y.agence=:agence AND y.actif=true ORDER BY y.nom, y.prenom"),
    @NamedQuery(name = "YvsGrhEmployes.countAll", query = "SELECT COUNT(y) FROM YvsGrhEmployes y WHERE y.agence.societe = :societe AND y.actif=true"),
    @NamedQuery(name = "YvsGrhEmployes.findCountAll", query = "SELECT COUNT(y) FROM YvsGrhEmployes y WHERE y.agence=:agence AND y.actif=true "),
//ordonne les employé de l'agence résultat par matricule
    @NamedQuery(name = "YvsGrhEmployes.findAllMat", query = "SELECT y FROM YvsGrhEmployes y WHERE y.agence.societe=:societe AND y.actif=true ORDER BY y.matricule"),
    //récupère les employé d'une société
    @NamedQuery(name = "YvsGrhEmployes.findAlls", query = "SELECT y FROM YvsGrhEmployes y WHERE y.agence.societe =:societe AND y.actif=true  ORDER BY y.agence.id, y.nom"),
    @NamedQuery(name = "YvsGrhEmployes.findCountAlls", query = "SELECT COUNT(y) FROM YvsGrhEmployes y WHERE y.agence.societe=:societe AND y.actif=true "),
    //employés ayant un code user donées ou un matricule   
    @NamedQuery(name = "YvsGrhEmployes.findByMatricule", query = "SELECT y FROM YvsGrhEmployes y WHERE y.matricule = :matricule OR y.codeUsers.codeUsers=:matricule"),
    @NamedQuery(name = "YvsGrhEmployes.findByReference", query = "SELECT y FROM YvsGrhEmployes y WHERE y.matricule = :matricule AND y.agence.societe = :societe"),
    @NamedQuery(name = "YvsGrhEmployes.findOneByMatricule", query = "SELECT y FROM YvsGrhEmployes y WHERE (y.matricule = :matricule OR y.codeUsers.codeUsers=:matricule) AND y.agence.societe = :societe"),
    @NamedQuery(name = "YvsGrhEmployes.count", query = "SELECT count(y) FROM YvsGrhEmployes y WHERE (y.codeUsers.id = :user OR y.matricule=:matricule) AND y.agence.societe=:societe"),
    //compte les employé ayant un  matricule   dans une société
    @NamedQuery(name = "YvsGrhEmployes.count1", query = "SELECT count(y) FROM YvsGrhEmployes y WHERE y.matricule=:matricule AND y.agence.societe=:societe"),
    //récupère les employé d'une agence en ignorant le caractère actif ou non
    @NamedQuery(name = "YvsGrhEmployes.findByAgence", query = "SELECT y FROM YvsGrhEmployes y WHERE y.agence = :agence AND y.actif=true ORDER BY y.nom"),
    @NamedQuery(name = "YvsGrhEmployes.findActifs", query = "SELECT y FROM YvsGrhEmployes y WHERE y.agence.societe =:societe AND y.actif=:actif ORDER BY y.nom"),
    //récupère les employé d'une société en ignorant le caractère actif ou non
    @NamedQuery(name = "YvsGrhEmployes.findCActifs", query = "SELECT COUNT(y) FROM YvsGrhEmployes y WHERE y.agence.societe=:societe AND y.actif=:actif GROUP BY y.nom ORDER BY y.nom"),
    @NamedQuery(name = "YvsGrhEmployes.findByCivilite", query = "SELECT y FROM YvsGrhEmployes y WHERE y.civilite = :civilite"),
    //recherche par equipe
    @NamedQuery(name = "YvsGrhEmployes.findByEquipe", query = "SELECT y FROM YvsGrhEmployes y WHERE y.equipe=:equipe AND y.actif=true ORDER BY y.nom ASC, y.prenom"),
    @NamedQuery(name = "YvsGrhEmployes.findByEquipeC", query = "SELECT COUNT(y) FROM YvsGrhEmployes y WHERE y.equipe=:equipe AND y.actif=true"),
    //recherche par Département
    @NamedQuery(name = "YvsGrhEmployes.findByService", query = "SELECT y FROM YvsGrhEmployes y WHERE y.posteActif.departement=:departement AND y.actif=true ORDER BY y.nom ASC, y.prenom"),
    @NamedQuery(name = "YvsGrhEmployes.findByServiceC", query = "SELECT COUNT(y) FROM YvsGrhEmployes y WHERE y.posteActif.departement=:departement AND y.actif=true"),
    //recherche par Département
    @NamedQuery(name = "YvsGrhEmployes.findByServiceIN", query = "SELECT y FROM YvsGrhEmployes y WHERE y.posteActif.departement.id IN :departement AND y.actif=true ORDER BY y.nom ASC, y.prenom"),
    @NamedQuery(name = "YvsGrhEmployes.findByServiceINC", query = "SELECT COUNT(y) FROM YvsGrhEmployes y WHERE y.posteActif.departement.id IN :departement AND y.actif=true"),
    //Recherche les employé rataché à une code user
    @NamedQuery(name = "YvsGrhEmployes.findByUsers", query = "SELECT y FROM YvsGrhEmployes y WHERE y.codeUsers=:user ORDER BY y.nom"),
    @NamedQuery(name = "YvsGrhEmployes.findByUsersC", query = "SELECT COUNT(y) FROM YvsGrhEmployes y WHERE y.codeUsers=:user"),
    //Recherche les employés d'une mutuelle
    @NamedQuery(name = "YvsGrhEmployes.findByMutuelle", query = "SELECT y FROM YvsGrhEmployes y WHERE y.agence.mutuelle = :mutuelle ORDER BY y.matricule"),
    @NamedQuery(name = "YvsGrhEmployes.findByMutuelleC", query = "SELECT COUNT(y) FROM YvsGrhEmployes y WHERE y.agence.mutuelle = :mutuelle"),

    @NamedQuery(name = "YvsGrhEmployes.findById", query = "SELECT y FROM YvsGrhEmployes y WHERE y.id=:id"),
    @NamedQuery(name = "YvsGrhEmployes.findTiersById", query = "SELECT y.compteTiers FROM YvsGrhEmployes y WHERE y.id=:id"),
    @NamedQuery(name = "YvsGrhEmployes.findByIds", query = "SELECT y FROM YvsGrhEmployes y WHERE y.id IN :ids ORDER BY y.nom, y.prenom"),
    @NamedQuery(name = "YvsGrhEmployes.findByCompteTiers", query = "SELECT y FROM YvsGrhEmployes y JOIN FETCH y.contrat WHERE y.compteTiers=:tiers AND y.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhEmployes.findByCompteTiersActif", query = "SELECT y FROM YvsGrhEmployes y WHERE y.compteTiers=:tiers AND y.agence.societe=:societe AND y.actif=true"),
    @NamedQuery(name = "YvsGrhEmployes.findLikeCodeUsers", query = "SELECT y FROM YvsGrhEmployes y WHERE (upper(y.nom) LIKE upper(:codeUsers) OR upper(y.prenom) LIKE upper(:codeUsers) OR upper(y.matricule) LIKE upper(:codeUsers)) AND y.actif=true AND y.agence=:agence ORDER BY y.nom"),
    @NamedQuery(name = "YvsGrhEmployes.findByCodeUsers", query = "SELECT y FROM YvsGrhEmployes y WHERE (upper(concat(y.nom,' ',y.prenom)) LIKE upper(:codeUsers) OR upper(y.nom) LIKE upper(:codeUsers) OR upper(y.prenom) LIKE upper(:codeUsers) OR upper(y.matricule) LIKE upper(:codeUsers)) AND y.actif=true AND y.agence.societe=:agence ORDER BY y.nom"),
    @NamedQuery(name = "YvsGrhEmployes.findByCodeUsersS", query = "SELECT y FROM YvsGrhEmployes y WHERE (upper(concat(y.nom,' ',y.prenom)) LIKE upper(:codeUsers) OR upper(y.prenom) LIKE upper(:codeUsers) OR upper(y.matricule) LIKE upper(:codeUsers)) AND y.actif=true AND y.agence.societe=:agence ORDER BY y.nom"),
    @NamedQuery(name = "YvsGrhEmployes.findByCodeUsers1", query = "SELECT y FROM YvsGrhEmployes y WHERE y.codeUsers.codeUsers=:codeUsers AND y.actif=true AND y.agence=:agence"),
    @NamedQuery(name = "YvsGrhEmployes.findByDateNaissance", query = "SELECT y FROM YvsGrhEmployes y WHERE y.dateNaissance = :dateNaissance"),
    @NamedQuery(name = "YvsGrhEmployes.findByLieuNaissance", query = "SELECT y FROM YvsGrhEmployes y WHERE y.lieuNaissance = :lieuNaissance"),
    @NamedQuery(name = "YvsGrhEmployes.findByNom", query = "SELECT y FROM YvsGrhEmployes y WHERE y.nom LIKE :nom AND y.agence=:agence AND y.actif=true ORDER BY y.nom"),
    @NamedQuery(name = "YvsGrhEmployes.findByPrenom", query = "SELECT y FROM YvsGrhEmployes y WHERE y.prenom = :prenom"),
    @NamedQuery(name = "YvsGrhEmployes.findByEtatCivil", query = "SELECT y FROM YvsGrhEmployes y WHERE y.etatCivil = :etatCivil"),
    @NamedQuery(name = "YvsGrhEmployes.findByPays", query = "SELECT y FROM YvsGrhEmployes y WHERE y.pays = :pays"),
    @NamedQuery(name = "YvsGrhEmployes.findByCodePostal", query = "SELECT y FROM YvsGrhEmployes y WHERE y.codePostal = :codePostal"),
    @NamedQuery(name = "YvsGrhEmployes.findByCni", query = "SELECT y FROM YvsGrhEmployes y WHERE y.cni = :cni"),
    @NamedQuery(name = "YvsGrhEmployes.findByDateDelivCni", query = "SELECT y FROM YvsGrhEmployes y WHERE y.dateDelivCni = :dateDelivCni"),
    @NamedQuery(name = "YvsGrhEmployes.findByDateExpCni", query = "SELECT y FROM YvsGrhEmployes y WHERE y.dateExpCni = :dateExpCni"),
    @NamedQuery(name = "YvsGrhEmployes.findByLieuDelivCni", query = "SELECT y FROM YvsGrhEmployes y WHERE y.lieuDelivCni = :lieuDelivCni"),
    @NamedQuery(name = "YvsGrhEmployes.findByDateEmbauche", query = "SELECT y FROM YvsGrhEmployes y WHERE y.dateEmbauche = :dateEmbauche"),
    @NamedQuery(name = "YvsGrhEmployes.findByDateArret", query = "SELECT y FROM YvsGrhEmployes y WHERE y.dateArret = :dateArret"),
    @NamedQuery(name = "YvsGrhEmployes.findByCnps", query = "SELECT y FROM YvsGrhEmployes y WHERE y.matriculeCnps = :matriculeCnps"),
    @NamedQuery(name = "YvsGrhEmployes.findByHDynamique", query = "SELECT y FROM YvsGrhEmployes y WHERE y.agence=:agence AND y.actif=true AND y.horaireDynamique=true ORDER BY y.nom"),
    @NamedQuery(name = "YvsGrhEmployes.findByMotifArret", query = "SELECT y FROM YvsGrhEmployes y WHERE y.motifArret = :motifArret"),

    @NamedQuery(name = "YvsGrhEmployes.findByTiers", query = "SELECT y FROM YvsGrhEmployes y WHERE y.compteTiers = :tiers"),

    @NamedQuery(name = "YvsGrhEmployes.findDataById", query = "SELECT y.matricule, y.nom, y.prenom FROM YvsGrhEmployes y WHERE y.id=:id"),
    @NamedQuery(name = "YvsGrhEmployes.findIdByCode", query = "SELECT y.id FROM YvsGrhEmployes y WHERE y.agence.societe = :societe AND y.matricule = :code"),

    @NamedQuery(name = "YvsGrhEmployes.findPrevByNom", query = "SELECT y FROM YvsGrhEmployes y WHERE y.agence.societe = :societe AND CONCAT(y.nom, ' ', y.prenom) < :nom_prenom ORDER BY y.nom DESC, y.prenom DESC"),
    @NamedQuery(name = "YvsGrhEmployes.findNextByNom", query = "SELECT y FROM YvsGrhEmployes y WHERE y.agence.societe = :societe AND CONCAT(y.nom, ' ', y.prenom) > :nom_prenom ORDER BY y.nom, y.prenom"),

    @NamedQuery(name = "YvsGrhEmployes.findPrevByNomAgence", query = "SELECT y FROM YvsGrhEmployes y WHERE y.agence = :agence AND CONCAT(y.nom, ' ', y.prenom) < :nom_prenom ORDER BY y.nom DESC, y.prenom DESC"),
    @NamedQuery(name = "YvsGrhEmployes.findNextByNomAgence", query = "SELECT y FROM YvsGrhEmployes y WHERE y.agence = :agence AND CONCAT(y.nom, ' ', y.prenom) > :nom_prenom ORDER BY y.nom, y.prenom")})
public class YvsGrhEmployes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_employes_id_seq")
    @SequenceGenerator(sequenceName = "yvs_employes_id_seq", allocationSize = 1, name = "yvs_employes_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_prochain_avancement")
    @Temporal(TemporalType.DATE)
    private Date dateProchainAvancement;
    @Size(max = 2147483647)
    @Column(name = "matricule_cnps")
    private String matriculeCnps;
    @Column(name = "solde")
    private Double solde;
    @Column(name = "date_cloture_conge")
    @Temporal(TemporalType.DATE)
    private Date dateClotureConge;
    @Size(max = 2147483647)
    @Column(name = "telephone1")
    private String telephone1;
    @Size(max = 2147483647)
    @Column(name = "telephone2")
    private String telephone2;
    @Size(max = 2147483647)
    @Column(name = "mail1")
    private String mail1;
    @Size(max = 2147483647)
    @Column(name = "mail2")
    private String mail2;
    @Size(max = 2147483647)
    @Column(name = "adresse1")
    private String adresse1;
    @Size(max = 2147483647)
    @Column(name = "adresse2")
    private String adresse2;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "horaire_dynamique")
    private Boolean horaireDynamique;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 2147483647)
    @Column(name = "situation_familiale")
    private String situationFamiliale;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_update")
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "civilite")
    private String civilite;
    @Size(max = 2147483647)
    @Column(name = "matricule")
    private String matricule;
    @Column(name = "photos")
    private String photos;
    @Column(name = "photo_extension")
    private String photoExtension;
    @Column(name = "date_naissance")
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    @Size(max = 2147483647)
    @Column(name = "nom")
    private String nom;
    @Size(max = 2147483647)
    @Column(name = "prenom")
    private String prenom;
    @Size(max = 2147483647)
    @Column(name = "etat_civil")
    private String etatCivil;
    @Size(max = 2147483647)
    @Column(name = "code_postal")
    private String codePostal;
    @Size(max = 2147483647)
    @Column(name = "cni")
    private String cni;
    @Column(name = "date_deliv_cni")
    @Temporal(TemporalType.DATE)
    private Date dateDelivCni;
    @Column(name = "date_exp_cni")
    @Temporal(TemporalType.DATE)
    private Date dateExpCni;
    @Column(name = "date_embauche")
    @Temporal(TemporalType.DATE)
    private Date dateEmbauche;
    @Column(name = "date_arret")
    @Temporal(TemporalType.DATE)
    private Date dateArret;
    @Size(max = 2147483647)
    @Column(name = "motif_embauche")
    private String motifEmbauche;
    @Size(max = 2147483647)
    @Column(name = "motif_arret")
    private String motifArret;
    @Column(name = "num_securite_social")
    private String numSecuriteSocial;
    @Column(name = "nom_jeune_fille")
    private String nomJeuneFille;

    @JoinColumn(name = "lieu_naissance", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire lieuNaissance;
    @JoinColumn(name = "pays", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire pays;
    @JoinColumn(name = "lieu_deliv_cni", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire lieuDelivCni;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "profil", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhProfil profil;
    @JoinColumn(name = "poste_actif", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPosteDeTravail posteActif;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_collectif", referencedColumnName = "id")
    private YvsBasePlanComptable compteCollectif;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_tiers", referencedColumnName = "id")
    private YvsBaseTiers compteTiers;
    @JoinColumn(name = "statut_emps", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhStatutEmploye statutEmps;
    @JoinColumn(name = "equipe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEquipeEmploye equipe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "regle_tache", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhRegleTache regleTache;

    @JoinColumn(name = "code_users")
    @OneToOne(fetch = FetchType.LAZY)
    private YvsUsers codeUsers;
    @OneToOne(mappedBy = "employe", fetch = FetchType.LAZY)
    private YvsGrhContratEmps contrat;
    @OneToOne(mappedBy = "employe", fetch = FetchType.LAZY)
    private YvsExtEmploye codeExterne;
    @OneToOne(mappedBy = "employe", fetch = FetchType.LAZY)
    private YvsMutMutualiste mutualiste;

    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<YvsContactsEmps> contactsEmployes;
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<YvsPermisDeCoduire> permis;
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<YvsGrhPosteEmployes> historiques;
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<YvsLangueEmps> yvsLangueEmpsList;
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<YvsGrhConventionEmploye> conventions;
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<YvsDiplomeEmploye> yvsDiplomeEmployeList;
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<YvsGrhQualificationEmploye> yvsQualificationList;
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<YvsGrhProfilEmps> yvsProfilEmpsList;
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<YvsCompteBancaire> yvsCompteBancaireList;
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<YvsGrhPersonneEnCharge> personnes;
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<YvsGrhDocumentEmps> documents;

    @Transient
    private Double valeurElt;

    @Transient
    private YvsGrhMissions mission;
    @Transient
    private YvsGrhCongeEmps conge;
    @Transient
    private YvsGrhConventionCollective convention;
    @Transient
    private YvsGrhPlanningEmploye planing;
    @Transient
    private String nom_prenom;
    @Transient
    private boolean select;

    public YvsGrhEmployes() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsGrhEmployes(Long id) {
        this();
        this.id = id;
    }

    public YvsGrhEmployes(String matricule) {
        this();
        this.matricule = matricule;
    }

    public YvsGrhEmployes(Long id, String matricule) {
        this(id);
        this.matricule = matricule;
    }

    public YvsGrhEmployes(Long id, String nom, String prenom) {
        this(id);
        this.nom = nom;
        this.prenom = prenom;
    }

    public YvsGrhEmployes(Long id, String civilite, String nom, String prenom) {
        this(id, nom, prenom);
        this.civilite = civilite;
    }

    public YvsGrhEmployes(Long id, String matricule, String civilite, String nom, String prenom) {
        this(id, civilite, nom, prenom);
        this.matricule = matricule;
    }

    public YvsExtEmploye getCodeExterne() {
        return codeExterne;
    }

    public void setCodeExterne(YvsExtEmploye codeExterne) {
        this.codeExterne = codeExterne;
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

    public String getNom_prenom() {
        nom_prenom = "";
        if (nom != null ? !nom.equals("") : false) {
            nom_prenom = nom + " ";
        }
        if (prenom != null ? !prenom.equals("") : false) {
            nom_prenom += prenom;
        }
        return nom_prenom;
    }

    public Long getId() {
        return id == null ? -1 : id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCivilite() {
        return civilite != null ? civilite.trim().length() > 0 ? civilite : "M." : "M.";
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsers getCodeUsers() {
        return codeUsers;
    }

    public void setCodeUsers(YvsUsers codeUsers) {
        this.codeUsers = codeUsers;
    }

    public String getMatricule() {
        return matricule != null ? matricule : "";
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getPhotoExtension() {
        return photoExtension != null ? photoExtension.trim().length() > 0 ? photoExtension : "png" : "png";
    }

    public void setPhotoExtension(String photoExtension) {
        this.photoExtension = photoExtension;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateNaissance() {
        return dateNaissance;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @XmlTransient
    @JsonIgnore
    public YvsDictionnaire getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(YvsDictionnaire lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEtatCivil() {
        return etatCivil;
    }

    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    @XmlTransient
    @JsonIgnore
    public YvsDictionnaire getPays() {
        return pays;
    }

    public void setPays(YvsDictionnaire pays) {
        this.pays = pays;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getCni() {
        return cni;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateDelivCni() {
        return dateDelivCni;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateDelivCni(Date dateDelivCni) {
        this.dateDelivCni = dateDelivCni;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateExpCni() {
        return dateExpCni;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateExpCni(Date dateExpCni) {
        this.dateExpCni = dateExpCni;
    }

    @XmlTransient
    @JsonIgnore
    public YvsDictionnaire getLieuDelivCni() {
        return lieuDelivCni;
    }

    public void setLieuDelivCni(YvsDictionnaire lieuDelivCni) {
        this.lieuDelivCni = lieuDelivCni;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateEmbauche() {
        return dateEmbauche;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateEmbauche(Date dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateArret() {
        return dateArret;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateArret(Date dateArret) {
        this.dateArret = dateArret;
    }

    public String getMotifEmbauche() {
        return motifEmbauche;
    }

    public void setMotifEmbauche(String motifEmbauche) {
        this.motifEmbauche = motifEmbauche;
    }

    public String getMotifArret() {
        return motifArret;
    }

    public void setMotifArret(String motifArret) {
        this.motifArret = motifArret;
    }

    @XmlTransient
    @JsonIgnore
    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public String getNomJeuneFille() {
        return nomJeuneFille;
    }

    public void setNomJeuneFille(String nomJeuneFille) {
        this.nomJeuneFille = nomJeuneFille;
    }

    public String getNumSecuriteSocial() {
        return numSecuriteSocial;
    }

    public void setNumSecuriteSocial(String numSecuriteSocial) {
        this.numSecuriteSocial = numSecuriteSocial;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhContratEmps getContrat() {
        return contrat;
    }

    public void setContrat(YvsGrhContratEmps contrat) {
        this.contrat = contrat;
    }

    public String getSituationFamiliale() {
        return situationFamiliale;
    }

    public void setSituationFamiliale(String situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getHoraireDynamique() {
        return horaireDynamique != null ? horaireDynamique : false;
    }

    public void setHoraireDynamique(Boolean horaireDynamique) {
        this.horaireDynamique = horaireDynamique;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public String getTelephone2() {
        return telephone2;
    }

    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    public String getMail1() {
        return mail1;
    }

    public void setMail1(String mail1) {
        this.mail1 = mail1;
    }

    public String getMail2() {
        return mail2;
    }

    public void setMail2(String mail2) {
        this.mail2 = mail2;
    }

    public String getAdresse1() {
        return adresse1;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    @XmlTransient
    @JsonIgnore
    public boolean isEndEmbauche() {
        if (!getActif()) {
            return false;
        }
        if (contrat != null) {
            return contrat.isEndEmbauche();
        }
        return false;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsPermisDeCoduire> getPermis() {
        return permis;
    }

    public void setPermis(List<YvsPermisDeCoduire> permis) {
        this.permis = permis;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateClotureConge() {
        return dateClotureConge;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateClotureConge(Date dateClotureConge) {
        this.dateClotureConge = dateClotureConge;
    }

    public Double getSolde() {
        return solde;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateProchainAvancement() {
        return dateProchainAvancement;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateProchainAvancement(Date dateProchainAvancement) {
        this.dateProchainAvancement = dateProchainAvancement;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhRegleTache getRegleTache() {
        return regleTache;
    }

    public void setRegleTache(YvsGrhRegleTache regleTache) {
        this.regleTache = regleTache;
    }

    public String getMatriculeCnps() {
        return matriculeCnps;
    }

    public void setMatriculeCnps(String matriculeCnps) {
        this.matriculeCnps = matriculeCnps;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsContactsEmps> getContactsEmployes() {
        return contactsEmployes;
    }

    public void setContactsEmployes(List<YvsContactsEmps> contactsEmployes) {
        this.contactsEmployes = contactsEmployes;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhPosteDeTravail getPosteActif() {
        return posteActif;
    }

    public void setPosteActif(YvsGrhPosteDeTravail posteActif) {
        this.posteActif = posteActif;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhMissions getMission() {
        return mission != null ? mission : new YvsGrhMissions();
    }

    public void setMission(YvsGrhMissions mission) {
        this.mission = mission;
    }

    @XmlTransient
    @JsonIgnore
    public Double getValeurElt() {
        return valeurElt;
    }

    public void setValeurElt(Double valeurElt) {
        this.valeurElt = valeurElt;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhCongeEmps getConge() {
        return conge != null ? conge : new YvsGrhCongeEmps();
    }

    public void setConge(YvsGrhCongeEmps conge) {
        this.conge = conge;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhConventionCollective getConvention() {
        return convention != null ? convention : new YvsGrhConventionCollective();
    }

    public void setConvention(YvsGrhConventionCollective convention) {
        this.convention = convention;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhEquipeEmploye getEquipe() {
        return equipe;
    }

    public void setEquipe(YvsGrhEquipeEmploye equipe) {
        this.equipe = equipe;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBasePlanComptable getCompteCollectif() {
        return compteCollectif;
    }

    public void setCompteCollectif(YvsBasePlanComptable compteCollectif) {
        this.compteCollectif = compteCollectif;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseTiers getCompteTiers() {
        return compteTiers;
    }

    public void setCompteTiers(YvsBaseTiers compteTiers) {
        this.compteTiers = compteTiers;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhProfil getProfil() {
        return profil;
    }

    public void setProfil(YvsGrhProfil profil) {
        this.profil = profil;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhStatutEmploye getStatutEmps() {
        return statutEmps;
    }

    public void setStatutEmps(YvsGrhStatutEmploye statutEmps) {
        this.statutEmps = statutEmps;
    }

    @XmlTransient
    @JsonIgnore
    public YvsMutMutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(YvsMutMutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsGrhPosteEmployes> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<YvsGrhPosteEmployes> historiques) {
        this.historiques = historiques;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsLangueEmps> getYvsLangueEmpsList() {
        return yvsLangueEmpsList;
    }

    public void setYvsLangueEmpsList(List<YvsLangueEmps> yvsLangueEmpsList) {
        this.yvsLangueEmpsList = yvsLangueEmpsList;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsGrhConventionEmploye> getConventions() {
        return conventions;
    }

    public void setConventions(List<YvsGrhConventionEmploye> conventions) {
        this.conventions = conventions;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsDiplomeEmploye> getYvsDiplomeEmployeList() {
        return yvsDiplomeEmployeList;
    }

    public void setYvsDiplomeEmployeList(List<YvsDiplomeEmploye> yvsDiplomeEmployeList) {
        this.yvsDiplomeEmployeList = yvsDiplomeEmployeList;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsGrhQualificationEmploye> getYvsQualificationList() {
        return yvsQualificationList;
    }

    public void setYvsQualificationList(List<YvsGrhQualificationEmploye> yvsQualificationList) {
        this.yvsQualificationList = yvsQualificationList;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsGrhProfilEmps> getYvsProfilEmpsList() {
        return yvsProfilEmpsList;
    }

    public void setYvsProfilEmpsList(List<YvsGrhProfilEmps> yvsProfilEmpsList) {
        this.yvsProfilEmpsList = yvsProfilEmpsList;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsCompteBancaire> getYvsCompteBancaireList() {
        return yvsCompteBancaireList;
    }

    public void setYvsCompteBancaireList(List<YvsCompteBancaire> yvsCompteBancaireList) {
        this.yvsCompteBancaireList = yvsCompteBancaireList;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsGrhPersonneEnCharge> getPersonnes() {
        return personnes;
    }

    public void setPersonnes(List<YvsGrhPersonneEnCharge> personnes) {
        this.personnes = personnes;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsGrhDocumentEmps> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsGrhDocumentEmps> documents) {
        this.documents = documents;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhPlanningEmploye getPlaning() {
        return planing;
    }

    public void setPlaning(YvsGrhPlanningEmploye planing) {
        this.planing = planing;
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
        if (!(object instanceof YvsGrhEmployes)) {
            return false;
        }
        YvsGrhEmployes other = (YvsGrhEmployes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + matricule + ")";
    }

}

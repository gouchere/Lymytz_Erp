/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

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
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import yvs.dao.YvsEntity;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_base_depots")
@NamedQueries({
    @NamedQuery(name = "YvsBaseDepots.findAll", query = "SELECT y FROM YvsBaseDepots y WHERE y.agence.societe = :societe ORDER BY y.code"),
    @NamedQuery(name = "YvsBaseDepots.findAllActif", query = "SELECT y FROM YvsBaseDepots y WHERE y.agence.societe = :societe AND y.actif=true ORDER BY y.code"),
    @NamedQuery(name = "YvsBaseDepots.findByAgence", query = "SELECT y FROM YvsBaseDepots y WHERE y.agence = :agence ORDER BY y.code"),
    @NamedQuery(name = "YvsBaseDepots.findByAgenceActif", query = "SELECT y FROM YvsBaseDepots y WHERE y.agence = :agence AND y.actif = :actif ORDER BY y.code"),
    @NamedQuery(name = "YvsBaseDepots.findByAgenceCount", query = "SELECT COUNT(y) FROM YvsBaseDepots y WHERE y.agence = :agence"),
    @NamedQuery(name = "YvsBaseDepots.findById", query = "SELECT y FROM YvsBaseDepots y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseDepots.findByIds", query = "SELECT y FROM YvsBaseDepots y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsBaseDepots.findBySociete", query = "SELECT y FROM YvsBaseDepots y WHERE y.agence.societe = :societe ORDER BY y.code"),
    @NamedQuery(name = "YvsBaseDepots.findByAbbreviation", query = "SELECT y FROM YvsBaseDepots y WHERE y.abbreviation = :abbreviation AND  y.agence.societe = :societe"),
    @NamedQuery(name = "YvsBaseDepots.findByAgenceAbbreviation", query = "SELECT y FROM YvsBaseDepots y WHERE y.abbreviation = :abbreviation AND y.agence = :agence"),
    @NamedQuery(name = "YvsBaseDepots.findByActif", query = "SELECT y FROM YvsBaseDepots y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseDepots.findByAdresse", query = "SELECT y FROM YvsBaseDepots y WHERE y.adresse = :adresse"),
    @NamedQuery(name = "YvsBaseDepots.findByCode", query = "SELECT y FROM YvsBaseDepots y WHERE y.agence.societe = :societe AND y.code = :code"),
    @NamedQuery(name = "YvsBaseDepots.findByCurrentCode", query = "SELECT y FROM YvsBaseDepots y WHERE y.code = :code AND y.agence.societe = :societe"),
    @NamedQuery(name = "YvsBaseDepots.findByDesignation", query = "SELECT y FROM YvsBaseDepots y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseDepots.findByResponsable", query = "SELECT y FROM YvsBaseDepots y WHERE y.responsable = :responsable"),
    @NamedQuery(name = "YvsBaseDepots.findByActifResponsableAgence", query = "SELECT y FROM YvsBaseDepots y WHERE y.agence = :agence AND y.responsable = :responsable AND y.actif = TRUE"),
    @NamedQuery(name = "YvsBaseDepots.findByActifResponsable", query = "SELECT y FROM YvsBaseDepots y WHERE y.responsable = :responsable AND y.actif = TRUE"),
    @NamedQuery(name = "YvsBaseDepots.findByActifResponsableOther", query = "SELECT y FROM YvsBaseDepots y WHERE (y.responsable = :responsable OR y.id IN :ids) AND y.actif = TRUE"),
    @NamedQuery(name = "YvsBaseDepots.findIdByResponsable", query = "SELECT y.id FROM YvsBaseDepots y WHERE y.responsable = :responsable"),
    @NamedQuery(name = "YvsBaseDepots.findByOpAchat", query = "SELECT y FROM YvsBaseDepots y WHERE y.opAchat = :opAchat AND y.agence = :agence"),
    @NamedQuery(name = "YvsBaseDepots.findByOpAchatCount", query = "SELECT COUNT(y) FROM YvsBaseDepots y WHERE y.opAchat = :opAchat AND y.agence = :agence"),
    @NamedQuery(name = "YvsBaseDepots.findByOpAchatS", query = "SELECT y FROM YvsBaseDepots y WHERE y.opAchat = :opAchat AND y.agence.societe = :societe"),
    @NamedQuery(name = "YvsBaseDepots.findByOpAchatCountS", query = "SELECT COUNT(y) FROM YvsBaseDepots y WHERE y.opAchat = :opAchat AND y.agence.societe = :societe"),
    @NamedQuery(name = "YvsBaseDepots.findByOpVente", query = "SELECT y FROM YvsBaseDepots y WHERE y.opVente = :opVente"),
    @NamedQuery(name = "YvsBaseDepots.findByOpProduction", query = "SELECT y FROM YvsBaseDepots y WHERE y.opProduction = :opProduction"),
    @NamedQuery(name = "YvsBaseDepots.findByOpTransit", query = "SELECT y FROM YvsBaseDepots y WHERE y.opTransit = :opTransit"),
    @NamedQuery(name = "YvsBaseDepots.findByCrenau", query = "SELECT y FROM YvsBaseDepots y WHERE y.crenau = :crenau"),
    @NamedQuery(name = "YvsBaseDepots.findCountC", query = "SELECT COUNT(y) FROM YvsBaseDepots y WHERE y.code = :code"),
    @NamedQuery(name = "YvsBaseDepots.findCount", query = "SELECT COUNT(y) FROM YvsBaseDepots y WHERE y.code = :code OR y.abbreviation=:abbr"),
    @NamedQuery(name = "YvsBaseDepots.findIdsDepotAgence", query = "SELECT y.id FROM YvsBaseDepots y WHERE y.agence=:agence"),

    @NamedQuery(name = "YvsBaseDepots.findDepotByTypeOpMP", query = "SELECT y FROM YvsBaseDepots y WHERE y.agence.societe=:societe AND y.typeMp=TRUE"),
    @NamedQuery(name = "YvsBaseDepots.findDepotByTypeOpPF", query = "SELECT y FROM YvsBaseDepots y WHERE y.agence.societe=:societe AND y.typePf=TRUE"),
    @NamedQuery(name = "YvsBaseDepots.findDepotByTypeOpNegoce", query = "SELECT y FROM YvsBaseDepots y WHERE y.agence.societe=:societe AND y.typeNe=TRUE"),
    @NamedQuery(name = "YvsBaseDepots.findDepotByTypeOpPSF", query = "SELECT y FROM YvsBaseDepots y WHERE y.agence.societe=:societe AND y.typePsf=TRUE"),
    @NamedQuery(name = "YvsBaseDepots.findByControlStock", query = "SELECT y FROM YvsBaseDepots y WHERE y.controlStock = :controlStock"),

    @NamedQuery(name = "YvsBaseDepots.findResponsableById", query = "SELECT y.responsable FROM YvsBaseDepots y WHERE y.id = :id")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseDepots extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_depots_id_seq", name = "yvs_base_depots_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_depots_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 255)
    @Column(name = "abbreviation")
    private String abbreviation;
    @Column(name = "description")
    private String description;
    @Size(max = 255)
    @Column(name = "adresse")
    private String adresse;
    @Size(max = 255)
    @Column(name = "code")
    private String code;
    @Size(max = 255)
    @Column(name = "designation")
    private String designation;
    @Column(name = "op_achat")
    private Boolean opAchat = false;
    @Column(name = "op_vente")
    private Boolean opVente = false;
    @Column(name = "op_production")
    private Boolean opProduction = false;
    @Column(name = "op_transit")
    private Boolean opTransit = false;
    @Column(name = "op_retour")
    private Boolean opRetour = false;
    @Column(name = "op_reserv")
    private Boolean opReserv = false;
    @Column(name = "crenau")
    private Boolean crenau = false;
    @Column(name = "control_stock")
    private Boolean controlStock = false;
    @Column(name = "op_technique")
    private Boolean opTechnique;
    @Column(name = "supp")
    private Boolean supp = false;
    @Column(name = "actif")
    private Boolean actif = true;
    @Column(name = "principal")
    private Boolean principal = false;
    @Column(name = "verify_appro")
    private Boolean verifyAppro = false;
    @Column(name = "verify_all_valid_inventaire")
    private Boolean verifyAllValidInventaire = false;
    @Column(name = "requiere_lot")
    private Boolean requiereLot = false;

    @Column(name = "type_pf")
    private Boolean typePf = false;
    @Column(name = "type_mp")
    private Boolean typeMp = false;
    @Column(name = "type_ne")
    private Boolean typeNe = false;
    @Column(name = "type_psf")
    private Boolean typePsf = false;

    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes responsable;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "code_acces", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCodeAcces codeAcces;

    @OneToMany(mappedBy = "depot",fetch = FetchType.LAZY)
    private List<YvsBaseDepotOperation> operations;
    @OneToMany(mappedBy = "depot", fetch = FetchType.LAZY)
    private List<YvsBaseArticleDepot> articles;
    @OneToMany(mappedBy = "depot", fetch = FetchType.LAZY)
    private List<YvsBasePointVenteDepot> points;
    @OneToMany(mappedBy = "depot",fetch = FetchType.LAZY)
    private List<YvsBaseEmplacementDepot> emplacements;

    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean error;

    public YvsBaseDepots() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        this.articles = new ArrayList<>();
    }

    public YvsBaseDepots(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseDepots(Long id, String designation) {
        this(id);
        this.designation = designation;
    }

    public YvsBaseDepots(Long id, boolean actif) {
        this(id);
        this.actif = actif;
    }

    public YvsBaseDepots(Long id, String designation, YvsAgences agence) {
        this(id, designation);
        this.agence = agence;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getOpRetour() {
        return opRetour != null ? opRetour : false;
    }

    public void setOpRetour(Boolean opRetour) {
        this.opRetour = opRetour;
    }

    public Boolean getOpReserv() {
        return opReserv != null ? opReserv : false;
    }

    public void setOpReserv(Boolean opReserv) {
        this.opReserv = opReserv;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public Boolean getVerifyAllValidInventaire() {
        return verifyAllValidInventaire != null ? verifyAllValidInventaire : true;
    }

    public void setVerifyAllValidInventaire(Boolean verifyAllValidInventaire) {
        this.verifyAllValidInventaire = verifyAllValidInventaire;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCrenau() {
        return crenau != null ? crenau : false;
    }

    public void setCrenau(Boolean crenau) {
        this.crenau = crenau;
    }

    public String getAbbreviation() {
        return abbreviation != null ? abbreviation : "";
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Boolean getOpAchat() {
        return opAchat != null ? opAchat : false;
    }

    public void setOpAchat(Boolean opAchat) {
        this.opAchat = opAchat;
    }

    public Boolean getOpVente() {
        return opVente != null ? opVente : false;
    }

    public void setOpVente(Boolean opVente) {
        this.opVente = opVente;
    }

    public Boolean getOpProduction() {
        return opProduction != null ? opProduction : false;
    }

    public void setOpProduction(Boolean opProduction) {
        this.opProduction = opProduction;
    }

    public Boolean getOpTransit() {
        return opTransit != null ? opTransit : false;
    }

    public void setOpTransit(Boolean opTransit) {
        this.opTransit = opTransit;
    }

    public Boolean getControlStock() {
        return controlStock != null ? controlStock : false;
    }

    public void setControlStock(Boolean controlStock) {
        this.controlStock = controlStock;
    }

    public Boolean getRequiereLot() {
        return requiereLot != null ? requiereLot : false;
    }

    public void setRequiereLot(Boolean requiereLot) {
        this.requiereLot = requiereLot;
    }

    public Boolean getSupp() {
        return supp != null ? supp : false;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getOpTechnique() {
        return opTechnique != null ? opTechnique : false;
    }

    public void setOpTechnique(Boolean opTechnique) {
        this.opTechnique = opTechnique;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Boolean getVerifyAppro() {
        return verifyAppro != null ? verifyAppro : false;
    }

    public void setVerifyAppro(Boolean verifyAppro) {
        this.verifyAppro = verifyAppro;
    }

    public Boolean getTypePf() {
        return typePf != null ? typePf : false;
    }

    public void setTypePf(Boolean typePf) {
        this.typePf = typePf;
    }

    public Boolean getTypeMp() {
        return typeMp != null ? typeMp : false;
    }

    public void setTypeMp(Boolean typeMp) {
        this.typeMp = typeMp;
    }

    public Boolean getTypeNe() {
        return typeNe != null ? typeNe : false;
    }

    public void setTypeNe(Boolean typeNe) {
        this.typeNe = typeNe;
    }

    public Boolean getTypePsf() {
        return typePsf != null ? typePsf : false;
    }

    public void setTypePsf(Boolean typePsf) {
        this.typePsf = typePsf;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public YvsGrhEmployes getResponsable() {
        return responsable;
    }

    public void setResponsable(YvsGrhEmployes responsable) {
        this.responsable = responsable;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseCodeAcces getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(YvsBaseCodeAcces codeAcces) {
        this.codeAcces = codeAcces;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticleDepot> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticleDepot> articles) {
        this.articles = articles;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBasePointVenteDepot> getPoints() {
        return points;
    }

    public void setPoints(List<YvsBasePointVenteDepot> points) {
        this.points = points;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseDepotOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<YvsBaseDepotOperation> operations) {
        this.operations = operations;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseEmplacementDepot> getEmplacements() {
        return emplacements;
    }

    public void setEmplacements(List<YvsBaseEmplacementDepot> emplacements) {
        this.emplacements = emplacements;
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
        if (!(object instanceof YvsBaseDepots)) {
            return false;
        }
        YvsBaseDepots other = (YvsBaseDepots) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseDepots[ id=" + id + " ]";
    }

}

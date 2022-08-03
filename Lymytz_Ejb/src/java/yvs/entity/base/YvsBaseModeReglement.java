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
import javax.persistence.OneToOne;
import yvs.dao.YvsEntity;
import yvs.entity.compta.YvsComptaPhaseReglement;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_mode_reglement")
@NamedQueries({
    @NamedQuery(name = "YvsBaseModeReglement.findAll", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.societe = :societe ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseModeReglement.findById", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseModeReglement.findByDesignation", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.designation = :designation AND y.societe = :societe"),
    @NamedQuery(name = "YvsBaseModeReglement.findByDesignationEx", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.designation = :designation AND y.societe = :societe AND y.id!=:id"),
    @NamedQuery(name = "YvsBaseModeReglement.findByDescription", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseModeReglement.findByActif", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.societe = :societe AND y.actif = :actif ORDER BY y.designation"),

    @NamedQuery(name = "YvsBaseModeReglement.findAllNotIds", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.societe = :societe AND y.id NOT IN :ids ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseModeReglement.findForAcompte", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.societe = :societe AND y.typeReglement NOT IN ('COMPENSATION', 'SALAIRE') ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseModeReglement.findByTypeActif", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.societe = :societe AND y.typeReglement = :type AND y.actif = :actif ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseModeReglement.findByDefault", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.societe = :societe AND y.typeReglement = :type AND y.actif = :actif AND y.defaultMode = :defaut ORDER BY y.designation")})
public class YvsBaseModeReglement extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_model_de_reglement_id_seq", name = "yvs_model_de_reglement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_model_de_reglement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "designation")
    private String designation;
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "visible_on_print_vente")
    private Boolean visibleOnPrintVente;
    @Column(name = "default_mode")
    private Boolean defaultMode;
    @Size(max = 2147483647)
    @Column(name = "type_reglement")
    private String typeReglement;
    @Size(max = 2147483647)
    @Column(name = "numero_marchand")
    private String numeroMarchand;
    @Size(max = 2147483647)
    @Column(name = "code_paiement")
    private String codePaiement;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    @OneToOne(mappedBy = "mode", fetch = FetchType.LAZY)
    private YvsBaseModeReglementInformations infos;
    @OneToOne(mappedBy = "mode", fetch = FetchType.LAZY)
    private YvsBaseModeReglementBanque banque;

    @Transient
    private List<YvsComptaPhaseReglement> phases;
    @Transient
    private boolean select;
    @Transient
    private boolean new_;
    @Transient
    private double ca;
    @Transient
    private double solde;

    public YvsBaseModeReglement() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseModeReglement(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseModeReglement(Long id, String designation) {
        this(id);
        this.designation = designation;
    }

    public YvsBaseModeReglement(Long id, String designation, String typeReglement) {
        this(id, designation);
        this.typeReglement = typeReglement;
    }

    public YvsBaseModeReglement(YvsBaseModeReglement y) {
        this(y.id, y.designation, y.typeReglement);
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
        return dateSave != null ? dateSave : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    @Override
    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation != null ? designation : "";
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getVisibleOnPrintVente() {
        return visibleOnPrintVente != null ? visibleOnPrintVente : false;
    }

    public void setVisibleOnPrintVente(Boolean visibleOnPrintVente) {
        this.visibleOnPrintVente = visibleOnPrintVente;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public Boolean getDefaultMode() {
        return defaultMode != null ? defaultMode : false;
    }

    public void setDefaultMode(Boolean defaultMode) {
        this.defaultMode = defaultMode;
    }

    public double getCa() {
        return ca;
    }

    public void setCa(double ca) {
        this.ca = ca;
    }

    public String getNumeroMarchand() {
        return numeroMarchand;
    }

    public void setNumeroMarchand(String numeroMarchand) {
        this.numeroMarchand = numeroMarchand;
    }

    public String getCodePaiement() {
        return codePaiement;
    }

    public void setCodePaiement(String codePaiement) {
        this.codePaiement = codePaiement;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getTypeReglement() {
        return typeReglement != null ? typeReglement.trim().length() > 0 ? typeReglement : "ESPECE" : "ESPECE";
    }

    public void setTypeReglement(String typeReglement) {
        this.typeReglement = typeReglement;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaPhaseReglement> getPhases() {
        return phases;
    }

    public void setPhases(List<YvsComptaPhaseReglement> phases) {
        this.phases = phases;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public YvsBaseModeReglementInformations getInfos() {
        return infos;
    }

    public void setInfos(YvsBaseModeReglementInformations infos) {
        this.infos = infos;
    }

    public YvsBaseModeReglementBanque getBanque() {
        return banque;
    }

    public void setBanque(YvsBaseModeReglementBanque banque) {
        this.banque = banque;
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
        if (!(object instanceof YvsBaseModeReglement)) {
            return false;
        }
        YvsBaseModeReglement other = (YvsBaseModeReglement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getDesignation();
    }

}

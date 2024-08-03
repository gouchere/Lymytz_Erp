/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.client;

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
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_categorie_client")
@NamedQueries({
    @NamedQuery(name = "YvsBaseCategorieClient.findAll", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.societe = :societe  ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseCategorieClient.findAllActif", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.societe = :societe AND y.actif=true ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseCategorieClient.findDefault", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.societe = :societe AND y.defaut = true ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseCategorieClient.findDefaultActif", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.societe = :societe AND y.defaut = :defaut AND y.actif = :actif ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseCategorieClient.findById", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByNotId", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.id != :id AND y.societe = :societe ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByCode", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.code = :code"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByCurrentCode", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.code = :code AND y.societe = :societe"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByLibelle", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByParent", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.parent = :parent"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByOnline", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.societe = :societe AND y.venteOnline = :venteOnline"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByLierClient", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.societe = :societe AND y.lierClient = :lierClient ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByDescription", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.description = :description")})
public class YvsBaseCategorieClient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_categorie_client_id_seq", name = "yvs_com_categorie_client_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_categorie_client_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "code")
    private String code;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "lier_client")
    private Boolean lierClient;
    @Column(name = "defaut")
    private Boolean defaut;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "vente_online")
    private Boolean venteOnline;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCategorieClient parent;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModelReglement model;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "categorie")
    private List<YvsComPlanReglementCategorie> plans;
    @OneToMany(mappedBy = "categorie")
    private List<YvsBasePlanTarifaire> tarifs;
    @OneToMany(mappedBy = "categorie")
    private List<YvsComCategorieTarifaire> tarifaires;
    @OneToMany(mappedBy = "parent")
    private List<YvsBaseCategorieClient> fils;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;
    @Transient
    private String designation;

    public YvsBaseCategorieClient() {
        plans = new ArrayList<>();
        tarifs = new ArrayList<>();
        tarifaires = new ArrayList<>();
        fils = new ArrayList<>();
    }

    public YvsBaseCategorieClient(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseCategorieClient(Long id, String libelle) {
        this(id);
        this.libelle = libelle;
    }

    public YvsBaseCategorieClient(Long id, String code, String libelle) {
        this(id, libelle);
        this.code = code;
    }

    public YvsBaseCategorieClient(Long id, String code, String libelle, YvsBaseCategorieClient parent) {
        this(id, code, libelle);
        this.parent = parent;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Boolean getVenteOnline() {
        return venteOnline != null ? venteOnline : false;
    }

    public void setVenteOnline(Boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public String getDesignation() {
        designation = getLibelle();
        if (getParent() != null ? getParent().getId() > 0 : false) {
            designation = getParent().getDesignation() + "/" + designation;
        }
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @XmlTransient  @JsonIgnore
    public YvsBaseModelReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModelReglement model) {
        this.model = model;
    }

    @XmlTransient  @JsonIgnore
    public List<YvsComCategorieTarifaire> getTarifaires() {
        return tarifaires;
    }

    public void setTarifaires(List<YvsComCategorieTarifaire> tarifaires) {
        this.tarifaires = tarifaires;
    }

    @XmlTransient  @JsonIgnore
    public List<YvsBasePlanTarifaire> getTarifs() {
        return tarifs;
    }

    public void setTarifs(List<YvsBasePlanTarifaire> tarifs) {
        this.tarifs = tarifs;
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

    public Boolean getDefaut() {
        return defaut;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Boolean getLierClient() {
        return lierClient;
    }

    public void setLierClient(Boolean lierClient) {
        this.lierClient = lierClient;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle != null ? libelle : "";
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    @XmlTransient  @JsonIgnore
    public List<YvsBaseCategorieClient> getFils() {
        return fils;
    }

    public void setFils(List<YvsBaseCategorieClient> fils) {
        this.fils = fils;
    }

    @XmlTransient  @JsonIgnore
    public YvsBaseCategorieClient getParent() {
        return parent;
    }

    public void setParent(YvsBaseCategorieClient parent) {
        this.parent = parent;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @XmlTransient  @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient  @JsonIgnore
    public List<YvsComPlanReglementCategorie> getPlans() {
        return plans;
    }

    public void setPlans(List<YvsComPlanReglementCategorie> plans) {
        this.plans = plans;
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
        if (!(object instanceof YvsBaseCategorieClient)) {
            return false;
        }
        YvsBaseCategorieClient other = (YvsBaseCategorieClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.client.YvsBaseCategorieClient[ id=" + id + " ]";
    }

}

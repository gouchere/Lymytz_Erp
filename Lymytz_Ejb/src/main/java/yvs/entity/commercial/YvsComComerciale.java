/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial;

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
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.commercial.commission.YvsComPlanCommission;
import yvs.entity.commercial.vente.YvsComCommercialVente;
import yvs.entity.compta.YvsComptaCaissePieceCommission;
import yvs.entity.param.YvsAgences;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_com_comerciale")
@NamedQueries({
    @NamedQuery(name = "YvsComComerciale.findAll", query = "SELECT y FROM YvsComComerciale y JOIN FETCH y.utilisateur WHERE y.utilisateur.agence.societe = :societe ORDER bY y.nom, y.prenom"),
    @NamedQuery(name = "YvsComComerciale.findByAgence", query = "SELECT y FROM YvsComComerciale y JOIN FETCH y.utilisateur WHERE y.utilisateur.agence = :agence ORDER bY y.nom, y.prenom"),
    @NamedQuery(name = "YvsComComerciale.findById", query = "SELECT y FROM YvsComComerciale y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComComerciale.findTiersById", query = "SELECT y.tiers FROM YvsComComerciale y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComComerciale.findByCodeRef", query = "SELECT y FROM YvsComComerciale y WHERE y.codeRef = :codeRef"),
    @NamedQuery(name = "YvsComComerciale.findByNom", query = "SELECT y FROM YvsComComerciale y WHERE y.nom = :nom"),
    @NamedQuery(name = "YvsComComerciale.findBySociete", query = "SELECT y FROM YvsComComerciale y WHERE y.agence.societe = :societe AND y.actif = TRUE ORDER bY y.codeRef"),
    @NamedQuery(name = "YvsComComerciale.findByUser", query = "SELECT y FROM YvsComComerciale y WHERE y.utilisateur = :user"),
    @NamedQuery(name = "YvsComComerciale.findByTiers", query = "SELECT y FROM YvsComComerciale y WHERE y.tiers=:tiers AND y.actif=true"),
    @NamedQuery(name = "YvsComComerciale.findByPrenom", query = "SELECT y FROM YvsComComerciale y WHERE y.prenom = :prenom"),
    @NamedQuery(name = "YvsComComerciale.findByTelephone", query = "SELECT y FROM YvsComComerciale y WHERE y.telephone = :telephone"),
    @NamedQuery(name = "YvsComComerciale.findByDateSave", query = "SELECT y FROM YvsComComerciale y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComComerciale.findDefaut", query = "SELECT y FROM YvsComComerciale y WHERE y.agence.societe = :societe AND y.actif = TRUE AND y.defaut = TRUE")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComComerciale implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_comerciale_id_seq", name = "yvs_com_comerciale_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_comerciale_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "code_ref")
    private String codeRef;
    @Size(max = 2147483647)
    @Column(name = "nom")
    private String nom;
    @Size(max = 2147483647)
    @Column(name = "prenom")
    private String prenom;
    @Size(max = 2147483647)
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "defaut")
    private Boolean defaut;

    @JoinColumn(name = "tiers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTiers tiers;
    @JoinColumn(name = "utilisateur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers utilisateur;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "commission", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComPlanCommission commission;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "commercial", fetch = FetchType.LAZY)
    private List<YvsComCommercialVente> factures;
    @Transient
    private List<YvsComptaCaissePieceCommission> reglements;
    @Transient
    private boolean new_;
    @Transient
    private double sommeCommission;

    public YvsComComerciale() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        factures = new ArrayList<>();
        reglements = new ArrayList<>();
    }

    public YvsComComerciale(Long id) {
        this();
        this.id = id;
    }

    public YvsComComerciale(Long id, String nom, String prenom) {
        this(id);
        this.nom = nom;
        this.prenom = prenom;
    }

    public YvsComComerciale(YvsComComerciale y) {
        this.id = y.id;
        this.codeRef = y.codeRef;
        this.nom = y.nom;
        this.prenom = y.prenom;
        this.telephone = y.telephone;
        this.dateSave = y.dateSave;
        this.dateUpdate = y.dateUpdate;
        this.actif = y.actif;
        this.defaut = y.defaut;
        this.author = y.author;
        this.utilisateur = y.utilisateur;
        this.agence = y.agence;
        this.commission = y.commission;
        this.factures = y.factures;
        this.new_ = y.new_;
        this.sommeCommission = y.sommeCommission;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeRef() {
        return codeRef;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Boolean getDefaut() {
        return defaut != null ? defaut : false;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public String getNom_prenom() {
        return (nom != null) ? (nom.concat(" ").concat((prenom != null) ? prenom : "")) : (prenom != null) ? prenom : "";
    }

    public double getSommeCommission() {
        return sommeCommission;
    }

    public void setSommeCommission(double sommeCommission) {
        this.sommeCommission = sommeCommission;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsers getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(YvsUsers utilisateur) {
        this.utilisateur = utilisateur;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsComPlanCommission getCommission() {
        return commission;
    }

    public void setCommission(YvsComPlanCommission commission) {
        this.commission = commission;
    }

    public YvsBaseTiers getTiers() {
        return tiers;
    }

    public void setTiers(YvsBaseTiers tiers) {
        this.tiers = tiers;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComCommercialVente> getFactures() {
        return factures;
    }

    public void setFactures(List<YvsComCommercialVente> factures) {
        this.factures = factures;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceCommission> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceCommission> reglements) {
        this.reglements = reglements;
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
        if (!(object instanceof YvsComComerciale)) {
            return false;
        }
        YvsComComerciale other = (YvsComComerciale) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.objectifs.YvsComComerciale[ id=" + id + " ]";
    }

}

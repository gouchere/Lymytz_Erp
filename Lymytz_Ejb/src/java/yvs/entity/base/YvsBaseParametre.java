/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_parametre")
@NamedQueries({
    @NamedQuery(name = "YvsBaseParametre.findAll", query = "SELECT y FROM YvsBaseParametre y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsBaseParametre.findById", query = "SELECT y FROM YvsBaseParametre y WHERE y.id = :id")})
public class YvsBaseParametre extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_parametre_id_seq", name = "yvs_base_parametre_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_parametre_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "generer_password")
    private Boolean genererPassword;
    @Column(name = "generer_reference_article")
    private Boolean genererReferenceArticle;
    @Column(name = "taille_lettre_reference_article")
    private Integer tailleLettreReferenceArticle;
    @Column(name = "taille_numero_reference_article")
    private Integer tailleNumeroReferenceArticle;
    @Column(name = "nombre_elt_accueil")
    private Integer nombreEltAccueil = 5;
    @Size(max = 2147483647)
    @Column(name = "defaut_password")
    private String defautPassword;
    @Column(name = "taille_password")
    private Integer taillePassword;
    @Column(name = "duree_inactiv_article")
    private Integer dureeInactivArticle;
    @Column(name = "taux_ecart_pr")
    private Double tauxEcartPr;
    @Column(name = "monitor_pr")
    private Boolean monitorPr;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "back_color_etiquette")
    private String backColorEtiquette = "FFAF02";
    @Column(name = "fore_color_etiquette")
    private String foreColorEtiquette = "C78801";
    @Column(name = "nombre_page_min")
    private Integer nombrePageMin;

    @Column(name = "display_avis")
    private Boolean displayAvis;
    @Column(name = "display_caracteristique")
    private Boolean displayCaracteristique;
    @Column(name = "display_analytique")
    private Boolean displayAnalytique;
    @Column(name = "display_fournisseur")
    private Boolean displayFournisseur;
    @Column(name = "display_tarif_zone")
    private Boolean displayTarifZone;
    @Column(name = "display_equivalent")
    private Boolean displayEquivalent;
    @Column(name = "display_production")
    private Boolean displayProduction;

    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsBaseParametre() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseParametre(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTaillePassword() {
        return taillePassword != null ? taillePassword : 5;
    }

    public void setTaillePassword(Integer taillePassword) {
        this.taillePassword = taillePassword;
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

    public Boolean getGenererReferenceArticle() {
        return genererReferenceArticle != null ? genererReferenceArticle : false;
    }

    public void setGenererReferenceArticle(Boolean genererReferenceArticle) {
        this.genererReferenceArticle = genererReferenceArticle;
    }

    public Integer getTailleLettreReferenceArticle() {
        return tailleLettreReferenceArticle != null ? tailleLettreReferenceArticle : 3;
    }

    public void setTailleLettreReferenceArticle(Integer tailleLettreReferenceArticle) {
        this.tailleLettreReferenceArticle = tailleLettreReferenceArticle;
    }

    public Integer getTailleNumeroReferenceArticle() {
        return tailleNumeroReferenceArticle != null ? tailleNumeroReferenceArticle : 3;
    }

    public void setTailleNumeroReferenceArticle(Integer tailleNumeroReferenceArticle) {
        this.tailleNumeroReferenceArticle = tailleNumeroReferenceArticle;
    }

    public Boolean getGenererPassword() {
        return genererPassword != null ? genererPassword : false;
    }

    public void setGenererPassword(Boolean genererPassword) {
        this.genererPassword = genererPassword;
    }

    public Integer getNombreEltAccueil() {
        return nombreEltAccueil != null ? nombreEltAccueil : 5;
    }

    public void setNombreEltAccueil(Integer nombreEltAccueil) {
        this.nombreEltAccueil = nombreEltAccueil;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public String getDefautPassword() {
        return defautPassword != null ? defautPassword : "ADMIN";
    }

    public void setDefautPassword(String defautPassword) {
        this.defautPassword = defautPassword;
    }

    public Double getTauxEcartPr() {
        return tauxEcartPr != null ? tauxEcartPr : 0;
    }

    public void setTauxEcartPr(Double tauxEcartPr) {
        this.tauxEcartPr = tauxEcartPr;
    }

    public Boolean getMonitorPr() {
        return monitorPr != null ? monitorPr : false;
    }

    public void setMonitorPr(Boolean monitorPr) {
        this.monitorPr = monitorPr;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Integer getDureeInactivArticle() {
        return dureeInactivArticle != null ? dureeInactivArticle : 30;
    }

    public void setDureeInactivArticle(Integer dureeInactivArticle) {
        this.dureeInactivArticle = dureeInactivArticle;
    }

    public String getBackColorEtiquette() {
        return backColorEtiquette != null ? backColorEtiquette.trim().length() > 0 ? backColorEtiquette : "FFAF02" : "FFAF02";
    }

    public void setBackColorEtiquette(String backColorEtiquette) {
        this.backColorEtiquette = backColorEtiquette;
    }

    public String getForeColorEtiquette() {
        return foreColorEtiquette != null ? foreColorEtiquette.trim().length() > 0 ? foreColorEtiquette : "C78801" : "C78801";
    }

    public void setForeColorEtiquette(String foreColorEtiquette) {
        this.foreColorEtiquette = foreColorEtiquette;
    }

    public Integer getNombrePageMin() {
        return nombrePageMin != null ? nombrePageMin : 10;
    }

    public void setNombrePageMin(Integer nombrePageMin) {
        this.nombrePageMin = nombrePageMin;
    }

    public Boolean isDisplayAvis() {
        return displayAvis;
    }

    public void setDisplayAvis(Boolean displayAvis) {
        this.displayAvis = displayAvis;
    }

    public Boolean isDisplayCaracteristique() {
        return displayCaracteristique;
    }

    public void setDisplayCaracteristique(Boolean displayCaracteristique) {
        this.displayCaracteristique = displayCaracteristique;
    }

    public Boolean isDisplayAnalytique() {
        return displayAnalytique;
    }

    public void setDisplayAnalytique(Boolean displayAnalytique) {
        this.displayAnalytique = displayAnalytique;
    }

    public Boolean isDisplayFournisseur() {
        return displayFournisseur;
    }

    public void setDisplayFournisseur(Boolean displayFournisseur) {
        this.displayFournisseur = displayFournisseur;
    }

    public Boolean isDisplayTarifZone() {
        return displayTarifZone;
    }

    public void setDisplayTarifZone(Boolean displayTarifZone) {
        this.displayTarifZone = displayTarifZone;
    }

    public Boolean isDisplayEquivalent() {
        return displayEquivalent;
    }

    public void setDisplayEquivalent(Boolean displayEquivalent) {
        this.displayEquivalent = displayEquivalent;
    }

    public Boolean isDisplayProduction() {
        return displayProduction;
    }

    public void setDisplayProduction(Boolean displayProduction) {
        this.displayProduction = displayProduction;
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
        if (!(object instanceof YvsBaseParametre)) {
            return false;
        }
        YvsBaseParametre other = (YvsBaseParametre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseParametre[ id=" + id + " ]";
    }

}

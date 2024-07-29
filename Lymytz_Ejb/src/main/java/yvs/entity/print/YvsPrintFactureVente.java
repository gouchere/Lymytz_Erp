/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.print;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
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
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_print_facture_vente", catalog = "lymytz_demo_0", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsPrintFactureVente.findAll", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsPrintFactureVente.findById", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsPrintFactureVente.findByNom", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.nom = :nom AND y.societe = :societe"),
    @NamedQuery(name = "YvsPrintFactureVente.findByDefaut", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.defaut = :defaut AND y.societe = :societe"),
    @NamedQuery(name = "YvsPrintFactureVente.findByModel", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.model = :model AND y.societe = :societe"),
    @NamedQuery(name = "YvsPrintFactureVente.findByDateUpdate", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsPrintFactureVente.findByDateSave", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsPrintFactureVente.findByViewDateFacture", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.viewDateFacture = :viewDateFacture"),
    @NamedQuery(name = "YvsPrintFactureVente.findByViewNumeroFacture", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.viewNumeroFacture = :viewNumeroFacture"),
    @NamedQuery(name = "YvsPrintFactureVente.findByViewTaxeFacture", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.viewTaxeFacture = :viewTaxeFacture"),
    @NamedQuery(name = "YvsPrintFactureVente.findByViewCoutFacture", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.viewCoutFacture = :viewCoutFacture"),
    @NamedQuery(name = "YvsPrintFactureVente.findByViewServiceFacture", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.viewServiceFacture = :viewServiceFacture"),
    @NamedQuery(name = "YvsPrintFactureVente.findByViewReglementFacture", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.viewReglementFacture = :viewReglementFacture"),
    @NamedQuery(name = "YvsPrintFactureVente.findByViewStatutFacture", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.viewStatutFacture = :viewStatutFacture"),
    @NamedQuery(name = "YvsPrintFactureVente.findByViewNameVendeur", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.viewNameVendeur = :viewNameVendeur"),
    @NamedQuery(name = "YvsPrintFactureVente.findByViewPointVente", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.viewPointVente = :viewPointVente"),
    @NamedQuery(name = "YvsPrintFactureVente.findByViewNameClient", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.viewNameClient = :viewNameClient"),
    @NamedQuery(name = "YvsPrintFactureVente.findByViewPhoneClient", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.viewPhoneClient = :viewPhoneClient"),
    @NamedQuery(name = "YvsPrintFactureVente.findByViewSignatureVendeur", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.viewSignatureVendeur = :viewSignatureVendeur"),
    @NamedQuery(name = "YvsPrintFactureVente.findByViewSignatureCaissier", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.viewSignatureCaissier = :viewSignatureCaissier"),
    @NamedQuery(name = "YvsPrintFactureVente.findByViewSignatureClient", query = "SELECT y FROM YvsPrintFactureVente y WHERE y.viewSignatureClient = :viewSignatureClient")})
public class YvsPrintFactureVente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", columnDefinition = "SERIAL")
    @SequenceGenerator(sequenceName = "yvs_print_facture_vente_id_seq", name = "yvs_print_facture_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_print_facture_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "nom")
    private String nom;
    @Size(max = 2147483647)
    @Column(name = "model")
    private String model;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "defaut")
    private Boolean defaut;
    @Column(name = "view_date_facture")
    private Boolean viewDateFacture;
    @Column(name = "view_numero_facture")
    private Boolean viewNumeroFacture;
    @Column(name = "view_taxe_facture")
    private Boolean viewTaxeFacture;
    @Column(name = "view_cout_facture")
    private Boolean viewCoutFacture;
    @Column(name = "view_service_facture")
    private Boolean viewServiceFacture;
    @Column(name = "view_reglement_facture")
    private Boolean viewReglementFacture;
    @Column(name = "view_statut_facture")
    private Boolean viewStatutFacture;
    @Column(name = "view_name_vendeur")
    private Boolean viewNameVendeur;
    @Column(name = "view_point_vente")
    private Boolean viewPointVente;
    @Column(name = "view_name_client")
    private Boolean viewNameClient;
    @Column(name = "view_phone_client")
    private Boolean viewPhoneClient;
    @Column(name = "view_adresse_client")
    private Boolean viewAdresseClient;
    @Column(name = "view_nui_client")
    private Boolean viewNuiClient;
    @Column(name = "view_rcc_client")
    private Boolean viewRccClient;
    @Column(name = "view_signature_vendeur")
    private Boolean viewSignatureVendeur;
    @Column(name = "view_signature_caissier")
    private Boolean viewSignatureCaissier;
    @Column(name = "view_signature_client")
    private Boolean viewSignatureClient;
    @Column(name = "view_image_payer")
    private Boolean viewImagePayer;
    @Column(name = "view_image_livrer")
    private Boolean viewImageLivrer;
    @Column(name = "view_impaye_vente")
    private Boolean viewImpayeVente;
    @Column(name = "view_penalite_facture")
    private Boolean viewPenaliteFacture;

    @JoinColumn(name = "header", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsPrintHeader header;
    @JoinColumn(name = "footer", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsPrintFooter footer;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsPrintFactureVente() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsPrintFactureVente(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public Boolean getDefaut() {
        return defaut != null ? defaut : false;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
    }

    public Boolean getViewDateFacture() {
        return viewDateFacture != null ? viewDateFacture : true;
    }

    public void setViewDateFacture(Boolean viewDateFacture) {
        this.viewDateFacture = viewDateFacture;
    }

    public Boolean getViewNumeroFacture() {
        return viewNumeroFacture != null ? viewNumeroFacture : true;
    }

    public void setViewNumeroFacture(Boolean viewNumeroFacture) {
        this.viewNumeroFacture = viewNumeroFacture;
    }

    public Boolean getViewTaxeFacture() {
        return viewTaxeFacture != null ? viewTaxeFacture : true;
    }

    public void setViewTaxeFacture(Boolean viewTaxeFacture) {
        this.viewTaxeFacture = viewTaxeFacture;
    }

    public Boolean getViewCoutFacture() {
        return viewCoutFacture != null ? viewCoutFacture : true;
    }

    public void setViewCoutFacture(Boolean viewCoutFacture) {
        this.viewCoutFacture = viewCoutFacture;
    }

    public Boolean getViewServiceFacture() {
        return viewServiceFacture != null ? viewServiceFacture : true;
    }

    public void setViewServiceFacture(Boolean viewServiceFacture) {
        this.viewServiceFacture = viewServiceFacture;
    }

    public Boolean getViewReglementFacture() {
        return viewReglementFacture != null ? viewReglementFacture : true;
    }

    public void setViewReglementFacture(Boolean viewReglementFacture) {
        this.viewReglementFacture = viewReglementFacture;
    }

    public Boolean getViewStatutFacture() {
        return viewStatutFacture != null ? viewStatutFacture : true;
    }

    public void setViewStatutFacture(Boolean viewStatutFacture) {
        this.viewStatutFacture = viewStatutFacture;
    }

    public Boolean getViewNameVendeur() {
        return viewNameVendeur != null ? viewNameVendeur : true;
    }

    public void setViewNameVendeur(Boolean viewNameVendeur) {
        this.viewNameVendeur = viewNameVendeur;
    }

    public Boolean getViewPointVente() {
        return viewPointVente != null ? viewPointVente : true;
    }

    public void setViewPointVente(Boolean viewPointVente) {
        this.viewPointVente = viewPointVente;
    }

    public Boolean getViewNameClient() {
        return viewNameClient != null ? viewNameClient : true;
    }

    public void setViewNameClient(Boolean viewNameClient) {
        this.viewNameClient = viewNameClient;
    }

    public Boolean getViewPhoneClient() {
        return viewPhoneClient != null ? viewPhoneClient : true;
    }

    public void setViewPhoneClient(Boolean viewPhoneClient) {
        this.viewPhoneClient = viewPhoneClient;
    }

    public Boolean getViewImpayeVente() {
        return viewImpayeVente != null ? viewImpayeVente : true;
    }

    public void setViewImpayeVente(Boolean viewImpayeVente) {
        this.viewImpayeVente = viewImpayeVente;
    }

    public Boolean getViewPenaliteFacture() {
        return viewPenaliteFacture != null ? viewPenaliteFacture : true;
    }

    public void setViewPenaliteFacture(Boolean viewPenaliteFacture) {
        this.viewPenaliteFacture = viewPenaliteFacture;
    }

    public Boolean getViewAdresseClient() {
        return viewAdresseClient != null ? viewAdresseClient : true;
    }

    public void setViewAdresseClient(Boolean viewAdresseClient) {
        this.viewAdresseClient = viewAdresseClient;
    }

    public Boolean getViewNuiClient() {
        return viewNuiClient != null ? viewNuiClient : true;
    }

    public void setViewNuiClient(Boolean viewNuiClient) {
        this.viewNuiClient = viewNuiClient;
    }

    public Boolean getViewRccClient() {
        return viewRccClient != null ? viewRccClient : true;
    }

    public void setViewRccClient(Boolean viewRccClient) {
        this.viewRccClient = viewRccClient;
    }

    public Boolean getViewSignatureVendeur() {
        return viewSignatureVendeur != null ? viewSignatureVendeur : true;
    }

    public void setViewSignatureVendeur(Boolean viewSignatureVendeur) {
        this.viewSignatureVendeur = viewSignatureVendeur;
    }

    public Boolean getViewSignatureCaissier() {
        return viewSignatureCaissier != null ? viewSignatureCaissier : true;
    }

    public void setViewSignatureCaissier(Boolean viewSignatureCaissier) {
        this.viewSignatureCaissier = viewSignatureCaissier;
    }

    public Boolean getViewSignatureClient() {
        return viewSignatureClient != null ? viewSignatureClient : true;
    }

    public void setViewSignatureClient(Boolean viewSignatureClient) {
        this.viewSignatureClient = viewSignatureClient;
    }

    public Boolean getViewImagePayer() {
        return viewImagePayer != null ? viewImagePayer : true;
    }

    public void setViewImagePayer(Boolean viewImagePayer) {
        this.viewImagePayer = viewImagePayer;
    }

    public Boolean getViewImageLivrer() {
        return viewImageLivrer != null ? viewImageLivrer : true;
    }

    public void setViewImageLivrer(Boolean viewImageLivrer) {
        this.viewImageLivrer = viewImageLivrer;
    }

    public YvsPrintFooter getFooter() {
        return footer;
    }

    public void setFooter(YvsPrintFooter footer) {
        this.footer = footer;
    }

    public YvsPrintHeader getHeader() {
        return header;
    }

    public void setHeader(YvsPrintHeader header) {
        this.header = header;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsPrintFactureVente)) {
            return false;
        }
        YvsPrintFactureVente other = (YvsPrintFactureVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.print.YvsPrintFactureVente[ id=" + id + " ]";
    }

}

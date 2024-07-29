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
@Table(name = "yvs_print_ticket_vente", catalog = "lymytz_demo_0", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsPrintTicketVente.findAll", query = "SELECT y FROM YvsPrintTicketVente y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsPrintTicketVente.findById", query = "SELECT y FROM YvsPrintTicketVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsPrintTicketVente.findByNom", query = "SELECT y FROM YvsPrintTicketVente y WHERE y.nom = :nom AND y.societe = :societe"),
    @NamedQuery(name = "YvsPrintTicketVente.findByDefaut", query = "SELECT y FROM YvsPrintTicketVente y WHERE y.defaut = :defaut AND y.societe = :societe"),
    @NamedQuery(name = "YvsPrintTicketVente.findByModel", query = "SELECT y FROM YvsPrintTicketVente y WHERE y.model = :model AND y.societe = :societe"),
    @NamedQuery(name = "YvsPrintTicketVente.findByDateUpdate", query = "SELECT y FROM YvsPrintTicketVente y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsPrintTicketVente.findByDateSave", query = "SELECT y FROM YvsPrintTicketVente y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsPrintTicketVente.findByViewDateFacture", query = "SELECT y FROM YvsPrintTicketVente y WHERE y.viewDateFacture = :viewDateFacture"),
    @NamedQuery(name = "YvsPrintTicketVente.findByViewNumeroFacture", query = "SELECT y FROM YvsPrintTicketVente y WHERE y.viewNumeroFacture = :viewNumeroFacture")})
public class YvsPrintTicketVente implements Serializable {

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
    @Column(name = "view_name_societe")
    private Boolean viewNameSociete;
    @Column(name = "view_rcc_societe")
    private Boolean viewRccSociete;
    @Column(name = "view_nui_societe")
    private Boolean viewNuiSociete;
    @Column(name = "view_phone_societe")
    private Boolean viewPhoneSociete;
    @Column(name = "view_adresse_societe")
    private Boolean viewAdresseSociete;
    @Column(name = "view_date_facture")
    private Boolean viewDateFacture;
    @Column(name = "view_numero_facture")
    private Boolean viewNumeroFacture;
    @Column(name = "view_name_vendeur")
    private Boolean viewNameVendeur;
    @Column(name = "view_name_client")
    private Boolean viewNameClient;
    @Column(name = "view_phone_client")
    private Boolean viewPhoneClient;
    @Column(name = "view_statut_regler_facture")
    private Boolean viewStatutReglerFacture;
    @Column(name = "view_image_qr_facture")
    private Boolean viewImageQrFacture;

    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsPrintTicketVente() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsPrintTicketVente(Long id) {
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

    public Boolean getViewImageQrFacture() {
        return viewImageQrFacture != null ? viewImageQrFacture : true;
    }

    public void setViewImageQrFacture(Boolean viewImageQrFacture) {
        this.viewImageQrFacture = viewImageQrFacture;
    }

    public Boolean getViewNameSociete() {
        return viewNameSociete != null ? viewNameSociete : true;
    }

    public void setViewNameSociete(Boolean viewNameSociete) {
        this.viewNameSociete = viewNameSociete;
    }

    public Boolean getViewRccSociete() {
        return viewRccSociete != null ? viewRccSociete : true;
    }

    public void setViewRccSociete(Boolean viewRccSociete) {
        this.viewRccSociete = viewRccSociete;
    }

    public Boolean getViewNuiSociete() {
        return viewNuiSociete != null ? viewNuiSociete : true;
    }

    public void setViewNuiSociete(Boolean viewNuiSociete) {
        this.viewNuiSociete = viewNuiSociete;
    }

    public Boolean getViewStatutReglerFacture() {
        return viewStatutReglerFacture != null ? viewStatutReglerFacture : true;
    }

    public void setViewStatutReglerFacture(Boolean viewStatutReglerFacture) {
        this.viewStatutReglerFacture = viewStatutReglerFacture;
    }

    public Boolean getViewNameVendeur() {
        return viewNameVendeur != null ? viewNameVendeur : true;
    }

    public void setViewNameVendeur(Boolean viewNameVendeur) {
        this.viewNameVendeur = viewNameVendeur;
    }

    public Boolean getViewPhoneSociete() {
        return viewPhoneSociete != null ? viewPhoneSociete : true;
    }

    public void setViewPhoneSociete(Boolean viewPhoneSociete) {
        this.viewPhoneSociete = viewPhoneSociete;
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

    public Boolean getViewAdresseSociete() {
        return viewAdresseSociete != null ? viewAdresseSociete : true;
    }

    public void setViewAdresseSociete(Boolean viewAdresseSociete) {
        this.viewAdresseSociete = viewAdresseSociete;
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
        if (!(object instanceof YvsPrintTicketVente)) {
            return false;
        }
        YvsPrintTicketVente other = (YvsPrintTicketVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.print.YvsPrintTicketVente[ id=" + id + " ]";
    }

}

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
@Table(name = "yvs_print_footer", catalog = "lymytz_demo_0", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsPrintFooter.findAll", query = "SELECT y FROM YvsPrintFooter y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsPrintFooter.findById", query = "SELECT y FROM YvsPrintFooter y WHERE y.id = :id"),
    @NamedQuery(name = "YvsPrintFooter.findByNom", query = "SELECT y FROM YvsPrintFooter y WHERE y.nom = :nom AND y.societe = :societe"),
    @NamedQuery(name = "YvsPrintFooter.findByModel", query = "SELECT y FROM YvsPrintFooter y WHERE y.model = :model AND y.societe = :societe"),
    @NamedQuery(name = "YvsPrintFooter.findByDateUpdate", query = "SELECT y FROM YvsPrintFooter y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsPrintFooter.findByDateSave", query = "SELECT y FROM YvsPrintFooter y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsPrintFooter.findByViewAdresseSociete", query = "SELECT y FROM YvsPrintFooter y WHERE y.viewAdresseSociete = :viewAdresseSociete"),
    @NamedQuery(name = "YvsPrintFooter.findByViewSiegeSociete", query = "SELECT y FROM YvsPrintFooter y WHERE y.viewSiegeSociete = :viewSiegeSociete"),
    @NamedQuery(name = "YvsPrintFooter.findByViewBpSociete", query = "SELECT y FROM YvsPrintFooter y WHERE y.viewBpSociete = :viewBpSociete"),
    @NamedQuery(name = "YvsPrintFooter.findByViewFaxSociete", query = "SELECT y FROM YvsPrintFooter y WHERE y.viewFaxSociete = :viewFaxSociete"),
    @NamedQuery(name = "YvsPrintFooter.findByViewEmailSociete", query = "SELECT y FROM YvsPrintFooter y WHERE y.viewEmailSociete = :viewEmailSociete"),
    @NamedQuery(name = "YvsPrintFooter.findByViewPhoneSociete", query = "SELECT y FROM YvsPrintFooter y WHERE y.viewPhoneSociete = :viewPhoneSociete"),
    @NamedQuery(name = "YvsPrintFooter.findByViewSiteSociete", query = "SELECT y FROM YvsPrintFooter y WHERE y.viewSiteSociete = :viewSiteSociete"),
    @NamedQuery(name = "YvsPrintFooter.findByViewContribSociete", query = "SELECT y FROM YvsPrintFooter y WHERE y.viewContribSociete = :viewContribSociete"),
    @NamedQuery(name = "YvsPrintFooter.findByViewRegistrSociete", query = "SELECT y FROM YvsPrintFooter y WHERE y.viewRegistrSociete = :viewRegistrSociete")})
public class YvsPrintFooter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", columnDefinition = "SERIAL")
    @SequenceGenerator(sequenceName = "yvs_print_footer_id_seq", name = "yvs_print_footer_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_print_footer_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "view_adresse_societe")
    private Boolean viewAdresseSociete;
    @Column(name = "view_siege_societe")
    private Boolean viewSiegeSociete;
    @Column(name = "view_bp_societe")
    private Boolean viewBpSociete;
    @Column(name = "view_fax_societe")
    private Boolean viewFaxSociete;
    @Column(name = "view_email_societe")
    private Boolean viewEmailSociete;
    @Column(name = "view_phone_societe")
    private Boolean viewPhoneSociete;
    @Column(name = "view_site_societe")
    private Boolean viewSiteSociete;
    @Column(name = "view_contrib_societe")
    private Boolean viewContribSociete;
    @Column(name = "view_registr_societe")
    private Boolean viewRegistrSociete;
    @Column(name = "view_capital_societe")
    private Boolean viewCapitalSociete;
    @Column(name = "view_agreement_societe")
    private Boolean viewAgreementSociete;

    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsPrintFooter() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsPrintFooter(Long id) {
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

    public Boolean getViewAdresseSociete() {
        return viewAdresseSociete != null ? viewAdresseSociete : true;
    }

    public void setViewAdresseSociete(Boolean viewAdresseSociete) {
        this.viewAdresseSociete = viewAdresseSociete;
    }

    public Boolean getViewSiegeSociete() {
        return viewSiegeSociete != null ? viewSiegeSociete : true;
    }

    public void setViewSiegeSociete(Boolean viewSiegeSociete) {
        this.viewSiegeSociete = viewSiegeSociete;
    }

    public Boolean getViewBpSociete() {
        return viewBpSociete != null ? viewBpSociete : true;
    }

    public void setViewBpSociete(Boolean viewBpSociete) {
        this.viewBpSociete = viewBpSociete;
    }

    public Boolean getViewFaxSociete() {
        return viewFaxSociete != null ? viewFaxSociete : true;
    }

    public void setViewFaxSociete(Boolean viewFaxSociete) {
        this.viewFaxSociete = viewFaxSociete;
    }

    public Boolean getViewEmailSociete() {
        return viewEmailSociete != null ? viewEmailSociete : true;
    }

    public void setViewEmailSociete(Boolean viewEmailSociete) {
        this.viewEmailSociete = viewEmailSociete;
    }

    public Boolean getViewPhoneSociete() {
        return viewPhoneSociete != null ? viewPhoneSociete : true;
    }

    public void setViewPhoneSociete(Boolean viewPhoneSociete) {
        this.viewPhoneSociete = viewPhoneSociete;
    }

    public Boolean getViewAgreementSociete() {
        return viewAgreementSociete != null ? viewAgreementSociete : true;
    }

    public void setViewAgreementSociete(Boolean viewAgreementSociete) {
        this.viewAgreementSociete = viewAgreementSociete;
    }

    public Boolean getViewSiteSociete() {
        return viewSiteSociete != null ? viewSiteSociete : true;
    }

    public void setViewSiteSociete(Boolean viewSiteSociete) {
        this.viewSiteSociete = viewSiteSociete;
    }

    public Boolean getViewContribSociete() {
        return viewContribSociete != null ? viewContribSociete : true;
    }

    public void setViewContribSociete(Boolean viewContribSociete) {
        this.viewContribSociete = viewContribSociete;
    }

    public Boolean getViewRegistrSociete() {
        return viewRegistrSociete != null ? viewRegistrSociete : true;
    }

    public void setViewRegistrSociete(Boolean viewRegistrSociete) {
        this.viewRegistrSociete = viewRegistrSociete;
    }

    public Boolean getViewCapitalSociete() {
        return viewCapitalSociete != null ? viewCapitalSociete : true;
    }

    public void setViewCapitalSociete(Boolean viewCapitalSociete) {
        this.viewCapitalSociete = viewCapitalSociete;
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
        if (!(object instanceof YvsPrintFooter)) {
            return false;
        }
        YvsPrintFooter other = (YvsPrintFooter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.print.YvsPrintFooter[ id=" + id + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.vente;

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
import javax.persistence.Transient;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.YvsEntity;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_proformat_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComProformaVente.findAll", query = "SELECT y FROM YvsComProformaVente y WHERE y.agence.societe = :societe ORDER BY y.dateDoc DESC"),
    @NamedQuery(name = "YvsComProformaVente.findById", query = "SELECT y FROM YvsComProformaVente y  WHERE y.id = :id"),
    @NamedQuery(name = "YvsComProformaVente.findByIds", query = "SELECT y FROM YvsComProformaVente y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsComProformaVente.findByReference", query = "SELECT y FROM YvsComProformaVente y WHERE y.agence.societe = :societe AND y.numero LIKE :reference ORDER by y.numero DESC"),})
public class YvsComProformaVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_proformat_vente_id_seq", name = "yvs_com_proformat_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_proformat_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numero")
    private String numero;
    @Column(name = "statut")
    private String statut;
    @Column(name = "client")
    private String client;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "description")
    private String description;
    @Column(name = "date_doc")
    @Temporal(TemporalType.DATE)
    private Date dateDoc;
    @Column(name = "date_expiration")
    @Temporal(TemporalType.DATE)
    private Date dateExpiration;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private boolean new_;

    public YvsComProformaVente() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComProformaVente(Long id) {
        this();
        this.id = id;
    }

    public YvsComProformaVente(Long id, String numDoc) {
        this(id);
        this.numero = numDoc;
    }

    public YvsComProformaVente(Long id, String numDoc, String statut) {
        this(id, numDoc);
        this.statut = statut;
    }

    @Override
    public Long getId() {
        return id; //To change body of generated methods, choose Tools | Templates.
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getStatut() {
        return statut != null ? statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getDateDoc() {
        return dateDoc;
    }

    public void setDateDoc(Date dateDoc) {
        this.dateDoc = dateDoc;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean canEditable() {
        return statut.equals(Constantes.ETAT_ATTENTE) || statut.equals(Constantes.ETAT_EDITABLE);
    }

    public boolean canDelete() {
        return statut.equals(Constantes.ETAT_ATTENTE) || statut.equals(Constantes.ETAT_EDITABLE) || statut.equals(Constantes.ETAT_SUSPENDU) || statut.equals(Constantes.ETAT_ANNULE);
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsComProformaVente)) {
            return false;
        }
        YvsComProformaVente other = (YvsComProformaVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.vente.YvsComProformaVente[ id=" + id + " ]";
    }
}

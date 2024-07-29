/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial;

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
import yvs.dao.YvsEntity;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_parametre")
@NamedQueries({
    @NamedQuery(name = "YvsComParametre.findAll", query = "SELECT y FROM YvsComParametre y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsComParametre.findById", query = "SELECT y FROM YvsComParametre y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComParametre.findJourUsine", query = "SELECT y.jourUsine FROM YvsComParametre y WHERE y.societe=:societe"),
    @NamedQuery(name = "YvsComParametre.findInitJour", query = "SELECT y.jourDebutMois FROM YvsComParametre y WHERE y.societe=:societe"),
    @NamedQuery(name = "YvsComParametre.findByReglementAuto", query = "SELECT y FROM YvsComParametre y WHERE y.reglementAuto = :reglementAuto")})
public class YvsComParametre extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_parametre_id_seq", name = "yvs_com_parametre_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_parametre_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "reglement_auto")
    private Boolean reglementAuto;
    @Column(name = "use_lot_reception")
    private Boolean useLotReception;
    @Size(max = 2147483647)
    @Column(name = "document_mouv_achat")
    private String documentMouvAchat;
    @Size(max = 2147483647)
    @Column(name = "document_mouv_vente")
    private String documentMouvVente;
    @Size(max = 2147483647)
    @Column(name = "taux_marge_sur")
    private String tauxMargeSur = "V";
    @Size(max = 2147483647)
    @Column(name = "document_generer_from_ecart")
    private String documentGenererFromEcart;
    @Size(max = 2147483647)
    @Column(name = "mode_inventaire")
    private String modeInventaire;
    @Column(name = "converter")
    private Integer converter;
    @Column(name = "converter_cs")
    private Integer converterCs;
    @Column(name = "seuil_fsseur")
    private Double seuilFsseur;
    @Column(name = "seuil_client")
    private Double seuilClient;
    @Column(name = "duree_inactiv")
    private Integer dureeInactiv;
    @Column(name = "jour_usine")
    private Integer jourUsine = 30;
    @Column(name = "jour_debut_mois")
    private Integer jourDebutMois = 1;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsComParametre() {
    }

    public YvsComParametre(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getConverter() {
        return converter != null ? converter : 0;
    }

    public void setConverter(Integer converter) {
        this.converter = converter;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Integer getDureeInactiv() {
        return dureeInactiv != null ? dureeInactiv : 0;
    }

    public void setDureeInactiv(Integer dureeInactiv) {
        this.dureeInactiv = dureeInactiv;
    }

    public Double getSeuilFsseur() {
        return seuilFsseur != null ? seuilFsseur : 0;
    }

    public void setSeuilFsseur(Double seuilFsseur) {
        this.seuilFsseur = seuilFsseur;
    }

    public Double getSeuilClient() {
        return seuilClient != null ? seuilClient : 0;
    }

    public void setSeuilClient(Double seuilClient) {
        this.seuilClient = seuilClient;
    }

    public Boolean getUseLotReception() {
        return useLotReception != null ? useLotReception : false;
    }

    public void setUseLotReception(Boolean useLotReception) {
        this.useLotReception = useLotReception;
    }

    public Boolean getReglementAuto() {
        return reglementAuto != null ? reglementAuto : false;
    }

    public void setReglementAuto(Boolean reglementAuto) {
        this.reglementAuto = reglementAuto;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public String getDocumentMouvAchat() {
        return documentMouvAchat;
    }

    public void setDocumentMouvAchat(String documentMouvAchat) {
        this.documentMouvAchat = documentMouvAchat;
    }

    public String getDocumentMouvVente() {
        return documentMouvVente;
    }

    public void setDocumentMouvVente(String documentMouvVente) {
        this.documentMouvVente = documentMouvVente;
    }

    public String getTauxMargeSur() {
        return tauxMargeSur != null ? !tauxMargeSur.trim().isEmpty() ? tauxMargeSur : "V" : "V";
    }

    public void setTauxMargeSur(String tauxMargeSur) {
        this.tauxMargeSur = tauxMargeSur;
    }

    public String getDocumentGenererFromEcart() {
        return documentGenererFromEcart != null ? !documentGenererFromEcart.trim().isEmpty() ? documentGenererFromEcart : "RE" : "RE";
    }

    public void setDocumentGenererFromEcart(String documentGenererFromEcart) {
        this.documentGenererFromEcart = documentGenererFromEcart;
    }

    public String getModeInventaire() {
        return modeInventaire;
    }

    public void setModeInventaire(String modeInventaire) {
        this.modeInventaire = modeInventaire;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Integer getJourUsine() {
        return jourUsine != null ? jourUsine : 1;
    }

    public void setJourUsine(Integer jourUsine) {
        this.jourUsine = jourUsine;
    }

    public Integer getConverterCs() {
        return converterCs != null ? converterCs : 2;
    }

    public void setConverterCs(Integer converterCs) {
        this.converterCs = converterCs;
    }

    public Integer getJourDebutMois() {
        return jourDebutMois;
    }

    public void setJourDebutMois(Integer jourDebutMois) {
        this.jourDebutMois = jourDebutMois;
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
        if (!(object instanceof YvsComParametre)) {
            return false;
        }
        YvsComParametre other = (YvsComParametre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.YvsComParametre[ id=" + id + " ]";
    }

}

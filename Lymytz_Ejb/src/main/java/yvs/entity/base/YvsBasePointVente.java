/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

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
import yvs.dao.YvsEntity;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.commercial.YvsComCommercialPoint;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_point_vente")
@NamedQueries({
    @NamedQuery(name = "YvsBasePointVente.findAll", query = "SELECT DISTINCT y FROM YvsBasePointVente y LEFT JOIN FETCH y.listTranche WHERE y.agence.societe = :societe ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBasePointVente.findByAgence", query = "SELECT y FROM YvsBasePointVente y WHERE y.agence = :agence ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBasePointVente.findByAgenceActif", query = "SELECT y FROM YvsBasePointVente y WHERE y.agence = :agence AND y.actif = :actif ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBasePointVente.findById", query = "SELECT y FROM YvsBasePointVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBasePointVente.findByIds", query = "SELECT y FROM YvsBasePointVente y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsBasePointVente.findByCode", query = "SELECT y FROM YvsBasePointVente y WHERE y.code = :code AND y.agence.societe = :societe ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBasePointVente.findByLibelle", query = "SELECT y FROM YvsBasePointVente y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsBasePointVente.findByActif", query = "SELECT y FROM YvsBasePointVente y WHERE y.agence.societe = :societe AND y.actif = :actif ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBasePointVente.findByVenteOnlineActif", query = "SELECT y FROM YvsBasePointVente y WHERE y.agence.societe.venteOnline = :venteOnline AND y.actif = :actif ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBasePointVente.findByVenteOnline", query = "SELECT y FROM YvsBasePointVente y WHERE y.agence.societe = :societe AND y.venteOnline = :venteOnline"),
    @NamedQuery(name = "YvsBasePointVente.findByAdresse", query = "SELECT y FROM YvsBasePointVente y WHERE y.adresse = :adresse"),
    @NamedQuery(name = "YvsBasePointVente.findByResponsable", query = "SELECT y FROM YvsBasePointVente y WHERE y.responsable = :responsable"),
    @NamedQuery(name = "YvsBasePointVente.findByResponsableAgence", query = "SELECT y FROM YvsBasePointVente y WHERE y.agence = :agence AND y.responsable = :responsable AND y.actif = true")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBasePointVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_point_vente_id_seq", name = "yvs_base_point_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_point_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Size(max = 2147483647)
    @Column(name = "adresse")
    private String adresse;
    @Size(max = 2147483647)
    @Column(name = "photo")
    private String photo;
    @Size(max = 2147483647)
    @Column(name = "telephone")
    private String telephone;
    @Size(max = 2147483647)
    @Column(name = "code_pin")
    private String codePin;
    @Size(max = 2147483647)
    @Column(name = "color")
    private String color;
    @Column(name = "type")
    private Character type = Constantes.SERVICE_COMMERCE_GENERAL;
    @Column(name = "accept_client_no_name")
    private Boolean acceptClientNoName = false;
    @Column(name = "validation_reglement")
    private Boolean validationReglement = false;
    @Column(name = "reglement_auto")
    private Boolean reglementAuto = false;
    @Column(name = "actif")
    private Boolean actif = true;
    @Column(name = "prix_min_strict")
    private Boolean prixMinStrict = false;
    @Column(name = "livraison_on")
    private Character livraisonOn;
    @Column(name = "commission_for")
    private Character commissionFor;
    @Column(name = "vente_online")
    private Boolean venteOnline = false;
    @Column(name = "activer_notification")
    private Boolean activerNotification = false;
    @Column(name = "saisie_phone_obligatoire")
    private Boolean saisiePhoneObligatoire = false;
    @Column(name = "miminum_active_livraison")
    private Double miminumActiveLivraison;
    @Column(name = "comptabilisation_auto")
    private Boolean comptabilisationAuto;

    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComClient client;
    @JoinColumn(name = "secteur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire secteur;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes responsable;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePointVente parent;

    @OneToMany(mappedBy = "pointVente", fetch = FetchType.LAZY)
    private List<YvsBasePointVenteDepot> depots; //dépôt rattaché aux points de vente
    @OneToMany(mappedBy = "point", fetch = FetchType.LAZY)
    private List<YvsBaseArticlePoint> articles;
    @OneToMany(mappedBy = "point", fetch = FetchType.LAZY)
    private List<YvsComCreneauPoint> listTranche; //planification 
    @OneToMany(mappedBy = "point", fetch = FetchType.LAZY)
    private List<YvsComCommercialPoint> commerciaux; //planification 

    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsBasePointVente() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        articles = new ArrayList<>();
        depots = new ArrayList<>();
        commerciaux = new ArrayList<>();
        listTranche = new ArrayList<>();
    }

    public YvsBasePointVente(Long id) {
        this();
        this.id = id;
    }

    public YvsBasePointVente(Long id, String libelle) {
        this(id);
        this.libelle = libelle;
    }

    @Override
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

    public Double getMiminumActiveLivraison() {
        return miminumActiveLivraison != null ? miminumActiveLivraison : 0;
    }

    public void setMiminumActiveLivraison(Double miminumActiveLivraison) {
        this.miminumActiveLivraison = miminumActiveLivraison;
    }

    public Boolean getAcceptClientNoName() {
        return acceptClientNoName != null ? acceptClientNoName : false;
    }

    public void setAcceptClientNoName(Boolean acceptClientNoName) {
        this.acceptClientNoName = acceptClientNoName;
    }

    public Character getCommissionFor() {
        return commissionFor != null ? String.valueOf(commissionFor).trim().length() > 0 ? commissionFor : 'C' : 'C';
    }

    public void setCommissionFor(Character commissionFor) {
        this.commissionFor = commissionFor;
    }

    public Character getLivraisonOn() {
        return livraisonOn != null ? String.valueOf(livraisonOn).trim().length() > 0 ? livraisonOn : 'V' : 'V';
    }

    public void setLivraisonOn(Character livraisonOn) {
        this.livraisonOn = livraisonOn;
    }

    public Boolean getReglementAuto() {
        return reglementAuto != null ? reglementAuto : false;
    }

    public void setReglementAuto(Boolean reglementAuto) {
        this.reglementAuto = reglementAuto;
    }

    public Boolean getValidationReglement() {
        return validationReglement != null ? validationReglement : false;
    }

    public void setValidationReglement(Boolean validationReglement) {
        this.validationReglement = validationReglement;
    }

    public Boolean getSaisiePhoneObligatoire() {
        return saisiePhoneObligatoire != null ? saisiePhoneObligatoire : false;
    }

    public void setSaisiePhoneObligatoire(Boolean saisiePhoneObligatoire) {
        this.saisiePhoneObligatoire = saisiePhoneObligatoire;
    }

    public String getCode() {
        return code != null ? code : "";
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCodePin() {
        return codePin != null ? codePin : "";
    }

    public void setCodePin(String codePin) {
        this.codePin = codePin;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Character getType() {
        return type != null ? String.valueOf(type).trim().length() > 0 ? type : Constantes.SERVICE_COMMERCE_GENERAL : Constantes.SERVICE_COMMERCE_GENERAL;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getComptabilisationAuto() {
        return comptabilisationAuto != null ? comptabilisationAuto : false;
    }

    public void setComptabilisationAuto(Boolean comptabilisationAuto) {
        this.comptabilisationAuto = comptabilisationAuto;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getVenteOnline() {
        return venteOnline != null ? venteOnline : false;
    }

    public void setVenteOnline(Boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public Boolean getActiverNotification() {
        return activerNotification != null ? activerNotification : false;
    }

    public void setActiverNotification(Boolean activerNotification) {
        this.activerNotification = activerNotification;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsDictionnaire getSecteur() {
        return secteur;
    }

    public void setSecteur(YvsDictionnaire secteur) {
        this.secteur = secteur;
    }

    public YvsBasePointVente getParent() {
        return parent;
    }

    public void setParent(YvsBasePointVente parent) {
        this.parent = parent;
    }

    public YvsComClient getClient() {
        return client;
    }

    public void setClient(YvsComClient client) {
        this.client = client;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhEmployes getResponsable() {
        return responsable;
    }

    public void setResponsable(YvsGrhEmployes responsable) {
        this.responsable = responsable;
    }

    @XmlTransient
    @JsonIgnore
    @Override
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticlePoint> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticlePoint> articles) {
        this.articles = articles;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBasePointVenteDepot> getDepots() {
        return depots;
    }

    public void setDepots(List<YvsBasePointVenteDepot> depots) {
        this.depots = depots;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComCreneauPoint> getListTranche() {
        return listTranche;
    }

    public void setListTranche(List<YvsComCreneauPoint> listTranche) {
        this.listTranche = listTranche;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComCommercialPoint> getCommerciaux() {
        return commerciaux;
    }

    public void setCommerciaux(List<YvsComCommercialPoint> commerciaux) {
        this.commerciaux = commerciaux;
    }

    public Boolean getPrixMinStrict() {
        return prixMinStrict != null ? prixMinStrict : false;
    }

    public void setPrixMinStrict(Boolean prixMinStrict) {
        this.prixMinStrict = prixMinStrict;
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
        if (!(object instanceof YvsBasePointVente)) {
            return false;
        }
        YvsBasePointVente other = (YvsBasePointVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBasePointVente[ id=" + id + " ]";
    }

}

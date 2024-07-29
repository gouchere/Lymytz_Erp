/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.vente;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBasePointLivraison;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_doc_ventes_informations")
@NamedQueries({
    @NamedQuery(name = "YvsComDocVentesInformations.findAll", query = "SELECT y FROM YvsComDocVentesInformations y"),
    @NamedQuery(name = "YvsComDocVentesInformations.findById", query = "SELECT y FROM YvsComDocVentesInformations y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComDocVentesInformations.findByDateSave", query = "SELECT y FROM YvsComDocVentesInformations y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComDocVentesInformations.findByDateUpdate", query = "SELECT y FROM YvsComDocVentesInformations y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComDocVentesInformations.findByHeureDebut", query = "SELECT y FROM YvsComDocVentesInformations y WHERE y.heureDebut = :heureDebut"),
    @NamedQuery(name = "YvsComDocVentesInformations.findByDateFin", query = "SELECT y FROM YvsComDocVentesInformations y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsComDocVentesInformations.findByHeureFin", query = "SELECT y FROM YvsComDocVentesInformations y WHERE y.heureFin = :heureFin"),
    @NamedQuery(name = "YvsComDocVentesInformations.findByAdresseLivraison", query = "SELECT y FROM YvsComDocVentesInformations y WHERE y.adresseLivraison = :adresseLivraison"),
    @NamedQuery(name = "YvsComDocVentesInformations.findByNumCni", query = "SELECT y FROM YvsComDocVentesInformations y WHERE y.numCni = :numCni"),
    @NamedQuery(name = "YvsComDocVentesInformations.findByNombrePersonne", query = "SELECT y FROM YvsComDocVentesInformations y WHERE y.nombrePersonne = :nombrePersonne"),
    @NamedQuery(name = "YvsComDocVentesInformations.findByNomPersonneSupplementaire", query = "SELECT y FROM YvsComDocVentesInformations y WHERE y.nomPersonneSupplementaire = :nomPersonneSupplementaire"),
    @NamedQuery(name = "YvsComDocVentesInformations.findByNumImmatriculation", query = "SELECT y FROM YvsComDocVentesInformations y WHERE y.numImmatriculation = :numImmatriculation"),
    
    @NamedQuery(name = "YvsComDocVentesInformations.findByFacture", query = "SELECT y FROM YvsComDocVentesInformations y WHERE y.facture = :facture")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComDocVentesInformations extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_doc_ventes_informations_id_seq", name = "yvs_com_doc_ventes_informations_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_doc_ventes_informations_id_seq", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "heure_debut")
    @Temporal(TemporalType.TIME)
    private Date heureDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "heure_fin")
    @Temporal(TemporalType.TIME)
    private Date heureFin;
    @Size(max = 2147483647)
    @Column(name = "num_cni")
    private String numCni;
    @Column(name = "nombre_personne")
    private Integer nombrePersonne;
    @Size(max = 2147483647)
    @Column(name = "nom_personne_supplementaire")
    private String nomPersonneSupplementaire;
    @Size(max = 2147483647)
    @Column(name = "modele_vehicule")
    private String modeleVehicule;
    @Size(max = 2147483647)
    @Column(name = "num_immatriculation")
    private String numImmatriculation;
    
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "adresse_livraison", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePointLivraison adresseLivraison;
    @JoinColumn(name = "facture", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocVentes facture;
    
    @Transient
    private boolean new_;

    public YvsComDocVentesInformations() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComDocVentesInformations(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
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
    public Date getHeureDebut() {
        return heureDebut;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateFin() {
        return dateFin;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getHeureFin() {
        return heureFin;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public String getNumCni() {
        return numCni;
    }

    public void setNumCni(String numCni) {
        this.numCni = numCni;
    }

    public Integer getNombrePersonne() {
        return nombrePersonne != null ? nombrePersonne : 0;
    }

    public void setNombrePersonne(Integer nombrePersonne) {
        this.nombrePersonne = nombrePersonne;
    }

    public String getNomPersonneSupplementaire() {
        return nomPersonneSupplementaire;
    }

    public void setNomPersonneSupplementaire(String nomPersonneSupplementaire) {
        this.nomPersonneSupplementaire = nomPersonneSupplementaire;
    }

    public String getModeleVehicule() {
        return modeleVehicule;
    }

    public void setModeleVehicule(String modeleVehicule) {
        this.modeleVehicule = modeleVehicule;
    }

    public String getNumImmatriculation() {
        return numImmatriculation;
    }

    public void setNumImmatriculation(String numImmatriculation) {
        this.numImmatriculation = numImmatriculation;
    }

    public YvsComDocVentes getFacture() {
        return facture;
    }

    public void setFacture(YvsComDocVentes facture) {
        this.facture = facture;
    }
    
    public YvsBasePointLivraison getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(YvsBasePointLivraison adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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
        if (!(object instanceof YvsComDocVentesInformations)) {
            return false;
        }
        YvsComDocVentesInformations other = (YvsComDocVentesInformations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.vente.YvsComDocVentesInformations[ id=" + id + " ]";
    }

}

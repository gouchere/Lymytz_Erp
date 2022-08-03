/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.salaire;

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
import yvs.entity.grh.param.poste.YvsGrhDepartement;
import yvs.entity.grh.param.poste.YvsGrhPosteDeTravail;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_header_bulletin")
@NamedQueries({
    @NamedQuery(name = "YvsGrhHeaderBulletin.findAll", query = "SELECT y FROM YvsGrhHeaderBulletin y"),
    @NamedQuery(name = "YvsGrhHeaderBulletin.findById", query = "SELECT y FROM YvsGrhHeaderBulletin y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhHeaderBulletin.findByMatricule", query = "SELECT y FROM YvsGrhHeaderBulletin y WHERE y.matricule = :matricule"),
    @NamedQuery(name = "YvsGrhHeaderBulletin.findByCni", query = "SELECT y FROM YvsGrhHeaderBulletin y WHERE y.cni = :cni"),
    @NamedQuery(name = "YvsGrhHeaderBulletin.findByPoste", query = "SELECT y FROM YvsGrhHeaderBulletin y WHERE y.poste = :poste"),
    @NamedQuery(name = "YvsGrhHeaderBulletin.findByPostes", query = "SELECT y FROM YvsGrhHeaderBulletin y WHERE y.idPoste = :poste"),
    @NamedQuery(name = "YvsGrhHeaderBulletin.findByDepartement", query = "SELECT y FROM YvsGrhHeaderBulletin y WHERE y.departement = :departement"),
    @NamedQuery(name = "YvsGrhHeaderBulletin.findByDateEmbauche", query = "SELECT y FROM YvsGrhHeaderBulletin y WHERE y.dateEmbauche = :dateEmbauche"),
    @NamedQuery(name = "YvsGrhHeaderBulletin.findByTelephone", query = "SELECT y FROM YvsGrhHeaderBulletin y WHERE y.telephone = :telephone"),
    @NamedQuery(name = "YvsGrhHeaderBulletin.findByEmail", query = "SELECT y FROM YvsGrhHeaderBulletin y WHERE y.email = :email"),
    @NamedQuery(name = "YvsGrhHeaderBulletin.findByCategoriePro", query = "SELECT y FROM YvsGrhHeaderBulletin y WHERE y.categoriePro = :categoriePro"),
    @NamedQuery(name = "YvsGrhHeaderBulletin.findByEchelonPro", query = "SELECT y FROM YvsGrhHeaderBulletin y WHERE y.echelonPro = :echelonPro"),
    @NamedQuery(name = "YvsGrhHeaderBulletin.findByMatriculeCnps", query = "SELECT y FROM YvsGrhHeaderBulletin y WHERE y.matriculeCnps = :matriculeCnps")})
public class YvsGrhHeaderBulletin implements Serializable {

    @Size(max = 2147483647)
    @Column(name = "code_agence")
    private String codeAgence;
    @Size(max = 2147483647)
    @Column(name = "designation_agence")
    private String designationAgence;
    @Size(max = 2147483647)
    @Column(name = "code_societe")
    private String codeSociete;
    @Size(max = 2147483647)
    @Column(name = "adresse_agence")
    private String adresseAgence;
    @Size(max = 2147483647)
    @Column(name = "adresse_societe")
    private String adresseSociete;
    @Size(max = 2147483647)
    @Column(name = "nom_employe")
    private String nomEmploye;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_header_bulletin_id_seq", name = "yvs_grh_header_bulletin_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_header_bulletin_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "matricule")
    private String matricule;
    @Size(max = 2147483647)
    @Column(name = "cni")
    private String cni;
    @Size(max = 2147483647)
    @Column(name = "poste")
    private String poste;
    @Size(max = 2147483647)
    @Column(name = "departement")
    private String departement;
    @Column(name = "date_embauche")
    @Temporal(TemporalType.DATE)
    private Date dateEmbauche;
    @Size(max = 2147483647)
    @Column(name = "telephone")
    private String telephone;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 2147483647)
    @Column(name = "email")
    private String email;
    @Size(max = 2147483647)
    @Column(name = "categorie_pro")
    private String categoriePro;
    @Size(max = 2147483647)
    @Column(name = "echelon_pro")
    private String echelonPro;
    @Size(max = 2147483647)
    @Column(name = "matricule_cnps")
    private String matriculeCnps;
    @Column(name = "civilite")
    private String civilite;
    @Column(name = "anciennete")
    private String anciennete;
    @Column(name = "adresse_employe")
    private String adresseEmploye;
    @Column(name = "mode_paiement")
    private String modePaiement;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "id_service", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhDepartement idService;
    @JoinColumn(name = "id_poste", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPosteDeTravail idPoste;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "bulletin", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhBulletins bulletin;

    public YvsGrhHeaderBulletin() {
    }

    public YvsGrhHeaderBulletin(Long id) {
        this.id = id;
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

    public String getAdresseEmploye() {
        return adresseEmploye;
    }

    public void setAdresseEmploye(String adresseEmploye) {
        this.adresseEmploye = adresseEmploye;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getAnciennete() {
        return anciennete;
    }

    public void setAnciennete(String anciennete) {
        this.anciennete = anciennete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getCni() {
        return cni;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public Date getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(Date dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategoriePro() {
        return categoriePro;
    }

    public void setCategoriePro(String categoriePro) {
        this.categoriePro = categoriePro;
    }

    public String getEchelonPro() {
        return echelonPro;
    }

    public void setEchelonPro(String echelonPro) {
        this.echelonPro = echelonPro;
    }

    public String getMatriculeCnps() {
        return matriculeCnps;
    }

    public void setMatriculeCnps(String matriculeCnps) {
        this.matriculeCnps = matriculeCnps;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhBulletins getBulletin() {
        return bulletin;
    }

    public void setBulletin(YvsGrhBulletins bulletin) {
        this.bulletin = bulletin;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsGrhPosteDeTravail getIdPoste() {
        return idPoste;
    }

    public void setIdPoste(YvsGrhPosteDeTravail idPoste) {
        this.idPoste = idPoste;
    }

    public YvsGrhDepartement getIdService() {
        return idService;
    }

    public void setIdService(YvsGrhDepartement idService) {
        this.idService = idService;
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
        if (!(object instanceof YvsGrhHeaderBulletin)) {
            return false;
        }
        YvsGrhHeaderBulletin other = (YvsGrhHeaderBulletin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhHeaderBulletin[ id=" + id + " ]";
    }

    public String getCodeAgence() {
        return codeAgence;
    }

    public void setCodeAgence(String codeAgence) {
        this.codeAgence = codeAgence;
    }

    public String getDesignationAgence() {
        return designationAgence;
    }

    public void setDesignationAgence(String designationAgence) {
        this.designationAgence = designationAgence;
    }

    public String getCodeSociete() {
        return codeSociete;
    }

    public void setCodeSociete(String codeSociete) {
        this.codeSociete = codeSociete;
    }

    public String getAdresseAgence() {
        return adresseAgence;
    }

    public void setAdresseAgence(String adresseAgence) {
        this.adresseAgence = adresseAgence;
    }

    public String getAdresseSociete() {
        return adresseSociete;
    }

    public void setAdresseSociete(String adresseSociete) {
        this.adresseSociete = adresseSociete;
    }

    public String getNomEmploye() {
        return nomEmploye;
    }

    public void setNomEmploye(String nomEmploye) {
        this.nomEmploye = nomEmploye;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

}

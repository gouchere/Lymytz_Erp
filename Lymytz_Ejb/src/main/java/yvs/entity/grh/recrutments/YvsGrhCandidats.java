/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.recrutments;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
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
import javax.validation.constraints.Size;
import yvs.entity.grh.param.poste.YvsGrhPosteDeTravail;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_candidats")
@NamedQueries({
    @NamedQuery(name = "YvsGrhCandidats.findAll", query = "SELECT y FROM YvsGrhCandidats y WHERE y.author.agence.societe=:societe AND y.actif=true"),
    @NamedQuery(name = "YvsGrhCandidats.findById", query = "SELECT y FROM YvsGrhCandidats y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhCandidats.findByCivilite", query = "SELECT y FROM YvsGrhCandidats y WHERE y.civilite = :civilite"),
    @NamedQuery(name = "YvsGrhCandidats.findByName", query = "SELECT y FROM YvsGrhCandidats y WHERE y.nameCandidat = :name"),
    @NamedQuery(name = "YvsGrhCandidats.findByPrenom", query = "SELECT y FROM YvsGrhCandidats y WHERE y.prenom = :prenom"),
    @NamedQuery(name = "YvsGrhCandidats.findByBoitePostal", query = "SELECT y FROM YvsGrhCandidats y WHERE y.boitePostal = :boitePostal"),
    @NamedQuery(name = "YvsGrhCandidats.findByDateNaissance", query = "SELECT y FROM YvsGrhCandidats y WHERE y.dateNaissance = :dateNaissance"),
    @NamedQuery(name = "YvsGrhCandidats.findByLieuNaissance", query = "SELECT y FROM YvsGrhCandidats y WHERE y.lieuNaissance = :lieuNaissance"),
    @NamedQuery(name = "YvsGrhCandidats.findByVilleHabitation", query = "SELECT y FROM YvsGrhCandidats y WHERE y.villeHabitation = :villeHabitation"),
    @NamedQuery(name = "YvsGrhCandidats.findByPaysOrigine", query = "SELECT y FROM YvsGrhCandidats y WHERE y.paysOrigine = :paysOrigine"),
    @NamedQuery(name = "YvsGrhCandidats.findByTelephones", query = "SELECT y FROM YvsGrhCandidats y WHERE y.telephones = :telephones"),
    @NamedQuery(name = "YvsGrhCandidats.findByAdresseMail", query = "SELECT y FROM YvsGrhCandidats y WHERE y.adresseMail = :adresseMail"),
    @NamedQuery(name = "YvsGrhCandidats.findByAPropos", query = "SELECT y FROM YvsGrhCandidats y WHERE y.aPropos = :aPropos")})
public class YvsGrhCandidats implements Serializable {

    @OneToMany(mappedBy = "candidat")
    private List<YvsGrhEntretienCandidat> yvsGrhEtretienCandidatList;
    @OneToMany(mappedBy = "candidat")
    private List<YvsGrhQualificationCandidat> yvsGrhQualificationCandidatList;
    @OneToMany(mappedBy = "candidat")
    private List<YvsGrhDiplomesCandidat> yvsGrhDiplomesCandidatList;

    @OneToMany(mappedBy = "candidat")
    private List<YvsGrhLanguesCandidats> langues;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_candidats_id_seq", name = "yvs_grh_candidats_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_candidats_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "code")
    private String code;
    @Column(name = "anciennete")
    private Integer anciennete; //en mois
    @Size(max = 2147483647)
    @Column(name = "civilite")
    private String civilite;
    @Size(max = 2147483647)
    @Column(name = "name_candidat")
    private String nameCandidat;
    @Size(max = 2147483647)
    @Column(name = "prenom")
    private String prenom;
    @Size(max = 2147483647)
    @Column(name = "boite_postal")
    private String boitePostal;
    @Column(name = "date_naissance")
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    @JoinColumn(name = "lieu_naissance", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire lieuNaissance;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ville_habitation", referencedColumnName = "id")
    private YvsDictionnaire villeHabitation;
    @JoinColumn(name = "pays_origine", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire paysOrigine;
    @Size(max = 2147483647)
    @Column(name = "telephones")
    private String telephones;
    @Size(max = 2147483647)
    @Column(name = "adresse_mail")
    private String adresseMail;
    @Size(max = 2147483647)
    @Column(name = "a_propos")
    private String aPropos;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "poste_travail", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPosteDeTravail posteTravail;
    @Column(name = "actif")
    private Boolean actif;

    public YvsGrhCandidats() {
    }

    public YvsGrhCandidats(Long id) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getNameCandidat() {
        return nameCandidat;
    }

    public void setNameCandidat(String nameCandidat) {
        this.nameCandidat = nameCandidat;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getBoitePostal() {
        return boitePostal;
    }

    public void setBoitePostal(String boitePostal) {
        this.boitePostal = boitePostal;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public YvsDictionnaire getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(YvsDictionnaire lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public YvsDictionnaire getVilleHabitation() {
        return villeHabitation;
    }

    public void setVilleHabitation(YvsDictionnaire villeHabitation) {
        this.villeHabitation = villeHabitation;
    }

    public YvsDictionnaire getPaysOrigine() {
        return paysOrigine;
    }

    public void setPaysOrigine(YvsDictionnaire paysOrigine) {
        this.paysOrigine = paysOrigine;
    }

    public String getaPropos() {
        return aPropos;
    }

    public void setaPropos(String aPropos) {
        this.aPropos = aPropos;
    }

    public String getTelephones() {
        return telephones;
    }

    public void setTelephones(String telephones) {
        this.telephones = telephones;
    }

    public String getAdresseMail() {
        return adresseMail;
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public String getAPropos() {
        return aPropos;
    }

    public void setAPropos(String aPropos) {
        this.aPropos = aPropos;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhPosteDeTravail getPosteTravail() {
        return posteTravail;
    }

    public void setPosteTravail(YvsGrhPosteDeTravail posteTravail) {
        this.posteTravail = posteTravail;
    }

    public Boolean getActif() {
        return (actif != null) ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getAnciennete() {
        return (anciennete != null) ? anciennete : 0;
    }

    public void setAnciennete(Integer anciennete) {
        this.anciennete = anciennete;
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
        if (!(object instanceof YvsGrhCandidats)) {
            return false;
        }
        YvsGrhCandidats other = (YvsGrhCandidats) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.recrutments.YvsGrhCandidats[ id=" + id + " ]";
    }

    public List<YvsGrhLanguesCandidats> getLangues() {
        return langues;
    }

    public void setLangues(List<YvsGrhLanguesCandidats> langues) {
        this.langues = langues;
    }

    public List<YvsGrhQualificationCandidat> getYvsGrhQualificationCandidatList() {
        return yvsGrhQualificationCandidatList;
    }

    public void setYvsGrhQualificationCandidatList(List<YvsGrhQualificationCandidat> yvsGrhQualificationCandidatList) {
        this.yvsGrhQualificationCandidatList = yvsGrhQualificationCandidatList;
    }

    public List<YvsGrhDiplomesCandidat> getYvsGrhDiplomesCandidatList() {
        return yvsGrhDiplomesCandidatList;
    }

    public void setYvsGrhDiplomesCandidatList(List<YvsGrhDiplomesCandidat> yvsGrhDiplomesCandidatList) {
        this.yvsGrhDiplomesCandidatList = yvsGrhDiplomesCandidatList;
    }

    public List<YvsGrhEntretienCandidat> getYvsGrhEtretienCandidatList() {
        return yvsGrhEtretienCandidatList;
    }

    public void setYvsGrhEtretienCandidatList(List<YvsGrhEntretienCandidat> yvsGrhEtretienCandidatList) {
        this.yvsGrhEtretienCandidatList = yvsGrhEtretienCandidatList;
    }

}

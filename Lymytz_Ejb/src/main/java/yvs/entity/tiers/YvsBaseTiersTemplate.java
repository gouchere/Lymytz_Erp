/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.tiers;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.commission.YvsComPlanCommission;
import yvs.entity.commercial.rrr.YvsComPlanRistourne;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_tiers_template")
@NamedQueries({
    @NamedQuery(name = "YvsBaseTiersTemplate.findAll", query = "SELECT y FROM YvsBaseTiersTemplate y WHERE y.agence.societe = :societe"),
    @NamedQuery(name = "YvsBaseTiersTemplate.findByType2", query = "SELECT y FROM YvsBaseTiersTemplate y WHERE y.agence.societe = :societe AND (y.type = :type1 OR y.type = :type2)"),
    @NamedQuery(name = "YvsBaseTiersTemplate.findAllCount", query = "SELECT COUNT(y) FROM YvsBaseTiersTemplate y WHERE y.agence.societe = :societe"),
    @NamedQuery(name = "YvsBaseTiersTemplate.findByAgence", query = "SELECT y FROM YvsBaseTiersTemplate y WHERE y.agence = :agence"),
    @NamedQuery(name = "YvsBaseTiersTemplate.findById", query = "SELECT y FROM YvsBaseTiersTemplate y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseTiersTemplate.findByAlwaysVisible", query = "SELECT y FROM YvsBaseTiersTemplate y WHERE y.alwaysVisible = :alwaysVisible"),
    @NamedQuery(name = "YvsBaseTiersTemplate.findByPays", query = "SELECT y FROM YvsBaseTiersTemplate y WHERE y.agence.societe = :societe AND y.pays = :pays"),
    @NamedQuery(name = "YvsBaseTiersTemplate.findByPaysOthersNull", query = "SELECT y FROM YvsBaseTiersTemplate y WHERE y.agence.societe = :societe AND y.pays = :pays AND y.ville IS NULL AND y.secteur IS NULL AND y.type = :type"),
    @NamedQuery(name = "YvsBaseTiersTemplate.findByVille", query = "SELECT y FROM YvsBaseTiersTemplate y WHERE y.agence.societe = :societe AND y.ville = :ville AND y.type = :type"),
    @NamedQuery(name = "YvsBaseTiersTemplate.findBySecteur", query = "SELECT y FROM YvsBaseTiersTemplate y WHERE y.agence.societe = :societe AND y.secteur = :secteur AND y.type = :type"),
    @NamedQuery(name = "YvsBaseTiersTemplate.findByPaysVille", query = "SELECT y FROM YvsBaseTiersTemplate y WHERE y.agence.societe = :societe AND y.ville = :ville AND y.pays = :pays"),
    @NamedQuery(name = "YvsBaseTiersTemplate.findByPaysVilleOthersNull", query = "SELECT y FROM YvsBaseTiersTemplate y WHERE y.agence.societe = :societe AND y.ville = :ville AND y.pays = :pays AND y.secteur IS NULL AND y.type = :type"),
    @NamedQuery(name = "YvsBaseTiersTemplate.findByPaysSecteur", query = "SELECT y FROM YvsBaseTiersTemplate y WHERE y.agence.societe = :societe AND y.secteur = :secteur AND y.pays = :pays AND y.type = :type"),
    @NamedQuery(name = "YvsBaseTiersTemplate.findByVilleSecteur", query = "SELECT y FROM YvsBaseTiersTemplate y WHERE y.agence.societe = :societe AND y.secteur = :secteur AND y.ville = :ville AND y.type = :type"),
    @NamedQuery(name = "YvsBaseTiersTemplate.findByPaysVilleSecteur", query = "SELECT y FROM YvsBaseTiersTemplate y WHERE y.agence.societe = :societe AND y.secteur = :secteur AND y.ville = :ville AND y.pays = :pays AND y.type = :type")})
public class YvsBaseTiersTemplate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_tiers_template_id_seq", name = "yvs_base_tiers_template_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_tiers_template_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "add_secteur")
    private Boolean addSecteur;
    @Column(name = "taille_secteur")
    private Integer tailleSecteur;
    @Column(name = "add_nom")
    private Boolean addNom;
    @Column(name = "taille_nom")
    private Integer tailleNom;
    @Column(name = "add_prenom")
    private Boolean addPrenom;
    @Column(name = "add_radical")
    private Boolean addRadical;
    @Column(name = "radical")
    private String radical;
    @Column(name = "taille_prenom")
    private Integer taillePrenom;
    @Column(name = "add_separateur")
    private Boolean addSeparateur;
    @Size(max = 255)
    @Column(name = "separateur")
    private String separateur;
    @Column(name = "type")
    private char type = 'M';
    @Column(name = "always_visible")
    private Boolean alwaysVisible;
    
    @JoinColumn(name = "compte_collectif", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compteCollectif;
    @JoinColumn(name = "secteur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire secteur;
    @JoinColumn(name = "ville", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire ville;
    @JoinColumn(name = "pays", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire pays;
    @JoinColumn(name = "ristourne", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComPlanRistourne ristourne;
    @JoinColumn(name = "comission", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComPlanCommission comission;
    @JoinColumn(name = "categorie_comptable", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCategorieComptable categorieComptable;
    @JoinColumn(name = "mdr", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement mdr;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    
    @Transient
    private boolean select;
    @Transient
    private boolean choose;
    @Transient
    private boolean new_;
    @Transient
    private String apercu;
    @Transient
    private String nameType;
    @Transient
    private String nom = "FOTSO";
    @Transient
    private String prenom = "ALAIN";

    public YvsBaseTiersTemplate() {
    }

    public YvsBaseTiersTemplate(Long id) {
        this.id = id;
    }

    public YvsBaseTiersTemplate(Long id, String nom, String prenom) {
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getApercu() {
        apercu = "";
        if (getAddSeparateur()) {
            if (getAddSecteur()) {
                apercu = getSecteur_().getAbreviation().length() > getTailleSecteur() ? getSecteur_().getAbreviation().substring(0, getTailleSecteur()) : getSecteur_().getAbreviation();
                if (getAddNom()) {
                    apercu += getSeparateur() + ((nom.length() > getTailleNom()) ? nom.substring(0, getTailleNom()) : nom);
                    if (getAddPrenom()) {
                        apercu += getSeparateur() + ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                } else {
                    if (getAddPrenom()) {
                        apercu += getSeparateur() + ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                }
            } else {
                if (getAddNom()) {
                    apercu += getSeparateur() + ((nom.length() > getTailleNom()) ? nom.substring(0, getTailleNom()) : nom);
                    if (getAddPrenom()) {
                        apercu += getSeparateur() + ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                } else {
                    if (getAddPrenom()) {
                        apercu += getSeparateur() + ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                }
            }
        } else {
            if (getAddSecteur()) {
                apercu = getSecteur_().getAbreviation().length() > getTailleSecteur() ? getSecteur_().getAbreviation().substring(0, getTailleSecteur()) : getSecteur_().getAbreviation();
                if (getAddNom()) {
                    apercu += ((nom.length() > getTailleNom()) ? nom.substring(0, getTailleNom()) : nom);
                    if (getAddPrenom()) {
                        apercu += ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                } else {
                    if (getAddPrenom()) {
                        apercu += ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                }
            } else {
                if (getAddNom()) {
                    apercu += ((nom.length() > getTailleNom()) ? nom.substring(0, getTailleNom()) : nom);
                    if (getAddPrenom()) {
                        apercu += ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                } else {
                    if (getAddPrenom()) {
                        apercu += ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                }
            }
        }
        return apercu;
    }

    public void setApercu(String apercu) {
        this.apercu = apercu;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public YvsDictionnaire getVille() {
        return ville;
    }

    public YvsDictionnaire getVille_() {
        return ville != null ? ville.getId() > 0 ? ville : getPays_() : getPays_();
    }

    public void setVille(YvsDictionnaire ville) {
        this.ville = ville;
    }

    public YvsDictionnaire getPays() {
        return pays;
    }

    public YvsDictionnaire getPays_() {
        return pays != null ? pays : new YvsDictionnaire();
    }

    public void setPays(YvsDictionnaire pays) {
        this.pays = pays;
    }

    public boolean isChoose() {
        choose = getApercu().trim().length() > 0;
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAddSecteur() {
        return addSecteur != null ? addSecteur : false;
    }

    public void setAddSecteur(Boolean addSecteur) {
        this.addSecteur = addSecteur;
    }

    public Integer getTailleSecteur() {
        return tailleSecteur != null ? tailleSecteur : 0;
    }

    public void setTailleSecteur(Integer tailleSecteur) {
        this.tailleSecteur = tailleSecteur;
    }

    public Boolean getAddNom() {
        return addNom != null ? addNom : false;
    }

    public void setAddNom(Boolean addNom) {
        this.addNom = addNom;
    }

    public Integer getTailleNom() {
        return tailleNom != null ? tailleNom : 0;
    }

    public void setTailleNom(Integer tailleNom) {
        this.tailleNom = tailleNom;
    }

    public Boolean getAddPrenom() {
        return addPrenom != null ? addPrenom : false;
    }

    public void setAddPrenom(Boolean addPrenom) {
        this.addPrenom = addPrenom;
    }

    public Integer getTaillePrenom() {
        return taillePrenom != null ? taillePrenom : 0;
    }

    public void setTaillePrenom(Integer taillePrenom) {
        this.taillePrenom = taillePrenom;
    }

    public Boolean getAddSeparateur() {
        return addSeparateur != null ? addSeparateur : false;
    }

    public void setAddSeparateur(Boolean addSeparateur) {
        this.addSeparateur = addSeparateur;
    }

    public String getSeparateur() {
        return separateur != null ? ((separateur.trim().length() > 0) ? separateur : "-") : "-";
    }

    public void setSeparateur(String separateur) {
        this.separateur = separateur;
    }

    public Boolean getAlwaysVisible() {
        return alwaysVisible != null ? alwaysVisible : false;
    }

    public void setAlwaysVisible(Boolean alwaysVisible) {
        this.alwaysVisible = alwaysVisible;
    }

    public YvsBasePlanComptable getCompteCollectif() {
        return compteCollectif;
    }

    public void setCompteCollectif(YvsBasePlanComptable compteCollectif) {
        this.compteCollectif = compteCollectif;
    }

    public YvsDictionnaire getSecteur() {
        return secteur;
    }

    public YvsDictionnaire getSecteur_() {
        return secteur != null ? secteur.getId() > 0 ? secteur : getVille_() : getVille_();
    }

    public void setSecteur(YvsDictionnaire secteur) {
        this.secteur = secteur;
    }

    public YvsBaseCategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(YvsBaseCategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    public YvsBaseModeReglement getMdr() {
        return mdr;
    }

    public void setMdr(YvsBaseModeReglement mdr) {
        this.mdr = mdr;
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

    public YvsComPlanRistourne getRistourne() {
        return ristourne;
    }

    public void setRistourne(YvsComPlanRistourne ristourne) {
        this.ristourne = ristourne;
    }

    public YvsComPlanCommission getComission() {
        return comission;
    }

    public void setComission(YvsComPlanCommission comission) {
        this.comission = comission;
    }

    public String getRadical() {
        return radical;
    }

    public void setAddRadical(Boolean addRadical) {
        this.addRadical = addRadical;
    }

    public Boolean getAddRadical() {
        return addRadical != null ? addRadical : false;
    }

    public void setRadical(String radical) {
        this.radical = radical;
    }

    public String getLibelle() {
        return libelle != null ? libelle : "";
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getNameType() {
        nameType = (type == 'C' ? "CLIENT" : (type == 'F' ? "FOURNISSEUR" : "MIXTE"));
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
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
        if (!(object instanceof YvsBaseTiersTemplate)) {
            return false;
        }
        YvsBaseTiersTemplate other = (YvsBaseTiersTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.tiers.YvsBaseTiersTemplate[ id=" + id + " ]";
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.tiers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.Comptes;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.tiers.YvsBaseTiersDocument;
import yvs.entity.tiers.YvsBaseTiersTemplate;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.dico.Dictionnaire;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Tiers implements Serializable, Comparator<Tiers> {

    private long id;
    private String statut = "Société";
    private String codeTiers;
    private String nature; //client, fournisseurs, représentant, personnel
    private String civilite = "M.";
    private String photos = "user1.jpg";
    private String nom;
    private String nom_prenom;
    private String prenom;
    private String codeClient;
    private String codeFournisseur;
    private String codeRepresentant;
    private String adresse;
    private String codePersonel;
    private String email;
    private String telephone;
    private String responsable;
    private String codeBarre;
    private String codePostal;
    private String logo = "user1.jpg";
    private String type;
    private String codeRation;
    private String localisation;
    private String bp;
    private String site;
    private String utilisateurRatache;
    private Dictionnaire ville = new Dictionnaire();
    private Dictionnaire pays = new Dictionnaire();
    private Dictionnaire secteur = new Dictionnaire();
    private Agence agence = new Agence();
    private long compteCollectif;
    private Comptes compte = new Comptes();
    private long idAgence;
    private long categorieTarifaire;
    private CategorieComptable categorieComptable = new CategorieComptable();
    private long planRistourne;
    private long planComission;
    private int modelDeRglt;
    private boolean client;
    private boolean fournisseur, fabricant;
    private boolean employe;
    private boolean representant;
    private boolean societe;
    private boolean personnel;
    private Contact contact = new Contact();
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private List<Contact> contacts;
    private boolean selectActif, new_, update, error;
    private boolean actif = true;
    private YvsBaseTiersTemplate template = new YvsBaseTiersTemplate();
    private List<Profil> profils;
    private Profil selectProfil = new Profil();
    private List<YvsComClient> clients;

    public Tiers() {
        contacts = new ArrayList<>();
        profils = new ArrayList<>();
        clients = new ArrayList<>();
    }

    public Tiers(long id) {
        this();
        this.id = id;
    }

    public Tiers(long id, String nom, String prenom) {
        this(id);
        this.nom = nom;
        this.prenom = prenom;
    }

    public Tiers(long id, String code, String nom, String prenom) {
        this(id, nom, prenom);
        this.codeTiers = code;
    }

    public Tiers(long id, String nom_prenom) {
        this(id);
        this.nom_prenom = nom_prenom;
    }

    public String getCodeRation() {
        return codeRation != null ? codeRation.trim() : "";
    }

    public void setCodeRation(String codeRation) {
        this.codeRation = codeRation;
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

    public boolean isPersonnel() {
        return personnel;
    }

    public void setPersonnel(boolean personnel) {
        this.personnel = personnel;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public Comptes getCompte() {
        return compte;
    }

    public void setCompte(Comptes compte) {
        this.compte = compte;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isActif() {
        return actif;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public Dictionnaire getSecteur() {
        return secteur;
    }

    public void setSecteur(Dictionnaire secteur) {
        this.secteur = secteur;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public String getSite() {
        site = societe ? site : "";
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNom_prenom() {
        nom_prenom = "";
        if (!(getNom() == null || getNom().trim().equals(""))) {
            nom_prenom = getNom();
        }
        if (!(getPrenom() == null || getPrenom().trim().equals(""))) {
            if (nom_prenom == null || nom_prenom.trim().equals("")) {
                nom_prenom = getPrenom();
            } else {
                nom_prenom += " " + getPrenom();
            }
        }
        if (isSociete()) {
            if (!(getResponsable() == null || getResponsable().trim().equals(""))) {
                if (nom_prenom == null || nom_prenom.trim().equals("")) {
                    nom_prenom = getResponsable();
                } else {
                    nom_prenom += " / " + getResponsable();
                }
            }
        }
        return nom_prenom;
    }

    public void setNom_prenom(String nom_prenom) {
        this.nom_prenom = nom_prenom;
    }

    public String getPrenom() {
        prenom = societe ? "" : (prenom != null ? prenom : "");
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public boolean isFabricant() {
        return fabricant;
    }

    public void setFabricant(boolean fabricant) {
        this.fabricant = fabricant;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public CategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(CategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    public long getCategorieTarifaire() {
        return categorieTarifaire;
    }

    public void setCategorieTarifaire(long categorieTarifaire) {
        this.categorieTarifaire = categorieTarifaire;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public String getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(String codeClient) {
        this.codeClient = codeClient;
    }

    public String getCodeFournisseur() {
        return codeFournisseur;
    }

    public void setCodeFournisseur(String codeFournisseur) {
        this.codeFournisseur = codeFournisseur;
    }

    public String getCodePersonel() {
        return codePersonel;
    }

    public void setCodePersonel(String codePersonel) {
        this.codePersonel = codePersonel;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getCodeRepresentant() {
        return codeRepresentant;
    }

    public void setCodeRepresentant(String codeRepresentant) {
        this.codeRepresentant = codeRepresentant;
    }

    public long getCompteCollectif() {
        return compteCollectif;
    }

    public void setCompteCollectif(long compteCollectif) {
        this.compteCollectif = compteCollectif;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Dictionnaire getPays() {
        return pays;
    }

    public void setPays(Dictionnaire pays) {
        this.pays = pays;
    }

    public Dictionnaire getVille() {
        return ville;
    }

    public void setVille(Dictionnaire ville) {
        this.ville = ville;
    }

    public long getPlanComission() {
        return planComission;
    }

    public void setPlanComission(long planComission) {
        this.planComission = planComission;
    }

    public long getPlanRistourne() {
        return planRistourne;
    }

    public void setPlanRistourne(long planRistourne) {
        this.planRistourne = planRistourne;
    }

    public String getStatut() {
        return statut;
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

    public String getUtilisateurRatache() {
        return utilisateurRatache;
    }

    public void setUtilisateurRatache(String utilisateurRatache) {
        this.utilisateurRatache = utilisateurRatache;
    }

    public boolean isClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }

    public boolean isEmploye() {
        return employe;
    }

    public void setEmploye(boolean employe) {
        this.employe = employe;
    }

    public boolean isFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(boolean fournisseur) {
        this.fournisseur = fournisseur;
    }

    public boolean isRepresentant() {
        return representant;
    }

    public void setRepresentant(boolean representant) {
        this.representant = representant;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isSociete() {
        return societe;
    }

    public void setSociete(boolean societe) {
        this.societe = societe;
    }

    public int getModelDeRglt() {
        return modelDeRglt;
    }

    public void setModelDeRglt(int modelDeRglt) {
        this.modelDeRglt = modelDeRglt;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public long getIdAgence() {
        return idAgence;
    }

    public void setIdAgence(long idAgence) {
        this.idAgence = idAgence;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public Dictionnaire getPays_() {
        return pays != null ? pays : new Dictionnaire();
    }

    public Dictionnaire getVille_() {
        return ville != null ? ville.getId() > 0 ? ville : getPays_() : getPays_();
    }

    public Dictionnaire getSecteur_() {
        return secteur != null ? secteur.getId() > 0 ? secteur : getVille_() : getVille_();
    }

    public YvsBaseTiersTemplate getTemplate() {
        return template;
    }

    public void setTemplate(YvsBaseTiersTemplate template) {
        this.template = template;
    }

    public String getCodeTiers() {
        return codeTiers != null ? codeTiers.toUpperCase() : "";
    }

    public void setCodeTiers(String codeTiers) {
        this.codeTiers = codeTiers;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Profil> getProfils() {
        return profils;
    }

    public void setProfils(List<Profil> profils) {
        this.profils = profils;
    }

    public List<YvsComClient> getClients() {
        return clients;
    }

    public void setClients(List<YvsComClient> clients) {
        this.clients = clients;
    }

    public Profil getSelectProfil() {
        return selectProfil;
    }

    public void setSelectProfil(Profil selectProfil) {
        this.selectProfil = selectProfil;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tiers other = (Tiers) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
//compare les tiers par nature

    @Override
    public int compare(Tiers o1, Tiers o2) {
        if (o1.getNature().compareTo(o2.getNature()) < 0) {
            return -1;
        } else if (o1.getNature().compareTo(o2.getNature()) > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}

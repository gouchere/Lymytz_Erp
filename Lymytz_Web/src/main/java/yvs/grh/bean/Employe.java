/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import yvs.grh.bean.mission.Mission;
import yvs.parametrage.poste.PosteDeTravail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.grh.param.YvsGrhDomainesQualifications;
import yvs.grh.Affectation;
import yvs.grh.assurance.Assurance;
import yvs.base.compta.Comptes;
import yvs.base.tiers.Tiers;
import yvs.entity.compta.YvsComptaAffecAnalEmp;
import yvs.entity.grh.activite.YvsContactsEmps;
import yvs.entity.grh.param.poste.YvsGrhPosteEmployes;
import yvs.entity.grh.personnel.YvsCompteBancaire;
import yvs.entity.grh.personnel.YvsDiplomeEmploye;
import yvs.entity.grh.personnel.YvsGrhConventionEmploye;
import yvs.entity.grh.personnel.YvsGrhDocumentEmps;
import yvs.entity.grh.personnel.YvsGrhPersonneEnCharge;
import yvs.entity.grh.personnel.YvsLangueEmps;
import yvs.entity.grh.personnel.YvsPermisDeCoduire;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.dico.Dictionnaire;
import yvs.users.Users;
import yvs.util.Constantes;
import yvs.util.Util;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Employe implements Serializable {

    private long id;
    private String codeEmploye;
    private Users user = new Users();
    private String civilite = "M.";
    private String nom;
    private String prenom;
    private String nom_prenom;
    private String nomDeJeuneFille;
    private String matricule;
    private String codePostal;
    private String centreAnal;
    private String matriculeCnps;
    private boolean actif = true;
    private String photos = Constantes.DEFAULT_PHOTO_EMPLOYE_MAN();
    private String photoExtension = "png";
    private Profils profil = new Profils();
    private StatutEmps statutEmps = new StatutEmps();
    private Date dateEmbauche = new Date();
    private String motifDentree = "Embauche";
    private Date dateArret = new Date(), dateAvancement;
    private String motifArret = "Embauche";
    private Agence agence = new Agence();
//    private Departements departement = new Departements(); //département de service
    private String commentaire;
    private Date dateNaissance = new Date();
    private Dictionnaire villeNaissance = new Dictionnaire();
    private Dictionnaire paysDorigine = new Dictionnaire();
    private String situationFamilial = "Marié";
//    private int nbreEnfant;
//    private Contrats contrat = new Contrats();
    private String cni;
    private Date dateDelivCni = new Date();
    private Date dateExpCni = new Date();
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private Dictionnaire lieuDelivCni = new Dictionnaire();
//    private Comptes compteAuxiliaire = new Comptes();
    private String mail1, mail2, telephone1, telephone2, adresse1, adresse2;
    private boolean horaireDynamique;
    private PosteDeTravail posteDeTravail = new PosteDeTravail();
//    private List<Qualification> qualifications;
    private List<YvsGrhDomainesQualifications> qualifications;
    private Convention convention = new Convention();
    private List<YvsDiplomeEmploye> diplomes;
    private List<YvsPermisDeCoduire> permis;
    private List<YvsLangueEmps> langues;
    private List<YvsGrhPersonneEnCharge> personneEnCharge;
    private List<YvsGrhDocumentEmps> documents;
    private List<YvsCompteBancaire> comptesBancaires;
    private List<YvsContactsEmps> contacts;
    private List<Assurance> assurances;
//    private List<PointageEmploye> fichePresence;
    private boolean selectActif, viewList;
//    private double jourConge;
    private double solde;
    private int nombreEnfant;

    private ContratEmploye contrat;
    private List<Affectation> affectations;
    private List<YvsGrhPosteEmployes> postesTravail;
    private EquipeEmploye equipe = new EquipeEmploye();

    private Comptes compteCollectif = new Comptes();
    private Tiers compteTiers = new Tiers();

    private Conges conge;   //permet de préciser juste si un employé est en congé à un moment donnée
    private Mission mission;   //permet de préciser juste si un employé est en congé à un moment donnée
    private double dureeAnciennete, dureePreavis;
    private String anciennete;
    private List<YvsGrhConventionEmploye> conventions;

    private List<YvsComptaAffecAnalEmp> sectionsAnalytiques;

    private boolean error;

    public Employe() {
        documents = new ArrayList<>();
        personneEnCharge = new ArrayList<>();
        qualifications = new ArrayList<>();
        langues = new ArrayList<>();
//        fichePresence = new ArrayList<>();
        permis = new ArrayList<>();
        assurances = new ArrayList<>();
        diplomes = new ArrayList<>();
        contacts = new ArrayList<>();
        conventions = new ArrayList<>();
        affectations = new ArrayList<>();
        comptesBancaires = new ArrayList<>();
        postesTravail = new ArrayList<>();
        sectionsAnalytiques = new ArrayList<>();
        //initialisation de la date de naissance
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1990);
        dateNaissance = cal.getTime();
    }

    public Employe(long id) {
        this();
        this.id = id;
    }

    public Employe(String matricule) {
        this(0);
        this.matricule = matricule;
    }

    public Employe(long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Employe(long id, String matricule, String nom, String prenom) {
        this(id);
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
    }

    public Employe(long id, Tiers tiers) {
        this(id);
        this.matricule = tiers.getCodeTiers();
        this.nom = tiers.getNom();
        this.prenom = tiers.getPrenom();
        this.selectActif = tiers.isSelectActif();
        this.error = tiers.isError();
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

    public List<YvsGrhPosteEmployes> getPostesTravail() {
        return postesTravail;
    }

    public void setPostesTravail(List<YvsGrhPosteEmployes> postesTravail) {
        this.postesTravail = postesTravail;
    }

    public List<YvsGrhConventionEmploye> getConventions() {
        return conventions;
    }

    public void setConventions(List<YvsGrhConventionEmploye> conventions) {
        this.conventions = conventions;
    }

    public String getNom_prenom() {
        nom_prenom = "";
        if (nom != null ? !nom.equals("") : false) {
            nom_prenom = nom;
        }
        if (prenom != null ? !prenom.equals("") : false) {
            nom_prenom += " " + prenom;
        }
        return nom_prenom;
    }

    public String getAnciennete() {
        anciennete = Util.calculNbyear(getDateEmbauche(), new Date());
        return anciennete;
    }

    public void setAnciennete(String anciennete) {
        this.anciennete = anciennete;
    }

    public double getDureeAnciennete() {
        dureeAnciennete = Util.calculNbMonth(getDateEmbauche(), new Date());
        return dureeAnciennete;
    }

    public void setDureeAnciennete(double dureeAnciennete) {
        this.dureeAnciennete = dureeAnciennete;
    }

    public double getDureePreavis() {
        return dureePreavis;
    }

    public void setDureePreavis(double dureePreavis) {
        this.dureePreavis = dureePreavis;
    }

    public boolean isViewList() {
        return viewList;
    }

    public void setViewList(boolean viewList) {
        this.viewList = viewList;
    }

    public void setNom_prenom(String nom_prenom) {
        this.nom_prenom = nom_prenom;
    }

    public EquipeEmploye getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeEmploye equipe) {
        this.equipe = equipe;
    }

    public List<Affectation> getAffectations() {
        return affectations;
    }

    public void setAffectations(List<Affectation> affectations) {
        this.affectations = affectations;
    }

    public List<YvsCompteBancaire> getComptesBancaires() {
        return comptesBancaires;
    }

    public void setComptesBancaires(List<YvsCompteBancaire> comptesBancaires) {
        this.comptesBancaires = comptesBancaires;
    }

    public List<YvsContactsEmps> getContacts() {
        return contacts;
    }

    public void setContacts(List<YvsContactsEmps> contacts) {
        this.contacts = contacts;
    }

    public Date getDateAvancement() {
        return dateAvancement;
    }

    public void setDateAvancement(Date dateAvancement) {
        this.dateAvancement = dateAvancement;
    }

    public void setMatriculeCnps(String matriculeCnps) {
        this.matriculeCnps = matriculeCnps;
    }

    public String getMatriculeCnps() {
        return matriculeCnps;
    }

    public int getNombreEnfant() {
        return nombreEnfant;
    }

    public ContratEmploye getContrat() {
        return contrat;
    }

    public void setContrat(ContratEmploye contrat) {
        this.contrat = contrat;
    }

    public void setNombreEnfant(int nombreEnfant) {
        this.nombreEnfant = nombreEnfant;
    }

    public Conges getConge() {
        return conge;
    }

    public void setConge(Conges conge) {
        this.conge = conge;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public String getCivilite() {
        return civilite != null ? civilite.trim().length() > 0 ? civilite : "M." : "M.";
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getCodeEmploye() {
        return codeEmploye;
    }

    public void setCodeEmploye(String codeEmploye) {
        this.codeEmploye = codeEmploye;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Date getDateArret() {
        return dateArret;
    }

    public void setDateArret(Date dateArret) {
        this.dateArret = dateArret;
    }

    public Date getDateDelivCni() {
        return dateDelivCni;
    }

    public void setDateDelivCni(Date dateDelivCni) {
        this.dateDelivCni = dateDelivCni;
    }

    public Date getDateExpCni() {
        return dateExpCni;
    }

    public void setDateExpCni(Date dateExpCni) {
        this.dateExpCni = dateExpCni;
    }

    public Date getDateEmbauche() {
        return dateEmbauche != null ? dateEmbauche : new Date();
    }

    public void setDateEmbauche(Date dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getMotifDentree() {
        return motifDentree;
    }

    public void setMotifDentree(String motifDentree) {
        this.motifDentree = motifDentree;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Dictionnaire getPaysDorigine() {
        return paysDorigine;
    }

    public void setPaysDorigine(Dictionnaire paysDorigine) {
        this.paysDorigine = paysDorigine;
    }

    public Dictionnaire getVilleNaissance() {
        return villeNaissance;
    }

    public void setVilleNaissance(Dictionnaire villeNaissance) {
        this.villeNaissance = villeNaissance;
    }

    public String getPhotos() {
        String photo = getCivilite().equals("M.") ? Constantes.DEFAULT_PHOTO_EMPLOYE_MAN() : Constantes.DEFAULT_PHOTO_EMPLOYE_WOMAN();
        return photos != null ? photos.trim().length() > 0 ? photos : photo : photo;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getPhotoExtension() {
        return photoExtension != null ? photoExtension.trim().length() > 0 ? photoExtension : "png" : "png";
    }

    public void setPhotoExtension(String photoExtension) {
        this.photoExtension = photoExtension;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSituationFamilial() {
        return situationFamilial;
    }

    public void setSituationFamilial(String situationFamilial) {
        this.situationFamilial = situationFamilial;
    }

    public String getNomDeJeuneFille() {
        return nomDeJeuneFille;
    }

    public void setNomDeJeuneFille(String nomDeJeuneFille) {
        this.nomDeJeuneFille = nomDeJeuneFille;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users codeUser) {
        this.user = codeUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getCentreAnal() {
        return centreAnal;
    }

    public void setCentreAnal(String centreAnal) {
        this.centreAnal = centreAnal;
    }

    public String getMotifArret() {
        return motifArret;
    }

    public void setMotifArret(String motifArret) {
        this.motifArret = motifArret;
    }

    public String getCni() {
        return cni;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    public Dictionnaire getLieuDelivCni() {
        return lieuDelivCni;
    }

    public void setLieuDelivCni(Dictionnaire lieuDelivCni) {
        this.lieuDelivCni = lieuDelivCni;
    }

    public Comptes getCompteCollectif() {
        return compteCollectif;
    }

    public void setCompteCollectif(Comptes compteCollectif) {
        this.compteCollectif = compteCollectif;
    }

    public Tiers getCompteTiers() {
        return compteTiers;
    }

    public void setCompteTiers(Tiers compteTiers) {
        this.compteTiers = compteTiers;
    }

    public PosteDeTravail getPosteDeTravail() {
        return posteDeTravail;
    }

    public void setPosteDeTravail(PosteDeTravail posteDeTravail) {
        this.posteDeTravail = posteDeTravail;
    }

    public Profils getProfil() {
        return profil;
    }

    public void setProfil(Profils profil) {
        this.profil = profil;
    }

    public List<YvsGrhDomainesQualifications> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<YvsGrhDomainesQualifications> qualifications) {
        this.qualifications = qualifications;
    }

    public List<Assurance> getAssurances() {
        return assurances;
    }

    public void setAssurances(List<Assurance> assurances) {
        this.assurances = assurances;
    }

    public Convention getConvention() {
        return convention;
    }

    public void setConvention(Convention convention) {
        this.convention = convention;
    }

    public List<YvsDiplomeEmploye> getDiplomes() {
        return diplomes;
    }

    public void setDiplomes(List<YvsDiplomeEmploye> diplomes) {
        this.diplomes = diplomes;
    }

    public List<YvsPermisDeCoduire> getPermis() {
        return permis;
    }

    public void setPermis(List<YvsPermisDeCoduire> permis) {
        this.permis = permis;
    }

    public List<YvsLangueEmps> getLangues() {
        return langues;
    }

    public void setLangues(List<YvsLangueEmps> langues) {
        this.langues = langues;
    }

    public List<YvsGrhPersonneEnCharge> getPersonneEnCharge() {
        return personneEnCharge;
    }

    public void setPersonneEnCharge(List<YvsGrhPersonneEnCharge> personneEnCharge) {
        this.personneEnCharge = personneEnCharge;
    }

    public List<YvsGrhDocumentEmps> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsGrhDocumentEmps> documents) {
        this.documents = documents;
    }

    public List<YvsComptaAffecAnalEmp> getSectionsAnalytiques() {
        return sectionsAnalytiques;
    }

    public void setSectionsAnalytiques(List<YvsComptaAffecAnalEmp> sectionsAnalytiques) {
        this.sectionsAnalytiques = sectionsAnalytiques;
    }

    public StatutEmps getStatutEmps() {
        return statutEmps;
    }

    public void setStatutEmps(StatutEmps statutEmps) {
        this.statutEmps = statutEmps;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public String getMail1() {
        return mail1;
    }

    public void setMail1(String mail1) {
        this.mail1 = mail1;
    }

    public String getMail2() {
        return mail2;
    }

    public void setMail2(String mail2) {
        this.mail2 = mail2;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public String getTelephone2() {
        return telephone2;
    }

    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    public String getAdresse1() {
        return adresse1;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public boolean isHoraireDynamique() {
        return horaireDynamique;
    }

    public void setHoraireDynamique(boolean horaireDynamique) {
        this.horaireDynamique = horaireDynamique;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isError() {
        return error;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Employe other = (Employe) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

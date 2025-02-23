/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.agence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.BeanDeBase;
import yvs.parametrage.poste.Departements;
import yvs.grh.bean.Employe;
import yvs.grh.bean.EmployePartial;
import yvs.parametrage.poste.PosteDeTravail;
import yvs.grh.bean.Taches;
import yvs.grh.presence.PointageEmploye;
import yvs.parametrage.societe.Societe;
import yvs.parametrage.dico.Dictionnaire;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "agence")
@SessionScoped
public class Agence extends BeanDeBase implements Serializable, Comparator<Agence> {

    private long id;
    private String codeAgence;
    private String Abbreviation;
    private String designation;
    private String adresse;
    private Dictionnaire pays = new Dictionnaire();
    private Dictionnaire ville = new Dictionnaire();
    private SecteurActivite secteur = new SecteurActivite();
    private String region;
    private String author;
    private long idSave;
    private String telephone;
    private String email;
    private List<Departements> listDepartement;
    private List<Employe> listEmploye;
    private List<PointageEmploye> listFichePresence;
    private List<PosteDeTravail> listPosteTravailByAgence;
    private EmployePartial responsableAgence = new EmployePartial();
    private List<Taches> listTaches;
    private Societe societe = new Societe();
    private Date dateSave = new Date();
    private double etatEndettement = 0;
    private double masseSalariale = 0;
    private boolean selectActif, actif = true;
    private long employeTemporaire;
    private long employePermament;
    private long employeTacheron;
    private long employeStagiaire;

    public Agence() {
        listTaches = new ArrayList<>();
        listEmploye = new ArrayList<>();
        listFichePresence = new ArrayList<>();
        listDepartement = new ArrayList<>();
        listPosteTravailByAgence = new ArrayList<>();
    }

    public Agence(long id) {
        this();
        this.id = id;
    }

    public Agence(String codeAgence) {
        this();
        this.codeAgence = codeAgence;
    }

    public Agence(long id, String codeAgence) {
        this(id);
        this.codeAgence = codeAgence;
    }

    public Agence(long id, String codeAgence, String designation) {
        this(id, codeAgence);
        this.designation = designation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Dictionnaire getPays() {
        return pays;
    }

    public void setPays(Dictionnaire pays) {
        this.pays = pays;
    }

    public long getEmployeTemporaire() {
        return employeTemporaire;
    }

    public void setEmployeTemporaire(long employeTemporaire) {
        this.employeTemporaire = employeTemporaire;
    }

    public long getEmployePermament() {
        return employePermament;
    }

    public void setEmployePermament(long employePermament) {
        this.employePermament = employePermament;
    }

    public long getEmployeTacheron() {
        return employeTacheron;
    }

    public void setEmployeTacheron(long employeTacheron) {
        this.employeTacheron = employeTacheron;
    }

    public long getEmployeStagiaire() {
        return employeStagiaire;
    }

    public void setEmployeStagiaire(long employeStagiaire) {
        this.employeStagiaire = employeStagiaire;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public SecteurActivite getSecteur() {
        return secteur;
    }

    public void setSecteur(SecteurActivite secteur) {
        this.secteur = secteur;
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

    public EmployePartial getResponsableAgence() {
        return responsableAgence;
    }

    public void setResponsableAgence(EmployePartial responsableAgence) {
        this.responsableAgence = responsableAgence;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public List<PosteDeTravail> getListPosteTravailByAgence() {
        return listPosteTravailByAgence;
    }

    public void setListPosteTravailByAgence(List<PosteDeTravail> listPosteTravailByAgence) {
        this.listPosteTravailByAgence = listPosteTravailByAgence;
    }

    public double getEtatEndettement() {
        return etatEndettement;
    }

    public void setEtatEndettement(double etatEndettement) {
        this.etatEndettement = etatEndettement;
    }

    public double getMasseSalariale() {
        return masseSalariale;
    }

    public void setMasseSalariale(double masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    public List<Departements> getListDepartement() {
        return listDepartement;
    }

    public void setListDepartement(List<Departements> listDepartement) {
        this.listDepartement = listDepartement;
    }

    public List<Employe> getListEmploye() {
        return listEmploye;
    }

    public void setListEmploye(List<Employe> listEmploye) {
        this.listEmploye = listEmploye;
    }

    public List<PointageEmploye> getListFichePresence() {
        return listFichePresence;
    }

    public void setListFichePresence(List<PointageEmploye> listFichePresence) {
        this.listFichePresence = listFichePresence;
    }

    public List<Taches> getListTaches() {
        return listTaches;
    }

    public void setListTaches(List<Taches> listTaches) {
        this.listTaches = listTaches;
    }

    public String getAbbreviation() {
        return Abbreviation;
    }

    public void setAbbreviation(String Abbreviation) {
        this.Abbreviation = Abbreviation;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodeAgence() {
        return codeAgence;
    }

    public void setCodeAgence(String codeAgence) {
        this.codeAgence = codeAgence;
    }

    public String getDesignation() {
        return designation != null ? designation : "";
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Dictionnaire getVille() {
        return ville;
    }

    public void setVille(Dictionnaire ville) {
        this.ville = ville;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getIdSave() {
        return idSave;
    }

    public void setIdSave(long idSave) {
        this.idSave = idSave;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public boolean isActif() {
        return super.isActif(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setActif(boolean actif) {
        super.setActif(actif); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Agence other = (Agence) obj;
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
    //ordonne par défaut en fonction de la ville   

    @Override
    public int compare(Agence o1, Agence o2) {
//        if (o1.getVille().getTitre().compareTo(o2.getVille().getTitre()) > 0) {
//            return 1;
//        } else if (o1.getVille().getTitre().compareTo(o2.getVille().getTitre()) < 0) {
//            return -1;
//        } else {
//            return 0;
//        }  
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

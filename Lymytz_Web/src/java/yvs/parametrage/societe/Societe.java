/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.societe;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.*;
import javax.xml.bind.annotation.XmlTransient;
import yvs.BeanDeBase;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsBaseGroupeSociete;
import yvs.entity.param.YvsModuleActive;
import yvs.grh.bean.Secteurs;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.ParametreServeurMail;

/**
 *
 * @author GOUCHERE YVES
 */
@SessionScoped
@ManagedBean
public class Societe extends BeanDeBase implements Serializable {

    private long id;
    private String raisonSocial;
    private String description;
    private String complement;
    private Dictionnaire ville = new Dictionnaire();
    private Dictionnaire pays = new Dictionnaire();
    private String numIdentification;
    private String telephone;
    private String aPropos;
    private String telephone1;
    private String telephone2;
    private Date debutExoComptable = new Date();
    private Date finExoComptable = new Date();
//    private byte tailleCmpteGeneraux;
//    private byte tailleCmpteAnalytique;
    private String devise;
    private String cachet;
    private int nombreDeDecimal = 2;
    private String email;
    private String siteWeb;
    private String fax;
    private Double capital = 0.0;
    private String logo, pathLogo;
    private String numeroContribuable;
    private String numeroRegistreCom;
    private String codeAbreviation;
    private String siege;
    private String adressSiege;
    private String umonaie;
    private String formeJuridique;
    private String codePostal;
    private int ecartDocument;
    private boolean selectActif;
    private boolean venteOnline;
    private boolean forGlp;
    private boolean printWithEntete;
    private List<ParametreServeurMail> listServeur;
    private List<Secteurs> secteurs;
    private List<YvsAgences> agences;
    private List<YvsModuleActive> modules;
    private YvsBaseGroupeSociete groupe;
    private InfosVenteSociete infosVente = new InfosVenteSociete();
    private Date dateSave = new Date();
    private long employeTemporaire;
    private long employePermament;
    private long employeTacheron;
    private long employeStagiaire;

    public Societe() {
        listServeur = new ArrayList<>();
        secteurs = new ArrayList<>();
        agences = new ArrayList<>();
        modules = new ArrayList<>();
    }

    public Societe(long id) {
        this();
        this.id = id;
    }

    public Societe(long id, String raisonSocial) {
        this(id);
        this.raisonSocial = raisonSocial;
    }

    public boolean isForGlp() {
        return forGlp;
    }

    public void setForGlp(boolean forGlp) {
        this.forGlp = forGlp;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPathLogo() {
        return pathLogo;
    }

    public void setPathLogo(String pathLogo) {
        this.pathLogo = pathLogo;
    }

    public boolean isVenteOnline() {
        return venteOnline;
    }

    public void setVenteOnline(boolean venteOnline) {
        this.venteOnline = venteOnline;
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

    public void setGroupe(YvsBaseGroupeSociete groupe) {
        this.groupe = groupe;
    }

    public YvsBaseGroupeSociete getGroupe() {
        return groupe;
    }

    public List<Secteurs> getSecteurs() {
        return secteurs;
    }

    public void setSecteurs(List<Secteurs> secteurs) {
        this.secteurs = secteurs;
    }

    public List<ParametreServeurMail> getListServeur() {
        return listServeur;
    }

    public void setListServeur(List<ParametreServeurMail> listServeur) {
        this.listServeur = listServeur;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAPropos() {
        return aPropos;
    }

    public void setAPropos(String aPropos) {
        this.aPropos = aPropos;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public String getRaisonSocial() {
        return raisonSocial;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public Date getDebutExoComptable() {
        return debutExoComptable;
    }

    public void setDebutExoComptable(Date debutExoComptable) {
        this.debutExoComptable = debutExoComptable;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public String getCachet() {
        return cachet;
    }

    public void setCachet(String cachet) {
        this.cachet = cachet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFinExoComptable() {
        return finExoComptable;
    }

    public void setFinExoComptable(Date finExoComptable) {
        this.finExoComptable = finExoComptable;
    }

    public int getNombreDeDecimal() {
        return nombreDeDecimal;
    }

    public void setNombreDeDecimal(int nombreDeDecimal) {
        this.nombreDeDecimal = nombreDeDecimal;
    }

    public String getNumIdentification() {
        return numIdentification;
    }

    public void setNumIdentification(String numIdentification) {
        this.numIdentification = numIdentification;
    }

    public Dictionnaire getPays() {
        return pays;
    }

    public void setPays(Dictionnaire pays) {
        this.pays = pays;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Dictionnaire getVille() {
        return ville;
    }

    public void setVille(Dictionnaire ville) {
        this.ville = ville;
    }

    public void test() {
        raisonSocial = description;
    }

    public Double getCapital() {
        return capital;
    }

    public void setCapital(Double capital) {
        this.capital = capital;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNumeroContribuable() {
        return numeroContribuable;
    }

    public void setNumeroContribuable(String numeroContribuable) {
        this.numeroContribuable = numeroContribuable;
    }

    public String getNumeroRegistreCom() {
        return numeroRegistreCom;
    }

    public void setNumeroRegistreCom(String numeroRegistreCom) {
        this.numeroRegistreCom = numeroRegistreCom;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodeAbreviation() {
        return codeAbreviation;
    }

    public void setCodeAbreviation(String codeAbreviation) {
        this.codeAbreviation = codeAbreviation;
    }

    public String getSiege() {
        return siege;
    }

    public void setSiege(String siege) {
        this.siege = siege;
    }

    public String getAdressSiege() {
        return adressSiege;
    }

    public void setAdressSiege(String adressSiege) {
        this.adressSiege = adressSiege;
    }

    public String getUmonaie() {
        return umonaie;
    }

    public void setUmonaie(String umonaie) {
        this.umonaie = umonaie;
    }

    public String getFormeJuridique() {
        return formeJuridique;
    }

    public void setFormeJuridique(String formeJuridique) {
        this.formeJuridique = formeJuridique;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public List<YvsAgences> getAgences() {
        return agences;
    }

    public void setAgences(List<YvsAgences> agences) {
        this.agences = agences;
    }

    public int getEcartDocument() {
        return ecartDocument;
    }

    public void setEcartDocument(int ecartDocument) {
        this.ecartDocument = ecartDocument;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isPrintWithEntete() {
        return printWithEntete;
    }

    public void setPrintWithEntete(boolean printWithEntete) {
        this.printWithEntete = printWithEntete;
    }

    public InfosVenteSociete getInfosVente() {
        return infosVente;
    }

    public void setInfosVente(InfosVenteSociete infosVente) {
        this.infosVente = infosVente;
    }

    public List<YvsModuleActive> getModules() {
        return modules;
    }

    public void setModules(List<YvsModuleActive> modules) {
        this.modules = modules;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Societe other = (Societe) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.depot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.client.Client;
import yvs.entity.base.YvsBaseConditionnementPoint;
import yvs.entity.base.YvsBasePointVenteUser;
import yvs.entity.commercial.YvsComCommercialPoint;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.users.YvsUsers;
import yvs.grh.bean.Employe;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.dico.Dictionnaire;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class PointVente implements Serializable {

    private long id;
    private String code;
    private String libelle;
    private String adresse;
    private String telephone;
    private String photo;
    private String color;
    private String codePin;
    private char type;
    private char livraisonOn;
    private char commissionFor;
    private boolean validationReglement;
    private boolean activerNotification;
    private boolean comptabilisationAuto;
    private boolean reglementAuto;
    private boolean prixMinStrict;
    private boolean actif = true;
    private boolean venteOnline;
    private boolean acceptClientNoName;
    private boolean saisiePhoneObligatoire;
    private double miminumActiveLivraison;
    private boolean select, update, new_, error;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();

    private Client client = new Client();
    private PointVente parent;
    private Agence agence = new Agence();
    private Dictionnaire secteur = new Dictionnaire();
    private Employe responsable = new Employe();

    private List<PointVenteDepot> liaisons;
    private List<YvsBaseConditionnementPoint> articles;
    private List<YvsComCreneauPoint> listTranche;
    private List<YvsUsers> vendeurs;
    private List<YvsComCommercialPoint> commerciaux;
    private List<YvsUsers> utilisateurs;

    public PointVente() {
        liaisons = new ArrayList<>();
        articles = new ArrayList<>();
        listTranche = new ArrayList<>();
        commerciaux = new ArrayList<>();
        vendeurs = new ArrayList<>();
        utilisateurs = new ArrayList<>();
    }

    public PointVente(long id) {
        this();
        this.id = id;
    }

    public PointVente(long id, String libelle) {
        this(id);
        this.libelle = libelle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public double getMiminumActiveLivraison() {
        return miminumActiveLivraison;
    }

    public void setMiminumActiveLivraison(double miminumActiveLivraison) {
        this.miminumActiveLivraison = miminumActiveLivraison;
    }

    public boolean isComptabilisationAuto() {
        return comptabilisationAuto;
    }

    public void setComptabilisationAuto(boolean comptabilisationAuto) {
        this.comptabilisationAuto = comptabilisationAuto;
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

    public boolean isAcceptClientNoName() {
        return acceptClientNoName;
    }

    public void setAcceptClientNoName(boolean acceptClientNoName) {
        this.acceptClientNoName = acceptClientNoName;
    }

    public char getType() {
        return type != ' ' ? String.valueOf(type).trim().length() > 0 ? type : Constantes.SERVICE_COMMERCE_GENERAL : Constantes.SERVICE_COMMERCE_GENERAL;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCodePin() {
        return codePin != null ? codePin : "";
    }

    public void setCodePin(String codePin) {
        this.codePin = codePin;
    }

    public char getCommissionFor() {
        return commissionFor != ' ' ? String.valueOf(commissionFor).trim().length() > 0 ? commissionFor : 'C' : 'C';
    }

    public void setCommissionFor(char commissionFor) {
        this.commissionFor = commissionFor;
    }

    public boolean isVenteOnline() {
        return venteOnline;
    }

    public void setVenteOnline(boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public boolean isSaisiePhoneObligatoire() {
        return saisiePhoneObligatoire;
    }

    public void setSaisiePhoneObligatoire(boolean saisiePhoneObligatoire) {
        this.saisiePhoneObligatoire = saisiePhoneObligatoire;
    }

    public char getLivraisonOn() {
        return String.valueOf(livraisonOn) != null ? String.valueOf(livraisonOn).trim().length() > 0 ? livraisonOn : 'V' : 'V';
    }

    public void setLivraisonOn(char livraisonOn) {
        this.livraisonOn = livraisonOn;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Employe getResponsable() {
        return responsable;
    }

    public void setResponsable(Employe responsable) {
        this.responsable = responsable;
    }

    public List<YvsBaseConditionnementPoint> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseConditionnementPoint> articles) {
        this.articles = articles;
    }

    public boolean isValidationReglement() {
        return validationReglement;
    }

    public void setValidationReglement(boolean validationReglement) {
        this.validationReglement = validationReglement;
    }

    public boolean isReglementAuto() {
        return reglementAuto;
    }

    public void setReglementAuto(boolean reglementAuto) {
        this.reglementAuto = reglementAuto;
    }

    public String getCode() {
        return code;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public Dictionnaire getSecteur() {
        return secteur;
    }

    public void setSecteur(Dictionnaire secteur) {
        this.secteur = secteur;
    }

    public List<PointVenteDepot> getLiaisons() {
        return liaisons;
    }

    public void setLiaisons(List<PointVenteDepot> liaisons) {
        this.liaisons = liaisons;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public PointVente getParent() {
        return parent;
    }

    public void setParent(PointVente parent) {
        this.parent = parent;
    }

    public List<YvsComCreneauPoint> getListTranche() {
        return listTranche;
    }

    public void setListTranche(List<YvsComCreneauPoint> listTranche) {
        this.listTranche = listTranche;
    }

    public List<YvsComCommercialPoint> getCommerciaux() {
        return commerciaux;
    }

    public void setCommerciaux(List<YvsComCommercialPoint> commerciaux) {
        this.commerciaux = commerciaux;
    }

    public boolean isPrixMinStrict() {
        return prixMinStrict;
    }

    public void setPrixMinStrict(boolean prixMinStrict) {
        this.prixMinStrict = prixMinStrict;
    }

    public List<YvsUsers> getVendeurs() {
        return vendeurs;
    }

    public void setVendeurs(List<YvsUsers> vendeurs) {
        this.vendeurs = vendeurs;
    }

    public boolean isActiverNotification() {
        return activerNotification;
    }

    public void setActiverNotification(boolean activerNotification) {
        this.activerNotification = activerNotification;
    }

    public List<YvsUsers> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<YvsUsers> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PointVente other = (PointVente) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}

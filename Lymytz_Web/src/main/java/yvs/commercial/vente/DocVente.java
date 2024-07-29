/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.vente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.ModelReglement;
import yvs.base.compta.Taxes;
import yvs.commercial.client.Client;
import yvs.entity.commercial.vente.YvsComCommercialVente;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComCoutSupDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.commercial.vente.YvsComRemiseDocVente;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;
import yvs.grh.presence.TrancheHoraire;
import yvs.init.Initialisation;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.entrepot.Depots;
import yvs.users.Users;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class DocVente implements Serializable, Comparable {

    private long id;
    private String numPiece;
    private String numDoc;
    private String numeroExterne;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private Date dateLivraisonPrevu = new Date();
    private Date dateLivraison = new Date();
    private Date dateAnnuler;
    private Date dateValider;
    private Date dateCloturer;
    private String typeDoc, description;
    private String statut = Constantes.ETAT_EDITABLE;
    private String statutRegle = String.valueOf(Constantes.STATUT_DOC_ATTENTE);
    private String statutLivre = String.valueOf(Constantes.STATUT_DOC_ATTENTE);
//    private BigInteger categorieTarifaire;
    private ModelReglement modeReglement = new ModelReglement();
    private String nomClient;
    private String telephone;
    private String nature = Constantes.VENTE;
    private boolean comptabilise;
    private boolean supp;
    private boolean actif;
    private boolean consigner, propagerRemise;
    private boolean livraisonAuto, validationReglement;
    private Date dateConsigner = new Date();
    private Date heureDoc = new Date();
    private Client tiers = new Client();
    private Users livreur = new Users();
    private Users AnnulerBy = new Users();
    private Users ValiderBy = new Users();
    private Users CloturerBy = new Users();
    private Users operateur = new Users();
    private DocVente documentLie;
    private EnteteDocVente enteteDoc = new EnteteDocVente();
    private Client client = new Client();
    private Depots depot = new Depots();
    private TrancheHoraire tranche = new TrancheHoraire();
    private CategorieComptable categorieComptable = new CategorieComptable();
    private ContenuDocVente contenu;
    private Dictionnaire pays = new Dictionnaire();
    private Dictionnaire ville = new Dictionnaire();
    private Dictionnaire adresse = new Dictionnaire();
    private DocVenteInformation information = new DocVenteInformation();
    private double montantAvance, commision, montantPlanifier;
    private int etapeValide, etapeTotal;
    private long nbreReglement, nbreCout, nbreCommerciaux, nbreDocLie, nbreLivraison;
    private double montantHT, montantTaxe, montantTTC, montantRemise, montantRemises, montantTotal, montantRistourne, montantCommission, montantCS, montantResteApayer, montantNetAPayer;
    private double montantTaxeR, montantAvoir, montantAvanceAvoir;    //contient le montant de la taxe des article dont le prix unitaire est un prix ttc.

    private List<Taxes> taxes;
    private List<YvsComMensualiteFactureVente> mensualites;
    private List<YvsComptaCaissePieceVente> reglements;
    private List<YvsComRemiseDocVente> remises;
    private List<YvsComContenuDocVente> contenus, contenusSave;
    private List<DocVente> fils;
    private List<YvsComCoutSupDocVente> couts;
    private boolean selectActif, new_, update, mens, cloturer, error, list, fromFacture = true;
    private List<YvsWorkflowValidFactureVente> etapesValidations;
    private List<YvsComCommercialVente> commerciaux;
    private List<YvsComDocVentes> documents;
    private String firstEtape = "VALIDER", printPath = Initialisation.getCheminSave() + FILE_SEPARATOR + "1.pdf";

    public DocVente() {
        taxes = new ArrayList<>();
        couts = new ArrayList<>();
        mensualites = new ArrayList<>();
        remises = new ArrayList<>();
        contenus = new ArrayList<>();
        fils = new ArrayList<>();
        reglements = new ArrayList<>();
        etapesValidations = new ArrayList<>();
        commerciaux = new ArrayList<>();
        documents = new ArrayList<>();
        contenusSave = new ArrayList<>();
    }

    public DocVente(long id) {
        this();
        this.id = id;
    }

    public DocVente(String numDoc) {
        this();
        this.numDoc = numDoc;
    }

    public DocVente(long id, String numDoc) {
        this(id);
        this.numDoc = numDoc;
    }

    public DocVente(long id, String numDoc, String statut) {
        this(id, numDoc);
        this.statut = statut;
    }

    public boolean isFromFacture() {
        return fromFacture;
    }

    public void setFromFacture(boolean fromFacture) {
        this.fromFacture = fromFacture;
    }

    public boolean isComptabilise() {
        return comptabilise;
    }

    public void setComptabilise(boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public long getNbreLivraison() {
        return nbreLivraison;
    }

    public void setNbreLivraison(long nbreLivraison) {
        this.nbreLivraison = nbreLivraison;
    }

    public String getPrintPath() {
        return printPath;
    }

    public void setPrintPath(String printPath) {
        this.printPath = printPath;
    }

    public String getNumeroExterne() {
        return numeroExterne;
    }

    public void setNumeroExterne(String numeroExterne) {
        this.numeroExterne = numeroExterne;
    }

    public String getFirstEtape() {
        return firstEtape;
    }

    public void setFirstEtape(String firstEtape) {
        this.firstEtape = firstEtape;
    }

    public int getEtapeValide() {
        return etapeValide;
    }

    public void setEtapeValide(int etapeValide) {
        this.etapeValide = etapeValide;
    }

    public int getEtapeTotal() {
        return etapeTotal;
    }

    public void setEtapeTotal(int etapeTotal) {
        this.etapeTotal = etapeTotal;
    }

    public List<YvsComDocVentes> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocVentes> documents) {
        this.documents = documents;
    }

    public boolean isPropagerRemise() {
        return propagerRemise;
    }

    public void setPropagerRemise(boolean propagerRemise) {
        this.propagerRemise = propagerRemise;
    }

    public boolean isValidationReglement() {
        return validationReglement;
    }

    public void setValidationReglement(boolean validationReglement) {
        this.validationReglement = validationReglement;
    }

    public Client getTiers() {
        return tiers;
    }

    public void setTiers(Client tiers) {
        this.tiers = tiers;
    }

    public double getCommision() {
        return commision;
    }

    public void setCommision(double commision) {
        this.commision = commision;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public boolean isLivraisonAuto() {
        return livraisonAuto;
    }

    public void setLivraisonAuto(boolean livraisonAuto) {
        this.livraisonAuto = livraisonAuto;
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

    public Dictionnaire getAdresse() {
        return adresse;
    }

    public void setAdresse(Dictionnaire adresse) {
        this.adresse = adresse;
    }

    public List<YvsWorkflowValidFactureVente> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidFactureVente> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public List<YvsComCoutSupDocVente> getCouts() {
        return couts;
    }

    public void setCouts(List<YvsComCoutSupDocVente> couts) {
        this.couts = couts;
    }

    public boolean isConsigner() {
        return consigner;
    }

    public void setConsigner(boolean consigner) {
        this.consigner = consigner;
    }

    public Date getDateConsigner() {
        return dateConsigner;
    }

    public void setDateConsigner(Date dateConsigner) {
        this.dateConsigner = dateConsigner;
    }

    public Users getCloturerBy() {
        return CloturerBy;
    }

    public void setCloturerBy(Users CloturerBy) {
        this.CloturerBy = CloturerBy;
    }

    public Date getDateLivraison() {
        return dateLivraison != null ? dateLivraison : new Date();
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public Date getDateAnnuler() {
        return dateAnnuler;
    }

    public void setDateAnnuler(Date dateAnnuler) {
        this.dateAnnuler = dateAnnuler;
    }

    public Date getDateValider() {
        return dateValider;
    }

    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

    public Date getDateCloturer() {
        return dateCloturer;
    }

    public void setDateCloturer(Date dateCloturer) {
        this.dateCloturer = dateCloturer;
    }

    public Users getAnnulerBy() {
        return AnnulerBy;
    }

    public void setAnnulerBy(Users AnnulerBy) {
        this.AnnulerBy = AnnulerBy;
    }

    public Users getValiderBy() {
        return ValiderBy;
    }

    public void setValiderBy(Users ValiderBy) {
        this.ValiderBy = ValiderBy;
    }

    public Date getDateLivraisonPrevu() {
        return dateLivraisonPrevu;
    }

    public void setDateLivraisonPrevu(Date dateLivraisonPrevu) {
        this.dateLivraisonPrevu = dateLivraisonPrevu;
    }

    public double getMontantRemises() {
        return montantRemises;
    }

    public void setMontantRemises(double montantRemises) {
        this.montantRemises = montantRemises;
    }

    public ModelReglement getModeReglement() {
        return modeReglement;
    }

    public void setModeReglement(ModelReglement modeReglement) {
        this.modeReglement = modeReglement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TrancheHoraire getTranche() {
        return tranche;
    }

    public void setTranche(TrancheHoraire tranche) {
        this.tranche = tranche;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isCloturer() {
        return cloturer;
    }

    public void setCloturer(boolean cloturer) {
        this.cloturer = cloturer;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public List<YvsComptaCaissePieceVente> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceVente> reglements) {
        this.reglements = reglements;
    }

    public String getStatutLivre() {
        return statutLivre != null ? statutLivre.trim().length() > 0 ? statutLivre : String.valueOf(Constantes.STATUT_DOC_ATTENTE) : String.valueOf(Constantes.STATUT_DOC_ATTENTE);
    }

    public void setStatutLivre(String statutLivre) {
        this.statutLivre = statutLivre;
    }

    public String getStatutRegle() {
        return statutRegle != null ? statutRegle.trim().length() > 0 ? statutRegle : String.valueOf(Constantes.STATUT_DOC_ATTENTE) : String.valueOf(Constantes.STATUT_DOC_ATTENTE);
    }

    public void setStatutRegle(String statutRegle) {
        this.statutRegle = statutRegle;
    }

    public boolean isMens() {
        return mens;
    }

    public void setMens(boolean mens) {
        this.mens = mens;
    }

    public List<YvsComMensualiteFactureVente> getMensualites() {
        return mensualites;
    }

    public void setMensualites(List<YvsComMensualiteFactureVente> mensualites) {
        this.mensualites = mensualites;
    }

    public double getMontantAvance() {
        return montantAvance;
    }

    public void setMontantAvance(double montantAvance) {
        this.montantAvance = montantAvance;
    }

    public double getMontantPlanifier() {
        return montantPlanifier;
    }

    public void setMontantPlanifier(double montantPlanifier) {
        this.montantPlanifier = montantPlanifier;
    }

    public List<YvsComRemiseDocVente> getRemises() {
        return remises;
    }

    public void setRemises(List<YvsComRemiseDocVente> remises) {
        this.remises = remises;
    }

    public List<Taxes> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<Taxes> taxes) {
        this.taxes = taxes;
    }

    public double getMontantNetAPayer() {
        return getMontantTotal();
    }

    public void setMontantNetAPayer(double montantNetAPayer) {
        this.montantNetAPayer = montantNetAPayer;
    }

    public double getMontantResteApayer() {
        montantResteApayer = getMontantNetAPayer() - getMontantAvance();
        return montantResteApayer;
    }

    public double getMontantResteAPlanifier() {
        montantResteApayer = getMontantNetAPayer() - getMontantPlanifier();
        return montantResteApayer;
    }

    public void setMontantResteApayer(double montantResteApayer) {
        this.montantResteApayer = montantResteApayer;
    }

    public double getMontantTaxeR() {
        return montantTaxeR;
    }

    public void setMontantTaxeR(double montantTaxeR) {
        this.montantTaxeR = montantTaxeR;
    }

    public double getMontantTaxe() {
        return montantTaxe;
    }

    public void setMontantTaxe(double montantTaxe) {
        this.montantTaxe = montantTaxe;
    }

    public double getMontantHT() {
        montantHT = getMontantTTC() - getMontantTaxe();
        return montantHT;
    }

    public void setMontantHT(double montantHT) {
        this.montantHT = montantHT;
    }

    public double getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(double montantTTC) {
        this.montantTTC = montantTTC;
    }

    public double getMontantRemise() {
        return montantRemise;
    }

    public void setMontantRemise(double montantRemise) {
        this.montantRemise = montantRemise;
    }

    public double getMontantAvoir() {
        return montantAvoir;
    }

    public void setMontantAvoir(double montantAvoir) {
        this.montantAvoir = montantAvoir;
    }

    public double getMontantAvanceAvoir() {
        return montantAvanceAvoir;
    }

    public void setMontantAvanceAvoir(double montantAvanceAvoir) {
        this.montantAvanceAvoir = montantAvanceAvoir;
    }

    public double getMontantTotal() {
        montantTotal = getMontantTTC() + getMontantCS() - getMontantRemises() - getMontantAvoir() + getMontantAvanceAvoir();
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public double getMontantRistourne() {
        return montantRistourne;
    }

    public void setMontantRistourne(double montantRistourne) {
        this.montantRistourne = montantRistourne;
    }

    public double getMontantCommission() {
        return montantCommission;
    }

    public void setMontantCommission(double montantCommission) {
        this.montantCommission = montantCommission;
    }

    public double getMontantCS() {
        return montantCS;
    }

    public void setMontantCS(double montantCS) {
        this.montantCS = montantCS;
    }

    public Date getHeureDoc() {
        return heureDoc != null ? heureDoc : new Date();
    }

    public void setHeureDoc(Date heureDoc) {
        this.heureDoc = heureDoc;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumPiece() {
        return numPiece;
    }

    public void setNumPiece(String numPiece) {
        this.numPiece = numPiece;
    }

    public String getNumDoc() {
        return numDoc != null ? numDoc : "";
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getTypeDoc() {
        return typeDoc != null ? typeDoc : "";
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public String getStatut() {
        return statut != null ? statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public List<YvsComCommercialVente> getCommerciaux() {
        return commerciaux;
    }

    public void setCommerciaux(List<YvsComCommercialVente> commerciaux) {
        this.commerciaux = commerciaux;
    }

    public String getNom_client() {
        String nom_client = "";
        if (getNomClient().trim().length() > 0 && getClient() != null) {
            if (getNomClient().equals(getClient().getNom_prenom())) {
                nom_client = getClient().getNom_prenom();
            } else {
                nom_client = getNomClient() + " [" + getClient().getCodeClient() + "]";
            }
        } else if (getClient() != null) {
            nom_client = getClient().getNom_prenom();
        } else if (getNomClient().trim().length() > 0) {
            nom_client = getNomClient();
        }
        return nom_client;
    }

    public String getNomClient() {
        return nomClient != null ? nomClient : "";
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Users getLivreur() {
        return livreur;
    }

    public void setLivreur(Users livreur) {
        this.livreur = livreur;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public DocVenteInformation getInformation() {
        return information;
    }

    public void setInformation(DocVenteInformation information) {
        this.information = information;
    }

    public List<YvsComContenuDocVente> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComContenuDocVente> contenus) {
        this.contenus = contenus;
    }

    public List<DocVente> getFils() {
        return fils;
    }

    public void setFils(List<DocVente> fils) {
        this.fils = fils;
    }

    public DocVente getDocumentLie() {
        return documentLie;
    }

    public void setDocumentLie(DocVente documentLie) {
        this.documentLie = documentLie;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public CategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(CategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUpdate() {
        return id > 0;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public EnteteDocVente getEnteteDoc() {
        return enteteDoc;
    }

    public void setEnteteDoc(EnteteDocVente enteteDoc) {
        this.enteteDoc = enteteDoc;
    }

    public ContenuDocVente getContenu() {
        return contenu;
    }

    public void setContenu(ContenuDocVente contenu) {
        this.contenu = contenu;
    }

    public long getNbreReglement() {
        return nbreReglement;
    }

    public void setNbreReglement(long nbreReglement) {
        this.nbreReglement = nbreReglement;
    }

    public long getNbreCout() {
        return nbreCout;
    }

    public void setNbreCout(long nbreCout) {
        this.nbreCout = nbreCout;
    }

    public long getNbreCommerciaux() {
        return nbreCommerciaux;
    }

    public void setNbreCommerciaux(long nbreCommerciaux) {
        this.nbreCommerciaux = nbreCommerciaux;
    }

    public long getNbreDocLie() {
        return nbreDocLie;
    }

    public void setNbreDocLie(long nbreDocLie) {
        this.nbreDocLie = nbreDocLie;
    }

    public Users getOperateur() {
        return operateur;
    }

    public void setOperateur(Users operateur) {
        this.operateur = operateur;
    }

    public List<YvsComContenuDocVente> getContenusSave() {
        return contenusSave;
    }

    public void setContenusSave(List<YvsComContenuDocVente> contenusSave) {
        this.contenusSave = contenusSave;
    }

    public boolean isLocation() {
        return nature != null ? nature.equals(Constantes.LOCATION) || nature.equals(Constantes.LOCATION_MATERIEL) || nature.equals(Constantes.LOCATION_IMMOBILIER) : false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final DocVente other = (DocVente) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        DocVente d = (DocVente) o;
        if (enteteDoc.equals(d.enteteDoc)) {
            if (heureDoc.equals(d.heureDoc)) {
                return Long.valueOf(id).compareTo(d.id);
            }
            return heureDoc.compareTo(d.heureDoc);
        }
        return enteteDoc.compareTo(d.enteteDoc);
    }

}

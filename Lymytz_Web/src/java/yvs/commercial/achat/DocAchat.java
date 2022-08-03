/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.achat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.ModelReglement;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.commercial.param.TypeDocDivers;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComCoutSupDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.param.workflow.YvsWorkflowValidFactureAchat;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.entrepot.Depots;
import yvs.users.Users;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class DocAchat implements Serializable, Comparable {

    private long id;
    private String numPiece;
    private String statut = Constantes.ETAT_EDITABLE, nameEtat;
    private String statutLivre;
    private String statutRegle;
    private String typeDoc;
    private boolean comptabilise;
    private String legendeType;
    private String numDoc;
    private Date dateSave;
    private String nameDoc;
    private String referenceExterne;
    private String fichier;
    private String description, libEtapes;
    private boolean livraisonDo, automatique, genererFactureAuto;
    private CategorieComptable categorieComptable = new CategorieComptable();
    private ModelReglement modeReglement = new ModelReglement();
    private Date dateDoc = new Date();
    private Date dateUpdate = new Date();
    private Date dateAnnuler = new Date();
    private Date dateValider = new Date();
    private Date dateCloturer = new Date();
    private Date dateSolder = new Date();
    private Date dateLivraison = new Date();
    private boolean supp, error;
    private boolean actif, list;
    private boolean cloturer, fromFacture = true;
    private int etapeValide, etapeTotal;
    private Users AnnulerBy = new Users();
    private Users ValiderBy = new Users();
    private Users CloturerBy = new Users();
    private List<YvsComContenuDocAchat> contenus, contenusSave;
    private List<DocAchat> fils;
    private DocAchat documentLie;
    private Fournisseur fournisseur = new Fournisseur();
    private Depots depotReception = new Depots();
    private TypeDocDivers typeAchat = new TypeDocDivers();
    private double montantTTC, montantHT, montantRemise, montantTaxe, montantTaxeR, montantCS, montantTotal, montantAvance, montantNetApayer, montantAvoir, montantAvanceAvoir;
    private double quantiteTotal;
    private List<MensualiteFactureAchat> mensualites;
    private List<YvsComptaCaissePieceAchat> reglements;
    private List<YvsBaseTaxes> taxes;
    private List<YvsComCoutSupDocAchat> couts;
    private TrancheHoraire tranche = new TrancheHoraire();
    private List<YvsWorkflowValidFactureAchat> etapesValidations;
    private List<YvsComDocAchats> documents;
    private boolean selectActif, new_, update, mens;
    private String firstEtape = "VALIDER";

    public DocAchat() {
        taxes = new ArrayList<>();
        contenus = new ArrayList<>();
        fils = new ArrayList<>();
        couts = new ArrayList<>();
        mensualites = new ArrayList<>();
        reglements = new ArrayList<>();
        contenusSave = new ArrayList<>();
        etapesValidations = new ArrayList<>();
        documents = new ArrayList<>();
    }

    public DocAchat(long id) {
        this();
        this.id = id;
    }

    public DocAchat(String numDoc) {
        this();
        this.numDoc = numDoc;
    }

    public DocAchat(String numDoc, boolean error) {
        this(numDoc);
        this.error = error;
    }

    public DocAchat(long id, String numDoc) {
        this(id);
        this.numDoc = numDoc;
    }

    public DocAchat(long id, CategorieComptable categorieComptable) {
        this(id);
        this.categorieComptable = categorieComptable;
    }

    public DocAchat(long id, String numDoc, String statut) {
        this(id, numDoc);
        this.statut = statut;
    }

    public boolean isComptabilise() {
        return comptabilise;
    }

    public void setComptabilise(boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public boolean isAutomatique() {
        return automatique;
    }

    public void setAutomatique(boolean automatique) {
        this.automatique = automatique;
    }

    public String getFirstEtape() {
        return firstEtape;
    }

    public void setFirstEtape(String firstEtape) {
        this.firstEtape = firstEtape;
    }

    public int getEtapeTotal() {
        return etapeTotal;
    }

    public void setEtapeTotal(int etapeTotal) {
        this.etapeTotal = etapeTotal;
    }

    public boolean isFromFacture() {
        return fromFacture;
    }

    public void setFromFacture(boolean fromFacture) {
        this.fromFacture = fromFacture;
    }

    public String getLibEtapes() {
        return libEtapes;
    }

    public void setLibEtapes(String libEtapes) {
        this.libEtapes = libEtapes;
    }

    public List<YvsComDocAchats> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocAchats> documents) {
        this.documents = documents;
    }

    public boolean isGenererFactureAuto() {
        return genererFactureAuto;
    }

    public void setGenererFactureAuto(boolean genererFactureAuto) {
        this.genererFactureAuto = genererFactureAuto;
    }

    public int getEtapeValide() {
        return etapeValide;
    }

    public void setEtapeValide(int etapeValide) {
        this.etapeValide = etapeValide;
    }

    public boolean isLivraisonDo() {
        return livraisonDo;
    }

    public void setLivraisonDo(boolean livraisonDo) {
        this.livraisonDo = livraisonDo;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public List<YvsWorkflowValidFactureAchat> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidFactureAchat> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public TypeDocDivers getTypeAchat() {
        return typeAchat;
    }

    public void setTypeAchat(TypeDocDivers typeAchat) {
        this.typeAchat = typeAchat;
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

    public Date getDateSolder() {
        return dateSolder;
    }

    public void setDateSolder(Date dateSolder) {
        this.dateSolder = dateSolder;
    }

    public Date getDateLivraison() {
        return dateLivraison != null ? dateLivraison : new Date();
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
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

    public Users getCloturerBy() {
        return CloturerBy;
    }

    public void setCloturerBy(Users CloturerBy) {
        this.CloturerBy = CloturerBy;
    }

    public TrancheHoraire getTranche() {
        return tranche;
    }

    public void setTranche(TrancheHoraire tranche) {
        this.tranche = tranche;
    }

    public boolean isCloturer() {
        return cloturer;
    }

    public void setCloturer(boolean cloturer) {
        this.cloturer = cloturer;
    }

    public double getQuantiteTotal() {
        return quantiteTotal;
    }

    public void setQuantiteTotal(double quantiteTotal) {
        this.quantiteTotal = quantiteTotal;
    }

    public Date getDateSave() {
        return dateSave!=null?dateSave:new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public double getMontantTaxeR() {
        return montantTaxeR;
    }

    public void setMontantTaxeR(double montantTaxeR) {
        this.montantTaxeR = montantTaxeR;
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
        montantTotal = getMontantTTC() - getMontantCS() - getMontantRemise() - getMontantAvoir() + getMontantAvanceAvoir();
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public double getMontantNetApayer() {
        montantNetApayer = getMontantTotal() - getMontantAvance();
        return montantNetApayer;
    }

    public void setMontantNetApayer(double montantNetApayer) {
        this.montantNetApayer = montantNetApayer;
    }

    public double getMontantAvance() {
        return montantAvance;
    }

    public void setMontantAvance(double montantAvance) {
        this.montantAvance = montantAvance;
    }

    public double getMontantCS() {
        return montantCS;
    }

    public void setMontantCS(double montantCS) {
        this.montantCS = montantCS;
    }

    public double getMontantTaxe() {
        return montantTaxe;
    }

    public void setMontantTaxe(double montantTaxe) {
        this.montantTaxe = montantTaxe;
    }

    public double getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(double montantTTC) {
        this.montantTTC = montantTTC;
    }

    public double getMontantHT() {
        montantHT = getMontantTTC() - getMontantTaxe();
        return montantHT;
    }

    public void setMontantHT(double montantHT) {
        this.montantHT = montantHT;
    }

    public List<YvsComptaCaissePieceAchat> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceAchat> reglements) {
        this.reglements = reglements;
    }

    public List<YvsBaseTaxes> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsBaseTaxes> taxes) {
        this.taxes = taxes;
    }

    public CategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(CategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    public String getNameEtat() {
        return nameEtat;
    }

    public void setNameEtat(String nameEtat) {
        this.nameEtat = nameEtat;
    }

    public boolean isMens() {
        return mens;
    }

    public void setMens(boolean mens) {
        this.mens = mens;
    }

    public List<MensualiteFactureAchat> getMensualites() {
        return mensualites;
    }

    public void setMensualites(List<MensualiteFactureAchat> mensualites) {
        this.mensualites = mensualites;
    }

    public List<YvsComCoutSupDocAchat> getCouts() {
        return couts;
    }

    public void setCouts(List<YvsComCoutSupDocAchat> couts) {
        this.couts = couts;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public Depots getDepotReception() {
        return depotReception;
    }

    public void setDepotReception(Depots depotReception) {
        this.depotReception = depotReception;
    }

    public String getNameDoc() {
        return nameDoc;
    }

    public void setNameDoc(String nameDoc) {
        this.nameDoc = nameDoc;
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

    public String getStatut() {
        return statut != null ? statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getStatutLivre() {
        return statutLivre != null ? statutLivre.trim().length() > 0 ? statutLivre : Constantes.ETAT_ATTENTE : Constantes.ETAT_ATTENTE;
    }

    public void setStatutLivre(String statutLivre) {
        this.statutLivre = statutLivre;
    }

    public String getStatutRegle() {
        return statutRegle != null ? statutRegle.trim().length() > 0 ? statutRegle : Constantes.ETAT_ATTENTE : Constantes.ETAT_ATTENTE;
    }

    public void setStatutRegle(String statutRegle) {
        this.statutRegle = statutRegle;
    }

    public String getTypeDoc() {
        return typeDoc != null ? typeDoc : "";
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public String getLegendeType() {
        return legendeType;
    }

    public void setLegendeType(String legendeType) {
        this.legendeType = legendeType;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public Date getDateDoc() {
        return dateDoc;
    }

    public void setDateDoc(Date dateDoc) {
        this.dateDoc = dateDoc;
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

    public List<YvsComContenuDocAchat> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComContenuDocAchat> contenus) {
        this.contenus = contenus;
    }

    public List<YvsComContenuDocAchat> getContenusSave() {
        return contenusSave;
    }

    public void setContenusSave(List<YvsComContenuDocAchat> contenusSave) {
        this.contenusSave = contenusSave;
    }

    public List<DocAchat> getFils() {
        return fils;
    }

    public void setFils(List<DocAchat> fils) {
        this.fils = fils;
    }

    public DocAchat getDocumentLie() {
        return documentLie;
    }

    public void setDocumentLie(DocAchat documentLie) {
        this.documentLie = documentLie;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final DocAchat other = (DocAchat) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        DocAchat m = (DocAchat) o;
        if (dateDoc.equals(m.dateDoc)) {
            return Long.valueOf(id).compareTo(m.id);
        }
        return dateDoc.compareTo(m.dateDoc);
    }

}

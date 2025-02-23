/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.CategorieComptable;
import yvs.commercial.achat.DocAchat;
import yvs.commercial.creneau.Creneau;
import yvs.dao.Util;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComCoutSupDocStock;
import yvs.entity.param.workflow.YvsWorkflowValidDocStock;
import yvs.parametrage.entrepot.Depots;
import yvs.users.Users;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class DocStock implements Serializable, Comparable {

    private long id;
    private Date dateDoc = new Date();
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private Date dateReception = new Date();
    private String numPiece;
    private String numDoc;
    private String nature, description;
    private NatureDoc natureDoc = new NatureDoc();
    private String statut = Constantes.ETAT_EDITABLE;
    private String typeDoc = Constantes.TYPE_ES;
    private boolean supp, error;
    private boolean actif, automatique;
    private int etapeValide, etapeTotal;
    private double montantTotalCS, montantTotal, montantTotalES, tauxEcartInventaire = 1, taux;
    private Creneau creneauDestinataire = new Creneau();
    private Creneau creneauSource = new Creneau();
    private Depots source = new Depots();
    private Depots destination = new Depots();
    private CategorieComptable categorieComptable = new CategorieComptable();
    private List<YvsComCoutSupDocStock> couts;
    private List<YvsComContenuDocStock> contenus, contenusSave;
    private DocStock documentLie;
    private DocAchat docAchat = new DocAchat();
    private String mouvement = "E";
    private boolean selectActif, new_, update, in, passation;
    private boolean cloturer;
    private boolean majPr;
    private Date dateAnnuler = new Date();
    private Users AnnulerBy = new Users();
    private Date dateValider = new Date();
    private Users ValiderBy = new Users();
    private Date dateCloturer = new Date();
    private Users CloturerBy = new Users();
    private Users editeur = new Users();
    private List<YvsWorkflowValidDocStock> etapesValidations;
    private String author;
    private String firstEtape = "VALIDER";
    private DocStockValeur valeur = new DocStockValeur();

    public DocStock() {
        couts = new ArrayList<>();
        contenus = new ArrayList<>();
        contenusSave = new ArrayList<>();
        etapesValidations = new ArrayList<>();
    }

    public DocStock(long id) {
        this();
        this.id = id;
    }

    public DocStock(String numDoc) {
        this();
        this.numDoc = numDoc;
    }

    public DocStock(long id, String numDoc) {
        this(id);
        this.numDoc = numDoc;
    }

    public DocStock(long id, String numDoc, String statut) {
        this(id, numDoc);
        this.statut = statut;
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

    public Users getEditeur() {
        return editeur;
    }

    public void setEditeur(Users editeur) {
        this.editeur = editeur;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public List<YvsWorkflowValidDocStock> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidDocStock> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public Date getDateAnnuler() {
        return dateAnnuler != null ? dateAnnuler : new Date();
    }

    public void setDateAnnuler(Date dateAnnuler) {
        this.dateAnnuler = dateAnnuler;
    }

    public Date getDateValider() {
        return dateValider != null ? dateValider : new Date();
    }

    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isPassation() {
        return passation;
    }

    public void setPassation(boolean passation) {
        this.passation = passation;
    }

    public Date getDateCloturer() {
        return dateCloturer != null ? dateCloturer : new Date();
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

    public Users getCloturerBy() {
        return CloturerBy;
    }

    public void setCloturerBy(Users CloturerBy) {
        this.CloturerBy = CloturerBy;
    }

    public String getNature() {
        return Util.asString(nature) ? nature : (getTypeDoc().equals(Constantes.TYPE_FT) ? Constantes.TRANSFERT : Constantes.OP_DONS);
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTauxEcartInventaire() {
        return tauxEcartInventaire;
    }

    public void setTauxEcartInventaire(double tauxEcartInventaire) {
        this.tauxEcartInventaire = tauxEcartInventaire;
    }

    public boolean isCloturer() {
        return cloturer;
    }

    public void setCloturer(boolean cloturer) {
        this.cloturer = cloturer;
    }

    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    public String getMouvement() {
        return mouvement;
    }

    public String getMouvement_() {
        if (Constantes.TYPE_ES.equals(typeDoc)) {
            mouvement = "E";
        } else {
            mouvement = "S";
        }
        return mouvement;
    }

    public void setMouvement(String mouvement) {
        this.mouvement = mouvement;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public DocAchat getDocAchat() {
        return docAchat;
    }

    public void setDocAchat(DocAchat docAchat) {
        this.docAchat = docAchat;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public double getMontantTotalES() {
        return montantTotalES;
    }

    public void setMontantTotalES(double montantTotalES) {
        this.montantTotalES = montantTotalES;
    }

    public double getMontantTotalCS() {
        return montantTotalCS;
    }

    public void setMontantTotalCS(double montantTotalCS) {
        this.montantTotalCS = montantTotalCS;
    }

    public DocStock getDocumentLie() {
        return documentLie;
    }

    public void setDocumentLie(DocStock documentLie) {
        this.documentLie = documentLie;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateReception() {
        return dateReception != null ? dateReception : new Date();
    }

    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    public Date getDateDoc() {
        return dateDoc != null ? dateDoc : new Date();
    }

    public void setDateDoc(Date dateDoc) {
        this.dateDoc = dateDoc;
    }

    public String getNumPiece() {
        return numPiece != null ? numPiece : "";
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

    public String getStatut() {
        return statut != null ? statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getTypeDoc() {
        return typeDoc != null ? typeDoc : "";
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
    }

    public boolean isAutomatique() {
        return automatique;
    }

    public void setAutomatique(boolean automatique) {
        this.automatique = automatique;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Creneau getCreneauDestinataire() {
        return creneauDestinataire != null ? creneauDestinataire : new Creneau();
    }

    public void setCreneauDestinataire(Creneau creneauDestinataire) {
        this.creneauDestinataire = creneauDestinataire;
    }

    public Creneau getCreneauSource() {
        return creneauSource != null ? creneauSource : new Creneau();
    }

    public void setCreneauSource(Creneau creneauSource) {
        this.creneauSource = creneauSource;
    }

    public Depots getSource() {
        return source != null ? source : new Depots();
    }

    public void setSource(Depots source) {
        this.source = source;
    }

    public Depots getDestination() {
        return destination != null ? destination : new Depots();
    }

    public void setDestination(Depots destination) {
        this.destination = destination;
    }

    public CategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(CategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    public List<YvsComCoutSupDocStock> getCouts() {
        return couts;
    }

    public void setCouts(List<YvsComCoutSupDocStock> couts) {
        this.couts = couts;
    }

    public List<YvsComContenuDocStock> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComContenuDocStock> contenus) {
        this.contenus = contenus;
    }

    public List<YvsComContenuDocStock> getContenusSave() {
        return contenusSave;
    }

    public void setContenusSave(List<YvsComContenuDocStock> contenusSave) {
        this.contenusSave = contenusSave;
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

    public DocStockValeur getValeur() {
        return valeur;
    }

    public void setValeur(DocStockValeur valeur) {
        this.valeur = valeur;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isMajPr() {
        return majPr;
    }

    public void setMajPr(boolean majPr) {
        this.majPr = majPr;
    }

    public NatureDoc getNatureDoc() {
        return natureDoc;
    }

    public void setNatureDoc(NatureDoc natureDoc) {
        this.natureDoc = natureDoc;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final DocStock other = (DocStock) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        DocStock m = (DocStock) o;
        if (dateDoc.equals(m.dateDoc)) {
            return Long.valueOf(id).compareTo(m.id);
        }
        return dateDoc.compareTo(m.dateDoc);
    }

}

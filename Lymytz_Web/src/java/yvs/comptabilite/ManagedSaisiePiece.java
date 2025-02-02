/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.Comptes;
import yvs.base.compta.Journaux;
import yvs.base.compta.ManagedCentreAnalytique;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.ManagedJournaux;
import yvs.base.compta.UtilCompta;
import yvs.base.tiers.ManagedTiers;
import yvs.base.tiers.Profil;
import yvs.base.tiers.Tiers;
import yvs.base.tiers.UtilTiers;
import yvs.commercial.achat.ManagedFactureAchat;
import yvs.commercial.vente.ManagedFactureVenteV2;
import yvs.commercial.vente.ManagedVente;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.comptabilite.caisse.ManagedReglementAchat;
import yvs.comptabilite.caisse.ManagedReglementMission;
import yvs.comptabilite.caisse.ManagedReglementVente;
import yvs.comptabilite.caisse.ManagedVirement;
import yvs.comptabilite.client.ManagedOperationClient;
import yvs.comptabilite.fournisseur.ManagedOperationFournisseur;
import yvs.comptabilite.tresorerie.ManagedDocDivers;
import yvs.dao.Options;
import yvs.dao.salaire.service.ResultatAction;
import yvs.dao.services.compta.ServiceComptabilite;
import yvs.dao.services.compta.TempContent;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaAbonementDocDivers;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaAffectationGenAnal;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceCompensation;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaContentModeleSaisi;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaModeleSaisie;
import yvs.entity.compta.YvsComptaPhasePiece;
import yvs.entity.compta.YvsComptaCaissePieceMission;
import yvs.entity.compta.YvsComptaCreditClient;
import yvs.entity.compta.YvsComptaPhasePieceAchat;
import yvs.entity.compta.YvsComptaPhasePieceDivers;
import yvs.entity.compta.YvsComptaPhasePieceVirement;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.YvsComptaReglementCreditClient;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.entity.compta.achat.YvsComptaCreditFournisseur;
import yvs.entity.compta.achat.YvsComptaPhaseAcompteAchat;
import yvs.entity.compta.achat.YvsComptaReglementCreditFournisseur;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.compta.client.YvsComptaPhaseReglementCreditClient;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCentreDocDivers;
import yvs.entity.compta.divers.YvsComptaCoutSupDocDivers;
import yvs.entity.compta.divers.YvsComptaTaxeDocDivers;
import yvs.entity.compta.fournisseur.YvsComptaPhaseReglementCreditFournisseur;
import yvs.entity.compta.saisie.YvsComptaContentAnalytique;
import yvs.entity.compta.saisie.YvsComptaContentJournalAbonnementDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalAcompteClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalAcompteFournisseur;
import yvs.entity.compta.saisie.YvsComptaContentJournalBulletin;
import yvs.entity.compta.saisie.YvsComptaContentJournalCreditClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalCreditFournisseur;
import yvs.entity.compta.saisie.YvsComptaContentJournalDocDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalEnteteBulletin;
import yvs.entity.compta.saisie.YvsComptaContentJournalEnteteFactureVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapeAcompteAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapeAcompteVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceVirement;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapeReglementCreditClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapeReglementCreditFournisseur;
import yvs.entity.compta.saisie.YvsComptaContentJournalFactureAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalFactureVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceMission;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceVirement;
import yvs.entity.compta.saisie.YvsComptaContentJournalReglementCreditClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalReglementCreditFournisseur;
import yvs.entity.compta.saisie.YvsComptaContentJournalRetenueSalaire;
import yvs.entity.compta.vente.YvsComptaPhaseAcompteVente;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.salaire.YvsGrhBulletins;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.stat.export.YvsStatExportEtat;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.grh.bean.mission.ManagedMission;
import yvs.grh.paie.ManagedRetenue;
import yvs.grh.paie.ManagedSalaire;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.mutuelle.Exercice;
import yvs.mutuelle.ManagedExercice;
import yvs.mutuelle.UtilMut;
import yvs.parametrage.agence.ManagedAgence;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedSaisiePiece extends Managed<PiecesCompta, YvsComptaPiecesComptable> implements Serializable {

    @ManagedProperty(value = "#{piecesCompta}")
    private PiecesCompta pieceCompta;
    private ContentPieceCompta content = new ContentPieceCompta();
    private ContentAnalytique contentAnal = new ContentAnalytique();
    private ContentModeleSaisie contentModel = null;
    private List<YvsComptaPiecesComptable> listePiece, piecesSelect;
    private YvsComptaPiecesComptable selectPiece;
    private YvsComptaContentJournal selectContent = new YvsComptaContentJournal();
    private YvsComptaContentAnalytique selectContentAnal;
    private YvsBasePlanComptable selectCompte;

    public PaginatorResult<YvsComptaContentJournal> paginatorContenu = new PaginatorResult<>();

    private List<YvsAgences> agencesSelect;
    private boolean actionAgence = true, updatePiece = false;
    private Date datePiece = new Date();
    private long journalPiece, journalPiece2;

    private String moisSaisie, operation, newLettre;
    private boolean initForm = true;
    private List<YvsComptaContentJournal> lettrages;
    private List<YvsComptaContentJournal> contenus, contenusLettrer;
    public PaginatorResult<YvsComptaContentJournal> p_contenu = new PaginatorResult<>();

    //Comptabilisation par piece
    private long piece;
    private Date dateDebut = new Date(), dateFin = new Date();
    private String naturePiece = Constantes.SCR_SALAIRE, typePiece = Constantes.SCR_AUTRES;
    private String typeLettrage = "M";//M= Montant D=Document
    private List<yvs.util.Options> naturesPieces = new ArrayList<yvs.util.Options>() {
        {
            add(new yvs.util.Options(Constantes.SCR_SALAIRE, Constantes.SCR_SALAIRE_NAME));
            add(new yvs.util.Options(Constantes.SCR_VENTE, Constantes.SCR_VENTE_NAME));
            add(new yvs.util.Options(Constantes.SCR_AVOIR_VENTE, Constantes.SCR_AVOIR_VENTE_NAME));
            add(new yvs.util.Options(Constantes.SCR_ACHAT, Constantes.SCR_ACHAT_NAME));
            add(new yvs.util.Options(Constantes.SCR_AVOIR_ACHAT, Constantes.SCR_AVOIR_ACHAT_NAME));
            add(new yvs.util.Options(Constantes.SCR_DIVERS, Constantes.SCR_DIVERS_NAME));
            add(new yvs.util.Options(Constantes.SCR_RETENUE, Constantes.SCR_RETENUE_NAME));
        }
    };
    private YvsGrhOrdreCalculSalaire selectSalaire;
    private YvsComEnteteDocVente selectHeadVente;
    private YvsComptaCaisseDocDivers selectDivers;
    private YvsComDocVentes selectVente;
    private YvsComDocAchats selectAchat;
    private YvsGrhElementAdditionel selectRetenue;

    private YvsComptaAbonementDocDivers selectAbonnementDivers;

    private YvsComptaCaissePieceDivers selectCaisseDivers;
    private YvsComptaCaissePieceAchat selectCaisseAchat;
    private YvsComptaCaissePieceVente selectCaisseVente;
    private YvsComptaAcompteClient selectAcompteVente;
    private YvsComptaAcompteFournisseur selectAcompteAchat;
    private YvsComptaCreditClient selectCreditVente;
    private YvsComptaCreditFournisseur selectCreditAchat;
    private YvsComptaCaissePieceMission selectCaisseMission;
    private YvsComptaCaissePieceVirement selectCaisseVirement;

    private List<YvsStatExportEtat> exports;
    private String tabIds, tabIds_contenu, model, docIds, rowIds, groupBy = "C";

    private Tiers tiersLetter = new Tiers();

    private long exoDeLetter;
    private Tiers tiersDeLetter = new Tiers();
    private Comptes compteDeLetter = new Comptes();
    private Comptes compteChange = new Comptes();
    private Journaux journalChange = new Journaux();

    private String compteContenuSearch, tiersContenuSearch, lettrageContenuSearch, mouvementContenuSearch;
    private Double debitContenuSearch = null, creditContenuSearch = null;
    private Boolean lettrerContenuSearch = null;

    private long journalContentSearch, journalSearch;
    private String compteSearch, tiersSearch, numeroSearch, referenceSearch, lettrageSearch, mouvementSearch;
    private Date debutContentSearch = new Date(), finContentSearch = new Date(), debutSearch = new Date(), finSearch = new Date();
    private Date debutSaveSearch = new Date(), finSaveSearch = new Date();
    private Long exerciceSearch, agenceSearch;
    private Double montantSearch = null;
    private boolean dateContentSearch, dateSaveContentSearch, dateSearch, dateSaveSearch;
    private Boolean lettrerSearch = false, withLiaisonSearch;

    private String natureFind, refExtFind;
    long tempId = -10000;

    private boolean unComptabilisedPieceVente = true;

    private Date dateExtourne = new Date();
    private long caisseUpgrade;
    private ServiceComptabilite service;

    private Profil profilFilter;

    public ManagedSaisiePiece() {
        listePiece = new ArrayList<>();
        contenus = new ArrayList<>();
        lettrages = new ArrayList<>();
        piecesSelect = new ArrayList<>();
        exports = new ArrayList<>();
        agencesSelect = new ArrayList<>();
        contenusLettrer = new ArrayList<>();
        if (currentUser != null) {
            fonction.loadInfos(currentAgence.getSociete(), currentAgence, currentUser, currentDepot, currentPoint, currentExo);
        }
    }

    public String getMouvementContenuSearch() {
        return mouvementContenuSearch;
    }

    public void setMouvementContenuSearch(String mouvementContenuSearch) {
        this.mouvementContenuSearch = mouvementContenuSearch;
    }

    public String getMouvementSearch() {
        return mouvementSearch;
    }

    public void setMouvementSearch(String mouvementSearch) {
        this.mouvementSearch = mouvementSearch;
    }

    public Date getDatePiece() {
        return datePiece;
    }

    public void setDatePiece(Date datePiece) {
        this.datePiece = datePiece;
    }

    public long getJournalPiece() {
        return journalPiece;
    }

    public void setJournalPiece(long journalPiece) {
        this.journalPiece = journalPiece;
    }

    public long getJournalPiece2() {
        return journalPiece2;
    }

    public void setJournalPiece2(long journalPiece2) {
        this.journalPiece2 = journalPiece2;
    }

    public Double getMontantSearch() {
        return montantSearch;
    }

    public void setMontantSearch(Double montantSearch) {
        this.montantSearch = montantSearch;
    }

    public long getExoDeLetter() {
        return exoDeLetter;
    }

    public void setExoDeLetter(long exoDeLetter) {
        this.exoDeLetter = exoDeLetter;
    }

    public Tiers getTiersDeLetter() {
        return tiersDeLetter;
    }

    public void setTiersDeLetter(Tiers tiersDeLetter) {
        this.tiersDeLetter = tiersDeLetter;
    }

    public Comptes getCompteDeLetter() {
        return compteDeLetter;
    }

    public void setCompteDeLetter(Comptes compteDeLetter) {
        this.compteDeLetter = compteDeLetter;
    }

    public Comptes getCompteChange() {
        return compteChange;
    }

    public void setCompteChange(Comptes compteChange) {
        this.compteChange = compteChange;
    }

    public Journaux getJournalChange() {
        return journalChange;
    }

    public void setJournalChange(Journaux journalChange) {
        this.journalChange = journalChange;
    }

    public Date getDebutSaveSearch() {
        return debutSaveSearch;
    }

    public void setDebutSaveSearch(Date debutSaveSearch) {
        this.debutSaveSearch = debutSaveSearch;
    }

    public Date getFinSaveSearch() {
        return finSaveSearch;
    }

    public void setFinSaveSearch(Date finSaveSearch) {
        this.finSaveSearch = finSaveSearch;
    }

    public boolean isDateSaveSearch() {
        return dateSaveSearch;
    }

    public void setDateSaveSearch(boolean dateSaveSearch) {
        this.dateSaveSearch = dateSaveSearch;
    }

    public long getTempId() {
        return tempId;
    }

    public void setTempId(long tempId) {
        this.tempId = tempId;
    }

    public long getCaisseUpgrade() {
        return caisseUpgrade;
    }

    public void setCaisseUpgrade(long caisseUpgrade) {
        this.caisseUpgrade = caisseUpgrade;
    }

    public boolean isUnComptabilisedPieceVente() {
        return unComptabilisedPieceVente;
    }

    public void setUnComptabilisedPieceVente(boolean unComptabilisedPieceVente) {
        this.unComptabilisedPieceVente = unComptabilisedPieceVente;
    }

    public List<YvsComptaContentJournal> getContenusLettrer() {
        return contenusLettrer;
    }

    public void setContenusLettrer(List<YvsComptaContentJournal> contenusLettrer) {
        this.contenusLettrer = contenusLettrer;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getWithLiaisonSearch() {
        return withLiaisonSearch;
    }

    public void setWithLiaisonSearch(Boolean withLiaisonSearch) {
        this.withLiaisonSearch = withLiaisonSearch;
    }

    public boolean isActionAgence() {
        return actionAgence;
    }

    public void setActionAgence(boolean actionAgence) {
        this.actionAgence = actionAgence;
    }

    public List<YvsAgences> getAgencesSelect() {
        return agencesSelect;
    }

    public void setAgencesSelect(List<YvsAgences> agencesSelect) {
        this.agencesSelect = agencesSelect;
    }

    public YvsComptaAbonementDocDivers getSelectAbonnementDivers() {
        return selectAbonnementDivers;
    }

    public void setSelectAbonnementDivers(YvsComptaAbonementDocDivers selectAbonnementDivers) {
        this.selectAbonnementDivers = selectAbonnementDivers;
    }

    public Tiers getTiersLetter() {
        return tiersLetter;
    }

    public void setTiersLetter(Tiers tiersLetter) {
        this.tiersLetter = tiersLetter;
    }

    public String getNewLettre() {
        return newLettre;
    }

    public void setNewLettre(String newLettre) {
        this.newLettre = newLettre;
    }

    public List<YvsComptaContentJournal> getLettrages() {
        return lettrages;
    }

    public void setLettrages(List<YvsComptaContentJournal> lettrages) {
        this.lettrages = lettrages;
    }

    public String getRowIds() {
        return rowIds;
    }

    public void setRowIds(String rowIds) {
        this.rowIds = rowIds;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public Boolean getLettrerSearch() {
        return lettrerSearch;
    }

    public void setLettrerSearch(Boolean lettrerSearch) {
        this.lettrerSearch = lettrerSearch;
    }

    public String getLettrageSearch() {
        return lettrageSearch;
    }

    public void setLettrageSearch(String lettrageSearch) {
        this.lettrageSearch = lettrageSearch;
    }

    public String getReferenceSearch() {
        return referenceSearch;
    }

    public void setReferenceSearch(String referenceSearch) {
        this.referenceSearch = referenceSearch;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public long getJournalSearch() {
        return journalSearch;
    }

    public void setJournalSearch(long journalSearch) {
        this.journalSearch = journalSearch;
    }

    public String getNumeroSearch() {
        return numeroSearch;
    }

    public void setNumeroSearch(String numeroSearch) {
        this.numeroSearch = numeroSearch;
    }

    public Date getDebutSearch() {
        return debutSearch;
    }

    public void setDebutSearch(Date debutSearch) {
        this.debutSearch = debutSearch;
    }

    public Date getFinSearch() {
        return finSearch;
    }

    public void setFinSearch(Date finSearch) {
        this.finSearch = finSearch;
    }

    public boolean isDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(boolean dateSearch) {
        this.dateSearch = dateSearch;
    }

    public YvsComptaAcompteClient getSelectAcompteVente() {
        return selectAcompteVente;
    }

    public void setSelectAcompteVente(YvsComptaAcompteClient selectAcompteVente) {
        this.selectAcompteVente = selectAcompteVente;
    }

    public YvsComptaAcompteFournisseur getSelectAcompteAchat() {
        return selectAcompteAchat;
    }

    public void setSelectAcompteAchat(YvsComptaAcompteFournisseur selectAcompteAchat) {
        this.selectAcompteAchat = selectAcompteAchat;
    }

    public YvsComptaCreditClient getSelectCreditVente() {
        return selectCreditVente;
    }

    public void setSelectCreditVente(YvsComptaCreditClient selectCreditVente) {
        this.selectCreditVente = selectCreditVente;
    }

    public YvsComptaCreditFournisseur getSelectCreditAchat() {
        return selectCreditAchat;
    }

    public void setSelectCreditAchat(YvsComptaCreditFournisseur selectCreditAchat) {
        this.selectCreditAchat = selectCreditAchat;
    }

    public YvsComptaCaisseDocDivers getSelectDivers() {
        return selectDivers;
    }

    public void setSelectDivers(YvsComptaCaisseDocDivers selectDivers) {
        this.selectDivers = selectDivers;
    }

    public ContentAnalytique getContentAnal() {
        return contentAnal;
    }

    public void setContentAnal(ContentAnalytique contentAnal) {
        this.contentAnal = contentAnal;
    }

    public YvsComptaContentAnalytique getSelectContentAnal() {
        return selectContentAnal;
    }

    public void setSelectContentAnal(YvsComptaContentAnalytique selectContentAnal) {
        this.selectContentAnal = selectContentAnal;
    }

    public String getDocIds() {
        return docIds;
    }

    public void setDocIds(String docIds) {
        this.docIds = docIds;
    }

    public String getTiersSearch() {
        return tiersSearch;
    }

    public void setTiersSearch(String tiersSearch) {
        this.tiersSearch = tiersSearch;
    }

    public long getJournalContentSearch() {
        return journalContentSearch;
    }

    public void setJournalContentSearch(long journalContentSearch) {
        this.journalContentSearch = journalContentSearch;
    }

    public String getCompteSearch() {
        return compteSearch;
    }

    public void setCompteSearch(String compteSearch) {
        this.compteSearch = compteSearch;
    }

    public Date getDebutContentSearch() {
        return debutContentSearch;
    }

    public void setDebutContentSearch(Date debutContentSearch) {
        this.debutContentSearch = debutContentSearch;
    }

    public Date getFinContentSearch() {
        return finContentSearch;
    }

    public void setFinContentSearch(Date finContentSearch) {
        this.finContentSearch = finContentSearch;
    }

    public boolean isDateContentSearch() {
        return dateContentSearch;
    }

    public void setDateContentSearch(boolean dateContentSearch) {
        this.dateContentSearch = dateContentSearch;
    }

    public boolean isDateSaveContentSearch() {
        return dateSaveContentSearch;
    }

    public void setDateSaveContentSearch(boolean dateSaveContentSearch) {
        this.dateSaveContentSearch = dateSaveContentSearch;
    }

    public List<YvsComptaContentJournal> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComptaContentJournal> contenus) {
        this.contenus = contenus;
    }

    public PaginatorResult<YvsComptaContentJournal> getP_contenu() {
        return p_contenu;
    }

    public void setP_contenu(PaginatorResult<YvsComptaContentJournal> p_contenu) {
        this.p_contenu = p_contenu;
    }

    public List<YvsStatExportEtat> getExports() {
        return exports;
    }

    public void setExports(List<YvsStatExportEtat> exports) {
        this.exports = exports;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getTabIds_contenu() {
        return tabIds_contenu;
    }

    public void setTabIds_contenu(String tabIds_contenu) {
        this.tabIds_contenu = tabIds_contenu;
    }

    public List<YvsComptaPiecesComptable> getPiecesSelect() {
        return piecesSelect;
    }

    public void setPiecesSelect(List<YvsComptaPiecesComptable> piecesSelect) {
        this.piecesSelect = piecesSelect;
    }

    public YvsGrhElementAdditionel getSelectRetenue() {
        return selectRetenue;
    }

    public void setSelectRetenue(YvsGrhElementAdditionel selectRetenue) {
        this.selectRetenue = selectRetenue;
    }

    public YvsGrhOrdreCalculSalaire getSelectSalaire() {
        return selectSalaire;
    }

    public void setSelectSalaire(YvsGrhOrdreCalculSalaire selectSalaire) {
        this.selectSalaire = selectSalaire;
    }

    public YvsComEnteteDocVente getSelectHeadVente() {
        return selectHeadVente;
    }

    public void setSelectHeadVente(YvsComEnteteDocVente selectHeadVente) {
        this.selectHeadVente = selectHeadVente;
    }

    public YvsComDocVentes getSelectVente() {
        return selectVente;
    }

    public void setSelectVente(YvsComDocVentes selectVente) {
        this.selectVente = selectVente;
    }

    public YvsComDocAchats getSelectAchat() {
        return selectAchat;
    }

    public void setSelectAchat(YvsComDocAchats selectAchat) {
        this.selectAchat = selectAchat;
    }

    public YvsComptaCaissePieceDivers getSelectCaisseDivers() {
        return selectCaisseDivers;
    }

    public void setSelectCaisseDivers(YvsComptaCaissePieceDivers selectCaisseDivers) {
        this.selectCaisseDivers = selectCaisseDivers;
    }

    public YvsComptaCaissePieceAchat getSelectCaisseAchat() {
        return selectCaisseAchat;
    }

    public void setSelectCaisseAchat(YvsComptaCaissePieceAchat selectCaisseAchat) {
        this.selectCaisseAchat = selectCaisseAchat;
    }

    public YvsComptaCaissePieceVente getSelectCaisseVente() {
        return selectCaisseVente;
    }

    public void setSelectCaisseVente(YvsComptaCaissePieceVente selectCaisseVente) {
        this.selectCaisseVente = selectCaisseVente;
    }

    public YvsComptaCaissePieceMission getSelectCaisseMission() {
        return selectCaisseMission;
    }

    public void setSelectCaisseMission(YvsComptaCaissePieceMission selectCaisseMission) {
        this.selectCaisseMission = selectCaisseMission;
    }

    public YvsComptaCaissePieceVirement getSelectCaisseVirement() {
        return selectCaisseVirement;
    }

    public void setSelectCaisseVirement(YvsComptaCaissePieceVirement selectCaisseVirement) {
        this.selectCaisseVirement = selectCaisseVirement;
    }

    public long getPiece() {
        return piece;
    }

    public void setPiece(long piece) {
        this.piece = piece;
    }

    public String getNaturePiece() {
        return naturePiece;
    }

    public void setNaturePiece(String naturePiece) {
        this.naturePiece = naturePiece;
    }

    public String getTypePiece() {
        return typePiece;
    }

    public void setTypePiece(String typePiece) {
        this.typePiece = typePiece;
    }

    public List<yvs.util.Options> getNaturesPieces() {
        return naturesPieces;
    }

    public void setNaturesPieces(List<yvs.util.Options> naturesPieces) {
        this.naturesPieces = naturesPieces;
    }

    public ContentModeleSaisie getContentModel() {
        return contentModel;
    }

    public void setContentModel(ContentModeleSaisie contentModel) {
        this.contentModel = contentModel;
    }

    public String getMoisSaisie() {
        return moisSaisie;
    }

    public void setMoisSaisie(String moisSaisie) {
        this.moisSaisie = moisSaisie;
    }

    public PiecesCompta getPieceCompta() {
        return pieceCompta;
    }

    public void setPieceCompta(PiecesCompta pieceCompta) {
        this.pieceCompta = pieceCompta;
    }

    public List<YvsComptaPiecesComptable> getListePiece() {
        return listePiece;
    }

    public void setListePiece(List<YvsComptaPiecesComptable> listePiece) {
        this.listePiece = listePiece;
    }

    public ContentPieceCompta getContent() {
        return content;
    }

    public void setContent(ContentPieceCompta content) {
        this.content = content;
    }

    public YvsComptaPiecesComptable getSelectPiece() {
        return selectPiece;
    }

    public void setSelectPiece(YvsComptaPiecesComptable selectPiece) {
        this.selectPiece = selectPiece;
    }

    public YvsComptaContentJournal getSelectContent() {
        return selectContent;
    }

    public void setSelectContent(YvsComptaContentJournal selectContent) {
        this.selectContent = selectContent;
    }

    public boolean isInitForm() {
        return initForm;
    }

    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }

    public PaginatorResult<YvsComptaContentJournal> getPaginatorContenu() {
        return paginatorContenu;
    }

    public void setPaginatorContenu(PaginatorResult<YvsComptaContentJournal> paginatorContenu) {
        this.paginatorContenu = paginatorContenu;
    }

    public String getCompteContenuSearch() {
        return compteContenuSearch;
    }

    public void setCompteContenuSearch(String compteContenuSearch) {
        this.compteContenuSearch = compteContenuSearch;
    }

    public String getTiersContenuSearch() {
        return tiersContenuSearch;
    }

    public void setTiersContenuSearch(String tiersContenuSearch) {
        this.tiersContenuSearch = tiersContenuSearch;
    }

    public String getLettrageContenuSearch() {
        return lettrageContenuSearch;
    }

    public void setLettrageContenuSearch(String lettrageContenuSearch) {
        this.lettrageContenuSearch = lettrageContenuSearch;
    }

    public Double getDebitContenuSearch() {
        return debitContenuSearch;
    }

    public void setDebitContenuSearch(Double debitContenuSearch) {
        this.debitContenuSearch = debitContenuSearch;
    }

    public Double getCreditContenuSearch() {
        return creditContenuSearch;
    }

    public void setCreditContenuSearch(Double creditContenuSearch) {
        this.creditContenuSearch = creditContenuSearch;
    }

    public Boolean getLettrerContenuSearch() {
        return lettrerContenuSearch;
    }

    public void setLettrerContenuSearch(Boolean lettrerContenuSearch) {
        this.lettrerContenuSearch = lettrerContenuSearch;
    }

    public Long getExerciceSearch() {
        return exerciceSearch;
    }

    public void setExerciceSearch(Long exerciceSearch) {
        this.exerciceSearch = exerciceSearch;
    }

    public Long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(Long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public String getNatureFind() {
        return natureFind;
    }

    public void setNatureFind(String natureFind) {
        this.natureFind = natureFind;
    }

    public String getRefExtFind() {
        return refExtFind;
    }

    public void setRefExtFind(String refExtFind) {
        this.refExtFind = refExtFind;
    }

    public Date getDateExtourne() {
        return dateExtourne;
    }

    public void setDateExtourne(Date dateExtourne) {
        this.dateExtourne = dateExtourne;
    }

    public String getTypeLettrage() {
        return typeLettrage;
    }

    public void setTypeLettrage(String typeLettrage) {
        this.typeLettrage = typeLettrage;
    }

    @Override
    public void doNothing() {
        ManagedAgence w = (ManagedAgence) giveManagedBean(ManagedAgence.class);
        if (w != null ? w.getListAgence().isEmpty() : false) {
            w.loadAgence();
        }
        service = new ServiceComptabilite(currentNiveau, currentUser, (w != null ? w.getListAgence() : null), dao);
    }

    @Override
    public void loadAll() {
        loadAll(null);
    }

    public void initView(String operation) {
        this.operation = operation;
        ManagedTiers wt = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (wt != null) {
            wt.setManagedBean(this);
        }
        ManagedCompte wc = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (wc != null) {
            wc.setManagedBean(this);
        }
        //initialise l'agence
        agenceSearch = currentAgence.getId();
    }

    public void loadAll(String operation) {
        this.operation = operation;
        if (moisSaisie != null ? moisSaisie.trim().length() < 1 : true) {
            moisSaisie = Constantes.tabMois[0];
        }
        initForm = true;
//        loadAllPiece(true);
//        loadAllContenu(true, true);
        model = null;
    }

    public void clearParams() {
        paginator.getParams().clear();
        loadAllPiece(true);
    }

    private void loadAllPiece(boolean avancer) {
        loadAllPiece(avancer, false);
    }

    private void loadAllPiece(boolean avancer, boolean diagnostic) {
        if (!diagnostic) {
            paginator.addParam(new ParametreRequete("y.piece.id", "desequilibrer", null, "IN", "AND"));
        }
        ParametreRequete p = new ParametreRequete("y.piece.journal.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
        paginator.addParam(p);
//        listePiece = paginator.executeDynamicQuery("YvsComptaPiecesComptable", "y.exercice.dateFin DESC, y.datePiece DESC", avancer, initForm, (int) imax, dao);
        listePiece = paginator.executeDynamicQuery("DISTINCT(y.piece)", "DISTINCT(y.piece)", "YvsComptaContentJournal y", "y.piece.exercice.dateFin DESC , y.piece.datePiece DESC", avancer, initForm, (int) imax, dao);
        if (listePiece.size() == 1) {
            onSelectObject(listePiece.get(0));
            execute("collapseForm('saisieJ')");
        } else {
            execute("collapseList('saisieJ')");
        }
        update("table_pc_comptable");
    }

    public void clearParamsContenuForPiece() {
        compteContenuSearch = "";
        tiersContenuSearch = "";
        lettrageContenuSearch = "";
        lettrerContenuSearch = null;
        mouvementContenuSearch = null;
        debitContenuSearch = null;
        creditContenuSearch = null;
        paginatorContenu.getParams().clear();
        loadAllContenuForPiece(true, true);
    }

    public void loadAllContenuForPiece(boolean avancer, boolean init) {
        List<YvsComptaContentJournal> lists = new ArrayList<>();
        if (selectPiece != null ? selectPiece.getId() > 0 : false) {
            ParametreRequete p = new ParametreRequete("y.piece", "piece", selectPiece, "=", "AND");
            paginatorContenu.addParam(p);
            lists = paginatorContenu.executeDynamicQuery("YvsComptaContentJournal", "y.jour", avancer, init, dao);
            Collections.sort(lists);
        }
        pieceCompta.setContentsPieces(lists);
        update("table_content_Pcomptable");
    }

    public void clearParamsContenu() {
        dateContentSearch = false;
        journalContentSearch = 0;
        mouvementSearch = "";
        compteSearch = "";
        tiersSearch = "";
        referenceSearch = "";
        lettrageSearch = "";
        lettrerSearch = null;
        p_contenu.getParams().clear();
        if (operation != null ? "L".equals(operation) : false) {
            contenus.clear();
            update("data_contenu_lettrage");
        } else {
            loadAllContenu(true, true);
        }
        update("zone-search_contenu_journal");
    }

    public void loadAllContenu(boolean avancer, boolean init) {
        ParametreRequete p = new ParametreRequete("y.piece.journal.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
        p_contenu.addParam(p);
        if (Util.asLong(agenceSearch)) {
            p = new ParametreRequete("y.piece.journal.agence", "agence", currentAgence, "=", "AND");
            p_contenu.addParam(p);
        } else {
            p_contenu.addParam(new ParametreRequete("y.piece.journal.agence", "agence", null, "=", "AND"));
        }
        boolean addOrderLettre = lettrerSearch != null ? lettrerSearch : false;
        String orderBy = (addOrderLettre ? "y.lettrage, " : "") + "y.numPiece";
        contenus = p_contenu.executeDynamicQuery("YvsComptaContentJournal", orderBy, avancer, init, (int) imax, dao);
        if (contenus.size() == 1) {
            onSelectObject(contenus.get(0).getPiece());
            execute("collapseForm('saisieJ')");
        } else {
            execute("collapseGrid('saisieJ')");
        }
        update("data_contenu_piece");
    }

    public void loadAllLettrage(boolean avancer, boolean init) {
        int idx;
        if ("C".equals(groupBy)) {
            idx = p_contenu.getParams().indexOf(new ParametreRequete("compte"));
            if (idx < 0) {
                return;
            }
        }
        if ("T".equals(groupBy)) {
            idx = p_contenu.getParams().indexOf(new ParametreRequete("tiers"));
            if (idx < 0) {
                return;
            }
        }
        idx = p_contenu.getParams().indexOf(new ParametreRequete("exercice"));
        if (idx < 0) {
            return;
        }
        loadAllContenu(avancer, init);
        update("data_contenu_lettrage");
    }

    public void init(boolean next) {
        initForm = false;
        loadAllPiece(next);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComptaPiecesComptable> re = paginator.parcoursDynamicData("YvsComptaPiecesComptable", "y", "y.datePiece DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        initForm = true;
        loadAllPiece(true);
    }

    public void choosePaginatorContenu(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllContenu(true, true);
    }

    public void choosePaginatorContenuForPiece(ValueChangeEvent ev) {
        paginatorContenu.choosePaginator(ev);
        loadAllContenuForPiece(true, true);
    }

    private YvsComptaPiecesComptable buildPieceCompta(PiecesCompta p) {
        YvsComptaPiecesComptable pc = UtilCompta.buildPieceCompta(p, currentUser);
        ManagedJournaux service = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
        if (service != null) {
            int idx = service.getJournaux().indexOf(pc.getJournal());
            if (idx >= 0) {
                pc.setJournal(service.getJournaux().get(idx));
            }
        }
        pc.setContentsPiece(p.getContentsPieces());
        return pc;
    }

    private YvsComptaContentJournal buildContentPiece(ContentPieceCompta c) {
        YvsComptaContentJournal y = UtilCompta.buildContentPiece(c, currentUser);
        y.setAnalytiques(new ArrayList<>(c.getAnalytiques()));
        return y;
    }

    private YvsComptaContentAnalytique buildContentAnalytique(ContentPieceCompta c, YvsComptaAffectationGenAnal a) {
        c.setIdX(c.getIdX() - 1);
        YvsComptaContentAnalytique r = new YvsComptaContentAnalytique(-c.getIdX());
        r.setCentre(a.getCentre());
        r.setCoefficient(a.getCoefficient());
        r.setContenu(new YvsComptaContentJournal(c.getId()));
        r.setDateSaisie(c.getDate());
        if (c.getCredit() != 0) {
            r.setCredit(c.getCredit() * a.getCoefficient() / 100);
        }
        if (c.getDebit() != 0) {
            r.setDebit(c.getDebit() * a.getCoefficient() / 100);
        }
        r.setAuthor(currentUser);
        r.setDateSave(new Date());
        r.setDateUpdate(new Date());
        return r;
    }

    private YvsComptaContentJournal buildContenJournal(long id, YvsComptaContentModeleSaisi cm, YvsComptaContentJournal c) {
        YvsComptaContentJournal r = new YvsComptaContentJournal(id);
        YvsComptaContentJournal l = null;
        if (pieceCompta.getContentsPieces() != null ? !pieceCompta.getContentsPieces().isEmpty() : false) {
            l = pieceCompta.getContentsPieces().get(pieceCompta.getContentsPieces().size() - 1);
        }
        //1-affectation du jour
        switch (cm.getModeSaisieJour()) {
            case 1: //saisie manuelle
                r.setJour(cm.getJour());
                break;
            case 2: //répétition
                if (c != null ? c.getId() != null : false) {
                    r.setJour(c.getJour());
                } else {
                    if (l != null ? l.getId() != null : false) {
                        r.setJour(l.getJour());
                    }
                }
                break;
            default:
                r.setJour(0);
                break;
        }
        //2-Affectation du numéro de pièce
        switch (cm.getMdsNumPiece()) {
            case 1: //saisie manuelle
                r.setNumPiece(cm.getNumPiece());
                break;
            case 2: //répétition
                if (c != null ? c.getId() != null : false) {
                    r.setNumPiece(c.getNumPiece());
                } else {
                    if (l != null ? l.getId() != null : false) {
                        r.setNumPiece(l.getNumPiece());
                    }
                }
                break;
            case 3: //généré par le système
                String str = genererReference(Constantes.TYPE_PIECE_COMPTABLE_NAME, pieceCompta.getDatePiece());
                r.setNumPiece(str);
                break;
            default:
                r.setNumPiece(null);
                break;
        }
        //3-Affectation du numéro de référence
        int idx = pieceCompta.getContentsPieces().indexOf(new YvsComptaContentJournal(r.getId()));
        if (idx < 0) {
            idx = pieceCompta.getContentsPieces().size();
        }
        String f = getReferenceContent(r.getNumPiece(), idx + 1);
        if (r.getNumRef() != null ? r.getNumRef().equals(f) : false) {
            f = r.getNumRef();
        }
        r.setNumRef(f);
        //4- Affectation du numéro de compte
        switch (cm.getMdsCompteGeneral()) {
            case 1: //saisie manuelle
                YvsBasePlanComptable compte = new YvsBasePlanComptable(cm.getCompteGeneral());
                r.setCompteGeneral(compte);
                break;
            case 2: //répétition
                if (c != null ? c.getId() != null : false) {
                    r.setCompteGeneral(c.getCompteGeneral());
                } else {
                    if (l != null ? l.getId() != null : false) {
                        r.setCompteGeneral(l.getCompteGeneral());
                    }
                }
                break;
            default:
                r.setCompteGeneral(null);
                break;
        }
        //5- Affectation du numéro de compte tiers
        switch (cm.getMdsCompteTiers()) {
            case 1: //saisie manuelle
                r.setCompteTiers(Long.valueOf(cm.getCompteTiers()));
                break;
            case 2: //répétition
                if (c != null ? c.getId() != null : false) {
                    r.setCompteTiers(c.getCompteTiers());
                } else {
                    if (l != null ? l.getId() != null : false) {
                        r.setCompteTiers(l.getCompteTiers());
                    }
                }
                break;
            default:
                r.setCompteTiers(null);
                break;
        }
        //5- Affectation du libellé
        switch (cm.getMdsLibelle()) {
            case 1: //saisie manuelle
                r.setLibelle(cm.getLibelle());
                break;
            case 2: //répétition
                if (c != null ? c.getId() != null : false) {
                    r.setLibelle(c.getLibelle());
                } else {
                    if (l != null ? l.getId() != null : false) {
                        r.setLibelle(l.getLibelle());
                    }
                }
                break;
            default:
                r.setLibelle(null);
                break;
        }
        //5- Affectation du debit
        switch (cm.getMdsDebit()) {
            case 1: //saisie manuelle
                r.setDebit(0.0);
                break;
            case 2: //répétition
                if (c != null ? c.getId() != null : false) {
                    r.setDebit(c.getDebit());
                } else {
                    if (l != null ? l.getId() != null : false) {
                        r.setDebit(l.getDebit());
                    }
                }
                break;
            case 3: //valeur calculé
                break;
            case 4: //equilibre
                r.setDebit(equilibrePiece(pieceCompta));
                break;
            default:
                break;
        }
        //5- Affectation du crédit
        switch (cm.getMdsCredit()) {
            case 1: //saisie manuelle
                r.setCredit(0.0);
                break;
            case 2: //répétition
                if (c != null ? c.getId() != null : false) {
                    r.setCredit(c.getCredit());
                } else {
                    if (l != null ? l.getId() != null : false) {
                        r.setCredit(l.getCredit());
                    }
                }
                break;
            case 3: //valeur calculé
                break;
            case 4: //equilibre
                r.setCredit(equilibrePiece(pieceCompta));
                break;
            default:
                break;
        }
        //5- Affectation de l'écheancier
        switch (cm.getMdsEcheance()) {
            case 1: //saisie manuelle
                r.setEcheance(cm.getEcheance());
                break;
            case 2: //répétition
                if (c != null ? c.getId() != null : false) {
                    r.setEcheance(c.getEcheance());
                } else {
                    if (l != null ? l.getId() != null : false) {
                        r.setEcheance(l.getEcheance());
                    }
                }
                break;
            default:
                r.setEcheance(null);
                break;
        }
        return r;
    }

    @Override
    public boolean controleFiche(PiecesCompta bean) {
        if (pieceCompta.getJournal().getId() <= 0) {
            getErrorMessage("Vous n'avez selectionné aucun journal");
            return false;
        }
        if (pieceCompta.getDatePiece() == null) {
            getErrorMessage("Vous devez entrer la date");
            return false;
        }
        if (bean.getContentsPieces().isEmpty() || bean.getNumPiece() == null) {
            String numRef = genererReference(Constantes.TYPE_PIECE_COMPTABLE_NAME, pieceCompta.getDatePiece(), pieceCompta.getJournal().getId(), Constantes.JOURNAL);
            if (numRef == null || numRef.trim().length() < 1) {
                return false;
            }
            pieceCompta.setNumPiece(numRef);
        }
        if (bean.isAutomatique()) {
            getErrorMessage("Vous ne pouvez pas modifier cette pièce.. car elle a été compabilisée automatiquement!");
            return false;
        }
        if (bean.getExercice() == null ? true : bean.getExercice().getId() <= 0) {
            getErrorMessage("Aucun exercices actif n'a été trouvé à cette date de saisie");
            return false;
        }
        if (bean.getExercice().isCloture()) {
            getErrorMessage("L'exercice en cours est déjà clôturé !!");
            return false;
        }
        return true;
    }

    private boolean controleContent(ContentPieceCompta c) {
        if (c.getCompteG() != null ? c.getCompteG().getId() < 1 : true) {
            getErrorMessage("Vous devez préciser le numéro de compte !");
            return false;
        }
        if (c.getCompteG().isSaisieCompteTiers()) {
            if (c.getCompteT() != null ? c.getCompteT().getSelectProfil().getId() < 1 : true) {
                getErrorMessage("Vous devez préciser le compte tiers !");
                return false;
            }
        }
        if (c.getCompteG().isSaisieEcheance()) {
            if (c.getEcheance() == null) {
                getErrorMessage("Vous devez entrer l'echéance !");
                return false;
            }
        }
        if ((c.getDebit() != 0 && c.getCredit() != 0) || (c.getCredit() == 0 && c.getDebit() == 0)) {
            getErrorMessage("Les valeurs débit / Crédit sont incorrectes !");
            return false;
        }
        if (c.getRefExterne() > 0) {
            getErrorMessage("Vous ne pouvez pas modifier cet élément.. car il a été généré automatiquement!");
            return false;
        }
        return true;
    }

    private boolean controleContent(ContentAnalytique bean) {
        if (bean.getContenu() == null) {
            getErrorMessage("Vous devez préciser la ligne du contenu journal !");
            return false;
        }
        if (bean.getCentre() != null ? bean.getCentre().getId() < 1 : true) {
            getErrorMessage("Vous devez préciser le centre analytique !");
            return false;
        }
        if (selectContent.getId() > 0) {
            if ((bean.getDebit() != 0 && bean.getCredit() != 0) || (bean.getCredit() == 0 && bean.getDebit() == 0)) {
                getErrorMessage("Les valeurs débit / Crédit sont incorrectes !");
                return false;
            }
        }
        if (bean.getCredit() != 0) {
            bean.setCoefficient(content.getCredit() != 0 ? (bean.getCredit() / content.getCredit()) * 100 : 0);
        }
        if (bean.getDebit() != 0) {
            bean.setCoefficient(content.getDebit() != 0 ? (bean.getDebit() / content.getDebit()) * 100 : 0);
        }
        double taux = 0;
        for (YvsComptaContentAnalytique a : content.getAnalytiques()) {
            if (!a.getId().equals(bean.getId())) {
                taux += a.getCoefficient();
            }
            if (!a.getId().equals(bean.getId()) && a.getCentre().equals(new YvsComptaCentreAnalytique(bean.getCentre().getId()))) {
                getErrorMessage("Vous avez déjà associé ce centre");
                return false;
            }
        }
        if (taux + bean.getCoefficient() > 100) {
            getErrorMessage("Vous ne pouvez pas definir ce montant car la somme depasse le montant du contenu");
            return false;
        }
        return true;
    }

    @Override
    public PiecesCompta recopieView() {
//        if (operation != null ? !"M".equals(operation) : false) {

//        }
        pieceCompta.setDateSaise(pieceCompta.getDateSaise() != null ? pieceCompta.getDateSaise() : new Date());
        pieceCompta.setMois(pieceCompta.getMois() != null ? pieceCompta.getMois() : new Date());
        return pieceCompta;
    }

    public ContentPieceCompta recopieViewContent(PiecesCompta piece, boolean generated) {
        pieceCompta.setExercice(UtilMut.buildBeanExercice(giveExerciceActif(piece.getDatePiece())));
        int idx = piece.getContentsPieces().indexOf(new YvsComptaContentJournal(content.getId()));
        if (idx < 0) {
            idx = piece.getContentsPieces().size();
        }
        String numero = content.getNumRef();
        if (Util.asString(content.getNumPiece())) {
            numero = getReferenceContent(content.getNumPiece(), idx + 1);
            if (content.getNumRef() != null ? content.getNumRef().equals(numero) : false) {
                numero = content.getNumRef();
            }
        }
        content.setNumRef(numero);
        content.setPiece(piece);
        return content;
    }

    @Override
    public void populateView(PiecesCompta bean) {
        cloneObject(pieceCompta, bean);
        Calendar c = Calendar.getInstance();
        if (bean.getDatePiece() != null) {
            c.setTime(bean.getDatePiece());
        }
        moisSaisie = Constantes.tabMois[c.get(Calendar.MONTH)];
        if (bean.getJournal() != null) {
            _giveSoldeJournal(bean.getJournal().getId());
        }
        loadAllContenuForPiece(true, true);
        resetFicheContent();
        update("cancel_saisieJ");
        update("save_saisieJ");
        update("zone_edit_piece_compta");
        update("table_content_Pcomptable");
    }

    public void populateView(ContentPieceCompta bean) {
        cloneObject(content, bean);
        //charge le tiers
        ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (w != null) {
            content.setCompteT(w.buildTiersByProfil(bean.getCompteTiers(), bean.getTableTiers()));
        }
        update("zone_edit_piece_compta");
    }

    @Override
    public void resetFiche() {
        resetFiche(pieceCompta);
        pieceCompta.setExercice(new Exercice());
        pieceCompta.setJournal(new Journaux());
        pieceCompta.getContentsPieces().clear();
        selectPiece = null;
        resetFicheContent();
        tabIds = "";
        update("cancel_saisieJ");
        update("save_saisieJ");
        update("table_content_Pcomptable");
    }

    public void resetFicheContent() {
        content = new ContentPieceCompta();
        selectContent = null;
        if (pieceCompta.getContentsPieces() != null ? !pieceCompta.getContentsPieces().isEmpty() : false) {
            YvsComptaContentJournal y = pieceCompta.getContentsPieces().get(0);
            content.setEcheance(y.getEcheance());
            content.setNumPiece(y.getNumPiece());
            content.setJour(y.getJour());
        }
        piece = 0;
        selectAchat = null;
        selectCaisseAchat = null;
        selectCaisseDivers = null;
        selectCaisseMission = null;
        selectCaisseVente = null;
        selectCaisseVirement = null;
        selectHeadVente = null;
        selectSalaire = null;
        selectVente = null;
        docIds = "";

        resetPartialFicheContent();
        update("zone_edit_piece_compta");
    }

    private void resetPartialFicheContent() {
        content.setId(tempId++);
        content.setCompteG(new Comptes());
        content.setCompteT(new Tiers());
        double solde = 0;
        for (YvsComptaContentJournal y : pieceCompta.getContentsPieces()) {
            solde += y.getSolde();
        }
        content.setCredit(solde > 0 ? solde : 0);
        content.setDebit(solde < 0 ? -solde : 0);
        content.setNumRef("");
        content.setLettrage("");
        content.getAnalytiques().clear();

        selectContent = new YvsComptaContentJournal();
        contentAnal = new ContentAnalytique();
        update("zone_edit_piece_compta");
        update("data_affectation");
    }

    @Override
    public boolean saveNew() {
        String action = pieceCompta.getId() > 0 ? "Modification" : "Insertion";
        try {
            PiecesCompta bean = recopieView();
            if (!bean.getContentsPieces().isEmpty()) {
                if (piece < 1) {
                    if (controleFiche(bean)) {
                        YvsComptaPiecesComptable y = buildPieceCompta(bean);
                        y = savePieceComptable(y);
                        int idx = listePiece.indexOf(y);
                        if (idx > -1) {
                            listePiece.set(idx, y);
                        } else {
                            listePiece.add(0, y);
                        }
                        resetFiche();
                        succes();
                        return true;
                    }
                } else {
                    boolean succes = false;
                    switch (naturePiece) {
                        case Constantes.SCR_RETENUE:
                            succes = comptabiliserRetenue(bean.getContentsPieces(), true);
                            break;
                        case Constantes.SCR_ACOMPTE_ACHAT:
                            succes = comptabiliserAcompteFournisseur(bean.getContentsPieces(), true);
                            break;
                        case Constantes.SCR_CREDIT_ACHAT:
                            succes = comptabiliserCreditFournisseur(bean.getContentsPieces(), true);
                            break;
                        case Constantes.SCR_ACOMPTE_VENTE:
                            succes = comptabiliserAcompteClient(bean.getContentsPieces(), true);
                            break;
                        case Constantes.SCR_CREDIT_VENTE:
                            succes = comptabiliserCreditClient(bean.getContentsPieces(), true);
                            break;
                        case Constantes.SCR_VIREMENT:
                            succes = comptabiliserCaisseVirement(bean.getContentsPieces(), true);
                            break;
                        case Constantes.SCR_FRAIS_MISSIONS:
                            succes = comptabiliserCaisseMission(bean.getContentsPieces(), true);
                            break;
                        case Constantes.SCR_NOTE_FRAIS:
//                            succes = comptabiliserCaisseVirement(selectCaisseVirement, false, bean.getContentsPieces());
                            break;
                        case Constantes.SCR_CAISSE_VENTE:
                            succes = comptabiliserCaisseVente(bean.getContentsPieces(), true);
                            break;
                        case Constantes.SCR_CAISSE_ACHAT:
                            succes = comptabiliserCaisseAchat(bean.getContentsPieces(), true);
                            break;
                        case Constantes.SCR_CAISSE_DIVERS:
                            succes = comptabiliserCaisseDivers(bean.getContentsPieces(), true);
                            break;
                        case Constantes.SCR_SALAIRE:
                            succes = comptabiliserSalaire(bean.getContentsPieces(), true);
                            break;
                        case Constantes.SCR_HEAD_VENTE:
                            succes = comptabiliserHeaderVente(bean.getContentsPieces(), true);
                            break;
                        case Constantes.SCR_AVOIR_VENTE:
                        case Constantes.SCR_VENTE:
                            succes = comptabiliserVente(bean.getContentsPieces(), true);
                            break;
                        case Constantes.SCR_AVOIR_ACHAT:
                        case Constantes.SCR_ACHAT:
                            succes = comptabiliserAchat(bean.getContentsPieces(), true);
                            break;
                        case Constantes.SCR_DIVERS:
                            succes = comptabiliserDivers(bean.getContentsPieces(), true);
                            break;
                        default:
                            break;
                    }
                    if (succes) {
                        resetFiche();
                        succes();
                    }
                }
            } else {
                getErrorMessage("Aucune pièce n'a été trouvé !");
            }
            return false;
        } catch (Exception ex) {
            getException(action + " impossible", ex);
            return false;
        }
    }

    public void updateNew() {
        try {
            updatePiece = true;
            ManagedJournaux w = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
            YvsComptaJournaux jrn = null;
            if (w != null) {
                int index = w.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                if (index > -1) {
                    jrn = w.getJournaux().get(index);
                }
            }
            switch (naturePiece) {
                case Constantes.SCR_ACOMPTE_ACHAT:
                    if (selectAcompteAchat != null) {
                        selectAcompteAchat.setDatePaiement(datePiece);
                        selectAcompteAchat.setJournal(jrn);
                    }
                    break;
                case Constantes.SCR_CREDIT_ACHAT:
                    if (selectCreditAchat != null) {
                        selectCreditAchat.setDateCredit(datePiece);
                        selectCreditAchat.setJournal(jrn);
                    }
                    break;
                case Constantes.SCR_ACOMPTE_VENTE:
                    if (selectAcompteVente != null) {
                        selectAcompteVente.setDatePaiement(datePiece);
                        selectAcompteVente.setJournal(jrn);
                    }
                    break;
                case Constantes.SCR_CREDIT_VENTE:
                    if (selectCreditVente != null) {
                        selectCreditVente.setDateCredit(datePiece);
                        selectCreditVente.setJournal(jrn);
                    }
                    break;
                case Constantes.SCR_VIREMENT:
                    if (selectCaisseVirement != null) {
                        selectCaisseVirement.setDatePaiement(datePiece);
                        selectCaisseVirement.setJournalSource(jrn);
                        if (w != null) {
                            int index = w.getJournaux().indexOf(new YvsComptaJournaux(journalPiece2));
                            if (index > -1) {
                                selectCaisseVirement.setJournalCible(w.getJournaux().get(index));
                            }
                        }
                    }
                    break;
                case Constantes.SCR_FRAIS_MISSIONS:
                    if (selectCaisseMission != null) {
                        selectCaisseMission.setDatePaiement(datePiece);
                        selectCaisseMission.setJournal(jrn);
                    }
                    break;
                case Constantes.SCR_NOTE_FRAIS:
//                            succes = comptabiliserCaisseVirement(selectCaisseVirement, false, bean.getContentsPieces());
                    break;
                case Constantes.SCR_CAISSE_VENTE:
                    if (selectCaisseVente != null) {
                        selectCaisseVente.setDatePaiement(datePiece);
                        selectCaisseVente.setJournal(jrn);
                    }
                    break;
                case Constantes.SCR_CAISSE_ACHAT:
                    if (selectCaisseAchat != null) {
                        selectCaisseAchat.setDatePaiement(datePiece);
                        selectCaisseAchat.setJournal(jrn);
                    }
                    break;
                case Constantes.SCR_CAISSE_DIVERS:
                    if (selectCaisseDivers != null) {
                        selectCaisseDivers.setDateValider(datePiece);
                        selectCaisseDivers.setJournal(jrn);
                    }
                    break;
                case Constantes.SCR_SALAIRE:
                    if (selectSalaire != null) {
                        selectSalaire.setDateDebutTraitement(datePiece);
                        selectSalaire.setJournal(jrn);
                    }
                    break;
                case Constantes.SCR_HEAD_VENTE:
                    if (selectHeadVente != null) {
                        selectHeadVente.setDateEntete(datePiece);
                        selectHeadVente.setJournal(jrn);
                    }
                    break;
                case Constantes.SCR_AVOIR_VENTE:
                case Constantes.SCR_VENTE:
                    if (selectVente != null) {
                        selectVente.getEnteteDoc().setDateEntete(datePiece);
                        selectVente.setJournal(jrn);
                    }
                    break;
                case Constantes.SCR_AVOIR_ACHAT:
                case Constantes.SCR_ACHAT:
                    if (selectAchat != null) {
                        selectAchat.setDateDoc(datePiece);
                        selectAchat.setJournal(jrn);
                    }
                    break;
                case Constantes.SCR_DIVERS:
                    if (selectDivers != null) {
                        selectDivers.setDateValider(datePiece);
                        selectDivers.setJournal(jrn);
                    }
                    break;
                default:
                    break;
            }
            saveNew();
            updatePiece = false;
            datePiece = null;
            journalPiece = 0;
            journalPiece2 = 0;
        } catch (Exception ex) {
            getException("updateNew", ex);
        }
    }

    public void updatePiece() {
        try {
            ManagedJournaux w = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
            if (w != null ? w.getJournaux().isEmpty() : false) {
                w.loadAllJournaux();
            }
            switch (naturePiece) {
                case Constantes.SCR_ACOMPTE_ACHAT:
                    if (selectAcompteAchat != null) {
                        datePiece = selectAcompteAchat.getDatePaiement();
                        journalPiece = selectAcompteAchat.getCaisse() != null ? selectAcompteAchat.getCaisse().getJournal() != null ? selectAcompteAchat.getCaisse().getJournal().getId() : 0 : 0;
                    }
                    break;
                case Constantes.SCR_CREDIT_ACHAT:
                    if (selectCreditAchat != null) {
                        datePiece = selectCreditAchat.getDateCredit();
                        journalPiece = selectCreditAchat.getJournal() != null ? selectCreditAchat.getJournal().getId() : 0;
                    }
                    break;
                case Constantes.SCR_ACOMPTE_VENTE:
                    if (selectAcompteVente != null) {
                        datePiece = selectAcompteVente.getDatePaiement();
                        journalPiece = selectAcompteVente.getCaisse() != null ? selectAcompteVente.getCaisse().getJournal() != null ? selectAcompteVente.getCaisse().getJournal().getId() : 0 : 0;
                    }
                    break;
                case Constantes.SCR_CREDIT_VENTE:
                    if (selectCreditVente != null) {
                        datePiece = selectCreditVente.getDateCredit();
                        journalPiece = selectCreditVente.getJournal() != null ? selectCreditVente.getJournal().getId() : 0;
                    }
                    break;
                case Constantes.SCR_VIREMENT:
                    if (selectCaisseVirement != null) {
                        datePiece = selectCaisseVirement.getDatePaiement();
                        journalPiece = selectCaisseVirement.getSource() != null ? selectCaisseVirement.getSource().getJournal() != null ? selectCaisseVirement.getSource().getJournal().getId() : 0 : 0;
                        journalPiece2 = selectCaisseVirement.getCible() != null ? selectCaisseVirement.getCible().getJournal() != null ? selectCaisseVirement.getCible().getJournal().getId() : 0 : 0;
                    }
                    break;
                case Constantes.SCR_FRAIS_MISSIONS:
                    if (selectCaisseMission != null) {
                        datePiece = selectCaisseMission.getDatePaiement();
                        journalPiece = selectCaisseMission.getCaisse() != null ? selectCaisseMission.getCaisse().getJournal() != null ? selectCaisseMission.getCaisse().getJournal().getId() : 0 : 0;
                    }
                    break;
                case Constantes.SCR_NOTE_FRAIS:
//                            succes = comptabiliserCaisseVirement(selectCaisseVirement, false, bean.getContentsPieces());
                    break;
                case Constantes.SCR_CAISSE_VENTE:
                    if (selectCaisseVente != null) {
                        datePiece = selectCaisseVente.getDatePaiement();
                        journalPiece = selectCaisseVente.getCaisse() != null ? selectCaisseVente.getCaisse().getJournal() != null ? selectCaisseVente.getCaisse().getJournal().getId() : 0 : 0;
                    }
                    break;
                case Constantes.SCR_CAISSE_ACHAT:
                    if (selectCaisseAchat != null) {
                        datePiece = selectCaisseAchat.getDatePaiement();
                        journalPiece = selectCaisseAchat.getCaisse() != null ? selectCaisseAchat.getCaisse().getJournal() != null ? selectCaisseAchat.getCaisse().getJournal().getId() : 0 : 0;
                    }
                    break;
                case Constantes.SCR_CAISSE_DIVERS:
                    if (selectCaisseDivers != null) {
                        datePiece = selectCaisseDivers.getDateValider();
                        journalPiece = selectCaisseDivers.getCaisse() != null ? selectCaisseDivers.getCaisse().getJournal() != null ? selectCaisseDivers.getCaisse().getJournal().getId() : 0 : 0;
                    }
                    break;
                case Constantes.SCR_SALAIRE:
                    if (selectSalaire != null) {
                        datePiece = selectSalaire.getDateDebutTraitement();
                        YvsComptaJournaux jrn = service.giveJournalSalaire(currentUser.getAgence());
                        journalPiece = jrn != null ? jrn.getId() : 0;
                    }
                    break;
                case Constantes.SCR_HEAD_VENTE:
                    if (selectHeadVente != null) {
                        datePiece = selectHeadVente.getDateEntete();
                        YvsComptaJournaux jrn = service.giveJournalVente(selectHeadVente.getAgence());
                        journalPiece = jrn != null ? jrn.getId() : 0;
                    }
                    break;
                case Constantes.SCR_AVOIR_VENTE:
                case Constantes.SCR_VENTE:
                    if (selectVente != null) {
                        datePiece = selectVente.getEnteteDoc().getDateEntete();
                        YvsComptaJournaux jrn = service.giveJournalVente(selectVente.getEnteteDoc().getAgence());
                        journalPiece = jrn != null ? jrn.getId() : 0;
                    }
                    break;
                case Constantes.SCR_AVOIR_ACHAT:
                case Constantes.SCR_ACHAT:
                    if (selectAchat != null) {
                        datePiece = selectAchat.getDateDoc();
                        YvsComptaJournaux jrn = service.giveJournalAchat(selectAchat.getAgence());
                        journalPiece = jrn != null ? jrn.getId() : 0;
                    }
                    break;
                case Constantes.SCR_DIVERS:
                    if (selectDivers != null) {
                        datePiece = selectDivers.getDateValider();
                        YvsComptaJournaux jrn = service.giveJournalDivers(selectDivers.getAgence());
                        journalPiece = jrn != null ? jrn.getId() : 0;
                    }
                    break;
                default:
                    break;
            }
            update("form-update_piece_comptable");
        } catch (Exception ex) {
            getException("updatePiece", ex);
        }
    }

    private YvsComptaPiecesComptable savePieceComptable(YvsComptaPiecesComptable pc) {
        List<YvsComptaContentJournal> contents = new ArrayList<>();
        contents.addAll(pc.getContentsPiece());
        pc.getContentsPiece().clear();
        if (pc.getId() > 0) {
            dao.update(pc);
        } else {
            pc.setId(null);
            pc = (YvsComptaPiecesComptable) dao.save1(pc);
        }
        List<YvsComptaContentAnalytique> analytiques;
        YvsComptaContentJournal c;
        for (int i = 0; i < contents.size(); i++) {
            c = contents.get(i);
            c.setPiece(pc);
            analytiques = new ArrayList<>();
            analytiques.addAll(c.getAnalytiques());
            c.getAnalytiques().clear();
            if (!Util.asString(c.getNumPiece())) {
                c.setNumPiece(pc.getNumPiece());
            }
            if (!Util.asString(c.getNumRef())) {
                c.setNumRef(pc.getNumPiece() + "-" + (i > 9 ? i : "0" + i));
            }
            if (c.getId() != null ? c.getId() > 0 : false) {
                dao.update(c);
            } else {
                c.setId(null);
                c = (YvsComptaContentJournal) dao.save1(c);
            }
            c.setAnalytiques(analytiques);
            saveAffectationLine(c);

            contents.set(i, c);
        }
        pc.getContentsPiece().addAll(contents);
        return pc;
    }

    public void print(YvsComptaPiecesComptable pc) {
        if (pc != null ? pc.getId() > 0 : false) {
            Map<String, Object> param = new HashMap<>();
            param.put("ID", pc.getId().intValue());
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("IMG_LOGO", returnLogo());
            param.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
            executeReport("piece_comptable", param);
        }
    }

    public YvsComptaPiecesComptable reBuildNumero(YvsComptaPiecesComptable y, boolean save, boolean msg) {
        if (y != null ? y.getId() > 0 : false) {
            String num = genererReference(Constantes.TYPE_PIECE_COMPTABLE_NAME, y.getDatePiece(), y.getJournal().getId());
            if (num != null ? num.trim().length() < 1 : true) {
                return y;
            }
            y.setNumPiece(num);
            y.setAuthor(currentUser);
            if (save) {
                dao.update(y);
            }
            if (msg) {
                succes();
            }
        }
        return y;
    }

    public void reBuildNumero() {
        List<Integer> index = decomposeSelection(tabIds);
        if (index != null ? !index.isEmpty() : false) {
            YvsComptaPiecesComptable y;
            for (Integer i : index) {
                if (listePiece.size() > i) {
                    y = reBuildNumero(listePiece.get(i), true, false);
                    listePiece.set(i, y);
                }
            }
            succes();
        } else {
            getErrorMessage("Vous devez selectionner des pièces");
        }
    }

    public void saveAllAffectation() {
        saveAffectationLine(selectContent);
        succes();
    }

    public void saveAffectationLine(YvsComptaContentJournal y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (y.getAnalytiques() != null ? !y.getAnalytiques().isEmpty() : false) {
                    List<YvsComptaContentAnalytique> analytiques = new ArrayList<>();
                    analytiques.addAll(y.getAnalytiques());
                    for (YvsComptaContentAnalytique c : analytiques) {
                        c.setContenu(y);
                        int idx = y.getAnalytiques().indexOf(c);
                        if (c.getId() > 0) {
                            dao.update(c);
                        } else {
                            c.setId(null);
                            c = (YvsComptaContentAnalytique) dao.save1(c);
                        }
                        y.getAnalytiques().set(idx, c);
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible!!!");
            getException("Error (saveAllAffectation) ", ex);
        }
    }

    public void blurCreditContent() {
        if (content.getAnalytiques() != null ? !content.getAnalytiques().isEmpty() : false) {
            for (YvsComptaContentAnalytique y : content.getAnalytiques()) {
                if (content.getCredit() == 0) {
                    y.setCredit(0.0);
                } else {
                    y.setCredit(content.getCredit() * (y.getCoefficient() / 100));
                }
            }
        }
    }

    public void blurDebitContent() {
        if (content.getAnalytiques() != null ? !content.getAnalytiques().isEmpty() : false) {
            for (YvsComptaContentAnalytique y : content.getAnalytiques()) {
                if (content.getDebit() == 0) {
                    y.setDebit(0.0);
                } else {
                    y.setDebit(content.getDebit() * (y.getCoefficient() / 100));
                }
            }
        }
    }

    public void eclaterAffectation() {
        List<YvsComptaAffectationGenAnal> list = dao.loadNameQueries("YvsComptaAffectationGenAnal.findByCompte", new String[]{"compte"}, new Object[]{new YvsBasePlanComptable(content.getCompteG().getId())});
        for (YvsComptaAffectationGenAnal a : list) {
            boolean add = true;
            for (YvsComptaContentAnalytique y : content.getAnalytiques()) {
                if (a.getCentre().equals(y.getCentre())) {
                    add = false;
                    break;
                }
            }
            if (add) {
                content.getAnalytiques().add(buildContentAnalytique(content, a));
            }
        }
    }

    public void addNewContentPiece() {
        ContentPieceCompta bean = recopieViewContent(pieceCompta, (pieceCompta.getModel() != null ? pieceCompta.getModel().getId() > 0 : false));
        int idx = pieceCompta.getContentsPieces().indexOf(new YvsComptaContentJournal(bean.getId()));
        if (idx < 0 && (pieceCompta.getModel() != null ? pieceCompta.getModel().getId() > 0 : false)) {
            getErrorMessage("Vous ne pouvez pas ajouter un élèment... car cette pièce est générée par un model");
            return;
        }
        if (controleContent(content)) {
            if (controleFiche(pieceCompta)) {
                pieceCompta.setStatutPiece(pieceCompta.getId() > 0 ? pieceCompta.getStatutPiece() : Constantes.STATUT_DOC_EDITABLE);
                YvsComptaContentJournal y = buildContentPiece(bean);
                if (idx > -1) {
                    pieceCompta.getContentsPieces().set(idx, y);
                } else {
                    pieceCompta.getContentsPieces().add(y);
                }
                resetPartialFicheContent();
            }
        }
    }

    public void addNewContentAnalytique() {
        contentAnal.setContenu(new ContentPieceCompta(selectContent.getId()));
        if (controleContent(contentAnal)) {
            YvsComptaContentAnalytique y = UtilCompta.buildContentPiece(contentAnal, currentUser);
            if (y.getId() == 0) {
                selectContent.setIdX(selectContent.getIdX() - 1);
                y.setId(-selectContent.getIdX());
            }
            int idx = selectContent.getAnalytiques().indexOf(y);
            if (idx < 0) {
                selectContent.getAnalytiques().add(y);
            } else {
                selectContent.getAnalytiques().set(idx, y);
            }
            idx = content.getAnalytiques().indexOf(y);
            if (idx < 0) {
                content.getAnalytiques().add(y);
            } else {
                content.getAnalytiques().set(idx, y);
            }
            int idy = pieceCompta.getContentsPieces().indexOf(selectContent);
            if (idy > -1) {
                idx = pieceCompta.getContentsPieces().get(idy).getAnalytiques().indexOf(y);
                if (idx < 0) {
                    pieceCompta.getContentsPieces().get(idy).getAnalytiques().add(y);
                } else {
                    pieceCompta.getContentsPieces().get(idy).getAnalytiques().set(idx, y);
                }
            }
            contentAnal = new ContentAnalytique();
            if (content.getCredit() > 0) {
                contentAnal.setCredit(content.getCredit() - selectContent.getTotalRepartitionCredit());
            } else {
                contentAnal.setDebit(content.getCredit() - selectContent.getTotalRepartitionDebit());
            }
            selectContentAnal = new YvsComptaContentAnalytique();
        }
    }

    public void updateContentByModel(YvsComptaContentJournal y) {
        if (pieceCompta.getModel() != null ? pieceCompta.getModel().getId() > 0 : false) {
            int idx = pieceCompta.getContentsPieces().indexOf(y);
            if (idx < pieceCompta.getContentsPieces().size() - 1 && pieceCompta.getModel().getContenus().size() > (idx + 1)) {
                long id = pieceCompta.getContentsPieces().get(idx + 1).getId();
                YvsComptaContentModeleSaisi c = pieceCompta.getModel().getContenus().get(idx + 1);
                YvsComptaContentJournal s = buildContenJournal(id, c, y);
                idx = pieceCompta.getContentsPieces().indexOf(s);
                if (idx > -1) {
                    pieceCompta.getContentsPieces().set(idx, s);
                } else {
                    pieceCompta.getContentsPieces().add(s);
                }
            }
        }
    }

    public void deleteSelection() {
        List<Integer> re = decomposeSelection(tabIds);
        List<YvsComptaPiecesComptable> l = new ArrayList<>();
        YvsComptaPiecesComptable p;
        for (int i : re) {
            p = listePiece.get(i);
            if (p.canDelete()) {
                dao.delete(p);
                l.add(p);
            }
        }
        listePiece.removeAll(l);
        tabIds = "";
        update("table_pc_comptable");
        succes();
    }

    @Override
    public void deleteBean() {
        try {
            if (selectPiece != null ? selectPiece.getId() != null : false) {
                if (selectPiece.canDelete()) {
                    dao.delete(selectPiece);
                    int idx = listePiece.indexOf(selectPiece);
                    if (idx > -1) {
                        listePiece.remove(idx);
                        update("table_pc_comptable");
                    }
                    if (selectPiece.getId().equals(pieceCompta.getId())) {
                        resetFiche();
                    }
                    succes();
                } else {
                    getErrorMessage("Vous ne pouves pas supprimer cette pièce");
                }
            }
        } catch (Exception ex) {
            getException("Suppression impossible", ex);
        }
    }

    public void deleteBeanContent() {
        try {
            if (pieceCompta != null) {
                if (pieceCompta.canEditable()) {
                    if (selectContent != null ? selectContent.getId() != null : false) {
                        if (selectContent.getRefExterne() > 0) {
                            getErrorMessage("Vous ne pouvez pas supprimer cet élément.. car il a été généré automatiquement!");
                            return;
                        }
                        dao.delete(selectContent);
                        int idx = pieceCompta.getContentsPieces().indexOf(selectContent);
                        if (idx > -1) {
                            pieceCompta.getContentsPieces().remove(idx);
                            update("table_content_Pcomptable");
                        }
                        int i = listePiece.indexOf(new YvsComptaPiecesComptable(pieceCompta.getId()));
                        if (i > -1) {
                            idx = listePiece.get(i).getContentsPiece().indexOf(selectContent);
                            if (idx > -1) {
                                listePiece.get(i).getContentsPiece().remove(idx);
                            }
                        }
                        if (selectContent.getId().equals(content.getId())) {
                            resetPartialFicheContent();
                        }
                        succes();
                    }
                } else {
                    getErrorMessage("Vous ne pouves pas modifier cette pièce");
                }
            }
        } catch (Exception ex) {
            getException("Suppression impossible", ex);
        }
    }

    public void deleteBeanAffectation(YvsComptaContentAnalytique y) {
        try {
            if (y != null) {
                if (y.getId() > 0) {
                    dao.delete(y);
                }
                selectContent.getAnalytiques().remove(y);
                content.getAnalytiques().remove(y);
                if (y.getId().equals(contentAnal.getId())) {
                    contentAnal = new ContentAnalytique();
                    selectContentAnal = new YvsComptaContentAnalytique();
                }
            }
        } catch (Exception ex) {
            getException("Suppression impossible", ex);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void openDialog(String widget) {
        super.openDialog(widget); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSelectObject(YvsComptaPiecesComptable y) {
        selectPiece = y;
        populateView(UtilCompta.buildBeanPieceCompta(y, dao));
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        YvsComptaPiecesComptable y = (YvsComptaPiecesComptable) ev.getObject();
        onSelectObject(y);
        tabIds = listePiece.indexOf(y) + "";
    }

    public void loadOnViewContenu(SelectEvent ev) {
        YvsComptaContentJournal y = (YvsComptaContentJournal) ev.getObject();
        if (y != null) {
            onSelectObject(y.getPiece());
        }
    }

    public void loadOnViewContent(SelectEvent ev) {
        selectContent = (YvsComptaContentJournal) ev.getObject();
        populateView(UtilCompta.buildBeanContentPiece(selectContent));
        contentModel = null;
        if (pieceCompta.getModel() != null ? pieceCompta.getModel().getId() > 0 : false) {
            int idx = pieceCompta.getContentsPieces().indexOf(selectContent);
            if (idx > -1 && pieceCompta.getModel().getContenus().size() > idx) {
                YvsComptaContentModeleSaisi c = pieceCompta.getModel().getContenus().get(idx);
                contentModel = UtilCompta.buildBeanContentModel(c);
            }
        }
    }

    public void loadOnViewModel(SelectEvent ev) {
        YvsComptaModeleSaisie model = (YvsComptaModeleSaisie) ev.getObject();
        if (pieceCompta.getContentsPieces() != null ? pieceCompta.getContentsPieces().isEmpty() : true) {
            pieceCompta.setModel(UtilCompta.buildBeanModel(model));
            generatedPiece(model);
        } else {
            openDialog("dlgConfirmDestroyContent");
        }
    }

    public void loadOnViewAffectation(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectContentAnal = (YvsComptaContentAnalytique) ev.getObject();
            contentAnal = UtilCompta.buildBeanContentPiece(selectContentAnal);
        }
    }

    public void unLoadOnViewAffectation(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            contentAnal = new ContentAnalytique();
            selectContentAnal = new YvsComptaContentAnalytique();
        }
    }

    public void loadContentJournal(YvsComptaContentJournal y) {
        selectContent = y;
        contentAnal = new ContentAnalytique();
        content = UtilCompta.buildBeanContentPiece(y);
        update("title_affectation_maj");
        update("data_affectation_maj");
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void unLoadOnViewContent(UnselectEvent ev) {
        resetPartialFicheContent();
    }

    public boolean controleContenu(YvsComptaPiecesComptable y) {
        Long count = (Long) dao.loadObjectByNameQueries("YvsComptaContentJournal.countByPiece", new String[]{"piece"}, new Object[]{y});
        if (count != null ? count > 0 : false) {
            return true;
        }
        return false;
    }

    public void chooseCompteG(SelectEvent ev) {
        YvsBasePlanComptable c = (YvsBasePlanComptable) ev.getObject();
        chooseCompteG(c);
        update("txt_zone_saisie_cg");
        update("txt_zone_saisie_ct");
        update("txt_saisie_echeance");
    }

    public void chooseCompteT(SelectEvent ev) {
        YvsBaseTiers t = (YvsBaseTiers) ev.getObject();
        Tiers tiers = UtilTiers.buildBeanTiers(t);
        if (tiers.getProfils().size() > 1) {
            //ouvre la boite de dialogue des profiles tiers                    
            openDialog("dlgProfilTiers");
            update("table_profils_tiers");
            cloneObject(content.getCompteT(), tiers);
            return;
        } else {
            if (tiers.getProfils().size() == 1) {
                cloneObject(content.getCompteT(), tiers);
                content.getCompteT().setSelectProfil(tiers.getProfils().get(0));
            } else {
                content.getCompteT().setError(true);
                content.getCompteT().setId(-1);
                content.getCompteT().setSelectProfil(new Profil());
                getWarningMessage("Aucun compte tiers n'a été trouvé avec ce code !");
            }
        }
        update("txt_zone_saisie_ct");
    }

    public void choisirProfils(SelectEvent ev) {
        if (ev != null) {
            Profil y = (Profil) ev.getObject();
            content.getCompteT().setSelectProfil(y);
            update("txt_zone_saisie_ct");
        }
    }

    public void blurCoefficientAffection() {
        if (content != null) {
            if (selectContent.getCredit() != 0) {
                contentAnal.setCredit(content.getCredit() * contentAnal.getCoefficient() / 100);
            }
            if (selectContent.getDebit() != 0) {
                contentAnal.setDebit(content.getDebit() * contentAnal.getCoefficient() / 100);
            }
        }
    }

    public double valeurAffectation() {
        double montant = 0;
        for (YvsComptaContentAnalytique a : selectContent.getAnalytiques()) {
            if (a.getDebit() != 0) {
                montant += a.getDebit();
            }
            if (a.getCredit() != 0) {
                montant += a.getCredit();
            }
        }
        return montant;
    }

    public void chooseCentre() {
        if (contentAnal.getCentre() != null ? contentAnal.getCentre().getId() > 0 : false) {
            ManagedCentreAnalytique w = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
            if (w != null) {
                int idx = w.getCentres().indexOf(new YvsComptaCentreAnalytique(contentAnal.getCentre().getId()));
                if (idx > -1) {
                    contentAnal.setCentre(UtilCompta.buildBeanCentreAnalytique(w.getCentres().get(idx)));
                }
            }
        }
    }

    public void chooseCompteG(YvsBasePlanComptable c) {
        content.setCompteG(UtilCompta.buildBeanCompte(c));
        if (c.getSaisieAnalytique()) {
            openDialog("dlgAffectation");
        }
    }

    /*Saisie des pièce*/
    public void ecouteSaisieCompteG(String numCompte) {
        ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (service != null) {
            service.findCompteByNum(numCompte);
            if (service.getListComptes().size() == 1) {
                chooseCompteG(service.getListComptes().get(0));
            } else if (service.getListComptes().size() > 1) {
                //ouvre la boite de dialogue des comptes
                openDialog("dlgCompteG");
                update("form_table_compteG_saisieJ");
            }
        }
    }

    public void ecouteSaisieCompteT(String numCompte) {
        ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (service != null) {
            service.findTiersByCode(numCompte);
            content.getCompteT().setError(service.getListTiers().isEmpty());
            content.getCompteT().setId(-1);
            content.getCompteT().setSelectProfil(new Profil());
            if (service.getListTiers() != null ? !service.getListTiers().isEmpty() : false) {
                if (service.getListTiers().size() == 1) {
                    YvsBaseTiers y = service.getListTiers().get(0);
                    Tiers tiers = UtilTiers.buildBeanTiers(y);
                    cloneObject(content.getCompteT(), tiers);
                    Profil current = new Profil(y.getId(), y.getCodeTiers(), y.getNom(), y.getPrenom(), y.getCompteCollectif(), yvs.dao.salaire.service.Constantes.BASE_TIERS_TIERS, y.getActif(), 3, y.getId(), yvs.dao.salaire.service.Constantes.BASE_TIERS_TIERS, y.getCodeTiers());
                    if (tiers.getProfils().size() > 1) {
                        tiers.getProfils().add(current);
                        //ouvre la boite de dialogue des profiles tiers
                        Profil p = findOneProfil(numCompte);
                        if (p == null) {
                            openDialog("dlgProfilTiers");
                            update("table_profils_tiers");
                        } else {
                            content.getCompteT().setSelectProfil(p);
                        }
                        return;
                    } else {
                        content.getCompteT().setSelectProfil(current);
                    }
                } else {
                    //ouvre la boite de dialogue des tiers
                    openDialog("dlgCompteT");
                    update("form_table_compteT_saisieJ");
                }
            }
        }
    }

    private Profil findOneProfil(String code) {
        return findOneProfil(content.getCompteT(), code);
    }

    private YvsBaseExercice giveExercice(Date date) {
        return (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByDate", new String[]{"societe", "date"}, new Object[]{currentAgence.getSociete(), date});
    }

    private YvsBaseExercice controleExercice(Date date, boolean msg) {
        YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByDate", new String[]{"societe", "date"}, new Object[]{currentAgence.getSociete(), date});
        if (exo != null ? exo.getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Comptabilisation impossible... car il n'existe pas d'exercice paramètré pour la date du " + formatDate.format(date));
            }
            return null;
        }
        if (!exo.getActif()) {
            if (msg) {
                getErrorMessage("Comptabilisation impossible... car l'exerice " + exo.getReference() + " n'est pas actif");
            }
            return null;
        }
        if (exo.getCloturer()) {
            if (msg) {
                getErrorMessage("Comptabilisation impossible... car l'exerice " + exo.getReference() + " est déjà cloturé");
            }
            return null;
        }
        //verifions la clôture de la période
        YvsMutPeriodeExercice periode = (YvsMutPeriodeExercice) dao.loadOneByNameQueries("YvsMutPeriodeExercice.findActifByDate", new String[]{"dateJour", "societe"}, new Object[]{date, currentAgence.getSociete()});
        if (periode != null ? periode.getId() > 0 : false) {
            if (periode.getCloture()) {
                getErrorMessage("Le document ne peut pas être enregistré dans une periode cloturée");
                return null;
            }
        }
        return exo;
    }

    public double giveSoePieces(List<YvsComptaContentJournal> contenus) {
        double re = 0, cr = 0, db = 0;
        if (contenus != null) {
            for (YvsComptaContentJournal cc : contenus) {
                db += cc.getDebit();
                cr += cc.getCredit();
            }
            re = db - cr;
        }
        return arrondi(re, 3);
    }

    public double giveSoePiece(PiecesCompta p) {
        double re, cr = 0, db = 0;
        for (YvsComptaContentJournal cc : p.getContentsPieces()) {
            db += cc.getDebit();
            cr += cc.getCredit();
        }
        re = db - cr;
        p.setCredits(cr);
        p.setDebits(db);
        p.setSolde(re);
        update("txt_solde_piece_compta");
        update("blog_form_montant_piece_compta");
        if (arrondi(re, 3) == 0) {
            switch (naturePiece) {
                case Constantes.SCR_CAISSE_ACHAT:
                    if (selectCaisseAchat != null) {
                        return selectCaisseAchat.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE) ? selectCaisseAchat.getMontant() < db ? -1 : 0 : 0;
                    }
                    break;
                case Constantes.SCR_CAISSE_VENTE:
                    if (selectCaisseVente != null) {
                        return selectCaisseVente.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE) ? selectCaisseVente.getMontant() < db ? -1 : 0 : 0;
                    }
                    break;
                case Constantes.SCR_ACOMPTE_ACHAT:
                    if (selectAcompteAchat != null) {
                        return selectAcompteAchat.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE) ? selectAcompteAchat.getMontant() < db ? -1 : 0 : 0;
                    }
                    break;
                case Constantes.SCR_ACOMPTE_VENTE:
                    if (selectAcompteVente != null) {
                        return selectAcompteVente.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE) ? selectAcompteVente.getMontant() < db ? -1 : 0 : 0;
                    }
                    break;
                default:
                    break;
            }
        }
        return arrondi(re, 3);
    }

    public void giveSoldeJournal(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                pieceCompta.getJournal().setId(id);
                _giveSoldeJournal(id);
                content.setId(tempId++);
            }
        }
    }

    public void _giveSoldeJournal(long id) {
        if (id > 0) {
            Double d = (Double) dao.loadObjectByNameQueries("YvsComptaContentJournal.findSoldeJournal", new String[]{"journal"}, new Object[]{new YvsComptaJournaux(id)});
            pieceCompta.getJournal().setSoldeJournal(((d != null) ? d : 0));
        }
    }
    /*End saisie pièce*/
    /*Générer les pièces de caisses à partir du modèles*/

    private void generatedPiece(YvsComptaModeleSaisie model) {
        model = (YvsComptaModeleSaisie) dao.loadOneByNameQueries("YvsComptaModeleSaisie.findById", new String[]{"id"}, new Object[]{model.getId()});
        if (model != null ? model.getId() > 0 : false) {
            if (controleFiche(pieceCompta)) {
                pieceCompta.setStatutPiece(pieceCompta.getId() > 0 ? pieceCompta.getStatutPiece() : Constantes.STATUT_DOC_EDITABLE);
                YvsComptaContentJournal cj;
                for (int i = 0; i < model.getContentsModel().size(); i++) {
                    YvsComptaContentModeleSaisi c = model.getContentsModel().get(i);
                    cj = buildContenJournal(-(i + 1), c, null);
                    pieceCompta.getContentsPieces().add(cj);
                }
                update("table_content_Pcomptable");
            }
        }
    }

    public void destroyModel() {
        if (pieceCompta.canEditable()) {
            pieceCompta.setModel(null);
        }
    }

    public void destroyContent() {
        if (pieceCompta != null) {
            if (pieceCompta.canEditable()) {
                for (YvsComptaContentJournal c : pieceCompta.getContentsPieces()) {
                    dao.delete(c);
                }
                pieceCompta.getContentsPieces().clear();
                generatedPiece(new YvsComptaModeleSaisie(pieceCompta.getModel().getId()));
            } else {
                getErrorMessage("Vous ne pouvez pas modifier cette pièce");
            }
        }

    }

//    private Date buildDate(int jour, Date mois) {
//        return new Date();
//    }
//
//    private Date buildDate(int jour, String mois) {
//        try {
//            int m = Util.getMonth(mois);
//            System.err.println("--- Mois "+m);
//            if (jour > 0) {
//                Calendar c = Calendar.getInstance();
//                c.set(Calendar.DAY_OF_MONTH, jour);
//                c.set(Calendar.MONTH, m);
//                return c.getTime();
//            }
//            return new Date();
//        } catch (Exception ex) {
//            getException("Convertion date impossible", ex);
//            return new Date();
//        }
//    }
    private double equilibrePiece(PiecesCompta p) {
        double re = 0;
        for (YvsComptaContentJournal c : p.getContentsPieces()) {
            re += (c.getSolde());
        }
        return Math.abs(re);
    }

    private double equilibrePiece(List<YvsComptaContentJournal> list) {
        double re = 0;
        for (YvsComptaContentJournal c : list) {
            re += (c.getSolde());
        }
        return Math.abs(re);
    }

    private String getReferenceContent(String num, int pos) {
        String c = pos < 10 ? ("-0" + pos) : ("-" + pos);
        String r = num + c;
        YvsComptaContentJournal y = (YvsComptaContentJournal) dao.loadOneByNameQueries("YvsComptaContentJournal.findByNumRef", new String[]{"numRef", "societe"}, new Object[]{r, currentAgence.getSociete()});
        if (y != null ? y.getId() < 1 : true) {
            return r;
        }
        for (int i = pos; i < 100; i++) {
            c = i < 10 ? ("-0" + i) : ("-" + i);
            r = num + c;
            y = (YvsComptaContentJournal) dao.loadOneByNameQueries("YvsComptaContentJournal.findByNumRef", new String[]{"numRef", "societe"}, new Object[]{r, currentAgence.getSociete()});
            if (y != null ? y.getId() < 1 : true) {
                return r;
            }
        }
        return r;
    }

    public boolean canEditCredit() {
        if (contentModel != null ? contentModel.getCredit() != null : false) {
            return contentModel.getCredit().getTypeValeur() > 0;
        }
        return true;
    }

    public boolean canEditDebit() {
        if (contentModel != null ? contentModel.getCredit() != null : false) {
            return contentModel.getDebit().getTypeValeur() > 0;
        }
        return true;
    }

    public boolean changeStatut(char etat) {
        if (changeStatut_(etat)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut_(char etat) {
        if (etat != ' ' && pieceCompta != null) {
            String rq = "UPDATE yvs_compta_pieces_comptable SET statut_piece = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(pieceCompta.getId(), 1)};
            dao.requeteLibre(rq, param);
            pieceCompta.setStatutPiece(etat);
            int idx = listePiece.indexOf(new YvsComptaPiecesComptable(pieceCompta.getId()));
            if (idx > -1) {
                listePiece.get(idx).setStatutPiece(etat);
                update("table_pc_comptable");
            }
            return true;
        }
        return false;
    }

    /*
     * BEGIN COMPTABILISER PIECE
     */
    public void onSelectDistantRetenue(YvsGrhElementAdditionel y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedRetenue s = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    public void onSelectDistantSalaire(YvsGrhOrdreCalculSalaire y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSalaire s = (ManagedSalaire) giveManagedBean(ManagedSalaire.class);
            if (s != null) {
                s.onSelectDistantOrdre(y);
            }
        }
    }

    public void onSelectDistantMission(YvsGrhMissions y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedMission s = (ManagedMission) giveManagedBean(ManagedMission.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    public void onSelectDistantPieceMission(YvsComptaCaissePieceMission y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedReglementMission s = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
            if (s != null) {
                s.onSelectDistant(y.getMission());
            }
        }
    }

    public void onSelectDistantDivers(YvsComptaCaisseDocDivers y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedDocDivers s = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    public void onSelectDistantPieceDivers(YvsComptaCaissePieceDivers y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedDocDivers s = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
            if (s != null) {
                s.onSelectDistant(y.getDocDivers());
            }
        }
    }

    public void onSelectDistantFactureAchat(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureAchat s = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    public void onSelectDistantPieceAchat(YvsComptaCaissePieceAchat y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureAchat s = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (s != null) {
                s.onSelectDistant(y.getAchat());
            }
        }
    }

    public void onSelectDistantVente(YvsComEnteteDocVente y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedVente s = (ManagedVente) giveManagedBean(ManagedVente.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    public void onSelectDistantFactureVente(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureVenteV2 s = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    public void onSelectDistantPieceVente(YvsComptaCaissePieceVente y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureVenteV2 s = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
            if (s != null) {
                s.onSelectDistant(y.getVente());
            }
        }
    }

    public void onSelectDistantAcompteVente(YvsComptaAcompteClient y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedOperationClient s = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    public void onSelectDistantCreditVente(YvsComptaCreditClient y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedOperationClient s = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    public void onSelectDistantAcompteAchat(YvsComptaAcompteFournisseur y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedOperationFournisseur s = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    public void onSelectDistantCreditAchat(YvsComptaCreditFournisseur y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedOperationFournisseur s = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    public void onSelectDistantVirement(YvsComptaCaissePieceVirement y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedVirement s = (ManagedVirement) giveManagedBean(ManagedVirement.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    public void loadOnViewRetenue(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsGrhElementAdditionel y = (YvsGrhElementAdditionel) ev.getObject();
            onSelectRetenue(y, true, true);
        }
    }

    private void onSelectRetenue(YvsGrhElementAdditionel y, boolean msg, boolean clear) {
        selectRetenue = y;
        if (selectRetenue != null ? (selectRetenue.getId() != null ? selectRetenue.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectRetenue.getId();
            List<YvsComptaContentJournal> list = buildRetenueToComptabilise(piece, msg);
            if (list != null) {
                if (!list.isEmpty()) {
                    double solde = giveSoePieces(list);
                    y.setErrorComptabilise(solde != 0);
                    pieceCompta.getContentsPieces().addAll(list);
                } else if (msg) {
                    getErrorMessage("Cet ordre de calcul des salaires n'a generé aucune ligne de comptabilité");
                }
            }
        }
    }

    public void loadOnViewSalaire(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsGrhOrdreCalculSalaire y = (YvsGrhOrdreCalculSalaire) ev.getObject();
            onSelectSalaire(y, true, true);
        }
    }

    private void onSelectSalaire(YvsGrhOrdreCalculSalaire y, boolean msg, boolean clear) {
        selectSalaire = y;
        if (selectSalaire != null ? (selectSalaire.getId() != null ? selectSalaire.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectSalaire.getId();
            List<YvsComptaContentJournal> list = buildSalaireToComptabilise(piece, msg);
            if (list != null) {
                if (!list.isEmpty()) {
                    double solde = giveSoePieces(list);
                    y.setErrorComptabilise(solde != 0);
                    pieceCompta.getContentsPieces().addAll(list);
                } else if (msg) {
                    getErrorMessage("Cet ordre de calcul des salaires n'a generé aucune ligne de comptabilité");
                }
            }
        }
    }

    public void loadOnViewMission(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCaissePieceMission y = (YvsComptaCaissePieceMission) ev.getObject();
            onSelectMission(y, true, true);
        }
    }

    private void onSelectMission(YvsComptaCaissePieceMission y, boolean msg, boolean clear) {
        selectCaisseMission = y;
        if (selectCaisseMission != null ? (selectCaisseMission.getId() != null ? selectCaisseMission.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectCaisseMission.getId();
            List<YvsComptaContentJournal> list = buildMissionToComptabilise(piece, msg);
            if (list != null) {
                if (!list.isEmpty()) {
                    double solde = giveSoePieces(list);
                    y.setErrorComptabilise(solde != 0);
                    pieceCompta.getContentsPieces().addAll(list);
                } else if (msg) {
                    getErrorMessage("Ces frais de missions n'a generé aucune ligne de comptabilité");
                }
            }
        }
    }

    public void loadOnViewHeadVente(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComEnteteDocVente y = (YvsComEnteteDocVente) ev.getObject();
            onSelectHeadVente(y, true, true);
        }
    }

    private void onSelectHeadVente(YvsComEnteteDocVente y, boolean msg, boolean clear) {
        selectHeadVente = y;
        if (selectHeadVente != null ? (selectHeadVente.getId() != null ? selectHeadVente.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectHeadVente.getId();
            List<YvsComptaContentJournal> list = buildHeaderVenteToComptabilise(piece, msg);
            if (list != null) {
                if (!list.isEmpty()) {
                    double solde = giveSoePieces(list);
                    y.setErrorComptabilise(solde != 0);
                    pieceCompta.getContentsPieces().addAll(list);
                } else if (msg) {
                    getErrorMessage("Ce journal de vente n'a generé aucune ligne de comptabilité");
                }
            }
        }
    }

    public void loadOnViewVente(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComDocVentes y = (YvsComDocVentes) ev.getObject();
            onSelectVente(y, true, true);
        }
    }

    private void onSelectVente(YvsComDocVentes y, boolean msg, boolean clear) {
        selectVente = y;
        if (selectVente != null ? (selectVente.getId() != null ? selectVente.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectVente.getId();
            List<YvsComptaContentJournal> list = buildVenteToComptabilise(piece, msg);
            if (list != null) {
                if (!list.isEmpty()) {
                    double solde = giveSoePieces(list);
                    y.setErrorComptabilise(solde != 0);
                    pieceCompta.getContentsPieces().addAll(list);
                } else if (msg) {
                    getErrorMessage("Cette facture de vente n'a generé aucune ligne de comptabilité");
                }
            }
        }
    }

    public void loadOnViewAchat(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComDocAchats y = (YvsComDocAchats) ev.getObject();
            onSelectAchat(y, true, true);
        }
    }

    private void onSelectAchat(YvsComDocAchats y, boolean msg, boolean clear) {
        selectAchat = y;
        if (selectAchat != null ? (selectAchat.getId() != null ? selectAchat.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectAchat.getId();
            List<YvsComptaContentJournal> list = buildAchatToComptabilise(piece, msg);
            if (list != null) {
                if (!list.isEmpty()) {
                    double solde = giveSoePieces(list);
                    y.setErrorComptabilise(solde != 0);
                    pieceCompta.getContentsPieces().addAll(list);
                } else if (msg) {
                    getErrorMessage("Cette facture d'achat n'a generé aucune ligne de comptabilité");
                }
            }
        }
    }

    public void loadOnViewVirement(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectVirement((YvsComptaCaissePieceVirement) ev.getObject(), true, true);
        }
    }

    private void onSelectVirement(YvsComptaCaissePieceVirement y, boolean msg, boolean clear) {
        selectCaisseVirement = y;
        if (selectCaisseVirement != null ? (selectCaisseVirement.getId() != null ? selectCaisseVirement.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectCaisseVirement.getId();
            List<YvsComptaContentJournal> list = buildCaisseVirementToComptabilise(piece, msg);
            if (list != null) {
                if (!list.isEmpty()) {
                    double solde = giveSoePieces(list);
                    y.setErrorComptabilise(solde != 0);
                    pieceCompta.getContentsPieces().addAll(list);
                } else if (msg) {
                    getErrorMessage("Ce virement n'a generé aucune ligne de comptabilité");
                }
            }
        }
    }

    public void loadOnViewDivers(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCaisseDocDivers y = (YvsComptaCaisseDocDivers) ev.getObject();
            onSelectDivers(y, true, true);
        }
    }

    public void onSelectDivers(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectDivers = (YvsComptaCaisseDocDivers) ev.getObject();
            selectDivers.setNew_(true);
        }
    }

    private void onSelectDivers(YvsComptaCaisseDocDivers y, boolean msg, boolean clear) {
        selectDivers = y;
        if (selectDivers != null ? (selectDivers.getId() != null ? selectDivers.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectDivers.getId();
            List<YvsComptaContentJournal> list = fonction.buildDiversToComptabilise(piece, dao);
            if (list != null) {
                if (!list.isEmpty()) {
                    double solde = giveSoePieces(list);
                    y.setErrorComptabilise(solde != 0);
                    pieceCompta.getContentsPieces().addAll(list);
                } else if (msg) {
                    getErrorMessage("Cette opération divers n'a generé aucune ligne de comptabilité");
                }
            } else {
                getErrorMessage(dao.getRESULT());
            }
        }
    }

    public void loadOnViewNoteFrais(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {

        }
    }

    public void loadOnViewCaisseDivers(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCaissePieceDivers y = (YvsComptaCaissePieceDivers) ev.getObject();
            onSelectCaisseDivers(y, true, true);
        }
    }

    private void onSelectCaisseDivers(YvsComptaCaissePieceDivers y, boolean msg, boolean clear) {
        selectCaisseDivers = y;
        if (selectCaisseDivers != null ? (selectCaisseDivers.getId() != null ? selectCaisseDivers.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectCaisseDivers.getId();
            List<YvsComptaContentJournal> list = buildCaisseDiversToComptabilise(piece, msg);
            if (list != null) {
                if (!list.isEmpty()) {
                    double solde = giveSoePieces(list);
                    y.setErrorComptabilise(solde != 0);
                    pieceCompta.getContentsPieces().addAll(list);
                } else if (msg) {
                    getErrorMessage("Ce reglement d'opération divers n'a generé aucune ligne de comptabilité");
                }
            }
        }
    }

    public void loadOnViewCaisseAchat(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCaissePieceAchat y = (YvsComptaCaissePieceAchat) ev.getObject();
            onSelectCaisseAchat(y, true, true);
        }
    }

    private void onSelectCaisseAchat(YvsComptaCaissePieceAchat y, boolean msg, boolean clear) {
        selectCaisseAchat = y;
        if (selectCaisseAchat != null ? (selectCaisseAchat.getId() != null ? selectCaisseAchat.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectCaisseAchat.getId();
            List<YvsComptaContentJournal> list = buildCaisseAchatToComptabilise(piece, msg);
            if (list != null) {
                if (!list.isEmpty()) {
                    double solde = giveSoePieces(list);
                    y.setErrorComptabilise(solde != 0);
                    pieceCompta.getContentsPieces().addAll(list);
                } else if (msg) {
                    getErrorMessage("Ce reglement d'achat n'a generé aucune ligne de comptabilité");
                }
            }
        }
    }

    public void loadOnViewCaisseVente(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCaissePieceVente y = (YvsComptaCaissePieceVente) ev.getObject();
            onSelectCaisseVente(y, true, true);
        }
    }

    private void onSelectCaisseVente(YvsComptaCaissePieceVente y, boolean msg, boolean clear) {
        selectCaisseVente = y;
        if (selectCaisseVente != null ? (selectCaisseVente.getId() != null ? selectCaisseVente.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectCaisseVente.getId();
            List<YvsComptaContentJournal> list = buildCaisseVenteToComptabilise(piece, msg);
            if (list != null ? !list.isEmpty() : false) {
                double solde = giveSoePieces(list);
                y.setErrorComptabilise(solde != 0);
                pieceCompta.getContentsPieces().addAll(list);
            } else if (msg) {
                getErrorMessage("Ce reglement de vente n'a generé aucune ligne de comptabilité");
            }
        }
    }

    public void loadOnViewAcompteVente(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaAcompteClient y = (YvsComptaAcompteClient) ev.getObject();
            if (!y.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Ce document n'est pas payé");
                return;
            }
            onSelectAcompteVente(y, true, true);
        }
    }

    private void onSelectAcompteVente(YvsComptaAcompteClient y, boolean msg, boolean clear) {
        selectAcompteVente = y;
        if (selectAcompteVente != null ? (selectAcompteVente.getId() != null ? selectAcompteVente.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectAcompteVente.getId();
            List<YvsComptaContentJournal> list = buildAcompteClientToComptabilise(piece, msg);
            if (list != null) {
                if (!list.isEmpty()) {
                    double solde = giveSoePieces(list);
                    y.setErrorComptabilise(solde != 0);
                    pieceCompta.getContentsPieces().addAll(list);
                } else if (msg) {
                    getErrorMessage("Cet acompte n'a generé aucune ligne de comptabilité");
                }
            }
        }
    }

    public void loadOnViewAcompteAchat(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaAcompteFournisseur y = (YvsComptaAcompteFournisseur) ev.getObject();
            if (!y.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Ce document n'est pas payé");
                return;
            }
            onSelectAcompteAchat(y, true, true);
        }
    }

    private void onSelectAcompteAchat(YvsComptaAcompteFournisseur y, boolean msg, boolean clear) {
        selectAcompteAchat = y;
        if (selectAcompteAchat != null ? (selectAcompteAchat.getId() != null ? selectAcompteAchat.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectAcompteAchat.getId();
            List<YvsComptaContentJournal> list = buildAcompteFournisseurToComptabilise(piece, msg);
            if (list != null) {
                if (!list.isEmpty()) {
                    double solde = giveSoePieces(list);
                    y.setErrorComptabilise(solde != 0);
                    pieceCompta.getContentsPieces().addAll(list);
                } else if (msg) {
                    getErrorMessage("Cet acompte n'a generé aucune ligne de comptabilité");
                }
            }
        }
    }

    public void loadOnViewCreditVente(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCreditClient y = (YvsComptaCreditClient) ev.getObject();
            onSelectCreditVente(y, true, true);
        }
    }

    private void onSelectCreditVente(YvsComptaCreditClient y, boolean msg, boolean clear) {
        selectCreditVente = y;
        if (selectCreditVente != null ? (selectCreditVente.getId() != null ? selectCreditVente.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectCreditVente.getId();
            List<YvsComptaContentJournal> list = buildCreditClientToComptabilise(piece, msg);
            if (list != null) {
                if (!list.isEmpty()) {
                    double solde = giveSoePieces(list);
                    y.setErrorComptabilise(solde != 0);
                    pieceCompta.getContentsPieces().addAll(list);
                } else if (msg) {
                    getErrorMessage("Ce crédit n'a generé aucune ligne de comptabilité");
                }
            }
        }
    }

    public void loadOnViewCreditAchat(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCreditFournisseur y = (YvsComptaCreditFournisseur) ev.getObject();
            onSelectCreditAchat(y, true, true);
        }
    }

    private void onSelectCreditAchat(YvsComptaCreditFournisseur y, boolean msg, boolean clear) {
        selectCreditAchat = y;
        if (selectCreditAchat != null ? (selectCreditAchat.getId() != null ? selectCreditAchat.getId() > 0 : false) : false) {
            if (clear) {
                pieceCompta.getContentsPieces().clear();
            }
            piece = selectCreditAchat.getId();
            List<YvsComptaContentJournal> list = buildCreditFournisseurToComptabilise(piece, msg);
            if (list != null) {
                if (!list.isEmpty()) {
                    double solde = giveSoePieces(list);
                    y.setErrorComptabilise(solde != 0);
                    pieceCompta.getContentsPieces().addAll(list);
                } else if (msg) {
                    getErrorMessage("Ce crédit n'a generé aucune ligne de comptabilité");
                }
            }
        }
    }

    public void loadMoreViewVirement() {
        ManagedVirement service = (ManagedVirement) giveManagedBean(ManagedVirement.class);
        if (service != null) {
            for (YvsComptaCaissePieceVirement pv : service.getListAllVirement()) {
                onSelectVirement(pv, false, true);
            }
        }
    }

    public void loadOnViewMore() {
        List<Integer> index = decomposeSelection(docIds);
        String ids = "0";
        if (index != null ? !index.isEmpty() : false) {
            switch (naturePiece) {
                case Constantes.SCR_RETENUE: {
                    ManagedRetenue w = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsGrhElementAdditionel y = w.getListRetenues().get(idx);
                            if (!y.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                                continue;
                            }
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                case Constantes.SCR_CREDIT_ACHAT: {
                    ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCreditFournisseur y = w.getCredits().get(idx);
                            if (!y.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                                continue;
                            }
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                case Constantes.SCR_CREDIT_VENTE: {
                    ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCreditClient y = w.getCredits().get(idx);
                            if (!y.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                                continue;
                            }
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                case Constantes.SCR_ACOMPTE_ACHAT: {
                    ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaAcompteFournisseur y = w.getAcomptes().get(idx);
                            if (!y.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                                continue;
                            }
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                case Constantes.SCR_ACOMPTE_VENTE: {
                    ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaAcompteClient y = w.getAcomptes().get(idx);
                            if (!y.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                                continue;
                            }
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                case Constantes.SCR_VIREMENT: {
                    ManagedVirement w = (ManagedVirement) giveManagedBean(ManagedVirement.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCaissePieceVirement y = w.getListAllVirement().get(idx);
                            if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                continue;
                            }
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                case Constantes.SCR_FRAIS_MISSIONS: {
                    ManagedReglementMission w = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCaissePieceMission y = w.getAvances().get(idx);
                            if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                continue;
                            }
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                case Constantes.SCR_NOTE_FRAIS: {
                    ManagedSalaire w = (ManagedSalaire) giveManagedBean(ManagedSalaire.class);
                    if (w != null) {
                        for (long id : index) {

                        }
                    }
                    break;
                }
                case Constantes.SCR_CAISSE_VENTE: {
                    ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCaissePieceVente y = w.getReglements().get(idx);
                            if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                continue;
                            }
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                case Constantes.SCR_CAISSE_ACHAT: {
                    ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCaissePieceAchat y = w.getPiecesCaisses().get(idx);
                            if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                continue;
                            }
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                case Constantes.SCR_CAISSE_DIVERS: {
                    ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCaissePieceDivers y = w.getPieces().get(idx);
                            if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                continue;
                            }
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                case Constantes.SCR_SALAIRE: {
                    ManagedSalaire w = (ManagedSalaire) giveManagedBean(ManagedSalaire.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsGrhOrdreCalculSalaire y = w.getListPlanification().get(idx);
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                case Constantes.SCR_HEAD_VENTE: {
                    ManagedVente w = (ManagedVente) giveManagedBean(ManagedVente.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComEnteteDocVente y = w.getDocuments().get(idx);
                            if (!y.getEtat().equals(Constantes.ETAT_VALIDE)) {
                                continue;
                            }
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                case Constantes.SCR_VENTE: {
                    ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComDocVentes y = w.getDocuments().get(idx);
                            if (!y.getStatut().equals(Constantes.ETAT_VALIDE)) {
                                continue;
                            }
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                case Constantes.SCR_ACHAT: {
                    ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComDocAchats y = w.getDocuments().get(idx);
                            if (!y.getStatut().equals(Constantes.ETAT_VALIDE)) {
                                continue;
                            }
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                case Constantes.SCR_DIVERS: {
                    ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCaisseDocDivers y = w.getDocuments().get(idx);
                            if (!y.getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                                continue;
                            }
                            piece = y.getId();
                            ids += "-" + y.getId();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
        List<YvsComptaContentJournal> list = buildContentJournal(ids, naturePiece, false);
        pieceCompta.setContentsPieces(list != null ? list : new ArrayList<YvsComptaContentJournal>());
    }

    public void unComptabiliseAll() {
        if (!autoriser("compta_od_annul_comptabilite")) {
            openNotAcces();
            return;
        }
        List<Integer> index = decomposeSelection(docIds);
        if (index != null ? !index.isEmpty() : false) {
            switch (naturePiece) {
                case Constantes.SCR_CREDIT_ACHAT: {
                    ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCreditFournisseur y = w.getCredits().get(idx);
                            unComptabiliserCreditFournisseur(y, true);
                        }
                    }
                    break;
                }
                case Constantes.SCR_CREDIT_VENTE: {
                    ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCreditClient y = w.getCredits().get(idx);
                            unComptabiliserCreditClient(y, true);
                        }
                    }
                    break;
                }
                case Constantes.SCR_ACOMPTE_ACHAT: {
                    ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaAcompteFournisseur y = w.getAcomptes().get(idx);
                            unComptabiliserAcompteFournisseur(y, true);
                        }
                    }
                    break;
                }
                case Constantes.SCR_ACOMPTE_VENTE: {
                    ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaAcompteClient y = w.getAcomptes().get(idx);
                            unComptabiliserAcompteClient(y, true);
                        }
                    }
                    break;
                }
                case Constantes.SCR_VIREMENT: {
                    ManagedVirement w = (ManagedVirement) giveManagedBean(ManagedVirement.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCaissePieceVirement y = w.getListAllVirement().get(idx);
                            unComptabiliserCaisseVirement(y, true);
                        }
                    }
                    break;
                }
                case Constantes.SCR_FRAIS_MISSIONS: {
                    ManagedReglementMission w = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCaissePieceMission y = w.getAvances().get(idx);
                            unComptabiliserCaisseMission(y, true);
                        }
                    }
                    break;
                }
                case Constantes.SCR_NOTE_FRAIS: {
                    ManagedSalaire w = (ManagedSalaire) giveManagedBean(ManagedSalaire.class);
                    if (w != null) {
                        for (long id : index) {

                        }
                    }
                    break;
                }
                case Constantes.SCR_CAISSE_VENTE: {
                    ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCaissePieceVente y = w.getReglements().get(idx);
                            unComptabiliserCaisseVente(y, true);
                        }
                    }
                    break;
                }
                case Constantes.SCR_CAISSE_ACHAT: {
                    ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCaissePieceAchat y = w.getPiecesCaisses().get(idx);
                            unComptabiliserCaisseAchat(y, true);
                        }
                    }
                    break;
                }
                case Constantes.SCR_CAISSE_DIVERS: {
                    ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCaissePieceDivers y = w.getPieces().get(idx);
                            unComptabiliserCaisseDivers(y, true);
                        }
                    }
                    break;
                }
                case Constantes.SCR_SALAIRE: {
                    ManagedSalaire w = (ManagedSalaire) giveManagedBean(ManagedSalaire.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsGrhOrdreCalculSalaire y = w.getListPlanification().get(idx);
                            unComptabiliserSalaire(y, true);
                        }
                    }
                    break;
                }
                case Constantes.SCR_HEAD_VENTE: {
                    ManagedVente w = (ManagedVente) giveManagedBean(ManagedVente.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComEnteteDocVente y = w.getDocuments().get(idx);
                            unComptabiliserHeaderVente(y, true);
                        }
                    }
                    break;
                }
                case Constantes.SCR_VENTE: {
                    ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComDocVentes y = w.getDocuments().get(idx);
                            unComptabiliserVente(y, true);
                        }
                    }
                    break;
                }
                case Constantes.SCR_ACHAT: {
                    ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComDocAchats y = w.getDocuments().get(idx);
                            unComptabiliserAchat(y, true);
                        }
                    }
                    break;
                }
                case Constantes.SCR_DIVERS: {
                    ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                    if (w != null) {
                        for (int idx : index) {
                            YvsComptaCaisseDocDivers y = w.getDocuments().get(idx);
                            unComptabiliserDivers(y, true);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
            succes();
        } else {
            getErrorMessage("Vous devez selectionner des documents");
        }
    }

    public void loadOnViewMore_OLD() {
        List<Integer> ids = decomposeSelection(docIds);
        if (ids != null ? !ids.isEmpty() : false) {
            boolean first = true;
            switch (naturePiece) {
                case Constantes.SCR_CREDIT_ACHAT: {
                    ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                    if (w != null) {
                        for (int idx : ids) {
                            YvsComptaCreditFournisseur y = w.getCredits().get(idx);
                            if (!y.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                                continue;
                            }
                            onSelectCreditAchat(y, false, first);
                            first = false;
                        }
                    }
                    break;
                }
                case Constantes.SCR_CREDIT_VENTE: {
                    ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                    if (w != null) {
                        for (int idx : ids) {
                            YvsComptaCreditClient y = w.getCredits().get(idx);
                            if (!y.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                                continue;
                            }
                            onSelectCreditVente(y, false, first);
                            first = false;
                        }
                    }
                    break;
                }
                case Constantes.SCR_ACOMPTE_ACHAT: {
                    ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                    if (w != null) {
                        for (int idx : ids) {
                            YvsComptaAcompteFournisseur y = w.getAcomptes().get(idx);
                            if (!y.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                                continue;
                            }
                            onSelectAcompteAchat(y, false, first);
                            first = false;
                        }
                    }
                    break;
                }
                case Constantes.SCR_ACOMPTE_VENTE: {
                    ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                    if (w != null) {
                        for (int idx : ids) {
                            YvsComptaAcompteClient y = w.getAcomptes().get(idx);
                            if (!y.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                                continue;
                            }
                            onSelectAcompteVente(y, false, first);
                            first = false;
                        }
                    }
                    break;
                }
                case Constantes.SCR_VIREMENT: {
                    ManagedVirement w = (ManagedVirement) giveManagedBean(ManagedVirement.class);
                    if (w != null) {
                        for (int idx : ids) {
                            YvsComptaCaissePieceVirement y = w.getListAllVirement().get(idx);
                            if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                continue;
                            }
                            onSelectVirement(y, false, first);
                            first = false;
                        }
                    }
                    break;
                }
                case Constantes.SCR_FRAIS_MISSIONS: {
                    ManagedReglementMission w = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
                    if (w != null) {
                        for (int idx : ids) {
                            YvsComptaCaissePieceMission y = w.getAvances().get(idx);
                            if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                continue;
                            }
                            onSelectMission(y, false, first);
                            first = false;
                        }
                    }
                    break;
                }
                case Constantes.SCR_NOTE_FRAIS: {
                    ManagedSalaire w = (ManagedSalaire) giveManagedBean(ManagedSalaire.class);
                    if (w != null) {
                        for (long id : ids) {

                        }
                    }
                    break;
                }
                case Constantes.SCR_CAISSE_VENTE: {
                    ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                    if (w != null) {
                        for (int idx : ids) {
                            YvsComptaCaissePieceVente y = w.getReglements().get(idx);
                            if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                continue;
                            }
                            onSelectCaisseVente(y, false, first);
                            first = false;
                        }
                    }
                    break;
                }
                case Constantes.SCR_CAISSE_ACHAT: {
                    ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                    if (w != null) {
                        for (int idx : ids) {
                            YvsComptaCaissePieceAchat y = w.getPiecesCaisses().get(idx);
                            if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                continue;
                            }
                            onSelectCaisseAchat(y, false, first);
                            first = false;
                        }
                    }
                    break;
                }
                case Constantes.SCR_CAISSE_DIVERS: {
                    ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                    if (w != null) {
                        for (int idx : ids) {
                            YvsComptaCaissePieceDivers y = w.getPieces().get(idx);
                            if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                continue;
                            }
                            onSelectCaisseDivers(y, false, first);
                            first = false;
                        }
                    }
                    break;
                }
                case Constantes.SCR_SALAIRE: {
                    ManagedSalaire w = (ManagedSalaire) giveManagedBean(ManagedSalaire.class);
                    if (w != null) {
                        for (int idx : ids) {
                            YvsGrhOrdreCalculSalaire y = w.getListPlanification().get(idx);
                            onSelectSalaire(y, false, first);
                            first = false;
                        }
                    }
                    break;
                }
                case Constantes.SCR_HEAD_VENTE: {
                    ManagedVente w = (ManagedVente) giveManagedBean(ManagedVente.class);
                    if (w != null) {
                        for (int idx : ids) {
                            YvsComEnteteDocVente y = w.getDocuments().get(idx);
                            if (!y.getEtat().equals(Constantes.ETAT_VALIDE)) {
                                continue;
                            }
                            onSelectHeadVente(y, false, first);
                            first = false;
                        }
                    }
                    break;
                }
                case Constantes.SCR_VENTE: {
                    ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (w != null) {
                        for (int idx : ids) {
                            YvsComDocVentes y = w.getDocuments().get(idx);
                            if (!y.getStatut().equals(Constantes.ETAT_VALIDE)) {
                                continue;
                            }
                            onSelectVente(y, false, first);
                            first = false;
                        }
                    }
                    break;
                }
                case Constantes.SCR_ACHAT: {
                    ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                    if (w != null) {
                        for (int idx : ids) {
                            YvsComDocAchats y = w.getDocuments().get(idx);
                            if (!y.getStatut().equals(Constantes.ETAT_VALIDE)) {
                                continue;
                            }
                            onSelectAchat(y, false, first);
                            first = false;
                        }
                    }
                    break;
                }
                case Constantes.SCR_DIVERS: {
                    ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                    if (w != null) {
                        for (int idx : ids) {
                            YvsComptaCaisseDocDivers y = w.getDocuments().get(idx);
                            if (!y.getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                                continue;
                            }
                            onSelectDivers(y, false, first);
                            first = false;
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }

    public void openDocuments() {
        docIds = "";
        openDialog("dlgListDocPiece");
    }

    public void chooseNaturePiece() {
        docIds = "";
        //Chargement de document
        if (naturePiece != null) {
            switch (naturePiece) {
                case Constantes.SCR_RETENUE: {
                    ManagedRetenue w = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
                    if (w != null) {
                        w.loadAll();
                    }
                }
                case Constantes.SCR_SALAIRE: {
                    ManagedSalaire w = (ManagedSalaire) giveManagedBean(ManagedSalaire.class);
                    if (w != null) {
                        w.loadHeadBulletin(false);
                    }
                }
                break;
                case Constantes.SCR_HEAD_VENTE: {
                    ManagedVente w = (ManagedVente) giveManagedBean(ManagedVente.class);
                    if (w != null) {
                        w.loadHeaderByStatut(Constantes.ETAT_VALIDE);
                    }
                }
                break;
                case Constantes.SCR_VENTE: {
                    ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (w != null) {
                        w.loadFactureTypeStatut(Constantes.TYPE_FV, Constantes.ETAT_VALIDE);
                    }
                }
                break;
                case Constantes.SCR_ACHAT: {
                    ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                    if (w != null) {
                        w.loadFactureStatut(Constantes.ETAT_VALIDE);
                    }
                }
                break;
                case Constantes.SCR_DIVERS: {
                    ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                    if (w != null) {
                        w.loadOthersStatut(Constantes.ETAT_VALIDE);
                    }
                }
                break;
                case Constantes.SCR_VIREMENT: {
                    ManagedVirement w = (ManagedVirement) giveManagedBean(ManagedVirement.class);
                    if (w != null) {
                        w.loadPieceByStatut(Constantes.ETAT_REGLE);
                    }
                }
                break;
                case Constantes.SCR_FRAIS_MISSIONS: {
                    ManagedReglementMission w = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
                    if (w != null) {
                        w.loadPieceByStatut(Constantes.ETAT_REGLE);
                    }
                }
                break;
                case Constantes.SCR_CAISSE_VENTE: {
                    ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                    if (w != null) {
                        w.loadPieceByStatut(Constantes.ETAT_REGLE);
                    }
                }
                break;
                case Constantes.SCR_CAISSE_ACHAT: {
                    ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                    if (w != null) {
                        w.loadPieceByStatut(Constantes.ETAT_REGLE);
                    }
                }
                break;
                case Constantes.SCR_CAISSE_DIVERS: {
                    ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                    if (w != null) {
                        w.loadPieceByStatut(Constantes.ETAT_REGLE);
                    }
                }
                break;
                case Constantes.SCR_ACOMPTE_VENTE: {
                    ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                    if (w != null) {
                        w.loadAllByStatut("A", Constantes.ETAT_REGLE);
                    }
                }
                break;
                case Constantes.SCR_CREDIT_VENTE: {
                    ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                    if (w != null) {
                        w.loadAllByStatut("C", Constantes.ETAT_VALIDE);
                    }
                }
                break;
                case Constantes.SCR_ACOMPTE_ACHAT: {
                    ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                    if (w != null) {
                        w.loadAllByStatut("A", Constantes.ETAT_REGLE);
                    }
                }
                break;
                case Constantes.SCR_CREDIT_ACHAT: {
                    ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                    if (w != null) {
                        w.loadAllByStatut("C", Constantes.ETAT_VALIDE);
                    }
                }
                break;
            }
        }
        agencesSelect.clear();
    }

    public void chooseTypePiece() {
        docIds = "";
        if (typePiece != null) {
            naturesPieces.clear();
            switch (typePiece) {
                case Constantes.SCR_TRESORERIE:
                    naturePiece = Constantes.SCR_VIREMENT;
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_ACOMPTE_VENTE, Constantes.SCR_ACOMPTE_VENTE_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_ACOMPTE_ACHAT, Constantes.SCR_ACOMPTE_ACHAT_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_CREDIT_VENTE, Constantes.SCR_CREDIT_VENTE_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_CREDIT_ACHAT, Constantes.SCR_CREDIT_ACHAT_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_FRAIS_MISSIONS, Constantes.SCR_FRAIS_MISSIONS_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_NOTE_FRAIS, Constantes.SCR_NOTE_FRAIS_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_CAISSE_ACHAT, Constantes.SCR_CAISSE_ACHAT_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_CAISSE_DIVERS, Constantes.SCR_CAISSE_DIVERS_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_CAISSE_VENTE, Constantes.SCR_CAISSE_VENTE_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_VIREMENT, Constantes.SCR_VIREMENT_NAME));
                    break;
                default:
                    naturePiece = Constantes.SCR_SALAIRE;
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_SALAIRE, Constantes.SCR_SALAIRE_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_VENTE, Constantes.SCR_VENTE_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_AVOIR_VENTE, Constantes.SCR_AVOIR_VENTE_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_ACHAT, Constantes.SCR_ACHAT_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_AVOIR_ACHAT, Constantes.SCR_AVOIR_ACHAT_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_DIVERS, Constantes.SCR_DIVERS_NAME));
                    naturesPieces.add(new yvs.util.Options(Constantes.SCR_RETENUE, Constantes.SCR_RETENUE_NAME));
                    break;
            }
            chooseNaturePiece();
        }
    }

    public List<yvs.util.Options> listAllNaturePiece() {
        List<yvs.util.Options> re = new ArrayList<>();
        re.add(new yvs.util.Options(Constantes.SCR_SALAIRE, Constantes.SCR_SALAIRE_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_VENTE, Constantes.SCR_VENTE_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_AVOIR_VENTE, Constantes.SCR_AVOIR_VENTE_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_ACHAT, Constantes.SCR_ACHAT_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_AVOIR_ACHAT, Constantes.SCR_AVOIR_ACHAT_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_DIVERS, Constantes.SCR_DIVERS_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_ACOMPTE_ACHAT, Constantes.SCR_ACOMPTE_ACHAT_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_ACOMPTE_VENTE, Constantes.SCR_ACOMPTE_VENTE_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_ACOMPTE_ACHAT, Constantes.SCR_ACOMPTE_ACHAT_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_CREDIT_VENTE, Constantes.SCR_CREDIT_VENTE_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_CREDIT_ACHAT, Constantes.SCR_CREDIT_ACHAT_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_FRAIS_MISSIONS, Constantes.SCR_FRAIS_MISSIONS_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_NOTE_FRAIS, Constantes.SCR_NOTE_FRAIS_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_CAISSE_ACHAT, Constantes.SCR_CAISSE_ACHAT_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_CAISSE_DIVERS, Constantes.SCR_CAISSE_DIVERS_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_CAISSE_VENTE, Constantes.SCR_CAISSE_VENTE_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_VIREMENT, Constantes.SCR_VIREMENT_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_RETENUE, Constantes.SCR_RETENUE_NAME));
        re.add(new yvs.util.Options(Constantes.SCR_BULLETIN, Constantes.SCR_BULLETIN_NAME));
        return re;
    }

    public void correctionComptabiliseByDate() {
        try {
            String query = "SELECT y.id, y.statut_piece, y.cible, y.source, c.journal, s.journal, y.date_paiement, y.date_paiment_prevu, ca.id, sa.id, ca.abbreviation, sa.abbreviation "
                    + "FROM yvs_compta_caisse_piece_virement y LEFT JOIN yvs_compta_content_journal_piece_virement v ON v.reglement = y.id "
                    + "INNER JOIN yvs_base_caisse s ON y.source = s.id INNER JOIN yvs_compta_journaux sj ON s.journal = sj.id INNER JOIN yvs_agences ca ON sj.agence = ca.id "
                    + "INNER JOIN yvs_base_caisse c ON y.cible = c.id INNER JOIN yvs_compta_journaux cj ON c.journal = cj.id INNER JOIN yvs_agences sa ON cj.agence = sa.id "
                    + "WHERE y.statut_piece IN ('P') AND ca.societe = ? AND y.date_piece BETWEEN ? AND ? GROUP BY y.id, c.id, s.id, cj.id, sj.id, ca.id, sa.id "
                    + "HAVING COUNT (v.id) < 2 ORDER BY y.date_piece, y.numero_piece";
            List<Object[]> data = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3)});
            if (data != null ? !data.isEmpty() : false) {
                YvsBaseCaisse cible, source;
                YvsComptaCaissePieceVirement y;
                for (Object[] lect : data) {
                    if (lect[6] != null) {
                        cible = new YvsBaseCaisse((Long) lect[2], new YvsComptaJournaux((Long) lect[4], new YvsAgences((Long) lect[8], (String) lect[10], "", currentAgence.getSociete())));
                        source = new YvsBaseCaisse((Long) lect[3], new YvsComptaJournaux((Long) lect[5], new YvsAgences((Long) lect[9], (String) lect[11], "", currentAgence.getSociete())));
                        y = new YvsComptaCaissePieceVirement((Long) lect[0], ((String) lect[1]).charAt(0), new Date(((java.sql.Timestamp) lect[6]).getTime()), new Date(((java.sql.Date) lect[7]).getTime()), cible, source);
                        query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe = ? AND table_externe = ?";
                        dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_VIREMENT, 2)});
                        query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_piece_virement j WHERE j.piece = p.id AND j.reglement = ?";
                        dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                        comptabiliserCaisseVirement(y, false, false);
                    }
                }
            }
        } catch (Exception ex) {
            getException("correctionComptabiliseByDate", ex);
            getErrorMessage("Action impossible!!!");
        }
    }

    public void comptabiliseByDate() {
        //Chargement de document
        if (naturePiece != null) {
            switch (naturePiece) {
                case Constantes.SCR_SALAIRE: {
                    ManagedSalaire w = (ManagedSalaire) giveManagedBean(ManagedSalaire.class);
                    if (w != null) {
                        getInfoMessage("Fonctionnalité non disponible pour ce document");
                    }
                }
                break;
                case Constantes.SCR_HEAD_VENTE: {
                    ManagedVente w = (ManagedVente) giveManagedBean(ManagedVente.class);
                    if (w != null) {
                        w.comptabiliseByDate(dateDebut, dateFin);
                    }
                }
                break;
                case Constantes.SCR_VENTE: {
                    ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (w != null) {
                        w.comptabiliseByDate(dateDebut, dateFin);
                    }
                }
                break;
                case Constantes.SCR_ACHAT: {
                    ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                    if (w != null) {
                        w.comptabiliseByDate(dateDebut, dateFin);
                    }
                }
                break;
                case Constantes.SCR_DIVERS: {
                    ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                    if (w != null) {
                        getInfoMessage("Fonctionnalité non disponible pour ce document");
                    }
                }
                break;
                case Constantes.SCR_VIREMENT: {
                    ManagedVirement w = (ManagedVirement) giveManagedBean(ManagedVirement.class);
                    if (w != null) {
                        getInfoMessage("Fonctionnalité non disponible pour ce document");
                    }
                }
                break;
                case Constantes.SCR_FRAIS_MISSIONS: {
                    ManagedReglementMission w = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
                    if (w != null) {
                        getInfoMessage("Fonctionnalité non disponible pour ce document");
                    }
                }
                break;
                case Constantes.SCR_CAISSE_VENTE: {
                    ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                    if (w != null) {
                        getInfoMessage("Fonctionnalité non disponible pour ce document");
                    }
                }
                break;
                case Constantes.SCR_CAISSE_ACHAT: {
                    ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                    if (w != null) {
                        getInfoMessage("Fonctionnalité non disponible pour ce document");
                    }
                }
                break;
                case Constantes.SCR_CAISSE_DIVERS: {
                    ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                    if (w != null) {
                        getInfoMessage("Fonctionnalité non disponible pour ce document");
                    }
                }
                break;
                case Constantes.SCR_ACOMPTE_VENTE: {
                    ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                    if (w != null) {
                        getInfoMessage("Fonctionnalité non disponible pour ce document");
                    }
                }
                break;
                case Constantes.SCR_CREDIT_VENTE: {
                    ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                    if (w != null) {
                        getInfoMessage("Fonctionnalité non disponible pour ce document");
                    }
                }
                break;
                case Constantes.SCR_ACOMPTE_ACHAT: {
                    ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                    if (w != null) {
                        getInfoMessage("Fonctionnalité non disponible pour ce document");
                    }
                }
                break;
                case Constantes.SCR_CREDIT_ACHAT: {
                    ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                    if (w != null) {
                        getInfoMessage("Fonctionnalité non disponible pour ce document");
                    }
                }
                break;
            }
        }
    }

    public void comptabilisedSalaire() {
        try {
            if (caisseUpgrade < 1) {
                getErrorMessage("Vous devez preciser la caisse");
                return;
            }
            ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
            if (w != null) {
                ManagedCaisses ws = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                for (YvsComptaCaissePieceVente p : w.getReglements()) {
                    if (p.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_SALAIRE) && p.getComptabilise()) {
                        p.setCaisse(new YvsBaseCaisse(caisseUpgrade));
                        if (ws != null) {
                            int idx = ws.getAllCaisses().indexOf(p.getCaisse());
                            if (idx > -1) {
                                p.setCaisse(ws.getAllCaisses().get(idx));
                            }
                        }
                        if (p.getPieceContenu() != null ? p.getPieceContenu().getId() > 0 : false) {
                            for (YvsComptaContentJournal c : p.getPieceContenu().getPiece().getContentsPiece()) {
                                c.setCompteTiers(null);
                                dao.update(c);
                            }
                            dao.delete(p.getPieceContenu());
                            p.setPieceContenu(null);
                        }
                        p.setComptabilise(false);
                        p.setComptabilised(false);
                        dao.update(p);
                        comptabiliserCaisseVente(p, true, false);
                    }
                }
                succes();

            }
        } catch (Exception ex) {
            getException("Traitement impossible", ex);
        }
    }

    public void loadOdWaitComptabilised() {
        selectDivers = null;
        ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
        if (w != null) {
            w.addParamComptabilised(false);
            w.loadOthersStatut(Constantes.ETAT_VALIDE);
        }
    }

    public void rattacherDivers() {
        if ((selectDivers != null ? selectDivers.getId() > 0 : false) && (selectPiece != null ? selectPiece.getId() > 0 : false)) {
            if (dao.isComptabilise(selectDivers.getId(), Constantes.SCR_DIVERS)) {
                getErrorMessage("Ce document est déjà comptabilisé");
                return;
            }
            YvsComptaContentJournalDocDivers yd = new YvsComptaContentJournalDocDivers(selectDivers, selectPiece);
            yd.setAuthor(currentUser);
            yd = (YvsComptaContentJournalDocDivers) dao.save1(yd);
            selectDivers.setPieceContenu(yd);
            if (selectDivers.isNew_()) {
                YvsComptaContentJournalPieceDivers yp;
                for (YvsComptaCaissePieceDivers p : selectDivers.getReglements()) {
                    yp = new YvsComptaContentJournalPieceDivers(p, selectPiece);
                    yp.setAuthor(currentUser);
                    yp = (YvsComptaContentJournalPieceDivers) dao.save1(yp);
                    p.setPieceContenu(yp);
                }
            }
            succes();
        }
    }

    private boolean controleContenu(YvsComptaContentJournal bean, boolean msg) {
        ResultatAction result = service.controleContenu(bean);
        if (result != null) {
            if (!result.isResult() ? msg : false) {
                getErrorMessage(result.getMessage());
            }
            return result.isResult();
        }
        return false;
    }

    private boolean controleComptabiliseMission(YvsComptaCaissePieceMission pm, boolean msg, boolean all, boolean statut) {
        ResultatAction result = service.controleComptabiliseMission(pm, all, statut);
        if (result != null) {
            if (!result.isResult() ? msg : false) {
                getErrorMessage(result.getMessage());
            }
            return result.isResult();
        }
        return false;
    }

    private boolean canDeletePieceComptable(List<YvsComptaPiecesComptable> list, boolean msg) {
        ResultatAction result = service.canDeletePieceComptable(list);
        if (result != null) {
            if (!result.isResult() ? msg : false) {
                getErrorMessage(result.getMessage());
            }
            return result.isResult();
        }
        return false;
    }

    private YvsComptaPiecesComptable saveNewPieceComptable(Date dateDoc, YvsComptaJournaux jrn, List<YvsComptaContentJournal> contenus, boolean msg) {
        ResultatAction result = service.saveNewPieceComptable(dateDoc, jrn, contenus);
        if (result != null) {
            if (!result.isResult() ? msg : false) {
                getErrorMessage(result.getMessage());
            }
            return result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null;
        }
        return null;
    }

    private YvsComptaPiecesComptable saveNewPieceComptable(YvsComptaPiecesComptable p, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = service.saveNewPieceComptable(p, contenus);
        if (result != null) {
            if (!result.isResult()) {
                getErrorMessage(result.getMessage());
            }
            return result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null;
        }
        return null;
    }

    private String nextLettre(YvsBaseExercice exercice) {
        return service.nextLettre(exercice);
    }

    public String lettrageCompte(List<YvsComptaContentJournal> contenus, YvsBaseExercice exercice) {
        return service.lettrageCompte(contenus, exercice);
    }

    public boolean unComptabiliserSalaire(YvsGrhOrdreCalculSalaire y, boolean msg) {
        ResultatAction result = service.unComptabiliserSalaire(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserSalaire_OLD(YvsGrhOrdreCalculSalaire y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_SALAIRE};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                y.setComptabilise(false);
                y.setComptabilised(false);
                succes();
                return true;
            }
        }
        return false;
    }

    private boolean comptabiliserSalaire(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
            ManagedSalaire w = (ManagedSalaire) giveManagedBean(ManagedSalaire.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn = j.getJournaux().get(index);
                    }
                }
                for (int idx : ids) {
                    YvsGrhOrdreCalculSalaire y = w.getListPlanification().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.setDateDebutTraitement(datePiece);
                        }
                        if (jrn != null) {
                            y.setJournal(jrn);
                        }
                        if (comptabiliserSalaire(y, list, msg, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_piece_salaire");
            }
        } else {
            succes = comptabiliserSalaire(selectSalaire, contenus, msg, false);
        }
        return succes;
    }

    public boolean comptabiliserSalaire(YvsGrhOrdreCalculSalaire y, boolean msg, boolean succes) {
        if (y != null) {
            return comptabiliserSalaire(y, buildSalaireToComptabilise(y.getId(), msg), msg, succes);
        }
        return false;
    }

    private boolean comptabiliserSalaire(YvsGrhOrdreCalculSalaire y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserSalaire(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    private boolean comptabiliserSalaire_OLD(YvsGrhOrdreCalculSalaire y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!y.getCloture()) {
                boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_SALAIRE);
                if (comptabilise) {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                    }
                    return false;
                }
                YvsComptaPiecesComptable p = majComptaSalaire(y.getId(), contenus, msg);
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (reponse) {
                    y.setComptabilised(true);
                    if (succes) {
                        succes();
                    }
                }
                return reponse;
            } else {
                getErrorMessage("Comptabilisation impossible... car cet ordre de calcul est verrouillé");
            }
        }
        return false;
    }

    public boolean unComptabiliserHeaderVente(YvsComEnteteDocVente y, boolean msg) {
        ResultatAction result = service.unComptabiliserHeaderVente(y, unComptabilisedPieceVente);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserHeaderVente_OLD(YvsComEnteteDocVente y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_HEAD_VENTE};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                if (unComptabilisedPieceVente) {
//                    String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
//                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_HEAD_VENTE, 2)});
//                     query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_facture_vente j "
//                        + "WHERE j.piece=p.id AND j.abonnement=?";
//                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});

                    String query = "SELECT DISTINCT y.id FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_compta_content_journal_facture_vente j ON y.vente = j.facture "
                            + "INNER JOIN yvs_compta_content_journal c ON j.piece = c.piece WHERE y.statut_piece = 'P' AND c.ref_externe = ? AND c.table_externe = ?";
                    List<Long> pieces = dao.loadListBySqlQuery(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_HEAD_VENTE, 2)});
                    YvsComptaCaissePieceVente p;
                    for (Long id : pieces) {
                        p = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{id});
                        unComptabiliserCaisseVente(p, false);
                    }
                }
                y.setComptabilised(false);
                y.setComptabilise(false);
                succes();
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserHeaderVente(YvsComEnteteDocVente y, boolean msg, boolean succes) {
        if (y != null) {
            return comptabiliserHeaderVente(y, buildHeaderVenteToComptabilise(y.getId(), msg), msg, succes);
        }
        return false;
    }

    private boolean comptabiliserHeaderVente(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
            ManagedVente w = (ManagedVente) giveManagedBean(ManagedVente.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn = j.getJournaux().get(index);
                    }
                }
                for (int idx : ids) {
                    YvsComEnteteDocVente y = w.getDocuments().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.setDateEntete(datePiece);
                        }
                        if (jrn != null) {
                            y.setJournal(jrn);
                        }
                        if (comptabiliserHeaderVente(y, list, msg, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_piece_head_vente");
            }
        } else {
            succes = comptabiliserHeaderVente(selectHeadVente, contenus, msg, false);
        }
        return succes;
    }

    private boolean comptabiliserHeaderVente(YvsComEnteteDocVente y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserHeaderVente(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    private boolean comptabiliserHeaderVente_OLD(YvsComEnteteDocVente y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!y.getEtat().equals(Constantes.ETAT_EDITABLE)) {
                boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_HEAD_VENTE);
                if (comptabilise) {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                    }
                    return false;
                }
                YvsComptaPiecesComptable p = majComptaHeaderVente(y.getId(), contenus, msg);
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (reponse) {
                    y.setComptabilised(true);
                    if (succes) {
                        succes();
                    }
                }
                return reponse;
            } else {
                getErrorMessage("Comptabilisation impossible... car ce journal de vente est editable");
            }
        }
        return false;
    }

    public boolean unComptabiliserVente(YvsComDocVentes y, boolean msg) {
        ResultatAction result = service.unComptabiliserVente(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserVente_OLD(YvsComDocVentes y, boolean msg) {
        try {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    if (msg) {
                        openNotAcces();
                    }
                    return false;
                }
                champ = new String[]{"id", "table"};
                val = new Object[]{y.getId(), y.getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_AVOIR_VENTE : Constantes.SCR_VENTE};
                nameQueri = "YvsComptaContentJournal.findPieceByExterne";
                List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
                if (canDeletePieceComptable(list, msg)) {
                    if (list != null ? list.isEmpty() : true) {
                        String query = "SELECT DISTINCT c.table_externe FROM yvs_compta_content_journal c INNER JOIN yvs_compta_content_journal_facture_vente p ON p.piece = c.piece WHERE p.facture = ?";
                        List<String> tables = dao.loadListBySqlQuery(query, new Options[]{new Options(y.getId(), 1)});
                        for (String table : tables) {
                            if (table.equals(Constantes.SCR_HEAD_VENTE)) {
                                if (msg) {
                                    getErrorMessage("Cette facture est comptabilisée à partir de son journal de vente");
                                }
                                return false;
                            }
                        }
                    }
                    try {
                        //Supprimme tous les contenu en rapport avec la facture
                        String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=?";
                        dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_VENTE, 2)});
                        query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_facture_vente j "
                                + "WHERE j.piece=p.id AND j.facture=?";
                        dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                        for (YvsComptaCaissePieceVente p : y.getReglements()) {
                            unComptabiliserCaisseVente(p, false);
                        }
                        y.setComptabilised(false);
                        y.setComptabilise(false);
                        y.setPieceContenu(null);
                        if (msg) {
                            succes();
                        }
                        return true;
                    } catch (Exception ex) {
                        getException(ex.getMessage(), ex);
                    }
                }
            }
        } catch (Exception ex) {
            getException("ManagedSaisiePiece (unComptabiliserVente)", ex);
        }
        return false;
    }

    private boolean comptabiliserVente(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
            ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn = j.getJournaux().get(index);
                    }
                }
                for (int idx : ids) {
                    YvsComDocVentes y = w.getDocuments().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.getEnteteDoc().setDateEntete(datePiece);
                        }
                        if (jrn != null) {
                            y.setJournal(jrn);
                        }
                        if (comptabiliserVente(y, list, false, false)) {
                            w.getDocuments().set(idx, y);
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_piece_vente");
            }
        } else {
            succes = comptabiliserVente(selectVente, contenus, msg, false);
        }
        return succes;
    }

    public boolean comptabiliserVente(YvsComDocVentes y) {
        return comptabiliserVente(y, true, true);
    }

    public boolean comptabiliserVente(YvsComDocVentes y, boolean msg, boolean succes) {
        if (y != null) {
            return comptabiliserVente(y, buildVenteToComptabilise(y.getId(), msg), msg, succes);
        }
        return false;
    }

    private boolean comptabiliserVente(YvsComDocVentes y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserVente(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    private boolean comptabiliserVente_OLD(YvsComDocVentes y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.ETAT_VALIDE)) {
                    boolean comptabilise = isComptabilise(y.getId(), y.getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_AVOIR_VENTE : Constantes.SCR_VENTE);
                    if (comptabilise) {
                        if (msg) {
                            //Met à jour le champ comptabilisé
                            if (!y.getComptabilise()) {
                                y.setComptabilise(true);
                                dao.update(y);
                            }
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaVente(y.getId(), contenus, msg);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
                            succes();
                        }
                    }
                    return reponse;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car cette facture n'est pas validée");
                    }
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserAchat(YvsComDocAchats y, boolean msg) {
        ResultatAction result = service.unComptabiliserAchat(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserAchat_OLD(YvsComDocAchats y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), y.getTypeDoc().equals(Constantes.TYPE_FAA) ? Constantes.SCR_AVOIR_ACHAT : Constantes.SCR_ACHAT};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                try {
                    String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_ACHAT, 2)});
                    query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_facture_achat j "
                            + "WHERE j.piece=p.id AND j.facture=?";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                    y.setComptabilised(false);
                    y.setComptabilise(false);
                    y.setPieceContenu(null);
                    for (YvsComptaCaissePieceAchat p : y.getReglements()) {
                        unComptabiliserCaisseAchat(p, false);
                    }
                    if (msg) {
                        succes();
                    }
                    return true;
                } catch (Exception ex) {
                    getErrorMessage("Echec de l'opération", ex.getMessage());
                    getException(ex.getMessage(), ex);
                }
            }
        }
        return false;
    }

    public boolean comptabiliserAchat(YvsComDocAchats y, boolean msg, boolean succes) {
        if (y != null) {
            return comptabiliserAchat(y, buildAchatToComptabilise(y.getId(), msg), msg, succes);
        }
        return false;
    }

    private boolean comptabiliserAchat(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
            ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn = j.getJournaux().get(index);
                    }
                }
                for (int idx : ids) {
                    YvsComDocAchats y = w.getDocuments().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.setDateDoc(datePiece);
                        }
                        if (jrn != null) {
                            y.setJournal(jrn);
                        }
                        if (comptabiliserAchat(y, list, msg, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_piece_achat");
            }
        } else {
            succes = comptabiliserAchat(selectAchat, contenus, msg, false);
        }
        return succes;
    }

    private boolean comptabiliserAchat(YvsComDocAchats y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserAchat(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    private boolean comptabiliserAchat_OLD(YvsComDocAchats y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.ETAT_VALIDE)) {
                    boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_ACHAT);
                    if (comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaAchat(y.getId(), contenus, msg);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
                            succes();
                        }
                    }
                    return reponse;
                } else {
                    getErrorMessage("Comptabilisation impossible... car cette facture n'est pas validée");
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserDivers(YvsComptaCaisseDocDivers y, boolean msg) {
        ResultatAction result = service.unComptabiliserDivers(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserDivers_OLD(YvsComptaCaisseDocDivers y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_DIVERS};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_DIVERS, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_doc_divers j "
                        + "WHERE j.piece=p.id AND j.divers=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                for (YvsComptaCaissePieceDivers p : y.getReglements()) {
                    if (unComptabiliserCaisseDivers(p, false)) {
                        p.setPieceContenu(null);
                    }
                }
                for (YvsComptaAbonementDocDivers p : y.getAbonnements()) {
                    unComptabiliserAbonnementDivers(p, false);
                }
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserDivers(YvsComptaCaisseDocDivers y, boolean msg, boolean succes) {
        List<YvsComptaAbonementDocDivers> abonnements = dao.loadNameQueries("YvsComptaAbonementDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaCentreDocDivers> sections = dao.loadNameQueries("YvsComptaCentreDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaTaxeDocDivers> taxes = dao.loadNameQueries("YvsComptaTaxeDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaCoutSupDocDivers> couts = dao.loadNameQueries("YvsComptaCoutSupDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        if (y.getCompteGeneral() != null ? y.getCompteGeneral().getSaisieAnalytique() && sections.isEmpty() : false) {
            getErrorMessage("Vous devez effectuer la répartition analytique !");
            return false;
        }
        List<YvsComptaContentJournal> contenus = fonction.buildDiversToComptabilise(y.getId(), abonnements, sections, taxes, couts, dao);
        if (contenus != null ? contenus.isEmpty() : true) {
            if (msg) {
                getErrorMessage(dao.getRESULT());
            }
            return false;
        }
        return comptabiliserDivers(y, contenus, abonnements, sections, taxes, couts, msg, succes);
    }

    public boolean comptabiliserDivers(YvsComptaCaisseDocDivers y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        List<YvsComptaAbonementDocDivers> abs = dao.loadNameQueries("YvsComptaAbonementDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaCentreDocDivers> secs = dao.loadNameQueries("YvsComptaCentreDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaTaxeDocDivers> taxs = dao.loadNameQueries("YvsComptaTaxeDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaCoutSupDocDivers> couts = dao.loadNameQueries("YvsComptaCoutSupDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        return comptabiliserDivers(y, contenus, abs, secs, taxs, couts, msg, succes);
    }

    private boolean comptabiliserDivers(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
            ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn = j.getJournaux().get(index);
                    }
                }
                for (int idx : ids) {
                    YvsComptaCaisseDocDivers y = w.getDocuments().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.setDateValider(datePiece);
                        }
                        if (jrn != null) {
                            y.setJournal(jrn);
                        }
                        if (comptabiliserDivers(y, list, msg, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_piece_divers");
            }
        } else {
            succes = comptabiliserDivers(selectDivers, contenus, msg, false);
        }
        return succes;
    }

    private boolean comptabiliserDivers(YvsComptaCaisseDocDivers y, List<YvsComptaContentJournal> contenus, List<YvsComptaAbonementDocDivers> abs, List<YvsComptaCentreDocDivers> secs, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserDivers(y, contenus, abs, secs, taxs, couts);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    private boolean comptabiliserDivers_OLD(YvsComptaCaisseDocDivers y, List<YvsComptaContentJournal> contenus, List<YvsComptaAbonementDocDivers> abs, List<YvsComptaCentreDocDivers> secs, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                    boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_DIVERS);
                    if (comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaDivers(y.getId(), contenus, abs, secs, taxs, couts, msg);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
                            succes();
                        }
                    }
                    return reponse;
                } else {
                    getErrorMessage("Comptabilisation impossible... car cette opération n'est pas validée");
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserCaisseAchat(YvsComptaCaissePieceAchat y, boolean msg) {
        ResultatAction result = service.unComptabiliserCaisseAchat(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserCaisseAchat_OLD(YvsComptaCaissePieceAchat y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_CAISSE_ACHAT};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                try {
                    String query = "DELETE FROM yvs_compta_content_journal "
                            + "WHERE ref_externe=? AND table_externe=? ";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_CAISSE_ACHAT, 2)});
                    query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_piece_achat j "
                            + "WHERE j.piece=p.id AND j.reglement=?";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                    y.setComptabilised(false);
                    y.setComptabilise(false);
                    y.setPieceContenu(null);
                    //Comptabilisation des compensation
                    if (y.getCompensations() != null ? !y.getCompensations().isEmpty() : false) {
                        for (YvsComptaCaissePieceCompensation o : y.getCompensations()) {
                            boolean comptabilise = isComptabilise(o.getVente().getId(), Constantes.SCR_CAISSE_VENTE);
                            if (!comptabilise) {
                                continue;
                            }
                            unComptabiliserCaisseVente(o.getVente(), false);
                        }
                    }
                    if (msg) {
                        succes();
                    }
                    return true;
                } catch (Exception ex) {
                    getErrorMessage("Opération echouée", ex.getMessage());
                    getException(ex.getMessage(), ex);
                }
            }
        }
        return false;
    }

    public boolean comptabiliserCaisseAchat(YvsComptaCaissePieceAchat y, boolean msg, boolean succes) {
        if (y != null) {
            return comptabiliserCaisseAchat(y, buildCaisseAchatToComptabilise(y.getId(), msg), msg, succes);
        }
        return false;
    }

    private boolean comptabiliserCaisseAchat(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
            ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn = j.getJournaux().get(index);
                    }
                }
                for (int idx : ids) {
                    YvsComptaCaissePieceAchat y = w.getPiecesCaisses().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.setDatePaiement(datePiece);
                        }
                        if (jrn != null) {
                            y.setJournal(jrn);
                        }
                        if (comptabiliserCaisseAchat(y, list, false, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_piece_caisse_achat");
            }
        } else {
            succes = comptabiliserCaisseAchat(selectCaisseAchat, contenus, msg, false);
        }
        return succes;
    }

    private boolean comptabiliserCaisseAchat(YvsComptaCaissePieceAchat y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserCaisseAchat(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    private boolean comptabiliserCaisseAchat_OLD(YvsComptaCaissePieceAchat y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_CAISSE_ACHAT);
                    if (comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaCaisseAchat(y.getId(), contenus, msg);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
                            succes();
                        }
                    }
                    return reponse;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car cette pièce de caisse n'est pas validée");
                    }
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserPhaseCaisseVente(YvsComptaPhasePiece y, boolean msg, boolean controle, boolean extourne) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_cheq_reg_vente_annul_comptabilite") && controle) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getPieceVente().getPhasesReglement().indexOf(y);
            YvsComptaPhasePiece pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getPieceVente().getPhasesReglement().size()) {
                pSvt = y.getPieceVente().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getPieceVente().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getPieceVente().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? (!pSvt.equals(y) && !extourne) : false) {
                if (isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_CAISSE_VENTE)) {
                    if (msg) {
                        getErrorMessage("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                    }
                    return false;
                }
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_PHASE_CAISSE_VENTE};
            nameQueri = "YvsComptaContentJournal.findPieceByExterneOrder";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (!extourne) {
                if (canDeletePieceComptable(list, msg)) {
                    String query = "DELETE FROM yvs_compta_content_journal "
                            + "WHERE ref_externe=? AND table_externe=? ";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_PHASE_CAISSE_VENTE, 2)});
                    query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_etape_piece_vente j "
                            + "WHERE j.piece=p.id AND j.etape=?";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                    y.setComptabilised(false);
                    y.setComptabilise(false);
                    y.setPieceContenu(null);
                    if (msg) {
                        succes();
                    }
                    return true;
                }
            } else {
                if (list != null ? !list.isEmpty() : false) {
                    YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) dao.loadOneByNameQueries("YvsComptaPiecesComptable.findById", new String[]{"id"}, new Object[]{list.get(0).getId()});
                    extournePiece(p, dateExtourne);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserPhaseCaisseVente(YvsComptaPhasePiece y, boolean msg, boolean controle) {
        return unComptabiliserPhaseCaisseVente(y, true, true, false);
    }

    public boolean comptabiliserPhaseCaisseVente(YvsComptaPhasePiece y, boolean msg, boolean succes) {
        return comptabiliserPhaseCaisseVente(y, msg, succes, false);
    }

    /**
     * * lorsqu'il s'agit d'une phase de chèque, elle peut faire l'objet d'une
     * extourne et d'une recomptabilisation; dans ce cas, le paramètre force
     * permet de laisser passer la double comptabilisation
     */
    public boolean comptabiliserPhaseCaisseVente(YvsComptaPhasePiece y, boolean msg, boolean succes, boolean force) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                if (force == false) {
                    boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_PHASE_CAISSE_VENTE);
                    if (comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car cette étape est déjà comptabilisée");
                        }
                        return false;
                    }
                }
                YvsComptaPiecesComptable p = majComptaEtapeCaisseVente(y.getId(), msg);
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (reponse) {
                    y.setComptabilised(true);
                    if (succes) {
                        succes();
                    }
                }
                return reponse;
            } else {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible... car cette phase n'est pas validée");
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserPhaseCaisseAchat(YvsComptaPhasePieceAchat y, boolean msg, boolean controle) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_cheq_reg_vente_annul_comptabilite") && controle) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getPieceAchat().getPhasesReglement().indexOf(y);
            YvsComptaPhasePieceAchat pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getPieceAchat().getPhasesReglement().size()) {
                pSvt = y.getPieceAchat().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getPieceAchat().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getPieceAchat().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? !pSvt.equals(y) : false) {
                if (isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_CAISSE_ACHAT)) {
                    if (msg) {
                        getErrorMessage("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                    }
                    return false;
                }
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_PHASE_CAISSE_ACHAT};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_PHASE_CAISSE_ACHAT, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_etape_piece_achat j "
                        + "WHERE j.piece=p.id AND j.etape=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserPhaseCaisseAchat(YvsComptaPhasePieceAchat y, boolean msg, boolean succes) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_PHASE_CAISSE_ACHAT);
                if (comptabilise) {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car cette étape est déjà comptabilisée");
                    }
                    return false;
                }
                YvsComptaPiecesComptable p = majComptaEtapeCaisseAchat(y.getId(), msg);
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (reponse) {
                    y.setComptabilised(true);
                    if (succes) {
                        succes();
                    }
                }
                return reponse;
            } else {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible... car cette phase n'est pas validée");
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserPhaseCaisseDivers(YvsComptaPhasePieceDivers y, boolean msg, boolean controle) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_cheq_reg_vente_annul_comptabilite") && controle) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getPieceDivers().getPhasesReglement().indexOf(y);
            YvsComptaPhasePieceDivers pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getPieceDivers().getPhasesReglement().size()) {
                pSvt = y.getPieceDivers().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getPieceDivers().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getPieceDivers().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? !pSvt.equals(y) : false) {
                if (isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_CAISSE_DIVERS)) {
                    if (msg) {
                        getErrorMessage("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                    }
                    return false;
                }
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_PHASE_CAISSE_DIVERS};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_PHASE_CAISSE_DIVERS, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_etape_piece_divers j "
                        + "WHERE j.piece=p.id AND j.etape=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserPhaseCaisseDivers(YvsComptaPhasePieceDivers y, boolean msg, boolean succes) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_PHASE_CAISSE_DIVERS);
                if (comptabilise) {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car cette étape est déjà comptabilisée");
                    }
                    return false;
                }
                YvsComptaPiecesComptable p = majComptaEtapeCaisseDivers(y.getId(), msg);
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (reponse) {
                    y.setComptabilised(true);
                    if (succes) {
                        succes();
                    }
                }
                return reponse;
            } else {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible... car cette phase n'est pas validée");
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserPhaseCaisseVirement(YvsComptaPhasePieceVirement y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_cheq_reg_vente_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getVirement().getPhasesReglement().indexOf(y);
            YvsComptaPhasePieceVirement pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getVirement().getPhasesReglement().size()) {
                pSvt = y.getVirement().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getVirement().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getVirement().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? !pSvt.equals(y) : false) {
                if (isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_VIREMENT)) {
                    if (msg) {
                        getErrorMessage("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                    }
                    return false;
                }
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_PHASE_VIREMENT};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_pieces_comptable WHERE id IN "
                        + "(SELECT DISTINCT p.piece FROM yvs_compta_content_journal_etape_piece_virement p LEFT JOIN yvs_compta_content_journal c ON p.piece = c.piece WHERE c.id IS NULL AND p.etape = ?)";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserPhaseCaisseVirement(YvsComptaPhasePieceVirement y, boolean msg, boolean succes) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_PHASE_VIREMENT);
                if (comptabilise) {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car cette étape est déjà comptabilisée");
                    }
                    return false;
                }
                YvsComptaPiecesComptable p = majComptaEtapeCaisseVirement(y.getId(), msg);
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (reponse) {
                    y.setComptabilised(true);
                    if (succes) {
                        succes();
                    }
                }
                return reponse;
            } else {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible... car cette phase n'est pas validée");
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserPhaseAcompteAchat(YvsComptaPhaseAcompteAchat y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getPieceAchat().getPhasesReglement().indexOf(y);
            YvsComptaPhaseAcompteAchat pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getPieceAchat().getPhasesReglement().size()) {
                pSvt = y.getPieceAchat().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getPieceAchat().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getPieceAchat().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? !pSvt.equals(y) : false) {
                if (isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_ACOMPTE_ACHAT)) {
                    if (msg) {
                        getErrorMessage("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                    }
                    return false;
                }
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_PHASE_ACOMPTE_ACHAT};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_PHASE_ACOMPTE_ACHAT, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_etape_acompte_achat j "
                        + "WHERE j.piece=p.id AND j.etape=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserPhaseAcompteAchat(YvsComptaPhaseAcompteAchat y, boolean msg, boolean succes) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_PHASE_ACOMPTE_ACHAT);
                if (comptabilise) {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car cette étape est déjà comptabilisée");
                    }
                    return false;
                }
                YvsComptaPiecesComptable p = majComptaEtapeAcompteAchat(y.getId(), msg);
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (reponse) {
                    y.setComptabilised(true);
                    if (succes) {
                        succes();
                    }
                }
                return reponse;
            } else {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible... car cette phase n'est pas validée");
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserPhaseAcompteVente(YvsComptaPhaseAcompteVente y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getPieceVente().getPhasesReglement().indexOf(y);
            YvsComptaPhaseAcompteVente pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getPieceVente().getPhasesReglement().size()) {
                pSvt = y.getPieceVente().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getPieceVente().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getPieceVente().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? !pSvt.equals(y) : false) {
                if (isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_ACOMPTE_VENTE)) {
                    if (msg) {
                        getErrorMessage("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                    }
                    return false;
                }
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_PHASE_ACOMPTE_VENTE};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_PHASE_ACOMPTE_VENTE, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_etape_acompte_vente j "
                        + "WHERE j.piece=p.id AND j.etape=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserPhaseAcompteVente(YvsComptaPhaseAcompteVente y, boolean msg, boolean succes) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_PHASE_ACOMPTE_VENTE);
                if (comptabilise) {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car cette étape est déjà comptabilisée");
                    }
                    return false;
                }

                YvsComptaPiecesComptable p = majComptaEtapeAcompteVente(y.getId(), msg);
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (reponse) {
                    y.setComptabilised(true);
                    if (succes) {
                        succes();
                    }
                }
                return reponse;
            } else {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible... car cette phase n'est pas validée");
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserPhaseCaisseCreditAchat(YvsComptaPhaseReglementCreditFournisseur y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getReglement().getPhasesReglement().indexOf(y);
            YvsComptaPhaseReglementCreditFournisseur pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getReglement().getPhasesReglement().size()) {
                pSvt = y.getReglement().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getReglement().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getReglement().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? !pSvt.equals(y) : false) {
                if (isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_CREDIT_ACHAT)) {
                    if (msg) {
                        getErrorMessage("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                    }
                    return false;
                }
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_PHASE_CREDIT_ACHAT};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_PHASE_CREDIT_ACHAT, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_etape_reglement_credit_fournisseur j "
                        + "WHERE j.piece=p.id AND j.etape=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserPhaseCaisseCreditAchat(YvsComptaPhaseReglementCreditFournisseur y, boolean msg, boolean succes) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_PHASE_CREDIT_ACHAT);
                if (comptabilise) {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car cette étape est déjà comptabilisée");
                    }
                    return false;
                }
                YvsComptaPiecesComptable p = majComptaEtapeCaisseCreditAchat(y.getId(), msg);
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (reponse) {
                    y.setComptabilised(true);
                    if (succes) {
                        succes();
                    }
                }
                return reponse;
            } else {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible... car cette phase n'est pas validée");
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserPhaseCaisseCreditVente(YvsComptaPhaseReglementCreditClient y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getReglement().getPhasesReglement().indexOf(y);
            YvsComptaPhaseReglementCreditClient pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getReglement().getPhasesReglement().size()) {
                pSvt = y.getReglement().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getReglement().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getReglement().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? !pSvt.equals(y) : false) {
                if (isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_CREDIT_VENTE)) {
                    if (msg) {
                        getErrorMessage("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                    }
                    return false;
                }
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_PHASE_CREDIT_VENTE};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_PHASE_CREDIT_VENTE, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_etape_reglement_credit_client j "
                        + "WHERE j.piece=p.id AND j.etape=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserPhaseCaisseCreditVente(YvsComptaPhaseReglementCreditClient y, boolean msg, boolean succes) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_PHASE_CREDIT_VENTE);
                if (comptabilise) {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car cette étape est déjà comptabilisée");
                    }
                    return false;
                }
                YvsComptaPiecesComptable p = majComptaEtapeCaisseCreditVente(y.getId(), msg);
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (reponse) {
                    y.setComptabilised(true);
                    if (succes) {
                        succes();
                    }
                }
                return reponse;
            } else {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible... car cette phase n'est pas validée");
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserCaisseVente(YvsComptaCaissePieceVente y, boolean msg) {
        ResultatAction result = service.unComptabiliserCaisseVente(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserCaisseVente_OLD(YvsComptaCaissePieceVente y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            //Vérifié avant tout que la période n'est pas clôturé            
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            if (y.getVerouille()) {
                if (msg) {
                    getErrorMessage("Le chèque concerné par cette phase à été vérouillé !");
                }
                return false;
            }
            String table_ = y.getVente().getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_CAISSE_AVOIR_VENTE : Constantes.SCR_CAISSE_VENTE;
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), table_};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                try {
//                    String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe = ? AND table_externe = ?";
//                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(table_, 2)});
                    String query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal c WHERE c.piece=p.id AND ref_externe = ? AND table_externe = ?";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(table_, 2)});
                    y.setPieceContenu(null);

                    y.setComptabilised(false);
                    y.setComptabilise(false);
                    //Comptabilisation des compensation
                    if (y.getCompensations() != null ? !y.getCompensations().isEmpty() : false) {
                        for (YvsComptaCaissePieceCompensation o : y.getCompensations()) {
                            boolean comptabilise = isComptabilise(o.getAchat().getId(), Constantes.SCR_CAISSE_ACHAT);
                            if (!comptabilise) {
                                continue;
                            }
                            unComptabiliserCaisseAchat(o.getAchat(), false);
                        }
                    }
                    if (msg) {
                        succes();
                    }
                    return true;
                } catch (Exception ex) {
                    getErrorMessage("Opération echouée", ex.getMessage());
                    getException(ex.getMessage(), ex);
                }
            }
        }
        return false;
    }

    private boolean comptabiliserCaisseVente(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
            ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn = j.getJournaux().get(index);
                    }
                }
                for (int idx : ids) {
                    YvsComptaCaissePieceVente y = w.getReglements().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.setDatePaiement(datePiece);
                        }
                        if (jrn != null) {
                            y.setJournal(jrn);
                        }
                        if (comptabiliserCaisseVente(y, list, false, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_piece_caisse_vente");
            }
        } else {
            succes = comptabiliserCaisseVente(selectCaisseVente, contenus, msg, false);
        }
        return succes;
    }

    public boolean comptabiliserCaisseVente(YvsComptaCaissePieceVente y) {
        return comptabiliserCaisseVente(y, true);
    }

    public boolean comptabiliserCaisseVente(YvsComptaCaissePieceVente y, boolean msg) {
        return comptabiliserCaisseVente(y, msg, true);
    }

    public boolean comptabiliserCaisseVente(YvsComptaCaissePieceVente y, boolean msg, boolean succes) {
        return comptabiliserCaisseVente(y, buildCaisseVenteToComptabilise(y.getId(), msg), msg, succes);
    }

    private boolean comptabiliserCaisseVente(YvsComptaCaissePieceVente y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserCaisseVente(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    private boolean comptabiliserCaisseVente_OLD(YvsComptaCaissePieceVente y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = isComptabilise(y.getId(), y.getVente().getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_CAISSE_AVOIR_VENTE : Constantes.SCR_CAISSE_VENTE);
                    if (comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaCaisseVente(y.getId(), contenus, msg);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
                            succes();
                        }
                    }
                    return reponse;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car cette pièce de caisse n'est pas validée");
                    }
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserCaisseCreditAchat(YvsComptaReglementCreditFournisseur y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_CAISSE_CREDIT_ACHAT};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_CAISSE_CREDIT_ACHAT, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_reglement_credit_fournisseur j "
                        + "WHERE j.piece=p.id AND j.reglement=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserCaisseCreditAchat(YvsComptaReglementCreditFournisseur y, boolean msg, boolean succes) {
        return comptabiliserCaisseCreditAchat(y, buildCaisseCreditAchatToComptabilise(y.getId(), msg), msg, succes);
    }

    private boolean comptabiliserCaisseCreditAchat(YvsComptaReglementCreditFournisseur y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = isComptabilise(y.getCredit().getId(), Constantes.SCR_CREDIT_ACHAT);
                    if (!comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car le crédit n'est pas comptabilisé");
                        }
                        return false;
                    }
                    comptabilise = isComptabilise(y.getId(), Constantes.SCR_CAISSE_CREDIT_ACHAT);
                    if (comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisé");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaCaisseCreditAchat(y.getId(), contenus, msg);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
                            succes();
                        }
                    }
                    return reponse;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car cette pièce de caisse n'est pas payée");
                    }
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserCaisseCreditVente(YvsComptaReglementCreditClient y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_CAISSE_CREDIT_VENTE};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_CAISSE_CREDIT_VENTE, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_reglement_credit_client j "
                        + "WHERE j.piece=p.id AND j.reglement=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserCaisseCreditVente(YvsComptaReglementCreditClient y, boolean msg, boolean succes) {
        return comptabiliserCaisseCreditVente(y, buildCaisseCreditVenteToComptabilise(y.getId(), msg), msg, succes);
    }

    private boolean comptabiliserCaisseCreditVente(YvsComptaReglementCreditClient y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = isComptabilise(y.getCredit().getId(), Constantes.SCR_CREDIT_VENTE);
                    if (!comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car le crédit n'est pas comptabilisé");
                        }
                        return false;
                    }
                    comptabilise = isComptabilise(y.getId(), Constantes.SCR_CAISSE_CREDIT_VENTE);
                    if (comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaCaisseCreditVente(y.getId(), contenus, msg);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
                            succes();
                        }
                    }
                    return reponse;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car cette pièce de caisse n'est pas payée");
                    }
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserCaisseDivers(YvsComptaCaissePieceDivers y, boolean msg) {
        ResultatAction result = service.unComptabiliserCaisseDivers(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserCaisseDivers_OLD(YvsComptaCaissePieceDivers y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_CAISSE_DIVERS};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                try {
                    if (y.getDocDivers().getIdTiers() != null ? y.getDocDivers().getIdTiers() < 1 : true) {
                        String query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal c INNER JOIN yvs_compta_caisse_piece_divers y ON c.ref_externe = y.id AND c.table_externe = ? WHERE c.piece = p.id AND y.doc_divers = ?";
                        dao.requeteLibre(query, new Options[]{new Options(Constantes.SCR_CAISSE_DIVERS, 1), new Options(y.getDocDivers().getId(), 2)});
                    } else {
                        String query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal c WHERE c.piece = p.id AND c.ref_externe = ? AND c.table_externe = ?";
                        dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_CAISSE_DIVERS, 2)});
                        if (y.getPieceContenu() != null) {
                            dao.delete(y.getPieceContenu());
                        }
                        y.setComptabilised(false);
                        y.setComptabilise(false);
                        y.setPieceContenu(null);
                    }
                    if (msg) {
                        succes();
                    }
                    return true;
                } catch (Exception ex) {
                    getErrorMessage("Echec de l'opération", ex.getMessage());
                    getException(ex.getMessage(), ex);
                }
            }
        }

        return false;
    }

    public boolean comptabiliserCaisseDivers(YvsComptaCaissePieceDivers y, boolean msg, boolean succes) {
        return comptabiliserCaisseDivers(y, buildCaisseDiversToComptabilise(y.getId(), msg), msg, succes);
    }

    private boolean comptabiliserCaisseDivers(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;

        if (ids != null ? !ids.isEmpty() : false) {
            ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn = j.getJournaux().get(index);
                    }
                }
                for (int idx : ids) {
                    YvsComptaCaissePieceDivers y = w.getPieces().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.setDateValider(datePiece);
                        }
                        if (jrn != null) {
                            y.setJournal(jrn);
                        }
                        if (comptabiliserCaisseDivers(y, list, false, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_piece_caisse_divers");
            }
        } else {
            succes = comptabiliserCaisseDivers(selectCaisseDivers, contenus, msg, false);
        }
        return succes;
    }

    private boolean comptabiliserCaisseDivers(YvsComptaCaissePieceDivers y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserCaisseDivers(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    private boolean comptabiliserCaisseDivers_OLD(YvsComptaCaissePieceDivers y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_CAISSE_DIVERS);
                    if (comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaCaisseDivers(y.getId(), contenus);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
                            succes();
                        }
                    }
                    return reponse;
                } else {
                    getErrorMessage("Comptabilisation impossible... car cette pièce de caisse n'est pas validée");
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserAbonnementDivers(YvsComptaAbonementDocDivers y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_ABONNEMENT_DIVERS};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_ABONNEMENT_DIVERS, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_abonnement_divers j "
                        + "WHERE j.piece=p.id AND j.abonnement=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
//                String query = "DELETE FROM yvs_compta_pieces_comptable WHERE id IN "
//                        + "(SELECT DISTINCT p.piece FROM yvs_compta_content_journal_abonnement_divers p LEFT JOIN yvs_compta_content_journal c ON p.piece = c.piece WHERE c.id IS NULL AND p.abonnement = ?)";
//                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
//                if (y.getPieceContenu() != null) {
//                    dao.delete(y.getPieceContenu());
//                }
//                if (y.isUnComptabiliseAll() && (y.getPieceContenu() != null ? y.getPieceContenu().getPiece() != null : false)) {
//                    dao.delete(y.getPieceContenu().getPiece());
//                }
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserAbonnementDivers(YvsComptaAbonementDocDivers y, boolean msg, boolean succes) {
        List<YvsComptaCentreDocDivers> secs = dao.loadNameQueries("YvsComptaCentreDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y.getDocDivers()});
        List<YvsComptaTaxeDocDivers> taxs = dao.loadNameQueries("YvsComptaTaxeDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y.getDocDivers()});
        List<YvsComptaCoutSupDocDivers> couts = dao.loadNameQueries("YvsComptaCoutSupDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y.getDocDivers()});
        return comptabiliserAbonnementDivers(y, fonction.buildDiversToComptabilise(y, secs, taxs, couts, dao), msg, succes);
    }

    private boolean comptabiliserAbonnementDivers(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;

        if (ids != null ? !ids.isEmpty() : false) {
            ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                for (int idx : ids) {
                    YvsComptaAbonementDocDivers y = w.getAbonnements().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (comptabiliserAbonnementDivers(y, list, false, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_piece_abonnement_divers");
            }
        } else {
            succes = comptabiliserAbonnementDivers(selectAbonnementDivers, contenus, msg, false);
        }
        return succes;
    }

    private boolean comptabiliserAbonnementDivers(YvsComptaAbonementDocDivers y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getDocDivers().getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                    boolean comptabilise = isComptabilise(y.getDocDivers().getId(), Constantes.SCR_DIVERS);
                    if (!comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car l'opération diverse n'est pas encore comptabilisée");
                        }
                        return false;
                    }

                    comptabilise = isComptabilise(y.getId(), Constantes.SCR_ABONNEMENT_DIVERS);
                    if (comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaAbonnementDivers(y.getId(), contenus);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
                            succes();
                        }
                    }
                    return reponse;
                } else {
                    getErrorMessage("Comptabilisation impossible... car cet abonnement est rattaché à un document qui n'est pas validée");
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserAcompteClient(YvsComptaAcompteClient y) {
        return unComptabiliserAcompteClient(y, true);
    }

    public boolean unComptabiliserAcompteClient(YvsComptaAcompteClient y, boolean msg) {
        ResultatAction result = service.unComptabiliserAcompteClient(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserAcompteClient_OLD(YvsComptaAcompteClient y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_ACOMPTE_VENTE};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_ACOMPTE_VENTE, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_acompte_client j "
                        + "WHERE j.piece=p.id AND j.acompte=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                succes();
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserAcompteClient(YvsComptaAcompteClient y, boolean msg, boolean succes) {
        if (y.getPhasesReglement() != null ? !y.getPhasesReglement().isEmpty() : false) {
            boolean correct = false;
            for (YvsComptaPhaseAcompteVente a : y.getPhasesReglement()) {
                if (Objects.equals(a.getStatut(), Constantes.STATUT_DOC_VALIDE)) {
                    correct = comptabiliserPhaseAcompteVente(a, true, true);
                }
            }
            return correct;
        } else {
            return comptabiliserAcompteClient(y, buildAcompteClientToComptabilise(y.getId(), msg), msg, succes);
        }
    }

    private boolean comptabiliserAcompteClient(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
            ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn = j.getJournaux().get(index);
                    }
                }
                for (int idx : ids) {
                    YvsComptaAcompteClient y = w.getAcomptes().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.setDatePaiement(datePiece);
                        }
                        if (jrn != null) {
                            y.setJournal(jrn);
                        }
                        if (comptabiliserAcompteClient(y, list, false, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_acompte_client");
            }
        } else {
            succes = comptabiliserAcompteClient(selectAcompteVente, contenus, msg, false);
        }
        return succes;
    }

    public boolean comptabiliserAcompteClient(YvsComptaAcompteClient y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserAcompteVente(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean comptabiliserAcompteClient_OLD(YvsComptaAcompteClient y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_ACOMPTE_VENTE);
                    if (comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaAcompteClient(y.getId(), contenus, msg);
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
                            succes();
                        }
                    }
                    return succes;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car cet acompte n'est pas validée");
                    }
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserAcompteFournisseur(YvsComptaAcompteFournisseur y) {
        return unComptabiliserAcompteFournisseur(y, true);
    }

    public boolean unComptabiliserAcompteFournisseur(YvsComptaAcompteFournisseur y, boolean msg) {
        ResultatAction result = service.unComptabiliserAcompteFournisseur(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserAcompteFournisseur_OLD(YvsComptaAcompteFournisseur y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_ACOMPTE_ACHAT};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_ACOMPTE_ACHAT, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_acompte_fournisseur j "
                        + "WHERE j.piece=p.id AND j.acompte=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserAcompteFournisseur(YvsComptaAcompteFournisseur y, boolean msg, boolean succes) {
        if (y.getPhasesReglement() != null ? !y.getPhasesReglement().isEmpty() : false) {
            boolean correct = false;
            for (YvsComptaPhaseAcompteAchat a : y.getPhasesReglement()) {
                if (a.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                    correct = comptabiliserPhaseAcompteAchat(a, true, true);
                }
            }
            return correct;
        } else {
            return comptabiliserAcompteFournisseur(y, buildAcompteFournisseurToComptabilise(y.getId(), msg), msg, succes);
        }
    }

    private boolean comptabiliserAcompteFournisseur(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
            ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn = j.getJournaux().get(index);
                    }
                }
                for (int idx : ids) {
                    YvsComptaAcompteFournisseur y = w.getAcomptes().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.setDatePaiement(datePiece);
                        }
                        if (jrn != null) {
                            y.setJournal(jrn);
                        }
                        if (comptabiliserAcompteFournisseur(y, list, false, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_acompte_fournisseur");
            }
        } else {
            succes = comptabiliserAcompteFournisseur(selectAcompteAchat, contenus, msg, false);
        }
        return succes;
    }

    public boolean comptabiliserAcompteFournisseur(YvsComptaAcompteFournisseur y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserAcompteAchat(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean comptabiliserAcompteFournisseur_OLD(YvsComptaAcompteFournisseur y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_ACOMPTE_ACHAT);
                    if (comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaAcompteFournisseur(y.getId(), contenus, msg);
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
                            succes();
                        }
                    }
                    return reponse;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car cet acompte n'est pas validée");
                    }
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserCreditFournisseur(YvsComptaCreditFournisseur y, boolean msg) {
        ResultatAction result = service.unComptabiliserCreditFournisseur(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserCreditFournisseur_OLD(YvsComptaCreditFournisseur y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_CREDIT_ACHAT};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_CREDIT_ACHAT, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_credit_fournisseur j "
                        + "WHERE j.piece=p.id AND j.credit=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserCreditFournisseur(YvsComptaCreditFournisseur y, boolean msg, boolean succes) {
        return comptabiliserCreditFournisseur(y, buildCreditFournisseurToComptabilise(y.getId(), msg), msg, succes);
    }

    private boolean comptabiliserCreditFournisseur(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;

        if (ids != null ? !ids.isEmpty() : false) {
            ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
            if (w != null) {
                boolean error = false;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn = j.getJournaux().get(index);
                    }
                }
                List<YvsComptaContentJournal> list;
                for (int idx : ids) {
                    YvsComptaCreditFournisseur y = w.getCredits().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.setDateCredit(datePiece);
                        }
                        if (jrn != null) {
                            y.setJournal(jrn);
                        }
                        if (comptabiliserCreditFournisseur(y, list, false, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_credit_fournisseur");
            }
        } else {
            succes = comptabiliserCreditFournisseur(selectCreditAchat, contenus, msg, false);
        }
        return succes;
    }

    public boolean comptabiliserCreditFournisseur(YvsComptaCreditFournisseur y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserCreditAchat(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean comptabiliserCreditFournisseur_OLD(YvsComptaCreditFournisseur y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                    boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_CREDIT_ACHAT);
                    if (comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaCreditFournisseur(y.getId(), contenus, msg);
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
                            succes();
                        }
                    }
                    return reponse;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car ce crédit n'est pas validée");
                    }
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserCreditClient(YvsComptaCreditClient y, boolean msg) {
        ResultatAction result = service.unComptabiliserCreditClient(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserCreditClient_OLD(YvsComptaCreditClient y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_CREDIT_VENTE};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_CREDIT_VENTE, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_credit_client j "
                        + "WHERE j.piece=p.id AND j.credit=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                succes();
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserCreditClient(YvsComptaCreditClient y, boolean msg, boolean succes) {
        return comptabiliserCreditClient(y, buildCreditClientToComptabilise(y.getId(), msg), msg, succes);
    }

    private boolean comptabiliserCreditClient(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;

        if (ids != null ? !ids.isEmpty() : false) {
            ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn = j.getJournaux().get(index);
                    }
                }
                for (int idx : ids) {
                    YvsComptaCreditClient y = w.getCredits().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.setDateCredit(datePiece);
                        }
                        if (jrn != null) {
                            y.setJournal(jrn);
                        }
                        if (comptabiliserCreditClient(y, list, false, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_credit_client");
            }
        } else {
            succes = comptabiliserCreditClient(selectCreditVente, contenus, msg, false);
        }
        return succes;
    }

    public boolean comptabiliserCreditClient(YvsComptaCreditClient y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserCreditVente(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean comptabiliserCreditClient_OLD(YvsComptaCreditClient y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                    boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_CREDIT_VENTE);
                    if (comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaCreditClient(y.getId(), contenus, msg);
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
                            succes();
                        }
                    }
                    return reponse;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... car ce crédit n'est pas validée");
                    }
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserCaisseVirement(YvsComptaCaissePieceVirement y, boolean msg) {
        ResultatAction result = service.unComptabiliserCaisseVirement(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserCaisseVirement_OLD(YvsComptaCaissePieceVirement y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_VIREMENT};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_VIREMENT, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_piece_virement j "
                        + "WHERE j.piece=p.id AND j.reglement=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean comptabiliserCaisseVirement(YvsComptaCaissePieceVirement y, boolean msg, boolean succes) {
        return comptabiliserCaisseVirement(y, buildCaisseVirementToComptabilise(y.getId(), msg), msg, succes);
    }

    private boolean comptabiliserCaisseVirement(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
            ManagedVirement w = (ManagedVirement) giveManagedBean(ManagedVirement.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn1 = null, jrn2 = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn1 = j.getJournaux().get(index);
                    }
                    index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece2));
                    if (index > -1) {
                        jrn2 = j.getJournaux().get(index);
                    }
                }
                for (int idx : ids) {
                    YvsComptaCaissePieceVirement y = w.getListAllVirement().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.setDatePaiement(datePiece);
                        }
                        if (jrn1 != null) {
                            y.setJournalSource(jrn1);
                        }
                        if (jrn2 != null) {
                            y.setJournalCible(jrn2);
                        }
                        if (comptabiliserCaisseVirement(y, list, msg, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_piece_mission");
            }
        } else {
            succes = comptabiliserCaisseVirement(selectCaisseVirement, contenus, msg, false);
        }
        return succes;
    }

    private boolean comptabiliserCaisseVirement(YvsComptaCaissePieceVirement y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserCaisseVirement(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    private boolean comptabiliserCaisseVirement_OLD(YvsComptaCaissePieceVirement y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) || y.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS)) {
                    List<Long> ids = new ArrayList<>();
                    ids.add(y.getId());
                    boolean comptabilise = dao.isComptabilise(ids, Constantes.SCR_VIREMENT, true, (y.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS) ? Constantes.MOUV_CAISS_SORTIE.charAt(0) : Constantes.MOUV_CAISS_ENTREE.charAt(0)));
                    if (comptabilise) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaCaisseVirement(y.getId(), contenus, msg);
                    if (p != null ? p.getId() > 0 : false) {
                        int idx = listePiece.indexOf(p);
                        if (idx > -1) {
                            listePiece.set(idx, p);
                        } else {
                            listePiece.add(0, p);
                        }
                    }
                    boolean reponse = (p != null ? p.getId() > 0 : false);
                    if (reponse) {
                        if (succes) {
                            succes();
                        }
                    }
                    return reponse;
                } else {
                    getErrorMessage("Comptabilisation impossible... car cette pièce de caisse n'est pas validée");
                }
            }
        }
        return false;
    }

    public boolean comptabiliserCaisseMission(YvsComptaCaissePieceMission y) {
        return comptabiliserCaisseMission(y, true);
    }

    public boolean comptabiliserCaisseMission(YvsComptaCaissePieceMission y, boolean msg) {
        return comptabiliserCaisseMission(y, msg, true);
    }

    public boolean comptabiliserCaisseMission(YvsComptaCaissePieceMission y, boolean msg, boolean succes) {
        return comptabiliserCaisseMission(y, buildMissionToComptabilise(y.getId(), msg), msg, succes);
    }

    private boolean comptabiliserCaisseMission(YvsComptaCaissePieceMission y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserCaisseMission(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    private boolean comptabiliserCaisseMission_OLD(YvsComptaCaissePieceMission y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_FRAIS_MISSIONS);
                    if (comptabilise) {
                        if (true) {
                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaCaisseMission(y.getId(), contenus, msg);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
                            succes();
                        }
                    }
                    return reponse;
                } else {
                    getErrorMessage("Comptabilisation impossible... car cette pièce de caisse n'est pas validée");
                }
            }
        }
        return false;
    }

    private boolean comptabiliserCaisseMission(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;

        if (ids != null ? !ids.isEmpty() : false) {
            ManagedReglementMission w = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn = j.getJournaux().get(index);
                    }
                }
                for (int idx : ids) {
                    YvsComptaCaissePieceMission y = w.getAvances().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.setDatePaiement(datePiece);
                        }
                        if (jrn != null) {
                            y.setJournal(jrn);
                        }
                        if (comptabiliserCaisseMission(y, list, msg, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_piece_mission");
            }
        } else {
            succes = comptabiliserCaisseMission(selectCaisseMission, contenus, msg, false);
        }
        return succes;
    }

    public boolean unComptabiliserCaisseMission(YvsComptaCaissePieceMission y, boolean msg) {
        ResultatAction result = service.unComptabiliserCaisseMission(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserCaisseMission_OLD(YvsComptaCaissePieceMission y, boolean msg) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                if (msg) {
                    openNotAcces();
                }
                return false;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{y.getId(), Constantes.SCR_FRAIS_MISSIONS};
            nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (canDeletePieceComptable(list, msg)) {
                y.setComptabilised(false);
                y.setComptabilise(false);
                if (msg) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean unComptabiliserBulletin(YvsGrhBulletins y, boolean msg) {
        ResultatAction result = service.unComptabiliserBulletin(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean comptabiliserBulletin(YvsGrhBulletins y) {
        return comptabiliserBulletin(y, true);
    }

    public boolean comptabiliserBulletin(YvsGrhBulletins y, boolean msg) {
        return comptabiliserBulletin(y, msg, true);
    }

    public boolean comptabiliserBulletin(YvsGrhBulletins y, boolean msg, boolean succes) {
        return comptabiliserBulletin(y, buildBulletinToComptabilise(y.getId(), msg), msg, succes);
    }

    public boolean comptabiliserBulletin(YvsGrhBulletins y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserBulletin(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean unComptabiliserRetenue(YvsGrhElementAdditionel y, boolean msg) {
        ResultatAction result = service.unComptabiliserRetenue(y);
        if (result != null) {
            if (msg) {
                if (result.isResult()) {
                    succes();
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    public boolean comptabiliserRetenue(YvsGrhElementAdditionel y) {
        return comptabiliserRetenue(y, true);
    }

    public boolean comptabiliserRetenue(YvsGrhElementAdditionel y, boolean msg) {
        return comptabiliserRetenue(y, msg, true);
    }

    public boolean comptabiliserRetenue(YvsGrhElementAdditionel y, boolean msg, boolean succes) {
        return comptabiliserRetenue(y, buildRetenueToComptabilise(y.getId(), msg), msg, succes);
    }

    public boolean comptabiliserRetenue(YvsGrhElementAdditionel y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes) {
        ResultatAction result = service.comptabiliserRetenue(y, contenus);
        if (result != null) {
            if (result.isResult()) {
                YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) result.getData();
                int idx = listePiece.indexOf(p);
                if (idx > -1) {
                    listePiece.set(idx, p);
                } else {
                    listePiece.add(0, p);
                }
                if (succes) {
                    succes();
                }
            } else {
                if (msg) {
                    getErrorMessage(result.getMessage());
                }
            }
            return result.isResult();
        }
        return false;
    }

    private boolean comptabiliserRetenue(List<YvsComptaContentJournal> contenus, boolean msg) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
            ManagedRetenue w = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
            if (w != null) {
                boolean error = false;
                List<YvsComptaContentJournal> list;
                ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                YvsComptaJournaux jrn = null;
                if (j != null) {
                    int index = j.getJournaux().indexOf(new YvsComptaJournaux(journalPiece));
                    if (index > -1) {
                        jrn = j.getJournaux().get(index);
                    }
                }
                for (int idx : ids) {
                    YvsGrhElementAdditionel y = w.getListRetenues().get(idx);
                    list = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        if (c.getRefExterne().equals(y.getId())) {
                            list.add(c);
                        }
                    }
                    if (!list.isEmpty()) {
                        if (updatePiece) {
                            y.setDateElement(datePiece);
                        }
                        if (jrn != null) {
                            y.setJournal(jrn);
                        }
                        if (comptabiliserRetenue(y, list, false, false)) {
                            succes = true;
                        } else {
                            error = true;
                        }
                    }
                }
                docIds = "";
                if (error) {
                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
                }
                update("data_doc_piece_retenue");
            }
        } else {
            succes = comptabiliserRetenue(selectRetenue, contenus, msg, false);
        }
        return succes;
    }

    private YvsComptaPiecesComptable majComptaHeaderVente(long id, List<YvsComptaContentJournal> contenus, boolean save) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (true) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComEnteteDocVente y = (YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                List<Long> factures = dao.loadNameQueries("YvsComDocVentes.findIdByEnteteTypeDoc", new String[]{"entete", "typeDoc"}, new Object[]{y, Constantes.TYPE_FV});
                if (factures != null ? !factures.isEmpty() : false) {
//                    boolean comptabilise = dao.isComptabilise(factures, Constantes.SCR_VENTE);
//                    if (comptabilise) {
//                        if (true) {
//                            getErrorMessage("Comptabilisation impossible... car ce document est rattaché à des factures déjà comptabilisées");
//                        }
//                        return null;
//                    }
                }
                champ = new String[]{"agence", "type", "default"};
                val = new Object[]{y.getAgence(), Constantes.VENTE, true};
                YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateEntete(), jrn, contenus, true);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalEnteteFactureVente cp = new YvsComptaContentJournalEnteteFactureVente(y, p);
                    cp.setAuthor(currentUser);
                    cp.setId(null);
                    dao.save(cp);

                    YvsComptaContentJournalFactureVente c;
                    for (YvsComDocVentes d : y.getDocuments()) {
                        boolean comptabilise = dao.isComptabilise(d.getId(), Constantes.SCR_VENTE);
                        if (comptabilise) {
                            continue;
                        }
                        if (d.getTypeDoc().equals(Constantes.TYPE_FV) && d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                            if ((d.getTiers() != null ? (d.getTiers().getId() > 0 ? (d.getTiers().getCompte() != null ? d.getTiers().getCompte().getId() > 0 : false) : false) : false)) {
                                c = new YvsComptaContentJournalFactureVente(d, p);
                                c.setAuthor(currentUser);
                                c.setId(null);
                                y.setDateSave(new Date());
                                y.setDateUpdate(new Date());
                                dao.save(c);

                                for (YvsComptaCaissePieceVente r : d.getReglements()) {
                                    if (r.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                        comptabiliserCaisseVente(r, false, false);
                                    }
                                }
                                d.setComptabilise(true);
                            }
                        }
                    }
                    y.setComptabilise(true);
                    return p;
                } else {
                    getErrorMessage("Comptabilisation impossible...car le journal par defaut des ventes n'existe pas");
                }
            }
        }
        return null;
    }

    private YvsComptaJournaux giveJournalVente(YvsAgences agence) {
        YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComParametreVente.findJournalByAgence", new String[]{"agence"}, new Object[]{agence});
        if (jrn != null ? jrn.getId() < 1 : true) {
            champ = new String[]{"agence", "type", "default"};
            val = new Object[]{agence, Constantes.VENTE, true};
            jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
        }
        return jrn;
    }

    private YvsComptaJournaux giveJournalSalaire(YvsAgences agence) {
        champ = new String[]{"agence", "type", "default"};
        val = new Object[]{agence, Constantes.SALAIRE, true};
        return (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
    }

    private YvsComptaPiecesComptable majComptaVente(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                YvsComptaJournaux jrn = giveJournalVente(y.getEnteteDoc().getAgence());
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getEnteteDoc().getDateEntete(), jrn, contenus, msg);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalFactureVente c = new YvsComptaContentJournalFactureVente(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    List<YvsComptaCaissePieceVente> pieces = dao.loadNameQueries("YvsComptaCaissePieceVente.findByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                    for (YvsComptaCaissePieceVente r : pieces) {
                        comptabiliserCaisseVente(r, false, false);
                    }

                    //Debut du lettrage
                    lettrerVente(y, pieces);
                    y.setComptabilise(true);
                    return p;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible...car le journal par defaut des ventes n'existe pas");
                    }
                }
            }
        }
        return null;
    }

    public List<YvsComptaContentJournal> lettrerVente(YvsComDocVentes y) {
        List<YvsComptaCaissePieceVente> pieces = dao.loadNameQueries("YvsComptaCaissePieceVente.findByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
        return lettrerVente(y, pieces);
    }

    public List<YvsComptaContentJournal> lettrerVente(YvsComDocVentes y, List<YvsComptaCaissePieceVente> pieces) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        if (pieces != null ? !pieces.isEmpty() : false) {
            String isLettrer = null;
            List<YvsComptaContentJournal> credits, debits = null;
            YvsComptaAcompteClient acompte = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaNotifReglementVente.findAcompteByFactureNature", new String[]{"facture", "nature"}, new Object[]{y, 'D'});
            if ((acompte != null ? acompte.getId() < 1 : true)) {
                List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceVente.findIdByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                if (ids != null ? ids.isEmpty() : true) {
                    ids = new ArrayList<Long>() {
                        {
                            add(-1L);
                        }
                    };
                }
                credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_VENTE});
                if (credits != null ? !credits.isEmpty() : false) {
                    if (Util.asString(credits.get(0).getLettrage())) {
                        isLettrer = credits.get(0).getLettrage();
                    } else {
                        debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterne", new String[]{"id", "table"}, new Object[]{y.getId(), y.getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_AVOIR_VENTE : Constantes.SCR_VENTE});
                    }
                }
            } else {
                credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{acompte.getId(), Constantes.SCR_ACOMPTE_VENTE});
                if (credits != null ? !credits.isEmpty() : false) {
                    if (Util.asString(credits.get(0).getLettrage())) {
                        isLettrer = credits.get(0).getLettrage();
                    } else {
                        List<Long> ids = dao.loadNameQueries("YvsComptaNotifReglementVente.findIdFactureByAcompte", new String[]{"acompte"}, new Object[]{acompte});
                        if (ids != null ? ids.isEmpty() : true) {
                            ids = new ArrayList<Long>() {
                                {
                                    add(-1L);
                                }
                            };
                        }
                        debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_VENTE});
                    }
                }
            }
            if (debits != null ? !debits.isEmpty() : false) {
                if (Util.asString(debits.get(0).getLettrage())) {
                    isLettrer = debits.get(0).getLettrage();
                } else {
                    List<YvsComptaContentJournal> list = new ArrayList<>();
                    list.addAll(debits);
                    list.addAll(credits);
                    if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                        YvsBaseExercice exo = giveExercice(y.getEnteteDoc().getDateEntete());
                        if (exo != null ? exo.getId() < 1 : true) {
                            return null;
                        }
                        lettrageCompte(list, exo);
                        result.addAll(list);
                    }
                }
            }
            if (Util.asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentAgence.getSociete(), isLettrer});
            }
        }
        return result;
    }

    private YvsComptaJournaux giveJournalAchat(YvsAgences agence) {
        YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComParametreAchat.findJournalByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        if (jrn != null ? jrn.getId() < 1 : true) {
            champ = new String[]{"agence", "type", "default"};
            val = new Object[]{agence, Constantes.ACHAT, true};
            jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
        }
        return jrn;
    }

    private YvsComptaPiecesComptable majComptaAchat(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComDocAchats y = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                YvsComptaJournaux jrn = giveJournalAchat(y.getAgence());
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateDoc(), jrn, contenus, msg);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalFactureAchat c = new YvsComptaContentJournalFactureAchat(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    List<YvsComptaCaissePieceAchat> pieces = dao.loadNameQueries("YvsComptaCaissePieceAchat.findByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                    for (YvsComptaCaissePieceAchat r : pieces) {
                        comptabiliserCaisseAchat(r, false, false);
                    }

                    //Debut du lettrage
                    lettrerAchat(y, pieces);
                    y.setComptabilise(true);
                    return p;
                } else {
                    getErrorMessage("Comptabilisation impossible...car le journal par defaut des achats n'existe pas");
                }
            }
        }
        return null;
    }

    public List<YvsComptaContentJournal> lettrerAchat(YvsComDocAchats y) {
        List<YvsComptaCaissePieceAchat> pieces = dao.loadNameQueries("YvsComptaCaissePieceAchat.findByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
        return lettrerAchat(y, pieces);
    }

    public List<YvsComptaContentJournal> lettrerAchat(YvsComDocAchats y, List<YvsComptaCaissePieceAchat> pieces) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        if (pieces != null ? !pieces.isEmpty() : false) {
            String isLettrer = null;
            List<YvsComptaContentJournal> credits = null, debits = null;
            YvsComptaAcompteClient acompte = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaNotifReglementAchat.findAcompteByFactureNature", new String[]{"facture", "nature"}, new Object[]{y, 'D'});
            if ((acompte != null ? acompte.getId() < 1 : true) || true) {
                List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceAchat.findIdByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                if (ids != null ? ids.isEmpty() : true) {
                    ids = new ArrayList<Long>() {
                        {
                            add(-1L);
                        }
                    };
                }
                debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_ACHAT});
                if (debits != null ? !debits.isEmpty() : false) {
                    if (Util.asString(debits.get(0).getLettrage())) {
                        isLettrer = debits.get(0).getLettrage();
                    } else {
                        credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getId(), Constantes.SCR_ACHAT});
                    }
                }
            } else {
                debits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{acompte.getId(), Constantes.SCR_ACOMPTE_ACHAT});
                if (debits != null ? !debits.isEmpty() : false) {
                    if (Util.asString(debits.get(0).getLettrage())) {
                        isLettrer = debits.get(0).getLettrage();
                    } else {
                        List<Long> ids = dao.loadNameQueries("YvsComptaNotifReglementAchat.findIdFactureByAcompte", new String[]{"acompte"}, new Object[]{acompte});
                        if (ids != null ? ids.isEmpty() : true) {
                            ids = new ArrayList<Long>() {
                                {
                                    add(-1L);
                                }
                            };
                        }
                        credits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_ACHAT});
                    }
                }
            }
            if (credits != null ? !credits.isEmpty() : false) {
                if (Util.asString(credits.get(0).getLettrage())) {
                    isLettrer = credits.get(0).getLettrage();
                } else {
                    List<YvsComptaContentJournal> list = new ArrayList<>();
                    list.addAll(debits);
                    list.addAll(credits);
                    if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                        YvsBaseExercice exo = giveExercice(y.getDateDoc());
                        if (exo != null ? exo.getId() < 1 : true) {
                            return null;
                        }
                        lettrageCompte(list, exo);
                    }
                }
            }
            if (Util.asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentAgence.getSociete(), isLettrer});
            }
        }
        return result;
    }

    private YvsComptaPiecesComptable majComptaDivers(long id, List<YvsComptaContentJournal> contenus, List<YvsComptaAbonementDocDivers> abs, List<YvsComptaCentreDocDivers> secs, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, boolean msg) {
        YvsComptaPiecesComptable p = null;
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaCaisseDocDivers y = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                List<YvsComptaCaissePieceDivers> pieces = dao.loadNameQueries("YvsComptaCaissePieceDivers.findByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                if (y.getIdTiers() != null ? y.getIdTiers() < 1 : true) {
                    List<YvsComptaContentJournal> restes = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        int index = pieces.indexOf(new YvsComptaCaissePieceDivers(c.getRefExterne()));
                        if (index > -1) {
                            pieces.get(index).getContenus().add(c);
                        } else {
                            restes.add(c);
                        }
                    }
                    YvsComptaJournaux jrn;
                    for (YvsComptaCaissePieceDivers r : pieces) {
                        jrn = r.getCaisse().getJournal();
                        p = saveNewPieceComptable(r.getDateValider(), jrn, r.getContenus(), msg);
                        if (p != null ? p.getId() < 1 : true) {
                            return p;
                        }
                        YvsComptaContentJournalPieceDivers c = new YvsComptaContentJournalPieceDivers(r, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                    }
                    p = saveNewPieceComptable(p, restes);
                } else {
                    champ = new String[]{"agence", "type", "default"};
                    val = new Object[]{y.getAgence(), Constantes.TRESORERIE, true};
                    YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
                    if (jrn != null ? (jrn.getId() != null ? jrn.getId() < 1 : true) : true) {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible...car le journal par defaut de trésorerie n'existe pas");
                        }
                        return p;
                    }
                    p = saveNewPieceComptable(y.getDateDoc(), jrn, contenus, msg);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }

                    for (YvsComptaCaissePieceDivers r : pieces) {
                        comptabiliserCaisseDivers(r, false, false);
                    }
                }
                YvsComptaContentJournalDocDivers c = new YvsComptaContentJournalDocDivers(y, p);
                c.setAuthor(currentUser);
                c.setId(null);
                dao.save(c);

                //Debut du lettrage
                lettrerDivers(y, pieces);
                y.setComptabilise(true);
                for (YvsComptaAbonementDocDivers r : abs) {
                    comptabiliserAbonnementDivers(r, fonction.buildDiversToComptabilise(r, secs, taxs, couts, dao), false, false);
                }
            }
        }
        return p;
    }

    public List<YvsComptaContentJournal> lettrerDivers(YvsComptaCaisseDocDivers y) {
        List<YvsComptaCaissePieceDivers> pieces = dao.loadNameQueries("YvsComptaCaissePieceDivers.findByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
        return lettrerDivers(y, pieces);
    }

    public List<YvsComptaContentJournal> lettrerDivers(YvsComptaCaisseDocDivers y, List<YvsComptaCaissePieceDivers> pieces) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        if (pieces != null ? !pieces.isEmpty() : false) {
            String isLettrer = null;
            List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceDivers.findIdByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
            if (ids != null ? ids.isEmpty() : true) {
                ids = new ArrayList<Long>() {
                    {
                        add(-1L);
                    }
                };
            }
            List<YvsComptaContentJournal> debits = new ArrayList<>();
            List<YvsComptaContentJournal> credits = new ArrayList<>();
            if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_DIVERS});
            } else {
                debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_DIVERS});
            }
            if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE) ? (credits != null ? !credits.isEmpty() : false) : (debits != null ? !debits.isEmpty() : false)) {
                if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                    debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterne", new String[]{"id", "table"}, new Object[]{y.getId(), Constantes.SCR_DIVERS});
                    if (Util.asString(credits.get(0).getLettrage())) {
                        isLettrer = credits.get(0).getLettrage();
                    }
                } else {
                    credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getId(), Constantes.SCR_DIVERS});
                    if (Util.asString(debits.get(0).getLettrage())) {
                        isLettrer = debits.get(0).getLettrage();
                    }
                }
                if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE) ? !debits.isEmpty() : !credits.isEmpty()) {
                    if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                        if (Util.asString(debits.get(0).getLettrage())) {
                            isLettrer = debits.get(0).getLettrage();
                        }
                    } else {
                        if (Util.asString(credits.get(0).getLettrage())) {
                            isLettrer = credits.get(0).getLettrage();
                        }
                    }
                    if (!Util.asString(isLettrer)) {
                        List<YvsComptaContentJournal> list = new ArrayList<>();
                        list.addAll(debits);
                        list.addAll(credits);
                        if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                            YvsBaseExercice exo = giveExercice(y.getDateDoc());
                            if (exo != null ? exo.getId() < 1 : true) {
                                return null;
                            }
                            lettrageCompte(list, exo);
                        }
                    }
                }
            }
            if (Util.asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentAgence.getSociete(), isLettrer});
            }
        }
        return result;
    }

    private YvsComptaPiecesComptable majComptaSalaire(long idHeader, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsGrhOrdreCalculSalaire y = (YvsGrhOrdreCalculSalaire) dao.loadOneByNameQueries("YvsGrhOrdreCalculSalaire.findById", new String[]{"id"}, new Object[]{idHeader});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                champ = new String[]{"agence", "type", "default"};
                val = new Object[]{currentAgence, Constantes.SALAIRE, true};
                YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getFinMois(), jrn, contenus, msg);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalEnteteBulletin cp = new YvsComptaContentJournalEnteteBulletin(y, p);
                    cp.setAuthor(currentUser);
                    cp.setId(null);
                    dao.save(cp);

                    YvsComptaContentJournalBulletin c;
                    YvsComptaContentJournalPieceVente v;
                    List<YvsGrhDetailPrelevementEmps> retenues;
                    for (YvsGrhBulletins d : y.getBulletins()) {
                        c = new YvsComptaContentJournalBulletin(d, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);

                        champ = new String[]{"contrat", "debut", "fin"};
                        val = new Object[]{d.getContrat(), y.getDateDebutTraitement(), y.getDateFinTraitement()};
                        retenues = dao.loadNameQueries("YvsGrhDetailPrelevementEmps.findForVenteByDates", champ, val);
                        for (YvsGrhDetailPrelevementEmps r : retenues) {
                            if (r.getRetenue().getPiceReglement() != null ? r.getRetenue().getPiceReglement().getId() > 0 : false) {
                                v = new YvsComptaContentJournalPieceVente(r.getRetenue().getPiceReglement(), p);
                                v.setAuthor(currentUser);
                                v.setId(null);
                                dao.save(v);
                            }
                        }
                        d.setComptabilise(true);
                    }
                    y.setComptabilise(true);
                    return p;
                } else {
                    getErrorMessage("Comptabilisation impossible...car le journal par defaut des salaires n'existe pas");
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaBulletin(long bulletin, boolean msg) {
        return majComptaBulletin(bulletin, buildBulletinToComptabilise(bulletin, msg));
    }

    private YvsComptaPiecesComptable majComptaBulletin(long bulletin, List<YvsComptaContentJournal> contenus) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (true) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsGrhBulletins y = (YvsGrhBulletins) dao.loadOneByNameQueries("YvsGrhBulletins.findById", new String[]{"id"}, new Object[]{bulletin});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                champ = new String[]{"agence", "type", "default"};
                val = new Object[]{currentAgence, Constantes.SALAIRE, true};
                YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getEntete().getDateDebutTraitement(), jrn, contenus, true);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalBulletin c = new YvsComptaContentJournalBulletin(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    y.setComptabilise(true);
                    return p;
                } else {
                    getErrorMessage("Comptabilisation impossible...car le journal par defaut des salaires n'existe pas");
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaRetenue(long retenue, boolean msg) {
        return majComptaRetenue(retenue, buildRetenueToComptabilise(retenue, msg));
    }

    private YvsComptaPiecesComptable majComptaRetenue(long retenue, List<YvsComptaContentJournal> contenus) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (true) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsGrhElementAdditionel y = (YvsGrhElementAdditionel) dao.loadOneByNameQueries("YvsGrhElementAdditionel.findById", new String[]{"id"}, new Object[]{retenue});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                System.err.println("y.getContrat().getEmploye().getAgence() : " + y.getContrat().getEmploye().getAgence());
                YvsComptaJournaux jrn = giveJournalSalaire(y.getContrat().getEmploye().getAgence());
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateElement(), jrn, contenus, true);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalRetenueSalaire c = new YvsComptaContentJournalRetenueSalaire(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    y.setComptabilise(true);
                    return p;
                } else {
                    getErrorMessage("Comptabilisation impossible...car le journal par defaut des salaires n'existe pas");
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaCaisseVente(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaCaissePieceVente y = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getNotifs() != null ? y.getNotifs().getId() > 0 : false) {// Verification si le reglement est rattaché a un acompre
                    if (y.getNotifs().getAcompte() != null ? y.getNotifs().getAcompte().getId() > 0 : false) {
                        if (y.getNotifs().getAcompte().getNature().equals('D')) {// Verification si l'acompte est un dépot
                            boolean comptabilise = isComptabilise(y.getNotifs().getAcompte().getId(), Constantes.SCR_ACOMPTE_VENTE);
                            if (!comptabilise) {
                                if (msg) {
                                    getErrorMessage("Comptabilisation impossible... car l'acompte lié à cette pièce n'est pas comptabilisé");
                                }
                                return null;
                            }
                            if (y.getNotifs().getAcompte().getPieceContenu() != null ? y.getNotifs().getAcompte().getPieceContenu().getId() > 0 : false) {
                                YvsComptaPiecesComptable p = y.getNotifs().getAcompte().getPieceContenu().getPiece();
                                YvsComptaContentJournalPieceVente c = new YvsComptaContentJournalPieceVente(y, p);
                                c.setAuthor(currentUser);
                                c.setId(null);
                                dao.save(c);
                                y.setComptabilise(true);
                                y.setDateUpdate(new Date());
                                y.setAuthor(currentUser);
                                dao.update(y);
                                return p;
                            }
                            return null;
                        }
                    }
                }
                boolean deja = false;
                if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    List<YvsComptaPhasePiece> phases = dao.loadNameQueries("YvsComptaPhasePiece.findByPiece", new String[]{"piece"}, new Object[]{y});
                    if (phases != null ? !phases.isEmpty() : false) {
                        int correct = 0;
                        for (YvsComptaPhasePiece r : phases) {
                            comptabiliserPhaseCaisseVente(r, false, false);
                        }
                        deja = true;
                        if (correct == phases.size()) {
                            return new YvsComptaPiecesComptable(1L);
                        }
                    }
                }
                if (!deja) {
                    boolean noCaisse = y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_COMPENSATION) || y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_SALAIRE);
                    if (noCaisse ? true : (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false)) {
                        YvsComptaJournaux jrn;
                        switch (y.getModel().getTypeReglement()) {
                            case Constantes.MODE_PAIEMENT_COMPENSATION:
                                jrn = giveJournalVente(y.getVente().getEnteteDoc().getAgence());
                                break;
                            case Constantes.MODE_PAIEMENT_SALAIRE:
                                jrn = giveJournalSalaire(y.getVente().getEnteteDoc().getAgence());
                                break;
                            default:
                                jrn = y.getCaisse().getJournal();
                                break;
                        }
                        YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDatePaiement(), jrn, contenus, msg);
                        if (p != null ? p.getId() < 1 : true) {
                            return p;
                        }
                        YvsComptaContentJournalPieceVente c = new YvsComptaContentJournalPieceVente(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);

                        //Debut du lettrage
                        lettrerCaisseVente(y);
                        y.setComptabilise(true);
                        //Comptabilisation des compensation
                        if (y.getCompensations() != null ? !y.getCompensations().isEmpty() : false) {
                            for (YvsComptaCaissePieceCompensation o : y.getCompensations()) {
                                comptabiliserCaisseAchat(o.getAchat(), false, false);
                            }
                        }
                        return p;
                    } else {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<YvsComptaContentJournal> lettrerCaisseVente(YvsComptaCaissePieceVente y) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        String isLettrer = null;
        List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceVente.findIdByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y.getVente(), Constantes.STATUT_DOC_PAYER});
        if (ids != null ? !ids.isEmpty() : false) {
            List<YvsComptaContentJournal> credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternes", new String[]{"ids", "table"}, new Object[]{ids, y.getVente().getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_CAISSE_AVOIR_VENTE : Constantes.SCR_CAISSE_VENTE});
            if (credits != null ? !credits.isEmpty() : false) {
                if (Util.asString(credits.get(0).getLettrage())) {
                    isLettrer = credits.get(0).getLettrage();
                } else {
                    List<YvsComptaContentJournal> debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterne", new String[]{"id", "table"}, new Object[]{y.getVente().getId(), y.getVente().getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_AVOIR_VENTE : Constantes.SCR_VENTE});
                    if (!debits.isEmpty()) {
                        if (Util.asString(debits.get(0).getLettrage())) {
                            isLettrer = debits.get(0).getLettrage();
                        } else {
                            List<YvsComptaContentJournal> list = new ArrayList<>();
                            list.addAll(debits);
                            list.addAll(credits);
                            if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                                YvsBaseExercice exo = giveExercice(y.getDatePaiement());
                                if (exo != null ? exo.getId() < 1 : true) {
                                    return null;
                                }
                                lettrageCompte(list, exo);
                            }
                        }
                    }
                }
            }
            if (Util.asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentAgence.getSociete(), isLettrer});
            }
        }
        return result;
    }

    private YvsComptaPiecesComptable majComptaCaisseAchat(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaCaissePieceAchat y = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getNotifs() != null ? y.getNotifs().getId() > 0 : false) {// Verification si le reglement est rattaché a un acompre
                    if (y.getNotifs().getAcompte() != null ? y.getNotifs().getAcompte().getId() > 0 : false) {
                        if (y.getNotifs().getAcompte().getNature().equals('D')) {// Verification si l'acompte est un dépot
                            boolean comptabilise = isComptabilise(y.getNotifs().getAcompte().getId(), Constantes.SCR_ACOMPTE_ACHAT);
                            if (!comptabilise) {
                                if (msg) {
                                    getErrorMessage("Comptabilisation impossible... car l'acompte lié à cette pièce n'est pas comptabilisé");
                                }
                                return null;
                            }
                            if (y.getNotifs().getAcompte().getPieceContenu() != null ? y.getNotifs().getAcompte().getPieceContenu().getId() > 0 : false) {
                                YvsComptaPiecesComptable p = y.getNotifs().getAcompte().getPieceContenu().getPiece();
                                YvsComptaContentJournalPieceAchat c = new YvsComptaContentJournalPieceAchat(y, p);
                                c.setAuthor(currentUser);
                                c.setId(null);
                                dao.save(c);
                                y.setComptabilise(true);
                                return p;
                            }
                            return null;
                        }
                    }
                }
                boolean deja = false;
                if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    List<YvsComptaPhasePieceAchat> phases = dao.loadNameQueries("YvsComptaPhasePieceAchat.findByPiece", new String[]{"piece"}, new Object[]{y});
                    if (phases != null ? !phases.isEmpty() : false) {
                        int correct = 0;
                        for (YvsComptaPhasePieceAchat r : phases) {
                            comptabiliserPhaseCaisseAchat(r, false, false);
                        }
                        deja = true;
                        if (correct == phases.size()) {
                            return new YvsComptaPiecesComptable(1L);
                        }
                    }
                }
                if (!deja) {
                    if ((y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_COMPENSATION)) ? true : (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false)) {
                        YvsComptaJournaux jrn = (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_COMPENSATION)) ? giveJournalAchat(y.getAchat().getAgence()) : y.getCaisse().getJournal();
                        YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDatePaiement(), jrn, contenus, msg);
                        if (p != null ? p.getId() < 1 : true) {
                            return p;
                        }
                        YvsComptaContentJournalPieceAchat c = new YvsComptaContentJournalPieceAchat(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);

                        //Debut du lettrage
                        lettrerCaisseAchat(y);
                        y.setComptabilise(true);
                        //Comptabilisation des compensation
                        if (y.getCompensations() != null ? !y.getCompensations().isEmpty() : false) {
                            for (YvsComptaCaissePieceCompensation o : y.getCompensations()) {
                                comptabiliserCaisseVente(o.getVente(), false, false);
                            }
                        }
                        return p;
                    } else {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<YvsComptaContentJournal> lettrerCaisseAchat(YvsComptaCaissePieceAchat y) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        String isLettrer = null;
        List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceAchat.findIdByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y.getAchat(), Constantes.STATUT_DOC_PAYER});
        if (ids != null ? !ids.isEmpty() : false) {
            List<YvsComptaContentJournal> debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_ACHAT});
            if (debits != null ? !debits.isEmpty() : false) {
                if (Util.asString(debits.get(0).getLettrage())) {
                    isLettrer = debits.get(0).getLettrage();
                } else {
                    List<YvsComptaContentJournal> credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getAchat().getId(), Constantes.SCR_ACHAT});
                    if (!credits.isEmpty()) {
                        if (Util.asString(credits.get(0).getLettrage())) {
                            isLettrer = credits.get(0).getLettrage();
                        } else {
                            List<YvsComptaContentJournal> list = new ArrayList<>();
                            list.addAll(debits);
                            list.addAll(credits);
                            if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                                YvsBaseExercice exo = giveExercice(y.getDatePaiement());
                                if (exo != null ? exo.getId() < 1 : true) {
                                    return null;
                                }
                                lettrageCompte(list, exo);
                            }
                        }
                    }
                }
            }
            if (Util.asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentAgence.getSociete(), isLettrer});
            }
        }
        return result;
    }

    private YvsComptaPiecesComptable majComptaCaisseDivers(long id, List<YvsComptaContentJournal> contenus) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (true) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaCaissePieceDivers y = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus, true);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalPieceDivers c = new YvsComptaContentJournalPieceDivers(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    //Debut du lettrage
                    lettrerCaisseDivers(y);
                    y.setComptabilise(true);
                    return p;
                } else {
                    getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                }
            }
        }
        return null;
    }

    public List<YvsComptaContentJournal> lettrerCaisseDivers(YvsComptaCaissePieceDivers y) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        String isLettrer = null;
        List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceDivers.findIdByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y.getDocDivers(), Constantes.STATUT_DOC_PAYER});
        if (ids != null ? !ids.isEmpty() : false) {
            List<YvsComptaContentJournal> debits = new ArrayList<>();
            List<YvsComptaContentJournal> credits = new ArrayList<>();
            if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_DIVERS});
            } else {
                debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_DIVERS});
            }
            if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE) ? (credits != null ? !credits.isEmpty() : false) : (debits != null ? !debits.isEmpty() : false)) {
                if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                    debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterne", new String[]{"id", "table"}, new Object[]{y.getDocDivers().getId(), Constantes.SCR_DIVERS});
                    if (Util.asString(credits.get(0).getLettrage())) {
                        isLettrer = credits.get(0).getLettrage();
                    }
                } else {
                    credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getDocDivers().getId(), Constantes.SCR_DIVERS});
                    if (Util.asString(debits.get(0).getLettrage())) {
                        isLettrer = debits.get(0).getLettrage();
                    }
                }
                if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE) ? !debits.isEmpty() : !credits.isEmpty()) {
                    if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                        if (Util.asString(debits.get(0).getLettrage())) {
                            isLettrer = debits.get(0).getLettrage();
                        }
                    } else {
                        if (Util.asString(credits.get(0).getLettrage())) {
                            isLettrer = credits.get(0).getLettrage();
                        }
                    }
                    if (!Util.asString(isLettrer)) {
                        List<YvsComptaContentJournal> list = new ArrayList<>();
                        list.addAll(debits);
                        list.addAll(credits);
                        if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                            YvsBaseExercice exo = giveExercice(y.getDateValider());
                            if (exo != null ? exo.getId() < 1 : true) {
                                return null;
                            }
                            lettrageCompte(list, exo);
                        }
                    }
                }
            }
            if (Util.asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentAgence.getSociete(), isLettrer});
            }
        }
        return result;
    }

    private YvsComptaPiecesComptable majComptaAbonnementDivers(long id, List<YvsComptaContentJournal> contenus) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (true) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaAbonementDocDivers y = (YvsComptaAbonementDocDivers) dao.loadOneByNameQueries("YvsComptaAbonementDocDivers.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                champ = new String[]{"agence", "type", "default"};
                val = new Object[]{y.getDocDivers().getAgence(), Constantes.TRESORERIE, true};
                YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getEcheance(), jrn, contenus, true);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalAbonnementDivers c = new YvsComptaContentJournalAbonnementDivers(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);
                    y.setComptabilise(true);
                    return p;
                } else {
                    getErrorMessage("Comptabilisation impossible... le journal de tresorerie n'est pas paramétré");
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaAcompteFournisseur(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaAcompteFournisseur y = (YvsComptaAcompteFournisseur) dao.loadOneByNameQueries("YvsComptaAcompteFournisseur.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    boolean deja = false;
                    if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                        List<YvsComptaPhaseAcompteAchat> phases = dao.loadNameQueries("YvsComptaPhaseAcompteAchat.findByPiece", new String[]{"piece"}, new Object[]{y});
                        if (phases != null ? !phases.isEmpty() : false) {
                            int correct = 0;
                            for (YvsComptaPhaseAcompteAchat r : phases) {
                                if (comptabiliserPhaseAcompteAchat(r, false, false)) {
                                    correct++;
                                }
                            }
                            deja = true;
                            if (correct == phases.size()) {
                                return new YvsComptaPiecesComptable(1L);
                            }
                        }
                    }
                    if (!deja) {
                        if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                            YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDatePaiement(), y.getCaisse().getJournal(), contenus, msg);
                            if (p != null ? p.getId() < 1 : true) {
                                return p;
                            }
                            YvsComptaContentJournalAcompteFournisseur c = new YvsComptaContentJournalAcompteFournisseur(y, p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);

                            //Debut du lettrage
                            lettrerAcompteFournisseur(y);
                            y.setComptabilise(true);
                            return p;
                        } else {
                            if (msg) {
                                getErrorMessage("Comptabilisation impossible...car cet acompte n'est associé à aucune caisse");
                            }
                        }
                    }
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible...car cet acompte n'est associé à aucune caisse");
                    }
                }
            }
        }
        return null;
    }

    public List<YvsComptaContentJournal> lettrerAcompteFournisseur(YvsComptaAcompteFournisseur y) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        String isLettrer = null;
        if (y.getNature().equals('D')) {
            List<YvsComptaContentJournal> credits = null, debits = null;
            debits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getId(), Constantes.SCR_ACOMPTE_ACHAT});
            if (debits != null ? !debits.isEmpty() : false) {
                if (Util.asString(debits.get(0).getLettrage())) {
                    isLettrer = debits.get(0).getLettrage();
                } else {
                    List<Long> ids = dao.loadNameQueries("YvsComptaNotifReglementAchat.findIdFactureByAcompte", new String[]{"acompte"}, new Object[]{y});
                    if (ids != null ? ids.isEmpty() : true) {
                        ids = new ArrayList<Long>() {
                            {
                                add(-1L);
                            }
                        };
                    }
                    credits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_ACHAT});
                }
            }
            if (credits != null ? !credits.isEmpty() : false) {
                if (Util.asString(credits.get(0).getLettrage())) {
                    isLettrer = credits.get(0).getLettrage();
                } else {
                    List<YvsComptaContentJournal> list = new ArrayList<>();
                    list.addAll(debits);
                    list.addAll(credits);
                    if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                        YvsBaseExercice exo = giveExercice(y.getDatePaiement());
                        if (exo != null ? exo.getId() < 1 : true) {
                            return null;
                        }
                        lettrageCompte(list, exo);
                    }
                }
            }
            if (Util.asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentAgence.getSociete(), isLettrer});
            }
        }
        return result;
    }

    private YvsComptaPiecesComptable majComptaAcompteClient(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaAcompteClient y = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaAcompteClient.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    boolean deja = false;
                    if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                        List<YvsComptaPhaseAcompteVente> phases = dao.loadNameQueries("YvsComptaPhaseAcompteVente.findByPiece", new String[]{"piece"}, new Object[]{y});
                        if (phases != null ? !phases.isEmpty() : false) {
                            int correct = 0;
                            for (YvsComptaPhaseAcompteVente r : phases) {
                                if (comptabiliserPhaseAcompteVente(r, false, false)) {
                                    correct++;
                                }
                            }
                            deja = true;
                            if (correct == phases.size()) {
                                return new YvsComptaPiecesComptable(1L);
                            }
                        }
                    }
                    if (!deja) {
                        if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                            YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDatePaiement(), y.getCaisse().getJournal(), contenus, msg);
                            if (p != null ? p.getId() < 1 : true) {
                                return p;
                            }
                            YvsComptaContentJournalAcompteClient c = new YvsComptaContentJournalAcompteClient(y, p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);

                            //Debut du lettrage
                            lettrerAcompteClient(y);
                            y.setComptabilise(true);
                            return p;
                        } else {
                            if (msg) {
                                getErrorMessage("Comptabilisation impossible...car cet acompte n'est associé à aucune caisse");
                            }
                        }
                    }
                } else {
                    getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                }
            }
        }
        return null;
    }

    public List<YvsComptaContentJournal> lettrerAcompteClient(YvsComptaAcompteClient y) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        String isLettrer = null;
        List<YvsComptaContentJournal> credits, debits = null;
        credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getId(), Constantes.SCR_ACOMPTE_VENTE});
        if (credits != null ? !credits.isEmpty() : false) {
            if (Util.asString(credits.get(0).getLettrage())) {
                isLettrer = credits.get(0).getLettrage();
            } else {
                nameQueri = y.getNature().equals('D') ? "YvsComptaNotifReglementVente.findIdFactureByAcompte" : "YvsComptaNotifReglementVente.findIdPieceByAcompte";
                List<Long> ids = dao.loadNameQueries(nameQueri, new String[]{"acompte"}, new Object[]{y});
                if (ids != null ? ids.isEmpty() : true) {
                    ids = new ArrayList<Long>() {
                        {
                            add(-1L);
                        }
                    };
                }
                debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, y.getNature().equals('D') ? Constantes.SCR_VENTE : Constantes.SCR_CAISSE_VENTE});
            }
        }
        if (debits != null ? !debits.isEmpty() : false) {
            if (Util.asString(debits.get(0).getLettrage())) {
                isLettrer = debits.get(0).getLettrage();
            } else {
                List<YvsComptaContentJournal> list = new ArrayList<>();
                list.addAll(debits);
                list.addAll(credits);
                if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                    YvsBaseExercice exo = giveExercice(y.getDatePaiement());
                    if (exo != null ? exo.getId() < 1 : true) {
                        return null;
                    }
                    lettrageCompte(list, exo);
                }
            }
        }
        if (Util.asString(isLettrer)) {
            result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentAgence.getSociete(), isLettrer});
        }
        return result;
    }

    private YvsComptaPiecesComptable majComptaCreditFournisseur(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaCreditFournisseur y = (YvsComptaCreditFournisseur) dao.loadOneByNameQueries("YvsComptaCreditFournisseur.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getJournal() != null ? (y.getJournal().getId() != null ? y.getJournal().getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateCredit(), y.getJournal(), contenus, msg);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalCreditFournisseur c = new YvsComptaContentJournalCreditFournisseur(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);
                    if (contenus != null ? !contenus.isEmpty() : false) {
                        List<YvsComptaReglementCreditFournisseur> pieces = dao.loadNameQueries("YvsComptaReglementCreditFournisseur.findByCreditStatut", new String[]{"credit", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                        for (YvsComptaReglementCreditFournisseur r : pieces) {
                            comptabiliserCaisseCreditAchat(r, false, false);
                        }
                    }
                    y.setComptabilise(true);
                    return p;
                } else {
                    getErrorMessage("Comptabilisation impossible...car ce document n'est pas rattaché à un journal");
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaCreditClient(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaCreditClient y = (YvsComptaCreditClient) dao.loadOneByNameQueries("YvsComptaCreditClient.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getJournal() != null ? (y.getJournal().getId() != null ? y.getJournal().getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateCredit(), y.getJournal(), contenus, msg);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalCreditClient c = new YvsComptaContentJournalCreditClient(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);
                    if (contenus != null ? !contenus.isEmpty() : false) {
                        List<YvsComptaReglementCreditClient> pieces = dao.loadNameQueries("YvsComptaReglementCreditClient.findByCreditStatut", new String[]{"credit", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                        for (YvsComptaReglementCreditClient r : pieces) {
                            comptabiliserCaisseCreditVente(r, false, false);
                        }
                    }
                    y.setComptabilise(true);
                    return p;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible...car l'exercice à cette date n'existe pas ou n'est pas actif");
                    }
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaCaisseVirement(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() ? contenus.size() > 1 : false : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaCaissePieceVirement y = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getSource() != null ? (y.getSource().getId() != null ? y.getSource().getId() > 0 : false) : false) {
                    if (y.getCible() != null ? (y.getCible().getId() != null ? y.getCible().getId() > 0 : false) : false) {
                        //récupération du journal
                        List<YvsComptaContentJournal> sources = new ArrayList<>();
                        List<YvsComptaContentJournal> cibles = new ArrayList<>();
                        for (YvsComptaContentJournal c : contenus) {
                            if (c.getNumero().equals(1)) {
                                c.setNumero(c.getDebit() > 0 ? 1 : 2);
                                cibles.add(c);
                            } else {
                                c.setNumero(c.getDebit() > 0 ? 1 : 2);
                                sources.add(c);
                            }
                        }
                        boolean soumis = y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) ? (!dao.isComptabilise(id, Constantes.SCR_VIREMENT, true, Constantes.MOUV_CAISS_SORTIE.charAt(0))) : y.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS);
                        if (y.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS) || soumis) {
                            YvsComptaJournaux journal = y.getSource().getJournal();
                            YvsComptaPiecesComptable p2 = saveNewPieceComptable(y.getDatePaimentPrevu(), journal, sources, msg);
                            if (p2 != null ? p2.getId() < 1 : true) {
                                return p2;
                            }
                            YvsComptaContentJournalPieceVirement c = new YvsComptaContentJournalPieceVirement(y, p2);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            c.setSensCompta(yvs.dao.salaire.service.Constantes.MOUV_CAISS_SORTIE.charAt(0));
                            dao.save(c);
                            if (y.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS)) {
                                return p2;
                            }
                        }
                        if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                            YvsComptaJournaux journal = y.getCible().getJournal();
                            YvsComptaPiecesComptable p1 = saveNewPieceComptable(y.getDatePaiement(), journal, cibles, msg);
                            if (p1 != null ? p1.getId() < 1 : true) {
                                return p1;
                            }
                            YvsComptaContentJournalPieceVirement c = new YvsComptaContentJournalPieceVirement(y, p1);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            c.setSensCompta(yvs.dao.salaire.service.Constantes.MOUV_CAISS_ENTREE.charAt(0));
                            dao.save(c);
                            //Debut du lettrage
                            lettrerVirement(y);
                            y.setComptabilise(true);
                            return p1;
                        }
                    } else {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune destinataire");
                        }
                    }
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune emetteur");
                    }
                }
            }
        }
        return null;
    }

    public List<YvsComptaContentJournal> lettrerVirement(YvsComptaCaissePieceVirement y) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        try {
            String isLettrer = null;
            if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                List<YvsComptaContentJournal> credits, debits = null;
                YvsComptaContentJournalPieceVirement sortie = (YvsComptaContentJournalPieceVirement) dao.loadOneByNameQueries("YvsComptaContentJournalPieceVirement.findByFactureSource", new String[]{"reglement", "source"}, new Object[]{y, yvs.dao.salaire.service.Constantes.MOUV_CAISS_SORTIE.charAt(0)});
                if (sortie != null ? sortie.getId() < 1 : true) {
                    return result;
                }
                credits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitPiece", new String[]{"piece"}, new Object[]{sortie.getPiece()});
                if (credits != null ? !credits.isEmpty() : false) {
                    if (Util.asString(credits.get(0).getLettrage())) {
                        isLettrer = credits.get(0).getLettrage();
                    } else {
                        YvsComptaContentJournalPieceVirement entree = (YvsComptaContentJournalPieceVirement) dao.loadOneByNameQueries("YvsComptaContentJournalPieceVirement.findByFactureSource", new String[]{"reglement", "source"}, new Object[]{y, yvs.dao.salaire.service.Constantes.MOUV_CAISS_ENTREE.charAt(0)});
                        if (entree != null ? entree.getId() < 1 : true) {
                            return result;
                        }
                        debits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditPiece", new String[]{"piece"}, new Object[]{entree.getPiece()});
                    }
                }
                if (debits != null ? !debits.isEmpty() : false) {
                    if (Util.asString(debits.get(0).getLettrage())) {
                        isLettrer = debits.get(0).getLettrage();
                    } else {
                        List<YvsComptaContentJournal> list = new ArrayList<>();
                        list.addAll(debits);
                        list.addAll(credits);
                        if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                            YvsBaseExercice exo = giveExercice(y.getDatePaiement());
                            if (exo != null ? exo.getId() < 1 : true) {
                                return null;
                            }
                            lettrageCompte(list, exo);
                        }
                    }
                }
                if (Util.asString(isLettrer)) {
                    result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentAgence.getSociete(), isLettrer});
                }
            }
        } catch (Exception ex) {
            getException("lettrerVirement", ex);
        }
        return result;
    }

    //id= id pièce de caisse mision
    private YvsComptaPiecesComptable majComptaCaisseMission(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            //1. trouvons la pièce de caisse qui correspond à cette mission
            YvsComptaCaissePieceMission y = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findById", new String[]{"id"}, new Object[]{id});
            if (controleComptabiliseMission(y, msg, false, true)) {
                YvsBaseExercice exo = giveExerciceActif(y.getDatePaiement());
                if (exo != null ? (exo.getId() != null ? exo.getId() > 0 : false) : false) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(y.getDatePaiement());
                    //équilibre de la pièce
                    //Construire la pièce
                    YvsComptaPiecesComptable pc = new YvsComptaPiecesComptable();
                    pc.setAuthor(currentUser);
                    pc.setId(0L);
                    pc.setDatePiece(y.getDatePaiement());
                    pc.setDateSaisie(new Date());
                    pc.setExercice(exo);
                    pc.setJournal(y.getCaisse().getJournal());
                    String numRef = genererReference(Constantes.TYPE_PIECE_COMPTABLE_NAME, pc.getDatePiece(), y.getCaisse().getJournal().getId(), Constantes.JOURNAL);
                    if (numRef == null || numRef.trim().length() < 1) {
                        return null;
                    }
                    pc.setNumPiece(numRef);
                    pc.setStatutPiece(Constantes.STATUT_DOC_EDITABLE);
                    pc.getContentsPiece().addAll(contenus);

                    YvsComptaPiecesComptable p = savePieceComptable(pc);
                    if (p != null ? p.getId() > 0 : false) {
                        YvsComptaContentJournalPieceMission cj = new YvsComptaContentJournalPieceMission(y, p);
                        cj.setAuthor(currentUser);
                        dao.save(cj);
                        y.setComptabilise(true);
                    }
                    return p;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible...car l'exercice à cette date n'existe pas ou n'est pas actif");
                    }
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaCaisseCreditAchat(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaReglementCreditFournisseur y = (YvsComptaReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaReglementCreditFournisseur.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                boolean deja = false;
                if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    List<YvsComptaPhaseReglementCreditFournisseur> phases = dao.loadNameQueries("YvsComptaPhaseReglementCreditFournisseur.findByPiece", new String[]{"piece"}, new Object[]{y});
                    if (phases != null ? !phases.isEmpty() : false) {
                        int correct = 0;
                        for (YvsComptaPhaseReglementCreditFournisseur r : phases) {
                            comptabiliserPhaseCaisseCreditAchat(r, false, false);
                        }
                        deja = true;
                        if (correct == phases.size()) {
                            return new YvsComptaPiecesComptable(1L);
                        }
                    }
                }
                if (!deja) {
                    if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                        YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDatePaiement(), y.getCaisse().getJournal(), contenus, msg);
                        if (p != null ? p.getId() < 1 : true) {
                            return p;
                        }
                        YvsComptaContentJournalReglementCreditFournisseur c = new YvsComptaContentJournalReglementCreditFournisseur(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);

                        y.setComptabilise(true);
                        //Debut du lettrage
                        List<Long> ids = dao.loadNameQueries("YvsComptaReglementCreditFournisseur.findIdByCreditStatut", new String[]{"credit", "statut"}, new Object[]{y.getCredit(), Constantes.STATUT_DOC_PAYER});
                        if (ids != null ? !ids.isEmpty() : false) {
                            List<YvsComptaContentJournal> debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_CREDIT_ACHAT});
                            if (debits != null ? !debits.isEmpty() : false) {
                                if (debits.get(0).getLettrage() != null ? debits.get(0).getLettrage().trim().length() > 0 : false) {
                                    return p;
                                }
                                List<YvsComptaContentJournal> credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getCredit().getId(), Constantes.SCR_CREDIT_ACHAT});
                                if (!credits.isEmpty()) {
                                    if (credits.get(0).getLettrage() != null ? credits.get(0).getLettrage().trim().length() > 0 : false) {
                                        return p;
                                    }
                                    List<YvsComptaContentJournal> list = new ArrayList<>();
                                    list.addAll(debits);
                                    list.addAll(credits);
                                    if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                                        YvsBaseExercice exo = giveExercice(y.getDatePaiement());
                                        if (exo != null ? exo.getId() < 1 : true) {
                                            return null;
                                        }
                                        lettrageCompte(list, exo);
                                    }
                                }
                            }
                        }
                        return p;
                    } else {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                        }
                    }
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaCaisseCreditVente(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaReglementCreditClient y = (YvsComptaReglementCreditClient) dao.loadOneByNameQueries("YvsComptaReglementCreditClient.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                boolean deja = false;
                if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    List<YvsComptaPhaseReglementCreditClient> phases = dao.loadNameQueries("YvsComptaPhaseReglementCreditClient.findByPiece", new String[]{"piece"}, new Object[]{y});
                    if (phases != null ? !phases.isEmpty() : false) {
                        int correct = 0;
                        for (YvsComptaPhaseReglementCreditClient r : phases) {
                            comptabiliserPhaseCaisseCreditVente(r, false, false);
                            correct++;
                        }
                        deja = true;
                        if (correct == phases.size()) {
                            return new YvsComptaPiecesComptable(1L);
                        }
                    }
                }
                if (!deja) {
                    if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                        YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDatePaiement(), y.getCaisse().getJournal(), contenus, msg);
                        if (p != null ? p.getId() < 1 : true) {
                            return p;
                        }
                        YvsComptaContentJournalReglementCreditClient c = new YvsComptaContentJournalReglementCreditClient(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);

                        y.setComptabilise(true);
                        //Debut du lettrage
                        List<Long> ids = dao.loadNameQueries("YvsComptaReglementCreditClient.findIdByCreditStatut", new String[]{"credit", "statut"}, new Object[]{y.getCredit(), Constantes.STATUT_DOC_PAYER});
                        if (ids != null ? !ids.isEmpty() : false) {
                            List<YvsComptaContentJournal> credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_CREDIT_VENTE});
                            if (credits != null ? !credits.isEmpty() : false) {
                                if (credits.get(0).getLettrage() != null ? credits.get(0).getLettrage().trim().length() > 0 : false) {
                                    return p;
                                }
                                List<YvsComptaContentJournal> debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterne", new String[]{"id", "table"}, new Object[]{y.getCredit().getId(), Constantes.SCR_CREDIT_VENTE});
                                if (!debits.isEmpty()) {
                                    if (debits.get(0).getLettrage() != null ? debits.get(0).getLettrage().trim().length() > 0 : false) {
                                        return p;
                                    }
                                    List<YvsComptaContentJournal> list = new ArrayList<>();
                                    list.addAll(debits);
                                    list.addAll(credits);
                                    if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                                        YvsBaseExercice exo = giveExercice(y.getDatePaiement());
                                        if (exo != null ? exo.getId() < 1 : true) {
                                            return null;
                                        }
                                        lettrageCompte(list, exo);
                                    }
                                }
                            }
                        }
                        return p;
                    } else {
                        if (msg) {
                            getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                        }
                    }
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaEtapeCaisseVente(long id, boolean msg) {
        return majComptaEtapeCaisseVente(id, buildEtapeCaisseVenteToComptabilise(id, msg), msg);
    }

    private YvsComptaPiecesComptable majComptaEtapeCaisseVente(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaPhasePiece y = (YvsComptaPhasePiece) dao.loadOneByNameQueries("YvsComptaPhasePiece.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus, msg);
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhasePiece n = (YvsComptaPhasePiece) dao.loadOneByNameQueries("YvsComptaPhasePiece.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getPieceVente(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalPieceVente c = new YvsComptaContentJournalPieceVente(new YvsComptaCaissePieceVente(y.getPieceVente().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapePieceVente c = new YvsComptaContentJournalEtapePieceVente(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return p;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                    }
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaEtapeAcompteVente(long id, boolean msg) {
        return majComptaEtapeAcompteVente(id, buildEtapeAcompteVenteToComptabilise(id, msg), msg);
    }

    private YvsComptaPiecesComptable majComptaEtapeAcompteVente(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaPhaseAcompteVente y = (YvsComptaPhaseAcompteVente) dao.loadOneByNameQueries("YvsComptaPhaseAcompteVente.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus, msg);
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhaseAcompteVente n = (YvsComptaPhaseAcompteVente) dao.loadOneByNameQueries("YvsComptaPhaseAcompteVente.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getPieceVente(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalAcompteClient c = new YvsComptaContentJournalAcompteClient(new YvsComptaAcompteClient(y.getPieceVente().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapeAcompteVente c = new YvsComptaContentJournalEtapeAcompteVente(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return p;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                    }
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaEtapeCaisseAchat(long id, boolean msg) {
        return majComptaEtapeCaisseAchat(id, buildEtapeCaisseAchatToComptabilise(id, msg), msg);
    }

    private YvsComptaPiecesComptable majComptaEtapeCaisseAchat(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaPhasePieceAchat y = (YvsComptaPhasePieceAchat) dao.loadOneByNameQueries("YvsComptaPhasePieceAchat.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus, msg);
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhasePieceAchat n = (YvsComptaPhasePieceAchat) dao.loadOneByNameQueries("YvsComptaPhasePieceAchat.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getPieceAchat(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalPieceAchat c = new YvsComptaContentJournalPieceAchat(new YvsComptaCaissePieceAchat(y.getPieceAchat().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapePieceAchat c = new YvsComptaContentJournalEtapePieceAchat(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return p;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                    }
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaEtapeCaisseDivers(long id, boolean msg) {
        return majComptaEtapeCaisseDivers(id, buildEtapeCaisseDiversToComptabilise(id, msg), msg);
    }

    private YvsComptaPiecesComptable majComptaEtapeCaisseDivers(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaPhasePieceDivers y = (YvsComptaPhasePieceDivers) dao.loadOneByNameQueries("YvsComptaPhasePieceDivers.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus, msg);
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhasePieceDivers n = (YvsComptaPhasePieceDivers) dao.loadOneByNameQueries("YvsComptaPhasePieceDivers.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getPieceDivers(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalPieceDivers c = new YvsComptaContentJournalPieceDivers(new YvsComptaCaissePieceDivers(y.getPieceDivers().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapePieceDivers c = new YvsComptaContentJournalEtapePieceDivers(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return p;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                    }
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaEtapeCaisseVirement(long id, boolean msg) {
        return majComptaEtapeCaisseVirement(id, buildEtapeCaisseVirementToComptabilise(id, msg), msg);
    }

    private YvsComptaPiecesComptable majComptaEtapeCaisseVirement(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaPhasePieceVirement y = (YvsComptaPhasePieceVirement) dao.loadOneByNameQueries("YvsComptaPhasePieceVirement.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus, msg);
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhasePieceVirement n = (YvsComptaPhasePieceVirement) dao.loadOneByNameQueries("YvsComptaPhasePieceVirement.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getVirement(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalPieceVirement c = new YvsComptaContentJournalPieceVirement(new YvsComptaCaissePieceVirement(y.getVirement().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapePieceVirement c = new YvsComptaContentJournalEtapePieceVirement(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return p;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                    }
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaEtapeAcompteAchat(long id, boolean msg) {
        return majComptaEtapeAcompteAchat(id, buildEtapeAcompteAchatToComptabilise(id, msg), msg);
    }

    private YvsComptaPiecesComptable majComptaEtapeAcompteAchat(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaPhaseAcompteAchat y = (YvsComptaPhaseAcompteAchat) dao.loadOneByNameQueries("YvsComptaPhaseAcompteAchat.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus, msg);
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhaseAcompteAchat n = (YvsComptaPhaseAcompteAchat) dao.loadOneByNameQueries("YvsComptaPhaseAcompteAchat.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getPieceAchat(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalAcompteFournisseur c = new YvsComptaContentJournalAcompteFournisseur(new YvsComptaAcompteFournisseur(y.getPieceAchat().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapeAcompteAchat c = new YvsComptaContentJournalEtapeAcompteAchat(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return p;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                    }
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaEtapeCaisseCreditAchat(long id, boolean msg) {
        return majComptaEtapeCaisseCreditAchat(id, buildEtapeCaisseCreditAchatToComptabilise(id, msg), msg);
    }

    private YvsComptaPiecesComptable majComptaEtapeCaisseCreditAchat(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaPhaseReglementCreditFournisseur y = (YvsComptaPhaseReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaPhaseReglementCreditFournisseur.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus, msg);
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhaseReglementCreditFournisseur n = (YvsComptaPhaseReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaPhaseReglementCreditFournisseur.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getReglement(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalReglementCreditFournisseur c = new YvsComptaContentJournalReglementCreditFournisseur(new YvsComptaReglementCreditFournisseur(y.getReglement().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapeReglementCreditFournisseur c = new YvsComptaContentJournalEtapeReglementCreditFournisseur(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return p;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                    }
                }
            }
        }
        return null;
    }

    private YvsComptaPiecesComptable majComptaEtapeCaisseCreditVente(long id, boolean msg) {
        return majComptaEtapeCaisseCreditVente(id, buildEtapeCaisseCreditVenteToComptabilise(id, msg), msg);
    }

    private YvsComptaPiecesComptable majComptaEtapeCaisseCreditVente(long id, List<YvsComptaContentJournal> contenus, boolean msg) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaPhaseReglementCreditClient y = (YvsComptaPhaseReglementCreditClient) dao.loadOneByNameQueries("YvsComptaPhaseReglementCreditClient.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus, msg);
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhaseReglementCreditClient n = (YvsComptaPhaseReglementCreditClient) dao.loadOneByNameQueries("YvsComptaPhaseReglementCreditClient.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getReglement(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalReglementCreditClient c = new YvsComptaContentJournalReglementCreditClient(new YvsComptaReglementCreditClient(y.getReglement().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapeReglementCreditClient c = new YvsComptaContentJournalEtapeReglementCreditClient(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return p;
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                    }
                }
            }
        }
        return null;
    }

    //id= id pièce de caisse mision
    private List<YvsComptaContentJournal> buildMissionToComptabilise(long id, boolean msg) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        //1. trouvons la pièce de caisse qui correspond à cette mission
        YvsComptaCaissePieceMission pm = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findById", new String[]{"id"}, new Object[]{id});
        if (controleComptabiliseMission(pm, msg, false, false)) {
            list = buildContentJournal(id, Constantes.SCR_FRAIS_MISSIONS, msg);
        }
        return list;
    }

    private List<YvsComptaContentJournal> buildEtapeCaisseVenteToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_PHASE_CAISSE_VENTE, msg);
    }

    private List<YvsComptaContentJournal> buildEtapeAcompteVenteToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_PHASE_ACOMPTE_VENTE, msg);
    }

    private List<YvsComptaContentJournal> buildEtapeCaisseCreditVenteToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_PHASE_CREDIT_VENTE, msg);
    }

    private List<YvsComptaContentJournal> buildEtapeCaisseAchatToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_PHASE_CAISSE_ACHAT, msg);
    }

    private List<YvsComptaContentJournal> buildEtapeCaisseDiversToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_PHASE_CAISSE_DIVERS, msg);
    }

    private List<YvsComptaContentJournal> buildEtapeCaisseVirementToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_PHASE_VIREMENT, msg);
    }

    private List<YvsComptaContentJournal> buildEtapeAcompteAchatToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_PHASE_ACOMPTE_ACHAT, msg);
    }

    private List<YvsComptaContentJournal> buildEtapeCaisseCreditAchatToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_PHASE_CREDIT_ACHAT, msg);
    }

    private List<YvsComptaContentJournal> buildCaisseVenteToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_CAISSE_VENTE, msg);
    }

    private List<YvsComptaContentJournal> buildCaisseAchatToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_CAISSE_ACHAT, msg);
    }

    private List<YvsComptaContentJournal> buildCaisseCreditVenteToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_CAISSE_CREDIT_VENTE, msg);
    }

    private List<YvsComptaContentJournal> buildCaisseCreditAchatToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_CAISSE_CREDIT_ACHAT, msg);
    }

    private List<YvsComptaContentJournal> buildAcompteClientToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_ACOMPTE_VENTE, msg);
    }

    private List<YvsComptaContentJournal> buildAcompteFournisseurToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_ACOMPTE_ACHAT, msg);
    }

    private List<YvsComptaContentJournal> buildCreditClientToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_CREDIT_VENTE, msg);
    }

    private List<YvsComptaContentJournal> buildCreditFournisseurToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_CREDIT_ACHAT, msg);
    }

    private List<YvsComptaContentJournal> buildCaisseDiversToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_CAISSE_DIVERS, msg);
    }

    private List<YvsComptaContentJournal> buildCaisseVirementToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_VIREMENT, msg);
    }

    private List<YvsComptaContentJournal> buildCaisseMissionToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_VIREMENT, msg);
    }

    private List<YvsComptaContentJournal> buildRetenueToComptabilise(long id, boolean msg) {
        return buildContentJournal(id, Constantes.SCR_RETENUE, msg);
    }

    private List<YvsComptaContentJournal> buildHeaderVenteToComptabilise(long id, boolean msg) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        YvsComEnteteDocVente y = (YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{id});
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            y.setFactures(dao.loadNameQueries("YvsComDocVentes.findByEnteteTypeDoc", new String[]{"entete", "typeDoc"}, new Object[]{y, Constantes.TYPE_FV}));
            if (y.getFactures() != null ? !y.getFactures().isEmpty() : false) {
                if (y.canComptabilise()) {
                    list = buildContentJournal(id, Constantes.SCR_HEAD_VENTE, msg);
                } else {
                    if (msg) {
                        getErrorMessage("Comptabilisation impossible... Car ce journal de vente de vente n'est rattaché à aucune facture valide");
                    }
                    return null;
                }
            } else {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible... Car ce journal de vente de vente n'est rattaché à aucune facture");
                }
                return null;
            }
        } else {
            if (msg) {
                getErrorMessage("Comptabilisation impossible... Car ce journal de vente de vente n'existe pas");
            }
            return null;
        }
        return list;
    }

    private List<YvsComptaContentJournal> buildVenteToComptabilise(long id, boolean msg) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{id});
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            // Verification si les articles de la facture sont parametré avec la categorie comptable de la facture
            String req = "SELECT DISTINCT(co.article) FROM yvs_com_contenu_doc_vente co INNER JOIN yvs_com_doc_ventes dv ON co.doc_vente = dv.id "
                    + "WHERE dv.id = ? AND co.article NOT IN (select distinct(ca.article) "
                    + "from yvs_base_article_categorie_comptable ca where dv.categorie_comptable = ca.categorie)";
            List<Object> ids = dao.loadListBySqlQuery(req, new Options[]{new Options(id, 1)});
            if (ids != null ? !ids.isEmpty() : false) {
                Object o = ids.get(0);
                if (o != null) {
                    YvsBaseArticles a = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{(Long) o});
                    if (a != null ? (a.getId() != null ? a.getId() > 0 : false) : false) {
                        if (msg) {
                            getErrorMessage("Cette facture est rattachée à l'article " + a.getDesignation() + " qui n'est pas rattaché à la catégorie de cette facture");
                        }
                    }
                }
                return null;
            }
            list = buildContentJournal(id, y.getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_AVOIR_VENTE : Constantes.SCR_VENTE, msg);
        } else {
            if (msg) {
                getErrorMessage("Comptabilisation impossible... Car cette facture de vente n'existe pas");
            }
            return null;
        }
        return list;
    }

    private List<YvsComptaContentJournal> buildAchatToComptabilise(long id, boolean msg) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        YvsComDocAchats y = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{id});
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            // Verification si les articles de la facture sont parametré avec la categorie comptable de la facture
            String req = "select distinct(co.article) from yvs_com_contenu_doc_achat co "
                    + "inner join yvs_com_doc_achats dv on co.doc_achat = dv.id "
                    + "where dv.id = ? and co.article not in (select distinct(ca.article) "
                    + "from yvs_base_article_categorie_comptable ca where dv.categorie_comptable = ca.categorie)";
            List<Object> ids = dao.loadListBySqlQuery(req, new Options[]{new Options(id, 1)});
            if (ids != null ? !ids.isEmpty() : false) {
                Object o = ids.get(0);
                if (o != null) {
                    YvsBaseArticles a = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{(Long) o});
                    if (a != null ? (a.getId() != null ? a.getId() > 0 : false) : false) {
                        if (msg) {
                            getErrorMessage("Cette facture est rattachée à l'article " + a.getDesignation() + " qui n'est pas rattaché à la catégorie de cette facture");
                        }
                    }
                }
                return null;
            }
            list = buildContentJournal(id, y.getTypeDoc().equals(Constantes.TYPE_FAA) ? Constantes.SCR_AVOIR_ACHAT : Constantes.SCR_ACHAT, msg);
        } else {
            if (msg) {
                getErrorMessage("Comptabilisation impossible... Car cette facture d'achat n'existe pas");
            }
            return null;
        }
        return list;
    }

    private List<YvsComptaContentJournal> buildAvoirAchatToComptabilise(long id, boolean msg) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        YvsComDocAchats y = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{id});
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            // Verification si les articles de la facture sont parametré avec la categorie comptable de la facture
            String req = "select distinct(co.article) from yvs_com_contenu_doc_achat co "
                    + "inner join yvs_com_doc_achats dv on co.doc_achat = dv.id "
                    + "where dv.id = ? and co.article not in (select distinct(ca.article) "
                    + "from yvs_base_article_categorie_comptable ca where dv.categorie_comptable = ca.categorie)";
            List<Object> ids = dao.loadListBySqlQuery(req, new Options[]{new Options(id, 1)});
            if (ids != null ? !ids.isEmpty() : false) {
                Object o = ids.get(0);
                if (o != null) {
                    YvsBaseArticles a = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{(Long) o});
                    if (a != null ? (a.getId() != null ? a.getId() > 0 : false) : false) {
                        if (msg) {
                            getErrorMessage("Cet avoir est rattachée à l'article " + a.getDesignation() + " qui n'est pas rattaché à la catégorie de cette facture");
                        }
                    }
                }
                return null;
            }
            list = buildContentJournal(id, Constantes.SCR_AVOIR_ACHAT, msg);
        } else {
            if (msg) {
                getErrorMessage("Comptabilisation impossible... Car cet avoir sur achat n'existe pas");
            }
            return null;
        }
        return list;
    }

    private List<YvsComptaContentJournal> buildSalaireToComptabilise(long id, boolean msg) {
        return buildSalaireToComptabilise(id, agencesSelect, actionAgence, msg);
    }

    private List<YvsComptaContentJournal> buildSalaireToComptabilise(long id, List<YvsAgences> agences, boolean action, boolean msg) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        YvsGrhOrdreCalculSalaire y = (YvsGrhOrdreCalculSalaire) dao.loadOneByNameQueries("YvsGrhOrdreCalculSalaire.findById", new String[]{"id"}, new Object[]{id});
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getBulletins() != null ? !y.getBulletins().isEmpty() : false) {
                list = buildContentJournal(id, Constantes.SCR_SALAIRE, agences, action, msg);
            } else {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible... Car cet ordre de calcul des salaires n'est rattaché à aucun bulletin");
                }
                return null;
            }
        } else {
            if (msg) {
                getErrorMessage("Comptabilisation impossible... Car cet ordre de calcul des salaires n'existe pas");
            }
            return null;
        }
        return list;
    }

    private List<YvsComptaContentJournal> buildBulletinToComptabilise(long id, boolean msg) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        YvsGrhBulletins y = (YvsGrhBulletins) dao.loadOneByNameQueries("YvsGrhBulletins.findById", new String[]{"id"}, new Object[]{id});
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getStatut() == Constantes.STATUT_DOC_VALIDE) {
                list = buildContentJournal(id, Constantes.SCR_BULLETIN, msg);
            } else {
                if (msg) {
                    getErrorMessage("Comptabilisation impossible... Car ce bulletin n'est pas validé");
                }
                return null;
            }
        } else {
            if (msg) {
                getErrorMessage("Comptabilisation impossible... Car ce bulletin n'existe pas");
            }
            return null;
        }
        return list;
    }

    private List<YvsComptaContentJournal> buildContentJournal(long id, String table, boolean msg) {
        return buildContentJournal(id, table, null, false, msg);
    }

    private List<YvsComptaContentJournal> buildContentJournal(long id, String table, List<YvsAgences> agences, boolean action, boolean msg) {
        if (table.equals(Constantes.SCR_DIVERS)) {
            return fonction.buildDiversToComptabilise(id, dao);
        } else {
            ResultatAction result = service.buildContentJournal(id, table, agences, action);
            if (result != null ? !result.isResult() : true) {
                if (msg) {
                    getErrorMessage(result != null ? result.getMessage() : "Action impossible!!!");
                }
                return null;
            }
            return result.getListContent();
        }
    }

    private List<YvsComptaContentJournal> buildContentJournal(String ids, String table, boolean msg) {
        return buildContentJournal(ids, table, null, false, msg);
    }

    private List<YvsComptaContentJournal> buildContentJournal(String ids, String table, List<YvsAgences> agences, boolean action, boolean msg) {
        if (table.equals(Constantes.SCR_DIVERS)) {
            List<YvsComptaContentJournal> list = new ArrayList<>();
            for (String id : ids.split("-")) {
                List<YvsComptaContentJournal> result = fonction.buildDiversToComptabilise(Long.valueOf(id), dao);
                if (result != null) {
                    list.addAll(result);
                }
            }
            return list;
        } else {
            ResultatAction result = service.buildContentJournal(ids, table, agences, action);
            if (result != null ? !result.isResult() : true) {
                if (msg) {
                    getErrorMessage(result != null ? result.getMessage() : "Action impossible!!!");
                }
                return null;
            }
            return result.getListContent();
        }
    }

    private List<YvsComptaContentAnalytique> buildAnalytiqueToComptabilise(TempContent t, long entete, long compte, double valeur, boolean debit, boolean retenue, boolean salarial, boolean saisie_tiers) {
        return service.buildAnalytiqueToComptabilise(t, entete, compte, valeur, debit, retenue, salarial, saisie_tiers);
    }

    private List<YvsComptaContentJournal> buildElementGainToComptabilise(List<Object[]> data, int jour, boolean sum, long entete) {
        ResultatAction result = service.buildElementGainToComptabilise(data, jour, sum, entete);
        if (result != null ? !result.isResult() : true) {
            getErrorMessage(result != null ? result.getMessage() : "Action impossible!!!");
            return null;
        }
        return result.getListContent();
    }

    private List<YvsComptaContentJournal> buildElementRetenueToComptabilise(List<Object[]> data, int jour, boolean salarial, boolean saisie_tiers, boolean sum, long entete) {
        return buildElementRetenueToComptabilise(data, jour, salarial, saisie_tiers, sum, false, entete);
    }

    private List<YvsComptaContentJournal> buildElementRetenueToComptabilise(List<Object[]> data, int jour, boolean salarial, boolean saisie_tiers, boolean sum, boolean others, long entete) {
        ResultatAction result = service.buildElementRetenueToComptabilise(data, jour, salarial, saisie_tiers, sum, others, entete);
        if (result != null ? !result.isResult() : true) {
            getErrorMessage(result != null ? result.getMessage() : "Action impossible!!!");
            return null;
        }
        return result.getListContent();
    }

    public void equilibreDocComptabilise() {
        //cas des virements
        String rq = "UPDATE yvs_compta_caisse_piece_virement SET comptabilise=true WHERE id IN(SELECT ref_externe FROM yvs_compta_content_journal WHERE table_externe=?)";
        dao.requeteLibre(rq, new Options[]{new Options(Constantes.SCR_VIREMENT, 1)});
        rq = "UPDATE yvs_compta_caisse_piece_virement SET comptabilise=false WHERE id NOT IN(SELECT ref_externe FROM yvs_compta_content_journal WHERE table_externe=?)";
        dao.requeteLibre(rq, new Options[]{new Options(Constantes.SCR_VIREMENT, 1)});
        //cas des mission
        rq = "UPDATE yvs_compta_caisse_piece_mission SET comptabilise=true WHERE id IN(SELECT ref_externe FROM yvs_compta_content_journal WHERE table_externe=?)";
        dao.requeteLibre(rq, new Options[]{new Options(Constantes.SCR_FRAIS_MISSIONS, 1)});
        rq = "UPDATE yvs_compta_caisse_piece_mission SET comptabilise=false WHERE id NOT IN(SELECT ref_externe FROM yvs_compta_content_journal WHERE table_externe=?)";
        dao.requeteLibre(rq, new Options[]{new Options(Constantes.SCR_FRAIS_MISSIONS, 1)});
    }
    /*
     *END COMPTABILISER PIECE
     */

    public void loadOnExport(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsStatExportEtat y = (YvsStatExportEtat) ev.getObject();
            model = y.getReference();
        }
    }

    public void unLoadOnExport(UnselectEvent ev) {
        model = null;
    }

    public void loadExport() {
        champ = new String[]{"code", "societe"};
        val = new Object[]{Constantes.EXPORT_CONTENU_JOURNAL, currentAgence.getSociete()};
        exports = dao.loadNameQueries("YvsStatExportEtat.findByCode", champ, val);
        update("data_model_export_compta");
    }

    public void export(boolean force) {
        List<Long> ids = new ArrayList<>();
        List<Integer> re = decomposeSelection(tabIds);
        for (Integer i : re) {
            ids.add(listePiece.get(i).getId());
        }
        if (ids != null ? !ids.isEmpty() : false) {
            if (force) {
                if (model != null ? model.trim().length() > 0 : false) {
                    executeExport(model, ids);
                } else {
                    getErrorMessage("Vous devez selectionner le model d'exportation");
                }
            } else {
                loadExport();
                if (exports != null ? !exports.isEmpty() : false) {
                    if (exports.size() > 1) {
                        openDialog("dlgListExport");
                    } else {
                        model = exports.get(0).getReference();
                        executeExport(model, ids);
                    }
                }
            }
        } else {
            getErrorMessage("Vous devez selectionner des pièces");
        }
    }

    public boolean isComptabilise(long id, String table) {
        return dao.isComptabilise(id, table);
    }

    public void addParamJournal() {
//        ParametreRequete p = new ParametreRequete("y.journal", "journal", null, "=", "AND");
        ParametreRequete p = new ParametreRequete("y.piece.journal", "journal", null, "=", "AND");
        if (journalSearch > 0) {
            p.setObjet(new YvsComptaJournaux(journalSearch));
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true);
    }

    public void addParamExercice() {
//        ParametreRequete p = new ParametreRequete("y.exercice", "exercice", null, "=", "AND");
        ParametreRequete p = new ParametreRequete("y.piece.exercice", "exercice", null, "=", "AND");
        if (exerciceSearch > 0) {
            p.setObjet(new YvsBaseExercice(exerciceSearch));
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true);
    }

    public void addParamNumero() {
//        ParametreRequete p = new ParametreRequete("y.numPiece", "numPiece", null, "=", "AND");
        ParametreRequete p = new ParametreRequete("y.piece.numPiece", "numPiece", null, "=", "AND");
        if (numeroSearch != null ? numeroSearch.trim().length() > 0 : false) {
//            p = new ParametreRequete("y.numPiece", "numPiece", numeroSearch + "%", "LIKE", "AND");
            p = new ParametreRequete("y.piece.numPiece", "numPiece", numeroSearch + "%", "LIKE", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true);
    }

    public void addParamWithLiaison() {
//        ParametreRequete p = new ParametreRequete("y.id", "withLiaison", null, "=", "AND");
        ParametreRequete p = new ParametreRequete("y.piece.id", "withLiaison", null, "=", "AND");
        if (withLiaisonSearch != null) {
            List<Long> ids = dao.loadNameQueries("YvsComptaContentJournal.findIdPiece", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            if (ids.isEmpty()) {
                ids.add(-1L);
            }
//            p = new ParametreRequete("y.id", "withLiaison", ids, (withLiaisonSearch ? "IN" : "NOT IN"), "AND");
            p = new ParametreRequete("y.piece.id", "withLiaison", ids, (withLiaisonSearch ? "IN" : "NOT IN"), "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true);
    }

    public void addParamDate(SelectEvent ev) {
        findByDate();
    }

    public void findByDate() {
//        ParametreRequete p = new ParametreRequete("y.datePiece", "date", null, "BETWEEN", "AND");
        ParametreRequete p = new ParametreRequete("y.piece.datePiece", "date", null, "BETWEEN", "AND");
        if (dateSearch) {
            if (debutSearch != null && finSearch != null) {
                p = new ParametreRequete(null, "date", debutSearch, finSearch, "BETWEEN", "AND");
//                p.getOtherExpression().add(new ParametreRequete("y.dateSaisie", "date", debutSearch, finSearch, "BETWEEN", "OR"));
//                p.getOtherExpression().add(new ParametreRequete("y.datePiece", "date", debutSearch, finSearch, "BETWEEN", "OR"));
                p.getOtherExpression().add(new ParametreRequete("y.piece.dateSaisie", "date", debutSearch, finSearch, "BETWEEN", "OR"));
                p.getOtherExpression().add(new ParametreRequete("y.piece.datePiece", "date", debutSearch, finSearch, "BETWEEN", "OR"));
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true);
    }

    public void addParamDateSave(SelectEvent ev) {
        findByDateSave();
    }

    public void findByDateSave() {
        ParametreRequete p = new ParametreRequete("y.piece.dateSaisie", "dateSaisie", null, "BETWEEN", "AND");
        if (dateSaveSearch) {
            if (debutSaveSearch != null && finSaveSearch != null) {
                p = new ParametreRequete("y.piece.dateSaisie", "date", debutSaveSearch, finSaveSearch, "BETWEEN", "AND");
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true);
    }

    public void addParamJournalContent() {
        ParametreRequete p = new ParametreRequete("y.piece.journal", "journal", null, "=", "AND");
        if (journalContentSearch > 0) {
            p = new ParametreRequete("y.piece.journal", "journal", new YvsComptaJournaux(journalContentSearch), "=", "AND");
        }
        p_contenu.addParam(p);
        loadAllContenu(true, true);
    }

    public void addParamCompteContent() {
        ParametreRequete p = new ParametreRequete("y.compteGeneral", "compte", null, "=", "AND");
        if (operation != null ? "L".equals(operation) : false) {
            if (compteSearch != null ? compteSearch.trim().length() > 0 : false) {
                p = new ParametreRequete("y.compteGeneral.numCompte", "compte", compteSearch, "=", "AND");
            }
            p_contenu.addParam(p);
            loadAllLettrage(true, true);
        } else {
            if (compteSearch != null ? compteSearch.trim().length() > 0 : false) {
                p = new ParametreRequete("y.compteGeneral.numCompte", "compte", compteSearch + "%", "LIKE", "AND");
            }
            p_contenu.addParam(p);
            loadAllContenu(true, true);
        }
    }

    public void addParamCompteContentForPiece() {
        ParametreRequete p = new ParametreRequete("y.compteGeneral", "compte", null, "=", "AND");
        if (compteContenuSearch != null ? compteContenuSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.compteGeneral.numCompte", "compte", compteContenuSearch + "%", "LIKE", "AND");
        }
        paginatorContenu.addParam(p);
        loadAllContenuForPiece(true, true);
    }

    public void parcoursInAllCompte(boolean avancer) {
        ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (w != null) {
            w.setOffset((avancer) ? (w.getOffset() + 1) : (w.getOffset() - 1));
            if (w.getOffset() < 0 || w.getOffset() >= (w.paginator.getNbPage() * w.getNbMax())) {
                w.setOffset(0);
            }
            List<YvsBasePlanComptable> re = w.paginator.parcoursDynamicData("YvsBasePlanComptable", "y", "y.numCompte", w.getOffset(), dao);
            if (!re.isEmpty()) {
                onSelectCompte(re.get(0));
            }
        }
    }

    public void parcoursInAllTiers(boolean avancer) {
        ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (w != null) {
            w.setOffset((avancer) ? (w.getOffset() + 1) : (w.getOffset() - 1));
            if (w.getOffset() < 0 || w.getOffset() >= (w.paginator.getNbPage() * w.getNbMax())) {
                w.setOffset(0);
            }
            List<YvsBaseTiers> re = w.paginator.parcoursDynamicData("YvsBaseTiers", "y", "y.codeTiers,y.nom, y.prenom", w.getOffset(), dao);
            if (!re.isEmpty()) {
                onSelectTiers(re.get(0));
            }
        }
    }

    public void choisirCompteChange(SelectEvent ev) {
        if (ev != null) {
            cloneObject(compteChange, UtilCompta.buildSimpleBeanCompte(((YvsBasePlanComptable) ev.getObject())));
            update("bloc_compte_change");
        }
    }

    public void ecouteSaisierCompteChange() {
        String code = compteChange.getNumCompte();
        if (code != null ? code.trim().length() > 0 : false) {
            //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
            ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (service != null) {
                service.findCompteByNum(compteChange.getNumCompte());
                compteChange.setError(service.getListComptes().isEmpty());
                if (service.getListComptes() != null) {
                    if (!service.getListComptes().isEmpty()) {
                        if (service.getListComptes().size() == 1) {
                            compteChange.setError(false);
                            cloneObject(compteChange, UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                        } else {
                            compteChange.setError(false);
                            openDialog("dlgCmpteG");
                            update("table_cpt_compte_change");
                        }
                    } else {
                        compteChange.setError(true);
                    }
                } else {
                    compteChange.setError(true);
                }
            }
        }
    }

    public void onSelectCompte(YvsBasePlanComptable y) {
        ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (w != null) {
            w.setSelectCompte(y);
        }
        compteSearch = y.getNumCompte();
        selectCompte = y;
        addParamCompteContent();
        update("value-compte_search_lettrage");
    }

    public void onSelectTiers(YvsBaseTiers y) {
        ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (w != null) {
            w.setSelectTiers(y);
        }
        tiersSearch = y.getCodeTiers();
        addParamTiersContent();
        update("value-tiers_search_lettrage");
    }

    public void chooseJournalChange() {
        try {
            if (journalChange != null ? journalChange.getId() > 0 : false) {
                ManagedJournaux w = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                if (w != null) {
                    int index = w.getJournaux().indexOf(new YvsComptaJournaux(journalChange.getId()));
                    if (index > -1) {
                        journalChange = UtilCompta.buildBeanJournaux(w.getJournaux().get(index));
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("chooseJournalChange", ex);
        }
    }

    public void chooseProfilLetter(SelectEvent ev) {
        if (ev != null) {
            Profil y = (Profil) ev.getObject();
            this.profilFilter = y;
            ParametreRequete p = new ParametreRequete(null, "tiers", tiersSearch, "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.compteTiers", "tiers", y.getType().equals(yvs.dao.salaire.service.Constantes.BASE_TIERS_EMPLOYE) ? y.getIdTiers() : y.getId(), "=", "AND"));
            p.getOtherExpression().add(new ParametreRequete("y.tableTiers", "tableTiers", y.getTableTiers(), "=", "AND"));
            p_contenu.addParam(p);
            loadAllLettrage(true, true);
        }
    }

    public void changeCompteForContent() {
        try {
            if (!autoriser("compta_change_compte_content")) {
                openNotAcces();
                return;
            }
            if ((selectContent != null ? selectContent.getId() < 1 : true) && (tabIds_contenu != null ? tabIds_contenu.trim().length() < 1 : true)) {
                getErrorMessage("Vous devez selectionner une ou plusieurs opération(s)");
                return;
            }
            if (compteChange != null ? compteChange.getId() < 1 : true) {
                getErrorMessage("Vous devez choisir le compte de remplacement");
                return;
            }
            if (selectContent != null ? selectContent.getId() > 0 : false) {
                changeCompteForContent(selectContent, compteChange, true);
            } else {
                List<Integer> ids = decomposeSelection(tabIds_contenu);
                for (Integer index : ids) {
                    changeCompteForContent(contenus.get(index), compteChange, false);
                }
                update("data_contenu_piece");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("changeCompteForContent", ex);
        }
    }

    public void changeJournalForPiece() {
        try {
            if (!autoriser("compta_change_compte_content")) {
                openNotAcces();
                return;
            }
            if ((selectPiece != null ? selectPiece.getId() < 1 : true) && (tabIds != null ? tabIds.trim().length() < 1 : true)) {
                getErrorMessage("Vous devez selectionner une ou plusieurs pièces");
                return;
            }
            if (journalChange != null ? journalChange.getId() < 1 : true) {
                getErrorMessage("Vous devez choisir le journal de remplacement");
                return;
            }
            if (selectPiece != null ? selectPiece.getId() > 0 : false) {
                changeJournalForPiece(selectPiece, journalChange, true);
            } else {
                List<Integer> ids = decomposeSelection(tabIds);
                for (Integer index : ids) {
                    changeJournalForPiece(listePiece.get(index), journalChange, false);
                }
                update("table_pc_comptable");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("changeCompteForContent", ex);
        }
    }

    private void changeCompteForContent(YvsComptaContentJournal y, Comptes compte, boolean msg) {
        try {
            y.setCompteGeneral(new YvsBasePlanComptable(compte.getId(), compte.getNumCompte(), compte.getIntitule()));
            y.setAuthor(currentUser);
            y.setDateUpdate(new Date());
            dao.update(y);
            int index = contenus.indexOf(y);
            if (index > -1) {
                contenus.set(index, y);
            }
            if (msg) {
                update("data_contenu_piece");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("changeCompteForContent", ex);
        }
    }

    private void changeJournalForPiece(YvsComptaPiecesComptable y, Journaux journal, boolean msg) {
        try {
            y.setJournal(new YvsComptaJournaux(journal.getId(), journal.getCodejournal(), journal.getIntitule()));
            y.setAuthor(currentUser);
            y.setDateUpdate(new Date());
            dao.update(y);
            int index = listePiece.indexOf(y);
            if (index > -1) {
                listePiece.set(index, y);
            }
            if (msg) {
                update("table_pc_comptable");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("changeCompteForContent", ex);
        }
    }

    private ParametreRequete getParamTiersContent(String tiersSearch) {
        ParametreRequete p = new ParametreRequete("y.compteTiers", "tiers", null, "=", "AND");
        if (tiersSearch == null || tiersSearch.trim().isEmpty()) {
            return p;
        }
        //Recherche de la liste des tiers correspondant au code entré
        List<YvsBaseTiers> list = dao.loadNameQueries("YvsBaseTiers.findLikeCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), tiersSearch.toUpperCase() + "%"});
        if (list != null ? !list.isEmpty() : false) {
            List<Long> clients = new ArrayList<>();
            List<Long> fournisseurs = new ArrayList<>();
            List<Long> tiers = new ArrayList<>();
            Tiers y;
            Profil profil;
            for (YvsBaseTiers entity : list) {
                y = UtilTiers.buildBeanTiers(entity);
                if (y.getProfils().size() > 0) {
                    profil = findOneProfil(y, tiersSearch);
                    if (profil == null) {// S'il n'existe pas un seul profil on ajoute tous les profils dans la liste des paramètres
                        for (int j = 0; j < y.getProfils().size(); j++) {
                            profil = y.getProfils().get(j);
                            switch (profil.getType()) {
                                case yvs.dao.salaire.service.Constantes.BASE_TIERS_TIERS:
                                    tiers.add(profil.getId());
                                    break;
                                case yvs.dao.salaire.service.Constantes.BASE_TIERS_CLIENT:
                                    clients.add(profil.getId());
                                    break;
                                case yvs.dao.salaire.service.Constantes.BASE_TIERS_FOURNISSEUR:
                                    fournisseurs.add(profil.getId());
                                    break;
                                case yvs.dao.salaire.service.Constantes.BASE_TIERS_EMPLOYE:
                                    tiers.add(profil.getIdTiers());
                                    break;
                            }
                        }
                    } else {// S'il existe un seul profil on ajoute le profil dans la liste des parametres
                        switch (profil.getType()) {
                            case yvs.dao.salaire.service.Constantes.BASE_TIERS_TIERS:
                                tiers.add(profil.getId());
                                break;
                            case yvs.dao.salaire.service.Constantes.BASE_TIERS_CLIENT:
                                clients.add(profil.getId());
                                break;
                            case yvs.dao.salaire.service.Constantes.BASE_TIERS_FOURNISSEUR:
                                fournisseurs.add(profil.getId());
                                break;
                            case yvs.dao.salaire.service.Constantes.BASE_TIERS_EMPLOYE:
                                tiers.add(profil.getIdTiers());
                                break;
                        }
                    }
                }
            }
            if (tiers.isEmpty() && clients.isEmpty() && fournisseurs.isEmpty()) {
                p = new ParametreRequete("y.compteTiers", "tiers", -1, "=", "AND");
            } else {
                p = new ParametreRequete(null, "tiers", tiersSearch, "=", "AND");
                if (!tiers.isEmpty()) {
                    ParametreRequete pp = new ParametreRequete(null, "idTiers", tiersSearch, "=", "OR");
                    pp.getOtherExpression().add(new ParametreRequete("y.compteTiers", "idTiers", tiers, "IN", "AND"));
                    pp.getOtherExpression().add(new ParametreRequete("y.tableTiers", "tableTiers", yvs.dao.salaire.service.Constantes.BASE_TIERS_TIERS, "=", "AND"));
                    p.getOtherExpression().add(pp);
                }
                if (!clients.isEmpty()) {
                    ParametreRequete pp = new ParametreRequete(null, "idClients", tiersSearch, "=", "OR");
                    pp.getOtherExpression().add(new ParametreRequete("y.compteTiers", "idClients", clients, "IN", "AND"));
                    pp.getOtherExpression().add(new ParametreRequete("y.tableTiers", "tableTiers", yvs.dao.salaire.service.Constantes.BASE_TIERS_CLIENT, "=", "AND"));
                    p.getOtherExpression().add(pp);
                }
                if (!fournisseurs.isEmpty()) {
                    ParametreRequete pp = new ParametreRequete(null, "idFsseurs", tiersSearch, "=", "OR");
                    pp.getOtherExpression().add(new ParametreRequete("y.compteTiers", "idFsseurs", fournisseurs, "IN", "AND"));
                    pp.getOtherExpression().add(new ParametreRequete("y.tableTiers", "tableTiers", yvs.dao.salaire.service.Constantes.BASE_TIERS_FOURNISSEUR, "=", "AND"));
                    p.getOtherExpression().add(pp);
                }
            }
        } else {
            p = new ParametreRequete("y.compteTiers", "tiers", -1, "=", "AND");
        }
        return p;
    }

    public void addParamTiersContent() {
        ParametreRequete p = new ParametreRequete("y.compteTiers", "tiers", null, "=", "AND");
        if (operation != null ? "L".equals(operation) : false) {
            if (tiersSearch != null ? tiersSearch.trim().length() > 0 : false) {
                //Recherche du tiers correspondant au code entré
                YvsBaseTiers y = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseTiers.findByCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), tiersSearch});
                tiersLetter = UtilTiers.buildBeanTiers(y);
                if (tiersLetter.getProfils().size() > 0) {
                    //Recupere le profil correspondant au code entré
                    this.profilFilter = findOneProfil(tiersLetter, tiersSearch);
                    if (this.profilFilter == null) {// S'il n'existe pas un seul profil on ouvre la boite de dialogue des profil
                        openDialog("dlgProfilTiers");
                        update("table_profils_tiers_letter");
                        return;
                    } else {// S'il existe un seul profil on ajoute le profil dans la liste des parametres
                        p = new ParametreRequete(null, "tiers", tiersSearch, "=", "AND");
                        p.getOtherExpression().add(new ParametreRequete("y.compteTiers", "tiers", this.profilFilter.getType().equals(yvs.dao.salaire.service.Constantes.BASE_TIERS_EMPLOYE) ? this.profilFilter.getIdTiers() : this.profilFilter.getId(), "=", "AND"));
                        p.getOtherExpression().add(new ParametreRequete("y.tableTiers", "tableTiers", this.profilFilter.getTableTiers(), "=", "AND"));
                    }
                } else {
                    p = new ParametreRequete("y.compteTiers", "tiers", 0, "=", "AND");
                }
            }
            p_contenu.addParam(p);
            loadAllLettrage(true, true);
        } else {
            p = getParamTiersContent(tiersSearch);
            p_contenu.addParam(p);
            loadAllContenu(true, true);
        }
    }

    public void addParamTiersContentForPiece() {
        ParametreRequete p = getParamTiersContent(tiersContenuSearch);
        paginatorContenu.addParam(p);
        loadAllContenuForPiece(true, true);
    }

    public void addParamReferenceContent() {
        ParametreRequete p = new ParametreRequete("y.numRef", "numRef", null, "=", "AND");
        if (referenceSearch != null ? referenceSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "numRef", referenceSearch + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numRef)", "numRef", referenceSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numPiece)", "numRef", referenceSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        if (operation != null ? "L".equals(operation) : false) {
            loadAllLettrage(true, true);
        } else {
            loadAllContenu(true, true);
        }
    }

    public void addParamMontantContent() {
        ParametreRequete p = new ParametreRequete("y.credit", "credit", null, "=", "AND");
        if (montantSearch != null ? montantSearch > 0 : false) {
            p = new ParametreRequete(null, "credit", montantSearch, "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.credit", "credit", montantSearch, "=", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.debit", "debit", montantSearch, "=", "OR"));
        }
        p_contenu.addParam(p);
        if (operation != null ? "L".equals(operation) : false) {
            loadAllLettrage(true, true);
        } else {
            loadAllContenu(true, true);
        }
    }

    public void addParamDebitContentForPiece() {
        ParametreRequete p = new ParametreRequete("y.debit", "debit", null, "=", "AND");
        if (debitContenuSearch != null ? debitContenuSearch > -1 : false) {
            p = new ParametreRequete("y.debit", "credit", debitContenuSearch, "=", "AND");
        }
        paginatorContenu.addParam(p);
        loadAllContenuForPiece(true, true);
    }

    public void addParamCreditContentForPiece() {
        ParametreRequete p = new ParametreRequete("y.credit", "credit", null, "=", "AND");
        if (creditContenuSearch != null ? creditContenuSearch > -1 : false) {
            p = new ParametreRequete("y.credit", "credit", creditContenuSearch, "=", "AND");
        }
        paginatorContenu.addParam(p);
        loadAllContenuForPiece(true, true);
    }

    public void addParamLettrageContent() {
        ParametreRequete p = new ParametreRequete("y.lettrage", "lettrage", null, "=", "AND");
        if (lettrageSearch != null ? lettrageSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.lettrage)", "lettrage", lettrageSearch.toUpperCase(), "=", "AND");
        }
        p_contenu.addParam(p);
        if (operation != null ? "L".equals(operation) : false) {
            loadAllLettrage(true, true);
        } else {
            loadAllContenu(true, true);
        }
    }

    public void addParamLettrageContentForPiece() {
        ParametreRequete p = new ParametreRequete("y.lettrage", "lettrage", null, "=", "AND");
        if (lettrageContenuSearch != null ? lettrageContenuSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.lettrage)", "lettrage", lettrageSearch.toUpperCase(), "=", "AND");
        }
        paginatorContenu.addParam(p);
        loadAllContenuForPiece(true, true);
    }

    public void addParamMouvementContent() {
        ParametreRequete p = new ParametreRequete("y.debit", "mouvement", null, "=", "AND");
        if (mouvementSearch != null ? mouvementSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "mouvement", 0, ">", "AND");
            p.getOtherExpression().add(new ParametreRequete((mouvementSearch.equals("D") ? "y.debit" : "y.credit"), "mouvement", 0, ">", "AND"));
            p.getOtherExpression().add(new ParametreRequete((mouvementSearch.equals("D") ? "y.debit" : "y.credit"), "mouvement", "IS NOT NULL", "IS NOT NULL", "AND"));
        }
        p_contenu.addParam(p);
        if (operation != null ? "L".equals(operation) : false) {
            loadAllLettrage(true, true);
        } else {
            loadAllContenu(true, true);
        }
    }

    public void addParamMouvementContentForPiece() {
        ParametreRequete p = new ParametreRequete("y.debit", "mouvement", null, "=", "AND");
        if (mouvementContenuSearch != null ? mouvementContenuSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "mouvement", 0, ">", "AND");
            p.getOtherExpression().add(new ParametreRequete((mouvementContenuSearch.equals("D") ? "y.debit" : "y.credit"), "mouvement", 0, ">", "AND"));
            p.getOtherExpression().add(new ParametreRequete((mouvementContenuSearch.equals("D") ? "y.debit" : "y.credit"), "mouvement", "IS NOT NULL", "IS NOT NULL", "AND"));
        }
        paginatorContenu.addParam(p);
        loadAllContenuForPiece(true, true);
    }

    public void addParamLettrerContent() {
        ParametreRequete p = new ParametreRequete("y.lettrage", "lettrage", null, "=", "AND");
        if (lettrerSearch != null) {
            p = new ParametreRequete("COALESCE(y.lettrage, '')", "lettrage", "0", (lettrerSearch ? ">" : "<"), "AND");
        }
        p_contenu.addParam(p);
        if (operation != null ? "L".equals(operation) : false) {
            loadAllLettrage(true, true);
        } else {
            loadAllContenu(true, true);
        }
    }

    public void addParamLettrerContentForPiece() {
        ParametreRequete p = new ParametreRequete("y.lettrage", "lettrage", null, "=", "AND");
        if (lettrerContenuSearch != null) {
            p = new ParametreRequete("COALESCE(y.lettrage, '')", "lettrage", "0", (lettrerContenuSearch ? ">" : "<"), "AND");
        }
        paginatorContenu.addParam(p);
        loadAllContenuForPiece(true, true);
    }

    public void addParamExercices(ValueChangeEvent ev) {
        Long id = null;
        if (ev != null) {
            id = (Long) ev.getNewValue();
        }
        ParametreRequete p = new ParametreRequete("y.piece.exercice", "exercice", new YvsBaseExercice(id), "=", "AND");
        p_contenu.addParam(p);
        if (operation != null ? "L".equals(operation) : false) {
            loadAllLettrage(true, true);
        } else {
            loadAllContenu(true, true);
        }
    }

    public void addParamDateContent(SelectEvent ev) {
        findByDateContent();
    }

    public void findByDateContent() {
        ParametreRequete p = new ParametreRequete("y.piece.datePiece", "date", null, "BETWEEN", "AND");
        if (dateContentSearch) {
            if (debutContentSearch != null && finContentSearch != null) {
                if (debutContentSearch.before(finContentSearch) || debutContentSearch.equals(finContentSearch)) {
                    p = new ParametreRequete(null, "date", debutContentSearch, finContentSearch, "BETWEEN", "AND");
                    p.getOtherExpression().add(new ParametreRequete("y.piece.datePiece", "date", debutContentSearch, finContentSearch, "BETWEEN", "OR"));
//                    p.getOtherExpression().add(new ParametreRequete("y.piece.dateSaisie", "date", debutContentSearch, finContentSearch, "BETWEEN", "OR"));
                }
            }
        }
        p_contenu.addParam(p);
        if (operation != null ? "L".equals(operation) : false) {
            loadAllLettrage(true, true);
        } else {
            loadAllContenu(true, true);
        }
    }

    public void onSelectedDistant(YvsComptaContentJournal y) {
        switch (y.getTableExterne()) {
            case Constantes.SCR_SALAIRE: {
                ManagedSalaire w = (ManagedSalaire) giveManagedBean(ManagedSalaire.class
                );
                if (w
                        != null) {
                    YvsGrhOrdreCalculSalaire p = (YvsGrhOrdreCalculSalaire) dao.loadOneByNameQueries("YvsGrhOrdreCalculSalaire.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistantOrdre(p);
                }
            }
            break;

            case Constantes.SCR_HEAD_VENTE: {
                ManagedVente w = (ManagedVente) giveManagedBean(ManagedVente.class
                );
                if (w
                        != null) {
                    YvsComEnteteDocVente p = (YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p);
                }
            }
            break;

            case Constantes.SCR_VENTE: {
                ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class
                );
                if (w
                        != null) {
                    YvsComDocVentes p = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p);
                }
            }
            break;

            case Constantes.SCR_ACHAT: {
                ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class
                );
                if (w
                        != null) {
                    YvsComDocAchats p = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p);
                }
            }
            break;

            case Constantes.SCR_DIVERS: {
                ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class
                );
                if (w
                        != null) {
                    YvsComptaCaisseDocDivers p = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p);
                }
            }
            break;

            case Constantes.SCR_VIREMENT: {
                ManagedVirement w = (ManagedVirement) giveManagedBean(ManagedVirement.class
                );
                if (w
                        != null) {
                    YvsComptaCaissePieceVirement p = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p);
                }
            }
            break;

            case Constantes.SCR_FRAIS_MISSIONS: {
                ManagedReglementMission w = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class
                );
                if (w
                        != null) {
                    YvsComptaCaissePieceMission p = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p.getMission());
                }
            }
            break;

            case Constantes.SCR_CAISSE_VENTE: {
                ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class
                );
                if (w
                        != null) {
                    YvsComptaCaissePieceVente p = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p);
                }
            }
            break;

            case Constantes.SCR_CAISSE_ACHAT: {
                ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class
                );
                if (w
                        != null) {
                    YvsComptaCaissePieceAchat p = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p);
                }
            }
            break;

            case Constantes.SCR_CAISSE_DIVERS: {
                ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class
                );
                if (w
                        != null) {
                    YvsComptaCaissePieceDivers p = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p.getDocDivers());
                }
            }
            break;

            case Constantes.SCR_ACOMPTE_VENTE: {
                ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class
                );
                if (w
                        != null) {
                    YvsComptaAcompteClient p = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaAcompteClient.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p);
                }
            }
            break;

            case Constantes.SCR_ACOMPTE_ACHAT: {
                ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class
                );
                if (w
                        != null) {
                    YvsComptaAcompteFournisseur p = (YvsComptaAcompteFournisseur) dao.loadOneByNameQueries("YvsComptaAcompteFournisseur.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p);
                }
            }
            break;

            case Constantes.SCR_CREDIT_VENTE: {
                ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class
                );
                if (w
                        != null) {
                    YvsComptaCreditClient p = (YvsComptaCreditClient) dao.loadOneByNameQueries("YvsComptaCreditClient.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p);
                }
            }
            break;

            case Constantes.SCR_CREDIT_ACHAT: {
                ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class
                );
                if (w
                        != null) {
                    YvsComptaCreditFournisseur p = (YvsComptaCreditFournisseur) dao.loadOneByNameQueries("YvsComptaCreditFournisseur.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p);
                }
            }
            break;

            case Constantes.SCR_CAISSE_CREDIT_VENTE: {
                ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class
                );
                if (w
                        != null) {
                    YvsComptaReglementCreditClient p = (YvsComptaReglementCreditClient) dao.loadOneByNameQueries("YvsComptaReglementCreditClient.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p.getCredit());
                }
            }
            break;

            case Constantes.SCR_CAISSE_CREDIT_ACHAT: {
                ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class
                );
                if (w
                        != null) {
                    YvsComptaReglementCreditFournisseur p = (YvsComptaReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaReglementCreditFournisseur.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                    w.onSelectDistant(p.getCredit());
                }
            }
            break;
            default: {
                String table = null;
                long id = 0;
                switch (y.getTableExterne()) {
                    case Constantes.SCR_PHASE_ACOMPTE_ACHAT: {
                        table = Constantes.SCR_ACOMPTE_ACHAT;
                        YvsComptaPhaseAcompteAchat p = (YvsComptaPhaseAcompteAchat) dao.loadOneByNameQueries("YvsComptaPhaseAcompteAchat.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                        if (p != null ? p.getId() > 0 : false) {
                            id = p.getPieceAchat() != null ? p.getPieceAchat().getId() : 0;
                        }
                    }
                    break;
                    case Constantes.SCR_PHASE_ACOMPTE_VENTE: {
                        table = Constantes.SCR_ACOMPTE_VENTE;
                        YvsComptaPhaseAcompteVente p = (YvsComptaPhaseAcompteVente) dao.loadOneByNameQueries("YvsComptaPhaseAcompteVente.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                        if (p != null ? p.getId() > 0 : false) {
                            id = p.getPieceVente() != null ? p.getPieceVente().getId() : 0;
                        }
                    }
                    break;
                    case Constantes.SCR_PHASE_CREDIT_ACHAT: {
                        table = Constantes.SCR_CREDIT_ACHAT;
                        YvsComptaPhaseReglementCreditFournisseur p = (YvsComptaPhaseReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaPhaseReglementCreditFournisseur.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                        if (p != null ? p.getId() > 0 : false) {
                            id = p.getReglement() != null ? p.getReglement().getId() : 0;
                        }
                    }
                    break;
                    case Constantes.SCR_PHASE_CREDIT_VENTE: {
                        table = Constantes.SCR_CREDIT_VENTE;
                        YvsComptaPhaseReglementCreditClient p = (YvsComptaPhaseReglementCreditClient) dao.loadOneByNameQueries("YvsComptaPhaseReglementCreditClient.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                        if (p != null ? p.getId() > 0 : false) {
                            id = p.getReglement() != null ? p.getReglement().getId() : 0;
                        }
                    }
                    break;
                    case Constantes.SCR_PHASE_CAISSE_ACHAT: {
                        table = Constantes.SCR_ACHAT;
                        YvsComptaPhasePieceAchat p = (YvsComptaPhasePieceAchat) dao.loadOneByNameQueries("YvsComptaPhasePieceAchat.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                        if (p != null ? p.getId() > 0 : false) {
                            id = p.getPieceAchat() != null ? p.getPieceAchat().getId() : 0;
                        }
                    }
                    break;
                    case Constantes.SCR_PHASE_CAISSE_DIVERS: {
                        table = Constantes.SCR_DIVERS;
                        YvsComptaPhasePieceDivers p = (YvsComptaPhasePieceDivers) dao.loadOneByNameQueries("YvsComptaPhasePieceDivers.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                        if (p != null ? p.getId() > 0 : false) {
                            id = p.getPieceDivers() != null ? p.getPieceDivers().getId() : 0;
                        }
                    }
                    break;
                    case Constantes.SCR_PHASE_CAISSE_VENTE: {
                        table = Constantes.SCR_VENTE;
                        YvsComptaPhasePiece p = (YvsComptaPhasePiece) dao.loadOneByNameQueries("YvsComptaPhasePiece.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                        if (p != null ? p.getId() > 0 : false) {
                            id = p.getPieceVente() != null ? p.getPieceVente().getId() : 0;
                        }
                    }
                    break;
                    case Constantes.SCR_PHASE_VIREMENT: {
                        table = Constantes.SCR_VIREMENT;
                        YvsComptaPhasePieceVirement p = (YvsComptaPhasePieceVirement) dao.loadOneByNameQueries("YvsComptaPhasePieceVirement.findById", new String[]{"id"}, new Object[]{y.getRefExterne()});
                        if (p != null ? p.getId() > 0 : false) {
                            id = p.getVirement() != null ? p.getVirement().getId() : 0;
                        }
                    }
                    break;

                }
                if (table != null ? id > 0 : false) {
                    ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class
                    );
                    if (w
                            != null) {
                        w.onSelectDistantCheque(id, table);
                    }
                }
            }
            break;
        }
    }

    public void changeGroupContent() {
        loadAllLettrage(true, true);
    }

    public double summaryGroup(String groupe, String type) {
        double sum = 0;
        for (YvsComptaContentJournal c : contenus) {
            if (groupBy.equals("T")) {
                if (c.getCompteTiers() != null ? c.getCompteTiers().equals(groupe) : true) {
                    if (type.equals("C")) {
                        sum += c.getCredit();
                    } else {
                        sum += c.getDebit();
                    }
                }
            } else if (groupBy.equals("C")) {
                if (c.getCompteGeneral() != null ? c.getCompteGeneral().getNumCompte().equals(groupe) : true) {
                    if (type.equals("C")) {
                        sum += c.getCredit();
                    } else {
                        sum += c.getDebit();
                    }
                }
            } else {
                if (c.getNumPiece().equals(groupe)) {
                    if (type.equals("C")) {
                        sum += c.getCredit();
                    } else {
                        sum += c.getDebit();
                    }
                }
            }
        }
        return sum;
    }

    public void cancelLettrageCompte() {
        lettrages.clear();
    }

    public void deleteLettrageCompte() {
        if (lettrages != null ? !lettrages.isEmpty() : false) {
            if (!autoriser("compta_annule_lettrage_not_equilib")) {
                double solde = giveSoePieces(lettrages);
                if (solde != 0) {
                    getErrorMessage("Impossible d'annuler ces lignes", "La liste n'est pas equilibrée");
                    return;
                }
            }
            String lettrage = lettrages.get(0).getLettrage();
            YvsBaseExercice exercire = lettrages.get(0).getPiece().getExercice();
            for (int i = 1; i < lettrages.size(); i++) {
                if (!Objects.equals(exercire, lettrages.get(i).getPiece().getExercice())) {
                    getWarningMessage("Attention sur l'exercice", "car certaines lignes ne sont pas comptabilisé dans le même exercice que d'autres");
                }
                if (!Objects.equals(lettrage, lettrages.get(i).getLettrage())) {
                    getErrorMessage("Impossible d'annuler ces lignes", "car certaines lignes ne sont pas lettrées avec d'autres");
                    return;
                }
            }
            for (YvsComptaContentJournal c : lettrages) {
                c.setLettrage(null);
                c.setAuthor(currentUser);
                c.setDateUpdate(new Date());
                dao.update(c);

                int idx = contenus.indexOf(c);
                if (idx > -1) {
                    contenus.get(idx).setLettrage(null);
                }
            }
            champ = new String[]{"exercice", "lettrage"};
            val = new Object[]{exercire, lettrage};
            Double credit = (Double) dao.loadObjectByNameQueries("YvsComptaContentJournal.findSumCreditByLettrage", champ, val);
            Double debit = (Double) dao.loadObjectByNameQueries("YvsComptaContentJournal.findSumDebitByLettrage", champ, val);
            if (!Objects.equals(credit, debit)) {
                String query = "UPDATE yvs_compta_content_journal y SET lettrage = null FROM yvs_compta_pieces_comptable p WHERE y.piece = p.id AND p.exercice = ? AND y.lettrage = ?";
                dao.requeteLibre(query, new Options[]{new Options(exercire.getId(), 1), new Options(lettrage, 2)});
            }
        }
    }

    public void openChangeLettrageCompte() {
        if (lettrages != null ? !lettrages.isEmpty() : false) {
            double solde = giveSoePieces(lettrages);
            if (solde != 0) {
                getErrorMessage("Impossible de modifier ces lignes", "La liste n'est pas equilibrée");
                return;
            }
            String lettrage = lettrages.get(0).getLettrage();
            for (int i = 1; i < lettrages.size(); i++) {
                if (!Objects.equals(lettrage, lettrages.get(i).getLettrage())) {
                    getErrorMessage("Impossible de modifier ces lignes", "car certaines lignes ne sont pas lettrées avec d'autres");
                    return;
                }
            }
            newLettre = lettrage;
            update("txt_new_value_lettrage");
            openDialog("dlgChangeLettrage");
        }
    }

    public void changeLettrageCompte() {
        if (lettrages != null ? !lettrages.isEmpty() : false) {
            if (newLettre != null ? newLettre.trim().length() < 1 : true) {
                getErrorMessage("Impossible de modifier ces lignes", "Vous devez entrer la lettre du lettrage");
                return;
            }
            List<YvsComptaContentJournal> result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentAgence.getSociete(), newLettre});
            if (result != null ? !result.isEmpty() : false) {
                getErrorMessage("Impossible de modifier ces lignes", "car cette lettre est déja utilisée");
                return;
            }
            for (YvsComptaContentJournal c : lettrages) {
                c.setLettrage(newLettre);
                c.setAuthor(currentUser);
                c.setDateUpdate(new Date());
                dao.update(c);

                int idx = contenus.indexOf(c);
                if (idx > -1) {
                    contenus.get(idx).setLettrage(newLettre);
                }
            }
            closeDialog("dlgChangeLettrage");
        }
    }

    public void saveLettrageCompte() {
        if (lettrages != null ? !lettrages.isEmpty() : false) {
            Date date = lettrages.get(0).getPiece().getDatePiece();
            YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByDate", new String[]{"societe", "date"}, new Object[]{currentAgence.getSociete(), date});
            if (exo != null ? exo.getId() < 1 : true) {
                getErrorMessage("Aucun exercice trouvé pour la date du " + formatDate.format(date));
                return;
            }
            for (YvsComptaContentJournal c : lettrages) {
                if (!Objects.equals(c.getPiece().getExercice().getId(), exo.getId())) {
                    getErrorMessage("Vous ne pouvez pas lettrer des ecritures dans des exercices différents");
                    return;
                }
                if (asString(c.getLettrage())) {
                    getErrorMessage("Impossible de lettrer ces lignes", "car il existe des lignes déjà lettrées");
                    return;
                }
            }
            String lettrage = nextLettre(exo);
            for (YvsComptaContentJournal c : lettrages) {
                c.setLettrage(lettrage);
                c.setAuthor(currentUser);
                c.setDateUpdate(new Date());
                dao.update(c);

                int idx = contenus.indexOf(c);
                if (idx > -1) {
                    contenus.get(idx).setLettrage(lettrage);
                }
            }
        }
    }

    public String findNameTiers(Long id, String table, String info) {
        if (id != null ? id > 0 : false) {
            return dao.nameTiers(id, table, info);
        }
        return "";
    }

    public void addParamNaturePiece(String nature) {
        paginator.addParam(new ParametreRequete("y.tableExterne", "tableExterne", (Util.asString(nature) ? nature : null), "=", "AND"));
        initForm = true;
        loadAllPiece(true);
    }

    public void addParamNaturePiece(String nature, String refExterne) {
        ParametreRequete p = new ParametreRequete("y.id", "ids", null, "IN", "AND");
        List<Long> ids = new ArrayList<>();
        if ((nature != null ? !nature.trim().isEmpty() : false) && (refExterne != null ? !refExterne.trim().isEmpty() : false)) {
            switch (nature) {
                case Constantes.SCR_SALAIRE:
                    //id salaire comptabilisé

                    break;
                case Constantes.SCR_HEAD_VENTE:
                    break;
                case Constantes.SCR_VENTE:
                    ids = dao.loadNameQueries("YvsComptaContentJournalFactureVente.findByIdByRefPiece", new String[]{"numero", "societe"}, new Object[]{"%" + refExterne.trim().toUpperCase() + "%", currentAgence.getSociete()});
                    break;
                case Constantes.SCR_AVOIR_VENTE:
                    break;
                case Constantes.SCR_ACHAT:
                    ids = dao.loadNameQueries("YvsComptaContentJournalFactureAchat.findByIdByRefPiece", new String[]{"numero", "societe"}, new Object[]{"%" + refExterne.trim().toUpperCase() + "%", currentAgence.getSociete()});
                    break;
                case Constantes.SCR_AVOIR_ACHAT:
                    break;
                case Constantes.SCR_DIVERS:
                    ids = dao.loadNameQueries("YvsComptaContentJournalDocDivers.findByIdByRefPiece", new String[]{"numero", "societe"}, new Object[]{"%" + refExterne.trim().toUpperCase() + "%", currentAgence.getSociete()});
                    break;
                case Constantes.SCR_ACOMPTE_ACHAT:
                    ids = dao.loadNameQueries("YvsComptaContentJournalAcompteFournisseur.findByIdByRefPiece", new String[]{"numero", "societe"}, new Object[]{"%" + refExterne.trim().toUpperCase() + "%", currentAgence.getSociete()});
                    break;
                case Constantes.SCR_ACOMPTE_VENTE:
                    ids = dao.loadNameQueries("YvsComptaContentJournalAcompteClient.findByIdByRefPiece", new String[]{"numero", "societe"}, new Object[]{"%" + refExterne.trim().toUpperCase() + "%", currentAgence.getSociete()});
                    break;
                case Constantes.SCR_CREDIT_VENTE:
                    break;
                case Constantes.SCR_CREDIT_ACHAT:
                    break;
                case Constantes.SCR_FRAIS_MISSIONS:
//                    ids = dao.loadNameQueries("YvsComptaContentJournalAcompteClient.findByIdByRefPiece", new String[]{"numero", "societe"}, new Object[]{"%" + refExterne.trim().toUpperCase() + "%", currentAgence.getSociete()});
                    break;
                case Constantes.SCR_NOTE_FRAIS:
                    break;
                case Constantes.SCR_CAISSE_ACHAT:
                    ids = dao.loadNameQueries("YvsComptaContentJournalPieceAchat.findByIdByRefPiece", new String[]{"numero", "societe"}, new Object[]{"%" + refExterne.trim().toUpperCase() + "%", currentAgence.getSociete()});
                    break;
                case Constantes.SCR_CAISSE_DIVERS:
                    ids = dao.loadNameQueries("YvsComptaContentJournalPieceDivers.findByIdByRefPiece", new String[]{"numero", "societe"}, new Object[]{"%" + refExterne.trim().toUpperCase() + "%", currentAgence.getSociete()});
                    break;
                case Constantes.SCR_CAISSE_VENTE:
                    ids = dao.loadNameQueries("YvsComptaContentJournalPieceVente.findByIdByRefPiece", new String[]{"numero", "societe"}, new Object[]{"%" + refExterne.trim().toUpperCase() + "%", currentAgence.getSociete()});
                    break;
                case Constantes.SCR_VIREMENT:
                    ids = dao.loadNameQueries("YvsComptaContentJournalPieceVirement.findByIdByRefPiece", new String[]{"numero", "societe"}, new Object[]{"%" + refExterne.trim().toUpperCase() + "%", currentAgence.getSociete()});
                    break;
                case Constantes.SCR_RETENUE:
                    ids = dao.loadNameQueries("YvsComptaContentJournalRetenueSalaire.findByIdByRefPiece", new String[]{"numero", "societe"}, new Object[]{"%" + refExterne.trim().toUpperCase() + "%", currentAgence.getSociete()});
                    break;
                default:
                    break;
            }
            if (ids.isEmpty()) {
                ids.add(-1L);
            }
            p = new ParametreRequete("y.id", "ids", ids, "IN", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true);
    }

    public void onSearchDesequilibrer() {
        String req = "select p.id from yvs_compta_pieces_comptable p inner join yvs_compta_content_journal c on c.piece = p.id "
                + "where p.exercice is not null " + (exerciceSearch > 0 ? " AND p.exercice = " + exerciceSearch : "") + " group by p.id having abs(sum(c.debit) - sum(c.credit)) > 0.01";
        List<Long> desequilibrer = dao.loadListBySqlQuery(req, new Options[]{});
        if (desequilibrer.isEmpty()) {
            desequilibrer.add(-1L);
        }
        paginator.getParams().clear();
        if (exerciceSearch > 0) {
            paginator.addParam(new ParametreRequete("y.piece.exercice", "exercice", new YvsBaseExercice(exerciceSearch), "=", "AND"));
        }
        paginator.addParam(new ParametreRequete("y.piece.id", "desequilibrer", desequilibrer, "IN", "AND"));
        initForm = true;
        loadAllPiece(true, true);
    }

    public void clearContentJournal(boolean delete) {
        try {
            String req = "DELETE FROM yvs_compta_pieces_comptable WHERE id IN "
                    + "(SELECT p.id FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_journaux j ON j.id = p.journal "
                    + "INNER JOIN yvs_agences a ON a.id = j.agence LEFT JOIN yvs_compta_content_journal c ON c.piece = p.id "
                    + "WHERE a.societe = ? AND c.id IS NULL)";
            dao.requeteLibre(req, new yvs.dao.Options[]{new yvs.dao.Options(currentAgence.getSociete().getId(), 1)});
            succes();
        } catch (Exception ex) {
            getErrorMessage("Impossible de nettoyer");
            System.err.println("Error " + ex.getMessage());
        }
    }

    public boolean extournePiece(YvsComptaPiecesComptable p, Date date) {
        if (p != null) {
            YvsComptaPiecesComptable piece = new YvsComptaPiecesComptable(p);
            date = date != null ? date : new Date();
            piece.setDatePiece(date);
            piece.setDateSaisie(new Date());
            piece.setJournal(p.getJournal());
            piece.setDateSave(new Date());
            piece.setDateUpdate(new Date());
            piece.setAuthor(currentUser);
            piece.setExtourne(true);
            YvsBaseExercice exo = controleExercice(date, true);
            if (exo != null ? exo.getId() < 1 : true) {
                return false;
            }
            String num = genererReference(Constantes.TYPE_PIECE_COMPTABLE_NAME, date, p.getJournal().getId());
            if (num == null || num.trim().length() < 1) {
                return false;
            }
            piece.setExercice(exo);
            piece.setNumPiece(num);
            piece.setId(null);
            dao.save(piece);
            List<YvsComptaContentJournal> contenus = new ArrayList<>();
            YvsComptaContentJournal c;
            for (YvsComptaContentJournal cj : p.getContentsPiece()) {
                cj = (YvsComptaContentJournal) dao.loadOneByNameQueries("YvsComptaContentJournal.findById", new String[]{"id"}, new Object[]{cj.getId()});
                c = new YvsComptaContentJournal(cj);
                c.setId(null);
                c.setCompteGeneral(cj.getCompteGeneral());
                c.setAuthor(currentUser);
                if (cj.getCredit() > 0) {
                    c.setDebit(cj.getCredit());
                    c.setCredit(0d);
                } else {
                    c.setCredit(cj.getDebit());
                    c.setDebit(0d);
                }
                c.setLibelle(getLibelleExtourne(p));
                c.setPiece(piece);
                c.setLettrage(null);
                piece.setDateSave(new Date());
                piece.setDateUpdate(new Date());
                dao.save(c);
                contenus.add(c);
            }
            return true;
        }
        return false;
    }

    private String getLibelleExtourne(YvsComptaPiecesComptable p) {
        return "EXTOURNE OP";
    }

    public Long idSource;
    public String tableSource;

    public String getTableSource() {
        return tableSource;
    }

    public void displayPieceComptable(Long id, String table) {
        idSource = id;
        tableSource = table;
        if (id != null ? (id > 0 && table != null) : false) {
            String query = null;
            Options[] params = new Options[]{new Options(id, 1)};
            switch (table) {
                case Constantes.SCR_SALAIRE: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_entete_bulletin cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.entete=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_RETENUE: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_retenue_salaire cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.retenue=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_BULLETIN: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_bulletin cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.bulletin=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_HEAD_VENTE: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_entete_facture_vente cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.entete=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_AVOIR_VENTE:
                case Constantes.SCR_VENTE: {
                    query = "SELECT p.id AS piece,p.num_piece,p.date_piece, c.id, c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage FROM yvs_compta_content_journal c "
                            + "INNER JOIN yvs_compta_pieces_comptable p ON c.piece=p.id LEFT JOIN yvs_compta_content_journal_facture_vente cf ON p.id=cf.piece "
                            + "WHERE cf.facture = ? "
                            + " UNION "
                            + "SELECT p.id,p.num_piece,p.date_piece, c.id, c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage FROM yvs_compta_content_journal c "
                            + "INNER JOIN yvs_compta_pieces_comptable p ON c.piece=p.id LEFT JOIN yvs_compta_content_journal_piece_vente cf ON p.id=cf.piece LEFT JOIN yvs_compta_caisse_piece_vente r ON cf.reglement = r.id "
                            + "WHERE r.vente = ? "
                            + "ORDER BY piece, debit";
                    params = new Options[]{new Options(id, 1), new Options(id, 2)};
                    break;
                }
                case Constantes.SCR_AVOIR_ACHAT:
                case Constantes.SCR_ACHAT: {
                    query = "SELECT p.id AS piece,p.num_piece,p.date_piece, c.id, c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage FROM yvs_compta_content_journal c "
                            + "INNER JOIN yvs_compta_pieces_comptable p ON c.piece=p.id LEFT JOIN yvs_compta_content_journal_facture_achat cf ON p.id=cf.piece "
                            + "WHERE cf.facture = ? "
                            + " UNION "
                            + "SELECT p.id,p.num_piece,p.date_piece, c.id, c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage FROM yvs_compta_content_journal c "
                            + "INNER JOIN yvs_compta_pieces_comptable p ON c.piece=p.id LEFT JOIN yvs_compta_content_journal_piece_achat cf ON p.id=cf.piece LEFT JOIN yvs_compta_caisse_piece_achat r ON cf.reglement = r.id "
                            + "WHERE r.achat = ? "
                            + "ORDER BY piece, debit";
                    params = new Options[]{new Options(id, 1), new Options(id, 2)};
                    break;
                }
                case Constantes.SCR_DIVERS: {
                    query = "SELECT p.id AS piece ,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage FROM yvs_compta_content_journal c "
                            + "INNER JOIN yvs_compta_pieces_comptable p ON c.piece=p.id LEFT JOIN yvs_compta_content_journal_doc_divers cf ON p.id=cf.piece "
                            + "WHERE cf.divers=? "
                            + " UNION "
                            + "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage FROM yvs_compta_content_journal c "
                            + "INNER JOIN yvs_compta_pieces_comptable p ON c.piece=p.id LEFT JOIN yvs_compta_content_journal_abonnement_divers cf ON p.id=cf.piece LEFT JOIN yvs_compta_abonement_doc_divers a ON cf.abonnement = a.id "
                            + "WHERE a.doc_divers=? "
                            + " UNION "
                            + "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage FROM yvs_compta_content_journal c "
                            + "INNER JOIN yvs_compta_pieces_comptable p ON c.piece=p.id LEFT JOIN yvs_compta_content_journal_piece_divers cf ON p.id=cf.piece LEFT JOIN yvs_compta_caisse_piece_divers r ON cf.reglement = r.id "
                            + "WHERE r.doc_divers=? "
                            + "ORDER BY piece, debit";
                    params = new Options[]{new Options(id, 1), new Options(id, 2), new Options(id, 3)};
                    break;
                }
                case Constantes.SCR_CAISSE_VENTE: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_piece_vente cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.reglement=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_CAISSE_ACHAT: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_piece_achat cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.reglement=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_CAISSE_DIVERS: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_piece_divers cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.reglement=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_CAISSE_CREDIT_ACHAT: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_reglement_credit_fournisseur cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.reglement=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_CAISSE_CREDIT_VENTE: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_reglement_credit_client cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.reglement=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_ACOMPTE_ACHAT: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_acompte_fournisseur cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.acompte=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_ACOMPTE_VENTE: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_acompte_client cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.acompte=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_CREDIT_ACHAT: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_credit_fournisseur cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.credit=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_CREDIT_VENTE: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_credit_client cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.credit=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_VIREMENT: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_piece_virement cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.reglement=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_PHASE_CAISSE_VENTE: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_etape_piece_vente cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.etape=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_PHASE_CAISSE_ACHAT: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_etape_piece_achat cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.etape=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_PHASE_CAISSE_DIVERS: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_etape_piece_divers cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.etape=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_PHASE_ACOMPTE_ACHAT: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_etape_acompte_achat cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.etape=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_PHASE_ACOMPTE_VENTE: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_etape_acompte_vente cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.etape=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_PHASE_CREDIT_ACHAT: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_etape_reglement_credit_fournisseur cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.etape=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_PHASE_CREDIT_VENTE: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_etape_reglement_credit_client cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.etape=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_PHASE_VIREMENT: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_etape_piece_virement cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.etape=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_ABONNEMENT_DIVERS: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_abonnement_divers cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.abonnement=? ORDER BY p.id, c.debit DESC";
                    break;
                }
                case Constantes.SCR_FRAIS_MISSIONS: {
                    query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit, c.lettrage "
                            + "FROM yvs_compta_content_journal_piece_mission cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                            + "WHERE cf.reglement=? ORDER BY p.id, c.debit DESC";
                    break;
                }
            }
            if (query != null) {
                List<Object[]> result = dao.loadListBySqlQuery(query, params);
                long idPiece = 0;
                YvsComptaPiecesComptable pc = null;
                YvsComptaContentJournal c;
                getContentCompta().clear();
                for (Object[] line : result) {
                    if (line[0] != null ? ((Long) line[0] != idPiece) : false) {
                        idPiece = (Long) line[0];
                        pc = new YvsComptaPiecesComptable(idPiece);
                        pc.setNumPiece((String) line[1]);
                        pc.setDatePiece((line[2] != null) ? (Date) line[2] : null);
                        getContentCompta().add(pc);
                    } else {
                        if (line[0] == null) {
                            pc = null;
                            continue;
                        }
//                    if(pc!=null){
//                        contentCompta.add(pc);
//                    }
                    }
                    if (pc != null) {
                        if (pc.getContentsPiece() == null) {
                            pc.setContentsPiece(new ArrayList<YvsComptaContentJournal>());
                        }
                        c = new YvsComptaContentJournal((Long) line[3]);
                        c.setNumPiece((String) line[4]);
                        if (line[5] != null) {
                            c.setCompteGeneral(new YvsBasePlanComptable((Long) line[5], numCompte((Long) line[5])));
                        }
                        c.setCompteTiers((line)[6] != null ? (Long) line[6] : null);
                        c.setTableTiers((String) line[7]);
                        c.setDebit((line)[8] != null ? (Double) line[8] : null);
                        c.setCredit((line)[9] != null ? (Double) line[9] : null);
                        c.setLettrage((line)[10] != null ? (String) line[10] : null);
                        pc.getContentsPiece().add(c);
                    }
                }
            }
            update("table_pptes_compta");
        }
    }

    private String numCompte(Long id) {
        if (id != null) {
            return (String) dao.loadObjectByNameQueries("YvsBasePlanComptable.findNumeroById", new String[]{"id"}, new Object[]{id});
        }
        return null;
    }

    public void lettrageAutomatique() {
        try {
            if (contenus != null ? contenus.isEmpty() : true) {
                getErrorMessage("Pas d'element à lettrer");
                return;
            }
            YvsComptaContentJournal current, next;
            YvsBaseExercice exo;
            for (int i = 0; i < contenus.size(); i++) {
                current = contenus.get(i);
                if (Util.asString(current.getLettrage()) || ((!current.getCredit().equals(0D)) && (!current.getDebit().equals(0D)))) {
                    continue;
                }
                for (int n = i + 1; n < contenus.size(); n++) {
                    next = contenus.get(n);
                    if (Util.asString(next.getLettrage()) || ((!next.getCredit().equals(0D)) && (!next.getDebit().equals(0D)))) {
                        continue;
                    }
                    if ((!current.getCredit().equals(0D) && current.getCredit().equals(next.getDebit())) || (!current.getDebit().equals(0D) && next.getCredit().equals(current.getDebit()))) {
                        exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByDate", new String[]{"societe", "date"}, new Object[]{currentAgence.getSociete(), current.getPiece().getDatePiece()});
                        if (exo != null ? exo.getId() < 1 : true) {
                            continue;
                        }
                        String lettrage = nextLettre(exo);

                        current.setLettrage(lettrage);
                        current.setAuthor(currentUser);
                        current.setDateUpdate(new Date());
                        dao.update(current);
                        contenus.get(i).setLettrage(lettrage);

                        next.setLettrage(lettrage);
                        next.setAuthor(currentUser);
                        next.setDateUpdate(new Date());
                        dao.update(next);
                        contenus.get(n).setLettrage(lettrage);
                    }
                }
            }
            succes();
        } catch (Exception ex) {
            getException(getClass().getSimpleName() + " (lettrageAutomatique)", ex);
        }
    }

    public void findTiersDeLetter() {
        try {
            String code = tiersDeLetter.getCodeTiers();
            tiersDeLetter = new Tiers(0, code, "", "");
            tiersDeLetter.setError(true);

            champ = new String[]{"code", "societe"};
            val = new Object[]{code, currentAgence.getSociete()};
            nameQueri = "YvsBaseTiers.findByCode";
            YvsBaseTiers y = (YvsBaseTiers) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (y != null ? y.getId() > 0 : false) {
                tiersDeLetter = new Tiers(y.getId(), y.getCodeTiers(), y.getNom(), y.getPrenom());
                tiersDeLetter.setProfils(UtilTiers.buildBeanListProfil(y));
                tiersDeLetter.setError(false);
            }
        } catch (Exception ex) {
            getException(getClass().getSimpleName() + " (findTiersDeLetter)", ex);
        }
    }

    public void findCompteDeLetter() {
        try {
            String numCompte = compteDeLetter.getNumCompte();
            compteDeLetter = new Comptes(0, numCompte, "");
            compteDeLetter.setError(true);

            champ = new String[]{"numCompte", "societe"};
            val = new Object[]{numCompte, currentAgence.getSociete()};
            nameQueri = "YvsBasePlanComptable.findByNumCompte";
            YvsBasePlanComptable y = (YvsBasePlanComptable) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (y != null ? y.getId() > 0 : false) {
                compteDeLetter = new Comptes(y.getId(), y.getNumCompte(), y.getIntitule());
                compteDeLetter.setError(false);
            }
        } catch (Exception ex) {
            getException(getClass().getSimpleName() + " (findCompteDeLetter)", ex);
        }
    }

    public void chooseProfilDeLetter() {
        try {
            if (tiersDeLetter != null ? tiersDeLetter.getId() > 0 : false) {
                if (tiersDeLetter.getSelectProfil() != null ? Util.asString(tiersDeLetter.getSelectProfil().getValue()) : false) {
                    String[] values = tiersDeLetter.getSelectProfil().getValue().split("-");
                    if (values != null ? values.length > 1 : false) {
                        long id = Long.valueOf(values[0]);
                        String type = values[1];
                        int index = tiersDeLetter.getProfils().indexOf(new Profil(id, type));
                        if (index > -1) {
                            tiersDeLetter.setSelectProfil(tiersDeLetter.getProfils().get(index));
                        }
                    }
                }
            }
        } catch (NumberFormatException ex) {
            getException(getClass().getSimpleName() + " (chooseProfilDeLetter)", ex);
        }
    }

    public void deleteAllLettrageCompte() {
        try {
            if (exoDeLetter < 1) {
                getErrorMessage("Vous devez selectionner un exercice");
                return;
            }
            if ((tiersDeLetter.getSelectProfil() != null ? tiersDeLetter.getSelectProfil().getId() < 1 : true) && (compteDeLetter != null ? compteDeLetter.getId() < 1 : true)) {
                getErrorMessage("Vous devez selectionner un compte tiers ou un compte général");
                return;
            }
            int position = 1;
            List<Options> params = new ArrayList<>();
            params.add(new Options(exoDeLetter, position++));
            String query = "UPDATE yvs_compta_content_journal y SET lettrage = null FROM yvs_compta_pieces_comptable p WHERE CHAR_LENGTH(COALESCE(y.lettrage, '')) > 0 AND y.piece = p.id AND p.exercice = ?";
            if (tiersDeLetter != null ? tiersDeLetter.getId() > 0 : false) {
                query += " AND compte_tiers = ? AND table_tiers = ?";
                params.add(new Options(tiersDeLetter.getSelectProfil().getId(), position++));
                params.add(new Options(tiersDeLetter.getSelectProfil().getTableTiers(), position++));
            }
            if (compteDeLetter != null ? compteDeLetter.getId() > 0 : false) {
                query += " AND compte_general = ?";
                params.add(new Options(compteDeLetter.getId(), position++));
            }
            dao.requeteLibre(query, params.toArray(new Options[params.size()]));
            succes();
        } catch (Exception ex) {
            getException(getClass().getSimpleName() + " (deleteAllLettrageCompte)", ex);
        }
    }

    public void lettrageAutoByMontant() {
        if (agenceSearch != null ? agenceSearch <= 0 : true) {
            getErrorMessage("Vous devez choisir une agence !");
            return;
        }
        if (exerciceSearch != null ? exerciceSearch <= 0 : true) {
            getErrorMessage("Vous devez choisir un exercice !");
            return;
        }
        if (selectCompte != null ? !Util.asLong(selectCompte.getId()) : true) {
            getErrorMessage("Vous devez selectionner un compte ");
            return;
        }
        switch (typeLettrage) {
            case "D":
                lettrageByNumero();
                break;
            case "M":
                lettrageByMontant();
                break;
        }
    }

    private void lettrageByMontant() {
        //1. Récupère les montant distinct
        List<Double> montants = null;
        List<Object[]> nombres = null;
        Long nbDebit = 0l, nbCredit = 0l, nb;
        YvsBaseExercice exo = null;
        String lettre = null;
        String queryCount = "SELECT (SELECT COUNT(*) FROM yvs_compta_content_journal c INNER JOIN yvs_compta_pieces_comptable p ON p.id=c.piece "
                + "INNER JOIN yvs_compta_journaux j ON j.id=p.journal "
                + "INNER JOIN yvs_agences a ON a.id=j.agence "
                + "WHERE a.societe=? AND c.compte_general=? AND a.id=? AND p.exercice=? AND c.debit=? AND COALESCE(lettrage,'')=''),"
                + "(SELECT COUNT(*) FROM yvs_compta_content_journal c INNER JOIN yvs_compta_pieces_comptable p ON p.id=c.piece "
                + "INNER JOIN yvs_compta_journaux j ON j.id=p.journal "
                + "INNER JOIN yvs_agences a ON a.id=j.agence "
                + "WHERE a.societe=? AND c.compte_general=? AND a.id=? AND p.exercice=? AND c.credit=? AND COALESCE(lettrage,'')='') ";
        String queryMontant = "SELECT DISTINCT c.debit FROM yvs_compta_content_journal c INNER JOIN yvs_compta_pieces_comptable p ON p.id=c.piece "
                + "INNER JOIN yvs_compta_journaux j ON j.id=p.journal "
                + "INNER JOIN yvs_agences a ON a.id=j.agence "
                + "WHERE a.societe=? AND c.compte_general=? AND a.id=? AND p.exercice=? AND c.debit!=0 AND COALESCE(lettrage,'')='' ORDER BY c.debit";
        String queryUpdateD = "UPDATE yvs_compta_content_journal SET lettrage=? WHERE id IN (SELECT c.id FROM yvs_compta_content_journal c INNER JOIN yvs_compta_pieces_comptable p ON p.id=c.piece "
                + "INNER JOIN yvs_compta_journaux j ON j.id=p.journal "
                + "INNER JOIN yvs_agences a ON a.id=j.agence "
                + "WHERE a.societe=? AND c.compte_general=? AND a.id=? AND p.exercice=? AND c.debit= ? AND COALESCE(lettrage,'')='' LIMIT ?)";
        String queryUpdateC = "UPDATE yvs_compta_content_journal SET lettrage=? WHERE id IN (SELECT c.id FROM yvs_compta_content_journal c INNER JOIN yvs_compta_pieces_comptable p ON p.id=c.piece "
                + "INNER JOIN yvs_compta_journaux j ON j.id=p.journal "
                + "INNER JOIN yvs_agences a ON a.id=j.agence "
                + "WHERE a.societe=? AND c.compte_general=? AND a.id=? AND p.exercice=? AND c.credit= ? AND COALESCE(lettrage,'')='' LIMIT ?)";

        montants = dao.loadListBySqlQuery(queryMontant, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(selectCompte.getId(), 2), new Options(agenceSearch, 3), new Options(exerciceSearch, 4)});
        ManagedExercice service = (ManagedExercice) giveManagedBean(ManagedExercice.class);
        int idx = service.getExercices().indexOf(new YvsBaseExercice(exerciceSearch));
        if (idx >= 0) {
            exo = service.getExercices().get(idx);
        }
        int line = 0;
        for (Double montant : montants) {
            //récupère le nombre de ligne debit/credit portant ce montant
            nombres = dao.loadListBySqlQuery(queryCount, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(selectCompte.getId(), 2), new Options(agenceSearch, 3), new Options(exerciceSearch, 4), new Options(montant, 5), new Options(currentAgence.getSociete().getId(), 6), new Options(selectCompte.getId(), 7), new Options(currentAgence.getId(), 8), new Options(exerciceSearch, 9), new Options(montant, 10)});
            nbDebit = (Long) nombres.get(0)[0];
            nbCredit = (Long) nombres.get(0)[1];
            if (nbCredit != null && nbDebit != null) {
                if (nbCredit < nbDebit) {
                    nb = nbCredit;
                } else {
                    nb = nbDebit;
                }
                //met à jour les données en les limitant à max nb.
                lettre = nextLettre(exo);
                dao.requeteLibre(queryUpdateC, new Options[]{new Options(lettre, 1), new Options(currentAgence.getSociete().getId(), 2), new Options(selectCompte.getId(), 3), new Options(agenceSearch, 4), new Options(exerciceSearch, 5), new Options(montant, 6), new Options(nb, 7)});
                dao.requeteLibre(queryUpdateD, new Options[]{new Options(lettre, 1), new Options(currentAgence.getSociete().getId(), 2), new Options(selectCompte.getId(), 3), new Options(agenceSearch, 4), new Options(exerciceSearch, 5), new Options(montant, 6), new Options(nb, 7)});
            }
        }
        succes();
    }

    public void lettrageByNumero() {
        List<Object[]> pieces = null;
        boolean filter = false;
        YvsBaseExercice exo = null;
        String lettre = null;
        String queryUpdate = "UPDATE yvs_compta_content_journal SET lettrage=? WHERE id IN (SELECT c.id FROM yvs_compta_content_journal c INNER JOIN yvs_compta_pieces_comptable p ON p.id=c.piece "
                + "INNER JOIN yvs_compta_journaux j ON j.id=p.journal "
                + "INNER JOIN yvs_agences a ON a.id=j.agence "
                + "WHERE a.societe=? AND c.compte_general=? AND a.id=? AND p.exercice=? AND c.num_piece= ? AND COALESCE(lettrage,'')='')";
        String query = "SELECT c.num_piece, SUM(c.debit-c.credit) FROM yvs_compta_content_journal c INNER JOIN yvs_compta_pieces_comptable p ON p.id=c.piece "
                + "INNER JOIN yvs_compta_journaux j ON j.id=p.journal "
                + "INNER JOIN yvs_agences a ON a.id=j.agence "
                + "WHERE a.societe=? AND c.compte_general=? AND a.id=? AND p.exercice=? AND COALESCE(lettrage,'')='' "
                + "GROUP BY c.num_piece HAVING(SUM(c.debit-c.credit)=0)";
        if (dateContentSearch && debutContentSearch != null && finContentSearch != null) {
            if (debutContentSearch.before(finContentSearch) || debutContentSearch.equals(finContentSearch)) {
                query = "SELECT c.num_piece, SUM(c.debit-c.credit) FROM yvs_compta_content_journal c INNER JOIN yvs_compta_pieces_comptable p ON p.id=c.piece "
                        + "INNER JOIN yvs_compta_journaux j ON j.id=p.journal "
                        + "INNER JOIN yvs_agences a ON a.id=j.agence "
                        + "WHERE a.societe=? AND c.compte_general=? AND a.id=? AND p.exercice=? AND TRIM(COALESCE(lettrage,''))='' "
                        + "AND p.date_piece BETWEEN ? AND ? "
                        + "GROUP BY c.num_piece HAVING(SUM(c.debit-c.credit)=0)";
                filter = true;
                pieces = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(selectCompte.getId(), 2), new Options(agenceSearch, 3), new Options(exerciceSearch, 4), new Options(debutContentSearch, 5), new Options(finContentSearch, 6)});
            }
        }
        if (!filter) {
            pieces = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(selectCompte.getId(), 2), new Options(agenceSearch, 3), new Options(exerciceSearch, 4)});
        }
        ManagedExercice service = (ManagedExercice) giveManagedBean(ManagedExercice.class);
        int idx = service.getExercices().indexOf(new YvsBaseExercice(exerciceSearch));
        if (idx >= 0) {
            exo = service.getExercices().get(idx);
        }
        int i = 0;
        for (Object[] line : pieces) {
            lettre = nextLettre(exo);
            dao.requeteLibre(queryUpdate, new Options[]{new Options(lettre, 1), new Options(currentAgence.getSociete().getId(), 2), new Options(selectCompte.getId(), 3), new Options(agenceSearch, 4), new Options(exerciceSearch, 5), new Options((String) line[0], 6)});
        }
        succes();

    }

    public void filtreLesOperationMalLettre() {
        // Récupérer les lettres non equilibré
        String query = null;
        Options[] params = null;
        List<String> lettres;
        if (null != groupBy) {
            switch (groupBy) {
                case "T":
                    query = "SELECT c.lettrage FROM yvs_compta_content_journal c INNER JOIN yvs_compta_pieces_comptable p ON p.id=c.piece "
                            + "INNER JOIN yvs_compta_journaux j ON j.id=p.journal "
                            + "INNER JOIN yvs_agences a ON a.id=j.agence "
                            + "WHERE a.societe=? AND (c.compte_tiers=? and c.table_tiers=?) "
                            + "AND a.id=? AND p.exercice=? AND TRIM(COALESCE(lettrage,''))!='' "
                            + "GROUP BY c.lettrage HAVING(SUM(c.debit-c.credit)!=0)";
                    params = new Options[]{
                        new Options(currentAgence.getSociete().getId(), 1),
                        new Options(this.profilFilter.getIdTiers(), 2),
                        new Options(this.profilFilter.getTableTiers(), 3),
                        new Options(currentAgence.getId(), 4),
                        new Options(exerciceSearch, 5)};
                    break;
                case "C":
                    query = "SELECT c.lettrage FROM yvs_compta_content_journal c INNER JOIN yvs_compta_pieces_comptable p ON p.id=c.piece "
                            + "INNER JOIN yvs_compta_journaux j ON j.id=p.journal "
                            + "INNER JOIN yvs_agences a ON a.id=j.agence "
                            + "WHERE a.societe=? AND c.compte_general=? AND a.id=? AND p.exercice=? AND TRIM(COALESCE(lettrage,''))!='' "
                            + "GROUP BY c.lettrage HAVING(SUM(c.debit-c.credit)!=0)";
                    params = new Options[]{
                        new Options(currentAgence.getSociete().getId(), 1),
                        new Options(selectCompte.getId(), 2),
                        new Options(currentAgence.getId(), 3),
                        new Options(exerciceSearch, 4)};
                    break;
            }
        }
        if (query != null) {
            lettres = dao.loadListBySqlQuery(query, params);
            if (!lettres.isEmpty()) {
                ParametreRequete p = new ParametreRequete("y.lettrage", "lettrage", lettres, "IN", "AND");
                p_contenu.addParam(p);
                loadAllLettrage(true, true);
                contenusLettrer.clear();
                contenusLettrer.addAll(contenus);
                openDialog("dlg_op_mal_lettre");
                update("data_operation_mal_lettre");
            }
        }
    }

    public void annulerLeLettrageDesOperationDesequilibre() {
        List<Long> ids = new ArrayList<>();
        StringBuilder joinedList = new StringBuilder();
        for (int i = 0; i < contenusLettrer.size(); i++) {
            joinedList.append(contenusLettrer.get(i).getId());
            if (i < contenusLettrer.size() - 1) {
                joinedList.append(",");
            }
        }
        try {
            String queryUpdate = "UPDATE yvs_compta_content_journal SET lettrage=null WHERE id IN (" + joinedList.toString() + ")";
            dao.requeteLibre(queryUpdate, new Options[]{});
            succes();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Erreur lors de l'annulation du lettrage", ex);
            getErrorMessage("Impossible d'effectuer l'opération !");
            return;
        }
        contenusLettrer.clear();
        succes();
    }
}

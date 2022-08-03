/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.tresorerie;

import yvs.comptabilite.analytique.CentreDocDivers;
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
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.Comptes;
import yvs.base.compta.ManagedCentreAnalytique;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.ModelReglement;
import yvs.base.compta.UtilCompta;
import yvs.base.tiers.ManagedTiers;
import yvs.base.tiers.Profil;
import yvs.base.tiers.Tiers;
import yvs.base.tiers.UtilTiers;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.ManagedTaxes;
import yvs.commercial.param.ManagedTypeDocDivers;
import yvs.commercial.param.TypeDocDivers;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.caisse.Caisses;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.comptabilite.caisse.ManagedPieceCaisse;
import yvs.comptabilite.caisse.ManagedPlanAbonnement;
import yvs.comptabilite.caisse.PlanDecoupage;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseDepartement;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.base.YvsBaseTrancheReglement;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.compta.YvsComptaAbonementDocDivers;
import yvs.entity.compta.YvsComptaAffectationGenAnal;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.YvsComptaParametre;
import yvs.entity.compta.YvsComptaPhasePieceDivers;
import yvs.entity.compta.YvsComptaPhaseReglement;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.YvsComptaPlanAbonnement;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaisseDocDiversTiers;
import yvs.entity.compta.divers.YvsComptaCaisseMensualiteDocDivers;
import yvs.entity.compta.divers.YvsComptaCaissePieceCoutDivers;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.divers.YvsComptaCentreDocDivers;
import yvs.entity.compta.divers.YvsComptaCoutSupDocDivers;
import yvs.entity.compta.divers.YvsComptaJustificatifBon;
import yvs.entity.compta.divers.YvsComptaTaxeDocDivers;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.contrat.YvsGrhTypeElementAdditionel;
import yvs.entity.grh.contrat.retenue.YvsGrhRetenueDocDivers;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsGrhPlanPrelevement;
import yvs.entity.param.workflow.YvsWorkflowAutorisationValidDoc;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidBonProvisoire;
import yvs.entity.param.workflow.YvsWorkflowValidDocCaisse;
import yvs.entity.proj.YvsProjDepartement;
import yvs.entity.proj.projet.YvsProjProjet;
import yvs.entity.proj.projet.YvsProjProjetCaisseDocDivers;
import yvs.entity.proj.projet.YvsProjProjetService;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.UtilGrh;
import yvs.grh.bean.ContratEmploye;
import yvs.grh.bean.ManagedTypeCout;
import yvs.grh.bean.TypeCout;
import yvs.grh.paie.ManagedRetenue;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.agence.ManagedAgence;
import yvs.users.Users;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;
import yvs.util.enume.Nombre;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedDocDivers extends Managed<DocCaissesDivers, YvsComptaCaisseDocDivers> implements Serializable {

    @ManagedProperty(value = "#{docCaissesDivers}")
    private DocCaissesDivers docDivers;
    private List<YvsComptaCaisseDocDivers> documents;
    private YvsComptaCaisseDocDivers selectDoc = new YvsComptaCaisseDocDivers();
    private TiersDivers tiers = new TiersDivers();
    private YvsComptaCaisseDocDiversTiers selectTiers = new YvsComptaCaisseDocDiversTiers();
    private String notes;
    private long projet;

    private String tabIds, tabIds_mensualite;
    private boolean date_up = false, deleteRetenue = false;
    private Date dateDebut_ = new Date(), dateFin_ = new Date();

    private CoutSupDocDivers cout = new CoutSupDocDivers();
    private TaxeDocDivers taxe = new TaxeDocDivers();
    private CentreDocDivers section = new CentreDocDivers();

    private String motifEtape;
    YvsWorkflowValidDocCaisse etape;
    private boolean lastEtape;

    private List<String> typesDocs, naturesSearch;
    private List<YvsWorkflowValidDocCaisse> etapesValidations;
    private YvsComptaCaissePieceDivers selectExtourne;
    private YvsComptaCaissePieceDivers pieceCD = new YvsComptaCaissePieceDivers();
    private YvsComptaPhasePieceDivers selectPhaseDivers = new YvsComptaPhasePieceDivers();
    private YvsComptaPhasePieceDivers currentPhaseDivers = new YvsComptaPhasePieceDivers();
    private YvsComptaAbonementDocDivers abonnCD = new YvsComptaAbonementDocDivers();
    private boolean validePiece;
    private List<YvsComptaCaissePieceDivers> pieces;
    private List<YvsComptaAbonementDocDivers> abonnements;
    public PaginatorResult<YvsComptaCaissePieceDivers> p_piece = new PaginatorResult<>();
    private boolean dateSearch;
    private Date dateDebutSearch = new Date(), dateFinSearch = new Date();
    private String numPieceF;

    private AbonnementDocDivers abonnement = new AbonnementDocDivers();

    private boolean changeNumdocAuto = true;
    private long agenceSearch, agencePieceSearch;
    private Long typeODSearch;
    private String numSearch, codeTiers, numCompte, typeSearch, natureSearch, statutSearch, forSearch;
    private Character statutRegSearch;
    private String egaliteStatut = "!=";
    private String egaliteStatutReg = "!=", comparer = ">=";
    private boolean date, _first = true, toValideLoad = true, addMontant;
    private Date dateDebut = new Date(), dateFin = new Date();
    private double montantDebut, montantFin, montantRetour;
    private Boolean bonSearch, comptaSearch, comptaRegSearch, withPlanSearch, withCompteSearch, withTiersSearch;
    private long nbrComptaSearch, nbrComptaPieceSearch;
    private List<String> statuts;

    YvsComptaParametre currentParam;
    private List<Profil> profils;

    private String sourceRetenue = "D";
    private double montantRetenue = 0;

    public ManagedDocDivers() {
        documents = new ArrayList<>();
        pieces = new ArrayList<>();
        typesDocs = new ArrayList<>();
        naturesSearch = new ArrayList<>();
        etapesValidations = new ArrayList<>();
        abonnements = new ArrayList<>();
        statuts = new ArrayList<>();
        profils = new ArrayList<>();
        typesDocs.add(Constantes.TYPE_DOC_CAISSE_CHARGE);
        typesDocs.add(Constantes.TYPE_DOC_CAISSE_CHARGE_TIERS);
    }

    public long getAgencePieceSearch() {
        return agencePieceSearch;
    }

    public void setAgencePieceSearch(long agencePieceSearch) {
        this.agencePieceSearch = agencePieceSearch;
    }

    public boolean isDeleteRetenue() {
        return deleteRetenue;
    }

    public void setDeleteRetenue(boolean deleteRetenue) {
        this.deleteRetenue = deleteRetenue;
    }

    public List<Profil> getProfils() {
        return profils;
    }

    public void setProfils(List<Profil> profils) {
        this.profils = profils;
    }

    public YvsComptaCaisseDocDiversTiers getSelectTiers() {
        return selectTiers;
    }

    public void setSelectTiers(YvsComptaCaisseDocDiversTiers selectTiers) {
        this.selectTiers = selectTiers;
    }

    public TiersDivers getTiers() {
        return tiers;
    }

    public void setTiers(TiersDivers tiers) {
        this.tiers = tiers;
    }

    public long getProjet() {
        return projet;
    }

    public void setProjet(long projet) {
        this.projet = projet;
    }

    public boolean isDate_up() {
        return date_up;
    }

    public void setDate_up(boolean date_up) {
        this.date_up = date_up;
    }

    public Date getDateDebut_() {
        return dateDebut_;
    }

    public void setDateDebut_(Date dateDebut_) {
        this.dateDebut_ = dateDebut_;
    }

    public Date getDateFin_() {
        return dateFin_;
    }

    public void setDateFin_(Date dateFin_) {
        this.dateFin_ = dateFin_;
    }

    public double getMontantRetenue() {
        return this.montantRetenue;
    }

    public void setMontantRetenue(double montantRetenue) {
        this.montantRetenue = montantRetenue;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isChangeNumdocAuto() {
        return changeNumdocAuto;
    }

    public void setChangeNumdocAuto(boolean changeNumdocAuto) {
        this.changeNumdocAuto = changeNumdocAuto;
    }

    public long getNbrComptaSearch() {
        return nbrComptaSearch;
    }

    public void setNbrComptaSearch(long nbrComptaSearch) {
        this.nbrComptaSearch = nbrComptaSearch;
    }

    public long getNbrComptaPieceSearch() {
        return nbrComptaPieceSearch;
    }

    public void setNbrComptaPieceSearch(long nbrComptaPieceSearch) {
        this.nbrComptaPieceSearch = nbrComptaPieceSearch;
    }

    public Boolean getWithTiersSearch() {
        return withTiersSearch;
    }

    public void setWithTiersSearch(Boolean withTiersSearch) {
        this.withTiersSearch = withTiersSearch;
    }

    public Boolean getWithCompteSearch() {
        return withCompteSearch;
    }

    public void setWithCompteSearch(Boolean withCompteSearch) {
        this.withCompteSearch = withCompteSearch;
    }

    public List<YvsComptaAbonementDocDivers> getAbonnements() {
        return abonnements;
    }

    public void setAbonnements(List<YvsComptaAbonementDocDivers> abonnements) {
        this.abonnements = abonnements;
    }

    public Boolean getWithPlanSearch() {
        return withPlanSearch;
    }

    public void setWithPlanSearch(Boolean withPlanSearch) {
        this.withPlanSearch = withPlanSearch;
    }

    public AbonnementDocDivers getAbonnement() {
        return abonnement;
    }

    public void setAbonnement(AbonnementDocDivers abonnement) {
        this.abonnement = abonnement;
    }

    public String getForSearch() {
        return forSearch;
    }

    public void setForSearch(String forSearch) {
        this.forSearch = forSearch;
    }

    public long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public YvsComptaPhasePieceDivers getCurrentPhaseDivers() {
        return currentPhaseDivers;
    }

    public void setCurrentPhaseDivers(YvsComptaPhasePieceDivers currentPhaseDivers) {
        this.currentPhaseDivers = currentPhaseDivers;
    }

    public YvsComptaCaissePieceDivers getSelectExtourne() {
        return selectExtourne;
    }

    public void setSelectExtourne(YvsComptaCaissePieceDivers selectExtourne) {
        this.selectExtourne = selectExtourne;
    }

    public YvsComptaPhasePieceDivers getSelectPhaseDivers() {
        return selectPhaseDivers;
    }

    public void setSelectPhaseDivers(YvsComptaPhasePieceDivers selectPhaseDivers) {
        this.selectPhaseDivers = selectPhaseDivers;
    }

    public double getMontantRetour() {
        return montantRetour;
    }

    public void setMontantRetour(double montantRetour) {
        this.montantRetour = montantRetour;
    }

    public Boolean getComptaRegSearch() {
        return comptaRegSearch;
    }

    public void setComptaRegSearch(Boolean comptaRegSearch) {
        this.comptaRegSearch = comptaRegSearch;
    }

    public Boolean getComptaSearch() {
        return comptaSearch;
    }

    public void setComptaSearch(Boolean comptaSearch) {
        this.comptaSearch = comptaSearch;
    }

    public String getMotifEtape() {
        return motifEtape;
    }

    public void setMotifEtape(String motifEtape) {
        this.motifEtape = motifEtape;
    }

    public boolean isLastEtape() {
        return lastEtape;
    }

    public void setLastEtape(boolean lastEtape) {
        this.lastEtape = lastEtape;
    }

    public String getComparer() {
        return comparer != null ? comparer.trim().length() > 0 ? comparer : ">=" : ">=";
    }

    public void setComparer(String comparer) {
        this.comparer = comparer;
    }

    public boolean isAddMontant() {
        return addMontant;
    }

    public void setAddMontant(boolean addMontant) {
        this.addMontant = addMontant;
    }

    public double getMontantDebut() {
        return montantDebut;
    }

    public void setMontantDebut(double montantDebut) {
        this.montantDebut = montantDebut;
    }

    public double getMontantFin() {
        return montantFin;
    }

    public void setMontantFin(double montantFin) {
        this.montantFin = montantFin;
    }

    public List<String> getStatuts() {
        return statuts;
    }

    public void setStatuts(List<String> statuts) {
        this.statuts = statuts;
    }

    public boolean isToValideLoad() {
        return toValideLoad;
    }

    public void setToValideLoad(boolean toValideLoad) {
        this.toValideLoad = toValideLoad;
    }

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public CentreDocDivers getSection() {
        return section;
    }

    public void setSection(CentreDocDivers section) {
        this.section = section;
    }

    public Boolean getBonSearch() {
        return bonSearch;
    }

    public void setBonSearch(Boolean bonSearch) {
        this.bonSearch = bonSearch;
    }

    public CoutSupDocDivers getCout() {
        return cout;
    }

    public void setCout(CoutSupDocDivers cout) {
        this.cout = cout;
    }

    public TaxeDocDivers getTaxe() {
        return taxe;
    }

    public void setTaxe(TaxeDocDivers taxe) {
        this.taxe = taxe;
    }

    public boolean isDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(boolean dateSearch) {
        this.dateSearch = dateSearch;
    }

    public Date getDateDebutSearch() {
        return dateDebutSearch;
    }

    public void setDateDebutSearch(Date dateDebutSearch) {
        this.dateDebutSearch = dateDebutSearch;
    }

    public Date getDateFinSearch() {
        return dateFinSearch;
    }

    public void setDateFinSearch(Date dateFinSearch) {
        this.dateFinSearch = dateFinSearch;
    }

    public String getNumPieceF() {
        return numPieceF;
    }

    public void setNumPieceF(String numPieceF) {
        this.numPieceF = numPieceF;
    }

    public List<YvsComptaCaissePieceDivers> getPieces() {
        return pieces;
    }

    public void setPieces(List<YvsComptaCaissePieceDivers> pieces) {
        this.pieces = pieces;
    }

    public PaginatorResult<YvsComptaCaissePieceDivers> getP_piece() {
        return p_piece;
    }

    public void setP_piece(PaginatorResult<YvsComptaCaissePieceDivers> p_piece) {
        this.p_piece = p_piece;
    }

    public List<String> getNaturesSearch() {
        return naturesSearch;
    }

    public void setNaturesSearch(List<String> naturesSearch) {
        this.naturesSearch = naturesSearch;
    }

    public boolean isFirst() {
        return _first;
    }

    public void setFirst(boolean _first) {
        this._first = _first;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public String getCodeTiers() {
        return codeTiers;
    }

    public void setCodeTiers(String codeTiers) {
        this.codeTiers = codeTiers;
    }

    public String getNumCompte() {
        return numCompte;
    }

    public void setNumCompte(String numCompte) {
        this.numCompte = numCompte;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public String getNatureSearch() {
        return natureSearch;
    }

    public void setNatureSearch(String natureSearch) {
        this.natureSearch = natureSearch;
    }

    public String getStatutSearch() {
        return statutSearch;
    }

    public void setStatutSearch(String statutSearch) {
        this.statutSearch = statutSearch;
    }

    public boolean isDate() {
        return date;
    }

    public void setDate(boolean date) {
        this.date = date;
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

    public YvsWorkflowValidDocCaisse getCurrentEtape() {
        return currentEtape;
    }

    public void setCurrentEtape(YvsWorkflowValidDocCaisse currentEtape) {
        this.currentEtape = currentEtape;
    }

    public YvsComptaAbonementDocDivers getAbonnCD() {
        return abonnCD;
    }

    public void setAbonnCD(YvsComptaAbonementDocDivers abonnCD) {
        this.abonnCD = abonnCD;
    }

    public List<YvsWorkflowValidDocCaisse> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidDocCaisse> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public void setTypesDocs(List<String> typesDocs) {
        this.typesDocs = typesDocs;
    }

    public List<String> getTypesDocs() {
        return typesDocs;
    }

    public YvsComptaCaisseDocDivers getSelectDoc() {
        return selectDoc;
    }

    public void setSelectDoc(YvsComptaCaisseDocDivers selectDoc) {
        this.selectDoc = selectDoc;
    }

    public String getTabIds_mensualite() {
        return tabIds_mensualite;
    }

    public void setTabIds_mensualite(String tabIds_mensualite) {
        this.tabIds_mensualite = tabIds_mensualite;
    }

    public DocCaissesDivers getDocDivers() {
        return docDivers;
    }

    public void setDocDivers(DocCaissesDivers docDivers) {
        this.docDivers = docDivers;
    }

    public List<YvsComptaCaisseDocDivers> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComptaCaisseDocDivers> documents) {
        this.documents = documents;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public YvsComptaCaissePieceDivers getPieceCD() {
        return pieceCD;
    }

    public void setPieceCD(YvsComptaCaissePieceDivers pieceCD) {
        this.pieceCD = pieceCD;
    }

    public void setValidePiece(boolean validePiece) {
        this.validePiece = validePiece;
    }

    public boolean isValidePiece() {
        return validePiece;
    }

    public Character getStatutRegSearch() {
        return statutRegSearch;
    }

    public void setStatutRegSearch(Character statutRegSearch) {
        this.statutRegSearch = statutRegSearch;
    }

    public String getEgaliteStatutReg() {
        return egaliteStatutReg;
    }

    public void setEgaliteStatutReg(String egaliteStatutReg) {
        this.egaliteStatutReg = egaliteStatutReg;
    }

    public Long getTypeODSearch() {
        return typeODSearch;
    }

    public void setTypeODSearch(Long typeODSearch) {
        this.typeODSearch = typeODSearch;
    }

    @Override
    public void loadAll() {
//        setTiersDefaut();
        loadInfosWarning(false);
        if (statutSearch != null ? statutSearch.trim().length() < 1 : true) {
            this.egaliteStatut = "!=";
            this.statutSearch = Constantes.ETAT_VALIDE;
            addParamStatut(false);
        }
        if (isWarning != null ? isWarning : false) {
            loadByWarning();
        } else {
            addParamToValide();
        }
        loadParam();
        //initialise la caisse
        if (docDivers.getCaisseDefaut() != null ? docDivers.getCaisseDefaut().getId() < 1 : true) {
            YvsBaseCaisse y = findByResponsable(currentUser.getUsers());
            if (y != null) {
                docDivers.setCaisseDefaut(new Caisses(y.getId(), y.getIntitule()));
            }
        }
        if (docDivers.getAgence() != null ? docDivers.getAgence().getId() < 1 : true) {
            docDivers.setAgence(new Agence(currentAgence.getId()));
        }
        if (docDivers.getId() < 1 ? docDivers.getTypeDocDiv() != null ? docDivers.getTypeDocDiv().getId() < 1 : true : false) {
            ManagedTypeDocDivers w = (ManagedTypeDocDivers) giveManagedBean(ManagedTypeDocDivers.class);
            if (w != null ? !w.getTypesDocDivers().isEmpty() : false) {
                docDivers.setTypeDocDiv(new TypeDocDivers(w.getTypesDocDivers().get(0).getId(), w.getTypesDocDivers().get(0).getLibelle()));
            }
        }
    }

    public void loadParam() {
        currentParam = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        if (currentParam == null) {
            currentParam = new YvsComptaParametre();
        }
    }

    public void initViewPc() {
        loadParam();
        if (currentAgence != null) {
            docDivers.setAgence(new Agence(currentAgence.getId()));
        }
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadAllOthers(true, true);
    }

    public void setTiersDefaut() {
        if (docDivers.getTiers() != null ? docDivers.getTiers().getId() < 1 : true) {
            YvsBaseFournisseur c = currentTiersDefault();
            if (c != null ? c.getId() > 0 : false) {
                Tiers ti = UtilTiers.buildBeanTiers(c.getTiers());
                cloneObject(docDivers.getTiers(), ti);
                if (ti.getProfils().size() == 1) {
                    ti.setSelectProfil(ti.getProfils().get(0));
                }
            }
            update("chmp_ct_dd");
        }
    }

    public YvsBaseFournisseur currentTiersDefault() {
        champ = new String[]{"societe", "ville"};
        val = new Object[]{currentAgence.getSociete(), currentAgence.getVille()};
        nameQueri = "YvsBaseFournisseur.findDefautVille";
        YvsBaseFournisseur lc = (YvsBaseFournisseur) dao.loadOneByNameQueries(nameQueri, champ, val);
        if (lc != null) {
            return lc;
        }
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsBaseFournisseur.findDefaut";
        lc = (YvsBaseFournisseur) dao.loadOneByNameQueries(nameQueri, champ, val);
        if (lc != null) {
            getWarningMessage("Pensez à définir un tiers par défaut pour cette agence");
            return lc;
        }
        return null;
    }

    public void load(Boolean load) {

    }

    private int buildDroit() {
        if (autoriser("compta_od_view_all")) {
            return 1;
        }
        if (autoriser("compta_od_view_all_agence")) {
            return 2;
        }
        return 3;
    }

    public void loadAllOthers(boolean avance, boolean init) {
        switch (buildDroit()) {
            case 1:
                paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
                break;
            case 2:
                paginator.addParam(new ParametreRequete("y.agence", "agence", currentAgence, "=", "AND"));
                break;
            default:
                //récupère les ids de mon niveau d'accès
                List<Long> ids = dao.loadNameQueries("YvsBaseUsersAcces.findIdAccesByUsers", new String[]{"users", "societe"}, new Object[]{currentUser.getUsers(), currentAgence.getSociete()});
                if (ids.isEmpty()) {
                    ids.add(0L);
                }
                paginator.addParam(new ParametreRequete("y.typeDoc.codeAcces.id", "acces", ids, " IN ", "AND"));
                break;
        }
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        documents = paginator.executeDynamicQuery("y", "y", "YvsComptaCaisseDocDivers y JOIN FETCH y.agence", "y.dateDoc DESC, y.numPiece DESC", avance, init, (int) imax, dao);
        if (documents.size() == 1) {
            onSelectObject(documents.get(0));
            execute("collapseForm('others')");
        } else {
            execute("collapseList('others')");
        }
        update("data_others");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
//        List<YvsComptaCaisseDocDivers> re = paginator.parcoursDynamicData("YvsComptaCaisseDocDivers", "y", "y.dateDoc DESC, y.numPiece", getOffset(), dao);
        List<YvsComptaCaisseDocDivers> re = paginator.parcoursDynamicData("YvsComptaCaisseDocDivers y JOIN FETCH y.agence", "y", "", "y.dateDoc DESC, y.numPiece DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void loadOtherByStatut(String statut) {
        statutSearch = statut;
        addParamStatut();
    }

    public void loadAllPiece(boolean avancer, boolean init) {
        ParametreRequete p = new ParametreRequete("y.author.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
        p_piece.addParam(p);
        pieces = p_piece.executeDynamicQuery("YvsComptaCaissePieceDivers", "y.datePaimentPrevu DESC", avancer, init, (int) getNbMax(), dao);
    }

    public void loadPieceByStatut(String statut) {
        p_piece.getParams().clear();
        statut_ = statut;
        addParamStatutPieces();
    }

    public void loadOthersStatut(String statut) {
        if (_first) {
            clearParams();
        }
        _first = false;
        statutSearch = statut;
        egaliteStatut = "=";
        paginator.addParam(new ParametreRequete("y.statutDoc", "statutDoc", statutSearch, egaliteStatut, "AND"));
        loadAllOthers(true, true);
    }

    public void addParamStatutPieces() {
        ParametreRequete p = new ParametreRequete("y.statutPiece", "statut", null, "=", "AND");
        if (statut_ != null ? statut_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statutPiece", "statut", statut_.charAt(0), "=", "AND");
        }
        p_piece.addParam(p);
        loadAllPiece(true, true);
    }

    public void addParamReferencePieces() {
        ParametreRequete p = new ParametreRequete("y.numeroPiece", "numeroPiece", null, "=", "AND");
        if (numPieceF != null ? numPieceF.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.numeroPiece)", "numeroPiece", numPieceF.toUpperCase() + "%", "LIKE", "AND");
        }
        p_piece.addParam(p);
        loadAllPiece(true, true);
    }

    public void addParamComptabilisedPieces() {
        ParametreRequete p = new ParametreRequete("coalesce(y.comptabilise, false)", "comptabilise", comptaRegSearch, "=", "AND");
        if (comptaRegSearch != null) {
            String query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_piece_divers c RIGHT JOIN yvs_compta_caisse_piece_divers y ON c.reglement = y.id "
                    + "INNER JOIN yvs_base_caisse e ON y.caisse = e.id INNER JOIN yvs_compta_journaux h ON e.journal = h.id "
                    + "INNER JOIN yvs_agences a ON h.agence = a.id WHERE y.statut_piece = 'P' AND a.societe = ? "
                    + "AND c.id " + (comptaRegSearch ? "IS NOT NULL" : "IS NULL");
            Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1)};
            if (dateSearch) {
                query += " AND y.date_valider BETWEEN ? AND ?";
                param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateDebutSearch, 2), new Options(dateFinSearch, 3)};
            }
            Long count = (Long) dao.loadObjectBySqlQuery(query, param);
            nbrComptaPieceSearch = count != null ? count : 0;
        }
        p_piece.addParam(p);
        loadAllPiece(true, true);
    }

    public void addParamAgencePieces() {
        ParametreRequete p = new ParametreRequete("y.docDivers.agence", "agence", null, "=", "AND");
        if (agencePieceSearch > 0) {
            p = new ParametreRequete("y.docDivers.agence", "agence", new YvsAgences(agencePieceSearch), "=", "AND");
        }
        p_piece.addParam(p);
        loadAllPiece(true, true);
    }

    public void addParamDatesPieces() {
        ParametreRequete p = new ParametreRequete("y.datePiece", "datePiece", null, "=", "AND");
        if (dateSearch) {
            p = new ParametreRequete("y.datePiece", "datePiece", dateDebutSearch, dateFinSearch, "BETWEEN", "AND");
        }
        p_piece.addParam(p);
        loadAllPiece(true, true);
    }

    public void init(boolean next) {
        loadAllOthers(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllOthers(true, true);
    }

    public void choosePaginatorPiece(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllPiece(true, true);
    }

    public void choixTypeMouvement(ValueChangeEvent ev) {
        System.err.println("---- Choix type " + ev.getNewValue());
        if (ev.getNewValue() != null) {
            docDivers.setMouvement((String) ev.getNewValue());
//            choixTypeMouv();
        }
    }

//    public void choixTypeMouv() {
//        boolean is_update = docDivers.getId() > 0;
//        if (docDivers.getMouvement() != null) {
//            typesDocs.clear();
//            switch (docDivers.getMouvement()) {
//                case Constantes.COMPTA_DEPENSE:
//                    typesDocs.add(Constantes.TYPE_DOC_CAISSE_CHARGE);
//                    typesDocs.add(Constantes.TYPE_DOC_CAISSE_CHARGE_TIERS);
//                    break;
//                case Constantes.COMPTA_RECETTE:
//                    typesDocs.add(Constantes.TYPE_DOC_CAISSE_RECETTE);
//                    typesDocs.add(Constantes.TYPE_DOC_CAISSE_RECETTE_TIERS);
//                    break;
//            }
//        }
//    }
    public void choixPlanReglemet(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0 && docDivers.getMontant() > 0) {
                if (!docDivers.getStatutDoc().equals(Constantes.ETAT_ANNULE)) {
                    ManagedModeReglement service = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                    if (service != null) {
                        int idx = service.getModels().indexOf(new YvsBaseModelReglement(id));
                        if (idx >= 0) {
                            YvsBaseModelReglement mdr = service.getModels().get(idx);
                            //decompose mdr
                            if (docDivers.getReglements().isEmpty()) {
                                decomposeMensualiteMdr(mdr);
                            } else {
                                //confirm la génération d'un autre model de paiement
                                if (docDivers.getReglements().get(0).getId() > 0) {
                                    openDialog("dlgConfirmNewP");
                                } else {
                                    decomposeMensualiteMdr(mdr);
                                }
                            }
                        }
                    }
                } else {
                    getErrorMessage("Cette pièce est annulée");
                }
            } else if (id == -1) {
                openDialog("dlgMdr");
            } else if (docDivers.getMontant() <= 0) {
                getErrorMessage("Veuillez entrer le montant !");
            } else {

            }
        } else {
            docDivers.getReglements().clear();
        }
    }

    public void choixPlanAbonement(ValueChangeEvent ev) {
        if (docDivers != null ? docDivers.getId() < 1 : true) {
            getErrorMessage("Vous devez enregistrer une pièce");
            return;
        }
        if (docDivers.getMouvement().equals(Constantes.COMPTA_RECETTE)) {
            getErrorMessage("Vous ne pouvez pas planifier une recette");
            return;
        }
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            if (w.isComptabilise(docDivers.getId(), Constantes.SCR_DIVERS)) {
                getErrorMessage("Cette pièce est déjà comptabilisée");
                return;
            }
        }
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                if (!docDivers.getStatutDoc().equals(Constantes.ETAT_ANNULE)) {
                    ManagedPlanAbonnement wa = (ManagedPlanAbonnement) giveManagedBean(ManagedPlanAbonnement.class);
                    if (wa != null) {
                        int idx = wa.getPlans().indexOf(new YvsComptaPlanAbonnement(id));
                        if (idx >= 0) {
                            YvsComptaPlanAbonnement mdr = wa.getPlans().get(idx);
                            //decompose mdr
                            if (docDivers.getAbonnements().isEmpty()) {
                                decomposeAbonnement(mdr);
                            } else {
                                //confirm la génération d'un autre model de paiement
                                if (docDivers.getAbonnements().get(0).getId() != null ? docDivers.getAbonnements().get(0).getId() > 0 : true) {
                                    openDialog("dlgConfirmNewA");
                                } else {
                                    decomposeAbonnement(mdr);
                                }
                            }
                        }
                    }
                } else {
                    getErrorMessage("Cette pièce est annulée");
                }
            } else if (id == -1) {
                openDialog("dlgPlanA");
            } else if (docDivers.getId() <= 0) {
                getErrorMessage("Veuillez enregistrer la pièce avant !");
            } else {

            }
        }
    }

    public void choisirOneModelR(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBaseModelReglement bean = (YvsBaseModelReglement) ev.getObject();
            docDivers.setPlanReglement(new ModelReglement(bean.getId(), bean.getReference(), bean.getDescription()));
            decomposeMensualiteMdr(bean);
        }
    }

    public void confirmDecomposePlanR() {
        //vérifie qu'aucune pièce n'ai déjà été payé
        for (YvsComptaCaissePieceDivers pc : docDivers.getReglements()) {
            if (pc.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                getErrorMessage("Impossible d'appliquer ce nouveau plan !", "Des Pièces de ce document ont déjà été payés !");
                return;
            }
        }
        //supprime le précédent plan
        try {
            for (YvsComptaCaissePieceDivers pc : docDivers.getReglements()) {
                dao.delete(pc);
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de Continuer !");
            log.log(Level.SEVERE, null, ex);
        }
        ManagedModeReglement service = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
        if (service != null) {
            int idx = service.getModels().indexOf(new YvsBaseModelReglement(docDivers.getPlanReglement().getId()));
            if (idx >= 0) {
                YvsBaseModelReglement mdr = service.getModels().get(idx);
                decomposeMensualiteMdr(mdr);
            }
        }

    }

    public void confirmDecomposePlanA() {
        //vérifie qu'aucune pièce n'ai déjà été payé
        for (YvsComptaAbonementDocDivers pc : docDivers.getAbonnements()) {
            if (pc.getActif()) {
                getErrorMessage("Impossible d'appliquer ce nouveau plan !", "Des Pièces de ce document sont déjà actif !");
                return;
            }
        }
        //supprime le précédent plan
        try {
            for (YvsComptaAbonementDocDivers pc : docDivers.getAbonnements()) {
                dao.delete(pc);
            }
            docDivers.getAbonnements().clear();
        } catch (Exception ex) {
            getErrorMessage("Impossible de Continuer !");
            log.log(Level.SEVERE, null, ex);
        }
        ManagedPlanAbonnement w = (ManagedPlanAbonnement) giveManagedBean(ManagedPlanAbonnement.class);
        if (w != null) {
            int idx = w.getPlans().indexOf(new YvsComptaPlanAbonnement(docDivers.getPlanAbonement().getId()));
            if (idx >= 0) {
                YvsComptaPlanAbonnement mdr = w.getPlans().get(idx);
                decomposeAbonnement(mdr);
            }
        }
    }

    private void decomposeMensualiteMdr(YvsBaseModelReglement mdr) {
        List<YvsBaseTrancheReglement> lt = dao.loadNameQueries("YvsBaseTrancheReglement.findByModel", new String[]{"model"}, new Object[]{mdr});
        long id = -1000;
        YvsComptaCaissePieceDivers piece;
        YvsBaseModeReglement espece = modeEspece();
        YvsBaseModeReglement mode;
        Calendar cal = Calendar.getInstance();
        cal.setTime(docDivers.getDatePaiementPrevu());
        docDivers.getReglements().clear();
        double montant = docDivers.getMontantWithCout();
        if (montant > 0 && docDivers.getDatePaiementPrevu() != null) {
            double somme = 0;
            double valeur = 0;
            double total = montant - docDivers.getTotalPlanifie();
            for (YvsBaseTrancheReglement trch : lt) {
                valeur = total * trch.getTaux() / 100;
                somme += valeur;
                cal.add(Calendar.DAY_OF_MONTH, trch.getIntervalJour());
                if (trch.getMode() != null ? trch.getMode().getId() > 1 : false) {
                    mode = trch.getMode();
                } else {
                    mode = new YvsBaseModeReglement(espece.getId(), espece.getDesignation());
                }
                piece = getPiece(id++, docDivers.getCaisseDefaut(), docDivers, selectDoc, mode, valeur, cal.getTime());
                piece.setBeneficiaire(docDivers.getBeneficiaire());
                piece.setNumeroExterne(docDivers.getNumeroExterne());
                docDivers.getReglements().add(piece);
            }
            if (montant > somme) {
                cal.add(Calendar.DAY_OF_MONTH, 0);
                piece = getPiece(id++, docDivers.getCaisseDefaut(), docDivers, selectDoc, espece, montant - somme, cal.getTime());
                piece.setBeneficiaire(docDivers.getBeneficiaire());
                piece.setNumeroExterne(docDivers.getNumeroExterne());
                docDivers.getReglements().add(piece);
            }
        } else if (selectDoc.getMontant() <= 0) {
            getErrorMessage("Le montant du document n'a pas été trouvé !");
        } else {
            getErrorMessage("Vous devez initialiser la date de paiement !");
        }
    }

    public YvsComptaCaissePieceDivers getPiece(Long id, Caisses caisse, DocCaissesDivers docDivers, YvsComptaCaisseDocDivers selectDoc, YvsBaseModeReglement mode, double montant, Date date) {
        YvsComptaCaissePieceDivers piece = new YvsComptaCaissePieceDivers(id);
        piece.setModePaiement(mode);
        piece.setAuthor(currentUser);
        piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        piece.setMontant(montant);
        piece.setDatePiece(docDivers.getDateDoc());
        piece.setDatePaimentPrevu(date);
        piece.setMouvement(docDivers.getMouvement());
        String numero = genererReference(Constantes.TYPE_PC_DIVERS_NAME, piece.getDatePiece());
        if (numero != null ? numero.trim().length() < 1 : true) {
            return null;
        }
        piece.setNumPiece(numero);
        piece.setDocDivers(selectDoc);
        piece.setDateSave(new Date());
        piece.setDateUpdate(new Date());
        piece.setNote("REGLEMENT OD N° " + docDivers.getNumPiece() + " DU " + formatDate.format(piece.getDatePiece()));
        if (caisse != null ? caisse.getId() > 0 : false) {
            piece.setCaisse(new YvsBaseCaisse(caisse.getId(), caisse.getIntitule()));
        }
        piece.setBeneficiaire(docDivers.getTiers().getSelectProfil().getNomPrenom());
        return piece;
    }

    private void decomposeAbonnement(YvsComptaPlanAbonnement pa) {
        if (pa.getValeur() != null && pa.getTypeValeur() != ' ' && pa.getPeriodicite() != ' ') {
            double baseRetenu = docDivers.getMontantTotal();
            double avance = 0;
            double reste;
            int mens = 1;
            double valeur;
            Calendar cal = Calendar.getInstance();
            cal.setTime(docDivers.getDateDebutPlan());
            docDivers.getAbonnements().clear();
            //on ne planifie que les prélèvemnet non encore réglé
            switch (pa.getTypeValeur()) {
                case 'V':
                    //tant que le montant est inférieure à la dette
                    reste = baseRetenu;
                    while (avance < baseRetenu) {
                        if (pa.getValeur() > reste) {
                            valeur = reste;
                        } else {
                            valeur = pa.getValeur();
                        }
                        YvsComptaAbonementDocDivers d = buildAbonement(-(docDivers.getAbonnements().size() + 1), cal.getTime(), valeur, selectDoc, pa);
                        avance = avance + pa.getValeur();
                        cal.add((pa.getPeriodicite() == 'M') ? Calendar.MONTH : Calendar.YEAR, 1);
                        docDivers.getAbonnements().add(d);
                        mens++;
                        reste = (pa.getValeur() - avance);
                    }
                    break;
                case 'D':
                    avance = (baseRetenu / pa.getValeur());
                    reste = baseRetenu;
                    while (mens <= pa.getValeur()) {
                        reste = (reste - avance);
                        if (mens == pa.getValeur() && reste != 0) {
                            avance += reste;
                        }
                        YvsComptaAbonementDocDivers d = buildAbonement(-(docDivers.getAbonnements().size() + 1), cal.getTime(), avance, selectDoc, pa);
                        cal.add((pa.getPeriodicite() == 'M') ? Calendar.MONTH : Calendar.YEAR, 1);
                        docDivers.getAbonnements().add(d);
                        mens++;
                    }
                    break;
                case 'T':
                    double montantFixe = baseRetenu * pa.getValeur() / 100;
                    while (avance < baseRetenu) {
                        montantFixe = ((baseRetenu - avance) >= montantFixe) ? montantFixe : (docDivers.getResteAPlanifier() - avance);
                        YvsComptaAbonementDocDivers d = buildAbonement(-(docDivers.getAbonnements().size() + 1), cal.getTime(), montantFixe, selectDoc, pa);
                        avance = avance + montantFixe;
                        cal.add(Calendar.MONTH, 1);
                        cal.add((pa.getPeriodicite() == 'M') ? Calendar.MONTH : Calendar.YEAR, 1);
                        docDivers.getAbonnements().add(d);
                        mens++;
                    }
                    break;
                default:
                    break;
            }
            update("tab_prelevement_detail");
        }
    }

    private YvsComptaAbonementDocDivers buildAbonement(long id, Date echeance, double val, YvsComptaCaisseDocDivers doc, YvsComptaPlanAbonnement p) {
        YvsComptaAbonementDocDivers ab = new YvsComptaAbonementDocDivers();
        ab.setActif(false);
        ab.setAuthor(currentUser);
        ab.setDocDivers(doc);
        ab.setEcheance(echeance);
        ab.setLibelle(null);
        ab.setValeur(val);
        ab.setPlan(p);
        ab.setDateSave(new Date());
        ab.setDateUpdate(new Date());
        return ab;
    }

    public void ecouteSaisieCG() {
        String code = docDivers.getCompte().getNumCompte();
        if (code != null ? code.trim().length() > 0 : false) {
            //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
            ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (service != null) {
                service.findCompteByNum(docDivers.getCompte().getNumCompte());
                docDivers.getCompte().setError(service.getListComptes().isEmpty());
                if (service.getListComptes() != null) {
                    if (!service.getListComptes().isEmpty()) {
                        if (service.getListComptes().size() == 1) {
                            docDivers.getCompte().setError(false);
                            cloneObject(docDivers.getCompte(), UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                        } else {
                            docDivers.getCompte().setError(false);
                            openDialog("dlgCmpteG");
                            update("table_cpt_G_dd");
                        }
                    } else {
                        docDivers.getCompte().setError(true);
                    }
                } else {
                    docDivers.getCompte().setError(true);
                }
            }
        }
    }

    public void loadBonProvisoire(SelectEvent ev) {
        if (ev != null) {
            chooseBonProvisoire(((YvsComptaBonProvisoire) ev.getObject()));
            update("blog_bon_provisoire");
        }
    }

    public void openListBons() {
        openDialog("dlgListBons");
    }

    public void ecouteBonProvisoire() {
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        pieceCD.setBonProvisoire(new YvsComptaBonProvisoire());
        ManagedBonProvisoire service = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
        if (service != null) {
            YvsComptaBonProvisoire y = null;
            String num = pieceCD.getNumeroExterne();
            if (num != null ? num.trim().length() > 0 : false) {
                y = service.findBonProvisoirePaye(num, true);
            }
            if (y != null) {
                chooseBonProvisoire(y);
            }
        }
    }

    public void chooseBonProvisoire(YvsComptaBonProvisoire y) {
        if (!y.getStatutPaiement().equals(Constantes.ETAT_REGLE)) {
            getWarningMessage("Le bon provisoire selectionné n'est pas payé !");
        }
        pieceCD.setNumeroExterne(y.getNumero());
        pieceCD.setBonProvisoire(y);
        if (pieceCD.getId() < 1) {
            pieceCD.setMontant(y.getReste());
            pieceCD.setNumPiece(y.getNumero());
            pieceCD.setModePaiement(modeEspece());
            update("form_edit_pc_dd");
        }
        if (pieceCD.getCaisse() != null ? pieceCD.getCaisse().getId() < 1 : true) {
            pieceCD.setCaisse(y.getCaisse());
            update("menu_caisse_piece_divers");
        }
        update("blog_bon_piece_divers");
    }

    public void choisirCompteG(SelectEvent ev) {
        if (ev != null) {
            cloneObject(docDivers.getCompte(), UtilCompta.buildSimpleBeanCompte(((YvsBasePlanComptable) ev.getObject())));
            update("chmp_cg_dd");
        }
    }

    String sourceTiers;

    public void openListTiers(String sourceTiers) {
        this.sourceTiers = sourceTiers;
        openDialog("dlgTiers");
    }

    public void ecouteSaisieTiers() {
        ecouteSaisieTiers("");
    }

    public void ecouteSaisieTiers(String sourceTiers) {
        this.sourceTiers = sourceTiers;
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel    
        ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (service != null) {
            if (!Util.asString(sourceTiers)) {
                String code = docDivers.getTiers().getSelectProfil().getCode();
                if (code != null ? code.trim().length() > 0 : false) {
                    service.findTiersByCode(code);
                    docDivers.getTiers().setError(service.getListTiers().isEmpty());
                    docDivers.getTiers().setId(-1);
                    docDivers.getTiers().setSelectProfil(new Profil());
                    if (service.getListTiers() != null ? !service.getListTiers().isEmpty() : false) {
                        if (service.getListTiers().size() == 1) {
                            YvsBaseTiers y = service.getListTiers().get(0);
                            Tiers tiers = UtilTiers.buildBeanTiers(y);
                            cloneObject(docDivers.getTiers(), tiers);
                            Profil current = new Profil(y.getId(), y.getCodeTiers(), y.getNom(), y.getPrenom(), y.getCompteCollectif(), yvs.dao.salaire.service.Constantes.BASE_TIERS_TIERS, y.getActif(), 3, y.getId(), yvs.dao.salaire.service.Constantes.BASE_TIERS_TIERS, y.getCodeTiers());
                            if (tiers.getProfils().size() > 1) {
                                tiers.getProfils().add(current);
                                //ouvre la boite de dialogue des profiles tiers
                                Profil p = findOneProfil(code);
                                if (p == null) {
                                    profils = new ArrayList<>(tiers.getProfils());
                                    openDialog("dlgProfilTiers");
                                    update("table_profils_tiers");
                                } else {
                                    docDivers.getTiers().setSelectProfil(p);
                                }
                            } else {
                                docDivers.getTiers().setSelectProfil(current);
                            }
                        } else {
                            docDivers.getTiers().setId(-1);
                            docDivers.getTiers().setSelectProfil(new Profil());
                            openDialog("dlgTiers");
                            update("data_tiers_dd");
                        }
                    }
                } else {
                    docDivers.getTiers().setId(-1L);
                }
            } else if (sourceTiers.equals("T")) {
                String code = tiers.getTiers().getSelectProfil().getCode();
                if (code != null ? code.trim().length() > 0 : false) {
                    service.findTiersByCode(code);
                    tiers.getTiers().setError(service.getListTiers().isEmpty());
                    tiers.getTiers().setId(-1);
                    tiers.getTiers().setSelectProfil(new Profil());
                    if (service.getListTiers() != null ? !service.getListTiers().isEmpty() : false) {
                        if (service.getListTiers().size() == 1) {
                            YvsBaseTiers y = service.getListTiers().get(0);
                            Tiers tiers = UtilTiers.buildBeanTiers(y);
                            cloneObject(this.tiers.getTiers(), tiers);
                            Profil current = new Profil(y.getId(), y.getCodeTiers(), y.getNom(), y.getPrenom(), y.getCompteCollectif(), yvs.dao.salaire.service.Constantes.BASE_TIERS_TIERS, y.getActif(), 3, y.getId(), yvs.dao.salaire.service.Constantes.BASE_TIERS_TIERS, y.getCodeTiers());
                            if (tiers.getProfils().size() > 1) {
                                tiers.getProfils().add(current);
                                //ouvre la boite de dialogue des profiles tiers
                                Profil p = findOneProfil(code);
                                if (p == null) {
                                    profils = new ArrayList<>(tiers.getProfils());
                                    openDialog("dlgProfilTiers");
                                    update("table_profils_tiers");
                                } else {
                                    this.tiers.getTiers().setSelectProfil(p);
                                }
                            } else {
                                this.tiers.getTiers().setSelectProfil(current);
                            }
                        } else {
                            tiers.getTiers().setId(-1);
                            tiers.getTiers().setSelectProfil(new Profil());
                            openDialog("dlgTiers");
                            update("data_tiers_dd");
                        }
                    }
                } else {
                    tiers.getTiers().setId(-1L);
                }
                update("chmp_ctt_dd");
            } else {
                String num = pieceCD.getBeneficiaire();
                if ((num != null) ? !num.isEmpty() : false) {
                    service.findTiersByCode(num);
                    if (service.getListTiers() != null) {
                        if (!service.getListTiers().isEmpty()) {
                            if (service.getListTiers().size() == 1) {
                                pieceCD.setBeneficiaire(service.getListTiers().get(0).getNom_prenom());
                            } else {
                                openDialog("dlgTiers");
                                update("data_tiers_dd");
                            }
                        }
                    }
                }
            }
        }
    }

    public void choisirCompteTiers(SelectEvent ev) {
        if (ev != null) {
            YvsBaseTiers y = (YvsBaseTiers) ev.getObject();
            if (!Util.asString(sourceTiers)) {
                Tiers tiers = UtilTiers.buildBeanTiers(y);
                if (tiers.getProfils().size() > 1) {
                    //ouvre la boite de dialogue des profiles tiers   
                    cloneObject(docDivers.getTiers(), tiers);
                    profils = new ArrayList<>(tiers.getProfils());
                    openDialog("dlgProfilTiers");
                    update("table_profils_tiers");
                } else {
                    if (tiers.getProfils().size() == 1) {
                        cloneObject(docDivers.getTiers(), tiers);
                        docDivers.getTiers().setSelectProfil(tiers.getProfils().get(0));
                    } else {
                        docDivers.getTiers().setError(true);
                        docDivers.getTiers().setId(-1);
                        docDivers.getTiers().setSelectProfil(new Profil());
                        getWarningMessage("Aucun compte tiers n'a été trouvé avec ce code !");
                    }
                }
            } else if (sourceTiers.equals("T")) {
                Tiers tiers = UtilTiers.buildBeanTiers(y);
                if (tiers.getProfils().size() > 1) {
                    //ouvre la boite de dialogue des profiles tiers  
                    cloneObject(this.tiers.getTiers(), tiers);
                    profils = new ArrayList<>(tiers.getProfils());
                    openDialog("dlgProfilTiers");
                    update("table_profils_tiers");
                } else {
                    if (tiers.getProfils().size() == 1) {
                        cloneObject(this.tiers.getTiers(), tiers);
                        this.tiers.getTiers().setSelectProfil(tiers.getProfils().get(0));
                    } else {
                        this.tiers.getTiers().setError(true);
                        this.tiers.getTiers().setId(-1);
                        this.tiers.getTiers().setSelectProfil(new Profil());
                        getWarningMessage("Aucun compte tiers n'a été trouvé avec ce code !");
                    }
                }
                update("chmp_ctt_dd");
            } else {
                pieceCD.setBeneficiaire(y.getNom_prenom());
            }
        }
    }

    private Profil findOneProfil(String code) {
        int count = 0;
        Profil pp = null;
        for (Profil p : docDivers.getTiers().getProfils()) {
            if (p.getCode().startsWith(code) || p.getNomPrenom().startsWith(code)) {
                count++;
                pp = p;
            }
        }
        return (count == 1) ? pp : null;
    }

    public void choisirProfils(SelectEvent ev) {
        if (ev != null) {
            Profil y = (Profil) ev.getObject();
            if (!Util.asString(sourceTiers)) {
                docDivers.getTiers().setSelectProfil(y);
            } else if (sourceTiers.equals("T")) {
                this.tiers.getTiers().setSelectProfil(y);
                update("chmp_ctt_dd");
            }
        }
    }

    private boolean controleFiche(CoutSupDocDivers bean) {
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            if (w.isComptabilise(docDivers.getId(), Constantes.SCR_DIVERS)) {
                getErrorMessage("Cette pièce est déjà comptabilisée");
                return false;
            }
        }
        if (!canEditDoc() ? !autoriser("d_div_update_doc_valid") : false) {
            openNotAccesByCode();
            return false;
        }
        if (bean == null) {
            return false;
        }
        if (bean.getDocDivers() != null ? bean.getDocDivers().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le document divers");
            return false;
        }
        if (bean.getType() != null ? bean.getType().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le type de coût");
            return false;
        }
        return true;
    }

    public void saveNewCout() {
        String action = cout.getId() > 0 ? "Modification" : "insertion";
        try {
            cout.setDocDivers(docDivers);
            if (controleFiche(cout)) {
                YvsComptaCoutSupDocDivers y = UtilCompta.buildCoutSupDocDivers(cout, currentUser);
                if (cout.getId() < 1) {
                    y.setId(null);
                    y = (YvsComptaCoutSupDocDivers) dao.save1(y);
                } else {
                    dao.update(y);
                }
                int idx = docDivers.getCouts().indexOf(y);
                if (idx > -1) {
                    docDivers.getCouts().set(idx, y);
                } else {
                    docDivers.getCouts().add(y);
                }
//                int idx1 = documents.indexOf(selectDoc);
//                if (idx1 >= 0) {
//                    documents.get(idx1).getCouts().add(y);
//                }
                cout = new CoutSupDocDivers();
                succes();
                update("txt_couts_others");
                update("date_cout_others");
                update("form_cout_others");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error " + action, ex);
        }
    }

    private boolean controleFiche(TaxeDocDivers bean) {
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            if (w.isComptabilise(docDivers.getId(), Constantes.SCR_DIVERS)) {
                getErrorMessage("Cette pièce est déjà comptabilisée");
                return false;
            }
        }
        if (!canEditDoc() ? !autoriser("d_div_update_doc_valid") : false) {
            openNotAccesByCode();
            return false;
        }
        if (bean == null) {
            return false;
        }
        if (bean.getDocDivers() != null ? bean.getDocDivers().getId() < 1 : true) {
            getErrorMessage("Cette taxe ne fait référence à aucune pièce");
            return false;
        }
        if (bean.getTaxe() != null ? bean.getTaxe().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le taxe");
            return false;
        }
        return true;
    }

    private boolean controleFiche(CentreDocDivers bean) {
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            if (w.isComptabilise(docDivers.getId(), Constantes.SCR_DIVERS)) {
                getErrorMessage("Cette pièce est déjà comptabilisée");
                return false;
            }
        }
        if (!canEditDoc() ? !autoriser("d_div_update_doc_valid") : false) {
            openNotAccesByCode();
            return false;
        }
        if (bean == null) {
            return false;
        }
        if (bean.getDocDivers() != null ? bean.getDocDivers().getId() < 1 : true) {
            getErrorMessage("Cette taxe ne fait référence à aucune pièce");
            return false;
        }
        if (bean.getCentre() != null ? bean.getCentre().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le centre analytique");
            return false;
        }
        if (!docDivers.getCompte().isSaisieAnalytique()) {
            getErrorMessage("Le compte que vous avez choisi n'autorise pas la saisie analytique");
            return false;
        }
        double montant = 0;
        for (YvsComptaCentreDocDivers c : docDivers.getSections()) {
            if (c.getId() != bean.getId()) {
                montant = montant + c.getMontant();
            }
        }
        montant = montant + bean.getMontant();
        if (montant > docDivers.getMontantTotal()) {
            getErrorMessage("La répartition ne peut être supérieure au montant du document !");
            return false;
        }
        return true;
    }

    private YvsWorkflowValidDocCaisse giveCurrentStep() {
        ordonneEtapes(docDivers.getEtapesValidations());
        for (YvsWorkflowValidDocCaisse c : docDivers.getEtapesValidations()) {
            if (!c.getEtapeValid()) {
                return c;
            }
        }
        return null;
    }

    private boolean etapeAutorise(YvsNiveauAcces niveau, YvsWorkflowEtapeValidation et) {
        for (YvsWorkflowAutorisationValidDoc c : et.getAutorisations()) {
            if (c.getNiveauAcces().equals(niveau)) {
//                return c.getCanValide() && c.getEtapeValide().getCanUpdateHere();
                return true;
            }
        }
        return false;
    }

    private boolean canEditDoc() {
        if (!docDivers.getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
            if (!docDivers.getEtapesValidations().isEmpty()) {
                //récupère l'Etape courante
                YvsWorkflowValidDocCaisse step = giveCurrentStep();
                if (step != null) {
                    return etapeAutorise(currentUser.getUsers().getNiveauAcces(), step.getEtape());
                }
            } else if (!docDivers.getStatutDoc().equals(Constantes.ETAT_EDITABLE)) {
                return autoriser("d_div_update_doc");
            } else {
                return true;
            }
        }
        return false;
    }

    public void saveNewPlanAbonnement() {
        if (docDivers.getPlanAbonement().getReference() != null && docDivers.getPlanAbonement().getBasePlan() != ' ' && docDivers.getPlanAbonement().getPeriodicite() != ' ') {
            if (docDivers.getPlanAbonement().getValeur() > 0) {
                ManagedPlanAbonnement w = (ManagedPlanAbonnement) giveManagedBean(ManagedPlanAbonnement.class);
                if (w != null) {
                    YvsComptaPlanAbonnement pa = w.saveNew(docDivers.getPlanAbonement());
                    docDivers.setPlanAbonement(new PlanDecoupage());
                    update("data_abonnement_others");
                }
            } else {
                getErrorMessage("Entrer la valeur !");
            }
        } else if (docDivers.getPlanAbonement().getReference() == null) {
            getErrorMessage("Vous devez indiquer une désignation !");
        } else if (docDivers.getPlanAbonement().getBasePlan() == ' ') {
            getErrorMessage("Précisé la base du découpage !");
        } else if (docDivers.getPlanAbonement().getPeriodicite() == ' ') {
            getErrorMessage("Précisé la périodicité de l'abonnement !");
        }
    }

    public void loadPlanAbToView(SelectEvent ev) {
        if (ev != null) {
            YvsComptaPlanAbonnement pa = (YvsComptaPlanAbonnement) ev.getObject();
            docDivers.setPlanAbonement(UtilCompta.buildBeanPlanAbonnement(pa));
        }
    }

    public void loadPlanAbToDel(YvsComptaPlanAbonnement pa) {
        docDivers.setPlanAbonement(UtilCompta.buildBeanPlanAbonnement(pa));
    }

    public void deleteOnePlanAb() {
        if (docDivers.getPlanAbonement().getId() > 0) {
            ManagedPlanAbonnement w = (ManagedPlanAbonnement) giveManagedBean(ManagedPlanAbonnement.class);
            if (w != null) {
                w.deleteBean(new YvsComptaPlanAbonnement(docDivers.getPlanAbonement().getId()));
            }
            docDivers.setPlanAbonement(new PlanDecoupage());
        }
    }

    /**
     * **********************************************************************************************************
     * @param lp
     * @return
     */
    public double giveSoePlanifie(List<YvsComptaCaissePieceDivers> lp) {
        double re = 0;
        if (lp != null) {
            for (YvsComptaCaissePieceDivers c : lp) {
                if (c.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU && c.getStatutPiece() != Constantes.STATUT_DOC_ANNULE) {
                    re += c.getMontant();
                }
            }
        }
        return re;
    }

    public YvsComptaCaisseDocDivers buildDocDivers(DocCaissesDivers y) {
        if (y != null) {
            ManagedAgence service = (ManagedAgence) giveManagedBean(ManagedAgence.class);
            YvsComptaCaisseDocDivers d = UtilCompta.buildDocDivers(y);
            if (service != null) {
                int idx = service.getAgences().indexOf(d.getAgence());
                if (idx >= 0) {
                    d.setAgence(service.getAgences().get(idx));
                }
            }
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                d.setAuthor(currentUser);
            }
            d.setSociete(currentAgence.getSociete());
            return d;
        }
        return null;
    }

    public YvsComptaCaisseMensualiteDocDivers buildMensualiteDocDivers(MensualiteDocDivers y) {
        YvsComptaCaisseMensualiteDocDivers m = new YvsComptaCaisseMensualiteDocDivers();
        if (y != null) {
            m.setDateMensualite((y.getDateMensualite() != null) ? y.getDateMensualite() : new Date());
            m.setEtat(y.getEtat() != null ? y.getEtat() : Constantes.ETAT_EDITABLE);
            m.setId(y.getId());
            m.setMontant(y.getMontant());
            m.setDocDivers(selectDoc);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                m.setAuthor(currentUser);
            }
        }
        return m;
    }

    @Override
    public DocCaissesDivers recopieView() {
        DocCaissesDivers d = new DocCaissesDivers();
        d.setActif(true);
        d.setId(docDivers.getId());
        d.setNumPiece(docDivers.getNumPiece());
        d.setDescription(docDivers.getDescription());
        d.setDatePaiementPrevu((docDivers.getDatePaiementPrevu() != null) ? docDivers.getDatePaiementPrevu() : new Date());
        d.setMontant(docDivers.getMontant());
        d.setTiers(docDivers.getTiers());
        d.setMouvement((docDivers.getMouvement() != null) ? docDivers.getMouvement() : Constantes.MOUV_CAISS_SORTIE);
        return d;
    }

    public MensualiteDocDivers recopieViewMensualite(DocCaissesDivers doc) {
        MensualiteDocDivers m = new MensualiteDocDivers();
//        m.setDateMensualite((mensualite.getDateMensualite() != null) ? mensualite.getDateMensualite() : new Date());
//        m.setEtat(mensualite.getEtat());
//        m.setId(mensualite.getId());
//        m.setMontant(mensualite.getMontant());
//        m.setUpdate(mensualite.isUpdate());
//        m.setDocDivers(doc);
        m.setNew_(true);
        return m;
    }

    @Override
    public boolean controleFiche(DocCaissesDivers bean) {
        return controleFiche(bean, selectDoc, true);
    }

    public boolean controleFiche(DocCaissesDivers bean, YvsComptaCaisseDocDivers selectDoc, boolean msg) {
        if (bean.getAgence().getId() <= 0) {
            getErrorMessage("Aucune agence n'a été trouvé !");
            return false;
        }
//        if (bean.getTypeDocDiv().getId() <= 0) {
//            getErrorMessage("Vous devez indiquer un type d'opérations !");
//            return false;
//        }
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            if (w.isComptabilise(bean.getId(), Constantes.SCR_DIVERS)) {
                if (msg) {
                    getErrorMessage("Cette pièce est déjà comptabilisée");
                }
                return false;
            }
        }
        if (!bean.canEditable()) {
            if (!autoriser("d_div_update_doc_valid") || bean.getId() < 1 || bean.getMontant() != selectDoc.getMontant()) {
                if (msg) {
                    getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
                }
                return false;
            }
        }
        if (bean.getMontant() <= 0) {
            if (msg) {
                getErrorMessage("Vous devez entrer le montant ");
//                getErrorMessage(LoaderProperty.loadPropertie("LCF_PC_montant"));
            }
            return false;
        }
        if (bean.getDescription() == null) {
            if (msg) {
                getErrorMessage("Vous devez entrer le motif de ce document de caisse");
            }
            return false;
        }
        if ((selectDoc != null ? selectDoc.getId() > 0 ? !selectDoc.getDateDoc().equals(bean.getDateDoc()) : false : false)
                || (bean.getNumPiece() == null || bean.getNumPiece().trim().length() < 1)) {
            String ref = genererReference((bean.getMouvement().equals(Constantes.COMPTA_RECETTE) ? Constantes.TYPE_OD_RECETTE_NAME : Constantes.TYPE_OD_DEPENSE_NAME), bean.getDateDoc());
            if ((ref != null) ? ref.trim().equals("") : true) {
                return false;
            }
            bean.setNumPiece(ref);
        }
        if (bean.getId() > 0) {
            if (!bean.getMouvement().equals(selectDoc.getMouvement())) {
                if (!bean.getStatutDoc().equals(Constantes.ETAT_EDITABLE)) {
                    if (msg) {
                        getErrorMessage("Vous pouvez pas modifier la nature du document... Il doit être éditable");
                    }
                    return false;
                }
                if (!bean.getReglements().isEmpty()) {
                    if (msg) {
                        getErrorMessage("Vous pouvez pas modifier la nature du document... Il est déjà planifié");
                    }
                    return false;
                }
            }
            if (changeTydocDivers(bean, selectDoc)) {
                if (!bean.getStatutDoc().equals(Constantes.ETAT_EDITABLE)) {
                    if (msg) {
                        getErrorMessage("Vous pouvez pas modifier la nature du document... Il doit être éditable");
                    }
                    return false;
                }
            }
            if (!bean.getReglements().isEmpty()) {
                return controleMontant(null, true);
            }
        }
        return verifyDate(bean.getDateDoc(), 0);
    }

    private boolean changeTydocDivers(DocCaissesDivers bean, YvsComptaCaisseDocDivers selectDoc) {
        if (selectDoc.getTypeDoc() == null && bean.getTypeDocDiv().getId() > 0) {
            return true;
        }
        if (selectDoc.getTypeDoc() != null ? selectDoc.getTypeDoc().getId() != bean.getTypeDocDiv().getId() : false) {
            return true;
        }
        return false;
    }

    private boolean _controleFiche_(YvsComptaCaisseDocDivers bean) {
        if (bean == null) {
            getErrorMessage("Vous devez selectionner un document");
            return false;
        }
        if (!bean.canEditable()) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        return (giveExerciceActif(bean.getDateDoc()) != null);
    }

    private boolean controleMontant(YvsComptaCaissePieceDivers bean, boolean msg) {
        double mtant = 0;
        for (YvsComptaCaissePieceDivers m : docDivers.getReglements()) {
            if (m.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU) {
                mtant += m.getMontant();
            }
        }
        if (bean != null) {
            if (!docDivers.getReglements().contains(bean)) {
                mtant += bean.getMontant();
            } else {
                double d = docDivers.getReglements().get(docDivers.getReglements().indexOf(bean)).getMontant();
                mtant += (-d + bean.getMontant());
            }
        }
        if (bean != null) {
            if (mtant > (bean.isSelect() ? docDivers.getMontantTotal() : docDivers.getMontantWithCout())) {
                if (msg) {
                    getErrorMessage("La somme des montants des mensualités doit être égale au montant du document!");
                }
                return false;
            }
        } else {
            if (mtant > docDivers.getMontantTotal()) {
                if (msg) {
                    getErrorMessage("La somme des montants des mensualités doit être égale au montant du document!");
                }
                return false;
            }
        }
        return true;
    }

    public boolean controleFicheMensualite(YvsComptaCaissePieceDivers bean, boolean msg) {
        if ((bean.getDocDivers() != null) ? ((bean.getDocDivers().getId() != null) ? bean.getDocDivers().getId() <= 0 : false) : true) {
            if (msg) {
                getErrorMessage("Vous devez Enregistrer la pièce source");
            }
            return false;
        }
        if (bean.getMontant() < 1) {
            if (msg) {
                getErrorMessage("Vous devez entrer un montant");
            }
            return false;
        }
        if (bean.getBonProvisoire() != null ? bean.getBonProvisoire().getId() > 0 : false) {
            if (bean.isJustificatif() ? !bean.getBonProvisoire().getId().equals(bean.getJustify().getBon().getId()) : true) {
                if (bean.getBonProvisoire().getReste() < bean.getMontant()) {
                    getErrorMessage("Vous ne pouvez pas justifier ce montant sur ce bon provisoire");
                    return false;
                }
            }
        }
        if (bean.getNote().trim().length() < 1) {
            bean.setNote("REGLEMENT OD N° " + docDivers.getNumPiece() + " DU " + formatDate.format(bean.getDatePiece()));
        }
        if (!controleMontant(bean, msg)) {
            return false;
        }
        if (bean.getId() < 1) {
            bean.setMouvement(docDivers.getMouvement());
            String ref = genererReference(Constantes.TYPE_PC_DIVERS_NAME, bean.getDatePiece());
            if (ref != null ? ref.trim().length() < 1 : true) {
                return false;
            }
            bean.setNumPiece(ref);
        }
        YvsBaseExercice y = giveExerciceActif(bean.getDatePiece());
        if (y != null ? y.getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Aucun exercice trouvé pour l'opération du " + formatDate.format(bean.getDatePiece()));
            }
            return false;
        }
        return true;
    }

    public boolean controleValeurMensualite(YvsComptaCaissePieceDivers bean) {
        return controleValeurMensualite(bean, docDivers);
    }

    public boolean controleValeurMensualite(YvsComptaCaissePieceDivers bean, DocCaissesDivers docDivers) {
        return controleValeurMensualite(bean, selectDoc, true);
    }

    public boolean controleValeurMensualite(YvsComptaCaissePieceDivers bean, YvsComptaCaisseDocDivers docDivers, boolean msg) {
        if (bean.getModePaiement() != null ? bean.getModePaiement().getId() <= 0 : true) {
            getErrorMessage("Aucun mode de paiement n'a été renseigné pour cette pièce");
            return false;
        }
        if ((bean.getDocDivers() != null) ? ((bean.getDocDivers().getId() != null) ? bean.getDocDivers().getId() <= 0 : false) : true) {
            if (msg) {
                getErrorMessage("Vous devez Enregistrer la pièce source");
            }
            return false;
        }
        if (bean.getMontant() < 1) {
            if (msg) {
                getErrorMessage("Vous devez entrer un montant");
            }
            return false;
        }
        double mtant = 0;
        for (YvsComptaCaissePieceDivers m : docDivers.getReglements()) {
            if (m.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU) {
                mtant += m.getMontant();
            }
        }
        if (mtant > (docDivers.getMontantTotal() + docDivers.getCout())) {
            if (msg) {
                getErrorMessage("La somme des montants des mensualités doit être égale au montant du document!");
            }
            return false;
        }
        if (!verifyDate(bean.getDatePiece(), 0)) {
            return false;
        }
        return (giveExerciceActif(bean.getDatePiece()) != null);
    }

    @Override
    public void populateView(DocCaissesDivers bean) {
        cloneObject(docDivers, bean);
        docDivers.setTaxes(new ArrayList<>(bean.getTaxes()));
        docDivers.setTotalPlanifie(giveSoePlanifie(docDivers.getReglements()));
        if (bean.getEtapesValidations() != null ? !bean.getEtapesValidations().isEmpty() : false) {
            docDivers.setFirstEtape(bean.getEtapesValidations().get(0).getEtape().getLabelStatut());
        }
//        choixTypeMouv();
    }

    public void populateViewMensualite(MensualiteDocDivers bean) {
//        cloneObject(mensualite, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(docDivers);
        docDivers.setComptabilise(false);
        docDivers.setAgence(new Agence(currentAgence.getId()));
        docDivers.setMouvement(Constantes.MOUV_CAISS_SORTIE);
        docDivers.setStatutDoc(Constantes.ETAT_EDITABLE);
        docDivers.setStatutRegle(Constantes.STATUT_DOC_ATTENTE);
        docDivers.setTiers(new Tiers());
        docDivers.setCompte(new Comptes());
        docDivers.setPlanAbonement(new PlanDecoupage());
        docDivers.setPlanReglement(new ModelReglement());
        docDivers.setAuthor(new Users());
        docDivers.getAbonnements().clear();
        docDivers.getReglements().clear();
        docDivers.getEtapesValidations().clear();
        docDivers.setCaisse(new Caisses());
        docDivers.setCaisseDefaut(new Caisses());
        docDivers.getTaxes().clear();
        docDivers.getSections().clear();
        docDivers.getCouts().clear();
        selectDoc = new YvsComptaCaisseDocDivers();
        tabIds = "";
        ManagedTypeDocDivers w = (ManagedTypeDocDivers) giveManagedBean(ManagedTypeDocDivers.class);
        if (w != null ? !w.getTypesDocDivers().isEmpty() : false) {
            docDivers.setTypeDocDiv(new TypeDocDivers(w.getTypesDocDivers().get(0).getId(), w.getTypesDocDivers().get(0).getLibelle()));
        } else {
            docDivers.setTypeDocDiv(new TypeDocDivers());
        }
//        setTiersDefaut();
        resetFichePiece();
        update("etapes_valide_dcd");
        update("blog_form_others");
    }

    public void resetFichePiece() {
        pieceCD = new YvsComptaCaissePieceDivers(-1L);
        pieceCD.setModePaiement(new YvsBaseModeReglement(-1l));
//        if (selectDoc != null ? selectDoc.getTiers() != null ? selectDoc.getTiers().getId() > 0 : false : false) {
//            if (pieceCD.getBeneficiaire() != null ? pieceCD.getBeneficiaire().trim().isEmpty() : true) {
//                pieceCD.setBeneficiaire(selectDoc.getTiers().getNom_prenom());
//            }
//        }
        ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (w != null && selectDoc.getIdTiers() > 0) {
            Tiers t = w.buildTiersByProfil(selectDoc.getIdTiers(), selectDoc.getTableTiers());
        }
        tabIds_mensualite = "";
    }

    public void genererEtapesValidation(YvsComptaCaisseDocDivers selectDoc) {
        List<YvsWorkflowEtapeValidation> etapes = saveEtapesValidation(selectDoc);
        selectDoc.setEtapeTotal(etapes != null ? etapes.size() : 0);
        selectDoc.setEtapesValidations(saveEtapesValidation(selectDoc, etapes));
        dao.update(selectDoc);
        int idx = documents.indexOf(selectDoc);
        if (idx >= 0) {
            documents.set(idx, selectDoc);
        }
        succes();
    }

    public void equilibreOne(YvsComptaCaisseDocDivers doc) {
        equilibreOne(doc, true);
    }

    public void equilibreOne(YvsComptaCaisseDocDivers doc, boolean msg) {
        if ((doc != null) ? (doc.getId() != null ? doc.getId() > 0 : false) : false) {
            dao.getEquilibreDocDivers(doc.getId());
            doc = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{doc.getId()});
            int idx = documents.indexOf(doc);
            if (idx > -1) {
                documents.set(idx, doc);
                update("data_others");
            }
            if (msg) {
                succes();
            }
        }
    }

    @Override
    public boolean saveNew() {
        return saveNew(true);
    }

    public boolean saveNew(boolean msg) {
        try {
            long tiers = docDivers != null ? docDivers.getTiers().getSelectProfil().getIdTiers() : 0;
            double montant = selectDoc != null ? selectDoc.getMontant() : 0;
            if (controleFiche(docDivers, selectDoc, msg)) {
                boolean generer_workflow = false;
                if (docDivers.getId() > 0 ? (selectDoc != null ? !docDivers.getMouvement().equals(selectDoc.getMouvement()) : false) : false) {
                    String query = "DELETE FROM yvs_workflow_valid_doc_caisse WHERE doc_caisse = ?";
                    dao.requeteLibre(query, new Options[]{new Options(docDivers.getId(), 1)});
                    if (!docDivers.getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                        docDivers.setStatutDoc(Constantes.ETAT_EDITABLE);
                    }
                    generer_workflow = true;
                }
                if (changeTydocDivers(docDivers, selectDoc)) {
                    if (docDivers.getStatutDoc().equals(Constantes.ETAT_EDITABLE)) {
                        generer_workflow = true;
                    }
                }

                selectDoc = buildDocDivers(docDivers);
                List<YvsWorkflowEtapeValidation> etapes = new ArrayList<>();
                if (docDivers.getId() <= 0 || generer_workflow) {
                    etapes = saveEtapesValidation(selectDoc);
                    docDivers.setEtapeValide(0);
                    docDivers.setEtapeTotal(etapes != null ? etapes.size() : 0);
                    selectDoc.setEtapeValide(docDivers.getEtapeValide());
                    selectDoc.setEtapeTotal(docDivers.getEtapeTotal());
                }
                if (docDivers.getId() <= 0) {
                    selectDoc.setDateSave(new Date());
                    selectDoc.setId(null);
                    selectDoc = (YvsComptaCaisseDocDivers) dao.save1(selectDoc);
                    docDivers.setEtapesValidations(saveEtapesValidation(selectDoc, etapes));
                    docDivers.setId(selectDoc.getId());
                    selectDoc.setEtapesValidations(new ArrayList<>(docDivers.getEtapesValidations()));

                    if (selectDoc.getIdTiers() > 0) {
                        YvsComptaCaisseDocDiversTiers y = new YvsComptaCaisseDocDiversTiers();
                        y.setIdTiers(selectDoc.getIdTiers());
                        y.setTableTiers(selectDoc.getTableTiers());
                        y.setMontant(selectDoc.getMontantTotal());
                        y.setDocDivers(selectDoc);
                        y.setAuthor(currentUser);
                        y = (YvsComptaCaisseDocDiversTiers) dao.save1(y);
                        docDivers.getListTiers().add(y);
                        if (!selectDoc.getTiers().contains(y)) {
                            selectDoc.getTiers().add(y);
                        }
                        update("data_tiers_others");
                    }
                } else {
                    //vérifier 
                    if (generer_workflow) {
                        docDivers.setEtapesValidations(saveEtapesValidation(selectDoc, etapes));
                        selectDoc.setEtapesValidations(new ArrayList<>(docDivers.getEtapesValidations()));
                    } else {
                        selectDoc.setEtapesValidations(docDivers.getEtapesValidations());
                    }
                    dao.update(selectDoc);
                    if (docDivers.getMontant() != montant) {
                        updateBean();
                    }
                    if (selectDoc.getIdTiers() != null ? selectDoc.getIdTiers() > 0 ? !selectDoc.getIdTiers().equals(tiers) : false : false) {
                        String query = "UPDATE yvs_compta_caisse_piece_divers SET date_update = current_date, beneficiaire = ? WHERE doc_divers = ? AND beneficiaire IS NULL";
                        dao.requeteLibre(query, new Options[]{new Options(docDivers.getTiers().getSelectProfil().getNomPrenom(), 1), new Options(selectDoc.getId(), 2)});
                    }
                    //si la propriété bon est selectionné, et que des pièce de caisses ont déjà été généré
                }
                if (selectDoc.getIdTiers() > 0) {
                    if (pieceCD.getBeneficiaire() != null ? pieceCD.getBeneficiaire().trim().isEmpty() : true) {
                        pieceCD.setBeneficiaire(docDivers.getTiers().getSelectProfil().getNomPrenom());
                    }
                }
                int idx = documents.indexOf(selectDoc);
                if (idx >= 0) {
                    documents.set(idx, selectDoc);
                } else {
                    documents.add(0, selectDoc);
                }
                docDivers.setTotalPlanifie(giveSoePlanifie(docDivers.getReglements()));
                actionOpenOrResetAfter(this);
                if (msg) {
                    succes();
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Doc Divers Error...", ex);
        }
        return false;
    }

    public void insertOrDeleteJustif(boolean delete) {
        if (delete) {
            if (pieceCD.getJustify() != null ? pieceCD.getJustify().getId() > 0 : false) {
                dao.delete(pieceCD.getJustify());
                pieceCD.setJustify(new YvsComptaJustificatifBon());
            }
        } else {
            if (pieceCD.getBonProvisoire() != null ? pieceCD.getBonProvisoire().getId() > 0 : false) {
                if (pieceCD.getJustify() != null ? pieceCD.getJustify().getId() > 0 : false) {
                    if (pieceCD.getJustify().getBon() != null ? pieceCD.getJustify().getBon().getId() > 0 : false) {
                        if (!Objects.equals(pieceCD.getJustify().getBon().getId(), pieceCD.getBonProvisoire().getId())) {
                            dao.delete(pieceCD.getJustify());
                            pieceCD.setJustify(new YvsComptaJustificatifBon());
                        }
                    }
                }
            } else {
                insertOrDeleteJustif(true);
                return;
            }
            champ = new String[]{"bon", "piece"};
            val = new Object[]{pieceCD.getBonProvisoire(), pieceCD};
            YvsComptaJustificatifBon y = (YvsComptaJustificatifBon) dao.loadOneByNameQueries("YvsComptaJustificatifBon.findOne", champ, val);
            if (y != null ? y.getId() < 1 : true) {
                y = new YvsComptaJustificatifBon(pieceCD.getBonProvisoire(), pieceCD);
                y.setAuthor(currentUser);
                y.setId(null);
                y = (YvsComptaJustificatifBon) dao.save1(y);
            }
            pieceCD.setJustify(y);
        }
    }

    public void openToValidDocDivers(YvsComptaCaisseDocDivers dd) {
        selectDoc = dd;
        populateView(UtilCompta.buildBeanDocCaisse(dd));
        openDialog("dlgValidDoc");
    }

    public void confirmCancelDocDiver(boolean force, boolean suspend) {
        if (docDivers.getId() > 0) {
            int i = 0;
            if (docDivers.getStatutDoc().equals(Constantes.ETAT_ENCOURS) && !autoriser("compta_od_statut_encours_to_edit")) {
                openNotAcces();
                return;
            }
            if (docDivers.getStatutDoc().equals(Constantes.ETAT_VALIDE) && !autoriser("compta_od_statut_valid_to_edit")) {
                openNotAcces();
                return;
            }
            //le document ne doit contenir aucune pièce déjà payé
            for (YvsComptaCaissePieceDivers p : docDivers.getReglements()) {
                if (p.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                    getErrorMessage("Impossible de modifier cette pièce. elle contient des règlements déjà payé !");
                    return;
                }
            }
            YvsComptaCaisseDocDivers dd = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{docDivers.getId()});
            if (dao.isComptabilise(docDivers.getId(), Constantes.SCR_DIVERS)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return;
                }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    if (!w.unComptabiliserDivers(dd, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return;
                    }
                }
            }
            for (YvsWorkflowValidDocCaisse vm : docDivers.getEtapesValidations()) {
                vm.setEtapeActive(false);
                if (i == 0) {
                    vm.setEtapeActive(true);
                }
                vm.setAuthor(currentUser);
                vm.setEtapeValid(false);
                dao.update(vm);
                i++;
            }
            if (dd != null) {
                dd.setDateValider(null);
                dd.setValiderBy(null);
                dd.setAnnulerBy(suspend ? currentUser.getUsers() : null);
                dd.setDateAnnuler(suspend ? new Date() : null);
                dd.setStatutDoc(suspend ? Constantes.ETAT_ANNULE : Constantes.ETAT_EDITABLE);
                dd.setDateUpdate(new Date());
                dd.setEtapeValide(0);
                docDivers.setEtapeValide(0);
                docDivers.setStatutDoc(dd.getStatutDoc());
                dao.update(dd);
                if (documents.contains(dd)) {
                    documents.set(documents.indexOf(dd), dd);
                }
            }
        }
    }

    public boolean validerOrder(boolean succes) {
        return validerOrder(docDivers, selectDoc, succes);
    }

    public void validerOrderAll() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                for (Long ids : l) {
                    validerOrderOne(documents.get(ids.intValue()), false);
                }
                succes();
                update("data_others");

            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void validerOrderOne(YvsComptaCaisseDocDivers entity, boolean succes) {
        if (!entity.getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
            DocCaissesDivers bean = UtilCompta.buildBeanDocCaisse(entity);
            bean.setTaxes(new ArrayList<>(entity.getTaxes()));
            bean.setTotalPlanifie(giveSoePlanifie(bean.getReglements()));
            entity.setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidDocCaisse.findByFacture", new String[]{"facture"}, new Object[]{entity}));
            bean.setEtapesValidations(ordonneEtapes(entity.getEtapesValidations()));
            if (bean.getEtapesValidations() != null ? bean.getEtapesValidations().isEmpty() : true) {
                validerOrder(bean, entity, false);
            } else {
                for (YvsWorkflowValidDocCaisse r : bean.getEtapesValidations()) {
                    if (!r.getEtapeValid()) {
                        etape = r;
                        break;
                    }
                }
                if (etape != null ? etape.getId() > 0 : false) {
                    validEtapeOrdre(bean, entity, etape, false, false, dao);
                } else {
                    validerOrder(bean, entity, false);
                }
            }
            if (succes) {
                succes();
            }
        }
    }

    public boolean validerOrder(DocCaissesDivers docDivers, YvsComptaCaisseDocDivers selectDoc, boolean succes) {
        if (controleFiche(docDivers, selectDoc, true)) {
            boolean canEditable = true;
            if (canEditable ? selectDoc != null : false) {
                if (currentParam == null) {
                    currentParam = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                }
                if (docDivers.getEtapesValidations() != null ? docDivers.getEtapesValidations().isEmpty() : true) {
                    if (docDivers.getMouvement().equals(Constantes.COMPTA_DEPENSE)) {
                        if (!autoriser("compta_od_valide_depense")) {
                            openNotAcces();
                            return false;
                        }
                        if (currentParam != null ? currentParam.getMontantSeuilDepenseOd() > 0 ? selectDoc.getMontant() > currentParam.getMontantSeuilDepenseOd() : false : false) {
                            if (!autoriser("compta_od_valid_max_seuil_montant")) {
                                openNotAcces();
                                return false;
                            }
                        }
                    } else {
                        if (!autoriser("compta_od_valide_recette")) {
                            openNotAcces();
                            return false;
                        }
                        if (currentParam != null ? currentParam.getMontantSeuilRecetteOd() > 0 ? selectDoc.getMontant() > currentParam.getMontantSeuilRecetteOd() : false : false) {
                            if (!autoriser("compta_od_valid_max_seuil_montant")) {
                                openNotAcces();
                                return false;
                            }
                        }
                    }
                }
                if (selectDoc.getIdTiers() > 0 ? selectDoc.getTiers() != null ? !selectDoc.getTiers().isEmpty() : false : false) {
                    boolean contains = false;
                    for (YvsComptaCaisseDocDiversTiers y : selectDoc.getTiers()) {
                        if (y.getTableTiers().equals(selectDoc.getTableTiers()) && y.getIdTiers().equals(selectDoc.getIdTiers())) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        getErrorMessage("Le tiers du document doit etre présent dans la liste des tiers associé au document");
                        return false;
                    }
                }
                selectDoc.setDateValider(new Date());
                selectDoc.setValiderBy(currentUser.getUsers());
                selectDoc.setStatutDoc(Constantes.ETAT_VALIDE);
                selectDoc.setDateUpdate(new Date());
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    selectDoc.setAuthor(currentUser);
                }
                dao.update(selectDoc);
                if (documents.contains(selectDoc)) {
                    documents.set(documents.indexOf(selectDoc), selectDoc);
                }
                selectDoc.setTotalPlanifie(giveSoePlanifie(selectDoc.getReglements()));
                if (docDivers != null) {
                    docDivers.setTotalPlanifie(selectDoc.getTotalPlanifie());
                    docDivers.setStatutDoc(Constantes.ETAT_VALIDE);
                }
                if (currentParam.getMajComptaAutoDivers() ? currentParam.getMajComptaStatutDivers().equals(Constantes.STATUT_DOC_VALIDE) : false) {
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        w.comptabiliserDivers(selectDoc, true, false);
                    }
                }
//                equilibreOne(selectDoc);
                Collections.sort(selectDoc.getReglements(), new YvsComptaCaissePieceDivers());
                //Si la pièce est le justificatif d'un bon provisoire
                for (YvsComptaCaissePieceDivers p : selectDoc.getReglements()) {
                    if (!p.getStatutPiece().equals(Constantes.STATUT_DOC_ANNULE)) {
                        if (p.isJustificatif() ? p.getJustify().getBon().getStatutPaiement().equals(Constantes.STATUT_DOC_PAYER) : false) {
                            if (!p.getJustify().getBon().getStatutJustify().equals(Constantes.STATUT_DOC_JUSTIFIER)) {
                                validePc(p, selectDoc, false);
                            }
                        }
                    }
                }
                if (selectDoc.getMouvement().equals(Constantes.COMPTA_RECETTE) && selectDoc.getReglements().isEmpty()) {
                    Caisses caisse = docDivers.getCaisseDefaut().getId() > 0 ? docDivers.getCaisseDefaut() : docDivers.getCaisse();
                    if (caisse != null ? caisse.getId() > 0 : false) {
                        YvsBaseModeReglement espece = modeEspece();
                        if (espece != null ? espece.getId() > 0 : false) {
                            Date date = docDivers.getDatePaiementPrevu() != null ? docDivers.getDatePaiementPrevu() : docDivers.getDateDoc();
                            YvsComptaCaissePieceDivers piece = getPiece(null, caisse, docDivers, selectDoc, espece, docDivers.getMontantTotal(), date);
                            if (Util.asString(docDivers.getBeneficiaire())) {
                                piece.setBeneficiaire(docDivers.getBeneficiaire());
                            }
                            piece.setNumeroExterne(docDivers.getNumeroExterne());
                            piece = (YvsComptaCaissePieceDivers) dao.save1(piece);
                            docDivers.getReglements().add(piece);
                            int idx = selectDoc.getReglements().indexOf(piece);
                            if (idx < 0) {
                                selectDoc.getReglements().add(piece);
                            }
                            update("data_mensualite_others");
                        }
                    }
                }
                update("zone_txt_resteDD");
                if (succes) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public boolean saveNewPieceCaisse() {
        try {
            if (controleFicheMensualite(pieceCD, true)) {
                if (pieceCD.isChangeMode()) {
                    String query = "DELETE FROM yvs_compta_phase_piece_divers WHERE piece_divers=?";
                    dao.requeteLibre(query, new Options[]{new Options(pieceCD.getId(), 1)});
//                    for (YvsComptaPhasePieceDivers p : pieceCD.getPhasesReglement()) {                        
//                        dao.delete(new YvsComptaPhasePieceDivers(p));
//                    }
                    pieceCD.getPhasesReglement().clear();
                }
                pieceCD.setAuthor(currentUser);
                if ((pieceCD.getCaisse() != null) ? pieceCD.getCaisse().getId() <= 0 : false) {
                    pieceCD.setCaisse(null);
                } else {
                    ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                    if (w != null) {
                        int index = w.getCaisses().indexOf(pieceCD.getCaisse());
                        if (index > -1) {
                            pieceCD.setCaisse(w.getCaisses().get(index));
                        }
                    }
                }
                if (pieceCD.getBeneficiaire() != null ? pieceCD.getBeneficiaire().trim().isEmpty() : true) {
                    pieceCD.setBeneficiaire(docDivers.getTiers().getSelectProfil().getNomPrenom());
                }
                if (pieceCD.getId() <= 0) {
                    pieceCD.setDateSave(new Date());
                    pieceCD.setDateUpdate(new Date());
                    pieceCD.setId(null);
                    YvsComptaCaissePieceDivers y = (YvsComptaCaissePieceDivers) dao.save1(pieceCD);
                    pieceCD.setId(y.getId());
                    if (pieceCD.isSelect()) {
                        YvsComptaCaissePieceCoutDivers p = new YvsComptaCaissePieceCoutDivers(pieceCD.getCout(), pieceCD);
                        p.setAuthor(currentUser);
                        p.setId(null);
                        p = (YvsComptaCaissePieceCoutDivers) dao.save1(p);
                        pieceCD.setPieceCout(p);

                        pieceCD.getCout().setPieceCout(p);
                        int idx = docDivers.getCouts().indexOf(pieceCD.getCout());
                        if (idx > -1) {
                            docDivers.getCouts().set(idx, pieceCD.getCout());
                        }
                    }
                } else {
                    pieceCD.setDateUpdate(new Date());
                    dao.update(pieceCD);
                }
                if (pieceCD.getBonProvisoire() != null ? pieceCD.getBonProvisoire().getId() > 0 : false) {
                    insertOrDeleteJustif(false);
                } else {
                    insertOrDeleteJustif(true);
                }
                int idx = docDivers.getReglements().indexOf(pieceCD);
                if (idx > -1) {
                    docDivers.getReglements().set(idx, pieceCD);
                } else {
                    docDivers.getReglements().add(0, pieceCD);
                }
                idx = selectDoc.getReglements().indexOf(pieceCD);
                if (idx > -1) {
                    selectDoc.getReglements().set(idx, pieceCD);
                } else {
                    selectDoc.getReglements().add(0, pieceCD);
                }
                idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    documents.set(idx, selectDoc);
                }
                docDivers.setTotalPlanifie(giveSoePlanifie(docDivers.getReglements()));
                succes();
                update("data_mensualite_others");
                update("zone_txt_resteDD");
                update("data_others");
            }
            pieceCD = newPc();
            return true;
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void saveAllPieceCaisse() {
        saveAllPieceCaisse(true);
    }

    public void saveAllPieceCaisse(boolean msg) {
        List<YvsComptaCaissePieceDivers> list = new ArrayList<>();
        list.addAll(docDivers.getReglements());
        saveAllPieceCaisse(list, msg);
    }

    public void saveAllPieceCaisse(boolean msg, boolean loadView) {
        List<YvsComptaCaissePieceDivers> list = new ArrayList<>();
        list.addAll(docDivers.getReglements());
        List<Long> re = saveAllPieceCaisse(list, msg);
        // cas de création de la pièce sur la page des pièce de caisse
        if (loadView) {
            //Récupération du mouvement caisse
            ManagedPieceCaisse service = (ManagedPieceCaisse) giveManagedBean(ManagedPieceCaisse.class);
            if (service != null && (re != null) ? !re.isEmpty() : false) {
                List<YvsComptaMouvementCaisse> Lentity = dao.loadNameQueries("YvsComptaMouvementCaisse.findByIdsExterne", new String[]{"idExterne", "table"}, new Object[]{re, Constantes.SCR_DIVERS});
                if (Lentity != null) {
                    service.onSelectObject(Lentity.get(0));
                    service.getMouvements().addAll(0, Lentity);
                    update("table_all_pieceCaisse");
                    update("form_edit_pieceCaiss");
                }
            }
        }
    }

    private List<Long> saveAllPieceCaisse(List<YvsComptaCaissePieceDivers> list, boolean msg) {
        List<Long> ids = new ArrayList<>();
        if (list != null ? !list.isEmpty() : false) {
            if (docDivers.getId() <= 0) {
                saveNew(msg);
            }
            for (YvsComptaCaissePieceDivers pc : list) {
                if (docDivers.getId() > 0) {
                    pc.setDocDivers(selectDoc);
                }
                if (!controleFicheMensualite(pc, msg)) {
                    return null;
                }
            }
            String numero;
            for (YvsComptaCaissePieceDivers p : list) {
                p.setAuthor(currentUser);
                p.setDocDivers(new YvsComptaCaisseDocDivers(docDivers.getId()));
                if ((p.getCaisse() != null ? p.getCaisse().getId() < 1 : true) && (docDivers.getCaisseDefaut() != null ? docDivers.getCaisseDefaut().getId() > 0 : false)) {
                    p.setCaisse(new YvsBaseCaisse(docDivers.getCaisseDefaut().getId(), docDivers.getCaisseDefaut().getIntitule()));
                }
                if (p.getId() <= 0) {
                    p.setId(null);
                    numero = genererReference(Constantes.TYPE_PC_DIVERS_NAME, p.getDatePiece());
                    if (numero != null ? numero.trim().length() < 1 : true) {
                        return null;
                    }
                    p.setNumPiece(numero);
                    YvsComptaCaissePieceDivers y = (YvsComptaCaissePieceDivers) dao.save1(p);
                    p.setId(y.getId());
                } else {
                    dao.update(p);
                }
                ids.add(p.getId());
                int idx = docDivers.getReglements().indexOf(p);
                if (idx > -1) {
                    docDivers.getReglements().set(idx, p);
                }
                idx = selectDoc.getReglements().indexOf(p);
                if (idx > -1) {
                    selectDoc.getReglements().set(idx, p);
                }
            }
            if (msg) {
                succes();
            }
        }
        return ids;
    }

    public void saveAbonnementPieceCaisse() {
        if (docDivers != null ? docDivers.getId() > 0 : false) {
            for (YvsComptaAbonementDocDivers pc : docDivers.getAbonnements()) {
                pc.setAuthor(currentUser);
                pc.setDocDivers(new YvsComptaCaisseDocDivers(docDivers.getId()));
                if (pc.getId() != null ? pc.getId() <= 0 : true) {
                    pc.setId(null);
                    pc = (YvsComptaAbonementDocDivers) dao.save1(pc);
                } else {
                    dao.update(pc);
                }
                int idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    int idr = documents.get(idx).getAbonnements().indexOf(pc);
                    if (idr > -1) {
                        documents.get(idx).getAbonnements().set(idr, pc);
                    } else {
                        documents.get(idx).getAbonnements().add(pc);
                    }
                }
            }
            succes();
            update("data_others");
        }
    }

    public void saveAbonnement() {
        try {
            if (abonnement.getValeur() <= 0) {
                getErrorMessage("Vous devez entrer un montant");
                return;
            }
            YvsComptaAbonementDocDivers y = UtilCompta.buildAbonnement(abonnement, currentUser);
            if (y != null) {
                if (abonnement.getId() < 1) {
                    y = (YvsComptaAbonementDocDivers) dao.save1(y);
                    abonnement.setId(y.getId());
                } else {
                    dao.update(y);
                }
                int idx = docDivers.getAbonnements().indexOf(y);
                if (idx > -1) {
                    docDivers.getAbonnements().set(idx, y);
                } else {
                    docDivers.getAbonnements().add(y);
                }
                idx = selectDoc.getAbonnements().indexOf(y);
                if (idx > -1) {
                    selectDoc.getAbonnements().set(idx, y);
                } else {
                    selectDoc.getAbonnements().add(y);
                }
                succes();
                closeDialog("dlgUpdateAbonnement");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible!!");
            getException("saveAbonnement", ex);
        }
    }

    private YvsComptaCaissePieceDivers newPc() {
        YvsComptaCaissePieceDivers pc = new YvsComptaCaissePieceDivers();
        pc.setDatePiece(new Date());
        pc.setId((long) 0);
        pc.setMontant(0.0);
        pc.setDocDivers(selectDoc);
        pc.setModePaiement(new YvsBaseModeReglement(0l));
        pc.setBeneficiaire("");
        pc.setCaisse(new YvsBaseCaisse());
        return pc;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComptaCaisseDocDivers> list = new ArrayList<>();
                YvsComptaCaisseDocDivers bean;
                for (Long ids : l) {
                    bean = documents.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    if (!_controleFiche_(bean)) {
                        return;
                    }
                    dao.delete(bean);
                    if (deleteRetenue ? (bean.getTiers() != null ? !bean.getTiers().isEmpty() : false) : false) {
                        deleteRetenueByDivers(bean);
                    }
                }
                documents.removeAll(list);
                succes();
                resetFiche();
                update("data_others");

            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComptaCaisseDocDivers y) {
        selectDoc = y;
    }

    public void deleteBean_() {
        try {
            if (selectDoc != null) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                selectDoc.setAuthor(currentUser);
                selectDoc.setDateUpdate(new Date());
                selectDoc.setParent(null);
                dao.delete(selectDoc);
                documents.remove(selectDoc);
                if (deleteRetenue ? (selectDoc.getTiers() != null ? !selectDoc.getTiers().isEmpty() : false) : false) {
                    deleteRetenueByDivers(selectDoc);
                }
                succes();
                resetFiche();
                update("data_others");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanMensualite() {
        try {
            if (!_controleFiche_(selectDoc)) {
                return;
            }
            if ((tabIds_mensualite != null) ? !tabIds_mensualite.equals("") : false) {
                String[] tab = tabIds_mensualite.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                }
                succes();
                resetFichePiece();
                update("data_mensualite_others");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanCout(YvsComptaCoutSupDocDivers y) {
        if (y != null) {
            if (y.getPieceCout() != null ? y.getPieceCout().getId() > 0 : false) {
                if (y.getPieceCout().getPiece().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    getErrorMessage("Ce cout est déjà payé");
                    return;
                }
            }
            dao.delete(y);
            docDivers.getCouts().remove(y);
            if (cout.getId() == y.getId()) {
                cout = new CoutSupDocDivers();
                update("form_cout_others");
            }
            if (y.getPieceCout() != null ? y.getPieceCout().getId() > 0 : false) {
                deleteBeanMensualite_(y.getPieceCout().getPiece());
            }
            update("txt_couts_others");
            update("date_cout_others");
            succes();
        }
    }

    public void deleteBeanTaxe(YvsComptaTaxeDocDivers y) {
        if (y != null) {
            dao.delete(y);
            docDivers.getTaxes().remove(y);
            if (taxe.getId() == y.getId()) {
                taxe = new TaxeDocDivers();
                update("form_taxe_others");
            }
            update("txt_taxes_others");
            update("date_taxe_others");
            succes();
        }
    }

    public void deleteBeanSection(YvsComptaCentreDocDivers y) {
        if (y != null) {
            dao.delete(y);
            docDivers.getSections().remove(y);
            if (section.getId() == y.getId()) {
                section = new CentreDocDivers();
                update("form_section_others");
            }
            update("date_section_others");
            succes();
        }
    }

    public void deleteBeanSections() {
        if (docDivers.getSections() != null ? !docDivers.getSections().isEmpty() : false) {
            if (!dao.isComptabilise(docDivers.getId(), Constantes.SCR_DIVERS)) {
                int size = docDivers.getSections().size();
                for (int i = 0; i < size; i++) {
                    dao.delete(docDivers.getSections().get(i));
                }
                docDivers.getSections().clear();
                section = new CentreDocDivers();
                update("form_section_others");
                update("date_section_others");
                succes();
            } else {
                getErrorMessage("Vous ne pouvez pas modifier cette pièce");
            }
        }
    }

    public void deleteBeanExtourne() {
        try {
            if (selectExtourne != null ? selectExtourne.getId() > 0 : false) {
                if (selectExtourne.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    getErrorMessage("Le reglement est déjà payé");
                    return;
                }
                dao.delete(selectExtourne);
                pieceCD.getSousDivers().remove(selectExtourne);
                succes();
                update("data_extourne_caisse_divers");
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    public void choixModePaiement(ValueChangeEvent ev) {
        ManagedModeReglement service = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
        if (service != null) {
            Long oldValue = (Long) ev.getOldValue();
            Long newValue = (Long) ev.getNewValue();
            int idx = service.getModes().indexOf(new YvsBaseModeReglement(newValue));
            if (idx > -1) {
                YvsBaseModeReglement mode = service.getModes().get(idx);
                if (pieceCD != null ? pieceCD.getId() > 0 : false) {
                    if (pieceCD.getModePaiement().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE) && !mode.getId().equals(pieceCD.getModePaiement().getId())) {
                        boolean change = true;
                        for (YvsComptaPhasePieceDivers p : pieceCD.getPhasesReglement()) {
                            if (p.getPhaseOk()) {
                                change = false;
                                break;
                            }
                        }
                        if (!change) {
                            getErrorMessage("Vous ne pouvez pas modifier le mode de paiement... car les phases de ce cheques sotn en cours de traitement");
                            pieceCD.getModePaiement().setId((long) oldValue);
                            return;
                        }
                        pieceCD.setChangeMode(true);
                    }
                }
                pieceCD.setModePaiement(mode);
            }
        }
    }

    public void chooseCaisse() {
        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (service != null) {
            long id = docDivers.getCaisse().getId();
            if (id == -1) {
                service.loadAll(true, 0);
                update("chmp_caisse_piece_divers");
                update("chmp_caissier_bon_divers");
                update("menu_caisse_piece_divers");
                update("chmp_caisse_defaut_piece_divers");
            } else {
                int idx = service.getCaisses().indexOf(new YvsBaseCaisse(id));
                if (idx >= 0) {
                    YvsBaseCaisse y = service.getCaisses().get(idx);
                    docDivers.setCaisse(new Caisses(y.getId(), y.getIntitule()));
                    cloneObject(docDivers.getCaisseDefaut(), docDivers.getCaisse());
                }
            }
        }
    }

    public void chooseCaisseDefaut() {
        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (service != null) {
            long id = docDivers.getCaisseDefaut().getId();
            if (id == -1) {
                service.loadAll(true, 0);
                update("chmp_caisse_piece_divers");
                update("chmp_caissier_bon_divers");
                update("menu_caisse_piece_divers");
                update("chmp_caisse_defaut_piece_divers");
            } else {
                int idx = service.getCaisses().indexOf(new YvsBaseCaisse(id));
                if (idx >= 0) {
                    YvsBaseCaisse y = service.getCaisses().get(idx);
                    docDivers.setCaisseDefaut(new Caisses(y.getId(), y.getIntitule()));
                }
            }
        }
    }

    public void chooseAgence() {
        if (docDivers.getAgence() != null ? docDivers.getAgence().getId() > 0 : false) {
            ManagedAgence service = (ManagedAgence) giveManagedBean(ManagedAgence.class);
            if (service != null) {
                int idx = service.getAgences().indexOf(new YvsAgences(docDivers.getAgence().getId()));
                if (idx >= 0) {
                    YvsAgences y = service.getAgences().get(idx);
                    docDivers.setAgence(new Agence(y.getId(), y.getCodeagence(), y.getDesignation()));
                }
            }
        }
    }

    public void openViewPcToAddPc() {
        pieceCD = newPc();
        pieceCD.setId((long) -100);
        pieceCD.setDocDivers(selectDoc);
        double total = docDivers.getMontantWithCout() - docDivers.getTotalPlanifie();
        pieceCD.setMontant(total);
        pieceCD.setDatePiece(docDivers.getDatePaiementPrevu());
        pieceCD.setDatePaimentPrevu(docDivers.getDatePaiementPrevu());
        pieceCD.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        pieceCD.setBeneficiaire(docDivers.getTiers().getSelectProfil().getNomPrenom());
        update("form_edit_pc_dd");
    }

    public void viewListBons() {
        ManagedBonProvisoire w = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
        if (w != null) {
            if (w.getBonProvisoire().getMontant() < 1) {
                w.getBonProvisoire().setMontant(pieceCD.getMontant());
                w.getBonProvisoire().setAssocied(true);
            }
            if (w.getBonProvisoire().getBeneficiaire() == null || w.getBonProvisoire().getBeneficiaire().trim().isEmpty()) {
                if (docDivers.getTiers() != null ? docDivers.getTiers().getId() > 0 : false) {
                    w.getBonProvisoire().setTiers(docDivers.getTiers());
                    w.getBonProvisoire().setBeneficiaire(docDivers.getTiers().getNom_prenom());
                }
            }
            if (w.getBonProvisoire().getDescription() == null || w.getBonProvisoire().getDescription().trim().isEmpty()) {
                w.getBonProvisoire().setDescription(docDivers.getDescription());
            }
            update("blog_form_bon_divers");
        }
    }

    public void saveNewBonProvisoire() {
        ManagedBonProvisoire w = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
        if (w != null) {
            w.saveNew();
            if (w.getSelectBon() != null ? w.getSelectBon().getId() > 0 : false) {
                if (w.getBonProvisoire().getEtapeTotal() < 1) {
                    w.validerOrder();
                    update("table_bon_p");
                    update("etapes_valide_bon_divers");
                } else if (w.getBonProvisoire().getEtapeTotal() == 1) {
                    if (w.getBonProvisoire().getEtapesValidations() != null ? !w.getBonProvisoire().getEtapesValidations().isEmpty() : false) {
                        YvsWorkflowValidBonProvisoire etape = w.getBonProvisoire().getEtapesValidations().get(0);
                        boolean acces = asDroitValideEtape(etape.getEtape());
                        if (acces) {
                            w.validEtapeOrdre(etape, false);
                            update("table_bon_p");
                            update("etapes_valide_bon_divers");
                        }
                    }
                }
                if (w.getBonProvisoire().isAssocied()) {
                    chooseBonProvisoire(w.getSelectBon());
                    closeDialog("dlgListBons");
                }
            }
        }
    }

    public void generePieceByCout(YvsComptaCoutSupDocDivers y) {
        if (y != null ? y.getId() > 0 : false) {
            pieceCD = newPc();
            pieceCD.setId((long) -100);
            pieceCD.setSelect(true);
            pieceCD.setDocDivers(selectDoc);
            pieceCD.setMontant(y.getMontant());
            pieceCD.setDatePiece(docDivers.getDateDoc());
            pieceCD.setDatePaimentPrevu(docDivers.getDatePaiementPrevu());
            pieceCD.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            pieceCD.setBeneficiaire(docDivers.getTiers().getSelectProfil().getNomPrenom());
            pieceCD.setCout(y);
            update("form_edit_pc_dd");
        }
    }

    public void deleteBeanPieceR(YvsComptaCaissePieceDivers y) {
        if (y != null ? y.getId() != null ? y.getId() <= 0 : true : true) {
            docDivers.getReglements().remove(y);
        } else {
            pieceCD = y;
            openDialog("dlgConfirmDeleteMensualite");
        }
    }

    public void deleteBeanPieceA(YvsComptaAbonementDocDivers y) {
        abonnCD = y;
    }

    public void onTiersSelects(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            String d = (String) ev.getObject();
            pieceCD.setBeneficiaire(d);
        }
    }

    public void openDlgConfirmSuspens(YvsComptaCaissePieceDivers y) {
        pieceCD = y;
        openDialog("dlgConfirmSuspensPC");
    }

    public void openViewPcToUpdate(YvsComptaCaissePieceDivers y) {
        openViewPcToUpdate(y, false);
    }

    public void openViewPcToUpdate(YvsComptaCaissePieceDivers y, boolean withOD) {
        if (docDivers.getStatutRegle() != Constantes.STATUT_DOC_PAYER && y.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
            pieceCD = new YvsComptaCaissePieceDivers(y.getId(), y);
            pieceCD.setDocDivers(selectDoc);
            pieceCD.setBeneficiaire(y.getBeneficiaire());
            pieceCD.setReferenceExterne(y.getReferenceExterne());
            if (pieceCD.getModePaiement() != null ? pieceCD.getModePaiement().getId() < 1 : true) {
                pieceCD.setModePaiement(modeEspece());
            }
            if (pieceCD.getCaisse() == null) {
                pieceCD.setCaisse(new YvsBaseCaisse());
            }
            if (pieceCD.getBeneficiaire() != null ? pieceCD.getBeneficiaire().trim().isEmpty() : true) {
                pieceCD.setBeneficiaire(docDivers.getTiers().getSelectProfil().getNomPrenom());
            }
            if (y.getPieceCout() != null ? y.getPieceCout().getId() > 0 : false) {
                pieceCD.setSelect(true);
                pieceCD.setCout(y.getPieceCout().getCout());
            }
            if (pieceCD.getId() < 1) {
                pieceCD.setMontant(docDivers.getMontantWithCout() - docDivers.getTotalPlanifie());
            } else {
                if (pieceCD.isJustificatif()) {
                    pieceCD.setBonProvisoire(pieceCD.getJustify().getBon());
                    pieceCD.setNumeroExterne(pieceCD.getJustify().getBon().getNumero());
                }
            }
            update("form_edit_pc_dd");
            if (!withOD) {
                openDialog("dlgUpdatePC");
            } else {
                onSelectObject(y.getDocDivers());
                openDialog("dlgCreateOD");
                update("table_all_pieceCaisse");
                update("form_edit_pieceCaiss");
            }
        } else {
            getErrorMessage("Vous ne pouvez pas modifier cette pièce");
        }
    }

    public void suspendOnePieceCaisse() {
        suspendOnePieceCaisse(pieceCD, true);
    }

    public void suspendOnePieceCaisse(YvsComptaCaissePieceDivers pc, boolean msg) {
        suspendOnePieceCaisse(selectDoc, pc, msg);
    }

    public void suspendOnePieceCaisse(YvsComptaCaisseDocDivers selectDoc, YvsComptaCaissePieceDivers pc, boolean msg) {
        if (pc != null ? pc.getId() > 0 : false) {
            pc.setCaisse((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{pc.getCaisse().getId()}));
            if (controleAccesCaisse(pc.getCaisse(), true)) {
                if (!verifyCancelPieceCaisse(pc.getDateValider())) {
                    return;
                }
                if (dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_DIVERS)) {
                    if (!autoriser("compta_od_annul_comptabilite")) {
                        getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                        return;
                    }
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        if (!w.unComptabiliserCaisseDivers(pc, false)) {
                            getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                            return;
                        }
                    }
                }
                pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                pc.setDateAnnuler(new Date());
                pc.setAnnulerBy(currentUser.getUsers());
                pc.setValiderBy(null);
                pc.setDateValider(null);
                pc.setDateUpdate(new Date());
                pc.setAuthor(currentUser);
                dao.update(pc);
                if (docDivers != null) {
                    docDivers.setTotalPlanifie(giveSoePlanifie(docDivers.getReglements()));
                    if (selectDoc != null) {
                        selectDoc.setTotalPlanifie(docDivers.getTotalPlanifie());
                        selectDoc.setStatutRegle((docDivers.getMontant() != docDivers.getResteAPlanifier()) ? Constantes.STATUT_DOC_ENCOUR : Constantes.STATUT_DOC_ATTENTE);
                        dao.update(selectDoc);
                    }
                } else if (selectDoc != null) {
                    selectDoc.setTotalPlanifie(giveSoePlanifie(selectDoc.getReglements()));
                    selectDoc.setStatutRegle((selectDoc.getMontant() != selectDoc.getResteAPlanifier()) ? Constantes.STATUT_DOC_ENCOUR : Constantes.STATUT_DOC_ATTENTE);
                    dao.update(selectDoc);
                }
                update("zone_txt_resteDD");
                if (selectDoc != null) {
                    int idx = documents.indexOf(selectDoc);
                    if (idx >= 0) {
                        documents.set(idx, selectDoc);
                        update("data_others");
                    }
                }
                if (msg) {
                    succes();
                }
            }
        } else {
            getErrorMessage("Aucune pièce n'a été selectionné !");
        }
    }

    public void chooseCaissePiece() {
        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (w != null) {
            if (pieceCD.getCaisse() != null ? pieceCD.getCaisse().getId() > 0 : false) {
                long id = pieceCD.getCaisse().getId();
                if (id == -1) {
                    w.loadAll(true, 0);
                    update("chmp_caisse_piece_divers");
                    update("chmp_caissier_bon_divers");
                    update("menu_caisse_piece_divers");
                    update("chmp_caisse_defaut_piece_divers");
                } else {
                    int idx = w.getCaisses().indexOf(new YvsBaseCaisse(id));
                    if (idx >= 0) {
                        YvsBaseCaisse y = w.getCaisses().get(idx);
                        pieceCD.setCaisse(new YvsBaseCaisse(y));
                    }
                }
            }
        }
    }

    public void valideOnePieceCaisse() {
        pieceCD.setDateValider(pieceCD.getDatePaimentPrevu());
        validePc(pieceCD, selectDoc, true);
    }

    public void validePc(YvsComptaCaissePieceDivers y) {
        validePc(y, selectDoc, true);
    }

    public void validePiece() {
        pieceCD.setDateValider(pieceCD.getDatePaimentPrevu());
        validePc(pieceCD, pieceCD.getDocDivers(), true);
    }

    public boolean validePc(YvsComptaCaissePieceDivers y, YvsComptaCaisseDocDivers selectDoc, boolean msg) {
        pieceCD = y;
        if (controleValeurMensualite(y, selectDoc, msg)) {
            y.setCaisse((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{y.getCaisse().getId()}));
            //le document divers doit être valide
            int idx = documents.indexOf(selectDoc);
            if (idx > -1) {
                selectDoc.setStatutDoc(documents.get(idx).getStatutDoc());
            }
            selectDoc.setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidDocCaisse.findByFacture", new String[]{"facture"}, new Object[]{selectDoc}));
            if (selectDoc.canPaye()) {
                if (y.getCaisse() != null ? y.getCaisse().getId() < 1 : true) {
                    if (msg) {
                        getErrorMessage("Vous devez precisez la caisse");
                    }
                    return false;
                }
                if (!controleAccesCaisse(y.getCaisse(), msg)) {
                    return false;
                }
                if (pieceCD.getJustify() != null ? (pieceCD.getJustify().getId() > 0 ? pieceCD.getJustify().getBon() != null : false) : false) {
                    if (!pieceCD.getJustify().getBon().getStatutPaiement().equals(Constantes.ETAT_REGLE)) {
                        if (msg) {
                            getErrorMessage("Le bon provisoire rattaché n'est pas encore payé");
                        }
                        return false;
                    }
                }
                pieceCD.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                pieceCD.setValiderBy(currentUser.getUsers());
                pieceCD.setDateUpdate(new Date());
                pieceCD.setDateAnnuler(null);
                pieceCD.setAnnulerBy(null);
                if (pieceCD.getModePaiement() == null) {
                    pieceCD.setModePaiement(modeEspece());
                }
                pieceCD.setDateUpdate(new Date());
                pieceCD.setAuthor(currentUser);
                dao.update(pieceCD);
                selectDoc.setTotalPlanifie(giveSoePlanifie(selectDoc.getReglements()));
                ManagedSaisiePiece service = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (dao.isComptabilise(y.getDocDivers().getId(), Constantes.SCR_DIVERS)) {
                    if (service != null) {
                        service.comptabiliserCaisseDivers(pieceCD, false, false);
                        update("head_corp_form_suivi_prd");
                    }
                } else {
                    if (y.getDocDivers().getIdTiers() == null ? true : y.getDocDivers().getIdTiers() <= 0) {
                        //comptabilise
                        service.comptabiliserDivers(selectDoc, false, false);
                    }
                }
                update("head_form_suivi_prd");
                if (pieceCD.getJustify() != null ? pieceCD.getJustify().getId() > 0 : false) {
                    ManagedBonProvisoire w = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
                    if (w != null) {
                        w.verifyToJustify(pieceCD.getJustify().getBon());
                    }
                }
                update("zone_txt_resteDD");
                if (msg) {
                    succes();
                }
                equilibreOne(y.getDocDivers(), false);
                return true;
            } else {
                if (msg) {
                    getErrorMessage("Le document n'a pas encore été validé ou n'atteint pas l'etape spécifique ");
                }
            }
        }
        return false;
    }

    public void ActifOnePieceCaisse() {
        ActivePc(abonnCD);
    }

    public void ActivePc(YvsComptaAbonementDocDivers y) {
        abonnCD = y;
        //le document divers doit être valide
        if (docDivers.getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
            abonnCD.setActif(!abonnCD.getActif());
            succes();
        } else {
            getErrorMessage("Le document n'a pas encore été validé ");
        }
    }

    public void deleteBeanMensualite_() {
        deleteBeanMensualite_(pieceCD);
    }

    public void deleteBeanMensualite_(YvsComptaCaissePieceDivers pieceCD) {
        try {
            if (pieceCD != null ? !pieceCD.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) : false) {
                pieceCD.setAuthor(currentUser);
                dao.delete(pieceCD);
                docDivers.getReglements().remove(pieceCD);
                docDivers.setTotalPlanifie(docDivers.getTotalPlanifie() - pieceCD.getMontant());
                succes();
                update("data_mensualite_others");
                update("data_others");
            } else {
                getErrorMessage("Vous ne pouvez pas modifier cette pièce");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBeanAbonnement_() {
        try {
            if (docDivers.canDelete()) {
                if (abonnCD != null) {
                    abonnCD.setAuthor(currentUser);
                    dao.delete(abonnCD);
                    docDivers.getAbonnements().remove(abonnCD);
                    succes();
                    update("data_abonnement_others");
                    update("data_others");
                }
            } else {
                getErrorMessage("Vous ne pouvez pas modifier cette pièce");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBeanAbonnements() {
        try {
            if (docDivers.getAbonnements() != null ? !docDivers.getAbonnements().isEmpty() : false) {
                if (!dao.isComptabilise(docDivers.getId(), Constantes.SCR_DIVERS)) {
                    int size = docDivers.getAbonnements().size();
                    for (int i = 0; i < size; i++) {
                        dao.delete(docDivers.getAbonnements().get(i));
                        System.out.println("i " + i);
                    }
                    docDivers.getAbonnements().clear();
                    succes();
                    update("data_abonnement_others");
                    update("data_others");
                } else {
                    getErrorMessage("Vous ne pouvez pas modifier cette pièce");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onSelectDistant(YvsComptaCaisseDocDivers da) {
        if (da != null ? da.getId() > 0 : false) {
            onSelectObject(da);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Opérations Divers", "modCompta", "smenOperationDivers", true);
            }
        }
    }

    @Override
    public void onSelectObject(YvsComptaCaisseDocDivers y) {
        selectDoc = y;
        setOffset(paginator.getListeIds().indexOf(y.getId()));
        populateView(UtilCompta.buildBeanDocCaisse(selectDoc));
        if (y.getTypeDoc() != null) {
            //être sure de charger le type sur la vue
            ManagedTypeDocDivers w = (ManagedTypeDocDivers) giveManagedBean(ManagedTypeDocDivers.class);
            if (w != null ? !w.getTypesDocDivers().contains(y.getTypeDoc()) : false) {
                w.getTypesDocDivers().add(y.getTypeDoc());
            }
        }
        if (y.getAgence() != null) {
            //être sure de charger l'agence sur la vue
            ManagedAgence service = (ManagedAgence) giveManagedBean(ManagedAgence.class);
            if (service != null) {
                if (!service.getAgences().contains(y.getAgence())) {
                    service.getAgences().add(y.getAgence());
                }
            }
        }
        //charge les étapes de validations
        if (!y.getEtapesValidations().isEmpty()) {
            docDivers.setEtapesValidations(ordonneEtapes(dao.loadNameQueries("YvsWorkflowValidDocCaisse.findByFacture", new String[]{"facture"}, new Object[]{y})));
//            docDivers.setEtapesValidations(ordonneEtapes(y.getEtapesValidations()));
        }
        if (pieceCD.getBeneficiaire() != null ? pieceCD.getBeneficiaire().trim().isEmpty() : true) {
            pieceCD.setBeneficiaire(docDivers.getTiers().getSelectProfil().getNomPrenom());
        }
        //charge le tiers
        ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (w != null) {
            docDivers.setTiers(w.buildTiersByProfil(y.getIdTiers(), y.getTableTiers()));
        }
        //charge le plan d'abonnement s'il y'en a
        if (y.getAbonnements() != null ? !y.getAbonnements().isEmpty() : false) {
            YvsComptaPlanAbonnement plan = y.getAbonnements().get(0).getPlan();
            docDivers.setPlanAbonement(plan != null ? new PlanDecoupage(plan.getId()) : new PlanDecoupage());
        }
        update("blog_form_others");
        update("data_others");
        execute("_slideShow('zone_show_taxe_others')");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComptaCaisseDocDivers y = (YvsComptaCaisseDocDivers) ev.getObject();
            onSelectObject(y);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        update("form_others");
        update("grp_btn_etat");
        update("blog_form_mensualite_others");
    }

    public void loadOnViewMensualite(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComptaCaissePieceDivers y = (YvsComptaCaissePieceDivers) ev.getObject();
            pieceCD = y;
            pieceCD.setChangeMode(false);
            if (pieceCD.getCaisse() == null) {
                pieceCD.setCaisse(new YvsBaseCaisse());
            }
            if (y != null ? y.getId() > 0 : false) {
                openDialog("dlgUpdatePC");
                update("form_form_edit_pc_dd");
            }
        }
    }

    public void unLoadOnViewMensualite(UnselectEvent ev) {
        resetFichePiece();
        update("blog_form_mensualite_others");
    }

    public void loadOnViewCout(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCoutSupDocDivers y = (YvsComptaCoutSupDocDivers) ev.getObject();
            cout = UtilCompta.buildBeanCoutSupDocDivers(y);
        }
    }

    public void unLoadOnViewCout(UnselectEvent ev) {
        cout = new CoutSupDocDivers();
    }

    public void loadOnViewTaxe(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaTaxeDocDivers y = (YvsComptaTaxeDocDivers) ev.getObject();
            taxe = UtilCompta.buildBeanTaxeDocDivers(y);
        }
    }

    public void unLoadOnViewTaxe(UnselectEvent ev) {
        taxe = new TaxeDocDivers();
    }

    public void loadOnViewSection(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCentreDocDivers y = (YvsComptaCentreDocDivers) ev.getObject();
            section = UtilCompta.buildBeanCentreDocDivers(y);
            blurValeurSection();
        }
    }

    public void unLoadOnViewSection(UnselectEvent ev) {
        section = new CentreDocDivers();
    }

    public void chooseTypeCout_(ValueChangeEvent ev) {
        if (ev != null ? ev.getNewValue() != null : false) {
            long id = (long) ev.getNewValue();
            ManagedTypeCout w = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class);
            if (w != null) {
                if (id > 0) {
                    cout.getType().setId(id);
                    int idx = w.getTypes().indexOf(new YvsGrhTypeCout(cout.getType().getId()));
                    if (idx > -1) {
                        cout.setType(UtilGrh.buildBeanTypeCout(w.getTypes().get(idx)));
                    }
                } else {
                    if (id == -1) {
                        w.setTypeCout(new TypeCout(Constantes.COUT_OD));
                        openDialog("dlgAddTypeCout");
                    }
                    cout.getType().setId(0);
                }
            } else {
                cout.getType().setId(id);
            }
        }
    }

    public void chooseTypeCout(ValueChangeEvent ev) {
        if (ev != null ? ev.getNewValue() != null : false) {
            long id = (long) ev.getNewValue();
            ManagedTypeCout w = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class);
            if (w != null) {
                if (id > 0) {
                    cout.getType().setId(id);
                    int idx = w.getTypes().indexOf(new YvsGrhTypeCout(cout.getType().getId()));
                    if (idx > -1) {
                        cout.setType(UtilGrh.buildBeanTypeCout(w.getTypes().get(idx)));
                    }
                } else {
                    boolean paginer = true;
                    boolean next = false;
                    if (id == -1) {
                        w.setTypeCout(new TypeCout(Constantes.COUT_OD));
                        openDialog("dlgAddTypeCout");
                    } else if (id == -2) {
                        next = false;
                    } else if (id == -3) {
                        next = true;
                    } else {
                        paginer = false;
                    }
                    if (paginer) {
                        w.loadAllType(next, false);
                    }
                    cout.getType().setId(0);
                }
            } else {
                cout.getType().setId(id);
            }
        }
    }

    public void changePropertyTaxe(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            if (canEditDoc()) {
                if (!docDivers.getTaxes().isEmpty()) {
                    for (YvsComptaTaxeDocDivers t : docDivers.getTaxes()) {
                        if (!(Boolean) ev.getNewValue()) {
                            t.setMontant(docDivers.getMontant() * (t.getTaxe().getTaux() / 100));
                        } else {
                            docDivers.setMontantHt(docDivers.getMontant() / (1 + (t.getTaxe().getTaux() / 100)));
                            t.setMontant(docDivers.getMontant() - docDivers.getMontantHt());
                        }
                        dao.update(t);
                    }
                }
            } else {
                openNotAccesByCode();
            }
        }
    }

    public void saveNewTaxe() {
        String action = taxe.getId() > 0 ? "Modification" : "insertion";
        try {
            taxe.setDocDivers(docDivers);
            if (controleFiche(taxe)) {
                YvsComptaTaxeDocDivers y = UtilCompta.buildTaxeDocDivers(taxe, currentUser);
                if (taxe.getId() < 1) {
                    y.setId(null);
                    y = (YvsComptaTaxeDocDivers) dao.save1(y);
                } else {
                    dao.update(y);
                }
                int idx = docDivers.getTaxes().indexOf(y);
                if (idx > -1) {
                    docDivers.getTaxes().set(idx, y);
                } else {
                    docDivers.getTaxes().add(y);
                }
                idx = documents.indexOf(selectDoc);
                if (idx >= 0) {
                    if (!documents.get(idx).getTaxes().contains(y)) {
                        documents.get(idx).getTaxes().add(y);
                    }
                }
                taxe = new TaxeDocDivers();
                succes();
                update("txt_taxes_others");
                update("date_taxe_others");
                update("form_taxe_others");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error " + action, ex);
        }
    }

    public void saveNewSection() {
        String action = section.getId() > 0 ? "Modification" : "Insertion";
        try {
            section.setDocDivers(docDivers);
            if (controleFiche(section)) {
                YvsComptaCentreDocDivers y = UtilCompta.buildCentreDocDivers(section, currentUser);
                if (section.getId() < 1) {
                    y.setId(null);
                    y = (YvsComptaCentreDocDivers) dao.save1(y);
                } else {
                    dao.update(y);
                }
                int idx = docDivers.getSections().indexOf(y);
                if (idx > -1) {
                    docDivers.getSections().set(idx, y);
                } else {
                    docDivers.getSections().add(y);
                }
                idx = documents.indexOf(selectDoc);
                if (idx >= 0) {
                    if (!documents.get(idx).getSections().contains(y)) {
                        documents.get(idx).getSections().add(y);
                    }
                }
                section = new CentreDocDivers();
                section.setMontant(docDivers.getMontant() - docDivers.getTotalCentre());
                succes();
                update("date_section_others");
                update("form_section_others");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error " + action, ex);
        }
    }

    public void saveAllSection() {
        try {
            if (docDivers != null ? docDivers.getId() > 0 ? docDivers.getCompte() != null ? docDivers.getCompte().getId() > 0 : false : false : false) {
                if (!docDivers.getSections().isEmpty()) {
                    getErrorMessage("Au moins une affectation a déjà été réalisé !");
                    return;
                }
                List<YvsComptaAffectationGenAnal> list = dao.loadNameQueries("YvsComptaAffectationGenAnal.findByCompte", new String[]{"compte"}, new Object[]{new YvsBasePlanComptable(docDivers.getCompte().getId())});
                if (list != null ? !list.isEmpty() : false) {
                    YvsComptaCentreDocDivers y;
                    for (YvsComptaAffectationGenAnal a : list) {
                        y = new YvsComptaCentreDocDivers();
                        y.setAuthor(currentUser);
                        y.setCentre(a.getCentre());
                        y.setDocDivers(new YvsComptaCaisseDocDivers(docDivers.getId()));
                        y.setMontant(docDivers.getMontantTotal() * a.getCoefficient() / 100);
                        y = (YvsComptaCentreDocDivers) dao.save1(y);
                        int idx = docDivers.getSections().indexOf(y);
                        if (idx > -1) {
                            docDivers.getSections().set(idx, y);
                        } else {
                            docDivers.getSections().add(y);
                        }
                        idx = selectDoc.getSections().indexOf(y);
                        if (idx > -1) {
                            selectDoc.getSections().set(idx, y);
                        } else {
                            selectDoc.getSections().add(y);
                        }
                        idx = documents.indexOf(selectDoc);
                        if (idx > -1) {
                            documents.set(idx, selectDoc);
                        }
                    }
                    succes();
                    update("date_section_others");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error (saveAllSection)", ex);
        }
    }

    public void chooseTaxe() {
        if (taxe.getTaxe() != null ? taxe.getTaxe().getId() > 0 : false) {
            ManagedTaxes service = (ManagedTaxes) giveManagedBean(ManagedTaxes.class);
            if (service != null) {
                int idx = service.getTaxesList().indexOf(new YvsBaseTaxes(taxe.getTaxe().getId()));
                if (idx > -1) {
                    YvsBaseTaxes y = service.getTaxesList().get(idx);
                    taxe.setTaxe(UtilCompta.buildBeanTaxes(y));
                    if (!docDivers.isMontantTtc()) {
                        taxe.setMontant(docDivers.getMontant() * (taxe.getTaxe().getTaux() / 100));
                    } else {
                        docDivers.setMontantHt(docDivers.getMontant() / (1 + (taxe.getTaxe().getTaux() / 100)));
                        taxe.setMontant(docDivers.getMontant() - docDivers.getMontantHt());
                    }
                }
            }
        }
    }

    public void chooseCentre() {
        if (section.getCentre() != null ? section.getCentre().getId() > 0 : false) {
            ManagedCentreAnalytique service = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
            if (service != null) {
                int idx = service.getCentres().indexOf(new YvsComptaCentreAnalytique(section.getCentre().getId()));
                if (idx > -1) {
                    double taxe = docDivers.getTaxe();
                    double montant = docDivers.getMontantTotal() - taxe;
                    YvsComptaCentreAnalytique y = service.getCentres().get(idx);
                    section.setCentre(UtilCompta.buildBeanCentreAnalytique(y));
                    section.setMontant(montant - docDivers.getTotalCentre());
                }
            }
        }
    }

    public void blurTauxSection() {
        if (docDivers != null) {
            double taxe = docDivers.getTaxe();
            double montant = docDivers.getMontantTotal() - taxe;
            section.setMontant(montant * section.getTaux() / 100);
        }
    }

    public void blurValeurSection() {
        if (docDivers != null) {
            double taxe = docDivers.getTaxe();
            double montant = docDivers.getMontantTotal() - taxe;
            section.setTaux(section.getMontant() * 00 / montant);
        }
    }

    public void chooseCategorie() {
//        if ((docDivers.getCategorieComptable() != null) ? docDivers.getCategorieComptable().getId() > 0 : false) {
//            YvsBaseCategorieComptable d_ = categories.get(categories.indexOf(new YvsBaseCategorieComptable(docDivers.getCategorieComptable().getId())));
//            CategorieComptable d = UtilCom.buildBeanCategorieComptable(d_);
//            cloneObject(docDivers.getCategorieComptable(), d);
//        } else {
//            docDivers.setCategorieComptable(new CategorieComptable());
//        }
    }

    public void onTiersSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseTiers t_ = (YvsBaseTiers) ev.getObject();
            Tiers t = UtilTiers.buildBeanTiers(t_);
            cloneObject(docDivers.getTiers(), t);
        }
    }

    public void annulerOrder() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (dao.isComptabilise(selectDoc.getId(), Constantes.SCR_DIVERS)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return;
                }
            }
            List<YvsComptaCaisseDocDivers> l = dao.loadNameQueries("YvsBaseDocDivers.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
            if (l != null ? l.isEmpty() : true) {
                if (docDivers.getMontant() > 0) {
                    openDialog("dlgConfirmAnnuler_");
                } else {
                    if (true) {
                        selectDoc.setAnnulerBy(null);
                        selectDoc.setValiderBy(null);
                        selectDoc.setDateAnnuler(null);
                        selectDoc.setDateValider(null);
                        if (currentUser != null ? currentUser.getId() > 0 : false) {
                            selectDoc.setAuthor(currentUser);
                        }
                        dao.update(selectDoc);
                    }
                }
            } else {
                for (YvsComptaCaisseDocDivers d : l) {
                    if (!d.getStatutDoc().equals(Constantes.ETAT_EDITABLE)) {
                        getErrorMessage("Impossible d'annuler cet ordre car il possède un transfert déja valide");
                        return;
                    }
                }
                openDialog("dlgConfirmAnnuler");
            }
        }
    }

    public void annulerOrder_() {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                List<YvsComptaCaisseDocDivers> l = dao.loadNameQueries("YvsBaseDocDivers.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
                for (YvsComptaCaisseDocDivers d : l) {
                    dao.delete(d);
                }
                annulerOrder();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("Erreur : ", ex);
        }
    }

    public void _annulerOrder_() {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                for (YvsComptaCaissePieceDivers p : selectDoc.getReglements()) {
                    dao.delete(p);
                }
                annulerOrder();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("Erreur : ", ex);
        }
    }

    public void refuserOrder() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (dao.isComptabilise(selectDoc.getId(), Constantes.SCR_DIVERS)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return;
                }
            }
            List<YvsComptaCaisseDocDivers> l = dao.loadNameQueries("YvsBaseDocDivers.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
            if (l != null ? l.isEmpty() : true) {
                if (docDivers.getMontant() > 0) {
                    openDialog("dlgConfirmRefuser_");
                } else {
                    if (true) {
                        selectDoc.setAnnulerBy(currentUser.getUsers());
                        selectDoc.setValiderBy(null);
                        selectDoc.setDateAnnuler(new Date());
                        selectDoc.setDateValider(null);
                        if (currentUser != null ? currentUser.getId() > 0 : false) {
                            selectDoc.setAuthor(currentUser);
                        }
                        dao.update(selectDoc);
                    }
                }
            } else {
                for (YvsComptaCaisseDocDivers d : l) {
                    if (!d.getStatutDoc().equals(Constantes.ETAT_EDITABLE)) {
                        getErrorMessage("Impossible d'annuler cet ordre car il possède un transfert déja valide");
                        return;
                    }
                }
                openDialog("dlgConfirmRefuser");
            }
        }
    }

    public void refuserOrder_() {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                List<YvsComptaCaisseDocDivers> l = dao.loadNameQueries("YvsBaseDocDivers.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
                for (YvsComptaCaisseDocDivers d : l) {
                    dao.delete(d);
                }
                refuserOrder();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
        }
    }

    public void _refuserOrder_() {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                for (YvsComptaCaissePieceDivers p : selectDoc.getReglements()) {
                    dao.delete(p);
                }
                refuserOrder();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void cloturer(YvsComptaCaisseDocDivers y) {
        selectDoc = y;
    }

    public void cloturer() {
        if (selectDoc == null) {
            return;
        }
        if (currentUser != null ? currentUser.getId() > 0 : false) {
            selectDoc.setAuthor(currentUser);
        }
        dao.update(selectDoc);
        documents.set(documents.indexOf(selectDoc), selectDoc);
        update("data_others");
    }

    public boolean changeStatut_(String etat, DocCaissesDivers doc, YvsComptaCaisseDocDivers doc_) {
        if (!"".equals(etat)) {
            String rq = "UPDATE yvs_compta_caisse_doc_divers SET statut_doc = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(doc_.getId(), 1)};
            dao.requeteLibre(rq, param);
            doc.setStatutDoc(etat);
//            doc_.setStatutDoc(etat);
            if (documents.contains(doc_)) {
                documents.set(documents.indexOf(doc_), doc_);
            }
            update("form_others");
            update("data_others");
            update("grp_btn_etat");
            return true;
        }
        return false;
    }

    public void buildAbonnement(YvsComptaAbonementDocDivers y) {
        if (y != null) {
            abonnement = UtilCompta.buildBeanAbonnement(y);
        }
    }

    public void clearParams() {
        numSearch = null;
        codeTiers = null;
        idsSearch = "";
        numCompte = null;
        typeSearch = null;
        natureSearch = null;
        statutSearch = null;
        statutRegSearch = null;
        date = false;
        _first = true;
        toValideLoad = false;
        naturesSearch.clear();
        paginator.getParams().clear();
        loadAllOthers(true, true);
        update("blog_search_others");
        update("blog_more_option_others");
    }

    public void addParamComptabilised(boolean load) {
        ParametreRequete p = new ParametreRequete("coalesce(y.comptabilise, false)", "comptabilise", comptaSearch, "=", "AND");
        if (comptaSearch != null) {
            String query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_doc_divers c RIGHT JOIN yvs_compta_caisse_doc_divers y ON c.divers = y.id "
                    + "INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.statut_doc = 'V' AND a.societe = ? "
                    + "AND c.id " + (comptaSearch ? "IS NOT NULL" : "IS NULL");
            Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1)};
            if (date) {
                query += " AND y.date_doc BETWEEN ? AND ?";
                param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3)};
            }
            Long count = (Long) dao.loadObjectBySqlQuery(query, param);
            nbrComptaSearch = count != null ? count : 0;
        }
        paginator.addParam(p);
        if (load) {
            loadAllOthers(true, true);
        }
    }

    public void searchByNum(String numSearch) {
        this.numSearch = numSearch;
        searchByNum();
    }

    public void searchByNumAndType(String num, String mouvement) {
        ParametreRequete p = new ParametreRequete("y.numPiece", "numPiece", null, "LIKE", "AND");
        if (num != null ? num.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "numPiece", num + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numPiece)", "numPiece", num.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.referenceExterne)", "numPiece", num.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
//        p = new ParametreRequete("y.mouvement", "mouvement", mouvement, "=", "AND");
//        paginator.addParam(p);

        addParamTiers(mouvement);
    }

    public void searchByNum() {
        ParametreRequete p = new ParametreRequete("y.numPiece", "numPiece", null, "LIKE", "AND");
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "numPiece", numSearch + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numPiece)", "numPiece", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.referenceExterne)", "numPiece", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamTiers() {
        ParametreRequete p = new ParametreRequete("y.idTiers", "tiers", null, "LIKE", "AND");
        if (codeTiers != null ? codeTiers.trim().length() > 0 : false) {
            //Recherche de la liste des tiers correspondant au code entré
            List<YvsBaseTiers> list = dao.loadNameQueries("YvsBaseTiers.findLikeCodeOrNoms", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), codeTiers.toUpperCase() + "%"});
            if (list != null ? !list.isEmpty() : false) {
                List<Long> clients = new ArrayList<>();
                List<Long> fournisseurs = new ArrayList<>();
                List<Long> tiers = new ArrayList<>();
                Tiers y;
                Profil profil;
                for (YvsBaseTiers entity : list) {
                    y = UtilTiers.buildBeanTiers(entity);
                    if (y.getProfils().size() > 0) {
                        profil = findOneProfil(y, codeTiers);
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
                    p = new ParametreRequete("y.idTiers", "tiers", -1, "=", "AND");
                } else {
                    p = new ParametreRequete(null, "tiers", codeTiers, "=", "AND");
                    if (!tiers.isEmpty()) {
                        ParametreRequete pp = new ParametreRequete(null, "idTiers", codeTiers, "=", "OR");
                        pp.getOtherExpression().add(new ParametreRequete("y.idTiers", "idTiers", tiers, "IN", "AND"));
                        pp.getOtherExpression().add(new ParametreRequete("y.tableTiers", "tableTiers", yvs.dao.salaire.service.Constantes.BASE_TIERS_TIERS, "=", "AND"));
                        p.getOtherExpression().add(pp);
                    }
                    if (!clients.isEmpty()) {
                        ParametreRequete pp = new ParametreRequete(null, "idClients", codeTiers, "=", "OR");
                        pp.getOtherExpression().add(new ParametreRequete("y.idTiers", "idClients", clients, "IN", "AND"));
                        pp.getOtherExpression().add(new ParametreRequete("y.tableTiers", "tableTiers", yvs.dao.salaire.service.Constantes.BASE_TIERS_CLIENT, "=", "AND"));
                        p.getOtherExpression().add(pp);
                    }
                    if (!fournisseurs.isEmpty()) {
                        ParametreRequete pp = new ParametreRequete(null, "idFsseurs", codeTiers, "=", "OR");
                        pp.getOtherExpression().add(new ParametreRequete("y.idTiers", "idFsseurs", fournisseurs, "IN", "AND"));
                        pp.getOtherExpression().add(new ParametreRequete("y.tableTiers", "tableTiers", yvs.dao.salaire.service.Constantes.BASE_TIERS_FOURNISSEUR, "=", "AND"));
                        p.getOtherExpression().add(pp);
                    }
                }
            } else {
                p = new ParametreRequete("y.idTiers", "tiers", -1, "=", "AND");
            }
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamTiers(String mouvement) {
        ParametreRequete p = new ParametreRequete("y.idTiers", "tiers", null, "LIKE", "AND");
        if (codeTiers != null ? codeTiers.trim().length() > 0 : false) {
            //Recherche de la liste des tiers correspondant au code entré
            List<YvsBaseTiers> list = dao.loadNameQueries("YvsBaseTiers.findLikeCodeOrNoms", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), codeTiers.toUpperCase() + "%"});
            if (list != null ? !list.isEmpty() : false) {
                List<Long> clients = new ArrayList<>();
                List<Long> fournisseurs = new ArrayList<>();
                List<Long> tiers = new ArrayList<>();
                Tiers y;
                Profil profil;
                for (YvsBaseTiers entity : list) {
                    y = UtilTiers.buildBeanTiers(entity);
                    if (y.getProfils().size() > 0) {
                        profil = findOneProfil(y, codeTiers);

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
                    p = new ParametreRequete("y.idTiers", "tiers", -1, "=", "AND");
                } else {
                    p = new ParametreRequete(null, "tiers", codeTiers, "=", "AND");
                    if (!tiers.isEmpty()) {
                        ParametreRequete pp = new ParametreRequete(null, "idTiers", codeTiers, "=", "OR");
                        pp.getOtherExpression().add(new ParametreRequete("y.idTiers", "idTiers", tiers, "IN", "AND"));
                        pp.getOtherExpression().add(new ParametreRequete("y.tableTiers", "tableTiers1", yvs.dao.salaire.service.Constantes.BASE_TIERS_TIERS, "=", "AND"));
                        p.getOtherExpression().add(pp);
                    }
                    if (!clients.isEmpty()) {
                        ParametreRequete pp = new ParametreRequete(null, "idClients", codeTiers, "=", "OR");
                        pp.getOtherExpression().add(new ParametreRequete("y.idTiers", "idClients", clients, "IN", "AND"));
                        pp.getOtherExpression().add(new ParametreRequete("y.tableTiers", "tableTiers2", yvs.dao.salaire.service.Constantes.BASE_TIERS_CLIENT, "=", "AND"));
                        p.getOtherExpression().add(pp);
                    }
                    if (!fournisseurs.isEmpty()) {
                        ParametreRequete pp = new ParametreRequete(null, "idFsseurs", codeTiers, "=", "OR");
                        pp.getOtherExpression().add(new ParametreRequete("y.idTiers", "idFsseurs", fournisseurs, "IN", "AND"));
                        pp.getOtherExpression().add(new ParametreRequete("y.tableTiers", "tableTiers3", yvs.dao.salaire.service.Constantes.BASE_TIERS_FOURNISSEUR, "=", "AND"));
                        p.getOtherExpression().add(pp);

                    }
                }
            } else {
                p = new ParametreRequete("y.idTiers", "tiers", -1, "=", "AND");
            }

        }
        paginator.addParam(p);
        p = new ParametreRequete("y.mouvement", "mouvement", mouvement, "=", "AND");
        paginator.addParam(p);

        documents = paginator.executeDynamicQuery("YvsComptaCaisseDocDivers", "y.dateDoc DESC, y.numPiece", true, true, (int) imax, dao);

    }

    public void addParamCompte() {
        ParametreRequete p = new ParametreRequete("y.compteGeneral.numCompte", "compte", null, "LIKE", "AND");
        if (numCompte != null ? numCompte.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "compte", numCompte + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.compteGeneral.numCompte)", "compte", numCompte.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.compteGeneral.intitule)", "compte", numCompte.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamType(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.typeDoc", "typeDoc", null, "=", "AND");
        if (ev != null ? ev.getNewValue() != null : false) {
            Long id = (Long) ev.getNewValue();
            p.setObjet(new YvsBaseTypeDocDivers(id));
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamMouvement() {
        ParametreRequete p = new ParametreRequete("y.mouvement", "mouvement", null, "=", "AND");
        if (typeSearch != null ? typeSearch.trim().length() > 0 : false) {
            p.setObjet(typeSearch);
            paginator.addParam(p);
            loadAllOthers(true, true);
        }
    }

    public void addParamNature() {
        ParametreRequete p = new ParametreRequete("y.typeDoc", "typeDoc", null, "=", "AND");
        if (natureSearch != null ? natureSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.typeDoc", "typeDoc", natureSearch, "=", "AND");
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamTypeTiers() {
        ParametreRequete p = new ParametreRequete("y.tableTiers", "tableTiers", null, "=", "AND");
        if (forSearch != null ? forSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.tableTiers", "tableTiers", forSearch, "=", "AND");
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamAgence() {
        ParametreRequete p = new ParametreRequete("y.agence", "agence", null, "=", "AND");
        if (agenceSearch > 0) {
            p = new ParametreRequete("y.agence", "agence", new YvsAgences(agenceSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamStatut() {
        if (statutSearch != null ? statutSearch.trim().equals("Z") : false) {
            openDialog("dlgMoreStatuts");
        } else {
            addParamStatut(true);
        }
    }

    public void addParamStatutRegle(boolean load) {
        ParametreRequete p = new ParametreRequete("y.statutRegle", "statutRegle", null, "=", "AND");
        if (statutRegSearch != null ? true : false) {
            p = new ParametreRequete("y.statutRegle", "statutRegle", statutRegSearch, egaliteStatutReg, "AND");
        }
        paginator.addParam(p);
        if (load) {
            loadAllOthers(true, true);
        }
    }

    public void addParamWithCompte() {
        ParametreRequete p = new ParametreRequete("y.compteGeneral", "compteGeneral", null, "=", "AND");
        if (withCompteSearch != null) {
            p = new ParametreRequete("y.compteGeneral", "compteGeneral", (withCompteSearch ? "IS NOT NULL" : "IS NULL"), (withCompteSearch ? "IS NOT NULL" : "IS NULL"), "AND");
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamWithTiers() {
        ParametreRequete p = new ParametreRequete("y.idTiers", "compteTiers", null, "=", "AND");
        if (withTiersSearch != null) {
            p = new ParametreRequete("COALESCE(y.idTiers, 0)", "compteTiers", (withTiersSearch ? 0 : 1), (withTiersSearch ? ">" : "<"), "AND");
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamStatut(boolean load) {
        ParametreRequete p = new ParametreRequete("y.statutDoc", "statutDoc", null, "=", "AND");
        if (statutSearch != null ? statutSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statutDoc", "statutDoc", statutSearch, egaliteStatut, "AND");
        }
        paginator.addParam(p);
        if (load) {
            loadAllOthers(true, true);
        }
    }

    public void chooseStatuts() {
        ParametreRequete p = new ParametreRequete("y.statutDoc", "statutDoc", null);
        if (statuts != null ? !statuts.isEmpty() : false) {
            boolean add = true;
            for (String s : statuts) {
                if (s != null ? s.trim().length() < 1 : true) {
                    add = false;
                    break;
                }
            }
            if (add) {
                p = new ParametreRequete("y.etat", "statut", statuts, "IN", "AND");
            }
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateDoc", "dateDoc", null, "BETWEEN", "AND");
        if (date) {
            p = new ParametreRequete("y.dateDoc", "dateDoc", dateDebut, dateFin, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamWithPlan() {
        ParametreRequete p = new ParametreRequete("y.id", "plan", null, "IN", "AND");
        if (withPlanSearch != null) {
            String query = "SELECT DISTINCT y.doc_divers FROM yvs_compta_abonement_doc_divers y INNER JOIN yvs_compta_caisse_doc_divers d ON y.doc_divers = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE a.societe = ?";
            List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
            if (ids != null ? ids.isEmpty() : true) {
                if (ids == null) {
                    ids = new ArrayList<>();
                }
                ids.add(-1L);
            }
            p = new ParametreRequete("y.id", "plan", ids, (withPlanSearch ? "IN" : "NOT IN"), "AND");
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamMontant() {
        ParametreRequete p = new ParametreRequete("y.montant", "montant", null, "BETWEEN", "AND");
        if (addMontant) {
            if (comparer.equals("BETWEEN")) {
                if (montantDebut <= montantFin) {
                    p = new ParametreRequete("y.montant", "montant", montantDebut, montantFin, comparer, "AND");
                }
            } else {
                double prix = comparer.equals(Constantes.SYMBOLE_SUP_EGALE) ? montantDebut : montantFin;
                p = new ParametreRequete("y.montant", "montant", prix, comparer, "AND");
            }
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamIds() {
        addParamIds(true);
        loadAllOthers(true, true);
    }

    public void addParamToValide() {
        ParametreRequete p = new ParametreRequete("(y.etapeValide+1)", "etape", null, "IN", "AND");
        if (toValideLoad) {
            List<Integer> ordre_depense_with_type = dao.loadNameQueries("YvsWorkflowAutorisationValidDoc.findOrdreStepeByNatureWithType", new String[]{"document", "niveau", "nature"}, new Object[]{Constantes.DOCUMENT_DOC_DIVERS_CAISSE, currentNiveau, Constantes.DEPENSE});
            List<Integer> ordre_depense_without_type = dao.loadNameQueries("YvsWorkflowAutorisationValidDoc.findOrdreStepeByNatureWithOutType", new String[]{"document", "niveau", "nature"}, new Object[]{Constantes.DOCUMENT_DOC_DIVERS_CAISSE, currentNiveau, Constantes.DEPENSE});
            List<Integer> ordre_recette_with_type = dao.loadNameQueries("YvsWorkflowAutorisationValidDoc.findOrdreStepeByNatureWithType", new String[]{"document", "niveau", "nature"}, new Object[]{Constantes.DOCUMENT_DOC_DIVERS_CAISSE, currentNiveau, Constantes.RECETTE});
            List<Integer> ordre_recette_without_type = dao.loadNameQueries("YvsWorkflowAutorisationValidDoc.findOrdreStepeByNatureWithOutType", new String[]{"document", "niveau", "nature"}, new Object[]{Constantes.DOCUMENT_DOC_DIVERS_CAISSE, currentNiveau, Constantes.RECETTE});
            if ((ordre_depense_with_type != null ? !ordre_depense_with_type.isEmpty() : false) || (ordre_depense_without_type != null ? !ordre_depense_without_type.isEmpty() : false) || (ordre_recette_with_type != null ? !ordre_recette_with_type.isEmpty() : false) || (ordre_recette_without_type != null ? !ordre_recette_without_type.isEmpty() : false)) {
                p = new ParametreRequete(null, "etape", 0, "IN", "AND");
                if ((ordre_depense_with_type != null) ? !ordre_depense_with_type.isEmpty() : false) {
                    ParametreRequete p1 = new ParametreRequete(null, "etape10", 0, "=", "OR");
                    p1.getOtherExpression().add(new ParametreRequete("y.mouvement", "mouvement10", Constantes.COMPTA_DEPENSE, "=", "AND"));
                    p1.getOtherExpression().add(new ParametreRequete("(y.etapeValide+1)", "etape10", ordre_depense_with_type, "IN", "AND"));
                    p1.getOtherExpression().add(new ParametreRequete("y.typeDoc", "typeDoc10", "IS NOT NULL", "IS NOT NULL", "AND"));
                    p.getOtherExpression().add(p1);
                }
                if ((ordre_depense_without_type != null) ? !ordre_depense_without_type.isEmpty() : false) {
                    ParametreRequete p1 = new ParametreRequete(null, "etape11", 0, "=", "OR");
                    p1.getOtherExpression().add(new ParametreRequete("y.mouvement", "mouvement11", Constantes.COMPTA_DEPENSE, "=", "AND"));
                    p1.getOtherExpression().add(new ParametreRequete("(y.etapeValide+1)", "etape11", ordre_depense_without_type, "IN", "AND"));
                    p1.getOtherExpression().add(new ParametreRequete("y.typeDoc", "typeDoc10", "IS NULL", "IS NULL", "AND"));
                    p.getOtherExpression().add(p1);
                }
                if ((ordre_recette_with_type != null) ? !ordre_recette_with_type.isEmpty() : false) {
                    ParametreRequete p1 = new ParametreRequete(null, "etape20", 0, "=", "OR");
                    p1.getOtherExpression().add(new ParametreRequete("y.mouvement", "mouvement20", Constantes.COMPTA_RECETTE, "=", "AND"));
                    p1.getOtherExpression().add(new ParametreRequete("(y.etapeValide+1)", "etape20", ordre_recette_with_type, "IN", "AND"));
                    p1.getOtherExpression().add(new ParametreRequete("y.typeDoc", "typeDoc20", "IS NOT NULL", "IS NOT NULL", "AND"));
                    p.getOtherExpression().add(p1);
                }
                if ((ordre_recette_without_type != null) ? !ordre_recette_without_type.isEmpty() : false) {
                    ParametreRequete p1 = new ParametreRequete(null, "etape21", 0, "=", "OR");
                    p1.getOtherExpression().add(new ParametreRequete("y.mouvement", "mouvement21", Constantes.COMPTA_RECETTE, "=", "AND"));
                    p1.getOtherExpression().add(new ParametreRequete("(y.etapeValide+1)", "etape21", ordre_recette_without_type, "IN", "AND"));
                    p1.getOtherExpression().add(new ParametreRequete("y.typeDoc", "typeDoc21", "IS NULL", "IS NULL", "AND"));
                    p.getOtherExpression().add(p1);
                }
            }
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void reBuildNumero() {
        if (tabIds != null ? tabIds.trim().length() > 0 : false) {
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                YvsComptaCaisseDocDivers current;
                for (Integer index : ids) {
                    current = reBuildNumero(documents.get(index), true, false);
                    documents.set(index, current);
                }
                succes();
            }
            tabIds = "";
        }
    }

    public YvsComptaCaisseDocDivers reBuildNumero(YvsComptaCaisseDocDivers y, boolean save, boolean msg) {
        if (y != null ? y.getId() > 0 : false) {
            String ref = genererReference((y.getMouvement().equals(Constantes.COMPTA_RECETTE) ? Constantes.TYPE_OD_RECETTE_NAME : Constantes.TYPE_OD_DEPENSE_NAME), y.getDateDoc());
            if ((ref != null) ? ref.trim().equals("") : true) {
                return y;
            }
            y.setNumPiece(ref);
            if (save) {
                dao.update(y);
            }
            int idx = documents.indexOf(y);
            if (idx > -1) {
                documents.set(idx, y);
            }
            if (msg) {
                succes();
            }
        }
        return y;
    }

    public void openTochangeNumero(YvsComptaCaisseDocDivers doc) {
        this.selectDoc = doc;
        this.selectDoc.setOldReference(doc.getNumPiece());
        openDialog("dlgChangeNumDoc");
        update("form_num_doc");
    }

    public void changeNumero() {
        if (isChangeNumdocAuto()) {
            reBuildNumero(selectDoc, true, true);
        } else {
            if (autoriser("fv_change_num_doc")) {
                Long nb = (Long) dao.loadObjectByNameQueries("YvsComptaCaisseDocDivers.countDocByNumDoc", new String[]{"id", "numDoc", "societe"}, new Object[]{selectDoc.getId(), selectDoc.getNumPiece(), currentAgence.getSociete()});
                if (nb != null ? nb <= 0 : true) {
                    dao.update(selectDoc);
                } else {
                    selectDoc.setNumPiece(selectDoc.getOldReference());
                    getErrorMessage("Ce numéro est déjà attribué");
                    return;
                }
            } else {
                openNotAcces();
                return;
            }
        }
        update("data_others");
    }

    @Override
    public void updateBean() {
        if (docDivers.getTaxes() != null ? !docDivers.getTaxes().isEmpty() : false) {
            double montant = docDivers.getMontant();
            for (YvsComptaTaxeDocDivers t : docDivers.getTaxes()) {
                t.setMontant(montant * t.getTaxe().getTaux() / 100);
                t.setDateUpdate(new Date());
                t.setAuthor(currentUser);
                dao.update(t);
            }
            update("form_taxe_others");
        }
        if (docDivers.getSections() != null ? !docDivers.getSections().isEmpty() : false) {
            double montant = docDivers.getMontant();
            for (YvsComptaCentreDocDivers t : docDivers.getSections()) {
                t.setMontant(montant * t.getCentre().getCoeficient() / 100);
                t.setDateUpdate(new Date());
                t.setAuthor(currentUser);
                dao.update(t);
            }
            update("date_section_others");
        }
    }

    /**
     * ******************************************
     */
    private List<YvsWorkflowEtapeValidation> saveEtapesValidation(YvsComptaCaisseDocDivers doc) {
        if ((doc.getTypeDoc() != null) ? doc.getTypeDoc().getId() <= 0 : true) {
            champ = new String[]{"titre", "nature", "societe"};
            val = new Object[]{Constantes.DOCUMENT_DOC_DIVERS_CAISSE, (doc.getMouvement().equals("D") ? Constantes.DEPENSE : Constantes.RECETTE), doc.getAgence().getSociete()};
            return dao.loadNameQueries("YvsWorkflowEtapeValidation.findByModelNatureActif", champ, val);
        } else {
            champ = new String[]{"titre", "nature", "societe", "typeDoc"};
            val = new Object[]{Constantes.DOCUMENT_DOC_DIVERS_CAISSE, doc.getMouvement().equals("D") ? Constantes.DEPENSE : Constantes.RECETTE, doc.getAgence().getSociete(), doc.getTypeDoc()};
            return dao.loadNameQueries("YvsWorkflowEtapeValidation.findByModelNatureAndTypeActif", champ, val);
        }
    }

    private List<YvsWorkflowValidDocCaisse> saveEtapesValidation(YvsComptaCaisseDocDivers doc, List<YvsWorkflowEtapeValidation> model) {
        //charge les étape de vailidation
        List<YvsWorkflowValidDocCaisse> re = new ArrayList<>();
        PaginatorResult pagin_ = new PaginatorResult();
        if (doc != null) {
            if (doc.getTypeDoc() != null ? doc.getTypeDoc().getId() > 0 : false) {
                pagin_.addParam(new ParametreRequete("y.typeDocDivers", "typeDoc", doc.getTypeDoc(), "=", "AND"));
            }
        }
        if (!model.isEmpty()) {
            YvsWorkflowValidDocCaisse vd;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    vd = new YvsWorkflowValidDocCaisse();
                    vd.setDateSave(new Date());
                    vd.setDateUpdate(new Date());
                    vd.setAuthor(currentUser);
                    vd.setEtape(et);
                    vd.setEtapeValid(false);
                    vd.setDocCaisse(doc);
                    vd.setOrdreEtape(et.getOrdreEtape());
                    vd = (YvsWorkflowValidDocCaisse) dao.save1(vd);
                    re.add(vd);
                }
            }
        }
        doc.setStatutDoc(Constantes.ETAT_EDITABLE);
        dao.update(doc);
        return ordonneEtapes(re);
    }

    private List<YvsWorkflowValidDocCaisse> ordonneEtapes(List<YvsWorkflowValidDocCaisse> l) {
        return YvsWorkflowValidDocCaisse.ordonneEtapes(l);
    }

    private List<YvsWorkflowValidDocCaisse> ordonneEtapes_OLD(List<YvsWorkflowValidDocCaisse> l) {
        List<YvsWorkflowValidDocCaisse> re = new ArrayList<>();
        YvsWorkflowValidDocCaisse first = null;
        //recherche la première étape       
        for (YvsWorkflowValidDocCaisse vm : l) {
            if (vm.getEtape().getFirstEtape() && vm.getEtape().getActif()) {
                first = vm;
                break;
            }
        }
        if (first == null ? l != null ? !l.isEmpty() : false : false) {
            Collections.sort(l, new YvsWorkflowValidDocCaisse());
            first = l.get(0);
        }
        if (first != null) {
            re.add(first);  //ajoute la première étapes au résultat
            boolean find;
            //tant qu'il existe une étape suivante active
            while ((first.getEtape().getEtapeSuivante() != null) ? (first.getEtape().getEtapeSuivante().getActif()) : false) {
                find = false;
                for (YvsWorkflowValidDocCaisse vm : l) {
                    if (first.getEtape().getEtapeSuivante().equals(vm.getEtape())) {
                        re.add(vm); //Ajoute l'étape
                        first = vm;
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    break;
                }
            }
        }
        int i = 0;
        //toutes les étapes ont été construite
        if (re.size() > 0) {
            first = re.get(0);
            //si la première étape n'est pas validé, on l'active
            if (!first.getEtapeValid()) {
                re.get(0).setEtapeActive(true);
            }
        }
        for (YvsWorkflowValidDocCaisse vm : re) {
            if (first.getEtape().getEtapeSuivante() != null && vm.getEtapeValid()) {
                i++;
                if (re.size() > i) {
                    //active l'etape suivante
                    re.get(i).setEtapeActive(true);
                    first = re.get(i);
                }
            }
            if (vm.getEtape().equals(re.get(re.size() - 1).getEtape()) && !vm.getEtapeValid()) {
                if ((re.size() - 2) >= 0) {
                    if (re.get(re.size() - 2).getEtapeValid()) {
                        vm.setEtapeActive(true);
                    }
                }
            }
        }
        return re;
    }

    private YvsWorkflowValidDocCaisse currentEtape;

    public void validEtapeOrdre(YvsWorkflowValidDocCaisse etape, boolean lastEtape) {
        validEtapeOrdre(docDivers, selectDoc, etape, lastEtape, true, dao);
    }

    public void validEtapeOrdre(DocCaissesDivers docDivers, YvsComptaCaisseDocDivers selectDoc, YvsWorkflowValidDocCaisse etape, boolean lastEtape, DaoInterfaceLocal dao) {
        validEtapeOrdre(docDivers, selectDoc, etape, lastEtape, false, dao);
    }

    public void validEtapeOrdre(DocCaissesDivers docDivers, YvsComptaCaisseDocDivers selectDoc, YvsWorkflowValidDocCaisse etape, boolean lastEtape, boolean save, DaoInterfaceLocal dao) {
        //vérifier que la personne qui valide l'étape a le droit 
        if (!asDroitValideEtape(etape.getEtape())) {
            openNotAcces();
        } else {
            boolean continu = true;
            if (save) {
                continu = saveNew(false);
                selectDoc = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{docDivers.getId()});
                selectDoc.setEtapesValidations(new ArrayList<>(docDivers.getEtapesValidations()));
            }
            if (continu) {
                //contrôle la cohérence des dates
                if (etape.getEtapeSuivante() == null) {
                    if (currentParam.getMontantSeuilDepenseOd() > 0 ? selectDoc.getMontant() > currentParam.getMontantSeuilDepenseOd() : false) {
                        if (!autoriser("compta_od_valid_max_seuil_montant")) {
                            openNotAccesAction("Vous ne disposez pas de privillège pour valider un OD pour le montant est superieur à " + currentParam.getMontantSeuilDepenseOd() + " !");
                            return;
                        }
                    }
                }
                int idx = docDivers.getEtapesValidations().indexOf(etape);
                if (idx >= 0) {
                    etape.setAuthor(currentUser);
                    etape.setDateUpdate(new Date());
                    etape.setEtapeValid(true);
                    etape.setEtapeActive(false);
                    etape.setMotif(null);
                    if (docDivers.getEtapesValidations().size() > (idx + 1)) {
                        docDivers.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                    dao.update(etape);
                    selectDoc.setStatutDoc(Constantes.ETAT_ENCOURS);
                    selectDoc.setEtapeValide(selectDoc.getEtapeValide() + 1);
                    dao.update(selectDoc);
                    docDivers.setStatutDoc(Constantes.ETAT_ENCOURS);
                    getInfoMessage("Validation effectué avec succès !");
                } else {
                    getErrorMessage("Impossible de continuer !");
                    return;
                }
                //cas de la validation de la dernière étapes
                if (etape.getEtapeSuivante() == null) {
                    if (validerOrder(docDivers, selectDoc, false)) {
                        this.selectDoc = selectDoc;
                    }
                }
                if (etape.getEtape().getReglementHere()) {
                    if (docDivers.getReglements() != null ? docDivers.getReglements().isEmpty() : true) {
                        if (docDivers.getCaisse() != null ? docDivers.getCaisse().getId() > 0 : false) {
                            YvsBaseModeReglement espece = modeEspece();
                            YvsComptaCaissePieceDivers piece = getPiece(null, docDivers.getCaisse(), docDivers, selectDoc, espece, docDivers.getMontant(), docDivers.getDateDoc());
                            if (docDivers.getBeneficiaire() != null ? docDivers.getBeneficiaire().trim().length() > 0 : false) {
                                piece.setBeneficiaire(docDivers.getBeneficiaire());
                            } else {
                                if (docDivers.getTiers() != null ? docDivers.getTiers().getId() > 0 : false) {
                                    piece.setBeneficiaire(docDivers.getTiers().getNom_prenom());
                                }
                            }
                            piece.setNumeroExterne(docDivers.getNumeroExterne());
                            piece = (YvsComptaCaissePieceDivers) dao.save1(piece);
                            docDivers.getReglements().add(piece);
                            idx = selectDoc.getReglements().indexOf(piece);
                            if (idx < 0) {
                                selectDoc.getReglements().add(piece);
                            }
                        }
                    }
                }
                docDivers.setEtapeValide(selectDoc.getEtapeValide());
                idx = documents.indexOf(selectDoc);
                if (idx > -1) {
//                    documents.get(idx).setEtapeValide(selectDoc.getEtapeValide());
//                    documents.get(idx).setStatutDoc(selectDoc.getStatutDoc());
                    documents.set(idx, selectDoc);
                    update("data_others");
                }
            }
        }
    }

    public void validEtapeOrdre_OLD(DocCaissesDivers docDivers, YvsComptaCaisseDocDivers selectDoc, YvsWorkflowValidDocCaisse etape, boolean lastEtape, boolean save, DaoInterfaceLocal dao) {
        //vérifier que la personne qui valide l'étape a le droit 
        if (!asDroitValideEtape(etape.getEtape())) {
            openNotAcces();
        } else {
            boolean continu = true;
            if (save) {
                continu = saveNew(false);
                selectDoc = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{docDivers.getId()});
                selectDoc.setEtapesValidations(new ArrayList<>(docDivers.getEtapesValidations()));
            }
            if (continu) {
                //contrôle la cohérence des dates
                if (etape.getEtape().getEtapeSuivante() == null) {
                    if (currentParam.getMontantSeuilDepenseOd() > 0 ? selectDoc.getMontant() > currentParam.getMontantSeuilDepenseOd() : false) {
                        if (!autoriser("compta_od_valid_max_seuil_montant")) {
                            openNotAccesAction("Vous ne disposez pas de privillège pour valider un OD pour le montant est superieur à " + currentParam.getMontantSeuilDepenseOd() + " !");
                            return;
                        }
                    }
                }
                int idx = docDivers.getEtapesValidations().indexOf(etape);
                if (idx >= 0) {
                    etape.setAuthor(currentUser);
                    etape.setDateUpdate(new Date());
                    etape.setEtapeValid(true);
                    etape.setEtapeActive(false);
                    etape.setMotif(null);
                    if (docDivers.getEtapesValidations().size() > (idx + 1)) {
                        docDivers.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                    dao.update(etape);
                    selectDoc.setStatutDoc(Constantes.ETAT_ENCOURS);
                    selectDoc.setEtapeValide(selectDoc.getEtapeValide() + 1);
                    dao.update(selectDoc);
                    docDivers.setStatutDoc(Constantes.ETAT_ENCOURS);
                    getInfoMessage("Validation effectué avec succès !");
                } else {
                    getErrorMessage("Impossible de continuer !");
                    return;
                }
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() == null) {
                    if (validerOrder(docDivers, selectDoc, false)) {
                        this.selectDoc = selectDoc;
                    }
                }
                if (etape.getEtape().getReglementHere()) {
                    if (docDivers.getReglements() != null ? docDivers.getReglements().isEmpty() : true) {
                        if (docDivers.getCaisse() != null ? docDivers.getCaisse().getId() > 0 : false) {
                            YvsBaseModeReglement espece = modeEspece();
                            YvsComptaCaissePieceDivers piece = getPiece(null, docDivers.getCaisse(), docDivers, selectDoc, espece, docDivers.getMontant(), docDivers.getDateDoc());
                            if (docDivers.getBeneficiaire() != null ? docDivers.getBeneficiaire().trim().length() > 0 : false) {
                                piece.setBeneficiaire(docDivers.getBeneficiaire());
                            } else {
                                if (docDivers.getTiers() != null ? docDivers.getTiers().getId() > 0 : false) {
                                    piece.setBeneficiaire(docDivers.getTiers().getNom_prenom());
                                }
                            }
                            piece.setNumeroExterne(docDivers.getNumeroExterne());
                            piece = (YvsComptaCaissePieceDivers) dao.save1(piece);
                            docDivers.getReglements().add(piece);
                            idx = selectDoc.getReglements().indexOf(piece);
                            if (idx < 0) {
                                selectDoc.getReglements().add(piece);
                            }
                        }
                    }
                }
                docDivers.setEtapeValide(selectDoc.getEtapeValide());
                idx = documents.indexOf(selectDoc);
                if (idx > -1) {
//                    documents.get(idx).setEtapeValide(selectDoc.getEtapeValide());
//                    documents.get(idx).setStatutDoc(selectDoc.getStatutDoc());
                    documents.set(idx, selectDoc);
                    update("data_others");
                }
            }
        }
    }

    public void motifEtapeOrdre(String motifEtape, boolean lastEtape) {
        this.motifEtape = motifEtape;
        this.lastEtape = lastEtape;
    }

    public void annulEtapeOrdre(YvsWorkflowValidDocCaisse etape, boolean lastEtape) {
        this.etape = etape;
        this.lastEtape = lastEtape;
        this.motifEtape = null;
        if (etape.getEtapeSuivante() == null) {
            if (dao.isComptabilise(docDivers.getId(), Constantes.SCR_DIVERS)) {
                openDialog("dlgConfirmAnnuleDoc");
                return;
            }
        }
        openDialog("dglMotifCancelEtape");
    }

    public void annulEtapeOrdre_OLD(YvsWorkflowValidDocCaisse etape, boolean lastEtape) {
        this.etape = etape;
        this.lastEtape = lastEtape;
        this.motifEtape = null;
        if (etape.getEtape().getEtapeSuivante() == null) {
            if (dao.isComptabilise(docDivers.getId(), Constantes.SCR_DIVERS)) {
                openDialog("dlgConfirmAnnuleDoc");
                return;
            }
        }
        openDialog("dglMotifCancelEtape");
    }

    public boolean annulEtapeOrdre() {
        return annulEtapeOrdre(selectDoc, docDivers, currentUser, etape, motifEtape);
    }

    public boolean annulEtapeOrdre(YvsComptaCaisseDocDivers current, DocCaissesDivers divers, YvsUsersAgence users, YvsWorkflowValidDocCaisse etape, String motif) {
        if (divers.getStatutDoc().equals(Constantes.ETAT_VALIDE) && divers.getStatutRegle() == Constantes.STATUT_DOC_PAYER) {
            getErrorMessage("Ce document a déjà été payé. vous ne pouvez plus l'annuler");
            return false;
        }
        if (!asDroitValideEtape(etape.getEtape(), users.getUsers())) {
            openNotAcces();
        } else {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                getErrorMessage("Vous devez précisez le motif");
                return false;
            }
            current = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{divers.getId()});
            if (divers != null ? divers.getId() < 1 : true) {
                divers = UtilCompta.buildBeanDocCaisse(current);
            }
            int idx = divers.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                //cas de la validation de la dernière étapes
                if (etape.getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "facture"};
                    val = new Object[]{etape.getEtapeSuivante().getEtape(), current};
                    YvsWorkflowValidDocCaisse y = (YvsWorkflowValidDocCaisse) dao.loadOneByNameQueries("YvsWorkflowValidDocCaisse.findByEtapeFacture", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        getErrorMessage("Vous devez au préalable annuler l'étape suivante");
                        return false;
                    }
                }
                if (dao.isComptabilise(divers.getId(), Constantes.SCR_DIVERS)) {
                    if (!autoriser("compta_od_annul_comptabilite")) {
                        getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                        return false;
                    }
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        if (!w.unComptabiliserDivers(current, false)) {
                            getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                            return false;
                        }
                    }
                }
                etape.setAuthor(users);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                etape.setMotif(motif);
                dao.update(etape);
                current.setEtapeValide((current.getEtapeValide() - 1) < 0 ? 0 : (current.getEtapeValide() - 1));
                current.setStatutDoc(current.getEtapeValide() < 1 ? Constantes.ETAT_EDITABLE : Constantes.ETAT_ENCOURS);
                dao.update(current);

                divers.setStatutDoc(current.getStatutDoc());
                divers.setEtapeValide(current.getEtapeValide());
                if (documents != null ? documents.contains(current) : false) {
                    int idx_ = documents.indexOf(current);
                    if (idx_ >= 0) {
                        documents.get(idx_).setEtapeValide(current.getEtapeValide());
                        documents.get(idx_).setStatutDoc(current.getStatutDoc());
                    }
                    update("data_others");
                }
                idx = divers.getEtapesValidations().indexOf(etape.getEtapeSuivante());
                if (idx >= 0) {
                    divers.getEtapesValidations().get(idx).setEtapeActive(false);
                }
                idx = selectDoc.getEtapesValidations().indexOf(etape);
                if (idx >= 0) {
                    current.getEtapesValidations().get(idx).setEtapeActive(false);
                    current.getEtapesValidations().get(idx).setEtapeValid(false);
                }
                getInfoMessage("Annulation effectué avec succès !");
                update("tabview_facture_achat");
                return true;
            } else {
                getErrorMessage("Impossible de continuer !");
            }
        }
        return false;
    }

    public boolean annulEtapeOrdre_OLD(YvsComptaCaisseDocDivers current, DocCaissesDivers divers, YvsUsersAgence users, YvsWorkflowValidDocCaisse etape, String motif) {
        if (divers.getStatutDoc().equals(Constantes.ETAT_VALIDE) && divers.getStatutRegle() == Constantes.STATUT_DOC_PAYER) {
            getErrorMessage("Ce document a déjà été payé. vous ne pouvez plus l'annuler");
            return false;
        }
        if (!asDroitValideEtape(etape.getEtape(), users.getUsers())) {
            openNotAcces();
        } else {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                getErrorMessage("Vous devez précisez le motif");
                return false;
            }
            current = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{divers.getId()});
            if (divers != null ? divers.getId() < 1 : true) {
                divers = UtilCompta.buildBeanDocCaisse(current);
            }
            int idx = divers.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "facture"};
                    val = new Object[]{etape.getEtape().getEtapeSuivante(), current};
                    YvsWorkflowValidDocCaisse y = (YvsWorkflowValidDocCaisse) dao.loadOneByNameQueries("YvsWorkflowValidDocCaisse.findByEtapeFacture", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        getErrorMessage("Vous devez au préalable annuler l'étape suivante");
                        return false;
                    }
                }
                if (dao.isComptabilise(divers.getId(), Constantes.SCR_DIVERS)) {
                    if (!autoriser("compta_od_annul_comptabilite")) {
                        getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                        return false;
                    }
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        if (!w.unComptabiliserDivers(current, false)) {
                            getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                            return false;
                        }
                    }
                }
                etape.setAuthor(users);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                etape.setMotif(motif);
                dao.update(etape);
                current.setEtapeValide((current.getEtapeValide() - 1) < 0 ? 0 : (current.getEtapeValide() - 1));
                current.setStatutDoc(current.getEtapeValide() < 1 ? Constantes.ETAT_EDITABLE : Constantes.ETAT_ENCOURS);
                dao.update(current);

                divers.setStatutDoc(current.getStatutDoc());
                divers.setEtapeValide(current.getEtapeValide());
                if (documents != null ? documents.contains(current) : false) {
                    int idx_ = documents.indexOf(current);
                    if (idx_ >= 0) {
                        documents.get(idx_).setEtapeValide(current.getEtapeValide());
                        documents.get(idx_).setStatutDoc(current.getStatutDoc());
                    }
                    update("data_others");
                }
                getInfoMessage("Annulation effectué avec succès !");
                update("tabview_facture_achat");
                return true;
            } else {
                getErrorMessage("Impossible de continuer !");
            }
        }
        return false;
    }

    public void print(YvsComptaCaissePieceDivers y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + "/icones/" + (y.getStatutPiece() == Constantes.STATUT_DOC_PAYER ? "solde.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(y.getMontant()));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                param.put("LOGO", returnLogo());
                executeReport("pc_divers", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedDocDivers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cancelStatutPayeOfPCVForBanque() {
        try {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            for (YvsComptaPhasePieceDivers pp : pieceCD.getPhasesReglement()) {
                if (w != null) {
                    w.unComptabiliserPhaseCaisseDivers(pp, false, true);
                }
                dao.delete(pp);
            }
            pieceCD.getPhasesReglement().clear();
            pieceCD.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            pieceCD.setAuthor(currentUser);
            pieceCD.setDateValider(null);
            pieceCD.setCaissier(null);
            dao.update(pieceCD);
            int idx = docDivers.getReglements().indexOf(pieceCD);
            if (idx >= 0) {
                docDivers.getReglements().set(idx, pieceCD);
                update("data_mensualite_others");
            }
            pieceCD.getDocDivers().setStatutRegle(Constantes.STATUT_DOC_ATTENTE);
            pieceCD.getDocDivers().setDateUpdate(new Date());
            pieceCD.getDocDivers().setAuthor(currentUser);
            dao.update(pieceCD.getDocDivers());
        } catch (Exception ex) {
            getErrorMessage("Impossible de terminer cette opération !");
        }
    }

    public void openDlgConfirmValid(YvsComptaCaissePieceDivers pc) {
        pieceCD = pc;
        boolean direct = true;
        if (pc.getModePaiement() != null ? pc.getModePaiement().getId() > 0 : false) {
            if (pc.getModePaiement().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                direct = false;
                //si on veux valider le paiement
                if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                    if (pc.getPhasesReglement().isEmpty()) {
                        if (!autoriser("encais_piece_cheque")) {
                            openNotAcces();
                            return;
                        }
                        List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{pc.getModePaiement(), "D".equals(pc.getDocDivers().getMouvement())});
                        //lié les phases à la pièce de règlements
                        YvsComptaPhasePieceDivers pp;
                        if (pc.getPhasesReglement() == null) {
                            pc.setPhasesReglement(new ArrayList<YvsComptaPhasePieceDivers>());
                        }
                        for (YvsComptaPhaseReglement ph : phases) {
                            pp = new YvsComptaPhasePieceDivers();
                            pp.setId(null);
                            pp.setAuthor(currentUser);
                            pp.setPhaseOk(false);
                            pp.setPhaseReg(ph);
                            pp.setPieceDivers(pc);
                            pp.setCaisse(pc.getCaisse());
                            pp.setDateSave(new Date());
                            pp.setDateUpdate(new Date());
                            pp = (YvsComptaPhasePieceDivers) dao.save1(pp);
                            pc.getPhasesReglement().add(pp);
                        }
                        int idx = docDivers.getReglements().indexOf(pc);
                        if (idx >= 0) {
                            docDivers.getReglements().set(idx, pc);
                            update("data_mensualite_others");
                        }
                        pc.getDocDivers().setStatutRegle(Constantes.STATUT_DOC_ENCOUR);
                        pc.getDocDivers().setDateUpdate(new Date());
                        pc.getDocDivers().setAuthor(currentUser);
                        dao.update(pc.getDocDivers());
                        idx = documents.indexOf(pc.getDocDivers());
                        if (idx >= 0) {
                            documents.set(idx, pc.getDocDivers());
                            update("data_others");
                        }
                        if (docDivers.getId() > 0) {
                            docDivers.setStatutRegle(Constantes.STATUT_DOC_ENCOUR);
                        }
                        succes();
                    } else {
                        getWarningMessage("Les phases de ce règlement ont déjà été générées !");
                    }
                } else {
                    //annulser le paiement de la pièce
                    openDialog("dlgCancelPcv1");
                }
            }
        }
        if (direct) {
            if (pieceCD.getCaisse() == null) {
                pieceCD.setCaisse(new YvsBaseCaisse());
            }
            openDialog("dlgConfirmValidePC");
        }
    }

    private void ordonnePhase(List<YvsComptaPhasePieceDivers> l, YvsComptaPhasePieceDivers p) {
        int idx = l.indexOf(p);
        if (idx >= 0) {
            if (!p.getPhaseOk()) {
                if ((idx - 1) >= 0) {
                    if (l.get(idx - 1).getPhaseOk()) {
                        l.get(idx).setEtapeActive(true);
                    } else {
                        p.setEtapeActive(false);
                    }
                } else {
                    l.get(idx).setEtapeActive(true);
                }
            } else {
                p.setEtapeActive(false);
            }
        }
    }

    private List<YvsComptaPhasePieceDivers> ordonnePhase(List<YvsComptaPhasePieceDivers> l) {
//        Collections.sort(l, new YvsComptaPhasePieceDivers());
        int idx = 0;
        while (idx < l.size()) {
            ordonnePhase(l, l.get(idx));
            idx++;
        }
        return l;
    }

    private boolean controleValidation(YvsComptaPhasePieceDivers pp) {
        if ((pp.getPieceDivers().getCaisse() != null) ? pp.getPieceDivers().getCaisse().getId() <= 0 : true) {
            getErrorMessage("Aucune banque n'a été trouvé !");
            return false;
        }
        if ((pp.getPieceDivers().getModePaiement() != null) ? pp.getPieceDivers().getModePaiement().getId() <= 0 : true) {
            getErrorMessage("Aucune banque n'a été trouvé !");
            return false;
        }
        return true;
    }

    public void comptabiliserPhaseCaisseDivers(YvsComptaPhasePieceDivers pp, boolean comptabilise) {
        selectPhaseDivers = pp;
        if (pieceCD.getPhasesReglement() != null ? !pieceCD.getPhasesReglement().isEmpty() : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                int idx = pieceCD.getPhasesReglement().indexOf(pp);
                if (idx > -1) {
                    if (idx == 0) {
                        if (comptabilise) {
                            w.comptabiliserPhaseCaisseDivers(pp, true, true);
                        } else {
                            openDialog("dlgComptabilisePhase");
                        }
                    } else {
                        YvsComptaPhasePieceDivers prec = pieceCD.getPhasesReglement().get(idx - 1);
                        if (dao.isComptabilise(prec.getId(), Constantes.SCR_PHASE_CAISSE_DIVERS)) {
                            if (comptabilise) {
                                w.comptabiliserPhaseCaisseDivers(pp, true, true);
                            } else {
                                openDialog("dlgComptabilisePhase");
                            }
                        } else {
                            openDialog("dlgComptabilisePhaseByForce");
                        }
                    }
                }
            }
        }
    }

    public void printPhase(YvsComptaPhasePieceDivers y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getPieceDivers().getStatutPiece() == Constantes.STATUT_DOC_PAYER ? "solde.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(y.getPieceDivers().getMontant()));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                executeReport("phase_pc_divers", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedDocDivers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void validEtapesPieces(YvsComptaPhasePieceDivers pp) {
        if (!pp.getPhaseOk()) {
            if (!asDroitValidePhase(pp.getPhaseReg())) {
                openNotAcces();
                return;
            }
            if (controleValidation(pp)) {
                if (pp.getPhaseReg().getActionInBanque()) {
                    if (!pp.getCaisse().getTypeCaisse().equals("BANQUE")) {
                        getErrorMessage("La validation de cette phase doit se passer dans une banque");
                        return;
                    }
                }
                pp.setPhaseOk(true);
                pp.setDateUpdate(new Date());
                pp.setStatut(Constantes.STATUT_DOC_VALIDE);
                dao.update(pp);
                pp.setEtapeActive(false);
                if (pp.getPhaseReg().getReglementOk()) {
                    if (!pp.getPieceDivers().getDocDivers().getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                        getErrorMessage("L'opération n'est pas encore validée !");
                        return;
                    }
                    pp.getPieceDivers().setStatutPiece(Constantes.STATUT_DOC_PAYER);
                    pp.getPieceDivers().setDateValider(pp.getDateValider());
                } else {
                    if (pp.getPieceDivers().getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                        pp.getPieceDivers().setStatutPiece(Constantes.STATUT_DOC_ENCOUR);
                    }
                }
                dao.update(pp.getPieceDivers());
                pieceCD.setStatutPiece(pp.getPieceDivers().getStatutPiece());

                int idx = pieceCD.getPhasesReglement().indexOf(pp);
                if (idx >= 0 && (idx + 1) < pieceCD.getPhasesReglement().size()) {
                    pieceCD.getPhasesReglement().get(idx + 1).setEtapeActive(true);
                    currentPhaseDivers = pieceCD.getPhasesReglement().get(idx + 1);
                } else if (idx == (pieceCD.getPhasesReglement().size() - 1)) {
                    pieceCD.getPhasesReglement().get(idx).setEtapeActive(true);
                }
                idx = pieceCD.getPhasesReglement().indexOf(pp);
                if (idx >= 0) {
                    pieceCD.getPhasesReglement().set(idx, pp);
                }
                //Comptabilise la pièce 
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    w.comptabiliserPhaseCaisseDivers(pp, false, false);
                }
            }
        } else {
            getWarningMessage("Phase déjà valide! ");
        }
        update("head_form_suivi_prd");
        update("head_corps_form_suivi_prd");
    }

    //Supprimer les étapes de validation d'un chèque et ramener la pièce au statut en cours
    public void cancelAllEtapesPieces() {
        if (!autoriser("compta_cancel_piece_valide")) {
            openNotAcces();
            return;
        }
        YvsComptaCaissePieceDivers pc = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{pieceCD.getId()});
        if (pc != null ? pc.getId() != null ? pc.getId() > 0 : false : false) {
            //vérifie le droit:
            if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                try {
                    int i = 0;
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (pc.getPhasesReglement() != null ? !pc.getPhasesReglement().isEmpty() : false) {
                        for (YvsComptaPhasePieceDivers ph : pc.getPhasesReglement()) {
                            ph.setPhaseOk(false);
                            ph.setEtapeActive(i == 0);
                            ph.setAuthor(currentUser);
                            dao.update(ph);
                            if (w != null) {
                                w.unComptabiliserPhaseCaisseDivers(ph, false, true);
                            }
                            i++;
                            int idx = pieceCD.getPhasesReglement().indexOf(ph);
                            if (idx > -1) {
                                pieceCD.getPhasesReglement().set(idx, ph);
                            }
                        }
                    } else {
                        if (w != null) {
                            w.unComptabiliserCaisseDivers(pc, false);
                        }
                    }
                    pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                    if (pieceCD != null) {
                        pieceCD.setStatutPiece(pc.getStatutPiece());
                    }
                    pc.setDateUpdate(new Date());
                    pc.setAuthor(currentUser);
                    dao.update(pc);
                } catch (Exception ex) {
                    getErrorMessage("Impossible d'annuler les phases!");
                    log.log(Level.SEVERE, null, ex);
                }
            } else {
                openDialog("dlgConfirmCancel");
            }
        }
        update("head_form_suivi_prd");
        update("head_corps_form_suivi_prd");
    }

    public void cancelValidEtapesPieces(YvsComptaPhasePieceDivers pp) {
        cancelValidEtapesPieces(pp, false);
    }

    public void cancelValidEtapesPieces(YvsComptaPhasePieceDivers pp, boolean retour) {
        //l'étape suivante ne doit pas être validé
        if (!asDroitValidePhase(pp.getPhaseReg())) {
            openNotAcces();
            return;
        }
        int idx = pieceCD.getPhasesReglement().indexOf(pp);
        YvsComptaPhasePieceDivers pSvt = null;
        if (idx >= 0 && (idx + 1) < pieceCD.getPhasesReglement().size()) {
            pSvt = pieceCD.getPhasesReglement().get(idx + 1);
        } else if (idx == (pieceCD.getPhasesReglement().size() - 1) || idx == 0) {
            pSvt = pieceCD.getPhasesReglement().get(idx);
        }
        if (pSvt != null) {
            if (!pSvt.isEtapeActive() ? !pSvt.equals(pp) : false) {
                getErrorMessage("Vous ne pouvez annuler cette étape !");
                return;
            }
            pSvt.setEtapeActive(false);
            idx = pieceCD.getPhasesReglement().indexOf(pSvt);
            if (idx >= 0) {
                pieceCD.getPhasesReglement().set(idx, pSvt);
            }
        }
        if (dao.isComptabilise(pp.getId(), Constantes.SCR_PHASE_CAISSE_DIVERS)) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                return;
            }
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                if (retour) {
                    if (!w.comptabiliserPhaseCaisseDivers(pp, false, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return;
                    }
                } else {
                    if (!w.unComptabiliserPhaseCaisseDivers(pp, false, true)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return;
                    }
                }
            }
        }
        pp.setEtapeActive(true);
        pp.setPhaseOk(false);
        pp.setStatut(retour ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE);
        if (pp.getPhaseReg().getReglementOk()) {
            pp.getPieceDivers().setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            pp.getPieceDivers().setDateValider(null);
            dao.update(pp.getPieceDivers());

            pieceCD.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        }
        dao.update(pp);
        currentPhaseDivers = pp;
        currentPhaseDivers.setDateValider(new Date());

        YvsComptaCaissePieceDivers pc = pp.getPieceDivers();
        idx = pc.getPhasesReglement().indexOf(pp);
        if (idx >= 0) {
            pc.getPhasesReglement().set(idx, pp);
        }
        try {
            if (retour) {
                YvsBaseModeReglement mode = modeEspece();
                YvsComptaCaissePieceDivers y = buildPieceFromModel(-1, mode, pieceCD.getDocDivers(), pp.getCaisse(), new Date(), montantRetour);
                y.setParent(new YvsComptaCaissePieceDivers(pieceCD.getId()));
                y.setNote("Paiement pour pénalité d'extourne");
                y.setId(null);
                y = (YvsComptaCaissePieceDivers) dao.save1(y);
                pieceCD.getSousDivers().add(y);
                update("corps_form_suivi_prd");
            }
        } catch (Exception ex) {
            getException("Action impossible", ex);
            getErrorMessage("Action impossible");
        }
        update("head_form_suivi_prd");
        update("head_corps_form_suivi_prd");
    }

    public void chooseCaissePhaseDivers() {
        if (currentPhaseDivers.getCaisse() != null ? currentPhaseDivers.getCaisse().getId() > 0 : false) {
            ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (w != null) {
                int idx = w.getCaisses().indexOf(currentPhaseDivers.getCaisse());
                if (idx > -1) {
                    currentPhaseDivers.setCaisse(new YvsBaseCaisse(w.getCaisses().get(idx)));
                }
            }
        }
    }

    public void onSelectObjectForCheque(YvsComptaCaissePieceDivers y) {
        pieceCD = y;
        pieceCD.setPhasesReglement(dao.loadNameQueries("YvsComptaPhasePieceDivers.findByPiece", new String[]{"piece"}, new Object[]{y}));
        pieceCD.setPhasesReglement(ordonnePhase(pieceCD.getPhasesReglement()));
        if (pieceCD.getPhasesReglement() != null ? !pieceCD.getPhasesReglement().isEmpty() : false) {
            for (YvsComptaPhasePieceDivers r : pieceCD.getPhasesReglement()) {
                if (r.isEtapeActive()) {
                    currentPhaseDivers = r;
                    break;
                }
            }
        }
        if (currentPhaseDivers.getCaisse() == null) {
            currentPhaseDivers.setCaisse(new YvsBaseCaisse(y.getCaisse()));
        }
        currentPhaseDivers.setDateValider(new Date());
    }

    private YvsComptaCaissePieceDivers buildPieceFromModel(long id, YvsBaseModeReglement mode, YvsComptaCaisseDocDivers d, YvsBaseCaisse caisse, Date date, double montant) {
        YvsComptaCaissePieceDivers piece = new YvsComptaCaissePieceDivers(id);
        piece.setAuthor(currentUser);
        piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        piece.setMontant(montant);
        piece.setDatePaimentPrevu(date);
        piece.setDatePiece(new Date());
        piece.setMouvement(Constantes.MOUV_CAISS_ENTREE);
        String ref = genererReference(Constantes.TYPE_PC_DIVERS_NAME, piece.getDatePiece());
        if (ref != null ? ref.trim().length() < 1 : true) {
            return null;
        }
        piece.setNumPiece(ref);
        piece.setDocDivers(d);
        piece.setModePaiement(mode);
        if ((caisse != null) ? caisse.getId() > 0 : false) {
            piece.setCaisse(caisse);
        }
        return piece;
    }

    public void openTogeneratedPhaseDivers(YvsComptaCaissePieceDivers pc) {
        this.pieceCD = pc;
        openDialog("dlgInitPhase");
    }

    public void generatedPhaseDivers() {
        if (pieceCD != null && !pieceCD.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
            if (pieceCD.getPhasesReglement() != null) {
                try {
                    if (pieceCD.getCaisse() != null ? pieceCD.getCaisse().getId() < 1 : true) {
                        getErrorMessage("Vous devez precisez la caisse qui a été mouvementé");
                        return;
                    }
                    for (YvsComptaPhasePieceDivers ph : pieceCD.getPhasesReglement()) {
                        ph.setAuthor(currentUser);
                        dao.delete(ph);
                    }
                    pieceCD.getPhasesReglement().clear();
                    List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{pieceCD.getModePaiement(), "D".equals(pieceCD.getDocDivers().getMouvement())});
                    //lié les phases à la pièce de règlements
                    YvsComptaPhasePieceDivers pp;
                    if (pieceCD.getPhasesReglement() == null) {
                        pieceCD.setPhasesReglement(new ArrayList<YvsComptaPhasePieceDivers>());
                    }
                    for (YvsComptaPhaseReglement ph : phases) {
                        pp = new YvsComptaPhasePieceDivers(-1L);
                        pp.setAuthor(currentUser);
                        pp.setPhaseOk(false);
                        pp.setPhaseReg(ph);
                        pp.setPieceDivers(pieceCD);
                        if (pieceCD.getCaisse() != null ? pieceCD.getCaisse().getId() > 0 : false) {
                            pp.setCaisse(pieceCD.getCaisse());
                        }
                        pp = (YvsComptaPhasePieceDivers) dao.save1(pp);
                        pieceCD.getPhasesReglement().add(pp);
                    }
                    update("table_list_piece_cheque_achat");
                    succes();
                } catch (Exception ex) {
                    getErrorMessage("Impossible de réaliser cette action !");
                }
            }
        } else {
            if (pieceCD.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Cette pièce est déjà payé !");
            }
            if (pieceCD == null) {
                getErrorMessage("Aucune pièce de règlement n'a été selectionné !");
            }
        }
    }

    public void onSelectDistantBon(YvsComptaCaissePieceDivers y) {
        if (y != null ? y.getId() > 0 : false) {
            if (y.getBonProvisoire() != null ? y.getBonProvisoire().getId() > 0 : false) {
                ManagedBonProvisoire s = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
                if (s != null) {
                    s.onSelectObject(y.getBonProvisoire());
                    Navigations n = (Navigations) giveManagedBean(Navigations.class);
                    if (n != null) {
                        n.naviguationView("Bon Provisoire", "modCompta", "smenBonProvisoire", true);
                    }
                }
            }
        }
    }

    public void verifyComptabilised(Boolean comptabilised) {
        loadAllOthers(true, true);
        if (comptabilised != null) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                List<YvsComptaCaisseDocDivers> list = new ArrayList<>();
                list.addAll(documents);
                for (YvsComptaCaisseDocDivers y : list) {
                    y.setComptabilised(w.isComptabilise(y.getId(), Constantes.SCR_DIVERS));
                    if (comptabilised ? !y.isComptabilised() : y.isComptabilised()) {
                        documents.remove(y);
                    }
                }
            }
        }
        update("data_others");
    }

    public String findNameTiers(Long id, String table) {
        if (id != null ? id > 0 : false) {
            return dao.nameTiers(id, table, "N");
        }
        return "";
    }

    public void traitementLot() {
        for (YvsComptaCaisseDocDivers d : documents) {
            selectDoc = d;
            onSelectObject(d);
            for (YvsWorkflowValidDocCaisse et : ordonneEtapes(d.getEtapesValidations())) {
                if (!et.getEtapeValid()) {
                    validEtapeOrdre(et, false);
                }
            }
            //génère la pièce de règlement
            ManagedModeReglement service = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
            if (service != null) {
                int idx = service.getModels().indexOf(new YvsBaseModelReglement(1L));
                if (idx >= 0) {
                    YvsBaseModelReglement mdr = service.getModels().get(idx);
                    //decompose mdr
                    if (docDivers.getReglements().isEmpty()) {
                        docDivers.setBeneficiaire("DIVERS");
                        docDivers.setCaisseDefaut(new Caisses(1L, "CAISSE PRINCIPALE"));
                        decomposeMensualiteMdr(mdr);
                        saveAllPieceCaisse();
                        for (YvsComptaCaissePieceDivers p : docDivers.getReglements()) {
                            p.setDateValider(docDivers.getDateDoc());
                            p.setCaisse(new YvsBaseCaisse(1L));
                            validePc(p, d, false);
                        }
                    } else {
                        // paye
                        for (YvsComptaCaissePieceDivers p : docDivers.getReglements()) {
                            p.setCaisse(new YvsBaseCaisse(1L));
                            p.setDateValider(docDivers.getDateDoc());
                            validePc(p, d, false);
                        }
                    }

                }
            }
        }
    }

    public void forceOnViewDivers(YvsComptaBonProvisoire piece, YvsComptaCaissePieceDivers select) {
        if (piece != null ? piece.getId() > 0 : false) {
            if (piece.getBonDivers() != null) {
                piece.getBonDivers().setPiece(select);
            }
            if (select != null ? select.getId() > 0 : false) {
                addBonProvisoire(piece, select, true, true);
            }
        }
    }

    public YvsComptaJustificatifBon addBonProvisoire(YvsComptaBonProvisoire bon, YvsComptaCaissePieceDivers piece, boolean msg, boolean succes) {
        if (piece != null ? piece.getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez selectionner une mission!!!");
            }
            return null;
        }
        if (bon != null ? bon.getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez enregistrer un bon provisoire!!!");
            }
            return null;
        }
        if (bon.getBonDivers() != null ? (bon.getBonDivers().getId() > 0 ? !bon.getBonDivers().getPiece().equals(piece) : false) : false) {
            openDialog("dlgConfirmBonProvDivers");
            update("cfm_bon_divers");
            return null;
        }
        if (bon.getMontant() <= 0) {
            if (msg) {
                getErrorMessage("Vous devez entrer le montant du bon");
            }
            return null;
        }
        if (bon.getMontant() > piece.getMontant()) {
            if (msg) {
                getErrorMessage("Impossible de créer un bon au montant superieur au reglement du document");
            }
            return null;
        }
        YvsComptaJustificatifBon y = new YvsComptaJustificatifBon(bon, piece);
        y.setAuthor(currentUser);
        if (bon.getBonDivers() != null ? bon.getBonDivers().getId() < 1 : true) {
            champ = new String[]{"bon", "piece"};
            val = new Object[]{bon, piece};
            YvsComptaJustificatifBon old = (YvsComptaJustificatifBon) dao.loadOneByNameQueries("YvsComptaJustificatifBon.findOne", champ, val);
            if (old != null ? old.getId() < 1 : true) {
                y.setId(null);
                y = (YvsComptaJustificatifBon) dao.save1(y);
            } else {
                y = old;
            }
        } else {
            y.setId(bon.getBonDivers().getId());
            dao.update(y);
        }
        bon.setBonDivers(y);
        int idx = piece.getBonsProvisoire().indexOf(y);
        if (idx > 0) {
            piece.getBonsProvisoire().set(idx, y);
        } else {
            piece.getBonsProvisoire().add(0, y);
        }
        if (succes) {
            succes();
        }
        return y;
    }

    public YvsComptaCaissePieceDivers createNewPieceCaisse(DocCaissesDivers d, YvsComptaCaissePieceDivers pt, boolean delete) {
        return createNewPieceCaisse(d, pt, delete, false);
    }

    public YvsComptaCaissePieceDivers createNewPieceCaisse(DocCaissesDivers d, YvsComptaCaissePieceDivers pt, boolean delete, boolean validePhase) {
        return createNewPieceCaisse(d, pt, delete, validePhase, Constantes.MOUV_CAISS_ENTREE.charAt(0));
    }

    public YvsComptaCaissePieceDivers createNewPieceCaisse(DocCaissesDivers d, YvsComptaCaissePieceDivers pt, boolean delete, boolean validePhase, char mouvement) {
        if (controleAddPiece(d, pt)) {
            YvsComptaCaissePieceDivers piece = new YvsComptaCaissePieceDivers(pt.getId());
            piece.setStatutPiece(Constantes.ETAT_REGLE.charAt(0));
            piece.setMontant(pt.getMontant());
            piece.setDatePaimentPrevu(pt.getDatePaimentPrevu());
            piece.setDatePiece(pt.getDatePiece());
            piece.setDatePaimentPrevu(pt.getDatePaimentPrevu());
            piece.setDocDivers(UtilCompta.buildDocDivers(d));
            piece.setReferenceExterne(pt.getReferenceExterne());
            piece.setDateUpdate(new Date());

            piece.setDateSave(pt.getDateSave());
            piece.setAuthor(currentUser);
            piece.setNumPiece(pt.getNumPiece());
            piece.setMouvement(mouvement + "");
//            piece.setVerouille(false);
            if (pt.getCaisse() != null) {
                piece.setCaisse((pt.getCaisse().getId() > 0) ? pt.getCaisse() : null);
            }
            if (pt.getCaissier() != null) {
                piece.setCaissier((pt.getCaissier().getId() > 0) ? pt.getCaissier() : null);
            }
            if (piece.getId() <= 0 || (piece.getNumPiece() != null ? piece.getNumPiece().trim().length() < 1 : true)) {
                String numero = genererReference(Constantes.TYPE_PC_DIVERS_NAME, pt.getDatePiece(), pt.getCaisse().getId());
                if (numero != null ? numero.trim().length() < 1 : true) {
                    return null;
                }
                piece.setNumPiece(numero);
            }
            if (pt.getModePaiement() != null) {
                piece.setModePaiement((pt.getModePaiement().getId() > 0) ? pt.getModePaiement() : null);
            }
            if ((pt.getId() == null) ? true : pt.getId() <= 0) {
                piece.setDateSave(new Date());
                piece.setId(null);
                piece = (YvsComptaCaissePieceDivers) dao.save1(piece);
            } else {
                piece.setId(pt.getId());
                dao.update(piece);
            }
//            piece.setNew_(true);
            piece.setPhasesReglement(dao.loadNameQueries("YvsComptaPhasePieceDivers.findByPiece", new String[]{"piece"}, new Object[]{pt}));

            if (!validePhase) {
                if (!pt.getModePaiement().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) { // Si le paiement n'est pas par banque, on se rassure qu'aucune phases n'est rattaché. auquel cas, on les supprime
                    boolean ph_ok = false;
                    if (!delete) {
                        for (YvsComptaPhasePieceDivers pp : piece.getPhasesReglement()) {
                            if (pp.getPhaseOk()) {
                                ph_ok = true;
                                break;
                            }
                        }
                    }
                    if (!ph_ok) {
                        for (YvsComptaPhasePieceDivers pp : piece.getPhasesReglement()) {
                            try {
                                pp.setAuthor(currentUser);
                                dao.delete(pp);
                            } catch (Exception ex) {
                                log.log(Level.SEVERE, null, ex);
                            }
                        }
                        piece.getPhasesReglement().clear();
                    }
                }
            } else {
                for (YvsComptaPhasePieceDivers pp : piece.getPhasesReglement()) {
                    pp.setAuthor(currentUser);
                    pp.setDateUpdate(new Date());
                    pp.setPhaseOk(true);
                    dao.update(pp);
                }
            }

            return piece;
        } else {
            pieceCD = newPc();
        }
        return null;
    }

    public DocCaissesDivers searchDocument(String num) {
        DocCaissesDivers a = new DocCaissesDivers();
        a.setNumPiece(num);
        if (num != null ? num.trim().length() > 0 : false) {
            ParametreRequete p = new ParametreRequete("UPPER(y.numPiece)", "numPiece", num.trim().toUpperCase() + "%", "LIKE", "AND");
            paginator.addParam(p);
        } else {
            paginator.addParam(new ParametreRequete("y.numPiece", "numPiece", null));
        }
        return a;
    }

    private boolean controleAddPiece(DocCaissesDivers d, YvsComptaCaissePieceDivers pt) {
        if (pt.getMontant() <= 0) {
            getErrorMessage("Le montant est incorrecte !");
            return false;
        }

        if (pt.getId() > 0 && pt.getStatutPiece().equals(Constantes.STATUT_DOC_ANNULE)) {
            getErrorMessage("Vous ne pouvez modifier cette pièce de règlement,", "son statut n'est pas éditable");
            return false;
        }
        if (d != null) {
            double mtn;
            if (d.getReglements().contains(pt)) {//en cas de modification d'une pièce(isoler le montantF à comparer)
                mtn = (-d.getReglements().get(d.getReglements().indexOf(pt)).getMontant() + pt.getMontant());
            } else {
                mtn = pt.getMontant();
            }

        }
        if (pt.getDatePaimentPrevu() == null) {
            getErrorMessage("Vous devez préciser la date de paiement !");
            return false;
        }
        if (pt.getStatutPiece() == Constantes.STATUT_DOC_PAYER && (pt.getId() != null) ? pt.getId() > 0 : false) {
            getErrorMessage("La pièce en cours est déjà payé !");
            return false;
        }
        if ((pt.getModePaiement() != null) ? (pt.getModePaiement().getId() != null ? pt.getModePaiement().getId() < 1 : true) : true) {
            getErrorMessage("Vous devez préciser indiquer le moyen de paiement !");
            return false;
        }
        if (pt.getModePaiement().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
            if (pt.getReferenceExterne() != null ? pt.getReferenceExterne().trim().length() < 1 : true) {
                getErrorMessage("Vous devez préciser la reference externe !");
                return false;
            }
        }
        return true;
    }

    public boolean isComptabiliseBean(DocCaissesDivers y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_DIVERS));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabiliseAbonnement(YvsComptaAbonementDocDivers y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_ABONNEMENT_DIVERS));
                if (!y.getComptabilise()) {
                    y.setPieceContenu(null);
                }
            }
            return y.getComptabilise();
        }
        return false;
    }

    public boolean isComptabiliseCaisse(YvsComptaCaissePieceDivers y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_CAISSE_DIVERS));
                if (!y.getComptabilise()) {
                    y.setPieceContenu(null);
                }
            }
            return y.getComptabilise();
        }
        return false;
    }

    public boolean isComptabilise(YvsComptaCaisseDocDivers y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_DIVERS));
                if (!y.getComptabilise()) {
                    y.setPieceContenu(null);
                }
            }
            return y.getComptabilise();
        }
        return false;
    }

    public boolean displayBtnNext() {
        return (docDivers.getReglements() != null ? !docDivers.getReglements().isEmpty() : false) || (docDivers.getAbonnements() != null ? !docDivers.getAbonnements().isEmpty() : false);
    }

    public String titreSource() {
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            if (w.tableSource != null) {
                int index = -1;
                switch (w.tableSource) {
                    case Constantes.SCR_DIVERS:
                        return docDivers.getNumPiece();
                    case Constantes.SCR_CAISSE_DIVERS:
                        index = docDivers.getReglements().indexOf(new YvsComptaCaissePieceDivers(w.idSource));
                        if (index > -1) {
                            return docDivers.getReglements().get(index).getNumPiece();
                        }
                        break;
                    case Constantes.SCR_ABONNEMENT_DIVERS:
                        index = docDivers.getAbonnements().indexOf(new YvsComptaAbonementDocDivers(w.idSource));
                        if (index > -1) {
                            return formatDate.format(docDivers.getAbonnements().get(index).getEcheance());
                        }
                        break;
                }
            }
        }
        return null;
    }

    public void navigePiece(boolean avance) {
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            if (w.tableSource != null) {
                Long nextId = null;
                String nextTable = null;
                if (w.tableSource.equals(Constantes.SCR_DIVERS)) {
                    if (avance) {
                        if (docDivers.getReglements() != null ? !docDivers.getReglements().isEmpty() : false) {
                            nextId = docDivers.getReglements().get(0).getId();
                            nextTable = Constantes.SCR_CAISSE_DIVERS;
                        } else if (docDivers.getAbonnements() != null ? !docDivers.getAbonnements().isEmpty() : false) {
                            nextId = docDivers.getAbonnements().get(0).getId();
                            nextTable = Constantes.SCR_ABONNEMENT_DIVERS;
                        }
                    } else {
                        if (docDivers.getAbonnements() != null ? !docDivers.getAbonnements().isEmpty() : false) {
                            nextId = docDivers.getAbonnements().get(docDivers.getAbonnements().size() - 1).getId();
                            nextTable = Constantes.SCR_ABONNEMENT_DIVERS;
                        } else if (docDivers.getReglements() != null ? !docDivers.getReglements().isEmpty() : false) {
                            nextId = docDivers.getReglements().get(docDivers.getReglements().size() - 1).getId();
                            nextTable = Constantes.SCR_CAISSE_DIVERS;
                        }
                    }
                }
                if (nextId == null) {
                    if (w.tableSource.equals(Constantes.SCR_CAISSE_DIVERS)) {
                        int index = docDivers.getReglements().indexOf(new YvsComptaCaissePieceDivers(w.idSource));
                        if (avance) {
                            if (index < docDivers.getReglements().size() - 1) {
                                nextId = docDivers.getReglements().get(index + 1).getId();
                                nextTable = Constantes.SCR_CAISSE_DIVERS;
                            } else {
                                if (docDivers.getAbonnements() != null ? !docDivers.getAbonnements().isEmpty() : false) {
                                    nextId = docDivers.getAbonnements().get(0).getId();
                                    nextTable = Constantes.SCR_ABONNEMENT_DIVERS;
                                }
                            }
                        } else {
                            if (index > 0) {
                                nextId = docDivers.getReglements().get(index - 1).getId();
                                nextTable = Constantes.SCR_CAISSE_DIVERS;
                            } else {
                                nextId = docDivers.getId();
                                nextTable = Constantes.SCR_DIVERS;
                            }
                        }
                    }
                }
                if (nextId == null) {
                    if (w.tableSource.equals(Constantes.SCR_ABONNEMENT_DIVERS)) {
                        int index = docDivers.getAbonnements().indexOf(new YvsComptaAbonementDocDivers(w.idSource));
                        if (avance) {
                            if (index < docDivers.getAbonnements().size() - 1) {
                                nextId = docDivers.getAbonnements().get(index + 1).getId();
                                nextTable = Constantes.SCR_ABONNEMENT_DIVERS;
                            }
                        } else {
                            if (index > 0) {
                                nextId = docDivers.getAbonnements().get(index - 1).getId();
                                nextTable = Constantes.SCR_ABONNEMENT_DIVERS;
                            } else if (docDivers.getReglements() != null ? !docDivers.getReglements().isEmpty() : false) {
                                nextId = docDivers.getReglements().get(docDivers.getReglements().size() - 1).getId();
                                nextTable = Constantes.SCR_CAISSE_DIVERS;
                            }
                        }
                    }
                }
                if (nextId == null) {
                    nextId = docDivers.getId();
                    nextTable = Constantes.SCR_DIVERS;
                }
                w.displayPieceComptable(nextId, nextTable);
            }
        }
    }

    private List<YvsComptaPiecesComptable> contentCompta = new ArrayList<>();

    @Override
    public List<YvsComptaPiecesComptable> getContentCompta() {
        return contentCompta;
    }

    @Override
    public void setContentCompta(List<YvsComptaPiecesComptable> contentCompta) {
        this.contentCompta = contentCompta;
    }

    public void displayPieceComptableFacture(Long idAbonnement) {
        contentCompta.clear();
        if (idAbonnement != null ? idAbonnement > 0 : false) {
            String query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit "
                    + "FROM yvs_compta_content_journal_abonnement_divers cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                    + "WHERE cf.abonnement=? ORDER BY p.id";

            List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(idAbonnement, 1)});
            long idPiece = 0;
            YvsComptaPiecesComptable pc = null;
            YvsComptaContentJournal c;
            contentCompta.clear();
            for (Object[] line : result) {
                if (line[0] != null ? ((Long) line[0] != idPiece) : false) {
                    idPiece = (Long) line[0];
                    pc = new YvsComptaPiecesComptable(idPiece);
                    pc.setNumPiece((String) line[1]);
                    pc.setDatePiece((line[2] != null) ? (Date) line[2] : null);
                    contentCompta.add(pc);
                } else {
                    if (line[0] == null) {
                        pc = null;
                        continue;
                    }
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
                    pc.getContentsPiece().add(c);
                }
            }
        }
    }

    private String numCompte(Long id) {
        if (id != null) {
            return (String) dao.loadObjectByNameQueries("YvsBasePlanComptable.findNumeroById", new String[]{"id"}, new Object[]{id});
        }
        return null;
    }

    public void selectdocs(YvsComptaCaisseDocDivers doc) {
        if (doc != null ? doc.getId() > 0 : false) {
            System.out.println("doc = " + doc.getId());
            selectDoc = doc;
            notes = selectDoc.getNotes();

        }
    }

    public void addNote() {
        try {
            if (notes != null ? !notes.isEmpty() : false) {
                System.err.println("notes = " + notes);
                selectDoc.setNotes(notes);
                System.err.println("selectDoc = " + selectDoc);
                dao.update(selectDoc);
                succes();
                notes = "";
            } else {
                System.err.println("Null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void onLoadDiversProjet(YvsComptaCaisseDocDivers y) {
        selectDoc = y;
        chooseProjet();
    }

    public void chooseProjet() {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                nameQueri = "YvsProjProjetCaisseDocDivers.findByDocDivers";
                champ = new String[]{"docDivers"};
                val = new Object[]{selectDoc};

                String query = "SELECT p.id, p.code, p.libelle, d.id, d.code_departement, d.intitule, s.id, y.id FROM yvs_proj_projet p INNER JOIN yvs_proj_projet_service y ON y.projet = p.id "
                        + " INNER JOIN yvs_proj_departement s ON y.service = s.id INNER JOIN yvs_base_departement d ON s.service = d.id"
                        + " LEFT JOIN yvs_proj_projet_caisse_doc_divers c ON (c.projet = y.id AND c.doc_divers = ?) WHERE c.id IS NULL AND p.societe = ?";

                if (projet > 0) {
                    nameQueri = "YvsProjProjetCaisseDocDivers.findByProjetDocDivers";
                    champ = new String[]{"docDivers", "projet"};
                    val = new Object[]{selectDoc, new YvsProjProjet(projet)};
                    query += " AND p.id = " + projet;
                }
                selectDoc.setProjets(dao.loadNameQueries(nameQueri, champ, val));

                List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(selectDoc.getId(), 1), new Options(currentAgence.getSociete().getId(), 2)});
                YvsProjProjet p;
                YvsBaseDepartement d;
                YvsProjDepartement s;
                YvsProjProjetService y;
                YvsProjProjetCaisseDocDivers c;
                for (Object[] data : result) {
                    p = new YvsProjProjet((Long) data[0]);
                    p.setCode((String) data[1]);
                    p.setLibelle((String) data[2]);

                    d = new YvsBaseDepartement((Long) data[3]);
                    d.setCodeDepartement((String) data[4]);
                    d.setIntitule((String) data[5]);

                    s = new YvsProjDepartement((Long) data[6]);
                    s.setService(d);

                    y = new YvsProjProjetService((Long) data[7]);
                    y.setProjet(p);
                    y.setService(s);

                    c = new YvsProjProjetCaisseDocDivers(YvsProjProjetCaisseDocDivers.ids--);
                    c.setAuthor(currentUser);
                    c.setDocDivers(selectDoc);
                    c.setProjet(y);

                    selectDoc.getProjets().add(c);
                }
                if (projet > 0) {
                    query = "SELECT p.id, p.code, p.libelle, d.id, d.code_departement, d.intitule, s.id FROM yvs_proj_projet p, yvs_proj_departement s INNER JOIN yvs_base_departement d ON s.service = d.id"
                            + " LEFT JOIN yvs_proj_projet_service y ON (y.service = s.id AND y.projet = ?) WHERE y.id IS NULL AND p.id = ? AND d.societe = ?";
                    result = dao.loadListBySqlQuery(query, new Options[]{new Options(projet, 1), new Options(projet, 2), new Options(currentAgence.getSociete().getId(), 3)});
                    for (Object[] data : result) {
                        p = new YvsProjProjet((Long) data[0]);
                        p.setCode((String) data[1]);
                        p.setLibelle((String) data[2]);

                        d = new YvsBaseDepartement((Long) data[3]);
                        d.setCodeDepartement((String) data[4]);
                        d.setIntitule((String) data[5]);

                        s = new YvsProjDepartement((Long) data[6]);
                        s.setService(d);

                        y = new YvsProjProjetService(YvsProjProjetService.ids--);
                        y.setAuthor(currentUser);
                        y.setProjet(p);
                        y.setService(s);

                        c = new YvsProjProjetCaisseDocDivers(YvsProjProjetCaisseDocDivers.ids--);
                        c.setAuthor(currentUser);
                        c.setDocDivers(selectDoc);
                        c.setProjet(y);

                        selectDoc.getProjets().add(c);
                    }
                }
                update("data-od_projet");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cellEditProjet(CellEditEvent ev) {
        try {
            if (ev != null ? ev.getRowIndex() > -1 : false) {
                int index = ev.getRowIndex();
                YvsProjProjetCaisseDocDivers y = selectDoc.getProjets().get(index);
                if ((Double) ev.getNewValue() <= 0) {
                    if (y.getId() > 0) {
                        dao.delete(y);
                        y.setId(YvsProjProjetCaisseDocDivers.ids--);
                    }
                } else {
                    Double montant = (Double) dao.loadObjectByNameQueries("YvsProjProjetCaisseDocDivers.sumByDocDiversNotId", new String[]{"docDivers", "id"}, new Object[]{selectDoc, y.getId()});
                    if (((montant != null ? montant : 0) + (Double) ev.getNewValue()) > selectDoc.getMontantTotal()) {
                        y.setMontant((Double) ev.getOldValue());
                        if (index > -1) {
                            selectDoc.getProjets().set(index, y);
                        }
                        update("data-od_projet");
                        getErrorMessage("Vous ne pouvez pas exceder le montant du document");
                        return;
                    }
                    if (y.getProjet().getId() < 1) {
                        y.getProjet().setId(null);
                        y.setProjet((YvsProjProjetService) dao.save1(y.getProjet()));
                    }
                    y.setMontant((Double) ev.getNewValue());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    if (y.getId() > 0) {
                        dao.update(y);
                    } else {
                        y.setId(null);
                        y = (YvsProjProjetCaisseDocDivers) dao.save1(y);
                    }
                }
                if (index > -1) {
                    selectDoc.getProjets().set(index, y);
                }
                succes();
                update("data-od_projet");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean controleFiche(TiersDivers bean) {
        if (bean == null) {
            getErrorMessage("Action impossible!!!");
            return false;
        }
        if (bean.getTiers() != null ? bean.getTiers().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le tiers");
            return false;
        }
        if (bean.getDocDivers() != null ? bean.getDocDivers().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner un document");
            return false;
        }
        if (bean.getMontant() <= 0) {
            getErrorMessage("Vous devez preciser le montant");
            return false;
        }
        Double montant = (Double) dao.loadObjectByNameQueries("YvsComptaCaisseDocDiversTiers.sumByDocDiversNotId", new String[]{"docDivers", "id"}, new Object[]{selectDoc, bean.getId()});
        if (((montant != null ? montant : 0) + bean.getMontant()) > selectDoc.getMontantTotal()) {
            getErrorMessage("Vous ne pouvez pas exceder le montant du document");
            return false;
        }
        return true;
    }

    public void resetTiers() {
        tiers = new TiersDivers();
        selectTiers = new YvsComptaCaisseDocDiversTiers();
        update("form_tiers_others");
    }

    public void saveNewTiers() {
        try {
            tiers.setDocDivers(docDivers);
            if (controleFiche(tiers)) {
                selectTiers = UtilCompta.buildTiersDivers(tiers);
                selectTiers.setAuthor(currentUser);
                if (tiers.getId() < 1) {
                    selectTiers = (YvsComptaCaisseDocDiversTiers) dao.save1(selectTiers);
                    tiers.setId(selectTiers.getId());
                } else {
                    dao.update(selectTiers);
                }
                int index = docDivers.getListTiers().indexOf(selectTiers);
                if (index > -1) {
                    docDivers.getListTiers().set(index, selectTiers);
                } else {
                    docDivers.getListTiers().add(selectTiers);
                }
                resetTiers();
                update("data_tiers_others");
                succes();
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteAllTiers() {
        try {
            if (docDivers.getListTiers() != null ? !docDivers.getListTiers().isEmpty() : false) {
                if (!dao.isComptabilise(docDivers.getId(), Constantes.SCR_DIVERS)) {
                    int size = docDivers.getListTiers().size();
                    for (int i = 0; i < size; i++) {
                        dao.delete(docDivers.getListTiers().get(i));
                    }
                    docDivers.getListTiers().clear();
                    resetTiers();
                    update("data_tiers_others");
                    succes();
                } else {
                    getErrorMessage("Vous ne pouvez pas modifier cette pièce");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteTiers() {
        try {
            if (!dao.isComptabilise(docDivers.getId(), Constantes.SCR_DIVERS)) {
                dao.delete(selectTiers);
                if (tiers.getId() == selectTiers.getId()) {
                    resetTiers();
                }
                docDivers.getListTiers().remove(selectTiers);
                if (deleteRetenue ? (selectTiers.getRetenue() != null ? selectTiers.getRetenue().getId() > 0 : false) : false) {
                    deleteRetenueByTiersDivers(selectTiers);
                }
                update("data_tiers_others");
                succes();
            } else {
                getErrorMessage("Vous ne pouvez pas modifier cette pièce");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadOnTiers(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectTiers = (YvsComptaCaisseDocDiversTiers) ev.getObject();
            tiers = UtilCompta.buildBeanTiersDivers(selectTiers);
            //charge le tiers
            ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
            if (w != null) {
                tiers.setTiers(w.buildTiersByProfil(selectTiers.getIdTiers(), selectTiers.getTableTiers()));
            }
            update("form_tiers_others");
        }
    }

    public void unLoadOnTiers(UnselectEvent ev) {
        resetTiers();
    }

    public void deleteRetenueByDivers(YvsComptaCaisseDocDivers y) {
        try {
            if (y.getRetenues() != null ? !y.getRetenues().isEmpty() : false) {
                List<YvsGrhRetenueDocDivers> deletes = new ArrayList<>();
                for (YvsGrhRetenueDocDivers r : y.getRetenues()) {
                    boolean delete = r.getRetenue().getStatut().equals('E');
                    if (delete) {
                        for (YvsGrhDetailPrelevementEmps d : r.getRetenue().getListPrelevement()) {
                            if (d.getStatutReglement().equals('P')) {
                                delete = false;
                                break;
                            }
                        }
                    }
                    if (delete) {
                        dao.delete(r.getRetenue());
                        deletes.add(r);
                    }
                }
                if (y.getRetenues().size() != deletes.size()) {
                    getErrorMessage("Impossible de supprimer. certaines retenues sont payée ou en cours de paiement");
                }
                y.getRetenues().removeAll(deletes);
                int index = documents.indexOf(y);
                if (index > -1) {
                    documents.set(index, y);
                    update("data_others");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteRetenueByTiersDivers(YvsComptaCaisseDocDiversTiers y) {
        try {
            if (y.getRetenue() != null ? y.getRetenue().getId() > 0 : false) {
                boolean delete = y.getRetenue().getRetenue().getStatut().equals('E');
                if (delete) {
                    for (YvsGrhDetailPrelevementEmps d : y.getRetenue().getRetenue().getListPrelevement()) {
                        if (d.getStatutReglement().equals('P')) {
                            delete = false;
                            break;
                        }
                    }
                }
                if (delete) {
                    dao.delete(y.getRetenue().getRetenue());
                    y.setRetenue(null);
                    int index = docDivers.getListTiers().indexOf(y);
                    if (index > -1) {
                        docDivers.getListTiers().set(index, y);
                        update("data_tiers_others");
                    }
                    selectDoc.getRetenues().remove(y.getRetenue());

                    index = documents.indexOf(selectDoc);
                    if (index > -1) {
                        documents.set(index, selectDoc);
                        update("data_others");
                    }
                } else {
                    getErrorMessage("Impossible de supprimer. La retenue est payée ou en cours de paiement");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void genererRetenueByDivers(YvsComptaCaisseDocDivers y) {
        sourceRetenue = "D";
        try {
            selectDoc = y;
            montantRetenue = y.getMontantTotal();
            if (y != null ? y.getId() > 0 : false) {
                YvsGrhRetenueDocDivers r = (YvsGrhRetenueDocDivers) dao.loadOneByNameQueries("YvsGrhRetenueDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
                if (r != null ? r.getId() > 0 : false) {
                    getInfoMessage("Cette pièce est déjà associée à une retenue");
                } else {
                    ManagedRetenue service = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
                    if (service != null) {
                        service.loadAllTypeElementAddActif();
                        service.getElementAdd().getTypeElt().setId(0);
                        service.getElementAdd().getPlan().setId(0);
                        openDialog("dlgChoixTypeRet");
                        update("zone_choix_retenu_divers");
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void genererRetenueByTiersDivers(YvsComptaCaisseDocDiversTiers y) {
        sourceRetenue = "T";
        try {
            selectTiers = y;
            montantRetenue = y.getMontant();
            if (y != null ? y.getId() > 0 : false) {
                YvsGrhRetenueDocDivers r = (YvsGrhRetenueDocDivers) dao.loadOneByNameQueries("YvsGrhRetenueDocDivers.findByTiersDivers", new String[]{"tiersDivers"}, new Object[]{y});
                if (r != null ? r.getId() > 0 : false) {
                    getInfoMessage("Ce tiers est déjà associée à une retenue");
                } else {
                    ManagedRetenue service = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
                    if (service != null) {
                        service.loadAllTypeElementAddActif();
                        service.getElementAdd().getTypeElt().setId(0);
                        service.getElementAdd().getPlan().setId(0);
                        openDialog("dlgChoixTypeRet");
                        update("zone_choix_retenu_divers");
                        update("zone_choix_retenu_vente_reg");
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void valideRetenue() {
        if (sourceRetenue.equals("D")) {
            if (selectDoc.getTiers() != null ? selectDoc.getTiers().isEmpty() : true) {
                valideRetenue(selectDoc);
            } else {
                ManagedRetenue service = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
                if (service != null) {
                    if ((service.getElementAdd().getPlan().getId()) <= 0 || service.getElementAdd().getTypeElt().getId() <= 0) {
                        getErrorMessage("Formulaire incorrecte !");
                        return;
                    }
                    String message = null;
                    for (YvsComptaCaisseDocDiversTiers y : selectDoc.getTiers()) {
                        String m = valideRetenue(y, y.getMontant());
                        if (Util.asString(m)) {
                            message = m;
                        }
                    }
                    if (Util.asString(message)) {
                        getErrorMessage(message);
                    } else {
                        succes();
                    }
                }
            }
        } else {
            valideRetenue(selectTiers);
        }
    }

    private void valideRetenue(YvsComptaCaisseDocDivers y) {
        try {
            ManagedRetenue service = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
            if (service != null) {
                if ((service.getElementAdd().getPlan().getId()) <= 0 || service.getElementAdd().getTypeElt().getId() <= 0) {
                    getErrorMessage("Formulaire incorrecte !");
                    return;
                }
                YvsBaseTiers tiers = null;
                if (y.getTableTiers() != null) {
                    switch (y.getTableTiers()) {
                        case Constantes.BASE_TIERS_CLIENT:
                            tiers = (YvsBaseTiers) dao.loadOneByNameQueries("YvsComClient.findTiers", new String[]{"id"}, new Object[]{y.getIdTiers()});
                            break;
                        case Constantes.BASE_TIERS_FOURNISSEUR:
                            tiers = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseFournisseur.findTiers", new String[]{"id"}, new Object[]{y.getIdTiers()});
                            break;
                        case Constantes.BASE_TIERS_TIERS:
                            tiers = new YvsBaseTiers(y.getIdTiers());
                            break;
                    }
                }
                if (tiers != null ? tiers.getId() < 1 : true) {
                    getErrorMessage("Impossible de trouver le tiers associè à cette pièce", "Veuillez revoir votre paramétrage");
                    return;
                }
                YvsGrhEmployes employe = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCompteTiers", new String[]{"tiers", "societe"}, new Object[]{tiers, currentAgence.getSociete()});
                if (employe != null ? employe.getId() < 1 : true) {
                    getErrorMessage("Impossible de trouver l'employé représentant ce tiers", "Veuillez revoir votre paramétrage");
                    return;
                }
                service.getElementAdd().setContrat(new ContratEmploye(employe.getContrat().getId(), employe.getContrat().getReferenceContrat()));
                //enretistre la retenu
                YvsGrhElementAdditionel element = service.buildElementAdditionel(service.getElementAdd());
                if (element != null) {
                    element.setId(null);
                    element.setPiceReglement(null);
                    element.setPlanifier(true);
                    element.setContrat(UtilGrh.buildBeanContratEmploye(service.getElementAdd().getContrat()));
                    element.setPlanPrelevement(new YvsGrhPlanPrelevement(service.getElementAdd().getPlan().getId()));
                    element.setTypeElement(new YvsGrhTypeElementAdditionel(service.getElementAdd().getTypeElt().getId()));
                    element.setAuthor(currentUser);
                    element.setPermanent(false);
                    element.setDateUpdate(new Date());
                    element = (YvsGrhElementAdditionel) dao.save1(element);
                    for (YvsGrhDetailPrelevementEmps d : service.getElementAdd().getListPrelevement()) {
                        d.setAuthor(currentUser);
                        d.setId(null);
                        d.setRetenue(element);
                        d.setRetenuFixe(false);
                        d.setDateSave(new Date());
                        d.setDateUpdate(new Date());
                        d = (YvsGrhDetailPrelevementEmps) dao.save1(d);
                    }
                    YvsGrhRetenueDocDivers retenue = new YvsGrhRetenueDocDivers();
                    retenue.setRetenue(element);
                    retenue.setAuthor(currentUser);
                    retenue.setDocDivers(y);
                    retenue = (YvsGrhRetenueDocDivers) dao.save1(retenue);
                    y.getRetenues().add(retenue);

                    int index = documents.indexOf(y);
                    if (index > -1) {
                        documents.set(index, y);
                        update("data_others");
                    }
                    succes();
                } else {
                    getErrorMessage("Retenue non enregistré !");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            log.log(Level.SEVERE, null, ex);
        }
    }

    private void valideRetenue(YvsComptaCaisseDocDiversTiers y) {
        try {
            ManagedRetenue service = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
            if (service != null) {
                if ((service.getElementAdd().getPlan().getId()) <= 0 || service.getElementAdd().getTypeElt().getId() <= 0) {
                    getErrorMessage("Formulaire incorrecte !");
                    return;
                }
                YvsBaseTiers tiers = null;
                switch (y.getTableTiers()) {
                    case Constantes.BASE_TIERS_CLIENT:
                        tiers = (YvsBaseTiers) dao.loadOneByNameQueries("YvsComClient.findTiers", new String[]{"id"}, new Object[]{y.getIdTiers()});
                        break;
                    case Constantes.BASE_TIERS_FOURNISSEUR:
                        tiers = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseFournisseur.findTiers", new String[]{"id"}, new Object[]{y.getIdTiers()});
                        break;
                    case Constantes.BASE_TIERS_TIERS:
                        tiers = new YvsBaseTiers(y.getIdTiers());
                        break;
                }
                if (tiers != null ? tiers.getId() < 1 : true) {
                    getErrorMessage("Impossible de trouver le tiers associè à cette pièce", "Veuillez revoir votre paramétrage");
                    return;
                }
                YvsGrhEmployes employe = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCompteTiers", new String[]{"tiers", "societe"}, new Object[]{tiers, currentAgence.getSociete()});
                if (employe != null ? employe.getId() < 1 : true) {
                    getErrorMessage("Impossible de trouver l'employé représentant ce tiers", "Veuillez revoir votre paramétrage");
                    return;
                }
                service.getElementAdd().setContrat(new ContratEmploye(employe.getContrat().getId(), employe.getContrat().getReferenceContrat()));
                //enretistre la retenu
                YvsGrhElementAdditionel element = service.buildElementAdditionel(service.getElementAdd());
                if (element != null) {
                    element.setId(null);
                    element.setPiceReglement(null);
                    element.setPlanifier(true);
                    element.setContrat(UtilGrh.buildBeanContratEmploye(service.getElementAdd().getContrat()));
                    element.setPlanPrelevement(new YvsGrhPlanPrelevement(service.getElementAdd().getPlan().getId()));
                    element.setTypeElement(new YvsGrhTypeElementAdditionel(service.getElementAdd().getTypeElt().getId()));
                    element.setAuthor(currentUser);
                    element.setPermanent(false);
                    element.setDateUpdate(new Date());
                    element = (YvsGrhElementAdditionel) dao.save1(element);
                    for (YvsGrhDetailPrelevementEmps d : service.getElementAdd().getListPrelevement()) {
                        d.setAuthor(currentUser);
                        d.setId(null);
                        d.setRetenue(element);
                        d.setRetenuFixe(false);
                        d.setDateSave(new Date());
                        d.setDateUpdate(new Date());
                        d = (YvsGrhDetailPrelevementEmps) dao.save1(d);
                    }
                    YvsGrhRetenueDocDivers retenue = new YvsGrhRetenueDocDivers();
                    retenue.setRetenue(element);
                    retenue.setAuthor(currentUser);
                    retenue.setDocDivers(y.getDocDivers());
                    retenue.setTiersDivers(y);
                    y.setRetenue((YvsGrhRetenueDocDivers) dao.save1(retenue));

                    int index = docDivers.getListTiers().indexOf(y);
                    if (index > -1) {
                        docDivers.getListTiers().set(index, y);
                        update("data_tiers_others");
                    }
                    selectDoc.getRetenues().add(retenue);

                    index = documents.indexOf(selectDoc);
                    if (index > -1) {
                        documents.set(index, selectDoc);
                        update("data_others");
                    }
                    succes();
                } else {
                    getErrorMessage("Retenue non enregistré !");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            log.log(Level.SEVERE, null, ex);
        }
    }

    private String valideRetenue(YvsComptaCaisseDocDiversTiers y, double montant) {
        try {
            ManagedRetenue service = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
            if (service != null) {
                if ((service.getElementAdd().getPlan().getId()) <= 0 || service.getElementAdd().getTypeElt().getId() <= 0) {
                    return "Formulaire incorrecte !";
                }
                YvsBaseTiers tiers = null;
                switch (y.getTableTiers()) {
                    case Constantes.BASE_TIERS_CLIENT:
                        tiers = (YvsBaseTiers) dao.loadOneByNameQueries("YvsComClient.findTiers", new String[]{"id"}, new Object[]{y.getIdTiers()});
                        break;
                    case Constantes.BASE_TIERS_FOURNISSEUR:
                        tiers = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseFournisseur.findTiers", new String[]{"id"}, new Object[]{y.getIdTiers()});
                        break;
                    case Constantes.BASE_TIERS_TIERS:
                        tiers = new YvsBaseTiers(y.getIdTiers());
                        break;
                }
                if (tiers != null ? tiers.getId() < 1 : true) {
                    return "Impossible de trouver le tiers associè à cette pièce";
                }
                YvsGrhEmployes employe = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCompteTiers", new String[]{"tiers", "societe"}, new Object[]{tiers, currentAgence.getSociete()});
                if (employe != null ? employe.getId() < 1 : true) {
                    return "Impossible de trouver l'employé représentant ce tiers";
                }
                service.getElementAdd().setContrat(new ContratEmploye(employe.getContrat().getId(), employe.getContrat().getReferenceContrat()));
                //enretistre la retenu
                YvsGrhElementAdditionel element = service.buildElementAdditionel(service.getElementAdd());
                if (element != null) {
                    service.placerRetenu(montant);

                    element.setId(null);
                    element.setMontantElement(montant);
                    element.setPiceReglement(null);
                    element.setPlanifier(true);
                    element.setContrat(UtilGrh.buildBeanContratEmploye(service.getElementAdd().getContrat()));
                    element.setPlanPrelevement(new YvsGrhPlanPrelevement(service.getElementAdd().getPlan().getId()));
                    element.setTypeElement(new YvsGrhTypeElementAdditionel(service.getElementAdd().getTypeElt().getId()));
                    element.setAuthor(currentUser);
                    element.setPermanent(false);
                    element.setDateUpdate(new Date());
                    element = (YvsGrhElementAdditionel) dao.save1(element);
                    for (YvsGrhDetailPrelevementEmps d : service.getElementAdd().getListPrelevement()) {
                        d.setAuthor(currentUser);
                        d.setId(null);
                        d.setRetenue(element);
                        d.setRetenuFixe(false);
                        d.setDateSave(new Date());
                        d.setDateUpdate(new Date());
                        d = (YvsGrhDetailPrelevementEmps) dao.save1(d);
                    }
                    YvsGrhRetenueDocDivers retenue = new YvsGrhRetenueDocDivers();
                    retenue.setRetenue(element);
                    retenue.setAuthor(currentUser);
                    retenue.setDocDivers(y.getDocDivers());
                    retenue.setTiersDivers(y);
                    y.setRetenue((YvsGrhRetenueDocDivers) dao.save1(retenue));

                    int index = docDivers.getListTiers().indexOf(y);
                    if (index > -1) {
                        docDivers.getListTiers().set(index, y);
                        update("data_tiers_others");
                    }
                    selectDoc.getRetenues().add(retenue);

                    index = documents.indexOf(selectDoc);
                    if (index > -1) {
                        documents.set(index, selectDoc);
                        update("data_others");
                    }
                    return null;
                } else {
                    return "Retenue non enregistré !";
                }
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return "Action impossible!!!";
    }

}

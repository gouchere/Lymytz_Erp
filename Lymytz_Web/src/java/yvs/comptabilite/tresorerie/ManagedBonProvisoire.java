/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.tresorerie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.UtilCompta;
import yvs.base.tiers.ManagedTiers;
import yvs.base.tiers.Tiers;
import yvs.base.tiers.UtilTiers;
import yvs.commercial.achat.ManagedFactureAchat;
import yvs.commercial.param.ManagedTypeDocDivers;
import yvs.commercial.param.TypeDocDivers;
import yvs.comptabilite.caisse.Caisses;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.comptabilite.caisse.ManagedReglementAchat;
import yvs.comptabilite.caisse.ManagedReglementMission;
import yvs.dao.Util;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceMission;
import yvs.entity.compta.YvsComptaParametre;
import yvs.entity.compta.YvsComptaJustifBonMission;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.divers.YvsComptaJustifBonAchat;
import yvs.entity.compta.divers.YvsComptaJustificatifBon;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.workflow.YvsWorkflowAutorisationValidDoc;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidBonProvisoire;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.bean.mission.ManagedMission;
import yvs.grh.bean.mission.Mission;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.users.ManagedUser;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.Managed;
import static yvs.util.Managed.formatDate;
import yvs.util.ParametreRequete;
import yvs.util.enume.Nombre;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedBonProvisoire extends Managed<BonProvisoire, YvsComptaBonProvisoire> implements Serializable {

    private BonProvisoire bonProvisoire = new BonProvisoire();
    private List<YvsComptaBonProvisoire> documents;
    private YvsComptaBonProvisoire selectBon;
    public boolean date_up = false;
    private Date dateDebut_ = new Date(), dateFin_ = new Date();

    private String motifEtape;
    YvsWorkflowValidBonProvisoire etape;
    private boolean lastEtape;

    private String tabIds;
    YvsComptaParametre currentParam;
    private YvsWorkflowValidBonProvisoire currentEtape;

    private boolean addDate, select, toValideLoad = true, _first, createPiece = false;
    private Boolean forMissionSearch;
    private long caisseSearch;
    private Date dateDebutSearch = new Date(), dateFinSearch = new Date();
    private String numSearch, statutSearch, statutPaiement, statutJustify, tiersSearch, usersSearch, validateursearch;
    private String egaliteStatut = "!=", egalitePaiement = "!=", egaliteJustify = "!=";
    AutoComplete component = new AutoComplete();

    YvsBaseTiers tiersDivers;
    private double debitTiers, creditTiers, soldeTiers;

    private double totalNonJustifie;
    private double totalNonJustifieInscte;
    private double totalNonJustifieByUser;
    private int nbBpNonJustifAgence, nbMaxNonJustfScte, nbMaxNonJustifUser;

    public ManagedBonProvisoire() {
        documents = new ArrayList<>();
    }

    public String getValidateursearch() {
        return validateursearch;
    }

    public void setValidateursearch(String validateursearch) {
        this.validateursearch = validateursearch;
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

    public double getDebitTiers() {
        return debitTiers;
    }

    public void setDebitTiers(double debitTiers) {
        this.debitTiers = debitTiers;
    }

    public double getCreditTiers() {
        return creditTiers;
    }

    public void setCreditTiers(double creditTiers) {
        this.creditTiers = creditTiers;
    }

    public double getSoldeTiers() {
        return soldeTiers;
    }

    public void setSoldeTiers(double soldeTiers) {
        this.soldeTiers = soldeTiers;
    }

    public boolean isCreatePiece() {
        return createPiece;
    }

    public void setCreatePiece(boolean createPiece) {
        this.createPiece = createPiece;
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

    public Boolean getForMissionSearch() {
        return forMissionSearch;
    }

    public void setForMissionSearch(Boolean forMissionSearch) {
        this.forMissionSearch = forMissionSearch;
    }

    public long getCaisseSearch() {
        return caisseSearch;
    }

    public void setCaisseSearch(long caisseSearch) {
        this.caisseSearch = caisseSearch;
    }

    public boolean isFirst() {
        return _first;
    }

    public void setFirst(boolean _first) {
        this._first = _first;
    }

    public String getUsersSearch() {
        return usersSearch;
    }

    public void setUsersSearch(String usersSearch) {
        this.usersSearch = usersSearch;
    }

    public String getEgalitePaiement() {
        return egalitePaiement;
    }

    public void setEgalitePaiement(String egalitePaiement) {
        this.egalitePaiement = egalitePaiement;
    }

    public String getEgaliteJustify() {
        return egaliteJustify;
    }

    public void setEgaliteJustify(String egaliteJustify) {
        this.egaliteJustify = egaliteJustify;
    }

    public boolean isToValideLoad() {
        return toValideLoad;
    }

    public void setToValideLoad(boolean toValideLoad) {
        this.toValideLoad = toValideLoad;
    }

    public String getTiersSearch() {
        return tiersSearch;
    }

    public void setTiersSearch(String tiersSearch) {
        this.tiersSearch = tiersSearch;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getStatutSearch() {
        return statutSearch;
    }

    public void setStatutSearch(String statutSearch) {
        this.statutSearch = statutSearch;
    }

    public String getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(String statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public String getStatutJustify() {
        return statutJustify;
    }

    public void setStatutJustify(String statutJustify) {
        this.statutJustify = statutJustify;
    }

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public BonProvisoire getBonProvisoire() {
        return bonProvisoire;
    }

    public void setBonProvisoire(BonProvisoire bonProvisoire) {
        this.bonProvisoire = bonProvisoire;
    }

    public List<YvsComptaBonProvisoire> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComptaBonProvisoire> documents) {
        this.documents = documents;
    }

    public YvsComptaBonProvisoire getSelectBon() {
        return selectBon;
    }

    public void setSelectBon(YvsComptaBonProvisoire selectBon) {
        this.selectBon = selectBon;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public YvsComptaParametre getCurrentParam() {
        return currentParam;
    }

    public void setCurrentParam(YvsComptaParametre currentParam) {
        this.currentParam = currentParam;
    }

    public boolean isAddDate() {
        return addDate;
    }

    public void setAddDate(boolean addDate) {
        this.addDate = addDate;
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

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public YvsWorkflowValidBonProvisoire getCurrentEtape() {
        return currentEtape;
    }

    public void setCurrentEtape(YvsWorkflowValidBonProvisoire currentEtape) {
        this.currentEtape = currentEtape;
    }

    public double getTotalNonJustifie() {
        return totalNonJustifie;
    }

    public void setTotalNonJustifie(double totalNonJustifie) {
        this.totalNonJustifie = totalNonJustifie;
    }

    public double getTotalNonJustifieByUser() {
        return totalNonJustifieByUser;
    }

    public void setTotalNonJustifieByUser(double totalNonJustifieByUser) {
        this.totalNonJustifieByUser = totalNonJustifieByUser;
    }

    public double getTotalNonJustifieInscte() {
        return totalNonJustifieInscte;
    }

    public void setTotalNonJustifieInscte(double totalNonJustifieInscte) {
        this.totalNonJustifieInscte = totalNonJustifieInscte;
    }

    public int getNbBpNonJustifAgence() {
        return nbBpNonJustifAgence;
    }

    public void setNbBpNonJustifAgence(int nbBpNonJustifAgence) {
        this.nbBpNonJustifAgence = nbBpNonJustifAgence;
    }

    public int getNbMaxNonJustfScte() {
        return nbMaxNonJustfScte;
    }

    public void setNbMaxNonJustfiScte(int nbMaxNonJustfiScte) {
        this.nbMaxNonJustfScte = nbMaxNonJustfiScte;
    }

    public int getNbMaxNonJustifUser() {
        return nbMaxNonJustifUser;
    }

    public void setNbMaxNonJustifUser(int nbMaxNonJustifUser) {
        this.nbMaxNonJustifUser = nbMaxNonJustifUser;
    }

    @Override
    public void loadAll() {
        setTiersDefaut();
        if (statutSearch != null ? statutSearch.trim().length() < 1 : true) {
            this.egaliteStatut = "!=";
            this.statutSearch = Constantes.ETAT_VALIDE;
            addParamStatut(false);
        }
        addParamToValide();
        currentParam = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        if (bonProvisoire != null ? bonProvisoire.getOrdonnateur() != null ? bonProvisoire.getOrdonnateur().getId() < 1 : true : false) {
            bonProvisoire.setOrdonnateur(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
        }
        if (bonProvisoire.getId() < 1 ? bonProvisoire.getTypeDoc() != null ? bonProvisoire.getTypeDoc().getId() < 1 : true : false) {
            ManagedTypeDocDivers w = (ManagedTypeDocDivers) giveManagedBean(ManagedTypeDocDivers.class);
            if (w != null ? !w.getTypesDocDivers().isEmpty() : false) {
                bonProvisoire.setTypeDoc(new TypeDocDivers(w.getTypesDocDivers().get(0).getId(), w.getTypesDocDivers().get(0).getLibelle()));
            }
        }
        calculTotalBonNonJustifie();
    }

    public void loadAllOthers(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        String table = "YvsComptaBonProvisoire y JOIN FETCH y.ordonnateur LEFT JOIN FETCH y.justifyBy LEFT JOIN FETCH y.validerBy LEFT JOIN FETCH y.bonAchat LEFT JOIN FETCH y.bonMission LEFT JOIN FETCH y.bonDivers "
                + " LEFT JOIN FETCH y.justificatifs LEFT JOIN FETCH y.justificatifsAchats LEFT JOIN FETCH y.justificatifsMissions LEFT JOIN FETCH y.etapesValidations";
        documents = paginator.executeDynamicQuery("DISTINCT(y)", "DISTINCT(y)", table, "y.dateBon DESC, y.numero DESC", avance, init, (int) imax, dao);
        update("data_bon_provisoire");
    }

    public void loadNoJustifier() {
        forMissionSearch = null;
        addParamForMission(false);

        statutSearch = Constantes.ETAT_VALIDE;
        egaliteStatut = "=";
        addParamStatut(false);
        statutJustify = Constantes.ETAT_JUSTIFIE;
        egaliteJustify = "!=";
        addParamStatutJustify_();
        if (bonProvisoire != null ? bonProvisoire.getOrdonnateur() != null ? bonProvisoire.getOrdonnateur().getId() < 1 : true : false) {
            bonProvisoire.setOrdonnateur(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
        }
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComptaBonProvisoire> re = paginator.parcoursDynamicData("YvsComptaBonProvisoire", "y", "y.dateBon DESC, y.numero DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void avancer(boolean next) {
        loadAllOthers(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllOthers(true, true);
    }

    public void setTiersDefaut() {
        if (bonProvisoire.getTiers() != null ? bonProvisoire.getTiers().getId() < 1 : true) {
            YvsBaseTiers c = currentTiersDefault();
            if (c != null ? c.getId() > 0 : false) {
                cloneObject(bonProvisoire.getTiers(), UtilTiers.buildBeanTiers(c));
            }
            update("chmp_ct_dd");
        }
    }

    public YvsBaseTiers currentTiersDefault() {
        if (tiersDivers != null ? tiersDivers.getId() < 1 : true) {
            champ = new String[]{"societe", "ville"};
            val = new Object[]{currentAgence.getSociete(), currentAgence.getVille()};
            nameQueri = "YvsBaseFournisseur.findDefautVille";
            YvsBaseFournisseur lc = (YvsBaseFournisseur) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (lc != null) {
                tiersDivers = lc.getTiers();
            }
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            nameQueri = "YvsBaseFournisseur.findDefaut";
            lc = (YvsBaseFournisseur) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (lc != null) {
                getWarningMessage("Pensez à définir un client par défaut pour cette agence");
                tiersDivers = lc.getTiers();
            }
        }
        return tiersDivers;
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
        if (bonProvisoire.getStatut() != Constantes.STATUT_DOC_VALIDE) {
            if (!bonProvisoire.getEtapesValidations().isEmpty()) {
                //récupère l'Etape courante
                YvsWorkflowValidBonProvisoire step = giveCurrentStep();
                if (step != null) {
                    return etapeAutorise(currentUser.getUsers().getNiveauAcces(), step.getEtape());
                }
            } else if (bonProvisoire.getStatut() != Constantes.STATUT_DOC_EDITABLE) {
                return autoriser("d_div_update_doc");
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean controleFiche(BonProvisoire bean) {
        if (!canEditDoc()) {
            getErrorMessage("Vous ne pouvez pas modifié ce document");
            return false;
        }
        if (bean.getMontant() <= 0) {
            getErrorMessage("Vous devez entrer le montant ");
            return false;
        }
        if (bean.getDescription().trim().length() < 1) {
            getErrorMessage("Vous devez entrer le motif du bon provisoire");
            return false;
        }
        if (bean.getBeneficiaire().trim().length() < 1) {
            getErrorMessage("Vous devez entrer le beneficiare du bon provisoire");
            return false;
        }
        if (bean.getOrdonnateur() != null ? bean.getOrdonnateur().getId() < 1 : true) {
            getErrorMessage("Vous devez entrer l'ordonnateur du bon provisoire");
            return false;
        }
        if (bean.getNumero().trim().length() < 1 || (selectBon != null ? !selectBon.getDateBon().equals(bean.getDateBon()) : false)) {
            String num = genererReference(Constantes.TYPE_BP_NAME, bean.getDateBon());
            if (num != null ? num.trim().length() > 0 : false) {
                bean.setNumero(num);
            } else {
                return false;
            }
        }
//        return writeInExercice(bean.getDateDoc());
        return true;
    }

    private boolean controleFiche(YvsComptaBonProvisoire bean) {
        if (!bean.canEditable()) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        if (bean.getMontant() <= 0) {
            getErrorMessage("Vous devez entrer le montant ");
            return false;
        }
        if ((bean.getCaisse() != null) ? (bean.getCaisse().getId() != null) ? bean.getCaisse().getId() <= 0 : true : true) {
            getErrorMessage("Vous devez indiquer une caisse ");
            return false;
        }
        if (bean.getDescription().trim().length() < 1) {
            getErrorMessage("Vous devez entrer le motif du bon provisoire");
            return false;
        }
        if (bean.getBeneficiaire().trim().length() < 1) {
            getErrorMessage("Vous devez entrer le beneficiare du bon provisoire");
            return false;
        }
        if (bean.getOrdonnateur() != null ? bean.getOrdonnateur().getId() < 1 : true) {
            getErrorMessage("L'ordonnateur du bon n'a pas été trouvé", "Vous devez l'indiquer");
            return false;
        }
        if (bean.getNumero().trim().length() < 1) {
            String num = genererReference(Constantes.TYPE_BP_NAME, bean.getDateBon());
            if (num != null ? num.trim().length() > 0 : false) {
                bean.setNumero(num);
            } else {
                return false;
            }
        }
//        return writeInExercice(bean.getDateDoc());
        return true;
    }

    private boolean _controleFiche_(YvsComptaBonProvisoire bean) {
        if (bean == null) {
            getErrorMessage("Vous devez selectionner un document");
            return false;
        }
        if (!bean.canEditable()) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        return (giveExerciceActif(bean.getDateBon()) != null);
    }

    @Override
    public void resetFiche() {
        bonProvisoire = new BonProvisoire();
        selectBon = new YvsComptaBonProvisoire();
        bonProvisoire.setOrdonnateur(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
        setTiersDefaut();
        ManagedTypeDocDivers w = (ManagedTypeDocDivers) giveManagedBean(ManagedTypeDocDivers.class);
        if (w != null ? !w.getTypesDocDivers().isEmpty() : false) {
            bonProvisoire.setTypeDoc(new TypeDocDivers(w.getTypesDocDivers().get(0).getId(), w.getTypesDocDivers().get(0).getLibelle()));
        }
        tabIds = "";
        update("blog_form_bon_provisoire");
        update("entete_bon_provisoire");
    }

    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(bonProvisoire)) {
                boolean update = bonProvisoire.getId() > 0;
                selectBon = UtilCompta.buildBonProvisoire(bonProvisoire, currentUser);
                if (saveNew(selectBon)) {
                    if (!update) {
                        bonProvisoire.setEtapeTotal(selectBon.getEtapeTotal());
                        bonProvisoire.setId(selectBon.getId());
                        bonProvisoire.setEtapesValidations(new ArrayList<>(selectBon.getEtapesValidations()));
                    }

                    saveOrDeleteBonMission(selectBon, bonProvisoire);
                    saveOrDeleteBonAchat(selectBon, bonProvisoire);
                    saveOrDeleteBonDivers(selectBon, bonProvisoire);

                    documents.set(documents.indexOf(selectBon), selectBon);
                    actionOpenOrResetAfter(this);
                    succes();
                    return true;
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error... (saveNew)", ex);
        }
        return false;
    }

    private void saveOrDeleteBonMission(YvsComptaBonProvisoire entity, BonProvisoire bean) {
        if (bean.getMission() != null ? bean.getMission().getId() > 0 : false) {
            YvsComptaCaissePieceMission piece = bean.getPieceMission();
            if (createPiece) {
                PieceTresorerie pt = new PieceTresorerie();
                pt.setMontant(bean.getMontant());
                pt.setDatePaiementPrevu(bean.getDateBon());
                pt.setDatePiece(bean.getDateBon());
                pt.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
                pt.setCaisse(bean.getCaisse());
                ManagedReglementMission w = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
                if (w != null) {
                    piece = w.createOnePieceCaisse(bean.getMission(), pt, true, true);
                }
            }
            if (piece != null ? piece.getId() > 0 : false) {
                ManagedMission w = (ManagedMission) giveManagedBean(ManagedMission.class);
                if (w != null) {
                    YvsComptaJustifBonMission y = w.addBonProvisoire(entity, piece, true, false);
                    if (y != null ? y.getId() > 0 : false) {
                        bean.getJustificatifs().add(UtilCompta.buildBeanJustifierBon(y));
                    }
                }
            }
        } else {
            if (bean.getBonMission() != null ? bean.getBonMission().getId() > 0 : false) {
                dao.delete(bean.getBonMission());
                JustifierBon y = new JustifierBon(bean.getBonMission().getId(), "MI");
                bean.getJustificatifs().remove(y);
                bean.setBonMission(new YvsComptaJustifBonMission());
            }
        }
        update("data-justificatifs_bon_provisoire");
    }

    private void saveOrDeleteBonAchat(YvsComptaBonProvisoire entity, BonProvisoire bean) {
        if (bean.getAchat() != null ? bean.getAchat().getId() > 0 : false) {
            YvsComptaCaissePieceAchat piece = bean.getPieceAchat();
            if (createPiece) {
                piece = new YvsComptaCaissePieceAchat();
                piece.setAchat(bean.getAchat());
                piece.setAuthor(currentUser);
                piece.setCaisse(entity.getCaisse());
                piece.setCaissier(entity.getCaissier());
                piece.setDatePaimentPrevu(entity.getDateBon());
                piece.setDatePiece(entity.getDateBon());
                piece.setFournisseur(piece.getAchat().getFournisseur());
                piece.setModel(modeEspece());
                piece.setMontant(bean.getMontant());
                piece.setNote(bean.getDescription());
                piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                if (w != null) {
                    piece = w.createNewPieceCaisse(bean.getAchat(), piece, true, false, false);
                }
            }
            if (piece != null ? piece.getId() > 0 : false) {
                ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                if (w != null) {
                    YvsComptaJustifBonAchat y = w.addBonProvisoire(entity, piece, true, false);
                    if (y != null ? y.getId() > 0 : false) {
                        bean.getJustificatifs().add(UtilCompta.buildBeanJustifierBon(y));
                    }
                }
            }
        } else {
            if (bean.getBonAchat() != null ? bean.getBonAchat().getId() > 0 : false) {
                dao.delete(bean.getBonAchat());
                JustifierBon y = new JustifierBon(bean.getBonAchat().getId(), "FA");
                bean.getJustificatifs().remove(y);
                bean.setBonAchat(new YvsComptaJustifBonAchat());
            }
        }
        update("data-justificatifs_bon_provisoire");
    }

    private void saveOrDeleteBonDivers(YvsComptaBonProvisoire entity, BonProvisoire bean) {
        if (bean.getDivers() != null ? bean.getDivers().getId() > 0 : false) {
            YvsComptaCaissePieceDivers piece = bean.getPieceDivers();
            if (createPiece) {
                ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                if (w != null) {
                    piece = w.getPiece(-1L, bean.getCaisse(), new DocCaissesDivers(bean.getDivers().getId()), bean.getDivers(), modeEspece(), bean.getMontant(), entity.getDateBon());
                    piece.setCaissier(entity.getCaissier());
                    piece.setDatePaimentPrevu(entity.getDateBon());
                    piece.setDatePiece(entity.getDateBon());
                    piece.setNote(bean.getDescription());

                    piece.setId(null);
                    String numero = genererReference(Constantes.TYPE_PC_DIVERS_NAME, piece.getDatePiece());
                    if (numero != null ? numero.trim().length() < 1 : true) {
                        return;
                    }
                    piece.setNumPiece(numero);
                    piece = (YvsComptaCaissePieceDivers) dao.save1(piece);
                }
            }
            if (piece != null ? piece.getId() > 0 : false) {
                ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                if (w != null) {
                    YvsComptaJustificatifBon y = w.addBonProvisoire(entity, piece, true, false);
                    if (y != null ? y.getId() > 0 : false) {
                        bean.getJustificatifs().add(UtilCompta.buildBeanJustifierBon(y));
                    }
                }
            }
        } else {
            if (bean.getBonDivers() != null ? bean.getBonDivers().getId() > 0 : false) {
                dao.delete(bean.getBonAchat());
                JustifierBon y = new JustifierBon(bean.getBonDivers().getId(), "OD");
                bean.getJustificatifs().remove(y);
                bean.setBonDivers(new YvsComptaJustificatifBon());
            }
        }
        update("data-justificatifs_bon_provisoire");
    }

    public boolean saveNew(YvsComptaBonProvisoire selectBon) {
        try {
            if (controleFiche(selectBon)) {
                if (selectBon.getId() < 1) {
                    List<YvsWorkflowEtapeValidation> etapes = saveEtapesValidation(selectBon);
                    selectBon.setEtapeTotal(etapes != null ? etapes.size() : 0);
                    if (selectBon.getStatut().equals(Constantes.ETAT_VALIDE)) {
                        selectBon.setDateValider(selectBon.getDateBon());
                    }
                    if (selectBon.getStatutPaiement().equals(Constantes.ETAT_REGLE)) {
                        selectBon.setDatePayer(selectBon.getDateBon());
                    }
                    selectBon.setId(null);
                    selectBon = (YvsComptaBonProvisoire) dao.save1(selectBon);
                    selectBon.setEtapesValidations(saveEtapesValidation(selectBon, etapes));
                    documents.add(0, selectBon);
                } else {
                    //vérifier 
                    dao.update(selectBon);
                    documents.set(documents.indexOf(selectBon), selectBon);
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error... (saveNew)", ex);
        }
        return false;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComptaBonProvisoire> list = new ArrayList<>();
                YvsComptaBonProvisoire bean;
                for (Long ids : l) {
                    bean = documents.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    if (!_controleFiche_(bean)) {
                        return;
                    }
                    dao.delete(bean);
                    if (bonProvisoire.getId() == bean.getId()) {
                        resetFiche();
                    }
                }
                documents.removeAll(list);
                resetFiche();
                succes();
                update("data_bon_provisoire");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComptaBonProvisoire y) {
        selectBon = y;
    }

    public void deleteBean_() {
        try {
            if (selectBon != null) {
                if (!_controleFiche_(selectBon)) {
                    return;
                }
                dao.delete(selectBon);
                documents.remove(selectBon);
                if (bonProvisoire.getId() == selectBon.getId()) {
                    resetFiche();
                }
                succes();
                update("data_bon_provisoire");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void onSelectDistant(YvsComptaBonProvisoire da) {
        if (da != null ? da.getId() > 0 : false) {
            onSelectObject(da);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Bon Provisoire", "modCompta", "smenBonProvisoire", true);
            }
        }
    }

    @Override
    public void onSelectObject(YvsComptaBonProvisoire y) {
        y.setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidBonProvisoire.findByFacture", new String[]{"facture"}, new Object[]{y}));
        selectBon = y;
        bonProvisoire = UtilCompta.buildBeanBonProvisoire(y);
        //charge les étapes de validations
        if (!y.getEtapesValidations().isEmpty()) {
            bonProvisoire.setEtapesValidations(ordonneEtapes(y.getEtapesValidations()));
            bonProvisoire.setFirstEtape(bonProvisoire.getEtapesValidations().get(0).getEtape().getLabelStatut());
        }
        update("entete_bon_provisoire");
        update("blog_form_bon_provisoire");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComptaBonProvisoire y = (YvsComptaBonProvisoire) ev.getObject();
            onSelectObject(y);
            tabIds = documents.indexOf(y) + "";
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnUsers(SelectEvent ev) {
        if (ev != null) {
            YvsUsers y = (YvsUsers) ev.getObject();
            bonProvisoire.setOrdonnateur(new Users(y.getId(), y.getCodeUsers(), y.getNomUsers()));
        }
    }

    public void loadOnViewPieceMission(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCaissePieceMission y = (YvsComptaCaissePieceMission) ev.getObject();
            if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Cette pièce est déja reglée");
                return;
            }
            if (select) {
                ManagedMission w = (ManagedMission) giveManagedBean(ManagedMission.class);
                if (w != null) {
                    YvsComptaJustifBonMission entity = w.addBonProvisoire(selectBon, y, true, true);
                    if (entity != null ? entity.getId() > 0 : false) {
                        selectBon.setBonMission(entity);
                        int idx = selectBon.getJustificatifsMissions().indexOf(entity);
                        if (idx > -1) {
                            selectBon.getJustificatifsMissions().set(idx, entity);
                        }
                        idx = documents.indexOf(selectBon);
                        if (idx > -1) {
                            documents.set(idx, selectBon);
                            update("data_bon_provisoire");
                        }
                    }
                }
            } else {
                createPiece = false;
                bonProvisoire.setPieceMission(y);
            }
        }
    }

    public void loadOnViewMission(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsGrhMissions y = (YvsGrhMissions) ev.getObject();
            bonProvisoire.setMission(y);
            if (select) {
                createPiece = true;
                if (y.getReglements() != null ? !y.getReglements().isEmpty() : false) {
                    openDialog("dlgBonPieceMission");
                    update("data_piece_mission_bon");
                }
            } else {
                onSelectBonMission(y);
                update("blog_mission_bon");
            }
        }
    }

    public void loadOnViewPieceAchat(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCaissePieceAchat y = (YvsComptaCaissePieceAchat) ev.getObject();
            if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Cette pièce est déja reglée");
                return;
            }
            if (select) {
                ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                if (w != null) {
                    YvsComptaJustifBonAchat entity = w.addBonProvisoire(selectBon, y, true, true);
                    if (entity != null ? entity.getId() > 0 : false) {
                        selectBon.setBonAchat(entity);
                        int idx = selectBon.getJustificatifsAchats().indexOf(entity);
                        if (idx > -1) {
                            selectBon.getJustificatifsAchats().set(idx, entity);
                        }
                        idx = documents.indexOf(selectBon);
                        if (idx > -1) {
                            documents.set(idx, selectBon);
                            update("data_bon_provisoire");
                        }
                    }
                }
            } else {
                createPiece = false;
                bonProvisoire.setPieceAchat(y);
            }
        }
    }

    public void loadOnViewAchat(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComDocAchats y = (YvsComDocAchats) ev.getObject();
            bonProvisoire.setAchat(y);
            if (select) {
                createPiece = true;
                if (y.getReglements() != null ? !y.getReglements().isEmpty() : false) {
                    openDialog("dlgBonPieceAchat");
                    update("data_piece_achat_bon");
                }
            } else {
                onSelectBonAchat(y);
                update("blog_achat_bon");
            }
        }
    }

    public void loadOnViewPieceDivers(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCaissePieceDivers y = (YvsComptaCaissePieceDivers) ev.getObject();
            if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Cette pièce est déja reglée");
                return;
            }
            if (select) {
                ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                if (w != null) {
                    YvsComptaJustificatifBon entity = w.addBonProvisoire(selectBon, y, true, true);
                    if (entity != null ? entity.getId() > 0 : false) {
                        selectBon.setBonDivers(entity);
                        int idx = selectBon.getJustificatifs().indexOf(entity);
                        if (idx > -1) {
                            selectBon.getJustificatifs().set(idx, entity);
                        }
                        idx = documents.indexOf(selectBon);
                        if (idx > -1) {
                            documents.set(idx, selectBon);
                            update("data_bon_provisoire");
                        }
                    }
                }
            } else {
                createPiece = false;
                bonProvisoire.setPieceDivers(y);
            }
        }
    }

    public void loadOnViewDivers(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCaisseDocDivers y = (YvsComptaCaisseDocDivers) ev.getObject();
            bonProvisoire.setDivers(y);
            if (select) {
                createPiece = true;
                if (y.getReglements() != null ? !y.getReglements().isEmpty() : false) {
                    openDialog("dlgBonPieceDivers");
                    update("data_piece_divers_bon");
                }
            } else {
                onSelectBonDivers(y);
                update("blog_divers_bon");
            }
        }
    }

    public List<String> completeText(String query) {
        List<String> results = new ArrayList<>();
        ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (w != null) {
            for (String s : w.getListes()) {
                if (s != null ? s.toLowerCase().startsWith(query.toLowerCase()) : false) {
                    results.add(s);
                }
            }
        }
        return results;
    }

    public void onTiersSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            String noms = (String) ev.getObject();
            Tiers t = new Tiers();
            if (noms != null ? noms.trim().length() > 0 : false) {
                YvsBaseTiers y = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseTiers.findByNoms", new String[]{"societe", "noms"}, new Object[]{currentAgence.getSociete(), noms});
                if (y != null) {
                    t = new Tiers(y.getId(), y.getCodeTiers(), y.getNom(), y.getPrenom());
                }
            }
            bonProvisoire.setTiers(t);
        }
    }

    public void searchTiers() {
        String num = bonProvisoire.getBeneficiaire();
        bonProvisoire.setTiers(new Tiers());
        bonProvisoire.getTiers().setError(true);
        ManagedTiers m = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (m != null) {
            Tiers y = m.findTiers(num, false);
            if (m.getListTiers() != null ? !m.getListTiers().isEmpty() : false) {
                if (m.getListTiers().size() > 1) {
                    openDialog("dlgListTiers");
                    update("data_tiers_bon_provisoire");
                } else {
                    chooseTiers(y);
                }
                bonProvisoire.getTiers().setError(false);
            }
        }
    }

    public void forceOnViewMission() {
        if (bonProvisoire.getPieceMission() != null ? bonProvisoire.getPieceMission().getId() > 0 : false) {
            ManagedMission w = (ManagedMission) giveManagedBean(ManagedMission.class);
            if (w != null) {
                w.forceOnViewMission(selectBon, bonProvisoire.getPieceMission());
            }
        }
    }

    public void forceOnViewAchat() {
        if (bonProvisoire.getPieceAchat() != null ? bonProvisoire.getPieceAchat().getId() > 0 : false) {
            ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (w != null) {
                w.forceOnViewAchat(selectBon, bonProvisoire.getPieceAchat());
            }
        }
    }

    public void forceOnViewDivers() {
        if (bonProvisoire.getPieceDivers() != null ? bonProvisoire.getPieceDivers().getId() > 0 : false) {
            ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
            if (w != null) {
                w.forceOnViewDivers(selectBon, bonProvisoire.getPieceDivers());
            }
        }
    }

    public void openListMission(YvsComptaBonProvisoire y, boolean select) {
        this.select = select;
        if (y != null ? y.getId() > 0 : false) {
            selectBon = y;
        }
        openDialog("dlgBonMission");
    }

    public void openListAchat(YvsComptaBonProvisoire y, boolean select) {
        this.select = select;
        if (y != null ? y.getId() > 0 : false) {
            selectBon = y;
        }
        openDialog("dlgBonAchat");
    }

    public void openListDivers(YvsComptaBonProvisoire y, boolean select) {
        this.select = select;
        if (y != null ? y.getId() > 0 : false) {
            selectBon = y;
        }
        openDialog("dlgBonDivers");
    }

    public void openListTiers() {
        openDialog("dlgListTiers");
    }

    public void openListUsers() {
        openDialog("dlgListUsers");
    }

    public void findBeneficiaire() {
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (service != null) {
            Tiers t = new Tiers();
            String num = bonProvisoire.getBeneficiaire();
            if ((num != null) ? !num.isEmpty() : false) {
                t = service.findTiers(num, true);
            }
            chooseTiers(t);
        }
    }

    public void findOrdonnateur() {
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        ManagedUser service = (ManagedUser) giveManagedBean(ManagedUser.class);
        if (service != null) {
            Users u = new Users();
            String num = bonProvisoire.getOrdonnateur().getCodeUsers();
            if ((num != null) ? !num.isEmpty() : false) {
                u = service.searchUsersActif(num, false);
            }
            chooseUsers(u);
        }
    }

    private void chooseUsers(Users y) {
        if (y != null) {
            bonProvisoire.setOrdonnateur(y);
        }
    }

    public void loadOnTiers(SelectEvent ev) {
        if (ev != null) {
            YvsBaseTiers y = (YvsBaseTiers) ev.getObject();
            chooseTiers(new Tiers(y.getId(), y.getCodeTiers(), y.getNom(), y.getPrenom()));
        }
    }

    public void loadOnViewTiers(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseTiers y = (YvsBaseTiers) ev.getObject();
            Tiers bean = UtilTiers.buildBeanTiers(y);
            chooseTiers(bean);
        }
    }

    private void chooseTiers(Tiers y) {
        if (y != null) {
            bonProvisoire.setBeneficiaire(y.getNom_prenom());
            bonProvisoire.setTiers(y);
            update("chmp_tiers_bon_provisoire");
        }
    }

    public void chooseCaisse() {
        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (w != null) {
            long id = bonProvisoire.getCaisse().getId();
            if (id == -1) {
                w.loadAll(true, 0);
                update("chmp_caisse_piece_divers");
                update("chmp_caissier_bon_divers");
                update("menu_caisse_piece_divers");
                update("chmp_caisse_defaut_piece_divers");
            } else {
                int idx = w.getCaisses().indexOf(new YvsBaseCaisse(id));
                if (idx > -1) {
                    YvsBaseCaisse y = w.getCaisses().get(idx);
                    bonProvisoire.setCaisse(new Caisses(y.getId(), y.getIntitule()));
                    if (y.getResponsable() != null ? y.getResponsable().getId() > 0 : false) {
                        YvsGrhEmployes e = y.getResponsable();
                        if (e.getCodeUsers() != null ? e.getCodeUsers().getId() > 0 : false) {
                            YvsUsers u = e.getCodeUsers();
                            bonProvisoire.setCaissier(new Users(u.getId(), u.getCodeUsers(), u.getNomUsers()));
                        }
                    }
                }
            }
        }
    }

    public void chooseForBon(boolean force, boolean change) {
        if (!force && bonProvisoire.mustJustify()) {
            openDialog("dlgConfirmChangeJustify");
            return;
        }
        if (!change) {
            bonProvisoire.setBonFor(bonProvisoire.getOldBonFor());
        } else {
            bonProvisoire.setAchat(new YvsComDocAchats());
            bonProvisoire.setPieceAchat(new YvsComptaCaissePieceAchat());

            bonProvisoire.setMission(new YvsGrhMissions());
            bonProvisoire.setPieceMission(new YvsComptaCaissePieceMission());

            bonProvisoire.setDivers(new YvsComptaCaisseDocDivers());
            bonProvisoire.setPieceDivers(new YvsComptaCaissePieceDivers());

            bonProvisoire.setOldBonFor(bonProvisoire.getBonFor());
        }
    }

    public void clearParams() {
        numSearch = null;
        tiersSearch = null;
        usersSearch = null;
        statutSearch = null;
        statutPaiement = null;
        statutJustify = null;
        idsSearch = "";
        addDate = false;
        _first = true;
        toValideLoad = false;
        forMissionSearch = null;
        paginator.getParams().clear();
        loadAllOthers(true, true);
        update("blog_search_bon_provisoire");
        update("blog_more_option_bon_provisoire");
    }

    public YvsComptaBonProvisoire findBonProvisoirePaye(String num, boolean open) {
        paginator.addParam(new ParametreRequete("y.statutPaiement", "statutPaiement", Constantes.ETAT_REGLE, "=", "AND"));
        return findBonProvisoire(num, open);
    }

    public YvsComptaBonProvisoire findBonProvisoire(boolean open) {
        return findBonProvisoire(numSearch, open);
    }

    public YvsComptaBonProvisoire findBonProvisoire(String num, boolean open) {
        YvsComptaBonProvisoire a = new YvsComptaBonProvisoire();
        a.setNumero(num);
        a.setError(true);
        if (num != null ? num.trim().length() > 0 : false) {
            ParametreRequete p = new ParametreRequete(null, "numero", num.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numero)", "numero", num.toUpperCase() + "%", "LIKE", "OR"));
            paginator.addParam(p);
            loadAllOthers(true, true);
            if (documents != null ? !documents.isEmpty() : false) {
                a.setError(false);
                if (documents.size() > 1) {
                    if (open) {
                        openDialog("dlgListBons");
                    }
                } else {
                    a = documents.get(0);
                }
            } else {
                a.setNumero(num);
            }
        } else {
            paginator.addParam(new ParametreRequete("y.numero", "numero", null));
            loadAllOthers(true, true);
        }
        return a;
    }

    public void initBonProvisoire(BonProvisoire a) {
        if (a == null) {
            a = new BonProvisoire();
        }
        paginator.addParam(new ParametreRequete("y.numero", "numero", null));
        loadAllOthers(true, true);
    }

    public void addParamNumero() {
        ParametreRequete p = new ParametreRequete("y.numero", "numero", null, "LIKE", "AND");
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "numero", numSearch + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numero)", "numero", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.description)", "numero", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamTiers() {
        ParametreRequete p = new ParametreRequete("y.tiers.codeTiers", "tiers", null, "LIKE", "AND");
        if (tiersSearch != null ? tiersSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "tiers", tiersSearch + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.beneficiaire)", "beneficiaire", tiersSearch.toUpperCase() + "%", "LIKE", "OR"));
            ParametreRequete p1 = new ParametreRequete(null, "tiers", null, "LIKE", "OR");
            p1.getOtherExpression().add(new ParametreRequete("y.tiers", "tiers", "", "IS NOT NULL", "AND"));
            ParametreRequete p2 = new ParametreRequete(null, "tiers", null, "LIKE", "OR");
            p2.getOtherExpression().add(new ParametreRequete("UPPER(y.tiers.codeTiers)", "tiers", tiersSearch.toUpperCase() + "%", "LIKE", "OR"));
            p2.getOtherExpression().add(new ParametreRequete("UPPER(y.tiers.prenom)", "tiers", tiersSearch.toUpperCase() + "%", "LIKE", "OR"));
            p2.getOtherExpression().add(new ParametreRequete("UPPER(y.tiers.nom)", "tiers", tiersSearch.toUpperCase() + "%", "LIKE", "OR"));
            p1.getOtherExpression().add(p2);
            p.getOtherExpression().add(p1);
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamUsers() {
        ParametreRequete p = new ParametreRequete("y.ordonnateur.codeUsers", "users", null, "LIKE", "AND");
        if (usersSearch != null ? usersSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "users", usersSearch + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.ordonnateur.codeUsers)", "users", usersSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.ordonnateur.nomUsers)", "users", usersSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamValidateur() {
        ParametreRequete p = new ParametreRequete("y.validerBy.codeUsers", "usersV", null, "LIKE", "AND");
        if (validateursearch != null ? validateursearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "users", validateursearch + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.validerBy.codeUsers)", "usersV", validateursearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.validerBy.nomUsers)", "usersV", validateursearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamCaisse() {
        ParametreRequete p = new ParametreRequete("y.caisse", "caisse", null, "=", "AND");
        if (caisseSearch > 0) {
            p = new ParametreRequete("y.caisse", "caisse", new YvsBaseCaisse(caisseSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamStatut() {
        addParamStatut(true);
    }

    public void addParamStatut(boolean load) {
        ParametreRequete p = new ParametreRequete("y.statut", "statut", null, "=", "AND");
        if (statutSearch != null ? statutSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statut", "statut", statutSearch, egaliteStatut, "AND");
        }
        paginator.addParam(p);
        if (load) {
            loadAllOthers(true, true);
        }
    }

    public void addParamStatutPaiement() {
        addParamStatutPaiement(true);
    }

    public void addParamStatutPaiement(boolean load) {
        ParametreRequete p = new ParametreRequete("y.statutPaiement", "statutPaiement", null, "=", "AND");
        if (statutPaiement != null ? statutPaiement.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statutPaiement", "statutPaiement", statutPaiement, egalitePaiement, "AND");
        }
        paginator.addParam(p);
        if (load) {
            loadAllOthers(true, true);
        }
    }

    public void addParamStatutJustify_() {
        addParamStatutJustify(false);
    }

    public void addParamStatutJustify() {
        addParamStatutJustify(true);
    }

    public void addParamStatutJustify(boolean load) {
        ParametreRequete p = new ParametreRequete("y.statutJustify", "statutJustify", null, "=", "AND");
        if (statutJustify != null ? statutJustify.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statutJustify", "statutJustify", statutJustify, egaliteJustify, "AND");
        }
        paginator.addParam(p);
        if (load) {
            loadAllOthers(true, true);
        }
    }

    public void addParamForMission() {
        addParamForMission(true);
    }

    public void addParamForMission(boolean load) {
        ParametreRequete p = new ParametreRequete("y.bonMission", "bonMission", forMissionSearch, "=", "AND");
        if (forMissionSearch != null) {
            p = new ParametreRequete("y.bonMission", "bonMission", "X", (forMissionSearch ? "IS NOT NULL" : "IS NULL"), "AND");
        }
        paginator.addParam(p);
        if (load) {
            loadAllOthers(true, true);
        }
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateBon", "dateBon", null, "BETWEEN", "AND");
        if (addDate) {
            p = new ParametreRequete(null, "dateBon", dateDebutSearch, dateFinSearch, "BETWEEN", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.dateBon", "dateBon", dateDebutSearch, dateFinSearch, "BETWEEN", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.dateValider", "dateValider", dateDebutSearch, dateFinSearch, "BETWEEN", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.datePayer", "datePayer", dateDebutSearch, dateFinSearch, "BETWEEN", "OR"));
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void addParamToValide() {
        ParametreRequete p = new ParametreRequete("(y.etapeValide+1)", "etape", null, "IN", "AND");
        if (toValideLoad) {
            List<Integer> lnum = dao.loadNameQueries("YvsWorkflowAutorisationValidDoc.findOrdreStepe", new String[]{"document", "niveau"}, new Object[]{Constantes.DOCUMENT_BON_DIVERS_CAISSE, currentUser.getUsers().getNiveauAcces()});
            if ((lnum != null) ? !lnum.isEmpty() : false) {
                p = new ParametreRequete("(y.etapeValide+1)", "etape", lnum, "IN", "AND");
            }
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void openDlgConfirmSuspens(YvsComptaBonProvisoire y) {
        selectBon = y;
        openDialog("dlgConfirmSuspensPC");
    }

    public void openDlgConfirmValid() {
        openDlgConfirmValid(selectBon);
    }

    public void openDlgConfirmValid(YvsComptaBonProvisoire y) {
        selectBon = y;
        // si le bon provisoire est rattaché à une mission, elle ne doit pas encore être totalement payé
//        if (y.getBonMission() != null) {
//            YvsGrhMissions mi = y.getBonMission().getMission();
//            mi = setMontantTotalMission(mi, new Mission());
//            if (mi.getTotalFraisMission() <= mi.getTotalRegle()) {
//                getErrorMessage("Impossible de terminer cette action", "Le bon est rattaché à une mission déjà entièrement payé !");
//                return;
//            }
//        }
        if (selectBon.getCaisse() == null) {
            selectBon.setCaisse(new YvsBaseCaisse());
        }
        selectBon.setDatePayer(selectBon.getDateValider());
        update("chmp_caisse_bon_provisoire");
        openDialog("dlgConfirmValidePC");
    }

    public void suspendBon() {
        suspendBon(selectBon);
    }

    public void suspendBon(YvsComptaBonProvisoire selectBon) {
        if (selectBon != null ? selectBon.getId() > 0 : false) {
            if (!verifyDate(selectBon.getDatePayer(), -1)) {
                return;
            }
            if (!verifyCancelPieceCaisse(selectBon.getDatePayer())) {
                return;
            }
            if (selectBon.getStatutJustify().equals(Constantes.ETAT_JUSTIFIE)) {
                getErrorMessage("Ce bon est déja justifié");
                return;
            }
            selectBon.setStatutPaiement(Constantes.ETAT_ATTENTE);
            selectBon.setDateValider(null);
            dao.update(selectBon);
            update("chmp_montant_bon_provisoire");
            succes();
        } else {
            getErrorMessage("Aucune pièce n'a été selectionné !");
        }
    }

    public void valideBon() {
        valideBon(selectBon);
    }

    public void valideBon(YvsComptaBonProvisoire y) {
        valideBon(y, true);
    }

    public boolean valideBon(YvsComptaBonProvisoire y, boolean msg) {
        selectBon = y;
        y.setCaisse((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{y.getCaisse().getId()}));
        //le document divers doit être valide
        if (!y.canDelete()) {
            if (y.getCaisse() != null ? y.getCaisse().getId() < 1 : true) {
                if (msg) {
                    getErrorMessage("Vous devez préciser la caisse");
                }
                return false;
            }
            if (!y.getStatut().equals(Constantes.ETAT_VALIDE)) {
                if (msg) {
                    getErrorMessage("Le bon provisoire doit etre validé");
                }
                return false;
            }
            if (!verifyDate(y.getDatePayer(), -1)) {
                return false;
            }
            if (controleAccesCaisse(y.getCaisse(), true)) {
                if (selectBon.getStatutPaiement().equals(Constantes.ETAT_REGLE)) {
                    selectBon.setDatePayer(null);
                    selectBon.setStatutPaiement(Constantes.ETAT_ATTENTE);
                } else {
                    if (selectBon.getDatePayer() == null) {
                        getErrorMessage("Vous devez indiquer la date de paiement !");
                        return false;
                    }
                    selectBon.setStatutPaiement(Constantes.ETAT_REGLE);
                    //controle si le validateur de ce bon n'a pas exceder son quota de bon et son nombre de bo...
                    YvsUsers validBy = selectBon.getValiderBy();
                    if (validBy != null) {
                        calculTotalBonNonJustifieUser(validBy);
                        if (paramCompta.getNbMaxBp() > 0) {
                            if (paramCompta.getNbMaxBp() <= nbMaxNonJustifUser && !autoriser("compta_valid_bp_under_max", validBy)) {
                                getErrorMessage("Vous ne pouvez payer ce bon car il a été validé par " + validBy.getNomUsers(), "Ce dernier a atteint son quota de bons non justifié !");
                                return false;
                            }
                        }
                    } else {
                        getErrorMessage("Nous n'avons pas trouvé le validateur de ce bon");
                        return false;
                    }

                }
                selectBon.setDateUpdate(new Date());
                selectBon.setCaissier(currentUser.getUsers());
                dao.update(selectBon);
                if (bonProvisoire != null) {
                    bonProvisoire.setStatutPaiement(selectBon.getStatutPaiement().charAt(0));
                }
                int idx = documents.indexOf(selectBon);
                if (idx > -1) {
                    documents.set(idx, selectBon);
                }
                update("chmp_montant_bon_provisoire");
                if (msg) {
                    succes();
                }
            }
            return true;
        } else {
            if (msg) {
                getErrorMessage("Le document n'a pas encore été validé ");
            }
        }
        return false;
    }

    public void justifybon() {
        justifyBon(selectBon, true, true);
    }

    public void justifyBon(YvsComptaBonProvisoire y) {
        justifyBon(y, true);
    }

    public boolean justifyBon(YvsComptaBonProvisoire y, boolean msg) {
        return justifyBon(y, true, false);
    }

    public boolean justifyBon(YvsComptaBonProvisoire y, boolean msg, boolean force) {
        if (y != null) {
            selectBon = y;
            boolean succes = true;
            //le document divers doit être valide
            if (!y.canDelete()) {
                if (!selectBon.getStatutJustify().equals(Constantes.ETAT_JUSTIFIE)) {
                    if (!y.canJustify()) {
                        if (msg) {
                            getErrorMessage("Ce bon n'est rattaché à aucun justificatifs");
                        }
                        return false;
                    }
                    if (y.getCaisse() != null ? y.getCaisse().getId() < 1 : true) {
                        if (msg) {
                            getErrorMessage("Vous devez precisez la caisse");
                        }
                        return false;
                    }
                }
                selectBon.setJustifyBy(null);
                selectBon.setDateJustify(null);
                if (selectBon.getStatutJustify().equals(Constantes.ETAT_JUSTIFIE)) {
                    selectBon.setStatutJustify(Constantes.ETAT_INJUSTIFIE);
                } else {
                    if (!Constantes.ETAT_REGLE.equals(y.getStatutPaiement())) {
                        getErrorMessage("Le bon provisoire ne peut être justifier", "Il n'est pas encore payé!");
                        return false;
                    } else {
                        //Vérifie l'equilibre des montants
                        if (y.getMontant() >= y.getJustifier()) {
                            if (force && autoriser("compta_justif_bp")) {
                                selectBon.setStatutJustify(Constantes.ETAT_JUSTIFIE);
                                selectBon.setDateJustify(new Date());
                                selectBon.setJustifyBy(currentUser.getUsers());
                            } else {
                                getErrorMessage("Vous ne disposez pas des droits pour justifier ce bon provisoire");
                                return false;
                            }
                        } else {
                            selectBon.setStatutJustify(Constantes.ETAT_INJUSTIFIE);
                            getErrorMessage("Le montant des justificatifs n'équilibre pas le bon !");
                            succes = false;
                        }
                    }
                }
                selectBon.setAuthor(currentUser);
                selectBon.setDateUpdate(new Date());
                dao.update(selectBon);
                bonProvisoire.setStatutJustify((selectBon.getStatutJustify() != null) ? selectBon.getStatutJustify().charAt(0) : Constantes.STATUT_DOC_INJUSTIFIE);
                int idx = documents.indexOf(selectBon);
                if (idx > -1) {
                    documents.set(idx, selectBon);
                }
                update("chmp_montant_bon_provisoire");
                if (msg && succes) {
                    succes();
                }
                return true;
            } else {
                if (msg) {
                    getErrorMessage("Le document n'a pas encore été validé ");
                }
            }
        }
        return false;
    }

    public void verifyToJustify(YvsGrhMissions m) {
        if (m != null ? m.getId() > 0 : false) {
            if (m.getStatutMission().equals(Constantes.STATUT_DOC_VALIDE)) {
                setMontantTotalMission(m, new Mission());
                Double montant = (Double) dao.loadObjectByNameQueries("YvsComptaCaissePieceMission.findByMissPaye", new String[]{"mission", "statut"}, new Object[]{m, Constantes.STATUT_DOC_PAYER});
                double total = montant != null ? montant : 0;
                if (m.getTotalBon() <= total) {
                    m.setBonsProvisoire(dao.loadNameQueries("YvsComptaJustifBonMission.findByMission", new String[]{"mission"}, new Object[]{m}));
                    for (YvsComptaJustifBonMission y : m.getBonsProvisoire()) {
                        y.getPiece().setStatutJustify(Constantes.ETAT_JUSTIFIE);
                        y.getPiece().setDateUpdate(new Date());
                        y.getPiece().setAuthor(currentUser);
                        dao.update(y.getPiece());
                    }
                }
            }
        }
    }

    public void verifyToJustify(YvsComptaBonProvisoire y) {
        if (y != null ? y.getId() > 0 : false) {
            double total = 0;
            List<YvsComptaCaissePieceDivers> list = dao.loadNameQueries("YvsComptaJustificatifBon.findPieceBybon", new String[]{"bon"}, new Object[]{y});
            for (YvsComptaCaissePieceDivers c : list) {
                total += c.getMontant();
            }
            if (y.getMontant() <= total) {
                y.setStatutJustify(Constantes.ETAT_JUSTIFIE);
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                dao.update(y);
            }
        }
    }

    /**
     * ******************************************
     */
    private YvsWorkflowValidBonProvisoire giveCurrentStep() {
        ordonneEtapes(bonProvisoire.getEtapesValidations());
        for (YvsWorkflowValidBonProvisoire c : bonProvisoire.getEtapesValidations()) {
            if (!c.getEtapeValid()) {
                return c;
            }
        }
        return null;
    }

    private List<YvsWorkflowEtapeValidation> saveEtapesValidation(YvsComptaBonProvisoire doc) {
        if (doc.getTypeDoc() == null) {
            champ = new String[]{"titre", "societe"};
            val = new Object[]{Constantes.DOCUMENT_BON_DIVERS_CAISSE, currentAgence.getSociete()};
            return dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", champ, val);
        } else {
            champ = new String[]{"titre", "societe", "typeDoc"};
            val = new Object[]{Constantes.DOCUMENT_BON_DIVERS_CAISSE, currentAgence.getSociete(), doc.getTypeDoc()};
            return dao.loadNameQueries("YvsWorkflowEtapeValidation.findByModelTypeActif", champ, val);
        }
    }

    private List<YvsWorkflowValidBonProvisoire> saveEtapesValidation(YvsComptaBonProvisoire doc, List<YvsWorkflowEtapeValidation> model) {
        //charge les étape de vailidation
        List<YvsWorkflowValidBonProvisoire> re = new ArrayList<>();
        if (!model.isEmpty()) {
            YvsWorkflowValidBonProvisoire vd;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    vd = new YvsWorkflowValidBonProvisoire();
                    vd.setDateSave(new Date());
                    vd.setDateUpdate(new Date());
                    vd.setAuthor(currentUser);
                    vd.setEtape(et);
                    vd.setEtapeValid(false);
                    vd.setDocCaisse(doc);
                    vd.setOrdreEtape(et.getOrdreEtape());
                    vd = (YvsWorkflowValidBonProvisoire) dao.save1(vd);
                    re.add(vd);
                }
            }
        }
        doc.setStatut(Constantes.ETAT_EDITABLE);
        dao.update(doc);
        return ordonneEtapes(re);
    }

    private List<YvsWorkflowValidBonProvisoire> ordonneEtapes(List<YvsWorkflowValidBonProvisoire> l) {
        return YvsWorkflowValidBonProvisoire.ordonneEtapes(l);
    }

    public void validerOrder() {
        if (controleFiche(bonProvisoire)) {
            if (bonProvisoire.canEditable()) {
                if (autoriser("compta_valid_bp")) {
                    //Vérifie le montant des bon;s non justifié
                    calculTotalBonNonJustifieUser(currentUser.getUsers());
                    if (currentParam != null) {
                        if (currentParam.getPlafondBP() > 0) {
                            if (currentParam.getPlafondBP() < bonProvisoire.getMontant()) {
                                if (!autoriser("compta_valid_all_bp")) {
                                    getErrorMessage("Vous n'êtes pas autorisé à valider les bon au delà de votre plafond !", paramCompta.getPlafondBP() + "");
                                    openNotAcces();
                                    return;
                                }
                            }
                        }
                        if (currentParam.getNbMaxBp() > 0) {
                            if (currentParam.getNbMaxBp() <= nbMaxNonJustifUser) {
                                if (!autoriser("compta_valid_bp_under_max")) {
                                    getErrorMessage("Vous avez atteint votre max de bons non justifiés !", "veuillez tout d'abord justifier vos précédent bons");
                                    openNotAcces();
                                    return;
                                }
                            }
                        }
                        YvsComptaBonProvisoire dd = (YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findById", new String[]{"id"}, new Object[]{bonProvisoire.getId()});
                        if (dd != null) {
                            dd.setDateValider(new Date());
                            dd.setValiderBy(currentUser.getUsers());
                            dd.setStatut(Constantes.ETAT_VALIDE);
                            dao.update(dd);
                            bonProvisoire.setStatut(Constantes.STATUT_DOC_VALIDE);
                            if (selectBon != null && selectBon.getId().equals(bonProvisoire.getId())) {
                                selectBon.setStatut(Constantes.ETAT_VALIDE);
                            }
                            if (documents.contains(dd)) {
                                documents.set(documents.indexOf(dd), dd);
                            }
                            succes();
                        } else {
                            openNotAcces();
                        }
                    } else {
                        getErrorMessage("Aucun paramètre de base n'a été trouvé !");
                    }
                }
            }
        }
    }

    public void validEtapeOrdre(YvsWorkflowValidBonProvisoire etape, boolean lastEtape) {
        validEtapeOrdre(etape, true, true);
    }

    public void validEtapeOrdre(YvsWorkflowValidBonProvisoire etape, boolean controle, boolean msg) {
        //vérifier que la personne qui valide l'étape a le droit 
        if (!asDroitValideEtape(etape.getEtape()) && controle) {
            openNotAcces();
        } else {
            //contrôle la cohérence des dates
            YvsComptaBonProvisoire currentDoc = (YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findById", new String[]{"id"}, new Object[]{bonProvisoire.getId()});
            int idx = bonProvisoire.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                etape.setAuthor(currentUser);
                etape.setDateUpdate(new Date());
                etape.setEtapeValid(true);
                etape.setEtapeActive(false);
                etape.setMotif(null);
                if (bonProvisoire.getEtapesValidations().size() > (idx + 1)) {
                    bonProvisoire.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                }
                dao.update(etape);
                currentDoc.setStatut(Constantes.ETAT_ENCOURS);
                currentDoc.setAuthor(currentUser);
                currentDoc.setEtapeValide(currentDoc.getEtapeValide() + 1);
                dao.update(currentDoc);
                bonProvisoire.setStatut(Constantes.STATUT_DOC_ENCOUR);
                if (msg) {
                    getInfoMessage("Validation effectué avec succès !");
                }
            } else {
                if (msg) {
                    getErrorMessage("Impossible de continuer !");
                }
            }
            //cas de la validation de la dernière étapes
            if (etape.getEtapeSuivante() == null) {
                currentDoc.setStatut(Constantes.ETAT_VALIDE);
                currentDoc.setDateValider(new Date());
                currentDoc.setValiderBy(currentUser.getUsers());
                currentDoc.setOrdonnateur(currentUser.getUsers());
                currentDoc.setEtapeValide(currentDoc.getEtapeTotal());
                etape.setDateUpdate(new Date());
                dao.update(currentDoc);
                bonProvisoire.setStatut(Constantes.STATUT_DOC_VALIDE);
            }
            bonProvisoire.setEtapeValide(currentDoc.getEtapeValide());
            if (documents.contains(currentDoc)) {
                int idx_ = documents.indexOf(currentDoc);
                documents.get(idx_).setEtapeValide(currentDoc.getEtapeValide());
                documents.get(idx_).setStatut(currentDoc.getStatut());
                update("data_bon_provisoire");
            }
        }
    }

    public void motifEtapeFacture(String motifEtape, boolean lastEtape) {
        this.motifEtape = motifEtape;
        this.lastEtape = lastEtape;
    }

    public void annulEtapeOrdre(YvsWorkflowValidBonProvisoire etape, boolean lastEtape) {
        this.etape = etape;
        this.lastEtape = lastEtape;
        this.motifEtape = null;
    }

    public boolean annulEtapeOrdre() {
        return annulEtapeOrdre(selectBon, bonProvisoire, currentUser, etape, lastEtape, motifEtape);
    }

    public boolean annulEtapeOrdre(YvsComptaBonProvisoire current, BonProvisoire divers, YvsUsersAgence users, YvsWorkflowValidBonProvisoire etape, boolean lastEtape, String motif) {
        if (!asDroitValideEtape(etape.getEtape(), users.getUsers())) {
            openNotAcces();
        } else {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                getErrorMessage("Vous devez précisez le motif");
                return false;
            }
            if (current != null ? (current.getId() != null ? current.getId() < 1 : true) : true) {
                current = (YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findById", new String[]{"id"}, new Object[]{divers.getId()});
            }
            if (divers != null ? divers.getId() < 1 : true) {
                divers = UtilCompta.buildBeanBonProvisoire(current);
            }
            int idx = divers.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                //cas de la validation de la dernière étapes
                if (etape.getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "facture"};
                    val = new Object[]{etape.getEtapeSuivante().getEtape(), current};
                    YvsWorkflowValidBonProvisoire y = (YvsWorkflowValidBonProvisoire) dao.loadOneByNameQueries("YvsWorkflowValidBonProvisoire.findByEtapeFacture", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        getErrorMessage("Vous devez au préalable annuler l'étape suivante");
                        return false;
                    }
                }
                etape.setAuthor(users);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                etape.setMotif(motif);
                dao.update(etape);

                current.setEtapeValide(current.getEtapeValide() - 1);
                current.setStatut(current.getEtapeValide() < 1 ? Constantes.ETAT_EDITABLE : Constantes.ETAT_ENCOURS);
                dao.update(current);

                divers.setStatut(current.getStatut().charAt(0));
                divers.setEtapeValide(current.getEtapeValide());
                if (documents != null ? documents.contains(current) : false) {
                    int idx_ = documents.indexOf(current);
                    documents.get(idx_).setEtapeValide(current.getEtapeValide());
                    documents.get(idx_).setStatut(current.getStatut());
                    update("data_bon_provisoire");
                }
                getInfoMessage("Annulation effectué avec succès !");
                return true;
            } else {
                getErrorMessage("Impossible de continuer !");
            }
        }
        return false;
    }

    public void confirmCancelDocDiver(boolean force, boolean suspend) {
        if (bonProvisoire.getId() > 0) {
            int i = 0;
            boolean update = force;
            if (!force) {
                for (YvsWorkflowValidBonProvisoire vm : bonProvisoire.getEtapesValidations()) {
                    //si on trouve une étape non valide (on ne peut annuler un ordre de docAchat complètement valide)
                    if (!vm.getEtapeValid()) {
                        update = true;
                    } else {
                        //ais-je un droit de validation pour cet étape?
                        if (!asDroitValideEtape(vm.getEtape().getAutorisations(), currentUser.getUsers().getNiveauAcces())) {
                            getErrorMessage("Vous ne pouvez annuler cette facture ! Elle requière un niveau suppérieur");
                            return;
                        }
                    }
                }
            }
            if (update) {
                for (YvsWorkflowValidBonProvisoire vm : bonProvisoire.getEtapesValidations()) {
                    vm.setEtapeActive(false);
                    if (i == 0) {
                        vm.setEtapeActive(true);
                    }
                    vm.setAuthor(currentUser);
                    vm.setEtapeValid(false);
                    dao.update(vm);
                    i++;
                }
            } else if (!bonProvisoire.getEtapesValidations().isEmpty()) {
                openDialog("dlgConfirmCancelForcer");
                return;
            }
            //le document ne doit contenir aucune pièce déjà payé
            if (bonProvisoire.getStatutPaiement() == Constantes.STATUT_DOC_PAYER) {
                getErrorMessage("Impossible de modifier cette pièce. elle contient des règlements déjà payé !");
                return;
            }
            YvsComptaBonProvisoire dd = (YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findById", new String[]{"id"}, new Object[]{bonProvisoire.getId()});
            if (dd != null) {
                dd.setDateValider(null);
                dd.setValiderBy(null);
                dd.setStatut(suspend ? Constantes.ETAT_ANNULE : Constantes.ETAT_EDITABLE);
                dd.setDateUpdate(new Date());
                dd.setEtapeValide(0);
                bonProvisoire.setEtapeValide(0);
                bonProvisoire.setStatut(dd.getStatut().charAt(0));
                dao.update(dd);
                if (documents.contains(dd)) {
                    documents.set(documents.indexOf(dd), dd);
                }
            }
        }
    }

    public void print(YvsComptaBonProvisoire y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + "/icones/" + (Constantes.ETAT_REGLE.equals(y.getStatutPaiement()) ? "solde.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(y.getMontant()));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                executeReport("pc_divers", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedDocDivers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void onSelectBonMission(YvsGrhMissions y) {
        ManagedMission w = (ManagedMission) giveManagedBean(ManagedMission.class);
        if (w != null) {
            bonProvisoire.setMission(w.setMontantTotalMission(y));
        }
        if (bonProvisoire.getId() < 1) {
            if (bonProvisoire.getTiers() != null ? bonProvisoire.getTiers().getId() < 1 : true) {
                bonProvisoire.setTiers(UtilTiers.buildBeanTiers(bonProvisoire.getMission().getEmploye().getCompteTiers()));
            }
            if (bonProvisoire.getMontant() <= 0) {
                bonProvisoire.setMontant(bonProvisoire.getMission().getTotalReste());
            }
            bonProvisoire.setBeneficiaire(bonProvisoire.getMission().getEmploye().getCompteTiers().getNom_prenom());
            bonProvisoire.setNumeroExterne(bonProvisoire.getMission().getNumeroMission());
            bonProvisoire.setDescription("BON PROVISOIRE POUR FRAIS DE MISSION N°" + bonProvisoire.getMission().getNumeroMission());
            update("form_bon_provisoire");
        }
        createPiece = true;
        if (y.getReglements() != null ? !y.getReglements().isEmpty() : false) {
            openDialog("dlgBonPieceMission");
            update("data_piece_mission_bon");
        }
    }

    private void onSelectBonAchat(YvsComDocAchats y) {
        setMontantTotalDoc(y);
        bonProvisoire.setAchat(y);
        if (bonProvisoire.getId() < 1) {
            if (bonProvisoire.getTiers() != null ? bonProvisoire.getTiers().getId() < 1 : true) {
                bonProvisoire.setTiers(UtilTiers.buildBeanTiers(bonProvisoire.getAchat().getFournisseur().getTiers()));
            }
            if (bonProvisoire.getMontant() <= 0) {
                bonProvisoire.setMontant(bonProvisoire.getAchat().getMontantResteAPlanifier());
            }
            bonProvisoire.setBeneficiaire(bonProvisoire.getAchat().getFournisseur().getTiers().getNom_prenom());
            bonProvisoire.setNumeroExterne(bonProvisoire.getAchat().getNumDoc());
            bonProvisoire.setDescription("BON PROVISOIRE POUR FACTURE N°" + bonProvisoire.getAchat().getNumDoc());
            update("form_bon_provisoire");
        }
        createPiece = true;
        if (y.getReglements() != null ? !y.getReglements().isEmpty() : false) {
            openDialog("dlgBonPieceAchat");
            update("data_piece_achat_bon");
        }
    }

    private void onSelectBonDivers(YvsComptaCaisseDocDivers y) {
        bonProvisoire.setDivers(y);
        if (bonProvisoire.getId() < 1) {
            String nom_prenom = "";
            if (bonProvisoire.getTiers() != null ? bonProvisoire.getTiers().getId() < 1 : true) {
                if (y.getTableTiers().equals(Constantes.BASE_TIERS_TIERS)) {
                    String code = dao.nameTiers(y.getIdTiers(), y.getTableTiers(), "R");
                    nom_prenom = dao.nameTiers(y.getIdTiers(), y.getTableTiers(), "N");
                    bonProvisoire.setTiers(new Tiers(y.getIdTiers(), nom_prenom));
                }
            }
            if (bonProvisoire.getMontant() <= 0) {
                bonProvisoire.setMontant(y.getMontantTotal() - y.getTotalPlanifie());
            }
            bonProvisoire.setBeneficiaire(nom_prenom);
            bonProvisoire.setNumeroExterne(bonProvisoire.getDivers().getNumPiece());
            bonProvisoire.setDescription("BON PROVISOIRE POUR OPERATION DIVERSE N°" + bonProvisoire.getDivers().getNumPiece());
            update("form_bon_provisoire");
        }
        createPiece = true;
        if (y.getReglements() != null ? !y.getReglements().isEmpty() : false) {
            openDialog("dlgBonPieceDivers");
            update("data_piece_divers_bon");
        }
    }

    public void searchMission() {
        ManagedMission w = (ManagedMission) giveManagedBean(ManagedMission.class);
        if (w != null) {
            w.clearParams(false);
            w.setIncludeDocCloture(true);
            ParametreRequete p = new ParametreRequete("y.numeroMission", "reference", null, "LIKE", "AND");
            if (bonProvisoire.getNumeroExterne() != null ? bonProvisoire.getNumeroExterne().trim().length() > 0 : false) {
                p = new ParametreRequete(null, "reference", bonProvisoire.getNumeroExterne().toUpperCase(), "LIKE", "AND");
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.referenceMission)", "reference", bonProvisoire.getNumeroExterne().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.numeroMission)", "reference", bonProvisoire.getNumeroExterne().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.matricule)", "reference", bonProvisoire.getNumeroExterne().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.nom)", "reference", bonProvisoire.getNumeroExterne().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.prenom)", "reference", bonProvisoire.getNumeroExterne().toUpperCase() + "%", "LIKE", "OR"));
            }
            w.getPaginator().addParam(p);
            w.setInitForm(true);
            w.getPaginator().getParams().remove(new ParametreRequete("etape"));
            w.loadAllMission(true);
            bonProvisoire.setMission(new YvsGrhMissions(true));
            if (w.getListMission() != null ? !w.getListMission().isEmpty() : false) {
                bonProvisoire.getMission().setError(false);
                if (w.getListMission().size() == 1) {
                    bonProvisoire.setMission(w.getListMission().get(0));
                    onSelectBonMission(bonProvisoire.getMission());
                } else {
                    select = false;
                    openDialog("dlgBonMission");
                    update("data_mission_bon");
                }
            }
        }
    }

    public void searchAchat() {
        ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
        if (w != null) {
            w.searchFacture(bonProvisoire.getNumeroExterne(), null, false);
            bonProvisoire.setAchat(new YvsComDocAchats(true));
            if (w.getDocuments() != null ? !w.getDocuments().isEmpty() : false) {
                bonProvisoire.getAchat().setError(false);
                if (w.getDocuments().size() == 1) {
                    bonProvisoire.setAchat(w.getDocuments().get(0));
                    onSelectBonAchat(bonProvisoire.getAchat());
                } else {
                    select = false;
                    openDialog("dlgBonAchat");
                    update("data_achat_bon");
                }
            }
        }
    }

    public void searchDivers() {
        ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
        if (w != null) {
            w.searchByNum(bonProvisoire.getNumeroExterne());
            bonProvisoire.setDivers(new YvsComptaCaisseDocDivers(true));
            if (w.getDocuments() != null ? !w.getDocuments().isEmpty() : false) {
                bonProvisoire.getDivers().setError(false);
                if (w.getDocuments().size() == 1) {
                    bonProvisoire.setDivers(w.getDocuments().get(0));
                    onSelectBonDivers(bonProvisoire.getDivers());
                } else {
                    select = false;
                    openDialog("dlgBonDivers");
                    update("data_divers_bon");
                }
            }
        }
    }

    public List<Object[]> loadDatasBonProvisoire(long agence, Date dateDebut, Date dateFin, String groupBy) {
        List<Object[]> result = new ArrayList<>();
        debitTiers = 0;
        creditTiers = 0;
        soldeTiers = 0;
        try {
            if (Util.asString(groupBy) ? groupBy.equals("F") : true) {
                if (agence > 0) {
                    champ = new String[]{"agence", "dateDebut", "dateFin"};
                    val = new Object[]{new YvsAgences(agence), dateDebut, dateFin};
                    nameQueri = "YvsComptaBonProvisoire.findBeneficiaireByNotJustiferDatesAgence";
                } else {
                    champ = new String[]{"societe", "dateDebut", "dateFin"};
                    val = new Object[]{currentAgence.getSociete(), dateDebut, dateFin};
                    nameQueri = "YvsComptaBonProvisoire.findBeneficiaireByNotJustiferDates";
                }
                List<String> tiers = dao.loadNameQueries(nameQueri, champ, val);
                Object[] data;
                List<YvsComptaBonProvisoire> documents;
                double debit = 0;
                double credit = 0;
                double solde = 0;
                for (String f : tiers) {
                    debit = 0;
                    credit = 0;
                    solde = 0;
                    if (agence > 0) {
                        champ = new String[]{"beneficiaire", "agence", "dateDebut", "dateFin"};
                        val = new Object[]{f, new YvsAgences(agence), dateDebut, dateFin};
                        nameQueri = "YvsComptaBonProvisoire.findByNotJustiferDatesAgence";
                    } else {
                        champ = new String[]{"beneficiaire", "societe", "dateDebut", "dateFin"};
                        val = new Object[]{f, currentAgence.getSociete(), dateDebut, dateFin};
                        nameQueri = "YvsComptaBonProvisoire.findByNotJustiferDates";
                    }
                    documents = dao.loadNameQueries(nameQueri, champ, val);
                    for (YvsComptaBonProvisoire d : documents) {
                        double justifier = d.getJustifier();
                        debit += d.getMontant();
                        credit += justifier;
                        solde += d.getMontant() - justifier;
                    }
                    debitTiers += debit;
                    creditTiers += credit;
                    soldeTiers += solde;

                    data = new Object[5];
                    data[0] = f;
                    data[1] = documents;
                    data[2] = debit;
                    data[3] = credit;
                    data[4] = solde;
                    result.add(data);
                }
            } else {
                if (agence > 0) {
                    champ = new String[]{"agence", "dateDebut", "dateFin"};
                    val = new Object[]{new YvsAgences(agence), dateDebut, dateFin};
                    nameQueri = "YvsComptaBonProvisoire.findDateByNotJustiferDatesAgence";
                } else {
                    champ = new String[]{"societe", "dateDebut", "dateFin"};
                    val = new Object[]{currentAgence.getSociete(), dateDebut, dateFin};
                    nameQueri = "YvsComptaBonProvisoire.findDateByNotJustiferDates";
                }
                List<Date> dates = dao.loadNameQueries(nameQueri, champ, val);
                Object[] data;
                List<YvsComptaBonProvisoire> documents;
                double debit = 0;
                double credit = 0;
                double solde = 0;
                for (Date f : dates) {
                    debit = 0;
                    credit = 0;
                    solde = 0;
                    if (agence > 0) {
                        champ = new String[]{"dateBon", "agence"};
                        val = new Object[]{f, new YvsAgences(agence)};
                        nameQueri = "YvsComptaBonProvisoire.findByNotJustiferDateAgence";
                    } else {
                        champ = new String[]{"dateBon", "societe"};
                        val = new Object[]{f, currentAgence.getSociete()};
                        nameQueri = "YvsComptaBonProvisoire.findByNotJustiferDate";
                    }
                    documents = dao.loadNameQueries(nameQueri, champ, val);
                    for (YvsComptaBonProvisoire d : documents) {
                        double justifier = d.getJustifier();
                        debit += d.getMontant();
                        credit += justifier;
                        solde += d.getMontant() - justifier;
                    }
                    debitTiers += debit;
                    creditTiers += credit;
                    soldeTiers += solde;

                    data = new Object[5];
                    data[0] = formatDate.format(f);
                    data[1] = documents;
                    data[2] = debit;
                    data[3] = credit;
                    data[4] = solde;
                    result.add(data);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedBonProvisoire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllOthers(true, true);
    }

    public void calculTotalBonNonJustifie() {
        if (currentAgence != null && currentUser != null) {
            Double d = (Double) dao.loadObjectByNameQueries("YvsComptaBonProvisoire.sumNotJustifieInAgence", new String[]{"statutPaiement", "agence"}, new Object[]{Constantes.ETAT_REGLE, currentAgence});
            Double dS = (Double) dao.loadObjectByNameQueries("YvsComptaBonProvisoire.sumNotJustifieInSociete", new String[]{"statutPaiement", "societe"}, new Object[]{Constantes.ETAT_REGLE, currentAgence.getSociete()});
            Long N = (Long) dao.loadObjectByNameQueries("YvsComptaBonProvisoire.countNotJustifieInAgence", new String[]{"statutPaiement", "agence"}, new Object[]{Constantes.ETAT_REGLE, currentAgence});
            Long N2 = (Long) dao.loadObjectByNameQueries("YvsComptaBonProvisoire.countNotJustifieInSociete", new String[]{"statutPaiement", "societe"}, new Object[]{Constantes.ETAT_REGLE, currentAgence.getSociete()});
            totalNonJustifie = (d != null) ? d : 0d;
            totalNonJustifieInscte = (dS != null) ? dS : 0d;
            nbBpNonJustifAgence = (N != null) ? N.intValue() : 0;
            nbMaxNonJustfScte = (N != null) ? N.intValue() : 0;
            totalNonJustifieByUser = calculTotalBonNonJustifieUser(currentUser.getUsers());
        }
    }

    public double calculTotalBonNonJustifieUser(YvsUsers user) {
        if (currentUser != null) {
            Double dU = (Double) dao.loadObjectByNameQueries("YvsComptaBonProvisoire.sumNotJustifieForUser", new String[]{"statutPaiement", "users"}, new Object[]{Constantes.ETAT_REGLE, user});
            totalNonJustifieByUser = (dU != null) ? dU : 0d;
            Long N = (Long) dao.loadObjectByNameQueries("YvsComptaBonProvisoire.countNotJustifieForUser", new String[]{"statutPaiement", "users"}, new Object[]{Constantes.ETAT_REGLE, user});
            nbMaxNonJustifUser = (N != null) ? N.intValue() : 0;
        }
        return totalNonJustifieByUser;
    }

    public List<YvsComptaBonProvisoire> loadBonProvisoireNonJustifies(Date dateDebut, Date dateFin, int limit) {
        champ = new String[]{"agence", "dateDebut", "dateFin"};
        val = new Object[]{currentAgence, dateDebut, dateFin};
        nameQueri = "YvsComptaBonProvisoire.findNotJustiferByDatesAgence";
        if (limit > 0) {
            return dao.loadNameQueries(nameQueri, champ, val, 0, limit);
        } else {
            return dao.loadNameQueries(nameQueri, champ, val);
        }
    }

}

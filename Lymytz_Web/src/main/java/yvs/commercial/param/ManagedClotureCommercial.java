/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.UtilCompta;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.UtilCom;
import yvs.commercial.vente.EcartVenteOrStock;
import yvs.commercial.vente.EnteteDocVente;
import yvs.comptabilite.caisse.ManagedVirement;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.dao.Options;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.commercial.ServiceClotureVente;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEcartEnteteVente;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.vente.YvsComptaNotifVersementVente;
import yvs.entity.users.YvsUsers;
import yvs.users.Users;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedClotureCommercial extends Managed<YvsComEnteteDocVente, YvsComEnteteDocVente> implements Serializable {

    private long point, vendeur;
    private double cas, solde, ecart;
    private int totalWait, totalCours, totalRegle, totalLivre;
    private Boolean cloturer;
    private Date dateDebut = new Date(), dateFin = new Date();

    private EcartVenteOrStock ecarte = new EcartVenteOrStock();
    private YvsComEcartEnteteVente selectEcart = new YvsComEcartEnteteVente();
    private PieceTresorerie virement = new PieceTresorerie();

    private List<YvsBaseCaisse> caisses, caisseCible;
    private List<YvsComEnteteDocVente> documents;
    private YvsComEnteteDocVente header = new YvsComEnteteDocVente();
    private YvsComEnteteDocVente selectHeader = new YvsComEnteteDocVente();
    private YvsComDocVentes facture = new YvsComDocVentes();
    private List<YvsBasePointVente> points;
    private List<YvsUsers> vendeurs;
    public PaginatorResult<YvsBaseCaisse> paginatorCaisse = new PaginatorResult<>();

    private List<YvsComComerciale> commerciaux;

    ServiceClotureVente service;

    public ManagedClotureCommercial() {
        caisses = new ArrayList<>();
        caisseCible = new ArrayList<>();
        points = new ArrayList<>();
        vendeurs = new ArrayList<>();
        commerciaux = new ArrayList<>();
        documents = new ArrayList<>();
    }

    public PieceTresorerie getVirement() {
        return virement;
    }

    public void setVirement(PieceTresorerie virement) {
        this.virement = virement;
    }

    public List<YvsBaseCaisse> getCaisseCible() {
        return caisseCible;
    }

    public void setCaisseCible(List<YvsBaseCaisse> caisseCible) {
        this.caisseCible = caisseCible;
    }

    public EcartVenteOrStock getEcarte() {
        return ecarte;
    }

    public void setEcarte(EcartVenteOrStock ecarte) {
        this.ecarte = ecarte;
    }

    public YvsComEcartEnteteVente getSelectEcart() {
        return selectEcart;
    }

    public void setSelectEcart(YvsComEcartEnteteVente selectEcart) {
        this.selectEcart = selectEcart;
    }

    public YvsComEnteteDocVente getSelectHeader() {
        return selectHeader;
    }

    public void setSelectHeader(YvsComEnteteDocVente selectHeader) {
        this.selectHeader = selectHeader;
    }

    public Boolean getCloturer() {
        return cloturer;
    }

    public void setCloturer(Boolean cloturer) {
        this.cloturer = cloturer;
    }

    public YvsComEnteteDocVente getHeader() {
        return header;
    }

    public void setHeader(YvsComEnteteDocVente header) {
        this.header = header;
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

    public double getEcart() {
        return ecart;
    }

    public void setEcart(double ecart) {
        this.ecart = ecart;
    }

    public double getCas() {
        return cas;
    }

    public void setCas(double cas) {
        this.cas = cas;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public List<YvsBaseCaisse> getCaisses() {
        return caisses;
    }

    public void setCaisses(List<YvsBaseCaisse> caisses) {
        this.caisses = caisses;
    }

    public int getTotalWait() {
        return totalWait;
    }

    public void setTotalWait(int totalWait) {
        this.totalWait = totalWait;
    }

    public int getTotalCours() {
        return totalCours;
    }

    public void setTotalCours(int totalCours) {
        this.totalCours = totalCours;
    }

    public int getTotalRegle() {
        return totalRegle;
    }

    public void setTotalRegle(int totalRegle) {
        this.totalRegle = totalRegle;
    }

    public int getTotalLivre() {
        return totalLivre;
    }

    public void setTotalLivre(int totalLivre) {
        this.totalLivre = totalLivre;
    }

    public List<YvsComEnteteDocVente> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComEnteteDocVente> documents) {
        this.documents = documents;
    }

    public YvsComDocVentes getFacture() {
        return facture;
    }

    public void setFacture(YvsComDocVentes facture) {
        this.facture = facture;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getVendeur() {
        return vendeur;
    }

    public void setVendeur(long vendeur) {
        this.vendeur = vendeur;
    }

    public List<YvsBasePointVente> getPoints() {
        return points;
    }

    public void setPoints(List<YvsBasePointVente> points) {
        this.points = points;
    }

    public List<YvsUsers> getVendeurs() {
        return vendeurs;
    }

    public void setVendeurs(List<YvsUsers> vendeurs) {
        this.vendeurs = vendeurs;
    }

    public List<YvsComComerciale> getCommerciaux() {
        return commerciaux;
    }

    public void setCommerciaux(List<YvsComComerciale> commerciaux) {
        this.commerciaux = commerciaux;
    }

    @Override
    public void loadAll() {
        points = dao.loadNameQueries("YvsBasePointVente.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        vendeurs = dao.loadNameQueries("YvsComEnteteDocVente.findVendeur", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        service = new ServiceClotureVente(dao, currentNiveau, currentUser, currentAgence.getSociete());
    }

    @Override
    public boolean controleFiche(YvsComEnteteDocVente bean) {
        return controleFiche(bean, true);
    }

    public boolean controleFiche(YvsComEnteteDocVente bean, boolean msg) {
        if (bean == null) {
            return false;
        }
        if (!bean.getCloturer() && !bean.getEtat().equals(Constantes.ETAT_VALIDE)) {
            if (msg) {
                getErrorMessage("Ce journal de vente n'est pas validée");
            }
            return false;
        }
        return true;
    }

    public boolean controleFiche(YvsComDocVentes bean, boolean msg) {
        if (bean == null) {
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_VALIDE)) {
            if (msg) {
                getErrorMessage("Cette facture n'est pas validée");
            }
            return false;
        }
//        if (!bean.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
//            if (msg) {
//                getErrorMessage("Cette facture n'est pas livrée");
//            }
//            return false;
//        }
//        if (!bean.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
//            if (msg) {
//                getErrorMessage("Cette facture n'est pas reglée");
//            }
//            return false;
//        }
        return true;
    }

    public void loadData() {
        loadData(true, true);
        String query = "SELECT SUM(public.get_ca_entete_vente(h.id)) FROM yvs_com_entete_doc_vente h WHERE h.id IN (SELECT DISTINCT y.entete_doc FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id INNER JOIN yvs_agences a ON e.agence = a.id INNER JOIN yvs_com_creneau_horaire_users u ON e.creneau = u.id INNER JOIN yvs_com_creneau_point c ON u.creneau_point = c.id WHERE a.societe = ? AND y.type_doc = ? AND e.date_entete BETWEEN ? AND ?";
        int size = 4;
        if (point > 0) {
            size += 1;
            query += " AND c.point = ?";
        }
        if (vendeur > 0) {
            size += 1;
            query += " AND u.users = ?";
        }
        query += ")";
        Options[] params = new Options[size];
        params[0] = new Options(currentAgence.getSociete().getId(), 1);
        params[1] = new Options(Constantes.TYPE_FV, 2);
        params[2] = new Options(dateDebut, 3);
        params[3] = new Options(dateFin, 4);
        if (point > 0) {
            params[4] = new Options(point, 5);
        }
        if (vendeur > 0) {
            params[size - 1] = new Options(vendeur, size);
        }
        Object ca = dao.loadObjectBySqlQuery(query, params);
        this.cas = ca != null ? Double.valueOf(ca.toString()) : 0;

        paginator.addParam(new ParametreRequete("y.statut", "statut", Constantes.ETAT_EDITABLE, "=", "AND"));
        String nameQuery = paginator.buildDynamicQuery(paginator.getParams(), "SELECT COUNT(y) FROM YvsComDocVentes y WHERE");
        Long value = (Long) dao.loadObjectByEntity(nameQuery, paginator.getChamp(), paginator.getVal());
        totalWait = value != null ? value.intValue() : 0;

        paginator.addParam(new ParametreRequete("y.statut", "statut", Constantes.ETAT_ENCOURS, "=", "AND"));
        nameQuery = paginator.buildDynamicQuery(paginator.getParams(), "SELECT COUNT(y) FROM YvsComDocVentes y WHERE");
        value = (Long) dao.loadObjectByEntity(nameQuery, paginator.getChamp(), paginator.getVal());
        totalCours = value != null ? value.intValue() : 0;

        paginator.addParam(new ParametreRequete("y.statutRegle", "statut", Constantes.ETAT_REGLE, "!=", "AND"));
        nameQuery = paginator.buildDynamicQuery(paginator.getParams(), "SELECT COUNT(y) FROM YvsComDocVentes y WHERE");
        value = (Long) dao.loadObjectByEntity(nameQuery, paginator.getChamp(), paginator.getVal());
        totalRegle = value != null ? value.intValue() : 0;

        paginator.addParam(new ParametreRequete("y.statutLivre", "statut", Constantes.ETAT_LIVRE, "!=", "AND"));
        nameQuery = paginator.buildDynamicQuery(paginator.getParams(), "SELECT COUNT(y) FROM YvsComDocVentes y WHERE");
        value = (Long) dao.loadObjectByEntity(nameQuery, paginator.getChamp(), paginator.getVal());
        totalLivre = value != null ? value.intValue() : 0;

        paginator.addParam(new ParametreRequete("y.statutLivre", "statut", null, "!=", "AND"));

        paginatorCaisse.addParam(new ParametreRequete("y.vente.enteteDoc.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        paginatorCaisse.addParam(new ParametreRequete("y.vente.enteteDoc.dateEntete", "dateDoc", dateFin, "<=", "AND"));
        caisses = paginatorCaisse.executeDynamicQuery("DISTINCT(y.caisse)", "DISTINCT(y.caisse)", "YvsComptaCaissePieceVente y", "y.caisse.intitule", true, true, 0, dao);
        for (YvsBaseCaisse c : caisses) {
            c.setSolde(dao.getTotalCaisse(c.getId(), 0, null, Constantes.MODE_PAIEMENT_ESPECE, Constantes.STATUT_DOC_PAYER, dateFin));
            paginatorCaisse.addParam(new ParametreRequete("y.datePaiement", "dateDoc", dateFin, "<=", "AND"));
            paginatorCaisse.addParam(new ParametreRequete("y.caisse", "caisse", c, "=", "AND"));
            paginatorCaisse.addParam(new ParametreRequete("y.statutPiece", "statut", Constantes.STATUT_DOC_PAYER, "=", "AND"));
            paginatorCaisse.addParam(new ParametreRequete("COALESCE(y.mouvement, 'R')", "mouvement", 'R', "=", "AND"));
            nameQuery = paginatorCaisse.buildDynamicQuery(paginatorCaisse.getParams(), "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE");
            Double recette = (Double) dao.loadObjectByEntity(nameQuery, paginatorCaisse.getChamp(), paginatorCaisse.getVal());
            paginatorCaisse.addParam(new ParametreRequete("COALESCE(y.mouvement, 'R')", "mouvement", 'D', "=", "AND"));
            nameQuery = paginatorCaisse.buildDynamicQuery(paginatorCaisse.getParams(), "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE");
            Double depense = (Double) dao.loadObjectByEntity(nameQuery, paginatorCaisse.getChamp(), paginatorCaisse.getVal());
            c.setRecette(recette != null ? recette : 0);
            c.setDepense(depense != null ? depense : 0);
            c.setVersement(c.getRecette() - c.getDepense());

            Double virement = (Double) dao.loadObjectByNameQueries("YvsComptaCaissePieceVirement.findSumBySourceDates", new String[]{"caisse", "statut", "dateDebut", "dateFin"}, new Object[]{c, Constantes.STATUT_DOC_PAYER, dateDebut, dateFin});
            c.setVirement(virement != null ? virement : 0);
            c.setEcart(c.getVersement() - c.getVirement());
        }
        paginatorCaisse.addParam(new ParametreRequete("y.caisse", "caisse", null, "=", "AND"));
        paginatorCaisse.addParam(new ParametreRequete("y.statutPiece", "statut", null, "=", "AND"));
        paginatorCaisse.addParam(new ParametreRequete("y.mouvement", "mouvement", null, "=", "AND"));
    }

    public void loadData(boolean avance, boolean init) {
        cas = 0;
        paginator.addParam(new ParametreRequete("y.enteteDoc.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        paginator.addParam(new ParametreRequete("y.typeDoc", "typeDoc", Constantes.TYPE_FV, "=", "AND"));
        paginator.addParam(new ParametreRequete("y.enteteDoc.dateEntete", "dateDoc", dateDebut, dateFin, "BETWEEN", "AND"));
        documents = paginator.executeDynamicQuery("DISTINCT(y.enteteDoc)", "DISTINCT(y.enteteDoc)", "YvsComDocVentes y", "y.enteteDoc.dateEntete", avance, init, paginator.getRows(), dao);
        if (!documents.contains(header)) {
            resetFiche();
        }
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadData(true, true);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadData(true, true);
    }

    @Override
    public boolean saveNew() {
        try {

        } catch (Exception ex) {
            getErrorMessage("Actoin impossible");
            getException("saveNew", ex);
        }
        return false;
    }

    public boolean saveNew(YvsComEnteteDocVente y, Date date, long caisse, double montant) {
        try {
            return service.saveNew(y, date, caisse, montant).isResult();
        } catch (Exception ex) {
            getErrorMessage("Actoin impossible");
            getException("saveNew", ex);
        }
        return false;
    }

    public void showInfos(YvsComEnteteDocVente y) {
        selectHeader = loadData(y);
        selectHeader.setNew_(false);
        openDialog("dlgConfirmClotureHeader");
        update("form-cloture_header_vente");
    }

    public void loadVirement(YvsComEnteteDocVente y) {
        selectHeader = y;
        selectHeader.setVirements(dao.loadNameQueries("YvsComptaNotifVersementVente.findVirementByHeader", new String[]{"enteteDoc"}, new Object[]{y}));
        selectHeader.setCaisses(dao.loadNameQueries("YvsComptaCaissePieceVente.findCaisseByEntete", new String[]{"enteteDoc"}, new Object[]{y}));
        if (selectHeader.getCaisses() != null ? !selectHeader.getCaisses().isEmpty() : false) {
            YvsBaseCaisse caisse = selectHeader.getCaisses().get(0);
            onSelectSource(caisse);
        }
        virement.setDatePaiementPrevu(selectHeader.getDateEntete());
        openDialog("dlgListVirement");
        update("form-virement_header_vente");
    }

    public void chooseSource(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            // trouve les caisses parent d'une caisse données
            long id = (long) ev.getNewValue();
            caisseCible.clear();
            if (id > 0) {
                int idx = selectHeader.getCaisses().indexOf(new YvsBaseCaisse((long) ev.getNewValue()));
                if (idx >= 0) {
                    YvsBaseCaisse y = selectHeader.getCaisses().get(idx);
                    onSelectSource(y);
                }
            }
        }
    }

    public void onSelectSource(YvsBaseCaisse y) {
        if (y != null ? y.getId() > 0 : false) {
            virement.setCaisse(UtilCompta.buildBeanCaisse(y));
            caisseCible = dao.loadNameQueries("YvsBaseLiaisonCaisse.findCaisseCibleByCaisse", new String[]{"caisseSource"}, new Object[]{y});
        }
    }

    public void chooseCible(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                int idx = caisseCible.indexOf(new YvsBaseCaisse((long) ev.getNewValue()));
                if (idx >= 0) {
                    virement.setOtherCaisse(UtilCompta.buildBeanCaisse(caisseCible.get(idx)));
                }
            }
        }
    }

    public void chooseModeReglement() {
        if (virement.getMode() != null ? virement.getMode().getId() > 0 : false) {
            ManagedModeReglement w = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
            if (w != null) {
                int idx = w.getModes().indexOf(new YvsBaseModeReglement((long)virement.getMode().getId()));
                if (idx > -1) {
                    YvsBaseModeReglement y = w.getModes().get(idx);
                    virement.setMode(UtilCompta.buildBeanModeReglement(y));
                }
            }
        }
    }

    public void createNotif(YvsComptaCaissePieceVirement y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (y.getVersement() != null ? y.getVersement().getId() > 0 : false) {
                    if (y.getVersement().getEnteteDoc().equals(selectHeader)) {
                        getErrorMessage("Vous avez deja rattaché ce virement a ce journal");
                        return;
                    }
                }
                YvsComptaNotifVersementVente v = new YvsComptaNotifVersementVente(selectHeader, y, currentUser);
                v = (YvsComptaNotifVersementVente) dao.save1(v);
                y.setVersement(v);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("createNotif", ex);
        }
    }

    public void createVirement() {
        ManagedVirement w = (ManagedVirement) giveManagedBean(ManagedVirement.class);
        if (w != null) {
            virement.setEnteteDoc(new EnteteDocVente(selectHeader.getId()));
            virement.setDatePaiement(selectHeader.getDateEntete());
            virement.setDatePiece(selectHeader.getDateEntete());
            virement.setCaissier(new Users(selectHeader.getCreneau().getUsers().getId(), selectHeader.getCreneau().getUsers().getCodeUsers(), selectHeader.getCreneau().getUsers().getNomUsers()));
            YvsComptaCaissePieceVirement y = w.saveNew(virement);
            if (y != null ? y.getId() > 0 : false) {
                selectHeader.getVirements().add(y);
                succes();

                virement = new PieceTresorerie();
                virement.setDatePaiementPrevu(selectHeader.getDateEntete());
            }
        }
    }

    public void actionOnHeader(YvsComEnteteDocVente y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (!controleFiche(y)) {
                    return;
                }
                if (!y.getCloturer()) {
                    cleanVente(y, false);
                    selectHeader = loadData(y);
                    selectHeader.setNew_(true);
                    openDialog("dlgConfirmClotureHeader");
                    update("form-cloture_header_vente");
                    return;
                }
                y.setCloturer(!y.getCloturer());
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                y.getEcarts().clear();
                dao.update(y);

                String query = "UPDATE yvs_com_doc_ventes SET cloturer = ? WHERE id IN (SELECT y.id FROM yvs_com_doc_ventes y WHERE y.entete_doc = ?)";
                Options[] params = new Options[]{new Options(y.getCloturer(), 1), new Options(y.getId(), 2)};
                dao.requeteLibre(query, params);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("actionOnFacture", ex);
        }
    }

    public void actionOnHeader(YvsComEnteteDocVente y, boolean force) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (!controleFiche(y)) {
                    return;
                }
                if (!y.getCloturer() && !force) {
                    nameQueriCount = "YvsComDocVentes.countFactureNonLivreOrNonPayeByHeader";
                    champ = new String[]{"statut", "statutLivre", "statutRegle", "typeDoc", "header"};
                    val = new Object[]{Constantes.ETAT_VALIDE, Constantes.ETAT_LIVRE, Constantes.ETAT_REGLE, Constantes.TYPE_FV, y};
                    Long count = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
                    if (count != null ? count > 0 : false) {
                        openDialog("dlgForceClotureHeader");
                        return;
                    }
                }
                y.setCloturer(!y.getCloturer());
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                y.getEcarts().clear();
                dao.update(y);

                String query = "UPDATE yvs_com_doc_ventes SET cloturer = ? WHERE id IN (SELECT y.id FROM yvs_com_doc_ventes y WHERE y.entete_doc = ?)";
                Options[] params = new Options[]{new Options(y.getCloturer(), 1), new Options(y.getId(), 2)};
                dao.requeteLibre(query, params);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("actionOnFacture", ex);
        }
    }

    public void actionOnFacture(YvsComDocVentes y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (!controleFiche(y, true)) {
                    return;
                }
                y.setCloturer(!y.getCloturer());
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                dao.update(y);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("actionOnFacture", ex);
        }
    }

    public void cleanVente(YvsComEnteteDocVente y, boolean msg) {
        if (y != null ? y.getId() > 0 : false) {
            String req = "delete from yvs_com_doc_ventes where entete_doc = ? and id not in (select doc_vente from yvs_com_contenu_doc_vente)";
            dao.requeteLibre(req, new yvs.dao.Options[]{new Options(y.getId(), 1)});
            if (msg) {
                header.setDocuments(dao.loadNameQueries("YvsComDocVentes.findByEnteteNotTypeDoc", new String[]{"entete", "typeDoc"}, new Object[]{y, Constantes.TYPE_BLV}));
                succes();
            }
        }
    }

    private YvsComEnteteDocVente loadData(YvsComEnteteDocVente y) {
        String query = "SELECT SUM(get_ca_vente(y.id)) FROM yvs_com_doc_ventes y WHERE y.type_doc= ? AND y.entete_doc = ? AND y.statut = ? AND (y.document_lie IS NULL OR (y.document_lie IS NOT NULL AND y.statut_regle IN ('R', 'P')))";
        Options[] params = new Options[]{new Options(Constantes.TYPE_FV, 1), new Options(y.getId(), 2), new Options(Constantes.ETAT_VALIDE, 3)};
        Double valeur = (Double) dao.loadObjectBySqlQuery(query, params);
        y.setTotalFact(valeur != null ? valeur : 0);
        if (y.getTotalFact() > 0) {
            List<Long> ids = dao.loadListBySqlQuery(query.replace("SUM(get_ca_vente(y.id))", "DISTINCT y.id"), params);
            nameQueri = "YvsComptaCaissePieceVente.findSumByVentesStatut";
            champ = new String[]{"ventes", "statut"};
            val = new Object[]{ids, Constantes.STATUT_DOC_PAYER};
            valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            y.setAvanceFact(valeur != null ? valeur : 0);
        }
        query = "SELECT SUM(get_ca_vente(y.id)) FROM yvs_com_doc_ventes y WHERE y.type_doc= ? AND y.entete_doc = ? AND y.statut = ? AND COALESCE((SELECT COUNT(d.id) FROM yvs_com_doc_ventes d WHERE d.document_lie = y.id AND d.statut_regle NOT IN ('R', 'P')), 0) < 1";
        params = new Options[]{new Options(Constantes.TYPE_BCV, 1), new Options(y.getId(), 2), new Options(Constantes.ETAT_VALIDE, 3)};
        valeur = (Double) dao.loadObjectBySqlQuery(query, params);
        y.setTotalCmde(valeur != null ? valeur : 0);

        nameQueri = "YvsComptaCaissePieceVente.findSumBCVRecuByCaissierDates";
        champ = new String[]{"caissier", "dateDebut", "dateFin"};
        val = new Object[]{y.getCreneau().getUsers(), y.getDateEntete(), y.getDateEntete()};
        valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        y.setAvanceCmde(valeur != null ? valeur : 0);

        y.setVersementAttendu(y.getTotalFact() + y.getAvanceCmde());

        nameQueri = "YvsComptaNotifVersementVente.findSumByHeaderStatut";
        champ = new String[]{"enteteDoc", "statut"};
        val = new Object[]{y, Constantes.STATUT_DOC_PAYER};
        valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        y.setVersementReel(valeur != null ? valeur : 0);

        nameQueri = "YvsComEcartEnteteVente.findSumByEntete";
        champ = new String[]{"enteteDoc"};
        val = new Object[]{y};
        valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        y.setEcartPlanifier(valeur != null ? valeur : 0);

        y.setEcart(y.getVersementAttendu() - y.getVersementReel() + y.getEcartPlanifier());
        y.setCaisses(dao.loadNameQueries("YvsComptaCaissePieceVente.findCaisseByEntete", new String[]{"enteteDoc"}, new Object[]{y}));
        if (y.getCaisses() != null ? !y.getCaisses().isEmpty() : false) {
            y.setCaisse(y.getCaisses().get(0).getId());
        }
        return y;
    }

    public void actualiseEcart() {
        if (selectHeader != null ? selectHeader.getId() > 0 : false) {
            String query = "SELECT action_in_header_vente_or_piece_virement(?)";
            Boolean result = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(selectHeader.getId(), 1)});
            if (result != null ? result : false) {
                nameQueri = "YvsComEcartEnteteVente.findSumByEntete";
                champ = new String[]{"enteteDoc"};
                val = new Object[]{selectHeader};
                Double valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                selectHeader.setEcartPlanifier(valeur != null ? valeur : 0);
                update("link-ecar_planifier_header_vente");
            }
        }
    }

    public void loadDataEcart() {
        if (selectHeader != null ? selectHeader.getId() > 0 : false) {
            if (selectHeader.getEcarts() != null ? !selectHeader.getEcarts().isEmpty() : false) {
                onSelectObjectEcart(selectHeader.getEcarts().get(0));
            }
        }
    }

    @Override
    public void onSelectObject(YvsComEnteteDocVente y) {
        header = loadData(y);
        header.setMontantTotal(dao.loadCaEnteteVente(header.getId()));
        header.setDocuments(dao.loadNameQueries("YvsComDocVentes.findByEnteteNotTypeDoc", new String[]{"entete", "typeDoc"}, new Object[]{y, Constantes.TYPE_BLV}));
    }

    public void onSelectObjectEcart(YvsComEcartEnteteVente y) {
        selectEcart = y;
        ecarte = UtilCom.buildBeanEcartVente(selectEcart);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsComEnteteDocVente) ev.getObject());
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewEcart(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObjectEcart((YvsComEcartEnteteVente) ev.getObject());
        }
    }

    public void unLoadOnViewEcart(UnselectEvent ev) {
        selectEcart = new YvsComEcartEnteteVente();
        ecarte = new EcartVenteOrStock();
    }

    @Override
    public void resetFiche() {
        header = new YvsComEnteteDocVente();
        facture = new YvsComDocVentes();
    }

    public boolean errorInHeader(YvsComEnteteDocVente y) {
        nameQueri = "YvsComDocVentes.countByEnteteStatut";
        champ = new String[]{"enteteDoc", "statut", "typeDoc"};
        val = new Object[]{y, Constantes.ETAT_EDITABLE, Constantes.TYPE_FV};
        Long value = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
        y.setTotalWait(value != null ? value : 0);

        nameQueri = "YvsComDocVentes.countByEnteteStatut";
        champ = new String[]{"enteteDoc", "statut", "typeDoc"};
        val = new Object[]{y, Constantes.ETAT_ENCOURS, Constantes.TYPE_FV};
        value = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
        y.setTotalCours(value != null ? value : 0);

        nameQueri = "YvsComDocVentes.countByEnteteNotStatutsLivre";
        champ = new String[]{"enteteDoc", "statut", "statutLivre", "typeDoc"};
        val = new Object[]{y, Constantes.ETAT_VALIDE, Constantes.ETAT_LIVRE, Constantes.TYPE_FV};
        value = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
        y.setTotalLivre(value != null ? value : 0);

        nameQueri = "YvsComDocVentes.countByEnteteNotStatutsRegle";
        champ = new String[]{"enteteDoc", "statut", "statutRegle", "typeDoc"};
        val = new Object[]{y, Constantes.ETAT_VALIDE, Constantes.ETAT_REGLE, Constantes.TYPE_FV};
        value = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
        y.setTotalRegle(value != null ? value : 0);

        if (y.getTotalWait() > 0 || y.getTotalCours() > 0) {
            y.setLibelleStatut("(" + (y.getTotalWait() + y.getTotalCours()) + ") facture(s) en attente de validation");
            return true;
        } else if (y.getTotalRegle() > 0) {
            y.setLibelleStatut("(" + (y.getTotalRegle()) + ") facture(s) en attente de paiement");
            return true;
        } else if (y.getTotalLivre() > 0) {
            y.setLibelleStatut("(" + (y.getTotalLivre()) + ") facture(s) en attente de livraison");
            return true;
        }
        return false;
    }

    public void equilibreVente(YvsComDocVentes y) {
        if ((y != null) ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            Map<String, String> statuts = dao.getEquilibreVente(y.getId());
            if (statuts != null) {
                y.setStatutLivre(statuts.get("statut_livre"));
                y.setStatutRegle(statuts.get("statut_regle"));
            }
            int idx = header.getDocuments().indexOf(y);
            if (idx > -1) {
                header.getDocuments().set(idx, y);
                update("data-facture_cloture_by_point");
            }
            succes();
        }
    }

    public void equilibreHeader(YvsComEnteteDocVente y) {
        if ((y != null) ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            dao.getEquilibreEnteteVente(y.getId());
            y = (YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{y.getId()});
            int idx = documents.indexOf(y);
            if (idx > -1) {
                documents.set(idx, y);
                update("data-header_cloture_by_point");
            }
            succes();
        }
    }

    public void addParamPoint() {
        ParametreRequete p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point", "point", null);
        if (point > 0) {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point", "point", new YvsBasePointVente(point), "=", "AND");
        }
        paginator.addParam(p);

        p = new ParametreRequete("y.vente.enteteDoc.creneau.creneauPoint.point", "point", null);
        if (point > 0) {
            p = new ParametreRequete("y.vente.enteteDoc.creneau.creneauPoint.point", "point", new YvsBasePointVente(point), "=", "AND");
        }
        paginatorCaisse.addParam(p);
    }

    public void addParamCloture() {
        ParametreRequete p = new ParametreRequete("y.enteteDoc.cloturer", "cloturer", null);
        if (cloturer != null) {
            p = new ParametreRequete("y.enteteDoc.cloturer", "cloturer", cloturer, "=", "AND");
        }
        paginator.addParam(p);

        p = new ParametreRequete("y.vente.enteteDoc.cloturer", "cloturer", null);
        if (cloturer != null) {
            p = new ParametreRequete("y.vente.enteteDoc.cloturer", "cloturer", cloturer, "=", "AND");
        }
        paginatorCaisse.addParam(p);
    }

    public void addParamVendeur() {
        ParametreRequete p = new ParametreRequete("y.enteteDoc.creneau.users", "vendeur", null);
        if (vendeur > 0) {
            p = new ParametreRequete("y.enteteDoc.creneau.users", "vendeur", new YvsUsers(vendeur), "=", "AND");
        }
        paginator.addParam(p);

        p = new ParametreRequete("y.vente.enteteDoc.creneau.users", "vendeur", null);
        if (vendeur > 0) {
            p = new ParametreRequete("y.vente.enteteDoc.creneau.users", "vendeur", new YvsUsers(vendeur), "=", "AND");
        }
        paginatorCaisse.addParam(p);
    }
}

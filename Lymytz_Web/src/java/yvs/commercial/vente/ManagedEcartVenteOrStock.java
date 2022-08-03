/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.vente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.UtilCompta;
import yvs.base.tiers.ManagedTiers;
import yvs.base.tiers.UtilTiers;
import yvs.commercial.UtilCom;
import yvs.commercial.stock.ManagedInventaire;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.dao.Options;
import yvs.dao.services.commercial.ServiceClotureVente;
import yvs.entity.commercial.YvsComCategoriePersonnel;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.stock.YvsComDocStocksEcart;
import yvs.entity.commercial.stock.YvsComReglementEcartStock;
import yvs.entity.commercial.vente.YvsComEcartEnteteVente;
import yvs.entity.commercial.vente.YvsComReglementEcartVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.contrat.YvsGrhTypeElementAdditionel;
import yvs.entity.grh.contrat.retenue.YvsGrhRetenueEcartStock;
import yvs.entity.grh.contrat.retenue.YvsGrhRetenueEcartVente;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsGrhPlanPrelevement;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsers;
import yvs.grh.UtilGrh;
import yvs.grh.bean.ContratEmploye;
import yvs.grh.paie.ManagedRetenue;
import yvs.users.ManagedUser;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedEcartVenteOrStock extends Managed<EcartVenteOrStock, YvsComEcartEnteteVente> implements Serializable {

    private EcartVenteOrStock ecart = new EcartVenteOrStock();
    private List<YvsComReglementEcartVente> ecartsVente;
    private List<YvsComReglementEcartStock> ecartsStock;
    private List<YvsComEcartEnteteVente> programmes, filters, ecartVenteNoPlanifier;
    private List<YvsComDocStocksEcart> ecartStockNoPlanifier;
    private YvsComEcartEnteteVente selectEcartVente = new YvsComEcartEnteteVente();
    private YvsComDocStocksEcart selectEcartStock = new YvsComDocStocksEcart();
    private PaginatorResult<YvsComReglementEcartVente> paginator_vente = new PaginatorResult<>();
    private PaginatorResult<YvsComReglementEcartStock> paginator_stock = new PaginatorResult<>();

    private ReglementEcart reglement = new ReglementEcart();
    private YvsComReglementEcartVente selectReglementVente = new YvsComReglementEcartVente();
    private YvsComReglementEcartStock selectReglementStock = new YvsComReglementEcartStock();

    private String tabIds = "", tabIds_ecart, nature = "V";
    private double montantRetenue = 0, ecartTotal = 0;
    private boolean deleteEcart = false;

    private String vendeurSearch;
    private String statutSearchVente, usersSearchVente, numeroSearchVente, regleSearchVente, operationSearchVente = "=", operationRegleSearchVente = "=";
    private String statutSearchStock, usersSearchStock, numeroSearchStock, regleSearchStock, operationSearchStock = "=", operationRegleSearchStock = "=";
    private long agenceSearch, caisseSearchVente, caisseSearchStock, groupeSearch, agenceTiers;
    private boolean addDateSearchVente, addDateSearchStock;
    private Boolean comptabiliseSearchVente, comptabiliseSearchStock;
    private Date dateDebutSearchVente = new Date(), dateFinSearchVente = new Date();
    private Date dateDebutSearchStock = new Date(), dateFinSearchStock = new Date();

    ServiceClotureVente service;

    public ManagedEcartVenteOrStock() {
        programmes = new ArrayList<>();
        filters = new ArrayList<>();
        ecartsVente = new ArrayList<>();
        ecartsStock = new ArrayList<>();
        ecartVenteNoPlanifier = new ArrayList<>();
        ecartStockNoPlanifier = new ArrayList<>();
    }

    public long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public double getEcartTotal() {
        return ecartTotal;
    }

    public void setEcartTotal(double ecartTotal) {
        this.ecartTotal = ecartTotal;
    }

    public long getAgenceTiers() {
        return agenceTiers;
    }

    public void setAgenceTiers(long agenceTiers) {
        this.agenceTiers = agenceTiers;
    }

    public PaginatorResult currentPaginator() {
        if (nature.equals("V")) {
            return paginator_vente;
        } else {
            return paginator_stock;
        }
    }

    public Boolean getComptabiliseSearchVente() {
        return comptabiliseSearchVente;
    }

    public void setComptabiliseSearchVente(Boolean comptabiliseSearchVente) {
        this.comptabiliseSearchVente = comptabiliseSearchVente;
    }

    public Boolean getComptabiliseSearchStock() {
        return comptabiliseSearchStock;
    }

    public void setComptabiliseSearchStock(Boolean comptabiliseSearchStock) {
        this.comptabiliseSearchStock = comptabiliseSearchStock;
    }

    public List<YvsComEcartEnteteVente> getEcartVenteNoPlanifier() {
        return ecartVenteNoPlanifier;
    }

    public void setEcartVenteNoPlanifier(List<YvsComEcartEnteteVente> ecartVenteNoPlanifier) {
        this.ecartVenteNoPlanifier = ecartVenteNoPlanifier;
    }

    public List<YvsComDocStocksEcart> getEcartStockNoPlanifier() {
        return ecartStockNoPlanifier;
    }

    public void setEcartStockNoPlanifier(List<YvsComDocStocksEcart> ecartStockNoPlanifier) {
        this.ecartStockNoPlanifier = ecartStockNoPlanifier;
    }

    public String getTabIds_ecart() {
        return tabIds_ecart;
    }

    public void setTabIds_ecart(String tabIds_ecart) {
        this.tabIds_ecart = tabIds_ecart;
    }

    public boolean isDeleteEcart() {
        return deleteEcart;
    }

    public void setDeleteEcart(boolean deleteEcart) {
        this.deleteEcart = deleteEcart;
    }

    public String getStatutSearchStock() {
        return statutSearchStock;
    }

    public void setStatutSearchStock(String statutSearchStock) {
        this.statutSearchStock = statutSearchStock;
    }

    public String getUsersSearchStock() {
        return usersSearchStock;
    }

    public void setUsersSearchStock(String usersSearchStock) {
        this.usersSearchStock = usersSearchStock;
    }

    public String getNumeroSearchStock() {
        return numeroSearchStock;
    }

    public void setNumeroSearchStock(String numeroSearchStock) {
        this.numeroSearchStock = numeroSearchStock;
    }

    public String getRegleSearchStock() {
        return regleSearchStock;
    }

    public void setRegleSearchStock(String regleSearchStock) {
        this.regleSearchStock = regleSearchStock;
    }

    public String getOperationSearchStock() {
        return operationSearchStock;
    }

    public void setOperationSearchStock(String operationSearchStock) {
        this.operationSearchStock = operationSearchStock;
    }

    public String getOperationRegleSearchStock() {
        return operationRegleSearchStock;
    }

    public void setOperationRegleSearchStock(String operationRegleSearchStock) {
        this.operationRegleSearchStock = operationRegleSearchStock;
    }

    public long getCaisseSearchStock() {
        return caisseSearchStock;
    }

    public void setCaisseSearchStock(long caisseSearchStock) {
        this.caisseSearchStock = caisseSearchStock;
    }

    public boolean isAddDateSearchStock() {
        return addDateSearchStock;
    }

    public void setAddDateSearchStock(boolean addDateSearchStock) {
        this.addDateSearchStock = addDateSearchStock;
    }

    public Date getDateDebutSearchStock() {
        return dateDebutSearchStock;
    }

    public void setDateDebutSearchStock(Date dateDebutSearchStock) {
        this.dateDebutSearchStock = dateDebutSearchStock;
    }

    public Date getDateFinSearchStock() {
        return dateFinSearchStock;
    }

    public void setDateFinSearchStock(Date dateFinSearchStock) {
        this.dateFinSearchStock = dateFinSearchStock;
    }

    public PaginatorResult<YvsComReglementEcartVente> getPaginator_vente() {
        return paginator_vente;
    }

    public void setPaginator_vente(PaginatorResult<YvsComReglementEcartVente> paginator_vente) {
        this.paginator_vente = paginator_vente;
    }

    public PaginatorResult<YvsComReglementEcartStock> getPaginator_stock() {
        return paginator_stock;
    }

    public void setPaginator_stock(PaginatorResult<YvsComReglementEcartStock> paginator_stock) {
        this.paginator_stock = paginator_stock;
    }

    public String getVendeurSearch() {
        return vendeurSearch;
    }

    public void setVendeurSearch(String vendeurSearch) {
        this.vendeurSearch = vendeurSearch;
    }

    public long getGroupeSearch() {
        return groupeSearch;
    }

    public void setGroupeSearch(long groupeSearch) {
        this.groupeSearch = groupeSearch;
    }

    public List<YvsComEcartEnteteVente> getFilters() {
        return filters;
    }

    public void setFilters(List<YvsComEcartEnteteVente> filters) {
        this.filters = filters;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public YvsComDocStocksEcart getSelectEcartStock() {
        return selectEcartStock;
    }

    public void setSelectEcartStock(YvsComDocStocksEcart selectEcartStock) {
        this.selectEcartStock = selectEcartStock;
    }

    public double getMontantRetenue() {
        return montantRetenue;
    }

    public void setMontantRetenue(double montantRetenue) {
        this.montantRetenue = montantRetenue;
    }

    public String getOperationSearchVente() {
        return operationSearchVente;
    }

    public void setOperationSearchVente(String operationSearchVente) {
        this.operationSearchVente = operationSearchVente;
    }

    public String getOperationRegleSearchVente() {
        return operationRegleSearchVente;
    }

    public void setOperationRegleSearchVente(String operationRegleSearchVente) {
        this.operationRegleSearchVente = operationRegleSearchVente;
    }

    public String getRegleSearchVente() {
        return regleSearchVente;
    }

    public void setRegleSearchVente(String regleSearchVente) {
        this.regleSearchVente = regleSearchVente;
    }

    public String getNumeroSearchVente() {
        return numeroSearchVente;
    }

    public void setNumeroSearchVente(String numeroSearchVente) {
        this.numeroSearchVente = numeroSearchVente;
    }

    public EcartVenteOrStock getEcart() {
        return ecart;
    }

    public void setEcart(EcartVenteOrStock ecart) {
        this.ecart = ecart;
    }

    public List<YvsComReglementEcartVente> getEcartsVente() {
        return ecartsVente;
    }

    public void setEcartsVente(List<YvsComReglementEcartVente> ecartsVente) {
        this.ecartsVente = ecartsVente;
    }

    public List<YvsComReglementEcartStock> getEcartsStock() {
        return ecartsStock;
    }

    public void setEcartsStock(List<YvsComReglementEcartStock> ecartsStock) {
        this.ecartsStock = ecartsStock;
    }

    public YvsComEcartEnteteVente getSelectEcartVente() {
        return selectEcartVente;
    }

    public void setSelectEcartVente(YvsComEcartEnteteVente selectEcartVente) {
        this.selectEcartVente = selectEcartVente;
    }

    public ReglementEcart getReglement() {
        return reglement;
    }

    public void setReglement(ReglementEcart reglement) {
        this.reglement = reglement;
    }

    public YvsComReglementEcartVente getSelectReglementVente() {
        return selectReglementVente;
    }

    public void setSelectReglementVente(YvsComReglementEcartVente selectReglementVente) {
        this.selectReglementVente = selectReglementVente;
    }

    public YvsComReglementEcartStock getSelectReglementStock() {
        return selectReglementStock;
    }

    public void setSelectReglementStock(YvsComReglementEcartStock selectReglementStock) {
        this.selectReglementStock = selectReglementStock;
    }

    public String getTabIds() {
        return tabIds != null ? tabIds : "";
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getStatutSearchVente() {
        return statutSearchVente;
    }

    public void setStatutSearchVente(String statutSearchVente) {
        this.statutSearchVente = statutSearchVente;
    }

    public String getUsersSearchVente() {
        return usersSearchVente;
    }

    public void setUsersSearchVente(String usersSearchVente) {
        this.usersSearchVente = usersSearchVente;
    }

    public long getCaisseSearchVente() {
        return caisseSearchVente;
    }

    public void setCaisseSearchVente(long caisseSearchVente) {
        this.caisseSearchVente = caisseSearchVente;
    }

    public boolean isAddDateSearchVente() {
        return addDateSearchVente;
    }

    public void setAddDateSearchVente(boolean addDateSearchVente) {
        this.addDateSearchVente = addDateSearchVente;
    }

    public Date getDateDebutSearchVente() {
        return dateDebutSearchVente;
    }

    public void setDateDebutSearchVente(Date dateDebutSearchVente) {
        this.dateDebutSearchVente = dateDebutSearchVente;
    }

    public Date getDateFinSearchVente() {
        return dateFinSearchVente;
    }

    public void setDateFinSearchVente(Date dateFinSearchVente) {
        this.dateFinSearchVente = dateFinSearchVente;
    }

    public List<YvsComEcartEnteteVente> getProgrammes() {
        return programmes;
    }

    public void setProgrammes(List<YvsComEcartEnteteVente> programmes) {
        this.programmes = programmes;
    }

    @Override
    public void doNothing() {
        service = new ServiceClotureVente(dao, currentNiveau, currentUser, currentAgence.getSociete());
        if (agenceTiers < 0) {
            agenceTiers = currentAgence.getId();
        }
    }

    @Override
    public void loadAll() {
        loadAll(true, true); //To change body of generated methods, choose Tools | Templates.
        doNothing();
    }

    private void actualiseView() {
        if (nature.contains("V")) {
            if (ecartsVente != null ? ecartsVente.size() == 1 : false) {
                onSelectObject(ecartsVente.get(0).getPiece());
                execute("collapseForm('ecart_vente')");
            } else {
                execute("collapseList('ecart_vente')");
            }
        } else {
            if (ecartsStock != null ? ecartsStock.size() == 1 : false) {
                onSelectObject(ecartsStock.get(0).getPiece());
                execute("collapseForm('ecart_vente')");
            } else {
                execute("collapseList('ecart_vente')");
            }
        }
    }

    public void loadAll(boolean avance, boolean init) {
        if (nature.equals("V")) {
            paginator_vente.addParam(new ParametreRequete("y.piece.users.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
            ecartsVente = paginator_vente.executeDynamicQuery("YvsComReglementEcartVente", "y.dateReglement DESC, y.piece.numero DESC", avance, init, (int) imax, dao);
        } else {
            paginator_stock.addParam(new ParametreRequete("y.piece.tiers.societe", "societe", currentAgence.getSociete(), "=", "AND"));
            ecartsStock = paginator_stock.executeDynamicQuery("YvsComReglementEcartStock", "y.dateReglement DESC, y.piece.numero DESC", avance, init, (int) imax, dao);
        }
        update("data-ecart_vente");
    }

    public void parcoursInAllResult(boolean avancer) {
        if (nature.equals("V")) {
            setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
            if (getOffset() < 0 || getOffset() >= (paginator_vente.getNbResult())) {
                setOffset(0);
            }
            List<YvsComReglementEcartVente> re = paginator_vente.parcoursDynamicData("YvsComReglementEcartVente", "y", "y.dateReglement DESC, y.piece.numero DESC", getOffset(), dao);
            if (!re.isEmpty()) {
                onSelectObject(re.get(0).getPiece());
            }
        } else {
            setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
            if (getOffset() < 0 || getOffset() >= (paginator_stock.getNbResult())) {
                setOffset(0);
            }
            List<YvsComReglementEcartStock> re = paginator_stock.parcoursDynamicData("YvsComReglementEcartStock", "y", "y.dateReglement DESC, y.piece.numero DESC", getOffset(), dao);
            if (!re.isEmpty()) {
                onSelectObject(re.get(0).getPiece());
            }
        }
    }

    public void paginer(boolean avance) {
        loadAll(avance, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            long v;
            try {
                v = (long) ev.getNewValue();
            } catch (Exception ex) {
                v = (int) ev.getNewValue();
            }
            setNbMax((int) v);
            this.imax = getNbMax();
            this.idebut = 0;
            if (nature.equals("V")) {
                paginator_vente.setRows((int) imax);
            } else {
                paginator_stock.setRows((int) imax);
            }
        }
        loadAll(true, true);
    }

    public void gotoPagePaginator() {
        if (nature.equals("V")) {
            paginator_vente.gotoPage((int) imax);
        } else {
            paginator_stock.gotoPage((int) imax);
        }
        loadAll(true, true);
    }

    @Override
    public boolean controleFiche(EcartVenteOrStock bean) {
        if (bean == null) {
            return false;
        }
        if (bean.getMontant() == 0) {
            getErrorMessage("Vous devez entrer le montant");
            return false;
        }
        if (nature.equals("V")) {
            if (bean.getUsers() != null ? bean.getUsers().getId() < 1 : true) {
                getErrorMessage("Vous devez precisez l'utilisateur");
                return false;
            }
            if (!Util.asString(bean.getNumero())) {
                String numero = service.reference(bean.getDateEcart());
                bean.setNumero(numero);
            }
        } else {
            if (bean.getTiers() != null ? bean.getTiers().getId() < 1 : true) {
                getErrorMessage("Vous devez precisez le tiers");
                return false;
            }
            if (bean.getInventaire() != null ? bean.getInventaire().getId() < 1 : true) {
                getErrorMessage("Vous devez precisez l'inventaire");
                return false;
            }
            Double taux = (Double) dao.loadObjectByNameQueries("YvsComDocStocksEcart.sumByStockNotId", new String[]{"docStock", "id"}, new Object[]{new YvsComDocStocks(bean.getInventaire().getId()), bean.getId()});
            if (((taux != null ? taux : 0) + bean.getMontant()) > 100) {
                getErrorMessage("Vous ne pouvez pas exceder 100%");
                return false;
            }
        }
        return true;
    }

    public boolean controleFiche(ReglementEcart bean) {
        if (bean == null) {
            return false;
        }
        if (bean.getPiece() != null ? bean.getPiece().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez la piece d'ecart");
            return false;
        }
        if (bean.getMontant() <= 0) {
            getErrorMessage("Vous devez entrer le montant");
            return false;
        }
        Double montant = 0D, total = 0D;
        if (nature.equals("V")) {
            total = ecart.getMontant();
            montant = (Double) dao.loadObjectByNameQueries("YvsComReglementEcartVente.sumByPieceNotId", new String[]{"piece", "id"}, new Object[]{new YvsComEcartEnteteVente(bean.getPiece().getId()), bean.getId()});
        } else {
            total = (ecart.getMontant() * ecart.getInventaire().getMontantTotal()) / 100;
            montant = (Double) dao.loadObjectByNameQueries("YvsComReglementEcartStock.sumByPieceNotId", new String[]{"piece", "id"}, new Object[]{new YvsComDocStocksEcart(bean.getPiece().getId()), bean.getId()});
        }
        if ((montant != null ? montant : 0) + bean.getMontant() > (total + 1)) {
            getErrorMessage("La somme des reglements ne doit pas excéder le montant de l'ecart");
            return false;
        }
        if (!Util.asString(bean.getNumero())) {
            String numero;
            if (nature.equals("V")) {
                numero = "PC-" + bean.getPiece().getNumero() + "-0" + (ecart.getReglementsVente().size() + 1);
            } else {
                numero = "PC-" + bean.getPiece().getNumero() + "-0" + (ecart.getReglementsStock().size() + 1);
            }
            bean.setNumero(numero);
        }
        return true;
    }

    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(ecart)) {
                if (nature.equals("V")) {
                    if (ecart.getAgence() != null ? ecart.getAgence().getId() < 1 : true) {
                        ecart.setAgence(ecart.getUsers().getAgence());
                    }
                    selectEcartVente = UtilCom.buildEcartVente(ecart, currentUser);
                    if (ecart.getId() < 1) {
                        selectEcartVente.setId(null);
                        selectEcartVente = (YvsComEcartEnteteVente) dao.save1(selectEcartVente);
                    } else {
                        dao.update(selectEcartVente);
                    }
                    if (selectEcartVente != null ? selectEcartVente.getId() > 0 : false) {
                        ecart.setId(selectEcartVente.getId());
                        if (ecart.getReglementsVente() != null ? ecart.getReglementsVente().isEmpty() : true) {
                            YvsComReglementEcartVente r = onGenererReglementVente(selectEcartVente, false);
                            if (r != null) {
                                int idx = ecart.getReglementsVente().indexOf(r);
                                if (idx > -1) {
                                    ecart.getReglementsVente().set(idx, r);
                                } else {
                                    ecart.getReglementsVente().add(r);
                                }

                                idx = ecartsVente.indexOf(r);
                                if (idx > -1) {
                                    ecartsVente.set(idx, r);
                                } else {
                                    ecartsVente.add(r);
                                }
                                update("data-reglement_ecart_vente");
                            }
                        }
                        update("data-ecart_vente");
                        actionOpenOrResetAfter(this);
                        succes();
                    }
                } else {
                    if (!autoriser("gescom_inv_attrib_ecart")) {
                        openNotAcces();
                        return false;
                    }
                    selectEcartStock = UtilCom.buildEcartStock(ecart, currentUser);
                    if (ecart.getId() < 1) {
                        selectEcartStock.setId(null);
                        selectEcartStock = (YvsComDocStocksEcart) dao.save1(selectEcartStock);
                    } else {
                        dao.update(selectEcartStock);
                    }
                    if (selectEcartStock != null ? selectEcartStock.getId() > 0 : false) {
                        ecart.setId(selectEcartStock.getId());
                        if (ecart.getReglementsStock() != null ? ecart.getReglementsStock().isEmpty() : true) {
                            YvsComReglementEcartStock r = onGenererReglementStock(selectEcartStock, false);
                            if (r != null) {
                                int idx = ecart.getReglementsStock().indexOf(r);
                                if (idx > -1) {
                                    ecart.getReglementsStock().set(idx, r);
                                } else {
                                    ecart.getReglementsStock().add(r);
                                }

                                idx = ecartsStock.indexOf(r);
                                if (idx > -1) {
                                    ecartsStock.set(idx, r);
                                } else {
                                    ecartsStock.add(r);
                                }
                                update("data-reglement_ecart_stock");
                            }
                        }
                        update("data-ecart_vente");
                        actionOpenOrResetAfter(this);
                        succes();
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedEcartVente (saveNew)", ex);
        }
        return false;
    }

    public void onChangeInclure(YvsComEcartEnteteVente y) {
        y.setInclure(!y.isInclure());
        int index = programmes.indexOf(y);
        if (index > -1) {
            programmes.set(index, y);
        }
    }

    public void saveAllEcart(boolean valider) {
        List<YvsComEcartEnteteVente> list = new ArrayList<>();
        for (YvsComEcartEnteteVente y : programmes) {
            if (y.isInclure()) {
                saveEcart(y, valider, false);
                list.add(y);
            }
        }
        programmes.removeAll(list);
        succes();
        update("data-ecart_vente");
        update("data-programme_ecart_vente");
    }

    public void saveEcart(YvsComEcartEnteteVente y, boolean valider) {
        saveEcart(y, valider, true);
    }

    public void saveEcart(YvsComEcartEnteteVente y, boolean valider, boolean msg) {
        try {
            if (y != null) {
                String numero = service.reference(y.getDateEcart());
                y.setNumero(numero);
                y.setAgence(y.getUsers().getAgence());
                y.setStatut(valider ? Constantes.STATUT_DOC_VALIDE : Constantes.STATUT_DOC_EDITABLE);
                y.setStatutRegle(Constantes.STATUT_DOC_ATTENTE);
                y.setId(null);
                y = (YvsComEcartEnteteVente) dao.save1(y);
                if (msg) {
                    programmes.remove(y);
                }
                onGenererReglementVente(y, false);
                if (msg) {
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedEcartVente (saveEcart)", ex);
        }
    }

    public void removeEcart(YvsComEcartEnteteVente y) {
        try {
            if (y.getId() > 0) {
                dao.delete(y);
                ecartsVente.removeAll(y.getReglements());
                update("data-ecart_vente");
            }
            programmes.remove(y);
            update("data-programme_ecart_vente");
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedEcartVente (removeEcart)", ex);
        }
    }

    public void saveReglement() {
        try {
            reglement.setPiece(ecart);
            if (controleFiche(reglement)) {
                if (nature.equals("V")) {
                    YvsComReglementEcartVente y = UtilCom.buildReglementEcartVente(reglement, currentUser);
                    if (reglement.getId() < 1) {
                        y = (YvsComReglementEcartVente) dao.save1(y);
                    } else {
                        dao.update(y);
                    }
                    int idx = ecart.getReglementsVente().indexOf(y);
                    if (idx > -1) {
                        ecart.getReglementsVente().set(idx, y);
                    } else {
                        ecart.getReglementsVente().add(y);
                    }
                    idx = ecartsVente.indexOf(y);
                    if (idx > -1) {
                        ecartsVente.set(idx, y);
                    } else {
                        ecartsVente.add(y);
                    }
                    update("data-reglement_ecart_vente");
                } else {
                    YvsComReglementEcartStock y = UtilCom.buildReglementEcartStock(reglement, currentUser);
                    if (reglement.getId() < 1) {
                        y = (YvsComReglementEcartStock) dao.save1(y);
                    } else {
                        dao.update(y);
                    }
                    int idx = ecart.getReglementsStock().indexOf(y);
                    if (idx > -1) {
                        ecart.getReglementsStock().set(idx, y);
                    } else {
                        ecart.getReglementsStock().add(y);
                    }
                    idx = ecartsStock.indexOf(y);
                    if (idx > -1) {
                        ecartsStock.set(idx, y);
                    } else {
                        ecartsStock.add(y);
                    }
                    update("data-reglement_ecart_stock");
                }
                succes();
                resetReglement();
                update("data-ecart_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedEcartVente (saveReglement)", ex);
        }

    }

    @Override
    public void resetFiche() {
        ecart = new EcartVenteOrStock();
        selectEcartVente = new YvsComEcartEnteteVente();
        selectEcartStock = new YvsComDocStocksEcart();
        update("blog-ecart_vente");
        resetReglement();
    }

    public void resetReglement() {
        reglement = new ReglementEcart();
        selectReglementVente = new YvsComReglementEcartVente();
        selectReglementStock = new YvsComReglementEcartStock();
        Double montant = 0D, total = 0D;
        if (nature.equals("V")) {
            total = ecart.getMontant();
            montant = (Double) dao.loadObjectByNameQueries("YvsComReglementEcartVente.sumByPiece", new String[]{"piece",}, new Object[]{selectEcartVente});
        } else {
            total = (ecart.getMontant() * ecart.getInventaire().getMontantTotal()) / 100;
            montant = (Double) dao.loadObjectByNameQueries("YvsComReglementEcartStock.sumByPiece", new String[]{"piece"}, new Object[]{selectEcartStock});
        }
        montant = (total != null ? total : 0) - (montant != null ? montant : 0);
        reglement.setMontant(montant > 0 ? montant : 0);
        update("form-reglement_ecart_vente");
    }

    @Override
    public void onSelectObject(YvsComEcartEnteteVente y) {
        if (y.getReglements().isEmpty()) {
            y.setReglements(dao.loadNameQueries("YvsComReglementEcartVente.findByPiece", new String[]{"piece"}, new Object[]{y}));
        }
        selectEcartVente = y;
        ecart = UtilCom.buildBeanEcartVente(y);
        resetReglement();
        update("blog-ecart_vente");
    }

    public void onSelectObject(YvsComDocStocksEcart y) {
        if (y.getReglements().isEmpty()) {
            y.setReglements(dao.loadNameQueries("YvsComReglementEcartStock.findByPiece", new String[]{"piece"}, new Object[]{y}));
        }
        selectEcartStock = y;
        ecart = UtilCom.buildBeanEcartStock(y);
        if (y.getDocStock().getValeur() != null ? y.getDocStock().getValeur().getMontant() > 0 : false) {
            ecart.getInventaire().setMontantTotal(y.getDocStock().getValeur().getMontant());
        } else {
            ecart.getInventaire().setMontantTotal(y.getDocStock().calculMontantTotal(dao, false));
        }
        Double taux = (Double) dao.loadObjectByNameQueries("YvsComDocStocksEcart.sumByStock", new String[]{"docStock"}, new Object[]{y.getDocStock()});
        ecart.getInventaire().setTaux((taux != null ? taux : 0));
        resetReglement();
        update("blog-ecart_vente");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            if (nature.equals("V")) {
                onSelectObject(((YvsComReglementEcartVente) ev.getObject()).getPiece());
                tabIds = ecartsVente.indexOf((YvsComReglementEcartVente) ev.getObject()) + "";
            } else {
                onSelectObject(((YvsComReglementEcartStock) ev.getObject()).getPiece());
                tabIds = ecartsStock.indexOf((YvsComReglementEcartStock) ev.getObject()) + "";
            }
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewEcartVente(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsComEcartEnteteVente) ev.getObject());
        }
    }

    public void loadOnViewEcartStock(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsComDocStocksEcart) ev.getObject());
        }
    }

    public void loadOnViewReglement(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            if (nature.equals("V")) {
                selectReglementVente = (YvsComReglementEcartVente) ev.getObject();
                reglement = UtilCom.buildBeanReglementEcart(selectReglementVente);
            } else {
                selectReglementStock = (YvsComReglementEcartStock) ev.getObject();
                reglement = UtilCom.buildBeanReglementEcart(selectReglementStock);
            }
        }
    }

    public void unLoadOnViewReglement(UnselectEvent ev) {
        resetReglement();
    }

    public void loadOnViewInventaire(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            chooseInventaire((YvsComDocStocks) ev.getObject());
        }
    }

    public void loadOnViewTiers(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            chooseTiers((YvsBaseTiers) ev.getObject());
        }
    }

    public void loadOnViewUsers(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsUsers bean = (YvsUsers) ev.getObject();
            chooseUsers(UtilUsers.buildBeanUsers(bean));
            update("txt-users_ecart_vente");
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Integer> l = decomposeSelection(tabIds);
                if (nature.equals("V")) {
                    List<YvsComReglementEcartVente> list = new ArrayList<>();
                    YvsComReglementEcartVente bean;
                    for (Integer ids : l) {
                        bean = ecartsVente.get(ids);
                        if (bean.getRetenue() != null ? bean.getRetenue().getId() > 0 : false) {
                            if (!autoriser("ret_generer_by_ecart")) {
                                openNotAcces();
                                continue;
                            }
                        }
                        Long count = (Long) dao.loadObjectByNameQueries("YvsComReglementEcartVente.countByPiece", new String[]{"piece"}, new Object[]{bean.getPiece()});
                        if ((count != null ? count : 0) <= 1 && !deleteEcart) {
                            getErrorMessage("Vous ne pouvez pas supprimer le reglement + " + bean.getNumero() + ". Modifier le svp!!!");
                            return;
                        }
                        bean.setAuthor(currentUser);
                        bean.setDateUpdate(new Date());
                        list.add(bean);
                        dao.delete(bean);
                        if (bean.getPiece().getId() == ecart.getId()) {
                            resetFiche();
                        }
                        if (deleteEcart) {
                            dao.delete(bean.getPiece());
                        }
                    }
                    ecartsVente.removeAll(list);
                } else {
                    List<YvsComReglementEcartStock> list = new ArrayList<>();
                    YvsComReglementEcartStock bean;
                    for (Integer ids : l) {
                        bean = ecartsStock.get(ids);
                        if (bean.getRetenue() != null ? bean.getRetenue().getId() > 0 : false) {
                            if (!autoriser("ret_generer_by_ecart")) {
                                openNotAcces();
                                continue;
                            }
                        }
                        Long count = (Long) dao.loadObjectByNameQueries("YvsComReglementEcartStock.countByPiece", new String[]{"piece"}, new Object[]{bean.getPiece()});
                        if ((count != null ? count : 0) <= 1 && !deleteEcart) {
                            getErrorMessage("Vous ne pouvez pas supprimer le reglement + " + bean.getNumero() + ". Modifier le svp!!!");
                            return;
                        }
                        bean.setAuthor(currentUser);
                        bean.setDateUpdate(new Date());
                        list.add(bean);
                        dao.delete(bean);
                        if (bean.getPiece().getId() == ecart.getId()) {
                            resetFiche();
                        }
                        if (deleteEcart) {
                            dao.delete(bean.getPiece());
                        }
                    }
                    ecartsStock.removeAll(list);
                }
                succes();
                update("data-ecart_vente");
                tabIds = "";
            } else {
                getErrorMessage("Aucun élément selectionné !");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedEcartVente (deleteBean)", ex);
        }
    }

    public void deleteOneBean() {
        try {
            if (nature.equals("V")) {
                deleteReglementVente(selectReglementVente, true);
            } else {
                deleteReglementStock(selectReglementStock, true);
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedEcartVente (deleteBean)", ex);
        }
    }

    public void deleteEcartVente(YvsComEcartEnteteVente y) {
        try {
            dao.delete(y);
            ecartVenteNoPlanifier.remove(y);
            update("data-no_planifier_ecart_vente");
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedEcartVente (deleteBean)", ex);
        }
    }

    public void deleteEcartStock(YvsComDocStocksEcart y) {
        try {
            dao.delete(y);
            ecartStockNoPlanifier.remove(y);
            update("data-no_planifier_ecart_stock");
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedEcartVente (deleteBean)", ex);
        }
    }

    public void deleteReglementVente(YvsComReglementEcartVente y, boolean forPiece) {
        try {
            if (y != null) {
                if (y.getRetenue() != null ? y.getRetenue().getId() > 0 : false) {
                    if (!autoriser("ret_generer_by_ecart")) {
                        openNotAcces();
                        return;
                    }
                }
                System.err.println("y : " + y);
                YvsComEcartEnteteVente piece = y.getPiece();
                System.err.println("piece : " + piece);
                if (forPiece) {
                    Long count = (Long) dao.loadObjectByNameQueries("YvsComReglementEcartVente.countByPiece", new String[]{"piece"}, new Object[]{piece});
                    if ((count != null ? count : 0) <= 1 && !deleteEcart) {
                        getErrorMessage("Vous ne pouvez pas supprimer ce reglement. Modifier le svp!!!");
                        return;
                    }
                } else {
                    if (ecart.getReglementsVente().size() <= 1) {
                        getErrorMessage("Vous ne pouvez pas supprimer ce reglement. Modifier le svp!!!");
                        return;
                    }
                }
                dao.delete(y);
                ecartsVente.remove(y);
                if (forPiece) {
                    if (piece != null) {
                        if (piece.getId().equals(ecart.getId())) {
                            resetFiche();
                        }
                        if (deleteEcart) {
                            dao.delete(piece);
                        }
                    }
                } else {
                    ecart.getReglementsVente().remove(y);
                    if (y != null ? y.getId().equals(reglement.getId()) : false) {
                        resetReglement();
                    }
                    update("data-reglement_ecart_vente");
                }
                update("data-ecart_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedEcartVente (deleteBean)", ex);
        }
    }

    public void deleteReglementStock(YvsComReglementEcartStock y, boolean forPiece) {
        try {
            if (y != null) {
                if (y.getRetenue() != null ? y.getRetenue().getId() > 0 : false) {
                    if (!autoriser("ret_generer_by_ecart")) {
                        openNotAcces();
                        return;
                    }
                }
                YvsComDocStocksEcart piece = y.getPiece();
                if (forPiece) {
                    Long count = (Long) dao.loadObjectByNameQueries("YvsComReglementEcartStock.countByPiece", new String[]{"piece"}, new Object[]{piece});
                    if ((count != null ? count : 0) <= 1 && !deleteEcart) {
                        getErrorMessage("Vous ne pouvez pas supprimer ce reglement. Modifier le svp!!!");
                        return;
                    }
                } else {
                    if (ecart.getReglementsStock().size() <= 1) {
                        getErrorMessage("Vous ne pouvez pas supprimer ce reglement. Modifier le svp!!!");
                        return;
                    }
                }
                dao.delete(y);
                ecartsStock.remove(y);
                if (forPiece) {
                    if (piece != null) {
                        if (piece.getId().equals(ecart.getId())) {
                            resetFiche();
                        }
                        if (deleteEcart) {
                            dao.delete(piece);
                        }
                    }
                } else {
                    ecart.getReglementsStock().remove(y);
                    if (y != null ? y.getId().equals(reglement.getId()) : false) {
                        resetReglement();
                    }
                    update("data-reglement_ecart_stock");
                }
                update("data-ecart_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedEcartVente (deleteBean)", ex);
        }
    }

    public void searchUsers() {
        String num = ecart.getUsers().getCodeUsers();
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedUser m = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (m != null) {
                Users e = m.searchUsersActif(num, true);
                chooseUsers(e);
                if (m.getListUser() != null ? m.getListUser().size() > 1 : false) {
                    update("data-users_ecart_vente");
                }
            }
        }
    }

    public void searchTiers() {
        String num = ecart.getTiers().getCodeTiers();
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedTiers m = (ManagedTiers) giveManagedBean(ManagedTiers.class);
            if (m != null) {
                m.findTiersByCode(num);
                if (m.getListTiers() != null ? !m.getListTiers().isEmpty() : false) {
                    if (m.getListTiers().size() == 1) {
                        chooseTiers(m.getListTiers().get(0));
                    } else {
                        openDialog("dlgListTiers");
                        update("data-tiers_ecart_vente");
                    }
                } else {
                    ecart.getTiers().setError(true);
                }
            }
        }
    }

    public void searchInventaire() {
        String num = ecart.getInventaire().getNumDoc();
        ecart.getInventaire().setError(false);
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedInventaire m = (ManagedInventaire) giveManagedBean(ManagedInventaire.class);
            if (m != null) {
                m.setNumSearch_(num);
                m.searchByNum();
                if (m.getDocuments() != null ? !m.getDocuments().isEmpty() : false) {
                    if (m.getDocuments().size() == 1) {
                        chooseInventaire(m.getDocuments().get(0));
                    } else {
                        openDialog("dlgListInventaire");
                        update("data-inventaire_ecart_vente");
                    }
                } else {
                    ecart.getInventaire().setError(true);
                }
            }
        }
    }

    public void chooseUsers(Users u) {
        ecart.setUsers(u);
        ecart.setAgence(u.getAgence());
        Date dateDebut = (Date) dao.loadObjectByNameQueries("YvsComEcartEnteteVente.findLastDateByUsers", new String[]{"users"}, new Object[]{new YvsUsers(u.getId())});
        if (dateDebut == null) {
            dateDebut = (Date) dao.loadObjectByNameQueries("YvsComptaMouvementCaisse.findFirstDate", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateDebut);
            cal.add(Calendar.DATE, 1);
            dateDebut = cal.getTime();
        }
        ecart.setDateDebut(dateDebut);
        update("txt-date_debut_ecart_vente");
    }

    public void chooseTiers(YvsBaseTiers u) {
        ecart.setTiers(UtilTiers.buildSimpleBeanTiers(u));
        update("txt-tiers_ecart_vente");
    }

    public void chooseInventaire(YvsComDocStocks u) {
        u.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{u}));
        ecart.setInventaire(UtilCom.buildBeanDocStock(u));
        if (u.getValeur() != null ? u.getValeur().getMontant() > 0 : false) {
            ecart.getInventaire().setMontantTotal(u.getValeur().getMontant());
        } else {
            ecart.getInventaire().setMontantTotal(u.calculMontantTotal(dao, false));
        }
        ecart.setDateEcart(u.getDateDoc());
        Double taux = (Double) dao.loadObjectByNameQueries("YvsComDocStocksEcart.sumByStock", new String[]{"docStock"}, new Object[]{u});
        ecart.getInventaire().setTaux((taux != null ? taux : 0));
        ecart.setNumero(u.getNumDoc());
        update("txt-inventaire_ecart_vente");
    }

    public void chooseCaisse(boolean forEcart) {
        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (w != null) {
            if (forEcart) {
                int idx = w.getCaisses().indexOf(new YvsBaseCaisse(ecart.getCaisse().getId()));
                if (idx > -1) {
                    YvsBaseCaisse y = w.getCaisses().get(idx);
                    ecart.setCaisse(UtilCompta.buildSimpleBeanCaisse(y));
                }
            } else {
                int idx = w.getCaisses().indexOf(new YvsBaseCaisse(reglement.getCaisse().getId()));
                if (idx > -1) {
                    YvsBaseCaisse y = w.getCaisses().get(idx);
                    reglement.setCaisse(UtilCompta.buildSimpleBeanCaisse(y));
                }
            }
        }
    }

    public void calculMontant() {
        try {
            if (ecart.getUsers() != null ? ecart.getUsers().getId() < 1 : true) {
                getErrorMessage("Vous devez selectionner un vendeur");
                return;
            }
            double versementAttendu = dao.loadVersementAttendu(ecart.getUsers().getId(), ecart.getDateDebut(), ecart.getDateEcart());
            double versementReel = dao.loadVersementReel(ecart.getUsers().getId(), ecart.getDateDebut(), ecart.getDateEcart());
            double montant = versementAttendu - versementReel;
            ecart.setMontant(montant > 0 ? montant : 0);
            update("txt-mouvement_ecart_vente");
            update("txt-montant_ecart_vente");
        } catch (Exception ex) {
            getException("calculMontant", ex);
            getErrorMessage("Action impossible!!!");
        }
    }

    public boolean valider() {
        if (nature.equals("V")) {
            return validerVente(ecart, selectEcartVente);
        } else {
            return validerStock(ecart, selectEcartStock);
        }
    }

    public void validerSelected() {
        if ((tabIds != null) ? !tabIds.equals("") : false) {
            List<Integer> ids = decomposeSelection(tabIds);
            if (nature.equals("V")) {
                for (Integer index : ids) {
                    validerVente(null, ecartsVente.get(index).getPiece());
                }
            } else {
                for (Integer index : ids) {
                    validerStock(null, ecartsStock.get(index).getPiece());
                }
            }
        }
    }

    public void confirmer() {
        ecart.setCaisses(dao.loadNameQueries("YvsComptaCaissePieceVente.findCaisseByCaissier", new String[]{"caissier"}, new Object[]{new YvsUsers(ecart.getUsers().getId())}));
        update("form-caisse_ecart_vente");
        openDialog("dlgChooseCaisse");
    }

    public boolean validerVente(YvsComEcartEnteteVente y) {
        return validerVente(null, y);
    }

    public boolean validerStock(YvsComDocStocksEcart y) {
        return validerStock(null, y);
    }

    public boolean validerVente(EcartVenteOrStock ecart, YvsComEcartEnteteVente y) {
        if (y != null ? y.getId() > 0 : false) {
            return changeStatutVente(ecart, y, Constantes.STATUT_DOC_VALIDE);
        }
        return false;
    }

    public boolean validerStock(EcartVenteOrStock ecart, YvsComDocStocksEcart y) {
        if (y != null ? y.getId() > 0 : false) {
            return changeStatutStock(ecart, y, Constantes.STATUT_DOC_VALIDE);
        }
        return false;
    }

    public boolean annuler(boolean suspend) {
        if (nature.equals("V")) {
            return annulerVente(ecart, selectEcartVente, suspend);
        } else {
            return annulerStock(ecart, selectEcartStock, suspend);
        }
    }

    public boolean annulerVente(YvsComEcartEnteteVente y, boolean suspend) {
        return annulerVente(null, y, suspend);
    }

    public boolean annulerStock(YvsComDocStocksEcart y, boolean suspend) {
        return annulerStock(null, y, suspend);
    }

    public boolean annulerVente(EcartVenteOrStock ecart, YvsComEcartEnteteVente y, boolean suspend) {
        return changeStatutVente(ecart, y, (suspend ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE));
    }

    public boolean annulerStock(EcartVenteOrStock ecart, YvsComDocStocksEcart y, boolean suspend) {
        return changeStatutStock(ecart, y, (suspend ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE));
    }

    public boolean changeStatutVente(EcartVenteOrStock ecart, YvsComEcartEnteteVente y, char statut) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                if (ecart != null) {
                    y.setDateEcart(ecart.getDateEcart());
                }
                y.setStatut(statut);
                //evité la mise à jour en cascade
                List<YvsComReglementEcartVente> list = new ArrayList<>();
                list.addAll(y.getReglements());
                if (y.getReglements() != null) {
                    y.getReglements().clear();
                }
                dao.update(y);
                if (ecart != null) {
                    ecart.setStatut(statut);
                    if (ecart.getReglementsVente() != null ? ecart.getReglementsVente().isEmpty() : true) {
                        ecart.setReglementsVente(list);
                    }
                }
                for (YvsComReglementEcartVente r : list) {
                    r.setPiece(y);
                    if (ecart != null) {
                        int idx = ecart.getReglementsVente().indexOf(r);
                        if (idx > -1) {
                            ecart.getReglementsVente().set(idx, r);
                        }
                    }
                    int idx = ecartsVente.indexOf(r);
                    if (idx > -1) {
                        ecartsVente.set(idx, r);
                        update("data-ecart_vente");
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
        return false;
    }

    public boolean changeStatutStock(EcartVenteOrStock ecart, YvsComDocStocksEcart y, char statut) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                y.setStatut(statut);
                //evité la mise à jour en cascade
                List<YvsComReglementEcartStock> list = new ArrayList<>();
                list.addAll(y.getReglements());
                if (y.getReglements() != null) {
                    y.getReglements().clear();
                }
                dao.update(y);
                if (ecart != null) {
                    ecart.setStatut(statut);
                    if (ecart.getReglementsStock() != null ? ecart.getReglementsStock().isEmpty() : true) {
                        ecart.setReglementsStock(list);
                    }
                }
                for (YvsComReglementEcartStock r : list) {
                    r.setPiece(y);
                    if (ecart != null) {
                        int idx = ecart.getReglementsStock().indexOf(r);
                        if (idx > -1) {
                            ecart.getReglementsStock().set(idx, r);
                        }
                    }
                    int idx = ecartsStock.indexOf(r);
                    if (idx > -1) {
                        ecartsStock.set(idx, r);
                        update("data-ecart_vente");
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
        return false;
    }

    public void clearParams() {
        if (nature.equals("V")) {
            addDateSearchVente = false;
            operationSearchVente = "=";
            operationRegleSearchVente = "=";
            dateDebutSearchVente = new Date();
            dateFinSearchVente = new Date();
            usersSearchVente = "";
            statutSearchVente = null;
            regleSearchVente = null;
            numeroSearchVente = "";
            paginator_vente.clear();
        } else {
            addDateSearchStock = false;
            operationSearchStock = "=";
            operationRegleSearchStock = "=";
            dateDebutSearchStock = new Date();
            dateFinSearchStock = new Date();
            usersSearchStock = "";
            statutSearchStock = null;
            regleSearchStock = null;
            numeroSearchStock = "";
            paginator_stock.clear();
        }
        loadAll(true, true);
    }

    public void addParamDatesVente() {
        ParametreRequete p = new ParametreRequete("y.dateReglement", "dates", null, "BETWEEN", "AND");
        if (addDateSearchVente ? !dateDebutSearchVente.after(dateFinSearchVente) : false) {
            p = new ParametreRequete("y.dateReglement", "dates", dateDebutSearchVente, dateFinSearchVente, "BETWEEN", "AND");
        }
        paginator_vente.addParam(p);
        loadAll(true, true);
        actualiseView();
    }

    public void addParamAgence() {
        ParametreRequete p;
        if (agenceSearch > 0) {
            p = new ParametreRequete("y.piece.agence", "agence", new YvsAgences(agenceSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.piece.agence", "agence", null);
        }
        paginator_vente.addParam(p);
        loadAll(true, true);
        actualiseView();
    }

    public void addParamDatesStock() {
        ParametreRequete p = new ParametreRequete("y.dateReglement", "dates", null, "BETWEEN", "AND");
        if (addDateSearchStock ? !dateDebutSearchStock.after(dateFinSearchStock) : false) {
            p = new ParametreRequete("y.dateReglement", "dates", dateDebutSearchStock, dateFinSearchStock, "BETWEEN", "AND");
        }
        paginator_stock.addParam(p);
        loadAll(true, true);
        actualiseView();
    }

    public void addParamUsersVente() {
        ParametreRequete p = new ParametreRequete("y.piece.users", "users", null, "=", "AND");
        if (Util.asString(usersSearchVente)) {
            p = new ParametreRequete(null, "users", usersSearchVente, "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.piece.users.codeUsers)", "users", usersSearchVente.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.piece.users.nomUsers)", "users", usersSearchVente.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator_vente.addParam(p);
        loadAll(true, true);
        actualiseView();
    }

    public void addParamUsersStock() {
        ParametreRequete p = new ParametreRequete("y.piece.tiers", "tiers", null, "=", "AND");
        if (Util.asString(usersSearchStock)) {
            p = new ParametreRequete(null, "tiers", usersSearchStock, "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.piece.tiers.codeTiers)", "tiers", usersSearchStock.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.piece.tiers.nom)", "tiers", usersSearchStock.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator_stock.addParam(p);
        loadAll(true, true);
        actualiseView();
    }

    public void addParamNumeroVente() {
        ParametreRequete p = new ParametreRequete("y.piece.numero", "numero", null, "=", "AND");
        if (Util.asString(numeroSearchVente)) {
            p = new ParametreRequete("UPPER(y.piece.numero)", "numero", numeroSearchVente.toUpperCase() + "%", "LIKE", "AND");
        }
        paginator_vente.addParam(p);
        loadAll(true, true);
        actualiseView();
    }

    public void addParamNumeroStock() {
        ParametreRequete p = new ParametreRequete("y.piece.numero", "numero", null, "=", "AND");
        if (Util.asString(numeroSearchStock)) {
            p = new ParametreRequete("UPPER(y.piece.numero)", "numero", numeroSearchStock.toUpperCase() + "%", "LIKE", "AND");
        }
        paginator_stock.addParam(p);
        loadAll(true, true);
        actualiseView();
    }

    public void addParamStatutVente() {
        ParametreRequete p = new ParametreRequete("y.piece.statut", "statut", null, "=", "AND");
        if (Util.asString(statutSearchVente)) {
            p = new ParametreRequete("y.piece.statut", "statut", statutSearchVente.charAt(0), operationSearchVente, "AND");
        }
        paginator_vente.addParam(p);
        loadAll(true, true);
        actualiseView();
    }

    public void addParamStatutStock() {
        ParametreRequete p = new ParametreRequete("y.piece.statut", "statut", null, "=", "AND");
        if (Util.asString(statutSearchStock)) {
            p = new ParametreRequete("y.piece.statut", "statut", statutSearchStock.charAt(0), operationSearchStock, "AND");
        }
        paginator_stock.addParam(p);
        loadAll(true, true);
        actualiseView();
    }

    public void addParamStatutRegleVente() {
        ParametreRequete p = new ParametreRequete("y.piece.statutRegle", "statutRegle", null, "=", "AND");
        if (Util.asString(regleSearchVente)) {
            p = new ParametreRequete("y.piece.statutRegle", "statutRegle", regleSearchVente.charAt(0), operationRegleSearchVente, "AND");
        }
        paginator_vente.addParam(p);
        loadAll(true, true);
        actualiseView();
    }

    public void addParamStatutRegleStock() {
        ParametreRequete p = new ParametreRequete("y.piece.statutRegle", "statutRegle", null, "=", "AND");
        if (Util.asString(regleSearchStock)) {
            p = new ParametreRequete("y.piece.statutRegle", "statutRegle", regleSearchStock.charAt(0), operationRegleSearchStock, "AND");
        }
        paginator_stock.addParam(p);
        loadAll(true, true);
        actualiseView();
    }

    public void addParamComptabiliseVente() {
        ParametreRequete p = new ParametreRequete("y.id", "comptabilise", null, "=", "AND");
        if (comptabiliseSearchVente != null) {
            String query = "SELECT ecart FROM yvs_grh_retenue_ecart_vente r INNER JOIN yvs_grh_element_additionel e ON r.retenue = e.id INNER JOIN yvs_grh_type_element_additionel t ON e.type_element = t.id WHERE e.comptabilise IS TRUE AND t.societe = ?";
            List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
            if (ids.isEmpty()) {
                ids.add(-1L);
            }
            p = new ParametreRequete("y.id", "comptabilise", ids, (comptabiliseSearchVente ? "IN" : "NOT IN"), "AND");
        }
        paginator_vente.addParam(p);
        loadAll(true, true);
        actualiseView();
    }

    public void addParamComptabiliseStock() {
        ParametreRequete p = new ParametreRequete("y.id", "comptabilise", null, "=", "AND");
        if (comptabiliseSearchStock != null) {
            String query = "SELECT ecart FROM yvs_grh_retenue_ecart_stock r INNER JOIN yvs_grh_element_additionel e ON r.retenue = e.id INNER JOIN yvs_grh_type_element_additionel t ON e.type_element = t.id WHERE e.comptabilise IS TRUE AND t.societe = ?";
            List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
            if (ids.isEmpty()) {
                ids.add(-1L);
            }
            p = new ParametreRequete("y.id", "comptabilise", ids, (comptabiliseSearchStock ? "IN" : "NOT IN"), "AND");
        }
        paginator_stock.addParam(p);
        loadAll(true, true);
        actualiseView();
    }

    public void onGenererRetenue() {
        tabIds = "";
        if (!autoriser("ret_generer_by_ecart")) {
            openNotAcces();
            return;
        }
        if (ecart.getStatut() != Constantes.STATUT_DOC_VALIDE) {
            getErrorMessage("Vous devez au prealable valider l'ecart");
            return;
        }
        if (nature.equals("V")) {
            for (YvsComReglementEcartVente r : ecart.getReglementsVente()) {
                int index = ecartsVente.indexOf(r);
                if (index < 0) {
                    ecartsVente.add(r);
                    index = ecartsVente.indexOf(r);
                }
                if (index > -1) {
                    tabIds = Util.asString(tabIds) ? "-" : "" + index;
                }
            }
        } else {
            for (YvsComReglementEcartStock r : ecart.getReglementsStock()) {
                int index = ecartsStock.indexOf(r);
                if (index < 0) {
                    ecartsStock.add(r);
                    index = ecartsStock.indexOf(r);
                }
                if (index > -1) {
                    tabIds = Util.asString(tabIds) ? "-" : "" + index;
                }
            }
        }
        onGenererRetenueSelected();
    }

    public void onGenererRetenueSelected() {
        if (!autoriser("ret_generer_by_ecart")) {
            openNotAcces();
            return;
        }
        if ((tabIds != null) ? !tabIds.equals("") : false) {
            ManagedRetenue service = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
            if (service != null) {
                service.loadAllTypeElementAddActif();
                service.getElementAdd().getTypeElt().setId(0);
                service.getElementAdd().getPlan().setId(0);
                service.getElementAdd().getListPrelevement().clear();
                openDialog("dlgChoixTypeRet");
                update("zone_choix_retenu_vente");
            }
        }
    }

    public void onGenererRetenueVente(YvsComReglementEcartVente y) {
        try {
            if (!autoriser("ret_generer_by_ecart")) {
                openNotAcces();
                return;
            }
            if (!y.getPiece().getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                getErrorMessage("Vous devez au prealable valider l'ecart");
                return;
            }
            selectReglementVente = y;
            montantRetenue = y.getMontant();
            tabIds = "";
            if (y != null ? y.getId() > 0 : false) {
                YvsGrhRetenueEcartVente r = (YvsGrhRetenueEcartVente) dao.loadOneByNameQueries("YvsGrhRetenueEcartVente.findByEcart", new String[]{"ecart"}, new Object[]{y});
                if (r != null ? r.getId() > 0 : false) {
                    getInfoMessage("Cette pièce est déjà associée à une retenue");
                } else {
                    ManagedRetenue service = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
                    if (service != null) {
                        service.loadAllTypeElementAddActif();
                        service.getElementAdd().getTypeElt().setId(0);
                        service.getElementAdd().getPlan().setId(0);
                        service.getElementAdd().getListPrelevement().clear();
                        openDialog("dlgChoixTypeRet");
                        update("zone_choix_retenu_vente");
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onGenererRetenueStock(YvsComReglementEcartStock y) {
        try {
            if (!autoriser("ret_generer_by_ecart")) {
                openNotAcces();
                return;
            }
            if (!y.getPiece().getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                getErrorMessage("Vous devez au prealable valider l'ecart");
                return;
            }
            selectReglementStock = y;
            montantRetenue = y.getMontant();
            tabIds = "";
            if (y != null ? y.getId() > 0 : false) {
                YvsGrhRetenueEcartStock r = (YvsGrhRetenueEcartStock) dao.loadOneByNameQueries("YvsGrhRetenueEcartStock.findByEcart", new String[]{"ecart"}, new Object[]{y});
                if (r != null ? r.getId() > 0 : false) {
                    getInfoMessage("Cette pièce est déjà associée à une retenue");
                } else {
                    ManagedRetenue service = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
                    if (service != null) {
                        service.loadAllTypeElementAddActif();
                        service.getElementAdd().getTypeElt().setId(0);
                        service.getElementAdd().getPlan().setId(0);
                        service.getElementAdd().getListPrelevement().clear();
                        openDialog("dlgChoixTypeRet");
                        update("zone_choix_retenu_vente");
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onValideRetenueSelected() {
        if ((tabIds != null) ? !tabIds.equals("") : false) {
            List<Integer> ids = decomposeSelection(tabIds);
            if (nature.equals("V")) {
                for (Integer index : ids) {
                    if (ecartsVente.get(index).getRetenue() != null ? ecartsVente.get(index).getRetenue().getId() < 1 : true) {
                        onValideRetenue(ecartsVente.get(index), null, true);
                    }
                }
            } else {
                for (Integer index : ids) {
                    if (ecartsStock.get(index).getRetenue() != null ? ecartsStock.get(index).getRetenue().getId() < 1 : true) {
                        onValideRetenue(null, ecartsStock.get(index), true);
                    }
                }
            }
        }
    }

    public void onValideRetenue() {
        if ((tabIds != null) ? !tabIds.equals("") : false) {
            onValideRetenueSelected();
        } else {
            onValideRetenue(selectReglementVente, selectReglementStock, false);
        }
    }

    public void onValideRetenue(YvsComReglementEcartVente selectReglementVente, YvsComReglementEcartStock selectReglementStock, boolean planifier) {
        try {
            ManagedRetenue service = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
            if (service != null) {
                if ((service.getElementAdd().getPlan().getId()) <= 0 || service.getElementAdd().getTypeElt().getId() <= 0) {
                    getErrorMessage("Formulaire incorrecte !");
                    return;
                }
                double montant = 0;
                YvsGrhEmployes employe = null;
                if (nature.equals("V")) {
                    employe = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByUsers", new String[]{"user"}, new Object[]{selectReglementVente.getPiece().getUsers()});;
                    if (employe != null ? employe.getId() < 1 : true) {
                        getErrorMessage("Le vendeur " + selectReglementVente.getPiece().getUsers().getNomUsers() + " n'a pas de compte employé");
                        return;
                    }
                    montant = selectReglementVente.getMontant();
                } else {
                    employe = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCompteTiers", new String[]{"tiers", "societe"}, new Object[]{selectReglementStock.getPiece().getTiers(), currentAgence.getSociete()});;
                    if (employe != null ? employe.getId() < 1 : true) {
                        getErrorMessage("Le tiers " + selectReglementStock.getPiece().getTiers().getNom_prenom() + " n'a pas de compte employé");
                        return;
                    }
                    montant = selectReglementStock.getMontant();
                }
                if (employe.getContrat() != null ? employe.getContrat().getId() < 1 : true) {
                    getErrorMessage("L'employé " + employe.getNom_prenom() + " n'a pas de contrat");
                    return;
                }
                service.getElementAdd().setContrat(new ContratEmploye(employe.getContrat().getId(), employe.getContrat().getReferenceContrat()));
                //enretistre la retenu
                YvsGrhElementAdditionel element = service.buildElementAdditionel(service.getElementAdd());
                if (element != null && (selectReglementVente != null || selectReglementStock != null)) {
                    if (planifier) {
                        service.placerRetenu(montant);
                        element.setMontantElement(montant);
                    }
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
                    if (nature.equals("V") ? selectReglementVente != null : false) {
                        YvsGrhRetenueEcartVente retenue = new YvsGrhRetenueEcartVente();
                        retenue.setRetenue(element);
                        retenue.setAuthor(currentUser);
                        retenue.setEcart(selectReglementVente);
                        selectReglementVente.setRetenue((YvsGrhRetenueEcartVente) dao.save1(retenue));
                        selectReglementVente.setStatut(Constantes.STATUT_DOC_PAYER);
                        dao.update(selectReglementVente);
                        equilibreEcartVente(selectReglementVente.getPiece(), false);

                        if (selectEcartVente != null ? selectEcartVente.getReglements() != null : false) {
                            int index = selectEcartVente.getReglements().indexOf(selectReglementVente);
                            if (index > -1) {
                                selectEcartVente.getReglements().set(index, selectReglementVente);
                            }
                        }

                        int index = ecart.getReglementsVente().indexOf(selectReglementVente);
                        if (index > -1) {
                            ecart.getReglementsVente().set(index, selectReglementVente);
                            update("data-reglement_ecart_vente");
                        }

                        index = ecartsVente.indexOf(selectReglementVente);
                        if (index > -1) {
                            ecartsVente.set(index, selectReglementVente);
                            update("data-ecart_vente");
                        }
                    } else if (selectReglementStock != null) {
                        YvsGrhRetenueEcartStock retenue = new YvsGrhRetenueEcartStock();
                        retenue.setRetenue(element);
                        retenue.setAuthor(currentUser);
                        retenue.setEcart(selectReglementStock);
                        selectReglementStock.setRetenue((YvsGrhRetenueEcartStock) dao.save1(retenue));
                        selectReglementStock.setStatut(Constantes.STATUT_DOC_PAYER);
                        dao.update(selectReglementStock);
                        equilibreEcartStock(selectReglementStock.getPiece(), false);

                        if (selectEcartStock != null ? selectEcartStock.getReglements() != null : false) {
                            int index = selectEcartStock.getReglements().indexOf(selectReglementStock);
                            if (index > -1) {
                                selectEcartStock.getReglements().set(index, selectReglementStock);
                            }
                        }

                        int index = ecart.getReglementsStock().indexOf(selectReglementStock);
                        if (index > -1) {
                            ecart.getReglementsStock().set(index, selectReglementStock);
                            update("data-reglement_ecart_stock");
                        }

                        index = ecartsStock.indexOf(selectReglementStock);
                        if (index > -1) {
                            ecartsStock.set(index, selectReglementStock);
                            update("data-ecart_vente");
                        }
                    }
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        w.comptabiliserRetenue(element, false);
                    }
                }
                succes();
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onDeleteRetenueVente(YvsComReglementEcartVente y) {
        try {
            if (!autoriser("ret_generer_by_ecart")) {
                openNotAcces();
                return;
            }
            if (y != null ? (y.getId() > 0 ? y.getRetenue() != null ? y.getRetenue().getId() > 0 : false : false) : false) {
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
                    dao.delete(y.getRetenue());
                    y.setRetenue(null);
                    succes();

                    int index = ecart.getReglementsVente().indexOf(y);
                    if (index > -1) {
                        ecart.getReglementsVente().set(index, y);
                        update("data-reglement_ecart_vente");
                    }

                    index = selectEcartVente.getReglements().indexOf(y);
                    if (index > -1) {
                        selectEcartVente.getReglements().set(index, y);
                    }

                    index = ecartsVente.indexOf(y);
                    if (index > -1) {
                        ecartsVente.set(index, y);
                        update("data-ecart_vente");
                    }
                } else {
                    getErrorMessage("Impossible de supprimer. La retenue est payée ou en cours de paiement");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onDeleteRetenueStock(YvsComReglementEcartStock y) {
        try {
            if (!autoriser("ret_generer_by_ecart")) {
                openNotAcces();
                return;
            }
            if (y != null ? (y.getId() > 0 ? y.getRetenue() != null ? y.getRetenue().getId() > 0 : false : false) : false) {
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
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        w.unComptabiliserRetenue(y.getRetenue().getRetenue(), false);
                    }
                }
                if (delete) {
                    dao.delete(y.getRetenue());
                    y.setRetenue(null);
                    succes();

                    int index = ecart.getReglementsStock().indexOf(y);
                    if (index > -1) {
                        ecart.getReglementsStock().set(index, y);
                        update("data-reglement_ecart_stock");
                    }

                    index = selectEcartStock.getReglements().indexOf(y);
                    if (index > -1) {
                        selectEcartStock.getReglements().set(index, y);
                    }

                    index = ecartsStock.indexOf(y);
                    if (index > -1) {
                        ecartsStock.set(index, y);
                        update("data-ecart_vente");
                    }
                } else {
                    getErrorMessage("Impossible de supprimer. La retenue est payée ou en cours de paiement");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onLoadEcartNoPlanifier() {
        try {
            if (nature.equals("V")) {
                String query = "SELECT y.id FROM yvs_com_ecart_entete_vente y LEFT JOIN yvs_com_reglement_ecart_vente r ON y.id = r.piece INNER JOIN yvs_users u ON y.users = u.id WHERE r.id IS NULL AND u.agence = ? ";
                List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getId(), 1)});
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                ecartVenteNoPlanifier = dao.loadNameQueries("YvsComEcartEnteteVente.findByIds", new String[]{"ids"}, new Object[]{ids});
            } else {
                String query = "SELECT y.id FROM yvs_com_doc_stocks_ecart y LEFT JOIN yvs_com_reglement_ecart_stock r ON y.id = r.piece INNER JOIN yvs_users u ON y.users = u.id WHERE r.id IS NULL AND u.agence = ? ";
                List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getId(), 1)});
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                ecartStockNoPlanifier = dao.loadNameQueries("YvsComEcartEnteteVente.findByIds", new String[]{"ids"}, new Object[]{ids});

            }
            update("data-no_planifier_ecart_vente");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onGenererReglementVente() {
        try {
            for (YvsComEcartEnteteVente y : ecartVenteNoPlanifier) {
                onGenererReglementVente(y, false);
            }
            succes();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onGenererReglementStock() {
        try {
            for (YvsComDocStocksEcart y : ecartStockNoPlanifier) {
                onGenererReglementStock(y, false);
            }
            succes();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public YvsComReglementEcartVente onGenererReglementVente(YvsComEcartEnteteVente y, boolean msg) {
        try {
            YvsComReglementEcartVente r = new YvsComReglementEcartVente();
            r.setNumero("PC-" + y.getNumero() + "-0" + (y.getReglements().size() + 1));
            r.setAuthor(currentUser);
            r.setDateReglement(y.getDateEcart());
            r.setMontant(y.getMontant());
            r.setPiece(y);
            r.setStatut(Constantes.STATUT_DOC_ATTENTE);
            r = (YvsComReglementEcartVente) dao.save1(r);
            y.getReglements().add(r);
            ecartsVente.add(0, r);
            if (msg) {
                succes();
            }
            ecartVenteNoPlanifier.remove(y);
            update("data-ecart_vente");
            update("data-programme_ecart_vente");
            return r;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public YvsComReglementEcartStock onGenererReglementStock(YvsComDocStocksEcart y, boolean msg) {
        try {
            double montant = (selectEcartStock.getTaux() * ecart.getInventaire().getMontantTotal()) / 100;
            YvsComReglementEcartStock r = new YvsComReglementEcartStock();
            r.setNumero("PC-" + y.getNumero() + "-0" + (y.getReglements().size() + 1));
            r.setAuthor(currentUser);
            r.setDateReglement(y.getDocStock().getDateDoc());
            r.setMontant(montant);
            r.setPiece(y);
            r.setStatut(Constantes.STATUT_DOC_ATTENTE);
            r = (YvsComReglementEcartStock) dao.save1(r);
            y.getReglements().add(r);
            ecartsStock.add(0, r);
            if (msg) {
                succes();
            }
            ecartStockNoPlanifier.remove(y);
            update("data-ecart_vente");
            update("data-programme_ecart_vente");
            return r;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void onBuildProgramm() {
        try {
            ecartTotal = 0;
            programmes.clear();
            String query = "SELECT u.id, u.code_users, u.nom_users, c.id, c.libelle, a.id, a.codeagence FROM yvs_users u "
                    + "INNER JOIN yvs_agences a ON u.agence = a.id LEFT JOIN yvs_com_categorie_personnel c ON u.categorie = c.id "
                    + "WHERE u.actif IS TRUE AND a.societe = ?";
            List<Options> params = new ArrayList<>();
            params.add(new Options(currentAgence.getSociete().getId(), params.size() + 1));
            if (agenceTiers > 0) {
                query += " AND u.agence = ?";
                params.add(new Options(agenceTiers, params.size() + 1));
            }
            query += " ORDER BY u.nom_users";
            List<Object[]> result = dao.loadListBySqlQuery(query, params.toArray(new Options[params.size()]));
            YvsUsers users;
            YvsComEcartEnteteVente ecart;
            Date first = (Date) dao.loadObjectByNameQueries("YvsComptaMouvementCaisse.findFirstDate", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            for (Object[] data : result) {
                users = new YvsUsers((Long) data[0], (String) data[1], (String) data[2]);
                users.setCategorie(new YvsComCategoriePersonnel((Long) data[3], (String) data[4]));
                users.setAgence(new YvsAgences((Long) data[5], (String) data[6], null));
                Date dateDebut = (Date) dao.loadObjectByNameQueries("YvsComEcartEnteteVente.findLastDateByUsers", new String[]{"users"}, new Object[]{users});
                if (dateDebut == null) {
                    dateDebut = first;
                }
                ecart = new YvsComEcartEnteteVente(YvsComEcartEnteteVente.ids--);
                ecart.setDateDebut(dateDebut);
                ecart.setDateEcart(this.ecart.getDateEcart());
                ecart.setAuthor(currentUser);
                ecart.setUsers(users);
                ecart.setAgence(users.getAgence());
                double versementAttendu = dao.loadVersementAttendu(ecart.getUsers().getId(), ecart.getDateDebut(), ecart.getDateEcart());
                double versementReel = dao.loadVersementReel(ecart.getUsers().getId(), ecart.getDateDebut(), ecart.getDateEcart());
                double montant = versementAttendu - versementReel;
                if (montant > 0) {
                    ecart.setMontant(montant);
                    ecartTotal += montant;
                    programmes.add(ecart);
                }
            }
            filters = new ArrayList<>(programmes);
            update("data-programme_ecart_vente");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onSearchUsers() {
        boolean execute = false;
        List<YvsComEcartEnteteVente> list = new ArrayList<>();
        if (Util.asString(vendeurSearch)) {
            String search = vendeurSearch.replace("%", "").toUpperCase();
            for (YvsComEcartEnteteVente e : filters) {
                if (vendeurSearch.startsWith("%") ? e.getUsers().getNomUsers().toUpperCase().endsWith(search) : false) {
                    list.add(e);
                } else if (vendeurSearch.endsWith("%") ? e.getUsers().getNomUsers().toUpperCase().startsWith(search) : false) {
                    list.add(e);
                } else if ((vendeurSearch.startsWith("%") && vendeurSearch.endsWith("%")) ? e.getUsers().getNomUsers().toUpperCase().contains(search) : false) {
                    list.add(e);
                } else if (e.getUsers().getNomUsers().toUpperCase().equals(search)) {
                    list.add(e);
                }
            }
            execute = true;
        }
        if (groupeSearch > 0) {
            if (!execute) {
                programmes = new ArrayList<>(filters);
            } else {
                programmes = new ArrayList<>(list);
            }
            list.clear();
            for (YvsComEcartEnteteVente e : programmes) {
                if (e.getUsers().getCategorie() != null ? (e.getUsers().getCategorie().getId() > 0 ? e.getUsers().getCategorie().getId().equals(groupeSearch) : false) : false) {
                    list.add(e);
                }
            }
            execute = true;
        }
        if (!execute) {
            programmes = new ArrayList<>(filters);
        } else {
            programmes = new ArrayList<>(list);
        }
        update("data-programme_ecart_vente");
    }

    public void equilibreEcart() {
        if (nature.equals("V")) {
            equilibreEcartVente(selectEcartVente, true);
        } else {
            equilibreEcartStock(selectEcartStock, true);
        }
    }

    public void equilibreAllEcart() {
        if ((tabIds != null) ? !tabIds.equals("") : false) {
            List<Integer> ids = decomposeSelection(tabIds);
            if (nature.equals("V")) {
                List<YvsComEcartEnteteVente> list = new ArrayList<>();
                for (Integer index : ids) {
                    if (!list.contains(ecartsVente.get(index).getPiece())) {
                        equilibreEcartVente(ecartsVente.get(index).getPiece(), false);
                        list.add(ecartsVente.get(index).getPiece());
                    }
                }
            } else {
                List<YvsComDocStocksEcart> list = new ArrayList<>();
                for (Integer index : ids) {
                    if (!list.contains(ecartsStock.get(index).getPiece())) {
                        equilibreEcartStock(ecartsStock.get(index).getPiece(), false);
                        list.add(ecartsStock.get(index).getPiece());
                    }
                }
            }
            succes();
        }
    }

    public void equilibreEcartVente(YvsComEcartEnteteVente y, boolean msg) {
        try {
            Double regle = (Double) dao.loadObjectByNameQueries("YvsComReglementEcartVente.sumByPiecePaye", new String[]{"piece"}, new Object[]{y});
            char statut = Constantes.STATUT_DOC_ATTENTE;
            if (regle != null ? regle > 0 : false) {
                if (regle >= y.getMontant()) {
                    statut = Constantes.STATUT_DOC_PAYER;
                } else if (regle > 0) {
                    statut = Constantes.STATUT_DOC_ENCOUR;
                }
            }
            if (!y.getStatutRegle().equals(statut)) {
                y.setStatutRegle(statut);
                dao.update(y);
            }
            if (msg) {
                succes();
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void equilibreEcartStock(YvsComDocStocksEcart y, boolean msg) {
        try {
            Double regle = (Double) dao.loadObjectByNameQueries("YvsComReglementEcartStock.sumByPiecePaye", new String[]{"piece"}, new Object[]{y});
            char statut = Constantes.STATUT_DOC_ATTENTE;
            if (regle != null ? regle > 0 : false) {
                double montant = 0;
                if (y.getDocStock().getValeur() != null ? y.getDocStock().getValeur().getMontant() > 0 : false) {
                    montant = y.getDocStock().getValeur().getMontant();
                } else {
                    montant = y.getDocStock().calculMontantTotal(dao, false);
                }
                montant = ((y.getTaux() * montant) / 100);
                if (regle >= (montant - 1)) {
                    statut = Constantes.STATUT_DOC_PAYER;
                } else if (regle > 0) {
                    statut = Constantes.STATUT_DOC_ENCOUR;
                }
            }
            if (!y.getStatutRegle().equals(statut)) {
                y.setStatutRegle(statut);
                dao.update(y);
            }
            if (msg) {
                succes();
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

}

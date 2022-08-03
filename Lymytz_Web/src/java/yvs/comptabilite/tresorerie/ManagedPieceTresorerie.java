package yvs.comptabilite.tresorerie;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.commercial.tresorerie;
//
//import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.SessionScoped;
//import javax.faces.event.ValueChangeEvent;
//import org.primefaces.event.SelectEvent;
//import org.primefaces.event.UnselectEvent;
//import yvs.base.compta.UtilCompta;
//import yvs.commercial.UtilCom;
//import yvs.commercial.achat.DocAchat;
//import yvs.commercial.achat.MensualiteFactureAchat;
//import yvs.commercial.fournisseur.Fournisseur;
//import yvs.commercial.vente.DocVente;
//import yvs.commercial.vente.MensualiteFactureVente;
//import yvs.dao.Options;
//import yvs.entity.base.YvsComptaCaisseDocDivers;
//import yvs.entity.base.YvsBaseFournisseur;
//import yvs.entity.base.YvsComptaCaisseMensualiteDocDivers;
//import yvs.entity.commercial.achat.YvsComDocAchats;
//import yvs.entity.commercial.achat.YvsComMensualiteFactureAchat;
//import yvs.entity.commercial.client.YvsComClient;
//import yvs.entity.commercial.vente.YvsComDocVentes;
//import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
//import yvs.entity.param.YvsModePaiement;
//import yvs.entity.tiers.YvsBaseTiers;
//import yvs.base.tresoreri.ModePaiement;
//import yvs.base.tiers.Tiers;
//import yvs.entity.base.YvsComptaCaissePieceDivers;
//import yvs.theme.ClientService;
//import yvs.theme.FournisseurService;
//import yvs.theme.TiersService;
//import yvs.util.Constantes;
//import yvs.util.Managed;
//
///**
// *
// * @author lymytz
// */
//@ManagedBean
//@SessionScoped
//public class ManagedPieceTresorerie extends Managed<PieceTresorerie, YvsBaseCaisse> implements Serializable {
//
//    private PieceTresorerie piece = new PieceTresorerie();
//    private List<YvsComptaCaissePieceDivers> pieces, historiques;
//    private YvsComptaCaissePieceDivers selectPiece;
//
//    private YvsComDocAchats achat = new YvsComDocAchats();
//    private List<YvsComDocAchats> achats;
//    private List<YvsComMensualiteFactureAchat> mensualites_achat;
//    private YvsComMensualiteFactureAchat selectMensualite_achat;
//    private YvsComDocVentes vente = new YvsComDocVentes();
//    private List<YvsComDocVentes> ventes;
//    private List<YvsComMensualiteFactureVente> mensualites_vente;
//    private YvsComMensualiteFactureVente selectMensualite_vente;
//    private YvsComptaCaisseDocDivers diver = new YvsComptaCaisseDocDivers();
//    private List<YvsComptaCaisseDocDivers> divers;
//    private List<YvsComptaCaisseMensualiteDocDivers> mensualites_diver;
//    private YvsComptaCaisseMensualiteDocDivers selectMensualite_diver;
//
//    private List<YvsModePaiement> modes;
//
//    @ManagedProperty(value = "#{tiersService}")
//    private TiersService tiersService;
//    private List<YvsBaseTiers> tiers;
//    private YvsBaseTiers tiersSelect;
//
//    @ManagedProperty(value = "#{fournisseurService}")
//    private FournisseurService fournisseurService;
//    private List<YvsBaseFournisseur> fournisseurs;
//    private Fournisseur fournisseurSelect;
//
//    @ManagedProperty(value = "#{clientService}")
//    private ClientService clientService;
//    private List<YvsComClient> clients;
//    private YvsComClient clientSelect;
//
//    private boolean inSolde;
//    private Date dateDebutSearch = new Date(), dateFinSearch = new Date();
//    private String tabIds, eltSearch;
//
//    //
//    private long id;
//    private String num = "----------";
//    private Date date = new Date();
//    private double TTC = 0, netApayer, avance;
//
//    public ManagedPieceTresorerie() {
//        pieces = new ArrayList<>();
//        achats = new ArrayList<>();
//        ventes = new ArrayList<>();
//        divers = new ArrayList<>();
//        mensualites_achat = new ArrayList<>();
//        mensualites_vente = new ArrayList<>();
//        mensualites_diver = new ArrayList<>();
//        historiques = new ArrayList<>();
//        modes = new ArrayList<>();
//        tiers = new ArrayList<>();
//        fournisseurs = new ArrayList<>();
//        clients = new ArrayList<>();
//    }
//
//    public YvsComMensualiteFactureAchat getSelectMensualite_achat() {
//        return selectMensualite_achat;
//    }
//
//    public void setSelectMensualite_achat(YvsComMensualiteFactureAchat selectMensualite_achat) {
//        this.selectMensualite_achat = selectMensualite_achat;
//    }
//
//    public YvsComMensualiteFactureVente getSelectMensualite_vente() {
//        return selectMensualite_vente;
//    }
//
//    public void setSelectMensualite_vente(YvsComMensualiteFactureVente selectMensualite_vente) {
//        this.selectMensualite_vente = selectMensualite_vente;
//    }
//
//    public YvsComptaCaisseMensualiteDocDivers getSelectMensualite_diver() {
//        return selectMensualite_diver;
//    }
//
//    public void setSelectMensualite_diver(YvsComptaCaisseMensualiteDocDivers selectMensualite_diver) {
//        this.selectMensualite_diver = selectMensualite_diver;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public double getAvance() {
//        return avance;
//    }
//
//    public void setAvance(double avance) {
//        this.avance = avance;
//    }
//
//    public String getNum() {
//        return num;
//    }
//
//    public void setNum(String num) {
//        this.num = num;
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
//
//    public double getTTC() {
//        return TTC;
//    }
//
//    public void setTTC(double TTC) {
//        this.TTC = TTC;
//    }
//
//    public double getNetApayer() {
//        netApayer = (TTC - avance) > 0 ? (TTC - avance) : 0;
//        return netApayer;
//    }
//
//    public void setNetApayer(double netApayer) {
//        this.netApayer = netApayer;
//    }
//
//    public List<YvsComMensualiteFactureAchat> getMensualites_achat() {
//        return mensualites_achat;
//    }
//
//    public void setMensualites_achat(List<YvsComMensualiteFactureAchat> mensualites_achat) {
//        this.mensualites_achat = mensualites_achat;
//    }
//
//    public List<YvsComMensualiteFactureVente> getMensualites_vente() {
//        return mensualites_vente;
//    }
//
//    public void setMensualites_vente(List<YvsComMensualiteFactureVente> mensualites_vente) {
//        this.mensualites_vente = mensualites_vente;
//    }
//
//    public List<YvsComptaCaisseMensualiteDocDivers> getMensualites_diver() {
//        return mensualites_diver;
//    }
//
//    public void setMensualites_diver(List<YvsComptaCaisseMensualiteDocDivers> mensualites_diver) {
//        this.mensualites_diver = mensualites_diver;
//    }
//
//    public YvsComptaCaissePieceDivers getSelectPiece() {
//        return selectPiece;
//    }
//
//    public void setSelectPiece(YvsComptaCaissePieceDivers selectPiece) {
//        this.selectPiece = selectPiece;
//    }
//
//    public FournisseurService getFournisseurService() {
//        return fournisseurService;
//    }
//
//    public void setFournisseurService(FournisseurService fournisseurService) {
//        this.fournisseurService = fournisseurService;
//    }
//
//    public List<YvsBaseFournisseur> getFournisseurs() {
//        return fournisseurs;
//    }
//
//    public void setFournisseurs(List<YvsBaseFournisseur> fournisseurs) {
//        this.fournisseurs = fournisseurs;
//    }
//
//    public Fournisseur getFournisseurSelect() {
//        return fournisseurSelect;
//    }
//
//    public void setFournisseurSelect(Fournisseur fournisseurSelect) {
//        this.fournisseurSelect = fournisseurSelect;
//    }
//
//    public ClientService getClientService() {
//        return clientService;
//    }
//
//    public void setClientService(ClientService clientService) {
//        this.clientService = clientService;
//    }
//
//    public List<YvsComClient> getClients() {
//        return clients;
//    }
//
//    public void setClients(List<YvsComClient> clients) {
//        this.clients = clients;
//    }
//
//    public YvsComClient getClientSelect() {
//        return clientSelect;
//    }
//
//    public void setClientSelect(YvsComClient clientSelect) {
//        this.clientSelect = clientSelect;
//    }
//
//    public TiersService getTiersService() {
//        return tiersService;
//    }
//
//    public void setTiersService(TiersService tiersService) {
//        this.tiersService = tiersService;
//    }
//
//    public List<YvsBaseTiers> getTiers() {
//        return tiers;
//    }
//
//    public void setTiers(List<YvsBaseTiers> tiers) {
//        this.tiers = tiers;
//    }
//
//    public YvsBaseTiers getTiersSelect() {
//        return tiersSelect;
//    }
//
//    public void setTiersSelect(YvsBaseTiers tiersSelect) {
//        this.tiersSelect = tiersSelect;
//    }
//
//    public Date getDateFinSearch() {
//        return dateFinSearch;
//    }
//
//    public void setDateFinSearch(Date dateFinSearch) {
//        this.dateFinSearch = dateFinSearch;
//    }
//
//    public Date getDateDebutSearch() {
//        return dateDebutSearch;
//    }
//
//    public void setDateDebutSearch(Date dateDebutSearch) {
//        this.dateDebutSearch = dateDebutSearch;
//    }
//
//    public String getEltSearch() {
//        return eltSearch;
//    }
//
//    public void setEltSearch(String eltSearch) {
//        this.eltSearch = eltSearch;
//    }
//
//    public boolean isInSolde() {
//        return inSolde;
//    }
//
//    public void setInSolde(boolean inSolde) {
//        this.inSolde = inSolde;
//    }
//
//    public List<YvsModePaiement> getModes() {
//        return modes;
//    }
//
//    public void setModes(List<YvsModePaiement> modes) {
//        this.modes = modes;
//    }
//
//    public List<YvsComptaCaissePieceDivers> getHistoriques() {
//        return historiques;
//    }
//
//    public void setHistoriques(List<YvsComptaCaissePieceDivers> historiques) {
//        this.historiques = historiques;
//    }
//
//    public YvsComDocAchats getAchat() {
//        return achat;
//    }
//
//    public void setAchat(YvsComDocAchats achat) {
//        this.achat = achat;
//    }
//
//    public YvsComDocVentes getVente() {
//        return vente;
//    }
//
//    public void setVente(YvsComDocVentes vente) {
//        this.vente = vente;
//    }
//
//    public YvsComptaCaisseDocDivers getDiver() {
//        return diver;
//    }
//
//    public void setDiver(YvsComptaCaisseDocDivers diver) {
//        this.diver = diver;
//    }
//
//    public PieceTresorerie getPiece() {
//        return piece;
//    }
//
//    public void setPiece(PieceTresorerie piece) {
//        this.piece = piece;
//    }
//
//    public List<YvsComptaCaissePieceDivers> getPieces() {
//        return pieces;
//    }
//
//    public void setPieces(List<YvsComptaCaissePieceDivers> pieces) {
//        this.pieces = pieces;
//    }
//
//    public List<YvsComDocAchats> getAchats() {
//        return achats;
//    }
//
//    public void setAchats(List<YvsComDocAchats> achats) {
//        this.achats = achats;
//    }
//
//    public List<YvsComDocVentes> getVentes() {
//        return ventes;
//    }
//
//    public void setVentes(List<YvsComDocVentes> ventes) {
//        this.ventes = ventes;
//    }
//
//    public List<YvsComptaCaisseDocDivers> getDivers() {
//        return divers;
//    }
//
//    public void setDivers(List<YvsComptaCaisseDocDivers> divers) {
//        this.divers = divers;
//    }
//
//    public String getTabIds() {
//        return tabIds;
//    }
//
//    public void setTabIds(String tabIds) {
//        this.tabIds = tabIds;
//    }
//
//    @Override
//    public void loadAll() {
//        loadAllPieces();
//        loadAllModes();
//        loadAllAchats();
//        loadAllFournisseur();
//        load();
//    }
//
//    public void load() {
////        if (piece.getMontant() < 1) {
////            numSearch_ = "";
////        }
////        indiceNumsearch_ = genererPrefixe(Constantes.TYPE_OD_NAME, piece.getDatePiece(), 0, 0);
//    }
//
//    public void loadAllPieces() {
////        pieces = loadPieces();
//        
//        update("data_piece_tresor");
//    }
//
//    public void loadAllModes() {
//        nameQueri = "YvsModePaiement.findAll";
//        champ = new String[]{};
//        val = new Object[]{};
//        modes = dao.loadNameQueries(nameQueri, champ, val);
//    }
//
//    public void loadAllTiers() {
//        tiersService.getTiers().clear();
//        champ = new String[]{"societe"};
//        val = new Object[]{currentScte};
//        nameQueri = "YvsBaseTiers.findAlls";
//        tiers = dao.loadNameQueries(nameQueri, champ, val);
//        tiersService.getTiers().addAll(tiers);
//    }
//
//    public void loadAllClient() {
//        clientService.getClients().clear();
//        champ = new String[]{"societe"};
//        val = new Object[]{currentScte};
//        nameQueri = "YvsComClient.findAll";
//        clients = dao.loadNameQueries(nameQueri, champ, val);
//        clientService.getClients().addAll(clients);
//    }
//
//    public void loadAllFournisseur() {
//        fournisseurService.getFournisseurs().clear();
//        champ = new String[]{"societe"};
//        val = new Object[]{currentScte};
//        nameQueri = "YvsBaseFournisseur.findAll";
//        fournisseurs = dao.loadNameQueries(nameQueri, champ, val);
//        fournisseurService.getFournisseurs().addAll(fournisseurs);
//    }
//
//    public void loadAllAchats() {
//        if (isInSolde()) {
//            if (currentUser.getUsers().getAccesMultiAgence()) {
//                champ = new String[]{"societe", "s1", "s2", "s3", "typeDoc"};
//                val = new Object[]{currentScte, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.ETAT_REGLE, Constantes.TYPE_FA};
//                nameQueri = "YvsComDocAchats.findByTypeStatut3";
//            } else {
//                if (currentUser.getUsers().isSuperAdmin()) {
//                    champ = new String[]{"agence", "s1", "s2", "s3", "typeDoc"};
//                    val = new Object[]{currentAgence, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.ETAT_REGLE, Constantes.TYPE_FA};
//                    nameQueri = "YvsComDocAchats.findByAgenceStatut3";
//                } else {
//                    champ = new String[]{"depot", "s1", "s2", "s3", "typeDoc"};
//                    val = new Object[]{currentDepot, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.ETAT_REGLE, Constantes.TYPE_FA};
//                    nameQueri = "YvsComDocAchats.findByDepotStatut3";
//                }
//            }
//        } else {
//            if (currentUser.getUsers().getAccesMultiAgence()) {
//                champ = new String[]{"societe", "s1", "s2", "typeDoc"};
//                val = new Object[]{currentScte, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.TYPE_FA};
//                nameQueri = "YvsComDocAchats.findByTypeStatut2";
//            } else {
//                if (currentUser.getUsers().isSuperAdmin()) {
//                    champ = new String[]{"agence", "s1", "s2", "typeDoc"};
//                    val = new Object[]{currentAgence, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.TYPE_FA};
//                    nameQueri = "YvsComDocAchats.findByAgenceStatut2";
//                } else {
//                    champ = new String[]{"depot", "s1", "s2", "typeDoc"};
//                    val = new Object[]{currentDepot, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.TYPE_FA};
//                    nameQueri = "YvsComDocAchats.findByDepotStatut2";
//                }
//            }
//        }
//        achats = dao.loadNameQueries(nameQueri, champ, val);
//        update("data_achats");
//    }
//
//    public void loadAllMensAchat(YvsComDocAchats y) {
//        champ = new String[]{"facture"};
//        val = new Object[]{y};
//        nameQueri = "YvsComMensualiteFactureAchat.findByFacture";
//        mensualites_achat = dao.loadNameQueries(nameQueri, champ, val);
//        update("data_mensualite_achats");
//    }
//
//    public void loadHistoriquesAchat(YvsComDocAchats d, YvsComMensualiteFactureAchat m) {
//        historiques.clear();
//        if (d != null ? d.getId() > 0 : false) {
//            List<Object> l = dao.loadPieceAchat(d.getId());
//            for (Object o : l) {
//                champ = new String[]{"id"};
//                val = new Object[]{(long) o};
//                nameQueri = "YvsBasePieceTresorerie.findById";
//                YvsComptaCaissePieceDivers p = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries(nameQueri, champ, val);
//                if (p != null ? p.getId() > 0 : false) {
//                    historiques.add(p);
//                }
//            }
//        } else {
//            if (m != null ? m.getId() > 0 : false) {
//                champ = new String[]{"facture"};
//                val = new Object[]{m.getId()};
//                nameQueri = "YvsBasePieceTresorerie.findByAchat";
//                historiques = dao.loadNameQueries(nameQueri, champ, val);
//            }
//        }
//        update("data_piece_tresor_hist");
//    }
//
//    public void loadAllVentes() {
//        if (isInSolde()) {
//            if (currentUser.getUsers().getAccesMultiAgence()) {
//                champ = new String[]{"societe", "s1", "s2", "s3", "typeDoc"};
//                val = new Object[]{currentScte, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.ETAT_REGLE, Constantes.TYPE_FV};
//                nameQueri = "YvsComDocVentes.findByTypeStatut3";
//            } else {
//                if (currentUser.getUsers().isSuperAdmin()) {
//                    champ = new String[]{"agence", "s1", "s2", "s3", "typeDoc"};
//                    val = new Object[]{currentAgence, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.ETAT_REGLE, Constantes.TYPE_FV};
//                    nameQueri = "YvsComDocVentes.findByAgenceStatut3";
//                } else {
//                    champ = new String[]{"depot", "s1", "s2", "s3", "typeDoc"};
//                    val = new Object[]{currentDepot, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.ETAT_REGLE, Constantes.TYPE_FV};
//                    nameQueri = "YvsComDocVentes.findByDepotStatut3";
//                }
//            }
//        } else {
//            if (currentUser.getUsers().getAccesMultiAgence()) {
//                champ = new String[]{"societe", "s1", "s2", "typeDoc"};
//                val = new Object[]{currentScte, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.TYPE_FV};
//                nameQueri = "YvsComDocVentes.findByTypeStatut2";
//            } else {
//                if (currentUser.getUsers().isSuperAdmin()) {
//                    champ = new String[]{"agence", "s1", "s2", "typeDoc"};
//                    val = new Object[]{currentAgence, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.TYPE_FV};
//                    nameQueri = "YvsComDocVentes.findByAgenceStatut2";
//                } else {
//                    champ = new String[]{"depot", "s1", "s2", "typeDoc"};
//                    val = new Object[]{currentDepot, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.TYPE_FV};
//                    nameQueri = "YvsComDocVentes.findByDepotStatut2";
//                }
//            }
//        }
//        ventes = dao.loadNameQueries(nameQueri, champ, val);
//        update("data_ventes");
//    }
//
//    public void loadAllMensVente(YvsComDocVentes y) {
//        champ = new String[]{"facture"};
//        val = new Object[]{y};
//        nameQueri = "YvsComMensualiteFactureVente.findByFacture";
//        mensualites_vente = dao.loadNameQueries(nameQueri, champ, val);
//        update("data_mensualite_ventes");
//    }
//
//    public void loadHistoriquesVente(YvsComDocVentes d, YvsComMensualiteFactureVente m) {
//        historiques.clear();
//        if (d != null ? d.getId() > 0 : false) {
//            List<Object> l = dao.loadPieceVente(d.getId());
//            for (Object o : l) {
//                champ = new String[]{"id"};
//                val = new Object[]{(long) o};
//                nameQueri = "YvsBasePieceTresorerie.findById";
//                YvsComptaCaissePieceDivers p = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries(nameQueri, champ, val);
//                if (p != null ? p.getId() > 0 : false) {
//                    historiques.add(p);
//                }
//            }
//        } else {
//            if (m != null ? m.getId() > 0 : false) {
//                champ = new String[]{"facture"};
//                val = new Object[]{m.getId()};
//                nameQueri = "YvsBasePieceTresorerie.findByVente";
//                historiques = dao.loadNameQueries(nameQueri, champ, val);
//            }
//        }
//        update("data_piece_tresor_hist");
//    }
//
//    public void loadAllDivers() {
//        if (isInSolde()) {
//            if (currentUser.getUsers().getAccesMultiAgence()) {
//                champ = new String[]{"societe", "s1", "s2", "s3", "typeDoc"};
//                val = new Object[]{currentScte, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.ETAT_REGLE, Constantes.TYPE_OD};
//                nameQueri = "YvsBaseDocDivers.findByTypeStatut3";
//            } else {
//                champ = new String[]{"agence", "s1", "s2", "s3", "typeDoc"};
//                val = new Object[]{currentAgence, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.ETAT_REGLE, Constantes.TYPE_OD};
//                nameQueri = "YvsBaseDocDivers.findByAgenceStatut3";
//            }
//        } else {
//            if (currentUser.getUsers().getAccesMultiAgence()) {
//                champ = new String[]{"societe", "s1", "s2", "typeDoc"};
//                val = new Object[]{currentScte, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.TYPE_OD};
//                nameQueri = "YvsBaseDocDivers.findByTypeStatut2";
//            } else {
//                champ = new String[]{"agence", "s1", "s2", "typeDoc"};
//                val = new Object[]{currentAgence, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS, Constantes.TYPE_OD};
//                nameQueri = "YvsBaseDocDivers.findByAgenceStatut2";
//            }
//        }
//        divers = dao.loadNameQueries(nameQueri, champ, val);
//        update("data_divers");
//    }
//
//    public void loadAllMensDiver(YvsComptaCaisseDocDivers y) {
//        champ = new String[]{"docDivers"};
//        val = new Object[]{y};
//        nameQueri = "YvsComptaCaisseMensualiteDocDivers.findByDocDivers";
//        mensualites_diver = dao.loadNameQueries(nameQueri, champ, val);
//        update("data_mensualite_others");
//    }
//
//    public void loadHistoriquesDiver(YvsComptaCaisseDocDivers d, YvsComptaCaisseMensualiteDocDivers m) {
//        historiques.clear();
//        if (d != null ? d.getId() > 0 : false) {
//            List<Object> l = dao.loadPieceOther(d.getId());
//            for (Object o : l) {
//                champ = new String[]{"id"};
//                val = new Object[]{(long) o};
//                nameQueri = "YvsBasePieceTresorerie.findById";
//                YvsComptaCaissePieceDivers p = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries(nameQueri, champ, val);
//                if (p != null ? p.getId() > 0 : false) {
//                    historiques.add(p);
//                }
//            }
//        } else {
//            if (m != null ? m.getId() > 0 : false) {
//                champ = new String[]{"facture"};
//                val = new Object[]{m.getId()};
//                nameQueri = "YvsBasePieceTresorerie.findByOther";
//                historiques = dao.loadNameQueries(nameQueri, champ, val);
//            }
//        }
//        update("data_piece_tresor_hist");
//    }
//
//    public YvsComptaCaissePieceDivers buildPieceTresorerie(PieceTresorerie y) {
//        YvsComptaCaissePieceDivers p = new YvsComptaCaissePieceDivers();
//        if (y != null) {
//            p.setDatePiece((y.getDatePiece() != null) ? y.getDatePiece() : new Date());
//            p.setId(y.getId());
//            p.setMontant(y.getMontant());
//            p.setNumPiece(y.getNumPiece());
//            p.setStatutPiece(y.getStatut());
//            if (currentUser != null ? currentUser.getId() > 0 : false) {
//                p.setAuthor(currentUser);
//            }
//        }
//        return p;
//    }
//
//    @Override
//    public PieceTresorerie recopieView() {
//        PieceTresorerie p = new PieceTresorerie();
//        p.setDatePiece((piece.getDatePiece() != null) ? piece.getDatePiece() : new Date());
//        p.setDescription(piece.getDescription());
//        p.setId(piece.getId());
//        p.setIdExterne(piece.getIdExterne());
//        p.setLibelle(piece.getLibelle());
//        p.setMontant(piece.getMontant());
//        p.setNumPiece(piece.getNumPiece());
//        p.setStatut((piece.getStatut() != null) ? piece.getStatut() : Constantes.ETAT_EDITABLE);
//        p.setNumRef(piece.getNumRef());
//        p.setMontantMens(piece.getMontantMens());
//        p.setMouvement((piece.getMouvement() != null) ? piece.getMouvement() : Constantes.MOUV_CAISS_SORTIE);
//        p.setTableExterne(piece.getTableExterne());
//        p.setOnCompte(piece.isOnCompte());
//        p.setMode(piece.getMode());
//        p.setUpdate(piece.isUpdate());
//        p.setSource(piece.getSource());
//        p.setNameTable(piece.getNameTable());
//        p.setAchat(piece.getAchat());
//        p.setVente(piece.getVente());
//        p.setDivers(piece.getDivers());
//        p.setNew_(true);
//        return p;
//    }
//
//    public PieceTresorerie recopieView(String numPiece) {
//        PieceTresorerie p = new PieceTresorerie();
//        p.setDatePiece((piece.getDatePiece() != null) ? piece.getDatePiece() : new Date());
//        p.setDescription(piece.getDescription());
//        p.setId(piece.getId());
//        p.setIdExterne(piece.getIdExterne());
//        p.setLibelle(piece.getLibelle());
//        p.setMontant(piece.getMontant());
//        p.setNumPiece(numPiece);
//        p.setStatut((piece.getStatut() != null) ? piece.getStatut() : Constantes.ETAT_EDITABLE);
//        p.setNumRef(piece.getNumRef());
//        p.setMontantMens(piece.getMontantMens());
//        p.setMouvement((piece.getMouvement() != null) ? piece.getMouvement() : Constantes.MOUV_CAISS_SORTIE);
//        p.setTableExterne(piece.getTableExterne());
//        p.setOnCompte(piece.isOnCompte());
//        p.setMode(piece.getMode());
//        p.setUpdate(piece.isUpdate());
//        p.setSource(piece.getSource());
//        p.setNameTable(piece.getNameTable());
//        p.setAchat(piece.getAchat());
//        p.setVente(piece.getVente());
//        p.setDivers(piece.getDivers());
//        p.setNew_(true);
//        return p;
//    }
//
//    @Override
//    public boolean controleFiche(PieceTresorerie bean) {
//        if (bean.isUpdate()) {
//            if (bean.getIdExterne() < 1 || bean.getTableExterne() == null || bean.getTableExterne().trim().equals("")) {
//                getErrorMessage("Vous devez specifier la mensualité");
//                return false;
//            }
//        }
//        if ((bean.getMode() != null) ? bean.getMode().getId() < 1 : true) {
//            getErrorMessage("Vous devez specifier le mode de paiement");
//            return false;
//        }
//        if (bean.getNumPiece() == null || bean.getNumPiece().trim().equals("")) {
//            return setNumeroPiece(bean);
//        }
//        if (bean.getLibelle() == null || bean.getLibelle().trim().equals("")) {
//            getErrorMessage("Vous devez entrer le libelle");
//            return false;
//        }
//        if (bean.getMontant() < 1) {
//            getErrorMessage("Vous devez entrer le montant!");
//            return false;
//        }
//        if (bean.getMontant() > getNetApayer()) {
//            getErrorMessage("Vous devez entrer le montant inferieur au net à payer !");
//            return false;
//        }
//        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
//            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifier");
//            return false;
//        }
//        if (bean.isCloturer()) {
//            getErrorMessage("Ce document est deja cloturer");
//            return false;
//        }
////        return writeInExercice(bean.getDateDoc());
//        return true;
//    }
//
//    private boolean _controleFiche_(YvsComptaCaissePieceDivers bean) {
//        if (bean == null) {
//            getErrorMessage("Le devez selectionner un document");
//            return false;
//        }
//        switch (" ") {
//            case Constantes.SCR_ACHAT: {
//                if (achat != null ? (achat.getId() != null ? achat.getId() > 0 : false) : false) {
//                    if (achat.getCloturer()) {
//                        getErrorMessage("Ce document est deja cloturer");
//                        return false;
//                    }
//                    if (achat.getStatut().equals(Constantes.ETAT_ANNULE)) {
//                        getErrorMessage("Ce document est annulé");
//                        return false;
//                    }
//                    double s = dao.sumPieceAchat(achat.getId()) + bean.getMontant();
//                    if (TTC < s) {
//                        getErrorMessage("Impossible de modifier cette pièce car la somme des reglements sera superieur au total du document");
//                        return false;
//                    }
//                }
//                break;
//            }
//            case Constantes.SCR_VENTE: {
//                if (vente != null ? (vente.getId() != null ? vente.getId() > 0 : false) : false) {
//                    if (vente.getCloturer()) {
//                        getErrorMessage("Ce document est deja cloturer");
//                        return false;
//                    }
//                    if (vente.getStatut().equals(Constantes.ETAT_ANNULE)) {
//                        getErrorMessage("Ce document est annulé");
//                        return false;
//                    }
//                    double s = dao.sumPieceVente(vente.getId()) + bean.getMontant();
//                    if (TTC < s) {
//                        getErrorMessage("Impossible de modifier cette pièce car la somme des reglements sera superieur au total du document");
//                        return false;
//                    }
//                }
//                break;
//            }
//            case Constantes.SCR_DIVERS: {
//                if (diver != null ? (diver.getId() != null ? diver.getId() > 0 : false) : false) {
//                   
//                    if (diver.getStatutDoc().equals(Constantes.ETAT_ANNULE)) {
//                        getErrorMessage("Ce document est annulé");
//                        return false;
//                    }
//                    double s = dao.sumPieceOther(diver.getId()) + bean.getMontant();
//                    if (TTC < s) {
//                        getErrorMessage("Impossible de modifier cette pièce car la somme des reglements sera superieur au total du document");
//                        return false;
//                    }
//                }
//                break;
//            }
//            default:
//                break;
//        }
////        return writeInExercice(bean.getDateDoc());
//        return true;
//    }
//
//    private boolean controleFiche_(YvsComptaCaissePieceDivers bean) {
//        if (bean == null) {
//            getErrorMessage("Le devez selectionner un document");
//            return false;
//        }
//        if (!bean.getStatutPiece().equals(Constantes.ETAT_VALIDE)) {
//            getErrorMessage("Le document doit etre validé pour pouvoir etre reglé");
//            return false;
//        }
//        switch (" ") {
//            case Constantes.SCR_ACHAT: {
//                if (achat != null ? (achat.getId() != null ? achat.getId() > 0 : false) : false) {
//                    if (achat.getStatut().equals(Constantes.ETAT_ANNULE)) {
//                        getErrorMessage("Ce document est annulé");
//                        return false;
//                    }
//                }
//                break;
//            }
//            case Constantes.SCR_VENTE: {
//                if (vente != null ? (vente.getId() != null ? vente.getId() > 0 : false) : false) {
//                    if (vente.getStatut().equals(Constantes.ETAT_ANNULE)) {
//                        getErrorMessage("Ce document est annulé");
//                        return false;
//                    }
//                }
//                break;
//            }
//            case Constantes.SCR_DIVERS: {
//                if (diver != null ? (diver.getId() != null ? diver.getId() > 0 : false) : false) {
//                    if (diver.getStatutDoc().equals(Constantes.ETAT_ANNULE)) {
//                        getErrorMessage("Ce document est annulé");
//                        return false;
//                    }
//                }
//                break;
//            }
//            default:
//                break;
//        }
////        return writeInExercice(bean.getDateDoc());
//        return true;
//    }
//
//    @Override
//    public void populateView(PieceTresorerie bean) {
//        cloneObject(piece, bean);
//        if (bean.getSource() != null) {
//            switch (bean.getSource()) {
//                case Constantes.SCR_ACHAT: {
//                    selectMensualite_achat = (YvsComMensualiteFactureAchat) dao.loadOneByNameQueries("YvsComMensualiteFactureAchat.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
//                    loadHistoriquesAchat(null, selectMensualite_achat);
//                    mensualites_achat.clear();
//                    if (selectMensualite_achat != null ? (selectMensualite_achat.getId() != null ? selectMensualite_achat.getId() > 0 : false) : false) {
//                        achat = selectMensualite_achat.getFacture();
//                        if (achat != null) {
//                            loadAllMensAchat(achat);
//                            id = achat.getId();
//                            num = achat.getNumDoc();
//                            date = achat.getDateDoc();
//                            DocAchat a = new DocAchat(id);
//                            TTC = a.getMontantTTC();
//                            avance = a.getMontantAvance();
//                        }
//                    }
//                    if (achats.isEmpty()) {
//                        loadAllAchats();
//                    }
//                    break;
//                }
//                case Constantes.SCR_VENTE: {
//                    selectMensualite_vente = (YvsComMensualiteFactureVente) dao.loadOneByNameQueries("YvsComMensualiteFactureVente.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
//                    loadHistoriquesVente(null, selectMensualite_vente);
//                    mensualites_vente.clear();
//                    if (selectMensualite_vente != null ? (selectMensualite_vente.getId() != null ? selectMensualite_vente.getId() > 0 : false) : false) {
//                        vente = selectMensualite_vente.getFacture();
//                        if (vente != null) {
//                            loadAllMensVente(vente);
//                            id = vente.getId();
//                            num = vente.getNumDoc();
//                            date = vente.getEnteteDoc() != null ? vente.getEnteteDoc().getDateEntete() : new Date();
//                            DocVente a = new DocVente(id);
////                            setMontantTotalDoc(a);
//                            TTC = a.getMontantTTC();
//                            avance = a.getMontantAvance();
//                        }
//                    }
//                    if (ventes.isEmpty()) {
//                        loadAllVentes();
//                    }
//                    break;
//                }
//                case Constantes.SCR_DIVERS: {
//                    selectMensualite_diver = (YvsComptaCaisseMensualiteDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseMensualiteDocDivers.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
//                    loadHistoriquesDiver(null, selectMensualite_diver);
//                    mensualites_diver.clear();
//                    if (selectMensualite_diver != null ? (selectMensualite_diver.getId() != null ? selectMensualite_diver.getId() > 0 : false) : false) {
//                        diver = selectMensualite_diver.getDocDivers();
//                        if (diver != null) {
//                            loadAllMensDiver(diver);
//                            id = diver.getId();
//                            num = diver.getNumPiece();
//                            date = diver.getDateDoc();
//                            DocCaissesDivers a = new DocCaissesDivers(id);
//                            TTC = diver.getMontant();
//                            avance = a.getMontant();
//                        }
//                    }
//                    if (divers.isEmpty()) {
//                        loadAllDivers();
//                    }
//                    break;
//                }
//                default:
//                    break;
//            }
//        }
//        piece.setMontantMens(getNetApayer());
//        update("blog_source");
//    }
//
//    @Override
//    public void resetFiche() {
//        resetFiche_();
//        _resetFiche();
//    }
//
//    public void _resetFiche() {
//        achat = new YvsComDocAchats();
//        vente = new YvsComDocVentes();
//        diver = new YvsComptaCaisseDocDivers();
//
//        id = 0;
//        num = "------------";
//        date = new Date();
//        avance = 0;
//        TTC = 0;
//
//        mensualites_achat.clear();
//        mensualites_diver.clear();
//        mensualites_vente.clear();
//        historiques.clear();
//
//        selectMensualite_achat = null;
//        selectMensualite_diver = null;
//        selectMensualite_vente = null;
//        update("blog_document");
//    }
//
//    public void resetFiche_() {
//        double d = piece.getMontantMens();
//        String s = piece.getSource();
//        resetFiche(piece);
//
//        piece.setMode(new ModePaiement());
//        piece.setAchat(new MensualiteFactureAchat());
//        piece.setVente(new MensualiteFactureVente());
//        piece.setDivers(new MensualiteDocDivers());
//        piece.setTableExterne(Constantes.yvs_com_mensualite_facture_achat);
//        piece.setSource(Constantes.SCR_ACHAT);
//        piece.setMouvement("S");
//        piece.setMontantMens(d);
//        piece.setSource(s);
//
//        if (piece.getSource() != null) {
//            switch (piece.getSource()) {
//                case Constantes.SCR_ACHAT:
//                    piece.setTableExterne(Constantes.yvs_com_mensualite_facture_achat);
//                    piece.setMouvement("S");
//                    piece.setLibelle("Réglement Achat - " + achat.getNumDoc());
//                    piece.setDescription("Réglement Facture Achat N° " + achat.getNumDoc());
//                    break;
//                case Constantes.SCR_VENTE:
//                    piece.setTableExterne(Constantes.yvs_com_mensualite_facture_vente);
//                    piece.setMouvement("E");
//                    piece.setLibelle("Réglement Vente - " + vente.getNumDoc());
//                    piece.setDescription("Réglement Facture Vente N° " + vente.getNumDoc());
//                    break;
//                case Constantes.SCR_DIVERS:
//                    piece.setTableExterne(Constantes.yvs_base_mensualite_doc_divers);
//                    piece.setLibelle("Réglement OD - " + diver.getNumPiece());
//                    piece.setDescription("Réglement Opération Diverse N° " + diver.getNumPiece());
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        tabIds = "";
//        update("blog_infos");
//    }
//
//    public boolean saveNew_() {
//        try {
//            PieceTresorerie bean = recopieView();
//            if (controleFiche(bean)) {
//                selectPiece = buildPieceTresorerie(bean);
//                if (!bean.isUpdate()) {
//                    selectPiece = (YvsComptaCaissePieceDivers) dao.save1(selectPiece);
//                    bean.setId(selectPiece.getId());
//                    bean.setUpdate(true);
//                    piece.setId(selectPiece.getId());
//                    pieces.add(0, selectPiece);
//                    historiques.add(0, selectPiece);
//                } else {
//                    dao.update(selectPiece);
//                    bean.setUpdate(true);
//                    pieces.set(pieces.indexOf(bean), selectPiece);
//                    if (historiques.contains(selectPiece)) {
//                        historiques.set(historiques.indexOf(bean), selectPiece);
//                    }
//                }
//                piece.setUpdate(bean.isUpdate());
//                piece.setNumPiece(bean.getNumPiece());
//                piece.setNumRef(bean.getNumRef());
//                succes();
//                update("entete_form");
//                update("data_piece_tresor");
//                update("data_piece_tresor_hist");
//            }
//            return true;
//        } catch (Exception ex) {
//            getErrorMessage("Operation Impossible !");
//            getException("Error  : " + ex.getMessage(), ex);
//            return false;
//        }
//    }
//
//    @Override
//    public boolean saveNew() {
//        try {
//            PieceTresorerie bean = recopieView();
//            if (controleFiche(bean)) {
//                if (!bean.isUpdate()) {
//                    switch (bean.getSource()) {
//                        case Constantes.SCR_ACHAT:
//                            saveNewAchat(bean);
//                            break;
//                        case Constantes.SCR_VENTE:
//                            saveNewVente(bean);
//                            break;
//                        case Constantes.SCR_DIVERS:
//                            saveNewDivers(bean);
//                            break;
//                        default:
//                            break;
//                    }
//                } else {
//                    selectPiece = buildPieceTresorerie(bean);
//                    dao.update(selectPiece);
//                    pieces.set(pieces.indexOf(selectPiece), selectPiece);
//                    if (historiques.contains(selectPiece)) {
//                        historiques.set(historiques.indexOf(selectPiece), selectPiece);
//                    }
//                }
//                piece.setUpdate(true);
//                piece.setNumPiece(bean.getNumPiece());
//                piece.setNumRef(bean.getNumRef());
//                succes();
//                update("entete_form");
//                update("data_piece_tresor");
//                update("data_piece_tresor_hist");
//            }
//            return true;
//        } catch (Exception ex) {
//            getErrorMessage("Operation Impossible !");
//            getException("Error  : " + ex.getMessage(), ex);
//            return false;
//        }
//    }
//
//    public void saveNewAchat(PieceTresorerie bean) {
//        try {
//            if (achat != null ? achat.getId() > 0 : false) {
//                if (mensualites_achat != null ? mensualites_achat.isEmpty() : true) {
//                    YvsComMensualiteFactureAchat m = new YvsComMensualiteFactureAchat();
//                    m.setDateMensualite(new Date());
//                    m.setEtat(Constantes.ETAT_EDITABLE);
//                    m.setFacture(achat);
//                    m.setMontant(getNetApayer());
//                    if (currentUser != null ? currentUser.getId() > 0 : false) {
//                        m.setAuthor(currentUser);
//                    }
//                    m = (YvsComMensualiteFactureAchat) dao.save1(m);
//                    mensualites_achat.add(m);
//                }
//                int i = 0;
//                boolean int_ = false;
//                while (bean.getMontant() > 0 && i < mensualites_achat.size()) {
//                    double mtant = bean.getMontant();
//                    YvsComMensualiteFactureAchat m = mensualites_achat.get(i);
//                    Double s = (Double) dao.loadObjectByNameQueries("YvsBasePieceTresorerie.findByAchatStatut2S", new String[]{"facture", "s1", "s2"}, new Object[]{m.getId(), Constantes.ETAT_VALIDE, Constantes.ETAT_REGLE});
//                    m.setMontantRest(m.getMontant() - (s != null ? s : 0));
//                    if (bean.getMontantMens() > 0) {
//                        if (mtant > m.getMontantRest()) {
//                            bean.setMontant(m.getMontantRest());
//                            mtant -= m.getMontantRest();
//                            m.setEtat(Constantes.ETAT_ENCOURS);
//                            int_ = true;
//                        } else {
//                            bean.setMontant(mtant);
//                            mtant = 0;
//                            m.setEtat(Constantes.ETAT_REGLE);
//                        }
//                        setNumeroPiece(bean);
//                        bean.setStatut(Constantes.ETAT_VALIDE);
//                        setNumeroPiece(bean);
//                        bean.setIdExterne(m.getId());
//                        bean.setTableExterne(Constantes.yvs_com_mensualite_facture_achat);
//
//                        YvsComptaCaissePieceDivers en = buildPieceTresorerie(bean);
//                        en.setValiderBy(currentUser.getUsers());
//                        en.setDateValider(new Date());
//                        en = (YvsComptaCaissePieceDivers) dao.save1(en);
//                        dao.update(m);
//                        if (!int_) {
//                            selectPiece = en;
//                            piece.setId(en.getId());
//                            piece.setStatut(Constantes.ETAT_VALIDE);
//                        }
//
//                        pieces.add(en);
//                        historiques.add(en);
//                    } else {
//                        m.setEtat(Constantes.ETAT_REGLE);
//                        dao.update(m);
//                    }
//                    mensualites_achat.set(i, m);
//                    bean.setMontant(mtant);
//                    i++;
//                }
//                if (int_) {
//                    resetFiche_();
//                }
//                update("data_piece_tresor");
//                update("data_piece_tresor_hist");
//                update("data_mensualite_achats");
//
//                double s = dao.sumPieceAchat(achat.getId());
//                if (getNetApayer() <= s) {
//                    String rq = "UPDATE yvs_com_doc_achats SET statut = 'REGLE' WHERE id=?";
//                    Options[] param = new Options[]{new Options(achat.getId(), 1)};
//                    dao.requeteLibre(rq, param);
//                    achat.setStatut(Constantes.ETAT_REGLE);
//                } else {
//                    String rq = "UPDATE yvs_com_doc_achats SET statut = 'ENCOURS' WHERE id=?";
//                    Options[] param = new Options[]{new Options(achat.getId(), 1)};
//                    dao.requeteLibre(rq, param);
//                    achat.setStatut(Constantes.ETAT_ENCOURS);
//                }
//                if (achats.contains(achat)) {
//                    achats.set(achats.indexOf(achat), achat);
//                } else {
//                    achats.add(0, achat);
//                }
//                update("data_achats");
//            } else {
//                getErrorMessage("Vous devez selectionner une facture");
//            }
//        } catch (Exception ex) {
//            getErrorMessage("Opération Impossible !");
//            System.err.println("Error Opération : " + ex.getMessage());
//        }
//    }
//
//    public void saveNewVente(PieceTresorerie bean) {
//        try {
//            if (vente != null ? vente.getId() > 0 : false) {
//                if (mensualites_vente != null ? mensualites_vente.isEmpty() : true) {
//                    YvsComMensualiteFactureVente m = new YvsComMensualiteFactureVente();
//                    m.setDateMensualite(new Date());
//                    m.setEtat(Constantes.ETAT_EDITABLE);
//                    m.setFacture(vente);
//                    m.setMontant(getNetApayer());
//                    if (currentUser != null ? currentUser.getId() > 0 : false) {
//                        m.setAuthor(currentUser);
//                    }
//                    m = (YvsComMensualiteFactureVente) dao.save1(m);
//                    mensualites_vente.add(m);
//                }
//                int i = 0;
//                boolean int_ = false;
//                while (bean.getMontant() > 0 && i < mensualites_vente.size()) {
//                    double mtant = bean.getMontant();
//                    YvsComMensualiteFactureVente m = mensualites_vente.get(i);
//                    Double s = (Double) dao.loadObjectByNameQueries("YvsBasePieceTresorerie.findByVenteStatut2S", new String[]{"facture", "s1", "s2"}, new Object[]{m.getId(), Constantes.ETAT_VALIDE, Constantes.ETAT_REGLE});
//                    m.setMontantRest(m.getMontant() - (s != null ? s : 0));
//                    if (bean.getMontantMens() > 0) {
//                        if (mtant > m.getMontantRest()) {
//                            bean.setMontant(m.getMontantRest());
//                            mtant -= m.getMontantRest();
//                            m.setEtat(Constantes.ETAT_ENCOURS);
//                            int_ = true;
//                        } else {
//                            bean.setMontant(mtant);
//                            mtant = 0;
//                            m.setEtat(Constantes.ETAT_REGLE);
//                        }
//                        setNumeroPiece(bean);
//                        bean.setStatut(Constantes.ETAT_VALIDE);
//                        setNumeroPiece(bean);
//                        bean.setIdExterne(m.getId());
//                        bean.setTableExterne(Constantes.yvs_com_mensualite_facture_vente);
//
//                        YvsComptaCaissePieceDivers en = buildPieceTresorerie(bean);
//                        en.setValiderBy(currentUser.getUsers());
//                        en.setDateValider(new Date());
//                        en = (YvsComptaCaissePieceDivers) dao.save1(en);
//                        dao.update(m);
//                        if (!int_) {
//                            selectPiece = en;
//                            piece.setId(en.getId());
//                            piece.setStatut(Constantes.ETAT_VALIDE);
//                        }
//
//                        pieces.add(en);
//                        historiques.add(en);
//                    } else {
//                        m.setEtat(Constantes.ETAT_REGLE);
//                        dao.update(m);
//                    }
//                    mensualites_vente.set(i, m);
//                    bean.setMontant(mtant);
//                    i++;
//                }
//                if (int_) {
//                    resetFiche_();
//                }
//                update("data_piece_tresor");
//                update("data_piece_tresor_hist");
//                update("data_mensualite_ventes");
//
//                double s = dao.sumPieceVente(vente.getId());
//                if (getNetApayer() <= s) {
//                    String rq = "UPDATE yvs_com_doc_ventes SET statut = 'REGLE' WHERE id=?";
//                    Options[] param = new Options[]{new Options(vente.getId(), 1)};
//                    dao.requeteLibre(rq, param);
//                    vente.setStatut(Constantes.ETAT_REGLE);
//                } else {
//                    String rq = "UPDATE yvs_com_doc_ventes SET statut = 'ENCOURS' WHERE id=?";
//                    Options[] param = new Options[]{new Options(vente.getId(), 1)};
//                    dao.requeteLibre(rq, param);
//                    vente.setStatut(Constantes.ETAT_ENCOURS);
//                }
//                if (ventes.contains(vente)) {
//                    ventes.set(ventes.indexOf(vente), vente);
//                } else {
//                    ventes.add(0, vente);
//                }
//                update("data_ventes");
//            } else {
//                getErrorMessage("Vous devez selectionner une facture");
//            }
//        } catch (Exception ex) {
//            getErrorMessage("Opération Impossible !");
//            System.err.println("Error Opération : " + ex.getMessage());
//        }
//    }
//
//    public void saveNewDivers(PieceTresorerie bean) {
//        try {
//            if (diver != null ? diver.getId() > 0 : false) {
//                if (mensualites_diver != null ? mensualites_diver.isEmpty() : true) {
//                    YvsComptaCaisseMensualiteDocDivers m = new YvsComptaCaisseMensualiteDocDivers();
//                    m.setDateMensualite(new Date());
//                    m.setEtat(Constantes.ETAT_EDITABLE);
//                    m.setDocDivers(diver);
//                    m.setMontant(getNetApayer());
//                    if (currentUser != null ? currentUser.getId() > 0 : false) {
//                        m.setAuthor(currentUser);
//                    }
//                    m = (YvsComptaCaisseMensualiteDocDivers) dao.save1(m);
//                    mensualites_diver.add(m);
//                }
//                int i = 0;
//                boolean int_ = false;
//                while (bean.getMontant() > 0 && i < mensualites_diver.size()) {
//                    double mtant = bean.getMontant();
//                    YvsComptaCaisseMensualiteDocDivers m = mensualites_diver.get(i);
//                    Double s = (Double) dao.loadObjectByNameQueries("YvsBasePieceTresorerie.findByOtherStatut2S", new String[]{"facture", "s1", "s2"}, new Object[]{m.getId(), Constantes.ETAT_VALIDE, Constantes.ETAT_REGLE});
//                    if (bean.getMontantMens() > 0) {
////                        if (mtant > m.getMontantRest()) {
////                            bean.setMontant(m.getMontantRest());
////                            mtant -= m.getMontantRest();
////                            m.setEtat(Constantes.ETAT_ENCOURS);
////                            int_ = true;
////                        } else {
//                            bean.setMontant(mtant);
//                            mtant = 0;
//                            m.setEtat(Constantes.ETAT_REGLE);
////                        }
//                        setNumeroPiece(bean);
//                        bean.setStatut(Constantes.ETAT_VALIDE);
//                        setNumeroPiece(bean);
//                        bean.setIdExterne(m.getId());
//                        bean.setTableExterne(Constantes.yvs_base_mensualite_doc_divers);
//
//                        YvsComptaCaissePieceDivers en = buildPieceTresorerie(bean);
//                        en.setValiderBy(currentUser.getUsers());
//                        en.setDateValider(new Date());
//                        en = (YvsComptaCaissePieceDivers) dao.save1(en);
//                        dao.update(m);
//                        if (!int_) {
//                            selectPiece = en;
//                            piece.setId(en.getId());
//                            piece.setStatut(Constantes.ETAT_VALIDE);
//                        }
//
//                        pieces.add(en);
//                        historiques.add(en);
//                    } else {
//                        m.setEtat(Constantes.ETAT_REGLE);
//                        dao.update(m);
//                    }
//                    mensualites_diver.set(i, m);
//                    bean.setMontant(mtant);
//                    i++;
//                }
//                if (int_) {
//                    resetFiche_();
//                }
//                update("data_piece_tresor");
//                update("data_piece_tresor_hist");
//                update("data_mensualite_divers");
//
//                double s = dao.sumPieceOther(diver.getId());
//                if (getNetApayer() <= s) {
//                    String rq = "UPDATE yvs_base_doc_divers SET statut = 'REGLE' WHERE id=?";
//                    Options[] param = new Options[]{new Options(diver.getId(), 1)};
//                    dao.requeteLibre(rq, param);
//                    diver.setStatutDoc(Constantes.STATUT_DOC_PAYER);
//                } else {
//                    String rq = "UPDATE yvs_base_doc_divers SET statut = 'ENCOURS' WHERE id=?";
//                    Options[] param = new Options[]{new Options(diver.getId(), 1)};
//                    dao.requeteLibre(rq, param);
//                    diver.setStatutDoc(Constantes.STATUT_DOC_ENCOUR);
//                }
//                if (divers.contains(diver)) {
//                    divers.set(divers.indexOf(diver), diver);
//                } else {
//                    divers.add(0, diver);
//                }
//                update("data_divers");
//            } else {
//                getErrorMessage("Vous devez selectionner une OD");
//            }
//        } catch (Exception ex) {
//            getErrorMessage("Opération Impossible !");
//            System.err.println("Error Opération : " + ex.getMessage());
//        }
//    }
//
//    @Override
//    public void deleteBean() {
//        try {
//            if ((tabIds != null) ? !tabIds.equals("") : false) {
//                String[] tab = tabIds.split("-");
//                for (String ids : tab) {
//                    long id = Long.valueOf(ids);
//                    YvsComptaCaissePieceDivers bean = pieces.get(pieces.indexOf(new YvsComptaCaissePieceDivers(id)));
//                    if (!_controleFiche_(bean)) {
//                        return;
//                    }
//                    dao.delete(new YvsComptaCaissePieceDivers(bean.getId()));
//                    pieces.remove(bean);
//                    if (historiques.contains(bean)) {
//                        historiques.remove(bean);
//                    }
//                }
//                succes();
//                resetFiche();
//                update("data_piece_tresor");
//                update("data_piece_tresor_hist");
//            }
//        } catch (Exception ex) {
//            getErrorMessage("Suppression Impossible !");
//            getException("Error Suppression : " + ex.getMessage(), ex);
//        }
//    }
//
//    public void deleteBean_(YvsComptaCaissePieceDivers y) {
//        selectPiece = y;
//    }
//
//    public void deleteBean_() {
//        try {
//            if (selectPiece != null) {
//                if (!_controleFiche_(selectPiece)) {
//                    return;
//                }
//                dao.delete(selectPiece);
//                pieces.remove(selectPiece);
//                if (historiques.contains(selectPiece)) {
//                    historiques.remove(selectPiece);
//                }
//                succes();
//                resetFiche();
//                update("data_piece_tresor");
//                update("data_piece_tresor_hist");
//            }
//        } catch (Exception ex) {
//            getErrorMessage("Suppression Impossible !");
//            getException("Error Suppression : " + ex.getMessage(), ex);
//        }
//    }
//
//    @Override
//    public void loadOnView(SelectEvent ev) {
//        if ((ev != null) ? ev.getObject() != null : false) {
//            selectPiece = (YvsComptaCaissePieceDivers) ev.getObject();
//            historiques.clear();
////            populateView(UtilCom.buildBeanPieceTresorerie(selectPiece));
//            update("blog_form_piece_tresor");
//        }
//    }
//
//    public void loadOnView_(SelectEvent ev) {
//        if ((ev != null) ? ev.getObject() != null : false) {
//            selectPiece = (YvsComptaCaissePieceDivers) ev.getObject();
////            populateView(UtilCom.buildBeanPieceTresorerie(selectPiece));
//            update("infos_piece");
//            update("entete_form");
//        }
//    }
//
//    @Override
//    public void unLoadOnView(UnselectEvent ev) {
//        resetFiche_();
//        update("infos_piece");
//    }
//
//    public void loadOnViewAchat(SelectEvent ev) {
//        historiques.clear();
//        if ((ev != null) ? ev.getObject() != null : false) {
//            achat = (YvsComDocAchats) ev.getObject();
//            id = achat.getId();
//            num = achat.getNumDoc();
//            date = achat.getDateDoc();
//            DocAchat a = new DocAchat(id);
//            avance = a.getMontantAvance();
//            TTC = a.getMontantTTC();
//            loadAllMensAchat(achat);
//            loadHistoriquesAchat(achat, null);
//
//            piece.setMontant(getNetApayer());
//            piece.setMontantMens(getNetApayer());
//            piece.setLibelle("Réglement Achat - " + achat.getNumDoc());
//            piece.setDescription("Réglement Facture Achat N° " + achat.getNumDoc());
//            update("infos_piece");
//            update("blog_document");
//        }
//        update("data_piece_tresor_hist");
//    }
//
//    public void unLoadOnViewAchat(UnselectEvent ev) {
//        historiques.clear();
//        achat = new YvsComDocAchats();
//        update("infos_piece");
//        update("data_piece_tresor_hist");
//    }
//
//    public void loadOnViewVente(SelectEvent ev) {
//        historiques.clear();
//        if ((ev != null) ? ev.getObject() != null : false) {
//            vente = (YvsComDocVentes) ev.getObject();
//            id = vente.getId();
//            num = vente.getNumDoc();
//            date = vente.getEnteteDoc().getDateEntete();
//            DocVente a = new DocVente(id);
////            setMontantTotalDoc(a);
//            avance = a.getMontantAvance();
//            TTC = a.getMontantTTC();
//            loadAllMensVente(vente);
//            loadHistoriquesVente(vente, null);
//
//            piece.setMontant(getNetApayer());
//            piece.setMontantMens(getNetApayer());
//            piece.setLibelle("Réglement Vente - " + vente.getNumDoc());
//            piece.setDescription("Réglement Facture Vente N° " + vente.getNumDoc());
//            update("infos_piece");
//            update("blog_document");
//        }
//        update("data_piece_tresor_hist");
//    }
//
//    public void unLoadOnViewVente(UnselectEvent ev) {
//        historiques.clear();
//        vente = new YvsComDocVentes();
//        update("infos_piece");
//        update("data_piece_tresor_hist");
//    }
//
//    public void loadOnViewDivers(SelectEvent ev) {
//        historiques.clear();
//        if ((ev != null) ? ev.getObject() != null : false) {
//            diver = (YvsComptaCaisseDocDivers) ev.getObject();
//            piece.setMouvement(diver.getMouvement());
//            id = diver.getId();
//            num = diver.getNumPiece();
//            date = diver.getDateDoc();
//            DocCaissesDivers a = new DocCaissesDivers(id);
//            avance = a.getMontant();
//            TTC = diver.getMontant();
//            loadAllMensDiver(diver);
//            loadHistoriquesDiver(diver, null);
//
//            piece.setMontant(getNetApayer());
//            piece.setMontantMens(getNetApayer());
//            piece.setLibelle("Réglement OD - " + diver.getNumPiece());
//            piece.setDescription("Réglement Opération Diverse N° " + diver.getNumPiece());
//            update("infos_piece");
//            update("blog_document");
//        }
//        update("data_piece_tresor_hist");
//    }
//
//    public void unLoadOnViewDivers(UnselectEvent ev) {
//        historiques.clear();
//        diver = new YvsComptaCaisseDocDivers();
//        update("infos_piece");
//        update("data_piece_tresor_hist");
//    }
//
//    public void loadOnViewMensAchat(SelectEvent ev) {
//        historiques.clear();
//        if ((ev != null) ? ev.getObject() != null : false) {
//            YvsComMensualiteFactureAchat bean_ = (YvsComMensualiteFactureAchat) ev.getObject();
//            MensualiteFactureAchat bean = UtilCom.buildBeanMensualiteFactureAchat(bean_);
//            cloneObject(piece.getAchat(), bean);
//            piece.setIdExterne(bean.getId());
//            piece.setTableExterne(Constantes.yvs_com_mensualite_facture_achat);
//            piece.setMontantMens(bean.getMontantRest());
//            piece.setLibelle("Réglement Achat - " + achat.getNumDoc());
//            piece.setDescription("Réglement Facture Achat N° " + achat.getNumDoc());
//            loadHistoriquesAchat(null, bean_);
//
//            Double s = (Double) dao.loadObjectByNameQueries("YvsBasePieceTresorerie.findByAchatStatut2S", new String[]{"facture", "s1", "s2"}, new Object[]{bean.getId(), Constantes.ETAT_VALIDE, Constantes.ETAT_REGLE});
//            piece.setMontant(bean.getMontant() - (s != null ? s : 0));
//            piece.setMontantMens(bean.getMontant() - (s != null ? s : 0));
//            update("txt_montant_mensualite");
//        }
//        update("data_piece_tresor_hist");
//    }
//
//    public void unLoadOnViewMensAchat(UnselectEvent ev) {
//        historiques.clear();
//        piece.setAchat(new MensualiteFactureAchat());
//        piece.setIdExterne(0);
//        piece.setTableExterne("");
//        piece.setMontantMens(0);
//        piece.setLibelle("Réglement Achat - " + achat.getNumDoc());
//        piece.setDescription("Réglement Facture Achat N° " + achat.getNumDoc());
//        loadHistoriquesAchat(achat, null);
//        update("txt_montant_mensualite");
//        update("data_piece_tresor_hist");
//    }
//
//    public void loadOnViewMensVente(SelectEvent ev) {
//        historiques.clear();
//        if ((ev != null) ? ev.getObject() != null : false) {
//            YvsComMensualiteFactureVente bean_ = (YvsComMensualiteFactureVente) ev.getObject();
//            MensualiteFactureVente bean = UtilCom.buildBeanMensualiteFactureVente(bean_);
//            cloneObject(piece.getVente(), bean);
//            piece.setIdExterne(bean.getId());
//            piece.setTableExterne(Constantes.yvs_com_mensualite_facture_vente);
//            piece.setMontantMens(bean.getMontantRest());
//            piece.setLibelle("Réglement Vente - " + vente.getNumDoc());
//            piece.setDescription("Réglement Facture Vente N° " + vente.getNumDoc());
//            loadHistoriquesVente(null, bean_);
//
//            Double s = (Double) dao.loadObjectByNameQueries("YvsBasePieceTresorerie.findByVenteStatut2S", new String[]{"facture", "s1", "s2"}, new Object[]{bean.getId(), Constantes.ETAT_VALIDE, Constantes.ETAT_REGLE});
//            piece.setMontant(bean.getMontant() - (s != null ? s : 0));
//            piece.setMontantMens(bean.getMontant() - (s != null ? s : 0));
//            update("txt_montant_mensualite");
//        }
//        update("data_piece_tresor_hist");
//    }
//
//    public void unLoadOnViewMensVente(UnselectEvent ev) {
//        historiques.clear();
//        piece.setVente(new MensualiteFactureVente());
//        piece.setIdExterne(0);
//        piece.setTableExterne("");
//        piece.setMontantMens(0);
//        piece.setLibelle("Réglement Vente - " + vente.getNumDoc());
//        piece.setDescription("Réglement Facture Vente N° " + vente.getNumDoc());
//        loadHistoriquesVente(vente, null);
//        update("txt_montant_mensualite");
//        update("data_piece_tresor_hist");
//    }
//
//    public void loadOnViewMensDivers(SelectEvent ev) {
//        historiques.clear();
//        if ((ev != null) ? ev.getObject() != null : false) {
//            YvsComptaCaisseMensualiteDocDivers bean_ = (YvsComptaCaisseMensualiteDocDivers) ev.getObject();
//            MensualiteDocDivers bean = UtilCom.buildBeanMensualiteDocDivers(bean_);
//            cloneObject(piece.getDivers(), bean);
//            piece.setIdExterne(bean.getId());
//            piece.setTableExterne(Constantes.yvs_base_mensualite_doc_divers);
//            piece.setMontantMens(bean.getMontantRest());
//            piece.setLibelle("Réglement OD - " + diver.getNumPiece());
//            piece.setDescription("Réglement Opération Diverse N° " + diver.getNumPiece());
//            loadHistoriquesDiver(null, bean_);
//
//            Double s = (Double) dao.loadObjectByNameQueries("YvsBasePieceTresorerie.findByOtherStatut2S", new String[]{"facture", "s1", "s2"}, new Object[]{bean.getId(), Constantes.ETAT_VALIDE, Constantes.ETAT_REGLE});
//            piece.setMontant(bean.getMontant() - (s != null ? s : 0));
//            piece.setMontantMens(bean.getMontant() - (s != null ? s : 0));
//            update("txt_montant_mensualite");
//        }
//        update("data_piece_tresor_hist");
//    }
//
//    public void unLoadOnViewMensDivers(UnselectEvent ev) {
//        historiques.clear();
//        piece.setDivers(new MensualiteDocDivers());
//        piece.setIdExterne(0);
//        piece.setTableExterne("");
//        piece.setMontantMens(0);
//        piece.setLibelle("Réglement OD - " + diver.getNumPiece());
//        piece.setDescription("Réglement Opération Diverse N° " + diver.getNumPiece());
//        loadHistoriquesDiver(diver, null);
//        update("txt_montant_mensualite");
//        update("data_piece_tresor_hist");
//    }
//
//    public void chooseSource() {
//        _resetFiche();
//        if (piece.getSource() != null) {
//            switch (piece.getSource()) {
//                case Constantes.SCR_ACHAT:
//                    piece.setTableExterne(Constantes.yvs_com_mensualite_facture_achat);
//                    piece.setMouvement("S");
//                    loadAllAchats();
//                    break;
//                case Constantes.SCR_VENTE:
//                    piece.setTableExterne(Constantes.yvs_com_mensualite_facture_vente);
//                    piece.setMouvement("E");
//                    loadAllVentes();
//                    break;
//                case Constantes.SCR_DIVERS:
//                    piece.setTableExterne(Constantes.yvs_base_mensualite_doc_divers);
//                    loadAllDivers();
//                    break;
//                default:
//                    break;
//            }
//        }
//        update("data_piece_tresor_hist");
//    }
//
//    public void chooseMode() {
//        if ((piece.getMode() != null) ? piece.getMode().getId() > 0 : false) {
//            YvsModePaiement p_ = modes.get(modes.indexOf(new YvsModePaiement(piece.getMode().getId())));
//            ModePaiement p = UtilCompta.buildBeanModePaiement(p_);
//            cloneObject(piece.getMode(), p);
//        }
//    }
//
//    public void chooseSolde() {
//        switch (piece.getSource()) {
//            case Constantes.SCR_ACHAT:
//                loadAllAchats();
//                break;
//            case Constantes.SCR_VENTE:
//                loadAllVentes();
//                break;
//            case Constantes.SCR_DIVERS:
//                loadAllDivers();
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void init(boolean next) {
//        
//        loadAllPieces();
//    }
//
//    @Override
//    public void choosePaginator(ValueChangeEvent ev) {
//        super.choosePaginator(ev);
//        loadAllPieces();
//        setDisablePrev(true);
//    }
//
//    public void chooseStatut(ValueChangeEvent ev) {
//        statut_ = ((String) ev.getNewValue());
//        loadAllPieces();
//    }
//
//    public void chooseCloturer(ValueChangeEvent ev) {
////        cloturer_ = ((Boolean) ev.getNewValue());
//        loadAllPieces();
//    }
//
//    public void chooseMouv(ValueChangeEvent ev) {
////        entree_ = ((Boolean) ev.getNewValue());
//        loadAllPieces();
//    }
//
//    public void chooseDateSearch() {
//        loadAllPieces();
//    }
//
//    public void searchOnDates() {
//        if (isInSolde()) {
//            switch (piece.getSource()) {
//                case Constantes.SCR_ACHAT:
////                    loadAllAchatsByDates(true, true);
//                    break;
//                case Constantes.SCR_VENTE:
////                    loadAllVentesByDate(true, true);
//                    break;
//                case Constantes.SCR_DIVERS:
////                    loadAllDiversByDate(true, true);
//                    break;
//                default:
//                    break;
//            }
//        } else {
//            switch (piece.getSource()) {
//                case Constantes.SCR_ACHAT:
////                    loadAllAchatsByDates_(true, true);
//                    break;
//                case Constantes.SCR_VENTE:
////                    loadAllVentesByDate_(true, true);
//                    break;
//                case Constantes.SCR_DIVERS:
////                    loadAllDiversByDate_(true, true);
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//    public void searchOnFsseur() {
//        if (isInSolde()) {
////            loadAllAchatsByFsseur(true, true);
//        } else {
////            loadAllAchatsByFsseur_(true, true);
//        }
//    }
//
//    public void searchOnClient() {
//        if (isInSolde()) {
////            loadAllVentesByClt(true, true);
//        } else {
////            loadAllVentesByClt_(true, true);
//        }
//    }
//
//    public void searchOnTiers() {
//        if (isInSolde()) {
////            loadAllDiversByTiers(true, true);
//        } else {
////            loadAllDiversByTiers_(true, true);
//        }
//    }
//
//    public List<YvsBaseTiers> completeTiers(String query) {
//        List<YvsBaseTiers> allThemes = tiersService.getTiers();
//        List<YvsBaseTiers> filteredThemes = new ArrayList<>();
//        for (YvsBaseTiers t : allThemes) {
//            if (t.getNom_prenom().toLowerCase().startsWith(query.toLowerCase())) {
//                filteredThemes.add(t);
//            }
//        }
//        return filteredThemes;
//    }
//
//    public List<YvsComClient> completeClient(String query) {
//        List<YvsComClient> allThemes = clientService.getClients();
//        List<YvsComClient> filteredThemes = new ArrayList<>();
//        for (YvsComClient t : allThemes) {
//            if (t.getTiers().getNom_prenom().toLowerCase().startsWith(query.toLowerCase())) {
//                filteredThemes.add(t);
//            }
//        }
//        return filteredThemes;
//    }
//
//    public List<YvsBaseFournisseur> completeFournisseur(String query) {
//        List<YvsBaseFournisseur> allThemes = fournisseurService.getFournisseurs();
//        List<YvsBaseFournisseur> filteredThemes = new ArrayList<>();
//        for (YvsBaseFournisseur t : allThemes) {
//            if (t.getTiers().getNom_prenom().toLowerCase().startsWith(query.toLowerCase())) {
//                filteredThemes.add(t);
//            }
//        }
//        return filteredThemes;
//    }
//
//    public void onTiersSelect(SelectEvent ev) {
//        if ((ev != null) ? ev.getObject() != null : false) {
//            Tiers t = (Tiers) ev.getObject();
//            if (tiersSelect == null) {
//                tiersSelect = new YvsBaseTiers();
//            }
//            cloneObject(tiersSelect, t);
//        }
//    }
//
//    public void onClientSelect(SelectEvent ev) {
//        if ((ev != null) ? ev.getObject() != null : false) {
//            YvsComClient t = (YvsComClient) ev.getObject();
//            if (clientSelect == null) {
//                clientSelect = new YvsComClient();
//            }
//            cloneObject(clientSelect, t);
//        }
//    }
//
//    public void onFournisseurSelect(SelectEvent ev) {
//        if ((ev != null) ? ev.getObject() != null : false) {
//            Fournisseur t = (Fournisseur) ev.getObject();
//            if (fournisseurSelect == null) {
//                fournisseurSelect = new Fournisseur();
//            }
//            cloneObject(fournisseurSelect, t);
//        }
//    }
//
//    public void annulerOrder() {
//        if (changeStatut(Constantes.ETAT_EDITABLE, false)) {
//            selectPiece.setAnnulerBy(null);
//            selectPiece.setValiderBy(null);
//            selectPiece.setDateAnnuler(null);
//            selectPiece.setDateValider(null);
//            if (currentUser != null ? currentUser.getId() > 0 : false) {
//                selectPiece.setAuthor(currentUser);
//            }
//            dao.update(selectPiece);
//        }
//    }
//
//    public void refuserOrder() {
//        if (changeStatut(Constantes.ETAT_ANNULE, false)) {
//            selectPiece.setAnnulerBy(currentUser.getUsers());
//            selectPiece.setValiderBy(null);
//            selectPiece.setDateAnnuler(new Date());
//            selectPiece.setDateValider(null);
//            if (currentUser != null ? currentUser.getId() > 0 : false) {
//                selectPiece.setAuthor(currentUser);
//            }
//            dao.update(selectPiece);
//        }
//    }
//
//    public void validerOrder() {
//        if (changeStatut(Constantes.ETAT_VALIDE, false)) {
//            selectPiece.setAnnulerBy(null);
//            selectPiece.setValiderBy(currentUser.getUsers());
//            selectPiece.setDateAnnuler(null);
//            selectPiece.setDateValider(new Date());
//            if (currentUser != null ? currentUser.getId() > 0 : false) {
//                selectPiece.setAuthor(currentUser);
//            }
//            dao.update(selectPiece);
//        }
//    }
//
//    public void reglerOrder() {
//        if (changeStatut(Constantes.ETAT_REGLE, true)) {
//
//        }
//    }
//
//    public void cloturer() {
//        if (selectPiece == null) {
//            return;
//        }
//        piece.setCloturer(!piece.isCloturer());
//        if (currentUser != null ? currentUser.getId() > 0 : false) {
//            selectPiece.setAuthor(currentUser);
//        }
//        dao.update(selectPiece);
//        pieces.set(pieces.indexOf(selectPiece), selectPiece);
//        if (historiques.contains(selectPiece)) {
//            historiques.set(historiques.indexOf(selectPiece), selectPiece);
//            update("data_piece_tresor_hist");
//        }
//        update("data_piece_tresor");
//    }
//
//    public boolean changeStatut(String etat, boolean regle) {
//        if (changeStatut_(etat, regle)) {
//            succes();
//            return true;
//        }
//        return false;
//    }
//
//    public boolean changeStatut_(String etat, boolean regle) {
//        return changeStatut_(etat, piece, selectPiece, regle);
//    }
//
//    public boolean changeStatut(String etat, YvsComptaCaissePieceDivers doc_, boolean regle) {
//        if (changeStatut_(etat, doc_, regle)) {
//            succes();
//            return true;
//        }
//        return false;
//    }
//
//    public boolean changeStatut_(String etat, YvsComptaCaissePieceDivers doc_, boolean regle) {
////        return changeStatut_(etat, UtilCom.buildBeanPieceTresorerie(doc_), doc_, regle);
//        return false;
//    }
//
//    public boolean changeStatut(String etat, PieceTresorerie doc, YvsComptaCaissePieceDivers doc_, boolean regle) {
//        if (changeStatut_(etat, doc, doc_, regle)) {
//            succes();
//            return true;
//        }
//        return false;
//    }
//
//    public boolean changeStatut_(String etat, PieceTresorerie doc, YvsComptaCaissePieceDivers doc_, boolean regle) {
//        if (!etat.equals("")) {
//            if (regle) {
//                if (!controleFiche_(doc_)) {
//                    return false;
//                }
//            } else {
//                if (!_controleFiche_(doc_)) {
//                    return false;
//                }
//            }
//            String rq = "UPDATE yvs_base_piece_tresorerie SET statut = '" + etat + "' WHERE id=?";
//            Options[] param = new Options[]{new Options(doc_.getId(), 1)};
//            dao.requeteLibre(rq, param);
//            doc.setStatut(etat);
//            doc_.setStatutPiece(etat);
//            pieces.set(pieces.indexOf(doc_), doc_);
//            if (historiques.contains(doc_)) {
//                historiques.set(historiques.indexOf(doc_), doc_);
//            } else {
//                historiques.add(0, doc_);
//            }
//            update("data_piece_tresor_hist");
//            switch (doc.getSource()) {
//                case Constantes.SCR_ACHAT: {
//                    YvsComMensualiteFactureAchat m = new YvsComMensualiteFactureAchat(doc.getIdExterne());
//                    if (mensualites_achat.contains(m)) {
//                        m = mensualites_achat.get(mensualites_achat.indexOf(m));
//                    }
//                    Double p = (Double) dao.loadObjectByNameQueries("YvsBasePieceTresorerie.findByAchatStatut2S", new String[]{"facture", "s1", "s2"}, new Object[]{m.getId(), Constantes.ETAT_VALIDE, Constantes.ETAT_REGLE});
//                    if (m.getMontant() <= (p != null ? p : 0)) {
//                        rq = "UPDATE yvs_com_mensualite_facture_achat SET etat = 'REGLE' WHERE id=?";
//                        m.setEtat(Constantes.ETAT_REGLE);
//                    } else if ((p != null ? p : 0) > 0) {
//                        rq = "UPDATE yvs_com_mensualite_facture_achat SET etat = 'ENCOURS' WHERE id=?";
//                        m.setEtat(Constantes.ETAT_ENCOURS);
//                    } else {
//                        rq = "UPDATE yvs_com_mensualite_facture_achat SET etat = 'EDITABLE' WHERE id=?";
//                        m.setEtat(Constantes.ETAT_EDITABLE);
//                    }
//                    param = new Options[]{new Options(m.getId(), 1)};
//                    dao.requeteLibre(rq, param);
//                    if (mensualites_achat.contains(m)) {
//                        mensualites_achat.set(mensualites_achat.indexOf(m), m);
//                    } else {
//                        mensualites_achat.add(m);
//                    }
//                    update("data_mensualite_achats");
//
//                    if (achat != null ? (achat.getId() != null ? achat.getId() > 0 : false) : false) {
//                        double s = dao.sumPieceAchat(achat.getId());
//                        if (getNetApayer() <= s) {
//                            rq = "UPDATE yvs_com_doc_achats SET statut = 'REGLE' WHERE id=?";
//                            achat.setStatut(Constantes.ETAT_REGLE);
//                        } else if (s > 0) {
//                            rq = "UPDATE yvs_com_doc_achats SET statut = 'ENCOURS' WHERE id=?";
//                            achat.setStatut(Constantes.ETAT_ENCOURS);
//                        } else {
//                            rq = "UPDATE yvs_com_doc_achats SET statut = 'EDITABLE' WHERE id=?";
//                            achat.setStatut(Constantes.ETAT_EDITABLE);
//                        }
//                        param = new Options[]{new Options(achat.getId(), 1)};
//                        dao.requeteLibre(rq, param);
//                        if (achats.contains(achat)) {
//                            achats.set(achats.indexOf(achat), achat);
//                        } else {
//                            if (achat.getStatut().equals(Constantes.ETAT_VALIDE) || achat.getStatut().equals(Constantes.ETAT_REGLE)) {
//                                achats.add(achat);
//                            }
//                        }
//                        update("data_achats");
//                    }
//                    break;
//                }
//                case Constantes.SCR_VENTE: {
//                    YvsComMensualiteFactureVente m = new YvsComMensualiteFactureVente(doc.getIdExterne());
//                    if (mensualites_vente.contains(m)) {
//                        m = mensualites_vente.get(mensualites_vente.indexOf(m));
//                    }
//                    Double p = (Double) dao.loadObjectByNameQueries("YvsBasePieceTresorerie.findByVenteStatut2S", new String[]{"facture", "s1", "s2"}, new Object[]{m.getId(), Constantes.ETAT_VALIDE, Constantes.ETAT_REGLE});
//                    if (m.getMontant() <= (p != null ? p : 0)) {
//                        rq = "UPDATE yvs_com_mensualite_facture_vente SET etat = 'REGLE' WHERE id=?";
//                        m.setEtat(Constantes.ETAT_REGLE);
//                    } else if ((p != null ? p : 0) > 0) {
//                        rq = "UPDATE yvs_com_mensualite_facture_vente SET etat = 'ENCOURS' WHERE id=?";
//                        m.setEtat(Constantes.ETAT_ENCOURS);
//                    } else {
//                        rq = "UPDATE yvs_com_mensualite_facture_vente SET etat = 'EDITABLE' WHERE id=?";
//                        m.setEtat(Constantes.ETAT_EDITABLE);
//                    }
//                    param = new Options[]{new Options(m.getId(), 1)};
//                    dao.requeteLibre(rq, param);
//                    if (mensualites_vente.contains(m)) {
//                        mensualites_vente.set(mensualites_vente.indexOf(m), m);
//                    } else {
//                        mensualites_vente.add(m);
//                    }
//                    update("data_mensualite_ventes");
//
//                    if (vente != null ? (vente.getId() != null ? vente.getId() > 0 : false) : false) {
//                        double s = dao.sumPieceVente(vente.getId());
//                        if (getNetApayer() <= s) {
//                            rq = "UPDATE yvs_com_doc_ventes SET statut = 'REGLE' WHERE id=?";
//                            vente.setStatut(Constantes.ETAT_REGLE);
//                        } else if (s > 0) {
//                            rq = "UPDATE yvs_com_doc_ventes SET statut = 'ENCOURS' WHERE id=?";
//                            vente.setStatut(Constantes.ETAT_ENCOURS);
//                        } else {
//                            rq = "UPDATE yvs_com_doc_ventes SET statut = 'EDITABLE' WHERE id=?";
//                            vente.setStatut(Constantes.ETAT_EDITABLE);
//                        }
//                        param = new Options[]{new Options(vente.getId(), 1)};
//                        dao.requeteLibre(rq, param);
//                        if (ventes.contains(vente)) {
//                            ventes.set(ventes.indexOf(vente), vente);
//                        } else {
//                            if (vente.getStatut().equals(Constantes.ETAT_VALIDE) || vente.getStatut().equals(Constantes.ETAT_REGLE)) {
//                                ventes.add(vente);
//                            }
//                        }
//                        update("data_ventes");
//                    }
//                    break;
//                }
//                case Constantes.SCR_DIVERS: {
//                    YvsComptaCaisseMensualiteDocDivers m = new YvsComptaCaisseMensualiteDocDivers(doc.getIdExterne());
//                    if (mensualites_diver.contains(m)) {
//                        m = mensualites_diver.get(mensualites_diver.indexOf(m));
//                    }
//                    Double p = (Double) dao.loadObjectByNameQueries("YvsBasePieceTresorerie.findByOtherStatut2S", new String[]{"facture", "s1", "s2"}, new Object[]{m.getId(), Constantes.ETAT_VALIDE, Constantes.ETAT_REGLE});
//                    if (m.getMontant() <= (p != null ? p : 0)) {
//                        rq = "UPDATE yvs_base_mensualite_doc_divers SET etat = 'REGLE' WHERE id=?";
//                        m.setEtat(Constantes.ETAT_REGLE);
//                    } else if ((p != null ? p : 0) > 0) {
//                        rq = "UPDATE yvs_base_mensualite_doc_divers SET etat = 'ENCOURS' WHERE id=?";
//                        m.setEtat(Constantes.ETAT_ENCOURS);
//                    } else {
//                        rq = "UPDATE yvs_base_mensualite_doc_divers SET etat = 'EDITABLE' WHERE id=?";
//                        m.setEtat(Constantes.ETAT_EDITABLE);
//                    }
//                    param = new Options[]{new Options(m.getId(), 1)};
//                    dao.requeteLibre(rq, param);
//                    if (mensualites_diver.contains(m)) {
//                        mensualites_diver.set(mensualites_diver.indexOf(m), m);
//                    } else {
//                        mensualites_diver.add(m);
//                    }
//                    update("data_mensualite_divers");
//
//                    if (diver != null ? (diver.getId() != null ? diver.getId() > 0 : false) : false) {
//                        double s = dao.sumPieceOther(diver.getId());
//                        if (getNetApayer() <= s) {
//                            rq = "UPDATE yvs_base_doc_divers SET statut = 'REGLE' WHERE id=?";
//                            diver.setStatutDoc(Constantes.STATUT_DOC_PAYER);
//                        } else if (s > 0) {
//                            rq = "UPDATE yvs_base_doc_divers SET statut = 'ENCOURS' WHERE id=?";
//                            diver.setStatutDoc(Constantes.STATUT_DOC_ENCOUR);
//                        } else {
//                            rq = "UPDATE yvs_base_doc_divers SET statut = 'EDITABLE' WHERE id=?";
//                            diver.setStatutDoc(Constantes.STATUT_DOC_EDITABLE);
//                        }
//                        param = new Options[]{new Options(diver.getId(), 1)};
//                        dao.requeteLibre(rq, param);
//                        if (divers.contains(diver)) {
//                            divers.set(divers.indexOf(diver), diver);
//                        } else {
//                            if (diver.getStatutDoc().equals(Constantes.ETAT_VALIDE) || diver.getStatutDoc().equals(Constantes.ETAT_REGLE)) {
//                                divers.add(diver);
//                            }
//                        }
//                        update("data_divers");
//                    }
//                    break;
//                }
//                default:
//                    break;
//            }
//            update("form_others");
//            update("data_piece_tresor");
//            update("grp_btn_etat");
//            return true;
//        }
//        return false;
//    }
//
//    public void searchTranferts() {
////        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
////            if (currentUser.getUsers().getAccesMultiAgence()) {
////                champ = new String[]{"societe", "numDoc"};
////                val = new Object[]{currentScte, "%" + numSearch_ + "%"};
////                nameQueri = "YvsBasePieceTresorerie.findByReference";
////            } else {
////                champ = new String[]{"agence", "numDoc"};
////                val = new Object[]{currentAgence, "%" + numSearch_ + "%"};
////                nameQueri = "YvsBasePieceTresorerie.findByAgenceReference";
////            }
////            pieces = dao.loadNameQueries(nameQueri, champ, val, idebut, ifin);
////        } else {
////            loadAllPieces();
////        }
//        update("data_piece_tresor");
//    }
//
//    public boolean setNumeroPiece(PieceTresorerie bean) {
//        String ref = genererReference(Constantes.TYPE_PT_NAME, bean.getDatePiece());
//        if (ref == null || ref.trim().equals("")) {
//            return false;
//        }
//        bean.setNumPiece(ref);
//        switch (bean.getSource()) {
//            case Constantes.SCR_ACHAT:
//                bean.setNumRef(bean.getNumPiece() + " - " + achat.getNumDoc());
//                break;
//            case Constantes.SCR_VENTE:
//                bean.setNumRef(bean.getNumPiece() + " - " + vente.getNumDoc());
//                break;
//            case Constantes.SCR_DIVERS:
//                bean.setNumRef(bean.getNumPiece() + " - " + diver.getNumPiece());
//                break;
//            default:
//                break;
//        }
//        return true;
//    }
//
//    @Override
//    public void updateBean() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//}

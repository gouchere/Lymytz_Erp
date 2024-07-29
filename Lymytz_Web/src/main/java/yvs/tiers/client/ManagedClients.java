///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.tiers.client;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.SessionScoped;
//import javax.faces.context.FacesContext;
//import javax.faces.event.ValueChangeEvent;
//import org.primefaces.context.RequestContext;
//import org.primefaces.event.SelectEvent;
//import org.primefaces.event.TabChangeEvent;
//import org.primefaces.model.chart.PieChartModel;
//import yvs.dao.Options;
//import yvs.entity.base.YvsBaseCategorieComptable;
//import yvs.entity.grh.activite.YvsContactsEmps;
//import yvs.entity.param.YvsAgences;
////import yvs.entity.param.YvsModelDeReglement;
//import yvs.entity.param.YvsTraiteMdr;
//import yvs.entity.produits.YvsArticles;
//import yvs.entity.produits.YvsPlanDeRecompense;
//import yvs.entity.produits.YvsPlanTarif;
//import yvs.entity.produits.YvsPlanTarifPK;
//import yvs.entity.produits.group.YvsBorneTranches;
//import yvs.entity.produits.group.YvsCatTarif;
//import yvs.entity.produits.group.YvsTranches;
//import yvs.entity.tiers.YvsContactsTiers;
//import yvs.entity.tiers.YvsTiers;
//import yvs.parametrage.agence.UtilAgence;
//import yvs.parametrage.mdr.TraiteMdr;
//import yvs.tiers.Contacts;
//import yvs.tiers.ManagedTiers;
//import yvs.tiers.PlanTarifClient;
//import yvs.util.BorneTranche;
//import yvs.util.Constantes;
//import yvs.util.TrancheVal;
//
///**
// *
// * @author GOUCHERE YVES
// */
//@ManagedBean(name = "Mclient")
//@SessionScoped
//public class ManagedClients extends ManagedTiers {
//
//    @ManagedProperty(value = "#{client}")
//    private Clients client;
//    @ManagedProperty(value = "#{contact}")
//    private Contacts contact;
//    @ManagedProperty(value = "#{tarifClt}")
//    private PlanTarifClient tarif;
//    private Clients selectClient;
//    private List<Clients> listClient;
//    private boolean disableCodeClient;
//    private boolean disableCodeFseur;
//    private boolean disableCodeRep;
//    private String defaultButton = "Clients-bSave";
//    private String defaultButtonClt = "clt-bSave";//pour la vue de création des clts.
//    private boolean renderOnglet;
//    private PieChartModel pieModel;
//    private List<TraiteMdr> listTraite;
//    private Contacts selectedContact;
//    private boolean tranche;
//    private List<BorneTranche> listBorneTranche;
//    YvsAgences ag;
//    private PlanTarifClient selectTarifClt;
//    private BorneTranche selectBorne;
//
//    public ManagedClients() {
//        listClient = new ArrayList<>();
//        listTraite = new ArrayList<>();
//        pieModel = new PieChartModel();
//        listBorneTranche = new ArrayList<>();
//    }
//
//    @Override
//    public Contacts getContact() {
//        return contact;
//    }
//
//    @Override
//    public void setContact(Contacts contact) {
//        this.contact = contact;
//    }
//
//    public Clients getClient() {
//        return client;
//    }
//
//    public void setClient(Clients client) {
//        this.client = client;
//    }
//
//    @Override
//    public PlanTarifClient getTarif() {
//        return tarif;
//    }
//
//    @Override
//    public void setTarif(PlanTarifClient tarif) {
//        this.tarif = tarif;
//    }
//
//    public List<Clients> getListClient() {
//        return listClient;
//    }
//
//    public void setListClient(List<Clients> listClient) {
//        this.listClient = listClient;
//    }
//
//    @Override
//    public boolean isDisableCodeClient() {
//        return disableCodeClient;
//    }
//
//    @Override
//    public void setDisableCodeClient(boolean disableCodeClient) {
//        this.disableCodeClient = disableCodeClient;
//    }
//
//    @Override
//    public boolean isDisableCodeFseur() {
//        return disableCodeFseur;
//    }
//
//    public void setDisableCodeFseur(boolean disableCodeFseur) {
//        this.disableCodeFseur = disableCodeFseur;
//    }
//
//    public boolean isDisableCodeRep() {
//        return disableCodeRep;
//    }
//
//    public void setDisableCodeRep(boolean disableCodeRep) {
//        this.disableCodeRep = disableCodeRep;
//    }
//
//    @Override
//    public String getDefaultButton() {
//        return defaultButton;
//    }
//
//    @Override
//    public void setDefaultButton(String defaultButton) {
//        this.defaultButton = defaultButton;
//    }
//
//    public boolean isRenderOnglet() {
//        return renderOnglet;
//    }
//
//    public void setRenderOnglet(boolean renderOnglet) {
//        this.renderOnglet = renderOnglet;
//    }
//
//    public Clients getSelectClient() {
//        return selectClient;
//    }
//
//    public void setSelectClient(Clients selectClient) {
//        this.selectClient = selectClient;
//    }
//
//    @Override
//    public String getDefaultButtonClt() {
//        return defaultButtonClt;
//    }
//
//    @Override
//    public void setDefaultButtonClt(String defaultButtonClt) {
//        this.defaultButtonClt = defaultButtonClt;
//    }
//
//    @Override
//    public PieChartModel getPieModel() {
//        return pieModel;
//    }
//
//    @Override
//    public void setPieModel(PieChartModel pieModel) {
//        this.pieModel = pieModel;
//    }
//
//    @Override
//    public List<TraiteMdr> getListTraite() {
//        return listTraite;
//    }
//
//    @Override
//    public void setListTraite(List<TraiteMdr> listTraite) {
//        this.listTraite = listTraite;
//    }
//
//    @Override
//    public Contacts getSelectedContact() {
//        return selectedContact;
//    }
//
//    @Override
//    public void setSelectedContact(Contacts selectedContact) {
//        this.selectedContact = selectedContact;
//    }
//
//    @Override
//    public boolean isTranche() {
//        return tranche;
//    }
//
//    @Override
//    public void setTranche(boolean tranche) {
//        this.tranche = tranche;
//    }
//
//    @Override
//    public List<BorneTranche> getListBorneTranche() {
//        return listBorneTranche;
//    }
//
//    @Override
//    public void setListBorneTranche(List<BorneTranche> listBorneTranche) {
//        this.listBorneTranche = listBorneTranche;
//    }
//
//    @Override
//    public PlanTarifClient getSelectTarifClt() {
//        return selectTarifClt;
//    }
//
//    @Override
//    public void setSelectTarifClt(PlanTarifClient selectTarifClt) {
//        this.selectTarifClt = selectTarifClt;
//    }
//
//    @Override
//    public BorneTranche getSelectBorne() {
//        return selectBorne;
//    }
//
//    @Override
//    public void setSelectBorne(BorneTranche selectBorne) {
//        this.selectBorne = selectBorne;
//    }
//
//    public boolean controleFiche(Clients bean) {
//        if (bean.getNature() == null) {
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message.getMessage("choixNatureTiers"), "");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            return false;
//        }
//        if (bean.getNom() == null) {
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message.getMessage("nomTiers"), "");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            return false;
//        }
//        if (bean.isClient()) {
//            if (bean.getNom() == null) {
//                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message.getMessage("codeClient"), "");
//                FacesContext.getCurrentInstance().addMessage(null, msg);
//                return false;
//            }
//        }
//        if (bean.getAgence() == null) {
//            getMessage("Veuillez choisir l'agence", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
//        return true;
//    }
//    long key;
//
//    @Override
//    public boolean saveNew() {
//        Clients ti = recopieView();
//        if (controleFiche(ti)) {
//            YvsTiers t = buildEntityFromBean(ti);
//            t.setId(null);
//            t = (YvsTiers) dao.save1(t);
//            ti.setId(t.getId());
//            client.setId(t.getId());
//            client.setIdAgence(t.getAgence().getId());
//            listClient.add(0, ti);
//            setDisableSave(true);
//            setRenderOnglet(true);
//            update("mainClt");
//            update("tableclt");
//            defaultButton = "Clients-bUpdate";
//            succes();
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void resetFiche() {
//        super.resetFiche();
//        resetFiche(client);
//        setDisableSave(false);
//        setRenderOnglet(false);
//        update("mainClt");
//    }
//
//    @Override
//    public void updateBean() {
//        Clients ti = recopieView();
//        if (controleFiche(ti)) {
//            ti.setLastAuteur(getUserOnLine());
//            ti.setLastDateUpdate(df.format(new Date()));
//            YvsTiers t = buildEntityFromBean(ti);
//            dao.update(t);
//            listClient.set(listClient.indexOf(ti), ti);
//            succes();
//            update("mainClt");
//            update("tableclt");
//        }
//    }
//
//    @Override
//    public Clients recopieView() {
//        Clients tier = new Clients();
//        tier.setActif(client.isActif());
//        tier.setAdresse(client.getAdresse());
//        tier.setCodePostal(client.getCodePostal());
//        tier.setCategorieComptable(client.getCategorieComptable());
//        tier.setCategorieTarifaire(client.getCategorieTarifaire());
//        tier.setCivilite(client.getCivilite());
//        tier.setClient(client.isClient());
//        tier.setCodeBarre(client.getCodeBarre());
//        tier.setCodeClient(client.getCodeClient());
//        tier.setCodeFournisseur(client.getCodeFournisseur());
//        tier.setCodeRepresentant(client.getCodeRepresentant());
//        tier.setCompteCollectif(client.getCompteCollectif());
//        tier.setEmail(client.getEmail());
//        tier.setId(client.getId());
//        tier.setLogo(client.getLogo());
//        tier.setNom(client.getNom());
//        tier.setPays(client.getPays());
//        tier.setPlanRistourne(client.getPlanRistourne());
//        tier.setPlanComission(client.getPlanComission());
//        tier.setLocalisation(client.getLocalisation());
//        tier.setStatut(client.getStatut());
//        tier.setTelephone(client.getTelephone());
//        tier.setUtilisateurRatache(client.getUtilisateurRatache());
//        tier.setVille(client.getVille());
//        tier.setAgence(client.getAgence());
//        tier.setModelDeRglt(client.getModelDeRglt());
//        tier.setModelDeRglt(client.getModelDeRglt());
//        if (client.getDateSave() != null) {
//            tier.setDateSave(client.getDateSave());
//        } else {
//            tier.setDateSave(df.format(new Date()));
//        }
//        if (client.getLastDateUpdate() != null) {
//            tier.setLastDateUpdate(client.getLastDateUpdate());
//        } else {
//            tier.setLastDateUpdate(df.format(new Date()));
//        }
//        if (client.getAuteur() != null) {
//            tier.setAuteur(client.getAuteur());
//        } else {
//            tier.setAuteur(getUserOnLine());
//        }
//        if (client.getLastAuteur() != null) {
//            tier.setLastAuteur(client.getLastAuteur());
//        } else {
//            tier.setLastAuteur(getUserOnLine());
//        }
//        tier.setNature(Constantes.TIERS_CLIENT);
//        return tier;
//    }
//
//    public void populateView(Clients bean) {
//        client.setActif(bean.isActif());
//        client.setAdresse(bean.getAdresse());
//        client.setCodePostal(bean.getCodePostal());
//        client.setCategorieComptable(bean.getCategorieComptable());
//        client.setCategorieTarifaire(bean.getCategorieTarifaire());
//        //réccupère la catégorie tarifaire        
//        if (bean.getCategorieTarifaire() != 0) {
//            dao.setEntityClass(YvsCatTarif.class);
//            cat = (YvsCatTarif) dao.getOne(bean.getCategorieTarifaire());
//            disableCatT = cat.isOnlyTiers();
//        } else {
//            cat = null;
//        }
//        client.setCivilite(bean.getCivilite());
//        client.setClient(bean.isClient());
//        client.setCodeBarre(bean.getCodeBarre());
//        client.setCodeClient(bean.getCodeClient());
//        client.setCompteCollectif(bean.getCompteCollectif());
//        client.setEmail(bean.getEmail());
//        client.setId(bean.getId());
//        client.setLogo(bean.getLogo());
//        client.setNom(bean.getNom());
//        client.setPays(bean.getPays());
//        client.setPlanRistourne(bean.getPlanRistourne());
//        client.setPlanComission(bean.getPlanComission());
//        client.setModelDeRglt(bean.getModelDeRglt());
//        client.setLocalisation(bean.getLocalisation());
//        client.setStatut(bean.getStatut());
//        client.setTelephone(bean.getTelephone());
//        client.setUtilisateurRatache(bean.getUtilisateurRatache());
//        client.setDateSave(bean.getDateSave());
//        client.setLastDateUpdate(bean.getLastDateUpdate());
//        client.setAuteur(bean.getAuteur());
//        client.setLastAuteur(bean.getLastAuteur());
//        client.setVille(bean.getVille());
//        client.setAgence(bean.getAgence());
//        client.setListContact(bean.getListContact());
//        client.setIdAgence(bean.getIdAgence());
//        client.setListTarif(bean.getListTarif());
//    }
//
//    @Override
//    public void loadOnView(SelectEvent ev) {
//        Clients t = (Clients) ev.getObject();
//        populateView(t);
//        setDisableSave(true);
//        setRenderOnglet(true);
//        defaultButton = "Clients-bUpdate";
//        update("mainClt");
//    }
//
//    @Override
//    public void loadAll() {
//        listClient.clear();
//        String[] ch = {};
//        Object[] val = {};
//        List<YvsTiers> l = dao.loadNameQueries("YvsTiers.findByClient", ch, val);
//        for (YvsTiers t : l) {
//            listClient.add(buildBeanFromEntity(t));
//        }
//        createGraphe();
//    }
//
//    private Clients buildBeanFromEntity(YvsTiers t) {
//        if (t != null) {
//            Clients r = new Clients();
//            r.setId(t.getId());
//            r.setActif(t.getActif());
//            r.setAdresse(t.getAdresse());
//            r.setNom(t.getNom());
////            if (t.getCategorieComptable() != null) {
////                r.setCategorieComptable(t.getCategorieComptable().getId());
////            }
//            if (t.getCategorieTarifaire() != null) {
//                r.setCategorieTarifaire(t.getCategorieTarifaire().getId());
//            }
//            r.setCivilite(t.getCivilite());
//            r.setClient(t.getClient());
//            r.setCodeBarre(t.getCodeBarre());
//            r.setCodeClient(t.getCodeTiers());
//            r.setCodeFournisseur(t.getCodeTiers());
//            r.setCodePersonel(t.getCodeTiers());
//            r.setCodeRepresentant(t.getCodeTiers());
//            r.setCodePostal(t.getCodePostal());
//            r.setCompteCollectif(t.getCompte());
//            r.setEmail(t.getEmail());
//            r.setLocalisation(t.getPointDeVente());
//            r.setLogo(t.getLogo());
//            r.setNature(t.getClasse());
//            r.setNom(t.getNom());
////            r.setPays(t.getPays());
//            if (t.getComission() != null) {
//                r.setPlanComission(t.getComission().getId());
//            }
//            if (t.getRistourne() != null) {
//                r.setPlanRistourne(t.getRistourne().getId());
//            }
////            if (t.getMdr() != null) {
////                r.setModelDeRglt(t.getMdr().getId());
////            }
//            r.setSociete(t.getSociete());
//            r.setStatut(t.getStatut());
//            r.setTelephone(t.getTel());
////            r.setVille(t.getVille());
//            r.setAgence(UtilAgence.buildBeanAgence(t.getAgence()));
//            r.setIdAgence(t.getAgence().getId());
//
//            //charge les contacts
//            for (YvsContactsTiers ct : t.getYvsContactsTiersList()) {
//                if (r.getListContact() == null) {
//                    List<Contacts> l = new ArrayList<>();
//                    r.setListContact(l);
//                }
//                r.getListContact().add(buildContact(ct));
//            }
//            //charge les tarif
//
//            if (t.getCategorieTarifaire() != null) {
//                if (t.getCategorieTarifaire().isOnlyTiers()) {
//                    String[] ch = {"categorie"};
//                    Object[] val = {t.getCategorieTarifaire().getId()};
//                    List<YvsPlanTarif> lpt = dao.loadNameQueries("YvsPlanTarif.findByCategorie", ch, val);
//                    t.getCategorieTarifaire().setYvsPlanTarifList(lpt);
//                    if (r.getListTarif() == null) {
//                        List<PlanTarifClient> l = new ArrayList<>();
//                        r.setListTarif(l);
//                    }
//                    r.getListTarif().clear();
//                    for (YvsPlanTarif ct : t.getCategorieTarifaire().getYvsPlanTarifList()) {
//                        r.getListTarif().add(buildPlanTarif(ct));
//                    }
//                }
//            }
//            return r;
//        }
//        return null;
//    }
//
//    private YvsTiers buildEntityFromBean(Clients t) {
//        if (t != null) {
//            YvsTiers r = new YvsTiers(t.getId());
//            r.setActif(t.isActif());
//            r.setAdresse(t.getAdresse());
//            if (t.getCategorieComptable() != 0) {
////                r.setCategorieComptable(new YvsBaseCategorieComptable(t.getCategorieComptable()));
//            }
//            if (t.getCategorieTarifaire() != 0) {
//                r.setCategorieTarifaire(new YvsCatTarif(t.getCategorieTarifaire()));
//            }
//            r.setCivilite(t.getCivilite());
//            r.setClient(t.isClient());
//            r.setCodeBarre(t.getCodeBarre());
//            r.setCodeTiers(t.getCodeClient());
//            r.setCodePostal(t.getCodePostal());
//            r.setEmail(t.getEmail());
//            r.setPointDeVente(t.getLocalisation());
//            r.setLogo(t.getLogo());
//            r.setClasse(t.getNature());
//            r.setNom(t.getNom());
////            r.setPays(t.getPays());
//            if (t.getPlanComission() != 0) {
//                r.setComission(new YvsPlanDeRecompense(t.getPlanComission()));
//            }
//            if (t.getPlanRistourne() != 0) {
//                r.setRistourne(new YvsPlanDeRecompense(t.getPlanRistourne()));
//            }
//            if (t.getModelDeRglt() != 0) {
////                r.setMdr(new YvsModelDeReglement(t.getModelDeRglt()));
//            }
//            r.setSociete(t.isSociete());
//            r.setStatut(t.getStatut());
//            r.setTel(t.getTelephone());
////            r.setVille(t.getVille());
//            //réccupère l'agence
//            String[] ch = {"codeagence"};
//            Object[] val = {t.getAgence()};
//            dao.setEntityClass(YvsAgences.class);
//            ag = (YvsAgences) dao.getOne(ch, val);
//            client.setIdAgence(ag.getId());
//            r.setAgence(ag);
//            return r;
//        }
//        return null;
//    }
//
//    @Override
//    public void choixSociete(ValueChangeEvent ev) {
//        String statut = (String) ev.getNewValue();
//        if (statut.equals("Individu")) {
//            client.setSociete(false);
//        } else {
//            client.setSociete(true);
//        }
//    }
//
//    @Override
//    public void delete() {
//        YvsTiers tier = buildEntityFromBean(selectClient);
//        selectClient.setActif(false);
//        tier.setActif(false);
//        tier.setSup(true);
//        selectClient.setLastAuteur(getUserOnLine());
//        selectClient.setLastDateUpdate(df.format(new Date()));
//        dao.update(tier);
//        listClient.remove(selectClient);
//        update("tableclt");
//        succes();
//    }
//
//    @Override
//    public void disable() {
//        YvsTiers tier = buildEntityFromBean(selectClient);
//        selectClient.setActif(false);
//        tier.setActif(false);
//        selectClient.setLastAuteur(getUserOnLine());
//        selectClient.setLastDateUpdate(df.format(new Date()));
//        dao.update(tier);
//        listClient.set(listClient.indexOf(selectClient), selectClient);
//        update("tableclt");
//        succes();
//    }
//
//    private void createGraphe() {
//        if (!listClient.isEmpty()) {
//            Collections.sort(listClient, new Clients());
//            String art = listClient.get(0).getAgence().getCodeAgence();
//            int count = 0;
//            int nbre = 1;
//            for (Clients tie : listClient) {
//                if (tie.getAgence().equals(art)) {
//                    count++;
//                } else {
//                    pieModel.set(art, count);
//                    count = 1;
//                    art = tie.getAgence().getCodeAgence();
//                }
//                if (nbre == listClient.size()) {
//                    pieModel.set(art, count);
//                }
//                nbre++;
//            }
//        }
//    }
//    private TraiteMdr traite;
//
//    public void chooseMdr(ValueChangeEvent ev) {
////        String[] ch = {"mdr"};
////        Object[] val = {new YvsModelDeReglement((Integer) ev.getNewValue())};
////        List<YvsTraiteMdr> l = dao.loadNameQueries("YvsTraiteMdr.findByModeDelReglement", ch, val);
////        listTraite.clear();
////        if (l != null) {
////            for (YvsTraiteMdr t : l) {
////                traite = new TraiteMdr(t.getId());
////                traite.setDuree(t.getDuree());
////                traite.setModeDeReglement(t.getModeDeReglement());
////                traite.setNatureTraite(t.getNature());
////                traite.setPourcentage(t.getPourcentage());
////                listTraite.add(traite);
////
////            }
////        }
//    }
//    /**
//     * Ajouter des contacts
//     */
//    Contacts c = null;
//    YvsContactsTiers yvsC = null;
//
//    private boolean controleContact(Contacts c) {
//        if (c.getNom() == null) {
//            getMessage("Veuillez indiquer le nom de ce contact !", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
//        if (c.getTel() == null && c.getEmail() == null) {
//            getMessage("Veuillez indiquer un moyen de contact (mobile ou electronique)!", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
//        return true;
//    }
//
//    public void addContact() {
//        recopieContact();
//        if (client.getId() != 0) {
//            if (controleContact(c)) {
//                if (client.getListContact() == null) {
//                    List<Contacts> l = new ArrayList<>();
//                    client.setListContact(l);
//                }
//                buildContact(c);
//                if (!client.getListContact().contains(c)) {
//                    //on créée un noveaau contact  
//                    yvsC.setId(null);
//                    YvsContactsEmps id = (YvsContactsEmps) dao.save1(yvsC);
//                    c.setId(id.getId());
//                    client.getListContact().add(c);
//                } else {
//                    //on modifie un contact
//                    dao.update(yvsC);
//                    client.getListContact().set(client.getListContact().indexOf(c), c);
//                }
//                update("mainClt:tableContactClt");
//            }
//        }
//    }
//
//    public void detacherContact() {
//        if (selectedContact != null) {
//            dao.delete(new YvsContactsTiers((int)selectedContact.getId()));
//            client.getListContact().remove(selectedContact);
//            update("mainClt:tableContactClt");
//        }
//    }
//
//    public void slectContact(SelectEvent ev) {
//        Contacts ct = (Contacts) ev.getObject();
//        if (ct != null) {
//            contact.setId(ct.getId());
//            contact.setAdresse(ct.getAdresse());
//            contact.setCivilite(ct.getCivilite());
//            contact.setEmail(ct.getEmail());
//            contact.setIdTiers(ct.getIdTiers());
//            contact.setNom(ct.getNom());
//            contact.setTel(ct.getTel());
//            update("mainClt:Clt-contact");
//        }
//    }
//
//    private YvsContactsTiers buildContact(Contacts c) {
//        yvsC = new YvsContactsTiers((int)c.getId());
//        yvsC.setAdress(c.getAdresse());
//        yvsC.setCivilite(c.getCivilite());
//        yvsC.setEmail(c.getEmail());
//        yvsC.setTiers(new YvsTiers(c.getIdTiers()));
//        yvsC.setNom(c.getNom());
//        yvsC.setTelephone(c.getTel());
//        return yvsC;
//    }
//
//    private Contacts buildContact(YvsContactsTiers yvsc) {
//        c = new Contacts(yvsc.getId(), yvsc.getNom());
//        c.setAdresse(yvsc.getAdress());
//        c.setCivilite(yvsc.getCivilite());
//        c.setEmail(yvsc.getEmail());
//        if (yvsc.getTiers() != null) {
//            c.setIdTiers(yvsc.getTiers().getId());
//        }
//        c.setTel(yvsc.getTelephone());
//        return c;
//
//    }
//
//    private PlanTarifClient buildPlanTarif(YvsPlanTarif ta) {
//        PlanTarifClient t = new PlanTarifClient();
//        TrancheVal tranch = null;
//        //si le plan tarifaire est en tranche, on charge les bornes
//        if (ta.getIdTranche() != null) {
//            tranch = new TrancheVal();
//            tranch.setId(ta.getIdTranche().getId());
//            tranch.setModelTranche(ta.getIdTranche().getModelTranche());
//            //récupération des bornes
////            String[] ch = {"tranche"};
////            Object[] val = {ta.getIdTranche()};
////            List<Object[]> r = dao.loadNameQueries("YvsBorneTranches.findByTranche", ch, val);
//////            List<YvsBorneTranches> l = buildBorne(r);
////            List<YvsBorneTranches> l = ta.getIdTranche().getYvsBorneTranchesList();
////            ta.getIdTranche().setYvsBorneTranchesList(l);
//            List<BorneTranche> lb = new ArrayList<>();
//            for (YvsBorneTranches b : ta.getIdTranche().getYvsBorneTranchesList()) {
//                lb.add(buildBorne(b));
//            }
//            tranch.setListBorne(lb);
//        }
//        t.setRefArticle(ta.getYvsArticles().getRefArt());
//        t.setIdArticle(ta.getYvsArticles().getId());
//        t.setIdTranche(tranch);
//        t.setPrix(ta.getPrix());
//        t.setRemise(ta.getRemise());
//        return t;
//    }
//
//    private BorneTranche buildBorne(YvsBorneTranches b) {
//        BorneTranche bo = new BorneTranche();
//        bo.setBorne(b.getBorne());
//        bo.setId(b.getId());
//        bo.setIdTranche(b.getTranche().getId());
//        bo.setPrix(b.getPrix());
//        bo.setRemise(b.getRemise());
//        return bo;
//    }
//
//    private Contacts recopieContact() {
//        c = new Contacts();
//        c.setId(contact.getId());
//        c.setAdresse(contact.getAdresse());
//        c.setCivilite(contact.getCivilite());
//        c.setEmail(contact.getEmail());
//        c.setIdTiers(client.getId());
//        c.setNom(contact.getNom());
//        c.setTel(contact.getTel());
//        return c;
//    }
//    /**
//     * *
//     * Ajouter un plan tarifaire*
//     */
//    private String modelTranche;
//    private int borne;
//    private double remise;
//    private double prix;
//    private boolean disableCatT;
//
//    public String getModelTranche() {
//        return modelTranche;
//    }
//
//    @Override
//    public void setModelTranche(String modelTranche) {
//        this.modelTranche = modelTranche;
//    }
//
//    @Override
//    public int getBorne() {
//        return borne;
//    }
//
//    @Override
//    public void setBorne(int borne) {
//        this.borne = borne;
//    }
//
//    @Override
//    public double getPrix() {
//        return prix;
//    }
//
//    @Override
//    public void setPrix(double prix) {
//        this.prix = prix;
//    }
//
//    @Override
//    public double getRemise() {
//        return remise;
//    }
//
//    @Override
//    public void setRemise(double remise) {
//        this.remise = remise;
//    }
//
//    @Override
//    public boolean isDisableCatT() {
//        return disableCatT;
//    }
//
//    @Override
//    public void setDisableCatT(boolean disableCatT) {
//        this.disableCatT = disableCatT;
//    }
//
//    @Override
//    public void openBorne(ValueChangeEvent ev) {
//        boolean b = (Boolean) ev.getNewValue();
//        if (b) {
//            tarif.setRemise(0);
//            borne = 1;
//            listBorneTranche.clear();
////            remise = t.getRemise();
////            prix = article.getPuv();
//            update("Clt-bo-tarif");
//            update("Clt-bo-grid");
//            RequestContext.getCurrentInstance().execute("tranche.show()");
//        }
//    }
//
//    @Override
//    public void changeVal(ValueChangeEvent ev) {
//        if (ev.getOldValue() != ev.getNewValue()) {
//            if (tranche) {
//                tranche = false;
//            } else {
//                tarif.setRemise(1);
//            }
//        }
//    }
//
//    @Override
//    public void selectTarif(SelectEvent ev) {
//        PlanTarifClient t = (PlanTarifClient) ev.getObject();
//        if (t != null) {
//            tarif.setRefArticle(t.getRefArticle());
//            tarif.setIdArticle(t.getIdArticle());
//            tarif.setPrix(t.getPrix());
//            tarif.setRemise(t.getRemise());
//            listBorneTranche.clear();
//            if (t.getIdTranche() != null) {
//                tranche = true;
//                tarif.setRemise(0);
//                listBorneTranche.addAll(t.getIdTranche().getListBorne());
//                RequestContext.getCurrentInstance().execute("tranche.show()");
//            } else {
//                tranche = false;
//                tarif.setRemise(t.getRemise());
//                tarif.setPrix(t.getPrix());
//            }
//            Collections.sort(listBorneTranche, new BorneTranche());
//            update("Clt-bo-tarif");
//            update("mainClt:Clt-ch_catT");
//            update("mainClt:Clt-ch_catT1");
//            update("A-Clt-tarif");
//        }
//    }
//    /*
//     * l'ajout d'un plan tarifaire à un Clients consiste à créer une catégorie
//     * tarifaire implicete à laquelle on rattachera ce Clients. on doit
//     * s'assurer que le Clients est bien lié à une agence
//     */
//
//    private YvsCatTarif buildCatTarif() {
//        YvsCatTarif cat1 = new YvsCatTarif();
//        cat1.setActif(true);
//        cat1.setAuthor(getUserOnLine());
//        cat1.setDateSave(new Date());
//        cat1.setDesignation("C-" + client.getNom());
//        cat1.setLastAuthor(getUserOnLine());
//        cat1.setLastDateSave(new Date());
//        cat1.setOnlyTiers(true);
//        return cat1;
//    }
//    YvsCatTarif cat;
//
//    private void createCat() {
//        cat = buildCatTarif();
//        cat = (YvsCatTarif) dao.save1(cat);
//        //modifie la catégorie tarifaire du Clients
//        String rq = "UPDATE yvs_tiers SET categorie_tarifaire=? WHERE id=?";
//        Options[] op = {new Options(cat.getId(), 1), new Options(client.getId(), 2)};
//        dao.requeteLibre(rq, op);
//        client.setCategorieTarifaire(cat.getId());
//        disableCatT = true;
//        update("mainClt");
//    }
//
//    @Override
//    public void addTarif() {
//        if (client.getIdAgence() != 0) {
//            //cration de la catégorie tarifaire
//            if (cat != null) {
//                //si la catégorie chargé est déjà une catégorie implicete pour un tier, on suppose que 
//                //l'utilisateur veux juste modifier le plan tarifaire associé
//                if (!cat.isOnlyTiers()) {
//                    createCat();
//                }
//            } else {
//                createCat();
//            }
//            addTarif(cat);
//        } else {
//            getMessage("Le Clients doit être rattaché à une agence", FacesMessage.SEVERITY_ERROR);
//        }
//    }
//    PlanTarifClient planTarif;
//    YvsPlanTarif yvsPlanTarif;
//    PlanTarifClient toUpdate;
//
//    @Override
//    public void addTarif(YvsCatTarif catT) {
//        if (tarif.getRefArticle() != null) {
//            String[] ch = {"refArt"};
//            Object[] val = {tarif.getRefArticle()};
//            planTarif = new PlanTarifClient();
//            yvsPlanTarif = new YvsPlanTarif();
//            dao.setEntityClass(YvsArticles.class);
//            YvsArticles art = (YvsArticles) dao.getOne(ch, val);
//            //charge l'agence            
//            yvsPlanTarif.setYvsPlanTarifPK(new YvsPlanTarifPK(catT.getId(), art.getId()));
//            planTarif.setRefArticle(art.getRefArt());
//            planTarif.setIdArticle(art.getId());
//            planTarif.setRemise(tarif.getRemise());
//            planTarif.setPrix(tarif.getPrix());
//            if (client.getListTarif() == null) {
//                List<PlanTarifClient> l = new ArrayList<>();
//                client.setListTarif(l);
//            }
//            if (!client.getListTarif().contains(planTarif)) {
//                if (tranche && !listBorneTranche.isEmpty()) {
//                    TrancheVal tr = new TrancheVal(0, modelTranche);
//                    //je persiste la tranche
//                    YvsTranches tranch = tr.buildTranche(tr);
//                    tranch = (YvsTranches) dao.save1(tranch);
//                    tr.setId(tranch.getId());
//                    //parcour la liste des borne
//                    int pos = 0;
//                    for (BorneTranche b : listBorneTranche) {
//                        YvsBorneTranches bt = b.buildBorne(b);
//                        bt.setId(null);
//                        bt.setTranche(tranch);
//                        bt = (YvsBorneTranches) dao.save1(bt);
//                        b.setIdTranche(bt.getId());
//                        b.setId(bt.getId());
//                        listBorneTranche.set(pos, b);
//                        pos++;
//                    }
//                    List<BorneTranche> l = new ArrayList<>();
//                    l.addAll(listBorneTranche);
//                    tr.setListBorne(l);
//
//                    planTarif.setIdTranche(tr);
//                    yvsPlanTarif.setIdTranche(tranch);
//                } else if (tranche && listBorneTranche.isEmpty()) {
//                    getMessage("Vous avez indiqué que ce plan était repartie en tranche, cependant vous n'avez mis à jour aucune tranches", FacesMessage.SEVERITY_WARN);
//                    return;
//                } else {
//                    yvsPlanTarif.setIdTranche(null);
//                    planTarif.setIdTranche(null);
//                }
//                yvsPlanTarif.setPrix(planTarif.getPrix());
//                yvsPlanTarif.setRemise(planTarif.getRemise());
//                dao.save(yvsPlanTarif);
//                client.getListTarif().add(0, planTarif);
//                Clients ct = new Clients();
//                ct.setId(client.getId());
//                int idx = listClient.indexOf(ct);
//                if (listClient.get(idx).getListTarif() == null) {
//                    listClient.get(idx).setListTarif(client.getListTarif());
//                }
////                selectTiers.getListTarif().add(planTarif);
////                listTiers.set(listTiers.indexOf(Clients), Clients);
//                listBorneTranche.clear();
//                borne = 1;
//                remise = 1;
//                tarif.setRemise(1);
//                tranche = false;
//                update("Clt-ta-tarif");
//                update("Clt-bo-tarif");
//                update("tableclt");
//            } else {
//                //modification du tarif-groupe
//                //on vérifie si la ligne tarif-categorie était lié à une tranche.
//                int index = client.getListTarif().indexOf(new PlanTarifClient(tarif.getRefArticle()));
//                toUpdate = client.getListTarif().get(index);
//                if (toUpdate.getIdTranche() != null && !tranche) {
//                    //s'il y avait une tranche et qu'il y en a plus, on informe l'utilisateur                    
//                    getMessage("annullation de tranches", FacesMessage.SEVERITY_ERROR);
//                    openDialog("delTranche");
//                } else {
//                    //si la forma du plan tarifaire est inchangé,    
//                    if (toUpdate.getIdTranche() != null) {
//                        //on modifie les tranches s'il y en a   
//                        List<BorneTranche> lb = toUpdate.getIdTranche().getListBorne();
//                        int pos = 0;
//                        for (BorneTranche b : listBorneTranche) {
//                            YvsBorneTranches bb = b.buildBorne(b);
//                            bb.setTranche(new YvsTranches(toUpdate.getIdTranche().getId()));
//                            bb = (YvsBorneTranches) dao.update(bb);
//                            long id = b.getId();
//                            b.setId(bb.getId());
//                            //modifie sur la vue
//                            listBorneTranche.set(pos, b);
//                            //modifie l'objet toUpdate
//                            if (toUpdate.getIdTranche().getListBorne().contains(new BorneTranche(id))) {
//                                toUpdate.getIdTranche().getListBorne().set(toUpdate.getIdTranche().getListBorne().indexOf(new BorneTranche(id)), b);
//                            } else {
//                                toUpdate.getIdTranche().getListBorne().add(b);
//                            }
//                            pos++;
//                            if (lb.contains(b));
//                            lb.remove(b);
//                        }
//                        supprimeBorne(lb);
//                        client.getListTarif().set(client.getListTarif().indexOf(toUpdate), toUpdate);
////                        article.getListTarifArt().get(art.getListTarifArt().indexOf(new PlanTarifArticles(tarif.getRefCategorie(), tarif.getRefAgence()))).getIdTranche().setListBorne(listBorneTranche);
//                    } else {
//                        //s'il y a des tranches
//                        if (tranche && !listBorneTranche.isEmpty()) {
//                            TrancheVal tr = new TrancheVal(0, modelTranche);
//                            YvsTranches tranch = tr.buildTranche(tr);
//                            tranch = (YvsTranches) dao.save1(tranch);
//                            tr.setId(tranch.getId());
//                            //parcour la liste des borne
//                            int pos = 0;
//                            for (BorneTranche b : listBorneTranche) {
//                                YvsBorneTranches bt = b.buildBorne(b);
//                                bt.setId(null);
//                                bt.setTranche(tranch);
//                                bt = (YvsBorneTranches) dao.save1(bt);
//                                b.setIdTranche(bt.getId());
//                                b.setId(bt.getId());
//                                listBorneTranche.set(pos, b);
//                            }
//                            List<BorneTranche> l = new ArrayList<>();
//                            l.addAll(listBorneTranche);
//                            tr.setListBorne(l);
//                            toUpdate.setIdTranche(tr);
//                            YvsPlanTarif pt = new YvsPlanTarif(new YvsPlanTarifPK(catT.getId(), toUpdate.getIdArticle()));
//                            pt.setIdTranche(tranch);
//                            pt.setPrix(0);
//                            pt.setRemise(0);
//                            dao.update(pt);
//                            client.getListTarif().set(client.getListTarif().indexOf(toUpdate), toUpdate);
//                            toUpdate = null;
//                        } else {
//                            //s'il n'y a pas de tranche,                             
//                            toUpdate.setRemise(tarif.getRemise());
//                            toUpdate.setPrix(tarif.getPrix());
//                            YvsPlanTarif pt = new YvsPlanTarif(new YvsPlanTarifPK(catT.getId(), toUpdate.getIdArticle()));
//                            pt.setRemise(tarif.getRemise());
//                            pt.setPrix(tarif.getPrix());
//                            dao.update(pt);
//                            client.getListTarif().set(client.getListTarif().indexOf(toUpdate), toUpdate);
//                        }
//                    }
//                    succes();
//                    toUpdate = null;
//                }
//            }
//        } else {
//            getMessage(message.getMessage("choose_catTarif"), FacesMessage.SEVERITY_ERROR);
//        }
//    }
//
//    private void supprimeBorne(List<BorneTranche> l) {
//        for (BorneTranche bb : l) {
//            YvsBorneTranches b = new YvsBorneTranches(bb.getId());
//            b.supprimeBorne(b);
//        }
//    }
//    int idBorne = 0;
//
//    @Override
//    public void addBorne() {
//        BorneTranche b = new BorneTranche();
//        b.setBorne(getBorne());
//        b.setRemise(getRemise());
//        b.setId(idBorne++);
//        b.setPrix(getPrix());
//        listBorneTranche.add(0, b);
//        update("Clt-bo-tarif");
//    }
//
//    @Override
//    public void deleteBorne() {
//        try {
//            listBorneTranche.remove(selectBorne);
//            update("Clt-bo-tarif");
//        } catch (Exception ex) {
//            listBorneTranche.remove(selectBorne);
//            update("Clt-bo-tarif");
//        }
//    }
//
//    @Override
//    public void deletTarifGroup() {
//        YvsPlanTarif tar = new YvsPlanTarif();
//        tar.setYvsPlanTarifPK(new YvsPlanTarifPK(cat.getId(), selectTarifClt.getIdArticle()));
//        dao.delete(tar);
//        //si le tarif est lié à une tranche, on la supprime aussi
//        if (selectTarifClt.getIdTranche() != null) {
//            YvsTranches t = new YvsTranches(selectTarifClt.getIdTranche().getId());
//            dao.delete(t);
//        }
////        PlanTarifClient t = new PlanTarifClient();
////        t.setRefCategorie(selectTarifGroup.getRefCategorie());
//        client.getListTarif().remove(selectTarifClt);
//        update("Clt-groupP-table");
//        update("Clt-ta-tarif");
//    }
//
////cette methode est invoqué lorsqu'on  change d'onglet 
//    @Override
//    public void changeDefaultButon(TabChangeEvent ev) {
//        String id = ev.getTab().getId();
//        switch (id) {
//            case "Clt-tab-gen":
//                if (isDisableSave()) {
//                    setDefaultButton("Clients-bUpdate");
//                } else {
//                    setDefaultButton("Clients-bSave");
//                }
//                break;
//            case "Clt-tab-adresse":
//                if (isDisableSave()) {
//                    setDefaultButton("Clients-bUpdate");
//                } else {
//                    setDefaultButton("Clients-bSave");
//                }
//                break;
//            case "Clt-tab-complt":
//                if (isDisableSave()) {
//                    setDefaultButton("Clients-bUpdate");
//                } else {
//                    setDefaultButton("Clients-bSave");
//                }
//                break;
//            case "Clt-tab-tarif":
//                setDefaultButton("maiTiers:tier-buton-addTarif");
//                break;
//            case "Clt-tab-contact":
//                setDefaultButton("maiTiers:b-add-contact");
//                break;
//            default:
//                setDefaultButton("Clients-bSave");
//                break;
//        }
//        update("Clt-default");
//    }
//}

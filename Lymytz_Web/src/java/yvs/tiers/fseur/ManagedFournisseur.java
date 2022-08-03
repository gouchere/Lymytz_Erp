///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.tiers.fseur;
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
//import yvs.entity.param.YvsAgences;
////import yvs.entity.param.YvsCatcompta;
////import yvs.entity.param.YvsModelDeReglement;
//import yvs.entity.produits.YvsBaseArticles;
//import yvs.entity.produits.YvsPlanDeRecompense;
//import yvs.entity.produits.YvsBasePlanTarifaireArticle;
//import yvs.entity.produits.group.YvsBorneTranches;
//import yvs.entity.produits.group.YvsCatTarif;
//import yvs.entity.produits.group.YvsTranches;
//import yvs.entity.tiers.YvsContactsTiers;
//import yvs.entity.tiers.YvsBaseTiers;
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
//@ManagedBean(name = "Mfseur")
//@SessionScoped
//public class ManagedFournisseur extends ManagedTiers {
//
//    @ManagedProperty(value = "#{fseur}")
//    private Fournisseur fournisseur;
//    @ManagedProperty(value = "#{contact}")
//    private Contacts contact;
//    @ManagedProperty(value = "#{tarifFseur}")
//    private PlanTarifClient tarif;
//    private Fournisseur selectFournisseur;
//    private List<Fournisseur> listfFournisseurs;
//    private boolean disableCodeFseur;
//    private String defaultButton = "Fournisseur-bSave";
//    private String defaultButtonClt = "Fseur-bSave";//pour la vue de création des Fseurs.
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
//    public ManagedFournisseur() {
//        listfFournisseurs = new ArrayList<>();
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
//    public Fournisseur getFournisseur() {
//        return fournisseur;
//    }
//
//    public void setFournisseur(Fournisseur fournisseur) {
//        this.fournisseur = fournisseur;
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
//    public List<Fournisseur> getListfFournisseurs() {
//        return listfFournisseurs;
//    }
//
//    public void setListfFournisseurs(List<Fournisseur> listfFournisseurs) {
//        this.listfFournisseurs = listfFournisseurs;
//    }
//
//    @Override
//    public boolean isDisableCodeFseur() {
//        return disableCodeFseur;
//    }
//
//    @Override
//    public void setDisableCodeFseur(boolean disableCodeFseur) {
//        this.disableCodeFseur = disableCodeFseur;
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
//    @Override
//    public boolean isRenderOnglet() {
//        return renderOnglet;
//    }
//
//    @Override
//    public void setRenderOnglet(boolean renderOnglet) {
//        this.renderOnglet = renderOnglet;
//    }
//
//    public void setSelectFournisseur(Fournisseur selectFournisseur) {
//        this.selectFournisseur = selectFournisseur;
//    }
//
//    public Fournisseur getSelectFournisseur() {
//        return selectFournisseur;
//    }
//
//    @Override
//    public String getDefaultButtonClt() {
//        return defaultButtonClt;
//    }
//
//    @Override
//    public void setDefaultButtonClt(String defaultButtonFseur) {
//        this.defaultButtonClt = defaultButtonFseur;
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
//    public void setSelectTarifClt(PlanTarifClient selectTarifFseur) {
//        this.selectTarifClt = selectTarifFseur;
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
//    public boolean controleFiche(Fournisseur bean) {
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
//
//        return true;
//    }
//    long key;
//
//    @Override
//    public boolean saveNew() {
//        Fournisseur ti = recopieView();
//        if (controleFiche(ti)) {
//            YvsBaseTiers t = buildEntityFromBean(ti);
//            t.setId(null);
//            t = (YvsBaseTiers) dao.save1(t);
//            ti.setId(t.getId());
//            fournisseur.setId(t.getId());
//            listfFournisseurs.add(0, ti);
//            setDisableSave(true);
//            setRenderOnglet(true);
//            update("mainFseur");
//            update("tableFseur");
//            defaultButton = "Fournisseur-bUpdate";
//            succes();
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void resetFiche() {
//        super.resetFiche();
//        resetFiche(fournisseur);
//        setDisableSave(false);
//        setRenderOnglet(false);
//        update("mainFseur");
//    }
//
//    @Override
//    public void updateBean() {
//        Fournisseur ti = recopieView();
//        if (controleFiche(ti)) {
//            ti.setLastAuteur(getUserOnLine());
//            ti.setLastDateUpdate(df.format(new Date()));
//            YvsBaseTiers t = buildEntityFromBean(ti);
//            dao.update(t);
//            listfFournisseurs.set(listfFournisseurs.indexOf(ti), ti);
//            succes();
//            update("mainFseur");
//            update("tableFseur");
//        }
//    }
//
//    @Override
//    public Fournisseur recopieView() {
//        Fournisseur tier = new Fournisseur();
//        tier.setActif(fournisseur.isActif());
//        tier.setAdresse(fournisseur.getAdresse());
//        tier.setCodePostal(fournisseur.getCodePostal());
//        tier.setCategorieComptable(fournisseur.getCategorieComptable());
//        tier.setCategorieTarifaire(fournisseur.getCategorieTarifaire());
//        tier.setCivilite(fournisseur.getCivilite());
//        tier.setClient(fournisseur.isClient());
//        tier.setCodeBarre(fournisseur.getCodeBarre());
//        tier.setCodeClient(fournisseur.getCodeClient());
//        tier.setCodeFournisseur(fournisseur.getCodeFournisseur());
//        tier.setCodeRepresentant(fournisseur.getCodeRepresentant());
//        tier.setCompteCollectif(fournisseur.getCompteCollectif());
//        tier.setEmail(fournisseur.getEmail());
//        tier.setId(fournisseur.getId());
//        tier.setLogo(fournisseur.getLogo());
//        tier.setNom(fournisseur.getNom());
//        tier.setPays(fournisseur.getPays());
//        tier.setPlanRistourne(fournisseur.getPlanRistourne());
//        tier.setPlanComission(fournisseur.getPlanComission());
//        tier.setLocalisation(fournisseur.getLocalisation());
//        tier.setStatut(fournisseur.getStatut());
//        tier.setTelephone(fournisseur.getTelephone());
//        tier.setUtilisateurRatache(fournisseur.getUtilisateurRatache());
//        tier.setVille(fournisseur.getVille());
//        tier.setModelDeRglt(fournisseur.getModelDeRglt());
//        tier.setModelDeRglt(fournisseur.getModelDeRglt());
//        if (fournisseur.getLastDateUpdate() != null) {
//            tier.setLastDateUpdate(fournisseur.getLastDateUpdate());
//        } else {
//            tier.setLastDateUpdate(df.format(new Date()));
//        }
//        if (fournisseur.getAuteur() != null) {
//            tier.setAuteur(fournisseur.getAuteur());
//        } else {
//            tier.setAuteur(getUserOnLine());
//        }
//        if (fournisseur.getLastAuteur() != null) {
//            tier.setLastAuteur(fournisseur.getLastAuteur());
//        } else {
//            tier.setLastAuteur(getUserOnLine());
//        }
//        tier.setNature(Constantes.TIERS_CLIENT);
//        return tier;
//    }
//
//    public void populateView(Fournisseur bean) {
//        fournisseur.setActif(bean.isActif());
//        fournisseur.setAdresse(bean.getAdresse());
//        fournisseur.setCodePostal(bean.getCodePostal());
//        fournisseur.setCategorieComptable(bean.getCategorieComptable());
//        fournisseur.setCategorieTarifaire(bean.getCategorieTarifaire());
//        //réccupère la catégorie tarifaire        
//        if (bean.getCategorieTarifaire() != 0) {
//            dao.setEntityClass(YvsCatTarif.class);
//            cat = (YvsCatTarif) dao.getOne(bean.getCategorieTarifaire());
//            disableCatT = cat.isOnlyTiers();
//        } else {
//            cat = null;
//        }
//        fournisseur.setCivilite(bean.getCivilite());
//        fournisseur.setClient(bean.isClient());
//        fournisseur.setCodeBarre(bean.getCodeBarre());
//        fournisseur.setCodeClient(bean.getCodeClient());
//        fournisseur.setCompteCollectif(bean.getCompteCollectif());
//        fournisseur.setEmail(bean.getEmail());
//        fournisseur.setId(bean.getId());
//        fournisseur.setLogo(bean.getLogo());
//        fournisseur.setNom(bean.getNom());
//        fournisseur.setPays(bean.getPays());
//        fournisseur.setPlanRistourne(bean.getPlanRistourne());
//        fournisseur.setPlanComission(bean.getPlanComission());
//        fournisseur.setModelDeRglt(bean.getModelDeRglt());
//        fournisseur.setLocalisation(bean.getLocalisation());
//        fournisseur.setStatut(bean.getStatut());
//        fournisseur.setTelephone(bean.getTelephone());
//        fournisseur.setUtilisateurRatache(bean.getUtilisateurRatache());
//        fournisseur.setLastDateUpdate(bean.getLastDateUpdate());
//        fournisseur.setAuteur(bean.getAuteur());
//        fournisseur.setLastAuteur(bean.getLastAuteur());
//        fournisseur.setVille(bean.getVille());
//        fournisseur.setListContact(bean.getListContact());
//        fournisseur.setIdAgence(bean.getIdAgence());
//        fournisseur.setListTarif(bean.getListTarif());
//    }
//
//    @Override
//    public void loadOnView(SelectEvent ev) {
//        Fournisseur t = (Fournisseur) ev.getObject();
//        populateView(t);
//        setDisableSave(true);
//        setRenderOnglet(true);
//        defaultButton = "Fournisseur-bUpdate";
//        update("mainFseur");
//    }
//
//    @Override
//    public void loadAll() {
//        listfFournisseurs.clear();
//        String[] ch = {};
//        Object[] val = {};
//        List<YvsBaseTiers> l = dao.loadNameQueries("YvsTiers.findByFournisseur", ch, val);
//        for (YvsBaseTiers t : l) {
//            listfFournisseurs.add(buildBeanFromEntity(t));
//        }
//        createGraphe();
//    }
//
//    private Fournisseur buildBeanFromEntity(YvsBaseTiers t) {
//        if (t != null) {
//            Fournisseur r = new Fournisseur();
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
//            r.setCodeFournisseur(t.getCodeTiers());
//            r.setCodePostal(t.getCodePostal());
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
//            r.setSociete(t.getStSociete());
//            r.setStatut(t.getStatut());
//            r.setTelephone(t.getTel());
////            r.setVille(t.getVille());
//
//            //charge les contacts
//            for (YvsContactsTiers ct : t.getYvsContactsTiersList()) {
//                if (r.getListContact() == null) {
//                    List<Contacts> l = new ArrayList<>();
//                    r.setListContact(l);
//                }
//                r.getListContact().add(buildBeanContact(ct));
//            }
//            //charge les tarif
//
//            if (t.getCategorieTarifaire() != null) {
//                if (t.getCategorieTarifaire().isOnlyTiers()) {
//                    String[] ch = {"categorie"};
//                    Object[] val = {t.getCategorieTarifaire().getId()};
//                    List<YvsBasePlanTarifaireArticle> lpt = dao.loadNameQueries("YvsPlanTarif.findByCategorie", ch, val);
//                    t.getCategorieTarifaire().setYvsPlanTarifList(lpt);
//                    if (r.getListTarif() == null) {
//                        List<PlanTarifClient> l = new ArrayList<>();
//                        r.setListTarif(l);
//                    }
//                    r.getListTarif().clear();
//                    for (YvsBasePlanTarifaireArticle ct : t.getCategorieTarifaire().getYvsPlanTarifList()) {
//                        r.getListTarif().add(buildPlanTarif(ct));
//                    }
//                }
//            }
//            return r;
//        }
//        return null;
//    }
//
//    private YvsBaseTiers buildEntityFromBean(Fournisseur t) {
//        if (t != null) {
//            YvsBaseTiers r = new YvsBaseTiers(t.getId());
//            r.setActif(t.isActif());
//            r.setAdresse(t.getAdresse());
//            if (t.getCategorieComptable() != 0) {
////                r.setCategorieComptable(new YvsCatcompta(t.getCategorieComptable()));
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
//            r.setStSociete(t.isSociete());
//            r.setStatut(t.getStatut());
//            r.setTel(t.getTelephone());
////            r.setVille(t.getVille());
////            //réccupère l'agence
////            String[] ch = {"codeagence"};
////            Object[] val = {t.getAgence()};
////            dao.setEntityClass(YvsAgences.class);
////            ag = (YvsAgences) dao.getOne(ch, val);
////            fournisseur.setIdAgence(ag.getId());
////            r.setAgence(ag);
//            return r;
//        }
//        return null;
//    }
//
//    @Override
//    public void choixSociete(ValueChangeEvent ev) {
//        String statut = (String) ev.getNewValue();
//        if (statut.equals("Individu")) {
//            fournisseur.setSociete(false);
//        } else {
//            fournisseur.setSociete(true);
//        }
//    }
//
//    @Override
//    public void delete() {
//        YvsBaseTiers tier = buildEntityFromBean(selectFournisseur);
//        selectFournisseur.setActif(false);
//        tier.setActif(false);
//        selectFournisseur.setLastAuteur(getUserOnLine());
//        selectFournisseur.setLastDateUpdate(df.format(new Date()));
//        dao.update(tier);
//        listfFournisseurs.remove(selectFournisseur);
//        update("tableFseur");
//        succes();
//    }
//
//    @Override
//    public void disable() {
//        YvsBaseTiers tier = buildEntityFromBean(selectFournisseur);
//        selectFournisseur.setActif(false);
//        tier.setActif(false);
//        selectFournisseur.setLastAuteur(getUserOnLine());
//        selectFournisseur.setLastDateUpdate(df.format(new Date()));
//        dao.update(tier);
//        listfFournisseurs.set(listfFournisseurs.indexOf(selectFournisseur), selectFournisseur);
//        update("tableFseur");
//        succes();
//    }
//
//    private void createGraphe() {
//        if (!listfFournisseurs.isEmpty()) {
//            Collections.sort(listfFournisseurs, new Fournisseur());
////            String art = listfFournisseurs.get(0).getAgence().getCodeAgence();
//            int count = 0;
//            int nbre = 1;
//            for (Fournisseur tie : listfFournisseurs) {
////                if (tie.getAgence().equals(art)) {
////                    count++;
////                } else {
////                    pieModel.set(art, count);
////                    count = 1;
////                    art = tie.getAgence().getCodeAgence();
////                }
////                if (nbre == listfFournisseurs.size()) {
////                    pieModel.set(art, count);
////                }
//                nbre++;
//            }
//        }
//    }
//    private TraiteMdr traite;
//
//    @Override
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
//    @Override
//    public void addContact() {
//        recopieContact();
//        if (fournisseur.getId() != 0) {
//            if (controleContact(c)) {
//                if (fournisseur.getListContact() == null) {
//                    List<Contacts> l = new ArrayList<>();
//                    fournisseur.setListContact(l);
//                }
//                buildContact(c);
//                if (!fournisseur.getListContact().contains(c)) {
//                    //on créée un noveaau contact  
//                    yvsC.setId(null);
//                    YvsContactsTiers id = (YvsContactsTiers) dao.save1(yvsC);
//                    c.setId(id.getId());
//                    fournisseur.getListContact().add(c);
//                } else {
//                    //on modifie un contact
//                    dao.update(yvsC);
//                    fournisseur.getListContact().set(fournisseur.getListContact().indexOf(c), c);
//                }
//                update("mainFseur:tableContactFseur");
//            }
//        }
//    }
//
//    @Override
//    public void detacherContact() {
//        if (selectedContact != null) {
//            dao.delete(new YvsContactsTiers((int)selectedContact.getId()));
//            fournisseur.getListContact().remove(selectedContact);
//            update("mainFseur:tableContactFseur");
//        }
//    }
//
//    @Override
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
//            update("mainFseur:Fseur-contact");
//        }
//    }
//
//    private YvsContactsTiers buildContact(Contacts c) {
//        yvsC = new YvsContactsTiers((int)c.getId());
//        yvsC.setAdress(c.getAdresse());
//        yvsC.setCivilite(c.getCivilite());
//        yvsC.setEmail(c.getEmail());
//        yvsC.setTiers(new YvsBaseTiers(c.getIdTiers()));
//        yvsC.setNom(c.getNom());
//        yvsC.setTelephone(c.getTel());
//        return yvsC;
//    }
//
//    private Contacts buildBeanContact(YvsContactsTiers yvsc) {
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
//    private PlanTarifClient buildPlanTarif(YvsBasePlanTarifaireArticle ta) {
//        PlanTarifClient t = new PlanTarifClient();
//        TrancheVal tranch = null;
//        //si le plan tarifaire est en tranche, on charge les bornes
//        if (ta.getTranche() != null) {
//            tranch = new TrancheVal();
//            tranch.setId(ta.getTranche().getId());
//            tranch.setModelTranche(ta.getTranche().getModelTranche());
//            //récupération des bornes
////            String[] ch = {"tranche"};
////            Object[] val = {ta.getIdTranche()};
////            List<Object[]> r = dao.loadNameQueries("YvsBorneTranches.findByTranche", ch, val);
//////            List<YvsBorneTranches> l = buildBorne(r);
////            List<YvsBorneTranches> l = ta.getIdTranche().getYvsBorneTranchesList();
////            ta.getIdTranche().setYvsBorneTranchesList(l);
//            List<BorneTranche> lb = new ArrayList<>();
//            for (YvsBorneTranches b : ta.getTranche().getYvsBorneTranchesList()) {
//                lb.add(buildBorne(b));
//            }
//            tranch.setListBorne(lb);
//        }
//        t.setRefArticle(ta.getArticle().getRefArt());
//        t.setIdArticle(ta.getArticle().getId());
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
//        c.setIdTiers(fournisseur.getId());
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
//    @Override
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
//            update("Fseur-bo-tarif");
//            update("Fseur-bo-grid");
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
//            update("Fseur-bo-tarif");
//            update("mainFseur:Fseur-ch_catT");
//            update("mainFseur:Fseur-ch_catT1");
//            update("A-Fseur-tarif");
//        }
//    }
//    /*
//     * l'ajout d'un plan tarifaire à un Fournisseur consiste à créer une
//     * catégorie tarifaire implicete à laquelle on rattachera ce Fournisseur. on
//     * doit s'assurer que le Fournisseur est bien lié à une agence
//     */
//
//    private YvsCatTarif buildCatTarif() {
//        YvsCatTarif cat1 = new YvsCatTarif();
//        cat1.setActif(true);
//        cat1.setAuthor(getUserOnLine());
//        cat1.setDateSave(new Date());
//        cat1.setDesignation("C-" + fournisseur.getNom());
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
//        //modifie la catégorie tarifaire du Fournisseur
//        String rq = "UPDATE yvs_tiers SET categorie_tarifaire=? WHERE id=?";
//        Options[] op = {new Options(cat.getId(), 1), new Options(fournisseur.getId(), 2)};
//        dao.requeteLibre(rq, op);
//        fournisseur.setCategorieTarifaire(cat.getId());
//        disableCatT = true;
//        update("mainFseur");
//    }
//
//    @Override
//    public void addTarif() {
//        if (fournisseur.getIdAgence() != 0) {
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
//            getMessage("Le Fournisseur doit être rattaché à une agence", FacesMessage.SEVERITY_ERROR);
//        }
//    }
//    PlanTarifClient planTarif;
//    YvsBasePlanTarifaireArticle yvsPlanTarif;
//    PlanTarifClient toUpdate;
//
//    @Override
//    public void addTarif(YvsCatTarif catT) {
//        if (tarif.getRefArticle() != null) {
//            String[] ch = {"refArt"};
//            Object[] val = {tarif.getRefArticle()};
//            planTarif = new PlanTarifClient();
//            yvsPlanTarif = new YvsBasePlanTarifaireArticle();
//            dao.setEntityClass(YvsBaseArticles.class);
//            YvsBaseArticles art = (YvsBaseArticles) dao.getOne(ch, val);
//            //charge l'agence            
//            planTarif.setRefArticle(art.getRefArt());
//            planTarif.setIdArticle(art.getId());
//            planTarif.setRemise(tarif.getRemise());
//            planTarif.setPrix(tarif.getPrix());
//            if (fournisseur.getListTarif() == null) {
//                List<PlanTarifClient> l = new ArrayList<>();
//                fournisseur.setListTarif(l);
//            }
//            if (!fournisseur.getListTarif().contains(planTarif)) {
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
//                    yvsPlanTarif.setTranche(tranch);
//                } else if (tranche && listBorneTranche.isEmpty()) {
//                    getMessage("Vous avez indiqué que ce plan était repartie en tranche, cependant vous n'avez mis à jour aucune tranches", FacesMessage.SEVERITY_WARN);
//                    return;
//                } else {
//                    yvsPlanTarif.setTranche(null);
//                    planTarif.setIdTranche(null);
//                }
//                yvsPlanTarif.setPrix(planTarif.getPrix());
//                yvsPlanTarif.setRemise(planTarif.getRemise());
//                dao.save(yvsPlanTarif);
//                fournisseur.getListTarif().add(0, planTarif);
//                Fournisseur ct = new Fournisseur();
//                ct.setId(fournisseur.getId());
//                int idx = listfFournisseurs.indexOf(ct);
//                if (listfFournisseurs.get(idx).getListTarif() == null) {
//                    listfFournisseurs.get(idx).setListTarif(fournisseur.getListTarif());
//                }
////                selectTiers.getListTarif().add(planTarif);
////                listTiers.set(listTiers.indexOf(Fournisseur), Fournisseur);
//                listBorneTranche.clear();
//                borne = 1;
//                remise = 1;
//                tarif.setRemise(1);
//                tranche = false;
//                update("Fseur-ta-tarif");
//                update("Fseur-bo-tarif");
//                update("tableFseur");
//            } else {
//                //modification du tarif-groupe
//                //on vérifie si la ligne tarif-categorie était lié à une tranche.
//                int index = fournisseur.getListTarif().indexOf(new PlanTarifClient(tarif.getRefArticle()));
//                toUpdate = fournisseur.getListTarif().get(index);
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
//                        fournisseur.getListTarif().set(fournisseur.getListTarif().indexOf(toUpdate), toUpdate);
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
//                            YvsBasePlanTarifaireArticle pt = new YvsBasePlanTarifaireArticle();
//                            pt.setTranche(tranch);
//                            pt.setPrix(0);
//                            pt.setRemise(0);
//                            dao.update(pt);
//                            fournisseur.getListTarif().set(fournisseur.getListTarif().indexOf(toUpdate), toUpdate);
//                            toUpdate = null;
//                        } else {
//                            //s'il n'y a pas de tranche,                             
//                            toUpdate.setRemise(tarif.getRemise());
//                            toUpdate.setPrix(tarif.getPrix());
//                            YvsBasePlanTarifaireArticle pt = new YvsBasePlanTarifaireArticle();
//                            pt.setRemise(tarif.getRemise());
//                            pt.setPrix(tarif.getPrix());
//                            dao.update(pt);
//                            fournisseur.getListTarif().set(fournisseur.getListTarif().indexOf(toUpdate), toUpdate);
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
//        update("Fseur-bo-tarif");
//    }
//
//    @Override
//    public void deleteBorne() {
//        try {
//            listBorneTranche.remove(selectBorne);
//            update("Fseur-bo-tarif");
//        } catch (Exception ex) {
//            listBorneTranche.remove(selectBorne);
//            update("Fseur-bo-tarif");
//        }
//    }
//
//    public void deletTarifGroup() {
//        YvsBasePlanTarifaireArticle tar = new YvsBasePlanTarifaireArticle();
//        dao.delete(tar);
//        System.out.println(cat.getId() + " " + tarif.getIdArticle() + " " + fournisseur.getIdAgence());
//        //si le tarif est lié à une tranche, on la supprime aussi
//        if (selectTarifClt.getIdTranche() != null) {
//            YvsTranches t = new YvsTranches(selectTarifClt.getIdTranche().getId());
//            dao.delete(t);
//        }
////        PlanTarifClient t = new PlanTarifClient();
////        t.setRefCategorie(selectTarifGroup.getRefCategorie());
//        fournisseur.getListTarif().remove(selectTarifClt);
//        update("Fseur-groupP-table");
//        update("Fseur-ta-tarif");
//    }
//
////cette methode est invoqué lorsqu'on  change d'onglet 
//    public void changeDefaultButon(TabChangeEvent ev) {
//        String id = ev.getTab().getId();
//        switch (id) {
//            case "Fseur-tab-gen":
//                if (isDisableSave()) {
//                    setDefaultButton("Fournisseur-bUpdate");
//                } else {
//                    setDefaultButton("Fournisseur-bSave");
//                }
//                break;
//            case "Fseur-tab-adresse":
//                if (isDisableSave()) {
//                    setDefaultButton("Fournisseur-bUpdate");
//                } else {
//                    setDefaultButton("Fournisseur-bSave");
//                }
//                break;
//            case "Fseur-tab-complt":
//                if (isDisableSave()) {
//                    setDefaultButton("Fournisseur-bUpdate");
//                } else {
//                    setDefaultButton("Fournisseur-bSave");
//                }
//                break;
//            case "Fseur-tab-tarif":
//                setDefaultButton("maiTiers:tier-buton-addTarif");
//                break;
//            case "Fseur-tab-contact":
//                setDefaultButton("maiTiers:b-add-contact");
//                break;
//            default:
//                setDefaultButton("Fournisseur-bSave");
//                break;
//        }
//        update("Fseur-default");
//    }
//}

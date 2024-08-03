/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.stocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.dao.Util;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepotOperation;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseMouvementStock;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidDocStock;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;
import yvs.service.AbstractEntity;
import yvs.service.IEntitySax;
import yvs.service.param.workflow.IYvsWorkflowValidDocStock;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComDocStocks extends AbstractEntity {

    public AYvsComDocStocks() {
    }

    public AYvsComDocStocks(DaoInterfaceLocal dao) {
        this.dao = dao;
    }
    IEntitySax IEntitiSax = new IEntitySax();

    public ResultatAction<YvsComDocStocks> controleUpdate(YvsComDocStocks entity) {
        try {
            if (entity.getId() < 1) {
                return new ResultatAction<>(false, entity, 0L, "Le document n'existe pas");
            }
            return controle(entity);
        } catch (Exception e) {
            Logger.getLogger(AYvsComDocStocks.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComDocStocks> controle(YvsComDocStocks entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            YvsComDocStocks current = null;
            if (Util.asLong(entity.getId())) {
                current = (YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findById", new String[]{"id"}, new Object[]{entity.getId()});
            }
            if (entity.getId() < 1) {
                if (!entity.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                    return new ResultatAction<>(false, entity, 0L, "Le document doit etre éditable pour pouvoir etre modifié");
                }
            } else {
                if (entity.getStatut().equals(Constantes.ETAT_VALIDE) && (current != null ? current.getStatut().equals(Constantes.ETAT_VALIDE) : false)) {
                    return new ResultatAction<YvsComDocStocks>(false, entity, entity.getId(), "Vous ne pouvez pas modifer cette facture ! Elle est déja validée", false);
                }
                if (entity.getStatut().equals(Constantes.ETAT_ANNULE) && (current != null ? current.getStatut().equals(Constantes.ETAT_ANNULE) : false)) {
                    return new ResultatAction<YvsComDocStocks>(false, entity, entity.getId(), "Vous ne pouvez pas modifer cette facture ! Elle a été annulée", false);
                }
            }
            if (entity.getTypeDoc() != null ? entity.getTypeDoc().isEmpty() : true) {
                return new ResultatAction<>(false, entity, 0L, "Le document n'a pas de type");
            }
            if (entity.getTypeDoc().equals(Constantes.TYPE_ES) || entity.getTypeDoc().equals(Constantes.TYPE_FT)) {
                if ((entity.getDestination() != null) ? entity.getDestination().getId() < 1 : true) {
                    return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le dépôt destination");
                }
                if ((entity.getCreneauDestinataire() != null) ? entity.getCreneauDestinataire().getId() < 1 : true) {
                    return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le créneau destination");
                }
            } else if (entity.getTypeDoc().equals(Constantes.TYPE_SS) || entity.getTypeDoc().equals(Constantes.TYPE_FT)) {
                if ((entity.getSource() != null) ? entity.getSource().getId() < 1 : true) {
                    return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le dépôt source");
                }
                if ((entity.getCreneauSource() != null) ? entity.getCreneauSource().getId() < 1 : true) {
                    return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le créneau source");
                }
            } else {
                if ((entity.getSource() != null) ? entity.getSource().getId() < 1 : true) {
                    return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le dépôt");
                }
                if ((entity.getCreneauSource() != null) ? entity.getCreneauSource().getId() < 1 : true) {
                    return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le créneau");
                }
            }
            if (entity.getCloturer() && (current != null ? current.getCloturer() : false)) {
                return new ResultatAction<>(false, entity, 0L, "Ce document est vérouillé");
            }
            if (entity.getDescription() != null ? entity.getDescription().isEmpty() : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le motif de cette opération !");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocStocks.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComDocStocks> save(YvsComDocStocks entity) {
        ResultatAction result = controle(entity);
        System.err.println("resul control = " + result.getMessage());
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_doc_stocks", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    List<YvsWorkflowEtapeValidation> etapes = new ArrayList<>();
                    YvsSocietes societe = new YvsSocietes();
                    if (entity.getSociete() != null ? entity.getSociete().getId() > 0 : false) {
                        societe = entity.getSociete();
                    } else {
                        societe = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{entity.getSociete().getId()});
                    }
                    etapes = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", new String[]{"titre", "societe"}, new Object[]{Constantes.DOCUMENT_SORTIE, societe});
                    entity.setEtapeTotal(etapes.size());

                    entity.setId(null);
                    List<YvsWorkflowValidDocStock> etape_facture = new ArrayList<>(entity.getEtapesValidations());
                    entity.getEtapesValidations().clear();

                    entity = (YvsComDocStocks) dao.save1(entity);
                    if (etape_facture != null ? !etape_facture.isEmpty() : false) {
                        IYvsWorkflowValidDocStock impl = (IYvsWorkflowValidDocStock) IEntitiSax.createInstance("IYvsWorkflowValidDocStock", dao);
                        for (YvsWorkflowValidDocStock etape : etape_facture) {
                            etape.setDocStock(new YvsComDocStocks(entity.getId()));
                            impl.save(etape);

                        }
                    } else {
                        System.err.println("etape serveur = " + etapes);
                        if (etapes != null ? !etapes.isEmpty() : false) {
                            entity.setEtapesValidations(saveEtapesValidation(entity, etapes, entity.getAuthor()));
                            for (YvsWorkflowValidDocStock etap : entity.getEtapesValidations()) {
                                etap.setEtape(new YvsWorkflowEtapeValidation(etap.getEtape().getId()));
                            }
                        }
                        System.err.println("etapes serveur  =" + entity.getEtapesValidations());
                    }
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, result.getMessage());
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocStocks.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsComDocStocks> saveInventaire(YvsComDocStocks entity) {
        ResultatAction result = controle(entity);

        YvsComDocStocks entree = new YvsComDocStocks();
        YvsComDocStocks sortie = new YvsComDocStocks();
        try {
            Long source = entity.getId();
            if (result.isResult()) {
                List<YvsWorkflowEtapeValidation> etapes = new ArrayList<>();
                List<YvsComContenuDocStock> contenus = new ArrayList<>(entity.getContenus());
                entity.getContenus().clear();
                YvsSocietes societe = new YvsSocietes();
                if (entity.getSociete() != null ? entity.getSociete().getId() > 0 : false) {
                    societe = entity.getSociete();
                } else {
                    societe = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{entity.getSociete().getId()});
                }
                YvsBaseDepots depot = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{entity.getSource().getId()});
                if (depot != null ? depot.getId() > 0 : false) {
                    currentAgence = depot.getAgence();

                    System.err.println("agence = " + currentAgence.getDesignation());
                }
                currentAgence.setSociete(societe);
                etapes = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", new String[]{"titre", "societe"}, new Object[]{Constantes.DOCUMENT_SORTIE, societe});
                entity.setEtapeTotal(etapes.size());

                entity.setId(null);
                List<YvsWorkflowValidDocStock> etape_facture = new ArrayList<>(entity.getEtapesValidations());
                entity.getEtapesValidations().clear();
                String num = dao.genererReference(Constantes.TYPE_IN_NAME, entity.getDateDoc(), entity.getSource().getId(), societe, currentAgence);
                entity.setNumDoc(num);
                entity = (YvsComDocStocks) dao.save1(entity);

                // traitement du contenu
                for (YvsComContenuDocStock content : contenus) {
                    Double stock = 0.0;
                    stock = dao.stocks(content.getArticle().getId(), 0L, entity.getSource().getId(), 0L, entity.getSociete().getId(), entity.getDateDoc(), content.getConditionnement().getId(), 0L);
                    stock = stock != null ? stock : 0;
                    double qte = content.getQuantite();
                    if (stock == 0 ? content.getQuantite() == 0 : false) {
                        continue;
                    } else if (stock > content.getQuantite()) {
                        if (sortie != null ? sortie.getId() < 1 : true) {
                            sortie = new YvsComDocStocks();
                            sortie.setDateDoc(entity.getDateDoc());
                            num = dao.genererReference(Constantes.TYPE_SS_NAME, entity.getDateDoc(), entity.getSource().getId(), societe, currentAgence);
                            System.err.println("numero sortie= " + num);
                            sortie.setDateSave(new Date());
                            sortie.setNumDoc(num);
                            sortie.setId(null);
                            sortie.setAuthor(entity.getAuthor());
                            sortie.setSociete(entity.getSociete());
                            sortie.setDestination(entity.getDestination());
                            sortie.setSource(entity.getSource());
                            sortie.setDocumentLie(new YvsComDocStocks(entity.getId()));
                            sortie.setNature("AJUSTEMENT STOCK");
                            sortie.setTypeDoc(Constantes.TYPE_SS);
                            sortie.setDescription("SORTIE DE STOCK");
                            sortie.setDateUpdate(new Date());
                            dao.save(sortie);

                        }
                        content.setQuantite(stock - content.getQuantite());
                        content.setDocStock(new YvsComDocStocks(sortie.getId()));
                        Double prix = 0.0;
                        prix = dao.getPr(content.getArticle().getId(), entity.getSource().getId(), 0L, entity.getDateDoc(), content.getConditionnement().getId());
                        if (prix == null || prix == 0) {
                            prix = content.getConditionnement().getPrix();
                        }
                        content.setPrix(prix);
                        content.setPrixEntree(prix);
                    } else if (content.getQuantite() > stock) {
                        if (entree != null ? entree.getId() < 1 : true) {
                            entree = new YvsComDocStocks();
                            entree.setDateDoc(entity.getDateDoc());
                            num = dao.genererReference(Constantes.TYPE_ES_NAME, new Date(), entity.getSource().getId(), societe, currentAgence);
                            System.err.println("numero entree= " + num);
                            entree.setAuthor(entity.getAuthor());
                            entree.setSociete(entity.getSociete());
                            entree.setDestination(entity.getDestination());
                            entree.setSource(entity.getSource());
                            entree.setDocumentLie(new YvsComDocStocks(entity.getId()));
                            entree.setNature("AJUSTEMENT STOCK");
                            entree.setTypeDoc(Constantes.TYPE_ES);
                            entree.setDescription("ENTREE EN STOCK");
                            entree.setDateUpdate(new Date());
                            entree.setNumDoc(num);
                            entree.setId(null);
                            entree.setDateSave(new Date());
                            dao.save(entree);

                        }
                        content.setQuantite(content.getQuantite() - stock);
                        content.setDocStock(new YvsComDocStocks(entree.getId()));
                        YvsBaseMouvementStock baseMouvementStock = (YvsBaseMouvementStock) dao.loadOneByNameQueries("YvsBaseMouvementStock.findByDepotMouvement", new String[]{"depot", "mouvement"}, new Object[]{entity.getSource(), "E"});
                        Double prix = 0.0;
                        if (baseMouvementStock != null ? baseMouvementStock.getId() > 0 : false) {
                            if (baseMouvementStock.getCoutEntree() != null) {
                                prix = baseMouvementStock.getCoutEntree();
                            }
                        } else {
                            prix = content.getConditionnement().getPrixAchat();
                        }
                        content.setPrix(prix);
                        content.setPrixEntree(prix);
                    }

                    content.setDateUpdate(new Date());
                    content.setConditionnementEntree(content.getConditionnement());
                    content.setActif(true);
                    if (content != null ? content.getId() < 1 : true) {
                        content.setDateSave(new Date());
                        content.setId(null);
                        dao.save(content);
                    }

                    if (stock > qte) {
                        sortie.getContenus().add(content);
                        if (!entity.getDocuments().contains(societe)) {
                            entity.getDocuments().add(sortie);
                        }
                        System.err.println("sortie :" + sortie.getContenus());

                    } else if (stock < qte) {
                        entree.getContenus().add(content);
                        if (!entity.getDocuments().contains(entree)) {
                            entity.getDocuments().add(entree);
                        }

                    }

                }

                if (entity.getDocuments() != null) {
                    for (YvsComDocStocks d : entity.getDocuments()) {
                        for (YvsComContenuDocStock c : d.getContenus()) {
                            System.err.println("contenus = " + c.getArticle().getDesignation());
                        }
                    }
                }

                if (etape_facture != null ? !etape_facture.isEmpty() : false) {
                    IYvsWorkflowValidDocStock impl = (IYvsWorkflowValidDocStock) IEntitiSax.createInstance("IYvsWorkflowValidDocStock", dao);
                    for (YvsWorkflowValidDocStock etape : etape_facture) {
                        etape.setDocStock(new YvsComDocStocks(entity.getId()));
                        impl.save(etape);

                    }
                } else {
                    System.err.println("etape serveur = " + etapes);
                    if (etapes != null ? !etapes.isEmpty() : false) {
                        entity.setEtapesValidations(saveEtapesValidation(entity, etapes, entity.getAuthor()));
                        for (YvsWorkflowValidDocStock etap : entity.getEtapesValidations()) {
                            etap.setEtape(new YvsWorkflowEtapeValidation(etap.getEtape().getId()));
                        }
                    }
                    System.err.println("etapes serveur  =" + entity.getEtapesValidations());
                }

                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), source, "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, result.getMessage());
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocStocks.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsWorkflowValidDocStock> valideEtape(YvsWorkflowValidDocStock entity) {
        try {
            System.err.println("--- VALIDE ETAPE ---");

            if (entity != null ? entity.getId() > 0 : false) {
                System.err.println("etape = " + entity.getEtape());
                System.err.println("facture = " + entity.getDocStock());
                Long id = (Long) dao.loadOneByNameQueries("YvsWorkflowValidDocStock.findIdByEtapeFacture", new String[]{"facture", "etape"}, new Object[]{entity.getDocStock(), entity.getEtape()});
                entity.setId(id);

                entity.getDocStock().getEtapesValidations().clear();
                ResultatAction<YvsWorkflowValidDocStock> result = validEtapeFacture(entity, entity.getDocStock(), !entity.getEtape().getFirstEtape(), entity.getAuthor());
                System.err.println("result = " + result.getMessage());
                if (result.isResult()) {
                    entity.getDocStock().getDocuments().clear();

                    entity.getDocStock().getContenus().clear();
                    return new ResultatAction(true, null, null, "Succes");
                } else {
                    return new ResultatAction<>(false, null, 0L, result.getMessage());
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AYvsComDocStocks.class.getName()).log(Level.SEVERE, null, e);

        }
        return new ResultatAction<>(false, null, 0L, "Action Impossible");
    }

    public YvsComDocStocks rebuildDocStock(YvsComDocStocks d) {
        YvsComDocStocks re = new YvsComDocStocks(d.getId());
        re.setEtapesValidations(d.getEtapesValidations());
        return re;
    }

    public ResultatAction<YvsComDocStocks> update(YvsComDocStocks entity) {
        ResultatAction result = controleUpdate(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComDocStocks) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocStocks.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComDocStocks> delete(YvsComDocStocks entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocStocks.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private List<YvsWorkflowValidDocStock> saveEtapesValidation(YvsComDocStocks m, List<YvsWorkflowEtapeValidation> model, YvsUsersAgence currentUser) {
        //charge les étape de vailidation
        List<YvsWorkflowValidDocStock> re = new ArrayList<>();
        if (!model.isEmpty()) {
            YvsWorkflowValidDocStock vm;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    String[] champ = new String[]{"facture", "etape"};
                    Object[] val = new Object[]{m, et};
                    YvsWorkflowValidDocStock w = (YvsWorkflowValidDocStock) dao.loadOneByNameQueries("YvsWorkflowValidDocStock.findByEtapeFacture", champ, val);
                    if (w != null ? w.getId() < 1 : true) {
                        vm = new YvsWorkflowValidDocStock();
                        vm.setAuthor(new YvsUsersAgence(currentUser.getId()));
                        vm.setEtape(et);
                        vm.setEtapeValid(false);
                        vm.setDocStock(new YvsComDocStocks(m.getId()));
                        IYvsWorkflowValidDocStock impl = (IYvsWorkflowValidDocStock) IEntitiSax.createInstance("IYvsWorkflowValidDocStock", dao);
                        ResultatAction<YvsWorkflowValidDocStock> wo = impl.save(vm);
                        vm = (YvsWorkflowValidDocStock) wo.getData();
                        re.add(vm);
                    }
                }
            }
        }
        return ordonneEtapes(re);
    }

    private List<YvsWorkflowValidDocStock> ordonneEtapes(List<YvsWorkflowValidDocStock> l) {
        List<YvsWorkflowValidDocStock> re = new ArrayList<>();
        YvsWorkflowValidDocStock first = null;
        //recherche la première étape       
        for (YvsWorkflowValidDocStock vm : l) {
            if (vm.getEtape().getFirstEtape()) {
                first = vm;
                break;
            }
        }
        if (first != null) {
            re.add(first);  //ajoute la première étapes au résultat
            boolean find;
            //tant qu'il existe une étape suivante active
            while ((first.getEtape().getEtapeSuivante() != null) ? (first.getEtape().getEtapeSuivante().getActif()) : false) {
                find = false;
                for (YvsWorkflowValidDocStock vm : l) {
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
        for (YvsWorkflowValidDocStock vm : re) {
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

    public ResultatAction<YvsWorkflowValidDocStock> validEtapeFacture(YvsWorkflowValidDocStock etape, YvsComDocStocks docStock, boolean lastEtape, YvsUsersAgence currentUser) {
        //vérifier que la personne qui valide l'étape a le droit 
        List<YvsWorkflowValidDocStock> list = new ArrayList<>();
        docStock.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{docStock}));
        if (!docStock.getContenus().isEmpty()) {
            //contrôle la cohérence des dates
            YvsComDocStocks current = (YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findById", new String[]{"id"}, new Object[]{docStock.getId()});
            etape = (YvsWorkflowValidDocStock) dao.loadOneByNameQueries("YvsWorkflowValidDocStock.findById", new String[]{"id"}, new Object[]{etape.getId()});
            etape.setDocStock(current);
            list = dao.loadNameQueries("YvsWorkflowValidDocStock.findByFacture", new String[]{"facture"}, new Object[]{current});
            list = ordonneEtapes(list);
            int idx = list.indexOf(etape);
            if (idx >= 0) {
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() == null) {
                    ResultatAction<YvsComDocStocks> valide = valider(current, currentUser);
                    if (valide.isResult()) {
                        etape.setAuthor(currentUser);
                        etape.setEtapeValid(true);
                        etape.setEtapeActive(false);
                        etape.setMotif(null);
                        etape.setDateUpdate(new Date());
                        if (list.size() > (idx + 1)) {
                            list.get(idx + 1).setEtapeActive(true);
                        }
                        dao.update(etape);
                        current.setEtapeValide(current.getEtapeValide() + 1);
                        current.setStatut(Constantes.ETAT_VALIDE);
                        current.setEtapeValide(current.getEtapeValide());
                        dao.update(current);
                    } else {
                        return new ResultatAction<>(false, etape, 0L, valide.getMessage());
                    }
                } else {
                    etape.setAuthor(currentUser);
                    etape.setEtapeValid(true);
                    etape.setEtapeActive(false);
                    etape.setMotif(null);
                    etape.setDateUpdate(new Date());
                    if (list.size() > (idx + 1)) {
                        list.get(idx + 1).setEtapeActive(true);
                    }
                    dao.update(etape);
                    current.setStatut(Constantes.ETAT_ENCOURS);
                    current.setAuthor(currentUser);
                    current.setEtapeValide(current.getEtapeValide() + 1);
                    dao.update(current);
                    current.setStatut(Constantes.ETAT_ENCOURS);
                    current.setEtapeValide(current.getEtapeValide());

                    String result = null;
                    YvsComContenuDocStock cc = null;
                    for (YvsComContenuDocStock c : current.getContenus()) {
                        cc = c;
                        result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), current.getSource().getId(), 0L, c.getQuantite(), 0, "INSERT", "S", current.getDateDoc(), (c.getLotSortie() != null ? c.getLotSortie().getId() : 0));
                        if (result != null) {
                            break;
                        }
                    }
                    etape.getDocStock().getContenus().clear();
                    if (result != null) {
                        return new ResultatAction<>(true, etape, 0L, "la ligne d'article " + cc.getArticle().getDesignation() + " pourrait engendrer une incohérence dans le stock à la date du " + result);
                    }
                }
                return new ResultatAction<>(true, etape, etape.getId(), "Succès");
            } else {
                return new ResultatAction<>(false, etape, 0L, "Impossible de continuer !");
            }
        } else {
            return new ResultatAction<>(false, etape, 0L, "Le document de sortie de stocks n'a pas de contenu!,  veuillez enregistrer des lignes de contenue");
        }
    }

    public ResultatAction<YvsComDocStocks> valider(YvsComDocStocks docStock, YvsUsersAgence currentUser) {
        if (docStock == null) {
            return new ResultatAction<>(false, docStock, 0L, "L'élément ne peut pas etre null");
        } else {
            if (docStock.getDocumentLie() != null ? docStock.getDocumentLie().getTypeDoc().equals(Constantes.TYPE_IN) : false) {
                return new ResultatAction<>(false, docStock, 0L, "La validation de ce document est lié  à celle l'inventaire " + docStock.getDocumentLie().getNumDoc() + " l'ayant généré");
            }
        }
        if (docStock.getContenus().isEmpty()) {
            return new ResultatAction<>(false, docStock, 0L, "Le document de sortie de stocks n'a pas de contenu! , veuillez enregistrer des lignes de contenue");
        }
        if (docStock.getTypeDoc().equals(Constantes.TYPE_SS)) {
            if (!verifyOperation(docStock.getSource(), Constantes.SORTIE, docStock.getNature(), true)) {
                return new ResultatAction<>(false, docStock, 0L, "Ce depot n'autorise pas cette action !");
            }
            String result;
            List<YvsBaseConditionnement> controls = new ArrayList<>();
            for (YvsComContenuDocStock c : docStock.getContenus()) {
                double quantite = c.getQuantite();
                int control = controls.indexOf(c.getConditionnement());
                if (control > -1) {
                    quantite += controls.get(control).getStock();
                }
                result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), docStock.getSource().getId(), 0L, quantite, 0, "INSERT", "S", docStock.getDateDoc(), (c.getLotSortie() != null ? c.getLotSortie().getId() : 0));
                if (result != null) {
                    return new ResultatAction<>(false, docStock, 0L, "Impossible de valider ce document, la ligne d'article " + c.getArticle().getDesignation() + " engendrera une incohérence dans le stock à la date du " + result);
                }
                c.getConditionnement().setStock(quantite);
                if (control > -1) {
                    controls.set(control, c.getConditionnement());
                } else {
                    controls.add(c.getConditionnement());
                }
            }
        } else {
            if (!verifyOperation(docStock.getDestination(), Constantes.ENTREE, docStock.getNature(), true)) {
                return new ResultatAction<>(false, docStock, 0L, "Ce depot n'autorise pas cette action !");
            }
            for (YvsComContenuDocStock c : docStock.getContenus()) {
                if (!checkOperationArticle(c.getArticle().getId(), docStock.getDestination().getId(), Constantes.ENTREE)) {
                    return new ResultatAction<>(false, docStock, 0L, "L'article '" + c.getArticle().getDesignation() + "' ne fait pas d'entrée en stock dans le depot '" + docStock.getDestination().getDesignation() + "'");
                }
            }
        }
        if (changeStatut(Constantes.ETAT_VALIDE, docStock, currentUser)) {
            docStock.setCloturer(false);
            docStock.setAnnulerBy(null);
            docStock.setValiderBy(currentUser.getUsers());
            docStock.setCloturerBy(null);
            docStock.setDateAnnuler(null);
            docStock.setDateCloturer(null);
            docStock.setDateValider(new Date());
            docStock.setStatut(Constantes.ETAT_VALIDE);
            docStock.setAuthor(currentUser);
            dao.update(docStock);
            for (YvsComContenuDocStock c : docStock.getContenus()) {
                c.setStatut(Constantes.ETAT_VALIDE);
                dao.update(c);
            }

            return new ResultatAction<>(true, docStock, docStock.getId(), "Succès");
        }
        return new ResultatAction<>(false, docStock, 0L, "Action impossible !");
    }

    public boolean changeStatut(String etat, YvsComDocStocks docStock, YvsUsersAgence currentUser) {
        if (changeStatut_(etat, docStock, currentUser)) {
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat, YvsComDocStocks docStock, YvsUsersAgence currentUser) {
        if (!etat.equals("") && docStock != null) {
            if (docStock.getCloturer()) {
//                getErrorMessage("Ce document est verrouillé");
                return false;
            }
            String rq = "UPDATE yvs_com_doc_stocks SET statut = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(docStock.getId(), 1)};
            dao.requeteLibre(rq, param);
            docStock.setStatut(etat);
            docStock.setStatut(etat);

//            if (!etat.equals(Constantes.ETAT_EDITABLE)) {
//                if (historiques.contains(selectDoc)) {
//                    historiques.remove(historiques.indexOf(selectDoc));
//                    update(entree ? "data_entree_stock_hist" : "data_sortie_stock_hist");
//                }
//            } else {
//                if (!historiques.contains(selectDoc)) {
//                    historiques.add(0, selectDoc);
//                    update(entree ? "data_entree_stock_hist" : "data_sortie_stock_hist");
//                }
//            }
            if (etat.equals(Constantes.ETAT_VALIDE)) {
                for (YvsComContenuDocStock c : docStock.getContenus()) {
                    c.setStatut(Constantes.ETAT_VALIDE);
                    c.setAuthor(currentUser);
                    dao.update(c);
                }
            } else {
                for (YvsComContenuDocStock c : docStock.getContenus()) {
                    c.setStatut(Constantes.ETAT_EDITABLE);
                    c.setAuthor(currentUser);
                    dao.update(c);
                }
            }

            return true;
        } else {
//            getErrorMessage("Vous devez selectionner un document");
        }
        return false;
    }

    public boolean verifyOperation(YvsBaseDepots d, String type, String operation, boolean error) {
        if (d != null ? d.getId() > 0 : false) {
            if (!checkOperationDepot(d.getId(), type)) {
                if (error) {
//                    getErrorMessage("Le depot '" + d.getDesignation() + "' n'est pas paramètré pour les opérations '" + type + "'");
                } else {
//                    getWarningMessage("Le depot '" + d.getDesignation() + "' n'est pas paramètré pour les opérations '" + type + "'");
                }
                return false;
            }
            if (!checkOperationDepot(d.getId(), type, operation)) {
                if (error) {
//                    getErrorMessage("Le depot '" + d.getDesignation() + "' n'est pas paramètré pour les opérations '" + operation + "' en '" + type + "'");
                } else {
//                    getWarningMessage("Le depot '" + d.getDesignation() + "' n'est pas paramètré pour les opérations '" + operation + "' en '" + type + "'");
                }
                return false;
            }
        }
        return true;
    }

    public boolean checkOperationDepot(long depot, String type) {
        return checkOperationDepot(depot, type, dao);
    }

    public boolean checkOperationDepot(long depot, String type, DaoInterfaceLocal dao) {
        String[] champ = new String[]{"depot", "type"};
        Object[] val = new Object[]{new YvsBaseDepots(depot), type};
        String nameQueri = "YvsBaseDepotOperation.findByDepotType";
        List<YvsBaseDepotOperation> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        return l != null ? !l.isEmpty() : false;
    }

    public boolean checkOperationDepot(long depot, String type, String operation) {
        if (!checkOperationDepot(depot, type)) {
            return false;
        }
        String[] champ = new String[]{"depot", "type", "operation"};
        Object[] val = new Object[]{new YvsBaseDepots(depot), type, operation};
        String nameQueri = "YvsBaseDepotOperation.findByDepotTypeOperation";
        List<YvsBaseDepotOperation> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        return l != null ? !l.isEmpty() : false;
    }

    public boolean checkOperationArticle(long article, long depot, String operation) {
        if (depot > 0) {
            String[] champ = new String[]{"depot", "article"};
            Object[] val = new Object[]{new YvsBaseDepots(depot), new YvsBaseArticles(article)};
            String nameQueri = "YvsBaseArticleDepot.findByArticleDepot";
            List<YvsBaseArticleDepot> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
            if (l != null ? !l.isEmpty() : false) {
                YvsBaseArticleDepot a = l.get(0);
                if (a.getModeAppro() != null) {
                    switch (operation) {
                        case Constantes.ACHAT: {
                            switch (a.getModeAppro()) {
                                case Constantes.APPRO_ACHTON:
                                case Constantes.APPRO_ACHT_EN:
                                case Constantes.APPRO_ACHT_PROD:
                                case Constantes.APPRO_ACHT_PROD_EN:
                                    return true;
                                default:
                                    return false;
                            }
                        }
                        case Constantes.ENTREE: {
                            switch (a.getModeAppro()) {
                                case Constantes.APPRO_ENON:
                                case Constantes.APPRO_ACHT_EN:
                                case Constantes.APPRO_PROD_EN:
                                case Constantes.APPRO_ACHT_PROD_EN:
                                    return true;
                                default:
                                    return false;
                            }
                        }
                        case Constantes.PRODUCTION: {
                            switch (a.getModeAppro()) {
                                case Constantes.APPRO_PRODON:
                                case Constantes.APPRO_ACHT_PROD:
                                case Constantes.APPRO_PROD_EN:
                                case Constantes.APPRO_ACHT_PROD_EN:
                                    return true;
                                default:
                                    return false;
                            }
                        }
                        default:
                            return false;
                    }
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    public String controleStock(long article, long conditionnement, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date, long lot) {
        return dao.controleStock(article, conditionnement, depot, tranche, newQte, oldQte, action, mouvement, date, lot);
    }

    public String controleStock(long article, long conditionnement, long oldCond, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date, long lot) {
        return dao.controleStock(article, conditionnement, oldCond, depot, tranche, newQte, oldQte, action, mouvement, date, lot);
    }
}

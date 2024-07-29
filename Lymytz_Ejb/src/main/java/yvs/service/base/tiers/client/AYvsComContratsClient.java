/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers.client;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.client.YvsComContratsClient;
import yvs.entity.commercial.client.YvsComElementAddContratsClient;
import yvs.entity.commercial.client.YvsComElementContratsClient;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComCoutSupDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public abstract class AYvsComContratsClient extends AbstractEntity {

    public String[] champ;
    public Object[] val;
    public String nameQueri;

    public AYvsComContratsClient() {
    }

    public AYvsComContratsClient(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComContratsClient> controle(YvsComContratsClient entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if ((entity.getClient() != null) ? entity.getClient().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier le client");
            }
            //modifie le numéro de document si la date change   
            if ((entity.getReference() == null || entity.getReference().trim().length() < 1)) {
                String reference = dao.genererReference(Constantes.TYPE_CONTRAT_CLIENT_NAME, entity.getDateDebutFacturation(), 0, (currentAgence != null ? currentAgence.getSociete() : null), currentAgence);
                if (reference == null || reference.trim().equals("")) {
//                    return new ResultatAction<>(false, entity, 0L, dao.getRESULT());
                }
                entity.setReference(reference);
            }
            if (entity.getReference() == null || entity.getReference().trim().equals("")) {
//                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer un reference");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComContratsClient.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComContratsClient> save(YvsComContratsClient entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_contrats_client", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComContratsClient) dao.loadOneByNameQueries("YvsComContratsClient.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComContratsClient) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComContratsClient.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComContratsClient> update(YvsComContratsClient entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComContratsClient) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComContratsClient.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComContratsClient> delete(YvsComContratsClient entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComContratsClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ResultatAction<YvsComDocVentes> genereFacture(YvsComContratsClient entity, Date date, YvsUsers vendeur, YvsNiveauAcces niveau, YvsAgences agence, YvsUsersAgence author) {
        try {
            YvsBasePointVente point = null;
            YvsGrhTrancheHoraire tranche = null;
            List<YvsComCreneauPoint> creneaux = dao.loadNameQueries("YvsComCreneauHoraireUsers.findCreneauPointByUsers", new String[]{"users", "date"}, new Object[]{vendeur, date});
            if (creneaux != null ? !creneaux.isEmpty() : false) {
                point = creneaux.get(0).getPoint();
                tranche = creneaux.get(0).getTranche();
            }
            return genereFacture(entity, date, vendeur, point, tranche, niveau, agence, author);
        } catch (Exception ex) {
            Logger.getLogger(AYvsComContratsClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, null, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComDocVentes> genereFacture(YvsComContratsClient entity, Date date, YvsUsers vendeur, YvsBasePointVente point, YvsGrhTrancheHoraire tranche, YvsNiveauAcces niveau, YvsAgences agence, YvsUsersAgence author) {
        ResultatAction result = new ResultatAction<>(false, null, 0L, "Action Impossible");
        try {
            date = date != null ? date : new Date();
            if (entity != null ? entity.getId() < 1 : true) {
                return result;
            }
            if (!entity.getActif()) {
                return new ResultatAction<>(false, null, 0L, "Ce contrat n'est pas actif");
            }
            if (entity.getElements() != null ? entity.getElements().isEmpty() : true) {
                return new ResultatAction<>(false, null, 0L, "Ce contrat n'a pas d'element");
            }
            if (vendeur != null ? vendeur.getId() < 1 : true) {
                return new ResultatAction<>(false, null, 0L, "Le vendeur pour cette facture n'a pas ete trouvé");
            }
            if (point != null ? point.getId() < 1 : true) {
                return new ResultatAction<>(false, null, 0L, "Le point de vente pour cette facture n'a pas ete trouvé");
            }
            if (tranche != null ? tranche.getId() < 1 : true) {
                return new ResultatAction<>(false, null, 0L, "La tranche de vente pour cette facture n'a pas ete trouvé");
            }

            YvsComDocVentes facture = new YvsComDocVentes();
            facture.setId(null);
            facture.setClient(entity.getClient());
            facture.setNomClient(entity.getClient().getNom_prenom());
            facture.setCategorieComptable(entity.getClient().getCategorieComptable());
            facture.setAdresse(entity.getClient().getTiers().getSecteur());
            facture.setModelReglement(entity.getClient().getModel());
            facture.setTypeDoc(Constantes.TYPE_FV);
            facture.setStatut(Constantes.ETAT_EDITABLE);
            facture.setStatutLivre(Constantes.ETAT_ATTENTE);
            facture.setStatutRegle(Constantes.ETAT_ATTENTE);
            facture.setSupp(false);
            facture.setAuthor(author);
            facture.setOperateur(author.getUsers());
            facture.setHeureDoc(new Date());
            String numero = dao.genererReference(Constantes.TYPE_FV_NAME, date, point.getId(), agence.getSociete(), agence);
            if ((numero != null) ? numero.trim().equals("") : true) {
                return new ResultatAction<>(false, null, 0L, dao.getRESULT());
            }
            facture.setNumDoc(numero);
            YvsComEnteteDocVente entete = dao.getEntete(vendeur, point, tranche, date, niveau, agence, author);
            if (entete != null ? entete.getId() < 1 : true) {
                return new ResultatAction<>(false, null, 0L, dao.getRESULT());
            }
            facture.setEnteteDoc(entete);
            facture = (YvsComDocVentes) dao.save1(facture);
            if (facture != null ? facture.getId() > 0 : false) {
                YvsComContenuDocVente contenu;
                for (YvsComElementContratsClient element : entity.getElements()) {
                    contenu = new YvsComContenuDocVente();
                    contenu.setId(null);
                    contenu.setArticle(element.getArticle().getArticle());
                    contenu.setDocVente(new YvsComDocVentes(facture.getId()));
                    contenu.setQuantite(element.getQuantite());
                    contenu.setPrix(element.getPrix());
                    contenu.setSupp(false);
                    contenu.setActif(true);
                    contenu.setDateContenu(date);
                    contenu.setAuthor(author);
                    contenu.setStatut(Constantes.ETAT_EDITABLE);
                    contenu.setPuvMin(element.getArticle().getPrixMin());
                    contenu.setPrixTotal(contenu.getQuantite() * contenu.getPrix());
                    contenu.setConditionnement(element.getArticle());
                    contenu.setCalculPr(true);

                    contenu = (YvsComContenuDocVente) dao.save1(contenu);
                    facture.getContenus().add(contenu);
                }
                YvsComCoutSupDocVente cout;
                for (YvsComElementAddContratsClient element : entity.getAdditionnels()) {
                    if (element.getApplication().equals('I')) {
                        cout = new YvsComCoutSupDocVente();
                        cout.setId(null);
                        cout.setMontant(element.getMontant());
                        cout.setDocVente(new YvsComDocVentes(facture.getId()));
                        cout.setSupp(false);
                        cout.setActif(true);
                        cout.setAuthor(author);
                        cout.setTypeCout(element.getTypeCout());
                        cout.setService(true);

                        cout = (YvsComCoutSupDocVente) dao.save1(cout);
                        facture.getCouts().add(cout);
                    }
                }
                result = new ResultatAction(true, facture, facture.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComContratsClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.services.commercial;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Util;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.GenericService;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.vente.YvsComEcartEnteteVente;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.vente.YvsComptaCaissePieceEcartVente;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
public class ServiceClotureVente extends GenericService {

    public ServiceClotureVente(DaoInterfaceLocal dao, YvsNiveauAcces niveau, YvsUsersAgence user, YvsSocietes currentScte) {
        this.dao = dao;
        this.niveau = niveau;
        this.currentUser = user;
        this.currentScte = currentScte;
    }

    public String reference(Date date) {
        String numero = "ECR/";
        try {
            Calendar cal = Util.dateToCalendar(date);
            if (cal.get(Calendar.DATE) > 9) {
                numero += Integer.toString(cal.get(Calendar.DATE));
            }
            if (cal.get(Calendar.DATE) < 10) {
                numero += ("0" + Integer.toString(cal.get(Calendar.DATE)));
            }
            if (cal.get(Calendar.MONTH) + 1 > 9) {
                numero += Integer.toString(cal.get(Calendar.MONTH) + 1);
            }
            if (cal.get(Calendar.MONTH) + 1 < 10) {
                numero += ("0" + Integer.toString(cal.get(Calendar.MONTH) + 1));
            }
            numero += Integer.toString(cal.get(Calendar.YEAR)).substring(2) + "/";
            String reference = null;
            List<YvsComEcartEnteteVente> list = dao.loadNameQueries("YvsComEcartEnteteVente.findLikeNumero", new String[]{"numero", "societe"}, new Object[]{numero + "%", currentScte});
            if (list != null ? !list.isEmpty() : false) {
                reference = list.get(0).getNumero();
            }
            if (Util.asString(reference)) {
                int numeric = Integer.valueOf(reference.replace(numero, "").trim());
                numeric = numeric + 1;
                for (int i = 0; i < (4 - Integer.toString(numeric).length()); i++) {
                    numero += "0";
                }
                numero += numeric;
            } else {
                for (int i = 0; i < 3; i++) {
                    numero += "0";
                }
                numero += "1";
            }
        } catch (NumberFormatException ex) {
            getException("ServiceClotureVente (reference)", ex);
        }
        return numero;
    }

    public ResultatAction saveNew(YvsComEnteteDocVente entete, Date date, long caisse, double montant) {
        ResultatAction result = new ResultatAction();
        if (entete != null ? (entete.getId() < 1) : true) {
            result.setMessage("Vous devez precisez une journal de vente");
            return result;
        }
        if (entete.getCreneau() != null ? entete.getCreneau().getId() < 1 : true) {
            result.setMessage("Vous avez precisez un journal de vente qui n'a pas de planning");
            return result;
        }
        return saveNew(null, entete, entete.getCreneau().getUsers(), date, caisse, montant);
    }

    public ResultatAction saveNew(Long id, YvsComEnteteDocVente entete, YvsUsers users, Date date, long caisse, double montant) {
        return saveNew(id, null, entete, users, date, new YvsBaseCaisse(caisse), montant);
    }

    public ResultatAction saveNew(Long id, String numero, YvsComEnteteDocVente entete, YvsUsers users, Date date, YvsBaseCaisse caisse, double montant) {
        ResultatAction result = new ResultatAction();
        try {
            if (users != null ? users.getId() < 1 : true) {
                result.setMessage("Vous devez selectionner un vendeur");
                return result;
            }
            if (montant == 0) {
                result.setMessage("Vous devez indiquez le montant de l'écart");
                return result;
            }
            if (date == null) {
                result.setMessage("Vous devez entrer la date de l'ecart");
                return result;
            }
            if (numero != null ? numero.trim().length() < 1 : true) {
                numero = reference(date);
            }
            YvsComEcartEnteteVente entity = new YvsComEcartEnteteVente(id);
            entity.setNumero(numero);
            if (currentUser != null) {
                entity.setAuthor(currentUser);
            }
            entity.setDateEcart(date);
            if (entete != null ? entete.getId() > 0 : false) {
//                entity.setEnteteDoc(entete);
            }
            entity.setMontant(montant);
            entity.setStatut(Constantes.STATUT_DOC_ATTENTE);
            entity.setUsers(users);
            if (id != null ? id < 1 : true) {
                entity.setStatut(Constantes.STATUT_EDITABLE);
                entity.setStatutRegle(Constantes.STATUT_ATTENTE);
                entity = (YvsComEcartEnteteVente) dao.save1(entity);
//                equilibreEcart(entity);
            } else {
                dao.update(entity);
                entity = (YvsComEcartEnteteVente) dao.loadOneByNameQueries("YvsComEcartEnteteVente.findById", new String[]{"id"}, new Object[]{entity.getId()});
            }
            entity = savePieceEcart(entity, caisse);
            if (entity != null ? entity.getId() > 0 : false) {
                result.setData(entity);
                result.setResult(true);
            }
        } catch (Exception ex) {
            result.setMessage("Action impossible");
            getException("ServiceClotureVente (saveNew)", ex);
        }
        return result;
    }

    public YvsComEcartEnteteVente savePieceEcart(YvsComEcartEnteteVente entity, YvsBaseCaisse caisse) {
        try {
            if (caisse != null ? caisse.getId() > 0 : false) {//Verifie si on veut positionner l'ecart dans une caisse
                if (entity.getCaisse() != null ? entity.getCaisse().getId() > 0 : false) {//Verifie si  l'ecart etait déja positionné dans une caisse
                    if (!entity.getCaisse().equals(caisse)) {//Verifie si la caisse de l'ecart est differente de la nouvelle caisse
                        entity.getCaisse().setCaisse(caisse);
                        dao.update(entity.getCaisse());
                    }
                } else {
                    YvsBaseModeReglement mode = modeEspece(currentUser.getUsers().getAgence().getSociete());
                    if (mode != null ? mode.getId() > 0 : false) {
                        YvsComptaCaissePieceEcartVente ecart = new YvsComptaCaissePieceEcartVente(caisse, mode, entity, currentUser);
                        dao.save1(ecart);
                        entity.setCaisse(ecart);
                    }
                }
            } else if (entity.getCaisse() != null ? entity.getCaisse().getId() > 0 : false) {//Verifie si  l'ecart etait déja positionné dans une caisse
                dao.delete(entity.getCaisse());
                entity.setCaisse(null);
            }
            return entity;
        } catch (Exception ex) {
            result.setMessage("Action impossible");
            getException("ServiceClotureVente (savePieceEcart)", ex);
        }
        return null;
    }
}

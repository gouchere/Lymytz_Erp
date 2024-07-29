/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseModeleReference;
import yvs.entity.param.YvsSocietes;

/**
 *
 * @author Lymytz-pc
 */
public class ReferenceService {

    private static List<ReferenceService> services = new ArrayList<>();

    DaoInterfaceLocal dao;

    private List<Reference> references;
    private Long societe;

    public ReferenceService(Long societe, DaoInterfaceLocal dao) {
        this.societe = societe;
        this.dao = dao;
        this.references = new ArrayList<>();
    }

    public static void clear() {
        services.clear();
    }

    public static List<ReferenceService> getServices() {
        return services;
    }

    public List<Reference> getReferences() {
        return references;
    }

    private boolean isString(String value) {
        return value != null ? !value.trim().isEmpty() : false;
    }

    private boolean isNumeric(String value) {
        try {
            if (isString(value)) {
                Float.valueOf(value);
                return true;
            }
        } catch (Exception ex) {

        }
        return false;
    }

    public String nextVal(String prefix) {
        try {
            if (dao == null) {
                Logger.getLogger(ReferenceService.class.getName()).log(Level.SEVERE, "dao est null", new Object[]{});
                return null;
            }
            Reference reference = new Reference(prefix);
            int index = references.indexOf(reference);
            if (index < 0) {
                String separateur = "_";
                if (prefix.contains("/")) {
                    separateur = "/";
                } else if (prefix.contains("-")) {
                    separateur = "-";
                }
                String[] tab = prefix.split(separateur);
                if (tab != null ? tab.length < 1 : true) {
                    Logger.getLogger(ReferenceService.class.getName()).log(Level.SEVERE, "Prefixe incorrect " + prefix, new Object[]{});
                    return null;
                }
                YvsBaseModeleReference model = (YvsBaseModeleReference) dao.loadOneByNameQueries("YvsBaseModeleReference.findByPrefix", new String[]{"prefix", "societe"}, new Object[]{tab[0], new YvsSocietes(societe)});
                return nextVal(prefix, model);
            } else {
                return nextVal(prefix, null);
            }
        } catch (Exception ex) {
            Logger.getLogger(ReferenceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String nextVal(String prefix, YvsBaseModeleReference model) {
        try {
            if (dao == null) {
                Logger.getLogger(ReferenceService.class.getName()).log(Level.SEVERE, "dao est null", new Object[]{});
                return null;
            }
            Reference reference = new Reference(prefix);
            int index = references.indexOf(reference);
            if (index < 0) {
                if (model != null ? model.getId() < 1 : true) {
                    Logger.getLogger(ReferenceService.class.getName()).log(Level.SEVERE, "Aucun model de reference trouvé pour ce prefixe " + prefix, new Object[]{});
                    return null;
                }
                String query = "";
                switch (model.getElement().getDesignation()) {
                    case Constantes.TYPE_FV_NAME:
                    case Constantes.TYPE_BLV_NAME:
                    case Constantes.TYPE_BRV_NAME:
                    case Constantes.TYPE_BAV_NAME:
                    case Constantes.TYPE_BCV_NAME:
                    case Constantes.TYPE_FRV_NAME:
                    case Constantes.TYPE_FAV_NAME: {
                        query = "SELECT num_doc FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id INNER JOIN yvs_agences a ON e.agence = a.id "
                                + "WHERE d.num_doc LIKE ? AND a.societe = ? ORDER BY d.num_doc DESC LIMIT 1";
                        break;
                    }
                    case Constantes.TYPE_BP_NAME: {
                        break;
                    }
                    case Constantes.TYPE_FiA_NAME: {
                        break;
                    }
                    case Constantes.TYPE_BLA_NAME:
                    case Constantes.TYPE_BRA_NAME:
                    case Constantes.TYPE_BAA_NAME:
                    case Constantes.TYPE_BCA_NAME:
                    case Constantes.TYPE_FRA_NAME:
                    case Constantes.TYPE_FAA_NAME:
                    case Constantes.TYPE_FA_NAME: {
                        break;
                    }
                    case Constantes.TYPE_FT_NAME:
                    case Constantes.TYPE_SS_NAME:
                    case Constantes.TYPE_ES_NAME:
                    case Constantes.TYPE_RE_NAME:
                    case Constantes.TYPE_RA_NAME: {
                        break;
                    }
                    case Constantes.TYPE_OT_NAME: {
                        break;
                    }
                    case Constantes.TYPE_RS_NAME: {
                        break;
                    }
                    case Constantes.TYPE_OD_NAME:
                    case Constantes.TYPE_OD_RECETTE_NAME:
                    case Constantes.TYPE_OD_DEPENSE_NAME: {
                        break;
                    }
                    case Constantes.TYPE_PT_NAME:
                    case Constantes.TYPE_PC_NAME:
                    case Constantes.TYPE_PC_ACHAT_NAME:
                    case Constantes.TYPE_PC_VENTE_NAME:
                    case Constantes.TYPE_PC_MISSION_NAME:
                    case Constantes.TYPE_PC_DIVERS_NAME:
                    case Constantes.TYPE_PT_AVANCE_VENTE:
                    case Constantes.TYPE_PT_AVANCE_ACHAT: {
                        break;
                    }
                    case Constantes.TYPE_PIECE_COMPTABLE_NAME: {
                        break;
                    }
                    case Constantes.TYPE_DOC_MISSION_NAME: {
                        break;
                    }
                    case Constantes.MUT_TRANSACTIONS_MUT: {
                        break;
                    }
                    case Constantes.MUT_ACTIVITE_CREDIT: {
                        break;
                    }
                    case Constantes.PROD_TYPE_PROD_NAME: {
                        break;
                    }
                    case Constantes.TYPE_EMPLOYE: {
                        break;
                    }
                }
                if (!isString(query)) {
                    Logger.getLogger(ReferenceService.class.getName()).log(Level.SEVERE, "nextVal incomplet pour le prefixe " + prefix, new Object[]{});
                    return null;
                }
                Object result = dao.loadObjectBySqlQuery(query, new Options[]{new Options(prefix + "%", 1), new Options(societe, 2)});
                if (result != null) {
                    String numero = (String) result;
                    reference.setCurrent(numero.replace(prefix, "").trim());
                } else {
                    String value = "";
                    for (int i = 0; i < model.getTaille(); i++) {
                        value += "0";
                    }
                    reference.setCurrent(value);
                }
            } else {
                reference = references.get(index);
            }
            return getValue(reference, index);
        } catch (Exception ex) {
            Logger.getLogger(ReferenceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private synchronized String getValue(Reference reference, int index) {
        try {
            long current = Long.valueOf(reference.getCurrent());
            long next = current + 1;
            int length = reference.getCurrent().length();
            String value = "";
            for (int i = String.valueOf(next).length(); i < length; i++) {
                value += "0";
            }
            value += String.valueOf(next);
            reference.setCurrent(value);
            if (index > -1) {
                references.set(index, reference);
            } else {
                references.add(reference);
            }
            return reference.get();
        } catch (Exception ex) {
            Logger.getLogger(ReferenceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public synchronized static String nextValue(Long societe, String prefixe, DaoInterfaceLocal dao) {
        return nextValue(societe, prefixe, null, dao);
    }

    public synchronized static String nextValue(Long societe, String prefixe, YvsBaseModeleReference model, DaoInterfaceLocal dao) {
        try {
            if (services != null) {
                ReferenceService service = new ReferenceService(societe, dao);
                int index = services.indexOf(service);
                if (index > -1) {
                    service = services.get(index);
                }
                String numero = model != null ? service.nextVal(prefixe, model) : service.nextVal(prefixe);
                if (index > -1) {
                    services.set(index, service);
                } else {
                    services.add(service);
                }
                return numero;
            }
        } catch (Exception ex) {
            Logger.getLogger(ReferenceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.societe);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReferenceService other = (ReferenceService) obj;
        if (!Objects.equals(this.societe, other.societe)) {
            return false;
        }
        return true;
    }

}

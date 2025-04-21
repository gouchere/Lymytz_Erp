/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.local;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseDepotOperation;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseModeleReference;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComFicheApprovisionnement;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.stock.YvsComReservationStock;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.produits.YvsBaseArticles;

/**
 *
 * @author LENOVO
 */
public class UtilsBean {

    Requete dao;
    DaoInterfaceLocal ejb;

    public UtilsBean(Requete dao) {
        this.dao = dao;
    }

    public UtilsBean(Requete dao, DaoInterfaceLocal ejb) {
        this.dao = dao;
        this.ejb = ejb;
    }


    /*Générer les références des documents*/
    private YvsBaseModeleReference rechercheModeleReference(String mot, YvsAgences agence) {
        if (!mot.toLowerCase().equals("")) {
            String[] ch = new String[]{"designation", "societe"};
            Object[] v = new Object[]{mot, agence.getSociete()};
            String query = "YvsBaseModeleReference.findByElement";
            YvsBaseModeleReference l = null;
            if (dao != null) {
                l = (YvsBaseModeleReference) dao.loadObjectByNameQueries(query, ch, v);
            } else {
                if (ejb != null) {
                    l = (YvsBaseModeleReference) ejb.loadObjectByNameQueries(query, ch, v);
                }
            }
            return l;
        }
        return null;
    }

    public String genererReference(String element, Date date, long id, String type, String code, YvsAgences agence) {
        YvsBaseModeleReference model = rechercheModeleReference(element, agence);
        if ((model != null) ? model.getId() > 0 : false) {
            return getReferenceElement(model, date, id, type, code, agence);
        } else {
            return "";
        }

    }

    private String getReferenceElement(YvsBaseModeleReference modele, Date date, long id, String type, String code, YvsAgences agence) {
        String motRefTable = "";
        String inter = genererPrefixeComplet(modele, date, id, type, code, agence);
        switch (modele.getElement().getDesignation()) {
            case "Employe": {
                break;
            }
            case Constantes.TYPE_BP_NAME: {
                String[] ch = new String[]{"reference", "societe"};
                Object[] v = new Object[]{inter + "%", agence.getSociete()};
                String query = "YvsComptaBonProvisoire.findByReference";
                List<YvsComptaBonProvisoire> l = (dao != null) ? dao.loadNameQueries(query, ch, v) : ejb.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumero();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_FiA_NAME: {
                String[] ch = new String[]{"reference", "societe"};
                Object[] v = new Object[]{inter + "%", agence.getSociete()};
                String query = "YvsComFicheApprovisionnement.findByReference";
                List<YvsComFicheApprovisionnement> l = (dao != null) ? dao.loadNameQueries(query, ch, v) : ejb.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getReference();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_BLA_NAME:
            case Constantes.TYPE_BRA_NAME:
            case Constantes.TYPE_BAA_NAME:
            case Constantes.TYPE_BCA_NAME:
            case Constantes.TYPE_FRA_NAME:
            case Constantes.TYPE_FAA_NAME:
            case Constantes.TYPE_FA_NAME: {
                String[] ch = new String[]{"numRefDoc", "societe"};
                Object[] v = new Object[]{inter + "%", agence.getSociete()};
                String query = "YvsComDocAchats.findByReference";
                List<YvsComDocAchats> l = (dao != null) ? dao.loadNameQueries(query, ch, v) : ejb.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumDoc();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_BLV_NAME:
            case Constantes.TYPE_BRV_NAME:
            case Constantes.TYPE_BAV_NAME:
            case Constantes.TYPE_BCV_NAME:
            case Constantes.TYPE_FRV_NAME:
            case Constantes.TYPE_FAV_NAME:
            case Constantes.TYPE_FV_NAME: {
                String[] ch = new String[]{"numDoc", "societe"};
                Object[] v = new Object[]{inter + "%", agence.getSociete()};
                String query = "YvsComDocVentes.findByReference";
                List<YvsComDocVentes> l = (dao != null) ? dao.loadNameQueries(query, ch, v) : ejb.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumDoc();
                } else {
                    motRefTable = "";
                }
                System.err.println(" Mot ref. " + motRefTable);
                break;
            }
            case Constantes.TYPE_FT_NAME:
            case Constantes.TYPE_SS_NAME:
            case Constantes.TYPE_ES_NAME:
            case Constantes.TYPE_IN_NAME:
            case Constantes.TYPE_RE_NAME:
            case Constantes.TYPE_OT_NAME: {
                String[] ch = new String[]{"numDoc", "societe"};
                Object[] v = new Object[]{inter + "%", agence.getSociete()};
                String query = "YvsComDocStocks.findByReference";
                List<YvsComDocStocks> l = (dao != null) ? dao.loadNameQueries(query, ch, v) : ejb.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumDoc();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_RS_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", agence.getSociete()};
                String query = "YvsComReservationStock.findByReference";
                List<YvsComReservationStock> l = (dao != null) ? dao.loadNameQueries(query, ch, v) : ejb.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumReference();
                } else {
                    motRefTable = "";
                }
                break;
            }
//            case Constantes.TYPE_RA_NAME: {
//                String[] ch = new String[]{"numDoc", "agence"};
//                Object[] v = new Object[]{inter + "%", currentAgence};
//                String query = "YvsComDocRation.findByNumDocL";
//                List<YvsComDocRation> l = dao.loadNameQueries(query, ch, v);
//                if ((l != null) ? !l.isEmpty() : false) {
//                    motRefTable = l.get(0).getNumDoc();
//                } else {
//                    motRefTable = "";
//                }
//                break;
//            }
            case Constantes.TYPE_OD_NAME: {
                String[] ch = new String[]{"numPiece", "societe"};
                Object[] v = new Object[]{inter + "%", agence.getSociete()};
                String query = "YvsComptaCaisseDocDivers.findByNumPieceLike";
                List<YvsComptaCaisseDocDivers> l = (dao != null) ? dao.loadNameQueries(query, ch, v) : ejb.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PT_NAME: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", agence.getSociete()};
                String query = "YvsComptaCaissePieceVirement.findByNumeroPiece";
                List<YvsComptaCaissePieceVirement> l = (dao != null) ? dao.loadNameQueries(query, ch, v) : ejb.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", agence.getSociete()};
                String query = "YvsComptaMouvementCaisse.findByNumeroPiece";
                List<YvsComptaMouvementCaisse> l = (dao != null) ? dao.loadNameQueries(query, ch, v) : ejb.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumero();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PT_AVANCE_VENTE: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", agence.getSociete()};
                String query = "YvsComptaAcompteClient.findByNumPiece";
                List<YvsComptaAcompteClient> l = (dao != null) ? dao.loadNameQueries(query, ch, v) : ejb.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumRefrence();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PT_AVANCE_ACHAT: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", agence.getSociete()};
                String query = "YvsComptaAcompteFournisseur.findByNumPiece";
                List<YvsComptaAcompteFournisseur> l = (dao != null) ? dao.loadNameQueries(query, ch, v) : ejb.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumRefrence();
                } else {
                    motRefTable = "";
                }
                break;
            }
//            case Constantes.TYPE_PIECE_COMPTABLE_NAME: {
//                String[] ch = new String[]{"numeroPiece", "agence"};
//                Object[] v = new Object[]{inter + "%", currentAgence};
//                String query = "YvsComptaPiecesComptable.findByNumPiece";
//                List<YvsComptaPiecesComptable> l = dao.loadNameQueries(query, ch, v);
//                if ((l != null) ? !l.isEmpty() : false) {
//                    motRefTable = l.get(0).getNumPiece();
//                } else {
//                    motRefTable = "";
//                }
//                break;
//            }
//            case Constantes.TYPE_DOC_MISSION_NAME:
//                String[] ch = new String[]{"numeroPiece", "agence"};
//                Object[] v = new Object[]{inter + "%", currentAgence};
//                String query = "YvsGrhMissions.findByNumPiece";
//                List<YvsGrhMissions> lm = dao.loadNameQueries(query, ch, v);
//                if ((lm != null) ? !lm.isEmpty() : false) {
//                    motRefTable = lm.get(0).getNumeroMission();
//                } else {
//                    motRefTable = "";
//                }
//                break;
//            case Constantes.MUT_TRANSACTIONS_MUT: {
//                ch = new String[]{"numeroPiece", "mutuelle"};
//                v = new Object[]{inter + "%", currentMutuel};
//                query = "YvsMutOperationCompte.findByNumOp";
//                List<YvsMutOperationCompte> lop = dao.loadNameQueries(query, ch, v);
//                if ((lop != null) ? !lop.isEmpty() : false) {
//                    motRefTable = lop.get(0).getReferenceOperation();
//                } else {
//                    motRefTable = "";
//                }
//                break;
//            }
//            case Constantes.MUT_ACTIVITE_CREDIT: {
//                ch = new String[]{"numeroPiece", "mutuelle"};
//                v = new Object[]{inter + "%", currentMutuel};
//                query = "YvsMutCredit.findByNumOp";
//                List<YvsMutCredit> lop = dao.loadNameQueries(query, ch, v);
//                if ((lop != null) ? !lop.isEmpty() : false) {
//                    motRefTable = lop.get(0).getReference();
//                } else {
//                    motRefTable = "";
//                }
//                break;
//            }
//            case Constantes.PROD_TYPE_PROD_NAME: {
//                ch = new String[]{"codeRef", "societe"};
//                v = new Object[]{inter + "%", societe};
//                query = "YvsProdOrdreFabrication.findByReference";
//                List<YvsProdOrdreFabrication> lop = dao.loadNameQueries(query, ch, v);
//                if ((lop != null) ? !lop.isEmpty() : false) {
//                    motRefTable = lop.get(0).getCodeRef();
//                } else {
//                    motRefTable = "";
//                }
//                break;
//            }
            default: {
                break;
            }
        }
        String partieNum = motRefTable.replaceFirst(inter, "");
        if (partieNum != null ? partieNum.trim().length() > 0 : false) {
            int num = Integer.valueOf(partieNum.trim().replace("°", ""));
            if (Integer.toString(num + 1).length() > modele.getTaille()) {
                return "";
            } else {
                for (int i = 0; i < (modele.getTaille() - Integer.toString(num + 1).length()); i++) {
                    inter += "0";
                }
            }
            inter += Long.toString(Long.valueOf(partieNum.trim().replace("°", "")) + 1);
        } else {
            for (int i = 0; i < modele.getTaille() - 1; i++) {
                inter += "0";
            }
            inter += "1";
        }
        return inter;
    }

    public String genererPrefixe(String element, long id, String type, String code, YvsAgences agence) {
        YvsBaseModeleReference modele = rechercheModeleReference(element, agence);
        if ((modele != null) ? modele.getId() > 0 : false) {
            return genererPrefixe(modele, id, type, code, agence);
        }
        return "";
    }

    public String genererPrefixe(YvsBaseModeleReference modele, long id, String type, String code, YvsAgences agence) {
        String inter = modele.getPrefix();
        if (id > 0 && type != null) {
            code = genererPrefixe(modele, id, type, agence);
        }
        if (code != null ? code.trim().length() > 0 : false) {
            inter += modele.getSeparateur() + code;
        }
        inter += modele.getSeparateur();
        return inter != null ? inter : "";
    }

    public String genererPrefixe(YvsBaseModeleReference modele, long id, String type, YvsAgences agence) {
        if (modele.getCodePoint()) {
            String code = "";
            switch (modele.getElementCode()) {

                case Constantes.SOCIETE: {
                    if (agence != null ? agence.getSociete().getCodeAbreviation().trim().length() > 0 : false) {
                        code = agence.getSociete().getCodeAbreviation();
                    }
                    break;
                }
                case Constantes.AGENCE: {
                    if (agence != null ? agence.getAbbreviation().trim().length() > 0 : false) {
                        code = agence.getAbbreviation();
                    }
                    break;
                }
                case Constantes.AUTRE: {
                    switch (modele.getElement().getDesignation()) {
                        case Constantes.DEPOT: {
                            YvsBaseDepots p = (YvsBaseDepots) (dao != null ? dao.loadObjectByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{id}) : ejb.loadObjectByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{id}));
                            if (p != null ? p.getId() > 0 : false) {
                                code = p.getAbbreviation();
                            }
                            break;
                        }
                        case Constantes.JOURNAL: {
                            YvsComptaJournaux j = (YvsComptaJournaux) (dao != null ? dao.loadObjectByNameQueries("YvsComptaJournaux.findById", new String[]{"id"}, new Object[]{id}) : ejb.loadObjectByNameQueries("YvsComptaJournaux.findById", new String[]{"id"}, new Object[]{id}));
                            if (j != null ? j.getId() > 0 : false) {
                                code = j.getCodeJournal();
                            }
                            break;
                        }
                        case Constantes.POINTVENTE: {
                            YvsBasePointVente p = (YvsBasePointVente) (dao != null ? dao.loadObjectByNameQueries("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{id}) : ejb.loadObjectByNameQueries("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{id}));
                            if (p != null ? p.getId() > 0 : false) {
                                code = p.getCode();
                            }
                            break;
                        }
                        case Constantes.CAISSE: {
//                    YvsMutCaisse en = (YvsMutCaisse) dao.loadObjectByNameQueries("YvsMutCaisse.findById", new String[]{"id"}, new Object[]{id});
//                    if ((en != null ? en.getId() > 0 : false) && currentMutuel != null) {
//                        if (en.getReferenceCaisse().length() > modele.getLongueurCodePoint()) {
//                            modele.setCodePointvente(currentMutuel.getCode() + "/" + en.getReferenceCaisse().substring(0, modele.getLongueurCodePoint()));
//                        } else {
//                            modele.setCodePointvente(currentMutuel.getCode() + "/" + en.getReferenceCaisse());
//                        }
//                    }
//                    return modele.getCodePointvente();
                        }
//                case Constantes.TYPECREDIT: {
//                    YvsMutTypeCredit p = (YvsMutTypeCredit) dao.loadObjectByNameQueries("YvsMutTypeCredit.findById", new String[]{"id"}, new Object[]{id});
//                    if ((p != null ? p.getId() > 0 : false) && currentMutuel != null) {
//                        if (p.getCode().length() > modele.getLongueurCodePoint()) {
//                            modele.setCodePointvente(currentMutuel.getCode() + "/" + p.getCode().substring(0, modele.getLongueurCodePoint()));
//                        } else {
//                            modele.setCodePointvente(currentMutuel.getCode() + "/" + p.getCode());
//                        }
//                    }
//                    return modele.getCodePointvente();
//                }
                        default: {
                            if (agence != null ? agence.getSociete().getCodeAbreviation().trim().length() > 0 : false) {
                                code = agence.getSociete().getCodeAbreviation();
                            }
                            break;
                        }
                    }
                }
            }
            if (code.length() > modele.getLongueurCodePoint()) {
                return code.substring(0, modele.getLongueurCodePoint());
            } else {
                return code;
            }
        }
        return modele.getPrefix();
    }

    public String genererPrefixeComplet(YvsBaseModeleReference modele, Date date, long id, String type, String code, YvsAgences agence) {
        String prefixe = genererPrefixe(modele, id, type, code, agence);
        if (prefixe != null ? prefixe.trim().length() > 0 : false) {
            Calendar cal = Constantes.dateToCalendar(date);
            if (modele.getJour()) {
                if (cal.get(Calendar.DATE) > 9) {
                    prefixe += Integer.toString(cal.get(Calendar.DATE));
                }
                if (cal.get(Calendar.DATE) < 10) {
                    prefixe += ("0" + Integer.toString(cal.get(Calendar.DATE)));
                }
            }
            if (modele.getMois()) {
                if (cal.get(Calendar.MONTH) + 1 > 9) {
                    prefixe += Integer.toString(cal.get(Calendar.MONTH) + 1);
                }
                if (cal.get(Calendar.MONTH) + 1 < 10) {
                    prefixe += ("0" + Integer.toString(cal.get(Calendar.MONTH) + 1));
                }
            }
            if (modele.getAnnee()) {
                prefixe += Integer.toString(cal.get(Calendar.YEAR)).substring(2);
            }
            prefixe += modele.getSeparateur();
        }
        return prefixe != null ? prefixe : "";
    }

    public static boolean checkOperationArticle(long article, long depot, String operation) {
        Requete rq = new Requete();
        if (depot > 0) {
            String[] champ = new String[]{"depot", "article"};
            Object[] val = new Object[]{new YvsBaseDepots(depot), new YvsBaseArticles(article)};
            String nameQueri = "YvsBaseArticleDepot.findByArticleDepot";
            List<YvsBaseArticleDepot> l = rq.loadNameQueries(nameQueri, champ, val, 0, 1);
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

    public static boolean checkOperationDepot(long depot, String type) {
        Requete rq = new Requete();
        String[] champ = new String[]{"depot", "type"};
        Object[] val = new Object[]{new YvsBaseDepots(depot), type};
        String nameQueri = "YvsBaseDepotOperation.findByDepotType";
        List<YvsBaseDepotOperation> l = rq.loadNameQueries(nameQueri, champ, val, 0, 1);
        return l != null ? !l.isEmpty() : false;
    }

    public double arrondi(double d, YvsSocietes societe) {
        Requete rq = new Requete();
        return rq.arrondi(societe != null ? societe.getId() : 0, d);
    }

    public double setMontantTotalDoc(YvsComDocVentes doc, List<YvsComContenuDocVente> lc) {
        doc.setMontantRemise(0);
        doc.setMontantTaxe(0);
        doc.setMontantRistourne(0);
        doc.setMontantCommission(0);
        doc.setMontantHT(0);
        doc.setMontantTTC(0);
        doc.setMontantRemises(0);
        doc.setMontantCS(0);
        doc.setMontantAvance(0.0);
        doc.setMontantTaxeR(0);
        doc.setMontantResteApayer(0);
        doc.setMontantPlanifier(0);
        if (lc != null ? !lc.isEmpty() : false) {
            for (YvsComContenuDocVente c : lc) {
                doc.setMontantRemise(doc.getMontantRemise() + c.getRemise());
                doc.setMontantRistourne(doc.getMontantRistourne() + c.getRistourne());
                doc.setMontantCommission(doc.getMontantCommission() + c.getComission());
                doc.setMontantTTC(doc.getMontantTTC() + c.getPrixTotal());
                doc.setMontantTaxe(doc.getMontantTaxe() + c.getTaxe());
                doc.setMontantTaxeR(doc.getMontantTaxeR() + ((c.getArticle().getPuvTtc()) ? (c.getTaxe()) : 0));
            }
        }

        String[] champ = new String[]{"facture", "statut"};
        Object[] val = new Object[]{doc, Constantes.STATUT_DOC_PAYER};
        String nameQueri = "YvsComptaCaissePieceVente.findByFactureStatutS";
        Double a = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        doc.setMontantAvance(a != null ? a : 0);
        val = new Object[]{doc, Constantes.STATUT_DOC_SUSPENDU};
        nameQueri = "YvsComptaCaissePieceVente.findByFactureStatutSDiff";
        a = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        doc.setMontantPlanifier(a != null ? a : 0);

        champ = new String[]{"docVente", "sens"};
        val = new Object[]{doc, true};
        Double p = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumByDocVente", champ, val);
        val = new Object[]{doc, false};
        Double m = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumByDocVente", champ, val);
        double s = (p != null ? p : 0) - (m != null ? m : 0);
        doc.setMontantCS(s);
        YvsSocietes scte = doc.getEnteteDoc().getAgence().getSociete();
        doc.setMontantRemise(arrondi(doc.getMontantRemise(), scte));
        doc.setMontantTaxe(arrondi(doc.getMontantTaxe(), scte));
        doc.setMontantRistourne(arrondi(doc.getMontantRistourne(), scte));
        doc.setMontantCommission(arrondi(doc.getMontantCommission(), scte));
        doc.setMontantHT(arrondi(doc.getMontantHT(), scte));
        doc.setMontantTTC(arrondi(doc.getMontantTTC(), scte));
        doc.setMontantRemises(arrondi(doc.getMontantRemises(), scte));
        doc.setMontantCS(arrondi(doc.getMontantCS(), scte));
        doc.setMontantAvance(arrondi(doc.getMontantAvance(), scte));
        doc.setMontantTaxeR(arrondi(doc.getMontantTaxeR(), scte));
        doc.setMontantResteApayer(arrondi(doc.getMontantResteApayer(), scte));
        return doc.getMontantTotal();
    }

    public static double calculeVersementAttendu(long idCaisse) {
        double re = 0;
        // le versement attendu peut correspondre au solde de la caisse du vendeur

        return getTotalCaisse(0, idCaisse, 0, null, "R", "ESPECE,BANQUE", Constantes.STATUT_DOC_PAYER, new Date())
                - getTotalCaisse(0, idCaisse, 0, null, "D", "ESPECE,BANQUE", Constantes.STATUT_DOC_PAYER, new Date());
    }

    public static double getTotalCaisse(long societe, long caisse, long mode, String table, String mouvement, String type, Character statut, Date date) {
//        String query = "select public.compta_total_caisse(:societe,:caisse,:mode,:table,:mouvement,:type,:statut,:date)";
//        Requete rq = new Requete();
//        String champ[] = new String[]{"societe", "caisse", "mode", "table", "mouvement", "type", "statut", "date"};
//        Object val[] = new Object[]{societe, caisse, mode, table, mouvement, type, statut, date};
//        double re = rq.callProcedure(query, champ, val);
//        return re;
        String query = "select public.compta_total_caisse(?,?,?,?,?,?,?,?)";
        Requete rq = new Requete();
        Options[] options = new Options[]{new Options(societe, 1), new Options(caisse, 2), new Options(mode, 3), new Options(table, 4),
            new Options(mouvement, 5), new Options(type, 6), new Options(statut, 7), new Options(new Date(), 8)};
        String champ[] = new String[]{"societe", "caisse", "mode", "table", "mouvement", "type", "statut", "date"};
        Object val[] = new Object[]{societe, caisse, mode, table, mouvement, type, statut, date};
        double re = rq.getByrequeteLibre(query, options);
        return re;
    }

    public static double getTotalFacturesHeader(long header) {

        String query = "select public.get_ca_entete_vente(?)";
        Requete rq = new Requete();
        Options[] options = new Options[]{new Options(header, 1)};
        Double re = rq.getByrequeteLibre(query, options);
        return re != null ? re : 0;
    }

    public static double getVersementAttenduHeader(long header) {
        String query = "select public.com_get_versement_attendu(?)";
        Requete rq = new Requete();
        Options[] options = new Options[]{new Options("" + header, 1)};
        Double re = rq.getByrequeteLibre(query, options);
        return re != null ? re : 0;
    }

    public static double getTotalCommandeHeader(long header) {
        String champ[] = new String[]{"header", "typeDoc"};
        Object val[] = new Object[]{new YvsComEnteteDocVente(header), Constantes.TYPE_BCV};
        Double re = (Double) new Requete<>().loadObjectByNameQueries("YvsComContenuDocVente.findTotalByTypeDocAndHeader", champ, val);
        return re != null ? re : 0;
    }
}

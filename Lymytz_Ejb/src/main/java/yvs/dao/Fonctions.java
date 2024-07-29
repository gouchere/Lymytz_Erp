/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepotOperation;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBaseModeleReference;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.base.YvsBaseTrancheReglement;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComFicheApprovisionnement;
import yvs.entity.commercial.commission.YvsComCommissionCommerciaux;
import yvs.entity.commercial.ration.YvsComDocRation;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.stock.YvsComReservationStock;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.commercial.vente.YvsComProformaVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaAbonementDocDivers;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceMission;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaJustifBonMission;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.YvsComptaParametre;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaissePieceCoutDivers;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.divers.YvsComptaCentreDocDivers;
import yvs.entity.compta.divers.YvsComptaCoutSupDocDivers;
import yvs.entity.compta.divers.YvsComptaTaxeDocDivers;
import yvs.entity.compta.saisie.YvsComptaContentAnalytique;
import yvs.entity.compta.salaire.YvsComptaCaissePieceSalaire;
import yvs.entity.grh.activite.YvsGrhCongeEmps;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.contrat.YvsGrhTypeElementAdditionel;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.mutuelle.credit.YvsMutCredit;
import yvs.entity.mutuelle.operation.YvsMutOperationCompte;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsGrhPlanPrelevement;
import yvs.entity.param.YvsSocietes;
import yvs.entity.production.pilotage.YvsProdOrdreFabrication;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.entity.utils.ReferenceService;

/**
 *
 * @author Gouchere
 */
public class Fonctions {

    public String[] champ;
    public String nameQueriCount, nameQueri;
    public Object[] val;
    public static DateFormat ldf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
    public static DateFormat time = new SimpleDateFormat("HH:mm");

    public YvsAgences currentAgence = null;
    public YvsSocietes currentScte = null;
    public YvsUsersAgence currentUser = null;
    public YvsBasePointVente currentPoint = null;
    public YvsBaseDepots currentDepot = null;
    public YvsMutMutuelle currentMutuel = null;
    public YvsBaseExercice currentExo = null;

    public List<Integer> decomposeSelection(String selection, DaoInterfaceLocal dao) {
        List<Integer> re = new ArrayList<>();
        List<String> l = new ArrayList<>();
        if (selection != null) {
            String numroLine[] = selection.split("-");
            try {
                int index;
                for (String numroLine1 : numroLine) {
                    index = Integer.parseInt(numroLine1);
                    re.add(index);
                    l.add(numroLine1);
                }
            } catch (NumberFormatException ex) {
                selection = "";
                for (String numroLine1 : numroLine) {
                    if (!l.contains(numroLine1)) {
                        selection += numroLine1 + "-";      //mémorise la selection
                    }
                }
                dao.setRESULT("des élément de votre sélection doivent être encore en liaison");
            }
        }
        return re;
    }

    public void loadInfos(YvsSocietes currentScte, YvsAgences currentAgence, YvsUsersAgence currentUser, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint, YvsBaseExercice currentExo) {
        this.currentScte = currentScte;
        this.currentAgence = currentAgence;
        this.currentUser = currentUser;
        this.currentDepot = currentDepot;
        this.currentPoint = currentPoint;
        this.currentExo = currentExo;
    }

    public static int countDayBetweenDate(Date begin, Date end) {
        if (begin != null && end != null) {
            int re = 0;
            Calendar d1 = Calendar.getInstance();
            d1.setTime(begin);
            while (begin.before(end) || begin.equals(end)) {
                re++;
                d1.add(Calendar.DAY_OF_MONTH, 1);
                begin = d1.getTime();
            }
            return re;
        }
        return 0;
    }

    public YvsBaseModeReglement modeEspece(YvsUsersAgence currentUsers, DaoInterfaceLocal dao) {
        if (dao != null) {
            nameQueri = "YvsBaseModeReglement.findByDefault";
            champ = new String[]{"societe", "actif", "type", "defaut"};
            val = new Object[]{currentUsers.getAgence().getSociete(), true, Constantes.MODE_PAIEMENT_ESPECE, true};
            YvsBaseModeReglement modeByEspece = (YvsBaseModeReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (modeByEspece != null ? (modeByEspece.getId() != null ? modeByEspece.getId() < 1 : true) : true) {
                nameQueri = "YvsBaseModeReglement.findByTypeActif";
                champ = new String[]{"societe", "actif", "type"};
                val = new Object[]{currentUsers.getAgence().getSociete(), true, Constantes.MODE_PAIEMENT_ESPECE};
                modeByEspece = (YvsBaseModeReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
            }
            return modeByEspece;
        }
        return null;
    }

    public YvsBaseCaisse currentCaisse(YvsUsers responsable, DaoInterfaceLocal dao) {
        if (responsable != null ? responsable.getEmploye() != null : false) {
            nameQueri = "YvsBaseCaisse.findByResponsable";
            champ = new String[]{"responsable"};
            val = new Object[]{responsable.getEmploye()};
            return (YvsBaseCaisse) dao.loadOneByNameQueries(nameQueri, champ, val);
        }
        return null;
    }

    public YvsBaseCaisse defautlCaisse(YvsUsersAgence currentUsers, DaoInterfaceLocal dao) {
        if (dao != null) {
            nameQueri = "YvsBaseCaisse.findDefaultBySociete";
            champ = new String[]{"societe"};
            val = new Object[]{currentUsers.getAgence().getSociete()};
            return (YvsBaseCaisse) dao.loadOneByNameQueries(nameQueri, champ, val);
        }
        return null;
    }

    public double arrondi(double d, DaoInterfaceLocal dao) {
        return arrondi(d, currentScte != null ? currentScte.getId() : 0, dao);
    }

    public double arrondi(double d, long societe, DaoInterfaceLocal dao) {
        try {
            return dao.arrondi(societe, d);
        } catch (Exception ex) {
            return 0;
        }
    }

    public YvsBaseExercice giveExerciceActif(Date date, DaoInterfaceLocal dao) {
        if (currentExo != null) {
            if ((currentExo.getDateDebut().before(date) || currentExo.getDateDebut().equals(giveOnlyDate(date)))
                    && (currentExo.getDateFin().after(date) || currentExo.getDateFin().equals(giveOnlyDate(date)))) {
                return currentExo;
            }
        }
        if (currentUser != null) {
            currentExo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByActif", new String[]{"actif", "societe", "date"}, new Object[]{true, currentScte, date});
            return currentExo;
        } else {
            return null;
        }

    }

    public static Date giveOnlyDate(Date begin) {
        Calendar d1 = Calendar.getInstance();
        if (begin != null) {
            d1.setTime(begin);
        }
        d1.set(Calendar.HOUR, 0);
        d1.set(Calendar.MINUTE, 0);
        d1.set(Calendar.SECOND, 0);
        d1.set(Calendar.MILLISECOND, 0);
        return d1.getTime();
    }

    /*BEGIN REFERENCE*/
    public String getReferenceElement(YvsBaseModeleReference modele, Date date, long id, YvsUsersAgence currenUser, DaoInterfaceLocal dao, YvsAgences agences, YvsSocietes societes) {
        currentAgence = agences;
        currentScte = societes;
        return getReferenceElement(modele, date, id, societes, dao);
    }

    private String getReferenceElement(YvsBaseModeleReference modele, Date date, long id, YvsSocietes societe, DaoInterfaceLocal dao) {
        String motRefTable = "";
        String inter = genererPrefixeComplet(modele, date, id, dao);
        switch (modele.getElement().getDesignation()) {
            case "Employe": {
                break;
            }
            case Constantes.TYPE_COM_NAME: {
                String[] ch = new String[]{"reference", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComCommissionCommerciaux.findByNumero";
                List<YvsComCommissionCommerciaux> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumero();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PFV_NAME: {
                String[] ch = new String[]{"reference", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComProformaVente.findByReference";
                List<YvsComProformaVente> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumero();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_BP_NAME: {
                String[] ch = new String[]{"reference", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComptaBonProvisoire.findByReference";
                List<YvsComptaBonProvisoire> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumero();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_FiA_NAME: {
                String[] ch = new String[]{"reference", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComFicheApprovisionnement.findByReference";
                List<YvsComFicheApprovisionnement> l = dao.loadNameQueries(query, ch, v);
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
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComDocAchats.findByReference";
                List<YvsComDocAchats> l = dao.loadNameQueries(query, ch, v);
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
//                String[] ch = new String[]{"numDoc", "societe", "numDoc1"};
//                Object[] v = new Object[]{inter, societe, inter + "9999999999"};
//                String query = "YvsComDocVentes.findReferenceByReference";
//                motRefTable = (String) dao.loadObjectByNameQueries(query, ch, v);
//                if ((motRefTable == null)) {
//                    motRefTable = "";
//                }
//                break;
                return ReferenceService.nextValue(societe.getId(), inter, modele, dao);
            }
            case Constantes.TYPE_FT_NAME:
            case Constantes.TYPE_SS_NAME:
            case Constantes.TYPE_ES_NAME:
            case Constantes.TYPE_IN_NAME:
            case Constantes.TYPE_RE_NAME:
            case Constantes.TYPE_OT_NAME: {
                String[] ch = new String[]{"numDoc", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComDocStocks.findByReference";
                List<YvsComDocStocks> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumDoc();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_RS_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComReservationStock.findByReference";
                List<YvsComReservationStock> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumReference();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_RA_NAME: {
                String[] ch = new String[]{"numDoc", "agence"};
                Object[] v = new Object[]{inter + "%", currentAgence};
                String query = "YvsComDocRation.findByNumDocL";
                List<YvsComDocRation> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumDoc();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_OD_NAME: {
                String[] ch = new String[]{"numPiece", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComptaCaisseDocDivers.findByNumPieceLike_";
                String ref = (String) dao.loadObjectByNameQueries(query, ch, v);
                System.err.println("ref = " + ref);
                motRefTable = ref != null ? ref : "";
                break;
            }
            case Constantes.TYPE_OD_RECETTE_NAME: {
                String[] ch = new String[]{"numPiece", "societe", "mouvement"};
                Object[] v = new Object[]{inter + "%", societe, "R"};
                String query = "YvsComptaCaisseDocDivers.findByNumPieceLikeMouv";
                String ref = (String) dao.loadObjectByNameQueries(query, ch, v);
                motRefTable = ref != null ? ref : "";
                break;
            }
            case Constantes.TYPE_OD_DEPENSE_NAME: {
                String[] ch = new String[]{"numPiece", "societe", "mouvement"};
                Object[] v = new Object[]{inter + "%", societe, "D"};
                String query = "YvsComptaCaisseDocDivers.findByNumPieceLikeMouv";
                String ref = (String) dao.loadObjectByNameQueries(query, ch, v);
                motRefTable = ref != null ? ref : "";
                break;
            }
            case Constantes.TYPE_PT_NAME: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComptaCaissePieceVirement.findByNumeroPiece";
                List<YvsComptaCaissePieceVirement> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComptaMouvementCaisse.findByNumeroPiece";
                List<YvsComptaMouvementCaisse> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumero();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_ACHAT_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComptaCaissePieceAchat.findByNumPiece";
                List<YvsComptaCaissePieceAchat> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_VENTE_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComptaCaissePieceVente.findByNumPiece";
                List<YvsComptaCaissePieceVente> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_DIVERS_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComptaCaissePieceDivers.findByNumPiece";
                List<YvsComptaCaissePieceDivers> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_MISSION_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComptaCaissePieceMission.findByNumPiece";
                List<YvsComptaCaissePieceMission> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_SALAIRE_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComptaCaissePieceSalaire.findByNumPiece";
                List<YvsComptaCaissePieceSalaire> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PT_AVANCE_VENTE: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComptaAcompteClient.findByNumPiece";
                List<YvsComptaAcompteClient> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumRefrence();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PT_AVANCE_ACHAT: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComptaAcompteFournisseur.findByNumPiece";
                List<YvsComptaAcompteFournisseur> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumRefrence();
                } else {
                    motRefTable = "";
                }
                break;
            }

            case Constantes.TYPE_PIECE_COMPTABLE_NAME: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", societe};
                String query = "YvsComptaPiecesComptable.findByNumPiece";
                List<YvsComptaPiecesComptable> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_DOC_MISSION_NAME:
                String[] ch = new String[]{"numeroPiece", "agence"};
                Object[] v = new Object[]{inter + "%", currentAgence};
                String query = "YvsGrhMissions.findByNumPiece";
                List<YvsGrhMissions> lm = dao.loadNameQueries(query, ch, v);
                if ((lm != null) ? !lm.isEmpty() : false) {
                    motRefTable = lm.get(0).getNumeroMission();
                } else {
                    motRefTable = "";
                }
                break;
            case Constantes.MUT_TRANSACTIONS_MUT: {
                ch = new String[]{"numeroPiece", "mutuelle"};
                v = new Object[]{inter + "%", currentMutuel};
                query = "YvsMutOperationCompte.findByNumOp";
                List<YvsMutOperationCompte> lop = dao.loadNameQueries(query, ch, v);
                if ((lop != null) ? !lop.isEmpty() : false) {
                    motRefTable = lop.get(0).getReferenceOperation();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.MUT_ACTIVITE_CREDIT: {
                ch = new String[]{"numeroPiece", "mutuelle"};
                v = new Object[]{inter + "%", currentMutuel};
                query = "YvsMutCredit.findByNumOp";
                List<YvsMutCredit> lop = dao.loadNameQueries(query, ch, v);
                if ((lop != null) ? !lop.isEmpty() : false) {
                    motRefTable = lop.get(0).getReference();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.PROD_TYPE_PROD_NAME: {
                ch = new String[]{"codeRef", "societe"};
                v = new Object[]{inter + "%", societe};
                query = "YvsProdOrdreFabrication.findByReference";
                List<YvsProdOrdreFabrication> lop = dao.loadNameQueries(query, ch, v);
                if ((lop != null) ? !lop.isEmpty() : false) {
                    motRefTable = lop.get(0).getCodeRef();
                } else {
                    motRefTable = "";
                }
                break;
            }
            default: {
                break;
            }
        }
        if (motRefTable != null ? motRefTable.trim().isEmpty() : true) {
            motRefTable = inter;
        }
        String partieNum = motRefTable.replaceFirst(inter, "");
        if (partieNum != null ? partieNum.trim().length() > 0 : false) {
            int num = Integer.valueOf(partieNum.trim().replace("°", ""));
            if (Integer.toString(num + 1).length() > modele.getTaille()) {
                dao.setRESULT("Vous ne pouvez plus ajouter ce type de document");
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
        System.err.println("num = " + inter);
        return inter;
    }

    public YvsBaseModeleReference rechercheModeleReference(String mot, DaoInterfaceLocal dao) {
        return rechercheModeleReference(mot, currentScte, dao);
    }

    public YvsBaseModeleReference rechercheModeleReference(String mot, YvsSocietes societes, DaoInterfaceLocal dao) {
        if (!mot.toLowerCase().equals("")) {
            String[] ch = new String[]{"designation", "societe"};
            Object[] v = new Object[]{mot, societes};
            String query = "YvsBaseModeleReference.findByElement";
            List<YvsBaseModeleReference> l = dao.loadNameQueries(query, ch, v);
            if ((l != null) ? !l.isEmpty() : false) {
                return l.get(0);
            } else {
                if (mot.equals(Constantes.TYPE_OD_RECETTE_NAME) || mot.equals(Constantes.TYPE_OD_DEPENSE_NAME)) {
                    return rechercheModeleReference(Constantes.TYPE_OD_NAME, societes, dao);
                } else if (mot.equals(Constantes.TYPE_PC_ACHAT_NAME)
                        || mot.equals(Constantes.TYPE_PC_VENTE_NAME)
                        || mot.equals(Constantes.TYPE_PC_DIVERS_NAME)
                        || mot.equals(Constantes.TYPE_PC_MISSION_NAME)
                        || mot.equals(Constantes.TYPE_PC_SALAIRE_NAME)) {
                    return rechercheModeleReference(Constantes.TYPE_PC_NAME, societes, dao);
                }
            }
        }
        return null;
    }

//    public String getReferenceElement(YvsBaseModeleReference modele, Date date, long id, YvsUsersAgence currenUser, DaoInterfaceLocal dao, YvsAgences agences, YvsSocietes societes) {
//        currentAgence = agences;
//        currentScte = societes;
//        return getReferenceElement(modele, date, id, societes, dao, agences);
//    }
    public String genererPrefixeCodeElement(YvsBaseModeleReference modele, long id, DaoInterfaceLocal dao, YvsAgences agences) {
        currentAgence = agences;
        return genererPrefixeCodeElement(modele, id, dao);
    }

    public String genererPrefixeComplet(YvsBaseModeleReference modele, Date date, long id, DaoInterfaceLocal dao) {
        String prefixe = genererPrefixe(modele, id, dao);
        if (prefixe != null ? prefixe.trim().length() > 0 : false) {
            Calendar cal = Util.dateToCalendar(date);
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

    public String genererPrefixe(YvsBaseModeleReference modele, long id, DaoInterfaceLocal dao) {
        String inter = modele.getPrefix();
        String code = null;
        if (modele.getCodePoint()) {
            code = genererPrefixeCodeElement(modele, id, dao);
        }
        if (code != null ? code.trim().length() > 0 : false) {
            inter += modele.getSeparateur() + code;
        }
        inter += modele.getSeparateur();
        return inter != null ? inter : "";
    }

    public String genererPrefixeCodeElement(YvsBaseModeleReference modele, long id, DaoInterfaceLocal dao) {
        if (modele.getCodePoint()) {
            String code = "";
            switch (modele.getElementCode()) {
                case "SOCIETE": {
                    if (currentScte != null ? currentScte.getCodeAbreviation().trim().length() > 0 : false) {
                        code = currentScte.getCodeAbreviation();
                    }
                    break;
                }
                case "AGENCE": {
                    if (currentAgence != null ? currentAgence.getAbbreviation().trim().length() > 0 : false) {
                        code = currentAgence.getAbbreviation();
                    }
                    break;
                }
                case "AUTRE": {
                    switch (modele.getElement().getDesignation()) {
                        case Constantes.TYPE_FA_NAME:
                        case Constantes.TYPE_BLA_NAME:
                        case Constantes.TYPE_BLV_NAME:
                        case Constantes.PROD_TYPE_PROD_NAME:
                        case Constantes.TYPE_RA_NAME:
                        case Constantes.TYPE_RS_NAME:
                        case Constantes.TYPE_FiA_NAME:
                        case Constantes.TYPE_FT_NAME:
                        case Constantes.TYPE_SS_NAME:
                        case Constantes.TYPE_ES_NAME:
                        case Constantes.TYPE_IN_NAME:
                        case Constantes.TYPE_RE_NAME:
                        case Constantes.TYPE_OT_NAME:
                        case Constantes.TYPE_BRV_NAME:
                        case Constantes.TYPE_BAV_NAME:
                        case Constantes.TYPE_BCV_NAME:
                        case Constantes.TYPE_BRA_NAME:
                        case Constantes.TYPE_BAA_NAME:
                        case Constantes.TYPE_BCA_NAME:
                            YvsBaseDepots d = null;
                            if (id > 0) {
                                d = (YvsBaseDepots) dao.loadObjectByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (d != null ? d.getId() > 0 : false) {
                                code = d.getAbbreviation();
                            } else {
                                if (currentDepot != null ? currentDepot.getAbbreviation() != null : false) {
                                    code = currentDepot.getAbbreviation();
                                }
                            }
                            break;
                        case Constantes.TYPE_FV_NAME:
                            YvsBasePointVente p = null;
                            if (id > 0) {
                                p = (YvsBasePointVente) dao.loadObjectByNameQueries("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (p != null ? p.getId() > 0 : false) {
                                code = p.getCode();
                            } else {
                                if (currentPoint != null ? currentPoint.getCode() != null : false) {
                                    code = currentPoint.getCode();
                                }
                            }
                            break;
                        case Constantes.TYPE_OD_NAME:
                        case Constantes.TYPE_PC_NAME:
                        case Constantes.TYPE_PT_NAME:
                        case Constantes.TYPE_BP_NAME:
                        case Constantes.TYPE_DOC_MISSION_NAME:
                        case Constantes.TYPE_PT_AVANCE_ACHAT:
                        case Constantes.TYPE_PT_AVANCE_VENTE:
                            YvsBaseCaisse c = null;
                            if (id > 0) {
                                c = (YvsBaseCaisse) dao.loadObjectByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (c != null ? c.getId() > 0 : false) {
                                code = c.getCode();
                            }
                            break;
                        case Constantes.MUT_ACTIVITE_CREDIT:
                        case Constantes.MUT_TRANSACTIONS_MUT:
                            YvsMutCaisse en = null;
                            if (id > 0) {
                                en = (YvsMutCaisse) dao.loadObjectByNameQueries("YvsMutCaisse.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (en != null ? en.getId() > 0 : false) {
                                code = en.getReferenceCaisse();
                            }
                            break;
                        case Constantes.TYPE_PIECE_COMPTABLE_NAME:
                        case Constantes.TYPE_FRV_NAME:
                        case Constantes.TYPE_FAV_NAME:
                        case Constantes.TYPE_FRA_NAME:
                        case Constantes.TYPE_FAA_NAME:
                            YvsComptaJournaux j = null;
                            if (id > 0) {
                                j = (YvsComptaJournaux) dao.loadObjectByNameQueries("YvsComptaJournaux.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (j != null ? j.getId() > 0 : false) {
                                code = j.getCodeJournal();
                            }
                            break;
                    }
                    break;
                }
                default: {
                    break;
                }
            }
            if (code != null) {
                if (code.length() > modele.getLongueurCodePoint()) {
                    modele.setCode(code.substring(0, modele.getLongueurCodePoint()));
                } else {
                    modele.setCode(code);
                }
            }
        }
        return modele.getCode();
    }
    /*END REFERENCE*/

    /*BEGIN ACHAT*/
    public boolean validerFactureAchat(YvsComDocAchats current, DaoInterfaceLocal dao) {
        if (current == null) {
            return false;
        }
        if (current.getMontantNetApayer() < 0) {
            dao.setRESULT("Vous ne pouvez pas valider cette facture...car la somme des reglements est superieure au montant de la facture");
            return false;
        }
        for (YvsComContenuDocAchat c : current.getContenus()) {
            if (c.getPrixAchat() <= 0) {
                dao.setRESULT("Vous devez preciser le prix d'achat de l'article " + c.getArticle().getDesignation());
                return false;
            }
        }
        current.setLivraisonDo(true);
        if (Fonctions.this.changeStatutAchat(Constantes.ETAT_VALIDE, current, dao)) {
            current.setCloturer(false);
            current.setAnnulerBy(null);
            if (currentUser != null) {
                current.setValiderBy(currentUser.getUsers());
            }
            current.setDateAnnuler(null);
            current.setCloturerBy(null);
            current.setDateCloturer(null);
            current.setDateValider(new Date());
            current.setAuthor(currentUser);
            dao.update(current);

            for (YvsComContenuDocAchat c : current.getContenus()) {
                c.setAuthor(currentUser);
                c.setStatut(Constantes.ETAT_VALIDE);
                if (current.getContenus().contains(c)) {
                    current.getContenus().set(current.getContenus().indexOf(c), c);
                }
                dao.update(c);
            }

            return true;
        }
        return false;
    }

    public boolean changeStatutAchat(String etat, YvsComDocAchats current, DaoInterfaceLocal dao) {
        if (!etat.equals("")) {
            String rq = "UPDATE yvs_com_doc_achats SET statut = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(current.getId(), 1)};
            dao.requeteLibre(rq, param);
            current.setStatut(etat);
            return true;
        }
        return false;
    }
    /*END ACHAT*/

    /*BEGIN VENTE*/
    private boolean controlContentForTransmis(YvsComContenuDocVente c, YvsBaseDepots depot, Date dateLivraison, String statut, DaoInterfaceLocal dao) {
        champ = new String[]{"article", "depot"};
        val = new Object[]{c.getArticle(), depot};
        YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
        if (y != null ? y.getId() < 1 : true) {
            dao.setRESULT("Impossible d'effectuer cette action... Car le depot " + depot.getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation());
            return false;
        }
        if (statut.equals(Constantes.ETAT_VALIDE)) {
            double s = dao.stocksReel(c.getArticle().getId(), 0, depot.getId(), 0, 0, dateLivraison, c.getConditionnement().getId(), (c.getLot() != null ? c.getLot().getId() : 0));
            if (s < c.getQuantite()) {
                if (c.getArticle().getMethodeVal() != null) {
                    if (!c.getArticle().getMethodeVal().equals("CMPII")) {
                        dao.setRESULT("Impossible d'effectuer cette action... Car l'article " + c.getArticle().getDesignation() + " n'a plus un stock suffisant dans le depot " + depot.getDesignation());
                        return false;
                    }
                } else {
                    dao.setRESULT("Impossible d'effectuer cette action... Car l'article " + c.getArticle().getDesignation() + " n'a plus un stock suffisant  dans le depot" + depot.getDesignation());
                    dao.setRESULT("L'article " + c.getArticle().getDesignation() + " n'a aucune méthode de valorisation configuré");
                    return false;
                }
                dao.setRESULT("Stock négatif pour cette livraison sur l'article " + c.getArticle().getDesignation());
            }
        }
        return true;
    }

    public boolean validerFactureVente(YvsComDocVentes current, DaoInterfaceLocal dao) {
        try {
            if (current == null) {
                return false;
            }
            if (livrerFactureVente(current, dao)) {
                if (changeStatutVente(Constantes.ETAT_VALIDE, current, dao)) {
                    current.setCloturer(false);
                    current.setAnnulerBy(null);
                    current.setDateAnnuler(null);
                    current.setDateCloturer(null);
                    current.setDateValider(current.getDateLivraison());
                    current.setDateLivraison(current.getDateLivraison());
                    current.setDateUpdate(new Date());
                    if (currentUser != null ? currentUser.getId() > 0 : false) {
                        current.setAuthor(currentUser);
                        current.setValiderBy(currentUser.getUsers());
                    }
                    current.setStatut(Constantes.ETAT_VALIDE);
                    YvsComDocVentes y = new YvsComDocVentes(current);
                    y.getContenus().clear();
                    dao.update(y);
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return false;
    }

    public boolean validerFactureVente(YvsComDocVentes current, YvsSocietes currentScte, DaoInterfaceLocal dao) {
        try {
            // Générère l'echéancier prévu de règlement
            dao.generatedEcheancierReg(current, false, currentUser, currentScte);
            if (changeStatutVente(Constantes.ETAT_VALIDE, current, dao)) {
                current.setCloturer(false);
                current.setAnnulerBy(null);
                current.setCloturerBy(null);
                current.setDateAnnuler(null);
                current.setDateCloturer(null);
                current.setDateValider(new Date());
                current.setDateUpdate(new Date());
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    current.setValiderBy(currentUser.getUsers());
                    current.setAuthor(currentUser);
                }
                dao.update(current);
                //valider aussi les BL liés
                List<YvsComDocVentes> lv = dao.loadNameQueries("YvsComDocVentes.findByParentTypeDoc", new String[]{"documentLie", "typeDoc"}, new Object[]{current, Constantes.TYPE_BLV});
                if (current.getLivraisonAuto()) {
                    if (lv.isEmpty()) {
                        boolean succes = transmisOrder(current, current.getDateLivraisonPrevu(), Constantes.ETAT_VALIDE, dao);
                        if (succes) {
                            current.setStatutLivre(Constantes.ETAT_LIVRE);
                            current.setConsigner(false);
                            current.setDateConsigner(null);
                        }
                    }
                    for (YvsComDocVentes dLiv : lv) {
                        if (!dLiv.getStatut().equals(Constantes.ETAT_ANNULE) && !dLiv.getStatut().equals(Constantes.ETAT_VALIDE)) {
                            dLiv.setLivreur(current.getEnteteDoc().getCreneau().getUsers());
                            dLiv.setDocumentLie(current);
                            validerFactureVente(dLiv, dao);
                        }
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            dao.setRESULT("Exception : " + ex.getMessage());
            ex.getStackTrace();
        }
        return false;
    }

    public boolean changeStatutVente(String etat, YvsComDocVentes current, DaoInterfaceLocal dao) {
        if (!etat.equals("")) {
            String rq = "UPDATE yvs_com_doc_ventes SET statut = '" + etat + "'  WHERE id=?";
            Options[] param = new Options[]{new Options(current.getId(), 1)};
            dao.requeteLibre(rq, param);
            current.setStatut(etat);
            return true;
        }
        return false;
    }

    public boolean livrerFactureVente(YvsComDocVentes current, DaoInterfaceLocal dao) {
        try {
            if (current == null) {
                return false;
            }
            if (current.getDepotLivrer() != null ? current.getDepotLivrer().getId() < 1 : true) {
                dao.setRESULT("Vous devez specifier le dépot de livraison");
                return false;
            }
            if (current.getTrancheLivrer() != null ? current.getTrancheLivrer().getId() < 1 : true) {
                dao.setRESULT("Vous devez specifier la tranche de livraison");
                return false;
            }
            if (current.getLivreur() != null ? current.getLivreur().getId() < 1 : true) {
                dao.setRESULT("Vous devez specifier le livreur");
                return false;
            }
            YvsComDocVentes d = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{current.getDocumentLie().getId()});
            if (d.getEnteteDoc() != null ? (d.getEnteteDoc().getCreneau() != null ? (d.getEnteteDoc().getCreneau().getCreneauPoint() != null ? (d.getEnteteDoc().getCreneau().getCreneauPoint().getPoint() != null) : false) : false) : false) {
                switch (d.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getLivraisonOn()) {
                    case 'R': {
                        if (!d.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                            dao.setRESULT("Cette facture doit etre reglée avant de pouvoir générer une livraison");
                            return false;
                        }
                    }
                    case 'V': {
                        if (!d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                            dao.setRESULT("Cette facture doit etre validée avant de pouvoir générer une livraison");
                            return false;
                        }
                    }
                }
            }
            if (current.getContenus() != null ? !current.getContenus().isEmpty() : false) {
                for (YvsComContenuDocVente c : current.getContenus()) {
                    //controle les quantités déjà livré
                    Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", new String[]{"docVente", "statut", "typeDoc", "article", "unite"}, new Object[]{c.getDocVente().getDocumentLie(), Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, c.getArticle(), c.getConditionnement()});
                    qteLivre = (qteLivre != null) ? qteLivre : 0;
                    //trouve la quantité d'article facturé 
                    Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByArticle", new String[]{"docVente", "article", "unite"}, new Object[]{c.getDocVente().getDocumentLie(), c.getArticle(), c.getConditionnement()});
                    qteFacture = (qteFacture != null) ? qteFacture : 0;
                    //trouve la quantité d'article facturé 
                    Double qteBonusFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteBonusByFacture", new String[]{"docVente", "article", "unite"}, new Object[]{c.getDocVente().getDocumentLie(), c.getArticle(), c.getConditionnement()});
                    qteBonusFacture = (qteBonusFacture != null) ? qteBonusFacture : 0;
                    if (c.getDocVente().getDocumentLie() != null ? !c.getDocVente().getDocumentLie().getStatutRegle().equals(Constantes.ETAT_REGLE) : true) {
                        //si la facture n'est pas encore réglé, on ne dois pas inclure la quantité bonus dans la quantité à livrerFactureVente
                        if (c.getQuantite() > (qteFacture - qteLivre)) {
                            dao.setRESULT("Vous ne pouvez livrer l'article " + c.getArticle().getRefArt() + " au delà de la quantité facturée !");
                            return false;
                        }
                    } else {
                        if (c.getQuantite() > ((qteFacture + qteBonusFacture) - qteLivre)) {
                            dao.setRESULT("Vous ne pouvez livrer l'article " + c.getArticle().getRefArt() + " au delà de la quantité facturée !");
                            return false;
                        }
                    }
                    double s = dao.stocksReel(c.getArticle().getId(), 0, current.getDepotLivrer().getId(), 0, 0, current.getDateLivraison(), (c.getConditionnement() != null) ? c.getConditionnement().getId() : -1, (c.getLot() != null) ? c.getLot().getId() : 0);
                    if (s < c.getQuantite()) {
                        if (c.getArticle().getMethodeVal() != null) {
                            if (!c.getArticle().getMethodeVal().equals("CMPII")) {
                                dao.setRESULT("Impossible d'effectuer cette action... Car l'article " + c.getArticle().getDesignation() + " n'a plus un stock suffisant");
                                return false;
                            }
                        } else {
                            dao.setRESULT("Impossible d'effectuer cette action... Car l'article " + c.getArticle().getDesignation() + " n'a plus un stock suffisant");
                            dao.setRESULT("L'article " + c.getArticle().getDesignation() + " n'a aucune méthode de valorisation configuré");
                            return false;
                        }
                        dao.setRESULT("Stock négatif pour cette livraison sur l'article " + c.getArticle().getDesignation());
                    }
                    champ = new String[]{"article", "depot"};
                    val = new Object[]{c.getArticle(), new YvsBaseDepots(current.getDepotLivrer().getId())};
                    YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
                    if (y != null ? y.getId() < 1 : true) {
                        dao.setRESULT("Impossible d'effectuer cette action... Car le depot " + current.getDepotLivrer().getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation());
                        return false;
                    }
                }
                current.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
                current.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
                current.setDateLivraison(current.getStatutLivre().equals(String.valueOf(Constantes.STATUT_DOC_LIVRER)) ? new Date() : null);
                return true;
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return false;
    }

    public void transmisOrder(YvsComDocVentes current, DaoInterfaceLocal dao) {
        transmisOrder(current, new Date(), "V", dao);
    }

    public boolean transmisOrder(YvsComDocVentes facture, Date dateLivraison, String statut, DaoInterfaceLocal dao) {
        try {
            if (facture == null) {
                dao.setRESULT("Vous devez selectionner la facture");
                return false;
            }
            if (facture.getEnteteDoc() != null ? (facture.getEnteteDoc().getCreneau() != null ? (facture.getEnteteDoc().getCreneau().getCreneauPoint() != null ? (facture.getEnteteDoc().getCreneau().getCreneauPoint().getPoint() != null) : false) : false) : false) {
                switch (facture.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getLivraisonOn()) {
                    case 'R': {
                        if (!facture.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                            dao.setRESULT("Cette facture doit etre reglée avant de pouvoir générer une livraison");
                            return false;
                        }
                    }
                    case 'V': {
                        if (!facture.getStatut().equals(Constantes.ETAT_VALIDE)) {
                            dao.setRESULT("Cette facture doit etre validée avant de pouvoir générer une livraison");
                            return false;
                        }
                    }
                }
            }
            if (facture.getDepotLivrer() != null ? facture.getDepotLivrer().getId() < 1 : true) {
                dao.setRESULT("Aucun dépôt de livraison n'a été trouvé !");
                return false;
            }
            if (facture.getTrancheLivrer() != null ? facture.getTrancheLivrer().getId() < 1 : true) {
                dao.setRESULT("Aucune tranche de livraison n'a été trouvé !");
                return false;
            }
            if (dateLivraison != null ? dateLivraison.after(new Date()) : true) {
                dao.setRESULT("La date de livraison est incorrecte !");
                return false;
            }
            String num = dao.genererReference(Constantes.TYPE_BLV_NAME, dateLivraison, facture.getDepotLivrer().getId(), currentScte, currentAgence);
            if (num != null ? num.trim().length() > 0 : false) {
                List<YvsComContenuDocVente> l = loadContenusStay(facture, Constantes.TYPE_BLV, dao);
                if (l != null ? !l.isEmpty() : false) {
                    List<YvsBaseDepots> depotsLivraison = new ArrayList<>();
                    List<YvsComContenuDocVente> list = new ArrayList<>();
                    YvsBaseDepots depot;
                    for (YvsComContenuDocVente c : l) {
                        depot = facture.getDepotLivrer();
                        if (c.getDepoLivraisonPrevu() != null ? (c.getDepoLivraisonPrevu().getId() != null ? c.getDepoLivraisonPrevu().getId() > 0 : false) : false) {
                            depot = c.getDepoLivraisonPrevu();
                        }
                        if (!depotsLivraison.contains(depot)) {
                            depotsLivraison.add(depot);
                        }
                        if (!controlContentForTransmis(c, depot, dateLivraison, statut, dao)) {
                            return false;
                        }
                        list.add(c);
                        if (c.getQuantiteBonus() > 0) {
                            YvsComContenuDocVente a = new YvsComContenuDocVente(c);
                            a.setArticle(new YvsBaseArticles(c.getArticleBonus().getId(), c.getArticleBonus().getRefArt(), c.getArticleBonus().getDesignation()));
                            a.setConditionnement(new YvsBaseConditionnement(c.getConditionnementBonus().getId(), new YvsBaseUniteMesure(c.getConditionnementBonus().getUnite().getId(), c.getConditionnementBonus().getUnite().getReference(), c.getConditionnementBonus().getUnite().getLibelle())));
                            a.setQuantite(c.getQuantiteBonus());

                            a.setArticleBonus(null);
                            a.setConditionnementBonus(null);
                            a.setQuantiteBonus(0.0);
                            a.setPrix(0.0);
                            a.setPrixTotal(0.0);
                            a.setComission(0.0);
                            a.setRabais(0.0);
                            a.setRemise(0.0);
                            a.setRistourne(0.0);
                            a.setTaxe(0.0);

                            if (!controlContentForTransmis(a, depot, dateLivraison, statut, dao)) {
                                return false;
                            }
                            list.add(a);
                        }
                    }
                    if (facture.getEnteteDoc() != null) {
                        YvsComDocVentes y;
                        for (YvsBaseDepots d : depotsLivraison) {
                            y = new YvsComDocVentes(facture);
                            y.setEnteteDoc(facture.getEnteteDoc());
                            y.setDateSave(new Date());
                            y.setAuthor(currentUser);
                            y.setTypeDoc(Constantes.TYPE_BLV);
                            y.setNumDoc(num);
                            y.setNumPiece("BL N° " + facture.getNumDoc());
                            y.setDepotLivrer(d);
                            y.setTrancheLivrer(facture.getTrancheLivrer());
                            if (currentUser != null) {
                                y.setLivreur(currentUser.getUsers());
                            }
                            y.setDateLivraison(dateLivraison);
                            y.setDateLivraisonPrevu(facture.getDateLivraisonPrevu());
                            y.setDocumentLie(new YvsComDocVentes(facture.getId()));
                            y.setHeureDoc(new Date());
                            y.setStatut(statut);
                            y.setStatutLivre(statut.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE);
                            y.setStatutRegle(Constantes.ETAT_ATTENTE);
                            if (currentUser != null) {
                                y.setValiderBy(currentUser.getUsers());
                            }
                            y.setDateValider(facture.getDateLivraisonPrevu());
                            y.setDateSave(new Date());
                            y.setDateUpdate(new Date());
                            y.setDescription("Livraison de la facture N° " + facture.getNumDoc() + " le " + ldf.format(dateLivraison) + " à " + time.format(dateLivraison));
                            y = (YvsComDocVentes) dao.save1(y);
                            for (YvsComContenuDocVente c : list) {
                                if (c.getDepoLivraisonPrevu() != null ? (c.getDepoLivraisonPrevu().getId() != null ? c.getDepoLivraisonPrevu().getId() > 0 : false) : false) {
                                    if (c.getDepoLivraisonPrevu().equals(d)) {
                                        c.setDocVente(y);
                                        c.setStatut(Constantes.ETAT_VALIDE);
                                        c.setAuthor(currentUser);
                                        c.setParent(new YvsComContenuDocVente(c.getId()));
                                        dao.save(c);
                                    }
                                } else if (facture.getDepotLivrer().equals(d)) {
                                    c.setDocVente(y);
                                    c.setStatut(Constantes.ETAT_VALIDE);
                                    c.setAuthor(currentUser);
                                    c.setParent(new YvsComContenuDocVente(c.getId()));
                                    dao.save(c);
                                }
                            }
                            facture.getDocuments().add(y);
                        }
                        String rq = "UPDATE yvs_com_doc_ventes SET statut_livre = ? WHERE id=?";
                        Options[] param = new Options[]{new Options(statut.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE, 1), new Options(facture.getId(), 2)};
                        dao.requeteLibre(rq, param);
                        if (statut.equals(Constantes.ETAT_VALIDE)) {
                            rq = "UPDATE yvs_com_reservation_stock SET statut = 'L' WHERE id IN (SELECT id_reservation FROM yvs_com_contenu_doc_vente WHERE doc_vente = ?)";
                            param = new Options[]{new Options(facture.getId(), 1)};
                            dao.requeteLibre(rq, param);
                        }
                        facture.setConsigner(statut.equals(Constantes.ETAT_VALIDE) ? false : facture.getConsigner());
                        facture.setDateConsigner(statut.equals(Constantes.ETAT_VALIDE) ? null : facture.getDateConsigner());
                        facture.setStatutLivre(statut.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE);
                        if (statut.equals(Constantes.ETAT_VALIDE)) {
                            for (YvsComContenuDocVente c : facture.getContenus()) {
                                if (c.getIdReservation() != null ? c.getIdReservation().getId() != null ? c.getIdReservation().getId() > 0 : false : false) {
                                    long id = c.getIdReservation().getId();
                                    c.setIdReservation(null);
                                    rq = "UPDATE yvs_com_contenu_doc_vente SET id_reservation = null WHERE id = ?";
                                    param = new Options[]{new Options(c.getId(), 1)};
                                    dao.requeteLibre(rq, param);
                                    rq = "DELETE FROM yvs_com_reservation_stock WHERE id = ? AND id NOT IN (SELECT id_reservation FROM yvs_com_contenu_doc_vente WHERE id_reservation IS NOT NULL)";
                                    param = new Options[]{new Options(id, 1)};
                                    dao.requeteLibre(rq, param);
                                }
                            }
                        }
                        return true;
                    }
                } else {
                    if (!facture.getContenus().isEmpty()) {
                        String rq = "UPDATE yvs_com_doc_ventes SET statut_livre = 'L' WHERE id=?";
                        Options[] param = new Options[]{new Options(facture.getId(), 1)};
                        dao.requeteLibre(rq, param);
                        rq = "UPDATE yvs_com_doc_ventes SET statut_livre = 'L' WHERE document_lie=?";
                        param = new Options[]{new Options(facture.getId(), 1)};
                        dao.requeteLibre(rq, param);
                        facture.setConsigner(false);
                        facture.setDateConsigner(null);
                        facture.setStatutLivre(Constantes.ETAT_LIVRE);

                        return true;
                    } else {
                        dao.setRESULT("Vous ne pouvez pas livrer cette facture car elle est vide");
                    }
                }
            }
        } catch (Exception ex) {
            ex.getStackTrace();
            dao.setRESULT("Exception : " + ex.getMessage());
        }
        return false;
    }

    public YvsComMensualiteFactureVente definedAvance(List<YvsComMensualiteFactureVente> list, Double a, double montant) {
        YvsComMensualiteFactureVente e = new YvsComMensualiteFactureVente(Long.valueOf(-(list.size() + 1)));
        try {
            boolean deja = false;
            double avance = (a != null ? a : 0);
            for (YvsComMensualiteFactureVente v : list) {
                if (!v.getId().equals(e.getId())) {
                    if (!deja) {
                        avance -= v.getMontant();
                    }
                } else {
                    deja = true;
                }
            }
            if (avance <= 0) {
                e.setAvance(0.0);
                e.setEtat(Constantes.ETAT_ATTENTE);
            } else if (avance < montant) {
                e.setAvance(avance);
                e.setEtat(Constantes.ETAT_ENCOURS);
            } else {
                e.setAvance(montant);
                e.setEtat(Constantes.ETAT_REGLE);
            }
            e.setMontant(montant);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return e;
    }

    private List<YvsComContenuDocVente> loadContenusStay(YvsComDocVentes y, String type, DaoInterfaceLocal dao) {
        List<YvsComContenuDocVente> l = new ArrayList<>();
        try {
            y.setInt_(false);
            nameQueri = "YvsComContenuDocVente.findByDocVente";
            champ = new String[]{"docVente"};
            val = new Object[]{y};
            List<YvsComContenuDocVente> contenus = dao.loadNameQueries(nameQueri, champ, val);
            for (YvsComContenuDocVente c : contenus) {
                boolean deja = false;
                for (YvsComContenuDocVente c_ : l) {
                    if (c_.getArticle().equals(c.getArticle()) && c.getConditionnement().equals(c_.getConditionnement())) {
                        deja = true;
                        break;
                    }
                }
                if (!deja) {
                    champ = new String[]{"article", "unite", "docVente"};
                    val = new Object[]{c.getArticle(), c.getConditionnement(), new YvsComDocVentes(y.getId())};
                    Double qte = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByArticle", champ, val);
                    Double rem = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findRemByArticle", champ, val);
                    Double tax = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findTaxeByArticle", champ, val);
                    Double pt = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findPTByArticle", champ, val);
                    double prix = 0;
                    if (qte != null) {
                        prix = ((pt != null ? pt : 0) - (tax != null ? tax : 0) + (rem != null ? rem : 0)) / qte;
                    }

                    String[] ch = new String[]{"docVente", "typeDoc", "statut", "article", "unite"};
                    Object[] v = new Object[]{y, type, Constantes.ETAT_VALIDE, c.getArticle(), c.getConditionnement()};
                    Double s = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", ch, v);
                    if ((qte != null ? qte : 0) > (s != null ? s : 0)) {
                        c.setQuantite_((qte != null ? qte : 0));
                        c.setQuantite((qte != null ? qte : 0) - (s != null ? s : 0));
                        c.setTaxe((tax != null ? tax : 0));
                        c.setRemise((rem != null ? rem : 0));
                        c.setPrix(prix);
                        c.setParent(new YvsComContenuDocVente(c.getId()));
                        l.add(c);
                    }
                }
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return l;
    }

    public void valideReglementFacture(YvsComDocVentes doc, YvsComptaCaissePieceVente pieceVente, boolean msg, DaoInterfaceLocal dao) {
        try {
            YvsGrhElementAdditionel service = new YvsGrhElementAdditionel();
            if (service != null) {
                if ((service.getPlanPrelevement().getId()) <= 0 || service.getTypeElement().getId() <= 0) {
                    dao.setRESULT("Formulaire incorrecte !");
                    return;
                }
                //enretistre la retenu
                YvsGrhElementAdditionel ela = service;
                if (ela != null) {
                    ela.setId(null);
                    ela.setPiceReglement(pieceVente);
                    ela.setPlanifier(true);
                    ela.setContrat(service.getContrat());
                    ela.setPlanPrelevement(new YvsGrhPlanPrelevement(service.getPlanPrelevement().getId()));
                    ela.setTypeElement(new YvsGrhTypeElementAdditionel(service.getTypeElement().getId()));
                    ela.setAuthor(currentUser);
                    ela.setPermanent(false);
                    ela.setDateUpdate(new Date());
                    ela = (YvsGrhElementAdditionel) dao.save1(ela);
                    for (YvsGrhDetailPrelevementEmps d : service.getRetenues()) {
                        d.setAuthor(currentUser);
                        d.setId(null);
                        d.setRetenue(ela);
                        d.setRetenuFixe(false);
                        d = (YvsGrhDetailPrelevementEmps) dao.save1(d);
                    }
                    //marque la pièce réglé
                    pieceVente.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                    if (currentUser != null) {
                        pieceVente.setCaissier(currentUser.getUsers());
                    }
                    pieceVente.setDatePaiement(new Date());
                    pieceVente.setCaisse(null);
                    dao.update(pieceVente);
                    if (pieceVente.getVente() != null) {
                        dao.getEquilibreVente(pieceVente.getVente().getId());
                        if (currentUser != null) {
                            dao.setMontantTotalDoc(doc, currentUser.getAgence().getSociete().getId());
                        }
                    }
                }

            } else {
                dao.setRESULT("Retenue non enregistré !");
            }

        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    public List<YvsComptaCaissePieceVente> generetedPiecesFromModel(YvsBaseModelReglement model, YvsComDocVentes d, YvsBaseCaisse caisse, YvsUsersAgence current, DaoInterfaceLocal dao) {
        List<YvsComptaCaissePieceVente> re = new ArrayList<>();
        try {
            List<YvsBaseTrancheReglement> lt = dao.loadNameQueries("YvsBaseTrancheReglement.findByModel", new String[]{"model"}, new Object[]{model});
            if (d.getMontantResteAPlanifier() > 0) {
                long id = -1000;
                YvsComptaCaissePieceVente piece;
                Calendar cal = Calendar.getInstance();
                cal.setTime(d.getEnteteDoc().getDateEntete());  //date de la facturation
                double totalTaux = 0, sommeMontant = 0;
                YvsBaseModeReglement espece = modeEspece(current, dao), mode = null;
                YvsBaseTrancheReglement trch;
                if (lt != null ? !lt.isEmpty() : false) {
                    for (int i = 0; i < lt.size() - 1; i++) {
                        trch = lt.get(i);
                        cal.add(Calendar.DAY_OF_MONTH, trch.getIntervalJour());
                        double montant = arrondi(d.getMontantPlanifier() * trch.getTaux() / 100, dao);
                        sommeMontant += montant;
                        if (trch.getMode() != null ? trch.getMode().getId() != null ? trch.getMode().getId() > 0 : false : false) {
                            mode = trch.getMode();
                        } else {
                            mode = espece;
                        }
                        piece = buildPieceFromModel(id++, mode, d, caisse, cal.getTime(), montant, (i + 1), dao);
                        re.add(piece);
                        totalTaux += trch.getTaux();
                    }

                    trch = lt.get(lt.size() - 1);
                    if (trch.getMode() != null ? trch.getMode().getId() != null ? trch.getMode().getId() > 0 : false : false) {
                        mode = trch.getMode();
                    } else {
                        mode = espece;
                    }
                    totalTaux += trch.getTaux();
                    cal.add(Calendar.DAY_OF_MONTH, trch.getIntervalJour());
                    if (totalTaux > 100) {
                        dao.setRESULT("Les tranches du model de règlement sont supérieures à 100% !");
                    } else if (totalTaux < 100) {
                        dao.setRESULT("Les tranches du model de règlement sont inférieure à 100% !");
                    }
                }
                if (mode != null ? mode.getId() != null ? mode.getId() < 1 : true : true) {
                    mode = espece;
                }
                double montant = d.getMontantResteAPlanifier() - sommeMontant;
                piece = buildPieceFromModel(id++, mode, d, caisse, cal.getTime(), montant, (lt != null ? lt.size() : 1), dao);
                re.add(piece);
            } else {
                dao.setRESULT("Le montant du document n'a pas été trouvé !");
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return re;
    }

    private YvsComptaCaissePieceVente buildPieceFromModel(long id, YvsBaseModeReglement mode, YvsComDocVentes d, YvsBaseCaisse caisse, Date date, double montant, int numero, DaoInterfaceLocal dao) {
        YvsComptaCaissePieceVente piece = new YvsComptaCaissePieceVente(id);
        try {
            piece.setAuthor(currentUser);
            piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            piece.setMontant(montant);
            piece.setDatePaimentPrevu(date);
            piece.setDatePiece(new Date());
            String ref = dao.genererReference(Constantes.TYPE_PC_NAME, piece.getDatePiece(), piece.getCaisse().getId(), currentUser.getAgence().getSociete(), currentUser.getAgence());
            if (ref != null ? ref.trim().length() < 1 : true) {
                return null;
            }
            piece.setNumeroPiece(ref);
            piece.setVente(d);
            piece.setModel(mode);
//        piece.setClient(new YvsComClient(d.getClient().getId()));
            if ((caisse != null) ? caisse.getId() > 0 : false) {
                piece.setCaisse(caisse);
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return piece;
    }
    /*END VENTE*/

    /*BEGIN DOC DIVERS*/
    public double giveSoePlanifie(List<YvsComptaCaissePieceDivers> lp) {
        double re = 0;
        if (lp != null) {
            for (YvsComptaCaissePieceDivers c : lp) {
                if (c.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU && c.getStatutPiece() != Constantes.STATUT_DOC_ANNULE) {
                    re += c.getMontant();
                }
            }
        }
        return re;
    }

    public void verifyToJustify(YvsComptaBonProvisoire y, DaoInterfaceLocal dao) {
        if (y != null ? y.getId() > 0 : false) {
            double total = 0;
            List<YvsComptaCaissePieceDivers> list = dao.loadNameQueries("YvsComptaJustificatifBon.findPieceBybon", new String[]{"bon"}, new Object[]{y});
            for (YvsComptaCaissePieceDivers c : list) {
                total += c.getMontant();
            }
            if (y.getMontant() <= total) {
                y.setStatutJustify(Constantes.ETAT_JUSTIFIE);
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                dao.update(y);
            }
        }
    }

    public boolean controleValeurMensualite(YvsComptaCaissePieceDivers bean, YvsComptaCaisseDocDivers docDivers, DaoInterfaceLocal dao) {
        if ((bean.getDocDivers() != null) ? ((bean.getDocDivers().getId() != null) ? bean.getDocDivers().getId() <= 0 : false) : true) {
            dao.setRESULT("Vous devez Enregistrer la pièce source");
            return false;
        }
        if (bean.getMontant() < 1) {
            dao.setRESULT("Vous devez entrer un montant");
            return false;
        }
        double mtant = 0;
        for (YvsComptaCaissePieceDivers m : docDivers.getReglements()) {
            if (m.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU) {
                mtant += m.getMontant();
            }
        }
        if (mtant > docDivers.getMontantTotal()) {
            dao.setRESULT("La somme des montants des mensualités doit être égale au montant du document!");
            return false;
        }
        return (giveExerciceActif(bean.getDatePiece(), dao) != null);
    }

    public boolean validePieceDivers(YvsComptaCaissePieceDivers y, YvsComptaCaisseDocDivers current, DaoInterfaceLocal dao) {
        if (controleValeurMensualite(y, current, dao)) {
            //le document divers doit être valide
            if (current.getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                if (y.getCaisse() != null ? y.getCaisse().getId() < 1 : true) {
                    dao.setRESULT("Vous devez preciser la caisse");
                    return false;
                }
                if (y.getJustify() != null ? (y.getJustify().getId() > 0 ? y.getJustify().getBon() != null : false) : false) {
                    if (!y.getJustify().getBon().getStatutPaiement().equals(Constantes.STATUT_DOC_PAYER)) {
                        dao.setRESULT("Le bon provisoire rattaché n'est pas encore payé");
                        return false;
                    }
                }
                y.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                if (currentUser != null) {
                    y.setValiderBy(currentUser.getUsers());
                }
                y.setDateUpdate(new Date());
                y.setDateValider(new Date());
                y.setDateAnnuler(null);
                y.setAnnulerBy(null);
                if (y.getModePaiement() == null) {
                    y.setModePaiement(modeEspece(currentUser, dao));
                }
                dao.update(y);
                current.setTotalPlanifie(giveSoePlanifie(current.getReglements()));
                YvsComptaParametre currentParam = (YvsComptaParametre) dao.loadObjectByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentScte});
                if (currentParam != null ? currentParam.getMajComptaAutoDivers() : false) {
                    boolean maj = false;
                    if (currentParam.getMajComptaStatutDivers().equals(Constantes.STATUT_DOC_VALIDE)) {
                        maj = true;
                    } else if (currentParam.getMajComptaStatutDivers().equals(Constantes.STATUT_DOC_ENCOUR) && current.getTotalPlanifie() > 0) {
                        maj = true;
                    } else if (currentParam.getMajComptaStatutDivers().equals(Constantes.STATUT_DOC_PAYER) && current.getTotalPlanifie() >= current.getMontantTotal()) {
                        maj = true;
                    }
                    if (maj) {
                        comptabiliserCaisseDivers(y, dao);
                    }
                }
                if (y.getJustify() != null ? y.getJustify().getId() > 0 : false) {
                    verifyToJustify(y.getJustify().getBon(), dao);
                }
                return true;
            } else {
                dao.setRESULT("Le document n'a pas encore été validé ");
            }
        }
        return false;
    }
    /*END DOC DIVERS*/

    /*BEGIN CONGE*/
    public void applyDureeConge(YvsGrhCongeEmps conges, YvsBaseExercice exo, Date date, DaoInterfaceLocal dao) {
        if (conges.getEmploye().getId() > 0 && date != null) {
            exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findById", new String[]{"id"}, new Object[]{exo != null ? exo.getId() : 0});
            Options[] param = new Options[]{new Options(currentScte.getId(), 1), new Options(0, 2), new Options(0, 3), new Options(conges.getEmploye().getId(), 4), new Options((exo != null) ? exo.getDateDebut() : new Date(), 5), new Options(date, 6)};
            List<Object[]> durees = dao.loadListBySqlQuery("select agence, employe, valeur, element from grh_conges_employes(?, ?, ?, ?, ?, ?) order by element", param);
            for (Object[] data : durees) {
                if (data != null ? data.length > 0 : false) {
                    long agence = data.length > 0 ? (data[0] != null ? (long) data[0] : 0) : 0;
                    long employe = data.length > 1 ? (data[1] != null ? (long) data[1] : 0) : 0;
                    double valeur = data.length > 2 ? (data[2] != null ? (double) data[2] : 0) : 0;
                    String element = data.length > 3 ? (data[3] != null ? (String) data[3] : "") : "";
                    if (employe == conges.getEmploye().getId()) {
                        switch (element) {
                            case Constantes.CONGE_PRINCIPAL:
                                conges.setCongePrincipalDu((int) valeur);
                                break;
                            case Constantes.CONGE_ANNUEL_PRIS:
                                conges.setCongePrincipalPris((int) valeur);
                                break;
                            case Constantes.CONGE_SUPPLEMENTAIRE:
                                conges.setCongeSupp((int) valeur);
                                break;
                            case Constantes.PERMISSION_DU:
                                conges.setDureePermissionAutorise((int) valeur);
                                break;
                            case Constantes.PERMISSION_LONG_AUTORISE:
                                conges.setDureePermissionAutorisePris((int) valeur);
                                break;
                            case Constantes.PERMISSION_LONG_SPECIAL:
                                conges.setDureePermPrisSpeciale((int) valeur);
                                break;
                            case Constantes.PERMISSION_COURT_ANNUEL:
                                conges.setDureePermCD_AN((int) valeur);
                                break;
                            case Constantes.PERMISSION_COURT_AUTORISE:
                                conges.setDureePermCD_AU((int) valeur);
                                break;
                            case Constantes.PERMISSION_COURT_SALAIRE:
                                conges.setDureePermCD_SAL((int) valeur);
                                break;
                            case Constantes.PERMISSION_COURT_SPECIAL:
                                conges.setDureePermCD_SP((int) valeur);
                                break;
                        }
                    }
                }
            }
            conges.setDureePermCD(conges.getDureePermCD_AN() + conges.getDureePermCD_AU() + conges.getDureePermCD_SAL() + conges.getDureePermCD_SP());
        } else {
            //System.err.println("Aucun employé n'a été trouvé !");
        }
    }
    /*END CONGE*/

    /*BEGIN MISSION*/
    private boolean controlePiece(YvsComptaCaissePieceMission piece, YvsGrhMissions mission, boolean controleFrais) {
        //build pièce trésorerie
        if (piece == null) {
            return false;
        }
        if (piece.getMontant() <= 0) {
            System.err.println("Vous devez preciser le montant !");
            return false;
        }
        if (controleFrais) {
            if (piece.getMontant() > mission.getTotalReste()) {
                System.err.println("La valeur de la pièce de trésorerie ne doit pas être supérieure à la valeur total des frais de mission");
                return false;
            }
        }
        if (piece.getDatePaimentPrevu() == null) {
            System.err.println("Aucune date de paiement n'a été indiqué");
            return false;
        }
        if (piece.getModel() != null ? piece.getModel().getId() < 1 : true) {
            System.err.println("Vous devez preciser le mode de paiement !");
            return false;
        }
        return true;
    }

    //Créée une pièce de caisse en attente de paiement
    public YvsComptaCaissePieceMission createOnePieceCaisse(YvsGrhMissions mission, YvsComptaCaissePieceMission piece, boolean controleFrais, DaoInterfaceLocal dao) {
        if (controlePiece(piece, mission, controleFrais)) {
            piece.setAuthor(currentUser);
            piece.setId(null);
            piece.setMontant(arrondi(piece.getMontant(), dao));
            piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            piece.setMission(mission);
            piece.setDateSave(new Date());
            piece.setDateUpdate(new Date());
            if ((piece.getId() != null) ? piece.getId() < 1 : true) {
                String numero = dao.genererReference(Constantes.TYPE_PC_NAME, piece.getDatePiece(), 0, currentScte, currentAgence);
                if (numero != null ? numero.trim().length() < 1 : true) {
                    return null;
                }
                piece.setNumeroPiece(numero);
                piece = (YvsComptaCaissePieceMission) dao.save1(piece);
                YvsComptaMouvementCaisse mv = (YvsComptaMouvementCaisse) dao.loadOneByNameQueries("YvsComptaMouvementCaisse.findById", new String[]{"id"}, new Object[]{piece.getId()});
                if (mv != null) {
                    piece.setId(mv.getIdExterne());
                }
            } else {
                piece.setId(piece.getId());
                dao.update(piece);
            }
            return piece;
        } else {
            return null;
        }
    }

    public boolean valideOrdreMission(YvsGrhMissions entity, YvsUsersAgence currentUser, DaoInterfaceLocal dao) {
        try {
            if (entity != null ? (entity.getTotalResteAPlanifier() > 0) : false) {
                YvsBaseCaisse caisse = currentCaisse(currentUser.getUsers(), dao);
                if (caisse != null ? caisse.getId() < 1 : true) {
                    caisse = defautlCaisse(currentUser, dao);
                }
                if (caisse != null ? caisse.getId() < 1 : true) {
                    System.err.println("Aucune caisse de paiement n'a été trouvé; veuillez effectuer votre paramétrage !");
                    return false;
                }
                YvsBaseModeReglement modeP = modeEspece(currentUser, dao);
                if (modeP != null ? modeP.getId() < 1 : true) {
                    System.err.println("Aucun mode de paiement n'a été trouvé; veuillez effectuer votre paramétrage !");
                    return false;
                }
                String numero = dao.genererReference(Constantes.TYPE_PC_NAME, new Date(), caisse.getId(), currentScte, currentAgence);
                if (numero != null ? numero.trim().length() < 1 : true) {
                    return false;
                }
                // Enregistre une pièce de règlement mission pour le reste des frais non encore réglé
                YvsComptaCaissePieceMission y = new YvsComptaCaissePieceMission();
                y.setMontant(entity.getTotalResteAPlanifier());
                y.setNumeroPiece(numero);
                y.setModel(modeP);
                y.setDatePiece(new Date());
                y.setDatePaimentPrevu(new Date());
                y.setCaisse(caisse);
                y.setAuthor(currentUser);
                y.setNote("REGLEMENT MISSION N°" + entity.getNumeroMission());
                createOnePieceCaisse(entity, y, false, dao);
            }
            return true;
        } catch (Exception ex) {
            System.err.println("Action impossible!!!" + ex.getMessage());
        }
        return false;
    }

    public boolean valideBonProvisoire(YvsGrhMissions entity, YvsUsersAgence currentUser, DaoInterfaceLocal dao) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                entity.setBonsProvisoire(dao.loadNameQueries("YvsComptaJustifBonMission.findByMission", new String[]{"mission"}, new Object[]{entity}));
                if (entity.getBonsProvisoire() != null ? !entity.getBonsProvisoire().isEmpty() : false) {
                    double reste = entity.getTotalFraisMission() - entity.getTotalPiece() - entity.getTotalBonPaye();
                    if (reste <= 0) {
                        return false;
                    }
                    YvsBaseCaisse caisse = currentCaisse(currentUser.getUsers(), dao);
                    if (caisse != null ? caisse.getId() < 1 : true) {
                        caisse = defautlCaisse(currentUser, dao);
                    }
                    if (caisse != null ? caisse.getId() < 1 : true) {
                        return false;
                    }
                    YvsBaseModeReglement modeP = modeEspece(currentUser, dao);
                    if (modeP == null) {
                        return false;
                    }
                    // Enregistre une pièce de règlement mission pour le reste des frais non encore réglé
                    if (reste > 0) {
                        String numero = dao.genererReference(Constantes.TYPE_PC_NAME, new Date(), caisse.getId(), currentUser.getAgence().getSociete(), currentUser.getAgence());
                        if (numero != null ? numero.trim().length() < 1 : true) {
                            return false;
                        }
                        YvsComptaCaissePieceMission y = new YvsComptaCaissePieceMission();
                        y.setNumeroPiece(numero);
                        y.setMontant(reste);
                        y.setModel(modeP);
                        y.setCaisse(caisse);
                        y.setDatePiece(new Date());
                        y.setDatePaimentPrevu(new Date());
                        y.setNote("REGLEMENT MISSION N°" + entity.getNumeroMission());
                        createOnePieceCaisse(entity, y, false, dao);
                    }
                    //Enregistre une pièce correspondant au total des BP payé et justifier ces bon
                    YvsComptaCaissePieceMission y;
                    YvsComptaCaissePieceMission pcm;
                    for (YvsComptaJustifBonMission p : entity.getBonsProvisoire()) {
                        if (p.getPiece() != null ? p.getPiece().getId() > 0 : false) {
                            if (p.getPiece().getStatutPaiement().equals(Constantes.STATUT_DOC_PAYER)) {
                                String numero = dao.genererReference(Constantes.TYPE_PC_NAME, new Date(), caisse.getId(), currentUser.getAgence().getSociete(), currentUser.getAgence());
                                if (numero != null ? numero.trim().length() < 1 : true) {
                                    return false;
                                }
                                y = new YvsComptaCaissePieceMission();
                                y.setMontant(p.getPiece().getMontant());
                                y.setNumeroPiece(numero);
                                y.setModel(modeP);
                                y.setCaisse(caisse);
                                y.setDatePiece(new Date());
                                y.setDatePaiement(p.getPiece().getDateBon());
                                y.setDatePaimentPrevu(p.getPiece().getDateBon());
                                y.setNote("REGLEMENT MISSION N°" + entity.getNumeroMission() + "BP: " + p.getPiece().getNumero());
                                pcm = createOnePieceCaisse(entity, y, true, dao);
                                if (pcm != null ? (pcm.getId() != null ? pcm.getId() > 0 : false) : false) {
                                    pcm.setMission(entity);
                                    p.getPiece().setStatutJustify(Constantes.ETAT_JUSTIFIE);
                                    p.getPiece().setAuthor(currentUser);
                                    p.getPiece().setDateUpdate(new Date());
                                    dao.update(p.getPiece());
                                }
                            }
                        }

                    }
                }
            }
            return true;
        } catch (Exception ex) {
            System.err.println("Action impossible!!!" + ex.getMessage());
        }
        return false;
    }
    /*END MISSION*/

    /*BEGIN DOC STOCK*/
    public boolean verifyOperation(YvsBaseDepots d, String type, String operation, DaoInterfaceLocal dao) {
        if (d != null ? d.getId() > 0 : false) {
            if (!checkOperationDepot(d.getId(), type, dao)) {
                System.err.println("Le depot '" + d.getDesignation() + "' n'est pas paramètré pour les opérations '" + type + "'");
                return false;
            }
            if (!checkOperationDepot(d.getId(), type, operation, dao)) {
                System.err.println("Le depot '" + d.getDesignation() + "' n'est pas paramètré pour les opérations '" + operation + "' en '" + type + "'");
                return false;
            }
        }
        return true;
    }

    public boolean checkOperationDepot(long depot, String type, DaoInterfaceLocal dao) {
        champ = new String[]{"depot", "type"};
        val = new Object[]{new YvsBaseDepots(depot), type};
        nameQueri = "YvsBaseDepotOperation.findByDepotType";
        List<YvsBaseDepotOperation> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        return l != null ? !l.isEmpty() : false;
    }

    public boolean checkOperationDepot(long depot, String type, String operation, DaoInterfaceLocal dao) {
        if (!checkOperationDepot(depot, type, dao)) {
            return false;
        }
        champ = new String[]{"depot", "type", "operation"};
        val = new Object[]{new YvsBaseDepots(depot), type, operation};
        nameQueri = "YvsBaseDepotOperation.findByDepotTypeOperation";
        List<YvsBaseDepotOperation> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        return l != null ? !l.isEmpty() : false;
    }

    public boolean checkOperationArticle(long article, long depot, String operation, DaoInterfaceLocal dao) {
        if (depot > 0) {
            champ = new String[]{"depot", "article"};
            val = new Object[]{new YvsBaseDepots(depot), new YvsBaseArticles(article)};
            nameQueri = "YvsBaseArticleDepot.findByArticleDepot";
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

    public boolean validerStock(YvsComDocStocks entity, DaoInterfaceLocal dao) {
        if (entity == null) {
            return false;
        }
        if (!entity.getTypeDoc().equals(Constantes.TYPE_ES)) {
            if (!verifyOperation(entity.getSource(), Constantes.SORTIE, entity.getNature(), dao)) {
                return false;
            }
            for (YvsComContenuDocStock c : entity.getContenus()) {
                String result = dao.controleStock(c.getArticle().getId(), c.getConditionnement().getId(), entity.getSource().getId(), 0L, c.getQuantite(), 0, "INSERT", "S", entity.getDateDoc(), (c.getLotSortie() != null ? c.getLotSortie().getId() : 0));
                if (result != null) {
                    return false;
                }
            }
        } else {
            if (!verifyOperation(entity.getDestination(), Constantes.ENTREE, entity.getNature(), dao)) {
                return false;
            }
            for (YvsComContenuDocStock c : entity.getContenus()) {
                if (!checkOperationArticle(c.getArticle().getId(), entity.getDestination().getId(), Constantes.ENTREE, dao)) {
                    return false;
                }
            }
        }
        if (changeStatutStock(entity, Constantes.ETAT_VALIDE, dao)) {
            entity.setCloturer(false);
            entity.setAnnulerBy(null);
            entity.setValiderBy(currentUser.getUsers());
            entity.setCloturerBy(null);
            entity.setDateAnnuler(null);
            entity.setDateCloturer(null);
            entity.setDateValider(new Date());
            entity.setStatut(Constantes.ETAT_VALIDE);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                entity.setAuthor(currentUser);
            }
            dao.update(entity);
            for (YvsComContenuDocStock c : entity.getContenus()) {
                c.setStatut(Constantes.ETAT_VALIDE);
                dao.update(c);
            }
            return true;
        }
        return false;
    }

    public boolean changeStatutStock(YvsComDocStocks entity, String etat, DaoInterfaceLocal dao) {
        if (!etat.equals("") && entity != null) {
            if (entity.getCloturer()) {
                return false;
            }
            String rq = "UPDATE yvs_com_doc_stocks SET statut = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(entity.getId(), 1)};
            dao.requeteLibre(rq, param);
            entity.setStatut(etat);
            if (etat.equals(Constantes.ETAT_VALIDE)) {
                for (YvsComContenuDocStock c : entity.getContenus()) {
                    c.setStatut(Constantes.ETAT_VALIDE);
                    c.setAuthor(currentUser);
                    dao.update(c);
                }
            } else {
                for (YvsComContenuDocStock c : entity.getContenus()) {
                    c.setStatut(Constantes.ETAT_EDITABLE);
                    c.setAuthor(currentUser);
                    dao.update(c);
                }
            }
            return true;
        }
        return false;
    }
    /*END DOC STOCK*/

    /*BEGIN APPROVISIONNEMENT*/
    public boolean changeStatutAppro(YvsComFicheApprovisionnement entity, String etat, DaoInterfaceLocal dao) {
        if (!etat.equals("") && entity != null) {
            if (entity.getCloturer()) {
                return false;
            }
            String rq = "UPDATE yvs_com_fiche_approvisionnement SET etat = '" + etat + "', reference = '" + entity.getReference() + "' WHERE id=?";
            Options[] param = new Options[]{new Options(entity.getId(), 1)};
            dao.requeteLibre(rq, param);
            entity.setEtat(etat);
            return true;
        }
        return false;
    }
    /*END APPROVISIONNEMENT*/

    /*BEGIN COMPTABILISATION*/
    public YvsComptaContentJournal convertTabToContenu(Object[] line, Long id, DaoInterfaceLocal dao) {
        YvsComptaContentJournal re = null;
        if (line != null ? line.length > 0 : false) {
            Long _id = line[0] != null ? Long.valueOf(line[0].toString()) : null;
            Integer _jour = line[1] != null ? Integer.valueOf(line[1].toString()) : 0;
            String _num_piece = line[2] != null ? line[2].toString() : "";
            String _num_ref = line[3] != null ? line[3].toString() : "";
            Long _compte_general = line[4] != null ? Long.valueOf(line[4].toString()) : 0;
            Long _compte_tiers = line[5] != null ? Long.valueOf(line[5].toString()) : 0;
            String _libelle = line[6] != null ? line[6].toString() : "";
            Double _debit = line[7] != null ? Double.valueOf(line[7].toString()) : 0;
            Double _credit = line[8] != null ? Double.valueOf(line[8].toString()) : 0;
            Date _echeance = line[9] != null ? (Date) line[9] : new Date();
            Long _ref_externe = line[10] != null ? Long.valueOf(line[10].toString()) : id;
            String _table_externe = line[11] != null ? line[11].toString() : Constantes.SCR_VENTE;
            String _statut = line[12] != null ? line[12].toString() : Constantes.ETAT_VALIDE;
            String _error = line[13] != null ? line[13].toString() : "";
            Long _contenu_ = line[14] != null ? Long.valueOf(line[14].toString()) : 0;
            Long _centre = line[15] != null ? Long.valueOf(line[15].toString()) : 0;
            Double _coefficient = line[16] != null ? Double.valueOf(line[16].toString()) : 0;
            Integer _numero = line[17] != null ? Integer.valueOf(line[17].toString()) : 0;
            Integer _agence = line[18] != null ? Integer.valueOf(line[18].toString()) : 0;
            String _warning = line[19] != null ? line[19].toString() : "";
            String _tableTiers = line[20] != null ? line[20].toString() : "";

            if (_error != null ? _error.trim().length() > 0 : false) {
                dao.setRESULT("Comptabilisation impossible.. car " + _error);
                return null;
            }
            re = new YvsComptaContentJournal(_id, _jour, _num_piece, _num_ref, _libelle, _debit, _credit, _echeance, _compte_general, _compte_tiers, _tableTiers, _ref_externe, _table_externe, _statut.charAt(0), _numero);
            if (re.getCompteGeneral() != null ? re.getCompteGeneral().getId() > 0 : false) {
                re.setCompteGeneral((YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findById", new String[]{"id"}, new Object[]{re.getCompteGeneral().getId()}));
            }
            if (_contenu_ != null) {
                YvsComptaContentAnalytique ca = new YvsComptaContentAnalytique((long) -(re.getAnalytiques().size() + 1));
                if (_centre != null ? _centre > 0 : false) {
                    ca.setCentre((YvsComptaCentreAnalytique) dao.loadOneByNameQueries("YvsComptaCentreAnalytique.findById", new String[]{"id"}, new Object[]{_centre}));
                }
                ca.setCoefficient(_coefficient);
                ca.setDebit(_debit);
                ca.setCredit(_credit);
                ca.setAuthor(currentUser);
                ca.setDateSaisie(new Date());
                ca.setDateSave(new Date());
                ca.setDateUpdate(new Date());

                re.getAnalytiques().add(ca);
            }
        }
        return re;
    }

    public boolean controleContenu(YvsComptaContentJournal bean, DaoInterfaceLocal dao) {
        if (bean == null) {
            return false;
        }
        if (bean.getCompteGeneral() != null ? (bean.getCompteGeneral().getId() != null ? bean.getCompteGeneral().getId() < 1 : true) : true) {
            dao.setRESULT("Vous devez preciser le compte général");
            return false;
        }
        YvsBasePlanComptable c = bean.getCompteGeneral();
        if (c.getSaisieAnalytique() && false) {
//            dao.setRESULT("Vous devez preciser le plan analytique");
//            return false;
        }
        if (c.getSaisieCompteTiers() && bean.getCompteTiers() == null) {
            dao.setRESULT("Vous devez preciser le compte tiers");
            return false;
        }
        if (c.getSaisieEcheance() && bean.getEcheance() == null) {
            dao.setRESULT("Vous devez preciser l'échéancier");
            return false;
        }
        return true;
    }

    public List<YvsComptaContentJournal> buildCaisseDiversToComptabilise(long id, DaoInterfaceLocal dao) {
        return dao.buildContentJournal(id, Constantes.SCR_CAISSE_DIVERS, currentScte, "");
    }

    public boolean comptabiliserCaisseDivers(YvsComptaCaissePieceDivers y, DaoInterfaceLocal dao) {
        return comptabiliserCaisseDivers(y, buildCaisseDiversToComptabilise(y.getId(), dao), dao);
    }

    public boolean comptabiliserCaisseDivers(YvsComptaCaissePieceDivers y, List<YvsComptaContentJournal> contenus, DaoInterfaceLocal dao) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_CAISSE_DIVERS);
                if (comptabilise) {
                    dao.setRESULT("Comptabilisation impossible... car ce document est déjà comptabilisée");
                    return false;
                }
                YvsComptaPiecesComptable p = majComptaCaisseDivers(y.getId(), contenus, dao);
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (reponse) {
                    y.setComptabilised(true);
                }
                return reponse;
            } else {
                dao.setRESULT("Comptabilisation impossible... car cette pièce de caisse n'est pas validée");
            }
        }
        return false;
    }

    private YvsComptaPiecesComptable majComptaCaisseDivers(long id, List<YvsComptaContentJournal> contenus, DaoInterfaceLocal dao) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            YvsComptaCaissePieceDivers y = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    return dao.saveNewPieceComptable(y.getDatePiece(), y.getCaisse().getJournal(), contenus, currentUser);
                } else {
                    dao.setRESULT("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                }
            }
        }
        return null;
    }

    private int varianteDivers(YvsComptaCaisseDocDivers y, List<YvsComptaAbonementDocDivers> abs) {
        int variante = 1;// Comptabilisation compte general / compte caisse
        if (y.getCompteGeneral() != null ? y.getCompteGeneral().getId() < 1 : true) {
            return 1;
        }
        if (!y.getCompteGeneral().getSaisieCompteTiers()) {
            if (y.getIdTiers() != null ? (y.getIdTiers() > 0 ? (y.getTableTiers() != null ? y.getTableTiers().trim().length() > 0 : false) : false) : false) {
                variante = 2;// Comptabilisation compte general / compte tiers | compte tiers / compte caisse                
                if (abs != null ? !abs.isEmpty() : false) {
                    variante = 3;// Comptabilisation compte abonnement / compte tiers | compte tiers / compte caisse | compte general / compte abonnement
                }
            } else {
                if (abs != null ? !abs.isEmpty() : false) {
                    variante = 4;// Comptabilisation compte abonnement / compte caisse | compte general / compte abonnement
                }
            }
        } else {
            variante = 5;// Comptabilisation compte general (code tiers) / compte caisse
        }
        return variante;
    }

    private YvsComptaPiecesComptable buildAnnexeDivers(YvsComptaCaisseDocDivers y, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, DaoInterfaceLocal dao) {
        YvsComptaPiecesComptable p = new YvsComptaPiecesComptable();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            String libelle = y.getLibelleComptable();
            if (libelle != null ? libelle.trim().length() < 1 : true) {
                libelle = y.getNumPiece();
            }
            YvsComptaContentJournal cd;
            YvsComptaContentJournal cc;

            Calendar c = Calendar.getInstance();
            c.setTime(y.getDateDoc() != null ? y.getDateDoc() : new Date());

            double total = y.getMontant();
            double cout = 0;
            switch (y.getMouvement()) {
                case Constantes.MOUV_CAISS_ENTREE: {
                    for (YvsComptaCoutSupDocDivers o : couts) {
                        if (o.getTypeCout().getCompte() == null) {
                            dao.setRESULT("Aucun compte n'a été trouvé pour le type de coût [" + o.getTypeCout().getLibelle() + "]");
                            return null;
                        }
                        if (o.getLierTiers()) {
                            cout += o.getMontant();
                        } else {
                            if (o.getPieceCout() != null ? o.getPieceCout().getId() > 0 : false) {
                                if (o.getPieceCout().getPiece().getCaisse() == null) {
                                    dao.setRESULT("Aucune caisse n'a été trouvé pour le réglement le type de coût [" + o.getTypeCout().getLibelle() + "]");
                                    return null;
                                }
                                if (o.getPieceCout().getPiece().getCaisse().getCompte() == null) {
                                    dao.setRESULT("Aucune compte n'a été trouvé pour la caisse du réglement le type de coût [" + o.getTypeCout().getLibelle() + "]");
                                    return null;
                                }
                                p.getPiecesCout().add(o.getPieceCout());
                                cd = new YvsComptaContentJournal((long) -(p.getContentsDebit().size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (p.getContentsDebit().size() + 1), "Réglement " + o.getTypeCout().getLibelle() + " sur " + libelle, o.getMontant(), 0, new Date(), o.getPieceCout().getPiece().getCaisse().getCompte(), 0L, y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 1);
                                p.getContentsDebit().add(cd);
                            }
                        }
                        cc = new YvsComptaContentJournal((long) -(p.getContentsCredit().size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (p.getContentsCredit().size() + 1), o.getTypeCout().getLibelle() + " sur " + libelle, 0, o.getMontant(), new Date(), o.getTypeCout().getCompte(), 0L, y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 2);
                        p.getContentsCredit().add(cc);
                    }
                    for (YvsComptaTaxeDocDivers t : taxs) {
                        if (t.getTaxe().getCompte() == null) {
                            dao.setRESULT("Aucun compte n'a été trouvé pour la taxe [" + t.getTaxe().getDesignation() + "]");
                            return null;
                        }
                        double valeur = dao.arrondi(currentScte.getId(), t.getMontant());
                        total -= valeur;
                        cc = new YvsComptaContentJournal((long) -(p.getContentsCredit().size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (p.getContentsCredit().size() + 1), t.getTaxe().getLibellePrint() + " sur  " + libelle, 0, valeur, new Date(), t.getTaxe().getCompte(), 0L, y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 2);
                        p.getContentsCredit().add(cc);
                    }
                    break;
                }
                default: {
                    for (YvsComptaTaxeDocDivers t : taxs) {
                        if (t.getTaxe().getCompte() == null) {
                            dao.setRESULT("Aucun compte n'a été trouvé pour la taxe [" + t.getTaxe().getDesignation() + "]");
                            return null;
                        }
                        double valeur = dao.arrondi(currentScte.getId(), t.getMontant());
                        total -= valeur;
                        cd = new YvsComptaContentJournal((long) -(p.getContentsDebit().size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (p.getContentsDebit().size() + 1), t.getTaxe().getDesignation() + " sur " + libelle, valeur, 0, new Date(), t.getTaxe().getCompte(), 0L, y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 1);
                        p.getContentsDebit().add(cd);
                    }
                    for (YvsComptaCoutSupDocDivers o : couts) {
                        if (o.getTypeCout().getCompte() == null) {
                            dao.setRESULT("Aucun compte n'a été trouvé pour le type de coût [" + o.getTypeCout().getLibelle() + "]");
                            return null;
                        }
                        if (o.getLierTiers()) {
                            cout += o.getMontant();
                        } else {
                            if (o.getPieceCout() != null ? o.getPieceCout().getId() > 0 : false) {
                                if (o.getPieceCout().getPiece().getCaisse() == null) {
                                    dao.setRESULT("Aucune caisse n'a été trouvé pour le réglement le type de coût [" + o.getTypeCout().getLibelle() + "]");
                                    return null;
                                }
                                if (o.getPieceCout().getPiece().getCaisse().getCompte() == null) {
                                    dao.setRESULT("Aucune compte n'a été trouvé pour la caisse du réglement le type de coût [" + o.getTypeCout().getLibelle() + "]");
                                    return null;
                                }
                                p.getPiecesCout().add(o.getPieceCout());
                                cc = new YvsComptaContentJournal((long) -(p.getContentsCredit().size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (p.getContentsCredit().size() + 1), "Réglement " + o.getTypeCout().getLibelle() + " sur " + libelle, 0, o.getMontant(), new Date(), o.getPieceCout().getPiece().getCaisse().getCompte(), 0L, y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 2);
                                p.getContentsCredit().add(cc);
                            }
                        }
                        cd = new YvsComptaContentJournal((long) -(p.getContentsDebit().size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (p.getContentsDebit().size() + 1), o.getTypeCout().getLibelle() + " sur " + libelle, o.getMontant(), 0, new Date(), o.getTypeCout().getCompte(), 0L, y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 2);
                        p.getContentsDebit().add(cd);
                    }
                    break;
                }
            }
            p.setTotal(total);
            p.setCouts(cout);
        }
        return p;
    }

    private YvsComptaPiecesComptable buildAnnexeDivers(YvsComptaCaissePieceDivers r, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, DaoInterfaceLocal dao) {
        YvsComptaPiecesComptable p = new YvsComptaPiecesComptable();
        if (r != null ? (r.getId() != null ? r.getId() > 0 : false) : false) {
            String libelle = r.getDocDivers().getLibelleComptable();
            if (libelle != null ? libelle.trim().length() < 1 : true) {
                libelle = r.getDocDivers().getNumPiece();
            }
            YvsComptaContentJournal cd;
            YvsComptaContentJournal cc;

            Calendar c = Calendar.getInstance();
            c.setTime(r.getDateValider());

            double cout = 0;
            double total = r.getMontant();
            double taux = r.getMontant() / (r.getDocDivers().getMontant() + r.getDocDivers().getCout());
            switch (r.getDocDivers().getMouvement()) {
                case Constantes.MOUV_CAISS_ENTREE: {
                    total = r.getMontant();
                    for (YvsComptaTaxeDocDivers t : taxs) {
                        if (t.getTaxe().getCompte() == null) {
                            dao.setRESULT("Aucun compte n'a été trouvé pour la taxe [" + t.getTaxe().getDesignation() + "]");
                            return null;
                        }
                        double valeur = dao.arrondi(currentScte.getId(), r.getMontant() * t.getTaxe().getTaux() / 100);
                        total -= valeur;
                        cc = new YvsComptaContentJournal((long) -(p.getContentsCredit().size() + 1), c.get(Calendar.DAY_OF_MONTH), r.getDocDivers().getNumPiece(), r.getDocDivers().getNumPiece() + "-0" + (p.getContentsCredit().size() + 1), t.getTaxe().getLibellePrint() + " sur   " + libelle, 0, valeur, r.getDateValider(), t.getTaxe().getCompte(), 0L, r.getDocDivers().getTableTiers(), r.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 2);
                        p.getContentsCredit().add(cc);
                    }
                    for (YvsComptaCoutSupDocDivers o : couts) {
                        if (o.getTypeCout().getCompte() == null) {
                            dao.setRESULT("Aucun compte n'a été trouvé pour le type de coût [" + o.getTypeCout().getLibelle() + "]");
                            return null;
                        }
                        double valeur = dao.arrondi(currentScte.getId(), o.getMontant() * taux);
                        total -= valeur;
                        if (o.getLierTiers()) {
                            cout += valeur;
                        } else {
                            if (o.getPieceCout() != null ? o.getPieceCout().getId() > 0 : false) {
                                if (o.getPieceCout().getPiece().getCaisse() == null) {
                                    dao.setRESULT("Aucune caisse n'a été trouvé pour le réglement le type de coût [" + o.getTypeCout().getLibelle() + "]");
                                    return null;
                                }
                                if (o.getPieceCout().getPiece().getCaisse().getCompte() == null) {
                                    dao.setRESULT("Aucune compte n'a été trouvé pour la caisse du réglement le type de coût [" + o.getTypeCout().getLibelle() + "]");
                                    return null;
                                }
                                cd = new YvsComptaContentJournal((long) -(p.getContentsDebit().size() + 1), c.get(Calendar.DAY_OF_MONTH), r.getDocDivers().getNumPiece(), r.getDocDivers().getNumPiece() + "-0" + (p.getContentsDebit().size() + 1), "Réglement " + o.getTypeCout().getLibelle() + " sur " + libelle, valeur, 0, new Date(), o.getPieceCout().getPiece().getCaisse().getCompte(), 0L, r.getDocDivers().getTableTiers(), r.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 1);
                                p.getContentsDebit().add(cd);
                            }
                        }
                        cc = new YvsComptaContentJournal((long) -(p.getContentsCredit().size() + 1), c.get(Calendar.DAY_OF_MONTH), r.getDocDivers().getNumPiece(), r.getDocDivers().getNumPiece() + "-0" + (p.getContentsCredit().size() + 1), o.getTypeCout().getLibelle() + " sur " + libelle, 0, valeur, r.getDateValider(), o.getTypeCout().getCompte(), 0L, r.getDocDivers().getTableTiers(), r.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 2);
                        p.getContentsCredit().add(cc);
                    }
                    break;
                }
                default: {
                    total = r.getMontant();
                    for (YvsComptaTaxeDocDivers t : taxs) {
                        if (t.getTaxe().getCompte() == null) {
                            dao.setRESULT("Aucun compte n'a été trouvé pour la taxe [" + t.getTaxe().getDesignation() + "]");
                            return null;
                        }
                        double valeur = dao.arrondi(currentScte.getId(), r.getMontant() * t.getTaxe().getTaux() / 100);
                        total -= valeur;
                        cd = new YvsComptaContentJournal((long) -(p.getContentsDebit().size() + 1), c.get(Calendar.DAY_OF_MONTH), r.getDocDivers().getNumPiece(), r.getDocDivers().getNumPiece() + "-0" + (p.getContentsDebit().size() + 1), t.getTaxe().getLibellePrint() + " sur " + libelle, valeur, 0, c.getTime(), t.getTaxe().getCompte(), 0L, r.getDocDivers().getTableTiers(), r.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 1);
                        p.getContentsDebit().add(cd);
                    }
                    for (YvsComptaCoutSupDocDivers o : couts) {
                        if (o.getTypeCout().getCompte() == null) {
                            dao.setRESULT("Aucun compte n'a été trouvé pour le type de coût [" + o.getTypeCout().getLibelle() + "]");
                            return null;
                        }
                        double valeur = dao.arrondi(currentScte.getId(), o.getMontant() * taux);
                        total -= valeur;
                        if (o.getLierTiers()) {
                            cout += valeur;
                        } else {
                            if (o.getPieceCout() != null ? o.getPieceCout().getId() > 0 : false) {
                                if (o.getPieceCout().getPiece().getCaisse() == null) {
                                    dao.setRESULT("Aucune caisse n'a été trouvé pour le réglement le type de coût [" + o.getTypeCout().getLibelle() + "]");
                                    return null;
                                }
                                if (o.getPieceCout().getPiece().getCaisse().getCompte() == null) {
                                    dao.setRESULT("Aucune compte n'a été trouvé pour la caisse du réglement le type de coût [" + o.getTypeCout().getLibelle() + "]");
                                    return null;
                                }
                                cc = new YvsComptaContentJournal((long) -(p.getContentsCredit().size() + 1), c.get(Calendar.DAY_OF_MONTH), r.getDocDivers().getNumPiece(), r.getDocDivers().getNumPiece() + "-0" + (p.getContentsCredit().size() + 1), "Réglement " + o.getTypeCout().getLibelle() + " sur " + libelle, 0, valeur, new Date(), o.getPieceCout().getPiece().getCaisse().getCompte(), 0L, r.getDocDivers().getTableTiers(), r.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 2);
                                p.getContentsCredit().add(cc);
                            }
                        }
                        cd = new YvsComptaContentJournal((long) -(p.getContentsDebit().size() + 1), c.get(Calendar.DAY_OF_MONTH), r.getDocDivers().getNumPiece(), r.getDocDivers().getNumPiece() + "-0" + (p.getContentsDebit().size() + 1), o.getTypeCout().getLibelle() + " sur " + libelle, valeur, 0, c.getTime(), o.getTypeCout().getCompte(), 0L, r.getDocDivers().getTableTiers(), r.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 1);
                        p.getContentsDebit().add(cd);
                    }
                    break;
                }
            }
            p.setTotal(total);
            p.setCouts(cout);
        }
        return p;
    }

    private YvsComptaPiecesComptable buildAnnexeDivers(YvsComptaAbonementDocDivers ab, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, DaoInterfaceLocal dao) {
        YvsComptaPiecesComptable p = new YvsComptaPiecesComptable();
        if (ab != null ? (ab.getId() != null ? ab.getId() > 0 : false) : false) {
            String libelle = ab.getDocDivers().getLibelleComptable();
            if (libelle != null ? libelle.trim().length() < 1 : true) {
                libelle = ab.getDocDivers().getNumPiece();
            }
            YvsComptaContentJournal cd;
            YvsComptaContentJournal cc;

            Calendar c = Calendar.getInstance();
            c.setTime(ab.getEcheance());

            double cout = 0;
            double total = ab.getValeur();
            double taux = ab.getValeur() / (ab.getDocDivers().getMontant() + ab.getDocDivers().getCout());
            switch (ab.getDocDivers().getMouvement()) {
                case Constantes.MOUV_CAISS_ENTREE: {
                    total = ab.getValeur();
                    for (YvsComptaTaxeDocDivers t : taxs) {
                        if (t.getTaxe().getCompte() == null) {
                            dao.setRESULT("Aucun compte n'a été trouvé pour la taxe [" + t.getTaxe().getDesignation() + "]");
                            return null;
                        }
                        double valeur = dao.arrondi(currentScte.getId(), ab.getValeur() * t.getTaxe().getTaux() / 100);
                        total -= valeur;
                        cc = new YvsComptaContentJournal((long) -(p.getContentsCredit().size() + 1), c.get(Calendar.DAY_OF_MONTH), ab.getDocDivers().getNumPiece(), ab.getDocDivers().getNumPiece() + "-0" + (p.getContentsCredit().size() + 1), t.getTaxe().getLibellePrint() + " sur   " + libelle, 0, valeur, ab.getEcheance(), t.getTaxe().getCompte(), 0L, ab.getDocDivers().getTableTiers(), ab.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 2);
                        p.getContentsCredit().add(cc);
                    }
                    for (YvsComptaCoutSupDocDivers o : couts) {
                        if (o.getTypeCout().getCompte() == null) {
                            dao.setRESULT("Aucun compte n'a été trouvé pour le type de coût [" + o.getTypeCout().getLibelle() + "]");
                            return null;
                        }
                        double valeur = dao.arrondi(currentScte.getId(), o.getMontant() * taux);
                        total -= valeur;
                        if (o.getLierTiers()) {
                            cout += valeur;
                        } else {
                            if (o.getPieceCout() != null ? o.getPieceCout().getId() > 0 : false) {
                                if (o.getPieceCout().getPiece().getCaisse() == null) {
                                    dao.setRESULT("Aucune caisse n'a été trouvé pour le réglement le type de coût [" + o.getTypeCout().getLibelle() + "]");
                                    return null;
                                }
                                if (o.getPieceCout().getPiece().getCaisse().getCompte() == null) {
                                    dao.setRESULT("Aucune compte n'a été trouvé pour la caisse du réglement le type de coût [" + o.getTypeCout().getLibelle() + "]");
                                    return null;
                                }
                                cd = new YvsComptaContentJournal((long) -(p.getContentsDebit().size() + 1), c.get(Calendar.DAY_OF_MONTH), ab.getDocDivers().getNumPiece(), ab.getDocDivers().getNumPiece() + "-0" + (p.getContentsDebit().size() + 1), "Réglement " + o.getTypeCout().getLibelle() + " sur " + libelle, valeur, 0, new Date(), o.getPieceCout().getPiece().getCaisse().getCompte(), 0L, ab.getDocDivers().getTableTiers(), ab.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 1);
                                p.getContentsDebit().add(cd);
                            }
                        }
                        cc = new YvsComptaContentJournal((long) -(p.getContentsCredit().size() + 1), c.get(Calendar.DAY_OF_MONTH), ab.getDocDivers().getNumPiece(), ab.getDocDivers().getNumPiece() + "-0" + (p.getContentsCredit().size() + 1), o.getTypeCout().getLibelle() + " sur " + libelle, 0, valeur, ab.getEcheance(), o.getTypeCout().getCompte(), 0L, ab.getDocDivers().getTableTiers(), ab.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 2);
                        p.getContentsCredit().add(cc);
                    }
                    break;
                }
                default: {
                    total = ab.getValeur();
                    for (YvsComptaTaxeDocDivers t : taxs) {
                        if (t.getTaxe().getCompte() == null) {
                            dao.setRESULT("Aucun compte n'a été trouvé pour la taxe [" + t.getTaxe().getDesignation() + "]");
                            return null;
                        }
                        double valeur = dao.arrondi(currentScte.getId(), ab.getValeur() * t.getTaxe().getTaux() / 100);
                        total -= valeur;
                        cd = new YvsComptaContentJournal((long) -(p.getContentsDebit().size() + 1), c.get(Calendar.DAY_OF_MONTH), ab.getDocDivers().getNumPiece(), ab.getDocDivers().getNumPiece() + "-0" + (p.getContentsDebit().size() + 1), t.getTaxe().getLibellePrint() + " sur " + libelle, valeur, 0, c.getTime(), t.getTaxe().getCompte(), 0L, ab.getDocDivers().getTableTiers(), ab.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 1);
                        p.getContentsDebit().add(cd);
                    }
                    for (YvsComptaCoutSupDocDivers o : couts) {
                        if (o.getTypeCout().getCompte() == null) {
                            dao.setRESULT("Aucun compte n'a été trouvé pour le type de coût [" + o.getTypeCout().getLibelle() + "]");
                            return null;
                        }
                        double valeur = dao.arrondi(currentScte.getId(), o.getMontant() * taux);
                        total -= valeur;
                        if (o.getLierTiers()) {
                            cout += valeur;
                        } else {
                            if (o.getPieceCout() != null ? o.getPieceCout().getId() > 0 : false) {
                                if (o.getPieceCout().getPiece().getCaisse() == null) {
                                    dao.setRESULT("Aucune caisse n'a été trouvé pour le réglement le type de coût [" + o.getTypeCout().getLibelle() + "]");
                                    return null;
                                }
                                if (o.getPieceCout().getPiece().getCaisse().getCompte() == null) {
                                    dao.setRESULT("Aucune compte n'a été trouvé pour la caisse du réglement le type de coût [" + o.getTypeCout().getLibelle() + "]");
                                    return null;
                                }
                                cc = new YvsComptaContentJournal((long) -(p.getContentsCredit().size() + 1), c.get(Calendar.DAY_OF_MONTH), ab.getDocDivers().getNumPiece(), ab.getDocDivers().getNumPiece() + "-0" + (p.getContentsCredit().size() + 1), "Réglement " + o.getTypeCout().getLibelle() + " sur " + libelle, 0, valeur, new Date(), o.getPieceCout().getPiece().getCaisse().getCompte(), 0L, ab.getDocDivers().getTableTiers(), ab.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 2);
                                p.getContentsCredit().add(cc);
                            }
                        }
                        cd = new YvsComptaContentJournal((long) -(p.getContentsDebit().size() + 1), c.get(Calendar.DAY_OF_MONTH), ab.getDocDivers().getNumPiece(), ab.getDocDivers().getNumPiece() + "-0" + (p.getContentsDebit().size() + 1), o.getTypeCout().getLibelle() + " sur " + libelle, valeur, 0, c.getTime(), o.getTypeCout().getCompte(), 0L, ab.getDocDivers().getTableTiers(), ab.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 1);
                        p.getContentsDebit().add(cd);
                    }
                    break;
                }
            }
            p.setTotal(total);
            p.setCouts(cout);
        }
        return p;
    }

    private List<YvsComptaContentAnalytique> buildSectionAnalytique(YvsComptaCaissePieceDivers y, YvsComptaContentJournal c, List<YvsComptaCentreDocDivers> secs) {
        List<YvsComptaContentAnalytique> analytiques = new ArrayList<>();
        if (secs != null ? !secs.isEmpty() : false) {
            YvsComptaContentAnalytique se;
            double taux = y.getMontant() / y.getDocDivers().getMontant();
            System.err.println("y.getDocDivers().getMontant() : " + y.getDocDivers().getMontant());
            System.err.println("y.getMontant() : " + y.getMontant());
            System.err.println("taux : " + taux);
            for (YvsComptaCentreDocDivers s : secs) {
                se = new YvsComptaContentAnalytique((long) -(analytiques.size() + 1));
                se.setAuthor(currentUser);
                se.setCentre(s.getCentre());
                se.setCoefficient((s.getMontant() * taux) / y.getMontant() * 100);
                se.setContenu(c);
                se.setDateSave(new Date());
                se.setDateUpdate(new Date());
                se.setDateSaisie(new Date());
                switch (y.getMouvement()) {
                    case Constantes.MOUV_CAISS_ENTREE: {
                        se.setDebit(0.0);
                        se.setCredit(Constantes.arrondiA2Chiffre(y.getMontant() * se.getCoefficient() / 100));
                        break;
                    }
                    default: {
                        se.setCredit(0.0);
                        se.setDebit(Constantes.arrondiA2Chiffre(y.getMontant() * se.getCoefficient() / 100));
                        break;
                    }
                }
                analytiques.add(se);
            }
        }
        return analytiques;
    }

    private List<YvsComptaContentAnalytique> buildSectionAnalytique(YvsComptaCaisseDocDivers y, YvsComptaContentJournal c, List<YvsComptaCentreDocDivers> secs) {
        List<YvsComptaContentAnalytique> analytiques = new ArrayList<>();
        if (secs != null ? !secs.isEmpty() : false) {
            YvsComptaContentAnalytique se;
            for (YvsComptaCentreDocDivers s : secs) {
                se = new YvsComptaContentAnalytique((long) -(analytiques.size() + 1));
                se.setAuthor(currentUser);
                se.setCentre(s.getCentre());
                se.setCoefficient(s.getMontant() / y.getMontant() * 100);
                se.setContenu(c);
                se.setDateSave(new Date());
                se.setDateUpdate(new Date());
                se.setDateSaisie(new Date());
                switch (y.getMouvement()) {
                    case Constantes.MOUV_CAISS_ENTREE: {
                        se.setDebit(0.0);
                        se.setCredit(Constantes.arrondiA2Chiffre(y.getMontant() * se.getCoefficient() / 100));
                        break;
                    }
                    default: {
                        se.setCredit(0.0);
                        se.setDebit(Constantes.arrondiA2Chiffre(y.getMontant() * se.getCoefficient() / 100));
                        break;
                    }
                }
                analytiques.add(se);
            }
        }
        return analytiques;
    }

    private List<YvsComptaContentAnalytique> buildSectionAnalytique(YvsComptaAbonementDocDivers ab, YvsComptaContentJournal c, List<YvsComptaCentreDocDivers> secs) {
        List<YvsComptaContentAnalytique> analytiques = new ArrayList<>();
        if (secs != null ? !secs.isEmpty() : false) {
            YvsComptaContentAnalytique se;
            double total = ab.getDocDivers().getMontant() + ab.getDocDivers().getCout();
            double taux = ab.getValeur() / total;
            for (YvsComptaCentreDocDivers s : secs) {
                double montant = s.getMontant() * taux;
                se = new YvsComptaContentAnalytique((long) -(analytiques.size() + 1));
                se.setAuthor(currentUser);
                se.setCentre(s.getCentre());
                se.setCoefficient(montant / ab.getValeur() * 100);
                se.setContenu(c);
                se.setDateSave(new Date());
                se.setDateUpdate(new Date());
                se.setDateSaisie(ab.getEcheance());
                switch (ab.getDocDivers().getMouvement()) {
                    case Constantes.MOUV_CAISS_ENTREE: {
                        se.setDebit(0.0);
                        se.setCredit(Constantes.arrondiA2Chiffre(ab.getValeur() * se.getCoefficient() / 100));
                        break;
                    }
                    default: {
                        se.setCredit(0.0);
                        se.setDebit(Constantes.arrondiA2Chiffre(ab.getValeur() * se.getCoefficient() / 100));
                        break;
                    }
                }
                analytiques.add(se);
            }
        }
        return analytiques;
    }

    public List<YvsComptaContentJournal> buildDiversToComptabilise(long id, DaoInterfaceLocal dao) {
        List<YvsComptaAbonementDocDivers> abs = dao.loadNameQueries("YvsComptaAbonementDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{new YvsComptaCaisseDocDivers(id)});
        List<YvsComptaCentreDocDivers> secs = dao.loadNameQueries("YvsComptaCentreDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{new YvsComptaCaisseDocDivers(id)});
        List<YvsComptaTaxeDocDivers> taxs = dao.loadNameQueries("YvsComptaTaxeDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{new YvsComptaCaisseDocDivers(id)});
        List<YvsComptaCoutSupDocDivers> couts = dao.loadNameQueries("YvsComptaCoutSupDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{new YvsComptaCaisseDocDivers(id)});
        return buildDiversToComptabilise(id, abs, secs, taxs, couts, dao);
    }

    public List<YvsComptaContentJournal> buildDiversToComptabilise(YvsComptaAbonementDocDivers ab, List<YvsComptaCentreDocDivers> secs, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, DaoInterfaceLocal dao) {
        if (ab != null ? (ab.getId() != null ? ab.getId() > 0 : false) : false) {
            if (ab.getDocDivers().getCompteGeneral() != null ? (ab.getDocDivers().getCompteGeneral().getId() != null ? ab.getDocDivers().getCompteGeneral().getId() < 1 : true) : true) {
                dao.setRESULT("Aucun compte de charge n'a été trouvé ");
                return null;
            }

            YvsBasePlanComptable compte = ab.getPlan() != null ? ab.getPlan().getCompte() : null;
            System.err.println("compte" + compte);
            System.err.println("currentAgence" + currentAgence);
            System.err.println("currentScte" + currentScte);
            if (compte != null ? (compte.getId() != null ? compte.getId() < 1 : true) : true) {
                compte = (YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findByTypeNature", new String[]{"societe", "nature"}, new Object[]{currentScte, Constantes.NAT_ABONNEMENT});
                System.err.println("compte" + compte);
                if (compte != null ? (compte.getId() != null ? compte.getId() < 1 : true) : true) {
                    dao.setRESULT("Comptabilisation impossible...car le compte des abonnements n'est pas paramètré");
                    return null;
                }
            }

            YvsComptaPiecesComptable a = buildAnnexeDivers(ab, taxs, couts, dao);
            System.err.println("a" + a);
            if (a == null) {
                return null;
            }
            System.err.println("a.getTotal()" + a.getTotal());
            List<YvsComptaContentJournal> cds = new ArrayList<>(a.getContentsDebit());
            List<YvsComptaContentJournal> ccs = new ArrayList<>(a.getContentsCredit());
            List<YvsComptaContentJournal> list = new ArrayList<>();

            YvsComptaContentJournal cd;
            YvsComptaContentJournal cc;

            Calendar c = Calendar.getInstance();
            c.setTime(ab.getEcheance());

            String libelle = ab.getDocDivers().getLibelleComptable();
            if (libelle != null ? libelle.trim().length() < 1 : true) {
                libelle = ab.getDocDivers().getNumPiece();
            }

            switch (ab.getDocDivers().getMouvement()) {
                case Constantes.MOUV_CAISS_ENTREE: {
                    cc = new YvsComptaContentJournal((long) -(list.size() + 1), c.get(Calendar.DAY_OF_MONTH), ab.getDocDivers().getNumPiece(), ab.getDocDivers().getNumPiece() + "-0" + (list.size() + 1), libelle, 0, a.getTotal(), ab.getEcheance(), ab.getDocDivers().getCompteGeneral(), 0L, ab.getDocDivers().getTableTiers(), ab.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 2);
                    if (ab.getDocDivers().getCompteGeneral().getSaisieAnalytique()) {
                        cc.setAnalytiques(buildSectionAnalytique(ab, cc, secs));
                    }
                    ccs.add(cc);
                    cd = new YvsComptaContentJournal((long) -(list.size() + 1), c.get(Calendar.DAY_OF_MONTH), ab.getDocDivers().getNumPiece(), ab.getDocDivers().getNumPiece() + "-0" + (list.size() + 1), libelle, (a.getTotal() + a.getCouts()), 0, new Date(), compte, 0L, ab.getDocDivers().getTableTiers(), ab.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 1);
                    cds.add(cd);
                    break;
                }
                default: {
                    cd = new YvsComptaContentJournal((long) -(list.size() + 1), c.get(Calendar.DAY_OF_MONTH), ab.getDocDivers().getNumPiece(), ab.getDocDivers().getNumPiece() + "-0" + (list.size() + 1), libelle, a.getTotal(), 0, c.getTime(), ab.getDocDivers().getCompteGeneral(), 0L, ab.getDocDivers().getTableTiers(), ab.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 1);
                    if (ab.getDocDivers().getCompteGeneral().getSaisieAnalytique()) {
                        cd.setAnalytiques(buildSectionAnalytique(ab, cd, secs));
                    }
                    cds.add(cd);
                    cc = new YvsComptaContentJournal((long) -(list.size() + 1), c.get(Calendar.DAY_OF_MONTH), ab.getDocDivers().getNumPiece(), ab.getDocDivers().getNumPiece() + "-0" + (list.size() + 1), libelle, 0, (a.getTotal() + a.getCouts()), new Date(), compte, 0L, ab.getDocDivers().getTableTiers(), ab.getId(), Constantes.SCR_ABONNEMENT_DIVERS, 2);
                    ccs.add(cc);
                    break;
                }
            }
            list.addAll(cds);
            list.addAll(ccs);
            System.err.println("list" + list);
            return list;
        }
        return null;
    }

    public List<YvsComptaContentJournal> buildDiversToComptabilise(long id, List<YvsComptaAbonementDocDivers> abonnements, List<YvsComptaCentreDocDivers> secs, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, DaoInterfaceLocal dao) {
        dao.setRESULT(null);
        YvsComptaCaisseDocDivers y = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{id});
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getCompteGeneral() != null ? (y.getCompteGeneral().getId() != null ? y.getCompteGeneral().getId() < 1 : true) : true) {
                dao.setRESULT("Aucun compte de charge n'a été trouvé ");
                return null;
            }
            int variante = varianteDivers(y, abonnements);
            List<YvsComptaContentJournal> list = new ArrayList<>();
            switch (variante) {
                case 1: {// Comptabilisation compte general / compte caisse
                    list = build1DiversToComptabilise(y, secs, taxs, couts, dao);
                    break;
                }
                case 2: {// Comptabilisation compte general / compte tiers | compte tiers / compte caisse
                    list = build2DiversToComptabilise(y, secs, taxs, couts, dao);
                    break;
                }
                case 3: {// Comptabilisation compte abonnement / compte tiers | compte tiers / compte caisse | compte general / compte abonnement
                    list = build3DiversToComptabilise(y, secs, taxs, couts, dao);
                    break;
                }
                case 4: {// Comptabilisation compte abonnement / compte caisse | compte general / compte abonnement
                    list = build4DiversToComptabilise(y, secs, taxs, couts, dao);
                    break;
                }
                case 5: {// Comptabilisation compte general (code tiers) / compte caisse
                    list = build5DiversToComptabilise(y, secs, taxs, couts, dao);
                    break;
                }
            }
            return list;
        }
        return null;
    }

    private List<YvsComptaContentJournal> build1DiversToComptabilise(YvsComptaCaisseDocDivers y, List<YvsComptaCentreDocDivers> secs, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, DaoInterfaceLocal dao) {
        dao.setRESULT(null);
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getCompteGeneral() != null ? (y.getCompteGeneral().getId() != null ? y.getCompteGeneral().getId() < 1 : true) : true) {
                dao.setRESULT("Aucun compte de charge n'a été trouvé ");
                return null;
            }
            List<YvsComptaCaissePieceDivers> pieces = dao.loadNameQueries("YvsComptaCaissePieceDivers.findByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
            boolean error = true;
            if (pieces != null ? !pieces.isEmpty() : false) {
                double montant = 0;
                for (YvsComptaCaissePieceDivers p : pieces) {
                    montant += p.getMontant();
                }
                if (montant >= y.getMontantTotal()) {
                    error = false;
                }
            }
            if (error) {
                dao.setRESULT("ce document n'est pas encore réglé");
                return null;
            }
            List<YvsComptaContentJournal> cds = new ArrayList<>();
            List<YvsComptaContentJournal> ccs = new ArrayList<>();
            List<YvsComptaCaissePieceCoutDivers> piecesCout = new ArrayList<>();

            YvsComptaContentJournal cd;
            YvsComptaContentJournal cc;

            String libelle = y.getLibelleComptable();
            if (libelle != null ? libelle.trim().length() < 1 : true) {
                libelle = y.getDescription().substring(0, y.getDescription().length() > 50 ? 50 : y.getDescription().length());
            }
            if (libelle != null ? libelle.trim().length() < 1 : true) {
                libelle = y.getNumPiece();
            }
            List<YvsComptaContentJournal> list = new ArrayList<>();
            for (YvsComptaCaissePieceDivers p : pieces) {
                YvsComptaPiecesComptable a = buildAnnexeDivers(p, taxs, couts, dao);
                if (a == null) {
                    return null;
                }
                cds = new ArrayList<>(a.getContentsDebit());
                ccs = new ArrayList<>(a.getContentsCredit());
                piecesCout = new ArrayList<>(a.getPiecesCout());
                Calendar c = Calendar.getInstance();
                c.setTime(p.getDateValider());
                switch (y.getMouvement()) {
                    case Constantes.MOUV_CAISS_ENTREE: {
                        cc = new YvsComptaContentJournal((long) -(ccs.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (ccs.size() + 1), libelle, 0, a.getTotal(), c.getTime(), y.getCompteGeneral(), 0L, y.getTableTiers(), p.getId(), Constantes.SCR_CAISSE_DIVERS, 2);
                        cc.getAnalytiques().addAll(buildSectionAnalytique(p, cc, secs));
                        ccs.add(cc);

                        if (!piecesCout.contains(p.getPieceCout())) {
                            if (p.getCaisse() == null) {
                                dao.setRESULT("Aucune caisse n'a été trouvé pour le réglement N° [" + p.getNumPiece() + "]");
                                return null;
                            }
                            if (p.getCaisse().getCompte() == null) {
                                dao.setRESULT("Aucune compte n'a été trouvé pour la caisse du réglement N° [" + p.getNumPiece() + "]");
                                return null;
                            }
                            cd = new YvsComptaContentJournal((long) -(cds.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), p.getNumPiece(), "Réglement " + p.getNumPiece() + " sur " + libelle, p.getMontant(), 0, c.getTime(), p.getCaisse().getCompte(), 0L, y.getTableTiers(), p.getId(), Constantes.SCR_CAISSE_DIVERS, 1);
                            cds.add(cd);
                        }
                        break;
                    }
                    default: {
                        cd = new YvsComptaContentJournal((long) -(cds.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (cds.size() + 1), libelle, a.getTotal(), 0, c.getTime(), y.getCompteGeneral(), 0L, y.getTableTiers(), p.getId(), Constantes.SCR_CAISSE_DIVERS, 1);
                        cd.getAnalytiques().addAll(buildSectionAnalytique(p, cd, secs));
                        cds.add(cd);

                        if (!piecesCout.contains(p.getPieceCout())) {
                            if (p.getCaisse() == null) {
                                dao.setRESULT("Aucune caisse n'a été trouvé pour le réglement N° [" + p.getNumPiece() + "]");
                                return null;
                            }
                            if (p.getCaisse().getCompte() == null) {
                                dao.setRESULT("Aucune compte n'a été trouvé pour la caisse du réglement N° [" + p.getNumPiece() + "]");
                                return null;
                            }
                            cc = new YvsComptaContentJournal((long) -(ccs.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), p.getNumPiece(), "Réglement " + p.getNumPiece() + " sur " + libelle, 0, p.getMontant(), c.getTime(), p.getCaisse().getCompte(), 0L, y.getTableTiers(), p.getId(), Constantes.SCR_CAISSE_DIVERS, 2);
                            ccs.add(cc);
                        }
                        break;
                    }
                }
                list.addAll(cds);
                list.addAll(ccs);
            }
            return list;
        }
        return null;
    }

    private List<YvsComptaContentJournal> build2DiversToComptabilise(YvsComptaCaisseDocDivers y, List<YvsComptaCentreDocDivers> secs, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, DaoInterfaceLocal dao) {
        dao.setRESULT(null);
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getCompteGeneral() != null ? (y.getCompteGeneral().getId() != null ? y.getCompteGeneral().getId() < 1 : true) : true) {
                dao.setRESULT("Aucun compte de charge n'a été trouvé ");
                return null;
            }
            YvsBasePlanComptable compte = null;
            try {
                if (y.getIdTiers() != null ? y.getIdTiers() < 1 : true) {
                    dao.setRESULT("Comptabilisation impossible...car vous devez preciser le compte tiers");
                    return null;
                }
                Long numero = Long.valueOf(dao.nameTiers(y.getIdTiers(), y.getTableTiers(), "C"));
                compte = (YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findById", new String[]{"id"}, new Object[]{numero});
            } catch (NumberFormatException ex) {
            }
            if (compte != null ? compte.getId() < 1 : true) {
                dao.setRESULT("Comptabilisation impossible...car vous devez le tiers de ce document n'a pas de compte collectif");
                return null;
            }

            YvsComptaPiecesComptable a = buildAnnexeDivers(y, taxs, couts, dao);
            if (a == null) {
                return null;
            }
            List<YvsComptaContentJournal> cds = new ArrayList<>(a.getContentsDebit());
            List<YvsComptaContentJournal> ccs = new ArrayList<>(a.getContentsCredit());
            List<YvsComptaCaissePieceCoutDivers> piecesCout = new ArrayList<>(a.getPiecesCout());

            Calendar c = Calendar.getInstance();
            c.setTime(y.getDateDoc());

            YvsComptaContentJournal cd;
            YvsComptaContentJournal cc;

            YvsComptaContentAnalytique se;

            String libelle = y.getLibelleComptable();
            if (libelle != null ? libelle.trim().length() < 1 : true) {
                libelle = y.getDescription().substring(0, y.getDescription().length() > 50 ? 50 : y.getDescription().length());
            }
            if (libelle != null ? libelle.trim().length() < 1 : true) {
                libelle = y.getNumPiece();
            }

            switch (y.getMouvement()) {
                case Constantes.MOUV_CAISS_ENTREE: {
                    cc = new YvsComptaContentJournal((long) -(ccs.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (ccs.size() + 1), libelle, 0, a.getTotal(), new Date(), y.getCompteGeneral(), 0L, y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 2);
                    cc.getAnalytiques().addAll(buildSectionAnalytique(y, cc, secs));
                    ccs.add(cc);
                    cd = new YvsComptaContentJournal((long) -(cds.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (cds.size() + 1), libelle, y.getMontant() + a.getCouts(), 0, new Date(), compte, y.getIdTiers(), y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 1);
                    cds.add(cd);
                    break;
                }
                default: {
                    cd = new YvsComptaContentJournal((long) -(cds.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (cds.size() + 1), libelle, a.getTotal(), 0, new Date(), y.getCompteGeneral(), 0L, y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 1);
                    cd.getAnalytiques().addAll(buildSectionAnalytique(y, cd, secs));
                    cds.add(cd);
                    cc = new YvsComptaContentJournal((long) -(ccs.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (ccs.size() + 1), libelle, 0, y.getMontant() + a.getCouts(), new Date(), compte, y.getIdTiers(), y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 2);
                    ccs.add(cc);
                    break;
                }
            }
            List<YvsComptaContentJournal> list = new ArrayList<>();
            list.addAll(cds);
            list.addAll(ccs);
            return list;
        }
        return null;
    }

    private List<YvsComptaContentJournal> build3DiversToComptabilise(YvsComptaCaisseDocDivers y, List<YvsComptaCentreDocDivers> secs, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, DaoInterfaceLocal dao) {
        dao.setRESULT(null);
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            YvsBasePlanComptable compteGeneral = (YvsBasePlanComptable) dao.loadOneByNameQueries("YvsComptaAbonementDocDivers.findCompteByDocDivers", new String[]{"docDivers"}, new Object[]{y});
            if (compteGeneral != null ? (compteGeneral.getId() != null ? compteGeneral.getId() < 1 : true) : true) {
                compteGeneral = (YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findByTypeNature", new String[]{"societe", "nature"}, new Object[]{currentScte, Constantes.NAT_ABONNEMENT});
                if (compteGeneral != null ? (compteGeneral.getId() != null ? compteGeneral.getId() < 1 : true) : true) {
                    dao.setRESULT("Comptabilisation impossible...car le compte des abonnements n'est pas paramètré");
                    return null;
                }
            }
            YvsBasePlanComptable compte = null;
            try {
                if (y.getIdTiers() != null ? y.getIdTiers() < 1 : true) {
                    dao.setRESULT("Comptabilisation impossible...car vous devez preciser le compte tiers");
                    return null;
                }
                Long numero = Long.valueOf(dao.nameTiers(y.getIdTiers(), y.getTableTiers(), "C"));
                compte = (YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findById", new String[]{"id"}, new Object[]{numero});
            } catch (NumberFormatException ex) {
            }
            if (compte != null ? compte.getId() < 1 : true) {
                dao.setRESULT("Comptabilisation impossible...car vous devez le tiers de ce document n'a pas de compte collectif");
                return null;
            }

            YvsComptaPiecesComptable a = buildAnnexeDivers(y, taxs, couts, dao);
            if (a == null) {
                return null;
            }
            List<YvsComptaContentJournal> cds = new ArrayList<>(a.getContentsDebit());
            List<YvsComptaContentJournal> ccs = new ArrayList<>(a.getContentsCredit());

            Calendar c = Calendar.getInstance();
            c.setTime(y.getDateDoc());

            YvsComptaContentJournal cd;
            YvsComptaContentJournal cc;

            String libelle = y.getLibelleComptable();
            if (libelle != null ? libelle.trim().length() < 1 : true) {
                libelle = y.getDescription().substring(0, y.getDescription().length() > 50 ? 50 : y.getDescription().length());
            }
            if (libelle != null ? libelle.trim().length() < 1 : true) {
                libelle = y.getNumPiece();
            }

            switch (y.getMouvement()) {
                case Constantes.MOUV_CAISS_ENTREE: {
                    System.err.println("--- Here....");
                    cc = new YvsComptaContentJournal((long) -(ccs.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (ccs.size() + 1), libelle, 0, a.getTotal(), new Date(), compteGeneral, 0L, y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 2);
                    if (compteGeneral.getSaisieAnalytique()) {
                        cc.getAnalytiques().addAll(buildSectionAnalytique(y, cc, secs));
                    }
                    ccs.add(cc);
                    cd = new YvsComptaContentJournal((long) -(cds.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (cds.size() + 1), libelle, y.getMontant() + a.getCouts(), 0, new Date(), compte, y.getIdTiers(), y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 1);
                    cds.add(cd);
                    break;
                }
                default: {
                    cd = new YvsComptaContentJournal((long) -(cds.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (cds.size() + 1), libelle, a.getTotal(), 0, new Date(), compteGeneral, 0L, y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 1);
                    System.err.println("Cmpte Ab --- " + compteGeneral.getNumCompte());
                    if (compteGeneral.getSaisieAnalytique()) {
                        System.err.println("Cmpte Ab IS SAISIE ANAL--- ");
                        cd.getAnalytiques().addAll(buildSectionAnalytique(y, cd, secs));
                    }
                    cds.add(cd);
                    cc = new YvsComptaContentJournal((long) -(ccs.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (ccs.size() + 1), libelle, 0, y.getMontant() + a.getCouts(), new Date(), compte, y.getIdTiers(), y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 2);
                    ccs.add(cc);
                    break;
                }
            }

            List<YvsComptaContentJournal> list = new ArrayList<>();
            list.addAll(cds);
            list.addAll(ccs);
            return list;
        }
        return null;
    }

    private List<YvsComptaContentJournal> build4DiversToComptabilise(YvsComptaCaisseDocDivers y, List<YvsComptaCentreDocDivers> secs, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, DaoInterfaceLocal dao) {
        dao.setRESULT(null);
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            YvsBasePlanComptable compteGeneral = (YvsBasePlanComptable) dao.loadOneByNameQueries("YvsComptaAbonementDocDivers.findCompteByDocDivers", new String[]{"docDivers"}, new Object[]{y});
            if (compteGeneral != null ? (compteGeneral.getId() != null ? compteGeneral.getId() < 1 : true) : true) {
                compteGeneral = (YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findByTypeNature", new String[]{"societe", "nature"}, new Object[]{currentScte, Constantes.NAT_ABONNEMENT});
                if (compteGeneral != null ? (compteGeneral.getId() != null ? compteGeneral.getId() < 1 : true) : true) {
                    dao.setRESULT("Comptabilisation impossible...car le compte des abonnements n'est pas paramètré");
                    return null;
                }
            }

            List<YvsComptaCaissePieceDivers> pieces = dao.loadNameQueries("YvsComptaCaissePieceDivers.findByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
            boolean error = true;
            if (pieces != null ? !pieces.isEmpty() : false) {
                double montant = 0;
                for (YvsComptaCaissePieceDivers p : pieces) {
                    montant += p.getMontant();
                }
                if (montant >= y.getMontantTotal()) {
                    error = false;
                }
            }
            if (error) {
                dao.setRESULT("ce document n'est pas encore réglé");
                return null;
            }

            YvsComptaPiecesComptable a = buildAnnexeDivers(y, taxs, couts, dao);
            if (a == null) {
                return null;
            }
            List<YvsComptaContentJournal> cds = new ArrayList<>(a.getContentsDebit());
            List<YvsComptaContentJournal> ccs = new ArrayList<>(a.getContentsCredit());
            List<YvsComptaCaissePieceCoutDivers> piecesCout = new ArrayList<>(a.getPiecesCout());

            Calendar c = Calendar.getInstance();
            c.setTime(y.getDateDoc());

            YvsComptaContentJournal cd;
            YvsComptaContentJournal cc;

            String libelle = y.getLibelleComptable();
            if (libelle != null ? libelle.trim().length() < 1 : true) {
                libelle = y.getDescription().substring(0, y.getDescription().length() > 50 ? 50 : y.getDescription().length());
            }
            if (libelle != null ? libelle.trim().length() < 1 : true) {
                libelle = y.getNumPiece();
            }

            switch (y.getMouvement()) {
                case Constantes.MOUV_CAISS_ENTREE: {
                    cc = new YvsComptaContentJournal((long) -(ccs.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (ccs.size() + 1), libelle, 0, a.getTotal(), new Date(), compteGeneral, 0L, y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 2);
                    if (compteGeneral.getSaisieAnalytique()) {
                        cc.getAnalytiques().addAll(buildSectionAnalytique(y, cc, secs));
                    }
                    ccs.add(cc);
                    for (YvsComptaCaissePieceDivers p : pieces) {
                        if (p.getPieceCout() != null ? p.getPieceCout().getId() > 0 ? !piecesCout.contains(p.getPieceCout()) : true : true) {
                            if (p.getCaisse() == null) {
                                dao.setRESULT("Aucune caisse n'a été trouvé pour le réglement N° [" + p.getNumPiece() + "]");
                                return null;
                            }
                            if (p.getCaisse().getCompte() == null) {
                                dao.setRESULT("Aucune compte n'a été trouvé pour la caisse du réglement N° [" + p.getNumPiece() + "]");
                                return null;
                            }
                            cd = new YvsComptaContentJournal((long) -(cds.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), p.getNumPiece(), "Réglement " + p.getNumPiece() + " sur " + libelle, p.getMontant(), 0, new Date(), p.getCaisse().getCompte(), 0L, y.getTableTiers(), p.getId(), Constantes.SCR_CAISSE_DIVERS, 1);
                            cds.add(cd);
                        }
                    }
                    break;
                }
                default: {
                    cd = new YvsComptaContentJournal((long) -(cds.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (cds.size() + 1), libelle, a.getTotal(), 0, new Date(), compteGeneral, 0L, y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 1);
                    if (compteGeneral.getSaisieAnalytique()) {
                        cd.getAnalytiques().addAll(buildSectionAnalytique(y, cd, secs));
                    }
                    cds.add(cd);
                    for (YvsComptaCaissePieceDivers p : pieces) {
                        if (p.getPieceCout() != null ? p.getPieceCout().getId() > 0 ? !piecesCout.contains(p.getPieceCout()) : true : true) {
                            if (p.getCaisse() == null) {
                                dao.setRESULT("Aucune caisse n'a été trouvé pour le réglement N° [" + p.getNumPiece() + "]");
                                return null;
                            }
                            if (p.getCaisse().getCompte() == null) {
                                dao.setRESULT("Aucune compte n'a été trouvé pour la caisse du réglement N° [" + p.getNumPiece() + "]");
                                return null;
                            }
                            cc = new YvsComptaContentJournal((long) -(ccs.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), p.getNumPiece(), "Réglement " + p.getNumPiece() + " sur " + libelle, 0, p.getMontant(), new Date(), p.getCaisse().getCompte(), 0L, y.getTableTiers(), p.getId(), Constantes.SCR_CAISSE_DIVERS, 2);
                            ccs.add(cc);
                        }
                    }
                    break;
                }
            }
            List<YvsComptaContentJournal> list = new ArrayList<>();
            list.addAll(cds);
            list.addAll(ccs);
            return list;
        }
        return null;
    }

    private List<YvsComptaContentJournal> build5DiversToComptabilise(YvsComptaCaisseDocDivers y, List<YvsComptaCentreDocDivers> secs, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, DaoInterfaceLocal dao) {
        dao.setRESULT(null);
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getCompteGeneral() != null ? (y.getCompteGeneral().getId() != null ? y.getCompteGeneral().getId() < 1 : true) : true) {
                dao.setRESULT("Aucun compte de charge n'a été trouvé ");
                return null;
            }
            if (y.getIdTiers() != null ? y.getIdTiers() < 1 : true) {
                dao.setRESULT("Comptabilisation impossible...car vous devez preciser le compte tiers");
                return null;
            }

            List<YvsComptaCaissePieceDivers> pieces = dao.loadNameQueries("YvsComptaCaissePieceDivers.findByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
            boolean error = true;
            if (pieces != null ? !pieces.isEmpty() : false) {
                double montant = 0;
                for (YvsComptaCaissePieceDivers p : pieces) {
                    montant += p.getMontant();
                }
                if (montant >= y.getMontantTotal()) {
                    error = false;
                }
            }
            if (error) {
                dao.setRESULT("ce document n'est pas encore réglé");
                return null;
            }

            YvsComptaPiecesComptable a = buildAnnexeDivers(y, taxs, couts, dao);
            if (a == null) {
                return null;
            }
            List<YvsComptaContentJournal> cds = new ArrayList<>(a.getContentsDebit());
            List<YvsComptaContentJournal> ccs = new ArrayList<>(a.getContentsCredit());
            List<YvsComptaCaissePieceCoutDivers> piecesCout = new ArrayList<>(a.getPiecesCout());

            Calendar c = Calendar.getInstance();
            c.setTime(y.getDateDoc());

            YvsComptaContentJournal cd;
            YvsComptaContentJournal cc;

            String libelle = y.getLibelleComptable();
            if (libelle != null ? libelle.trim().length() < 1 : true) {
                libelle = y.getDescription().substring(0, y.getDescription().length() > 50 ? 50 : y.getDescription().length());
            }
            if (libelle != null ? libelle.trim().length() < 1 : true) {
                libelle = y.getNumPiece();
            }

            switch (y.getMouvement()) {
                case Constantes.MOUV_CAISS_ENTREE: {
                    cc = new YvsComptaContentJournal((long) -(ccs.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (ccs.size() + 1), libelle, 0, a.getTotal(), new Date(), y.getCompteGeneral(), y.getIdTiers(), y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 2);
                    cc.getAnalytiques().addAll(buildSectionAnalytique(y, cc, secs));
                    ccs.add(cc);
                    for (YvsComptaCaissePieceDivers p : pieces) {
                        if (!piecesCout.contains(p.getPieceCout())) {
                            if (p.getCaisse() == null) {
                                dao.setRESULT("Aucune caisse n'a été trouvé pour le réglement N° [" + p.getNumPiece() + "]");
                                return null;
                            }
                            if (p.getCaisse().getCompte() == null) {
                                dao.setRESULT("Aucune compte n'a été trouvé pour la caisse du réglement N° [" + p.getNumPiece() + "]");
                                return null;
                            }
                            cd = new YvsComptaContentJournal((long) -(cds.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), p.getNumPiece(), "Réglement " + p.getNumPiece() + " sur " + libelle, p.getMontant(), 0, new Date(), p.getCaisse().getCompte(), 0L, y.getTableTiers(), p.getId(), Constantes.SCR_CAISSE_DIVERS, 1);
                            cds.add(cd);
                        }
                    }
                    break;
                }
                default: {
                    cd = new YvsComptaContentJournal((long) -(cds.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), y.getNumPiece() + "-0" + (cds.size() + 1), libelle, a.getTotal(), 0, new Date(), y.getCompteGeneral(), y.getIdTiers(), y.getTableTiers(), y.getId(), Constantes.SCR_DIVERS, 1);
                    cd.getAnalytiques().addAll(buildSectionAnalytique(y, cd, secs));
                    cds.add(cd);
                    for (YvsComptaCaissePieceDivers p : pieces) {
                        if (!piecesCout.contains(p.getPieceCout())) {
                            if (p.getCaisse() == null) {
                                dao.setRESULT("Aucune caisse n'a été trouvé pour le réglement N° [" + p.getNumPiece() + "]");
                                return null;
                            }
                            if (p.getCaisse().getCompte() == null) {
                                dao.setRESULT("Aucune compte n'a été trouvé pour la caisse du réglement N° [" + p.getNumPiece() + "]");
                                return null;
                            }
                            cc = new YvsComptaContentJournal((long) -(ccs.size() + 1), c.get(Calendar.DAY_OF_MONTH), y.getNumPiece(), p.getNumPiece(), "Réglement " + p.getNumPiece() + " sur " + libelle, 0, p.getMontant(), new Date(), p.getCaisse().getCompte(), 0L, y.getTableTiers(), p.getId(), Constantes.SCR_CAISSE_DIVERS, 2);
                            ccs.add(cc);
                        }
                    }
                    break;
                }
            }
            List<YvsComptaContentJournal> list = new ArrayList<>();
            list.addAll(cds);
            list.addAll(ccs);
            return list;
        }
        return null;
    }

    public boolean comptabiliserDivers(YvsComptaCaisseDocDivers y, DaoInterfaceLocal dao) {
        List<YvsComptaContentJournal> list = buildDiversToComptabilise(y.getId(), dao);
        if (list != null ? list.isEmpty() : true) {
            return false;
        }
        return comptabiliserDivers(y, list, dao);
    }

    public boolean comptabiliserDivers(YvsComptaCaisseDocDivers y, List<YvsComptaContentJournal> contenus, DaoInterfaceLocal dao) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_DIVERS);
                if (comptabilise) {
                    dao.setRESULT("Comptabilisation impossible... car ce document est déjà comptabilisée");
                    return false;
                }
                YvsComptaPiecesComptable p = majComptaDivers(y.getId(), contenus, dao);
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (reponse) {
                    y.setComptabilised(true);
                }
                return reponse;
            } else {
                dao.setRESULT("Comptabilisation impossible... car cette opération n'est pas validée");
            }
        }
        return false;
    }

    public YvsComptaPiecesComptable majComptaDivers(long id, List<YvsComptaContentJournal> contenus, DaoInterfaceLocal dao) {
        YvsComptaCaisseDocDivers y = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{id});
        return majComptaDivers(y, y.getDateDoc(), contenus, dao);
    }

    public YvsComptaPiecesComptable majComptaDivers(YvsComptaCaisseDocDivers y, Date datePiece, List<YvsComptaContentJournal> contenus, DaoInterfaceLocal dao) {
        YvsComptaPiecesComptable p = null;
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                champ = new String[]{"agence", "type", "default"};
                val = new Object[]{currentAgence, Constantes.TRESORERIE, true};
                YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    YvsBaseExercice exo = giveExerciceActif(y.getDateDoc(), dao);
                    if (exo != null ? (exo.getId() != null ? exo.getId() > 0 : false) : false) {
                        return dao.saveNewPieceComptable(datePiece, jrn, contenus, currentUser);
                    } else {
                        dao.setRESULT("Comptabilisation impossible...car l'exercice à cette date n'existe pas ou n'est pas actif");
                    }
                } else {
                    dao.setRESULT("Comptabilisation impossible...car le journal par defaut de trésorerie n'existe pas");
                }
            }
        }
        return p;
    }
    /*BEGIN COMPTABILISATION*/
}

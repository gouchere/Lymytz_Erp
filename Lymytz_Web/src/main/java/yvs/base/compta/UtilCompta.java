/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import yvs.base.CodeAcces;
import yvs.base.tiers.Tiers;
import yvs.base.tresoreri.ModePaiement;
import yvs.commercial.UtilCom;
import yvs.commercial.achat.ContenuDocAchat;
import yvs.commercial.param.TypeDocDivers;
import yvs.commercial.vente.DocVente;
import yvs.commercial.vente.MensualiteFactureVente;
import yvs.comptabilite.ContentAnalytique;
import yvs.comptabilite.ContentModeleSaisie;
import yvs.comptabilite.ContentPieceCompta;
import yvs.comptabilite.ModelesSasie;
import yvs.comptabilite.ParametreCompta;
import yvs.comptabilite.PiecesCompta;
import yvs.comptabilite.analytique.CentreContenuAchat;
import yvs.comptabilite.analytique.CentreCoutVirement;
import yvs.comptabilite.analytique.CentreDocDivers;
import yvs.comptabilite.analytique.CentreMission;
import yvs.comptabilite.caisse.Caisses;
import yvs.comptabilite.caisse.PlanDecoupage;
import yvs.comptabilite.client.AcompteClient;
import yvs.comptabilite.client.AcomptesVenteDivers;
import yvs.comptabilite.client.CreditClient;
import yvs.comptabilite.client.ReglementCredit;
import yvs.comptabilite.fournisseur.AcompteFournisseur;
import yvs.comptabilite.fournisseur.AcomptesAchatDivers;
import yvs.comptabilite.fournisseur.CreditFournisseur;
import yvs.comptabilite.tresorerie.AbonnementDocDivers;
import yvs.comptabilite.tresorerie.BielletagePc;
import yvs.comptabilite.tresorerie.BonProvisoire;
import yvs.comptabilite.tresorerie.CoutSupDocDivers;
import yvs.comptabilite.tresorerie.CoutSupVirement;
import yvs.comptabilite.tresorerie.DocCaissesDivers;
import yvs.comptabilite.tresorerie.JustifierBon;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.comptabilite.tresorerie.TaxeDocDivers;
import yvs.comptabilite.tresorerie.TiersDivers;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.commission.YvsComCommissionCommerciaux;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBaseNatureCompte;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsBaseRadicalCompte;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.compta.YvsComptaAbonementDocDivers;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaBielletage;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceCommission;
import yvs.entity.compta.YvsComptaCaissePieceMission;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaContentModeleSaisi;
import yvs.entity.compta.YvsComptaCoutSupPieceVirement;
import yvs.entity.compta.YvsComptaCreditClient;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaJustifBonMission;
import yvs.entity.compta.YvsComptaModeleSaisie;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.YvsComptaNotifReglementDocDivers;
import yvs.entity.compta.YvsComptaNotifReglementVente;
import yvs.entity.compta.YvsComptaParametre;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.YvsComptaPlanAbonnement;
import yvs.entity.compta.YvsComptaReglementCreditClient;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.entity.compta.achat.YvsComptaCreditFournisseur;
import yvs.entity.compta.achat.YvsComptaNotifReglementAchat;
import yvs.entity.compta.achat.YvsComptaReglementCreditFournisseur;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.compta.analytique.YvsComptaCentreContenuAchat;
import yvs.entity.compta.analytique.YvsComptaCentreCoutVirement;
import yvs.entity.compta.analytique.YvsComptaCentreMission;
import yvs.entity.compta.analytique.YvsComptaPlanAnalytique;
import yvs.entity.compta.analytique.YvsComptaRepartitionAnalytique;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaisseDocDiversTiers;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.divers.YvsComptaCentreDocDivers;
import yvs.entity.compta.divers.YvsComptaCoutSupDocDivers;
import yvs.entity.compta.divers.YvsComptaJustifBonAchat;
import yvs.entity.compta.divers.YvsComptaJustificatifBon;
import yvs.entity.compta.divers.YvsComptaTaxeDocDivers;
import yvs.entity.compta.saisie.YvsComptaContentAnalytique;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsModePaiement;
import yvs.entity.param.YvsSocietes;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.UtilGrh;
import yvs.mutuelle.UtilMut;
import yvs.parametrage.agence.UtilAgence;
import yvs.production.UtilProd;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Constantes;

/**
 *
 * @author hp Elite 8300
 */
public class UtilCompta {
    
    public static ParametreCompta buildBeanParametre(YvsComptaParametre y) {
        ParametreCompta p = new ParametreCompta();
        if (y != null) {
            p.setId(y.getId());
            p.setDecimalArrondi(y.getDecimalArrondi());
            p.setModeArrondi(y.getModeArrondi());
            p.setMultipleArrondi(y.getMultipleArrondi());
            p.setTailleCompte(y.getTailleCompte());
            p.setValeurArrondi(y.getValeurArrondi());
            p.setConverter(y.getConverter());
            p.setMajComptaAutoDivers(y.getMajComptaAutoDivers());
            p.setMajComptaStatutDivers(y.getMajComptaStatutDivers());
            p.setDateSave(y.getDateSave());
            p.setMontantSeuilOd(y.getMontantSeuilDepenseOd());
            p.setMontantSeuilRecetteOd(y.getMontantSeuilRecetteOd());
            p.setValeurLimiteArrondi(y.getValeurLimiteArrondi());
            p.setEcartDaySoldeClient(y.getEcartDaySoldeClient());
            p.setNombreLigneSoldeClient(y.getNombreLigneSoldeClient());
            p.setJourAnterieur(y.getJourAnterieur());
            p.setJourAnterieurCancel(y.getJourAnterieurCancel());
            p.setReportByAgence(y.getReportByAgence());
            p.setCompteBeneficeReport(buildBeanCompte(y.getCompteBeneficeReport()));
            p.setComptePerteReport(buildBeanCompte(y.getComptePerteReport()));
            p.setJournalReport(buildBeanJournaux(y.getJournalReport()));
        }
        return p;
    }
    
    public static YvsComptaParametre buildParametre(ParametreCompta y, YvsSocietes ste, YvsUsersAgence ua) {
        YvsComptaParametre p = new YvsComptaParametre();
        if (y != null) {
            p.setId(y.getId());
            p.setDecimalArrondi(y.isDecimalArrondi());
            p.setModeArrondi(y.getModeArrondi());
            p.setMultipleArrondi(y.getMultipleArrondi());
            p.setTailleCompte(y.getTailleCompte());
            p.setValeurArrondi(y.getValeurArrondi());
            p.setConverter(y.getConverter());
            p.setMajComptaAutoDivers(y.isMajComptaAutoDivers());
            p.setMajComptaStatutDivers(y.getMajComptaStatutDivers());
            p.setMontantSeuilDepenseOd(y.getMontantSeuilOd());
            p.setMontantSeuilRecetteOd(y.getMontantSeuilRecetteOd());
            p.setValeurLimiteArrondi(y.getValeurLimiteArrondi());
            p.setEcartDaySoldeClient(y.getEcartDaySoldeClient());
            p.setNombreLigneSoldeClient(y.getNombreLigneSoldeClient());
            p.setJourAnterieur(y.getJourAnterieur());
            p.setJourAnterieurCancel(y.getJourAnterieurCancel());
            p.setReportByAgence(y.isReportByAgence());
            p.setSociete(ste);
            p.setAuthor(ua);
            p.setDateSave(y.getDateSave());
            p.setDateUpdate(new Date());
            if (y.getCompteBeneficeReport() != null ? y.getCompteBeneficeReport().getId() > 0 : false) {
                p.setCompteBeneficeReport(new YvsBasePlanComptable(y.getCompteBeneficeReport().getId(), y.getCompteBeneficeReport().getNumCompte(), y.getCompteBeneficeReport().getIntitule()));
            }
            if (y.getComptePerteReport() != null ? y.getComptePerteReport().getId() > 0 : false) {
                p.setComptePerteReport(new YvsBasePlanComptable(y.getComptePerteReport().getId(), y.getComptePerteReport().getNumCompte(), y.getComptePerteReport().getIntitule()));
            }
            if (y.getJournalReport() != null ? y.getJournalReport().getId() > 0 : false) {
                p.setJournalReport(new YvsComptaJournaux(y.getJournalReport().getId(), y.getJournalReport().getCodejournal()));
            }
        }
        return p;
    }
    
    public static PlanDecoupage buildBeanPlanAbonnement(YvsComptaPlanAbonnement y) {
        PlanDecoupage r = new PlanDecoupage();
        if (y != null) {
            r.setActif(y.getActif());
            r.setBasePlan(y.getTypeValeur());
            r.setId(y.getId());
            r.setPeriodicite(y.getPeriodicite());
            r.setReference(y.getReferencePlan());
            r.setValeur(y.getValeur());
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
            r.setCompte(buildSimpleBeanCompte(y.getCompte()));
        }
        return r;
    }
    
    public static YvsComptaPlanAbonnement buildBeanPlanAbonnement(PlanDecoupage y, YvsUsersAgence ua, YvsSocietes ste) {
        YvsComptaPlanAbonnement r = new YvsComptaPlanAbonnement();
        if (y != null) {
            r.setActif(y.isActif());
            r.setTypeValeur(y.getBasePlan());
            r.setId(y.getId());
            r.setPeriodicite(y.getPeriodicite());
            r.setReferencePlan(y.getReference());
            r.setValeur(y.getValeur());
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
            if (y.getCompte() != null ? y.getCompte().getId() > 0 : false) {
                r.setCompte(new YvsBasePlanComptable(y.getCompte().getId(), y.getCompte().getNumCompte(), y.getCompte().getIntitule()));
            }
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
            r.setSociete(ste);
            r.setNew_(true);
        }
        return r;
    }
    
    public static List<Comptes> buildBeanCompte(List<YvsBasePlanComptable> list) {
        List<Comptes> result = new ArrayList<>();
        for (YvsBasePlanComptable c : list) {
            result.add(buildBeanCompte(c));
        }
        return result;
    }
    
    public static Comptes buildSimpleBeanCompte(YvsBasePlanComptable cp) {
        Comptes c = new Comptes();
        if (cp != null) {
            c.setIntitule(cp.getIntitule());
            c.setId(cp.getId());
            c.setNumCompte(cp.getNumCompte());
            c.setSaisieAnalytique(cp.getSaisieAnalytique());
            c.setAbbreviation(cp.getAbbreviation());
            c.setActif(cp.getActif());
            c.setLettrable(cp.getLettrable());
            c.setSaisieCompteTiers(cp.getSaisieCompteTiers());
            c.setSaisieEcheance(cp.getSaisieEcheance());
            c.setVenteOnline(cp.getVenteOnline());
        }
        return c;
    }
    
    public static Comptes buildBeanCompte(YvsBasePlanComptable cp) {
        Comptes c = buildSimpleBeanCompte(cp);
        if (cp != null) {
            c.setCompteGeneral(buildSimpleBeanCompte(cp.getCompteGeneral()));
            c.setError(false);
            c.setAuteur(cp.getAuthor() != null ? cp.getAuthor().getUsers() != null ? cp.getAuthor().getUsers().getNomUsers() : "" : "");
            c.setNature(buildBeanNatureCompte(cp.getNatureCompte()));
            c.setSensCompte(cp.getSensCompte());
            c.setTypeCompte(cp.getTypeCompte());
            c.setTypeRepport(cp.getTypeReport());
            c.setDateSave(cp.getDateSave());
            c.setDateUpdate(cp.getDateUpdate());
        }
        return c;
    }
    
    public static YvsBasePlanComptable buildEntityCompte(Comptes cp) {
        YvsBasePlanComptable c = null;
        c = new YvsBasePlanComptable();
        c.setIntitule(cp.getIntitule());
        c.setId(cp.getId());
        c.setNumCompte(cp.getNumCompte());
        c.setActif(cp.isActif());
        c.setLettrable(cp.isLettrable());
        c.setSaisieCompteTiers(cp.isSaisieCompteTiers());
        c.setSaisieEcheance(cp.isSaisieEcheance());
        c.setSaisieAnalytique(cp.isSaisieAnalytique());
        c.setSensCompte(cp.getSensCompte());
        c.setAbbreviation(cp.getAbbreviation());
        c.setVenteOnline(cp.isVenteOnline());
        if (cp.getCompteGeneral() != null) {
            c.setCompteGeneral((cp.getCompteGeneral().getId() > 0) ? new YvsBasePlanComptable(cp.getCompteGeneral().getId()) : null);
        }
        c.setNatureCompte((cp.getNature().getId() > 0) ? new YvsBaseNatureCompte(cp.getNature().getId()) : null);
        c.setTypeReport(cp.getTypeRepport());
        c.setTypeCompte(cp.getTypeCompte());
        c.setDateSave(cp.getDateSave());
        c.setDateUpdate(new Date());
        return c;
    }
    
    public static PlanAnalytique buildBeanPlanAnalytique(YvsComptaPlanAnalytique ca) {
        PlanAnalytique c = new PlanAnalytique();
        if (ca != null) {
            c.setIntitule(ca.getIntitule());
            c.setId(ca.getId());
            c.setActif(ca.getActif());
            c.setCodePlan(ca.getCodePlan());
            c.setDescription(ca.getDescription());
            c.setDateSave(ca.getDateSave());
            c.setCentres(ca.getCentres());
        }
        return c;
    }
    
    public static YvsComptaPlanAnalytique buildPlanAnalytique(PlanAnalytique ca, YvsUsersAgence ua, YvsSocietes st) {
        YvsComptaPlanAnalytique c = new YvsComptaPlanAnalytique();
        if (ca != null) {
            c.setIntitule(ca.getIntitule());
            c.setId(ca.getId());
            c.setActif(ca.isActif());
            c.setCodePlan(ca.getCodePlan());
            c.setDescription(ca.getDescription());
            c.setDateUpdate(new Date());
            c.setDateSave(ca.getDateSave());
            c.setSociete(st);
            c.setAuthor(ua);
            c.setNew_(true);
        }
        return c;
    }
    
    public static CentreAnalytique buildBeanCentreAnalytique(YvsComptaCentreAnalytique ca) {
        CentreAnalytique c = new CentreAnalytique();
        if (ca != null) {
            c.setId(ca.getId());
            c.setCodeRef(ca.getCodeRef());
            c.setIntitule(ca.getDesignation());
            c.setDescription(ca.getDescription());
            c.setNiveau(ca.getNiveau());
            c.setType(ca.getTypeCentre());
            c.setUniteOeuvre(UtilProd.buildBeanUniteMesure(ca.getUniteOeuvre()));
            c.setActif(ca.getActif());
            c.setDateSave(ca.getDateSave());
            c.setPlan(buildBeanPlanAnalytique(ca.getPlan()));
            c.setRepartitions(ca.getRepartitions());
        }
        return c;
    }
    
    public static YvsComptaCentreAnalytique buildCentreAnalytique(CentreAnalytique ca, YvsUsersAgence ua) {
        YvsComptaCentreAnalytique c = new YvsComptaCentreAnalytique();
        if (ca != null) {
            c.setId(ca.getId());
            c.setCodeRef(ca.getCodeRef());
            c.setDesignation(ca.getIntitule());
            c.setDescription(ca.getDescription());
            c.setNiveau(ca.getNiveau());
            c.setTypeCentre(ca.getType());
            if (ca.getUniteOeuvre() != null ? ca.getUniteOeuvre().getId() > 0 : false) {
                c.setUniteOeuvre(new YvsBaseUniteMesure(ca.getUniteOeuvre().getId(), ca.getUniteOeuvre().getReference(), ca.getUniteOeuvre().getLibelle(), ca.getUniteOeuvre().getType()));
            }
            c.setDateSave(ca.getDateSave());
            c.setDateUpdate(new Date());
            if (ca.getPlan() != null ? ca.getPlan().getId() > 0 : false) {
                c.setPlan(new YvsComptaPlanAnalytique(ca.getPlan().getId(), ca.getPlan().getCodePlan(), ca.getPlan().getIntitule()));
            }
            c.setActif(ca.isActif());
            c.setAuthor(ua);
            c.setNew_(true);
        }
        return c;
    }
    
    public static LiaisonCentres buildBeanRepartition(YvsComptaRepartitionAnalytique ca) {
        LiaisonCentres c = new LiaisonCentres();
        if (ca != null) {
            c.setId(ca.getId());
            c.setCoefficient(ca.getTaux());
            c.setPrincipal(buildBeanCentreAnalytique(ca.getPrincipal()));
            c.setSecondaire(buildBeanCentreAnalytique(ca.getSecondaire()));
            c.setUnite(UtilProd.buildBeanUniteMesure(ca.getUnite()));
            c.setDateSave(ca.getDateSave());
        }
        return c;
    }
    
    public static YvsComptaRepartitionAnalytique buildRepartition(LiaisonCentres ca, YvsUsersAgence ua) {
        YvsComptaRepartitionAnalytique c = new YvsComptaRepartitionAnalytique();
        if (ca != null) {
            c.setId(ca.getId());
            c.setTaux(ca.getCoefficient());
            c.setDateUpdate(new Date());
            if (ca.getPrincipal() != null ? ca.getPrincipal().getId() > 0 : false) {
                c.setPrincipal(new YvsComptaCentreAnalytique(ca.getPrincipal().getId(), ca.getPrincipal().getCodeRef(), ca.getPrincipal().getIntitule()));
            }
            if (ca.getSecondaire() != null ? ca.getSecondaire().getId() > 0 : false) {
                c.setSecondaire(new YvsComptaCentreAnalytique(ca.getSecondaire().getId(), ca.getSecondaire().getCodeRef(), ca.getSecondaire().getIntitule()));
            }
            if (ca.getUnite() != null ? ca.getUnite().getId() > 0 : false) {
                c.setUnite(new YvsBaseUniteMesure(ca.getUnite().getId(), ca.getUnite().getReference(), ca.getUnite().getLibelle(), ca.getUnite().getType()));
            }
            c.setAuthor(ua);
            c.setDateSave(ca.getDateSave());
            c.setNew_(true);
        }
        return c;
    }
    
    public static CategorieComptable buildBeanCategorieComptable(YvsBaseCategorieComptable y) {
        CategorieComptable c = new CategorieComptable();
        if (y != null) {
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setCodeCategorie(y.getCode());
            c.setCodeAppel(y.getCodeAppel());
            c.setId(y.getId());
            c.setNature(y.getNature());
        }
        return c;
    }
    
    public static List<CategorieComptable> buildBeanListCategorieComptable(List<YvsBaseCategorieComptable> list) {
        List<CategorieComptable> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseCategorieComptable a : list) {
                r.add(buildBeanCategorieComptable(a));
            }
        }
        return r;
    }
    /*
     *
     *DEBUT GESTION TAXES
     *
     */
    
    public static Taxes buildBeanTaxes(YvsBaseTaxes y) {
        Taxes t = new Taxes();
        if (y != null) {
            t.setActif((y.getActif() != null) ? y.getActif() : false);
            t.setSupp((y.getSupp() != null) ? y.getSupp() : false);
            t.setCodeAppel(y.getCodeAppel());
            t.setCodeTaxe(y.getCodeTaxe());
            t.setLibellePrint(y.getLibellePrint());
            t.setDesignation(y.getDesignation());
            t.setId(y.getId());
            t.setTaux(y.getTaux());
            t.setModule(y.getModule());
            t.setCompte(UtilCompta.buildBeanCompte(y.getCompte()));
            t.setUpdate(true);
            t.setDateSave(y.getDateSave());
        }
        return t;
    }
    
    public static YvsBaseTaxes buildTaxes(Taxes y, YvsSocietes s, YvsUsersAgence ua) {
        YvsBaseTaxes t = new YvsBaseTaxes();
        if (y != null) {
            t.setActif(y.isActif());
            t.setSupp(y.isSupp());
            t.setCodeAppel(y.getCodeAppel());
            t.setCodeTaxe(y.getCodeTaxe());
            t.setLibellePrint(y.getLibellePrint());
            t.setDesignation(y.getDesignation());
            t.setId(y.getId());
            t.setModule(y.getModule());
            t.setTaux(y.getTaux());
            if (y.getCompte() != null ? y.getCompte().getId() > 0 : false) {
                t.setCompte(new YvsBasePlanComptable(y.getCompte().getId(), y.getCompte().getNumCompte()));
            }
            t.setSociete(s);
            t.setAuthor(ua);
            t.setDateSave(y.getDateSave());
            t.setDateUpdate(new Date());
        }
        return t;
    }
    
    public static List<Taxes> buildBeanListTaxes(List<YvsBaseTaxes> list) {
        List<Taxes> r = new ArrayList<>();
        for (YvsBaseTaxes a : list) {
            r.add(buildBeanTaxes(a));
        }
        return r;
    }
    
    public static Journaux buildBeanJournaux(YvsComptaJournaux y) {
        Journaux j = new Journaux();
        if (y != null) {
            j.setId(y.getId());
            j.setActif(y.getActif());
            j.setCodejournal(y.getCodeJournal());
            j.setIntitule(y.getIntitule());
            j.setTypeJournal(y.getTypeJournal());
            j.setDefaultFor(y.getDefaultFor());
            j.setDateSave(y.getDateSave());
            j.setDateUpdate(y.getDateUpdate());
            j.setAgence(UtilAgence.buildBeanAgence(y.getAgence()));
            j.setCodeAcces(y.getCodeAcces() != null ? y.getCodeAcces().getCode() : "");
        }
        return j;
    }
    
    public static YvsComptaJournaux buildBeanJournaux(Journaux y) {
        YvsComptaJournaux j = new YvsComptaJournaux();
        if (y != null) {
            j.setId(y.getId());
            j.setActif(y.isActif());
            j.setCodeJournal(y.getCodejournal());
            j.setIntitule(y.getIntitule());
            j.setTypeJournal(y.getTypeJournal());
            j.setDefaultFor(y.isDefaultFor());
            j.setDateSave(y.getDateSave());
            j.setDateUpdate(new Date());
            j.setAgence(new YvsAgences(y.getAgence().getId(), y.getAgence().getCodeAgence(), y.getAgence().getDesignation()));
        }
        return j;
    }
    
    public static ModeDeReglement buildBeanModeReglement(YvsBaseModeReglement y) {
        return UtilCom.buildBeanModeReglement(y);
    }
    
    public static List<ModeDeReglement> buildBeanListModelReglement(List<YvsBaseModeReglement> list) {
        List<ModeDeReglement> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseModeReglement a : list) {
                r.add(buildBeanModeReglement(a));
            }
        }
        return r;
    }
    
    public static List<Comptes> buildBeanListComptes(List<YvsBasePlanComptable> param) {
        List<Comptes> result = new ArrayList<>();
        if (param != null) {
            Comptes b;
            for (YvsBasePlanComptable param1 : param) {
                result.add(buildBeanCompte(param1));
            }
        }
        return result;
    }
    
    public static ModePaiement buildBeanModePaiement(YvsModePaiement y) {
        ModePaiement m = new ModePaiement();
        if (y != null) {
            m.setId(y.getId());
            m.setActif((y.getActif() != null) ? y.getActif() : false);
            m.setSupp((y.getSupp() != null) ? y.getSupp() : false);
            m.setTypePaiement(y.getTypePaiement());
        }
        return m;
    }
    
    public static List<ModePaiement> buildBeanListModePaiement(List<YvsModePaiement> list) {
        List<ModePaiement> r = new ArrayList<>();
        if (list != null) {
            for (YvsModePaiement a : list) {
                r.add(buildBeanModePaiement(a));
            }
        }
        return r;
    }
    
    public static NatureCompte buildBeanNatureCompte(YvsBaseNatureCompte nat) {
        NatureCompte r = new NatureCompte();
        if (nat != null) {
            r.setId(nat.getId());
            r.setDesignation(nat.getDesignation());
            if (nat.getRadicalsComptes() != null) {
                r.setRadicals(nat.getRadicalsComptes());
            } else {
                r.setRadicals(new ArrayList<YvsBaseRadicalCompte>());
            }
            r.setSaisieAnal(nat.getSaisieAnal());
            r.setSaisieCompteTiers(nat.getSaisieCompteTier());
            r.setSaisieEcheance(nat.getSaisieEcheance());
            r.setTypeReport(nat.getTypeReport());
            r.setActif(nat.getActif());
            r.setDateSave(nat.getDateSave());
            r.setNature(nat.getNature());
        }
        return r;
    }
    
    public static YvsBaseNatureCompte buildNatureCompte(NatureCompte nat, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        YvsBaseNatureCompte r = new YvsBaseNatureCompte();
        if (nat != null) {
            r.setId(nat.getId());
            r.setSensCompte(nat.getSensCompte());
            r.setDesignation(nat.getDesignation());
            r.setNature(nat.getNature());
            r.setLettrable(nat.isLettrable());
            r.setSaisieAnal(nat.isSaisieAnal());
            r.setSaisieCompteTier(nat.isSaisieCompteTiers());
            r.setSaisieEcheance(nat.isSaisieEcheance());
            r.setTypeReport(nat.getTypeReport());
            r.setActif(nat.isActif());
            r.setSociete(currentScte);
            r.setAuthor(currentUser);
            r.setDateUpdate(new Date());
            r.setDateSave(nat.getDateSave());
        }
        return r;
    }
    
    public static CoutSupVirement buildBeanCoutSupVirement(YvsComptaCoutSupPieceVirement y) {
        CoutSupVirement r = new CoutSupVirement();
        if (y != null) {
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setVirement(buildBeanTresoreri(y.getVirement()));
            r.setType(UtilGrh.buildBeanTypeCout(y.getTypeCout()));
            r.setId(y.getId());
            r.setMontant(y.getMontant());
        }
        return r;
    }
    
    public static YvsComptaCoutSupPieceVirement buildCoutSupVirement(CoutSupVirement y, YvsUsersAgence ua) {
        YvsComptaCoutSupPieceVirement r = new YvsComptaCoutSupPieceVirement();
        if (y != null) {
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            if (y.getVirement() != null ? y.getVirement().getId() > 0 : false) {
                r.setVirement(new YvsComptaCaissePieceVirement(y.getVirement().getId()));
            }
            if (y.getType() != null ? y.getType().getId() > 0 : false) {
                r.setTypeCout(UtilGrh.buildTypeCout(y.getType(), ua.getAgence().getSociete(), ua));
            }
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }
    
    public static CoutSupDocDivers buildBeanCoutSupDocDivers(YvsComptaCoutSupDocDivers y) {
        CoutSupDocDivers r = new CoutSupDocDivers();
        if (y != null) {
            r.setActif(y.getActif());
            r.setLierTiers(y.getLierTiers());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setDocDivers(buildBeanDocCaisse(y.getDocDivers()));
            r.setType(UtilGrh.buildBeanTypeCout(y.getTypeCout()));
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setSupp(y.getSupp());
        }
        return r;
    }
    
    public static YvsComptaCoutSupDocDivers buildCoutSupDocDivers(CoutSupDocDivers y, YvsUsersAgence ua) {
        YvsComptaCoutSupDocDivers r = new YvsComptaCoutSupDocDivers();
        if (y != null) {
            r.setActif(y.isActif());
            r.setLierTiers(y.isLierTiers());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            if (y.getDocDivers() != null ? y.getDocDivers().getId() > 0 : false) {
                r.setDocDivers(new YvsComptaCaisseDocDivers(y.getDocDivers().getId()));
            }
            if (y.getType() != null ? y.getType().getId() > 0 : false) {
                r.setTypeCout(UtilGrh.buildTypeCout(y.getType(), ua.getAgence().getSociete(), ua));
            }
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setSupp(y.isSupp());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }
    
    public static TaxeDocDivers buildBeanTaxeDocDivers(YvsComptaTaxeDocDivers y) {
        TaxeDocDivers r = new TaxeDocDivers();
        if (y != null) {
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setDocDivers(buildBeanDocCaisse(y.getDocDivers()));
            r.setTaxe(buildBeanTaxes(y.getTaxe()));
            r.setId(y.getId());
            r.setMontant(y.getMontant());
        }
        return r;
    }
    
    public static YvsComptaTaxeDocDivers buildTaxeDocDivers(TaxeDocDivers y, YvsUsersAgence ua) {
        YvsComptaTaxeDocDivers r = new YvsComptaTaxeDocDivers();
        if (y != null) {
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            if (y.getDocDivers() != null ? y.getDocDivers().getId() > 0 : false) {
                r.setDocDivers(new YvsComptaCaisseDocDivers(y.getDocDivers().getId()));
            }
            if (y.getTaxe() != null ? y.getTaxe().getId() > 0 : false) {
                r.setTaxe(buildTaxes(y.getTaxe(), ua.getAgence().getSociete(), ua));
            }
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }
    
    public static CentreCoutVirement buildBeanCentreVirement(YvsComptaCentreCoutVirement y) {
        CentreCoutVirement r = new CentreCoutVirement();
        if (y != null) {
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setCout(new CoutSupVirement(y.getCout().getId()));
            r.setCentre(buildBeanCentreAnalytique(y.getCentre()));
            r.setId(y.getId());
            r.setTaux(y.getCoefficient());
        }
        return r;
    }
    
    public static YvsComptaCentreCoutVirement buildCentreVirement(CentreCoutVirement y, YvsUsersAgence ua) {
        YvsComptaCentreCoutVirement r = new YvsComptaCentreCoutVirement();
        if (y != null) {
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            if (y.getCout() != null ? y.getCout().getId() > 0 : false) {
                r.setCout(new YvsComptaCoutSupPieceVirement(y.getCout().getId()));
            }
            if (y.getCentre() != null ? y.getCentre().getId() > 0 : false) {
                r.setCentre(buildCentreAnalytique(y.getCentre(), ua));
            }
            r.setId(y.getId());
            r.setCoefficient(y.getTaux());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }
    
    public static CentreContenuAchat buildBeanCentreContenuAchat(YvsComptaCentreContenuAchat y) {
        CentreContenuAchat r = new CentreContenuAchat();
        if (y != null) {
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setContenu(new ContenuDocAchat(y.getContenu().getId()));
            r.setCentre(buildBeanCentreAnalytique(y.getCentre()));
            r.setId(y.getId());
            r.setTaux(y.getCoefficient());
        }
        return r;
    }
    
    public static YvsComptaCentreContenuAchat buildCentreContenuAchat(CentreContenuAchat y, YvsUsersAgence ua) {
        YvsComptaCentreContenuAchat r = new YvsComptaCentreContenuAchat();
        if (y != null) {
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            if (y.getContenu() != null ? y.getContenu().getId() > 0 : false) {
                r.setContenu(new YvsComContenuDocAchat(y.getContenu().getId()));
            }
            if (y.getCentre() != null ? y.getCentre().getId() > 0 : false) {
                r.setCentre(buildCentreAnalytique(y.getCentre(), ua));
            }
            r.setId(y.getId());
            r.setCoefficient(y.getTaux());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }
    
    public static CentreDocDivers buildBeanCentreDocDivers(YvsComptaCentreDocDivers y) {
        CentreDocDivers r = new CentreDocDivers();
        if (y != null) {
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setDocDivers(buildBeanDocCaisse(y.getDocDivers()));
            r.setCentre(buildBeanCentreAnalytique(y.getCentre()));
            r.setId(y.getId());
            r.setMontant(y.getMontant());
        }
        return r;
    }
    
    public static YvsComptaCentreDocDivers buildCentreDocDivers(CentreDocDivers y, YvsUsersAgence ua) {
        YvsComptaCentreDocDivers r = new YvsComptaCentreDocDivers();
        if (y != null) {
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            if (y.getDocDivers() != null ? y.getDocDivers().getId() > 0 : false) {
                r.setDocDivers(new YvsComptaCaisseDocDivers(y.getDocDivers().getId()));
            }
            if (y.getCentre() != null ? y.getCentre().getId() > 0 : false) {
                r.setCentre(buildCentreAnalytique(y.getCentre(), ua));
            }
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }
    
    public static CentreMission buildBeanCentreMission(YvsComptaCentreMission y) {
        CentreMission r = new CentreMission();
        if (y != null) {
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setMission(UtilGrh.buildBeanMission(y.getMission()));
            r.setCentre(buildBeanCentreAnalytique(y.getCentre()));
            r.setId(y.getId());
            r.setTaux(y.getCoefficient());
        }
        return r;
    }
    
    public static YvsComptaCentreMission buildCentreMission(CentreMission y, YvsUsersAgence ua) {
        YvsComptaCentreMission r = new YvsComptaCentreMission();
        if (y != null) {
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            if (y.getMission() != null ? y.getMission().getId() > 0 : false) {
                r.setMission(new YvsGrhMissions(y.getMission().getId()));
            }
            if (y.getCentre() != null ? y.getCentre().getId() > 0 : false) {
                r.setCentre(buildCentreAnalytique(y.getCentre(), ua));
            }
            r.setId(y.getId());
            r.setCoefficient(y.getTaux());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }
    
    public static TiersDivers buildBeanTiersDivers(YvsComptaCaisseDocDiversTiers y) {
        TiersDivers d = new TiersDivers();
        if (y != null) {
            d.setDateSave(y.getDateSave());
            d.setId(y.getId());
            d.setMontant(y.getMontant());
            d.setTableTiers(y.getTableTiers());
            d.setIdTiers(y.getIdTiers());
            d.setDocDivers(y.getDocDivers() != null ? new DocCaissesDivers(y.getDocDivers().getId()) : new DocCaissesDivers());
        }
        return d;
    }
    
    public static YvsComptaCaisseDocDiversTiers buildTiersDivers(TiersDivers y) {
        YvsComptaCaisseDocDiversTiers d = new YvsComptaCaisseDocDiversTiers();
        if (y != null) {
            d.setDateSave(y.getDateSave());
            d.setId(y.getId());
            d.setMontant(y.getMontant());
            if (y.getTiers().getId() > 0) {
                d.setIdTiers(y.getTiers().getSelectProfil().getIdTiers());
                d.setTableTiers(y.getTiers().getSelectProfil().getTableTiers());
            } else {
                d.setIdTiers(-1L);
                d.setTableTiers(null);
            }
            if (y.getDocDivers() != null ? y.getDocDivers().getId() > 0 : false) {
                d.setDocDivers(new YvsComptaCaisseDocDivers(y.getDocDivers().getId()));
            }
        }
        return d;
    }
    
    public static DocCaissesDivers buildSimpleBeanDocCaisse(YvsComptaCaisseDocDivers y) {
        DocCaissesDivers d = new DocCaissesDivers();
        if (y != null) {
            d.setActif(y.getActif());
            d.setDateCloturer(y.getDateCloturer());
            d.setDatePaiementPrevu(y.getDateDoc());
            d.setDateDebutPlan(y.getDateDoc());
            d.setDateSave(y.getDateSave());
            d.setDescription(y.getDescription());
            d.setId(y.getId());
            d.setMontant(y.getMontant());
            d.setMouvement(y.getMouvement());
            d.setComptabilise(y.getComptabilise());
            d.setNumPiece(y.getNumPiece());
            d.setStatutDoc(y.getStatutDoc());
            d.setStatutRegle(y.getStatutRegle());
            d.setReglements(y.getReglements());
            d.setAbonnements(y.getAbonnements());
            d.setReference(y.getReferenceExterne());
            d.setDateDoc(y.getDateDoc());
            d.setLibelleComptable(y.getLibelleComptable());
            d.setEtapeTotal(y.getEtapeTotal());
            d.setEtapeValide(y.getEtapeValide());
            d.setMontantTtc(y.getIsMontantTTc());
        }
        return d;
    }
    
    public static DocCaissesDivers buildBeanDocCaisse(YvsComptaCaisseDocDivers y) {
        DocCaissesDivers d = new DocCaissesDivers();
        if (y != null) {
            d.setActif(y.getActif());
            d.setCompte(buildSimpleBeanCompte(y.getCompteGeneral()));
            if (y.getAuthor() != null ? y.getAuthor().getUsers() != null : false) {
                d.setAuthor(new Users(y.getAuthor().getUsers().getId(), y.getAuthor().getUsers().getNomUsers()));
            }
            d.setDateCloturer(y.getDateCloturer());
            d.setDatePaiementPrevu(y.getDateDoc());
            d.setDateDebutPlan(y.getDateDoc());
            d.setDateSave(y.getDateSave());
            d.setDescription(y.getDescription());
            d.setId(y.getId());
            d.setIdTiers(y.getIdTiers());
            d.setTableTiers(y.getTableTiers());
            d.setMontant(y.getMontant());
            d.setMouvement(y.getMouvement());
            d.setComptabilise(y.getComptabilise());
            d.setNumPiece(y.getNumPiece());
            d.setStatutDoc(y.getStatutDoc());
            d.setStatutRegle(y.getStatutRegle());
            d.setReglements(y.getReglements());
            d.setAbonnements(y.getAbonnements());
            d.setTaxe(y.getTaxe());
            d.setTaxes(y.getTaxes());
            d.setSections(y.getSections());
            d.setCout(y.getCout());
            d.setCouts(y.getCouts());
            d.setReference(y.getReferenceExterne());
            d.setDateDoc(y.getDateDoc());
            d.setTypeDocDiv(buildBeanTypeDoc(y.getTypeDoc()));
            d.setAgence(UtilAgence.buildBeanAgence(y.getAgence()));
            d.setCaisse(buildSimpleBeanCaisse(y.getCaisse()));
            d.setCaisseDefaut(buildSimpleBeanCaisse(y.getCaisse()));
            d.setLibelleComptable(y.getLibelleComptable());
            d.setEtapeTotal(y.getEtapeTotal());
            d.setEtapeValide(y.getEtapeValide());
            d.setMontantTtc(y.getIsMontantTTc());
            d.setParent(y.getParent() != null ? new DocCaissesDivers(y.getParent().getId()) : new DocCaissesDivers());
            d.setListTiers(y.getTiers());
        }
        return d;
    }
    
    public static YvsComptaCaisseDocDivers buildDocDivers(DocCaissesDivers y) {
        return buildDocDivers(y, null, null);
    }
    
    public static YvsComptaCaisseDocDivers buildDocDivers(DocCaissesDivers y, YvsUsersAgence ua, YvsSocietes st) {
        YvsComptaCaisseDocDivers d = new YvsComptaCaisseDocDivers();
        if (y != null) {
            d.setActif(y.isActif());
            d.setId(y.getId());
            d.setNumPiece(y.getNumPiece());
            d.setReferenceExterne(y.getReference());
            d.setDateDoc(y.getDateDoc());
            d.setDateSave(y.getDateSave());
            d.setMontant(y.getMontant());
            d.setMouvement((y.getMouvement() != null) ? y.getMouvement() : Constantes.MOUV_CAISS_SORTIE);
            d.setStatutDoc(y.getStatutDoc());
            d.setStatutRegle(y.getStatutRegle());
            d.setDateCloturer(y.getDateCloturer());
            d.setDescription(y.getDescription());
            d.setIsMontantTTc(y.isMontantTtc());
            d.setEtapeTotal(y.getEtapeTotal());
            d.setEtapeValide(y.getEtapeValide());
            d.setComptabilise(y.isComptabilise());
            if (y.getTiers().getId() > 0) {
                d.setIdTiers(y.getTiers().getSelectProfil().getIdTiers());
            } else {
                d.setIdTiers(-1L);
            }
            if (y.getTiers().getId() > 0) {
                d.setTableTiers(y.getTiers().getSelectProfil().getTableTiers());
            } else {
                d.setTableTiers(null);
            }
            d.setLibelleComptable(y.getLibelleComptable());
            if (y.getTypeDocDiv() != null ? y.getTypeDocDiv().getId() > 0 : false) {
                d.setTypeDoc(buildBeanTypeDoc(y.getTypeDocDiv(), ua));
            }
//            if (y.getTiers().getId() > 0) {
//                d.setTiers(new YvsBaseTiers(y.getTiers().getId(), y.getTiers().getNom(), y.getTiers().getPrenom()));
//            }
            if (y.getCompte().getId() > 0) {
                d.setCompteGeneral(buildEntityCompte(y.getCompte()));
            }
            if (y.getParent() != null ? y.getParent().getId() > 0 : false) {
                d.setParent(new YvsComptaCaisseDocDivers(y.getParent().getId()));
            }
            if (y.getCaisse() != null ? y.getCaisse().getId() > 0 : false) {
                d.setCaisse(new YvsBaseCaisse(y.getCaisse().getId(), y.getCaisse().getIntitule()));
            }
            if (y.getAgence() != null ? y.getAgence().getId() > 0 : false) {
                d.setAgence(new YvsAgences(y.getAgence().getId(), y.getAgence().getDesignation()));
            }
            d.setAuthor(ua);
            d.setSociete(st);
            d.setDateUpdate(new Date());
            d.setNew_(true);
        }
        return d;
    }
    
    public static AbonnementDocDivers buildBeanAbonnement(YvsComptaAbonementDocDivers y) {
        AbonnementDocDivers r = new AbonnementDocDivers();
        if (y != null) {
            r.setActif(y.getActif());
            r.setComptabilise(y.getComptabilise());
            r.setDateSave(y.getDateSave());
            r.setDocDivers(y.getDocDivers() != null ? new DocCaissesDivers(y.getDocDivers().getId()) : new DocCaissesDivers());
            r.setPlan(y.getPlan() != null ? new PlanDecoupage(y.getPlan().getId()) : new PlanDecoupage());
            r.setEcheance(y.getEcheance());
            r.setId(y.getId());
            r.setLibelle(y.getLibelle());
            r.setValeur(y.getValeur());
        }
        return r;
    }
    
    public static YvsComptaAbonementDocDivers buildAbonnement(AbonnementDocDivers y, YvsUsersAgence ua) {
        YvsComptaAbonementDocDivers r = new YvsComptaAbonementDocDivers();
        if (y != null) {
            r.setActif(y.isActif());
            r.setComptabilise(y.isComptabilise());
            r.setDateSave(y.getDateSave());
            if (y.getDocDivers() != null ? y.getDocDivers().getId() > 0 : false) {
                r.setDocDivers(new YvsComptaCaisseDocDivers(y.getDocDivers().getId()));
            }
            if (y.getPlan() != null ? y.getPlan().getId() > 0 : false) {
                r.setPlan(new YvsComptaPlanAbonnement(y.getPlan().getId()));
            }
            r.setEcheance(y.getEcheance());
            r.setId(y.getId());
            r.setLibelle(y.getLibelle());
            r.setValeur(y.getValeur());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
        }
        return r;
    }
    
    public static Caisses buildBeanCaisse(YvsBaseCaisse ca) {
        Caisses c = buildSimpleBeanCaisse(ca);
        if (c != null && ca != null) {
            c.setJournal((ca.getJournal() != null) ? buildBeanJournaux(ca.getJournal()) : new Journaux());
            c.setCaissier(ca.getCaissier() != null ? UtilUsers.buildSimpleBeanUsers(ca.getCaissier()) : new Users());
            c.setCompte(buildBeanCompte(ca.getCompte()));
            c.setResponsable(UtilGrh.buildBeanSimplePartialEmploye(ca.getResponsable()));
            c.setSubCaisses(ca.getCaisseLiees());
            c.setOthersComptes(ca.getOthersCompte());
            if (ca.getParent() != null) {
                c.setParent(new Caisses(ca.getParent().getId(), ca.getParent().getIntitule()));
            } else {
                c.setParent(new Caisses());
            }
        }
        return c;
    }
    
    public static Caisses buildSimpleBeanCaisse_(YvsBaseCaisse ca) {
        Caisses c = new Caisses();
        if (ca != null) {
            c.setActif(ca.getActif());
            c.setCode(ca.getCode());
            c.setAdresse(ca.getAdresse());
            c.setId(ca.getId());
            c.setIntitule(ca.getIntitule());
            c.setCanBeNegative(ca.getCanNegative());
            c.setPrincipal(ca.getPrincipal());
            c.setVenteOnline(ca.getVenteOnline());
            c.setSolde(0);
            c.setType(ca.getTypeCaisse());
            c.setDateSave(ca.getDateSave());
            c.setDefaultCaisse(ca.getDefaultCaisse());
            c.setGiveBilletage(ca.getGiveBilletage());
        }
        return c;
    }
    
    public static Caisses buildSimpleBeanCaisse(YvsBaseCaisse ca) {
        Caisses c = buildSimpleBeanCaisse_(ca);
        if (ca != null) {
            c.setCodeAcces(ca.getCodeAcces() != null ? ca.getCodeAcces().getCode() : "");
            c.setModeRegDefaut((ca.getModeRegDefaut() != null) ? new ModeDeReglement(ca.getModeRegDefaut().getId().intValue(), ca.getModeRegDefaut().getDesignation(), ca.getModeRegDefaut().getTypeReglement()) : new ModeDeReglement());
        }
        return c;
    }
    
    public static YvsBaseCaisse buildBeanCaisse(Caisses ca) {
        YvsBaseCaisse c = new YvsBaseCaisse();
        if (ca != null) {
            c.setId(ca.getId());
            c.setCode(ca.getCode());
            c.setActif(ca.isActif());
            c.setAdresse(ca.getAdresse());
            c.setIntitule(ca.getIntitule());
            c.setCanNegative(ca.isCanBeNegative());
            c.setTypeCaisse(ca.getType());
            c.setDateSave(ca.getDateSave());
            c.setDateUpdate(new Date());
            c.setDefaultCaisse(ca.isDefaultCaisse());
            c.setGiveBilletage(ca.isGiveBilletage());
            c.setPrincipal(ca.isPrincipal());
            c.setVenteOnline(ca.isVenteOnline());
            if (ca.getCompte() != null ? ca.getCompte().getId() > 0 : false) {
                c.setCompte(buildEntityCompte(ca.getCompte()));
            }
            if (ca.getResponsable() != null ? ca.getResponsable().getId() > 0 : false) {
//                c.setResponsable(UtilGrh.buildEmployeEntity(ca.getResponsable()));
            }
            if (ca.getJournal() != null ? ca.getJournal().getId() > 0 : false) {
                c.setJournal(buildBeanJournaux(ca.getJournal()));
            }
            if (ca.getParent() != null ? ca.getParent().getId() > 0 : false) {
                c.setParent(new YvsBaseCaisse(ca.getParent().getId(), ca.getParent().getIntitule()));
            }
            if (ca.getModeRegDefaut() != null ? ca.getModeRegDefaut().getId() > 0 : false) {
                c.setModeRegDefaut(new YvsBaseModeReglement((long) ca.getModeRegDefaut().getId()));
            }
            if (ca.getCaissier() != null ? ca.getCaissier().getId() > 0 : false) {
                c.setCaissier(UtilUsers.buildSimpleBeanUsers(ca.getCaissier()));
            }
        }
        return c;
    }

//    public static PieceTresorerie
    public static PieceTresorerie buildBeanTresoreri(YvsComptaMouvementCaisse mvt) {
        PieceTresorerie p = new PieceTresorerie();
        if (mvt != null) {
            p.setCaisse(UtilCompta.buildBeanCaisse(mvt.getCaisse()));
            p.setCaissier(UtilUsers.buildBeanUsers(mvt.getCaissier()));
            p.setDatePaiement(mvt.getDatePaye());
            p.setDatePaiementPrevu(mvt.getDatePaimentPrevu());
            p.setDatePiece(mvt.getDateMvt());
            p.setDescription(mvt.getNote());
            p.setError(false);
            p.setId(mvt.getId());
            p.setIdExterne(mvt.getIdExterne());
            p.setSource(mvt.getTableExterne());
            p.setMontant(mvt.getMontant());
            p.setMouvement(mvt.getMouvement());
            p.setNameTable(mvt.getTableExterne());
            p.setNumPiece(mvt.getNumero());
            p.setNumRef(mvt.getReferenceExterne());
            p.setStatutPiece(mvt.getStatutPiece());
            p.setTableExterne(mvt.getTableExterne());
            p.setMode(UtilCom.buildBeanModeReglement(mvt.getModel()));
        }
        return p;
    }
    
    public static PieceTresorerie buildBeanTresoreri(YvsComptaCaissePieceVirement mvt) {
        PieceTresorerie p = new PieceTresorerie();
        if (mvt != null) {
            p.setCaisse(UtilCompta.buildBeanCaisse(mvt.getSource()));
            p.setOtherCaisse(UtilCompta.buildBeanCaisse(mvt.getCible()));
            p.setCaissier(UtilUsers.buildSimpleBeanUsers(mvt.getCaissierSource()));
            p.setCaissierCible(UtilUsers.buildSimpleBeanUsers(mvt.getCaissierCible()));
            p.setDatePaiement(mvt.getDatePaiement());
            p.setDatePaiementPrevu(mvt.getDatePaimentPrevu());
            p.setDatePiece(mvt.getDatePiece());
            p.setDescription(mvt.getNote());
            p.setComptabilise(mvt.getComptabilise());
            p.setError(false);
            p.setId(mvt.getId());
            p.setSource(Constantes.SCR_VIREMENT);
            p.setMontant(mvt.getMontant());
            p.setNameTable(Constantes.SCR_VIREMENT);
            p.setNumPiece(mvt.getNumeroPiece());
            p.setNumRef(mvt.getNumeroPiece());
            p.setStatutPiece(mvt.getStatutPiece());
            p.setTableExterne(Constantes.SCR_VIREMENT);
            p.setDateSave(mvt.getDateSave());
            if (mvt.getVersement() != null ? mvt.getVersement().getId() > 0 : false) {
                p.setEnteteDoc(UtilCom.buildSimpleBeanEnteteDocVente(mvt.getVersement().getEnteteDoc()));
            }
            for (YvsComptaBielletage b : mvt.getBielletages()) {
                if (b.getBillet()) {
                    int idx = p.getBielletageBillet().indexOf(new BielletagePc(b.getFormatMonai()));
                    if (idx >= 0) {
                        p.getBielletageBillet().set(idx, new BielletagePc(b.getId(), b.getFormatMonai(), b.getQuantite(), b.getValeur()));
                    } else {
                        p.getBielletageBillet().add(new BielletagePc(b.getId(), b.getFormatMonai(), b.getQuantite(), b.getValeur()));
                    }
                } else {
                    int idx = p.getBielletagePiece().indexOf(new BielletagePc(b.getFormatMonai()));
                    if (idx >= 0) {
                        p.getBielletagePiece().set(idx, new BielletagePc(b.getId(), b.getFormatMonai(), b.getQuantite(), b.getValeur()));
                    } else {
                        p.getBielletagePiece().add(new BielletagePc(b.getId(), b.getFormatMonai(), b.getQuantite(), b.getValeur()));
                    }
                }
            }
            p.setCouts(mvt.getCouts());
        }
        return p;
    }
    
    public static YvsComptaCaissePieceVirement buildTresoreri(PieceTresorerie y, YvsUsersAgence ua) {
        YvsBaseCaisse source = new YvsBaseCaisse(y.getCaisse().getId(), y.getCaisse().getIntitule());
        YvsBaseCaisse cible = new YvsBaseCaisse(y.getOtherCaisse().getId(), y.getOtherCaisse().getIntitule());
        return buildTresoreri(y, source, cible, ua);
    }
    
    public static YvsComptaCaissePieceVirement buildTresoreri(PieceTresorerie y, YvsBaseCaisse source, YvsBaseCaisse cible, YvsUsersAgence ua) {
        YvsComptaCaissePieceVirement r = new YvsComptaCaissePieceVirement();
        if (y != null) {
            r.setId(y.getId());
            if (source != null ? source.getId() > 0 : false) {
                r.setSource(source);
            }
            if (cible != null ? cible.getId() > 0 : false) {
                r.setCible(cible);
            }
            if (y.getMode() != null ? y.getMode().getId() > 0 : false) {
                r.setModel(new YvsBaseModeReglement((long) y.getMode().getId(), y.getMode().getDesignation(), y.getMode().getTypeReglement()));
            }
            if (y.getCaissier() != null ? y.getCaissier().getId() > 0 : false) {
                r.setCaissierSource(UtilUsers.buildSimpleBeanUsers(y.getCaissier()));
            } else {
                r.setCaissierSource(ua.getUsers());
            }
            if (y.getCaissierCible() != null ? y.getCaissierCible().getId() > 0 : false) {
                r.setCaissierCible(UtilUsers.buildSimpleBeanUsers(y.getCaissierCible()));
            }
            r.setComptabilise(y.isComptabilise());
            r.setDateSave(y.getDateSave());
            r.setDatePaiement(y.getDatePaiement());
            r.setDatePaimentPrevu(y.getDatePaiementPrevu());
            r.setDatePiece(y.getDatePiece());
            r.setMontant(y.getMontant());
            r.setNote(y.getDescription());
            r.setNumeroPiece(y.getNumPiece());
            r.setStatutPiece(y.getStatutPiece());
            r.setAuthor(ua);
            if (y.getEnteteDoc() != null ? y.getEnteteDoc().getId() > 0 : false) {
                r.setHeader(new YvsComEnteteDocVente(y.getEnteteDoc().getId()));
            }
            r.setDateUpdate(new Date());
            r.setNew_(true);
        }
        return r;
    }
    
    public static List<PieceTresorerie> buildSimpleBeanPieceTresorerie(List<YvsComptaMouvementCaisse> l) {
        List<PieceTresorerie> re = new ArrayList<>();
        for (YvsComptaMouvementCaisse mv : l) {
            re.add(buildBeanTresoreri(mv));
        }
        return re;
    }
    
    public static PieceTresorerie buildSimpleBeanTresoreri(YvsComptaCaissePieceDivers y) {
        PieceTresorerie r = new PieceTresorerie();
        if (y != null) {
            r.setCaisse(UtilCompta.buildSimpleBeanCaisse(y.getCaisse()));
            r.setCaissier(UtilUsers.buildSimpleBeanUsers(y.getCaissier()));
            r.setDatePaiement(y.getDateValider());
            r.setDatePaiementPrevu(y.getDatePaimentPrevu());
            r.setDatePiece(y.getDatePiece());
            r.setDescription(y.getNote());
            r.setError(false);
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setNumPiece(y.getNumPiece());
            r.setMouvement(String.valueOf(y.getMouvement()));
            r.setStatutPiece(y.getStatutPiece());
            r.setValideBy(UtilUsers.buildSimpleBeanUsers(y.getValiderBy()));
            r.setDateValide(y.getDateValider());
            r.setComptabilise(y.getComptabilise());
            r.setDocDivers(UtilCompta.buildSimpleBeanDocCaisse(y.getDocDivers()));
            r.setClients(r.getDocVente().getClient());
            r.setNumRefExterne(y.getReferenceExterne());
            r.setMode(buildBeanModeReglement(y.getModePaiement()));
        }
        return r;
    }
    
    public static PieceTresorerie buildBeanTresoreri(YvsComptaCaissePieceDivers y) {
        PieceTresorerie r = new PieceTresorerie();
        if (y != null) {
            r = buildSimpleBeanTresoreri(y);
            r.setPhasesDivers(y.getPhasesReglement());
            r.setAcompte(y.getNotifs() != null ? y.getNotifs().getId() != null ? y.getNotifs().getId() > 0 : false : false);
            r.setNotifRegDivers(y.getNotifs());
            r.setSousDivers(y.getSousDivers());
            r.setIdParent((y.getParent() != null) ? y.getParent().getId() : -1);
        }
        return r;
    }
    
    public static PieceTresorerie buildSimpleBeanTresoreri(YvsComptaCaissePieceVente y) {
        PieceTresorerie r = new PieceTresorerie();
        if (y != null) {
            r.setCaisse(UtilCompta.buildSimpleBeanCaisse(y.getCaisse()));
            r.setCaissier(UtilUsers.buildSimpleBeanUsers(y.getCaissier()));
            r.setDatePaiement(y.getDatePaiement());
            r.setDatePaiementPrevu(y.getDatePaimentPrevu());
            r.setDatePiece(y.getDatePiece());
            r.setDescription(y.getNote());
            r.setError(false);
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setNumPiece(y.getNumeroPiece());
            r.setMouvement(String.valueOf(y.getMouvement()));
            r.setStatutPiece(y.getStatutPiece());
            r.setValideBy(UtilUsers.buildSimpleBeanUsers(y.getValideBy()));
            r.setDateValide(y.getDateValide());
            r.setComptabilise(y.getComptabilise());
            r.setDocVente(UtilCom.buildInfosBaseDocVente(y.getVente()));
            r.setClients(r.getDocVente().getClient());
            r.setNumRefExterne(y.getReferenceExterne());
            r.setMode(buildBeanModeReglement(y.getModel()));
        }
        return r;
    }
    
    public static PieceTresorerie buildBeanTresoreri(YvsComptaCaissePieceVente y) {
        PieceTresorerie r = new PieceTresorerie();
        if (y != null) {
            r = buildSimpleBeanTresoreri(y);
            r.setPhases(y.getPhasesReglement());
            r.setCompensations(y.getCompensations());
            r.setAcompte(y.getNotifs() != null ? y.getNotifs().getId() != null ? y.getNotifs().getId() > 0 : false : false);
            r.setNotifRegVente(y.getNotifs());
            r.setSousVentes(y.getSousVentes());
            r.setIdParent((y.getParent() != null) ? y.getParent().getId() : -1);
        }
        return r;
    }
    
    public static YvsComptaCaissePieceVente buildTresoreriVente(PieceTresorerie y, YvsUsersAgence ua) {
        YvsComptaCaissePieceVente r = new YvsComptaCaissePieceVente();
        if (y != null) {
            r.setId(y.getId());
            r.setDatePaiement(y.getDatePaiement());
            r.setDatePaimentPrevu(y.getDatePaiementPrevu());
            r.setDatePiece(y.getDatePiece());
            r.setNote(y.getDescription());
            r.setMontant(y.getMontant());
            r.setNumeroPiece(y.getNumPiece());
            r.setStatutPiece(y.getStatutPiece());
            r.setMouvement(y.getMouvement().charAt(0));
            r.setReferenceExterne(y.getNumRefExterne());
            r.setDateValide(y.getDateValide());
            r.setComptabilise(y.isComptabilise());
            if (y.getMode() != null ? y.getMode().getId() > 0 : false) {
                r.setModel(new YvsBaseModeReglement((long) y.getMode().getId(), y.getMode().getDesignation(), y.getMode().getTypeReglement()));
            }
            if (y.getCaisse() != null ? y.getCaisse().getId() > 0 : false) {
                r.setCaisse(new YvsBaseCaisse(y.getCaisse().getId(), y.getCaisse().getIntitule()));
            }
            if (y.getCaissier() != null ? y.getCaissier().getId() > 0 : false) {
                r.setCaissier(new YvsUsers(y.getCaissier().getId(), y.getCaissier().getCodeUsers(), y.getCaissier().getNomUsers()));
            }
            if (y.getValideBy() != null ? y.getValideBy().getId() > 0 : false) {
                r.setValideBy(new YvsUsers(y.getValideBy().getId(), y.getValideBy().getCodeUsers(), y.getValideBy().getNomUsers()));
            }
            if (y.getDocVente() != null ? y.getDocVente().getId() > 0 : false) {
                r.setVente(new YvsComDocVentes(y.getDocVente().getId(), y.getDocVente().getNumDoc(), y.getDocVente().getStatut()));
            }
            r.setCompensations(y.getCompensations());
            r.setAuthor(ua);
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setNew_(true);
        }
        return r;
    }
    
    public static PieceTresorerie buildBeanTresoreri(YvsComptaCaissePieceAchat y) {
        PieceTresorerie r = new PieceTresorerie();
        if (y != null) {
            r.setId(y.getId());
            r.setDatePaiement(y.getDatePaiement());
            r.setDatePaiementPrevu(y.getDatePaimentPrevu());
            r.setDatePiece(y.getDatePiece());
            r.setDescription(y.getNote());
            r.setError(false);
            r.setUpdate(true);
            r.setComptabilise(y.getComptabilise());
            r.setDateSave(y.getDateSave());
            r.setMontant(y.getMontant());
            r.setNumPiece(y.getNumeroPiece());
            r.setStatutPiece(y.getStatutPiece());
            r.setMode(buildBeanModeReglement(y.getModel()));
            r.setCaisse(UtilCompta.buildSimpleBeanCaisse(y.getCaisse()));
            r.setNumRefExterne(y.getReferenceExterne());
            r.setCaissier(UtilUsers.buildSimpleBeanUsers(y.getCaissier()));
            r.setDocAchat(UtilCom.buildSimpleBeanDocAchat(y.getAchat()));
            if (y.getFournisseur() != null ? y.getFournisseur().getId() > 0 : false) {
                r.setFournisseur(UtilCom.buildBeanFournisseur(y.getFournisseur()));
            } else {
                r.setFournisseur(r.getDocAchat().getFournisseur());
            }
            r.setCompensations(y.getCompensations());
            r.setPhasesAchat(y.getPhasesReglement());
            r.setSousAchats(y.getSousAchats());
        }
        return r;
    }
    
    public static YvsComptaCaissePieceAchat buildTresoreriAchat(PieceTresorerie y, YvsUsersAgence ua) {
        YvsComptaCaissePieceAchat r = new YvsComptaCaissePieceAchat();
        if (y != null) {
            r.setId(y.getId());
            r.setDatePaiement(y.getDatePaiement());
            r.setDatePaimentPrevu(y.getDatePaiementPrevu());
            r.setDatePiece(y.getDatePiece());
            r.setNote(y.getDescription());
            r.setMontant(y.getMontant());
            r.setComptabilise(y.isComptabilise());
            r.setNumeroPiece(y.getNumPiece());
            r.setStatutPiece(y.getStatutPiece());
            r.setReferenceExterne(y.getNumRefExterne());
            if (y.getMode() != null ? y.getMode().getId() > 0 : false) {
                r.setModel(new YvsBaseModeReglement((long) y.getMode().getId(), y.getMode().getDesignation(), y.getMode().getTypeReglement()));
            }
            if (y.getCaisse() != null ? y.getCaisse().getId() > 0 : false) {
                r.setCaisse(new YvsBaseCaisse(y.getCaisse().getId(), y.getCaisse().getIntitule(), y.getCaisse().isCanBeNegative()));
            }
            if (y.getCaissier() != null ? y.getCaissier().getId() > 0 : false) {
                r.setCaissier(new YvsUsers(y.getCaissier().getId(), y.getCaissier().getCodeUsers(), y.getCaissier().getNomUsers()));
            }
            if (y.getDocAchat() != null ? y.getDocAchat().getId() > 0 : false) {
                r.setAchat(new YvsComDocAchats(y.getDocAchat().getId(), y.getDocAchat().getNumDoc(), y.getDocAchat().getStatut()));
            }
            if (y.getFournisseur() != null ? y.getFournisseur().getId() > 0 : false) {
                r.setFournisseur(new YvsBaseFournisseur(y.getFournisseur().getId(), new YvsBaseTiers(y.getFournisseur().getTiers().getId(), y.getFournisseur().getTiers().getNom(), y.getFournisseur().getTiers().getPrenom())));
            }
            r.setCompensations(y.getCompensations());
            r.setAuthor(ua);
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setNew_(true);
        }
        return r;
    }
    
    public static PieceTresorerie buildBeanTresoreri(YvsComptaCaissePieceMission pv) {
        PieceTresorerie pieceCaisse = new PieceTresorerie();
        if (pv != null) {
            pieceCaisse.setCaisse(UtilCompta.buildBeanCaisse(pv.getCaisse()));
            pieceCaisse.setCaissier(UtilUsers.buildSimpleBeanUsers(pv.getCaissier()));
            pieceCaisse.setDatePaiement(pv.getDatePaiement());
            pieceCaisse.setDatePaiementPrevu(pv.getDatePaimentPrevu());
            pieceCaisse.setDatePiece(pv.getDatePiece());
            pieceCaisse.setDescription(pv.getNote());
            pieceCaisse.setError(false);
            pieceCaisse.setId(pv.getId());
            pieceCaisse.setMontant(pv.getMontant());
            pieceCaisse.setNumPiece(pv.getNumeroPiece());
            pieceCaisse.setStatutPiece(pv.getStatutPiece());
            pieceCaisse.setMode(buildBeanModeReglement(pv.getModel()));
            if (pv.getBonProvisoire() != null ? pv.getBonProvisoire().getId() > 0 : false) {
                pieceCaisse.setBonProvisoire(new BonProvisoire(pv.getBonProvisoire().getId(), pv.getBonProvisoire().getNumero()));
            }
        }
        return pieceCaisse;
    }
    
    public static PieceTresorerie buildBeanTresoreri(YvsComptaCaissePieceCommission pv) {
        PieceTresorerie pieceCaisse = new PieceTresorerie();
        if (pv != null) {
            pieceCaisse.setCaisse(UtilCompta.buildBeanCaisse(pv.getCaisse()));
            pieceCaisse.setCaissier(UtilUsers.buildSimpleBeanUsers(pv.getCaissier()));
            pieceCaisse.setDatePaiement(pv.getDatePaiement());
            pieceCaisse.setDatePaiementPrevu(pv.getDatePaimentPrevu());
            pieceCaisse.setDatePiece(pv.getDatePiece());
            pieceCaisse.setDescription(pv.getNote());
            pieceCaisse.setError(false);
            pieceCaisse.setId(pv.getId());
            pieceCaisse.setMontant(pv.getMontant());
            pieceCaisse.setNumPiece(pv.getNumeroPiece());
            pieceCaisse.setStatutPiece(pv.getStatutPiece());
            pieceCaisse.setMode(buildBeanModeReglement(pv.getModel()));
        }
        return pieceCaisse;
    }
    
    public static YvsComptaCaissePieceCommission buildTresoreriCommission(PieceTresorerie y, YvsUsersAgence u) {
        YvsComptaCaissePieceCommission r = new YvsComptaCaissePieceCommission();
        if (y != null) {
            r.setDatePaiement(y.getDatePaiement());
            r.setDatePaimentPrevu(y.getDatePaiementPrevu());
            r.setDatePiece(y.getDatePiece());
            r.setNote(y.getDescription());
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setNumeroPiece(y.getNumPiece());
            r.setStatutPiece(y.getStatutPiece());
            if (y.getMode() != null ? y.getMode().getId() > 0 : false) {
                r.setModel(new YvsBaseModeReglement((long) y.getMode().getId(), y.getMode().getDesignation(), y.getMode().getTypeReglement()));
            }
            if (y.getCaisse() != null ? y.getCaisse().getId() > 0 : false) {
                r.setCaisse(new YvsBaseCaisse(y.getCaisse().getId(), y.getCaisse().getIntitule()));
            }
            if (y.getCaissier() != null ? y.getCaissier().getId() > 0 : false) {
                r.setCaissier(new YvsUsers(y.getCaissier().getId(), y.getCaissier().getCodeUsers(), y.getCaissier().getNomUsers()));
            }
            if (y.getCommission() != null ? y.getCommission().getId() > 0 : false) {
                r.setCommission(new YvsComCommissionCommerciaux(y.getCommission().getId(), y.getCommission().getNumero(), y.getCommission().getStatut()));
            }
            r.setAuthor(u);
        }
        return r;
    }
    
    public static YvsComptaModeleSaisie buildBeanModel(ModelesSasie bean) {
        if (bean != null) {
            YvsComptaModeleSaisie model = new YvsComptaModeleSaisie(bean.getId());
            model.setActif(bean.isActif());
            model.setId(bean.getId());
            model.setIntitule(bean.getIntitule());
            model.setTypeModele(bean.getTypeModele());
            return model;
        }
        return null;
    }
    
    public static ModelesSasie buildBeanModel(YvsComptaModeleSaisie bean) {
        if (bean != null) {
            ModelesSasie model = new ModelesSasie();
            model.setActif(bean.getActif());
            model.setId(bean.getId());
            model.setIntitule(bean.getIntitule());
            model.setTypeModele(bean.getTypeModele());
            model.setContenus(bean.getContentsModel());
            return model;
        }
        return new ModelesSasie();
    }
    
    public static ContentModeleSaisie buildBeanContentModel(YvsComptaContentModeleSaisi bean) {
        ContentModeleSaisie cm = new ContentModeleSaisie();
        if (bean != null) {
            cm.getCompteG().setValeur(bean.getCompteGeneral());
            cm.getCompteG().setTypeValeur(bean.getMdsCompteGeneral());
            cm.getCompteT().setValeur(bean.getCompteTiers());
            cm.getCompteT().setTypeValeur(bean.getMdsCompteTiers());
            cm.getCredit().setValeur(bean.getCredit());
            cm.getCredit().setTypeValeur(bean.getMdsCredit());
            cm.getDebit().setValeur(bean.getDebit());
            cm.getDebit().setTypeValeur(bean.getMdsDebit());
            cm.getEcheance().setDate(bean.getEcheance());
            cm.getEcheance().setTypeValeur(bean.getMdsEcheance());
            cm.getJour().setJour(bean.getJour());
            cm.getJour().setTypeValeur(bean.getModeSaisieJour());
            cm.getLibelle().setValeur(bean.getLibelle());
            cm.getLibelle().setTypeValeur(bean.getMdsLibelle());
            cm.getNumPiece().setValeur(bean.getNumPiece());
            cm.getNumPiece().setTypeValeur(bean.getMdsNumPiece());
            cm.getReference().setValeur(bean.getNumRef());
            cm.getReference().setTypeValeur(bean.getMdsNumRef());
            cm.setId(bean.getId());
        }
        return cm;
    }
    
    public static PiecesCompta buildBeanPieceCompta(YvsComptaPiecesComptable y) {
        PiecesCompta r = new PiecesCompta();
        if (y != null) {
            r.setId(y.getId());
            r.setMois(y.getDatePiece());
            r.setDatePiece(y.getDatePiece());
            r.setDateSaise(y.getDateSaisie());
            r.setNumPiece(y.getNumPiece());
            r.setStatutPiece(y.getStatutPiece());
            r.setExercice(UtilMut.buildBeanExercice(y.getExercice()));
            r.setJournal(buildBeanJournaux(y.getJournal()));
            r.setModel(buildBeanModel(y.getModel()));
            r.setSolde(y.getSolde());
            r.getContentsPieces().addAll(y.getContentsPiece());
        }
        return r;
    }
    
    public static YvsComptaPiecesComptable buildPieceCompta(PiecesCompta y, YvsUsersAgence ua) {
        YvsComptaPiecesComptable r = new YvsComptaPiecesComptable();
        if (y != null) {
            r.setId(y.getId());
            r.setDatePiece(y.getDatePiece());
            r.setDateSaisie(y.getDateSaise());
            r.setNumPiece(y.getNumPiece());
            r.setStatutPiece(y.getStatutPiece());
            if (y.getExercice() != null ? y.getExercice().getId() > 0 : false) {
                r.setExercice(new YvsBaseExercice(y.getExercice().getId(), y.getExercice().getReference()));
            }
            if (y.getJournal() != null ? y.getJournal().getId() > 0 : false) {
                r.setJournal(buildBeanJournaux(y.getJournal()));
            }
            if (y.getModel() != null ? y.getModel().getId() > 0 : false) {
                r.setModel(buildBeanModel(y.getModel()));
            }
            r.setSolde(y.getSolde());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
        }
        return r;
    }
    
    public static ContentPieceCompta buildBeanContentPiece(YvsComptaContentJournal y) {
        ContentPieceCompta r = new ContentPieceCompta();
        if (y != null) {
            r.setId(y.getId());
            r.setJour(y.getJour());
            r.setNumPiece(y.getNumPiece());
            r.setNumRef(y.getNumRef());
            r.setCompteG(buildBeanCompte(y.getCompteGeneral()));
            r.setLibelle(y.getLibelle());
            r.setDebit(y.getDebit());
            r.setCredit(y.getCredit());
            r.setEcheance(y.getEcheance());
            r.setPiece(buildBeanPieceCompta(y.getPiece()));
            r.setRefExterne(y.getRefExterne());
            r.setTableExterne(y.getTableExterne());
            r.setSolde(y.getSolde());
            r.setLettrage(y.getLettrage());
            r.setCompteTiers(y.getCompteTiers() != null ? y.getCompteTiers() : 0);
            r.setTableTiers(y.getTableTiers());
            r.setReport(y.getReport());
            r.setAnalytiques(y.getAnalytiques());
        }
        return r;
    }
    
    public static YvsComptaContentJournal buildContentPiece(ContentPieceCompta y, YvsUsersAgence ua) {
        YvsComptaContentJournal r = new YvsComptaContentJournal();
        if (y != null) {
            r.setId(y.getId());
            r.setJour(y.getJour());
            r.setNumPiece(y.getNumPiece());
            r.setNumRef(y.getNumRef());
            r.setLibelle(y.getLibelle());
            r.setDebit(y.getDebit());
            r.setCredit(y.getCredit());
            r.setEcheance(y.getEcheance());
            r.setRefExterne(y.getRefExterne());
            r.setTableExterne(y.getTableExterne());
            r.setSolde(y.getSolde());
            r.setLettrage(y.getLettrage());
            if (y.getCompteG() != null ? y.getCompteG().getId() > 0 : false) {
                r.setCompteGeneral(buildEntityCompte(y.getCompteG()));
            }
//            r.setCompteTiers(y.getCompteTiers());
            r.setCompteTiers(y.getCompteT().getSelectProfil().getIdTiers());
            r.setTableTiers(y.getCompteT().getSelectProfil().getTableTiers());
            if (y.getPiece() != null ? y.getPiece().getId() > 0 : false) {
                r.setPiece(new YvsComptaPiecesComptable(y.getPiece().getId()));
            }
            r.setReport(y.isReport());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
        }
        return r;
    }
    
    public static ContentAnalytique buildBeanContentPiece(YvsComptaContentAnalytique y) {
        ContentAnalytique r = new ContentAnalytique();
        if (y != null) {
            r.setId(y.getId());
            r.setContenu(buildBeanContentPiece(y.getContenu()));
            r.setCentre(buildBeanCentreAnalytique(y.getCentre()));
            r.setDebit(y.getDebit());
            r.setCredit(y.getCredit());
            r.setCoefficient(y.getCoefficient());
            r.setDateSasie(y.getDateSaisie());
            r.setDateSave(y.getDateSave());
        }
        return r;
    }
    
    public static YvsComptaContentAnalytique buildContentPiece(ContentAnalytique y, YvsUsersAgence ua) {
        YvsComptaContentAnalytique r = new YvsComptaContentAnalytique();
        if (y != null) {
            r.setId(y.getId());
            r.setDebit(y.getDebit());
            r.setCredit(y.getCredit());
            r.setCoefficient(y.getCoefficient());
            r.setDateSaisie(y.getDateSasie());
            r.setDateSave(y.getDateSave());
            if (y.getContenu() != null ? y.getContenu().getId() > 0 : false) {
                r.setContenu(buildContentPiece(y.getContenu(), ua));
            }
            if (y.getCentre() != null ? y.getCentre().getId() > 0 : false) {
                r.setCentre(buildCentreAnalytique(y.getCentre(), ua));
            }
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
        }
        return r;
    }
    
    public static AcompteClient buildBeanAcompteClient(YvsComptaAcompteClient y, DaoInterfaceLocal dao) {
        AcompteClient r = new AcompteClient();
        if (y != null) {
            r.setId(y.getId());
            r.setClient(UtilCom.buildSimpleBeanClient(y.getClient()));
            r.setCaisse(buildSimpleBeanCaisse(y.getCaisse()));
            r.setMode(buildBeanModeReglement(y.getModel()));
            r.setCommentaire(y.getCommentaire());
            r.setDateAcompte(y.getDateAcompte());
            r.setMontant(y.getMontant());
            r.setNumRefrence(y.getNumRefrence());
            r.setStatut(y.getStatut());
            r.setNature(y.getNature());
            r.setComptabilise(y.getComptabilise());
            r.setStatutNotif(y.getStatutNotif());
            r.setRepartirAutomatique(y.getRepartirAutomatique());
            r.setDateSave(y.getDateSave());
            r.setReferenceExterne(y.getReferenceExterne());
            r.setPhasesReglement(y.getPhasesReglement());
            for (YvsComptaNotifReglementVente c : y.getNotifs()) {
                r.getVentesEtDivers().add(buildBeanAcomptesVenteDivers(c));
            }
            for (YvsComptaNotifReglementDocDivers c : y.getNotifsDivers()) {
                r.getVentesEtDivers().add(buildBeanAcomptesVenteDivers(c));
            }
            if (dao != null) {
                Double reste = (Double) dao.loadObjectByNameQueries("YvsComptaNotifReglementVente.findResteForAcompte", new String[]{"acompte"}, new Object[]{y});
                y.setReste(reste);
            }
            r.setReste(y.getReste());
        }
        return r;
    }
    
    public static YvsComptaAcompteClient buildAcompteClient(AcompteClient y, YvsUsersAgence ua) {
        YvsComptaAcompteClient r = new YvsComptaAcompteClient();
        if (y != null) {
            r.setId(y.getId());
            if (y.getClient() != null ? y.getClient().getId() > 0 : false) {
                r.setClient(new YvsComClient(y.getClient().getId(), y.getClient().getCodeClient(), y.getClient().getNom(), y.getClient().getPrenom()));
            }
            r.setCaisse(buildBeanCaisse(y.getCaisse()));
            if (y.getMode() != null ? y.getMode().getId() > 0 : false) {
                r.setModel(new YvsBaseModeReglement((long) y.getMode().getId(), y.getMode().getDesignation(), y.getMode().getTypeReglement()));
            }
            r.setComptabilise(y.isComptabilise());
            r.setCommentaire(y.getCommentaire());
            r.setDateAcompte(y.getDateAcompte());
            r.setMontant(y.getMontant());
            r.setNumRefrence(y.getNumRefrence());
            r.setStatut(y.getStatut());
            r.setNature(y.getNature());
            r.setStatutNotif(y.getStatutNotif());
            r.setRepartirAutomatique(y.isRepartirAutomatique());
            r.setAuthor(ua);
            r.setReferenceExterne(y.getReferenceExterne());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setNew_(true);
        }
        return r;
    }
    
    public static CreditClient buildBeanCreditClient(YvsComptaCreditClient y) {
        CreditClient r = new CreditClient();
        if (y != null) {
            r.setId(y.getId());
            r.setClient(UtilCom.buildBeanClient(y.getClient()));
            r.setJournal(buildBeanJournaux(y.getJournal()));
            r.setTypeCredit(UtilGrh.buildBeanTypeCout(y.getTypeCredit()));
            r.setDateCredit(y.getDateCredit());
            r.setMontant(y.getMontant());
            r.setMotif(y.getMotif());
            r.setReste(y.getReste());
            r.setComptabilise(y.getComptabilise());
            r.setNumReference(y.getNumReference());
            r.setReglements(y.getReglements());
            r.setDateSave(y.getDateSave());
            r.setStatut(y.getStatut());
        }
        return r;
    }
    
    public static YvsComptaCreditClient buildCreditClient(CreditClient y, YvsUsersAgence ua) {
        YvsComptaCreditClient r = new YvsComptaCreditClient();
        if (y != null) {
            r.setId(y.getId());
            if (y.getClient() != null ? y.getClient().getId() > 0 : false) {
                r.setClient(new YvsComClient(y.getClient().getId(), y.getClient().getCodeClient(), y.getClient().getNom(), y.getClient().getPrenom()));
            }
            if (y.getJournal() != null ? y.getJournal().getId() > 0 : false) {
                r.setJournal(new YvsComptaJournaux(y.getJournal().getId(), y.getJournal().getCodejournal()));
            }
            if (y.getTypeCredit() != null ? y.getTypeCredit().getId() > 0 : false) {
                r.setTypeCredit(new YvsGrhTypeCout(y.getTypeCredit().getId(), y.getTypeCredit().getLibelle()));
            }
            r.setDateCredit(y.getDateCredit());
            r.setMontant(y.getMontant());
            r.setMotif(y.getMotif());
            r.setNumReference(y.getNumReference());
            r.setComptabilise(y.isComptabilise());
            r.setAuthor(ua);
            r.setStatut(y.getStatut());
            r.setNew_(true);
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
        }
        return r;
    }
    
    public static ReglementCredit buildBeanReglementCredit(YvsComptaReglementCreditClient y) {
        ReglementCredit r = new ReglementCredit();
        if (y != null) {
            r.setId(y.getId());
            r.setNumero(y.getNumero());
            r.setReferenceExterne(y.getReferenceExterne());
            r.setCaisse(buildBeanCaisse(y.getCaisse()));
            r.setCredit(y.getCredit().getId());
            r.setDateReg(y.getDateReg());
            r.setHeureReg(y.getHeureReg());
            r.setValeur(y.getValeur());
            r.setStatut(y.getStatut());
            r.setComptabilise(y.getComptabilise());
            r.setMode(buildBeanModeReglement(y.getModel()));
        }
        return r;
    }
    
    public static YvsComptaReglementCreditClient buildReglementCredit(ReglementCredit y, YvsUsersAgence ua) {
        YvsComptaReglementCreditClient r = new YvsComptaReglementCreditClient();
        if (y != null) {
            r.setId(y.getId());
            r.setNumero(y.getNumero());
            r.setReferenceExterne(y.getReferenceExterne());
            if (y.getCaisse() != null ? y.getCaisse().getId() > 0 : false) {
                r.setCaisse(buildBeanCaisse(y.getCaisse()));
            }
            if (y.getCredit() > 0) {
                r.setCredit(new YvsComptaCreditClient(y.getCredit()));
            }
            if (y.getMode() != null ? y.getMode().getId() > 0 : false) {
                r.setModel(new YvsBaseModeReglement((long) y.getMode().getId(), y.getMode().getDesignation(), y.getMode().getTypeReglement()));
            }
            r.setComptabilise(y.isComptabilise());
            r.setDateReg(y.getDateReg());
            r.setHeureReg(y.getHeureReg());
            r.setValeur(y.getValeur());
            r.setStatut(y.getStatut());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }
    
    public static AcompteFournisseur buildBeanAcompteFournisseur(YvsComptaAcompteFournisseur y, DaoInterfaceLocal dao) {
        AcompteFournisseur r = new AcompteFournisseur();
        if (y != null) {
            r.setId(y.getId());
            r.setFournisseur(UtilCom.buildSimpleBeanFournisseur(y.getFournisseur()));
            r.setCaisse(buildSimpleBeanCaisse(y.getCaisse()));
            r.setMode(buildBeanModeReglement(y.getModel()));
            r.setCommentaire(y.getCommentaire());
            r.setDateAcompte(y.getDateAcompte());
            r.setMontant(y.getMontant());
            r.setNumRefrence(y.getNumRefrence());
            r.setReferenceExterne(y.getReferenceExterne());
            r.setStatut(y.getStatut());
            r.setNature(y.getNature());
            r.setComptabilise(y.getComptabilise());
            r.setStatutNotif(y.getStatutNotif());
            r.setRepartirAutomatique(y.getRepartirAutomatique());
            r.setDateSave(y.getDateSave());
            r.setPhasesReglement(y.getPhasesReglement());
            r.setDatePaiement(y.getDatePaiement());
            for (YvsComptaNotifReglementAchat c : y.getNotifs()) {
                r.getAchatsEtDivers().add(buildBeanAcomptesAchatDivers(c));
            }
            for (YvsComptaNotifReglementDocDivers c : y.getNotifsDivers()) {
                r.getAchatsEtDivers().add(buildBeanAcomptesAchatDivers(c));
            }
            if (dao != null) {
                Double reste = (Double) dao.loadObjectByNameQueries("YvsComptaNotifReglementAchat.findResteForAcompte", new String[]{"acompte"}, new Object[]{y});
                y.setReste(reste);
            }
            r.setReste(y.getReste());
        }
        return r;
    }
    
    public static YvsComptaAcompteFournisseur buildAcompteFournisseur(AcompteFournisseur y, YvsUsersAgence ua) {
        YvsComptaAcompteFournisseur r = new YvsComptaAcompteFournisseur();
        if (y != null) {
            r.setId(y.getId());
            if (y.getFournisseur() != null ? y.getFournisseur().getId() > 0 : false) {
                r.setFournisseur(new YvsBaseFournisseur(y.getFournisseur().getId(), y.getFournisseur().getCodeFsseur(), y.getFournisseur().getNom(), y.getFournisseur().getPrenom()));
            }
            r.setCaisse(buildBeanCaisse(y.getCaisse()));
            if (y.getMode() != null ? y.getMode().getId() > 0 : false) {
                r.setModel(new YvsBaseModeReglement((long) y.getMode().getId(), y.getMode().getDesignation(), y.getMode().getTypeReglement()));
            }
            r.setCommentaire(y.getCommentaire());
            r.setDateAcompte(y.getDateAcompte());
            r.setMontant(y.getMontant());
            r.setNumRefrence(y.getNumRefrence());
            r.setStatut(y.getStatut());
            r.setNature(y.getNature());
            r.setStatutNotif(y.getStatutNotif());
            r.setRepartirAutomatique(y.isRepartirAutomatique());
            r.setAuthor(ua);
            r.setNew_(true);
            r.setDateSave(y.getDateSave());
            r.setReferenceExterne(y.getReferenceExterne());
            r.setDatePaiement(y.getStatut() != Constantes.STATUT_DOC_PAYER ? null : y.getDatePaiement());
            r.setDateUpdate(new Date());
            r.setDatePaiement(y.getDatePaiement());
        }
        return r;
    }
    
    public static CreditFournisseur buildBeanCreditFournisseur(YvsComptaCreditFournisseur y) {
        CreditFournisseur r = new CreditFournisseur();
        if (y != null) {
            r.setId(y.getId());
            r.setFournisseur(UtilCom.buildBeanFournisseur(y.getFournisseur()));
            r.setJournal(buildBeanJournaux(y.getJournal()));
            r.setTypeCredit(UtilGrh.buildBeanTypeCout(y.getTypeCredit()));
            r.setDateCredit(y.getDateCredit());
            r.setMontant(y.getMontant());
            r.setMotif(y.getMotif());
            r.setReste(y.getReste());
            r.setComptabilise(y.getComptabilise());
            r.setNumReference(y.getNumReference());
            r.setReglements(y.getReglements());
            r.setDateSave(y.getDateSave());
            r.setStatut(y.getStatut());
        }
        return r;
    }
    
    public static YvsComptaCreditFournisseur buildCreditFournisseur(CreditFournisseur y, YvsUsersAgence ua) {
        YvsComptaCreditFournisseur r = new YvsComptaCreditFournisseur();
        if (y != null) {
            r.setId(y.getId());
            if (y.getFournisseur() != null ? y.getFournisseur().getId() > 0 : false) {
                r.setFournisseur(new YvsBaseFournisseur(y.getFournisseur().getId(), y.getFournisseur().getCodeFsseur(), y.getFournisseur().getNom(), y.getFournisseur().getPrenom()));
            }
            if (y.getJournal() != null ? y.getJournal().getId() > 0 : false) {
                r.setJournal(new YvsComptaJournaux(y.getJournal().getId(), y.getJournal().getCodejournal()));
            }
            if (y.getTypeCredit() != null ? y.getTypeCredit().getId() > 0 : false) {
                r.setTypeCredit(new YvsGrhTypeCout(y.getTypeCredit().getId(), y.getTypeCredit().getLibelle()));
            }
            r.setDateCredit(y.getDateCredit());
            r.setMontant(y.getMontant());
            r.setMotif(y.getMotif());
            r.setNumReference(y.getNumReference());
            r.setComptabilise(y.isComptabilise());
            r.setAuthor(ua);
            r.setNew_(true);
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setStatut(y.getStatut());
        }
        return r;
    }
    
    public static YvsComptaCaissePieceDivers buildPieceCaisse(PieceTresorerie y, YvsUsersAgence u) {
        return buildTresoreriDocDivers(y, u);
    }
    
    public static YvsComptaCaissePieceDivers buildTresoreriDocDivers(PieceTresorerie y, YvsUsersAgence ua) {
        YvsComptaCaissePieceDivers r = new YvsComptaCaissePieceDivers();
        if (y != null) {
            r.setId(y.getId());
            r.setDatePaimentPrevu(y.getDatePaiement());
            r.setDatePaimentPrevu(y.getDatePaiementPrevu());
            r.setDatePiece(y.getDatePiece());
            r.setNote(y.getDescription());
            r.setMontant(y.getMontant());
            r.setNumPiece(y.getNumPiece());
            r.setStatutPiece(y.getStatutPiece());
            r.setMouvement(y.getMouvement());
            r.setReferenceExterne(y.getNumRefExterne());
            r.setDateValider(y.getDateValide());
            r.setComptabilise(y.isComptabilise());
            if (y.getMode() != null ? y.getMode().getId() > 0 : false) {
                r.setModePaiement(new YvsBaseModeReglement((long) y.getMode().getId(), y.getMode().getDesignation(), y.getMode().getTypeReglement()));
            }
            if (y.getCaisse() != null ? y.getCaisse().getId() > 0 : false) {
                r.setCaisse(new YvsBaseCaisse(y.getCaisse().getId(), y.getCaisse().getIntitule()));
            }
            if (y.getCaissier() != null ? y.getCaissier().getId() > 0 : false) {
                r.setCaissier(new YvsUsers(y.getCaissier().getId(), y.getCaissier().getCodeUsers(), y.getCaissier().getNomUsers()));
            }
            if (y.getValideBy() != null ? y.getValideBy().getId() > 0 : false) {
                r.setValiderBy(new YvsUsers(y.getValideBy().getId(), y.getValideBy().getCodeUsers(), y.getValideBy().getNomUsers()));
            }
            if (y.getDocVente() != null ? y.getDocVente().getId() > 0 : false) {
                r.setDocDivers(new YvsComptaCaisseDocDivers(y.getDocDivers().getId()));
            }
//            r.setCompensations(y.getCompensations());
            r.setAuthor(ua);
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
//            r.setNew_(true);
        }
        return r;
    }
    
    public static ReglementCredit buildBeanReglementCredit(YvsComptaReglementCreditFournisseur y) {
        ReglementCredit r = new ReglementCredit();
        if (y != null) {
            r.setId(y.getId());
            r.setNumero(y.getNumero());
            r.setReferenceExterne(y.getReferenceExterne());
            r.setCaisse(buildBeanCaisse(y.getCaisse()));
            r.setCredit(y.getCredit().getId());
            r.setDateReg(y.getDateReg());
            r.setHeureReg(y.getHeureReg());
            r.setValeur(y.getValeur());
            r.setStatut(y.getStatut());
            r.setComptabilise(y.getComptabilise());
            r.setMode(buildBeanModeReglement(y.getModel()));
        }
        return r;
    }
    
    public static YvsComptaReglementCreditFournisseur buildReglementCreditF(ReglementCredit y, YvsUsersAgence ua) {
        YvsComptaReglementCreditFournisseur r = new YvsComptaReglementCreditFournisseur();
        if (y != null) {
            r.setId(y.getId());
            r.setNumero(y.getNumero());
            r.setReferenceExterne(y.getReferenceExterne());
            if (y.getCaisse() != null ? y.getCaisse().getId() > 0 : false) {
                r.setCaisse(buildBeanCaisse(y.getCaisse()));
            }
            if (y.getCredit() > 0) {
                r.setCredit(new YvsComptaCreditFournisseur(y.getCredit()));
            }
            if (y.getMode() != null ? y.getMode().getId() > 0 : false) {
                r.setModel(new YvsBaseModeReglement((long) y.getMode().getId(), y.getMode().getDesignation(), y.getMode().getTypeReglement()));
            }
            r.setComptabilise(y.isComptabilise());
            r.setDateReg(y.getDateReg());
            r.setHeureReg(y.getHeureReg());
            r.setValeur(y.getValeur());
            r.setStatut(y.getStatut());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }
    
    public static JustifierBon buildBeanJustificatif(YvsComptaJustificatifBon y) {
        JustifierBon r = new JustifierBon();
        if (y != null) {
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
//            r.setPiece(new PieceTresorerie(y.getPiece() != null ? y.getPiece().getId() : 0));
            r.setBon(new BonProvisoire(y.getBon() != null ? y.getBon().getId() : 0));
        }
        return r;
    }
    
    public static YvsComptaJustificatifBon buildJustificatif(JustifierBon y, YvsUsersAgence ua) {
        YvsComptaJustificatifBon r = new YvsComptaJustificatifBon();
        if (y != null) {
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
//            if (y.getPiece() != null ? y.getPiece().getId() > 0 : false) {
//                r.setPiece(new YvsComptaCaissePieceDivers(y.getPiece().getId()));
//            }
//            if (y.getBon() != null ? y.getBon().getId() > 0 : false) {
//                r.setBon(new YvsComptaBonProvisoire(y.getPiece().getId()));
//            }
            r.setAuthor(ua);
        }
        return r;
    }
    
    public static BonProvisoire buildBeanBonProvisoire(YvsComptaBonProvisoire y) {
        BonProvisoire r = new BonProvisoire();
        if (y != null) {
            r.setBeneficiaire(y.getBeneficiaire());
            if (y.getCaisse() != null ? y.getCaisse().getId() > 0 : false) {
                r.setCaisse(new Caisses(y.getCaisse().getId(), y.getCaisse().getIntitule()));
            }
            if (y.getCaissier() != null ? y.getCaissier().getId() > 0 : false) {
                r.setCaissier(new Users(y.getCaissier().getId(), y.getCaissier().getCodeUsers(), y.getCaissier().getNomUsers()));
            }
            r.setDateBon(y.getDateBon());
            r.setDateValider(y.getDateValider());
            r.setDateJustify(y.getDateJustify());
            r.setDatePayer(y.getDatePayer());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setDescription(y.getDescription());
            r.setEtapeTotal(y.getEtapeTotal());
            r.setEtapeValide(y.getEtapeValide());
            r.setEtapesValidations(y.getEtapesValidations());
            r.setId(y.getId());
//            r.setJustificatifs(y.getJustificatifs());
            r.setMontant(y.getMontant());
            r.setNumero(y.getNumero());
            if (y.getOrdonnateur() != null ? y.getOrdonnateur().getId() > 0 : false) {
                r.setOrdonnateur(new Users(y.getOrdonnateur().getId(), y.getOrdonnateur().getCodeUsers(), y.getOrdonnateur().getNomUsers()));
            }
            r.setStatut(y.getStatut().charAt(0));
            r.setStatutJustify(y.getStatutJustify().charAt(0));
            r.setStatutPaiement(y.getStatutPaiement().charAt(0));
            if (y.getTiers() != null ? y.getTiers().getId() > 0 : false) {
                r.setTiers(new Tiers(y.getTiers().getId(), y.getTiers().getCodeTiers(), y.getTiers().getNom(), y.getTiers().getPrenom()));
            }
            r.setTypeDoc(buildBeanTypeDoc(y.getTypeDoc()));
            r.setBonMission(y.getBonMission());
            if (y.getBonMission() != null ? y.getBonMission().getId() > 0 : false) {
                r.setPieceMission(y.getBonMission().getMission());
                r.setMission(r.getPieceMission().getMission());
                r.setNumeroExterne(r.getMission().getNumeroMission());
                r.setBonFor("M");
                r.setOldBonFor("M");
            }
            r.setBonAchat(y.getBonAchat());
            if (y.getBonAchat() != null ? y.getBonAchat().getId() > 0 : false) {
                r.setPieceAchat(y.getBonAchat().getPiece());
                r.setAchat(r.getPieceAchat().getAchat());
                r.setNumeroExterne(r.getAchat().getNumDoc());
                r.setBonFor("A");
                r.setOldBonFor("A");
            }
            r.setBonDivers(y.getBonDivers());
            if (y.getBonDivers() != null ? y.getBonDivers().getId() > 0 : false) {
                r.setPieceDivers(y.getBonDivers().getPiece());
                r.setDivers(r.getPieceDivers().getDocDivers());
                r.setNumeroExterne(r.getDivers().getNumPiece());
                r.setBonFor("D");
                r.setOldBonFor("D");
            }
            r.setJustifier(y.getJustifier());
            r.setAttente(y.getAttente());
            r.setReste(y.getReste());
            r.getJustificatifs().clear();

            // Docs divers
            for (YvsComptaJustificatifBon c : y.getJustificatifs()) {
                r.getJustificatifs().add(buildBeanJustifierBon(c));
            }
            for (YvsComptaJustifBonAchat c : y.getJustificatifsAchats()) {
                r.getJustificatifs().add(buildBeanJustifierBon(c));
            }
            for (YvsComptaJustifBonMission c : y.getJustificatifsMissions()) {
                r.getJustificatifs().add(buildBeanJustifierBon(c));
            }
        }
        return r;
    }
    
    public static YvsComptaBonProvisoire buildBonProvisoire(BonProvisoire y, YvsUsersAgence ua) {
        YvsComptaBonProvisoire r = new YvsComptaBonProvisoire();
        if (y != null) {
            r.setBeneficiaire(y.getBeneficiaire());
            if (y.getCaisse() != null ? y.getCaisse().getId() > 0 : false) {
                r.setCaisse(new YvsBaseCaisse(y.getCaisse().getId(), y.getCaisse().getIntitule()));
            }
            if (y.getCaissier() != null ? y.getCaissier().getId() > 0 : false) {
                r.setCaissier(new YvsUsers(y.getCaissier().getId(), y.getCaissier().getCodeUsers(), y.getCaissier().getNomUsers()));
            }
            r.setDateBon(y.getDateBon());
            r.setDateSave(y.getDateSave());
            r.setDateValider(y.getDateValider());
            r.setDateJustify(y.getDateJustify());
            r.setDatePayer(y.getDatePayer());
            r.setDateUpdate(new Date());
            r.setDescription(y.getDescription());
            r.setEtapeTotal(y.getEtapeTotal());
            r.setEtapeValide(y.getEtapeValide());
            r.setEtapesValidations(y.getEtapesValidations());
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setNumero(y.getNumero());
            if (y.getOrdonnateur() != null ? y.getOrdonnateur().getId() > 0 : false) {
                r.setOrdonnateur(new YvsUsers(y.getOrdonnateur().getId(), y.getOrdonnateur().getCodeUsers(), y.getOrdonnateur().getNomUsers()));
            }
            r.setStatut(y.getStatut() + "");
            r.setStatutJustify(y.getStatutJustify() + "");
            r.setStatutPaiement(y.getStatutPaiement() + "");
            if (y.getTiers() != null ? y.getTiers().getId() > 0 : false) {
                r.setTiers(new YvsBaseTiers(y.getTiers().getId(), y.getTiers().getCodeTiers(), y.getTiers().getNom(), y.getTiers().getPrenom()));
            }
            if (y.getTypeDoc() != null ? y.getTypeDoc().getId() > 0 : false) {
                r.setTypeDoc(new YvsBaseTypeDocDivers(y.getTypeDoc().getId(), y.getTypeDoc().getLibelle()));
            }
            r.setAuthor(ua);
            r.setAgence(ua.getAgence());
            r.setNew_(true);
        }
        return r;
    }
    
    public static JustifierBon buildBeanJustifierBon(YvsComptaJustificatifBon c) {
        JustifierBon ju = new JustifierBon();
        ju.setDateJustifier(c.getPiece().getDateValider());
        ju.setDateSave(c.getDateSave());
        ju.setId(c.getId());
        ju.setMontant(c.getPiece().getMontant());
        ju.setPiceDivers(c.getPiece());
        ju.setStatutPiece(c.getPiece().getStatutPiece());
        ju.setCaisse(c.getPiece().getCaisse());
        ju.setNumeroPiece(c.getPiece().getNumPiece());
        ju.setRefExterne(c.getPiece().getDocDivers().getNumPiece());
        ju.setDatePiece(c.getPiece().getDateValider());
        ju.setTiers(new YvsBaseTiers(c.getPiece().getDocDivers().getIdTiers(), c.getBon().getBeneficiaire()));
        ju.setType("OD");
        return ju;
    }
    
    public static JustifierBon buildBeanJustifierBon(YvsComptaJustifBonAchat c) {
        JustifierBon ju = new JustifierBon();
        ju.setDateJustifier(c.getPiece().getDatePaiement());
        ju.setDateSave(c.getDateSave());
        ju.setId(c.getId());
        ju.setMontant(c.getPiece().getMontant());
        ju.setPieceAchat(c.getPiece());
        ju.setStatutPiece(c.getPiece().getStatutPiece());
        ju.setCaisse(c.getPiece().getCaisse());
        ju.setNumeroPiece(c.getPiece().getNumeroPiece());
        ju.setRefExterne(c.getPiece().getAchat().getNumPiece());
        ju.setDatePiece(c.getPiece().getDatePaiement());
        ju.setTiers(c.getPiece().getAchat().getFournisseur().getTiers());
        ju.setType("FA");
        return ju;
    }
    
    public static JustifierBon buildBeanJustifierBon(YvsComptaJustifBonMission c) {
        JustifierBon ju = new JustifierBon();
        ju.setDateJustifier(c.getMission().getDatePaiement());
        ju.setDateSave(c.getDateSave());
        ju.setId(c.getId());
        ju.setMontant(c.getMission().getMontant());
        ju.setPieceMission(c.getMission());
        ju.setStatutPiece(c.getMission().getStatutPiece());
        ju.setCaisse(c.getMission().getCaisse());
        ju.setNumeroPiece(c.getMission().getNumeroPiece());
        ju.setRefExterne(c.getMission().getMission().getNumeroMission());
        ju.setDatePiece(c.getMission().getDatePaiement());
        ju.setTiers(c.getMission().getMission().getEmploye().getCompteTiers());
        ju.setType("MI");
        return ju;
        
    }
    
    public static AcomptesVenteDivers buildBeanAcomptesVenteDivers(YvsComptaNotifReglementVente c) {
        AcomptesVenteDivers p = new AcomptesVenteDivers();
        p.setId(c.getId());
        p.setNotifs(c);
        if (c.getPieceVente() != null) {
            p.setDateReglement(c.getPieceVente().getDatePaimentPrevu());
            p.setMontant(c.getPieceVente().getMontant());
            p.setStatutPiece(c.getPieceVente().getStatutPiece().toString());
            p.setPiece(c.getPieceVente().getNumeroPiece());
            p.setComptabilise(c.getPieceVente().getComptabilise());
            p.setVentes(c.getPieceVente().getVente());
            if (c.getPieceVente().getVente() != null) {
                p.setDatePiece(c.getPieceVente().getVente().getEnteteDoc().getDateEntete());
                p.setNumero(c.getPieceVente().getVente().getNumDoc());
            }
        }
        if (c.getAcompte() != null) {
            p.setMode(c.getAcompte().getModel());
            p.setClient(c.getAcompte().getClient().getNom_prenom());
        }
        p.setType("VENTE");
        return p;
    }
    
    public static AcomptesVenteDivers buildBeanAcomptesVenteDivers(YvsComptaNotifReglementDocDivers c) {
        AcomptesVenteDivers p = new AcomptesVenteDivers();
        p.setId(c.getId());
        p.setNotif_divers(c);
        if (c.getPieceDocDivers() != null) {
            p.setDateReglement(c.getPieceDocDivers().getDatePaimentPrevu());
            p.setMontant(c.getPieceDocDivers().getMontant());
            p.setMode(c.getPieceDocDivers().getModePaiement());
            p.setComptabilise(c.getPieceDocDivers().getComptabilise());
            p.setStatutPiece(c.getPieceDocDivers().getStatutPiece().toString());
            p.setPiece(c.getPieceDocDivers().getNumPiece());
            p.setDivers(c.getPieceDocDivers().getDocDivers());
            if (c.getPieceDocDivers().getDocDivers() != null) {
                p.setDatePiece(c.getPieceDocDivers().getDocDivers().getDateSave());
                p.setNumero(c.getPieceDocDivers().getDocDivers().getNumPiece());
            }
        }
        if (c.getAcompteClient() != null) {
            p.setClient(c.getAcompteClient().getClient().getNom_prenom());
        }
        p.setType("OD_V");
        return p;
        
    }
    
    public static AcomptesAchatDivers buildBeanAcomptesAchatDivers(YvsComptaNotifReglementAchat c) {
        AcomptesAchatDivers p = new AcomptesAchatDivers();
        p.setId(c.getId());
        p.setNotifs(c);
        if (c.getPieceAchat() != null) {
            p.setDateReglement(c.getPieceAchat().getDatePaimentPrevu());
            p.setMontant(c.getPieceAchat().getMontant());
            p.setPiece(c.getPieceAchat().getNumeroPiece());
            p.setStatutPiece(c.getPieceAchat().getStatutPiece().toString());
            p.setAchats(c.getPieceAchat().getAchat());
            if (c.getPieceAchat().getAchat() != null) {
                p.setDatePiece(c.getPieceAchat().getAchat().getDateDoc());
                p.setNumero(c.getPieceAchat().getAchat().getNumDoc());
            }
        }
        if (c.getAcompte() != null) {
            p.setMode(c.getAcompte().getModel());
            p.setFournisseur(c.getAcompte().getFournisseur().getNom_prenom());
        }
        p.setType("ACHAT");
        return p;
    }
    
    public static AcomptesAchatDivers buildBeanAcomptesAchatDivers(YvsComptaNotifReglementDocDivers c) {
        AcomptesAchatDivers p = new AcomptesAchatDivers();
        p.setId(c.getId());
        p.setNotif_divers(c);
        if (c.getPieceDocDivers() != null) {
            p.setDateReglement(c.getPieceDocDivers().getDatePaimentPrevu());
            p.setMontant(c.getPieceDocDivers().getMontant());
            p.setMode(c.getPieceDocDivers().getModePaiement());
            p.setStatutPiece(c.getPieceDocDivers().getStatutPiece().toString());
            p.setPiece(c.getPieceDocDivers().getNumPiece());
            p.setDivers(c.getPieceDocDivers().getDocDivers());
            if (c.getPieceDocDivers().getDocDivers() != null) {
                p.setDatePiece(c.getPieceDocDivers().getDocDivers().getDateSave());
                p.setNumero(c.getPieceDocDivers().getDocDivers().getNumPiece());
            }
        }
        if (c.getAcompteFournisseur() != null) {
            p.setFournisseur(c.getAcompteFournisseur().getFournisseur().getNom_prenom());
        }
        p.setType("OD_A");
        return p;
    }
    
    public static YvsComMensualiteFactureVente buildMensualiteVente(MensualiteFactureVente y, YvsUsersAgence ua) {
        YvsComMensualiteFactureVente r = new YvsComMensualiteFactureVente();
        if (y != null) {
            r.setNew_(true);
            r.setReste(y.getMontantRest());
            r.setOutDelai(y.isOutDelai());
            r.setNameMens(y.getNameMens());
            r.setMontant(y.getMontant());
            if (y.getModeReglement() != null ? y.getModeReglement().getId() > 0 : false) {
                r.setModeReglement(new YvsBaseModeReglement((long) y.getModeReglement().getId()));
            }
            r.setId(y.getId());
            if (y.getFacture() != null ? y.getFacture().getId() > 0 : false) {
                r.setFacture(new YvsComDocVentes(y.getFacture().getId()));
            }
            r.setEtat(y.getEtat());
            r.setDateUpdate(new Date());
            r.setDateSave(y.getDateSave());
            r.setDateMensualite(y.getDateMensualite());
            r.setAvance(y.getAvance());
            r.setAuthor(ua);
        }
        return r;
    }
    
    public static MensualiteFactureVente buildBeanMensualiteVente(YvsComMensualiteFactureVente y) {
        MensualiteFactureVente r = new MensualiteFactureVente();
        if (y != null) {
            r.setNew_(true);
            r.setMontantRest(y.getReste());
            r.setOutDelai(y.isOutDelai());
            r.setNameMens(y.getNameMens());
            r.setMontant(y.getMontant());
            r.setModeReglement(y.getModeReglement() != null ? new ModeDeReglement(y.getModeReglement().getId().intValue()) : new ModeDeReglement());
            r.setId(y.getId());
            r.setFacture(y.getFacture() != null ? new DocVente(y.getFacture().getId()) : new DocVente());
            r.setEtat(y.getEtat());
            r.setDateSave(y.getDateSave());
            r.setDateMensualite(y.getDateMensualite());
            r.setAvance(y.getAvance());
        }
        return r;
    }
    
    public static YvsBaseTypeDocDivers buildBeanTypeDoc(TypeDocDivers bean, YvsUsersAgence ua) {
        YvsBaseTypeDocDivers y = new YvsBaseTypeDocDivers();
        if (bean != null) {
            y.setId(bean.getId());
            y.setActif(bean.isActif());
            y.setDescription(bean.getDescription());
            y.setLibelle(bean.getLibelle());
            y.setModule(bean.getModule());
            if (bean.getCodeAcces() != null ? bean.getCodeAcces().getId() > 0 : false) {
                y.setCodeAcces(new YvsBaseCodeAcces(bean.getCodeAcces().getId()));
            }
            y.setCanReception(bean.isCanReception());
            y.setAuthor(ua);
            y.setDateSave(bean.getDateSave());
            y.setDateUpdate(new Date());
        }
        return y;
    }
    
    public static TypeDocDivers buildBeanTypeDoc(YvsBaseTypeDocDivers bean) {
        TypeDocDivers y = new TypeDocDivers();
        if (bean != null) {
            y.setId(bean.getId());
            y.setActif(bean.getActif());
            y.setDescription(bean.getDescription());
            y.setLibelle(bean.getLibelle());
            y.setModule(bean.getModule());
            y.setCanReception(bean.getCanReception());
            y.setDateSave(bean.getDateSave());
            if (bean.getCodeAcces() != null) {
                y.setCodeAcces(new CodeAcces(bean.getCodeAcces().getId(), bean.getCodeAcces().getCode()));
            } else {
                y.setCodeAcces(new CodeAcces());
            }
            y.setCategories(bean.getCategories());
        }
        return y;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import yvs.base.UtilBase;
import yvs.base.compta.ModeDeReglement;
import yvs.base.compta.UtilCompta;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.grh.activite.YvsGrhCongeEmps;
import yvs.entity.grh.activite.YvsContactsEmps;
import yvs.entity.grh.activite.YvsCoutsFormation;
import yvs.entity.grh.activite.YvsDemandeEmps;
import yvs.entity.grh.activite.YvsGrhFormateur;
import yvs.entity.grh.activite.YvsGrhFormation;
import yvs.entity.grh.activite.YvsGrhFormationEmps;
import yvs.entity.grh.activite.YvsGrhMissionRessource;
import yvs.entity.grh.activite.YvsGrhSanctionEmps;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.grh.activite.YvsGrhGrilleMission;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.activite.YvsGrhQualificationFormation;
import yvs.entity.grh.taches.YvsGrhTacheEmps;
import yvs.entity.grh.taches.YvsGrhTaches;
import yvs.entity.grh.activite.YvsTypeDemande;
import yvs.entity.grh.contrat.YvsGrhContratSuspendu;
import yvs.entity.grh.contrat.YvsGrhElementsIndemnite;
import yvs.entity.grh.contrat.YvsGrhGrilleTauxFinContrat;
import yvs.entity.grh.contrat.YvsGrhLibelleDroitFinContrat;
import yvs.entity.grh.contrat.YvsGrhParamContrat;
import yvs.entity.grh.contrat.YvsGrhRubriqueIndemnite;
import yvs.entity.grh.param.YvsCalendrier;
import yvs.entity.grh.param.YvsGrhCategorieProfessionelle;
import yvs.entity.grh.param.YvsGrhConventionCollective;
import yvs.entity.grh.param.YvsGrhEchelons;
import yvs.entity.grh.param.YvsGrhActivitesPoste;
import yvs.entity.grh.param.YvsGrhDecisionSanction;
import yvs.entity.grh.param.poste.YvsGrhDepartement;
//import yvs.entity.grh.param.YvsGrhDetailTypeChomage;
import yvs.entity.grh.param.YvsGrhFauteSanction;
import yvs.entity.grh.param.YvsGrhGradeEmploye;
import yvs.entity.grh.param.YvsGrhIntervalPrimeTache;
import yvs.entity.grh.param.YvsGrhMissionPoste;
import yvs.entity.grh.param.YvsGrhPrimeTache;
import yvs.entity.grh.param.YvsGrhSanction;
import yvs.entity.grh.param.YvsGrhTypeChomage;
import yvs.entity.grh.taches.YvsGrhIntervalMajoration;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.param.YvsGrhMajorationConge;
import yvs.entity.grh.taches.YvsGrhMontantTache;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.param.poste.YvsGrhPosteDeTravail;
import yvs.entity.grh.param.YvsGrhQualifications;
import yvs.entity.grh.taches.YvsGrhRegleTache;
import yvs.entity.grh.personnel.YvsBanques;
import yvs.entity.grh.personnel.YvsBulletin;
import yvs.entity.grh.personnel.YvsCategorieProEmploye;
import yvs.entity.grh.personnel.YvsCompteBancaire;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.personnel.YvsGrhConventionEmploye;
import yvs.entity.grh.personnel.YvsDiplomeEmploye;
import yvs.entity.grh.personnel.YvsDiplomes;
import yvs.entity.grh.personnel.YvsEchelonEmploye;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsGrhAssurance;
import yvs.entity.grh.personnel.YvsGrhAssureur;
import yvs.entity.grh.personnel.YvsGrhBilanAssurance;
import yvs.entity.grh.personnel.YvsGrhBilanCouverture;
import yvs.entity.grh.personnel.YvsGrhDetailsChomageTechnique;
import yvs.entity.grh.personnel.YvsGrhCouverturePersonneCharge;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.contrat.YvsGrhModelElementAdditionel;
import yvs.entity.grh.personnel.YvsGrhPersonneEnCharge;
import yvs.entity.grh.personnel.YvsGrhTypeAssurance;
import yvs.entity.grh.contrat.YvsGrhTypeElementAdditionel;
import yvs.entity.grh.param.YvsGrhDomainesQualifications;
import yvs.entity.grh.param.poste.YvsGrhModelPrimePoste;
import yvs.entity.grh.param.poste.YvsGrhQualificationPoste;
import yvs.entity.grh.personnel.YvsLangueEmps;
import yvs.entity.grh.personnel.YvsLangues;
import yvs.entity.grh.personnel.YvsPermisDeCoduire;
import yvs.entity.grh.param.poste.YvsGrhPosteEmployes;
import yvs.entity.grh.personnel.YvsGrhPlanningEmploye;
import yvs.entity.grh.personnel.YvsGrhProfilEmps;
import yvs.entity.grh.personnel.YvsGrhQualificationEmploye;
import yvs.entity.grh.personnel.YvsSpecialiteDiplomes;
import yvs.entity.grh.personnel.YvsTypeContrat;
import yvs.entity.grh.presence.YvsGrhPointage;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.grh.presence.YvsGrhTypeValidation;
import yvs.entity.grh.recrutments.YvsGrhCandidats;
import yvs.entity.grh.recrutments.YvsGrhDiplomesCandidat;
import yvs.entity.grh.recrutments.YvsGrhEntretienCandidat;
import yvs.entity.grh.recrutments.YvsGrhLanguesCandidats;
import yvs.entity.grh.recrutments.YvsGrhParamQuestionnaire;
import yvs.entity.grh.recrutments.YvsGrhQualificationCandidat;
import yvs.entity.grh.recrutments.YvsGrhRubriquesQuestionnaire;
import yvs.entity.grh.salaire.YvsGrhCategorieElement;
import yvs.entity.grh.salaire.YvsGrhElementSalaire;
import yvs.entity.grh.salaire.YvsGrhBulletins;
import yvs.entity.grh.salaire.YvsGrhCentreDepense;
import yvs.entity.grh.salaire.YvsGrhDepenseNote;
import yvs.entity.grh.salaire.YvsGrhDetailBulletin;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.grh.salaire.YvsGrhNotesFrais;
import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
import yvs.entity.grh.salaire.YvsGrhPlanifSalaireContrat;
import yvs.entity.grh.salaire.YvsGrhRubriqueBulletin;
import yvs.entity.grh.salaire.YvsGrhTypeDepense;
import yvs.entity.grh.salaire.YvsGrhStructureSalaire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsModePaiement;
import yvs.entity.param.YvsGrhPlanPrelevement;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsers;
import yvs.grh.assurance.Assurance;
import yvs.grh.assurance.Assureur;
import yvs.grh.assurance.BilanAssurance;
import yvs.grh.assurance.TypeAssurance;
import yvs.grh.bean.Banques;
import yvs.grh.bean.BulletinEmps;
import yvs.grh.bean.Categories;
import yvs.grh.bean.DetailChomagesTec;
import yvs.grh.bean.CompteBancaire;
import yvs.grh.bean.Conges;
import yvs.grh.bean.ContactEmps;
import yvs.grh.bean.ContratEmploye;
import yvs.grh.bean.Contrats;
import yvs.grh.bean.Convention;
import yvs.grh.bean.CoutFormation;
import yvs.grh.bean.Demande;
import yvs.parametrage.poste.Departements;
import yvs.grh.bean.Diplomes;
import yvs.grh.bean.DomainesQualifications;
import yvs.grh.bean.Echelons;
import yvs.grh.bean.ElementAdditionnel;
import yvs.grh.bean.Employe;
import yvs.grh.bean.EmployePartial;
import yvs.grh.bean.Formateur;
import yvs.grh.bean.Formation;
import yvs.grh.bean.FormationEmps;
import yvs.grh.bean.GradeEmploye;
import yvs.grh.bean.mission.GrilleFraisMission;
import yvs.grh.bean.Langues;
import yvs.grh.bean.mission.Mission;
import yvs.parametrage.poste.MissionPoste;
import yvs.grh.bean.MissionRessource;
import yvs.grh.bean.OptionEltSalaire;
import yvs.grh.bean.PermisDeConduire;
import yvs.grh.bean.PersonneEnCharge;
import yvs.grh.bean.PlanningWork;
import yvs.parametrage.poste.PosteDeTravail;
import yvs.grh.bean.PrimeTache;
import yvs.grh.bean.Profils;
import yvs.grh.bean.Qualification;
import yvs.grh.bean.QualificationFormation;
import yvs.parametrage.poste.QualificationPoste;
import yvs.grh.bean.RegleDeTache;
import yvs.grh.bean.Secteurs;
import yvs.grh.bean.TacheEmps;
import yvs.grh.bean.Taches;
import yvs.grh.bean.TypeContrat;
import yvs.grh.bean.TypeCout;
import yvs.grh.bean.TypeDemande;
import yvs.grh.bean.TypeElementAdd;
import yvs.grh.contrat.ElementIndemnite;
import yvs.grh.contrat.FinDeContrats;
import yvs.grh.contrat.GrilleTauxFinContrat;
import yvs.grh.contrat.LibelleDroitFinContrat;
import yvs.grh.contrat.ParamContrat;
import yvs.grh.contrat.RubriqueIndemnite;
import yvs.grh.majoration.IntervalMajoration;
import yvs.grh.majoration.MajorationConge;
import yvs.grh.paie.BulletinSalaire;
import yvs.grh.paie.CategorieRegleSalaire;
import yvs.grh.paie.DetailsBulletin;
import yvs.grh.paie.ElementSalaire;
import yvs.grh.paie.OrdreCalculSalaire;
import yvs.grh.paie.PrelevementEmps;
import yvs.grh.paie.RubriqueBulletin;
import yvs.grh.paie.StructureElementSalaire;
import yvs.grh.paie.noteDeFrais.CentreDepense;
import yvs.grh.paie.noteDeFrais.Depenses;
import yvs.grh.paie.noteDeFrais.NoteDeFrais;
import yvs.grh.paie.noteDeFrais.TypeDepense;
import yvs.grh.parametre.ParamsTauxChomageTechnique;
import yvs.grh.presence.PointageEmploye;
import yvs.grh.presence.TrancheHoraire;
import yvs.grh.presence.TypeValidation;
import yvs.grh.recrutement.DiplomeCandidat;
import yvs.grh.recrutement.EntretienCandidat;
import yvs.grh.recrutement.SpecialitesDiplomeCandidat;
import yvs.grh.recrutement.entretien.DomainesQualificationCandidat;
import yvs.grh.recrutement.entretien.Candidats;
import yvs.grh.recrutement.entretien.LangueCandidat;
import yvs.grh.recrutement.entretien.QualificationCandidat;
import yvs.grh.recrutement.entretien.QuestionsEntretien;
import yvs.grh.recrutement.entretien.RubriquesQuestionnaire;
import yvs.grh.sanction.DecisionSanction;
import yvs.grh.sanction.FauteSanction;
import yvs.grh.sanction.Sanction;
import yvs.grh.sanction.SanctionEmps;
import yvs.parametrage.PlanPrelevement;
import yvs.parametrage.societe.Societe;
import yvs.parametrage.societe.UtilSte;
import static yvs.parametrage.societe.UtilSte.buildBeanSociete;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.agence.SecteurActivite;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.poste.DomainesQualificationPoste;
import yvs.parametrage.poste.ElementAdditionnelPoste;
import yvs.parametrage.poste.SpecialiteDiplomes;
import yvs.base.produits.Articles;
import yvs.production.UtilProd;
import yvs.base.tiers.UtilTiers;
import yvs.base.tresoreri.ModePaiement;
import yvs.commercial.UtilCom;
import yvs.comptabilite.caisse.Caisses;
import yvs.entity.base.YvsBaseDepartement;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.grh.activite.YvsGrhDetailGrilleFraiMission;
import yvs.entity.grh.activite.YvsGrhObjetsMission;
import yvs.entity.grh.activite.YvsGrhObjetsMissionAnalytique;
import yvs.entity.grh.param.YvsGrhCategoriePreavis;
import yvs.entity.grh.param.poste.YvsGrhModelContrat;
import yvs.entity.grh.param.poste.YvsGrhPreavis;
import yvs.entity.grh.personnel.YvsGrhProfil;
import yvs.entity.grh.presence.YvsGrhEquipeEmploye;
import yvs.entity.grh.presence.YvsGrhPresence;
import yvs.entity.param.YvsBaseTarifPointLivraison;
import yvs.entity.param.YvsDictionnaireInformations;
import yvs.entity.param.YvsSocietes;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.bean.ConventionEmploye;
import yvs.grh.bean.EquipeEmploye;
import yvs.grh.bean.mission.DetailFraisMission;
import yvs.grh.bean.mission.ObjetMissionAnalytique;
import yvs.grh.bean.mission.ObjetMission;
import yvs.grh.contrat.ModelContrat;
import yvs.grh.contrat.Preavis;
import yvs.grh.presence.PresenceEmploye;
import yvs.parametrage.agence.UtilAgence;
import yvs.parametrage.dico.TarifLieux;
import yvs.users.Users;
import yvs.util.Constantes;
import yvs.util.Utilitaire;

/**
 *
 * @author GOUCHERE YVES
 */
public class UtilGrh {

    @EJB
    public static DaoInterfaceLocal dao;

    public static OrdreCalculSalaire buildBeanOrdre(YvsGrhOrdreCalculSalaire o) {
        OrdreCalculSalaire ob = new OrdreCalculSalaire();
        ob.setDateExecution(o.getDateExecution());
        ob.setDateJour(new Date());
        ob.setDebutMois(o.getDebutMois());
        ob.setFinMois(o.getFinMois());
        ob.setHeureExecution(o.getHeureExecution());
        ob.setReference(o.getReference());
        ob.setRealise(false);
        ob.setId(o.getId());
        ob.setCloture(o.getCloture());
        ob.setDebutTraitement(o.getDateDebutTraitement());
        ob.setFinTraitement(o.getDateFinTraitement());
        return ob;
    }

    public static GradeEmploye buildBeanGradeEmploye(YvsGrhGradeEmploye g) {
        GradeEmploye r = new GradeEmploye();
        if (g != null) {
            r.setId(g.getId());
            r.setLibelle(g.getLibelle());
        }
        return r;
    }

    public static List<GradeEmploye> buildBeanListGradeEmploye(List<YvsGrhGradeEmploye> list) {
        List<GradeEmploye> r = new ArrayList<>();
        if (list != null) {
            for (YvsGrhGradeEmploye g : list) {
                r.add(buildBeanGradeEmploye(g));
            }
        }
        return r;
    }

    public static BulletinEmps buildBeanBulletin(YvsBulletin b) {
        BulletinEmps be = new BulletinEmps();
        if (b != null) {
            be.setId(b.getId());
            be.setHoraire(b.getHoraire());
            be.setHoraireHebdo(b.getHoraireHebdo());
            be.setModePaiement(b.getModeDePayement());
            be.setSalaireHoraire(b.getSalaireHoraire());
            be.setSalaireMens(b.getSalaireMens());
        }
        return be;
    }

    public static Profils buildBeanProfil(YvsGrhProfilEmps p) {
        if (p != null) {
            Profils pr = new Profils();
            pr.setGrade((p.getGrade() != null) ? buildBeanGradeEmploye(p.getGrade()) : new GradeEmploye());
            pr.setId(p.getId());
            pr.setStatut(p.getStatut());
            return pr;
        } else {
            return new Profils();
        }
    }

    public static Profils buildBeanProfil(List<YvsGrhProfilEmps> lp) {
        if (lp != null) {
            for (YvsGrhProfilEmps pe : lp) {
                if (pe.getActif()) {
                    return buildBeanProfil(pe);
                }
            }
        }
        return new Profils();
    }

    public static List<Echelons> buildBeanEchelons(List<YvsGrhConventionCollective> lc) {
        List<Echelons> re = new ArrayList<>();
        for (YvsGrhConventionCollective cc : lc) {
            re.add(buildBeanEchelon(cc.getEchelon()));
        }
        return re;
    }

    public static List<Echelons> buildBeanEchelonsByEch(List<YvsGrhEchelons> lc) {
        List<Echelons> re = new ArrayList<>();
        for (YvsGrhEchelons cc : lc) {
            re.add(buildBeanEchelon(cc));
        }
        return re;
    }

    public static Categories buildBeanCategoriePro(YvsGrhCategorieProfessionelle c) {
        if (c != null) {
            if (c.getCategorie() != null) {
                Categories cp = new Categories(c.getId());
                cp.setCategorie(c.getCategorie());
                cp.setDegre(c.getDegre());
//                cp.getListEchelons().addAll(buildBeanEchelons(c.getListEchelons()));
                return cp;
            }
        }
        return new Categories();
    }

    public static Categories buildBeanCategorieEmploye(YvsCategorieProEmploye c) {
        if (c != null) {
            if (c.getCategorie() != null) {
                Categories cp = new Categories(c.getCategorie().getId(), c.getCategorie().getCategorie());
                cp.setCategorieActif(c.getId());
                return cp;
            }
        }
        return new Categories();
    }

    public static Echelons buildBeanEchelonEmploye(YvsEchelonEmploye e) {
        if (e != null) {
            if (e.getEchelon() != null) {
                Echelons ec = new Echelons(e.getEchelon().getId(), e.getEchelon().getEchelon());
                ec.setEchelonActif(e.getId());
                return ec;
            }
        }
        return new Echelons();
    }

    public static Echelons buildBeanEchelon(YvsGrhEchelons e) {
        if (e != null) {
            if (e.getEchelon() != null) {
                Echelons ec = new Echelons(e.getId());
                ec.setDegre(e.getDegre());
                ec.setEchelon(e.getEchelon());
                return ec;
            }
        }
        return new Echelons();
    }

    public static PermisDeConduire buildBeanPermis(YvsPermisDeCoduire p) {
        if (p != null) {
            PermisDeConduire pc = new PermisDeConduire();
            pc.setCategorie(p.getCategorie());
            pc.setDateExp(p.getDateExpiration());
            pc.setDateObtention(p.getDateObtention());
            pc.setId((p.getId() != null) ? p.getId() : 0);
            pc.setNumero(p.getNumero());
            pc.setExpire(p.getDateExpiration().before(new Date()));
            return pc;
        }
        return new PermisDeConduire();
    }

    public static PosteDeTravail buildBeanPoste(YvsGrhPosteDeTravail p) {
        PosteDeTravail pe = new PosteDeTravail();
        if (p != null) {
            pe.setId(p.getId());
            pe.setIntitule(p.getIntitule());
            p.setNombrePlace(p.getNombrePlace());
            pe.setDepartement((p.getDepartement() != null) ? new Departements(p.getDepartement().getId(), p.getDepartement().getIntitule(), p.getDepartement().getAbreviation(), p.getDepartement().getDescription()) : new Departements());
            pe.setFraisMission(buildGrilleMission(p.getFraisMission()));
            pe.setNiveauRequis(buildDiplome(p.getNiveau()));
            pe.setActif((p.getActif() != null) ? p.getActif() : false);
            p.setNombrePlace(p.getNombrePlace());
            pe.setCongeAcquis(p.getCongeAcquis());
            pe.setDureePreavie(p.getDureePreavis());
            pe.setModePaiement(buildBeanModePaiement(p.getModePaiement()));
            pe.setSalaireHoraire(p.getSalaireHoraire());
            pe.setSalaireMensuel(p.getSalaireMensuel());
            pe.setStructSalaire(buildBeanStructure(p.getStructureSalaire()));
            pe.setUnitePreavis(p.getUniteDureePreavis());
            pe.setNiveauRequis(buildDiplome(p.getNiveau()));
            pe.setGrade(p.getGrade());
            pe.setDateSave(p.getDateSave());
//            if (p.getPostesEmployes() != null) {
//                if (!p.getPostesEmployes().isEmpty()) {
//                    for (YvsGrhPosteEmployes po : p.getPostesEmployes()) {
//                        if (po.getEmploye().getSolde() != null) {
//                            pe.setEtatEndettement(pe.getEtatEndettement() + po.getEmploye().getSolde());
//                        }
//                    }
//                    pe.setTotalEmploye(p.getPostesEmployes().size());
//                }
//            }
            //récupère les missions
//            pe.setMissions(buildMissionPoste(p.getMissions()));
            //récupère les activités
//            pe.setActivites(buildActivitePoste(p.getActivites()));
//            pe.setQualifications(buildOrderBeanQualificationPoste(p.getQualifications()));
            pe.setModelContrat(buildBeanModelContrat(p.getModelContrat()));
        }
        return pe;
    }

    public static PosteDeTravail builSimpledBeanPoste(YvsGrhPosteDeTravail p) {
        if (p != null) {
            PosteDeTravail pe = new PosteDeTravail();
            pe.setId(p.getId());
            pe.setIntitule(p.getIntitule());
            pe.setDepartement((p.getDepartement() != null) ? new Departements(p.getDepartement().getId(), p.getDepartement().getIntitule()) : new Departements());
            pe.setFraisMission(buildGrilleMission(p.getFraisMission()));
            p.setDescriptionPoste(p.getDescriptionPoste());
            pe.setIntitule(p.getIntitule());
            pe.setIntitule(p.getIntitule());
            pe.setActif(p.getActif());
            p.setNombrePlace(p.getNombrePlace());
            pe.setCongeAcquis(p.getCongeAcquis());
            pe.setDureePreavie(p.getDureePreavis());
            pe.setModePaiement(buildBeanModePaiement(p.getModePaiement()));
            pe.setSalaireHoraire(p.getSalaireHoraire());
            pe.setSalaireMensuel(p.getSalaireMensuel());
            pe.setStructSalaire(buildBeanStructure(p.getStructureSalaire()));
            pe.setUnitePreavis(p.getUniteDureePreavis());
            pe.setNiveauRequis(buildDiplome(p.getNiveau()));
            pe.setGrade(p.getGrade());
            pe.setNombrePlace(p.getNombrePlace());
            pe.setPosteSuperieur((p.getPosteSuperieur() != null) ? buildBeanPoste(p.getPosteSuperieur()) : new PosteDeTravail());
            pe.setIdPosteSup((p.getPosteSuperieur() != null) ? p.getPosteSuperieur().getId() : 0);
            //récupère les missions
            pe.setMissions(buildMissionPoste(p.getMissions()));
            //récupère les activités
            pe.setActivites(buildActivitePoste(p.getActivites()));
//            pe.setQualifications(buildOrderBeanQualificationPoste(p.getQualifications()));
            pe.setModelContrat(buildBeanModelContrat(p.getModelContrat()));
            //récupère les primes au poste
            return pe;
        }
        return new PosteDeTravail();
    }

    public static Preavis buildBeanPreavis(YvsGrhPreavis y) {
        Preavis e = new Preavis();
        if (y != null) {
            e.setId(y.getId());
            e.setDuree(y.getDuree());
            e.setUnite(UtilProd.buildBeanUniteMesure(y.getUnite()));
        }
        return e;
    }

    public static YvsGrhPreavis buildPreavis(Preavis y, YvsUsersAgence ua) {
        YvsGrhPreavis e = new YvsGrhPreavis();
        if (y != null) {
            e.setId(y.getId());
            e.setDuree(y.getDuree());
            e.setUnite(new YvsBaseUniteMesure(y.getUnite().getId(), y.getUnite().getReference(), y.getUnite().getLibelle()));
            e.setAuthor(ua);
            e.setNew_(true);
        }
        return e;
    }

    public static ElementAdditionnelPoste buildBeanEltAddPoste(YvsGrhModelPrimePoste elt, boolean retenu) {
        ElementAdditionnelPoste e = new ElementAdditionnelPoste();
        e.setTypeElt((elt.getTypeElement() != null) ? buildTypeElt(elt.getTypeElement()) : new TypeElementAdd());
        if (retenu == e.getTypeElt().isRetenue()) {
            e.setId(elt.getId());
            e.setPermanent((elt.getPermanent() != null) ? elt.getPermanent() : true);
            e.setMontant(elt.getMontantElement());
            e.setPoste((elt.getPoste() != null) ? new PosteDeTravail(elt.getPoste().getId(), elt.getPoste().getIntitule()) : new PosteDeTravail());
            return e;
        }
        return e;
    }

    public static ElementAdditionnelPoste buildBeanModelPrimePoste(YvsGrhModelPrimePoste y) {
        ElementAdditionnelPoste e = new ElementAdditionnelPoste();
        if (y != null) {
            e.setId(y.getId());
            e.setPermanent((y.getPermanent() != null) ? y.getPermanent() : true);
            e.setMontant(y.getMontantElement());
            e.setDebut(y.getDateDebut());
            e.setFin(y.getDateFin());
            e.setTypeElt(buildTypeElt(y.getTypeElement()));
            e.setModel(buildBeanModelContrat(y.getModel()));
            e.setDateSave(y.getDateSave());
        }
        return e;
    }

    public static YvsGrhModelPrimePoste buildModelPrimePoste(ElementAdditionnelPoste y, YvsUsersAgence ua) {
        YvsGrhModelPrimePoste e = new YvsGrhModelPrimePoste();
        if (y != null) {
            e.setId(y.getId());
            e.setPermanent(y.isPermanent());
            e.setMontantElement(y.getMontant());
            e.setDateDebut(y.getDebut());
            e.setDateFin(y.getFin());
            e.setDateSave(y.getDateSave());
            e.setDateUpdate(new Date());
            e.setTypeElement(new YvsGrhTypeElementAdditionel(y.getTypeElt().getId(), y.getTypeElt().getLibelle()));
            e.setModel(new YvsGrhModelContrat(y.getModel().getId(), y.getModel().getIntitule()));
            e.setAuthor(ua);
            e.setNew_(true);
        }
        return e;
    }

    public static ModelContrat buildBeanModelContrat(YvsGrhModelContrat y) {
        ModelContrat e = new ModelContrat();
        if (y != null) {
            e.setId(y.getId());
            e.setCongeAcquis(y.getCongeAcquis());
            e.setIntitule(y.getIntitule());
            e.setMajorationConge(y.getMajorationConge());
            e.setPreavis(buildBeanPreavis(y.getPreavis()));
            e.setPrimes(y.getPrimes());
            e.setSalaireBaseHoraire(y.getSalaireBaseHoraire());
            e.setSalaireBaseMensuel(y.getSalaireBaseMensuel());
        }
        return e;
    }

    public static YvsGrhModelContrat buildModelContrat(ModelContrat y, YvsUsersAgence ua, YvsSocietes ste) {
        YvsGrhModelContrat e = new YvsGrhModelContrat();
        if (y != null) {
            e.setId(y.getId());
            e.setCongeAcquis(y.getCongeAcquis());
            e.setIntitule(y.getIntitule());
            e.setMajorationConge(y.getMajorationConge());
            if (y.getPreavis() != null ? y.getPreavis().getId() > 0 : false) {
                YvsBaseUniteMesure u = null;
                if (y.getPreavis().getUnite() != null) {
                    u = new YvsBaseUniteMesure(y.getPreavis().getUnite().getId(), y.getPreavis().getUnite().getReference(), y.getPreavis().getUnite().getLibelle());
                }
                e.setPreavis(new YvsGrhPreavis(y.getPreavis().getId(), y.getPreavis().getDuree(), u));
            }
            e.setPrimes(y.getPrimes());
            e.setSalaireBaseHoraire(y.getSalaireBaseHoraire());
            e.setSalaireBaseMensuel(y.getSalaireBaseMensuel());
            e.setSociete(ste);
            e.setDateSave(new Date());
            e.setAuthor(ua);
            e.setNew_(true);
        }
        return e;
    }

    public static List<ElementAdditionnelPoste> buildEltAddPoste(List<YvsGrhModelPrimePoste> l) {
        List<ElementAdditionnelPoste> r = new ArrayList<>();
//        for (YvsGrhModelPrimePoste m : l) {
//            r.add(buildBeanEltAddPoste(m, false));
//        }
        return r;
    }

    public static List<MissionPoste> buildMissionPoste(List<YvsGrhMissionPoste> l) {
        List<MissionPoste> r = new ArrayList<>();
        if (l != null) {
            for (YvsGrhMissionPoste m : l) {
                r.add(new MissionPoste(m.getId(), m.getLibelle()));
            }
        }
        return r;
    }

    public static List<MissionPoste> buildActivitePoste(List<YvsGrhActivitesPoste> l) {
        List<MissionPoste> r = new ArrayList<>();
        if (l != null) {
            for (YvsGrhActivitesPoste m : l) {
                r.add(new MissionPoste(m.getId(), m.getLibelle()));
            }
        }
        return r;
    }

    public static List<GrilleFraisMission> buildBeanListeGrilleFraisMission(List<YvsGrhGrilleMission> grille) {
        List<GrilleFraisMission> result = new ArrayList<>();
        if (grille != null) {
            for (YvsGrhGrilleMission gr : grille) {
                GrilleFraisMission g = buildGrilleMission(gr);
                result.add(g);
            }
        }
        return result;
    }

    public static GrilleFraisMission buildGrilleMission(YvsGrhGrilleMission gr) {
        GrilleFraisMission g = new GrilleFraisMission();
        if (gr != null) {
            g.setId(gr.getId());
            g.setActif(gr.getActif());
            g.setCategorie(gr.getCategorie());
            g.setTitre(gr.getTitre());
            g.setDetailsFrais(gr.getDetailsFraisMission());
            g.setCompteCharge(UtilCompta.buildSimpleBeanCompte(gr.getNumCompte()));
            g.setDateSave(gr.getDateSave());
        }
        return g;
    }

    public static GrilleFraisMission buildSimpleGrilleMission(YvsGrhGrilleMission gr) {
        GrilleFraisMission g = new GrilleFraisMission();
        if (gr != null) {
            g.setId(gr.getId());
            g.setActif(gr.getActif());
            g.setCategorie(gr.getCategorie());
            g.setTitre(gr.getTitre());
        }
        return g;
    }

    public static YvsGrhDetailGrilleFraiMission buildEntityDetailFrais(DetailFraisMission bean) {
        YvsGrhDetailGrilleFraiMission dg = new YvsGrhDetailGrilleFraiMission(bean.getId());
        dg.setDateSave(bean.getDateSave());
        dg.setMontantPrevu(bean.getMontant());
        dg.setProportionelDuree(bean.isProportionelDuree());
        dg.setTypeCout(buildTypeCout(bean.getTypeCout(), null, null));
        return dg;
    }

    public static TypeDemande buildBeanTypeDemande(YvsTypeDemande typeD) {
        if (typeD != null) {
            TypeDemande type = new TypeDemande();
            type.setId(typeD.getId());
            type.setLibelle(typeD.getLibelle());
            type.setSociete(buildBeanSociete(typeD.getSociete()));
            return type;
        }
        return new TypeDemande();
    }

    public static List<Qualification> buildBeanQualificatio(YvsGrhEmployes em) {
        List<Qualification> result = new ArrayList<>();
        if (em != null) {
            if (em.getYvsQualificationList() != null) {
                for (YvsGrhQualificationEmploye q : em.getYvsQualificationList()) {
                    Qualification c = new Qualification();
                    c.setId(q.getQualification().getId());
                    c.setIntitule(q.getQualification().getDesignation());
                    c.setDescription(q.getQualification().getDescription());
                    result.add(c);
                }
            }
        }
        return result;
    }

    public static List<Demande> buildBeanDemande(List<YvsDemandeEmps> d) {
        List<Demande> result = new ArrayList<>();
//        Demande demand = new Demande();
        if (d != null) {
            for (YvsDemandeEmps d1 : d) {
                Demande demand = new Demande();
                demand.setId(d1.getId());
                demand.setDateDebut(d1.getDateDebut());
                demand.setDateDemande(d1.getDateDemande());
                demand.setDateFin(d1.getDateFin());
                demand.setEmetteur(buildBeanPartialEmploye(d1.getEmetteur()));
                demand.setRecepteur(buildBeanPartialEmploye(d1.getDestinataire()));
                demand.setMotif(d1.getMessage());
                demand.setType(buildBeanTypeDemande(d1.getTypeDemande()));
                result.add(demand);
            }

        }
        return result;
    }

    public static List<TypeDemande> buildBeanListeTypeDemande(List<YvsTypeDemande> typeD) {
        List<TypeDemande> result = new ArrayList<>();
        if (typeD != null) {
            for (YvsTypeDemande typeD1 : typeD) {
                TypeDemande type = new TypeDemande();
                type.setId(typeD1.getId());
                type.setLibelle(typeD1.getLibelle());
                type.setSociete(buildBeanSociete(typeD1.getSociete()));
                result.add(type);
            }
        }
        return result;
    }

    public static YvsGrhMissions buildMission(Mission me, YvsUsersAgence ua) {
        YvsGrhMissions m = new YvsGrhMissions();
        m.setDateDebut(me.getDateDebut());
        m.setDateFin(me.getDateFin());
        m.setDateMission(me.getDateRetour());
        if ((me.getLieu() != null) ? me.getLieu().getId() != 0 : false) {
            m.setLieu(new YvsDictionnaire(me.getLieu().getId(), me.getLieu().getLibelle()));
        }
        m.setOrdre(me.getOrdre());
        m.setEmploye(UtilGrh.buildEmployeEntity(me.getEmploye()));
        m.setId(me.getId());
        m.setActif(me.isActif());
        m.setStatutMission(me.getStatutMission());
        m.setCloturer(me.isCloturer());
        m.setDateRetour(me.getDateRetour());
        m.setReferenceMission(me.getReference());
        m.setNumeroMission(me.getNumeroMission());
        m.setTransport(me.getTransport());
        m.setHeureArrive(me.getHeureArrive());
        m.setHeureDepart(me.getHeureDepart());
        m.setDateSave(new Date());
        m.setDureeMission(me.getDureeMission());
        if (me.getFraisMission() != null ? me.getFraisMission().getId() > 0 : false) {
            m.setFraisMission(new YvsGrhGrilleMission(me.getFraisMission().getId()));
        }
        m.setFraisMissions(me.getFraisMissions());
        m.setRoleMission(me.getRole());
        m.setAuthor(ua);
        return m;
    }

    public static Langues buildBeanLangue(YvsLangueEmps q) {
        Langues c = new Langues();
        c.setId(q.getLangue().getId());
        c.setLangue(q.getLangue().getNom());
        c.setLu(q.getLu());
        c.setEcrit(q.getEcrit());
        c.setParle(q.getParle());
        return c;
    }

    public static List<Diplomes> buildBeanDiplome(YvsGrhEmployes em) {
        List<Diplomes> result = new ArrayList<>();
        if (em != null) {
            if (em.getYvsDiplomeEmployeList() != null) {
                for (YvsDiplomeEmploye q : em.getYvsDiplomeEmployeList()) {
                    Diplomes c = new Diplomes();
                    c.setId(q.getDiplome().getId());
                    c.setDateObtention(q.getDateObtention());
                    c.setEcole(q.getEcole());
                    c.setMention(q.getMention());
                    c.setDesignation(q.getDiplome().getNom());
                    result.add(c);
                }
            }
        }
        return result;
    }

    public static Langues buildLangue(YvsLangues l) {
        Langues la = new Langues(l.getId(), l.getNom());
        la.setSave(true);
        return la;
    }

    public static List<YvsGrhEmployes> findEmploye(String code, YvsAgences currentAgence, int offset) {
        if (code != null) {
            String[] champ = new String[]{"codeUsers", "agence"};
            Object[] val = new Object[]{"%" + code + "%", currentAgence};
            List<YvsGrhEmployes> l = dao.loadNameQueries("YvsGrhEmployes.findByCodeUsers", champ, val, offset, 100);
            return l;
        }
        return null;
    }

    public static List<Employe> buildBeanListSimpleEmploye(List<YvsGrhEmployes> l) {
        List<Employe> result = new ArrayList<>();
        for (YvsGrhEmployes e : l) {
            result.add(buildBeanSimplePartialEmploye(e));
        }
        return result;
    }

    public static List<Employe> buildListEmployeBean(List<YvsGrhEmployes> l) {
        List<Employe> result = new ArrayList<>();
        for (YvsGrhEmployes e : l) {
            result.add(buildBeanPartialEmploye(e));
        }
        return result;
    }

    public static TrancheHoraire buildTrancheHoraire(YvsGrhTrancheHoraire tr) {
        return UtilCom.buildBeanTrancheHoraire(tr);
    }

    public static List<TrancheHoraire> buildTrancheHoraire(List<YvsGrhTrancheHoraire> l) {
        List<TrancheHoraire> result = new ArrayList<>();
        for (YvsGrhTrancheHoraire e : l) {
            result.add(buildTrancheHoraire(e));
        }
        return result;
    }

    public static YvsGrhTrancheHoraire buildTrancheHoraire(TrancheHoraire tr, YvsUsersAgence currentUser) {
        return UtilCom.buildTrancheHoraire(tr, currentUser);
    }

    public static Employe buildBeanSimplePartialEmploye(YvsGrhEmployes e) {
        Employe emp = new Employe();
        if (e != null) {
            emp.setId(e.getId());
            emp.setCivilite(e.getCivilite());
            emp.setUser((e.getCodeUsers() == null) ? new Users() : new Users(e.getCodeUsers().getId()));
            emp.setNom(e.getNom());
            emp.setPrenom(e.getPrenom());
            emp.setMatriculeCnps(e.getMatriculeCnps());
            emp.setMatricule(e.getMatricule());
            emp.setDateEmbauche(e.getDateEmbauche());
            emp.setPhotos(e.getPhotos());
            emp.setCni(e.getCni());
            emp.setActif((e.getActif() != null) ? e.getActif() : false);
            emp.setEquipe((e.getEquipe() != null) ? new EquipeEmploye(e.getEquipe().getId(), e.getEquipe().getTitreEquipe()) : new EquipeEmploye());
            emp.setPosteDeTravail(buildBeanPoste(e.getPosteActif()));
            if (e.getAgence() != null) {
                emp.setAgence(new Agence(e.getAgence().getId(), e.getAgence().getCodeagence(), e.getAgence().getDesignation()));
            }
            emp.setSolde((e.getSolde() != null) ? e.getSolde() : 0);
            if (e.getContrat() != null) {
                emp.setContrat(buildBeanContrat(e.getContrat()));
            }
            emp.setAgence(UtilAgence.buildBeanAgence(e.getAgence()));
        }
        return emp;
    }

    public static Employe buildBeanPartialEmploye(YvsGrhEmployes e) {
        Employe emp = new Employe();
        if (e != null) {
            emp = buildBeanSimplePartialEmploye(e);
            emp.setCodePostal(e.getCodePostal());
            emp.setNomDeJeuneFille(e.getNomJeuneFille());
            emp.setMotifDentree(e.getMotifEmbauche());
            emp.setDateArret(e.getDateArret());
            emp.setMotifArret(e.getMotifArret());
            emp.setDateNaissance(e.getDateNaissance());
            emp.setVilleNaissance((e.getLieuNaissance() == null) ? new Dictionnaire() : new Dictionnaire(e.getLieuNaissance().getId()));
            emp.setPaysDorigine((e.getPays() == null) ? new Dictionnaire() : new Dictionnaire(e.getPays().getId()));
            emp.setDateDelivCni(e.getDateDelivCni());
            emp.setDateExpCni(e.getDateExpCni());
            emp.setLieuDelivCni((e.getLieuDelivCni() == null) ? new Dictionnaire() : new Dictionnaire(e.getLieuDelivCni().getId()));
            emp.setSituationFamilial(e.getEtatCivil());
            emp.setHoraireDynamique((e.getHoraireDynamique() != null) ? e.getHoraireDynamique() : false);
            emp.setTelephone1(e.getTelephone1());
            emp.setTelephone2(e.getTelephone2());
            emp.setMail1(e.getMail1());
            emp.setMail2(e.getMail2());
            emp.setAdresse1(e.getAdresse1());
            emp.setAdresse2(e.getAdresse2());
            emp.setPersonneEnCharge(e.getPersonnes());
            emp.setNombreEnfant((e.getPersonnes() != null) ? e.getPersonnes().size() : 0);
            emp.setDateAvancement(e.getDateProchainAvancement());
            emp.setDateSave(e.getDateSave());
            emp.setDateUpdate(e.getDateUpdate());
            emp.setComptesBancaires(e.getYvsCompteBancaireList());
            emp.setContacts(e.getContactsEmployes());
            emp.setConvention(buildConvention(e.getConventions()));
            emp.setCompteCollectif(UtilCompta.buildSimpleBeanCompte(e.getCompteCollectif()));
            emp.setCompteTiers(UtilTiers.buildBeanTiers(e.getCompteTiers()));
            if ((e.getYvsProfilEmpsList() != null)) {
                emp.setProfil(buildBeanProfil(e.getYvsProfilEmpsList()));
            }
            emp.setProfil((e.getProfil() != null) ? new Profils(e.getProfil().getId(), e.getProfil().getStatutProfil()) : new Profils());
        }

        return emp;
    }

    public static Employe buildAllEMploye(YvsGrhEmployes e) {
        Employe em = buildBeanPartialEmploye(e);
        //charge les permis 
        em.setPermis(e.getPermis());
        em.setLangues(e.getYvsLangueEmpsList());
        em.setDiplomes(e.getYvsDiplomeEmployeList());
        em.setAffectations(buildBeanListeAffectation(e.getHistoriques()));
        em.setConventions(e.getConventions());
        for (YvsGrhConventionEmploye co : e.getConventions()) {
            boolean trouve = false;
            if (co.getConvention().getActif()) {
                for (YvsGrhCategoriePreavis pe : co.getConvention().getCategorie().getListDureePreavis()) {
                    if (pe.getAnciennete() != null ? pe.getAnciennete().getId() > 0 : false) {
                        if ((pe.getAnciennete().getAncienneteMin() < em.getDureeAnciennete() && em.getDureeAnciennete() < pe.getAnciennete().getAncienneteMax())
                                || (pe.getAnciennete().getAncienneteMin() == em.getDureeAnciennete() || em.getDureeAnciennete() == pe.getAnciennete().getAncienneteMax())) {
                            em.setDureePreavis(pe.getPreavis());
                            trouve = true;
                            break;
                        }
                    }
                }
            }
            if (trouve) {
                break;
            }
        }
        //charge les qualifications
        em.setQualifications(ordonneQualificationEmploye(e.getYvsQualificationList()));
        em.setDocuments(e.getDocuments());
        return em;
    }

    public static Employe buildAllOnlyEMploye(YvsGrhEmployes e) {
        Employe em = new Employe();
        if (e != null) {
            em.setId(e.getId());
            em.setCivilite(e.getCivilite());
            em.setUser((e.getCodeUsers() == null) ? new Users() : new Users(e.getCodeUsers().getId()));
            em.setNom(e.getNom());
            em.setPrenom(e.getPrenom());
            em.setMatriculeCnps(e.getMatriculeCnps());
            em.setMatricule(e.getMatricule());
            em.setDateEmbauche(e.getDateEmbauche());
            em.setPhotos(e.getPhotos());
            em.setPhotoExtension(e.getPhotoExtension());
            em.setCni(e.getCni());
            em.setActif((e.getActif() != null) ? e.getActif() : false);
            em.setEquipe((e.getEquipe() != null) ? new EquipeEmploye(e.getEquipe().getId(), e.getEquipe().getTitreEquipe()) : new EquipeEmploye());
            em.setCodePostal(e.getCodePostal());
            em.setNomDeJeuneFille(e.getNomJeuneFille());
            em.setMotifDentree(e.getMotifEmbauche());
            em.setDateArret(e.getDateArret());
            em.setMotifArret(e.getMotifArret());
            em.setDateNaissance(e.getDateNaissance());
            em.setVilleNaissance((e.getLieuNaissance() == null) ? new Dictionnaire() : new Dictionnaire(e.getLieuNaissance().getId()));
            em.setPaysDorigine((e.getPays() == null) ? new Dictionnaire() : new Dictionnaire(e.getPays().getId()));
            em.setDateDelivCni(e.getDateDelivCni());
            em.setDateExpCni(e.getDateExpCni());
            em.setLieuDelivCni((e.getLieuDelivCni() == null) ? new Dictionnaire() : new Dictionnaire(e.getLieuDelivCni().getId()));
            em.setSituationFamilial(e.getEtatCivil());
            em.setHoraireDynamique((e.getHoraireDynamique() != null) ? e.getHoraireDynamique() : false);
            em.setTelephone1(e.getTelephone1());
            em.setTelephone2(e.getTelephone2());
            em.setMail1(e.getMail1());
            em.setMail2(e.getMail2());
            em.setAdresse1(e.getAdresse1());
            em.setAdresse2(e.getAdresse2());
            em.setPosteDeTravail(new PosteDeTravail((e.getPosteActif() != null) ? e.getPosteActif().getId() : -1));
            em.setDateAvancement(e.getDateProchainAvancement());
            em.setDateSave(e.getDateSave());
            em.setDateUpdate(e.getDateUpdate());
            em.setProfil((e.getProfil() != null) ? new Profils(e.getProfil().getId(), e.getProfil().getStatutProfil()) : new Profils());
            if (e.getAgence() != null) {
                em.setAgence(new Agence(e.getAgence().getId(), e.getAgence().getCodeagence(), e.getAgence().getDesignation()));
            }
            em.setCompteCollectif(UtilCompta.buildSimpleBeanCompte(e.getCompteCollectif()));
            em.setCompteTiers(UtilTiers.buildSimpleBeanTiers(e.getCompteTiers()));

//            em.setComptesBancaires((e.getYvsCompteBancaireList() != null) ? buildBeanListCompteBancaire(e.getYvsCompteBancaireList()) : new ArrayList<CompteBancaire>());
//            em.setContacts(buildBeanListContactEmps(e.getContactsEmployes()));
//            em.setConvention(buildConvention(e.getConventions()));
//            em.setCompteCollectif(UtilCompta.buildSimpleBeanCompte(e.getCompteCollectif()));
//            em.setCompteTiers(UtilTiers.buildBeanTiers(e.getCompteTiers()));
//            em.setPersonneEnCharge((e.getPersonnes() != null) ? buildBeanListPersonneCharge(e.getPersonnes()) : new ArrayList<PersonneEnCharge>());
//            em.setNombreEnfant((e.getPersonnes() != null) ? e.getPersonnes().size() : 0);
//            em.setPermis(e.getPermis());
//            em.setLangues(e.getYvsLangueEmpsList());
//            em.setDiplomes(e.getYvsDiplomeEmployeList());
//            //charge les qualifications
//            em.setQualifications(ordonneQualificationEmploye(e.getYvsQualificationList()));
            em.setDocuments(e.getDocuments());
        }
        return em;
    }

    public static Convention buildConvention(List<YvsGrhConventionEmploye> lc) {
        Convention cc = new Convention();
        if (lc != null) {
            for (YvsGrhConventionEmploye ce : lc) {
                if (ce.getActif()) {
                    cc.setId(ce.getId());
                    cc.setCategorie(buildBeanCategoriePro(ce.getConvention().getCategorie()));
                    cc.setEchelon(buildBeanEchelon(ce.getConvention().getEchelon()));
                    break;
                }
            }
        }
        return cc;
    }

    public static Convention buildConvention(YvsGrhConventionCollective c) {
        Convention cc = new Convention();
        if (c != null) {
            cc.setId(c.getId());
            cc.setCategorie(buildBeanCategoriePro(c.getCategorie()));
            cc.setEchelon(buildBeanEchelon(c.getEchelon()));
        }
        return cc;
    }

    public static List<YvsGrhDomainesQualifications> ordonneQualificationEmploye(List<YvsGrhQualificationEmploye> l) {
        List<YvsGrhDomainesQualifications> result = new ArrayList<>();
        for (YvsGrhQualificationEmploye q : l) {
            q.getQualification().setId(q.getId());
            if (result.contains(q.getQualification().getDomaine())) {
                result.get(result.indexOf(q.getQualification().getDomaine())).getQualifications().add(q.getQualification());
            } else {
                YvsGrhDomainesQualifications d = q.getQualification().getDomaine();
                d.getQualifications().clear();
                d.getQualifications().add(q.getQualification());
                result.add(d);
            }
        }
        return result;
    }

    public static Convention buildBeanConventionCollective(YvsGrhConventionCollective c) {
        Convention con = new Convention();
        if (c != null) {
            con.setCategorie(buildBeanCategoriePro(c.getCategorie()));
            con.setEchelon(buildBeanEchelon(c.getEchelon()));
            con.setId(c.getId());
            con.setSecteur(new Secteurs(c.getYvsSecteurs().getId(), c.getYvsSecteurs().getNom(), c.getYvsSecteurs().getDescription()));
            con.setSalaireHoraireMin(c.getSalaireHoraireMin());
            con.setSalaireMin(c.getSalaireMin());
        }
        return con;
    }

    public static Convention buildBeanConvention(YvsGrhConventionEmploye c) {
        Convention con = new Convention();
        if (c != null) {
            con = buildBeanConventionCollective(c.getConvention());
            con.setId(c.getId());
            con.setActif(c.getActif());
        }
        return con;
    }

    public static YvsGrhPosteDeTravail posteActif(List<YvsGrhPosteEmployes> l) {
        for (YvsGrhPosteEmployes pe : l) {
            if (pe.getActif()) {
                return pe.getPoste();
            }
        }
        return null;
    }

    public static YvsGrhEmployes buildEmployeEntity(Employe e) {
        YvsGrhEmployes emp = new YvsGrhEmployes();
        if (e != null) {
            emp.setId(e.getId());
            if ((e.getUser() != null) ? e.getUser().getId() != 0 : false) {
                emp.setCodeUsers(new YvsUsers(e.getUser().getId()));
            }
            emp.setCivilite(e.getCivilite());
            emp.setNom(e.getNom());
            emp.setPrenom(e.getPrenom());
            emp.setMatricule(e.getMatricule());
            emp.setMatriculeCnps(e.getMatriculeCnps());
            emp.setCodePostal(e.getCodePostal());
            emp.setNomJeuneFille(e.getNomDeJeuneFille());
            emp.setDateEmbauche(e.getDateEmbauche());
            emp.setMotifEmbauche(e.getMotifDentree());
            emp.setDateArret(e.getDateArret());
            emp.setMotifArret(e.getMotifArret());
            emp.setEquipe((e.getEquipe().getId() > 0) ? new YvsGrhEquipeEmploye(e.getEquipe().getId()) : null);
            emp.setDateNaissance(e.getDateNaissance());
            if ((e.getVilleNaissance() != null) ? e.getVilleNaissance().getId() != 0 : false) {
                emp.setLieuNaissance(new YvsDictionnaire(e.getVilleNaissance().getId()));
            }
            if ((e.getPaysDorigine() != null) ? e.getPaysDorigine().getId() != 0 : false) {
                emp.setPays(new YvsDictionnaire(e.getPaysDorigine().getId()));
            }
            emp.setPhotos(e.getPhotos());
            emp.setPhotoExtension(e.getPhotoExtension());
            emp.setCni(e.getCni());
            emp.setDateDelivCni(e.getDateDelivCni());
            emp.setDateExpCni(e.getDateExpCni());
            emp.setDateSave(e.getDateSave());
            emp.setDateUpdate(new Date());
            if ((e.getLieuDelivCni() != null) ? e.getLieuDelivCni().getId() != 0 : false) {
                emp.setLieuDelivCni(new YvsDictionnaire(e.getLieuDelivCni().getId()));
            }
            emp.setEtatCivil(e.getSituationFamilial());
            emp.setActif(e.isActif());
            emp.setHoraireDynamique(e.isHoraireDynamique());
            emp.setTelephone1(e.getTelephone1());
            emp.setTelephone2(e.getTelephone2());
            emp.setMail1(e.getMail1());
            emp.setMail2(e.getMail2());
            emp.setAdresse1(e.getAdresse1());
            emp.setAdresse2(e.getAdresse2());
            emp.setProfil((e.getProfil().getId() > 0) ? new YvsGrhProfil(e.getProfil().getId()) : null);
            if (e.getPosteDeTravail().getId() > 0) {
                emp.setPosteActif(new YvsGrhPosteDeTravail(e.getPosteDeTravail().getId()));
            }
            if (e.getAgence().getId() > 0) {
                emp.setAgence(new YvsAgences(e.getAgence().getId(), e.getAgence().getDesignation()));
            }
            emp.setCompteCollectif((e.getCompteCollectif().getId() > 0) ? new YvsBasePlanComptable(e.getCompteCollectif().getId()) : null);
            emp.setCompteTiers((e.getCompteTiers().getId() > 0) ? new YvsBaseTiers(e.getCompteTiers().getId()) : null);
        }
        return emp;
    }

    public static CategorieRegleSalaire buildCategorieRegle(YvsGrhCategorieElement cat) {
        CategorieRegleSalaire re = new CategorieRegleSalaire();
        if (cat != null) {
            re.setCode(cat.getCodeCategorie());
            re.setDescription(cat.getDescription());
            re.setDesignation(cat.getDesignation());
            re.setActif(cat.getActif());
            re.setId(cat.getId());
        }
        return re;
    }

    public static ElementSalaire buildElementSalaire(YvsGrhElementSalaire elt) {
        ElementSalaire e = new ElementSalaire();
        if (elt != null) {
            e.setId(elt.getId());
            e.setBasePourcentage((elt.getBasePourcentage() != null) ? new OptionEltSalaire(elt.getBasePourcentage().getId(), elt.getBasePourcentage().getCode()) : new OptionEltSalaire());
            e.setExpQuantite((elt.getQuantite() != null) ? new OptionEltSalaire(elt.getQuantite().getId(), elt.getQuantite().getCode()) : new OptionEltSalaire());
            e.setCategorie((elt.getCategorie() != null) ? new CategorieRegleSalaire(elt.getCategorie().getId(), elt.getCategorie().getCodeCategorie(), elt.getCategorie().getDesignation())
                    : new CategorieRegleSalaire());
            e.setCode(elt.getCode());
            e.setTypeMontant(elt.getTypeMontant());
            e.setExpressionRegle(elt.getExpressionRegle());
            e.setMontant(elt.getMontant());
            e.setNom(elt.getNom());
            e.setActif(elt.getActif());
            e.setVisibleSurBulletin(elt.getVisibleBulletin());
            e.setTauxPatronale((elt.getPoucentagePatronale() != null) ? elt.getPoucentagePatronale() : 0);
            e.setTauxSalarial((elt.getPoucentageSalarial() != null) ? elt.getPoucentageSalarial() : 0);
            e.setRetenue((elt.getRetenue() != null) ? elt.getRetenue() : false);
            e.setSup((elt.getSupp() != null) ? elt.getSupp() : false);
            e.setNumSequence((elt.getNumSequence() != null) ? elt.getNumSequence() : 0);
            e.setDescription(elt.getDescriptionElement());
            e.setRubrique((elt.getRubrique() != null) ? new RubriqueBulletin(elt.getRubrique().getId(), elt.getRubrique().getCode(), elt.getRubrique().getDesignation()) : new RubriqueBulletin());
            e.setRegleConge((elt.getAllocationConge() != null) ? elt.getAllocationConge() : false);
            e.setExcludeInConge((elt.getExcludeInConge() != null) ? elt.getExcludeInConge() : false);
            e.setRegleArondi((elt.getRegleArrondi() != null) ? elt.getRegleArrondi() : false);
            e.setCompteCharge(UtilCompta.buildSimpleBeanCompte(elt.getCompteCharge()));
            e.setCompteCotisation(UtilCompta.buildSimpleBeanCompte(elt.getCompteCotisation()));
            e.setSaisiCompteTiers(elt.getSaisiCompteTiers());
            e.setDateSave(elt.getDateSave());
        }
        return e;
    }

    public static YvsGrhElementSalaire buildElementSalaire(ElementSalaire elt) {
        YvsGrhElementSalaire e = new YvsGrhElementSalaire();
        if (elt != null) {
            e.setId(elt.getId());
            e.setBasePourcentage((elt.getBasePourcentage().getId() != 0) ? new YvsGrhElementSalaire(elt.getBasePourcentage().getId()) : null);
            e.setQuantite((elt.getExpQuantite().getId() != 0) ? new YvsGrhElementSalaire(elt.getExpQuantite().getId()) : null);
            e.setCategorie((elt.getCategorie().getId() != 0) ? new YvsGrhCategorieElement(elt.getCategorie().getId()) : null);
            e.setCode(elt.getCode());
            e.setExpressionRegle(elt.getExpressionRegle());
            e.setMontant(elt.getMontant());
            e.setTypeMontant(elt.getTypeMontant());
            e.setNom(elt.getNom());
            e.setActif(elt.isActif());
            e.setVisibleBulletin(elt.isVisibleSurBulletin());
            e.setPoucentagePatronale(elt.getTauxPatronale());
            e.setPoucentageSalarial(elt.getTauxSalarial());
            e.setRetenue(elt.isRetenue());
            e.setDescriptionElement(elt.getDescription());
            e.setSupp(elt.isSup());
            e.setAllocationConge(elt.isRegleConge());
            e.setNumSequence(elt.getNumSequence());
            e.setRubrique((elt.getRubrique().getId() > 0) ? new YvsGrhRubriqueBulletin(elt.getRubrique().getId()) : null);
            e.setExcludeInConge(elt.isExcludeInConge());
            e.setCompteCharge((elt.getCompteCharge().getId() > 0) ? UtilCompta.buildEntityCompte(elt.getCompteCharge()) : null);
            e.setCompteCotisation((elt.getCompteCotisation().getId() > 0) ? UtilCompta.buildEntityCompte(elt.getCompteCotisation()) : null);
            e.setSaisiCompteTiers(elt.isSaisiCompteTiers());
            e.setDateSave(elt.getDateSave());
            e.setDateUpdate(new Date());
        }
        return e;
    }

    public static List<ElementSalaire> buildElementSalaire(List<YvsGrhElementSalaire> l) {
        List<ElementSalaire> result = new ArrayList<>();
        for (YvsGrhElementSalaire elt : l) {
            result.add(buildElementSalaire(elt));
        }
        return result;
    }

//    public static List<Formation> buildBeanListeFormationEmps(List<YvsFormationEmps> formation) {
//        List<Formation> result = new ArrayList();
//        if (formation != null) {
//            for (YvsGrhFormationEmps formation1 : formation) {
//                Formation f = new Formation();
//                f.setDateDebut(formation1.getDateDebut());
//                f.setDateFin(formation1.getDateFin());
//                f.setDateFormation(formation1.getDateFormation());
//                f.setEmploye(buildBeanPartialEmploye(formation1.getEmploye()));
//                f.setId(formation1.getId());
////                f.setLieu(formation1.getLieu());
//                f.setMotif(formation1.getMotif());
//                f.setDureeFormation((int) (formation1.getDateFin().getTime() - formation1.getDateDebut().getTime()) / 36000);
//                result.add(f);
//            }
//        }
//        return result;
//    }
    public static List<Formation> buildBeanListFormation(List<YvsGrhFormation> l) {
        List<Formation> re = new ArrayList<>();
        if (l != null) {
            if (!l.isEmpty()) {
                for (YvsGrhFormation e : l) {
                    Formation f = buildBeanFormation(e);
                    if (f != null) {
                        re.add(f);
                    }
                }
            }
        }
        return re;
    }

    public static FormationEmps buildBeanFormationEmp(YvsGrhFormationEmps e) {
        FormationEmps fm = new FormationEmps();
        fm.setDateDebut(e.getDateDebut());
        fm.setDateFin(e.getDateFin());
        fm.setDateFormation(e.getDateFormation());
        fm.setEmploye(buildBeanPartialEmploye(e.getEmploye()));
        fm.setCoutFormation(buildListBeanCoutFormation(e.getCouts()));
        fm.setCoutTotal(0);
        if (fm.getCoutFormation() != null) {
            if (!fm.getCoutFormation().isEmpty()) {
                for (CoutFormation c : fm.getCoutFormation()) {
                    fm.setCoutTotal(fm.getCoutTotal() + c.getMontant());
                }
            }
        }
        fm.setId(e.getId());
        fm.setVille((e.getLieu() != null) ? UtilSte.buildBeanDictionnaire(e.getLieu()) : UtilSte.buildBeanDictionnaire(e.getFormation().getLieuDefaut()));
        fm.setValider((e.getValider() != null) ? e.getValider() : false);
        fm.setDiplomee(e.getObtentionDiplome());
        return fm;
    }

    public static List<FormationEmps> buildBeanListFormationEmp(List<YvsGrhFormationEmps> l) {
        List<FormationEmps> re = new ArrayList<>();
        for (YvsGrhFormationEmps e : l) {
            re.add(buildBeanFormationEmp(e));
        }
        return re;
    }

    public static Affectation buildBeanAffectation(YvsGrhPosteEmployes pe) {
        Affectation aff = new Affectation();
        if (pe != null) {
            aff.setId(pe.getId());
            aff.setEmploye(buildBeanSimplePartialEmploye(pe.getEmploye()));
            aff.setPoste((pe.getPoste() != null) ? builSimpledBeanPoste(pe.getPoste()) : new PosteDeTravail());
            aff.setPostePrecedent((pe.getPoste() != null) ? builSimpledBeanPoste(pe.getPostePrecedent()) : new PosteDeTravail());
            aff.setDate(pe.getDateAcquisition());
            aff.setDateConfirmation(pe.getDateConfirmation());
            aff.setValider(pe.getValider());
            aff.setDateDebut(pe.getDateDebut());
            aff.setDateSave(pe.getDateSave());
            aff.setDateFinInterim(pe.getDateFinInterim());
            aff.setPosteActif(pe.getActif());
            aff.setIndemnisable(pe.isIndemnisable());
            aff.setStatut(pe.getStatut());
            aff.setHistoriques(pe.getEmploye().getHistoriques());
            aff.setAgence(UtilAgence.buildBeanAgence(pe.getAgence()));
        }
        return aff;
    }

    public static List<Affectation> buildBeanListeAffectation(List<YvsGrhPosteEmployes> poste) {
        List<Affectation> result = new ArrayList<>();
        if (poste != null) {
            for (YvsGrhPosteEmployes poste1 : poste) {
                result.add(buildBeanAffectation(poste1));
            }
        }
        return result;
    }

    public static List<Affectation> buildBeanListePoste(List<YvsGrhPosteEmployes> poste) {
        List<Affectation> result = new ArrayList<>();
        if (poste != null) {
            for (YvsGrhPosteEmployes poste1 : poste) {
                Affectation aff = new Affectation();
                aff.setId(poste1.getId());
                aff.setEmploye((poste1.getEmploye() != null) ? buildBeanPartialEmploye(poste1.getEmploye()) : new Employe());
                aff.setPoste((poste1.getPoste() != null) ? buildBeanPoste(poste1.getPoste()) : new PosteDeTravail());
                aff.setDate(poste1.getDateAcquisition());
                aff.setValider(poste1.getValider());
                result.add(aff);
            }
        }
        return result;
    }

    public static StructureElementSalaire buildBeanStructure(YvsGrhStructureSalaire st) {
        StructureElementSalaire s = new StructureElementSalaire();
        if (st != null) {
            s.setId(st.getId());
            s.setCode(st.getCode());
            s.setDescription(st.getDescription());
            s.setNom(st.getNom());
            s.setListElement(st.getElementSalaire());
            s.setDateSave(st.getDateSave());
        }
        return s;
    }

    public static YvsGrhStructureSalaire buildBeanStructure(StructureElementSalaire st) {
        YvsGrhStructureSalaire s = new YvsGrhStructureSalaire(st.getId());
        s.setCode(st.getCode());
        s.setNom(st.getNom());
        s.setDateSave(st.getDateSave());
        s.setDateUpdate(new Date());
        s.setDescription(st.getDescription());
        s.setActif(true);
        s.setSupp(false);
        return s;
    }

    public static List<StructureElementSalaire> buildBeanStructure(List<YvsGrhStructureSalaire> l) {
        List<StructureElementSalaire> result = new ArrayList<>();
        for (YvsGrhStructureSalaire st : l) {
            result.add(buildBeanStructure(st));
        }
        return result;
    }

    public static YvsGrhDepartement buildBeanDepartement(Departements dep) {
        YvsGrhDepartement d = null;
        if (dep != null) {
            d = new YvsGrhDepartement(dep.getId());
            d.setDescription(dep.getDescription());
            d.setIntitule(dep.getIntitule());
            d.setCodeDepartement(dep.getCodeDepartement());
            d.setAbreviation(dep.getAbreviation());
            d.setActif(true);
            if ((dep.getResponsable() != null) ? dep.getResponsable().getId() != 0 : false) {
                d.setChefDepartement(new YvsGrhEmployes(dep.getResponsable().getId()));
            }
            if ((dep.getService() != null) ? dep.getService().getId() != 0 : false) {
                d.setService(new YvsBaseDepartement(dep.getService().getId()));
            }
            d.setDepartementParent((dep.getIdParent() != 0 && dep.getIdParent() != -1) ? new YvsGrhDepartement(dep.getIdParent()) : null);
            d.setVisibleOnLivrePaie(dep.isVisibleOnLp());
            d.setDateSave(dep.getDateSave());
            d.setDateUpdate(new Date());
        }
        return d;
    }

    public static Departements buildBeanDepartement(YvsGrhDepartement dep) {
        Departements d = new Departements();
        if (dep != null) {
            d.setId(dep.getId());
            d.setDescription(dep.getDescription());
            d.setIntitule(dep.getIntitule());
            d.setAbreviation(dep.getAbreviation());
            d.setCodeDepartement(dep.getCodeDepartement());
            d.setIdParent((dep.getDepartementParent() != null) ? dep.getDepartementParent().getId() : -1);
//            d.setListPosteTravail(buildBeanListPoste(dep.getYvsPosteDeTravailList()));
            d.setResponsable((dep.getChefDepartement() != null) ? buildBeanSimpleEmployePartial(dep.getChefDepartement()) : new EmployePartial());
            d.setService(UtilBase.buildSimpleBeanDepartement(dep.getService()));
            d.setNomResponsable((dep.getChefDepartement() != null) ? dep.getChefDepartement().getPrenom() + " " + dep.getChefDepartement().getNom() : null);
            d.setVisibleOnLp(dep.getVisibleOnLivrePaie());
            d.setActif(dep.getActif());
            d.setDateSave(dep.getDateSave());
//            for (PosteDeTravail po : d.getListPosteTravail()) {
//                d.setEtatEndettement(d.getEtatEndettement() + po.getEtatEndettement());
//                d.setTotalEmploye(d.getTotalEmploye() + po.getTotalEmploye());
//            }
        }
        return d;
    }

    public static Departements buildSimpleBeanDepartement(YvsGrhDepartement dep) {
        Departements d = new Departements();
        if (dep != null) {
            d.setId(dep.getId());
            d.setDescription(dep.getDescription());
            d.setIntitule(dep.getIntitule());
            d.setCodeDepartement(dep.getCodeDepartement());
            d.setVisibleOnLp(dep.getVisibleOnLivrePaie());
            d.setActif(dep.getActif());
            d.setService(UtilBase.buildSimpleBeanDepartement(dep.getService()));
        }
        return d;
    }

    public static List<Departements> buildBeanDepartement(List<YvsGrhDepartement> ldep) {
        List<Departements> result = new ArrayList<>();
        for (YvsGrhDepartement dep : ldep) {
            chemin = new CheminDepartement();
            Departements d = buildBeanDepartement(dep);
            setcheminParent1(d, ldep);
            d.setCheminParent(chemin.path);
            result.add(d);
        }

        return result;
    }

    public static List<Departements> buildBeanListeDepartement(List<YvsGrhDepartement> l) {
        List<Departements> list = new ArrayList<>();
        if (l != null) {
            for (YvsGrhDepartement dep : l) {
                chemin = new CheminDepartement();
                Departements d = buildBeanDepartement(dep);
                setcheminParent1(d, l);
                d.setCheminParent(chemin.path);
                list.add(d);
            }
        }
        return list;
    }

    static CheminDepartement chemin;

    static class CheminDepartement {

        public String path = null;
        public List<Integer> ids = new ArrayList<>();
    }

    public static String setcheminParent1(Departements dep, List<YvsGrhDepartement> l) {
        try {
            if (chemin.ids == null) {
                chemin.ids = new ArrayList<>();
            }
            if (dep.getIdParent() != -1 && dep.getIdParent() != dep.getId()) {
                YvsGrhDepartement d = null;
                int i = l.indexOf(new YvsGrhDepartement(dep.getIdParent()));
                if (i >= 0) {
                    d = l.get(i);
                }
                if (d != null && !chemin.ids.contains(d.getId())) {
                    chemin.ids.add(d.getId());
                    if (chemin.path != null) {
                        chemin.path = d.getIntitule() + " / " + chemin.path;
                    } else {
                        chemin.path = d.getIntitule();
                    }
                    Departements dd = buildBeanDepartement(d);
                    setcheminParent1(dd, l);
                }
            } else {
                return chemin.path;
            }
            return chemin.path;
        } catch (Exception ex) {
            Logger.getLogger(UtilGrh.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Departements setcheminOneParent(Departements d, List<Departements> l) {
        if (d != null) {
            for (Departements dep : l) {
                if (d.getIdParent() == dep.getId()) {
                    d.setCheminParent((d.getCheminParent() != null) ? d.getCheminParent() + " / " + d.getIntitule() : dep.getIntitule());
                    setcheminOneParent(dep, l.subList(l.indexOf(dep), l.size()));
                }
            }
        }
        return d;
    }

    public static PosteDeTravail buildBeanPoste(YvsGrhPosteDeTravail poste, List<Departements> l) {
        PosteDeTravail p = new PosteDeTravail(poste.getId().intValue());
        p.setDepartement(setcheminOneParent(buildBeanDepartement(poste.getDepartement()), l));
        p.setDescription(poste.getDescriptionPoste());
        p.setIntitule(poste.getIntitule());
        p.setNombrePlace(poste.getNombrePlace());
        p.setIntitule(poste.getIntitule());
        p.setActif(poste.getActif());
        p.setCongeAcquis(poste.getCongeAcquis());
        p.setDureePreavie(poste.getDureePreavis());
        p.setModePaiement(buildBeanModePaiement(poste.getModePaiement()));
        p.setSalaireHoraire(poste.getSalaireHoraire());
        p.setSalaireMensuel(poste.getSalaireMensuel());
        p.setStructSalaire(buildBeanStructure(poste.getStructureSalaire()));
        p.setUnitePreavis(poste.getUniteDureePreavis());
        p.setNiveauRequis(buildDiplome(poste.getNiveau()));
        p.setFraisMission(buildGrilleMission(poste.getFraisMission()));
        p.setGrade(poste.getGrade());
        return p;
    }

    public static YvsGrhDepartement setcheminOneParent_(Departements d, List<Departements> l) {
        YvsGrhDepartement re = null;
        if (d != null) {
            re = buildBeanDepartement(d);
            for (Departements dep : l) {
                if (d.getIdParent() == dep.getId()) {
                    re.setCheminParent((d.getCheminParent() != null) ? d.getCheminParent() + " / " + d.getIntitule() : dep.getIntitule());
                    setcheminOneParent(dep, l.subList(l.indexOf(dep), l.size()));
                }
            }
        }
        return re;
    }

    public static YvsGrhPosteDeTravail buildEntityPoste(YvsGrhPosteDeTravail poste, List<Departements> l) {
        poste.setDepartement(setcheminOneParent_(buildBeanDepartement(poste.getDepartement()), l));
        return poste;
    }

    public static YvsGrhPosteDeTravail buildEntityPoste_(YvsGrhPosteDeTravail poste, List<Departements> l) {
        poste.setDepartement(setcheminOneParent_(buildSimpleBeanDepartement(poste.getDepartement()), l));
        return poste;
    }

    public static List<YvsGrhPosteDeTravail> buildBeanPoste(List<YvsGrhPosteDeTravail> lp, List<Departements> l) {
        List<YvsGrhPosteDeTravail> result = new ArrayList<>();
        for (YvsGrhPosteDeTravail po : lp) {
            result.add(buildEntityPoste(po, l));
        }
        return result;
    }

    public static List<YvsGrhPosteDeTravail> buildBeanPoste_(List<YvsGrhPosteDeTravail> lp, List<Departements> l) {
        List<YvsGrhPosteDeTravail> result = new ArrayList<>();
        for (YvsGrhPosteDeTravail po : lp) {
            result.add(buildEntityPoste_(po, l));
        }
        return result;
    }

    public static YvsGrhPosteDeTravail buildBeanPoste(PosteDeTravail poste) {
        YvsGrhPosteDeTravail p = new YvsGrhPosteDeTravail(poste.getId());
        p.setNombrePlace(poste.getNombrePlace());
        p.setDepartement((poste.getDepartement().getId() != 0) ? new YvsGrhDepartement(poste.getDepartement().getId(), poste.getDepartement().getIntitule()) : null);
        p.setDescriptionPoste(poste.getDescription());
        p.setIntitule(poste.getIntitule());
        p.setActif(poste.isActif());
        p.setCongeAcquis(poste.getCongeAcquis());
        p.setDureePreavis(poste.getDureePreavie());
        p.setFraisMission((poste.getFraisMission().getId() > 0) ? new YvsGrhGrilleMission(poste.getFraisMission().getId()) : null);
        p.setModePaiement((poste.getModePaiement().getId() > 0) ? new YvsModePaiement(poste.getModePaiement().getId()) : null);
        p.setSalaireHoraire(poste.getSalaireHoraire());
        p.setSalaireMensuel(poste.getSalaireMensuel());
        p.setStructureSalaire((poste.getStructSalaire().getId() > 0) ? new YvsGrhStructureSalaire(poste.getStructSalaire().getId()) : null);
        p.setUniteDureePreavis(poste.getUnitePreavis());
        p.setGrade(poste.getGrade());
        p.setId(poste.getId());
        return p;
    }

//    public static YvsGrhContratEmps buildBeanContratEmploye(ContratEmploye ce) {
//        YvsGrhContratEmps contrat = new YvsGrhContratEmps();
//        if (ce != null) {
//            contrat.setDeteDebut(ce.getDateDebut());
//            contrat.setDateFin(ce.getDateFin());
//            contrat.setFinEssaie(ce.getFinEssai());
//            contrat.setHoraireHebdo(ce.getHoraireHebdo());
//            contrat.setHoraireMensuel(ce.getHoraireMensuel());
//            contrat.setEmploye(buildEmployeEntity(ce.getEmploye()));
//            contrat.setFichier(ce.getFichier());
//            contrat.setId(ce.getId());
//            contrat.setSalaireHoraire(ce.getSalaireHoraire());
//            contrat.setSalaireMensuel(ce.getSalaireMensuel());
//            if (ce.getTypeContrat().getId() > 0) {
//                contrat.setTypeContrat(new YvsTypeContrat(ce.getTypeContrat().getId()));
//            }
//            contrat.setActif(ce.isActif());
//            contrat.setReferenceContrat(ce.getReference());
//            contrat.setPrimes(ce.getPrimes());
//            contrat.setUnitePreavis(ce.getUnitePreavis());
//            contrat.setTypeTranche(ce.getTypeTranche());
//            contrat.setDateFirstConge(ce.getDateFirstConge());
//            contrat.setSourceFirstConge(ce.getSourceFirstConge());
//        }
//        return contrat;
//    }
    public static YvsGrhContratEmps buildBeanContratEmploye(ContratEmploye ce) {
        if (ce != null) {
            YvsGrhContratEmps contrat = new YvsGrhContratEmps();
            contrat.setActif(ce.isActif());
            contrat.setSupp(false);
            contrat.setContratPrincipal(true);
            contrat.setDateFin(ce.getDateFin());
            contrat.setDateDebut(ce.getDateDebut());
            contrat.setFichier(ce.getFichier());
            contrat.setFinEssaie(ce.getFinEssai());
            contrat.setHoraireHebdo(ce.getHoraireHebdo());
            contrat.setHoraireMensuel(ce.getHoraireMensuel());
            contrat.setId(ce.getId());
            contrat.setStatut(ce.getStatut());
            contrat.setModePaiement(new YvsBaseModeReglement((long) ce.getModePaiement().getId()));
            contrat.setSalaireHoraire(ce.getSalaireHoraire());
            contrat.setSalaireMensuel(ce.getSalaireMensuel());
            contrat.setCongeAcquis(ce.getCongeAcquis());
            contrat.setReferenceContrat(ce.getReference());
            if (ce.getCalendrier().getId() > 0) {
                contrat.setCalendrier(new YvsCalendrier(ce.getCalendrier().getId()));
            }
            if (ce.getCaisse() != null ? ce.getCaisse().getId() > 0 : false) {
                contrat.setCaisse(new YvsBaseCaisse(ce.getCaisse().getId(), ce.getCaisse().getIntitule()));
            }
            contrat.setDureePreavie(ce.getDureePreavie());
            contrat.setUnitePreavis(ce.getUnitePreavis());
            contrat.setTypeTranche(ce.getTypeTranche());
            contrat.setAccesRestreint(ce.isAccesRestreint());
            contrat.setDateFirstConge(ce.getDateFirstConge());
            contrat.setSourceFirstConge(ce.getSourceFirstConge());
            contrat.setDateSave(ce.getDateSave());
            contrat.setDateUpdate(new Date());
            if (ce.getStructSalaire().getId() > 0) {
                contrat.setStructureSalaire(buildBeanStructure(ce.getStructSalaire()));
            }
            if (ce.getTypeContrat().getId() > 0) {
                contrat.setTypeContrat(new YvsTypeContrat(ce.getTypeContrat().getId(), ce.getTypeContrat().getLibelle()));
            }
            contrat.setEmploye(UtilGrh.buildEmployeEntity(ce.getEmploye()));
            return contrat;
        }
        return null;
    }

    public static ContratEmploye buildBeanContrat(YvsGrhContratEmps ce) {
        ContratEmploye contrat = new ContratEmploye();
        if (ce != null) {
            contrat.setId(ce.getId());
            contrat.setDateDebut(ce.getDateDebut());
            contrat.setFichier(ce.getFichier());
            contrat.setDateFin(ce.getDateFin());
            contrat.setFinEssai(ce.getFinEssaie());
            contrat.setHoraireHebdo(ce.getHoraireHebdo());
            contrat.setStrHoraireHebdo(Utilitaire.doubleToHour(ce.getHoraireHebdo()));
            contrat.setHoraireMensuel(ce.getHoraireMensuel());
            contrat.setSalaireHoraire(ce.getSalaireHoraire());
            contrat.setSalaireMensuel(ce.getSalaireMensuel());
            contrat.setReference(ce.getReferenceContrat());
            contrat.setActif(ce.getActif());
            contrat.setReference(ce.getReferenceContrat());
            contrat.setCalendrier((ce.getCalendrier() != null) ? buildBeanCalendrier(ce.getCalendrier()) : new Calendrier());
            contrat.setBulletins(ce.getBulletins());
            contrat.setCongeAcquis((ce.getCongeAcquis() != null) ? ce.getCongeAcquis() : 0);
            contrat.setDureePreavie((ce.getDureePreavie() != null) ? ce.getDureePreavie() : 0);
            contrat.setUnitePreavis(ce.getUnitePreavis());
            contrat.setContratS(buildBeanFinDeContrat(ce.getContratSuspendu(), false));
            contrat.setTypeTranche(ce.getTypeTranche());
            contrat.setDateFirstConge(ce.getDateFirstConge());
            contrat.setDateSave(ce.getDateSave());
            contrat.setDateUpdate(ce.getDateUpdate());
            contrat.setSourceFirstConge(ce.getSourceFirstConge());
            contrat.setStatut(ce.getStatut());
            contrat.setModePaiement((ce.getModePaiement() != null) ? UtilCom.buildBeanModeReglement(ce.getModePaiement()) : new ModeDeReglement());
            if (ce.getStructureSalaire() != null) {
                contrat.setStructSalaire(new StructureElementSalaire(ce.getStructureSalaire().getId(), ce.getStructureSalaire().getNom(), null));
            }
            contrat.setCaisse(ce.getCaisse() != null ? new Caisses(ce.getCaisse().getId(), ce.getCaisse().getIntitule()) : new Caisses());
            if (ce.getTypeContrat() != null) {
                contrat.setTypeContrat(new TypeContrat(ce.getTypeContrat().getId(), ce.getTypeContrat().getLibelle()));
            }
            for (YvsGrhBulletins b : ce.getBulletins()) {
                contrat.setSalaireCumule(contrat.getSalaireCumule() + b.getNetApayer());
            }
            contrat.setPrimes(ce.getPrimes());
        }
        return contrat;
    }

    public static ContratEmploye buildBeanContratEmploye(YvsGrhContratEmps ce, boolean retenu) {
        ContratEmploye contrat = new ContratEmploye();
        if (ce != null) {
            contrat = buildBeanContrat(ce);
            contrat.setEmploye((ce.getEmploye() != null) ? buildBeanPartialEmploye(ce.getEmploye()) : new Employe());
        }
        return contrat;
    }

    public static ContratEmploye buildBeanContratEmploye(YvsGrhContratEmps ce) {
        ContratEmploye contrat = new ContratEmploye();
        if (ce != null) {
            contrat.setDateDebut(ce.getDateDebut());
            contrat.setDateFin(ce.getDateFin());
            contrat.setFinEssai(ce.getFinEssaie());
            contrat.setStatut(ce.getStatut());
            contrat.setHoraireHebdo((ce.getHoraireHebdo() != null) ? ce.getHoraireHebdo() : 0);
            contrat.setStrHoraireHebdo(Utilitaire.doubleToHour((ce.getHoraireHebdo() != null) ? ce.getHoraireHebdo() : 0));
            contrat.setHoraireMensuel((ce.getHoraireMensuel() != null) ? ce.getHoraireMensuel() : 0);
            contrat.setEmploye((ce.getEmploye() != null) ? buildBeanPartialEmploye(ce.getEmploye()) : new Employe());
            contrat.setFichier(ce.getFichier());
            contrat.setId(ce.getId());
            contrat.setModePaiement((ce.getModePaiement() != null) ? UtilCom.buildBeanModeReglement(ce.getModePaiement()) : new ModeDeReglement());
            contrat.setSalaireHoraire((ce.getSalaireHoraire() != null) ? ce.getSalaireHoraire() : 0);
            contrat.setSalaireMensuel((ce.getSalaireMensuel() != null) ? ce.getSalaireMensuel() : 0);
            contrat.setCongeAcquis((ce.getCongeAcquis() != null) ? ce.getCongeAcquis() : 0);
            if (ce.getStructureSalaire() != null) {
                contrat.setStructSalaire(new StructureElementSalaire(ce.getStructureSalaire().getId(), ce.getStructureSalaire().getNom(), null));
            }
            if (ce.getTypeContrat() != null) {
                contrat.setTypeContrat(new TypeContrat(ce.getTypeContrat().getId(), ce.getTypeContrat().getLibelle()));
            }
            contrat.setActif((ce.getActif() != null) ? ce.getActif() : false);
            contrat.setReference(ce.getReferenceContrat());
            contrat.setCalendrier((ce.getCalendrier() != null) ? buildBeanCalendrier(ce.getCalendrier()) : new Calendrier());
            contrat.setDureePreavie((ce.getDureePreavie() != null) ? ce.getDureePreavie() : 0);
            contrat.setUnitePreavis(ce.getUnitePreavis());
            contrat.setDateFirstConge(ce.getDateFirstConge());
            contrat.setSourceFirstConge(ce.getSourceFirstConge());
        }
        return contrat;
    }

    public static List<ContratEmploye> buildBeanListContratEmploye(List<YvsGrhContratEmps> l, boolean retenu) {
        List<ContratEmploye> result = new ArrayList<>();
        for (YvsGrhContratEmps l1 : l) {
            ContratEmploye contrat = buildBeanContratEmploye(l1, retenu);
            result.add(contrat);
        }
        return result;
    }

    public static List<FinDeContrats> buildListBeanContratSuspendu(List<YvsGrhContratSuspendu> l) {
        List<FinDeContrats> result = new ArrayList<>();
        for (YvsGrhContratSuspendu l1 : l) {
            FinDeContrats contratS = buildBeanFinDeContrat(l1, true);
            result.add(contratS);
        }
        return result;
    }

    public static FinDeContrats buildBeanFinDeContrat(YvsGrhContratSuspendu cos, boolean loadContrat) {
        FinDeContrats re = new FinDeContrats();
        if (cos != null) {
            re.setAnciennete((cos.getAnciennete() != null) ? cos.getAnciennete() : 0);
            re.setAuthor(new Users(cos.getAuthor().getId(), cos.getAuthor().getUsers().getNomUsers()));
            if (loadContrat) {
                re.setContrat(buildBeanContratEmploye(cos.getContrat()));
            }
            re.setDateEffet(cos.getDateEffet());
            re.setDateSave(cos.getDateSave());
            re.setDurrePreavis((cos.getDureePreavis() != null) ? cos.getDureePreavis() : 0);
            re.setId(cos.getId());
            re.setMotif(cos.getMotif());
            re.setUniteDuree(cos.getUnitePeriodePreavis());
            re.setIndemnites(buildBeanElementIndemnite(cos.getIndemnites()));
        }
        return re;
    }

    public static List<TypeContrat> buildBeanListTypeContrat(List<YvsTypeContrat> l) {
        List<TypeContrat> result = new ArrayList<>();
        for (YvsTypeContrat l1 : l) {
            TypeContrat tContrat = new TypeContrat();
            tContrat.setId(l1.getId());
            tContrat.setLibelle(l1.getLibelle());
            tContrat.setSupp(l1.getSupp());
            result.add(tContrat);
        }
        return result;
    }

    public static ParamContrat buildBeanParamContrat(YvsGrhParamContrat p) {
        ParamContrat bean = new ParamContrat();
        bean.setId(p.getId());
        bean.setFormulePreavis(p.getFormulePreavis());
        bean.setFormuleSalaireMoyen(p.getFormuleSalaire());
        bean.setPeriodeReference(p.getPeriodeReference());
        bean.setSecteur(new SecteurActivite(p.getId().intValue()));
        bean.setAncienneteRequise((p.getAncienneteRequise() != null) ? p.getAncienneteRequise() : 0);
        return bean;
    }

    public static ElementIndemnite buildBeanElementIndemnite(YvsGrhElementsIndemnite in) {
        ElementIndemnite bean = new ElementIndemnite();
        if (in != null) {
            bean.setLibelle(in.getLibelle());
            bean.setBase(in.getBase());
            bean.setId(in.getId());
            bean.setQuantite(in.getQuantite());
            bean.setRetenue(in.getRetenue());
            bean.setTaux(in.getTaux());
            bean.setRubrique(buildSimpleBeanRubrique(in.getRubrique()));
        }
        return bean;
    }

    public static List<ElementIndemnite> buildBeanElementIndemnite(List<YvsGrhElementsIndemnite> l) {
        List<ElementIndemnite> re = new ArrayList<>();
        if (l != null) {
            for (YvsGrhElementsIndemnite r : l) {
                re.add(buildBeanElementIndemnite(r));
            }
        }
        return re;
    }

    public static RubriqueIndemnite buildSimpleBeanRubrique(YvsGrhRubriqueIndemnite r) {
        RubriqueIndemnite bean = new RubriqueIndemnite();
        if (r != null) {
            bean.setCode(r.getCode());
            bean.setDesignation(r.getDesignation());
            bean.setId(r.getId());
            bean.setOrdre(r.getOrdre());
            bean.setIndemnites(new ArrayList());
        }
        return bean;
    }

    public static List<RubriqueIndemnite> buildSimpleBeanRubrique(List<YvsGrhRubriqueIndemnite> l) {
        List<RubriqueIndemnite> re = new ArrayList<>();
        if (l != null) {
            for (YvsGrhRubriqueIndemnite r : l) {
                re.add(buildSimpleBeanRubrique(r));
            }
        }
        return re;
    }

    public static RubriqueIndemnite buildBeanRubrique(YvsGrhRubriqueIndemnite r) {
        RubriqueIndemnite bean = new RubriqueIndemnite();
        if (r != null) {
            bean.setCode(r.getCode());
            bean.setDesignation(r.getDesignation());
            bean.setId(r.getId());
            bean.setOrdre(r.getOrdre());
            bean.setIndemnites(buildBeanElementIndemnite(r.getIndemnites()));
        }
        return bean;
    }

    public static List<RubriqueIndemnite> buildBeanRubrique(List<YvsGrhRubriqueIndemnite> l) {
        List<RubriqueIndemnite> re = new ArrayList<>();
        if (l != null) {
            for (YvsGrhRubriqueIndemnite r : l) {
                re.add(buildBeanRubrique(r));
            }
        }
        return re;
    }

    public static GrilleTauxFinContrat buildBeanGrilleTaux(YvsGrhGrilleTauxFinContrat g) {
        GrilleTauxFinContrat gr = new GrilleTauxFinContrat();
        gr.setAncMax(g.getAncMax());
        gr.setAncMin(g.getAncMin());
        gr.setId(g.getId());
        gr.setTaux(g.getTaux());
        return gr;
    }

    public static List<GrilleTauxFinContrat> buildBeanGrilleTaux(List<YvsGrhGrilleTauxFinContrat> l) {
        List<GrilleTauxFinContrat> re = new ArrayList<>();
        if (l != null) {
            for (YvsGrhGrilleTauxFinContrat r : l) {
                re.add(buildBeanGrilleTaux(r));
            }
        }
        return re;
    }

    public static LibelleDroitFinContrat buildBeanLibelle(YvsGrhLibelleDroitFinContrat l) {
        LibelleDroitFinContrat le = new LibelleDroitFinContrat();
        le.setActif(l.getActif());
        le.setLibelle(l.getLibelle());
        le.setId(l.getId());
        return le;
    }

    public static List<LibelleDroitFinContrat> buildBeanLibelle(List<YvsGrhLibelleDroitFinContrat> l) {
        List<LibelleDroitFinContrat> re = new ArrayList<>();
        if (l != null) {
            for (YvsGrhLibelleDroitFinContrat r : l) {
                re.add(buildBeanLibelle(r));
            }
        }
        return re;
    }

    public static TypeContrat buildBeanTypeContrat(YvsTypeContrat l1) {
        TypeContrat tContrat = new TypeContrat();
        tContrat.setId(l1.getId());
        tContrat.setLibelle(l1.getLibelle());
        tContrat.setSupp(l1.getSupp());
        return tContrat;
    }

    public static TypeValidation buildBeanTypeValidation(YvsGrhTypeValidation tv) {
        TypeValidation re = new TypeValidation();
        if (tv != null) {
            re.setId(tv.getId());
            re.setLibelle(tv.getLibelle());
            re.setCode(tv.getCode());
            re.setActif(tv.getActif());
            re.setTaux(tv.getTauxJournee());
            re.setTempsMinimal(tv.getTempsMinimal());
        }
        return re;
    }

    public static YvsGrhTypeValidation buildBeanTypeContrat(TypeValidation l1) {
        YvsGrhTypeValidation tContrat = new YvsGrhTypeValidation();
        tContrat.setId(l1.getId());
        tContrat.setLibelle(l1.getLibelle());
        tContrat.setCode(l1.getCode().replace(' ', '_').trim());
        tContrat.setActif(l1.isActif());
        return tContrat;
    }

    public static List<TypeValidation> buildBeanListTypeValidation(List<YvsGrhTypeValidation> l) {
        List<TypeValidation> result = new ArrayList<>();
        for (YvsGrhTypeValidation l1 : l) {
            result.add(buildBeanTypeValidation(l1));
        }
        return result;
    }

    public static List<TypeElementAdd> buildBeanListTypePrime(List<YvsGrhTypeElementAdditionel> l) {
        List<TypeElementAdd> result = new ArrayList<>();
        for (YvsGrhTypeElementAdditionel l1 : l) {
            TypeElementAdd tContrat = new TypeElementAdd();
            tContrat.setId(l1.getId());
            tContrat.setLibelle(l1.getLibelle());
            tContrat.setCode(l1.getCodeElement());
            tContrat.setRetenue(l1.getRetenue());
            tContrat.setActif(l1.getActif());
            result.add(tContrat);
        }
        return result;
    }

    public static List<StructureElementSalaire> buildBeanListStructureSalaire(List<YvsGrhStructureSalaire> l) {
        List<StructureElementSalaire> result = new ArrayList<>();
        for (YvsGrhStructureSalaire l1 : l) {
            StructureElementSalaire structurSal = buildBeanStructure(l1);
            result.add(structurSal);
        }
        return result;
    }

    public static YvsGrhPersonneEnCharge getPersonne(PersonneEnCharge p) {
        YvsGrhPersonneEnCharge pe = new YvsGrhPersonneEnCharge();
        pe.setDateNaissance(p.getDateNaissance());
        pe.setInfirme(p.isInfirme());
        pe.setNom(p.getNom());
        pe.setScolarise(p.isScolarise());
        pe.setEpouse(p.isEpouse());
        pe.setSupp(false);
        pe.setId(p.getId());
        return pe;
    }

    public static YvsContactsEmps getContact(ContactEmps c) {
        YvsContactsEmps r = new YvsContactsEmps();
        if (c != null) {
            r.setActif(true);
            r.setAdress(c.getAdress());
            r.setId(c.getId());
            r.setNom(c.getNom());
            r.setTelephone(c.getTelephone());
            r.setSupp(false);
            r.setDateSave(c.getDateSave());
        }
        return r;
    }

    public static YvsCompteBancaire getCompteBancaire(CompteBancaire c) {
        YvsCompteBancaire r = new YvsCompteBancaire();
        if (c != null) {
            r.setActif(true);
            r.setId(c.getId());
            r.setSupp(false);
            r.setNumeroCompte(c.getNumeroCompte());
            r.setCleCompte(c.getCleCompte());
            r.setCodeBanque(c.getCodeBanque());
            r.setDateUpdate(new Date());
        }
        return r;
    }

    public static CompteBancaire buildBeanCompteBancaire(YvsCompteBancaire c) {
        CompteBancaire r = new CompteBancaire();
        if (c != null) {
            r.setActif(true);
            r.setId(c.getId());
            r.setSupp(false);
            r.setNumeroCompte(c.getNumeroCompte());
            r.setCleCompte(c.getCleCompte());
            r.setCodeBanque(c.getCodeBanque());
            r.setBanque(buildBeanBanque(c.getBanque()));
        }
        return r;
    }

    public static PersonneEnCharge getPersonne(YvsGrhPersonneEnCharge p) {
        PersonneEnCharge pe = new PersonneEnCharge();
        pe.setDateNaissance(p.getDateNaissance());
        pe.setInfirme(p.getInfirme());
        pe.setNom(p.getNom());
        pe.setScolarise(p.getInfirme());
        pe.setId((p.getId() != null) ? p.getId() : 0);
        return pe;
    }

    public static List<TypeCout> buildBeanListeTypeCout(List<YvsGrhTypeCout> type) {
        List<TypeCout> result = new ArrayList<>();
        if (type != null) {
            for (YvsGrhTypeCout type1 : type) {
                TypeCout cout = new TypeCout();
                cout.setId(type1.getId());
                cout.setLibelle(type1.getLibelle());
                result.add(cout);
            }
        }
        return result;
    }

    public static Articles buildPartialArticles(YvsBaseArticles art) {
        Articles result = new Articles();
        if (art != null) {
            result.setId(art.getId());
            result.setDescription(art.getDescription());
            result.setDesignation(art.getDesignation());
            result.setRefArt(art.getRefArt());
        }
        return result;
    }

    public static Formateur buildBeanFormateur(YvsGrhFormateur e) {
        Formateur r = new Formateur();
        if (e != null) {
            r.setId(e.getId());
            r.setAdresse(e.getAdresse());
            r.setNom(e.getNom());
            r.setTelephone(e.getTelephone());
        }
        return r;
    }

    public static List<Formateur> buildBeanListFormateur(List<YvsGrhFormateur> l) {
        List<Formateur> r = new ArrayList<>();
        if (l != null) {
            if (!l.isEmpty()) {
                for (YvsGrhFormateur f : l) {
                    r.add(buildBeanFormateur(f));
                }
            }
        }
        return r;
    }

    public static Formation buildBeanFormation(YvsGrhFormationEmps e) {
        Formation fm = buildBeanFormation(e.getFormation());
        return fm;
    }

    public static Formation buildBeanFormation(YvsGrhFormation e) {
        Formation fm = new Formation();
        if (e != null) {
            fm.setCout((e.getCoutFormation() != null) ? e.getCoutFormation() : 0);
            fm.setDescription(e.getDescription());
            fm.setDiplome(new Diplomes(e.getDiplome().getId(), e.getDiplome().getNom()));
            fm.setId(e.getId());
            fm.setDateDebut(e.getDateDebut());
            fm.setDateFin(e.getDateFin());
            fm.setFormateur((e.getFormateur() != null) ? buildBeanFormateur(e.getFormateur()) : new Formateur());
            fm.setReference(e.getReference());
            fm.setTitreFormation(e.getLibelle());
            fm.setLieuParDefaut((e.getLieuDefaut() != null) ? UtilSte.buildBeanDictionnaire(e.getLieuDefaut()) : new Dictionnaire());
            fm.setEmployes(buildBeanFormationEmp(e.getEmployes()));
            fm.setQualifications(buildBeanDomaineQualificationFormation(e.getQualifications()));
            fm.setDureeFormation(((e.getDateFin() != null) ? e.getDateFin().getDate() : new Date().getDate())
                    - ((e.getDateFin() != null) ? e.getDateDebut().getDate() : new Date().getDate()));
            fm.setSelectActif(false);
            fm.setStatutFormation(e.getStatutFormation());
            fm.setEtapesValidations(e.getEtapesValidations());
        } else {
            fm = null;
        }
        return fm;
    }

    public static List<DomainesQualifications> buildBeanDomaineQualificationFormation(List<YvsGrhQualificationFormation> lqu) {
        List<DomainesQualifications> re = new ArrayList<>();
        DomainesQualifications dom;
        Qualification qual;
        for (YvsGrhQualificationFormation qu : lqu) {
            dom = buildBeanDomaineQualification(qu.getQualification().getDomaine());
            qual = buildBeanQualification(qu.getQualification());
            qual.setIdLiaison(qu.getId());
            if (re.contains(dom)) {
                re.get(re.indexOf(dom)).getQualification().add(qual);
            } else {
                dom.getQualification().add(qual);
                re.add(dom);
            }
        }
        return re;
    }

    public static List<FormationEmps> buildBeanFormationEmp(List<YvsGrhFormationEmps> l) {
        List<FormationEmps> re = new ArrayList<>();
        for (YvsGrhFormationEmps e : l) {
            re.add(buildBeanFormationEmp(e));
        }
        return re;
    }

    public static List<QualificationFormation> buildListQualificationFormation(List<YvsGrhQualificationFormation> qu) {
        List<QualificationFormation> result = new ArrayList<>();
        for (YvsGrhQualificationFormation q2 : qu) {
            QualificationFormation q = new QualificationFormation();
            q.setQualif(buildBeanQualification(q2.getQualification()));
            result.add(q);
        }
        return result;
    }

    public static QualificationFormation buildListBeanQualificationFormation(YvsGrhQualificationFormation qu) {
        QualificationFormation q = new QualificationFormation();
        if (qu != null) {
            q.setQualif(buildBeanQualification(qu.getQualification()));
        }
        return q;
    }

    public static DomainesQualifications buildBeanDomaineQualification(YvsGrhDomainesQualifications qu) {
        DomainesQualifications re = new DomainesQualifications();
        if (qu != null) {
            re.setId(qu.getId());
            re.setTitreDomaine(qu.getTitreDomaine());
            re.setQualification(buildBeanQualification(qu.getQualifications()));
        }
        return re;
    }

    public static List<DomainesQualifications> buildBeanDomaineQualification(List<YvsGrhDomainesQualifications> lqu) {
        List<DomainesQualifications> re = new ArrayList<>();
        for (YvsGrhDomainesQualifications qu : lqu) {
            re.add(buildBeanDomaineQualification(qu));
        }
        return re;
    }

    public static DomainesQualificationCandidat buildBeanDomaineQualification_(YvsGrhDomainesQualifications qu) {
        DomainesQualificationCandidat re = new DomainesQualificationCandidat();
        if (qu != null) {
            re.setId(qu.getId());
            re.setTitreDomaine(qu.getTitreDomaine());
            re.setQualifications(buildBeanQualificationCandidat(qu.getQualifications()));
        }
        return re;
    }

    public static List<QualificationCandidat> buildBeanQualificationCandidat(List<YvsGrhQualifications> lqu) {
        List<QualificationCandidat> re = new ArrayList<>();
        for (YvsGrhQualifications qu : lqu) {
            re.add(new QualificationCandidat(qu.getId(), buildBeanQualification(qu)));
        }
        return re;
    }

    public static Qualification buildBeanQualification(YvsGrhQualifications qu) {
        Qualification re = new Qualification();
        if (qu != null) {
            re.setId(qu.getId());
            re.setCodeInterne(qu.getCodeInterne());
            re.setIntitule(qu.getDesignation());
            re.setActif(qu.getActif());
            re.setDomaine(new DomainesQualifications(qu.getDomaine().getId(), qu.getDomaine().getTitreDomaine()));
        }
        return re;
    }

    public static List<DomainesQualificationCandidat> buildBeanDomaineQualificationCandidat(List<YvsGrhDomainesQualifications> lqu) {
        List<DomainesQualificationCandidat> re = new ArrayList<>();
        for (YvsGrhDomainesQualifications qu : lqu) {
            re.add(buildBeanDomaineQualification_(qu));
        }
        return re;
    }

    public static List<Qualification> buildBeanQualification(List<YvsGrhQualifications> lqu) {
        List<Qualification> re = new ArrayList<>();
        for (YvsGrhQualifications qu : lqu) {
            re.add(buildBeanQualification(qu));
        }
        return re;
    }

    public static List<QualificationPoste> buildBeanQualificationPoste(List<YvsGrhQualificationPoste> lqu) {
        List<QualificationPoste> re = new ArrayList<>();
        for (YvsGrhQualificationPoste qu : lqu) {
            re.add(new QualificationPoste(qu.getId(), buildBeanQualification(qu.getQualification())));
        }
        return re;
    }

    public static List<DomainesQualificationPoste> buildOrderBeanQualificationPoste(List<YvsGrhQualificationPoste> lqu) {
        List<DomainesQualificationPoste> re = new ArrayList<>();
        DomainesQualificationPoste domain;
        if (lqu != null) {
            for (YvsGrhQualificationPoste qu : lqu) {
                domain = new DomainesQualificationPoste(qu.getQualification().getDomaine().getId(), qu.getQualification().getDomaine().getTitreDomaine());
                if (re.contains(domain)) {
                    domain.getQualifications().add(new QualificationPoste(qu.getId(), buildBeanQualification(qu.getQualification()), true));
                } else {
                    domain.getQualifications().add(new QualificationPoste(qu.getId(), buildBeanQualification(qu.getQualification()), true));
                    re.add(domain);
                }
            }
        }
        return re;
    }

    public static CoutFormation buildBeanCoutFormation(YvsCoutsFormation c) {
        CoutFormation cf = new CoutFormation();
        if (c != null) {
            // Boucle infinie
            //cf.setFormationEmp(buildBeanFormationEmp(c.getYvsFormationEmps()));
            cf.setMontant(c.getMontant());
            cf.setTypeCout(new TypeCout(c.getYvsGrhTypeCout().getId(), c.getYvsGrhTypeCout().getLibelle()));
        }
        return cf;
    }

    public static List<CoutFormation> buildListBeanCoutFormation(List<YvsCoutsFormation> c) {
        List<CoutFormation> cf = new ArrayList<>();
        if (c != null) {
            for (YvsCoutsFormation cout : c) {
                cf.add(buildBeanCoutFormation(cout));
            }
        }
        return cf;
    }

    public static ParametrageGRH buildBeanParametrageGRH(YvsParametreGrh param1) {
        ParametrageGRH p = new ParametrageGRH();
        if (param1 != null) {
            p.setActif(param1.getActif());
//            p.setDateDebutExercice(param1.getDateDebutExercice());
//            p.setDateFinExercice(param1.getDateFinExercice());
            p.setDatePaiementSalaire(param1.getDatePaiementSalaire());
            p.setDureeCumulConge(param1.getDureeCumuleConge());
            p.setEchellonage(param1.getEchellonageAutomatique());
            p.setId(param1.getId());
            p.setPeriodeDavancement(param1.getPeriodeDavancement());
            p.setPeriodePremierAvancement(param1.getPeriodePremierAvancement());
            p.setNombreMoisAvanceMaxRetenue(param1.getNombreMoisAvanceMaxRetenue());
            p.setLimitHeureSup(param1.getLimitHeureSup());
//            p.setSociete(buildBeanSociete(param1.getSociete()));
            p.setTotalHeureTravailHebdo(param1.getTotalHeureTravailHebd());
            p.setTotaCongePermis(param1.getTotalCongePermis());
            p.setSupp(param1.getSupp());
            p.setDateDebutTraitementSalaire((param1.getDateDebutTraitementSalaire() != null) ? param1.getDateDebutTraitementSalaire() : new Date());
            p.setCalendrier(buildBeanCalendrier(param1.getCalendrier()));
            p.setTimeMargeAvance(param1.getTimeMargeAvance());
            p.setDureeRetardAutorise(param1.getDureeRetardAutorise());
            p.setHeureMinimaleRequise(param1.getHeureMinimaleRequise());
            p.setPositionBaseSalaire(param1.getPositionBaseSalaire());
            p.setEcartSaisiPointage(param1.getDelaisSasiePointage());
            p.setEcartValideFiche(param1.getDelaisValidationPointage());
            p.setHeureDebutTravail(param1.getHeureDebutTravail());
            p.setHeureFinTravail(param1.getHeureFinTravail());
            p.setHeureDebutPause(param1.getHeureDebutPause());
            p.setHeureFinPause(param1.getHeureFinPause());
//            p.setTotalHeureTravailH(param1.getT);
        }
        return p;
    }

    public static JoursOuvres buildBeanJoursOuvree(YvsJoursOuvres j) {
        JoursOuvres jour = new JoursOuvres();
        if (j != null) {
            jour.setId(j.getId().intValue());
            jour.setActif((j.getActif() != null) ? j.getActif() : true);
            jour.setHeureDebutPause((j.getHeureDebutPause() != null) ? j.getHeureDebutPause() : new Date());
            jour.setHeureDebutTravail((j.getHeureDebutTravail() != null) ? j.getHeureDebutTravail() : new Date());
            jour.setHeureFinPause((j.getHeureFinPause() != null) ? j.getHeureFinPause() : new Date());
            jour.setHeureFinTravail((j.getHeureFinTravail() != null) ? j.getHeureFinTravail() : new Date());
            jour.setJour(j.getJour());
            jour.setDureePause(j.getDureePause());
            jour.setOuvrable(j.getOuvrable());
            jour.setJourDerepos(j.getJourDeRepos());
            jour.setOrdre(j.getOrdre());
            jour.setDateSave(j.getDateSave());
            if (j.getHeureDebutTravail() != null && j.getHeureFinTravail() != null) {
                jour.setDureeService(new Date(j.getHeureFinTravail().getTime() - j.getHeureDebutTravail().getTime() - Constantes.HOUR));
            }
            if (j.getHeureDebutPause() != null && j.getHeureFinPause() != null) {
                jour.setDureePause(new Date(j.getHeureFinPause().getTime() - j.getHeureDebutPause().getTime() - Constantes.HOUR));
            }

        }
        return jour;
    }

    static double totalTime;

    public static List<JoursOuvres> buildBeanListeJoursOuvree(List<YvsJoursOuvres> param) {
        List<JoursOuvres> result = new ArrayList<>();
        if (param != null) {
            if (!param.isEmpty()) {
                totalTime = 0;
                for (YvsJoursOuvres param1 : param) {
                    JoursOuvres p = buildBeanJoursOuvree(param1);
                    result.add(p);
                    totalTime += Utilitaire.timeToDouble(p.getDureeService());
                }
            }
        }
        Collections.sort(result, new JoursOuvres());
        return result;
    }

    public static List<ParametrageGRH> buildBeanListeParametrageGRH(List<YvsParametreGrh> param) {
        List<ParametrageGRH> result = new ArrayList<>();
        if (param != null) {
            if (!param.isEmpty()) {
                for (YvsParametreGrh param1 : param) {
                    ParametrageGRH p = buildBeanParametrageGRH(param1);
                    result.add(p);
                }
            }
        }
        return result;
    }

    public static ContratEmploye buildBeanContratsToContratEmpls(Contrats ce) {
        ContratEmploye co = new ContratEmploye();
        if (ce != null) {
            co.setDateDebut(ce.getDebut());
            co.setFichier(ce.getFile());
            co.setDateFin(ce.getFin());
            co.setFinEssai(ce.getFinEssai());
            co.setId(ce.getId());
            co.setSalaireHoraire(ce.getSalaireHoraire());
            co.setSalaireMensuel(ce.getSalaireMensuel());
            co.setReference(ce.getReference());
            co.setRegleTache(ce.getRegleTache());
        }
        return co;
    }

    public static Contrats buildBeanContratEmplsToContrats(ContratEmploye ce) {
        Contrats co = new Contrats();
        if (ce != null) {
            co.setDebut(ce.getDateDebut());
            co.setFile(ce.getFichier());
            co.setFin(ce.getDateFin());
            co.setFinEssai(ce.getFinEssai());
            co.setId(ce.getId());
            co.setSalaireHoraire(ce.getSalaireHoraire());
            co.setSalaireMensuel(ce.getSalaireMensuel());
            co.setReference(ce.getReference());
            co.setRegleTache(ce.getRegleTache());
        }
        return co;
    }

    /**
     * ******GESTION TACH
     *
     * @param regle
     * @return E********
     */
    public static RegleDeTache buildBeanRegleDeTache(YvsGrhRegleTache regle) {
        RegleDeTache r = new RegleDeTache();
        if (regle != null) {
            r.setCode(regle.getCode());
            r.setDesignation(regle.getNom());
            r.setId(regle.getId());
            r.setActif(regle.getActif());
            r.setListeTache(regle.getListeTaches());
            r.setListeTache(regle.getListeTaches());
            for (YvsGrhMontantTache t : r.getListeTache()) {
                r.setMontantTotal(r.getMontantTotal() + t.getMontant());
            }
        }
        return r;
    }

    public static RegleDeTache buildSimpleBeanRegleDeTache(YvsGrhRegleTache regle) {
        RegleDeTache r = new RegleDeTache();
        if (regle != null) {
            r.setCode(regle.getCode());
            r.setDesignation(regle.getNom());
            r.setId(regle.getId());
            r.setSupp(regle.getSupp());
            r.setActif(regle.getActif());
        }
        return r;
    }

    public static List<RegleDeTache> buildBeanListRegleDeTache(List<YvsGrhRegleTache> list) {
        List<RegleDeTache> result = new ArrayList<>();
        if (list != null) {
            for (YvsGrhRegleTache r : list) {
                result.add(buildBeanRegleDeTache(r));
            }
        }
        return result;
    }
    /*charge juste les propriétés des règle des tâches sans les tâches elle même*/

    public static List<RegleDeTache> buildSimpleBeanListRegleDeTache(List<YvsGrhRegleTache> list) {
        List<RegleDeTache> result = new ArrayList<>();
        if (list != null) {
            for (YvsGrhRegleTache r : list) {
                result.add(buildSimpleBeanRegleDeTache(r));
            }
        }
        return result;
    }

    public static Taches buildBeanTacheRegle(YvsGrhMontantTache tacheRegle) {
        Taches tache = buildBeanTaches((tacheRegle.getTaches() != null) ? tacheRegle.getTaches() : new YvsGrhTaches(tacheRegle.getTaches().getId()));
        Taches t = new Taches();
        t.setCodeTache(tache.getCodeTache());
        t.setDescription(tache.getDescription());
        t.setDesignation(tache.getDesignation());
        t.setId(tache.getId());
        t.setPrimeTache(buildBeanPrimeTache(tacheRegle.getPrimeTache()));
        t.setMontant(tacheRegle.getMontant());
        t.setSupp(tache.isSupp());
//        t.setRegleTache(buildBeanRegleDeTache(tacheRegle.getYvsRegleTache()));
        return t;
    }

    public static List<Taches> buildBeanListTacheRegle(List<YvsGrhMontantTache> list) {
        List<Taches> result = new ArrayList<>();
        if (list != null) {
            for (YvsGrhMontantTache r : list) {
                result.add(buildBeanTacheRegle(r));
            }
        }
        return result;
    }

    public static Taches buildBeanTaches(YvsGrhTaches tache) {
        Taches t = new Taches();
        if (tache != null) {
            t.setActif((tache.getActif() != null) ? tache.getActif() : true);
            t.setCodeTache(tache.getCodeTache());
            t.setDescription(tache.getDescription());
            t.setId(tache.getId());
            t.setDesignation(tache.getModuleTache());
        }
        return t;
    }

    public static List<Taches> buildBeanListTaches(List<YvsGrhTaches> list) {
        List<Taches> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhTaches r : list) {
                    result.add(buildBeanTaches(r));
                }
            }
        }
        return result;
    }

    public static PrimeTache buildBeanPrimeTache(YvsGrhPrimeTache prime) {
        PrimeTache p = new PrimeTache();
        if (prime != null) {
            p.setId(prime.getId());
            p.setDatePrime(prime.getDatePrime());
            p.setReference(prime.getReferencePrime());
            p.setActif((prime.getActif() != null) ? prime.getActif() : true);
            p.setListTranches(prime.getTranchesPrime());
        }
        return p;
    }

    public static List<PrimeTache> buildBeanListPrimeTache(List<YvsGrhPrimeTache> list) {
        List<PrimeTache> result = new ArrayList<>();
        if (list != null) {
            for (YvsGrhPrimeTache p : list) {
                result.add(buildBeanPrimeTache(p));
            }
        }
        return result;
    }

    public static PrimeTache buildBeanIntervalPrimeTache(YvsGrhIntervalPrimeTache prime) {
        PrimeTache p = new PrimeTache();
        if (prime != null) {
            p.setId(prime.getId().intValue());
            p.setMontant(prime.getMontant());
            p.setQuantite(prime.getQuantite());
            p.setTaux((prime.getTaux() != null) ? prime.getTaux() : false);
        }
        return p;
    }

    public static List<PrimeTache> buildBeanListIntervalPrimeTache(List<YvsGrhIntervalPrimeTache> list) {
        List<PrimeTache> result = new ArrayList<>();
        if (list != null) {
            for (YvsGrhIntervalPrimeTache p : list) {
                result.add(buildBeanIntervalPrimeTache(p));
            }
        }
        return result;
    }

    public static RegleDeTache buildBeanRegleDeTacheSansTache(YvsGrhRegleTache regle) {
        RegleDeTache r = new RegleDeTache();
        if (regle != null) {
            r.setCode(regle.getCode());
            r.setDesignation(regle.getNom());
            r.setId(regle.getId());
            r.setSupp(regle.getSupp());
            r.setActif(regle.getActif());
            for (YvsGrhMontantTache t : regle.getListeTaches()) {
                r.setMontantTotal(r.getMontantTotal() + t.getMontant());
            }
        }
        return r;
    }

    public static TacheEmps buildBeanTacheEmps(YvsGrhTacheEmps tache) {
        TacheEmps result = new TacheEmps();
        if (tache != null) {
            result.setActif(tache.isActif());
            result.setDateFin(tache.getDateFin());
            result.setDatePlanification(tache.getDatePlanification());
            result.setId(tache.getId());
//            result.setEmploye(buildBeanPartialEmploye(tache.getEmploye()));
            result.setTaches(buildBeanTaches(tache.getTache().getTaches()));
            result.setAttribuer(true);
        }
        return result;
    }

    public static List<TacheEmps> buildBeanListTacheEmp(List<YvsGrhTacheEmps> list) {
        List<TacheEmps> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhTacheEmps p : list) {
                    result.add(buildBeanTacheEmps(p));
                }
            }
        }
        return result;
    }

    /**
     * *******FIN GESTION TACHES**********
     */
    /**
     * PRIME ET AVANTAGE
     *
     * @param elt
     * @return
     */
    public static YvsGrhElementAdditionel buildBeanElt(ElementAdditionnel elt) {
        YvsGrhElementAdditionel e = new YvsGrhElementAdditionel();
        e.setDescription(elt.getDescription());
        e.setMontantElement(elt.getMontant());
        e.setDateElement(elt.getDate());
        e.setId(elt.getId());
        e.setPlanifier(elt.isPlanifie());
        e.setTypeElement(buildTypeElt(elt.getTypeElt()));
        return e;
    }

    public static ElementAdditionnel buildBeanElt(YvsGrhElementAdditionel elt, boolean retenu) {
        ElementAdditionnel e = new ElementAdditionnel();
        e.setTypeElt((elt.getTypeElement() != null) ? buildTypeElt(elt.getTypeElement()) : new TypeElementAdd());
        if (retenu == e.getTypeElt().isRetenue()) {
            e.setId(elt.getId());
            e.setDescription(elt.getDescription());
            e.setDate(elt.getDateElement());
            e.setDescription(elt.getDescription());
            e.setMontant(elt.getMontantElement());
            e.setPlanifie(elt.getPlanifier());
            e.setPermanent((elt.getPermanent() != null) ? elt.getPermanent() : true);
            e.setMontant(elt.getMontantElement());
            e.setSuspendu(!e.getListPrelevement().isEmpty());
            e.setStatut(elt.getStatut());
            e.setPlan((elt.getPlanPrelevement() != null) ? buildBeanPlanPrelevement(elt.getPlanPrelevement()) : new PlanPrelevement());
            if (!elt.getRetenues().isEmpty()) {
                e.setListPrelevement(elt.getRetenues());
            }
            e.setContrat((elt.getContrat() != null) ? buildBeanContratEmploye(elt.getContrat()) : new ContratEmploye());
            e.setMontantPlanifie(elt.getMontantPlanifie());
            e.setDateSave(elt.getDateSave());
            return e;
        }
        return null;
    }

    public static YvsGrhTypeElementAdditionel buildTypeElt(TypeElementAdd t) {
        YvsGrhTypeElementAdditionel ta = new YvsGrhTypeElementAdditionel();
        ta.setId(t.getId());
        ta.setActif(t.isActif());
        ta.setCodeElement(t.getCode());
        ta.setLibelle(t.getLibelle());
        ta.setRetenue(t.isRetenue());
        ta.setVisibleEnGescom(t.isVisibleEnGescom());
        if (t.getCompte() != null ? t.getCompte().getId() > 0 : false) {
            ta.setCompte(new YvsBasePlanComptable(t.getCompte().getId(), t.getCompte().getNumCompte(), t.getCompte().getIntitule()));
        }
        return ta;
    }

    public static TypeElementAdd buildTypeElt(YvsGrhTypeElementAdditionel t) {
        TypeElementAdd ta = new TypeElementAdd();
        if (t != null) {
            ta.setId(t.getId());
            ta.setActif(t.getActif());
            ta.setCode(t.getCodeElement());
            ta.setLibelle(t.getLibelle());
            ta.setRetenue(t.getRetenue());
            ta.setVisibleEnGescom(t.getVisibleEnGescom());
            ta.setCompte(UtilCompta.buildSimpleBeanCompte(t.getCompte()));
        }
        return ta;
    }

    public static List<ElementAdditionnel> buildBeanElt(List<YvsGrhElementAdditionel> list, boolean retenu) {
        List<ElementAdditionnel> result = new ArrayList<>();
        for (YvsGrhElementAdditionel e : list) {
            ElementAdditionnel ee = buildBeanElt(e, retenu);
            if (ee != null) {
//                if (!e.getRetenues().isEmpty()) {
//                    ee.setPrelevement(new PrelevementEmps(e.getRetenues().get(0).getId()));
//                }
                result.add(ee);
            }
        }
        return result;
    }

    public static ElementAdditionnel buildBeanElt(YvsGrhModelElementAdditionel elt) {
        ElementAdditionnel e = new ElementAdditionnel();
        e.setTypeElt((elt.getTypeElement() != null) ? buildTypeElt(elt.getTypeElement()) : new TypeElementAdd());
        e.setId(elt.getId());
        e.setDescription(elt.getDescription());
        e.setDate(elt.getDateElement());
        e.setDescription(elt.getDescription());
        e.setMontant(elt.getMontantElement());
        e.setPermanent((elt.getPermanent() != null) ? elt.getPermanent() : true);
        e.setMontant(elt.getMontantElement());
        e.setSuspendu(!e.getListPrelevement().isEmpty());
        return e;
    }

    public static List<ElementAdditionnel> buildBeanElt(List<YvsGrhModelElementAdditionel> list) {
        List<ElementAdditionnel> result = new ArrayList<>();
        for (YvsGrhModelElementAdditionel e : list) {
            ElementAdditionnel ee = buildBeanElt(e);
            result.add(ee);
        }
        return result;
    }

    /**
     * Gestion des pointages
     *
     * @param p
     * @return
     */
    public static PresenceEmploye buildSimpleBeanFichePresence(YvsGrhPresence fiche) {
        PresenceEmploye re = new PresenceEmploye();
        if (fiche != null) {
            re.setId(fiche.getId());
            re.setDateSave(fiche.getDateSave());
            re.setDateUpdate(fiche.getDateUpdate());
            re.setDebut(fiche.getDateDebut());
            re.setEmploye(fiche.getEmploye());
            re.setFin(fiche.getDateFin());
            re.setHeureDebut(fiche.getHeureDebut());
            re.setHeureFin(fiche.getHeureFin());
        }
        return re;
    }

    public static PointageEmploye buildBeanPointage(YvsGrhPointage p) {
        PointageEmploye fich = new PointageEmploye();
        fich.setId(p.getId());
        fich.setEntree(p.getHeureEntree());
        fich.setSortie(p.getHeureSortie());
        fich.setValider(p.getValider());
        fich.setMotif("En Service");
        fich.setHoraireNormale(p.getHoraireNormale());
        fich.setIdFiche(p.getPresence().getId());
        fich.setLineTransient(false);
        fich.setHeurePointageOut(p.getDateSaveSortie());
        fich.setHeurePonitageIn(p.getDateSaveEntree());
        fich.setPointageAuto(p.getPointageAutomatique());
        fich.setPresence(buildSimpleBeanFichePresence(p.getPresence()));
        if (p.getOperateurEntree() != null) {
            fich.setOperateurIn(new Users(p.getOperateurEntree().getId(), p.getOperateurEntree().getCodeUsers()));
        }
        if (p.getOperateurSortie() != null) {
            fich.setOperateurOut(new Users(p.getOperateurSortie().getId(), p.getOperateurSortie().getCodeUsers()));
        }

        return fich;
    }

    public static PointageEmploye buildSimpleBeanPointage(YvsGrhPointage p) {
        PointageEmploye fich = new PointageEmploye();
        fich.setId(p.getId());
        fich.setHeure(p.getHeureEntree());
        fich.setValider(p.getValider());
        fich.setHoraireNormale(p.getHoraireNormale());
        fich.setIdFiche(p.getPresence().getId());
        fich.setHeurePointageOut(p.getDateSaveSortie());
        fich.setHeurePonitageIn(p.getDateSaveEntree());
        fich.setPointageAuto(p.getPointageAutomatique());
        fich.setHeureCompensation(p.getCompensationHeure());
        fich.setHeureSupp(p.getHeureSupplementaire());
        fich.setPointeuse(p.getPointeuseIn());

        if (p.getOperateurEntree() != null) {
            fich.setOperateurIn(new Users(p.getOperateurEntree().getId(), p.getOperateurEntree().getCodeUsers()));
        }
        if (p.getOperateurSortie() != null) {
            fich.setOperateurOut(new Users(p.getOperateurSortie().getId(), p.getOperateurSortie().getCodeUsers()));
        }
        return fich;
    }

    public static YvsGrhPointage buildBeanPointage(PointageEmploye p) {
        YvsGrhPointage fich = new YvsGrhPointage();
        fich.setId(p.getId());
        fich.setActif(p.isValider());
        fich.setValider(p.isValider());
        fich.setDateSaveEntree(null);
//        fich.setMotif("En Service");
//        fich.setHoraireNormale(p.getHoraireNormale());
//        fich.setIdFiche(p.getPresence().getId());
//        fich.setLineTransient(false);
//        fich.setHeurePointageOut(p.getDateSaveSortie());
//        fich.setHeurePonitageIn(p.getDateSaveEntree());
//        fich.setPointageAuto(p.getPointageAutomatique());
//        if (p.getOperateurEntree() != null) {
//            fich.setOperateurIn(new Users(p.getOperateurEntree().getId(), p.getOperateurEntree().getCodeUsers()));
//        }
//        if (p.getOperateurSortie() != null) {
//            fich.setOperateurOut(new Users(p.getOperateurSortie().getId(), p.getOperateurSortie().getCodeUsers()));
//        }

        return fich;
    }

    public static List<PointageEmploye> buildBeanPointage(List<YvsGrhPointage> l) {
        List<PointageEmploye> r = new ArrayList<>();
        int i = 1;
        YvsGrhPointage svt;
        PointageEmploye interne;
        for (YvsGrhPointage p : l) {
            r.add(buildBeanPointage(p));
            if (l.size() > i) {
                //cette ligne permet d'ajouter une ligne de pointage qui n'a d'importance que visuelle
                //compare la sortie et l'entrée précédent
                svt = l.get(i);
                if (svt.getHeureEntree() != null && p.getHeureSortie() != null) {
                    if (!p.getHeureSortie().equals(svt.getHeureEntree())) {
                        interne = new PointageEmploye();
                        interne.setId(p.getId());
                        interne.setEntree(p.getHeureSortie());
                        interne.setSortie(svt.getHeureEntree());
                        interne.setValider(true);
                        interne.setLineTransient(true);
                        interne.setMotif(p.getMotifSortie());
                        interne.setHoraireNormale(p.getHoraireNormale());
                        interne.setIdFiche(p.getPresence().getId());
                        interne.setHeurePointageOut(p.getDateSaveSortie());
                        interne.setHeurePonitageIn(p.getDateSaveEntree());
                        interne.setPointageAuto(p.getPointageAutomatique());
                        interne.setPresence(buildSimpleBeanFichePresence(p.getPresence()));
                        if (p.getOperateurEntree() != null) {
                            interne.setOperateurIn(new Users(p.getOperateurEntree().getId(), p.getOperateurEntree().getCodeUsers()));
                        }
                        if (p.getOperateurSortie() != null) {
                            interne.setOperateurOut(new Users(p.getOperateurSortie().getId(), p.getOperateurSortie().getCodeUsers()));
                        }
                        r.add(interne);
                    }
                }
            }
            i++;
        }
        return r;
    }

    public static Taches buildBeanMontantTache(YvsGrhMontantTache tm) {
        Taches tache = new Taches();
        if (tm != null) {
            tache.setId(tm.getId());
            tache.setAttribuer(false);
            tache.setCodeTache(tm.getTaches().getCodeTache());
            tache.setDescription(tm.getTaches().getDescription());
            tache.setDesignation(tm.getTaches().getModuleTache());
            tache.setMontant(tm.getMontant());
            tache.setPrimeTache(buildBeanPrimeTache(tm.getPrimeTache()));
            tache.setEmployes(tm.getEmployes());
        }
        return tache;
    }

    public static List<Taches> buildBeanListMontantTache(List<YvsGrhMontantTache> list) {
        List<Taches> result = new ArrayList<>();
        if (list != null) {
            for (YvsGrhMontantTache r : list) {
                result.add(buildBeanMontantTache(r));
            }
        }
        return result;
    }

    /**
     * *******GESTION MISSION
     *
     **********
     * @param m1
     * @param missionEmp
     * @return
     */
//    public static MissionEmps buildBeanMissionEmps(YvsGrhMissionEmps missionEmp) {
//        MissionEmps mission = new MissionEmps();
//        if (missionEmp != null) {
//            mission.setId(missionEmp.getId());
//            mission.setEmploye(buildBeanPartialEmploye(missionEmp.getEmploye()));
//            mission.setRole(missionEmp.getRole());
//            mission.setCoutsMission((missionEmp.getYvsCoutMissionList() != null) ? buldBeanListCoutMissionEmps(missionEmp.getYvsCoutMissionList())
//                    : new ArrayList<CoutMissionEmps>());
//        }
//        return mission;
//    }
//    public static CoutMissionEmps buildBeanCoutMissionEmps(YvsGrhCoutMission c) {
//        CoutMissionEmps r = new CoutMissionEmps();
//        if (c != null) {
//            r.setId(c.getId());
//            r.setMontant(c.getMontant());
//            r.setTypeCout((c.getTypeCout() != null) ? new TypeCout(c.getTypeCout().getId(), c.getTypeCout().getLibelle()) : new TypeCout());
//        }
//        return r;
//    }
//
//    public static List<CoutMissionEmps> buldBeanListCoutMissionEmps(List<YvsGrhCoutMission> l) {
//        List<CoutMissionEmps> r = new ArrayList<>();
//        if (l != null) {
//            if (!l.isEmpty()) {
//                for (YvsGrhCoutMission c : l) {
//                    r.add(buildBeanCoutMissionEmps(c));
//                }
//            }
//        }
//        return r;
//    }
    public static MissionRessource buildBeanMissionRessource(YvsGrhMissionRessource m1) {
        MissionRessource ress = new MissionRessource();
        if (m1 != null) {
            ress.setId(m1.getId());
            ress.setQuantite(m1.getQuantite());
            ress.setArticle(m1.getRessource());
        }
        return ress;
    }

//    private static Mission buildBeanPartialMission(YvsGrhMissions m) {
//        Mission result = new Mission();
//        result.setDateDebut(m.getDateDebut());
//        result.setDateFin(m.getDateFin());
//        result.setDateMission(m.getDateMission());
//        result.setId(m.getId());
//        result.setLieu((m.getLieu() != null) ? UtilSte.buildBeanDictionnaire(m.getLieu()) : new Dictionnaire());
//        result.setOrdre(m.getOrdre());
//        return result;
//    }
    public static List<Mission> buildBeanListeMission(List<YvsGrhMissions> Lmission) {
        List<Mission> result = new ArrayList<>();
        if (Lmission != null) {
            for (YvsGrhMissions Lmission1 : Lmission) {
                Mission mission = buildBeanMission(Lmission1);
                result.add(mission);
            }
        }
        return result;
    }

    public static List<Mission> buildBeanListeMissionFiltree(List<YvsGrhMissions> Lmission) {
        List<Mission> result = new ArrayList<>();
        if (Lmission != null) {
            for (YvsGrhMissions Lmission1 : Lmission) {
                Mission mission = buildBeanMission(Lmission1);
                result.add(mission);
            }
        }
        return result;
    }

    public static Mission buildBeanMission(YvsGrhMissions miss) {
        Mission mission = new Mission();
        if (miss != null) {
            mission.setDateDebut(miss.getDateDebut());
            mission.setDateFin(miss.getDateFin());
            mission.setDateRetour(miss.getDateMission());
            mission.setId(miss.getId());
            mission.setOrdre(miss.getOrdre());
            mission.setCloturer(miss.getCloturer());
            mission.setStatutMission(miss.getStatutMission());
            mission.setTransport(miss.getTransport());
            mission.setDateRetour(miss.getDateRetour());
            mission.setHeureArrive(miss.getHeureArrive());
            mission.setHeureDepart(miss.getHeureDepart());
            mission.setRole(miss.getRoleMission());
            mission.setReference(miss.getReferenceMission());
            mission.setDateSave(miss.getDateSave());
            mission.setLieuEscale(miss.getLieuEscale());
            mission.setNumeroMission(miss.getNumeroMission());
            mission.setDureeMission(miss.getDureeMission());
            mission.setAuthor((miss.getValiderBy() != null) ? miss.getValiderBy().getNomUsers() : null);
            mission.setDateValider(miss.getDateValider());
            mission.setDateSave(miss.getDateSave());
            mission.setDateUpdate(miss.getDateUpdate());
            mission.setEtapeValide(miss.getEtapeValide());
            mission.setEtapeTotal(miss.getEtapeTotal());
            mission.setStatutPaiement(miss.getStatutPaiement());

            mission.setTotalPaye(miss.getTotalRegle());
            mission.setTotalPiece(miss.getTotalPiece());
            mission.setRestPlanifier(miss.getTotalReste());
            mission.setTotalFraisMission(miss.getTotalFraisMission());

            mission.setLieu((miss.getLieu() != null) ? UtilGrh.buildSimpleBeanDictionnaire(miss.getLieu()) : new Dictionnaire());
            mission.setObjet(buildSimpleBeanObjetMission(miss.getObjetMission()));
            mission.setEmploye(buildAllOnlyEMploye(miss.getEmploye()));
            mission.setFraisMission(buildSimpleGrilleMission(miss.getFraisMission()));
            mission.setAnalytiques(miss.getAnalytiques());
        }
        return mission;
    }

    public static List<Mission> buildBeanListeMission1(List<YvsGrhMissions> Lmission) {
        List<Mission> result = new ArrayList<>();
        if (Lmission != null) {
            for (YvsGrhMissions Lmission1 : Lmission) {
                Mission mission = buildBeanMission(Lmission1);
                result.add(mission);
            }
        }
        return result;
    }

//    public static List<MissionEmps> buildBeanListeMissionEmps(List<YvsGrhMissionEmps> LmissionEmps) {
//        List<MissionEmps> result = new ArrayList<>();
//        if (LmissionEmps != null) {
//            for (YvsGrhMissionEmps LmissionEmp : LmissionEmps) {
//                MissionEmps mission = buildBeanMissionEmps(LmissionEmp);
//                result.add(mission);
//            }
//        }
//        return result;
//    }
    public static List<MissionRessource> buildBeanListeMissionRessource(List<YvsGrhMissionRessource> m) {
        List<MissionRessource> result = new ArrayList<>();
        if (m != null) {
            for (YvsGrhMissionRessource m1 : m) {
                MissionRessource ress = buildBeanMissionRessource(m1);
                result.add(ress);
            }
        }
        return result;
    }

    public static List<Articles> buildBeanListeArticles(List<YvsBaseArticles> art) {
        List<Articles> result = new ArrayList<>();
        if (art != null) {
            for (YvsBaseArticles art1 : art) {
                Articles a = buildBeanArticles(art1);
                result.add(a);
            }
        }
        return result;
    }

    public static Articles buildBeanArticles(YvsBaseArticles art1) {
        Articles a = new Articles();
        if (art1 != null) {
            a.setId(art1.getId());
            a.setDescription(art1.getDescription());
            a.setDesignation(art1.getDesignation());
            a.setRefArt(art1.getRefArt());
            a.setChangePrix(art1.getChangePrix());
        }
        return a;
    }

    /**
     * *******FIN GESTION MISSION**********
     */
    /**
     * *******GESTION TABLEAU DE BORD
     *
     * @param list
     * @return
     */
    public static List<PosteDeTravail> buildBeanListPoste(List<YvsGrhPosteDeTravail> list) {
        List<PosteDeTravail> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhPosteDeTravail po : list) {
                    result.add(buildBeanPoste(po));
                }
            }
        }
        return result;
    }

    public static ModePaiement buildBeanModePaiement(YvsModePaiement mode) {
        ModePaiement result = new ModePaiement();
        if (mode != null) {
            result.setActif(mode.getActif());
            result.setId(mode.getId());
            result.setSupp(mode.getSupp());
            result.setTypePaiement(mode.getTypePaiement());
        }
        return result;
    }

    public static List<ModePaiement> buidBeanListeModePaiement(List<YvsModePaiement> list) {
        List<ModePaiement> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsModePaiement mo : list) {
                    ModePaiement mode = buildBeanModePaiement(mo);
                    result.add(mode);
                }
            }
        }
        return result;
    }

    public static Conges buildBeanCongeEmps(YvsGrhCongeEmps co) {
        Conges conge = new Conges();
        if (co != null) {
            conge.setStartDate(co.getDateDebut());
            conge.setEndDate(co.getDateFin());
            conge.setIdConge(co.getId());
            conge.setTitle(co.getEmploye().getNom() + " :- " + co.getLibelle());
            conge.setEffet(co.getEffet());
            conge.setTypeConge(co.getTypeConge());
            conge.setDateSave(co.getDateSave());
            conge.setDateUpdate(co.getDateUpdate());
            conge.setDateRetourConge(co.getDateRetour());
            conge.setTypeDureePermission(co.getDureePermission());
            conge.setNature(co.getNature());
            conge.setDureeAbsence(co.getDuree());
            if (co.getStatut() != null) {
                conge.setStatut(co.getStatut());
            }
            conge.setCompteJourRepos(co.getCompterJourRepos());
            conge.setCompteJourRepos(co.getCompterJourRepos());
            conge.setDescription(co.getMotif());
            conge.setEmploye(buildBeanSimpleEmploye(co.getEmploye()));
            conge.setEtapesValidations(co.getEtapesValidations());
            conge.setHeureFin(co.getHeureFin());
            conge.setHeureDebut(co.getHeureDebut());
            conge.setReferenceConge(co.getReferenceConge());
            conge.setEtapeValide(co.getEtapeValide());
//            conge.setAuthor((co.getAuthor() != null) ? co.getAuthor().getUsers().getNomUsers() : null);
        }
        return conge;
    }

    public static List<Conges> builBeanListCongeEmps(List<YvsGrhCongeEmps> list) {
        List<Conges> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhCongeEmps l : list) {
                    Conges conge = buildBeanCongeEmps(l);
                    result.add(conge);
                }
            }
        }
        return result;
    }

    /**
     * *******FIN GESTION TABLEAU DE BORD**********
     */
    /**
     * *******GESTION NOTE DE FRAIS
     *
     **********
     * @param f
     * @return
     */
    public static NoteDeFrais buildBeanNoteDeFrais(YvsGrhNotesFrais f) {
        NoteDeFrais n = new NoteDeFrais();
        if (f != null) {
            n.setId(f.getId());
            n.setDate(f.getDateNote());
            n.setDescription(f.getDescription());
            n.setStatut(f.getStatut());
            n.setEmploye(buildBeanSimplePartialEmploye(f.getEmploye()));
            n.setDepenses(buildBeanListDepense(f.getYvsGrhDepenseNoteList()));
            for (Depenses d : n.getDepenses()) {
                n.setTotalMontant(n.getTotalMontant() + d.getMontant());
                n.setTotalMontantApprouve(n.getTotalMontantApprouve() + d.getMontantApprouve());
            }
            if (f.getCentreDepense() != null) {
                n.setCentreAnal(buildBeanCentreDepense((f.getCentreDepense().getId() != null) ? f.getCentreDepense() : new YvsGrhCentreDepense()));
            }
        }
        return n;
    }

    public static List<NoteDeFrais> buildBeanListTypeNoteDeFrais(List<YvsGrhNotesFrais> list) {
        List<NoteDeFrais> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhNotesFrais t : list) {
                    NoteDeFrais d = buildBeanNoteDeFrais(t);
                    result.add(d);
                }
            }
        }
        return result;
    }

    public static Depenses buildBeanDepense(YvsGrhDepenseNote t) {
        Depenses p = new Depenses();
        if (t != null) {
            p.setMontant(t.getMontant());
            p.setMontantApprouve(t.getMontantApprouve());
            p.setTypeDeDepense(buildBeanTypeDepense(t.getYvsGrhTypeDepense()));
        }
        return p;
    }

    public static List<Depenses> buildBeanListDepense(List<YvsGrhDepenseNote> list) {
        List<Depenses> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhDepenseNote t : list) {
                    Depenses d = buildBeanDepense(t);
                    result.add(d);
                }
            }
        }
        return result;
    }

    public static TypeDepense buildBeanTypeDepense(YvsGrhTypeDepense t) {
        TypeDepense p = new TypeDepense();
        if (t != null) {
            p.setId(t.getId());
            p.setMarge(t.getMargeMontant());
            p.setMontant(t.getSeuilMontant());
            p.setTypeDepense(t.getLibelle());
        }
        return p;
    }

    public static List<TypeDepense> buildBeanListTypeDepense(List<YvsGrhTypeDepense> list) {
        List<TypeDepense> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhTypeDepense t : list) {
                    TypeDepense d = buildBeanTypeDepense(t);
                    result.add(d);
                }
            }
        }
        return result;
    }

    public static CentreDepense buildBeanCentreDepense(YvsGrhCentreDepense t) {
        CentreDepense p = new CentreDepense();
        if (t != null) {
            try {
                p.setId(t.getId());
                p.setIdSource(t.getIdSource());
                if (t.getSource() != null) {
                    p.setSource(Class.forName(t.getSource()));
                    switch (t.getSource()) {
                        case "yvs.entity.grh.param.YvsGrhDepartement":
                            p.setNameSource("Départements");
                            break;
                        case "yvs.entity.grh.activite.YvsMissions":
                            p.setNameSource("Missions");
                            break;
                        case "yvs.entity.grh.activite.YvsFormation":
                            p.setNameSource("Formations");
                            break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UtilGrh.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return p;
    }

    public static List<CentreDepense> buildBeanListCentreDepense(List<YvsGrhCentreDepense> list) {
        List<CentreDepense> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhCentreDepense t : list) {
                    CentreDepense d = buildBeanCentreDepense(t);
                    result.add(d);
                }
            }
        }
        return result;
    }

    /**
     * *******FIN GESTION NOTE DE FRAIS**********
     */
    /**
     * *******GESTION DES PLAN DE PRELEVEMENTS
     *
     **********
     * @param p
     * @return
     */
//    public static PlanPrelevement buildBeanDetailPlanPrelevement(YvsGrhDetailPlanPrelevement p) {
//        PlanPrelevement pp = new PlanPrelevement();
//        if (p != null) {
//            pp.setId(p.getId());
//            pp.setBaseTaux(p.getTaux());
//            pp.setBorneMaximal(p.getBorneMaximal());
//            pp.setBorneMinimal(p.getBorneMinimal());
//        }
//        return pp;
//    }
//
//    public static List<PlanPrelevement> buildBeanListDetailPlanPrelevement(List<YvsGrhDetailPlanPrelevement> list) {
//        List<PlanPrelevement> l = new ArrayList<>();
//        if (list != null) {
//            if (!list.isEmpty()) {
//                for (YvsGrhDetailPlanPrelevement p : list) {
//                    PlanPrelevement pp = buildBeanDetailPlanPrelevement(p);
//                    l.add(pp);
//                }
//            }
//        }
//        return l;
//    }
    public static PlanPrelevement buildBeanPlanPrelevement(YvsGrhPlanPrelevement p) {
        PlanPrelevement pp = new PlanPrelevement();
        if (p != null) {
            pp.setId(p.getId());
            pp.setReference(p.getReferencePlan());
            pp.setBase(p.getBaseRetenue());
            if (p.getBaseRetenue() == 'S') {
                pp.setNameBaseInterval("Salaire");
            } else if (p.getBaseRetenue() == 'D') {
                pp.setNameBaseInterval("Dette");
            }
            pp.setDefaut(p.getDefaut());
            pp.setValeur(p.getValeur());
            pp.setActif(p.getActif());
            pp.setVisibleEnGescom(p.getVisibleEnGescom());
            if (p.getBasePlan() != null) {
                pp.setBasePlan(p.getBasePlan());
            }
        }
        return pp;
    }

    public static List<PlanPrelevement> buildBeanListPlanPrelevement(List<YvsGrhPlanPrelevement> list) {
        List<PlanPrelevement> l = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhPlanPrelevement p : list) {
                    PlanPrelevement pp = buildBeanPlanPrelevement(p);
                    l.add(pp);
                }
            }
        }
        return l;
    }

    public static PrelevementEmps buildBeanDetailPrelevementEmploye(YvsGrhDetailPrelevementEmps p) {
        PrelevementEmps pp = new PrelevementEmps();
        if (p != null) {
            pp.setId(p.getId());
            pp.setValeur(p.getValeur());
            pp.setDatePrelevement(p.getDatePrelevement());
            pp.setElementAdd(buildBeanElt(p.getRetenue(), true));
            pp.setPlan(buildBeanPlanPrelevement(p.getPlanPrelevement()));
            pp.setReference(p.getReference());
            pp.setStatut(p.getStatutReglement());

        }
        return pp;
    }

    public static List<PrelevementEmps> buildBeanListDetailPrelevementEmploye(List<YvsGrhDetailPrelevementEmps> list) {
        List<PrelevementEmps> l = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhDetailPrelevementEmps p : list) {
                    PrelevementEmps pp = buildBeanDetailPrelevementEmploye(p);
                    l.add(pp);
                }
            }
        }
        return l;
    }

//    public static PrelevementEmps buildBeanPrelevementEmploye(YvsGrhPrelevementEmps p) {
//        PrelevementEmps pp = new PrelevementEmps();
//        if (p != null) {
//            pp.setId(p.getId());
//            pp.setBase((p.getBase() != null) ? p.getBase() : (short) 1);
//            if (pp.getBase() == 1) {
//                pp.setNameBase("Salaire");
//            } else if (pp.getBase() == 2) {
//                pp.setNameBase("Dette");
//            }
//            pp.setTaux((p.getTaux() != null) ? p.getTaux() : 0.0);
//            pp.setMontantPrelevement((p.getMontantPrelevement() != null) ? p.getMontantPrelevement() : 0.0);
//            pp.setDatePrelevement((p.getDatePrelevement() != null) ? p.getDatePrelevement() : new Date());
//            pp.setNombreMois((p.getNombreMois() != null) ? p.getNombreMois() : 0);
//            pp.setPlanDefaut((p.getPlanDefaut() != null) ? p.getPlanDefaut() : true);
//            pp.setListDetails(buildBeanListDetailPrelevementEmploye(p.getYvsGrhDetailPrelevementEmpsList()));
//            pp.setSuspendu((p.getSuspendu() != null) ? p.getSuspendu() : false);
//        }
//        return pp;
//    }
//
//    public static PrelevementEmps buildBeanSinglePrelevementEmploye(YvsGrhPrelevementEmps p) {
//        PrelevementEmps pp = new PrelevementEmps();
//        if (p != null) {
//            pp.setId(p.getId());
//            pp.setMontantPrelevement(p.getMontantPrelevement());
//            pp.setDatePrelevement(p.getDatePrelevement());
//            pp.setPrelevementRegle(p.getRegle());
//        }
//        return pp;
//    }
//
//    public static List<PrelevementEmps> buildBeanListPrelevementEmploye(List<YvsGrhPrelevementEmps> list) {
//        List<PrelevementEmps> l = new ArrayList<>();
//        if (list != null) {
//            if (!list.isEmpty()) {
//                for (YvsGrhPrelevementEmps p : list) {
//                    PrelevementEmps pp = buildBeanPrelevementEmploye(p);
//                    l.add(pp);
//                }
//            }
//        }
//        return l;
//    }
    /**
     * *******FIN GESTION DES PLAN DE PRELEVEMENTS**********
     */
    /**
     * ***Debut gestion salaire
     *
     *****
     * @param bs
     * @return
     */
    public static YvsGrhBulletins buildEntityBulletin(BulletinSalaire bs) {
        YvsGrhBulletins b = new YvsGrhBulletins();
        b.setContrat(new YvsGrhContratEmps(bs.getContrat().getId()));
        b.setDateCalcul(new Date());
        b.setDateDebut(bs.getDateDebut());
        b.setDateFin(bs.getDateFin());
        b.setId(bs.getId());
        b.setStatut(bs.getStatut());
        b.setNumero(bs.getNumero());
        b.setRefBulletin(bs.getRefBulletin());
        b.setNumMois(bs.getNumMonth());
        b.setComptabilise(bs.isComptabilise());
        b.setEntete(new YvsGrhOrdreCalculSalaire(bs.getEntete().getId()));
        return b;
    }

    public static List<BulletinSalaire> buildEntityBulletin(List<YvsGrhBulletins> le, boolean retenu) {
        List<BulletinSalaire> r = new ArrayList<>();
        for (YvsGrhBulletins m : le) {
            r.add(buildBeanBulletin(m, retenu));
        }
        return r;
    }

    public static BulletinSalaire buildBeanBulletin(YvsGrhBulletins bs, boolean retenu) {
        BulletinSalaire b = new BulletinSalaire();
        b.setContrat(buildBeanContratEmploye(bs.getContrat(), retenu));
        b.setDateDebut(bs.getDateDebut());
        b.setDateFin(bs.getDateFin());
        b.setDateCalcul(bs.getDateCalcul());
        b.setDateSave(bs.getDateSave());
        b.setDateUpdate(bs.getDateUpdate());
        b.setId(bs.getId());
        b.setStatut(bs.getStatut());
        b.setNumero(bs.getNumero());
        b.setRefBulletin(bs.getRefBulletin());
        b.setComptabilise(bs.getComptabilise());
        b.setNumMonth(bs.getNumMois());
        b.setListDetails(bs.getListDetails());
        if (bs.getEntete() != null) {
            OrdreCalculSalaire o = new OrdreCalculSalaire();
            o.setReference(bs.getEntete().getReference());
            o.setId(bs.getEntete().getId());
            b.setEntete(o);
        }
        return b;
    }

    public static YvsGrhDetailBulletin buildEntityMontantBulletin(DetailsBulletin e) {
        YvsGrhDetailBulletin mtn = new YvsGrhDetailBulletin();
        mtn.setBase(e.getBase());
        mtn.setQuantite(e.getQuantite());
        mtn.setTauxPatronal(Constantes.arrondi(e.getElementSalaire().getTauxPatronale(), 2));
        mtn.setTauxSalarial(Constantes.arrondi(e.getElementSalaire().getTauxSalarial(), 2));
        mtn.setMontantEmployeur(Constantes.arrondi(e.getMontantEmployeur(), 0));
        mtn.setMontantPayer(Constantes.arrondi(e.getMontantSalaire(), 0));
        mtn.setRetenuSalariale(Constantes.arrondi(e.getMontantRetenueSalarial(), 0));
        mtn.setDateSave(e.getDateSave());
        mtn.setDateUpdate(new Date());
        return mtn;
    }

    public static DetailsBulletin buildBeanMontantBulletin(YvsGrhDetailBulletin e) {
        DetailsBulletin mtn = new DetailsBulletin();
        mtn.setBase(e.getBase());
        mtn.setDateSave(e.getDateSave());
        mtn.setQuantite(e.getQuantite());
        ElementSalaire el = buildElementSalaire(e.getElement());
        el.setTauxPatronale(e.getTauxPatronal());
        el.setTauxSalarial(e.getTauxSalarial());
        mtn.setElementSalaire(el);
        return mtn;
    }

    public static List<DetailsBulletin> buildBeanMontantBulletin(List<YvsGrhDetailBulletin> le) {
        List<DetailsBulletin> r = new ArrayList<>();
        for (YvsGrhDetailBulletin m : le) {
            r.add(buildBeanMontantBulletin(m));
        }
        return r;
    }

    public static Calendrier buildBeanCalendrier(YvsCalendrier c) {
        Calendrier cal = new Calendrier();
        if (c != null) {
            cal.setId(c.getId());
            cal.setReference(c.getReference());
//            cal.setListJoursOuvres(buildBeanListeJoursOuvree(c.getJoursOuvres()));
            cal.setDefaut(c.isDefaut());
            cal.setTotalHeureHebdo(totalTime);
            cal.setActif(c.getActif());
            cal.setMarge(c.getTempsMarge());
            cal.setModule(c.getModule());
            cal.setDateSave(c.getDateSave());
        }
        return cal;
    }

    public static List<Calendrier> buildBeanListeCalendrier(List<YvsCalendrier> param) {
        List<Calendrier> result = new ArrayList<>();
        if (param != null) {
            if (!param.isEmpty()) {
                for (YvsCalendrier param1 : param) {
                    Calendrier p = buildBeanCalendrier(param1);
                    result.add(p);
                }
            }
        }
        return result;
    }

    /**
     * *******DEBUT GESTION ASSURANCE
     *
     **********
     * @param e
     * @return
     */
    public static BilanAssurance buildBeanBilanAssurance(YvsGrhBilanAssurance e) {
        BilanAssurance b = new BilanAssurance();
        if (e != null) {
            b.setId(e.getId());
            b.setFichier(e.getFichier());
            b.setDateAssurance((e.getDateAssurance() != null) ? e.getDateAssurance() : new Date());
            b.setMontant((e.getMontant() != null) ? e.getMontant() : 0);
//            b.setAssurance((e.getAssurance() != null) ? buildBeanAssurance(e.getAssurance()) : new Assurance());
        }
        return b;
    }

    public static List<BilanAssurance> buildBeanListBilanAssurance(List<YvsGrhBilanAssurance> e) {
        List<BilanAssurance> l = new ArrayList<>();
        if (e != null) {
            for (YvsGrhBilanAssurance i : e) {
                BilanAssurance b = buildBeanBilanAssurance(i);
                l.add(b);
            }
        }
        return l;
    }

    public static BilanAssurance buildBeanBilanCouverture(YvsGrhBilanCouverture e) {
        BilanAssurance b = new BilanAssurance();
        if (e != null) {
            b.setId(e.getId());
            b.setFichier(e.getFichier());
            b.setDateAssurance((e.getDateCouverture() != null) ? e.getDateCouverture() : new Date());
            b.setMontant((e.getMontant() != null) ? e.getMontant() : 0);
//            b.setAssurance((e.getCouverturePersonne() != null) ? buildBeanCouverture(e.getCouverturePersonne()) : new Assurance());
        }
        return b;
    }

    public static List<BilanAssurance> buildBeanListBilanCouverture(List<YvsGrhBilanCouverture> e) {
        List<BilanAssurance> l = new ArrayList<>();
        if (e != null) {
            for (YvsGrhBilanCouverture i : e) {
                BilanAssurance b = buildBeanBilanCouverture(i);
                l.add(b);
            }
        }
        return l;
    }

    public static PersonneEnCharge buildBeanPersonneCharge(YvsGrhPersonneEnCharge e) {
        PersonneEnCharge b = new PersonneEnCharge();
        if (e != null) {
            b.setId(e.getId());
            b.setDateNaissance((Date) ((e.getDateNaissance() != null) ? e.getDateNaissance() : new Date()));
            b.setNom(e.getNom());
            b.setInfirme((e.getInfirme() != null) ? e.getInfirme() : false);
            b.setScolarise((e.getScolarise() != null) ? e.getScolarise() : true);
            b.setEpouse((e.getEpouse() != null) ? e.getEpouse() : false);
        }
        return b;
    }

    public static List<PersonneEnCharge> buildBeanListPersonneCharge(List<YvsGrhPersonneEnCharge> e) {
        List<PersonneEnCharge> l = new ArrayList<>();
        if (e != null) {
            for (YvsGrhPersonneEnCharge i : e) {
                PersonneEnCharge b = buildBeanPersonneCharge(i);
                l.add(b);
            }
        }
        return l;
    }

    public static Assurance buildBeanCouverture(YvsGrhCouverturePersonneCharge e) {
        Assurance b = new Assurance();
        if (e != null) {
            b.setId(e.getId());
            b.setReference(e.getAssurance().getReference());
            b.setBilanAssuranceList((e.getYvsGrhBilanCouvertureList() != null) ? buildBeanListBilanCouverture(e.getYvsGrhBilanCouvertureList())
                    : new ArrayList<BilanAssurance>());
            b.setTauxCouverture((e.getTauxCouverture() != null) ? e.getTauxCouverture() : 0);
            b.setPersonneCharge((e.getPersonne() != null) ? buildBeanPersonneCharge(e.getPersonne()) : new PersonneEnCharge());
        }
        return b;
    }

    public static List<Assurance> buildBeanListCouverture(List<YvsGrhCouverturePersonneCharge> e) {
        List<Assurance> l = new ArrayList<>();
        if (e != null) {
            for (YvsGrhCouverturePersonneCharge i : e) {
                Assurance b = buildBeanCouverture(i);
                l.add(b);
            }
        }
        return l;
    }

    public static Assurance buildBeanAssurance(YvsGrhAssurance e) {
        Assurance b = new Assurance();
        if (e != null) {
            b.setId(e.getId());
            if ((e.getYvsGrhBilanAssuranceList() != null) ? !e.getYvsGrhBilanAssuranceList().isEmpty() : false) {
                b.setBilanAssuranceList(buildBeanListBilanAssurance(e.getYvsGrhBilanAssuranceList()));
            }
            b.setTauxCouverture((e.getTauxCouverture() != null) ? e.getTauxCouverture() : 0);
            b.setCouvertureList((e.getYvsGrhCouverturePersonneChargeList() != null) ? buildBeanListCouverture(e.getYvsGrhCouverturePersonneChargeList())
                    : new ArrayList<Assurance>());
            b.setReference(e.getReference());
            b.setTauxCotisation((e.getTauxCotisation() != null) ? e.getTauxCotisation() : 0);
            b.setTypeAssurance((e.getTypeAssurance() != null) ? buildBeanTypeAssurance(e.getTypeAssurance()) : new TypeAssurance());
//            b.setEmploye(buildBeanSimplePartialEmploye(e.getEmploye()));
        }
        return b;
    }

    public static List<Assurance> buildBeanListAssurance(List<YvsGrhAssurance> e) {
        List<Assurance> l = new ArrayList<>();
        if (e != null) {
            for (YvsGrhAssurance i : e) {
                Assurance b = buildBeanAssurance(i);
                l.add(b);
            }
        }
        return l;
    }

    public static TypeAssurance buildBeanTypeAssurance(YvsGrhTypeAssurance e) {
        TypeAssurance b = new TypeAssurance();
        if (e != null) {
            b.setId(e.getId());
            b.setDescription(e.getDescription());
            b.setLibelle(e.getLibelle());
//            b.setAssuranceList(buildBeanListAssurance(e.getYvsGrhAssuranceList()));
            b.setAssureur((e.getAssureur() != null) ? buildBeanAssureur(e.getAssureur()) : new Assureur());
        }
        return b;
    }

    public static List<TypeAssurance> buildBeanListTypeAssurance(List<YvsGrhTypeAssurance> e) {
        List<TypeAssurance> l = new ArrayList<>();
        if (e != null) {
            for (YvsGrhTypeAssurance i : e) {
                TypeAssurance b = buildBeanTypeAssurance(i);
                l.add(b);
            }
        }
        return l;
    }

    public static Assureur buildBeanAssureur(YvsGrhAssureur e) {
        Assureur b = new Assureur();
        if (e != null) {
            b.setId(e.getId());
            b.setNom(e.getNom());
            b.setTelephone(e.getTelephone());
            b.setAdresse(e.getAdresse());
            b.setSociete((e.getSociete() != null) ? buildBeanSociete(e.getSociete()) : new Societe());
//            b.setTypeAssuranceList(buildBeanListTypeAssurance(e.getYvsGrhTypeAssuranceList()));
        }
        return b;
    }

    public static List<Assureur> buildBeanListAssureur(List<YvsGrhAssureur> e) {
        List<Assureur> l = new ArrayList<>();
        if (e != null) {
            for (YvsGrhAssureur i : e) {
                Assureur b = buildBeanAssureur(i);
                l.add(b);
            }
        }
        return l;
    }

    /**
     * *******FIN GESTION ASSURANCE**********
     */
    /**
     * *******DEBUT GESTION MAJORATION CONGE**********
     *
     * @param m
     * @return
     */
    public static MajorationConge buildBeanMajorationConge(YvsGrhMajorationConge m) {
        MajorationConge c = new MajorationConge();
        if (m != null) {
            c.setId(m.getId());
            c.setActif((m.getActif() != null) ? m.getActif() : true);
            c.setNature(m.getNature());
            c.setListIntervalle(m.getListIntervalle());
            c.setUniteIntervalle(m.getUniteIntervalle());
        }
        return c;
    }

    public static List<MajorationConge> buildBeanListMajorationConge(List<YvsGrhMajorationConge> l) {
        List<MajorationConge> r = new ArrayList<>();
        if (l != null) {
            if (!l.isEmpty()) {
                for (YvsGrhMajorationConge l1 : l) {
                    MajorationConge m = buildBeanMajorationConge(l1);
                    r.add(m);
                }
            }
        }
        return r;
    }

    public static IntervalMajoration buildBeanIntervalMajoration(YvsGrhIntervalMajoration i) {
        IntervalMajoration r = new IntervalMajoration();
        if (i != null) {
            r.setId(i.getId());
            r.setActif((i.getActif() != null) ? i.getActif() : true);
            r.setNbreJour((i.getNbJour() != null) ? i.getNbJour() : 0.0);
            r.setValeurMaximal(i.getValeurMaximal());
            r.setValeurMinimal(i.getValeurMinimal());
            r.setMajorationConge(buildBeanMajorationConge(i.getMajorationConge()));
        }
        return r;
    }

    public static List<IntervalMajoration> buildBeanListIntervalMajoration(List<YvsGrhIntervalMajoration> l) {
        List<IntervalMajoration> r = new ArrayList<>();
        if (l != null) {
            if (!l.isEmpty()) {
                for (YvsGrhIntervalMajoration l1 : l) {
                    IntervalMajoration m = buildBeanIntervalMajoration(l1);
                    r.add(m);
                }
            }
        }
        return r;
    }

    /**
     * *******FIN GESTION MAJORATION CONGE**********
     *
     * @param e
     * @return
     */
    public static Employe buildBeanEmploye(YvsGrhEmployes e) {
        return buildBeanSimpleEmploye(e);
    }

    public static EmployePartial buildBeanSimpleEmployePartial(YvsGrhEmployes e) {
        EmployePartial emp = new EmployePartial();
        if (e != null) {
            emp.setId(e.getId());
            emp.setNom(e.getNom());
            emp.setPrenom(e.getPrenom());
            emp.setMatricule(e.getMatricule());
            emp.setDateEmbauche(e.getDateEmbauche());
            emp.setPhotos(e.getPhotos());
            emp.setCni(e.getCni());
            emp.setCivilite(e.getCivilite());
            emp.setPaysDorigine((e.getPays() != null) ? UtilSte.buildBeanDictionnaire(e.getPays())
                    : new Dictionnaire());
            emp.setVilleNaissance((e.getLieuNaissance() != null) ? UtilSte.buildBeanDictionnaire(e.getLieuNaissance())
                    : new Dictionnaire());
        }
        return emp;
    }

    public static Employe buildBeanSimpleEmploye(YvsGrhEmployes e) {
        Employe emp = new Employe();
        if (e != null) {
            emp.setId(e.getId());
            emp.setNom(e.getNom());
            emp.setPrenom(e.getPrenom());
            emp.setMatricule(e.getMatricule());
            emp.setDateEmbauche(e.getDateEmbauche());
            emp.setPhotos(e.getPhotos());
            emp.setCni(e.getCni());
            emp.setCivilite(e.getCivilite());
        }
        return emp;
    }

    public static EmployePartial buildBeanEmployeToEmployePartial(YvsGrhEmployes e) {
        EmployePartial emp = new EmployePartial();
        if (e != null) {
            emp.setId(e.getId());
            emp.setNom(e.getNom());
            emp.setPrenom(e.getPrenom());
            emp.setMatricule(e.getMatricule());
            emp.setDateEmbauche(e.getDateEmbauche());
            emp.setPhotos(e.getPhotos());
            emp.setCni(e.getCni());
            emp.setCivilite(e.getCivilite());
//            emp.setPaysDorigine(e.getPaysDorigine());
//            emp.setVilleNaissance(e.getVilleNaissance());
        }
        return emp;
    }

    public static Employe buildBeanEmployePartialToEmploye(EmployePartial e) {
        Employe emp = new Employe();
        if (e != null) {
            emp.setId(e.getId());
            emp.setNom(e.getNom());
            emp.setPrenom(e.getPrenom());
            emp.setMatricule(e.getMatricule());
            emp.setDateEmbauche(e.getDateEmbauche());
            emp.setPhotos(e.getPhotos());
            emp.setCni(e.getCni());
            emp.setCivilite(e.getCivilite());
            emp.setPaysDorigine(e.getPaysDorigine());
            emp.setVilleNaissance(e.getVilleNaissance());
        }
        return emp;
    }

    /**
     * ******* GESTION SANCTION
     *
     **********
     * @param d
     * @return
     */
    public static DecisionSanction buildBeanDecisionSanction(YvsGrhDecisionSanction d) {
        DecisionSanction r = new DecisionSanction();
        if (d != null) {
            r.setActif((d.getActif() != null) ? d.getActif() : true);
            r.setDescriptionMotif(d.getDescriptionMotif());
            r.setLibelle(d.getLibelle());
            r.setDuree(d.getDuree());
            r.setId(d.getId());
//            r.setListSanction((d.getYvsGrhSanctionList() != null) ? buildBeanListSanction(d.getYvsGrhSanctionList())
//                    : new ArrayList<Sanction>());
            r.setSociete((d.getSociete() != null) ? buildBeanSociete(d.getSociete()) : new Societe());
        }
        return r;
    }

    public static List<DecisionSanction> buildBeanListDecisionSanction(List<YvsGrhDecisionSanction> list) {
        List<DecisionSanction> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhDecisionSanction d : list) {
                    DecisionSanction d1 = buildBeanDecisionSanction(d);
                    result.add(d1);
                }
            }
        }
        return result;
    }

    public static FauteSanction buildBeanFauteSanction(YvsGrhFauteSanction d) {
        FauteSanction r = new FauteSanction();
        if (d != null) {
            r.setLibelle(d.getLibelle());
            r.setId(d.getId());
//            r.setListSanction((d.getYvsGrhSanctionList() != null) ? buildBeanListSanction(d.getYvsGrhSanctionList())
//                    : new ArrayList<Sanction>());
            r.setSociete((d.getSociete() != null) ? buildBeanSociete(d.getSociete()) : new Societe());
        }
        return r;
    }

    public static List<FauteSanction> buildBeanListFauteSanction(List<YvsGrhFauteSanction> list) {
        List<FauteSanction> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhFauteSanction d : list) {
                    FauteSanction d1 = buildBeanFauteSanction(d);
                    result.add(d1);
                }
            }
        }
        return result;
    }

    public static Sanction buildBeanSanction(YvsGrhSanction d) {
        Sanction r = new Sanction();
        if (d != null) {
            r.setActif((d.getActif() != null) ? d.getActif() : true);
            r.setPoint((d.getPoint() != null) ? d.getPoint() : 0);
            r.setSupp((d.getSupp() != null) ? d.getSupp() : false);
            r.setDescription(d.getDescription());
            r.setCode(d.getCode());
            r.setFaute((d.getFaute() != null) ? buildBeanFauteSanction(d.getFaute()) : new FauteSanction());
            r.setDecision((d.getDecision() != null) ? buildBeanDecisionSanction(d.getDecision()) : new DecisionSanction());
            r.setId(d.getId());
        }
        return r;
    }

    public static List<Sanction> buildBeanListSanction(List<YvsGrhSanction> list) {
        List<Sanction> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhSanction d : list) {
                    Sanction d1 = buildBeanSanction(d);
                    result.add(d1);
                }
            }
        }
        return result;
    }

    public static SanctionEmps buildBeanSanctionEmps(YvsGrhSanctionEmps semps) {
        SanctionEmps sanctE = new SanctionEmps();
        if (semps != null) {
            sanctE.setId(semps.getId());
            sanctE.setActif((semps.getActif() != null) ? semps.getActif() : true);
            sanctE.setDateDebut((semps.getDateDebut() != null) ? semps.getDateDebut() : new Date());
            sanctE.setDateFin((semps.getDateFin() != null) ? semps.getDateFin() : new Date());
            sanctE.setDateSanction((semps.getDateSanction() != null) ? semps.getDateSanction() : new Date());
            sanctE.setSupp((semps.getSupp() != null) ? semps.getSupp() : false);
            sanctE.setSanction((semps.getSanction() != null) ? buildBeanSanction(semps.getSanction()) : new Sanction());
            sanctE.setEmploye((semps.getEmploye() != null) ? buildBeanSimplePartialEmploye(semps.getEmploye()) : new Employe());
        }
        return sanctE;
    }

    public static List<SanctionEmps> buildBeanListSanctionEmps(List<YvsGrhSanctionEmps> semps) {
        List<SanctionEmps> sanctE = new ArrayList<>();
        if (semps != null) {
            if (!semps.isEmpty()) {
                for (YvsGrhSanctionEmps gard : semps) {
                    SanctionEmps gardbean = buildBeanSanctionEmps(gard);
                    sanctE.add(gardbean);
                }
            }
        }
        return sanctE;
    }

    public static List<Employe> buildBeanListEmployeBySanction(List<YvsGrhSanctionEmps> semps) {
        List<Employe> l = new ArrayList<>();
        if (semps != null) {
            if (!semps.isEmpty()) {
                for (YvsGrhSanctionEmps gard : semps) {
                    Employe em = buildBeanSimplePartialEmploye(gard.getEmploye());
                    if (!l.contains(em)) {
                        l.add(em);
                    }
                }
            }
        }
        return l;
    }

    /**
     * *******FIN GESTION SANCTION**********
     */
    /**
     * *******DEBUT GESTION CONTACT EMPLOYE
     *
     **********
     * @param c
     * @return
     */
    public static ContactEmps buildBeanContactEmps(YvsContactsEmps c) {
        ContactEmps r = new ContactEmps();
        if (c != null) {
            r.setActif((c.getActif() != null) ? c.getActif() : true);
            r.setAdress(c.getAdress());
            r.setId(c.getId());
            r.setNom(c.getNom());
            r.setTelephone(c.getTelephone());
            r.setSupp((c.getSupp() != null) ? c.getSupp() : false);
            r.setDateSave(c.getDateSave());
        }
        return r;
    }

    public static List<ContactEmps> buildBeanListContactEmps(List<YvsContactsEmps> list) {
        List<ContactEmps> r = new ArrayList<>();
        if (list != null) {
            for (YvsContactsEmps c : list) {
                ContactEmps e = buildBeanContactEmps(c);
                r.add(e);
            }
        }
        return r;
    }

    /**
     * *******FIN GESTION CONTACT EMPLOYE**********
     */
    /**
     * *******DEBUT GESTION COMPTE BANCAIRE
     *
     **********
     * @param c
     * @return
     */
    public static List<CompteBancaire> buildBeanListCompteBancaire(List<YvsCompteBancaire> list) {
        List<CompteBancaire> r = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsCompteBancaire c : list) {
                    CompteBancaire e = buildBeanCompteBancaire(c);
                    r.add(e);
                }
            }
        }
        return r;
    }

    public static List<Dictionnaire> buildBeanDictionnaire(List<YvsDictionnaire> l) {
        List<Dictionnaire> r = new ArrayList<>();
        for (YvsDictionnaire c : l) {
            r.add(UtilSte.buildBeanDictionnaire(c));
        }
        return r;
    }

    public static YvsDictionnaire buildDictionnaire(Dictionnaire d, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        YvsDictionnaire di = new YvsDictionnaire();
        di.setId(d.getId());
        di.setTitre(d.getTitre());
        di.setLibele(d.getLibelle());
        di.setAbreviation(d.getAbreviation());
        if (d.getParent() != null ? d.getParent().getId() > 0 : false) {
            di.setParent(new YvsDictionnaire(d.getParent().getId(), d.getParent().getLibelle(), d.getParent().getAbreviation()));
        }
        di.setDateSave(d.getDateSave());
        di.setDateUpdate(new Date());
        di.setSociete(currentScte);
        di.setAuthor(currentUser);
        di.setActif(d.isUpdate() ? d.isActif() : true);
        di.setSupp(false);
        di.setNew_(true);
        return di;
    }

    public static Dictionnaire buildSimpleBeanDictionnaire(YvsDictionnaire d) {
        Dictionnaire r = new Dictionnaire();
        if (d != null) {
            r.setId(d.getId());
            r.setLibelle(d.getLibele());
            r.setTitre(d.getTitre());
            r.setAbreviation(d.getAbreviation());
            r.setDateSave(d.getDateSave());
            r.setDateUpdate(d.getDateUpdate());
            r.setActif(d.getActif());
        }
        return r;
    }

    public static Dictionnaire buildBeanDictionnaire(YvsDictionnaire d) {
        Dictionnaire r = buildSimpleBeanDictionnaire(d);
        if (d != null) {
            r.setParent(d.getParent() != null ? new Dictionnaire(d.getParent().getId(), d.getParent().getLibele(), d.getParent().getAbreviation()) : new Dictionnaire());
            Collections.sort(d.getFils());
            r.setFils(d.getFils());
//            r.setTarifs(d.getTarifs());
            r.setUpdate(true);
        }
        return r;
    }

    public static TarifLieux buildBeanTarifLieux(YvsBaseTarifPointLivraison d) {
        TarifLieux r = new TarifLieux();
        if (d != null) {
            r.setId(d.getId());
            r.setFraisLivraison(d.getFraisLivraison());
            r.setDelaiForLivraison(d.getDelaiForLivraison());
            r.setDelaiForRetrait(d.getDelaiForRetrait());
            r.setLivraisonDomicile(d.getLivraisonDomicile());
            r.setDelaiRetour(d.getDelaiRetour());
            r.setArticle(UtilProd.buildSimpleBeanArticles(d.getArticle()));
            r.setDateSave(d.getDateSave());
        }
        return r;
    }

    public static YvsBaseTarifPointLivraison buildTarifLieux(TarifLieux d, YvsSocietes ste, YvsUsersAgence ua) {
        YvsBaseTarifPointLivraison r = new YvsBaseTarifPointLivraison();
        if (d != null) {
            r.setId(d.getId());
            r.setFraisLivraison(d.getFraisLivraison());
            r.setDelaiForLivraison(d.getDelaiForLivraison());
            r.setDelaiForRetrait(d.getDelaiForRetrait());
            r.setLivraisonDomicile(d.isLivraisonDomicile());
            r.setDelaiRetour(d.getDelaiRetour());
            if (d.getLieux() != null ? d.getLieux().getId() > 0 : false) {
                r.setLieuxLiv(new YvsDictionnaireInformations(d.getLieux().getId()));
            }
            if (d.getArticle() != null ? d.getArticle().getId() > 0 : false) {
                r.setArticle(new YvsBaseArticles(d.getArticle().getId(), d.getArticle().getRefArt(), d.getArticle().getDesignation()));
            }
            r.setDateSave(d.getDateSave());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static Banques buildBeanBanque(YvsBanques b) {
        Banques ba = new Banques();
        if (b != null) {
            ba.setId(b.getId());
            ba.setCodePostal(b.getBoitePostal());
            ba.setTelephone(b.getTelephone());
            ba.setActif(b.getActif());
            ba.setNom(b.getNom());
            ba.setPays(UtilSte.buildBeanDictionnaire(b.getPays()));
            ba.setVille(UtilSte.buildBeanDictionnaire(b.getVille()));
        }
        return ba;
    }

    public static YvsBanques buildEntityBanque(Banques b) {
        YvsBanques ba = new YvsBanques();
        if (b != null) {
            ba.setId(b.getId());
            ba.setBoitePostal(b.getCodePostal());
            ba.setTelephone(b.getTelephone());
            ba.setActif(b.isActif());
            ba.setNom(b.getNom());
            ba.setVille((b.getVille().getId() != 0) ? new YvsDictionnaire(b.getVille().getId()) : null);
            ba.setPays((b.getPays().getId() != 0) ? new YvsDictionnaire(b.getPays().getId()) : null);
        }
        return ba;
    }

    /**
     * *******FIN GESTION COMPTE BANCAIRE**********
     */
    /**
     * *******DEBUT GESTION CHOMAGE
     *
     **********
     * @param c
     * @return
     */
//    public static ParamsTauxChomageTechnique buildBeanDetailTypeChomage(YvsGrhDetailTypeChomage c) {
//        ParamsTauxChomageTechnique r = new ParamsTauxChomageTechnique();
//        if (c != null) {
//            r.setId(c.getId());
//        }
//        return r;
//    }
//
//    public static List<TypeChomage> buildBeanListDetailTypeChomage(List<YvsGrhDetailTypeChomage> list) {
//        List<TypeChomage> r = new ArrayList<>();
//        if (list != null) {
//            if (!list.isEmpty()) {
//                for (YvsGrhDetailTypeChomage d : list) {
//                    ParamsTauxChomageTechnique de = buildBeanDetailTypeChomage(d);
//                    r.add(de);
//                }
//            }
//        }
//        return r;
//    }
    public static ParamsTauxChomageTechnique buildBeanTypeChomage(YvsGrhTypeChomage c) {
        ParamsTauxChomageTechnique r = new ParamsTauxChomageTechnique();
        if (c != null) {
//            r.setId(c.getId());
//            r.setLibelle(c.getLibelle());
//            r.setCode(c.getCode());
//            r.setActif((c.getActif() != null) ? c.getActif() : true);
//            r.setDescription(c.getDescription());
//            if (c.getEtat() != null) {
//                r.setEtat(c.getEtat());
//            }
//            r.setEmployes(buildBeanListChomageEmps(c.getDetailsChomage()));
        }
        return r;
    }

    public static List<ParamsTauxChomageTechnique> buildBeanListTypeChomage(List<YvsGrhTypeChomage> list) {
        List<ParamsTauxChomageTechnique> r = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhTypeChomage d : list) {
                    ParamsTauxChomageTechnique de = buildBeanTypeChomage(d);
                    r.add(de);
                }
            }
        }
        return r;
    }
//

    public static DetailChomagesTec buildBeanChomageEmps(YvsGrhDetailsChomageTechnique c) {
        DetailChomagesTec r = new DetailChomagesTec();
        if (c != null) {
            r.setId(c.getId());
//            r.setEmployes(buildBeanSimplePartialEmploye(c.getEmploye()));
        }
        return r;
    }
//

    public static List<YvsGrhEmployes> buildBeanListChomageEmps(List<YvsGrhDetailsChomageTechnique> list) {
        List<YvsGrhEmployes> re = new ArrayList<>();
        if (list != null) {
            for (YvsGrhDetailsChomageTechnique d : list) {
                if (!re.contains(d.getConge().getEmploye())) {
                    re.add(d.getConge().getEmploye());
                }
            }
        }
        return re;
    }

    /**
     * *******FIN GESTION CHOMAGE
     *
     **********
     * @param o
     * @return
     */
    public static YvsGrhOrdreCalculSalaire buildBeanOrdrePlanif(OrdreCalculSalaire o) {
        if (o != null) {
            YvsGrhOrdreCalculSalaire re = new YvsGrhOrdreCalculSalaire();
            re.setId(o.getId());
            re.setCloture(o.isCloture());
            re.setDateExecution(o.getDateExecution());
            re.setDateJour(o.getDateJour());
            re.setDebutMois(o.getDebutMois());
            re.setFinMois(o.getFinMois());
            re.setHeureExecution(o.getHeureExecution());
            re.setRealise(o.isRealise());
            re.setComptabilise(o.isComptabilise());
            re.setReference(o.getReference());
            re.setDateCloture(o.getDateCloture());
            re.setDateDebutTraitement(o.getDebutTraitement());
            re.setDateFinTraitement(o.getFinTraitement());
            return re;
        } else {
            return null;
        }
    }

    public static OrdreCalculSalaire buildBeanOrdrePlanif(YvsGrhOrdreCalculSalaire o) {
        if (o != null) {
            OrdreCalculSalaire re = new OrdreCalculSalaire();
            re.setId(o.getId());
            re.setCloture((o.getCloture() != null) ? o.getCloture() : false);
            re.setDateExecution(o.getDateExecution());
            re.setDateJour(o.getDateJour());
            re.setDebutMois(o.getDebutMois());
            re.setFinMois(o.getFinMois());
            re.setHeureExecution(o.getHeureExecution());
            re.setRealise((o.getRealise() != null) ? o.getRealise() : false);
            re.setReference(o.getReference());
            re.setComptabilise(o.getComptabilise());
            re.setDateCloture(o.getDateCloture());
            re.setDebutTraitement(o.getDateDebutTraitement());
            re.setFinTraitement(o.getDateFinTraitement());
            if (o.getContrats() != null) {
                for (YvsGrhPlanifSalaireContrat ce : o.getContrats()) {
                    re.getListContrat().add(ce.getContrat());
                }
            }
            return re;
        } else {
            return null;
        }
    }

    public static List<ContratEmploye> buildContratPlanifie(List<YvsGrhPlanifSalaireContrat> lp) {
        List<ContratEmploye> re = new ArrayList<>();
        for (YvsGrhPlanifSalaireContrat p : lp) {
            re.add(buildBeanContratEmploye(p.getContrat()));
        }
        return re;
    }

    public static QuestionsEntretien buildQuestionnaire(YvsGrhParamQuestionnaire q) {
        QuestionsEntretien re = new QuestionsEntretien();
        if (q != null) {
            re.setId(q.getId());
            re.setQuestion(q.getQuestion());
            re.setNumeroQuestion(q.getNumeroQuestion());
            re.setTypeReponse(q.getTypeReponse());
            if (q.getRubrique() != null) {
                re.setRubrique(new RubriquesQuestionnaire(q.getRubrique().getId(), q.getRubrique().getCodeRubrique(), q.getRubrique().getLibelleRubrique()));
            }
            if (q.getReponses() != null) {
                String[] reps = q.getReponses().split(";");
                for (String s : reps) {
                    re.getReponses().add(s);
                }
            }
        }
        return re;
    }

    public static List<QuestionsEntretien> buildQuestionnaire(List<YvsGrhParamQuestionnaire> lq) {
        List<QuestionsEntretien> re = new ArrayList<>();
        for (YvsGrhParamQuestionnaire q : lq) {
            re.add(buildQuestionnaire(q));
        }
        return re;
    }

    public static RubriquesQuestionnaire buildRubriqueQuestionnaire(YvsGrhRubriquesQuestionnaire r) {
        RubriquesQuestionnaire re = new RubriquesQuestionnaire();
        if (r != null) {
            re.setCodeRubrique(r.getCodeRubrique());
            re.setId(r.getId());
            re.setLibelleRubrique(r.getLibelleRubrique());
            re.setQuestions(buildQuestionnaire(r.getYvsGrhParamQuestionnaireList()));
        }
        return re;
    }

    public static List<RubriquesQuestionnaire> buildRubriqueQuestionnaire(List<YvsGrhRubriquesQuestionnaire> lr) {
        List<RubriquesQuestionnaire> re = new ArrayList<>();
        for (YvsGrhRubriquesQuestionnaire q : lr) {
            re.add(buildRubriqueQuestionnaire(q));
        }
        return re;
    }

    public static List<Langues> buildLangues(List<YvsLangues> ll) {
        List<Langues> re = new ArrayList<>();
        for (YvsLangues l : ll) {
            re.add(buildLangue(l));
        }
        return re;
    }

    public static List<LangueCandidat> buildLanguesCandidat(List<YvsGrhLanguesCandidats> ll) {
        List<LangueCandidat> re = new ArrayList<>();
        for (YvsGrhLanguesCandidats l : ll) {
            re.add(buildLangue(l));
        }
        return re;
    }

    public static LangueCandidat buildLangue(YvsGrhLanguesCandidats l) {
        LangueCandidat la = new LangueCandidat();
        if (l != null) {
            la.setIdLangueCandidat(l.getId().intValue());
            la.setEcrit(l.getEcrit());
            la.setId(l.getLangue().getId());
            la.setLangue(l.getLangue().getNom());
            la.setLu(l.getLu());
            la.setParle(l.getParler());
            la.setSave(true);
        }
        return la;
    }

    public static YvsGrhLanguesCandidats buildLangue(LangueCandidat l) {
        YvsGrhLanguesCandidats la = new YvsGrhLanguesCandidats();
        if (l != null) {
            la.setEcrit(l.isEcrit());
            la.setLangue(new YvsLangues(l.getId()));
            la.setId(l.getIdLangueCandidat());
            la.setLu(l.isLu());
            la.setParler(l.isParle());
        }
        return la;
    }

    public static YvsGrhCandidats buildCandidat(Candidats c) {
        YvsGrhCandidats re = new YvsGrhCandidats();
        if (c != null) {
            re.setAPropos(c.getApropos());
            re.setBoitePostal(c.getCodePostal());
            re.setCivilite(c.getCivilite());
            re.setDateNaissance(c.getDateNaiss());
            re.setId(c.getId());
            re.setLieuNaissance((c.getLieuNaissance().getId() > 0) ? new YvsDictionnaire(c.getLieuNaissance().getId()) : null);
            re.setPaysOrigine((c.getPaysOrigine().getId() > 0) ? new YvsDictionnaire(c.getPaysOrigine().getId()) : null);
            re.setVilleHabitation((c.getVille().getId() > 0) ? new YvsDictionnaire(c.getVille().getId()) : null);
            re.setNameCandidat(c.getNom());
            re.setPrenom(c.getPrenom());
            re.setActif(c.isActif());
            if (c.getPoste().getId() > 0) {
                re.setPosteTravail(new YvsGrhPosteDeTravail(c.getPoste().getId()));
            }
            if (!c.getTelephones().contains(c.getTelephone()) && c.getTelephone() != null) {
                c.getTelephones().add(c.getTelephone());
            }
            if (!c.getTelephones().isEmpty()) {
                int i = 0;
                String ch = "";
                for (String r : c.getTelephones()) {
                    if (i < c.getTelephones().size()) {
                        ch = ch + (r + " ; ");
                    } else {
                        ch = ch + r;
                    }
                    i++;
                }
                re.setTelephones(ch);
            }
            if (!c.getEmails().contains(c.getEmail()) && c.getEmail() != null) {
                c.getEmails().add(c.getEmail());
            }
            if (!c.getEmails().isEmpty()) {
                int i = 0;
                String ch = "";
                for (String r : c.getEmails()) {
                    if (i < c.getEmails().size()) {
                        ch = ch + (r + " ; ");
                    } else {
                        ch = ch + r;
                    }
                    i++;
                }
                re.setAdresseMail(ch);
            }
        }
        return re;
    }

    public static Candidats buildCandidat(YvsGrhCandidats c) {
        Candidats re = new Candidats();
        if (c != null) {
            re.setApropos(c.getAPropos());
            re.setCodePostal(c.getBoitePostal());
            re.setCivilite(c.getCivilite());
            re.setDateNaiss(c.getDateNaissance());
            re.setPoste(builSimpledBeanPoste(c.getPosteTravail()));
            re.setId(c.getId());
            re.setCode(c.getCode());
            re.setAnciennete(c.getAnciennete());
            if (c.getLieuNaissance() != null) {
                re.setLieuNaissance(new Dictionnaire(c.getLieuNaissance().getId(), c.getLieuNaissance().getLibele()));
            }
            if (c.getPaysOrigine() != null) {
                re.setPaysOrigine(new Dictionnaire(c.getPaysOrigine().getId(), c.getPaysOrigine().getLibele()));
            }
            if (c.getVilleHabitation() != null) {
                re.setVille(new Dictionnaire(c.getVilleHabitation().getId(), c.getVilleHabitation().getLibele()));
            }
            re.setNom(c.getNameCandidat());
            re.setPrenom(c.getPrenom());
            if (c.getTelephones() != null) {
                String[] reps = c.getTelephones().trim().split(";");
                for (String s : reps) {
                    if (s.length() != 0) {
                        re.getTelephones().add(s);
                    }
                }
                if (!re.getTelephones().isEmpty()) {
                    re.setTelephone(re.getTelephones().get(0));
                }
            }
            if (c.getAdresseMail() != null) {
                String[] reps = c.getAdresseMail().trim().split(";");
                for (String s : reps) {
                    if (s.length() != 0) {
                        re.getEmails().add(s);
                    }
                }
                if (!re.getEmails().isEmpty()) {
                    re.setEmail(re.getEmails().get(0));
                }
            }
            //charge les langues
            if (!c.getLangues().isEmpty()) {
                re.setLangues(buildLanguesCandidat(c.getLangues()));
            }
            re.setActif(c.getActif());
            //charge les qualifications
            if (!c.getYvsGrhQualificationCandidatList().isEmpty()) {
                re.setCompetences(buildQualificationCandidatD(c.getYvsGrhQualificationCandidatList()));
            }
            //charge les formations
            if (!c.getYvsGrhDiplomesCandidatList().isEmpty()) {
                re.setFormations(buildFormationCandidatD(c.getYvsGrhDiplomesCandidatList()));
            }
        }
        return re;
    }

    public static List<Candidats> buildCandidat(List<YvsGrhCandidats> lc) {
        List<Candidats> re = new ArrayList<>();
        for (YvsGrhCandidats c : lc) {
            re.add(buildCandidat(c));
        }
        return re;
    }

    public static Diplomes buildDiplome(YvsDiplomes c) {
        Diplomes re = new Diplomes();
        if (c != null) {
            re.setCodeInterne(c.getCodeInterne());
            re.setCycle(c.getCycleDipome());
            re.setDesignation(c.getNom());
            re.setId(c.getId());
            re.setNiveau(c.getNiveau());
            re.setSerie(c.getSerie());
            re.setSpecialite(new SpecialiteDiplomes(c.getSpecialite().getId(), c.getSpecialite().getCodeInterne(), c.getSpecialite().getTitreSpecialite()));
        }
        return re;
    }

    public static DiplomeCandidat buildDiplomeCandidat(YvsDiplomes c) {
        DiplomeCandidat re = new DiplomeCandidat();
        if (c != null) {
            re.setDiplome(buildDiplome(c));
            re.setSpecialite(new SpecialitesDiplomeCandidat(c.getSpecialite().getId(), c.getSpecialite().getCodeInterne(), c.getSpecialite().getTitreSpecialite()));
            re.setActif(c.getActif());
        }
        return re;
    }

    public static List<Diplomes> buildDiplome(List<YvsDiplomes> ld) {
        List<Diplomes> result = new ArrayList<>();
        for (YvsDiplomes d : ld) {
            result.add(buildDiplome(d));
        }
        return result;
    }

    public static YvsDiplomes buildDiplome(Diplomes c) {
        YvsDiplomes re = new YvsDiplomes();
        if (c != null) {
            re.setCodeInterne(c.getCodeInterne());
            re.setCycleDipome(c.getCycle());
            re.setNom(c.getDesignation());
            re.setId(c.getId());
            re.setNiveau(c.getNiveau());
            re.setSerie(c.getSerie());
            if (c.getSpecialite().getId() > 0) {
                re.setSpecialite(new YvsSpecialiteDiplomes(c.getSpecialite().getId()));
            }

        }
        return re;
    }

//    public static YvsGrhQualifications buildQualificationCandidat(QualificationCandidat c) {
//        YvsGrhQualifications re = new YvsGrhQualifications();
//        if (c != null) {
//            re.setId(c.getId());
//            re.setCodeInterne(c.getQualification().getCodeInterne());
//            re.setDesignation(c.getQualification().getIntitule());
//            re.setDescription(c.getQualification().getDescription());
//            re.setActif(c.isActif());
//            if (c.getQualification().getDomaine() != null) {
//                if (c.getQualification().getDomaine().getId() > 0) {
//                    re.setDomaine(new YvsGrhDomainesQualifications(c.getQualification().getDomaine().getId()));
//                }
//            }
//        }
//        return re;
//    }
    public static YvsGrhQualificationCandidat buildQualificationCandidat(QualificationCandidat c) {
        YvsGrhQualificationCandidat entity = new YvsGrhQualificationCandidat();
        if (c != null) {
            entity.setNiveau(c.getNiveau());
            entity.setId(c.getId());
            entity.setQualification(new YvsGrhQualifications(c.getQualification().getId()));
        }
        return entity;
    }

    public static YvsGrhDiplomesCandidat buildFormationCandidat(DiplomeCandidat d) {
        YvsGrhDiplomesCandidat entity = new YvsGrhDiplomesCandidat();
        if (d != null) {
            entity.setDiplome(new YvsDiplomes(d.getDiplome().getId()));
            entity.setId(d.getIdDiplomeCandidat());
            entity.setEtablissement(d.getEcole());
            entity.setMention(d.getMention());
        }
        return entity;
    }

    public static QualificationCandidat buildQualificationCandidat(YvsGrhQualificationCandidat c) {
        QualificationCandidat bean = new QualificationCandidat();
        if (c != null) {
            bean.setNiveau(c.getNiveau());
            bean.setId(c.getId());
            bean.setQualification(buildBeanQualification(c.getQualification()));
        }
        return bean;
    }

    public static List<DomainesQualificationCandidat> buildQualificationCandidatD(List<YvsGrhQualificationCandidat> lc) {
        List<DomainesQualificationCandidat> re = new ArrayList<>();
        DomainesQualificationCandidat dom;
        for (YvsGrhQualificationCandidat qc : lc) {
            dom = new DomainesQualificationCandidat(qc.getQualification().getDomaine().getId(), qc.getQualification().getDomaine().getTitreDomaine());
            if (!re.contains(dom)) {
                dom.getQualifications().add(buildQualificationCandidat(qc));
                re.add(dom);
            } else {
                re.get(re.indexOf(dom)).getQualifications().add(buildQualificationCandidat(qc));
            }
        }
        return re;
    }

    public static List<SpecialitesDiplomeCandidat> buildFormationCandidatD(List<YvsGrhDiplomesCandidat> lc) {
        List<SpecialitesDiplomeCandidat> re = new ArrayList<>();
        SpecialitesDiplomeCandidat dom;
        DiplomeCandidat dp;
        for (YvsGrhDiplomesCandidat qc : lc) {
            dom = new SpecialitesDiplomeCandidat(qc.getDiplome().getSpecialite().getId(), qc.getDiplome().getSpecialite().getTitreSpecialite());
            dp = buildDiplomeCandidat(qc.getDiplome());
            dp.setEcole(qc.getEtablissement());
            dp.setMention(qc.getMention());
            dp.setDateObtention(qc.getDateObtention());
            dp.setIdDiplomeCandidat(qc.getId());
            if (!re.contains(dom)) {
                dom.getDiplomes().add(dp);
                re.add(dom);
            } else {
                re.get(re.indexOf(dom)).getDiplomes().add(dp);
            }
        }
        return re;
    }

    public static EntretienCandidat buildEntretient(YvsGrhEntretienCandidat e) {
        EntretienCandidat re = new EntretienCandidat();
        if (e != null) {
            re.setId(e.getId());
            re.setCandidat(buildCandidat(e.getCandidat()));
            re.setDate(e.getDateEntretien());
            re.setExaminateur(buildBeanPartialEmploye(e.getExaminateur()));
            re.setLieu(e.getLieu());
        }
        return re;
    }

    public static List<EntretienCandidat> buildEntretient(List<YvsGrhEntretienCandidat> le) {
        List<EntretienCandidat> re = new ArrayList<>();
        for (YvsGrhEntretienCandidat e : le) {
            re.add(buildEntretient(e));
        }
        return re;
    }

    public static SpecialiteDiplomes buildSpecialiteDiplome(YvsSpecialiteDiplomes e) {
        SpecialiteDiplomes re = new SpecialiteDiplomes();
        if (e != null) {
            re.setCodeInterne(e.getCodeInterne());
            re.setDesignation(e.getTitreSpecialite());
            re.setId(e.getId());
            re.setDiplomes(buildDiplome(e.getDiplomes()));
        }
        return re;
    }

    public static List<SpecialiteDiplomes> buildSpecialiteDiplome(List<YvsSpecialiteDiplomes> le) {
        List<SpecialiteDiplomes> re = new ArrayList<>();
        if (le != null) {
            for (YvsSpecialiteDiplomes sp : le) {
                re.add(buildSpecialiteDiplome(sp));
            }
        }
        return re;
    }

    public static PlanningWork buildplanning(YvsGrhPlanningEmploye pl) {
        PlanningWork re = new PlanningWork();
        if (pl != null) {
            re.setActif(pl.getActif());
            re.setEmploye(buildBeanSimplePartialEmploye(pl.getEmploye()));
            re.setId(pl.getId());
            re.setJour(pl.getDateDebut());
            re.setDateFin(pl.getDateFin());
            re.setTranche(buildTrancheHoraire(pl.getTranche()));
        }
        return re;
    }

    public static List<PlanningWork> buildplanning(List<YvsGrhPlanningEmploye> le) {
        List<PlanningWork> re = new ArrayList<>();
        if (le != null) {
            for (YvsGrhPlanningEmploye sp : le) {
                re.add(buildplanning(sp));
            }
        }
        return re;
    }

    public static YvsGrhTypeCout buildTypeCout(TypeCout y, YvsSocietes currentScte, YvsUsersAgence currentUser) {
        YvsGrhTypeCout m = new YvsGrhTypeCout();
        if (y != null) {
            m.setId(y.getId());
            m.setLibelle(y.getLibelle());
            m.setModuleCout(y.getModule());
            if (y.getCompte() != null ? y.getCompte().getId() > 0 : false) {
                m.setCompte(new YvsBasePlanComptable(y.getCompte().getId(), y.getCompte().getNumCompte(), y.getCompte().getIntitule()));
            }
            m.setAugmentation(y.isAugmentation());
            m.setSociete(currentScte);
            m.setAuthor(currentUser);
            m.setDateSave(y.getDateSave());
            m.setDateUpdate(new Date());
            m.setNew_(true);
        }
        return m;
    }

    public static TypeCout buildBeanTypeCout(YvsGrhTypeCout y) {
        TypeCout m = new TypeCout();
        if (y != null) {
            m.setId(y.getId());
            m.setLibelle(y.getLibelle());
            m.setModule(y.getModuleCout());
            m.setMontant(y.getMontant());
            m.setDateSave(y.getDateSave());
            m.setAugmentation((y.getAugmentation() != null) ? y.getAugmentation() : false);
            m.setCompte(UtilCompta.buildBeanCompte(y.getCompte()));
            m.setUpdate(true);
        }
        return m;
    }

    public static List<TypeCout> buildBeanListTypeCout(List<YvsGrhTypeCout> list) {
        List<TypeCout> r = new ArrayList<>();
        if (list != null) {
            for (YvsGrhTypeCout a : list) {
                r.add(buildBeanTypeCout(a));
            }
        }
        return r;
    }

    public static ObjetMissionAnalytique buildBeanObjetMissionAnalytique(YvsGrhObjetsMissionAnalytique y) {
        ObjetMissionAnalytique r = new ObjetMissionAnalytique();
        if (y != null) {
            r.setId(y.getId());
            r.setTaux(y.getCoefficient());
            if (y.getObjetMission() != null) {
                r.setObjetMission(new ObjetMission(y.getObjetMission().getId(), y.getObjetMission().getTitre()));
            }
            r.setCentre(UtilCompta.buildBeanCentreAnalytique(y.getCentre()));
            r.setDateSave(y.getDateSave());
        }
        return r;
    }

    public static YvsGrhObjetsMissionAnalytique buildObjetMissionAnalytique(ObjetMissionAnalytique y, YvsUsersAgence ua) {
        YvsGrhObjetsMissionAnalytique r = new YvsGrhObjetsMissionAnalytique();
        if (y != null) {
            r.setId(y.getId());
            r.setCoefficient(y.getTaux());
            if (y.getObjetMission() != null ? y.getObjetMission().getId() > 0 : false) {
                r.setObjetMission(new YvsGrhObjetsMission(y.getObjetMission().getId(), y.getObjetMission().getTitre()));
            }
            if (y.getCentre() != null ? y.getCentre().getId() > 0 : false) {
                r.setCentre(new YvsComptaCentreAnalytique(y.getCentre().getId(), y.getCentre().getCodeRef(), y.getCentre().getIntitule()));
            }
            r.setAuthor(ua);
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setNew_(true);
        }
        return r;
    }

    public static YvsGrhObjetsMission buildBeanObjetMission(ObjetMission ob, YvsUsersAgence ua) {
        YvsGrhObjetsMission o = new YvsGrhObjetsMission();
        if (ob.getCompteCharge() != null ? ob.getCompteCharge().getId() > 0 : false) {
            o.setCompteCharge(UtilCompta.buildEntityCompte(ob.getCompteCharge()));
        }
        o.setDescription(ob.getDescription());
        o.setId(ob.getId());
        o.setTitre(ob.getTitre());
        o.setActif(ob.isActif());
        o.setDateSave(ob.getDateSave());
        o.setDateUpdate(new Date());
        o.setAuthor(ua);
        return o;
    }

    public static ObjetMission buildBeanObjetMission(YvsGrhObjetsMission ob) {
        ObjetMission o = buildSimpleBeanObjetMission(ob);
        if (ob != null) {
            o.setCompteCharge(UtilCompta.buildSimpleBeanCompte(ob.getCompteCharge()));
            o.setAnalytiques(ob.getAnalytiques());
        }
        return o;
    }

    public static ObjetMission buildSimpleBeanObjetMission(YvsGrhObjetsMission ob) {
        ObjetMission o = new ObjetMission();
        if (ob != null) {
            o.setDescription(ob.getDescription());
            o.setId(ob.getId());
            o.setTitre(ob.getTitre());
            o.setActif(ob.getActif());
            o.setDateSave(ob.getDateSave());
        }
        return o;
    }

    public static ConventionEmploye buildBeanConventionEmps(YvsGrhConventionEmploye c) {
        ConventionEmploye con = new ConventionEmploye();
        if (c != null) {
            con.setId(c.getId());
            con.setSupp(c.getSupp());
            con.setDateChange(c.getDateChange());
            con.setActif(c.getActif());
            con.setConvention(buildBeanConventionCollective(c.getConvention()));
            con.setEmploye(buildBeanSimplePartialEmploye(c.getEmploye()));
        }
        return con;
    }

    public static YvsGrhConventionEmploye buildConventionEmps(ConventionEmploye c, YvsUsersAgence ua) {
        YvsGrhConventionEmploye con = new YvsGrhConventionEmploye();
        if (c != null) {
            con.setId(c.getId());
            con.setSupp(c.isSupp());
            con.setDateChange(c.getDateChange());
            con.setActif(c.isActif());
            if (c.getConvention() != null ? c.getConvention().getId() > 0 : false) {
                con.setConvention(new YvsGrhConventionCollective(c.getConvention().getId(),
                        new YvsGrhEchelons(c.getConvention().getEchelon().getId(), c.getConvention().getEchelon().getEchelon()),
                        new YvsGrhCategorieProfessionelle(c.getConvention().getCategorie().getId(), c.getConvention().getCategorie().getCategorie())));
            }
            if (c.getEmploye() != null ? c.getEmploye().getId() > 0 : false) {
                con.setEmploye(new YvsGrhEmployes(c.getEmploye().getId(), c.getEmploye().getNom(), c.getEmploye().getPrenom()));
            }
            con.setAuthor(ua);
            con.setNew_(true);
        }
        return con;
    }

    public static int getOrdre(String jour) {
        switch (jour) {
            case Constantes.LUNDI:
                return 1;
            case Constantes.MARDI:
                return 2;
            case Constantes.MERCREDI:
                return 3;
            case Constantes.JEUDI:
                return 4;
            case Constantes.VENDREDI:
                return 5;
            case Constantes.SAMEDI:
                return 6;
            case Constantes.DIMANCHE:
                return 7;
        }
        return 0;
    }
}

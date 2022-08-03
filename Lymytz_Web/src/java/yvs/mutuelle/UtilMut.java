/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.mutuelle.YvsMutParametre;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.base.YvsMutGrilleTauxTypeCredit;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.mutuelle.base.YvsMutPoste;
import yvs.entity.mutuelle.base.YvsMutPosteEmploye;
import yvs.entity.mutuelle.base.YvsMutPrimePoste;
import yvs.entity.mutuelle.base.YvsMutTiers;
import yvs.entity.mutuelle.base.YvsMutTypeCompte;
import yvs.entity.mutuelle.base.YvsMutTypePrime;
import yvs.entity.mutuelle.credit.YvsMutAvaliseCredit;
import yvs.entity.mutuelle.credit.YvsMutConditionCredit;
import yvs.entity.mutuelle.credit.YvsMutCredit;
import yvs.entity.mutuelle.credit.YvsMutFraisTypeCredit;
import yvs.entity.mutuelle.credit.YvsMutReglementCredit;
import yvs.entity.mutuelle.credit.YvsMutTypeCredit;
import yvs.entity.mutuelle.credit.YvsMutVoteValidationCredit;
import yvs.entity.mutuelle.echellonage.YvsMutEchellonage;
import yvs.entity.mutuelle.echellonage.YvsMutMensualite;
import yvs.entity.mutuelle.echellonage.YvsMutReglementMensualite;
import yvs.entity.mutuelle.evenement.YvsMutActivite;
import yvs.entity.mutuelle.evenement.YvsMutActiviteType;
import yvs.entity.mutuelle.evenement.YvsMutContributionEvenement;
import yvs.entity.mutuelle.evenement.YvsMutCoutEvenement;
import yvs.entity.mutuelle.evenement.YvsMutEvenement;
import yvs.entity.mutuelle.evenement.YvsMutFinancementActivite;
import yvs.entity.mutuelle.evenement.YvsMutParticipantEvenement;
import yvs.entity.mutuelle.evenement.YvsMutTauxContribution;
import yvs.entity.mutuelle.evenement.YvsMutTypeEvenement;
import yvs.entity.mutuelle.operation.YvsMutMouvementCaisse;
import yvs.entity.mutuelle.operation.YvsMutOperationCompte;
import yvs.entity.mutuelle.salaire.YvsMutInteret;
import yvs.entity.mutuelle.salaire.YvsMutReglementPrime;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import yvs.grh.bean.TypeCout;
import yvs.mutuelle.base.GrilleTaux;
import yvs.mutuelle.base.Poste;
import yvs.mutuelle.base.PrimePoste;
import yvs.mutuelle.base.Tiers;
import yvs.mutuelle.base.TypeCompte;
import yvs.mutuelle.base.TypePrime;
import yvs.mutuelle.credit.Avalise;
import yvs.mutuelle.credit.Credit;
import yvs.mutuelle.credit.FraisTypeCredit;
import yvs.mutuelle.credit.ReglementCreditMut;
import yvs.mutuelle.credit.TypeCredit;
import yvs.mutuelle.credit.VoteCredit;
import yvs.mutuelle.echellonage.Echellonage;
import yvs.mutuelle.echellonage.Mensualite;
import yvs.mutuelle.echellonage.ReglementMensualite;
import yvs.mutuelle.evenement.Activite;
import yvs.mutuelle.evenement.Contribution;
import yvs.mutuelle.evenement.ContributionEvenement;
import yvs.mutuelle.evenement.CoutEvenement;
import yvs.mutuelle.evenement.Evenement;
import yvs.mutuelle.evenement.FinancementActivite;
import yvs.mutuelle.evenement.ParticipantEvenement;
import yvs.mutuelle.evenement.TypeContribution;
import yvs.mutuelle.evenement.TypeEvenement;
import yvs.mutuelle.operation.OperationCaisse;
import yvs.mutuelle.operation.OperationCompte;
import yvs.mutuelle.salaire.Interet;
import yvs.mutuelle.salaire.OrdreCalculSalaire;
import yvs.mutuelle.salaire.ReglementPrime;
import yvs.mutuelle.salaire.TypeAvance;
import yvs.parametrage.dico.Dictionnaire;
import yvs.util.Constantes;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
public class UtilMut {

    public static Employe buildBeanEmploye(YvsGrhEmployes y) {
        return UtilGrh.buildBeanSimplePartialEmploye(y);
    }

    /**
     *
     * DEBUT GESTION POSTE
     *
     * @param y
     * @return
     */
    public static TypePrime buildBeanTypePrime(YvsMutTypePrime y) {
        TypePrime t = new TypePrime();
        if (y != null) {
            t.setDesignation(y.getDesignation());
            t.setId(y.getId());
            t.setMontantMaximal((y.getMontantMaximal() != null) ? y.getMontantMaximal() : 0);
            t.setNatureMontant(y.getNatureMontant());
            t.setMutuelle((y.getMutuelle() != null) ? buildBeanMutuelle(y.getMutuelle()) : new Mutuelle());
            t.setPeriodeRemuneration(y.getPeriodeRemuneration());
            t.setDateSave(y.getDateSave());
        }
        return t;
    }

    public static List<TypePrime> buildBeanListTypePrime(List<YvsMutTypePrime> y) {
        List<TypePrime> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutTypePrime c : y) {
                l.add(buildBeanTypePrime(c));
            }
        }
        return l;
    }

    public static PrimePoste buildBeanPrimePoste(YvsMutPrimePoste y) {
        PrimePoste p = new PrimePoste();
        if (y != null) {
            p.setId(y.getId());
            p.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            p.setType((y.getType() != null) ? buildBeanTypePrime(y.getType()) : new TypePrime());
            p.setPoste(buildBeanPoste(y.getPoste()));
            p.setDateSave(y.getDateSave());
        }
        return p;
    }

    public static List<PrimePoste> buildBeanListPrimePoste(List<YvsMutPrimePoste> y) {
        List<PrimePoste> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutPrimePoste c : y) {
                l.add(buildBeanPrimePoste(c));
            }
        }
        return l;
    }

    public static Poste buildBeanPoste(YvsMutPoste y) {
        Poste p = new Poste();
        if (y != null) {
            p.setDesignation(y.getDesignation());
            p.setId(y.getId());
            p.setDescription(y.getDescription());
            p.setMutuelle((y.getMutuelle() != null) ? buildBeanMutuelle(y.getMutuelle()) : new Mutuelle());
            p.setPrimes(y.getPrimes());
            p.setMontantPrime(y.getMontantPrime());
            p.setCanVoteCredit(y.getCanVoteCredit());
            p.setDateSave(y.getDateSave());
        }
        return p;
    }

    public static List<Poste> buildBeanListPoste(List<YvsMutPoste> y) {
        List<Poste> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutPoste c : y) {
                l.add(buildBeanPoste(c));
            }
        }
        return l;
    }

    /**
     *
     * FIN GESTION POSTE
     *
     */
    /**
     *
     * DEBUT GESTION PARAMETRAGE
     *
     * @param y
     * @return
     */
    public static CaisseMutuelle buildBeanCaisse(YvsMutCaisse y) {
        CaisseMutuelle c = new CaisseMutuelle();
        if (y != null) {
            c.setId(y.getId());
            c.setSolde((y.getSolde() != null) ? y.getSolde() : 0);
            c.setReference(y.getReferenceCaisse());
            c.setPrincipale(y.getPrincipal());
            c.setActif(y.getActif());
            c.setDateSave(y.getDateSave());
            if ((y.getMutuelle() != null) ? y.getMutuelle().getId() != 0 : false) {
                String name = y.getMutuelle().getDesignation();
                c.setProprietaire(name);
            }
            if (y.getResponsable() != null) {
                c.setResponsable(UtilMut.buildSimpleBeanMutualiste(y.getResponsable()));
            }
        }
        return c;
    }

    public static YvsMutCaisse buildBeanCaisse(CaisseMutuelle y) {
        YvsMutCaisse c = new YvsMutCaisse();
        if (y != null) {
            c.setId(y.getId());
            c.setActif(y.isActif());
            c.setPrincipal(y.isPrincipale());
            c.setReferenceCaisse(y.getReference());
        }
        return c;
    }

    public static List<CaisseMutuelle> buildBeanListCaisse(List<YvsMutCaisse> y) {
        List<CaisseMutuelle> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutCaisse c : y) {
                l.add(buildBeanCaisse(c));
            }
        }
        return l;
    }

//    public static TypeCaisse buildBeanTypeCaisse(YvsMutTypeCompte y) {
//        TypeCaisse t = new TypeCaisse();
//        if (y != null) {
//            t.setId(y.getId());
//            t.setLibelle(y.getLibelle());
//            t.setNature(y.getNature());
//            t.setTypeCaisse((y.getTypeCaisse() != null) ? y.getTypeCaisse() : false);
//            t.setMutuelle((y.getMutuelle() != null) ? buildBeanMutuelle(y.getMutuelle()) : new Mutuelle());
//        }
//        return t;
//    }
//    public static List<TypeCaisse> buildBeanListTypeCaisse(List<YvsMutTypeCompte> y) {
//        List<TypeCaisse> l = new ArrayList<>();
//        if (y != null) {
//            for (YvsMutTypeCompte c : y) {
//                l.add(buildBeanTypeCaisse(c));
//            }
//        }
//        return l;
//    }
    public static Parametre buildBeanParametre(YvsMutParametre y) {
        Parametre p = new Parametre();
        if (y != null) {
            p.setId(y.getId());
            p.setDureeMembre((y.getDureeMembre() != null) ? y.getDureeMembre() : 0);
            p.setQuotiteCessible((y.getQuotiteCessible() != null) ? y.getQuotiteCessible() : 0);
            p.setPeriodeSalaireMoyen((y.getPeriodeSalaireMoyen() != null) ? y.getPeriodeSalaireMoyen() : 0);
            p.setSouscriptionGeneral((y.getSouscriptionGeneral() != null) ? y.getSouscriptionGeneral() : false);
            p.setRetainsEpargne((y.getRetainsEpargne()) ? y.getRetainsEpargne() : false);
            p.setMonnaie(y.getMonnaie());
            p.setValidCreditByVote(y.getValidCreditByVote());
            p.setDureeEtudeCredit(y.getDureeEtudeCredit());
            p.setTauxVoteValidCreditCorrect(y.getTauxVoteValidCreditCorrect());
            p.setTauxVoteValidCreditIncorrect(y.getTauxVoteValidCreditIncorrect());
            p.setTauxCouvertureCredit(y.getTauxCouvertureCredit());
            p.setPaiementParCompteStrict(y.getPaiementParCompteStrict());
            p.setAcceptRetraitEpargne(y.getAcceptRetraitEpargne());
            p.setDebutEpargne(y.getDebutEpargne());
            p.setFinEpargne(y.getFinEpargne());
            p.setRetardSaisieEpargne(y.getRetardSaisieEpargne());
            p.setCreditRetainsInteret(y.getCreditRetainsInteret());
            p.setCapaciteEndettement(y.getCapaciteEndettement());
            p.setBaseCapaciteEndettement(y.getBaseCapaciteEndettement());
            p.setDateSave(y.getDateSave());
        }
        return p;
    }

    public static List<Parametre> buildBeanListParametre(List<YvsMutParametre> y) {
        List<Parametre> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutParametre c : y) {
                l.add(buildBeanParametre(c));
            }
        }
        return l;
    }

    public static Mutuelle buildBeanMutuelle(YvsMutMutuelle y) {
        Mutuelle m = new Mutuelle();
        if (y != null) {
            m.setDateCreation((y.getDateCreation() != null) ? y.getDateCreation() : new Date());
            m.setDesignation(y.getDesignation());
            m.setCode((y.getCode() != null) ? y.getCode() : "");
            m.setLogo(y.getLogo());
            m.setId(y.getId());
            m.setMontantEpargne((y.getMontantEpargne() != null) ? y.getMontantEpargne() : 0);
            m.setMontantInscription((y.getMontantInscription() != null) ? y.getMontantInscription() : 0);
            m.setMontantAssurance(y.getMontantAssurance());
            m.setAgences(y.getAgences());
            m.setCaisses(y.getCaisses());
            m.setParametres(y.getParamsMutuelle());
            m.setDateSave(y.getDateSave());
        }
        return m;
    }

    public static List<Mutuelle> buildBeanListMutuelle(List<YvsMutMutuelle> y) {
        List<Mutuelle> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutMutuelle c : y) {
                l.add(buildBeanMutuelle(c));
            }
        }
        return l;
    }

    public static TypeCompte buildBeanTypeCompte(YvsMutTypeCompte y) {
        TypeCompte t = new TypeCompte();
        if (y != null) {
            t.setId(y.getId());
            t.setLibelle(y.getLibelle());
            t.setNature(y.getNature());
            t.setTypeCaisse((y.getTypeCaisse() != null) ? y.getTypeCaisse() : false);
            t.setMutuelle((y.getMutuelle() != null) ? buildBeanMutuelle(y.getMutuelle()) : new Mutuelle());
            t.setDateSave(y.getDateSave());
        }
        return t;
    }

    public static List<TypeCompte> buildBeanListTypeCompte(List<YvsMutTypeCompte> y) {
        List<TypeCompte> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutTypeCompte c : y) {
                l.add(buildBeanTypeCompte(c));
            }
        }
        return l;
    }

    public static Tiers buildBeanTiers(YvsMutTiers y) {
        Tiers t = new Tiers();
        if (y != null) {
            t.setAdresse(y.getAdresse());
            t.setId(y.getId());
            t.setNom(y.getNom());
            t.setPrenom(y.getPrenom());
            t.setRaisonSociale(y.getRaisonSociale());
            t.setTelephone(y.getTelephone());
            t.setTypeTiers(y.getTypeTiers());
            t.setMutuelle((y.getMutuelle() != null) ? buildBeanMutuelle(y.getMutuelle()) : new Mutuelle());
            t.setIdExterne((y.getIdExterne() != null) ? y.getIdExterne() : 0);
        }
        return t;
    }

    public static List<Tiers> buildBeanListTiers(List<YvsMutTiers> y) {
        List<Tiers> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutTiers c : y) {
                l.add(buildBeanTiers(c));
            }
        }
        return l;
    }

    /**
     *
     * FIN GESTION PARAMETRAGE
     *
     */
    /**
     *
     * DEBUT GESTION MUTUALISTE
     *
     *
     * @param y
     * @return
     */
    public static PosteEmploye buildBeanPosteEmploye(YvsMutPosteEmploye y) {
        PosteEmploye p = new PosteEmploye();
        if (y != null) {
            p.setActif((y.getActif() != null) ? y.getActif() : false);
            p.setDateOccupation((y.getDateOccupation() != null) ? y.getDateOccupation() : new Date());
            p.setId(y.getId());
            p.setPoste((y.getPoste() != null) ? buildBeanPoste(y.getPoste()) : new Poste());
        }
        return p;
    }

    public static List<PosteEmploye> buildBeanListPosteEmploye(List<YvsMutPosteEmploye> y) {
        List<PosteEmploye> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutPosteEmploye c : y) {
                l.add(buildBeanPosteEmploye(c));
            }
        }
        return l;
    }

    public static Compte buildBeanCompte(YvsMutCompte y) {
        Compte c = new Compte();
        if (y != null) {
            c.setId(y.getId());
            c.setReference(y.getReference());
            c.setSolde((y.getSolde() != null) ? y.getSolde() : 0);
            c.setType((y.getTypeCompte() != null) ? buildBeanTypeCompte(y.getTypeCompte()) : new TypeCompte());
            c.setMutualiste(buildSimpleBeanMutualiste(y.getMutualiste()));
            c.setProprietaire(y.getProprietaire());
            c.setDateSave(y.getDateSave());
            c.setPrimes(y.getPrime());
            c.setSalaire(y.getSalaire());
            c.setInteret(y.getInteret());
            c.setActif(y.getActif());
        }
        return c;
    }

    public static List<Compte> buildBeanListCompte(List<YvsMutCompte> y) {
        List<Compte> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutCompte c : y) {
                l.add(buildBeanCompte(c));
            }
        }
        return l;
    }

    public static Mutualiste buildBeanMutualiste(YvsMutMutualiste y) {
        Mutualiste m = buildSimpleBeanMutualiste(y);
        if (y != null) {
            m.setAssistance((y.getAssistance() != null) ? y.getAssistance() : false);
            //calcul le montant total d'epargne
            for (YvsMutCompte c : y.getComptes()) {
                if (c.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_EPARGNE)) {
                    m.setMontantTotalEpargne(m.getMontantTotalEpargne() + c.getSolde());
                }
                if (c.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_COURANT)) {
                    m.setMontantCredit(m.getMontantCredit() + c.getSolde());
                }
                if (c.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_ASSURANCE)) {
                    m.setSoldeAssurance(m.getMontantCredit() + c.getSolde());
                }
            }
            //calcul le montant total des avalises
            m.setCouvertureAvalise(y.getCouvertureAvalise());
            m.setMatricule((y.getEmploye() != null) ? y.getEmploye().getMatricule() : "");
            if (y.getEmploye().getContrat() != null) {
                m.setMontantSalaire(y.getEmploye().getContrat().getSalaireMensuel());
            }
        }
        return m;
    }

    public static Mutualiste buildSimpleBeanMutualiste(YvsMutMutualiste y) {
        Mutualiste m = new Mutualiste();
        if (y != null) {
            m.setId(y.getId());
            m.setActif((y.getActif() != null) ? y.getActif() : false);
            m.setMontantEpargne(y.getMontantEpargne());
            m.setDateAdhesion((y.getDateAdhesion() != null) ? y.getDateAdhesion() : new Date());
            m.setEmploye((y.getEmploye() != null) ? buildBeanEmploye(y.getEmploye()) : new Employe());
            m.setMatricule(y.getEmploye() != null ? y.getEmploye().getMatricule() : "");
            m.setMutuelle((y.getMutuelle() != null) ? buildBeanMutuelle(y.getMutuelle()) : new Mutuelle());
            m.setPostes(y.getPostes());
            m.setPosteEmploye(new PosteEmploye());
            for (YvsMutPosteEmploye p : m.getPostes()) {
                if (p.getActif()) {
                    m.setPosteEmploye(buildBeanPosteEmploye(p));
                    break;
                }
            }
            m.setComptes(y.getComptes());
            m.setMontantCredit(0);
            m.setMontantTotalEpargne(0);
            m.setSoldeAssurance(0);
            m.setDateSave(y.getDateSave());
        }
        return m;
    }

    public static YvsMutMutualiste buildMutualiste(Mutualiste y, YvsUsersAgence ua) {
        YvsMutMutualiste m = null;
        if (y != null) {
            m = new YvsMutMutualiste();
            m.setId(y.getId());
            m.setActif(y.isActif());
            m.setMontantEpargne(y.getMontantEpargne());
            m.setDateAdhesion(y.getDateAdhesion());
            m.setMontantTotalEpargne(0);
            m.setPostes(y.getPostes());
            m.setDateSave(y.getDateSave());
            m.setDateUpdate(new Date());
            m.setEmploye((y.getEmploye().getId() > 0) ? UtilGrh.buildEmployeEntity(y.getEmploye()) : null);
            m.setMutuelle((y.getMutuelle().getId() > 0) ? new YvsMutMutuelle(y.getMutuelle().getId()) : null);
        }
        return m;
    }

    public static List<Mutualiste> buildBeanListMutualiste(List<YvsMutMutualiste> y) {
        List<Mutualiste> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutMutualiste c : y) {
                l.add(buildBeanMutualiste(c));
            }
        }
        return l;
    }

    /**
     *
     * FIN GESTION MUTUALISTE
     *
     *
     */
    /**
     * DEBUT GESTION OPERATION
     *
     * @param y
     * @return
     */
    public static OperationCaisse buildBeanOperationCaisse(YvsMutMouvementCaisse y) {
        OperationCaisse o = new OperationCaisse();
        if (y != null) {
            o.setDateOperation((y.getDateMvt() != null) ? y.getDateMvt() : new Date());
            o.setId(y.getId());
            o.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            o.setSensOperation(y.getMouvement());
            o.setCaisse((y.getCaisse() != null) ? buildBeanCaisse(y.getCaisse()) : new CaisseMutuelle());
            o.setCommentaire(y.getNote());
            o.setDateSave(y.getDateSave());
            o.setIdExterne(y.getIdExterne());
            o.setTableExterne(y.getTableExterne());
            o.setInSoldeMutuelle(y.getInSoldeCaisse());
            if (y.getTiersExterne() != null) {
                o.setSource((y.getTiersExterne() != null) ? buildBeanTiers(y.getTiersExterne()) : new Tiers());
            }
        }
        return o;
    }

    public static List<OperationCaisse> buildBeanListOperationCaisse(List<YvsMutMouvementCaisse> y) {
        List<OperationCaisse> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutMouvementCaisse c : y) {
                l.add(buildBeanOperationCaisse(c));
            }
        }
        return l;
    }

    public static OperationCompte buildBeanOperationCompte(YvsMutOperationCompte y) {
        OperationCompte o = new OperationCompte();
        if (y != null) {
            o.setDateOperation((y.getDateOperation() != null) ? y.getDateOperation() : new Date());
            o.setId(y.getId());
            o.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            o.setSensOperation(y.getSensOperation());
            o.setCompte((y.getCompte() != null) ? buildBeanCompte(y.getCompte()) : new Compte());
            o.setCommentaire(y.getCommentaire());
            o.setHeureOperation((y.getHeureOperation() != null) ? y.getHeureOperation() : new Date());
            o.setAutomatique((y.getAutomatique() != null) ? y.getAutomatique() : false);
            o.setReferenceOperation(y.getReferenceOperation());
            o.setPeriode(UtilMut.buildPeriodeMutuelle(y.getPeriode()));
            o.setDateSave(y.getDateSave());
            o.setNature(y.getNature());
        }
        return o;
    }

    public static List<OperationCompte> buildBeanListOperationCompte(List<YvsMutOperationCompte> y) {
        List<OperationCompte> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutOperationCompte c : y) {
                l.add(buildBeanOperationCompte(c));
            }
        }
        return l;
    }

    /**
     *
     * FIN GESTION OPERATION
     *
     *
     */
    /**
     *
     * DEBUT GESTION CREDIT
     *
     *
     * @param y
     * @return
     */
    public static Avalise buildBeanAvalise(YvsMutAvaliseCredit y) {
        Avalise a = new Avalise();
        if (y != null) {
            a.setId(y.getId());
            a.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            a.setMutualiste((y.getMutualiste() != null) ? buildBeanMutualiste(y.getMutualiste()) : new Mutualiste());
        }
        return a;
    }

    public static List<Avalise> buildBeanListAvalise(List<YvsMutAvaliseCredit> y) {
        List<Avalise> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutAvaliseCredit c : y) {
                l.add(buildBeanAvalise(c));
            }
        }
        return l;
    }

    public static GrilleTaux buildBeanGrilleTaux(YvsMutGrilleTauxTypeCredit y) {
        GrilleTaux g = new GrilleTaux();
        if (y != null) {
            g.setId(y.getId() != null ? y.getId() : 0);
            g.setMontantMinimal(y.getMontantMinimal());
            g.setMontantMaximal(y.getMontantMaximal());
            g.setPeriodeMaximal(y.getPeriodeMaximal());
            g.setTaux(y.getTaux());
        }
        return g;
    }

    public static List<GrilleTaux> buildBeanListGrilleTaux(List<YvsMutGrilleTauxTypeCredit> y) {
        List<GrilleTaux> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutGrilleTauxTypeCredit c : y) {
                l.add(buildBeanGrilleTaux(c));
            }
        }
        return l;
    }

    public static FraisTypeCredit buildBeanFraisTypeCredit(YvsMutFraisTypeCredit y) {
        FraisTypeCredit r = new FraisTypeCredit();
        if (y != null) {
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setCredit(buildBeanTypeCredit(y.getCredit()));
            r.setType(UtilGrh.buildBeanTypeCout(y.getType()));
        }
        return r;
    }

    public static YvsMutFraisTypeCredit buildFraisTypeCredit(FraisTypeCredit y, YvsUsersAgence ua) {
        YvsMutFraisTypeCredit r = new YvsMutFraisTypeCredit();
        if (y != null) {
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            if (y.getCredit() != null ? y.getCredit().getId() > 0 : false) {
                r.setCredit(new YvsMutTypeCredit(y.getCredit().getId()));
            }
            if (y.getType() != null ? y.getType().getId() > 0 : false) {
                r.setType(new YvsGrhTypeCout(y.getType().getId(), y.getType().getLibelle()));
            }
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static TypeAvance buildBeanTypeAvance(YvsMutTypeCredit y) {
        TypeAvance t = new TypeAvance();
        if (y != null) {
            t.setCode(y.getCode());
            t.setDesignation(y.getDesignation());
            t.setId(y.getId());
            t.setTypeAvance((y.getTypeAvance() != null) ? y.getTypeAvance() : false);
            t.setMontantMaximal((y.getMontantMaximal() != null) ? y.getMontantMaximal() : 0);
            t.setPeriodeMaximal((y.getPeriodeMaximal() != null) ? y.getPeriodeMaximal() : 0);
            t.setTauxMaximal((y.getTauxMaximal() != null) ? y.getTauxMaximal() : 0);
            t.setImpayeDette((y.getImpayeDette() != null) ? y.getImpayeDette() : false);
            t.setMutuelle((y.getMutuelle() != null) ? buildBeanMutuelle(y.getMutuelle()) : new Mutuelle());
            t.setJourDebutAvance((y.getJourDebutAvance() != null) ? y.getJourDebutAvance() : 0);
            t.setJourFinAvance((y.getJourFinAvance() != null) ? y.getJourFinAvance() : 0);
            t.setGrilles(y.getGrilles());
            t.setFrais(y.getFrais());
            t.setNatureMontant(y.getNatureMontant());
            t.setSuffixeMontant(y.getSuffixeMontant());
            t.setNbreAvalise(y.getNbreAvalise());
            t.setPeriodicite(y.getPeriodicite());
            t.setTypeMensualite(y.getTypeMensualite());
            t.setFormuleInteret(y.getFormuleInteret());
            t.setModelRemboursement(y.getModelRemboursement());
            t.setReechellonagePossible(y.getReechellonagePossible());
            t.setFusionPossible(y.getFusionPossible());
            t.setAnticipationPossible(y.getAnticipationPossible());
            t.setPenaliteAnticipation(y.getPenaliteAnticipation());
            t.setTauxPenaliteAnticipation(y.getTauxPenaliteAnticipation());
            t.setBasePenaliteAnticipation(y.getBasePenaliteAnticipation());
            t.setSuspensionPossible(y.getSuspensionPossible());
            t.setPenaliteSuspension(y.getPenaliteSuspension());
            t.setTauxPenaliteSuspension(y.getTauxPenaliteSuspension());
            t.setBasePenaliteSuspension(y.getBasePenaliteSuspension());
            t.setPenaliteRetard(y.getPenaliteRetard());
            t.setTauxPenaliteRetard(y.getTauxPenaliteRetard());
            t.setBasePenaliteRetard(y.getBasePenaliteRetard());
            t.setByFusion(y.getByFusion());
            t.setDateSave(y.getDateSave());
        }
        return t;
    }

    public static YvsMutTypeCredit buildTypeCredit(TypeCredit y, YvsUsersAgence ua, YvsMutMutuelle mut) {
        YvsMutTypeCredit t = new YvsMutTypeCredit();
        if (y != null) {
            t.setCode(y.getCode());
            t.setDesignation(y.getDesignation());
            t.setId(y.getId());
            t.setNatureMontant(y.getNatureMontant());
            t.setMontantMaximal(y.getMontantMaximal());
            t.setPeriodeMaximal(y.getPeriodeMaximal());
            t.setTauxMaximal(y.getTauxMaximal());
            t.setAssistance(y.isAssistance());
            t.setImpayeDette(y.isImpayeDette());
            t.setMutuelle(mut);
            t.setTypeAvance(false);
            t.setAuthor(ua);
            t.setNbreAvalise(y.getNbreAvalise());
            t.setPeriodicite(y.getPeriodicite());
            t.setTypeMensualite(y.getTypeMensualite());
            t.setFormuleInteret(y.getFormuleInteret());
            t.setModelRemboursement(y.getModelRemboursement());
            t.setReechellonagePossible(y.isReechellonagePossible());
            t.setFusionPossible(y.isFusionPossible());
            t.setAnticipationPossible(y.isAnticipationPossible());
            t.setPenaliteAnticipation(y.isPenaliteAnticipation());
            t.setTauxPenaliteAnticipation(y.getTauxPenaliteAnticipation());
            t.setBasePenaliteAnticipation(y.getBasePenaliteAnticipation());
            t.setNaturePenaliteAnticipation(y.getNaturePenaliteAnticipation());
            t.setSuspensionPossible(y.isSuspensionPossible());
            t.setPenaliteSuspension(y.isPenaliteSuspension());
            t.setTauxPenaliteSuspension(y.getTauxPenaliteSuspension());
            t.setBasePenaliteSuspension(y.getBasePenaliteSuspension());
            t.setNaturePenaliteSuspension(y.getNaturePenaliteSuspension());
            t.setPenaliteRetard(y.isPenaliteRetard());
            t.setTauxPenaliteRetard(y.getTauxPenaliteRetard());
            t.setBasePenaliteRetard(y.getBasePenaliteRetard());
            t.setNaturePenaliteRetard(y.getNaturePenaliteRetard());
            t.setCoefficientRemboursement(y.getCoefficientRemboursement());
            t.setByFusion(y.isByFusion());
            t.setDateSave(y.getDateSave());
            t.setDateUpdate(new Date());
            t.setNew_(true);
        }
        return t;
    }

    public static List<TypeAvance> buildBeanListTypeAvance(List<YvsMutTypeCredit> y) {
        List<TypeAvance> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutTypeCredit c : y) {
                l.add(buildBeanTypeAvance(c));
            }
        }
        return l;
    }

    public static TypeCredit buildBeanTypeCredit(YvsMutTypeCredit y) {
        TypeCredit t = new TypeCredit();
        if (y != null) {
            t.setCode(y.getCode());
            t.setDesignation(y.getDesignation());
            t.setId(y.getId());
            t.setTypeAvance((y.getTypeAvance() != null) ? y.getTypeAvance() : false);
            t.setAssistance((y.getAssistance() != null) ? y.getAssistance() : false);
            t.setMontantMaximal((y.getMontantMaximal() != null) ? y.getMontantMaximal() : 0);
            t.setPeriodeMaximal((y.getPeriodeMaximal() != null) ? y.getPeriodeMaximal() : 0);
            t.setTauxMaximal((y.getTauxMaximal() != null) ? y.getTauxMaximal() : 0);
            t.setImpayeDette((y.getImpayeDette() != null) ? y.getImpayeDette() : false);
            t.setJourDebutAvance((y.getJourDebutAvance() != null) ? y.getJourDebutAvance() : 0);
            t.setJourFinAvance((y.getJourFinAvance() != null) ? y.getJourFinAvance() : 0);
            t.setMutuelle((y.getMutuelle() != null) ? buildBeanMutuelle(y.getMutuelle()) : new Mutuelle());
            t.setGrilles(y.getGrilles());
            t.setFrais(y.getFrais());
            t.setNatureMontant(y.getNatureMontant());
            t.setSuffixeMontant(y.getSuffixeMontant());
            t.setNbreAvalise(y.getNbreAvalise());
            t.setPeriodicite(y.getPeriodicite());
            t.setTypeMensualite(y.getTypeMensualite());
            t.setFormuleInteret(y.getFormuleInteret());
            t.setModelRemboursement(y.getModelRemboursement());
            t.setReechellonagePossible(y.getReechellonagePossible());
            t.setFusionPossible(y.getFusionPossible());
            t.setAnticipationPossible(y.getAnticipationPossible());
            t.setPenaliteAnticipation(y.getPenaliteAnticipation());
            t.setTauxPenaliteAnticipation(y.getTauxPenaliteAnticipation());
            t.setBasePenaliteAnticipation(y.getBasePenaliteAnticipation());
            t.setNaturePenaliteAnticipation(y.getNaturePenaliteAnticipation());
            t.setSuspensionPossible(y.getSuspensionPossible());
            t.setPenaliteSuspension(y.getPenaliteSuspension());
            t.setTauxPenaliteSuspension(y.getTauxPenaliteSuspension());
            t.setBasePenaliteSuspension(y.getBasePenaliteSuspension());
            t.setNaturePenaliteSuspension(y.getNaturePenaliteSuspension());
            t.setPenaliteRetard(y.getPenaliteRetard());
            t.setTauxPenaliteRetard(y.getTauxPenaliteRetard());
            t.setBasePenaliteRetard(y.getBasePenaliteRetard());
            t.setNaturePenaliteRetard(y.getNaturePenaliteRetard());
            t.setByFusion(y.getByFusion());
            t.setCoefficientRemboursement(y.getCoefficientRemboursement());
            t.setDateSave(y.getDateSave());
        }
        return t;
    }

    public static List<TypeCredit> buildBeanListTypeCredit(List<YvsMutTypeCredit> y) {
        List<TypeCredit> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutTypeCredit c : y) {
                l.add(buildBeanTypeCredit(c));
            }
        }
        return l;
    }

    public static List<Condition> buildConditions(List<YvsMutConditionCredit> lc) {
        List<Condition> re = new ArrayList<>();
        Condition co;
        for (YvsMutConditionCredit c : lc) {
            co = new Condition();
            co.setCode(c.getCode());
            co.setCommentaire(c.getCommentaire());
            co.setCorrect(c.getCorrect());
            co.setDateModification(c.getDateModification());
            co.setId(c.getId());
            co.setLibelle(c.getLibelle());
            co.setUnite(c.getUnite());
            co.setValeurEntree(c.getValeurEntree());
            co.setValeurRequise(c.getValeurRequise());
            co.setValeurRequise2(c.getValeurRequise());
            re.add(co);
        }
        return re;
    }

    public static Credit buildBeanCredit(YvsMutCredit y) {
        Credit c = buildSimpleBeanCredit(y);
        if (y != null) {
            c.setMontantReste(y.getMontantReste());
            c.setConditions(y.getConditions());
            c.setEcheanciers(y.getRemboursements());
            if (y.getRemboursements() != null) {
                for (YvsMutEchellonage ech : y.getRemboursements()) {
                    if (ech.getActif() && !ech.getEtat().equals(Constantes.ETAT_SUSPENDU)) {
                        c.setEcheancier(buildBeanSimpleEchellonage(ech));
                        y.setEcheancier(ech);
                    }
                }
            }
            c.setFraisAdditionnel(y.getFraisAdditionnel());
            c.setReglements(y.getReglements());
        }
        return c;
    }

    public static Credit buildSimpleBeanCredit(YvsMutCredit y) {
        Credit c = new Credit();
        if (y != null) {
            c.setId(y.getId() != null ? y.getId() : 0);
            c.setDateCredit((y.getDateCredit() != null) ? y.getDateCredit() : new Date());
            c.setHeureCredit((y.getHeureCredit() != null) ? y.getHeureCredit() : new Date());
            c.setReference(y.getReference());
            c.setEtatValidation(y.getEtat());
            c.setMotifRefus(y.getMotifRefus());
            c.setMontantVerse(y.getMontantVerse());
            c.setMontantEncaisse(y.getMontantEncaisse());
//            c.setMontantRestant(y.getMontantRestant());
            c.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            c.setCompte((y.getCompte() != null) ? buildBeanCompte(y.getCompte()) : new Compte());
            c.setType((y.getType() != null) ? buildBeanTypeCredit(y.getType()) : new TypeCredit());
            c.setDuree((y.getDuree() != null) ? y.getDuree() : 0);
            c.setDateEffet(y.getDateEffet());
            c.setAvalises(y.getAvalises());
            c.setMontantDispo(y.getMontantDispo());
            c.setVotes(y.getVotes());
            c.setVotesApprouve(y.getVotesApprouve());
            c.setVotesDeapprouve(y.getVotesDeapprouve());
            c.setDateSoumission(y.getDateSoumission());
            c.setDateSave(y.getDateSave());
            c.setStatutRemboursement(y.getStatutCredit());
            c.setStatutPaiement(y.getStatutPaiement());
//            double montant = c.getMontant() + (c.getCompte().getMutualiste().getMontantSalaire() * c.getType().getTauxMaximal() / 100);
            c.setMontantTotal(y.getMontant());
            c.setDateSave(y.getDateSave());
        }
        return c;
    }

    public static YvsMutCredit buildCredit(Credit y, YvsUsersAgence ua) {
        return buildCredit(y, buildTypeCredit(y.getType(), ua, null), ua);
    }

    public static YvsMutCredit buildCredit(Credit y, YvsMutTypeCredit type, YvsUsersAgence ua) {
        YvsMutCredit c = new YvsMutCredit();
        if (y != null) {
            c.setId(y.getId());
            c.setDateCredit((y.getDateCredit() != null) ? y.getDateCredit() : new Date());
            c.setHeureCredit((y.getHeureCredit() != null) ? y.getHeureCredit() : new Date());
            c.setReference(y.getReference());
            c.setEtat(y.getEtatValidation());
            c.setMontant(y.getMontant());
            c.setDateSave(y.getDateSave());
            c.setDateEffet(y.getDateEffet());
            c.setDateSoumission(y.getDateSoumission());
            c.setStatutCredit(y.getStatutRemboursement());
            c.setStatutPaiement(y.getStatutPaiement());
            c.setFraisAdditionnel(y.getFraisAdditionnel());
            c.setAutomatique(y.isAutomatique());
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
            c.setAuthor(ua);
            c.setDuree((y.getDuree() <= 0) ? (int) y.getType().getPeriodeMaximal() : y.getDuree());
            if ((y.getCompte() != null) ? y.getCompte().getId() > 0 : false) {
                YvsGrhEmployes e = new YvsGrhEmployes(y.getCompte().getMutualiste().getEmploye().getId(), y.getCompte().getMutualiste().getEmploye().getMatricule(), y.getCompte().getMutualiste().getEmploye().getCivilite(), y.getCompte().getMutualiste().getEmploye().getNom(), y.getCompte().getMutualiste().getEmploye().getPrenom());
                e.setPhotos(y.getCompte().getMutualiste().getEmploye().getPhotos());
                c.setCompte(new YvsMutCompte(y.getCompte().getId(), y.getCompte().getReference(), new YvsMutMutualiste(y.getCompte().getMutualiste().getId(), e)));
            }
            if (type != null ? type.getId() > 0 : false) {
                c.setType(type);
            }
            c.getConditions().addAll(y.getConditions());
        }
        return c;
    }

    public static List<Credit> buildBeanListCredit(List<YvsMutCredit> y) {
        List<Credit> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutCredit c : y) {
                l.add(buildBeanCredit(c));
            }
        }
        return l;
    }

    public static VoteCredit buildBeanVoteCredit(YvsMutVoteValidationCredit y) {
        VoteCredit v = new VoteCredit();
        if (y != null) {
            v.setId(y.getId());
            v.setAccepte(y.getAccepte());
            v.setDateValidation(y.getDateValidation());
            v.setCredit(buildSimpleBeanCredit(y.getCredit()));
            v.setMutualiste(buildSimpleBeanMutualiste(y.getMutualiste()));
        }
        return v;
    }

    public static YvsMutVoteValidationCredit buildVoteCredit(VoteCredit y, YvsUsersAgence ua) {
        YvsMutVoteValidationCredit v = new YvsMutVoteValidationCredit();
        if (y != null) {
            v.setId(y.getId());
            v.setAccepte(y.isAccepte());
            v.setDateValidation(y.getDateValidation());
            v.setCredit(new YvsMutCredit(y.getCredit().getId()));
            v.setMutualiste(new YvsMutMutualiste(y.getMutualiste().getId()));
            v.setAuthor(ua);
            v.setNew_(true);
        }
        return v;
    }

    /**
     *
     * FIN GESTION CREDIT
     *
     *
     */
    /**
     *
     * DEBUT GESTION EVENEMENT
     *
     *
     * @param y
     * @return
     */
    public static TypeEvenement buildBeanTypeEvenement(YvsMutTypeEvenement y) {
        TypeEvenement t = new TypeEvenement();
        if (y != null) {
            t.setDescription(y.getDescription());
            t.setDesignation(y.getDesignation());
            t.setId(y.getId());
            t.setNombreRepresentant((y.getNombreParticipant() != null) ? y.getNombreParticipant() : 0);
            if (t.getNombreRepresentant() < 1) {
                t.setNombreParticipant("Tous");
            } else {
                t.setNombreParticipant("" + t.getNombreRepresentant());
            }
            t.setLierMutualiste((y.getLierMutualiste() != null) ? y.getLierMutualiste() : false);
            t.setMutuelle((y.getMutuelle() != null) ? buildBeanMutuelle(y.getMutuelle()) : new Mutuelle());
            t.setContributions(y.getContributions());
            t.setDateSave(y.getDateSave());
        }
        return t;
    }

    public static List<TypeEvenement> buildBeanListTypeEvenement(List<YvsMutTypeEvenement> y) {
        List<TypeEvenement> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutTypeEvenement c : y) {
                l.add(buildBeanTypeEvenement(c));
            }
        }
        return l;
    }

    public static ContributionEvenement buildBeanContributionEvenement(YvsMutContributionEvenement y) {
        ContributionEvenement c = new ContributionEvenement();
        if (y != null) {
            c.setId(y.getId());
            c.setDateContribution((y.getDateContribution() != null) ? y.getDateContribution() : new Date());
            c.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            c.setMontantVerse(y.getMontantVerse());
            c.setCompte((y.getCompte() != null) ? buildBeanCompte(y.getCompte()) : new Compte());
            c.setEvenement(buildBeanEvenement(y.getEvenement()));
            c.setRegle(y.isRegle());
            c.setDateSave(y.getDateSave());
        }
        return c;
    }

    public static YvsMutContributionEvenement buildContributionEvenement(ContributionEvenement y, YvsUsersAgence ua) {
        YvsMutContributionEvenement c = new YvsMutContributionEvenement();
        if (y != null) {
            c.setId(y.getId());
            c.setDateContribution((y.getDateContribution() != null) ? y.getDateContribution() : new Date());
            c.setMontant(y.getMontant());
            if (y.getCompte() != null ? y.getCompte().getId() > 0 : false) {
                c.setCompte(new YvsMutCompte(y.getCompte().getId()));
            }
            if (y.getEvenement() != null ? y.getEvenement().getIdEvt() > 0 : false) {
                c.setEvenement(new YvsMutEvenement(y.getEvenement().getIdEvt()));
            }
            c.setMontantVerse(y.getMontantVerse());
            c.setRegle(y.isRegle());
            c.setAuthor(ua);
            c.setNew_(true);
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
        }
        return c;
    }

    public static List<ContributionEvenement> buildBeanListContributionEvenement(List<YvsMutContributionEvenement> y) {
        List<ContributionEvenement> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutContributionEvenement c : y) {
                l.add(buildBeanContributionEvenement(c));
            }
        }
        return l;
    }

    public static ParticipantEvenement buildBeanParticipantEvenement(YvsMutParticipantEvenement y) {
        ParticipantEvenement p = new ParticipantEvenement();
        if (y != null) {
            p.setId(y.getId());
            p.setMutualiste((y.getMutualiste() != null) ? buildBeanMutualiste(y.getMutualiste()) : new Mutualiste());
            p.setActivite(buildBeanActivite(y.getActivite()));
            p.setOrganisateur(y.getOrganisateur());
            p.setRoleMembre(y.getRoleMembre());
        }
        return p;
    }

    public static YvsMutParticipantEvenement buildParticipantEvenement(ParticipantEvenement y, YvsUsersAgence ua) {
        YvsMutParticipantEvenement p = new YvsMutParticipantEvenement();
        if (y != null) {
            p.setId(y.getId());
            if (y.getMutualiste() != null ? y.getMutualiste().getId() > 0 : false) {
                p.setMutualiste(new YvsMutMutualiste(y.getMutualiste().getId(), new YvsGrhEmployes(y.getMutualiste().getEmploye().getId(), y.getMutualiste().getEmploye().getNom(), y.getMutualiste().getEmploye().getPrenom())));
            }
            if (y.getActivite() != null ? y.getActivite().getId() > 0 : false) {
                p.setActivite(new YvsMutActivite(y.getActivite().getId(), new YvsMutActiviteType(y.getActivite().getTypeActivite().getId(), y.getActivite().getTypeActivite().getLibelle())));
            }
            p.setOrganisateur(y.isOrganisateur());
            p.setRoleMembre(y.getRoleMembre());
            p.setAuthor(ua);
            p.setNew_(true);
        }
        return p;
    }

    public static List<ParticipantEvenement> buildBeanListParticipantEvenement(List<YvsMutParticipantEvenement> y) {
        List<ParticipantEvenement> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutParticipantEvenement c : y) {
                l.add(buildBeanParticipantEvenement(c));
            }
        }
        return l;
    }

    public static FinancementActivite buildBeanFinancementActivite(YvsMutFinancementActivite y) {
        FinancementActivite r = new FinancementActivite();
        if (y != null) {
            r.setId(y.getId() != null ? y.getId() : 0);
            r.setActivite(buildBeanActivite(y.getActivite()));
            r.setCaisse(buildBeanCaisse(y.getCaisse()));
            r.setDateFinancement(y.getDateFinancement());
            r.setMontantRecu(y.getMontantRecu());
        }
        return r;
    }

    public static YvsMutFinancementActivite buildFinancementActivite(FinancementActivite y, YvsUsersAgence ua) {
        YvsMutFinancementActivite r = new YvsMutFinancementActivite();
        if (y != null) {
            r.setId(y.getId());
            if (y.getCaisse() != null ? y.getCaisse().getId() > 0 : false) {
                r.setCaisse(new YvsMutCaisse(y.getCaisse().getId(), y.getCaisse().getReference()));
            }
            if (y.getActivite() != null ? y.getActivite().getId() > 0 : false) {
                r.setActivite(new YvsMutActivite(y.getActivite().getId(), new YvsMutActiviteType(y.getActivite().getTypeActivite().getId(), y.getActivite().getTypeActivite().getLibelle())));
            }
            r.setDateFinancement(y.getDateFinancement());
            r.setMontantRecu(y.getMontantRecu());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static Activite buildBeanActivite(YvsMutActivite y) {
        Activite r = new Activite();
        if (y != null) {
            r.setId(y.getId() != null ? y.getId() : 0);
            r.setEvenement(buildBeanEvenement(y.getEvenement()));
            r.setMontantRequis(y.getMontantRequis());
            r.setParticipants(y.getParticipants());
            r.setFinancements(y.getFinancements());
            r.setTypeActivite(buildBeanTypeContribution(y.getTypeActivite()));
        }
        return r;
    }

    public static YvsMutActivite buildActivite(Activite y, YvsUsersAgence ua) {
        YvsMutActivite r = new YvsMutActivite();
        if (y != null) {
            r.setId(y.getId());
            if (y.getEvenement() != null ? y.getEvenement().getIdEvt() > 0 : false) {
                r.setEvenement(new YvsMutEvenement(y.getEvenement().getIdEvt()));
            }
            r.setMontantRequis(y.getMontantRequis());
            r.setParticipants(y.getParticipants());
            if (y.getTypeActivite() != null ? y.getTypeActivite().getId() > 0 : false) {
                r.setTypeActivite(new YvsMutActiviteType(y.getTypeActivite().getId()));
            }
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static YvsMutEvenement buildEvenement(Evenement y, YvsUsersAgence ua) {
        YvsMutEvenement e = new YvsMutEvenement();
        if (y != null) {
            e.setDateEvenement((y.getDateEvenement() != null) ? y.getDateEvenement() : new Date());
            e.setDescription(y.getDescriptionEvt());
            e.setId(y.getIdEvt());
            if ((y.getType() != null) ? y.getType().getId() != 0 : false) {
                e.setType(new YvsMutTypeEvenement(y.getType().getId(), y.getType().getDesignation()));
            }
            if ((y.getMutualiste() != null) ? y.getMutualiste().getId() != 0 : false) {
                e.setMutualiste(new YvsMutMutualiste(y.getMutualiste().getId(), new YvsGrhEmployes(y.getMutualiste().getEmploye().getId(), y.getMutualiste().getEmploye().getNom(), y.getMutualiste().getEmploye().getPrenom())));
            }
            e.setHeureFin((y.getHeureFin() != null) ? y.getHeureFin() : new Date());
            e.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            e.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            e.setEtat(y.getEtat());
            e.setMontantObligatoire(y.getMontantObligatoire());
            e.setHeureDebut((y.getHeureDebut() != null) ? y.getHeureDebut() : new Date());
            e.setDateClotureContribution((y.getDateClotureContribution() != null) ? y.getDateClotureContribution() : new Date());
            e.setDateOuvertureContribution((y.getDateOuvertureContribution() != null) ? y.getDateOuvertureContribution() : new Date());
            if ((y.getLieu() != null) ? y.getLieu().getId() != 0 : false) {
                e.setLieu(new YvsDictionnaire(y.getLieu().getId(), y.getLieu().getLibelle()));
            }
            e.setAuthor(ua);
            e.setDateSave(y.getDateSave());
            e.setDateUpdate(new Date());
            e.setCaisseEvent(buildBeanCaisse(y.getCaisse()));
        }
        return e;
    }

    public static Evenement buildBeanEvenement(YvsMutEvenement y) {
        Evenement e = new Evenement();
        if (y != null) {
            e.setMutualiste((y.getMutualiste() != null) ? buildBeanMutualiste(y.getMutualiste()) : new Mutualiste());
            e.setType((y.getType() != null) ? buildBeanTypeEvenement(y.getType()) : new TypeEvenement());
            e.setDateEvenement((y.getDateEvenement() != null) ? y.getDateEvenement() : new Date());
            e.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            e.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            e.setHeureDebut((y.getHeureDebut() != null) ? y.getHeureDebut() : new Date());
            e.setHeureFin((y.getHeureFin() != null) ? y.getHeureFin() : new Date());
            e.setDescriptionEvt(y.getDescription());
            e.setEtat((y.getEtat() != null) ? ((!y.getEtat().trim().equals("")) ? y.getEtat() : Constantes.ETAT_EDITABLE) : Constantes.ETAT_EDITABLE);
            e.setIdEvt(y.getId());
            e.setDateClotureContribution((y.getDateClotureContribution() != null) ? y.getDateClotureContribution() : new Date());
            e.setDateOuvertureContribution((y.getDateOuvertureContribution() != null) ? y.getDateOuvertureContribution() : new Date());
            e.setLieu((y.getLieu() != null) ? UtilGrh.buildBeanDictionnaire(y.getLieu()) : new Dictionnaire());
            e.setParticipants(y.getParticipants());
            e.setFinancements(y.getFinancements());
            e.setActivites(y.getActivites());
            e.setCouts(y.getCouts());
            e.setCoutTotal(y.getCoutTotal());
            e.setContributions(y.getContributions());
            e.setMontantAttendu(y.getMontantAttendu());
            e.setMontantRecu(y.getMontantRecu());
            e.setMontantObligatoire(y.getMontantObligatoire());
            e.setDateSave(y.getDateSave());

            e.setStartDate(e.getDateDebut());
            e.setEndDate(e.getDateFin());
            e.setDescription(e.getDescriptionEvt());
            String titre = " " + e.getMutualiste().getEmploye().getPrenom() + " " + e.getMutualiste().getEmploye().getNom() + " : " + e.getType().getDesignation();
            e.setTitle(titre);
        }
        return e;
    }

    public static List<Evenement> buildBeanListEvenement(List<YvsMutEvenement> y) {
        List<Evenement> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutEvenement c : y) {
                l.add(buildBeanEvenement(c));
            }
        }
        return l;
    }

    public static CoutEvenement buildBeanCoutEvenement(YvsMutCoutEvenement y) {
        CoutEvenement c = new CoutEvenement();
        if (y != null) {
            c.setId(y.getId());
            c.setEvenement((y.getEvenement() != null) ? new Evenement(y.getEvenement().getId()) : new Evenement());
            c.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            c.setType((y.getType() != null) ? UtilGrh.buildBeanTypeCout(y.getType()) : new TypeCout());
            c.setUpdate(true);
        }
        return c;
    }

    public static List<CoutEvenement> buildBeanListCoutEvenement(List<YvsMutCoutEvenement> y) {
        List<CoutEvenement> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutCoutEvenement c : y) {
                l.add(buildBeanCoutEvenement(c));
            }
        }
        return l;
    }

    public static TypeContribution buildBeanTypeContribution(YvsMutActiviteType y) {
        TypeContribution t = new TypeContribution();
        if (y != null) {
            t.setDescription(y.getDescription());
            t.setId(y.getId());
            t.setLibelle(y.getLibelle());
            t.setMutuelle((y.getMutuelle() != null) ? buildBeanMutuelle(y.getMutuelle()) : new Mutuelle());
            t.setDateSave(y.getDateSave());
        }
        return t;
    }

    public static List<TypeContribution> buildBeanListTypeContribution(List<YvsMutActiviteType> y) {
        List<TypeContribution> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutActiviteType c : y) {
                l.add(buildBeanTypeContribution(c));
            }
        }
        return l;
    }

    public static Contribution buildBeanContribution(YvsMutTauxContribution y) {
        Contribution c = new Contribution();
        if (y != null) {
            c.setId(y.getId());
            c.setMontant((y.getMontantMin() != null) ? y.getMontantMin() : 0);
            c.setTypeContibution((y.getTypeContibution() != null) ? buildBeanTypeContribution(y.getTypeContibution()) : new TypeContribution());
        }
        return c;
    }

    public static List<Contribution> buildBeanListContribution(List<YvsMutTauxContribution> y) {
        List<Contribution> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutTauxContribution c : y) {
                l.add(buildBeanContribution(c));
            }
        }
        return l;
    }

    /**
     *
     * FIN GESTION EVENEMENT
     *
     *
     */
    /**
     *
     * DEBUT GESTION EXERCICE
     *
     *
     * @param y
     * @return
     */
    public static Exercice buildBeanExercice(YvsBaseExercice y) {
        Exercice e = new Exercice();
        if (y != null) {
            e.setActif((y.getActif() != null) ? y.getActif() : false);
            e.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            e.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            e.setId(y.getId());
            e.setReference(y.getReference());
            e.setNameExercice("du " + Managed.dft.format(y.getDateDebut()) + " au " + Managed.dft.format(y.getDateFin()));
            e.setPeriodes(y.getPeriodesMutuelles());
            e.setDateSave(y.getDateSave());
            e.setCloture(y.getCloturer());
//            PeriodeExercice pe;
//            for (YvsMutPeriodeExercice p : y.getPeriodesMutuelles()) {
//                pe = new PeriodeExercice();
//                pe.setCloture(p.getCloture());
//                pe.setDateDebut(p.getDateDebut());
//                pe.setDateFin(p.getDateFin());
//                pe.setId(p.getId());
//                e.getPeriodes().add(pe);
//            }
        }
        return e;
    }

    public static YvsBaseExercice buildExercice(Exercice y, YvsSocietes s, YvsUsersAgence u) {
        YvsBaseExercice e = new YvsBaseExercice();
        if (y != null) {
            e.setId(y.getId());
            e.setReference(y.getReference());
            e.setActif(y.isActif());
            e.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            e.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            e.setCloturer(y.isCloture());
            e.setSociete(s);
            e.setAuthor(u);
            e.setDateUpdate(new Date());
            e.setDateSave(y.getDateSave());
        }
        return e;
    }

    public static List<Exercice> buildBeanListExercice(List<YvsBaseExercice> y) {
        List<Exercice> l = new ArrayList<>();
        if (y != null) {
            for (YvsBaseExercice c : y) {
                l.add(buildBeanExercice(c));
            }
        }
        return l;
    }

    /**
     *
     * FIN GESTION EXERCICE
     *
     *
     */
    /**
     *
     * DEBUT GESTION REMBOURSEMENT
     *
     *
     * @param y
     * @return
     */
    public static Mensualite buildBeanMensualite(YvsMutMensualite y) {
        Mensualite m = new Mensualite();
        if (y != null) {
            m.setDateMensualite((y.getDateMensualite() != null) ? y.getDateMensualite() : new Date());
            m.setId(y.getId());
            m.setCommentaire(y.getCommentaire());
            m.setMontant(y.getMontant());
            m.setAmortissement((y.getAmortissement() != null) ? y.getAmortissement() : 0);
            m.setInteret((y.getInteret() != null) ? y.getInteret() : 0);
            m.setReglements(y.getReglements());
            m.setMontantVerse(y.getMontantVerse());
            m.setMontantReste(y.getMontantReste());
            m.setMontantReel(y.getMontantReel());
            m.setMontantPenalite(y.getMontantPenalite());
            m.setEtat(y.getEtat());
        }
        return m;
    }

    public static List<Mensualite> buildBeanListMensualite(List<YvsMutMensualite> y) {
        List<Mensualite> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutMensualite c : y) {
                l.add(buildBeanMensualite(c));
            }
        }
        Collections.sort(l, new Mensualite()); //trie la liste des mensualité par date
        return l;
    }

    public static ReglementMensualite buildBeanReglementMensualite(YvsMutReglementMensualite y) {
        ReglementMensualite r = new ReglementMensualite();
        if (y != null) {
            r.setId(y.getId());
            r.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            r.setReglerPar(y.getReglePar());
            r.setStatutPiece(y.getStatutPiece());
            r.setCaisse(buildBeanCaisse(y.getCaisse()));
            r.setDateReglement((y.getDateReglement() != null) ? y.getDateReglement() : new Date());
            r.setCredit(buildSimpleBeanCredit(y.getMensualite().getEchellonage().getCredit()));
        }
        return r;
    }

    public static ReglementCreditMut buildBeanReglementCredit(YvsMutReglementCredit y) {
        ReglementCreditMut r = new ReglementCreditMut();
        if (y != null) {
            r.setId(y.getId());
            r.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            r.setReglerPar(y.getReglePar());
            r.setStatutPiece(y.getStatutPiece());
            r.setCaisse(buildBeanCaisse(y.getCaisse()));
            r.setDateReglement((y.getDateReglement() != null) ? y.getDateReglement() : new Date());
            r.setCredit(buildSimpleBeanCredit(y.getCredit()));
            r.setDateSave(y.getDateSave());
            r.setModePaiement(y.getModePaiement());
        }
        return r;
    }

    public static YvsMutCompte buildCompte(Compte y, YvsUsersAgence ua) {
        if ((y != null) ? y.getId() > 0 : false) {
            YvsMutCompte c = new YvsMutCompte(y.getId());
            c.setActif(y.isActif());
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
            c.setMutualiste(buildMutualiste(y.getMutualiste(), ua));
            c.setReference(y.getReference());
            c.setTypeCompte(new YvsMutTypeCompte(y.getType().getId(), y.getType().getLibelle(), y.getType().getNature()));
            return c;
        }
        return null;
    }

    public static YvsMutPeriodeExercice buildPeriode(PeriodeExercice y) {
        if ((y != null) ? y.getId() > 0 : false) {
            YvsMutPeriodeExercice c = new YvsMutPeriodeExercice(y.getId());
            c.setActif(y.isActif());
            c.setReferencePeriode(y.getReference());
            c.setDateDebut(y.getDateDebut());
            c.setDateFin(y.getDateFin());
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
            return c;
        }
        return null;
    }

    public static YvsMutReglementCredit buildReglementCredit(ReglementCreditMut y, YvsMutCredit c, YvsUsersAgence ua) {
        YvsMutReglementCredit r = new YvsMutReglementCredit();
        if (y != null) {
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setReglePar(y.getReglerPar());
            r.setStatutPiece(y.getStatutPiece());
            r.setCredit(c);
            r.setModePaiement(y.getModePaiement());
            r.setDateUpdate(new Date());
            r.setDateSave(y.getDateSave());
            if ((y.getCaisse() != null) ? y.getCaisse().getId() > 0 : false) {
                r.setCaisse(new YvsMutCaisse(y.getCaisse().getId(), y.getCaisse().getReference()));
            } else {
                r.setCaisse(null);
            }
            r.setComptes(buildCompte(y.getCompte(), ua));
            r.setDateReglement((y.getDateReglement() != null) ? y.getDateReglement() : new Date());
            r.setAuthor(ua);
        }
        return r;
    }

    public static List<ReglementMensualite> buildBeanListReglementMensualite(List<YvsMutReglementMensualite> y) {
        List<ReglementMensualite> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutReglementMensualite c : y) {
                l.add(buildBeanReglementMensualite(c));
            }
        }
        return l;
    }

    public static Echellonage buildBeanSimpleEchellonage(YvsMutEchellonage y) {
        Echellonage r = new Echellonage();
        if (y != null) {
            r.setDateEchellonage((y.getDateEchellonage() != null) ? y.getDateEchellonage() : new Date());
            r.setDureeEcheance((y.getDureeEcheance() != null) ? y.getDureeEcheance() : 0);
            r.setEtat(y.getEtat());
            r.setIdEch(y.getId() != null ? y.getId() : 0);
            r.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            r.setEcartMensualite((y.getEcartMensualite() != null) ? y.getEcartMensualite() : 0);
            r.setTaux((y.getTaux() != null) ? y.getTaux() : 0);
            r.setMontantVerse(y.getMontantVerse());
            r.setMontantReste(y.getMontantReste());
            r.setMensualites(y.getMensualites());
            for (YvsMutMensualite m : r.getMensualites()) {
                if (!m.getEtat().equals(Constantes.ETAT_EDITABLE)) {
                    r.setUpdate(false);
                    break;
                }
            }
            r.setDateSave(y.getDateSave());
            r.setActif(y.getActif());
            r.setMontantInteret(y.getMontantInteret());
            r.setEchellone((r.getMensualites() != null) ? r.getMensualites().isEmpty() : true);
            r.setStartDate(r.getDateEchellonage());
            r.setEndDate(r.getDateEchellonage());
            r.setTitle(r.getMontant() + " Fcfa");
            if (y.getCredit() != null ? y.getCredit().getId() > 0 : false) {
                String val = y.getCredit().getCompte().getMutualiste().getEmploye().getPrenom() + " " + y.getCredit().getCompte().getMutualiste().getEmploye().getNom();
                val += " : Montant = " + r.getTitle();
                r.setDescription(val);
            }
            r.setCreditRetainsInteret(y.getCreditRetainsInteret());
        }
        return r;
    }

    public static Echellonage buildBeanEchellonage(YvsMutEchellonage y) {
        Echellonage r = buildBeanSimpleEchellonage(y);
        if (y != null) {
            r.setCredit((y.getCredit() != null) ? buildBeanCredit(y.getCredit()) : new Credit());
//            r.setMensualites(y.getMensualites());
        }
        return r;
    }

    public static List<Echellonage> buildBeanLisEchellonage(List<YvsMutEchellonage> y) {
        List<Echellonage> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutEchellonage c : y) {
                l.add(buildBeanEchellonage(c));
            }
        }
        return l;
    }

    /**
     *
     * FIN GESTION REMBOURSEMENT
     *
     *
     */
    /**
     *
     * DEBUT GESTION AVANCE SALAIRE
     *
     *
     */
    /**
     * DEBUT GESTION AVANCE SALAIRE
     *
     * @param y
     * @return
     */
//    public static ReglementAvance buildBeanReglementAvance(YvsMutReglementAvance y) {
//        ReglementAvance r = new ReglementAvance();
//        if (y != null) {
//            r.setDateReglement((y.getDateReglement() != null) ? y.getDateReglement() : new Date());
//            r.setId(y.getId());
//            r.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
//        }
//        return r;
//    }
//
//    public static List<ReglementAvance> buildBeanListReglementAvance(List<YvsMutReglementAvance> y) {
//        List<ReglementAvance> l = new ArrayList<>();
//        if (y != null) {
//            for (YvsMutReglementAvance c : y) {
//                l.add(buildBeanReglementAvance(c));
//            }
//        }
//        return l;
//    }
//
//    public static AvanceSalaire buildBeanAvanceSalaire(YvsMutAvanceSalaire y) {
//        AvanceSalaire a = new AvanceSalaire();
//        if (y != null) {
//            a.setId(y.getId());
//            a.setDateAvance((y.getDateAvance() != null) ? y.getDateAvance() : new Date());
//            a.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
//            a.setReference(y.getReference());
//            a.setMutualiste((y.getMutualiste() != null) ? buildBeanMutualiste(y.getMutualiste()) : new Mutualiste());
//            a.setType((y.getType() != null) ? buildBeanTypeAvance(y.getType()) : new TypeAvance());
//            a.setReglements((y.getReglements() != null) ? buildBeanListReglementAvance(y.getReglements())
//                    : new ArrayList<ReglementAvance>());
//            double montant = a.getMontant() + (a.getMutualiste().getMontantSalaire() * a.getType().getTauxMaximal() / 100);
//            a.setMontantTotal(montant);
//            a.setMontantReste(a.getMontantTotal());
//            a.setUpdate(true);
//            a.setEtat(y.getEtat());
//            a.setMotifRefus(y.getMotifRefus());
//            if (!a.getReglements().isEmpty()) {
//                for (ReglementAvance r : a.getReglements()) {
//                    a.setMontantReste(a.getMontantReste() - r.getMontant());
//                }
//                a.setUpdate(false);
//            }
//        }
//        return a;
//    }
//
//    public static List<AvanceSalaire> buildBeanListAvanceSalaire(List<YvsMutAvanceSalaire> y) {
//        List<AvanceSalaire> l = new ArrayList<>();
//        if (y != null) {
//            for (YvsMutAvanceSalaire c : y) {
//                l.add(buildBeanAvanceSalaire(c));
//            }
//        }
//        return l;
//    }
    /**
     *
     * FIN GESTION AVANCE SALAIRE
     *
     *
     */
    /**
     *
     * DEBUT GESTION SALAIRE ET INTERET
     *
     *
     * @param y
     * @return
     */
//    public static PaiementSalaire buildBeanPaiementSalaire(YvsMutPaiementSalaire y) {
//        PaiementSalaire p = new PaiementSalaire();
//        if (y != null) {
//            p.setId(y.getId());
//            p.setCommentaire(y.getCommentaire());
//            p.setDatePaiement((y.getDatePaiement() != null) ? y.getDatePaiement() : new Date());
//            p.setMontantPaye((y.getMontantPaye() != null) ? y.getMontantPaye() : 0);
//            p.setMontantAPayer((y.getSalaire() != null) ? y.getSalaire() : 0);
//            p.setMutualiste((y.getMutualiste() != null) ? buildBeanMutualiste(y.getMutualiste()) : new Mutualiste());
//            p.setPayer(y.getPayer());
//            p.setSoeCredit((y.getCreditRetenu() != null) ? y.getCreditRetenu() : 0);
//            p.setEpargneDuMois((y.getEpargneDuMois() != null) ? y.getEpargneDuMois() : 0);
//            p.setSoeAcompte((y.getAvanceSalaireRetenu() != null) ? y.getAvanceSalaireRetenu() : 0);
//            p.setPeriode((y.getPeriode() != null) ? buildPeriodeSalaire_(y.getPeriode()) : new PeriodeSalaireMut());
//        }
//        return p;
//    }
//
//    public static List<PaiementSalaire> buildBeanListPaiementSalaire(List<YvsMutPaiementSalaire> y) {
//        List<PaiementSalaire> l = new ArrayList<>();
//        if (y != null) {
//            for (YvsMutPaiementSalaire c : y) {
//                l.add(buildBeanPaiementSalaire(c));
//            }
//        }
//        return l;
//    }
    public static Interet buildBeanInteret(YvsMutInteret y) {
        Interet i = new Interet();
        if (y != null) {
            i.setDateInteret((y.getDateInteret() != null) ? y.getDateInteret() : new Date());
            i.setId(y.getId());
            i.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            i.setMutualiste((y.getMutualiste() != null) ? buildBeanMutualiste(y.getMutualiste()) : new Mutualiste());
        }
        return i;
    }

    public static List<Interet> buildBeanListInteret(List<YvsMutInteret> y) {
        List<Interet> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutInteret c : y) {
                l.add(buildBeanInteret(c));
            }
        }
        return l;
    }

    public static ReglementPrime buildBeanReglementPrime(YvsMutReglementPrime y) {
        ReglementPrime r = new ReglementPrime();
        if (y != null) {
            r.setDatePrime((y.getDatePrime() != null) ? y.getDatePrime() : new Date());
            r.setId(y.getId());
            r.setMutualiste((y.getMutualiste() != null) ? buildBeanMutualiste(y.getMutualiste()) : new Mutualiste());
            r.setPrime((y.getPrime() != null) ? buildBeanPrimePoste(y.getPrime()) : new PrimePoste());
            r.setPoste((y.getPoste() != null) ? buildBeanPoste(y.getPoste()) : new Poste());
            r.setMontant((y.getMontant() == null) ? 0 : y.getMontant());
        }
        return r;
    }

    public static List<ReglementPrime> buildBeanListReglementPrime(List<YvsMutReglementPrime> y) {
        List<ReglementPrime> l = new ArrayList<>();
        if (y != null) {
            for (YvsMutReglementPrime c : y) {
                l.add(buildBeanReglementPrime(c));
            }
        }
        return l;
    }

    public static List<OrdreCalculSalaire> buildOrdreCalculSalaire(List<YvsGrhOrdreCalculSalaire> l) {
        OrdreCalculSalaire o;
        List<OrdreCalculSalaire> lr = new ArrayList<>();
        for (YvsGrhOrdreCalculSalaire en : l) {
            o = new OrdreCalculSalaire();
            o.setDateJour(en.getDateJour());
            o.setDebutMois(en.getDebutMois());
            o.setFinMois(en.getFinMois());
            o.setReference(en.getReference());
            o.setId(en.getId());
            lr.add(o);
        }
        return lr;
    }

    /**
     *
     * FIN GESTION SALAIRE ET INTERET
     *
     *
     */
    /**
     * @param y
     *
     * @param lmens
     *
     * @return
     */
    public static Mutualiste buildMutualiste(YvsMutMutualiste y) {
        Mutualiste m = new Mutualiste();
        if (y != null) {
            m.setId(y.getId());
            m.setActif((y.getActif() != null) ? y.getActif() : false);
            m.setMontantEpargne((y.getMontantEpargne() != null) ? y.getMontantEpargne() : 0);
            m.setDateAdhesion((y.getDateAdhesion() != null) ? y.getDateAdhesion() : new Date());
            m.setMontantTotalEpargne(0);
            m.setPostes(y.getPostes());
            m.setEmploye((y.getEmploye() != null) ? buildBeanEmploye(y.getEmploye()) : new Employe());
            m.setMutuelle((y.getMutuelle() != null) ? buildBeanMutuelle(y.getMutuelle()) : new Mutuelle());
            for (YvsMutPosteEmploye p : m.getPostes()) {
                if (p.getActif()) {
                    m.setPosteEmploye(buildBeanPosteEmploye(p));
                    break;
                }
            }
//            if ((y.getEmploye().getEmploye().getYvsContratEmpsList() != null)) {
//                List<YvsGrhContratEmps> l = y.getEmploye().getEmploye().getYvsContratEmpsList();
//                for (YvsGrhContratEmps c : l) {
//                    if (c.getContratPrincipal()) {
//                        double montant = 0;
//                        if ((c.getSalaireMensuel() != null) ? c.getSalaireMensuel() > 0 : false) {
//                            montant = c.getSalaireMensuel();
//                        } else {
//                            if ((c.getSalaireHoraire() != null) ? c.getSalaireHoraire() > 0 : false) {
//                                montant = c.getSalaireHoraire() * c.getHoraireMensuel();
//                            }
//                        }
//                        m.setMontantSalaire(montant);
//                    }
//                }
//            }
        }
        return m;
    }

    public static OrdreCalculSalaire buildPeriodeSalaire(YvsGrhOrdreCalculSalaire p) {
        OrdreCalculSalaire pe = new OrdreCalculSalaire();
        pe.setId(p.getId());
        pe.setDateExecution(p.getDateExecution());
        pe.setDateJour(p.getDateJour());
        pe.setDebutMois(p.getDebutMois());
        pe.setFinMois(p.getFinMois());
        pe.setHeureExecution(p.getHeureExecution());
        pe.setRealise(p.getRealise());
        pe.setCloture(p.getCloture());
        pe.setReference(p.getReference());
        return pe;
    }

//    public static PeriodeSalaireMut buildPeriodeSalaire_(YvsMutPeriodeSalaire p) {
//        PeriodeSalaireMut pe = new PeriodeSalaireMut();
//        pe.setId(p.getId());
//        pe.setActif(p.getActif());
//        pe.setCloture(p.getCloture());
//        return pe;
//    }
    public static PeriodeExercice buildPeriodeMutuelle(YvsMutPeriodeExercice p) {
        PeriodeExercice pe = new PeriodeExercice();
        if (p != null) {
            pe.setId(p.getId());
            pe.setActif(p.getActif());
            pe.setCloture(p.getCloture());
            pe.setDateDebut(p.getDateDebut());
            pe.setDateFin(p.getDateFin());
            pe.setReference(p.getReferencePeriode());
        }
        return pe;
    }

    public static YvsMutPoste buildEntityPoste(Poste p) {
        YvsMutPoste y = null;
        if (p != null ? p.getId() > 0 : false) {
            y = new YvsMutPoste();
            y.setId(p.getId());
            y.setCanVoteCredit(p.isCanVoteCredit());
            y.setDateSave(p.getDateSave());
            y.setDescription(p.getDescription());
            y.setDesignation(p.getDesignation());
        }
        return y;
    }

//    public static PeriodeSalaireMut buildPeriodeSalaire(YvsMutPeriodeSalaire p) {
//        PeriodeSalaireMut pe = new PeriodeSalaireMut();
//        pe.setId(p.getId());
//        pe.setActif(p.getActif());
//        pe.setCloture(p.getCloture());
//        pe.setPeriodeRh(buildPeriodeSalaire(p.getPeriodeRh()));
////        pe.setSalaires(p.getSalaires());
//        return pe;
//    }
//
//    public static List<PeriodeSalaireMut> buildPeriodesSalaire(List<YvsMutPeriodeSalaire> lp) {
//        List<PeriodeSalaireMut> re = new ArrayList<>();
//        for (YvsMutPeriodeSalaire p : lp) {
//            re.add(buildPeriodeSalaire(p));
//        }
//        return re;
//    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.mutuelle.echellonage.YvsMutMensualite;
import yvs.entity.mutuelle.echellonage.YvsMutReglementMensualite;
import yvs.entity.mutuelle.operation.YvsMutMouvementCaisse;
import yvs.entity.mutuelle.operation.YvsMutOperationCompte;

/**
 *
 * @author GOUCHERE YVES
 * @param <T>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class DaoStateFul<T extends Serializable> implements DaoInterfaceStateFull<T> {

    @PersistenceContext(unitName = "LYMYTZ-ERP-ejbPU")
    public EntityManager em;

    @Resource
    private UserTransaction utx;
    @EJB
    DaoInterfaceLocal dao;

    public DaoStateFul() {
    }

    @Override
    public boolean saveTransactionEpargne(YvsMutOperationCompte retrait, YvsMutOperationCompte depot, YvsMutOperationCompte depot2) {
        try {
            utx.begin();
            if (retrait != null) {
                retrait = (YvsMutOperationCompte) em.merge(retrait);
            }
            if (retrait != null && depot != null) {
                depot.setSouceReglement(retrait.getId());
            }
            if (depot != null) {
                em.merge(depot);
            }
            if (depot2 != null) {
                em.merge(depot2);
            }
            try {
                utx.commit();
                return true;
            } catch (RollbackException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicMixedException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicRollbackException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            }

            return false;
        } catch (NotSupportedException ex) {
            Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean saveTransactionRegMensualite(YvsMutOperationCompte op1, List<YvsMutReglementMensualite> lmens) {
        try {
            utx.begin();
            em.persist(op1);
            for (YvsMutReglementMensualite m : lmens) {
//                YvsMutMensualite mens = (YvsMutMensualite) em.find(YvsMutMensualite.class, m.getMensualite().getId());
                Query q = em.createNamedQuery("YvsMutMensualite.findById");
                q.setParameter("id", m.getMensualite().getId());
                YvsMutMensualite mens = (YvsMutMensualite) q.getSingleResult();
                if (mens != null) {
                    em.merge(m);
                    mens.getReglements().add(m);
                    String etat = Constantes.ETAT_REGLE;
                    if (mens.getMontantReste() > 0) {
                        etat = Constantes.ETAT_ENCOURS;
                    }
                    mens.setAuthor(m.getAuthor());
                    mens.setDateUpdate(new Date());
                    mens.setEtat(etat);
                    em.merge(mens);
                    //update l'echéancier
                    mens.getEchellonage().setEtat(Constantes.ETAT_ENCOURS);
                    q = em.createNamedQuery("YvsMutReglementMensualite.findSumVerse");
                    q.setParameter("echeancier", mens.getEchellonage());
                    q.setParameter("statutMens", Constantes.ETAT_REGLE);
                    q.setParameter("statutReg", Constantes.STATUT_DOC_PAYER);
                    Double re = (Double) q.getSingleResult();
                    re = (re != null) ? re : 0d;
                    if (mens.getEchellonage().getMontant() <= re) {
                        mens.getEchellonage().setEtat(Constantes.ETAT_REGLE);
                    }
                    em.merge(mens.getEchellonage());
                    //update crédit
                    mens.getEchellonage().getCredit().setStatutCredit(mens.getEchellonage().getEtat().charAt(0));
                    em.merge(mens.getEchellonage().getCredit());
                }
            }
            try {
                System.err.println("  Commit de la transaction ");
                utx.commit();
                return true;
            } catch (RollbackException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicMixedException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicRollbackException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            }

            return false;
        } catch (NotSupportedException ex) {
            Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean saveTransactionRetenue(YvsMutOperationCompte op1, YvsMutMouvementCaisse mvtC) {
        try {
            utx.begin();
            if (op1 != null) {
                op1 = (YvsMutOperationCompte) em.merge(op1);
            }
            if (op1 != null) {
                mvtC.setIdExterne(op1.getId());
            }
            if (mvtC != null) {
                em.merge(mvtC);
            }
            try {
                System.err.println("  Commit de la transaction ");
                utx.commit();
                return true;
            } catch (RollbackException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicMixedException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicRollbackException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
            }

            return false;
        } catch (NotSupportedException ex) {
            Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(DaoStateFul.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}

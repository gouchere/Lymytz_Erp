/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.local;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import yvs.dao.Options;
import yvs.entity.commercial.vente.YvsComDocVentes;

/**
 *
 * @param <T>
 * @author Yves
 */
public class Requete<T extends Serializable> {

    protected Class<T> entity;

    public Requete() {

    }

    public Class<T> getEntity() {
        return entity;
    }

    public void setEntity(Class<T> entity) {
        this.entity = entity;
    }

    public T save1(T entity) {
        try {
//            if (ConnexionBD.getInstance().getEntityManager() != null) {
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
//                ConnexionBD.getInstance().getEntityManager().persist(entity);
//                ConnexionBD.getInstance().getEntityManager().flush();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();
//                return entity;
//            }
            if (ConnexionBD.getInstance().getEntityManagerFactory() != null) {
                EntityManager em = ConnexionBD.getInstance().getEntityManagerFactory().createEntityManager();
                em.getTransaction().begin();
                em.persist(entity);
                em.flush();
                em.getTransaction().commit();
                em.close();
                return entity;
            }

        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

    public boolean save(T entite) {
        try {
            if (ConnexionBD.getInstance().getEntityManagerFactory() != null) {
//                if (ConnexionBD.getInstance().getEntityManager().getTransaction().isActive()) {
//                    ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
//                }
                boolean b = false;
//                try {
//                    ConnexionBD.getInstance().getEntityManager().persist(entite);
//                    b = ConnexionBD.getInstance().getEntityManager().contains(entite);
//                } catch (RollbackException ex) {
//                    ConnexionBD.setInstance(null);
//                }
//                if (ConnexionBD.getInstance().getEntityManager().getTransaction().isActive()) {
//                    ConnexionBD.getInstance().getEntityManager().getTransaction().commit();
//                }
                EntityManager em = ConnexionBD.getInstance().getEntityManagerFactory().createEntityManager();
                em.getTransaction().begin();
                em.persist(entite);
                b = em.contains(entite);
                em.getTransaction().commit();
                em.close();
                return b;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return false;
    }

    public T update(T entity) {
        try {
//            if (ConnexionBD.getInstance().getEntityManager() != null) {
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
//                ConnexionBD.getInstance().getEntityManager().merge(entity);
//                ConnexionBD.getInstance().getEntityManager().flush();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();
//                return entity;
//            }

            if (ConnexionBD.getInstance().getEntityManagerFactory() != null) {
                EntityManager em = ConnexionBD.getInstance().getEntityManagerFactory().createEntityManager();
                em.getTransaction().begin();
                em.merge(entity);
                em.flush();
                em.getTransaction().commit();
                em.close();
                return entity;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

    public void delete(T entity) {
        try {
//            if (ConnexionBD.getInstance().getEntityManager() != null) {
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
//                ConnexionBD.getInstance().getEntityManager().merge(entity);
//                ConnexionBD.getInstance().getEntityManager().remove(entity);
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();
//            }
            if (ConnexionBD.getInstance().getEntityManagerFactory() != null) {
                EntityManager em = ConnexionBD.getInstance().getEntityManagerFactory().createEntityManager();
                em.getTransaction().begin();
                em.merge(entity);
                em.remove(entity);
                em.getTransaction().commit();
                em.close();
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void refresh(T entity) {
        try {
            if (ConnexionBD.getInstance().getEntityManagerFactory() != null) {
                EntityManager em = ConnexionBD.getInstance().getEntityManagerFactory().createEntityManager();
                em.refresh(entity);
                em.close();
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void refresh(List<T> entity) {
        try {
            if (ConnexionBD.getInstance().getEntityManagerFactory() != null) {
                EntityManager em = ConnexionBD.getInstance().getEntityManagerFactory().createEntityManager();
                em.refresh(entity);
                em.close();
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public T getLastDoc(String champ1, Object value1, String ordre) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                T result = null;
                String requete = "FROM " + entity.getSimpleName() + " AS d WHERE  d." + champ1 + " =?1 ORDER BY d." + ordre + " DESC";
                result = (T) ConnexionBD.getInstance().getEntityManager().createQuery(requete).setParameter(1, value1).setMaxResults(1).getSingleResult();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();                 
                return result;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

    public T getLastVerst(String champ1, String ordre) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                T result = null;
                String requete = "FROM " + entity.getSimpleName() + " AS d WHERE  d." + champ1 + " IS NOT NULL ORDER BY d." + ordre + " DESC";
                result = (T) ConnexionBD.getInstance().getEntityManager().createQuery(requete).setMaxResults(1).getSingleResult();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();
                return result;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

    public T getOne(Object id) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
                Serializable sb = (Serializable) id;
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                T entite = (T) ConnexionBD.getInstance().getEntityManager().find(entity, sb);
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();                 
                return entite;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

    public T findArticleByCodebarre(String code) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                T entite = (T) ConnexionBD.getInstance().getEntityManager().createNamedQuery("Articles.findByCodebarre").setParameter("codebarre", code).setMaxResults(1).getSingleResult();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();                 
                return entite;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

    public T findOneEntity(String nameQuery, String[] champ, Object[] val) {
        try {
            try {
                if (ConnexionBD.getInstance().getEntityManager() != null) {
//                    ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                    Query qr = ConnexionBD.getInstance().getEntityManager().createNamedQuery(nameQuery);
                    int i = 0;
                    for (String st : champ) {
                        qr.setParameter(st, val[i]);
                        i++;
                    }
                    qr.setMaxResults(1);
                    T re = (T) qr.getSingleResult();
//                    ConnexionBD.getInstance().getEntityManager().getTransaction().commit();

                    return re;
                }
            } catch (NoResultException ex) {
                return null;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

    public List<T> findAllArticle(Object val) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                List<T> result = ConnexionBD.getInstance().getEntityManager().createNamedQuery("Articledepots.findByCodedepot").setParameter("codedepot", val).getResultList();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();                 
                return result;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return new ArrayList<>();
    }

    public List<T> loadNameQueries(String querie, String[] champ, Object[] val) {

        try {
            List<T> result = null;
            if (ConnexionBD.getInstance().getEntityManagerFactory() != null) {
                EntityManager em = ConnexionBD.getInstance().getEntityManagerFactory().createEntityManager();
                em.clear();
                em.getTransaction().begin();
                Query qr = em.createNamedQuery(querie);
                int i = 0;
                for (String st : champ) {
                    qr.setParameter(st, val[i]);
                    i++;
                }
                result = qr.getResultList();
                em.getTransaction().commit();
                em.close();
                return result;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return new ArrayList<>();
    }

    public List<T> loadNameQueries(String querie, String[] champ, Object[] val, int offset, int limit) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
//            ConnexionBD.getInstance().getEntityManager().clear();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                Query qr = ConnexionBD.getInstance().getEntityManager().createNamedQuery(querie);
                int i = 0;
                for (String st : champ) {
                    qr.setParameter(st, val[i]);
                    i++;
                }
                qr.setFirstResult(offset);
                qr.setMaxResults(limit);
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();

                return qr.getResultList();
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return new ArrayList<>();
    }

    public List<T> loadEntity(String querie, String[] champ, Object[] val) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
//            ConnexionBD.getInstance().getEntityManager().clear();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                Query qr = ConnexionBD.getInstance().getEntityManager().createQuery(querie);
                int i = 0;
                for (String st : champ) {
                    qr.setParameter(st, val[i]);
                    i++;
                }
                List<T> re = qr.getResultList();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();

                return re;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return new ArrayList<>();
    }

    public List<T> loadEntity(String querie, String[] champ, Object[] val, int debut, int limit) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
//            ConnexionBD.getInstance().getEntityManager().clear();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                Query qr = ConnexionBD.getInstance().getEntityManager().createQuery(querie);
                int i = 0;
                for (String st : champ) {
                    qr.setParameter(st, val[i]);
                    i++;
                }
                qr.setFirstResult(debut);
                qr.setMaxResults(limit);
                List<T> re = qr.getResultList();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();

                return re;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return new ArrayList<>();
    }

    public Object loadOneByEntity(String querie, String[] champ, Object[] val) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
//            ConnexionBD.getInstance().getEntityManager().clear();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                Query qr = ConnexionBD.getInstance().getEntityManager().createQuery(querie);
                int i = 0;
                for (String st : champ) {
                    qr.setParameter(st, val[i]);
                    i++;
                }
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();

                return qr.getSingleResult();
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return new ArrayList<>();
    }

    public List<T> findAllClient() {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                List<T> result = ConnexionBD.getInstance().getEntityManager().createNamedQuery("Clients.findBySommeil").setParameter("sommeil", false).getResultList();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();

                return result;
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile(ex);
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public List<Object[]> loadDataBySqlQuerie(String query, Options[] lp) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
                List<Object[]> re;
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                Query qr = ConnexionBD.getInstance().getEntityManager().createNativeQuery(query);
                for (Options o : lp) {
                    qr.setParameter(o.getPosition(), o.getValeur());
                }
                try {
                    re = qr.getResultList();
                } catch (NoResultException e) {
                    return null;
                }
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();
                return re;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return new ArrayList<>();
    }

    public List<Long> loadIdTableBySqlQuerie(String query, Options[] lp) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
                List<Long> re;
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                Query qr = ConnexionBD.getInstance().getEntityManager().createNativeQuery(query);
                for (Options o : lp) {
                    qr.setParameter(o.getPosition(), o.getValeur());
                }
                try {
                    re = qr.getResultList();
                } catch (NoResultException e) {
                    return null;
                }
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();

                return re;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return new ArrayList<>();
    }

    public double arrondi(long societe, double valeur) {
        double re = 0;
        try {
            try {
                if (ConnexionBD.getInstance().getEntityManager() != null) {
//                    ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                    Query qr = ConnexionBD.getInstance().getEntityManager().createNativeQuery("select public.arrondi(?,?)");
                    qr.setParameter(1, societe).setParameter(2, valeur);
                    Double dr = (Double) qr.getSingleResult();
//                    ConnexionBD.getInstance().getEntityManager().getTransaction().commit();

                    re = (dr != null) ? dr : 0.0;
                }
            } catch (NoResultException ex) {
                return 0;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return re;
    }

    public double callProcedure(String query, String[] champ, Object[] val) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                Query qr = ConnexionBD.getInstance().getEntityManager().createNativeQuery(query);
                int i = 0;
                for (String st : champ) {
                    qr.setParameter(st, val[i]);
                    i++;
                }
                Double re = (Double) qr.getSingleResult();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();

                return (re != null) ? re : 0;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return 0;
    }

    public Object loadObjectByNameQueries(String query, String[] champ, Object[] val) {
        Object result = null;
        try {
            try {
//                if (ConnexionBD.getInstance().getEntityManager() != null) {
////                    ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
//                    Query qr = ConnexionBD.getInstance().getEntityManager().createNamedQuery(query);
//                    int i = 0;
//                    for (String st : champ) {
//                        qr.setParameter(st, val[i]);
//                        i++;
//                    }
//                    qr.setMaxResults(1);
////                    ConnexionBD.getInstance().getEntityManager().getTransaction().commit();
//                    return qr.getSingleResult();
//                }

                if (ConnexionBD.getInstance().getEntityManagerFactory() != null) {
                    EntityManager em = ConnexionBD.getInstance().getEntityManagerFactory().createEntityManager();
//                    em.getTransaction().begin();
                    Query qr = ConnexionBD.getInstance().getEntityManager().createNamedQuery(query);
                    int i = 0;
                    for (String st : champ) {
                        qr.setParameter(st, val[i]);
                        i++;
                    }
                    qr.setMaxResults(1);
//                    em.getTransaction().commit();
                    result = (Object) qr.getSingleResult();
                    em.close();
                    return result;
                }
            } catch (NoResultException e) {
                return null;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

    public double getByrequeteLibre(String rq, Options[] lp) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                javax.persistence.Query q = ConnexionBD.getInstance().getEntityManager().createNativeQuery(rq);
                for (Options o : lp) {
                    q.setParameter(o.getPosition(), o.getValeur());
                }
                Double d = (Double) q.getSingleResult();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();                 
                return (d != null) ? d : 0;
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return 0;
    }

    public Object findBySqlQuery(String rq, Options[] lp) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
//                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                javax.persistence.Query q = ConnexionBD.getInstance().getEntityManager().createNativeQuery(rq);
                for (Options o : lp) {
                    q.setParameter(o.getPosition(), o.getValeur());
                }
                Object d = (Object) q.getSingleResult();
//                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();                 
                return (d != null) ? d : 0;
            }
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return 0;
    }

    public void requeteLibre(String rq, Options[] lp) {
        try {
            if (ConnexionBD.getInstance().getEntityManager() != null) {
                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                javax.persistence.Query q = ConnexionBD.getInstance().getEntityManager().createNativeQuery(rq);
                for (Options o : lp) {
                    q.setParameter(o.getPosition(), o.getValeur());
                }
                q.executeUpdate();
                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();
            }
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public boolean createLogfile() {
        File logFile = new File("log");
//            final FileWriter file=new FileWriter(new File("log/appslog.txt"));
        if (!logFile.exists()) {
            logFile.mkdirs();
        }
        logFile = new File("log/appslog1.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return logFile.exists();
    }

    public boolean entityManagerIsNull() {
        try {
            return ConnexionBD.getInstance().getEntityManager() == null;
        } catch (Exception ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public YvsComDocVentes equilibreStatutLivraison(YvsComDocVentes facture) {
        if (getEquilibreVente(facture.getId())) {
            facture = (YvsComDocVentes) findOneEntity("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{facture.getId()});
        }
        return facture;

    }

    public boolean getEquilibreVente(long vente) {
        try {
            Boolean dr = null;
            if (ConnexionBD.getInstance().getEntityManager() != null) {
                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                javax.persistence.Query q = ConnexionBD.getInstance().getEntityManager().createNativeQuery("select public.equilibre_vente(?)");
                q.setParameter(1, vente);
                dr = (Boolean) q.getSingleResult();
                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();
            }
            return dr != null ? dr : false;
        } catch (Exception ex) {
            try {
                LogFiles.addLogInFile(ex);
                ConnexionBD.setInstance(null);
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex1) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return false;
    }

}

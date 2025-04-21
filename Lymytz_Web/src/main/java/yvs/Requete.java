/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs;

import java.io.Serializable;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import yvs.dao.Options;

/**
 *
 * @author Lymytz Dowes
 * @param <T>
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

    public boolean save(T entite) {
        try {
            boolean b = false;
            try {
                Dao.getInstance().getEntityManager().persist(entite);
                b = Dao.getInstance().getEntityManager().contains(entite);
            } catch (RollbackException ex) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            }
            return b;
        } catch (Exception ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public T insert(T entity) {
        try {
            Dao.getInstance().getEntityManager().persist(entity);
            Dao.getInstance().getEntityManager().flush();
            return entity;
        } catch (Exception ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public T update(T entity) {
        try {
            Dao.getInstance().getEntityManager().merge(entity);
            Dao.getInstance().getEntityManager().flush();
            return entity;
        } catch (Exception ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean execute(String query) {
        try {
            try (Statement st = Dao.CONNECTION().createStatement()) {
                st.execute(query);
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean requeteLibre(String query, Options[] lp) {
        try {
            Query q = Dao.getInstance().getEntityManager().createNativeQuery(query);
            for (Options o : lp) {
                q.setParameter(o.getPosition(), o.getValeur());
            }
            q.executeUpdate();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public T loadOneByNameQueries(String querie, String[] champ, Object[] val) {
        try {//            
            Query qr = Dao.getInstance().getEntityManager().createNamedQuery(querie);
            int i = 0;
            for (String st : champ) {
                qr.setParameter(st, val[i]);
                i++;
            }
            qr.setFirstResult(0);
            qr.setMaxResults(1);
            try {//                
                return (T) qr.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object loadObjectByNameQueries(String query, String[] champ, Object[] val) {
        try {
            try {
                Query qr = Dao.getInstance().getEntityManager().createNamedQuery(query);
                int i = 0;
                for (String st : champ) {
                    qr.setParameter(st, val[i]);
                    i++;
                }
                qr.setMaxResults(1);
                return qr.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<T> loadNameQueries(String querie, String[] champ, Object[] val) {
        try {
            Query qr = Dao.getInstance().getEntityManager().createNamedQuery(querie);
            int i = 0;
            for (String st : champ) {
                qr.setParameter(st, val[i]);
                i++;
            }//            
            return qr.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<T> loadNameQueries(String querie, String[] champ, Object[] val, int offset, int limit) {
        try {
            Dao.getInstance().getEntityManager().clear();
            Query qr = Dao.getInstance().getEntityManager().createNamedQuery(querie);
            int i = 0;
            for (String st : champ) {
                qr.setParameter(st, val[i]);
                i++;
            }
            qr.setFirstResult(offset);
            qr.setMaxResults(limit);//            
            return qr.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> loadDataBySqlQuerie(String query, Options[] lp) {
        try {
            List<Object[]> re;
            Query qr = Dao.getInstance().getEntityManager().createNativeQuery(query);
            for (Options o : lp) {
                qr.setParameter(o.getPosition(), o.getValeur());
            }
            try {
                re = qr.getResultList();
            } catch (NoResultException e) {
                return null;
            }
            return re;
        } catch (Exception ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]> loadListTableByNameQueries(String querie, String[] champ, Object[] val) {
        try {
            Query qr = Dao.getInstance().getEntityManager().createNamedQuery(querie);
            int i = 0;
            for (String st : champ) {
                qr.setParameter(st, val[i]);
                i++;
            }
            return qr.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object callProcedure(String query, String[] champ, Object[] val) {
        try {
            Query qr = Dao.getInstance().getEntityManager().createNativeQuery(query);
            int i = 0;
            for (String st : champ) {
                qr.setParameter(st, val[i]);
                i++;
            }
            return qr.getSingleResult();
        } catch (Exception ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object callProcedure(String query, Options[] lp) {
        try {
            Query qr = Dao.getInstance().getEntityManager().createNativeQuery(query);
            for (Options o : lp) {
                qr.setParameter(o.getPosition(), o.getValeur());
            }
            return qr.getSingleResult();
        } catch (Exception ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public double arrondi(long societe, double valeur) {
        Double re = (Double) callProcedure("select public.arrondi(?,?)", new Options[]{new Options(societe, 1), new Options(valeur, 2)});
        return re != null ? re : 0;
    }

    public double loadCaVente(long vente) {
        Double re = (Double) callProcedure("select get_ca_vente(?)", new Options[]{new Options(vente, 1)});
        return re != null ? re : 0;
    }

    public boolean getEquilibreVente(Date dateDebut, Date dateFin) {
        Boolean re = (Boolean) callProcedure("select public.equilibre_vente(?,?)", new Options[]{new Options(dateDebut, 1), new Options(dateFin, 2)});
        return re != null ? re : false;
    }

    public Map<String, String> getEquilibreVente(long vente) {
        Map<String, String> re = new HashMap<>();
        Object[] dr = (Object[]) callProcedure("select * from public.equilibre_vente(?)", new Options[]{new Options(vente, 1)});
        re.put("statut_livre", (dr != null ? dr.length > 0 ? dr[0].toString() : "W" : "W"));
        re.put("statut_regle", (dr != null ? dr.length > 1 ? dr[1].toString() : "W" : "W"));
        return re;
    }
}

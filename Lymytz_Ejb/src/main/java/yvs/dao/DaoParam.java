///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.dao;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import javax.ejb.Stateless;
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import yvs.dao.salaire.service.ResultatAction;
//import yvs.entity.param.YvsDictionnaire;
//
///**
// *
// * @author GOUCHERE YVES
// * @param <T>
// *
// */
//@Stateless
//public class DaoParam<T extends Serializable> extends AbstractDao<T> implements InterfaceDaoGeneric<T> {
//
//    String entityName;
//
//    public DaoParam() {
//    }
//
//    @Override
//    public EntityManager getEntityManager() {
//        return em;
//    }
//
//    //modifier le titre dans le dictionnaire
//    public void updateTitreDico(String titre, String oldLibele, String newLibele) {
////        Query qu = getEntityManager().createNamedQuery("YvsDictionnaire.findBylib");
////        qu.setParameter("lib", oldLibele);
////        YvsDictionnaire r = (YvsDictionnaire) qu.setMaxResults(1).getSingleResult();
////        System.out.println("Avant refresh "+r.getLibele());
//        String query = "UPDATE yvs_dictionnaire SET libele=? WHERE libele=?";
//        Query q = getEntityManager().createNativeQuery(query);
//        q.setParameter("1", newLibele);
//        q.setParameter("2", oldLibele);
//        q.executeUpdate();
////        r.setAuthor("JEAN");
////        getEntityManager().refresh(r);
////        getEntityManager().flush();
////        System.out.println("Après refresh "+r.getLibele());
//    }
//    //charger le dictionnaire par une requête sql 
//
//    public List<YvsDictionnaire> loadAllEntity1() {
//        List<YvsDictionnaire> result = new ArrayList<>();
//        Iterator it = getEntityManager().createNativeQuery("SELECT d.id, d.titre,d.libele,d.symbole,"
//                                                           + "d.author,d.lastauthor,d.datesave,"
//                                                           + "d.lastdatesave From yvs_dictionnaire d").getResultList().iterator();
//        while (it.hasNext()) {
//            Object[] tuple = (Object[]) it.next();
//            long id = ((Long) tuple[0]);
//            String titre = (String) tuple[1];
//            String lib = (String) tuple[2];
//            String symb = (String) tuple[3];
//            String auth = (String) tuple[4];
//            String lastAth = (String) tuple[5];
//            Date ds = (Date) tuple[6];
//            Date ds1 = (Date) tuple[7];
//            YvsDictionnaire d = new YvsDictionnaire(titre, lib);
//            d.setId(id);           
//            result.add(d);
//        }
//        return result;
//    }            
//
//    public void testEm() {
//    }
//
//    @Override
//    public EntityManager getEntityManager(String EM) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public ResultatAction checkEntity(T en) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//}

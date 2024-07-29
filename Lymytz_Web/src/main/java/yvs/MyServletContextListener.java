/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.entity.utils.ReferenceService;

/**
 *
 * @author Lymytz Dowes
 */
public class MyServletContextListener implements ServletContextListener {

    @EJB
    DaoInterfaceLocal dao;

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        Logger.getLogger(MyServletContextListener.class.getName()).log(Level.INFO, "CONTEXT INITIALISED ");
        String query = "UPDATE yvs_com_doc_ventes SET action = null WHERE action IS NOT NULL";
        dao.requeteLibre(query, new Options[]{});
    }

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        Logger.getLogger(MyServletContextListener.class.getName()).log(Level.INFO, "CONTEXT DESTROYED ");
        ReferenceService.clear();
    }
}

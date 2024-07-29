/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lymytz.ws;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Lymytz Dowes
 */
@javax.ws.rs.ApplicationPath("ws")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        // following code can be used to customize Jersey 1.x JSON provider:
        try {
            Class jacksonProvider = Class.forName("org.codehaus.jackson.jaxrs.JacksonJsonProvider");
            resources.add(jacksonProvider);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(lymytz.ws.GenericResource.class);
        resources.add(lymytz.ws.base.GenericResource.class);
        resources.add(lymytz.ws.base.v1.GenericResource.class);
        resources.add(lymytz.ws.commercial.GenericResource.class);
        resources.add(lymytz.ws.commercial.dashboard.GenericResource.class);
        resources.add(lymytz.ws.commercial.v1.GenericResource.class);
        resources.add(lymytz.ws.compta.GenericResource.class);
        resources.add(lymytz.ws.compta.dashboard.GenericResource.class);
        resources.add(lymytz.ws.compta.v1.GenericResource.class);
        resources.add(lymytz.ws.dashboard.GenericResource.class);
        resources.add(lymytz.ws.grh.GenericResource.class);
        resources.add(lymytz.ws.market.GenericResource.class);
        resources.add(lymytz.ws.param.GenericResource.class);
        resources.add(lymytz.ws.param.v1.GenericResource.class);
        resources.add(lymytz.ws.validation.GenericResource.class);
        resources.add(lymytz.ws.validation.v1.GenericResource.class);
    }
    
}

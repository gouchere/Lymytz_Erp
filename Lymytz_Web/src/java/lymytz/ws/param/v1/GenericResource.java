/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws.param.v1;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.param.YvsSocietesAvis;
import yvs.service.IEntitySax;
import yvs.service.param.IYvsSocietesAvis;

/**
 *
 * @author hp Elite 8300
 */
@Path("services/parametrage/v1")
@RequestScoped
public class GenericResource extends lymytz.ws.param.GenericResource {

    IEntitySax IEntitiSax = new IEntitySax();
    /**
     * BEGIN AVIS SOCIETE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_societe_avis")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsSocietesAvis> saveArticleAvis(YvsSocietesAvis entity) {
        try {
            IYvsSocietesAvis impl = (IYvsSocietesAvis) IEntitiSax.createInstance("IYvsSocietesAvis", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_societe_avis")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsSocietesAvis> updateArticleAvis(YvsSocietesAvis entity) {
        try {
            IYvsSocietesAvis impl = (IYvsSocietesAvis) IEntitiSax.createInstance("IYvsSocietesAvis", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_societe_avis")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsSocietesAvis> deleteArticleAvis(YvsSocietesAvis entity) {
        try {
            IYvsSocietesAvis impl = (IYvsSocietesAvis) IEntitiSax.createInstance("IYvsSocietesAvis", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END AVIS SOCIETE**/
}

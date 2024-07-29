/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.pdf.codec.Base64;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.Options;
import yvs.dao.Util;
import yvs.dao.salaire.service.ResultatAction;
import yvs.dao.services.compta.ServiceComptabilite;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseArticleCategorieComptableTaxe;
import yvs.entity.base.YvsBaseArticlePoint;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseConditionnementPoint;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBasePointLivraison;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComTaxeContenuVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.grh.activite.YvsGrhFraisMission;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsBaseTarifPointLivraison;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.YvsSocietesConnexion;
import yvs.entity.param.workflow.YvsWorkflowAutorisationValidDoc;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.production.base.YvsBaseArticleSite;
import yvs.entity.produits.YvsBaseArticleContenuPack;
import yvs.entity.produits.YvsBaseArticleDescription;
import yvs.entity.produits.YvsBaseArticlePack;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.entity.synchro.YvsSynchroServeurs;
import yvs.entity.synchro.YvsSynchroServeursVersion;
import yvs.entity.synchro.YvsSynchroTables;
import yvs.entity.tiers.YvsBaseVisiteur;
import yvs.entity.tiers.YvsBaseVisiteurPointLivraison;
import yvs.entity.users.YvsAutorisationRessourcesPage;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsNiveauUsers;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.entity.utils.ReferenceService;
import yvs.init.Initialisation;
import yvs.service.IEntitySax;
import yvs.service.UtilRebuild;
import yvs.util.Constantes;
import yvs.util.MdpUtil;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;

/**
 * REST Web Service
 *
 * @author Lymytz Dowes
 */
@Path("services")
@RequestScoped
public class GenericResource {

    @Context
    protected UriInfo context;
    @Context
    protected ServletContext resource;
    @EJB
    protected DaoInterfaceWs dao;

    @Resource(name = "lymytz_erp", mappedName = "lymytz_erp")
    protected DataSource ds;

    protected String nameQueri;
    protected String[] champ;
    protected Object[] val;

    protected double totalPaye;
    protected DateFormat dm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    protected DateFormat hf = new SimpleDateFormat("HH:mm:ss");
    protected DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    protected DateFormat dfXml = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    protected DateFormat dj = new SimpleDateFormat("dddd", Locale.FRENCH);
    protected final String FILE_SEPARATOR = System.getProperty("file.separator");
    protected final String USER_HOME = System.getProperty("user.home");

    protected static Gson gSon = new GsonBuilder()
            .serializeNulls()
            //            .setLenient()
            .setDateFormat("dd-MM-yyyy HH:mm:ss").create();

    protected IEntitySax IEntitiSax = new IEntitySax();

    static YvsUsers onlineUsers;
    static YvsGrhTrancheHoraire onlineTranche;
    static YvsBaseCategorieComptable onlineCategorie;
    static YvsBaseCategorieClient onlineTarifaire;
    static YvsBasePlanComptable onlineCompte;
    static YvsComClient defaultClient;
    static YvsSocietes onlineSociete;

    static YvsSocietes currentScte;
    static YvsAgences currentAgence;
    static YvsUsersAgence currentUsers;
    static YvsBaseDepots currentDepot;
    static YvsBasePointVente currentPoint;
    static YvsBaseExercice currentExo;

    protected YvsNiveauUsers currentNiveau;

    protected String PATH_PHOTO;

    protected String[] CHAMP_ARTICLE = new String[]{};
    protected Object[] VAL_ARTICLE = new Object[]{};
    protected String QUERY_ARTICLE;
    protected Long COUNT_ARTICLE;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    public String getRemotePath() {
        try {
            String path = "/resources/lymytz/documents/";
            PATH_PHOTO = resource.getContextPath() + path;
            return resource.getRealPath(path) + FILE_SEPARATOR;
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getLocalPath(YvsSocietes societe) {
        try {
            if (societe != null ? societe.getId() > 0 : false) {
                if (!asString(societe.getCodeAbreviation())) {
                    societe = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{societe.getId()});
                }
                StringBuilder sb = new StringBuilder(USER_HOME);
                sb.append(FILE_SEPARATOR).append("lymytz");
                if (societe.getCodeAbreviation() != null) {
                    sb.append(FILE_SEPARATOR).append(societe.getCodeAbreviation().replace(" ", "")).append(FILE_SEPARATOR);
                }
                File file = new File(sb.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                //chemin des documents d'entreprise
                String documents = sb.append("documents").toString();
                file = new File(documents);
                if (!file.exists()) {
                    file.mkdirs();
                }
                return documents + FILE_SEPARATOR;
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //copie le fichier inputStream dans le repertoire dest sous le nom fileName
    public String copyFile(String fileName, String path, InputStream in) throws IOException {
        String destination = null;
        try {
            File doc = new File(path);
            if (!doc.exists()) {
                doc.mkdirs();
            }
            if (!doc.canWrite()) {
                doc.setWritable(true);
            }
            destination = path + FILE_SEPARATOR + fileName;
            File f = new File(destination);
            if (!f.exists()) {
                f.createNewFile();
                if (!f.canWrite()) {
                    f.setWritable(true);
                }
            }
            try (OutputStream out = new FileOutputStream(f)) {
                int read = 0;
                byte[] bytes = new byte[1024];
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
            }
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "---- Impossible de créer le fichier de restauration....", e);
        }
        return destination;
    }

    @POST
    @Path("obtains")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String obtains(@HeaderParam("societe") String societe, @HeaderParam("prefixe") String prefixe) {
        return ReferenceService.nextValue(Long.valueOf(societe), prefixe, dao);
    }

    @GET
    @Path("online")
    @Produces(MediaType.TEXT_PLAIN)
    public String online() {
        try {
            YvsAgences agence = new YvsAgences(3015L, "AGENCE");
            YvsUsers users = new YvsUsers(2549L, "MEG", "USERS");
            YvsUsersAgence ua = new YvsUsersAgence(users, agence);
            String result = new JsonString(ua).value();
            try {
                ua = gSon.fromJson(result, YvsUsersAgence.class);
            } catch (JsonSyntaxException ex) {

            }
            if (!asString(PATH_PHOTO)) {
                getRemotePath();
            }
            return PATH_PHOTO;
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("serverOnline")
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean serverOnline() {
//        System.err.println(" liste ws online-------");
        return true;
    }

    @POST
    @Path("notify_online")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean notifyOnline(@HeaderParam("adresse") String adresse) {
        try {
            YvsSynchroServeursVersion y = (YvsSynchroServeursVersion) dao.loadOneByNameQueries("YvsSynchroServeursVersion.findByAdresse", new String[]{"adresse"}, new Object[]{adresse});
            if (y != null ? y.getId() > 0 : false) {
                y.setDateUpdate(new Date());
                dao.update(y);
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @GET
    @Path("onlineSocietes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsSocietes> onlineSocietes() {
        try {
            return dao.loadNameQueries("YvsSocietes.findByOnline", new String[]{"venteOnline"}, new Object[]{true});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("findSocieteById")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsSocietes> findSocieteById(@HeaderParam("ids") String ids) {
        try {
            if (asString(ids)) {
                List<Long> values = convertArrayStringToLong(ids.split("-"));
                if (values.isEmpty()) {
                    values.add(-1L);
                }
                return dao.loadNameQueries("YvsSocietes.findByIds", new String[]{"ids"}, new Object[]{values});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("findSocieteByNotId")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsSocietes> findSocieteByNotId(@HeaderParam("ids") String ids) {
        try {
            if (asString(ids)) {
                List<Long> values = convertArrayStringToLong(ids.split("-"));
                if (values.isEmpty()) {
                    values.add(-1L);
                }
                return dao.loadNameQueries("YvsSocietes.findByNotIdsVenteOnline", new String[]{"ids", "venteOnline"}, new Object[]{values, true});
            } else {
                return dao.loadNameQueries("YvsSocietes.findByOnline", new String[]{"venteOnline"}, new Object[]{true});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("currentSociete")
    @Produces(MediaType.APPLICATION_JSON)
    public YvsSocietes currentSociete() {
        try {
            return (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findByGlp", new String[]{"forGlp"}, new Object[]{true});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("currentUsers")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String currentUsers(@HeaderParam("societe") String societe) {
        try {

            if (onlineUsers != null ? onlineUsers.getId() < 1 : true) {
                loadUsers(societe);
            }
            if (onlineUsers != null ? onlineUsers.getId() > 0 : false) {
                return onlineUsers.getId() + "";
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "-1";
    }

    @POST
    @Path("currentTranche")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String currentTranche(@HeaderParam("societe") String societe) {
        try {
            if (onlineTranche != null ? onlineTranche.getId() < 1 : true) {
                loadTranche(societe);
            }
            if (onlineTranche != null ? onlineTranche.getId() > 0 : false) {
                return onlineTranche.getId() + "";
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "-1";
    }

    @POST
    @Path("returnTaxes")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON})
    public List<YvsBaseTaxes> returnTaxes(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBaseTaxes.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.
                    getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnGroupes")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON})
    public List<YvsBaseGroupesArticle> returnGroupes(@HeaderParam("societe") String societe, @HeaderParam("service") String service
    ) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                return dao.loadNameQueries("YvsBaseArticles.findGroupeBySociete", new String[]{"societe", "typeService"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), service.charAt(0)});
            } else {
                return dao.loadNameQueries("YvsBaseArticles.findGroupeByOnline", new String[]{"venteOnline", "typeService"}, new Object[]{true, service.charAt(0)});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnParentGroupes")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON})
    public List<YvsBaseGroupesArticle> returnParentGroupes(@HeaderParam("societe") String societe
    ) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                return dao.loadNameQueries("YvsBaseGroupesArticle.findParentActif", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            } else {
                return dao.loadNameQueries("YvsBaseGroupesArticle.findParentVenteOnline", new String[]{"venteOnline"}, new Object[]{true});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnSousGroupes")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON})
    public List<YvsBaseGroupesArticle> returnSousGroupes(@HeaderParam("societe") String societe
    ) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                return dao.loadNameQueries("YvsBaseGroupesArticle.findSousActif", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            } else {
                return dao.loadNameQueries("YvsBaseGroupesArticle.findSousVenteOnline", new String[]{"venteOnline"}, new Object[]{true});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnGroupesByParent")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public List<YvsBaseGroupesArticle> returnGroupesByParent(@HeaderParam("parent") String parent
    ) {
        try {
            return dao.loadNameQueries("YvsBaseGroupesArticle.findByGroupeParent", new String[]{"groupeParent"}, new Object[]{new YvsBaseGroupesArticle(Long.valueOf(parent))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnUnites")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON})
    public List<YvsBaseUniteMesure> returnUnites(@HeaderParam("societe") String societe
    ) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                return dao.loadNameQueries("YvsBaseUniteMesure.findByType", new String[]{"societe", "type"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), Constantes.UNITE_QUANTITE});
            } else {
                return dao.loadNameQueries("YvsBaseUniteMesure.findByVenteOnline", new String[]{"venteOnline", "type"}, new Object[]{true, Constantes.UNITE_QUANTITE});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("searchArticles")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public YvsBaseArticles searchArticles(@HeaderParam("reference") String id
    ) {
        try {
            return (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{Long.valueOf(id)});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnConditionnements")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public List<YvsBaseConditionnement> returnConditionnements(@HeaderParam("article") String article) {
        try {
            return dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnUnitesPoint")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public List<YvsBaseConditionnementPoint> returnUnitesPoint(@HeaderParam("article") String article
    ) {
        try {
            return dao.loadNameQueries("YvsBaseConditionnementPoint.findVenteByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("currentClient")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public YvsComClient currentClient(@HeaderParam("societe") String societe, @HeaderParam("visiteur") String visiteur) {
        try {
            if ((asString(visiteur) ? Long.valueOf(visiteur) > 0 : false) && (asString(societe) ? Long.valueOf(societe) > 0 : false)) {
                YvsBaseVisiteur y = (YvsBaseVisiteur) dao.loadOneByNameQueries("YvsBaseVisiteur.findById", new String[]{"id"}, new Object[]{Long.valueOf(visiteur)});
                YvsComClient client = null;
                if (y != null ? y.getId() > 0 : false) {
                    client = (YvsComClient) dao.loadOneByNameQueries("YvsBaseVisiteurClient.findClientByVisiteurSociete", new String[]{"societe", "visiteur"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), y});
                    if (client != null ? client.getId() < 1 : true && (y.getVille() != null ? y.getVille().getId() > 0 : false)) {
                        client = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findDefautVille", new String[]{"societe", "ville"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), y.getVille()});
                    }
                    if (client != null ? client.getId() < 1 : true && (y.getPays() != null ? y.getPays().getId() > 0 : false)) {
                        client = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findDefautPays", new String[]{"societe", "pays"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), y.getPays()});
                    }
                }
                if (client != null ? client.getId() < 1 : true) {
                    client = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findDefaut", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
                }
                return client;
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnCategoriesComptable")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsBaseCategorieComptable> returnCategoriesComptable(@HeaderParam("societe") String societe) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                return dao.loadNameQueries("YvsBaseCategorieComptable.findByNature", new String[]{"societe", "nature"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), Constantes.VENTE});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnCategoriesComptableNotIds")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsBaseCategorieComptable> returnCategoriesComptableNotIds(@HeaderParam("societe") String societe, @HeaderParam("ids") String ids) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                List<Long> value = new ArrayList<>();
                if (asString(ids)) {
                    for (String id : ids.split(",")) {
                        value.add(Long.valueOf(id));
                    }
                }
                if (value.isEmpty()) {
                    value.add(-1L);
                }
                return dao.loadNameQueries("YvsBaseCategorieComptable.findAllNotIds", new String[]{"societe", "ids"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), value});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnPointLivraison")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public List<YvsBaseVisiteurPointLivraison> returnPointLivraison(@HeaderParam("visiteur") String visiteur, @HeaderParam("ville") String ville
    ) {
        try {
            return dao.loadNameQueries("YvsBaseVisiteurPointLivraison.findByVisiteurVille", new String[]{"visiteur", "ville"}, new Object[]{new YvsBaseVisiteur(Long.valueOf(visiteur)), new YvsDictionnaire(Long.valueOf(ville))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnPointRetrait")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public List<YvsBasePointLivraison> returnPointRetrait(@HeaderParam("societe") String societe, @HeaderParam("ville") String ville
    ) {
        try {
            return dao.loadNameQueries("YvsBasePointLivraison.findBySocieteVille", new String[]{"societe", "ville"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), new YvsDictionnaire(Long.valueOf(ville))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnTarifLieux")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public YvsBaseTarifPointLivraison returnTarifLieux(@HeaderParam("societe") String societe, @HeaderParam("lieux") String lieux
    ) {
        try {
            return (YvsBaseTarifPointLivraison) dao.loadOneByNameQueries("YvsBaseTarifPointLivraison.findByLieuxSociete", new String[]{"societe", "lieux"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), new YvsDictionnaire(Long.valueOf(lieux))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnServices")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON})
    public List<Character> returnServices(@HeaderParam("societe") String societe) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                return dao.loadListByNameQueries("YvsBaseArticles.findServiceBySociete", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            } else {
                return dao.loadListByNameQueries("YvsBaseArticles.findServiceByOnline", new String[]{"venteOnline"}, new Object[]{true});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("returnLieux")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON})
    public List<YvsDictionnaire> returnLieux() {
        try {
            return dao.loadNameQueries("YvsDictionnaire.findByActif", new String[]{"actif"}, new Object[]{true});
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnLieuxNotIds")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON})
    public List<YvsDictionnaire> returnLieuxNotIds(@HeaderParam("ids") String ids) {
        try {
            List<Long> value = new ArrayList<>();
            if (asString(ids)) {
                for (String id : ids.split(",")) {
                    value.add(Long.valueOf(id));
                }
            }
            if (value.isEmpty()) {
                value.add(-1L);
            }
            return dao.loadNameQueries("YvsDictionnaire.findByActifNotIds", new String[]{"actif", "ids"}, new Object[]{true, value});
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("countArticles")
    @Produces({MediaType.TEXT_PLAIN})
    @Consumes({MediaType.TEXT_PLAIN})
    public String countArticles(@HeaderParam("societe") String societe, @HeaderParam("reference") String reference, @HeaderParam("service") String service, @HeaderParam("groupe") String groupe, @HeaderParam("parent") String parent
    ) {
        try {
            PaginatorResult<YvsBaseArticles> paginator = new PaginatorResult<>();
            paginator.addParam(new ParametreRequete("s.refArt", "reference", null, "=", "AND"));
            if (asString(reference)) {
                ParametreRequete p = new ParametreRequete(null, "reference", reference.toUpperCase() + "%", "=", "AND");
                p.getOtherExpression().add(new ParametreRequete("UPPER(s.refArt)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(s.designation)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
                paginator.addParam(p);
            }
            paginator.addParam(new ParametreRequete("s.typeService", "service", null, "=", "AND"));
            if (asString(service)) {
                paginator.addParam(new ParametreRequete("s.typeService", "service", service.charAt(0), "=", "AND"));
            }
            paginator.addParam(new ParametreRequete("s.groupe", "groupe", null, "=", "AND"));
            if (asString(groupe)) {
                if (asString(parent) ? Boolean.valueOf(parent) : false) {
                    List<Integer> ids = dao.loadNameQueries("YvsBaseGroupesArticle.findIdByParent", new String[]{"parent"}, new Object[]{new YvsBaseGroupesArticle(Long.valueOf(groupe))});
                    if (ids.isEmpty()) {
                        ids.add(-1);
                    }
                    ParametreRequete p = new ParametreRequete(null, "groupe", ids, "IN", "AND");
                    p.getOtherExpression().add(new ParametreRequete("s.groupe.id", "groupes", ids, "IN", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("s.groupe.id", "groupe", Integer.valueOf(groupe), "=", "OR"));
                    paginator.addParam(p);
                } else {
                    paginator.addParam(new ParametreRequete("s.groupe.id", "groupe", convertArrayStringToInt(groupe.split("-")), "IN", "AND"));
                }
            }
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                paginator.addParam(new ParametreRequete("s.famille.societe", "societe", new YvsSocietes(Long.valueOf(societe)), "=", "AND"));
            } else {
                paginator.addParam(new ParametreRequete("s.famille.societe.venteOnline", "venteOnline", true, "=", "AND"));
            }
            QUERY_ARTICLE = !paginator.getParams().isEmpty() ? paginator.buildDynamicQuery(paginator.getParams(), "SELECT ? FROM YvsBaseArticles s WHERE") : paginator.buildDynamicQuery(paginator.getParams(), "SELECT ? FROM YvsBaseArticles s");
            CHAMP_ARTICLE = paginator.getChamp();
            VAL_ARTICLE = paginator.getVal();
            String nameQuery = QUERY_ARTICLE.replace("?", "COUNT(s)");
            COUNT_ARTICLE = (Long) dao.loadObjectByEntity(nameQuery, CHAMP_ARTICLE, VAL_ARTICLE);
            return (COUNT_ARTICLE != null ? COUNT_ARTICLE : 0) + "";
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnArticles")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public List<YvsBaseArticles> returnArticles(@HeaderParam("societe") String societe, @HeaderParam("reference") String reference, @HeaderParam("service") String service, @HeaderParam("groupe") String groupe, @HeaderParam("parent") String parent, @HeaderParam("offset") String offset, @HeaderParam("limit") String limit, @HeaderParam("init") String init
    ) {
        try {
            List<YvsBaseArticles> result = new ArrayList<>();
            if (!asString(QUERY_ARTICLE) || Boolean.valueOf(init)) {
                countArticles(societe, reference, service, groupe, parent);
            }
            if (COUNT_ARTICLE != null ? COUNT_ARTICLE > 0 : false) {
                String nameQuery = QUERY_ARTICLE.replace("?", "s") + " ORDER BY s.designation";
                result.add(new YvsBaseArticles(-1000L, "?COUNT", "" + COUNT_ARTICLE));
                if (Integer.valueOf(limit) > 0) {
                    result.addAll(dao.loadEntity(nameQuery, CHAMP_ARTICLE, VAL_ARTICLE, Integer.valueOf(offset), Integer.valueOf(limit)));
                } else {
                    result.addAll(dao.loadEntity(nameQuery, CHAMP_ARTICLE, VAL_ARTICLE));
                }
                return result;
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("countUnitesPoint")
    @Produces({MediaType.TEXT_PLAIN})
    @Consumes({MediaType.TEXT_PLAIN})
    public String countUnitesPoint(@HeaderParam("article") String article
    ) {
        try {
            Long count = (Long) dao.loadObjectByNameQueries("YvsBaseConditionnementPoint.countVenteByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
            return count != null ? count + "" : "0";
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "0";
    }

    @POST
    @Path("returnCriteres")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public List<Object> returnCriteres(@HeaderParam("article") String article
    ) {
        try {
            return dao.loadNameQueries("Articlecritere.findVenteByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnArticlePoint")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public List<YvsBaseArticlePoint> returnArticlePoint(@HeaderParam("article") String article
    ) {
        try {
            return dao.loadNameQueries("YvsBaseArticlePoint.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnArticleDescription")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public List<YvsBaseArticleDescription> returnArticleDescription(@HeaderParam("article") String article
    ) {
        try {
            return dao.loadNameQueries("YvsBaseArticleDescription.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnArticlePack")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public List<YvsBaseArticlePack> returnArticlePack(@HeaderParam("article") String article
    ) {
        try {
            return dao.loadNameQueries("YvsBaseArticlePack.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnArticleContenuPack")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public List<YvsBaseArticleContenuPack> returnArticleContenuPack(@HeaderParam("pack") String pack
    ) {
        try {
            return dao.loadNameQueries("YvsBaseArticleContenuPack.findByPack", new String[]{"pack"}, new Object[]{new YvsBaseArticlePack(Long.valueOf(pack))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("mostVendu")
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.APPLICATION_JSON})
    public List<YvsBaseArticles> mostVendu(@HeaderParam("societe") String societe
    ) {
        try {
            List<Long> ids = new ArrayList<>();
            String query = "SELECT y.article FROM yvs_com_contenu_doc_vente y INNER JOIN yvs_com_doc_ventes d ON d.id = y.doc_vente INNER JOIN yvs_com_entete_doc_vente e ON e.id = d.entete_doc INNER JOIN yvs_agences a ON a.id = e.agence INNER JOIN yvs_societes s On a.societe = s.id"
                    + " WHERE a.societe = ?  GROUP BY y.article ORDER BY (SELECT COUNT(y1.id) FROM yvs_com_contenu_doc_vente y1 WHERE y1.article = y.article)";
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                ids = dao.loadListBySqlQuery(query, new Options[]{new Options(Long.valueOf(societe), 1)});
            } else {
                query = query.replace("a.societe = ?", "s.vente_online = ?");
                ids = dao.loadListBySqlQuery(query, new Options[]{new Options(true, 1)});
            }
            if (ids != null ? ids.isEmpty() : true) {
                ids = new ArrayList<>();
                ids.add(0L);
            }
            return dao.loadNameQueries("YvsBaseArticles.findByIds", new String[]{"ids"}, new Object[]{ids}, 0, 10);

        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("countVentes")
    @Produces({MediaType.TEXT_PLAIN})
    @Consumes({MediaType.TEXT_PLAIN})
    public String countVentes(@HeaderParam("visiteur") String visiteur
    ) {
        try {
            List<Long> clients = dao.loadNameQueries("YvsBaseVisiteurClient.findIdClientByVisiteur", new String[]{"visiteur"}, new Object[]{new YvsBaseVisiteur(Long.valueOf(visiteur))});
            if (clients != null ? !clients.isEmpty() : false) {
                return (Long) dao.loadObjectByNameQueries("YvsComDocVentes.countByClients", new String[]{"clients"}, new Object[]{clients}) + "";
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "0";
    }

    @POST
    @Path("returnVentes")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public List<YvsComDocVentes> returnVentes(@HeaderParam("visiteur") String visiteur, @HeaderParam("offset") String offset, @HeaderParam("limit") String limit, @HeaderParam("init") String init
    ) {
        try {
            List<YvsComDocVentes> result = new ArrayList<>();
            if (Boolean.valueOf(init)) {
                String count = countVentes(visiteur);
                result.add(new YvsComDocVentes(-1000L, "?COUNT", count, ""));
            }
            List<Long> clients = dao.loadNameQueries("YvsBaseVisiteurClient.findIdClientByVisiteur", new String[]{"visiteur"}, new Object[]{new YvsBaseVisiteur(Long.valueOf(visiteur))});
            if (clients != null ? !clients.isEmpty() : false) {
                result.addAll(dao.loadNameQueries("YvsComDocVentes.findByClients", new String[]{"clients"}, new Object[]{clients}, Integer.valueOf(offset), Integer.valueOf(limit)));
            }
            return result;
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnContent")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public YvsComContenuDocVente returnContent(@HeaderParam("point") String point, @HeaderParam("categorie") String categorie, @HeaderParam("date_doc") String date_doc, @HeaderParam("client") String client, @HeaderParam("article") String article, @HeaderParam("unite") String unite, @HeaderParam("quantite") String quantite, @HeaderParam("prix") String prix) {
        YvsComContenuDocVente y = new YvsComContenuDocVente();
        try {
            y.setArticle(new YvsBaseArticles(Long.valueOf(article)));
            y.setQuantite(Double.valueOf(quantite));
            y.setPrix(Double.valueOf(prix));
            y.setConditionnement(new YvsBaseConditionnement(Long.valueOf(unite)));
            double remise = dao.getRemiseVente(Long.valueOf(article), y.getQuantite(), y.getPrix(), Long.valueOf(client), Long.valueOf(point), df.parse(date_doc), Long.valueOf(unite));
            y.setRemise(remise);
            List<YvsComTaxeContenuVente> taxes = buildTaxes(Long.valueOf(article), y.getQuantite(), y.getPrix(), remise, Long.valueOf(categorie));
            if (taxes != null ? !taxes.isEmpty() : false) {
                y.setTaxes(taxes);
            }
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return y;
    }

    @POST
    @Path("returnContents")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public List<YvsComContenuDocVente> returnContents(@HeaderParam("facture") String facture
    ) {
        try {
            return dao.loadNameQueries("YvsComContenuDocVente.findByFacture", new String[]{"docVente"}, new Object[]{new YvsComDocVentes(Long.valueOf(facture))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnTTC")
    @Produces({MediaType.TEXT_PLAIN})
    @Consumes({MediaType.TEXT_PLAIN})
    public String returnTTC(@HeaderParam("facture") String facture
    ) {
        try {
            return (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findTTCByFacture", new String[]{"docVente"}, new Object[]{new YvsComDocVentes(Long.valueOf(facture))}) + "";
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "0";
    }

    @POST
    @Path("searchVente")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public YvsComDocVentes searchVente(@HeaderParam("numero") String numero
    ) {
        try {
            return (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{Long.valueOf(numero)});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("insertVisiteur")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public YvsBaseVisiteur insertVisiteur(@HeaderParam("civilite") String civilite, @HeaderParam("nom") String nom, @HeaderParam("prenom") String prenom, @HeaderParam("email") String email, @HeaderParam("adresse") String adresse, @HeaderParam("telephone") String telephone, @HeaderParam("cni") String cni, @HeaderParam("photo") String photo, @HeaderParam("pays") String pays, @HeaderParam("ville") String ville
    ) {
        try {
            YvsBaseVisiteur entity = new YvsBaseVisiteur(telephone);
            entity.setCivilite(civilite);
            entity.setNom(nom);
            entity.setPrenom(prenom);
            entity.setPhoto(photo);
            entity.setEmail(email);
            entity.setAdresse(adresse);
            entity.setCni(cni);
            if (asString(pays) ? Long.valueOf(pays) > 0 : false) {
                entity.setPays(new YvsDictionnaire(Long.valueOf(pays)));
            }
            if (asString(ville) ? Long.valueOf(ville) > 0 : false) {
                entity.setVille(new YvsDictionnaire(Long.valueOf(ville)));
            }
            return (YvsBaseVisiteur) dao.save1(entity);
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("updateVisiteur")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public YvsBaseVisiteur updateVisiteur(@HeaderParam("id") String id, @HeaderParam("civilite") String civilite, @HeaderParam("nom") String nom, @HeaderParam("prenom") String prenom, @HeaderParam("email") String email, @HeaderParam("adresse") String adresse, @HeaderParam("telephone") String telephone, @HeaderParam("cni") String cni, @HeaderParam("photo") String photo, @HeaderParam("pays") String pays, @HeaderParam("ville") String ville
    ) {
        try {
            YvsBaseVisiteur entity = (YvsBaseVisiteur) dao.loadOneByNameQueries("YvsBaseVisiteur.findById", new String[]{"id"}, new Object[]{Long.valueOf(id)});
            if (entity != null ? entity.getId() < 1 : true) {
                return insertVisiteur(civilite, nom, prenom, email, adresse, telephone, cni, photo, pays, ville);
            } else {
                if (entity != null) {
                    entity.setTelephone(telephone);
                    entity.setCivilite(civilite);
                    entity.setNom(nom);
                    entity.setPrenom(prenom);
                    entity.setPhoto(photo);
                    entity.setEmail(email);
                    entity.setAdresse(adresse);
                    entity.setCni(cni);
                    if (asString(pays) ? Long.valueOf(pays) > 0 : false) {
                        entity.setPays(new YvsDictionnaire(Long.valueOf(pays)));
                    }
                    if (asString(ville) ? Long.valueOf(ville) > 0 : false) {
                        entity.setVille(new YvsDictionnaire(Long.valueOf(ville)));
                    }
                    dao.update(entity);
                    return entity;
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("searchVisiteur")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public YvsBaseVisiteur searchVisiteur(@HeaderParam("nom") String nom, @HeaderParam("telephone") String telephone
    ) {
        try {
            if (asString(nom) && asString(telephone)) {
                YvsBaseVisiteur y = (YvsBaseVisiteur) dao.loadOneByNameQueries("YvsBaseVisiteur.findByNomsPhone", new String[]{"noms", "telephone"}, new Object[]{nom, telephone});
                if (y != null ? y.getId() > 0 : false) {
                    return y;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("insertPointLivraison")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public YvsBaseVisiteurPointLivraison insertPointLivraison(@HeaderParam("visiteur") String visiteur, @HeaderParam("secteur") String secteur, @HeaderParam("libelle") String libelle
    ) {
        try {
            if (asString(visiteur) && asString(secteur) && asString(libelle)) {
                YvsDictionnaire dico = (YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{Long.valueOf(secteur)});
                if (dico != null ? dico.getId() < 1 : true) {
                    return null;
                }
                YvsBaseVisiteur clt = (YvsBaseVisiteur) dao.loadOneByNameQueries("YvsBaseVisiteur.findById", new String[]{"id"}, new Object[]{Long.valueOf(visiteur)});
                if (clt != null ? clt.getId() < 1 : true) {
                    return null;
                }
                YvsBaseVisiteurPointLivraison y = new YvsBaseVisiteurPointLivraison();
                y.setLibelle(libelle);
                y.setVisiteur(clt);
                y.setSecteur(dico);
                if (currentUsers != null) {
                    y.setAuthor(currentUsers);
                }
                y = (YvsBaseVisiteurPointLivraison) dao.save1(y);
                return y;
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("insertLieu")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN})
    public YvsDictionnaire insertLieu(@HeaderParam("societe") String societe, @HeaderParam("libele") String libele, @HeaderParam("titre") String titre, @HeaderParam("parent") String parent
    ) {
        try {
            if (asString(libele) && asString(titre)) {
                YvsDictionnaire dico = (YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findBylib", new String[]{"lib"}, new Object[]{libele});
                if (dico != null ? dico.getId() > 0 : false) {
                    return dico;
                }
                String abb = (libele.trim().length() > 3 ? libele.substring(0, 3) : libele).toUpperCase();
                dico = new YvsDictionnaire(null, libele, abb, titre);
                dico.setSociete(new YvsSocietes(Long.valueOf(societe)));
                if (asString(parent) ? Long.valueOf(parent) > 0 : false) {
                    dico.setParent(new YvsDictionnaire(Long.valueOf(parent)));
                }
                dico = (YvsDictionnaire) dao.save1(dico);
                return dico;
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("loadInfos")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String loadInfos(@HeaderParam("users") String users, @HeaderParam("agence") String agence
    ) {
        try {
            YvsUsers current = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(users)});
            if (current != null) {
                currentAgence = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{Long.valueOf(agence)});
                currentUsers = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findByUsersAgence", new String[]{"agence", "user"}, new Object[]{currentAgence, current});

                currentScte = currentUsers.getAgence().getSociete();
                currentExo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByActif", new String[]{"actif", "societe", "date"}, new Object[]{true, currentScte, new Date()});

                dao.loadInfos(currentScte, currentAgence, currentUsers, currentDepot, currentPoint, currentExo);
                return "true";
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "false";
    }

    @POST
    @Path("getMontant")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComDocAchats setMontantTotalDoc(@HeaderParam("doc") String doc, @HeaderParam("societe") String societe
    ) {
        try {
            YvsSocietes societes = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{Long.valueOf(societe)});
            YvsComDocAchats achats = new YvsComDocAchats(Long.valueOf(doc));
            dao.setMontantTotalDoc(achats, societes.getId());
            return achats;
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @POST
    @Path("getMontantVente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComDocVentes getMontantVente(@HeaderParam("doc") String doc, @HeaderParam("societe") String societe) {
        try {
            YvsSocietes societes = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{Long.valueOf(societe)});
            YvsComDocVentes ventes = new YvsComDocVentes(Long.valueOf(doc));
            dao.setMontantTotalDoc(ventes, societes.getId());
            return ventes;
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @POST
    @Path("montantMission")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsGrhMissions montantMission(@HeaderParam("mission") String mission
    ) {
        try {
            Long id_mission = Long.valueOf(mission);
            YvsGrhMissions missions = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{id_mission});
            YvsGrhMissions frais = setMontantTotalMission(missions);
            return frais;
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @POST
    @Path("fraisMission")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsGrhFraisMission> fraisMission(@HeaderParam("mission") String mission
    ) {
        try {
            return dao.loadNameQueries("YvsGrhFraisMission.findByMission", new String[]{"mission"}, new Object[]{new YvsGrhMissions(Long.valueOf(mission))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public String getXmlObject(Object object) {
        String result = null;
        try {
            if (object != null) {
                List<Field> contraintes = new ArrayList<>();
                String name_root = object.getClass().getSimpleName();
                if (object.getClass().isAnnotationPresent(Table.class)) {
                    Table table = (Table) object.getClass().getAnnotation(Table.class);
                    if (table != null) {
                        name_root = table.name();
                    }
                }
                result = "<" + name_root + ">";
                result += "     <row>";
                for (Field field : object.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(JoinColumn.class)) {
                        contraintes.add(field);
                    } else if (field.isAnnotationPresent(Column.class)) {
                        field.setAccessible(true);
                        String name = field.getName();
                        Object value = field.get(object);
                        result += "             <" + name + ">" + value + "</" + name + ">";
                    }
                }
                result += "     </row>";
                result += "</" + name_root + ">";
                if (contraintes != null ? !contraintes.isEmpty() : false) {
                    if (object instanceof YvsNiveauUsers) {
                        System.out.println("getIdNiveau : " + ((YvsNiveauUsers) object).getIdNiveau());
                        System.out.println("getIdUser : " + ((YvsNiveauUsers) object).getIdUser());
                    }
                    for (Field field : contraintes) {
                        field.setAccessible(true);
                        System.out.println("field : " + field);
                        Object value = field.get(object);
                        System.out.println("value : " + value);
                        if (value != null) {
                            result += getXmlObject(value);
                        }
                    }
                }
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @POST
    @Path("authentification")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsNiveauUsers authentification(@HeaderParam("code_users") String code_users, @HeaderParam("password_user") String password_user) {
        try {
            YvsUsers users = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findByCodeUsers", new String[]{"codeUsers"}, new Object[]{code_users});
            if (users != null ? users.getId() > 0 : false) {
                String mdp = code_users.concat(users.getAleaMdp()).concat(password_user);
                mdp = MdpUtil.hashString(mdp);
                if (users.getPasswordUser().equals(mdp)) {
                    currentAgence = users.getAgence();
                    YvsNiveauUsers niveau = (YvsNiveauUsers) dao.loadOneByNameQueries("YvsNiveauUsers.findNivoUserInScte", new String[]{"user", "societe"}, new Object[]{users, users.getAgence().getSociete()});
                    if (niveau != null ? niveau.getId() > 0 : false) {
                        currentUsers = yvsUsersAgence(users, users.getAgence());
                        currentScte = currentUsers.getAgence().getSociete();
                        currentExo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByActif", new String[]{"actif", "societe", "date"}, new Object[]{true, currentScte, new Date()});

                        dao.loadInfos(currentScte, currentAgence, currentUsers, currentDepot, currentPoint, currentExo);
                    }
                    return niveau;
                }
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
    }

    @POST
    @Path("yvsUsersAgence")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsUsersAgence yvsUsersAgence(@HeaderParam("users") String users, @HeaderParam("agence") String agence) {
        try {
            if ((asString(users) ? Long.valueOf(users) > 0 : false) && (asString(agence) ? Long.valueOf(agence) > 0 : false)) {
                return yvsUsersAgence(new YvsUsers(Long.valueOf(users)), new YvsAgences(Long.valueOf(agence)));
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("getSocieteConnexion")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsSocietesConnexion getSocieteConnexion(@HeaderParam("societe") String societe) {
        try {
            if ((asString(societe) ? Long.valueOf(societe) > 0 : false)) {
                return (YvsSocietesConnexion) dao.loadOneByNameQueries("YvsSocietesConnexion.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public YvsUsersAgence yvsUsersAgence(YvsUsers users, YvsAgences agence) {
        try {
            if (users != null ? users.getId() > 0 : false) {
                if (agence != null ? agence.getId() > 0 : false) {
                    YvsUsersAgence y = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findByUsersAgence", new String[]{"agence", "user"}, new Object[]{agence, users});
                    if (y != null ? y.getId() < 1 : true) {
                        y = new YvsUsersAgence(users, agence);
                        y = (YvsUsersAgence) dao.save1(y);
                    }
                    return y;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("getCrenauUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComCreneauHoraireUsers getCrenauUser(@HeaderParam("user") String user) {
        System.out.println("user = " + user);
        try {
            return (YvsComCreneauHoraireUsers) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.findByUsers", new String[]{"users"}, new Object[]{new YvsUsers(Long.valueOf(user))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @GET
    @Path("returnSocietes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsSocietes> returnSocietes() {
        try {
            return dao.loadNameQueries("YvsSocietes.findAll", new String[]{}, new Object[]{});
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("readJSON_V2")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public YvsBaseArticleSite readJSON_V2(YvsBaseArticleSite value) {
        try {
            System.out.println("result : " + value);
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    @POST
    @Path("getGroupeSociete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsSocietes> getGroupeSociete(@HeaderParam("user") String user) {
        try {
            List<YvsSocietes> groupes = new ArrayList<>();
            YvsUsers users = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(user)});
            if (users.getAgence().getSociete().getGroupe() != null ? users.getAgence().getSociete().getGroupe().getId() > 0 : false) {
                groupes = dao.loadNameQueries("YvsSocietes.findBySameGroupe", new String[]{"groupe"}, new Object[]{users.getAgence().getSociete().getGroupe()});
            } else {
                groupes.add(users.getAgence().getSociete());
            }
            return groupes;
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }

    }

    @POST
    @Path("getNiveauUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsNiveauUsers getNiveauUsers(@HeaderParam("user") String user, @HeaderParam("societe") String societe
    ) {
        try {
            if (asString(user) && asString(societe)) {
                return (YvsNiveauUsers) dao.loadOneByNameQueries("YvsNiveauUsers.findNivoUserInScte", new String[]{"user", "societe"}, new Object[]{new YvsUsers(Long.valueOf(user)), new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("getAgences")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<YvsAgences> getAgences(@HeaderParam("societe") String societe) {
        try {
            if (asString(societe)) {
                return dao.loadNameQueries("YvsAgences.findBySociete", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("getPointVente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBasePointVente> getPointVente(@HeaderParam("societe") String societe
    ) {
        try {
            if (asString(societe)) {
                return dao.loadNameQueries("YvsBasePointVente.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    @POST
    @Path("getArticles")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseArticles> getArticles(@HeaderParam("societe") String societe) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                return dao.loadNameQueries("YvsBaseArticles.findBySociete", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("getArticlesNotIds")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseArticles> getArticlesNotIds(@HeaderParam("societe") String societe, @HeaderParam("ids") String ids) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                List<Long> value = new ArrayList<>();
                if (asString(ids)) {
                    for (String id : ids.split(",")) {
                        value.add(Long.valueOf(id));
                    }
                }
                if (value.isEmpty()) {
                    value.add(-1L);
                }
                return dao.loadNameQueries("YvsBaseArticles.findAllNotIds", new String[]{"societe", "ids"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), value});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("getDepots")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseDepots> getDepots(@HeaderParam("societe") String societe
    ) {
        try {
            if (asString(societe)) {
                return dao.loadNameQueries("YvsBaseDepots.findBySociete", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("getCaisses")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseCaisse> getCaisses(@HeaderParam("societe") String societe) {
        try {
            if (asString(societe)) {
                return dao.loadNameQueries("YvsBaseCaisse.findAllActif", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("getModel")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseModelReglement> getModel(@HeaderParam("societe") String societe) {
        try {
            if (asString(societe)) {
                return dao.loadNameQueries("YvsBaseModelReglement.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("getMode")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseModeReglement> getMode(@HeaderParam("societe") String societe) {
        try {
            if (asString(societe)) {
                return dao.loadNameQueries("YvsBaseModeReglement.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("getModeNotIds")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseModeReglement> getModeNotIds(@HeaderParam("societe") String societe, @HeaderParam("ids") String ids) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                List<Long> value = new ArrayList<>();
                if (asString(ids)) {
                    for (String id : ids.split(",")) {
                        value.add(Long.valueOf(id));
                    }
                }
                if (value.isEmpty()) {
                    value.add(-1L);
                }
                return dao.loadNameQueries("YvsBaseModeReglement.findAllNotIds", new String[]{"societe", "ids"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), value});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("getSoldeCaisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public double getSoldeCaisse(@HeaderParam("societe_") String societe_, @HeaderParam("caisse_") String caisse_, @HeaderParam("mode_") String mode_, @HeaderParam("date_") String date_
    ) {
        System.out.println(" id Societe = " + societe_);
        try {
            if (!asString(societe_)) {
                return 0;
            }
            YvsBaseCaisse caisse = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{Long.valueOf(caisse_)});
            if (caisse != null ? caisse.getId() < 1 : true) {
                return 0;
            }
            Date debut = df.parse(date_);
            if (caisse.getTypeCaisse().equals("CAISSE")) {
                // YvsBaseModelReglement moderegl = (YvsBaseModelReglement) dao.loadOneByNameQueries("YvsBaseModelReglement.findById", new String[]{"id"}, new Object[]{Long.valueOf(mode_)});
                return dao.getTotalCaisse(Long.valueOf(societe_), caisse.getId(), 0, "", "", "", 'P', debut);
                // return dao.getTotalCaisse(societes.getId(), caisse.getId(), moderegl.getId(), "", "", "", 'P', debut);

            } else if (caisse.getTypeCaisse().equals("BANQUE")) {
                return dao.getTotalCaisse(Long.valueOf(societe_), caisse.getId(), 0, "", "", "", 'P', debut);
            }
        } catch (NumberFormatException | ParseException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }

    @POST
    @Path("getConditionnement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseConditionnement> getConditionnement(@HeaderParam("article") String article
    ) {
        try {
            System.out.println("article  =" + article);
            return dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("PointArticle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBasePointVente> getPointByArticle(@HeaderParam("article") String article
    ) {
        try {
            return dao.loadNameQueries("YvsBaseConditionnementPoint.findPointByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("returnConditionnementsPoint")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseConditionnementPoint> returnConditionnementsPoint(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBaseConditionnementPoint.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("returnArticlesPoint")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseArticlePoint> returnArticlesPoint(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBaseArticlePoint.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("getConditionnementPoint")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseConditionnementPoint> getConditionnementPoint(@HeaderParam("article") String article, @HeaderParam("point") String point
    ) {
        try {
            YvsBaseArticlePoint articlepoint = (YvsBaseArticlePoint) dao.loadOneByNameQueries("YvsBaseArticlePoint.findArticlePoint", new String[]{"point", "article"}, new Object[]{new YvsBasePointVente(Long.valueOf(point)), new YvsBaseArticles(Long.valueOf(article))});
            return dao.loadNameQueries("YvsBaseConditionnementPoint.findByArticlePoint", new String[]{"article"}, new Object[]{articlepoint});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("getConditionnementArticle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseConditionnement> getConditionnementArticle(@HeaderParam("article") String article
    ) {
        try {
            return dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    //***// BEGIN FONCTIONS SECONDAIRES /***/
    public YvsGrhMissions setMontantTotalMission(YvsGrhMissions mi) {
        //total des bons planifié rattaché à la mission
        Double montant = (Double) dao.loadObjectByNameQueries("YvsComptaJustifBonMission.getTotalBon", new String[]{"mission", "statut"}, new Object[]{mi, Constantes.STATUT_DOC_ANNULE});
        mi.setTotalBon(montant != null ? montant : 0d);
        //total des bons déjà payé rattaché à la mission
        montant = (Double) dao.loadObjectByNameQueries("YvsComptaJustifBonMission.getTotalBonPaye", new String[]{"mission", "statut", "statutP"}, new Object[]{mi, Constantes.STATUT_DOC_ANNULE, Constantes.STATUT_DOC_PAYER});
        double totalBonPaye = (montant != null ? montant : 0d);
        //total des pièce de caisse mission
        montant = (Double) dao.loadObjectByNameQueries("YvsComptaCaissePieceMission.findTotalPieceByMission", new String[]{"mission", "statut"}, new Object[]{mi, Constantes.STATUT_DOC_ANNULE});
        mi.setTotalPiece(montant != null ? montant : 0d);
        //total des pièce de caisse mission dejà payé
        montant = (Double) dao.loadObjectByNameQueries("YvsComptaCaissePieceMission.findTotalPiecePayeByMission", new String[]{"mission", "statut"}, new Object[]{mi, Constantes.STATUT_DOC_PAYER});
        double totalPiecePaye = (montant != null ? montant : 0d);
        totalPaye = (mi.getTotalBonPaye() + totalPiecePaye);
        montant = (Double) dao.loadObjectByNameQueries("YvsGrhFraisMission.sumByMission", new String[]{"mission"}, new Object[]{mi});
        mi.setTotalFraisMission(montant != null ? montant : 0d);
        double restPlanifier = (mi.getTotalFraisMission() - mi.getTotalPiece() - mi.getTotalBon());
        mi.setTotalBon(mi.getTotalBon());
        mi.setTotalFraisMission(mi.getTotalFraisMission());
        mi.setTotalPiece(mi.getTotalPiece());
        mi.setTotalRegle(totalPaye);
        mi.setTotalReste(mi.getTotalFraisMission() - totalPaye);
        mi.setTotalResteAPlanifier(restPlanifier);
        mi.setTotalBonPaye(totalBonPaye);
        return mi;
    }

    public boolean asNumeric(String valeur) {
        try {
            if (asString(valeur)) {
                Float.valueOf(valeur);
                return true;
            }
        } catch (NumberFormatException ex) {

        }
        return false;
    }

    public boolean asInteger(String valeur) {
        try {
            if (asString(valeur)) {
                Float _float_ = Float.valueOf(valeur);
                Integer _integer_ = Integer.valueOf(valeur);
                return Math.abs(_float_) - Math.abs(_integer_) <= 0;
            }
        } catch (NumberFormatException ex) {

        }
        return false;
    }

    public boolean asString(String valeur) {
        if (valeur != null ? valeur.trim().length() > 0 : false) {
            return true;
        }
        return false;
    }

    public static List<Integer> convertArrayStringToInt(String[] entree) {
        List<Integer> result = new ArrayList<>();
        if (entree != null) {
            for (String e : entree) {
                result.add(Integer.valueOf(e != null ? e.trim().length() > 0 ? e.trim() : "0" : "0"));
            }
        }
        return result;
    }

    public static List<Long> convertArrayStringToLong(String[] entree) {
        List<Long> result = new ArrayList<>();
        if (entree != null) {
            for (String e : entree) {
                result.add(Long.valueOf(e != null ? e.trim().length() > 0 ? e.trim() : "0" : "0"));
            }
        }
        return result;
    }

    public boolean autoriser(String ressource) {
        if (currentNiveau != null ? currentNiveau.getId() < 1 : true) {
            return false;
        }
        YvsAutorisationRessourcesPage b = (YvsAutorisationRessourcesPage) dao.loadObjectByNameQueries("YvsAutorisationRessourcesPage.findAccesRessource_", new String[]{"reference", "niveau"}, new Object[]{ressource, currentNiveau.getIdNiveau()});
        return b != null ? b.getAcces() : false;
    }

    public void initRepResource(YvsSocietes societe) {
        if (!asString(Initialisation.getCheminAllDoc())) {
            if (societe != null ? societe.getId() > 0 : false) {
                if (!asString(societe.getCodeAbreviation())) {
                    societe = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{societe.getId()});
                }
                Initialisation.initRepResource(societe.getCodeAbreviation());
            }
        }
    }

    public void copyImage(String path, String photo, String bytePhoto) {
        try {
            String destination = path + FILE_SEPARATOR + photo;
            File file = new File(destination);
            if (asString(destination) ? !file.exists() : false) {
                if (Util.asString(bytePhoto)) {
                    byte[] bytes = Base64.decode(bytePhoto);
                    if (bytes != null) {
                        yvs.util.Util.copySVGFile(photo, path + "" + Initialisation.FILE_SEPARATOR, bytes);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteImage(String path, String photo) {
        try {
            String destination = path + FILE_SEPARATOR + photo;
            File file = new File(destination);
            if (asString(destination) ? file.exists() : false) {
                file.delete();
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int buildDocByDroit(String type) {
        type = asString(type) ? type : Constantes.TYPE_FV;
        switch (type) {
            case Constantes.TYPE_FAA:
            case Constantes.TYPE_FA: {
                if (autoriser("fa_view_all_doc")) {
                    return 1;
                } else if (autoriser("fa_view_only_doc_agence")) {
                    return 2;
                } else if (autoriser("fa_view_only_doc_depot")) {
                    return 3;
                } else {
                    return 4;
                }
            }
            case Constantes.TYPE_BRA:
            case Constantes.TYPE_BLA: {
                if (autoriser("bla_view_all_doc")) {
                    return 1;
                } else if (autoriser("bla_view_only_doc_agence")) {
                    return 2;
                } else if (autoriser("bla_view_only_doc_depot")) {
                    return 3;
                } else {
                    return 4;
                }
            }
            case Constantes.TYPE_BCV:
            case Constantes.TYPE_FV: {
                if (autoriser("fv_view_all_doc")) {
                    return 1;
                } else if (autoriser("fv_view_only_doc_agence")) {
                    return 2;
                } else if (autoriser("fv_view_only_doc_pv")) {
                    return 3;
                } else if (autoriser("fv_view_only_doc_depot")) {
                    return 4;
                } else {
                    return 5;
                }
            }
            case Constantes.TYPE_BLV: {
                if (autoriser("blv_view_all_doc")) {
                    return 1;
                } else if (autoriser("blv_view_only_doc_agence")) {
                    return 2;
                } else if (autoriser("blv_view_only_doc_depot")) {
                    return 3;
                } else {
                    return 4;
                }
            }
            case Constantes.TYPE_SS:
            case Constantes.TYPE_IN:
            case Constantes.TYPE_RA:
            case Constantes.TYPE_ES: {
                if (autoriser("d_stock_view_all_agence")) {
                    return 1;
                } else if (autoriser("d_stock_view_all_depot")) {
                    return 2;
                } else if (autoriser("d_stock_view_all_date")) {
                    return 3;
                } else {
                    return 4;
                }
            }
            case Constantes.TYPE_FT: {
                if (autoriser("tr_view_all")) {
                    return 1;
                } else if (autoriser("tr_view_all_user")) {
                    return 2;
                } else if (autoriser("tr_view_historique")) {
                    return 3;
                } else {
                    return 4;
                }
            }
            default:
                return -1;
        }
    }

    public void loadTranche(String societe) {
        onlineTranche = (YvsGrhTrancheHoraire) dao.loadOneByNameQueries("YvsGrhTrancheHoraire.findByOnline", new String[]{"societe", "venteOnline"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), true});
    }

    public void loadUsers(String societe) {
        onlineUsers = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findByOnline", new String[]{"societe", "venteOnline"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), true});
    }

    public void loadCategorieComptable(String societe) {
        onlineCategorie = (YvsBaseCategorieComptable) dao.loadOneByNameQueries("YvsBaseCategorieComptable.findByOnline", new String[]{"societe", "venteOnline"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), true});
    }

    public void loadCategorieClient(String societe) {
        onlineTarifaire = (YvsBaseCategorieClient) dao.loadOneByNameQueries("YvsBaseCategorieClient.findByOnline", new String[]{"societe", "venteOnline"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), true});
    }

    public void loadCategorieCompte(String societe) {
        onlineCompte = (YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findByOnline", new String[]{"societe", "venteOnline"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), true});
    }

    public void loadSociete() {
        onlineSociete = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findByOnline", new String[]{"venteOnline"}, new Object[]{true});
    }

    public YvsSocietes getOnlineSocietes() {
        if (onlineSociete != null ? onlineSociete.getId() < 1 : true) {
            loadSociete();
        }
        return onlineSociete;
    }

    public YvsBaseCategorieComptable getOnlineCategorieComptable(String societe) {
        if (onlineCategorie != null ? onlineCategorie.getId() < 1 : true) {
            loadCategorieComptable(societe);
        }
        return onlineCategorie;
    }

    public YvsBasePlanComptable getOnlineCategorieCompte(String societe) {
        if (onlineCompte != null ? onlineCompte.getId() < 1 : true) {
            loadCategorieCompte(societe);
        }
        return onlineCompte;
    }

    public YvsBaseCategorieClient getOnlineCategorieTarifaire(String societe) {
        if (onlineTarifaire != null ? onlineTarifaire.getId() < 1 : true) {
            loadCategorieClient(societe);
        }
        return onlineTarifaire;
    }

    public YvsUsers getOnlineUsers(String societe) {
        if (onlineUsers != null ? onlineUsers.getId() < 1 : true) {
            loadUsers(societe);
        }
        return onlineUsers;
    }

    protected boolean asDroitValideEtape(YvsWorkflowEtapeValidation etape, YvsNiveauAcces n) {
        return asDroitValideEtape(etape.getAutorisations(), n);
    }

    protected boolean asDroitValideEtape(List<YvsWorkflowAutorisationValidDoc> lau, YvsNiveauAcces n) {
        if (lau != null ? !lau.isEmpty() : false) {
            for (YvsWorkflowAutorisationValidDoc au : lau) {
                if (au.getNiveauAcces().equals(n)) {
                    return au.getCanValide();
                }
            }
            return false;
        }
        return true;
    }
    //***// BEGIN FONCTIONS SECONDAIRES /***/

    protected List<YvsComTaxeContenuVente> buildTaxes(Long article, Double quantite, Double prix, Double remise, Long categorie) {
        List<YvsComTaxeContenuVente> list = new ArrayList<>();
        if (article != null ? article > 0 : false) {
            double taxe = 0;
            double valeur = 0;
            YvsBaseArticles y = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{article});
            nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticle";
            champ = new String[]{"categorie", "article"};
            val = new Object[]{new YvsBaseCategorieComptable(categorie), y};
            YvsBaseArticleCategorieComptable acc = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (acc != null ? (acc.getId() != null ? acc.getId() > 0 : false) : false) {
                if (y.getPuvTtc()) {
                    for (YvsBaseArticleCategorieComptableTaxe t : acc.getTaxes()) {
                        taxe += t.getTaxe().getTaux();
                    }
                    prix = prix / (1 + (taxe / 100));
                }
                valeur = quantite * prix;
                for (YvsBaseArticleCategorieComptableTaxe t : acc.getTaxes()) {
                    taxe = 0;
                    if (t.getAppRemise()) {
                        taxe = (((valeur - remise) * t.getTaxe().getTaux()) / 100);
                    } else {
                        taxe = ((valeur * t.getTaxe().getTaux()) / 100);
                    }
                    if (taxe != 0) {
                        YvsComTaxeContenuVente ct = new YvsComTaxeContenuVente();
                        ct.setMontant(taxe);
                        ct.setTaxe(t.getTaxe());
                        list.add(0, ct);
                    }
                }
            }
        }
        return list;
    }

    @POST
    @Path("json_data")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String json_data(RequestBodys request) {
        try {
            System.out.println(request.request1.hello);
            System.out.println(request.request1.foo);
            System.out.println(request.request1.count);

            System.err.println(request.request2.hello);
            System.err.println(request.request2.foo);
            System.err.println(request.request2.count);
            return "true";
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "false";
    }

    @POST
    @Path("getDocTransfert")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComContenuDocStock> getDocTransfert(@HeaderParam("depot") String depot) {
        List<YvsComContenuDocStock> re = new ArrayList<>();
        try {
            return dao.loadNameQueries("YvsComContenuDocStock.findTransfertToValid", new String[]{"typeDoc", "depot", "statut", "statut2"},
                    new Object[]{Constantes.TYPE_FT, new YvsBaseDepots(Long.valueOf(depot)), Constantes.ETAT_SOUMIS, Constantes.ETAT_ENCOURS});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @POST
    @Path("countTransfert")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Long countNbWaitingTransfert(@HeaderParam("depot") String depot) {
        try {
            if (asString(depot)) {
                Long N = (Long) dao.loadObjectByNameQueries("YvsComContenuDocStock.countTransfertToValid", new String[]{"typeDoc", "depot", "statut"},
                        new Object[]{Constantes.TYPE_FT, new YvsBaseDepots(Long.valueOf(depot)), Constantes.ETAT_VALIDE});
                return (N != null) ? N : 0L;
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return 0L;
        }
        return 0L;
    }

    @POST
    @Path("insertServeur")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String[] insertServeur(YvsSynchroServeurs value) {
        try {
            if (value != null ? asString(value.getAdresseIp()) : false) {
                String url = ds.getConnection().getMetaData().getURL();
                String cleanURI = url.substring(5);
                URI uri = URI.create(cleanURI);
                String adresse = uri.getHost();

                YvsSynchroServeursVersion v = (YvsSynchroServeursVersion) dao.loadOneByNameQueries("YvsSynchroServeursVersion.findByAdresse",
                        new String[]{"adresse"}, new Object[]{value.getAdresseIp()});
                if (v != null ? v.getId() < 1 : true) {
                    v = new YvsSynchroServeursVersion();
                    v.setAdresse(value.getAdresseIp());
                    v.setVersion("00.01");
                    dao.save(v, false);
                } else {
                    String version = v.getVersion();
                    version = version.replace(".", "");
                    int _version_ = Integer.valueOf(version);
                    _version_ = _version_ + 1;
                    version = "";
                    for (int i = 0; i < 4 - (_version_ + "").length(); i++) {
                        version += "0";
                    }
                    version += "" + _version_;
                    version = version.substring(0, 2) + "." + version.substring(2, 4);
                    v.setVersion(version);
                    dao.update(v, false);
                }
                value.setAdresseIp(value.getAdresseIp() + "-" + v.getVersion());
                YvsSynchroServeurs y = (YvsSynchroServeurs) dao.loadOneByNameQueries("YvsSynchroServeurs.findByAdresseIp", new String[]{"adresseIp"}, new Object[]{value.getAdresseIp()});
                if (y != null ? y.getId() < 1 : true) {
                    dao.save(value, false);
                }
                String[] result = new String[2];
                result[0] = adresse;
                result[1] = v.getVersion();
                return result;
            }
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("updateVersionServeur")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean updateVersionServeur(@HeaderParam("id") String id, @HeaderParam("version") String version) {
        try {
            if (asString(id)) {
                dao.requeteLibre("UPDATE yvs_synchro_serveurs_version SET version = ? WHERE id = ?", new Options[]{new Options(version, 1), new Options(Long.valueOf(id), 2)});
            }
            return true;
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @POST
    @Path("getServeurVersion")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Object[] getServeurVersion(@HeaderParam("adresseIp") String adresseIp, @HeaderParam("adresseIpVersion") String adresseIpVersion) {
        Object[] result = new Object[2];
        try {
            String version = "00.01";
            Long id = 0L;
            if (asString(adresseIp)) {
                YvsSynchroServeursVersion v = (YvsSynchroServeursVersion) dao.loadOneByNameQueries("YvsSynchroServeursVersion.findByAdresse", new String[]{"adresse"}, new Object[]{adresseIp});
                if (v != null ? v.getId() > 0 : false) {
                    version = v.getVersion();
                    YvsSynchroServeurs s = (YvsSynchroServeurs) dao.loadOneByNameQueries("YvsSynchroServeurs.findByAdresseIp", new String[]{"adresseIp"}, new Object[]{adresseIpVersion});
                    if (s != null ? s.getId() > 0 : false) {
                        id = s.getId();
                    }
                }
            }
            result[0] = version;
            result[1] = id;
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @POST
    @Path("getCountSynchroForVersion")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Long getCountSynchroForVersion(@HeaderParam("adresseIp") String adresseIp) {
        Long count = 0L;
        try {
            if (asString(adresseIp)) {
                count = (Long) dao.loadObjectByNameQueries("YvsSynchroDataSynchro.countByServeur", new String[]{"serveur"}, new Object[]{adresseIp});
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    @POST
    @Path("requete_libre")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public boolean requeteLibre(@HeaderParam("query") String query) {
        try {
            dao.requeteLibre(query, new Options[]{});
            return true;
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @GET
    @Path("toText")
    @Produces(MediaType.APPLICATION_JSON)
    public YvsComDocVentes toText() {
        try {
            return (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{10});
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("getXmlDb")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getXmlDb(@HeaderParam("societe") String societe) {
        try {
            String remotePath = getRemotePath() + "database";
            File doc = new File(remotePath);
            if (!doc.exists()) {
                doc.mkdirs();
            }
            if (!doc.canWrite()) {
                doc.setWritable(true);
            }
            List<YvsSynchroTables> list = dao.loadNameQueries("YvsSynchroTables.findAll", new String[]{}, new Object[]{});
            if (list != null ? list.isEmpty() : true) {
                return null;
            }
            YvsSocietes ste = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{Long.valueOf(societe)});
            String localPath = getLocalPath(ste);
            if (!asString(localPath)) {
                return null;
            }
            localPath += "database";
            doc = new File(localPath);
            if (!doc.exists()) {
                doc.mkdirs();
            }
            if (!doc.canWrite()) {
                doc.setWritable(true);
            }

            String fileName = "database_ste" + societe + ".xml";
            File remoteFile = new File(remotePath, fileName);
            if (remoteFile.exists()) {
                remoteFile.delete();
            }
            File localFile = new File(localPath, fileName);
            if (localFile.exists()) {
                localFile.delete();
            }
            localFile.createNewFile();
            try (FileWriter write = new FileWriter(localFile)) {
                try (BufferedWriter out = new BufferedWriter(write)) {
                    out.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    out.newLine();
                    out.append("<database>");
                    out.newLine();
                    for (YvsSynchroTables y : list) {
                        String xml = getValueOnTable(societe, y.getTableName(), y.getUseSociete().toString(), y.getIntervalLoad().toString());
                        if (xml != null) {
                            out.append(xml);
                            out.newLine();
                        }
                    }
                    out.newLine();
                    out.append("</database>");
                }
            }
            remotePath = copyFile(fileName, remotePath, new FileInputStream(localFile));
            remoteFile = new File(remotePath);
            return remoteFile.getAbsolutePath();
        } catch (NumberFormatException | IOException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("getValueOnTables")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<String> getValueOnTables(@HeaderParam("societe") String societe, @HeaderParam("no_tables") String no_tables) {
        List<String> result = new ArrayList<>();
        try {
            if (asString(societe)) {
                List<String> exists = new ArrayList<>();
                if (asString(no_tables)) {
                    exists = Arrays.asList(no_tables.split(","));
                } else {
                    exists.add("");
                }
                List<YvsSynchroTables> list = dao.loadNameQueries("YvsSynchroTables.findNotTables", new String[]{"tables"}, new Object[]{exists});
                if (list != null ? !list.isEmpty() : false) {
                    for (YvsSynchroTables y : list) {
                        String xml = getValueOnTable(societe, y.getTableName(), y.getUseSociete().toString(), y.getIntervalLoad().toString());
                        if (xml != null) {
                            result.add(xml);
                        }
                    }
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @POST
    @Path("getValueOnTable")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getValueOnTable(@HeaderParam("societe") String societe, @HeaderParam("table") String table, @HeaderParam("use_societe") String useSociete, @HeaderParam("interval_load") String interval_load) {
        try {
            if (asString(societe) && asString(table)) {
                List<String> colonnes = dao.field(table, "", "");
                if (colonnes.contains("author")) {
                    String select = "SELECT y.* FROM " + table + " y";
                    if (asString(useSociete) ? Boolean.valueOf(useSociete) : false) {
                        select += " INNER JOIN yvs_users_agence ua ON y.author = ua.id INNER JOIN yvs_agences a ON ua.agence = a.id WHERE a.societe = " + societe;
                    }
                    if (asString(interval_load) ? (Integer.valueOf(interval_load) > 0 ? colonnes.contains("date_save") : false) : false) {
                        int interval = Integer.valueOf(interval_load) * 30;
                        Date dateFin = new Date();
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DATE, -interval);
                        Date dateDebut = c.getTime();
                        if (select.contains("WHERE")) {
                            select += " AND ";
                        } else {
                            select += " WHERE ";
                        }
                        select += "y.date_save BETWEEN '" + df.format(dateDebut) + "' AND '" + df.format(dateFin) + "'";
                    }
                    String xmls = "table xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
                    String xmls_end = "</table>";
                    Options[] params = new Options[]{new Options(select, 1), new Options(xmls, 2), new Options(table, 3), new Options(xmls_end, 4), new Options("</" + table + ">", 5)};
                    String query = "SELECT REPLACE(REPLACE(XMLSERIALIZE(DOCUMENT query_to_xml(?, false, false, '') AS TEXT), ?, ?), ?, ?)";
                    return (String) dao.loadObjectBySqlQuery(query, params);
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("getValueOnQuery")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getValueOnQuery(@HeaderParam("query") String select, @HeaderParam("table") String table) {
        try {
            if (asString(select) && asString(table)) {
                String xmls = "table xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
                String xmls_end = "</table>";
                Options[] params = new Options[]{new Options(select, 1), new Options(xmls, 2), new Options(table, 3), new Options(xmls_end, 4), new Options("</" + table + ">", 5)};
                String query = "SELECT REPLACE(REPLACE(XMLSERIALIZE(DOCUMENT query_to_xml(?, false, false, '') AS TEXT), ?, ?), ?, ?)";
                return (String) dao.loadObjectBySqlQuery(query, params);
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("getStockValueToXml")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getStockValueToXml(@HeaderParam("societe") String societe, @HeaderParam("depots") String depots) {
        try {
            if (asString(societe) && asString(depots)) {
                List<String> values = Arrays.asList(depots.split(","));
                String new_line = "";
                String result = "<yvs_base_mouvement_stock>" + new_line;
                List<YvsBaseConditionnement> list = dao.loadNameQueries("YvsBaseConditionnement.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
                if (list != null ? !list.isEmpty() : false) {
                    int id = 0;
                    for (YvsBaseConditionnement c : list) {
                        for (String depot : values) {
                            double stock = dao.stocks(c.getArticle().getId(), Long.valueOf(depot), new Date(), c.getId());
                            if (stock != 0) {
                                double cout_entree = dao.getPr(c.getArticle().getId(), Long.valueOf(depot), 0, new Date(), c.getId());
                                double cout_stock = cout_entree;
                                String mouvement = stock < 0 ? "S" : "E";
                                String date_doc = dfXml.format(new Date());
                                String date_mouvement = dfXml.format(new Date());
                                String date_save = dfXml.format(new Date());
                                String date_update = dfXml.format(new Date());

                                result += "     <row>" + new_line;
                                result += "             <id>" + (id++) + "</id>" + new_line;
                                result += "             <supp>false</supp>" + new_line;
                                result += "             <actif>true</actif>" + new_line;
                                result += "             <calcul_pr>true</calcul_pr>" + new_line;
                                result += "             <description>stock initial</description>" + new_line;
                                result += "             <quantite>" + stock + "</quantite>" + new_line;
                                result += "             <date_doc>" + date_doc + "</date_doc>" + new_line;
                                result += "             <mouvement>" + mouvement + "</mouvement>" + new_line;
                                result += "             <article>" + c.getArticle().getId() + "</article>" + new_line;
                                result += "             <depot>" + depot + "</depot>" + new_line;
                                result += "             <cout_entree>" + cout_entree + "</cout_entree>" + new_line;
                                result += "             <date_mouvement>" + date_mouvement + "</date_mouvement>" + new_line;
                                result += "             <cout_stock>" + cout_stock + "</cout_stock>" + new_line;
                                result += "             <date_save>" + date_save + "</date_save>" + new_line;
                                result += "             <conditionnement>" + c.getId() + "</conditionnement>" + new_line;
                                result += "             <date_update>" + date_update + "</date_update>" + new_line;
                                result += "     </row>" + new_line;
                            }
                        }
                    }
                }
                result += "</yvs_base_mouvement_stock>";
                return result;
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Si le résultat de cette méthode est null, c'est qu'il y a certainement
     * erreur sur le nom de la table*
     */
    @GET
    @Path("entityExist")
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean entityExist(@HeaderParam("id") String id, @HeaderParam("table") String table) {
        if (id != null && table != null) {
            try {
                Long id_ = Long.valueOf(id);
                String query = "SELECT COUNT(*) FROM ".concat(table).concat(" WHERE id=?");
                Object o = dao.loadObjectBySqlQuery(query, new Options[]{new Options(id_, 1)});
                if (o != null) {
                    Long re = (Long) o;
                    return re > 0;
                }
            } catch (NumberFormatException ex) {
                Logger.getLogger(lymytz.ws.market.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @POST
    @Path("getListByQuery")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<Object[]> getListByQuery(@HeaderParam("query") String query, @HeaderParam("param") String param) {
        try {
            if (asString(query)) {
                Options[] params = new Options[]{};
                if (asString(param)) {
                    String[] tabs = param.split(";");
                    params = new Options[tabs.length];
                    for (int i = 0; i < tabs.length; i++) {
                        String tab = tabs[i];
                        params[i] = new Options(asNumeric(tab) ? asInteger(tab) ? Integer.valueOf(tab) : Float.valueOf(tab) : tab, i + 1);
                    }
                }
                return dao.loadListBySqlQuery(query, params);
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("getObjectByQuery")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Object getObjectByQuery(@HeaderParam("query") String query, @HeaderParam("param") String param) {
        try {
            if (asString(query)) {
                Options[] params = new Options[]{};
                if (asString(param)) {
                    String[] tabs = param.split(";");
                    params = new Options[tabs.length];
                    for (int i = 0; i < tabs.length; i++) {
                        String tab = tabs[i];
                        params[i] = new Options(asNumeric(tab) ? asInteger(tab) ? Integer.valueOf(tab) : Float.valueOf(tab) : tab, i + 1);
                    }
                }
                return dao.loadObjectBySqlQuery(query, params);
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected Long getLocalCurrent(Long distant, String tableName, String serveur) {
        try {
            if (distant != null ? distant > 0 : false) {
                return (Long) dao.loadObjectByNameQueries("YvsSynchroDataSynchro.findSource", new String[]{"tableName", "distant", "serveur"}, new Object[]{tableName, distant, serveur});
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.market.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected ResultatAction rebuild(ResultatAction result) {
        try {
            if (result != null ? result.isResult() ? result.getData() != null : false : false) {
                if (result.getData() != null) {
                    result.setData(rebuild(result.getData()));
                }
                if (result.getEntity() != null) {
                    result.setEntity((Serializable) rebuild(result.getEntity()));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.market.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    protected Object rebuild(Object entity) {
        return UtilRebuild.rebuild(entity);
    }

    protected byte[] executeReport(String report, Map<String, Object> param, String extension) {
        try {
            switch (extension) {
                case "xls":
                    return executeReportXls(report, param);
                default:
                    return executeReportPdf(report, param);
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected String SUBREPORT_DIR(boolean withHeader) {
        String result = resource.getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR;
        if (!withHeader) {
            result += "empty" + FILE_SEPARATOR;
        }
        return result;
    }

    protected String returnLogo(YvsSocietes societe) {
        if (societe != null ? societe.getId() > 0 : false) {
            if (asString(societe.getLogo())) {
                String file = getLocalPath(societe) + "logos_doc" + FILE_SEPARATOR + "" + societe.getLogo();
                if (new File(file).exists()) {
                    return file;
                }
            }
        }
        String file = "resources" + FILE_SEPARATOR + "lymytz" + FILE_SEPARATOR + "documents" + FILE_SEPARATOR + "logos_doc" + FILE_SEPARATOR + "default.png";
        return resource.getRealPath(file);
    }

    protected String createQRCode(String qrCodeData) {
        try {
            String path = resource.getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
            String filePath = path + FILE_SEPARATOR + "QRCode.png";
            String charset = "UTF-8"; // or "ISO-8859-1"
            Map hintMap = new HashMap();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            int qrCodeheight = 200, qrCodewidth = 200;

            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset), BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath.lastIndexOf('.') + 1), new File(filePath));
            return filePath;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(yvs.util.Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WriterException | IOException ex) {
            Logger.getLogger(yvs.util.Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected String readQRCode(String filePath) {
        String value = null;
        try {
            String charset = "UTF-8"; // or "ISO-8859-1"
            Map hintMap = new HashMap();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
            Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
            value = qrCodeResult.getText();
        } catch (NotFoundException | FileNotFoundException ex) {
            Logger.getLogger(yvs.util.Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(yvs.util.Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    protected byte[] executeReportXls(String report, Map<String, Object> param) {
        Connection con = null;
        byte[] bytes = null;
        try {
            JasperPrint j = new JasperPrint();
            try {
                try {
                    //cette methode permet de récupérer le chemin absolu du repertoire report suivant.
                    con = ds.getConnection();
                } catch (SQLException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
                File file = new File(resource.getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report"));
                j = JasperFillManager.fillReport(new FileInputStream(new File(file, "" + report + ".jasper")), param, con);//                       
            } catch (FileNotFoundException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
            String PATH_REPORT_FILE = resource.getRealPath(Initialisation.getCheminDownload()) + FILE_SEPARATOR;
            //tableau de byte avec l'objet jasperPrint
            JRXlsExporter exporterXLS = new JRXlsExporter();
            exporterXLS.setExporterInput(new SimpleExporterInput(j));
            File outputFile = new File(PATH_REPORT_FILE + report + ".xls");
            exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setIgnoreCellBorder(false);
            configuration.setWhitePageBackground(false);
            configuration.setCollapseRowSpan(false);
            configuration.setRemoveEmptySpaceBetweenRows(false);
            configuration.setRemoveEmptySpaceBetweenColumns(false);
            configuration.setOnePagePerSheet(false);
            configuration.setDetectCellType(true);//Set configuration as you like it!!
            configuration.setImageBorderFixEnabled(true);
            configuration.setIgnoreGraphics(false);
            exporterXLS.setConfiguration(configuration);
            exporterXLS.exportReport();
            bytes = Util.read(outputFile);
        } catch (JRException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                    con = null;
                } catch (SQLException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return bytes;
    }

    protected byte[] executeReportPdf(String report, Map<String, Object> param) {
        Connection con = null;
        byte[] bytes = null;
        try {
            JasperPrint j = new JasperPrint();
            try {
                try {
                    //cette methode permet de récupérer le chemin absolu du repertoire report suivant.
                    con = ds.getConnection();
                } catch (SQLException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
                File file = new File(resource.getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report"));
                j = JasperFillManager.fillReport(new FileInputStream(new File(file, "" + report + ".jasper")), param, con);//                       
            } catch (FileNotFoundException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
            //tableau de byte avec l'objet jasperPrint
            bytes = JasperExportManager.exportReportToPdf(j);
        } catch (JRException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                    con = null;
                } catch (SQLException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return bytes;
    }

    /*COMPTABILISATION VENTE**/
    public ResultatAction comptabiliserVente(YvsComDocVentes y) {
        if (y != null) {
            YvsComParametreVente cp = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{y.getEnteteDoc().getAgence()});
            Boolean comptabilise = (cp != null ? cp.getComptabilisationAuto() : false);
            if (comptabilise != null ? comptabilise : false) {
                YvsNiveauAcces n = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauUsers.findNiveauByUser", new String[]{"user", "societe"}, new Object[]{y.getAuthor().getUsers(), y.getAuthor().getUsers().getAgence().getSociete()});
                ServiceComptabilite service = new ServiceComptabilite(n, y.getAuthor(), dao);
                ResultatAction re = service.comptabiliserVente(y);
                return re;
            }
        }
        return null;
    }

    public ResultatAction unComptabiliserVente(YvsComDocVentes y) {
        if (y != null) {
            YvsNiveauAcces n = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauUsers.findNiveauByUser", new String[]{"user", "societe"}, new Object[]{y.getAuthor().getUsers(), y.getAuthor().getUsers().getAgence().getSociete()});
            ServiceComptabilite service = new ServiceComptabilite(n, y.getAuthor(), dao);
            ResultatAction re = service.unComptabiliserVente(y);
            return re;
        }
        return null;
    }
    /*END COMPTABILISATION VENTE**/

    /*COMPTABILISATION REGLEMENT VENTE**/
    public ResultatAction comptabiliserCaisseVente(YvsComptaCaissePieceVente y) {
        if (y != null) {
            YvsNiveauAcces n = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauUsers.findNiveauByUser", new String[]{"user", "societe"}, new Object[]{y.getAuthor().getUsers(), y.getAuthor().getUsers().getAgence().getSociete()});
            ServiceComptabilite service = new ServiceComptabilite(n, y.getAuthor(), dao);
            ResultatAction re = service.comptabiliserCaisseVente(y);
            return re;
        }
        return null;
    }

    public ResultatAction unComptabiliserCaisseVente(YvsComptaCaissePieceVente y) {
        if (y != null) {
            YvsNiveauAcces n = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauUsers.findNiveauByUser", new String[]{"user", "societe"}, new Object[]{y.getAuthor().getUsers(), y.getAuthor().getUsers().getAgence().getSociete()});
            ServiceComptabilite service = new ServiceComptabilite(n, y.getAuthor(), dao);
            ResultatAction re = service.unComptabiliserCaisseVente(y);
            return re;
        }
        return null;
    }
    /*END COMPTABILISATION REGLEMENT VENTE**/

    /*COMPTABILISATION VIREMENT**/
    public ResultatAction comptabiliserCaisseVirement(YvsComptaCaissePieceVirement y) {
        if (y != null) {
            YvsNiveauAcces n = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauUsers.findNiveauByUser", new String[]{"user", "societe"}, new Object[]{y.getAuthor().getUsers(), y.getAuthor().getUsers().getAgence().getSociete()});
            ServiceComptabilite service = new ServiceComptabilite(n, y.getAuthor(), dao);
            ResultatAction re = service.comptabiliserCaisseVirement(y);
            return re;
        }
        return null;
    }

    public ResultatAction unComptabiliserCaisseVirement(YvsComptaCaissePieceVirement y) {
        if (y != null) {
            YvsNiveauAcces n = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauUsers.findNiveauByUser", new String[]{"user", "societe"}, new Object[]{y.getAuthor().getUsers(), y.getAuthor().getUsers().getAgence().getSociete()});
            ServiceComptabilite service = new ServiceComptabilite(n, y.getAuthor(), dao);
            ResultatAction re = service.unComptabiliserCaisseVirement(y);
            return re;
        }
        return null;
    }
    /*END COMPTABILISATION VIREMENT**/
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import yvs.dao.Options;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsSocietes;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsers;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@WebService(serviceName = "LymytzWS")
public class LymytzWS extends ManagedWS implements Serializable {

    @Resource
    WebServiceContext resource;

    /**
     * This is a sample web service operation
     *
     * @return
     */

    @WebMethod(operationName = "value")
    public String value() {
        return null;
    }

    @Override
    public String getPathArticle() {
        try {
            String path = "/resources/lymytz/documents/docArticle/";
            ServletContext context = (ServletContext) resource.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
            PATH_PHOTO = context.getContextPath() + path;
            return context.getRealPath(path) + FILE_SEPARATOR;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "online")
    public String online() {
        try {
            if (!asString(PATH_PHOTO)) {
                getPathArticle();
            }
            return PATH_PHOTO;
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "currentUsers")
    public long currentUsers() {
        try {
            if (onlineUsers != null ? onlineUsers.getId() < 1 : true) {
                loadUsers();
            }
            if (onlineUsers != null ? onlineUsers.getId() > 0 : false) {
                return onlineUsers.getId();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return -1;
    }

    @WebMethod(operationName = "currentClient")
    public long currentClient(@WebParam(name = "adresse") String adresse) {
        try {
            if (defaultClient != null ? defaultClient.getId() < 1 : true) {
                loadClient(Long.valueOf(adresse));
            }
            if (defaultClient != null ? defaultClient.getId() > 0 : false) {
                return defaultClient.getId();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return -1;
    }

    @WebMethod(operationName = "currentTranche")
    public Object currentTranche() {
        try {
            if (onlineTranche != null ? onlineTranche.getId() < 1 : true) {
                loadTranche();
            }
            if (onlineTranche != null ? onlineTranche.getId() > 0 : false) {
                return onlineTranche;
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return onlineTranche;
    }

    @WebMethod(operationName = "currentSociete")
    public long currentSociete() {
        try {
            if (onlineSociete != null ? onlineSociete.getId() < 1 : true) {
                loadSociete();
            }
            if (onlineSociete != null ? onlineSociete.getId() > 0 : false) {
                return onlineSociete.getId();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return -1;
    }

    @WebMethod(operationName = "returnPointsVente")
    public byte[] returnPointsVente() {
        try {
            return encode(dao.loadNameQueries("YvsBasePointVente.findByActif", new String[]{"societe", "actif"}, new Object[]{getOnlineSocietes(), true}));
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "returnGroupes")
    public byte[] returnGroupes() {
        try {
            return encode(dao.loadNameQueries("YvsBaseGroupesArticle.findByActif", new String[]{"societe", "actif"}, new Object[]{getOnlineSocietes(), true}));
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "returnParentGroupes")
    public byte[] returnParentGroupes() {
        try {
            return encode(dao.loadNameQueries("YvsBaseGroupesArticle.findParentActif", new String[]{"societe"}, new Object[]{onlineSociete}));
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "returnSousGroupes")
    public byte[] returnSousGroupes() {
        try {
            return encode(dao.loadNameQueries("YvsBaseGroupesArticle.findSousActif", new String[]{"societe"}, new Object[]{getOnlineSocietes()}));
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "returnGroupesByParent")
    public byte[] returnGroupesByParent(@WebParam(name = "parent") String parent) {
        try {
            return encode(dao.loadNameQueries("YvsBaseGroupesArticle.findByGroupeParent", new String[]{"groupeParent"}, new Object[]{new YvsBaseGroupesArticle(Long.valueOf(parent))}));
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "searchArticles")
    public byte[] searchArticles(@WebParam(name = "reference") String id) {
        try {
            return encode(dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{Long.valueOf(id)}));
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "returnUnites")
    public byte[] returnUnites(@WebParam(name = "article") String article) {
        try {
            return encode(dao.loadNameQueries("YvsBaseConditionnement.findVenteByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))}));
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "returnCriteres")
    public byte[] returnCriteres(@WebParam(name = "article") String article) {
        try {
            return encode(dao.loadNameQueries("Articlecritere.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))}));
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "countArticles")
    public Long countArticles(@WebParam(name = "reference") String reference, @WebParam(name = "groupe") String groupe, @WebParam(name = "parent") String parent) {
        try {
            PaginatorResult<YvsBaseArticles> paginator = new PaginatorResult<>();
            paginator.addParam(new ParametreRequete("s.refArt", "reference", null, "=", "AND"));
            if (asString(reference)) {
                ParametreRequete p = new ParametreRequete(null, "reference", reference.toUpperCase() + "%", "=", "AND");
                p.getOtherExpression().add(new ParametreRequete("UPPER(s.refArt)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(s.designation)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
                paginator.addParam(p);
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
            QUERY_ARTICLE = !paginator.getParams().isEmpty() ? paginator.buildDynamicQuery(paginator.getParams(), "SELECT ? FROM YvsBaseArticles s WHERE") : paginator.buildDynamicQuery(paginator.getParams(), "SELECT ? FROM YvsBaseArticles s");
            CHAMP_ARTICLE = paginator.getChamp();
            VAL_ARTICLE = paginator.getVal();
            String nameQuery = QUERY_ARTICLE.replace("?", "COUNT(s)");
            COUNT_ARTICLE = (Long) dao.loadObjectByEntity(nameQuery, CHAMP_ARTICLE, VAL_ARTICLE);
            return COUNT_ARTICLE != null ? COUNT_ARTICLE : 0;
        } catch (NumberFormatException ex) {
            ex.getStackTrace();
        }
        return 0L;
    }

    @WebMethod(operationName = "returnArticles")
    public byte[] returnArticles(@WebParam(name = "reference") String reference, @WebParam(name = "groupe") String groupe, @WebParam(name = "parent") String parent, @WebParam(name = "offset") String offset, @WebParam(name = "limit") String limit, @WebParam(name = "init") String init) {
        List<YvsBaseArticles> result = new ArrayList<>();
        try {
            if (!asString(QUERY_ARTICLE) || Boolean.valueOf(init)) {
                countArticles(reference, groupe, parent);
            }
            if (COUNT_ARTICLE != null ? COUNT_ARTICLE > 0 : false) {
                String nameQuery = QUERY_ARTICLE.replace("?", "s") + " ORDER BY s.designation";
                result.add(new YvsBaseArticles(-1L, "?COUNT", "" + COUNT_ARTICLE));
                if (Integer.valueOf(limit) > 0) {
                    result.addAll(dao.loadEntity(nameQuery, CHAMP_ARTICLE, VAL_ARTICLE, Integer.valueOf(offset), Integer.valueOf(limit)));
                } else {
                    result.addAll(dao.loadEntity(nameQuery, CHAMP_ARTICLE, VAL_ARTICLE));
                }
                return encode(result);
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "mostVendu")
    public byte[] mostVendu() {
        try {
            String query = "SELECT y.article FROM yvs_com_contenu_doc_vente y GROUP BY y.article ORDER BY (SELECT COUNT(y1.id) FROM yvs_com_contenu_doc_vente y1 WHERE y1.article = y.article)";
            List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{});
            if (ids != null ? ids.isEmpty() : true) {
                ids = new ArrayList<Long>() {
                    {
                        add(0L);
                    }
                ;
            }
            ;
            }
            return encode(dao.loadNameQueries("YvsBaseArticles.findByIds", new String[]{"ids"}, new Object[]{ids}, 0, 10));
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "countVentes")
    public Long countVentes(@WebParam(name = "client") String client) {
        try {
            return (Long) dao.loadObjectByNameQueries("YvsComDocVentes.countByClient", new String[]{"client"}, new Object[]{new YvsComClient(Long.valueOf(client))});
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return 0L;
    }

    @WebMethod(operationName = "returnVentes")
    public byte[] returnVentes(@WebParam(name = "client") String client, @WebParam(name = "offset") String offset, @WebParam(name = "limit") String limit, @WebParam(name = "init") String init) {
        List<YvsComDocVentes> result = new ArrayList<>();
        try {
            if (Boolean.valueOf(init)) {
                Long count = countVentes(client);
                result.add(new YvsComDocVentes("?COUNT", "" + count));
            }
            result.addAll(dao.loadNameQueries("YvsComDocVentes.findByClient", new String[]{"client"}, new Object[]{new YvsComClient(Long.valueOf(client))}, Integer.valueOf(offset), Integer.valueOf(limit)));
            return encode(result);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "returnContents")
    public byte[] returnContents(@WebParam(name = "facture") String facture) {
        try {
            return encode(dao.loadNameQueries("YvsComContenuDocVente.findByFacture", new String[]{"docVente"}, new Object[]{new YvsComDocVentes(Long.valueOf(facture))}));
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "returnTTC")
    public Double returnTTC(@WebParam(name = "facture") String facture) {
        try {
            return (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findTTCByFacture", new String[]{"docVente"}, new Object[]{new YvsComDocVentes(Long.valueOf(facture))});
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "searchVente")
    public byte[] searchVente(@WebParam(name = "numero") String numero) {
        try {
            return encode(dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{Long.valueOf(numero)}));
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "insertClient")
    public boolean insertClient(@WebParam(name = "nom") String nom, @WebParam(name = "prenom") String prenom, @WebParam(name = "email") String email, @WebParam(name = "adresse") String adresse, @WebParam(name = "telephone") String telephone) {
        try {
            YvsBaseTiers tiers = new YvsBaseTiers(telephone);
            tiers.setNom(nom);
            tiers.setPrenom(prenom);
            tiers.setTel(telephone);
            tiers.setEmail(email);
            tiers.setAdresse(adresse);
            tiers.setCategorieComptable(getOnlineCategorieComptable());
            tiers.setSociete(getOnlineSocietes());
            tiers.setDateSave(new Date());
            tiers.setDateUpdate(new Date());
            tiers.setActif(true);
            tiers.setStSociete(false);
            tiers.setClient(true);
            tiers.setFournisseur(false);
            tiers.setFabriquant(false);
            tiers.setRepresentant(false);
            tiers.setEmploye(false);
            tiers.setPersonnel(false);
            tiers = (YvsBaseTiers) dao.save1(tiers);

            YvsComClient client = new YvsComClient(null, telephone, nom, prenom, tiers);
            client.setCategorieComptable(getOnlineCategorieComptable());
            client.setDateCreation(new Date());
            client.setDateUpdate(new Date());
            client.setConfirmer(false);
            client.setActif(true);
            client.setDefaut(false);
            client.setSuiviComptable(false);
            client = (YvsComClient) dao.save1(client);

            return client != null ? client.getId() > 0 : false;
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return false;
    }

    @WebMethod(operationName = "updateClient")
    public boolean updateClient(@WebParam(name = "code") String code, @WebParam(name = "nom") String nom, @WebParam(name = "prenom") String prenom, @WebParam(name = "email") String email, @WebParam(name = "adresse") String adresse, @WebParam(name = "telephone") String telephone) {
        try {
            YvsComClient y = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findById", new String[]{"id"}, new Object[]{Long.valueOf(code)});
            if (y != null ? y.getId() < 1 : true) {
                return insertClient(nom, prenom, email, adresse, telephone);
            } else {
                if (y.getTiers() != null) {
                    y.getTiers().setNom(nom);
                    y.getTiers().setPrenom(prenom);
                    y.getTiers().setEmail(email);
                    y.getTiers().setAdresse(adresse);
                    y.getTiers().setTel(telephone);
                    dao.update(y.getTiers());
                }
                y.setNom(nom);
                y.setPrenom(prenom);
                dao.update(y);

                return true;
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return false;
    }

    @WebMethod(operationName = "searchClient")
    public String searchClient(@WebParam(name = "nom") String nom, @WebParam(name = "telephone") String telephone) {
        try {
            if (nom != null && telephone != null) {
                YvsComClient y = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findByNomPhone", new String[]{"nom", "phone"}, new Object[]{nom, telephone});
                if (y != null ? y.getCodeClient() != null ? y.getCodeClient().trim().length() > 0 : false : false) {
                    return y.getCodeClient();
                }
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }
    /*
    
     */

    @WebMethod(operationName = "view")
    public String view() {
        return "OK";
    }

    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        List<YvsBasePointVente> list = dao.loadNameQueries("YvsBasePointVente.findByActif", new String[]{"societe", "actif"}, new Object[]{getOnlineSocietes(), true});
        System.out.println("YvsGrhTrancheHoraire " + list);
        String xmlString = generateXml(list, List.class);
        System.out.println("xmlString " + xmlString);
        return "Hello " + txt + " !";
    }

    @WebMethod(operationName = "valideFacture")
    public boolean valideFacture(@WebParam(name = "facture") Long facture, @WebParam(name = "users") Long users) {
        return new ActionVente().validerFacture(facture, users);
    }

    @WebMethod(operationName = "insertPiece")
    public boolean insertPiece(@WebParam(name = "piece") Long piece, @WebParam(name = "users") Long users) {
        return new ActionReglement().saveReglement(piece, users);
    }

    @WebMethod(operationName = "identity")
    public String identity(@WebParam(name = "users") Long users, @WebParam(name = "societe") Long societe) {
        load(users, 0, societe);
        if (currentScte != null ? currentScte.getId() > 0 : false) {
            YvsUsers y = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{users});
            if (y != null ? y.getId() > 0 : false) {
                if (y.getEmploye() != null ? y.getEmploye().getId() > 0 : false) {
                    String path = getPathIdentity(currentScte.getName());
                    if (path != null ? path.trim().length() > 0 : false) {
                        if (y.getEmploye().getPhotos() != null ? y.getEmploye().getPhotos().trim().length() > 0 : false) {
                            String nameFile = path + FILE_SEPARATOR + y.getEmploye().getPhotos();
                            File file = new File(nameFile);
                            if (file.exists()) {
                                return Util.readToString(file);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @WebMethod(operationName = "photos")
    public String photos(@WebParam(name = "article") Long article, @WebParam(name = "societe") Long societe) {
        YvsSocietes ste = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{societe});
        if (ste != null ? ste.getId() > 0 : false) {
            YvsBaseArticles y = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{article});
            if (y != null ? y.getId() > 0 : false) {
                String path = getPathArticle(ste.getName());
                if (path != null ? path.trim().length() > 0 : false) {
                    if (y.getPhoto1() != null ? y.getPhoto1().trim().length() > 0 : false) {
                        String nameFile = path + FILE_SEPARATOR + y.getPhoto1();
                        File file = new File(nameFile);
                        if (file.exists()) {
                            return Util.readToString(file);
                        }
                    }
                }
            }
        }
        return null;
    }

    @WebMethod(operationName = "returnEmploye")
    public Object[] returnEmploye(@WebParam(name = "matricule") String matricule, @WebParam(name = "societe") long societe) {
        Object[] o = new Object[4];
        YvsGrhEmployes s = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findOneByMatricule", new String[]{"matricule", "societe"}, new Object[]{matricule, new YvsSocietes(societe)});
        if (s != null ? s.getId() > 0 : false) {
            o[0] = s.getId();
            o[1] = s.getNom();
            o[2] = s.getPrenom();
            o[3] = s.getMatricule();
        }
        return o;
    }

    @WebMethod(operationName = "insertPointage")
    public String insertPointage(@WebParam(name = "employe") Long employe, @WebParam(name = "heure") String _heure, @WebParam(name = "societe") Long societe) {
        String result = load(0, employe, societe);
        try {
            boolean b = ActionsPointage.insertPointage(dm.parse(_heure), currentEmploye, currentScte, currentParam);
            return "" + b;
        } catch (ParseException ex) {
            return "false (WS) : " + result + " -- ParseException (" + ex.getMessage() + ")";
        }
    }

    @WebMethod(operationName = "returnMouvement")
    public String returnMouvement(@WebParam(name = "employe") Long employe, @WebParam(name = "heure") String _heure, @WebParam(name = "societe") Long societe) {
        String result = load(0, employe, societe);
        try {
            return ActionsPointage.returnMouvement(dm.parse(_heure), currentEmploye, currentScte, currentParam);
        } catch (ParseException ex) {
            return "ES (WS) : " + result + " -- ParseException (" + ex.getMessage() + ")";
        }
    }
}

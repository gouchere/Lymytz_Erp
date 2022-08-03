/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users.messagerie.chat;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;
//import javax.websocket.EndpointConfig;
//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.PathParam;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.users.YvsUsers;

/**
 *
 * @author Lymytz_Serveur
 */
//@ServerEndpoint(value = "/chat/{user}")
@ManagedBean
@SessionScoped
public class ChatServer implements Serializable {

    private final Logger log = Logger.getLogger(getClass().getName());
    private static List<YvsUsers> listUser;
    public static List<String> listChanel = new ArrayList<>();
    @EJB
    public DaoInterfaceLocal dao;
    @Resource(name = "jdbc/lymytz-erp", mappedName = "jdbc/lymytz-erp")
    DataSource ds;

    public ChatServer() {
        listUser = new ArrayList<>();
    }

//    @OnOpen
//    public void openSession(Session session, EndpointConfig config, @PathParam("user") String idUser) {
////        connecte toi et alerte toutes les personne connecté de ta présence
//        log.info("connecté");
//        long id = Long.parseLong(idUser);
//        YvsUsers u = findUser(id);
//        
//        session.getUserProperties().put("user", u);
//        publishConnect(session);
////        log.info(u.toString());
////        System.err.println("-------A");
//    }
//
//    @OnMessage
//    public void receivedMessage(String message, Session session) {
//        BeanCom bean=parse(message);
//        if(bean.getTypeMessage()==3){
//            //calcule le canal de communication entre sender et receiver
//            String chanel= getChanel(bean);
//            log.info(chanel);
//        }
//    }
//
//    @OnError
//    public void erreur(Throwable error) {
////        System.err.println("---- erreur..." + error.getMessage());
//    }
//
//    @OnClose
//    public void close(Session s) {
//        publishDisconnect(s);
//    }
//
//    public void loadUser() {
//        String champ[] = new String[]{"actif", "societe"};
//        Object val[] = new Object[]{true, new YvsSocietes((long) 1)};
//        listUser = dao.loadNameQueries("YvsUsers.findBySociete", champ, val);
//    }
//
//    public YvsUsers findUser(long id) {
//        return listUser.get(listUser.indexOf(new YvsUsers(id)));
//    }
//
//    private void publishConnect(Session s) {
//        long id = ((YvsUsers) s.getUserProperties().get("user")).getId();
//        String chaine = "{\"typeMessage\":\"20\", \"idReceiver\":\"" + id + "\"}";
//        for (Session ss : s.getOpenSessions()) {
//            try {
//                ss.getBasicRemote().sendText(chaine);
//            } catch (IOException ex) {
//                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//
//    private void publishDisconnect(Session s) {
//        long id = ((YvsUsers) s.getUserProperties().get("user")).getId();
//        String chaine = "{\"typeMessage\":\"21\", \"idReceiver\":\"" + id + "\"}";
//        for (Session ss : s.getOpenSessions()) {
//            try {
//                ss.getBasicRemote().sendText(chaine);
//            } catch (IOException ex) {
//                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }

    private BeanCom parse(String msg) {
        BeanCom b = new BeanCom();
        try {
            JSONObject mp = new JSONObject(msg);
            b.setMsg(mp.getString("message"));
            b.setIdReceiver(mp.getInt("idReceiver"));
            b.setIdSender(mp.getInt("idSender"));
            b.setSender(mp.getString("sender"));
            b.setReceiver(mp.getString("receiver"));
            b.setTypeMessage(mp.getInt("typeMessage"));
//            b.setHour((Date) mp.get("hour")); 
        } catch (JSONException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
    }
    
    private String getChanel(BeanCom c) {
        for (String st : listChanel) {
            if (st.equals(c.getIdSender() + "-" + c.getIdReceiver()) || st.equals(c.getIdReceiver() + "-" + c.getIdSender())) {
                return st;
            }
        }
        listChanel.add((c.getIdSender() + "-" + c.getIdReceiver()));
        return (c.getIdSender() + "-" + c.getIdReceiver());
    }
}

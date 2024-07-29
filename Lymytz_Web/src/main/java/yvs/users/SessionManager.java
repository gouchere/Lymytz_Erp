///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.users;
//
//import java.util.ArrayList;
//import java.util.List;
//import javax.servlet.http.HttpSession;
//import javax.servlet.http.HttpSessionEvent;
//import javax.servlet.http.HttpSessionListener;
//
///**
// *
// * @author Lymytz Dowes
// */
//public class SessionManager implements HttpSessionListener {
//
//    public static List<Sessions> sessions = new ArrayList<>();
//
//    @Override
//    public void sessionCreated(HttpSessionEvent event) {
//        sessions.add(new Sessions(event.getSession().getId(), event.getSession()));
//    }
//
//    @Override
//    public void sessionDestroyed(HttpSessionEvent event) {
//        sessions.remove(new Sessions(event.getSession().getId()));
//    }
//
//    public static boolean isActive(String sessionId) {
//        return sessions.contains(new Sessions(sessionId));
//    }
//
//    public static boolean invalidate(String sessionId) {
//        if (isActive(sessionId)) {
//            HttpSession session = sessions.get(sessions.indexOf(new Sessions(sessionId))).getValeur();
//            if (session != null) {
//                session.invalidate();
//                return true;
//            }
//        }
//        return false;
//    }
//}

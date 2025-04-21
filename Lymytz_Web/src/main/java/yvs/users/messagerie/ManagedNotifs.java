/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users.messagerie;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.param.workflow.YvsNotificationUsers;
import yvs.util.Managed;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "Mnotif")
@SessionScoped
public class ManagedNotifs extends Managed<ManagedNotifs, YvsBaseCaisse> implements Serializable {

    private String messageNotif;
    private String titleNotif;
    private String destinataire;
    private List<Notifications> listNotif;
    private List<Notifications> listNotifF;
    private List<Notifications> selectedNotif;
    private long nbreMsg, nbreAlert, nbreConnecte;

    public ManagedNotifs() {
        listNotif = new ArrayList<>();
        selectedNotif = new ArrayList<>();
    }

    public String getTitleNotif() {
        return titleNotif;
    }

    public void setTitleNotif(String titleNotif) {
        this.titleNotif = titleNotif;
    }

    public String getMessageNotif() {
        return messageNotif;
    }

    public void setMessageNotif(String messageNotif) {
        this.messageNotif = messageNotif;
    }

    public List<Notifications> getSelectedNotif() {
        return selectedNotif;
    }

    public void setSelectedNotif(List<Notifications> selectedNotif) {
        this.selectedNotif = selectedNotif;
    }

    public List<Notifications> getListNotif() {
        return listNotif;
    }

    public void setListNotif(List<Notifications> listNotif) {
        this.listNotif = listNotif;
    }

    public List<Notifications> getListNotifF() {
        return listNotifF;
    }

    public void setListNotifF(List<Notifications> listNotifF) {
        this.listNotifF = listNotifF;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public long getNbreAlert() {
        return nbreAlert;
    }

    public void setNbreAlert(long nbreAlert) {
        this.nbreAlert = nbreAlert;
    }

    public long getNbreConnecte() {
        return nbreConnecte;
    }

    public void setNbreConnecte(long nbreConnecte) {
        this.nbreConnecte = nbreConnecte;
    }

    public long getNbreMsg() {
        return nbreMsg;
    }

    public void setNbreMsg(long nbreMsg) {
        this.nbreMsg = nbreMsg;
    }

    @Override
    public boolean controleFiche(ManagedNotifs bean) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ManagedNotifs recopieView() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void populateView(ManagedNotifs bean) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        //modifier la propriété vue.
        Notifications nt=(Notifications)ev.getObject();  
        System.err.println(nt.getId());
        if (selectedNotif.size() == 1) {
            messageNotif = nt.getMessages();
            titleNotif = nt.getTitle();
            destinataire = nt.getDestinataire();
            dao.updateVueNotif(nt.getIdUser(), nt.getIdNotif());
        }
    }

    @Override
    public void loadAll() {
        //récupère les mesages de l'utilisateur connecté et les messages à diffusion
        String[] ch = {"users"};
        Object[] val = {currentUser.getId()};
        List<YvsNotificationUsers> l = dao.loadNameQueries("YvsNotificationUsers.findByUsers", ch, val,0, 100);
        listNotif.clear();
        selectedNotif.clear();
        for (YvsNotificationUsers not : l) {
            Notifications no = new Notifications();
            no.setId(not.getYvsNotifications().getId());
            no.setDate(not.getYvsNotifications().getDateNotif());
            no.setDestinataire("Système");
            no.setHeure(not.getYvsNotifications().getHeureNotif());
            no.setMessages(not.getMessage());
            no.setTitle(not.getYvsNotifications().getTitre());
            no.setIdNotif(not.getYvsNotifications().getId());
            no.setIdUser(not.getYvsUsers().getId());
            no.setVue(not.isVue());
            listNotif.add(no);
        }
    }

    public void countMsg() {
        setNbreMsg(countNbreMsg(currentUser.getId()));
    }
    @Resource(name = "lymytzDS", mappedName = "lymytzDS")
    DataSource ds;

    public long countNbreMsg(long user) {
        String requete = "SELECT COUNT(*) FROM yvs_notification_users WHERE vue is false AND users=?";
        long result = 0; 
        try {
            Connection cn=ds.getConnection();
            PreparedStatement ps = cn.prepareStatement(requete);
            ps.setLong(1, user);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result = (long) rs.getObject(1);
                }
            }
            cn.close();          
        } catch (SQLException ex) {
            Logger.getLogger(ManagedNotifs.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.th.workflow;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.param.workflow.YvsAbonnementUsersTaches;
import yvs.entity.param.workflow.YvsAbonnementUsersTachesPK;
import yvs.entity.grh.taches.YvsGrhTaches;
import yvs.entity.users.YvsUsers;
import yvs.users.Users;
import yvs.util.Managed;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "Mworkf")
@SessionScoped
public class ManagedWorkflow extends Managed<ManagedWorkflow, YvsBaseCaisse> implements Serializable {

    private String tache;
    private long idTache;
    private boolean ajout;
    private boolean modif;
    private boolean remove;
    private String msgAjout;
    private String msgModif;
    private String msgRemove;
    private List<Users> listUsers;
    private List<Users> listUsersF;
    private List<Taches> listTache;
    private List<Taches> listTacheF;
    @ManagedProperty(value = "#{users}")
    private Users user;
    private String libelleTache;
    private String module;
    private List<Users> selectedUsers;

    public ManagedWorkflow() {
        listUsers = new ArrayList<>();
        listTache = new ArrayList<>();
        selectedUsers= new ArrayList<>();
    }

    public boolean isAjout() {
        return ajout;
    }

    public void setAjout(boolean ajout) {
        this.ajout = ajout;
    }

    public List<Taches> getListTache() {
        return listTache;
    }

    public void setListTache(List<Taches> listTache) {
        this.listTache = listTache;
    }

    public List<Taches> getListTacheF() {
        return listTacheF;
    }

    public void setListTacheF(List<Taches> listTacheF) {
        this.listTacheF = listTacheF;
    }

    public List<Users> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<Users> listUsers) {
        this.listUsers = listUsers;
    }

    public List<Users> getListUsersF() {
        return listUsersF;
    }

    public void setListUsersF(List<Users> listUsersF) {
        this.listUsersF = listUsersF;
    }

    public boolean isModif() {
        return modif;
    }

    public void setModif(boolean modif) {
        this.modif = modif;
    }

    public String getMsgAjout() {
        return msgAjout;
    }

    public void setMsgAjout(String msgAjout) {
        this.msgAjout = msgAjout;
    }

    public String getMsgModif() {
        return msgModif;
    }

    public void setMsgModif(String msgModif) {
        this.msgModif = msgModif;
    }

    public String getMsgRemove() {
        return msgRemove;
    }

    public void setMsgRemove(String msgRemove) {
        this.msgRemove = msgRemove;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public String getTache() {
        return tache;
    }

    public void setTache(String tache) {
        this.tache = tache;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getLibelleTache() {
        return libelleTache;
    }

    public void setLibelleTache(String libelleTache) {
        this.libelleTache = libelleTache;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public List<Users> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<Users> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public long getIdTache() {
        return idTache;
    }

    public void setIdTache(long idTache) {
        this.idTache = idTache;
    }

    @Override
    public void loadAll() {
        String[] ch = {"societe"};
        Object[] val = {currentAgence.getSociete().getId()};
        List<YvsUsers> l = dao.loadNameQueries("YvsUsers.findAll", ch, val);
        listUsers.clear();
        for (YvsUsers g : l) {
//            listUsers.add(user.buildBeanFromENtity(g, dft));
        }
        //charger la liste des taches à notifier
        String[] ch1 = {};
        Object[] val1 = {};
        List<YvsGrhTaches> lt = dao.loadNameQueries("YvsTaches.findAll", ch1, val1);
        listTache.clear();
        for (YvsGrhTaches t : lt) {
            Taches ta = new Taches();
            ta.setId(t.getId());
//            ta.setLibelle(t.getLibelle());
//            ta.setModule(t.getModule());
            listTache.add(ta);
        }
    }

    public void addTache() {
        YvsGrhTaches t = new YvsGrhTaches();
//        t.setLibelle(libelleTache);
//        t.setModule(module);
        dao.save(t);
        succes();
    }

    public void saveAbonnement() {
        System.err.println(selectedUsers);
        if (selectedUsers != null) {
            if (selectedUsers.isEmpty()) {
                getMessage("Vous devez choisir les utilisateurs à notifier", FacesMessage.SEVERITY_ERROR);
                return;
            }
            for (Users u : selectedUsers) {
                YvsAbonnementUsersTaches entity = new YvsAbonnementUsersTaches();
                entity.setAjout(ajout);
                entity.setMessageAjout(msgAjout);
                entity.setMessageModif(msgModif);
                entity.setMessageRemove(msgRemove);
                entity.setModif(modif);
                entity.setMove(remove);
                entity.setYvsAbonnementUsersTachesPK(new YvsAbonnementUsersTachesPK(u.getId(), idTache));
                entity.setYvsUsers(new YvsUsers(u.getId()));
                entity.setYvsTaches(new YvsGrhTaches(idTache));
                dao.save(entity);
            }
            succes();
        }
    }

    public void chooseTache(ValueChangeEvent ev) {
        //charge les utilisateur à l'écoute de cette tâche
        long idT = (long) ev.getNewValue();
        String[] ch = {"taches"};
        Object[] val = {idT};
        List<YvsAbonnementUsersTaches> l = dao.loadNameQueries("YvsAbonnementUsersTaches.findByTaches", ch, val);
        int i = 0;
        selectedUsers.clear();
        for (YvsAbonnementUsersTaches ab : l) {
//            Users u = user.buildBeanFromENtity(ab.getYvsUsers(), dft);
//            selectedUsers.add(u);
            i++;
        }
    }

    @Override
    public boolean controleFiche(ManagedWorkflow bean) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ManagedWorkflow recopieView() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void populateView(ManagedWorkflow bean) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

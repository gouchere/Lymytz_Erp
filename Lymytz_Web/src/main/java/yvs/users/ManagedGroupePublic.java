/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.users.YvsMsgDiffusionContact;
import yvs.entity.users.YvsMsgGroupeDiffusion;
import yvs.entity.users.YvsUsers;
import yvs.parametrage.societe.Societe;
import yvs.users.messagerie.DiffusionContact;
import yvs.users.messagerie.GroupeDiffusion;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedGroupePublic extends Managed<GroupeDiffusion, YvsMsgGroupeDiffusion> implements Serializable {

    @ManagedProperty(value = "#{groupeDiffusion}")
    private GroupeDiffusion groupeDiffusion;
    private List<GroupeDiffusion> listGroupeDiffusion, listSelectGroupeDiffusion;
    private boolean updateGroupeDiffusion, selectGroupeDiffusion, optionUpdate, viewList = true, selectUsers, selectDiffusionContact;
    private List<Users> listUsers, listSelectUsers;
    private List<DiffusionContact> listSelectDiffusionContact;
    YvsMsgGroupeDiffusion entityGroupe;

    public ManagedGroupePublic() {
        listGroupeDiffusion = new ArrayList<>();
        listSelectGroupeDiffusion = new ArrayList<>();
        listUsers = new ArrayList<>();
        listSelectUsers = new ArrayList<>();
        listSelectDiffusionContact = new ArrayList<>();
    }

    public boolean isSelectUsers() {
        return selectUsers;
    }

    public void setSelectUsers(boolean selectUsers) {
        this.selectUsers = selectUsers;
    }

    public boolean isSelectDiffusionContact() {
        return selectDiffusionContact;
    }

    public void setSelectDiffusionContact(boolean selectDiffusionContact) {
        this.selectDiffusionContact = selectDiffusionContact;
    }

    public List<DiffusionContact> getListSelectDiffusionContact() {
        return listSelectDiffusionContact;
    }

    public void setListSelectDiffusionContact(List<DiffusionContact> listSelectDiffusionContact) {
        this.listSelectDiffusionContact = listSelectDiffusionContact;
    }

    public boolean isViewList() {
        return viewList;
    }

    public void setViewList(boolean viewList) {
        this.viewList = viewList;
    }

    public List<GroupeDiffusion> getListGroupeDiffusion() {
        return listGroupeDiffusion;
    }

    public void setListGroupeDiffusion(List<GroupeDiffusion> listGroupeDiffusion) {
        this.listGroupeDiffusion = listGroupeDiffusion;
    }

    public List<GroupeDiffusion> getListSelectGroupeDiffusion() {
        return listSelectGroupeDiffusion;
    }

    public void setListSelectGroupeDiffusion(List<GroupeDiffusion> listSelectGroupeDiffusion) {
        this.listSelectGroupeDiffusion = listSelectGroupeDiffusion;
    }

    public boolean isUpdateGroupeDiffusion() {
        return updateGroupeDiffusion;
    }

    public void setUpdateGroupeDiffusion(boolean updateGroupeDiffusion) {
        this.updateGroupeDiffusion = updateGroupeDiffusion;
    }

    public boolean isSelectGroupeDiffusion() {
        return selectGroupeDiffusion;
    }

    public void setSelectGroupeDiffusion(boolean selectGroupeDiffusion) {
        this.selectGroupeDiffusion = selectGroupeDiffusion;
    }

    public boolean isOptionUpdate() {
        return optionUpdate;
    }

    public void setOptionUpdate(boolean optionUpdate) {
        this.optionUpdate = optionUpdate;
    }

    public List<Users> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<Users> listUsers) {
        this.listUsers = listUsers;
    }

    public List<Users> getListSelectUsers() {
        return listSelectUsers;
    }

    public void setListSelectUsers(List<Users> listSelectUsers) {
        this.listSelectUsers = listSelectUsers;
    }

    public YvsMsgGroupeDiffusion getEntityGroupe() {
        return entityGroupe;
    }

    public void setEntityGroupe(YvsMsgGroupeDiffusion entityGroupe) {
        this.entityGroupe = entityGroupe;
    }

    public GroupeDiffusion getGroupeDiffusion() {
        return groupeDiffusion;
    }

    public void setGroupeDiffusion(GroupeDiffusion groupeDiffusion) {
        this.groupeDiffusion = groupeDiffusion;
    }

    public YvsMsgGroupeDiffusion buildGroupeDiffusion(GroupeDiffusion g) {
        YvsMsgGroupeDiffusion r = new YvsMsgGroupeDiffusion();
        if (g != null) {
            r.setId(g.getId());
            r.setLibelle(g.getLibelle());
            r.setReference(g.getReference());
            r.setPublics(true);
            r.setSociete(currentAgence.getSociete());
        }
        return r;
    }

    public YvsMsgDiffusionContact buildDiffusionContact(DiffusionContact d) {
        YvsMsgDiffusionContact r = new YvsMsgDiffusionContact();
        if (d != null) {
            r.setActif(true);
            r.setId(d.getId());
            r.setUsers(new YvsUsers(d.getUser().getId()));
            r.setGroupeDiffusion(entityGroupe);
        }
        return r;
    }

    @Override
    public boolean controleFiche(GroupeDiffusion bean) {
        if (bean.getReference() == null) {
            getErrorMessage("Vous devez entrer la designation");
            return false;
        }
        return true;
    }

    @Override
    public GroupeDiffusion recopieView() {
        GroupeDiffusion bean = new GroupeDiffusion();
        bean.setId(groupeDiffusion.getId());
        bean.setLibelle(groupeDiffusion.getLibelle());
        bean.setReference(groupeDiffusion.getReference());
        bean.setPublics(true);
        bean.setSociete(groupeDiffusion.getSociete());
        return bean;
    }

    @Override
    public void populateView(GroupeDiffusion bean) {
        cloneObject(groupeDiffusion, bean);
        entityGroupe = buildGroupeDiffusion(groupeDiffusion);
    }

    @Override
    public void selectOnView(GroupeDiffusion bean) {
        bean.setSelectActif(!bean.isSelectActif());
        listGroupeDiffusion.get(listGroupeDiffusion.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (listSelectGroupeDiffusion.contains(bean)) {
            listSelectGroupeDiffusion.remove(bean);
        } else {
            listSelectGroupeDiffusion.add(bean);
        }
        if (listSelectGroupeDiffusion.isEmpty()) {
            resetFiche();
        } else {
            populateView(listSelectGroupeDiffusion.get(listSelectGroupeDiffusion.size() - 1));
        }
        setSelectGroupeDiffusion(!listSelectGroupeDiffusion.isEmpty());
        setUpdateGroupeDiffusion(isSelectGroupeDiffusion());
        setOptionUpdate(listSelectGroupeDiffusion.size() == 1);
        update("form_groupe_user_00");
        update("entete_groupe_user_00");
    }

    public void selectOnViewUsers(Users bean) {
        bean.setSelectActif(!bean.isSelectActif());
        listUsers.get(listUsers.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (listSelectUsers.contains(bean)) {
            listSelectUsers.remove(bean);
        } else {
            listSelectUsers.add(bean);
        }
        setSelectUsers(!listSelectUsers.isEmpty());
        update("data_contact_groupe_00");
        update("footer_contact_groupe_00");
    }

    public void selectOnViewDiffusionContact(DiffusionContact bean) {
        bean.setSelectActif(!bean.isSelectActif());
        groupeDiffusion.getDiffusionContactList().get(groupeDiffusion.getDiffusionContactList().indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (listSelectDiffusionContact.contains(bean)) {
            listSelectDiffusionContact.remove(bean);
        } else {
            listSelectDiffusionContact.add(bean);
        }
        setSelectDiffusionContact(!listSelectDiffusionContact.isEmpty());
        update("form_groupe_user_02");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            GroupeDiffusion bean = (GroupeDiffusion) ev.getObject();
            selectOnView(bean);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            GroupeDiffusion bean = (GroupeDiffusion) ev.getObject();
            selectOnView(bean);
        }
    }

    public void loadOnViewUsers(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            Users bean = (Users) ev.getObject();
            selectOnViewUsers(bean);
        }
    }

    public void unLoadOnViewUsers(UnselectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            Users bean = (Users) ev.getObject();
            selectOnViewUsers(bean);
        }
    }

    public void loadOnViewDiffusionContact(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            DiffusionContact bean = (DiffusionContact) ev.getObject();
            selectOnViewDiffusionContact(bean);
        }
    }

    public void unLoadOnViewDiffusionContact(UnselectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            DiffusionContact bean = (DiffusionContact) ev.getObject();
            selectOnViewDiffusionContact(bean);
        }
    }

    @Override
    public void loadAll() {
        listGroupeDiffusion.clear();
        String[] champ = new String[]{"societe"};
        Object[] val = new Object[]{currentAgence.getSociete()};
        listGroupeDiffusion = UtilUsers.buildBeanListGroupeDiffusion(dao.loadNameQueries("YvsMsgGroupeDiffusion.findBySociete", champ, val));
    }

    public void loadAllUser() {
        listUsers.clear();
        String[] champ = new String[]{"societe", "actif"};
        Object[] val = new Object[]{currentAgence.getSociete(), true};
        listUsers = UtilUsers.buildBeanListUsers(dao.loadNameQueries("YvsUsers.findBySociete", champ, val));

    }

    @Override
    public void resetFiche() {
        resetFiche(groupeDiffusion);
        groupeDiffusion.setSociete(new Societe());
        groupeDiffusion.setDiffusionContactList(new ArrayList<DiffusionContact>());
        setUpdateGroupeDiffusion(false);
        setOptionUpdate(false);
        setSelectGroupeDiffusion(false);
        setSelectDiffusionContact(false);
        setSelectUsers(false);
        resetPage();
        update("form_groupe_user_01");
    }

    @Override
    public void resetPage() {
        for (GroupeDiffusion bean : listGroupeDiffusion) {
            listGroupeDiffusion.get(listGroupeDiffusion.indexOf(bean)).setSelectActif(false);
        }
    }

    @Override
    public void deleteBean() {
        if (!listSelectGroupeDiffusion.isEmpty()) {
            for (GroupeDiffusion g : listSelectGroupeDiffusion) {
                dao.delete(new YvsMsgGroupeDiffusion(g.getId()));
                listGroupeDiffusion.remove(g);
            }
            resetFiche();
            succes();
            update("entete_groupe_user_00");
            update("body_groupe_user_00");
        }
    }

    @Override
    public void updateBean() {
        setViewList(false);
        setSelectGroupeDiffusion(false);
        update("entete_groupe_user_00");
        update("body_groupe_user_00");
    }

    @Override
    public boolean saveNew() {
        GroupeDiffusion bean = recopieView();
        if (controleFiche(bean)) {
            entityGroupe = buildGroupeDiffusion(bean);
            if (!isUpdateGroupeDiffusion()) {
                entityGroupe.setId(null);
                entityGroupe = (YvsMsgGroupeDiffusion) dao.save1(entityGroupe);
                bean.setId(entityGroupe.getId());
                groupeDiffusion.setId(bean.getId());
                listGroupeDiffusion.add(bean);
            } else {
                dao.update(entityGroupe);
                listGroupeDiffusion.set(listGroupeDiffusion.indexOf(groupeDiffusion), bean);
            }
            setUpdateGroupeDiffusion(true);
            succes();
            update("form_groupe_user_01");
        }
        return true;
    }

    @Override
    public void changeView() {
        setViewList(!isViewList());
        resetFiche();
        update("entete_groupe_user_00");
        update("body_groupe_user_00");
    }

    public void addContactInGroupe() {
        if (!listSelectUsers.isEmpty()) {
            for (Users u : listSelectUsers) {
                DiffusionContact bean = new DiffusionContact();
                bean.setUser(u);
                YvsMsgDiffusionContact entity = buildDiffusionContact(bean);
                entity.setId(null);
                entity = (YvsMsgDiffusionContact) dao.save1(entity);
                bean.setId(entity.getId());
                groupeDiffusion.getDiffusionContactList().add(bean);
            }
            succes();
            update("form_groupe_user_02");
        }
    }

    public void deleteContactOnGroupe() {
        if (!listSelectDiffusionContact.isEmpty()) {
            for (DiffusionContact d : listSelectDiffusionContact) {
                dao.delete(new YvsMsgDiffusionContact(d.getId()));
                groupeDiffusion.getDiffusionContactList().remove(d);
            }
            succes();
            update("form_groupe_user_02");
        }
    }
}

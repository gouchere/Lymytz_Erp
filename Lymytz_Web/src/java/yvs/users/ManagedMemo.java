/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.users.YvsUsersMemo;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedMemo extends Managed<UsersMemo, YvsUsersMemo> implements Serializable {

//    @ManagedProperty(value = "#{usersMemo}")
    private UsersMemo user=new UsersMemo();
    private List<YvsUsersMemo> memos;
    private YvsUsersMemo selectCond;

    private String tabIds;
    private int position = -1;

    private String numSearch;
    private boolean date;
    private Date dateDebut = new Date(), dateFin = new Date();

    public ManagedMemo() {
        memos = new ArrayList<>();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public UsersMemo getUser() {
        return user;
    }

    public void setUser(UsersMemo user) {
        this.user = user;
    }

    public YvsUsersMemo getSelectCond() {
        return selectCond;
    }

    public void setSelectCond(YvsUsersMemo selectCond) {
        this.selectCond = selectCond;
    }

    public List<YvsUsersMemo> getMemos() {
        return memos;
    }

    public void setMemos(List<YvsUsersMemo> memos) {
        this.memos = memos;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public boolean getDate() {
        return date;
    }

    public void setDate(boolean date) {
        this.date = date;
    }

    @Override
    public void loadAll() {
        loadAll(true, true);
        tabIds = "";
        position = -1;
    }

    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.users", "users", currentUser, "=", "AND"));
        memos = paginator.executeDynamicQuery("YvsUsersMemo", "y.dateDebutRappel DESC, y.id", avance, init, (int) imax, dao);
    }

    @Override
    public UsersMemo recopieView() {
        return user;
    }

    @Override
    public void populateView(UsersMemo bean) {
        cloneObject(user, bean);
    }

    @Override
    public boolean controleFiche(UsersMemo bean) {
        if (bean.getTitre() == null || bean.getTitre().trim().equals("")) {
            getErrorMessage("Vous devez entrer le titre");
            return false;
        }
        return true;
    }

    @Override
    public void resetFiche() {
        resetFiche(user);
        user.setUsers(new Users());
        position = -1;
        tabIds = "";
        update("blog_form_users_memo");
    }

    @Override
    public boolean saveNew() {
        try {
            UsersMemo bean = recopieView();
            if (controleFiche(bean)) {
                YvsUsersMemo y = UtilUsers.buildUsersMemo(bean, currentUser.getUsers(), currentUser);
                if (bean.getId() > 0) {
                    dao.update(y);
                    memos.set(memos.indexOf(y), y);
                } else {
                    y.setId(null);
                    y = (YvsUsersMemo) dao.save1(y);
                    memos.add(0, y);
                }
                succes();
                update("data_users_memo");
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
            return false;
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsUsersMemo bean = memos.get(memos.indexOf(new YvsUsersMemo(id)));
                    dao.delete(new YvsUsersMemo(bean.getId()));
                    memos.remove(bean);
                }
                resetFiche();
                succes();
                update("data_users_memo");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
        }
    }

    public void deleteBean_(YvsUsersMemo y, boolean open) {
        selectCond = y;
        if (!open) {
            deleteBean_();
        }
    }

    public void deleteBean_() {
        try {
            if (selectCond != null) {
                dao.delete(selectCond);
                memos.remove(selectCond);
                resetFiche();
                succes();
                update("data_users_memo");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadMemoByPosition() {
        if (position > -1 && memos.size() > position) {
            YvsUsersMemo y = memos.get(position);
            if (y != null ? y.getId() > 0 : false) {
                selectOnView(y);
            }
        } else {
            resetFiche();
        }
    }

    private void selectOnView(YvsUsersMemo y) {
        populateView(UtilUsers.buildBeanUsersMemo(y));
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsUsersMemo f = (YvsUsersMemo) ev.getObject();
            selectOnView(f);
            update("blog_form_users_memo");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void onChangeDuree() {
        Calendar c = Calendar.getInstance();
        c.setTime(user.getDateDebutRappel());
        c.add(Calendar.DAY_OF_YEAR, user.getDureeRappel());
        user.setDateFinRappel(c.getTime());
    }

    public void init(boolean next) {
        loadAll(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.titre", "reference", null, "LIKE", "AND");
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", numSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.titre)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.description)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateDebutRappel", "dates", null, "BETWEEN", "AND");
        if (date) {
            p = new ParametreRequete(null, "dates", dateDebut, "BETWEEN", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.dateMemo", "dates", dateDebut, dateFin, "BETWEEN", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.dateDebutRappel", "dates", dateDebut, dateFin, "BETWEEN", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.dateFinRappel", "dates", dateDebut, dateFin, "BETWEEN", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }
}

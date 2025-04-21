/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.base;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.mutuelle.base.YvsMutTypePrime;
import yvs.mutuelle.UtilMut;
import yvs.util.Constantes;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedTypePrime extends Managed<TypePrime, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{typePrime}")
    private TypePrime typePrime;
    private List<YvsMutTypePrime> types;

    private String tabIds, input_reset;
    private boolean updateType;

    public ManagedTypePrime() {
        types = new ArrayList<>();
    }

    public TypePrime getTypePrime() {
        return typePrime;
    }

    public void setTypePrime(TypePrime typePrime) {
        this.typePrime = typePrime;
    }

    public List<YvsMutTypePrime> getTypes() {
        return types;
    }

    public void setTypes(List<YvsMutTypePrime> types) {
        this.types = types;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getInput_reset() {
        return input_reset;
    }

    public void setInput_reset(String input_reset) {
        this.input_reset = input_reset;
    }

    public boolean isUpdateType() {
        return updateType;
    }

    public void setUpdateType(boolean updateType) {
        this.updateType = updateType;
    }

    @Override
    public void loadAll() {
        
        loadAllTypePrime();
        tabIds = "";
    }

    public void loadAllTypePrime() {
        types = dao.loadNameQueries("YvsMutTypePrime.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
    }

    public YvsMutTypePrime buildTypePrime(TypePrime y) {
        YvsMutTypePrime t = new YvsMutTypePrime();
        if (y != null) {
            t.setDesignation(y.getDesignation());
            t.setId(y.getId());
            t.setMontantMaximal(y.getMontantMaximal());
            t.setNatureMontant(y.getNatureMontant());
            t.setPeriodeRemuneration(y.getPeriodeRemuneration());
            t.setMutuelle(currentMutuel);
            t.setAuthor(currentUser);
            t.setDateSave(y.getDateSave());
            t.setDateUpdate(new Date());
        }
        return t;
    }

    @Override
    public TypePrime recopieView() {
        TypePrime t = new TypePrime();
        t.setDesignation(typePrime.getDesignation());
        t.setId(typePrime.getId());
        t.setMontantMaximal(typePrime.getMontantMaximal());
        t.setPeriodeRemuneration(typePrime.getPeriodeRemuneration());
        t.setNatureMontant(typePrime.getNatureMontant());
        return t;
    }

    @Override
    public boolean controleFiche(TypePrime bean) {
        if (bean.getDesignation() == null || bean.getDesignation().equals("")) {
            getErrorMessage("Vous devez entrer la designation");
            return false;
        }
        if (currentMutuel != null ? currentMutuel.getId() < 1 : true) {
            getErrorMessage("Vous devez etre connecté dans une mutuelle");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(TypePrime bean) {
        cloneObject(typePrime, bean);
        setUpdateType(true);
    }

    @Override
    public void resetFiche() {
        resetFiche(typePrime);
        typePrime.setNatureMontant(Constantes.MUT_TYPE_MONTANT_FIXE);
        typePrime.setPeriodeRemuneration(Constantes.MUT_PERIODE_ANNEE);
        tabIds = "";
        input_reset = "";
        setUpdateType(false);
    }

    @Override
    public boolean saveNew() {
        if (input_reset != null && input_reset.equals("reset")) {
            setUpdateType(false);
            input_reset = "";
        }
        String action = isUpdateType() ? "Modification" : "Insertion";
        try {
            TypePrime bean = recopieView();
            bean.setNew_(true);
            if (controleFiche(bean)) {
                YvsMutTypePrime entyPoste = buildTypePrime(bean);
                if (!isUpdateType()) {
                    entyPoste.setId(null);
                    entyPoste = (YvsMutTypePrime) dao.save1(entyPoste);
                    bean.setId(entyPoste.getId());
                    typePrime.setId(entyPoste.getId());
                    types.add(0, entyPoste);
                } else {
                    dao.update(entyPoste);
                    types.set(types.indexOf(entyPoste), entyPoste);
                }
                succes();
                resetFiche();
                update("type_prime_poste");
            }
        } catch (Exception ex) {
           Logger.getLogger(ManagedTypePrime.class.getName()).log(Level.SEVERE, null, ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                String[] ids = tabIds.split("-");
                if ((ids != null) ? ids.length > 0 : false) {
                    for (String s : ids) {
                        long id = Integer.valueOf(s);
                        YvsMutTypePrime bean = types.get(types.indexOf(new YvsMutTypePrime(id)));
                        dao.delete(new YvsMutTypePrime(bean.getId()));
                        types.remove(bean);
                    }
                    resetFiche();
                    succes();
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            Logger.getLogger(ManagedTypePrime.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBean(YvsMutTypePrime y) {
        try {
            if (y != null) {
                dao.delete(y);
                types.remove(y);

                resetFiche();
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    @Override
    public void updateBean() {
        if ((tabIds != null) ? tabIds.length() > 0 : false) {
            String[] ids = tabIds.split("-");
            setUpdateType((ids != null) ? ids.length > 0 : false);
            if (isUpdateType()) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsMutTypePrime bean = types.get(types.indexOf(new YvsMutTypePrime(id)));
                populateView(UtilMut.buildBeanTypePrime(bean));
                tabIds = "";
                input_reset = "";
                update("blog_form_type_prime_");
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutTypePrime bean = (YvsMutTypePrime) ev.getObject();
            populateView(UtilMut.buildBeanTypePrime(bean));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void chooseNatureMontant() {
        if ((typePrime != null) ? typePrime.getNatureMontant() != null : false) {
            switch (typePrime.getNatureMontant()) {
                case "Fixe":
                    typePrime.setSuffixeMontant("Fcfa");
                    break;
                case "Pourcentage":
                    typePrime.setSuffixeMontant("% Salaire");
                    break;
                default:
                    break;
            }
        }
    }

}

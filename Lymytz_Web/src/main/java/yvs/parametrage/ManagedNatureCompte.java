/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage;

import yvs.base.compta.NatureCompte;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.UtilCompta;
import yvs.entity.compta.YvsBaseNatureCompte;
import yvs.entity.compta.YvsBaseRadicalCompte;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class ManagedNatureCompte extends Managed<NatureCompte, YvsBaseNatureCompte> implements Serializable {

    private NatureCompte nature = new NatureCompte();
    private List<RadicalCompte> listRadical;
    private YvsBaseNatureCompte selectNature;
    private RadicalCompte selectedRadical;
    private List<String> listNature;
    private boolean renderTab;
    private YvsBaseRadicalCompte radical = new YvsBaseRadicalCompte();
    private List<YvsBaseNatureCompte> listeNatures;

    private String codeSearch, sensSearch, reportSearch;
    private Boolean actifSearch;

    public ManagedNatureCompte() {
        listRadical = new ArrayList<>();
        listNature = new ArrayList<>();
        listeNatures = new ArrayList<>();
        listNature.add("AUTRES TIERS");
        listNature.add("COMMANDE");
        listNature.add("CLIENT");
        listNature.add("CHARGES");
        listNature.add("CAPITAL");
        listNature.add("CHARGE HAO");
        listNature.add("FOURNISSEUR");
        listNature.add("IMMOBILISATION");
        listNature.add("REPRESENTANT");
        listNature.add("SALARIE");
        listNature.add("TRESORERIE");
        listNature.add("RISTOURNE");
    }

    public YvsBaseNatureCompte getSelectNature() {
        return selectNature;
    }

    public void setSelectNature(YvsBaseNatureCompte selectNature) {
        this.selectNature = selectNature;
    }

    public String getCodeSearch() {
        return codeSearch;
    }

    public void setCodeSearch(String codeSearch) {
        this.codeSearch = codeSearch;
    }

    public String getSensSearch() {
        return sensSearch;
    }

    public void setSensSearch(String sensSearch) {
        this.sensSearch = sensSearch;
    }

    public String getReportSearch() {
        return reportSearch;
    }

    public void setReportSearch(String reportSearch) {
        this.reportSearch = reportSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public List<YvsBaseNatureCompte> getListeNatures() {
        return listeNatures;
    }

    public void setListeNatures(List<YvsBaseNatureCompte> listeNatures) {
        this.listeNatures = listeNatures;
    }

    public YvsBaseRadicalCompte getRadical() {
        return radical;
    }

    public void setRadical(YvsBaseRadicalCompte radical) {
        this.radical = radical;
    }

    public NatureCompte getNature() {
        return nature;
    }

    public void setNature(NatureCompte nature) {
        this.nature = nature;
    }

    public List<RadicalCompte> getListRadical() {
        return listRadical;
    }

    public void setListRadical(List<RadicalCompte> listRadical) {
        this.listRadical = listRadical;
    }

    public RadicalCompte getSelectedRadical() {
        return selectedRadical;
    }

    public void setSelectedRadical(RadicalCompte selectedRadical) {
        this.selectedRadical = selectedRadical;
    }

    public List<String> getListNature() {
        return listNature;
    }

    public void setListNature(List<String> listNature) {
        this.listNature = listNature;
    }

    public boolean isRenderTab() {
        return renderTab;
    }

    public void setRenderTab(boolean renderTab) {
        this.renderTab = renderTab;
    }

    @Override
    public boolean controleFiche(NatureCompte bean) {
        if ((bean.getDesignation() == null) ? true : bean.getDesignation().isEmpty()) {
            getErrorMessage("Vous devez entrer la désignation !");
            return false;
        }
        if (!bean.getNature().equals(Constantes.NAT_AUTRE)) {
            nameQueri = "YvsBaseNatureCompte.findByNature";
            champ = new String[]{"societe", "nature"};
            val = new Object[]{currentAgence.getSociete(), bean.getNature()};
            YvsBaseNatureCompte y = (YvsBaseNatureCompte) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
                getErrorMessage("Vous ne pouvez pas associer deux natures de compte à cette nature");
                return false;
            }
        }
        return true;
    }

    @Override
    public void resetFiche() {
        nature = new NatureCompte();
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(nature)) {
            selectNature = UtilCompta.buildNatureCompte(nature, currentUser, currentAgence.getSociete());
            if (nature.getId() <= 0) {
                selectNature.setId(null);
                selectNature = (YvsBaseNatureCompte) dao.save1(selectNature);
                nature.setId(selectNature.getId());
            } else {
                dao.update(selectNature);
            }
            int idx = listeNatures.indexOf(selectNature);
            if (idx > -1) {
                listeNatures.set(idx, selectNature);
            } else {
                listeNatures.add(0, selectNature);
            }
            //sauvegarde les radicaux
            for (YvsBaseRadicalCompte rc : nature.getRadicals()) {
                rc.setDateUpdate(new Date());
                rc.setAuthor(currentUser);
                if (rc.getId() <= 0) {
                    rc.setDateSave(new Date());
                    rc.setId(null);
                    rc.setNatureCompte(selectNature);
                    rc = (YvsBaseRadicalCompte) dao.save1(rc);
                } else {
                    dao.update(rc);
                }
            }
            succes();
            update("chmp_cpt_nat");
        }
        return true;
    }

    @Override
    public void updateBean() {
        nature = recopieView();
        if (controleFiche(nature)) {
            selectNature = UtilCompta.buildNatureCompte(nature, currentUser, currentAgence.getSociete());
            dao.update(selectNature);
        }
    }

    @Override
    public NatureCompte recopieView() {
        return nature;
    }

    @Override
    public void populateView(NatureCompte bean) {
        cloneObject(nature, bean);
    }

    @Override
    public void loadAll() {
        listeNatures = dao.loadNameQueries("YvsBaseNatureCompte.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAll(boolean init, boolean avance) {
        //charge les natures de comptes
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        listeNatures = paginator.executeDynamicQuery("YvsBaseNatureCompte", "y.designation", avance, init, dao);
    }

    private List<RadicalCompte> buildRadical(List<YvsBaseRadicalCompte> l) {
        listRadical.clear();
        setNumero(1);
        for (YvsBaseRadicalCompte r : l) {
//            rc = new RadicalCompte();
//            rc.setNumero(getNumero());
//            rc.setId(r.getId());
//            rc.setRadical(r.getRadical());
//            listRadical.add(rc);
            setNumero(getNumero() + 1);
        }
        return listRadical;
    }
    // ajout des radicaux

    public void addRadical() {
        if (radical.getRadical() != null) {
            StringBuilder sb = new StringBuilder(radical.getRadical());
            radical.setRadical(sb.toString());
            if (nature.getId() > 0) {
                radical.setNatureCompte(new YvsBaseNatureCompte(nature.getId()));
                radical.setAuthor(currentUser);
                radical.setDateSave(new Date());
                radical.setDateUpdate(new Date());
                radical.setId(null);
                radical = (YvsBaseRadicalCompte) dao.save1(radical);
                nature.getRadicals().add(radical);
            } else {
                radical.setNatureCompte(null);
                radical.setId((long) 0);
                nature.getRadicals().add(radical);
            }
            for (int i = radical.getRadical().length(); i <= getLongueurCompte(); i++) {
                sb.append("X");
            }
        } else {
            getMessage("Impossible de continuer, Assuré vous d'avoir saisie un radical ou une nature de compte valide", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void toogleActiveNat(YvsBaseNatureCompte nat) {
        if (nat != null) {
            nat.setActif(!nat.getActif());
            nat.setAuthor(currentUser);
            dao.update(nat);
        }
    }

    public void deleteNature(YvsBaseNatureCompte nat) {
        if (nat != null) {
            nat.setAuthor(currentUser);
            try {
                dao.delete(nat);
                listeNatures.remove(nat);
            } catch (Exception ex) {
                log.log(Level.SEVERE, null, ex);
                getErrorMessage("Impossible de supprimer cet élément !");
            }
        }
    }
//    public void removeRadical() {
//        for (long id : listRadicaloDelete) {
//            dao.delete(new YvsBaseRadicalCompte(id));
//        }
//        listRadicaloDelete.clear();
//        succes();
//    }
//
//    public void delete() {
//        listRadicaloDelete.add(selectedRadical.getId());
//        nature.getRadicals().remove(selectedRadical);
//    }

    public void updateRadical(RowEditEvent ev) {
        RadicalCompte rad = (RadicalCompte) ev.getObject();
        if (rad.getId() != 0 && rad.getRadical() != null) {
            StringBuilder sb = new StringBuilder(rad.getRadical());
            for (int i = rad.getRadical().length(); i <= getLongueurCompte(); i++) {
                sb.append("X");
            }
            radical.setId(rad.getId());
            if (selectNature == null) {
                selectNature = new YvsBaseNatureCompte();
            }
            selectNature.setId(nature.getId());
            radical.setNatureCompte(selectNature);
            radical.setRadical(sb.toString());
            dao.update(radical);
        }
    }

    public void findAgence(String id) {
        String[] ch = {"codeagence"};
        Object[] val = {id};
        String r = (String) dao.loadObjectByNameQueries("YvsAgences.findDesignation", ch, val);
        if (r != null) {
            setTexte(r);
        } else {
            setTexte("Aucune agence avec ce code");
        }
        update("texte-nc");
        execute("giveFocus('main-tab:chprp')");
    }

    public void openNatureToUpdate(YvsBaseNatureCompte nat) {
        if (nat != null) {
            nature = UtilCompta.buildBeanNatureCompte(nat);
            openDialog("dlg_nature");
        }
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        selectNature = (YvsBaseNatureCompte) ev.getObject();
        populateView(UtilCompta.buildBeanNatureCompte(selectNature));
        update("natCompte-main1");
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void paginer(boolean avancer) {
        loadAll(false, avancer);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAll(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }

    public void clearParams() {
        paginator.clear();
        loadAll(true, true);
    }

    public void addParamDesignation() {
        ParametreRequete p = new ParametreRequete("y.designation", "designation", null);
        if ((codeSearch != null) ? !codeSearch.trim().isEmpty() : false) {
            System.err.println("codeSearch " + codeSearch);
            p = new ParametreRequete("UPPER(y.designation)", "designation", codeSearch.trim().toUpperCase() + "%", "LIKE", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamTypeReport() {
        ParametreRequete p = new ParametreRequete("y.typeReport", "typeReport", null);
        if (reportSearch != null ? !reportSearch.trim().isEmpty() : false) {
            p = new ParametreRequete("y.typeReport", "typeReport", reportSearch, "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamSens() {
        ParametreRequete p = new ParametreRequete("y.sensCompte", "sensCompte", null);
        if (sensSearch != null ? !sensSearch.trim().isEmpty() : false) {
            p = new ParametreRequete("y.sensCompte", "sensCompte", sensSearch, "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamActif() {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND");
        paginator.addParam(p);
        loadAll(true, true);
    }
}

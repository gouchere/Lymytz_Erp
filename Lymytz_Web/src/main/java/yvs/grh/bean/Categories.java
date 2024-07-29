/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.param.YvsGrhCategorieProfessionelle;
import yvs.entity.grh.param.YvsGrhCategoriePreavis;
import yvs.entity.grh.param.YvsGrhIntervalleAnciennete;
import yvs.util.Managed;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Categories extends Managed<Categories, YvsBaseCaisse> implements Serializable {

    private int id, degre;
    private String categorie;
    private long categorieActif;
    private List<Echelons> listEchelons;
    private boolean displayButonDelete;
    private String chainePreavis;
    private IntervalleAnciennete durePreavis = new IntervalleAnciennete();
    private boolean actif;
    private String chaineSelectCategorie;
    private int indexIntervalle;
    private Date dateSave = new Date();

    private List<YvsGrhCategoriePreavis> listeDureePreavis;
    private List<YvsGrhIntervalleAnciennete> listeIntervalleAnc;
    private List<YvsGrhCategorieProfessionelle> listValue;

    public Categories() {
        listValue = new ArrayList<>();
        listEchelons = new ArrayList<>();
        listeDureePreavis = new ArrayList<>();
        listeIntervalleAnc = new ArrayList<>();
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getChainePreavis() {
        return chainePreavis;
    }

    public void setChainePreavis(String chainePreavis) {
        this.chainePreavis = chainePreavis;
    }

    public List<YvsGrhCategoriePreavis> getListeDureePreavis() {
        return listeDureePreavis;
    }

    public void setListeDureePreavis(List<YvsGrhCategoriePreavis> listeDureePreavis) {
        this.listeDureePreavis = listeDureePreavis;
    }

    public int getDegre() {
        return degre;
    }

    public void setDegre(int degre) {
        this.degre = degre;
    }

    public Categories(int id) {
        this.id = id;
        listEchelons = new ArrayList<>();
    }

    public long getCategorieActif() {
        return categorieActif;
    }

    public void setCategorieActif(long categorieActif) {
        this.categorieActif = categorieActif;
    }

    public Categories(int id, String categorie) {
        this.id = id;
        this.categorie = categorie;
        listEchelons = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public List<YvsGrhCategorieProfessionelle> getListValue() {
        return listValue;
    }

    public void setListValue(List<YvsGrhCategorieProfessionelle> listValue) {
        this.listValue = listValue;
    }

    public List<Echelons> getListEchelons() {
        return listEchelons;
    }

    public void setListEchelons(List<Echelons> listEchelons) {
        this.listEchelons = listEchelons;
    }

    public boolean isDisplayButonDelete() {
        return displayButonDelete;
    }

    public void setDisplayButonDelete(boolean displayButonDelete) {
        this.displayButonDelete = displayButonDelete;
    }

    public IntervalleAnciennete getDurePreavis() {
        return durePreavis;
    }

    public void setDurePreavis(IntervalleAnciennete durePreavis) {
        this.durePreavis = durePreavis;
    }

    public String getChaineSelectCategorie() {
        return chaineSelectCategorie;
    }

    public void setChaineSelectCategorie(String chaineSelectCategorie) {
        this.chaineSelectCategorie = chaineSelectCategorie;
    }

    public int getIndexIntervalle() {
        return indexIntervalle;
    }

    public void setIndexIntervalle(int indexIntervalle) {
        this.indexIntervalle = indexIntervalle;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public List<YvsGrhIntervalleAnciennete> getListeIntervalleAnc() {
        return listeIntervalleAnc;
    }

    public void setListeIntervalleAnc(List<YvsGrhIntervalleAnciennete> listeIntervalleAnc) {
        this.listeIntervalleAnc = listeIntervalleAnc;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Categories other = (Categories) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public boolean controleFiche(Categories bean) {
        //on s'assure qu'il y a pas déja d'ntité portant le même titre en bd
        if (getId() <= 0) {
            champ = new String[]{"categorie", "societe"};
            val = new Object[]{bean.getCategorie(), currentUser.getAgence().getSociete()};
            YvsGrhCategorieProfessionelle c = (YvsGrhCategorieProfessionelle) dao.loadObjectByNameQueries("YvsCategorieProfessionelle.findByCategorieSociete", champ, val);
            return c == null;
        } else {
            return true;
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Categories recopieView() {
        return this;
    }

    @Override
    public void populateView(Categories bean) {
        cloneObject(bean, this);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
//        setDisplayButonDelete(!listSelection.isEmpty());
    }

    public void selectLine() {
//        setDisplayButonDelete(!listSelection.isEmpty());
//        update("categorie-gridButon");
    }

    @Override
    public void loadAll() {
        listValue.clear();
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listValue = dao.loadNameQueries("YvsCategorieProfessionelle.findAll", champ, val);
//        for (YvsGrhCategorieProfessionelle d : l) {
//            Categories dd = buildEntityByBean(d);
//            //construit la chaine des préavis rattaché à la catégorie
//            chainePreavis = "";
//            for (YvsGrhCategoriePreavis cp : d.getListDureePreavis()) {
//                chainePreavis += "[" + cp.getAnciennete().getAncienneteMin() + " - " + cp.getAnciennete().getAncienneteMax() + " ans] "
//                        + " (" + cp.getPreavis() + " " + cp.getUnitePreavis() + ") ; ";
//                dd.getListeDureePreavis().add(new IntervalleAnciennete(cp.getAnciennete().getId()));
//            }
//            dd.setChainePreavis(chainePreavis);
//            listValue.add(dd);
//        }
        if (!listValue.isEmpty()) {
            setDegre(listValue.get(listValue.size() - 1).getDegre() + 1);
        }

        //charge la liste des intervalles d'ancienneté
        listeIntervalleAnc = dao.loadNameQueries("YvsGrhIntervalleAnciennete.findAll", champ, val);

    }

    @Override
    public boolean saveNew() {
        if (controleFiche(this)) {
            //persiste
            YvsGrhCategorieProfessionelle cat = buildEntityByBean(this);
            cat.setDateUpdate(new Date());
            cat.setNew_(true);
            if (id <= 0) {
                cat.setId(null);
                cat.setDateSave(new Date());
                cat = (YvsGrhCategorieProfessionelle) dao.save1(cat);
                listValue.add(cat);
                degre++;
            } else {
                dao.update(cat);
                listValue.set(listValue.indexOf(cat), cat);
                degre++;
            }
            actionOpenOrResetAfter(this);
            update("tabCategorie");
            update("form_edit_catP");
            return true;
        } else {
            getErrorMessage("Création impossible.... " + this.categorie + " existe deja");
            return false;
        }
    }

    public void choixCategorie(SelectEvent ev) {
        if (ev != null) {
            YvsGrhCategorieProfessionelle c = (YvsGrhCategorieProfessionelle) ev.getObject();
            this.categorie = c.getCategorie();
            this.degre = c.getDegre();
            this.actif = c.getActif();
            this.id = c.getId();
            listeDureePreavis = dao.loadNameQueries("YvsGrhCategoriePreavis.findByCategorie", new String[]{"categorie"}, new Object[]{c});
            boolean trouve;
            int idTemp = -1000;
            YvsGrhCategoriePreavis catP;
            List<YvsGrhCategoriePreavis> list = new ArrayList<>();
            for (YvsGrhIntervalleAnciennete i : listeIntervalleAnc) {
                trouve = false;
                for (YvsGrhCategoriePreavis cp : listeDureePreavis) {
                    if (cp.getAnciennete().equals(i)) {
                        trouve = true;
                        break;
                    }
                }
                if (!trouve) {
                    catP = new YvsGrhCategoriePreavis(idTemp++);
                    catP.setAnciennete(i);
                    catP.setCategorie(c);
                    catP.setPreavis(0);
                    list.add(catP);
                }
            }
            listeDureePreavis.addAll(list);
        }
    }

    @Override
    public void deleteBean() {
//        try {
//            for (Categories c : listSelection) {
//                YvsGrhCategorieProfessionelle ca=new YvsGrhCategorieProfessionelle(c.getId());
//                ca.setAuthor(currentUser);
//                dao.delete(ca);
//                listValue.remove(ca);
//            }
//            displayButonDelete = !displayButonDelete;
//            update("categorie-gridButon");
//            update("tabCategorie");
//        } catch (Exception ex) {
//            getMessage("Impossible de terminer cette action! La catégorie est certainement utilisé par d'autres ressource", FacesMessage.SEVERITY_ERROR);
//        }
    }

    public void deleteCategorie(YvsGrhCategorieProfessionelle c) {
        try {
            YvsGrhCategorieProfessionelle cc = new YvsGrhCategorieProfessionelle(c.getId());
            cc.setAuthor(currentUser);
            cc.setDateUpdate(new Date());
            dao.delete(cc);
            listValue.remove(c);
        } catch (Exception ex) {
            getFatalMessage("Impossible de supprimer cet élément !");
            Logger.getLogger(Categories.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private YvsGrhCategorieProfessionelle buildEntityByBean(Categories c) {
        YvsGrhCategorieProfessionelle cat = new YvsGrhCategorieProfessionelle();
        cat.setCategorie(c.getCategorie());
        cat.setId((int) c.getId());
        cat.setActif(c.isActif());
        cat.setSupp(false);
        cat.setDegre(c.getDegre());
        cat.setSociete(currentAgence.getSociete());
        cat.setAuthor(currentUser);
        cat.setDateSave(dateSave);
        cat.setDateUpdate(new Date());
        return cat;
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * GERER LES INTERVALLE ET PREAVIS **
     */
    private boolean updateIntervallePreavis;

    public void saveUpdatePreavis(CellEditEvent ev) {
        int idx = ev.getRowIndex();
        Integer p = (Integer) ev.getNewValue();
        if (idx >= 0 && p > 0) {
            YvsGrhCategoriePreavis catp = listeDureePreavis.get(idx);
            if (catp != null) {
                catp.setDateUpdate(new Date());
                if (catp.getId() > 0) {
                    dao.update(catp);
                } else {
                    catp.setDateSave(new Date());
                    catp.setUnitePreavis("Jour");
                    catp.setId(null);
                    catp = (YvsGrhCategoriePreavis) dao.save1(catp);
                }
            }
        }
    }

    public void createIntervalleAnciennete() {
        if (durePreavis.getAncienneteMax() > 0 && durePreavis.getAncienneteMin() >= 0) {
            YvsGrhIntervalleAnciennete an = buildEntityIntervalle(durePreavis);
            an.setSociete(currentAgence.getSociete());
            if (!updateIntervallePreavis) {
                an.setAuthor(currentUser);
                an.setId(null);
                an = (YvsGrhIntervalleAnciennete) dao.save1(an);
                durePreavis.setId(an.getId());
                listeIntervalleAnc.add(an);
            } else {
                an.setId(durePreavis.getId());
                an.setAuthor(currentUser);
                dao.update(an);
                listeIntervalleAnc.set(listeIntervalleAnc.indexOf(an), an);
            }
            durePreavis = new IntervalleAnciennete();
            updateIntervallePreavis = false;
        } else {
            getErrorMessage("Erreur lors de l'enregistrement! votre formulaire est incorrecte");
        }
    }

    //modifie l'intervale d'anciènneté
    public void openIntervalleToUpdate(YvsGrhIntervalleAnciennete i) {
        if (i != null) {
            durePreavis.setAncienneteMax(i.getAncienneteMax());
            durePreavis.setAncienneteMin(i.getAncienneteMin());
            durePreavis.setId(i.getId());
            updateIntervallePreavis = true;
        }
    }

    public void deleteIntervallePreavis(IntervalleAnciennete i) {
        if (i != null) {
            try {
                dao.delete(new YvsGrhIntervalleAnciennete(i.getId()));
                listeDureePreavis.remove(i);
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cet élément !");
                Logger.getLogger(Categories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private List<Integer> giveCategorieSelectione() {
        List<Integer> l = new ArrayList<>();
        if (chaineSelectCategorie != null) {
            String numroLine[] = chaineSelectCategorie.split("-");
            try {
                int index;
                for (String numroLine1 : numroLine) {
                    index = Integer.parseInt(numroLine1);
                    l.add(index);
                }
                chaineSelectCategorie = "";
                succes();
            } catch (NumberFormatException ex) {
                chaineSelectCategorie = "";
                for (String numroLine1 : numroLine) {
                    if (!l.contains(numroLine1)) {
                        chaineSelectCategorie += numroLine1 + "-";
                    }
                }
                getErrorMessage("Impossible de terminer cette opération !");
            }
        }
        return l;
    }

    private YvsGrhIntervalleAnciennete buildEntityIntervalle(IntervalleAnciennete i) {
        YvsGrhIntervalleAnciennete r = new YvsGrhIntervalleAnciennete();
        r.setAncienneteMax(i.getAncienneteMax());
        r.setAncienneteMin(i.getAncienneteMin());
        return r;
    }

    @Override
    public void resetFiche() {
        categorie = "";
        degre = 0;
        update("catC");
        update("deg-catP");
    }
}

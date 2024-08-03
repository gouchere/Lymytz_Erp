/*
 * Manage la vue des mouvement de stock
 */
package yvs.stocks;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.param.YvsCrenauxHoraire;
import yvs.entity.produits.YvsBaseArticles;
import yvs.parametrage.entrepot.Depots;
import yvs.parametrage.th.CrenauxHoraire;
import yvs.base.produits.Articles;
import yvs.util.Constantes;
import yvs.util.ManagedUtil;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "MMstock")
@SessionScoped
public class MouvementStocks1 extends ManagedUtil implements Serializable {

    private String refArt1, refArt2, depot, depot2, creno;
    private YvsBaseArticles art1, art2;
    private YvsBaseDepots depots, depots2;
    private YvsCrenauxHoraire crenos;
    private boolean tout;
    private Date date = new Date();
    private Date date1 = new Date();
    private List<Stocks> listStock;
    private List<Depots> listDepot;
    private List<Depots> listFilterDepot;
    private List<Articles> listArt;
    private List<Articles> listFilterArt;
    private List<CrenauxHoraire> listCrenaux;
    private List<CrenauxHoraire> listCrenauxF;
    private List<String> ltypeArt;
    @ManagedProperty(value = "#{depots}")
    private Depots dep;

    public MouvementStocks1() {
        listStock = new ArrayList<>();
        listDepot = new ArrayList<>();
        listCrenaux = new ArrayList<>();
        listArt = new ArrayList<>();
        ltypeArt = new ArrayList<>();
        ltypeArt.add(Constantes.CAT_MP);
        ltypeArt.add(Constantes.CAT_MARCHANDISE);
        ltypeArt.add(Constantes.CAT_SERVICE);
        ltypeArt.add(Constantes.CAT_PF);
        ltypeArt.add(Constantes.CAT_PSF);
    }

    public Depots getDep() {
        return dep;
    }

    public void setDep(Depots dep) {
        this.dep = dep;
    }

    public String getCreno() {
        return creno;
    }

    public void setCreno(String creno) {
        this.creno = creno;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getRefArt1() {
        return refArt1;
    }

    public void setRefArt1(String refArt1) {
        this.refArt1 = refArt1;
    }

    public String getRefArt2() {
        return refArt2;
    }

    public void setRefArt2(String refArt2) {
        this.refArt2 = refArt2;
    }

    public boolean isTout() {
        return tout;
    }

    public void setTout(boolean tout) {
        this.tout = tout;
    }

    public List<Stocks> getListStock() {
        return listStock;
    }

    public void setListStock(List<Stocks> listStock) {
        this.listStock = listStock;
    }

    public List<Articles> getListArt() {
        return listArt;
    }

    public void setListArt(List<Articles> listArt) {
        this.listArt = listArt;
    }

    public List<CrenauxHoraire> getListCrenaux() {
        return listCrenaux;
    }

    public void setListCrenaux(List<CrenauxHoraire> listCrenaux) {
        this.listCrenaux = listCrenaux;
    }

    public List<CrenauxHoraire> getListCrenauxF() {
        return listCrenauxF;
    }

    public void setListCrenauxF(List<CrenauxHoraire> listCrenauxF) {
        this.listCrenauxF = listCrenauxF;
    }

    public List<Depots> getListDepot() {
        return listDepot;
    }

    public void setListDepot(List<Depots> listDepot) {
        this.listDepot = listDepot;
    }

    public List<Articles> getListFilterArt() {
        return listFilterArt;
    }

    public void setListFilterArt(List<Articles> listFilterArt) {
        this.listFilterArt = listFilterArt;
    }

    public List<Depots> getListFilterDepot() {
        return listFilterDepot;
    }

    public void setListFilterDepot(List<Depots> listFilterDepot) {
        this.listFilterDepot = listFilterDepot;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public String getDepot2() {
        return depot2;
    }

    public void setDepot2(String depot2) {
        this.depot2 = depot2;
    }
    int rang;

    public void openListArt(int ran) {
        rang = ran;
        listArt.clear();
        if (depots != null) {
            listArt = chargement.loadListArtNegoceInDepot(depots.getId(), ltypeArt);
            openDialog("listArt");
        }
    }

    public void saisieProduit1() {
        if (depots != null) {
            art1 = chargement.loadOneArtNegoceInDepot((depots != null) ? depots.getId() : 0, refArt1, ltypeArt);
        } else {
            art1 = null;
            refArt1 = null;
        }
    }

    public void saisieProduit2() {
        if (depots != null) {
            art2 = chargement.loadOneArtNegoceInDepot((depots != null) ? depots.getId() : 0, refArt2, ltypeArt);
        } else {
            art2 = null;
            refArt2 = null;
        }
    }

    public void chooseArticle(SelectEvent ev) {
        Articles art = (Articles) ev.getObject();
        if (rang == 1) {
            if (art1 == null) {
                art1 = new YvsBaseArticles();
            }
            if (art != null) {
                art1.setId(art.getId());
                art1.setRefArt(art.getRefArt());
                refArt1 = art.getRefArt();

            }
        } else if (rang == 2) {
            if (art2 == null) {
                art2 = new YvsBaseArticles();
            }
            if (art != null) {
                art2.setId(art.getId());
                art2.setRefArt(art.getRefArt());
                refArt2 = art.getRefArt();
            }
        }
        rang = 0;
        closeDialog("listArt");
    }

    private Articles buildArt(YvsBaseArticles a) {
        Articles ar = new Articles();
        if (a != null) {
            ar.setId(a.getId());
            ar.setPua(a.getPua());
            ar.setPuv(a.getPuv());
            ar.setPuvMin(a.getPrixMin());
//            ar.setSuiviEnStock(a.isSuiviEnStock());
//            ar.setMethodeVal(a.getMathodeVal());
            ar.setRefArt(a.getRefArt());
        }
        return ar;
    }

    private YvsBaseArticles buildArt(Articles a) {
        YvsBaseArticles ar = new YvsBaseArticles(a.getId());
        ar.setPua(a.getPua());
        ar.setPuv(a.getPuv());
        ar.setPrixMin(a.getPuvMin());
        ar.setRefArt(a.getRefArt());
        return ar;
    }

    public void openListDep() {
//        listDepot = dep.loadLisDepot(currentAgence.getId());
        openDialog("listDep");
        update(":ldep-form:ldep-S");
    }

    public void choixDepot() {
        depots = chargement.loadDepot(depot, currentAgence.getId());
        if (depots != null) {
//            listCrenaux = dep.buildListCrenaux(depots);
        } else {
            depots = null;
            depot = null;
        }
    }

    public void choixDepot2() {
        depots2 = chargement.loadDepot(depot, currentAgence.getId());
        if (depots2 != null) {
//            listCrenaux = dep.buildListCrenaux(depots);
        } else {
            depots2 = null;
            depot2 = null;
        }
    }

    public void chooseDepot(SelectEvent ev) {
        Depots choix = (Depots) ev.getObject();
        if (rang == 1) {
            if (depots == null) {
                depots = new YvsBaseDepots();
            }
            depots.setId(choix.getId());
            depots.setCode(choix.getCode());
            depot = choix.getCode();
        } else if (rang == 2) {
            if (depots2 == null) {
                depots2 = new YvsBaseDepots();
            }
            depots2.setId(choix.getId());
            depots2.setCode(choix.getCode());
            depot2 = choix.getCode();
        }
        rang=0;
        closeDialog("listDep");

    }

    public void openListCreno() {
        openDialog("listCrenaux");
    }

    public void loadStock() {
//        List<Object[]> l;
//        if (tout) {
//            l = daoStock.loadStock1(depots.getId(), date);
//        } else {
//            l = daoStock.loadStock(depots.getId(), art1.getRefArt(), art2.getRefArt(), date);
//        }
//        listStock.clear();
//        numero = 0;
//        for (Object[] ins : l) {
//                Stocks st = new Stocks();
//            st.setNumero(numero = numero + 1);
//            Articles art = new Articles();
//            art.setRefArt((String) ins[0]);
//            art.setDesignation((String) ins[1]);
//            st.setProduit(art);
//            st.setStockMin((double) ins[2]);
//            st.setStockMax((double) ins[3]);
//            st.setStock((double) ins[4]);
//            listStock.add(st);
//        }
    }
}

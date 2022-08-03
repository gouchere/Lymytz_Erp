///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.tiers.client;
//
//import java.util.ArrayList;
//import java.util.List;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import yvs.entity.base.YvsBaseCategorieComptable;
////import yvs.entity.param.YvsCatcompta;
//import yvs.entity.param.YvsModelDeReglement;
//import yvs.entity.produits.YvsPlanDeRecompense;
//import yvs.entity.produits.group.YvsCatTarif;
//import yvs.parametrage.catCompta.CategorieComptable;
//import yvs.parametrage.catTarif.CategorieTarifaire;
//import yvs.parametrage.mdr.ModelDeReglement;
//import yvs.produits.Recompenses;
//import yvs.tiers.Tiers;
//
///**
// *
// * @author GOUCHERE YVES
// */
//@ManagedBean(name = "client")
//@SessionScoped
//public class Clients extends Tiers {
//
//    private ModelDeReglement mdr;
//    private CategorieTarifaire catT;
//    private Recompenses ristourne;
//    private CategorieComptable catC;
//
//    public Clients() {
//    }
//
//    public CategorieComptable getCatC() {
//        return catC;
//    }
//
//    public void setCatC(CategorieComptable catC) {
//        this.catC = catC;
//    }
//
//    public CategorieTarifaire getCatT() {
//        return catT;
//    }
//
//    public void setCatT(CategorieTarifaire catT) {
//        this.catT = catT;
//    }
//
//    public ModelDeReglement getMdr() {
//        return mdr;
//    }
//
//    public void setMdr(ModelDeReglement mdr) {
//        this.mdr = mdr;
//    }
//
//    public Recompenses getRistourne() {
//        return ristourne;
//    }
//
//    public void setRistourne(Recompenses ristourne) {
//        this.ristourne = ristourne;
//    }
//
//    public List<Clients> loadListClient(long agence) {
//        String[] ch = {"agence"};
//        Object[] val = {agence};
//        List<Object[]> l = dao.loadListTableByNameQueries("YvsTiers.findByFilterClt", ch, val);
//        Clients clt = new Clients();
//        List<Clients> lf = new ArrayList<>();
//        for (Object[] tab : l) {
//            clt.setId((long) tab[0]);
//            clt.setNom((String) tab[1]);
//            clt.setCodeClient((String) tab[2]);
//            clt.setCatC(buildCatC((YvsBaseCategorieComptable) tab[3]));
//            clt.setRistourne(buildRistourne((YvsPlanDeRecompense) tab[4]));
//            clt.setMdr(buildMdr((YvsModelDeReglement) tab[5]));
//            clt.setCatT(buildCatT((YvsCatTarif) tab[6]));
//            lf.add(clt);
//            clt = new Clients();
//        }
//        return lf;
//    }
//
//    private ModelDeReglement buildMdr(YvsModelDeReglement model) {
//        if (model != null) {
//            if (model.getActif()) {
//                ModelDeReglement md = new ModelDeReglement(model.getDesignation(), model.getDesignation());
//                md.setId(model.getId());
//                return md;
//            }
//        }
//        return null;
//    }
//
//    private Recompenses buildRistourne(YvsPlanDeRecompense rist) {
//        if (rist != null) {
//            if (rist.getActif()) {
//                Recompenses re = new Recompenses(rist.getId(), rist.getDesignation(), rist.getNature());
//                re.setDuree(rist.getDuree());
//                re.setDebut(rist.getDebut());
//                re.setFin(rist.getFin());
//                return re;
//            }
//        }
//        return null;
//    }
//
//    private CategorieTarifaire buildCatT(YvsCatTarif cat) {
//        if (cat != null) {
//            if (cat.getActif()) {
//                CategorieTarifaire c = new CategorieTarifaire(cat.getId());
//                c.setCodeAppel(cat.getCodeAppel());
//                c.setDesignation(cat.getDesignation());
//                return c;
//            }
//        } else {
//            return null;
//        }
//        return null;
//    }
//
//    private CategorieComptable buildCatC(YvsBaseCategorieComptable cat) {
//        if (cat != null) {
//            if (cat.getActif()) {
//                CategorieComptable c = new CategorieComptable(cat.getCode(), cat.getNature());
//                c.setCodeCategorie(cat.getCode());
//                return c;
//            }
//        } else {
//            return null;
//        }
//        return null;
//    }
//
//    @Override
//    public boolean isClient() {
//        return true;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final Clients other = (Clients) obj;
//        if (this.getId() != other.getId()) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 53 * hash + (int) (this.getId() ^ (this.getId() >>> 32));
//        return hash;
//    }
//
//    @Override
//    public int compare(Tiers o1, Tiers o2) {
//        if (o1.getAgence().getCodeAgence().compareTo(o2.getAgence().getCodeAgence()) < 0) {
//            return -1;
//        } else if (o1.getAgence().getCodeAgence().compareTo(o2.getAgence().getCodeAgence()) > 0) {
//            return 1;
//        } else {
//            return 0;
//        }
//    }
//}

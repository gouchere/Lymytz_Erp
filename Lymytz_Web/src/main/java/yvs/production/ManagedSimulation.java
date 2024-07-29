/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.ManagedArticles;
import yvs.base.produits.UniteMesure;
import yvs.entity.production.base.YvsProdComposantNomenclature;
import yvs.entity.production.base.YvsProdNomenclature;
import yvs.entity.production.pilotage.YvsProdOrdreFabrication;
import yvs.entity.produits.YvsBaseArticles;
import yvs.production.technique.Nomenclature;
import yvs.util.Constantes;
import yvs.util.Managed;

/**
 *
 * @author hp Elite 8300
 */
@SessionScoped
@ManagedBean
public class ManagedSimulation extends Managed<OrdreFabrication, YvsProdOrdreFabrication> implements Serializable {

    private Articles article = new Articles();
    private Nomenclature nomenclature = new Nomenclature();
    private double quantite;
    private List<String> objects;
    private List<SimulNomenclature> nomenclatures;
    private List<Composants> composants;

    public ManagedSimulation() {
        nomenclatures = new ArrayList<>();
        composants = new ArrayList<>();
        objects = new ArrayList<>();
        objects.add("");
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public List<SimulNomenclature> getNomenclatures() {
        return nomenclatures;
    }

    public void setNomenclatures(List<SimulNomenclature> nomenclatures) {
        this.nomenclatures = nomenclatures;
    }

    public List<Composants> getComposants() {
        return composants;
    }

    public void setComposants(List<Composants> composants) {
        this.composants = composants;
    }

    public Nomenclature getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(Nomenclature nomenclature) {
        this.nomenclature = nomenclature;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public List<String> getObjects() {
        return objects;
    }

    public void setObjects(List<String> objects) {
        this.objects = objects;
    }

    public void searchArticle(String code) {
        if (code != null ? code.trim().length() > 0 : false) {
            ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
            if (m != null) {
                article = m.searchArticleActif("P", code, false);
                if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                    if (m.getArticlesResult().size() > 1) {
                        initOpenArticle();
                    } else {
                        chooseArticle(article);
                    }
                    article.setError(false);
                }
            }
        }
    }

    public void initOpenArticle() {
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            m.setManagedBean(this);
            openDialog("dlgArticles");
            update("tab_article");
        }
    }

    public void chooseArticle(Articles y) {
        article = y;
        article.setNomenclatures(dao.loadNameQueries("YvsProdNomenclature.findByArticleFor", new String[]{"article", "for"}, new Object[]{new YvsBaseArticles(y.getId()), false}));
        update("chp_art_sim");
        update("chp_nomenclature_");
        closeDialog("dlgArticles");
    }

    public void chooseNomenclature(ValueChangeEvent ev) {
        if (ev != null) {
            Long id = (Long) ev.getNewValue();
            if (id != null ? id > 0 : false) {
                int idx = article.getNomenclatures().indexOf(new YvsProdNomenclature(id));
                if (idx >= 0) {
                    nomenclature = UtilProd.buildBeanNomenclature(article.getNomenclatures().get(idx));
                    nomenclature.setComposants(dao.loadNameQueries("YvsProdComposantNomenclature.findByNomenclature", new String[]{"nomenclature"}, new Object[]{article.getNomenclatures().get(idx)}));
                }
            }
        }
    }

    public void addNomenclatureOnListBesoin() {
        if (nomenclature.getId() > 0) {
            if (quantite > 0) {
                SimulNomenclature nm = new SimulNomenclature(UtilProd.buildBeanNomenclature(nomenclature, currentUser), quantite);
                int idx = nomenclatures.indexOf(nm);
                if (idx >= 0) {
                    nomenclatures.set(idx, nm);
                    //retranche oldQte
                    removeNomenclatureOnListBesoin(nm);
                } else {
                    nomenclatures.add(0, nm);
                }
                double qteMp;
                Articles a;
                Composants cp;
                for (YvsProdComposantNomenclature cn : nomenclature.getComposants()) {
//                    qteMp = quantite * cn.getQuantite() / nm.getQuantite();
                    qteMp=giveQuantite(cn, nm.getNomenclature(),nm.getQuantite());
                    //find composant
                    a = UtilProd.buildBeanArticles(cn.getArticle());
                    cp = new Composants(a, qteMp,UtilProd.buildBeanUniteMesure(cn.getUnite().getUnite()), cn.getType().equals(Constantes.PROD_OP_TYPE_COMPOSANT_SOUS_PRODUIT));
                    int i = composants.indexOf(cp);
                    if (i >= 0) {
                        //exite déjà
                        composants.get(i).setQuantite(composants.get(i).getQuantite() + qteMp);
                    } else {
                        composants.add(cp);
                    }
                }
            } else {
                getErrorMessage("Veuillez entrer la quantité du produit !");
            }
        } else {
            getErrorMessage("Aucune nomenclature choisie !");
        }
    }

    public void deleteSimNomenclature(SimulNomenclature nm) {
        removeNomenclatureOnListBesoin(nm);
        nomenclatures.remove(nm);
        update("table_sim_nomenclature");
        update("table_com_simule");
    }

    public void removeNomenclatureOnListBesoin(SimulNomenclature nm) {
        if (nm != null) {
            nm.getNomenclature().setComposants(dao.loadNameQueries("YvsProdComposantNomenclature.findByNomenclature", new String[]{"nomenclature"}, new Object[]{new YvsProdNomenclature(nm.getNomenclature().getId())}));
            double qteMp;
            Articles a;
            Composants cp;
            for (YvsProdComposantNomenclature cn : nm.getNomenclature().getComposants()) {
//                qteMp = nm.getQuantite() * cn.getQuantite() / nm.getNomenclature().getQuantite();
                qteMp=giveQuantite(cn, nm.getNomenclature(),nm.getQuantite());
                //find composant
                a = UtilProd.buildBeanArticles(cn.getArticle());
                cp = new Composants(a, qteMp, UtilProd.buildBeanUniteMesure(cn.getUnite().getUnite()), cn.getType().equals(Constantes.PROD_OP_TYPE_COMPOSANT_SOUS_PRODUIT));
                int i = composants.indexOf(cp);
                if (i >= 0) {
                    composants.get(i).setQuantite(composants.get(i).getQuantite() - qteMp);
                }
            }
        } else {
            getErrorMessage("Veuillez entrer la quantité du produit !");
        }
    }
    private double giveQuantite(YvsProdComposantNomenclature com_, YvsProdNomenclature nom_, double quantiteOF) {
        if (com_.getComposantLie() != null) {
            // récupère le composant             
            YvsProdComposantNomenclature c = (YvsProdComposantNomenclature)dao.loadOneByNameQueries("YvsProdComposantNomenclature.findById", new String[]{"id"}, new Object[]{com_.getComposantLie().getId()});
            if (c != null) {
                double d=giveQuantite(c, nom_, quantiteOF);
                return com_.getQuantite() * d/ 100;
            }
        } else {
            return (com_.getQuantite() * quantiteOF / nom_.getQuantite());
        }
        return 0;
    }

    @Override
    public boolean controleFiche(OrdreFabrication bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public class Composants {

        private Articles art = new Articles();
        private Double quantite = 0d;
        private UniteMesure unite = new UniteMesure();
        private boolean sousProduit;

        public Composants(Articles art, Double qte, UniteMesure unite, boolean sp) {
            this.art = art;
            this.quantite = qte;
            this.sousProduit = sp;
            this.unite = unite;
        }

        public Articles getArt() {
            return art;
        }

        public void setArt(Articles art) {
            this.art = art;
        }

        public Double getQuantite() {
            return quantite;
        }

        public void setQuantite(Double quantite) {
            this.quantite = quantite;
        }

        public boolean isSousProduit() {
            return sousProduit;
        }

        public void setSousProduit(boolean sousProduit) {
            this.sousProduit = sousProduit;
        }

        public UniteMesure getUnite() {
            return unite;
        }

        public void setUnite(UniteMesure unite) {
            this.unite = unite;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.art);
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
            final Composants other = (Composants) obj;
            if (!Objects.equals(this.art, other.art)) {
                return false;
            }
            return true;
        }

    }

    public class SimulNomenclature {

        private YvsProdNomenclature nomenclature;
        private double quantite;

        public SimulNomenclature() {
        }

        public SimulNomenclature(YvsProdNomenclature nomenclature, double quantite) {
            this.nomenclature = nomenclature;
            this.quantite = quantite;
        }

        public YvsProdNomenclature getNomenclature() {
            return nomenclature;
        }

        public void setNomenclature(YvsProdNomenclature nomenclature) {
            this.nomenclature = nomenclature;
        }

        public double getQuantite() {
            return quantite;
        }

        public void setQuantite(double quantite) {
            this.quantite = quantite;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + Objects.hashCode(this.nomenclature);
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
            final SimulNomenclature other = (SimulNomenclature) obj;
            if (!Objects.equals(this.nomenclature, other.nomenclature)) {
                return false;
            }
            return true;
        }

    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import yvs.production.UtilProd;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.produits.YvsBaseArticleCodeBarre;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.util.*;

/**
 *
 * @author GOUCHERE YVES
 */
@SessionScoped
@ManagedBean
public class ManagedCondArticles extends Managed<Conditionnement, YvsBaseConditionnement> implements Serializable {

    private Conditionnement conditionnement = new Conditionnement();
    private List<YvsBaseConditionnement> conditionnements;
    private YvsBaseConditionnement selectedCond;
    private CodeBarre codeBarre = new CodeBarre();

    private long groupeSearch, familleSearch;
    private String categorieSearch, searchArticle, codeBarreSearch;
    private Boolean actifSearch, displayId = false;

    private boolean paramDate;
    private Date dateDebut = new Date(), dateFin = new Date();

    public ManagedCondArticles() {
        conditionnements = new ArrayList<>();
    }

    public String getCodeBarreSearch() {
        return codeBarreSearch;
    }

    public void setCodeBarreSearch(String codeBarreSearch) {
        this.codeBarreSearch = codeBarreSearch;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public List<YvsBaseConditionnement> getConditionnements() {
        return conditionnements;
    }

    public void setConditionnements(List<YvsBaseConditionnement> conditionnements) {
        this.conditionnements = conditionnements;
    }

    public YvsBaseConditionnement getSelectedCond() {
        return selectedCond;
    }

    public void setSelectedCond(YvsBaseConditionnement selectedCond) {
        this.selectedCond = selectedCond;
    }

    public CodeBarre getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(CodeBarre codeBarre) {
        this.codeBarre = codeBarre;
    }

    public long getGroupeSearch() {
        return groupeSearch;
    }

    public void setGroupeSearch(long groupeSearch) {
        this.groupeSearch = groupeSearch;
    }

    public long getFamilleSearch() {
        return familleSearch;
    }

    public void setFamilleSearch(long familleSearch) {
        this.familleSearch = familleSearch;
    }

    public String getCategorieSearch() {
        return categorieSearch;
    }

    public void setCategorieSearch(String categorieSearch) {
        this.categorieSearch = categorieSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public boolean isParamDate() {
        return paramDate;
    }

    public void setParamDate(boolean paramDate) {
        this.paramDate = paramDate;
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

    public String getSearchArticle() {
        return searchArticle;
    }

    public void setSearchArticle(String searchArticle) {
        this.searchArticle = searchArticle;
    }

    @Override
    public void loadAll() {
        loadAllArticle(true, true);
    }

    @Override
    public boolean controleFiche(Conditionnement bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            onSelectObject((YvsBaseConditionnement) ev.getObject());
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSelectObject(YvsBaseConditionnement y) {
        conditionnement = UtilProd.buildBeanConditionnement(y);
        //charge les codes barre
        conditionnement.setCodesBarres(dao.loadNameQueries("YvsBaseArticleCodeBarre.findByConditionnement", new String[]{"conditionnement"}, new Object[]{new YvsBaseConditionnement(conditionnement.getId())}));
        update("fiche_articles_cond");
    }

    public void loadAllArticle(boolean avancer, boolean init) {
        ParametreRequete p = new ParametreRequete("y.article.famille.societe", "societe", currentAgence.getSociete());
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        conditionnements = paginator.executeDynamicQuery("YvsBaseConditionnement", "y.article.refArt", avancer, init, (int) imax, dao);
        if (conditionnements != null ? conditionnements.size() == 1 : false) {
            onSelectObject(conditionnements.get(0));
            execute("collapseForm('article')");
        } else {
            execute("collapseList('article')");
        }
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAllArticle(true, true);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllArticle(true, true);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbResult())) {
            setOffset(0);
        }
        List<YvsBaseConditionnement> re = paginator.parcoursDynamicData("YvsBaseConditionnement", "y", "y.article.refArt", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void saveNewCode() {
        YvsBaseArticleCodeBarre code = saveNewCode(conditionnement.getArticle().getId(), conditionnement.getId(), conditionnement.getCodeBarre());
        if (code != null ? code.getId() > 0 : false) {
            int index = conditionnement.getCodesBarres().indexOf(code);
            if (index > -1) {
                conditionnement.getCodesBarres().set(index, code);
            } else {
                conditionnement.getCodesBarres().add(0, code);
            }
        }
    }

    public YvsBaseArticleCodeBarre saveNewCode(Long article, Long conditionnement, String codeBarre) {
        YvsBaseArticleCodeBarre code = null;
        if (conditionnement > 0) {
            if (codeBarre != null ? !codeBarre.trim().isEmpty() : false) {
                code = (YvsBaseArticleCodeBarre) dao.loadObjectByNameQueries("YvsBaseArticleCodeBarre.findByCode", new String[]{"codeBarre", "societe"}, new Object[]{codeBarre.trim(), currentAgence.getSociete()});
                if (code != null ? code.getId() > 0 : false) {
                    getErrorMessage("Ce code barre à déjà été rattaché à un article");
                    return code;
                }
                code = new YvsBaseArticleCodeBarre();
                code.setAuthor(currentUser);
                code.setCodeBarre(codeBarre);
                code.setConditionnement(new YvsBaseConditionnement(conditionnement));
                code.setDateSave(new Date());
                code.setDateUpdate(new Date());
                code.setId(null);
                code = (YvsBaseArticleCodeBarre) dao.save1(code);
            } else {
                getErrorMessage("Veuillez entrer le code barre !");
            }
        } else {
            getErrorMessage("Aucun article n'a été selectionné !");
        }
        return code;
    }

    public void deleteCodeBarre(YvsBaseArticleCodeBarre code) {
        try {
            dao.delete(code);
            conditionnement.getCodesBarres().remove(code);
        } catch (Exception ex) {
            getException("Error Delete code barre ", ex);
        }
    }

    public void findArticle(String str) {
        ParametreRequete p;
        if ((str == null) ? false : !str.trim().equals("")) {
            p = new ParametreRequete(null, "refArt", str + "%", "LIKE ", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "refArt", str.toUpperCase() + "%", "LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "designation", str.toUpperCase() + "%", "LIKE ", "OR"));
        } else {
            p = new ParametreRequete("refArt", "refArt", null);
        }
        paginator.addParam(p);
        loadAllArticle(false, true);
        if (!conditionnements.isEmpty()) {
            if (conditionnements.size() > 0) {
                onSelectObject(conditionnements.get(0));
            }
        }
    }

    public void chooseDateSearch(ValueChangeEvent ev) {
        paramDate = (Boolean) ev.getNewValue();
        addParaDate(paramDate);
    }

    public void addParamDate1(SelectEvent ev) {
        addParaDate(paramDate);
    }

    public void addParamDate2() {
        addParaDate(paramDate);
    }

    private void addParaDate(boolean b) {
        ParametreRequete p = new ParametreRequete("y.dateSave", "dateSave", null, " BETWEEN ", "AND");
        if (b) {
            if (dateDebut != null && dateFin != null) {
                if (dateDebut.before(dateFin) || dateDebut.equals(dateFin)) {
                    p.setObjet(dateDebut);
                    p.setOtherObjet(dateFin);
                }
            }
        }
        paginator.addParam(p);
        loadAllArticle(true, true);
    }

    public void addParamActif() {
        ParametreRequete p = new ParametreRequete("y.article.actif", "actif", null);
        if (actifSearch != null) {
            p.setObjet(actifSearch);
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAllArticle(false, true);
    }

    public void findArticle_() {
        ParametreRequete p;
        if ((searchArticle == null) ? false : !searchArticle.trim().equals("")) {
            p = new ParametreRequete(null, "refArt", searchArticle + "%", "LIKE ", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "refArt", searchArticle.toUpperCase() + "%", "LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "designation", searchArticle.toUpperCase() + "%", "LIKE ", "OR"));
        } else {
            p = new ParametreRequete("refArt", "refArt", null);
        }
        paginator.addParam(p);
        loadAllArticle(false, true);
    }

    public void addParamGroupe() {
        ParametreRequete p = new ParametreRequete("y.article.groupe", "groupe", null);
        if (groupeSearch > 0) {
            p.setObjet(new YvsBaseGroupesArticle(groupeSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAllArticle(false, true);
    }

    public void addParamFamille() {
        ParametreRequete p = new ParametreRequete("y.article.famille", "famille", null);
        if (familleSearch > 0) {
            p.setObjet(new YvsBaseFamilleArticle(familleSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAllArticle(false, true);
    }

    public void addParamCategorie() {
        ParametreRequete p = new ParametreRequete("y.article.categorie", "categorie", null);
        if (categorieSearch != null ? categorieSearch.trim().length() > 0 : false) {
            p.setObjet(categorieSearch);
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAllArticle(false, true);
    }

    public void addParamCodeBarre() {
        ParametreRequete p = new ParametreRequete("y.id", "ids", null, "IN", "AND");
        if (codeBarreSearch != null ? codeBarreSearch.trim().length() > 0 : false) {
            List<Long> ids = dao.loadNameQueries("YvsBaseArticleCodeBarre.findIdArticleByCodeBarre", new String[]{"codeBarre"}, new Object[]{codeBarreSearch.trim() + "%"});
            if (ids.isEmpty()) {
                ids.add(-1L);
            }
            p = new ParametreRequete("y.id", "ids", ids, "IN", "AND");
        }
        paginator.addParam(p);
        loadAllArticle(false, true);
    }

}

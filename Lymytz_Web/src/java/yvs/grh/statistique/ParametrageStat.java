/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.statistique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.paie.ElementSalaire;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ParametrageStat  implements Serializable {

    private List<ElementSalaire> elementsSalaire;
    private List<Etats> etats;

    private String chaineSelectElement;
    private Etats etat;

    public ParametrageStat() {
        etats = new ArrayList<>();
        elementsSalaire = new ArrayList<>();
    }

    public List<ElementSalaire> getElementsSalaire() {
        return elementsSalaire;
    }

    public void setElementsSalaire(List<ElementSalaire> elementsSalaire) {
        this.elementsSalaire = elementsSalaire;
    }

    public List<Etats> getEtats() {
        return etats;
    }

    public void setEtats(List<Etats> etats) {
        this.etats = etats;
    }

    public String getChaineSelectElement() {
        return chaineSelectElement;
    }

    public void setChaineSelectElement(String chaineSelectElement) {
        this.chaineSelectElement = chaineSelectElement;
    }

    public Etats getEtat() {
        return etat;
    }

    public void setEtat(Etats etat) {
        this.etat = etat;
    }
//
//    @Override
//    public boolean controleFiche(ParametrageStat bean) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void deleteBean() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void updateBean() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public ParametrageStat recopieView() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void populateView(ParametrageStat bean) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void loadOnView(SelectEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void unLoadOnView(UnselectEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void loadAll() {
//        etats.clear();
//        champ = new String[]{};
//        val = new Object[]{};
//        List<YvsGrhDocStatistiques> l = dao.loadNameQueries("YvsGrhDocStatistiques.findAll", champ, val);
//        for (YvsGrhDocStatistiques y : l) {
//            etats.add(new Etats(y.getId(), y.getCode(), y.getDesignationEtat()));
//        }
//        champ = new String[]{"societe"};
//        val = new Object[]{currentScte};
//        elementsSalaire = UtilGrh.buildElementSalaire(dao.loadNameQueries("YvsGrhElementSalaire.findAll", champ, val));
//    }
//
//    public void bindRegleWithEtat() {
//        List<ElementSalaire> selection = giveSelection();
//        if (etat != null) {
//            YvsGrhElementStatistique estat;
//            for (ElementSalaire e : selection) {
//                estat=new YvsGrhElementStatistique();
//                estat.setActif(true);
//                estat.setElementSalaire(new YvsElementSalaire(e.getId()));
//                estat.setEtat(new YvsGrhDocStatistiques(etat.getId()));
//                estat.setCode(chaineSelectElement);
//                dao.save(estat);
//            }
//            succes();
//        }else{
//            getErrorMessage("Selectionnez l'état !");
//        }
//    }
//
//    private List<ElementSalaire> giveSelection() {
//        List<ElementSalaire> re = new ArrayList<>();
//        if (chaineSelectElement != null) {
//            String numroLine[] = chaineSelectElement.split("-");
//            List<String> l = new ArrayList<>();
//            try {
//                int index;
//                for (String numroLine1 : numroLine) {
//                    index = Integer.parseInt(numroLine1);
//                    re.add(elementsSalaire.get(index));
//                }
//                chaineSelectElement = "";
//            } catch (NumberFormatException ex) {
//                chaineSelectElement = "";
//                for (String numroLine1 : numroLine) {
//                    if (!l.contains(numroLine1)) {
//                        chaineSelectElement += numroLine1 + "-";
//                    }
//                }
//                getErrorMessage("Impossible de terminer cette opération !");
//            }
//        }
//        return re;
//    }
}

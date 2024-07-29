/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.activite.YvsDemandeEmps;
import yvs.entity.grh.activite.YvsTypeDemande;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.grh.UtilGrh;
import yvs.util.Managed;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedDemande extends Managed<Demande, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{demande}")
    Demande demande;
    public List<Demande> listdemande, listfiltree, listselectionDT, listselectionDG;
    public List<TypeDemande> listvalue;
    public List<String> listType;
    private List<Employe> employes;
    private boolean disableB = true;
    private boolean updateDemande = false;
    private boolean deleteDemande = false;
    private boolean activVue = false;
    private boolean selection = false;   

    public ManagedDemande() {
        listType = new ArrayList<>();
//        listType.add("Explication");
        listdemande = new ArrayList<>();
        listvalue = new ArrayList<>();
        employes = new ArrayList<>();
        listselectionDT = new ArrayList<>();
        listselectionDG = new ArrayList<>();
    }

    public List<Demande> getListselectionDT() {
        return listselectionDT;
    }

    public void setListselectionDT(List<Demande> listselectionDT) {
        this.listselectionDT = listselectionDT;
    }

    public List<Demande> getListselectionDG() {
        return listselectionDG;
    }

    public void setListselectionDG(List<Demande> listselectionDG) {
        this.listselectionDG = listselectionDG;
    }

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }

    public boolean isUpdateDemande() {
        return updateDemande;
    }

    public void setUpdateDemande(boolean updateDemande) {
        this.updateDemande = updateDemande;
    }

    public boolean isActivVue() {
        return activVue;
    }

    public void setActivVue(boolean activVue) {
        this.activVue = activVue;
    }

    public YvsTypeDemande getCurrentType() {
        return currentType;
    }

    public void setCurrentType(YvsTypeDemande currentType) {
        this.currentType = currentType;
    }

    public List<TypeDemande> getListvalue() {
        return listvalue;
    }

    public void setListvalue(List<TypeDemande> listvalue) {
        this.listvalue = listvalue;
    }

    public boolean isDeleteDemande() {
        return deleteDemande;
    }

    public void setDeleteDemande(boolean deleteDemande) {
        this.deleteDemande = deleteDemande;
    }

    public YvsDemandeEmps getCurrentdemande() {
        return currentdemande;
    }

    public void setCurrentdemande(YvsDemandeEmps currentdemande) {
        this.currentdemande = currentdemande;
    }

    public boolean isDisableB() {
        return disableB;
    }

    public void setDisableB(boolean disableB) {
        this.disableB = disableB;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public List<Demande> getListdemande() {
        return listdemande;
    }

    public void setListdemande(List<Demande> listdemande) {
        this.listdemande = listdemande;
    }

    public List<Demande> getListfiltree() {
        return listfiltree;
    }

    public void setListfiltree(List<Demande> listfiltree) {
        this.listfiltree = listfiltree;
    }

    public List<String> getListType() {
        return listType;
    }

    public void setListType(List<String> listType) {
        this.listType = listType;
    }

    public List<Employe> getEmployes() {
        return employes;
    }

    public void setEmployes(List<Employe> employes) {
        this.employes = employes;
    }

    public int getOffsetEmps() {
        return offsetEmps;
    }

    public void setOffsetEmps(int offsetEmps) {
        this.offsetEmps = offsetEmps;
    }

    public boolean isDisPrevEmps() {
        return disPrevEmps;
    }

    public void setDisPrevEmps(boolean disPrevEmps) {
        this.disPrevEmps = disPrevEmps;
    }

    public boolean isDisNextEmps() {
        return disNextEmps;
    }

    public void setDisNextEmps(boolean disNextEmps) {
        this.disNextEmps = disNextEmps;
    }

    //ajout d'une demande
    public void addDemande() {
        demande.setDateDemande(new Date());
        currentdemande = buildDemande(demande);
        currentdemande.setId(null);
        currentdemande = (YvsDemandeEmps) dao.save1(currentdemande);
        demande.setId(currentdemande.getId());
        System.out.println("+++++current demande id: " + currentdemande.getId());
        succes();
    }

    //verification de l'existence du type de demande
    public void verification() {
        String type = demande.getType().getLibelle();
        if (!"".equals(type)) {
            for (String listType1 : listType) {
                if (type.equals(listType1)) {
                    return;
                }
            }
            openDialog("dlgconfirm");
        }
    }

    YvsTypeDemande currentType;

    //ajout d'un type de demande
    public void ajoutType() {
        currentType = buildType(demande);
        currentType.setId(null);
        currentType = (YvsTypeDemande) dao.save1(currentType);
    }

    //confirmation d'une suppression
    public void confirmation() {
        openDialog("dlgConfirmDelete");
    }

    public void findEmps() {
        offsetEmps = 0;
        if (demande.getRecepteur().getCodeEmploye() != null) {
            String[] champ = new String[]{"codeUsers", "agence"};
            Object[] val = new Object[]{"%" + demande.getRecepteur().getCodeEmploye() + "%", currentAgence};
            List<YvsGrhEmployes> l = dao.loadNameQueries("YvsGrhEmployes.findByCodeUsers", champ, val, offsetEmps, 100);
            System.err.println("------- " + l.size() + " ---- ");
            if (l.size() > 1) {
                //ouvre la liste des resultats dans la boite de dialogue listEmployés
                employes = UtilGrh.buildListEmployeBean(l);
                int n = employes.size();
                disPrevEmps = offsetEmps <= 0;
                if (n < 100) {
                    disNextEmps = true;
                } else if (n == 0) {
                    disNextEmps = true;
                    disPrevEmps = true;
                } else if (n > 0) {
                    disNextEmps = false;
                    disPrevEmps = false;
                }
                openDialog("dlgEmploye");
                update(":form-formation-01:VL-employe-formation");
            } else if (l.size() == 1) {
                demande.getEmetteur().setNom(l.get(0).getNom());
                demande.getEmetteur().setId(l.get(0).getId());
            }
        }
    }

    int offsetEmps = 0;
    private boolean disPrevEmps = true, disNextEmps;

    /**
     * *
     * CHOIX D'UN EMPLOYE
     */
    private Employe seleEmployeEmploye;
    private boolean vueListe;
    private Demande selectDemande;

    public Demande getSelectDemande() {
        return selectDemande;
    }

    public void setSelectDemande(Demande selectDemande) {
        this.selectDemande = selectDemande;
    }

    public Employe getSeleEmployeEmploye() {
        return seleEmployeEmploye;
    }

    public void setSeleEmployeEmploye(Employe seleEmployeEmploye) {
        this.seleEmployeEmploye = seleEmployeEmploye;
    }

    public boolean isVueListe() {
        return vueListe;
    }

    public void setVueListe(boolean vueListe) {
        this.vueListe = vueListe;
    }

    public void activeVueList() {
        vueListe = false;
    }

    public void desactiveVueListe() {
        vueListe = true;
    }

    private boolean vueListeDemande;

    public boolean isVueListeDemande() {
        return vueListeDemande;
    }

    public void setVueListeDemande(boolean vueListeDemande) {
        this.vueListeDemande = vueListeDemande;
    }

    public void activeVueListDemande() {
        vueListeDemande = false;
        listselectionDG.clear();
    }

    public void desactiveVueListeDemande() {
        vueListeDemande = true;
        listselectionDT.clear();
    }

    public void choixEmploye(SelectEvent ev) {
        if (ev != null) {
            demande.setRecepteur((Employe) ev.getObject());
        }
        closeDialog("dlgEmploye");
    }

    public void choixEmploye1(Employe ev) {
        demande.setEmetteur(ev);
    }

    public void choixDemande(Demande de) {
        populateView(de);
        de.setCheck(!de.isCheck());
        listdemande.set(listdemande.indexOf(de), de);
        if (de.isCheck()) {
            if (!listselectionDG.contains(de)) {
                listselectionDG.add(de);
            }
        } else if (!de.isCheck()) {
            if (listselectionDG.contains(de)) {
                listselectionDG.remove(de);
            }
        }
        System.out.println("*******listeselectionDG = " + listselectionDG.size());
        // System.out.println("id de la demande = "+listselectionDG.get(listselectionDG));
        updateDemande = true;
        selection = true;
        activeBtnSupprimer();
        System.out.println("----" + deleteDemande + "----");
        update("liste-demande");
    }

    public void loadAllEmploye(int limit, boolean init) {
        if (!init) {
            offsetEmps = +limit;
        } else {
            offsetEmps = 0;
        }
        employes.clear();
        champ = new String[]{"agence"};
        val = new Object[]{currentAgence};
        employes.clear();
        employes = UtilGrh.buildListEmployeBean(dao.loadNameQueries("YvsGrhEmployes.findAll", champ, val, offsetEmps, limit));
        int n = employes.size();
        disPrevEmps = offsetEmps <= 0;
        if (n < limit) {
            disNextEmps = true;
        } else if (n == 0) {
            disNextEmps = true;
            disPrevEmps = true;
        } else if (n > 0) {
            disNextEmps = false;
            disPrevEmps = false;
        }
    }

    //chargement de toutes les demandes
    public void loadAllDemande() {
        listdemande.clear();
         champ = new String[]{"agence"};
        val = new Object[]{currentAgence};
        listdemande = UtilGrh.buildBeanDemande(dao.loadNameQueries("YvsDemandeEmps.findAll", champ, val));
    }

    //chargement de tous les types de demandes
    public void loadAllTypeDemande() {
        listType.clear();
        listvalue.clear();
        champ = new String[]{"societe"};
         val = new Object[]{currentAgence.getSociete()};
        listvalue = UtilGrh.buildBeanListeTypeDemande(dao.loadNameQueries("YvsTypeDemande.findAll", champ, val));
        for (TypeDemande listvalue1 : listvalue) {
            String ajoutTypeChaine = "";
            ajoutTypeChaine = listvalue1.getLibelle();
            listType.add(ajoutTypeChaine);
        }

    }

    YvsDemandeEmps currentdemande;

    //création de l'entité yvsdemandeemps
    private YvsDemandeEmps buildDemande(Demande d) {
        YvsDemandeEmps de = new YvsDemandeEmps();
        de.setDateDebut(d.getDateDebut());
        de.setDateDemande(d.getDateDemande());
        de.setId((updateDemande) ? d.getId() : null);
        //de.setDateDemande();
        de.setDateFin(d.getDateFin());
        champ = new String[]{"codeUsers", "agence"};
        val = new Object[]{currentUser.getUsers().getCodeUsers(), currentAgence};
        de.setMessage(d.getMotif());
        String[] champ2 = new String[]{"societe"};
        Object[] val2 = new Object[]{currentAgence.getSociete()};
        de.setTypeDemande((YvsTypeDemande) dao.loadOneByNameQueries("YvsTypeDemande.findBySociete", champ2, val2));
        de.setEmetteur((YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCodeUsers1", champ, val));
        de.setDestinataire(new YvsGrhEmployes(d.getRecepteur().getId()));
        return de;
    }

    //création de l'entité yvstypedemande
    private YvsTypeDemande buildType(Demande d) {
        YvsTypeDemande type = new YvsTypeDemande();
        type.setLibelle(d.getType().getLibelle());
        type.setSociete(currentAgence.getSociete());
        return type;
    }

    //enregistrement d'une demande
    @Override
    public boolean saveNew() {
        if (controleFiche(demande)) {
            if (!updateDemande) {
                addDemande();
                //resetFiche();
                updateDemande = true;
                selection = true;
                loadAllDemande();
                disableB = false;
                update("bloc-demande");
//                succes();
            } else if (updateDemande) {
                System.err.println("----UPDATE-----");
                updateBean();
                succes();
//                resetFiche();
                updateDemande = true;
                selection = true;
                loadAllDemande();
                update("bloc-demande");
            }
        }
        return true;
    }

    public boolean update() {
        activVue = !activVue;
        deleteDemande = false;
        update("bloc-demande");
        update("bloc-en-tete-demande");
        return true;
    }

    //enregistrement d'un type
    public boolean saveNewType() {
        System.err.println("----saveNewType-----");
        ajoutType();
        loadAllTypeDemande();
        //update(":form-demande-00:tabview-demande:panelVue:typeDemande");
        update("bloc-demande");
        return true;
    }

    //controle des éléments rentrés sur le formulaire
    @Override
    public boolean controleFiche(Demande bean) {
        if (bean.getRecepteur().getId() == 0) {
            getMessage("Vous devez selectionner un employé", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if ("".equals(bean.getType().getLibelle())) {
            getMessage("Vous devez selectionner un type de demande", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if ("".equals(bean.getMotif())) {
            getMessage("Vous devez entrer un motif", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    //mise a jour d'une demande
    @Override
    public void updateBean() {
        currentdemande = buildDemande(demande);
        currentdemande = (YvsDemandeEmps) dao.update(currentdemande);
    }

    @Override
    public void resetFiche() {
        resetFiche(demande);
        demande.setEmetteur(new Employe());
        demande.setRecepteur(new Employe());
        demande.setType(new TypeDemande());
        updateDemande = false;
        selection = false;
        deleteDemande = false;
        update("bloc-demande");
        update("bloc-en-tete-demande");
    }

    //suppression d'une demande
    public void deleteBean() {
//        System.out.println("listeselectionDG = "+listselectionDT.size());
        if (listselectionDG != null) {
            if (!listselectionDG.isEmpty()) {
                for (Demande listselectionDG1 : listselectionDG) {
                    dao.delete(new YvsDemandeEmps(listselectionDG1.getId()));
                    listdemande.remove(new Demande(listselectionDG1.getId()));
                }
                listselectionDG.clear();
            }
        }
        if (listselectionDT != null) {
            if (!listselectionDT.isEmpty()) {
                for (Demande listselection1 : listselectionDT) {
                    dao.delete(new YvsDemandeEmps(listselection1.getId()));
                    listdemande.remove(new Demande(listselection1.getId()));
                }
                listselectionDT.clear();
            }
        }
        succes();
        resetFiche();
    }

    @Override
    public Demande recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //transfère les infos de l'objet selectionné à l'objet associé à la vue
    @Override
    public void populateView(Demande bean) {
        demande.setDateDebut(bean.getDateDebut());
        demande.setDateDemande(bean.getDateDemande());
        demande.setDateFin(bean.getDateFin());
        demande.setMotif(bean.getMotif());
        demande.setType(bean.getType());
        demande.setId(bean.getId());
        demande.setRecepteur(bean.getRecepteur());
        demande.setCheck(bean.isCheck());
    }

    //raffraichit la page en la faisant passer du formulaire à la liste des demandes et vice versa
    public void loadDemandeVue() {
        //openDialog("dlgDemande");
        activVue = !activVue;
        if (selection) {
            deleteDemande = !deleteDemande;
        }
        update("bloc-demande");
        update("bloc-en-tete-demande");
    }

    //recupère les infos sur l'objet selectionné
    @Override
    public void loadOnView(SelectEvent ev) {
        Demande de = (Demande) ev.getObject();
        populateView(de);
        updateDemande = true;
        selection = true;
        activeBtnSupprimer();
        System.out.println("----" + deleteDemande + "----");
        System.out.println("listselection DT = " + listselectionDT.size());
    }
    
    public void vider(){
        if(listselectionDT.isEmpty()){
            resetFiche();
        }
    }

    //activation du btn supprimer
    public void activeBtnSupprimer() {
        deleteDemande = true;
        update("bloc-en-tete-demande");
    }

    @Override
    public void loadAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

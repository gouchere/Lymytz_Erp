/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.StreamedContent;
import yvs.entity.grh.param.YvsGrhDomainesQualifications;
import yvs.entity.grh.param.YvsGrhQualifications;
import yvs.init.Initialisation;
//import yvs.tiers.ManagedTiers;
import yvs.util.Managed;
import yvs.util.Util;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Qualification extends Managed<Qualification, YvsBaseCaisse> implements Serializable {

    private long id;
    private String codeInterne;
    private String intitule;
    private String description;
    private String cheminDesc;
    private DomainesQualifications domaine = new DomainesQualifications();
    private boolean actif;
    private long idLiaison; //modélise l'id des tables en liaison tel que (QualificationFormation ou QualificationEmployé,...)

    private List<Qualification> listValue;
    private List<Qualification> listFilter;

    private StreamedContent file;

    public Qualification() {
        listValue = new ArrayList<>();
    }

    public Qualification(long id) {
        this.id = id;
    }

    public Qualification(long id, String intitule) {
        this.id = id;
        this.intitule = intitule;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public List<Qualification> getListValue() {
        return listValue;
    }

    public void setListValue(List<Qualification> listValue) {
        this.listValue = listValue;
    }

    public List<Qualification> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<Qualification> listFilter) {
        this.listFilter = listFilter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCheminDesc() {
        return cheminDesc;
    }

    public void setCheminDesc(String cheminDesc) {
        this.cheminDesc = cheminDesc;
    }

    public StreamedContent getFile() {
        return file;
    }

    public String getCodeInterne() {
        return codeInterne;
    }

    public void setCodeInterne(String codeInterne) {
        this.codeInterne = codeInterne;
    }

    public DomainesQualifications getDomaine() {
        return domaine;
    }

    public void setDomaine(DomainesQualifications domaine) {
        this.domaine = domaine;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public long getIdLiaison() {
        return idLiaison;
    }

    public void setIdLiaison(long idLiaison) {
        this.idLiaison = idLiaison;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Qualification other = (Qualification) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public boolean controleFiche(Qualification bean) {
        //on s'assure qu'il y a pas déja d'ntité portant le même titre en bd
        String[] champ = new String[]{"designation"};
        Object[] val = new Object[]{bean.getIntitule()};
        Long nbre = (Long) dao.loadObjectByNameQueries("YvsQualifications.count", champ, val);
        long d = (nbre != null) ? nbre : 0;
        return d == 0;
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Qualification recopieView() {
        Qualification cal = new Qualification();
        cloneObject(cal, this);
        return cal;
    }

    @Override
    public void populateView(Qualification bean) {
        this.id = bean.getId();
        this.intitule = bean.getIntitule();
        this.description = bean.getDescription();
        this.cheminDesc = bean.getCheminDesc();
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        Qualification qal = (Qualification) ev.getObject();
        populateView(qal);
        System.err.println("file " + this.getCheminDesc());
    }

    @Override
    public void loadAll() {
        listValue.clear();
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        List<YvsGrhQualifications> l = dao.loadNameQueries("YvsQualifications.findAll", champ, val);
        for (YvsGrhQualifications d : l) {
            Qualification dd = buildEntityByBean(d);
            listValue.add(0, dd);
        }
    }

    private Qualification buildEntityByBean(YvsGrhQualifications c) {
        Qualification cal = new Qualification();
        cal.setCheminDesc(c.getFile());
        cal.setDescription(c.getDescription());
        cal.setIntitule(c.getDesignation());
        cal.setId(c.getId());
        return cal;
    }

    private YvsGrhQualifications buildEntityByBean(Qualification c) {
        YvsGrhQualifications cal = new YvsGrhQualifications();
        cal.setDescription(c.getDescription());
        cal.setDesignation(c.getIntitule());
        cal.setFile(c.getCheminDesc());
        cal.setDomaine(new YvsGrhDomainesQualifications(c.getDomaine().getId()));
        return cal;
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(this)) {
            //persiste
            dao.save(buildEntityByBean(this));
            listValue.add(0, recopieView());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(this);
        update("butons");
        update("view");
    }

    /*
     * gère l'ajout d'un fichier description de la qualification
     */
    public void handleFileUpload(FileUploadEvent ev) {
        String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminDocEnterprise().substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminDocEnterprise().length());
//répertoire destination de sauvegarge= C:\\users\lymytz...
        String repDestSVG = Initialisation.getCheminDocEnterprise();
        String file = Util.giveFileName() + "." + Util.getExtension(ev.getFile().getFileName());
        try {
            Util.copyFile(file, repDest + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
            //copie dans le dossier de sauvegarde
            Util.copySVGFile(file, repDestSVG + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
            File f = new File(new StringBuilder(repDest).append(file).toString());
            if (!f.exists()) {
                File doc = new File(repDest);
                if (!doc.exists()) {
                    doc.mkdirs();
                    f.createNewFile();
                } else {
                    f.createNewFile();
                }
            }
            this.setCheminDesc(file);
            FacesMessage msg = new FacesMessage("Success! ", ev.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (IOException ex) {
            Logger.getLogger(Qualification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void downloadFile() {
        if (this.getCheminDesc() != null) {
            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminDocEnterprise().substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminDocEnterprise().length());
            String file = FacesContext.getCurrentInstance().getExternalContext().getRealPath(repDest + "" + Initialisation.FILE_SEPARATOR + this.getCheminDesc());
            File f = new File(file);
            System.err.println("File " + f.exists());
            byte[] bytes = Util.read(f);
            Util.openFile(FacesContext.getCurrentInstance(), bytes, Util.getExtension(this.getCheminDesc()), this.getCheminDesc());
        }
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

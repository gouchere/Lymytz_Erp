/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.tiers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.tiers.YvsBaseTiersDocument;
import yvs.service.base.tiers.IYvsBaseTiersDocument;
import yvs.service.base.tiers.SYvsBaseTiersDocument;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.Util;

/**
 *
 * @author Lymytz-pc
 */
@ManagedBean
@SessionScoped
public class ManagedTiersDocument extends Managed<TiersDocument, YvsBaseTiersDocument> implements Serializable {

    private Tiers tiers = new Tiers();
    private TiersDocument bean = new TiersDocument();
    private YvsBaseTiersDocument entity = new YvsBaseTiersDocument();
    private List<YvsBaseTiersDocument> list, selection;
    private StreamedContent apercu;

    IYvsBaseTiersDocument service;

    public ManagedTiersDocument() {
        list = new ArrayList<>();
        selection = new ArrayList<>();
    }

    public StreamedContent getApercu() {
        return apercu;
    }

    public void setApercu(StreamedContent apercu) {
        this.apercu = apercu;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }

    public TiersDocument getBean() {
        return bean;
    }

    public void setBean(TiersDocument bean) {
        this.bean = bean;
    }

    public YvsBaseTiersDocument getEntity() {
        return entity;
    }

    public void setEntity(YvsBaseTiersDocument entity) {
        this.entity = entity;
    }

    public List<YvsBaseTiersDocument> getList() {
        return list;
    }

    public void setList(List<YvsBaseTiersDocument> list) {
        this.list = list;
    }

    public List<YvsBaseTiersDocument> getSelection() {
        return selection;
    }

    public void setSelection(List<YvsBaseTiersDocument> selection) {
        this.selection = selection;
    }

    public IYvsBaseTiersDocument getService() {
        return service;
    }

    public void setService(IYvsBaseTiersDocument service) {
        this.service = service;
    }

    @Override
    public void loadAll() {
        service = (SYvsBaseTiersDocument) IEntitiSax.createInstance("IYvsBaseTiersDocument", dao);
    }

    public void loadAll(Tiers tiers) {
        this.list.clear();
        this.tiers = tiers;
        if (tiers != null ? tiers.getId() > 0 : false) {
            list = dao.loadNameQueries("YvsBaseTiersDocument.findByTiers", new String[]{"tiers"}, new Object[]{new YvsBaseTiers(tiers.getId())});
        }
        resetFiche();
    }

    @Override
    public boolean controleFiche(TiersDocument bean) {
        return true;
    }

    @Override
    public boolean saveNew() {
        try {
            if (service != null) {
                entity = UtilTiers.buildTiersDocument(bean, currentUser);
                ResultatAction<YvsBaseTiersDocument> result = null;
                if (bean.getId() < 1) {
                    result = service.save(entity);
                } else {
                    result = service.update(entity);
                }
                if (result != null ? result.isResult() : false) {
                    entity = (YvsBaseTiersDocument) result.getData();
                    int index = list.indexOf(entity);
                    if (index > 1) {
                        list.set(index, entity);
                    } else {
                        list.add(entity);
                    }
                    succes();
                    resetFiche();
                    update("blog_profil_tiers:data-tiers_document");
                    return true;
                } else {
                    getErrorMessage(result != null ? result.getMessage() : "Action impossible!!!");
                }
            }
        } catch (Exception ex) {
            getException("saveNew", ex);
        }
        return false;
    }

    @Override
    public void deleteBean() {
        try {
            if (selection != null ? !selection.isEmpty() : false) {
                for (YvsBaseTiersDocument y : selection) {
                    deleteBean(y);
                }
            }
        } catch (Exception ex) {
            getException("deleteBean", ex);
        }
    }

    @Override
    public void deleteBean(YvsBaseTiersDocument y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                dao.delete(y);
                list.remove(y);
                update("blog_profil_tiers:data-tiers_document");
            }
        } catch (Exception ex) {
            getException("deleteBean", ex);
        }
    }

    @Override
    public void resetFiche() {
        bean = new TiersDocument();
        bean.setTiers(tiers);
        update("blog_profil_tiers:form-tiers_document");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsBaseTiersDocument) ev.getObject());
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    @Override
    public void onSelectObject(YvsBaseTiersDocument y) {
        this.entity = y;
        bean = UtilTiers.buildBeanTiersDocument(y);
        update("blog_profil_tiers:form-tiers_document");
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            InputStream is = event.getFile().getInputstream();
            String extension = Util.getExtension(event.getFile().getFileName());
            byte[] bytes = IOUtils.toByteArray(is);
            String file = new String(Base64.encodeBase64(bytes));
            bean.setFichier(file);
            bean.setExtension(extension);
            getInfoMessage("Charger !");
        } catch (IOException ex) {
            getErrorMessage("Action impossible!!!");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void chooseType() {
        try {
            switch (bean.getType()) {
                case Constantes.FILE_TIERS_NUI:
                    bean.setTitre(Constantes.FILE_TIERS_NUI_NAME);
                    break;
                case Constantes.FILE_TIERS_RCC:
                    bean.setTitre(Constantes.FILE_TIERS_RCC_NAME);
                    break;
                case Constantes.FILE_TIERS_RIB:
                    bean.setTitre(Constantes.FILE_TIERS_RIB_NAME);
                    break;
                default:
                    bean.setTitre("");
                    break;
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean showOther() {
        try {
            return bean.getType().equals(Constantes.AUTRE);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public void onBuildApercu(YvsBaseTiersDocument y) {
        try {
            entity = y;
            byte[] decode = Base64.decodeBase64(y.getFichier());
            ByteArrayInputStream bis = new ByteArrayInputStream(decode);
            String contentType = "text/html";
            switch (y.getExtension()) {
                case "doc":
                    contentType = "application/msword";
                    break;
                case "pdf":
                    contentType = "application/pdf";
                    break;
                case "docx":
                    contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                    break;
                case "xls":
                    contentType = "application/vnd.ms-excel";
                    break;
                case "xlsx":
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    break;
            }
            apercu = new DefaultStreamedContent(bis, contentType);
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

}

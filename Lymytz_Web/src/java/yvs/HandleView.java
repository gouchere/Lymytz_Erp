/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs;

import java.io.IOException;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 *
 * @author GOUCHERE YVES
 */
public class HandleView extends ViewHandler {
 
    ViewHandler parent;

    public HandleView() {
    }

    public HandleView(ViewHandler parent) {
        this.parent = parent;
    }

    @Override
    public Locale calculateLocale(FacesContext context) {
        return parent.calculateLocale(context);
    }

    @Override
    public String calculateRenderKitId(FacesContext context) {
        return parent.calculateRenderKitId(context);
    }

    @Override
    public UIViewRoot createView(FacesContext context, String viewId) {
        return parent.createView(context, viewId);
    }

    @Override
    public String getActionURL(FacesContext context, String viewId) {
        return parent.getActionURL(context, viewId);
    }

    @Override
    public String getResourceURL(FacesContext context, String path) {
        return parent.getResourceURL(context, path);
    }

    @Override
    public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
        parent.renderView(context, viewToRender);
    }

    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        UIViewRoot root = null;
        root = parent.restoreView(context, viewId);
        if (root == null) {
            root = createView(context, viewId);
        }
        return root;


    }

    @Override
    public void writeState(FacesContext context) throws IOException {
        parent.writeState(context);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsers;

/**
 *
 * @author GOUCHERE YVES
 */
public abstract class ManagedUtil {

    public int numero = 1; //servira 0 numéroter les ligne d'un tableau
    public DateFormat df = (FacesContext.getCurrentInstance() != null) ? new SimpleDateFormat((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("formatDateTime")) : new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public Messages message = new Messages();
    public YvsAgences currentAgence = ((YvsAgences) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("agenc"));
    public YvsSocietes currentScte = ((YvsSocietes) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("societ"));
    public YvsUsers currentUser = ((YvsUsers) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
    @EJB
    public DaoInterfaceLocal dao;
    @ManagedProperty(value = "#{chargements}")
    public Chargements chargement;
    private String defaultButton;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDefaultButton() {
        return defaultButton;
    }

    public void setDefaultButton(String defaultButton) {
        this.defaultButton = defaultButton;
    }

    public YvsAgences getCurrentAgence() {
        return currentAgence;
    }

    public YvsSocietes getCurrentScte() {
        return currentScte;
    }

    public Chargements getChargement() {
        return chargement;
    }

    public void setChargement(Chargements chargement) {
        this.chargement = chargement;
    }

    public void resetFiche(Object o) {
        Class t = o.getClass();
        Field[] lf = t.getDeclaredFields();
        for (Field f : lf) {
            f.setAccessible(true);
            Class type = f.getType();
            try {
                switch (type.getSimpleName()) {
                    case "long":
                        f.set(o, 0);
                        break;
                    case "boolean":
                        if (f.getName().equals("actif")) {
                            f.set(o, true);
                        } else {
                            f.set(o, true);
                        }
                        break;
                    case "double":
                        f.set(o, 0);
                        break;
                    case "int":
                        f.set(o, 0);
                        break;
                    case "short":
                        f.set(o, 0);
                        break;
                    case "List":
//                        System.out.println("Liste");
                        break;
                    case "char":
                        f.set(o, ' ');
                        break;
                    default:
                        f.set(o, null);
                        break;
                }

            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ManagedUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ManagedUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void resetFiche(Object o, String str) {
        resetFiche(o);
        update(str);
    }

    public void cloneObject(Object o0, Object o) {
//        Class t0 = o0.getClass();
        Field[] lf0 = o0.getClass().getDeclaredFields();
        Class t = o.getClass();
        Field[] lf = t.getDeclaredFields();
        int i = 0;
        for (Field f : lf) {
            try {
                lf0[i].setAccessible(true);
                f.setAccessible(true);
                lf0[i].set(o0, f.get(o)); 
                i++;
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ManagedUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ManagedUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void succes() {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes !", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void getMessage(String message, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(severity, message, "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void update(String id) {
        RequestContext.getCurrentInstance().update(id);
    }

    public void execute(String id) {
        RequestContext.getCurrentInstance().execute(id);
    }

    public String getUserOnLine() {
        String str;
        str = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codeUser");
        return str;
    }

    public int getLongueurCompte() {
        int t;
        t = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("longueurCompte");
        return (t == 0) ? 9 : t;
    }

    public String getDevise() {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("devise");
    }

    public void openDialog(String widget) {
        RequestContext.getCurrentInstance().execute(widget + ".show()");
    }

    public void closeDialog(String widget) {
        RequestContext.getCurrentInstance().execute(widget + ".hide()");
    }

   
}

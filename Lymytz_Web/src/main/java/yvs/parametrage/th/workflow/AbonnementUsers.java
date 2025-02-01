/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.th.workflow;

/**
 *
 * @author GOUCHERE YVES
 */
public class AbonnementUsers {

    private boolean ajout;
    private boolean modif;
    private boolean remove;
    private String msgAjout;
    private String msgModif;
    private String msgRemove;

    public AbonnementUsers() {
    }

    public boolean isAjout() {
        return ajout;
    }

    public void setAjout(boolean ajout) {
        this.ajout = ajout;
    }

    public boolean isModif() {
        return modif;
    }

    public void setModif(boolean modif) {
        this.modif = modif;
    }

    public String getMsgAjout() {
        return msgAjout;
    }

    public void setMsgAjout(String msgAjout) {
        this.msgAjout = msgAjout;
    }

    public String getMsgModif() {
        return msgModif;
    }

    public void setMsgModif(String msgModif) {
        this.msgModif = msgModif;
    }

    public String getMsgRemove() {
        return msgRemove;
    }

    public void setMsgRemove(String msgRemove) {
        this.msgRemove = msgRemove;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

/**
 *
 * @author hp Elite 8300
 */
public class ObjectMessages {

    private int id;
    private String titre;
    private String detail;

    public ObjectMessages() {
    }

    public ObjectMessages(int id, String titre, String detail) {
        this.id = id;
        this.titre = titre;
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}

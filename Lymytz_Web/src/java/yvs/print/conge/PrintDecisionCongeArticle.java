/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.print.conge;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.print.YvsPrintDecisionCongeArticle;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class PrintDecisionCongeArticle implements Serializable {

    private long id;
    private String titre;
    private String contenu;
    private String indice;
    private int niveau = 1;
    private Date dateSave = new Date();
    private PrintDecisionCongeHeader header = new PrintDecisionCongeHeader();

    public PrintDecisionCongeArticle() {
    }

    public PrintDecisionCongeArticle(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu != null ? contenu : "";
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public PrintDecisionCongeHeader getHeader() {
        return header;
    }

    public void setHeader(PrintDecisionCongeHeader header) {
        this.header = header;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PrintDecisionCongeArticle other = (PrintDecisionCongeArticle) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public static PrintDecisionCongeArticle buildBean(YvsPrintDecisionCongeArticle e) {
        PrintDecisionCongeArticle r = new PrintDecisionCongeArticle();
        if (e != null) {
            r.setId(e.getId());
            r.setDateSave(e.getDateSave());
            r.setContenu(e.getContenu());
            r.setIndice(e.getIndice());
            r.setNiveau(e.getNiveau());
            r.setTitre(e.getTitre());
            r.setHeader(PrintDecisionCongeHeader.buildBean(e.getHeader()));
        }
        return r;
    }

    public static YvsPrintDecisionCongeArticle buildEntity(PrintDecisionCongeArticle e, YvsUsersAgence ua) {
        YvsPrintDecisionCongeArticle r = new YvsPrintDecisionCongeArticle();
        if (e != null) {
            r.setId(e.getId());
            r.setDateSave(e.getDateSave());
            r.setContenu(e.getContenu());
            r.setIndice(e.getIndice());
            r.setNiveau(e.getNiveau());
            r.setTitre(e.getTitre());
            if (e.getHeader() != null ? e.getHeader().getId() > 0 : false) {
                r.setHeader(PrintDecisionCongeHeader.buildEntity(e.getHeader(), null, ua));
            }
            r.setDateSave(new Date());
            r.setAuthor(ua);
        }
        return r;
    }

}

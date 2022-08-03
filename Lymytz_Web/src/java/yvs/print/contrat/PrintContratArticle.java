/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.print.contrat;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.print.YvsPrintContratEmployeArticle;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class PrintContratArticle implements Serializable {

    private long id;
    private String titre;
    private String contenu;
    private String indice;
    private int niveau = 1;
    private Date dateSave = new Date();
    private PrintContratHeader header = new PrintContratHeader();

    public PrintContratArticle() {
    }

    public PrintContratArticle(long id) {
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

    public PrintContratHeader getHeader() {
        return header;
    }

    public void setHeader(PrintContratHeader header) {
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
        final PrintContratArticle other = (PrintContratArticle) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public static PrintContratArticle buildBean(YvsPrintContratEmployeArticle e) {
        PrintContratArticle r = new PrintContratArticle();
        if (e != null) {
            r.setId(e.getId());
            r.setDateSave(e.getDateSave());
            r.setContenu(e.getContenu());
            r.setIndice(e.getIndice());
            r.setNiveau(e.getNiveau());
            r.setTitre(e.getTitre());
            r.setHeader(PrintContratHeader.buildBean(e.getHeader()));
        }
        return r;
    }

    public static YvsPrintContratEmployeArticle buildEntity(PrintContratArticle e, YvsUsersAgence ua) {
        YvsPrintContratEmployeArticle r = new YvsPrintContratEmployeArticle();
        if (e != null) {
            r.setId(e.getId());
            r.setDateSave(e.getDateSave());
            r.setContenu(e.getContenu());
            r.setIndice(e.getIndice());
            r.setNiveau(e.getNiveau());
            r.setTitre(e.getTitre());
            if (e.getHeader() != null ? e.getHeader().getId() > 0 : false) {
                r.setHeader(PrintContratHeader.buildEntity(e.getHeader(), null, ua));
            }
            r.setDateSave(new Date());
            r.setAuthor(ua);
        }
        return r;
    }

}

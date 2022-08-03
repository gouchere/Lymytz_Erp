/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class Version implements Serializable {

    private String name;
    private List<Update> updates;

    public Version() {
        updates = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Update> getUpdates() {
        return updates;
    }

    public void setUpdates(List<Update> updates) {
        this.updates = updates;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.name);
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
        final Version other = (Version) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    public static class Update {

        private String header;
        private String content;

        public Update() {
        }

        public Update(String header, String content) {
            this.header = header;
            this.content = content;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 17 * hash + Objects.hashCode(this.header);
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
            final Update other = (Update) obj;
            if (!Objects.equals(this.header, other.header)) {
                return false;
            }
            return true;
        }

    }

}

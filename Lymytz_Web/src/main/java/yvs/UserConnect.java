/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs;

import java.util.Date;

/**
 *
 * @author hp Elite 8300
 */
public class UserConnect {

    private long id;
    private String idSession;
    private String nameUsers;
    private Date timeConnect;

    public UserConnect() { 
    }

    public UserConnect(long id) {
        this.id = id;
    }

    public UserConnect(long id, String idSession, String nameUsers, Date timeConnect) {
        this.id = id;
        this.idSession = idSession;
        this.nameUsers = nameUsers;
        this.timeConnect = timeConnect;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdSession() {
        return idSession;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
    }

    public String getNameUsers() {
        return nameUsers;
    }

    public void setNameUsers(String nameUsers) {
        this.nameUsers = nameUsers;
    }

    public Date getTimeConnect() {
        return timeConnect;
    }

    public void setTimeConnect(Date timeConnect) {
        this.timeConnect = timeConnect;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final UserConnect other = (UserConnect) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

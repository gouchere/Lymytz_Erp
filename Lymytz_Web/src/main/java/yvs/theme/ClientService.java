/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.theme;

/**
 *
 * @author LYMYTZ
 */
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.commercial.client.YvsComClient;

@ManagedBean(name = "clientService", eager = true)
@SessionScoped
public class ClientService implements Serializable{

    private List<YvsComClient> clients;

    public ClientService() {
        clients = new ArrayList<>();
    }

    public ClientService(List<YvsComClient> clients) {
        this.clients = clients;
    }

    public List<YvsComClient> getClients() {
        return clients;
    }

    public void setClients(List<YvsComClient> clients) {
        this.clients = clients;
    }
}

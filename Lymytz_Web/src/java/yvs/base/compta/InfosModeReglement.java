/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class InfosModeReglement implements Serializable {

    private long id;
    private String authorisationHeader;
    private String channelUserMsisdn;
    private String channelUserPin;
    private String merchantKey;
    private String currency;
    private Date dateSave = new Date();
    private ModeDeReglement mode;

    public InfosModeReglement() {
    }

    public InfosModeReglement(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthorisationHeader() {
        return authorisationHeader;
    }

    public void setAuthorisationHeader(String authorisationHeader) {
        this.authorisationHeader = authorisationHeader;
    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public void setMerchantKey(String merchantKey) {
        this.merchantKey = merchantKey;
    }

    public String getChannelUserMsisdn() {
        return channelUserMsisdn;
    }

    public void setChannelUserMsisdn(String channelUserMsisdn) {
        this.channelUserMsisdn = channelUserMsisdn;
    }

    public String getChannelUserPin() {
        return channelUserPin;
    }

    public void setChannelUserPin(String channelUserPin) {
        this.channelUserPin = channelUserPin;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public ModeDeReglement getMode() {
        return mode;
    }

    public void setMode(ModeDeReglement mode) {
        this.mode = mode;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final InfosModeReglement other = (InfosModeReglement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_base_mode_reglement_informations")
@NamedQueries({
    @NamedQuery(name = "YvsBaseModeReglementInformations.findAll", query = "SELECT y FROM YvsBaseModeReglementInformations y"),
    @NamedQuery(name = "YvsBaseModeReglementInformations.findById", query = "SELECT y FROM YvsBaseModeReglementInformations y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseModeReglementInformations.findByAuthorisationHeader", query = "SELECT y FROM YvsBaseModeReglementInformations y WHERE y.authorisationHeader = :authorisationHeader"),
    @NamedQuery(name = "YvsBaseModeReglementInformations.findByMerchantKey", query = "SELECT y FROM YvsBaseModeReglementInformations y WHERE y.merchantKey = :merchantKey"),
    @NamedQuery(name = "YvsBaseModeReglementInformations.findByCurrency", query = "SELECT y FROM YvsBaseModeReglementInformations y WHERE y.currency = :currency"),
    @NamedQuery(name = "YvsBaseModeReglementInformations.findByDateUpdate", query = "SELECT y FROM YvsBaseModeReglementInformations y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseModeReglementInformations.findByDateSave", query = "SELECT y FROM YvsBaseModeReglementInformations y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseModeReglementInformations.findByExecuteTrigger", query = "SELECT y FROM YvsBaseModeReglementInformations y WHERE y.executeTrigger = :executeTrigger")})
public class YvsBaseModeReglementInformations implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_mode_reglement_informations_id_seq", name = "yvs_base_mode_reglement_informations_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_mode_reglement_informations_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "authorisation_header")
    private String authorisationHeader;
    @Size(max = 2147483647)
    @Column(name = "merchant_key")
    private String merchantKey;
    @Size(max = 2147483647)
    @Column(name = "channel_user_msisdn")
    private String channelUserMsisdn;
    @Size(max = 2147483647)
    @Column(name = "channel_user_pin")
    private String channelUserPin;
    @Size(max = 2147483647)
    @Column(name = "currency")
    private String currency;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "mode", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseModeReglement mode;

    public YvsBaseModeReglementInformations() {
    }

    public YvsBaseModeReglementInformations(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getExecuteTrigger() {
        return executeTrigger;
    }

    public void setExecuteTrigger(String executeTrigger) {
        this.executeTrigger = executeTrigger;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseModeReglement getMode() {
        return mode;
    }

    public void setMode(YvsBaseModeReglement mode) {
        this.mode = mode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsBaseModeReglementInformations)) {
            return false;
        }
        YvsBaseModeReglementInformations other = (YvsBaseModeReglementInformations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseModeReglementInformations[ id=" + id + " ]";
    }
    
}

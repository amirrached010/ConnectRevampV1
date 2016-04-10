/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect.revampv1;

import java.util.Properties;

/**
 *
 * @author Amir.Rashed

 */
public class ConnectRevampCDR {
    String msisdn;             // Customer dial
    String timeStamp;               // timestamp
    String templates;           // templates
    String quotas;              // quotas
    String expiryDate;           // expiryDate
    

   
    public ConnectRevampCDR(){
        
    }
    
    public ConnectRevampCDR(String [] CDRs) {
        if(CDRs.length < 6)
            return;
        this.msisdn = CDRs[0];
        this.timeStamp = CDRs[1];
        this.templates = CDRs[2];
        this.quotas = CDRs[3];
        this.expiryDate = CDRs[4];
       
    }

    public ConnectRevampCDR(String CDR, int linecounter){
       
        String [] CDRs =CDR.split(",");
        if(CDRs.length < 6)
            return;
        this.msisdn = CDRs[0];
        this.timeStamp = CDRs[1];
        this.templates = CDRs[2];
        this.quotas = CDRs[3];
        this.expiryDate = CDRs[4];
      
    }
    
    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getQuotas() {
        return quotas;
    }

    public void setQuotas(String quotas) {
        this.quotas = quotas;
    }

    public String getTemplates() {
        return templates;
    }

    public void setTemplates(String templates) {
        this.templates = templates;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    
    @Override
    public String toString() {
        return "CDR{" + "msisdn=" + msisdn + ", quotas=" + quotas + ", pp_after=" + templates + ", time=" + timeStamp + ", rech_val=" + expiryDate + '}';
    }
    
    
    
}


package connect.revampv1;

import java.util.ArrayList;
import java.util.Properties;

/**
 * This class containing The parsing of the CDR of ConnectRevamp.
 * 
 */
public class ConnectRevampCDR {
    String msisdn;             // Customer dial
    String timeStamp;               // timestamp
    String templates;           // templates
    ArrayList<String> parametersValue;           
    

   
    public ConnectRevampCDR(){
        
    }
    
    public ConnectRevampCDR(String [] CDRs) {

        parametersValue = new ArrayList<String>();
        this.msisdn = CDRs[0];
        this.timeStamp = CDRs[1];
        this.templates = CDRs[2];
        for(int i=3; i<CDRs.length;i++){
            parametersValue.add(CDRs[i]);
        }
       
    }

    public ConnectRevampCDR(String CDR, int linecounter){
       
        String [] CDRs =CDR.split(",");
        parametersValue = new ArrayList<String>();
        this.msisdn = CDRs[0];
        this.timeStamp = CDRs[1];
        this.templates = CDRs[2];
        for(int i=3; i<CDRs.length;i++){
            parametersValue.add(CDRs[i]);
        }
      
    }

    public ArrayList<String> getParametersValue() {
        return parametersValue;
    }

    public void setParametersValue(ArrayList<String> parametersValue) {
        this.parametersValue = parametersValue;
    }
    
    
    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
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

    @Override
    public String toString() {
        return "CDR{" + "msisdn=" + msisdn +  ", pp_after=" + templates + ", time=" + timeStamp + '}';
    }
    
    
    
}


package connect.template;


import connect.revampv1.Globals.TemplateParameterType;
import org.apache.log4j.Logger;

/**
 *
 * This class handles the Template Parameter in case it is a Quota Unit.
 */
public class TemplateParameterQuotaUnit extends TemplateParameterUnit{

    Logger logger;
    public TemplateParameterQuotaUnit(String unit,Logger logger) {
        super();
        this.logger=logger;
        setTemplateParameterUnitString(unit);
        super.templateParameterUnitType=TemplateParameterType.QUOTA;
    }
    
    public TemplateParameterQuotaUnit() {
        super();
        super.templateParameterUnitType=TemplateParameterType.QUOTA;
    }

    @Override
    public void setTemplateParameterUnitString(String template) {
        if("MB".equals(template))
            super.templateParameterUnitString="MB";
        else
            if("GB".equals(template))
                super.templateParameterUnitString="GB";
            else
                if("B".equals(template))
                    super.templateParameterUnitString="B";
                else
                    super.templateParameterUnitString=null;
    }

    @Override
    public String getTemplateParameterUnitString() {
         return super.templateParameterUnitString;
    }

    @Override
    public void setTemplateParameterUnitType(String template) {
        super.templateParameterUnitType=TemplateParameterType.QUOTA;
    }

    @Override
    public TemplateParameterType getTemplateParameterUnitType() {
        return super.templateParameterUnitType;
    }
    
    /**
     * Handles the conversion between what is sent in the CDR and what needs to appear in the SMS.
     * 
     * @param parameter
     * @return The Quota String that will appear in the SMS. 
     */
    @Override
    public String parseParameter(String parameter) {
        if(super.templateParameterUnitString == "MB"){
            long quota = Long.parseLong(parameter);
            return (quota/(1024*1024))+" \u0645\u064A\u062C\u0627 ";
        }
        else
        {
            if(super.templateParameterUnitString == "B"){
                long quota = Long.parseLong(parameter);
                return (quota)+" \u0628\u0627\u064A\u062A ";
            }
            else {
                if(super.templateParameterUnitString == "GB"){
                    double quota = Double.parseDouble(parameter);
                    return (quota/(1024*1024*1024))+" \u062C\u064A\u062C\u0627 ";
                }
            }
        }
        return null;
    }
}


package connect.template;

import connect.revampv1.Globals.TemplateParameterType;
import org.apache.log4j.Logger;

/**
 *
 * This class handles the Template Parameter in case it is a Money Unit.
 */
public class TemplateParameterMoneyUnit extends TemplateParameterUnit{

    Logger logger;
    public TemplateParameterMoneyUnit(String unit,Logger logger) {
        super();
        this.logger=logger;
        setTemplateParameterUnitString(unit);
        super.templateParameterUnitType=TemplateParameterType.MONEY;
    }
    
    public TemplateParameterMoneyUnit() {
        super();
        super.templateParameterUnitType=TemplateParameterType.MONEY;
    }

    @Override
    public void setTemplateParameterUnitString(String template) {
        if("PTS".equals(template))
            super.templateParameterUnitString="PTS";
        else
            if("EGP".equals(template))
                super.templateParameterUnitString="EGP";
            else
                super.templateParameterUnitString=null;
    }

    @Override
    public String getTemplateParameterUnitString() {
         return super.templateParameterUnitString;
    }

    @Override
    public void setTemplateParameterUnitType(String template) {
        super.templateParameterUnitType=TemplateParameterType.MONEY;
    }

    @Override
    public TemplateParameterType getTemplateParameterUnitType() {
        return super.templateParameterUnitType;
    }
    
    /**
     * Handles the conversion between what is sent in the CDR and what needs to appear in the SMS.
     * 
     * @param parameter
     * @return The Money String that will appear in the SMS. 
     */
    @Override
    public String parseParameter(String parameter) {
        if(super.templateParameterUnitString == "PTS"){
            long quota = Long.parseLong(parameter);
            return (quota)+"";
        }
        else
        {
            if(super.templateParameterUnitString == "EGP"){
                double quota = Double.parseDouble(parameter);
                return (quota/100)+"";
            }
            
        }
        return null;
    }
}

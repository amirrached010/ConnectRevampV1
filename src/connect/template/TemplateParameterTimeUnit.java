
package connect.template;

import connect.revampv1.Globals.TemplateParameterType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;


/**
 *
 * This class handles the Template Parameter in case it is a Time Unit.
 */
public class TemplateParameterTimeUnit extends TemplateParameterUnit{

    Logger logger;
    public TemplateParameterTimeUnit(String unit,Logger logger) {
        super();
        this.logger=logger;
        setTemplateParameterUnitString(unit);
        super.templateParameterUnitType=TemplateParameterType.TIME;
    }
    public TemplateParameterTimeUnit() {
        super();
        super.templateParameterUnitType=TemplateParameterType.TIME;
    }
    @Override
    public void setTemplateParameterUnitString(String template) {
        if("MIN".equals(template))
            super.templateParameterUnitString="MIN";
        else
            if("SEC".equals(template))
                super.templateParameterUnitString="SEC";
            else
                super.templateParameterUnitString=null;
    }

    @Override
    public String getTemplateParameterUnitString() {
         return super.templateParameterUnitString;
    }

    @Override
    public void setTemplateParameterUnitType(String template) {
        super.templateParameterUnitType=TemplateParameterType.TIME;
    }

    @Override
    public TemplateParameterType getTemplateParameterUnitType() {
        return super.templateParameterUnitType;
    }

    /**
     * Handles the conversion between what is sent in the CDR and what needs to appear in the SMS.
     * 
     * @param parameter
     * @return The Time String that will appear in the SMS. 
     */
    @Override
    public String parseParameter(String parameter) {
        if(super.templateParameterUnitString == "SEC"){
            long quota = Long.parseLong(parameter);
            return (quota)+"";
        }
        else
        {
            if(super.templateParameterUnitString == "MIN"){
                double quota = Double.parseDouble(parameter);
                return (quota/60)+"";
            }
            
        }
        return null;
    }
    
}

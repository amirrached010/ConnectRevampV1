/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect.template;

import connect.revampv1.Globals.TemplateParameterType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;


/**
 *
 * @author Amir.Rashed
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

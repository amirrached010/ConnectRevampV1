/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect.template;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import connect.revampv1.*;
import connect.revampv1.Globals.TemplateParameterType;

/**
 *
 * @author Amir.Rashed
 */
public class TemplateParameterDateUnit extends TemplateParameterUnit{

    Logger logger;
    public TemplateParameterDateUnit(String unit,Logger logger) {
        super();
        this.logger=logger;
        setTemplateParameterUnitString(unit);
        super.templateParameterUnitType=TemplateParameterType.DATESTAMP;
    }

    
    public TemplateParameterDateUnit() {
        super();
        super.templateParameterUnitType=TemplateParameterType.DATESTAMP;
    }

    @Override
    public void setTemplateParameterUnitString(String template) {
        if("DATESTAMP".equals(template))
                super.templateParameterUnitString="DATESTAMP";
            else
                super.templateParameterUnitString=null;
    }

    @Override
    public String getTemplateParameterUnitString() {
         return super.templateParameterUnitString;
    }

    @Override
    public void setTemplateParameterUnitType(String template) {
        super.templateParameterUnitType=TemplateParameterType.DATESTAMP;
    }

    @Override
    public TemplateParameterType getTemplateParameterUnitType() {
        return super.templateParameterUnitType;
    }

    @Override
    public String parseParameter(String parameter) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date currentDate = sdf.parse(parameter);
            return sdf1.format(currentDate);
        } catch (ParseException ex) {
            logger.error("Error in parsing Date");
        }
        return null;
    }
    
}

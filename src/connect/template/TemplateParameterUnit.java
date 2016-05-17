/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect.template;

import connect.revampv1.Globals.TemplateParameterType;
import org.apache.log4j.Logger;

/**
 *
 * This is an abstract class.
 */
public abstract class TemplateParameterUnit {
    String templateParameterUnitString;
    TemplateParameterType templateParameterUnitType;
    
    public abstract void setTemplateParameterUnitString(String template);
    
    public abstract String getTemplateParameterUnitString();
    
    public abstract void setTemplateParameterUnitType(String template);
    
    public abstract TemplateParameterType getTemplateParameterUnitType();

    public abstract String parseParameter(String parameter);
    
    /**
     * Gets the type of the TemplateParameter from the passed Unit String.
     * @param unit
     * @param logger
     * @return 
     */
    public static TemplateParameterUnit getTemplateParameterUnit(String unit,Logger logger){
        TemplateParameterUnit currentUnit = new TemplateParameterTimeUnit(unit,logger);
        
        if(currentUnit.getTemplateParameterUnitString() != null)
            return currentUnit;
        
        currentUnit = new TemplateParameterQuotaUnit(unit,logger);
        
        if(currentUnit.getTemplateParameterUnitString() != null)
            return currentUnit;
        
        currentUnit = new TemplateParameterDateUnit(unit,logger);
        
        if(currentUnit.getTemplateParameterUnitString() != null)
            return currentUnit;
        
        currentUnit = new TemplateParameterMoneyUnit(unit,logger);
        
        if(currentUnit.getTemplateParameterUnitString() != null)
            return currentUnit;
        
        return null;
        
    }

    
}





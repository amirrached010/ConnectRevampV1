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
 * @author Amir.Rashed
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect.template;

/**
 *
 * @author Amir.Rashed
 */
public class TemplateParameter {
    TemplateParameterUnit templateParameterUnit;
    String templateParameterValue;

    public TemplateParameter() {
    }

    
    public TemplateParameter(TemplateParameterUnit templateParameterUnit, String templateParameterValue) {
        this.templateParameterUnit = templateParameterUnit;
        this.templateParameterValue = templateParameterValue;
    }

    
    public TemplateParameterUnit getTemplateParameterUnit() {
        return templateParameterUnit;
    }

    public void setTemplateParameterUnit(TemplateParameterUnit templateParameterUnit) {
        this.templateParameterUnit = templateParameterUnit;
    }

    public String getTemplateParameterValue() {
        return templateParameterValue;
    }

    public void setTemplateParameterValue(String templateParameterValue) {
        this.templateParameterValue = this.templateParameterUnit.parseParameter(templateParameterValue);
    }
    
    
}

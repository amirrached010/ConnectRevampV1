
package connect.template;

/**
 * This class handles the each parameter related to any template sent in the CDR.
 *
 */
public class TemplateParameter {
    // The Unit of the templateParameter.
    TemplateParameterUnit templateParameterUnit;
    // The value of the templateParameter.
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

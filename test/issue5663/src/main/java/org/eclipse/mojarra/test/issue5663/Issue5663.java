package org.eclipse.mojarra.test.issue5663;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named
@RequestScoped
public class Issue5663 {

    private String result;

    public void form1button() {
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("form2");
    }

    public void form2button() {
        result = "form2button";
    }

    public String getResult() {
        return result;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import java.math.BigInteger;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import model_controller.TourBean;
import model_controller.UserBean;

/**
 *
 * @author USER
 */
@ManagedBean(name = "UserConverterBean") 
@FacesConverter(value = "userConverter")
public class TourConverter implements Converter{
    private transient EntityManager em;  
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return em.find(TourBean.class, new BigInteger(value));  
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return String.valueOf(((TourBean) value).getTourID());    
    }
    
}
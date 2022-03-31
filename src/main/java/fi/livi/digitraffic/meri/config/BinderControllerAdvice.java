package fi.livi.digitraffic.meri.config;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
@Order(10000)
public class BinderControllerAdvice {
    @InitBinder
    public void setAllowedFields(final WebDataBinder dataBinder) {
        final String[] denylist = new String[]{"class.*", "Class.*", "*.class.*", "*.Class.*"};

        dataBinder.setDisallowedFields(denylist);
    }
}


package vaadin.playground;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Tag(Tag.DIV)
public class GlobalExceptionHandler extends Component implements HasErrorParameter<Exception> {

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<Exception> parameter) {
        URI uri = URI.create("?error=Error");
        UI.getCurrent().getPage().setLocation(uri);
        return HttpServletResponse.SC_TEMPORARY_REDIRECT;
    }
}

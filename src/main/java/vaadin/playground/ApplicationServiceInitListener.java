package vaadin.playground;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class ApplicationServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(sessionInitEvent -> sessionInit(sessionInitEvent.getSession()));
    }

    private void sessionInit(VaadinSession session) {
        session.setErrorHandler(errorEvent -> UI.getCurrent().navigate(""));
    }
}

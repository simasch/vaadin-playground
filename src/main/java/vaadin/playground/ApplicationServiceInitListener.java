package vaadin.playground;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringComponent
public class ApplicationServiceInitListener implements VaadinServiceInitListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServiceInitListener.class);

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(sessionInitEvent -> sessionInit(sessionInitEvent.getSession()));
    }

    private void sessionInit(VaadinSession session) {
        session.setErrorHandler(errorEvent -> {
            LOGGER.error(errorEvent.getThrowable().getMessage(), errorEvent.getThrowable());

            UI.getCurrent().navigate("");
        });
    }
}

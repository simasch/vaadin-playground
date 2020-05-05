package vaadin.playground;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;

@Route
public class SelectView extends Div {

    public SelectView() {
        Select<String> select = new Select<>();
        select.setItems("Alpha", "Beta", "Gamma");

        select.addValueChangeListener(event -> {
            System.out.println(event.getValue());
        });

        add(select);
    }
}

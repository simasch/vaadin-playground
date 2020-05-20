package vaadin.playground;

import com.vaadin.componentfactory.Tooltip;
import com.vaadin.componentfactory.TooltipAlignment;
import com.vaadin.componentfactory.TooltipPosition;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;

@Route
public class SelectView extends Div {

    public SelectView() {
        Select<String> select = new Select<>();
        select.setItems("Alpha", "Beta", "Gamma");

        Tooltip tooltip = new Tooltip();

        tooltip.attachToComponent(select);

        tooltip.setPosition(TooltipPosition.RIGHT);
        tooltip.setAlignment(TooltipAlignment.LEFT);

        tooltip.add(new H5("Hello"));
        tooltip.add(new Paragraph("This is an example of how to use it"));

        select.addValueChangeListener(event -> {
            System.out.println(event.getValue());
        });

        add(select);
    }
}

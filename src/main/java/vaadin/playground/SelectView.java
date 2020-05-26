package vaadin.playground;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import org.vaadin.gatanaso.MultiselectComboBox;

@Route
public class SelectView extends Div {

    public SelectView() {
        Select<String> select = new Select<>();
        select.setItems("Alpha", "Beta", "Gamma");

        select.addValueChangeListener(event -> {
            System.out.println(event.getValue());
        });

        add(select);

        MultiselectComboBox<String> multiselectComboBox = new MultiselectComboBox<>();
        multiselectComboBox.setLabel("Select items");
        multiselectComboBox.setItems("Item 1", "Item 2", "Item 3", "Item 4");
        add(multiselectComboBox);

        MultiSelectListBox<String> multiSelectListBox = new MultiSelectListBox<>();
        multiSelectListBox.setItems("Item 1", "Item 2", "Item 3", "Item 4");
        add(multiSelectListBox);
    }
}

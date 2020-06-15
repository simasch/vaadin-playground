package vaadin.playground;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.ArrayList;
import java.util.List;

@Route
public class SelectView extends Div {

    public SelectView() {
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("1px", 1));
        add(formLayout);

        Select<String> select = new Select<>();
        select.setItems("Alpha", "Beta", "Gamma");

        select.addValueChangeListener(event -> {
            System.out.println(event.getValue());
        });

        formLayout.addFormItem(select, "select");

        MultiselectComboBox<String> multiselectComboBox = new MultiselectComboBox<>();
        multiselectComboBox.setLabel("Select items");
        multiselectComboBox.setItems(createItems());

        formLayout.addFormItem(multiselectComboBox, "multiselectComboBox");


        MultiSelectListBox<String> multiSelectListBox = new MultiSelectListBox<>();
        multiSelectListBox.setItems(createItems());
        multiselectComboBox.setHeight("300px");

        formLayout.addFormItem(multiSelectListBox, "multiSelectListBox");
    }

    private List<String> createItems() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            items.add("Item " + i);
        }
        return items;
    }
}

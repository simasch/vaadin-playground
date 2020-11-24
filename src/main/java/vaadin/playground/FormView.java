package vaadin.playground;

import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route
public class FormView extends FormLayout {

    public FormView() {
        TextField textField = new TextField();
        textField.setWidthFull();

        addFormItem(textField, "Text");

        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.setClassName("tosca-date-time-picker");
        dateTimePicker.setWidthFull();

        addFormItem(dateTimePicker, "Date and Time");
    }
}

package ch.martinelli.vaadinplayground;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

@Route(value = "index")
public class IndexView extends VerticalLayout {

    public IndexView() {
        Grid<Employee> grid = new Grid<>();

        Grid.Column<Employee> nameColumn = grid.addColumn(Employee::getName).setHeader("Name");
        Grid.Column<Employee> birthdayColumn = grid.addColumn(Employee::getBirthday).setHeader("Birthday");

        Binder<Employee> binder = new Binder<>(Employee.class);
        Editor<Employee> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        Div validationStatus = new Div();
        validationStatus.setId("validation");

        TextField field = new TextField();
        binder.forField(field)
                .withValidator(name -> name.startsWith("Employee"),
                        "Name should start with Person")
                .withStatusLabel(validationStatus).bind("name");
        nameColumn.setEditorComponent(field);

        DatePicker datePicker = new DatePicker();
        binder.bind(datePicker, "birthday");
        birthdayColumn.setEditorComponent(datePicker);

        Collection<Button> editButtons = Collections.newSetFromMap(new WeakHashMap<>());

        Grid.Column<Employee> editorColumn = grid.addComponentColumn(employee -> {
            Button edit = new Button("Edit");
            edit.addClassName("edit");
            edit.addClickListener(e -> {
                editor.editItem(employee);
                field.focus();
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        });

        editor.addOpenListener(e -> editButtons.stream().forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons.stream().forEach(button -> button.setEnabled(!editor.isOpen())));

        Button save = new Button("Save", e -> editor.save());
        save.addClassName("save");

        Button cancel = new Button("Cancel", e -> editor.cancel());
        cancel.addClassName("cancel");

        grid.getElement().addEventListener("keyup", event -> editor.cancel())
                .setFilter("event.key === 'Escape' || event.key === 'Esc'");

        Div buttons = new Div(save, cancel);
        editorColumn.setEditorComponent(buttons);

        editor.addSaveListener(
                event -> validationStatus.setText(event.getItem().getName() + ", " + event.getItem().getBirthday()));

        grid.setItems(new Employee("Simon", LocalDate.of(1980, 1, 1)),
                new Employee("Peter", LocalDate.of(2000, 12, 11)));

        add(grid);
    }

}

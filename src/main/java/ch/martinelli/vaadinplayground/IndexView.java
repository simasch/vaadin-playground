package ch.martinelli.vaadinplayground;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;

@Route(value = "index")
public class IndexView extends VerticalLayout {

    public IndexView() {
        Grid<Employee> grid = new Grid<>();

        Grid.Column<Employee> nameColumn = grid.addColumn((ValueProvider<Employee, String>) employee -> employee.getName())
                .setHeader("Name");
        Grid.Column<Employee> birthdayColumn = grid.addColumn((ValueProvider<Employee, LocalDate>) employee -> employee.getBirthday())
                .setHeader("Birthday");

        Binder<Employee> binder = new Binder<>(Employee.class);
        Editor<Employee> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        TextField field = new TextField();
        binder.bind(field, "name");
        nameColumn.setEditorComponent(field);

        DatePicker datePicker = new DatePicker();
        binder.bind(datePicker, "birthday");
        birthdayColumn.setEditorComponent(datePicker);

        grid.addItemClickListener(event -> {
            editor.save();
            editor.editItem(event.getItem());
        });

        grid.getElement().addEventListener("keyup", event -> editor.cancel())
                .setFilter("event.key === 'Escape' || event.key === 'Esc'");

        grid.setItems(new Employee("Simon", LocalDate.of(1980, 1, 1)),
                new Employee("Peter", LocalDate.of(2000, 12, 11)));

        add(grid);
    }

}

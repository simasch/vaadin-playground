package vaadin.playground;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.StringToBigIntegerConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Route
public class LargeView extends SplitLayout {

    public LargeView() {
        setHeightFull();

        setOrientation(Orientation.HORIZONTAL);

        Grid<Employee> grid = new Grid();
        grid.addColumn(Employee::getId).setHeader("ID");
        grid.addColumn(Employee::getName).setHeader("Name");
        grid.addColumn(Employee::getBirthday).setHeader("Birthday");
        grid.setItems(createEmployees());
        grid.setHeightFull();

        addToPrimary(grid);

        Binder<Employee> binder = new Binder<>();

        FormLayout formLayout = new FormLayout();

        TextField id = new TextField("ID");
        formLayout.add(id);
        binder.forField(id).withConverter(new StringToIntegerConverter("Must be a number")).bind(Employee::getId, null);

        TextField name = new TextField("Name");
        formLayout.add(name);
        binder.forField(name).bind(Employee::getName, Employee::setName);

        DatePicker datePicker = new DatePicker("Birthday");
        formLayout.add(datePicker);
        binder.forField(datePicker).bind(Employee::getBirthday, Employee::setBirthday);

        for (int i = 0; i < 100; i++) {
            formLayout.add(new TextField("#" + i));
        }

        addToSecondary(formLayout);

        setSplitterPosition(30);

        grid.addItemClickListener(event -> binder.setBean(event.getItem()));
    }

    private List<Employee> createEmployees() {
        Fairy fairy = Fairy.create();

        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            Person person = fairy.person();
            employees.add(new Employee(i, person.getFullName(), person.getDateOfBirth()));
        }
        return employees;
    }

}

package vaadin.playground;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Route
public class LargeView extends SplitLayout {

    private List<Employee> employees;

    public LargeView() {
        setHeightFull();

        setOrientation(Orientation.HORIZONTAL);

        Grid<Employee> grid = new Grid<>();
        grid.addColumn(Employee::getId).setHeader("ID");
        grid.addColumn(Employee::getName).setHeader("Name");
        grid.addColumn(Employee::getBirthday).setHeader("Birthday");
        grid.setHeightFull();

        DataProvider<Employee, Void> dataProvider = new CallbackDataProvider<>(
                query -> employees.stream().skip(query.getOffset()).limit(query.getLimit()),
                query -> employees.size(),
                Employee::getId
        );
        grid.setDataProvider(dataProvider);

        Button selectRecord100 = new Button("Select Record 100");
        selectRecord100.addClickListener(event -> {
            grid.select(employees.get(99));
            grid.scrollToIndex(99);
        });

        Button updateRecord100 = new Button("Update Record 100");
        updateRecord100.addClickListener(event -> {
            employees.get(99).setName("Jane Doe");
            dataProvider.refreshItem(employees.get(99));
        });

        Button replaceRecord100 = new Button("Replace Record 100");
        replaceRecord100.addClickListener(event -> {
            Employee employee = employees.get(99);
            employees.remove(99);
            employees.add(99, new Employee(employee.getId(), "John Doe", LocalDate.now()));
            dataProvider.refreshItem(employees.get(99));
        });

        addToPrimary(new VerticalLayout(grid, new HorizontalLayout(selectRecord100, updateRecord100, replaceRecord100)));

        Binder<Employee> binder = new Binder<>();

        FormLayout formLayout = new FormLayout();

        TextField id = new TextField("ID");
        formLayout.add(id);
        binder.forField(id).bind(Employee::getId, null);

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

        createEmployees();
    }

    private void createEmployees() {
        Fairy fairy = Fairy.create();

        employees = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            Person person = fairy.person();
            employees.add(new Employee(person.getFullName(), person.getDateOfBirth()));
        }
    }

}

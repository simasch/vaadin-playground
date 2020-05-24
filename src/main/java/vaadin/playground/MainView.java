package vaadin.playground;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.time.LocalDate;
import java.util.stream.Stream;

@Route
public class MainView extends VerticalLayout {

    public MainView() {
        TreeGrid<Employee> treeGrid = new TreeGrid<>();

        Grid.Column<Employee> nameColumn = treeGrid.addHierarchyColumn((ValueProvider<Employee, String>) Employee::getName)
                .setHeader("Name");
        Grid.Column<Employee> birthdayColumn = treeGrid.addColumn((ValueProvider<Employee, LocalDate>) Employee::getBirthday)
                .setHeader("Birthday");

        Binder<Employee> binder = new Binder<>(Employee.class);
        Editor<Employee> editor = treeGrid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        editor.addSaveListener(e -> {
            editor.cancel();
            treeGrid.getDataProvider().refreshAll();
        });

        TextField field = new TextField();
        binder.bind(field, "name");
        nameColumn.setEditorComponent(field);

        DatePicker datePicker = new DatePicker();
        binder.bind(datePicker, "birthday");
        birthdayColumn.setEditorComponent(datePicker);

        treeGrid.addItemClickListener(event -> {
            editor.save();
            editor.editItem(event.getItem());
        });

        treeGrid.getElement().addEventListener("keyup", event -> editor.cancel())
                .setFilter("event.key === 'Escape' || event.key === 'Esc'");

        Employee boss = createEmployees();

        treeGrid.setDataProvider(new AbstractBackEndHierarchicalDataProvider<Employee, Void>() {
            @Override
            public int getChildCount(HierarchicalQuery<Employee, Void> hierarchicalQuery) {
                if (hierarchicalQuery.getParent() == null) {
                    return 1;
                } else {
                    return hierarchicalQuery.getParent().getDirects().size();
                }
            }

            @Override
            public boolean hasChildren(Employee employee) {
                return !employee.getDirects().isEmpty();
            }

            @Override
            protected Stream<Employee> fetchChildrenFromBackEnd(HierarchicalQuery<Employee, Void> hierarchicalQuery) {
                if (hierarchicalQuery.getParent() == null) {
                    return Stream.of(boss);
                } else {
                    return hierarchicalQuery.getParent().getDirects().stream().skip(hierarchicalQuery.getOffset()).limit(hierarchicalQuery.getLimit());
                }
            }
        });

        add(treeGrid);

        add(new RouterLink("Tree", TreeView.class));
    }

    private Employee createEmployees() {
        Employee boss = new Employee(1, "Simon", LocalDate.of(1980, 1, 1));

        Employee direct1 = new Employee(2, "Peter", LocalDate.of(2000, 12, 11));
        boss.getDirects().add(direct1);
        direct1.setBoss(boss);

        Employee direct2 = new Employee(3, "Karin", LocalDate.of(2010, 2, 9));
        boss.getDirects().add(direct2);
        direct2.setBoss(boss);

        return boss;
    }

}

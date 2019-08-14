package ch.martinelli.vaadinplayground;

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

import java.time.LocalDate;
import java.util.stream.Stream;

@Route(value = "index")
public class IndexView extends VerticalLayout {

    public IndexView() {
        TreeGrid<Employee> treeGrid = new TreeGrid<>();

        Grid.Column<Employee> nameColumn = treeGrid.addHierarchyColumn((ValueProvider<Employee, String>) employee -> employee.getName())
                .setHeader("Name");
        Grid.Column<Employee> birthdayColumn = treeGrid.addColumn((ValueProvider<Employee, LocalDate>) employee -> employee.getBirthday())
                .setHeader("Birthday");

        Binder<Employee> binder = new Binder<>(Employee.class);
        Editor<Employee> editor = treeGrid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

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

        Employee direct = new Employee("Peter", LocalDate.of(2000, 12, 11));
        Employee root = new Employee("Simon", LocalDate.of(1980, 1, 1), direct);

        treeGrid.setDataProvider(new AbstractBackEndHierarchicalDataProvider<Employee, Void>() {
            @Override
            public int getChildCount(HierarchicalQuery<Employee, Void> hierarchicalQuery) {
                if (hierarchicalQuery.getParent() == null) {
                    return root.getDirects().size();
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
                    return Stream.of(root);
                } else {
                    return hierarchicalQuery.getParent().getDirects().stream().skip(hierarchicalQuery.getOffset()).limit(hierarchicalQuery.getLimit());
                }
            }
        });

        add(treeGrid);
    }

}

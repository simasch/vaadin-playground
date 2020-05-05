package vaadin.playground;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.stream.Stream;

@Route
public class TreeView extends VerticalLayout {

    Fairy fairy = Fairy.create();

    public TreeView() {
        TreeGrid<Employee> treeGrid = new TreeGrid<>();

        treeGrid.addHierarchyColumn((ValueProvider<Employee, String>) employee -> employee.getName()).setHeader("Name");
        treeGrid.addColumn((ValueProvider<Employee, LocalDate>) employee -> employee.getBirthday()).setHeader("Birthday");

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

        Button expand = new Button("Expand");
        expand.addClickListener(buttonClickEvent -> {
            treeGrid.expand(boss, boss.getDirects().get(100), boss.getDirects().get(100).getDirects().get(10));
            treeGrid.scrollToIndex(110);
            treeGrid.select(boss.getDirects().get(100).getDirects().get(10));
        });

        add(expand);

        Button exceptionButton = new Button("Throw RuntimeException");
        exceptionButton.addClickListener(event -> {
            throw new RuntimeException();
        });
        add(exceptionButton);

        throw new RuntimeException();
    }

    private Employee createEmployees() {
        Employee boss = new Employee("Boss", LocalDate.of(1980, 1, 1));
        generateEmployees(boss, 2000);
        for (Employee direct : boss.getDirects()) {
            generateEmployees(direct, 20);
        }
        return boss;
    }

    private void generateEmployees(Employee boss, int numberToGenerate) {
        for (int i = 0; i < numberToGenerate; i++) {
            Person person = fairy.person();
            Employee employee = new Employee(person.getFullName(), person.getDateOfBirth());
            boss.getDirects().add(employee);
            employee.setBoss(boss);
        }
    }

}

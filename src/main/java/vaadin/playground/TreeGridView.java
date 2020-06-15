package vaadin.playground;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Route
public class TreeGridView extends VerticalLayout {

    public TreeGridView() {
        TreeGrid<Employee> treeGrid = new TreeGrid<>();

        treeGrid.addHierarchyColumn((ValueProvider<Employee, String>) Employee::getName).setHeader("Name");
        treeGrid.addColumn((ValueProvider<Employee, LocalDate>) Employee::getBirthday).setHeader("Birthday");

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
            treeGrid.expand(boss, boss.getDirects().get(10), boss.getDirects().get(3).getDirects().get(3));
            treeGrid.scrollToIndex(30);
            treeGrid.select(boss.getDirects().get(3).getDirects().get(3));
        });

        add(expand);

        Button delete = new Button("Delete");
        delete.addClickListener(buttonClickEvent -> {
            treeGrid.getSelectedItems().stream().findFirst().ifPresent(employee -> {
                deleteEmployee(boss.getDirects(), employee);
                treeGrid.getDataProvider().refreshItem(employee.getBoss(), true);
            });
        });

        add(delete);

        Button refresh = new Button("Refresh");
        refresh.addClickListener(buttonClickEvent -> {
            treeGrid.getDataProvider().refreshAll();
        });

        add(refresh);
    }

    private void deleteEmployee(List<Employee> directs, Employee employee) {
        for (Employee direct : directs) {
            if (direct.equals(employee)) {
                direct.getBoss().getDirects().remove(employee);
                return;
            } else {
                deleteEmployee(direct.getDirects(), employee);
            }
        }
    }

    private Employee createEmployees() {
        Employee boss = new Employee("Boss", LocalDate.of(1980, 1, 1));
        generateEmployees(boss, 200);
        for (Employee direct : boss.getDirects()) {
            generateEmployees(direct, 5);
        }
        return boss;
    }

    private void generateEmployees(Employee boss, int numberToGenerate) {
        Fairy fairy = Fairy.create();

        for (int i = 0; i < numberToGenerate; i++) {
            Person person = fairy.person();
            Employee employee = new Employee(person.getFullName(), person.getDateOfBirth());
            boss.getDirects().add(employee);
            employee.setBoss(boss);
        }
    }

}

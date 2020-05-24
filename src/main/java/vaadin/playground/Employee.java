package vaadin.playground;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Employee {

    private Integer id;
    private String name;
    private LocalDate birthday;
    private Employee boss;
    private List<Employee> directs = new ArrayList<>();

    public Employee(Integer id, String name, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Employee getBoss() {
        return boss;
    }

    public void setBoss(Employee boss) {
        this.boss = boss;
    }

    public List<Employee> getDirects() {
        return directs;
    }

    public void setDirects(List<Employee> directs) {
        this.directs = directs;
    }
}

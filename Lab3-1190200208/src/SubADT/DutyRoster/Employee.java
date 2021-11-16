package SubADT.DutyRoster;

import java.util.Objects;

public class Employee {
    private final String name; // 姓名
    private final String duty; // 职务
    private final String phone;// 电话

    public Employee(String name, String duty, String phone) {
        this.name = name;
        this.duty = duty;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getDuty() {
        return duty;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(name, employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class EmployeeService {
    public final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee getEmployeeById(long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> getEmployeesById(List<Long> id) {
        return employeeRepository.findAllById(id);
    }

    public List<Employee> getEmployeeBySkillAndDayAvailable(Employee employee) {
        return employeeRepository.findEmployeesBySkillsAndDaysAvailable(employee.getSkills(), employee.getDaysAvailable(), employee.getSkills().size(), employee.getDaysAvailable().size());
    }

//    public Set<EmployeeSkill> getSkillsByEmployeeIds(List<Long> employeeIds) {
//        return employeeRepository.findSkillsByEmployeeIds(employeeIds);
//    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
}

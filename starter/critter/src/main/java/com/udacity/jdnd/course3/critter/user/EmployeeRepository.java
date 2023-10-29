package com.udacity.jdnd.course3.critter.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e " +
            "WHERE " +
            "(SELECT COUNT(s) FROM e.skills s WHERE s IN :skills) >= :minSkills " +
            "AND (SELECT COUNT(d) FROM e.daysAvailable d WHERE d IN :daysAvailable) >= :minDaysAvailable")
    List<Employee> findEmployeesBySkillsAndDaysAvailable(Set<EmployeeSkill> skills, Set<DayOfWeek> daysAvailable, long minSkills, long minDaysAvailable);

    @Query("SELECT es FROM Employee e JOIN e.skills es WHERE e.id IN :employeeIds")
    Set<EmployeeSkill> findSkillsByEmployeeIds(List<Long> employeeIds);
}

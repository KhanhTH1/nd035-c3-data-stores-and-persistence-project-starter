package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule s JOIN s.pets p WHERE p.id = :petId")
    List<Schedule> findSchedulesByPetId(long petId);

    @Query("SELECT s FROM Schedule s JOIN s.pets p WHERE p.id IN :petIds")
    List<Schedule> findSchedulesByPetIds(List<Long> petIds);

    @Query("SELECT s FROM Schedule s JOIN s.employees e WHERE e.id = :employeeId")
    List<Schedule> findSchedulesByEmployeeId(long employeeId);
}

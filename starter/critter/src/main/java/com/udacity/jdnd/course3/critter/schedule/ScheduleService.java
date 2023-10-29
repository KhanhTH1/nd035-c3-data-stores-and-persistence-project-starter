package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ScheduleService {
    public final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getSchedulesByPetId(long petId) {
        return scheduleRepository.findSchedulesByPetId(petId);
    }

    public List<Schedule> getSchedulesByPetIds(List<Long> petIds) {
        return scheduleRepository.findSchedulesByPetIds(petIds);
    }

    public List<Schedule> getSchedulesByEmployeeId(long employeeId) {
        return scheduleRepository.findSchedulesByEmployeeId(employeeId);
    }

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }
}

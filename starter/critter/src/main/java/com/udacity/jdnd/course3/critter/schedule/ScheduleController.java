package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    public final ScheduleService scheduleService;
    public final EmployeeService employeeService;
    public final PetService petService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, EmployeeService employeeService, PetService petService) {
        this.scheduleService = scheduleService;
        this.employeeService = employeeService;
        this.petService = petService;
    }


    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        List<Employee> employeeList = employeeService.getEmployeesById(scheduleDTO.getEmployeeIds());
        Set<Employee> employeeSet = new HashSet<>(employeeList);
        List<Pet> petList = petService.getPetsById(scheduleDTO.getPetIds());
        Set<Pet> petSet = new HashSet<>(petList);

        Schedule schedule = scheduleService.saveSchedule(mapScheduleDTOToSchedule(scheduleDTO, employeeSet, petSet));
        return mapScheduleToScheduleDTO(schedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> scheduleList = scheduleService.getAllSchedules();
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        if (!scheduleList.isEmpty()) {
            for (Schedule schedule : scheduleList) {
                scheduleDTOList.add(mapScheduleToScheduleDTO(schedule));
            }
        }

        return scheduleDTOList;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> scheduleList = scheduleService.getSchedulesByPetId(petId);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        if (!scheduleList.isEmpty()) {
            for (Schedule schedule : scheduleList) {
                scheduleDTOList.add(mapScheduleToScheduleDTO(schedule));
            }
        }

        return scheduleDTOList;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> scheduleList = scheduleService.getSchedulesByEmployeeId(employeeId);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        if (!scheduleList.isEmpty()) {
            for (Schedule schedule : scheduleList) {
                scheduleDTOList.add(mapScheduleToScheduleDTO(schedule));
            }
        }

        return scheduleDTOList;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Pet> petList = petService.getPetsByOwnerId(customerId);
        List<Long> petIds = new ArrayList<>();

        if (petList.isEmpty()) {
            return null;
        }

        for (Pet pet : petList) {
            petIds.add(pet.getId());
        }

        List<Schedule> scheduleList = scheduleService.getSchedulesByPetIds(petIds);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        if (!scheduleList.isEmpty()) {
            for (Schedule schedule : scheduleList) {
                scheduleDTOList.add(mapScheduleToScheduleDTO(schedule));
            }
        }

        return scheduleDTOList;
    }

    public Schedule mapScheduleDTOToSchedule(ScheduleDTO scheduleDTO, Set<Employee> employeeSet, Set<Pet> petSet) {
        Schedule schedule = new Schedule();

        schedule.setEmployees(employeeSet);
        schedule.setPets(petSet);
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());

        return schedule;
    }

    public ScheduleDTO mapScheduleToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        List<Long> employeeIds = new ArrayList<>();
        List<Long> petIds = new ArrayList<>();

        for (Employee employee : schedule.getEmployees()) {
            employeeIds.add(employee.getId());
        }

        for (Pet pet : schedule.getPets()) {
            petIds.add(pet.getId());
        }

        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setEmployeeIds(employeeIds);
        scheduleDTO.setPetIds(petIds);
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setActivities(schedule.getActivities());

        return scheduleDTO;
    }
}

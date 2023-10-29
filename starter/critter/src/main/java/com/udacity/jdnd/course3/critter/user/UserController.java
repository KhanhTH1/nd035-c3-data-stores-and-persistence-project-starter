package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final PetService petService;

    @Autowired
    public UserController(CustomerService customerService, EmployeeService employeeService, PetService petService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.petService = petService;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = customerService.saveCustomer(mapCustomerDTOToCustomer(customerDTO));
        return mapCustomerToCustomerDTO(customer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customerList = customerService.getAllCustomer();

        if (customerList.isEmpty()) {
            return null;
        }

        List<CustomerDTO> customerDTOList = new ArrayList<>();

        for (Customer customer : customerList) {
            customerDTOList.add(mapCustomerToCustomerDTO(customer));
        }

        return customerDTOList;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        Pet pet = petService.getPetById(petId);

        if (pet == null || pet.getOwner() == null) {
            return null;
        }

        Customer customer = customerService.getCustomerById(pet.getOwner().getId());
        return mapCustomerToCustomerDTO(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeService.saveEmployee(mapEmployeeDTOToEmployee(employeeDTO));
        return mapEmployeeToEmployeeDTO(employee);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        return mapEmployeeToEmployeeDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);

        if (employee != null) {
            employee.setDaysAvailable(daysAvailable);
            employeeService.saveEmployee(employee);
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employeeList = employeeService.getEmployeeBySkillAndDayAvailable(mapEmployeeRequestDTOToEmployee(employeeDTO));

        if (employeeList.isEmpty()) {
            return null;
        }

        List<EmployeeDTO> employeeDTOList = new ArrayList<>();

        for (Employee employee : employeeList) {
            employeeDTOList.add(mapEmployeeToEmployeeDTO(employee));
        }

        return employeeDTOList;
    }

    public Customer mapCustomerDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();

        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());

        return customer;
    }

    public CustomerDTO mapCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        List<Long> petIds = new ArrayList<>();
        List<Pet> pets = customer.getPets();

        if (pets != null && !pets.isEmpty()) {
            for (Pet pet : pets) {
                petIds.add(pet.getId());
            }
        }

        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setNotes(customer.getNotes());
        customerDTO.setPetIds(petIds);

        return customerDTO;
    }

    public Employee mapEmployeeDTOToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        employee.setName(employeeDTO.getName());
        employee.setSkills(employeeDTO.getSkills());
        employee.setDaysAvailable(employeeDTO.getDaysAvailable());

        return employee;
    }

    public EmployeeDTO mapEmployeeToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSkills(employee.getSkills());
        employeeDTO.setDaysAvailable(employee.getDaysAvailable());

        return employeeDTO;
    }

    public Employee mapEmployeeRequestDTOToEmployee(EmployeeRequestDTO employeeRequestDTO) {
        Employee employee = new Employee();
        Set<DayOfWeek> daysAvailable = new HashSet<>();

        daysAvailable.add(employeeRequestDTO.getDate().getDayOfWeek());

        employee.setSkills(employeeRequestDTO.getSkills());
        employee.setDaysAvailable(daysAvailable);

        return employee;
    }
}

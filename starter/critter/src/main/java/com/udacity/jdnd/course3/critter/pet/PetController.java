package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;
    private final CustomerService customerService;

    @Autowired
    public PetController(PetService petService, CustomerService customerService) {
        this.petService = petService;
        this.customerService = customerService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Customer customer = customerService.getCustomerById(petDTO.getOwnerId());
        Pet inputPet = mapPetDTOToPet(petDTO, customer);

        if (customer.getPets() == null || customer.getPets().isEmpty()) {
            List<Pet> petList = new ArrayList<>();
            petList.add(inputPet);
            customer.setPets(petList);
        } else {
            customer.getPets().add(inputPet);
        }

        Pet pet = petService.savePet(inputPet);
        return mapPetToPetDTO(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.getPetById(petId);
        return mapPetToPetDTO(pet);
    }

    @GetMapping
    public List<PetDTO> getPets() {
        List<Pet> petList = petService.getAllPets();

        if (petList.isEmpty()) {
            return null;
        }

        List<PetDTO> petDTOList = new ArrayList<>();

        for (Pet pet : petList) {
            petDTOList.add(mapPetToPetDTO(pet));
        }

        return petDTOList;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> petList = petService.getPetsByOwnerId(ownerId);

        if (petList.isEmpty()) {
            return null;
        }

        List<PetDTO> petDTOList = new ArrayList<>();

        for (Pet pet : petList) {
            petDTOList.add(mapPetToPetDTO(pet));
        }

        return petDTOList;
    }

    public Pet mapPetDTOToPet(PetDTO petDTO, Customer customer) {
        Pet pet = new Pet();

        pet.setType(petDTO.getType());
        pet.setName(petDTO.getName());
        pet.setOwner(customer);
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setNotes(petDTO.getNotes());

        return pet;
    }

    public PetDTO mapPetToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();

        petDTO.setId(pet.getId());
        petDTO.setType(pet.getType());
        petDTO.setName(pet.getName());
        petDTO.setOwnerId(pet.getOwner().getId());
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setNotes(pet.getNotes());

        return petDTO;
    }
}

package com.udacity.jdnd.course3.critter.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PetService {
    public final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet getPetById(long id) {
        return petRepository.findById(id).orElse(null);
    }

    public List<Pet> getPetsById(List<Long> id) {
        return petRepository.findAllById(id);
    }

    public List<Pet> getPetsByOwnerId(long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }

    public Pet savePet(Pet pet) {
        return petRepository.save(pet);
    }
}

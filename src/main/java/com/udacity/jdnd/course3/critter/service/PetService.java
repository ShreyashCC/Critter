package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Pet savePet(Pet pet, Long ownerId) {
        // Find the customer (owner) by ID
        Customer customer = customerRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + ownerId));

        // Set owner in pet
        pet.setOwner(customer);

        // Save pet to DB
        Pet savedPet = petRepository.save(pet);

        // Make sure customer's pet list is initialized
        if (customer.getPets() == null) {
            customer.setPets(new ArrayList<>());
        }

        // Add pet to customer's list
        customer.getPets().add(savedPet);

        // Save customer to update the pet list relationship
        customerRepository.save(customer);

        return savedPet;
    }

    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found with id: " + petId));
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));
        return customer.getPets() != null ? customer.getPets() : new ArrayList<>();
    }
}

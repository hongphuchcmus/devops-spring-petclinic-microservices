package org.springframework.samples.petclinic.customers.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.model.OwnerRepository;
import org.springframework.samples.petclinic.customers.model.Pet;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(OwnerResource.class)
class OwnerResourceTest {

  private Owner owner;

  @Autowired
  MockMvc mvc;

  @MockBean
  OwnerRepository ownerRepository;

  private void setUp() {
      owner = new Owner();
      owner.setFirstName("John");
      owner.setLastName("Doe");
      owner.setAddress("123 Main St");
      owner.setCity("Springfield");
      owner.setTelephone("1234567890");
  }

  @Test
  void testGetters() {
      setUp();
      assertEquals("John", owner.getFirstName());
      assertEquals("Doe", owner.getLastName());
      assertEquals("123 Main St", owner.getAddress());
      assertEquals("Springfield", owner.getCity());
      assertEquals("1234567890", owner.getTelephone());
  }

  @Test
  void testSetters() {
      setUp();
      owner.setFirstName("Jane");
      owner.setLastName("Smith");
      owner.setAddress("456 Elm St");
      owner.setCity("Shelbyville");
      owner.setTelephone("0987654321");

      assertEquals("Jane", owner.getFirstName());
      assertEquals("Smith", owner.getLastName());
      assertEquals("456 Elm St", owner.getAddress());
      assertEquals("Shelbyville", owner.getCity());
      assertEquals("0987654321", owner.getTelephone());
  }

  @Test
  void testAddPet() {
      setUp();
      Pet pet = new Pet();
      pet.setName("Buddy");

      owner.addPet(pet);

      List<Pet> pets = owner.getPets();
      assertEquals(1, pets.size());
      assertEquals("Buddy", pets.get(0).getName());
      assertEquals(owner, pets.get(0).getOwner());
  }

  @Test
  void testGetPets() {
      setUp();
      Pet pet1 = new Pet();
      pet1.setName("Buddy");

      Pet pet2 = new Pet();
      pet2.setName("Charlie");

      owner.addPet(pet1);
      owner.addPet(pet2);

      List<Pet> pets = owner.getPets();
      assertEquals(2, pets.size());
      assertEquals("Buddy", pets.get(0).getName());
      assertEquals("Charlie", pets.get(1).getName());
  }

  @Test
  void testToString() {
      setUp();
      String expected = "Owner[id=null, lastName=Doe, firstName=John, address=123 Main St, city=Springfield, telephone=1234567890]";
      assertTrue(owner.toString().contains("id=null"));
      assertTrue(owner.toString().contains("lastName=Doe"));
      assertTrue(owner.toString().contains("firstName=John"));
  }
  
}
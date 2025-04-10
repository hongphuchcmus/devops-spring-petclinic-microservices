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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Optional;
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
  
  @Test
  void shouldGetOwnerById() throws Exception {
      // Arrange: Create a mock Owner object
      Owner owner = new Owner();
      owner.setId(1);
      owner.setFirstName("John");
      owner.setLastName("Doe");
      owner.setAddress("123 Main St");
      owner.setCity("Springfield");
      owner.setTelephone("1234567890");

      // Mock the repository to return the owner when queried
      given(ownerRepository.findById(1)).willReturn(Optional.of(owner));

      // Act & Assert: Perform a GET request and verify the response
      mvc.perform(get("/owners/1").accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk()) // Check that the status is 200 OK
          .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Check the response type
          .andExpect(jsonPath("$.id").value(1)) // Verify the ID
          .andExpect(jsonPath("$.firstName").value("John")) // Verify the first name
          .andExpect(jsonPath("$.lastName").value("Doe")) // Verify the last name
          .andExpect(jsonPath("$.address").value("123 Main St")) // Verify the address
          .andExpect(jsonPath("$.city").value("Springfield")) // Verify the city
          .andExpect(jsonPath("$.telephone").value("1234567890")); // Verify the telephone
  }

  @Test
  void shouldCreateNewOwner() throws Exception {
      // Arrange: Create a mock Owner object
      Owner owner = new Owner();
      owner.setId(2);
      owner.setFirstName("Jane");
      owner.setLastName("Smith");
      owner.setAddress("456 Elm St");
      owner.setCity("Shelbyville");
      owner.setTelephone("0987654321");

      // Mock the repository to save the owner
      given(ownerRepository.save(Mockito.any(Owner.class))).willReturn(owner);

      // Act & Assert: Perform a POST request and verify the response
      mvc.perform(post("/owners")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"address\":\"456 Elm St\",\"city\":\"Shelbyville\",\"telephone\":\"0987654321\"}"))
          .andExpect(status().isCreated()) // Check that the status is 201 Created
          .andExpect(jsonPath("$.id").value(2)) // Verify the ID
          .andExpect(jsonPath("$.firstName").value("Jane")) // Verify the first name
          .andExpect(jsonPath("$.lastName").value("Smith")) // Verify the last name
          .andExpect(jsonPath("$.address").value("456 Elm St")) // Verify the address
          .andExpect(jsonPath("$.city").value("Shelbyville")) // Verify the city
          .andExpect(jsonPath("$.telephone").value("0987654321")); // Verify the telephone
  }

  @Test
  void shouldUpdateOwner() throws Exception {
      // Arrange: Create a mock Owner object
      Owner existingOwner = new Owner();
      existingOwner.setId(3);
      existingOwner.setFirstName("OldName");
      existingOwner.setLastName("OldLastName");

      Owner updatedOwner = new Owner();
      updatedOwner.setId(3);
      updatedOwner.setFirstName("NewName");
      updatedOwner.setLastName("NewLastName");

      // Mock the repository to find and save the owner
      given(ownerRepository.findById(3)).willReturn(Optional.of(existingOwner));
      given(ownerRepository.save(Mockito.any(Owner.class))).willReturn(updatedOwner);

      // Act & Assert: Perform a PUT request and verify the response
      mvc.perform(put("/owners/3")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"firstName\":\"NewName\",\"lastName\":\"NewLastName\"}"))
          .andExpect(status().isOk()) // Check that the status is 200 OK
          .andExpect(jsonPath("$.id").value(3)) // Verify the ID
          .andExpect(jsonPath("$.firstName").value("NewName")) // Verify the updated first name
          .andExpect(jsonPath("$.lastName").value("NewLastName")); // Verify the updated last name
  }

  @Test
  void shouldDeleteOwner() throws Exception {
      // Arrange: Mock the repository to do nothing when deleting
      doNothing().when(ownerRepository).deleteById(4);

      // Act & Assert: Perform a DELETE request and verify the response
      mvc.perform(delete("/owners/4"))
          .andExpect(status().isNoContent()); // Check that the status is 204 No Content
  }
}
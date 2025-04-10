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

  @Autowired
  private MockMvc mvc;

  @MockBean
  private OwnerRepository ownerRepository;

  @MockBean
  private OwnerEntityMapper ownerEntityMapper;

  @Test
  void shouldCreateOwner() throws Exception {
    Owner owner = new Owner();
    owner.setId(1);
    owner.setFirstName("John");
    owner.setLastName("Doe");
    owner.setAddress("123 Main St");
    owner.setCity("Springfield");
    owner.setTelephone("1234567890");

    given(ownerRepository.save(Mockito.any(Owner.class))).willReturn(owner);

    mvc.perform(post("/owners")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":\"123 Main St\",\"city\":\"Springfield\",\"telephone\":\"1234567890\"}"))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").value(1))
      .andExpect(jsonPath("$.firstName").value("John"))
      .andExpect(jsonPath("$.lastName").value("Doe"));
  }

  @Test
  void shouldFindOwnerById() throws Exception {
    Owner owner = new Owner();
    owner.setId(1);
    owner.setFirstName("John");
    owner.setLastName("Doe");

    given(ownerRepository.findById(1)).willReturn(Optional.of(owner));

    mvc.perform(get("/owners/1").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(1))
      .andExpect(jsonPath("$.firstName").value("John"))
      .andExpect(jsonPath("$.lastName").value("Doe"));
  }

  @Test
  void shouldReturnNotFoundForNonExistingOwner() throws Exception {
    given(ownerRepository.findById(99)).willReturn(Optional.empty());

    mvc.perform(get("/owners/99").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldFindAllOwners() throws Exception {
    Owner owner1 = new Owner();
    owner1.setId(1);
    owner1.setFirstName("John");
    owner1.setLastName("Doe");

    Owner owner2 = new Owner();
    owner2.setId(2);
    owner2.setFirstName("Jane");
    owner2.setLastName("Smith");

    given(ownerRepository.findAll()).willReturn(Arrays.asList(owner1, owner2));

    mvc.perform(get("/owners").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").value(1))
      .andExpect(jsonPath("$[0].firstName").value("John"))
      .andExpect(jsonPath("$[1].id").value(2))
      .andExpect(jsonPath("$[1].firstName").value("Jane"));
  }

  @Test
  void shouldUpdateOwner() throws Exception {
    Owner existingOwner = new Owner();
    existingOwner.setId(1);
    existingOwner.setFirstName("John");
    existingOwner.setLastName("Doe");

    given(ownerRepository.findById(1)).willReturn(Optional.of(existingOwner));
    given(ownerRepository.save(Mockito.any(Owner.class))).willReturn(existingOwner);

    mvc.perform(put("/owners/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"firstName\":\"John\",\"lastName\":\"Updated\",\"address\":\"123 Main St\",\"city\":\"Springfield\",\"telephone\":\"1234567890\"}"))
      .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnNotFoundWhenUpdatingNonExistingOwner() throws Exception {
    given(ownerRepository.findById(99)).willReturn(Optional.empty());

    mvc.perform(put("/owners/99")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"firstName\":\"John\",\"lastName\":\"Updated\",\"address\":\"123 Main St\",\"city\":\"Springfield\",\"telephone\":\"1234567890\"}"))
      .andExpect(status().isNotFound());
  }
}
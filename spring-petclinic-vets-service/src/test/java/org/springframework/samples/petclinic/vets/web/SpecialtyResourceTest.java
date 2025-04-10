package org.springframework.samples.petclinic.vets.web;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.vets.model.Specialty;
import static org.assertj.core.api.Assertions.assertThat;



class SpecialtyResourceTest {

  @Test
  void shouldCreateSpecialty() {
    Specialty specialty = new Specialty();
    specialty.setName("surgery");
    specialty.setId(1);
    assertThat(specialty).isNotNull();
    assertThat(specialty.getId()).isEqualTo(1);
    assertThat(specialty.getName()).isEqualTo("surgery");
  }
  
}
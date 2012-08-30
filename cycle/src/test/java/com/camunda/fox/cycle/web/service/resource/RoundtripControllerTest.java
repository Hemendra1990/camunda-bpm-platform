package com.camunda.fox.cycle.web.service.resource;

import java.util.Date;
import com.camunda.fox.cycle.repository.*;
import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.*;
import org.junit.After;
import static org.junit.Assume.*;
import static org.junit.Assert.*;

import com.camunda.fox.cycle.entity.Roundtrip;
import com.camunda.fox.cycle.web.dto.BpmnDiagramDTO;
import com.camunda.fox.cycle.web.dto.RoundtripDTO;

/**
 *
 * @author nico.rehwaldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
  loader = SpringockitoContextLoader.class, 
  locations = {"classpath:/spring/context.xml", "classpath:/spring/test-*.xml"}
)
public class RoundtripControllerTest {

  @Inject
  private RoundtripRepository roundtripRepository;

  @Inject
  private RoundtripService roundtripController;

  @After
  public void after() {
    // Remove all entities
    roundtripRepository.deleteAll();
  }

  @Test
  public void shouldCreateRoundtrip() throws Exception {
    // given
    RoundtripDTO data = createTestRoundtripDTOWithDetails();
    
    // when
    RoundtripDTO createdData = roundtripController.create(data);
   
    // then
    assertThat(createdData.getId(), notNullValue());
  }

  @Test
  public void shouldUpdateRoundtripDetails() throws Exception {
    // given
    Roundtrip r = roundtripRepository.saveAndFlush(new Roundtrip("Test Roundtrip"));
    RoundtripDTO data = createTestRoundtripDTOWithDetails();
    data.setId(r.getId());
    
    // when
    RoundtripDTO createdData = roundtripController.updateDetails(data);
    
    // then
    assertThat(createdData.getLeftHandSide(), is(notNullValue()));
    assertThat(createdData.getRightHandSide(), is(notNullValue()));
    assertThat(createdData.getLeftHandSide().getId(), is(notNullValue()));
    assertThat(createdData.getRightHandSide().getId(), is(notNullValue()));
  }

  private RoundtripDTO createTestRoundtripDTO() {
    RoundtripDTO dto = new RoundtripDTO();
    dto.setLastSync(new Date());
    dto.setName("Test Roundtrip");
    return dto;
  }

  private RoundtripDTO createTestRoundtripDTOWithDetails() {
    RoundtripDTO dto = createTestRoundtripDTO();
    
    BpmnDiagramDTO rhs = new BpmnDiagramDTO();
    rhs.setDiagramPath("foo/bar");
    rhs.setModeler("Fox modeler");
    
    BpmnDiagramDTO lhs = new BpmnDiagramDTO();
    lhs.setDiagramPath("foo/foo");
    lhs.setModeler("Another Modeler");
    
    dto.setRightHandSide(rhs);
    dto.setLeftHandSide(lhs);
    
    return dto;
  }
}

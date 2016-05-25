package edu.usm.it.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.MemberInfo;
import edu.usm.dto.PredicateDto;
import edu.usm.dto.QueryDto;
import edu.usm.service.ContactService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ContactQueryControllerTest extends WebAppConfigurationAware {

    @Autowired
    ContactService contactService;

    @Before
    public void setup() throws Exception {
        Contact contact = new Contact("first", "last", "email@email.com", "1224567890");
        contact.setAssessment(5);
        contactService.create(contact);

        Contact contact1 = new Contact("joe", "hill", "email@yahoo.com", "1234567891");
        contact1.setAssessment(4);
        contactService.create(contact1);

        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setStatus(1);
        memberInfo.setAttendedOrientation(true);
        memberInfo.setPaidDues(true);
        memberInfo.setSignedAgreement(true);

        contactService.updateMemberInfo(contact1.getId(), memberInfo);
    }

    @After
    public void teardown() {
        contactService.deleteAll();
    }

    @Test
    public void testStringPredicateEquals() throws Exception {
        QueryDto queryDto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("firstName", "eq", "first"));
        queryDto.setPredicateDtos(predicateDtos);

        String json = new ObjectMapper().writeValueAsString(queryDto);

        mockMvc.perform(post("/contacts/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("first")));
    }

    @Test
    public void testStringPredicateNotEquals() throws Exception {
        QueryDto queryDto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("firstName", "neq", "first"));
        queryDto.setPredicateDtos(predicateDtos);

        String json = new ObjectMapper().writeValueAsString(queryDto);

        mockMvc.perform(post("/contacts/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("joe")));
    }

    @Test
    public void testStringPredicateBadOperator() throws Exception {
        QueryDto queryDto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("firstName", "bob", "first"));
        queryDto.setPredicateDtos(predicateDtos);

        String json = new ObjectMapper().writeValueAsString(queryDto);

        mockMvc.perform(post("/contacts/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testStringNonExistantField() throws Exception {
        QueryDto queryDto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("bob", "eq", "first"));
        queryDto.setPredicateDtos(predicateDtos);

        String json = new ObjectMapper().writeValueAsString(queryDto);

        mockMvc.perform(post("/contacts/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testIntegerPredicateLessThan() throws Exception {
        QueryDto queryDto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("assessment", "lt", "5"));
        queryDto.setPredicateDtos(predicateDtos);

        String json = new ObjectMapper().writeValueAsString(queryDto);

        mockMvc.perform(post("/contacts/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("joe")))
                .andExpect(jsonPath("$[0].assessment", is(4)));

    }

    @Test
    public void testIntegerPredicateEquals() throws Exception {
        QueryDto queryDto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("assessment", "eq", "5"));
        queryDto.setPredicateDtos(predicateDtos);

        String json = new ObjectMapper().writeValueAsString(queryDto);

        mockMvc.perform(post("/contacts/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("first")))
                .andExpect(jsonPath("$[0].assessment", is(5)));

    }

    @Test
    public void testIntegerPredicateGreaterThan() throws Exception {
        QueryDto queryDto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("assessment", "gt", "4"));
        queryDto.setPredicateDtos(predicateDtos);

        String json = new ObjectMapper().writeValueAsString(queryDto);

        mockMvc.perform(post("/contacts/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("first")))
                .andExpect(jsonPath("$[0].assessment", is(5)));

    }


    @Test
    public void testIntegerPredicateNonNumeric() throws Exception {
        QueryDto queryDto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("assessment", "eq", "bob"));
        queryDto.setPredicateDtos(predicateDtos);

        String json = new ObjectMapper().writeValueAsString(queryDto);

        mockMvc.perform(post("/contacts/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void testBooleanPredicateTrue() throws Exception {
        QueryDto queryDto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("member", "eq", "true"));
        queryDto.setPredicateDtos(predicateDtos);

        String json = new ObjectMapper().writeValueAsString(queryDto);

        mockMvc.perform(post("/contacts/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("joe")))
                .andExpect(jsonPath("$[0].member", is(true)));

    }

    @Test
    public void testBooleanPredicateFalse() throws Exception {
        QueryDto queryDto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("member", "eq", "false"));
        queryDto.setPredicateDtos(predicateDtos);

        String json = new ObjectMapper().writeValueAsString(queryDto);

        mockMvc.perform(post("/contacts/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("first")))
                .andExpect(jsonPath("$[0].member", is(false)));
    }

    @Test
    public void testBooleanPredicateNonBooleanValue() throws Exception {
        QueryDto queryDto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("member", "eq", "bob"));
        queryDto.setPredicateDtos(predicateDtos);

        String json = new ObjectMapper().writeValueAsString(queryDto);

        mockMvc.perform(post("/contacts/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testBooleanPredicateBadOperator() throws Exception {
        QueryDto queryDto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("member", "lt", "false"));
        queryDto.setPredicateDtos(predicateDtos);

        String json = new ObjectMapper().writeValueAsString(queryDto);

        mockMvc.perform(post("/contacts/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is4xxClientError());
    }
}

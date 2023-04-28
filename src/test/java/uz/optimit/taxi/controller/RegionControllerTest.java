package uz.optimit.taxi.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import uz.optimit.taxi.model.request.RegionRegisterRequestDto;
import uz.optimit.taxi.service.BaseTestConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class RegionControllerTest extends BaseTestConfiguration {

    @AfterEach
    void tearDown() {
        regionRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addRegion() throws Exception {
        add().andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void addRegionThrows() throws Exception {
        add();
        add().andExpect(status().isAlreadyReported());
    }

    private ResultActions add() throws Exception {
        final MockHttpServletRequestBuilder request =
                post("/api/v1/region/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                new RegionRegisterRequestDto("Tashkent")));
        return mockMvc.perform(request);
    }

}
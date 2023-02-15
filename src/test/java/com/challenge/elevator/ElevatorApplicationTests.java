package com.challenge.elevator;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class ElevatorApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
    }

    @Test
    void elevatorAddsSuccessfully() throws Exception {
        mockMvc.perform(post("/elevator/add-elevator"))
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Elevator Added Successfully"))
                .andExpect(
                        status().isOk());
    }
    @Test
    void elevatorCalledSuccessfully() throws Exception {
        mockMvc.perform(post("/elevator/add-elevator"));
        mockMvc.perform(post("/elevator/call")
                        .param("destinationFloor","2")
                        .param("elevatorID","1"))
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Elevator Called Successfully"))
                .andExpect(
                        status().isOk());
    }

    @Test
    void getStatusSuccessfully() throws Exception {
        mockMvc.perform(post("/elevator/add-elevator"));
        mockMvc.perform(post("/elevator/call")
                        .param("destinationFloor","2")
                        .param("elevatorID","1"));
        mockMvc.perform(get("/elevator/status"))
                .andDo(print())
                .andExpect(jsonPath("$[0].currentFloor").value("2"))
                .andExpect(
                        status().isOk());
    }

}

package ru.alexdern.spring.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.alexdern.spring.audit.controller.EventController;
import ru.alexdern.spring.audit.domain.Event;
import ru.alexdern.spring.audit.service.EventService;
import ru.alexdern.spring.audit.service.UserService;

import java.util.Arrays;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EventController.class, secure = false)
@RunWith(SpringRunner.class)
public class EventControllerTest {

    @MockBean
    EventService events;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;


    @Before
    public void setUp() {
        given(this.events.findAll())
            .willReturn(Arrays.asList(Event.builder().eventType("test").build()));

        given(this.events.findById(2L))
                .willReturn(Event.builder().eventType("test").build());

    }


    @Test
    public void testGetAll() throws Exception {

        this.mockMvc
                .perform(
                        get("/api/events", 2L)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..eventType").value("test"));

        verify(this.events, times(1)).findAll();
        verifyNoMoreInteractions(this.events);
    }


    @Test
    public void testGetById() throws Exception {

        this.mockMvc
                .perform(
                        get("/api/events/{id}", 2L)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventType").value("test"));

        verify(this.events, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(this.events);
    }


}

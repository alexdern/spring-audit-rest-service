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
import ru.alexdern.spring.audit.controller.AuditController;
import ru.alexdern.spring.audit.domain.Event;
import ru.alexdern.spring.audit.domain.User;
import ru.alexdern.spring.audit.repository.EventRepository;
import ru.alexdern.spring.audit.service.EventService;
import ru.alexdern.spring.audit.service.UserService;

import java.util.Arrays;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = AuditController.class, secure = false)
@RunWith(SpringRunner.class)
public class AuditControllerTest {

    @MockBean
    EventService events;

    @MockBean
    UserService users;

    @MockBean
    EventRepository eventRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;


    @Before
    public void setUp() {

        Event once = Event.builder().eventType("LOGIN").build();
        once.setEventTimestamp("2019-01-01T11:45:05.123Z[GMT]");
        once.setUser(User.builder().username("TESTER").build());

        given(this.events.findById(1L))
                .willReturn(once);

        given(this.events.findAll())
                .willReturn(Arrays.asList(once));

        given(this.events.create(any(Event.class)))
                .willReturn(once);

        //doNothing().when(this.events).delete(any(Event.class).getId());
    }


    @Test
    public void testJournal() throws Exception {

        this.mockMvc
                .perform(
                        get("/api/audit/journal")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..event_type").value("LOGIN"));

        verify(this.events, times(1)).findAll();
        verifyNoMoreInteractions(this.events);
    }


}

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
import ru.alexdern.spring.audit.controller.UserController;
import ru.alexdern.spring.audit.domain.Event;
import ru.alexdern.spring.audit.domain.User;
import ru.alexdern.spring.audit.service.EventService;
import ru.alexdern.spring.audit.service.UserService;

import java.util.Arrays;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class, secure = false)
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @MockBean
    UserService users;

    @MockBean
    EventService events;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;


    @Before
    public void setUp() {

        given(this.events.findAllByUserEID(1L))
                .willReturn(Arrays.asList(Event.builder().eventType("test").build()));

        given(this.events.findById(2L))
                .willReturn(Event.builder().eventType("test").build());

        given(this.users.findAll())
            .willReturn(Arrays.asList(User.builder().username("pavelfoo").build()));

        given(this.users.findById(2L))
                .willReturn(User.builder().username("pavelfoo").build());

    }


    @Test
    public void testGetAll() throws Exception {

        this.mockMvc
                .perform(
                        get("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..username").value("pavelfoo"));

        verify(this.users, times(1)).findAll();
        verifyNoMoreInteractions(this.users);
    }


    @Test
    public void testGetByUserEId() throws Exception {

        this.mockMvc
                .perform(
                        get("/api/users/{id}/events", 1L)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..eventType").value("test"));

        verify(this.events, times(1)).findAllByUserEID(any(Long.class));
        verifyNoMoreInteractions(this.events);
    }


}

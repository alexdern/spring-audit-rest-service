package ru.alexdern.spring.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ru.alexdern.spring.audit.dto.AuditEventDto;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext applicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails
    public void getJournal() throws Exception {
        this.mockMvc
                .perform(
                        get("/api/audit/journal")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }


    @Test
    public void testAuditDataSave() throws Exception {
        this.mockMvc
                .perform(
                        post("/api/audit/collector")
                                .content(this.objectMapper.writeValueAsBytes(AuditEventDto.builder().user_id(1L).company_id(1L).event_type("TEST").build()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }


    @Test
    @WithUserDetails
    public void testAuditDataSaveWithMock() throws Exception {

        AuditEventDto data = AuditEventDto.builder()
                .login("TEST").user_id(1L).company_id(1L)
                .event_type("TEST")
                .timestamp("2019-01-01T11:45:05.123Z[GMT]")
                .build();

        this.mockMvc
                .perform(
                        post("/api/audit/collector")
                                .content(this.objectMapper.writeValueAsBytes(data))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }


}

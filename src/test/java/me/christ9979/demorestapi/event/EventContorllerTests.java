package me.christ9979.demorestapi.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventContorllerTests {

    /**
     * MockMvc는 웹서버를 띄우지 않지만
     * Dispatcher 등 웹과 관련된 빈을 생성하기 때문에
     * 단위 테스트라고 할 수는 없고, 단위 테스트보다 조금 더 느리다.
     */
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createEvent() throws Exception {

        Event event = Event.builder()
                .name("Spring")
                .description("REST API Development")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 01, 17, 8, 43, 22))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 01, 18, 8, 43, 22))
                .beginEventDateTime(LocalDateTime.of(2019, 01, 19, 8, 43, 22))
                .endEventDateTime(LocalDateTime.of(2019, 01, 20, 8, 43, 22))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("구디역")
                .build();

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event))
                    )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists());

    }
}

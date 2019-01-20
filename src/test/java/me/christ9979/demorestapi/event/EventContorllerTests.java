package me.christ9979.demorestapi.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @MockBean
    EventRepository eventRepository;

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
        event.setId(10);

        /**
         * EventRepository는 Mock 객체이므로 모든 메소드에 대해 null을 반환한다.
         * 그러므로 아래 코드처럼 Stub을 지정한다.
         * Stub : Mock 객체의 행동을 지정한다.
         */
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event))
                    )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/hal+json;charset=UTF-8"));

    }
}

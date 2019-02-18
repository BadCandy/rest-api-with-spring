package me.christ9979.demorestapi.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import me.christ9979.demorestapi.accounts.Account;
import me.christ9979.demorestapi.accounts.AccountSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
/**
 * 연관관계를 가진 필드를 of에 두지말 것.
 * 같은 이유로 @Data란 어노테이션을 쓰지말 것.
 */
@EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;

    /**
     * Spring Boot 2.1부터 JPA 3.2를 지원하므로
     * LocalDateTime도 매핑이 기본적으로 된다.
     */
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    @ManyToOne
    /**
     * manager를 serialize할때는 모든 Account 정보가 필요하지 않고
     * 노출시 보안 문제 때문에
     * 커스텀하게 구현한 Serializer를 이용하여 serialize한다.
     */
    @JsonSerialize(using = AccountSerializer.class)
    private Account manager;
    public void main() {

    }

    public void update() {
        // Update free
        if (basePrice == 0 && maxPrice == 0) {
            free = true;
        } else {
            free = false;
        }

        // Update offline
        if (location == null || location.isBlank()) {
            offline = false;
        } else {
            offline = true;
        }
    }
}

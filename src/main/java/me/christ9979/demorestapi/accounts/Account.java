package me.christ9979.demorestapi.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Integer id;
    private String email;
    private String password;

    /**
     * @ElementCollection : 여러개의 Enum을 사용한다.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;

}

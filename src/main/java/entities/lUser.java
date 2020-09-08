package entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class lUser {

    private int id;
    private String username;
    private String password;
    private String name;
    private String lastname;
    private String email;
    private String personalCode;

}

// is the same as

//@ToString
//@EqualsAndHashCode
//@AllArgsConstructor
//public class lUser {
//
//    @Getter
//    @Setter private String username;
//    @Getter @Setter private String password;
//    @Getter @Setter private String name;
//    @Getter @Setter private String lastname;
//    @Getter @Setter private String email;
//    @Getter @Setter private String personalCode;
//
//}
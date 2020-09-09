package entities;

import lombok.*;

@Builder
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Property {

    private User user;
    private int id;
    private String ownerPersonalCode;
    private String type;
    private String address;

}

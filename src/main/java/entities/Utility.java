package entities;

import lombok.*;

@Builder
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Utility {

    private int utilityId;
    private String utilityName;

}

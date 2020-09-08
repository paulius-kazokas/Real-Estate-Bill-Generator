package entities;

import lombok.*;

@Builder
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class lIndicator {

    private lProperty property;
    private Utility utility;
    private int id;
    private int utilityId;
    private int propertyId;
    private String date;
    private int monthStartAmount;
    private int monthEndAmount;
}

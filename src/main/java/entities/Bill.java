package entities;

import lombok.*;

@Builder
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Bill {

    private int id;
    private int indicatorId;
    private Price price;

}

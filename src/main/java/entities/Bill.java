package entities;

import lombok.*;

@Builder
@Data
@ToString
public class Bill {

    private int id;
    private int indicatorId;
    private Price price;

}

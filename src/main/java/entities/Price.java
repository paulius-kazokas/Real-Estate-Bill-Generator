package entities;

import lombok.*;

@Builder
@Data
@ToString
public class Price {

    private int id;
    private String currency;
    private float course;

}

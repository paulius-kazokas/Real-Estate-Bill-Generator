package playground;

import lombok.*;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

/*
    Annotations:

        objectWithConstructorAnno()
            @Getter
            @Setter
            @AllArgsConstructor
            @NoArgsConstructor
            @ToString(exclude = "id", includeFieldNames = false)

        objectWitBuilderAnno()
            @Getter
            @Setter
            @ToString(exclude = "id", includeFieldNames = false)
            @Builder

        objectWithDataAnno()
            @Data
            Note: can be comined with @AllArgsConstructor and @Builder

        objectWithValAndVarFields()
            @Data

        objectWithNotNullValAndVarFields()
            @Data

        cleanUpLogic()
            @Data

        reqConstructor()
            @Data
            @RequiredArgsConstructor(staticName = "req")
            @Builder

        everyConstructor()
            in the code
            +
            @ToString(includeFieldNames = false)

        valueAnno()
            @Value
            @RequiredArgsConstructor
            @AllArgsConstructor     // without this, @NonFinal fields are not visible because of @RequiredArgsConstructor
            @Setter                 // only fields with @NonFinal can be set


        sneakyThrowsExample()
            @NoArgsConstructor

 */



@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Slf4j
public class Alien {

    @NonNull String name;
    Integer retro;

    Integer id;
    @NonFinal Double weight; // annotation allows to change the field value
    Double height;
    String race;
    Boolean friendlyToHumans;

    public String roar() {
        val beginning = "Roooaarrrr Im a "; // final String
        var roarBeingCreated = beginning; // compiled to String
        roarBeingCreated += name;
        roarBeingCreated += " and my race is " + race;
        return roarBeingCreated;
    }

    // if value is null then it will inform which value is null instead in which line the null appears
    // makes fail fast INSTEAD go through lines and when null appears then throw NPE
    public String greetings(@NonNull Integer volume) {
        val beginning = "Greatings, ";
        var greetingsBeingCreated = beginning;
        greetingsBeingCreated += "I weight " + weight + " times " + volume + ". Thank you";
        return greetingsBeingCreated;
    }

    @SneakyThrows
    // in the situations where you know the exceptions wont appear OR to dismiss try catch blocks and convert exception into Runtime exception
    public void alienSleep() {
        log.info("1");
        Thread.sleep(1000);
        log.info("3");
    }

}

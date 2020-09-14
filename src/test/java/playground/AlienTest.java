package playground;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class AlienTest {

    /*@Test
    void objectWithConstructorAnno() {
         when @AllArgsConstructor and @NoArgsConstructor
         Alien alien = new Alien(1, 10.1d, 3.2d, "real_alien", "wookies", true);

        System.out.println("This is the alien: " + alien);

        assertEquals(10.1d, alien.getWeight());
        alien.setFriendlyToHumans(false);
        assertFalse(alien.isFriendlyToHumans());
    }*/

    /*@Test
    void objectWitBuilderAnno() {
        Alien alien = Alien.builder()
                .friendlyToHumans(true)
                .height(12.1d)
                .weight(14.52d)
                .id(1)
                .name("Rudolph")
                .race("rudolphians")
                .build();

        log.info("Lombok race alien: " + alien); // alien.toString() is the same

        assertEquals("Rudolph", alien.getName());
        alien.setId(2);
        assertEquals(2, alien.getId());
    }*/

    /*@Test
    void objectWithDataAnno() {
        Alien alien1 = Alien.builder()
                .friendlyToHumans(true)
                .height(12.1d)
                .weight(14.52d)
                .id(1)
                .name("Rudolph")
                .race("rudolphians")
                .build();

        Alien alien2 = Alien.builder()
                .friendlyToHumans(true)
                .height(12.1d)
                .weight(14.52d)
                .id(1)
                .name("ne Rudolph")
                .race("rudolphians")
                .build();

        Alien alien3 = Alien.builder()
                .friendlyToHumans(true)
                .height(12.1d)
                .weight(14.52d)
                .id(1)
                .name("Rudolph")
                .race("rudolphians")
                .build();

        assertNotSame(alien1, alien2); // name differs
        assertEquals(alien1, alien3);

    }*/

    // String roar()
    /*@Test
    void objectWithValAndVarFields() {

        Alien alien1 = Alien.builder()
                .friendlyToHumans(true)
                .height(12.1d)
                .weight(14.52d)
                .id(1)
                .name("Rudolph")
                .race("rudolphians")
                .build();

        Alien alien2 = Alien.builder()
                .friendlyToHumans(true)
                .height(12.1d)
                .weight(14.52d)
                .id(1)
                .name("ne Rudolph")
                .race("net ne rudolphians")
                .build();

        log.warn(alien1.roar());
        log.warn(alien2.roar());

    }*/

    // String greetings
    /*@Test
    void objectWithNotNullValAndVarFields() {

        Alien alien1 = Alien.builder()
                .friendlyToHumans(true)
                .height(12.1d)
                .weight(14.52d)
                .id(1)
                .name("Rudolph")
                .race("rudolphians")
                .build();

        log.warn(alien1.greetings(null));

    }*/


    // cleanup
    /*@Test
    void cleanUpLogic() throws IOException {

        // without @Cleanup

        InputStream in = System.in;
        OutputStream out = System.out;

        String data = "123";

        try {
            in.read(data.getBytes());
            out.write(data.getBytes());
        } finally {
            in.close();
            out.close();
        }


        // with @Cleanup

        @Cleanup InputStream lin = System.in;
        @Cleanup OutputStream lout = System.out;

        lin.read(data.getBytes());
        lout.write(data.getBytes());

        // lombok will close Streams automatically

    }*/

    // @RequiredArgsConstructor
    // @AllArgsConstructor
    // @NoArgsConstructor

/*
    @Test
    void everyConstructor() {

    /*
        @RequiredArgsConstructor(staticName = "req") // marked fields required to create object
        @AllArgsConstructor(staticName = "all")      // all fields required to create object
        @NoArgsConstructor(staticName = "noArgs")    // no fields required to create object


                Alien reqAlien = Alien.req("s");
        Alien allAlien = Alien.all("Alien Bond", 12, 1, 12.3d, 13.2d, "aliens duh", false);
        Alien emptyAlien = Alien.noArgs();
        // can be built too with a builder
    */

    /*
        ---
        @RequiredArgsConstructor
        @AllArgsConstructor
        @NoArgsConstructor


        Alien emptyaAlien = new Alien();
        Alien reqAlien = new Alien("Jacob");
        Alien allAlien = new Alien("Alien Bond", 12, 1, 12.3d, 13.2d, "aliens duh", false);


        log.info("\n" + emptyaAlien);
        log.info("\n" + reqAlien);
        log.info("\n" + allAlien);
      }
     */

/*
    @Test
    void valueAnno() {
        Alien alien = new Alien("name", 2, 2, 12.3d, "race", false);
        alien.setWeight(14.5d);
    }
*/

    @Test
    void sneakyThrowsExample() {
        Alien alien = new Alien();
        alien.alienSleep();
    }

}
package interfaces;

import entities.User;

import java.util.Map;

public interface PropertyInterface {

    Map<String, String> getUserProperties(User user);

}

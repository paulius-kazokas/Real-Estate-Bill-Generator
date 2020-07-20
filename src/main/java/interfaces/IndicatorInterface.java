package interfaces;

import entities.Property;

import java.util.List;

public interface IndicatorInterface {

    List<Property> getUserProperties(String username);
}

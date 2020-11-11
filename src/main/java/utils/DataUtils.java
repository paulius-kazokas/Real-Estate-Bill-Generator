package utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataUtils {

    public static Map<Integer, String> elementToMap(List<String> elementList) {

        Map<Integer, String> elementMap = IntStream.range(0, elementList.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(),
                        elementList::get,
                        (k, v) -> k, LinkedHashMap::new
                ));

        elementMap.forEach((k, v) -> System.out.println(String.format("%s. %s", k, v)));

        return elementMap;
    }
}

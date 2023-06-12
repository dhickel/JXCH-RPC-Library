import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.mindspice.util.JsonUtils;
import io.mindspice.util.StringListDeserializer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DeserializerTests {

    public record testMultiStringArr(
            @JsonDeserialize(using = StringListDeserializer.class)
            List<String> arr
    ){}


    @Test
    void testDeserializer() throws IOException {
        String json1 = "{\"arr\":[\"string\"]}";
        String json2 = "{\"arr\":[[\"String\", 3, \"string\"], \"string\"]}";
        String json3 = "{\"arr\":[[\"a\", 1, \"b\"], [2, \"c\", 3], \"d\"]}";
        String json4 = "{\"arr\":[[\"a\", 1, \"b\"], [2, \"c\", 3], \"string\"]}";

        testMultiStringArr test1 = JsonUtils.readJson(json1, testMultiStringArr.class);
        testMultiStringArr test2 = JsonUtils.readJson(json2, testMultiStringArr.class);
        testMultiStringArr test3 = JsonUtils.readJson(json3, testMultiStringArr.class);
        testMultiStringArr test4 = JsonUtils.readJson(json4, testMultiStringArr.class);

        List<String> expectedList1 = List.of("string");
        List<String> expectedList2 = Arrays.asList("String", "3", "string", "string");
        List<String> expectedList3 = Arrays.asList("a", "1", "b", "2", "c", "3", "d");
        List<String> expectedList4 = Arrays.asList("a", "1", "b", "2", "c", "3", "string");

        assertEquals(expectedList1, test1.arr());
        assertEquals(expectedList2, test2.arr());
        assertEquals(expectedList3, test3.arr());
        assertEquals(expectedList4, test4.arr());
    }
}

package cristina.tech.blog.travel.domain;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * To test the JSON slice of the Wanderlust application, as of testing that JSON serialization and deserialization is working as expected,
 * I will use @JsonTest.
 * <ul>This will:
 * <li>Auto-configure Jackson and/or Gson</li>
 * <li>Add any Module or @JsonComponent beans that you’ve defined</li>
 * <li>Trigger initialization of any JacksonTester or GsonTester fields</li>
 * </ul>
 *
 * @SpringRunner tells JUnit to run using Spring’s testing support. SpringRunner is the new name for SpringJUnit4ClassRunner
 */
@RunWith(SpringRunner.class)
@JsonTest
public class HolidayJsonTests {

    @Autowired
    private JacksonTester<Holiday> json;

    @Test
    public void testSerialize() throws Exception {
        Holiday holiday = new Holiday(null, true, true);
        holiday.setDaysCount(15);
        holiday.setPrice(new BigDecimal(1700));
        holiday.setStartOn(LocalDateTime.now().parse("2017-03-17 11:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        holiday.setPackageInfo("Group Travel 'On a shoe string'");
        holiday.setDepartFrom("Amsterdam Airport");

        // Assert serialized holiday object instance against a `.json` file in the same package as the test
        assertThat(json.write(holiday)).isEqualToJson("holiday_no_destination_link.json");
    }

    @Test
    public void testDeserialize() throws Exception {
        // deserialize
        Holiday holiday = json.readObject("holiday_no_destination_link.json");

        assertThat(holiday).isNotNull();
        assertThat(holiday.getDaysCount()).isEqualTo(15);
        assertThat(holiday.getAccomodationIncluded()).isEqualTo(true);
        assertThat(holiday.getFlightIncluded()).isEqualTo(true);
        assertThat(holiday.getDepartFrom()).isEqualTo("Amsterdam Airport");
        assertThat(holiday.getPackageInfo()).isEqualTo("Group Travel 'On a shoe string'");
        assertThat(holiday.getStartOn()).isGreaterThan(LocalDateTime.now()); // assert start date in the future
        assertThat(holiday.getPrice()).isEqualTo(new BigDecimal(1700));
    }
}

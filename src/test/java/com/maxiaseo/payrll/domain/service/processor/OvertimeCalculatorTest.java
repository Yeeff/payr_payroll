package com.maxiaseo.payrll.domain.service.processor;

import com.maxiaseo.payrll.domain.model.Overtime;
import com.maxiaseo.payrll.domain.util.ConstantsDomain;
import com.maxiaseo.payrll.domain.util.ConstantsDomain.OvertimeTypeEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OvertimeCalculatorTest {

    @ParameterizedTest
    @CsvSource({
            "2024-09-20T07:00, 2024-09-20T16:00, 60, 7736.41 ",
            "2024-09-20T07:00, 2024-09-20T16:30, 90, 11604.62",
            "2024-09-20T07:00, 2024-09-20T18:00, 180, 23209.23",
    })
    void testDayOvertime(String startStr, String endStr, long expectedQuantity, String expectedMoneyValue) {

        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end, ConstantsDomain.MAXIMUM_HOURS_PER_DAY, "1423500");

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.DAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Day overtime not found"));

        assertEquals(1, result.size());
        assertEquals(OvertimeTypeEnum.DAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(expectedQuantity, dayOvertime.getQuantityOfMinutes());
        assertEquals(expectedMoneyValue, dayOvertime.getMoneyCost().toString() );
    }

    @ParameterizedTest
    @CsvSource({
            "2024-09-19T14:00, 2024-09-19T23:00, 60, 10830.98",
            "2024-09-19T14:00, 2024-09-20T02:00, 240, 43323.92"
    })
    void testNightOverTime(String startStr, String endStr, Long expectedQuantity, String expectedMoneyValue) {
            LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end, ConstantsDomain.MAXIMUM_HOURS_PER_DAY,  "1423500");

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night overtime not found"));

        assertEquals(1, result.size());

        assertEquals(OvertimeTypeEnum.NIGHT, dayOvertime.getOvertimeTypeEnum());
        assertEquals(expectedQuantity, dayOvertime.getQuantityOfMinutes());
        assertEquals(expectedMoneyValue, dayOvertime.getMoneyCost().toString());
    }

    @ParameterizedTest
    @CsvSource({
            "2024-09-22T08:00, 2024-09-22T17:00, 60, 12378.26",
            "2024-09-22T08:00, 2024-09-22T19:00, 180, 37134.78"
    })
    void testHolidayOverTime(String startStr, String endStr, Long expectedQuantity, String expectedMoneyValue) {
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end, ConstantsDomain.MAXIMUM_HOURS_PER_DAY, "1423500");

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Holiday overtime not found"));

        assertEquals(1, result.size());

        assertEquals(OvertimeTypeEnum.HOLIDAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(expectedQuantity, dayOvertime.getQuantityOfMinutes());
        assertEquals(expectedMoneyValue, dayOvertime.getMoneyCost().toString());
    }

    @ParameterizedTest
    @CsvSource({
            "2024-09-21T18:00, 2024-09-22T03:00, 60, 15472.83",
            "2024-09-21T18:00, 2024-09-22T05:00, 180, 46418.49"
    })
    void testHolidayNightOverTime(String startStr, String endStr, Long expectedQuantity, String expectedMoneyValue) {
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end, ConstantsDomain.MAXIMUM_HOURS_PER_DAY, "1423500");

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.NIGHT_HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night Holiday overtime not found"));

        assertEquals(1, result.size());

        assertEquals(OvertimeTypeEnum.NIGHT_HOLIDAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(expectedQuantity, dayOvertime.getQuantityOfMinutes());
        assertEquals(expectedMoneyValue, dayOvertime.getMoneyCost().toString());
    }

    @ParameterizedTest
    @CsvSource({
            "2024-09-21T08:00, 2024-09-22T23:00, 300, 180, 900, 480",
            "2024-09-21T06:00, 2024-09-22T22:00, 420, 180, 900, 420"
    })
    void testMixedOvertime(String startStr, String endStr, Long dayValue, Long nightValue, Long holidayValue, Long nightHolidayValue) {
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end, ConstantsDomain.MAXIMUM_HOURS_PER_DAY, "1423500");

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.DAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        Overtime nightOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        Overtime holidayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Holiday surcharge not found"));

        Overtime nightHolidayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.NIGHT_HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night-Holiday surcharge not found"));

        Long dayQuantityHours = dayOvertime.getQuantityOfMinutes();
        Long nightQuantityHours = nightOvertime.getQuantityOfMinutes();
        Long holidayQuantityHours = holidayOvertime.getQuantityOfMinutes();
        Long nightHolidayQuantityHours = nightHolidayOvertime.getQuantityOfMinutes();

        assertEquals(OvertimeTypeEnum.DAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(dayValue, dayQuantityHours);

        assertEquals(OvertimeTypeEnum.NIGHT, nightOvertime.getOvertimeTypeEnum());
        assertEquals(nightValue, nightQuantityHours);

        assertEquals(OvertimeTypeEnum.HOLIDAY, holidayOvertime.getOvertimeTypeEnum());
        assertEquals(holidayValue, holidayQuantityHours);  // 10 hours (Monday day)

        assertEquals(OvertimeTypeEnum.NIGHT_HOLIDAY, nightHolidayOvertime.getOvertimeTypeEnum());
        assertEquals(nightHolidayValue, nightHolidayQuantityHours);  // 2 hours (Monday night)
    }

}
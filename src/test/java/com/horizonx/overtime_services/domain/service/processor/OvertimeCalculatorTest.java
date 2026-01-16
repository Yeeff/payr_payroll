package com.horizonx.overtime_services.domain.service.processor;

import com.horizonx.overtime_services.domain.model.Overtime;
import com.horizonx.overtime_services.domain.util.ConstantsDomain;
import com.horizonx.overtime_services.domain.util.ConstantsDomain.OvertimeTypeEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OvertimeCalculatorTest {

    @ParameterizedTest
    @CsvSource({
            "2024-09-20T07:00, 2024-09-20T16:00, 60",
            "2024-09-20T07:00, 2024-09-20T16:30, 90",
            "2024-09-20T07:00, 2024-09-20T18:00, 180",
    })
    void testDayOvertime(String startStr, String endStr, long expectedMinutes) {
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end, ConstantsDomain.MAXIMUM_HOURS_PER_DAY);

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.DAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Day overtime not found"));

        assertEquals(1, result.size());
        assertEquals(OvertimeTypeEnum.DAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(expectedMinutes, dayOvertime.getQuantityOfMinutes());
    }

    @ParameterizedTest
    @CsvSource({
            "2024-09-19T14:00, 2024-09-19T23:00, 60",
            "2024-09-19T14:00, 2024-09-20T02:00, 240"
    })
    void testNightOverTime(String startStr, String endStr, Long expectedValue) {
            LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end, ConstantsDomain.MAXIMUM_HOURS_PER_DAY);

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night overtime not found"));

        assertEquals(1, result.size());

        assertEquals(OvertimeTypeEnum.NIGHT, dayOvertime.getOvertimeTypeEnum());
        assertEquals(expectedValue, dayOvertime.getQuantityOfMinutes());
    }

    @ParameterizedTest
    @CsvSource({
            "2024-09-22T08:00, 2024-09-22T17:00, 60",
            "2024-09-22T08:00, 2024-09-22T19:00, 180"
    })
    void testHolidayOverTime(String startStr, String endStr, Long expectedValue) {
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end, ConstantsDomain.MAXIMUM_HOURS_PER_DAY);

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Holiday overtime not found"));

        assertEquals(1, result.size());

        assertEquals(OvertimeTypeEnum.HOLIDAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(expectedValue, dayOvertime.getQuantityOfMinutes());
    }

    @ParameterizedTest
    @CsvSource({
            "2024-09-21T18:00, 2024-09-22T03:00, 60",
            "2024-09-21T18:00, 2024-09-22T05:00, 180"
    })
    void testHolidayNightOverTime(String startStr, String endStr, Long expectedValue) {
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end, ConstantsDomain.MAXIMUM_HOURS_PER_DAY);

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.NIGHT_HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night Holiday overtime not found"));

        assertEquals(1, result.size());

        assertEquals(OvertimeTypeEnum.NIGHT_HOLIDAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(expectedValue, dayOvertime.getQuantityOfMinutes());
    }

    @ParameterizedTest
    @CsvSource({
            "2024-09-21T08:00, 2024-09-22T23:00, 300, 180, 900, 480",
            "2024-09-21T06:00, 2024-09-22T22:00, 420, 180, 900, 420"
    })
    void testMixedOvertime(String startStr, String endStr, Long dayValue, Long nightValue, Long holidayValue, Long nightHolidayValue) {
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end, ConstantsDomain.MAXIMUM_HOURS_PER_DAY);

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
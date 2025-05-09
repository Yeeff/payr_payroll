package com.horizonx.overtime_services.domain.service.processor;

import com.horizonx.overtime_services.domain.model.Surcharge;
import com.horizonx.overtime_services.domain.util.ConstantsDomain;
import com.horizonx.overtime_services.domain.util.ConstantsDomain.SurchargeTypeEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SurchargeCalculatorTest {

    @ParameterizedTest
    @CsvSource({
            "2024-09-20T18:00, 2024-09-21T02:00, 300",
            "2024-09-20T18:00, 2024-09-21T01:30, 270",
            "2024-09-20T02:00, 2024-09-20T09:00, 240",
            "2024-09-20T18:00, 2024-09-21T16:00, 300"
    })
    void testNightSurcharge(String startStr, String endStr, Long expectedValue) {
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end, ConstantsDomain.MAXIMUM_HOURS_PER_DAY);

        Surcharge nightSurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        assertEquals(1, result.size());

        assertEquals(SurchargeTypeEnum.NIGHT, nightSurcharge.getSurchargeTypeEnum());
        assertEquals(expectedValue, nightSurcharge.getQuantityOfMinutes());
    }

    @ParameterizedTest
    @CsvSource({
            "2024-09-22T04:00, 2024-09-22T10:00, 240",
            "2024-09-22T16:00, 2024-09-22T23:00, 300",
            "2024-09-22T04:00, 2024-09-22T23:00, 360"
    })
    void testHolidaySurcharge(String startStr, String endStr, Long expectedValue) {
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end, ConstantsDomain.MAXIMUM_HOURS_PER_DAY);

        Surcharge holidaySurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        assertEquals(2, result.size());

        assertEquals(SurchargeTypeEnum.HOLIDAY, holidaySurcharge.getSurchargeTypeEnum());
        assertEquals(expectedValue, holidaySurcharge.getQuantityOfMinutes());  // 8 hours of holiday overtime
    }

    @ParameterizedTest
    @CsvSource({
            "2024-09-22T18:00, 2024-09-22T23:00, 120",
            "2024-09-22T03:00, 2024-09-22T10:00, 180"
    })
    void testNightHolidaySurcharge(String startStr, String endStr, Long expectedValue) {
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end, ConstantsDomain.MAXIMUM_HOURS_PER_DAY);

        Surcharge nightHolidaySurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT_HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        assertEquals(2, result.size());

        assertEquals(SurchargeTypeEnum.NIGHT_HOLIDAY, nightHolidaySurcharge.getSurchargeTypeEnum());
        assertEquals(expectedValue, nightHolidaySurcharge.getQuantityOfMinutes());  // 8 hours of night holiday overtime
    }

    @ParameterizedTest
    @CsvSource({
            "2024-09-15T18:00, 2024-09-16T02:00, 120, 180, 180",
            "2024-09-15T20:00, 2024-09-16T04:00, 240, 60, 180"
    })
    void testMixedSurcharges(String startStr, String endStr, Long nightValue, Long holidayValue, Long nightHolidayValue) {
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end, ConstantsDomain.MAXIMUM_HOURS_PER_DAY);

        Surcharge nightSurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        Surcharge holidaySurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Holiday surcharge not found"));

        Surcharge nightHolidaySurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT_HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night-Holiday surcharge not found"));

        Long nightQuantityHours = nightSurcharge.getQuantityOfMinutes();
        Long holidayQuantityHours = holidaySurcharge.getQuantityOfMinutes();
        Long nightHolidayQuantityHours = nightHolidaySurcharge.getQuantityOfMinutes();

        // Verify Night Surcharge
        assertEquals(SurchargeTypeEnum.NIGHT, nightSurcharge.getSurchargeTypeEnum());
        assertEquals(nightValue, nightQuantityHours);  // 2 hours (Sunday night)

        // Verify Holiday Surcharge
        assertEquals(SurchargeTypeEnum.HOLIDAY, holidaySurcharge.getSurchargeTypeEnum());
        assertEquals(holidayValue, holidayQuantityHours);  // 10 hours (Monday day)

        // Verify Night-Holiday Surcharge
        assertEquals(SurchargeTypeEnum.NIGHT_HOLIDAY, nightHolidaySurcharge.getSurchargeTypeEnum());
        assertEquals(nightHolidayValue, nightHolidayQuantityHours);  // 2 hours (Monday night)

    }

}
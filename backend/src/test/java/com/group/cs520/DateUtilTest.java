package com.group.cs520;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import com.group.cs520.service.DateUtil;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateUtilTest {
    @Test
    public void testDateFormatter_WithValidDateString() {
        String date = "2019-01-01";
        String pattern = "yyyy-MM-dd";
        LocalDate result = DateUtil.dateFormatter(date, pattern);
        assertEquals(result, LocalDate.of(2019, 1, 1));
    }
}

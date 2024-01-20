package proj.java.spring.service;


import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GeneralServiceTest {
    @Test
    public void testAuth() throws IOException, JSONException {
//        DateTimeFormatter bankSettleFormatter = DateTimeFormat.forPattern("yyyyMMdd");
//        String bankSettle = "20230821091143".substring(0,8);
//        LocalDateTime bankSettleDate = LocalDateTime.parse(bankSettle, bankSettleFormatter);
    }

}



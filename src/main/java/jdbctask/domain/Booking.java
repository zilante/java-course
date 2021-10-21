package jdbctask.domain;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class Booking {
    private final String bookRef;
    private final Timestamp bookDate;
    private final  double totalAmount;
}
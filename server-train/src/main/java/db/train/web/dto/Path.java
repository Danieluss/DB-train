package db.train.web.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Path {
    Long fromStation;
    Long toStation;
    LocalTime time;
    String date;
}

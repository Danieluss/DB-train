package db.train.web.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Path {
    private Long fromStation;
    private Long toStation;
    private LocalTime time;
    private String date;
}

package db.train.web.dto;

import db.train.persistence.model.Connection;
import db.train.persistence.model.Station;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationList {

    @Size(min = 2)
    private List<Station> stations;
    private Connection data;

}

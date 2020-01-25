package db.train.service;

import db.train.exception.CannotConnectException;
import db.train.persistence.model.Connection;
import db.train.persistence.model.Station;
import db.train.web.dto.StationList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.webrepogen.annotations.ServicePackage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShortestPathService {

    @AllArgsConstructor
    @Data
    private static class Node {

    }

    public Connection connect(StationList stationList) {
        List<Station> resultingConnection = new ArrayList<>();
        resultingConnection.add(stationList.getStations().get(0));
        for (int i = 0; i < stationList.getStations().size() - 1; i++) {
            resultingConnection
                    .addAll(
                            connect(
                                    stationList.getStations().get(i),
                                    stationList.getStations().get(i + 1)));
        }
        Connection connection = stationList.getData();
        connection.setStations(resultingConnection.stream().map(Station::getId).collect(Collectors.toList()));
        return connection;
    }


    public List<Station> connect(Station from, Station to) { // return path (from; to]
        //TODO
        throw new CannotConnectException(String.format("Could not find connection between %s and %s", from.getId(), to.getId()));
    }

}

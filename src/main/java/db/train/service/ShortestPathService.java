package db.train.service;

import db.train.exception.CannotConnectException;
import db.train.persistence.model.Connection;
import db.train.persistence.model.Edge;
import db.train.persistence.model.Station;
import db.train.web.dto.StationList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.webrepogen.annotations.ServicePackage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShortestPathService {

    @AllArgsConstructor
    @Data
    private static class Node implements Comparable<Node> {
        double distance;
        Station station;
        Station previousStation;
        @Override
        public int compareTo(Node node) {
            double diff = node.distance-distance;
            return diff > 0.1 ? 1 : (diff < -0.1 ? -1 : 0);
        }
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
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(0, from, null));
        Map<Station, Node> previousStation = new HashMap();
        while(!queue.isEmpty()) {
            Node a = queue.poll();
            if(previousStation.containsKey(a.getStation())) {
                continue;
            }
            previousStation.put(a.getStation(), a);
            if(a.getStation() == to) {
                break;
            }
            for(Edge e : a.getStation().getEdges()) {
                Hibernate.initialize(e);
                Hibernate.initialize(e.getStation2());
                queue.add(new Node(a.getDistance()+e.getDistance(), e.getStation2(), e.getStation1()));
            }
        }
        if(!previousStation.containsKey(to)) {
            throw new CannotConnectException(String.format("Could not find connection between %s and %s", from.getId(), to.getId()));
        }
        List<Station> result = new ArrayList<>();
        Station a = to;
        while(a != from) {
            result.add(a);
            a = previousStation.get(a).getPreviousStation();
        }
        Collections.reverse(result);
        return result;
    }

}

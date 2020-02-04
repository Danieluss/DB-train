package db.train.service;

import db.train.exception.CannotConnectException;
import db.train.persistence.model.Connection;
import db.train.persistence.model.Edge;
import db.train.persistence.model.Station;
import db.train.persistence.model.join.StationsConnections;
import db.train.web.dto.StationList;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShortestPathService {

    @AllArgsConstructor
    @Data
    private static class Node implements Comparable<Node> {
        private double distance;
        private Station station;
        private Station previousStation;
        @Override
        public int compareTo(Node node) {
            double diff = node.distance-distance;
            return diff > 0.001 ? -1 : (diff < -0.001 ? 1 : 0);
        }
    }

    public List<StationsConnections> connect(List<Station> stationList) {
        List<StationsConnections> resultingConnection = new ArrayList<>();
        StationsConnections sc = new StationsConnections(new Long(0), 0, true, LocalTime.MIN, LocalTime.MIN, stationList.get(0), new Connection());
        sc.getConnection().setId(new Long(0));
        resultingConnection.add(sc);
        for (int i = 0; i < stationList.size() - 1; i++) {
            resultingConnection.addAll(connect(stationList.get(i), stationList.get(i+1), resultingConnection.get(resultingConnection.size()-1)));
        }
        return resultingConnection;
    }


    public List<StationsConnections> connect(Station from, Station to, StationsConnections start) { // return path (from; to]
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(0, from, null));
        Map<Long, Node> previousStation = new HashMap();
        while(!queue.isEmpty()) {
            Node a = queue.poll();
            if(previousStation.containsKey(a.getStation().getId())) {
                continue;
            }
            previousStation.put(a.getStation().getId(), a);
            if(a.getStation().getId().equals(to.getId())) {
                break;
            }
            for(Edge e : a.getStation().getEdges()) {
                Hibernate.initialize(e);
                Hibernate.initialize(e.getStation2());
                queue.add(new Node(a.getDistance()+e.getDistance(), e.getStation2(), e.getStation1()));
            }
        }
        if(!previousStation.containsKey(to.getId())) {
            throw new CannotConnectException(String.format("Could not find connection between %s and %s", from.getId(), to.getId()));
        }
        List<Station> stations = new ArrayList<>();
        List<Double> distances = new ArrayList<>();
        Station a = to;
        while(!a.getId().equals(from.getId())) {
            Node b = previousStation.get(a.getId());
            stations.add(a);
            distances.add(b.getDistance());
            a = b.getPreviousStation();
        }
        Collections.reverse(distances);
        Collections.reverse(stations);
        List<StationsConnections> result = new ArrayList<>();
        for(int i=0; i < stations.size(); i++) {
            StationsConnections prev = start;
            Long d = distances.get(i).longValue();
            if(i > 0) {
                prev = result.get(result.size() - 1);
                d = new Double(distances.get(i)-distances.get(i-1)).longValue();
            }
            Long t =(3*d)/5;
            result.add(new StationsConnections(
                    new Long(0), prev.getNumber()+1, true,
                    prev.getDeparture().plusMinutes(t),
                    prev.getDeparture().plusMinutes(t+1),
                    stations.get(i), new Connection()
            ));
            result.get(result.size()-1).getConnection().setId(new Long(0));
        }
        return result;
    }

}

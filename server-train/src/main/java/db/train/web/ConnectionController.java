package db.train.web;

import db.train.persistence.model.Connection;
import db.train.persistence.model.Station;
import db.train.persistence.model.join.StationsConnections;
import db.train.repository.StationRepository;
import db.train.service.ShortestPathService;
import db.train.web.dto.StationList;
import db.train.web.dto.Path;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.webrepogen.ICRUDRepository;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.sql.Time;

@RestController
@RequestMapping("/api/connection")
public class ConnectionController extends AbstractWebController<Connection, Long> {

	private final ShortestPathService service;
	private final ICRUDRepository<Station, Long> stationRepository;
	@Autowired
    private EntityManager entityManager;

	@Autowired
	public ConnectionController(final ICRUDRepository<Connection, Long> repository, final ShortestPathService service, final ICRUDRepository<Station, Long> stationRepository) {
		init(repository, Connection.class, Long.class);
		this.service = service;
		this.stationRepository = stationRepository;
	}

	@RequestMapping(value = "/generate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<StationsConnections> generate(@RequestBody @Valid final StationList stationList) {
		final List<Station> fetchedStations = new ArrayList<>();
		for (int i = 0; i < stationList.getStations().size(); i++) {
			fetchedStations.add(
					stationRepository
							.findById(stationList.getStations().get(i))
							.orElse(null));
		}
		return service.connect(fetchedStations);
	}

	@Transactional
	@RequestMapping(value = "/findconnection", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Object> findConnection(@RequestBody @Valid final Path path) {
		final Session session = entityManager.unwrap(Session.class);
		final Query query = session.createSQLQuery(
			"select ca.departure as departure, cb.arrival as arrival, ca.id as sc1Id, cb.id as sc2Id, c.id as connectionId " +
			"from stations_connections ca, stations_connections cb, connection c " +
			"where ca.connection_id = cb.connection_id " +
			"and ca.number < cb.number " +
			"and ca.station_id = :fromStation " +
			"and cb.station_id = :toStation " +
			"and c.id = ca.connection_id " +
			"and c.first_day <= :date and :date <= c.last_day " +
			"order by mod(cast(extract(epoch from (ca.departure-:time)) as int)+24*60*60, 24*60*60) " +
			"limit 6"
		).addScalar("departure", new StringType())
		.addScalar("arrival", new StringType())
		.addScalar("sc1Id", new LongType())
		.addScalar("sc2Id", new LongType())
		.addScalar("connectionId", new LongType());

		return query.setParameter("fromStation", path.getFromStation())
			.setParameter("toStation", path.getToStation())
			.setParameter("date", java.sql.Date.valueOf(path.getDate()))
			.setParameter("time", java.sql.Time.valueOf(path.getTime()))
			.list();
	}

}

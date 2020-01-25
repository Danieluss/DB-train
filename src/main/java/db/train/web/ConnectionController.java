package db.train.web;

import db.train.persistence.model.Connection;
import db.train.persistence.model.Station;
import db.train.service.ShortestPathService;
import db.train.web.dto.StationList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.webrepogen.ICRUDRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/connection")
public class ConnectionController extends AbstractWebController<Connection, Long> {

	private ShortestPathService service;
	private ICRUDRepository<Station, Long> stationRepository;

	@Autowired
	public ConnectionController(ICRUDRepository<Connection, Long> repository, ShortestPathService service, ICRUDRepository<Station, Long> stationRepository) {
		init(repository, Connection.class, Long.class);
		this.service = service;
		this.stationRepository = stationRepository;
	}

	@RequestMapping(value = "/generate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Connection generate(@RequestBody @Valid StationList stationList) {
		List<Station> fetchedStations = new ArrayList<>();
		for (int i = 0; i < stationList.getStations().size(); i++) {
			fetchedStations.add(
					stationRepository
							.findById(stationList.getStations().get(i).getId())
							.orElse(null));
		}
		stationList.setStations(fetchedStations);
		Connection connection = service.connect(stationList);
		return repo.save(connection);
	}

}

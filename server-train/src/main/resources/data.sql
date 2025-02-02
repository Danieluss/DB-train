INSERT INTO zone(id, description, name) VALUES (1, 'WLKP', 'Wielkopolska') ON CONFLICT DO NOTHING;
INSERT INTO station(id, name, longitude, latitude) VALUES (1, 'Poznań', 50.0, 50.0) ON CONFLICT DO NOTHING;
INSERT INTO station(id, name, longitude, latitude) VALUES (2, 'Warszawa', 50.0, 50.0) ON CONFLICT DO NOTHING;
INSERT INTO edge(id, station1_id, station2_id, distance) VALUES (1, 1, 2, 300.0) ON CONFLICT DO NOTHING;
INSERT INTO carriage_type(id, seats, cabin) VALUES (1, 40, true) ON CONFLICT DO NOTHING;
INSERT INTO train(id, name) VALUES (1, 'Pegazus') ON CONFLICT DO NOTHING;
INSERT INTO carriage(id, train_id, type_id) VALUES (1, 1, 1) ON CONFLICT DO NOTHING;
INSERT INTO connection(id, first_station, last_station, name, first_day, last_day) VALUES (1, 'Poznań', 'Warszawa', 'Poznaniak', TIMESTAMP '2019-12-15 00:00:00', TIMESTAMP '2020-03-11 00:00:00') ON CONFLICT DO NOTHING;
INSERT INTO stations_connections(id, station_id, connection_id, number, stop, arrival, departure) VALUES (1, 1, 1, 1, true, '10:00', '10:20') ON CONFLICT DO NOTHING;
INSERT INTO stations_connections(id, station_id, connection_id, number, stop, arrival, departure) VALUES (2, 2, 1, 2, true, '15:34', '15:55') ON CONFLICT DO NOTHING;
INSERT INTO trains_connections(train_id, connection_id) VALUES (1, 1) ON CONFLICT DO NOTHING;
INSERT INTO train_user(id, email, name, password, role, surname, username) VALUES (1, 'admin@gmail.com', 'Janusz', '$2a$10$q5vKefZnFSi2PM4gS3s7a.LVC5ijXp0eCjLYYyPMypamZJzpFsXRm', 'ROLE_USER', 'Kowalski', 'user') ON CONFLICT DO NOTHING;
INSERT INTO train_user(id, email, name, password, role, surname, username) VALUES (2, 'user@gmail.com', 'Janusz', '$2a$10$B86Leqg4lyrroM.bv9OBJupQMPVK8MMA8iW5yllPIKgRFL/f9sKKO', 'ROLE_ADMIN', 'Niekowalski', 'admin') ON CONFLICT DO NOTHING;
INSERT INTO discount(id, name, percent_off) VALUES (2, 'świąteczna', 25) ON CONFLICT DO NOTHING;
INSERT INTO discount(id, name, percent_off) VALUES (1, 'brak', 0) ON CONFLICT DO NOTHING;
INSERT INTO ticket(uuid, discount_id, user_id) VALUES ('de644b9b-7744-4dc4-9501-bb0ef0386952', 1, 1) ON CONFLICT DO NOTHING;
INSERT INTO ticket(uuid, discount_id, user_id) VALUES ('643dafc5-6d35-464c-badd-41d1640e8338', 2, 1) ON CONFLICT DO NOTHING;
INSERT INTO commutation_ticket_type(id, name, price, zone_id) VALUES (1, 'semestralny', 25, 1) ON CONFLICT DO NOTHING;
INSERT INTO zones_connections(zone_id, connection_id) VALUES (1, 1) ON CONFLICT DO NOTHING;
INSERT INTO commutation_ticket(uuid, end_date, start_date, type_id) VALUES ('de644b9b-7744-4dc4-9501-bb0ef0386952', TIMESTAMP '2020-01-10 10:23:54', TIMESTAMP '2020-06-15 10:23:54', 1) ON CONFLICT DO NOTHING;
INSERT INTO path_ticket(uuid, price, stationconnection_id1, stationconnection_id2, date) VALUES ('643dafc5-6d35-464c-badd-41d1640e8338', 15, 1, 2, TIMESTAMP '2020-01-27') ON CONFLICT DO NOTHING;


CREATE OR REPLACE FUNCTION ticketsOutsideInterval()
    RETURNS trigger AS  E'
DECLARE
    res bigint;
BEGIN
    SELECT COUNT(*) into res
    FROM stations_connections sc, path_ticket pt
    where pt.stationconnection_id1 = sc.id
    and sc.connection_id = NEW.id
    and (
        date_trunc(\'day\', pt.date) < date_trunc(\'day\', NEW.first_day)
        or
        date_trunc(\'day\', pt.date) > date_trunc(\'day\', NEW.last_day)
    );
    if res > 0 then
        RAISE EXCEPTION \'You cannot shrink the interval this way because there are tickets outside of it.\';
    end if;
    return NEW;
END; '
LANGUAGE 'plpgsql';

DROP TRIGGER if exists ticketsOutsideIntervalTrigger on connection;

CREATE TRIGGER ticketsOutsideIntervalTrigger
    BEFORE UPDATE ON connection
    FOR EACH ROW
    EXECUTE PROCEDURE ticketsOutsideInterval();

CREATE OR REPLACE FUNCTION checkIfEdgeIsUsed()
    RETURNS trigger AS E'
DECLARE
    total integer;
BEGIN
    select count(*) into total
    from stations_connections a, stations_connections b, connection c
    where a.connection_id = b.connection_id
    and c.id = a.connection_id
    and a.number+1 = b.number
    and a.station_id = OLD.station1_id
    and b.station_id = OLD.station2_id
    and c.last_day >= current_date;
    if total > 0 then
        RAISE EXCEPTION \'This edge is currently in use\';
    end if;
    return OLD;
END; '
LANGUAGE 'plpgsql';

DROP TRIGGER if exists checkIfEdgeIsUsedTrigger on edge;

CREATE TRIGGER checkIfEdgeIsUsedTrigger
    BEFORE DELETE ON edge
    FOR EACH ROW
    EXECUTE PROCEDURE checkIfEdgeIsUsed();

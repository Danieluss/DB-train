INSERT INTO zone(id, description, name) VALUES (1, 'WLKP', 'Wielkopolska') ON CONFLICT DO NOTHING;
INSERT INTO station(id, name, longitude, latitude) VALUES (1, 'Poznań', 50.0, 50.0) ON CONFLICT DO NOTHING;
INSERT INTO station(id, name, longitude, latitude) VALUES (2, 'Warszawa', 50.0, 50.0) ON CONFLICT DO NOTHING;
INSERT INTO edge(id, station1_id, station2_id, distance) VALUES (1, 1, 2, 300.0) ON CONFLICT DO NOTHING;
INSERT INTO carriage_type(id, seats, cabin) VALUES (1, 40, true) ON CONFLICT DO NOTHING;
INSERT INTO train(id, name) VALUES (1, 'Pegazus') ON CONFLICT DO NOTHING;
INSERT INTO carriage(id, train_id, type_id) VALUES (1, 1, 1) ON CONFLICT DO NOTHING;
INSERT INTO connection(id, name) VALUES (1, 'Poznaniak') ON CONFLICT DO NOTHING;
INSERT INTO stations_connections(id, station_id, connection_id, number, stop, arrival, departure) VALUES (1, 1, 1, 1, true, '10:00', '10:20') ON CONFLICT DO NOTHING;
INSERT INTO stations_connections(id, station_id, connection_id, number, stop, arrival, departure) VALUES (2, 2, 1, 2, true, '15:34', '15:55') ON CONFLICT DO NOTHING;
INSERT INTO trains_connections(train_id, connection_id) VALUES (1, 1) ON CONFLICT DO NOTHING;
INSERT INTO train_user(id, email, name, surname, username) VALUES (1, 'user@gmail.com', 'Janusz', 'Kowalski', 'JK') ON CONFLICT DO NOTHING;
INSERT INTO discount(id, name, percent_off) VALUES (1, 'świąteczna', 25) ON CONFLICT DO NOTHING;
INSERT INTO ticket(uuid, discount_id, user_id) VALUES ('de644b9b-7744-4dc4-9501-bb0ef0386952', 1, 1) ON CONFLICT DO NOTHING;
INSERT INTO ticket(uuid, user_id) VALUES ('643dafc5-6d35-464c-badd-41d1640e8338', 1) ON CONFLICT DO NOTHING;
INSERT INTO commutation_ticket_type(id, name, price, zone_id) VALUES (1, 'semestralny', 25, 1) ON CONFLICT DO NOTHING;
INSERT INTO zones_connections(zone_id, connection_id) VALUES (1, 1) ON CONFLICT DO NOTHING;
INSERT INTO commutation_ticket(uuid, end_date, start_date, type_id) VALUES ('de644b9b-7744-4dc4-9501-bb0ef0386952', TIMESTAMP '2020-01-10 10:23:54', TIMESTAMP '2020-06-15 10:23:54', 1) ON CONFLICT DO NOTHING;
INSERT INTO path_ticket(uuid, price, station1_id, connection_id, station2_id, date) VALUES ('643dafc5-6d35-464c-badd-41d1640e8338', 15, 1, 1, 2, TIMESTAMP '2020-01-27') ON CONFLICT DO NOTHING;


CREATE OR REPLACE FUNCTION total ()
    RETURNS double precision AS '
DECLARE
    total_from_path real;
    total_from_commutation real;
BEGIN
    SELECT sum(price) into total_from_path FROM path_ticket;
    SELECT sum(price) into total_from_commutation FROM commutation_ticket join commutation_ticket_type on type_id = id;
    RETURN total_from_commutation + total_from_path;
END;
'
LANGUAGE 'plpgsql';


CREATE OR REPLACE PROCEDURE price_rise (IN modifier double precision)
    AS '
    DECLARE
BEGIN
    UPDATE commutation_ticket_type set price = price * modifier;
    END;
'
    LANGUAGE 'plpgsql';

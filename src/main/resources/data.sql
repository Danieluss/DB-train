INSERT INTO station(id, place, longitude, latitude) VALUES (1, 'Poznań', 50.0, 50.0) ON CONFLICT DO NOTHING;
INSERT INTO station(id, place, longitude, latitude) VALUES (2, 'Warszawa', 50.0, 50.0) ON CONFLICT DO NOTHING;
INSERT INTO edge(id, station1_id, station2_id, distance) VALUES (1, 1, 2, 300.0) ON CONFLICT DO NOTHING;
INSERT INTO carriage_type(id, seats, cabin) VALUES (1, 40, true) ON CONFLICT DO NOTHING;
INSERT INTO train(id, name) VALUES (1, 'Pegazus') ON CONFLICT DO NOTHING;
INSERT INTO carriage(id, train_id, type_id) VALUES (1, 1, 1) ON CONFLICT DO NOTHING;

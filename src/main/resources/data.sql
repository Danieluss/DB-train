INSERT INTO Station(id, place, longitude, latitude) VALUES (1, 'Poznań', 50.0, 50.0) ON CONFLICT DO NOTHING;
INSERT INTO Station(id, place, longitude, latitude) VALUES (2, 'Warszawa', 50.0, 50.0) ON CONFLICT DO NOTHING;
INSERT INTO Edge(id, station1_id, station2_id, distance) VALUES (1, 1, 2, 300.0) ON CONFLICT DO NOTHING;
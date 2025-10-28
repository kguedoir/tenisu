-- Flyway V2: Seed initial players dataset
-- Inserts countries, players, player_data, and player_last_results based on provided JSON

-- Countries
INSERT INTO countries (code, picture) VALUES
    ('SRB', 'https://tenisu.latelier.co/resources/Serbie.png'),
    ('USA', 'https://tenisu.latelier.co/resources/USA.png'),
    ('SUI', 'https://tenisu.latelier.co/resources/Suisse.png'),
    ('ESP', 'https://tenisu.latelier.co/resources/Espagne.png');

-- Players (avec colonnes de PlayerData car @Embeddable)
INSERT INTO players (id, firstname, lastname, shortname, sex, country_code, picture, rank, points, weight, height, age) VALUES
    (52,  'Novak',  'Djokovic',  'N.DJO', 'M', 'SRB', 'https://tenisu.latelier.co/resources/Djokovic.png', 2, 2542, 80000, 188, 31),
    (95,  'Venus',  'Williams',  'V.WIL', 'F', 'USA', 'https://tenisu.latelier.co/resources/Venus.webp', 52, 1105, 74000, 185, 38),
    (65,  'Stan',   'Wawrinka',  'S.WAW', 'M', 'SUI', 'https://tenisu.latelier.co/resources/Wawrinka.png', 21, 1784, 81000, 183, 33),
    (102, 'Serena', 'Williams',  'S.WIL', 'F', 'USA', 'https://tenisu.latelier.co/resources/Serena.png', 10, 3521, 72000, 175, 37),
    (17,  'Rafael', 'Nadal',     'R.NAD', 'M', 'ESP', 'https://tenisu.latelier.co/resources/Nadal.png', 1, 1982, 85000, 185, 33);

-- Player last results (five per player, positions 0..4)
-- Novak Djokovic (52): [1,1,1,1,1]
INSERT INTO player_last_results (player_id, position, result_value) VALUES
    (52, 0, 1), (52, 1, 1), (52, 2, 1), (52, 3, 1), (52, 4, 1);

-- Venus Williams (95): [0,1,0,0,1]
INSERT INTO player_last_results (player_id, position, result_value) VALUES
    (95, 0, 0), (95, 1, 1), (95, 2, 0), (95, 3, 0), (95, 4, 1);

-- Stan Wawrinka (65): [1,1,1,0,1]
INSERT INTO player_last_results (player_id, position, result_value) VALUES
    (65, 0, 1), (65, 1, 1), (65, 2, 1), (65, 3, 0), (65, 4, 1);

-- Serena Williams (102): [0,1,1,1,0]
INSERT INTO player_last_results (player_id, position, result_value) VALUES
    (102, 0, 0), (102, 1, 1), (102, 2, 1), (102, 3, 1), (102, 4, 0);

-- Rafael Nadal (17): [1,0,0,0,1]
INSERT INTO player_last_results (player_id, position, result_value) VALUES
    (17, 0, 1), (17, 1, 0), (17, 2, 0), (17, 3, 0), (17, 4, 1);

-- Données initiales pour la base de données
-- Ce fichier est automatiquement exécuté par Spring Boot au démarrage

-- Insertion des équipes de Ligue 1
INSERT INTO equipe (id, name, acronym, budget) VALUES (1, 'OGC Nice', 'OGCN', 50000000);
INSERT INTO equipe (id, name, acronym, budget) VALUES (2, 'Paris Saint-Germain', 'PSG', 500000000);
INSERT INTO equipe (id, name, acronym, budget) VALUES (3, 'Olympique de Marseille', 'OM', 80000000);
INSERT INTO equipe (id, name, acronym, budget) VALUES (4, 'AS Monaco', 'ASM', 100000000);
INSERT INTO equipe (id, name, acronym, budget) VALUES (5, 'Olympique Lyonnais', 'OL', 70000000);
INSERT INTO equipe (id, name, acronym, budget) VALUES (6, 'RC Lens', 'RCL', 40000000);
INSERT INTO equipe (id, name, acronym, budget) VALUES (7, 'Lille OSC', 'LOSC', 60000000);
INSERT INTO equipe (id, name, acronym, budget) VALUES (8, 'Stade Rennais', 'SRFC', 55000000);

-- Insertion des joueurs pour OGC Nice
INSERT INTO joueur (id, name, position, equipe_id) VALUES (1, 'Kasper Schmeichel', 'Gardien', 1);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (2, 'Jean-Clair Todibo', 'Défenseur', 1);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (3, 'Khéphren Thuram', 'Milieu', 1);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (4, 'Terem Moffi', 'Attaquant', 1);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (5, 'Gaëtan Laborde', 'Attaquant', 1);

-- Insertion des joueurs pour PSG
INSERT INTO joueur (id, name, position, equipe_id) VALUES (6, 'Gianluigi Donnarumma', 'Gardien', 2);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (7, 'Marquinhos', 'Défenseur', 2);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (8, 'Vitinha', 'Milieu', 2);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (9, 'Kylian Mbappé', 'Attaquant', 2);

-- Insertion des joueurs pour OM
INSERT INTO joueur (id, name, position, equipe_id) VALUES (10, 'Pau Lopez', 'Gardien', 3);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (11, 'Chancel Mbemba', 'Défenseur', 3);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (12, 'Pierre-Emerick Aubameyang', 'Attaquant', 3);

-- Insertion des joueurs pour Monaco
INSERT INTO joueur (id, name, position, equipe_id) VALUES (13, 'Philipp Köhn', 'Gardien', 4);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (14, 'Wissam Ben Yedder', 'Attaquant', 4);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (15, 'Takumi Minamino', 'Milieu', 4);

-- Insertion des joueurs pour Lyon
INSERT INTO joueur (id, name, position, equipe_id) VALUES (16, 'Anthony Lopes', 'Gardien', 5);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (17, 'Alexandre Lacazette', 'Attaquant', 5);

-- Insertion des joueurs pour Lens
INSERT INTO joueur (id, name, position, equipe_id) VALUES (18, 'Brice Samba', 'Gardien', 6);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (19, 'Florian Sotoca', 'Attaquant', 6);

-- Insertion des joueurs pour Lille
INSERT INTO joueur (id, name, position, equipe_id) VALUES (20, 'Lucas Chevalier', 'Gardien', 7);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (21, 'Jonathan David', 'Attaquant', 7);

-- Insertion des joueurs pour Rennes
INSERT INTO joueur (id, name, position, equipe_id) VALUES (22, 'Steve Mandanda', 'Gardien', 8);
INSERT INTO joueur (id, name, position, equipe_id) VALUES (23, 'Amine Gouiri', 'Attaquant', 8);
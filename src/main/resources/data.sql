-- PRODUCTS
INSERT INTO product (title, artist, genre, release_year, label, price, stock, cover_url, description) VALUES
('A Night at the Opera', 'Queen', 'Rock', 1975, 'EMI', 35.99, 10,
'https://upload.wikimedia.org/wikipedia/en/4/4d/Queen_A_Night_At_The_Opera.png',
'Sorti en 1975, cet album marque un tournant dans la carrière de Queen. Mélange de rock progressif, de ballades et d’opéra rock, il inclut le célèbre "Bohemian Rhapsody".'),

('The Miseducation of Lauryn Hill', 'Lauryn Hill', 'Neo-Soul', 1998, 'Ruffhouse', 30.99, 5,
'https://upload.wikimedia.org/wikipedia/en/9/99/The_Miseducation_of_Lauryn_Hill.png',
'Seul album studio de Lauryn Hill, mêlant soul, R&B et reggae. Lauréat de plusieurs Grammy Awards, il est considéré comme un classique du genre.'),

('Disintegration', 'The Cure', 'Post-Punk', 1989, 'Fiction', 33.99, 7,
'https://upload.wikimedia.org/wikipedia/en/b/b8/CureDisintegration.jpg',
'Album culte de The Cure, reconnu pour son atmosphère sombre et ses compositions gothiques emblématiques.'),

('Nevermind', 'Nirvana', 'Grunge', 1991, 'DGC', 27.99, 2,
'https://upload.wikimedia.org/wikipedia/en/b/b7/NirvanaNevermindalbumcover.jpg',
'Album qui a popularisé le grunge dans le monde entier, contenant le tube "Smells Like Teen Spirit".'),

('MTV Unplugged in New York', 'Nirvana', 'Acoustique', 1994, 'DGC', 40.99, 6,
'https://upload.wikimedia.org/wikipedia/en/5/54/Nirvana_mtv_unplugged_in_new_york.png',
'Enregistrement acoustique mythique de Nirvana, capturant une performance intimiste et unique.'),

('Lemonade', 'Beyoncé', 'R&B', 2016, 'Parkwood', 34.99, 8,
'https://upload.wikimedia.org/wikipedia/en/5/53/Beyonce_-_Lemonade_%28Official_Album_Cover%29.png',
'Album visuel et conceptuel de Beyoncé, sorti en 2016, abordant des thèmes personnels et politiques, reconnu pour son mélange de R&B, rock, soul et country.'),

('Good Kid, M.A.A.D City (Deluxe)', 'Kendrick Lamar', 'Hip-Hop', 2012, 'TDE', 31.99, 12,
'https://upload.wikimedia.org/wikipedia/en/c/ce/KendrickLamarGKMCDeluxe.jpg',
'Deuxième album studio de Kendrick Lamar, acclamé pour son récit autobiographique sur la jeunesse à Compton et son innovation musicale.'),

('Grace', 'Jeff Buckley', 'Rock Alternatif', 1994, 'Columbia', 26.99, 2,
'https://upload.wikimedia.org/wikipedia/en/e/e4/Jeff_Buckley_grace.jpg',
'Premier et unique album studio de Jeff Buckley, acclamé pour sa voix exceptionnelle et ses compositions poétiques. Inclut la reprise emblématique de "Hallelujah".'),

('Back to Black', 'Amy Winehouse', 'Soul', 2006, 'Island', 27.99, 15,
'https://upload.wikimedia.org/wikipedia/en/6/67/Amy_Winehouse_-_Back_to_Black_%28album%29.png',
'Album soul moderne devenu culte, mélangeant influences rétro et paroles personnelles poignantes.'),

('OK Computer', 'Radiohead', 'Rock Alternatif', 1997, 'Parlophone', 29.99, 20,
'https://upload.wikimedia.org/wikipedia/en/b/ba/Radioheadokcomputer.png',
'Album expérimental de Radiohead, explorant l’aliénation moderne et les nouvelles technologies.');

-- CUSTOMERS
INSERT INTO customer (email, name) VALUES
  ('alice@example.com', 'Alice'),
  ('bob@example.com', 'Bob');

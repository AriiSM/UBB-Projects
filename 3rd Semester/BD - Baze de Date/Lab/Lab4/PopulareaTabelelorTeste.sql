USE FirmaDeTransportExtern

--Crearea tabelelor care sunt legate de View
DELETE FROM Tables
SET IDENTITY_INSERT Tables ON;
INSERT INTO Tables(TableID, Name) VALUES
	(1, 'Clienti'),
	(2, 'Comanda'),
	(3, 'FirmaComenziClienti');
SET IDENTITY_INSERT Tables OFF;

--Cream un View pt un singur tabel (PK)
CREATE OR ALTER VIEW View_Client AS
	SELECT nume AS Denumire, 
			locatie AS Locatie, 
			telefon AS Telefon 
	FROM Clienti;
--Cream un View pentru cele 2 tabele (PK+FK)
CREATE OR ALTER VIEW View_ComandaClient AS
	SELECT comanda.adr_incarcare AS Incarcare, 
			comanda.adr_descarcare AS Descarcare,
			comanda.pret AS Pret, 
			client.nume AS Client,
			client.telefon AS Telefon
	FROM Clienti client INNER JOIN Comanda comanda ON comanda.cod_client = client.cod_client
--Cream un View ce contine o comanda SELECT pe cel putin 2 tabele
-- si avand o clauza GROUP BY
CREATE OR ALTER VIEW View_FirmaComenziClienti AS
	SELECT f.denumire AS Firma,
			cl.nume as Client, 
			co.adr_incarcare AS Incarcare,
			co.distanta AS Distanta, 
			co.pret AS Pret
	FROM Clienti cl INNER JOIN Comanda co ON cl.cod_client=co.cod_client INNER JOIN Firma_transport f ON f.caen=co.caen
	GROUP BY f.denumire,cl.nume, co.adr_incarcare,co.distanta,co.pret

--In View adaugam View-urile create mai sus
SET IDENTITY_INSERT Views ON;
INSERT INTO Views(ViewID, Name) VALUES
	(1, 'View_Client'),
	(2, 'View_ComandaClient'),
	(3, 'View_Comenzi_Clienti_Pret');
SET IDENTITY_INSERT Views OFF;

--Testele pe care le am ( ca si optiuni ) pentru tabele
SET IDENTITY_INSERT Tests ON;
DELETE FROM Tests;
INSERT INTO Tests(TestID, Name) VALUES
	(1, 'selectView'),
	(2, 'insertClienti'),
	(3, 'insertComanda'),
	(4, 'deleteClienti'),
	(5, 'deleteComanda'),
	(6, 'insertFirma'),
	(7, 'deleteFirma');
SET IDENTITY_INSERT Tests OFF;

--Testele pentru View
INSERT INTO TestViews (TestID, ViewID) VALUES
	(1, 1),
	(1, 2),
	(1, 3);

--Testele pentru tabele in ordinea corespunzatoare
SELECT * FROM TestTables;
INSERT INTO TestTables(TestID, TableID, NoOfRows, Position) VALUES
	(2, 1, 10000, 1), 
	(6, 3, 10000, 2),
	(3, 2, 10000, 3),
	(5, 2, 10000, 4),
	(7, 3, 10000, 5),
	(4, 1, 10000, 6);
	
--Verificari
SELECT * FROM View_Client;
SELECT * FROM View_ComandaClient;
SELECT * FROM View_FirmaComenziClienti;

SELECT * FROM Tables;
SELECT * FROM Views;
SELECT * FROM Tests;
SELECT * FROM TestTables;
SELECT * FROM TestRunTables;
SELECT * FROM TestViews;

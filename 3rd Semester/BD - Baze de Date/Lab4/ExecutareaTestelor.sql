USE FirmaDeTransportExtern

--Procedura de testare pentru toate View-urile 
--(adaugam in tabelul TestRunView cat dureaza selectul pentru fiecare View)
GO
CREATE OR ALTER PROCEDURE testRunViewProc AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @start DATETIME;
	DECLARE @end DATETIME;
	DECLARE @view INT;

	SELECT @view = ViewID FROM Views WHERE Name = 'View_Client';
	SET @start = SYSDATETIME();
	SELECT * FROM View_Client;
	SET @end = SYSDATETIME();
	PRINT('View_Client test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms')
	INSERT INTO TestRuns(Description, StartAt,EndAt) VALUES
		('View_Client Test ', @start, @end);
	INSERT INTO TestRunViews(TestRunID, ViewID, StartAt, EndAt) VALUES
		(@@IDENTITY, @view, @start, @end);



	SELECT @view = ViewID FROM Views WHERE Name = 'View_ComandaClient';
	SET @start = SYSDATETIME();
	SELECT * FROM View_Client;
	SET @end = SYSDATETIME();
	PRINT('View_ComandaClient test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms')
	INSERT INTO TestRuns(Description, StartAt,EndAt) VALUES
		('View_ComandaClient Test ', @start, @end);
	INSERT INTO TestRunViews(TestRunID, ViewID, StartAt, EndAt) VALUES
		(@@IDENTITY, @view, @start, @end);


	SELECT @view = ViewID FROM Views WHERE Name = 'View_MasiniCheltuieliTotale';
	SET @start = SYSDATETIME();
	SELECT * FROM View_Client;
	SET @end = SYSDATETIME();
	PRINT('View_MasiniCheltuieliTotale test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms')
	INSERT INTO TestRuns(Description, StartAt,EndAt) VALUES
		('View_MasiniCheltuieliTotale Test ', @start, @end);
	INSERT INTO TestRunViews(TestRunID, ViewID, StartAt, EndAt) VALUES
		(@@IDENTITY, @view, @start, @end);
END




--Insertul pentru Clienti
GO
CREATE OR ALTER PROC insertClienti AS
BEGIN
		DECLARE @rows INT = (SELECT TOP 1 TT.NoOfRows
		FROM TestTables TT INNER JOIN Tests T ON TT.TestID=T.TestID
		WHERE T.Name = 'insertClienti');

		DECLARE @current INT = 1;
		DECLARE @id INT = 11;
		WHILE @current <= @rows
		BEGIN
			INSERT INTO Clienti(cod_client,nume,locatie,telefon,cont_bancar) VALUES
			( @id, 'numeCL', 'locatieCL', 'telefonCL', 'cont_bancarCL')
			SET @id = @id + 1
			SET @current = @current + 1
		END
END
GO
CREATE OR ALTER PROCEDURE INSERT_CLIENTI AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @start DATETIME;
	DECLARE @end DATETIME;
	DECLARE @table INT;
		
	SELECT @table = TableID FROM Tables WHERE Name='Clienti';
	SET @start = SYSDATETIME();
	EXEC insertClienti;
	SET @end = SYSDATETIME();
	PRINT('insertClienti test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms')
	INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES
		('insertClienti Test', @start, @end)
	INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt) VALUES
		(@@IDENTITY, @table, @start, @end);
END

--Insertul pentru Firma
GO
CREATE OR ALTER PROC insertFirma AS
BEGIN
	DECLARE @rows INT = (SELECT TOP 1 TT.NoOfRows
		FROM TestTables TT INNER JOIN Tests T ON TT.TestID=T.TestID
		WHERE T.Name = 'insertFirma');

	DECLARE @current INT = 1;
	DECLARE @id INT = 1;
	WHILE @current <= @rows
	BEGIN
		IF @id = 5229
		BEGIN
			SET @id = @id + 1
		END
		INSERT INTO Firma_transport(caen,denumire,sediu_firma,telefon,email) VALUES
		(CAST(@id AS VARCHAR), 'denumire', 'sediu', 'tel', 'email');
		SET @current = @current + 1
		SET @id = @id + 1
	END
END
GO
CREATE OR ALTER PROCEDURE INSERT_FIRMA AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @start DATETIME;
	DECLARE @end DATETIME;
	DECLARE @table INT;

	SELECT @table = TableID FROM Tables WHERE Name = 'FirmaComenziClienti';
	SET @start = SYSDATETIME();
	EXEC insertFirma;
	SET @end = SYSDATETIME();
	PRINT('insertFirma test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms')
	INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES
		('insertFirma Test', @start, @end)
	INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt) VALUES
		(@@IDENTITY, @table, @start, @end);
END

--Inserare pentru Comanda
GO
CREATE OR ALTER PROC insertComanda AS
BEGIN
	DECLARE @rows INT = (SELECT TOP 1 TT.NoOfRows
		FROM TestTables TT INNER JOIN Tests T ON TT.TestID=T.TestID
		WHERE T.Name = 'insertComanda');

		DECLARE @current INT = 1;
		DECLARE @idCO INT = 16;
		DECLARE @idCL INT = 11;
		DECLARE @caen INT = 1;
		WHILE @current <= @rows
		BEGIN
			INSERT INTO Comanda(id_comanda, cod_client, caen, adr_incarcare, adr_descarcare, cantitate, distanta, pret) VALUES
			(@idCO, @idCL, CAST(@caen AS VARCHAR), 'adrIncarcare', 'adrDescarcare', 1 ,1, 1);
			SET @idCO = @idCO + 1
			SET @idCL = @idCL + 1
			SET @caen = @caen + 1
			SET @current = @current + 1
		END
END
GO
CREATE OR ALTER PROCEDURE INSERT_COMANDA AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @start DATETIME;
	DECLARE @end DATETIME;
	DECLARE @table INT;

	SELECT @table = TableID FROM Tables WHERE Name='Comanda';
	SET @start = SYSDATETIME();
	EXEC insertComanda;
	SET @end = SYSDATETIME();
	PRINT('insertComanda test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms')
	INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES
		('insertComanda Test', @start, @end)
	INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt) VALUES
		(@@IDENTITY, @table, @start, @end);
END

--Delete pe Comanda
GO
CREATE OR ALTER PROCEDURE deleteComanda AS
BEGIN
	DECLARE @rows INT = (SELECT TOP 1 TT.NoOfRows
		FROM TestTables TT INNER JOIN Tests T ON TT.TestID=T.TestID
		WHERE T.Name = 'deleteComanda');

	DECLARE @current INT = 1;
		DECLARE @idCO INT = 16;
		DECLARE @idCL INT = 11;
		DECLARE @caen INT = 1;
		WHILE @current <= @rows
		BEGIN
			DELETE FROM Comanda WHERE cod_client = @idCL AND id_comanda = @idCO AND caen = @caen;
			SET @idCO = @idCO + 1
			SET @idCL = @idCL + 1
			SET @caen = @caen + 1
			SET @current = @current + 1
		END
END

GO
CREATE OR ALTER PROCEDURE DELETE_COMANDA AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @start DATETIME;
	DECLARE @end DATETIME;
	DECLARE @table INT;

	SELECT @table = TableID FROM Tables WHERE Name = 'Comanda';
	SET @start = SYSDATETIME();
	EXEC deleteComanda;
	SET @end = SYSDATETIME();
	PRINT('deleteComanda test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms');
	INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES
		('deleteComanda Test', @start, @end)
	INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt) VALUES
		(@@IDENTITY, @table, @start, @end)
END

--Delete pe Clienti
GO
CREATE OR ALTER PROCEDURE deleteClienti AS
BEGIN
	
	DECLARE @rows INT = (SELECT TOP 1 TT.NoOfRows
		FROM TestTables TT INNER JOIN Tests T ON TT.TestID=T.TestID
		WHERE T.Name = 'deleteClienti');

	DECLARE @current INT = 1;
	DECLARE @id INT = 11;
	WHILE @current <= @rows
	BEGIN
		DELETE FROM Clienti WHERE cod_client = @id;
		SET @id = @id + 1
		SET @current = @current + 1
	END
END
GO
CREATE OR ALTER PROCEDURE DELETE_CLIENT AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @start DATETIME;
	DECLARE @end DATETIME;
	DECLARE @table INT;

	SELECT @table = TableID FROM Tables WHERE Name = 'Clienti';
	SET @start = SYSDATETIME();
	EXEC deleteClienti;
	SET @end = SYSDATETIME();
	PRINT('deleteClienti test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms');
	INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES
		('deleteClienti Test', @start, @end)
	INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt) VALUES
		(@@IDENTITY, @table, @start, @end)
END

--Delete pe Firma
GO
CREATE OR ALTER PROCEDURE deleteFirma AS
BEGIN

	DECLARE @rows INT = (SELECT TOP 1 TT.NoOfRows
		FROM TestTables TT INNER JOIN Tests T ON TT.TestID=T.TestID
		WHERE T.Name = 'deleteFirma');

	DECLARE @current INT = 1;
	DECLARE @caen INT = 1;
	WHILE @current <= @rows
	BEGIN
		IF @caen = 5229
		BEGIN
			SET @caen = @caen + 1
		END
		DELETE FROM Firma_transport WHERE caen = CAST(@caen AS VARCHAR)
		SET @caen = @caen + 1
		SET @current = @current + 1
	END
END

GO
CREATE OR ALTER PROCEDURE DELETE_FIRMA AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @start DATETIME;
	DECLARE @end DATETIME;
	DECLARE @table INT;

	SELECT @table = TableID FROM Tables WHERE Name = 'FirmaComenziClienti';
	SET @start = SYSDATETIME();
	EXEC deleteFirma;
	SET @end = SYSDATETIME();
	PRINT('deleteFirma test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms');
	INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES
		('deleteFirma Test', @start, @end)
	INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt) VALUES
		(@@IDENTITY, @table, @start, @end)
END


--Main ul (facut de mine ca sa aiba sens acel Position pentru mine :) )
GO
CREATE OR ALTER PROCEDURE main AS
BEGIN
	DECLARE @TestID INT, @TableID INT, @NoOfRows INT, @Position INT;
	SELECT TOP 1 @TestID = TestID, @TableID = TableID, @NoOfRows = NoOfRows, @Position = Position
	FROM TestTables
	ORDER BY Position;
	WHILE @@ROWCOUNT > 0
	BEGIN
		-- Apelează procedura stocată aici
		--EXEC NumeleProcedurii @TestID, @TableID, @NoOfRows, @Position;
		IF @Position = 1
		BEGIN
			EXEC INSERT_CLIENTI;
		END

		IF @Position = 2
		BEGIN
			EXEC INSERT_FIRMA;
		END

		IF @Position = 3
		BEGIN
			EXEC INSERT_COMANDA;
			EXEC testRunViewProc;
		END

		IF @Position = 4
		BEGIN
			EXEC DELETE_COMANDA;
		END

		IF @Position = 5
		BEGIN
			EXEC DELETE_FIRMA;
		END

		IF @Position = 6
		BEGIN
			EXEC DELETE_CLIENT;
		END

		SELECT TOP 1 @TestID = TestID, @TableID = TableID, @NoOfRows = NoOfRows, @Position = Position
		FROM TestTables
		WHERE Position > @Position
		ORDER BY Position;
	END;
END


EXEC main

SELECT * FROM TestRuns;
DELETE FROM TestRuns;

SELECT * FROM Clienti;
SELECT * FROM Comanda;
SELECT * FROM Firma_transport;
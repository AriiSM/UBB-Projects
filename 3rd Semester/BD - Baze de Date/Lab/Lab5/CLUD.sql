USE FirmaDeTransportExtern

/*
Functie de validare pentru FACTURI_EMISE
*/
GO
CREATE OR ALTER FUNCTION Validare_FacturiEmise(
	@nr_comanda INT, --PK
	@data DATE, --date not null
	@TVA VARCHAR(100), --not null
	@tarif FLOAT, --not null
	@cantitate FLOAT --not null
	)RETURNS INT AS BEGIN
	--Verificam daca mai exista vreun obiect cu acest id 
	DECLARE @nr_comanda_count INT;
	SELECT @nr_comanda_count = COUNT(*) FROM Facturi_emise WHERE nr_comanda = @nr_comanda;
	
	IF @TVA IS NULL OR @tarif IS NULL OR @cantitate IS NULL OR (@nr_comanda_count IS NOT NULL AND @nr_comanda_count > 0)
		RETURN 0

	RETURN 1
END
GO

--Mic test pentru validare
DECLARE @result INT;
SELECT @result = dbo.Validare_FacturiEmise(123, '2023-01-01', '19%', 23, 12);
PRINT @result;

GO
CREATE OR ALTER PROCEDURE CRUD_FacturiEmise
	@nr_comanda INT,
	@data DATE, 
	@TVA VARCHAR(100), 
	@tarif FLOAT, 
	@cantitate FLOAT 
AS BEGIN
	DECLARE @validation_status INT;
	SELECT @validation_status = dbo.Validare_FacturiEmise(@nr_comanda, @data, @TVA, @tarif, @cantitate);
	IF (@validation_status = 0)
	BEGIN
		RAISERROR(N'Au fost introduse date incorecte', 16, 1);
		RETURN;
	END

	--CREATE = INSERT
	INSERT INTO Facturi_emise(nr_comanda, data, TVA, tarif, cantitate)
		VALUES (@nr_comanda, @data, @TVA, @tarif, @cantitate);
	SELECT * FROM Facturi_emise
	PRINT(N'Factura a fost creata cu succes! Folosing numarul de comanda '+ CAST(@nr_comanda AS VARCHAR(20))+' !');

	--READ = SELECT
	SELECT * FROM Facturi_emise
	WHERE nr_comanda = @nr_comanda;
	PRINT(N'Factura a fost printata cu succes cu id-ul ' + CAST(@nr_comanda AS VARCHAR(100)) + ' !');

	--UPDATE
	UPDATE Facturi_emise
	SET TVA = 'Updated'+@TVA
	WHERE nr_comanda = @nr_comanda;
	SELECT * FROM Facturi_emise
	PRINT(N'Factura a fost modificata cu succes! Id unic '+CAST(@nr_comanda AS VARCHAR(20)) + ' !');

	--DELETE
	DELETE FROM Facturi_emise
	WHERE nr_comanda = @nr_comanda;
	SELECT * FROM Facturi_emise
	PRINT(N'Factura cu id-ul ' + CAST(@nr_comanda AS VARCHAR(100)) + ' a fost stearsa!');
END
GO

/*
Functia de validare pentru COMANDA
*/
GO
CREATE OR ALTER FUNCTION Validare_Comanda(
	@id_comanda INT, --PK
	@cod_client INT, --FK catre alt tabel
	@caen VARCHAR(100), --FK catre alt tabel

	@adr_incarcare VARCHAR(100), --not null
	@adr_descarcare VARCHAR(100), --not null
	@cantitate FLOAT, --not null
	@distanta FLOAT, --not null
	@pret FLOAT --not null
	)
	RETURNS INT AS BEGIN
	
	DECLARE @id_comanda_count INT;
	SELECT @id_comanda_count = COUNT(*) FROM Comanda WHERE id_comanda = @id_comanda;

	DECLARE @cod_client_count INT;
	SELECT @cod_client_count = COUNT(*) FROM Comanda WHERE cod_client = @cod_client;
	
	DECLARE @caen_count INT;
	SELECT @caen_count = COUNT(*) FROM Comanda WHERE caen = @caen;

	IF @adr_descarcare IS NULL OR @adr_incarcare IS NULL OR @cantitate IS NULL OR @distanta 
		IS NULL OR @pret IS NULL OR (@id_comanda_count IS NOT NULL AND @id_comanda_count > 0) 
		OR (@cod_client_count IS NOT NULL AND @cod_client = 0) OR (@caen_count IS NOT NULL AND @caen_count = 0)
		RETURN 0;
	RETURN 1;
END
GO

--Mic test pentru validare
DECLARE @result INT;
SELECT @result = dbo.Validare_Comanda(1000, 1, '5229', 'abc', 'abc', 12, 13, 14);
PRINT @result;


GO
CREATE OR ALTER PROCEDURE CRUD_Comanda
	@id_comanda INT, 
	@cod_client INT, 
	@caen VARCHAR(100), 

	@adr_incarcare VARCHAR(100),
	@adr_descarcare VARCHAR(100), 
	@cantitate FLOAT, 
	@distanta FLOAT, 
	@pret FLOAT 
AS BEGIN
	DECLARE @validation_status INT;
	SELECT  @validation_status = dbo.Validare_Comanda(@id_comanda, @cod_client, @caen, @adr_incarcare, @adr_descarcare, @cantitate, @distanta, @pret);
	
	IF (@validation_status = 0)
	BEGIN
		RAISERROR(N'Au fost introduse date invalide', 16, 1);
		RETURN;
	END

	--CREATE = INSERT
	INSERT INTO Comanda(id_comanda, cod_client, caen, adr_incarcare, adr_descarcare, cantitate, distanta, pret)
	VALUES(@id_comanda, @cod_client, @caen, @adr_incarcare, @adr_descarcare, @cantitate, @distanta, @pret);
	SELECT * FROM Comanda
	PRINT(N'Comanda a fost creata cu succes! Folosind id-ul unic '+ CAST(@id_comanda AS VARCHAR(20)) + ' !');

	--READ = SELECT
	SELECT * FROM Comanda
	WHERE id_comanda = @id_comanda
	PRINT(N'Comanda a fost printata avand id-ul + '+CAST(@id_comanda AS VARCHAR(20)) + ' !');

	--UPDATE
	UPDATE Comanda
	SET adr_incarcare = 'Updated ' + @adr_incarcare, adr_descarcare = 'Updated '+@adr_descarcare
	WHERE id_comanda = @id_comanda;
	SELECT * FROM Comanda
	PRINT(N'Comanda a fost modificata cu succes! Avand id-ul '+ CAST(@id_comanda AS VARCHAR(20)) + ' !');

	--DELETE
	DELETE FROM Comanda
	WHERE id_comanda= @id_comanda
	SELECT * FROM Comanda
	PRINT(N'Comanda a fost stearsa cu succes! Avand id-ul '+ CAST(@id_comanda AS VARCHAR(20)) + ' !');
END
GO

/*
Functia de validare pentru ComandaFacturi
*/
GO
CREATE OR ALTER FUNCTION Validare_ComandaFacturi(
	@id_comanda INT, --FK Comanda
	@nr_comanda INT --FK Factura
	)
	RETURNS INT AS BEGIN
	
	DECLARE @id_comada_count INT;
	SELECT @id_comada_count = COUNT(*)FROM Comanda WHERE id_comanda = @id_comanda;

	DECLARE @nr_comanda_count INT;
	SELECT @nr_comanda_count = COUNT(*) FROM Facturi_emise WHERE nr_comanda = @nr_comanda;

	IF @id_comada_count = 0 OR @nr_comanda_count = 0
		RETURN 0;
	RETURN 1;
END
GO

--Mic test pentru validare
DECLARE @result INT;
SELECT @result = dbo.Validare_ComandaFacturi(1, 1);
PRINT @result;


GO
CREATE OR ALTER PROCEDURE CRUD_ComandaFacturi
	@id_comanda INT, 
	@nr_comanda INT 
AS BEGIN
	DECLARE @validation_status INT;
	SELECT @validation_status = dbo.Validare_ComandaFacturi(@id_comanda, @nr_comanda);

	IF (@validation_status = 0)
	BEGIN
		RAISERROR(N'Au fost introduse gresit datele', 16, 1);
		RETURN;
	END

	--CREATE = INSERT
	INSERT INTO ComandaFacturi(id_comanda, nr_comanda)
		VALUES (@id_comanda, @nr_comanda);
	SELECT * FROM ComandaFacturi
	PRINT(N'Conexiunea a fost creata cu succes!');

	--READ = SELECT
	SELECT * FROM ComandaFacturi
	WHERE id_comanda = @id_comanda AND nr_comanda = @nr_comanda
	PRINT(N'Conexiune printata cu succes!');

	--UPDATE
	PRINT(N'Nu putem efectua update!');

	--DELETE
	DELETE FROM ComandaFacturi
	WHERE id_comanda = @id_comanda AND nr_comanda = @nr_comanda
	SELECT * FROM ComandaFacturi
	PRINT(N'Conexiune stearsa cu succes!');
END 
GO

--Comenzile+Factura averenta, distanta medie minima cu 300km
GO

				--VIEW

CREATE OR ALTER VIEW VW_Comanda_Factura  AS 
	SELECT c.id_comanda AS Comanda,
			fe.nr_comanda AS Factura,
			adr_incarcare AS Incarcare,
			fe.data AS Data, 
			AVG(distanta)AS DistantaMedie
	FROM Comanda c INNER JOIN ComandaFacturi f on c.id_comanda=f.id_comanda INNER JOIN Facturi_emise fe on fe.nr_comanda=f.nr_comanda
	GROUP BY c.id_comanda,fe.nr_comanda,adr_incarcare,fe.data
	HAVING AVG(distanta) >= 300
GO

--View care ne arata comenzile care produc peste 1000 lei (sa zicem)
GO
CREATE OR ALTER VIEW VW_Comanda AS
	SELECT id_comanda AS Comanda,
			--cod_client AS Client,
			--adr_incarcare AS Incarcare,
			--adr_descarcare AS Descarcare,
			distanta AS Distanta,
			pret AS Pret
	FROM Comanda
	WHERE pret > 1000
GO


							--INDEXI

--Verific daca exista deja un index cu acest nume
IF EXISTS(SELECT name FROM sys.indexes WHERE name = N'idx_Comanda_id_comanda')
	--daca exista il sterg
	DROP INDEX idx_Comanda_id_comanda ON Comanda;
--creez un nou index
CREATE NONCLUSTERED INDEX idx_Comanda_id_comanda ON Comanda(pret);

SELECT * FROM VW_Comanda


--Verific daca exista deja un index cu acest nume
IF EXISTS(SELECT name FROM sys.indexes WHERE name = N'idx_FacturiEmise_nr_comanda')
	--daca exista il sterg
	DROP INDEX idx_FacturiEmise_nr_comanda ON Facturi_emise;
--creez un nou index
CREATE NONCLUSTERED INDEX idx_FacturiEmise_nr_comanda ON Facturi_emise(nr_comanda);

--Verific daca exista deja un index cu acest nume
IF EXISTS(SELECT name FROM sys.indexes WHERE name = N'idx_ComandaFacturi_pk_comandaFacturi')
	--daca exista il sterg
	DROP INDEX idx_ComandaFacturi_pk_comandaFacturi ON ComandaFacturi;
--creez un nou index
CREATE NONCLUSTERED INDEX idx_ComandaFacturi_pk_comandaFacturi ON ComandaFacturi(id_comanda, nr_comanda);

		SELECT * FROM VW_Comanda_Factura




--Executia acestui CRUD pentru ComandaFactura
EXEC CRUD_ComandaFacturi 2, 3
--Executam acest CRUD pt Factura
EXEC CRUD_FacturiEmise 109,'2023-12-12','19%',12,13
--Executia acestui CRUD pentru Comanda
EXEC CRUD_Comanda 200,1,'5229','abc','abc',12,13,14


DELETE FROM Comanda WHERE id_comanda = 200
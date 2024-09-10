USE FirmaDeTransportExtern

-- ======================================================================================================================================================
-- FUNCTII DE VALIDARE A DATELOR DIN TABELA      Client
-- ======================================================================================================================================================
GO
CREATE OR ALTER FUNCTION validareCodClient (@cod_client INT) 
RETURNS INT  
AS 
BEGIN
    IF @cod_client IS NULL
        RETURN 0;
    IF EXISTS (SELECT 1 FROM Clienti WHERE cod_client = @cod_client)
        RETURN 0;
    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validareNume (@nume VARCHAR(100)) 
RETURNS INT  
AS 
BEGIN
    IF @nume IS NULL
        RETURN 0;
    IF @nume =''
        RETURN 0;
    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validareLocatie (@locatie VARCHAR(100)) 
RETURNS INT   
AS 
BEGIN
    IF @locatie IS NULL
        RETURN 0;
    IF @locatie =''
        RETURN 0;
	IF LEN(@locatie) < 10 
		RETURN 0;
    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validareTelefon (@telefon VARCHAR(100)) 
RETURNS INT  
AS 
BEGIN
    IF @telefon IS NULL
        RETURN 0;
    IF @telefon =''
        RETURN 0;
    IF LEN(@telefon) != 12 
        RETURN 0;
    IF LEFT(@telefon, 3) != '+40' 
        RETURN 0;

    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validareContBancar (@cont_bancar VARCHAR(100)) 
RETURNS INT  
AS 
BEGIN
    IF @cont_bancar IS NULL
        RETURN 0;
    IF @cont_bancar =''
        RETURN 0;
   IF LEN(@cont_bancar) != 24 
        RETURN 0;
	IF LEFT(@cont_bancar, 2) != 'RO'
        RETURN 0;

    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validareClienti(
	@cod_client INT,

	@nume VARCHAR(100),
	@locatie VARCHAR(100),
	@telefon VARCHAR(100),
	@cont_bancar VARCHAR(100)
)RETURNS VARCHAR(200)
AS BEGIN
	DECLARE @err VARCHAR(200)
	SET @err = ''
	IF (dbo.validareCodClient(@cod_client) = 0)
		SET @err = @err + 'Cod Invalid.'

	IF (dbo.validareNume(@nume) = 0)
		SET @err = @err + 'Nume Invalid.'
	
	IF (dbo.validareLocatie(@locatie) = 0)
		SET @err = @err + 'Locatie Invalid.'

	IF (dbo.validareTelefon(@telefon) = 0)
		SET @err = @err + 'Telefon Invalid.'

	IF (dbo.validareContBancar(@cont_bancar) = 0)
		SET @err = @err + 'Cont Bancar Invalid.'
	
	RETURN @err
END
GO





-- ======================================================================================================================================================
-- FUNCTII DE VALIDARE A DATELOR DIN TABELA      Firma_transport
-- ======================================================================================================================================================

GO
CREATE OR ALTER FUNCTION validareCaen (@caen VARCHAR(100)) 
RETURNS INT  
AS 
BEGIN
    IF @caen IS NULL
        RETURN 0;
	IF EXISTS (SELECT 1 FROM Firma_transport WHERE caen = @caen)
        RETURN 0;

    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validareDenumire (@denumire VARCHAR(100)) 
RETURNS INT  
AS 
BEGIN
   IF @denumire IS NULL OR LEN(@denumire) < 10
        RETURN 0;

    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validareSediuFirma (@sediu_firma VARCHAR(100)) 
RETURNS INT  
AS 
BEGIN
    IF @sediu_firma IS NULL
        RETURN 0;
    IF @sediu_firma =''
        RETURN 0;
	IF LEN(@sediu_firma) < 10 
		RETURN 0;
    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validareTelefonFirma (@telefon VARCHAR(100)) 
RETURNS INT  
AS 
BEGIN
    IF @telefon IS NULL
        RETURN 0;
    IF @telefon =''
        RETURN 0;
    IF LEN(@telefon) != 12 
        RETURN 0;
    IF LEFT(@telefon, 3) != '+40' 
        RETURN 0;

    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validareEmail (@email VARCHAR(100)) 
RETURNS INT  
AS 
BEGIN
    IF @email IS NULL
        RETURN 0;
    IF @email LIKE '%@gmail.com'OR @email LIKE '%@yahoo.com'
        RETURN 1;

    RETURN 0;
END
GO

GO
CREATE OR ALTER FUNCTION validareFirmaTransport(
	@caen VARCHAR(100),

	@denumire VARCHAR(100),
	@sediu_firma VARCHAR(100),
	@telefon VARCHAR(100),
	@email VARCHAR(100)
)RETURNS VARCHAR(200)
AS BEGIN
	DECLARE @err VARCHAR(200)
	SET @err = ''
	IF (dbo.validareCaen(@caen) = 0)
		SET @err = @err + 'Caen Invalid.'

	IF (dbo.validareDenumire(@denumire) = 0)
		SET @err = @err + 'Denumire Invalid.'
	
	IF (dbo.validareSediuFirma (@sediu_firma) = 0)
		SET @err = @err + 'Sefiu Firma Invalid.'

	IF (dbo.validareTelefonFirma (@telefon) = 0)
		SET @err = @err + 'Telefon Firma Invalid.'

	IF (dbo.validareEmail (@email) = 0)
		SET @err = @err + 'Email Invalid.'
	
	RETURN @err
END
GO





-- ======================================================================================================================================================
-- FUNCTII DE VALIDARE A DATELOR DIN TABELA      Comanda
-- ======================================================================================================================================================

GO
CREATE OR ALTER FUNCTION validareIdComanda (@id_comanda INT) 
RETURNS INT  
AS 
BEGIN
    IF @id_comanda IS NULL
        RETURN 0;
    IF EXISTS (SELECT 1 FROM Comanda WHERE id_comanda = @id_comanda)
        RETURN 0;
    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validareAdrIncarcare (@adr_incarcare VARCHAR(100)) 
RETURNS INT  
AS 
BEGIN
    IF @adr_incarcare IS NULL
        RETURN 0;
    IF @adr_incarcare =''
        RETURN 0;
	IF LEN(@adr_incarcare) < 10 
		RETURN 0;
    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validareAdrDescarcare (@adr_descarcare VARCHAR(100)) 
RETURNS INT  
AS 
BEGIN
    IF @adr_descarcare IS NULL
        RETURN 0;
    IF @adr_descarcare =''
        RETURN 0;
	IF LEN(@adr_descarcare) < 10 
		RETURN 0;
    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validareCantitate (@cantitate FLOAT) 
RETURNS INT  
AS 
BEGIN
    IF @cantitate IS NULL
        RETURN 0;
    IF @cantitate < 1 OR @cantitate > 30
        RETURN 0;

    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validareDistanta (@distanta FLOAT) 
RETURNS INT  
AS 
BEGIN
    IF @distanta IS NULL
        RETURN 0;
    IF @distanta < 100 OR @distanta > 2000
        RETURN 0;

    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validarePret (@pret FLOAT) 
RETURNS INT  
AS 
BEGIN
    IF @pret IS NULL
        RETURN 0;

    IF @pret < 300 OR @pret > 3000
        RETURN 0;

    RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validareComanda(
	@id_comanda INT,
	@cod_client INT,
	@caen VARCHAR(100),

	@adr_incarcare VARCHAR(100),
	@adr_descarcare VARCHAR(100),
	@cantitate FLOAT,
	@distanta FLOAT,
	@pret FLOAT
)RETURNS VARCHAR(200)
AS BEGIN
	DECLARE @err VARCHAR(200)
	SET @err = ''
	IF (dbo.validareIdComanda (@id_comanda ) = 0)
		SET @err = @err + 'Id Comanda Invalid.'



	IF (dbo.validareAdrIncarcare (@adr_incarcare) = 0)
		SET @err = @err + 'Adresa Incarcare Invalid.'

	IF (dbo.validareAdrDescarcare (@adr_descarcare) = 0)
		SET @err = @err + 'Adresa Descarcare Invalid.'

	IF (dbo.validareCantitate (@cantitate) = 0)
		SET @err = @err + 'Cantitate Invalid.'

	IF (dbo.validareDistanta (@distanta) = 0)
		SET @err = @err + 'Distanta Invalid.'

	IF (dbo.validarePret (@pret) = 0)
		SET @err = @err + 'Pret Invalid.'
	RETURN @err
END
GO





-- ======================================================================================================================================================
-- TABEL DE URMARIRE
-- ======================================================================================================================================================
CREATE TABLE Urmarire
(
	nume_tabel NVARCHAR(20),
	actiune NVARCHAR(20),
	amprenta_temporala TIME
);
GO





-- ======================================================================================================================================================
-- ROLL-BACK PE INTREAGA PROCEDURA
-- ======================================================================================================================================================

GO
CREATE OR ALTER PROCEDURE insertClientFirmaComanda(
	--Clienti---------------------------------
	--CODCLIENT PK
	@nume VARCHAR(100),
	@locatie VARCHAR(100),
	@telefon VARCHAR(100),
	@cont_bancar VARCHAR(100),


	--Firma-----------------------------------
	@caen VARCHAR(100),

	@denumire VARCHAR(100),
	@sediu_firma VARCHAR(100),
	@telefonfirma VARCHAR(100),
	@email VARCHAR(100),


	--Comanda---------------------------------
	--IDCOMANDA PK
	--CODCLIENT FK
	--@caenC VARCHAR(100), --FK

	@adr_incarcare VARCHAR(100),
	@adr_descarcare VARCHAR(100),
	@cantitate FLOAT,
	@distanta FLOAT,
	@pret FLOAT

)AS BEGIN
	BEGIN TRAN
	BEGIN TRY
		DECLARE @err VARCHAR(200)
		--Client
		PRINT 'ZONA CLIENTULUI'
		DECLARE @codClient INT
		SET @codClient = (SELECT MAX(cod_client) + 1 FROM Clienti) 
		PRINT @codClient
		PRINT 'VALIDARE'
		SET @err = dbo.validareClienti(@codClient, @nume, @locatie, @telefon, @cont_bancar);
		IF (@err != '')
			BEGIN
				PRINT @err
				RAISERROR(@err, 14, 1)
			END
		PRINT 'DONE!!'
		PRINT 'INSERT CLIENT'
		INSERT INTO Clienti(cod_client, nume, locatie, telefon, cont_bancar) VALUES (@codClient, @nume, @locatie, @telefon, @cont_bancar);
		PRINT 'DONE!!'
		PRINT 'INSERT URMARIRE'
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Clienti', 'Insert', CURRENT_TIMESTAMP)
		PRINT 'DONE!!'
		PRINT 'DONE!!'


		

		--Firma_Transport
		PRINT 'ZONA FIRMEI DE TRANSPORT'
		PRINT 'VALIDARE FIRMA'
		SET @err = dbo.validareFirmaTransport(@caen, @denumire, @sediu_firma, @telefonfirma, @email)
		IF (@err != '')
			BEGIN
				PRINT @err
				RAISERROR(@err, 14, 1)
			END
		PRINT 'DONE!!'
		PRINT 'INSERT FIRMA DE TRANSPORT'
		INSERT INTO Firma_transport(caen, denumire, sediu_firma, telefon, email) VALUES (@caen, @denumire, @sediu_firma, @telefonfirma, @email)
		PRINT 'DONE!!'
		PRINT 'INSERT URMARIRE'
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Firma_Transport', 'Insert', CURRENT_TIMESTAMP)
		PRINT 'DONE!!'
		PRINT 'DONE!!'



		--Comanda
		PRINT 'ZONA COMENZII'
		PRINT 'VALIDADRE COMANDA'
		DECLARE @idComanda INT
		SET @idComanda = (SELECT MAX(id_comanda) + 1 FROM Comanda) 

		SET @err = dbo.validareComanda(@idComanda, @codClient, @caen, @adr_incarcare, @adr_descarcare, @cantitate, @distanta, @pret)
		IF (@err != '')
			BEGIN
				PRINT @err
				RAISERROR(@err, 14, 1)
			END
		PRINT 'DONE!!'
		PRINT 'INSERT COMANDA'
		INSERT INTO Comanda(id_comanda, cod_client, caen, adr_incarcare, adr_descarcare, cantitate, distanta, pret) VALUES (@idComanda, @codClient, @caen, @adr_incarcare, @adr_descarcare, @cantitate, @distanta, @pret)
		PRINT 'DONE!!'
		PRINT 'INSERT URMARIRE'
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Comanda', 'Insert', CURRENT_TIMESTAMP)
		PRINT 'DONE!!'

		COMMIT TRAN
		SELECT 'Commited!'
	END TRY

	BEGIN CATCH
		ROLLBACK TRAN
		SELECT 'Rollback!'
	END CATCH
END
GO

--Scenariu RollBack
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire
EXECUTE dbo.insertClientFirmaComanda
'Stan Ariana', 'Cluj-Napoca, Strada 4, nr 8', '+4072155513', 'FKXX1234ABCD5678EFGH1234',
'5228', 'Caravana Succesului', 'Bucuresti, Sector 6, Strada 3, nr 9', '+40762234567', 'succes@gmail.com',
'Terminal Maritim, Constanta', 'Magazin Alimentar, Sibiu', 11, 200, 1000
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire


--Scenariu Succes (nu a fost testat - ar trebui sa mearga)
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire
EXECUTE dbo.insertClientFirmaComanda
'Stan Adriana', 'Cluj-Napoca, Strada 4, nr 8', '+40721555123', 'ROXX1234ABCD5678EFGH1234',
'0000', 'Targul Viselor', 'Bucuresti, Sector 6, Strada 3, nr 9', '+40721555123', 'succes@gmail.com',
'Terminal Maritim, Constanta', 'Magazin Alimentar, Sibiu', 11, 200, 1000
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire
-- ======================================================================================================================================================
-- ROLL-BACK PE O PARTE DIN PROCEDURA
-- ======================================================================================================================================================

GO
CREATE OR ALTER PROCEDURE insertClientFirmaComandaPartial(
	--Clienti---------------------------------
	--CODCLIENT PK
	@nume VARCHAR(100),
	@locatie VARCHAR(100),
	@telefon VARCHAR(100),
	@cont_bancar VARCHAR(100),


	--Firma-----------------------------------
	@caen VARCHAR(100),

	@denumire VARCHAR(100),
	@sediu_firma VARCHAR(100),
	@telefonfirma VARCHAR(100),
	@email VARCHAR(100),


	--Comanda---------------------------------
	--IDCOMANDA PK
	--CODCLIENT FK
	--@caenC VARCHAR(100), --FK

	@adr_incarcare VARCHAR(100),
	@adr_descarcare VARCHAR(100),
	@cantitate FLOAT,
	@distanta FLOAT,
	@pret FLOAT

)AS BEGIN
	DECLARE @err VARCHAR(200)
	DECLARE @rollback INT
	SET @rollback = 0




	--CLIENT
	BEGIN TRAN
	BEGIN TRY
		PRINT 'ZONA CLIENTULUI'
		DECLARE @codClient INT
		SET @codClient = (SELECT MAX(cod_client) + 1 FROM Clienti) 
		PRINT @codClient
		PRINT 'VALIDARE'
		SET @err = dbo.validareClienti(@codClient, @nume, @locatie, @telefon, @cont_bancar);
		IF (@err != '')
			BEGIN
				PRINT @err
				RAISERROR(@err, 14, 1)
			END
		PRINT 'DONE!!'
		PRINT 'INSERT CLIENT'
		INSERT INTO Clienti(cod_client, nume, locatie, telefon, cont_bancar) VALUES (@codClient, @nume, @locatie, @telefon, @cont_bancar);
		PRINT 'DONE!!'
		PRINT 'INSERT URMARIRE'
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Clienti', 'Insert', CURRENT_TIMESTAMP)
		PRINT 'DONE!!'
		PRINT 'DONE!!' 

		COMMIT TRAN
		SELECT 'Commited for Clienti'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		SET @rollback = 1
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Clienti', 'Rollback', CURRENT_TIMESTAMP)
		SELECT 'Rollback for Clienti'
	END CATCH




	--FIRMA DE TRANSPORT
	BEGIN TRAN
	BEGIN TRY 
		PRINT 'ZONA FIRMEI DE TRANSPORT'
		PRINT 'VALIDARE FIRMA'
		SET @err = dbo.validareFirmaTransport(@caen, @denumire, @sediu_firma, @telefonfirma, @email)
		IF (@err != '')
			BEGIN
				PRINT @err
				RAISERROR(@err, 14, 1)
			END
		PRINT 'DONE!!'
		PRINT 'INSERT FIRMA DE TRANSPORT'
		INSERT INTO Firma_transport(caen, denumire, sediu_firma, telefon, email) VALUES (@caen, @denumire, @sediu_firma, @telefonfirma, @email)
		PRINT 'DONE!!'
		PRINT 'INSERT URMARIRE'
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Firma_Transport', 'Insert', CURRENT_TIMESTAMP)
		PRINT 'DONE!!'
		PRINT 'DONE!!'
		COMMIT TRAN
		SELECT 'Commited for Firma de Transport'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		SET @rollback = 1
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Firma_Transport', 'Rollback', CURRENT_TIMESTAMP)
		SELECT 'Rollback for Firma de Transport'
	END CATCH




	--COMANDA
	BEGIN TRAN
	BEGIN TRY
		IF (@rollback = 1)
			BEGIN
				PRINT 'NU SE POATE FACE INSERAREA IN COMANDA FARA CLIENT SI FIRMA'
				RAISERROR(@err, 14, 1)
			END

		PRINT 'ZONA COMENZII'
		PRINT 'VALIDADRE COMANDA'
		DECLARE @idComanda INT
		SET @idComanda = (SELECT MAX(id_comanda) + 1 FROM Comanda) 

		SET @err = dbo.validareComanda(@idComanda, @codClient, @caen, @adr_incarcare, @adr_descarcare, @cantitate, @distanta, @pret)
		IF (@err != '')
			BEGIN
				PRINT @err
				RAISERROR(@err, 14, 1)
			END
		PRINT 'DONE!!'
		PRINT 'INSERT COMANDA'
		INSERT INTO Comanda(id_comanda, cod_client, caen, adr_incarcare, adr_descarcare, cantitate, distanta, pret) VALUES (@idComanda, @codClient, @caen, @adr_incarcare, @adr_descarcare, @cantitate, @distanta, @pret)
		PRINT 'DONE!!'
		PRINT 'INSERT URMARIRE'
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Comanda', 'Insert', CURRENT_TIMESTAMP)
		PRINT 'DONE!!'
		COMMIT TRAN
		SELECT 'Commited for Comanda'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Comanda', 'Rollback', CURRENT_TIMESTAMP)
		SELECT 'Rollback for Comanda'
	END CATCH
END
GO



--Scenariu Succes Client 
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire
EXECUTE dbo.insertClientFirmaComandaPartial
'Test2', 'Cluj-Napoca, Strada 4, nr 8', '+40721555123', 'ROXX1234ABCD5678EFGH1234', --Client
'5228', 'Targul Viselor', 'Bucuresti, Sector 6, Strada 3, nr 9', '+40721555123', 'succes@gmail.com', -- Firma
'Terminal Maritim, Constanta', 'Magazin Alimentar, Sibiu', 11, 200, 1000 -- Comanda
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire



--Scenariu RollBack Client 
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire
EXECUTE dbo.insertClientFirmaComandaPartial
'Stan Adriana', 'Cluj-Napoca, Strada 4, nr 8', '+40721555', 'FXXX1234ABCD5678EFGH1234', --Client
'5228', 'Targul Viselor', 'Bucuresti, Sector 6, Strada 3, nr 9', '+40721555123', 'succes@gmail.com', -- Firma
'Terminal Maritim, Constanta', 'Magazin Alimentar, Sibiu', 11, 200, 1000 -- Comanda
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire




--Scenariu Succes Firma 
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire
EXECUTE dbo.insertClientFirmaComandaPartial
'Stan Adriana', 'Cluj-Napoca, Strada 4, nr 8', '+40721555', 'FXXX1234ABCD5678EFGH1234', --Client
'0001', 'Targul Viselor', 'Bucuresti, Sector 6, Strada 3, nr 9', '+40721555123', 'succes@gmail.com', -- Firma
'Terminal Maritim, Constanta', 'Magazin Alimentar, Sibiu', 11, 200, 1000 -- Comanda
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire



--Scenariu Rollback Firma 
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire
EXECUTE dbo.insertClientFirmaComandaPartial
'Stan Adriana', 'Cluj-Napoca, Strada 4, nr 8', '+40721555', 'FXXX1234ABCD5678EFGH1234', --Client
'0000', 'Targul Viselor', 'Bucuresti, Sector 6, Strada 3, nr 9', '+40721555123', 'succes@gmail.com', -- Firma
'Terminal Maritim, Constanta', 'Magazin Alimentar, Sibiu', 11, 200, 1000 -- Comanda
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire




--Scenariu Succes Comanda 
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire
EXECUTE dbo.insertClientFirmaComandaPartial
'TestComanda2', 'Cluj-Napoca, Strada 4, nr 8', '+40721555123', 'ROXX1234ABCD5678EFGH1234', --Client
'0003', 'Targul Viselor', 'Bucuresti, Sector 6, Strada 3, nr 9', '+40721555123', 'succes@gmail.com', -- Firma
'Terminal Maritim, Constanta', 'Magazin Alimentar, Sibiu', 11, 200, 1000 -- Comanda
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire




--Scenariu Rollback Comanda 
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire
EXECUTE dbo.insertClientFirmaComandaPartial
'Stan Adriana', 'Cluj-Napoca, Strada 4, nr 8', '+40721555123', 'ROXX1234ABCD5678EFGH1234', --Client
'5228', 'Targul Viselor', 'Bucuresti, Sector 6, Strada 3, nr 9', '+40721555123', 'succes@gmail.com', -- Firma
'Terminal Maritim, Constanta', 'Magazin Alimentar, Sibiu', 50, 3000, 1000 -- Comanda
SELECT * FROM Clienti
SELECT * FROM Firma_transport
SELECT * FROM Comanda
SELECT * FROM Urmarire
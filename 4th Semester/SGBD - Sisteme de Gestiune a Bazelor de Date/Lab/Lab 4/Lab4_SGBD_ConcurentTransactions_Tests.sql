USE FirmaDeTransportExtern
GO

-- ======================================================================================================================================================
--DIRTY READS
-- ======================================================================================================================================================




--PROBLEM:
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; 
BEGIN TRAN
	SELECT * FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
	WAITFOR DELAY '00:00:10'
	SELECT * FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
COMMIT TRAN


--SOLUTION:
SET TRANSACTION ISOLATION LEVEL READ COMMITTED 
BEGIN TRAN
	SELECT * FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
	WAITFOR DELAY '00:00:10'
	SELECT * FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
COMMIT TRAN




-- ======================================================================================================================================================
--UNREPEATABLE READS
-- ======================================================================================================================================================




--PROBLEM:
SET TRANSACTION ISOLATION LEVEL READ COMMITTED 
BEGIN TRAN
	SELECT * FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
	WAITFOR DELAY '00:00:10'
	SELECT * FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
COMMIT TRAN


--SOLUTION:
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ 
BEGIN TRAN
	SELECT * FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
	WAITFOR DELAY '00:00:10'
	SELECT * FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
COMMIT TRAN




-- ======================================================================================================================================================
--PHANTOM READS
-- ======================================================================================================================================================




--PROBLEM:
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ 
BEGIN TRAN
	SELECT * FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
	WAITFOR DELAY '00:00:10'
	SELECT * FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
COMMIT TRAN




--SOLUTION:
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
BEGIN TRAN
	SELECT * FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
	WAITFOR DELAY '00:00:10'
	SELECT * FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
COMMIT TRAN




-- ======================================================================================================================================================
--DEADLOCK
-- ======================================================================================================================================================

--PROBLEM:
GO
CREATE OR ALTER PROC deadlock1 AS
BEGIN
	SET DEADLOCK_PRIORITY NORMAL;
	BEGIN TRAN
	UPDATE Clienti SET nume='Deadlock' WHERE cod_client = (SELECT MAX(cod_client) FROM Clienti)

	WAITFOR DELAY '00:00:05'

	UPDATE Facturi_emise SET TVA = 'Deadlock 00%' WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
	COMMIT TRAN
END
GO


EXEC deadlock1

SELECT * FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
SELECT * FROM Clienti WHERE cod_client = (SELECT MAX (cod_client) FROM Clienti)

--SOLUTION
GO
CREATE OR ALTER PROC deadlock2 AS
BEGIN
	SET DEADLOCK_PRIORITY HIGH;
	BEGIN TRAN
	UPDATE Clienti SET nume='Deadlock' WHERE cod_client = (SELECT MAX(cod_client) FROM Clienti)

	WAITFOR DELAY '00:00:05'

	UPDATE Facturi_emise SET TVA = 'Deadlock 00%' WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
	COMMIT TRAN
END
GO


EXEC deadlock2

SELECT * FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
SELECT * FROM Clienti WHERE cod_client = (SELECT MAX (cod_client) FROM Clienti)











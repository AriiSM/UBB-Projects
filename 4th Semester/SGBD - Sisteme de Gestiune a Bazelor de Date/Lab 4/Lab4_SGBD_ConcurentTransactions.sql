USE FirmaDeTransportExtern
GO

SELECT * FROM Facturi_emise
SELECT * FROM Clienti

-- ======================================================================================================================================================
--DIRTY READS
-- ======================================================================================================================================================


BEGIN TRAN
	UPDATE Facturi_emise SET TVA ='Dirty 00%' WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
	WAITFOR DELAY '00:00:05'
ROLLBACK TRAN


-- ======================================================================================================================================================
--UNREPEATABLE READS
-- ======================================================================================================================================================


BEGIN TRAN
	WAITFOR DELAY '00:00:05'
	UPDATE Facturi_emise SET TVA = 'Unrepeatable 00%' WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
COMMIT TRAN
UPDATE Facturi_emise SET TVA = '20%' WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)


-- ======================================================================================================================================================
--PHANTOM READS
-- ======================================================================================================================================================


--INSERT INTO Facturi_emise(nr_comanda,data, TVA, tarif, cantitate) VALUES (16, '2023-10-19', '100%' , 112,112)

DELETE FROM Facturi_emise WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
BEGIN TRAN
	WAITFOR DELAY '00:00:05'
	INSERT INTO Facturi_emise(nr_comanda,data, TVA, tarif, cantitate) VALUES (16, '2023-10-19', 'Phantom' , 112,112)
COMMIT TRAN


-- ======================================================================================================================================================
--DEADLOCK
-- ======================================================================================================================================================


GO
CREATE OR ALTER PROC deadLock AS
BEGIN
	BEGIN TRAN
	UPDATE Facturi_emise SET TVA = 'Deadlock 00%' WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
	WAITFOR DELAY '00:00:06'

	UPDATE Clienti SET nume='Deadlock' WHERE cod_client = (SELECT MAX(cod_client) FROM Clienti)
	COMMIT TRAN
END
GO
EXEC deadLock


UPDATE Facturi_emise SET TVA = '20%' WHERE nr_comanda = (SELECT MAX(nr_comanda) FROM Facturi_emise)
UPDATE Clienti SET nume='Test2' WHERE cod_client = (SELECT MAX(cod_client) FROM Clienti)



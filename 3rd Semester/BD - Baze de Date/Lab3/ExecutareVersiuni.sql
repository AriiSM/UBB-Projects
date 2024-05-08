--modifica tipul unei coloane;
CREATE PROCEDURE do_proc_1
AS
BEGIN
	ALTER TABLE Soferi ALTER COLUMN salar BIGINT;
	UPDATE _version SET versionNumber = 1;
END
GO

CREATE PROCEDURE undo_proc_1
AS
BEGIN
	ALTER TABLE Soferi ALTER COLUMN salar FLOAT;
	UPDATE _version set versionNumber = 0;
END
GO


--adauga o costrângere de “valoare implicită” pentru un câmp;
CREATE PROCEDURE do_proc_2
AS
BEGIN
	ALTER TABLE Facturi_emise ADD CONSTRAINT DF_Facturi_emise_TVA DEFAULT '19%' FOR TVA;
	UPDATE _version SET versionNumber = 2;
END
GO

CREATE PROCEDURE undo_proc_2
AS
BEGIN
	ALTER TABLE Facturi_emise DROP CONSTRAINT DF_Facturi_emise_TVA;
	UPDATE _version SET versionNumber = 1;
END
GO

--creea/şterge o tabelă
CREATE PROCEDURE do_proc_3
AS
BEGIN
	CREATE TABLE Voluntar(
		voluntarID INT PRIMARY KEY IDENTITY,
		nume_prenume_voluntar VARCHAR(100),
		telefon VARCHAR(100),
		email VARCHAR(100)
	);
	UPDATE _version SET versionNumber = 3;
END
GO

CREATE PROCEDURE undo_proc_3
AS
BEGIN
	DROP TABLE Voluntar;
	UPDATE _version SET versionNumber = 2;
END
GO

--adăuga un câmp nou;
CREATE PROCEDURE do_proc_4
AS
BEGIN
	ALTER TABLE Voluntar ADD salariu INT;
	UPDATE _version SET versionNumber = 4;
END
GO

CREATE PROCEDURE undo_proc_4
AS
BEGIN
	ALTER TABLE Voluntar DROP COLUMN salariu;
	UPDATE _version SET versionNumber = 3;
END
GO

--creea/şterge o constrângere de cheie străină
CREATE PROCEDURE do_proc_5
AS
BEGIN
	ALTER TABLE Voluntar ADD Leader VARCHAR(100);
	ALTER TABLE Voluntar ADD CONSTRAINT fk_voluntar FOREIGN KEY (Leader) REFERENCES Angajati(cnp);
	UPDATE _version SET versionNumber = 5;
END
GO

CREATE PROCEDURE undo_proc_5
AS
BEGIN
	ALTER TABLE Voluntar DROP CONSTRAINT fk_voluntar;
	ALTER TABLE Voluntar DROP COLUMN Leader;
	UPDATE _version SET versionNumber = 4;
END
GO

CREATE PROCEDURE main
@version INT
AS
BEGIN
		IF @version > 5 OR @version < 0
			BEGIN
				PRINT 'Versiune invalida!'
			END
		ELSE
			BEGIN
				IF EXISTS(Select versionNumber FROM _version WHERE @version = versionNumber)
					BEGIN
						PRINT 'Asta este deja versiunea curenta!!!'
					END
				ELSE
					BEGIN
						DECLARE @curent_version INT;
						SELECT @curent_version = versionNumber FROM _version;
						WHILE @curent_version != @version
							BEGIN
								IF @curent_version < @version
									BEGIN
										SET @curent_version = @curent_version + 1
										EXEC('do_proc_'+@curent_version);
									END
								ELSE
									BEGIN
										EXEC('undo_proc_'+@curent_version);
										SET @curent_version = @curent_version - 1;
									END
							END
					END
					PRINT 'Versiunea curenta este: '+ CAST(@curent_version AS VARCHAR(3));
			END
END
GO


EXEC main 0;
SELECT * FROM _version;
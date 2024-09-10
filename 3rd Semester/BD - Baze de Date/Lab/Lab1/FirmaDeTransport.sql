CREATE DATABASE FirmaDeTransportExtern;
GO
USE FirmaDeTransportExtern

CREATE TABLE Clienti(
	cod_client INT PRIMARY KEY,

	nume VARCHAR(100),
	locatie VARCHAR(100),
	telefon VARCHAR(100),
	cont_bancar VARCHAR(100)
);

CREATE TABLE Firma_transport(
	caen VARCHAR(100) PRIMARY KEY,

	denumire VARCHAR(100),
	sediu_firma VARCHAR(100),
	telefon VARCHAR(100),
	email VARCHAR(100)
);

CREATE TABLE Comanda(
	id_comanda INT PRIMARY KEY,
	cod_client INT FOREIGN KEY REFERENCES Clienti(cod_client),
	caen VARCHAR(100) FOREIGN KEY REFERENCES Firma_transport(caen),

	adr_incarcare VARCHAR(100),
	adr_descarcare VARCHAR(100),
	cantitate FLOAT,
	distanta FLOAT,
	pret FLOAT
);

CREATE TABLE Facturi_emise(
	nr_comanda INT PRIMARY KEY,

	data DATE,
	TVA VARCHAR(100),
	tarif FLOAT,
	cantitate FLOAT
);


CREATE TABLE ComandaFacturi(
	id_comanda INT FOREIGN KEY REFERENCES Comanda(id_comanda),
	nr_comanda INT FOREIGN KEY REFERENCES Facturi_emise(nr_comanda),

	CONSTRAINT pk_comandaFacturi PRIMARY KEY(id_comanda,nr_comanda)
);



CREATE TABLE Angajati(
	cnp VARCHAR(100) PRIMARY KEY,
	caen VARCHAR(100) FOREIGN KEY REFERENCES Firma_transport(caen),

	nume  VARCHAR(100),
	prenume VARCHAR(100),
	functie VARCHAR(100),
	salar FLOAT,
	bonusuri FLOAT
);

CREATE TABLE Soferi(
	id_sofer INT PRIMARY KEY,
	caen VARCHAR(100) FOREIGN KEY REFERENCES Firma_transport(caen),

	cnp_sofer VARCHAR(100),
	nume VARCHAR(100),
	prenumme VARCHAR(100),
	permis VARCHAR(100),
	tara_interdictie VARCHAR(100),
	salar FLOAT,
	bonusuri FLOAT,
	diurna FLOAT
);

CREATE TABLE Masini(
	nr_masina VARCHAR(100),
	model VARCHAR(100),
	capacitate FLOAT,

	id_sofer INT FOREIGN KEY REFERENCES Soferi(id_sofer),
	CONSTRAINT pk_masina PRIMARY KEY(id_sofer)
);

CREATE TABLE Costuri(
	id_costuri INT PRIMARY KEY,
	pk_masina INT FOREIGN KEY REFERENCES Masini(id_sofer),

	revizie FLOAT,
	tari_vinieta FLOAT,
	impozit FLOAT,
	asigurari FLOAT,
	copie_conform FLOAT
);

CREATE TABLE Furnizori_de_piese_auto(
	cod_furnizor INT PRIMARY KEY,

	denumire VARCHAR(100),
	pret FLOAT,
	descriere VARCHAR(100),
	cantitate FLOAT
);

CREATE TABLE MasiniFurnizori(
	pk_masina INT FOREIGN KEY REFERENCES Masini(id_sofer),
	cod_furnizor INT FOREIGN KEY REFERENCES Furnizori_de_piese_auto(cod_furnizor),

	CONSTRAINT pk_masiniFurnizori PRIMARY KEY(pk_masina,cod_furnizor)
);


CREATE TABLE Facturi_incasate(
	codi_factura INT PRIMARY KEY,
	cod_furnizor INT FOREIGN KEY REFERENCES Furnizori_de_piese_auto(cod_furnizor),

	descriere VARCHAR(100),
	data DATE,
	tarif FLOAT
);

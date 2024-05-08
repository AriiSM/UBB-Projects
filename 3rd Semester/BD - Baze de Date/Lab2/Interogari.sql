--1) Comenzile care au fost efectuate cu TVA ul de 20%
SELECT c.id_comanda,cod_client,adr_incarcare,adr_descarcare,distanta,pret,data
FROM Comanda c INNER JOIN ComandaFacturi f on c.id_comanda=f.id_comanda
INNER JOIN Facturi_emise fe on fe.nr_comanda=f.nr_comanda
WHERE FE.TVA='20%'

--2)Comenzile care au fost efectuate unde s-a parcurs o distanta medie minima cu 300km
SELECT c.id_comanda,fe.nr_comanda,adr_incarcare,fe.data, AVG(distanta)AS DistantaMedie
FROM Comanda c INNER JOIN ComandaFacturi f on c.id_comanda=f.id_comanda
INNER JOIN Facturi_emise fe on fe.nr_comanda=f.nr_comanda
GROUP BY c.id_comanda,fe.nr_comanda,adr_incarcare,fe.data
HAVING AVG(distanta) >= 300

--3)Clientii care au efectuat comenzi in cadrul firmei
SELECT f.denumire,cl.nume, co.adr_incarcare,co.distanta, AVG(pret) AS PretMediu
FROM Clienti cl INNER JOIN Comanda co ON cl.cod_client=co.cod_client INNER JOIN Firma_transport f ON f.caen=co.caen
GROUP BY f.denumire,cl.nume, co.adr_incarcare,co.distanta
HAVING AVG(pret) > 1000

--4)Soferii, masinile lor si tarile unde au interdictie
SELECT s.nume,s.prenumme, m.nr_masina,s.tara_interdictie
FROM Soferi s INNER JOIN Masini m ON s.id_sofer=m.id_sofer
WHERE m.capacitate = 4

--5) Salar+Bonusuri / Angajat din firma
SELECT f.denumire,a.cnp, (a.nume+' '+a.prenume) AS Angajati, SUM(a.salar+a.bonusuri) AS SalarBonusuriAngajati
FROM Angajati a INNER JOIN Firma_transport f on a.caen=f.caen 
GROUP BY f.denumire,cnp,a.nume,a.prenume

--6) Modelele de masini cu capacitatea peste 3t
SELECT DISTINCT model
FROM Masini 
WHERE capacitate >3 

--7
SELECT DISTINCT salar
FROM Soferi
WHERE salar > 4000

--8) Comenzile care au fost efectuate, unde s-a transportat mai mult de 10t de marfa si tva-ul a fost 18%
SELECT c.id_comanda,cod_client,adr_incarcare,adr_descarcare,distanta,data,tarif
FROM Comanda c INNER JOIN ComandaFacturi f on c.id_comanda=f.id_comanda
INNER JOIN Facturi_emise fe on fe.nr_comanda=f.nr_comanda
WHERE c.cantitate>=10  and fe.TVA='18%'

--9) Comenzile efectuate, distanta si pretul
SELECT adr_incarcare,adr_descarcare,distanta,pret
FROM Comanda c INNER JOIN ComandaFacturi f on c.id_comanda=f.id_comanda
INNER JOIN Facturi_emise fe on fe.nr_comanda=f.nr_comanda
WHERE c.distanta>=300

--10) Costurile totale pentru fiecare masina peste 16.000 lei
SELECT (s.nume+' '+s.prenumme) AS NumePrenume ,m.nr_masina, SUM(revizie+tari_vinieta+impozit+asigurari+copie_conform) AS CheltuieliTotale
FROM Costuri co INNER JOIN Masini m ON co.pk_masina = m.id_sofer INNER JOIN Soferi s ON s.id_sofer=m.id_sofer
GROUP BY s.nume,s.prenumme,co.id_costuri,m.nr_masina
HAVING  SUM(revizie+tari_vinieta+impozit+asigurari+copie_conform)>16000
ORDER BY CheltuieliTotale
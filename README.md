# HaulageCompany
Szoftverarchitektúrák házi feladat backend része

## Adminisztrációs rendszer fuvarozó cég számára 
A feladat egy fuvarozós cég adminisztrációs rendszerének az elkészítése, amiben tárolni kell a vállalat telephelyeit, járműparkját, a szerződtetett boltok listáját és az árukat, amiket a kamionok a megadott boltokba szállítanak.

Egy mobil alkalmazás segítségével lehetőség van árubeszerzésre, új boltok felvételére, új járművek megvásárlására és fuvarok indítására. Amennyiben egy bolttal felbontásra kerül a szerződés, az adminisztrációs alkalmazás segítségével törölhető a listából. Hasonlóan megoldható ez járművek kivonásánál is.

Local install guide: 
- kell hozzá Java, Maven, PostgreSQL, létre kell hozni az adatbázist (pgAdmin applikációban egyszerű) pl.: 'haulage_company' néven
- az application.properties fileban módosítani kell a kapcsolódási beállításokat a lokális PostgreSQL-nek megfelelően pl.: 
- spring.datasource.url=jdbc:postgresql://localhost:5432/'YOUR_DATABASE_NAME'
- spring.datasource.username='YOUR_USERNAME'
- spring.datasource.password='YOUR_PASSWORD'

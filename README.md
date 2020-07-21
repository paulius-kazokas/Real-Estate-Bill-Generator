Jul 21, 2020:

Goals:

1. Fetch data for rolling month yyyy-MM before user actions (Main.class).

2. Add extra logic with indicators by using utility name (might be good as is, but double check).

3. Bill entity, interface, repository. Connections with user, bill, utility entities.

4. Add bill to loggedInMenuActions case 3.

5. Get back to DataGenerator.class logic.

6. Add user info to loggedInMenuActions case 4.

7. Add user bank balance (so it could actually pay the bill).

    ...
   
n-?. Separate utility prices for different citites.

(admin)

n-1. Add role system to separate admin and regular users (will need to add extra collumns into the db).

n. Add very rough constant numbers for how many people can live in one building type building.
(each building is individual: privatus namas, butas 5aukstai, butas 9aukstai, butas 13aukstu ...).

n+1. Count same persons with current active address (to calculate members of building).

n+2. Calculate how many buildings city regions contains.

n+3. Calculate how many regions city has (strict city region structure).

n+4. Calculate how many cities in country has.

n+5. Generate rough country sleeping city regions utility monthly report.


Jul 10, 2020:

1. Generates a interfaces monthly bill.
2. Generates Utility PVM monthly price report.
3. Generates Utility Unit monthly price report.



Jun 23, 2020:

If image below doesn't load click here: https://drive.google.com/file/d/1zpSG0aVPqFjjHitsbTRivA1bUXgGkoRW/view?usp=sharing
<p align="midlde">
  <img src="https://drive.google.com/uc?export=view&id=1zpSG0aVPqFjjHitsbTRivA1bUXgGkoRW" width="800" height="700">
</p>

Urban utilities Calculator

(diagram here)

1. Paslaugas teikiančio įmonės komponentas - duomenų tiekėjų šaltinis.
2. Sąskaitos komponentas - duomenų šaltinis.
3. Sąskaitos gavėjo komponentas - suinteresuotas sąskaitos gavėjas, nekilnojamo turto savininkas.
4. Profilio komponentas - visa informacija apie gavėją.
5. Veiksmų komponentas - veiksmai su sąskaita.
6. Benros sistemos komponentas - bendra visų sąskaitų system, kurioje saugomos visos sąskaitų būsenos, detali informacija, istorija.

Procesas:

Komunalines paslaugas teikianti įmonė išsiunčia einančio mėnesio kainos rodiklius į Sąskaitos komponentą. Sąskaitos komponente saugomi 5 metų komunalinių kainų svyravimai už mėnesį. Ateinant mėnesio pabaigai pagal gavėjo sunaudotų komunalinių prietaisų skaitliukų rodiklius sugeneruojamas ir išsiunčiamas sąskaitos išrašas gavėjui. Gavėjas gali sumokėti, sustabdyti, pridėti prie kito mėnesio sąskaitos už tam tikras komunalines paslaugas, jas patikslinti (jei bloga informacija) arba nesumokėti. Visa detali informacija apie gavėjo turimą nekilnojamąjį turtą, asmeninę informaciją. Visas visų gavėjų sąskaitų istorijas, būsenas ir detalią informaciją prieinama Bendros sistemos komponente. Joje taip pat galima rūšiuoti sąskaitas pagal mėnesius, pagal gavėjus, pagal tam tikras komunalines paslaugas, pagal nekilnojamo turto tipus, mikrorajonus, miestus ir galiausiai šalies mėnesines komunalines išlaidas.

Duomenys bus skaičiuojami pagal 2020-05 mėnesio vyravusias komunalines kainas, bet geliausiai bus atvaizduojama apytikslė daugiabučio, mikrorajonų, miesot, šalies mėnesinės komunalines išlaidas.

Technologijos:

Java11, MySQL DB, Maven, Github.

Konsolinė Java aplikacija.

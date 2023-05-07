# Ütemezési Modellek beadandó
## Készítette: Kovács Dániel (WQU2KS) | SZTE TTIK - 2022/23 tavaszi félév
---
## [1] Használat
### [1.1] Indítás
0) Java Runtime Environment 11 verzió telepítése
1) *[VSP_KovacsDaniel_WQU2KS.jar](https://github.com/KovacsDe17/Utemezesi-beadando/blob/main/target/VSP_KovacsDaniel_WQU2KS.jar)* letöltése
2) A letöltött *VSP_KovacsDaniel_WQU2KS.jar* fájl mellé egy "*input*" nevű mappa létrehozása
3) Az "*Input_VSP.xlsx*" fájl "*input*" mappába másolása
4) A *VSP_KovacsDaniel_WQU2KS.jar* fájl indítása dupla kattintással vagy a `java -jar "VSP_KovacsDaniel_WQU2KS.jar"` paranccsal
5) A főmenüből válasszuk ki valamely lehetőséget

### [1.2] Gráfok
Az alábbiak érvényesek a Kapcsolat alapú hálózati modellre és az Idő-tér alapú hálózati modellre is!
</br>
#### Csúcsok:
* Kör alakú elemek jelölik
* A címkék a megfelelő azonosítók
* Mozgathatóak
#### Élek:
* Nyilak jelölik
* Színek szerint a következők lehetnek
   - 🟢 depó él
   - 🟣 járat él
   - 🟠 várakozó él
   - 🔴 rezsi él

## [2] Menüpontok
### [2.1] Connection Based Graph
Kapcsolat alapú hálózati modell.
</br>
Megnyitáskor megjelenik a gráf. A bal és jobb oldalon a depó-indulási és -érkezési csúcs látható, melyeket rendre a (D) és (D') jelöl. Ezek között két oszlopban, egymással szemben a külöböző járat-indulási és -érkezési csúcsok találhatóak. Ezek között futnak a megfelelő depó(🟢)-, járat(🟣)- és rezsi(🔴) élek.

### [2.2] Time-Space Graph
Idő-tér hálózati modell.
</br>
Megnyitáskor megjelenik a gráf. Három sorban láthatjuk a járat-indulási és -érkezési csúcsokat azok végállomásai/depói alapján. Az alsó sorban bal és jobb oldalon a depó-indulási és -érkezési csúcs látható, melyeket rendre a (D) és (D') jelöl. Felettük két sorban a külöböző járat-indulási és -érkezési csúcsok találhatóak. Ezek között futnak a megfelelő depó(🟢)-, járat(🟣)-, várakozó(🟠)- és rezsi(🔴) élek.

### [2.3] Build IP model
Optimalizálási modell felépítése kapcsolat alapú hálózati modellre.
</br>
Megnyitáskor megjelenik egy üres szövegdoboz. A fenti *Generate Model* gombbal a program legenerál egy AMPL típusú optimalizálási modellt az első menüpontban szereplő kapcsolat alapú hálózati modellre. Ez tartalmazza a célfüggvényt (járművek minimalizálása), a változókat, megkötéseket. A generálást követően el tudjuk menteni *.txt* formátumban a modellt a *Save into file...* gomb segítségével.

### [2.4] Exit
Kilépés a programból.

# StorageManager
Android hf

## Bemutatás
Az alkalmazás raktár elemek kezelésére szolgál. Az alkalmazásban vannak raktár elem kategóriák, ezeken belül a kategóriába tartozó elemek. A felhasználó ezeket tudja megtekinteni, törölni, módosítani és bővíteni új elemekkel.

## Főbb funkciók
A felhasználó először egy raktár elem kategória listát lát az alkalmazásban (RecyclerView) itt lehetősége van új elemet felvenni (DialogFragment), meglévő kategóriát és elemeit törölni, illetve módosítani. Ha rákattint egy adott kategóriára, megjelenik a kategóriába tartozó elemek listája (Intent, Activity, RecyclerView), ezeket szintén tudja módosítani (DialogFragment), törölni, kiegészíteni új elemmel. Az alkalmazás eltárolja a megadott adatokat egy adatbázisban (Room, SQLLite). Az adatbázis minden egyes műveltre megfelelően frissül.

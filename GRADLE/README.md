# Com executar el programa

Per l'entrega final no fa falta cap driver generic, ja tenim tota la interfície programada.

Per executar:

./gradlew run

# Com executar els tests unitaris

Hem realitzat un test JUnit per tota classe del domini implementada que ho necessitava. 
Per executar els tests:

./gradlew test

# Altres comandes

El fet d'utilitzar gradle per aquest projecte ens ofereix moltes possibilitats. Les comandes següents poden resultar utils:

./gradlew jar: creara un .jar al directori build/libs amb només el codi del projecte, sense les dependencies.

./gradlew assembleDist: creara un .tar i un .zip (ambdos tenen el mateix) al directori build/distributions que contenen
l'estructura de directoris sencera que permetra instal·lar el projecte amb les seves dependencies en una maquina sense IDE i executarlo.

./gradlew clean: eliminara els fitxers de compilacio i els artefactes creats.

Mes informacio a: https://docs.gradle.org/current/userguide/application_plugin.html


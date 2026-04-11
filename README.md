# Monopoly (Java)

Implementación del juego **Monopoly** desarrollada en **Java**, centrada en modelar la lógica del tablero, turnos, propiedades, pagos y estado de jugadores.

---

## 📚 Descripción del proyecto

Este proyecto simula una partida de Monopoly a nivel de lógica de juego.  
El objetivo es practicar diseño orientado a objetos, separación de responsabilidades y gestión del estado de una partida por turnos.

Está pensado como proyecto académico/personal y puede evolucionar con nuevas reglas, interfaz o modo multijugador.

---

## 🧠 ¿Cómo funciona?

Una partida sigue el flujo clásico de Monopoly:

1. Se inicializa el tablero, jugadores y recursos.
2. Cada jugador juega por turnos.
3. En su turno, lanza dados y se mueve por el tablero.
4. Según la casilla:
   - puede comprar propiedad,
   - pagar alquiler,
   - recibir o pagar dinero por eventos,
   - ir a cárcel o ejecutar acciones especiales.
5. El juego continúa hasta que se cumpla la condición de fin de partida definida en tu implementación.

---

## 🧩 Reglas y mecánicas modeladas

- Gestión de **jugadores** (saldo, posición, estado)
- **Movimiento** por tablero con tiradas de dados
- **Propiedades** comprables
- **Cobro de alquileres**
- Control de **banca/saldo**
- Casillas especiales (según implementación)
- Eliminación de jugadores sin fondos (si aplica)

> Nota: Las reglas exactas dependen de tu implementación concreta en código.

---

## 🏗️ Arquitectura (orientativa)

El proyecto suele estructurarse en entidades como:

- `Juego` / `Game`: controla el ciclo principal
- `Jugador` / `Player`: estado del jugador
- `Tablero` / `Board`: casillas y navegación
- `Casilla` / `Tile`: comportamiento por tipo de casilla
- `Propiedad` / `Property`: compra, dueño y alquiler
- `Dado` / `Dice`: generación de tiradas

Si tus nombres reales difieren, puedes adaptar esta sección a tus clases.

---

## 🛠️ Tecnologías

- **Java** (100%)

---

## ▶️ Ejecución del proyecto

### Requisitos
- Java 17+ (recomendado)
- Git

### 1) Clonar repositorio
```bash
git clone https://github.com/nonovs/Monopoly.git
cd Monopoly
```

### 2) Compilar y ejecutar

#### Opción A — Proyecto Java simple
```bash
javac -d out $(find . -name "*.java")
java -cp out Main
```

#### Opción B — Maven (si existe `pom.xml`)
```bash
mvn clean compile
mvn exec:java
```

#### Opción C — Gradle (si existe `build.gradle`)
```bash
./gradlew build
./gradlew run
```

---

## 🧪 Pruebas

Si tienes tests:
- Con Maven: `mvn test`
- Con Gradle: `./gradlew test`

Si todavía no hay tests, es una buena mejora futura incluir pruebas unitarias para:
- compra de propiedades,
- cálculo de alquiler,
- bancarrota,
- turnos y movimiento.

---

## 🗺️ Roadmap (mejoras futuras)

- [ ] Añadir interfaz gráfica
- [ ] Añadir persistencia de partidas
- [ ] Añadir más reglas oficiales de Monopoly
- [ ] Mejorar cobertura de tests
- [ ] Añadir modo multijugador en red

---

## 🤝 Contribución

Las contribuciones son bienvenidas:

1. Haz fork del repo
2. Crea una rama: `feature/mi-mejora`
3. Haz commit de tus cambios
4. Abre una Pull Request

---

## 📄 Licencia

Este proyecto está bajo licencia **MIT**.  
Consulta el archivo [LICENSE](./LICENSE).

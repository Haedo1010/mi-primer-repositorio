# mi-primer-repositorio
# Escáner de Red (Trabajo Práctico - Redes 5to 1ra ET36)

Este proyecto es una herramienta creada en Java que permite escanear un rango de direcciones IP dentro de una red local para detectar qué dispositivos están activos. Se realiza mediante comandos `ping` e intenta resolver los nombres de host.

---

## Funcionalidades principales

- Ingreso de IP inicial y final
- Validación de direcciones IP antes del escaneo
- Ping a cada host para verificar si está activo
- Resolución del nombre del equipo (DNS / nslookup)
- Barra de progreso en tiempo real
- Tabla de resultados (IP, nombre, estado, tiempo)
- Cuenta total de dispositivos activos al finalizar
- Ordenamiento de columnas en la tabla
- Posibilidad de guardar los resultados en archivo CSV
- Tiempo de espera seleccionable (1000ms, 2000ms, 4000ms)

---

## Estructura del código

| Clase       | Descripción                                                         |
|-------------|---------------------------------------------------------------------|
| `Main`      | Arranque del programa con SwingUtilities                            |
| `GUI`       | Interfaz gráfica con tabla, botones y barra de progreso             |
| `RedScanner`| Lógica que hace ping a IPs y guarda CSV                             |
| `HostInfo`  | Objeto con datos de cada host encontrado                            |
| `Utils`     | Validación de formato IP                                            |

---

## Cómo ejecutar el programa

1. Clonar el repositorio o descargar los archivos `.java`
2. Abrir el proyecto en un IDE (recomendado IntelliJ o NetBeans)
3. Compilar y ejecutar la clase `Main.java`

También se puede ejecutar por terminal:

```bash
javac *.java
java Main

## Guardar resultados
Presionar el botón Guardar CSV y elegir una ubicación. El archivo se guarda con extensión .csv y puede abrirse con Excel, Google Sheets, etc.

## equisitos técnicos
Java JDK 8 o superior
Sistema operativo Windows (por uso del comando ping -n)
Librerías estándar de Java (Swing)

## Autor
Lucas Haedo - 5to 1ra ET36
Trabajo Práctico de Redes (Año 2025)
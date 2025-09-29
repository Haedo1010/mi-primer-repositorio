# mi-primer-repositorio
# Escáner de Red (Trabajo Práctico - Redes 5to 1ra ET36)

## video de demostracion de uso, explicacion, y preguntas frecuentes.
https://youtu.be/Y7HnouK2Ym8

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

## Nuevas funcionalidades (Netstat)

Ahora incluye 3 funciones de Netstat que permiten consultar el estado de la red directamente desde la interfaz gráfica:

Netstat Conexiones (netstat -ano)
    Muestra todas las conexiones activas en el equipo (local, remota, estado de la conexión y PID del proceso asociado).

Netstat Estadísticas (netstat -e)
    Muestra estadísticas generales de red (bytes enviados y recibidos, errores de transmisión, paquetes descartados, etc).

Netstat Rutas (netstat -r)
    Muestra la tabla de enrutamiento de la red, incluyendo las rutas disponibles, máscaras de subred y gateways configurados.

---

## Cómo usar las funciones de Netstat

Al ejecutar el programa, en la parte superior de la ventana encontrarás tres nuevos botones:
    - Netstat Conexiones.
    - Netstat Estadísticas.
    - Netstat Rutas.

Al presionar cualquiera de ellos se abrirá una ventana emergente con los resultados del comando correspondiente.

El contenido se muestra en un cuadro desplazable para facilitar la lectura.

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

## Cómo usar el programa

Para usar el escáner de red, sigue estos sencillos pasos:

1.  **Ejecutar la aplicación**: Abre el proyecto en tu IDE (como IntelliJ o NetBeans) y ejecuta la clase `Main.java` para iniciar la interfaz.también se puede ejecutar por terminal: ```bash  javac *.java / java Main.
2.  **Ingresar el rango de IPs**: En los campos de texto **"IP Inicio"** y **"IP Fin"**, escribe las direcciones IP que definen el rango que deseas escanear. Por ejemplo, `192.168.1.1` y `192.168.1.254`.
3.  **Seleccionar el tiempo de espera (Timeout)**: Elige el tiempo máximo en milisegundos que el programa esperará por una respuesta de cada host.
4.  **Iniciar el escaneo**: Haz clic en el botón **"Escanear"**. La barra de progreso en la parte inferior de la ventana comenzará a llenarse, indicando el avance del escaneo.
5.  **Revisar los resultados**: Una vez completado el escaneo, verás una tabla con los resultados. Cada fila muestra la IP, el nombre del equipo, su estado (Activo/Inactivo) y el tiempo de respuesta. Puedes hacer clic en los encabezados de las columnas para ordenar la información.
6.  **Guardar los resultados**: Para guardar los datos en un archivo, presiona el botón **"Guardar CSV"**. Se abrirá una ventana para que elijas dónde quieres guardar el archivo.
7.  **Limpiar la pantalla**: Si deseas realizar un nuevo escaneo, haz clic en el botón **"Limpiar"** para borrar todos los campos y la tabla de resultados.

## Guardar resultados
Presionar el botón Guardar CSV y elegir una ubicación. El archivo se guarda con extensión .csv y puede abrirse con Excel, Google Sheets, etc.

## equisitos técnicos
Java JDK 8 o superior
Sistema operativo Windows (por uso del comando ping -n)
Librerías estándar de Java (Swing)

## Autor
Lucas Haedo - 5to 1ra ET36
Trabajo Práctico de Redes (Año 2025)
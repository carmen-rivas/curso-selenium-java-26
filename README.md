# Curso Selenium con Java — Proyecto de muestra

Este repositorio es el proyecto de automatización que se construye a lo largo del curso **Selenium con Java**, módulo a módulo. No es un ejemplo aislado: cada carpeta, cada clase y cada línea del `pom.xml` corresponde a una decisión tomada y explicada en algún punto del curso. Este README documenta esas decisiones para que puedas usarlo como referencia mientras avanzás, o para orientarte si llegás a este repo sin haber visto todavía todos los módulos.

## Qué se está probando

La aplicación bajo prueba es una tienda demo de OpenCart, alojada por Abstracta:
https://opencart.abstracta.us/


Es un sitio público, pensado para práctica — los tests de este proyecto interactúan con búsqueda de productos, login, registro, carrito y checkout de esa instancia real.

## Stack y versiones

| Dependencia | Versión | Por qué esta versión |
|---|---|---|
| Java | 25 | LTS más reciente al momento del curso. El material original recomendaba 17; este proyecto usa 25 porque es la versión que tenía instalada el entorno donde se construyó. Si usás 17, ajustá `maven.compiler.source`/`target` en el `pom.xml` — el resto del proyecto no depende de una versión específica. |
| Selenium Java | 4.44.0 | Incluye Selenium Manager (no hace falta descargar drivers a mano) y las features de Selenium 4 (Relative Locators, CDP, nueva gestión de ventanas) que se usan en el Módulo 2 y 3. |
| TestNG | 7.11.0 | Framework de testing: anotaciones, `@DataProvider`, `groups`, suites XML. |
| Allure + `allure-testng` | 2.35.3 | Reportes visuales, steps, categorización de fallos (Módulo 7). |
| AspectJ Weaver | 1.9.25.1 | Requerido para que Allure pueda tejer (`weave`) las anotaciones `@Step`/`@Attachment` en tiempo de ejecución. **Importa la versión**: releases viejos de AspectJ no reconocen el bytecode que genera un JDK reciente (Java 25 genera "class file major version 69"); si actualizás el JDK, puede que también necesites subir esta versión. |
| Maven Surefire | **3.5.6** | Ver la sección dedicada más abajo — esta versión no es la más reciente, y es así a propósito. |

## Estructura del proyecto

curso-selenium-java/
├── .github/workflows/
│ └── smoke.yml # CI: corre el grupo "smoke" en cada ejecución manual/PR
├── src/
│ ├── main/resources/
│ └── test/
│ ├── java/
│ │ ├── com/abstracta/cursoselenium/
│ │ │ ├── pages/ # Page Objects — un archivo por pantalla
│ │ │ │ ├── components/ # Page Component Objects — fragmentos repetidos entre pantallas
│ │ │ │ │ ├── BaseComponent.java
│ │ │ │ │ └── TopBarComponent.java
│ │ │ │ ├── BasePage.java
│ │ │ │ ├── HomePage.java
│ │ │ │ ├── LoginPage.java
│ │ │ │ ├── MiCuentaPage.java
│ │ │ │ ├── ProductoPage.java
│ │ │ │ └── ResultadosBusquedaPage.java
│ │ │ ├── tests/ # Clases de test — nunca contienen un locator suelto
│ │ │ │ ├── BaseTest.java
│ │ │ │ ├── BusquedaDataDrivenTest.java
│ │ │ │ ├── BusquedaProductoTest.java
│ │ │ │ ├── ComponenteTopBarTest.java
│ │ │ │ ├── FlujosCompletosTest.java
│ │ │ │ ├── HomePageTest.java
│ │ │ │ ├── LoginBifurcacionTest.java
│ │ │ │ ├── LoginTest.java
│ │ │ │ ├── ProductoTest.java
│ │ │ │ └── TiposDeFalloTest.java
│ │ │ └── utils/ # Infraestructura: configuración y creación del driver
│ │ │ ├── ConfigReader.java
│ │ │ └── DriverFactory.java
│ │ ├── EstrategiasLocalizacionTest.java # ()
│ │ ├── FormularioComplejoTest.java # ()
│ │ ├── PrimerTest.java # ()
│ │ ├── RegistroLocalizadoresTest.java # ()
│ │ ├── TimingLoginTest.java # ()
│ │ └── VentanasYAlertsTest.java # ()
│ └── resources/
│ ├── allure.properties
│ ├── categories.json
│ ├── config.properties
│ └── config-ci.properties
├── .gitignore
├── pom.xml
├── smoke.xml # Suite de TestNG usada por Surefire (suiteXmlFiles)
└── suite.xml # Suite completa del proyecto


**(\*) Sobre los archivos en el paquete default:** los seis archivos sueltos, directo bajo `src/test/java` sin ningún paquete, son prácticas de los primeros módulos del curso (2.x–3.x), de cuando el proyecto todavía no tenía Page Object Model ni una estructura de paquetes definida. Se dejaron así a propósito — migrarlos a `com.abstracta.cursoselenium.tests` es válido y queda como ejercicio para quien quiera practicar una refactorización real, pero no es necesario para que el proyecto funcione: Surefire los descubre y los corre igual, estén en el paquete que estén.

## Decisiones de diseño, explicadas

### Por qué no hay `PageFactory`

`PageFactory` está deprecado en Selenium 4. Sus campos `@FindBy` se resuelven como proxies perezosos, sin ningún punto donde insertar una espera explícita antes de esa resolución — el mismo problema estructural de fondo que hace que `Thread.sleep()` sea una mala estrategia de sincronización. Todos los Page Objects de este proyecto usan campos `By` privados, resueltos explícitamente con `driver.findElement()`/`findElements()` dentro de métodos que sí pueden esperar con `WebDriverWait`.

### `BasePage` y `BaseComponent`

`BasePage` centraliza `driver`, `wait`, y los métodos de interacción comunes (`escribirEn`, `clickEn`, `esperarVisible`, `esperarClickeable`, `obtenerTexto`) — cada Page Object hereda de ella. `BaseComponent` es su equivalente para fragmentos de UI que se repiten entre pantallas (como la barra superior): en vez de recibir un `WebDriver` completo, recibe un `WebElement root` y busca todo *dentro* de ese elemento — así, si el mismo componente aparece en cinco pantallas distintas, cada instancia solo ve su propio fragmento del DOM, nunca el resto de la página.

### Navegación fluida entre Page Objects

Cada Page Object de entrada (`LoginPage`, `HomePage`) tiene un método estático `abrir(driver, wait, baseUrl)` que encapsula la URL — un test nunca hace `driver.get(...)` directo. Los métodos de acción retornan el Page Object al que navegan (`HomePage.buscar()` retorna `ResultadosBusquedaPage`), de modo que un cambio en el flujo de la aplicación se detecta al compilar, no en tiempo de ejecución.

### `BASE_URL` no es una constante estática

En `BaseTest`, `BASE_URL` es un campo de instancia (`protected String BASE_URL`), poblado en cada `setUp()` desde `ConfigReader.getBaseUrl()` — no un `static final`. Esto permite que cada corrida (local, CI, contra otro ambiente) use una URL distinta sin tocar código, manteniendo el mismo nombre de campo que ya usaban todos los tests desde antes de esta migración.

### `ConfigReader` — precedencia de configuración

system property > archivo .properties > valor por defecto


`ConfigReader` resuelve `baseUrl`, `browser` y `timeout` con esa precedencia, leyendo `config.properties` (local) o `config-ci.properties` (cuando corrés con `-Denv=ci`). Esto permite, por ejemplo, correr en modo headless sin editar ningún archivo:
```bash
mvn test -Dbrowser=chrome-headless
```

### `DriverFactory` — creación centralizada del driver

Un único punto (`DriverFactory.crearDriver(browser)`) decide qué `WebDriver` instanciar, con soporte para Chrome y Firefox, cada uno con su variante headless. Firefox está incluido como segundo caso para mostrar el patrón completo del `switch`, aunque el proyecto solo usó Chrome activamente durante el curso.

### Grupos de TestNG y filtrado de ejecución

Los tests están etiquetados con `groups` (`smoke`, `regresion`, y un dominio secundario como `login`/`busqueda`/`homepage`). Esto permite correr subconjuntos específicos:
```bash
mvn test -Dsurefire.suiteXmlFiles=smoke.xml   # solo los tests marcados como smoke
mvn test -Dgroups=regresion -DexcludedGroups=busqueda
```

### Allure — reportes, steps y categorización

- `@Step` en los métodos de negocio de los Page Objects (nunca en los helpers técnicos de `BasePage`).
- Screenshot automático on-failure, capturado en `BaseTest.tearDown()` antes de `driver.quit()`, siempre dentro de un `try/catch`.
- `@Epic`/`@Feature`/`@Story`/`@Severity` organizan el reporte por funcionalidad de negocio (pestaña "Behaviors"), no por clase.
- `categories.json` clasifica los fallos en categorías propias (bug de aplicación, selector roto, timeout, infraestructura) en vez de depender solo de los defaults de Allure.

```bash
mvn clean test
mvn allure:serve
```

### CI — GitHub Actions

`.github/workflows/smoke.yml` corre el grupo `smoke` en modo headless, genera el reporte de Allure y lo publica como artefacto descargable, sin importar si los tests pasaron o fallaron (`if: always()`). Se dispara manualmente (`workflow_dispatch`) o en Pull Requests contra `main`.

## Sobre la versión de Surefire — por qué 3.5.6 y no 3.6.0

Este proyecto migró en algún momento a **Surefire 3.6.0**, la versión más reciente al momento del curso, que unifica la ejecución de TestNG/JUnit bajo un único motor (JUnit Platform). La migración en sí funcionó: el filtrado por `groups`/`excludedGroups` sin necesidad de un `suiteXmlFiles` fijo en el `pom.xml` corrió correctamente según el propio resumen de Surefire.

Pero al generar el reporte de Allure sobre esa misma corrida, aparecieron **muchos más tests de los que realmente se habían filtrado** — con timestamps y duraciones reales, no vacíos. El diagnóstico: `allure-testng` es un *listener* que se conecta directo a la API interna de TestNG, no al resumen que Surefire imprime en consola. El nuevo motor de Surefire 3.6.0 no le comunica correctamente a ese listener cuáles tests fueron seleccionados por el filtro de grupos, aunque Surefire sí lo sepa para sí mismo.

Es una incompatibilidad real entre dos herramientas de terceros, en una versión de Surefire liberada muy recientemente — no algo que se resuelva ajustando este proyecto. Por eso el `pom.xml` quedó fijado en **3.5.6**, con el filtrado hecho vía `suiteXmlFiles` (`smoke.xml`/`suite.xml`) en vez de `-Dgroups` directo — el mecanismo que sí funciona correctamente en todas las capas, reporte incluido.

**La lección, más que el número de versión:** un `BUILD SUCCESS` y un conteo de tests correcto en la consola no garantizan que *todo* lo que depende de esa corrida esté bien — en este caso, el artefacto que un equipo realmente termina mirando (el reporte) contaba una historia distinta a la de la consola. Vale la pena confirmar el resultado final, no solo el primer número que aparece.

## Cómo correr el proyecto

```bash
# Suite completa
mvn test

# Solo smoke tests
mvn test -Dsurefire.suiteXmlFiles=smoke.xml

# En modo headless, contra el ambiente de CI
mvn test -Denv=ci

# Con un browser u override puntual
mvn test -Dbrowser=chrome-headless

# Reporte de Allure
mvn clean test
mvn allure:serve
```

## Revisión asistida por IA

El proyecto se apoya en **Tero**, el framework de Abstracta para agentes de IA orientados a QA, con un agente configurado específicamente para este curso: un checklist de estándares propio (estructura, locators y esperas, calidad de tests, reportes, entrega) y acceso directo a este repositorio en GitHub.

El modo de trabajo por defecto de ese agente no es recibir código pegado en un chat — es **leer los archivos reales directamente desde el repo**, en la rama que se le indique, y aplicar el checklist sobre esa fuente. Esto importa porque valida lo que efectivamente quedó público, no una copia ni una intención: si algo se pusheó distinto de lo que se creía haber corregido, el agente lo va a leer tal como está en GitHub.

Un hallazgo de Tero es una hipótesis a evaluar, no una corrección automática — la decisión final sobre cada uno sigue siendo de quien automatiza. El detalle completo de este flujo, incluido el system prompt del agente, está en el Módulo 8 del curso.

## Estado del proyecto

Este repositorio refleja el proyecto tal como queda al completar el Módulo 8 del curso — Fundamentos, Localizadores, Interacción, TestNG, Page Object Model, Data-Driven Testing, Reportes y el proyecto en el mundo real (Git, configuración externalizada, CI/CD, mantenimiento de la suite). Cada decisión documentada acá tiene su explicación completa, con el razonamiento y las alternativas consideradas, en el módulo correspondiente del material del curso.
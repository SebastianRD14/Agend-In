# Agend-In 📱

## Aplicación Android de Gestión de Actividades y Calendario

### 📋 Descripción del Proyecto

Agend-In es una aplicación Android desarrollada en Java que integra múltiples conceptos avanzados de desarrollo móvil para crear una solución completa de gestión de actividades y calendario. La aplicación implementa desde fragmentos y navegación hasta servicios, bases de datos y funcionalidades multimedia.

---

## 🎯 Requerimientos Técnicos

### **Especificaciones del Sistema**

-  **Plataforma**: Android
-  **Lenguaje**: Java
-  **API Mínima**: Android API 28 (Android 9.0)
-  **API Target**: Android API 34 (Android 14)
-  **Herramienta de Build**: Gradle

### **Arquitectura y Patrones**

-  Patrón MVC (Model-View-Controller)
-  Arquitectura por capas
-  Uso de FragmentManager y Navigation Component

---

## 📚 Implementación de Temas Académicos

### **Tema 4: Fragmentos, Flujo Maestro-Detalle y Menú**

-  ✅ **Fragmentos dinámicos** para navegación entre pantallas
-  ✅ **Flujo maestro-detalle** para lista de actividades y detalles
-  ✅ **Menú de opciones** con ActionBar y DrawerLayout
-  ✅ **Navigation Drawer** para acceso rápido a secciones

### **Tema 5: Elementos de Interfaz Gráfica**

-  ✅ **RecyclerView** para listas de actividades
-  ✅ **CardView** para presentación de tareas
-  ✅ **FloatingActionButton** para agregar actividades
-  ✅ **Toolbar** personalizada
-  ✅ **BottomNavigationView** para navegación principal

### **Tema 6: Más sobre Interfaz Gráfica**

-  ✅ **Material Design Components**
-  ✅ **Layouts adaptativos** (ConstraintLayout)
-  ✅ **Themes** y estilos personalizados
-  ✅ **Animations** y transiciones fluidas
-  ✅ **Custom Views** para calendario

### **Tema 7: Transiciones**

-  ✅ **Shared Element Transitions** entre fragmentos
-  ✅ **Activity Transitions** con animaciones personalizadas
-  ✅ **Fragment Transitions** para navegación fluida
-  ✅ **Reveal Animations** para elementos emergentes

### **Tema 8: Uso de Aplicaciones Externas**

-  ✅ **Intent implícitos** para compartir actividades
-  ✅ **Integración con calendario del sistema**
-  ✅ **Envío de emails** con detalles de tareas
-  ✅ **Compartir en redes sociales**

### **Tema 9: Emisiones, Hilos y Servicios**

-  ✅ **AsyncTask** para operaciones en segundo plano
-  ✅ **Handler y Looper** para UI threading
-  ✅ **ExecutorService** para manejo de hilos
-  ✅ **LiveData** para observación de datos

### **Tema 10: Servicios**

-  ✅ **Foreground Service** para recordatorios persistentes
-  ✅ **IntentService** para sincronización de datos
-  ✅ **JobScheduler** para tareas programadas
-  ✅ **AlarmManager** para notificaciones temporizadas

### **Tema 11: Bases de Datos**

-  ✅ **Room Database** como ORM principal
-  ✅ **SQLite** para almacenamiento local
-  ✅ **Entities, DAOs y Database** bien estructurados
-  ✅ **Migraciones** de base de datos
-  ✅ **Repository Pattern** para acceso a datos

### **Tema 12: Multimedia**

-  ✅ **Grabación de audio** para notas de voz en actividades
-  ✅ **Captura de imágenes** para adjuntar a tareas
-  ✅ **MediaPlayer** para reproducir recordatorios de audio
-  ✅ **Galería de imágenes** integrada

### **Tema 13: Mapeo**

-  ✅ **Google Maps Integration** para ubicación de eventos
-  ✅ **Geolocalización** para actividades basadas en ubicación
-  ✅ **Marcadores personalizados** en mapa
-  ✅ **Rutas y direcciones** a eventos

### **Tema 14: Sensores**

-  ✅ **Acelerómetro** para detección de movimiento
-  ✅ **Sensor de proximidad** para pausar notificaciones
-  ✅ **Sensor de luz** para modo automático día/noche
-  ✅ **Giroscopio** para interacciones gestuales

### **Tema 15: Publicación en Google Play**

-  ✅ **App Bundle** optimizado para distribución
-  ✅ **Signing configs** para release
-  ✅ **Proguard/R8** para ofuscación de código
-  ✅ **Play Console** configuración completa

---

## 🚀 Funcionalidades Principales

### **Core Features**

1. **Gestión de Actividades**

   -  Crear, editar y eliminar tareas
   -  Categorización por importancia y tipo
   -  Fechas de vencimiento y recordatorios

2. **Sistema de Notificaciones**

   -  Notificaciones push inteligentes
   -  Recordatorios basados en ubicación
   -  Snooze y personalización de alertas

3. **Calendario Interactivo**

   -  Vista mensual, semanal y diaria
   -  Navegación fluida entre períodos
   -  Indicadores visuales de carga de trabajo

4. **Multimedia y Adjuntos**
   -  Notas de voz para actividades
   -  Adjuntar imágenes y documentos
   -  Galería integrada de archivos

### **Features Avanzados**

5. **Geolocalización**

   -  Actividades basadas en ubicación
   -  Recordatorios al llegar a un lugar
   -  Integración con Google Maps

6. **Análisis y Reportes**

   -  Estadísticas de productividad
   -  Gráficos de cumplimiento
   -  Exportación de datos

7. **Sincronización**
   -  Backup automático en la nube
   -  Sincronización entre dispositivos
   -  Importar/exportar calendario

---

## 🛠️ Tecnologías y Librerías

### **Core Android**

```gradle
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'androidx.fragment:fragment:1.6.2'
implementation 'androidx.navigation:navigation-fragment:2.7.4'
implementation 'androidx.navigation:navigation-ui:2.7.4'
```

### **Material Design**

```gradle
implementation 'com.google.android.material:material:1.10.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
```

### **Base de Datos**

```gradle
implementation 'androidx.room:room-runtime:2.5.0'
annotationProcessor 'androidx.room:room-compiler:2.5.0'
```

### **Mapas y Ubicación**

```gradle
implementation 'com.google.android.gms:play-services-maps:18.1.0'
implementation 'com.google.android.gms:play-services-location:21.0.1'
```

### **Multimedia**

```gradle
implementation 'androidx.camera:camera-camera2:1.3.0'
implementation 'androidx.camera:camera-lifecycle:1.3.0'
```

---

## 📱 Arquitectura de la Aplicación

### **Estructura de Paquetes**

```
com.srd14.agend_in/
├── ui/
│   ├── activities/         # Actividades principales
│   ├── fragments/          # Fragmentos de UI
│   ├── adapters/          # Adaptadores para RecyclerView
│   └── dialogs/           # Diálogos personalizados
├── data/
│   ├── entities/          # Entidades de Room
│   ├── dao/              # Data Access Objects
│   ├── database/         # Configuración de BD
│   └── repository/       # Repositorios de datos
├── services/             # Servicios en segundo plano
├── utils/                # Utilidades y helpers
├── models/              # Modelos de datos
└── receivers/           # Broadcast Receivers
```

### **Pantallas Principales**

1. **MainActivity** - Pantalla principal con navegación
2. **CalendarFragment** - Vista de calendario interactivo
3. **TaskListFragment** - Lista de actividades
4. **TaskDetailFragment** - Detalles y edición de tareas
5. **SettingsFragment** - Configuraciones de la app
6. **MapFragment** - Integración con mapas
7. **StatisticsFragment** - Análisis y reportes

---

## 🎨 Diseño y UX

### **Principios de Diseño**

-  **Material Design 3** como guía principal
-  **Accesibilidad** completa con TalkBack
-  **Responsive Design** para diferentes tamaños de pantalla
-  **Dark Theme** y modo automático
-  **Animaciones fluidas** y micro-interacciones

### **Paleta de Colores**

-  **Primary**: #6200EE (Material Purple)
-  **Secondary**: #03DAC6 (Material Teal)
-  **Surface**: #FFFFFF / #121212 (Light/Dark)
-  **Error**: #B00020 (Material Red)

---

## ⚙️ Configuración del Proyecto

### **Requisitos de Desarrollo**

-  Android Studio Arctic Fox o superior
-  JDK 11 o superior
-  SDK Manager con API 28-34
-  Google Play Services actualizados

### **Configuración de Build**

```gradle
android {
    compileSdk 34

    defaultConfig {
        applicationId "com.srd14.agend_in"
        minSdk 28
        targetSdk 34
        versionCode 1
        versionName "1.0.0"
    }

    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'),
                         'proguard-rules.pro'
        }
    }
}
```

---

## 🚀 Instalación y Ejecución

### **Pasos para Desarrollo**

1. Clonar el repositorio
2. Abrir en Android Studio
3. Configurar API Keys (Google Maps, etc.)
4. Sincronizar Gradle
5. Ejecutar en dispositivo/emulador

### **Testing**

-  Unit Tests con JUnit
-  Integration Tests con Espresso
-  UI Tests automatizados
-  Performance Testing con Profiler

---

## 🌎 Contexto del Proyecto

### **Problema Identificado**

En la era digital actual, las personas enfrentan una sobrecarga de actividades y responsabilidades que pueden generar:

-  Estrés y ansiedad por la falta de organización
-  Pérdida de productividad por mala gestión del tiempo
-  Olvidos frecuentes de tareas importantes
-  Dificultad para priorizar actividades

### **Solución Propuesta**

Agend-In ofrece una solución integral que combina:

-  Gestión intuitiva de actividades con interfaz moderna
-  Sistema inteligente de notificaciones y recordatorios
-  Integración con servicios externos del sistema
-  Funcionalidades multimedia para enriquecer las tareas
-  Análisis de productividad personal

---

## 👥 Beneficiarios

-  **Estudiantes universitarios** que necesitan organizar proyectos y exámenes
-  **Profesionales** que manejan múltiples responsabilidades
-  **Freelancers** que requieren gestión eficiente de proyectos
-  **Familias** que coordinan actividades y eventos
-  **Equipos de trabajo** que necesitan sincronización de tareas

---

## 📈 Roadmap de Desarrollo

### **Fase 1: Fundamentos (Temas 4-7)**

-  Implementación de fragmentos y navegación
-  Diseño de interfaz con Material Design
-  Sistema de transiciones y animaciones

### **Fase 2: Integración (Temas 8-10)**

-  Conexión con aplicaciones externas
-  Implementación de servicios en segundo plano
-  Sistema de hilos y manejo asíncrono

### **Fase 3: Datos y Multimedia (Temas 11-12)**

-  Base de datos Room completa
-  Funcionalidades multimedia integradas
-  Sistema de backup y sincronización

### **Fase 4: Avanzado (Temas 13-15)**

-  Integración con mapas y sensores
-  Optimización para publicación
-  Testing y deployment

---

## 📄 Licencia

Este proyecto es desarrollado con fines académicos para la materia de Desarrollo de Aplicaciones Móviles.

---

## 👨‍💻 Desarrolladores

**SebastianRD14** - Desarrollo y arquitectura
**Lalopro23** - Desarrollo

---

_Proyecto desarrollado como integración de conocimientos del primer y segundo parcial de Desarrollo de Aplicaciones Móviles, implementando los temas 4 al 15 del plan de estudios._

## Planteamiento del proyecto 📕

### 1. Contexto 🌎

A lo largo de los años ha existido un cambio en el mundo desde que se introdujo la revolución tecnológica de los smartphones y todas las nuevas tecnologías que están surgiendo, esto ha provocado que no solo la forma en la que trabajamos cambiara sino también la forma de estudiar, prepararse y pasar el tiempo libre.

### 2. Problema ⚠️

Gracias a estos cambios las personas tienen que preocuparse por más actividades que realizar en comparación a anteriores décadas y esto puede ser abrumador para ciertas personas si no tienen la costumbre de anotar o revisar su calendario. Esto puede provocar muchos problemas en el ámbito laboral o escolar ya que normalmente la puntualidad y la calidad están muy presentes hoy en día y fallar en alguna de las dos puede ser desfavorable ante la competencia.

### 3. Justificación 🎯

El no tener un orden y control de las actividades que se tienen que realizar puede provocar:

-  Estrés y ansiedad al tener una gran cantidad de actividades y sentir que no avanzas en ellas.
-  Errores y olvidos en las tareas ya que no se da el tiempo necesario para revisar cada una.
-  Provoca una mala reputación debido a la falta de cumplimiento, cosa que puede afectar la percepción de las personas ya sea compañeros de trabajo o amigos.
-  Baja productividad al perder el tiempo en otras actividades que no son importantes en ese momento.

### 4. Propuesta de solución 💡

La propuesta pensada es diseñar una aplicación móvil la cual será una aplicación tipo calendario donde se notificará de las tareas y, de acuerdo a la importancia de cada una, mostrará más notificaciones que el resto. La idea también es que en cada actividad guardada se pueda almacenar alguna información importante que sea relevante para la actividad. Además, que sea una aplicación sencilla y que siempre esté lista para agregar actividades, ya que muchas veces estas aplicaciones tardan varios pasos para agregar actividades. Otro punto es que, al ser una aplicación móvil, la mayoría de personas pueda usarla comúnmente ya que los dispositivos móviles hoy en día son casi indispensables.

### 5. Alcance 📱

La aplicación incluirá:

-  3 o 4 pantallas (asignación de actividades, calendario, actividad, editar actividades).
-  Posibilidad de agregar y quitar actividades siempre que se quiera.
-  Notificaciones automáticas dependiendo de la importancia de las tareas.
-  Opción de almacenar información en cada actividad.

**Limitaciones iniciales:** ⛔

-  No se incluirá una versión para iOS.
-  No tendrá funciones avanzadas como la nube con otros calendarios.
-  No se desarrollarán funciones con inteligencia artificial.
-  No tendrá muchas opciones de personalización.

### 6. Beneficiarios 👥

-  **Estudiantes**: especialmente universitarios que ya empiezan a tener más responsabilidades con su tiempo.
-  **Oficinistas**: que necesiten apuntar trabajos repentinos que no tenían planeados o recordar eventos importantes fuera del trabajo que se puedan olvidar.
-  **Personas con muchas responsabilidades**: personas que tienen muchos trabajos o tienen que cuidar de familiares, brindarles apoyo o simplemente necesitan organizar su tiempo de mejor manera.
-  **Trabajadores autónomos**: que necesitan organizar sus encargos o trabajos de tal manera que puedan tener de manera clara y a la mano qué deben priorizar antes.
-  **Desarrolladores y programadores**: que necesiten organizar qué funciones en sus códigos desean implementar en ciertos días para que no consuman más tiempo del debido.

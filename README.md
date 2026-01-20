# GoldenRoseAPK

Aplicación móvil teórica para la compra de skins de Valorant. El proyecto ahora funciona en modo local para desarrollo, usando datos almacenados en el dispositivo en lugar de consumir APIs externas.

## Datos locales actuales
- **Catálogo de productos**: se carga desde `app/src/main/assets/products.json` y se renderiza usando imágenes locales (`android.resource://`).
- **Usuarios**: se guardan en SQLite (Room) para simular registro e inicio de sesión sin depender de servicios externos.
- **Órdenes**: se guardan en archivos JSON dentro del almacenamiento interno de la app para simular compras (pendiente migración a SQLite).
- **Boletas**: se guardan en SQLite (Room) para mantener el historial de comprobantes de compra.

## Configuración Valorant API (imágenes/video)
Para asociar imágenes de armas desde Valorant API, se incorporó un helper en `app/src/main/java/com/example/golden_rose_apk/config/ValorantApi.kt` con URLs base y utilidades para construir enlaces a imágenes y videos.

- En el seed local (`products.json`), puedes usar el campo `valorantWeaponId` para que el repositorio local genere la URL de imagen desde `https://media.valorant-api.com/weapons/{weaponId}/displayicon.png`.
- Si necesitas videos demo, utiliza `ValorantApi.weaponVideoUrl(weaponId)` en el futuro para obtener `streamedvideo.mp4`.

## Próximos pasos (API externa)
Cuando se vuelva a habilitar el consumo de API, una opción común para contenido de armas/skins de Valorant es el endpoint público de valorant-api.com, que incluye campos para imágenes PNG y videos de demostración (por ejemplo, `displayIcon` y `streamedVideo`).

**Ejemplos útiles:**
- `https://valorant-api.com/v1/weapons`
- `https://valorant-api.com/v1/weapons/skins`

> Nota: esto queda documentado como referencia futura, pero la app actualmente opera 100% en modo local.

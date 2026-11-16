# EMC for Minecraft 26.3 (Aristois Compatibility Port)

Maintained by **Davy** (Updated Nov 16, 2026).

This repository contains the **EMC (Easy Minecraft Client) Framework** ported to **Minecraft 26.3** (`Java 25`, `Fabric Loader 0.20.1`) so Aristois and EMC-based mods run on 26.3.

## Requirements
- **Minecraft**: `26.3`
- **Fabric Loader**: `0.20.1`
- **Java**: JDK `25` (for building and running)

## Installation (Prebuilt Release)
1. Download `EMC-F-v2-latest-26.3.jar` from the [Releases](../../releases) page.
2. Place it in your Minecraft libraries folder:
   ```
   .minecraft/libraries/me/deftware/EMC-F-v2/latest-26.3/EMC-F-v2-latest-26.3.jar
   ```
3. Launch your `26.3-Aristois` profile with Java 25.

## Building from Source
```powershell
$env:JAVA_HOME = "path\to\jdk-25"
.\gradlew.bat build --no-daemon --console=plain
```
The compiled jar is output to `build/libs/EMC-17.0.0.jar`.

## License
EMC is licensed under the [MIT License](LICENSE).

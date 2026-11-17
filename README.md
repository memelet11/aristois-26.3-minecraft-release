# EMC + Aristois for Minecraft 26.3

Maintained by **Davy** (Updated Nov 16, 2026).

This repository contains the **EMC (Easy Minecraft Client) Framework** ported to **Minecraft 26.3** (`Java 25`, `Fabric Loader 0.20.1`) so Aristois runs on 26.3.

## Requirements
- **Minecraft**: `26.3` (run vanilla `26.3` once in the Minecraft Launcher before installing)
- **Fabric Loader**: `0.20.1` (downloaded automatically by the launcher from `26.3-Aristois.json`)
- **Java**: JDK `25` (`javaw.exe` configured in the launcher installation if your default Java is older)

## Installation (Prebuilt Release)
1. Download all three files from the [Releases](../../releases) page:
   - `EMC-F-v2-latest-26.3.jar`
   - `Aristois.jar`
   - `26.3-Aristois.json`
2. Place them into your `.minecraft` directory at these exact paths:
   - **Launcher profile**:
     ```
     .minecraft/versions/26.3-Aristois/26.3-Aristois.json
     ```
   - **EMC Loader**:
     ```
     .minecraft/libraries/me/deftware/EMC-F-v2/latest-26.3/EMC-F-v2-latest-26.3.jar
     ```
   - **Aristois Core Client**:
     ```
     .minecraft/libraries/EMC/26.3/Aristois.jar
     ```
3. Restart the Minecraft Launcher, select the **`26.3-Aristois`** installation, ensure it points to **Java 25** (`javaw.exe`), and click **Play**.

## Building EMC from Source
```powershell
$env:JAVA_HOME = "path\to\jdk-25"
.\gradlew.bat build --no-daemon --console=plain
```
The compiled jar is output to `build/libs/EMC-17.0.0.jar` (install it as `EMC-F-v2-latest-26.3.jar` above).

## License
EMC is licensed under the [MIT License](LICENSE).

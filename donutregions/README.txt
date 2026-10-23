Donut Regions (Fabric, Minecraft 1.21.4, client-side)

BUILD WITHOUT INSTALLING ANYTHING (GitHub)
  Upload this folder to a new GitHub repository. The workflow in .github/workflows/build.yml builds
  it automatically. Open the Actions tab > latest run > Artifacts > "donutregions", and use the jar
  that is NOT named -sources.

BUILD LOCALLY
  1. Install JDK 21.
  2. In this folder run:  gradle build     (or ./gradlew build if you add the Gradle wrapper)
     Easiest wrapper source: generate a template at https://fabricmc.net/develop/template
     and copy its gradlew, gradlew.bat and gradle/ folder here.
  3. Take build/libs/donutregions-1.0.0.jar (NOT the -sources jar) and put it in .minecraft/mods
     together with Fabric Loader and Fabric API.

USE
  - HUD (top-left): region number, area name, your X/Z.
  - Press M: full map with a red dot on your position. M or Esc closes it.
  - Optional keybind "Toggle region HUD" in Controls (unbound by default).
  - In the Nether, coordinates are multiplied by 8 to match the overworld map.

CONFIG
  On first launch the mod creates .minecraft/config/donutregions.yml (HUD on/off, position,
  background, coordinates, Nether scaling). Edit it and restart the game.

OTHER MINECRAFT VERSIONS
  Edit gradle.properties with the numbers from https://fabricmc.net/develop
  (and the "minecraft" line in fabric.mod.json). Newer versions may need small API tweaks,
  e.g. the KeyBinding category constructor.

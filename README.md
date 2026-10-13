# Aether Cosmic Client

A client-side Fabric mod prototype for Minecraft Java **1.21.11**. Press **Right Shift** to open the cosmic interface. The screen includes a starfield, perspective grid, category-specific module lists, left-click toggles, right-click settings, draggable panel, and settings controls.

## Build with GitHub Actions
1. Create a GitHub repository and upload this project.
2. Open **Actions** and enable workflows if GitHub asks.
3. Run **Build Aether Cosmic Client** or push a commit to `main`.
4. Open the successful workflow run and download the `aether-cosmic-client` artifact.

## Local build
Requires JDK 21. Run `gradle build` or use the included Gradle wrapper if you add one via `gradle wrapper`.

## Important prototype notes
- This is a visual client-side interface, not a real Minecraft dimension transfer. The world is covered by the GUI while open; `Escape` closes it.
- The module registry has 110+ unique entries across distinct categories. Many entries are UI prototypes/placeholders and do not claim to implement gameplay functionality yet.
- The UI's state is currently in memory; persistent profile saving and a full 3D world renderer are future implementation steps.
- The project has not been compiled in this environment. The GitHub Actions workflow is included to test it in a real build environment; fix any mapping/API compatibility errors reported by the build.

# XL-Farmer

XL-Farmer is a farming progression Spigot plugin that tracks player experience, unlocks crops and rewards, and integrates with popular skyblock ecosystems. The plugin uses Google Guice for dependency injection and persists player data in either SQLite or MySQL.

## Features
- Farming experience and level progression with configurable maximum level and experience gains
- Reward tiers that can unlock crops, execute commands, and grant fortune bonuses
- GUI menus for skill overview, rewards, and top farmers with pagination support
- Leaderboard data sourced from the database and displayed in configurable formats
- PlaceholderAPI support for exposing player progress in other plugins
- Database abstraction with support for SQLite (default) and MySQL backends

## Requirements
- **Server:** Paper 1.21 or newer
- **Dependencies:** SuperiorSkyblock2, EconomyShopGUI-Premium, CoreProtect, and PlaceholderAPI
- **Database:** SQLite is embedded by default; MySQL 8+ is supported for production deployments

## Configuration
All gameplay settings live in [`config.yml`](src/main/resources/config.yml):
- `max_level`, `exp_per_crop`, and `use_default_formula` control progression pacing and formulas
- `gui` and `crops` sections customize menu layout and crop unlock thresholds
- `rewards` define commands, bonuses, and unlocks granted at each level
- `database` provides connection properties for SQLite or MySQL

## Messages
Every player-facing message is stored in [`messages.yml`](src/main/resources/messages.yml). Update the strings to localize or tailor the plugin to your server style, then reload with `/farma reload` or restart the server to apply your changes.

## Building from source
```bash
mvn clean package
```
The shaded plugin JAR will be produced under `target/XL-Farmer-<version>.jar`.

## Installation
1. Install the required dependency plugins listed above and restart your server to ensure they load correctly.
2. Copy the XL-Farmer JAR from the releases page (or the `target` directory when building from source) into your server's `plugins/` folder.
3. Start the server to generate default configuration and message files.
4. Configure `config.yml`, customize `messages.yml` if desired, and reload with `/farma reload` or restart the server.

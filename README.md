Noturn Project
📜 Overview
Noturn is a suite of plugins developed for a Minecraft server. This project was a commissioned work and is no longer maintained. The plugins are designed to work together to create a unique gameplay experience, with features like a virtual currency, a ranking system, and optimizations for mob and item stacking.

💎 Modules
The Noturn project consists of the following modules:

NoturnGems
Gems Economy: Implements a virtual currency called "Gems."

Mob Drops: Mobs can drop gems upon death.

Item Conversion: Players can convert gem items into currency by right-clicking them.

PlaceholderAPI Support: Integrates with PlaceholderAPI to display gem balances.

Commands:

/gems [player]: Checks the gem balance of a player.

/gems give <player> <amount>: Gives a specified amount of gems to a player.

/gems remove <player> <amount>: Removes a specified amount of gems from a player.

/gems set <player> <amount>: Sets a player's gem balance to a specific amount.

/gems sell: Sells gem items from the player's inventory.

NoturnRanks
Ranking System: Players can rank up using both in-game currency and gems.

Commands on Rank-Up: Executes predefined commands when a player ranks up.

PlaceholderAPI Support: Integrates with PlaceholderAPI to display player ranks.

Commands:

/rankup: Attempts to rank up the player.

Spawners
Custom Spawners: Manages custom spawners in the game world.

Database Integration: Spawner data is stored in a MySQL database.

Caching: Uses a local cache to improve performance.

StackDrops
Item Stacking: Stacks dropped items to reduce server lag.

Customizable Item Names: Displays the stack size in the item's name.

Pickup Handling: Manages item pickups by players and hoppers.

StackMobs
Mob Stacking: Stacks mobs to reduce the number of entities and improve server performance.

Customizable Mob Names: Displays the stack size in the mob's custom name.

Configurable Limits: Allows setting a limit for the number of mobs in a stack.

⚙️ Installation and Configuration
Dependencies: Ensure all required dependencies for each module are installed. These may include Vault and PlaceholderAPI.

Database:

Set up a MySQL database.

Configure the database connection details in the config.yml file for each module that requires it (Gems, Ranks, Spawners).

Configuration:

Review and customize the config.yml files for each module to adjust settings like mob stacking limits, rank requirements, and gem drop rates.

⚠️ Disclaimer
This project was a commission and is no longer actively maintained. The code is provided as-is, and there is no guarantee of future updates or support.

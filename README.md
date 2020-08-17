# Revive
Multiplayer game mode plugin for Minecraft

Creates a less punishing hardcore game mode aimed at multiplayer servers.

Compatiable Minecraft Versions:

- 1.16.1 :heavy_check_mark:
- 1.16.2 :heavy_check_mark:

# Commands
/listres - lists all dead players and their resurrection cost.

/resurrect <player> - resurrects player given you can afford the cost.

/ifidie - shows how much your resurrection cost will be if you were to perm death that instant. (also shows total playtime on the server)

/book - gives you a book containing information about the plugin.

# Death Mechanics
Currently I have it set up that the material used to resurrect the player is diamonds. (Might add a config file later to change this more easily but can be changed now 
if you know what you're doing)

**<ins>First Death:</ins>**

In-game statistics will count this as a death. However, you will spawn back at spawn or a bed if you have one. Your max health will be halved (from 10 hearts to 5).
You can regain these hearts by consuming certain items:

- Golden Apple

Consuming will give you half a heart back (consume 10 to restore all hearts).

- Enchanted Golden Apple

Consuming resets your health back to normal (consume 1 to restore all hearts).

**<ins>Second Death:</ins>**

In-game statistics will also count this as a death. You will be put into spectator mode. Your resurrection cost will be calculated at this time. 
In order to play again someone will need to resurrect you. Keep in mind this will all happen whether you restored your hearts or not.

This cycle will repeat.

# Misc
Resurrection cost is currently calculated from this equation:

cost = (3h + 350) * 0.1

h = hours played on the server rounded down

#

This uses MongoDB. You will need to make a file in Utilites called Mongo.java and initialize your MongoClient.

Example:

```java
package me.perjergersen.revive.Utilities;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class Mongo {
    public static final MongoClientURI uri = new MongoClientURI(
            "<REPLACE WITH MONGO CONNECTION URI>"); // Add mongo connection URI

    public static final MongoClient mongoClient = new MongoClient(uri);
}
```

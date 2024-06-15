# CommandHook
Minecraft plugin for Bukkit API compatible servers. Vanilla selector parsing for non-vanilla commands.

**Supported versions:**
**1.13** up to **1.21**

> **Note:** Plugin is not in active development, only bugfixes and ports to newer MC runtimes are done. <br/><br/>
> That means updates may be slow from version to version as I am no longer playing Minecraft or maintaining any server since 2019.
> I do welcome pull requests and issue notices, though.

## Contact
* Discord: _newage
* Spigot - [_NewAge](https://www.spigotmc.org/members/_newage.106350/)

## Structure
To achieve _easier_ maintainability, the plugin was split into multiple maven modules <br/>
These can be found under _modules_ folder <br/>

| Module   | Description                                       |
|----------|---------------------------------------------------|
| plugin   | Spigot / Paper plugin                             |
| api      | Connection between the plugin and mapping modules |
| provider | Mapping provider for both Spigot and Paper        |

Adding to the modules above, you may configure the provider module to contain at least one or more mappings. <br/>

As of CommandHook 3.0.0, different mappings are available for Spigot runtime and Paper runtime.
These are configurable in provider modules in both [spigot](modules/mapping/spigot/provider) and [paper](modules/mapping/paper/provider) directory

----

### Paper mappings
Since 1.20.5 [Paper no longer relocates CraftBukkit internals](https://forums.papermc.io/threads/important-dev-psa-future-removal-of-cb-package-relocation.1106/) and [switched to mojang mapped runtime](https://forums.papermc.io/threads/paper-velocity-1-20-6.1152/).
That means plugin _should_ be upwards compatible without any work whilst running on Paper based servers.

| Mapping       | Description                               |
|---------------|-------------------------------------------|
| mojang-mapped | Mapping for Paper 1.20.6+ (Mojang-mapped) |


### Spigot mappings

**Note:** As of CommandHook 3.0.0, Only latest version is supported, all other mappings are subject to removal in future versions. 

| NMS      | Description                               |
|----------|-------------------------------------------|
| v1_21_R1 | NMS mapping for Minecraft 1.21            |
| v1_20_R4 | NMS mapping for Minecraft 1.20.5 - 1.20.6 |
| v1_20_R3 | NMS mapping for Minecraft 1.20.3 - 1.20.4 |
| v1_20_R2 | NMS mapping for Minecraft 1.20.2          |
| v1_20_R1 | NMS mapping for Minecraft 1.20   - 1.20.1 |
| v1_19_R3 | NMS mapping for Minecraft 1.19.4          |
| v1_19_R2 | NMS mapping for Minecraft 1.19.3          |
| v1_19_R1 | NMS mapping for Minecraft 1.19   - 1.19.2 |
| v1_18_R2 | NMS mapping for Minecraft 1.18.2          |
| v1_18_R1 | NMS mapping for Minecraft 1.18   - 1.18.1 |
| v1_18    | NMS mapping for Minecraft 1.18   - 1.18.1 |
| v1_17    | NMS mapping for Minecraft 1.17   - 1.17.1 |
| v1_16_R3 | NMS mapping for Minecraft 1.16.4 - 1.16.5 |
| v1_16_R2 | NMS mapping for Minecraft 1.16.2 - 1.16.3 | 
| v1_16_R1 | NMS mapping for Minecraft 1.16.1          |
| v1_15    | NMS mapping for Minecraft 1.15   - 1.15.2 |
| v1_14    | NMS mapping for Minecraft 1.14   - 1.14.4 |
| v1_13_R2 | NMS mapping for Minecraft 1.13.1 - 1.13.2 |
| v1_13_R1 | NMS mapping for Minecraft 1.13            |

Knowing the above, to support newer Spigot version, a new module has to be created. <br/>
However, in comparison to reflection, compile safety is met. No more guesses if field/method exists.

----

## License
[![GPL-3.0 License](https://img.shields.io/github/license/NewAgeCZ/CommandHook?&logo=github)](LICENSE)

All files are licensed under GPL 3.0 license
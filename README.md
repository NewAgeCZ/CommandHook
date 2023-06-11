# CommandHook
Minecraft plugin for Bukkit API compatible servers. Vanilla selector parsing for non-vanilla commands.

**Supported versions:**
**1.13** up to **1.20**

## Contact
* Discord: _NewAge#5267
* Spigot - [_NewAge](https://www.spigotmc.org/members/_newage.106350/)

## Structure
To achieve _easier_ maintainability, the plugin was split into multiple maven modules <br/>
These can be found under _modules_ folder <br/>

| Module | Description                                                                            |
|--------|----------------------------------------------------------------------------------------|
| plugin | Spigot plugin                                                                          |
| api    | Connection between the plugin and NMS mapping                                          |
| nms    | Used as dependency in the _plugin_ module. Contains the NMS mappings (via maven shade) |
| legacy | Reflection mapping used in versions up to _2.0.0_. Unmaintained.                       |

Adding to the modules above, you may configure the nms module to contain at least one or more mappings <br/>
All you need to do is edit the nms module dependencies in pom.xml & NmsMappingSelector class

| NMS      | Description                               |
|----------|-------------------------------------------|
| v1_20_R1 | NMS mapping for Minecraft 1.20            |
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

Knowing the above, to support newer version, a new module has to be created. <br/>
However, in comparison to reflection, compile safety is met. No more guesses if field/method exists.

## License
[![GPL-3.0 License](https://img.shields.io/github/license/NewAgeCZ/CommandHook?&logo=github)](LICENSE)

All files are licensed under GPL 3.0 license
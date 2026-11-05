# S-Lib Test Mod

The test mod for [S-Lib](https://github.com/Stalemated/s-lib). Its configs and screens use the whole S-Lib config API, and it adds item tooltips built on `TargetMatcherFactory` and a client mixin that only applies when YACL is loaded (`ConditionalMixinPlugin`).
Built on [Stalemated's Stonecutter template](https://github.com/Stalemated/template-mod).

## Versions

| Loader       | 1.20.1 | 1.21.1 | 26.3 |
|:-------------|:------:|:------:|:----:|
| **Fabric**   |   ✅   |   ✅   |  ✅  |
| **Forge**    |   ✅   |   ❌   |  ❌  |
| **NeoForge** |   ❌   |   ✅   |  ✅  |

---

## Requirements

- JDK 25 to run Gradle.
- S-Lib in your mavenLocal, because `gradle.properties` sets `slibLocal=true`. Publish it first with `./gradlew publishToMavenLocal` in the S-Lib repository.

## Running

```bash
./gradlew :1.21.1-neoforge:runClient
```

```bash
./gradlew :1.21.1-neoforge:runServer
```

```bash
./gradlew build
```

- Each node runs in `run/<node>`. The client and the server of a node share that folder, so they also share the config files in `run/<node>/config/s-lib_test_mod/`: `config.json5` (synced) and `config_local.json5` (local).
- `build` builds every node and runs `TestModConfigScreenTest` on each one. The test builds both screens and checks the lang file.
- `-PnoYacl` starts a run without YACL, Mod Menu, and the Forge parsers:

```bash
./gradlew :1.20.1-forge:runClient -PnoYacl
```

- The sources in git are written for `1.20.1-fabric`. To work on another node, run `./gradlew "Set active project to <node>"`, and `./gradlew "Reset active project"` before committing.

### Dedicated server

1. Run `./gradlew :<node>:runServer` once. It stops after writing `run/<node>/eula.txt` and `run/<node>/server.properties`.
2. Set `eula=true` in `eula.txt` and `online-mode=false` in `server.properties`. Development clients have no Mojang account, so the server has to accept offline players.
3. Run `runServer` again, start `runClient` for the same node, and join `localhost`.

## Tabs

The config screen is "S-Lib Test Mod Config".

| Tab                       | Builder call                                                     | What it shows                                                                                                                                                                                                                                                              |
|---------------------------|------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Synced Config             | `category(synced, "synced", ...)`                                | Groups made with `include` (primitives, adapters), `configureOption` (Player Profile UUID is disabled), `listOption`, `listElement` (First Status Indicator edits `statusIndicators[0]`), `mapPairsOption`, `dropdown` (Client Preferences), `nestedMap` and `nestedList`. |
| Visual Settings           | `nested("visualSettings")`                                       | A nested object whose options use `@EnabledIf`: Enable HUD Overlay needs Test Boolean Toggle from the Synced Config tab, and HUD Scale needs Enable HUD Overlay.                                                                                                           |
| Enchantment Allowed Lists | `nestedMapLists("enchantmentRules", ...)`                        | One "Allowed with <enchantment>" list per enchantment of the game, then the keys of `enchantmentRules`. In 1.21.1 and 26.3 the enchantments come from the world, so outside a world only the map keys are shown.                                                           |
| Custom Tooltips           | `group("tooltip_tools", ...)` and `nestedList("tooltipEntries")` | The Edit First Entry and Add Entry buttons, which open the Tooltip Entry screen (`editListElement`/`addListElement` with `livePreview`), and the tooltip entries.                                                                                                          |
| Local Config              | `autoGenerate`                                                   | The local config, which never syncs.                                                                                                                                                                                                                                       |

## Manual test checklist

Each test names its nodes, the steps, and the expected result. "Log" is the game output in the terminal of the run.

### 1. Loading and the server mixin

- **Nodes:** all 6, plus `runServer` on `26.3-neoforge`.
- **Steps:** start the client and open a singleplayer world. Start the dedicated server of `26.3-neoforge`.
- **Expected:** no crash, and the log has `s_lib_test_mod sees the level loading` when the world loads. The dedicated server starts without errors about client classes (tooltips, screens, the title screen mixin).

### 2. Config button

- **Nodes:** all 6.
- **Steps:** open Mod Menu (Fabric) or the Mods list (Forge, NeoForge) from the title screen and open the config of S-Lib Test Mod.
- **Expected:** "S-Lib Test Mod Config" opens with the 5 tabs of the table above.

### 3. Without YACL

- **Nodes:** `1.20.1-fabric` and `1.20.1-forge`.
- **Steps:**
  - Start the client with `-PnoYacl`.
  - Start it again without the flag.
- **Expected:**
  - With `-PnoYacl` the game starts with no crash, and the log never has `YACL is loaded, so the conditional mixin applied`. On Forge the Config button of the Mods list stays greyed out for the mod. Fabric runs also lose Mod Menu with the flag, so there is no button to check.
  - Without the flag the log has `YACL is loaded, so the conditional mixin applied` each time the title screen opens.

### 4. Sliders and @Format

- **Nodes:** `1.20.1-fabric` and `26.3-neoforge`.
- **Steps:** in Synced Config, group "1. Primitives & Numeric Constraints", drag Slider (Step 0.05), Slider (Step 0.002) and Percentage.
- **Expected:** Slider (Step 0.05) moves in steps of 0.05 from 0 to 1, Slider (Step 0.002) in steps of 0.002 from 0 to 0.1, and their values show no float noise (0.55, not 0.55000001). Percentage shows `75%` and moves in steps of 1.

### 5. @EnabledIf

- **Nodes:** `1.20.1-fabric`.
- **Steps and expected:**
  - Turn off Test Boolean Toggle in Synced Config, without saving, and open Visual Settings. Enable HUD Overlay and HUD Scale are greyed out.
  - Turn Test Boolean Toggle back on and turn off Enable HUD Overlay. HUD Scale is greyed out. Turn Enable HUD Overlay on again and Test Boolean Toggle off: HUD Scale stays greyed out, even though Enable HUD Overlay is on.
  - Test Enum Option starts at Option B, so Test Double is greyed out. Pick Option A or Option C: Test Double is available at once. Option B greys it out again.
  - With Test Boolean Toggle and Enable HUD Overlay on, change HUD Scale, then turn off Enable HUD Overlay before saving. HUD Scale goes back to its saved value. Save and reopen the screen: HUD Scale has the old value, and turning Enable HUD Overlay on does not bring the edit back.

### 6. Item dropdown

- **Nodes:** `1.20.1-fabric` and `26.3-fabric`.
- **Steps:** use Favorite Weapon (group "2. Built-in Type Adapters"), Item Blacklist (Synced Config) and Target (Tooltip Entry screen) with the mouse and with Tab.
- **Expected:**
  - Without focus, an item shows its name and icon. `#minecraft:logs` in Item Blacklist shows in gold with no icon.
  - A click opens the suggestion list and selects the text. Tab only selects the text; typing or Enter opens the list.
  - In the list, Up, Down, and Tab move, and Enter or a click outside picks the entry.
  - Favorite Weapon suggests items only, no `#` tags.
  - The mouse works the same way on `26.3-fabric`.

### 7. Adapters

- **Nodes:** `1.20.1-fabric`.
- **Steps and expected** (group "2. Built-in Type Adapters"):
  - Translucent Accent Color shows `#80FF69B4`. Typing `#40FF0000` applies a red with alpha 64. Text that is not a color turns red and is not applied.
  - Primary Color shows `#4682B4`.
  - Player Profile UUID is greyed out.
  - Spawn Offset shows `0.0,1.5,0.0`. Type `1,2,3` and save: `config.json5` has `spawnOffset` with `x` 1, `y` 2, and `z` 3. Type `abc`: the value is not applied, and the log has `A Vector3f must be written as x,y,z, got 'abc'`.

### 8. List element

- **Nodes:** `1.20.1-fabric`.
- **Steps and expected:**
  - In Synced Config, group "List Elements", change First Status Indicator to, and save. After reopening the screen, the first entry of Status Indicators has the same color, and so does the first `statusIndicators` entry in `config.json5`.
  - Change the first entry of Status Indicators and save. First Status Indicator follows it.

### 9. Enchantment Allowed Lists

- **Nodes:** `1.20.1-fabric`, `1.21.1-fabric` and `26.3-fabric` in a world; `1.21.1-neoforge` and `26.3-neoforge` from the title screen.
- **Steps:** open the tab inside a world (Mods button of the pause menu), and on the other nodes from the title screen.
- **Expected:** inside a world there is one "Allowed with <id>" list for each enchantment of the game, each one once. From the title screen in 1.21.1 and 26.3 the tab opens with no error and shows only `minecraft:mending` and `minecraft:sharpness`. In 1.20.1 it shows every enchantment from the title screen too.

### 10. Tooltip Entry screen

- **Nodes:** `1.20.1-fabric` and `1.21.1-neoforge`.
- **Steps and expected:**
  - In Custom Tooltips, Edit First Entry opens "Tooltip Entry" with Comment / Label "Custom Legendary Item Tooltip". Each change logs `Previewing tooltip entry '<comment>'`. Save: the change is in `config.json5`, and in the entry list once the config screen is opened again.
  - Add Entry opens a new default entry. Saving it adds a third entry to `tooltipEntries`.
  - Change Test Integer in Synced Config without saving. Open Edit First Entry, change Comment / Label, and save there. Go back and save the main screen. Both changes are in `config.json5`: saving the main screen does not undo the entry.
  - Remove every entry from the list and save. After reopening the screen, Edit First Entry is greyed out. Delete `config.json5` afterwards to get the default entries back for the next test.

### 11. Tooltips

- **Nodes:** `1.20.1-fabric`, `1.20.1-forge`, `1.21.1-neoforge` and `26.3-fabric`.
- **Steps and expected:**
  - Hover a diamond sword. Below the vanilla lines it shows "Legendary Blade" in bold gold and "Forged in dragon breath" in gold.
  - In the first entry, set Tooltip Position to TOP, turn on Italic Font and change Border Accent Color, then save. The lines move right under the item name, turn italic, and take the new color, with no restart.
  - Set Target of the first entry to `minecraft:diamond_*` and save. The log has `Skipping tooltip entry '<comment>': 'minecraft:diamond_*' is not a valid target`, and the game keeps running. Set it back to `minecraft:diamond_sword`.
  - Create a world with cheats on, then the data pack `run/<node>/saves/<world>/datapacks/tooltip_test/`:
     - `pack.mcmeta`: `{"pack": {"description": "Tooltip test", "pack_format": 15}}` in 1.20.1, `"pack_format": 48` in 1.21.1, and `{"pack": {"description": "Tooltip test", "min_format": 121, "max_format": 121}}` in 26.3.
     - The tag: `data/s_lib_test_mod/tags/items/tooltip_targets.json` in 1.20.1, `data/s_lib_test_mod/tags/item/tooltip_targets.json` from 1.21 on, with `{"values": ["minecraft:stick"]}`.
  - Run `/reload`. A stick shows "Tagged by a datapack", from the entry with target `regex:#s_lib_test_mod:tooltip_.*`.
  - Change the tag to `minecraft:apple` and run `/reload`. The apple shows the line and the stick no longer does.

### 12. Sync with a dedicated server

- **Nodes:** `1.21.1-neoforge` or `26.3-neoforge`, set up as in [Dedicated server](#dedicated-server).
- **Steps and expected:**
  - Start the client first and note Test Integer. Then set `testInt` to 42 in `run/<node>/config/s-lib_test_mod/config.json5` and start the server. The client does not read the file again, so the two sides hold different values.
  - Join the server. Synced Config shows Test Integer 42. Leave: it shows the old value again, and the file still has 42.
  - Without OP, the Synced Config options are greyed out, and their description ends with "Warning: This server does not let you edit this." Client Preferences, Server Informed Int, Local Client Toggle, Local Double, and the Custom Tooltips entries stay editable.
  - On join, the server log has `<player> sent clientPreferences 'high_performance' and serverInformedInt 100`. Change Server Informed Int and save: the line comes again with the new value.
  - Run `op <player>` in the server console, then disconnect, join again, and reopen the screen. The options are editable. Change Test Integer and save: the server writes the new value to `config.json5`.
  - In a singleplayer world opened to LAN (Esc, Open to LAN), the host can edit every Synced Config option, with no warning.

### 13. Old key in the config file

- **Nodes:** `1.20.1-fabric`.
- **Steps:** close the game, rename `testString` to `legacyString` in `config.json5`, give it the value, and start the game.
- **Expected:** Test Text String shows `Old value`, and the file has `testString` again, with no `legacyString`.

### 14. Run folder per node

- **Nodes:** two of them, for example `1.20.1-fabric` and `1.21.1-neoforge`.
- **Steps:** run both clients and change a value in each one.
- **Expected:** each node has its own `run/<node>` with its own `config/s-lib_test_mod/`, logs and saves, and a change in one node does not show up in the other.

## License

[MIT](LICENSE)

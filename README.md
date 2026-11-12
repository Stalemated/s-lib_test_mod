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
| Custom Tooltips           | `group("tooltip_tools", ...)` and `nestedList("tooltipEntries")` | The Edit First Entry and Add Entry buttons, which open the Tooltip Entry screen (`editListElement`/`addListElement` with `livePreview`), and the tooltip entries, with S-Lib's own Add Entry under the list title and Remove Entry in each entry.                          |
| Local Config              | `autoGenerate`                                                   | The local config, which never syncs.                                                                                                                                                                                                                                       |

## Manual test checklist

Each test names its nodes, the steps, and the expected result. "Log" is the game output in the terminal of the run.

### 1. Loading and the server mixin

- **Nodes:** all 6, plus `runServer` on `26.3-neoforge`.
- **Steps:** start the client and open a singleplayer world. Start the dedicated server of `26.3-neoforge`.
- **Expected:** no crash, and the log has `s_lib_test_mod sees the level loading` when the world loads. The dedicated server starts without errors about client classes (tooltips, screens, the title screen mixin). At startup the log has neither `Synced config changed` nor `Local config changed`: each one only shows up after a change to that config is saved.

### 2. Config button

- **Nodes:** all 6.
- **Steps:** open Mod Menu (Fabric) or the Mods list (Forge, NeoForge) from the title screen and open the config of S-Lib Test Mod.
- **Expected:** "S-Lib Test Mod Config" opens with the five tabs of the table above. On `1.21.1-fabric` and `1.21.1-neoforge`, scrolling the screen with the mouse wheel leaves no `Legacy list scrolled` line in the log.

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
  - Turn off Test Boolean Toggle in Synced Config without saving and open Visual Settings. Enable HUD Overlay and HUD Scale are greyed out.
  - Turn Test Boolean Toggle back on and turn off Enable HUD Overlay. HUD Scale is greyed out. Turn Enable HUD Overlay on again and Test Boolean Toggle off: HUD Scale stays greyed out, even though Enable HUD Overlay is on.
  - Test Enum Option starts at Option B, so Test Double is greyed out. Pick Option A or Option C: Test Double is available at once. Option B greys it out again.
  - With Test Boolean Toggle and Enable HUD Overlay on, change HUD Scale, then turn off Enable HUD Overlay before saving. HUD Scale goes back to its saved value. Save and reopen the screen: HUD Scale has the old value, and turning Enable HUD Overlay on does not bring the edit back.

### 6. Item dropdown

- **Nodes:** `1.20.1-fabric` and `26.3-fabric`, plus `1.21.1-fabric` and `26.3-neoforge` for the click checks.
- **Steps:** use Favorite Weapon (group "2. Built-in Type Adapters"), Item Blacklist (Synced Config) and Target (Tooltip Entry screen) with the mouse and with Tab.
- **Expected:**
  - Without focus, an item shows its name and icon. `#minecraft:logs` in Item Blacklist shows in gold with no icon.
  - On `26.3-fabric`, opened from the title screen before entering any world, items show their name without an icon. Enter a world and reopen the screen: the icons are there.
  - A click opens the suggestion list and selects the text. Tab only selects the text; typing or Enter opens the list.
  - In the list, Up, Down, and Tab move, and Enter or a click outside picks the entry.
  - Favorite Weapon suggests items only, no `#` tags.
  - Click checks on `26.3-fabric`, `26.3-neoforge` and `1.21.1-fabric`. The three nodes behave the same. Start each check with no field focused and Favorite Weapon showing Netherite Sword, and press Undo when done:
    - Click the text of Favorite Weapon once: the list opens and the whole text is selected. Type `diamond_sw`: every letter goes in and the list narrows to Diamond Sword.
    - With the list still open, click the text again: the list stays open, the selection goes away, and the caret lands under the mouse, not to its left. Click two more times at other places in the text: each click only moves the caret, and the list stays open. Type `x`: it goes in at the caret.
    - Click the text of Favorite Weapon, type `minecraft:light_blue_stained_glass_pane`, press Home, then click a letter near the middle of the text: the caret lands next to that letter.
    - On `1.20.1-fabric`, `1.21.1-fabric` and `26.3-fabric`, with that long id in Favorite Weapon, its text and its underline start where those of Sound Effect ID start, and never cover the option name. Set Favorite Weapon back to Netherite Sword: its name and icon do not reach the option name either, with or without the mouse over the row.
    - With the list open, move the mouse over the text without clicking: the highlighted entry of the list does not change. Press Enter: the highlighted item is picked.
    - Press Tab until Favorite Weapon is selected and type `d`: the list opens and `d` goes in.
    - Press Enter to close the list, then click the text once: the list opens with the whole text selected, as with the first click. Type `d`: the text becomes `d`, and Favorite Weapon does not switch to Netherite Sword.
    - Click the text four times, then type `a` and press Backspace: both go in, without pressing an arrow key first.
    - Change Test Text String to any other text, click its text, then click its reset button: it shows `Hello World!` again.
    - In Item Blacklist, click the text of `minecraft:tnt` once: the list opens and the text is selected. Type `sto`: every letter goes in and the list narrows. Enter picks the highlighted item.
    - Click the text of `minecraft:lava_bucket` twice: the list stays open and the caret moves. Type `x`: it goes in. Backspace deletes it.
    - Click Client Preferences: its list opens. Click its text again: the list stays open. Click an entry: it is picked and the list closes.
    - Click Client Preferences and pick an entry. Click it again and press Down, then Enter: the next entry is picked.
    - Open the list of Favorite Weapon and change the size of the game window: the list closes and the config screen fills the whole window. Do the same with the list of Client Preferences and with the color picker of Primary Color.

### 7. Adapters

- **Nodes:** `1.20.1-fabric`.
- **Steps and expected** (group "2. Built-in Type Adapters"):
  - Translucent Accent Color shows `#80FF69B4`. Typing `#40FF0000` applies a red with alpha 64. Text that is not a color turns red and is not applied.
  - Primary Color shows `#4682B4`.
  - Click the text of Primary Color: the caret shows where you clicked, and typing changes the color. Chat Highlight TextColor works the same way, and a click on the color square still opens the color picker.
  - Change Primary Color and press its reset button: it goes back to `#4682B4`.
  - In Color Palette and in Status Indicators, the remove button of an entry removes it.
  - Repeat the three checks above on `1.21.1-fabric` and `26.3-fabric`.
  - Type `#FFFF69B4` in Translucent Accent Color, open its color picker and drag fast across it while holding the mouse button: the color square stays right next to the text and does not jump left. Check it on `1.20.1-fabric`, `1.21.1-fabric` and `26.3-fabric`.
  - Typing with the color picker open, on `26.3-fabric`, `26.3-neoforge` and `1.21.1-fabric`: click the color square of Primary Color, press End, press Backspace twice, and type `11`. Both characters go in, the text ends in `11`, and the picker shows the new color. Close the picker with a click outside it, open it again from the square, and type once more: the characters still go in. Repeat with the first entry of Color Palette, the first entry of Status Indicators, and the first entry of Gradient Accent Colors in the Tooltip Entry screen (Custom Tooltips, Edit First Entry). Press Undo when done.
  - Player Profile UUID is greyed out.
  - Spawn Offset shows `0.0,1.5,0.0`. Type `1,2,3` and save: `config.json5` has `spawnOffset` with `x` 1, `y` 2, and `z` 3. Type `abc`: the value is not applied, and the log has `A Vector3f must be written as x,y,z, got 'abc'`.
  - Type `1E120,0,0` in Spawn Offset and save: nothing crashes, Spawn Offset shows its saved value again, and the log has `Invalid value for config option 'spawnOffset'`.

### 8. List element

- **Nodes:** `1.20.1-fabric`.
- **Steps and expected:**
  - In Synced Config, group "List Elements", change First Status Indicator to `gold`, and save. After reopening the screen, the first entry of Status Indicators has the same color, and so does the first `statusIndicators` entry in `config.json5`.
  - Change the first entry of Status Indicators and save. First Status Indicator follows it.
  - Change First Status Indicator and press its reset button: it goes back to `#55FF55`.

### 9. Enchantment Allowed Lists

- **Nodes:** `1.20.1-fabric`, `1.21.1-fabric` and `26.3-fabric` in a world; `1.21.1-neoforge` and `26.3-neoforge` from the title screen.
- **Steps:** open the tab inside a world (Mods button of the pause menu), and on the other nodes from the title screen.
- **Expected:** inside a world there is one "Allowed with <id>" list for each enchantment of the game, each one once. From the title screen in 1.21.1 and 26.3, the tab opens with no error and shows only `minecraft:mending` and `minecraft:sharpness`. In 1.20.1 it shows every enchantment from the title screen too. In `1.20.1-fabric`, press `+` on "Allowed with minecraft:mending": the new row is empty. Type `othermod:future_enchantment` in it. Press `+` again and type `Test test`: the capital T and the space do not go in, so the row reads `esttest`. Copy `mine craft:air` from anywhere and paste it into that row: nothing goes in. Press `+` once more, leave the row empty, and save. `allowed` of `minecraft:mending` in `config.json5` has `othermod:future_enchantment` and `minecraft:esttest`, without `minecraft:`, and after the save the empty row is gone. A `minecraft:` that an earlier run left in `config.json5` shows as `minecraft:`. On `26.3-fabric`, click the text of the first row of "Allowed with minecraft:mending", then click it again: typing a letter and Backspace both work, without pressing an arrow key first. Click it a third time: they still work.

### 10. Tooltip Entry screen

- **Nodes:** `1.20.1-fabric` and `1.21.1-neoforge`, plus `1.20.1-forge` and `26.3-fabric` where a step says so.
- **Steps and expected:**
  - Start with the `config.json5` of the earlier runs. If its `allowed` and `restricted` lists only hold ids, it loads without `Using defaults for invalid values in config.json5`. If an earlier run saved text that is not an id there, such as `test test: id test`, that log line names it, and that list of the entry goes back to its default (`minecraft:infinity, minecraft:mending` for Allowed Enchantments, `minecraft:unbreaking` for Restricted Enchantments).
  - In Custom Tooltips, Edit First Entry opens "Tooltip Entry" with Comment / Label "Custom Legendary Item Tooltip". Each change logs `Previewing tooltip entry '<comment>'`. Save: the change is in `config.json5`, and back on the main screen the first entry shows it right away, without reopening the config screen.
  - In Custom Tooltips, Tooltip Text Lines of the first entry shows `Legendary Blade, Forged in dragon breath`, which is two lines, and its description says that commas separate the lines and double quotes keep a comma inside a line. Change it to `"Legendary Blade, Forged in dragon breath"` and save: that entry's `text` in `config.json5` has the single line `Legendary Blade, Forged in dragon breath`, and the field shows `"Legendary Blade, Forged in dragon breath"` again. Then type `"Legendary Blade` without the closing quote and save: the line does not change and the log shows `Ignoring list text`. Gradient Accent Colors of the same entry still separates its colors with plain commas.
  - In Synced Config, under "5. Unrestricted Enchanting Rules (@Nest Map)", open `minecraft:mending`: Allowed Enchantments shows `minecraft:infinity`, and its description says to separate the entries with commas. Change it to `minecraft:infinity, test test: id test, othermod:future_enchantment` and save: `allowed` of `minecraft:mending` in `config.json5` has `minecraft:infinity` and `othermod:future_enchantment`, the field shows `minecraft:infinity, othermod:future_enchantment`, and the log has `Ignoring invalid id 'test test: id test'`.
  - Add Entry opens a new default entry. Save it and go back: Custom Tooltips shows a third entry right away and the tab stays on Custom Tooltips. The entry is in `tooltipEntries` in `config.json5`.
  - Change Test Integer in Synced Config without saving. Open Edit First Entry, change Comment / Label, and save there. Go back: the main screen still shows the changed Test Integer and Save is lit. Save it. Both changes are in `config.json5`: the main screen only writes the options that changed in it, so it does not undo the entry.
  - On `26.3-fabric` and `1.20.1-fabric`, change Test Integer in Synced Config and open Visual Settings without saving: Save Changes, Cancel and Undo are on there too. Press Undo there: Test Integer goes back, and Visual Settings shows Done and Reset. Change Test Integer again, open Local Config and press Save Changes: the change is saved and the screen stays open. Done then closes it.
  - Open Edit First Entry, change Comment / Label, and save there. Back on the main screen, edit the second entry inline in Custom Tooltips and save. Both changes are in `config.json5`: the main screen only writes the entries it changed. Do the same with Add Entry: the added entry stays.
  - In Custom Tooltips, change Comment / Label of the second entry inline to `Renamed inline`. While you type, its title keeps the old comment. Save: the title becomes `#2: Renamed inline` right away and the tab stays on Custom Tooltips.
  - On `1.20.1-fabric`, in Enchantment Allowed Lists, add `minecraft:mending` to Allowed with minecraft:unbreaking and save. Without reopening the config screen, Enchantment Rules in Synced Config has a `minecraft:unbreaking` entry.
  - In Custom Tooltips, press Remove Entry at the end of the second entry. It disappears, the next entry is renumbered, the tab stays on Custom Tooltips, and Save is lit. Press Undo: the entry is back. Remove it again and press Cancel: `config.json5` still has it. Open the screen, remove it again, and save: it is gone from `config.json5`.
  - In Synced Config, under "5. Unrestricted Enchanting Rules (@Nest Map)", open the `minecraft:mending` entry, and scroll down until Test Integer is out of sight. Change Restricted Enchantments of `minecraft:mending` to `minecraft:unbreaking, minecraft:looting` and save: after the save the screen keeps the scroll, without sliding down from the top first, and the same groups are open. Type `minecraft:looting` in New Entry Key and press Add Entry: the scroll and the open groups stay, and the `minecraft:looting` entry shows up collapsed. Press Undo: they still stay. Press Remove Entry at the end of `minecraft:mending`: they stay again. Press Cancel.
  - On `1.20.1-fabric`, scroll Synced Config to the very bottom, change the last option and save: after the save the list is still at the very bottom.
  - In Synced Config, press Remove Entry at the end of `minecraft:sharpness` in Enchantment Rules and save: `enchantmentRules` in `config.json5` no longer has that key.
  - In Synced Config, under "5. Unrestricted Enchanting Rules (@Nest Map)", type `minecraft:fortune` in New Entry Key: Save stays off while you type. Press Add Entry below it. A `minecraft:fortune` entry appears first, since the map is sorted, the tab stays on Synced Config and Save is lit. Its Allowed Enchantments shows `minecraft:infinity, minecraft:mending`, and Restricted Enchantments shows `minecraft:unbreaking`. Press Undo: the entry is gone. Add it again and save: `enchantmentRules` in `config.json5` has `minecraft:fortune`, and Enchantment Allowed Lists shows "Allowed with minecraft:fortune".
  - Press Add Entry with New Entry Key empty, then with `minecraft:mending`. Nothing is added, and the log has `Key of a new 'enchantmentRules' entry must not be blank, got ''` and `Key of a new 'enchantmentRules' entry must not be in the map already, got 'minecraft:mending'`.
  - In Custom Tooltips, press the Add Entry under "6. Custom Tooltip Entries (@Nest List)", not the one in Tooltip Entry Tools. A collapsed `Custom Legendary Item Tooltip` entry appears at the end, numbered one after the last entry, the tab stays on Custom Tooltips and Save is lit. Press Undo: it is gone. Add it again and save: `tooltipEntries` in `config.json5` has one more entry, with the comment `Custom Legendary Item Tooltip`.
  - Repeat the three Add Entry checks above on `1.20.1-forge` and `26.3-fabric`.
  - Use the Add Entry in Tooltip Entry Tools and save there, go back and press Undo: the added entry stays, since it was saved in its own screen.
  - Repeat the Add Entry and Remove Entry checks on `1.20.1-forge` and `26.3-fabric`.
  - Remove every entry with Remove Entry and save. Custom Tooltips shows "No entries", and Edit First Entry is greyed out right away, without reopening the config screen. The Add Entry under "6. Custom Tooltip Entries (@Nest List)" is still there above "No entries"; press it and `#1: Custom Legendary Item Tooltip` appears. Delete `config.json5` afterward to get the default entries back for the next test.

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

- **Nodes:** `1.21.1-neoforge` or `26.3-neoforge`, set up as in [Dedicated server](#dedicated-server). Run the `op` and `deop` steps on both, since each one hooks a different `PlayerList` method.
- **Steps and expected:**
  - Start the client first and note Test Integer. Then set `testInt` to 42 in `run/<node>/config/s-lib_test_mod/config.json5` and start the server. The client does not read the file again, so the two sides hold different values.
  - Join the server. Synced Config shows Test Integer 42. If Test Integer was 50 before joining, the client log has a single `Synced config changed` line on join, with `testInt 42`. Leave: it shows the old value again, and the file still has 42.
  - Without OP, the Synced Config options are greyed out, and their description ends with "Warning: This server does not let you edit this." Client Preferences, Server Informed Int, Local Client Toggle, Local Double, and the Custom Tooltips entries stay editable.
  - On join, the server log has `<player> sent clientPreferences 'high_performance' and serverInformedInt 100`. Change Server Informed Int and save: the line comes again with the new value.
  - Still without OP and with nothing pending, press Reset: Test Integer keeps 42 and stays greyed out, and Server Informed Int goes back to 100. Save: the client log has `Synced config changed: testInt 42`, and the server log has `<player> sent clientPreferences 'high_performance' and serverInformedInt 100`. Leave the screen and open it again: Test Integer still shows 42.
  - Close the screen and run `op <player>` in the server console. Open the screen again without leaving the server: the Synced Config options are editable, and the client log has no new `Synced config changed` line, since no value changed. Change Test Integer and save: the server writes the new value to `config.json5`, and the client log has one `Synced config changed` line with the new value.
  - Close the screen, run `deop <player>`, and open it again: the Synced Config options are greyed out with the warning again.
  - With the screen open and nothing pending, open and close any dropdown or color picker on it, then run `op <player>`: the screen reloads at once and the Synced Config options become editable. The tab, the scroll and the open groups stay as they were. Lists that were closed while greyed out, such as Color Palette, now show open, as when the screen is opened with OP.
  - With the screen open, change Test Integer and Local Client Toggle without saving, then run `deop <player>`: the screen reloads, Test Integer shows the server value again and is greyed out, and Local Client Toggle keeps its pending change, with Save lit.
  - Start a second client with another player name and join the same server, and open Synced Config on it. From the first client, with OP, change Test Integer and save: the second screen shows the new value at once, whether that player has OP or not.
  - In a singleplayer world opened to LAN (Esc, Open to LAN), the host can edit every Synced Config option, with no warning.
  - On `26.3-fabric` and `1.21.1-fabric`, open a world to LAN with commands allowed, join it from a second client with another player name, and open Synced Config on both. Change Test Integer on the second client and save: the host's screen shows the new value within a moment, without leaving the screen. Any pending change on the host stays pending.
  - On `26.3-fabric` or `26.3-neoforge`, in a singleplayer world with Allow Cheats on: Esc, World Options..., set Multiplayer to LAN and press Apply Changes. Join it from a second client with another player name and open Synced Config there: its options are greyed out with the warning. On the host, open World Options..., turn Command Access on and press Apply Changes: the guest's screen reloads at once and its Synced Config options become editable. Turn Command Access off and apply: they are greyed out with the warning again. Turn Allow Cheats off and apply: the guest's options stay greyed out, and the host's log has no error.

### 13. Text in the config file

- **Nodes:** `1.20.1-fabric`.
- **Steps and expected:**
  - Close the game, rename `testString` to `legacyString` in `config.json5`, give it the value `Old value`, and start the game. Test Text String shows `Old value`, and the file has `testString` again, with no `legacyString`.
  - Set Test Text String to the Spanish word for "year" (a, n with tilde, o), paste a grinning face emoji after it, and save. In `config.json5`, `testString` shows the n with tilde and the emoji as typed, with no `\u` escapes. The field may draw the emoji as a box. Restart the game: Test Text String shows the same text.
  - Add the entry `"say \"hi\"": Hello` to Translation Aliases and save. `translationAliases` in `config.json5` has the line `"say \"hi\"": "Hello"`. Restart the game: the log has no `is not valid JSON5` line, and Translation Aliases still has the entry. Remove it and save.

### 14. Run folder per node

- **Nodes:** two of them, for example `1.20.1-fabric` and `1.21.1-neoforge`.
- **Steps:** run both clients and change a value in each one.
- **Expected:** each node has its own `run/<node>` with its own `config/s-lib_test_mod/`, logs and saves, and a change in one node does not show up in the other.

## License

[MIT](LICENSE)

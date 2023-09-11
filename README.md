<p align="center"><img src="https://cdn.discordapp.com/attachments/1067274729205010463/1104640330671861760/image.png" alt="Open Source ROM Library for Pokemon Generation III" />
</p>

# PokéROM Kotlin Library
Welcome to PokéROM Library, a modern library for reading and writing to Pokémon Generation III ROMs. Written in Kotlin, this library will give you the ability to read data such as Pokémon names, weight, moves, evolution data, etc., as well as create sprites, tilesets and more. The intended use of this library is for PokéForce's client to provide users their own method of extracting game data in order to play the MMORPG. 

---

# Usage

#### Get file data

```kotlin

// Load the ROM file, replacing "test" with your roms name
val rom = Rom(file = Paths.get("./rom/test.gba").toFile()).load()

// Pikachu's dex number is 25, so provide 25 as the index 
val pokemonName = rom.definition<String>(type = DefinitionType.PokemonNames, index = 25)

// Print out the Pokemons name
println("I choose you, $pokemonName!")
```

---

# Quick Links

<b>PokéForce Related</b>
- [PokéForce Discord](https://discord.gg/V5YfWmyAqV)
- [PokeCommunity](http://pokecommunity.com/)
- [PokeData](https://github.com/hugmanrique/PokeData) - original source code credits



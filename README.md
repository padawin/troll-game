# Troll game

In this game two players can use trolls to fight each other in a closed world.
This version is the one done during my studies.

## Features

The trolls can move in the world, grab objects (weapons, potions...), equip
objects, hit the other trolls...

## Requirements

The game needs a postgresql database and the jdbc .jar file to use it.

Needed packages:

- libpostgresql-jdbc-java
- postgresql

## Database creation

In Postgres, you need to create a user 'troll' with as password 'Troll' and a database 'trolls':

```
> psql -U postgres
postgres=# CREATE USER troll WITH PASSWORD 'Troll';
CREATE ROLE
postgres=# CREATE DATABASE trollss OWNER troll;
CREATE DATABASE
postgres=# \q
```

Then, with the created user, import the needed tables:

```
> psql -U troll trolls
trolls=> \i ps.sql
trolls=> \i troll.sql
```

## Compilation and running

To compile, just run in the project's root:

```
make
```

Then to run the game:

```
cd bin
java -cp .:/path/to/postgresql/jdbc.jar projet.Start
```

## Future developments

* The code has to be translated in english.
* I also may wish to write it in another language (Python, C or C++ maybe ?)
* And some features don't really work and need to be cleaned up a little...



<p align="center">
    <img src="./assets/img/carecaoke.png" width="400" alt="logo"/>
</p>
<h1 align="center">Carecaokê</h1>

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/grass.png)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Quarkus](https://img.shields.io/badge/-Quarkus-black?style=for-the-badge&logo=quarkus)
![H2 Database](https://img.shields.io/badge/-h2-black?style=for-the-badge&logo=h2&color=darkblue)

[![⚙️ Build Workflow](https://github.com/coppolaop/CarecaokeAPI/actions/workflows/maven.yml/badge.svg)](https://github.com/coppolaop/CarecaokeAPI/actions/workflows/maven.yml)

[![GitHub release](https://img.shields.io/github/release/coppolaop/CarecaokeAPI.svg)](https://github.com/coppolaop/CarecaokeAPI/releases)
[![GitHub license](https://img.shields.io/github/license/coppolaop/CarecaokeAPI.svg)](https://github.com/coppolaop/https://img.shields.io/github/license/CarecaokeAPI.svg/blob/main/LICENSE)

## 📋 Table of Contents

1. 🎙️ [About Carecaokê](#about)
2. 🎉 [What is this API ?](#what-is-this-api)
3. 🎵 [Features](#features)
4. 🔨 [Development mode and Unit testing](#dev-and-tst)
5. 🌿 [Env variables](#env-variables)
6. 🐙 [GitHub Actions](#github-actions)
7. ©️ [License](#license)
8. ❤️ [Contributors](#contributors)

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/grass.png)

## <a name="about">🎙️ About</a>

When singing, some people feel like they are exposing part of their soul. I'm one of those people.
In these moments, there is nothing more comforting than knowing that, in addition to enjoying singing, others enjoyed
listening.
Therefore, Carecaokê aims to not only bring this instant feedback but also enable a fun game between friends, in this
special moment.
Gather your friends, sing, be happy!

> Hey now, you're an all star<br/>
> Get your game on, go play<br/>
> Hey now, you're a rock star<br/>
> Get the show on, get paid<br/>
> And all that glitters is gold<br/>
> Only shooting stars break the mold<br/>
> -- Smash Mouth - All Star

Embrace singing competition!

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/grass.png)

## <a name="what-is-this-api">🎉 What is this API ?</a>

Carecaokê API provides over HTTP requests a way of manage the singing competition.

It's a **Backend** application to support your competition.

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/grass.png)

## <a name="features">🎵 Features</a>

- Managing your party Guests
- Managing all sung songs
- Managing votes
- Generate ratings
- Listing all results
- Controlling list of songs to sing and call the next song
- Preventing singers from voting for themselves
- Allowing guests to manage only their own music and votes

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/grass.png)

## <a name="dev-and-tst">🔨 Development mode and Unit testing

This project uses Quarkus, the Supersonic Subatomic Java Framework.
If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .
We suggest you configure your preferred Java IDE.
Remember that you will also need to configure adapters for the **Quarkus** framework.
For unit tests, we aim to ensure maximum coverage in the **controller** and **service** classes. Run them using your
IDE.

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

### Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

### Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/carecaoke-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

### Related Guides

- Hibernate ORM ([guide](https://quarkus.io/guides/hibernate-orm)): Define your persistent model with Hibernate ORM and
  Jakarta Persistence
- JDBC Driver - H2 ([guide](https://quarkus.io/guides/datasource)): Connect to the H2 database via JDBC
- RESTEasy Reactive ([guide](https://quarkus.io/guides/resteasy-reactive)): A Jakarta REST implementation utilizing
  build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the
  extensions that depend on it.

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/grass.png)

## <a name="env-variables">🌿 Env variables</a>

Environnement variables are available in a secret file at folder
**[resources](https://github.com/coppolaop/CarecaokeAPI/tree/main/src/main/resources)**.
File name must be **secret.properties**.

Environment variables are :

|             Name              |            Description            | Required |
|:-----------------------------:|:---------------------------------:|:--------:|
| `quarkus.datasource.username` | Your database credential username |    ✅     |
| `quarkus.datasource.password` | Your database credential password |    ✅     |

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/grass.png)

## <a name="github-actions">🐙 GitHub Actions</a>

This project uses **GitHub Actions** to automate some tasks and reduce toil.

You can find all the workflows in the
**[.github/workflows directory](https://github.com/coppolaop/CarecaokeAPI/tree/main/.github/workflows).**

### 🎢 Workflows

|                                         Name                                          |             Description & Status              |                      Triggered on                      |    
|:-------------------------------------------------------------------------------------:|:---------------------------------------------:|:------------------------------------------------------:|
| **[⚙️ Build](https://github.com/coppolaop/CarecaokeAPI/actions/workflows/maven.yml)** | Build the application and runs all Unit Tests | `push` on `develop` and all pull requests to `develop` |

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/grass.png)

## <a name="license">©️ License</a>

This project is licensed under the [Apache License, Version 2.0](https://opensource.org/license/apache-2-0).

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/grass.png)

## <a name="contributors">❤️ Contributors</a>

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<a href="https://github.com/timgl"><img src="https://avatars.githubusercontent.com/u/19476398?v=4" width="100" height="100" alt=""/></a>
<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->
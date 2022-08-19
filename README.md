
<p align="center">
<img src="./resources/logo.png">
<h2 align="center"> Real Time Real Estate</h2>
</p>

<p align="center"> An open and free reference implementation of the RTRE concept
. A platform for experimentation with and evaluation of new technologies and <br>research results
. An approach to gradual migration from CAD to BIM
. A simplified process for information managers at FFOs
. A simplified and more inclusive process for architectural firms that an FFO uses
Multiple communication channels to spread knowledge and progress in the field*
</p>


 


###### [* Property Information Management]

---

## 📖 Content

- [🔑 Key Features](#---key-features)
- [⚡ Quick-Setup](#--quick-setup)
  * [🐋 with Docker](#---with-docker)
  * [🔥 Firestore Configuration](#---firestore-configuration)
- [🤖 Dev-Setup](#---dev-setup)
  * [🔒 Requirements](#---requirements)
- [⚠️ License](#---license)
- [⭐ Acknowledgments](#--acknowledgments)

![Alt Text](./resources/main.gif)
---

## 🔑 Key Features 

RTRE application offers multiple features to enhance the development experience using the IFC file format

+ File Management
    - Check-in a file
    - Check out a file
+ Merge functionality
+ Model Viewer
+ Timeline Viewer
+ Project Management
+ Notification
+ User Management
+ Chat System (experimental)
+ Supports 
    - IFC 2x3 
    - IFC 4

## ⚡ Quick-Setup
---
### 🐋 with Docker


First, install Docker and Docker-compose and set it up.

Download the zip file of repository
>🔔 If you're cloning it make sure git config core.autocrlf is set to false
>  ```bash
> git config --list
> git config --global/--system/--local/--worktree core.autocrlf false
> ```

### 🔥 Firestore Configuration
>🔔 you can also follow the instructions in the [wiki](https://github.com/therealtimerealestate/RTRE/wiki/Firestore-setup)

<br>

Create a [Firestore](https://firebase.google.com/?gclid=CjwKCAjwo_KXBhAaEiwA2RZ8hLe4oIrXEhwgYLuZdC5CjJ_NGgIsY9Ajb9lGdqry4v5eg_YZCc9JXBoC7tMQAvD_BwE&gclsrc=aw.ds) Database and acquire the service account key by generating it

 ```bash
 - Go to the console
 - Add project
 - Build
 - Firestore Database (follow instructions)
 - Press the gear ⚙️ icon next To Project Overview
 - Project Settings -> Service accounts
 - Generate a new private key 
 - save as "serviceAccountKey" in ./RTRE/Server
```
>🔔  Make sure before the following step this should be as standard if fresh clone
, in server/fore/rtre/server/Main.java BimPort is set to docker.internal.host:port

```java

public class Main {
	 public static String BimPort = "http://host.docker.internal:8082";
	// public static String BimPort = "http://localhost:8082";

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}

``` 
navigate to ./RTRE and type the following;

```bash
## build server, frontend and Bimserver images
> docker-compose up
```
After the process is done, should be when the spring-boot backend has started these are the following addresses at default:

+ Springboot - http://localhost:3030
+ Bimserver - http://localhost:8082
+ Client - http://localhost:8080
    - Default Admin username = admin@admin.com
    - Default Admin password = password


## 🤖 Dev-Setup
---
### 🔒 Requirements

- Python 3.9
- JDK 8
- IfcOpenshell
- Bimserver
- Maven
- Node.js
- npm
- Firestore

First installing the merge functionality, navigate to [IfcOpenshell](https://github.com/IfcOpenShell/IfcOpenShell/releases/tag/blenderbim-220817) repository and install the blenderbim-py3.9 version

Copy the files from ./blenderbim/libs/site/packages/ and copy the 
```bash 
ifcpatch and ifcopenshell 
```
folders to python3.9 site-packages/dist-packages You can locate the site using the command:
```bash 
python -m site
```

You will also need to connect to a firebase Database, follow instructions in the section [firestore Configuration](#---firestore-configuration)
>🔔 you can also follow the instructions in the [wiki](https://github.com/therealtimerealestate/RTRE/wiki/Firestore-setup)

<br>

Next, either install [Bimserver](https://github.com/opensourceBIM/BIMserver/releases) from its repository version 1.5.182 Or the easier way through docker with an image from [dockerhub](https://hub.docker.com/r/disitlab/bimserver/tags) 


```bash 
## pull image
docker pull disitlab/bimserver:1.5.182
## run bimserver on port 8082
docker run -p 8082:8080 disitlab/bimserver:1.5.182
```

Clone repository and navigate to ./RTRE/frontend/rtre-webbapplication/ and run commands

```bash 
## install modules
npm install
## run frontend Client
npm run serve
```
>🔔 Make sure before the following step, in server/fore/rtre/server/Main.java BimPort is set to localhost:port 

```java

public class Main {
	// public static String BimPort = "http://host.docker.internal:8082";
	public static String BimPort = "http://localhost:8082";

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}

```

Next, navigate to ./RTRE/Server/ and run 

```bash 
mvn clean install
mvn spring-boot:run
```

## ⚠️ License
--- 

MIT License

Copyright (c) 2021 Fore Development

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

## ⭐ Acknowledgments
---
* [Bimserver](https://github.com/opensourceBIM/BIMserver)
* [IfcOpenshell](https://github.com/IfcOpenShell/IfcOpenShell)

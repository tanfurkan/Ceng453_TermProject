# group7

                    CENG 453 Special Topics in Computer Engineering
                Software Construction Term Project Multiplayer Shooter Game


This project is a multiplayer spaceship (bullet hell type) shooter game that consists of 5 levels total. Main goal is to kill every enemy without dying and finish the final boss to complete the game. First four levels are designed as singleplayer levels and for the final level every player will team up with another player to defeat the final boss. On multiplayer level if a person dies, the game ends. Records can be submitted and the highest scores will display on main leaderboard.

## Installation
**To install the project**
1. Download the project from the main git page of the project or run following command in desired directory:
    git clone http://144.122.71.144:8080/furkan.tan/group7.git
(If Intellij IDEA is used, project can be downloaded and opened directly using the Version Control of Intellij IDEA)
2. Open the project with the desired IDE of choice (Intellij IDEA is recommended. If Version Control is used on Step 1, skip this step.)
3. Connect the database using IDE User Interface.
    For example in Intellij:
    *   Select Database tab from the right side bar of the Intellij
    *   Click "+" on Database menu.
    *   From opening dropdown menu select "Data Source" and select MariaDB.
    *   Enter host as 144.122.71.144, port as 3306 and enter username and password for the database.
    *   Click Apply to connect database to the Intellij.
    *   After that from the left side bar of the Intellij, select "Persistence".
    *   Right click on gameServer database and click "Assign Data Sources".
    *   Finally from opening window Click Data Source tab and select connected MariaDB database with 144.122.71.144 host.
4. Run the project.

## Backend
* For the backend side of the game, Spring Boot and Maven is used to create the project server.
* For database management, MariaDB is used. 
* For authorization and authentication, Spring Security is used. 
* For session handling, JWT tokens are created and passed to the user.
* For security and password encryption, Spring Security and BCryptEncoder are used.
* For API documentation, Swagger and Swagger UI is used. 

For Swagger Documentation:
Please run the project and enter:
    http://localhost:8080/swagger-ui.html
Main usages of every service and explanations of the models can be found on Swagger UI.
    
## Frontend
## About Us

Furkan TAN - 2172039

İlyas Eren YILMAZ - 2172203

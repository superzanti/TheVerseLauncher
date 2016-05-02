# The Verse Launcher
---

This java application is a very simplistic and basic launcher for Minecraft intended for my minecraft server called 'TheVerse'. This application is intended to use git commands to pull an updated client every single time the launcher is run. This launcher will always use the ./data/.minecraft directory where "./" is where you lauched "TheVerseLauncher.jar" from.

Since a git pull is run the launcher can run any version of minecraft that the server has uploaded to the git repository. On top of this it prevents forge mod and config discrepancies.

Much of this code was taken from [tomsik68](https://github.com/tomsik68/OneClickMC). The GUI elements are almost exaclty the same code.

### Order of opperations
---
1. Start the git pull (This runs as a separate thread so step 2 will start immediately)
    1. Re-init the folder for git (incase the .git directory gets deleted)
    2. Set the remote origin
    3. Set the branch to master
    4. Merge the branch
    5. Run a git fetch
    6. Run a git reset --hard
    7. Run a git clean
    8. Run a git pull
2. Initialize the Launcher gui (loginframe)
3. Set all the values we need to launch
    1. Check if there is a stored profile
        1. If there isn't then display the login gui elements
        2. If there is then display the logout buttons
    2. Enable and display all the gui elements
    3. wait for buttons to be clicked
4. When the launch button is clicked run the GameLauncher
5. The game launcher will wait at "updating assets" until the git pull is finished
6. Launch the game

### Dependencies
---

* flow-nbt-1.0.0.jar
* glassfish-cobra-orb-3.0.0-b023.jar
* jsch-0.1.53.jar
* json-smart-1.1.1.jar
* org.eclipse.jgit-4.3.0.201604071810-r.jar
* slf4j-api-1.7.21.jar
* slf4j-simple-1.7.21.jar

All of the above dependencies are included in the repository to make sure versioning stays the same between all builds.

### Building in eclipse
---
1. Run a "git clone https://github.com/superzanti/TheVerseLauncher.git" from a directory (we will call it 'workspace')
2. Open eclipse and set workspace as your workspace.
3. Click File > New > Java Project.
4. Set 'TheVerseLauncher' as the project name.
    * The default location should now say ./workspace/TheVerseLauncher
5. Click Finish.

The libs folder will be automatically included in the build source. You should be able to click the 'TheVerseLauncher > src > com.superzanti.launcher' then click run and have the application pop up.

## TODO
---
- [x] Create a decent readme
- [x] Integrate git functionality
- [x] Integrate minecraft launch functionalty
- [ ] Change the icon for the launcher (away from the tomsik68 default)
- [ ] Create dynamic gui scaling
- [ ] Add a close button to the gui
- [ ] Clicking the enter key will click the launch button
- [ ] Email is autoselected
- [ ] Add some sort of progress indicator for the git pull and downloading assets
- [ ] Add the username image when the user autologs in

# ERU (Social Media)

## How can I build this?

[Video Tutorial If Preferred](https://github.com/CSC-3380-Fall-2024/Team-4/blob/main/ReadMeFiles/BuildingAndRunning.mp4)

1. Install the following dependencies
    - [Node.JS v22.12.0](https://nodejs.org/en)
    - [Git](https://git-scm.com/downloads)
    - [Java JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
    - [IntelliJ IDEA (My Java IDE of choice)](https://www.jetbrains.com/idea/)
2. Clone the repo
```cmd
git clone https://github.com/CSC-3380-Fall-2024/Team-4
```
3. Navigate to the Team-4 directory
```cmd
cd Team-4
```
4. Navigate to the Frontend directory
```cmd
cd ERU.Frontend
```
5. Install the node modules
```cmd
npm install
```
6. Open the `ERU.Backend` folder in IDEA and run the application
7. Run the frontend application (with the terminal in the `ERU.Frontend` folder)
```cmd
npm run web
```

## Features
Take everything profile related with a grain of salt as you are only logged in under my profile since our design lead never actually designed anything, so we couldn't make a login tab to register or log in users (thus my AccountId is hardcoded).
- Home feed displaying all posts
- Uploading tab to upload posts
- Caption system for posts
- Comments working
- Profile tab for showing posts from a profile and its information
- Explore tab for showing different posts (this was abitious from the start, we never got to the algorithm, so it is just placeholder images at the moment).

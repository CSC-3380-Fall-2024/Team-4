import { useState } from 'react';
import Splash from "./Components/SplashScreen";
import { ThemeProvider} from "styled-components";

const lightTheme = {
    background: "white",
    fontColor: "black",
}

const darkTheme ={
    background: "black",
    fontColor: "white"
}

const themes ={
    light: lightTheme,
    dark: darkTheme
}

function App()
{
    //Use useState import to set initial theme to light
    const [theme, setTheme] = useState("light") //theme will be initialized to the word light, setTheme is the funciton that allows it to be set and will act as a toggle
    return(    
<ThemeProvider theme={themes[theme]}>
    <Splash theme={theme} setTheme={setTheme} />
</ThemeProvider>
    );
}

export default App;

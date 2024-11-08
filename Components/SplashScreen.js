import styled from "styled-components";
import {CgSun} from "react-icons/cg";
import {HiMoon} from "react-icons/hi";

const Toggle = styled.button`
    cursor: pointer;
    height: 50px;
    width: 50px;
    border-radius: 50%;
    border: none;
    backgroundColor: {props => props.theme.fontColor};
    color: {props => props.theme.background};
    &: focus{
    outline: none;
    }
    transition: all .5s ease;

`;


const Page = styled.div`
    display: flex;
    justify-Content: center;
    align-Items:  center;
    height: 100vh;
    width: 100%;
    backgroundColor: {props => props.theme.background};
    transition: all .5s ease;
  `  ;

const Container = styled.div`
    dispaly: flex;
    flex-direction:column;
    align-itmes: center;
`;



function Splash(props){
    function themeSwitch(){
        if (props.theme == "light"){
            props.setTheme("dark");
        }else{
            props.setTheme("light");
        }
    };

//When set to light mode display moon, else display sun icon
    const icon = props.theme === "light" ? <HiMoon size ={40} /> :<CgSun size={40} />

    return(
      <Page>
            <Container>
               <Toggle onClick={themeSwitch}>
                 {icon}
                </Toggle>
            </Container> 
     </Page>
    );
}
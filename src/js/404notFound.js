import React, { Component } from "react";
import logo from '../logo.png'

import {createMuiTheme, ThemeProvider} from "@material-ui/core/styles";
const theme = createMuiTheme({
    typography: {
        fontFamily: 'Open Sans',
    },
    palette: {
        primary: {
            main: '#BB64FF',
        },
    },
});

const classes = {
    logo: {
        marginTop: '12rem',
        width: '10%',
        height: '10%',
    },
    googleIcon: {
        marginTop: '0.25rem',
        marginRight: '0.5rem',
        height: '5%',
    },
    googleButton: {
        marginTop: '1rem',
        width: 375,
        height: 50,
        backgroundColor: 'white',
        textAlign: 'center',
    },
    button: {
        height: '100%',
        width: '100%',
    },
    align: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
    },
    text: {
        fontSize: '14px',
        fontWeight: '700',
        color: '#999999',
    },
};

export default class NotFound extends Component {


    render() {
        return (
            <ThemeProvider theme={theme}>
                <div style={{minWidth: '100vh', minHeight: '100vh', backgroundColor:'#bb65ff', margin: -8,
                    borderColor: '#bb65ff'}}>
                    <div style={classes.align}>
                        <img src={logo} alt={"Logo"} style={classes.logo}/>
                    </div>
                    <div style={classes.align}>
                        <h1 className={"not-found"}>404: The requested URL was not found on this server.</h1>
                    </div>
                </div>
            </ThemeProvider>
        );
    }
}

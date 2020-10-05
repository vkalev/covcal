import React, { Component } from "react";
import auth from "./../auth";
import logo from '../../logo.png'
import {Redirect} from "react-router-dom";
import GoogleLogo from '../../google-logo.png';
import Button from "@material-ui/core/Button";
import Icon from "@material-ui/core/Icon";
import {createMuiTheme, ThemeProvider} from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";

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

export default class Signin extends Component {

    state = {
        isSignedIn: false
    };

    onSuccess() {
        this.setState({
            isSignedIn: true
        });
    }

    componentDidMount() {
        this.googleSDK();
    }

    prepareLoginButton = () => {

        this.auth2.attachClickHandler(this.refs.googleLoginBtn, {},
            (googleUser) => {

                let profile = googleUser.getBasicProfile();
                this.setState({ isSignedIn: true });
                const postParameters = {
                    id: profile.getId(),
                    name: profile.getName(),
                    email: profile.getEmail(),
                    image: profile.getImageUrl()
                };

                // Here, we update the user information:
                // create a new XMLHttpRequest
                const xhr = new XMLHttpRequest();
                // get a callback when the server responds
                xhr.addEventListener('load', () => {
                    // update the state of the component with the result here
                    if (xhr.response === 'true'){
                        auth.setIsFirst(true);
                    }
                });
                // open the request with the verb and the url
                xhr.open('POST', 'http://localhost:5555/pushuserinfo', false);
                // send the request
                xhr.send(JSON.stringify(postParameters));

                // Now, we must get the info from the database to cache:
                // create a new XMLHttpRequest
                const xhrData = new XMLHttpRequest();
                // We now cache the user data
                xhrData.addEventListener('load', () => {
                    let user = JSON.parse(xhrData.response);
                    auth.email = user[0];
                    auth.name = user[1];
                    auth.dateJoined = user[2];
                    auth.image = user[3];
                    auth.desc = user[4];
                });
                // open the request with the verb and the url
                xhrData.open('POST', 'http://localhost:5555/get_user', false);
                // send the request
                xhrData.send(profile.getId());

                // Finally, we login and head to the home page
                auth.login(() => {
                    const {history} = this.props;
                    history.push('/')
                }, profile.getId());
            });
    };

    googleSDK = () => {

        window['googleSDKLoaded'] = () => {
            window['gapi'].load('auth2', () => {
                this.auth2 = window['gapi'].auth2.init({
                    client_id: '852603679013-1b1raq3fne05a53quluvqlq075ej820k.apps.googleusercontent.com',
                    cookiepolicy: 'single_host_origin',
                    scope: 'profile email',
                });
                this.prepareLoginButton();
            });
        };

        (function(d, s, id){
            let js, fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) {return;}
            js = d.createElement(s); js.id = id;
            js.src = "https://apis.google.com/js/platform.js?onload=googleSDKLoaded";
            fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'google-jssdk'));

    };

    render() {
        const { isSignedIn } = this.state;
        if (isSignedIn) {
            return <Redirect to={'/'}/>;
        }

        return (
            <ThemeProvider theme={theme}>
                <div style={{minWidth: '100vh', minHeight: '100vh', backgroundColor:'#bb65ff', margin: -8,
                    borderColor: '#bb65ff'}}>
                    <div style={classes.align}>
                        <img src={logo} alt={"Logo"} style={classes.logo}/>
                    </div>
                    <div style={classes.align}>
                        <Button
                            variant="contained"
                            id="signInButton"
                            ref="googleLoginBtn"
                            style={classes.googleButton}
                            startIcon={<Icon style={classes.googleIcon}>
                                <img src={GoogleLogo} alt="googleLogo" style={classes.button} />
                            </Icon>}
                        >
                            <Typography style={classes.text}> Sign In with Google </Typography>
                        </Button>
                    </div>
                </div>
            </ThemeProvider>
        );
    }
}

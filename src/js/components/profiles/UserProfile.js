import React from 'react';
import auth from "../../auth";
import MenuBar from "../homepage/MenuBar";
import ProfilePic from "./ProfilePic";
import UserAttributes from "./UserAttributes";
import "../../../css/opensans.css"
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

// Styling:
const classes = {
    content: {
        justifyContent: 'center',
        display: 'flex',
    },
    imageCard: {
        justifyContent: 'center',
        marginTop: '1rem',
        paddingRight: 5,
    },
    userAttr: {
        marginTop: '1rem',
        paddingLeft: 5,
    },
};

let email;
let name;
let date;
let image;
let description;
let numGroups;
let numCoveredFor;
let numCoveredBy;

// For the props, we input the id of the user's page we are accessing.
export default class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            email : "",
            name : "",
            date : "",
            imgURL : "",
            description : "",
            numCoveredBy : -1,
            numCoveredFor : -1,
            numGroups : -1
        };
    }

    handleGetUserInfo = () => {
        // create a new XMLHttpRequest
        const xhrData = new XMLHttpRequest();
        // We now cache the user data
        xhrData.addEventListener('load', () => {
            let user = JSON.parse(xhrData.response);
            if (this.props.location.state.firstGetInfo) {
                this.setState({
                    email: user[0],
                    name: user[1],
                    date: user[2],
                    imgURL: user[3],
                    description: user[4],
                });
                this.props.location.state.firstGetInfo = false;
            }
        });
        // open the request with the verb and the url
        xhrData.open('POST', 'http://localhost:5555/get_user', false);
        // send the request
        xhrData.send(this.props.location.state.userId);
    };

    handleGetUserFacts = () => {
        // create a new XMLHttpRequest for getting the attributes
        const xhrAtt = new XMLHttpRequest();
        // We now cache the user data
        xhrAtt.addEventListener('load', () => {
            let user = JSON.parse(xhrAtt.response);
            if (this.props.location.state.firstGetFacts) {
                this.setState({
                    numGroups: user[0],
                    numCoveredFor: user[1],
                    numCoveredBy: user[2],
                });
                this.props.location.state.firstGetFacts = false;
            }
        });
        // open the request with the verb and the url
        xhrAtt.open('POST', 'http://localhost:5555/get_user_facts', false);
        // send the request
        xhrAtt.send(this.props.location.state.userId);
    };

    handleImgChange = (newImg) => {
        this.setState({
            imgURL : newImg
        })
    };

    handleAttrChange = (newName, newDesc) => {
        this.setState({
            description : newDesc,
            name : newName
        })
    };

    render() {
        this.handleGetUserFacts();
        this.handleGetUserInfo();
        return (
            <div>
                <ThemeProvider theme={theme}>
                    <MenuBar history={this.props.history}/>
                    <div style={classes.content}>
                        <div style={classes.imageCard}>
                            <ProfilePic picture={this.state.imgURL} name={this.state.name} description={this.state.description}
                                        handleImgChange={this.handleImgChange} isOwner={this.props.location.state.userId === auth.getCurrUser()}/>
                        </div>
                        <div style={classes.userAttr}>
                            <UserAttributes groups={this.state.numGroups} for={this.state.numCoveredFor}
                                            by={this.state.numCoveredBy} description={this.state.description}
                                            email={this.state.email} date={this.state.date} name={this.state.name}
                                            handleAttrChange={this.handleAttrChange}
                                            isOwner={this.props.location.state.userId === auth.getCurrUser()}/>
                        </div>
                    </div>
                </ThemeProvider>
            </div>
        );
    }
}


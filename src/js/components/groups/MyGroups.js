import React from "react";
import MenuBar from "../homepage/MenuBar";
import auth from "../../auth";
import Leaderboard from "./Leaderboard"
import Container from "@material-ui/core/Container";
import "../../../css/opensans.css"
import { createMuiTheme, ThemeProvider } from '@material-ui/core/styles'

let _history;
const noGroupsMsg = <h3 className={"noGroupsMsg"}>You currently are not a part of any groups.
                        <div className={"empty-mygroup-clicks"}>
                        <span onClick={() => {_history.push("/new_group")}} className={"create-mygroups"}>Create</span> or
                            <span onClick={() => {_history.push("/",{join:true})}}className={"join-mygroups"}> Join </span>
                            a group!</div> </h3>;

const theme = createMuiTheme({
    typography: {
        fontFamily: 'Open Sans',
    }
});

let currGroup = null;

function getGroups(history) {
    _history = history;
    let orgs;
    const xhr = new XMLHttpRequest();
    // get a callback when the server responds
    xhr.addEventListener('load', () => {
        // update the state of the component with the result here
        orgs = JSON.parse(xhr.response);
    });
    // open the request with the verb and the url
    xhr.open('POST', 'http://localhost:5555/get_orgs_user/' + auth.getCurrUser(), false);
    xhr.send();

    let allOrgs = [];
    for (var i = 0; i < orgs.length; i++) {
        console.log(orgs[i]);
        allOrgs.push(<br/>)
        allOrgs.push(<Leaderboard history={history} orgDescription = {orgs[i].description}
                                  orgID = {parseInt(orgs[i].id)} orgOwner = {orgs[i].owner}
                                  orgName = {orgs[i].name}/>);
    }
    if (allOrgs.length == 0) allOrgs.push(noGroupsMsg)
    allOrgs.push(<br/>)
    return allOrgs ;
}


export default class MyGroups extends React.Component {

    render() {

        console.log(this.props.history);
        return (
            <div>
                <ThemeProvider theme={theme}>
                    <MenuBar history={this.props.history}/>
                    <Container maxWidth="md">
                        {getGroups(this.props.history)}
                    </Container>
                </ThemeProvider>
            </div>
        );
    }
}

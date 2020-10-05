import Signin from "./js/components/Signin";
import './css/index.css';
import React from "react";
import {render} from "react-dom";
import Home from "./js/components/homepage/Home.js";
import {BrowserRouter as Router, Route, Switch } from "react-router-dom";
import {ProtectedRoute} from "./js/protected.route.js";
import CreateGroup from "./js/components/groups/CreateGroup";
import UserProfile from "./js/components/profiles/UserProfile";
import MyGroups from "./js/components/groups/MyGroups";
import NotFound from "./js/404notFound";
import { Redirect} from 'react-router-dom';
export default class App extends React.Component {
    render() {
        return (
            <Router>
                <div>
                    <Switch>
                        <Route path={"/signin"} exact component={Signin}/>
                        <Route path="/404"  exact component={NotFound} />
                        <ProtectedRoute path={"/"} exact component={Home}/>
                        <ProtectedRoute path={"/new_group"} exact component={CreateGroup}/>
                        <ProtectedRoute path={"/user_profile"} exact component={UserProfile}/>
                        <ProtectedRoute path={"/my_groups"} exact component={MyGroups}/>
                        <Redirect from="*" to="/404" exact component={NotFound}/>
                    </Switch>
                </div>
            </Router>
        )
    }
    constructor(props){
        super(props);
        this.state = {
            isSignedIn: false,
        }
    }
}

render(<App />, window.document.getElementById('signin'));
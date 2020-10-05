import React from "react";
import MenuBar from "../homepage/MenuBar";
import auth from "../../auth";
import NewGroup from "./NewGroup";
import "../../../css/opensans.css"
import { createMuiTheme, ThemeProvider } from '@material-ui/core/styles'

const theme = createMuiTheme({
    typography: {
        fontFamily: 'Open Sans',
    }
});

export default class CreateGroup extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            nameValue: '',
            description: '',
            positions: [],
        };
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleInputChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        this.setState({
            [name]: value
        });
    }

    handleSubmit(event) {
        const postParameters = {
            gName: this.state.nameValue,
            gType: "employee",
            gOwner: auth.getCurrUser(),
            gPositions: null,
            gDescription: this.state.description
        };

        // create a new XMLHttpRequest
        const xhr = new XMLHttpRequest();

        // get a callback when the server responds
        xhr.addEventListener('load', () => {
            // update the state of the component with the result here
        });
        // open the request with the verb and the url
        xhr.open('POST', 'http://localhost:5555/createnewgroup');
        // send the request
        xhr.send(JSON.stringify(postParameters));
        event.preventDefault;
        const {history} = this.props;
        history.push("/");
    }

    render() {
        return (
            <div>
                <ThemeProvider theme={theme}>
                    <MenuBar history={this.props.history}/>
                    <NewGroup handleChange={this.handleInputChange} handleClick={this.handleSubmit} />
                </ThemeProvider>
            </div>
        );
    }
}
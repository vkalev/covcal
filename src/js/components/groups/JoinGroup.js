import React from 'react';
import TextField from '@material-ui/core/TextField';
import Button from "@material-ui/core/Button";
import auth from "../../auth";
import Alert from '@material-ui/lab/Alert'

const classes = {
    root: {
        marginTop: '1rem',
    },
    alignContent: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    },
    form: {
        width: '40ch',
        alignItems: 'center',
    },
    button: {
        marginTop: '1rem',
        marginBottom: '1rem',
    },
    errors: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        color: 'red'
    },
};

export default class JoinGroupComp extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            idValue: '',
            errorMessage: '',
            showError: false
        };
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleInputChange(event) {
        const target = event.target;
        this.state.idValue = target.value;
    }

    handleSubmit(event) {
        const postParameters = {
            gID: this.state.idValue,
            eID: auth.getCurrUser(),
            positions: null
        };

        // create a new XMLHttpRequest
        const xhr = new XMLHttpRequest();

        // get a callback when the server responds
        xhr.addEventListener('load', () => {
            // update the state of the component with the result here
            const response = xhr.responseText;
            if (response === "-1") {
                this.setState({errorMessage: "group does not exist", showError: true});
            } else if (response === "-2") {
                this.setState({errorMessage: "already joined this group", showError: true});
            } else if (response === "-3") {
                this.setState({errorMessage: "server error", showError: true});
            } else if (response === "-4") {
                this.setState({errorMessage: "invalid group ID number", showError: true});
            } else {
                const xhr = new XMLHttpRequest();
                xhr.addEventListener('load', () => {
                    console.log("This is where join group listen happened");
                    console.log(JSON.parse(xhr.response));
                    this.props.handleAddJoinGroup(JSON.parse(xhr.response));
                });
                // open the request with the verb and the url
                xhr.open('POST', 'http://localhost:5555/get_org?orgID=' + this.state.idValue);
                // send the request
                xhr.send(JSON.stringify(postParameters));
            }
        });
        // open the request with the verb and the url
        xhr.open('POST', 'http://localhost:5555/add_employee', false);
        // send the request
        xhr.send(JSON.stringify(postParameters));
        event.preventDefault;
    }


    render() {
        const errStyle = this.state.showError ? {} : {display: 'none'};
        return (
            <div style={classes.root}>
                <div style={classes.alignContent}>
                    <form style={classes.form} noValidate autoComplete="off">
                        <div style={classes.alignContent}>
                            <TextField
                                required
                                id="outlined-required"
                                label="Group ID"
                                variant="outlined"
                                name="id"
                                style={classes.input}
                                onChange={this.handleInputChange}
                            />
                        </div>
                        <Alert severity="error" style={errStyle}> {this.state.errorMessage} </Alert>
                        <div style={classes.alignContent}>
                            <Button color="default" variant="contained" style={classes.button} onClick={this.handleSubmit}>
                                Join
                            </Button>
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}

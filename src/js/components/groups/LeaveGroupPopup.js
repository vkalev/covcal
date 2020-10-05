import React from 'react';
import Button from "@material-ui/core/Button";
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
        marginTop: '1rem',
        width: '55ch',
        alignItems: 'center',
    },
    button: {
        marginBottom: '1rem',
    },
    sbutton: {
        marginLeft: '1rem',
        marginRight: '1rem',
        marginBottom: '1rem',
        background: '#BB64FF',
        color: 'White',
    },
    errors: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        color: 'red'
    },
};

export default class LeaveGroupPopup extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            errorMessage: '',
            showError: false
        };
    }

    render() {
        const errStyle = this.state.showError ? {} : {display: 'none'};
        return (
            <div style={classes.root}>
                <div style={classes.alignContent}>
                    <form style={classes.form} noValidate autoComplete="off">
                        <div style={classes.alignContent}>
                            <Button color="default" variant="contained" style={classes.button} onClick={this.props.handleNo}>
                                Cancel
                            </Button>
                            <Button variant="contained" style={classes.sbutton} onClick={this.props.handleYes}>
                                {this.props.type}
                            </Button>
                        </div>
                        <Alert severity="error" style={errStyle}> {this.state.errorMessage} </Alert>
                    </form>
                </div>
            </div>
        );
    }
}

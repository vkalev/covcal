import React from 'react';
import Drawer from 'material-ui/Drawer';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'

const classes = {
    button: {
        marginTop: '2rem',
        marginLeft: '1rem',
    }

};
export default class MyGroupsSideBar extends React.Component {

    constructor(props) {
        super(props);
        this.state = {open: false};
        this.groups = props.groups;
    }

    handleToggle = () => this.setState({open: !this.state.open});

    handleClose = () => this.setState({open: false});

    render() {
        return (
            <MuiThemeProvider>
            <div>
                <RaisedButton
                    style={classes.button}
                    label="Groups"
                    onClick={this.handleToggle}
                />
                <Drawer

                    docked={false}
                    width={200}
                    open={this.state.open}
                    onRequestChange={(open) => this.setState({open})}
                >
                    <MenuItem onClick={this.handleClose}>Menu Item</MenuItem>
                    <MenuItem onClick={this.handleClose}>Menu Item 2</MenuItem>
                </Drawer>
            </div>
            </MuiThemeProvider>
        );
    }
}
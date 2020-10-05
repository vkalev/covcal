import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import MenuItem from '@material-ui/core/MenuItem';
import Menu from '@material-ui/core/Menu';
import logo from "../../../logo.png";
import auth2 from "../../auth";
import { useHistory } from "react-router-dom";
import Avatar from "@material-ui/core/Avatar";
import Tooltip from "@material-ui/core/Tooltip";

const useStyles = makeStyles((theme) => ({
    root: {
        width: '98.75%',
        flexGrow: 1,
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        flexGrow: 1,
    },
    appBar: {
        backgroundColor: '#BB64FF',
    }
}));


export default function MenuAppBar() {
    const classes = useStyles();
    const [auth, setAuth] = React.useState(true);
    const [anchorEl, setAnchorEl] = React.useState(null);
    const open = Boolean(anchorEl);
    const history2 = useHistory();

    const handleChange = (event) => {
        setAuth(event.target.checked);
    };

    const handleMenu = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    return (
        <div className={classes.root}>
            <AppBar position="static" className={classes.appBar}>
                <Toolbar>
                    <img src={logo} alt={"Logo"} className={"smallLogo"} onClick={() => history2.push("/")}/>
                    <Typography variant="h6" className={classes.title}>
                        CovCal
                    </Typography>
                    {auth && (
                        <div>
                            <Tooltip title={auth2.getName()} aria-label="add">
                                <Avatar style={{cursor: 'pointer'}} src={auth2.getImage()} onClick={handleMenu}/>
                            </Tooltip>
                            <Menu
                                id="menu-appbar"
                                anchorEl={anchorEl}
                                anchorOrigin={{
                                    vertical: 'top',
                                    horizontal: 'right',
                                }}
                                keepMounted
                                transformOrigin={{
                                    vertical: 'top',
                                    horizontal: 'right',
                                }}
                                open={open}
                                onClose={handleClose}
                            >
                                <MenuItem onClick={
                                    () => {
                                        history2.push({
                                            pathname: "/user_profile",
                                        state: {
                                                userId: auth2.getCurrUser(),
                                                firstGetInfo : true,
                                                firstGetFacts : true,
                                        }})}}>
                                    Profile
                                </MenuItem>
                                <MenuItem onClick={() => auth2.logout(() => {
                                    window.location.reload();})} >Logout</MenuItem>
                            </Menu>

                        </div>
                    )}
                </Toolbar>
            </AppBar>
        </div>
    );
}

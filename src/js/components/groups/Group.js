import ListItem from "@material-ui/core/ListItem";
import Checkbox from "@material-ui/core/Checkbox";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import IconButton from "@material-ui/core/IconButton";
import MoreVertIcon from "@material-ui/icons/MoreVert";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import React from "react";
import {makeStyles} from "@material-ui/core/styles";
import auth from "../../auth";
import Dialog from "@material-ui/core/Dialog";
import LeaveGroupPopup from "./LeaveGroupPopup";
import InputLabel from "@material-ui/core/InputLabel";
import Typography from "@material-ui/core/Typography";

const ITEM_HEIGHT = 48;

const useStyles = makeStyles((theme) => ({
    root: {
        height: '100%',
        width: '100%',
        maxWidth: '40ch',
        marginTop: '1rem',
    },
    form: {
        width: '100%',
        maxWidth: '36ch',
        maxHeight: '100%',
        overflow: 'auto',
        marginRight: '1rem',
        backgroundColor: theme.palette.background.paper,
    },
    inline: {
        display: 'flex',
    },
    alignContent: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        marginTop: '0.8rem',
        marginBottom: '0.8rem',
    },
    button: {
        marginTop: '1rem',
    },
    label: {
        display: 'flex',
    },
    group: {
        marginRight: '1rem',
    },
    divider: {
        marginRight: '1rem',
    },
    popup: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        marginTop: '1rem',
    }
}));

export default function Group(orgInfo) {
    const classes = useStyles();
    const [anchorEl, setAnchorEl] = React.useState(null);
    const menu = Boolean(anchorEl);
    const [checked, setChecked] = React.useState(true);
    const [openLeave, setOpenLeave] = React.useState(false);
    const [openDelete, setOpenDelete] = React.useState(false);
    const [popupErrMsg, setPopupErrMsg] = React.useState("");
    const [showPopupErrMsg, setShowPopupErrMsg] = React.useState(false);

    const handleMenuClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    const handleLeaveOpen = () => {
        setOpenLeave(true);
    };

    const handleLeaveClose = () => {
        setOpenLeave(false);
    };

    const handleDeleteOpen = () => {
        setOpenDelete(true);
    };

    const handleDeleteClose = () => {
        setOpenDelete(false);
    };

    const handleLeave = () => {
        const xhr = new XMLHttpRequest();
        // get a callback when the server responds
        xhr.addEventListener('load', () => {
            // update the state of the component with the result here
            const org = JSON.parse(xhr.response);
            if (org.id === -1) {
                setShowPopupErrMsg(true);
                setPopupErrMsg("System Error occurred. Please try again.");
            } else {
                orgInfo.handleRemoveGroup(org);
                handleLeaveClose();
                handleMenuClose();
            }
        });
        // open the request with the verb and the url
        xhr.open('POST', 'http://localhost:5555/remove_employee/' + orgInfo.orgID, false);
        // send the request
        xhr.send(JSON.stringify({empID: auth.getCurrUser()}));
    };

    const handleDelete = () => {
        const xhr = new XMLHttpRequest();
        // get a callback when the server responds
        xhr.addEventListener('load', () => {
            // update the state of the component with the result here
            const org = JSON.parse(xhr.response);
            if (org.id === -1) {
                setShowPopupErrMsg(true);
                setPopupErrMsg("You are not the owner of this group.");
            } else if (org.id === -2) {
                setShowPopupErrMsg(true);
                setPopupErrMsg("System Error occurred. Please try again.");
            } else {
                orgInfo.handleRemoveGroup(org);
                handleDeleteClose();
                handleMenuClose();
            }
        });
        // open the request with the verb and the url
        xhr.open('POST', 'http://localhost:5555/delete_org/' + orgInfo.orgID, false);
        // send the request
        xhr.send(JSON.stringify({empID: auth.getCurrUser()}));
    };

    const handleCheckChange = (event) => {
        setChecked(!checked);
        orgInfo.handleGroupChange(event, !checked);
    };

    return <ListItem
        value="end"
        control={<Checkbox name={orgInfo.orgID} color="primary"/>}
        label={orgInfo.name}
        key={orgInfo.index}
        className={classes.label}
    >
        <ListItemIcon>
            <Checkbox name={orgInfo.orgID.toString()} checked={checked} onChange={handleCheckChange} color="default"/>
        </ListItemIcon>
        <ListItemText id={orgInfo.index} primary={orgInfo.name}/>
        <ListItemSecondaryAction>
            <IconButton aria-label="more"
                        aria-controls="long-menu"
                        aria-haspopup="true"
                        onClick={handleMenuClick}>
                <MoreVertIcon/>
            </IconButton>
        </ListItemSecondaryAction>
        <Menu
            id="long-menu"
            anchorEl={anchorEl}
            keepMounted
            open={menu}
            onClose={handleMenuClose}
            PaperProps={{
                style: {
                    maxHeight: ITEM_HEIGHT * 4.5,
                    width: '20ch',
                },
            }}
        >
            <MenuItem onClick={handleLeaveOpen}>Leave</MenuItem>
            {auth.getCurrUser() === orgInfo.owner ? <MenuItem onClick={handleDeleteOpen}>Delete</MenuItem> : ''}
        </Menu>
        <Dialog open={openLeave} onClose={handleLeaveClose} aria-labelledby="form-dialog-title">
            <InputLabel className={classes.popup}>
                <Typography> Are you sure you want to leave {orgInfo.name}? </Typography>
            </InputLabel>
            <LeaveGroupPopup handleYes={handleLeave} handleNo={handleLeaveClose} type={"Leave"}/>
        </Dialog>
        <Dialog open={openDelete} onClose={handleDeleteClose} aria-labelledby="form-dialog-title">
            <InputLabel className={classes.popup}>
                <Typography> Are you sure you want to delete {orgInfo.name}? </Typography>
            </InputLabel>
            <LeaveGroupPopup handleYes={handleDelete} handleNo={handleDeleteClose} type={"Delete"}/>
        </Dialog>
    </ListItem>
}

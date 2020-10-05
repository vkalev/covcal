import React, {useEffect} from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Button from "@material-ui/core/Button";
require("regenerator-runtime/runtime");
import { useHistory } from "react-router-dom";
import JoinGroupComp from "./JoinGroup";
import Dialog from "@material-ui/core/Dialog";
import auth from "../../auth";
import ButtonGroup from "@material-ui/core/ButtonGroup";
import List from "@material-ui/core/List";
import Group from "./Group";

const useStyles = makeStyles((theme) => ({
    root: {
        height: '100%',
        width: '100%',
        maxWidth: '35ch',
    },
    form: {
        width: '100%',
        maxWidth: '35ch',
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
        '&:hover': {
            backgroundColor: '#f3e0ff',
        },
    },
    label: {
        display: 'flex',
    },
    group: {
        marginRight: '1rem',
    },
    divider: {
        marginRight: '1rem',
    }
}));

const ITEM_HEIGHT = 48;

export default function GroupList(handlers) {

    const classes = useStyles();
    const history = useHistory();
    const [open, setOpen] = handlers.autoJoin ? React.useState(true) : React.useState(false);
    const [checked, setChecked] = React.useState(true);
    const [lstOrgs, setOrgs] = React.useState([]);
    const [firstRender, setFirstRender] = React.useState(false);

    const handleChange = (event) => {
        setChecked(event.target.checked);
    };
    const handleClickOpen = () => {
        setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
    };

    const [anchorEl, setAnchorEl] = React.useState(null);
    const menu = Boolean(anchorEl);

    const handleMenuClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    useEffect(() => {
        if (firstRender) {
            handlers.handleSet(lstOrgs);
        }
    }, [lstOrgs]);

    const handleJoinGroup = (org) => {
        const copyOrgs = lstOrgs;
        copyOrgs.push(org);
        setOrgs(copyOrgs);
        handlers.handleJoin(org);
        handleClose();
    };

    const handleLeaveDeleteGroup = (org) => {
        const copyOrgs = [];
        for (let i=0; i<lstOrgs.length; i++) {
            if (org.id !== lstOrgs[i].id) {
                copyOrgs.push(lstOrgs[i]);
            }
        }
        setOrgs(copyOrgs);
    };


    const xhr = new XMLHttpRequest();
    // get a callback when the server responds
    xhr.addEventListener('load', () => {
        // update the state of the component with the result here
        if (!firstRender) {
            setOrgs(JSON.parse(xhr.response));
            setFirstRender(true);
        }
    });
    // open the request with the verb and the url
    xhr.open('POST', 'http://localhost:5555/get_orgs_user/' + auth.getCurrUser(), false);
    xhr.send();


    return (
        <div className={classes.root}>
            <div className={classes.alignContent}>
                <ButtonGroup className={classes.group} variant="text" color="default" aria-label="contained primary button group">
                    <Button className={classes.button} onClick={() => {history.push("/new_group")}}>
                        New Group
                    </Button>
                    <Button className={classes.button} onClick={handleClickOpen}>
                        Join Group
                    </Button>
                    <Button className={classes.button} onClick={() => {history.push("/my_groups")}}>
                        My Groups
                    </Button>
                    <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
                        <JoinGroupComp handleAddJoinGroup={handleJoinGroup}/>
                     </Dialog>
                </ButtonGroup>
            </div>
            <div className={classes.alignContent}>
                <List component="nav" className={classes.form} style={{maxHeight: '100%', overflow: 'auto'}}>
                    {lstOrgs.map(function(org, index){
                        return <Group key={index} orgID={org.id} name={org.name} owner={org.owner} index={index}
                                      handleGroupChange={handlers.handleChecked} handleRemoveGroup={handleLeaveDeleteGroup} />
                    })}
                </List>
            </div>
        </div>);

}

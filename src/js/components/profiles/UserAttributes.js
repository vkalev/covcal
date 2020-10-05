import React from "react";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Typography from '@material-ui/core/Typography';
import TextField from "@material-ui/core/TextField";
import auth from "../../auth";
import Button from "@material-ui/core/Button";
import Snackbar from "@material-ui/core/Snackbar";

// Style:
const classes = {
    root: {
        width: 400,
        marginLeft: '1rem',
    },
    name: {
        display: 'flex',
    },
    desc: {
        display: 'flex',
        marginTop: '1rem',

    },
    grid: {
        paddingTop: '1rem',
    },
    paper: {
        height: 60,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
    },
    title: {
        fontSize: 24,
    },
    button: {
        minWidth: 100,
        display: 'flex',
        marginTop: '3rem',
        background: '#BB64FF',
        color: '#FFFFFF',
    },
    format: {
        alignItems: 'flex-end',
        flexDirection: 'row',
        justifyContent: 'flex-end',
        display: 'flex',
    },
};

export default function UserAttributes(props) {
    let newName = props.name;
    let newDesc = props.description;
    const [open, setOpen] = React.useState(false);

    const handleClick = () => {
        setOpen(true);
    };

    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }

        setOpen(false);
    };

    const handleNameChange = (event) => {
        const target = event.target;
        newName = target.value;
    };
    const handleDescChange = (event) => {
        const target = event.target;
        newDesc = target.value;
    };

    const handleSubmit = (event) => {
        const postParameters = {
            userId: auth.getCurrUser(),
            newName: newName,
            newDesc: newDesc,
        };

        // create a new XMLHttpRequest
        const nameXhr = new XMLHttpRequest();

        // get a callback when the server responds
        nameXhr.addEventListener('load', () => {
            // update the state of the component with the result here
            auth.name = newName;
            props.handleAttrChange(newName, newDesc);
        });
        // open the request with the verb and the url
        nameXhr.open('POST', 'http://localhost:5555/user_profile/upload_name');
        // send the request
        nameXhr.send(JSON.stringify(postParameters));

        handleClick();
    };

    return (
        <div>
            <div style={classes.root}>
                <div>
                    <form style={classes.form} noValidate autoComplete="off">
                        <TextField
                            disabled={!props.isOwner}
                            variant="filled"
                            id="filled-basic"
                            label="Full name"
                            multiline={true}
                            defaultValue={props.name}
                            name="nameValue"
                            style={classes.name}
                            onChange={handleNameChange}/>
                        <Grid item xs style={classes.grid}>
                            <Paper style={classes.paper} align={'center'}>
                                <Typography> Email: {props.email} </Typography>
                            </Paper>
                        </Grid>
                        <div style={classes.grid}>
                            <Grid container spacing={2}>
                                <Grid item xs>
                                    <Paper style={classes.paper}>
                                        <Typography> Shifts Requested: {props.by} </Typography>
                                    </Paper>
                                </Grid>
                                <Grid item xs>
                                    <Paper style={classes.paper}>
                                        <Typography> Shifts Covered: {props.for} </Typography>
                                    </Paper>
                                </Grid>
                            </Grid>
                        </div>
                        <TextField
                            disabled={ !props.isOwner }
                            variant="filled"
                            id="filled-basic"
                            label="What I do"
                            defaultValue={props.description}
                            name="descValue"
                            helperText= { props.isOwner ? "Let people know what you do." : null }
                            style={classes.desc}
                            onChange={handleDescChange}
                            multiline={true}
                            rows={8}/>
                    </form>
                </div>
            </div>
            <div style={classes.format}>
                { props.isOwner ? <Button variant="contained"
                                          onClick={handleSubmit}
                                          style={classes.button}> <Typography> Save Changes </Typography> </Button> : null }
            </div>
            <Snackbar
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'center',
                }}
                open={open}
                autoHideDuration={3000}
                onClose={handleClose}
                message="Profile Updated Successfully"
            />
        </div>
    )
}
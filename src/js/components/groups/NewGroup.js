import React from 'react';
import TextField from '@material-ui/core/TextField';
import { makeStyles } from '@material-ui/core/styles';
import Button from "@material-ui/core/Button";

const useStyles = makeStyles((theme) => ({
    root: {
        marginTop: '1rem',
    },
    alignContent: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    },
    form: {
        '& .MuiTextField-root': {
            margin: theme.spacing(1),
            width: '70ch',
        },
    },
    button: {
        marginTop: '1rem',
    },
}));

export default function NewGroup(handlers) {
    const classes = useStyles();

    return (
        <div className={classes.root}>
            <div className={classes.alignContent}>
                <form className={classes.form} noValidate autoComplete="off">
                    <div>
                        <TextField
                            required
                            id="outlined-required"
                            label="Group Name"
                            defaultValue=""
                            variant="outlined"
                            name="nameValue"
                            onChange={handlers.handleChange}
                        />
                    </div>
                    <div>
                        <TextField id="outlined-search" label="Description (50 characters)" type="search" variant="outlined"
                                   multiline={true} rows={10} className={classes.description} name="description" inputProps={{ maxLength: 50 }}
                                   onChange={handlers.handleChange}/>
                    </div>
                    <div className={classes.alignContent}>
                        <Button color="default" variant="contained" className={classes.button} onClick={handlers.handleClick}>
                            Create Group
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    );
}
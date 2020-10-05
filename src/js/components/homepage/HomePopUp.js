import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import { makeStyles } from '@material-ui/core/styles';
import auth from "../../auth";
import Typography from "@material-ui/core/Typography";

// Style:
const useStyles = makeStyles({
    title: {
        background: '#BB64FF',
        '& h2': {
            color: 'white',
            fontSize: 30
        }
    },
    body: {
        color: '#575757',
        fontSize: 15,
        marginBottom: '0.75rem',
    },
    body3: {
        color: '#575757',
        fontSize: 15,
        fontWeight: '700',
        marginBottom: '0.75rem',
        marginLeft: '1.5rem',
    },
    button: {
        background: '#BB64FF',
        borderRadius: 3,
        border: 0,
        color: 'white',
        fontSize: 16,
        height: 48,
        padding: '0 30px',
        '&:hover': {
            background: '#ae5ced',
        },
        marginRight: '0.5rem',
        marginBottom: '0.5rem',
    },
    label: {
        textTransform: 'capitalize',
    },
});

export default function AlertDialog() {
    const classes = useStyles();

    const [popOpen, setPopOpen] = React.useState(auth.isFirstTime === true);

    const handlePopOpen = () => {
        setPopOpen(true);
    };

    const handlePopClose = () => {
        setPopOpen(false);
        auth.isFirstTime = false;
    };

    return (
        <div>
            <Dialog
                open={popOpen}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle className={classes.title}>Welcome to CovCal, {auth.name}!</DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        <Typography className={classes.body}>
                            We know a workplace can be chaotic, and it can be a hassle to get your shift covered.
                            So we've provided an easy to way manage your shifts.
                        </Typography>
                        <Typography className={classes.body}>
                            Cover coworkers' shifts to increase your "helpfulness":
                        </Typography>
                        <Typography className={classes.body3}>
                            Earn a higher score by covering others with high helpfulness 🔥 versus low helpfulness ❄️!
                        </Typography>
                        <Typography className={classes.body}>
                            By covering shifts, you are increasing the chance that your shift will be covered in the future!
                        </Typography>
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button classes={{ root: classes.button, label: classes.label, }}
                            onClick={handlePopClose}>
                        Got It
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

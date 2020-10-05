import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import Shift from "./Shift";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import {
    MuiPickersUtilsProvider,
    KeyboardTimePicker,
    KeyboardDatePicker,
} from '@material-ui/pickers';
import DialogActions from "@material-ui/core/DialogActions";
import DateFnsUtils from '@date-io/date-fns';
import Grid from "@material-ui/core/Grid";
import auth from "../../auth";
import Alert from "@material-ui/lab/Alert";
import Select from "@material-ui/core/Select";
import MenuItem from "@material-ui/core/MenuItem";
import InputLabel from "@material-ui/core/InputLabel";
import FormControl from "@material-ui/core/FormControl";
import AddIcon from '@material-ui/icons/Add';

const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
        minWidth: '33.75ch',
        backgroundColor: theme.palette.background.paper,
    },
    inline: {
        display: 'flex',
    },
    alignContent: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        marginTop: '0.2rem',
        marginBottom: '0.8rem',
    },
    paper: {
        height: '100%',
        width: '100%',
    },
    button: {
        background: 'White',
    },
    grid: {
        display: 'flex',
        alignItems: 'center',
    },
    select: {
        margin: theme.spacing(1),
        minWidth: 220,
    }
}));

/**
 * This function visualizes the shifts that need to be covered for the current selected group. It creates
 * a list of cards that contain the time of the shift and the individual asking for it to be covered.
 */
export default function ShiftList(displayInfo) {
    const classes = useStyles();
    const tempDate = new Date();
    tempDate.setHours(0);
    tempDate.setMinutes(0);
    tempDate.setSeconds(0);
    // Accessing the state
    const [open, setOpen] = React.useState(false);
    const [selectedDate1, setSelectedDate1] = React.useState(tempDate);
    const [selectedDate2, setSelectedDate2] = React.useState(tempDate);
    const [org, setOrg] = React.useState("");
    const [orgName, setOrgName] = React.useState("");
    const [lstShifts, setShifts] = React.useState([]);
    const [showError, setShowError] = React.useState(false);
    const [errorMsg, setErrorMsg] = React.useState("");

    // Functions necessary to update clicking / changing of inputs
    const handleClickOpen = () => {
        setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
    };
    const handleDateChange1 = (date) => {
        setSelectedDate1(date);
    };

    const handleDateChange2 = (date) => {
        setSelectedDate2(date);
    };

    const handleTextInput = (event) => {
        const target = event.target;
        setOrg(target.value);
    };

    const handleOrgChange = (event) => {
        setOrgName(event.target.value);
        setOrg(displayInfo.allOrgIDs[displayInfo.allOrgNames.indexOf(event.target.value)]);
    };

    const handleSubmit = (event) => {
        const month =  selectedDate1.getMonth() + 1;
        let strMonth = "0" + month.toString();
        if (month >= 10) {
            strMonth = month.toString();
        }
        const date = selectedDate1.getFullYear() + '-' + strMonth + '-' + selectedDate1.getDate();
        const start = (selectedDate1.getHours() * 100) + selectedDate1.getMinutes();
        const end = (selectedDate2.getHours() * 100) + selectedDate2.getMinutes();
        const postParameters = {
            empID: auth.getCurrUser(),
            date: date,
        };

        // create a new XMLHttpRequest
        const xhr = new XMLHttpRequest();

        // get a callback when the server responds
        xhr.addEventListener('load', () => {
            // update the state of the component with the result here
            const serverResp = xhr.response.substring(1, xhr.response.length - 1);
            if (serverResp !== "success") {
                setShowError(true);
                setErrorMsg(serverResp);
            } else {
                displayInfo.handleGetMyShifts();
                handleClose();
            }
        });
        // open the request with the verb and the url
        xhr.open('POST', 'http://localhost:5555/new_shift?orgID=' + org + '&start=' + start + '&end=' + end);
        // send the request
        xhr.send(JSON.stringify(postParameters));
        event.preventDefault;
    };

    const handleAcceptDecline = (requestID, isAccept) => {
        const copyLst = [];
        for (let i=0; i<displayInfo.lstShifts.length; i++) {
            if (displayInfo.lstShifts[i].id !== requestID) {
                copyLst.push(displayInfo.lstShifts[i]);
            }
        }
        setShifts(copyLst);
        displayInfo.handleSetShifts(copyLst, isAccept);
    };

    return (
        <div className={classes.paper}>
            <div className={classes.alignContent}>
                <Button startIcon={<AddIcon style={{color: '#BB64FF'}}/>} variant="contained" className={classes.button} onClick={handleClickOpen}>
                    Add Request
                </Button>
                <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
                    <DialogTitle id="form-dialog-title">Create shift</DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                            Please enter the time and date that you request to be covered.
                        </DialogContentText>
                        <MuiPickersUtilsProvider utils={DateFnsUtils}>
                            <Grid className={classes.grid} container justify="space-around">
                                <FormControl className={classes.select}>
                                    <InputLabel id="demo-customized-select-label">Group Name</InputLabel>
                                    <Select
                                        labelId="demo-customized-select-label"
                                        id="demo-customized-select"
                                        value={orgName}
                                        onChange={handleOrgChange}
                                    >
                                        {displayInfo.allOrgNames.map(function(orgN) {
                                            return <MenuItem value={orgN}>{orgN}</MenuItem>;
                                        })}
                                    </Select>
                                </FormControl>
                                <KeyboardDatePicker
                                    margin="normal"
                                    id="date-picker-dialog"
                                    label="Date"
                                    format="MM/dd/yyyy"
                                    value={selectedDate1}
                                    onChange={handleDateChange1}
                                    KeyboardButtonProps={{
                                        'aria-label': 'change date',
                                    }}
                                />
                                <KeyboardTimePicker
                                    margin="normal"
                                    id="time-picker"
                                    label="Start time"
                                    value={selectedDate1}
                                    onChange={handleDateChange1}
                                    KeyboardButtonProps={{
                                        'aria-label': 'change time',
                                    }}
                                />
                                <KeyboardTimePicker
                                    margin="normal"
                                    id="time-picker"
                                    label="End time"
                                    value={selectedDate2}
                                    onChange={handleDateChange2}
                                    KeyboardButtonProps={{
                                        'aria-label': 'change time',
                                    }}
                                />
                            </Grid>
                        </MuiPickersUtilsProvider>
                        <Alert severity="error" style={showError ? {} : {display: 'none'}}>{errorMsg}</Alert>
                    </DialogContent>
                    <DialogActions>
                        <Button color="default" onClick={handleClose}>
                            Cancel
                        </Button>
                        <Button color="default" onClick={handleSubmit}>
                            Create
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
            <div className={classes.alignContent}>
                <List className={classes.root} style={{maxHeight: 400, overflow: 'auto'}}>
                    {displayInfo.lstShifts.map(function(shift, index) {
                        for (let i=0; i<displayInfo.lstOrgs.length; i++) {
                            if (shift.orgId === displayInfo.lstOrgs[i]) {
                                return <Shift id={shift.id} orgId={shift.orgId} date={shift.date} displayStartTime={shift.displayStartTime}
                                              displayEndTime={shift.displayEndTime} createdById={shift.createdById} nameAsker={shift.nameAsker}
                                              userImg={shift.userImg} index={index} handleRequestChange={handleAcceptDecline}
                                              typeShift={"display"} orgName={displayInfo.lstOrgNames[i]} />
                            }
                        }
                    })}
                </List>
            </div>
        </div>
    );
}
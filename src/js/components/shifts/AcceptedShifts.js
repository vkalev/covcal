import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import Shift from "./Shift";

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
        marginTop: '1rem',
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

    const handleAcceptDecline = (requestID, isAccept) => {
        const copyLst = [];
        for (let i=0; i<displayInfo.lstShifts.length; i++) {
            if (displayInfo.lstShifts[i].id !== requestID) {
                copyLst.push(displayInfo.lstShifts[i]);
            }
        }
        displayInfo.handleSetShifts(copyLst);
    };

    return (
        <div className={classes.paper}>
            <div className={classes.alignContent}>
                <List className={classes.root} style={{maxHeight: 400, overflow: 'auto'}}>
                    {displayInfo.lstShifts.map(function(shift, index) {
                        for (let i=0; i<displayInfo.lstOrgs.length; i++) {
                            if (shift.orgId === displayInfo.lstOrgs[i]) {
                                return <Shift id={shift.id} orgId={shift.orgId} date={shift.date} displayStartTime={shift.displayStartTime}
                                              displayEndTime={shift.displayEndTime} createdById={shift.createdById} nameAsker={shift.nameAsker}
                                              userImg={shift.userImg} index={index} handleRequestChange={handleAcceptDecline}
                                              typeShift={"accepted"} orgName={displayInfo.lstOrgNames[i]}  />
                            }
                        }
                    })}
                </List>
            </div>
        </div>
    );
}
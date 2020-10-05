import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import ListItem from "@material-ui/core/ListItem";
import Avatar from "@material-ui/core/Avatar";
import ListItemText from "@material-ui/core/ListItemText";
import auth from "../../auth";
import {useHistory} from "react-router-dom";
import CardHeader from "@material-ui/core/CardHeader";

const useStyles = makeStyles({
    root: {
        minWidth: '30ch',
    },
    content: {
        display: 'flex',
        marginBottom: -15,
    },
    inline: {
        justifyContent: 'center',
        alignItems: 'center',
        display: 'flex',
    },
    title: {
        fontSize: 14,
    },
    text: {
        justifyContent: 'center',
        alignItems: 'center',
        marginBottom: 5,
    },
    accept: {
        alignSelf: "end",
        flexDirection: 'row',
        justifyContent: 'flex-end',
        display: 'flex',
        marginLeft: '6.5rem',
    },
    release: {
        display: 'flex',
        marginLeft: '11rem',
    },
    mine: {
        display: 'flex',
    },
    avatar: {
        '&:hover': {
            cursor: 'pointer',
        }
    },
    header: {
        marginBottom: -20,
    },
    button: {
        margin: 3,
    },
    label: {
        fontSize: '14px',
        marginLeft: '0.75rem',
        marginTop: '0.6rem',
        marginRight: '0.5rem',
    },
    emoji: {
        fontSize: '14px',
        marginTop: '0.6rem',
        marginRight: '5.5rem',
    },
});

function getHelpfulnessIcon(num) {
    if (num === -1) return "❄️";
    if (num === 0) return "";
    else return "🔥" + getHelpfulnessIcon(num - 1)
}

function getData(id) {
    const xhr = new XMLHttpRequest();

    const postParameters = {
        orgID: id
    };

    let workerMap = null;
    xhr.addEventListener('load', () => {
        workerMap = JSON.parse(xhr.response);
    });

    xhr.open('GET', 'http://localhost:5555/get_org_ledger/' + id,false);
    xhr.send(JSON.stringify(postParameters));
    return workerMap
}

function createFlame(props) {
    const workerMap = getData(props.orgId);
    let flames = '';
    flames += getHelpfulnessIcon(workerMap[props.createdById].rank);
    return flames;
}

export default function Shift(shiftInfo) {
    const classes = useStyles();
    const history2 = useHistory();

    const handleAccept = () => {
        const postParameters = {
            empID : auth.getCurrUser()
        };
        const xhr = new XMLHttpRequest();
        // get a callback when the server responds
        xhr.addEventListener('load', () => {
            shiftInfo.handleRequestChange(shiftInfo.id, true);
        });
        // open the request with the verb and the url
        xhr.open('POST', 'http://localhost:5555/accept_request?orgID=' + shiftInfo.id, false);
        xhr.send(JSON.stringify(postParameters));
    };

    const handleDecline = () => {
        const postParameters = {
            empID : auth.getCurrUser()
        };
        const xhr = new XMLHttpRequest();
        // get a callback when the server responds
        xhr.addEventListener('load', () => {
            shiftInfo.handleRequestChange(shiftInfo.id, false);
        });
        // open the request with the verb and the url
        xhr.open('POST', 'http://localhost:5555/decline_request?orgID=' + shiftInfo.id, false);
        xhr.send(JSON.stringify(postParameters));
    };

    const handleRelease = () => {
        const postParameters = {
            empID : auth.getCurrUser()
        };
        const xhr = new XMLHttpRequest();
        // get a callback when the server responds
        xhr.addEventListener('load', () => {
            shiftInfo.handleRequestChange(shiftInfo.id, true);
        });
        // open the request with the verb and the url
        xhr.open('POST', 'http://localhost:5555/release_shift?shiftID=' + shiftInfo.id + '&empID=' + auth.getCurrUser(), false);
        xhr.send(JSON.stringify(postParameters));
    };

    const handleCancel = () => {
        const postParameters = {
            empID : auth.getCurrUser()
        };
        const xhr = new XMLHttpRequest();
        // get a callback when the server responds
        xhr.addEventListener('load', () => {
            shiftInfo.handleRequestChange(shiftInfo.id, true);
        });
        // open the request with the verb and the url
        xhr.open('POST', 'http://localhost:5555/cancel_req?shiftID=' + shiftInfo.id + '&empID=' + auth.getCurrUser(), false);
        xhr.send(JSON.stringify(postParameters));
    };

    return (
        <ListItem alignItems="flex-start" id={shiftInfo.orgId}>
            <Card className={classes.root} variant="outlined">
                <CardHeader
                    className={classes.header}
                    avatar={<Avatar alt={shiftInfo.index} src={shiftInfo.userImg}
                                    className={classes.avatar}
                                    onClick={() => {
                                        history2.push({
                                            pathname: "/user_profile",
                                            state: {
                                                userId: shiftInfo.createdById,
                                                firstGetInfo : true,
                                                firstGetFacts : true,
                                            }})}}/>}
                    title={ shiftInfo.nameAsker + '   ' + createFlame(shiftInfo) }
                    subheader={ shiftInfo.orgName }
                />
                <CardContent className={classes.content}>
                    <ListItemText
                        className={classes.text}
                        secondary={
                            <React.Fragment>
                                <Typography
                                    component="span"
                                    variant="body2"
                                    className={classes.inline}
                                    color="textPrimary"
                                >
                                    {shiftInfo.date}
                                </Typography>
                                <Typography
                                    component="span"
                                    variant="body2"
                                    className={classes.inline}
                                >
                                {shiftInfo.displayStartTime} - {shiftInfo.displayEndTime}
                                </Typography>
                            </React.Fragment>
                        }
                    />
                </CardContent>
                <CardActions>
                    <div>
                        {(() => {
                            switch (shiftInfo.typeShift) {
                                case "display":   return <div className={classes.accept}>
                                                            <Button color="default" className={classes.button} onClick={handleDecline}>
                                                                Decline
                                                            </Button>
                                                            <Button color="default" className={classes.button} onClick={handleAccept}>
                                                                Accept
                                                            </Button>
                                                        </div>;
                                case "accepted": return <div className={classes.release}>
                                                            <Button color="default" className={classes.button} onClick={handleRelease}>
                                                                Release
                                                            </Button>
                                                        </div>;
                                case "mine":  return <div className={classes.mine}>
                                                            <Typography className={classes.label} > {shiftInfo.isAccepted ? shiftInfo.donorName : "Pending"} </Typography>
                                                            <Typography className={classes.emoji} > {shiftInfo.isAccepted ? " ✅" : " 🕗"} </Typography>
                                                            <Button color="default" className={classes.button} onClick={handleCancel}>
                                                                Cancel
                                                            </Button>
                                                        </div>;
                            }
                        })()}
                    </div>
                </CardActions>
            </Card>
        </ListItem>
    );
}

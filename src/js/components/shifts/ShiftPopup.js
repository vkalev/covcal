import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Avatar from "@material-ui/core/Avatar";
import ListItemText from "@material-ui/core/ListItemText";
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
        marginLeft: '6rem',
    },
    release: {
        alignSelf: "end",
        flexDirection: 'row',
        justifyContent: 'flex-end',
        display: 'flex',
        marginLeft: '10rem',
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
});

function getHelpfulnessIcon(num) {
    if (num === -1) return "❄";
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
    for (const worker in workerMap) {
        if (workerMap[worker].id === props.createdById) {
            flames += getHelpfulnessIcon(workerMap[worker].rank);
        }
    }
    return flames;
}

export default function ShiftPopup(shiftInfo) {
    const classes = useStyles();
    const history2 = useHistory();


    return (
        <Card className={classes.root} variant="outlined">
            <CardHeader
                className={classes.header}
                avatar={<Avatar alt={1} src={shiftInfo.userImg}
                                className={classes.avatar}
                                onClick={() => {
                                    history2.push({
                                        pathname: "/user_profile",
                                        state: {
                                            userId: shiftInfo.createdById,
                                            firstGetInfo : true,
                                            firstGetFacts : true,
                                        }})}}/>}
                title={ shiftInfo.title }
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
                                {shiftInfo.start} - {shiftInfo.end}
                            </Typography>
                        </React.Fragment>
                    }
                />
            </CardContent>
        </Card>
    );
}

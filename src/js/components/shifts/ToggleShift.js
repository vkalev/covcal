import React from 'react';
import PropTypes from 'prop-types';
import SwipeableViews from 'react-swipeable-views';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import { green } from '@material-ui/core/colors';
import Box from '@material-ui/core/Box';
import InsertInvitationIcon from '@material-ui/icons/InsertInvitation';
import ArchiveIcon from '@material-ui/icons/Archive';
import PersonIcon from '@material-ui/icons/Person';
import Tooltip from "@material-ui/core/Tooltip";

function TabPanel(props) {
    const { children, value, index, ...other } = props;

    return (
        <Typography
            component="div"
            role="tabpanel"
            hidden={value !== index}
            id={`action-tabpanel-${index}`}
            aria-labelledby={`action-tab-${index}`}
            {...other}
        >
            {value === index && <Box p={3}>{children}</Box>}
        </Typography>
    );
}

TabPanel.propTypes = {
    children: PropTypes.node,
    index: PropTypes.any.isRequired,
    value: PropTypes.any.isRequired,
};

function a11yProps(index) {
    return {
        id: `action-tab-${index}`,
        'aria-controls': `action-tabpanel-${index}`,
    };
}

const useStyles = makeStyles((theme) => ({
    root: {
        marginTop: '1.5rem',
        backgroundColor: theme.palette.background.paper,
        width: 300,
        position: 'relative',
        minHeight: 200,
    },
    fab: {
        position: 'absolute',
        bottom: theme.spacing(2),
        right: theme.spacing(2),
    },
    fabGreen: {
        color: theme.palette.common.white,
        backgroundColor: green[500],
        '&:hover': {
            backgroundColor: green[600],
        },
    },
    tab: {
        minWidth: 100,
        width: 100
    },
    appBar: {
        minWidth: 300,
        width: 300
    }
}));

export default function ToggleShift(props) {
    const classes = useStyles();
    const theme = useTheme();
    const [value, setValue] = React.useState(0);

    const handleChange = (event, newValue) => {
        setValue(newValue);
        props.handleToggleChange(newValue);
    };

    const handleChangeIndex = (index) => {
        setValue(index);
    };

    const transitionDuration = {
        enter: theme.transitions.duration.enteringScreen,
        exit: theme.transitions.duration.leavingScreen,
    };


    return (
        <div className={classes.root}>
            <AppBar position="static" color="default" classes={{ root: classes.appBar }}>
                <Tabs
                    value={value}
                    onChange={handleChange}
                    indicatorColor="primary"
                    textColor="primary"
                    aria-label="action tabs example"
                >
                    <Tooltip title="Current Requests" aria-label="add">
                        <Tab classes={{ root: classes.tab }} icon={<InsertInvitationIcon />} {...a11yProps(0)} />
                    </Tooltip>
                    <Tooltip title="Accepted Requests" aria-label="add">
                        <Tab classes={{ root: classes.tab }} icon={<ArchiveIcon />} {...a11yProps(1)} />
                    </Tooltip>
                    <Tooltip title="My Requests" aria-label="add">
                        <Tab classes={{ root: classes.tab }} icon={<PersonIcon />} {...a11yProps(2)} />
                    </Tooltip>
                </Tabs>
            </AppBar>
            <SwipeableViews
                axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
                index={value}
                onChangeIndex={handleChangeIndex}
            >
                <TabPanel value={value} index={0} dir={theme.direction}>
                    {props.shiftList}
                </TabPanel>
                <TabPanel value={value} index={1} dir={theme.direction}>
                    {props.acceptedShifts}
                </TabPanel>
                <TabPanel value={value} index={2} dir={theme.direction}>
                    {props.myShifts}
                </TabPanel>
            </SwipeableViews>
        </div>
    );
}

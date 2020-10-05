import { Calendar, dateFnsLocalizer } from 'react-big-calendar'
import format from 'date-fns/format'
import parse from 'date-fns/parse'
import startOfWeek from 'date-fns/startOfWeek'
import getDay from 'date-fns/getDay'
import React from "react";
import 'react-big-calendar/lib/css/react-big-calendar.css';
import Dialog from "@material-ui/core/Dialog";
import ShiftPopup from "../shifts/ShiftPopup";

const locales = {
    'en-US': require('date-fns/locale/en-US'),
};

const localizer = dateFnsLocalizer({
    format,
    parse,
    startOfWeek,
    getDay,
    locales,
});

export default class ShiftCalendar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            filteredShiftList: [],
            firstRender: false,
            openPopup: false,
            popupTitle: "",
            popupStart: "",
            popupEnd: "",
            popupImg: "",
            popupCreatorID: "",
            popupOrgName: ""
        };
        this.getFilteredShiftList = this.getFilteredShiftList.bind(this);
        this.getEventList = this.getEventList.bind(this);
        this.handleCalEventClick = this.handleCalEventClick.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }

    getFilteredShiftList = (typeShifts) => {
        const filteredShifts = [];
        let shift;
        let tempShiftLst = this.props.lstShifts;
        if (typeShifts === "accepted") {
            tempShiftLst = this.props.lstAcceptedShifts;
        } else if (typeShifts === "mine") {
            tempShiftLst = this.props.lstMyShifts;
        }
        for (let i=0; i<tempShiftLst.length; i++) {
            shift = tempShiftLst[i];
            for (let j=0; j<this.props.lstOrgs.length; j++) {
                if (shift.orgId === this.props.lstOrgs[j]) {
                    filteredShifts.push(shift);
                    break;
                }
            }
        }
        return filteredShifts;
    };

    getEventList = () => {
        const showDisplay = this.props.toggleIndex === 0;
        const showAccept = this.props.toggleIndex === 1;
        const showMine = this.props.toggleIndex === 2;
        let eventList;
        eventList = this.getFilteredShiftList("display").map(function(shift, index){
            return {
                id: index,
                title: shift.nameAsker + '\'s shift',
                start: new Date(shift.yearNum, shift.monthNum, shift.dayOfMonth, shift.startHour, shift.startMinutes),
                end: new Date(shift.yearNum, shift.monthNum, shift.dayOfMonth, shift.endHour, shift.endMinutes),
                userImg: shift.userImg,
                createdById: shift.createdById,
                orgName: shift.orgName,
                currShow: showDisplay,
                type: "display"
            };
        });
        eventList = eventList.concat(this.getFilteredShiftList("accepted").map(function(shift, index){
            return {
                id: index,
                title: shift.nameAsker + '\'s shift',
                start: new Date(shift.yearNum, shift.monthNum, shift.dayOfMonth, shift.startHour, shift.startMinutes),
                end: new Date(shift.yearNum, shift.monthNum, shift.dayOfMonth, shift.endHour, shift.endMinutes),
                userImg: shift.userImg,
                createdById: shift.createdById,
                orgName: shift.orgName,
                currShow: showAccept,
                type: "accepted"
            };
        }));
        eventList = eventList.concat(this.getFilteredShiftList("mine").map(function(shift, index){
            return {
                id: index,
                title: shift.nameAsker + '\'s shift',
                start: new Date(shift.yearNum, shift.monthNum, shift.dayOfMonth, shift.startHour, shift.startMinutes),
                end: new Date(shift.yearNum, shift.monthNum, shift.dayOfMonth, shift.endHour, shift.endMinutes),
                userImg: shift.userImg,
                createdById: shift.createdById,
                orgName: shift.orgName,
                currShow: showMine,
                type: "mine"
            };
        }));
        return eventList;
    };

    eventStyleGetter = (event) => {
        let backgroundColor;
        if (event.type === "display") {
            backgroundColor = '#BB64FF';
            if (!event.currShow) {
                backgroundColor = '#F1DEFF';
            }
        } else if (event.type === "accepted") {
            backgroundColor = '#61E74E';
            if (!event.currShow) {
                backgroundColor = '#C9FFC9';
            }
        } else if (event.type === "mine") {
            backgroundColor = '#56C2FF';
            if (!event.currShow) {
                backgroundColor = '#D8F1FF';
            }
        }
        const style = {
            backgroundColor: backgroundColor,
            borderRadius: '0px',
            opacity: 0.8,
            color: 'white',
            border: '0px',
            display: 'block'
        };
        return {
            style: style
        };
    };

    getStringStartDate = (date) => {
        let dayName;
        if (date.getDay() === 0) {
            dayName = "Sunday";
        } else if (date.getDay() === 1) {
            dayName = "Monday";
        } else if (date.getDay() === 2) {
            dayName = "Tuesday";
        } else if (date.getDay() === 3) {
            dayName = "Wednesday";
        } else if (date.getDay() === 4) {
            dayName = "Thursday";
        } else if (date.getDay() === 5) {
            dayName = "Friday";
        } else if (date.getDay() === 6) {
            dayName = "Saturday";
        }
        let monthName;
        if (date.getMonth() === 0) {
            monthName = "January";
        } else if (date.getMonth() === 1) {
            monthName = "February";
        }  else if (date.getMonth() === 2) {
            monthName = "March";
        }  else if (date.getMonth() === 3) {
            monthName = "April";
        }  else if (date.getMonth() === 4) {
            monthName = "May";
        } else if (date.getMonth() === 5) {
            monthName = "June";
        }  else if (date.getMonth() === 6) {
            monthName = "July";
        }  else if (date.getMonth() === 7) {
            monthName = "August";
        }  else if (date.getMonth() === 8) {
            monthName = "September";
        }  else if (date.getMonth() === 9) {
            monthName = "October";
        }  else if (date.getMonth() === 10) {
            monthName = "November";
        }  else if (date.getMonth() === 11) {
            monthName = "December";
        }
        let output = dayName + " " + monthName + date.getDate() + ", ";
        output += date.getFullYear() + ": ";
        output += this.getStringOfTime(date);
        return output;
    };

    getStringOfTime = (date) => {
        let amPm;
        let numHours = date.getHours();
        let numMinutes = date.getMinutes();
        let extraMinute = "";
        if (numHours >= 12) {
            amPm = "PM";
            if (numHours !== 12) {
                numHours -= 12;
            }
        } else {
            amPm = "AM";
            if (numHours === 0) {
                numHours = 12;
            }
        }
        if (numMinutes < 10) {
            extraMinute = "0";
        }
        return numHours + ":" + extraMinute + numMinutes + amPm;
    };

    handleCalEventClick = (event) => {
        this.setState({openPopup : true, popupTitle : event.title, popupStart : this.getStringStartDate(event.start),
            popupEnd : this.getStringOfTime(event.end), popupImg : event.userImg, popupCreatorID: event.createdById,
            popupOrgName : event.orgName});
    };

    handleClose = () => {
        this.setState({openPopup : false, popupTitle : "", popupStart : "", popupEnd : "", popupImg : "",
            popupCreatorID : "", popupOrgName : ""});
    };

    render() {

        const eventList = this.getEventList();

        return (
            <div>
                <Dialog open={this.state.openPopup} onClose={this.handleClose} aria-labelledby="form-dialog-title">
                    <ShiftPopup title={this.state.popupTitle} start={this.state.popupStart} end={this.state.popupEnd}
                                userImg={this.state.popupImg} createdById={this.state.popupCreatorID}
                                orgName={this.state.popupOrgName} />
                </Dialog>
                <Calendar
                    popup
                    localizer={localizer}
                    events={eventList}
                    startAccessor="start"
                    endAccessor="end"
                    onSelectEvent={this.handleCalEventClick}
                    style={{height: 500, width: 675, marginTop: '1.5rem'}}
                    eventPropGetter={(this.eventStyleGetter)}
                />
            </div>
        )
    }
};
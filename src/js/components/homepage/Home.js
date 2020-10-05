import React from "react";
import MenuBar from "./MenuBar";
import ShiftList from "../shifts/ShiftList";
import HomePopUp from "./HomePopUp";
import auth from '../../auth';
import GroupList from "../groups/GroupList";
import ShiftCalendar from "./Calendar";
import "../../../css/opensans.css"
import { createMuiTheme, ThemeProvider } from '@material-ui/core/styles'
import ToggleShift from "../shifts/ToggleShift";
import AcceptedShifts from "../shifts/AcceptedShifts";
import MyShifts from "../shifts/MyShifts";

const theme = createMuiTheme({
    typography: {
        fontFamily: 'Open Sans',
    },
    palette: {
        primary: {
            main: '#BB64FF',
        },
    },
});

const content = {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'space-between',
};

export default class Home extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            checkedOrgs : [],
            lstOrgNames: [],
            lstOrgIDs: [],
            lstShifts: [],
            lstAcceptedShifts: [],
            lstMyShifts: [],
            firstRender: false,
            toggleIndex: 0
        };
        this.handleGroupChange = this.handleGroupChange.bind(this);
        this.setCheckedOrgs = this.setCheckedOrgs.bind(this);
        this.handleAddJoinGroup = this.handleAddJoinGroup.bind(this);
        this.handleSetShifts = this.handleSetShifts.bind(this);
        this.handleGetShifts = this.handleGetShifts.bind(this);
        this.handleGetAcceptedShifts = this.handleGetAcceptedShifts.bind(this);
        this.handleSetAcceptedShifts = this.handleSetAcceptedShifts.bind(this);
        this.handleToggleChange = this.handleToggleChange.bind(this);
        this.handleGetMyShifts = this.handleGetMyShifts.bind(this);
        this.handleSetMyShifts = this.handleSetMyShifts.bind(this);
    }

    setCheckedOrgs(lstOrgs) {
        const checkCopy = [];
        const allOrgNames = [];
        for (let i=0; i<lstOrgs.length; i++) {
            checkCopy.push(lstOrgs[i].id);
            allOrgNames.push(lstOrgs[i].name);
        }
        this.setState(() => { return {checkedOrgs : checkCopy, checkedOrgNames : allOrgNames,
            lstOrgNames : allOrgNames, lstOrgIDs : checkCopy}});
    }

    handleGroupChange(event, checked) {
        const groupID = parseInt(event.target.name);
        if (checked) {
            this.setState((state) => {
                const index = state.lstOrgIDs.indexOf(groupID);
                state.checkedOrgNames.push(state.lstOrgNames[index]);
                state.checkedOrgs.push(groupID);
                return {checkedOrgs : state.checkedOrgs, checkedOrgNames : state.checkedOrgNames};
            });
        } else {
            const copyChecked = [];
            const copyCheckedNames = [];
            const {checkedOrgs, checkedOrgNames} = this.state;
            for (let i=0; i<checkedOrgs.length; i++) {
                if (checkedOrgs[i] !== groupID) {
                    copyCheckedNames.push(checkedOrgNames[i]);
                    copyChecked.push(checkedOrgs[i]);
                }
            }
            this.setState(() => { return { checkedOrgs : copyChecked, checkedOrgNames : copyCheckedNames } });
        }
    };

    handleGetShifts() {
        const xhr = new XMLHttpRequest();
        let firstRenderGetShifts = false;
        // get a callback when the server responds
        xhr.addEventListener('load', () => {
            // update the state of the component with the result here
            if (!firstRenderGetShifts) {
                firstRenderGetShifts = true;
                this.setState({lstShifts : JSON.parse(xhr.response)});
            }
        });
        // open the request with the verb and the url
        xhr.open('POST', 'http://localhost:5555/get_display_requests/' + auth.getCurrUser(), false);
        xhr.send();
    };

    handleGetAcceptedShifts() {
        const xhr = new XMLHttpRequest();
        let firstRenderGetShifts = false;
        // get a callback when the server responds
        xhr.addEventListener('load', () => {
            // update the state of the component with the result here
            if (!firstRenderGetShifts) {
                firstRenderGetShifts = true;
                this.setState({lstAcceptedShifts : JSON.parse(xhr.response)});
            }
        });
        // open the request with the verb and the url
        xhr.open('POST', 'http://localhost:5555/get_accepted_reqs/' + auth.getCurrUser(), false);
        xhr.send();
    };

    handleGetMyShifts() {
        const xhr = new XMLHttpRequest();
        let firstRenderGetShifts = false;
        // get a callback when the server responds
        xhr.addEventListener('load', () => {
            // update the state of the component with the result here
            if (!firstRenderGetShifts) {
                firstRenderGetShifts = true;
                this.setState({lstMyShifts : JSON.parse(xhr.response)});
            }
        });
        // open the request with the verb and the url
        xhr.open('POST', 'http://localhost:5555/get_reqs/' + auth.getCurrUser(), false);
        xhr.send();
    };


    handleSetShifts(shiftLst, isAccept) {
        this.setState({lstShifts : shiftLst});
        if (isAccept) {
            this.handleGetAcceptedShifts();
        }
    }

    handleSetAcceptedShifts(shiftLst) {
        this.setState({lstAcceptedShifts : shiftLst});
        this.handleGetAcceptedShifts();
    }

    handleSetMyShifts(shiftLst) {
        this.setState({lstMyShifts : shiftLst});
    }

    getJoin(props) {
        try {
            return this.props.history.location.state.join;
        } catch (e) {
            return false;
        }
    }

    handleAddJoinGroup(group) {
        this.setState((state) => {
            state.checkedOrgs.push(group.id);
            state.checkedOrgNames.push(group.name);
            return { checkedOrgs : state.checkedOrgs, lstOrgNames : state.lstOrgNames, lstOrgIDs : state.lstOrgIDs };
        }, () => {this.handleGetShifts();
        this.handleGetAcceptedShifts();
        this.handleGetMyShifts();});
    };

    handleToggleChange(newIndex) {
        this.setState({toggleIndex : newIndex});
    }

    render() {
        let toJoin = this.getJoin(this.props);

        if (!this.state.firstRender) {
            this.handleGetShifts();
            this.handleGetAcceptedShifts();
            this.handleGetMyShifts();
            this.setState({firstRender : true});
        }
        const maxWidthToggle = {
            width : '100px'
        };
        return (
            <div>
                <ThemeProvider theme={theme}>
                    <MenuBar history={this.props.history}/>
                    <div style={content}>
                        <ToggleShift
                            style={maxWidthToggle}
                            handleToggleChange={this.handleToggleChange}
                            shiftList={<ShiftList lstShifts={this.state.lstShifts} handleGetMyShifts={this.handleGetMyShifts}
                                                  handleSetShifts={this.handleSetShifts} lstOrgs={this.state.checkedOrgs}
                                                  lstOrgNames={this.state.checkedOrgNames} allOrgNames={this.state.lstOrgNames}
                                                  allOrgIDs={this.state.lstOrgIDs}/>}
                            myShifts={<MyShifts lstShifts={this.state.lstMyShifts} handleSetShifts={this.handleSetMyShifts}
                                                lstOrgs={this.state.checkedOrgs} lstOrgNames={this.state.checkedOrgNames}
                                                allOrgNames={this.state.lstOrgNames} allOrgIDs={this.state.lstOrgIDs}/>}
                            acceptedShifts={<AcceptedShifts lstShifts={this.state.lstAcceptedShifts} handleSetShifts={this.handleSetAcceptedShifts}
                                                            lstOrgs={this.state.checkedOrgs} lstOrgNames={this.state.checkedOrgNames}
                                                            allOrgNames={this.state.lstOrgNames} allOrgIDs={this.state.lstOrgIDs}/>}
                        />
                        <ShiftCalendar toggleIndex={this.state.toggleIndex} lstShifts={this.state.lstShifts}
                                       lstAcceptedShifts={this.state.lstAcceptedShifts} lstMyShifts={this.state.lstMyShifts}
                                       lstOrgs={this.state.checkedOrgs} />
                        <GroupList autoJoin={toJoin} handleChecked={this.handleGroupChange} handleSet={this.setCheckedOrgs}
                                   handleJoin={this.handleAddJoinGroup} />
                    </div>
                    <HomePopUp/>
                </ThemeProvider>
            </div>
        );
    }
}

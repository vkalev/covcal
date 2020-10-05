import React from 'react';
import auth from "../../auth";
import MenuBar from "../homepage/MenuBar";
import ProfilePic from "./ProfilePic";

// Styling:
const classes = {
    imageCard: {
        paddingTop: 10,
        border: 10,
        borderColor: "#ff2200",
    },
    attributes: {
        paddingTop: 10,
        paddingRight: 50,
    }
};

export default class Home extends React.Component {
    render() {
        return (
            <div align={'center'}>
                <MenuBar history={this.props.history}/>
                <div>
                    <div style={classes.imageCard}>
                        <ProfilePic picture={auth.getImage()} name={auth.getName()} isOwner={true}/>
                    </div>
                </div>
            </div>
        );
    }
}
import React from 'react';
import Card from '@material-ui/core/Card';
import CardMedia from '@material-ui/core/CardMedia';
import Typography from '@material-ui/core/Typography';
import IconButton from "@material-ui/core/IconButton";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import Dialog from "@material-ui/core/Dialog";
import Button from "@material-ui/core/Button";
import DialogActions from "@material-ui/core/DialogActions";
import auth from "../../auth";
import CancelIcon from '@material-ui/icons/Cancel';
import GridListTile from "@material-ui/core/GridListTile";
import GridList from "@material-ui/core/GridList";
import {makeStyles} from "@material-ui/core/styles";

// Styling:
const useStyles = makeStyles((theme) => ({
    root: {
        maxWidth: 200,
        marginRight: '2rem',
    },
    media: {
        height: 200,
        width: 200,
    },
    button: {
        display: 'flex',
        marginLeft: -15,
        marginTop: '1rem',
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
    },
    title: {
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    tile: {
        cursor: 'pointer',
        '&:hover': {
            background: "#BB64FF",
        },
    },
}));

const imageGallery = [
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/health-worker_1f9d1-200d-2695-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-health-worker_1f468-200d-2695-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-health-worker_1f469-200d-2695-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/student_1f9d1-200d-1f393.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-student_1f468-200d-1f393.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-student_1f469-200d-1f393.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/teacher_1f9d1-200d-1f3eb.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-teacher_1f468-200d-1f3eb.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-teacher_1f469-200d-1f3eb.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/judge_1f9d1-200d-2696-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-judge_1f468-200d-2696-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-judge_1f469-200d-2696-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/farmer_1f9d1-200d-1f33e.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-farmer_1f468-200d-1f33e.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-farmer_1f469-200d-1f33e.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/cook_1f9d1-200d-1f373.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-cook_1f468-200d-1f373.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-cook_1f469-200d-1f373.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/mechanic_1f9d1-200d-1f527.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-mechanic_1f468-200d-1f527.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-mechanic_1f469-200d-1f527.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/factory-worker_1f9d1-200d-1f3ed.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-factory-worker_1f468-200d-1f3ed.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-factory-worker_1f469-200d-1f3ed.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/office-worker_1f9d1-200d-1f4bc.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-office-worker_1f468-200d-1f4bc.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-office-worker_1f469-200d-1f4bc.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/scientist_1f9d1-200d-1f52c.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-scientist_1f468-200d-1f52c.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-scientist_1f469-200d-1f52c.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/technologist_1f9d1-200d-1f4bb.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-technologist_1f468-200d-1f4bb.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-technologist_1f469-200d-1f4bb.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/singer_1f9d1-200d-1f3a4.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-singer_1f468-200d-1f3a4.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-singer_1f469-200d-1f3a4.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/artist_1f9d1-200d-1f3a8.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-artist_1f468-200d-1f3a8.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-artist_1f469-200d-1f3a8.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/pilot_1f9d1-200d-2708-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-pilot_1f468-200d-2708-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-pilot_1f469-200d-2708-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/astronaut_1f9d1-200d-1f680.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-astronaut_1f468-200d-1f680.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-astronaut_1f469-200d-1f680.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/firefighter_1f9d1-200d-1f692.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-firefighter_1f468-200d-1f692.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-firefighter_1f469-200d-1f692.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/sleuth-or-spy_1f575.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-sleuth_1f575-fe0f-200d-2642-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-sleuth_1f575-fe0f-200d-2640-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/guardsman_1f482.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-guard_1f482-200d-2642-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-guard_1f482-200d-2640-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/construction-worker_1f477.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/male-construction-worker_1f477-200d-2642-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/female-construction-worker_1f477-200d-2640-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/superhero_1f9b8.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/man-superhero_1f9b8-200d-2642-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/woman-superhero_1f9b8-200d-2640-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/supervillain_1f9b9.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/man-supervillain_1f9b9-200d-2642-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/woman-supervillain_1f9b9-200d-2640-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/mage_1f9d9.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/man-mage_1f9d9-200d-2642-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/woman-mage_1f9d9-200d-2640-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/fairy_1f9da.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/man-fairy_1f9da-200d-2642-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/woman-fairy_1f9da-200d-2640-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/vampire_1f9db.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/man-vampire_1f9db-200d-2642-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/woman-vampire_1f9db-200d-2640-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/merperson_1f9dc.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/merman_1f9dc-200d-2642-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/merwoman_1f9dc-200d-2640-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/elf_1f9dd.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/man-elf_1f9dd-200d-2642-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/woman-elf_1f9dd-200d-2640-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/genie_1f9de.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/man-genie_1f9de-200d-2642-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/woman-genie_1f9de-200d-2640-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/zombie_1f9df.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/man-zombie_1f9df-200d-2642-fe0f.png",
    "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/144/apple/237/woman-zombie_1f9df-200d-2640-fe0f.png",
];

export default function ProfilePic(props) {
    const classes = useStyles();

    const [open, setOpen] = React.useState(false);

    const handleOpen = () => {
        setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
    };
    
    const GalleryClickHandler = (imageUrl) => {
        return function () {
            // First, we update the cached image and close the modal
            auth.image = imageUrl;
            handleClose();
            // create a new XMLHttpRequest
            const xhr = new XMLHttpRequest();
            xhr.addEventListener('load', () => {
                props.handleImgChange(imageUrl)
            });
            // open the request with the verb and the url
            xhr.open('POST', 'http://localhost:5555/user_profile/upload_image');
            const postParameters = {
                userId: auth.idToken,
                newImage: imageUrl
            };
            // send the request
            xhr.send(JSON.stringify(postParameters));
        }
    };

    const Gallery = () => {
        return (
            <Dialog open={open} onClose={() => handleClose()}>
                <DialogTitle className={classes.title} >{"Choose a new profile picture!"}</DialogTitle>
                <DialogContent>
                    <GridList cellHeight={160} cols={3}>
                        {imageGallery.map((tile) => (
                            <GridListTile className={classes.tile} onClick={GalleryClickHandler(tile)}>
                                <img src={tile}  alt={'https://jooinn.com/images/white-11.jpg'}/>
                            </GridListTile>
                        ))}
                    </GridList>
                </DialogContent>
                <DialogActions>
                    <IconButton onClick={() => handleClose()}>
                        <CancelIcon/>
                    </IconButton>
                </DialogActions>
            </Dialog>
        )
    };

    return (
        <div>
            <Card className={classes.root}>
                <CardMedia
                    className={classes.media}
                    image={props.picture}
                    title="Profile Picture"
                />
            </Card>
            <Gallery/>
            { props.isOwner ? <Button className={classes.button} variant="contained" onClick={() => handleOpen()}>
                <Typography> Change Profile Picture </Typography> </Button> : null }
        </div>
    )
}
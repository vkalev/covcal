import React from 'react';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Typography from '@material-ui/core/Typography';
import IconButton from "@material-ui/core/IconButton";
import EditIcon from "@material-ui/icons/Edit";
import PhotoCameraIcon from "@material-ui/icons/PhotoCamera";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import Dialog from "@material-ui/core/Dialog";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import DialogActions from "@material-ui/core/DialogActions";
import auth from "../../auth";
import CancelIcon from '@material-ui/icons/Cancel';
import GridListTile from "@material-ui/core/GridListTile";
import GridList from "@material-ui/core/GridList";
import GridListTileBar from "@material-ui/core/GridListTileBar";
import CheckIcon from '@material-ui/icons/Check';

// Styling:
const classes = {
    root: {
        maxWidth: 345,
    },
    media: {
        height: 345,
        width: 345,
    },
    button: {
        background: 'linear-gradient(45deg, #BB64FF 30%, #d48eff 90%)',
        color: 'White',
        margin: 5,
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
    },
    title: {
        background: 'linear-gradient(45deg, #BB64FF 30%, #d48eff 60%)',
        color: 'white',
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    tile: {
        background: 'rgba(0, 0, 0, 0)'
    },
    icon: {
        background: 'linear-gradient(45deg, #BB64FF 30%, #d48eff 60%)',
        color: 'white',
    },
};
const imageGallery = [
    "https://theofficeanalytics.files.wordpress.com/2017/11/dwight.jpeg?w=1200",
    "https://imgix.bustle.com/uploads/image/2018/1/29/66b4ebf6-4606-430e-9dfe-d8c9bdefd6d8-screen-shot-2018-01-29-at-11654-pm.png?w=970&h=546&fit=crop&crop=faces&auto=format%2Ccompress&cs=srgb&q=70",
    "https://www.dailyactor.com/wp-content/uploads/2013/05/john-krasinski-the-office-interview.jpg",
    "https://i.pinimg.com/originals/e5/67/cc/e567cc9331491412a1e53e83c7766a1a.jpg",
    "https://static2.srcdn.com/wordpress/wp-content/uploads/2019/04/Kevin-Malone-in-The-Office.jpg",
    "https://miro.medium.com/max/3224/1*N1i2yo1XdW3GHLEPF0sUiA.png",
    "https://media1.popsugar-assets.com/files/thumbor/eXDFklp6xdWXLyvn9BcH5p-b_rk/fit-in/2048xorig/filters:format_auto-!!-:strip_icc-!!-/2016/06/21/951/n/1922283/05e3e789_edit_img_image_41730517_1466542783_kelly-th/i/Kelly-Kapoor-GIFs-From-Office.jpg",
    "https://www.business.com/images/content/5ca/3d4125a215e8a458b7996/1500-0-",
    "https://media.architecturaldigest.com/photos/58f52055af560d04a46dba3b/3:4/w_1181,h_1574,c_limit/GettyImages-141187430%20(1).jpg",
    "https://www.cheap-neckties.com/blog/uploads/andy-bernard-best-dressed-fashion.jpg",
    "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F6%2F2007%2F01%2F15251__melora_l.jpg",
    "https://tvguide1.cbsistatic.com/i/r/2007/04/05/4d297069-8139-4edc-8483-d00a10ad0148/thumbnail/900x600/a0fce02ce60fe2a96d0e43f637923cf9/C662783F-0F97-4659-B6E1-1AB8D7F7A0C4.jpg",
    "https://pawneeindiana.com/wp-content/themes/g6f07bu02zdv3txfv7a8t621759/files/images/promos/ron-proud.jpg",
    "https://img.women.com/images/images/000/181/700/large/andy-parks-and-rec-quotes-header-053019.jpg?1559238774",
    "https://vignette.wikia.nocookie.net/parksandrecreation/images/c/cb/Janet_Snakehole.png/revision/latest/top-crop/width/360/height/450?cb=20160225132511",
    "https://images.theconversation.com/files/301743/original/file-20191114-26207-lray93.jpg?ixlib=rb-1.1.0&q=45&auto=format&w=926&fit=clip",
    "https://emborapets.com/wp-content/uploads/2019/04/pug.jpg",
    "https://www.petnpat.com/wp-content/uploads/2018/05/best-bunny-treats.jpg",
];

export default function OrgPic(props) {
    const [open, setOpen] = React.useState(false);

    const handleOpen = () => {
        setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
    };

    const [nameOpen, nameSetOpen] = React.useState(false);

    const handleNameOpen = () => {
        nameSetOpen(true);
    };

    const handleNameClose = () => {
        nameSetOpen(false);
    };

    const GalleryClickHandler = (imageUrl) => {
        return function () {
            // First, we update the cached image and close the modal
            auth.image = imageUrl;
            handleClose();
            // create a new XMLHttpRequest
            const xhr = new XMLHttpRequest();
            xhr.addEventListener('load', () => {
            });
            // open the request with the verb and the url
            xhr.open('POST', 'http://localhost:5555/user_profile/upload_image');
            const postParameters = {
                userId: auth.idToken,
                newImage: imageUrl
            };
            // send the request
            xhr.send(JSON.stringify(postParameters));
            window.location.reload();
        }
    };

    const EditName = () => {
        let newName = props.name;
        let newDesc = props.description;

        const handleNameChange = (event) => {
            const target = event.target;
            newName = target.value;
        };
        const handleDescChange = (event) => {
            const target = event.target;
            newDesc = target.value;
        };

        const handleSubmit = (event) => {
            const postParameters = {
                orgId: props.id,
                newName: newName,
                newDesc: newDesc,
            };

            // create a new XMLHttpRequest
            const nameXhr = new XMLHttpRequest();

            // get a callback when the server responds
            nameXhr.addEventListener('load', () => {
                // update the state of the component with the result here
                auth.name = newName;
            });
            // open the request with the verb and the url
            nameXhr.open('POST', 'http://localhost:5555/org_profile/upload_name');
            // send the request
            nameXhr.send(JSON.stringify(postParameters));
            window.location.reload();
        };

        return (
            <Dialog
                open={nameOpen} onClose={() => handleNameClose}>
                <DialogTitle style={classes.title}>Edit Name and Description</DialogTitle>
                <form noValidate autoComplete="off">
                    <div style={classes.form}>
                        <TextField
                            required
                            id="outlined-required"
                            defaultValue={props.name}
                            name="nameValue"
                            onChange={handleNameChange}/>
                        <TextField
                            required
                            id="outlined-required"
                            defaultValue={props.description}
                            name="descValue"
                            onChange={handleDescChange}/>
                    </div>
                    <div align={'center'}>
                        <Button style={classes.button} onClick={handleNameClose}>
                            Cancel
                        </Button>
                        <Button style={classes.button} onClick={handleSubmit}>
                            Save
                        </Button>
                    </div>
                </form>
            </Dialog>
        )
    };

    const Gallery = () => {
        return (
            <Dialog open={open} onClose={() => handleClose()}>
                <DialogTitle style={classes.title}>{"Choose a new profile picture!"}</DialogTitle>
                <DialogContent>
                    <GridList cellHeight={160} cols={3}>
                        {imageGallery.map((tile) => (
                            <GridListTile>
                                <img src={tile} alt={'https://jooinn.com/images/white-11.jpg'}/>
                                <GridListTileBar style={classes.tile}
                                                 actionIcon={
                                                     <IconButton style={classes.icon}
                                                                 onClick={GalleryClickHandler(tile)}>
                                                         <CheckIcon/>
                                                     </IconButton>
                                                 }/>
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

    const Buttons = () => (
        <div>
            <IconButton title="edit name"
                        onClick={() => handleNameOpen()}>
                <EditIcon/>
            </IconButton>
            <IconButton title="change image"
                        onClick={() => handleOpen()}>
                <PhotoCameraIcon/>
            </IconButton>
        </div>
    );

    return (
        <div>
            <Card style={classes.root}>
                <CardActionArea>
                    <CardMedia
                        style={classes.media}
                        image={props.picture}
                        title="Profile Picture"
                    />
                    <CardContent style={classes.title}>
                        <Typography gutterBottom variant="h5" component="h2">
                            {props.name}
                        </Typography>
                        {props.isOwner ? <Buttons/> : null}
                    </CardContent>
                </CardActionArea>
            </Card>
            <Gallery/>
            <EditName/>
        </div>
    )
}

import * as React from 'react';
import axios from "axios";
import {useEffect, useState} from "react";

const MatchComponent = (matchId) => {
    const [open, setOpen] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    useEffect(() => {
        handleLoadPlayers();
    }, []);
    const [playerHome, setPlayerHome] = useState({
        id: 0,
        firstName: '',
        lastName: ''
    });
    const [playerAway, setPlayerAway] = useState({
        id: 0,
        firstName: '',
        lastName: ''
    })
    const handleLoadPlayers = async () => {
        const response = (await axios.get(`/api/matches/loadMatch/${matchId.matchId}`)).data;
        console.log(response);
        return (<div>
        </div>
        );
    }
};

export default MatchComponent
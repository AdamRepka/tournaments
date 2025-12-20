import React, { useEffect, useState } from 'react';

const GroupComponent = ({ groupId }) => {
    const [selectedGroup, setSelectedGroup] = useState({
        id: null,
        name: '',
        players: [],
        matches: [],
    });

    useEffect(() => {
        fetch(`/api/groups/${groupId}`)
            .then((response) => response.json())
            .then((data) => {
                setSelectedGroup({
                    id: data.id,
                    name: data.name,
                    players: data.players,
                    matches: data.matches,
                });
            })
            .catch((error) => console.error('Chyba při načítání skupiny:', error));
    }, [groupId]);

    return (
        <div>
            <h2>Skupina: {selectedGroup.name}</h2>
            <ul>
                {selectedGroup.players.map((player) => (
                    <li key={player.id}>{player.firstName} {player.lastName}</li>
                ))}
            </ul>
        </div>
    );
};

export default GroupComponent;

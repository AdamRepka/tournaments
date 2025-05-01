import React, { useState, useEffect } from 'react';

const PlayerListComponent = () => {
    const [players, setPlayers] = useState([]);

    useEffect(() => {
        // Zde bude volání na backend pro načtení hráčů (API)
        fetch('/api/players')
            .then((response) => response.json())
            .then((data) => setPlayers(data))
            .catch((error) => console.error('Chyba při načítání hráčů:', error));
    }, []);

    return (
        <div className="p-4">
            <h2 className="text-xl font-bold">Seznam hráčů</h2>
            <ul className="mt-4">
                {players.map((player) => (
                    <li key={player.id} className="border p-2 my-2 rounded">
                        <span className="font-medium">{player.firstName} {player.lastName}</span> - {player.category}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default PlayerListComponent;

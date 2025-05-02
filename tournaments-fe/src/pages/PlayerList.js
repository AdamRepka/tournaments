import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { Edit3, Trash2 } from 'lucide-react'; // přidáme i Trash2 pro ikonu koše

const PlayerList = () => {
    const [players, setPlayers] = useState([]);

    useEffect(() => {
        fetchPlayers();
    }, []);

    const fetchPlayers = async () => {
        try {
            const response = await axios.get('/api/players');
            setPlayers(response.data);
        } catch (error) {
            console.error('Chyba při načítání hráčů:', error);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Jste si jistí, že chcete odstranit tohoto hráče?')) {
            try {
                await axios.delete(`/api/players/${id}`);
                fetchPlayers(); // znovu načteme hráče
            } catch (error) {
                console.error('Chyba při odstraňování hráče:', error);
            }
        }
    };

    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold mb-4">Seznam hráčů</h1>
            <table className="min-w-full bg-white shadow rounded">
                <thead>
                <tr className="bg-blue-500 text-white">
                    <th className="py-2 px-4">ID</th>
                    <th className="py-2 px-4">Jméno</th>
                    <th className="py-2 px-4">Kategorie</th>
                    <th className="py-2 px-4">Akce</th>
                </tr>
                </thead>
                <tbody>
                {players.map((player) => (
                    <tr key={player.id} className="border-b hover:bg-gray-100">
                        <td className="py-2 px-4">{player.id}</td>
                        <td className="py-2 px-4">{player.firstName} {player.lastName}</td>
                        <td className="py-2 px-4">{player.category}</td>
                        <td className="py-2 px-4 flex space-x-2">
                            <Link
                                to={`/players/edit/${player.id}`}
                                className="text-blue-500 hover:text-blue-700"
                                title="Upravit hráče"
                            >
                                <Edit3 className="w-5 h-5 inline" />
                            </Link>
                            <button
                                onClick={() => handleDelete(player.id)}
                                className="text-red-500 hover:text-red-700"
                                title="Odstranit hráče"
                            >
                                <Trash2 className="w-5 h-5 inline" />
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default PlayerList;

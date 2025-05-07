import React, { useEffect, useState } from 'react';
import axios from 'axios';

const GroupTables = () => {
    const [groups, setGroups] = useState([]);
    const [loading, setLoading] = useState(true);
    const [deletingGroupId, setDeletingGroupId] = useState(null);
    const [toast, setToast] = useState(null);

    const fetchGroups = async () => {
        try {
            const response = await axios.get('/api/groups');
            setGroups(response.data);
        } catch (error) {
            console.error('Chyba při načítání skupin:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchGroups();
    }, []);

    const handleDeleteGroup = async (groupId) => {
        if (!window.confirm('Opravdu chcete smazat tuto skupinu?')) return;

        setDeletingGroupId(groupId);
        try {
            await axios.delete(`/api/groups/${groupId}`);
            setGroups((prevGroups) => prevGroups.filter((g) => g.id !== groupId));
            showToast('✅ Skupina byla úspěšně smazána.');
        } catch (error) {
            console.error('Chyba při mazání skupiny:', error);
            showToast('❌ Nepodařilo se smazat skupinu.');
        } finally {
            setDeletingGroupId(null);
        }
    };

    const showToast = (message) => {
        setToast(message);
        setTimeout(() => setToast(null), 3000); // toast zmizí po 3 sekundách
    };

    if (loading) return <p className="text-center mt-4">⏳ Načítám skupiny...</p>;

    if (groups.length === 0) return <p className="text-center mt-4">⚠️ Žádné skupiny nebyly nalezeny.</p>;

    return (
        <div className="space-y-8 p-6 max-w-screen-xl mx-auto relative">
            {toast && (
                <div className="fixed top-4 right-4 bg-gray-800 text-white px-4 py-2 rounded shadow-lg z-50">
                    {toast}
                </div>
            )}

            {groups.map((group) => (
                <div key={group.id} className="bg-white p-4 rounded-xl shadow-lg overflow-x-auto relative">
                    <div className="flex justify-between items-center mb-4">
                        <h2 className="text-xl font-bold text-blue-700">{group.name}</h2>
                        <button
                            onClick={() => handleDeleteGroup(group.id)}
                            className={`text-red-600 hover:text-red-800 transition flex items-center`}
                            title="Smazat skupinu"
                            disabled={deletingGroupId === group.id}
                        >
                            {deletingGroupId === group.id ? (
                                <span className="animate-spin mr-1">⏳</span>
                            ) : (
                                '🗑️'
                            )}
                            {deletingGroupId === group.id && 'Mazání...'}
                        </button>
                    </div>
                    <table className="min-w-full border text-sm">
                        <thead>
                        <tr>
                            <th className="border p-2 bg-gray-100">Hráč</th>
                            {group.players.map((player) => (
                                <th key={player.id} className="border p-2 bg-gray-100">
                                    {player.firstName} {player.lastName}
                                </th>
                            ))}
                            <th className="border p-2 bg-gray-100">Body</th>
                            <th className="border p-2 bg-gray-100">Sety</th>
                            <th className="border p-2 bg-gray-100">Míčky</th>
                            <th className="border p-2 bg-gray-100">Pořadí</th>
                        </tr>
                        </thead>
                        <tbody>
                        {group.players.map((rowPlayer) => (
                            <tr key={rowPlayer.id}>
                                <td className="border p-2 bg-gray-100 font-medium">
                                    {rowPlayer.firstName} {rowPlayer.lastName}
                                </td>
                                {group.players.map((colPlayer) => (
                                    <td
                                        key={colPlayer.id}
                                        className={`border p-2 text-center ${
                                            rowPlayer.id === colPlayer.id
                                                ? 'bg-black text-white font-bold'
                                                : 'bg-white'
                                        }`}
                                    >
                                        {rowPlayer.id === colPlayer.id ? 'X' : (
                                            <input
                                                type="text"
                                                placeholder=""
                                                className="w-16 text-center border rounded p-1"
                                            />
                                        )}
                                    </td>
                                ))}
                                <td className="border p-2 text-center">0</td>
                                <td className="border p-2 text-center">0</td>
                                <td className="border p-2 text-center">0</td>
                                <td className="border p-2 text-center">-</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            ))}
        </div>
    );
};

export default GroupTables;

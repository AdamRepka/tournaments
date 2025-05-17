import React, { useEffect, useState } from 'react';
import axios from 'axios';

// Modal komponenta
const MatchModal = ({ isOpen, onClose, onSave, player1, player2, initialSets }) => {
    const [sets, setSets] = useState(initialSets || [{ player1Points: '', player2Points: '' }, { player1Points: '', player2Points: '' }]);

    useEffect(() => {
        setSets(initialSets || [{ player1Points: '', player2Points: '' }, { player1Points: '', player2Points: '' }]);
    }, [initialSets]);

    const handleSetChange = (index, field, value) => {
        const updatedSets = [...sets];
        updatedSets[index][field] = value;
        setSets(updatedSets);
    };

    const handleSave = () => {
        onSave(sets);
        onClose();
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50 animate-fadeIn">
            <div className="bg-white rounded-2xl shadow-2xl p-8 w-96 max-w-full transform animate-slideUp">
                <h2 className="flex items-center justify-center gap-4 mb-6 text-lg font-semibold text-gray-800 border-b pb-4">
                    <span className="flex items-center gap-2 px-3 py-1 bg-blue-100 text-blue-700 rounded-full whitespace-nowrap overflow-hidden text-ellipsis">
                        {player1?.firstName} {player1?.lastName}
                    </span>
                    <span className="text-gray-500 text-xl">⚔️</span>
                    <span className="flex items-center gap-2 px-3 py-1 bg-green-100 text-green-700 rounded-full whitespace-nowrap overflow-hidden text-ellipsis">
                        {player2?.firstName} {player2?.lastName}
                    </span>
                </h2>

                <div className="space-y-4">
                    {sets.map((set, index) => (
                        <div key={index} className="flex items-center justify-between border rounded-xl px-4 py-2 shadow-sm">
                            <span className="text-sm font-medium text-gray-600">Set {index + 1}</span>
                            <div className="flex items-center gap-2">
                                <input
                                    type="number"
                                    className="w-14 border rounded-lg text-center focus:ring-2 focus:ring-blue-300 focus:outline-none"
                                    value={set.player1Points}
                                    onChange={(e) => handleSetChange(index, 'player1Points', e.target.value)}
                                    placeholder="P1"
                                />
                                <span className="text-gray-500">:</span>
                                <input
                                    type="number"
                                    className="w-14 border rounded-lg text-center focus:ring-2 focus:ring-blue-300 focus:outline-none"
                                    value={set.player2Points}
                                    onChange={(e) => handleSetChange(index, 'player2Points', e.target.value)}
                                    placeholder="P2"
                                />
                            </div>
                        </div>
                    ))}
                </div>

                <div className="flex justify-end gap-3 mt-6">
                    <button
                        onClick={onClose}
                        className="px-4 py-2 rounded-lg bg-gray-200 text-gray-700 hover:bg-gray-300 transition shadow-sm"
                    >
                        Zrušit
                    </button>
                    <button
                        onClick={handleSave}
                        className="px-4 py-2 rounded-lg bg-blue-600 text-white hover:bg-blue-700 transition shadow-md"
                    >
                        Uložit
                    </button>
                </div>
            </div>

            <style jsx>{`
                .animate-fadeIn {
                    animation: fadeIn 0.2s ease-out;
                }
                .animate-slideUp {
                    animation: slideUp 0.3s ease-out;
                }
                @keyframes fadeIn {
                    from { opacity: 0; }
                    to { opacity: 1; }
                }
                @keyframes slideUp {
                    from { transform: translateY(20px); opacity: 0; }
                    to { transform: translateY(0); opacity: 1; }
                }
            `}</style>
        </div>
    );
};

const renderMatchResult = (matches, p1Id, p2Id) => {
    if (!matches || matches.length === 0) return '-';

    const match = matches.find(m =>
        m?.player1Id !== undefined &&
        m?.player2Id !== undefined &&
        (m.player1Id === p1Id && m.player2Id === p2Id)
    );

    if (!match) return '-';

    const result = `${match.player1SetsWon}:${match.player2SetsWon}`;

    const sets = match.matchSets.map((set, idx) => {
        const winner = set.player1Points > set.player2Points ? 'player1'
            : set.player1Points < set.player2Points ? 'player2' : 'none';
        return (
            <span key={idx} className={`mx-1 ${winner === 'player1' ? 'text-green-600 font-bold' 
                : winner === 'player2' ? 'text-red-700' : 'text-gray-700'}`}>
                {set.player1Points}:{set.player2Points}
            </span>
        );
    });

    return (
        <div className="flex flex-col items-center">
            <div className={`font-semibold ${match.player1SetsWon > match.player2SetsWon ? 'text-green-700' :
                match.player1SetsWon < match.player2SetsWon ? 'text-red-700': 'text-gray-700'}`}>
                {result}
            </div>
            <div className="flex text-xs">{sets}</div>
        </div>
    );
};

const GroupTables = () => {
    const [groups, setGroups] = useState([]);
    const [loading, setLoading] = useState(true);
    const [deletingGroupId, setDeletingGroupId] = useState(null);
    const [toast, setToast] = useState(null);
    const [selectedMatch, setSelectedMatch] = useState(null);

    const fetchGroups = async () => {
        try {
            const response = await axios.get('/api/groups');
            const groups = response.data.map(group => ({
                ...group,
                matches: group.matches || []
            }));
            setGroups(groups);
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
            setGroups(prevGroups => prevGroups.filter(g => g.id !== groupId));
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
        setTimeout(() => setToast(null), 3000);
    };

    const openMatchModal = (player1, player2, key) => {
        const existingMatch = groups.flatMap(g => g.matches).find(m =>
            (m.player1Id === player1.id && m.player2Id === player2.id) ||
            (m.player1Id === player2.id && m.player2Id === player1.id)
        );

        const initialSets = existingMatch?.matchSets?.map(set => ({
            id: set.id,
            player1Points: set.player1Points,
            player2Points: set.player2Points
        })) || [{ player1Points: '', player2Points: '' }, { player1Points: '', player2Points: '' }];

        setSelectedMatch({
            player1,
            player2,
            key,
            initialSets,
            existingMatchId: existingMatch?.id ?? null
        });
    };

    const saveMatchResult = async (key, sets) => {
        const [player1Id, player2Id] = key.split('-').map(id => parseInt(id));

        const group = groups.find(g =>
            g.players.some(p => p.id === player1Id) &&
            g.players.some(p => p.id === player2Id)
        );

        if (!group) {
            showToast('❌ Skupina pro tento zápas nebyla nalezena.');
            return;
        }

        const payload = {
            id: selectedMatch?.existingMatchId ?? null,
            player1Id,
            player2Id,
            tournamentGroupId: group.id,
            matchSets: sets.map(set => ({
                id: set.id ?? null,
                player1Points: parseInt(set.player1Points),
                player2Points: parseInt(set.player2Points)
            }))
        };

        try {
            await axios.post('/api/matches/result', payload);
            await fetchGroups();
            showToast('✅ Zápas byl úspěšně uložen.');
        } catch (err) {
            console.error('Chyba při ukládání zápasu:', err);
            showToast('❌ Nepodařilo se uložit zápas.');
        }
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

            {groups.map(group => (
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
                            {group.players.map(player => (
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
                        {group.players.map(rowPlayer => (
                            <tr key={rowPlayer.id}>
                                <td className="border p-2 bg-gray-100 font-medium">
                                    {rowPlayer.firstName} {rowPlayer.lastName}
                                </td>
                                {group.players.map(colPlayer => {
                                    const key = `${rowPlayer.id}-${colPlayer.id}`;
                                    const match = group.matches.find(m =>
                                        m.player1Id === rowPlayer.id && m.player2Id === colPlayer.id
                                    );
                                    const matchPlayed = match && (match.player1SetsWon > 0 || match.player2SetsWon > 0);

                                    return (
                                        <td
                                            key={colPlayer.id}
                                            onClick={() => rowPlayer.id !== colPlayer.id && openMatchModal(rowPlayer, colPlayer, key)}
                                            className={`border p-2 text-center cursor-pointer ${
                                                rowPlayer.id === colPlayer.id
                                                    ? 'bg-black text-white font-bold'
                                                    : `${matchPlayed ? 'bg-blue-100' : ''} hover:bg-blue-50`
                                            }`}
                                        >
                                            {rowPlayer.id === colPlayer.id
                                                ? 'X'
                                                : renderMatchResult(group.matches, rowPlayer.id, colPlayer.id)}
                                        </td>
                                    );
                                })}

                                <td className="border p-2 text-center">{rowPlayer.points}</td>
                                <td className="border p-2 text-center">{rowPlayer.sets}</td>
                                <td className="border p-2 text-center">{rowPlayer.balls}</td>
                                <td className="border p-2 text-center">{rowPlayer.groupPosition}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            ))}

            <MatchModal
                isOpen={selectedMatch !== null}
                onClose={() => setSelectedMatch(null)}
                onSave={(sets) => {
                    if (selectedMatch) {
                        saveMatchResult(selectedMatch.key, sets);
                    }
                }}
                player1={selectedMatch?.player1}
                player2={selectedMatch?.player2}
                initialSets={selectedMatch?.initialSets}
            />
        </div>
    );
};

export default GroupTables;

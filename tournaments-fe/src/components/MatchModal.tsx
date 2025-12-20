import  { useState } from 'react';
import { TournamentMatchDto, MatchSetDto, PlayerDto } from '../dto/types';
import { Dialog } from '@headlessui/react';
import { saveMatch } from '../api/matchApi.ts';
import * as React from "react";

type Props = {
    match: TournamentMatchDto;
    players: [PlayerDto, PlayerDto];
    onClose: () => void;
    onSave: (match: TournamentMatchDto) => void;
};

const MatchModal: React.FC<Props> = ({ match, players, onClose, onSave }) => {
    const [sets, setSets] = useState<MatchSetDto[]>(match.matchSets || []);
    const [error, setError] = useState<string | null>(null);

    const handleSave = async () => {
        const homeSets = sets.filter(s => s.player1Points > s.player2Points).length;
        const awaySets = sets.filter(s => s.player2Points > s.player1Points).length;

        // 🔍 Tady doplň validaci
        const isValid = true; // 👉 Zde vlož vlastní validační logiku

        if (!isValid) {
            setError("Zápas má skóre v nesprávném formátu a nebyl uložen.");
            return;
        }

        const updatedMatch: TournamentMatchDto = {
            ...match,
            matchSets: sets,
            playerHomeSetsWon: homeSets,
            playerAwaySetsWon: awaySets,
        };

        try {
            const saved = await saveMatch(updatedMatch);
            onSave(saved);
        } catch (error) {
            console.error('Chyba při ukládání zápasu:', error);
            setError("Nepodařilo se uložit zápas. Zkuste to prosím znovu.");
        }
    };

    const handleChange = (index: number, field: 'player1Points' | 'player2Points', value: number) => {
        const updatedSets = [...sets];
        updatedSets[index] = {
            ...updatedSets[index],
            [field]: value,
        };
        setSets(updatedSets);
    };

    const addSet = () => setSets([...sets, { id: null, player1Points: 0, player2Points: 0 }]);

    return (
        <Dialog open={true} onClose={onClose} className="fixed inset-0 z-50 flex items-center justify-center bg-black/40">
            <div className="bg-white p-6 rounded-xl shadow-xl w-full max-w-lg">
                <h2 className="text-lg font-bold mb-4">
                    Zápas: {players[0].lastName} vs. {players[1].lastName}
                </h2>

                {sets.map((set, index) => (
                    <div key={index} className="flex items-center space-x-4 mb-2">
                        <label>Set {index + 1}:</label>
                        <input
                            type="number"
                            className="border rounded px-2 py-1 w-16"
                            value={set.player1Points}
                            onChange={(e) => handleChange(index, 'player1Points', +e.target.value)}
                        />
                        <span>:</span>
                        <input
                            type="number"
                            className="border rounded px-2 py-1 w-16"
                            value={set.player2Points}
                            onChange={(e) => handleChange(index, 'player2Points', +e.target.value)}
                        />
                    </div>
                ))}

                <button onClick={addSet} className="text-blue-600 underline mb-4">Přidat set</button>

                {error && <div className="text-red-600 font-semibold mb-4">{error}</div>}

                <div className="flex justify-end space-x-2">
                    <button onClick={onClose} className="bg-gray-200 px-4 py-2 rounded">Zrušit</button>
                    <button onClick={handleSave} className="bg-green-600 text-white px-4 py-2 rounded">Uložit</button>
                </div>
            </div>
        </Dialog>
    );
};

export default MatchModal;

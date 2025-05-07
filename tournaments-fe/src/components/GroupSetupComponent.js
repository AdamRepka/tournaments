import React, { useState } from 'react';
import axios from 'axios';
import { DragDropContext, Droppable, Draggable } from '@hello-pangea/dnd';

const GroupSetupComponent = () => {
    const [category, setCategory] = useState('SENIOR');
    const [groupCount, setGroupCount] = useState(1);
    const [playersPerGroup, setPlayersPerGroup] = useState(1);
    const [generatedGroups, setGeneratedGroups] = useState([]);
    const [saving, setSaving] = useState(false);
    const [saveMessage, setSaveMessage] = useState('');

    const handleGenerateGroups = async () => {
        try {
            const response = await axios.post('/api/groups/generate', {
                category,
                groupCount,
                playersPerGroup
            });
            setGeneratedGroups(response.data);
        } catch (error) {
            console.error('Chyba při generování skupin:', error.response ? error.response.data : error.message);
        }
    };

    const handleDragEnd = (result) => {
        const { source, destination } = result;

        if (!destination) return;

        const sourceGroupIndex = parseInt(source.droppableId.split('-')[1]);
        const destGroupIndex = parseInt(destination.droppableId.split('-')[1]);

        const updatedGroups = [...generatedGroups];

        const [movedPlayer] = updatedGroups[sourceGroupIndex].players.splice(source.index, 1);
        updatedGroups[destGroupIndex].players.splice(destination.index, 0, movedPlayer);

        setGeneratedGroups(updatedGroups);
    };

    const handleSaveGroups = async () => {
        setSaving(true);
        setSaveMessage('');
        try {
            const payload = generatedGroups.map((group) => ({
                id: group.id,  // správně, backend čeká 'id', ne 'groupId'
                name: group.name,
                players: group.players.map((player) => ({
                    id: player.id,
                    firstName: player.firstName,
                    lastName: player.lastName,
                    category: player.category,
                    skillLevel: player.skillLevel,
                    code: player.code,
                    entryFeePaid: player.entryFeePaid
                })),
                matches: group.matches || []  // pokud posíláš prázdně, nevadí
            }));

            await axios.post('/api/groups/save', payload);

            setSaveMessage('✅ Rozdělení úspěšně uloženo.');
        } catch (error) {
            console.error('Chyba při ukládání rozdělení:', error.response ? error.response.data : error.message);
            setSaveMessage('❌ Nepodařilo se uložit rozdělení.');
        } finally {
            setSaving(false);
            setTimeout(() => setSaveMessage(''), 5000);
        }
    };

    return (
        <div className="flex gap-6 p-6 max-w-screen-xl mx-auto">
            {/* Formulář vlevo */}
            <div className="w-1/4 bg-white rounded-2xl shadow-lg p-6">
                <h2 className="text-2xl font-bold mb-6 text-blue-700">Generování skupin</h2>

                <div className="mb-4">
                    <label className="block mb-2 font-medium">Kategorie</label>
                    <select
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                        className="border w-full p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                    >
                        <option value="SENIOR">Muži</option>
                        <option value="WOMEN">Ženy</option>
                        <option value="JUNIOR">Děti</option>
                    </select>
                </div>

                <div className="mb-4">
                    <label className="block mb-2 font-medium">Počet skupin</label>
                    <input
                        type="number"
                        value={groupCount}
                        onChange={(e) => setGroupCount(e.target.value)}
                        className="border w-full p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                        min={1}
                    />
                </div>

                <div className="mb-6">
                    <label className="block mb-2 font-medium">Hráčů ve skupině</label>
                    <input
                        type="number"
                        value={playersPerGroup}
                        onChange={(e) => setPlayersPerGroup(e.target.value)}
                        className="border w-full p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                        min={1}
                    />
                </div>

                <button
                    onClick={handleGenerateGroups}
                    className="bg-blue-600 hover:bg-blue-700 text-white w-full py-2 rounded transition"
                >
                    Generovat skupiny
                </button>

                {generatedGroups.length > 0 && (
                    <div className="mt-6">
                        <button
                            onClick={handleSaveGroups}
                            disabled={saving}
                            className={`w-full py-2 rounded transition ${
                                saving ? 'bg-gray-400' : 'bg-green-600 hover:bg-green-700 text-white'
                            }`}
                        >
                            {saving ? 'Ukládám...' : '💾 Uložit rozdělení'}
                        </button>
                        {saveMessage && <p className="mt-2 text-center">{saveMessage}</p>}
                    </div>
                )}
            </div>

            {/* Panel se skupinami vpravo */}
            <div className="w-3/4 bg-white rounded-2xl shadow-lg p-6 overflow-y-auto max-h-[80vh]">
                <h3 className="text-2xl font-bold mb-6 text-green-700">Vygenerované skupiny</h3>

                {generatedGroups.length === 0 ? (
                    <p className="text-gray-500 italic">Žádné skupiny zatím nebyly vygenerovány.</p>
                ) : (
                    <DragDropContext onDragEnd={handleDragEnd}>
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                            {generatedGroups.map((group, groupIndex) => (
                                <Droppable key={group.id} droppableId={`group-${groupIndex}`}>
                                    {(provided) => (
                                        <div
                                            ref={provided.innerRef}
                                            {...provided.droppableProps}
                                            className="p-4 border rounded-lg shadow-sm bg-gray-50"
                                        >
                                            <h4 className="font-bold mb-2 text-lg text-blue-800">
                                                Skupina {groupIndex + 1}
                                            </h4>
                                            <ul className="space-y-2">
                                                {group.players.map((player, playerIndex) => (
                                                    <Draggable
                                                        key={player.id}
                                                        draggableId={player.id.toString()}
                                                        index={playerIndex}
                                                    >
                                                        {(provided, snapshot) => (
                                                            <li
                                                                ref={provided.innerRef}
                                                                {...provided.draggableProps}
                                                                {...provided.dragHandleProps}
                                                                className={`p-2 rounded bg-white shadow border ${
                                                                    snapshot.isDragging
                                                                        ? 'bg-yellow-100'
                                                                        : ''
                                                                }`}
                                                            >
                                                                {player.firstName} {player.lastName}
                                                            </li>
                                                        )}
                                                    </Draggable>
                                                ))}
                                                {provided.placeholder}
                                            </ul>
                                        </div>
                                    )}
                                </Droppable>
                            ))}
                        </div>
                    </DragDropContext>
                )}
            </div>
        </div>
    );
};

export default GroupSetupComponent;

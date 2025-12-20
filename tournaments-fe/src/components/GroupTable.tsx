import { useState } from 'react';
import { TournamentGroupDto, TournamentMatchDto, PlayerDto } from '../dto/types';
import MatchModal from './MatchModal.tsx';
import * as React from 'react';

type ViewMode = 'ADMIN' | 'DISPLAY';

type MatchKey = {
    a: number;
    b: number;
};

type Props = {
    group: TournamentGroupDto;
    onReload: () => void;
    mode: ViewMode;
};

const HIGHLIGHT_DURATION_MS = 2000;

const GroupTable: React.FC<Props> = ({ group, onReload, mode }) => {
    const [selectedMatch, setSelectedMatch] = useState<TournamentMatchDto | null>(null);
    const [modalPlayers, setModalPlayers] = useState<[PlayerDto, PlayerDto] | null>(null);

    const [hoveredMatch, setHoveredMatch] = useState<MatchKey | null>(null);
    const [lastUpdatedMatch, setLastUpdatedMatch] = useState<MatchKey | null>(null);

    const getMatch = (a: number, b: number) =>
        group.matches.find(
            m =>
                (m.playerHomeId === a && m.playerAwayId === b) ||
                (m.playerHomeId === b && m.playerAwayId === a)
        );

    const isSameMatch = (
        a1: number,
        b1: number,
        a2: number,
        b2: number
    ) =>
        (a1 === a2 && b1 === b2) ||
        (a1 === b2 && b1 === a2);

    const handleCellClick = (p1: PlayerDto, p2: PlayerDto) => {
        if (mode === 'DISPLAY') return;

        const match = getMatch(p1.id, p2.id);

        setSelectedMatch(
            match ?? {
                id: null,
                playerHomeId: p1.id,
                playerAwayId: p2.id,
                matchSets: [],
                tournamentGroupId: group.id,
                playerHomeSetsWon: 0,
                playerAwaySetsWon: 0,
            }
        );

        setModalPlayers([p1, p2]);
    };

    const markLastUpdatedMatch = (a: number, b: number) => {
        setLastUpdatedMatch({ a, b });
        setTimeout(() => setLastUpdatedMatch(null), HIGHLIGHT_DURATION_MS);
    };

    const getCellPresentation = (rowId: number, colId: number) => {
        const match = getMatch(rowId, colId);

        const isUnplayed =
            !match ||
            match.matchSets.length === 0 ||
            (match.playerHomeSetsWon === 0 && match.playerAwaySetsWon === 0);

        if (isUnplayed) {
            return (
                <span className="text-gray-400 italic">
                    neodehráno
                </span>
            );
        }

        const isMirror = match.playerHomeId !== rowId;

        const p1Points = match.matchSets.reduce((a, s) => a + s.player1Points, 0);
        const p2Points = match.matchSets.reduce((a, s) => a + s.player2Points, 0);

        const win =
            match.playerHomeSetsWon > match.playerAwaySetsWon
                ? 'home'
                : match.playerAwaySetsWon > match.playerHomeSetsWon
                    ? 'away'
                    : p1Points > p2Points
                        ? 'home'
                        : p2Points > p1Points
                            ? 'away'
                            : 'draw';

        let cls = 'text-gray-700';

        if (win === 'draw') {
            cls = 'text-gray-700 font-bold bg-blue-100';
        } else if (
            (win === 'home' && !isMirror) ||
            (win === 'away' && isMirror)
        ) {
            cls = 'text-green-700 font-bold bg-green-200';
        } else {
            cls = 'text-red-700 font-bold bg-red-200';
        }

        if (isMirror) {
            return (
                <div className={cls}>
                    <div>{match.playerAwaySetsWon}:{match.playerHomeSetsWon}</div>
                    <div className="text-xs">{p2Points}:{p1Points}</div>
                </div>
            );
        } else {
            return (
                <div className={cls}>
                    <div>{match.playerHomeSetsWon}:{match.playerAwaySetsWon}</div>
                    <div className="text-xs">{p1Points}:{p2Points}</div>
                </div>
            );
        }
    };

    return (
        <div className="overflow-auto rounded-xl shadow-xl bg-white">
            <table
                className={`table-auto w-full text-center ${
                    mode === 'DISPLAY' ? 'text-lg' : 'text-sm'
                }`}
            >
                <thead className="bg-gray-100">
                <tr>
                    <th className="p-2"></th>
                    {group.players.map(p => (
                        <th key={p.id} className="p-2">{p.lastName}</th>
                    ))}
                    <th>Body</th>
                    <th>Sety</th>
                    <th>Míčky</th>
                    <th>Pořadí</th>
                </tr>
                </thead>

                <tbody>
                {group.players.map(row => (
                    <tr key={row.id} className="border-t">
                        <th className="p-2 text-left bg-gray-50">{row.lastName}</th>

                        {group.players.map(col => {
                            if (row.id === col.id) {
                                return <td key={col.id} className="bg-gray-700" />;
                            }

                            const isHover =
                                hoveredMatch &&
                                isSameMatch(hoveredMatch.a, hoveredMatch.b, row.id, col.id);

                            const isLastUpdated =
                                lastUpdatedMatch &&
                                isSameMatch(lastUpdatedMatch.a, lastUpdatedMatch.b, row.id, col.id);

                            return (
                                <td
                                    key={col.id}
                                    onClick={() => handleCellClick(row, col)}
                                    onMouseEnter={() =>
                                        mode === 'ADMIN' &&
                                        setHoveredMatch({ a: row.id, b: col.id })
                                    }
                                    onMouseLeave={() => setHoveredMatch(null)}
                                    className={`
                                        p-2 transition
                                        ${mode === 'ADMIN'
                                        ? 'cursor-pointer hover:bg-blue-100'
                                        : 'pointer-events-none'}
                                        ${isHover ? 'ring-2 ring-blue-400 animate-pulse' : ''}
                                        ${isLastUpdated ? 'ring-4 ring-yellow-400 animate-pulse' : ''}
                                    `}
                                >
                                    {getCellPresentation(row.id, col.id)}
                                </td>
                            );
                        })}

                        <td>{row.points}</td>
                        <td>{row.sets}</td>
                        <td>{row.balls}</td>
                        <td>{row.groupPosition}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            {mode === 'ADMIN' && selectedMatch && modalPlayers && (
                <MatchModal
                    match={selectedMatch}
                    players={modalPlayers}
                    onClose={() => setSelectedMatch(null)}
                    onSave={async () => {
                        const [a, b] = modalPlayers.map(p => p.id);
                        setSelectedMatch(null);
                        await onReload();
                        markLastUpdatedMatch(a, b);
                    }}
                />
            )}
        </div>
    );
};

export default GroupTable;

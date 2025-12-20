import { TournamentGroupDto } from '../dto/types';
import * as React from 'react';

type Props = {
    group: TournamentGroupDto;
};

const MIN_NAME_COL = 220;
const MAX_NAME_COL = 420;
const CHAR_WIDTH = 14; // px – pro text-2xl

// velmi jemný „gold subtle“ breathing glow – pouze text
const LEADER_TEXT_GLOW =
    'text-yellow-200 font-extrabold animate-[textGlow_4s_ease-in-out_infinite]';

const GroupTableTv: React.FC<Props> = ({ group }) => {
    const longestNameLength = Math.max(
        ...group.players.map(p => p.lastName.length),
        10
    );

    const nameColWidth = Math.min(
        MAX_NAME_COL,
        Math.max(MIN_NAME_COL, longestNameLength * CHAR_WIDTH)
    );

    const getMatch = (a: number, b: number) =>
        group.matches.find(
            m =>
                (m.playerHomeId === a && m.playerAwayId === b) ||
                (m.playerHomeId === b && m.playerAwayId === a)
        );

    const renderCell = (rowId: number, colId: number) => {
        if (rowId === colId) {
            return (
                <td className="bg-gray-800">
                    <div className="text-xl opacity-30">—</div>
                </td>
            );
        }

        const match = getMatch(rowId, colId);

        if (
            !match ||
            match.matchSets.length === 0 ||
            (match.playerHomeSetsWon === 0 && match.playerAwaySetsWon === 0)
        ) {
            return (
                <td className="bg-gray-700/60 border border-gray-600 text-gray-300">
                    <div className="text-2xl font-semibold">—</div>
                    <div className="text-sm uppercase tracking-wide">
                        neodehráno
                    </div>
                </td>
            );
        }

        const isMirror = match.playerHomeId !== rowId;

        const p1 = match.matchSets.reduce((a, s) => a + s.player1Points, 0);
        const p2 = match.matchSets.reduce((a, s) => a + s.player2Points, 0);

        const win =
            match.playerHomeSetsWon > match.playerAwaySetsWon
                ? 'home'
                : match.playerAwaySetsWon > match.playerHomeSetsWon
                    ? 'away'
                    : p1 > p2
                        ? 'home'
                        : p2 > p1
                            ? 'away'
                            : 'draw';

        const isWin =
            win !== 'draw' &&
            ((win === 'home' && !isMirror) ||
                (win === 'away' && isMirror));

        const isDraw = win === 'draw';

        let cls =
            'bg-gradient-to-br from-gray-600 to-gray-700 text-gray-200 border border-gray-500';
        let glow = '';
        let icon = '≈';

        if (isDraw) {
            cls =
                'bg-gradient-to-br from-blue-500/80 to-blue-700/80 text-white border border-blue-400';
            glow = 'shadow-[0_0_14px_rgba(59,130,246,0.35)]';
        } else if (isWin) {
            cls =
                'bg-gradient-to-br from-green-500/80 to-green-700/80 text-white border border-green-400';
            glow = 'shadow-[0_0_14px_rgba(34,197,94,0.35)]';
            icon = '✔';
        } else {
            cls =
                'bg-gradient-to-br from-red-500/80 to-red-700/80 text-white border border-red-400';
            glow = 'shadow-[0_0_14px_rgba(239,68,68,0.35)]';
            icon = '✖';
        }

        const setsA = isMirror
            ? match.playerAwaySetsWon
            : match.playerHomeSetsWon;
        const setsB = isMirror
            ? match.playerHomeSetsWon
            : match.playerAwaySetsWon;

        const ballsA = isMirror ? p2 : p1;
        const ballsB = isMirror ? p1 : p2;

        return (
            <td className={`${cls} ${glow} transition-all duration-300`}>
                <div className="text-3xl font-extrabold leading-tight">
                    {setsA}:{setsB}
                </div>
                <div className="text-sm opacity-90">
                    {ballsA}:{ballsB}
                </div>
                <div className="text-xl mt-1">{icon}</div>
            </td>
        );
    };

    return (
        <div className="overflow-hidden rounded-2xl shadow-2xl">
            <table className="table-fixed w-full border-collapse text-center">
                <colgroup>
                    <col style={{ width: `${nameColWidth}px` }} />
                    {group.players.map((_, idx) => (
                        <col key={idx} />
                    ))}
                    <col style={{ width: '80px' }} />
                    <col style={{ width: '80px' }} />
                    <col style={{ width: '90px' }} />
                    <col style={{ width: '70px' }} />
                </colgroup>

                <thead>
                <tr className="text-2xl text-gray-300 bg-gray-800">
                    <th />
                    {group.players.map(p => (
                        <th key={p.id} className="p-4">
                            {p.lastName}
                        </th>
                    ))}
                    <th>Body</th>
                    <th>Sety</th>
                    <th>Míčky</th>
                    <th>#</th>
                </tr>
                </thead>

                <tbody>
                {group.players.map(row => {
                    const isLeader = row.groupPosition === 1;

                    return (
                        <tr
                            key={row.id}
                            className={
                                isLeader
                                    ? 'bg-yellow-600/10'
                                    : 'bg-gray-900'
                            }
                        >
                            {/* JMÉNO */}
                            <th className="text-left p-4 text-2xl whitespace-nowrap">
                                    <span className={isLeader ? LEADER_TEXT_GLOW : ''}>
                                        {row.lastName}
                                    </span>
                            </th>

                            {group.players.map(col => (
                                <React.Fragment key={col.id}>
                                    {renderCell(row.id, col.id)}
                                </React.Fragment>
                            ))}

                            {/* BODY */}
                            <td className="text-2xl">
                                    <span className={isLeader ? LEADER_TEXT_GLOW : ''}>
                                        {row.points}
                                    </span>
                            </td>

                            <td className="text-2xl">{row.sets}</td>
                            <td className="text-2xl">{row.balls}</td>

                            {/* POŘADÍ */}
                            <td className="text-2xl font-bold">
                                    <span className={isLeader ? LEADER_TEXT_GLOW : ''}>
                                        {row.groupPosition}
                                    </span>
                            </td>
                        </tr>
                    );
                })}
                </tbody>
            </table>

            {/* KEYFRAMES – GOLD SUBTLE */}
            <style>
                {`
                @keyframes textGlow {
                    0%, 100% {
                        filter:
                            drop-shadow(0 0 3px rgba(250,204,21,0.25))
                            drop-shadow(0 0 6px rgba(250,204,21,0.15));
                    }
                    50% {
                        filter:
                            drop-shadow(0 0 6px rgba(250,204,21,0.45))
                            drop-shadow(0 0 10px rgba(250,204,21,0.25));
                    }
                }
            `}
            </style>
        </div>
    );
};

export default GroupTableTv;

import { useCallback, useEffect, useRef, useState } from 'react';
import axios from 'axios';
import { TournamentGroupDto } from '../dto/types';
import GroupTable from './GroupTable.tsx';
import GroupTableTv from './GroupTableTv.tsx';
import * as React from 'react';

type Props = {
    tournamentId?: number;
};

const ROTATION_INTERVAL_MS = 10_000;
const FADE_DURATION_MS = 800;

const GroupView: React.FC<Props> = ({ tournamentId }) => {
    const params = new URLSearchParams(window.location.search);
    const isTvMode = params.get('mode') === 'tv';

    const containerRef = useRef<HTMLDivElement | null>(null);

    const [groups, setGroups] = useState<TournamentGroupDto[]>([]);
    const [visibleIndex, setVisibleIndex] = useState(0);
    const [isFading, setIsFading] = useState(false);
    const [countdown, setCountdown] = useState(ROTATION_INTERVAL_MS / 1000);

    const fetchGroups = useCallback(async () => {
        const res = await axios.get<TournamentGroupDto[]>('/api/groups');
        setGroups(
            res.data.map(g => ({
                ...g,
                matches: g.matches ?? []
            }))
        );
    }, []);

    useEffect(() => {
        fetchGroups();
    }, [fetchGroups]);

    /* 🖥️ FULLSCREEN – jen TV */
    useEffect(() => {
        if (!isTvMode || !containerRef.current) return;

        if (!document.fullscreenElement) {
            containerRef.current.requestFullscreen?.().catch(() => {});
        }
    }, [isTvMode]);

    /* ⏱️ COUNTDOWN + ROTACE (BEZ BLINKU) */
    useEffect(() => {
        if (!isTvMode || groups.length === 0) return;

        setCountdown(ROTATION_INTERVAL_MS / 1000);

        const countdownTimer = setInterval(() => {
            setCountdown(c => c - 1);
        }, 1000);

        const rotateTimer = setInterval(() => {
            // 1️⃣ začni fade-out
            setIsFading(true);

            // 2️⃣ po doběhnutí animace přepni skupinu
            setTimeout(() => {
                setVisibleIndex(i => (i + 1) % groups.length);
                setIsFading(false);

                // 3️⃣ až TEĎ refresh dat
                fetchGroups();
                setCountdown(ROTATION_INTERVAL_MS / 1000);
            }, FADE_DURATION_MS);
        }, ROTATION_INTERVAL_MS);

        return () => {
            clearInterval(countdownTimer);
            clearInterval(rotateTimer);
        };
    }, [isTvMode, groups.length, fetchGroups]);

    /* =========================
       📺 TV MÓD – STABILNÍ CROSSFade
       ========================= */
    if (isTvMode && groups.length > 0) {
        const group = groups[visibleIndex];

        return (
            <div
                ref={containerRef}
                className="min-h-screen bg-gray-900 text-white p-10 overflow-hidden"
                onClick={() => {
                    if (!document.fullscreenElement) {
                        containerRef.current?.requestFullscreen?.();
                    }
                }}
            >
                {/* HEADER */}
                <div className="flex justify-between items-center mb-8">
                    <h1 className="text-5xl font-extrabold">
                        Skupina {group.name}
                    </h1>
                </div>

                {/* FADE CONTAINER */}
                <div
                    className={`
                        transition-opacity
                        duration-${FADE_DURATION_MS}
                        ${isFading ? 'opacity-0' : 'opacity-100'}
                    `}
                >
                    <GroupTableTv group={group} />
                </div>
            </div>
        );
    }

    /* =========================
       🛠️ ADMIN MÓD
       ========================= */
    return (
        <div className="p-6 space-y-10">
            {groups.map(g => (
                <div key={g.id}>
                    <h2 className="text-xl font-bold mb-4">
                        Skupina {g.name}
                    </h2>
                    <GroupTable
                        group={g}
                        mode="ADMIN"
                        onReload={fetchGroups}
                    />
                </div>
            ))}
        </div>
    );
};

export default GroupView;

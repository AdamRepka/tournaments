// PlayOffSetupComponent.tsx
import { useMemo, useState } from "react";
import * as React from "react";
import axios, { AxiosError } from "axios";

// =====================
// DTO + Enums
// =====================

/**
 * Odpovídá tomu, co posíláš na backend.
 * Uprav názvy fieldů podle svého backend DTO, pokud se liší.
 */
export type PlayOffSetupDto = {
    startRound: StartRound;
    setsToWin: number; // 3 nebo 5
};

export enum StartRound {
    SIXTEENTH_FINAL = "SIXTEENTH_FINAL",
    EIGHTH_FINAL = "EIGHTH_FINAL",
    QUARTER_FINAL = "QUARTER_FINAL",
    SEMI_FINAL = "SEMI_FINAL",
}

// =====================
// Axios instance
// =====================

const api = axios.create({
    baseURL: "/", // pokud máš api třeba na / (nebo nastav Vite/CRA proxy), nech takhle
    headers: {
        "Content-Type": "application/json",
    },
});

// =====================
// Component
// =====================

const PlayOffSetupComponent: React.FC = () => {
    const [roundType, setRoundType] = useState<StartRound>(StartRound.EIGHTH_FINAL);
    const [setsToWin, setSetsToWin] = useState<number>(3);

    const [isGenerating, setIsGenerating] = useState(false);
    const [isGenerated, setIsGenerated] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [successMsg, setSuccessMsg] = useState<string | null>(null);

    const roundOptions = useMemo(
        () => [
            { value: StartRound.SIXTEENTH_FINAL, label: "Šestnáctifinále" },
            { value: StartRound.EIGHTH_FINAL, label: "Osmifinále" },
            { value: StartRound.QUARTER_FINAL, label: "Čtvrtfinále" },
            { value: StartRound.SEMI_FINAL, label: "Semifinále" },
        ],
        []
    );

    const setsOptions = useMemo(
        () => [
            { value: 3, label: "BO3 (na 3 vítězné sety)" },
            { value: 5, label: "BO5 (na 5 vítězných setů)" },
        ],
        []
    );

    const buildDto = (): PlayOffSetupDto => ({
        startRound: roundType,
        setsToWin,
    });

    const parseAxiosError = (e: unknown): string => {
        if (!axios.isAxiosError(e)) return "Nastala neznámá chyba.";
        const ae = e as AxiosError<any>;
        // zkus vytáhnout zprávu z backendu, když existuje
        const msg =
            ae.response?.data?.message ||
            ae.response?.data?.error ||
            ae.message ||
            "Nastala chyba při komunikaci s backendem.";
        return typeof msg === "string" ? msg : "Nastala chyba při komunikaci s backendem.";
    };

    const handleGenerate = async () => {
        setError(null);
        setSuccessMsg(null);
        setIsGenerating(true);

        try {
            const dto = buildDto();

            await api.post("/api/playoff/setup", dto);

            setIsGenerated(true);
            setSuccessMsg("Play-off bylo úspěšně vygenerováno.");
        } catch (e) {
            setIsGenerated(false);
            setError(parseAxiosError(e));
        } finally {
            setIsGenerating(false);
        }
    };

    const handleRedirectToGame = () => {
        // Redirect na backend endpoint (typicky vrací HTML stránku / nebo přesměrování / nebo JSON)
        // Pokud chceš přesměrovat SPA routu, použij react-router navigate místo window.location.
        window.location.href = "/api/playoff/game";
    };

    return (
        <div className="w-full max-w-2xl mx-auto p-4">
            <div className="bg-white rounded-2xl shadow-sm border border-slate-200">
                <div className="p-6 border-b border-slate-200">
                    <h2 className="text-xl font-semibold text-slate-900">Nastavení Play-off</h2>
                </div>

                <div className="p-6 space-y-6">
                    {/* Alerts */}
                    {error && (
                        <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">
                            {error}
                        </div>
                    )}
                    {successMsg && (
                        <div className="rounded-xl border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm text-emerald-800">
                            {successMsg}
                        </div>
                    )}

                    {/* Round type */}
                    <div className="space-y-2">
                        <label className="block text-sm font-medium text-slate-800">Začít od kola</label>
                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                            {roundOptions.map((opt) => {
                                const selected = opt.value === roundType;
                                return (
                                    <button
                                        key={opt.value}
                                        type="button"
                                        onClick={() => setRoundType(opt.value)}
                                        disabled={isGenerating}
                                        className={[
                                            "text-left rounded-xl border px-4 py-3 transition",
                                            selected
                                                ? "bg-blue-600 hover:bg-blue-700 text-white w-full py-2 rounded transition"
                                                : "border-slate-200 bg-white text-slate-900 hover:bg-slate-50",
                                            isGenerating ? "opacity-60 cursor-not-allowed" : "",
                                        ].join(" ")}
                                    >
                                        <div className="text-sm font-semibold">{opt.label}</div>
                                    </button>
                                );
                            })}
                        </div>
                    </div>

                    {/* Sets */}
                    <div className="space-y-2">
                        <label className="block text-sm font-medium text-slate-800">Formát zápasu</label>
                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                            {setsOptions.map((opt) => {
                                const selected = opt.value === setsToWin;
                                return (
                                    <button
                                        key={opt.value}
                                        type="button"
                                        onClick={() => setSetsToWin(opt.value)}
                                        disabled={isGenerating}
                                        className={[
                                            "text-left rounded-xl border px-4 py-3 transition",
                                            selected
                                                ? "bg-blue-600 hover:bg-blue-700 text-white w-full py-2 rounded transition"
                                                : "border-slate-200 bg-white text-slate-900 hover:bg-slate-50",
                                            isGenerating ? "opacity-60 cursor-not-allowed" : "",
                                        ].join(" ")}
                                    >
                                        <div className="text-sm font-semibold">{opt.label}</div>
                                    </button>
                                );
                            })}
                        </div>
                    </div>

                    {/* Summary */}
                    <div className="rounded-xl border border-slate-200 bg-slate-50 px-4 py-3">
                        <div className="text-sm font-medium text-slate-900">Shrnutí</div>
                        <div className="text-sm text-slate-700 mt-1">
                            Kolo: <span className="font-semibold">{roundOptions.find((r) => r.value === roundType)?.label}</span>
                            {" • "}
                            Formát: <span className="font-semibold">{setsToWin === 3 ? "BO3" : "BO5"}</span>
                        </div>
                    </div>
                </div>

                {/* Actions */}
                <div className="p-6 border-t border-slate-200 flex flex-col sm:flex-row gap-3 sm:items-center sm:justify-between">
                    <button
                        type="button"
                        onClick={handleGenerate}
                        disabled={isGenerating}
                        className={[
                            "inline-flex items-center justify-center rounded-xl px-5 py-3 text-sm font-semibold",
                            "bg-blue-600 hover:bg-blue-700 text-white transition",
                            isGenerating ? "opacity-60 cursor-not-allowed" : "",
                        ].join(" ")}
                    >
                        {isGenerating ? "Generuji..." : "Vygenerovat play-off"}
                    </button>

                    <button
                        type="button"
                        onClick={handleRedirectToGame}
                        disabled={!isGenerated || isGenerating}
                        className={[
                            "inline-flex items-center justify-center rounded-xl px-5 py-3 text-sm font-semibold",
                            isGenerated && !isGenerating
                                ? "bg-white text-slate-900 border border-slate-300 hover:bg-slate-50"
                                : "bg-white text-slate-400 border border-slate-200 cursor-not-allowed",
                            "transition",
                        ].join(" ")}
                        title={isGenerated ? "Otevřít vygenerované play-off" : "Nejdřív musíš vygenerovat play-off"}
                    >
                        Otevřít play-off
                    </button>
                </div>
            </div>
        </div>
    );
};

export default PlayOffSetupComponent;

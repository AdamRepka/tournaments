export interface TournamentGroupDto {
    id: number;
    name: string;
    players: PlayerDto[];
    matches: TournamentMatchDto[];
}

export interface TournamentMatchDto {
    id: number | null;
    playerHomeId: number;
    playerAwayId: number;
    matchSets: MatchSetDto[];
    tournamentGroupId: number;
    playerHomeSetsWon: number;
    playerAwaySetsWon: number;
}

export interface MatchSetDto {
    id: number | null;
    player1Points: number;
    player2Points: number;
}

export interface PlayerDto {
    id: number;
    firstName: string;
    lastName: string;
    category: PlayerCategory;
    skillLevel: SkillLevel;
    code: string;
    entryFeePaid: boolean;

    points: number | null;
    sets: number | null;
    balls: number | null;
    wins: number | null;
    losses: number | null;
    draws: number | null;
    groupPosition: number | null;
    tournamentPosition: number | null;
    groupIndex: number | null;
}

export type PlayerCategory = 'SENIOR' | 'JUNIOR' | 'WOMEN';

export type SkillLevel = 'BASIC' | 'ADVANCED';

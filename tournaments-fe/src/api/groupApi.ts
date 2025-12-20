import axios from 'axios';
import { TournamentGroupDto } from '../dto/types';

const API_BASE_URL = '/api';

export const fetchGroupById = async (groupId: number): Promise<TournamentGroupDto> => {
    const response = await axios.get<TournamentGroupDto>(`${API_BASE_URL}/groups/${groupId}`);
    return response.data;
};

export const fetchGroupsByTournamentId = async (
    tournamentId: number
): Promise<TournamentGroupDto[]> => {
    const res = await fetch(
        `/api/tournaments/${tournamentId}/groups`
    );

    if (!res.ok) {
        throw new Error('Nepodařilo se načíst skupiny turnaje');
    }

    return res.json();
};


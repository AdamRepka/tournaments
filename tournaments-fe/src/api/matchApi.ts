import axios from 'axios';
import { TournamentMatchDto } from '../dto/types';

const API_BASE_URL = '/api';

export const saveMatch = async (match: TournamentMatchDto): Promise<TournamentMatchDto> => {
    const response = await axios.post<TournamentMatchDto>(`${API_BASE_URL}/matches/result`, match);
    return response.data;
};

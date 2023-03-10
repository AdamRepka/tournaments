package service.impl;

import model.Score;
import org.springframework.stereotype.Service;
import service.api.ScoreServiceInt;

/**
 * This class represents service for Score data object
 */
@Service
public class ScoreServiceImpl implements ScoreServiceInt {

    @Override
    public Boolean validate(Score score) {
        // Covers   - Score is lower than 0
        //          - None of players has score 11 and higher
        if (       score.firstPlayerScore() < 0
                || score.secondPlayerScore() < 0
                || score.firstPlayerScore().equals(score.secondPlayerScore())
                || (score.firstPlayerScore() < 11 && score.secondPlayerScore() < 11)) return false;

        // First player won
        if (score.firstPlayerScore() > score.secondPlayerScore()) {
            // Standard set - In this case second player have to have score in interval <0; 9>
            if (score.firstPlayerScore() == 11) {
                return score.secondPlayerScore() >= 0 && score.secondPlayerScore() <= 9;
            }
            // Non-standard set - In this case first player won only when his score is exactly two points higher
            else if (score.firstPlayerScore() > 11) {
                return score.firstPlayerScore() - score.secondPlayerScore() == 2;
            }
            else return false;
        }
        // Second player won - same logic
        else {
            if (score.secondPlayerScore() == 11) {
                return score.firstPlayerScore() >= 0 && score.firstPlayerScore() <= 9;
            }
            else if (score.secondPlayerScore() > 11) {
                return score.secondPlayerScore() - score.firstPlayerScore() == 2;
            }
            else return false;
        }
    }
}

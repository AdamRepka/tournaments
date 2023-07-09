package service.impl;

import com.tournaments.tournamentsbe.service.impl.ScoreServiceImpl;
import com.tournaments.tournamentsbe.model.competition.Score;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ScoreServiceImpl.class)
class ScoreServiceImplTest {

    @Autowired
    private ScoreServiceImpl scoreService;

    @Test
    void validateValidScoreTest() {
        Score score = new Score(11L, 9L);
        Assertions.assertTrue(scoreService.validate(score));

        score = new Score(9L, 11L);
        Assertions.assertTrue(scoreService.validate(score));

        score = new Score(0L, 11L);
        Assertions.assertTrue(scoreService.validate(score));

        score = new Score(11L, 0L);
        Assertions.assertTrue(scoreService.validate(score));

        score = new Score(11L, 3L);
        Assertions.assertTrue(scoreService.validate(score));

        score = new Score(3L, 11L);
        Assertions.assertTrue(scoreService.validate(score));

        score = new Score(13L, 11L);
        Assertions.assertTrue(scoreService.validate(score));

        score = new Score(11L, 13L);
        Assertions.assertTrue(scoreService.validate(score));

        score = new Score(10L, 12L);
        Assertions.assertTrue(scoreService.validate(score));

        score = new Score(12L, 10L);
        Assertions.assertTrue(scoreService.validate(score));
    }

    @Test
    void validateInvalidScoreTest() {
        Score score = new Score(11L, 11L);
        Assertions.assertFalse(scoreService.validate(score));

        score = new Score(0L, 0L);
        Assertions.assertFalse(scoreService.validate(score));

        score = new Score(10L, 11L);
        Assertions.assertFalse(scoreService.validate(score));

        score = new Score(11L, 10L);
        Assertions.assertFalse(scoreService.validate(score));

        score = new Score(12L, 3L);
        Assertions.assertFalse(scoreService.validate(score));

        score = new Score(14L, 11L);
        Assertions.assertFalse(scoreService.validate(score));

        score = new Score(11L, 14L);
        Assertions.assertFalse(scoreService.validate(score));

        score = new Score(11L, 12L);
        Assertions.assertFalse(scoreService.validate(score));

        score = new Score(12L, 11L);
        Assertions.assertFalse(scoreService.validate(score));
    }
}
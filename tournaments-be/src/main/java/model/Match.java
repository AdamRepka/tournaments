package model;

import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class Match {
    private Long id;
    private Player firstPlayer;
    private Player secondPlayer;
    private MatchStatus matchStatus;
    private Player winner;
    private Player loser;
    private List<MatchSet> sets;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public MatchStatus getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getLoser() {
        return loser;
    }

    public void setLoser(Player loser) {
        this.loser = loser;
    }

    public List<MatchSet> getSets() {
        return sets;
    }

    public void setSets(List<MatchSet> sets) {
        this.sets = sets;
    }
}

package com.cotede.interns.task.round;

import java.util.Objects;
import java.io.Serializable;

public class RoundKey implements Serializable{

        private Long game;
        private Long roundNumber;

        public RoundKey() {}

        public RoundKey(Long game, Long roundNumber) {
            this.game = game;
            this.roundNumber = roundNumber;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RoundKey that = (RoundKey) o;
            return roundNumber == that.roundNumber && Objects.equals(game, that.game);
        }

        @Override
        public int hashCode() {
            return Objects.hash(game, roundNumber);
        }
}

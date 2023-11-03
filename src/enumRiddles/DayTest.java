package enumRiddles;

enum Day {
    MONDAY {
        public Day next() {
            return TUESDAY;
        }

        public int getDayNumber() {
            return 1;
        }
    },
    TUESDAY {
        public Day next() {
            return WEDNESDAY;
        }

        public int getDayNumber() {
            return 2;
        }
    },
    WEDNESDAY {
        public Day next() {
            return THURSDAY;
        }

        public int getDayNumber() {
            return 3;
        }
    },
    THURSDAY {
        public Day next() {
            return FRIDAY;
        }

        public int getDayNumber() {
            return 4;
        }
    },
    FRIDAY {
        public Day next() {
            return SATURDAY;
        }

        public int getDayNumber() {
            return 5;
        }
    },
    SATURDAY {
        public Day next() {
            return SUNDAY;
        }

        public int getDayNumber() {
            return 6;
        }
    },
    SUNDAY {
        public Day next() {


            return MONDAY;
        }

        public int getDayNumber() {
            return 7;

        }

    };

    public abstract Day next();

    public abstract int getDayNumber();


}

public class DayTest {
    public static void main(String[] args) {
        for (Day day : Day.values()) {
            System.out.printf("%s (%d), next is %s\n", day, day.getDayNumber(), day.next());
        }
    }
}

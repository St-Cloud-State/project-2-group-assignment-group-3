package WarehouseFSM;
public class Enums {
    public enum State {
        CLIENT(0),
        CLERK(1),
        MANAGER(2),
        LOGIN(3);

        private final int idx;

        State(int idx) { 
            this.idx = idx; 
        }

        public int idx() { 
            return idx; 
        }
    }

    public enum Transition {
        TO_CLIENT(0),
        TO_CLERK(1),
        TO_MANAGER(2),
        TO_LOGIN(3),
        CLEAN_EXIT(-1),
        ERROR_EXIT(-2);

        private final int code;

        Transition(int code) { 
            this.code = code; 
        }

        public int code() { 
            return code; 
        }
    }
}

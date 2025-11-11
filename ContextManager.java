import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

record Session(Enums.State state, String userId) {}

public class ContextManager {
    private static ContextManager context;
    private static Enums.State currentState = Enums.State.LOGIN;
    private final Deque<Session> sessionStack = new ArrayDeque<>();
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static State[] states;
    private int[][] nextState;

    public String getToken(String prompt) {
        do {
            try {
                System.out.println(prompt);
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
                if (tokenizer.hasMoreTokens()) {
                return tokenizer.nextToken();
                }
            } catch (IOException ioe) {
                System.exit(0);
            }
        } while (true);
    }

    public void addSession(Session session) {
        this.sessionStack.push(session);
         //currentState = session.state();
    }

    public Session getSession() {
        return this.sessionStack.peek();
    }

    private ContextManager() {
        states = new State[4];
        nextState = new int[4][4];

        states[Enums.State.CLIENT.idx()] = ClientMenuState.instance();
        states[Enums.State.CLERK.idx()] = ClerkMenuState.instance();
        states[Enums.State.MANAGER.idx()] = ManagerMenuState.instance();
        states[Enums.State.LOGIN.idx()] = LoginState.instance();

        // Client transitions
        nextState[Enums.State.CLIENT.idx()][Enums.Transition.TO_CLIENT.code()] = Enums.Transition.ERROR_EXIT.code();
        nextState[Enums.State.CLIENT.idx()][Enums.Transition.TO_CLERK.code()] = Enums.Transition.ERROR_EXIT.code();
        nextState[Enums.State.CLIENT.idx()][Enums.Transition.TO_MANAGER.code()] = Enums.Transition.ERROR_EXIT.code();
        nextState[Enums.State.CLIENT.idx()][Enums.Transition.TO_LOGIN.code()] = Enums.State.LOGIN.idx();

        // Clerk transitions
        nextState[Enums.State.CLERK.idx()][Enums.Transition.TO_CLIENT.code()] = Enums.State.CLIENT.idx();
        nextState[Enums.State.CLERK.idx()][Enums.Transition.TO_CLERK.code()] = Enums.Transition.ERROR_EXIT.code();
        nextState[Enums.State.CLERK.idx()][Enums.Transition.TO_MANAGER.code()] = Enums.Transition.ERROR_EXIT.code();
        nextState[Enums.State.CLERK.idx()][Enums.Transition.TO_LOGIN.code()] = Enums.State.LOGIN.idx();

        // Manager transitions
        nextState[Enums.State.MANAGER.idx()][Enums.Transition.TO_CLIENT.code()] = Enums.Transition.ERROR_EXIT.code();
        nextState[Enums.State.MANAGER.idx()][Enums.Transition.TO_CLERK.code()] = Enums.State.CLERK.idx();
        nextState[Enums.State.MANAGER.idx()][Enums.Transition.TO_MANAGER.code()] = Enums.Transition.ERROR_EXIT.code();
        nextState[Enums.State.MANAGER.idx()][Enums.Transition.TO_LOGIN.code()] = Enums.State.LOGIN.idx();

        // Login transitions
        nextState[Enums.State.LOGIN.idx()][Enums.Transition.TO_CLIENT.code()] = Enums.State.CLIENT.idx();
        nextState[Enums.State.LOGIN.idx()][Enums.Transition.TO_CLERK.code()] = Enums.State.CLERK.idx();
        nextState[Enums.State.LOGIN.idx()][Enums.Transition.TO_MANAGER.code()] = Enums.State.MANAGER.idx();
        nextState[Enums.State.LOGIN.idx()][Enums.Transition.TO_LOGIN.code()] = Enums.Transition.ERROR_EXIT.code();
    }

    public void changeState(Enums.Transition transition) {
        System.out.println(sessionStack.size());
        final int curIdx = currentState.idx();
        final int code = transition.code();

        System.out.println(code);

        if (code < 0) {
            handleExitCode(code);
            return;
        }

        int result = nextState[curIdx][code];
        System.out.println(result);
        if (result < 0) {
            handleExitCode(result);
            return;
        }



        currentState = Enums.State.values()[result];
        states[currentState.idx()].run();
    }

    private void handleExitCode(int exitCode) {
        if (exitCode == Enums.Transition.ERROR_EXIT.code()) {
            System.out.println("Error has occurred"); 
            terminate();
        }

        if (exitCode == Enums.Transition.CLEAN_EXIT.code()) {
            terminate();
        }
    }

    public void handleLogout() {
        if (sessionStack.isEmpty()) {           
            changeState(Enums.Transition.TO_LOGIN);
            return;
        }

        sessionStack.pop();
        final Session session = sessionStack.peek();

        if (session != null) {
            currentState = session.state();
            states[currentState.idx()].run();
        } else {
            changeState(Enums.Transition.TO_LOGIN);
        }
    }

    private void terminate() {
        System.out.println(" Goodbye \n ");
        System.exit(0);
        return;
    }

    public static ContextManager instance() {
        if (context == null) {
            System.out.println("Calling constructor");
            context = new ContextManager();
        }
        return context;
    }

    public void process(){
        states[currentState.idx()].run();
    }

    public static void main (String[] args){
        ContextManager.instance().process();
      }
}

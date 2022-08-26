package util.tool;

public class thread {
    public static Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) {
                System.out.println(t.getName());
                return t;
            }
        }
        return null;
    }
}

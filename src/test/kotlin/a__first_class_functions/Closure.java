package a__first_class_functions;

public class Closure {
    public String foo = "";

    public static void process(final Closure t) {
        System.out.println(t.toString() + "= " + t.foo);
        t.foo = "bar";
        new Runnable() {
            public void run() {
                System.out.println(t.toString() + " = " + t.foo);
                t.foo = "baz";
            }
        }.run();
        System.out.println(t.toString() + " = " + t.foo);
    }

    public static void main(String[] args) {
        process(new Closure());
    }
}

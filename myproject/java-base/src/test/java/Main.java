public class Main {
    public static void main(String[] args) {
        String str = "abc    def   ghi     jkl  1111 2222";

        str = str.replaceAll( "\\s+", " " );

        System.out.println(str);
    }
}

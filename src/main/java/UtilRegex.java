import ru.lanwen.verbalregex.VerbalExpression;

/**
 * Created by cy on 2016/3/21.
 */
public class UtilRegex {
    public static boolean isMatchDeclare(String content){
        String declare="private Button  mBtn_Login_;";
        VerbalExpression testRegex=VerbalExpression.regex()
                .startOfLine()
                .then("private")
                .space()
                .oneOrMore()
                .range("A","Z")
                .count(1)

                .capt()
                .range("0","9","a","z","A","Z")
                .maybe("_")
                .endCapt()
                .zeroOrMore()

                .space()
                .oneOrMore()
                .then("m")

                .capt()
                .range("0","9","a","z","A","Z","_")
                .maybe("_")
                .endCapt()

                .zeroOrMore()
                .space()
                .zeroOrMore()
                .then(";")
                .build();

        return testRegex.testExact(content);
    }
}

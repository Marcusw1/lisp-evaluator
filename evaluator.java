package evaluator;

import java.util.*;

public class Evaluator {

    private Stack<Operand> opdStack;
    private Stack<Operator> oprStack;

    public Evaluator() {

        opdStack = new Stack<Operand>();
        oprStack = new Stack<Operator>();

        Operator.operators.put("+", new AdditionOperator());
        Operator.operators.put("-", new SubtractionOperator());
        Operator.operators.put("*", new MultiplicationOperator());
        Operator.operators.put("/", new DivideOperator());
        Operator.operators.put("#", new PoundOperator());

    }

    public int eval(String expr) {

        String tok;
        int result = 0;

        // init stack - necessary with operator priority schema;
        // the priority of any operator in the operator stack other then
        // the usual operators - "+-*/" - should be less than the priority
        // of the usual operators 
        oprStack.push((Operator) Operator.operators.get("#"));
        String delimiters = "+-*/#! ";

        StringTokenizer st = new StringTokenizer(expr, delimiters, true);

        while (st.hasMoreTokens()) {

            if (!(tok = st.nextToken()).equals(" ")) {  // filter out spaces 

                if (Operand.check(tok)) {                // check if tok is an operand 

                    opdStack.push(new Operand(tok));
                } else {
                    if (!Operator.check(tok)) {         //check if invalid operator
                        System.out.println("*****invalid token******");
                        System.exit(1);
                    }

                    Operator newOpr = (Operator) Operator.operators.get(tok); // POINT 1

                    while (((Operator) oprStack.peek()).priority() >= newOpr.priority()) {

                        Operator oldOpr = ((Operator) oprStack.pop());
                        Operand op2 = (Operand) opdStack.pop();
                        Operand op1 = (Operand) opdStack.pop();
                        opdStack.push(oldOpr.execute(op1, op2));

                    }

                    oprStack.push(newOpr);
                }
            }
        }

       // Control gets here when we've picked up all of the token  
        while (oprStack.peek().priority() > 1) {
            Operator oldOpr = ((Operator) oprStack.pop());
            Operand op2 = (Operand) opdStack.pop();
            Operand op1 = (Operand) opdStack.pop();
            opdStack.push(oldOpr.execute(op1, op2));
        }

        result = opdStack.pop().getValue();
        oprStack.pop();

        return result;
    }

}

//abstract operator class
abstract class Operator {

    public static final HashMap operators = new HashMap();

    abstract int priority();

    static boolean check(String tok) {
        boolean isValid = false;

        switch (tok) {
            case "+":
                isValid = true;
                break;
            case "-":
                isValid = true;
                break;
            case "*":
                isValid = true;
                break;
            case "/":
                isValid = true;
                break;
            case "#":
                isValid = true;
                break;
        }

        return isValid;
    }

    abstract Operand execute(Operand opd1, Operand opd2);
}

//priority 1 pound sign
class PoundOperator extends Operator {

    @Override
    int priority() {
        return 1;
    }

    @Override
    public Operand execute(Operand op1, Operand op2) {
        return op2;
    }
}

//priority 2, addition and subtraction
class AdditionOperator extends Operator {

    @Override
    int priority() {
        return 2;
    }

    @Override
    public Operand execute(Operand op1, Operand op2) {
        int result = op1.getValue() + op2.getValue();
        Operand op3 = new Operand(result);

        return op3;
    }
}

class SubtractionOperator extends Operator {

    @Override
    int priority() {
        return 2;
    }

    @Override
    public Operand execute(Operand op1, Operand op2) {
        int result = op1.getValue() - op2.getValue();
        Operand op3 = new Operand(result);

        return op3;
    }
}

//priority 3 multiplcation and division
class MultiplicationOperator extends Operator {

    @Override
    int priority() {
        return 3;
    }

    @Override
    public Operand execute(Operand op1, Operand op2) {
        int result = op1.getValue() * op2.getValue();
        Operand op3 = new Operand(result);

        return op3;
    }
}

class DivideOperator extends Operator {

    @Override
    int priority() {
        return 3;
    }

    @Override
    public Operand execute(Operand op1, Operand op2) {

        int result = op1.getValue() / op2.getValue();
        Operand op3 = new Operand(result);

        return op3;
    }
}

//operand class
class Operand {

    private int operandValue;
    private String operandTok;

    public Operand(String tok) {

        if (check(tok) == true) {
            operandTok = tok;
            operandValue = Integer.parseInt(tok);
        } else {
            operandTok = "";
        }
    }

    public Operand(int value) {
        this.operandValue = value;
    }

    static boolean check(String tok) {
        boolean isValid = true;
        int value;

        try {

            value = Integer.parseInt(tok);

        } catch (Exception e) {

            isValid = false;
        }

        return isValid;
    }

    int getValue() {

        return operandValue;
    }
}

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ChemicalStack {

    Stack<String> stack = new Stack<>();
    int mass = 0;
    String result;

    public ChemicalStack(String formula){
        pushStack(formula);
        calculate();
    }

    private void pushStack(String formula){//将字符串转化为栈储存
        char[] chars = formula.toCharArray();
        for (char c : chars) {
            if (Matchers.matcherLower(String.valueOf(c))) {//如果遇到小写字母则将其与前面的大写字母一块作为元素储存
                stack.push(stack.pop() + c);
            } else {
                stack.push(String.valueOf(c));
            }
        }
    }

    private void calculate(){
        if (hasMiddleBrackets()){//含中括号
            result = brackets(stack);
        }else if (hasLowerBrackets()){//仅含小括号
            result = lowers(stack);
        }else if (hasDot()){//含点的简单形式
            result = dot(stack);
        }else {//简单形式
            result = basic(stack);
        }
    }

    private String brackets(Stack<String> stack){
        Stack<String> temp_s = new Stack<>();
        List<Stack<String>> middles = new ArrayList<>();
        StringBuilder ret = new StringBuilder();
        while (!stack.empty()){//遍历栈
            if (Matchers.matcherNumber(stack.peek())){//判断数字
                String num = stack.pop();
                if (Matchers.matcherMiddleBrackets(stack.peek())){//判断中括号
                    //若数字前是括号则提取括号及其内部的内容(含外层数字)->collectMiddle()
                    Stack<String> temp = collectMiddle(stack);
                    temp.push(num);
                    middles.add(temp);//所有的括号内容各自独立的存入List
                }else {
                    temp_s.push(num);
                }
            }else if (Matchers.matcherMiddleBrackets(stack.peek())){//若碰到中括号则用collectMiddle
                middles.add(collectMiddle(stack));
            }else {
                temp_s.push(stack.pop());
            }
        }
        ret.append(basic(flip(temp_s))).append("+");//将除去括号的内容进行简单形式运算得出运算式
        for (int i = 0;i < middles.size();i++){//遍历所有中括号
            if (i+1 == middles.size()){//判断这个中括号是不是最后一个,若是则末尾不加+号
                ret.append(middleBrackets(middles.get(i)));
            }else {
                ret.append(middleBrackets(middles.get(i))).append("+");
            }
        }
        return ret.toString();
    }

    private Stack<String> collectLower(Stack<String> stack){
        Stack<String> temp = new Stack<>();
        temp.push(stack.pop());
        while (!Matchers.matcherLowerBrackets(stack.peek())){//
            temp.push(stack.pop());
        }
        temp.push(stack.pop());
        return temp;
    }

    private Stack<String> collectMiddle(Stack<String> stack){
        Stack<String> temp = new Stack<>();
        temp.push(stack.pop());
        while (!Matchers.matcherMiddleBrackets(stack.peek())){
            temp.push(stack.pop());
        }
        temp.push(stack.pop());
        return temp;
    }


    private String middleBrackets(Stack<String> stack){
        String times = "1";
        if (Matchers.matcherNumber(stack.peek())){
            times = stack.pop();
        }
        stack = removeMiddle(flip(stack));
        if (times.equals("1")){
            return lowers(stack);
        }else {
            return times + "*(" + lowers(stack) + ")";
        }
    }


    private String lowers(Stack<String> stack){
        StringBuilder ret = new StringBuilder();
        Stack<String> temp_s = new Stack<>();
        List<Stack<String>> lowers = new ArrayList<>();
        while (!stack.empty()){
            if (Matchers.matcherNumber(stack.peek())){
                String num = stack.pop();
                if (Matchers.matcherLowerBrackets(stack.peek())){
                    Stack<String> temp = collectLower(stack);
                    temp.push(num);
                    lowers.add(temp);
                }else {
                    temp_s.push(num);
                }
            }else if (Matchers.matcherLowerBrackets(stack.peek())){
                lowers.add(collectLower(stack));
            }else {
                temp_s.push(stack.pop());
            }
        }
        ret.append(basic(flip(temp_s))).append("+");
        for (int i = 0;i < lowers.size();i++){
            if (i+1 == lowers.size()){
                ret.append(lowerBrackets(lowers.get(i)));
            }else {
                ret.append(lowerBrackets(lowers.get(i))).append("+");
            }
        }
        return ret.toString();
    }

    private String lowerBrackets(Stack<String> stack){
        String times = "1";
        if (Matchers.matcherNumber(stack.peek())){
            times = stack.pop();
        }
        stack = removeLower(stack);
        if (times.equals("1")){
            return basic(stack);
        }else {
            return times + "*(" + basic(stack) + ")";
        }
    }

    private Stack<String> removeMiddle(Stack<String> stack){
        Stack<String> temp = new Stack<>();
        while (!stack.empty()){
            if (Matchers.matcherMiddleBrackets(stack.peek())){
                stack.pop();
            }else {
                temp.push(stack.pop());
            }
        }
        return flip(temp);
    }

    private Stack<String> removeLower(Stack<String> stack){
        Stack<String> temp = new Stack<>();
        while (!stack.empty()){
            if (Matchers.matcherLowerBrackets(stack.peek())){
                stack.pop();
            }else {
                temp.push(stack.pop());
            }
        }
        return flip(temp);
    }

    private String dot(Stack<String> stack){
        Stack<String> temp_s = new Stack<>();
        StringBuilder ret = new StringBuilder();
        while (!stack.empty()){
            if (Matchers.matcherDot(stack.peek())){
                stack.pop();
                ret.append(basic(stack)).append("+");
                flipStack(stack,temp_s);
                ret.append(times(Integer.parseInt(temp_s.pop()), stack));
            }else {
                temp_s.push(stack.pop());
            }
        }
        return ret.toString();
    }

    private String times(int times,Stack<String> stack){
        if (times == 1){
            return basic(stack);
        }else {
            return times + "*(" + basic(stack) + ")";
        }

    }

    private String basic(Stack<String> stack){
        StringBuilder ret = new StringBuilder();
        while (!stack.empty()){
            if (Matchers.matcherNumber(stack.peek())){
                String num = stack.pop();
                if (stack.empty()){
                    ret = new StringBuilder(num + "*(" + ret + ")");
                }else {
                    ret.append(num).append("*");
                }
            }else {
                ret.append(ElementsTable.getMass(stack.pop()));
                if (!stack.empty()){
                    String element = stack.pop();
                    if (!(stack.empty() && Matchers.matcherNumber(element))){
                        ret.append("+");
                    }
                    stack.push(element);
                }
            }
        }
        return ret.toString();
    }

    private boolean hasMiddleBrackets(){
        boolean ret = false;
        for (String s:stack){
            if (Matchers.matcherMiddleBrackets(s)){
                ret = true;
                break;
            }
        }
        return ret;
    }

    private boolean hasLowerBrackets(){
        boolean ret = false;
        for (String s:stack){
            if (Matchers.matcherLowerBrackets(s)){
                ret = true;
                break;
            }
        }
        return ret;
    }

    private boolean hasDot(){
        boolean ret = false;
        for (String s:stack){
            if (Matchers.matcherDot(s)){
                ret = true;
                break;
            }
        }
        return ret;
    }

    private void flipStack(Stack<String> in,Stack<String> out){
        String num = "1";
        if (Matchers.matcherNumber(out.peek())){
            num = out.pop();
        }
        while (!out.empty()){
            in.push(out.pop());
        }
        out.push(num);
    }

    private Stack<String> flip(Stack<String> stack){
        Stack<String> temp = new Stack<>();
        while (!stack.empty()){
            temp.push(stack.pop());
        }
        return temp;
    }

    public String getResult(){
        return result;
    }

}

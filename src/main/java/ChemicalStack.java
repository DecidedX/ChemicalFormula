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

    private Stack<String> collectLower(Stack<String> stack){//同中括号一样
        Stack<String> temp = new Stack<>();
        temp.push(stack.pop());
        while (!Matchers.matcherLowerBrackets(stack.peek())){
            temp.push(stack.pop());
        }
        temp.push(stack.pop());
        return temp;
    }

    private Stack<String> collectMiddle(Stack<String> stack){
        Stack<String> temp = new Stack<>();
        temp.push(stack.pop());//括号入栈
        while (!Matchers.matcherMiddleBrackets(stack.peek())){//再次碰到括号时停止
            temp.push(stack.pop());//元素入栈
        }
        temp.push(stack.pop());//括号入栈
        return temp;//返回括号及其内容
    }


    private String middleBrackets(Stack<String> stack){//对中括号栈进行处理
        String times = "1";//栈顶有数字即是倍数,若不是则倍数为1
        if (Matchers.matcherNumber(stack.peek())){
            times = stack.pop();
        }
        stack = removeMiddle(flip(stack));//移除中括号，flip是翻转栈
        if (times.equals("1")){
            return lowers(stack);
        }else {
            return times + "*(" + lowers(stack) + ")";
        }
    }


    private String lowers(Stack<String> stack){//获取小括号及其内容
        StringBuilder ret = new StringBuilder();
        Stack<String> temp_s = new Stack<>();//临时栈
        List<Stack<String>> lowers = new ArrayList<>();//每个括号及内容为一个元素，将其存入此列表
        while (!stack.empty()){
            if (Matchers.matcherNumber(stack.peek())){//判断数字
                String num = stack.pop();//从栈顶移除并赋值给num
                if (Matchers.matcherLowerBrackets(stack.peek())){
                    //判断括号，若是括号则存有倍数关系
                    //collectLower 收集小括号内容
                    Stack<String> temp = collectLower(stack);
                    temp.push(num);//将倍数存入栈顶
                    lowers.add(temp);//添加到存括号的列表
                }else {
                    //不是括号，则为简单形式，直接入栈
                    temp_s.push(num);
                }
            }else if (Matchers.matcherLowerBrackets(stack.peek())){//不是数字时，判断括号
                //若是括号则为简单形式括号，直接收集并添加到列表
                lowers.add(collectLower(stack));
            }else {
                //不是括号，则为简单形式，直接入栈
                temp_s.push(stack.pop());
            }
        }
        //输入栈内括号处理结束后
        if (!temp_s.empty()){//栈内剩下的简单形式处理
            ret.append(basic(flip(temp_s))).append("+");
        }
        //遍历存括号内容的列表
        for (int i = 0;i < lowers.size();i++){
            if (i+1 == lowers.size()){//若为列表中最后一个，则不加括号
                ret.append(lowerBrackets(lowers.get(i)));
            }else {
                ret.append(lowerBrackets(lowers.get(i))).append("+");
            }
        }
        return ret.toString();
    }

    private String lowerBrackets(Stack<String> stack){
        //初始倍数为1
        String times = "1";
        //若栈最上层匹配数字，则为倍数并覆盖初始值
        if (Matchers.matcherNumber(stack.peek())){
            times = stack.pop();
        }
        stack = removeLower(stack);//移除小括号
        if (times.equals("1")){
            return basic(flip(stack));//1倍不需要乘
        }else {
            return times + "*(" + basic(flip(stack)) + ")";
        }
    }

    private Stack<String> removeMiddle(Stack<String> stack){
        Stack<String> temp = new Stack<>();
        while (!stack.empty()){
            //若是括号则移除，不是就保留
            if (Matchers.matcherMiddleBrackets(stack.peek())){
                stack.pop();
            }else {
                temp.push(stack.pop());
            }
        }
        return flip(temp);
    }

    private Stack<String> removeLower(Stack<String> stack){//同Middle
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

    private String dot(Stack<String> stack){//中间存在点的简单式
        //临时栈，以点为界限，此栈用于存放后半部分
        Stack<String> temp_s = new Stack<>();
        StringBuilder ret = new StringBuilder();
        while (!stack.empty()){
            //若元素匹配为点，则分离完成
            if (Matchers.matcherDot(stack.peek())){
                stack.pop();//删除点
                //此时临时栈和输入栈均为简单形式，使用basic方法进行运算获取返回值并用加号相连
                ret.append(basic(stack)).append("+");
                flipStack(stack,temp_s);
                ret.append(times(Integer.parseInt(temp_s.pop()), stack));
            }else {
                //一直出栈未匹配到点则一直存入临时栈
                temp_s.push(stack.pop());
            }
        }
        return ret.toString();
    }

    private String times(int times,Stack<String> stack){//倍数处理
        if (times == 1){
            //倍数为一则进行简单处理
            return basic(stack);
        }else {
            //倍数不为一则将倍数乘以栈处理结果
            return times + "*(" + basic(stack) + ")";
        }

    }

    private String basic(Stack<String> stack){
        StringBuilder ret = new StringBuilder();
        while (!stack.empty()){//遍历栈
            if (Matchers.matcherNumber(stack.peek())){//判断数字
                String num = stack.pop();//取出数字
                if (stack.empty()){//若是空栈则为总倍数
                    ret = new StringBuilder(num + "*(" + ret + ")");
                }else {
                    ret.append(num).append("*");//加上乘号，表示乘以后方元素
                }
            }else {
                //不是数字
                ret.append(ElementsTable.getMass(stack.pop()));//获取元素的相对原子质量
                if (!stack.empty()){
                    //出栈再判断是否为空栈，若不是空栈并且不是数字则在后方添加加号
                    String element = stack.pop();
                    if (!(stack.empty() && Matchers.matcherNumber(element))){
                        ret.append("+");
                    }
                    stack.push(element);//元素入栈，继续进行处理
                }
            }
        }
        return ret.toString();
    }

    private boolean hasMiddleBrackets(){//栈内是否含有中括号
        boolean ret = false;
        for (String s:stack){
            if (Matchers.matcherMiddleBrackets(s)){
                ret = true;
                break;
            }
        }
        return ret;
    }

    private boolean hasLowerBrackets(){//栈内是否含有小括号
        boolean ret = false;
        for (String s:stack){
            if (Matchers.matcherLowerBrackets(s)){
                ret = true;
                break;
            }
        }
        return ret;
    }

    private boolean hasDot(){//栈内是否含有点符号
        boolean ret = false;
        for (String s:stack){
            if (Matchers.matcherDot(s)){
                ret = true;
                break;
            }
        }
        return ret;
    }

    private void flipStack(Stack<String> in,Stack<String> out){//TODO:NEED REFACTOR
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
        //遍历出栈移入栈完成翻转
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

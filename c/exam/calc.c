/*
模拟简单运算器的工作。假设计算器只能进行加减乘除运算，运算数和结果都是整数，四种运算符的优先级相同，按从左到右的顺序计算。

输入格式
=======
输入在一行中给出一个四则运算算式，没有空格，且至少有一个操作数。 遇等号”=” 说明输入结束。

输出格式
=======
在一行中输出算式的运算结果，或者如果除法分母为0或有非法运算符，则输出错误信息"ERROR”

输入样例:
1+2*10-10/2=

输出样例:
10
*/
#include "token.c";

void calc(s_token **tokens, int token_count) {
  int operand1;
  char op = 0;
  int i;
  int error = 0;

  for (i = 0; i < token_count; i++) {
    s_token* token = tokens[i];

    switch (token->type) {
      case 1: {
        s_operator_token *operator_token = (s_operator_token *)token;
        switch (operator_token->c) {
          case '+':
          case '-':
          case '*':
          case '/': {
            op = operator_token->c;
            break;
          }
          case '=':
            goto end;
          default:
            error = 1;
            goto error_end;
        }
        break;
      }
      case 2: {
        int operand = ((s_number_token *)token)->num->value;
        if (op == 0) {
          operand1 = operand;
        } else {
          switch (op) {
            case '+':
              operand1 = operand1 + operand;
              break;
            case '-':
              operand1 = operand1 - operand;
              break;
            case '*':
              operand1 = operand1 * operand;
              break;
            case '/':
              if (operand == 0) {
                error = 1;
                goto error_end;
              }
              operand1 = operand1 / operand;
              break;
          }
          op = 0;
        }
        break;
      }
    }
  }

  error_end:
  if (error) {
    printf("ERROR\n");
    return;
  }

  end:
  printf("%d", operand1);
}

int main() {
  char exprStr[40];
  scanf("%s", exprStr);
  int token_count = 0;
  s_token** tokens = parse_tokens(exprStr, &token_count);
  calc(tokens, token_count);
  return 0;
}
/*
正题
====
四则运算表达式由运算数(必定包含数字，可能包含正或负符号、小数点)、 
运算符 (包括+、-、*、/) 以及小括号(和)组成，每个运算数、运算符和括号都是一个token (标记)。
现在，对于给定的一个四则运算表达式，请把她的每个token切分出来。题目保证给定的表达式是正确的，不需要做有效性检查。

输入格式
=======
在-行中给出长度不超过40个字符的表达式，其中没有空格，仅由上文中token的字符组成

输出格式
=======
依次输出表达式中的tokens,每个token占一行。
*/

#include <stdio.h>
#include <ctype.h>
#include <math.h>
#include <stdlib.h>

// token
typedef struct {
  short type;
} s_token;

// 运算符token
typedef struct {
  s_token token;
  char c;  
} s_operator_token;

// 数字token
typedef struct {
  s_token token;
  s_number *num;  
} s_number_token;

// 表示数字
typedef struct {
  double value;
  // 是否是浮点数
  short isFloat;
  // 小数位数
  short p;
} s_number;

s_token* make_operator_token(char c) {
  s_operator_token *token = (s_operator_token *)malloc(sizeof(s_operator_token));
  ((s_token *)token)->type = 1;
  token->c = c;
  return (s_token *)token;
}

s_token* make_number_token(s_number *num) {
  s_number_token *token = (s_number_token *)malloc(sizeof(s_number_token));
  ((s_token *)token)->type = 2;
  token->num = num;
  return (s_token *)token;
}

// 打印一个数字
void print_number(s_number *num) {
  if (num->isFloat) {
    char fmt[7];
    sprintf(fmt, "%%.%dlf\n", num->p);
    printf(fmt, num->value);
  } else {
    printf("%ld\n", (long int)num->value);
  }
}

// 读一个浮点数
s_number* read_float(const char *exprStr, int sign, char **pos) {
  double n = 0;
  int p = 0;
  char *sc = (char*)exprStr;
  for (; *sc; sc++) {
    if (isdigit(*sc)) {
      n = n * 10 + (*sc - '0');
      p++;
    } else if (*sc == '.') {
      continue;
    } else {
      break;
    }
  }

  *pos = (char*)exprStr + (sc - exprStr);

  s_number *num = (s_number *)malloc(sizeof(s_number));
  num->isFloat = 1;
  num->p = p;
  num->value = sign * (n / pow(10, p));
  return num;
}

// 读一个数字，可能是整数或是小数
s_number* read_number(const char *exprStr, int sign, char **pos) {
  double n = 0;
  int hasFloat = 0;
  // 读整数部分
  char *sc = (char*)exprStr;
  for (; *sc; sc++) {
    if (isdigit(*sc)) {
      n = n * 10 + (*sc - '0');
    } else if (*sc == '.') {
      hasFloat = 1;
      break;
    } else {
      break;
    }
  }

  s_number *num = (s_number *)malloc(sizeof(s_number));
  num->isFloat = hasFloat;

  // 如果有小数部分
  if (hasFloat) {
    s_number *float_num = read_float(sc, +1, pos);
    n += float_num->value;
    num->p = float_num->p;
  } else {
    *pos = (char*)(exprStr + (sc - exprStr));
  }

  num->value = sign * n;
  return num;
}

// 暂时写死token数量
#define TOKEN_MAX_NUMBER 300

s_token** parse_tokens(const char *exprStr, int *token_count) {
  s_token **tokens = (s_token **)malloc(sizeof(s_token *) * TOKEN_MAX_NUMBER);
  int index = 0;
  char *sc = (char*)exprStr;
  while (*sc) {
    switch (*sc) {
      case '+':
      case '-':
      case '*':
      case '/':
      case '(':
      case ')':
      case '=':
        tokens[index++] = make_operator_token(*sc);
        sc++;
        break;
      case '.':
        if (isdigit(*(sc + 1))) {
          tokens[index++] = make_number_token(read_float(sc, +1, &sc));
        }
        break;
      default:
        if (isdigit(*sc)) {
          tokens[index++] = make_number_token(read_number(sc, +1, &sc));
        } else {
          sc++;
        }
    }
  }

  *token_count = index;
  return tokens;
}

void analyse_print(s_token **tokens, int token_count) {
  s_token *prev = NULL;
  int i;
  for (i = 0; i < token_count; i++) {
    s_token* token = tokens[i];
    switch (token->type) {
      case 1: {
        s_operator_token *operator_token = (s_operator_token *)token;
        if (operator_token->c == '+' || operator_token->c == '-') {
          if (prev == NULL || prev->type == 1 && ((s_operator_token *)prev)->c == '(') {
            printf("%c", operator_token->c);
          } else {
            printf("%c\n", operator_token->c);
          }
        } else {
          printf("%c\n", operator_token->c);
        }
        break;
      }
      case 2:
        print_number(((s_number_token *)token)->num);
        break;
    }
    prev = token;
  }
}

int main() {
  //char exprStr[40] = "32*((2-2)+5)/(-15)";
  char exprStr[40];
  scanf("%s", exprStr);
  int token_count = 0;
  s_token** tokens = parse_tokens(exprStr, &token_count);
  analyse_print(tokens, token_count);
  return 0;
}
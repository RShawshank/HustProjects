.386
STACK  SEGMENT  USE16  STACK
DB  200  DUP(0)
STACK  ENDS 
DATA  SEGMENT  USE16
XUEHAO  DB  20   DUP(0)
BUF1  DB  6,6,0
DATA  ENDS
CODE  SEGMENT  USE16
ASSUME  CS: CODE, DS :DATA,SS:STACK
START: MOV  AX ,DATA
            MOV   DS,AX
		    MOV CX,3
	    	MOV SI ,0
		    MOV DI,OFFSET XUEHAO
		    LEA BX,BUF1  ;作为BUF1的基址
LOPA:	MOV AL,[BX]
			MOV [DI],AL
			ADD BX,1
			ADD DI,1
			DEC CX
			JNZ LOPA
			MOV AL,BUF1
			MOV XUEHAO[3],AL ;直接寻址方式
			MOV AL,BUF1+1
			MOV XUEHAO[4],AL
			MOV AL,BUF1+2
			MOV XUEHAO[5],AL
			LEA BX,BUF1
			MOV AL,0[BX]   ;变址寻址
			MOV XUEHAO[6],AL
			MOV AL,1[BX]
			MOV XUEHAO[7],AL
			MOV AL,2[BX]
			MOV XUEHAO[8],AL
			LEA BX,BUF1
			MOV AL,0[BX][SI]  ;基址变址寻址
			MOV XUEHAO[9],AL
			MOV AL,1[BX][SI]
			MOV XUEHAO[10],AL
			MOV AL,2[BX][SI]
			MOV XUEHAO[11],AL
			CODE  ENDS 
					END   START

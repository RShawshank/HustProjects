.386
STACK  SEGMENT  USE16  STACK
            DB  200  DUP(0)
STACK  ENDS

DATA            SEGMENT      USE16
BNAME           DB                'ZHANG SAN',0              ;�ϰ��������������Լ����ֵ�ƴ�����˴�0��ascll��Ϊ00�����ַ�
BPASS           DB                'test',0,0                    ;����
N               EQU               30
SNAME           DB                'SHOP','$'                       ;�������ƣ���0����
GA1             DB                'PEN', 7 DUP(0) ,10    ;��Ʒ���Ƽ��ۿ�
                DW                35,56,70,25,0          ; �Ƽ��Ȼ�δ����
GA2             DB                'BOOK', 6 DUP(0) ,9    ; ��Ʒ���Ƽ��ۿ�  һ��21���ֳ���
                DW                12,30,25,5,0           ;�Ƽ��Ȼ�δ����
GAN             DB                N-2 DUP( 'Temp-Value',8,15,0,20,0,30,0,2,0,0,0) ;����2���Ѿ����嶨���˵���Ʒ��Ϣ���⣬������Ʒ��Ϣ��ʱ�ٶ�Ϊһ���ġ�
BUF1            DB                0DH,0AH,'please input the name?','$'
BUF2            DB                0DH,0AH,'please input the password?','$'
BUF3            DB                0DH,0AH,'please input what do you want to search?','$'
BUF4            DB                0DH,0AH,'the place is zero.','$'
BUF5            DB                0DH,0AH,'$'
IN_NAME         DB                10   
                DB                0
		        DB                10   DUP(0)
IN_PWD		  	DB                6
				DB                0 
				DB                6    DUP(0)
IN_GOODS        DB                10
				DB                0
				DB                10   DUP(0)
NEXT            DB                10
                DB                0
				DB                10   DUP(0)
AUTH            DB                0
GOODS           DB                0;     ��¼��Ʒ��� 
DATA  ENDS

CODE            SEGMENT      USE16
                ASSUME       DS:DATA,SS:STACK,CS:CODE
START  :        
				MOV                AX,STACK
				MOV                SS,AX
				MOV                SP,200
				MOV                AX, DATA
				MOV                DS,AX
				MOV                DI,2
				LEA                DX,SNAME+0 ;�����������
				MOV                AH,9
				INT                21H
				LEA                DX,BUF1    ;������������
				MOV                AH,9
				INT                21H
				LEA                DX,IN_NAME   ;��������
				MOV                AH,10
				INT                21H	
				CMP                IN_NAME[DI],'q'         ;�ж��Ƿ��������q
				JE                 END_THIS                ;�����˳�
				JMP				   CONTINUE				
END_THIS:	
				JMP                ENDCODE
CONTINUE:		CMP                IN_NAME[DI],0DH
				JE                 ADD_AUTH                ;����س�������auth��ֵΪ0
JUDGE:          pop                DI
				CMP                IN_NAME[DI],0DH         ;�ж��Ƿ�������ǻس���
				JE                 THREE                   ;���빦�������������ܶ�
				LEA                DX,BUF2                 ;������������
				MOV                AH,9
				INT                21H
				LEA                DX,IN_PWD               ;��������
				MOV                AH,10
				INT                21H
				JMP                SECOND
ADD_AUTH:       PUSH               DI   
				MOV                DI,0
				MOV                AUTH[DI],0
			    JMP                JUDGE
SECOND	:       MOV                SI,1
				MOV                DI,-1
LOOPAFORNAME:   LEA                BX,IN_NAME
				MOV                CL,9
                CMP                IN_NAME+1,CL
				JNE                START
				INC                SI
				INC                DI
				CMP                BNAME[DI],'N'          ;������'N'ʱ������ѭ��
				JE                 LOOPAFORPASSWORD       ;��ʼ�ж������Ƿ�������ȷ
				MOV                CL,[BX+SI]
				CMP                BNAME[DI],CL	
				JE                 LOOPAFORNAME           ;�����Ƚ��������ַ�
				JNE                START                  ;ʧ�ܷ��ع���1
LOOPAFORPASSWORD:MOV               SI,1
				MOV                DI,-1
LOOPA:          LEA                BX,IN_PWD
                MOV                CL,4
				CMP                IN_PWD+1,4
		        JNE                START	
				INC                SI
				INC                DI
				CMP                BPASS[DI],'t'          ;������'0'ʱ������ѭ��
				JE                 ADD_AUTH_ONE           ;auth��ֵ 1 ��������������3
				POP                DI
				MOV                CL,[BX][SI]
				CMP                BPASS[DI],CL
				JE                 LOOPA
				JNE                START                  ;ʧ�ܷ��ع���1
ADD_AUTH_ONE : 	
				push               DI
				MOV                DI,0
				MOV                AUTH[DI],1
THREE:          LEA                DX,BUF3              ;������鿴����Ʒ������
			    MOV                AH,9
			    INT                21H
			    LEA                DX,IN_GOODS        ;������Ʒ��
		        MOV                AH,10
			    INT                21H
			    MOV                DI,2
			    CMP                IN_GOODS[DI],0DH      ;����س���
			    JE                 START                 ;���ع���1
			    LEA                BX,GA1                ;��GA1�Ļ�ַ��Ϊ�ӳ���Ĳ�������
			    MOV                AL,N                  ;BL��Ϊ��Ʒ����
;���ܣ��ж��������Ʒ������BX��Ϊ��ַ����Ʒ���Ƚ�	
	  
  
LOOPA_number: MOV                  SI,1
			  MOV                  DI,-1
              DEC                  AL
LOOPA_GOODS:  INC                  SI
			  INC                  DI
			  CMP                  BYTE PTR [BX+DI],0           ;��Ʒ��������ȷ
			  JE                   CHECK_LOAD                   ;�жϵ�¼״̬
			  MOV                  CL,IN_GOODS[SI]
			  CMP                  [BX][DI],CL
			  JE                   LOOPA_GOODS
			  ADD                  BX,21                  ;��Ʒ�����벻��ȷ�������һ����Ʒ��
			  CMP                  AL,0
			  JE                   THREE                   ;δ�ҵ���Ʒ�����ع�����
			  JNE                  LOOPA_number            ;������Ʒ�������Ա���һ����Ʒ			  
CHECK_LOAD:   MOV                  DI,0
			  CMP                  AUTH[DI],1
			  JE                   SHOWGOODSNAME           ;��¼��ȷ��չʾ��Ʒ��Ϣ
			  JNE                  INTOFOUTH               ;�����Ƽ��ȣ����빦����			  
SHOWGOODSNAME:
              MOV                  BYTE PTR[BX+SI],'$' 
			  LEA                  DX,BUF5
			  MOV                  AH,9
			  INT                  21H
			  LEA                  DX,[BX]
			  MOV                  AH,9
			  INT                  21H
			  JMP                  START                 
INTOFOUTH:    
              MOV                  CX,0
			  MOV                  AX,0
			  MOV                  AX,WORD PTR [BX+11]   ;�����۸�
			  MOV                  CX,WORD PTR [BX+13]   ;���ۼ۸�
			  MOV                  DX,128
			  IMUL                 AX,DX
			  MOV                  DX,0                  ;��ֹDX���
			  CMP                  CX,0
			  JE                   OVERFLOW
			  DIV                  CX
			  push                 AX
			  PUSH                 BX
			  MOV                  CX,0
			  MOV                  AX,0
			  POP                  BX
			  MOV                  AX,WORD PTR [BX+17]    ;��������
			  MOV                  CX,WORD PTR [BX+15]     ;��������
			  MOV                  DX,128
			  IMUL                 AX,DX 
			  MOV                  DX,0
			  CMP                  AX,0
			  JE                   OVERFLOW
			  DIV                  CX   
			  POP                  DX
			  ADD                  AX,DX
			  MOV                  WORD PTR [BX+19],AX
			  JMP                  FOUTH
OVERFLOW :    LEA                  DX,BUF4         ;����۸�Ϊ0
			  MOV                  AH,9
			  INT                  21H
			  MOV                  WORD PTR  [BX+19],0         ;�Ƽ���Ϊ0
FOUTH:
              CMP                  AL,100
			  
			  JS                   L1
			  LEA                  DX,BUF5
			  MOV                  AH,9
			  INT                  21H
			  MOV                  DL,041H       ;�Ƽ��ȴ���100,���A
			  MOV                  AH,2
			  INT                  21H
			  LEA                  DX,NEXT        ;�ó�����ʾ
		      MOV                  AH,10
			  INT                  21H
L1:           CMP                  AL,50
              JS                   L2
			  LEA                  DX,BUF5
			  MOV                  AH,9
			  INT                  21H
			  MOV                  DL,042H       ;�Ƽ��ȴ���50,���B
			  MOV                  AH,2
			  INT                  21H
			  LEA                  DX,NEXT        ;�ó�����ʾ
		      MOV                  AH,10
			  INT                  21H
L2:           CMP                  AL,10
              JS                   L3
			  LEA                  DX,BUF5
			  MOV                  AH,9
			  INT                  21H
			  MOV                  DL,043H  ;�Ƽ��ȴ���10�����C
			  MOV                  AH,2
			  INT                  21H
			  LEA                  DX,BUF5
			  MOV                  AH,9
			  INT                  21H
			  LEA                  DX,NEXT        ;�ó�����ʾ
		      MOV                  AH,10
			  INT                  21H
L3:           MOV                  DL,046H
              MOV                  AH,2
			  INT                  21H
			  LEA                  DX,NEXT        ;�ó�����ʾ
		      MOV                  AH,10
			  INT                  21H
ENDCODE:	  MOV                  AX, 4C00H
			  INT                  21H
		    CODE ENDS
			END START

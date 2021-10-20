#include <stdio.h>      //��׼IOͷ�ļ�
#include <stdlib.h>     /* Standard library */
#include <unistd.h>     //exex��forkͷ�ļ�
#include <signal.h>
#include <string.h>
#include <sys/wait.h>
#define BUF_SIZE 30//

int pid1, pid2;



void childProcess_handler(int signal) {
    kill(pid1, SIGUSR1);
    kill(pid2, SIGUSR2);
}


void sigusr_handler(int signal) {
    if (signal == SIGUSR1) {
        printf("Child Process 1 is Killed by Parent!\n");
        exit(0);
    }
    else if (signal == SIGUSR2) {
        printf("Child Process 2 is Killed by Parent!\n");
        exit(0);
    }
}

int main(void) {
    int pfd[2];//��������
    char read_buf[BUF_SIZE];//���������
    int read_num;
    int wait_tmp;
    signal(SIGINT, childProcess_handler);

//�����̴����ܵ�ʱ��ÿ��
//����Ҫ�ṩ�����ļ��������������ܵ�������һ���Թܵ�����д��������һ���Թܵ����ж��������Թܵ��Ķ�д��һ���IOϵͳ����һ
//�£�ʹ��write()����д�����ݣ�ʹ��read()�������ݡ�
//�ɹ�������0�����򷵻�-1�������������pipeʹ�õ������ļ�����������pfd[0]:���ܵ���pfd[1]:д�ܵ���
    if (pipe(pfd) == -1)
        printf("fail to create pipe\n");
    /**
     *������fork()ʱ����ִ�����¶�����
      ��ϵͳ����һ����PID
      �����ӽ��̣����Ƹ����̵�PCB����ø����̵����ݿռ䡢�ѡ�ջ����Դ�ĸ���
      �ڸ������з����ӽ��̵�PID�����ӽ����з���0
     *
     */
    switch (pid1 = fork()) {
        case -1:
            printf("First fork create fail\n");

            /* Child 1 for write data */
        case 0:
            /*SIG_ING �������SIGINT�ź� */
            signal(SIGINT, SIG_IGN);
            /*��׽SIGUSR1*/
            signal(SIGUSR1, sigusr_handler);
            if (close(pfd[0]) == -1)//�ӽ���1�رն���
                printf("Child 1 close read fd fail\n");

            int count = 1;
            char write_buf[BUF_SIZE];
            while(1) {
                sprintf(write_buf, "I send you %d times.\n", count);
                if (write(pfd[1], write_buf, strlen(write_buf)) != strlen(write_buf))
                {
                    printf("Child 1 write fail/n");
                }
                sleep(1);
                count++;
            }

            /* ������ */
        default:
            switch (pid2 = fork()) {
                case -1:
                    printf("Second fork create fail\n");
                    /* Child 2 for read data */
                case 0:
                    signal(SIGINT, SIG_IGN);
                    signal(SIGUSR2, sigusr_handler);
                    if (close(pfd[1] == -1))
                        printf("Child 2 close write fd fail\n");

                    while(1) {
                        read_num = read(pfd[0], read_buf, BUF_SIZE);
                        if (read_num == -1)
                            printf("Child 2 read fail\n");
                        if (read_num == 0)
                            continue;
                        if (write(STDOUT_FILENO, read_buf, read_num) != read_num)
                            printf("Child 2 write fail\n");
                    }
                default:
                    if (close(pfd[0]) == -1)
                        printf("Parent close pfd[0]\n");
                    if (close(pfd[1]) == -1)
                        printf("Parent close pfd[1]\n");
                    waitpid(pid1, &wait_tmp, 0);
                    waitpid(pid2, &wait_tmp, 0);
                    printf("Parent Process is Killed!\n");
                    exit(0);
            }
    }
}
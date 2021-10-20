#include <stdio.h>  
#include <Windows.h>
#include <process.h>


#define MAX 100

//3�������źŵ�
HANDLE mutex_road; //��·�źŵ�
HANDLE mutex_w;  //���������źŵ�
HANDLE mutex_n; //�ϱ������źŵ�

//�������飬���ڱ������г�������
HANDLE car[MAX];

//����������
int size = 0;

//��ʼ���ϱ�����Ͷ������򵽴�·�ڵĳ�����
int numOfWestAndEast= 0, numOfNorthAndSouth= 0;



//�������У����������Ӹ������򵽴�ĳ���
struct queue
{
	int num[MAX];  //�������
	int front; //ͷָ��
	int rear; //βָ��
	int count; //������ĳ�����

	queue() {
		front = rear = count = 0;
	}
	void push(int n) { //�������
		count++;
		rear = (rear + 1) % MAX;
		num[rear] = n;
	}
	int pop() {//��������
		count--;
		front = (front + 1) % MAX;
		return num[front];
	}
};

//4������ĳ�������
queue car_south;
queue car_east;
queue car_north;
queue car_west;

unsigned  __stdcall car_from_west(void* arg) {

	int current_west;
	if (car_west.count > 0)
		current_west = car_west.pop();
	printf("�� %d  ���������򵽴�·��\n", current_west);

	numOfWestAndEast++;
	if (numOfWestAndEast == 1)  //�������򵽴�·�ڵĵ�һ��������ռ��·
		WaitForSingleObject(mutex_road, 100);//����·
		
	ReleaseMutex(mutex_w);
	printf("�� %d  ���������򵽴�·�ڲ��Ӷ������뿪·��\n", current_west);
	WaitForSingleObject(mutex_w, 100);
	numOfWestAndEast--;
	if (numOfWestAndEast == 0) //�������������û�г��ˣ�����
		ReleaseMutex(mutex_road);
	ReleaseMutex(mutex_w);

	return NULL;
}

unsigned __stdcall car_from_east(void* arg) {

	int current_east;
	if (car_east.count > 0)
		current_east = car_east.pop();
	printf("�� %d  ���Ӷ����򵽴�·��\n", current_east);
	
	numOfWestAndEast++;
	if (numOfWestAndEast == 1)  //�������򵽴�·�ڵĵ�һ��������ռ��·
		WaitForSingleObject(mutex_road, 1000);//����·

	ReleaseMutex(mutex_w);
	printf("�� %d  ���Ӷ����򵽴�·�ڲ����������뿪·��\n", current_east);
	WaitForSingleObject(mutex_w, 1000);
	numOfWestAndEast--;
	if (numOfWestAndEast == 0) //�������������û�г��ˣ�����
		ReleaseMutex(mutex_road);
	ReleaseMutex(mutex_w);
	return NULL;
}

unsigned __stdcall car_from_south(void* arg) {

	int current_south;
	if (car_south.count > 0)
		current_south = car_south.pop();
	printf("�� %d  �����Ϸ��򵽴�·��\n", current_south);

	numOfNorthAndSouth++;
	if (numOfNorthAndSouth == 1)  //�ϱ����򵽴�·�ڵĵ�һ��������ռ��·
		WaitForSingleObject(mutex_road, 1000);//����·

	ReleaseMutex(mutex_n);
	printf("�� %d  �����Ϸ��򵽴�·�ڲ��ӱ������뿪·��\n", current_south);
	WaitForSingleObject(mutex_n, 1000);
	numOfNorthAndSouth--;
	if (numOfNorthAndSouth == 0) //����ϱ�������û�г��ˣ�����
		ReleaseMutex(mutex_road);
	ReleaseMutex(mutex_n);
	return NULL;
}

unsigned __stdcall car_from_north(void* arg) {

	int current_north;
	if (car_north.count > 0)
		current_north = car_north.pop();
	printf("�� %d  ���ӱ����򵽴�·��\n", current_north);


	WaitForSingleObject(mutex_n, 1000);
	numOfNorthAndSouth++;
	if (numOfNorthAndSouth == 1)  //�ϱ����򵽴�·�ڵĵ�һ��������ռ��·
		WaitForSingleObject(mutex_road, 1000);//����·

	ReleaseMutex(mutex_n);
	printf("�� %d  ���ӱ����򵽴�·�ڲ����Ϸ����뿪·��\n", current_north);
	WaitForSingleObject(mutex_n, 1000);
	numOfNorthAndSouth--;
	if (numOfNorthAndSouth == 0) //����ϱ�������û�г��ˣ�����
		ReleaseMutex(mutex_road);
	ReleaseMutex(mutex_n);
	return NULL;
}
int main(int argc, char** argv) {

	//��ʼ���źŵ�
	mutex_road = CreateMutex(NULL, FALSE, NULL);
	mutex_n = CreateMutex(NULL, FALSE, NULL);
	mutex_w = CreateMutex(NULL, FALSE, NULL);

	printf("\n��n��ʾ�ӱ����򵽴�·�ڵĳ�����s��ʾ�Ϸ���w��ʾ������e��ʾ����");
	printf("\n�����뵽��·�ڵĳ�����");

	char carIn[MAX];
	scanf_s("%s", carIn, 100);
	int len = strlen(carIn);

	//��������
	for (int i = 0; i < len; i++) {
		switch (carIn[i])
		{
		case 'w': {
			car_west.push(i + 1);
			car[i]=(HANDLE)_beginthreadex(NULL,0,&car_from_west,NULL,0,NULL);
			WaitForSingleObject(mutex_w, 100);
				break;
		}
		case 'e': {
			car_east.push(i + 1);
			car[i]=(HANDLE)_beginthreadex(NULL, 0, &car_from_east, NULL, 0, NULL);
			WaitForSingleObject(mutex_w, 100);
			break;
		}
		case 's': {
			car_south.push(i + 1);
			car[i] =(HANDLE) _beginthreadex(NULL, 0, &car_from_south, NULL, 0, NULL);
			WaitForSingleObject(mutex_n, 100);
				break;
		}
		case 'n': {
			car_north.push(i + 1);
			car[i] = (HANDLE)_beginthreadex(NULL, 0, &car_from_north, NULL, 0, NULL);
			WaitForSingleObject(mutex_n, 100);
				break;
		}
		default:
			break;
		}
	}
	WaitForMultipleObjects(len, car, TRUE, INFINITE);
	//���ٻ�����
	CloseHandle(mutex_n);
	CloseHandle(mutex_w);
	CloseHandle(mutex_road);
}
#include <stdio.h>  
#include <Windows.h>
#include <process.h>


#define MAX 100

//3个互斥信号灯
HANDLE mutex_road; //道路信号灯
HANDLE mutex_w;  //东西方向信号灯
HANDLE mutex_n; //南北方向信号灯

//进程数组，用于保存所有车辆进程
HANDLE car[MAX];

//车辆数计数
int size = 0;

//初始化南北方向和东西方向到达路口的车辆数
int numOfWestAndEast= 0, numOfNorthAndSouth= 0;



//车辆队列，用于描述从各个方向到达的车辆
struct queue
{
	int num[MAX];  //车辆序号
	int front; //头指针
	int rear; //尾指针
	int count; //队列里的车辆数

	queue() {
		front = rear = count = 0;
	}
	void push(int n) { //车辆入队
		count++;
		rear = (rear + 1) % MAX;
		num[rear] = n;
	}
	int pop() {//车辆出队
		count--;
		front = (front + 1) % MAX;
		return num[front];
	}
};

//4个方向的车辆队列
queue car_south;
queue car_east;
queue car_north;
queue car_west;

unsigned  __stdcall car_from_west(void* arg) {

	int current_west;
	if (car_west.count > 0)
		current_west = car_west.pop();
	printf("第 %d  车从西方向到达路口\n", current_west);

	numOfWestAndEast++;
	if (numOfWestAndEast == 1)  //东西方向到达路口的第一辆车先抢占道路
		WaitForSingleObject(mutex_road, 100);//锁道路
		
	ReleaseMutex(mutex_w);
	printf("第 %d  车从西方向到达路口并从东方向离开路口\n", current_west);
	WaitForSingleObject(mutex_w, 100);
	numOfWestAndEast--;
	if (numOfWestAndEast == 0) //如果东西方向上没有车了，解锁
		ReleaseMutex(mutex_road);
	ReleaseMutex(mutex_w);

	return NULL;
}

unsigned __stdcall car_from_east(void* arg) {

	int current_east;
	if (car_east.count > 0)
		current_east = car_east.pop();
	printf("第 %d  车从东方向到达路口\n", current_east);
	
	numOfWestAndEast++;
	if (numOfWestAndEast == 1)  //东西方向到达路口的第一辆车先抢占道路
		WaitForSingleObject(mutex_road, 1000);//锁道路

	ReleaseMutex(mutex_w);
	printf("第 %d  车从东方向到达路口并从西方向离开路口\n", current_east);
	WaitForSingleObject(mutex_w, 1000);
	numOfWestAndEast--;
	if (numOfWestAndEast == 0) //如果东西方向上没有车了，解锁
		ReleaseMutex(mutex_road);
	ReleaseMutex(mutex_w);
	return NULL;
}

unsigned __stdcall car_from_south(void* arg) {

	int current_south;
	if (car_south.count > 0)
		current_south = car_south.pop();
	printf("第 %d  车从南方向到达路口\n", current_south);

	numOfNorthAndSouth++;
	if (numOfNorthAndSouth == 1)  //南北方向到达路口的第一辆车先抢占道路
		WaitForSingleObject(mutex_road, 1000);//锁道路

	ReleaseMutex(mutex_n);
	printf("第 %d  车从南方向到达路口并从北方向离开路口\n", current_south);
	WaitForSingleObject(mutex_n, 1000);
	numOfNorthAndSouth--;
	if (numOfNorthAndSouth == 0) //如果南北方向上没有车了，解锁
		ReleaseMutex(mutex_road);
	ReleaseMutex(mutex_n);
	return NULL;
}

unsigned __stdcall car_from_north(void* arg) {

	int current_north;
	if (car_north.count > 0)
		current_north = car_north.pop();
	printf("第 %d  车从北方向到达路口\n", current_north);


	WaitForSingleObject(mutex_n, 1000);
	numOfNorthAndSouth++;
	if (numOfNorthAndSouth == 1)  //南北方向到达路口的第一辆车先抢占道路
		WaitForSingleObject(mutex_road, 1000);//锁道路

	ReleaseMutex(mutex_n);
	printf("第 %d  车从北方向到达路口并从南方向离开路口\n", current_north);
	WaitForSingleObject(mutex_n, 1000);
	numOfNorthAndSouth--;
	if (numOfNorthAndSouth == 0) //如果南北方向上没有车了，解锁
		ReleaseMutex(mutex_road);
	ReleaseMutex(mutex_n);
	return NULL;
}
int main(int argc, char** argv) {

	//初始化信号灯
	mutex_road = CreateMutex(NULL, FALSE, NULL);
	mutex_n = CreateMutex(NULL, FALSE, NULL);
	mutex_w = CreateMutex(NULL, FALSE, NULL);

	printf("\n以n表示从北方向到达路口的车辆，s表示南方，w表示西方，e表示东方");
	printf("\n请输入到达路口的车辆：");

	char carIn[MAX];
	scanf_s("%s", carIn, 100);
	int len = strlen(carIn);

	//创建进程
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
	//销毁互斥量
	CloseHandle(mutex_n);
	CloseHandle(mutex_w);
	CloseHandle(mutex_road);
}
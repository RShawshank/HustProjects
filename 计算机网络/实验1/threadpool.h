#ifndef THREADPOOL_H
#define THREADPOOL_H
#include<exception>
#include<windows.h>
#include <list>
#include <queue>
#include <memory>
using namespace std;
#define THRESHOLE_OF_WAIT_TASK 20
typedef int(*TaskFun)(PVOID param);//������
typedef void(*TaskCallbackFun)(int result);//�ص�����
class ThreadPool
{

private:
	class Thread
	{
	public: Thread(ThreadPool* threadPool);
		  ~Thread();
		  BOOL isBusy();
		  void ExecuteTask(TaskFun task, PVOID param, TaskCallbackFun taskcallbackfun);//ִ������
	private:
		ThreadPool* threadPool;//�����̳߳�
		BOOL busy;//�����Ƿ���ִ��
		BOOL exit;//�Ƿ��˳�
		HANDLE thread;//�߳̾��
		TaskFun task;//��ִ�е�����
		PVOID param;//�������
		TaskCallbackFun taskcb;//�ص�������
		static unsigned int __stdcall ThreadProc(PVOID PM);//�̺߳���

	};
	//�߳��ٽ���
	class CriticalSectionLock
	{
	private:
		CRITICAL_SECTION  CS;//�ٽ���
	public:
		CriticalSectionLock()
		{
			InitializeCriticalSection(&CS);
		}
		~CriticalSectionLock()
		{
			DeleteCriticalSection(&CS);
		}
		void Lock()
		{
			EnterCriticalSection(&CS);
		}
		void UnLock()
		{
			LeaveCriticalSection(&CS);
		}
	};
	class WaitTask
	{
	public:
		TaskFun    task;                    // Ҫִ�е�����
		PVOID      param;                    // �������
		TaskCallbackFun taskCb;            // �ص�������
		BOOL        bLong;                        // �Ƿ�ʱ������
		WaitTask(TaskFun TASK, PVOID param, TaskCallbackFun TASKCB, BOOL LONG)
		{
			this->bLong = LONG;
			this->param = param;
			this->task = TASK;
			this->taskCb = TASKCB;
		}
		~WaitTask()
		{
			task = NULL;
			taskCb = NULL;
			bLong = FALSE;
			param = NULL;
		}
	};

	HANDLE dispatchThread;//�ַ������߳�
	HANDLE stopEvent;//֪ͨ�߳��˳���ʱ��
	HANDLE completionPort;//��ɶ˿�
	size_t maxNumOfThread;//�̳߳��������߳���
	size_t minNumOfThread;//�̳߳�����С���߳���
	size_t NumOfLongFun;
	CriticalSectionLock IdleThreadLock;//�����߳��б���
	list<Thread*> IdleThreadList;//�����߳��б�
	CriticalSectionLock BusyThreadLock;//æµ�߳��б���
	list<Thread*>BusyThreadList;//æµ�߳��б�
	CriticalSectionLock WaitTaskLock;
	list<WaitTask*> waitTaskList;//�����б�

	size_t getIdleThreadNum()
	{
		return IdleThreadList.size();
	}

	size_t getBusyThreadNum()
	{
		return BusyThreadList.size();
	}

	size_t getCurNumOfThread()
	{
		return getBusyThreadNum() + getIdleThreadNum();
	}

	size_t getMinNumOfThread()
	{
		return minNumOfThread;
	}

	size_t getMaxNumOfThread()
	{
		return maxNumOfThread - NumOfLongFun;
	}

	void SetMaxNumOfThread(size_t size)
	{
		if (size < NumOfLongFun)
		{
			this->maxNumOfThread = size+NumOfLongFun;
		}
		else
		{
			maxNumOfThread = size;
		}
	}
	void SetMinNumOfThread(size_t size)
	{
		this->minNumOfThread = size;
	}

	//���������߳�
	void CreateIdleThread(size_t size);
	void DeleteIdleThread(size_t size);
	Thread* GetIdleThread();
	//��æµ�̼߳��뵽�����߳���
	void MoveBusyThreadToIdleList(Thread* busyThread);
	//���̼߳���æµ�߳���
	void MoveThreadToBusyList(Thread* thread);
	//�����������ȡ������
	void GetTaskExcute();
	WaitTask* GetTask();
	
	enum WAIT_OPERATION_TYPE
	{
		GET_TASK,EXIT
	};
	
	//�������б���ȡ������̺߳���
	static unsigned int __stdcall GetTaskThreadProc(PVOID PM)
	{
		ThreadPool* threadPool = (ThreadPool*)PM;
		BOOL bRet = FALSE;
		DWORD dwbytes = 0;
		WAIT_OPERATION_TYPE OpType;
		OVERLAPPED* ol;
		while(WAIT_OBJECT_0!=WaitForSingleObject(threadPool->stopEvent,0))
		{
			BOOL bRet = GetQueuedCompletionStatus(threadPool->completionPort, &dwbytes, (PULONG_PTR)&OpType, &ol, INFINITE);
			//�յ��˳���־
			if (EXIT == (DWORD)OpType)
				break;
			else if (GET_TASK == (DWORD)OpType)
				threadPool->GetTaskExcute();
		}
		return 0;
	}
public:
	ThreadPool(size_t minNumOfThread=2, size_t maxNumOfThread=10);
	~ThreadPool();
	BOOL QueueTaskItem(TaskFun TASK, PVOID param, TaskCallbackFun TASKCB = NULL, BOOL longFun = false);
};

#endif

#include "RdtSender.h"
#include<deque>
using namespace std;
struct sendPKT
{
	Packet packet;
	bool acpt;//��ʶ�Ƿ��յ������ڻ���
};
class SRRdtSender:public RdtSender
{
private:
	int seqRange;//��ŷ�Χ
	int windowLen;//���ڳ���
	int baseSeq;//�����
	int NextSeqNum;//��һ���ȴ����Ͱ������
	deque<sendPKT>* window;
public:
	bool getWaitingState();
	bool send(Message&);
	void receive(Packet&);
	void timeoutHandler(int);
public:
	SRRdtSender();
	~SRRdtSender();
};
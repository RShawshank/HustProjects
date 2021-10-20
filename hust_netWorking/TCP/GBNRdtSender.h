#include"RdtSender.h"
#include<deque>
using namespace std;
class GBNRdtSender :public RdtSender
{
private:
	int seqRange;//��ŷ�Χ
	int windowLen;//���ڳ���
	int baseSeq;//�����
	int NextSeqNum;//�¸��ȴ����Ͱ������
	int DuplicateACK;//�����ack��ʵ�ֿ����ش�
	deque<Packet>* window;//����ʵ��GBN�еĴ���
public:
	bool getWaitingState();
	bool send(Message&);
	void receive(Packet&);
	void timeoutHandler(int);
public:
	GBNRdtSender();
	virtual ~GBNRdtSender();
};
#include"RdtReceiver.h"
#include<deque>
using namespace std;
struct receiverPKT
{
	Message message;
	bool acpt;
};
class SRRdtReceiver :public RdtReceiver
{
private:
	int windowLen;
	int baseSeq;
	Packet ACKPKT;
	deque<receiverPKT>* window;
public:
	void receive(Packet&);//���ձ��ģ�����NetworkService����	
public:
	SRRdtReceiver();
	~SRRdtReceiver();
};

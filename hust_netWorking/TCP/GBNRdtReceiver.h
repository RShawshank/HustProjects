#include "RdtReceiver.h"
class GBNRdtReceiver:public RdtReceiver
{
private:
	int WantSeqNum;//�ڴ������
	Packet lastACKPKT;//����յ���ACK	
public:
	void receive(Packet&);//���ձ��ģ�����NetworkService����	
public:
	GBNRdtReceiver();
	virtual ~GBNRdtReceiver();
};

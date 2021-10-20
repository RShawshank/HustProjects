#include "stdafx.h"
#include "Global.h"
#include "SRRdtReceiver.h"
#include<fstream>
ofstream Receiverlog("SRReceiverlog.txt");
SRRdtReceiver::SRRdtReceiver() : windowLen(4), baseSeq(0)
{
	ACKPKT.acknum = 0;
	ACKPKT.checksum = pUtils->calculateCheckSum(ACKPKT);
	ACKPKT.seqnum = -1;
	//��ʼ������,��ʼ�ձ��ִ�������4��λ��
	window = new deque<receiverPKT>;
	for (int i = 0; i < windowLen; i++)
	{
		receiverPKT blankPKT;
		blankPKT.acpt = false;
		window->push_back(blankPKT);
	}
}
SRRdtReceiver::~SRRdtReceiver()
{
	delete window;
}
void SRRdtReceiver::receive(Packet& packet)
{
	//���У���
	int checksum = pUtils->calculateCheckSum(packet);
	int offset = (packet.seqnum-baseSeq+8)%8;
	//�����û����
	if (checksum == packet.checksum)
	{
		Receiverlog<<"���ܷ����յ��ı��ĵ����Ϊ��"<<packet.seqnum<<endl;
		if (offset < windowLen)
		{
			window->at(offset).acpt = true;//�յ���Ų�����ȷ
		   //���ڻ���
			memcpy(window->at(offset).message.data, packet.payload, sizeof(packet.payload));
		}
		//����ACK�����ͷ�
		ACKPKT.acknum = packet.seqnum;
		ACKPKT.checksum = pUtils->calculateCheckSum(ACKPKT);
		Receiverlog << "���շ����͵�ACK���Ϊ��" << ACKPKT.acknum << endl;
		pns->sendToNetworkLayer(SENDER, ACKPKT);

		//�����ڵ�����ߵı����Ѿ�ȷ���յ��ˣ��ƶ����ڲ����ύ����
		while (window->begin()->acpt == true)
		{
			Message message;
			memcpy(message.data, window->begin()->message.data, sizeof(window->begin()->message.data));
			pns->delivertoAppLayer(RECEIVER, message);
			//Receiverlog << message.data <<endl;
			window->pop_front();
			receiverPKT blankPKT;
			blankPKT.acpt = false;
			window->push_back(blankPKT);//���䴰��
			baseSeq++;
			baseSeq %= 8;
		}
	}
	else
			Receiverlog << "���շ��յ��ı��ĵļ���ʹ��������Ϊ��" << packet.seqnum << endl;	
}
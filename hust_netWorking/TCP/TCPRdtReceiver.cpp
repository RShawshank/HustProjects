#include "stdafx.h"
#include "Global.h"
#include "GBNRdtReceiver.h"
#include<fstream>
ofstream Receiverlog("Receiverlog.txt");
GBNRdtReceiver::GBNRdtReceiver() :WantSeqNum(0)
{
	lastACKPKT.acknum = (WantSeqNum + 7) % 8;//ǰһ����ű�ʾ�յ���ACK,8�Ǵ��ڴ�С
	lastACKPKT.checksum = pUtils->calculateCheckSum(lastACKPKT);
	lastACKPKT.seqnum = -1;
}
GBNRdtReceiver::~GBNRdtReceiver()
{
}
void GBNRdtReceiver::receive(Packet& packet)
{
	//���У���
	int checksum = pUtils->calculateCheckSum(packet);
	//�����û����ͬʱ���շ�ϣ���յ�������Ǹ����
	if (checksum == packet.checksum && this->WantSeqNum == packet.seqnum)
	{
		Message message;
		memcpy(message.data, packet.payload, sizeof(packet.payload));
		pns->delivertoAppLayer(RECEIVER, message);//���ݸ�Ӧ�ò�
		lastACKPKT.acknum = packet.seqnum;//��ȷ���������Ϊ���ݰ����
		lastACKPKT.checksum = pUtils->calculateCheckSum(lastACKPKT);
		Receiverlog << "���շ����͵�ACK���Ϊ��" << lastACKPKT.acknum << endl;
		pns->sendToNetworkLayer(SENDER, lastACKPKT);
		this->WantSeqNum++;
		this->WantSeqNum %= 8;
	}
	else
	{
		//����Ͳ���
		if (checksum != packet.checksum)
		{
			Receiverlog << "���շ��յ��ı��ĵļ���ʹ���" << endl;
		}
		//����ʧ��
		else
		{
			Receiverlog << "���շ��յ��ı�����ʧ���ģ������Ϊ��" << packet.seqnum << endl;
		}
		//��������յ���ACK�����ͷ�
		Receiverlog << "���շ���������յ���ACK�����ͷ���ACKΪ��" << lastACKPKT.acknum << endl;
		pns->sendToNetworkLayer(SENDER, lastACKPKT);
	}
}
#include"SRRdtSender.h"
#include"Global.h"
#include"stdafx.h"
#include<fstream>
ofstream Senderlog("srsenderlog.txt");
SRRdtSender::SRRdtSender() :seqRange(8), windowLen(4), NextSeqNum(0), baseSeq(0)
{
	window = new deque<sendPKT>;
}
SRRdtSender::~SRRdtSender()
{
	delete window;
}
bool SRRdtSender::getWaitingState()
{
	return(((window->size()) == windowLen) ? true : false);
}
bool SRRdtSender::send(Message& message)
{
	if (this->getWaitingState())//���ͷ����ȴ�ȷ�ϻ��߷��ʹ�������
		return false;
	sendPKT NewPacket;
	NewPacket.acpt = false;
	NewPacket.packet.acknum = -1;//ȷ�Ϻţ�����
	NewPacket.packet.seqnum = this->NextSeqNum;//���
	memcpy(NewPacket.packet.payload, message.data, sizeof(message.data));
	//Senderlog << message.data << endl;
	NewPacket.packet.checksum = pUtils->calculateCheckSum(NewPacket.packet);//У���
	window->push_back(NewPacket);//���µİ����봰��
	Senderlog << "\n" << "���ͷ��������ݱ��ģ����Ϊ��" << NewPacket.packet.seqnum << endl;
		pns->startTimer(SENDER, Configuration::TIME_OUT, NewPacket.packet.seqnum);//���ͷ�������ʱ��
	pns->sendToNetworkLayer(RECEIVER, NewPacket.packet);
	(this->NextSeqNum)++;
	this->NextSeqNum %= this->seqRange;
	return true;
}
void SRRdtSender::receive(Packet& ACKPKT)
{
	int checksum = pUtils->calculateCheckSum(ACKPKT);//����У���
	int offset = (ACKPKT.acknum - baseSeq+seqRange) % seqRange;//��ʱack���������ŵĲ�ֵ
	//�������Ȳ���ֻ���ڴ����ڵ�ACK
	if (checksum == ACKPKT.checksum && offset < window->size() && window->at(offset).acpt==false )
	{
		pns->stopTimer(SENDER, ACKPKT.acknum);//���ͷ�ֹͣ��ʱ��
		window->at(offset).acpt = true;//��ʶ������Ѿ����յ�
		Senderlog << "���ͷ��յ�ACKΪ��" << ACKPKT.acknum << endl;
		Senderlog << "���ڵ������Ϊ��" << baseSeq;
		Senderlog << "���ڵ���һ�������Ͱ������Ϊ��" << NextSeqNum;
		Senderlog << "���ڵĴ�СΪ��" << window->size() << endl;
		//���յ��İ�Ϊ��������ߵİ�ʱ�������ƶ�
		while (window->size()&&window->begin()->acpt==true)
		{
			baseSeq++;
			baseSeq %= seqRange;
			window->pop_front();
		}
		Senderlog << "���ڵ������Ϊ��" << baseSeq;
		Senderlog << "���ڵ���һ�������Ͱ������Ϊ��" << NextSeqNum;
		Senderlog << "���ڵĴ�СΪ��" << window->size() << endl;
	}
}
void SRRdtSender::timeoutHandler(int seqnum)
{
	//Senderlog << seqnum << endl;
	pns->stopTimer(SENDER, seqnum);
	Senderlog << "��ʱ!�������·��ʹ����ڵ����ݱ��ģ�";
	Senderlog << "��ʱ�Ĵ��ڵ������Ϊ��" << baseSeq << ",��һ�������Ͱ������Ϊ��" << NextSeqNum << ",���ڴ�С��" << window->size() << endl;
	int offset = (seqnum - baseSeq+seqRange) % seqRange;
	//����ʱ�İ��ڴ����ڣ������·���
	if (offset< window->size())
	{
		pns->sendToNetworkLayer(RECEIVER, window->at(offset).packet);
		pns->startTimer(SENDER, Configuration::TIME_OUT, seqnum);
	}
}
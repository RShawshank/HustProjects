#include "stdafx.h"
#include "Global.h"
#include "GBNRdtSender.h"
#include <fstream>
ofstream Senderlog("senderlog.txt");
GBNRdtSender::GBNRdtSender() :seqRange(8), windowLen(4), baseSeq(0), NextSeqNum(0)
{
	//��ʼ������
	window = new deque<Packet>;
}
GBNRdtSender::~GBNRdtSender()
{
	delete window;
}
//����RdtSender�Ƿ��ڵȴ�״̬��������ͷ����ȴ�ȷ�ϻ��߷��ʹ�������������true
bool GBNRdtSender::getWaitingState()
{
	return(( (window->size()) == windowLen)? true : false);
}
//����Ӧ�ò�������Message����NetworkService����,������ͷ��ɹ��ؽ�Message���͵�����㣬����true;�����Ϊ���ͷ����ڵȴ�ȷ��״̬���ʹ����������ܾ�����Message���򷵻�false
bool GBNRdtSender::send(Message& message) 
{
	if (this->getWaitingState())//���ͷ����ȴ�ȷ�ϻ��߷��ʹ�������
		return false;
	Packet NewPacket;
	NewPacket.acknum = -1;//ȷ�Ϻţ�����
	NewPacket.seqnum = this->NextSeqNum;//���
	memcpy(NewPacket.payload, message.data,sizeof(message.data));
	NewPacket.checksum = pUtils->calculateCheckSum(NewPacket);//У���
	window->push_back(NewPacket);//���µİ����봰��
	Senderlog<<"\n"<<"���ͷ��������ݱ��ģ����Ϊ��"<< NewPacket.seqnum<<endl;
	if (this->baseSeq== this->NextSeqNum)//�״η�
	{
		pns->startTimer(SENDER, Configuration::TIME_OUT, NewPacket.seqnum);//���ͷ�������ʱ��
	}
	pns->sendToNetworkLayer(RECEIVER, NewPacket);
	(this->NextSeqNum)++;
	this->NextSeqNum %= this->seqRange;
	return true;
}
//����ȷ��Ack������NetworkService����	
void GBNRdtSender::receive(Packet &ACKPKT)
{
	int checksum = pUtils->calculateCheckSum(ACKPKT);//����У���
	//�������Ȳ�����Ų���֮ǰ��Ӧ�յ���ACK��Ű�
	if (checksum == ACKPKT.checksum && ACKPKT.acknum != (this->baseSeq + 7) % 8)
	{
		pns->stopTimer(SENDER, baseSeq);//���ͷ�ֹͣ��ʱ��
		Senderlog << "���ͷ��յ�ACKΪ��" << ACKPKT.acknum << endl;
		Senderlog << "���ڵ������Ϊ��" << baseSeq;
		Senderlog << "      ���ڵ���һ�������Ͱ������Ϊ��" << NextSeqNum;
		Senderlog << "      ���ڵĴ�СΪ��" << window->size() << endl;
		while (baseSeq != (ACKPKT.acknum + 1) % seqRange)
		{
			window->pop_front();
			baseSeq++;
			baseSeq %= seqRange;
		}
		Senderlog << "���ͷ��յ�ACKΪ��" << ACKPKT.acknum << endl;
		Senderlog << "���ں�������Ϊ��" << baseSeq ;
		Senderlog << "       ���ڵ���һ�������Ͱ������Ϊ��" << NextSeqNum;
		Senderlog << "       ���ڵĴ�СΪ��" << window->size() << endl;
		//����û�з�����ʱ
		if (window->size())
		{//�����Ե�һ������ʼ��ʱ
			pns->startTimer(SENDER, Configuration::TIME_OUT, window->front().seqnum);
		}
	}
}
//Timeout handler������NetworkService����
void GBNRdtSender::timeoutHandler(int seqnum)
{
	Senderlog << "��ʱ!�������·��ʹ����ڵ����ݱ��ģ�";
	Senderlog << "��ʱ�Ĵ��ڵ������Ϊ��" << baseSeq << ",��һ�������Ͱ������Ϊ��" << NextSeqNum << ",���ڴ�С��" << window->size() << endl;
	pns->stopTimer(SENDER, seqnum);
	for (deque<Packet>::iterator iterator = window->begin(); iterator != window->end(); iterator++)
		pns->sendToNetworkLayer(RECEIVER, *iterator);//���·������ݰ�
	pns->startTimer(SENDER, Configuration::TIME_OUT, seqnum);
}

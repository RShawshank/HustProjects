#include <process.h>
#include <WinSock2.h>
#include <fstream>
#include <sstream>
#include "Webserver.h"
#include <iostream>
#include<WS2tcpip.h>
int WebServer::closeAll = 0;
WebServer::WebServer(char* ip,short port)
{
	try
	{
		StatusCodes = readMap("config/StatusCodes.txt");
		ContentTypes = readMap("config/ContentTypes.txt");
		StatusPages = readMap("config/StatusHtml.txt");
		if (ContentTypes.empty()) throw "Missing contentypes";
		else if (StatusCodes.empty()) throw "Missing StatusCodes";
		else if (StatusPages.empty()) throw "Missing StatusPages";
		//����Э��ջ
		WebServer::startWSA();
		
		//������������ַ��ip+�˿ں�
		sockaddr_in addr;
		addr.sin_family = AF_INET;//��ַ��,IPV4
		addr.sin_port = htons(port);//�˿ں�
		inet_pton(AF_INET, ip,(void*)&addr.sin_addr);//IP��ַ
		memset(addr.sin_zero, 0, 8);//���sin_zeroΪ0
		//����һ�������׽��֣����ڼ����û�����
		Sock_ = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
		if (Sock_ == INVALID_SOCKET) throw "INVALID SOCKET";

		//�󶨼����׽��ֺͷ�������ַ
		if (bind(Sock_, (LPSOCKADDR)&addr, sizeof(addr)) != 0)
		{
			closesocket(Sock_);
			WSAGetLastError();
			throw"INVALID SOCKET";
		}
		//�����ͻ���
		listen(Sock_, SOMAXCONN);
		//���ܺʹ����µ�����
		unsigned ret=0;
		int count = 0;
		while(true)
		{
			Socket* s = this->Accept();
			//�����̺߳���ΪRequest������Ϊ*S���̣߳�retΪ�߳�Id
		HANDLE handle = (HANDLE)_beginthreadex(0, 0,(unsigned int(__stdcall*)(void*))Request, (void*)s, 0, &ret);
		std::cout << "�߳�ID��"<<ret<<std::endl;
		count++;
			if(count>10)
			{
				break;
			}
			
			if (handle==NULL)
			{
				std::cout << "�����߳�ʧ��" << std::endl;
			}
		}
	}
	catch (char* e)
	{
		std::cout << e << "\n";
		system("pause");
	}
}
WebServer::~WebServer()
{
	WebServer::stopWSA();
}
void WebServer::stopWSA()
{
	WSACleanup();//�����Socket��İ󶨲��ͷ�Socket����ռ�õ���Դ
}

unsigned WebServer::Request(void* ptrSock)
{
	Handler request_handler((reinterpret_cast<Socket*>(ptrSock)));
	return 0;
}
Socket* WebServer::Accept()
{
	struct sockaddr_in ConnAddrs;
	//�����г�ʼ���Ļ����õ��Ŀͻ���IP��ַ��204.204.204.204
	ConnAddrs.sin_port = htons(80);
	ConnAddrs.sin_addr.S_un.S_addr = htonl(INADDR_ANY);
	ConnAddrs.sin_family = AF_INET;
	SOCKET newSocket = accept(Sock_, (struct sockaddr*)&ConnAddrs, 0);
	if (newSocket == INVALID_SOCKET) throw"INVALID_SOCKET";
	int port = ntohs(ConnAddrs.sin_port);
	char sendBuf[20] = { '\0' };
	inet_ntop(AF_INET, &(ConnAddrs.sin_port), sendBuf, 16);
	std::cout<<"�ͻ��˵ĵ�ַΪ��" <<sendBuf <<"  �˿ں�Ϊ��"<<port<<std::endl;
	Socket* s = new Socket(newSocket);
	return s;
}
void WebServer::startWSA()
{
	WSADATA wsadata;
	if (WSAStartup(MAKEWORD(2,0),&wsadata)==0)
	{
		if(!(LOBYTE(wsadata.wVersion)>=2))
		{
			throw"VERSION NOT SUPPORTED";
		}
	}
	else
	{
		throw"Startup failed";
	}
}

string_map WebServer::readMap(std::string f)
{
	string_map vars;
	std::string tempKey, tempItem;
	std::ifstream file(f.c_str());
	if (file)
	{
		file >> tempKey;  //��ȡkeyֵ
		getline(file, tempItem, '\n');//��ȡvalue
		tempItem = tempItem.substr(1, tempItem.length() - 1);
		while (!file.eof())
		{
			vars.insert(std::pair<std::string, std::string>(tempKey, tempItem));
			file >> tempKey;
			getline(file, tempItem, '\n');
			tempItem = tempItem.substr(1, tempItem.length() - 1);
		}
	}
	return vars;
}




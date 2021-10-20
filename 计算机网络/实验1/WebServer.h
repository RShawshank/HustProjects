#ifndef WIN32_LEAN_AND_MEAN//����ͷ�ļ�
#define WIN32_LEAN_AND_MEAN
#endif

#ifndef WEBSERVER_H
#define WEBSERVER_H
#pragma comment(lib, "Ws2_32.lib")
#include <string>
#include <map>
#include <WinSock2.h>
typedef std::map<std::string, std::string> string_map;
class Socket;
class Handler;
class WebServer
{
private:
	static string_map ContentTypes;     //��ҳ����
	static string_map StatusCodes;      //��ҳ״̬��
	static string_map StatusPages;     //HTML code
	SOCKET Sock_;
	static int closeAll;//�رշ�����
	friend class Socket;              
	friend class Handler;

	static unsigned   __stdcall   Request(void* ptrSock); //����һ�������߳�
	Socket* Accept();           //����һ���µ�socket          
	string_map                  readMap(std::string f); 
	static void                 startWSA();             
	static void                 stopWSA();             
public:
	WebServer(char* ip,short port);
	~WebServer();
	void setcloseAll()
	{
		closeAll = 0;
	}
};
//�׽�����
class Socket
{
private:
	SOCKET sock_;
public:
	Socket(SOCKET s);
	~Socket();

	void close();  //�ر�����
	std::string  rxData(); //��������
	std::string  rxLine();//�����ַ���
	void txData(char* data, int size);
	void txLine(std::string line);
};
class Handler
{
private:
	struct Page
	{
		Socket* sock_;
		std::string path;  //request��path
		std::string status;//OK||NO found
		std::string contentTpye;
		struct Data
		{
			int size;
			char* content;
		}data;
	}page;
	std::string parsePath(std::string l);
	std::string parseContentType(std::string p);
	void constData(std::string s);
	void readData(std::string f);
	void createPage(std::string l);
	void parseData(std::string d);
	void sendPage();
	void setPage404(Page::Data& p);
public:
	Handler(Socket* s);
	~Handler();
	
};
#endif
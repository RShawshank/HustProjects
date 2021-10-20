#include "Webserver.h"
#include <fstream>
#include <string>
#include <map>
#include <sstream>
#include <iostream>
using namespace std;
string_map WebServer::ContentTypes;
string_map WebServer::StatusCodes;
string_map WebServer::StatusPages;

//��������
Handler::Handler(Socket* s)
{
	string line;
	page.data.content = NULL;
	page.sock_ = s;
	line = page.sock_->rxLine();//��ȡ������
	cout << "http �������У� "<<line << endl;
	if (!line.empty()&&line.find("GET")==0)
	{
		createPage(line);
		sendPage();
	}
}
Handler::~Handler()
{
	delete[] page.data.content;
}


//��Ӧ����
void Handler::sendPage()
{
	stringstream s_size;
	s_size << page.data.size;
	std::cout<<"���ͱ��Ķ����ݣ�"<<std::endl;
	page.sock_->txLine(WebServer::StatusCodes[page.status] + "\r");
	page.sock_->txLine("Connection: close\r");
	page.sock_->txLine("Content-Type: " + page.contentTpye + "\r");
	page.sock_->txLine("Content-Length: " + s_size.str()+"\r");
	page.sock_->txLine("\r");
	page.sock_->txData(page.data.content, page.data.size);
}

//��get���󴴽�����
void Handler::createPage(std::string l)
{
	page.path = parsePath(l);
	readData(page.path);
	if (page.data.content!=NULL)
	{
		page.status = "200";
		page.contentTpye = parseContentType(page.path);
		
	}
	else
	{
		if (page.path=="favicon.ico")//����ʾ��ǩ����ͼ��
		{
			page.status = "204";
		}
		else
		{
			page.status = "404";
			page.path = "404.html";
		}
		constData(page.status);
		page.contentTpye = "text/html";
	}
}

//��http�������н���·��
std::string Handler::parsePath(std::string l)
{
	string::size_type start, stop;//string����
	string result;
	start = l.find_first_of(" ");
	stop = l.find(" ", start + 1);
	result = l.substr((start + 1), (stop - start - 1));
	if (result=="/")
	{
		result = "index.html";
	}
	else
	{
		result = l.substr((start + 2), (stop - start - 2));
	}
	return result;
}


std::string Handler::parseContentType(std::string p)
{
	string temp, type = "";
	string_map::iterator it;
	string::size_type start, stop = p.length();
	start = p.find_last_of(".");
	temp = p.substr(start + 1, stop - 1);
	it = WebServer::ContentTypes.find(temp);
	if (it != WebServer::ContentTypes.end())
		type = it->second;
	else
	{
		type = "application/unknown";
	}
	return  type;
}


void Handler::readData(std::string f)
{
	int end, begin;
	page.data.size = 0;
	page.data.content = NULL;
	ifstream file(f.c_str(), ios::binary);//�����Ʒ�ʽ��
	if (file)
	{
		file.seekg(0, ios::beg);
		begin = (int)file.tellg();
		file.seekg(0, ios::end);
		end = (int)file.tellg();
		page.data.size = (end - begin);
		file.seekg(0, ios::beg);
		if (page.data.content != NULL)
		{
			delete[] page.data.content;
		}
		page.data.content = new char[page.data.size + 1];
		file.read(page.data.content, page.data.size);
		page.data.content[page.data.size] = '\0';

	}
	else
	{
		cout << "readData error" << endl;
	}
}
//��ȡ������Ӧ״̬��
void Handler::constData(std::string s)
{
	string_map::iterator it;
	it = WebServer::StatusPages.find(s);
	if (it!=WebServer::StatusPages.end())
	{
		page.data.size = it->second.size();
		if (page.data.content!=NULL)
		{
			delete[] page.data.content;
		}
		page.data.content =new char[page.data.size + 1];
		strcpy_s(page.data.content,strlen(it->second.c_str())+1, it->second.c_str());
	}
	else
	{
		
	}
}





